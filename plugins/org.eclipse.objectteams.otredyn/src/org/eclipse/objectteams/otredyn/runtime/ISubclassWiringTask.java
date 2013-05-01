/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2011, 2012 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.runtime;


/**
 * Interface for tasks that need to be performed when wiring a class to one of its subclasses.
 * @author stephan
 */
public interface ISubclassWiringTask {

	/** Perform necessary action for the given pair of classes. */
	public void wire(IBoundClass superClass, IBoundClass subClass);
	
}
