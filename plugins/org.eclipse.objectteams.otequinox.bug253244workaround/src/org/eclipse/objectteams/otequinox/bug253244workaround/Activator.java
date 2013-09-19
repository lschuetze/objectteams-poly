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
package org.eclipse.objectteams.otequinox.bug253244workaround;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * The sole purpose of this fragment is to work around
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=253244 in p2.
 * 
 * We need to set the start level of org.eclipse.objectteams.otequinox to 1
 * so that weaving is activate as early as possible.
 */
@SuppressWarnings("deprecation") // useing deprecated class PackageAdmin
public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		PackageAdmin packageAdmin = null;
		
		ServiceReference<?> ref= context.getServiceReference(PackageAdmin.class.getName());
		if (ref!=null) {
			packageAdmin = (PackageAdmin)context.getService(ref);
			Bundle[] bundles = packageAdmin.getBundles("org.eclipse.objectteams.otequinox", "2.3.0");
			if (bundles != null && bundles.length > 0) {
				BundleStartLevel startLevel = bundles[0].adapt(BundleStartLevel.class);
				if (startLevel.getStartLevel() != 1) {
					startLevel.setStartLevel(1);
				}
				// this would cause lock problems (and is successfully handled already via p2.inf):
//				bundles[0].start();
			}
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// nop
	}
}
