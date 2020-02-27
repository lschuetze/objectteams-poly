/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2017 GK Software AG
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 */
package org.eclipse.objectteams.runtime;

/**
 * Hooks intercepted by the debugger.
 * 
 * CAUTION: The debugger hardcodes line numbers in this file and will install synthetic breakpoins on these lines.
 * CAUTION: Any editing must preserve existing line numbers!
 * CAUTION: Also argument positions are relevant!
 */
public class DebugHooks {

	/** Called after Instrumention.redefineClasses has been performed regarding the specified class. */
	public static void afterRedefineClasses(String className) {
		// nop
	} // #30
}
