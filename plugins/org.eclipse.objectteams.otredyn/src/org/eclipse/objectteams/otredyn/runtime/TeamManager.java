/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2014 Oliver Frank and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Oliver Frank - Initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.objectteams.otredyn.bytecode.AbstractBoundClass;
import org.objectteams.ITeam;
import org.objectteams.ITeamManager;
import org.objectteams.Team;

/**
 * This class administrates the the active teams and their
 * callin ids for all joinpoints.
 * Teams have to register/unregister themselves at this class, 
 * while their activation/deactivation.
 * The generated client code can call methods of this class to
 * get the active teams and callin ids.
 * @author Oliver Frank
 *
 */
public class TeamManager implements ITeamManager {

	//	void handleTeamStateChange(ITeam t, TeamStateChange stateChange) ;
	private static List<List<ITeam>> _teams = new ArrayList<List<ITeam>>();
	private static List<List<Integer>> _callinIds = new ArrayList<List<Integer>>();
	private static Map<String, Integer> joinpointMap = new HashMap<String, Integer>();
	// key: Team class, value: list of global memberIds, indexed by local accessId
	private static Map<Class<?>, List<Integer>> accessIdMap = new HashMap<Class<?>, List<Integer>>();
	private static int currentJoinpointId = 0;
	// map all original joinpoints to their inherited versions in subclasses
	private static Map<Integer,List<Integer>> joinpointToSubJoinpoints = new HashMap<Integer, List<Integer>>();
	private static IClassRepository classRepository;

	public static void setup(IClassRepository repo) {
		classRepository = repo;
		Team.registerTeamManager(new TeamManager()); // install callback
	}

	/**
	 * Returns all active teams for joinpoint.
	 * This method is intended to be called by 
	 * generated client code.
	 * @param joinpointId
	 * @return all active teams for the joinpoint or null
	 * if there are no active teams
	 */
	public static ITeam[] getTeams(int joinpointId) {
		synchronized(_teams) {
			List<ITeam> teams = _teams.get(joinpointId);
			int size = teams.size();
			if (size == 0)
				return null;
			ITeam[] active = new ITeam[size];
			int count = 0;
			Thread th = Thread.currentThread();
			for (int i = 0; i < active.length; i++) {
				ITeam t = teams.get(i);
				if (t.isActive(th))
					active[count++] = t;
			}
			if (count == 0)
				return null;
			if (count != size) 
				System.arraycopy(active, 0, active = new ITeam[count], 0, count);
			return active;
		}
	}

	/**
	 * Returns all callin ids for the active teams and the joinpoint.
	 * This method is intended to be called by 
	 * generated client code.
	 * @param joinpointId
	 * @return the callin ids for the joinpoint or null
	 * if there are no active teams
	 */
	public static int[] getCallinIds(int joinpointId) {
		List<Integer> callinIds = _callinIds.get(joinpointId);
		if (callinIds.size() == 0) {
			return null;
		}
		int[] result = new int[callinIds.size()];
		int i = 0;
		for (Integer callinId : callinIds) {
			result[i] = callinId;
			i++;
		}
		return result;
	}

	/**
	 * Returns the member id for a given team and a access id 
	 * used in this team.
	 * This method is intended to be called by 
	 * generated client code.
	 * @param accessId
	 * @param Team
	 * @return
	 */
	public static int getMemberId(int accessId, Class<? extends ITeam> teamClass) {
		Integer id = accessIdMap.get(teamClass).get(accessId);
		return id;
	}

	/**
	 * Returns and possibly creates a globally unique id for a
	 * joinpoint identifier. Additionally it prepares the 
	 * structures needed to store the teams and callin ids for a joinpoint.
	 * @param joinpointIdentifier
	 * @return a joinpoint id
	 */
	public synchronized static int getJoinpointId(String joinpointIdentifier) {
		Integer joinpointId = getExistingJoinpointId(joinpointIdentifier);
		if (joinpointId == null) {
			joinpointMap.put(joinpointIdentifier, currentJoinpointId);
			List<ITeam> teams = new ArrayList<ITeam>();
			_teams.add(teams);
			List<Integer> callinIds = new ArrayList<Integer>();
			_callinIds.add(callinIds);
			return currentJoinpointId++;
		}
		return joinpointId;
	}
	
	/**
	 * Returns an existing globally unique joinpoint id
	 * for a joinpoint identifier 
	 * @param joinpointIdentifier
	 * @return the joinpoint id or null if a joinpoint id
	 * doesn't exist for this joinpoint identifier
	 */
	private static Integer getExistingJoinpointId(String joinpointIdentifier) {
		return joinpointMap.get(joinpointIdentifier);
	}

