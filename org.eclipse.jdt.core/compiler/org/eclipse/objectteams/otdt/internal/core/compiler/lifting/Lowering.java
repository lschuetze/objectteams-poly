/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2003, 2014 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.internal.core.compiler.lifting;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.RoleTypeBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.TeamModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.transformer.StandardElementGenerator;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.AstGenerator;

/**
 * Helper that generates the lowering statements necessary for the callout compiler-feature.
 *
 * Nested classes PushingExpression and PopExpression are used to store a value on
 * the stack that must be evaluated only once but is first used for a null-check
 * and later the same value is used for the actual translation.
 *
 * @author haebor
 * @version $Id: Lowering.java 23417 2010-02-03 20:13:55Z stephan $
 */
public class Lowering implements IOTConstants {

	/**
	 * This method generates a lowering translation for a given expression.
	 * We have three distinct alternatives:
	 * (1) arrays are translated via a method call (see ArrayLowering).
	 * (2) field access (applicable for same and sibling roles).
	 * (3) _OT$getBase() method (else).
	 *
	 * @param scope for lookup
	 * @param expression original
	 * @param unloweredType either a RoleTypeBinding or an ArrayBinding thereof.
	 * @param requiredType  the base type (or ArrayBinding thereof).
	 * @param teamExpression can be used as a receiver for array lowering method calls
	 * @param needNullCheck if expression maybe null, a null-check is required at runtime.
	 * @return translation expression
	 */
	public Expression lowerExpression(
              final BlockScope  scope,
              final Expression  expression,
		      TypeBinding        unloweredType,
			  TypeBinding        requiredType,
			  final Expression  teamExpression,
			  boolean           needNullCheck)
	{
		return lowerExpression(scope, expression, unloweredType, requiredType, teamExpression, needNullCheck, false);
	}
	public Expression lowerExpression(
            final BlockScope  scope,
            final Expression  expression,
		      TypeBinding unloweredType,
			  TypeBinding requiredType,
			  final Expression  teamExpression,
			  boolean     needNullCheck,
			  boolean 	   deferredResolve)
	{
        // Note, this method is responsible for 'resolving' all AST nodes it generates!

        int sourceStart = expression.sourceStart;
        int sourceEnd   = expression.sourceEnd;
    	AstGenerator gen = new AstGenerator(sourceStart, sourceEnd);

    	unloweredType = TeamModel.strengthenRoleType(scope.enclosingSourceType(), unloweredType);
        Expression loweringExpr = null;

        TypeBinding expressionType = expression.resolvedType;
        // this assertion needed for pushing/casting using unchecked one-byte opcodes.
        assert expressionType == null || expressionType.leafComponentType() instanceof ReferenceBinding;

    	LocalVariableBinding localVar = null;
    	Expression unloweredExpression = expression;
    	if (expression instanceof ThisReference || expression instanceof AllocationExpression)
    		needNullCheck = false;
    	if (needNullCheck) {
        	localVar = makeNewLocal(scope, unloweredType, sourceStart, sourceEnd);
        	SingleNameReference varRef = gen.singleNameReference(localVar.name);
        	varRef.binding = localVar;
        	varRef.bits = Binding.LOCAL;
        	varRef.constant = Constant.NotAConstant;
        	varRef.resolvedType = unloweredType;
			unloweredExpression = varRef;
    	}

        if(unloweredType.isArrayType())
		{
        	// (1) array translation:
			ArrayLowering trans = new ArrayLowering(teamExpression);
			loweringExpr =  trans.lowerArray(
								scope,
								unloweredExpression,
								unloweredType,
								requiredType,
								deferredResolve);
		}
		else
		{
            RoleTypeBinding roleType = (RoleTypeBinding)unloweredType;
			boolean needMethod =   roleType.isRegularInterface()
								|| !roleType.isSiblingRole(scope.enclosingSourceType());
            if (needMethod) {
            	// (3) translation using _OT$.getBase() method:
				MessageSend callLower = gen.messageSend(
						unloweredExpression,
                		IOTConstants._OT_GETBASE,
						new Expression[0]);

                // manual resolving:
                callLower.actualReceiverType = unloweredType;
                callLower.constant       = Constant.NotAConstant;
                callLower.resolvedType   = roleType.baseclass();

                callLower.binding =
                            StandardElementGenerator.getGetBaseMethod(
                            		scope,
                                    roleType.roleModel,
                                    roleType.baseclass());
                loweringExpr = callLower;
            } else {
                // (2) translation using field _OT$base:
                FieldReference invokeBaseOnRole =
                    new FieldReference(IOTConstants._OT_BASE, (((long)sourceStart) << 32) + sourceEnd);
                ReferenceBinding roleClass = roleType.roleModel.getClassPartBinding();
                TypeReference classRef = gen.typeReference(roleClass);
                if (classRef != null) {
                	// field access needs cast to the role-class.
                	// FIXME(SH): use synthetic role field accessor instead!
                	classRef.constant = Constant.NotAConstant;
                	classRef.resolvedType = roleClass;
                	CastExpression unloweredExpr;
                    unloweredExpr = new CastExpression(unloweredExpression, classRef, CastExpression.NEED_CLASS);
                    unloweredExpr.constant     = Constant.NotAConstant;
                    unloweredExpr.resolvedType = roleClass;
					unloweredExpr.bits        |= ASTNode.GenerateCheckcast;
	                invokeBaseOnRole.receiver  = unloweredExpr;
                } else {
                	invokeBaseOnRole.receiver  = unloweredExpression;
                }

                invokeBaseOnRole.actualReceiverType = roleClass;
                invokeBaseOnRole.resolvedType       = roleClass.baseclass();
                invokeBaseOnRole.binding = scope.findField(
                                        			  roleClass,
                                        			  IOTConstants._OT_BASE,
                                        			  invokeBaseOnRole,
                                        			  true);
                invokeBaseOnRole.constant           = Constant.NotAConstant;
               	loweringExpr = invokeBaseOnRole;
            }
		}
        if (needNullCheck) {
        	// ((local = (expression)) == null) ? (RequiredType)local : lower(local));
        	@SuppressWarnings("null") // needNullCheck => localVar != null
			SingleNameReference lhs = gen.singleNameReference(localVar.name);
			Assignment assignment = gen.assignment(lhs, expression);
			loweringExpr = new CheckedLoweringExpression(expression, gen.nullCheck(assignment), gen.nullLiteral(), loweringExpr, localVar);
			if (!deferredResolve) {
				lhs.binding = localVar;
				lhs.resolvedType = unloweredType;
				lhs.bits = Binding.LOCAL|ASTNode.FirstAssignmentToLocal;
				assignment.constant = Constant.NotAConstant;
				loweringExpr.constant = Constant.NotAConstant;
				loweringExpr.resolvedType = requiredType;
			}
        }
        return loweringExpr;
	}
	
