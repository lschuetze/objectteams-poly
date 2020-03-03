/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2004-2009 Berlin Institute of Technology, Germany.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Please visit http://www.objectteams.org for updates and contact.
 *
 * Contributors:
 * Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otre.util;

/**
 * @author Christine Hundt
 * @version $Id: BoundMethod.java 23408 2010-02-03 18:07:35Z stephan $
 */
public class BoundMethod {
	private String name;
	private String signature;
	private boolean isCallin;
	
//	private MethodBinding binding;
	
	public BoundMethod(String methodName, String methodSignature, boolean isCallin, MethodBinding methodBinding) {
		name = methodName;
		signature = methodSignature;
		this.isCallin = isCallin;
//		binding = methodBinding;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public boolean getIsCallin() {
		return this.isCallin;
	}
}
