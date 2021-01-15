/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2019 Oliver Frank and others.
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

import org.eclipse.objectteams.otredyn.bytecode.Method;
import org.eclipse.objectteams.otredyn.transformer.names.ClassNames;
import org.eclipse.objectteams.otredyn.transformer.names.ConstantMembers;
import org.eclipse.objectteams.otredyn.util.SMAPConstants;
import org.objectteams.ITeam;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * This class creates and adds the instructions, that are needed to call the
 * method callAllBindings to a method.<br/>
 * <br/>
 * The instructions looks as follows:<br/>
 * <code>
 * Object[] args = {args1, ..., argsn};<br/>
 * this.callAllBindings(boundMethodId, args);
 * </code>
 * 
 * @author Oliver Frank
 */
public class CreateCallAllBindingsCallInOrgMethod extends AbstractTransformableClassNode {

	private final Method orgMethod;
	private final int boundMethodId;
	private final int joinpointId;
	private final String joinpointDesc;

	public CreateCallAllBindingsCallInOrgMethod(final Method orgMethod, final int boundMethodId, final int joinpointId,
			final String joinpointDesc) {
		this.orgMethod = orgMethod;
		this.boundMethodId = boundMethodId;
		this.joinpointId = joinpointId;
		this.joinpointDesc = joinpointDesc;
	}

	@Override
	public boolean transform() {
		MethodNode method = getMethod(orgMethod);
		if (method == null || (method.access & Opcodes.ACC_ABSTRACT) != 0)
			return false;

		// start of try-block:
		InsnList newInstructions = new InsnList();

		LabelNode start = new LabelNode();
		newInstructions.add(start);
		Type[] args = Type.getArgumentTypes(method.desc);

		{
			if (orgMethod.getName().equals("<init>")) {
				// keep instructions, find insertion points:
				int last = method.instructions.size();
				LabelNode callAll = new LabelNode();
				boolean hasGenerated = true;
				for (int i = 0; i < last; i++) {
					AbstractInsnNode returnCandidate = method.instructions.get(i);
					if (returnCandidate.getOpcode() == Opcodes.RETURN) {
						method.instructions.set(returnCandidate, callAll);
						generateInvocation(method, args, callAll, newInstructions);
						hasGenerated = true;
					}
				}
				if (!hasGenerated)
					throw new IllegalStateException("Insertion point for weaving into ctor not found!!!");
			} else {
				int startLine = peekFirstLineNumber(method.instructions);
				if (startLine == -1)
					startLine = 1;
				method.instructions.clear();
				addLineNumber(newInstructions, startLine);
				generateInvocation(method, args, null, newInstructions);
			}
		}

		// catch and unwrap SneakyException:
		addCatchSneakyException(method, start);

		int localSlots = 0;
		int maxArgSize = 1;
		for (Type type : args) {
			int size = type.getSize();
			localSlots += size;
			if (size == 2)
				maxArgSize = 2;
		}
		method.maxStack = args.length > 0 ? 5 + maxArgSize : 3;
		method.maxLocals = localSlots + 1;

		return true;
	}

	private static final Handle callinBootstrapHandle = new Handle(Opcodes.H_INVOKESTATIC,
			ClassNames.CALLIN_BOOTSTRAP_SLASH, ConstantMembers.callinBootstrap.getName(),
			ConstantMembers.callinBootstrap.getSignature(), false);

	private static final Handle teamsAndIdsBootstrapHandle = new Handle(Opcodes.H_INVOKESTATIC,
			ClassNames.TEAMS_AND_IDS_BOOTSTRAP_SLASH, ConstantMembers.teamsAndCallinIdsBootstrap.getName(),
			ConstantMembers.teamsAndCallinIdsBootstrap.getSignature(), false);

