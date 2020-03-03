/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009 Technical University Berlin.
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
 * Marco Mosconi - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.pde.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action to add (TBD: or remove) OT support on projects.
 * To be invoked via the "Configure" context-menu in Package Explorer.
 * @author mosconi
 * @since 1.3.2
 */
public class ToggleOTSupportAction implements IObjectActionDelegate {

	private Shell shell;
	private ISelection selection;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if (!(selection instanceof IStructuredSelection))
			return;
		for (Object element : ((IStructuredSelection) selection).toArray()) {
			IProject project = null;
			if (element instanceof IProject) {
				project = (IProject) element;
			}
			else if (element instanceof IAdaptable) {
				project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
			}
			if (project != null) {
				toggleOTSupport(project);
			}
		}
	}

	private void toggleOTSupport(IProject project)  {
		try {
			IProjectDescription description = project.getDescription();
			if (description.hasNature(JavaCore.OTJ_NATURE_ID)) {
				removeOTSupport(project);
			}
			else {
				OTPluginProject.makeOTPlugin(project);
			}
		}
		catch (CoreException e) {
			ErrorDialog.openError(shell, 
								  OTPDEUIMessages.ToggleOTSupportAction_configurationError_title, 
								  NLS.bind(OTPDEUIMessages.ToggleOTSupportAction_configurationError_message, project.getName()), 
								  e.getStatus());
			OTPDEUIPlugin.getDefault().getLog().log(OTPDEUIPlugin.createErrorStatus("Project configuration error", e)); //$NON-NLS-1$
		}
	}

	/**
	 * Removes OT/J nature, builder, and dependencies from the given project
	 */
	private void removeOTSupport(IProject project) throws CoreException {
		OTPDEUIPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, OTPDEUIPlugin.PLUGIN_ID, "Removing OT configuration from a plug-in project is not yet implemented")); //$NON-NLS-1$
		//TODO: yet to be implemented (also needs additional hooking in plugin.xml)
	}

}
