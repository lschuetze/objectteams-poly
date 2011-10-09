/*******************************************************************************
 * Copyright (c) 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.objectteams.internal.jdt.nullity;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CharOperation;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MarkerAnnotation;
import org.eclipse.jdt.internal.compiler.ast.MemberValuePair;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.ast.SubRoutineStatement;
import org.eclipse.jdt.internal.compiler.ast.SynchronizedStatement;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.IBinaryAnnotation;
import org.eclipse.jdt.internal.compiler.env.IBinaryMethod;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.InitializationFlowContext;
import org.eclipse.jdt.internal.compiler.flow.InsideSubRoutineFlowContext;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.jdt.internal.compiler.impl.IrritantSet;
import org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.ImportBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.UnresolvedReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.eclipse.jdt.internal.compiler.util.HashtableOfInt;
import org.objectteams.Instantiation;
import org.objectteams.InstantiationPolicy;

import static org.eclipse.objectteams.internal.jdt.nullity.Constants.IProblem;
import static org.eclipse.objectteams.internal.jdt.nullity.Constants.TagBits;
import static org.eclipse.objectteams.internal.jdt.nullity.Constants.TypeIds;

import base org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import base org.eclipse.jdt.internal.compiler.ast.Assignment;
import base org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import base org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import base org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import base org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import base org.eclipse.jdt.internal.compiler.ast.MessageSend;
import base org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import base org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import base org.eclipse.jdt.internal.compiler.ast.Statement;
import base org.eclipse.jdt.internal.compiler.flow.LoopingFlowContext;
import base org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext;
import base org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import base org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding;
import base org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import base org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import base org.eclipse.jdt.internal.compiler.lookup.MethodVerifier15;
import base org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import base org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import base org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import base org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import base org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import base org.eclipse.jdt.internal.core.JavaProject;
import base org.eclipse.jdt.internal.core.JavaModelManager;

/**
 * This team class adds the implementation from
 * https://bugs.eclipse.org/bugs/186342 - [compiler][null] Using annotations for null checking
 * to the compiler.
 * 
 * It essentially reflects the state of https://bugs.eclipse.org/bugs/attachment.cgi?id=186890
 */
