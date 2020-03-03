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
package org.eclipse.objectteams.otdt.internal.debug.adaptor.actions;

import org.eclipse.jdt.internal.debug.ui.actions.ToggleBooleanPreferenceAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.objectteams.otdt.internal.debug.adaptor.OTDebugAdaptorPlugin;
import org.eclipse.objectteams.otdt.internal.debug.adaptor.PreferenceInitializer;

/**
 * Toggle action for filtering OT-Internal variables (starting with "_OT$").
 * This action can be used by different views (currently: TeamView and VariablesView).
 * 
 * @author stephan
 * @since 1.2.0
 */
@SuppressWarnings("restriction")
public class ShowOTInternalVariablesAction extends ToggleBooleanPreferenceAction {

	public ShowOTInternalVariablesAction() {
		super();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.debug.ui.actions.ViewFilterAction#getPreferenceKey()
	 */
	protected String getPreferenceKey() {
		return PreferenceInitializer.PREF_SHOW_OTINTERNAL_VARIABLES; 
	}
	
	protected IPreferenceStore getPreferenceStore() {
		return OTDebugAdaptorPlugin.getDefault().getPreferenceStore();
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.debug.ui.actions.ToggleBooleanPreferenceAction#getViewKey()
	 */
	protected String getViewKey() {
		return getCompositeKey();
	}

	public static boolean includeOTInternal(String contextId){
		IPreferenceStore store = OTDebugAdaptorPlugin.getDefault().getPreferenceStore();
		String key = contextId + "." + PreferenceInitializer.PREF_SHOW_OTINTERNAL_VARIABLES; //$NON-NLS-1$
		return store.getBoolean(key);
	}
}
