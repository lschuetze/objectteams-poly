/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2020 GK Software SE
 *  
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode.asm;

import org.eclipse.objectteams.otredyn.bytecode.Method;
import org.eclipse.objectteams.otredyn.util.SMAPConstants;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

public class MarkAsStepOverAdapter extends AbstractTransformableClassNode {

	private Method[] methods;

	public MarkAsStepOverAdapter(Method[] methods) {
		this.methods = methods;
	}

	@Override
	protected boolean transform() {
		for (Method method : this.methods) {			
			MethodNode orgMethod = getMethod(method);
			for (AbstractInsnNode insnNode : orgMethod.instructions) {
				if (insnNode instanceof LineNumberNode)
					((LineNumberNode) insnNode).line = SMAPConstants.STEP_OVER_LINENUMBER;
			}
		}
		return true;
	}
	
	public static byte[] transformTeamManager(byte[] classfileBuffer, ClassLoader loader) {
		if (!IS_DEBUG)
			return classfileBuffer;

		Method[] methodsToMark = new Method[] { new Method("getMemberId", "(ILjava/lang/Class;)I") };
		MarkAsStepOverAdapter adapter = new MarkAsStepOverAdapter(methodsToMark);
		ClassReader reader = new ClassReader(classfileBuffer);
		reader.accept(adapter, ClassReader.SKIP_FRAMES);
		if (adapter.transform()) {
			LoaderAwareClassWriter writer = new LoaderAwareClassWriter(reader, ClassWriter.COMPUTE_FRAMES, loader);
			adapter.accept(writer);
			return writer.toByteArray();
		}
		return classfileBuffer;
	}
}
