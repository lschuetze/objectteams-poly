/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 Stephan Herrmann.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.text.correction;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractMethodMappingDeclaration;
import org.eclipse.jdt.core.dom.CallinMappingDeclaration;
import org.eclipse.jdt.core.dom.CalloutMappingDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccessSpec;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IMethodMappingBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodMappingElement;
import org.eclipse.jdt.core.dom.MethodSpec;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.RoleTypeDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.text.correction.proposals.LinkedCorrectionProposal;
import org.eclipse.objectteams.otdt.internal.ui.util.OTStubUtility;
import org.eclipse.objectteams.otdt.ui.OTDTUIPlugin;
import org.eclipse.text.edits.TextEditGroup;


/**
 * Correction proposals for adding signatures to signature-less method mappings.
 * Use linked mode if several base methods of the given name exist and are compatible.
 * 
 * @since 2.1.0; before 2.0.0 everything was inlined in {@link QuickAssistProcessor}.
 */
@SuppressWarnings("restriction")
public class AddMethodMappingSignaturesProposal extends LinkedCorrectionProposal 
{
	protected static final String KEY_BASEMETHOD = "basemethod"; //$NON-NLS-1$

	private final AbstractMethodMappingDeclaration mapping;

	public AddMethodMappingSignaturesProposal(ICompilationUnit cu, AbstractMethodMappingDeclaration mapping, int relevance) 
	{
		super(CorrectionMessages.QuickAssistProcessor_addMethodBindingSignatures_label, 
			  cu, null, 
			  relevance, JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_ADD));
		this.mapping = mapping;
	}

	@Override
	protected ASTRewrite getRewrite() throws CoreException 
	{
		ASTRewrite rewrite = ASTRewrite.create(mapping.getAST());
		ICompilationUnit cu = getCompilationUnit();
		TextEditGroup editGroup = new TextEditGroup(CorrectionMessages.QuickAssistProcessor_addSignature_editName);
		ImportRewrite imports = createImportRewrite((CompilationUnit) ASTNodes.getParent(mapping, ASTNode.COMPILATION_UNIT));
		// role method:
		IMethodBinding roleMethod = ((MethodSpec)mapping.getRoleMappingElement()).resolveBinding();
		MethodSpec newSpec = OTStubUtility.createMethodSpec(cu, rewrite, imports, roleMethod, true);
		convertTypeParameters(mapping.getAST(), roleMethod, newSpec);
		rewrite.set(mapping, mapping.getRoleElementProperty(), newSpec, editGroup);
		
		// base method(s):
		if (mapping.getNodeType() == ASTNode.CALLIN_MAPPING_DECLARATION)
			addSignatureToCallinBases(cu, rewrite, imports, (CallinMappingDeclaration) mapping, editGroup, roleMethod);
		else
			addSignatureToCalloutBase(cu, rewrite, imports, (CalloutMappingDeclaration) mapping, editGroup, roleMethod);
		return rewrite;
	}

	// helper: add signatures to all base specs of a callin mapping:
	private void addSignatureToCallinBases(ICompilationUnit cu,
			ASTRewrite rewrite, 
			ImportRewrite imports,
			CallinMappingDeclaration mapping, 
			TextEditGroup editGroup, 
			IMethodBinding roleMethod) 
	{
		@SuppressWarnings("unchecked")
		List<MethodSpec> oldBaseSpecs = mapping.getBaseMappingElements();
		ListRewrite baseMethodsRewrite = rewrite.getListRewrite(mapping, CallinMappingDeclaration.BASE_MAPPING_ELEMENTS_PROPERTY);
		IMethodMappingBinding mappingBinding = mapping.resolveBinding();
		if (mappingBinding != null) {
			// create proposal from exact methods
			IMethodBinding[] baseMethods = mappingBinding.getBaseMethods();
			for (int i=0; i<baseMethods.length; i++) {
				IMethodBinding baseMethod = baseMethods[i];
				try {
					MethodSpec newSpec = OTStubUtility.createMethodSpec(cu, rewrite, imports, baseMethod, true);
					baseMethodsRewrite.replace(oldBaseSpecs.get(i), newSpec, editGroup);
				} catch (CoreException e) {
					OTDTUIPlugin.log(e);
				}
			}
		} else {
			ITypeBinding[] roleParameters = roleMethod.getParameterTypes();
			RoleTypeDeclaration role = (RoleTypeDeclaration) mapping.getParent();
			for (int i=0; i < oldBaseSpecs.size(); i++) {
				MethodSpec oldBaseSpec = oldBaseSpecs.get(i);
				// search matching base methods:
				List<IMethodBinding> matchingBaseMethods = new ArrayList<IMethodBinding>();
				guessBaseMethod(role.resolveBinding(), oldBaseSpec.getName().getIdentifier(), roleParameters, true, matchingBaseMethods);
				if (matchingBaseMethods.size() > 0) try {
					MethodSpec newSpec = OTStubUtility.createMethodSpec(cu, rewrite, imports, matchingBaseMethods.get(0), true);
					baseMethodsRewrite.replace(oldBaseSpecs.get(i), newSpec, editGroup);
					// do we have alternatives to propose?
					if (matchingBaseMethods.size() > 1) {
						addLinkedPosition(rewrite.track(newSpec), false, KEY_BASEMETHOD);
						for(IMethodBinding baseMethodBinding : matchingBaseMethods) {
							MethodSpec bSpec = OTStubUtility.createMethodSpec(cu, rewrite, imports, baseMethodBinding, true);
							addLinkedPositionProposal(KEY_BASEMETHOD, bSpec.toString(), null);
						}
					}
				} catch (CoreException e) {
					OTDTUIPlugin.log(e);
				}
			}
		}
	}

	// helper: add signature to a callout base spec (method or field)
	private void addSignatureToCalloutBase(ICompilationUnit cu,
										   ASTRewrite rewrite, 
										   ImportRewrite imports,
										   CalloutMappingDeclaration mapping, 
										   TextEditGroup editGroup, 
										   IMethodBinding roleMethod) 
	{
		MethodMappingElement baseElement;
		try {
			List<IMethodBinding> matchingBaseMethods = new ArrayList<IMethodBinding>();
			if (mapping.bindingOperator().isCalloutToField()) {
				IVariableBinding baseField = ((FieldAccessSpec)mapping.getBaseMappingElement()).resolveBinding();
				baseElement = OTStubUtility.createFieldSpec(mapping.getAST(), imports, baseField, true);
			} else {
				IMethodMappingBinding mappingBinding = mapping.resolveBinding();
				if (mappingBinding != null) {
					// create proposal from exact method
					IMethodBinding baseMethod = mappingBinding.getBaseMethods()[0];
					baseElement = OTStubUtility.createMethodSpec(cu, rewrite, imports, baseMethod, true);
				} else {
					RoleTypeDeclaration role = (RoleTypeDeclaration) mapping.getParent();
					ITypeBinding[] roleParameters = roleMethod.getParameterTypes();
					MethodSpec oldBaseSpec = (MethodSpec) mapping.getBaseMappingElement();
					// search matching base methods:
					guessBaseMethod(role.resolveBinding(), oldBaseSpec.getName().getIdentifier(), roleParameters, false, matchingBaseMethods);
					if (matchingBaseMethods.size() == 0)
						return;
					baseElement = OTStubUtility.createMethodSpec(cu, rewrite, imports, matchingBaseMethods.get(0), true);
				}
			}
			rewrite.set(mapping, CalloutMappingDeclaration.BASE_MAPPING_ELEMENT_PROPERTY, baseElement, editGroup);
			// do we have alternatives to propose?
			if (matchingBaseMethods.size() > 1) {
				addLinkedPosition(rewrite.track(baseElement), false, KEY_BASEMETHOD);
				for(IMethodBinding baseMethodBinding : matchingBaseMethods) {
					MethodSpec bSpec = OTStubUtility.createMethodSpec(cu, rewrite, imports, baseMethodBinding, true);
					addLinkedPositionProposal(KEY_BASEMETHOD, bSpec.toString(), null);
				}
			}
		} catch (CoreException e) {
			OTDTUIPlugin.log(e);
		}
	}

	private void guessBaseMethod(ITypeBinding roleType, String selector, ITypeBinding[] roleParameters, boolean isCallin, List<IMethodBinding> result) 
	{	
		ITypeBinding baseClass = roleType.getBaseClass();
		if (baseClass == null)
			return;
		baseMethods: 
		for (IMethodBinding baseMethod : baseClass.getDeclaredMethods()) {
			if (!baseMethod.getName().equals(selector))
				continue;
			ITypeBinding[] baseParameters = baseMethod.getParameterTypes();
			int provided = isCallin ? baseParameters.length : roleParameters.length;
			int consumed = isCallin ? roleParameters.length : baseParameters.length;
			if (provided < consumed)
				continue;
			for(int i=0; i<consumed; i++)
				if (!isCompatible(roleParameters[i], baseParameters[i], isCallin))
					continue baseMethods;
			if (baseParameters.length == roleParameters.length)
				result.add(0, baseMethod); // same number of parameters is considered our best guess
			else
				result.add(baseMethod);
		}
	}

	private boolean isCompatible(ITypeBinding roleSideType, ITypeBinding baseSideType, boolean isCallin) {
		ITypeBinding requiredType = isCallin ? roleSideType : baseSideType;
		ITypeBinding providedType = isCallin ? baseSideType : roleSideType;

		if (providedType.isAssignmentCompatible(requiredType))
			return true;
		
		if (isCallin) {
			requiredType = requiredType.getBaseClass(); // anticipate lifting
			if (requiredType == null)
				return false;
		} else {
			providedType = providedType.getBaseClass(); // use lowering
			if (providedType == null)
				return false;
		}
		if (providedType.isAssignmentCompatible(requiredType))
			return true;

		return false;
	}

	@SuppressWarnings("unchecked")
	private void convertTypeParameters(final AST ast, IMethodBinding roleMethod, MethodSpec destMethodSpec) 
	{
		for (ITypeBinding typeParameter : roleMethod.getTypeParameters()) {
			TypeParameter newTypeParameter = ast.newTypeParameter();
			newTypeParameter.setName(ast.newSimpleName(typeParameter.getName()));
			for (ITypeBinding typeBound : typeParameter.getTypeBounds())
				newTypeParameter.typeBounds().add(convertType(ast, typeBound));
			destMethodSpec.typeParameters().add(newTypeParameter);
		}
	}

	@SuppressWarnings("unchecked")
	private Type convertType(AST ast, ITypeBinding typeBinding) {
		if (typeBinding.isTypeVariable())
			return ast.newSimpleType(ast.newSimpleName(typeBinding.getName()));
		String typeName = typeBinding.getErasure().getQualifiedName();
		Type type = (typeName.indexOf('.') > -1)
			? ast.newSimpleType(ast.newName(typeName))
			: ast.newSimpleType(ast.newSimpleName(typeName));
		ITypeBinding[] typeArguments = typeBinding.getTypeArguments();
		if (typeArguments.length > 0) {
			ParameterizedType parameterizedType = ast.newParameterizedType(type);
			for (ITypeBinding bound : typeArguments)
				parameterizedType.typeArguments().add(convertType(ast, bound));
			return parameterizedType;
		}
		return type;
	}
}