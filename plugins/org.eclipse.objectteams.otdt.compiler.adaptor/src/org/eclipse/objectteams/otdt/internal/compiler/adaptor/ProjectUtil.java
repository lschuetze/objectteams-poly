/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2007 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.internal.compiler.adaptor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.core.ext.OTJavaNature;

public class ProjectUtil {

	static final String PLUGIN_ID = "org.eclipse.objectteams.otdt.internal.compiler.adaptor";  //$NON-NLS-1$
	
	// Don't use API from from PDE, to reduce dependencies.
	static final String PLUGIN_NATURE = "org.eclipse.pde.PluginNature"; //$NON-NLS-1$
	
	public static IProject safeGetOTPluginProject(ICompilationUnit unitElem) {
		IJavaProject project= unitElem.getJavaProject();
		if (ProjectUtil.isOTPluginProject(project.getProject()))
			return project.getProject();
		return null;
	}

	public static boolean isOTPluginProject(IProject project)  {
		if (project == null) return false;
		try {
			return    project.hasNature(PLUGIN_NATURE)
			       && OTJavaNature.hasOTJavaNature(project);
		} catch (CoreException e) {
			if (!OTModelManager.EXTERNAL_PROJECT_NAME.equals(project.getName())) // see JavaProject.hasJavaNature()
				JavaCore.getJavaCore().getLog().log(new Status(IStatus.ERROR, 
															   PLUGIN_ID, 
															   "Error reading project natures",          //$NON-NLS-1$
															   e));
			return false;
		}
	}
}
