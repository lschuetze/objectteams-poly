/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2016 GK Software AG
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
import org.eclipse.objectteams.otredyn.transformer.names.ClassNames;
import org.eclipse.objectteams.otredyn.transformer.names.ConstantMembers;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * When weaving into a method inherited from an unweavable superclass (j.l.Object?),
 * a new label must be added to the switch inside _OT$callOrig(),
 * where the original is called via invokespecial. 
 *
 * @since 2.5
 */
public class CreateSpecificSuperCallInCallOrigAdapter extends AbstractTransformableClassNode {

	private Method method;
	private String superClassName;
	private int boundMethodId;
	private int firstArgIndex;
	private int argOffset;
	private Method callOrig;

	public CreateSpecificSuperCallInCallOrigAdapter(AsmWritableBoundClass clazz, String superClassName, Method method, int boundMethodId) {
		this.superClassName = superClassName;
		this.method = method;
		this.boundMethodId = boundMethodId;
		if (method.isStatic()) {
			firstArgIndex = 0;
			argOffset = clazz.isRole() ? 2 : 0;
			callOrig = clazz.getCallOrigStatic();
		} else {
			firstArgIndex = 1;
			callOrig = ConstantMembers.callOrig;
		}
	}

	@Override
	protected boolean transform() {
		MethodNode callOrig = getMethod(this.callOrig);
		String methodSignature = method.getSignature();
		Type returnType = Type.getReturnType(methodSignature);

		InsnList newInstructions = new InsnList();
		if (!method.isStatic())
			newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));

		// Unpacking & unboxing arguments
		Type[] args = Type.getArgumentTypes(methodSignature);
		int size = 0;
		if (args.length > 0) {
			for (int i = argOffset; i < args.length; i++) {
				newInstructions.add(new VarInsnNode(Opcodes.ALOAD, firstArgIndex + argOffset + 1));
				newInstructions.add(createLoadIntConstant(i));
				newInstructions.add(new InsnNode(Opcodes.AALOAD));
				Type arg = args[i];
				size += arg.getSize();
				if (arg.getSort() != Type.ARRAY && arg.getSort() != Type.OBJECT) {
					String objectType = AsmTypeHelper.getBoxingType(arg);
					newInstructions.add(new TypeInsnNode(Opcodes.CHECKCAST, objectType));
					newInstructions.add(AsmTypeHelper.getUnboxingInstructionForType(arg, objectType));
				} else if (!arg.getInternalName().equals(ClassNames.OBJECT_SLASH)) {
					newInstructions.add(new TypeInsnNode(Opcodes.CHECKCAST, arg.getInternalName()));
				}
			}
		}

		newInstructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, superClassName, method.getName(), methodSignature, false));

		newInstructions.add(AsmTypeHelper.getBoxingInstructionForType(returnType));
		newInstructions.add(new InsnNode(Opcodes.ARETURN));
		
		addNewLabelToSwitch(callOrig.instructions, newInstructions, boundMethodId);


		// a minimum stacksize of 3 is needed to box the arguments
		callOrig.maxStack = Math.max(Math.max(callOrig.maxStack, size+3/*safety*/), 3);
		
		// we have to increment the max. stack size, because we have to put NULL on the stack
		if (returnType.getSort() == Type.VOID) {
			callOrig.maxStack += 1;
		}
		return true;
	}
}
