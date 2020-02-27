/**********************************************************************
 * This file is part of "Object Teams Runtime Environment"
 *
 * Copyright 2007, 2012 Berlin Institute of Technology, Germany, and others.
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
 * 		Oliver Frank - Initial API and Implementation
 **********************************************************************/
package org.objectteams;

/**
 * Super type for all bound base classes when using the "Object Teams Dynamic Runtime Environment".
 * Purely internal class, not intended for client use.
 */
public interface IBoundBase2 {

	/**
	 * Call a bound base method identified by its ID.
	 * @param boundMethod_id globally unique ID of a bound base method
	 * @param args           packed arguments (incl. boxing)
	 * @return               (possibly boxed) result of the bound base method.
	 */
	Object _OT$callOrig(int boundMethod_id, Object[] args);

	/**
	 * Provides access to fields and methods of a base class
	 * that have package, protected or private visbility
	 * @param accessId Unique identifier in the class for the field or method
	 * @param opKind 0 for read access, 1 for write access. only used for fields
	 * @param args arguments for a method
	 * @param caller the team containing the calling role
	 * @return
	 */
	Object _OT$access(int accessId, int opKind, Object[] args, ITeam caller);

	/** Method to be used by generated code, only (lifting constructor & unregisterRole()). */
	void _OT$addOrRemoveRole(Object aRole, boolean adding);
}