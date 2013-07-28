/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2013 GK Software AG
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.otequinox.TransformerPlugin.log;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBinding.BaseBundle;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBinding.TeamBinding;
import org.eclipse.objectteams.internal.osgi.weaving.Util.ProfileKind;
import org.eclipse.objectteams.otequinox.Constants;
import org.eclipse.objectteams.otequinox.TransformerPlugin;
import org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;
import org.osgi.framework.hooks.weaving.WovenClassListener;
import org.osgi.framework.namespace.PackageNamespace;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.resource.Wire;


/**
 * This class integrates the OT/J weaver into OSGi using the standard API {@link WeavingHook}.
 * <p>
 * Additionally, we listen to events of woven classes changing to state {@link WovenClass#DEFINED}:
 * </p>
 * <ul>
 * <li>Given that {@link AspectBindingRegistry#addDeferredTeamClasses} was used to record
 * teams that could not be instantiated due to some required class being reported
 * as {@link NoClassDefFoundError}.</li>
 * <li>Assuming further that this error happened because the required class was in the process
 * of being loaded further down the call stack.</li>
 * <li>If later one of the not-found classes has been defined we use that trigger to
 * re-attempt instantiating the dependent team(s).</li>
 * </ul>
 */
public class OTWeavingHook implements WeavingHook, WovenClassListener {

	enum WeavingReason { None, Aspect, Base }
	
	/** Interface to data about aspectBinding extensions. */
	private @NonNull AspectBindingRegistry aspectBindingRegistry = new AspectBindingRegistry();
	
	/** Map of trip wires to be fired when a particular base bundle is loaded. */
	private @NonNull HashMap<String, BaseBundleLoadTrigger> baseTripWires = new HashMap<>();

	/** Set of classes for which processing has started but which are not yet defined in the class loader. */
	private @NonNull Set<String> beingDefined = new HashSet<>();

	/** Records of teams that have been deferred due to unresolved class dependencies: */
	private @NonNull List<WaitingTeamRecord> deferredTeams = new ArrayList<>();

	/** Call-back once the extension registry is up and running. */
	public void activate(BundleContext bundleContext, ServiceReference<IExtensionRegistry> serviceReference) {
		loadAspectBindingRegistry(bundleContext, serviceReference);
		TransformerPlugin.getDefault().registerAspectBindingRegistry(this.aspectBindingRegistry);
	}

	// ====== Aspect Binding: ======

	@SuppressWarnings("deprecation")
	private void loadAspectBindingRegistry(BundleContext context, ServiceReference<IExtensionRegistry> serviceReference) {
		org.osgi.service.packageadmin.PackageAdmin packageAdmin = null;
		
		ServiceReference<?> ref= context.getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class.getName());
		if (ref!=null)
			packageAdmin = (org.osgi.service.packageadmin.PackageAdmin)context.getService(ref);
		else
			log(IStatus.ERROR, "Failed to load PackageAdmin service. Will not be able to handle fragments.");

