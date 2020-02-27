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
package org.eclipse.objectteams.otdt.core;


import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;

/**
 * OTM Method mapping element
 * 
 * @author jwloka
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IMethodMapping extends IOTJavaElement, IMember, IAnnotatable
{
	/**
	 * Returns the type of mapping e.g. callin Mapping
	 * @return possible values are: IOTJavaElement.CALLIN_MAPPING
	 * 								IOTJavaElement.CALLOUT_MAPPING
	 * 								IOTJavaElement.CALLOUT_TO_FIELD_MAPPING
	 */
	public int getMappingKind();

	/**
	 * Dynamically resolves associated role method from the JavaModel
	 * @return a role method JavaModel element
	 */	
	public IMethod getRoleMethod();

    /**
     * Retrieve a handle for the role method.
     * Used by the SelectionRequestor to find out about the role method spec, 
     * if no role method can be resolved (short-hand callout).
     * @return the role-side method spec 
     */
	public IMethodSpec getRoleMethodHandle();
    
	/**
	 * Returns the start position of the 'name' part of this method mapping.
	 * For labelled callin mappings it is the label, otherwise the name
	 * of the role method spec.
	 */
	public int getSourceStart();
	
	/**
	 * Returns the end position of the 'name' part of this method mapping.
	 * For labelled callin mappings it is the label, otherwise the name
	 * of the role method spec.
	 */
	public int getSourceEnd();
	
	/**
	 * Returns the start position of this MethodMapping declaration within
	 * the CompilationUnit  
	 * @return start position in characters 
	 */	
	public int getDeclarationSourceStart();

	/**
	 * Returns the end position of this MethodMapping declaration within
	 * the CompilationUnit  
	 * @return end position in characters 
	 */	
	public int getDeclarationSourceEnd();
    
    /**
     * Is this method mapping a long version with signatures?
     * (callout with signature is a candidate for short-hand callout).
     */
    boolean hasSignature();
}
