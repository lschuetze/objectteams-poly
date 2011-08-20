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
package org.eclipse.objectteams.otdt.core.ext;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.objectteams.otdt.internal.core.ext.JavaElementMarkable;
import org.eclipse.objectteams.otdt.internal.core.ext.ResourceMarkable;

/**
 * Factory for {@link IMarkableJavaElement elements} to which a marker can be attached.
 * 
 * @since 2.1.0
 * @noextend This class is not intended to be extended by clients.
 */
public class MarkableFactory {

	private MarkableFactory() {} // don't instantiate
	
	public static IMarkableJavaElement createMarkable(IResource resource) {
		return new ResourceMarkable(resource);
	}
	
	public static IMarkableJavaElement createMarkable(IJavaElement javaElement) {
		IClassFile classFile = (IClassFile) javaElement.getAncestor(IJavaElement.CLASS_FILE);
		if (classFile != null)
			return new JavaElementMarkable(classFile);
		else
			return new ResourceMarkable(javaElement.getResource());
	}
}
