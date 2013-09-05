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
 *								bug 342671 - ClassCastException: org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding cannot be cast to org.eclipse.jdt.internal.compiler.lookup.ArrayBinding
 *								bug 392099 - [1.8][compiler][null] Apply null annotation on types for null analysis
 *        Andy Clement - Contributions for
 *                          Bug 383624 - [1.8][compiler] Revive code generation support for type annotations (from Olivier's work)
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.TypeAnchorReference;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.Dependencies;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.ITranslationStates;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.ITeamAnchor;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.OTClassScope;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.ProblemAnchorBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.RoleTypeBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.RoleTypeCreator;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.TypeAnalyzer;

/**
 * OTDT changes:
 * What: support baseclass decapsulation
 *
 * What: support value parameters
 *
 * Syntactic representation of a reference to a generic type.
 * Note that it might also have a dimension.
 */
public class ParameterizedSingleTypeReference extends ArrayTypeReference {

	public TypeReference[] typeArguments;

//{ObjectTeams: anchor(s):
	public TypeAnchorReference[] typeAnchors;
	// decapsulation:
	@Override
	public void setBaseclassDecapsulation(DecapsulationState state) {
		super.setBaseclassDecapsulation(state);
		if (this.typeArguments != null)
			for (TypeReference argument : this.typeArguments)
				if (argument != null)
					argument.setBaseclassDecapsulation(state);
	}
// SH}

	public ParameterizedSingleTypeReference(char[] name, TypeReference[] typeArguments, int dim, long pos){
		super(name, dim, pos);
		this.originalSourceEnd = this.sourceEnd;
		this.typeArguments = typeArguments;
		for (int i = 0, max = typeArguments.length; i < max; i++) {
			if ((typeArguments[i].bits & ASTNode.HasTypeAnnotations) != 0) {
				this.bits |= ASTNode.HasTypeAnnotations;
				break;
			}
		}
	}
	public ParameterizedSingleTypeReference(char[] name, TypeReference[] typeArguments, int dim, Annotation[][] annotationsOnDimensions, long pos) {
		this(name, typeArguments, dim, pos);
		this.annotationsOnDimensions = annotationsOnDimensions;
		if (annotationsOnDimensions != null) {
			this.bits |= ASTNode.HasTypeAnnotations;
		}
	}
	public void checkBounds(Scope scope) {
		if (this.resolvedType == null) return;

		if (this.resolvedType.leafComponentType() instanceof ParameterizedTypeBinding) {
			ParameterizedTypeBinding parameterizedType = (ParameterizedTypeBinding) this.resolvedType.leafComponentType();
			ReferenceBinding currentType = parameterizedType.genericType();
			TypeVariableBinding[] typeVariables = currentType.typeVariables();
			TypeBinding[] argTypes = parameterizedType.arguments;
			if (argTypes != null && typeVariables != null) { // may be null in error cases
				parameterizedType.boundCheck(scope, this.typeArguments);
			}
		}
	}
	/**
	 * @see org.eclipse.jdt.internal.compiler.ast.TypeReference#copyDims(int)
	 */
	public TypeReference copyDims(int dim) {
		return new ParameterizedSingleTypeReference(this.token, this.typeArguments, dim, (((long)this.sourceStart)<<32)+this.sourceEnd);
	}
	public TypeReference copyDims(int dim, Annotation [][] annotationsOnDims) {
		ParameterizedSingleTypeReference parameterizedSingleTypeReference = new ParameterizedSingleTypeReference(this.token, this.typeArguments, dim, annotationsOnDims, (((long)this.sourceStart)<<32)+this.sourceEnd);
		parameterizedSingleTypeReference.bits |= (this.bits & ASTNode.HasTypeAnnotations);
		if (annotationsOnDims != null) {
			parameterizedSingleTypeReference.bits |= ASTNode.HasTypeAnnotations;
		}
		return parameterizedSingleTypeReference;
	}

	/**
	 * @return char[][]
	 */
	public char [][] getParameterizedTypeName(){
		StringBuffer buffer = new StringBuffer(5);
		buffer.append(this.token).append('<');
//{ObjectTeams: type anchors?
		boolean haveOne = false;
		if (this.typeAnchors != null) {
			for (int i = 0, length = this.typeAnchors.length; i < length; i++) {
				if (i > 0) buffer.append(',');
				buffer.append(CharOperation.concatWith(this.typeAnchors[i].getParameterizedTypeName(), '.'));
				haveOne = true;
			}
		}
// SH}
		for (int i = 0, length = this.typeArguments.length; i < length; i++) {
//{ObjectTeams:
		  if (haveOne)
			  buffer.append(',');
		  else
// SH}
			if (i > 0) buffer.append(',');
			buffer.append(CharOperation.concatWith(this.typeArguments[i].getParameterizedTypeName(), '.'));
		}
		buffer.append('>');
		int nameLength = buffer.length();
		char[] name = new char[nameLength];
		buffer.getChars(0, nameLength, name, 0);
		int dim = this.dimensions;
		if (dim > 0) {
			char[] dimChars = new char[dim*2];
			for (int i = 0; i < dim; i++) {
				int index = i*2;
				dimChars[index] = '[';
				dimChars[index+1] = ']';
			}
			name = CharOperation.concat(name, dimChars);
		}
		return new char[][]{ name };
	}
	/**
     * @see org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference#getTypeBinding(org.eclipse.jdt.internal.compiler.lookup.Scope)
     */
    protected TypeBinding getTypeBinding(Scope scope) {
        return null; // not supported here - combined with resolveType(...)
    }
    
    public boolean isParameterizedTypeReference() {
    	return true;
    }

    /*
     * No need to check for reference to raw type per construction
     */
	private TypeBinding internalResolveType(Scope scope, ReferenceBinding enclosingType, boolean checkBounds) {
		// handle the error here
		this.constant = Constant.NotAConstant;
		if ((this.bits & ASTNode.DidResolve) != 0) { // is a shared type reference which was already resolved
			if (this.resolvedType != null) { // is a shared type reference which was already resolved
				if (this.resolvedType.isValidBinding()) {
					return this.resolvedType;
				} else {
					switch (this.resolvedType.problemId()) {
						case ProblemReasons.NotFound :
						case ProblemReasons.NotVisible :
						case ProblemReasons.InheritedNameHidesEnclosingName :
							TypeBinding type = this.resolvedType.closestMatch();
							return type;
						default :
							return null;
					}
				}
			}
		}
		this.bits |= ASTNode.DidResolve;
//{ObjectTeams: check team anchor first:
	    for (int typeParamPos=0; typeParamPos<this.typeArguments.length; typeParamPos++) {
		    if (this.typeArguments[typeParamPos] instanceof TypeAnchorReference)
		    {
		    	TypeAnchorReference typeAnchorReference = (TypeAnchorReference)this.typeArguments[typeParamPos];
				ITeamAnchor anchor = typeAnchorReference.resolveAnchor(scope);
				if (!ProblemAnchorBinding.checkAnchor(scope, typeAnchorReference, anchor, this.token)) {
					int problemId = anchor != null ? anchor.problemId() : ProblemReasons.AnchorNotFound;
					return this.resolvedType = new ProblemReferenceBinding(anchor, typeAnchorReference.getLastToken(), null, problemId);
				}
		    	for (ITeamAnchor seg : anchor.getBestNamePath())
		    		if (!seg.isFinal())
		    			scope.problemReporter().anchorPathNotFinal(typeAnchorReference, seg, this.token);
		    		// proceed to possibly report more errors
	
		    	// try member type (role) of anchor's type (team):
				this.resolvedType = anchor.resolveRoleType(this.token, this.dimensions); // FIXME(SH): although contained in a loop only one anchor can win
				if (this.resolvedType == null) {
					// not a role type try type with explicit value anchor:
					this.resolvedType = scope.getType(this.token); // resolve raw type (premature, see below).
					if (this.resolvedType.isTypeVariable()) {
						ITeamAnchor[] declaredAnchors = ((TypeVariableBinding)this.resolvedType).anchors;
						if (declaredAnchors != null && typeParamPos < declaredAnchors.length && declaredAnchors[typeParamPos].isValidBinding())
							if (!declaredAnchors[typeParamPos].hasSameBestNameAs(anchor))
								scope.problemReporter().rebindingTypeVariableAnchor(this, typeAnchorReference, declaredAnchors[typeParamPos]);
					}
					this.resolvedType = typeAnchorReference.createDependentTypeBinding(scope, this, typeParamPos);
					if (this.resolvedType == null) {
						this.resolvedType = new ProblemReferenceBinding(anchor, this.token, null, ProblemReasons.NotFound);
						return this.resolvedType;
					}
				}
				if (this.resolvedType != null && this.resolvedType.isValidBinding() && this.resolvedType instanceof ReferenceBinding) {
					if (!checkParameterizedRoleVisibility(scope, anchor, (ReferenceBinding) this.resolvedType))
						return this.resolvedType;
				}
	
				if (   shouldAnalyzeRoleReference()
					&& isIllegalQualifiedUseOfProtectedRole(scope))
					return this.resolvedType; // problem binding may be in this.resolvedType
				// consume first arg:
				int len = 0;
				if (this.typeAnchors != null) {
					len = this.typeAnchors.length;
					System.arraycopy(this.typeAnchors, 0,
								     this.typeAnchors = new TypeAnchorReference[len+1], 0, len);
				} else {
					this.typeAnchors = new TypeAnchorReference[1];
				}
				this.typeAnchors[len] = typeAnchorReference;
				len = this.typeArguments.length-1;
				System.arraycopy(this.typeArguments, 1,
								 this.typeArguments = new TypeReference[len], 0, len); // FIXME(SH): reducing this array conflicts with loop condition
	
				// note: handling of arrays differs for role and regular types
				if (len == 0)
					return this.resolvedType; // we're done

				// proceed with a word of warning:
				scope.problemReporter().experimentalFeature(this, "Implementation for mixed type and value parameters is experimental.");
		    }
		}
		// find a base import scope if that's allowed:
	    Scope importScope = null;
		if (getBaseclassDecapsulation().isAllowed()) {
			Scope currentScope = scope;
			while (currentScope != null) {
				if (currentScope instanceof OTClassScope) {
					importScope = ((OTClassScope)currentScope).getBaseImportScope();
					if (importScope != null)
						break;
				}
				currentScope = currentScope.parent;
			}
		}
		TypeBinding type = internalResolveLeafType(importScope, scope, enclosingType, checkBounds);
/* orig:
		TypeBinding type = internalResolveLeafType(scope, enclosingType, checkBounds);
  :giro */
// SH}
		resolveAnnotations(scope);
		// handle three different outcomes:
		if (type == null) {
			this.resolvedType = createArrayType(scope, this.resolvedType);
			return null;							// no useful type, but still captured dimensions into this.resolvedType
		} else {
			type = createArrayType(scope, type);
			if (!this.resolvedType.isValidBinding())
				return type;						// found some error, but could recover useful type (like closestMatch)
			else 
				return this.resolvedType = type; 	// no complaint, keep fully resolved type (incl. dimensions)
		}
	}
//{ObjectTeams: consider two scopes (base imports, regular):
	private TypeBinding internalResolveLeafType(Scope importScope, Scope scope, ReferenceBinding enclosingType, boolean checkBounds) {
/* orig:
	private TypeBinding internalResolveLeafType(Scope scope, ReferenceBinding enclosingType, boolean checkBounds) {
 */
		ReferenceBinding currentType;
		if (enclosingType == null) {
//:giro
		  if (importScope != null) 
			this.resolvedType = importScope.getType(this.token);
		  if (this.resolvedType == null || this.resolvedType.problemId() == ProblemReasons.NotFound)
// SH}
			this.resolvedType = scope.getType(this.token);
			if (this.resolvedType.isValidBinding()) {
				currentType = (ReferenceBinding) this.resolvedType;
			} else {
				reportInvalidType(scope);
				switch (this.resolvedType.problemId()) {
					case ProblemReasons.NotFound :
					case ProblemReasons.NotVisible :
					case ProblemReasons.InheritedNameHidesEnclosingName :
						TypeBinding type = this.resolvedType.closestMatch();
						if (type instanceof ReferenceBinding) {
							currentType = (ReferenceBinding) type;
							break;
						}
						//$FALL-THROUGH$ - unable to complete type binding, but still resolve type arguments
					default :
						boolean isClassScope = scope.kind == Scope.CLASS_SCOPE;
					int argLength = this.typeArguments.length;
					for (int i = 0; i < argLength; i++) {
						TypeReference typeArgument = this.typeArguments[i];
						if (isClassScope) {
							typeArgument.resolveType((ClassScope) scope);
						} else {
							typeArgument.resolveType((BlockScope) scope, checkBounds);
						}
					}
					return null;
				}
				// be resilient, still attempt resolving arguments
			}
			enclosingType = currentType.enclosingType(); // if member type
			if (enclosingType != null) {
				enclosingType = currentType.isStatic()
					? (ReferenceBinding) scope.environment().convertToRawType(enclosingType, false /*do not force conversion of enclosing types*/)
					: scope.environment().convertToParameterizedType(enclosingType);
				currentType = scope.environment().createParameterizedType((ReferenceBinding) currentType.erasure(), null /* no arg */, enclosingType);
			}
		} else { // resolving member type (relatively to enclosingType)
			this.resolvedType = currentType = scope.getMemberType(this.token, enclosingType);
			if (!this.resolvedType.isValidBinding()) {
				scope.problemReporter().invalidEnclosingType(this, currentType, enclosingType);
				return null;
			}
			if (isTypeUseDeprecated(currentType, scope))
				scope.problemReporter().deprecatedType(currentType, this);
			ReferenceBinding currentEnclosing = currentType.enclosingType();
			if (currentEnclosing != null && currentEnclosing.erasure() != enclosingType.erasure()) {
				enclosingType = currentEnclosing; // inherited member type, leave it associated with its enclosing rather than subtype
			}
		}

		// check generic and arity
	    boolean isClassScope = scope.kind == Scope.CLASS_SCOPE;
	    TypeReference keep = null;
	    if (isClassScope) {
	    	keep = ((ClassScope) scope).superTypeReference;
	    	((ClassScope) scope).superTypeReference = null;
	    }
		int argLength = this.typeArguments.length;
		TypeBinding[] argTypes = new TypeBinding[argLength];
		boolean argHasError = false;
		ReferenceBinding currentOriginal = (ReferenceBinding)currentType.original();
		for (int i = 0; i < argLength; i++) {
		    TypeReference typeArgument = this.typeArguments[i];
//{ObjectTeams: limitation (value parameter other than 1st position).
		    if (typeArgument instanceof TypeAnchorReference) {
		    	scope.problemReporter().incompleteDependentTypesImplementation(typeArgument,
		    									"type value parameter must be in 1st position"); //$NON-NLS-1$
		    	argHasError = true;
		    	continue;
		    }
// SH}
		    TypeBinding argType = isClassScope
				? typeArgument.resolveTypeArgument((ClassScope) scope, currentOriginal, i)
				: typeArgument.resolveTypeArgument((BlockScope) scope, currentOriginal, i);
			this.bits |= (typeArgument.bits & ASTNode.HasTypeAnnotations);
		     if (argType == null) {
		         argHasError = true;
		     } else {
//{ObjectTeams:
		    	argType = RoleTypeCreator.maybeWrapUnqualifiedRoleType(scope, argType, typeArgument);
// SH}
			    if (typeArgument.annotations != null)
			    	argTypes[i] = captureTypeAnnotations(scope, enclosingType, argType, typeArgument.annotations[0]);
			    else
			    	argTypes[i] = argType;
		     }
		}
		if (argHasError) {
			return null;
		}
		if (isClassScope) {
	    	((ClassScope) scope).superTypeReference = keep;
			if (((ClassScope) scope).detectHierarchyCycle(currentOriginal, this))
				return null;
		}

		final boolean isDiamond = (this.bits & ASTNode.IsDiamond) != 0;
		TypeVariableBinding[] typeVariables = currentOriginal.typeVariables();
		if (typeVariables == Binding.NO_TYPE_VARIABLES) { // non generic invoked with arguments
			boolean isCompliant15 = scope.compilerOptions().originalSourceLevel >= ClassFileConstants.JDK1_5;
			if ((currentOriginal.tagBits & TagBits.HasMissingType) == 0) {
				if (isCompliant15) { // below 1.5, already reported as syntax error
					this.resolvedType = currentType;
					scope.problemReporter().nonGenericTypeCannotBeParameterized(0, this, currentType, argTypes);
					return null;
				}
			}
			// resilience do not rebuild a parameterized type unless compliance is allowing it
			if (!isCompliant15) {
				if (!this.resolvedType.isValidBinding())
					return currentType;
				return this.resolvedType = currentType;
			}
			// if missing generic type, and compliance >= 1.5, then will rebuild a parameterized binding
		} else if (argLength != typeVariables.length) {
			if (!isDiamond) { // check arity, IsDiamond never set for 1.6-
				scope.problemReporter().incorrectArityForParameterizedType(this, currentType, argTypes);
				return null;
			} 
		} else if (!currentType.isStatic()) {
			ReferenceBinding actualEnclosing = currentType.enclosingType();
			if (actualEnclosing != null && actualEnclosing.isRawType()){
				scope.problemReporter().rawMemberTypeCannotBeParameterized(
						this, scope.environment().createRawType(currentOriginal, actualEnclosing), argTypes);
				return null;
			}
		}
//{ObjectTeams: already done?
		if (!isDiamond && argLength == 0)
			return this.resolvedType;
// SH}

    	ParameterizedTypeBinding parameterizedType = scope.environment().createParameterizedType(currentOriginal, argTypes, enclosingType);
		// check argument type compatibility for non <> cases - <> case needs no bounds check, we will scream foul if needed during inference.
    	if (!isDiamond) {
    		if (checkBounds) // otherwise will do it in Scope.connectTypeVariables() or generic method resolution
    			parameterizedType.boundCheck(scope, this.typeArguments);
    		else
    			scope.deferBoundCheck(this);
    	}
		if (isTypeUseDeprecated(parameterizedType, scope))
			reportDeprecatedType(parameterizedType, scope);

		if (!this.resolvedType.isValidBinding()) {
			return parameterizedType;
		}
		return this.resolvedType = parameterizedType;
	}
	private TypeBinding createArrayType(Scope scope, TypeBinding type) {
		if (this.dimensions > 0) {
			if (this.dimensions > 255)
				scope.problemReporter().tooManyDimensions(this);
			return scope.createArrayType(type, this.dimensions);
		}
		return type;
	}

	public StringBuffer printExpression(int indent, StringBuffer output){
		if (this.annotations != null && this.annotations[0] != null) {
			printAnnotations(this.annotations[0], output);
			output.append(' ');
		}
		output.append(this.token);
		output.append("<"); //$NON-NLS-1$
		int length = this.typeArguments.length;
//{ObjectTeams: value parameters:
		if (this.typeAnchors != null) {
			int anchorLen = this.typeAnchors.length;
			for (int i = 0; i < anchorLen; i++) {
				this.typeAnchors[i].print(0, output);
				if (i+1 < anchorLen || length > 0)
					output.append(", "); //$NON-NLS-1$
			}
		}
// SH}
		if (length > 0) {
			int max = length - 1;
			for (int i= 0; i < max; i++) {
				this.typeArguments[i].print(0, output);
				output.append(", ");//$NON-NLS-1$
			}
			this.typeArguments[max].print(0, output);
		}
		output.append(">"); //$NON-NLS-1$
		if ((this.bits & IsVarArgs) != 0) {
			for (int i= 0 ; i < this.dimensions - 1; i++) {
				if (this.annotationsOnDimensions != null && this.annotationsOnDimensions[i] != null) {
					output.append(" "); //$NON-NLS-1$
					printAnnotations(this.annotationsOnDimensions[i], output);
					output.append(" "); //$NON-NLS-1$
				}
				output.append("[]"); //$NON-NLS-1$
			}
			if (this.annotationsOnDimensions != null && this.annotationsOnDimensions[this.dimensions - 1] != null) {
				output.append(" "); //$NON-NLS-1$
				printAnnotations(this.annotationsOnDimensions[this.dimensions - 1], output);
				output.append(" "); //$NON-NLS-1$
			}
			output.append("..."); //$NON-NLS-1$
		} else {
			for (int i= 0 ; i < this.dimensions; i++) {
				if (this.annotationsOnDimensions != null && this.annotationsOnDimensions[i] != null) {
					output.append(" "); //$NON-NLS-1$
					printAnnotations(this.annotationsOnDimensions[i], output);
					output.append(" "); //$NON-NLS-1$
				}
				output.append("[]"); //$NON-NLS-1$
			}
		}
		return output;
	}

	public TypeBinding resolveType(BlockScope scope, boolean checkBounds) {
	    return internalResolveType(scope, null, checkBounds);
	}

	public TypeBinding resolveType(ClassScope scope) {
	    return internalResolveType(scope, null, false /*no bounds check in classScope*/);
	}

	public TypeBinding resolveTypeEnclosing(BlockScope scope, ReferenceBinding enclosingType) {
	    return internalResolveType(scope, enclosingType, true/*check bounds*/);
	}

//{ObjectTeams: resolve helpers
	// for base-imported types (only single is supported):
	@Override
	public TypeBinding checkResolveUsingBaseImportScope(Scope scope, boolean tolerate) {
		// same as in SingleTypeReference:
		if (   this.getBaseclassDecapsulation().isAllowed()
			|| tolerate
			|| scope.isBaseGuard())
		{
			TypeBinding problem = this.resolvedType;
			this.resolvedType = null; // force re-computation in getTypeBinding()
			Scope currentScope = scope;
			while (currentScope != null) {
				if (currentScope instanceof OTClassScope) {
					Scope baseImportScope = ((OTClassScope)currentScope).getBaseImportScope();
					if (baseImportScope != null) {
						this.resolvedType = getTypeBinding(baseImportScope);
						if (this.resolvedType != null && this.resolvedType.isValidBinding())
							return this.resolvedType = checkResolvedType(this.resolvedType, baseImportScope, false);
					}
				}
				currentScope = currentScope.parent;
			}
			this.resolvedType = problem;
		}
		return null;
	}
	// check for externalized role's visibility
	boolean checkParameterizedRoleVisibility(Scope scope, ITeamAnchor anchor, ReferenceBinding type) {
		if (!type.isPublic()) {
			Dependencies.ensureBindingState(type, ITranslationStates.STATE_LENV_CONNECT_TYPE_HIERARCHY);
			if (TypeAnalyzer.isConfined(type)) {
				// confined has highest priority for reporting / doesn't allow decapsulation
				scope.problemReporter().decapsulatingConfined(this, (ReferenceBinding)this.resolvedType);
				setBaseclassDecapsulation(DecapsulationState.CONFINED);
				this.resolvedType = new ProblemReferenceBinding(anchor, this.token, type, ProblemReasons.NotVisible);
				return false;
			} else if (RoleTypeBinding.isRoleWithExplicitAnchor(type)) {
				// externalized non-public role
				switch (getBaseclassDecapsulation()) {
				case ALLOWED:
					scope.problemReporter().decapsulation(this, type);
					setBaseclassDecapsulation(DecapsulationState.REPORTED);
					//$FALL-THROUGH$
				case REPORTED:
					return true;
				case NONE:
					this.resolvedType = new ProblemReferenceBinding(anchor, this.token, type, ProblemReasons.NotVisible);
					scope.problemReporter().qualifiedProtectedRole(this, type);
					return false;
				}
			} else 	if (!type.canBeSeenBy(scope)) {
				// other invisible types
				this.resolvedType = new ProblemReferenceBinding(anchor, this.token, type, ProblemReasons.NotVisible);
				reportInvalidType(scope);
				return false;
			}
		}
		return true;
	}
// SH}
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.annotations != null) {
				Annotation [] typeAnnotations = this.annotations[0];
				for (int i = 0, length = typeAnnotations == null ? 0 : typeAnnotations.length; i < length; i++) {
					typeAnnotations[i].traverse(visitor, scope);
				}
			}
			if (this.annotationsOnDimensions != null) {
				for (int i = 0, max = this.annotationsOnDimensions.length; i < max; i++) {
					Annotation[] annotations2 = this.annotationsOnDimensions[i];
					if (annotations2 != null) {
						for (int j = 0, max2 = annotations2.length; j < max2; j++) {
							Annotation annotation = annotations2[j];
							annotation.traverse(visitor, scope);
						}
					}
				}
			}
			for (int i = 0, max = this.typeArguments.length; i < max; i++) {
				this.typeArguments[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}

	public void traverse(ASTVisitor visitor, ClassScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.annotations != null) {
				Annotation [] typeAnnotations = this.annotations[0];
				for (int i = 0, length = typeAnnotations == null ? 0 : typeAnnotations.length; i < length; i++) {
					typeAnnotations[i].traverse(visitor, scope);
				}
			}
			if (this.annotationsOnDimensions != null) {
				for (int i = 0, max = this.annotationsOnDimensions.length; i < max; i++) {
					Annotation[] annotations2 = this.annotationsOnDimensions[i];
					for (int j = 0, max2 = annotations2.length; j < max2; j++) {
						Annotation annotation = annotations2[j];
						annotation.traverse(visitor, scope);
					}
				}
			}
			for (int i = 0, max = this.typeArguments.length; i < max; i++) {
				this.typeArguments[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}
//{ObjectTeams: API for OT-Model:
	/**
     * API for OT-Model:
     * If typeReference is an anchored type, return the qualified name of the type anchor.
     */
	public static char[][] getTypeAnchor(TypeReference typeReference) {
		if (typeReference instanceof ParameterizedSingleTypeReference) {
			ParameterizedSingleTypeReference type= (ParameterizedSingleTypeReference)typeReference;
			if (type.resolvedType != null) {
				// after resolve use typeAnchors:
				TypeAnchorReference[] anchors= type.typeAnchors;
				if (anchors != null && anchors.length == 1)
					return beautifyTypeAnchor(anchors[0].getTypeName());
			} else {
				if (type.typeArguments != null && type.typeArguments.length > 0) {
					TypeReference first= type.typeArguments[0];
					if (first instanceof TypeAnchorReference) {
						char[][] anchorName= first.getTypeName();
						anchorName[0] = CharOperation.subarray(anchorName[0], 1, -1); // chop off '@'
						return beautifyTypeAnchor(anchorName);
					}
				}
			}
		}
		return null;
	}
	private static char[][] beautifyTypeAnchor(char[][] orig) {
		int len= orig.length;
		if (!CharOperation.equals(orig[len-1], IOTConstants._OT_BASE))
			return orig;
		char[][] result= new char[len][];
		System.arraycopy(orig, 0, result, 0, len-1);
		result[len-1]= IOTConstants.BASE;
		return result;
	}
// SH}
}
