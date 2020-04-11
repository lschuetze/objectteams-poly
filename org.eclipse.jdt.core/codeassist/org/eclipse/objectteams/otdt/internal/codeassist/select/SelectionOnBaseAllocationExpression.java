/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2011 GK Software AG
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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.codeassist.select;

import org.eclipse.jdt.internal.codeassist.select.SelectionNodeFound;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.BaseAllocationExpression;

/**
 * For code select, a base allocation expression should be mapped to the corresponding
 * base constructor, which is found via this.expression (an AllocationExpression).
 *
 * @author stephan
 * @since 2.0 (from eclipse.org).
 */
public class SelectionOnBaseAllocationExpression extends BaseAllocationExpression {

	public SelectionOnBaseAllocationExpression(int start, int end) {
		super(start, end);
	}

	@Override
	public TypeBinding resolveType(BlockScope scope) {
		super.resolveType(scope);
		if (this.expression instanceof AllocationExpression)
			throw new SelectionNodeFound(((AllocationExpression)this.expression).binding);
		else
			throw new SelectionNodeFound(); // see super method on how we can get here.
	}
}
