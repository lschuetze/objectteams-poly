/*******************************************************************************
 * Copyright (c) 2016 GK Software AG, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation
 *******************************************************************************/
package org.eclipse.objectteams.otdt.internal.samples;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.objectteams.otdt.ui.help.OTHelpPlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.internal.ui.*;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.osgi.framework.Bundle;

public class SampleWizard extends Wizard implements INewWizard, IExecutableExtension {
	private IConfigurationElement[] samples;
	private IConfigurationElement selection;
	private ProjectNamesPage namesPage;
	private ReviewPage lastPage;

	private boolean switchPerspective = true;
	private boolean selectRevealEnabled = true;
	private boolean activitiesEnabled = true;
	private IProject[] createdProjects;

	private class ImportOverwriteQuery implements IOverwriteQuery {
		@Override
		public String queryOverwrite(String file) {
			String[] returnCodes = {YES, NO, ALL, CANCEL};
			int returnVal = openDialog(file);
			return returnVal < 0 ? CANCEL : returnCodes[returnVal];
		}

		private int openDialog(final String file) {
			final int[] result = {IDialogConstants.CANCEL_ID};
			getShell().getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					String title = Messages.SampleWizard_title;
					String msg = NLS.bind(Messages.SampleWizard_overwrite, file);
					String[] options = {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.YES_TO_ALL_LABEL, IDialogConstants.CANCEL_LABEL};
					MessageDialog dialog = new MessageDialog(getShell(), title, null, msg, MessageDialog.QUESTION, options, 0);
					result[0] = dialog.open();
				}
			});
			return result[0];
		}
	}

	public SampleWizard() {
		PDEPlugin.getDefault().getLabelProvider().connect(this);
		setDefaultPageImageDescriptor(PDEPluginImages.DESC_NEWEXP_WIZ);
		samples = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.pde.ui.samples"); //$NON-NLS-1$ //FIXME
		namesPage = new ProjectNamesPage(this);
		lastPage = new ReviewPage(this);
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.ShowSampleAction_title);
	}

	@Override
	public void dispose() {
		PDEPlugin.getDefault().getLabelProvider().disconnect(this);
		super.dispose();
	}

	public IConfigurationElement[] getSamples() {
		return samples;
	}

	@Override
	public void addPages() {
		if (selection == null) {
			addPage(new SelectionPage(this));
		}
		addPage(namesPage);
		addPage(lastPage);
	}

	@Override
	public boolean performFinish() {
		try {
			String perspId = selection.getAttribute("perspectiveId"); //$NON-NLS-1$
			IWorkbenchPage page = OTSamplesPlugin.getActivePage();
			if (perspId != null && switchPerspective) {
				PlatformUI.getWorkbench().showPerspective(perspId, page.getWorkbenchWindow());
			}
			SampleOperation op = new SampleOperation(selection, namesPage.getProjectNames(), new ImportOverwriteQuery());
			getContainer().run(true, true, op);
			createdProjects = op.getCreatedProjects();
			if (selectRevealEnabled) {
				selectReveal(getShell());
			}
			if (activitiesEnabled)
				enableActivities();
		} catch (InvocationTargetException e) {
			OTSamplesPlugin.logException(e, null, null);
			return false;
		} catch (InterruptedException e) {
			//PDEPlugin.logException(e);
			return false;
		} catch (CoreException e) {
			OTSamplesPlugin.logException(e, null, null);
			return false;
		} catch (OperationCanceledException e) {
			return false;
		}
		return true;
	}

	public void selectReveal(Shell shell) {
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				doSelectReveal();
			}
		});
	}

	private void doSelectReveal() {
		if (selection == null || createdProjects==null)
			return;
		String viewId = selection.getAttribute("targetViewId"); //$NON-NLS-1$
		if (viewId == null)
			return;
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window == null)
			return;
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return;
		IViewPart view = page.findView(viewId);
		if (view == null || !(view instanceof ISetSelectionTarget))
			return;
		ISetSelectionTarget target = (ISetSelectionTarget) view;
		IConfigurationElement[] projects = selection.getChildren("project"); //$NON-NLS-1$

		ArrayList<IResource> items = new ArrayList<IResource>();
		for (int i = 0; i < projects.length; i++) {
			String path = projects[i].getAttribute("selectReveal"); //$NON-NLS-1$
			if (path == null)
				continue;
			IResource resource = createdProjects[i].findMember(path);
			if (resource != null && resource.exists()) {
				if (resource.getName().equals("Intro0.html")) { //$NON-NLS-1$
					resource = setBaseTag(createdProjects[i], resource);
				}
				items.add(resource);
			}
		}
		if (items.size() > 0)
			target.selectReveal(new StructuredSelection(items));
		
		revealInEditor(items, page);
	}

	/** Insert a HTML base-tag to enable relative navigation to resources in OTHelp. */
	private IResource setBaseTag(IProject project, IResource resource) {
		if (resource instanceof IFile) { 				
			IFile file = (IFile)resource;
			try {
				NullProgressMonitor npm = new NullProgressMonitor();
				
				// fetch path to help plugin hosting images/ and guide/otjld/def/:
				Bundle helpBundle = OTHelpPlugin.getDocPlugin();
				String absPath = FileLocator.toFileURL(helpBundle.getEntry("/")).toString();  //$NON-NLS-1$
				String baseTag = "<base href=\""+absPath+"\">\n"; //$NON-NLS-1$ //$NON-NLS-2$
				
				// assemble new new file with new content:
				IFile newFile = project.getFile("Intro.html"); //$NON-NLS-1$
				ByteArrayInputStream stream = new ByteArrayInputStream(baseTag.getBytes("UTF8")); //$NON-NLS-1$
				newFile.create(stream, false, npm);
				newFile.appendContents(file.getContents(), 0, npm);
				stream.close();
				resource.delete(false, npm);
				return newFile;
			} catch (Exception e) {
				OTSamplesPlugin.getDefault().getLog().log(
					OTSamplesPlugin.createErrorStatus("Failed to convert Intro.html", e)); //$NON-NLS-1$
			}
		}	
		return resource;
	}

	void revealInEditor(List<IResource> items, IWorkbenchPage page) {
		if (items.size() > 0) {
			for (IResource resource : items) {
	            if (resource instanceof IFile) {
	                try {
	                    IDE.openEditor(page, (IFile)resource);
	                }
	                catch (PartInitException ex)
	                { /* ignore, user will try to open it manually, then */ } 
	            }
	        }
		}
	}

	public void enableActivities() {
		IConfigurationElement[] elements = selection.getChildren("activity"); //$NON-NLS-1$
		HashSet<String> activitiesToEnable = new HashSet<>();
		IWorkbenchActivitySupport workbenchActivitySupport = PlatformUI.getWorkbench().getActivitySupport();

		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			String id = element.getAttribute("id"); //$NON-NLS-1$
			if (id == null)
				continue;
			activitiesToEnable.add(id);
		}
		HashSet<String> set = new HashSet<>(workbenchActivitySupport.getActivityManager().getEnabledActivityIds());
		set.addAll(activitiesToEnable);
		workbenchActivitySupport.setEnabledActivityIds(set);
	}

	/**
	 *
	 */
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		String variable = data != null && data instanceof String ? data.toString() : null;
		if (variable != null) {
			for (int i = 0; i < samples.length; i++) {
				IConfigurationElement element = samples[i];
				String id = element.getAttribute("id"); //$NON-NLS-1$
				if (id != null && id.equals(variable)) {
					setSelection(element);
					break;
				}
			}
		}
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	/**
	 * @return Returns the selection.
	 */
	public IConfigurationElement getSelection() {
		return selection;
	}

	/**
	 * @param selection
	 *            The selection to set.
	 */
	public void setSelection(IConfigurationElement selection) {
		this.selection = selection;
	}

	/**
	 * @param switchPerspective The switchPerspective to set.
	 * @todo Generated comment
	 */
	public void setSwitchPerspective(boolean switchPerspective) {
		this.switchPerspective = switchPerspective;
	}

	/**
	 * @param selectRevealEnabled The selectRevealEnabled to set.
	 * @todo Generated comment
	 */
	public void setSelectRevealEnabled(boolean selectRevealEnabled) {
		this.selectRevealEnabled = selectRevealEnabled;
	}

	/**
	 * @param activitiesEnabled The activitiesEnabled to set.
	 * @todo Generated comment
	 */
	public void setActivitiesEnabled(boolean activitiesEnabled) {
		this.activitiesEnabled = activitiesEnabled;
	}

	public void updateEntries() {
		namesPage.updateEntries();
	}
}
