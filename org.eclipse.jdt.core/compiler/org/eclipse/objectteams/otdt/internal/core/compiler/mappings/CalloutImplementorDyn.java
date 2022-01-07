/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2011, 2015 GK Software AG and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.compiler.mappings;

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.parser.TerminalTokens;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.CalloutMappingDeclaration;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.FieldAccessSpec;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.MethodSpec;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.PotentialLowerExpression;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.RoleModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.TeamModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.AstGenerator;

/**
 * This class only contains those parts of callout generation that for
 * dynamic weaving deviated from the normal strategy.
 *
 * @author stephan
 */
public class CalloutImplementorDyn {

	// wrapper methods for decapsulating callout
	public static final char[] OT_ACCESS = "_OT$access".toCharArray(); //$NON-NLS-1$
	public static final char[] OT_ACCESS_STATIC = "_OT$accessStatic".toCharArray(); //$NON-NLS-1$


	public static Expression baseAccessExpression(Scope scope, RoleModel roleModel, ReferenceBinding baseType,
												  Expression receiver, CalloutMappingDeclaration calloutDecl, Expression[] arguments,
												  AstGenerator gen)
	{
		MethodSpec baseSpec = calloutDecl.baseMethodSpec;
		char[] selector = ensureAccessor(scope, baseType, baseSpec.isStatic()).selector;
		TeamModel teamModel = roleModel.getTeamModel();
		TeamModel.UpdatableIntLiteral accessIdArg = gen.updatableIntLiteral(baseSpec.accessId);
		teamModel.recordUpdatableAccessId(accessIdArg); // may need updating before codeGen.
		for (int i = 0; i < arguments.length; i++) {
			Expression arg = arguments[i];
			if (arg instanceof PotentialLowerExpression) {
				TypeBinding type = ((PotentialLowerExpression) arg).expectedType;
				if (type instanceof BaseTypeBinding)
					arguments[i] = gen.createBoxing(arg, (BaseTypeBinding) type);
			}
		}
		int opKind = 0;
		if (baseSpec instanceof FieldAccessSpec)
			if (((FieldAccessSpec) baseSpec).calloutModifier == TerminalTokens.TokenNameset)
				opKind = 1;
		Expression opKindArg = gen.intLiteral(opKind);
		Expression packagedArgs = gen.arrayAllocation(gen.qualifiedTypeReference(TypeConstants.JAVA_LANG_OBJECT), 1, arguments);
		Expression callerArg = gen.qualifiedThisReference(gen.typeReference(teamModel.getBinding()));
		MessageSend messageSend = gen.messageSend(receiver, selector, new Expression[] {accessIdArg, opKindArg, packagedArgs, callerArg});
		if (baseSpec.resolvedType() == TypeBinding.VOID || opKind == 1)
			return messageSend;
		TypeBinding expectedType = calloutDecl.mappings != null || calloutDecl.roleMethodSpec.returnNeedsTranslation
				? baseSpec.resolvedType()
				: calloutDecl.roleMethodSpec.resolvedType(); // shortcut to avoid casting to an intermediate base type
		return gen.createCastOrUnboxing(messageSend, expectedType, true/*baseAccess*/);
	}

	public static MethodBinding ensureAccessor(Scope scope, ReferenceBinding baseType, boolean isStatic) {
		if (baseType.isRoleType())
			baseType = baseType.getRealClass();
		char[] selector = isStatic ? OT_ACCESS_STATIC : OT_ACCESS;
		MethodBinding[] methods = baseType.getMethods(selector);
		if (methods != null && methods.length == 1) {
			return methods[0];
		} else {
			int modifiers = ClassFileConstants.AccPublic|ClassFileConstants.AccSynthetic;
			if (isStatic)
				modifiers |= ClassFileConstants.AccStatic;
			MethodBinding method = new MethodBinding(
						modifiers,
						selector,
						scope.getJavaLangObject(),
						new TypeBinding[] {
							TypeBinding.INT, TypeBinding.INT,
							scope.environment().createArrayType(scope.getJavaLangObject(), 1),
							scope.getOrgObjectteamsITeam()
						},
						Binding.NO_EXCEPTIONS,
						baseType);
			baseType.addMethod(method);
			return method;
		}
	}
}
