/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2003, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id: StackTransformStatementsVisitor.java 23416 2010-02-03 19:59:31Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.transformer;

import java.util.HashMap;
import java.util.Stack;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.objectteams.otdt.core.exceptions.InternalCompilerError;

/**
 * This class provides stack based management of expressiones/statements to
 * be replaced while traversing an AST.
 * @author stephan
 */
public class StackTransformStatementsVisitor
    extends AbstractTransformStatementsVisitor
{

    private Stack <Statement> replacements = new Stack<Statement>();
    private Stack <Statement> toReplace    = new Stack<Statement>();

    // Record for each AstNode with replacement at what stack size
    // the replacement lies. (For sanity check only)
    private HashMap <Statement,Integer> stackSizes  = new HashMap<Statement,Integer>();

    /**
     * Verify that all replacements entered for and below node have been consumed by now.
     * @param node the statement/expression .. for which treatment is finished
     */
    @Override
	protected void assertAllConsumed(ASTNode node)
    {
        Integer stackSize = this.stackSizes.get(node);
        if (stackSize == null)
            return; // nothing entered
        if (this.replacements.size() != stackSize.intValue())
            throw new InternalCompilerError("Unbalanced replacement stack."); //$NON-NLS-1$
        this.stackSizes.remove(node);
    }

    @Override
	protected void enterExpression(Expression oldExpr, Expression newExpr, Statement node)
    {
        this.toReplace.push(oldExpr);
        this.replacements.push(newExpr);
        this.stackSizes.put(node, new Integer(this.replacements.size()));
    }

    @Override
	public Expression checkConsumeExpression(Expression oldExpr, BlockScope scope)
    {
        if (!this.toReplace.isEmpty())
        {
            Object old = this.toReplace.peek();
            if (old == oldExpr)
            {
                this.toReplace.pop();
                return (Expression)this.replacements.pop();
            }
        }
        return oldExpr;
    }
    @Override
	protected Statement checkConsumeStatement(Statement oldStat, BlockScope scope)
    {
        if (!this.toReplace.isEmpty())
        {
            Object old = this.toReplace.peek();
            if (old == oldStat)
            {
                this.toReplace.pop();
                return this.replacements.pop();
            }
        }
        return oldStat;
    }

}
