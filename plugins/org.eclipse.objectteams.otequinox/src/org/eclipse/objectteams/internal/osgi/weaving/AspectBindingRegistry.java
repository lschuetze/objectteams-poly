/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008, 2013 Technical University Berlin, Germany and others.
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 *	Technical University Berlin - Initial API and implementation
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.otequinox.TransformerPlugin.log;
import static org.eclipse.objectteams.otequinox.Constants.ACTIVATION;
import static org.eclipse.objectteams.otequinox.Constants.ASPECT_BINDING_EXTPOINT_ID;
import static org.eclipse.objectteams.otequinox.Constants.BASE_PLUGIN;
import static org.eclipse.objectteams.otequinox.Constants.CLASS;
import static org.eclipse.objectteams.otequinox.Constants.ID;
import static org.eclipse.objectteams.otequinox.Constants.REQUIRED_FRAGMENT;
import static org.eclipse.objectteams.otequinox.Constants.SELF;
import static org.eclipse.objectteams.otequinox.Constants.SUPERCLASS;
import static org.eclipse.objectteams.otequinox.Constants.TEAM;
import static org.eclipse.objectteams.otequinox.Constants.TRANSFORMER_PLUGIN_ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.otequinox.Constants;
import org.eclipse.objectteams.otequinox.hook.ILogger;
import org.osgi.framework.Bundle;

/**
 * An instance of this class holds the information loaded from extensions
 * to the <code>aspectBindings</code> extension point.
 */
// parts of this class are moved from org.eclipse.objectteams.otequinox.TransformerPlugin
@NonNullByDefault
public class AspectBindingRegistry {
	
	private static List<String> KNOWN_OTDT_ASPECTS = new ArrayList<String>();
	static {
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.jdt.ui");
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.compiler.adaptor");
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.refactoring");
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.pde.ui");
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.samples");
	}
	/** main internal registry of aspect bindings. */
	private static HashMap<String, ArrayList<AspectBinding>> aspectBindingsByBasePlugin = 
			   new HashMap<String, ArrayList<AspectBinding>>();
	private static HashMap<String, ArrayList<AspectBinding>> aspectBindingsByAspectPlugin = 
		       new HashMap<String, ArrayList<AspectBinding>>();
	private Set<String> selfAdaptingAspects= new HashSet<String>(); // TODO, never read / evaluated
	
	public static boolean IS_OTDT = false;
	
	public boolean isOTDT() {
		return IS_OTDT;
	}

