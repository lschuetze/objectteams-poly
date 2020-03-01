/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010, 2011 Stephan Herrmann.
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
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.text.correction;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractMethodMappingDeclaration;
import org.eclipse.jdt.core.dom.CallinMappingDeclaration;
import org.eclipse.jdt.core.dom.CalloutMappingDeclaration;
import org.eclipse.jdt.core.dom.FieldAccessSpec;
import org.eclipse.jdt.core.dom.MethodMappingElement;
import org.eclipse.jdt.core.dom.MethodSpec;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.text.java.correction.ASTRewriteCorrectionProposal;

/** 
 * Proposals for removing signatures from method mappings. 
 * 
 * @since 2.1.0; before 2.0.0 everything was inlined in {@link QuickAssistProcessor}.
 */
@SuppressWarnings("restriction")
public class RemoveMethodMappingSignaturesProposal extends ASTRewriteCorrectionProposal {
	
	private AbstractMethodMappingDeclaration mapping;

	public RemoveMethodMappingSignaturesProposal(ICompilationUnit cu, AbstractMethodMappingDeclaration mapping, int relevance) 
	{
		super(CorrectionMessages.QuickAssistProcessor_removeMethodBindingSignatures_label, 
			  cu, null,
			  relevance, JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_REMOVE));
		this.mapping = mapping;
	}
	@Override
	protected ASTRewrite getRewrite() throws CoreException {
		
		ASTRewrite rewrite= ASTRewrite.create(mapping.getAST());

		// remove type parameters (only LHS):
		ListRewrite typeParamters = rewrite.getListRewrite(mapping.getRoleMappingElement(), MethodSpec.TYPE_PARAMETERS_PROPERTY);
		for (Object typeParamObject : ((MethodSpec)mapping.getRoleMappingElement()).typeParameters())
			typeParamters.remove((ASTNode) typeParamObject, null);
		
		// remove role signature:
		removeSignature(rewrite, mapping.getRoleMappingElement());
		
		// remove base signature(s):
		if (mapping instanceof CalloutMappingDeclaration)
			removeSignature(rewrite, ((CalloutMappingDeclaration)mapping).getBaseMappingElement());
		else
			for (Object baseElement : ((CallinMappingDeclaration)mapping).getBaseMappingElements())
				removeSignature(rewrite, (MethodMappingElement)baseElement);
		
		return rewrite;
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

}
