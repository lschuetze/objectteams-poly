/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2013 GK Software AG, Germany,
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
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.search;

import org.eclipse.osgi.util.NLS;

/**
 * @since 3.10 (OT 2.3)
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.objectteams.otdt.core.search.messages"; //$NON-NLS-1$

	public static String OTSearchHelper_progress_searchRoleTypes;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
