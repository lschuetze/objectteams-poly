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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBinding.State;
import org.osgi.framework.Bundle;
import org.osgi.framework.hooks.weaving.WovenClass;

/**
 * Each instance of this class represents the fact that a given base bundle has aspect bindings,
 * which require to load / instantiate / activate one or more teams at a suitable point in time.
 */
public class BaseBundleLoadTrigger {

	private AspectBindingRegistry aspectBindingRegistry;
	@SuppressWarnings("deprecation")
	private org.osgi.service.packageadmin.PackageAdmin admin;

	private String baseBundleName;	

	public BaseBundleLoadTrigger(String bundleSymbolicName, AspectBindingRegistry aspectBindingRegistry, 
			@SuppressWarnings("deprecation") org.osgi.service.packageadmin.PackageAdmin admin) 
	{
		this.baseBundleName = bundleSymbolicName;
		this.aspectBindingRegistry = aspectBindingRegistry;
		this.admin = admin;
	}
	
	/**
	 * Signal that the given class is being loaded and trigger any necessary steps:
	 * - scan team & add reverse imports (now)
	 * - load & instantiate & activate (now or later).
	 */
	public boolean fire(WovenClass baseClass, Set<String> beingDefined) {
		List<AspectBindingRegistry.WaitingTeamRecord> deferredTeamClasses = new ArrayList<>();
		List<AspectBinding> aspectBindings = aspectBindingRegistry.getAdaptingAspectBindings(baseBundleName);
		boolean allDone = true;
		if (aspectBindings != null) {
			for (AspectBinding aspectBinding : aspectBindings) {
				if (aspectBinding.state == State.Initial)
					log(IStatus.INFO, "Preparing aspect binding for base bundle "+baseBundleName);
				if (aspectBinding.state == State.TeamsActivated)
					continue;
				@SuppressWarnings("deprecation")
				Bundle[] aspectBundles = admin.getBundles(aspectBinding.aspectPlugin, null);
				if (aspectBundles == null || aspectBundles.length == 0) {
					log(IStatus.ERROR, "Cannot find aspect bundle "+aspectBinding.aspectPlugin);
					continue;
				}
				Bundle aspectBundle = aspectBundles[0];
				if (aspectBinding.state != State.TeamsScanned)
					aspectBinding.scanTeamClasses(aspectBundle);
				TeamLoader loading = new TeamLoader(deferredTeamClasses, beingDefined);
				if (!loading.loadTeamsForBase(aspectBundle, aspectBinding, baseClass))
					allDone = false;
			}
			if (!deferredTeamClasses.isEmpty()) {
				aspectBindingRegistry.addDeferredTeamClasses(deferredTeamClasses);
				return false;
			}
		}
		return allDone;
	}
}
