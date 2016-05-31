package org.eclipse.objectteams.otdt.internal.ui.preferences;

import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.ui.preferences.OptionsConfigurationBlock.Key;
import org.eclipse.jdt.internal.ui.preferences.ProblemSeveritiesPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Shell;

import base org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * Help the "Configure Problem Severities" link in problem hovers to dispatch into OT/J compiler properties
 */
@SuppressWarnings("restriction")
public team class ConfigureCompilerOptions {

	@SuppressWarnings("decapsulation") // base class is final
	protected class DispatchingUtil playedBy PreferencesUtil {

		void dispatch(String id, Object data) <- replace PreferenceDialog createPropertyDialogOn(Shell s, IAdaptable ia, String id, String[] ign, Object data)
				with { id <- id, data <- data }

		static callin void dispatch(String id, Object data) {
			if (ProblemSeveritiesPreferencePage.PROP_ID.equals(id) && data instanceof Map<?,?>) {
				Object optionId = ((Map<?,?>)data).get(ProblemSeveritiesPreferencePage.DATA_SELECT_OPTION_KEY);
				for (Key otKey : CompilerConfigurationBlock.getKeys()) {
					if (otKey.getName().equals(optionId)) {
						id = OTCompilerPreferencePage.PROP_ID; // dispatch to our own page.
						break;
					}
				}
			}
			base.dispatch(id, data);
		}
	}
}
