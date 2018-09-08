/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.RoleClassLiteralAccess;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.RoleTypeBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.TeamModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.RoleTypeCreator;

/**
 * OTDT changes:
 * What: distinguish verbatim ClassLiteralAccess and RoleClassLiteralAccess
 * Why:  see RoleClassLiteralAccess
 *
 * @version $Id: ClassLiteralAccess.java 23405 2010-02-03 17:02:18Z stephan $
 */
public class ClassLiteralAccess extends Expression {

	public TypeReference type;
	public TypeBinding targetType;
	FieldBinding syntheticField;

//{ObjectTeams: role class literals?
	Expression roleClassLiteralAccess = null;
//  marking generated instances that are verbatim, ie., never replaced with RoleClassLiteralAccess
	boolean verbatim = false;
	public ClassLiteralAccess(int sourceEnd, TypeReference t, boolean verbatim) {
		this(sourceEnd, t);
		this.verbatim = verbatim;
	}
// SH}

	public ClassLiteralAccess(int sourceEnd, TypeReference type) {
		this.type = type;
		type.bits |= IgnoreRawTypeCheck; // no need to worry about raw type usage
		this.sourceStart = type.sourceStart;
		this.sourceEnd = sourceEnd;
	}

	@Override
	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

//{ObjectTeams: role class literal?
		if (this.roleClassLiteralAccess != null)
			return this.roleClassLiteralAccess.analyseCode(currentScope, flowContext, flowInfo);
// SH}
		// if reachable, request the addition of a synthetic field for caching the class descriptor
		SourceTypeBinding sourceType = currentScope.outerMostClassScope().enclosingSourceType();
		// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=22334
		if (!sourceType.isInterface()
				&& !this.targetType.isBaseType()
				&& currentScope.compilerOptions().targetJDK < ClassFileConstants.JDK1_5) {
			this.syntheticField = sourceType.addSyntheticFieldForClassLiteral(this.targetType, currentScope);
		}
		return flowInfo;
	}

	/**
	 * MessageSendDotClass code generation
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
	@Override
	public void generateCode(
		BlockScope currentScope,
		CodeStream codeStream,
		boolean valueRequired) {
//{ObjectTeams: role class literal?
		if (this.roleClassLiteralAccess != null) {
			this.roleClassLiteralAccess.generateCode(currentScope, codeStream, valueRequired);
			return;
		}
// SH}
		int pc = codeStream.position;

		// in interface case, no caching occurs, since cannot make a cache field for interface
		if (valueRequired) {
			codeStream.generateClassLiteralAccessForType(this.type.resolvedType, this.syntheticField);
			codeStream.generateImplicitConversion(this.implicitConversion);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {

		return this.type.print(0, output).append(".class"); //$NON-NLS-1$
	}

	@Override
	public TypeBinding resolveType(BlockScope scope) {

		this.constant = Constant.NotAConstant;
		if ((this.targetType = this.type.resolveType(scope, true /* check bounds*/)) == null)
			return null;
		
		/* https://bugs.eclipse.org/bugs/show_bug.cgi?id=320463
		   https://bugs.eclipse.org/bugs/show_bug.cgi?id=312076
		   JLS3 15.8.2 forbids the type named in the class literal expression from being a parameterized type.
		   And the grammar in 18.1 disallows (where X and Y are some concrete types) constructs of the form
		   Outer<X>.class, Outer<X>.Inner.class, Outer.Inner<X>.class, Outer<X>.Inner<Y>.class etc.
		   Corollary wise, we should resolve the type of the class literal expression to be a raw type as
		   class literals exist only for the raw underlying type. 
		 */
		LookupEnvironment environment = scope.environment();
		this.targetType = environment.convertToRawType(this.targetType, true /* force conversion of enclosing types*/);

		if (this.targetType.isArrayType()) {
			ArrayBinding arrayBinding = (ArrayBinding) this.targetType;
			TypeBinding leafComponentType = arrayBinding.leafComponentType;
			if (leafComponentType == TypeBinding.VOID) {
				scope.problemReporter().cannotAllocateVoidArray(this);
				return null;
			} else if (leafComponentType.isTypeVariable()) {
				scope.problemReporter().illegalClassLiteralForTypeVariable((TypeVariableBinding)leafComponentType, this);
			}
		} else if (this.targetType.isTypeVariable()) {
			scope.problemReporter().illegalClassLiteralForTypeVariable((TypeVariableBinding)this.targetType, this);
		}
//{ObjectTeams: do we need a RoleClassLiteralAccess?
		if (this.targetType instanceof ReferenceBinding) {
			ReferenceBinding targetRef = (ReferenceBinding)this.targetType;
			if (targetRef.isRole()) {
				if (this.verbatim) {
					this.targetType= RoleTypeCreator.maybeWrapUnqualifiedRoleType(scope, this.targetType, this);
				} else {
					SourceTypeBinding site = scope.enclosingSourceType();
					if (   scope.methodScope().isStatic // role class literal needs team instance
						&& !site.isRole()               // static role method are OK.
						&& !RoleTypeBinding.isRoleWithExplicitAnchor(this.targetType)) // t.R.class?
					{
						scope.problemReporter().roleClassLiteralLacksTeamInstance(this, targetRef);
						return null;
					}
					ReferenceBinding teamBinding;
					if (RoleTypeBinding.isRoleWithExplicitAnchor(targetRef))
						teamBinding = targetRef.enclosingType();
					else
						teamBinding = TeamModel.findEnclosingTeamContainingRole(site, targetRef);
					if (teamBinding == null)
						scope.problemReporter().externalizedRoleClassLiteral(this, targetRef);
					else {
						TypeBinding methodType= RoleClassLiteralAccess.ensureGetClassMethod(
								teamBinding.getTeamModel(),
								targetRef.roleModel);       // not affected by visibility check (for resilience)
						this.roleClassLiteralAccess = new RoleClassLiteralAccess(this, methodType);
						this.resolvedType = this.roleClassLiteralAccess.resolveType(scope);
					}
					return this.resolvedType;
				}
			}
		}
// SH}
		ReferenceBinding classType = scope.getJavaLangClass();
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=328689
		if (scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) {
			// Integer.class --> Class<Integer>, perform boxing of base types (int.class --> Class<Integer>)
			TypeBinding boxedType = null;
			if (this.targetType.id == T_void) {
				boxedType = environment.getResolvedJavaBaseType(JAVA_LANG_VOID, scope);
			} else {
				boxedType = scope.boxing(this.targetType);
			}
			if (environment.usesNullTypeAnnotations())
				boxedType = environment.createAnnotatedType(boxedType, new AnnotationBinding[] { environment.getNonNullAnnotation() });
			this.resolvedType = environment.createParameterizedType(classType, new TypeBinding[]{ boxedType }, null/*not a member*/);
		} else {
			this.resolvedType = classType;
		}
		return this.resolvedType;
	}

	@Override
	public void traverse(
		ASTVisitor visitor,
		BlockScope blockScope) {
//{ObjectTeams: alternate AST?
		if (this.roleClassLiteralAccess != null) {
			this.roleClassLiteralAccess.traverse(visitor, blockScope);
			return;
		}
// SH}

		if (visitor.visit(this, blockScope)) {
			this.type.traverse(visitor, blockScope);
		}
		visitor.endVisit(this, blockScope);
	}
}
