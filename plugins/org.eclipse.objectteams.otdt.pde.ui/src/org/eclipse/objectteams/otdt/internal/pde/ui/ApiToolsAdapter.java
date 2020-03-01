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
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.pde.ui;

import org.eclipse.objectteams.otdt.core.IOTJavaElement;

import base org.eclipse.pde.api.tools.ui.internal.JavaElementActionFilter;

@SuppressWarnings("restriction")
public team class ApiToolsAdapter {
	
	/** Protect one more hard coded switch-case against unexpected OT elements. */
	protected class FilterAdaptor playedBy JavaElementActionFilter {

		testAttribute <- replace testAttribute;

		callin boolean testAttribute(Object target) {
			if (target instanceof IOTJavaElement)
				// treat OT elements via their java correspondence:
				target = ((IOTJavaElement)target).getCorrespondingJavaElement();
			return base.testAttribute(target);
		}
	}
}
