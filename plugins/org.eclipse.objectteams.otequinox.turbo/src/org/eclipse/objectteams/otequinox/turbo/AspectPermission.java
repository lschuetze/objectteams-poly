/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009, 2014 Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otequinox.turbo;

/**
 * Copy of corresponding type in org.eclipse.objectteams.otequinox (inaccessible to this fragment).
 */
public enum AspectPermission {
	/** Not influencing negotiation between other parties. */
	UNDEFINED,
	/** A permission is granted unless someone else denies it. */
	GRANT, 
	/** A permission is explicitly denied. Cannot be overridden. */
	DENY;
}