	/* Load extensions for org.eclipse.objectteams.otequinox.aspectBindings and check aspect permissions. */
	public void loadAspectBindings(
			@SuppressWarnings("deprecation") @Nullable org.osgi.service.packageadmin.PackageAdmin packageAdmin,
			OTWeavingHook hook) 
	{
		IConfigurationElement[] aspectBindingConfigs = RegistryFactory.getRegistry().getConfigurationElementsFor(
				TRANSFORMER_PLUGIN_ID, ASPECT_BINDING_EXTPOINT_ID);
		
		for (int i = 0; i < aspectBindingConfigs.length; i++) {
			IConfigurationElement currentBindingConfig = aspectBindingConfigs[i];

			//aspect:
			@SuppressWarnings("null")@NonNull String aspectBundleId= currentBindingConfig.getContributor().getName();
			IS_OTDT |= KNOWN_OTDT_ASPECTS.contains(aspectBundleId);
			if (packageAdmin != null) {
				@SuppressWarnings("deprecation")
				Bundle[] aspectBundles = packageAdmin.getBundles(aspectBundleId, null);
				if (aspectBundles == null || aspectBundles.length == 0 || (aspectBundles[0].getState() < Bundle.RESOLVED)) {
					log(ILogger.ERROR, "aspect bundle "+aspectBundleId+" is not resolved - not loading aspectBindings.");
					continue;
				}
			}
			
			//base:
			IConfigurationElement[] basePlugins = currentBindingConfig.getChildren(BASE_PLUGIN);
			if (basePlugins.length != 1) {
				log(ILogger.ERROR, "aspectBinding of "+aspectBundleId+" must declare exactly one basePlugin");
				continue;
			}
			String baseBundleId = basePlugins[0].getAttribute(ID);
			if (baseBundleId == null) {
				log(ILogger.ERROR, "aspectBinding of "+aspectBundleId+" must specify the id of a basePlugin");
				continue;
			}
				
 			//base fragments?
			IConfigurationElement[] fragments = basePlugins[0].getChildren(REQUIRED_FRAGMENT);
			if (fragments != null 
					&& !checkRequiredFragments(aspectBundleId, baseBundleId, fragments, packageAdmin)) // reported inside
				continue;
			
			IConfigurationElement[] teams = currentBindingConfig.getChildren(TEAM);
			AspectBinding binding = new AspectBinding(aspectBundleId,
														baseBundleId,
														basePlugins[0].getChildren(Constants.FORCED_EXPORTS_ELEMENT), 
														teams.length);
			// TODO(SH): maybe enforce that every bundle id is given only once?

			//teams:
			try {
				for (int j = 0; j < teams.length; j++) {
					String teamClass = teams[j].getAttribute(CLASS);
					binding.teamClasses[j] = teamClass;
					String activation = teams[j].getAttribute(ACTIVATION);
					binding.setActivation(j, activation);
				}
				
				@NonNull String realBaseBundleId = baseBundleId.toUpperCase().equals(SELF) ? aspectBundleId : baseBundleId;
				addBindingForBaseBundle(realBaseBundleId, binding);
				addBindingForAspectBundle(aspectBundleId, binding);
				hook.setBaseTripWire(packageAdmin, realBaseBundleId);

				
				// now that binding.teamClasses is filled connect to super team, if requested:
				for (int j = 0; j < teams.length; j++) {
					String superTeamName = teams[j].getAttribute(SUPERCLASS);
					if (superTeamName != null)
						addSubTeam(aspectBundleId, binding.teamClasses[j], superTeamName);
				}
				log(ILogger.INFO, "registered:\n"+binding);
			} catch (Throwable t) {
				log(t, "Invalid aspectBinding extension");
			}
		}
	}
	
	@SuppressWarnings("deprecation") // multiple uses of deprecated but still recommended class PackageAdmin
	private boolean checkRequiredFragments(String aspectBundleId, String baseBundleId, IConfigurationElement[] fragments, 
			@Nullable org.osgi.service.packageadmin.PackageAdmin packageAdmin) 
	{
		// checking only, no real action needed.
		boolean hasError = false;
		for (IConfigurationElement fragment : fragments) {
			String fragId = fragment.getAttribute(ID);
			if (fragId == null) {
				log(ILogger.ERROR, "Mandatory attribute \"id\" missing from element \"requiredFragment\" of aspect binding in "+aspectBundleId);
				return false;
			} 
			if (packageAdmin == null) {
				log(ILogger.ERROR, "Not checking required fragment "+fragId+" in aspect binding of "+aspectBundleId+", package admin service not present");
				return false; // report only once.
			}
			
			Bundle[] fragmentBundles = packageAdmin.getBundles(fragId, null);
			if (fragmentBundles == null || fragmentBundles.length == 0) {
				log(ILogger.ERROR, "Required fragment "+fragId+" not found in aspect binding of "+aspectBundleId);
				hasError = true;
				continue;
			}
			Bundle fragmentBundle = fragmentBundles[0];
			String aspectBindingHint = " (aspect binding of "+aspectBundleId+")";
			if (packageAdmin.getBundleType(fragmentBundle) != org.osgi.service.packageadmin.PackageAdmin.BUNDLE_TYPE_FRAGMENT) {
				log(ILogger.ERROR, "Required fragment " + fragId + " is not a fragment" + aspectBindingHint);
				hasError = true;
				continue;
			}
			Bundle[] hosts = packageAdmin.getHosts(fragmentBundle);
			if (hosts == null || hosts.length == 0) {
				if (fragmentBundle.getState() < Bundle.RESOLVED) {
					log(ILogger.ERROR, "Required fragment " + fragId + " is not resolved" + aspectBindingHint);
					hasError = true;
					continue;
				}
				log(ILogger.ERROR, "Required fragment "+fragId+" has no host bundle"+aspectBindingHint);
				hasError = true;					
				continue;
			}
			Bundle host = hosts[0];
			if (!host.getSymbolicName().equals(baseBundleId)) {
				log(ILogger.ERROR, "Required fragment "+fragId+" has wrong host "+host.getSymbolicName()+aspectBindingHint);
				hasError = true;
			}
		}
		return !hasError;
	}

