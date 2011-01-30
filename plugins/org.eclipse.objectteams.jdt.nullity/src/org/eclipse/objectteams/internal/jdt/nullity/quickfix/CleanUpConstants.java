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

@SuppressWarnings("restriction")
public class CleanUpConstants extends org.eclipse.jdt.internal.corext.fix.CleanUpConstants {

	// option name for NullAnnotationsCleanUp:
	public static final String ADD_DEFINITELY_MISSING_RETURN_ANNOTATION_NULLABLE = "cleanup.add_definitely_missing_nullable_return_annotation"; //$NON-NLS-1$
	public static final String ADD_POTENTIALLY_MISSING_RETURN_ANNOTATION_NULLABLE = "cleanup.add_potentially_missing_nullable_return_annotation"; //$NON-NLS-1$
	public static final String ADD_DEFINITELY_MISSING_PARAMETER_ANNOTATION_NULLABLE = "cleanup.add_definitely_missing_nullable_param_annotation"; //$NON-NLS-1$

}
