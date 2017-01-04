/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2015 Stephan Herrmann.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode.asm;

import org.eclipse.objectteams.otredyn.transformer.names.ClassNames;
import org.eclipse.objectteams.otredyn.transformer.names.ConstantMembers;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Implements the method <code>void _OT$addOrRemoveRole(Object role, boolean isAdding)</code>
 * from <code>org.objectteams.IBoundBase2</code>
 * 
 * @author stephan
 */
public class CreateAddRemoveRoleMethod extends AbstractTransformableClassNode {

	
	@Override
	protected boolean transform() {
		// void _OT$addRemoveRole(Object role, boolean isAdding) {
		MethodNode method = getMethod(ConstantMembers.addOrRemoveRole);
		final int ROLE_SLOT = 1, IS_ADDING_SLOT = 2;
		Label start = new Label(), end = new Label();
		method.instructions.clear();
		method.visitLabel(start);

			// set = <initialized _OT$roleSet;>
			final int SET_SLOT = 3;
			method.visitLocalVariable("set", "Ljava/util/Set;", null, start, end, SET_SLOT);
			genGetInitializedRoleSet(method.instructions, SET_SLOT);
						
			// if (isAdding) {
			method.instructions.add(new VarInsnNode(Opcodes.ILOAD, IS_ADDING_SLOT));
			LabelNode jumpToRemove = new LabelNode();
			method.instructions.add(new JumpInsnNode(Opcodes.IFEQ, jumpToRemove));
			
				// set.add(role);
				method.instructions.add(new VarInsnNode(Opcodes.ALOAD, SET_SLOT));
				method.instructions.add(new VarInsnNode(Opcodes.ALOAD, ROLE_SLOT));
				method.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ClassNames.HASH_SET_SLASH, "add", "(Ljava/lang/Object;)Z", false));
				method.instructions.add(new InsnNode(Opcodes.POP));
				
			LabelNode jumpToEnd = new LabelNode();
			method.instructions.add(new JumpInsnNode(Opcodes.GOTO, jumpToEnd));
			// } else {
			method.instructions.add(jumpToRemove);
				// set.remove(role);
				method.instructions.add(new VarInsnNode(Opcodes.ALOAD, SET_SLOT));
				method.instructions.add(new VarInsnNode(Opcodes.ALOAD, ROLE_SLOT));
				method.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ClassNames.HASH_SET_SLASH, "remove", "(Ljava/lang/Object;)Z", false));
				method.instructions.add(new InsnNode(Opcodes.POP));
	
			method.instructions.add(jumpToEnd);
			// }
			
			method.instructions.add(new InsnNode(Opcodes.RETURN));
			method.visitLabel(end);
		// }
		// maxs are computed, maxStack from flow, maxLocals from localVariable-slots
		return true;
	}

	void genGetInitializedRoleSet(InsnList instructions, int targetLocal) {
		// x = this._OT$roleSet 
		instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
		instructions.add(new FieldInsnNode(Opcodes.GETFIELD, name, ConstantMembers.OT_ROLE_SET, ConstantMembers.HASH_SET_FIELD_TYPE));
		
		instructions.add(new IntInsnNode(Opcodes.ASTORE, targetLocal));
		instructions.add(new VarInsnNode(Opcodes.ALOAD, targetLocal));
		
		// if (x == null) {
		LabelNode skipInstantiation = new LabelNode();
		instructions.add(new JumpInsnNode(Opcodes.IFNONNULL, skipInstantiation));
				
			// this._OT$roleSet = new HashSet();
			instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
			instructions.add(new TypeInsnNode(Opcodes.NEW, ClassNames.HASH_SET_SLASH));
			instructions.add(new InsnNode(Opcodes.DUP));
			instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, ClassNames.HASH_SET_SLASH, "<init>", "()V", false));
			
			instructions.add(new IntInsnNode(Opcodes.ASTORE, targetLocal));
			instructions.add(new VarInsnNode(Opcodes.ALOAD, targetLocal));
			instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, name, ConstantMembers.OT_ROLE_SET, ConstantMembers.HASH_SET_FIELD_TYPE));
			
		instructions.add(skipInstantiation);
		// }
	}
}
