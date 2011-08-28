/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 Stephan Herrmann.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.text.correction;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractMethodMappingDeclaration;
import org.eclipse.jdt.core.dom.CallinMappingDeclaration;
import org.eclipse.jdt.core.dom.CalloutMappingDeclaration;
import org.eclipse.jdt.core.dom.FieldAccessSpec;
import org.eclipse.jdt.core.dom.GuardPredicateDeclaration;
import org.eclipse.jdt.core.dom.MethodMappingElement;
import org.eclipse.jdt.core.dom.MethodSpec;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.text.correction.proposals.ASTRewriteCorrectionProposal;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;
import org.eclipse.swt.graphics.Image;

/**
 * OT/J specific quick assists.
 * @since 0.7.0
 */
@SuppressWarnings("restriction")
public class QuickAssistProcessor implements IQuickAssistProcessor {

	enum Errors { NONE, EXPECTED, UNEXPECTED }
	
	public boolean hasAssists(IInvocationContext context) throws CoreException {
		ASTNode coveringNode= context.getCoveringNode();
		if (coveringNode != null)
			return hasToggleSignaturesOfMethodMappingProposal(coveringNode);
		return false;
	}

	private boolean hasToggleSignaturesOfMethodMappingProposal(ASTNode coveringNode) {
		while (coveringNode != null) {
			switch (coveringNode.getNodeType()) {
			case ASTNode.CALLIN_MAPPING_DECLARATION:
			case ASTNode.CALLOUT_MAPPING_DECLARATION:
				AbstractMethodMappingDeclaration methodMapping = (AbstractMethodMappingDeclaration)coveringNode;
				if (methodMapping.hasParameterMapping())
					return false;
				return true;
			case ASTNode.TYPE_DECLARATION:
			case ASTNode.ROLE_TYPE_DECLARATION:
			case ASTNode.COMPILATION_UNIT:
				return false;
			}
			coveringNode = coveringNode.getParent();
		}
		return false;
	}

	public IJavaCompletionProposal[] getAssists(IInvocationContext context, IProblemLocation[] locations)
			throws CoreException
	{
		ASTNode coveringNode= context.getCoveringNode();
		if (coveringNode != null) {
			ArrayList<ASTRewriteCorrectionProposal> resultingCollections= new ArrayList<ASTRewriteCorrectionProposal>();
			Errors matchedErrorsAtLocation= matchErrorsAtLocation(locations, 
											new int[]{IProblem.AmbiguousCallinMethodSpec, IProblem.AmbiguousCalloutMethodSpec});

			// quick assists that show up also if there is an error/warning
			getRemoveMethodMappingSignaturesProposal(context.getCompilationUnit(), coveringNode, resultingCollections);
			if (matchedErrorsAtLocation != Errors.UNEXPECTED) {
				int relevance = matchedErrorsAtLocation == Errors.NONE ? 1 : 10;
				getAddMethodMappingSignaturesProposal(context.getCompilationUnit(), coveringNode, relevance, resultingCollections);
			}
			return resultingCollections.toArray(new IJavaCompletionProposal[resultingCollections.size()]);
		}
		return null;
	}
	
