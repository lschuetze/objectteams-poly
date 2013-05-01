/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 *
 * Copyright 2008, 2010 Berlin Institute of Technology, Germany, and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 *		Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.objectteams;

/**
 * Marker interface: if a role declares to implement this interface
 * the compiler will generate the method defined herein, and prepare
 * the role so that the migration will indeed be possible.
 *
 * @author stephan
 * @since 1.2.5
 */
public interface IBaseMigratable {
	/**
	 * Migrate the current role to the otherBase.
	 *
	 * @param otherBase new base that this role should adapt, must
	 *        be of a valid base type for the current role.
	 */
	<B> void migrateToBase(B otherBase);
}
