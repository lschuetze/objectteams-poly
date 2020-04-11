/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2013 GK Software AG, Germany,
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
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

/**
 * @since 3.10 (OT 2.3)
 */
public class OTSearchHelper {

	/**
	 * Add the key->value pair to the given map of sets, creating a new value set if needed.
	 * @param mapOfSets
	 * @param key
	 * @param value
	 */
	public static <M extends IMember> void addToMapOfSets(final Map<IMember, Set<M>> mapOfSets, IMember key, M value)
	{
		Set<M> setForType = mapOfSets.get(key);
		if (setForType == null)
			mapOfSets.put(key, setForType = new HashSet<M>());
		setForType.add(value);
	}

	/**
	 * Find all playedBy bindings within a given set of projects refering to one of baseTypes as its baseclass.
	 *
	 * @param baseTypes
	 * @param projects
	 * @param monitor
	 * @return a map indexed by base types containing sets of role types bound to the given base type.
	 */
	public static Map<IMember, Set<IType>> searchPlayedByBindings(Collection<IType> baseTypes, IJavaProject[] projects, IProgressMonitor monitor)
	{
	    if (baseTypes == null || baseTypes.size() == 0) {
	        monitor.beginTask("", 1); //$NON-NLS-1$
			monitor.done();
	        return null;
	    }

	    OTSearchEngine engine = new OTSearchEngine();
	    IJavaSearchScope searchScope = OTSearchEngine.createOTSearchScope(projects, false);
	    final Map<IMember, Set<IType>> resultMap = new HashMap<IMember, Set<IType>>();

	    try {
	        monitor.beginTask(Messages.OTSearchHelper_progress_searchRoleTypes, baseTypes.size());

	        for (final IType baseType : baseTypes)
	        {
	        	if (monitor.isCanceled()) return null;
	        	try
	        	{
		            IProgressMonitor searchMonitor = new SubProgressMonitor(monitor, 1);
		            if (!baseType.exists()) // ensure it's 'open'
		                continue;
		            if (baseType.isEnum() || baseType.isAnnotation())
		            	continue; // no callin-to-enum/annot
			        SearchPattern pattern = SearchPattern.createPattern(baseType, IJavaSearchConstants.PLAYEDBY_REFERENCES);
			        if (pattern == null)
			            JavaCore.getJavaCore().getLog().log(new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, "Error creating pattern")); //$NON-NLS-1$
			        else
			        	engine.search(
	                        pattern,
	                        searchScope,
	                        new SearchRequestor() {
			                    @Override
								public void acceptSearchMatch(SearchMatch match)
			                            throws CoreException
			                    {
			                        Object element = match.getElement();
			                        if (element instanceof IType)
			                            addToMapOfSets(resultMap, baseType, (IType) element);
			                    }
	                        },
	                        searchMonitor);
	            }
	            catch (CoreException ex)
	            {
	            	JavaCore.getJavaCore().getLog().log(new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, "Error finding playedBy bindings", ex)); //$NON-NLS-1$
	            }
	        }
	    }
	    finally {
	        monitor.done();
	    }

	    return resultMap;
	}

}
