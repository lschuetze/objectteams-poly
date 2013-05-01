/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2012 Oliver Frank and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Oliver Frank - Initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode.asm;

import org.eclipse.objectteams.otredyn.transformer.names.ConstantMembers;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * This class creates the instructions, that are needed
 * to call super.callOrig(boundMethodId, args)
 * @author Oliver Frank
 */
public class CreateSuperCallInCallOrigAdapter extends AbstractTransformableClassNode {

	private int joinpointId;
	private String superClassName;

	public CreateSuperCallInCallOrigAdapter(String superClassName,
			int joinpointId) {
		this.superClassName = superClassName;
		this.joinpointId = joinpointId;
	}

	@Override
	public void transform() {
		MethodNode callOrig = getMethod(ConstantMembers.callOrig);
		InsnList instructions = new InsnList();
		
		Type[] args = Type.getArgumentTypes(callOrig.desc);
		addInstructionsForLoadArguments(instructions, args, false);

		instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL,
				superClassName, ConstantMembers.callOrig.getName(),
				ConstantMembers.callOrig.getSignature()));
		instructions.add(new InsnNode(Opcodes.ARETURN));
		addNewLabelToSwitch(callOrig.instructions, instructions, joinpointId);
		callOrig.maxStack = Math.max(callOrig.maxStack, args.length + 1);
	}
}
