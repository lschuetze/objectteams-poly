/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014 GK Software AG
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

import static org.eclipse.objectteams.otredyn.bytecode.asm.AsmBoundClass.ASM_API;
import static org.eclipse.objectteams.otredyn.transformer.names.ClassNames.*;
import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.Field;

import org.objectteams.Team;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.InstructionAdapter;

/**
 * If a lifting participant has been configured, insert the code to invoke it.
 * @since 2.3.1
 */
public class LiftingParticipantAdapter extends ClassVisitor {

	private static boolean checked = false;
	private static String PARTICIPANT_NAME = System.getProperty("ot.lifting.participant");
	private static final String LIFTING_PARTICIPANT_FIELD = "_OT$liftingParticipant";

	private static final String LIFT_PREFIX = "_OT$liftTo"; 

	private static final String CREATE_ROLE_METHOD = "createRole";
	private static final String CREATE_ROLE_DESC = "(L"+ITEAM_SLASH+";L"+OBJECT_SLASH+";Ljava/lang/String;)L"+OBJECT_SLASH+";";

	public LiftingParticipantAdapter(ClassVisitor cv) {
		super(ASM_API, cv);
	}
	
	public static synchronized boolean isLiftingParticipantConfigured(ClassLoader loader) {
		try {
			Field participantField = Team.class.getField(LIFTING_PARTICIPANT_FIELD);

			boolean shouldInstantiateAndRegister = check(participantField);

			if (shouldInstantiateAndRegister) {			
				// install a shared instance into class Team:
				Class<?> participantClass = loader.loadClass(PARTICIPANT_NAME); 
				participantField.set(null, participantClass.newInstance());
			}
		} catch (Exception e) {
			new IllegalArgumentException("Lifting participant "+PARTICIPANT_NAME+" is invalid.", e).printStackTrace();
			PARTICIPANT_NAME = null; // disable requested lifting participant
		}
		return PARTICIPANT_NAME != null;
	}

	/** Check configuration via system property and directly preset object in the field. */
	synchronized static boolean check(Field participantField) throws IllegalAccessException {
		// perform class loading *outside* this synchronized method
		if (!checked) {
			checked = true;
	
			Object participant = participantField.get(null);
	
			if (PARTICIPANT_NAME != null) 
			{
				// initialized from property "ot.lifting.participant"
				if (participant != null)
					throw new IllegalStateException("liftingParticipant already installed.");
				return true;
			} 
			else if (participant != null) 
			{
				// field was already initialized by a third party
				
				// fetch the class name to signal that transformations are needed.
				PARTICIPANT_NAME = participant.getClass().getName();
			}
		}
		return false;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.startsWith(LIFT_PREFIX)) {
        	final MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, null, null);
			return new InstructionAdapter(this.api, methodVisitor) {
				private Label done = null;
				@Override
				public void visitTypeInsn(int opcode, String type) {
					if (isRelevantAllocation(opcode, type))
						insertParticipantSequence(type);
					super.visitTypeInsn(opcode, type);
				}
				boolean isRelevantAllocation(int opcode, String type) {
					return opcode == NEW
							&& !(type.equals(LIFTING_FAILED_EXCEPTION)
									|| type.equals(LIFTING_VETO_EXCEPTION)
									|| type.equals(WRONG_ROLE_EXCEPTION));
				}
				void insertParticipantSequence(String roleType) {
					// o = Team._OT$liftingParticipant.createRole(aTeam, aBase, roleType);
					getstatic(TEAM_SLASH, LIFTING_PARTICIPANT_FIELD, 'L'+ILIFTING_PARTICIPANT+';');
					visitVarInsn(ALOAD, 0); 	// team 			: Team
					visitVarInsn(ALOAD, 1); 	// base				: Object
					visitLdcInsn(roleType); 	// role class name	: String
					invokeinterface(ILIFTING_PARTICIPANT, CREATE_ROLE_METHOD, CREATE_ROLE_DESC);
		
					// if (o != null)
					dup();
					Label doCreate = new Label(); 
					ifnull(doCreate);
					
					// { ...
					checkcast(Type.getObjectType(roleType));
					done = new Label();
					goTo(done);
					
					// } else {...
					visitLabel(doCreate);
					pop(); // discard unused DUP above
					// ... continue with original role allocation...
				}
				@Override
				public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
					super.visitMethodInsn(opcode, owner, name, desc, itf);
					if (done != null && opcode == INVOKESPECIAL) { // is it the <init> invocation after the original "new"?
						// }
						visitLabel(done);
						done = null;
					}
				}
			};
		}
		return null;
	}

}