@SuppressWarnings("restriction")
public team class CompilerAdaptation {
	
	public CompilerAdaptation () {
		// add more irritants to IrritantSet:
		IrritantSet.COMPILER_DEFAULT_ERRORS.set( NullCompilerOptions.NullContractViolation
				 							    |NullCompilerOptions.PotentialNullContractViolation);
		IrritantSet.COMPILER_DEFAULT_WARNINGS.set(NullCompilerOptions.NullContractInsufficientInfo);
		IrritantSet.NULL.set( NullCompilerOptions.NullContractViolation
							 |NullCompilerOptions.PotentialNullContractViolation
							 |NullCompilerOptions.NullContractInsufficientInfo);
	}

	// ======================= Statement level analysis ============================
	
	@SuppressWarnings({"abstractrelevantrole", "hidden-lifting-problem"}) // due to abstractness of this role failed lifting could theoretically block callin triggers
	protected abstract class Statement playedBy Statement {
		abstract Expression getExpression();
		
		FlowContext flowContext;
		void storeFlowContext(BlockScope scope, FlowContext flowContext) {
			this.flowContext = flowContext;
		}
		
		// use custom hook from JDT/Core (https://bugs.eclipse.org/335093)
		// TODO(SH): should calls to this method be guarded by "if (flowInfo.reachMode() == FlowInfo.REACHABLE)"?
		//           otherwise we'll proceed with incomplete information (normal null analysis avoids dead code).
		checkAgainstNullAnnotation <- replace checkAgainstNullAnnotation;

		/** Check assignment to local with null annotation. */
		@SuppressWarnings("basecall")
		callin int checkAgainstNullAnnotation(BlockScope currentScope, LocalVariableBinding local,int nullStatus)
		{
			if (   local != null
				&& (local.tagBits & TagBits.AnnotationNonNull) != 0 
				&& nullStatus != FlowInfo.NON_NULL)
			{
				recordNullityMismatch(currentScope, flowContext, getExpression(), local.type, 
						nullStatus, currentScope.environment().getNonNullAnnotationName());
				nullStatus=FlowInfo.NON_NULL;
			}
			return nullStatus;
		}
		
	}
	protected class Assignment extends Statement playedBy Assignment {
		/** Wire required method of super class. */
		Expression getExpression() -> get Expression expression;
		void storeFlowContext(BlockScope scope, FlowContext flowContext) 
		<- before FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo);
	}
	protected class LocalDeclaration extends Statement playedBy LocalDeclaration {
		/** Wire required method of super class. */
		Expression getExpression() -> get Expression initialization;
		void storeFlowContext(BlockScope scope, FlowContext flowContext) 
		<- before FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo);
	}
	
	protected abstract class MessageSendish {
		
		protected abstract Expression[] getArguments();
		protected abstract MethodBinding getBinding();
		
		void analyseArguments(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
			// compare actual null-status against parameter annotations of the called method:
			MethodBinding methodBinding = getBinding();
			Expression[] arguments = getArguments();
			if (arguments != null && methodBinding.parameterNonNullness != null) {
				char[][] annotationName = currentScope.environment().getNonNullAnnotationName();
				for (int i = 0; i < arguments.length; i++) {
					if (methodBinding.parameterNonNullness[i] == Boolean.TRUE) { 
						TypeBinding expectedType = methodBinding.getParameters()[i];
						Expression argument = arguments[i];
						int nullStatus = argument.nullStatus(flowInfo); // slight loss of precision: should also use the null info from the receiver.
						if (nullStatus != FlowInfo.NON_NULL) // if required non-null is not provided
							recordNullityMismatch(currentScope, flowContext, argument, expectedType, nullStatus, annotationName);
					}
				}
			}
		}
	}
	
	void recordNullityMismatch(BlockScope currentScope, FlowContext flowContext, Expression expression, TypeBinding expectedType, int nullStatus, char[][] annotationName) {
		if (expression.localVariableBinding() != null) { // flowContext cannot yet handle non-localvar expressions (e.g., fields)
			while (flowContext != null) {
				// some flow contexts implement deferred checking, should we participate in that?
				if (flowContext instanceof org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext) {
					// cf. decision structure inside FinallyFlowContext.recordUsingNullReference(..)
					if (nullStatus == FlowInfo.UNKNOWN ||
							((flowContext.tagBits & FlowContext.DEFER_NULL_DIAGNOSTIC) != 0 && nullStatus != FlowInfo.NULL)) 
					{
						// deferred reporting
						recordNullReference((org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext)flowContext,
									expression, expectedType, ConditionalFlowContext.ASSIGN_TO_NONNULL);										
						return;
					}
				}
				else if (flowContext instanceof org.eclipse.jdt.internal.compiler.flow.LoopingFlowContext) {
					// deferred reporting
					recordNullReference((org.eclipse.jdt.internal.compiler.flow.LoopingFlowContext)flowContext,
								expression, expectedType, ConditionalFlowContext.ASSIGN_TO_NONNULL);
					return;
				}
				flowContext = flowContext.parent;
			}
		}			
		// other cases report immediately
		currentScope.problemReporter().nullityMismatch(expression, expectedType, nullStatus, annotationName);
	}
	
	/** Analyse argument expressions as part of a MessageSend, check against method parameter annotation. */
	@Instantiation(InstantiationPolicy.ALWAYS)
	protected class MessageSend extends MessageSendish playedBy MessageSend {

		Expression[] getArguments() -> get Expression[] arguments;
		MethodBinding getBinding() -> get MethodBinding binding;

		nullStatus <- replace nullStatus;

		@SuppressWarnings("basecall")
		callin int nullStatus(FlowInfo flowInfo) {
			int status = getNullStatus();
			if (status != FlowInfo.UNKNOWN)
				return status;
			return base.nullStatus(flowInfo);
		}
		
		void analyseArguments(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) 
		<- after FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo);

		checkNPE <- after checkNPE;
		/** Detect and signal directly dereferencing a nullable message send result. */
		void checkNPE(BlockScope scope, FlowContext flowContext, FlowInfo flowInfo) {
			if (getNullStatus() == FlowInfo.POTENTIALLY_NULL)
				scope.problemReporter().messageSendPotentialNullReference(getBinding(), this);
		}
		
		protected int getNullStatus() {
			MethodBinding binding = this.getBinding();
			if (binding.isValidBinding()) {
				// try to retrieve null status of this message send from an annotation of the called method:
				long tagBits = binding.getTagBits();
				if ((tagBits & TagBits.AnnotationNonNull) != 0)
					return FlowInfo.NON_NULL;
				if ((tagBits & TagBits.AnnotationNullable) != 0)
					return FlowInfo.POTENTIALLY_NULL;
			}
			return FlowInfo.UNKNOWN;
		}
	}
	protected class ExplicitConstructorCall extends MessageSendish playedBy ExplicitConstructorCall {

		Expression[] getArguments() -> get Expression[] arguments;
		MethodBinding getBinding() -> get MethodBinding binding;
		
		
		void analyseArguments(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) 
		<- after FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo);

	}
	
	@Instantiation(InstantiationPolicy.ALWAYS)
	protected class EqualExpression playedBy EqualExpression {
		
		MessageSend getLeftMessage() 		 -> get Expression left
			with { result					 <- (left instanceof MessageSend) ? (MessageSend) left : null }
		int getLeftNullStatus(FlowInfo info) -> get Expression left
			with { result					 <- left.nullStatus(info) }
		MessageSend getRightMessage() 		 -> get Expression right
			with { result					 <- (right instanceof MessageSend) ? (MessageSend) right : null }
		int getRightNullStatus(FlowInfo info)-> get Expression right
			with { result					 <- right.nullStatus(info) }
		
		
		checkNullComparison <- before checkNullComparison;

		/** Detect and signal when comparing a non-null message send against null. */
		void checkNullComparison(BlockScope scope, FlowContext flowContext, FlowInfo flowInfo,
				FlowInfo initsWhenTrue, FlowInfo initsWhenFalse) 
		{
			MessageSend leftMessage = getLeftMessage();
			if (   leftMessage != null 
				&& leftMessage.getNullStatus() == FlowInfo.NON_NULL
				&& getRightNullStatus(flowInfo) == FlowInfo.NULL) 
			{
				scope.problemReporter().messageSendRedundantCheckOnNonNull(leftMessage.getBinding(), leftMessage);
			}
			MessageSend rightMessage = getRightMessage();
			if (   rightMessage != null 
				&& rightMessage.getNullStatus() == FlowInfo.NON_NULL 
				&& getLeftNullStatus(flowInfo) == FlowInfo.NULL) 
			{
				scope.problemReporter().messageSendRedundantCheckOnNonNull(rightMessage.getBinding(), rightMessage);
			}
		}		
	}
	
	@SuppressWarnings("bindingconventions")
	protected class NodeWithBits playedBy ASTNode {
		int getBits() 							-> get int bits;
		public void addBit(int bit) 	 		-> set int bits 
			with { bit | getBits() 				-> bits }

	}
	
	/** Analyse the expression within a return statement, check against method return annotation. */
	@Instantiation(InstantiationPolicy.ALWAYS)
	protected class ReturnStatement extends NodeWithBits playedBy ReturnStatement {

		Expression getExpression() 	-> get Expression expression;
		int getSourceStart() 		-> get int sourceStart;
		int getSourceEnd() 			-> get int sourceEnd;

		FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) 
		<- replace FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo);

		
		@SuppressWarnings({ "inferredcallout", "basecall", "decapsulation" })
		callin FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
			if (this.expression != null) {
				// workaround for Bug 354480 - VerifyError due to bogus lowering in inferred callout-to-field
				org.eclipse.jdt.internal.compiler.lookup.BlockScope blockScope = currentScope;
				flowInfo = this.expression.analyseCode(blockScope, flowContext, flowInfo);
				if ((this.expression.implicitConversion & org.eclipse.jdt.internal.compiler.lookup.TypeIds.UNBOXING) != 0) {
					this.expression.checkNPE(blockScope, flowContext, flowInfo);
				}
				if (flowInfo.reachMode() == FlowInfo.REACHABLE)
					checkAgainstNullAnnotation(currentScope, this.expression.nullStatus(flowInfo));
			}
			this.initStateIndex =
				currentScope.methodScope().recordInitializationStates(flowInfo);
			// compute the return sequence (running the finally blocks)
			FlowContext traversedContext = flowContext;
			int subCount = 0;
			boolean saveValueNeeded = false;
			boolean hasValueToSave = needValueStore();
			do {
				SubRoutineStatement sub;
				if ((sub = traversedContext.subroutine()) != null) {
					if (this.subroutines == null){
						this.subroutines = new SubRoutineStatement[5];
					}
					if (subCount == this.subroutines.length) {
						System.arraycopy(this.subroutines, 0, (this.subroutines = new SubRoutineStatement[subCount*2]), 0, subCount); // grow
					}
					this.subroutines[subCount++] = sub;
					if (sub.isSubRoutineEscaping()) {
						saveValueNeeded = false;
						addBit(ASTNode.IsAnySubRoutineEscaping);
						break;
					}
				}
				traversedContext.recordReturnFrom(flowInfo.unconditionalInits());

				if (traversedContext instanceof InsideSubRoutineFlowContext) {
					ASTNode node = traversedContext.associatedNode;
					if (node instanceof SynchronizedStatement) {
						addBit(ASTNode.IsSynchronized);
					} else if (node instanceof TryStatement) {
						TryStatement tryStatement = (TryStatement) node;
						flowInfo.addInitializationsFrom(tryStatement.subRoutineInits); // collect inits
						if (hasValueToSave) {
							if (this.saveValueVariable == null){ // closest subroutine secret variable is used
								prepareSaveValueLocation(tryStatement);
							}
							saveValueNeeded = true;
							this.initStateIndex =
								currentScope.methodScope().recordInitializationStates(flowInfo);
						}
					}
				} else if (traversedContext instanceof InitializationFlowContext) {
						currentScope.problemReporter().cannotReturnInInitializer(this);
						return FlowInfo.DEAD_END;
				}
			} while ((traversedContext = traversedContext.parent) != null);

			// resize subroutines
			if ((this.subroutines != null) && (subCount != this.subroutines.length)) {
				System.arraycopy(this.subroutines, 0, (this.subroutines = new SubRoutineStatement[subCount]), 0, subCount);
			}

			// secret local variable for return value (note that this can only occur in a real method)
			if (saveValueNeeded) {
				if (this.saveValueVariable != null) {
					this.saveValueVariable.useFlag = LocalVariableBinding.USED;
				}
			} else {
				this.saveValueVariable = null;
				if (((getBits() & ASTNode.IsSynchronized) == 0) && this.expression != null && this.expression.resolvedType == TypeBinding.BOOLEAN) {
					this.expression.bits |= ASTNode.IsReturnedValue;
				}
			}
			return FlowInfo.DEAD_END;
		}	
		
		void checkAgainstNullAnnotation(BlockScope scope, int nullStatus) {
			if (nullStatus != FlowInfo.NON_NULL) {
				// if we can't prove non-null check against declared null-ness of the enclosing method:
				long tagBits;
				org.eclipse.jdt.internal.compiler.lookup.MethodBinding methodBinding;
				try {
					methodBinding = scope.methodScope().referenceMethod().binding;
					tagBits = methodBinding.tagBits;
				} catch (NullPointerException npe) {
					return;
				}
				if ((tagBits & TagBits.AnnotationNonNull) != 0) {
					char[][] annotationName = scope.environment().getNonNullAnnotationName();
					scope.problemReporter().nullityMismatch(getExpression(), methodBinding.returnType, nullStatus, annotationName);
				}
			}
		}
	}

	// ======================= Method level annotations ============================

	@SuppressWarnings("bindingconventions")
	@Instantiation(InstantiationPolicy.ALWAYS)
	protected class StandardAnnotation playedBy Annotation {

		@SuppressWarnings("decapsulation")
		detectStandardAnnotation <- replace detectStandardAnnotation;

		callin long detectStandardAnnotation(Scope scope, ReferenceBinding annotationType, MemberValuePair valueAttribute) 
		{
			long tagBits = base.detectStandardAnnotation(scope, annotationType, valueAttribute);
			switch (annotationType.id) {
			case TypeIds.T_ConfiguredAnnotationNullable :
				tagBits |= TagBits.AnnotationNullable;
				break;
			case TypeIds.T_ConfiguredAnnotationNonNull :
				tagBits |= TagBits.AnnotationNonNull;
				break;
			case TypeIds.T_ConfiguredAnnotationNullableByDefault :
				tagBits |= TagBits.AnnotationNullableByDefault;
				break;
			case TypeIds.T_ConfiguredAnnotationNonNullByDefault :
				tagBits |= TagBits.AnnotationNonNullByDefault;
				break;
			}
			return tagBits;
		}
	}
	
	protected class AbstractMethodDeclaration extends NodeWithBits playedBy AbstractMethodDeclaration {

		BlockScope getScope()      				-> get MethodScope scope;
		Argument[] getArguments()  				-> get Argument[] arguments;
		Annotation[] getAnnotations()			-> get Annotation[] annotations;
		void setAnnotations(Annotation[] annot)	-> set Annotation[] annotations;
		MethodBinding getBinding() 				-> get MethodBinding binding;
		
		public void createArgumentBindingsWithAnnotations() {
			MethodBinding binding = getBinding();
			if (binding == null)
				return;
			BlockScope scope = getScope();
			Argument[] arguments = this.getArguments();
			if (arguments != null && binding != null) {
				for (int i = 0, length = arguments.length; i < length; i++) {
					Argument argument = arguments[i];
					// the following three lines are copied from Argument.bind()
					// luckily both are protected against double-execution.
					if (argument.binding == null)
						argument.binding = new LocalVariableBinding(argument, null, argument.modifiers, true);
					ASTNode.resolveAnnotations(scope, argument.annotations, argument.binding);
					// transfer nullness info from the argument to the method:
					if ((argument.binding.tagBits & (TagBits.AnnotationNonNull|TagBits.AnnotationNullable)) != 0) {
						if (binding.parameterNonNullness == null)
							binding.parameterNonNullness = new Boolean[arguments.length];
						binding.parameterNonNullness[i] = Boolean.valueOf((argument.binding.tagBits & TagBits.AnnotationNonNull) != 0);
					}
				}
			}
		}

		void analyseArgumentNullity(FlowInfo info) {
			MethodBinding binding = getBinding();
			Argument[] arguments = this.getArguments();
			if (arguments != null && binding.parameterNonNullness != null) {
				for (int i = 0, count = arguments.length; i < count; i++) {
					// leverage null-info from parameter annotations:
					Boolean nonNullNess = binding.parameterNonNullness[i];
					if (nonNullNess != null) {
						if (nonNullNess)
							info.markAsDefinitelyNonNull(arguments[i].binding);
						else
							info.markPotentiallyNullBit(arguments[i].binding);
					}
				}
			}
		}
		/** 
		 * Materialize a null annotation that has been added from the current default,
		 * in order to ensure that this annotation will be generated into the .class file, too.
		 */
		public void addNullnessAnnotation(long defaultNullness, ReferenceBinding annotationBinding) {
			Annotation[] annotations = getAnnotations();
			setAnnotations(addAnnotation(this, annotations, annotationBinding));
		}
		/** 
		 * Materialize a null parameter annotation that has been added from the current default,
		 * in order to ensure that this annotation will be generated into the .class file, too.
		 */
		public void addParameterNullnessAnnotation(int i, long defaultNullness, ReferenceBinding annotationBinding) {
			Argument argument = getArguments()[i];
			Annotation[] annotations = argument.annotations;
			argument.annotations = addAnnotation(argument.type, annotations, annotationBinding);
		}

		Annotation[] addAnnotation(ASTNode location, Annotation[] annotations, ReferenceBinding annotationBinding) {
			int sourceStart = location.sourceStart;
			long pos = ((long)sourceStart<<32) + location.sourceEnd;
			long[] poss = new long[annotationBinding.compoundName.length];
			Arrays.fill(poss, pos);
			MarkerAnnotation annotation = new MarkerAnnotation(new QualifiedTypeReference(annotationBinding.compoundName, poss), sourceStart);
			annotation.declarationSourceEnd = location.sourceEnd;
			annotation.resolvedType = annotationBinding;
			annotation.bits = Constants.IsSynthetic; // later use ASTNode.IsSynthetic; // prevent from conversion to DOM AST
			if (annotations == null) {
				annotations = new Annotation[] {annotation};
			} else {
				int len = annotations.length;
				System.arraycopy(annotations, 0, annotations=new Annotation[len+1], 1, len);
				annotations[0] = annotation;
			}
			return annotations;
		}
	}
	protected class MethodDeclaration extends AbstractMethodDeclaration playedBy MethodDeclaration {

		TypeReference getReturnType() -> get TypeReference returnType; 
		/** Feed null status from parameter annotation into the analysis of the method's body. */
		void analyseArgumentNullity(FlowInfo info)
		<- before void analyseCode(ClassScope classScope, FlowContext initializationContext, FlowInfo info)
			with { info <- info }

	}
	protected class ConstructorDeclaration extends AbstractMethodDeclaration playedBy ConstructorDeclaration {
		/** Feed null status from parameter annotation into the analysis of the method's body. */
		void analyseArgumentNullity(FlowInfo info)
		<- before void analyseCode(ClassScope classScope, InitializationFlowContext initializationContext, FlowInfo info, int reachMode)
			with { info <- info }
	}

	/** Add a field to store parameter nullness information. */
	protected class MethodBinding playedBy MethodBinding {

		/** Store nullness information from annotation (incl. inherited contracts). */
		public Boolean[] parameterNonNullness;  // TRUE means @NonNull declared, FALSE means @Nullable declared, null means nothing declared

		TypeBinding getReturnType() 			-> get TypeBinding returnType;
		ReferenceBinding getDeclaringClass() 	-> get ReferenceBinding declaringClass;
		SourceTypeBinding getDeclaringSourceType()	-> get ReferenceBinding declaringClass
			with { result 						<- (SourceTypeBinding)declaringClass }
		TypeBinding[] getParameters() 			-> get TypeBinding[] parameters;
		long getTagBits() 					 	-> get long tagBits;
		public void addTagBit(long bit) 	 	-> set long tagBits 
			with { bit | getTagBits() 			-> tagBits }

		boolean isStatic() 						-> boolean isStatic();
		boolean isValidBinding() 				-> boolean isValidBinding();
		AbstractMethodDeclaration sourceMethod()-> AbstractMethodDeclaration sourceMethod();
		char[] readableName() 					-> char[] readableName();
		char[] shortReadableName() 				-> char[] shortReadableName();

		/** After method verifier has finished, fill in missing nullness values from the applicable default. */
		protected void fillInDefaultNullness(long defaultNullness, TypeBinding annotationBinding) {
			TypeBinding[] parameters = getParameters();
			if (this.parameterNonNullness == null)
				this.parameterNonNullness = new Boolean[parameters.length];
			Boolean value = Boolean.valueOf(defaultNullness == TagBits.AnnotationNonNull);
			AbstractMethodDeclaration sourceMethod = sourceMethod();
			for (int i = 0; i < this.parameterNonNullness.length; i++) {
				if (parameters[i].isBaseType())
					continue;
				boolean added = false;
				if (this.parameterNonNullness[i] == null) {
					added = true;
					this.parameterNonNullness[i] = value;
					if (sourceMethod != null)
						sourceMethod.addParameterNullnessAnnotation(i, defaultNullness, (ReferenceBinding)annotationBinding);
				}
				if (added)
					addTagBit(TagBits.HasParameterAnnotations);
			}
			TypeBinding returnType = getReturnType();
			if (   returnType != null
				&& !returnType.isBaseType()
				&& (getTagBits() & (TagBits.AnnotationNonNull|TagBits.AnnotationNullable)) == 0)
			{
				addTagBit(defaultNullness);
				if (sourceMethod != null)
					sourceMethod.addNullnessAnnotation(defaultNullness, (ReferenceBinding)annotationBinding);
			}
		}
	}

	/** Transfer inherited null contracts and check compatibility. */
	@SuppressWarnings("decapsulation")
	protected class MethodVerifier15 playedBy MethodVerifier15 {

		LookupEnvironment getEnvironment() 	-> get LookupEnvironment environment;
		SourceTypeBinding getType() 		-> get SourceTypeBinding type;
		AbstractMethodDeclaration[] getMethodDeclarations() -> get SourceTypeBinding type
				with { 	result 				<- type.scope.referenceContext.methods }
		MethodBinding[] getMethodBindings() -> get SourceTypeBinding type
				with { 	result 				<- type.methods() }

		
		void checkNullContractInheritance(MethodBinding currentMethod, MethodBinding[] methods, int length)
		<- after
		void checkAgainstInheritedMethods(MethodBinding currentMethod, MethodBinding[] methods, int length, MethodBinding[] allInheritedMethods);

		void checkNullContractInheritance(MethodBinding currentMethod, MethodBinding[] methods, int length) {
			// TODO: change traversal: process all methods at once!
			for (int i = length; --i >= 0;)
				if (!currentMethod.isStatic() && !methods[i].isStatic())
					checkNullContractInheritance(currentMethod, methods[i]);
		}
		
		void checkNullContractInheritance(MethodBinding currentMethod, MethodBinding inheritedMethod) {
			long inheritedBits = inheritedMethod.getTagBits();
			long currentBits = currentMethod.getTagBits();
			LookupEnvironment environment = this.getEnvironment();

			// return type:
			if ((inheritedBits & TagBits.AnnotationNonNull) != 0) {
				long currentNullBits = currentBits & (TagBits.AnnotationNonNull|TagBits.AnnotationNullable);
				if (currentNullBits != TagBits.AnnotationNonNull) {				
					AbstractMethodDeclaration methodDecl = currentMethod.sourceMethod();
					getType().problemReporter().illegalReturnRedefinition(methodDecl, inheritedMethod,
																environment.getNonNullAnnotationName());
				}
			}

			// parameters:
			Argument[] currentArguments = currentMethod.sourceMethod().getArguments();
			if (inheritedMethod.parameterNonNullness != null) {
				// inherited method has null-annotations, check compatibility:

				for (int i = 0; i < inheritedMethod.parameterNonNullness.length; i++) {
					
					Boolean inheritedNonNullNess = inheritedMethod.parameterNonNullness[i];
					Boolean currentNonNullNess = (currentMethod.parameterNonNullness == null)
												? null : currentMethod.parameterNonNullness[i];
					if (inheritedNonNullNess != null) {				// super has a null annotation
						if (currentNonNullNess == null) {			// current parameter lacks null annotation
							boolean needNonNull = false;
							char[][] annotationName;
							if (inheritedNonNullNess == Boolean.TRUE) {
								needNonNull = true;
								annotationName = environment.getNonNullAnnotationName();
							} else {
								annotationName = environment.getNullableAnnotationName();
							}
								
							getType().problemReporter().parameterLackingNonNullAnnotation(
									currentArguments[i],
									inheritedMethod.getDeclaringClass(),
									needNonNull,
									annotationName);
							continue;
						}						
					}
					if (inheritedNonNullNess != Boolean.TRUE) {		// super parameter is not restricted to @NonNull
						if (currentNonNullNess == Boolean.TRUE) { 	// current parameter is restricted to @NonNull
							getType().problemReporter().illegalRedefinitionToNonNullParameter(
																currentArguments[i],
																inheritedMethod.getDeclaringClass(),
																inheritedNonNullNess == null
																? null
																: environment.getNullableAnnotationName());
						} 
					}
				}
			} else if (currentMethod.parameterNonNullness != null) {
				// super method has no annotations but current has
				for (int i = 0; i < currentMethod.parameterNonNullness.length; i++) {
					if (currentMethod.parameterNonNullness[i] == Boolean.TRUE) { // tightening from unconstrained to @NonNull
						getType().problemReporter().illegalRedefinitionToNonNullParameter(
																		currentArguments[i],
																		inheritedMethod.getDeclaringClass(),
																		null);
					}
				}
			}
		}
	}
	TypeBinding findDefaultNullness(SourceTypeBinding type, LookupEnvironment environment) {
		// find the applicable default inside->out:
		
		// type
		SourceTypeBinding currentType = type;
		TypeBinding annotationBinding = null;
		while (currentType != null) {
			annotationBinding = currentType.getNullnessDefaultAnnotation();
			if (annotationBinding != null)
				return annotationBinding;
			currentType = currentType.enclosingType();
		}
		
		// package
		annotationBinding = type.getPackage().getNullnessDefaultAnnotation();
		if (annotationBinding != null)
			return annotationBinding;
		
		// global
		long defaultNullness = environment.getGlobalOptions().defaultNonNullness;
		if (defaultNullness != 0) {
			annotationBinding = environment.getNullAnnotationBinding(defaultNullness);
			if (annotationBinding != null)
				return annotationBinding;
			
			// on this branch default was not defined using an annotation, thus annotation type can still be missing
			if (defaultNullness == TagBits.AnnotationNonNull)
				type.problemReporter().missingNullAnnotationType(environment.getNonNullAnnotationName());
			else if (defaultNullness == TagBits.AnnotationNullable)
				type.problemReporter().missingNullAnnotationType(environment.getNullableAnnotationName());
			else
				type.problemReporter().abortDueToInternalError("Illegal default nullness value: "+defaultNullness); //$NON-NLS-1$
			// reset default to avoid duplicate errors:
			environment.getGlobalOptions().defaultNonNullness = 0;
		}
		return null;
	}

	@SuppressWarnings("bindingconventions")
	protected class TaggableTypeBinding playedBy TypeBinding {
		long getTagBits() 					 	-> get long tagBits;
		public void addTagBit(long bit) 	 	-> set long tagBits 
			with { bit | getTagBits() 			-> tagBits }
	}
	
	protected class SourceTypeBinding extends TaggableTypeBinding playedBy SourceTypeBinding {

		PackageBinding getPackage() 		-> PackageBinding getPackage();

		ProblemReporter problemReporter() 	-> get ClassScope scope
					with {  result 			<- scope.problemReporter() } 

		SourceTypeBinding enclosingType() 	-> ReferenceBinding enclosingType()
					with { result 			<- (SourceTypeBinding)result }
		
		long computeTypeAnnotationTagBits()	-> long getAnnotationTagBits();
		
		private TypeBinding nullnessDefaultAnnotation;
		private int nullnessDefaultInitialized = 0; // 0: nothing; 1: type; 2: package

		/** initialize a normal type */
		void evaluateNullAnnotations(long tagBits)	<- after long getAnnotationTagBits()
			with { tagBits <- result }

		/** initialize a package-info.java */
		readAndEvaluateAnnotations 			<- after initializeDeprecatedAnnotationTagBits
			base when (CharOperation.equals(base.sourceName, TypeConstants.PACKAGE_INFO_NAME));

		void callBindArguments(MethodBinding method) <- after MethodBinding resolveTypesFor(MethodBinding method);

		private void callBindArguments(MethodBinding method) {
			switch (this.nullnessDefaultInitialized) {
			case 0:
				computeTypeAnnotationTagBits();
				//$FALL-THROUGH$
			case 1:
				getPackage().computeAnnotations();
				this.nullnessDefaultInitialized = 2;
			}
			AbstractMethodDeclaration methodDecl = method.sourceMethod();
			if (methodDecl != null) {
				if (method.getParameters() != Binding.NO_PARAMETERS)
					methodDecl.createArgumentBindingsWithAnnotations();
				TypeBinding annotationBinding = findDefaultNullness(method.getDeclaringSourceType(), methodDecl.getScope().environment());
				if (annotationBinding != null) {
					long defaultNullness = Constants.getNullnessTagbit(annotationBinding);
					method.fillInDefaultNullness(defaultNullness, annotationBinding);
				}
			}
		}
		
		void readAndEvaluateAnnotations() {
			computeTypeAnnotationTagBits();
		}
		@SuppressWarnings("inferredcallout")
		void evaluateNullAnnotations(long tagBits) {
			if (this.nullnessDefaultInitialized > 0)
				return;
			this.nullnessDefaultInitialized = 1;
			// transfer nullness info from tagBits to this.nullnessDefaultAnnotation 
			long tagBit = Constants.applyDefaultNullnessTagbit(tagBits);
			if (tagBit == 0)
				return;
			TypeBinding nullnessDefaultAnnotation = getPackage().getEnvironment().getUnresolvedNullAnnotationBinding(tagBit);
			if (nullnessDefaultAnnotation != null) {
				if (CharOperation.equals(this.sourceName, TypeConstants.PACKAGE_INFO_NAME)) {
					getPackage().nullnessDefaultAnnotation = nullnessDefaultAnnotation;
				} else {
					this.nullnessDefaultAnnotation = nullnessDefaultAnnotation;
				}
			}
		}
		public TypeBinding getNullnessDefaultAnnotation() {
			if (this.nullnessDefaultAnnotation instanceof UnresolvedReferenceBinding)
				return this.nullnessDefaultAnnotation =
						org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding.resolveType(this.nullnessDefaultAnnotation, 
									getPackage().getEnvironment(), false);
			return this.nullnessDefaultAnnotation;
		}
	}
	
	/** Retrieve null annotations from binary methods. */
	protected class BinaryTypeBinding extends TaggableTypeBinding playedBy BinaryTypeBinding {

		@SuppressWarnings("decapsulation")
		LookupEnvironment getEnvironment() 	-> get LookupEnvironment environment;

		PackageBinding getPackage() 		-> PackageBinding getPackage();
				
		char[] sourceName() 				-> char[] sourceName();
		
		void scanMethodForNullAnnotation(IBinaryMethod method, MethodBinding methodBinding) 
		<- after MethodBinding createMethod(IBinaryMethod method, long sourceLevel, char[][][] missingTypeNames)
			with { method <- method, methodBinding <- result }
		
		void scanMethodForNullAnnotation(IBinaryMethod method, MethodBinding methodBinding) {
			LookupEnvironment environment = this.getEnvironment();
			char[][] nullableAnnotationName = environment.getNullableAnnotationName();
			char[][] nonNullAnnotationName = environment.getNonNullAnnotationName();
			if (nullableAnnotationName == null || nonNullAnnotationName == null)
				return; // not configured to use null annotations

			// return:
			IBinaryAnnotation[] annotations = method.getAnnotations();
			if (annotations != null) {
				for (int i = 0; i < annotations.length; i++) {
					char[] annotationTypeName = annotations[i].getTypeName();
					if (annotationTypeName[0] != 'L')
						continue;
					char[][] typeName = CharOperation.splitOn('/', annotationTypeName, 1, annotationTypeName.length-1); // cut of leading 'L' and trailing ';'
					if (CharOperation.equals(typeName, nonNullAnnotationName)) {
						methodBinding.addTagBit(TagBits.AnnotationNonNull);
						break;
					}
					if (CharOperation.equals(typeName, nullableAnnotationName)) {
						methodBinding.addTagBit(TagBits.AnnotationNullable);
						break;
					}
				}
			}

			// parameters:
			TypeBinding[] parameters = methodBinding.getParameters();
			for (int j = 0; j < parameters.length; j++) {
				IBinaryAnnotation[] paramAnnotations = method.getParameterAnnotations(j); 
				if (paramAnnotations != null) {
					for (int i = 0; i < paramAnnotations.length; i++) {
						char[] annotationTypeName = paramAnnotations[i].getTypeName();
						if (annotationTypeName[0] != 'L')
							continue;
						char[][] typeName = CharOperation.splitOn('/', annotationTypeName, 1, annotationTypeName.length-1); // cut of leading 'L' and trailing ';'
						if (CharOperation.equals(typeName, nonNullAnnotationName)) {
							if (methodBinding.parameterNonNullness == null)
								methodBinding.parameterNonNullness = new Boolean[parameters.length];
							methodBinding.parameterNonNullness[j] = Boolean.TRUE;
							break;
						} else if (CharOperation.equals(typeName, nullableAnnotationName)) {
							if (methodBinding.parameterNonNullness == null)
								methodBinding.parameterNonNullness = new Boolean[parameters.length];
							methodBinding.parameterNonNullness[j] = Boolean.FALSE;
							break;
						}
					}
				}
			}
		}

		scanTypeForNullAnnotation <- after cachePartsFrom;

		void scanTypeForNullAnnotation(IBinaryType binaryType) {
			LookupEnvironment environment = this.getEnvironment();
			char[][] nullableByDefaultAnnotationName = environment.getNullableByDefaultAnnotationName();
			char[][] nonNullByDefaultAnnotationName = environment.getNonNullByDefaultAnnotationName();
			if (nullableByDefaultAnnotationName == null || nonNullByDefaultAnnotationName == null)
				return; // not configured to use null annotations

			IBinaryAnnotation[] annotations = binaryType.getAnnotations();
			if (annotations != null) {
				long annotationBit = 0L;
				TypeBinding defaultNullness = null;
				for (int i = 0; i < annotations.length; i++) {
					char[] annotationTypeName = annotations[i].getTypeName();
					if (annotationTypeName[0] != 'L')
						continue;
					char[][] typeName = CharOperation.splitOn('/', annotationTypeName, 1, annotationTypeName.length-1); // cut of leading 'L' and trailing ';'
					if (CharOperation.equals(typeName, nonNullByDefaultAnnotationName)) {
						annotationBit = TagBits.AnnotationNonNullByDefault;
						defaultNullness = getEnvironment().getUnresolvedNullAnnotationBinding(TagBits.AnnotationNonNull);
						break;
					}
					if (CharOperation.equals(typeName, nullableByDefaultAnnotationName)) {
						annotationBit = TagBits.AnnotationNullableByDefault;
						defaultNullness = getEnvironment().getUnresolvedNullAnnotationBinding(TagBits.AnnotationNullable);
						break;
					}
				}
				if (annotationBit != 0L) {
					addTagBit(annotationBit);
					if (CharOperation.equals(this.sourceName(), TypeConstants.PACKAGE_INFO_NAME))
						this.getPackage().nullnessDefaultAnnotation = defaultNullness;
				}
			}
		}
		
		
	}

	// ========================== Configuration of null annotation types =========================

	/** Initiate setup of configured null annotation types. */
	protected class LookupEnvironment playedBy LookupEnvironment {

		@SuppressWarnings("decapsulation")
		TypeBinding getTypeFromCompoundName(char[][] compoundName, boolean isParameterized, boolean wasMissingType) 
		-> ReferenceBinding getTypeFromCompoundName(char[][] compoundName, boolean isParameterized, boolean wasMissingType);
		ReferenceBinding getType(char[][] compoundName) 	-> ReferenceBinding getType(char[][] compoundName);
		PackageBinding createPackage(char[][] compoundName) -> PackageBinding createPackage(char[][] compoundName);

		boolean packageInitialized = false;
		
		/** The first time a package is requested initialize the null annotation type package. */
		void initNullAnnotationPackage()
					<- before PackageBinding getTopLevelPackage(char[] name),
							  PackageBinding createPackage(char[][] compoundName)
			when (!this.packageInitialized);

		private void initNullAnnotationPackage() {
			this.packageInitialized = true;
			char[][] compoundName = getNullableAnnotationName();
			if (compoundName != null)
				setupNullAnnotationPackage(compoundName, TypeIds.T_ConfiguredAnnotationNullable);
			compoundName = getNonNullAnnotationName();
			if (compoundName != null)
				setupNullAnnotationPackage(compoundName, TypeIds.T_ConfiguredAnnotationNonNull);
			compoundName = getNullableByDefaultAnnotationName();
			if (compoundName != null)
				setupNullAnnotationPackage(compoundName, TypeIds.T_ConfiguredAnnotationNullableByDefault);
			compoundName = getNonNullByDefaultAnnotationName();
			if (compoundName != null)
				setupNullAnnotationPackage(compoundName, TypeIds.T_ConfiguredAnnotationNonNullByDefault);
		}
		
		/** 
		 * Create or retrieve the package holding the specified type.
		 * If emulation is enabled fill it with an emulated type of the given name.
		 * Prepare the package with all information to do the second stage of initialization/checking.
		 */
		void setupNullAnnotationPackage(char[][] typeName, int typeId) {
			char[][] packageName = CharOperation.subarray(typeName, 0, typeName.length-1);
			PackageBinding packageBinding = createPackage(packageName);
			char[] simpleTypeName = typeName[typeName.length-1];
			if (typeId == TypeIds.T_ConfiguredAnnotationNullable)
				packageBinding.nullableName = simpleTypeName;
			else if (typeId == TypeIds.T_ConfiguredAnnotationNonNull)
				packageBinding.nonNullName = simpleTypeName;
			else if (typeId == TypeIds.T_ConfiguredAnnotationNullableByDefault)
				packageBinding.nullableByDefaultName = simpleTypeName;
			else if (typeId == TypeIds.T_ConfiguredAnnotationNonNullByDefault)
				packageBinding.nonNullByDefaultName = simpleTypeName;
		}
		
		reset <- after reset;
		void reset() {
			this.packageInitialized = false;
		}

		CompilerOptions getGlobalOptions() -> get CompilerOptions globalOptions;

		public char[][] getNullableAnnotationName() {
			return getGlobalOptions().nullableAnnotationName;
		}

		public char[][] getNonNullAnnotationName() {
			return getGlobalOptions().nonNullAnnotationName;
		}

		public char[][] getNullableByDefaultAnnotationName() {
			return getGlobalOptions().nullableByDefaultAnnotationName;
		}

		public char[][] getNonNullByDefaultAnnotationName() {
			return getGlobalOptions().nonNullByDefaultAnnotationName;
		}
		public TypeBinding getNullAnnotationBinding(long annotationTagBit) {
			if (annotationTagBit == TagBits.AnnotationNonNull)
				return getType(getNonNullAnnotationName());
			if (annotationTagBit == TagBits.AnnotationNullable)
				return getType(getNullableAnnotationName());
			return null;
		}
		public TypeBinding getUnresolvedNullAnnotationBinding(long annotationTagBit) {
			if (annotationTagBit == TagBits.AnnotationNonNull)
				return getTypeFromCompoundName(getNonNullAnnotationName(), false, false);
			if (annotationTagBit == TagBits.AnnotationNullable)
				return getTypeFromCompoundName(getNullableAnnotationName(), false, false);
			return null;
		}
	}

	/** The package holding the configured null annotation types detects and marks these annotation types. */
	protected class PackageBinding playedBy PackageBinding {

		LookupEnvironment getEnvironment() 				-> get LookupEnvironment environment;
		void computeAnnotations()						-> boolean isViewedAsDeprecated();

		protected char[] nullableName = null;
		protected char[] nonNullName = null;
		protected char[] nullableByDefaultName = null;
		protected char[] nonNullByDefaultName = null;

		protected TypeBinding nullnessDefaultAnnotation;

		void setupNullAnnotationType(ReferenceBinding type) <- after void addType(ReferenceBinding type)
			when (this.nullableName != null || this.nonNullName != null || this.nullableByDefaultName != null || this.nonNullByDefaultName != null);

		void setupNullAnnotationType(ReferenceBinding type) {
			int id = 0;
			if (CharOperation.equals(this.nullableName, type.sourceName))
				id = TypeIds.T_ConfiguredAnnotationNullable;
			else if (CharOperation.equals(this.nonNullName, type.sourceName))
				id = TypeIds.T_ConfiguredAnnotationNonNull;
			else if (CharOperation.equals(this.nullableByDefaultName, type.sourceName))
				id = TypeIds.T_ConfiguredAnnotationNullableByDefault;
			else if (CharOperation.equals(this.nonNullByDefaultName, type.sourceName))
				id = TypeIds.T_ConfiguredAnnotationNonNullByDefault;
			else 
				return;
			
			type.id = id;	// ensure annotations of this type are detected as standard annotations.
		}

		public TypeBinding getNullnessDefaultAnnotation() {
			if (this.nullnessDefaultAnnotation instanceof UnresolvedReferenceBinding)
				return this.nullnessDefaultAnnotation =
						org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding.resolveType(this.nullnessDefaultAnnotation, getEnvironment(), false);
			return this.nullnessDefaultAnnotation;
		}
	}

	/** Intermediate role to provide access to environment and problemReporter as roles, too. */
	protected class BlockScope playedBy BlockScope {
		ProblemReporter problemReporter() 	-> ProblemReporter problemReporter();
		MethodScope methodScope() 			-> MethodScope methodScope();
		LookupEnvironment environment() 	-> LookupEnvironment environment();
	}

	// ---- Handling sub-classes of FlowContext that perform deferred null checking: ----
	
	public <B base ConditionalFlowContext> void recordNullReference(B as ConditionalFlowContext flowContext,
							Expression expression, TypeBinding expectedType, int checkType) 
	{
		flowContext.recordExpectedType(expectedType);
		flowContext.recordNullReference(expression.localVariableBinding(), expression, checkType);
	}
	protected abstract class ConditionalFlowContext {
		// new constant for checkTypes:
		protected final static int ASSIGN_TO_NONNULL = 0x0080;
		
		// declare abstract as to avoid binding to FlowContext (don't want roles for regular flowContexts):
		protected abstract int getNullCount();
		protected abstract long getTagBits();
		protected abstract Expression[] getNullReferences();
		protected abstract LocalVariableBinding[] getNullLocals();
		protected abstract int[] getNullCheckTypes();
		protected abstract FlowContext getParent();
		
		protected abstract void recordNullReference(LocalVariableBinding local, Expression expression, int status);
		
		protected abstract FlowInfo getFlowInfo(FlowInfo callerFlowInfo);
		
		// new array to store the expected type from the potential error location:
		public TypeBinding[] expectedTypes;
		
		// and the method to add to expectedTypes:
		protected void recordExpectedType(TypeBinding expectedType) {
			int nullCount = getNullCount();
			if (nullCount == 0) {
				this.expectedTypes = new TypeBinding[5];
			} else if (this.expectedTypes == null) {
				int size = 5;
				while (size <= nullCount) size *= 2;
				this.expectedTypes = new TypeBinding[size];
			}
			else if (nullCount == this.expectedTypes.length) {
				System.arraycopy(this.expectedTypes, 0,
					this.expectedTypes = new TypeBinding[nullCount * 2], 0, nullCount);
			}
			this.expectedTypes[nullCount] = expectedType;
		}

		/** Main logic for deferred checking. To be bound by after callin. */
		void complainOnNullTypeError(BlockScope scope, FlowInfo callerFlowInfo) {
			FlowInfo flowInfo = getFlowInfo(callerFlowInfo);
			if ((getTagBits() & FlowContext.DEFER_NULL_DIAGNOSTIC) != 0) {
				for (int i = 0; i < getNullCount(); i++) {
					int nullCheckType = getNullCheckTypes()[i];
					if (nullCheckType == ASSIGN_TO_NONNULL) {
						FlowContext parent = getParent();
						if (parent instanceof org.eclipse.jdt.internal.compiler.flow.LoopingFlowContext) {
							CompilerAdaptation.this.recordNullReference((org.eclipse.jdt.internal.compiler.flow.LoopingFlowContext)parent, 
									getNullReferences()[i], this.expectedTypes[i], nullCheckType);
							continue;
						} else if (parent instanceof org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext) {
							CompilerAdaptation.this.recordNullReference((org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext)parent, 
									getNullReferences()[i], this.expectedTypes[i], nullCheckType);
							continue;
						}
					} else {
						getParent().recordUsingNullReference(scope, getNullLocals()[i],
								getNullReferences()[i],	nullCheckType, flowInfo);
					}
				}
			} else {
				// check inconsistent null checks on outermost looping context
				for (int i = 0; i < getNullCount(); i++) {
					Expression expression = getNullReferences()[i];
					// final local variable
					LocalVariableBinding local = getNullLocals()[i];
					if ((getNullCheckTypes()[i] & ASSIGN_TO_NONNULL) != 0) {
						char[][] annotationName = scope.environment().getNonNullAnnotationName();
						int nullStatus = flowInfo.nullStatus(local);
						if (nullStatus != FlowInfo.NON_NULL)
							scope.problemReporter().nullityMismatch(expression, this.expectedTypes[i], nullStatus, annotationName);
						break;
					}
				}
			}
		}
	}
	/** Straight-forward binding of implementation role to concrete base class. */
	protected class FinallyFlowContext extends ConditionalFlowContext playedBy FinallyFlowContext {
		getTagBits -> get tagBits;
		getParent  -> get parent;
		@SuppressWarnings("decapsulation") getNullCount      -> get nullCount;
		@SuppressWarnings("decapsulation") getNullReferences -> get nullReferences;
		@SuppressWarnings("decapsulation") getNullLocals     -> get nullLocals;
		@SuppressWarnings("decapsulation") getNullCheckTypes -> get nullCheckTypes;
		
		// here this team enters special data:
		@SuppressWarnings("decapsulation") recordNullReference -> recordNullReference;

		protected FlowInfo getFlowInfo(FlowInfo callerFlowInfo) { return callerFlowInfo; } // nothing special :)
		
		// here the recorded data is evaluated:
		void complainOnNullTypeError(BlockScope scope, FlowInfo callerFlowInfo)
		<- after void complainOnDeferredChecks(FlowInfo flowInfo, BlockScope scope)
			with { scope <- scope, callerFlowInfo <- flowInfo }
	}
	/** Straight-forward binding of implementation role to concrete base class. */
	protected class LoopingFlowContext extends ConditionalFlowContext playedBy LoopingFlowContext {

		getTagBits -> get tagBits;
		getParent  -> get parent;
		@SuppressWarnings("decapsulation") getNullCount      -> get nullCount;
		@SuppressWarnings("decapsulation") getNullReferences -> get nullReferences;
		@SuppressWarnings("decapsulation") getNullLocals     -> get nullLocals;
		@SuppressWarnings("decapsulation") getNullCheckTypes -> get nullCheckTypes;
		
		// here this team enters special data:
		@SuppressWarnings("decapsulation") recordNullReference -> recordNullReference;
		
		@SuppressWarnings({ "inferredcallout", "decapsulation" })
		protected FlowInfo getFlowInfo(FlowInfo callerFlowInfo) {
			// copied from LoopingFlowContext.complainOnDeferredNullChecks:
			return this.upstreamNullFlowInfo.
					addPotentialNullInfoFrom(callerFlowInfo.unconditionalInitsWithoutSideEffect());
		}
		
		// here the recorded data is evaluated:
		complainOnNullTypeError <- after complainOnDeferredNullChecks;
	}

	// ======================= Problem reporting ==================================
	
	/** 
	 * Adapt the base class for handling new problem ids and irritants.
	 * Add new problem reporting methods.
	 */
	protected class ProblemReporter playedBy ProblemReporter {

		@SuppressWarnings("decapsulation")
		void handle(int problemId, String[] problemArguments, String[] messageArguments, int severity,
				int problemStartPosition, int problemEndPosition)
		->
		void handle(int problemId, String[] problemArguments, String[] messageArguments, int severity,
				int problemStartPosition, int problemEndPosition);
		@SuppressWarnings("decapsulation")
		void handle(int problemId, String[] problemArguments, String[] messageArguments, int problemStartPosition, int problemEndPosition)
		->
		void handle(int problemId, String[] problemArguments, String[] messageArguments, int problemStartPosition, int problemEndPosition);
		
		void abortDueToInternalError(String errorMessage) -> void abortDueToInternalError(String errorMessage);
		
		void cannotReturnInInitializer(ASTNode location) -> void cannotReturnInInitializer(ASTNode location);
		
		getProblemCategory <- replace getProblemCategory;
		
		getNullIrritant <- replace getIrritant;

		
		@SuppressWarnings("basecall")
		static callin int getProblemCategory(int severity, int problemID) {
			categorizeOnIrritant: {
				// fatal problems even if optional are all falling into same category (not irritant based)
				if ((severity & ProblemSeverities.Fatal) != 0)
					break categorizeOnIrritant;
				int irritant = getIrritant(problemID);
				switch (irritant) {
				case CompilerOptions.NullContractViolation :
				case CompilerOptions.PotentialNullContractViolation :
				case CompilerOptions.NullContractInsufficientInfo :
					return CategorizedProblem.CAT_POTENTIAL_PROGRAMMING_PROBLEM;
				}
				// categorize fatal problems per ID
				switch (problemID) {
					case IProblem.MissingNullAnnotationType :
						return CategorizedProblem.CAT_BUILDPATH;
				}
			}
			return base.getProblemCategory(severity, problemID);
		}
		
		@SuppressWarnings("basecall")
		static callin int getNullIrritant(int problemID) {
			int irritant = getIrritant(problemID);
			if (irritant != 0)
				return irritant;
			return base.getNullIrritant(problemID);
		}

		private static int getIrritant(int problemID) {
			switch(problemID) {
				case IProblem.RequiredNonNullButProvidedNull:
				case IProblem.IllegalReturnNullityRedefinition:
				case IProblem.IllegalRedefinitionToNonNullParameter:
				case IProblem.IllegalDefinitionToNonNullParameter:
					return CompilerOptions.NullContractViolation;
				case IProblem.RequiredNonNullButProvidedPotentialNull:
					return CompilerOptions.PotentialNullContractViolation;
				case IProblem.RequiredNonNullButProvidedUnknown:
					return CompilerOptions.NullContractInsufficientInfo;
				case IProblem.PotentialNullMessageSendReference:
					return org.eclipse.jdt.internal.compiler.impl.CompilerOptions.PotentialNullReference;
				case IProblem.RedundantNullCheckOnNonNullMessageSend:
					return org.eclipse.jdt.internal.compiler.impl.CompilerOptions.RedundantNullCheck;
			}
			return 0;
		}
	
		public void nullityMismatch(Expression expression, TypeBinding requiredType, int nullStatus, char[][] annotationName) {
			int problemId = IProblem.RequiredNonNullButProvidedUnknown;
			if ((nullStatus & FlowInfo.NULL) != 0)
				problemId = IProblem.RequiredNonNullButProvidedNull;
			if ((nullStatus & FlowInfo.POTENTIALLY_NULL) != 0)
				problemId = IProblem.RequiredNonNullButProvidedPotentialNull;
			String[] arguments = new String[] { 
					String.valueOf(CharOperation.concatWith(annotationName, '.')),
					String.valueOf(requiredType.readableName())
			};
			String[] argumentsShort = new String[] { 
					String.valueOf(annotationName[annotationName.length-1]),
					String.valueOf(requiredType.shortReadableName())
			};
			this.handle(
				problemId,
				arguments,
				argumentsShort,
				expression.sourceStart,
				expression.sourceEnd);
		}
		public void illegalRedefinitionToNonNullParameter(Argument argument, ReferenceBinding declaringClass, char[][] inheritedAnnotationName) {
			int sourceStart = argument.type.sourceStart;
			if (argument.annotations != null) {
				for (int i=0; i<argument.annotations.length; i++) {
					Annotation annotation = argument.annotations[i];
					if (   annotation.resolvedType.id == TypeIds.T_ConfiguredAnnotationNullable
						|| annotation.resolvedType.id == TypeIds.T_ConfiguredAnnotationNonNull) 
					{
						sourceStart = annotation.sourceStart;
						break;
					}
				}
			}
			if (inheritedAnnotationName == null) {
				this.handle(
					IProblem.IllegalDefinitionToNonNullParameter, 
					new String[] { new String(argument.name), new String(declaringClass.readableName()) },
					new String[] { new String(argument.name), new String(declaringClass.shortReadableName()) },
					sourceStart,
					argument.type.sourceEnd);
				
			} else {
				this.handle(
					IProblem.IllegalRedefinitionToNonNullParameter, 
					new String[] { new String(argument.name), new String(declaringClass.readableName()), CharOperation.toString(inheritedAnnotationName)},
					new String[] { new String(argument.name), new String(declaringClass.shortReadableName()), new String(inheritedAnnotationName[inheritedAnnotationName.length-1])},
					sourceStart,
					argument.type.sourceEnd);
			}
		}
		public void parameterLackingNonNullAnnotation(Argument argument, ReferenceBinding declaringClass, boolean needNonNull, char[][] inheritedAnnotationName) {
			this.handle(
				needNonNull ? IProblem.ParameterLackingNonNullAnnotation : IProblem.ParameterLackingNullableAnnotation, 
				new String[] { new String(argument.name), new String(declaringClass.readableName()), CharOperation.toString(inheritedAnnotationName)},
				new String[] { new String(argument.name), new String(declaringClass.shortReadableName()), new String(inheritedAnnotationName[inheritedAnnotationName.length-1])},
				argument.type.sourceStart,
				argument.type.sourceEnd);
		}
		public void illegalReturnRedefinition(AbstractMethodDeclaration abstractMethodDecl,
											  MethodBinding inheritedMethod, char[][] nonNullAnnotationName) 
		{
			MethodDeclaration methodDecl = (MethodDeclaration) abstractMethodDecl;
			StringBuffer methodSignature = new StringBuffer();
			methodSignature
				.append(inheritedMethod.getDeclaringClass().readableName())
				.append('.')
				.append(inheritedMethod.readableName());

			StringBuffer shortSignature = new StringBuffer();
			shortSignature
				.append(inheritedMethod.getDeclaringClass().shortReadableName())
				.append('.')
				.append(inheritedMethod.shortReadableName());
			int sourceStart = methodDecl.getReturnType().sourceStart;
			Annotation[] annotations = methodDecl.getAnnotations();
			if (annotations != null) {
				for (int i=0; i<annotations.length; i++) {
					if (annotations[i].resolvedType.id == TypeIds.T_ConfiguredAnnotationNullable) {
						sourceStart = annotations[i].sourceStart;
						break;
					}
				}
			}
			this.handle(
				IProblem.IllegalReturnNullityRedefinition, 
				new String[] { methodSignature.toString(), CharOperation.toString(nonNullAnnotationName)},
				new String[] { shortSignature.toString(), new String(nonNullAnnotationName[nonNullAnnotationName.length-1])},
				sourceStart, 
				methodDecl.getReturnType().sourceEnd);
		}
		public void messageSendPotentialNullReference(MethodBinding method, ASTNode location) {
			String[] arguments = new String[] {new String(method.readableName())};
			this.handle(
				IProblem.PotentialNullMessageSendReference,
				arguments,
				arguments,
				location.sourceStart,
				location.sourceEnd);
		}
		public void messageSendRedundantCheckOnNonNull(MethodBinding method, ASTNode location) {
			String[] arguments = new String[] {new String(method.readableName())  };
			this.handle(
				IProblem.RedundantNullCheckOnNonNullMessageSend,
				arguments,
				arguments,
				location.sourceStart,
				location.sourceEnd);
		}

		public void missingNullAnnotationType(char[][] nullAnnotationName) {
			String[] args = { new String(CharOperation.concatWith(nullAnnotationName, '.')) };
			this.handle(IProblem.MissingNullAnnotationType, args, args, 0, 0);	
		}
		// NOTE: adaptation of toString() omitted
	}
	
	/** Supply more error messages. */
	protected class ProblemFactory playedBy DefaultProblemFactory {
		
		@SuppressWarnings("decapsulation")
		int keyFromID(int id) -> int keyFromID(int id);

		void loadMessageTemplates(HashtableOfInt templates, Locale loc)
		<- after HashtableOfInt loadMessageTemplates(Locale loc)
			with { templates <- result, loc <- loc }
		
		@SuppressWarnings("rawtypes") // copied from its base class, only bundleName has been changed
		public static void loadMessageTemplates(HashtableOfInt templates, Locale loc) {
			ResourceBundle bundle = null;
			String bundleName = "org.eclipse.objectteams.internal.jdt.nullity.problem_messages"; //$NON-NLS-1$
			try {
				bundle = ResourceBundle.getBundle(bundleName, loc);
			} catch(MissingResourceException e) {
				System.out.println("Missing resource : " + bundleName.replace('.', '/') + ".properties for locale " + loc); //$NON-NLS-1$//$NON-NLS-2$
				throw e;
			}
			Enumeration keys = bundle.getKeys();
			while (keys.hasMoreElements()) {
			    String key = (String)keys.nextElement();
			    try {
			        int messageID = Integer.parseInt(key);
					templates.put(keyFromID(messageID), bundle.getString(key));
			    } catch(NumberFormatException e) {
			        // key ill-formed
				} catch (MissingResourceException e) {
					// available ID
			    }
			}
		}
	}

	// ================================== Compiler Options: ==================================

	@SuppressWarnings("rawtypes")
	protected class CompilerOptions implements org.eclipse.objectteams.internal.jdt.nullity.NullCompilerOptions playedBy CompilerOptions 
	{

		@SuppressWarnings("decapsulation")
		void updateSeverity(int irritant, Object severityString) 	-> void updateSeverity(int irritant, Object severityString);
		String getSeverityString(int irritant) 						-> String getSeverityString(int irritant);
		
		public boolean isAnnotationBasedNullAnalysisEnabled;
		
		/** Fully qualified name of annotation to use as marker for nullable types. */
		public char[][] nullableAnnotationName;
		/** Fully qualified name of annotation to use as marker for nonnull types. */
		public char[][] nonNullAnnotationName;
		/** Fully qualified name of annotation to use as marker for default nullable. */
		public char[][] nullableByDefaultAnnotationName;
		/** Fully qualified name of annotation to use as marker for default nonnull. */
		public char[][] nonNullByDefaultAnnotationName;

		public long defaultNonNullness; // 0 or TagBits#AnnotationNullable or TagBits#AnnotationNonNull

		String optionKeyFromIrritant(int irritant) <- replace String optionKeyFromIrritant(int irritant);
		@SuppressWarnings("basecall")
		static callin String optionKeyFromIrritant(int irritant) {
			switch(irritant) {
			case NullContractViolation :
				return OPTION_ReportNullContractViolation;
			case PotentialNullContractViolation :
				return OPTION_ReportPotentialNullContractViolation;
			case NullContractInsufficientInfo :
				return OPTION_ReportNullContractInsufficientInfo;
			default:
				return base.optionKeyFromIrritant(irritant);
			}
		}
		String warningTokenFromIrritant(int irritant) <- replace String warningTokenFromIrritant(int irritant);
		@SuppressWarnings("basecall")
		static callin String warningTokenFromIrritant(int irritant) {
			switch(irritant) {
			case NullContractViolation :
			case PotentialNullContractViolation :
			case NullContractInsufficientInfo :
				return "nullcontract"; //$NON-NLS-1$
			default:
				return base.warningTokenFromIrritant(irritant);
			}
		}

		warningTokenToIrritants <- replace warningTokenToIrritants;
		@SuppressWarnings("basecall")
		static callin IrritantSet warningTokenToIrritants(String string) {
			if ("nullcontract".equals(string)) //$NON-NLS-1$
				return new IrritantSet(NullContractViolation).set(PotentialNullContractViolation).set(NullContractInsufficientInfo);
			return base.warningTokenToIrritants(string);
		}

		void getMap(Map optionsMap) <- after Map getMap()
			with {optionsMap <- result}
		@SuppressWarnings("unchecked")
		private void getMap(Map optionsMap) {
			optionsMap.put(OPTION_AnnotationBasedNullAnalysis, this.isAnnotationBasedNullAnalysisEnabled ? ENABLED : DISABLED);
			if (this.isAnnotationBasedNullAnalysisEnabled) {
				optionsMap.put(OPTION_ReportNullContractViolation, getSeverityString(NullContractViolation));
				optionsMap.put(OPTION_ReportPotentialNullContractViolation, getSeverityString(PotentialNullContractViolation));
				optionsMap.put(OPTION_ReportNullContractInsufficientInfo, getSeverityString(NullContractInsufficientInfo));
				if (this.nullableAnnotationName != null) {
					char[] compoundName = CharOperation.concatWith(this.nullableAnnotationName, '.');
					optionsMap.put(OPTION_NullableAnnotationName, String.valueOf(compoundName));
				}
				if (this.nonNullAnnotationName != null) {
					char[] compoundName = CharOperation.concatWith(this.nonNullAnnotationName, '.');
					optionsMap.put(OPTION_NonNullAnnotationName, String.valueOf(compoundName));
				}
				if (this.nullableByDefaultAnnotationName != null) {
					char[] compoundName = CharOperation.concatWith(this.nullableByDefaultAnnotationName, '.');
					optionsMap.put(OPTION_NullableByDefaultAnnotationName, String.valueOf(compoundName));
				}
				if (this.nonNullByDefaultAnnotationName != null) {
					char[] compoundName = CharOperation.concatWith(this.nonNullByDefaultAnnotationName, '.');
					optionsMap.put(OPTION_NonNullByDefaultAnnotationName, String.valueOf(compoundName));
				}
				if (this.defaultNonNullness == TagBits.AnnotationNullable)
					optionsMap.put(OPTION_NullnessDefault, NULLABLE);
				else if (this.defaultNonNullness == TagBits.AnnotationNonNull)
					optionsMap.put(OPTION_NullnessDefault, NONNULL);
				else
					optionsMap.remove(OPTION_NullnessDefault);
			}
		}
		void set(Map optionsMap) <- before void set(Map optionsMap);
		private void set(Map optionsMap) {
			Object optionValue;
			if ((optionValue = optionsMap.get(OPTION_AnnotationBasedNullAnalysis)) != null) {
				if (ENABLED.equals(optionValue)) {
					this.isAnnotationBasedNullAnalysisEnabled = true;
					// ensure that we actually have annotation names to use:
					ensureNullAnnotationNames();
				} else if (DISABLED.equals(optionValue)) {
					this.isAnnotationBasedNullAnalysisEnabled = false;
				}
			}
			if (this.isAnnotationBasedNullAnalysisEnabled) {
				if ((optionValue = optionsMap.get(OPTION_ReportNullContractViolation)) != null) updateSeverity(NullContractViolation, optionValue);
				if ((optionValue = optionsMap.get(OPTION_ReportPotentialNullContractViolation)) != null) updateSeverity(PotentialNullContractViolation, optionValue);
				if ((optionValue = optionsMap.get(OPTION_ReportNullContractInsufficientInfo)) != null) updateSeverity(NullContractInsufficientInfo, optionValue);
				if ((optionValue = optionsMap.get(OPTION_NullableAnnotationName)) != null) {
					this.nullableAnnotationName = CharOperation.splitAndTrimOn('.', ((String)optionValue).toCharArray());
				}
				if ((optionValue = optionsMap.get(OPTION_NonNullAnnotationName)) != null) {
					this.nonNullAnnotationName = CharOperation.splitAndTrimOn('.', ((String)optionValue).toCharArray());
				}
				if ((optionValue = optionsMap.get(OPTION_NullableByDefaultAnnotationName)) != null) {
					this.nullableByDefaultAnnotationName = CharOperation.splitAndTrimOn('.', ((String)optionValue).toCharArray());
				}
				if ((optionValue = optionsMap.get(OPTION_NonNullByDefaultAnnotationName)) != null) {
					this.nonNullByDefaultAnnotationName = CharOperation.splitAndTrimOn('.', ((String)optionValue).toCharArray());
				}
				if ((optionValue = optionsMap.get(OPTION_NullnessDefault)) != null) {
					if (NULLABLE.equals(optionValue)) {
						defaultNonNullness = TagBits.AnnotationNullable;
						ensureNullAnnotationNames();
					} else if (NONNULL.equals(optionValue)) {
						defaultNonNullness = TagBits.AnnotationNonNull;
						ensureNullAnnotationNames();
					} else {
						defaultNonNullness = 0;
					}
				}
			}
		}
		private void ensureNullAnnotationNames() {
			if (this.nullableAnnotationName == null)
				this.nullableAnnotationName = NullCompilerOptions.DEFAULT_NULLABLE_ANNOTATION_NAME;
			if (this.nonNullAnnotationName == null)
				this.nonNullAnnotationName = NullCompilerOptions.DEFAULT_NONNULL_ANNOTATION_NAME;
			if (this.nullableByDefaultAnnotationName == null)
				this.nullableByDefaultAnnotationName = NullCompilerOptions.DEFAULT_NULLABLEBYDEFAULT_ANNOTATION_NAME;
			if (this.nonNullByDefaultAnnotationName == null)
				this.nonNullByDefaultAnnotationName = NullCompilerOptions.DEFAULT_NONNULLBYDEFAULT_ANNOTATION_NAME;
		}
	}
	@SuppressWarnings("rawtypes")
	protected class JavaModelManager playedBy JavaModelManager {
		JavaModelManager getJavaModelManager() 			-> JavaModelManager getJavaModelManager();
		@SuppressWarnings("decapsulation")
		protected HashSet getOptionNames() 			 	-> get HashSet optionNames;
		IEclipsePreferences getInstancePreferences() 	-> IEclipsePreferences getInstancePreferences();
		public void storePreference(String key, char[][] value) {
			getInstancePreferences().put(key, String.valueOf(CharOperation.concatWith(value, '.')));
		}
	}
	@SuppressWarnings({"rawtypes","unchecked"})
	protected class JavaProject playedBy JavaProject {

		void fillOptionNames() <- before Map getOptions(boolean inheritJavaCoreOptions);

		private void fillOptionNames() {
			JavaModelManager javaModelManager = JavaModelManager.getJavaModelManager();
			HashSet optionNames = javaModelManager.getOptionNames();
			if (optionNames.contains(NullCompilerOptions.OPTION_AnnotationBasedNullAnalysis))
				return;
			optionNames.add(NullCompilerOptions.OPTION_AnnotationBasedNullAnalysis);
			optionNames.add(NullCompilerOptions.OPTION_NonNullAnnotationName);
			optionNames.add(NullCompilerOptions.OPTION_NullableAnnotationName);
			optionNames.add(NullCompilerOptions.OPTION_NonNullByDefaultAnnotationName);
			optionNames.add(NullCompilerOptions.OPTION_NullableByDefaultAnnotationName);
			optionNames.add(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo);
			optionNames.add(NullCompilerOptions.OPTION_ReportNullContractViolation);
			optionNames.add(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation);
			optionNames.add(NullCompilerOptions.OPTION_NullnessDefault);
			// also add to the instance preferences:
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NullableAnnotationName, NullCompilerOptions.DEFAULT_NULLABLE_ANNOTATION_NAME);
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NullableAnnotationName, NullCompilerOptions.DEFAULT_NULLABLE_ANNOTATION_NAME);
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NonNullAnnotationName, NullCompilerOptions.DEFAULT_NONNULL_ANNOTATION_NAME);
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NullableByDefaultAnnotationName, NullCompilerOptions.DEFAULT_NULLABLEBYDEFAULT_ANNOTATION_NAME);
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NonNullByDefaultAnnotationName, NullCompilerOptions.DEFAULT_NONNULLBYDEFAULT_ANNOTATION_NAME);
		}
	}
}
