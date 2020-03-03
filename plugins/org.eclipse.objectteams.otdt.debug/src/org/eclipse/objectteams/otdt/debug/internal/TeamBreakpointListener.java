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
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug.internal;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.core.dom.Message;
import org.eclipse.jdt.debug.core.IJavaBreakpoint;
import org.eclipse.jdt.debug.core.IJavaBreakpointListener;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaLineBreakpoint;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.internal.debug.core.model.JDIObjectValue;
import org.eclipse.jdt.internal.debug.core.model.JDIThisVariable;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;
import org.eclipse.objectteams.otdt.debug.IOTDebugEventListener;
import org.eclipse.objectteams.otdt.debug.OTDebugElementsContainer;
import org.eclipse.objectteams.otdt.debug.OTDebugPlugin;
import org.eclipse.objectteams.otdt.debug.TeamInstance;
import org.eclipse.objectteams.otdt.debug.internal.breakpoints.OTBreakpoints;

public class TeamBreakpointListener implements IJavaBreakpointListener
{
	static final String FIELD_THIS = "this"; //$NON-NLS-1$
	static final String LOCAL_THREAD_ACT_DEACT = "thread"; //$NON-NLS-1$
	static final String LOCAL_THREAD_IMPLICIT_ACT_DEACT = "currentThread"; //$NON-NLS-1$
	static final String FIELD_ALL_THREADS = "ALL_THREADS"; //$NON-NLS-1$
	
	private static TeamBreakpointListener _singleton;

	public static TeamBreakpointListener getInstance()
	{
		if(_singleton == null)
			_singleton = new TeamBreakpointListener();
		
		return _singleton;
	}
	
	public int breakpointHit(IJavaThread thread, IJavaBreakpoint breakpoint)
	{
	    OTDebugElementsContainer otDebugElementsContainer = getDebugElementsContainer(thread.getLaunch());
	    if (otDebugElementsContainer == null)
	    	return DONT_CARE; // something's broken for OT, don't interfere the debugger

	    // without a marker it can't be a valid OOT breakpoint
	    if (!breakpoint.getMarker().exists())
	    	return DONT_CARE;

		try
		{
			if (OTBreakpoints.Descriptor.TeamConstructor.matches(breakpoint))
			{
			  // A team is being instantiated!
              JDIThisVariable teamVariable = (JDIThisVariable) thread.findVariable(FIELD_THIS);
              if (otDebugElementsContainer.getTeamInstance(teamVariable) == null) {
	              TeamInstance newTeam= otDebugElementsContainer.addTeamInstance(teamVariable);
	              notifyTeamInstantiation(otDebugElementsContainer, newTeam);
              }
              return IJavaBreakpointListener.DONT_SUSPEND;
			}
			
			if (OTBreakpoints.Descriptor.TeamFinalize.matches(breakpoint))
			{
			  // A team is being disposed!
              IJavaVariable teamVariable = thread.findVariable(FIELD_THIS);
              int idx= otDebugElementsContainer.removeTeamInstance(teamVariable);
              if (idx != -1)
            	  notifyTeamFinalize(otDebugElementsContainer, idx);
              return IJavaBreakpointListener.DONT_SUSPEND;
			}
			
			if (OTBreakpoints.Descriptor.TeamActivate.matches(breakpoint))
			{
				IJavaVariable teamVariable = thread.findVariable(FIELD_THIS);
				TeamInstance teamInstance = otDebugElementsContainer.getTeamInstance(teamVariable);

				IJavaVariable teamActiveThread = thread.findVariable(LOCAL_THREAD_ACT_DEACT);
				IValue threadValue = teamActiveThread.getValue();
				long threadID = ((JDIObjectValue)threadValue).getUniqueId();
				teamInstance.setActiveForThreadID(threadID);
				
				IJavaVariable teamGlobalThread = thread.findVariable(FIELD_ALL_THREADS);
				IValue globalThreadValue = teamGlobalThread.getValue();
				long globalThreadID = ((JDIObjectValue)globalThreadValue).getUniqueId();
				teamInstance.setGlobalThreadID(globalThreadID);
				
				notifyActivationState(teamInstance);
				return IJavaBreakpointListener.DONT_SUSPEND;
			}

			if (OTBreakpoints.Descriptor.TeamDeactivate.matches(breakpoint))
			{
				IJavaVariable teamVariable = thread.findVariable(FIELD_THIS);
				TeamInstance teamInstance = otDebugElementsContainer.getTeamInstance(teamVariable);
				
				IJavaVariable teamDeactiveThread= thread.findVariable(LOCAL_THREAD_ACT_DEACT);
				IValue threadValue = teamDeactiveThread.getValue();
				long threadID = ((JDIObjectValue)threadValue).getUniqueId();
				teamInstance.setInactiveForThreadID(new Long(threadID));
				
				notifyActivationState(teamInstance);
				return IJavaBreakpointListener.DONT_SUSPEND;
			}
			
			if (OTBreakpoints.Descriptor.TeamImplicitActivate.matches(breakpoint))
			{
				IJavaVariable teamVariable = thread.findVariable(FIELD_THIS);
				TeamInstance teamInstance = otDebugElementsContainer.getTeamInstance(teamVariable);
				
				// don't use fragile access to local variable 
				// but directly use the current thread:
				long threadID= ((JDIThread)thread).getUnderlyingThread().uniqueID();
				teamInstance.setImplicitActiveForThreadID(threadID);
				
				notifyActivationState(teamInstance);
				return IJavaBreakpointListener.DONT_SUSPEND;
			}

			if (OTBreakpoints.Descriptor.TeamImplicitDeactivate.matches(breakpoint))
			{
				IJavaVariable teamVariable = thread.findVariable(FIELD_THIS);
				TeamInstance teamInstance = otDebugElementsContainer.getTeamInstance(teamVariable);
				
				// don't use fragile access to local variable 
				// but directly use the current thread:
				long threadID = ((JDIThread)thread).getUnderlyingThread().uniqueID();
				teamInstance.setImplicitInactiveForThreadID(threadID);
				
				notifyActivationState(teamInstance);
				return IJavaBreakpointListener.DONT_SUSPEND;
			}
		}
		catch (Exception ex)
		{
	 		OTDebugPlugin.logException("Teammonitor can't read infos from debugTarget anymore. Disconnected?", ex); //$NON-NLS-1$
			//if something fails, let the debugger go on
			return IJavaBreakpointListener.DONT_SUSPEND;
		}

      return IJavaBreakpointListener.DONT_CARE;
	}
	
