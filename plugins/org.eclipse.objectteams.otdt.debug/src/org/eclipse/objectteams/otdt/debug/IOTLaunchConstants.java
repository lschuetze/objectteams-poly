/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
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
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug;


/**
 * @author resix
 */
public interface IOTLaunchConstants {
	/**
	 * Launch configuration attribute key. The value is a list of team names
	 * to be woven and activated automatically in this objectteams launch configuration.
	 */
	public static final String ATTR_TEAMCONFIG_LIST = OTDebugPlugin.PLUGIN_ID + ".TEAMCONFIG_ATTR"; //$NON-NLS-1$
	
	/**
	 * Launch configuration attribute key. The value states, if the teams in the objectteams 
	 * launch configuration will be actually used while launching.
	 */
	public static final String ATTR_TEAMCONFIG_ACTIVE = OTDebugPlugin.PLUGIN_ID + ".TEAMCONFIG_ACTIVE_ATTR"; //$NON-NLS-1$

}
