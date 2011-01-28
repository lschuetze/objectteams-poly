/*******************************************************************************
 * Copyright (c) 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.objectteams.internal.jdt.nullity.quickfix;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix.CompilationUnitRewriteOperation;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalModel;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.text.edits.TextEditGroup;

/**
 * Rewrite operation that inserts an annotation into a method signature.
 * 
 * Crafted after the lead of Java50Fix.AnnotationRewriteOperation
 * @author stephan
 *
 */
@SuppressWarnings("restriction")
class AnnotationRewriteOperation extends CompilationUnitRewriteOperation {

	private final BodyDeclaration fBodyDeclaration;
	private final String fAnnotation;

	public AnnotationRewriteOperation(BodyDeclaration bodyDeclaration, String annotation) {
		fBodyDeclaration= bodyDeclaration;
		fAnnotation= annotation;
	}

	/**
	 * {@inheritDoc}
	 */
	public void rewriteAST(CompilationUnitRewrite cuRewrite, LinkedProposalModel model) throws CoreException {
		AST ast= cuRewrite.getRoot().getAST();
		ListRewrite listRewrite= cuRewrite.getASTRewrite().getListRewrite(fBodyDeclaration, fBodyDeclaration.getModifiersProperty());
		Annotation newAnnotation= ast.newMarkerAnnotation();
		ImportRewrite importRewrite = cuRewrite.getImportRewrite();
		String resolvableName = importRewrite.addImport(fAnnotation);
		newAnnotation.setTypeName(ast.newName(resolvableName));
		TextEditGroup group= createTextEditGroup(Messages.format(FixMessages.Generic_add_missing_annotation, 
												 BasicElementLabels.getJavaElementName(fAnnotation)), cuRewrite);
		listRewrite.insertLast(newAnnotation, group); // null annotation is last modifier, directly preceding the return type
	}
}