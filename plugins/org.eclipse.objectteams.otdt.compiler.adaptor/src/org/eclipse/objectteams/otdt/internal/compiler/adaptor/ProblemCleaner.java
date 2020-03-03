/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.compiler.adaptor;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.objectteams.otdt.compiler.adaptor.CompilerAdaptorPlugin;
import org.eclipse.objectteams.otdt.core.ext.IMarkableJavaElement;

import base org.eclipse.core.internal.events.BuildManager;

@SuppressWarnings("restriction")
public team class ProblemCleaner {

	protected class BuildWatcher playedBy BuildManager {

		void hookStartBuild(IBuildConfiguration[] configs, int trigger) 
		<- before void hookStartBuild(IBuildConfiguration[] configs, int trigger)
				base when (trigger == IncrementalProjectBuilder.FULL_BUILD
						  || trigger == IncrementalProjectBuilder.CLEAN_BUILD);

		private void hookStartBuild(IBuildConfiguration[] configs, int trigger) {
			try {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				root.deleteMarkers(IMarkableJavaElement.GLOBAL_PROBLEM_ID, false, IResource.DEPTH_INFINITE);
			} catch (CoreException e) {
				CompilerAdaptorPlugin.logException("Error occurred when deleting markers", e);
			}
		}
	}
}
