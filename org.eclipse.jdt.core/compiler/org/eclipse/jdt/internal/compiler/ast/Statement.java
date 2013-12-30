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
 *     Stephan Herrmann - Contributions for
 *								bug 335093 - [compiler][null] minimal hook for future null annotation support
 *								bug 349326 - [1.7] new warning for missing try-with-resources
 *								bug 186342 - [compiler][null] Using annotations for null checking
 *								bug 365983 - [compiler][null] AIOOB with null annotation analysis and varargs
 *								bug 368546 - [compiler][resource] Avoid remaining false positives found when compiling the Eclipse SDK
 *								bug 370930 - NonNull annotation not considered for enhanced for loops
 *								bug 365859 - [compiler][null] distinguish warnings based on flow analysis vs. null annotations
 *								bug 392862 - [1.8][compiler][null] Evaluate null annotations on array types
 *								bug 331649 - [compiler][null] consider null annotations for fields
 *								bug 383368 - [compiler][null] syntactic null analysis for field references
 *								Bug 392099 - [1.8][compiler][null] Apply null annotation on types for null analysis
 *								Bug 415043 - [1.8][null] Follow-up re null type annotations after bug 392099
 *								Bug 415291 - [1.8][null] differentiate type incompatibilities due to null annotations
 *								Bug 392238 - [1.8][compiler][null] Detect semantically invalid null type annotations
 *								Bug 416307 - [1.8][compiler][null] subclass with type parameter substitution confuses null checking
 *        Andy Clement - Contributions for
 *                          Bug 383624 - [1.8][compiler] Revive code generation support for type annotations (from Olivier's work)
 *                          Bug 409250 - [1.8][compiler] Various loose ends in 308 code generation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.mappings.CallinImplementorDyn;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.MethodModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.TeamModel;

/**
 * OTDT changes:
 * What: ignore unreachable generated "return _OT$result"
 */
public abstract class Statement extends ASTNode {

	/**
	 * Answers true if the if is identified as a known coding pattern which
	 * should be tolerated by dead code analysis.
	 * e.g. if (DEBUG) print(); // no complaint
	 * Only invoked when overall condition is known to be optimizeable into false/true.
	 */
	protected static boolean isKnowDeadCodePattern(Expression expression) {
		// if (!DEBUG) print(); - tolerated
		if (expression instanceof UnaryExpression) {
			expression = ((UnaryExpression) expression).expression;
		}
		// if (DEBUG) print(); - tolerated
		if (expression instanceof Reference) return true;

//		if (expression instanceof BinaryExpression) {
//			BinaryExpression binary = (BinaryExpression) expression;
//			switch ((binary.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT/* operator */) {
//				case OperatorIds.AND_AND :
//				case OperatorIds.OR_OR :
//					break;
//				default: 
//					// if (DEBUG_LEVEL > 0) print(); - tolerated
//					if ((binary.left instanceof Reference) && binary.right.constant != Constant.NotAConstant)
//						return true;
//					// if (0 < DEBUG_LEVEL) print(); - tolerated
//					if ((binary.right instanceof Reference) && binary.left.constant != Constant.NotAConstant)
//						return true;
//			}
//		}
		return false;
	}
public abstract FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo);

	public static final int NOT_COMPLAINED = 0;
	public static final int COMPLAINED_FAKE_REACHABLE = 1;
	public static final int COMPLAINED_UNREACHABLE = 2;
	

/** Analysing arguments of MessageSend, ExplicitConstructorCall, AllocationExpression. */
protected void analyseArguments(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, MethodBinding methodBinding, Expression[] arguments)
{
	// compare actual null-status against parameter annotations of the called method:
	if (arguments != null) {
		CompilerOptions compilerOptions = currentScope.compilerOptions();
		boolean considerTypeAnnotations = compilerOptions.sourceLevel >= ClassFileConstants.JDK1_8
				&& compilerOptions.isAnnotationBasedNullAnalysisEnabled;
		boolean hasJDK15NullAnnotations = methodBinding.parameterNonNullness != null;
		int numParamsToCheck = methodBinding.parameters.length;
		TypeBinding varArgsType = null;
		boolean passThrough = false;
		if (considerTypeAnnotations || hasJDK15NullAnnotations) {
			// check if varargs need special treatment:
			if (methodBinding.isVarargs()) {
				int varArgPos = numParamsToCheck-1;
				// this if-block essentially copied from generateArguments(..):
				if (numParamsToCheck == arguments.length) {
					varArgsType = methodBinding.parameters[varArgPos];
					TypeBinding lastType = arguments[varArgPos].resolvedType;
					if (lastType == TypeBinding.NULL
							|| (varArgsType.dimensions() == lastType.dimensions()
							&& lastType.isCompatibleWith(varArgsType)))
						passThrough = true; // pass directly as-is
				}
				if (!passThrough)
					numParamsToCheck--; // with non-passthrough varargs last param is fed from individual args -> don't check
			}
		}
		if (considerTypeAnnotations) {
			for (int i=0; i<numParamsToCheck; i++) {
				TypeBinding expectedType = methodBinding.parameters[i];
				analyseOneArgument18(currentScope, flowContext, flowInfo, expectedType, arguments[i]);
			}
			if (!passThrough && varArgsType instanceof ArrayBinding) {
				TypeBinding expectedType = ((ArrayBinding) varArgsType).elementsType();
				for (int i = numParamsToCheck; i < arguments.length; i++) {
					analyseOneArgument18(currentScope, flowContext, flowInfo, expectedType, arguments[i]);
				}
			}
		} else if (hasJDK15NullAnnotations) {
			for (int i = 0; i < numParamsToCheck; i++) {
				if (methodBinding.parameterNonNullness[i] == Boolean.TRUE) {
					TypeBinding expectedType = methodBinding.parameters[i];
					Expression argument = arguments[i];
					int nullStatus = argument.nullStatus(flowInfo, flowContext); // slight loss of precision: should also use the null info from the receiver.
					if (nullStatus != FlowInfo.NON_NULL) // if required non-null is not provided
						flowContext.recordNullityMismatch(currentScope, argument, argument.resolvedType, expectedType, nullStatus);
				}
			}
		} 
	}
}
void analyseOneArgument18(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo,
		TypeBinding expectedType, Expression argument) {
	int nullStatus = argument.nullStatus(flowInfo, flowContext);
	NullAnnotationMatching annotationStatus = NullAnnotationMatching.analyse(expectedType, argument.resolvedType, nullStatus);
	if (annotationStatus.isDefiniteMismatch()) {
		// immediate reporting:
		currentScope.problemReporter().nullityMismatchingTypeAnnotation(argument, argument.resolvedType, expectedType, annotationStatus);
	} else if (annotationStatus.isUnchecked()) {
		flowContext.recordNullityMismatch(currentScope, argument, argument.resolvedType, expectedType, nullStatus);
	}
}

/**
 * INTERNAL USE ONLY.
 * This is used to redirect inter-statements jumps.
 */
public void branchChainTo(BranchLabel label) {
	// do nothing by default
}

// Report an error if necessary (if even more unreachable than previously reported
// complaintLevel = 0 if was reachable up until now, 1 if fake reachable (deadcode), 2 if fatal unreachable (error)
public int complainIfUnreachable(FlowInfo flowInfo, BlockScope scope, int previousComplaintLevel, boolean endOfBlock) {
	if ((flowInfo.reachMode() & FlowInfo.UNREACHABLE) != 0) {
		if ((flowInfo.reachMode() & FlowInfo.UNREACHABLE_OR_DEAD) != 0)
			this.bits &= ~ASTNode.IsReachable;
//{ObjectTeams: ignore generated "return _OT$result" (generateCode will also ignore this statement):
		boolean shouldReport = true;
		if (this instanceof ReturnStatement) {
			ReturnStatement returnStat = (ReturnStatement)this;
			if (returnStat.expression instanceof SingleNameReference) {
				if (CharOperation.equals(
						((SingleNameReference)returnStat.expression).token,
						IOTConstants.OT_RESULT))
				{
					shouldReport = false;
				}
			}
		}
// orig:
		if (flowInfo == FlowInfo.DEAD_END) {
			if (previousComplaintLevel < COMPLAINED_UNREACHABLE) {
/* OT: */	  if (shouldReport)
				scope.problemReporter().unreachableCode(this);
				if (endOfBlock)
					scope.checkUnclosedCloseables(flowInfo, null, null, null);
			}
			return COMPLAINED_UNREACHABLE;
		} else {
			if (previousComplaintLevel < COMPLAINED_FAKE_REACHABLE) {
/* OT: */	  if (shouldReport)			
// SH}
				scope.problemReporter().fakeReachable(this);
				if (endOfBlock)
					scope.checkUnclosedCloseables(flowInfo, null, null, null);
			}
			return COMPLAINED_FAKE_REACHABLE;
		}
	}
	return previousComplaintLevel;
}

/**
 * Generate invocation arguments, considering varargs methods
 */
public void generateArguments(MethodBinding binding, Expression[] arguments, BlockScope currentScope, CodeStream codeStream) {
	if (binding.isVarargs()) {
		// 5 possibilities exist for a call to the vararg method foo(int i, int ... value) :
		//      foo(1), foo(1, null), foo(1, 2), foo(1, 2, 3, 4) & foo(1, new int[] {1, 2})
		TypeBinding[] params = binding.parameters;
		int paramLength = params.length;
		int varArgIndex = paramLength - 1;
		for (int i = 0; i < varArgIndex; i++) {
			arguments[i].generateCode(currentScope, codeStream, true);
		}
		ArrayBinding varArgsType = (ArrayBinding) params[varArgIndex]; // parameterType has to be an array type
		ArrayBinding codeGenVarArgsType = (ArrayBinding) binding.parameters[varArgIndex].erasure();
		int elementsTypeID = varArgsType.elementsType().id;
		int argLength = arguments == null ? 0 : arguments.length;

		if (argLength > paramLength) {
			// right number but not directly compatible or too many arguments - wrap extra into array
			// called with (argLength - lastIndex) elements : foo(1, 2) or foo(1, 2, 3, 4)
			// need to gen elements into an array, then gen each remaining element into created array
			codeStream.generateInlinedValue(argLength - varArgIndex);
			codeStream.newArray(codeGenVarArgsType); // create a mono-dimensional array
			for (int i = varArgIndex; i < argLength; i++) {
				codeStream.dup();
				codeStream.generateInlinedValue(i - varArgIndex);
				arguments[i].generateCode(currentScope, codeStream, true);
				codeStream.arrayAtPut(elementsTypeID, false);
			}
		} else if (argLength == paramLength) {
			// right number of arguments - could be inexact - pass argument as is
			TypeBinding lastType = arguments[varArgIndex].resolvedType;
			if (lastType == TypeBinding.NULL
				|| (varArgsType.dimensions() == lastType.dimensions()
					&& lastType.isCompatibleWith(varArgsType))) {
				// foo(1, new int[]{2, 3}) or foo(1, null) --> last arg is passed as-is
				arguments[varArgIndex].generateCode(currentScope, codeStream, true);
			} else {
				// right number but not directly compatible or too many arguments - wrap extra into array
				// need to gen elements into an array, then gen each remaining element into created array
				codeStream.generateInlinedValue(1);
				codeStream.newArray(codeGenVarArgsType); // create a mono-dimensional array
				codeStream.dup();
				codeStream.generateInlinedValue(0);
				arguments[varArgIndex].generateCode(currentScope, codeStream, true);
				codeStream.arrayAtPut(elementsTypeID, false);
			}
		} else { // not enough arguments - pass extra empty array
			// scenario: foo(1) --> foo(1, new int[0])
			// generate code for an empty array of parameterType
			codeStream.generateInlinedValue(0);
			codeStream.newArray(codeGenVarArgsType); // create a mono-dimensional array
		}
	} else if (arguments != null) { // standard generation for method arguments
//{ObjectTeams: decapsulation under OTREDyn requires packing into an array etc.:
		if (   CharOperation.equals(CallinImplementorDyn.OT_ACCESS, binding.selector)
			|| CharOperation.equals(CallinImplementorDyn.OT_ACCESS_STATIC, binding.selector)) 
		{
			SourceTypeBinding roleType = currentScope.enclosingSourceType();
			// adjust the target method binding
			binding.parameters = new TypeBinding[] {
											TypeBinding.INT,
											TypeBinding.INT,
											currentScope.createArrayType(currentScope.getJavaLangObject(), 1),
											currentScope.getOrgObjectteamsITeam() 
									};
			binding.returnType = currentScope.getJavaLangObject();
			// accessId:
			codeStream.generateInlinedValue(MethodModel.getMethodAccessId(binding));
			// opkind (not used for method access):
			codeStream.iconst_0();
			// pack original arguments into a new Object[]
			codeStream.generateInlinedValue(arguments.length);
			codeStream.anewarray(currentScope.getJavaLangObject());
			for (int i = 0, max = arguments.length; i < max; i++) {
				codeStream.dup();
				codeStream.generateInlinedValue(i);
				arguments[i].generateCode(currentScope, codeStream, true);
				if (arguments[i].resolvedType.isBaseType())
					codeStream.generateBoxingConversion(arguments[i].resolvedType.id);
				codeStream.aastore();
			}
			// pass enclosing team:
			Object[] path = currentScope.getEmulationPath(roleType.enclosingType(), true, false);
			codeStream.generateOuterAccess(path, null, roleType.enclosingType(), currentScope);
			return;
		}
// SH}
		for (int i = 0, max = arguments.length; i < max; i++)
//{ObjectTeams: check for need for role-ifc to plain-class cast:
		{
			TypeBinding[] originalParameters = binding.original().parameters;
// orig:
			arguments[i].generateCode(currentScope, codeStream, true);
// :giro
			if (i < originalParameters.length) { // consider polymorphic signatures
				TypeBinding requiredType = checkRoleToPlainCast(arguments[i].resolvedType, originalParameters[i]);
				if (requiredType != null)
					codeStream.checkcast(requiredType);
			}
		}
// SH}
	}
}

//{ObjectTeams:
private TypeBinding checkRoleToPlainCast(TypeBinding providedType, TypeBinding requiredBinding) {
	if (   providedType.isRole() 
		&& !TeamModel.isTeamContainingRole(((ReferenceBinding)providedType).enclosingType(), (ReferenceBinding) requiredBinding))
		return requiredBinding;
	return null;
}
// SH}

public abstract void generateCode(BlockScope currentScope, CodeStream codeStream);

protected boolean isBoxingCompatible(TypeBinding expressionType, TypeBinding targetType, Expression expression, Scope scope) {
	if (scope.isBoxingCompatibleWith(expressionType, targetType))
		return true;

	return expressionType.isBaseType()  // narrowing then boxing ? Only allowed for some target types see 362279
		&& !targetType.isBaseType()
		&& !targetType.isTypeVariable()
		&& scope.compilerOptions().sourceLevel >= org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants.JDK1_5 // autoboxing
		&& (targetType.id == TypeIds.T_JavaLangByte || targetType.id == TypeIds.T_JavaLangShort || targetType.id == TypeIds.T_JavaLangCharacter)
		&& expression.isConstantValueOfTypeAssignableToType(expressionType, scope.environment().computeBoxingType(targetType));
}

public boolean isEmptyBlock() {
	return false;
}

public boolean isValidJavaStatement() {
	//the use of this method should be avoid in most cases
	//and is here mostly for documentation purpose.....
	//while the parser is responsible for creating
	//welled formed expression statement, which results
	//in the fact that java-non-semantic-expression-used-as-statement
	//should not be parsed...thus not being built.
	//It sounds like the java grammar as help the compiler job in removing
	//-by construction- some statement that would have no effect....
	//(for example all expression that may do side-effects are valid statement
	// -this is an approximative idea.....-)

	return true;
}

public StringBuffer print(int indent, StringBuffer output) {
	return printStatement(indent, output);
}

public abstract StringBuffer printStatement(int indent, StringBuffer output);

public abstract void resolve(BlockScope scope);

/**
 * Returns case constant associated to this statement (NotAConstant if none)
 */
public Constant resolveCase(BlockScope scope, TypeBinding testType, SwitchStatement switchStatement) {
	// statement within a switch that are not case are treated as normal statement....
	resolve(scope);
	return Constant.NotAConstant;
}
/** 
 * Implementation of {@link org.eclipse.jdt.internal.compiler.lookup.InvocationSite#expectedType}
 * suitable at this level. Subclasses should override as necessary.
 * @see org.eclipse.jdt.internal.compiler.lookup.InvocationSite#expectedType()
 */
public TypeBinding expectedType() {
	return null;
}
}
