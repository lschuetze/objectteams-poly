/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.compiler.adaptor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class CompilerAdaptorPlugin extends Plugin {

	private static final String PLUGIN_ID = "org.eclipse.objectteams.otdt.compiler.adaptor"; //$NON-NLS-1$
	
	private static CompilerAdaptorPlugin instance;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		instance = null;
	}
	
	public static void logException(String message, Exception ex) {
		instance.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message, ex));
	}
}
