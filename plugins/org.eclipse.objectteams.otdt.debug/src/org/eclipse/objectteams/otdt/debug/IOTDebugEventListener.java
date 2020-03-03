/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
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
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug;

import org.eclipse.objectteams.otdt.debug.internal.TeamBreakpointListener;

/**
 * This interface defines a protocol between the {@link TeamBreakpointListener} (source)
 * and the Team Monitor View (in plugin org.eclipse.objectteams.otdt.debug.ui) (sink)
 * in order to turn events that reflect the instantiation and activation state of teams
 * into updates of the Team Monitor View.
 * 
 * Note that the TeamView directly observes termination of a launch by implementing
 * ILaunchesListener2 (no need to involve this interface for that). 
 *  
 * @author gis
 */
public interface IOTDebugEventListener
{
	/**
	 * A team instance has been instantiated in the debug target.
	 * @param newTeam the new team instance
	 */
    public void teamInstantiated(TeamInstance newTeam);
    /**
     * A team instance has been disposed in the debug target (finalize was called).
     * @param idx position of the element within the viewer, 
     * 	corresponds also with the order in {@link org.eclipse.objectteams.otdt.debug.OTDebugElementsContainer#_teamInstances}.
     */
    public void teamDisposed(int idx);
    /**
     * The activation state of a team instance has changed.
     * @param teamInstance the team instance
     */
    public void activationStateChanged(TeamInstance teamInstance);
}
