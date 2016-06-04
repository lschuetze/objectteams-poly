/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008 Technical University Berlin, Germany.
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
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.debug.core.IJavaType;
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
	 * Fix source lookup for "Open Actual Type" action on a phantom role.
	 * Note that no JSR045 information is available, because the stackframe doesn't
	 * know about the actual type. 
	 */
	protected class JavaDebugUtils playedBy JavaDebugUtils 
	{
		IJavaElement getJavaElement(Object sourceElement) -> IJavaElement getJavaElement(Object sourceElement);

		Object resolveSourceElement(Object object, String stratum, ILaunch launch) -> Object resolveSourceElement(Object object, String stratum, ILaunch launch);

//		@SuppressWarnings("decapsulation")
//		IType resolveType(String qualifiedName, IJavaElement javaElement) <- replace IType resolveType(String qualifiedName, IJavaElement javaElement);
//
//		static callin IType resolveType(String qualifiedName, IJavaElement javaElement) {
//    		IType result = base.resolveType(qualifiedName, javaElement);
//    		// start OT-adaptation: check result:
//    		if (result != null && result.exists())
//    			return result;
//    		// the given compilation unit doesn't have a type `type.getName()`
//    		try {
//	    		ICompilationUnit resolvedCU = (ICompilationUnit) javaElement;
//	    		return findSuperTypeInCU(qualifiedName, resolvedCU);
//    		} catch (Exception e) {
//    			// nothing found
//    		}
//    		return null;
//		}
//		
//		static IType findSuperTypeInCU(String qualifiedName, ICompilationUnit resolvedCU) throws JavaModelException {
//    		int lastDollar = qualifiedName.lastIndexOf('$');
//    		if (lastDollar != -1) {
//    			// find a type that is (a) superclass of `type` and (b) contained in `resolvedCU`
//    			String enclosingName = qualifiedName.substring(0, lastDollar);
//    			IJavaProject javaProject = resolvedCU.getJavaProject();
//				IType enclosingType = javaProject.findType(enclosingName);
//    			while (enclosingType != null) {
//    				if (resolvedCU.equals(enclosingType.getAncestor(IJavaElement.COMPILATION_UNIT))) {
//    					// got the enclosing team, now find the corresponding role:
//    					String roleName = qualifiedName.substring(lastDollar+1);
//    					if (roleName.startsWith(IOTConstants.OT_DELIM))
//    						roleName = roleName.substring(IOTConstants.OT_DELIM_LEN);
//						return enclosingType.getType(roleName);
//    				}
//    				String[][] superclassName = enclosingType.resolveType((enclosingType.getSuperclassName()));
//    				try {
//    					enclosingType = javaProject.findType(superclassName[0][0], superclassName[0][1]);
//    				} catch (JavaModelException jme) {
//    					if (jme.isDoesNotExist()) {
//    						enclosingType = findSuperTypeInCU(enclosingName, resolvedCU);
//    					}
//    				}
//    			}
//    		}
//			return null;
//		}

		IJavaElement resolveJavaElement(Object object, ILaunch launch) <- replace IJavaElement resolveJavaElement(Object object, ILaunch launch);

		static callin IJavaElement resolveJavaElement(Object object, ILaunch launch) throws CoreException {
			IJavaElement result = base.resolveJavaElement(object, launch);
			if (result == null) {
				Object sourceElement = resolveSourceElement(object, null, launch); // if lookup in "JAVA" stratum failed, try default
				return getJavaElement(sourceElement);
			}
			return result;
		}
	}
}
