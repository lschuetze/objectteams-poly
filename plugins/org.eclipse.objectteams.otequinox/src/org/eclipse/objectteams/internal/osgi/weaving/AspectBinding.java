/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009, 2013 Germany and Technical University Berlin, Germany and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 		Technical University Berlin - Initial API and implementation
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.otequinox.TransformerPlugin.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.otequinox.ActivationKind;
import org.objectteams.Team;
import org.osgi.framework.Bundle;
import org.osgi.framework.hooks.weaving.WovenClass;

/** 
 * Each instance of this class represents the information read from one extension to org.eclipse.objectteams.otequinox.aspectBindings.
 * Already during {@link AspectBindingRegistry#loadAspectBindings()} the string based information is resolved into instances
 * of {@link TeamBinding} and {@link BaseBundle}.
 * @author stephan
 * @since 1.3.0 (was a nested class before that) 
 */
@NonNullByDefault
public class AspectBinding {

	class TeamBinding {

		String teamName;
		@Nullable Class<? extends Team> teamClass;

		@Nullable String superTeamName;
		@Nullable TeamBinding superTeam;
		final List<TeamBinding> subTeams = new ArrayList<>();
		Set<TeamBinding> equivalenceSet = new HashSet<>();

		ActivationKind activation;
		boolean isActivated;

		boolean importsAdded;
		boolean importsAddedToSuper;
		boolean importsAddedToSub;

		final List<String> baseClassNames = new ArrayList<>();

		public TeamBinding(String teamName, ActivationKind activationKind, @Nullable String superTeamName) {
			this.teamName = teamName;
			this.activation = activationKind;
			this.superTeamName = superTeamName;
			this.equivalenceSet.add(this);
		}

		/** After scanning class file attributes: add the names of all bound base classes. */
		public void addBaseClassNames(Collection<String> baseClassNames) {
			this.baseClassNames.addAll(baseClassNames);
			for (String baseClassName : baseClassNames) {
				Set<TeamBinding> teams = baseBundle.teamsPerBase.get(baseClassName);
				if (teams == null)
					baseBundle.teamsPerBase.put(baseClassName, teams = new HashSet<>());
				teams.add(this);
			}
		}

		@SuppressWarnings("unchecked")
		public @Nullable Class<? extends Team> loadTeamClass(Bundle fallbackBundle) {
			if (teamClass != null) return teamClass;
			for (String candidate : TeamLoader.possibleTeamNames(teamName)) {
				try {
					Bundle aspectBundle = AspectBinding.this.aspectBundle;
					// FIXME: no aspectBundle if no PackageAdmin was found, is using the fallbackBundle OK?
					if (aspectBundle == null)
						aspectBundle = fallbackBundle;
					Class<?> result = aspectBundle.loadClass(candidate);
					if (result != null)
						return this.teamClass = (Class<? extends Team>) result;
				} catch (NoClassDefFoundError|ClassNotFoundException e) {
					e.printStackTrace();
					// keep looking
				}
			}
			return null;
		}

		/**
		 * Add imports to this team's package into the bundle of the given base class.
		 * @param baseClass
		 * @param direction 1 means traveling to supers, -1 means traveling to subs, 0 comprises both
		 */
		public void addImportTo(WovenClass baseClass, int direction) {
			importsAdded = true;
			List<String> imports = baseClass.getDynamicImports();
			String packageOfTeam = "";
			int dot = teamName.lastIndexOf('.'); // TODO: can we detect if thats really the package (vs. Outer.Inner)?
			if (dot != -1)
				packageOfTeam = teamName.substring(0, dot);
			imports.add(packageOfTeam);
			log(IStatus.INFO, "Added dependency from base "+baseClass.getClassName()+" to package '"+packageOfTeam+"'");
			if (direction != -1) {
				importsAddedToSuper = true;
				final TeamBinding superTeam2 = superTeam;
				if (superTeam2 != null)
					superTeam2.addImportTo(baseClass, 1);
			}
			if (direction != 1) {
				importsAddedToSub = true;
				for (TeamBinding subTeam : subTeams)
					subTeam.addImportTo(baseClass, -1);
			}
		}

