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
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otequinox;

/**
 * Possible values for the "activation" attribute of a "team" element within an "aspectBinding" extension.
 *  
 * @author stephan
 * @since 1.2.7 (was inline previously)
 */
public enum ActivationKind {
	/** Don't activate team by default. */
	NONE, 
	/** Activate team for current thread. */
	THREAD, 
	/** Globally activate team. */
	ALL_THREADS;
}