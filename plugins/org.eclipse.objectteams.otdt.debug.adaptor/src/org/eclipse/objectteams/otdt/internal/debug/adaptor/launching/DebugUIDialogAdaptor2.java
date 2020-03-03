/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008 Technical University Berlin, Germany.
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
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.debug.adaptor.launching;

import base org.eclipse.jdt.internal.debug.ui.launcher.LocalJavaApplicationTabGroup;

/**
 * Apply adaptations of the super team to a class from org.eclipse.jdt.debug.ui.
 * 
 * @author stephan
 * @since 1.2.1
 */
@SuppressWarnings("restriction")
public team class DebugUIDialogAdaptor2 extends DebugUIDialogAdaptor {
	protected class JDTTabGroup
			extends LaunchConfigurationTabGroup
			playedBy LocalJavaApplicationTabGroup 
	{
		// empty role, just advise OT/Equinox to weave into one more base class. See Trac #145
	}
}
