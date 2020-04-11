/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2010 Stephan Herrmann
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
 * 		Stephan Herrmann  - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld;

import org.eclipse.jdt.core.tests.util.Util;

public class Stringify {
	public static void main(String[] args) {
		System.out.println(Util.fileContentToDisplayString(args[0], 2, true));
	}
}
