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

import static org.eclipse.objectteams.otequinox.TransformerPlugin.log;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBinding.TeamBinding;
import org.eclipse.objectteams.otequinox.ActivationKind;
import org.eclipse.objectteams.otequinox.TransformerPlugin;
import org.objectteams.Team;
import org.osgi.framework.Bundle;
import org.osgi.framework.hooks.weaving.WovenClass;

/**
 * This class triggers the actual loading/instantiation/activation of teams.
 * <p>
 * It implements a strategy of deferring those teams that are not ready for
 * instantiating / activating, which we check by comparing the sets of
 * bound base classes of the team vs. the set of classes currently being
 * processed by class loading & weaving.
 * This reflects that fact that classes for which loading has already been
 * started would otherwise trigger an irrecoverable NoClassDefFoundError. 
 * </p><p>
 * Which teams participate in deferred instantiation is communicated via the 
 * shared list {@link #deferredTeams}.
 * </p>
 */
@NonNullByDefault
public class TeamLoader {

	/** Collect here any teams that cannot yet be handled and should be scheduled later. */
	private List<WaitingTeamRecord> deferredTeams;

	private Set<String> beingDefined;
	
	public TeamLoader(List<WaitingTeamRecord> deferredTeams, Set<String> beingDefined) {
		this.deferredTeams = deferredTeams;
		this.beingDefined = beingDefined;
	}

	/**
	 * Team loading, 1st attempt before the base class is even loaded
	 * Trying to do these phases: load (now) instantiate/activate (if ready),
	 */
	public void loadTeamsForBase(Bundle aspectBundle, AspectBinding aspectBinding, WovenClass baseClass) {
		@SuppressWarnings("null")@NonNull String className = baseClass.getClassName();
		Collection<TeamBinding> teamsForBase = aspectBinding.getTeamsForBase(className);
		if (teamsForBase == null) 
			return; // not done
		for (TeamBinding teamForBase : teamsForBase) {
			if (teamForBase.isActivated) continue;
			// Load:
			Class<? extends Team> teamClass;
			teamClass = teamForBase.loadTeamClass(aspectBundle);
			if (teamClass == null) {
				log(new ClassNotFoundException("Not found: "+teamForBase), "Failed to load team "+teamForBase);
				continue;
			}
			// Try to instantiate & activate, failures are recorded in deferredTeams
			ActivationKind activationKind = teamForBase.activation;
			if (activationKind == ActivationKind.NONE) {
				teamForBase = aspectBinding.getOtherTeamToActivate(teamForBase);
				if (teamForBase != null) {
					if (teamForBase.isActivated) continue;
					activationKind = teamForBase.activation;
					teamClass = teamForBase.loadTeamClass(aspectBundle);
					if (teamClass == null) {
						log(new ClassNotFoundException("Not found: "+teamForBase.teamName+" in bundle "+aspectBundle.getSymbolicName()), "Failed to load team "+teamForBase);
						continue;						
					}
				} else {
					continue;
				}
			}
			if (activationKind == ActivationKind.NONE) 
				continue;
			instantiateAndActivate(aspectBinding, teamForBase, activationKind);
		}
	}

	public static @Nullable Pair<URL,String> findTeamClassResource(String className, Bundle bundle) {
		for (String candidate : possibleTeamNames(className)) {
			URL result = bundle.getResource(candidate.replace('.', '/')+".class");
			if (result != null)
				return new Pair<>(result, candidate);
		}
		return null;
	}

	/** 
	 * Starting from currentName compute a list of potential binary names of (nested) teams
	 * using "$__OT__" as the separator, to find class parts of nested teams.  
	 */
	public static List<String> possibleTeamNames(String currentName) {
		List<String> result = new ArrayList<String>();
		result.add(currentName);
		char sep = '.'; // assume source name
		if (currentName.indexOf('$') > -1)
			// binary name
			sep = '$';
		int from = currentName.length()-1;
		while (true) {
			int pos = currentName.lastIndexOf(sep, from);
			if (pos == -1)
				break;
			String prefix = currentName.substring(0, pos); 
			String postfix = currentName.substring(pos+1);
			if (sep=='$') {
				if (!postfix.startsWith("__OT__"))
					result.add(0, currentName = prefix+"$__OT__"+postfix);
			} else {
				// heuristic: 
				// only replace if parent element looks like a class (expected to start with uppercase)
				int prevDot = prefix.lastIndexOf('.');
				if (prevDot > -1 && Character.isUpperCase(prefix.charAt(prevDot+1))) 
					result.add(0, currentName = prefix+"$__OT__"+postfix);
				else 
					break;
			}
			from = pos-1;
		}
		return result;
	}

	/**
	 * Check if the given team is ready. If so instantiate it and if activationKind requires also activate it.
	 */
	void instantiateAndActivate(AspectBinding aspectBinding, TeamBinding team, ActivationKind activationKind)
	{
		String teamName = team.teamName;
		// don't try to instantiate before all base classes successfully loaded.
		synchronized(aspectBinding) {
			if (!isReadyToLoad(aspectBinding, team, teamName, activationKind))
				return;
			team.isActivated = true;
		}

		try {
			@SuppressWarnings("null")@NonNull Team instance = team.teamClass.newInstance();
			TransformerPlugin.registerTeamInstance(instance);
			log(IStatus.INFO, "Instantiated team "+teamName);
			
			try {
				switch (activationKind) {
				case ALL_THREADS:
					instance.activate(Team.ALL_THREADS);
					log(IStatus.INFO, "Activated team "+teamName);
					break;
				case THREAD:
					instance.activate();
					log(IStatus.INFO, "Activated team "+teamName);
					break;
				//$CASES-OMITTED$
				default:
					break;
				}
			} catch (Throwable t) {
				// application errors during activation
				log(t, "Failed to activate team "+teamName);
			}
		} catch (Throwable e) {
			// application error during constructor execution?
			log(e, "Failed to instantiate team "+teamName);
		}
	}

	private boolean isReadyToLoad(AspectBinding aspectBinding,
									TeamBinding team, String teamName,
									ActivationKind activationKind)
	{
		for (@SuppressWarnings("null")@NonNull String baseclass : aspectBinding.getBasesPerTeam(teamName)) {
			if (this.beingDefined.contains(baseclass)) {
				synchronized (deferredTeams) {
					WaitingTeamRecord record = new WaitingTeamRecord(team, aspectBinding, activationKind, baseclass);
					deferredTeams.add(record); // TODO(SH): synchronization, deadlock? performed while holding lock an aspectBinding
				}
				log(IStatus.INFO, "Defer instantation/activation of team "+teamName);
				return false;
			}
		}
		return true;
	}
}
