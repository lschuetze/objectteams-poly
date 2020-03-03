/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2012 Oliver Frank and others.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Oliver Frank - Initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.eclipse.objectteams.otredyn.bytecode.asm.AsmBoundClass.ASM_API;

/**
 * Adds an field to the bytecode of a class
 * @author Oliver Frank
 */
class AddFieldAdapter extends ClassVisitor {

	private int access;
	private String name;
	private String desc;

	public AddFieldAdapter(ClassVisitor cv, String name, int access,
			String desc) {
		super(ASM_API, cv);
		this.access = access;
		this.desc = desc;
		this.name = name;
	}

	@Override
	public void visitEnd() {
		FieldVisitor fv = cv.visitField(access, name, desc, null, null);
		fv.visitEnd();
	}
		
	@Override
	public MethodVisitor visitMethod(int arg0, String arg1, String arg2, String arg3, String[] arg4) {
		return null; // also consider other visitors
	}

	@Override
	public String toString() {
		return "AddField "+this.desc+' '+this.name;
	}
}