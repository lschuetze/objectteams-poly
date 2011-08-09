/*******************************************************************************
 * Copyright (c) 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.objectteams.internal.jdt.nullity.quickfix;

import org.eclipse.osgi.util.NLS;

public class FixMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.objectteams.internal.jdt.nullity.quickfix.FixMessages"; //$NON-NLS-1$
	
	
	public static String NullAnnotationsCleanUp_add_nullable_annotation;
	public static String NullAnnotationsCleanUp_add_nonnull_annotation;
	
	public static String QuickFixes_add_annotation_change_name;
	
	public static String QuickFixes_declare_method_parameter_nullness;

	public static String QuickFixes_declare_method_return_nullness;


	public static String QuickFixes_declare_overridden_parameter_as_nonnull;


	public static String QuickFixes_declare_overridden_return_as_nullable;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, FixMessages.class);
	}

	private FixMessages() {
	}
}
