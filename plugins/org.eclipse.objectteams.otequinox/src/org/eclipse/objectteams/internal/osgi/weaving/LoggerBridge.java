/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2020 GK Software SE
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.log.Logger;
import org.eclipse.osgi.framework.log.FrameworkLogEntry;
import org.osgi.service.log.LogService;

/**
 * Utility to bridge from IStatus logging to {@link Logger}.
 */
public class LoggerBridge {
	
	public static void log(Logger logger, IStatus status) {
		logger.log(LoggerBridge.getLog(status), LoggerBridge.getLevel(status), status.getMessage(), status.getException());
	}

	// --- copied from org.eclipse.core.internal.runtime.PlatformLogWriter.getLog(IStatus): ---

	public static final String EQUINOX_LOGGER_NAME = "org.eclipse.equinox.logger"; //$NON-NLS-1$

	static FrameworkLogEntry getLog(IStatus status) {
		Throwable t = status.getException();
		ArrayList<FrameworkLogEntry> childlist = new ArrayList<>();

		int stackCode = t instanceof CoreException ? 1 : 0;
		// ensure a substatus inside a CoreException is properly logged 
		if (stackCode == 1) {
			IStatus coreStatus = ((CoreException) t).getStatus();
			if (coreStatus != null) {
				childlist.add(getLog(coreStatus));
			}
		}

		if (status.isMultiStatus()) {
			IStatus[] children = status.getChildren();
			for (IStatus child : children) {
				childlist.add(getLog(child));
			}
		}

		FrameworkLogEntry[] children = childlist.size() == 0 ? null : childlist.toArray(new FrameworkLogEntry[childlist.size()]);

		return new FrameworkLogEntry(status, status.getPlugin(), status.getSeverity(), status.getCode(), status.getMessage(), stackCode, t, children);
	}

	@SuppressWarnings("deprecation")
	public static int getLevel(IStatus status) {
		switch (status.getSeverity()) {
			case IStatus.ERROR :
				return LogService.LOG_ERROR;
			case IStatus.WARNING :
				return LogService.LOG_WARNING;
			case IStatus.INFO :
				return LogService.LOG_INFO;
			case IStatus.OK :
				return LogService.LOG_DEBUG;
			case IStatus.CANCEL :
			default :
				return 32; // unknown
		}
	}
}
