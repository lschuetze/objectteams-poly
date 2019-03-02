/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008, 2017 Technical University Berlin, Germany and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.debug.adaptor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;

import base org.eclipse.jdt.internal.debug.core.JavaDebugUtils;

/**
 * This team makes the PDE source lookup aware of role files.
 * 
 * @author stephan
 * @since 1.2.1
 */
@SuppressWarnings("restriction")
public team class SourceLookupAdaptor {

	/**
	 * Fix source lookup for "Open Actual Type" action for special OT types:
	 * <ul>
	 * <li>"$__OT__" delimiters are removed from role names for successful lookup of ITypes.</li>
	 * <li>Find role files in the proper team package.</li>
	 * <li>Find the most specific source for phantom roles.</li>
	 * </ul> 
	 */
	protected class JavaDebugUtils playedBy JavaDebugUtils 
	{
		IJavaElement getJavaElement(Object sourceElement) -> IJavaElement getJavaElement(Object sourceElement);

		Object resolveSourceElement(Object object, String stratum, ILaunch launch) -> Object resolveSourceElement(Object object, String stratum, ILaunch launch);

		@SuppressWarnings("decapsulation")
		IType resolveType(String qualifiedName, IJavaElement javaElement) <- replace IType resolveType(String qualifiedName, IJavaElement javaElement);

		static callin IType resolveType(String qualifiedName, IJavaElement javaElement) {
			qualifiedName = qualifiedName.replace("$__OT__", "$"); //$NON-NLS-1$ //$NON-NLS-2$
    		IType result = base.resolveType(qualifiedName, javaElement);
    		if (result != null && result.exists())
    			return result;
    		// the given compilation unit doesn't have a type `type.getName()`
    		try {
	    		ICompilationUnit resolvedCU = (ICompilationUnit) javaElement;
	    		return findRoFiOrTSuper(qualifiedName, resolvedCU);
    		} catch (Exception e) {
    			// nothing found
    		}
    		return null;
		}
		
		static IType findRoFiOrTSuper(String qualifiedName, ICompilationUnit resolvedCU) throws JavaModelException {
    		int lastDollar = qualifiedName.lastIndexOf('$');
    		if (lastDollar != -1) {
    			String roleName = qualifiedName.substring(lastDollar+1);
    			IJavaProject javaProject = resolvedCU.getJavaProject();

    			// check for role file:
    			IPackageFragment pack = (IPackageFragment) resolvedCU.getParent();
    			int start = 0;
    			int currentDollar;
    			while ((currentDollar = qualifiedName.indexOf('$', start)) != -1) {
	    			String teamPackName = qualifiedName.substring(0, currentDollar); // include at least one type name
	    			if (pack.getElementName().equals(teamPackName)) {
						return getTypeInPackage(pack, qualifiedName.substring(currentDollar+1));
					}
	    			start = currentDollar+1;
    			}
    			
    			// find a type that is (a) tsuper of `type` (search via superclass of enclosing) and (b) exists
    			String enclosingName = qualifiedName.substring(0, lastDollar);
				IType enclosingType = javaProject.findType(enclosingName);
    			while (enclosingType != null) {
    				IType type = enclosingType.getType(roleName);
    				if (type.exists())
    					return type;
    				String[][] superclassName = enclosingType.resolveType((enclosingType.getSuperclassName()));
    				try {
    					enclosingType = javaProject.findType(superclassName[0][0], superclassName[0][1]);
    				} catch (JavaModelException jme) {
    					if (jme.isDoesNotExist()) {
    						enclosingType = findRoFiOrTSuper(enclosingName, resolvedCU);
    					}
    				}
    			}
    		}
			return null;
		}
	}
	private static IType getTypeInPackage(IPackageFragment pack, String relativeName) {
		int firstDollar = relativeName.indexOf('$');
		String cuName = firstDollar == -1 ? relativeName : relativeName.substring(0, firstDollar);
		ICompilationUnit unit = pack.getCompilationUnit(cuName + SuffixConstants.SUFFIX_STRING_java);
		if (unit.exists()) {
			IType top = unit.getType(cuName);
			if (top.exists() && firstDollar != -1)
				return findMemberType(top, relativeName.substring(firstDollar+1));
			return top;
		}
		return null;
	}
	private static IType findMemberType(IType type, String memberName) {
		int firstDollar = memberName.indexOf('$');
		if (firstDollar == -1) {
			return type.getType(memberName);
		}
		return findMemberType(type.getType(memberName.substring(0, firstDollar)), memberName.substring(firstDollar)+1);
	}
}
