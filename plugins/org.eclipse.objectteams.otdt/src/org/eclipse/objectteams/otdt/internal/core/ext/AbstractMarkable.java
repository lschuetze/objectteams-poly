/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008 Technical University Berlin, Germany.
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
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.ext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.hierarchy.TypeHierarchy;
import org.eclipse.objectteams.otdt.core.compiler.OTNameUtils;
import org.eclipse.objectteams.otdt.core.ext.IMarkableJavaElement;


/**
 * Partial implementation of {@link IMarkableJavaElement}, as a shared base
 * for concrete subclasses.
 *
 * @author stephan
 * @since 1.2.5
 */
public abstract class AbstractMarkable implements IMarkableJavaElement {

	final static String PDE_PLUGIN_NATURE_ID = "org.eclipse.pde.PluginNature"; //$NON-NLS-1$ // avoid importing org.eclipse.pde.core
	
	/**
	 * Fetch the member and super types of type and add them to the given sets.
	 * Works recursively.
	 * 
	 * @param members  all members, direct and indirect, including inherited members.
	 * @param supers   all super types of this and its members.
	 * @param type     focus type
	 * @param currentProject the project of the current Markable, use this first when searching types.
	 * @param projects       where to search for super types which are given by their name
	 * @param monitor
	 * @throws JavaModelException
	 */
	void addSuperAndMemberTypes(Set<IType> members, Set<IType> supers, IType type, IJavaProject currentProject, IJavaProject[] projects, IProgressMonitor monitor) 
			throws JavaModelException 
	{
		if (!type.exists())
			return; // mh?
		
		IType superType = null;
		String superclassName = type.getSuperclassName();
		String packageName = null;
		String typeName = null;
		if (superclassName != null) {
			superclassName = superclassName.replace('$', '.');
			if (superclassName.indexOf('.') != -1) {
				// qualified, find the type directly:
				superType = currentProject.findType(superclassName);
			} else {
				// resolve it now:
				String[][] resolvedSuperName = type.resolveType(superclassName);
				if (resolvedSuperName != null && resolvedSuperName.length == 1) {
					packageName = resolvedSuperName[0][0];
					typeName    = resolvedSuperName[0][1];
					if (!(packageName.equals("java.lang") && typeName.equals("Object"))) //$NON-NLS-1$ //$NON-NLS-2$
					{
						superType = currentProject.findType(packageName, typeName, (IProgressMonitor)null);
						
						if (superType == null) 
							for (IJavaProject prj : projects)
								if ((superType = prj.findType(packageName, typeName, (IProgressMonitor)null)) != null)
									break;
					}
				}
			}
		}

		if (superType != null && !superType.isAnonymous()) {
			supers.add(superType);
			if (!members.contains(superType)) // avoid super-member-loop
				addSuperAndMemberTypes(members, supers, superType, currentProject, projects, monitor);					
		}

		for (IType member : type.getTypes()) {
			if (member.isInterface()) continue; // not currently bindable
			if (   Flags.isRole(member.getFlags())
				&& OTNameUtils.isTopConfined(member.getElementName())) continue; // confineds are not bound base
			members.add(member);
			if (!supers.contains(member)) // avoid super-member-loop
				addSuperAndMemberTypes(members, supers, member, currentProject, projects, monitor);
		}
	}
	/**
	 * Get all direct and indirect subtypes of all types in 'types'.
	 * @param types
	 * @param monitor
	 * @return
	 * @throws JavaModelException
	 */
	Set<IType> getSubTypes(Set<IType> types, IProgressMonitor monitor) throws JavaModelException {
		monitor.beginTask(OTCoreExtMessages.AbstractMarkable_baseClassHierarchy_progress, types.size());

		IJavaSearchScope workspaceScope = SearchEngine.createWorkspaceScope();
		Set<IType> subTypes = new HashSet<IType>(13);
		for (IType type: types) {
			TypeHierarchy hier = new TypeHierarchy(type, null, workspaceScope, true);
			hier.refresh(monitor);
			for(IType subType : hier.getAllSubtypes(type))
				subTypes.add(subType);
			monitor.worked(1);
		}
		monitor.done();
		return subTypes;
	}

	/** Fetch
	 *  - directly dependent projects from a resource, or
	 *  - all workspace OT projects if resource == null
	 */
	IJavaProject[] getProjects(IResource resource) {
		/*
		 * Should we ever want to restrict the set of projects to those that have a direct
		 * dependency on the "current" project, we'd need to perform these steps:
		 *
		 * PDE scenarii:
		 * - determine the current plugin:
		 *   - root = getJavaElement().getAncestor(PACKAGE_FRAGMENT_ROOT)
		 *   - traverse getJavaElement().getJavaProject().getResolvedClasspath(true)
		 *     - match javaProject.getPackageFragmentRoots(cpEntry) against root
		 *     - when found extract pluginName = cpEntry.extraAttributes[o.e.ot.originBaseBundle*]
		 * - for each candidate project 
		 *   - fetch the AspectBindingReader
		 *     (See org.eclipse.objectteams.otdt.internal.compiler.adaptor.ResourceProjectAdaptor.getAspectBindingReader(IProject))
		 *   - check if an aspect binding exists towards pluginName
		 * This appears to be quite heavy weight, will only pay off if
		 * - number of OT-plugins is large
		 * - looking at a class from a non-WS plugin
		 * 
		 * Plain-Java scenarii:
		 * - have no dependency information other than project dependencies, see next.
		 * 
		 * Dependencies between workspace projects are already leveraged in calculateProjectsToSearch()
		 */
		IJavaProject[] projects = null;
        if (resource != null) {
        	IProject project = resource.getProject();
        	projects = getProjectsToSearch(project);
        } else {
        	IWorkspace ws = ResourcesPlugin.getWorkspace();
        	ArrayList<IJavaProject> projectList = new ArrayList<IJavaProject>(); 
        	for (IProject prj : ws.getRoot().getProjects())
        		if (isOTProject(prj) && prj.isOpen())
        			projectList.add(JavaCore.create(prj));
        	projects = projectList.toArray(new IJavaProject[projectList.size()]);
        }
		return projects;
	}

    private IJavaProject[] getProjectsToSearch(IProject baseProject)
    {
        Set<IJavaProject> result = new HashSet<IJavaProject>();
        calculateProjectsToSearch(baseProject, result, true);
        return result.toArray(new IJavaProject[result.size()]);
    }
        
    private boolean isOTProject(IProject project)
    {
        try
        {
            return project.hasNature(JavaCore.OTJ_NATURE_ID);
        }
        catch (CoreException ex)
        {
	        return false;
        }
    }
    
    private boolean isPluginProject(IProject project)
    {
        try
        {
			return project.hasNature(PDE_PLUGIN_NATURE_ID);
        }
        catch (CoreException ex)
        {
	        return false;
        }
    }
    
    private void calculateProjectsToSearch(IProject currentProject, Set<IJavaProject> allProjectsFound, boolean descend)
    {
        if (isOTProject(currentProject))
            allProjectsFound.add(JavaCore.create(currentProject));
        
        if (!descend)
        	return;
        
        // don't search indirect dependencies of plug-in projects because aspectBindings must be declared directly:
        descend = !isPluginProject(currentProject);
        
        for(IProject referencingProject : currentProject.getReferencingProjects())
        	calculateProjectsToSearch(referencingProject, allProjectsFound, descend);
    }
}
