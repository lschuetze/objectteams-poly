/**
 * This file is part of "Object Teams Development Tooling"-Software.
 *
 * Copyright 2013 GK Software AG.
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
 * 	Stephan Herrmann - Initial API and implementation.
 */
package org.eclipse.objectteams.builder;

/**
 * Simple logging interface to decouple the {@link BuildTimeWeaver} from Maven specifics.
 */
public interface ILogger {

	void info(String string);

	void debug(String string);

	void error(String string);

}
