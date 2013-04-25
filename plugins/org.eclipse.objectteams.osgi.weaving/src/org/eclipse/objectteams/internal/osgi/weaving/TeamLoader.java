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

import static org.eclipse.objectteams.osgi.weaving.Activator.log;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBindingRegistry.WaitingTeamRecord;
import org.eclipse.objectteams.osgi.weaving.ActivationKind;
import org.eclipse.objectteams.otequinox.hook.ILogger;
import org.objectteams.ITeam;
import org.objectteams.Team;
import org.osgi.framework.Bundle;

/**
 * This class triggers the actual loading/instantiation/activation of teams.
 * <p>
 * It implements a strategy of deferring those teams where instantiation/activation
 * failed (NoClassDefFoundError), which assumably happens, if one required class
 * cannot be loaded, because its loading is already in progress further down in
 * our call stack.
 * </p><p>
 * Which teams participate in deferred instantiation is communicated via the list
 * {@link #deferredTeams}.
 * </p>
 */
public class TeamLoader {

	private List<WaitingTeamRecord> deferredTeams;
	
	/** did we record the fact that a team needs deferring? */
	boolean needDeferring; 
	
	public TeamLoader(List<WaitingTeamRecord> deferredTeams) {
		this.deferredTeams = deferredTeams;
	}

	/** Team loading, 1st attempt (trying to do all three phases load/instantiate/activate). */
	public boolean loadTeams(Bundle aspectBundle, AspectBinding aspectBinding, String className) {
		Collection<String> teamsForBase = aspectBinding.getTeamsForBase(className);
		if (teamsForBase == null) return true;
		for (String teamForBase : teamsForBase) {
			// Load:
			Class<? extends ITeam> teamClass;
			try {
				teamClass = (Class<? extends ITeam>) aspectBundle.loadClass(teamForBase);
			} catch (ClassNotFoundException e) {
				log(e, "Failed to load team "+teamForBase);
				continue;
			}
			// Instantiate?
			ActivationKind activationKind = aspectBinding.getActivation(teamForBase);
			if (activationKind == ActivationKind.NONE)
				continue;
			ITeam teamInstance = instantiateTeam(aspectBinding, teamClass, teamForBase);
			if (teamInstance == null)
				continue;
			// Activate?
			activateTeam(aspectBinding, teamForBase, teamInstance, activationKind);
		}
		return !needDeferring; // TODO, need to figure out whether we're done with aspectBinding.
	}

	/** Team loading, subsequent attempts. */
	public void instantiateWaitingTeam(WaitingTeamRecord record)
			throws InstantiationException, IllegalAccessException 
	{
		ITeam teamInstance = record.teamInstance;
		String teamName = record.getTeamName();
		if (teamInstance == null) {
			// Instantiate (we only get here if activationKind != NONE)
			teamInstance = instantiateTeam(record.aspectBinding, record.teamClass, teamName);
			if (teamInstance == null)
				return;
		}
		// Activate?
		ActivationKind activationKind = record.aspectBinding.getActivation(teamName);
		activateTeam(record.aspectBinding, teamName, teamInstance, activationKind);
	}

	private @Nullable ITeam instantiateTeam(AspectBinding aspectBinding, Class<? extends ITeam> teamClass, String teamName) {
		try {
			ITeam instance = teamClass.newInstance();
			log(ILogger.INFO, "Instantiated team "+teamName);
			return instance;
		} catch (NoClassDefFoundError ncdfe) {
			needDeferring = true;
			deferredTeams.add(new WaitingTeamRecord(teamClass, aspectBinding, ncdfe.getMessage().replace('/','.')));
		} catch (Throwable e) {
			// application error during constructor execution?
			log(e, "Failed to instantiate team "+teamName);
		}
		return null;
	}

	private void activateTeam(AspectBinding aspectBinding, String teamName, ITeam teamInstance, ActivationKind activationKind)
	{
		try {
			switch (activationKind) {
			case ALL_THREADS:
				teamInstance.activate(Team.ALL_THREADS);
				break;
			case THREAD:
				teamInstance.activate();
				break;
			//$CASES-OMITTED$
			default:
				break;
			}
		} catch (NoClassDefFoundError e) {
			deferredTeams.add(new WaitingTeamRecord(teamInstance, aspectBinding, e.getMessage().replace('/','.'))); // TODO(SH): synchronization
		} catch (Throwable t) {
			// application errors during activation
			log(t, "Failed to activate team "+teamName);
		}
	}
}
