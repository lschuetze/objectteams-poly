/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2013 GK Software AG
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.objectteams.otequinox.ActivationKind;
import org.objectteams.Team;

/** Record for one team waiting for instantiation & activation. */
@NonNullByDefault
class WaitingTeamRecord {
	Class<? extends Team> teamClass;
	AspectBinding aspectBinding;
	ActivationKind activationKind;
	String notFoundClass;
	
	public WaitingTeamRecord(Class<? extends Team> teamClass, AspectBinding aspectBinding, ActivationKind activationKind, String notFoundClass) {
		this.teamClass = teamClass;
		this.aspectBinding = aspectBinding;
		this.notFoundClass = notFoundClass;
		this.activationKind = activationKind;
	}

	@SuppressWarnings("null") // calling well-known library function
	public String getTeamName() {
		return teamClass.getName();
	}		
}