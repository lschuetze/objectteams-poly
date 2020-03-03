/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.compiler.adaptor;

import org.eclipse.osgi.util.NLS;

public class OTMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.objectteams.otdt.internal.compiler.adaptor.OTMessages"; //$NON-NLS-1$
	public static String CheckUniqueCallinCapture_error_cannotCreateMarker;
	public static String CheckUniqueCallinCapture_warning_multipleCallinsToBaseMethod;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, OTMessages.class);
	}

	private OTMessages() {
	}
}
