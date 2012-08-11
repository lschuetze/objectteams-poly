/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2012 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.refactoring.adaptor.pullup;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite.ImportRewriteContext;
import org.eclipse.jdt.internal.corext.codemanipulation.ContextSensitiveImportRewriteContext;
import org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.jdt.internal.corext.refactoring.structure.MemberVisibilityAdjustor.IncomingMemberVisibilityAdjustment;
import org.eclipse.jdt.internal.corext.refactoring.structure.TypeVariableMaplet;
import org.eclipse.jdt.internal.corext.refactoring.structure.TypeVariableUtil;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

/**
 * Data class to mediate data flows between callins in team PullUpAdaptor.
 */
@SuppressWarnings("restriction")
class ChangeManagerDetails {

	RefactoringStatus status;
	CompilationUnitRewrite rewrite;
	AbstractTypeDeclaration declaration;
	ImportRewriteContext context;
	CompilationUnitRewrite sourceRewrite;
	TypeVariableMaplet[] mapping;
	Map<IMember, IncomingMemberVisibilityAdjustment> adjustments;
	IProgressMonitor monitor;

	public ChangeManagerDetails(CompilationUnitRewrite sourceRewrite, CompilationUnitRewrite rewrite, IType declaringType, IType destinationType, RefactoringStatus status, IProgressMonitor monitor) throws JavaModelException {
		this.rewrite = rewrite;
		this.sourceRewrite = sourceRewrite;
		this.declaration = ASTNodeSearchUtil.getAbstractTypeDeclarationNode(destinationType, rewrite.getRoot());
		this.context= new ContextSensitiveImportRewriteContext(declaration, rewrite.getImportRewrite());
		this.mapping= TypeVariableUtil.subTypeToSuperType(declaringType, destinationType);
		this.status = status;
		this.monitor = monitor;
	}
}