	private static void addBindingForBaseBundle(String baseBundleId, AspectBinding binding) {
		ArrayList<AspectBinding> bindingList = aspectBindingsByBasePlugin.get(baseBundleId);
		if (bindingList == null) {
			bindingList = new ArrayList<AspectBinding>();
			aspectBindingsByBasePlugin.put(baseBundleId, bindingList);
		}
		bindingList.add(binding);
	}

	private void addBindingForAspectBundle(String aspectBundleId, AspectBinding binding) {
		ArrayList<AspectBinding> bindingList = aspectBindingsByAspectPlugin.get(aspectBundleId);
		if (bindingList == null) {
			bindingList = new ArrayList<AspectBinding>();
			aspectBindingsByAspectPlugin.put(aspectBundleId, bindingList);
		}
		bindingList.add(binding);
		if (binding.basePlugin.toUpperCase().equals(SELF))
			selfAdaptingAspects.add(aspectBundleId);
	}
	
	/**
	 * Record a sub-class relationship of two teams within the same aspect bundle.
	 * 
	 * @param aspectBundleId
	 * @param subTeamName (nullable only until we have JSR 308)
	 * @param teamName
	 */
	private void addSubTeam(String aspectBundleId, @Nullable String subTeamName, String teamName) {
		ArrayList<AspectBinding> bindingList = aspectBindingsByAspectPlugin.get(aspectBundleId);
		if (bindingList == null) {
			Exception e = new Exception("No such aspect binding");
			log(e, "Class "+teamName+" not registered (declared to be superclass of team "+subTeamName);
		} else {
			for (AspectBinding binding : bindingList)
				if (binding.teamClasses != null)
					for (int i=0; i < binding.teamClasses.length; i++) 
						if (binding.teamClasses[i].equals(teamName)) {
							if (binding.subTeamClasses[i] == null)
								binding.subTeamClasses[i] = new ArrayList<String>();
							binding.subTeamClasses[i].add(subTeamName);
							return;
						}
			Exception e = new Exception("No such aspect binding");
			log(e, "Class "+teamName+" not registered(2) (declared to be superclass of team "+subTeamName);
		}		
	}

	/**
	 * Given a potential aspect bundle, answer the symbolic names of all base bundles
	 * adapted by the aspect bundle.
	 */
	public @Nullable String[] getAdaptedBasePlugins(Bundle aspectBundle) {
		ArrayList<AspectBinding> bindings = aspectBindingsByAspectPlugin.get(aspectBundle.getSymbolicName());
		if (bindings == null) return null;
		String[] basePlugins = new String[bindings.size()];
		for (int i=0; i<basePlugins.length; i++) {
			basePlugins[i] = bindings.get(i).basePlugin;
		}
		return basePlugins;
	}

	/** Is `symbolicName' the name of a base plugin for which an adapting team is registered? */
	public boolean isAdaptedBasePlugin(@Nullable String symbolicName) {
		ArrayList<AspectBinding> list = aspectBindingsByBasePlugin.get(symbolicName);
		return list != null && !list.isEmpty();
	}

	/**
	 * Get the list of aspect bindings affecting the given base plugin.
	 */
	public @Nullable List<AspectBinding> getAdaptingAspectBindings(@Nullable String basePluginName) {
		return aspectBindingsByBasePlugin.get(basePluginName);
	}
}
