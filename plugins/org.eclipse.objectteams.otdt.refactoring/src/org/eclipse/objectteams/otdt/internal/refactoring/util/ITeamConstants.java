/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2007, 2009 Technical University Berlin,
 * Germany.
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
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.refactoring.util;

import org.eclipse.jdt.core.Signature;

/**
 * @author svacina
 *
 * Constants for class org.objectteams.Team
 */
@SuppressWarnings("nls")
public interface ITeamConstants
{
	/**
	 *	constants for class team:
	 *	- virtualTeamMethodNames: Names of virtual team methods (_OT$ methods excluded)
	 *	- virtualTeamMethodParamTypes: parameter types of virtual team methods
	 *	- virtualTeamMethodReturnTypes: return types pf virtual team methods 
	 */
	String[] noParams= new String[0];
	String[] virtualTeamMethodNames = new String[]{"getActivationRepr", "getActivationRepr",
			"isActive","activate", "deactivate",
			"hasRole", "hasRole",
			"hasRole", "hasRole",
			"getRole", "getRole",
			"getRole", "getRole",
			"isExecutingCallin",
			"unregisterRole", "unregisterRole",
			"unregisterRole", "unregisterRole"};
	
	String[][] virtualTeamMethodParamTypes = new String[][]{noParams, noParams,
			noParams, noParams, noParams,
			{"QObject;"}, {"Qjava.lang.Object;"},
			{"QObject;", "QClass;"},{"Qjava.lang.Object;", "Qjava.lang.Class;"},
			{"QObject;"}, {"Qjava.lang.Object;"},
			{"QObject;", "QClass;"},{"Qjava.lang.Object;", "Qjava.lang.Class;"},
			noParams,
			{"QObject;"}, {"Qjava.lang.Object;"},
			{"QObject;", "QClass;"},{"Qjava.lang.Object;", "Qjava.lang.Class;"}};
	
	String[] virtualTeamMethodReturnTypes = new String[]{"QString;", "Qjava.lang.String;",
			Signature.SIG_BOOLEAN, Signature.SIG_VOID, Signature.SIG_VOID,
			Signature.SIG_BOOLEAN, Signature.SIG_BOOLEAN,
			Signature.SIG_BOOLEAN, Signature.SIG_BOOLEAN,
			"QObject;", "Qjava.lang.Object;",
			"QObject;", "Qjava.lang.Object;",
			Signature.SIG_BOOLEAN,
			Signature.SIG_VOID, Signature.SIG_VOID,
			Signature.SIG_VOID, Signature.SIG_VOID};
}
