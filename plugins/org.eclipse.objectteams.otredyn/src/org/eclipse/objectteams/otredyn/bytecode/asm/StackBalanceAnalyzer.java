/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2014 Stephan Herrmann.
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

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;

public class StackBalanceAnalyzer {

    /**
     * Answer an array of insertion points such that each element points to the start of a loading sequence
     * that produces as many bytes on the stack as are required for the corresponding type in 'args'.
     * 
     * @param location point in an instruction list at which all args are present on the stack
     * @param args types of the arguments on the top of the stack at 'location'
     * @return one instruction per element in 'args'
     */
	public static AbstractInsnNode[] findInsertionPointsBefore(AbstractInsnNode location, Type[] args) {
		AbstractInsnNode[] nodes = new AbstractInsnNode[args.length];
		AbstractInsnNode current = location;
		for (int i = args.length-1; i >= 0; i--) {
			current = findInsertionPointBefore(current, - args[i].getSize());
			nodes[args.length-i-1] = current;
		}
		return nodes;
	}

    /**
     * Answer the closest point in the instruction list before 'location'
     * where the stack has 'stackDifference' more bytes than at 'location'.
     * Negative 'stackDifference' means we are looking for smaller stack.
     * 
     * @param location start searching here
     * @param stackDifference 
     * @return the last instruction before which the required stack size was present 
     */
    public static AbstractInsnNode findInsertionPointBefore(AbstractInsnNode location, int stackDifference) {
    	int offset = 0;    	
    	AbstractInsnNode current = location;
    	while (offset != stackDifference) {
    		current = location.getPrevious();
    		if (current == null)
    			return null;
    		offset -= SIZE[current.getOpcode()];
    	}
    	return current;
    }

	
	// Field from org.objectweb.asm.Frame, made accessible:
    /**
     * The stack size variation corresponding to each JVM instruction. This
     * stack variation is equal to the size of the values produced by an
     * instruction, minus the size of the values consumed by this instruction.
     */
    static final int[] SIZE;

    /**
     * Computes the stack size variation corresponding to each JVM instruction.
     */
    static {
        int i;
        int[] b = new int[202];
        String s = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDD"
                + "CDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCD"
                + "CDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFED"
                + "DDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
        for (i = 0; i < b.length; ++i) {
            b[i] = s.charAt(i) - 'E';
        }
        SIZE = b;
    }
    
}
