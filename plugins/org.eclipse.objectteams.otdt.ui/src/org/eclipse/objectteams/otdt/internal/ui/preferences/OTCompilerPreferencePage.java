/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Fraunhofer FIRST - extended API and implementation
 *     Technical University Berlin - extended API and implementation
 *******************************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.preferences;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.JavaPlugin;

import org.eclipse.jdt.internal.ui.preferences.PropertyAndPreferencePage;

import static org.eclipse.jdt.internal.ui.preferences.ProblemSeveritiesPreferencePage.USE_PROJECT_SPECIFIC_OPTIONS;
import static org.eclipse.jdt.internal.ui.preferences.ProblemSeveritiesPreferencePage.DATA_SELECT_OPTION_KEY;
import static org.eclipse.jdt.internal.ui.preferences.ProblemSeveritiesPreferencePage.DATA_SELECT_OPTION_QUALIFIER;

/**
 * OT_COPY_PASTE
 * The page to configure the compiler options.
 * Copied from org.eclipse.jdt.internal.ui.preferences.CompliancePreferencePage
 * 
 * This page is used by "Window->Preferences->Java->Compiler (OT/J)"
 * 
 * Created on Sep 11, 2005
 * 
 * @author stephan
 */
public class OTCompilerPreferencePage extends PropertyAndPreferencePage {

	// sync with ids in plugin.xml:
	// id of the page in the org.eclipse.ui.preferencePages extension
	public static final String PREF_ID= "org.eclipse.objectteams.otdt.ui.preferences.CompilerPreferencePage"; //$NON-NLS-1$
	// id of the pages in the org.eclipse.ui.propertyPages extension
	public static final String PROP_ID= "org.eclipse.objectteams.otdt.ui.propertyPages.CompilerPreferencePage"; //$NON-NLS-1$
	
	private CompilerConfigurationBlock fConfigurationBlock;

	public OTCompilerPreferencePage() {
		setPreferenceStore(JavaPlugin.getDefault().getPreferenceStore());
		//setDescription(OTPreferencesMessages.CompilerConfigurationBlock_common_description);
		
		// only used when page is shown programatically
		setTitle(OTPreferencesMessages.OTCompilerPreferencePage_title);
	}

	/*
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		IWorkbenchPreferenceContainer container= (IWorkbenchPreferenceContainer) getContainer();
		fConfigurationBlock= new CompilerConfigurationBlock(getNewStatusChangedListener(), getProject(), container);
		
		super.createControl(parent);
		
		// TODO(SH): do we have more appropriate help?
		if (isProjectPreferencePage()) {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJavaHelpContextIds.COMPILER_PROPERTY_PAGE);
		} else {
			PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJavaHelpContextIds.COMPILER_PREFERENCE_PAGE);
		}
	}

	@Override
	protected Control createPreferenceContent(Composite composite) {
		return fConfigurationBlock.createContents(composite);
	}
	
	@Override
	public Point computeSize() {
		Point size= super.computeSize();
		size.y= 10; //see bug 294763
		return size;
	}

	@Override
	protected boolean hasProjectSpecificOptions(IProject project) {
		return fConfigurationBlock.hasProjectSpecificOptions(project);
	}
	
	@Override
	protected String getPreferencePageID() {
		return PREF_ID;
	}
	
	@Override
	protected String getPropertyPageID() {
		return PROP_ID;
	}
	
	@Override
	public void dispose() {
		if (fConfigurationBlock != null) {
			fConfigurationBlock.dispose();
		}
		super.dispose();
	}
	
	@Override
	protected void enableProjectSpecificSettings(boolean useProjectSpecificSettings) {
		super.enableProjectSpecificSettings(useProjectSpecificSettings);
		if (fConfigurationBlock != null) {
			fConfigurationBlock.useProjectSpecificSettings(useProjectSpecificSettings);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.preferences.PropertyAndPreferencePage#enablePreferenceContent(boolean)
	 */
	protected void enablePreferenceContent(boolean enable) {
		if (fConfigurationBlock != null) {
			fConfigurationBlock.enablePreferenceContent(enable);
		}
	}
	
	/*
	 * @see org.eclipse.jface.preference.IPreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		super.performDefaults();
		if (fConfigurationBlock != null) {
			fConfigurationBlock.performDefaults();
		}
	}

	/*
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		if (fConfigurationBlock != null && !fConfigurationBlock.performOk()) {
			return false;
		}	
		return super.performOk();
	}
	
	/*
	 * @see org.eclipse.jface.preference.IPreferencePage#performApply()
	 */
	@Override
	public void performApply() {
		if (fConfigurationBlock != null) {
			fConfigurationBlock.performApply();
		}
	}
	
	@Override
	public void applyData(Object data) {
		super.applyData(data);
		if (data instanceof Map && fConfigurationBlock != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map= (Map<String, Object>) data;
			if (isProjectPreferencePage()) {
				Boolean useProjectOptions= (Boolean) map.get(USE_PROJECT_SPECIFIC_OPTIONS);
				if (useProjectOptions != null) {
					enableProjectSpecificSettings(useProjectOptions.booleanValue());
				}
			}
			Object key= map.get(DATA_SELECT_OPTION_KEY);
			Object qualifier= map.get(DATA_SELECT_OPTION_QUALIFIER);
			if (key instanceof String && qualifier instanceof String) {
				fConfigurationBlock.selectOption((String) key, (String) qualifier);
			}
		}
	}

	@Override
	public void setElement(IAdaptable element) {
		super.setElement(element);
		setDescription(null); // no description for property page
	}
	
}
