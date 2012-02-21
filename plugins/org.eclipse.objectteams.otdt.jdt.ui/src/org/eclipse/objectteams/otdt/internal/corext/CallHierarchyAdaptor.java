/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2006, 2007 Technical University Berlin, Germany and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: CallHierarchyAdaptor.java 23438 2010-02-04 20:05:24Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *     Technical University Berlin - Initial API and implementation
 *     IBM Corporation - copies of individual methods from bound base classes.
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.corext;

import static org.eclipse.jdt.internal.core.search.matching.MethodPattern.findingCallers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodCall;
import org.eclipse.jdt.internal.corext.util.SearchUtils;
import org.eclipse.objectteams.otdt.core.ICallinMapping;
import org.eclipse.objectteams.otdt.core.ICalloutMapping;
import org.eclipse.objectteams.otdt.core.IMethodMapping;
import org.eclipse.objectteams.otdt.core.IOTJavaElement;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.core.compiler.OTNameUtils;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.CallinMappingDeclaration;

import base org.eclipse.jdt.internal.corext.callhierarchy.CallSearchResultCollector;
import base org.eclipse.jdt.internal.corext.callhierarchy.CalleeMethodWrapper;
import base org.eclipse.jdt.internal.corext.callhierarchy.CallerMethodWrapper;
import base org.eclipse.jdt.internal.corext.callhierarchy.MethodReferencesSearchRequestor;
import base org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;
import base org.eclipse.jdt.internal.ui.callhierarchy.CallHierarchyContentProvider;

/**
 * This class allows the call hierarchy to cope with callin/callout method mappings.
 * 
 * @author stephan
 */
