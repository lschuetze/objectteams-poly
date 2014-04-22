/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2011, 2012 GK Software AG and others.
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
 * Representation of a repository of {@link IBoundClass}es.
 * 
 * @author stephan
 */
public interface IClassRepository {

	/**
	 * Returns a instance of AbstractBoundClass for the
	 * given FQN and id. If there is no instance it
	 * is created. The class have not to be loaded to get
	 * an instance of AbstractBoundClass representing this class.
	 * It guarantees, that it returns always the same 
	 * instance for the same id. More formally:
	 * if id1.equals(id2) then getBoundClass(..., id1) == getBoundClass(id2) 
	 * @param className the name of the class
	 * @param id a globally unique identifier for the class 
	 * @return
	 */
	public IBoundClass getBoundClass(String className, String id, ClassLoader loader);

	/**
	 * Returns a instance of AbstractBoundClass for the
	 * given FQN and id and sets the bytecode for this class. 
	 * If there is no instance it is created. 
	 * This class should be called while loading the class.
	 * It guarantees, that it returns always the same 
	 * instance for the same id. More formally:
	 * if id1.equals(id2) then getBoundClass(..., id1) == getBoundClass(..., id2) 
	 * @param className the name of the class
	 * @param id a globally unique identifier for the class 
	 * @return
	 */
	public IBoundClass getBoundClass(String className, String id, byte[] classBytes, ClassLoader loader);

	/**
	 * Returns a instance of AbstractTeam for the
	 * given FQN and id. If there is no instance it
	 * is created. 
	 * It guarantees, that it returns always the same 
	 * instance for the same id. More formally:
	 * if id1.equals(id2) then getTeam(..., id1) == getTeam(..., id2).
	 * This method should only be called, if it is known
	 * that the name identifies a team and not a class.
	 * Otherwise call getBoundClass().isTeam() to check this. 
	 * @param teamName the name of the team
	 * @param id a globally unique identifier for the team 
	 * @return
	 */
	public IBoundTeam getTeam(String teamName, String id, ClassLoader loader);

}