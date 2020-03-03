/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
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
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otre.util;

/** Representation of a base-super method call, requiring a special access method. */
public class SuperMethodDescriptor {
	public String methodName;
	public String declaringClass;
	public String superClass;
	public String signature;
	public SuperMethodDescriptor(String methodName, String declaringClass,
			String superClass, String signature) {
		super();
		this.methodName = methodName;
		this.declaringClass = declaringClass;
		this.superClass = superClass;
		this.signature = signature;
	}
}
