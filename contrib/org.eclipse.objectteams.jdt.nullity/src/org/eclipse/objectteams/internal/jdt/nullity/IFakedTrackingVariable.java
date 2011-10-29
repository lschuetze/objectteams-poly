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
package org.eclipse.objectteams.internal.jdt.nullity;

import org.eclipse.jdt.internal.compiler.lookup.MethodScope;

/**
 * Fassade to a class that was introduced in JDT/Core for 3.8M3.
 *  
 * @author stephan
 */
@SuppressWarnings("restriction")
public interface IFakedTrackingVariable {

	MethodScope methodScope();

	void markClosedInNestedMethod();
	
}
