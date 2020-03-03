/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008 Technical University Berlin, Germany.
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
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.debug.adaptor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/** This Activator enables the debug adaptor to manage preferences of its own.
 *  
 *  @author stephan
 *  @since 1.2.0
 */
public class OTDebugAdaptorPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.eclipse.objectteams.otdt.internal.debug.adaptor"; //$NON-NLS-1$
	
	private static OTDebugAdaptorPlugin instance;
	
	public OTDebugAdaptorPlugin() {
		instance = this;
	}

	public static AbstractUIPlugin getDefault() {
		return instance; 
	}
	
	public static void logError(String msg) {
		instance.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, msg));
	}
	
	
	public static void logException(String msg, Exception ex) {
		instance.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, msg, ex));
	}
}
