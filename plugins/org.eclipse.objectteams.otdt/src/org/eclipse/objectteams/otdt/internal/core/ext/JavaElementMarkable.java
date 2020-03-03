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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.views.markers.MarkerViewUtil;

/**
 * Implement markable protocol for IClassFile elements.
 * 
 * @author stephan
 * @since 1.2.5
 */
public class JavaElementMarkable extends AbstractMarkable {
	IClassFile fJavaElement;

	/**
	 * @param javaElement should actually be an IClassFile (otherwise use ResourceMarkable on the IFile (source file))
	 */
	public JavaElementMarkable(IClassFile javaElement) {
		this.fJavaElement = javaElement;
	}

	public String getName() {
		return this.fJavaElement.getElementName();
	}
	
	public Set<IType> getAllTypes(IJavaProject[] projects, IProgressMonitor monitor) throws JavaModelException {
		Set<IType> result = new HashSet<IType>(13);
		
		IType type = this.fJavaElement.getType();

		Set<IType> members = new HashSet<IType>(5);
		Set<IType> supers = new HashSet<IType>(5);
		members.add(type);
		addSuperAndMemberTypes(members, supers, type, this.fJavaElement.getJavaProject(), projects, monitor);
		result.addAll(members);
		result.addAll(supers);
		monitor.worked(5);
		
		result.addAll(getSubTypes(members, new SubProgressMonitor(monitor, 5, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK)));

		monitor.done();
		return result;
	}

	public void removeMarkers(String[] markerIds) throws CoreException {
		if (this.fJavaElement.exists())
		{
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

			for (IMarker marker : getAllMarkers(root, markerIds))
				if (JavaCore.isReferencedBy(this.fJavaElement, marker))
					marker.delete();
    	}
	}

	private IMarker[] getAllMarkers(IResource resource, String[] markerIds) throws CoreException {
		IMarker[][] markers = new IMarker[markerIds.length][];
		int total = 0;
		for (int i = 0; i < markerIds.length; i++) {
			markers[i] = resource.findMarkers(markerIds[i], true, IResource.DEPTH_INFINITE);
			total += markers[i].length;
		}
		IMarker[] result = new IMarker[total];
		int offset = 0;
		for (int i = 0; i < markerIds.length; i++) {
			System.arraycopy(markers[i], 0, result, offset, markers[i].length);
			offset += markers[i].length;
		}
		return result;
	}

	public IClassFile getJavaElement() {
		return this.fJavaElement;
	}
	
	public IResource getResource() {
		return this.fJavaElement.getResource();
	}
	
	public boolean containsElement(IJavaElement element) {
		IJavaElement current = element;
		while (current != null) {
			if (current == this.fJavaElement)
				return true;
			current = current.getParent();
		}
		return false;
	}

	public boolean exists() {
		return this.fJavaElement.exists();
	}
	
	public boolean isBinary() {
		return true;
	}

	public IJavaProject[] getProjects() throws JavaModelException {
		// FIXME(SH): might want to lookup the project from a library??
		return getProjects(this.fJavaElement.getCorrespondingResource());
	}

	public IMarker createMarker(String id) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IMarker marker = root.createMarker(id);
		JavaCore.getJavaCore().configureJavaElementMarker(marker, this.fJavaElement);
		marker.setAttribute(MarkerViewUtil.NAME_ATTRIBUTE, getName());
		marker.setAttribute(MarkerViewUtil.PATH_ATTRIBUTE, this.fJavaElement.getPath().toString());
		return marker;
	}

}
