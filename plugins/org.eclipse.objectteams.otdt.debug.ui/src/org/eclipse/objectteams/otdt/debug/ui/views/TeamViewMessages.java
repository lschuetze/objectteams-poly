/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
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
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug.ui.views;

import org.eclipse.osgi.util.NLS;

public class TeamViewMessages extends NLS
{
	private static final String BUNDLE_NAME = "org.eclipse.objectteams.otdt.debug.ui.views.TeamViewMessages"; //$NON-NLS-1$

	public static String SortTeamByName_0;

	public static String SortTeamByActivation_0;
	
	public static String SortTeamByInstantiation_0;

	public static String SortTeamDescription_0;
	
	public static String TeamView_0;

	public static String TeamView_1;
	
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, TeamViewMessages.class);
	}

	private TeamViewMessages()
	{
	}
}
