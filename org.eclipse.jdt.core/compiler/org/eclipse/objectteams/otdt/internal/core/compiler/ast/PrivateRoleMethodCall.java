/** 
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors: 
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.compiler.ast;

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.ITeamAnchor;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.RoleTypeBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.SyntheticRoleBridgeMethodBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.AstGenerator;


/**
 * A message send to a private role method requiring redirection via a pair of bridge methods.
 * Directly hooks into generateCode to achieve the necessary modifications.
 * 
 * (Before 3.7/0.8 this was an anonymous class inside CalloutImplementor).
 * @author stephan
 * @since 3.7
 */
public class PrivateRoleMethodCall extends MessageSend {
	private boolean isCalloutToField;
	private AstGenerator gen;
	
	public PrivateRoleMethodCall(Expression receiver, char[] selector, Expression[] arguments, boolean isCalloutToField,
								 Scope scope, ReferenceBinding targetClass, MethodBinding targetMethod, AstGenerator gen)
	{
		super();
		this.syntheticAccessor = SyntheticRoleBridgeMethodBinding.findOuterAccessor(scope, targetClass, targetMethod);
		this.receiver = receiver;
		this.selector = selector;
		this.arguments = arguments;
		this.sourceStart = gen.sourceStart;
		this.sourceEnd = gen.sourceEnd;
		this.isCalloutToField = isCalloutToField;
		this.gen = gen;
		this.constant = Constant.NotAConstant;
	}

	@Override
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
		// manually redirect to synth bridge:
		Expression receiverReference;
		boolean isCallinAccess = false;
		if (RoleTypeBinding.isRoleWithExplicitAnchor(this.actualReceiverType)) {
			// new receiver is the anchor denoting the base role's enclosing team instance:
			ITeamAnchor teamAnchor = ((RoleTypeBinding)this.actualReceiverType)._teamAnchor;
			TypeAnchorReference syntheticReceiver = this.gen.typeAnchorReference(teamAnchor);
			syntheticReceiver.isExpression = true;
			receiverReference = syntheticReceiver;			
		} else {
			isCallinAccess = true;
			// call from inside a otre-dyn callin wrapper: receiver is the current team:
			receiverReference = this.gen.thisReference();
		}
		receiverReference.resolve(currentScope);
		if (this.isCalloutToField)
			// for c-t-f this receiver *replaces* the original receiver,
			// role instance additionally exists as a visible method argument
			this.receiver = receiverReference;
		else
			// for method callout or callin to private *add* the team instance to the front of pushes
			// original role instance receiver will become the first implicit argument
			receiverReference.generateCode(currentScope, codeStream, true/*valueRequired*/);
		
		if (isCallinAccess) {
			// might need more synthetic args:
			if (this.binding.isStatic()) {
				codeStream.aconst_null();	// first arg in role bridge: (null) role
				codeStream.iconst_0(); 		// enclosingTeamInstance: dummy value 
				codeStream.aload_0();		// enclosingTeamInstance: team instance
			}
		}
		// directly use the accessor and its declaring class for the invoke instruction:
		this.binding = this.syntheticAccessor;
		this.actualReceiverType = this.syntheticAccessor.declaringClass;
		this.syntheticAccessor = null;
		super.generateCode(currentScope, codeStream, valueRequired);
	}

}
