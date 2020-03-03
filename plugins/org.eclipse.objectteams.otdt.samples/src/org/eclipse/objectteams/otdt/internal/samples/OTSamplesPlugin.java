/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2016 Fraunhofer Gesellschaft, Munich, Germany,
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
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.samples;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author gis
 */
public class OTSamplesPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "org.eclipse.objectteams.otdt.samples"; //$NON-NLS-1$
	private static OTSamplesPlugin instance;
	
	public OTSamplesPlugin()
    {
        super();
        instance = this;
    }

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}
	
	public static OTSamplesPlugin getDefault()
	{
		return instance;
	}

	public static Status createErrorStatus(String message, Throwable ex)
	{
		return new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, ex);
	}

	public static void logException(Throwable e, final String title, String message) {
		if (e instanceof InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
		}
		IStatus status = null;
		if (e instanceof CoreException) {
			status = ((CoreException) e).getStatus();
		} else {
			if (message == null)
				message = e.getMessage();
			if (message == null)
				message = e.toString();
			status = new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, e);
		}
		ResourcesPlugin.getPlugin().getLog().log(status);
		Display display = getStandardDisplay();
		final IStatus fstatus = status;
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				ErrorDialog.openError(null, title, null, fstatus);
			}
		});		
	}
	public static IWorkbenchPage getActivePage() {
		IWorkbenchWindow workbenchWin = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		return workbenchWin != null ? workbenchWin.getActivePage() : null;
	}

	public static Display getStandardDisplay() {
		Display display;
		display = Display.getCurrent();
		if (display == null)
			display = Display.getDefault();
		return display;
	}
}
