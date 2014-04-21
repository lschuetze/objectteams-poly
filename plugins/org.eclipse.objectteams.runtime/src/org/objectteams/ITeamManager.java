/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2014 Stephan Herrmann.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
