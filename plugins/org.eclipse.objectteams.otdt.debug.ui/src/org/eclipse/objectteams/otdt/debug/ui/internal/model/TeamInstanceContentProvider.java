/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2003, 2007 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.debug.ui.internal.model;

import org.eclipse.debug.internal.ui.model.elements.VariableContentProvider;
import org.eclipse.objectteams.otdt.debug.ui.OTDebugUIPlugin;

/**
 * Content provider for the fields of a team instance.
 * Sole purpose compared to its superclass: make it applicable to the TeamView, too. 
 * 
 * Note, that filtering takes place within the debug.adaptor.VariablesViewAdaptor.
 * 
 * @author stephan
 * @since 1.1.2
 */
@SuppressWarnings("restriction") // team view as variant of variables view needs access to internals
public class TeamInstanceContentProvider extends VariableContentProvider 
{
	@Override
	protected boolean supportsContextId(String id) {
		return id.equals(OTDebugUIPlugin.TEAM_VIEW_ID);
	}
}
