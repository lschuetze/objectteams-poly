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

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
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
		String fAnnotationToAdd;
		String fAnnotationToRemove;
		boolean fAllowRemove;
		CompilationUnit fUnit;
		
		protected String fKey;
		
		/** A globally unique key that identifies the position being annotated (for avoiding double annotations). */
		public String getKey() { return this.fKey; }

		public CompilationUnit getCompilationUnit() {
			return fUnit;
		}
		

		boolean checkExisting(@SuppressWarnings("rawtypes") List existingModifiers, 
							  ListRewrite listRewrite, 
							  TextEditGroup editGroup) 
		{
			for (Object mod : existingModifiers) {
				if (mod instanceof MarkerAnnotation) {
					MarkerAnnotation annotation = (MarkerAnnotation) mod;
					String existingName = annotation.getTypeName().getFullyQualifiedName();
					int lastDot = fAnnotationToRemove.lastIndexOf('.');
					if (   existingName.equals(fAnnotationToRemove)
						|| (lastDot != -1 && fAnnotationToRemove.substring(lastDot+1).equals(existingName)))
					{
						if (!fAllowRemove)
							return false; // veto this change
						listRewrite.remove(annotation, editGroup);
						return true; 
					}
					// paranoia: check if by accident the annotation is already present (shouldn't happen):
					lastDot = fAnnotationToAdd.lastIndexOf('.');
					if (   existingName.equals(fAnnotationToAdd)
						|| (lastDot != -1 && fAnnotationToAdd.substring(lastDot+1).equals(existingName)))
					{
						return false; // already present
					}					
				}
			}
			return true;
		}
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

		public ReturnAnnotationRewriteOperation(CompilationUnit unit,
												MethodDeclaration method,
												String annotationToAdd,
												String annotationToRemove,
												boolean allowRemove)
		{
			fUnit = unit;
			fKey= method.resolveBinding().getKey()+"<return>"; //$NON-NLS-1$
			fBodyDeclaration= method;
			fAnnotationToAdd= annotationToAdd;
			fAnnotationToRemove= annotationToRemove;
			fAllowRemove= allowRemove;
		}

		public void rewriteAST(CompilationUnitRewrite cuRewrite, LinkedProposalModel model) throws CoreException {
			AST ast= cuRewrite.getRoot().getAST();
			ListRewrite listRewrite= cuRewrite.getASTRewrite().getListRewrite(fBodyDeclaration, fBodyDeclaration.getModifiersProperty());
			TextEditGroup group= createTextEditGroup(Messages.format(FixMessages.QuickFixes_declare_method_return_nullable, 
													 BasicElementLabels.getJavaElementName(fAnnotationToAdd)), cuRewrite);
			if (!checkExisting(fBodyDeclaration.modifiers(), listRewrite, group))
				return;
			Annotation newAnnotation= ast.newMarkerAnnotation();
			ImportRewrite importRewrite = cuRewrite.getImportRewrite();
			String resolvableName = importRewrite.addImport(fAnnotationToAdd);
			newAnnotation.setTypeName(ast.newName(resolvableName));
			listRewrite.insertLast(newAnnotation, group); // null annotation is last modifier, directly preceding the return type
		}
	}
	
	static class ParameterAnnotationRewriteOperation extends SignatureAnnotationRewriteOperation {

		private SingleVariableDeclaration fArgument;

		public ParameterAnnotationRewriteOperation(CompilationUnit unit,
												   MethodDeclaration method,
												   String annotationToAdd,
												   String annotationToRemove,
												   String paramName,
												   boolean allowRemove)
		{
			fUnit= unit;
			fKey= method.resolveBinding().getKey();
			fAnnotationToAdd= annotationToAdd;
			fAnnotationToRemove= annotationToRemove;
			fAllowRemove= allowRemove;
			for (Object param : method.parameters()) {
				SingleVariableDeclaration argument = (SingleVariableDeclaration) param;
				if (argument.getName().getIdentifier().equals(paramName)) {
					fArgument= argument;
					fKey += argument.getName().getIdentifier();
					return;
				}
			}
			// shouldn't happen, we've checked that paramName indeed denotes a parameter.
			throw new RuntimeException("Argument "+paramName+" not found in method "+method.getName().getIdentifier()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		public ParameterAnnotationRewriteOperation(CompilationUnit unit,
												   MethodDeclaration method,
												   String annotationToAdd,
												   String annotationToRemove,
												   int paramIdx,
												   boolean allowRemove)
		{
			fUnit= unit;
			fKey= method.resolveBinding().getKey();
			fAnnotationToAdd= annotationToAdd;
			fAnnotationToRemove= annotationToRemove;
			fAllowRemove= allowRemove;
			fArgument = (SingleVariableDeclaration) method.parameters().get(paramIdx);
			fKey += fArgument.getName().getIdentifier();
		}

		public void rewriteAST(CompilationUnitRewrite cuRewrite, LinkedProposalModel linkedModel) throws CoreException {
			AST ast= cuRewrite.getRoot().getAST();
			ListRewrite listRewrite= cuRewrite.getASTRewrite().getListRewrite(fArgument, SingleVariableDeclaration.MODIFIERS2_PROPERTY);
			TextEditGroup group= createTextEditGroup(Messages.format(FixMessages.QuickFixes_declare_method_parameter_nullable, 
													 BasicElementLabels.getJavaElementName(fAnnotationToAdd)), cuRewrite);
			if (!checkExisting(fArgument.modifiers(), listRewrite, group))
				return;
			Annotation newAnnotation= ast.newMarkerAnnotation();
			ImportRewrite importRewrite = cuRewrite.getImportRewrite();
			String resolvableName = importRewrite.addImport(fAnnotationToAdd);
			newAnnotation.setTypeName(ast.newName(resolvableName));
			listRewrite.insertLast(newAnnotation, group); // null annotation is last modifier, directly preceding the return type
		}
	}
}
