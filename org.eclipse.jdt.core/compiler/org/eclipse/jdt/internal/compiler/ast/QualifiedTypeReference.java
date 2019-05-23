/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Fraunhofer FIRST - extended API and implementation
 *     Technical University Berlin - extended API and implementation
 *     Stephan Herrmann - Contribution for
 *								bug 392099 - [1.8][compiler][null] Apply null annotation on types for null analysis
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.TypeModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.RoleTypeCreator;

/**
 * OTDT changes:
 * What: support resolving of anchored types.
 *
 * What: support baseclass decapsulation
 *
 * What: detect and report qualifiedProtectedRole (except for generated references)
 *
 * @version $Id: QualifiedTypeReference.java 23404 2010-02-03 14:10:22Z stephan $
 */
public class QualifiedTypeReference extends TypeReference {

	public char[][] tokens;
	public long[] sourcePositions;

	public QualifiedTypeReference(char[][] sources , long[] poss) {

		this.tokens = sources ;
		this.sourcePositions = poss ;
		this.sourceStart = (int) (this.sourcePositions[0]>>>32) ;
		this.sourceEnd = (int)(this.sourcePositions[this.sourcePositions.length-1] & 0x00000000FFFFFFFFL ) ;
	}

	@Override
	public TypeReference augmentTypeWithAdditionalDimensions(int additionalDimensions, Annotation[][] additionalAnnotations, boolean isVarargs) {
		int totalDimensions = this.dimensions() + additionalDimensions;
		Annotation [][] allAnnotations = getMergedAnnotationsOnDimensions(additionalDimensions, additionalAnnotations);
		ArrayQualifiedTypeReference arrayQualifiedTypeReference = new ArrayQualifiedTypeReference(this.tokens, totalDimensions, allAnnotations, this.sourcePositions);
		arrayQualifiedTypeReference.annotations = this.annotations;
		arrayQualifiedTypeReference.bits |= (this.bits & ASTNode.HasTypeAnnotations);
		if (!isVarargs)
			arrayQualifiedTypeReference.extendedDimensions = additionalDimensions;
		return arrayQualifiedTypeReference;
	}

//{ObjectTeams:
	/**
	 * Try to resolve this type reference as an anchored type "t.R".
	 * @param scope
	 */
	@Override
	TypeBinding resolveAnchoredType(Scope scope) {
		if (this.tokens.length >= 2) {
			TypeBinding anchoredType = RoleTypeCreator.resolveAnchoredType(
					scope, this, this.tokens, dimensions());
			if (anchoredType != null) {
				// it is an anchored role type ...
				this.resolvedType = anchoredType;
				if (anchoredType.problemId() == ProblemReasons.NotVisible)
				{
					// ... but an illegal one
					ProblemReferenceBinding problemType = (ProblemReferenceBinding)anchoredType;
					scope.problemReporter().externalizingNonPublicRole(
							this,
							(ReferenceBinding)problemType.closestMatch().leafComponentType());
					return null; // don't report again
				}
				recordResolution(scope.environment(), anchoredType);
				return anchoredType; // includes the case of AnchorNotFinal
			}
		}
		return null;
	}
// SH}

	protected TypeBinding findNextTypeBinding(int tokenIndex, Scope scope, PackageBinding packageBinding) {
		LookupEnvironment env = scope.environment();
		try {
			env.missingClassFileLocation = this;
			if (this.resolvedType == null) {
				this.resolvedType = scope.getType(this.tokens[tokenIndex], packageBinding);
			} else {
//{ObjectTeams: members are in the classpart
		    	if (this.resolvedType.isRole())
		    		this.resolvedType = ((ReferenceBinding)this.resolvedType).roleModel.getClassPartBinding();
// SH}
				this.resolvedType = scope.getMemberType(this.tokens[tokenIndex], (ReferenceBinding) this.resolvedType);
				if (!this.resolvedType.isValidBinding()) {
					this.resolvedType = new ProblemReferenceBinding(
						CharOperation.subarray(this.tokens, 0, tokenIndex + 1),
						(ReferenceBinding)this.resolvedType.closestMatch(),
						this.resolvedType.problemId());
				}
			}
//{ObjectTeams: baseclass decapsulation?
		    checkBaseclassDecapsulation(scope);
// SH}
			return this.resolvedType;
		} catch (AbortCompilation e) {
			e.updateContext(this, scope.referenceCompilationUnit().compilationResult);
			throw e;
		} finally {
			env.missingClassFileLocation = null;
		}
	}

	@Override
	public char[] getLastToken() {
		return this.tokens[this.tokens.length-1];
	}

