/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2004-2009 Berlin Institute of Technology, Germany.
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
 * Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.objectteams;

/**
 * This exception is thrown if a cast to a role class fails due to
 * different enclosing team instances.
 * @author Stephan Herrmann
 */
public class RoleCastException extends ClassCastException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final String MSG =
		"Different enclosing team instances (see OT/J language definition para. 1.2.4(b)).";

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return MSG;
	}
}