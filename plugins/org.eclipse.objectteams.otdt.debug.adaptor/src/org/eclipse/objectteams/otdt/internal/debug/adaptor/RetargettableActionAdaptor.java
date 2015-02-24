/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009 Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: RetargettableActionAdaptor.java 23456 2010-02-04 20:44:45Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.debug.adaptor;

import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.objectteams.otdt.debug.ui.internal.actions.OTToggleBreakpointAdapter;

import base org.eclipse.jdt.internal.debug.ui.actions.RetargettableActionAdapterFactory;

/**
 * This team replaces the former OTRetargettableActionAdapterFactory class.
 * 
 * @author mosconi
 */
@SuppressWarnings("restriction")
public team class RetargettableActionAdaptor {
	protected class RetargettableActionAdapterFactory playedBy RetargettableActionAdapterFactory {
	    @SuppressWarnings({ "basecall", "unchecked" })
		callin <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
	        if (adapterType == IToggleBreakpointsTarget.class) {
	            return (T)new OTToggleBreakpointAdapter();
	        }
	        return base.getAdapter(adaptableObject, adapterType);
	    }
	    getAdapter <- replace getAdapter;
	}
}
