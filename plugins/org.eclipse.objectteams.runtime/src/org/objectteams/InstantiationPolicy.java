/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2011 GK Software AG
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.objectteams;

/**
 * Possible values for the {@link Instantiation} annotation for role types:
 * {@link #NEVER}, {@link #ONDEMAND} (default), {@link #SINGLETON} and {@link #ALWAYS}.
 *
 * @author stephan
 */
public enum InstantiationPolicy {
	/**
	 * Roles with the instantiation policy NEVER are not instantiated by lifting.
	 * Such roles cannot have state.
	 * The compiler still has to ensure that access to base instances still behaves
	 * as if a role instance would exist.
	 * Note: this is not yet supported by the OT/J compiler.
	 */
	NEVER,
	/**
	 * The constant ONDEMAND corresponds to the default behavior in OT/J
	 * where lifting will create a role for a given base instance upon first access,
	 * i.e., if no matching role is found in the team's internal cache.
	 * Otherwise the existing role from the cache is re-used.
	 */
	ONDEMAND,
	/**
	 * For a role with instantiation policy SINGLETON at most one instance per team
	 * will be created by lifting. Subsequent lifting operations will always return
	 * the same role instance.
	 * The compiler still has to ensure that access to base instances still behaves
	 * as if a role instance would exist.
	 * Note: this is not yet supported by the OT/J compiler.
	 */
	SINGLETON,
	/**
	 * The instantiation policy ALWAYS advises the compiler to omit generating an
	 * internal cache for the given role type. Instead of first consulting the
	 * cache (as it is done in the default ONDEMAND policy) each lifting operation
	 * creates a new role instance.
	 */
	ALWAYS;
}
