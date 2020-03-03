/**********************************************************************
 * This file is part of "Object Teams Runtime Environment"-Software
 * 
 * Copyright 2011 GK Software AG
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 */
package org.eclipse.objectteams.otre.bcel;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/**
 * Representation of a class file that reads and retains only structural information:
 * <ul>
 * <li>class name
 * <li>access flags
 * <li>names of super class and super interfaces
 * </ul>
 * Also discard the constant pool when done reading. 
 */
@SuppressWarnings("serial")
public class DietJavaClass extends JavaClass {

	public DietJavaClass(int class_name_index, int superclass_name_index,
			String file_name, int major, int minor, int access_flags,
			ConstantPool constant_pool, int[] interfaces, byte source) {
		super(class_name_index, superclass_name_index, file_name, major, minor, access_flags, constant_pool, interfaces, 
			  null/*fields*/, null/*methods*/, null/*attributes*/, source);
		setConstantPool(null);
	}
	@Override
	public Field[] getFields() {
		throw new UnsupportedOperationException("Class has been parsed in diet mode, fields are not available");
	}
	@Override
	public Method[] getMethods() {
		throw new UnsupportedOperationException("Class has been parsed in diet mode, methods are not available");
	}
	@Override
	public Attribute[] getAttributes() {
		throw new UnsupportedOperationException("Class has been parsed in diet mode, attributes are not available");
	}
}
