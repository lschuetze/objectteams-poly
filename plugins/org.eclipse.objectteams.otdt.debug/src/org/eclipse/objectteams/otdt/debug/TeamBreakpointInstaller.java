/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug;

import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.debug.internal.breakpoints.OOTBreakpoints;

/**
 * This class un/installs support for breakpoints in certain well-known methods in class {@link org.objectteams.Team}.
 * <p>
 * This happens in a two-phase model:
 * <ul>
 * <li>register a listener that reacts to creation of a java debug target, i.e., an application was launched in debug mode
 * <li>for each detected new java debug target install the actual breakpoints.
 * </ul>
 */
public class TeamBreakpointInstaller
{
    private static Hashtable<String, IBreakpoint> OT_BREAKPOINTS = new Hashtable<String, IBreakpoint>(5);

    /**
     * Request breakpoints to be installed once a new launch fires.
     * @param project used for lookup of org.objectteams.Team, i.e., this class must be in the projects classpath.
     * @throws CoreException various reasons, like could not find class org.objectteams.Team or could not create a breakpoint.
     */
    public static void installTeamBreakpoints(IJavaProject project) throws CoreException
    {       
        DebugPlugin.getDefault().addDebugEventListener(new IDebugEventSetListener() {
        	// since we want to avoid using the breakpoint manager (thus hiding synthetic breakpoints from the UI),
        	// we have to track creation of the debug target in order to manually install our breakpoints into the target:
			public void handleDebugEvents(DebugEvent[] events) {
				boolean done = false;
				if (OT_BREAKPOINTS.size() == 0) {
					done = true;
				} else {
					for (DebugEvent event : events) {
						if (event.getKind() == DebugEvent.CREATE) {
							if (event.getSource() instanceof IJavaDebugTarget) {
								IDebugTarget debugTarget = ((IJavaDebugTarget) event.getSource()).getDebugTarget();
								for (IBreakpoint bp : OT_BREAKPOINTS.values())
									debugTarget.breakpointAdded(bp);
								done = true;
								break;
							}
						}
					}
				}
				if (done)
					DebugPlugin.getDefault().removeDebugEventListener(this);					
			}
		});
        try
        {
            IType oot = project.findType(new String(IOTConstants.STR_ORG_OBJECTTEAMS_TEAM));
            if (oot != null)
            {            	
                // Breakpoints in class org.objectteams.Team:
                // bp on ctor, finalize()-, activate()-, deactivate(), -methods
            	if(!OT_BREAKPOINTS.containsKey(OOTBreakpoints.ATTR_OT_BREAKPOINT_CTOR))
            		OT_BREAKPOINTS.put(OOTBreakpoints.ATTR_OT_BREAKPOINT_CTOR, OOTBreakpoints.createOOTConstructorBreakpoint(oot));
            	
            	if(!OT_BREAKPOINTS.containsKey(OOTBreakpoints.ATTR_OT_BREAKPOINT_FINALIZE))
            		OT_BREAKPOINTS.put(OOTBreakpoints.ATTR_OT_BREAKPOINT_FINALIZE, OOTBreakpoints.createOOTFinalizeBreakpoint(oot));
          		
            	if(!OT_BREAKPOINTS.containsKey(OOTBreakpoints.ATTR_OT_BREAKPOINT_ACT))
            		OT_BREAKPOINTS.put(OOTBreakpoints.ATTR_OT_BREAKPOINT_ACT, OOTBreakpoints.createOOTActivateBreakpoint(oot));
            		
       			if(!OT_BREAKPOINTS.containsKey(OOTBreakpoints.ATTR_OT_BREAKPOINT_DEACT))
       				OT_BREAKPOINTS.put(OOTBreakpoints.ATTR_OT_BREAKPOINT_DEACT, OOTBreakpoints.createOOTDeactivateBreakpoint(oot));
                
            	if(!OT_BREAKPOINTS.containsKey(OOTBreakpoints.ATTR_OT_BREAKPOINT_IMPLICIT_ACT))
            		OT_BREAKPOINTS.put(OOTBreakpoints.ATTR_OT_BREAKPOINT_IMPLICIT_ACT, OOTBreakpoints.createOOTImplicitActivateBreakpoint(oot));
            		
       			if(!OT_BREAKPOINTS.containsKey(OOTBreakpoints.ATTR_OT_BREAKPOINT_IMPLICIT_DEACT))
       				OT_BREAKPOINTS.put(OOTBreakpoints.ATTR_OT_BREAKPOINT_IMPLICIT_DEACT, OOTBreakpoints.createOOTImplicitDeactivateBreakpoint(oot));
            }
        }
        catch (JavaModelException ex)
        {
            throw new CoreException(new Status(IStatus.WARNING, OTDebugPlugin.PLUGIN_ID, IStatus.OK, "Cannot set breakpoints for team-activation tracking", ex)); //$NON-NLS-1$
        }
    }

    /** Unregister any previously installed breakpoints, so that the next launch will be without. */
    public static void uninstallTeamBreakpoints() throws CoreException
    {
        OT_BREAKPOINTS.clear();
    }
}
