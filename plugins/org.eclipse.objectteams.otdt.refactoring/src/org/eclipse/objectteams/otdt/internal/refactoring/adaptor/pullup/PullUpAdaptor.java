/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010, 2012 Johannes Gebauer and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 *		Johannes Gebauer - Initial API and implementation
 *		Stephan Herrmann - Bug fixes and improvements
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.refactoring.adaptor.pullup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.BindingKey;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite.ImportRewriteContext;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.MethodDeclarationMatch;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.corext.codemanipulation.ContextSensitiveImportRewriteContext;
import org.eclipse.jdt.internal.corext.dom.ASTNodeFactory;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.corext.refactoring.Checks;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.jdt.internal.corext.refactoring.base.JavaStatusContext;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.jdt.internal.corext.refactoring.structure.ImportRewriteUtil;
import org.eclipse.jdt.internal.corext.refactoring.structure.MemberVisibilityAdjustor.IncomingMemberVisibilityAdjustment;
import org.eclipse.jdt.internal.corext.refactoring.structure.TypeVariableMaplet;
import org.eclipse.jdt.internal.corext.refactoring.typeconstraints.types.TType;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.JdtFlags;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.corext.util.SearchUtils;
import org.eclipse.jdt.internal.ui.javaeditor.ASTProvider;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.ltk.core.refactoring.GroupCategorySet;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.RefactoringStatusEntry;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.objectteams.otdt.core.ICallinMapping;
import org.eclipse.objectteams.otdt.core.ICalloutMapping;
import org.eclipse.objectteams.otdt.core.ICalloutToFieldMapping;
import org.eclipse.objectteams.otdt.core.IMethodMapping;
import org.eclipse.objectteams.otdt.core.IOTJavaElement;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.core.TypeHelper;
import org.eclipse.objectteams.otdt.core.hierarchy.OTTypeHierarchies;
import org.eclipse.objectteams.otdt.internal.core.AbstractCalloutMapping;
import org.eclipse.objectteams.otdt.internal.core.CalloutMapping;
import org.eclipse.objectteams.otdt.internal.core.CalloutToFieldMapping;
import org.eclipse.objectteams.otdt.internal.core.RoleType;
import org.eclipse.objectteams.otdt.internal.refactoring.RefactoringMessages;
import org.eclipse.objectteams.otdt.internal.refactoring.util.IAmbuguityMessageCreator;
import org.eclipse.objectteams.otdt.internal.refactoring.util.IOverloadingMessageCreator;
import org.eclipse.objectteams.otdt.internal.refactoring.util.RefactoringUtil;
import org.eclipse.osgi.util.NLS;
import org.eclipse.text.edits.TextEditGroup;

import base org.eclipse.jdt.internal.corext.refactoring.RefactoringAvailabilityTester;
import base org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil;
import base org.eclipse.jdt.internal.corext.refactoring.structure.HierarchyProcessor;
import base org.eclipse.jdt.internal.corext.refactoring.structure.MemberVisibilityAdjustor;
import base org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor;
import base org.eclipse.jdt.internal.corext.refactoring.structure.ReferenceFinderUtil;
import base org.eclipse.jdt.internal.corext.refactoring.structure.constraints.SuperTypeRefactoringProcessor;
import base org.eclipse.jdt.internal.ui.refactoring.PullUpMemberPage;
import base org.eclipse.jdt.internal.ui.refactoring.PullUpMemberPage.MemberActionInfo;

/**
 * @author Johannes Gebauer
 * 
 */
