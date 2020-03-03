/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
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
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.samples;

import org.eclipse.osgi.util.NLS;

public class SampleMessages extends NLS {
	private static final String BUNDLE_NAME = SampleMessages.class.getName();

	public static String SamplesAdapter_cannot_run_selected;

	public static String SamplesAdapter_unable_to_run;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SampleMessages.class);
	}

	private SampleMessages() {
		// don't instantiate
	}
}
