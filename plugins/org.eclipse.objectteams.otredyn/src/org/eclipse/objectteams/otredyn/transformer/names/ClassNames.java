/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2014 Oliver Frank and others.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Oliver Frank - Initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.transformer.names;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashSet;
import java.util.List;

import org.objectteams.IBoundBase2;
import org.eclipse.objectteams.otredyn.runtime.dynamic.linker.*;
import org.objectteams.ILiftingParticipant;
import org.objectteams.ITeam;
import org.objectteams.ImplicitTeamActivation;
import org.objectteams.LiftingFailedException;
import org.objectteams.LiftingVetoException;
import org.objectteams.SneakyException;
import org.objectteams.Team;
import org.objectteams.TeamThreadManager;
import org.objectteams.WrongRoleException;

/**
 * Container for class names used in the bytecode manipulating classes
 * 
 * @author Oliver Frank
 */
public abstract class ClassNames {

	public final static String CALLSITE_SLASH = CallSite.class.getName().replace('.', '/');
	public final static String LOOKUP_SLASH = MethodHandles.Lookup.class.getName().replace('.', '/');
	public final static String METHODTYPE_SLASH = MethodType.class.getName().replace('.', '/');
	public final static String CALLIN_BOOTSTRAP_SLASH = CallinBootstrap.class.getName().replace('.', '/');
	public final static String TEAMS_AND_IDS_BOOTSTRAP_SLASH = TeamsAndCallinIdsBootstrap.class.getName().replace('.', '/');

	public final static String I_BOUND_BASE_SLASH = IBoundBase2.class.getName().replace('.', '/');
	public final static String OBJECT_SLASH = Object.class.getName().replace('.', '/');
	public final static String CLASS_SLASH = Class.class.getName().replace('.', '/');
	public final static String I_BOUND_BASE_DOT = IBoundBase2.class.getName();
	public final static String TEAM_MANAGER_SLASH = "org/eclipse/objectteams/otredyn/runtime/TeamManager"; // don't touch special class TeamManager, which is woven itself
	public final static String ITEAM_SLASH = ITeam.class.getName().replace('.', '/');
	public final static String TEAM_SLASH = Team.class.getName().replace('.', '/');
	public final static String LIST_SLASH = List.class.getName().replace('.', '/');
	public final static String HASH_SET_SLASH = HashSet.class.getName().replace('.', '/');
	public final static String IMPLICIT_ACTIVATION = ImplicitTeamActivation.class.getName().replace('.', '/');
	public final static String ILIFTING_PARTICIPANT = ILiftingParticipant.class.getName().replace('.', '/');
	public static final String WRONG_ROLE_EXCEPTION = WrongRoleException.class.getName().replace('.', '/');
	public static final String LIFTING_FAILED_EXCEPTION = LiftingFailedException.class.getName().replace('.', '/');
	public static final String LIFTING_VETO_EXCEPTION = LiftingVetoException.class.getName().replace('.', '/');

	public static final String SNEAKY_EXCEPTION_SLASH = SneakyException.class.getName().replace('.', '/');
	// member of SneakyException:
	public static final String RETHROW_SELECTOR = "rethrow";
	public static final String RETHROW_SIGNATURE = "()V";

	public static final String THREAD_SLASH = Thread.class.getName().replace('.', '/');
	public static final String TEAM_THREAD_MANAGER_SLASH = TeamThreadManager.class.getName().replace('.', '/');
	public static final String THROWABLE_SLASH = Throwable.class.getName().replace('.', '/');
	public static final String RUNNABLE_SLASH = Runnable.class.getName().replace('.', '/');
}
