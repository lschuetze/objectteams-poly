/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2009 Technical University Berlin, Germany.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.compiler.lookup;

/**
 * This interface abstracts of role type bindings.
 * @author stephan
 * @since 1.2.6
 */
public interface IRoleTypeBinding
{
	/** Get the team anchor of this role binding, can be <code>tthis</code>. */
	ITeamAnchor getAnchor();
	/**
	 * Get the full path representing the best name of this type's anchor.
	 * The best name already considers assignments between potential team anchors.
	 */
	ITeamAnchor[] getAnchorBestName();
	/** Is the anchor of this type exlicit, i.e., not <code>tthis</code>? */
	boolean hasExplicitAnchor();
}
