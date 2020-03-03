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

import org.eclipse.debug.internal.ui.viewers.model.provisional.IModelDelta;
import org.eclipse.debug.internal.ui.viewers.model.provisional.ModelDelta;
import org.eclipse.debug.internal.ui.viewers.provisional.AbstractModelProxy;
import org.eclipse.objectteams.otdt.debug.IOTDebugEventListener;
import org.eclipse.objectteams.otdt.debug.OTDebugElementsContainer;
import org.eclipse.objectteams.otdt.debug.OTDebugPlugin;
import org.eclipse.objectteams.otdt.debug.TeamInstance;

/**
 * This class mediates between the model and its view:
 * 
 * + Receive triggers from TeamBreakpointListener (via IOTDebugEventListener)
 * + Create and fire ModelDelta events (received by TreeModelContentProvider)
 * 
 * Installation:
 * + TreeModelContentProvider.inputChanged()->installModelProxy()
 *   -> makes TreeModelContentProvider an fListener (modelChangedListener) of this
 * 
 * 
 * @author stephan
 * @since 1.1.2
 */
@SuppressWarnings("restriction") // team view as variant of variables view needs access to internals
public class OTDefaultVariableViewModelProxy
	extends AbstractModelProxy
	implements IOTDebugEventListener
{

	private OTDebugElementsContainer container;

	public OTDefaultVariableViewModelProxy(OTDebugElementsContainer debugElementsContainer) {
		super();
		this.container= debugElementsContainer;
		OTDebugPlugin.getDefault().addOTDebugEventListener(this);
	}

	public void teamInstantiated(TeamInstance newTeam) 
	{
		int idx= container.getIndexOfTeamInstance(newTeam);
		ModelDelta delta= new ModelDelta(container, IModelDelta.NO_CHANGE);
		delta.addNode(newTeam, idx, IModelDelta.INSERTED);
		fireModelChanged(delta);
	}

	public void teamDisposed(int idx) {
		ModelDelta delta= new ModelDelta(container, IModelDelta.NO_CHANGE);
		delta.addNode(null, idx, IModelDelta.REMOVED);
		fireModelChanged(delta);		
	}

	public void activationStateChanged(TeamInstance teamInstance) 
	{
		int idx= container.getIndexOfTeamInstance(teamInstance);
		ModelDelta delta= new ModelDelta(container, IModelDelta.NO_CHANGE);
		delta.addNode(teamInstance, idx, IModelDelta.STATE);
		fireModelChanged(delta);		
	}
	
	@Override
	public synchronized void dispose() {
		super.dispose();
		OTDebugPlugin.getDefault().removeOTDebugEventListener(this);
	}
}