	/* Proposals for removing signatures from method mappings. */
	private void getRemoveMethodMappingSignaturesProposal(ICompilationUnit cu,
														  ASTNode coveringNode, 
														  ArrayList<ASTRewriteCorrectionProposal> resultingCollections) 
	{
		AbstractMethodMappingDeclaration mapping = getMethodMapping(coveringNode);
		if (mapping != null && mapping.hasSignature()) {
			
			if (mapping.getNodeType() == ASTNode.CALLIN_MAPPING_DECLARATION)
				if (predicateUsesMethodSpecArgument((CallinMappingDeclaration) mapping))
					return; // don't propose to remove signatures, since an arg is used in the predicate
			
			AST ast= mapping.getAST();
			ASTRewrite rewrite= ASTRewrite.create(ast);

			// remove type parameters (only LHS):
			ListRewrite typeParamters = rewrite.getListRewrite(mapping.getRoleMappingElement(), MethodSpec.TYPE_PARAMETERS_PROPERTY);
			for (Object typeParamObject : ((MethodSpec)mapping.getRoleMappingElement()).typeParameters())
				typeParamters.remove((ASTNode) typeParamObject, null);
			
			// remove role signature:
			removeSignature(rewrite, mapping.getRoleMappingElement());
			
			// remove base signature(s):
			if (mapping instanceof CalloutMappingDeclaration) {
				removeSignature(rewrite, ((CalloutMappingDeclaration)mapping).getBaseMappingElement());									
			} else {
				for (Object baseElement : ((CallinMappingDeclaration)mapping).getBaseMappingElements()) {
					removeSignature(rewrite, (MethodMappingElement)baseElement);					
				}
			}
			
			String label= CorrectionMessages.QuickAssistProcessor_removeMethodBindingSignatures_label;
			Image image= JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_REMOVE);
			ASTRewriteCorrectionProposal proposal= new ASTRewriteCorrectionProposal(label, cu, rewrite, 1, image);
			resultingCollections.add(proposal);
		}
	}

	// helper: detect whether a guard predicate uses an argument of the corresponding method spec.
	private boolean predicateUsesMethodSpecArgument(CallinMappingDeclaration callinMapping) 
	{
		GuardPredicateDeclaration predicate = callinMapping.getGuardPredicate();
		if (predicate == null)
			return false;
		final List<String> argNames = new ArrayList<String>();
		if (predicate.isBase())
			for (Object baseMethodObject : callinMapping.getBaseMappingElements()) 
				for (Object baseArgObj : ((MethodSpec)baseMethodObject).parameters())
					argNames.add(((SingleVariableDeclaration) baseArgObj).getName().getIdentifier());
		else
			for (Object roleArgObj : ((MethodSpec)callinMapping.getRoleMappingElement()).parameters())
				argNames.add(((SingleVariableDeclaration) roleArgObj).getName().getIdentifier());
		
		try {
			predicate.getExpression().accept(new ASTVisitor() {
				@Override
				public boolean visit(SimpleName node) {
					for (String argName : argNames)
						if (argName.equals(node.getIdentifier()))
							throw new RuntimeException();
					return false;
				}
			});
		} catch (RuntimeException re) {
			return true; 
		}
		return false;
	}
	
	// helper: remove the signature from one mapping element (method spec or field access spec).
	private void removeSignature(ASTRewrite rewrite, MethodMappingElement mappingElement) {
		rewrite.set(mappingElement, mappingElement.signatureProperty(), Boolean.FALSE, null);
		switch (mappingElement.getNodeType()) {
		case ASTNode.FIELD_ACCESS_SPEC:
			rewrite.set(mappingElement, FieldAccessSpec.FIELD_TYPE_PROPERTY, null, null);
			break;
		case ASTNode.METHOD_SPEC:
			// return type:
			rewrite.set(mappingElement, MethodSpec.RETURN_TYPE2_PROPERTY, null, null);
			// type parameters:
			ListRewrite listRewrite = rewrite.getListRewrite(mappingElement, MethodSpec.TYPE_PARAMETERS_PROPERTY);
			for (Object toRemove : ((MethodSpec)mappingElement).typeParameters())
				listRewrite.remove((ASTNode) toRemove, null);
			// parameters:
			listRewrite = rewrite.getListRewrite(mappingElement, MethodSpec.PARAMETERS_PROPERTY);
			for (Object toRemove : ((MethodSpec)mappingElement).parameters())
				listRewrite.remove((ASTNode) toRemove, null);
			break;
		}
	}

	/* Proposals for adding signatures to method mappings. */
	private void getAddMethodMappingSignaturesProposal(ICompilationUnit cu,
			  										   ASTNode coveringNode,
												       int relevance, 
												       ArrayList<ASTRewriteCorrectionProposal> resultingCollections) 
	{
		final AbstractMethodMappingDeclaration mapping = getMethodMapping(coveringNode);
		if (mapping != null && !mapping.hasSignature()) {			
			resultingCollections.add(new AddMethodMappingSignaturesProposal(cu, mapping, relevance));
		}		
	}


	private AbstractMethodMappingDeclaration getMethodMapping (ASTNode coveringNode) {
		if (coveringNode instanceof AbstractMethodMappingDeclaration)
			return (AbstractMethodMappingDeclaration) coveringNode;
		return (AbstractMethodMappingDeclaration) ASTNodes.getParent(coveringNode, AbstractMethodMappingDeclaration.class);
	}


	private Errors matchErrorsAtLocation(IProblemLocation[] locations, int[] expectedProblemIds) {
		boolean hasMatch = false;
		if (locations != null) {
			locations: for (int i= 0; i < locations.length; i++) {
				IProblemLocation location= locations[i];
				if (location.isError()) {
					int problemId = location.getProblemId();
					if (expectedProblemIds != null)
						for (int j = 0; j < expectedProblemIds.length; j++)
							if (expectedProblemIds[j] == problemId) {
								hasMatch = true;
								continue locations;
							}
					if (IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER.equals(location.getMarkerType())
							&& JavaCore.getOptionForConfigurableSeverity(problemId) != null) {
						// continue (only drop out for severe (non-optional) errors)
					} else {
						return Errors.UNEXPECTED;
					}
				}
			}
		}
		return hasMatch ? Errors.EXPECTED : Errors.NONE;
	}
}
