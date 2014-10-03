/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014 GK Software AG
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.objectteams.otequinox.AspectPermission;
import org.eclipse.objectteams.otequinox.TransformerPlugin;

/** Reflexive gateway to a class from fragment org.eclipse.objectteams.otequinox.turbo, if present. */
public class ForcedExportsDelegate {

	Class<?> registryClass;
	Method getForcedExportsByAspect;
	Method parseForcedExportsFile;
	
	public ForcedExportsDelegate() {
		try {
			registryClass = getClass().getClassLoader().loadClass("org.eclipse.objectteams.otequinox.turbo.ForcedExportsRegistry");
			getForcedExportsByAspect = registryClass.getMethod("getForcedExportsByAspect", new Class<?>[] { String.class, int.class } );
			parseForcedExportsFile = registryClass.getMethod("parseForcedExportsFile", new Class<?>[] { File.class, int.class } );
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			// ignore, turbo fragment may just be absent.
		}
	}
	
	public boolean isAvailable() {
		return registryClass != null;
	}

	@SuppressWarnings({ "unchecked", "null" }) // neither reflection nor Collections.emptyList() knows about nullness
	public @NonNull List<String[]> getForcedExportsByAspect(String aspectBundleId, AspectPermission perm) {
		if (getForcedExportsByAspect != null) {
			try {
				return (List<String[]>) getForcedExportsByAspect.invoke(null, new Object[] {aspectBundleId, perm.ordinal()});
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				TransformerPlugin.log(e, "Failed to access forced exports");
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Called when we know the workspace location to read workspace-specific permissions.
	 * New information is integrated with existing information inside the ForcedExportsRegistry.
	 */
	public void parseForcedExportsFile(File configFile, AspectPermission perm) {
		if (parseForcedExportsFile != null) {
			try {
				parseForcedExportsFile.invoke(null, new Object[] {configFile, perm.ordinal()});
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				TransformerPlugin.log(e, "Failed to access forced exports file");
			}
		}
	}
}
