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
 * Exception to be thrown when a bound role is being instantiated but
 * the constructor does not assign a base object.
 *
 * @author stephan
 */
@SuppressWarnings("serial")
public class IllegalRoleCreationException extends RuntimeException {
	public IllegalRoleCreationException() {
		super();
	}

	@Override
	public String getMessage() {
		return "Cannot instantiate a bound role using a default constructor of its tsuper class";
	}
}
