/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2006, 2010 Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: DOMAdaptor.java 23438 2010-02-04 20:05:24Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.corext;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CalloutMappingDeclaration;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.LiftingType;
import org.eclipse.jdt.core.dom.RoleTypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import base org.eclipse.jdt.core.dom.VariableBinding;
import base org.eclipse.jdt.internal.corext.dom.LocalVariableIndex;
import base org.eclipse.jdt.internal.corext.dom.ASTFlattener;
import base org.eclipse.jdt.internal.corext.dom.ModifierRewrite;

/**
 * This team adapts classes from org.eclipse.jdt.internal.corext.dom.
 * 
 * Issues:
 * - hard coded switch missing OT-cases.
 * - visitors unaware of OT-syntax, specifically: declared lifting.
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class DOMAdaptor 
{
	
	protected class ModifierRewrite playedBy ModifierRewrite 
	{
		@SuppressWarnings("decapsulation")
		evaluateListRewrite <- replace evaluateListRewrite;
		
		/** Add missing switch case. */
		@SuppressWarnings("basecall")
		callin ListRewrite evaluateListRewrite(ASTRewrite rewrite, ASTNode declNode) 
		{
			switch (declNode.getNodeType()) {
			case ASTNode.ROLE_TYPE_DECLARATION:
				return rewrite.getListRewrite(declNode, RoleTypeDeclaration.MODIFIERS2_PROPERTY);
			case ASTNode.CALLOUT_MAPPING_DECLARATION:
				return rewrite.getListRewrite(declNode, CalloutMappingDeclaration.MODIFIERS2_PROPERTY);
			}
			return base.evaluateListRewrite(rewrite, declNode);
		}
	}
	
	/** 
	 * Make this visitor aware of declared lifting.
	 * 
	 * TODO: other OT syntax? It seems, this flattener is, e.g, NOT used for classes
	 *       so playedBy etc. needn't be handled. 
	 */
	protected class ASTFlattener playedBy ASTFlattener 
	{
		@SuppressWarnings("decapsulation")
		StringBuffer getFBuffer() -> get StringBuffer fBuffer;

		boolean visit(LiftingType node) <- replace boolean visit(LiftingType node);

		@SuppressWarnings("basecall")
		callin boolean visit(LiftingType node) {
			node.getBaseType().accept(this);
			getFBuffer().append(" as "); //$NON-NLS-1$
			node.getRoleType().accept(this);
			return false;
		}
	}
	
	/** Make this visitor aware of declared lifting. */
	protected team class LocalVariableIndex playedBy LocalVariableIndex 
	{
		/** gateway to package private class (rather than exposing new method via the API interface) */
		protected class VariableBinding playedBy VariableBinding {
			int getVariableIdMax() -> int getVariableIdMax();
		}
		
		@SuppressWarnings("decapsulation") int getFTopIndex() -> get int fTopIndex;
		@SuppressWarnings("decapsulation") void setFTopIndex(int fTopIndex) -> set int fTopIndex;

		@SuppressWarnings("decapsulation")
		void handleVariableBinding(VariableBinding variableBinding) <- replace void handleVariableBinding(IVariableBinding binding)
			base when (binding instanceof VariableBinding)
			with { variableBinding <- (VariableBinding)binding }

		/** An argument with declared lifting has two IDs, use the max of both. */
		@SuppressWarnings("basecall")
		callin void handleVariableBinding(VariableBinding binding) {
			setFTopIndex(Math.max(getFTopIndex(), binding.getVariableIdMax()));
		}
	}
}
