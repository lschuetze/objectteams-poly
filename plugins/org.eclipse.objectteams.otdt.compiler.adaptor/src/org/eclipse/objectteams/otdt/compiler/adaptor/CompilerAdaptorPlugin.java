/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		instance = this;
	}

	public void stop(BundleContext context) throws Exception {
		// empty
	}
	
	public static void logException(String message, Exception ex) {
		instance.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message, ex));
	}
}
