/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Technical University Berlin - extended API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.ITeamAnchor;

/*
 * Encapsulates aspects related to type variable substitution
 */
public interface Substitution {

	/**
	 * Don't substitute any type variables.
	 * Enables the use of {@link Scope.Substitutor} for other purposes.
	 */
	public static class NullSubstitution implements Substitution {
		LookupEnvironment environment;

		public NullSubstitution(LookupEnvironment environment) {
			this.environment = environment;
		}
		@Override
		public TypeBinding substitute(TypeVariableBinding typeVariable) {
			return typeVariable;
		}
		@Override
		public boolean isRawSubstitution() {
			return false;
		}
		@Override
		public LookupEnvironment environment() {
			return this.environment;
		}
//{ObjectTeams:
		@Override
		public ITeamAnchor substituteAnchor(ITeamAnchor anchor, int rank) {
			return anchor;
		}
// SH}
	}

	/**
	 * Returns the type substitute for a given type variable, or itself
	 * if no substitution got performed.
	 */
	TypeBinding substitute(TypeVariableBinding typeVariable);

	/**
	 * Returns the lookup environment
	 */
	LookupEnvironment environment();

	/**
	 * Returns true for raw substitution
	 */
	boolean isRawSubstitution();

//{ObjectTeams: one more thing to substitute
	/** Return the substitute for a given type anchor, or null if no substitution got performed. */
	ITeamAnchor substituteAnchor(ITeamAnchor anchor, int rank);
// SH}
}
