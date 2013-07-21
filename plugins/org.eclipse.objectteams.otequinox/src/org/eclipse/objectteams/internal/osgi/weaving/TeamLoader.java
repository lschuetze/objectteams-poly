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
import org.eclipse.objectteams.otequinox.ActivationKind;
import org.eclipse.objectteams.otequinox.TransformerPlugin;
import org.eclipse.objectteams.otequinox.hook.ILogger;
import org.objectteams.Team;
import org.osgi.framework.Bundle;
import org.osgi.framework.hooks.weaving.WovenClass;

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
	 * Trying to do these phases: load/instantiate/activate (if ready),
	 * and also adds a reverse import to the base (always).
	 */
	public boolean loadTeamsForBase(Bundle aspectBundle, AspectBinding aspectBinding, WovenClass baseClass) {
		@SuppressWarnings("null")@NonNull String className = baseClass.getClassName();
		Collection<String> teamsForBase = aspectBinding.getTeamsForBase(className);
		if (teamsForBase == null) 
			return false; // not done
		List<String> imports = baseClass.getDynamicImports();
		for (String teamForBase : teamsForBase) {
			// Add dependency:
			String packageOfTeam = "";
			int dot = teamForBase.lastIndexOf('.');
			if (dot != -1)
				packageOfTeam = teamForBase.substring(0, dot);
			imports.add(packageOfTeam);
			log(IStatus.INFO, "Added dependency from base "+baseClass.getClassName()+" to package '"+packageOfTeam+"'");
			// Load:
			Class<? extends Team> teamClass;
			teamClass = findTeamClass(teamForBase, aspectBundle);
			if (teamClass == null) {
				log(new ClassNotFoundException("Not found: "+teamForBase), "Failed to load team "+teamForBase);
				continue;
			}
			// Try to instantiate & activate, failures are recorded in deferredTeams
			ActivationKind activationKind = aspectBinding.getActivation(teamForBase);
			if (activationKind == ActivationKind.NONE)
				continue;
			Team teamInstance = instantiateAndActivate(aspectBinding, teamClass, activationKind);
			if (teamInstance == null)
				continue;
		}
		return true; // all activatable teams have been activated or added to deferredTeams
	}

	/** Team loading, subsequent attempts. */
	public void instantiateWaitingTeam(WaitingTeamRecord record)
			throws InstantiationException, IllegalAccessException 
	{
		// Instantiate (we only get here if activationKind != NONE)
		Class<? extends Team> teamClass = record.teamClass;
		assert teamClass != null : "cannot be null if teamInstance is null";
		instantiateAndActivate(record.aspectBinding, teamClass, record.activationKind);
	}

	public static @Nullable Pair<URL,String> findTeamClassResource(String className, Bundle bundle) {
		for (String candidate : possibleTeamNames(className)) {
			URL result = bundle.getResource(candidate.replace('.', '/')+".class");
			if (result != null)
				return new Pair<>(result, candidate);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static @Nullable Class<? extends Team> findTeamClass(String className, Bundle bundle) {
		for (String candidate : possibleTeamNames(className)) {
			try {
				Class<?> result = bundle.loadClass(candidate);
				if (result != null)
					return (Class<? extends Team>) result;
			} catch (NoClassDefFoundError|ClassNotFoundException e) {
				// keep looking
			}
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

	@Nullable Team instantiateAndActivate(AspectBinding aspectBinding, Class<? extends Team> teamClass, ActivationKind activationKind) 
	{
		@SuppressWarnings("null")@NonNull String teamName = teamClass.getName();
		// don't try to instantiate before all base classes successfully loaded.
		synchronized(aspectBinding) {
			if (!isReadyToLoad(aspectBinding, teamClass, teamName, activationKind))
				return null;
			aspectBinding.markAsActivated(teamName);
		}

		try {
			@SuppressWarnings("null")@NonNull Team instance = teamClass.newInstance();
			TransformerPlugin.registerTeamInstance(instance);
			log(ILogger.INFO, "Instantiated team "+teamName);
			
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

			return instance;
		} catch (Throwable e) {
			// application error during constructor execution?
			log(e, "Failed to instantiate team "+teamName);
		}
		return null;
	}

	boolean isReadyToLoad(AspectBinding aspectBinding,
			Class<? extends Team> teamClass, String teamName,
			ActivationKind activationKind)
	{
		for (@SuppressWarnings("null")@NonNull String baseclass : aspectBinding.basesPerTeam.get(teamName)) {
			if (this.beingDefined.contains(baseclass)) {
				synchronized (deferredTeams) {
					WaitingTeamRecord record = new WaitingTeamRecord(teamClass, aspectBinding, activationKind, baseclass);
					deferredTeams.add(record); // TODO(SH): synchronization, deadlock? performed while holding lock an aspectBinding
				}
				log(IStatus.INFO, "Defer instantation/activation of team "+teamName);
				return false;
			}
		}
		return true;
	}
}
