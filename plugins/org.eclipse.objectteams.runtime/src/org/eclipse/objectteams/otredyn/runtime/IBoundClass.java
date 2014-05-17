/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2011, 2014 GK Software AG and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.runtime;

/**
 * Interface through which the {@link TeamManager} reaches into the OTREDyn.
 * Representation of a class that participates in a binding.
 * 
 * @author stephan
 */
public interface IBoundClass {

	/**
	 * Returns the identifier of the class
	 * @return
	 */
	String getId();

	/** Answer whether this class is an anonymous subclass. */
	boolean isAnonymous();

	/**
	 * Returns the method of this class, with the specified name
	 * and signature. If the class is not already loaded yet, the method
	 * is created. 
	 * This method ensures, that it returns the same instance
	 * of {@link Method} for the same tuple of name and desc. More formally:
	 * if (name1 + desc1).equals(name2 + desc2) then 
	 * getMethod(name1, desc1) == getMethod(name2, desc2) 
	 * @param flags see IBinding.STATIC_BASE and IBinding.CALLIN_BASE
	 * @param handleCovariantReturn	whether or not methods with covariant return type should be considered
	 * @param name the name of the method
	 * @param desc the signature of the method (JVM encoding)
	 * @return
	 */
	IMethod getMethod(String memberName, String memberSignature, int flags, boolean handleCovariantReturn);

	/**
	 * This method creates a globally unique identifier for the method
	 * @param method
	 * @return
	 */
	String getMethodIdentifier(IMethod method);

	/**
	 * Returns the field of this class, with the specified name
	 * and signature. If the class is not already loaded yet, the field
	 * is created.
	 * This method ensures, that it returns the same instance
	 * of {@link Field} for the same tupel of name and desc. More formally:
	 * if (name1 + desc1).equals(name2 + desc2) then 
	 * getField(name1, desc1) == getField(name2, desc2)
	 * @param name the name of the method
	 * @param desc the siganture of the method
	 * @return
	 */
	IMember getField(String memberName, String memberSignature);

	/**
	 * Handle a new binding a decides, weather weaving is neede or not
	 * @param binding the new binding
	 */
	void handleAddingOfBinding(IBinding binding);

	/** 
	 * Add a {@link ISubclassWiringTask wiring task} to be performed when a 
	 * newly loaded class is linked to this class as its super class.
	 */
	void addWiringTask(ISubclassWiringTask subclassWiringTask);

}
