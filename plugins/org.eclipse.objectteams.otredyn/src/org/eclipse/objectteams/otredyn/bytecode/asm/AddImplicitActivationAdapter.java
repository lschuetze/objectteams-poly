/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2011, 2012 GK Software AG.
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
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import org.objectweb.asm.Opcodes;

/**
 * This visitor adds calls to _OT$implicitlyActivate and _OT$implicitlyDeactivate
 * into all relevant methods as configured by
 * <ul>
 * <li>system property <code>ot.implicit.team.activation</code>
 * <li>annotation {@link org.objectteams.ImplicitTeamActivation}.
 * </ul>
 */
public class AddImplicitActivationAdapter extends ClassAdapter {

	public static final Object ANNOTATION_IMPLICIT_ACTIVATION = 'L'+ClassNames.IMPLICIT_ACTIVATION+';';

	protected static final String TARGET_CLASS_NAME = ClassNames.ITEAM_SLASH;
	protected static final String IMPLICIT_ACTIVATE_METHOD_NAME = "_OT$implicitlyActivate";
	protected static final String IMPLICIT_DEACTIVATE_METHOD_NAME = "_OT$implicitlyDeactivate";
	protected static final String METHOD_DESC = "()V";

	// -------------------------------------------------------
	// ---------- Modes for implicit team activation --------
	// -------------------------------------------------------
	private enum ImplicitActivationMode { NEVER, ANNOTATED, ALWAYS }
	private static ImplicitActivationMode implicitActivationMode = ImplicitActivationMode.ANNOTATED;
	static {
		String prop = System.getProperty("ot.implicit.team.activation");
		for (ImplicitActivationMode mode : ImplicitActivationMode.values()) {
			if (mode.name().equals(prop)) {
				implicitActivationMode = mode;
				break;
			}
		}
	}


	private AsmBoundClass clazz;
	
	public AddImplicitActivationAdapter(ClassVisitor cv, AsmBoundClass clazz) {
		super(cv);
		this.clazz = clazz;
	}

	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (isCandidateForImplicitActivation(name, desc, access)) {
        	final MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, null, null);
        	final String enclTeamDesc = clazz.isRole() ? 'L'+clazz.getEnclosingClass().getName().replace('.', '/')+';' : null;
            return new AdviceAdapter(methodVisitor, access, name, desc) {
            	@Override
            	protected void onMethodEnter() {
            		if (clazz.isTeam()) {
            			methodVisitor.visitIntInsn(Opcodes.ALOAD, 0);
            			methodVisitor.visitMethodInsn(INVOKEINTERFACE, TARGET_CLASS_NAME, IMPLICIT_ACTIVATE_METHOD_NAME, METHOD_DESC);
            		}
            		if (clazz.isRole()) {
            			// TODO(SH): respect nesting depth (this$n)
            			methodVisitor.visitIntInsn(Opcodes.ALOAD, 0);
						methodVisitor.visitFieldInsn(Opcodes.GETFIELD, clazz.getName().replace('.', '/'), "this$0", enclTeamDesc);
            			methodVisitor.visitMethodInsn(INVOKEINTERFACE, TARGET_CLASS_NAME, IMPLICIT_ACTIVATE_METHOD_NAME, METHOD_DESC);
            		}
            	}
            	@Override
            	protected void onMethodExit(int opcode) {
            		if (clazz.isTeam()) {
            			methodVisitor.visitIntInsn(Opcodes.ALOAD, 0);
            			methodVisitor.visitMethodInsn(INVOKEINTERFACE, TARGET_CLASS_NAME, IMPLICIT_DEACTIVATE_METHOD_NAME, METHOD_DESC);
            		}
            		if (clazz.isRole()) {
            			// TODO(SH): respect nesting depth (this$n)
            			methodVisitor.visitIntInsn(Opcodes.ALOAD, 0);
            			methodVisitor.visitFieldInsn(Opcodes.GETFIELD, clazz.getName().replace('.', '/'), "this$0", enclTeamDesc);
            			methodVisitor.visitMethodInsn(INVOKEINTERFACE, TARGET_CLASS_NAME, IMPLICIT_DEACTIVATE_METHOD_NAME, METHOD_DESC);
            		}
            		if (clazz.isTeam() || clazz.isRole())
            			methodVisitor.visitMaxs(0, 0);
            	}
			};
        }
        return null;
	}

	
	private boolean isCandidateForImplicitActivation(String methName, String methDesc, int accessFlags) {
		if (clazz.isTeam()) {
			if ((accessFlags & Opcodes.ACC_PRIVATE) != 0)
				return false;
		} else if (clazz.isRole()) {
			if (   clazz.isProtected()
				|| (accessFlags & Opcodes.ACC_PUBLIC) == 0)
				return false;
		} else {
			return false;
		}
		switch (implicitActivationMode) {
		case NEVER:
			return false;
		case ANNOTATED:
			if (!clazz.hasMethodImplicitActivation(methName+methDesc))
				return false;
			//$FALL-THROUGH$
		case ALWAYS:
			// TODO: respect RoleClassMethodModifiers attribute
			return canImplicitlyActivate(accessFlags, methName, methDesc);
		}
		return false;
	}

	private static boolean canImplicitlyActivate(int methFlags, String methName, String methDesc) {
		boolean isCandidate =
			((methFlags & (Opcodes.ACC_ABSTRACT|Opcodes.ACC_STATIC)) == 0) &&
			(!methName.startsWith("_OT$")) &&
			(!methName.equals("<init>")) &&
			(!(methName.equals("activate") && methDesc.equals("()V"))) &&
			(!(methName.equals("deactivate") && methDesc.equals("()V"))) &&
			(!ConstantMembers.isReflectiveOTMethod(methName, methDesc));
		return isCandidate;
	}

}
