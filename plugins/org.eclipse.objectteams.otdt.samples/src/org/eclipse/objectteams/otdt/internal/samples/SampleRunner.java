package org.eclipse.objectteams.otdt.internal.samples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
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

class SampleRunner {

	private ILaunchShortcut launchShortcut;
	final String sampleId;

	public SampleRunner(String sampleId) {
		this.sampleId = sampleId;
	}

	void doRun(String launcher, String target, final boolean debug) {
		try {
			final ILaunchShortcut fshortcut = getLaunchShortcut(launcher);

			Object launchSelection = getLaunchSelection(target);
			final ISelection selection = (launchSelection != null) ? new StructuredSelection(launchSelection) : new StructuredSelection();

			BusyIndicator.showWhile(Display.getDefault(), new Runnable() {
				public void run() {
					fshortcut.launch(selection, debug ? ILaunchManager.DEBUG_MODE : ILaunchManager.RUN_MODE);
				}
			});
		} catch (CoreException ex) {
			ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					SampleMessages.SamplesAdapter_unable_to_run, SampleMessages.SamplesAdapter_cannot_run_selected,
					ex.getStatus());
			OTSamplesPlugin.getDefault().getLog().log(ex.getStatus());
		}
	}

	private ILaunchShortcut getLaunchShortcut(String launcher) throws CoreException {
		if (launchShortcut != null && launchShortcut.getClass().getName().equals(launcher))
			return launchShortcut;
		
		try {
			Class<?> launcherClass = Class.forName(launcher);
			launchShortcut = (ILaunchShortcut) launcherClass.newInstance();
			return launchShortcut;
		} catch (Exception ex) {
			IStatus status = OTSamplesPlugin.createErrorStatus("Unable to create launcher", ex); //$NON-NLS-1$
			throw new CoreException(status);
		} 
	}

	// NEW: target is the "launchTarget" property from the launchTarget attribute in the sample.properties
	//      file or the <sample> configuration element.
	//      Note: target may be null.
	// falls back to search a main-class in the first src-folder available.
	Object getLaunchSelection(String target) throws JavaModelException {
		IProject project = getProject(sampleId);
		if (project != null) {
			IJavaProject javaProject = JavaCore.create(project);
			if (javaProject.exists()) {
				if (target != null) {
					IType targetType = javaProject.findType(target);
					if (targetType != null)
						return targetType;
				}
				IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
				for (int i = 0; i < packageFragmentRoots.length; i++) {
					IPackageFragmentRoot root = packageFragmentRoots[i];
					if (root.getKind() == IPackageFragmentRoot.K_SOURCE)
						return root;
				}
			}
		}
		return null;
	}

	// OT_COPY_PASTE: STATE: 3.2: most parts copy&paste from SampleStandbyContent.doBrowse()
	IProject getProject(String sid) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = root.getProjects();
		if (sid == null)
			return null;
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			if (!project.exists() || !project.isOpen())
				continue;
			IFile pfile = project.getFile("sample.properties"); //$NON-NLS-1$
			if (pfile.exists()) {
				try {
					InputStream is = pfile.getContents();
					Properties prop = new Properties();
					prop.load(is);
					is.close();
					String id = prop.getProperty("id"); //$NON-NLS-1$
					if (id != null && id.equals(sid)) {
						return project;
					}
				} catch (IOException e) {
					OTSamplesPlugin.logException(e, null, null);
				} catch (CoreException e) {
					OTSamplesPlugin.logException(e, null, null);
				}
			}
		}
		return null;
	}
}
