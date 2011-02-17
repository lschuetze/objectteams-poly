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

import static org.eclipse.objectteams.internal.jdt.nullity.IConstants.IProblem.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix.CompilationUnitRewriteOperation;
import org.eclipse.jdt.internal.corext.fix.IProposableFix;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.text.correction.ASTResolving;
import org.eclipse.jdt.internal.ui.text.correction.ProblemLocation;
import org.eclipse.jdt.internal.ui.text.correction.proposals.FixCorrectionProposal;
import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.objectteams.internal.jdt.nullity.NullCompilerOptions;
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
			case DefiniteNullFromNonNullMethod:
			case PotentialNullFromNonNullMethod:
			case DefiniteNullToNonNullParameter:
			case PotentialNullToNonNullParameter:
			case IProblem.NonNullLocalVariableComparisonYieldsFalse:
			case IProblem.RedundantNullCheckOnNonNullLocalVariable:
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
			case DefiniteNullFromNonNullMethod:
			case PotentialNullFromNonNullMethod:
			case DefiniteNullToNonNullParameter:
			case PotentialNullToNonNullParameter:
				addNullableAnnotationInSignatureProposal(context, problem, proposals);
				break;			
			case IProblem.NonNullLocalVariableComparisonYieldsFalse:
			case IProblem.RedundantNullCheckOnNonNullLocalVariable:
				if (isComplainingAboutArgument(context, problem))
					addNullableAnnotationInSignatureProposal(context, problem, proposals);
				break;
			}			
		}		
	}
		
	@SuppressWarnings("unchecked")
	void addNullableAnnotationInSignatureProposal(IInvocationContext context, IProblemLocation problem, @SuppressWarnings("rawtypes") Collection proposals) {
		IProposableFix fix= createNullableInSignatureFix(context.getASTRoot(), problem);
		
		if (fix != null) {
			Image image= JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
			Map<String, String> options= new Hashtable<String, String>();
			options.put(CleanUpConstants.ADD_MISSING_ANNOTATIONS, CleanUpOptions.TRUE);
			if (problem.getProblemId() == DefiniteNullFromNonNullMethod)
				options.put(CleanUpConstants.ADD_DEFINITELY_MISSING_RETURN_ANNOTATION_NULLABLE, CleanUpOptions.TRUE);
			if (problem.getProblemId() == PotentialNullFromNonNullMethod)
				options.put(CleanUpConstants.ADD_POTENTIALLY_MISSING_RETURN_ANNOTATION_NULLABLE, CleanUpOptions.TRUE);
			if (mayIndicateParameterNullcheck(problem.getProblemId()))
				options.put(CleanUpConstants.ADD_DEFINITELY_MISSING_PARAMETER_ANNOTATION_NULLABLE, CleanUpOptions.TRUE);
			if (problem.getProblemId() == DefiniteNullToNonNullParameter)
				options.put(CleanUpConstants.ADD_DEFINITELY_MISSING_PARAMETER_ANNOTATION_NULLABLE, CleanUpOptions.TRUE);
			FixCorrectionProposal proposal= new FixCorrectionProposal(fix, new NullAnnotationsCleanUp(options, this), 15, image, context);
			proposals.add(proposal);
		}		
	}
	
	boolean isComplainingAboutArgument(IInvocationContext context, IProblemLocation problem) {

		CompilationUnit astRoot= context.getASTRoot();
		ASTNode selectedNode= problem.getCoveringNode(astRoot);

		if (!(selectedNode instanceof SimpleName)) {
			return false;
		}
		SimpleName nameNode= (SimpleName) selectedNode;
		IBinding binding = nameNode.resolveBinding();
		if (binding.getKind() == IBinding.VARIABLE && ((IVariableBinding) binding).isParameter())
			return true;
		return false;
	}

	CompilationUnitRewriteOperationsFix createNullableInSignatureFix(CompilationUnit compilationUnit, IProblemLocation problem) {
		String nullableAnnotationName = getNullableAnnotationName(compilationUnit.getJavaElement(), false);
		String nonNullAnnotationName = getNonNullAnnotationName(compilationUnit.getJavaElement(), false);
		RewriteOperations.SignatureAnnotationRewriteOperation operation = createAddNullableOperation(
				compilationUnit, problem, nullableAnnotationName, nonNullAnnotationName, null, false, true);
		if (operation == null)
			return null;
		
		int lastDot = nullableAnnotationName.lastIndexOf('.');
		if (lastDot != -1)
			nullableAnnotationName = nullableAnnotationName.substring(lastDot+1);
		String messageTemplate = null;
		switch (problem.getProblemId()) {
		case DefiniteNullFromNonNullMethod:
		case PotentialNullFromNonNullMethod:
			messageTemplate = FixMessages.QuickFixes_declare_method_return_nullable;
			break;
		case DefiniteNullToNonNullParameter:
		case PotentialNullToNonNullParameter:
		case IProblem.NonNullLocalVariableComparisonYieldsFalse:
		case IProblem.RedundantNullCheckOnNonNullLocalVariable:
			messageTemplate = FixMessages.QuickFixes_declare_method_parameter_nullable;
			break;
		}
		return new CompilationUnitRewriteOperationsFix(Messages.format(messageTemplate, nullableAnnotationName),
														operation.getCompilationUnit(), 
														new CompilationUnitRewriteOperation[] {operation});
	}

	ICleanUpFix createCleanUp(CompilationUnit compilationUnit, 
								boolean addDefinitelyMissingReturnAnnotations, 
								boolean addPotentiallyMissingReturnAnnotations, 
								boolean addDefinitelyMissingParamAnnotations, 
								IProblemLocation[] locations) 
	{
		
		ICompilationUnit cu= (ICompilationUnit)compilationUnit.getJavaElement();
		if (!JavaModelUtil.is50OrHigher(cu.getJavaProject()))
			return null;
		
		if (! (addDefinitelyMissingReturnAnnotations || addPotentiallyMissingReturnAnnotations || addDefinitelyMissingParamAnnotations))
			return null;
		
		List<CompilationUnitRewriteOperation> operations= new ArrayList<CompilationUnitRewriteOperation>();
		
		if (locations == null) {
			org.eclipse.jdt.core.compiler.IProblem[] problems= compilationUnit.getProblems();
			locations= new IProblemLocation[problems.length];
			for (int i= 0; i < problems.length; i++) {
				if (   (addDefinitelyMissingReturnAnnotations && (problems[i].getID() == DefiniteNullFromNonNullMethod))
					|| (addPotentiallyMissingReturnAnnotations && (problems[i].getID() == PotentialNullFromNonNullMethod))
					|| (addDefinitelyMissingParamAnnotations && mayIndicateParameterNullcheck(problems[i].getID())))
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
		String nonNullAnnotationName = getNonNullAnnotationName(compilationUnit.getJavaElement(), false);
		Set<String> handledPositions = new HashSet<String>();
		for (int i= 0; i < locations.length; i++) {
			IProblemLocation problem= locations[i];
			if (problem == null) continue; // problem was filtered out by createCleanUp()
			CompilationUnitRewriteOperation fix = createAddNullableOperation(
						compilationUnit, problem, nullableAnnotationName, nonNullAnnotationName, handledPositions, true, false);
			if (fix != null)
				result.add(fix);
		}
	}
	
	RewriteOperations.SignatureAnnotationRewriteOperation createAddNullableOperation(CompilationUnit compilationUnit,
			IProblemLocation problem, String nullableAnnotationName, String nonNullAnnotationName, Set<String> handledPositions,
			boolean thisUnitOnly, boolean allowRemove) 
	{
		ICompilationUnit cu= (ICompilationUnit)compilationUnit.getJavaElement();
		if (!JavaModelUtil.is50OrHigher(cu.getJavaProject()))
			return null;
		
		if (!isMissingNullableAnnotationProblem(problem.getProblemId())) // TODO is this obsolete?
			return null;
		
		if (hasExplicitNullnessAnnotation(cu, problem.getOffset()))
			return null;
		
		ASTNode selectedNode= problem.getCoveringNode(compilationUnit);
		if (selectedNode == null)
			return null;
			
		RewriteOperations.SignatureAnnotationRewriteOperation result = null;
		ASTNode declaringNode= getDeclaringNode(selectedNode);
		if (selectedNode.getParent() instanceof MethodInvocation) {
			// check problem ID!
			MethodInvocation methodInvocation = (MethodInvocation)selectedNode.getParent();
			int paramIdx = methodInvocation.arguments().indexOf(selectedNode);
			IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
			ASTNode methodDecl= compilationUnit.findDeclaringNode(methodBinding.getMethodDeclaration());
			if (methodDecl == null) {
				// is methodDecl defined in another CU?
				ITypeBinding declaringTypeDecl= methodBinding.getDeclaringClass().getTypeDeclaration();
				if (declaringTypeDecl.isFromSource()) {
					ICompilationUnit targetCU = null;
					try {
						targetCU = ASTResolving.findCompilationUnitForBinding(cu, compilationUnit, declaringTypeDecl);
					} catch (JavaModelException e) { /* can't do better */ }
					if (targetCU != null) {
						compilationUnit = ASTResolving.createQuickFixAST(targetCU, null);
						methodDecl= compilationUnit.findDeclaringNode(methodBinding.getKey());
					}
				}
			}
			if (methodDecl != null)
				result = new RewriteOperations.ParameterAnnotationRewriteOperation(compilationUnit,
																				   (MethodDeclaration) methodDecl,
																				   nullableAnnotationName,
																				   nonNullAnnotationName,
																				   paramIdx,
																				   allowRemove);
		} else if (declaringNode instanceof MethodDeclaration) {
		
			MethodDeclaration declaration= (MethodDeclaration) declaringNode;
			
			if (mayIndicateParameterNullcheck(problem.getProblemId())) {
				if (selectedNode.getNodeType() == ASTNode.SIMPLE_NAME && declaration.getNodeType() == ASTNode.METHOD_DECLARATION) {
					IBinding binding = ((SimpleName)selectedNode).resolveBinding();
					if (binding.getKind() == IBinding.VARIABLE && ((IVariableBinding)binding).isParameter())
						result = new RewriteOperations.ParameterAnnotationRewriteOperation(compilationUnit,
																						   declaration,
																						   nullableAnnotationName,
																						   nonNullAnnotationName,
																						   ((SimpleName) selectedNode).getIdentifier(),
																						   allowRemove);
				}
			} else {
				result = new RewriteOperations.ReturnAnnotationRewriteOperation(compilationUnit,
																				declaration,
																				nullableAnnotationName,
																				nonNullAnnotationName,
																				allowRemove);
			}
			
		}
		if (handledPositions != null && result != null) {
			if (handledPositions.contains(result.getKey()))
				return null;
			handledPositions.add(result.getKey());
		}
		return result;
	}

	/** The relevant declaring node of a return statement is the enclosing method. */
	ASTNode getDeclaringNode(ASTNode selectedNode) {
		return ASTNodes.getParent(selectedNode, ASTNode.METHOD_DECLARATION);
	}

	static boolean isMissingNullableAnnotationProblem(int id) {
		return id == DefiniteNullFromNonNullMethod || id == PotentialNullFromNonNullMethod 
				|| id == DefiniteNullToNonNullParameter || id == PotentialNullToNonNullParameter
				|| mayIndicateParameterNullcheck(id);
	}
	
	static boolean mayIndicateParameterNullcheck(int problemId) {
		return problemId == IProblem.NonNullLocalVariableComparisonYieldsFalse || problemId == IProblem.RedundantNullCheckOnNonNullLocalVariable;
	}
	
	static boolean hasExplicitNullnessAnnotation(ICompilationUnit compilationUnit, int offset) {
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

	static String getNullableAnnotationName(IJavaElement javaElement, boolean makeSimple) {
		String qualifiedName = javaElement.getJavaProject().getOption(NullCompilerOptions.OPTION_NullableAnnotationName, true);
		int lastDot;
		if (makeSimple && qualifiedName != null && (lastDot = qualifiedName.lastIndexOf('.')) != -1)
			return qualifiedName.substring(lastDot+1);
		return qualifiedName;
	}

	static String getNonNullAnnotationName(IJavaElement javaElement, boolean makeSimple) {
		String qualifiedName = javaElement.getJavaProject().getOption(NullCompilerOptions.OPTION_NonNullAnnotationName, true);
		int lastDot;
		if (makeSimple && qualifiedName != null && (lastDot = qualifiedName.lastIndexOf('.')) != -1)
			return qualifiedName.substring(lastDot+1);
		return qualifiedName;
	}
}
