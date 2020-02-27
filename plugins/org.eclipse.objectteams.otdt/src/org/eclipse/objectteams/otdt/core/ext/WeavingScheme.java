/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014, GK Software AG, Germany.
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
 *			Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.ext;

/**
 * Select the target weaving scheme.
 * @since 2.3
 */
public enum WeavingScheme {
	/** Use the traditional "Object Teams Runtime Environment" based on BCEL. */
	OTRE,
	/** Use the newer "Object Teams Dynamic Runtime Environment" based on ASM and supporting runtime weaving. */
	OTDRE;
}
