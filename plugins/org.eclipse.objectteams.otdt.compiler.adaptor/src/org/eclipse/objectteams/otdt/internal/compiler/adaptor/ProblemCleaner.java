/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
