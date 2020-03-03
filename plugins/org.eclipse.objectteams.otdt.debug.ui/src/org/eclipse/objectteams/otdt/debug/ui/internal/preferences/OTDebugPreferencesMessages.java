/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
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
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug.ui.internal.preferences;

import org.eclipse.osgi.util.NLS;

public class OTDebugPreferencesMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.objectteams.otdt.debug.ui.internal.preferences.OTDebugPreferencesMessages"; //$NON-NLS-1$
	public static String OTDebugPreferencePage_title;
	public static String OTDebugPreferencePage_enable_filter_title;
	public static String OTDebugPreferencePage_enableFilter_label;
	public static String OTDebugPreferencePage_stackFrameColoringGroup_label;
	public static String OTDebugPreferencePage_colorGeneratedCode_label;
	public static String OTDebugPreferencePage_colorSpecialCode_label;
	public static String OTDebugPreferencePage_callin_stepping_title;
	public static String OTDebugPreferencePage_callin_step_base_label;
	public static String OTDebugPreferencePage_callin_step_recurse_label;
	public static String OTDebugPreferencePage_callin_step_role_label;
	public static String OTDebugPreferencePage_callin_stepping_hint;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, OTDebugPreferencesMessages.class);
	}

	private OTDebugPreferencesMessages() {
	}
}
