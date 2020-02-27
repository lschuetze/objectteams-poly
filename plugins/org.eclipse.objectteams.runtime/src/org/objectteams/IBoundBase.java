/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2007-2009 Berlin Institute of Technology, Germany.
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
 *		Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.objectteams;

/**
 * Super type for all bound base classes when using the traditional weaver (OTRE).
 * Purely internal class, not intended for client use.
 * @author Stephan Herrmann
 */
public interface IBoundBase {
	/** Method to be used by generated code, only (lifting constructor). */
	void _OT$addRole(Object aRole);
	/** Method to be used by generated code, only (unregisterRole()). */
	void _OT$removeRole(Object aRole);
}