/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Fraunhofer FIRST - extended API and implementation
 *     Technical University Berlin - extended API and implementation
 *     Stephan Herrmann - Contribution for
 *							Bug 400874 - [1.8][compiler] Inference infrastructure should evolve to meet JLS8 18.x (Part G of JSR335 spec)
 *							Bug 433478 - [compiler][null] NPE in ReferenceBinding.isCompatibleWith
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.AnchorMapping;

/**
 * OTDT change:
 *
 * What: If a closest match is given, replace it with instantiated copy.
 */
public class ProblemMethodBinding extends MethodBinding {

	private int problemReason;
	public MethodBinding closestMatch; // TODO (philippe) should rename into #alternateMatch
	public InferenceContext18 inferenceContext; // inference context may help to coordinate error reporting

public ProblemMethodBinding(char[] selector, TypeBinding[] args, int problemReason) {
	this.selector = selector;
	this.parameters = (args == null || args.length == 0) ? Binding.NO_PARAMETERS : args;
	this.problemReason = problemReason;
	this.thrownExceptions = Binding.NO_EXCEPTIONS;
}
public ProblemMethodBinding(char[] selector, TypeBinding[] args, ReferenceBinding declaringClass, int problemReason) {
	this.selector = selector;
	this.parameters = (args == null || args.length == 0) ? Binding.NO_PARAMETERS : args;
	this.declaringClass = declaringClass;
	this.problemReason = problemReason;
	this.thrownExceptions = Binding.NO_EXCEPTIONS;
}
public ProblemMethodBinding(MethodBinding closestMatch, char[] selector, TypeBinding[] args, int problemReason) {
	this(selector, args, problemReason);
	this.closestMatch = closestMatch;
//{ObjectTeams:
	if (closestMatch != null) {
		// check for need to instantiate parameters;
		TypeBinding[] newParameters = AnchorMapping.instantiateParameters(null, closestMatch.parameters, closestMatch);
		// if instantiating was actually performed, we need a clone to hold the new parameters:
		if (newParameters != closestMatch.parameters) {
			// clone 'closestMatch' depending on its dynamic type:
			MethodBinding clone = getMethodClone(closestMatch);
			if (clone != null) {
				clone.parameters = newParameters;
				this.closestMatch = clone;
			}
		}
	}
// SH}
	if (closestMatch != null && problemReason != ProblemReasons.Ambiguous) {
		this.declaringClass = closestMatch.declaringClass;
		this.returnType = closestMatch.returnType;
		if (problemReason == ProblemReasons.InvocationTypeInferenceFailure) {
			this.thrownExceptions = closestMatch.thrownExceptions;
			this.typeVariables = closestMatch.typeVariables;
			this.modifiers = closestMatch.modifiers;
			this.tagBits = closestMatch.tagBits;
		}
	}
}

//{ObjectTeams: Clone closestMatch before manipulation.
private MethodBinding getMethodClone(MethodBinding givenClosestMatch) {
	if(givenClosestMatch instanceof ParameterizedGenericMethodBinding) {
		ParameterizedGenericMethodBinding pgmb = (ParameterizedGenericMethodBinding)givenClosestMatch;
		MethodBinding clone = new ParameterizedGenericMethodBinding(givenClosestMatch, pgmb.typeArguments, pgmb.environment(), false, false, pgmb.targetType);
		clone.declaringClass = givenClosestMatch.declaringClass;
		return clone;
	} else if(givenClosestMatch instanceof ParameterizedMethodBinding) {
		if (givenClosestMatch.declaringClass instanceof ParameterizedTypeBinding)
			return new ParameterizedMethodBinding((ParameterizedTypeBinding)givenClosestMatch.declaringClass, givenClosestMatch);
		else
			return null;
	} else {
		return new MethodBinding(givenClosestMatch, givenClosestMatch.declaringClass);
	}
}
// SH}

@Override
public MethodBinding computeSubstitutedMethod(MethodBinding method, LookupEnvironment env) {
	return this.closestMatch == null ? this : this.closestMatch.computeSubstitutedMethod(method, env);
}
@Override
public MethodBinding findOriginalInheritedMethod(MethodBinding inheritedMethod) {
	return this.closestMatch == null ? this : this.closestMatch.findOriginalInheritedMethod(inheritedMethod);
}
@Override
public MethodBinding genericMethod() {
	return this.closestMatch == null ? this : this.closestMatch.genericMethod();
}
@Override
public MethodBinding original() {
	return this.closestMatch == null ? this : this.closestMatch.original();
}
@Override
public MethodBinding shallowOriginal() {
	return this.closestMatch == null ? this : this.closestMatch.shallowOriginal();
}
@Override
public MethodBinding tiebreakMethod() {
	return this.closestMatch == null ? this : this.closestMatch.tiebreakMethod();
}
@Override
public boolean hasSubstitutedParameters() {
	if (this.closestMatch != null)
		return this.closestMatch.hasSubstitutedParameters();
	return false;
}
@Override
public boolean isParameterizedGeneric() {
	return this.closestMatch instanceof ParameterizedGenericMethodBinding;
}
/** API
 * Answer the problem id associated with the receiver.
 * NoError if the receiver is a valid binding.
 */
@Override
public final int problemId() {
	return this.problemReason;
}
}
