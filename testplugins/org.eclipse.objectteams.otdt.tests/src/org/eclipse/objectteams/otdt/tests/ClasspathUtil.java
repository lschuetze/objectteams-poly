/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010, 2014 GK Software AG
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
package org.eclipse.objectteams.otdt.tests;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
import org.eclipse.objectteams.otdt.core.ext.OTREContainer;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;

/** Constants for the classpath of OT/J Projects.*/
public class ClasspathUtil {

	// === OT Paths: ===
	public static final String OTDT_PATH 		 = JavaCore.getClasspathVariable(OTDTPlugin.OTDT_INSTALLDIR).toOSString();
	public static IPath[] getBytecodeLibJarPath(WeavingScheme weavingScheme) {
		return OTREContainer.getBytecodeLibraryPaths(weavingScheme);
	}
	public static String getOTREPath(WeavingScheme weavingScheme) {
		return new OTREContainer().getClasspathEntries()[0].getPath().toOSString();
	}

}
