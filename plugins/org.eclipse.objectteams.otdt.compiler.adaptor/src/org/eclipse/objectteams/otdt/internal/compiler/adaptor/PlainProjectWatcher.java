/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2007 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
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
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.compiler.adaptor;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.InvocationSite;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

import base org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

/**
 * This team advises the ProblemReporter for non-OT-Plugin projects.
 * These projects must simply ignore `AccessRestrictions' with
 * problemId AdaptedPluginAccess, which is not a real problem.
 *   
 * @author stephan
 * @since 1.1.5
 */
@SuppressWarnings("restriction")
public team class PlainProjectWatcher extends CompilationThreadWatcher
{
	protected class ProblemReporter playedBy ProblemReporter 
	{
		void forbiddenReference(AccessRestriction restriction) 
		<- replace void forbiddenReference(TypeBinding   type,   ASTNode location, byte entryType, AccessRestriction restriction),
				   void forbiddenReference(FieldBinding  field,  ASTNode location, byte entryType, AccessRestriction restriction),
				   void forbiddenReference(MethodBinding method, InvocationSite location, byte entryType, AccessRestriction restriction)
		   with { restriction <- restriction }

		@SuppressWarnings("basecall")
		callin void forbiddenReference(AccessRestriction restriction) {
			if (restriction.getProblemId() != IProblem.AdaptedPluginAccess)
				base.forbiddenReference(restriction);
		}
	}
}
