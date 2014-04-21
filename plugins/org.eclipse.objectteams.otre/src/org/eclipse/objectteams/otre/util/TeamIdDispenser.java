/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2002-2009 Berlin Institute of Technology, Germany.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: TeamIdDispenser.java 23408 2010-02-03 18:07:35Z stephan $
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
