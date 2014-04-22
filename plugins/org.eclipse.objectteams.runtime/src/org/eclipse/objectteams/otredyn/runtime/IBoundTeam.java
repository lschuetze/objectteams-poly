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

import java.util.Collection;

/**
 * Interface through which the {@link TeamManager} reaches into the OTREDyn.
 * Representation of a team class.
 * 
 * @author stephan
 */
public interface IBoundTeam {

	/**
	 * Returns all bindings this team have got.
	 * Parses the bytecode, if thats needed
	 * @return
	 */
	Collection<IBinding> getBindings();

	/**
	 * Returns the highest access defined by this team.
	 * The access id is a unique identifier for a base member
	 * in the team
	 * @return
	 */
	int getHighestAccessId();

}
