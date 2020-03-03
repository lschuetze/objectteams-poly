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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.internal.ui.model.elements.ElementLabelProvider;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.jface.viewers.TreePath;

/** Label for the root node of the TeamView (which does not have a label). */
@SuppressWarnings("restriction") // team view as variant of variables view needs access to internals
public class OTDebugElementsContainerLabelProvider extends ElementLabelProvider {

	@Override
	protected String getLabel(TreePath             elementPath, 
							  IPresentationContext presentationContext, 
							  String               columnId)
			throws CoreException 
	{
		return "never-seen-OTDebugLabel"; //$NON-NLS-1$
	}

}
