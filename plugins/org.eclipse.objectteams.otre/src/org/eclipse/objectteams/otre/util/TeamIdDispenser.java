/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2002-2009 Berlin Institute of Technology, Germany.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Please visit http://www.objectteams.org for updates and contact.
 *
 * Contributors:
 * Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otre.util;
import java.util.*;

public class TeamIdDispenser {
  
    static int lastDispensedId = 0;
    private static HashMap<String, Integer> teamIDs = new HashMap<String, Integer>();

	private static int produceNextTeamId(String team_name) {
        lastDispensedId++;
        Integer teamId = Integer.valueOf(lastDispensedId);
        teamIDs.put(team_name, teamId);
        return lastDispensedId;
    }

    synchronized public static int getTeamId(String class_name) {
        Integer teamId = teamIDs.get(class_name);
        if (teamId != null)
        	// the team <class_name> already has a team-id assigned
        	return teamId.intValue();
		else return produceNextTeamId(class_name);
    }
}
