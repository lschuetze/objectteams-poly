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
import org.eclipse.jdt.debug.core.IJavaBreakpointListener;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.debug.internal.breakpoints.OTBreakpoints;

/**
 * This class un/installs support for breakpoints in certain well-known methods in class {@link org.objectteams.Team}.
 * <p>
 * This happens in a two-phase model:
 * <ul>
 * <li>register a listener that reacts to creation of a java debug target, i.e., an application was launched in debug mode
 * <li>for each detected new java debug target install the actual breakpoints.
 * </ul>
 */
public class OTBreakpointInstaller
{
    private static Hashtable<String, IBreakpoint> OT_BREAKPOINTS = new Hashtable<String, IBreakpoint>(5);

    /** Optional listener for Instrumentation.redefineClasses (when running under OTDRE). */
    private static IJavaBreakpointListener dynListener;

    /**
     * Request breakpoints to be installed once a new launch fires.
     * @param project used for lookup of org.objectteams.Team, i.e., this class must be in the projects classpath.
     * @throws CoreException various reasons, like could not find class org.objectteams.Team or could not create a breakpoint.
     */
    public static void installOTBreakpoints(IJavaProject project, /*Nullable*/IJavaBreakpointListener dynListener)
    		throws CoreException
    {       
    	OTBreakpointInstaller.dynListener = dynListener;
    	if (dynListener != null)
        	JDIDebugModel.addJavaBreakpointListener(dynListener);
    
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
        try {
            IType oot = project.findType(String.valueOf(IOTConstants.STR_ORG_OBJECTTEAMS_TEAM));
            for (OTBreakpoints.Descriptor bpDescriptor : OTBreakpoints.Descriptor.values()) {
				if (bpDescriptor.isOOTBreakPoint()) {
					if (oot != null)
						bpDescriptor.insertInto(oot, OT_BREAKPOINTS);
				} else if (dynListener != null) {
					// other breakpoint only under OTDRE
		            IType instrumentation = project.findType(bpDescriptor.getTypeName());
		            if (instrumentation != null)
						bpDescriptor.insertInto(instrumentation, OT_BREAKPOINTS);
				}
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
    	if (dynListener != null) {
    		JDIDebugModel.removeJavaBreakpointListener(dynListener);
    		dynListener = null;
    	}
        OT_BREAKPOINTS.clear();
    }
}
