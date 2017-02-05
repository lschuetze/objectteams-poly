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
package org.eclipse.objectteams.otdt.internal.debug.adaptor.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdi.internal.ReferenceTypeImpl;
import org.eclipse.jdi.internal.ValueCache;
import org.eclipse.jdi.internal.jdwp.JdwpReferenceTypeID;
import org.eclipse.jdt.core.dom.Message;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaBreakpoint;
import org.eclipse.jdt.debug.core.IJavaBreakpointListener;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaLineBreakpoint;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;
import org.eclipse.objectteams.otdt.debug.internal.breakpoints.OTBreakpoints;
import org.eclipse.objectteams.otdt.internal.debug.adaptor.OTDebugAdaptorPlugin;

import com.sun.jdi.VirtualMachine;

import base org.eclipse.jdi.internal.VirtualMachineImpl;

/**
 * Listen to breakpoint hits on InstrumentationImpl.redefineClasses in order to
 * <ul>
 * <li>create obsolete {@link ReferenceTypeImpl} from internal cache
 * <li>refresh breakpoints in the affected type
 * </ul>
 * Implemented as a team only to gain access to the cache inside {@link VirtualMachineImpl}.
 */
@SuppressWarnings("restriction")
public team class RedefineClassesBPListener implements IJavaBreakpointListener {

	/**
	 * Conditionally get a breakpoint listener for class redefinition events (only for OTDRE). 
	 */
	public static IJavaBreakpointListener get(WeavingScheme scheme) {
    	if (scheme == WeavingScheme.OTDRE)
    		return new RedefineClassesBPListener();
		return null;
	}

	@Override
	public int breakpointHit(IJavaThread thread, IJavaBreakpoint breakpoint) {
	    if (!breakpoint.getMarker().exists())
	    	return DONT_CARE;
	    try {
		    if (OTBreakpoints.Descriptor.RedefineClasses.matches(breakpoint)) {
		    	handleClassRedefinition(thread);
				return IJavaBreakpointListener.DONT_SUSPEND;
		    }
	    }
	    catch (Exception ex) {
	 		OTDebugAdaptorPlugin.logException("RedefineClassesBPListener can't read infos from debugTarget anymore. Disconnected?", ex); //$NON-NLS-1$
			// if something fails, let the debugger go on
			return IJavaBreakpointListener.DONT_SUSPEND;
		}

	    return IJavaBreakpointListener.DONT_CARE;
	}

	private void handleClassRedefinition(IJavaThread thread) throws DebugException {
		IStackFrame frame = thread.getTopStackFrame();
		IVariable[] variables = frame.getVariables(); 
		IValue values = variables[2].getValue();
		if (values instanceof IJavaArray) {
			IJavaValue[] arrayValues = ((IJavaArray) values).getValues();
			for (IJavaValue value : arrayValues) {
				if (value instanceof IJavaObject) {
					IJavaValue clazz = ((IJavaObject) value).sendMessage("getDefinitionClass", "()Ljava/lang/Class;", null, thread, false);
					IJavaValue name = ((IJavaObject) clazz).sendMessage("getName", "()Ljava/lang/String;", null, thread, false);
					String className = name.getValueString();
					VirtualMachine vm = ((org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget) thread.getDebugTarget()).getVM();
					removeTypeFromCache((org.eclipse.jdi.internal.VirtualMachineImpl) vm, className);
					updateBreakpoints(thread.getDebugTarget(), className);
				}
			}
		}
	}

	private void removeTypeFromCache(VirtualMachineImpl as VM vm, String typeName) {
		vm.removeTypeFromCache(typeName);
	}

	/**
	 * Gateway to inaccessible cache of ReferenceTypeImpl
	 */
	protected class VM playedBy VirtualMachineImpl {
		@SuppressWarnings("decapsulation")
		ValueCache getCachedReftypes() -> get ValueCache fCachedReftypes;
		
		protected void removeTypeFromCache(String typeName) {
			List<JdwpReferenceTypeID> found = new ArrayList<>();
			ValueCache cache = getCachedReftypes();
			for (Object value : cache.values()) {
				if (value instanceof ReferenceTypeImpl) { 
					ReferenceTypeImpl refType = (ReferenceTypeImpl) value;
					if (refType.name().equals(typeName))
						found.add(refType.getRefTypeID());
				}
			}
			/* alternative to above loop (but involves a JDWP request:
			   List<ReferenceType> classes = target.jdiClassesByName(name);
			 */
			for (JdwpReferenceTypeID id : found) {
				cache.remove(id);
			}
		}
	}

	private void updateBreakpoints(IDebugTarget debugTarget, String className) {
		// cf. JavaHotCodeReplaceManager.redefineTypesJDK() -> target.reinstallBreakpointsIn()
        IBreakpointManager breakpointManager = DebugPlugin.getDefault().getBreakpointManager();
        IBreakpoint[] breakpoints = breakpointManager.getBreakpoints(JDIDebugModel.getPluginIdentifier());
        for (IBreakpoint breakpoint : breakpoints) {
			if (breakpoint instanceof IJavaLineBreakpoint) {
				IJavaLineBreakpoint lineBreakpoint = (IJavaLineBreakpoint) breakpoint;
				try {
					if (lineBreakpoint.getTypeName().equals(className)) {
						debugTarget.breakpointRemoved(lineBreakpoint, null);
						debugTarget.breakpointAdded(lineBreakpoint);
					}
				} catch (CoreException e) {
					OTDebugAdaptorPlugin.logException("Failed to update breakpoint", e);
				}
			}
		}
	}

	// --- empty implementation of unused hooks: ---
	@Override
	public int installingBreakpoint(IJavaDebugTarget target, IJavaBreakpoint breakpoint, IJavaType type) {
		return IJavaBreakpointListener.DONT_CARE;
	}

	@Override public void addingBreakpoint(IJavaDebugTarget target, IJavaBreakpoint breakpoint) { }
	@Override public void breakpointInstalled(IJavaDebugTarget target, IJavaBreakpoint breakpoint) { }
	@Override public void breakpointRemoved(IJavaDebugTarget target, IJavaBreakpoint breakpoint) { }
	@Override public void breakpointHasRuntimeException(IJavaLineBreakpoint breakpoint, DebugException exception) { }
	@Override public void breakpointHasCompilationErrors(IJavaLineBreakpoint breakpoint, Message[] errors) { }
}
