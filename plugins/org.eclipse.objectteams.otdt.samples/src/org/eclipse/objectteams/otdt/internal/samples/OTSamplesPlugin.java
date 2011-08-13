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
 * $Id: OTSamplesPlugin.java 23482 2010-02-05 20:16:19Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.samples;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * @author gis
 */
@SuppressWarnings("deprecation") // package admin is still recommended for this particular purpose
public class OTSamplesPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "org.eclipse.objectteams.otdt.samples"; //$NON-NLS-1$
	private static final String OT_DOC_BUNDLE = "org.eclipse.objectteams.otdt.doc"; //$NON-NLS-1$
	private static OTSamplesPlugin instance;
	private BundleContext context;
	
	public OTSamplesPlugin()
    {
        super();
        instance = this;
    }

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.context = context;
	}
	
	public static OTSamplesPlugin getDefault()
	{
		return instance;
	}

	public Bundle getHelpPlugin() {
		ServiceReference<PackageAdmin> ref= (ServiceReference<PackageAdmin>) this.context.getServiceReference(PackageAdmin.class);
		if (ref == null)
			throw new IllegalStateException("Cannot connect to PackageAdmin"); //$NON-NLS-1$
		PackageAdmin packageAdmin = context.getService(ref);
		return packageAdmin.getBundles(OT_DOC_BUNDLE, null)[0];
	}

	public static Status createErrorStatus(String message, Throwable ex)
	{
		return new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, ex);
	}
}
