/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014, GK Software AG, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 			Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.ext;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.objectteams.otdt.core.ext.OTDREContainer;

/**
 * This class creates the OTDREContainer. Installed from plugin.xml as an extension.
 * @since 2.3
 */
public class OTDREContainerInitializer extends ClasspathContainerInitializer
{

    public OTDREContainerInitializer() {
        super();
    }

    public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
    	
        if (containerPath == null || containerPath.isEmpty() || !containerPath.segment(0).equals(OTDREContainer.CONTAINER_NAME))
            return;
        
        JavaCore.setClasspathContainer(
                containerPath, 
                new IJavaProject[] { project }, 
                new IClasspathContainer[] { new OTDREContainer() }, 
                null);
    }
}
