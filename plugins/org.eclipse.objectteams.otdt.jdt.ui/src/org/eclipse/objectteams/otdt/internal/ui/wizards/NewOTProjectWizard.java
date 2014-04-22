/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014, GK Software AG, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *			Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.wizards.JavaProjectWizard;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.objectteams.otdt.core.ext.OTJavaNature;
import org.eclipse.objectteams.otdt.core.ext.OTREContainer;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;
import org.eclipse.objectteams.otdt.internal.ui.Messages;
import org.eclipse.objectteams.otdt.ui.ImageConstants;
import org.eclipse.objectteams.otdt.ui.OTDTUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.objectteams.Team;

import base org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathsBlock;
import base org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import base org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne.JREGroup;

/**
 * Adapt the New Java Project Wizard to create OT/J projects.
 * Additional option: Target Weaving Scheme.
 * 
 * <h3>Activation policy:</h3>
 * <ul>
 * <li>The outer team {@link NewOTProjectWizard} is active from {@link #addPages} up-to any of 
 * 		{@link #finishPage}, {@link #performFinish} or {@link #performCancel}.<br/>
 *      This enables callins from two roles at this level:
 *      <ul>
 *      <li>{@link WizardPageOne} intercepts the initialization of the first wizard page.</li>
 *      <li>{@link NatureAndBuilder} intercepts the final initialization of the created project.</li>
 *      </ul></li>
 * </ul>
 * @since 2.3
 */
@SuppressWarnings("restriction")
public team class NewOTProjectWizard extends JavaProjectWizard {
	
	static final String LAST_SELECTED_WEAVING_SCHEME_KEY= OTDTUIPlugin.UIPLUGIN_ID + ".last.selected.project.weaving.scheme"; //$NON-NLS-1$

	public NewOTProjectWizard() {
		this(null, null);
	}
	
	public NewOTProjectWizard(org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne pageOne, 
			org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo pageTwo) {
		super(pageOne, pageTwo);
		
		setDefaultPageImageDescriptor(OTDTUIPlugin.getDefault().getImageRegistry().getDescriptor(ImageConstants.NEW_OT_PROJECT));
        setDialogSettings(OTDTUIPlugin.getDefault().getDialogSettings());
        setWindowTitle(OTDTUIPlugin.getResourceString("NewOTProjectCreationWizard.title")); //$NON-NLS-1$
	}

	@Override
	public void addPages() {
		activate(Team.ALL_THREADS); // all threads needed, getDefaultClasspathEntries() is not called on main thread
		super.addPages();
	}
	
	@Override
	protected void finishPage(IProgressMonitor monitor)
			throws InterruptedException, CoreException {
		try {
			super.finishPage(monitor);
		} finally {			
			deactivate(Team.ALL_THREADS);
		}
	}
	
	@Override
	public boolean performFinish() {
		try {
			return super.performFinish();
		} finally {			
			deactivate(Team.ALL_THREADS);
		}
	}

	@Override
	public boolean performCancel() {
		deactivate(Team.ALL_THREADS);
		return super.performCancel();
	}
	
	/** Value of the new widget (combo). */
	WeavingScheme weavingScheme = WeavingScheme.OTRE;
	
	/**
	 * Second level adaptation: during {@link #createPageControls()} we activate this inner team,
	 * so its nested role {@link JREGroup} can contribute more widgets.
	 * Team/role nesting directly reflects nesting of base classes.
	 */
	protected team class WizardPageOne playedBy NewJavaProjectWizardPageOne {
		
		/** The new widget we're adding. */
		ComboDialogField weavingControl;

		init <- after NewJavaProjectWizardPageOne;
		@SuppressWarnings("inferredcallout")
		void init() {
			setTitle(OTDTUIPlugin.getResourceString("NewOTProjectCreationWizard.MainPage.title")); //$NON-NLS-1$
	        setDescription(OTDTUIPlugin.getResourceString("NewOTProjectCreationWizard.MainPage.description")); //$NON-NLS-1$
		}

		createJRESelectionControl <- replace createJRESelectionControl;

		callin Control createJRESelectionControl(Composite composite) {
			within (this)
				return base.createJRESelectionControl(composite);
		}

		@SuppressWarnings("decapsulation")
		GridLayout initGridLayout(GridLayout layout, boolean margins) -> GridLayout initGridLayout(GridLayout layout, boolean margins);		

		/** Inner role, active only during {@link WizardPageOne#createJRESelectionControl}. */
		@SuppressWarnings("decapsulation")
		protected class JREGroup implements IDialogFieldListener playedBy JREGroup {

			void storeSelectionValue(ComboDialogField combo, String preferenceKey)
			-> void storeSelectionValue(ComboDialogField combo, String preferenceKey);

			void addWeavingControls(Group group) <- after Control createControl(Composite parent)
				with { group <- (Group) result }
			
			void addWeavingControls(Group group) {
				group.setText(Messages.NewOTProjectWizardPageOne_JREGroup_title);
				
				Label weavingLabel = new Label(group, SWT.NONE);
				weavingLabel.setText(Messages.NewOTProjectWizardPageOne_Weaving_label);
				weavingLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false));

				weavingControl= new ComboDialogField(SWT.READ_ONLY);
				fillWeavingTargets(weavingControl);
				weavingControl.setDialogFieldListener(this);
				Combo comboControl = weavingControl.getComboControl(group);
				comboControl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			}
			
			private void fillWeavingTargets(ComboDialogField weavingControl) {
				String[] labels = new String[WeavingScheme.values().length];
				int i = 0;
				for (WeavingScheme scheme : WeavingScheme.values())
					labels[i++] = scheme.toString();
				weavingControl.setItems(labels);
				weavingControl.selectItem(0);
			}
			
			void dialogFieldChanged(DialogField field) {
				if (field == weavingControl) {
					storeSelectionValue(weavingControl, LAST_SELECTED_WEAVING_SCHEME_KEY);
					weavingScheme = WeavingScheme.values()[weavingControl.getSelectionIndex()];
				}
			}
		}

		IClasspathEntry[] getDefaultClasspathEntries() <- replace IClasspathEntry[] getDefaultClasspathEntries();

		callin IClasspathEntry[] getDefaultClasspathEntries() {
			IClasspathEntry[] regulars = base.getDefaultClasspathEntries();
			int l1 = regulars.length;
			System.arraycopy(regulars, 0, regulars = new IClasspathEntry[l1+1], 0, l1);
			regulars[l1] = JavaCore.newContainerEntry(OTREContainer.getContainerPath());
			return regulars;
		}
	}
	
	protected class NatureAndBuilder playedBy BuildPathsBlock {

		addOTJNature <- after addJavaNature;

		private static void addOTJNature(IProject project, IProgressMonitor monitor) throws CoreException {
			OTJavaNature.addOTNatureAndBuilder(project);
			JavaCore.create(project).setOption(JavaCore.COMPILER_OPT_WEAVING_SCHEME, weavingScheme.toString());
		}
	}
}
