/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
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
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.hierarchy;

import java.util.HashMap;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.objectteams.otdt.core.TypeHelper;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;

/**
 * Strategy for the {@link OTTypeHierarchyTraverser}.
 * @author svacina
 */

public class InheritedMethodsRequestor extends TraverseRequestor
{
	private HashMap<String, IMethod> _result;
	private boolean _overwriteCollectedMethods;
	private boolean _checkVisibility;
	
	/**
	 * Create a requestor the will collect inherited methods.
	 * @param type						the focus type of the traversal
	 * @param overwriteCollectedMethods if methods with the same signature should overwrite previously collected methods
	 * @param checkVisibility			if method visibility should be checked.
	 */
	public InheritedMethodsRequestor(IType type,
			boolean overwriteCollectedMethods,
			boolean checkVisibility)
	{
		// TODO (jsv): can this class cope with IOTTypes as well? If not, it should use its correspondingJavaElement()
		_result = new HashMap<String, IMethod>(); 
		_focusType = type;
		_overwriteCollectedMethods = overwriteCollectedMethods;
		_checkVisibility = checkVisibility;
	}

	@Override
	void report(IType type, HierarchyContext context) {
		
		boolean checkVisibility;
		try {
			if (!_checkVisibility) {
				checkVisibility = false;
			} else if (type.isInterface()) {
		        if (_focusType.isInterface())
					checkVisibility = false;
				else
					return;
		    } else {
		        if (context.isFocusType)
					checkVisibility = false;
				else
					checkVisibility = context.isExplicitSuperclass || context.isBehindExplicitInheritance;
		    }
		    writeMethodsToResult(type,checkVisibility);
		} catch (JavaModelException e) {
			// TODO(jsv)handle exception
			e.printStackTrace();
		}
	}
	
	private void writeMethodsToResult(
			IType type, 
			boolean checkVisibility) throws JavaModelException
	{
		IMethod[] methods = type.getMethods();
		IMethod currMethod = null;
		
		for (int methodIdx = 0; methodIdx < methods.length; methodIdx++)
        {
			currMethod = methods[methodIdx];
            if (currMethod.getElementName().startsWith(IOTConstants.OT_DOLLAR))
				continue;
            
            if (currMethod.isConstructor())
				continue;
            // TODO(jsv) check completeness
            if (checkVisibility)
            {
            	// private
            	if (Flags.isPrivate(currMethod.getFlags()))
					continue;
				else if (   Flags.isPublic(currMethod.getFlags()) 
            			 || Flags.isProtected(currMethod.getFlags()) )
					storeMethod(currMethod);
				else if (type.getPackageFragment().equals(_focusType.getPackageFragment()))
					storeMethod(currMethod);
				else
					continue;
            } else {
            	storeMethod(currMethod);
            }
        }
	}
	
	private void storeMethod(IMethod method)
	{
		String key = TypeHelper.getMethodSignature(method);
		
		if (!_overwriteCollectedMethods) {
			if (!_result.containsKey(key))
				_result.put(key, method);
		} else {
			_result.put(key, method);
		}
	}
	
	/** 
	 * Retrieve the result of the traverse operation.
	 * @return a non-null array of methods.
	 */
	public IMethod[] getResult() {
		return _result.values().toArray(new IMethod[_result.size()]);	
	}
}
