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
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.IBinaryAnnotation;
import org.eclipse.jdt.internal.compiler.env.IBinaryMethod;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.InitializationFlowContext;
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
import base org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import base org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import base org.eclipse.jdt.internal.compiler.ast.MessageSend;
import base org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import base org.eclipse.jdt.internal.compiler.ast.Statement;
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
	@Instantiation(InstantiationPolicy.ALWAYS)
	protected abstract class Statement playedBy Statement {
		abstract Expression getExpression();
		
		// use custom hook from JDT/Core (https://bugs.eclipse.org/335093)
		checkAgainstNullAnnotation <- replace checkAgainstNullAnnotation;

		/** Check assignment to local with null annotation. */
		@SuppressWarnings("basecall")
		callin int checkAgainstNullAnnotation(BlockScope currentScope, LocalVariableBinding local,int nullStatus)
		{
			if (   local != null
				&& (local.tagBits & TagBits.AnnotationNonNull) != 0 
				&& nullStatus != FlowInfo.NON_NULL)
			{
				currentScope.problemReporter().possiblyNullToNonNullLocal(local.name, getExpression(), nullStatus,
						currentScope.environment().getNonNullAnnotationName());
				nullStatus=FlowInfo.NON_NULL;
			}
			return nullStatus;
		}
		
	}
	@Instantiation(InstantiationPolicy.ALWAYS)
	protected class Assignment extends Statement playedBy Assignment {
		/** Wire required method of super class. */
		Expression getExpression() -> get Expression expression;		
	}
	@Instantiation(InstantiationPolicy.ALWAYS)
	protected class LocalDeclaration extends Statement playedBy LocalDeclaration {
		/** Wire required method of super class. */
		Expression getExpression() -> get Expression initialization;
	}
	
	/** Analyse argument expressions as part of a MessageSend, check against method parameter annotation. */
	@Instantiation(InstantiationPolicy.ALWAYS)
	protected class MessageSend playedBy MessageSend {

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
		
		void analyseArguments(BlockScope currentScope, FlowInfo flowInfo) 
		<- after FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo)
			with { currentScope <- currentScope, flowInfo <- flowInfo }

		void analyseArguments(BlockScope currentScope, FlowInfo flowInfo) {
			// compare actual null-status against parameter annotations of the called method:
			MethodBinding methodBinding = getBinding();
			Expression[] arguments = getArguments();
			if (arguments != null && methodBinding.parameterNonNullness != null) {
				int length = arguments.length;
				for (int i = 0; i < length; i++) {
					int nullStatus = arguments[i].nullStatus(flowInfo); // slight loss of precision: should also use the null info from the receiver.
					if (   nullStatus != FlowInfo.NON_NULL 
						&& methodBinding.parameterNonNullness[i] != null)
					{
						if (methodBinding.parameterNonNullness[i].booleanValue()) // if @NonNull is required
						{
							char[][] annotationName = currentScope.environment().getNonNullAnnotationName();
							currentScope.problemReporter().possiblyNullToNonNullParameter(arguments[i], nullStatus, annotationName[annotationName.length-1]);
						}
					}
				}
			}
		}

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
	
	/** Analyse the expression within a return statement, check against method return annotation. */
	@Instantiation(InstantiationPolicy.ALWAYS)
	protected class ReturnStatement playedBy ReturnStatement {

		Expression getExpression() 	-> get Expression expression;
		int getSourceStart() 		-> get int sourceStart;
		int getSourceEnd() 			-> get int sourceEnd;
		
		void analyseNull(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) 
		<- after FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo);

		void analyseNull(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
			Expression expression = getExpression();
			if (expression != null) {
				flowInfo = expression.analyseCode(currentScope, flowContext, flowInfo); // may cause some issues to be reported twice :(
				int nullStatus = expression.nullStatus(flowInfo);
				if (nullStatus != FlowInfo.NON_NULL) {
					// if we can't prove non-null check against declared null-ness of the enclosing method:
					long tagBits;
					try {
						tagBits = currentScope.methodScope().referenceMethod().binding.tagBits;
					} catch (NullPointerException npe) {
						return;
					}
					if ((tagBits & TagBits.AnnotationNonNull) != 0) {
						char[][] annotationName = currentScope.environment().getNonNullAnnotationName();
						currentScope.problemReporter().possiblyNullFromNonNullMethod(this, nullStatus, 
								annotationName[annotationName.length-1]);
					}
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
	
	protected class AbstractMethodDeclaration playedBy AbstractMethodDeclaration {

		int sourceEnd() 						-> int sourceEnd();
		int sourceStart() 						-> int sourceStart();
		BlockScope getScope()      				-> get MethodScope scope;
		Argument[] getArguments()  				-> get Argument[] arguments;
		Annotation[] getAnnotations()			-> get Annotation[] annotations;
		void setAnnotations(Annotation[] annot)	-> set Annotation[] annotations;
		MethodBinding getBinding() 				-> get MethodBinding binding;
		void bindArguments()       				-> void bindArguments();
		
		
		void resolveArgumentNullAnnotations() <- replace void bindArguments();

		@SuppressWarnings("basecall")
		callin void resolveArgumentNullAnnotations() {
			MethodBinding binding = getBinding();
			if (binding != null) {
				if ((binding.getTagBits() & TagBits.HasBoundArguments) != 0) // avoid double execution
					return;
				binding.addTagBit(TagBits.HasBoundArguments);
			}
			base.resolveArgumentNullAnnotations();
			Argument[] arguments = this.getArguments();
			if (arguments != null && binding != null) {
				for (int i = 0, length = arguments.length; i < length; i++) {
					Argument argument = arguments[i];
					// transfer nullness info from the argument to the method:
					if ((argument.binding.tagBits & (TagBits.AnnotationNonNull|TagBits.AnnotationNullable)) != 0) {
						if (binding.parameterNonNullness == null)
							binding.parameterNonNullness = new Boolean[arguments.length];
						binding.parameterNonNullness[i] = Boolean.valueOf((argument.binding.tagBits & TagBits.AnnotationNonNull) != 0);
					}
				}
			}
		}

		/** Feed null status from parameter annotation into the analysis of the method's body. */
		void analyseArgumentNullity(FlowInfo info)
		<- before void analyseCode(ClassScope classScope, InitializationFlowContext initializationContext, FlowInfo info)
			with { info <- info }

		private void analyseArgumentNullity(FlowInfo info) {
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
			setAnnotations(addAnnotation(annotations, annotationBinding));
		}
		/** 
		 * Metarialize a null parameter annotation that has been added from the current default,
		 * in order to ensure that this annotation will be generated into the .class file, too.
		 */
		public void addParameterNullnessAnnotation(int i, long defaultNullness, ReferenceBinding annotationBinding) {
			Argument argument = getArguments()[i];
			Annotation[] annotations = argument.annotations;
			argument.annotations = addAnnotation(annotations, annotationBinding);
		}

		Annotation[] addAnnotation(Annotation[] annotations, ReferenceBinding annotationBinding) {
			int sourceStart = this.sourceStart();
			long pos = ((long)sourceStart<<32) + this.sourceEnd();
			long[] poss = new long[annotationBinding.compoundName.length];
			Arrays.fill(poss, pos);
			MarkerAnnotation annotation = new MarkerAnnotation(new QualifiedTypeReference(annotationBinding.compoundName, poss), sourceStart);
			annotation.resolvedType = annotationBinding;
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

	/** Add a field to store parameter nullness information. */
	protected class MethodBinding playedBy MethodBinding {

		/** Store nullness information from annotation (incl. inherited contracts). */
		public Boolean[] parameterNonNullness;  // TRUE means @NonNull declared, FALSE means @Nullable declared, null means nothing declared

		ReferenceBinding getDeclaringClass() 	-> get ReferenceBinding declaringClass;
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
			if (this.parameterNonNullness == null)
				this.parameterNonNullness = new Boolean[getParameters().length];
			Boolean value = Boolean.valueOf(defaultNullness == TagBits.AnnotationNonNull);
			AbstractMethodDeclaration sourceMethod = sourceMethod();
			for (int i = 0; i < this.parameterNonNullness.length; i++) {
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
			if ((getTagBits() & (TagBits.AnnotationNonNull|TagBits.AnnotationNullable)) == 0) {
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

		void bindMethodArguments() <- after void computeMethods();

		void fillInDefaultNullness() <- after void checkMethods();
		
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
			
			if ((inheritedBits & TagBits.HasBoundArguments) == 0) {
				ReferenceBinding supertype = inheritedMethod.getDeclaringClass();
				if (!((ReferenceBinding) supertype.erasure()).isBinaryBinding()) {
					AbstractMethodDeclaration sourceMethod = inheritedMethod.sourceMethod();
					if (sourceMethod != null)
						sourceMethod.bindArguments();
				}
			}
			
			// return type:
			if ((inheritedBits & TagBits.AnnotationNonNull) != 0) {
				if ((currentBits & TagBits.AnnotationNullable) != 0) {
					AbstractMethodDeclaration methodDecl = currentMethod.sourceMethod();
					getType().problemReporter().illegalReturnRedefinition(methodDecl, inheritedMethod, 
																		environment.getNonNullAnnotationName());
				}
			}
			if ((currentBits & (TagBits.AnnotationNonNull|TagBits.AnnotationNullable)) == 0)
				currentMethod.addTagBit(inheritedBits & (TagBits.AnnotationNonNull|TagBits.AnnotationNullable));

			// parameters:
			Argument[] currentArguments = currentMethod.sourceMethod().getArguments();
			if (inheritedMethod.parameterNonNullness != null) {
				// inherited method has null-annotations, check and possibly transfer:
				
				// prepare for transferring (contract inheritance):
				if (currentMethod.parameterNonNullness == null)
					currentMethod.parameterNonNullness = new Boolean[currentMethod.getParameters().length];
				
				for (int i = 0; i < inheritedMethod.parameterNonNullness.length; i++) {
					
					Boolean inheritedNonNullNess = inheritedMethod.parameterNonNullness[i];
					if (inheritedNonNullNess != Boolean.TRUE) { 	 				 // super parameter is not restricted to @NonNull
						if (currentMethod.parameterNonNullness[i] == Boolean.TRUE) { // current parameter is restricted to @NonNull
							getType().problemReporter().illegalRedefinitionToNonNullParameter(
																currentArguments[i],
																inheritedMethod.getDeclaringClass(),
																inheritedNonNullNess == null
																? null
																: environment.getNullableAnnotationName());
							continue;
						} 
					}
					
					if (currentMethod.parameterNonNullness[i] == null && inheritedNonNullNess != null) {
						// inherit this annotation as the current method has no annotation:
						currentMethod.parameterNonNullness[i] = inheritedNonNullNess;
						VariableBinding argumentBinding = currentArguments[i].binding;
						argumentBinding.tagBits |= inheritedNonNullNess.booleanValue()
														? TagBits.AnnotationNonNull : TagBits.AnnotationNullable;
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

		void bindMethodArguments() {
			// binding the arguments is required for null contract checking, which needs argument annotations
			AbstractMethodDeclaration[] methodDeclarations = getMethodDeclarations();
			if (methodDeclarations != null)
				for (AbstractMethodDeclaration methodDecl : methodDeclarations)
					methodDecl.bindArguments();
		}
		
		/** 
		 * after checkMethods has passed down inherited nullness info,
		 * now fill in missing annotations from a default setting from an enclosing scope.
		 */
		void fillInDefaultNullness() {
			TypeBinding annotationBinding = findDefaultNullness();
			// apply this default to all methods:
			if (annotationBinding != null) {
				long defaultNullness = Constants.getNullnessTagbit(annotationBinding);
				MethodBinding[] methodBindings = getMethodBindings();
				if (methodBindings != null) {
					for (MethodBinding methodBinding : methodBindings)
						methodBinding.fillInDefaultNullness(defaultNullness, annotationBinding);
				}
			}
		}
	
		TypeBinding findDefaultNullness() {
			// find the applicable default inside->out:

			// type
			SourceTypeBinding type = getType();
			SourceTypeBinding currentType = type;
			TypeBinding annotationBinding = null;
			while (currentType != null) {
				annotationBinding = currentType.nullnessDefaultAnnotation;
				if (annotationBinding != null)
					return annotationBinding;
				currentType = currentType.enclosingType();
			}
			
			// package
			annotationBinding = type.getPackage().nullnessDefaultAnnotation;
			if (annotationBinding != null)
				return annotationBinding;
			
			// global
			long defaultNullness = getEnvironment().getGlobalOptions().defaultNonNullness;
			if (defaultNullness != 0) {
				annotationBinding = getEnvironment().getNullAnnotationBinding(defaultNullness);
				if (annotationBinding != null)
					return annotationBinding;
				
				// on this branch default was not defined using an annotation, thus annotation type can still be missing
				if (defaultNullness == TagBits.AnnotationNonNull)
					type.problemReporter().missingNullAnnotationType(getEnvironment().getNonNullAnnotationName());
				else if (defaultNullness == TagBits.AnnotationNullable)
					type.problemReporter().missingNullAnnotationType(getEnvironment().getNullableAnnotationName());
				else
					type.problemReporter().abortDueToInternalError("Illegal default nullness value: "+defaultNullness); //$NON-NLS-1$
				// reset default to avoid duplicate errors:
				getEnvironment().getGlobalOptions().defaultNonNullness = 0;
			}
			return null;
		}
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
		
		protected TypeBinding nullnessDefaultAnnotation;

		/** initialize a normal type */
		evaluateNullAnnotations 			<- after getAnnotationTagBits;
		
		/** initialize a package-info.java */
		evaluateNullAnnotations 			<- after initializeDeprecatedAnnotationTagBits
			base when (CharOperation.equals(base.sourceName, TypeConstants.PACKAGE_INFO_NAME));

		@SuppressWarnings("inferredcallout")
		void evaluateNullAnnotations() {
			// transfer nullness info from tagBits to this.nullnessDefaultAnnotation 
			long tagBit = Constants.applyDefaultNullnessTagbit(getTagBits());
			if (tagBit == 0)
				return;
			TypeBinding nullnessDefaultAnnotation = getPackage().getEnvironment().getNullAnnotationBinding(tagBit);
			if (nullnessDefaultAnnotation != null) {
				if (CharOperation.equals(this.sourceName, TypeConstants.PACKAGE_INFO_NAME)) {
					getPackage().nullnessDefaultAnnotation = nullnessDefaultAnnotation;
				} else {
					this.nullnessDefaultAnnotation = nullnessDefaultAnnotation;
				}
			}
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
						defaultNullness = getEnvironment().getNullAnnotationBinding(TagBits.AnnotationNonNull);
						break;
					}
					if (CharOperation.equals(typeName, nullableByDefaultAnnotationName)) {
						annotationBit = TagBits.AnnotationNullableByDefault;
						defaultNullness = getEnvironment().getNullAnnotationBinding(TagBits.AnnotationNullable);
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
	}

	/** The package holding the configured null annotation types detects and marks these annotation types. */
	protected class PackageBinding playedBy PackageBinding {

		LookupEnvironment getEnvironment() 				-> get LookupEnvironment environment;

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
	}

	/** Intermediate role to provide access to environment and problemReporter as roles, too. */
	protected class BlockScope playedBy BlockScope {
		ProblemReporter problemReporter() 	-> ProblemReporter problemReporter();
		MethodScope methodScope() 			-> MethodScope methodScope();
		LookupEnvironment environment() 	-> LookupEnvironment environment();
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
				case IProblem.DefiniteNullFromNonNullMethod:
				case IProblem.DefiniteNullToNonNullLocal:
				case IProblem.DefiniteNullToNonNullParameter:
				case IProblem.IllegalReturnNullityRedefinition:
				case IProblem.IllegalRedefinitionToNonNullParameter:
				case IProblem.IllegalDefinitionToNonNullParameter:
					return CompilerOptions.NullContractViolation;
				case IProblem.PotentialNullFromNonNullMethod:
				case IProblem.PotentialNullToNonNullLocal:
				case IProblem.PotentialNullToNonNullParameter:
					return CompilerOptions.PotentialNullContractViolation;
				case IProblem.NonNullLocalInsufficientInfo:
				case IProblem.NonNullParameterInsufficientInfo:
				case IProblem.NonNullReturnInsufficientInfo:
					return CompilerOptions.NullContractInsufficientInfo;
				case IProblem.PotentialNullMessageSendReference:
					return org.eclipse.jdt.internal.compiler.impl.CompilerOptions.PotentialNullReference;
				case IProblem.RedundantNullCheckOnNonNullMessageSend:
					return org.eclipse.jdt.internal.compiler.impl.CompilerOptions.RedundantNullCheck;
			}
			return 0;
		}
	
		public void possiblyNullFromNonNullMethod(ReturnStatement returnStatement, int nullStatus, char[] annotationName) {
			int problemId = IProblem.NonNullReturnInsufficientInfo;
			if ((nullStatus & FlowInfo.NULL) != 0)
				problemId = IProblem.DefiniteNullFromNonNullMethod;
			if ((nullStatus & FlowInfo.POTENTIALLY_NULL) != 0)
				problemId = IProblem.PotentialNullFromNonNullMethod;
			String[] arguments = new String[] { String.valueOf(annotationName) };
			this.handle(
				problemId,
				arguments,
				arguments,
				returnStatement.getSourceStart(),
				returnStatement.getSourceEnd());
		}
		public void possiblyNullToNonNullLocal(char[] variableName, org.eclipse.jdt.internal.compiler.ast.Expression expression, int nullStatus, char[][] annotationName) {
			int problemId = IProblem.NonNullLocalInsufficientInfo;
			if ((nullStatus & FlowInfo.NULL) != 0)
				problemId = IProblem.DefiniteNullToNonNullLocal;
			else if ((nullStatus & FlowInfo.POTENTIALLY_NULL) != 0)
				problemId = IProblem.PotentialNullToNonNullLocal;
			String[] arguments = new String[] {
					String.valueOf(variableName),
					String.valueOf(annotationName[annotationName.length-1]) 
			};
			this.handle(
				problemId,
				arguments,
				arguments,
				expression.sourceStart,
				expression.sourceEnd);
		}
		public void possiblyNullToNonNullParameter(org.eclipse.jdt.internal.compiler.ast.Expression argument, int nullStatus, char[] annotationName) {
			int problemId = IProblem.NonNullParameterInsufficientInfo;
			if ((nullStatus & FlowInfo.NULL) != 0)
				problemId = IProblem.DefiniteNullToNonNullParameter;
			else if ((nullStatus & FlowInfo.POTENTIALLY_NULL) != 0)
				problemId = IProblem.PotentialNullToNonNullParameter;
			String[] arguments = new String[] { String.valueOf(annotationName) };
			this.handle(
				problemId,
				arguments,
				arguments,
				argument.sourceStart,
				argument.sourceEnd);
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
		public void illegalReturnRedefinition(org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration abstractMethodDecl,
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
			int sourceStart = methodDecl.returnType.sourceStart;
			if (methodDecl.annotations != null) {
				for (int i=0; i<methodDecl.annotations.length; i++) {
					if (methodDecl.annotations[i].resolvedType.id == TypeIds.T_ConfiguredAnnotationNullable) {
						sourceStart = methodDecl.annotations[i].sourceStart;
						break;
					}
				}
			}
			this.handle(
				IProblem.IllegalReturnNullityRedefinition, 
				new String[] { methodSignature.toString(), CharOperation.toString(nonNullAnnotationName)},
				new String[] { shortSignature.toString(), new String(nonNullAnnotationName[nonNullAnnotationName.length-1])},
				sourceStart, 
				methodDecl.returnType.sourceEnd);
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
		void internalStorePreference(String key, String value, IEclipsePreferences pref)
														-> boolean storePreference(String key, String value, IEclipsePreferences pref);
		public void storePreference(String key, String value) {
			internalStorePreference(key, value, getInstancePreferences());
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
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NullableAnnotationName, String.valueOf(CharOperation.concatWith(NullCompilerOptions.DEFAULT_NULLABLE_ANNOTATION_NAME, '.')));
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NullableAnnotationName, String.valueOf(CharOperation.concatWith(NullCompilerOptions.DEFAULT_NULLABLE_ANNOTATION_NAME, '.')));
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NonNullAnnotationName, String.valueOf(CharOperation.concatWith(NullCompilerOptions.DEFAULT_NONNULL_ANNOTATION_NAME, '.')));
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NullableByDefaultAnnotationName, String.valueOf(CharOperation.concatWith(NullCompilerOptions.DEFAULT_NULLABLEBYDEFAULT_ANNOTATION_NAME, '.')));
			javaModelManager.storePreference(NullCompilerOptions.OPTION_NonNullByDefaultAnnotationName, String.valueOf(CharOperation.concatWith(NullCompilerOptions.DEFAULT_NONNULLBYDEFAULT_ANNOTATION_NAME, '.')));
		}
	}
}