	private void generateInvocation(MethodNode method, Type[] args, AbstractInsnNode insertBefore,
			InsnList newInstructions) {
		// TODO Lars: invokedynamic to make Team[] array a constant if nothing changed
		// put this on the stack
		newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
		// put joinpointId on the stack as argument to
		// TeamManager.getTeamsAndCallinIds[]
		newInstructions.add(createLoadIntConstant(joinpointId));
		// put ITeam[] on the stack by calling getTeams with joinpointId
		addLineNumber(newInstructions, SMAPConstants.STEP_INTO_LINENUMBER);
		newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassNames.TEAM_MANAGER_SLASH,
				ConstantMembers.getTeams.getName(), ConstantMembers.getTeams.getSignature(), false));
		addLineNumber(newInstructions, SMAPConstants.STEP_OVER_LINENUMBER);
		// put starting idx 0 on the stack
		newInstructions.add(createLoadIntConstant(0));
		// put joinpointId on the stack as argument to TeamManager.getCallinIds[]
		newInstructions.add(createLoadIntConstant(joinpointId));
		// put int[] callinIds on the stack
		addLineNumber(newInstructions, SMAPConstants.STEP_INTO_LINENUMBER);
		newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassNames.TEAM_MANAGER_SLASH,
				ConstantMembers.getCallinIds.getName(), ConstantMembers.getCallinIds.getSignature(), false));
		addLineNumber(newInstructions, SMAPConstants.STEP_OVER_LINENUMBER);
		// put boundMethodId on the stack
		if (method.name.equals("<init>")) { // set bit 0x8000000 to signal the ctor
			newInstructions.add(createLoadIntConstant(0x8000_0000 | boundMethodId));
		} else {
			newInstructions.add(createLoadIntConstant(boundMethodId));
		}
		// box the arguments to Object[]
		newInstructions.add(getBoxingInstructions(args, false));

		// invoke invokedynamic bootstrap
		addLineNumber(newInstructions, SMAPConstants.STEP_INTO_LINENUMBER);
		newInstructions.add(new InvokeDynamicInsnNode(method.name.replaceAll("[<>]", ""),
				ConstantMembers.callAllBindingsTeam.getSignature(), callinBootstrapHandle, 0, joinpointDesc,
				boundMethodId));
		addLineNumber(newInstructions, SMAPConstants.STEP_OVER_LINENUMBER);

		Type returnType = Type.getReturnType(method.desc);
		// TODO Lars: invokedynamic should unbox on its own
		newInstructions.add(getUnboxingInstructionsForReturnValue(returnType));

		if (insertBefore != null) {
			method.instructions.insertBefore(insertBefore, newInstructions);
			method.instructions.remove(insertBefore); // remove extra RETURN
		} else {
			method.instructions.add(newInstructions);
		}
	}

	void addCatchSneakyException(MethodNode method, LabelNode start) {
		method.tryCatchBlocks.add(getCatchBlock(method.instructions, start, orgMethod));
	}

	TryCatchBlockNode getCatchBlock(InsnList instructions, LabelNode start, Method method) {
		// end (exclusive) of try-block
		LabelNode end = new LabelNode();
		instructions.add(end);

		// catch (SneakyException e) { e.rethrow(); }
		LabelNode catchSneaky = new LabelNode();
		instructions.add(catchSneaky);
		instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ClassNames.SNEAKY_EXCEPTION_SLASH,
				ClassNames.RETHROW_SELECTOR, ClassNames.RETHROW_SIGNATURE, false));

		// never reached, just to please the verifier:
		Type returnType = Type.getReturnType(method.getSignature());
		instructions.add(getReturnInsn(returnType));
		return new TryCatchBlockNode(start, end, catchSneaky, ClassNames.SNEAKY_EXCEPTION_SLASH);
	}

	protected InsnList getReturnInsn(Type returnType) {
		InsnList instructions = new InsnList();
		switch (returnType.getSort()) {
		case Type.VOID:
			instructions.add(new InsnNode(Opcodes.RETURN));
			break;
		case Type.ARRAY:
		case Type.OBJECT:
			instructions.add(new InsnNode(Opcodes.ACONST_NULL));
			instructions.add(new InsnNode(Opcodes.ARETURN));
			break;
		case Type.BOOLEAN:
		case Type.CHAR:
		case Type.BYTE:
		case Type.INT:
		case Type.SHORT:
		case Type.LONG:
			instructions.add(new InsnNode(Opcodes.ICONST_0));
			instructions.add(new InsnNode(Opcodes.IRETURN));
			break;
		case Type.DOUBLE:
			instructions.add(new InsnNode(Opcodes.DCONST_0));
			instructions.add(new InsnNode(Opcodes.DRETURN));
			break;
		case Type.FLOAT:
			instructions.add(new InsnNode(Opcodes.FCONST_0));
			instructions.add(new InsnNode(Opcodes.FRETURN));
			break;
		}
		return instructions;
	}
}
