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

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;

import base org.eclipse.jdt.internal.compiler.ast.FakedTrackingVariable;

/**
 * Fassade to a class that was introduced in JDT/Core 3.8M3.
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class Compatibility {

	protected class FakedTrackingVariable implements IFakedTrackingVariable playedBy FakedTrackingVariable {
		FakedTrackingVariable getCloseTrackingVariable(Expression expression)
		-> FakedTrackingVariable getCloseTrackingVariable(Expression expression);

		@SuppressWarnings("decapsulation")
		protected MethodScope methodScope() -> get MethodScope methodScope;

		void markClosedInNestedMethod() -> void markClosedInNestedMethod();
	}

	public IFakedTrackingVariable getCloseTrackingVariable(Expression expression) {
		return FakedTrackingVariable.getCloseTrackingVariable(expression);
	}

	// delegate to new method in BlockScope:
	public void removeTrackingVar(BlockScope currentScope, IFakedTrackingVariable trackingVariable) {
		currentScope.removeTrackingVar((FakedTrackingVariable)trackingVariable);
	}
	
}