	@NonNull LocalVariableBinding makeNewLocal(BlockScope scope, TypeBinding variableType, int sourceStart, int sourceEnd) {
		char[] name = ("_OT$unlowered$"+sourceStart).toCharArray(); //$NON-NLS-1$
		LocalVariableBinding varBinding = new LocalVariableBinding(name, variableType, 0, false);
		varBinding.declaration = new LocalDeclaration(name, sourceStart, sourceEnd); // needed for BlockScope.computeLocalVariablePositions() -> CodeStream.record()
		scope.addLocalVariable(varBinding);
		varBinding.setConstant(Constant.NotAConstant);
		varBinding.useFlag = LocalVariableBinding.USED;
		return varBinding;
	}
	
	/** A conditional expression that checks for null before performing the actual lowering. */
	static class CheckedLoweringExpression extends ConditionalExpression {
		
		private final LocalVariableBinding localVar;
		Expression origExpression;

		CheckedLoweringExpression(Expression origExpression,
								  Expression condition,
								  Expression valueIfTrue,
								  Expression valueIfFalse,
								  LocalVariableBinding localVar)
		{
			super(condition, valueIfTrue, valueIfFalse);
			this.localVar = localVar;
			this.origExpression = origExpression;
		}

		@Override
		public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
			codeStream.addVisibleLocalVariable(this.localVar);
			super.generateCode(currentScope, codeStream, valueRequired);
		}
	}

	public static boolean isLoweringConditional(Expression expr) {
		return expr instanceof CheckedLoweringExpression;
	}

	/**
	 * Extract the actual value, considering wrapping by CheckedLoweringExpression.
	 *
	 * @param expression
	 * @return the actual value to be lowered
	 */
	public static Expression unwrapExpression(Expression expression) {
		if (expression instanceof CheckedLoweringExpression)
			return ((CheckedLoweringExpression)expression).origExpression;
		return null;
	}
}