		/** Has all work for this team been done? */
		public boolean isDone() { // TODO travel up/down?
			if (activation != ActivationKind.NONE && !isActivated)
				return false;
			if (!(importsAdded && importsAddedToSub && importsAddedToSuper))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "team "+teamName+"("+(this.activation)+") super "+superTeamName;
		}
	}
	
	/**
	 * Represents a base bundle against which one or more aspectBindings are declared.
	 * Used to find all teams affecting any given base class in this base bundle.
	 */
	static class BaseBundle {
		String bundleName;
		/** Team classes indexed by base classes that should trigger activating the team. */
		private HashMap<String, Set<TeamBinding>> teamsPerBase = new HashMap<>();		

		public BaseBundle(String bundleName) {
			this.bundleName = bundleName;
		}
	}
	
	public String aspectPlugin;
	public @Nullable Bundle aspectBundle; // null if we don't have a PackageAdmin for bundle lookup
	public String basePluginName;
	public BaseBundle baseBundle;
	public @Nullable IConfigurationElement[] forcedExports; // not yet evaluated
	public TeamBinding[]   teams;

	public boolean hasScannedTeams;
	
	Set<TeamBinding> teamsInProgress = new HashSet<>(); // TODO cleanup teams that are done
	
	public AspectBinding(String aspectId, @Nullable Bundle aspectBundle, BaseBundle baseBundle, @Nullable IConfigurationElement[] forcedExportsConfs, int count) 
	{
		this.aspectPlugin= aspectId;
		this.aspectBundle= aspectBundle;
		this.baseBundle=     baseBundle;
		this.basePluginName= baseBundle.bundleName;
		this.forcedExports= forcedExportsConfs;
		
		this.teams = new TeamBinding[count];
	}

	/** Create an initial (unconnected) resolved team binding. */
	public TeamBinding createResolvedTeam(int count, String teamName, @Nullable String activationSpecifier, @Nullable String superTeamName) {
		ActivationKind kind = ActivationKind.NONE;
		try {
			if (activationSpecifier != null)
				kind = ActivationKind.valueOf(activationSpecifier);
		} catch (IllegalArgumentException iae) {	
			log(iae, "Invalid activation kind "+activationSpecifier+" for team "+teamName);
		}
		return this.teams[count] = new TeamBinding(teamName, kind, superTeamName);
	}

	/** Connect all resolvable info in all contained TeamBindings using the given lookup table. */
	public void connect(Map<String, Set<TeamBinding>> teamLookup) {
		for (int i = 0; i < teams.length; i++) {
			TeamBinding team = teams[i];

			// do we have TeamBindings representing the same team class (from different aspect bindings)?
			Set<TeamBinding> equivalenceSet = teamLookup.get(team.teamName);
			if (equivalenceSet != null)
				team.equivalenceSet.addAll(equivalenceSet);
			if (team.equivalenceSet.size() > 1)
				log(IStatus.INFO, "team "+team.teamName+" participates in "+team.equivalenceSet.size()+" aspect bindings.");

			if (team.superTeamName != null) {
				Set<TeamBinding> superTeams = teamLookup.get(team.superTeamName);
				if (superTeams != null) {
					for (TeamBinding superTeam : superTeams) {
						team.superTeam = superTeam;
						superTeam.subTeams.add(team);
					}
				} else {
					Exception e = new Exception("No such aspect binding");
					log(e, "Class "+team.superTeamName+" not registered (declared to be superclass of team "+team.teamName);
				}
			}
		}		
	}

	/** Answer the names of teams to load for a given base class. */
	public synchronized @Nullable Collection<TeamBinding> getTeamsForBase(String baseClassName) {
		Set<TeamBinding> teams = baseBundle.teamsPerBase.get(baseClassName);
		// in case any team cannot immediately be instantiated/activated
		// it will be added to the next queue: OTWeavingHook.deferredTeams.
		if (teams != null) {
			teams =  new HashSet<>(teams);
			teams.removeAll(teamsInProgress); // no double-triggering
			teamsInProgress.addAll(teams);
		}
		return teams;		
	}

