/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2012 Oliver Frank and others.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Oliver Frank - Initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode;

import java.lang.instrument.UnmodifiableClassException;

/**
 * Classes that implements this interface can redefine classes at runtime.
 * @author Oliver Frank
 */
public interface IRedefineStrategy {
	
	/**
	 * Redefines a class at runtime. 
	 * @param clazz the existing class instance
	 * @param bytecode the new bytecode for this class 
	 * @throws ClassNotFoundException the class was not loaded yet
	 * @throws UnmodifiableClassException it is impossible to redefine the class
	 */
	public void redefine(Class<?> clazz, byte[] bytecode) throws ClassNotFoundException, UnmodifiableClassException;
}
