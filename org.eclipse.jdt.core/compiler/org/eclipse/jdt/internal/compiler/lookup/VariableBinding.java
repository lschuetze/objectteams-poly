/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Fraunhofer FIRST - extended API and implementation
 *     Technical University Berlin - extended API and implementation
 *     Stephan Herrmann - Contribution for
 *								bug 331649 - [compiler][null] consider null annotations for fields
 *								Bug 392099 - [1.8][compiler][null] Apply null annotation on types for null analysis
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.TeamAnchor;

/**
 * OTDT changes:
 *
 * What: Management of paths of variable bindings used as team anchor
 *       for an externalized role.
 * How:  Implemented in intermediate superclass TeamAnchor.
 *
 * What: differentiate pretty-printed and raw name of generated variables (_OT$)
 * How:  methods readableName(), internalName()
 *
 * @version $Id: VariableBinding.java 23405 2010-02-03 17:02:18Z stephan $
 */
public abstract class VariableBinding
//{ObjectTeams: pulled up additions to new intermediate superclass:
// orig: extends Binding
		extends TeamAnchor {
// SH}

	public int modifiers;
//{ObjectTeams: pulled up to TeamAnchor:
	//public TypeBinding type;
// SH}
	public char[] name;
	protected Constant constant;
	public int id; // for flow-analysis (position in flowInfo bit vector)
	public long tagBits;

	public VariableBinding(char[] name, TypeBinding type, int modifiers, Constant constant) {
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
		this.constant = constant;
		if (type != null) {
			this.tagBits |= (type.tagBits & TagBits.HasMissingType);
		}
	}

	public Constant constant() {
		return this.constant;
	}

	public abstract AnnotationBinding[] getAnnotations();

	public final boolean isBlankFinal(){
		return (this.modifiers & ExtraCompilerModifiers.AccBlankFinal) != 0;
	}

	/* Answer true if the receiver is explicitly or implicitly final
	 * and cannot be changed. Resources on try and multi catch variables are
	 * marked as implicitly final.
	*/
	public final boolean isFinal() {
		return (this.modifiers & ClassFileConstants.AccFinal) != 0;
	}
	
	public final boolean isEffectivelyFinal() {
		return (this.tagBits & TagBits.IsEffectivelyFinal) != 0;
	}

	/** Answer true if null annotations are enabled and this field is specified @NonNull */
	public boolean isNonNull() {
		return (this.tagBits & TagBits.AnnotationNonNull) != 0
				|| (this.type != null 
					&& (this.type.tagBits & TagBits.AnnotationNonNull) != 0);
	}

	/** Answer true if null annotations are enabled and this field is specified @Nullable */
	public boolean isNullable() {
		return (this.tagBits & TagBits.AnnotationNullable) != 0
				|| (this.type != null 
				&& (this.type.tagBits & TagBits.AnnotationNullable) != 0);
	}

	public char[] readableName() {
//{ObjectTeams: pretty printing for generated names:
	if (CharOperation.prefixEquals(IOTConstants.OT_DOLLAR_NAME, this.name))
		return CharOperation.subarray(this.name, IOTConstants.OT_DOLLAR_LEN, -1);
// SH}
		return this.name;
	}
//{ObjectTeams: un-pretty-printed (original) version:
public char[] internalName() {
		return this.name;
	}
// SH}
	public void setConstant(Constant constant) {
		this.constant = constant;
	}
	public String toString() {
		StringBuffer output = new StringBuffer(10);
		ASTNode.printModifiers(this.modifiers, output);
		if ((this.modifiers & ExtraCompilerModifiers.AccUnresolved) != 0) {
			output.append("[unresolved] "); //$NON-NLS-1$
		}
		output.append(this.type != null ? this.type.debugName() : "<no type>"); //$NON-NLS-1$
		output.append(" "); //$NON-NLS-1$
		output.append((this.name != null) ? new String(this.name) : "<no name>"); //$NON-NLS-1$
		return output.toString();
	}
}
