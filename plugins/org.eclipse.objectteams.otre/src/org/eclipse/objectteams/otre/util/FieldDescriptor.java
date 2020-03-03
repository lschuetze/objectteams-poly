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
 * @author resix
 */
public class FieldDescriptor {
	private String fieldName;
	private String fieldSignature;
	private boolean isStaticField;

	public FieldDescriptor(String name, String signature, boolean is_static) {
		fieldName = name;
		fieldSignature = signature;
		isStaticField = is_static;
	}
	
	/**
	 * @return Returns the fieldName.
	 */
	public String getFieldName() {
		return fieldName;
	}
	
	/**
	 * @return Returns the fieldSignature.
	 */
	public String getFieldSignature() {
		return fieldSignature;
	}
	
	/**
	 * @return Returns the isStaticField.
	 */
	public boolean isStaticField() {
		return isStaticField;
	}
}
