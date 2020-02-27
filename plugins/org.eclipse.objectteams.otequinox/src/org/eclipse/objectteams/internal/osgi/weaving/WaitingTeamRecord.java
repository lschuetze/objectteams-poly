/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2013, 2014 GK Software AG
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
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBinding.TeamBinding;
import org.eclipse.objectteams.otequinox.ActivationKind;

/** Record for one team waiting for instantiation & activation. */
@NonNullByDefault
class WaitingTeamRecord {
	TeamBinding team;
	AspectBinding aspectBinding;
	ActivationKind activationKind;
	String notFoundClass;
	
	public WaitingTeamRecord(TeamBinding team, ActivationKind activationKind, String notFoundClass) {
		this.team = team;
		this.aspectBinding = team.getAspectBinding();
		this.notFoundClass = notFoundClass;
		this.activationKind = activationKind;
	}	
}