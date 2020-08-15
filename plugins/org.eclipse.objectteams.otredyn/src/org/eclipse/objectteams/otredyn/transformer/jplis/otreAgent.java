/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2002, 2010 Berlin Institute of Technology, Germany.
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
 *		Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.transformer.jplis;

import java.lang.instrument.Instrumentation;

import org.eclipse.objectteams.otredyn.runtime.TeamManager;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.InsnNode;


/**
 * @author  Christine Hundt
 */
public class otreAgent {
	private static Instrumentation instCopy;

	
	private static ObjectTeamsTransformer otTransformer;

	public static void premain(String options, Instrumentation inst) {
		instCopy = inst;
		checkASM();
		otTransformer = new ObjectTeamsTransformer();
		instCopy.addTransformer(otTransformer);
		TeamManager.class.getName(); // trigger earliest possible weaving 
	}
	
	private static void checkASM() {
		ClassVisitor.class.getName(); // asm
		AdviceAdapter.class.getName(); // asm.commons
		InsnNode.class.getName(); // asm.tree
	}

	public static Instrumentation getInstrumentation() {
		return instCopy;
	}
}
