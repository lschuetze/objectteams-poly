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
package org.eclipse.objectteams.otdt.core.ext;

import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Generalization over elements to which a marker can be attached:
 * <ul>
 * <li>resources representing source files
 * <li>java elements representing binary class files
 * </ul>
 *
 * @since 2.1.0
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IMarkableJavaElement {

	/** Constant used for markers referring to a detail inside a classfile. */
	public static final String ATT_DETAIL_ID = "org.eclipse.objectteams.otdt.JavaModelManager.detailHandleID";

	/** Id of markers denoting problems that are detected only during a full or clean build. */
	public static final String GLOBAL_PROBLEM_ID = "org.eclipse.objectteams.otdt.globalProblem";

	/** Name of this markable for use in the UI. */
	String getName();

	/** Remove all markers of the given IDs from this markable. */
	void removeMarkers(String[] markerIds) throws CoreException;

	/**
	 * Answer the java element underlying this markable.
	 * For binaries this is the IClassFile, otherwise an ICompilationUnit.
	 */
	IJavaElement getJavaElement();

	/**
	 * Answer the resource underlying this markable.
	 * @see IJavaElement#getResource()
	 */
	IResource getResource();
	
	/** Create a real marker with the given ID. */
	IMarker createMarker(String id) throws CoreException;
	
    /** Does this represent a real JavaElement (including existing ancestors)? */
	boolean exists();
	
	/** Is underlying element binary / a classfile? */
	boolean isBinary();

	/** 
	 * Get all types that contribute members to the current markable.
	 * Contribution happens through containment (members) and inheritance. 
	 */
	Set<IType> getAllTypes(IJavaProject[] projects, IProgressMonitor monitor) throws CoreException;
	
	/**
	 * Get all projects that could be relevant for searching.
	 */
	IJavaProject[] getProjects() throws JavaModelException;

	/**
	 * Is the given element contained in this markable?
	 */
	boolean containsElement(IJavaElement element);

}
