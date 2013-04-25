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
import org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer;
import org.osgi.framework.Bundle;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;
import org.osgi.framework.wiring.BundleWiring;

/**
 * This class integrates the OT/J weaver into OSGi using the standard API {@link WeavingHook}.
 */
public class OTWeavingHook implements WeavingHook {

	private AspectBindingRegistry aspectBindingRegistry;
	private ObjectTeamsTransformer objectTeamsTransformer;
	
	public OTWeavingHook() {
		this.aspectBindingRegistry = Activator.loadAspectBindingRegistry();
		this.objectTeamsTransformer = new ObjectTeamsTransformer();
	}

	@Override
	public void weave(WovenClass wovenClass) {
		try {
			// TODO(SH): ideally this trigger would be inserted into the previous woven class
			// do whatever left-overs we find from previous invocations:
			aspectBindingRegistry.instantiateScheduledTeams();
			
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
			// unblock any waiting teams depending on this class:
			aspectBindingRegistry.scheduleTeamClassesFor(className);
		} catch (ClassCircularityError cce) {
			log(cce, "Weaver encountered a circular class dependency");
		}
	}

	private boolean requiresWeaving(BundleWiring bundleWiring) {
		@SuppressWarnings("null")@NonNull
		Bundle bundle = bundleWiring.getBundle();
		return aspectBindingRegistry.getAdaptedBasePlugins(bundle) != null
				|| aspectBindingRegistry.isAdaptedBasePlugin(bundle.getSymbolicName());
	}

}
