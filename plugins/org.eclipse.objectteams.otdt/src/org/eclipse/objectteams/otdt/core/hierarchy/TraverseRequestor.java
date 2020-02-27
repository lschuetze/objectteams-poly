/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
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
 * $Id: TraverseRequestor.java 23416 2010-02-03 19:59:31Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.hierarchy;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.objectteams.otdt.core.ICallinMapping;
import org.eclipse.objectteams.otdt.core.ICalloutMapping;
import org.eclipse.objectteams.otdt.core.ICalloutToFieldMapping;

/**
 * @author svacina
 */
abstract class TraverseRequestor
{

	static class HierarchyContext {
		boolean isFocusType;
		boolean isExplicitSuperclass;
		boolean isBehindExplicitInheritance;
		HierarchyContext(boolean isFocusType, boolean isExplicitSuperclass, boolean isBehindExplicitInheritance) {
			super();
			this.isFocusType = isFocusType;
			this.isExplicitSuperclass = isExplicitSuperclass;
			this.isBehindExplicitInheritance = isBehindExplicitInheritance;
		}
	}
	
	protected IType _focusType = null;
	
	
	void report(IType type, HierarchyContext context) { /* default: empty */ }
	
	void report(IMethod method, HierarchyContext context) { /* default: empty */ }
	
	void report(ICallinMapping callinMapping, HierarchyContext context) { /* default: empty */ }
	
	void report(ICalloutMapping calloutMapping, HierarchyContext context) { /* default: empty */ }
	
	void report(ICalloutToFieldMapping calloutToFieldMapping, HierarchyContext context) { /* default: empty */ }

	void report(IField field, HierarchyContext context) { /* default: empty */ }
	
	IType getFocusType()
	{
		return _focusType;		
	}
}
