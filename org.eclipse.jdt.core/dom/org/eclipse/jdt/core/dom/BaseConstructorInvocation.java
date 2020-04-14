/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2020 Fraunhofer Gesellschaft, Munich, Germany,
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
 * $Id: BaseConstructorInvocation.java 23416 2010-02-03 19:59:31Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.jdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * NEW for OTDT
 *
 * BaseConstructorMessageSend represents a 'base call' to a constructor of the
 * role's base class (OTJLD §2.4.2),
 * e.g. <code>base();</code> or <code>base(arg1, arg2);</code>.
 *
 * Contained AST elements:
 * a list of argument expressions (<code>Expression</code>).
 *
 * Locations in source code:
 * This node can only be used within role constructor bodies, i.e. it appears in
 * ordinary <code>ExpressionStatement</code>s only.
 *
 * Contrary to org.eclipse.jdt.internal.compiler.ast.BaseConstructorMessageSend
 * this is not an assignment. In the compiler ast
 * "base(args)" is translated to "_OT$base = new BaseClass(args);".
 * In the dom ast we just need "base(args);".
 *
 * @author mkr
 * @version $Id: BaseConstructorInvocation.java 23416 2010-02-03 19:59:31Z stephan $
 */
public class BaseConstructorInvocation extends Expression implements Invocation
{
    /**
     * The "arguments" structural property of this node type.
     * @since 3.0
     */
    public static final ChildListPropertyDescriptor ARGUMENTS_PROPERTY =
        new ChildListPropertyDescriptor(BaseConstructorInvocation.class, "arguments", Expression.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;

	static
	{
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(2);
		createPropertyList(BaseConstructorInvocation.class, propertyList);
        addProperty(ARGUMENTS_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS&ast;</code> constants
	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 */
	public static List<StructuralPropertyDescriptor> propertyDescriptors(int apiLevel)
	{
		return PROPERTY_DESCRIPTORS;
	}

    /**
     * The list of argument expressions (element type:
     * <code>Expression</code>). Defaults to an empty list.
     */
    private ASTNode.NodeList arguments =
        new ASTNode.NodeList(ARGUMENTS_PROPERTY);

	/**
	 * Creates a new AST node for a base expression owned by the given AST.
	 * By default, there is no qualifier.
	 *
	 * @param ast the AST that is to own this node
	 */
	BaseConstructorInvocation(AST ast)
	{
		super(ast);
	}

    /**
     * Returns the live ordered list of argument expressions in this
     * base constructor invocation expression.
     *
     * @return the live list of argument expressions
     *    (element type: <code>Expression</code>)
     */
    @Override
	public List getArguments()
    {
        return this.arguments;
    }

    @Override
	public ChildListPropertyDescriptor getArgumentsProperty() {
    	return ARGUMENTS_PROPERTY;
    }

	@Override
	final List internalStructuralPropertiesForType(int apiLevel)
	{
		return propertyDescriptors(apiLevel);
	}

    @Override
	final List internalGetChildListProperty(ChildListPropertyDescriptor property)
    {
        if (property == ARGUMENTS_PROPERTY)
        {
            return getArguments();
        }

        // allow default implementation to flag the error
        return super.internalGetChildListProperty(property);
    }

	@Override
	final int getNodeType0()
	{
		return BASE_CONSTRUCTOR_INVOCATION;
	}

	@Override
	@SuppressWarnings("unchecked")
	ASTNode clone0(AST target)
	{
		BaseConstructorInvocation result = new BaseConstructorInvocation(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
        result.getArguments().addAll(ASTNode.copySubtrees(target, getArguments()));

		return result;
	}

	@Override
	final boolean subtreeMatch0(ASTMatcher matcher, Object other)
	{
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

    @Override
	void accept0(ASTVisitor visitor)
    {
        boolean visitChildren = visitor.visit(this);

        if (visitChildren)
        {
            // visit children in normal left to right reading order
            acceptChildren(visitor, this.arguments);
        }
        visitor.endVisit(this);
    }


	@Override
	int memSize()
	{
		// treat Operator as free
		return BASE_NODE_SIZE + 1 * 4;
	}

	@Override
	int treeSize()
	{
		return memSize() + (this.arguments == null
		                        ? 0
                                : this.arguments.listSize());
	}

    public IMethodBinding resolveConstructorBinding() {
        return this.ast.getBindingResolver().resolveConstructor(this);
    }

	@Override
	public IMethodBinding resolveMethodBinding() {
		return resolveConstructorBinding();
	}
}
