/*******************************************************************************
 * Copyright (c) 2016 GK Software AG, and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation
 *******************************************************************************/
package org.eclipse.objectteams.otdt.internal.samples;

import org.eclipse.osgi.util.NLS;;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.objectteams.otdt.internal.samples.messages";//$NON-NLS-1$

	public static String SampleWizard_title;
	public static String SampleWizard_overwrite;

	public static String ShowSampleAction_title;

	public static String SelectionPage_title;
	public static String SelectionPage_desc;

	public static String ProjectNamesPage_projectName;
	public static String ProjectNamesPage_multiProjectName;
	public static String ProjectNamesPage_title;
	public static String ProjectNamesPage_desc;

	public static String ProjectNamesPage_noSampleFound;
	public static String ProjectNamesPage_emptyName;
	public static String ProjectNamesPage_duplicateNames;

	public static String ReviewPage_title;
	public static String ReviewPage_desc;
	public static String ReviewPage_descContent;
	public static String ReviewPage_content;

	public static String ReviewPage_noSampleFound;

	
	public static String SampleEditor_desc;
	public static Object SampleEditor_content;

	
	public static String SampleOperation_creating;

	
	public static String SampleStandbyContent_content;
	public static String SampleStandbyContent_desc;




	

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
