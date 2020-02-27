/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2003-2009 Berlin Institute of Technology, Germany.
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
 * This exception is thrown by the OT/J infra structure if a role for a given base object
 * was requested during lifting, but a role with an incompatible type was already
 * registered for that base object. Can only happen if a compile time warning occurred.
 * @author Stephan Herrmann
 */
public class WrongRoleException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Class<?> clazz;
	private Object base;
	private Object role;

	/**
	 * @param clazz
	 * @param base
	 * @param role
	 */
	public WrongRoleException (Class<?> clazz, Object base, Object role) {
		this.clazz = clazz;
		this.base = base;
		this.role = role;
	}

	@Override
	public String getMessage() {
		String baseClazz = base.getClass().getName();
		String roleClazz = role.getClass().getName();
		return "The compiler has warned you about ambiguous role bindings.\n"
				+ "Now lifting to " + clazz
				+ " fails with the following objects\n"
				+ "(see OT/J language definition para. 2.3.4(d)):\n"
				+ "Provided:\n  Base object: " + base + "\n" + "  Base type:   "
				+ baseClazz + "\n"
				+ "Found in cache:\n  Role object: " + role + "\n"
				+ "  Role type:   " + roleClazz;
	}
}
