/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2003, 2014 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.ext;

import org.eclipse.osgi.util.NLS;

public class OTCoreExtMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.objectteams.otdt.internal.core.ext.OTCoreExtMessages"; //$NON-NLS-1$

	public static String OTREContainer__Description;

	public static String OTREContainer_otre_not_found;

	public static String AbstractMarkable_baseClassHierarchy_progress;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, OTCoreExtMessages.class);
	}

	private OTCoreExtMessages() {
		// don't instantiate
	}
}
