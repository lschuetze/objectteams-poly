/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
import org.eclipse.objectteams.otdt.core.ext.OTREContainer;
import org.eclipse.objectteams.otdt.internal.core.compiler.mappings.CallinImplementorDyn;

/** Constants for the classpath of OT/J Projects.*/
public class ClasspathUtil {

	// === OT Paths: ===
	// TODO(SH): adjust for otdyn:
	public static final String OTRE_OF_PATH 	 = "/home/stephan/git/otredyn/otj/org.eclipse.objectteams.otredyn";
	public static final String OTRE_PATH; 
		//new OTREContainer().getClasspathEntries()[0].getPath().toOSString();
	public static final String OTDT_PATH 		 = JavaCore.getClasspathVariable(OTDTPlugin.OTDT_INSTALLDIR).toOSString();
	public static final String OTRE_MIN_JAR_PATH; 
	public static final String OTAGENT_JAR_PATH; 
		// getOTDTJarPath("otre_agent");
	// hijack this var to point to ASM instead:
	public static final IPath  BCEL_JAR_PATH;
		// OTREContainer.BCEL_PATH;

	static {
		if (CallinImplementorDyn.DYNAMIC_WEAVING) {
			OTRE_PATH     	 = OTRE_OF_PATH+"/bin";
			OTRE_MIN_JAR_PATH = OTRE_OF_PATH+"/otre_min.jar";
			OTAGENT_JAR_PATH  = OTRE_OF_PATH+"/otre_agent.jar";
			BCEL_JAR_PATH     = JavaCore.getClasspathVariable("ECLIPSE_HOME").append("/plugins/org.objectweb.asm_3.3.1.v201105211655.jar");
		} else {
			OTRE_PATH     	 = new OTREContainer().getClasspathEntries()[0].getPath().toOSString();
			OTRE_MIN_JAR_PATH = getOTDTJarPath("otre_min");
			OTAGENT_JAR_PATH  = getOTDTJarPath("otre_agent");
			BCEL_JAR_PATH     = OTREContainer.BCEL_PATH;
		}
	}
	
	private static String getOTDTJarPath(String jarName) {
		return OTDT_PATH + File.separator + "lib" + File.separator + jarName + ".jar";
	}

}
