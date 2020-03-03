/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009 Stephan Herrmann.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * Stephan Herrmann
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.refactoring.corext.rename;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import base org.eclipse.jdt.internal.corext.dom.LinkedNodeFinder.BindingFinder;

/**
 * Help the refactoring implementation to compare role bindings by
 * uniformly using the interface part.
 * 
 * @author stephan
 * @since 1.3.1
 */
@SuppressWarnings({ "restriction", "decapsulation" }) // base class BindingFinder
public team class RolePartsUnifier {
        protected class BindingFinder playedBy BindingFinder {

                IBinding getDeclaration(IBinding binding) <- replace IBinding getDeclaration(IBinding binding);

                static callin IBinding getDeclaration(IBinding binding) {
                        IBinding decl = base.getDeclaration(binding);
                        if (decl instanceof ITypeBinding) {
                                ITypeBinding typeBinding = (ITypeBinding)decl;
                                if (typeBinding.isClass() && typeBinding.isRole()) {
                                        ITypeBinding ifcPart = typeBinding.getIfcPart();
                                        if (ifcPart != null)
                                                return ifcPart;
                                }
                        }
                        return decl;
                }               
        }
}
