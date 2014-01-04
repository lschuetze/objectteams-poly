/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2010 Fraunhofer Gesellschaft, Munich, Germany,
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

package org.eclipse.objectteams.otdt.internal.codeassist;

import org.eclipse.jdt.internal.codeassist.select.SelectionNodeFound;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.TSuperMessageSend;

/**
 * @author haebor
 */
public class SelectionOnTSuperMessageSend extends TSuperMessageSend
{
    @Override
    protected void findMethodBinding(BlockScope scope, TypeBinding[] argumentTypes, boolean polyExpressionSeen) {
    	super.findMethodBinding(scope, argumentTypes, polyExpressionSeen);
    	throw new SelectionNodeFound(this.binding);
    }
}
