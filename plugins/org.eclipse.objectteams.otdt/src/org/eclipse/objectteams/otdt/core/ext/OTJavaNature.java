/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2003, 2007 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.core.ext;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.objectteams.otdt.core.OTModelManager;


/**
 * Project nature for OT/J.
 * 
 * @author jwloka
 * @version $Id: OTJavaNature.java 23427 2010-02-03 22:23:59Z stephan $
 * @noinstantiate clients are not supposed to instantiate this class.
 */
public class OTJavaNature implements IProjectNature
{
	private IProject _prj;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Here: Add the OT/J builder to the project
	 * </p>
	 */
    public void configure() throws CoreException
    {
        IProjectDescription projectDescription = _prj.getDescription();
        ICommand command = projectDescription.newCommand();
        command.setBuilderName(JavaCore.OTJ_BUILDER_ID);

        ICommand[] buildCommands    = projectDescription.getBuildSpec();
        ICommand[] newBuildCommands;

		if (contains(buildCommands, JavaCore.OTJ_BUILDER_ID))
		{
			return; // safety, do nothing
		}

        if (contains(buildCommands, JavaCore.BUILDER_ID))
        {
            newBuildCommands =
                replace(buildCommands, JavaCore.BUILDER_ID, command);
        }
        else
        {
            newBuildCommands = insert(buildCommands, command);
        }				

        projectDescription.setBuildSpec(newBuildCommands);
        _prj.setDescription(projectDescription, null);
    }
    
	/**
	 * {@inheritDoc}
	 * <p>
	 * Here: Remove the OT/J builder from the project
	 * </p>
	 */
    public void deconfigure() throws CoreException
    {
        IProjectDescription description   = _prj.getDescription();
        ICommand[]          buildCommands = description.getBuildSpec();
        ICommand            command       = description.newCommand();
        command.setBuilderName(JavaCore.BUILDER_ID);

        ICommand[] newBuildCommands;

        if (contains(buildCommands, JavaCore.OTJ_BUILDER_ID))
        {
            newBuildCommands =
                replace(buildCommands, JavaCore.OTJ_BUILDER_ID, command);
        }
        else
        {
            newBuildCommands =
                remove(buildCommands, JavaCore.OTJ_BUILDER_ID);
        }

        description.setBuildSpec(newBuildCommands);
        _prj.setDescription(description, null);
    }

    /** {@inheritDoc} */
    public IProject getProject()
    {
        return _prj;
    }

    /** {@inheritDoc} */
    public void setProject(IProject value)
    {
        _prj = value;
    }
    
    /** 
     * Check if a project has the OT/J nature.
     * @param project
     * @return true if the project has the OT/J nature.
     */
    public static boolean hasOTJavaNature(IProject project) {
    	try {
			return project.hasNature(JavaCore.OTJ_NATURE_ID);
		} catch (CoreException e) {
			if (OTModelManager.EXTERNAL_PROJECT_NAME.equals(project.getName())) // see JavaProject
				return true;
		}
		return false;
    }

    public static WeavingScheme getWeavingScheme(IJavaProject javaProject) {
    	Object option = javaProject.getOption(JavaCore.COMPILER_OPT_WEAVING_SCHEME, true);
    	if (option instanceof String) {
    		WeavingScheme weavingScheme = WeavingScheme.valueOf((String) option);
    		if (weavingScheme != null)
    			return weavingScheme;
    	}
    	return WeavingScheme.OTRE;
    }

	public static void addOTNatureAndBuilder(IProject project) throws CoreException {
		IProjectDescription prjDesc = project.getDescription();
		prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
		ICommand[] buildSpecs = prjDesc.getBuildSpec();
		prjDesc.setBuildSpec(replaceOrAddOTBuilder(prjDesc, buildSpecs));
		project.setDescription(prjDesc, null);
	}

	private static ICommand[] replaceOrAddOTBuilder(IProjectDescription prjDesc, ICommand[] buildSpecs) {
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

	private boolean contains(ICommand[] commands, String builderId) {
        for (int i = 0; i < commands.length; i++)
			if (commands[i].getBuilderName().equals(builderId))
				return true;
		return false;
    }

    private ICommand[] replace(ICommand[] sourceCommands, String oldBuilderId, ICommand newCommand) {
        ICommand[] newCommands = new ICommand[sourceCommands.length];

        for (int i = 0; i < sourceCommands.length; i++)
			if (sourceCommands[i].getBuilderName().equals(oldBuilderId))
				newCommands[i] = newCommand;
			else
				newCommands[i] = sourceCommands[i];

        return newCommands;
    }

    private ICommand[] insert(ICommand[] sourceCommands, ICommand command) {
        ICommand[] newCommands = new ICommand[sourceCommands.length + 1];
        newCommands[0] = command;

        for (int i = 0; i < sourceCommands.length; i++)
			newCommands[i + 1] = sourceCommands[i];

        return newCommands;
    }

    private ICommand[] remove(ICommand[] sourceCommands, String builderId) {
        ICommand[] newCommands     = new ICommand[sourceCommands.length - 1];
        int        newCommandIndex = 0;

        for (int i = 0; i < sourceCommands.length; i++)
			if (!sourceCommands[i].getBuilderName().equals(builderId))
				newCommands[newCommandIndex++] = sourceCommands[i];

        return newCommands;
    }
}
