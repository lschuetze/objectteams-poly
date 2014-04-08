/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2014 Oliver Frank and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Oliver Frank - Initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode;

import org.eclipse.objectteams.otredyn.runtime.IBoundClass;
import org.eclipse.objectteams.otredyn.runtime.IMethod;


/**
 * 
 */
public class Method extends Member implements IMethod {
	
	private boolean implemented;

	public Method(String name, String signature) {
		super(name, signature);
	}

	public Method(String name, String signature, boolean isStatic, int accessFlags) {
		super(name, signature, isStatic, accessFlags);
	}

	public boolean isImplemented() {
		return implemented;
	}

	public void setImplemented(boolean implemented) {
		this.implemented = implemented;
	}
	
	/**
	 * Returns a globally unique id for the method
	 * @param clazz
	 * @return the ID
	 */
	@Override
	public int getGlobalId(IBoundClass clazz) {
		String key = null;
		// special treatment of private conflicts with test415_nonexistingBaseMethod3i
		// see also AbstractBoundClass.handleTaskList()
		if (/*isPrivate() ||*/ isStatic() || "<init>".equals(getName())) { // also no dynamic binding for constructors
			key = clazz.getId() + getName() + getSignature();
		} else {
			key = getName() + getSignature();
		}
		
		return getId(key);
	}
}
