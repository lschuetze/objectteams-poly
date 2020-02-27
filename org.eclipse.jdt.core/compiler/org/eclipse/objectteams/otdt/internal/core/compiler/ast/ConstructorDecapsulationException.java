/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2014, GK Software AG and others.
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
 * 			Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.compiler.ast;

/**
 * Thrown when during resolving of an AllocationExpression we detect that
 * decapsulation is involved, which in OTREDyn requires redirection to
 * an _OT$access or _OT$accessStatic method.
 * @since 2.3
 */
@SuppressWarnings("serial")
public class ConstructorDecapsulationException extends RuntimeException {

	public int accessId;

	public ConstructorDecapsulationException(int accessId) {
		this.accessId = accessId;
	}

}
