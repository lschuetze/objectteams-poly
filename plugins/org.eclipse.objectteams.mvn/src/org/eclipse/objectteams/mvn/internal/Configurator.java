/*******************************************************************************
 * Copyright (c) 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.objectteams.mvn.internal;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
import org.eclipse.objectteams.otdt.core.ext.OTJavaNature;

import base org.eclipse.m2e.jdt.internal.AbstractJavaProjectConfigurator;

/**
 * When configuring a Java project from its maven pom, check if it's an OT/J project,
 * which we detect by seeing a <code>&lt;flavor&gt;otj&lt;/flavor&gt;</code> element in the
 * compiler configuration. When an OT/J project is detected add the OTJavaNature
 * and update builders accordingly.
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class Configurator {
	
	final static String MAVEN_COMPILER_PLUGIN 	= "org.apache.maven.plugins:maven-compiler-plugin";
	final static String FLAVOR 					= "flavor";
	final static String OTJ 					= "otj";

	protected class OTJProjectConfigurator playedBy AbstractJavaProjectConfigurator {

		// Fetch the request to be used by subsequent callins:
		ProjectConfigurationRequest request;
		storeRequest <- before configure;
		void storeRequest(ProjectConfigurationRequest request) {
			this.request = request;
		}
		
		boolean isOTJProject(IProject project) {
			MavenProject mavenProject = this.request.getMavenProject();
			Plugin plugin = mavenProject.getPlugin(MAVEN_COMPILER_PLUGIN);
			
			if (plugin != null) {
				Object configuration = plugin.getConfiguration();
				if (configuration instanceof Xpp3Dom) {
					Xpp3Dom flavor = ((Xpp3Dom) configuration).getChild(FLAVOR);
					if (flavor != null && OTJ.equals(flavor.getValue()))
						return true;
				}
			}
			return false;
		}
		
		void addOTNatureAndBuilder(IProject project) <- after void addJavaNature(IProject project, IProgressMonitor monitor)
			when ( isOTJProject(project) && !OTJavaNature.hasOTJavaNature(project) );

		// from org.eclipse.objectteams.otdt.internal.pde.ui.OTPluginProject,
		// consider making API:
		void addOTNatureAndBuilder(IProject project)
				throws CoreException 
		{
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			ICommand[] buildSpecs = prjDesc.getBuildSpec();
			prjDesc.setBuildSpec(replaceOrAddOTBuilder(prjDesc, buildSpecs));
			project.setDescription(prjDesc, null);
		}

		private static ICommand[] replaceOrAddOTBuilder(
				IProjectDescription prjDesc, ICommand[] buildSpecs) 
		{
			ICommand otBuildCmd = OTDTPlugin.createProjectBuildCommand(prjDesc);
			// replace existing Java builder?
			for (int i = 0; i < buildSpecs.length; i++) {
				if (buildSpecs[i].getBuilderName().equals(JavaCore.BUILDER_ID)) {
					buildSpecs[i] = otBuildCmd;
					return buildSpecs;
				}
			}
			// not found, add to front:
			int len = buildSpecs.length;
			System.arraycopy(buildSpecs, 0, buildSpecs = new ICommand[len + 1],
					1, len);
			buildSpecs[0] = otBuildCmd;
			return buildSpecs;
		}
	}
}
