package org.eclipse.objectteams.otdt.internal.refactoring.adaptor.pullup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.MethodDeclarationMatch;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.jdt.internal.corext.refactoring.base.JavaStatusContext;
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
import org.eclipse.objectteams.otdt.internal.core.RoleType;
import org.eclipse.objectteams.otdt.internal.refactoring.RefactoringMessages;
import org.eclipse.objectteams.otdt.internal.refactoring.util.IAmbuguityMessageCreator;
import org.eclipse.objectteams.otdt.internal.refactoring.util.IOverloadingMessageCreator;
import org.eclipse.objectteams.otdt.internal.refactoring.util.RefactoringUtil;
import org.eclipse.osgi.util.NLS;

import base org.eclipse.jdt.internal.corext.refactoring.structure.MemberVisibilityAdjustor;
import base org.eclipse.jdt.internal.corext.refactoring.structure.PullUpRefactoringProcessor;

/**
 * @author Johannes Gebauer
 * 
 */
@SuppressWarnings({ "restriction", "decapsulation" }) // private base classes
public team class PullUpAdaptor {

	public class PullUpRefactoringProcessorRole playedBy PullUpRefactoringProcessor {
		
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
				status.merge(checkDestinationForOTElements());
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
		
		private RefactoringStatus checkDestinationForOTElements() throws JavaModelException {
			RefactoringStatus status = new RefactoringStatus();
			for (int i = 0; i < getFMembersToMove().length; i++) {
				IMember element = getFMembersToMove()[i];
				if (element instanceof IMethod){
					IMethod method = (IMethod)element;
					// callin methods can only be moved to roles
					if(Flags.isCallin(method.getFlags()) && !TypeHelper.isRole(getDestinationType().getFlags())){
						String msg = NLS.bind(RefactoringMessages.PullUpAdaptor_callinMethodToNonRole_error, method.getElementName());
						status.addFatalError(msg, JavaStatusContext.create(method));
					}
				}
			}
			return status;
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
						if(mapping.getBoundBaseMethod().equals(member)){
							addAspectBindingWarning(member, status, mapping);
						}
					}
					if (element instanceof ICalloutToFieldMapping) {
						ICalloutToFieldMapping mapping = (ICalloutToFieldMapping) element;
						if(mapping.getBoundBaseField().equals(member)){
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
			
	}

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
	}
}


