/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009 Stephan Herrmann.
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
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otequinox.internal.branding;

import org.eclipse.osgi.util.NLS;

public class BrandingMessages extends NLS {
	private static final String BUNDLE_NAME = BrandingMessages.class.getName();

	public static String BrandingAdaptor_OT_adapted_by;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, BrandingMessages.class);
	}

	private BrandingMessages() {
		// don't instantiate
	}
}
