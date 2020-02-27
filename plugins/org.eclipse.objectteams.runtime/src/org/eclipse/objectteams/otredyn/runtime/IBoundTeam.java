/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2011, 2012 GK Software AG and others.
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
