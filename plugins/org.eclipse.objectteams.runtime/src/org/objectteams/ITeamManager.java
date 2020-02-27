/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2014 Stephan Herrmann.
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
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.objectteams;

/**
 * This interface encapsulates the OTDRE for callbacks from class {@link Team}.
 * NOT API.
 */
public interface ITeamManager {
	
	public static enum TeamStateChange {
		REGISTER,
		UNREGISTER
	}

	void handleTeamStateChange(ITeam aTeam, TeamStateChange register);
}
