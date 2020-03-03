/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009, Stephan Herrmann.
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
package org.eclipse.objectteams.otdt.internal.ui;

import org.eclipse.osgi.util.NLS;

public class OTDTUIMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.objectteams.otdt.internal.ui.OTDTUIMessages"; //$NON-NLS-1$
	public static String BaseMethodCompareElement_sourceNotFound_text;
	public static String CompareWithBaseMethodAction_errorTitle;
	public static String CompareWithBaseMethodAction_ambiguousBindingsError;
	public static String CompareWithBaseMethodAction_multipleBaseMethodsError;
	public static String CompareBoundMethods_base_method_label;
	public static String CompareBoundMethods_compare_title;
	public static String CompareBoundMethods_compare_tooltip;
	public static String CompareBoundMethods_role_method_label;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, OTDTUIMessages.class);
	}

	private OTDTUIMessages() {
	}
}
