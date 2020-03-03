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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Provide defaults for this plugin's preferences.
 * 
 * @author stephan
 * @since 1.2.0
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public static final String PREF_SHOW_OTINTERNAL_VARIABLES = OTDebugAdaptorPlugin.PLUGIN_ID+".show_otinternal_variables"; //$NON-NLS-1$

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = OTDebugAdaptorPlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceInitializer.PREF_SHOW_OTINTERNAL_VARIABLES, false);
	}

}
