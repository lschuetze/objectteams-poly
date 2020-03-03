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

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.internal.ui.model.elements.ElementContentProvider;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerUpdate;
import org.eclipse.objectteams.otdt.debug.OTDebugElementsContainer;
import org.eclipse.objectteams.otdt.debug.TeamInstance;
import org.eclipse.objectteams.otdt.debug.ui.OTDebugUIPlugin;

/**
 * Provide top level nodes for the TeamView:
 * + team instances from an OTDebugElementsContainer
 * 
 * @author stephan
 * @since 1.1.2
 */
@SuppressWarnings("restriction") // team view as variant of variables view needs access to internals
public class OTDebugElementsContainerContentProvider extends ElementContentProvider
{

	@Override
	protected int getChildCount(Object               element, 
								IPresentationContext context, 
								IViewerUpdate        monitor) 
			throws CoreException 
	{
		OTDebugElementsContainer container = (OTDebugElementsContainer) element;
		return container.getChildCount();
	}

	@Override
	protected Object[] getChildren(Object               parent, 
			                       int                  index, 
			                       int                  length,
								   IPresentationContext context, 
								   IViewerUpdate        monitor)
			throws CoreException 
	{
        OTDebugElementsContainer container = (OTDebugElementsContainer) parent;
        if(container.hasTeamInstances())
        {
        	ArrayList<TeamInstance> teamInstances = new ArrayList<>(container.getTeamInstances());
        	length = Math.min(length, teamInstances.size()-index);
        	Object[] result= new Object[length];
        	for (int i=0; i<length; i++)
        		result[i]= teamInstances.get(index+i);
        	return result;
        }
		return null;

	}

	@Override
	protected boolean supportsContextId(String id) {
		return id.equals(OTDebugUIPlugin.TEAM_VIEW_ID);
	}
}