	/** If a given team requires no activation, check if its super team should be activated instead. */
	public @Nullable TeamBinding getOtherTeamToActivate(TeamBinding team) {
		TeamBinding superTeam = team.superTeam;
		if (superTeam != null && superTeam.activation != ActivationKind.NONE) {
			return superTeam;
		}
		// sub teams?
		return null;
	}

	/**
	 * Read OT attributes of all teams in this aspectBinding 
	 * and collect affected base classes into the teamBindings.
	 */
	public synchronized void scanTeamClasses(Bundle bundle) {
		ClassScanner scanner = new ClassScanner();
		for (@SuppressWarnings("null")@NonNull TeamBinding team : getAllTeamBindings()) {
			try {
				String teamName = scanner.readOTAttributes(bundle, team.teamName);
				Collection<String> baseClassNames = scanner.getCollectedBaseClassNames();
				if (team.baseClassNames.isEmpty())
					addBaseClassNames(teamName, baseClassNames);
				log(IStatus.INFO, "Scanned team class "+teamName+", found "+baseClassNames.size()+" base classes");
			} catch (Exception e) {
				log(e, "Failed to scan team class "+team.teamName);
			}
		}
		this.hasScannedTeams = true;
	}

	private List<TeamBinding> getAllTeamBindings() {
		List<TeamBinding> all = new ArrayList<>();
		for (TeamBinding team : teams) all.add(team);
		for (int i = 0; i < teams.length; i++) {
			if (teams[i].superTeam != null)
				all.add(teams[i].superTeam);
			all.addAll(teams[i].subTeams);
		}
		return all;
	}

	private void addBaseClassNames(String teamName, Collection<String> baseClassNames) {
		for (int i = 0; i < teams.length; i++) {
			TeamBinding team = teams[i];
			if (team.teamName.equals(teamName)) {
				for (TeamBinding equivalent : team.equivalenceSet)
					equivalent.addBaseClassNames(baseClassNames);
				return;
			}
		}
		// try super:
		for (int i = 0; i < teams.length; i++) {
			TeamBinding team = teams[i];
			TeamBinding superTeam = team.superTeam;
			if (superTeam != null && superTeam.teamName.equals(teamName)) {
				for (TeamBinding equivalentSuper : superTeam.equivalenceSet)
					equivalentSuper.addBaseClassNames(baseClassNames);
				return;
			}
		}
	}

	public List<String> getBasesPerTeam(String teamName) {
		for (int i = 0; i < this.teams.length; i++) {
			TeamBinding team = this.teams[i];
			if (team.teamName.equals(teamName))
				return team.baseClassNames;
		}
		@SuppressWarnings("null")@NonNull // well-known library function
		List<String> emptyList = Collections.emptyList();
		return emptyList;		
	}

	/** Add all require imports to match the hidden reverse dependency created by any team binding to this base class. */
	public void addImports(WovenClass baseClass) {
		String baseClassName = baseClass.getClassName();
		Set<TeamBinding> teams = baseBundle.teamsPerBase.get(baseClassName);
		if (teams != null)
			for (TeamBinding resolvedTeam : teams)
				resolvedTeam.addImportTo(baseClass, 0); // 0 = travel both directions (sub/super)
	}

	public void cleanUp(String baseClass) {
		baseBundle.teamsPerBase.remove(baseClass);
	}

	public boolean isDone() {
		for (int i = 0; i < teams.length; i++)
			if (!teams[i].isDone())
				return false;
		return true;
	}

	public String toString() {
		String result = "\tbase plugin "+basePluginName+"\n\tadapted by aspect pluging "+aspectPlugin;
		for (TeamBinding team : teams)
			result += "\n\t\t "+team.toString();
		return result;
	}
}