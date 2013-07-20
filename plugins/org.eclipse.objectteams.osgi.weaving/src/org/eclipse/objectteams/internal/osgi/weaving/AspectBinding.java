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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.otequinox.ActivationKind;
import org.osgi.framework.Bundle;

/** 
 * A simple record representing the information read from an extension to org.eclipse.objectteams.otequinox.aspectBindings.
 * @author stephan
 * @since 1.3.0 (was a nested class before that) 
 */
public class AspectBinding {
	enum State { Initial, TeamsScanned, TeamsActivated };
	
	public String aspectPlugin;
	public String basePlugin;
	public IConfigurationElement[] forcedExports;
	public ActivationKind[] activations = null; 
	public String[]         teamClasses;
	public List<String>[]   subTeamClasses;

	public State 			 state = State.Initial;
	
	/** Dispenser for team classes indexed by base classes that should trigger activating the team. */
	private HashMap<String, Set<String>> teamsPerBase = new HashMap<>();
	
	/** Lookup to find base classes affected by a team (need to be available before instantiate/activate). */
	HashMap<String, Collection<String>> basesPerTeam = new HashMap<>();
	
	Set<String> teamsInProgress = new HashSet<>(); // TODO cleanup teams that are done
	
	public AspectBinding(String aspectId, String baseId, IConfigurationElement[] forcedExportsConfs) {
		this.aspectPlugin= aspectId;
		this.basePlugin= baseId;
		this.forcedExports= forcedExportsConfs;
	}
	
	@SuppressWarnings("unchecked")
	public void initTeams(int count) {
		this.teamClasses    = new String[count];
		this.subTeamClasses = new List[count]; // new List<String>[count] is illegal!
		this.activations    = new ActivationKind[count];
	}
	
	public void setActivation(int i, String specifier) {
		if (specifier == null)
			this.activations[i] = ActivationKind.NONE;
		else
			this.activations[i] = ActivationKind.valueOf(specifier);
	}
	public String toString() {
		String result = "\tbase plugin "+basePlugin+"\n\tadapted by aspect pluging "+aspectPlugin;
		for (String teamClass : teamClasses) {
			result += "\n\t\t + team "+teamClass;
		}
		return result;
	}

	public List<String> getAllTeams() {
		List<String> all = Arrays.asList(this.teamClasses);
		if (subTeamClasses != null) {
			all = new ArrayList<>(all);
			for (int i = 0; i < subTeamClasses.length; i++)
				if (subTeamClasses[i] != null)
					all.addAll(subTeamClasses[i]);
		}
		return all;
	}

	public void addBaseClassNames(String teamName, Collection<String> baseClassNames) {
		basesPerTeam.put(teamName, baseClassNames);
		for (String baseClassName : baseClassNames) {
			Set<String> teams = teamsPerBase.get(baseClassName);
			if (teams == null)
				teamsPerBase.put(baseClassName, teams = new HashSet<>());
			teams.add(teamName);
		}
	}

	/** Destructively read the names of teams to load for a given base class. */
	public synchronized @Nullable Collection<String> getTeamsForBase(String baseClassName) {
		Set<String> teamNames = teamsPerBase.remove(baseClassName);
		if (teamNames != null) {
			teamNames.removeAll(teamsInProgress);
			teamsInProgress.addAll(teamNames);
		}
		return teamNames;		
	}

	public ActivationKind getActivation(String teamClassName) {
		for (int i=0; i<teamClasses.length; i++) {
			if (teamClasses[i].equals(teamClassName))
				return activations[i];
		}
		return ActivationKind.NONE;
	}

	/** Read OT attributes of all teams in aspectBinding and collect affected base classes. */
	public synchronized void scanTeamClasses(Bundle bundle) {
		ClassScanner scanner = new ClassScanner();
		for (String teamName : getAllTeams()) {
			try {
				teamName = scanner.readOTAttributes(bundle, teamName);
				Collection<String> baseClassNames = scanner.getCollectedBaseClassNames();
				addBaseClassNames(teamName, baseClassNames);
				log(IStatus.INFO, "Scanned team class "+teamName+", found "+baseClassNames.size()+" base classes");
			} catch (Exception e) {
				log(e, "Failed to scan team class "+teamName);
			}
		}
		this.state = State.TeamsScanned;
	}
}