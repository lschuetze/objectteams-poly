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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix.CompilationUnitRewriteOperation;
import org.eclipse.jdt.internal.corext.fix.IProposableFix;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.text.correction.ProblemLocation;
import org.eclipse.jdt.internal.ui.text.correction.proposals.FixCorrectionProposal;
import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.objectteams.internal.jdt.nullity.NullCompilerOptions;
import org.eclipse.objectteams.internal.jdt.nullity.IConstants.IProblem;
import org.eclipse.swt.graphics.Image;

import base org.eclipse.jdt.internal.ui.text.correction.QuickFixProcessor;

/**
 * Quickfixes for null-annotation related problems.
 * Hooks into JDT/UI's QuickFixProcessor.
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class QuickFixes {

	protected class Processor playedBy QuickFixProcessor {

		hasCorrections <- replace hasCorrections;

		@SuppressWarnings("basecall")
		callin boolean hasCorrections(ICompilationUnit cu, int problemId) {
			switch (problemId) {
			case IProblem.DefiniteNullFromNonNullMethod:
			case IProblem.PotentialNullFromNonNullMethod:
					return true;
			default:
				return base.hasCorrections(cu, problemId);
			}
		}

		process <- after process;

		/**
		 * Add our proposals to the list assembled by the base class.
		 */
		void process(IInvocationContext context, IProblemLocation problem, @SuppressWarnings("rawtypes") Collection proposals) {
			int id= problem.getProblemId();
			if (id == 0) { // no proposals for none-problem locations
				return;
			}
			switch (id) {
			case IProblem.DefiniteNullFromNonNullMethod:
				addNullableAnnotationProposal(context, problem, proposals);
				break;			
			}			
		}		
	}
		
	@SuppressWarnings("unchecked")
	void addNullableAnnotationProposal(IInvocationContext context, IProblemLocation problem, @SuppressWarnings("rawtypes") Collection proposals) {
		IProposableFix fix= createNullableFix(context.getASTRoot(), problem);
		
		if (fix != null) {
			Image image= JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
			Map<String, String> options= new Hashtable<String, String>();
			options.put(CleanUpConstants.ADD_MISSING_ANNOTATIONS, CleanUpOptions.TRUE);
			options.put(CleanUpConstants.ADD_MISSING_ANNOTATIONS_NULLABLE, CleanUpOptions.TRUE);
			FixCorrectionProposal proposal= new FixCorrectionProposal(fix, new NullAnnotationsCleanUp(options, this), 15, image, context);
			proposals.add(proposal);
		}		
	}

	CompilationUnitRewriteOperationsFix createNullableFix(CompilationUnit compilationUnit, IProblemLocation problem) {
		String nullableAnnotationName = getNullableAnnotationName(compilationUnit.getJavaElement(), false);
		AnnotationRewriteOperation operation = createAddNullableOperation(compilationUnit, problem, nullableAnnotationName);
		if (operation == null)
			return null;
		
		int lastDot = nullableAnnotationName.lastIndexOf('.');
		if (lastDot != -1)
			nullableAnnotationName = nullableAnnotationName.substring(lastDot+1);
		return new CompilationUnitRewriteOperationsFix(Messages.format(FixMessages.Generic_add_missing_annotation, nullableAnnotationName),
														compilationUnit, 
														new CompilationUnitRewriteOperation[] {operation});
	}

	ICleanUpFix createCleanUp(CompilationUnit compilationUnit, boolean addNullableAnnotations, IProblemLocation[] locations) {
		
		ICompilationUnit cu= (ICompilationUnit)compilationUnit.getJavaElement();
		if (!JavaModelUtil.is50OrHigher(cu.getJavaProject()))
			return null;
		
		if (!addNullableAnnotations)
			return null;
		
		List<CompilationUnitRewriteOperation> operations= new ArrayList<CompilationUnitRewriteOperation>();
		
		if (locations == null) {
			org.eclipse.jdt.core.compiler.IProblem[] problems= compilationUnit.getProblems();
			locations= new IProblemLocation[problems.length];
			for (int i= 0; i < problems.length; i++) {
				locations[i]= new ProblemLocation(problems[i]);
			}
		}
		
		createAddNullableAnnotationOperations(compilationUnit, locations, operations);
		
		if (operations.size() == 0)
			return null;
		
		CompilationUnitRewriteOperation[] operationsArray= operations.toArray(new CompilationUnitRewriteOperation[operations.size()]);
		return new CompilationUnitRewriteOperationsFix(FixMessages.QuickFixes_add_annotation_change_name, compilationUnit, operationsArray);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void createAddNullableAnnotationOperations(CompilationUnit compilationUnit, IProblemLocation[] locations, List result) {
		String nullableAnnotationName = getNullableAnnotationName(compilationUnit.getJavaElement(), false);
		for (int i= 0; i < locations.length; i++) {
			IProblemLocation problem= locations[i];
			
			CompilationUnitRewriteOperation fix = createAddNullableOperation(compilationUnit, problem, nullableAnnotationName);
			if (fix != null)
				result.add(fix);
		}
	}
	
	AnnotationRewriteOperation createAddNullableOperation(CompilationUnit compilationUnit,
			IProblemLocation problem, String nullableAnnotationName) 
	{
		ICompilationUnit cu= (ICompilationUnit)compilationUnit.getJavaElement();
		if (!JavaModelUtil.is50OrHigher(cu.getJavaProject()))
			return null;
		
		if (!isMissingNullableAnnotationProblem(problem.getProblemId()))
			return null;
		
		if (hasExplicitNullnessAnnotation(cu, problem.getOffset()))
			return null;
		
		ASTNode selectedNode= problem.getCoveringNode(compilationUnit);
		if (selectedNode == null)
			return null;
			
		ASTNode declaringNode= getDeclaringNode(selectedNode);
		if (!(declaringNode instanceof BodyDeclaration))
			return null;
		
		BodyDeclaration declaration= (BodyDeclaration) declaringNode;
		
		return new AnnotationRewriteOperation(declaration, nullableAnnotationName);
	}

	/** The relevant declaring node of a return statement is the enclosing method. */
	ASTNode getDeclaringNode(ASTNode selectedNode) {
		return ASTNodes.getParent(selectedNode, ASTNode.METHOD_DECLARATION);
	}

	boolean isMissingNullableAnnotationProblem(int id) {
		return id == IProblem.DefiniteNullFromNonNullMethod || id == IProblem.PotentialNullFromNonNullMethod;
	}
	
	boolean hasExplicitNullnessAnnotation(ICompilationUnit compilationUnit, int offset) {
		try {
			IJavaElement problemElement = compilationUnit.getElementAt(offset);
			if (problemElement.getElementType() == IJavaElement.METHOD) {
				IMethod method = (IMethod) problemElement;
				String nullable = getNullableAnnotationName(compilationUnit, true);
				String nonnull = getNonNullAnnotationName(compilationUnit, true);
				for (IAnnotation annotation : method.getAnnotations()) {
					if (   annotation.getElementName().equals(nonnull)
						|| annotation.getElementName().equals(nullable))
						return true;
				}
			}
		} catch (JavaModelException jme) {
			/* nop */
		}
		return false;
	}

	String getNullableAnnotationName(IJavaElement compilationUnit, boolean makeSimple) {
		String qualifiedName = compilationUnit.getJavaProject().getOption(NullCompilerOptions.OPTION_NullableAnnotationName, true);
		int lastDot;
		if (makeSimple && qualifiedName != null && (lastDot = qualifiedName.lastIndexOf('.')) != -1)
			return qualifiedName.substring(lastDot+1);
		return qualifiedName;
	}

	String getNonNullAnnotationName(ICompilationUnit compilationUnit, boolean makeSimple) {
		String qualifiedName = compilationUnit.getJavaProject().getOption(NullCompilerOptions.OPTION_NonNullAnnotationName, true);
		int lastDot;
		if (makeSimple && qualifiedName != null && (lastDot = qualifiedName.lastIndexOf('.')) != -1)
			return qualifiedName.substring(lastDot+1);
		return qualifiedName;
	}
}