	/**
	 * Handles registration and unregistration of teams.
	 * Stores the team and the ids dependend on the joinpoints and
	 * tell the {@link AbstractBoundClass} representing the base classes
	 * of the team, that there is new Binding
	 * @param t
	 * @param stateChange
	 */
	public void handleTeamStateChange(ITeam t, ITeamManager.TeamStateChange stateChange) {
		IClassIdentifierProvider provider = ClassIdentifierProviderFactory.getClassIdentifierProvider();
		Class<? extends ITeam> teamClass = t.getClass();
		String teamId = provider.getClassIdentifier(teamClass);
		IBoundTeam teem = classRepository.getTeam(teamClass.getName(), teamId, teamClass.getClassLoader());

		for (IBinding binding : teem.getBindings()) {
			String boundClassName = binding.getBoundClass();
			String boundClassIdentifier = provider.getBoundClassIdentifier(teamClass, boundClassName);
			// FIXME(SH): the following may need adaptation for OT/Equinox or other multi-classloader settings:
			IBoundClass boundClass = classRepository.getBoundClass(boundClassName.replace('/', '.'), boundClassIdentifier, teamClass.getClassLoader());
			// FIXME(SH): if boundClass is a role we need to find tsub roles, too!
			switch (binding.getType()) {
			case CALLIN_BINDING:
				IMethod method = boundClass.getMethod(binding.getMemberName(), binding.getMemberSignature(), binding.isHandleCovariantReturn());
				int joinpointId = getJoinpointId(boundClass
						.getMethodIdentifier(method));
				synchronized (method) {
					changeTeamsForJoinpoint(t, binding.getPerTeamId(), joinpointId, stateChange);
					List<Integer> subJoinpoints = joinpointToSubJoinpoints.get(joinpointId);
					if (subJoinpoints != null)
						for (Integer subJoinpoint : subJoinpoints)
							changeTeamsForJoinpoint(t, binding.getPerTeamId(), subJoinpoint, stateChange);
				}
				boundClass.handleAddingOfBinding(binding); // TODO: do we want/need to group all bindings into one action?
				break;
			}
		}
	}
	
	/**
	 * When a team is about to be activated propagate its bindings to all base classes,
	 * but don't yet register any join points, we aren't activating yet. 
	 */
	public static void prepareTeamActivation(Class<? extends ITeam> teamClass) {
		String teamName = teamClass.getName();
		ClassLoader teamClassLoader = teamClass.getClassLoader();
		IClassIdentifierProvider provider = ClassIdentifierProviderFactory.getClassIdentifierProvider();
		String teamId = provider.getClassIdentifier(teamClass);
		IBoundTeam teem = classRepository.getTeam(teamName, teamId, teamClassLoader);

		for (IBinding binding : teem.getBindings()) {
			String boundClassName = binding.getBoundClass();
			String boundClassIdentifier = provider.getBoundClassIdentifier(teamClass, boundClassName);
			// FIXME(SH): the following may need adaptation for OT/Equinox or other multi-classloader settings:
			IBoundClass boundClass = classRepository.getBoundClass(boundClassName.replace('/', '.'), boundClassIdentifier, teamClass.getClassLoader());
			// FIXME(SH): if boundClass is a role we need to find tsub roles, too!
			switch (binding.getType()) {
			case CALLIN_BINDING:
				boundClass.handleAddingOfBinding(binding);
				break;
			}
		}		
	}

	public static void handleTeamLoaded(Class<? extends ITeam> teamClass) {
		if (teamClass != null)
			handleDecapsulation(teamClass);
		performPendingTask();
	}
	private static void handleDecapsulation(Class<? extends ITeam> teamClass) {
		IClassIdentifierProvider provider = ClassIdentifierProviderFactory.getClassIdentifierProvider();
		String teamId = provider.getClassIdentifier(teamClass);
		IBoundTeam teem = classRepository.getTeam(teamClass.getName(), teamId, teamClass.getClassLoader());

		for (IBinding binding : teem.getBindings()) {
			String boundClassName = binding.getBoundClass();
			String boundClassIdentifier = provider.getBoundClassIdentifier(teamClass, boundClassName.replace('.', '/'));
			// FIXME(SH): the following may need adaptation for OT/Equinox or other multi-classloader settings:
			IBoundClass boundClass = classRepository.getBoundClass(boundClassName.replace('/', '.'), boundClassIdentifier, teamClass.getClassLoader());

			switch (binding.getType()) {
			case FIELD_ACCESS: // fallthrough
			case METHOD_ACCESS:
				IMember member = null;
				if (binding.getType() == IBinding.BindingType.FIELD_ACCESS) {
					member = boundClass.getField(binding.getMemberName(),
							binding.getMemberSignature());
				} else {
					member = boundClass.getMethod(binding.getMemberName(), binding.getMemberSignature(), false/*covariantReturn*/);
				}
				
				int memberId = member.getGlobalId(boundClass);
				synchronized (member) {
					addAccessIds(teamClass, teem, binding.getPerTeamId(), memberId);
				}
				boundClass.handleAddingOfBinding(binding);
				break;
			}
		}
	}
	/**
	 * Stores the access ids of a team and the corresponding member ids,
	 * where accessId is local to the team and the member id is globally unique.
	 * @param teamClass
	 * @param teem
	 * @param accessId
	 * @param memberId
	 * @param stateChange
	 */
	private static void addAccessIds(Class<? extends ITeam> teamClass, IBoundTeam teem, int accessId, int memberId) {
		List<Integer> accessIds = accessIdMap.get(teamClass);
		if (accessIds == null) {
			int highestAccessId = teem.getHighestAccessId() + 1;
			accessIds = new ArrayList<Integer>(highestAccessId);
			for (int i = 0; i <= highestAccessId; i++) {
				accessIds.add(0);
			}
			accessIdMap.put(teamClass, accessIds);
		}
		accessIds.set(accessId, memberId);
	}

