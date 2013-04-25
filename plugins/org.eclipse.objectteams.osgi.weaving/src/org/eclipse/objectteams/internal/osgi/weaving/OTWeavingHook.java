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

import static org.eclipse.objectteams.osgi.weaving.Activator.log;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.objectteams.osgi.weaving.Activator;
import org.eclipse.objectteams.otequinox.hook.ILogger;
import org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;
import org.osgi.framework.hooks.weaving.WovenClassListener;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;


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

	private AspectBindingRegistry aspectBindingRegistry;
	private ObjectTeamsTransformer objectTeamsTransformer;
	
	/** Call-back from DS framework. */
	public void activate(ComponentContext context) {
		this.aspectBindingRegistry = loadAspectBindingRegistry(context.getBundleContext());
		this.objectTeamsTransformer = new ObjectTeamsTransformer();
	}

	@SuppressWarnings("deprecation")
	private AspectBindingRegistry loadAspectBindingRegistry(BundleContext context) {
		org.osgi.service.packageadmin.PackageAdmin packageAdmin = null;;
		
		ServiceReference<?> ref= context.getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class.getName());
		if (ref!=null)
			packageAdmin = (org.osgi.service.packageadmin.PackageAdmin)context.getService(ref);
		else
			log(ILogger.ERROR, "Failed to load PackageAdmin service. Will not be able to handle fragments.");

		AspectBindingRegistry aspectBindingRegistry = new AspectBindingRegistry();
		aspectBindingRegistry.loadAspectBindings(packageAdmin);
		return aspectBindingRegistry;
	}

	@Override
	public void weave(WovenClass wovenClass) {
		try {
			BundleWiring bundleWiring = wovenClass.getBundleWiring();
			String bundleName = bundleWiring.getBundle().getSymbolicName();
			String className = wovenClass.getClassName();
			
			// do whatever is needed *before* loading this class:
			aspectBindingRegistry.triggerLoadingHooks(bundleName, className);
			
			if (requiresWeaving(bundleWiring)) {
				Class<?> classBeingRedefined = null; // TODO
				ProtectionDomain protectionDomain = null; // TODO
				byte[] bytes = wovenClass.getBytes();
				try {
					log(IStatus.INFO, "About to transform class "+wovenClass);
					byte[] newBytes = objectTeamsTransformer.transform(bundleWiring.getClassLoader(),
										className, classBeingRedefined, protectionDomain, bytes);
					if (newBytes != bytes)
						wovenClass.setBytes(newBytes);
				} catch (IllegalClassFormatException e) {
					log(e, "Failed to transform class "+className);
				}
			}
		} catch (ClassCircularityError cce) {
			log(cce, "Weaver encountered a circular class dependency");
		}
	}
	
	@Override
	public void modified(WovenClass wovenClass) {
		if (wovenClass.getState() == WovenClass.DEFINED) {
			@SuppressWarnings("null") @NonNull String className = wovenClass.getClassName();
			aspectBindingRegistry.instantiateScheduledTeams(className);
		}
	}

	private boolean requiresWeaving(BundleWiring bundleWiring) {
		@SuppressWarnings("null")@NonNull
		Bundle bundle = bundleWiring.getBundle();
		return aspectBindingRegistry.getAdaptedBasePlugins(bundle) != null
				|| aspectBindingRegistry.isAdaptedBasePlugin(bundle.getSymbolicName());
	}

}
