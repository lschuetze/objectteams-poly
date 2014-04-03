/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2014, GK Software AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
