/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 GK Software AG
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.core;

import java.lang.reflect.Method;

import org.eclipse.jdt.core.dom.BaseCallMessageSend;
import org.eclipse.jdt.core.dom.BaseConstructorInvocation;
import org.eclipse.jdt.core.dom.CallinMappingDeclaration;
import org.eclipse.jdt.core.dom.CalloutMappingDeclaration;
import org.eclipse.jdt.core.dom.FieldAccessSpec;
import org.eclipse.jdt.core.dom.GuardPredicateDeclaration;
import org.eclipse.jdt.core.dom.LiftingType;
import org.eclipse.jdt.core.dom.MethodBindingOperator;
import org.eclipse.jdt.core.dom.MethodSpec;
import org.eclipse.jdt.core.dom.ParameterMapping;
import org.eclipse.jdt.core.dom.PrecedenceDeclaration;
import org.eclipse.jdt.core.dom.RoleTypeDeclaration;
import org.eclipse.jdt.core.dom.TSuperConstructorInvocation;
import org.eclipse.jdt.core.dom.TSuperMessageSend;
import org.eclipse.jdt.core.dom.TypeAnchor;
import org.eclipse.jdt.core.dom.WithinStatement;

import base org.eclipse.jdt.ui.tests.core.HierarchicalASTVisitorTest;

/**
 * This team excludes OT-specific AST nodes from HierarchicalASTVisitorTest
 * because the HierarchicalASTVisitor is not OT-aware. 
 */
public team class HierarchicalASTVisiting {

	protected class Tests playedBy HierarchicalASTVisitorTest {

		@SuppressWarnings("decapsulation")
		boolean isVisitMethod(Method method) <- replace boolean isVisitMethod(Method method);

		static callin boolean isVisitMethod(Method method) {
			return base.isVisitMethod(method) && !isOTNode(method.getParameterTypes()[0]);
		}
		
		
		private static boolean isOTNode(Class nodeClass) {
			Class[] otNodeClasses = new Class[] {
					MethodSpec.class,
					FieldAccessSpec.class,
					CallinMappingDeclaration.class,
					CalloutMappingDeclaration.class,
					MethodBindingOperator.class,
					ParameterMapping.class,
					RoleTypeDeclaration.class,
					LiftingType.class,
					TypeAnchor.class,
					WithinStatement.class,
					TSuperMessageSend.class,
					TSuperConstructorInvocation.class,
					BaseConstructorInvocation.class,
					BaseCallMessageSend.class,
					PrecedenceDeclaration.class,
					GuardPredicateDeclaration.class
			};
			for (int i = 0; i < otNodeClasses.length; i++) 
				if (otNodeClasses[i] == nodeClass)
					return true;
			return false;
		}
	}

}
