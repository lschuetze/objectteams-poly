/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2017 GK Software AG
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug.internal.breakpoints;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.debug.core.IJavaBreakpoint;
import org.eclipse.objectteams.otdt.debug.OTDebugPlugin;

public class ClassRedefinitionBreakpoint {
	
	public static final String BREAKPOINT_REDEFINE_CLASSES = OTDebugPlugin.PLUGIN_ID + "InstrumentationBreakpoint.redefineClasses"; //$NON-NLS-1$

	static final String OT_SYNTHETIC_BREAKPOINT = OTDebugPlugin.PLUGIN_ID + "InstrumentationBreakpoint"; //$NON-NLS-1$
	
	static final String INSTRUMENTATION_IMPL = "sun.instrument.InstrumentationImpl"; //$NON-NLS-1$
	static final String REDEFINE_CLASSSES = "redefineClasses"; //$NON-NLS-1$
	static final String REDEFINE_CLASSSES_SIGNATURE = "([Ljava/lang/instrument/ClassDefinition;)V"; //$NON-NLS-1$

    public static IJavaBreakpoint createRedefineClassesBreakpoint(IJavaProject project) throws CoreException {
    	
    	try {
    		IType instrumentation = project.findType(INSTRUMENTATION_IMPL);
    		if (instrumentation != null) {
    			Map<String, Object> attributes = getBreakpointAttributes();
    			attributes.put(BREAKPOINT_REDEFINE_CLASSES, Boolean.TRUE);
    			return OOTBreakpoints.createMethodBreakpoint(instrumentation, REDEFINE_CLASSSES, REDEFINE_CLASSSES_SIGNATURE, false, -1, attributes);
    		}
    	}
    	catch (JavaModelException ex) {
    		throw new CoreException(new Status(IStatus.WARNING, OTDebugPlugin.PLUGIN_ID, IStatus.OK, "Cannot set breakpoints for tracking class redefinition", ex)); //$NON-NLS-1$
    	}
    	return null;
    }

    public static final boolean isRedefineClassesBreakpoint(IBreakpoint breakpoint) throws CoreException {
        return breakpoint.getMarker().getAttribute(BREAKPOINT_REDEFINE_CLASSES) != null;
    }
    
    private static Map<String, Object> getBreakpointAttributes() {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(OT_SYNTHETIC_BREAKPOINT, Boolean.TRUE);
        return attrs;
    }
}
