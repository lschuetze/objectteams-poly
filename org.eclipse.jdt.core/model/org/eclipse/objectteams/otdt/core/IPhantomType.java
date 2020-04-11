/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2010 GK Software AG
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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core;

import org.eclipse.jdt.core.IType;

/**
 * Marker interface to distinguish phantom roles from regular types.
 * A phantom role is a role that is inherited from a super team,
 * and not overridden in the current team, i.e., the current team
 * has no source corresponding to the phantom role.
 *
 * @since 3.7
 * @noimplement This interface is not intended to be implemented by clients.
 */
//TODO: should this extend IRoleType rather than just IType?
public interface IPhantomType extends IType {
	// no additional methods at this moment
}
