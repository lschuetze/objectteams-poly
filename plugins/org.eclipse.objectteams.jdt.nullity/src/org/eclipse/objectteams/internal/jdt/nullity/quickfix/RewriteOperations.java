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
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix.CompilationUnitRewriteOperation;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalModel;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.text.edits.TextEditGroup;

@SuppressWarnings("restriction")
public class RewriteOperations {

	static abstract class SignatureAnnotationRewriteOperation extends CompilationUnitRewriteOperation {
		String fAnnotation;
		
		protected String fKey;
		
		/** A globally unique key that identifies the position being annotated (for avoiding double annotations). */
		public String getKey() { return this.fKey; }
	}
	
	/**
	 * Rewrite operation that inserts an annotation into a method signature.
	 * 
	 * Crafted after the lead of Java50Fix.AnnotationRewriteOperation
	 * @author stephan
	 *
	 */
	static class ReturnAnnotationRewriteOperation extends SignatureAnnotationRewriteOperation {

		private final BodyDeclaration fBodyDeclaration;

		public ReturnAnnotationRewriteOperation(MethodDeclaration method, String annotation) {
			fKey= method.resolveBinding().getKey()+"<return>"; //$NON-NLS-1$
			fBodyDeclaration= method;
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
	
	static class ParameterAnnotationRewriteOperation extends SignatureAnnotationRewriteOperation {

		private SingleVariableDeclaration fArgument;

		public ParameterAnnotationRewriteOperation(MethodDeclaration method, String annotation, String paramName) {
			fKey = method.resolveBinding().getKey();
			fAnnotation= annotation;
			for (Object param : method.parameters()) {
				SingleVariableDeclaration argument = (SingleVariableDeclaration) param;
				if (argument.getName().getIdentifier().equals(paramName)) {
					fArgument = argument;
					fKey += argument.getName().getIdentifier();
					return;
				}
			}
			// shouldn't happen, we've checked that paramName indeed denotes a parameter.
			throw new RuntimeException("Argument "+paramName+" not found in method "+method.getName().getIdentifier()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
		public void rewriteAST(CompilationUnitRewrite cuRewrite, LinkedProposalModel linkedModel) throws CoreException {
			AST ast= cuRewrite.getRoot().getAST();
			ListRewrite listRewrite= cuRewrite.getASTRewrite().getListRewrite(fArgument, SingleVariableDeclaration.MODIFIERS2_PROPERTY);
			Annotation newAnnotation= ast.newMarkerAnnotation();
			ImportRewrite importRewrite = cuRewrite.getImportRewrite();
			String resolvableName = importRewrite.addImport(fAnnotation);
			newAnnotation.setTypeName(ast.newName(resolvableName));
			TextEditGroup group= createTextEditGroup(Messages.format(FixMessages.Generic_add_missing_annotation, 
													 BasicElementLabels.getJavaElementName(fAnnotation)), cuRewrite);
			listRewrite.insertLast(newAnnotation, group); // null annotation is last modifier, directly preceding the return type
		}
	}
}
