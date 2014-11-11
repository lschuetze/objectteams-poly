/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2010 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.compiler.ast;

import static org.eclipse.objectteams.otdt.core.compiler.IOTConstants.CALLIN_FLAG_DEFINITELY_MISSING_BASECALL;
import static org.eclipse.objectteams.otdt.core.compiler.IOTConstants.CALLIN_FLAG_POTENTIALLY_MISSING_BASECALL;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.SuperReference;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemMethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.objectteams.otdt.core.exceptions.InternalCompilerError;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.MethodModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.AstGenerator;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.TSuperHelper;

/**
 * NEW for OTDT.
 *
 * A messages send using tsuper has to perform additional checks,
 * because it is legal only if enclosing method and invoked method have
 * the same signature (except for the tsuper-mark argument).
 *
 * What: add appropriate marker argument
 * How:  before actual resolve, resolve the (possibly qualified) tsuper reference
 *       and determine from it the appropriate marker interface.
 *
 * @author stephan
 * @version $Id: TSuperMessageSend.java 23401 2010-02-02 23:56:05Z stephan $
 */
public class TSuperMessageSend extends MessageSend {

	public  TsuperReference tsuperReference;

	/** Check whether this message send contributes to base call analysis */
	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
		flowInfo = super.analyseCode(currentScope, flowContext, flowInfo);
		if (this.binding.isCallin())
		{
			MethodBinding tsuperMethod = this.binding;
			if (tsuperMethod.copyInheritanceSrc != null)
				tsuperMethod = tsuperMethod.copyInheritanceSrc;
			if (!MethodModel.hasCallinFlag(tsuperMethod, CALLIN_FLAG_DEFINITELY_MISSING_BASECALL)) { // no contribution if no base call.
				MethodDeclaration callinMethod = (MethodDeclaration)currentScope.methodScope().referenceContext;
				LocalVariableBinding trackingVariable = callinMethod.baseCallTrackingVariable.binding;
				if (MethodModel.hasCallinFlag(tsuperMethod, CALLIN_FLAG_POTENTIALLY_MISSING_BASECALL)) {
					if (   flowInfo.isDefinitelyAssigned(trackingVariable)
						|| flowInfo.isPotentiallyAssigned(trackingVariable))
					{
						currentScope.problemReporter().potentiallyDuplicateBasecall(this);
					} else {
						FlowInfo potential = flowInfo.copy();
						potential.markAsDefinitelyAssigned(trackingVariable);
						flowInfo = FlowInfo.conditional(flowInfo.initsWhenTrue(), potential.initsWhenTrue());
					}
				} else { // tsuper definitely has a base call:
					if (flowInfo.isDefinitelyAssigned(trackingVariable))
						currentScope.problemReporter().definitelyDuplicateBasecall(this);
					else if (flowInfo.isPotentiallyAssigned(trackingVariable))
						currentScope.problemReporter().potentiallyDuplicateBasecall(this);
					if (!flowInfo.isDefinitelyAssigned(trackingVariable))
						flowInfo.markAsDefinitelyAssigned(trackingVariable);
				}
			}
		}
		return flowInfo;
	}

	@Override
	protected void findMethodBinding(BlockScope scope) {
		
		// check: is a tsuper call legal in the current context?
		
		AbstractMethodDeclaration context = scope.methodScope().referenceMethod();
		if (context == null 
				|| !CharOperation.equals(this.selector, context.selector)
				|| context.binding.parameters.length != this.argumentTypes.length) 
		{
			scope.problemReporter().tsuperCallsWrongMethod(this);
			return;
		}

		ReferenceBinding receiverRole;
		if (!(this.actualReceiverType instanceof ReferenceBinding) 
				|| !(receiverRole = (ReferenceBinding)this.actualReceiverType).isSourceRole()) 
		{
			scope.problemReporter().tsuperOutsideRole(context, this, this.actualReceiverType);
			return;
		}

		ReferenceBinding[] tsuperRoleBindings = receiverRole.roleModel.getTSuperRoleBindings();
	    if (tsuperRoleBindings.length == 0) {
	    	scope.problemReporter().tsuperCallWithoutTsuperRole(receiverRole, this);
	    	return;
	    }

	    // context is OK, start searching:

	    this.tsuperReference.resolveType(scope);
	    // qualified tsuper? => directly search within the designated tsuper role:
	    if (this.tsuperReference.qualification != null) {
	    	TypeBinding tsuperRole = this.tsuperReference.resolvedType;
	    	if (tsuperRole == null || !tsuperRole.isRole())
	    		return;
	    	this.binding = scope.getMethod(tsuperRole, this.selector, this.argumentTypes, this);
	    	if (!this.binding.isValidBinding() && ((ProblemMethodBinding)this.binding).declaringClass == null)
	    		this.binding.declaringClass = (ReferenceBinding) tsuperRole;
    		resolvePolyExpressionArguments(this, this.binding, this.argumentTypes, scope);
	    	return;
	    }
	    // no qualification => search all tsupers by priority:
	    MethodBinding bestMatch = null;
	    for (int i=tsuperRoleBindings.length-1; i>=0; i--) {
	    	ReferenceBinding tsuperRole = tsuperRoleBindings[i];
	    	MethodBinding candidate = scope.getMethod(tsuperRole, this.selector, this.argumentTypes, this);
	    	if (candidate.isValidBinding()) {
	    		if (scope.parameterCompatibilityLevel(candidate, this.argumentTypes) != Scope.COMPATIBLE) {
	    			scope.problemReporter().tsuperCallsWrongMethod(this);
	    			return;
	    		}
	    		this.binding = candidate;
	    		resolvePolyExpressionArguments(this, this.binding, this.argumentTypes, scope);
	    		return;
	    	}
	    	if (bestMatch == null || 
	    			(bestMatch.problemId() == ProblemReasons.NotFound && candidate.problemId() != ProblemReasons.NotFound))
	    		bestMatch = candidate;
	    }
	    if (bestMatch == null)
	    	bestMatch = new ProblemMethodBinding(this.selector, this.argumentTypes, ProblemReasons.NotFound);
	    if (bestMatch.declaringClass == null)
	    	bestMatch.declaringClass = (ReferenceBinding) this.tsuperReference.resolvedType;
	    this.binding = bestMatch;
	}
	
	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) 
	{
		// for code gen we need to add the marker arg... 
		int len = this.binding.parameters.length;
		TypeBinding[] extendedParameters = new TypeBinding[len+1];
		System.arraycopy(this.binding.parameters, 0, extendedParameters, 0, len);
		char[] tSuperMarkName = TSuperHelper.getTSuperMarkName(this.tsuperReference.resolvedType.enclosingType());
		extendedParameters[len] = currentScope.getType(tSuperMarkName);
	
		// ... and find the copied method binding
		MethodBinding codegenBinding = currentScope.getMethod(this.actualReceiverType, this.selector, extendedParameters, this);
		
		if (codegenBinding.problemId() == ProblemReasons.NotFound) {
			// tsuper.m() may in fact refer to tsuper.super.m().
			// try to find the method as super.tsuper() instead:
			ReferenceBinding superRole = ((ReferenceBinding)this.receiver.resolvedType).superclass();
			codegenBinding = getAlternateMethod(currentScope, superRole, extendedParameters);
			if (codegenBinding == null)
				codegenBinding = getAlternateMethod(currentScope, superRole, this.binding.parameters);
			if (codegenBinding == null)
				throw new InternalCompilerError("cannot find real method binding for tsuper call!"); //$NON-NLS-1$

			this.receiver = new SuperReference(this.receiver.sourceStart, this.receiver.sourceEnd);
			this.receiver.resolvedType = superRole;
			this.receiver.constant = Constant.NotAConstant;
			this.actualReceiverType = superRole;
		}
			
		MethodBinding tsuperMethod = this.binding;
		this.binding = codegenBinding;
		try {
			super.generateCode(currentScope, codeStream, valueRequired);
		} finally {
			this.binding = tsuperMethod;
		}
		
		if (valueRequired && this.binding.isCallin()) {
			if (this.resolvedType != null && this.resolvedType.isValidBinding()) {
				if (this.resolvedType.isBaseType()) {
					// something like: ((Integer)result).intValue()
					char[][] boxtypeName= AstGenerator.boxTypeName((BaseTypeBinding)this.resolvedType);
					codeStream.checkcast(currentScope.getType(boxtypeName, 3));
					codeStream.generateUnboxingConversion(this.resolvedType.id);
				} else {
					// (RefType)result
					codeStream.checkcast(this.resolvedType);
				}
			}
		}
	}

	@SuppressWarnings("hiding")
	@Override
	public void generateArguments(MethodBinding binding, Expression[] arguments, BlockScope currentScope, CodeStream codeStream) 
	{
		super.generateArguments(binding, arguments, currentScope, codeStream);
		// check if we need to pass the marker arg, too:
		TypeBinding[] parameters = this.binding.parameters;
		TypeBinding tsuperMarkerBinding = parameters.length == 0 ? null : parameters[parameters.length-1];
		if (tsuperMarkerBinding == null || !TSuperHelper.isMarkerInterface(tsuperMarkerBinding))
			return;
		codeStream.aconst_null();
		codeStream.checkcast(tsuperMarkerBinding);
	}

	private MethodBinding getAlternateMethod(Scope scope, ReferenceBinding superRole, TypeBinding[] extendedArgumentTypes)
	{
		MethodBinding alternateMethod = scope.getMethod(superRole, this.selector, extendedArgumentTypes, this);
		if (alternateMethod.problemId() == ProblemReasons.NotVisible) {
			return alternateMethod; // want to see this error as IProblem.IndirectTSuperInvisible, cf. ProblemReporter.invalidMethod
		}
		MethodBinding alternateSrc = alternateMethod.copyInheritanceSrc;
		// TODO(SH): binary verbatim copies (no marker arg) are not recognized as copies!
		if (   alternateSrc != null
		    && isRoleOfSuperTeam(alternateSrc.declaringClass, scope))
			return alternateMethod;
		return null;
	}

	protected boolean isAnySuperAccess() {
		return true;
	}

	private boolean isRoleOfSuperTeam(ReferenceBinding roleClass, Scope scope) {
		ReferenceBinding site = scope.enclosingSourceType();
		if (!site.isRole())
			return false;
		return site.enclosingType().superclass().isCompatibleWith(roleClass.enclosingType());
	}

	@Override
	public TypeBinding resolveType(BlockScope scope) {
		TypeBinding answer = super.resolveType(scope);
		if (this.binding != null && this.binding.isCallin())
			// restore return type which has been generalized to 'Object':
			return this.resolvedType= MethodModel.getReturnType(this.binding);
	
		return answer;
	}

	public StringBuffer printExpression(int indent, StringBuffer output){
		if (this.tsuperReference != null && this.tsuperReference.qualification != null) {
			this.tsuperReference.qualification.printExpression(indent, output);
			output.append("."); //$NON-NLS-1$
		}
	    output.append("tsuper."); //$NON-NLS-1$
	    return super.printExpression(indent, output);
	}

}
