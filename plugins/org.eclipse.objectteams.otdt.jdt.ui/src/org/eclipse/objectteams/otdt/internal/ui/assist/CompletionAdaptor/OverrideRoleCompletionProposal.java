/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 Stephan Herrmann
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
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
team package org.eclipse.objectteams.otdt.internal.ui.assist.CompletionAdaptor;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.ui.text.correction.ASTResolving;
import org.eclipse.jdt.internal.ui.text.correction.proposals.ASTRewriteCorrectionProposal;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

/**
 * Completion proposal to override a role from the super team.
 */
@SuppressWarnings("restriction")
protected class OverrideRoleCompletionProposal extends ASTRewriteCorrectionProposal
{
	private static final int R_TYPE=100; // raise relevance by this amount above method mapping proposals
	
	private String fRoleName;
	private int fReplaceStart;
	private int fModifiers;
	private StyledString fDisplayString; 

	public OverrideRoleCompletionProposal( 
		       ICompilationUnit   cu,  
		       CompletionProposal proposal,  
			   int                length,
			   StyledString       displayName,
			   Image              image) 
	{
		super(displayName.toString(), cu, null, computeRelevance(proposal)+R_TYPE, image);
		fDisplayString = displayName;
		fRoleName=   String.valueOf(proposal.getName());
		fModifiers=    proposal.getFlags();
		fReplaceStart= proposal.getReplaceStart();
	}
	
	public StyledString getStyledDisplayString() {
		return this.fDisplayString;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected ASTRewrite getRewrite() throws CoreException 
	{
		CompilationUnit unit= ASTResolving.createQuickFixAST(getCompilationUnit(), null);

		// find enclosing team type:
		ASTNode node= NodeFinder.perform(unit, fReplaceStart, 0);
		while(node != null && !(node instanceof AbstractTypeDeclaration)) {
			node= node.getParent();
		}
		
		if (node != null) {
			AbstractTypeDeclaration teamDecl= ((AbstractTypeDeclaration) node);
			
			// create and setup the rewrite:
			AST ast = unit.getAST();
			ASTRewrite rewrite= ASTRewrite.create(ast);
			rewrite.setToOTJ();
			
			// create type
			TypeDeclaration newType = ast.newTypeDeclaration();
			newType.setName(ast.newSimpleName(this.fRoleName));
			newType.setInterface(Flags.isInterface(this.fModifiers));
			// add @Override:
			Annotation overrideAnnotation = ast.newMarkerAnnotation();
			overrideAnnotation.setTypeName(ast.newSimpleName("Override")); //$NON-NLS-1$
			List modifiers = newType.modifiers();
			modifiers.add(overrideAnnotation);
			// add protected or public
			if (Flags.isPublic(this.fModifiers))
				modifiers.add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
			else
				modifiers.add(ast.newModifier(ModifierKeyword.PROTECTED_KEYWORD));
			
			insertStub(rewrite, teamDecl, teamDecl.getBodyDeclarationsProperty(), this.fReplaceStart, newType);
			return rewrite;
		}
		return null;
	}
}