@SuppressWarnings({ "restriction", "decapsulation" }) // private base classes
public team class PullUpAdaptor {
	
	protected team class PullUpRefactoringProcessorRole playedBy PullUpRefactoringProcessor {
		

		public ChangeManagerDetails changeManagerDetails; // set when this team is activated (see #createChangeManager())


		// callout (constant):
		GroupCategorySet getSET_PULL_UP() -> get GroupCategorySet SET_PULL_UP;
		// callouts (fields):
		void setFCachedSkippedSuperTypes(Set<IType> fCachedSkippedSuperTypes) 
																-> set Set<IType> fCachedSkippedSuperTypes;
		Set<IType> getFCachedSkippedSuperTypes() 				-> get Set<IType> fCachedSkippedSuperTypes;
		IMember[] getMembersToDelete(IProgressMonitor monitor) 	-> IMember[] getMembersToDelete(IProgressMonitor monitor);
		IMethod[] getFDeletedMethods() 							-> get IMethod[] fDeletedMethods;
		IMember[] getFMembersToMove() 							-> get IMember[] fMembersToMove;

		// callouts (methods):
		IType getDestinationType() 								-> IType getDestinationType();
		IMember[] getMembersToMove() 							-> IMember[] getMembersToMove();
		IType getDeclaringType() 								-> IType getDeclaringType();
		ITypeHierarchy getDestinationTypeHierarchy(IProgressMonitor pm)
																-> ITypeHierarchy getDestinationTypeHierarchy(IProgressMonitor pm);
		
		private void checkFinalConditions(IProgressMonitor pm, CheckConditionsContext context, RefactoringStatus status) throws CoreException {
			
			status.merge(checkForAspectBindings(pm));
			status.merge(checkOverloadingAndAmbiguity(pm));
			status.merge(checkOverriding(pm));
			if(TypeHelper.isRole(getDeclaringType().getFlags())){
				status.merge(checkDestinationForOTElements(pm));
				status.merge(checkShadowingFieldInImplicitHierarchy(pm));
			}
		}
		
		void checkFinalConditions(IProgressMonitor pm, CheckConditionsContext context, RefactoringStatus status) <- after RefactoringStatus checkFinalConditions(IProgressMonitor pm,
				CheckConditionsContext context) with {
			pm <- pm,
			context <- context,
			status <- result
		}

		private RefactoringStatus checkShadowingFieldInImplicitHierarchy(IProgressMonitor pm) throws JavaModelException {
			RefactoringStatus status = new RefactoringStatus();
			if(!TypeHelper.isRole(getDestinationType().getFlags())){
				return status;
			}
			
			ITypeHierarchy hier = getDestinationTypeHierarchy(pm);
			ArrayList<IType> implicitSubRoles = new ArrayList<IType>();
			implicitSubRoles.addAll(Arrays.asList(OTTypeHierarchies.getInstance().getAllTSubTypes(hier, getDestinationType())));
			
			// remove the subtypes of the declaring type
			implicitSubRoles.removeAll(Arrays.asList(OTTypeHierarchies.getInstance().getAllTSubTypes(hier, getDeclaringType())));
			
			pm.beginTask(RefactoringMessages.PullUpAdaptor_checkShadowing_progress, implicitSubRoles.size());
			pm.subTask(""); //$NON-NLS-1$
			
			for (int i = 0; i < getFMembersToMove().length; i++) {
				IMember element = getFMembersToMove()[i];
				if(element instanceof IField){
					IField field = (IField) element;
					for (IType type : implicitSubRoles) {
						IField shadowingField = RefactoringUtil.fieldIsShadowedInType(field.getElementName(), field.getTypeSignature(), type);
						if(shadowingField != null){
							
							ArrayList<IMember> membersToDelete = new ArrayList<IMember>();
							membersToDelete.addAll(Arrays.asList(getMembersToDelete(pm)));
							// do not indicate shadowing by deleted fields as an error
							if(!membersToDelete.contains(shadowingField)) {
								String msg = NLS.bind(RefactoringMessages.PullUpAdaptor_fieldShadowing_error, 
													  field.getElementName(), 
													  type.getFullyQualifiedName('.'));
								status.addFatalError(msg, JavaStatusContext.create(shadowingField));
							}
							
						}
						pm.worked(1);
						// do not repeat errors in hierarchy
						if(status.hasFatalError()){
							pm.done();
							return status;
						}
					}
				}
			}
			pm.done();
			return status;
			
		}
		
		private RefactoringStatus checkDestinationForOTElements(IProgressMonitor pm) throws JavaModelException {
			RefactoringStatus status = new RefactoringStatus();
			for (int i = 0; i < getFMembersToMove().length; i++) {
				IMember element = getFMembersToMove()[i];
				if (element instanceof IMethod){
					IMethod method = (IMethod)element;
					// callin methods can only be moved to roles
					if(Flags.isCallin(method.getFlags()) && !TypeHelper.isRole(getDestinationType().getFlags())){
						String msg = NLS.bind(RefactoringMessages.PullUpAdaptor_callinMethodToNonRole_error, method.getElementName());
						status.addFatalError(msg, JavaStatusContext.create(method));
					} else {
						// for callout bindings check if the base member will be accessible after refactoring:
						checkDestinationForCallout(method, getDestinationType(), status, pm);
					}
				}
			}
			return status;
		}
		
		protected static void checkDestinationForCallout(IMethod method, IType destinationType, RefactoringStatus status, IProgressMonitor pm) throws JavaModelException {
			IMember baseMember = null;
			switch (method.getElementType()) {
			case IOTJavaElement.CALLOUT_MAPPING:
				baseMember = ((CalloutMapping)method).getBoundBaseMethod();
				break;
			case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
				baseMember = ((CalloutToFieldMapping)method).getBoundBaseField();
				break;
			}
			if (baseMember == null) return; // not a callout or unresolvable base member
			IType requiredBaseType = baseMember.getDeclaringType();
			if (!TypeHelper.isRole(destinationType.getFlags())) {
				String msg = NLS.bind(RefactoringMessages.PullUpAdaptor_calloutToNonRole_error, method.getElementName());
				status.addFatalError(msg, JavaStatusContext.create(method));
			} else {
				IRoleType roleType = (IRoleType) OTModelManager.getOTElement(destinationType);
				IType baseClass = roleType.getBaseClass();
				if (baseClass == null) {
					String msg = NLS.bind(RefactoringMessages.PullUpAdaptor_calloutToUnboundRole_error, method.getElementName());
					status.addFatalError(msg, JavaStatusContext.create(method));									
				} else if (!requiredBaseType.equals(baseClass)) {
					IJavaElement[] elements = new IJavaElement[]{requiredBaseType, baseClass};
					ASTParser astParser = ASTParser.newParser(ASTProvider.SHARED_AST_LEVEL);
					astParser.setProject(method.getDeclaringType().getJavaProject());
					IBinding[] bindings = astParser.createBindings(elements, pm);
					ITypeBinding requiredBaseBinding = (ITypeBinding)bindings[0];
					ITypeBinding baseOfDestination = (ITypeBinding)bindings[1];
					if (!baseOfDestination.isAssignmentCompatible(requiredBaseBinding)) {
						String msg = NLS.bind(RefactoringMessages.PullUpAdaptor_calloutBaseNotBoundInDest_error, 
												new Object[]{method.getElementName(), destinationType.getElementName(), baseMember.getElementName()});
						status.addFatalError(msg, JavaStatusContext.create(method));											
					}
				}
			}
		}

		private RefactoringStatus checkOverloadingAndAmbiguityInType(IProgressMonitor pm, IType type) throws JavaModelException {
			RefactoringStatus status = new RefactoringStatus();
			ITypeHierarchy hier = getDestinationTypeHierarchy(pm);
			for (int i = 0; i < getFMembersToMove().length; i++) {
				IMember element = getFMembersToMove()[i];
				if (element instanceof IMethod){
					final IMethod method = (IMethod)element;
					String[] paramTypes = method.getParameterTypes();
					status.merge(RefactoringUtil.checkOverloadingAndAmbiguity(type, hier, method.getElementName(), paramTypes,
							new IAmbuguityMessageCreator() {

						public String createAmbiguousMethodSpecifierMsg() {
							return RefactoringMessages.PullUpAdaptor_ambiguousMethodSpec_error;
						}

					}, new IOverloadingMessageCreator() {

						public String createOverloadingMessage() {
							String msg = NLS.bind(RefactoringMessages.PullUpAdaptor_overloading_error, method.getElementName());
							return msg;
						}

					}, pm));
				}
			}
			return status;
		}
		
		private RefactoringStatus checkOverloadingAndAmbiguity(IProgressMonitor pm) throws JavaModelException {
			
			ITypeHierarchy destinationTypeHierarchy = getDestinationTypeHierarchy(pm);
			IType[] subtypes = destinationTypeHierarchy.getAllSubtypes(getDestinationType());
			
			pm.beginTask(RefactoringMessages.PullUpAdaptor_checkOverloading_progress, subtypes.length + 1);
			pm.subTask(""); //$NON-NLS-1$
			
			RefactoringStatus status = new RefactoringStatus();
			
			// check overloading in destination type
			status.merge(checkOverloadingAndAmbiguityInType(pm, getDestinationType()));
			
			pm.worked(1);
			
			// do not repeat errors in hierarchy
			if(status.hasFatalError()){
				pm.done();
				return status;
			}
			
			// check overloading in subtypes of the destination type
			for (int i = 0; i < subtypes.length; i++) {
				status.merge(checkOverloadingAndAmbiguityInType(pm, subtypes[i]));
				
				pm.worked(1);
				
				// do not repeat errors in hierarchy
				if(status.hasFatalError()){
					pm.done();
					return status;
				}
			}
			pm.done();
			return status;
		}
		
		private RefactoringStatus checkOverriding(IProgressMonitor pm) throws JavaModelException{
			RefactoringStatus status = new RefactoringStatus();
			
			ITypeHierarchy hier = getDestinationTypeHierarchy(pm);
			ArrayList<IType> allSubTypes = new ArrayList<IType>();
			allSubTypes.addAll(Arrays.asList(hier.getAllSubtypes(getDestinationType())));
			
			// remove the subtypes of the declaring type
			allSubTypes.removeAll(Arrays.asList(hier.getAllSubtypes(getDeclaringType())));
			
			pm.beginTask(RefactoringMessages.PullUpAdaptor_checkOverriding_progress, allSubTypes.size());
			pm.subTask(""); //$NON-NLS-1$
			
			for (int i = 0; i < getFMembersToMove().length; i++) {
				IMember element = getFMembersToMove()[i];
				if(element instanceof IMethod){
					IMethod method = (IMethod) element;
					for (IType type : allSubTypes) {
						IMethod overridingMethod = methodIsOverriddenInType(method.getElementName(), method.getParameterTypes(), type);
						if(overridingMethod != null){
							
							ArrayList<IMember> membersToMove = new ArrayList<IMember>();
							membersToMove.addAll(Arrays.asList(getMembersToMove()));
							ArrayList<IMember> membersToDelete = new ArrayList<IMember>();
							membersToDelete.addAll(Arrays.asList(getMembersToDelete(pm)));
							// do not indicate overriding for deleted/moved methods as an error
							if(!membersToMove.contains(overridingMethod) && !membersToDelete.contains(overridingMethod)) {
								String msg = NLS.bind(RefactoringMessages.PullUpAdaptor_overriding_error,
													  method.getElementName(),
													  type.getFullyQualifiedName('.'));
								status.addError(msg, JavaStatusContext.create(overridingMethod));
							}
							
						}
						pm.worked(1);
						// do not repeat errors in hierarchy
						if(status.hasError()){
							pm.done();
							return status;
						}
					}
				}
			}
			pm.done();
			return status;
		}
		
		private IMethod methodIsOverriddenInType(String methodName, String[] paramTypes, IType type){
			IMethod method = type.getMethod(methodName, paramTypes);
			if(method.exists()){
				return method;
			}
			return null;
		}
		
		
		/**
		 * Searches for aspect bindings that reference members to be moved.
		 * @return The refactoring status contains warnings if any referencing aspect bindings exist.
		 */
		private RefactoringStatus checkForAspectBindings(IProgressMonitor monitor) throws CoreException {
			// search all references for the members to be moved
			IMember[] membersToMove = getMembersToMove();
			final HashMap<IMember,Set<SearchMatch>> references= new HashMap<IMember,Set<SearchMatch>>();
			IJavaSearchScope scope= SearchEngine.createWorkspaceScope();
			for (int i = 0; i < membersToMove.length; i++) {
				final IMember member = membersToMove[i];
				// may not be able to access return type, but IGNORE_RETURN_TYPE is only effective with ALL_OCCURRENCES:
				int limitTo = IJavaSearchConstants.ALL_OCCURRENCES|IJavaSearchConstants.IGNORE_RETURN_TYPE;
				SearchPattern pattern= SearchPattern.createPattern(member, limitTo, SearchPattern.R_EXACT_MATCH);
				SearchEngine engine= new SearchEngine();
				engine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant()}, scope, new SearchRequestor() {
					public void acceptSearchMatch(SearchMatch match) throws CoreException {
						if (   match.getAccuracy() == SearchMatch.A_ACCURATE
							&& !match.isInsideDocComment()
							&& !(match instanceof MethodDeclarationMatch)) // limit to references despite ALL_OCCURRENCES above:
						{
							if(references.get(member) == null){
								Set<SearchMatch> refSet = new HashSet<SearchMatch>();
								refSet.add(match);
								references.put(member, refSet);
							}else{
								references.get(member).add(match);
							}
						}
					}
				}, monitor);
			}
			
			// search the matches for aspect bindings
			RefactoringStatus status = new RefactoringStatus();
			for (int i = 0; i < membersToMove.length; i++) {
				IMember member = membersToMove[i];
				Set<SearchMatch> refSet = references.get(member);
				if(refSet == null){
					continue;
				}
				for (SearchMatch match : refSet) {
					Object element= match.getElement();
					if (element instanceof ICalloutMapping) {
						ICalloutMapping mapping = (ICalloutMapping) element;
						IMethod boundBaseMethod = mapping.getBoundBaseMethod();
						if (boundBaseMethod == null) {
							addUnresolvedBaseMemberError(mapping, status, member);
						} else if(boundBaseMethod.equals(member)){
							addAspectBindingWarning(member, status, mapping);
						}
					}
					if (element instanceof ICalloutToFieldMapping) {
						ICalloutToFieldMapping mapping = (ICalloutToFieldMapping) element;
						IField boundBaseField = mapping.getBoundBaseField();
						if (boundBaseField == null) {
							addUnresolvedBaseMemberError(mapping, status, member);
						} else if(boundBaseField.equals(member)){
							addAspectBindingWarning(member, status, mapping);
						}
					}
					if (element instanceof ICallinMapping) {
						ICallinMapping mapping = (ICallinMapping) element;
						for (int j = 0; j < mapping.getBoundBaseMethods().length; j++) {
							if(mapping.getBoundBaseMethods()[i].equals(member)){
								addAspectBindingWarning(member, status, mapping);
								break;
							}
						}
					}	
				}
			}
			return status;
		}
		
		private void addUnresolvedBaseMemberError(IMethodMapping referencedMapping, RefactoringStatus status, IMember member) 
		{
			status.addEntry(new RefactoringStatusEntry(RefactoringStatus.ERROR,
					NLS.bind(RefactoringMessages.PullUpAdaptor_referencedCalloutUnresolvedBaseMember_error,
							 referencedMapping.getElementName(),
							 member.getElementName())));
		}

		/**
		 * Adds a warning to the given refactoring status that notifies the user about existing aspect bingings.
		 * 
		 * @param member the member to be pulled up.
		 * @param status the refactoring status, where the warning should be added.
		 * @param mapping the method mapping that causes the warning.
		 */
		private void addAspectBindingWarning(IMember member, RefactoringStatus status,IMethodMapping mapping){
			status.addEntry(new RefactoringStatusEntry(RefactoringStatus.WARNING,
					NLS.bind(RefactoringMessages.PullUpAdaptor_referencedByMethodBinding_error,
							 member.getElementName(), 
							 mapping.getDeclaringType().getFullyQualifiedName())));
		
		}
		
		/**
		 * Adds implicit role super types for roles. Candidate types are the possible super types,
		 * that are available as the destination type.
		 */
		callin IType[] getCandidateTypes(final RefactoringStatus status, final IProgressMonitor monitor) throws JavaModelException{
			RefactoringStatus jdtStatus = new RefactoringStatus();
			IType[] jdtCandidates = base.getCandidateTypes(jdtStatus, monitor);
			final IType declaring= getDeclaringType();
			// try to get the corresponding OTType that is represented in the declaring type
			IOTType otType = OTModelManager.getOTElement(declaring);
			if(otType != null && otType instanceof RoleType){
				RoleType role = (RoleType) otType;
				IType[] otSuperTypes = role.newTypeHierarchy(monitor).getAllSupertypes(declaring);
				List<IType> list = new ArrayList<IType>();
				int binary = 0;
				for (int i = 0; i < otSuperTypes.length; i++) {
					IType type = otSuperTypes[i];
					if(OTModelManager.hasOTElementFor(otSuperTypes[i])){
						IOTType otSuperType = OTModelManager.getOTElement(type);
						if(otSuperType instanceof RoleType && type != null && type.exists() && !type.isReadOnly() && !type.isBinary()){
							list.add(type);
						}else if(type.isBinary()){
							binary += 1;
						}
					}
				}
				if(!list.isEmpty()){
					if(jdtStatus.hasFatalError()){
						if (otSuperTypes.length == binary)
							status.addFatalError(RefactoringCoreMessages.PullUPRefactoring_no_all_binary);
					}else{
						status.merge(jdtStatus);
					}
					Collections.reverse(list);
					list.addAll(0,Arrays.asList(jdtCandidates));
					return list.toArray(new IType[list.size()]);
				}
			}
			status.merge(jdtStatus);
			return jdtCandidates;
		}

		getCandidateTypes <- replace getCandidateTypes;
		
		
		
		/**
		 * Prevents callin methods from visibility adjustments.
		 */
		callin boolean needsVisibilityAdjustment(final IMember member, final boolean references, final IProgressMonitor monitor, final RefactoringStatus status) throws JavaModelException {
			boolean result = base.needsVisibilityAdjustment(member, references, monitor, status);
			if(Flags.isCallin(member.getFlags())){
				return false;
			}else{
				return result;
			}
		}
		
		needsVisibilityAdjustment <- replace needsVisibilityAdjustment;

		void createAbstractMethod(IMethod sourceMethod, CompilationUnitRewrite sourceRewriter, CompilationUnit declaringCuNode, AbstractTypeDeclaration destination, TypeVariableMaplet[] mapping, CompilationUnitRewrite targetRewrite, Map<IMember, IncomingMemberVisibilityAdjustment> adjustments, IProgressMonitor monitor, RefactoringStatus status)
		<- replace void createAbstractMethod(IMethod sourceMethod, CompilationUnitRewrite sourceRewriter, CompilationUnit declaringCuNode, AbstractTypeDeclaration destination, TypeVariableMaplet[] mapping, CompilationUnitRewrite targetRewrite, Map<IMember, IncomingMemberVisibilityAdjustment> adjustments, IProgressMonitor monitor, RefactoringStatus status)
			base when (sourceMethod instanceof IMethodMapping);

		/** support creation of an abstract method from a callout mapping. */
		@SuppressWarnings({ "inferredcallout", "basecall" })
		callin void createAbstractMethod(IMethod sourceMethod, CompilationUnitRewrite sourceRewriter, CompilationUnit declaringCuNode, AbstractTypeDeclaration destination, TypeVariableMaplet[] mapping, CompilationUnitRewrite targetRewrite, Map<IMember, IncomingMemberVisibilityAdjustment> adjustments, IProgressMonitor monitor, RefactoringStatus status)
				throws JavaModelException 
		{
			// search callout decl, not method decl:
			final CalloutMappingDeclaration oldCallout= getCalloutDeclarationNode(sourceMethod, declaringCuNode);
			if (JavaModelUtil.is50OrHigher(sourceMethod.getJavaProject()) && (fSettings.overrideAnnotation || JavaCore.ERROR.equals(sourceMethod.getJavaProject().getOption(JavaCore.COMPILER_PB_MISSING_OVERRIDE_ANNOTATION, true)))) {
				final MarkerAnnotation annotation= sourceRewriter.getAST().newMarkerAnnotation();
				annotation.setTypeName(sourceRewriter.getAST().newSimpleName("Override")); //$NON-NLS-1$
				sourceRewriter.getASTRewrite().getListRewrite(oldCallout, CalloutMappingDeclaration.MODIFIERS2_PROPERTY).insertFirst(annotation, sourceRewriter.createCategorizedGroupDescription(RefactoringCoreMessages.PullUpRefactoring_add_override_annotation, SET_PULL_UP));
			}
			final MethodDeclaration newMethod= targetRewrite.getAST().newMethodDeclaration();
			newMethod.setBody(null);
			newMethod.setConstructor(false);
// callout has no extra dimensions:
//			newMethod.setExtraDimensions(oldCallout.getExtraDimensions());
			newMethod.setJavadoc(null);
			int modifiers= getModifiersWithUpdatedVisibility(sourceMethod, Modifier.ABSTRACT | JdtFlags.clearFlag(Modifier.NATIVE | Modifier.FINAL, sourceMethod.getFlags()), adjustments, monitor, false, status);
// callout doesn't support varargs syntax:
//			if (oldCallout.isVarargs())
//				modifiers&= ~Flags.AccVarargs;
			newMethod.modifiers().addAll(ASTNodeFactory.newModifiers(targetRewrite.getAST(), modifiers));
			newMethod.setName(((SimpleName) ASTNode.copySubtree(targetRewrite.getAST(), oldCallout.getRoleMappingElement().getName())));
			copyReturnType(targetRewrite.getASTRewrite(), getDeclaringType().getCompilationUnit(), (MethodSpec)oldCallout.getRoleMappingElement(), newMethod, mapping);
			copyParameters(targetRewrite.getASTRewrite(), getDeclaringType().getCompilationUnit(), (MethodSpec)oldCallout.getRoleMappingElement(), newMethod, mapping);
// callout does not declare exceptions:
//			copyThrownExceptions(oldCallout, newMethod);
			ImportRewriteContext context= new ContextSensitiveImportRewriteContext(destination, targetRewrite.getImportRewrite());
			ImportRewriteUtil.addImports(targetRewrite, context, newMethod, new HashMap<Name, String>(), new HashMap<Name, String>(), false);
			targetRewrite.getASTRewrite().getListRewrite(destination, destination.getBodyDeclarationsProperty()).insertAt(newMethod, ASTNodes.getInsertionIndex(newMethod, destination.bodyDeclarations()), targetRewrite.createCategorizedGroupDescription(RefactoringCoreMessages.PullUpRefactoring_add_abstract_method, SET_PULL_UP));
		}

		createChangeManager <- replace createChangeManager;
		@SuppressWarnings("inferredcallout")
		callin void createChangeManager(IProgressMonitor monitor, RefactoringStatus status) throws CoreException {
			IType destinationType = getDestinationType();
			IType declaringType = getDeclaringType();
			this.changeManagerDetails = new ChangeManagerDetails(getCompilationUnitRewrite(fCompilationUnitRewrites, declaringType.getCompilationUnit()),
																 getCompilationUnitRewrite(fCompilationUnitRewrites, destinationType.getCompilationUnit()),
																 declaringType,
																 destinationType, 
																 status,
																 monitor);
			within (this)
				base.createChangeManager(monitor, status);
		}

		/** during createChangeManager we add callouts to the declaration nodes to be removed. */
		protected team class HierarchyProcRole playedBy HierarchyProcessor {
			// Note: when defined in role PullUpRefactoringProcessorRole this currently triggers a VerifyError
			
			// === for removal of old callout declarations: ===
			List<ASTNode> getDeclarationNodes(CompilationUnit cuNode, List<IMember> members) 
			<- replace  List<ASTNode> getDeclarationNodes(CompilationUnit cuNode, List<IMember> members);
				
			static callin List<ASTNode> getDeclarationNodes(CompilationUnit cuNode, List<IMember> members)
					throws JavaModelException 
			{
				List<ASTNode> result = base.getDeclarationNodes(cuNode, members);
				for (IMember member : members) {
					ASTNode node= null;
					if (member instanceof AbstractCalloutMapping)
						node= getCalloutDeclarationNode((IMethod) member, cuNode);
					if (node != null)
						result.add(node);
				}
				return result;
			}
		}

		/** during createChangeManager we intercept the visibility adjustments: */
		protected class Adjustor playedBy MemberVisibilityAdjustor {
			setAdjustments <- after setAdjustments;

			private void setAdjustments(Map<IMember, IncomingMemberVisibilityAdjustment> map) {
				PullUpRefactoringProcessorRole.this.changeManagerDetails.adjustments = map;
			}
		}

		/** during createChangeManager we hook into getMethodDeclarationNode to handle callouts, too: */
		protected class CalloutUtil playedBy ASTNodeSearchUtil {
			getMethodDeclarationNode <- replace getMethodDeclarationNode
				base when (!PullUpRefactoringProcessorRole.this.isExecutingCallin());  // inhibit callin trigger during getDeclarationNodes()

			@SuppressWarnings("basecall")
			static callin MethodDeclaration getMethodDeclarationNode(IMethod iMethod, CompilationUnit compilationUnit) throws JavaModelException {
				if (iMethod instanceof AbstractCalloutMapping) {
					// cf block inside org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor.createChangeManager()
					// which starts "} else if (member instanceof IMethod) {":
					CalloutMappingDeclaration oldCallout = getCalloutDeclarationNode(iMethod, compilationUnit);
					if (oldCallout != null) {
						CompilationUnitRewrite sourceRewriter = PullUpRefactoringProcessorRole.this.changeManagerDetails.sourceRewrite;
						CompilationUnitRewrite rewrite = PullUpRefactoringProcessorRole.this.changeManagerDetails.rewrite;
						AbstractTypeDeclaration declaration = PullUpRefactoringProcessorRole.this.changeManagerDetails.declaration;
						ImportRewriteContext context = PullUpRefactoringProcessorRole.this.changeManagerDetails.context;
						RefactoringStatus status = PullUpRefactoringProcessorRole.this.changeManagerDetails.status;
						TypeVariableMaplet[] mapping = PullUpRefactoringProcessorRole.this.changeManagerDetails.mapping;
						Map<IMember, IncomingMemberVisibilityAdjustment> adjustments = PullUpRefactoringProcessorRole.this.changeManagerDetails.adjustments;
						ASTRewrite rewriter = rewrite.getASTRewrite();
						IProgressMonitor subsub = PullUpRefactoringProcessorRole.this.changeManagerDetails.monitor;
						
						if (JdtFlags.isStatic(iMethod) && getDestinationType().isInterface())
							status.merge(RefactoringStatus.createErrorStatus(Messages.format(RefactoringCoreMessages.PullUpRefactoring_moving_static_method_to_interface, new String[] { JavaElementLabels.getTextLabel(iMethod, JavaElementLabels.ALL_FULLY_QUALIFIED)}), JavaStatusContext.create(iMethod)));
						CalloutMappingDeclaration newMethod= createNewCalloutDeclarationNode(sourceRewriter, rewrite, iMethod, oldCallout, mapping, adjustments, new SubProgressMonitor(subsub, 1), status);
						rewriter.getListRewrite(declaration, declaration.getBodyDeclarationsProperty()).insertAt(newMethod, ASTNodes.getInsertionIndex(newMethod, declaration.bodyDeclarations()), rewrite.createCategorizedGroupDescription(RefactoringCoreMessages.HierarchyRefactoring_add_member, getSET_PULL_UP()));
						ImportRewriteUtil.addImports(rewrite, context, oldCallout, new HashMap<Name, String>(), new HashMap<Name, String>(), false);

					}
					return null;
				}
				return base.getMethodDeclarationNode(iMethod, compilationUnit);
			}
		}
		
		// ==== COPY&PASTE from base class, use MethodSpec as a template rather then MethodDeclaration: ====
		
		@SuppressWarnings("inferredcallout")
		protected void copyReturnType(ASTRewrite rewrite, ICompilationUnit unit, MethodSpec oldMethod, IMethodNode newMethod, TypeVariableMaplet[] mapping) throws JavaModelException {
			Type newReturnType= null;
			if (mapping.length > 0)
				newReturnType= createPlaceholderForType(oldMethod.getReturnType2(), unit, mapping, rewrite);
			else
				newReturnType= createPlaceholderForType(oldMethod.getReturnType2(), unit, rewrite);
			newMethod.setReturnType2(newReturnType);
		}

		@SuppressWarnings("inferredcallout")
		protected void copyParameters(ASTRewrite rewrite, ICompilationUnit unit, MethodSpec oldMethod, IMethodNode newMethod, TypeVariableMaplet[] mapping) throws JavaModelException {
			SingleVariableDeclaration newDeclaration= null;
			for (int index= 0, size= oldMethod.parameters().size(); index < size; index++) {
				final SingleVariableDeclaration oldDeclaration= (SingleVariableDeclaration) oldMethod.parameters().get(index);
				if (mapping.length > 0)
					newDeclaration= createPlaceholderForSingleVariableDeclaration(oldDeclaration, unit, mapping, rewrite);
				else
					newDeclaration= createPlaceholderForSingleVariableDeclaration(oldDeclaration, unit, rewrite);
				newMethod.parameters().add(newDeclaration);
			}
		}

		@SuppressWarnings("inferredcallout")
		CalloutMappingDeclaration createNewCalloutDeclarationNode(CompilationUnitRewrite sourceRewrite, CompilationUnitRewrite targetRewrite, 
				IMethod sourceMethod, CalloutMappingDeclaration oldCallout, TypeVariableMaplet[] mapping, Map<IMember, IncomingMemberVisibilityAdjustment> adjustments, IProgressMonitor monitor, RefactoringStatus status) 
				throws JavaModelException 
		{
			final ASTRewrite rewrite= targetRewrite.getASTRewrite();
			final AST ast= rewrite.getAST();
			final CalloutMappingDeclaration newCallout= ast.newCalloutMappingDeclaration();
// TODO: copy parameter mappings:
//			if (!getDestinationType().isInterface())
//				copyBodyOfPulledUpMethod(sourceRewrite, targetRewrite, sourceMethod, oldCallout, newMethod, mapping, monitor);
			copyJavadocNode(rewrite, oldCallout, newCallout);
			int modifiers= getModifiersWithUpdatedVisibility(sourceMethod, sourceMethod.getFlags(), adjustments, monitor, true, status);

// TODO
//			copyAnnotations(oldCallout, newMethod);
			newCallout.modifiers().addAll(ASTNodeFactory.newModifiers(ast, modifiers));
			newCallout.setRoleMappingElement(createNewMethodSpec(ast, rewrite, mapping, (MethodSpec) oldCallout.getRoleMappingElement()));
			MethodMappingElement baseMappingElement = oldCallout.getBaseMappingElement();
			if (baseMappingElement instanceof MethodSpec) {
				newCallout.setBaseMappingElement(createNewMethodSpec(ast, rewrite, mapping, (MethodSpec) baseMappingElement));
			} else {
				newCallout.setBaseMappingElement(createNewFieldSpec(ast, rewrite, mapping, (FieldAccessSpec) baseMappingElement));
				newCallout.bindingOperator().setBindingModifier(ast.newModifier(oldCallout.bindingOperator().bindingModifier().getKeyword()));
			}
			newCallout.setSignatureFlag(oldCallout.hasSignature());
			return newCallout;
		}
		MethodSpec createNewMethodSpec(AST ast, ASTRewrite rewrite, TypeVariableMaplet[] mapping, MethodSpec oldMethodSpec) throws JavaModelException {
			MethodSpec newMethodSpec = ast.newMethodSpec();
			newMethodSpec.setName(((SimpleName) ASTNode.copySubtree(ast, oldMethodSpec.getName())));
			if (oldMethodSpec.hasSignature()) {
				copyReturnType(rewrite, getDeclaringType().getCompilationUnit(), oldMethodSpec, newMethodSpec, mapping);
				copyParameters(rewrite, getDeclaringType().getCompilationUnit(), oldMethodSpec, newMethodSpec, mapping);
				newMethodSpec.setSignatureFlag(oldMethodSpec.hasSignature());
//				copyTypeParameters(oldMethodSpec, newMethodSpec);
			}
			return newMethodSpec;
		}
		@SuppressWarnings("inferredcallout")
		FieldAccessSpec createNewFieldSpec(AST ast, ASTRewrite rewrite, TypeVariableMaplet[] mapping, FieldAccessSpec oldFieldSpec) throws JavaModelException {
			FieldAccessSpec newFieldSpec = ast.newFieldAccessSpec();
			newFieldSpec.setName(((SimpleName) ASTNode.copySubtree(ast, oldFieldSpec.getName())));
			if (oldFieldSpec.hasSignature()) {
				Type newFielType= null;
				if (mapping.length > 0)
					newFielType= createPlaceholderForType(oldFieldSpec.getFieldType(), getDeclaringType().getCompilationUnit(), mapping, rewrite);
				else
					newFielType= createPlaceholderForType(oldFieldSpec.getFieldType(), getDeclaringType().getCompilationUnit(), rewrite);
				newFieldSpec.setFieldType(newFielType);
				newFieldSpec.setSignatureFlag(oldFieldSpec.hasSignature());
			}
			return newFieldSpec;
		}
	}

	static CalloutMappingDeclaration getCalloutDeclarationNode(IMethod iMethod, CompilationUnit cuNode) throws JavaModelException {
		ASTNode node = NodeFinder.perform(cuNode, iMethod.getNameRange());
		return (CalloutMappingDeclaration)ASTNodes.getParent(node, CalloutMappingDeclaration.class);
	}

	/**
	 * Enable pull-up also for callout mappings. 
	 */
	protected class Availability playedBy RefactoringAvailabilityTester {

		boolean isPullUpAvailable(IMember member) <- replace boolean isPullUpAvailable(IMember member);

		static callin boolean isPullUpAvailable(IMember member) throws JavaModelException {
			if (base.isPullUpAvailable(member))
				return true;
			int kind = member.getElementType();
			switch (kind) {
			case IOTJavaElement.CALLOUT_MAPPING:
			case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
				// extract from base method:
				if (!member.exists())
					return false;
				if (!Checks.isAvailable(member))
					return false;
				return true;
			default:
				return false;
			}
		}

		IMember[] getPullUpMembers(IType type) <- replace IMember[] getPullUpMembers(IType type);

		static callin IMember[] getPullUpMembers(IType type) throws JavaModelException {
			IMember[] result = base.getPullUpMembers(type);
			if (OTModelManager.isRole(type)) {
				List<IMember> callouts = new ArrayList<IMember>();
				for (IJavaElement elem : type.getChildren()) {
					switch (elem.getElementType()) {
					case IOTJavaElement.CALLOUT_MAPPING:
					case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
						callouts.add((IMember) elem);
					}
				}
				if (callouts.size() > 0) {
					int l1 = result.length, l2 = callouts.size();
					IMember[] combined = new IMember[l1+l2];
					callouts.toArray(combined);
					System.arraycopy(result, 0, combined, l2, l1);
					return combined;
				}
			}
			return result;
		}		
	}

	/** Visibility checking for callout mappings. */
	protected class Visibility playedBy MemberVisibilityAdjustor {

		IJavaElement getFReferencing() -> get IJavaElement fReferencing;

		void adjustOutgoingVisibility(IMember member, ModifierKeyword threshold, String template)
		-> void adjustOutgoingVisibility(IMember member, ModifierKeyword threshold, String template);

		ModifierKeyword thresholdTypeToMethod(IType referencing, IMethod referenced, IProgressMonitor monitor)
		-> ModifierKeyword thresholdTypeToMethod(IType referencing, IMethod referenced, IProgressMonitor monitor);
		
		checkOTMember <- replace adjustOutgoingVisibilityChain;

		@SuppressWarnings("basecall")
		callin void checkOTMember(IMember member, IProgressMonitor monitor) throws JavaModelException {
			try {
				base.checkOTMember(member, monitor);
			} catch (JavaModelException jme) {
				// when a MethodDeclarationMatch reports a SourceMethod representation actually a callout mapping
				// the method answers that it doesn't exist, that's what we intercept here:
				if (jme.getJavaModelStatus().isDoesNotExist()) {
					IType type = member.getDeclaringType();
					IOTType ottype = OTModelManager.getOTElement(type);
					if (ottype != null) {
						if (ottype.isRole() && member instanceof IMethod) {
							for (IMethodMapping map : ((IRoleType)ottype).getMethodMappings()) {
								if (map.getElementType() != IMethodMapping.CALLIN_MAPPING
										&& member.equals(map.getCorrespondingJavaElement())) 
								{
									if (!Modifier.isPublic(map.getFlags())) {
										final ModifierKeyword threshold= computeOutgoingVisibilityThreshold(map, monitor);
										adjustOutgoingVisibility(map, threshold, RefactoringCoreMessages.MemberVisibilityAdjustor_change_visibility_method_warning);
									}
									if (member.getDeclaringType() != null)
										base.checkOTMember(member.getDeclaringType(), monitor);
									return;
								}
							}
						}
					}
				}
				throw jme;
			}
		}
		// adjusted copy, base version cannot handle OT elements
		private ModifierKeyword computeOutgoingVisibilityThreshold(final IMember referenced, final IProgressMonitor monitor) throws JavaModelException {
			final IJavaElement referencing = getFReferencing();
			Assert.isTrue(referencing instanceof ICompilationUnit || referencing instanceof IType || referencing instanceof IPackageFragment);
			ModifierKeyword keyword= ModifierKeyword.PUBLIC_KEYWORD;
			try {
				monitor.beginTask("", 1); //$NON-NLS-1$
				monitor.setTaskName(RefactoringCoreMessages.MemberVisibilityAdjustor_checking);
				final int referencingType= referencing.getElementType();
				final int referencedType= referenced.getElementType();
				switch (referencedType) {
					case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
					case IOTJavaElement.CALLOUT_MAPPING: {
						final IMethodMapping calloutReferenced= (IMethodMapping) referenced;
						final ICompilationUnit referencedUnit= calloutReferenced.getCompilationUnit();
						switch (referencingType) {
							case IJavaElement.COMPILATION_UNIT: {
								final ICompilationUnit unit= (ICompilationUnit) referencing;
								if (referencedUnit != null && referencedUnit.equals(unit))
									keyword= ModifierKeyword.PRIVATE_KEYWORD;
								else if (referencedUnit != null && referencedUnit.getParent().equals(unit.getParent()))
									keyword= null;
								break;
							}
							case IJavaElement.TYPE: {
								keyword= thresholdTypeToMethod((IType) referencing, 
										(IMethod) calloutReferenced.getCorrespondingJavaElement(), monitor);
								break;
							}
							case IJavaElement.PACKAGE_FRAGMENT: {
								final IPackageFragment fragment= (IPackageFragment) referencing;
								if (calloutReferenced.getDeclaringType().getPackageFragment().equals(fragment))
									keyword= null;
								break;
							}
							default:
								Assert.isTrue(false);
						}
						break;
					}
					default:
						Assert.isTrue(false);
				}
			} finally {
				monitor.done();
			}
			return keyword;
		}
		
		// simple adjustment: for visibility checks in incoming direction the corresponding IMethod suffices:
		ModifierKeyword getVisibilityThreshold(IMember referenced)
		<- replace ModifierKeyword getVisibilityThreshold(IJavaElement referencing, IMember referenced, IProgressMonitor monitor)
			base when(referenced instanceof AbstractCalloutMapping)
			with { referenced <- referenced, result -> result }

		callin ModifierKeyword getVisibilityThreshold(IMember referencedMovedElement) throws JavaModelException {
			IMethod method = (IMethod) ((AbstractCalloutMapping)referencedMovedElement).getCorrespondingJavaElement();
			return base.getVisibilityThreshold(method);
		}
	}
	
	/**
	 * Method ReferenceFinderUtil.getMethodsReferencedIn
	 * should also report callout mappings as methods.
	 */
	protected class ReferenceFinder playedBy ReferenceFinderUtil {

		/** Adapt the method that filters METHOD elements. */
		Set<IJavaElement> extractMethods(SearchMatch[] searchResults)
		<- replace Set<IJavaElement> extractElements(SearchMatch[] searchResults, int elementType)
			base when (elementType == IJavaElement.METHOD);

		@SuppressWarnings("basecall")
		static callin Set<IJavaElement> extractMethods( SearchMatch[] searchResults) {
			Set<IJavaElement> elements= new HashSet<IJavaElement>();
			for (int i= 0; i < searchResults.length; i++) {
				IJavaElement el= SearchUtils.getEnclosingJavaElement(searchResults[i]);
				if (el instanceof IMember) {
					el = methodOrCallout((IMember) el);
					if (el != null)
						elements.add(el);
				}
			}
			return elements;
		}
		static IJavaElement methodOrCallout(IMember member) {
			int memberType = member.getElementType();
			if (member.exists()) {
				switch (memberType) {
				case IJavaElement.METHOD:
				case IOTJavaElement.CALLOUT_MAPPING:
				case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
					return member;
				default: 
					return null;
				}
			}
            if (memberType == IJavaElement.METHOD) {
            	// search a callout mapping that might be "equal" to this method:
            	IType type = member.getDeclaringType();
            	try {
					for (IJavaElement child : type.getChildren()) {
						int elementType = child.getElementType();
						if (elementType == IOTJavaElement.CALLOUT_MAPPING || elementType == IOTJavaElement.CALLOUT_TO_FIELD_MAPPING) {
							AbstractCalloutMapping map = (AbstractCalloutMapping) child;
							if (member.equals(map.getCorrespondingJavaElement())) {
								return map;
							}
						}
					}
				} catch (JavaModelException e) {
					return null;
				}
            }
            return null;
		}
	}


	/** Help the first wizard page to avoid illegal settings (impossible pull-up of callout). */
	protected team class WizardPage playedBy PullUpMemberPage {

		/** Gateway to a private inner class: */
		protected class MemberActionInfo playedBy MemberActionInfo {

			int getNO_ACTION() 						-> get int NO_ACTION;
			
			protected IMember getMember()  			-> IMember getMember();			
			protected boolean hasAction() 			-> int getAction()
					with { result 					<- result != getNO_ACTION() }
			protected void setAction(int action) 	-> void setAction(int action);
		}

		int getDECLARE_ABSTRACT_ACTION()   -> get int DECLARE_ABSTRACT_ACTION;
		int getPULL_UP_ACTION() 		   -> get int PULL_UP_ACTION;

		MemberActionInfo[] getTableInput() -> MemberActionInfo[] getTableInput();
		IType getDestinationType()         -> IType getDestinationType();

		checkActionForCallouts <- before updateWizardPage;

		void checkActionForCallouts() {
			MemberActionInfo[] infos = getTableInput();
			for (MemberActionInfo info : infos) {
				if (!info.hasAction()) continue;
				IMember member = info.getMember();
				if (member instanceof AbstractCalloutMapping) {
					RefactoringStatus status = new RefactoringStatus();
					boolean haveError = false;
					try {
						PullUpRefactoringProcessorRole.checkDestinationForCallout((IMethod)member, getDestinationType(), status, new NullProgressMonitor());
						haveError = status.hasFatalError();
					} catch (JavaModelException e) {
						haveError = true;
					}
					if (haveError) {
						info.setAction(getDECLARE_ABSTRACT_ACTION());
					}
				}
			}
		}
	}

	// advance fix for Bug 393932 - [refactoring] pull-up with "use the destination type where possible" creates bogus import of nested type
	protected class UseSuperTypeFix playedBy SuperTypeRefactoringProcessor {

		void rewriteTypeOccurrence(final TType estimate, final CompilationUnitRewrite rewrite, final ASTNode node, final TextEditGroup group)
		<- replace 
		void rewriteTypeOccurrence(final TType estimate, final CompilationUnitRewrite rewrite, final ASTNode node, final TextEditGroup group);

		@SuppressWarnings("basecall")
		callin void rewriteTypeOccurrence(TType estimate, CompilationUnitRewrite rewrite, ASTNode node, TextEditGroup group) {
			// combined from direct base method plus createCorrespondingNode(..):
			rewrite.getImportRemover().registerRemovedNode(node);
			ImportRewrite importRewrite= rewrite.getImportRewrite();
			ImportRewriteContext context = new ContextSensitiveImportRewriteContext(node, importRewrite);
			ASTNode correspondingNode = importRewrite.addImportFromSignature(new BindingKey(estimate.getBindingKey()).toSignature(), rewrite.getAST(), context);
			rewrite.getASTRewrite().replace(node, correspondingNode, group);
		}
	}
}