	protected void rejectAnnotationsOnPackageQualifiers(Scope scope, PackageBinding packageBinding) {
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=390882
		if (packageBinding == null || this.annotations == null) return;

		int i = packageBinding.compoundName.length;
		for (int j = 0; j < i; j++) {
			Annotation[] qualifierAnnot = this.annotations[j];
			if (qualifierAnnot != null && qualifierAnnot.length > 0) {
				if (j == 0) {
					for (int k = 0; k < qualifierAnnot.length; k++) {
						scope.problemReporter().typeAnnotationAtQualifiedName(qualifierAnnot[k]);
					}
				} else {
					scope.problemReporter().misplacedTypeAnnotations(qualifierAnnot[0],
							qualifierAnnot[qualifierAnnot.length - 1]);
					this.annotations[j] = null;
				}
			}
		}
	}

	protected static void rejectAnnotationsOnStaticMemberQualififer(Scope scope, ReferenceBinding currentType, Annotation[] qualifierAnnot) {
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=385137
		if (currentType.isMemberType() && currentType.isStatic() && qualifierAnnot != null && qualifierAnnot.length > 0) {
			scope.problemReporter().illegalTypeAnnotationsInStaticMemberAccess(qualifierAnnot[0],
					qualifierAnnot[qualifierAnnot.length - 1]);
		}
	}

	@Override
	protected TypeBinding getTypeBinding(Scope scope) {

		if (this.resolvedType != null) {
			return this.resolvedType;
		}
		Binding binding = scope.getPackage(this.tokens);
		if (this.resolvedType != null) { // recheck in case we had re-entrance
			return this.resolvedType;
		}
//{ObjectTeams: give it a try as a team.R type:	FIXME(SH): redundant if invoked from resolveType()
		if (binding == null || binding.problemId() == ProblemReasons.NotFound) {
			// inheritance from anchored type is not allowed (OTJLD 1.2.2(g)).
			boolean isSearchingSuper = ((scope instanceof ClassScope) && ((ClassScope)scope).superTypeReference == this);
			if (this.tokens.length >= 2 && !isSearchingSuper) {
				// 0-dimensions because ArrayQualifiedTypeReference will wrap roleType in an ArrayBinding later.
				TypeBinding roleType = RoleTypeCreator.resolveAnchoredType(scope, this, this.tokens, 0);
				if (roleType != null && roleType.problemId() != ProblemReasons.NotFound) {
					this.resolvedType = roleType;
					TypeModel.checkReferencedTeam(roleType, scope);
					ReferenceBinding enclosingType = scope.enclosingSourceType();
					if (!(enclosingType != null  && enclosingType.isSynthInterface()))
						if ((this.bits & IsAllocationType) == 0) // new path.R() is a different story
							reportDeprecatedPathSyntax(scope);
					recordResolution(scope.environment(), roleType);
					return roleType;
				}
			}
		}
// SH}
		if (binding != null && !binding.isValidBinding()) {
			if (binding instanceof ProblemReferenceBinding && binding.problemId() == ProblemReasons.NotFound) {
				ProblemReferenceBinding problemBinding = (ProblemReferenceBinding) binding;
				Binding pkg = scope.getTypeOrPackage(this.tokens);
				return new ProblemReferenceBinding(problemBinding.compoundName, pkg instanceof PackageBinding ? null : scope.environment().createMissingType(null, this.tokens), ProblemReasons.NotFound);
			}
			return (ReferenceBinding) binding; // not found
		}
	    PackageBinding packageBinding = binding == null ? null : (PackageBinding) binding;
	    int typeStart = packageBinding == null ? 0 : packageBinding.compoundName.length;
	    
	    if (packageBinding != null) {
	    	PackageBinding uniquePackage = packageBinding.getVisibleFor(scope.module(), false);
	    	if (uniquePackage instanceof SplitPackageBinding) {
	    		CompilerOptions compilerOptions = scope.compilerOptions();
	    		boolean inJdtDebugCompileMode = compilerOptions.enableJdtDebugCompileMode;
	    		if (!inJdtDebugCompileMode) {
	    			SplitPackageBinding splitPackage = (SplitPackageBinding) uniquePackage;
	    			scope.problemReporter().conflictingPackagesFromModules(splitPackage, scope.module(), this.sourceStart, (int)this.sourcePositions[typeStart-1]);
	    			this.resolvedType = new ProblemReferenceBinding(this.tokens, null, ProblemReasons.Ambiguous);
	    			return null;
	    		}
	    	}
	    }
	    rejectAnnotationsOnPackageQualifiers(scope, packageBinding);

	    boolean isClassScope = scope.kind == Scope.CLASS_SCOPE;
	    ReferenceBinding qualifiedType = null;
		for (int i = typeStart, max = this.tokens.length, last = max-1; i < max; i++) {
			findNextTypeBinding(i, scope, packageBinding);
			if (!this.resolvedType.isValidBinding())
				return this.resolvedType;
			if (i == 0 && this.resolvedType.isTypeVariable() && ((TypeVariableBinding) this.resolvedType).firstBound == null) { // cannot select from a type variable
				scope.problemReporter().illegalAccessFromTypeVariable((TypeVariableBinding) this.resolvedType, this);
				return null;
			}
			if (i <= last && isTypeUseDeprecated(this.resolvedType, scope)) {
				reportDeprecatedType(this.resolvedType, scope, i);
			}
//{ObjectTeams: statically qualified use of role?
			if (i > 0 && !this.isGenerated && shouldAnalyzeRoleReference()) { // generated (and copied) methods are allowed to use MyTeam.R
				if (isIllegalQualifiedUseOfProtectedRole(scope)) {
					// already reported
				} else {
					scope.problemReporter().qualifiedRole(this, (ReferenceBinding)this.resolvedType);
				}
				// keep a problem binding,  clients may be interested in this information
				return this.resolvedType = new ProblemReferenceBinding(((ReferenceBinding)this.resolvedType).compoundName, (ReferenceBinding)this.resolvedType, ProblemReasons.ProblemAlreadyReported);
			}
// SH}
			if (isClassScope)
				if (((ClassScope) scope).detectHierarchyCycle(this.resolvedType, this)) // must connect hierarchy to find inherited member types
					return null;
			ReferenceBinding currentType = (ReferenceBinding) this.resolvedType;
			if (qualifiedType != null) {
				if (this.annotations != null) {
					rejectAnnotationsOnStaticMemberQualififer(scope, currentType, this.annotations[i-1]);
				}
				ReferenceBinding enclosingType = currentType.enclosingType();
				if (enclosingType != null && TypeBinding.notEquals(enclosingType.erasure(), qualifiedType.erasure())) {
					qualifiedType = enclosingType; // inherited member type, leave it associated with its enclosing rather than subtype
				}
				if (currentType.isGenericType()) {
					qualifiedType = scope.environment().createRawType(currentType, qualifiedType);
				} else if (!currentType.hasEnclosingInstanceContext()) {
					qualifiedType = currentType; // parameterization of enclosing is irrelevant in this case
				} else {
					boolean rawQualified = qualifiedType.isRawType();
					if (rawQualified) {
						qualifiedType = scope.environment().createRawType((ReferenceBinding)currentType.erasure(), qualifiedType);
					} else if (qualifiedType.isParameterizedType() && TypeBinding.equalsEquals(qualifiedType.erasure(), currentType.enclosingType().erasure())) {
						qualifiedType = scope.environment().createParameterizedType((ReferenceBinding)currentType.erasure(), null, qualifiedType);
					} else {
						qualifiedType = currentType;
					}
				}
			} else {
				qualifiedType = currentType.isGenericType() ? (ReferenceBinding)scope.environment().convertToRawType(currentType, false /*do not force conversion of enclosing types*/) : currentType;
			}
			recordResolution(scope.environment(), qualifiedType);
		}
		this.resolvedType = qualifiedType;
		return this.resolvedType;
	}
//{ObjectTeams: overridable hook for above
	protected void reportDeprecatedPathSyntax(Scope scope) {
		scope.problemReporter().deprecatedPathSyntax(this);		
	}
// SH}

