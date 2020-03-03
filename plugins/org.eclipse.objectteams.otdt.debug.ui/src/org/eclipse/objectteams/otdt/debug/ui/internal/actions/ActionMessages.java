/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
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
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug.ui.internal.actions;

import org.eclipse.osgi.util.NLS;

public class ActionMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.objectteams.otdt.debug.ui.internal.actions.ActionMessages"; //$NON-NLS-1$
	public static String ChangeTeamActivationAction_activate_label;
	public static String ChangeTeamActivationAction_activate_description;
	public static String ChangeTeamActivationAction_deactivate_label;
	public static String ChangeTeamActivationAction_deactivate_description;
	public static String ChangeTeamActivationAction_error_title;
	public static String ChangeTeamActivationAction_error_exception;
	public static String ChangeTeamActivationAction_error_no_thread_suspended;
	public static String UpdateTeamViewAction_permanently_update_description;
	public static String UpdateTeamViewAction_permanently_update_tooltip;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ActionMessages.class);
	}

	private ActionMessages() {
		// don't instantiate
	}
}
