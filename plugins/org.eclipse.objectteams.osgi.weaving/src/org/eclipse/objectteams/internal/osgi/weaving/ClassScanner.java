/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008, 2010 Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: ClassScanner.java 23461 2010-02-04 22:10:39Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.osgi.weaving.Activator.log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.objectteams.otequinox.hook.ILogger;
import org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer;
import org.eclipse.objectteams.otre.util.CallinBindingManager;
import org.osgi.framework.Bundle;

/**
 * Bridge for the TransformerPlugin by which it can access the ObjectTeamsTransformer
 * without accessing that OTRE class.
 * 
 * @author stephan
 * @since 1.2.0
 */
@SuppressWarnings("nls")
public class ClassScanner 
{
	// default is "on", leave this switch for trouble shooting and profiling:
	public static final boolean REPOSITORY_USE_RESOURCE_LOADER = !"off".equals(System.getProperty("otequinox.repository.hook"));

	// collect class names recorded by readOTAttributes:
	
	//   * this version used by the MasterTeamLoader.loadTeams:
	HashMap<String,ArrayList<String>> baseClassNamesByTeam = new HashMap<String, ArrayList<String>>();
	//   * these fields used by TransformerHook.processClass (collected over multiple readOTAttributes):
	ArrayList<String> allBaseClassNames = new ArrayList<String>();
	ArrayList<String> roleClassNames = new ArrayList<String>();


	/** 
	 * Read all OT byte code attributes for the specified class.
	 * While doing so the names of roles and adapted base classes are collected.
	 * 
	 * @param bundle    where to look
	 * @param className the class to investigate (team or role)
	 * @param loader    the loader (could be null) to use for further classFile lookup
	 * @throws ClassFormatError
	 * @throws IOException
	 * @throws ClassNotFoundException the team or role class was not found
	 */
	public void readOTAttributes(Bundle bundle, String className)
			throws ClassFormatError, IOException, ClassNotFoundException 
	{
		Object loader = REPOSITORY_USE_RESOURCE_LOADER ? bundle : null;
		URL classFile = bundle.getResource(className.replace('.', '/')+".class");
		if (classFile == null) 
			throw new ClassNotFoundException(className);
		ObjectTeamsTransformer transformer = new ObjectTeamsTransformer();
		try (InputStream inputStream = classFile.openStream()) {
			transformer.readOTAttributes(inputStream, classFile.getFile(), loader);
		}
		Collection<String> currentBaseNames = transformer.fetchAdaptedBases(); // destructive read
		if (currentBaseNames != null) {
			// store per team:
			ArrayList<String> basesPerTeam = this.baseClassNamesByTeam.get(className);
			if (basesPerTeam == null) {
				basesPerTeam = new ArrayList<String>();
				this.baseClassNamesByTeam.put(className, basesPerTeam);
			}
			basesPerTeam.addAll(currentBaseNames);
			// accumulated store:
			allBaseClassNames.addAll(currentBaseNames);
		}
		readMemberTypeAttributes(bundle, className, transformer);
	}
	
	/** 
	 * Get the names of the base classes adapted by the given team and 
	 * encountered while reading the byte code attributes.
	 * (Destructive read). 
	 */
	public Collection<String> getCollectedBaseClassNames(String teamName) {
		return this.baseClassNamesByTeam.remove(teamName);
	}

	/** 
	 * Get the names of all adapted base classes encountered while reading the byte code attributes.
	 * (Destructive read). 
	 */
	public Collection<String> getCollectedBaseClassNames() {
		try {
			return this.allBaseClassNames;
		} finally {
			this.allBaseClassNames = new ArrayList<String>();
		}
	}
	
	/** 
	 * Get the names of all member roles encountered while reading the byte code attributes. 
	 * (Destructive read).
	 */
	public Collection<String> getCollectedRoleClassNames() {
		return this.roleClassNames;
	}
	
	/*
	 * Recurse into member types scanning OT attributes.
	 */
	private void readMemberTypeAttributes(Bundle               bundle,
										  String                 className, 
										  ObjectTeamsTransformer transformer)
	{
		List<String> roles = CallinBindingManager.getRolePerTeam(className);
		if (roles != null) {
			for (String roleName: roles) {
				log(ILogger.OK, "scanning role "+roleName);
				try {
					this.roleClassNames.add(roleName);
					readOTAttributes(bundle, roleName);					
				} catch (Throwable t) {
					log(t, "Failed to read OT-Attributes of role "+roleName);
				}
				readMemberTypeAttributes(bundle, roleName, transformer);
			}
		}
	}
};