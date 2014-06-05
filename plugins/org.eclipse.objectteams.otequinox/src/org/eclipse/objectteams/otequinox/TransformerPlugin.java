/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2013, 2014 GK Software AG
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
package org.eclipse.objectteams.otequinox;

import static org.eclipse.objectteams.otequinox.Constants.TRANSFORMER_PLUGIN_ID;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBinding;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBindingRegistry;
import org.eclipse.objectteams.internal.osgi.weaving.OTWeavingHook;
import org.eclipse.objectteams.otre.ClassLoaderAccess;
import org.objectteams.Team;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClassListener;

public class TransformerPlugin implements BundleActivator, IAspectRegistry {

	private static BundleContext context;

	public static Bundle getBundle() {
		return context.getBundle();
	}

	private static ILog log;
	private static List<IStatus> pendingLogEntries = new ArrayList<>();
	private static TransformerPlugin plugin;
	
	private AspectBindingRegistry aspectBindingRegistry;
	private List<Team> teamInstances = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext bundleContext) throws Exception {
		plugin = this;
		TransformerPlugin.context = bundleContext;
		
		acquireLog(bundleContext);
	
		if (!"false".equals(System.getProperty("ot.equinox"))) {
			OTREInit();
			
			// register our weaving service:
			final OTWeavingHook otWeavingHook = new OTWeavingHook();
			context.registerService(new String[] { WeavingHook.class.getName(), WovenClassListener.class.getName() },
					otWeavingHook, null);
			
			// but wait until the extension registry is available for reading aspectBindings:
			try {
				ServiceReference<IExtensionRegistry> reference = context.getServiceReference(IExtensionRegistry.class);
				if (reference != null) {
					otWeavingHook.activate(bundleContext, reference);
				} else {
					context.addServiceListener(
						new ServiceListener() { 
							public void serviceChanged(ServiceEvent event) {
								if(event.getType() == ServiceEvent.REGISTERED)
									otWeavingHook.activate(bundleContext, context.getServiceReference(IExtensionRegistry.class));
							}
						},
						"(objectclass="+IExtensionRegistry.class.getName()+")"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			catch (InvalidSyntaxException ex) {
				log(ex, "Failed to register service listener");
			}
		}
	}

	@SuppressWarnings("restriction")
	private static void acquireLog(BundleContext bundleContext) {
		try {
			TransformerPlugin.log = org.eclipse.core.internal.runtime.InternalPlatform.getDefault().getLog(bundleContext.getBundle());
		} catch (NullPointerException npe) {
			// WTF?
		}
	}

	private void OTREInit() {
		// this influences the OTRE behavior (see e.g., JPLISEnhancer):
		System.setProperty("ot.equinox", "true");
		try {
			ClassLoaderAccess.setLoadClass(Bundle.class.getMethod("loadClass", String.class));
			ClassLoaderAccess.setGetResource(Bundle.class.getMethod("getResource", String.class));
		} catch (NoSuchMethodException | SecurityException e) {
			log(e, "Failed to wire an OSGi class into the OTRE");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		TransformerPlugin.context = null;
	}

	// configure OT/Equinox debugging:
	public static int WARN_LEVEL = IStatus.ERROR;
	static {
		String level = System.getProperty("otequinox.debug");
		if (level != null) {
			level = level.toUpperCase();
			if (level.equals("OK"))
				WARN_LEVEL = IStatus.OK;
			else if (level.equals("INFO"))
				WARN_LEVEL = IStatus.INFO;
			else if (level.startsWith("WARN"))
				WARN_LEVEL = IStatus.WARNING;
			else if (level.startsWith("ERR"))
				WARN_LEVEL = IStatus.ERROR;
			else
				WARN_LEVEL = IStatus.OK;
		}
	}

	public static synchronized void log (Throwable ex, String msg) {
		msg = "OT/Equinox: "+msg;
		System.err.println(msg);
		ex.printStackTrace();
		pendingLogEntries.add(new Status(IStatus.ERROR, TRANSFORMER_PLUGIN_ID, msg, ex));
	}
	
	public static void log(int status, String msg) {
		if (status >= WARN_LEVEL)
			doLog(status, msg);
	}

	public static synchronized void doLog(int status, String msg) {
		msg = "OT/Equinox: "+msg;
		try {
// this seems to cause java.lang.NoClassDefFoundError: org/eclipse/ui/statushandlers/StatusAdapter etc.
//		if (log == null) acquireLog(context);
			pendingLogEntries.add(new Status(status, TRANSFORMER_PLUGIN_ID, msg));
		} catch (NoClassDefFoundError e) {
			System.err.println("OT/Euqinox (not ready for logging): "+msg);
		}
	}
	
	public static void flushLog() {
		List<IStatus> copy;
		synchronized(TransformerPlugin.class) {
			copy = pendingLogEntries;
			pendingLogEntries = new ArrayList<>();
		}
		for (IStatus status : copy) {
			if (log != null) {
				log.log(status);
			} else {
				if (status.getCode() == IStatus.ERROR)
					System.err.println(status.getMessage());
				else
					System.out.println(status.getMessage());
			}
		}
	}
	

	public static TransformerPlugin getDefault() {
		return plugin;
	}

	public void registerAspectBindingRegistry(AspectBindingRegistry aspectBindingRegistry) {
		this.aspectBindingRegistry = aspectBindingRegistry;
	}

	/**
	 * public API:
	 * {@link IAspectRegistry#getAdaptingAspectPlugins(Bundle)} 
	 */
	public @NonNull String[] getAdaptingAspectPlugins(Bundle basePlugin) {
		return getAdaptingAspectPlugins(basePlugin.getSymbolicName());
	}

	public @NonNull String[] getAdaptingAspectPlugins(String id) {
		List<AspectBinding> aspectBindings = this.aspectBindingRegistry.getAdaptingAspectBindings(id);
		if (aspectBindings == null)
			return new String[0];
		String[] result = new String[aspectBindings.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = aspectBindings.get(i).aspectPlugin;
		return result;
	}

	public static synchronized void registerTeamInstance(Team instance) {
		plugin.teamInstances.add(instance);
	}
	/**
	 * Copy all registered team instances into the given list,
     */
	public static synchronized void getTeamInstances(List<Team> list) {
		list.addAll(plugin.teamInstances);
	}

	@Override
	public boolean isOTDT() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAdaptedBasePlugin(String baseBundleName) {
		return this.aspectBindingRegistry.isAdaptedBasePlugin(baseBundleName);
	}

	@Override
	public String[] getAdaptedBasePlugins(Bundle aspectBundle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasInternalTeams(Bundle bundle) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDeniedAspectPlugin(String symbolicName) {
		// TODO Auto-generated method stub
		return false;
	}
}