    protected OTDebugElementsContainer getDebugElementsContainer(ILaunch launch)
    {
        return (OTDebugElementsContainer) launch.getAdapter(OTDebugElementsContainer.class);
    }

    private void notifyTeamInstantiation(OTDebugElementsContainer container, TeamInstance newTeam)
    {
        for (IOTDebugEventListener listener : OTDebugPlugin.getDefault().getOTDebugEventListeners())
            listener.teamInstantiated(newTeam);
    }

    private void notifyTeamFinalize(OTDebugElementsContainer container, int idx)
    {
        for (IOTDebugEventListener listener : OTDebugPlugin.getDefault().getOTDebugEventListeners())
            listener.teamDisposed(idx);
    }
    
    private void notifyActivationState(TeamInstance teamInstance)
    {
        for (IOTDebugEventListener listener : OTDebugPlugin.getDefault().getOTDebugEventListeners())
            listener.activationStateChanged(teamInstance);
    }
    
	public void addingBreakpoint(IJavaDebugTarget target, IJavaBreakpoint breakpoint) {}
	public void breakpointHasCompilationErrors(IJavaLineBreakpoint breakpoint, Message[] errors) {}
	public void breakpointHasRuntimeException(IJavaLineBreakpoint breakpoint, DebugException exception) {}
	public void breakpointInstalled(IJavaDebugTarget target, IJavaBreakpoint breakpoint) {}
	public void breakpointRemoved(IJavaDebugTarget target, IJavaBreakpoint breakpoint) {}
	public int installingBreakpoint(IJavaDebugTarget target, IJavaBreakpoint breakpoint, IJavaType type)
	{
		return IJavaBreakpointListener.DONT_CARE;
	}

	public void dispose()
	{
		_singleton = null;
	}
}