@SuppressWarnings({"restriction", "decapsulation"})	
public team class CallHierarchyAdaptor 
{
	
	/** 
	 * Purpose: accept method mappings, too. 
	 */
	protected class MappingReferenceSearchRequestor playedBy MethodReferencesSearchRequestor
	{
		protected MappingReferenceSearchRequestor() {
			base();
		}
		acceptSearchMatch <- replace acceptSearchMatch;
	    @SuppressWarnings("basecall")
		callin void acceptSearchMatch(SearchMatch match) {
	    	// COPY&PASTE from base version
	        if (getRequireExactMatch() && (match.getAccuracy() != SearchMatch.A_ACCURATE)) {
	            return;
	        }
	        
	        if (match.isInsideDocComment()) {
	            return;
	        }
	        
	        if (match.getElement() != null && match.getElement() instanceof IMember) {
	            IMember member= (IMember) match.getElement();
	            switch (member.getElementType()) {
	                case IJavaElement.METHOD:
	                case IJavaElement.TYPE:
	                case IJavaElement.FIELD:
	                case IJavaElement.INITIALIZER:
//{ObjectTeams: the payload:	            
	                case IOTJavaElement.CALLIN_MAPPING:
	                case IOTJavaElement.CALLOUT_MAPPING:
	                case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
// SH}
	                    getSearchResults().addMember(member, member, match.getOffset(), match.getOffset()+match.getLength());
	                    break;
	            }
	        }
	    }

	    boolean getRequireExactMatch()               -> get boolean fRequireExactMatch;
	    CallSearchResultCollector getSearchResults() -> get CallSearchResultCollector fSearchResults;
		Map<String, MethodCall> getCallers()		 -> Map<String, MethodCall> getCallers();
	}

	/**
	 * Count method mappings as legal children, too.
	 */
	protected class ContentProvider playedBy CallHierarchyContentProvider
	{
		@SuppressWarnings("hidden-lifting-problem") // lifting 'wrapper' could theoretically fail
		boolean hasChildren(MethodMappingWrapper wrapper) <- replace boolean hasChildren(Object element)
		base when (element instanceof MethodWrapper)
		with { wrapper <- (MethodWrapper)element }
		
		callin boolean hasChildren(MethodMappingWrapper methodWrapper) {
			if (base.hasChildren(methodWrapper)) 
				return true;		
			switch (methodWrapper.getMember().getElementType()) {
			case IOTJavaElement.CALLIN_MAPPING:
			case IOTJavaElement.CALLOUT_MAPPING:
			case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
				break; // continue below
			default:
				return false;
			}
			if (shouldStopTraversion(methodWrapper)) {
				return false;
			}
			return true;
		}
		boolean shouldStopTraversion(MethodMappingWrapper methodWrapper) -> boolean shouldStopTraversion(MethodWrapper methodWrapper);
	}	

	/**
	 * Implement new strategies for finding children of method mappings. General part. 
	 */
	@SuppressWarnings("abstractrelevantrole") // MethodWrapper is abstract, both sub classes are bound individually
	abstract protected class MethodMappingWrapper playedBy MethodWrapper
	{ 	
		@SuppressWarnings("hidden-lifting-problem") // abstract role could potentially throw LiftingFailedException
		doFindChildren <- replace doFindChildren;
		@SuppressWarnings("basecall")
		callin void doFindChildren(IProgressMonitor progressMonitor) {
			IMember member = getMember();
			IMember[] children = null;
			if (   member.getElementType() == IOTJavaElement.CALLIN_MAPPING
				|| member.getElementType() == IOTJavaElement.CALLOUT_MAPPING) 
			{
				Map<String,MethodCall> existingResults = lookupMethod(getMethodCall());
		        if (existingResults != null && !existingResults.isEmpty()) {
		        	Map<String,MethodCall> newElements = new HashMap<String,MethodCall>();
		        	setElementMap(newElements);
		            newElements.putAll(existingResults);
		            return; // successfully done
		        } else {
		        	// try direct lookup instead of searching:
					try {
						children = (member.getElementType() == IOTJavaElement.CALLIN_MAPPING) ?
									getCallinMethods((ICallinMapping)member) :
									getCalloutMethods((ICalloutMapping)member);
					} catch (JavaModelException e) {
						return; // search failed
					}
				}
			} else if (isGuardPredicate(member)) {
				// directly fetch affected callins without search
				children = getCallinsAffectedByGuard(member);
			}
			if (children != null) {
				// if found add result:
				addReferencedMembers(children);
				return;
			}
			if (handleRoleInstantiations(member, progressMonitor))
				return;
			// nothing-found scenarii, revert to normal behavior:
			base.doFindChildren(progressMonitor);
		}

		private void addReferencedMembers(IMember[] children) {
			initCalls(); // orig from doFindChildren()
			Map<String, MethodCall> newElements = getFElements();
			for (IMember method : children) {
				MethodCall methodCall = new MethodCall(method);
				addCallToCache(methodCall); // see performSearch()
				newElements.put(method.getHandleIdentifier(), methodCall);
			}
		}
		private boolean isGuardPredicate(IMember member) {
			if (member.getElementType() != IJavaElement.METHOD)
				return false;
			String selector = member.getElementName();
			return OTNameUtils.isPredicate(selector.toCharArray());
		}

		// hooks:
		abstract IMethod[] getCallinMethods(ICallinMapping callinMapping) throws JavaModelException;
		abstract IMethod[] getCalloutMethods(ICalloutMapping calloutMapping) throws JavaModelException;
		IMember[] getCallinsAffectedByGuard(IMember member) {
			/* default: */ return null;
		}
		boolean handleRoleInstantiations(IMember member, IProgressMonitor progressMonitor) {
			/* default: */ return false;
		}
		
		// callouts:
				  void setElementMap(Map<String,MethodCall> map)-> set Map<String,MethodCall> fElements;
		protected Map<String, MethodCall> getFElements()		-> get Map<String,MethodCall> fElements;
				  void setMethodCall(MethodCall methodCall)  	-> set MethodCall fMethodCall;
		protected void initCalls()                  		    -> void initCalls();
				  IMember getMember()                       	-> IMember getMember();
				  MethodCall getMethodCall()                 	-> MethodCall getMethodCall();
				  Map<String,MethodCall> lookupMethod(MethodCall methodCall)
				  												-> Map<String,MethodCall> lookupMethod(MethodCall methodCall);
		protected void addCallToCache(MethodCall methodCall) 	-> void addCallToCache(MethodCall methodCall);
	}
	
	/**
	 * Special case method wrapper: Callee direction. Relies on smart lifting. 
	 * FIXME(SH): This class is mainly untested yet.
	 * 			  Find out how the original visitors work and how they must be adapted.
	 */
	protected class CalleeMethodMappingWrapper 
		extends MethodMappingWrapper
		playedBy CalleeMethodWrapper
	{
		@Override
		public IMethod[] getCallinMethods(ICallinMapping callinMapping)
			throws JavaModelException 
		{
			return new IMethod[] {callinMapping.getRoleMethod()};
		}
		@Override
		public IMethod[] getCalloutMethods(ICalloutMapping calloutMapping)
			throws JavaModelException 
		{
			return new IMethod[] {calloutMapping.getBoundBaseMethod()};
		}
	}

	/**
	 * Special case method wrapper: Caller direction. Relies on smart lifting. 
	 */
	protected class CallerMethodMappingWrapper 
		extends MethodMappingWrapper
		playedBy CallerMethodWrapper
	{
		IJavaSearchScope getAccurateSearchScope(IJavaSearchScope defaultSearchScope,	IMember member)
			-> IJavaSearchScope getAccurateSearchScope(IJavaSearchScope defaultSearchScope, IMember arg1);
		IJavaSearchScope getSearchScope() -> IJavaSearchScope getSearchScope();
		
		@Override
		public IMethod[] getCallinMethods(ICallinMapping callinMapping)
			throws JavaModelException 
		{
			// no need for searching: our callers are explicitly listed here:
			return callinMapping.getBoundBaseMethods();
		}
		@Override
		public IMethod[] getCalloutMethods(ICalloutMapping calloutMapping)
			throws JavaModelException 
		{
			return null; // calloutMapping.getRoleMethod() is not useful, revert to normal searching.
		}
		
		/**
		 * Investigate predicate method name and collect all callins
		 * affected by this guard.
		 */
		@Override
		IMember[] getCallinsAffectedByGuard(IMember member) {
			IType declaringClass = member.getDeclaringType();
			String suffix = null; // what comes after _OT$when or _OT$base_when
			String selector = member.getElementName();
			if (selector.startsWith(String.valueOf(IOTConstants.PREDICATE_METHOD_NAME))) {
				// regular guard
				if (selector.length() == IOTConstants.PREDICATE_METHOD_NAME.length) {
					// no suffix
				} else {
					suffix = selector.substring(IOTConstants.PREDICATE_METHOD_NAME.length);
				}
			} else if (selector.equals(String.valueOf(IOTConstants.BASE_PREDICATE_PREFIX))) {
				// base guard
				if (selector.length() == IOTConstants.BASE_PREDICATE_PREFIX.length) {
					// no suffix
				} else {
					suffix = selector.substring(IOTConstants.BASE_PREDICATE_PREFIX.length);
				}
			}
			if (suffix == null) {
				// class level predicate
				IOTType otType = OTModelManager.getOTElement(declaringClass);
				ArrayList<IMember> result = new ArrayList<IMember> ();
				if (otType.isTeam()) {
					try {
						IType[] roles = otType.getRoleTypes();
						for (IType type : roles) {
							IRoleType roleType = (IRoleType) OTModelManager.getOTElement(type);
							for (IMember m : roleType.getMethodMappings(IRoleType.CALLINS))
								result.add(m);
						}
					} catch (JavaModelException jme) {
						// no success retrieving roles, proceed below
					}
				}
				if (otType.isRole())
					for (IMember m : ((IRoleType)otType).getMethodMappings(IRoleType.CALLINS))
						result.add(m);
				return result.toArray(new IMember[result.size()]);
			} else {
				StringTokenizer tokens = new StringTokenizer(suffix, "$"); //$NON-NLS-1$
				ArrayList<IMember> mList = new ArrayList<IMember>();
				switch (tokens.countTokens()) {
				case 1: // role method guard
					try {
						selector = tokens.nextToken();
						for (IMethod method : declaringClass.getMethods())
							if (method.getElementName().equals(selector))
								mList.add(method);
					} catch (JavaModelException e) {
						// failed to retrieve methods -> no result
					}
					break;
                case 3: // binding guard (s.t. like "roleMeth$after$baseMeth")
                	String roleSelector = tokens.nextToken();
                	String modifier = tokens.nextToken();
                	String baseSelector = tokens.nextToken();
                	IRoleType roleType = (IRoleType) OTModelManager.getOTElement(declaringClass);
                	mappings:
                	for (IMethodMapping mapping : roleType.getMethodMappings(IRoleType.CALLINS)) {
                		if (mapping.getRoleMethod().getElementName().equals(roleSelector)) {
                			int callinKind = ((ICallinMapping)mapping).getCallinKind();
							if (CallinMappingDeclaration.callinModifier(callinKind).equals(modifier))
							{
								try {
									for (IMethod baseMethod : ((ICallinMapping)mapping).getBoundBaseMethods())
										if (baseMethod.getElementName().equals(baseSelector)) {
											mList.add(mapping);
											continue mappings;
										}
								} catch (JavaModelException e) {
									// failed to resolve base methods, proceed to next mapping
								}
							}    			
    					}
                	}
				}
				return mList.toArray(new IMember[mList.size()]);
			}
		}

		boolean handleRoleInstantiations(IMember member, IProgressMonitor monitor) {
			if (member.getElementType() == IJavaElement.METHOD) {
				if (isLiftingCtor((IMethod)member)) {
					return findRoleInstantiations(member.getDeclaringType(), monitor);
				}
			} else if (member.getElementType() == IJavaElement.TYPE) {
				IType type = (IType) member;
				if (OTModelManager.isRole(type)) {
					return findRoleInstantiations(type, monitor);
				}
			}
			return false;
		}

		private boolean isLiftingCtor(IMethod method) {
			try {
				if (!method.isConstructor()) return false;
				String[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length != 1) return false;
				IType declaringType = method.getDeclaringType();
				IOTType otType = OTModelManager.getOTElement(declaringType);
				if (otType == null || !otType.isRole()) return false;
				String baseClassName = ((IRoleType) otType).getBaseclassName();
				if (baseClassName == null) return false;
				String paramType = Signature.toString(parameterTypes[0]);
				if (baseClassName.indexOf('.') != -1)
					return baseClassName.equals(paramType);
				return baseClassName.equals(Signature.getSimpleName(paramType));
			} catch (JavaModelException jme) {
				return false;
			}
		}

		boolean findRoleInstantiations(IType roleType, IProgressMonitor monitor) {
			try {
				initCalls();
				Map<String, MethodCall> fElements = getFElements();
				// search-engine based search for constructor invocations and lifting references:
				Map<String, MethodCall> ctorElements = searchRoleCtorInvocations(roleType, monitor);
				if (!ctorElements.isEmpty()) {
					for (MethodCall call: ctorElements.values())
						addCallToCache(call);
					fElements.putAll(ctorElements);
				}
				// direct lookup of all non-static callins in this role:
				IRoleType otRoleType = (IRoleType) OTModelManager.getOTElement(roleType);
				for (IMethodMapping mapping : otRoleType.getMethodMappings(IRoleType.CALLINS)) {
					if (!Flags.isStatic(mapping.getRoleMethod().getFlags())) {
						MethodCall call = new MethodCall(mapping);
						addCallToCache(call);
						fElements.put(mapping.getHandleIdentifier(), call);
					}
				}
				return true;
			} catch (CoreException ex) {
				return false; // search failed or some found element doesn't exist?
			}			
		}

		Map<String, MethodCall> searchRoleCtorInvocations(IType roleType, IProgressMonitor monitor) throws CoreException {
			// mimicked after pieces from CallerMethodWrapper.findChildren():
			SearchPattern pattern= SearchPattern.createPattern(roleType,
					IJavaSearchConstants.CLASS_INSTANCE_CREATION_TYPE_REFERENCE, // this is lifting-aware!
					SearchUtils.GENERICS_AGNOSTIC_MATCH_RULE);
			SearchEngine searchEngine= new SearchEngine();
			MappingReferenceSearchRequestor searchRequestor= new MappingReferenceSearchRequestor();
			IJavaSearchScope defaultSearchScope= getSearchScope();
			boolean isWorkspaceScope= SearchEngine.createWorkspaceScope().equals(defaultSearchScope);
			IJavaSearchScope searchScope= isWorkspaceScope ? getAccurateSearchScope(defaultSearchScope, roleType) : defaultSearchScope;
			searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, searchScope, searchRequestor,
					monitor);
			return searchRequestor.getCallers();
		}

		
		// while searching for children in a caller-hierarchy, constrain all
		// method-mapping matches to the non-declaration side. 
		callin void findChildren() {
			findingCallers.set(new Object());
			try {
				base.findChildren();
			} finally {
				findingCallers.remove();
			}
		}		
		findChildren <- replace findChildren;
		
	}
	
	/** Handle for an invisible class. */
	protected class CallSearchResultCollector playedBy CallSearchResultCollector 
	{
		protected void addMember(IMember member, IMember calledMember, int start, int end) -> void addMember(IMember member, IMember calledMember, int start, int end);
	}
}
