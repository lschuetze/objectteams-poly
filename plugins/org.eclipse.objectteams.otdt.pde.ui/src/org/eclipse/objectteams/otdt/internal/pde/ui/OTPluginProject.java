/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2020 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.internal.pde.ui;

import static org.eclipse.objectteams.otequinox.Constants.TRANSFORMER_PLUGIN_ID;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions.WeavingScheme;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
import org.eclipse.objectteams.otdt.core.ext.OTREContainer;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.internal.core.ICoreConstants;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModel;
import org.eclipse.pde.internal.core.natures.PDE;
import org.eclipse.pde.internal.core.plugin.WorkspacePluginModelBase;
import org.eclipse.pde.ui.templates.PluginReference;

@SuppressWarnings("restriction") // need some PDE internals
public class OTPluginProject
{
	public static void makeOTPlugin(IProject project) throws CoreException
	{
		addOTNatureAndBuilder(project);
		OTREContainer.initializeOTJProject(project); // still needed? See doc of ClasspathComputerAdapter.sortClasspathEntries(IProject)
		if (PDE.hasPluginNature(project)) {
			// add plug-in dependency to org.eclipse.objectteams.otequinox:
			WorkspacePluginModelBase fModel = new WorkspaceBundlePluginModel(
					project.getFile(ICoreConstants.BUNDLE_FILENAME_DESCRIPTOR),
					project.getFile(ICoreConstants.PLUGIN_FILENAME_DESCRIPTOR));
			IPluginBase pluginBase = fModel.getPluginBase();
			IPluginReference ref = new PluginReference(TRANSFORMER_PLUGIN_ID, null, 0);
			IPluginImport iimport = fModel.getPluginFactory().createImport();
			iimport.setId(ref.getId());
			iimport.setVersion(ref.getVersion());
			iimport.setMatch(ref.getMatch());
			pluginBase.add(iimport);
			fModel.save();
			ClasspathComputerAdapter.sortClasspathEntries(project);
		}
		// require base-imports for base classes per default:
		IJavaProject javaProject = JavaCore.create(project);
		String value = javaProject.getOption(OTDTPlugin.OT_COMPILER_BINDING_CONVENTIONS, true);
		if (!value.equals(JavaCore.ERROR))
			javaProject.setOption(OTDTPlugin.OT_COMPILER_BINDING_CONVENTIONS, JavaCore.ERROR);
		if (JavaModelUtil.is1d8OrHigher(javaProject))
			javaProject.setOption(OTDTPlugin.OT_COMPILER_WEAVING_SCHEME, WeavingScheme.OTDRE.name());
	}

	public static void addOTNatureAndBuilder(IProject project) throws CoreException
	{
		IProjectDescription prjDesc = project.getDescription();
		prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
		ICommand[] buildSpecs = prjDesc.getBuildSpec();
		prjDesc.setBuildSpec(replaceOrAddOTBuilder(prjDesc, buildSpecs));
		project.setDescription(prjDesc, null);
	}

	private static ICommand[] replaceOrAddOTBuilder(IProjectDescription prjDesc, ICommand[] buildSpecs) 
	{
		ICommand otBuildCmd = OTDTPlugin.createProjectBuildCommand(prjDesc);
		// replace existing Java builder?
		for(int i=0; i<buildSpecs.length; i++) {
			if (buildSpecs[i].getBuilderName().equals(JavaCore.BUILDER_ID)) {
				buildSpecs[i] = otBuildCmd;
				return buildSpecs;
			}
		}
		// not found, add to front:
		int len = buildSpecs.length;
		System.arraycopy(buildSpecs, 0, buildSpecs = new ICommand[len+1], 1, len);
		buildSpecs[0] = otBuildCmd;
		return buildSpecs;
	}
}
