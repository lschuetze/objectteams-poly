/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008, 2014 Technical University Berlin, Germany and others
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 		Technical University Berlin - Initial API and implementation
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.otequinox.TransformerPlugin.log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.otre.util.CallinBindingManager;
import org.osgi.framework.Bundle;

/**
 * Simple facility for scanning class files for OT Attributes without
 * actually doing any weaving.
 * 
 * @author stephan
 * @since 1.2.0
 */
@SuppressWarnings("nls")
@NonNullByDefault
public class ClassScanner 
{
	// default is "on", leave this switch for trouble shooting and profiling:
	public static final boolean REPOSITORY_USE_RESOURCE_LOADER = !"off".equals(System.getProperty("otequinox.repository.hook"));

	// collect class names recorded by readOTAttributes:
	
	//   * this version used by the MasterTeamLoader.loadTeams:
	Map<String,ArrayList<String>> baseClassNamesByTeam = new HashMap<String, ArrayList<String>>();
	//   * these fields used by TransformerHook.processClass (collected over multiple readOTAttributes):
	List<String> allBaseClassNames = new ArrayList<String>();
	List<String> roleClassNames = new ArrayList<String>();


	/** 
	 * Read all OT byte code attributes for the specified class.
	 * While doing so the names of roles and adapted base classes are collected.
	 * 
	 * @param bundle    where to look
	 * @param className the class to investigate (team or role)
	 * @param loader    the loader (could be null) to use for further classFile lookup
	 * @return the real class name (potentially involving '$' and '__OT__' substitution/insertion
	 * @throws ClassFormatError
	 * @throws IOException
	 * @throws ClassNotFoundException the team or role class was not found
	 */
	public String readOTAttributes(Bundle bundle, String className, DelegatingTransformer transformer)
			throws ClassFormatError, IOException, ClassNotFoundException 
	{
		Bundle loader = REPOSITORY_USE_RESOURCE_LOADER ? bundle : null;
		Pair<URL,String> result = TeamLoader.findTeamClassResource(className, bundle);
		if (result == null)
			throw new ClassNotFoundException(className);
		URL classFile = result.first;
		className = result.second;
		try (InputStream inputStream = classFile.openStream()) {
			transformer.readOTAttributes(className, inputStream, classFile.getFile(), loader);
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
		return className;
	}
	
	/** 
	 * Get the names of the base classes adapted by the given team and 
	 * encountered while reading the byte code attributes.
	 * (Destructive read). 
	 */
	public @Nullable Collection<String> getCollectedBaseClassNames(String teamName) {
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
	void readMemberTypeAttributes(Bundle			     bundle,
								  String                 className, 
								  DelegatingTransformer  transformer)
	{
		List<String> roles = CallinBindingManager.getRolePerTeam(className);
		if (roles != null) {
			for (@NonNull String roleName: roles) {
				log(IStatus.OK, "scanning role "+roleName);
				try {
					this.roleClassNames.add(roleName);
					readOTAttributes(bundle, roleName, transformer);					
				} catch (Throwable t) {
					log(t, "Failed to read OT-Attributes of role "+roleName);
				}
				readMemberTypeAttributes(bundle, roleName, transformer);
			}
		}
	}
};