	/**
	 * Stores or removes the team
	 * @param t
	 * @param callinId
	 * @param joinpointId
	 * @param stateChange
	 */
	private static void changeTeamsForJoinpoint(ITeam t, int callinId, int joinpointId, TeamManager.TeamStateChange stateChange) {
		switch (stateChange) {
		case REGISTER:
			List<ITeam> teams = _teams.get(joinpointId);
			teams.add(0, t);
			List<Integer> callinIds = _callinIds.get(joinpointId);
			callinIds.add(0, callinId);
			break;
		case UNREGISTER:
			teams = _teams.get(joinpointId);
			callinIds = _callinIds.get(joinpointId);
			
			int index = teams.indexOf(t);
			while (index > -1) {
				teams.remove(index);
				callinIds.remove(index);
				index = teams.indexOf(t);
			}
			break;
		default: throw new RuntimeException("Unknown team state change: " + stateChange.name());
		}
	}

	/**
	 * Merge the teams and callin ids of two joinpoints into the second.
	 * This is used so that activating teams for the second joinpoint (subclass)
	 * will include the effect of activation of the former (superclass).
	 * @param superClass
	 * @param subClass
	 * @param superMethod
	 * @param subMethod
	 */
	public static void mergeJoinpoints(IBoundClass superClass,
			IBoundClass subClass, final IMethod superMethod, IMethod subMethod, final boolean handleCovariantReturn) {
		if (subClass.isAnonymous()) {
			// destClass is a placeholder, schedule real merging for when a real subclass gets wired:
			subClass.addWiringTask(new ISubclassWiringTask() {
				public void wire(IBoundClass superClass, IBoundClass subClass) {
					IMethod subMethod = subClass.getMethod(superMethod.getName(), superMethod.getSignature(),
														   handleCovariantReturn);
					mergeJoinpoints(superClass, subClass, superMethod, subMethod, handleCovariantReturn);
				}
			});
			return;
		}
		synchronized (subClass) {
			Integer superJoinpointId = getJoinpointId(superClass.getMethodIdentifier(superMethod));
			if (superJoinpointId != null) {
				int subJoinpointId = getJoinpointId(subClass.getMethodIdentifier(subMethod));
				
				List<Integer> subJoinpoints = joinpointToSubJoinpoints.get(superJoinpointId);
				if (subJoinpoints == null) {
					subJoinpoints = new ArrayList<Integer>();
					joinpointToSubJoinpoints.put(superJoinpointId, subJoinpoints);
				}
				// already processed?
				if (!subJoinpoints.contains(subJoinpointId)) {
					subJoinpoints.add(subJoinpointId);
					applyJoinpointMerge(superJoinpointId, subJoinpointId);
				}
			}
		}
	}

	private synchronized static void applyJoinpointMerge(Integer srcJoinpointId, int destJoinpointId) {
		List<ITeam> teams = _teams.get(destJoinpointId);
		List<Integer> callinIds = _callinIds.get(destJoinpointId);
		List<ITeam> srcTeams = _teams.get(srcJoinpointId);
		List<Integer> srcCallins = _callinIds.get(srcJoinpointId);
		for (int s=0; s<srcTeams.size(); s++) {
			int d = 0; // FIXME(SH): find insertion index based on activation priority!!
			teams.add(d, srcTeams.get(s));
			callinIds.add(0, srcCallins.get(s));
		}
		_teams.set(destJoinpointId, teams);
		_callinIds.set(destJoinpointId, callinIds);
	}
	
	/** 
	 * A "mailbox" for pending tasks (per thread). 
	 * The weaver stores redefinition tasks here that failed because the class
	 * was in a state between load-time transformation and definition.
	 */
	public static ThreadLocal<Runnable> pendingTasks = new ThreadLocal<Runnable>();
	/**
	 * Perform any tasks that are pending for this thread: redefinitions of a class 
	 * that was in a state between load-time transformation and definition.
	 */
	public static void performPendingTask() {
		Runnable task = pendingTasks.get();
		if (task != null) {
//			System.out.println("pending "+task);
			pendingTasks.set(null);
			try {
				task.run();
			} catch (Throwable t) {
				System.err.println(t+" because "+t.getCause());
				pendingTasks.set(task); // no success try later ??
			}
		}
	}
}
