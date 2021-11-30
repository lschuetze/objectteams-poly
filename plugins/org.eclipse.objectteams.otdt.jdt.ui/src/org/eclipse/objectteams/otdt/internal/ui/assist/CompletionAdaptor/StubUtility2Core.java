/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2007, 2018 Technical University Berlin, Germany and others.
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
 * 	   Technical University Berlin - Initial API and implementation
 *     IBM Corporation - implementation of individual method bodies
 **********************************************************************/
team package org.eclipse.objectteams.otdt.internal.ui.assist.CompletionAdaptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;

/**
 * Add new functions to StubUtility2Core, accessing otherwise invisible helper functions.
 * @author stephan
 * @since 2.7.2 (separated out from sibling StubUtility2 due to code move from jdt.ui to jdt.core.manipulation)
 */
@SuppressWarnings({ "unchecked", "rawtypes"/*parameter-less lists from DOM*/, "decapsulation"/*final baseclass + callout-decapsulation*/})
protected class StubUtility2Core playedBy StubUtility2Core
{

	// CALLOUT INTERFACE:
	void findUnimplementedInterfaceMethods(ITypeBinding typeBinding, HashSet<ITypeBinding> visited, ArrayList<IMethodBinding> allMethods, IPackageBinding currPack, ArrayList<IMethodBinding> toImplement) 
		-> void findUnimplementedInterfaceMethods(ITypeBinding typeBinding, HashSet<ITypeBinding> visited, ArrayList<IMethodBinding> allMethods, IPackageBinding currPack, ArrayList<IMethodBinding> toImplement);
	IMethodBinding findMethodBinding(IMethodBinding method, List<IMethodBinding> allMethods) 
		-> IMethodBinding findMethodBinding(IMethodBinding method, List<IMethodBinding> allMethods);
	
	/** 
	 * This callin adds treatment of implicit inheritance including phantom roles
	 * to it's base version.
	 */
	IMethodBinding[] getUnimplementedMethods(ITypeBinding typeBinding, boolean implementAbstractsOfInput) 
		<- replace IMethodBinding[] getUnimplementedMethods(ITypeBinding typeBinding, boolean implementAbstractsOfInput);
	@SuppressWarnings("basecall")
	callin static IMethodBinding[] getUnimplementedMethods(ITypeBinding typeBinding, boolean implementAbstractsOfInput) {
		// COPY&PASTE from base version:
		ArrayList<IMethodBinding> allMethods= new ArrayList<>();
		ArrayList<IMethodBinding> toImplement= new ArrayList<>();

		for (IMethodBinding curr : typeBinding.getDeclaredMethods()) {
			int modifiers= curr.getModifiers();
			if (!curr.isConstructor() && !Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers)
					&& !curr.isSyntheticRecordMethod()) {
//{ObjectTeams: never enter copied methods (have no java element)
			  if (!curr.isCopied())
// SH}
				allMethods.add(curr);
			}
		}
//{ObjectTeams: direct tsuper roles:
		ITypeBinding[] tsupers= typeBinding.getSuperRoles();
		  if (tsupers != null)
			  for (ITypeBinding tsuperRole : tsupers) 
				  findVisibleVirtualMethods(tsuperRole, allMethods);
// SH}
		
		ITypeBinding superClass= typeBinding.getSuperclass();
		while (superClass != null) {
//{ObjectTeams: watch for phantom roles (which don't exist):
		  if (superClass.getJavaElement().exists()) {
			  // extracted orig:
			  findVisibleVirtualMethods(superClass, allMethods);
		  } else {
			  // proceed to the existing super of a phantom role:
			  tsupers= superClass.getSuperRoles();
			  if (tsupers != null)
				  for (ITypeBinding tsuperRole : tsupers) 
					  findVisibleVirtualMethods(tsuperRole, allMethods);
		  }
// SH}
		  superClass= superClass.getSuperclass();
		}

		for (IMethodBinding curr : allMethods) {
			int modifiers= curr.getModifiers();
			if ((Modifier.isAbstract(modifiers) || curr.getDeclaringClass().isInterface()) && (implementAbstractsOfInput || typeBinding != curr.getDeclaringClass())) {
				// implement all abstract methods
				toImplement.add(curr);
			}
		}

		HashSet<ITypeBinding> visited= new HashSet<>();
		ITypeBinding curr= typeBinding;
		while (curr != null) {
			for (ITypeBinding superInterface : curr.getInterfaces()) {
//{ObjectTeams: consider that a role class part finds its super interfaces in the interface part:
				if (curr.isClassPartOf(superInterface)) {
					// do consider transitively inherited methods:
					ITypeBinding[] superSuperInterfaces = superInterface.getInterfaces();
					for (ITypeBinding superSuperIfc : superSuperInterfaces)
						findUnimplementedInterfaceMethods(superSuperIfc, visited, allMethods, typeBinding.getPackage(), toImplement);
					// but don't add methods from our own ifc-part (occurs when role extends non-role).
					continue; 
				}
// SH}
				findUnimplementedInterfaceMethods(superInterface, visited, allMethods, typeBinding.getPackage(), toImplement);
			}
			curr= curr.getSuperclass();
		}

		return toImplement.toArray(new IMethodBinding[toImplement.size()]);
	}
	// COPY&PASTE: orig extracted as helper from base of above:
	private static void findVisibleVirtualMethods(ITypeBinding typeBinding, ArrayList<IMethodBinding> allMethods) {
		for (IMethodBinding curr : typeBinding.getDeclaredMethods()) {
			int modifiers= curr.getModifiers();
			if ((!curr.isConstructor() && !Modifier.isStatic(modifiers) && !Modifier.isPrivate(modifiers))
					|| Modifier.isAbstract(modifiers)) // OT: account for 'abstract static' (partially virtual ;P )
			{
			  if (!curr.isCopied()) // new check for OT
				if (findMethodBinding(curr, allMethods) == null) {
					allMethods.add(curr);
				}
			}
		}		
	}
}

