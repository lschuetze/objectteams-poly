/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2007, 2018 Technical University Berlin, Germany and others.
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
 * 	   Technical University Berlin - Initial API and implementation
 *     IBM Corporation - implementation of individual method bodies
 **********************************************************************/
team package org.eclipse.objectteams.otdt.internal.ui.assist.CompletionAdaptor;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite.ImportRewriteContext;

/**
 * Add new functions to StubUtility2, accessing otherwise invisible helper functions.
 * @author stephan
 * @since 1.1.2
 */
@SuppressWarnings("decapsulation") // base class is final
protected class StubUtility2 playedBy StubUtility2
{

	// CALLOUT INTERFACE:
	@SuppressWarnings("restriction") // internal type CodeGenerationSettings
	MethodDeclaration createImplementationStub(ICompilationUnit unit, ASTRewrite rewrite, ImportRewrite imports, ImportRewriteContext context,
			IMethodBinding binding, ITypeBinding targetType, CodeGenerationSettings settings, boolean inInterface, ASTNode astNode)
		-> MethodDeclaration createImplementationStub(ICompilationUnit unit, ASTRewrite rewrite, ImportRewrite imports, ImportRewriteContext context,
			IMethodBinding binding, ITypeBinding targetType, CodeGenerationSettings settings, boolean inInterface, ASTNode astNode);
}

