/*******************************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2007 Technical University Berlin, Germany and others.
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
 *     Technical University Berlin - Initial API and implementation
 *     IBM Corporation - copies of individual methods from bound base classes.
 *******************************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.assist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.TSuperConstructorInvocation;
import org.eclipse.jdt.internal.corext.dom.Bindings;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.text.correction.ASTResolving;
import org.eclipse.jdt.internal.ui.text.correction.CorrectionMessages;
import org.eclipse.jdt.internal.ui.text.correction.IProposalRelevance;
import org.eclipse.jdt.internal.ui.text.correction.proposals.NewAbstractMethodCorrectionProposal;
import org.eclipse.jdt.internal.ui.text.correction.proposals.NewMethodCorrectionProposal;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import org.eclipse.jdt.ui.JavaElementImageDescriptor;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.correction.ICommandAccess;
import org.eclipse.swt.graphics.Image;

import base org.eclipse.jdt.internal.ui.text.correction.UnresolvedElementsSubProcessor;

/**
 * Individual method bodies are copied from their base version.
 * 
 * @author stephan
 * @since 1.1.2
 */
@SuppressWarnings("restriction")
public team class CorrectionAdaptor {
	protected class UnresolvedElementsSubProcessor playedBy UnresolvedElementsSubProcessor
	{
		/** This callin adjusts the compilation unit, when a new method should be inserted
		 *  into the outer type: which for a role file is NOT within the current CU.
		 */	
		@SuppressWarnings("decapsulation")
		void addNewMethodProposals(ICompilationUnit cu, CompilationUnit astRoot, Expression sender, List<Expression> arguments, boolean isSuperInvocation, ASTNode invocationNode, String methodName, Collection<ICommandAccess> proposals)
		<- replace void addNewMethodProposals(ICompilationUnit cu, CompilationUnit astRoot, Expression sender, List<Expression> arguments, boolean isSuperInvocation, ASTNode invocationNode, String methodName, Collection<ICommandAccess> proposals);
		
		@SuppressWarnings("basecall")
		callin static void addNewMethodProposals(ICompilationUnit cu, CompilationUnit astRoot, Expression sender, List<Expression> arguments, boolean isSuperInvocation, ASTNode invocationNode, String methodName, Collection<ICommandAccess> proposals) 
				throws JavaModelException
		{
			// OT_COPY_PASTE:
			ITypeBinding nodeParentType= Bindings.getBindingOfParentType(invocationNode);
			ITypeBinding binding= null;
			if (sender != null) {
				binding= sender.resolveTypeBinding();
			} else {
				binding= nodeParentType;
				if (isSuperInvocation && binding != null) {
					binding= binding.getSuperclass();
				}
			}
			if (binding != null && binding.isFromSource()) {
				ITypeBinding senderDeclBinding= binding.getErasure().getTypeDeclaration();
//{ObjectTeams: don't erase a RoleTypeBinding:
				if (binding.isRole()) {
					senderDeclBinding = binding.getTypeDeclaration();
				}
// SH}

				ICompilationUnit targetCU= ASTResolving.findCompilationUnitForBinding(cu, astRoot, senderDeclBinding);
				if (targetCU != null) {
					String label;
					String labelAbstract;
					Image image;
					ITypeBinding[] parameterTypes= getParameterTypes(arguments);
					if (parameterTypes != null) {
						String sig= org.eclipse.jdt.internal.ui.text.correction.ASTResolving.getMethodSignature(methodName, parameterTypes, false);
						boolean is18OrHigher= JavaModelUtil.is1d8OrHigher(targetCU.getJavaProject());
						boolean isSenderTypeAbstractClass = (senderDeclBinding.getModifiers() &  Modifier.ABSTRACT) > 0; 
						boolean isSenderBindingInterface= senderDeclBinding.isInterface();
						if (nodeParentType == senderDeclBinding) {
							label= Messages.format(CorrectionMessages.UnresolvedElementsSubProcessor_createmethod_description, sig);
							labelAbstract= Messages.format(CorrectionMessages.UnresolvedElementsSubProcessor_createmethod_abstract_description, sig);
							if (isSenderBindingInterface) {
								image= JavaPluginImages.get(JavaPluginImages.IMG_MISC_PUBLIC);
							} else {
								image= JavaPluginImages.get(JavaPluginImages.IMG_MISC_PRIVATE);
							}
						} else {
							label= Messages.format(CorrectionMessages.UnresolvedElementsSubProcessor_createmethod_other_description, new Object[] { sig, BasicElementLabels.getJavaElementName(senderDeclBinding.getName()) } );
							labelAbstract= Messages.format(CorrectionMessages.UnresolvedElementsSubProcessor_createmethod_abstract_other_description, new Object[] { sig, BasicElementLabels.getJavaElementName(senderDeclBinding.getName()) } );
							image= JavaPluginImages.get(JavaPluginImages.IMG_MISC_PUBLIC);
						}
						if (is18OrHigher || !isSenderBindingInterface
								|| (nodeParentType != senderDeclBinding && (!(sender instanceof SimpleName) || !((SimpleName) sender).getIdentifier().equals(senderDeclBinding.getName())))) {
							proposals.add(new NewMethodCorrectionProposal(label, targetCU, invocationNode, arguments, senderDeclBinding, IProposalRelevance.CREATE_METHOD, image));
							if ( isSenderTypeAbstractClass ) {
								proposals.add(new NewAbstractMethodCorrectionProposal(labelAbstract, targetCU, invocationNode, arguments, senderDeclBinding, IProposalRelevance.CREATE_METHOD, JavaPluginImages.get(JavaPluginImages.IMG_MISC_PROTECTED)));
							}
						}

						if (senderDeclBinding.isNested() && cu.equals(targetCU) && sender == null && Bindings.findMethodInHierarchy(senderDeclBinding, methodName, (ITypeBinding[]) null) == null) { // no covering method
							ASTNode anonymDecl= astRoot.findDeclaringNode(senderDeclBinding);
							if (anonymDecl != null) {
								senderDeclBinding= Bindings.getBindingOfParentType(anonymDecl.getParent());
								isSenderBindingInterface= senderDeclBinding.isInterface();
								isSenderTypeAbstractClass = (senderDeclBinding.getModifiers() &  Modifier.ABSTRACT) > 0;
								if (!senderDeclBinding.isAnonymous()) {
									if (is18OrHigher || !isSenderBindingInterface) {
										String[] args= new String[] { sig, org.eclipse.jdt.internal.ui.text.correction.ASTResolving.getTypeSignature(senderDeclBinding) };
										label= Messages.format(CorrectionMessages.UnresolvedElementsSubProcessor_createmethod_other_description, args);
										labelAbstract= Messages.format(CorrectionMessages.UnresolvedElementsSubProcessor_createmethod_abstract_other_description, args);
										if (isSenderBindingInterface) {
											image= JavaPluginImages.get(JavaPluginImages.IMG_MISC_PUBLIC);
										} else {
											image= JavaPluginImages.get(JavaPluginImages.IMG_MISC_PROTECTED);
										}
//{ObjectTeams: the pay-load: when traveling out of a role file, search for the new targetCU:
										if (binding.isRole() && astRoot.findDeclaringNode(senderDeclBinding) == null)
											targetCU= ASTResolving.findCompilationUnitForBinding(cu, astRoot, senderDeclBinding);
// SH}
										proposals.add(new NewMethodCorrectionProposal(label, targetCU, invocationNode, arguments, senderDeclBinding, IProposalRelevance.CREATE_METHOD, image));
										if ( isSenderTypeAbstractClass ) {
											proposals.add(new NewAbstractMethodCorrectionProposal(labelAbstract, targetCU, invocationNode, arguments, senderDeclBinding, IProposalRelevance.CREATE_METHOD, JavaPluginImages.get(JavaPluginImages.IMG_MISC_PROTECTED)));
										}
									}
								}
							}
						}
					}
				}
			}
		}

		void getConstructorProposals(IInvocationContext context, IProblemLocation problem, Collection<ICommandAccess> proposals) 
			<- replace void getConstructorProposals(IInvocationContext context, IProblemLocation problem, Collection<ICommandAccess> proposals);
		
		/* Copied from base method and adapted for tsuper ctor calls and role constructors. */
		@SuppressWarnings({"unchecked", "basecall"})
		static callin void getConstructorProposals(IInvocationContext context, IProblemLocation problem, Collection<ICommandAccess> proposals)
				throws CoreException
		{
			ICompilationUnit cu= context.getCompilationUnit();
	
			CompilationUnit astRoot= context.getASTRoot();
			ASTNode selectedNode= problem.getCoveringNode(astRoot);
			if (selectedNode == null) {
				return;
			}
	
			ITypeBinding targetBinding= null;
			List<Expression> arguments= null;
			IMethodBinding recursiveConstructor= null;
	
			int type= selectedNode.getNodeType();
			if (type == ASTNode.CLASS_INSTANCE_CREATION) {
				ClassInstanceCreation creation= (ClassInstanceCreation) selectedNode;

				IBinding binding= creation.getType().resolveBinding();
				if (binding instanceof ITypeBinding) {
					targetBinding= (ITypeBinding) binding;
					arguments= creation.arguments();
				}
			} else if (type == ASTNode.SUPER_CONSTRUCTOR_INVOCATION) {
				ITypeBinding typeBinding= Bindings.getBindingOfParentType(selectedNode);
				if (typeBinding != null && !typeBinding.isAnonymous()) {
					targetBinding= typeBinding.getSuperclass();
					arguments= ((SuperConstructorInvocation) selectedNode).arguments();
				}
			} else if (type == ASTNode.CONSTRUCTOR_INVOCATION) {
				ITypeBinding typeBinding= Bindings.getBindingOfParentType(selectedNode);
				if (typeBinding != null && !typeBinding.isAnonymous()) {
					targetBinding= typeBinding;
					arguments= ((ConstructorInvocation) selectedNode).arguments();
					recursiveConstructor= ASTResolving.findParentMethodDeclaration(selectedNode).resolveBinding();
				}
//{ObjectTeams: similar to above but with different super class computation:
			} else if (type == ASTNode.TSUPER_CONSTRUCTOR_INVOCATION) {
				ITypeBinding typeBinding= Bindings.getBindingOfParentType(selectedNode);
				if (typeBinding != null && !typeBinding.isAnonymous()) {
					targetBinding= getTSuperClass(typeBinding);
					arguments= ((TSuperConstructorInvocation) selectedNode).getArguments();
				}
// SH}
			}

			if (selectedNode.getParent() instanceof EnumConstantDeclaration) {
				EnumConstantDeclaration enumNode= (EnumConstantDeclaration) selectedNode.getParent();
				ITypeBinding typeBinding= Bindings.getBindingOfParentType(selectedNode);
				if (typeBinding != null && !typeBinding.isAnonymous()) {
					targetBinding= typeBinding;
					arguments= enumNode.arguments();
				}
			}
			if (targetBinding == null) {
				return;
			}
//{ObjectTeams: instantiation requires the class:
			if (targetBinding.isRole() && targetBinding.isInterface())
				targetBinding = targetBinding.getClassPart();
// SH}
			ArrayList<IMethodBinding> similarElements= new ArrayList<>();
			for (IMethodBinding curr : targetBinding.getDeclaredMethods()) {
				if (curr.isConstructor() && recursiveConstructor != curr) {
					similarElements.add(curr); // similar elements can contain a implicit default constructor
				}
			}
	
			addParameterMissmatchProposals(context, problem, similarElements, selectedNode, arguments, proposals);
	
			if (targetBinding.isFromSource()) {
				ITypeBinding targetDecl= targetBinding.getTypeDeclaration();
	
				ICompilationUnit targetCU= ASTResolving.findCompilationUnitForBinding(cu, astRoot, targetDecl);
				if (targetCU != null) {
					String[] args= new String[] { org.eclipse.jdt.internal.ui.text.correction.ASTResolving.getMethodSignature( org.eclipse.jdt.internal.ui.text.correction.ASTResolving.getTypeSignature(targetDecl), getParameterTypes(arguments), false) };
					String label= Messages.format(CorrectionMessages.UnresolvedElementsSubProcessor_createconstructor_description, args);
					Image image= JavaElementImageProvider.getDecoratedImage(JavaPluginImages.DESC_MISC_PUBLIC, JavaElementImageDescriptor.CONSTRUCTOR, JavaElementImageProvider.SMALL_SIZE);
					proposals.add(new NewMethodCorrectionProposal(label, targetCU, selectedNode, arguments, targetDecl, IProposalRelevance.CREATE_CONSTRUCTOR, image));
				}
			}
		}
		
		static ITypeBinding getTSuperClass(ITypeBinding type) { // FIXME(SH): currently only first-level tsuper is used
			ITypeBinding[] superRoles = type.getSuperRoles();
			if (superRoles != null && superRoles.length > 0)
				return superRoles[0];
			return null;
		}
		
		
		@SuppressWarnings("decapsulation")
		ITypeBinding[] getParameterTypes(List<Expression> args) -> ITypeBinding[] getParameterTypes(List<Expression> args);

		@SuppressWarnings("decapsulation")
		void addParameterMissmatchProposals(IInvocationContext arg0, IProblemLocation arg1, List<IMethodBinding> arg2, ASTNode arg3, List<Expression> arg4, Collection<ICommandAccess> arg5) 
			-> void addParameterMissmatchProposals(IInvocationContext arg0, IProblemLocation arg1, List<IMethodBinding> arg2, ASTNode arg3, List<Expression> arg4, Collection<ICommandAccess> arg5);
	}
}
