/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2018 GK Software SE
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode.asm;

import org.eclipse.objectteams.otredyn.bytecode.Method;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CreateSuperCallAdapter extends AbstractTransformableClassNode {
	
	private String superClassName;
	private Method method;

	public CreateSuperCallAdapter(String superClassName, Method method) {
		this.superClassName = superClassName;
		this.method = method;
	}

	@Override
	protected boolean transform() {
		MethodNode methodNode = getMethod(method);
		InsnList instructions = new InsnList();
		Type[] args = Type.getArgumentTypes(methodNode.desc);
		addInstructionsForLoadArguments(instructions, args, false);

		instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL,
				superClassName, method.getName(),
				method.getSignature(), false));
		instructions.add(new InsnNode(Opcodes.ARETURN));
		methodNode.instructions = instructions;
		methodNode.maxStack = Math.max(methodNode.maxStack, args.length + 1);
		return true;
	}
}
