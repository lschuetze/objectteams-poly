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

/**
 * Represents a field access spec in a callout-to-field mapping in the extended Java model.
 *
 * @author stephan
 * @since 3.7
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IFieldAccessSpec {

	/** answer the name used for referencing the base field. */
	public String getSelector();

	/** answer the field type if specified. */
	public String getFieldType();

	/** answer whether this specifies a setter access (else: getter). */
	public boolean isSetter();

}