	void recordResolution(LookupEnvironment env, TypeBinding typeFound) {
		if (typeFound != null && typeFound.isValidBinding()) {
			synchronized (env.root) {
				for (int i = 0; i < env.root.resolutionListeners.length; i++) {
					env.root.resolutionListeners[i].recordResolution(this, typeFound);
				}
			}
		}
	}

	@Override
	public char[][] getTypeName(){

		return this.tokens;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		for (int i = 0; i < this.tokens.length; i++) {
			if (i > 0) output.append('.');
			if (this.annotations != null && this.annotations[i] != null) {
				printAnnotations(this.annotations[i], output);
				output.append(' ');
			}
//{ObjectTeams: suppress prefix:
		  if (CharOperation.equals(this.tokens[i], IOTConstants._OT_BASE))
			output.append(IOTConstants.BASE);
		  else
// SH}
			output.append(this.tokens[i]);
		}
		return output;
	}

	@Override
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.annotations != null) {
				int annotationsLevels = this.annotations.length;
				for (int i = 0; i < annotationsLevels; i++) {
					int annotationsLength = this.annotations[i] == null ? 0 : this.annotations[i].length;
					for (int j = 0; j < annotationsLength; j++)
						this.annotations[i][j].traverse(visitor, scope);
				}
			}
		}
		visitor.endVisit(this, scope);
	}

	@Override
	public void traverse(ASTVisitor visitor, ClassScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.annotations != null) {
				int annotationsLevels = this.annotations.length;
				for (int i = 0; i < annotationsLevels; i++) {
					int annotationsLength = this.annotations[i] == null ? 0 : this.annotations[i].length;
					for (int j = 0; j < annotationsLength; j++)
						this.annotations[i][j].traverse(visitor, scope);
				}
			}
		}
		visitor.endVisit(this, scope);
	}
	@Override
	public int getAnnotatableLevels() {
		return this.tokens.length;
	}
}
