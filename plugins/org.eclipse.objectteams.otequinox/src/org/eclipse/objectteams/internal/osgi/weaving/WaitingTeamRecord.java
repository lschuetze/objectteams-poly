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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.otequinox.ActivationKind;
import org.objectteams.Team;

/** Record for one team waiting for instantiation/activation. */
@NonNullByDefault
class WaitingTeamRecord {
	@Nullable Class<? extends Team> teamClass; // ... either this is set
	@Nullable Team teamInstance;				// ... or this
	AspectBinding aspectBinding;
	ActivationKind activationKind;
	String notFoundClass;
	
	public WaitingTeamRecord(Class<? extends Team> teamClass, AspectBinding aspectBinding, ActivationKind activationKind, String notFoundClass) {
		this.teamClass = teamClass;
		this.aspectBinding = aspectBinding;
		this.notFoundClass = notFoundClass;
		this.activationKind = activationKind;
	}
	public WaitingTeamRecord(Team teamInstance, AspectBinding aspectBinding, ActivationKind activationKind, String notFoundClass) {
		this.teamInstance = teamInstance;
		this.aspectBinding = aspectBinding;
		this.notFoundClass = notFoundClass;
		this.activationKind = activationKind;
	}
	public WaitingTeamRecord(WaitingTeamRecord record, String notFoundClass) {
		this.teamClass = record.teamClass;
		this.teamInstance = record.teamInstance;
		this.aspectBinding = record.aspectBinding;
		this.activationKind = record.activationKind;
		this.notFoundClass = notFoundClass;
	}
	@SuppressWarnings("null") // calling well-known library functions
	public String getTeamName() {
		final Class<? extends Team> clazz = teamClass;
		if (clazz != null) {
			return clazz.getName();
		} else {
			final Team instance = teamInstance;
			if (instance != null)
				return instance.getClass().getName();
		}
		return "<unknown team>";
	}		
}