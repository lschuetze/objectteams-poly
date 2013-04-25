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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.osgi.framework.Bundle;

/**
 * Each instance of this class represents the fact that a given base bundle has aspect bindings,
 * which require to load / instantiate / activate one or more teams at a suitable point in time.
 */
public class BaseBundleLoadTrigger {

	private AspectBindingRegistry aspectBindingRegistry;
	@SuppressWarnings("deprecation")
	private org.osgi.service.packageadmin.PackageAdmin admin;

	private String baseBundleName;
	
	private boolean teamsScanned = false;
	

	public BaseBundleLoadTrigger(String bundleSymbolicName, AspectBindingRegistry aspectBindingRegistry, 
			@SuppressWarnings("deprecation") org.osgi.service.packageadmin.PackageAdmin admin) 
	{
		this.baseBundleName = bundleSymbolicName;
		this.aspectBindingRegistry = aspectBindingRegistry;
		this.admin = admin;
	}
	
	/** Signal that the given class is being loaded and trigger any necessary loading/instantiation/activation. */
	public void fire(String className) {
		List<AspectBindingRegistry.WaitingTeamRecord> deferredTeamClasses = new ArrayList<>();
		List<AspectBinding> aspectBindings = aspectBindingRegistry.getAdaptingAspectBindings(baseBundleName);
		if (aspectBindings != null) {
			for (AspectBinding aspectBinding : aspectBindings) {
				if (aspectBinding.activated)
					continue;
				@SuppressWarnings("deprecation")
				Bundle[] aspectBundles = admin.getBundles(aspectBinding.aspectPlugin, null);
				if (aspectBundles == null || aspectBundles.length == 0) {
					log(IStatus.ERROR, "Cannot find aspect bundle "+aspectBinding.aspectPlugin);
					continue;
				}
				Bundle aspectBundle = aspectBundles[0];
				if (shouldScan())
					scanTeamClasses(aspectBundle, aspectBinding);
				TeamLoader loading = new TeamLoader(deferredTeamClasses);
				if (loading.loadTeams(aspectBundle, aspectBinding, className))
//					aspectBinding.activated = true; // FIXME(SH): this still spoils team activation, the given class may not be the trigger
					;
			}
			if (!deferredTeamClasses.isEmpty())
				aspectBindingRegistry.addDeferredTeamClasses(deferredTeamClasses);
		}
	}

	private synchronized boolean shouldScan() {
		boolean shouldScan = !teamsScanned;
		teamsScanned = true;
		return shouldScan;
	}

	/** Read OT attributes of all teams in aspectBinding and collect affected base classes. */
	private void scanTeamClasses(Bundle bundle, AspectBinding aspectBinding) { 
		List<String> allTeams = aspectBinding.getAllTeams();
		ClassScanner scanner = new ClassScanner();
		for (String teamName : allTeams) {
			try {
				scanner.readOTAttributes(bundle, teamName);
				aspectBinding.addBaseClassNames(teamName, scanner.getCollectedBaseClassNames());
			} catch (Exception e) {
				log(e, "Failed to load team class "+teamName);
			}
		}
	}
}
