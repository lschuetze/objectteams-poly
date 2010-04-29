/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2007 Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: HierarchyUtilAdaptor.java 23438 2010-02-04 20:05:24Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.util;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.ui.typehierarchy.TypeHierarchyLifeCycle;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreePathContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.objectteams.otdt.core.IOTJavaElement;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.internal.core.OTType;
import org.eclipse.objectteams.otdt.internal.core.OTTypeHierarchy;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

import base org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import base org.eclipse.jdt.internal.corext.util.MethodOverrideTester;
import base org.eclipse.jdt.internal.corext.util.MethodOverrideTester.Substitutions;
import base org.eclipse.jdt.internal.ui.typehierarchy.TraditionalHierarchyViewer.TraditionalHierarchyContentProvider;
import base org.eclipse.jdt.internal.ui.util.OpenTypeHierarchyUtil;

/**
 * This team adapts different utility classes relating to hierarchies.
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class HierarchyUtilAdaptor 
{
	/** Also consider method mappings as candidates for a hierarchy. */ 
	protected class OpenTypeHierarchy playedBy OpenTypeHierarchyUtil 
	{		
		@SuppressWarnings("basecall")
		static callin IJavaElement[] getCandidates(Object input) {
			if (input instanceof IJavaElement) {
				IJavaElement elem= (IJavaElement) input;
				switch (elem.getElementType()) {
					case IOTJavaElement.CALLIN_MAPPING:
					case IOTJavaElement.CALLOUT_MAPPING:
					case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
						return new IJavaElement[] { elem };
				} 
			}
			return base.getCandidates(input);
		}
		getCandidates <- replace getCandidates;
	}
	
	/** Consider multiple super classes for roles. */
	@SuppressWarnings("decapsulation"/*final baseclass*/) 
	protected class JavaModelUtil playedBy JavaModelUtil 
	{
		@SuppressWarnings("basecall")
		static callin boolean isSuperType(ITypeHierarchy hierarchy, IType possibleSuperType, IType type)
		{
			if (hierarchy instanceof OTTypeHierarchy) {
		        IOTType otType = OTModelManager.getOTElement(type);
		        if (otType != null && otType.isRole())
				{
					IType[] supertypes = ((OTTypeHierarchy)hierarchy).getAllSupertypes(type);
					for (IType supertype : supertypes) 
						if (supertype.equals(possibleSuperType))
							return true;
					return false;
				}
			}
			return base.isSuperType(hierarchy, possibleSuperType, type);
		}
		isSuperType <- replace isSuperType;
	}
	
	/** Find overridden methods in all kinds of super types. */
	protected class MethodOverrideTester playedBy MethodOverrideTester 
	{
		abstract IMethod findOverriddenMethodInType(IType overriddenType, IMethod overriding)
			throws JavaModelException;
		
		findOverriddenMethodInType -> findOverriddenMethodInType;

		@SuppressWarnings("decapsulation")
		ITypeHierarchy getHierarchy() -> get ITypeHierarchy fHierarchy;

		
		@SuppressWarnings("basecall")
		callin IMethod findOverriddenMethod(IMethod overriding, boolean testVisibility) 
			throws JavaModelException 
		{
			ITypeHierarchy hierarchy = getHierarchy();
			if (hierarchy instanceof OTTypeHierarchy) 
			{
				IType declaringType = overriding.getDeclaringType();
				IOTType declaringOTType = OTModelManager.getOTElement(declaringType);
				if (declaringOTType != null && declaringOTType.isRole()) 
				{
					int flags= overriding.getFlags();
					if (Flags.isPrivate(flags) || Flags.isStatic(flags) || overriding.isConstructor()) {
						return null;
					}
					
					IType[] supertypes = ((OTTypeHierarchy)hierarchy).getAllSupertypes(declaringType);
					for (IType type : supertypes) {
						IMethod method = findOverriddenMethodInType(type, overriding);
						if (method != null)
							return method;
					}
					return null;
				}
			}
			return base.findOverriddenMethod(overriding, testVisibility);
		}
		findOverriddenMethod <- replace findOverriddenMethod;
		
		// ---- Second Use Case: invoked from Quick-Hierarchy of a role method
		@SuppressWarnings("decapsulation")
		void computeOTSubstitutions(IType instantiatedType, IType instantiatingType, String[] typeArguments)
		<- replace
		void computeSubstitutions(IType instantiatedType, IType instantiatingType, String[] typeArguments)
			base when (OTModelManager.isRole(instantiatedType));

		// for recursion go back to the base method
		@SuppressWarnings("decapsulation")
		void computeSubstitutions(IType instantiatedType, IType instantiatingType, String[] typeArguments)
		-> void computeSubstitutions(IType instantiatedType, IType instantiatingType, String[] typeArguments);
		
		@SuppressWarnings({ "inferredcallout", "decapsulation", "basecall", "unchecked" })
		callin void computeOTSubstitutions(IType instantiatedType, IType instantiatingType, String[] typeArguments) 
				throws JavaModelException 
		{
			Substitutions s= new Substitutions();
			fTypeVariableSubstitutions.put(instantiatedType, s);

			ITypeParameter[] typeParameters= instantiatedType.getTypeParameters();

			if (instantiatingType == null) { // the focus type
				for (int i= 0; i < typeParameters.length; i++) {
					ITypeParameter curr= typeParameters[i];
					// use star to make type variables different from type refs
					s.addSubstitution(curr.getElementName(), '*' + curr.getElementName(), getTypeParameterErasure(curr, instantiatedType));
				}
			} else {
				if (typeParameters.length == typeArguments.length) {
					for (int i= 0; i < typeParameters.length; i++) {
						ITypeParameter curr= typeParameters[i];
						String substString= getSubstitutedTypeName(typeArguments[i], instantiatingType); // substitute in the context of the instantiatingType
						String erasure= getErasedTypeName(typeArguments[i], instantiatingType); // get the erasure from the type argument
						s.addSubstitution(curr.getElementName(), substString, erasure);
					}
				} else if (typeArguments.length == 0) { // raw type reference
					for (int i= 0; i < typeParameters.length; i++) {
						ITypeParameter curr= typeParameters[i];
						String erasure= getTypeParameterErasure(curr, instantiatedType);
						s.addSubstitution(curr.getElementName(), erasure, erasure);
					}
				} else {
					// code with errors
				}
			}
			String superclassTypeSignature= instantiatedType.getSuperclassTypeSignature();
			if (superclassTypeSignature != null) {
				String[] superTypeArguments= Signature.getTypeArguments(superclassTypeSignature);
//{ObjectTeams: account for multiple super classes:
/* orig:
				IType superclass= fHierarchy.getSuperclass(instantiatedType);
				if (superclass != null && !fTypeVariableSubstitutions.containsKey(superclass)) {
					computeSubstitutions(superclass, instantiatedType, superTypeArguments);
				}
  :giro */
				for (IType superclass : ((OTTypeHierarchy)fHierarchy).getSuperclasses(instantiatedType)) {
					if (superclass != null && !fTypeVariableSubstitutions.containsKey(superclass)) {
						computeSubstitutions(superclass, instantiatedType, superTypeArguments);
					}
				}
// SH}
			}
			String[] superInterfacesTypeSignature= instantiatedType.getSuperInterfaceTypeSignatures();
			int nInterfaces= superInterfacesTypeSignature.length;
			if (nInterfaces > 0) {
				IType[] superInterfaces= fHierarchy.getSuperInterfaces(instantiatedType);
				if (superInterfaces.length == nInterfaces) {
					for (int i= 0; i < nInterfaces; i++) {
						String[] superTypeArguments= Signature.getTypeArguments(superInterfacesTypeSignature[i]);
						IType superInterface= superInterfaces[i];
						if (!fTypeVariableSubstitutions.containsKey(superInterface)) {
							computeSubstitutions(superInterface, instantiatedType, superTypeArguments);
						}
					}
				}
			}			
		}
	}
	
	/** Mere gateway role, confined as to force lowering when assigning to Object. */
	@SuppressWarnings("decapsulation")
	protected class Substitutions extends Confined playedBy Substitutions {
		protected Substitutions() { base(); }

		void addSubstitution(String elementName, String string, String typeParameterErasure)
		-> void addSubstitution(String elementName, String string, String typeParameterErasure);
	}
	
	/** If a single parent is requested of a role type answer its explicit superclass. */
	protected class TraditionalHierarchyContent playedBy TraditionalHierarchyContentProvider {

		IType getParentType(IType type) <- replace IType getParentType(IType type)
			base when (OTModelManager.isRole(type));

		@SuppressWarnings("basecall")
		callin IType getParentType(IType type) {
			ITypeHierarchy hierarchy = getTypeHierarchy();
			return ((OTTypeHierarchy)hierarchy).getExplicitSuperclass(type);
		}

		@SuppressWarnings("decapsulation")
		ITypeHierarchy getTypeHierarchy() -> get TypeHierarchyLifeCycle fTypeHierarchy
			with { result <- result.getHierarchy() }	
	}
}
