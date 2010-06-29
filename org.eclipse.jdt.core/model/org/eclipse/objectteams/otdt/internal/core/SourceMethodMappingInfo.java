/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 Stephan Herrmann
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
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core;

import org.eclipse.jdt.internal.core.AnnotatableInfo;

public class SourceMethodMappingInfo extends AnnotatableInfo {

	private String[] roleParameterNames;
	private String roleReturnType;

	private String[][] baseParameterNames;
	private String[] baseReturnTypes;
	
	public void setRoleArgumentNames(String[] parameterNames) {
		this.roleParameterNames = parameterNames;		
	}

	public void setRoleReturnType(String returnType) {
		this.roleReturnType = returnType;
	}
	
	public void setBaseArgumentNames(String[][] parameterNames) {
		this.baseParameterNames = parameterNames;		
	}

	public void setBaseReturnType(String[] returnTypes) {
		this.baseReturnTypes = returnTypes;
	}
	
}
