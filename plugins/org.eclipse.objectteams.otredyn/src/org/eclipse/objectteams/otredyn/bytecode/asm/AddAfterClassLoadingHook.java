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

import org.eclipse.objectteams.otredyn.bytecode.AbstractBoundClass;
import org.eclipse.objectteams.otredyn.transformer.names.ClassNames;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import static org.eclipse.objectteams.otredyn.bytecode.asm.AsmBoundClass.ASM_API;


/**
 * This adapter adds static initialization that gives the TeamManager
 * a chance to trigger class redefinition after the class has been defined successfully.
 * This is needed if a weaving task is recorded after load-time weaving has been performed
 * and before defining the class has finished.
 * 
 * Also the per-team-class mapping of accessIds is initialized at that point.
 * 
 * @author stephan
 *
 */
public class AddAfterClassLoadingHook extends ClassVisitor {

	// the method to look for or add:
	private static final String CLINIT_NAME = "<clinit>";
	private static final String CLINIT_DESC = "()V";

	// the method to invoke:
	private static final String TARGET_CLASS_NAME = ClassNames.TEAM_MANAGER_SLASH;
	private static final String TARGET_METHOD_NAME = "handleTeamLoaded";
	private static final String TARGET_METHOD_DESC = "(Ljava/lang/Class;)V";
	
	boolean needToAdd = true;
	AbstractBoundClass clazz;
	
	public AddAfterClassLoadingHook(ClassVisitor arg0, AbstractBoundClass clazz) {
		super(ASM_API, arg0);
		this.clazz = clazz;
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (CLINIT_NAME.equals(name)) {
        	// clinit already exists, add our statement to the front:
        	this.needToAdd = false;
        	final MethodVisitor clinit = cv.visitMethod(access, name, desc, null, null);
            return new AdviceAdapter(this.api, clinit, access, name, desc) {
            	@Override
            	protected void onMethodEnter() {
            		createHookCall(clinit);
            	}
            	@Override
            	public void visitMaxs(int maxStack, int maxLocals) {
            		super.visitMaxs(Math.max(1,maxStack), maxLocals);
            	}
			};
        }
        return null;
	}
	
	@Override
	public void visitEnd() {
		if (needToAdd) {
			// no clinit found, add one now:
			MethodVisitor mv = cv.visitMethod(ACC_STATIC, CLINIT_NAME, CLINIT_DESC, null, null);
			mv.visitCode();
			createHookCall(mv);
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 0);
			mv.visitEnd();
		}
	}

	void createHookCall(MethodVisitor clinit) {
		if (clazz.isTeam())
			clinit.visitLdcInsn(Type.getObjectType(clazz.getName().replace('.', '/')));
		else
			clinit.visitInsn(Opcodes.ACONST_NULL);
		clinit.visitMethodInsn(INVOKESTATIC, TARGET_CLASS_NAME, TARGET_METHOD_NAME, TARGET_METHOD_DESC, false);
	}
}