		IExtensionRegistry extensionRegistry = context.getService(serviceReference);
		if (extensionRegistry == null)
			log(IStatus.ERROR, "Failed to acquire ExtensionRegistry service, cannot load aspect bindings.");
		else
			aspectBindingRegistry.loadAspectBindings(extensionRegistry, packageAdmin, this);
	}

	// ====== Base Bundle Trip Wires: ======
	
	/**
	 * Callback during AspectBindingRegistry#loadAspectBindings():
	 * Set-up a trip wire to fire when the mentioned base bundle is loaded.
	 */
	void setBaseTripWire(@SuppressWarnings("deprecation") @Nullable org.osgi.service.packageadmin.PackageAdmin packageAdmin, @NonNull String baseBundleId, BaseBundle baseBundle) 
	{
		if (!baseTripWires.containsKey(baseBundleId))
			baseTripWires.put(baseBundleId, new BaseBundleLoadTrigger(baseBundleId, baseBundle, aspectBindingRegistry, packageAdmin));
	}

	/** Check if the given base bundle / base class mandate any loading/instantiation/activation of teams. */
	void triggerBaseTripWires(@Nullable String bundleName, @NonNull WovenClass baseClass) {
		BaseBundleLoadTrigger activation = baseTripWires.get(bundleName);
		if (activation != null) {
			activation.fire(baseClass, beingDefined, this);
			if (activation.isDone())
				baseTripWires.remove(bundleName);
		}
	}

	// ====== Main Weaving Entry: ======

	@Override
	public void weave(WovenClass wovenClass) {
		beingDefined.add(wovenClass.getClassName());

		try {
			BundleWiring bundleWiring = wovenClass.getBundleWiring();
			String bundleName = bundleWiring.getBundle().getSymbolicName();
			String className = wovenClass.getClassName();
			
			if (bundleName.equals(Constants.TRANSFORMER_PLUGIN_ID))
				return;

			if (BCELPatcher.BCEL_PLUGIN_ID.equals(bundleName)) {
				BCELPatcher.fixBCEL(wovenClass);
				return;
			}

			WeavingReason reason = requiresWeaving(bundleWiring);
			if (reason != WeavingReason.None) {
				// do whatever is needed *before* loading this class:
				triggerBaseTripWires(bundleName, wovenClass);

				ObjectTeamsTransformer transformer = new ObjectTeamsTransformer();
				Class<?> classBeingRedefined = null; // TODO prepare for otre-dyn
				ProtectionDomain protectionDomain = wovenClass.getProtectionDomain();
				byte[] bytes = wovenClass.getBytes();
				try {
					log(IStatus.OK, "About to transform class "+className);
					long time = 0;
					if (Util.PROFILE) time= System.nanoTime();
					byte[] newBytes = transformer.transform(bundleWiring.getBundle(),
										className, classBeingRedefined, protectionDomain, bytes);
					if (newBytes != bytes && !Arrays.equals(newBytes, bytes)) {
						if (Util.PROFILE) Util.profile(time, ProfileKind.Transformation, className);
						log(IStatus.INFO, "Transformation performed on "+className);
						wovenClass.setBytes(newBytes);
						if (reason == WeavingReason.Aspect)
							recordBaseClasses(transformer, bundleName, className);
					} else {
						if (Util.PROFILE) Util.profile(time, ProfileKind.NoTransformation, className);
					}
				} catch (IllegalClassFormatException e) {
					log(e, "Failed to transform class "+className);
				}
			}
		} catch (ClassCircularityError cce) {
			log(cce, "Weaver encountered a circular class dependency");
		}
	}

	WeavingReason requiresWeaving(BundleWiring bundleWiring) {
		@SuppressWarnings("null")@NonNull
		Bundle bundle = bundleWiring.getBundle();
		if (aspectBindingRegistry.getAdaptedBasePlugins(bundle) != null)
			return WeavingReason.Aspect;
		if (aspectBindingRegistry.isAdaptedBasePlugin(bundle.getSymbolicName()))
			return WeavingReason.Base;
		return WeavingReason.None;
	}

	private void recordBaseClasses(ObjectTeamsTransformer transformer, @NonNull String aspectBundle, String className) {
		Collection<String> adaptedBases = transformer.fetchAdaptedBases();
		if (adaptedBases == null || adaptedBases.isEmpty()) return;
		List<AspectBinding> aspectBindings = aspectBindingRegistry.getAspectBindings(aspectBundle);
		if (aspectBindings != null)
			for (AspectBinding aspectBinding : aspectBindings)
				if (!aspectBinding.hasScannedTeams)
					for (TeamBinding team : aspectBinding.teams)
						if (team.teamName.equals(className))
							if (!team.hasScannedBases) {
								for (TeamBinding equivalent : team.equivalenceSet) {
									equivalent.addBaseClassNames(adaptedBases);
									equivalent.hasScannedBases = true;
								}
								return; // done all equivalent teams
							}
	}

	// ===== handling deferred teams: ======

	/**
	 * Record the given team classes as waiting for instantiation/activation.
	 * Callback during {@link BaseBundleLoadTrigger#fire()}
	 */
	public void addDeferredTeamClasses(List<WaitingTeamRecord> teamClasses) {
		synchronized (deferredTeams) {
			deferredTeams.addAll(teamClasses);
		}
	}

	/**
	 * Try to instantiate/activate any deferred teams that may be unblocked
	 * by the definition of the given trigger class.
	 */
	public void instantiateScheduledTeams(String triggerClassName) {
		List<WaitingTeamRecord> scheduledTeams = null;
		synchronized(deferredTeams) {
			for (WaitingTeamRecord record : new ArrayList<>(deferredTeams)) {
				if (record.notFoundClass.equals(triggerClassName)) {
					if (scheduledTeams == null)
						scheduledTeams = new ArrayList<>();
					if (deferredTeams.remove(record))
						scheduledTeams.add(record);
				}
			}
		}
		if (scheduledTeams == null) return;
		for(WaitingTeamRecord record : scheduledTeams) {
			if (record.team.isActivated)
				continue;
			String teamName = record.team.teamName;
			log(IStatus.INFO, "Consider for instantiation/activation: team "+teamName);
			try {
				TeamLoader loader = new TeamLoader(deferredTeams, beingDefined);
				// Instantiate (we only get here if activationKind != NONE)
				loader.instantiateAndActivate(record.aspectBinding, record.team, record.activationKind); // may re-insert to deferredTeams
			} catch (Exception e) {
				log(e, "Failed to instantiate team "+teamName);
				continue;
			}
		}
	}

	@Override
	public void modified(WovenClass wovenClass) {
		if (wovenClass.getState() == WovenClass.DEFINED) {
			beingDefined.remove(wovenClass.getClassName());
			@SuppressWarnings("null") @NonNull String className = wovenClass.getClassName();
			instantiateScheduledTeams(className);
		}
	}

	static boolean hasImport(WovenClass clazz, String packageName, String packageWithAttribute) {
		List<String> imports = clazz.getDynamicImports();
		for (String imp : imports)
			if (imp.equals(packageName) || imp.equals(packageWithAttribute))
				return true;
		for (Wire wire : clazz.getBundleWiring().getRequiredResourceWires(PackageNamespace.PACKAGE_NAMESPACE)) {
			Object packageValue = wire.getRequirement().getAttributes().get(PackageNamespace.PACKAGE_NAMESPACE);
			if (packageName.equals(packageValue) || packageWithAttribute.equals(packageValue))
				return true;
		}
		return false;
	
	}
}
