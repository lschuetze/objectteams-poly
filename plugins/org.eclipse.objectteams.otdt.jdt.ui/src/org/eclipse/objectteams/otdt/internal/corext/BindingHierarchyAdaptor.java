/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2006, 2020 Technical University Berlin, Germany.
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
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.corext;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import base org.eclipse.jdt.internal.corext.dom.Bindings;
/**
 * This team adapts the override markers in order to filter out the generated
 * relation between a class part and its interface part.
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class BindingHierarchyAdaptor 
{
	protected class OverrideMarkerAdaptor playedBy Bindings
	{
		findOverriddenMethodInHierarchy <- replace findOverriddenMethodInHierarchy;
		@SuppressWarnings("basecall")
		static callin IMethodBinding findOverriddenMethodInHierarchy(ITypeBinding type, IMethodBinding binding)
		{
			if (binding.isCopied())
				return null; // copied method (synth) is never considered as overriding
			ITypeBinding declaringClass = binding.getDeclaringClass();
			if (declaringClass.isClassPartOf(type)) {
				// if type is the interfacepart of declaringClass
				// the real superInterfaces are in type.getInterfaces():
				for (ITypeBinding superIfc : type.getInterfaces()) {
					IMethodBinding method = base.findOverriddenMethodInHierarchy(superIfc, binding);
					if (method != null)
						return method;
				}
				return null; 
			}
			if (declaringClass.isRole()) {
				IMethodBinding[] tsupers = binding.getImplicitlyOverridden();
				if (tsupers != null && tsupers.length > 0)
					return tsupers[0];
			}
			return base.findOverriddenMethodInHierarchy(type, binding);
		}
	}
}
