/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2013, 2015 GK Software AG
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.equinox.log.LogFilter;
import org.eclipse.equinox.log.Logger;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBinding;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBindingRegistry;
import org.eclipse.objectteams.internal.osgi.weaving.AspectPermissionManager;
import org.eclipse.objectteams.internal.osgi.weaving.DelegatingTransformer.OTAgentNotInstalled;
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
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClassListener;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.util.tracker.ServiceTracker;

@NonNullByDefault
public class TransformerPlugin implements BundleActivator, IAspectRegistry {

    private static final String OTEQUINOX_AGENT_JAR_FILENAME = "otequinoxAgent.jar"; //$NON-NLS-1$
	private static final String OTEQUINOX_LOGGER_NAME = "org.eclipse.objectteams.otequinox.logger"; //$NON-NLS-1$

	/**
	 * State class representing the initialized state, i.e., after {@link start()}
	 * and {@link #initialize()} have been called.
	 */
	static class InitializedPlugin extends TransformerPlugin {

		AspectBindingRegistry aspectBindingRegistry;
		@Nullable AspectPermissionManager aspectPermissionManager;
		ILog log;
		List<Team> teamInstances = new ArrayList<>();
		
		public InitializedPlugin(AspectBindingRegistry aspectBindingRegistry, @Nullable AspectPermissionManager permissionManager, ILog log) {
			this.aspectBindingRegistry = aspectBindingRegistry;
			this.aspectPermissionManager = permissionManager;
			this.log = log;
		}

		@Override
		public boolean isDeniedAspectPlugin(String symbolicName) {
			final AspectPermissionManager manager = this.aspectPermissionManager;
			if (manager != null)
				return manager.isDeniedAspectPlugin(symbolicName);
			return false;
		}

		@Override
		public boolean isOTDT() {
			return this.aspectBindingRegistry.isOTDT();
		}

		@Override
		public boolean isAdaptedBasePlugin(@Nullable String baseBundleName) {
			return this.aspectBindingRegistry.isAdaptedBasePlugin(baseBundleName);
		}

		@Override
		public @Nullable String[] getAdaptedBasePlugins(Bundle aspectBundle) {
			return this.aspectBindingRegistry.getAdaptedBasePlugins(aspectBundle);
		}

		@Override
		public String[] getAdaptingAspectPlugins(@Nullable String id) {
			List<AspectBinding> aspectBindings = this.aspectBindingRegistry.getAdaptingAspectBindings(id);
			if (aspectBindings == null)
				return new String[0];
			String[] result = new String[aspectBindings.size()];
			for (int i = 0; i < result.length; i++)
				result[i] = aspectBindings.get(i).aspectPlugin;
			return result;
		}
	}
	private static @Nullable InitializedPlugin plugin;
	/**
	 * Single point of access: either we get a fully initialized instance, or ISE is thrown.
	 * @throws IllegalStateException if the plugin has not been initialized yet.
	 */
	private static InitializedPlugin plugin() {
		InitializedPlugin plugin = TransformerPlugin.plugin;
		if (plugin == null)
			throw notInitialized();
		return plugin;
	}
	
	static @Nullable BundleContext context;
	public static Bundle getBundle() {
		BundleContext context = TransformerPlugin.context;
		if (context != null)
			return context.getBundle();
		throw new IllegalStateException("TransformerPlugin has not been started");
	}

	private static List<IStatus> pendingLogEntries = new ArrayList<>();
	private static @Nullable URL agentURL; // null signals an error
	

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext bundleContext) throws Exception {
		TransformerPlugin.context = bundleContext;
	
		if (!"false".equals(System.getProperty("ot.equinox"))) {
			OTREInit();
			
			// register our weaving service:
			final OTWeavingHook otWeavingHook = new OTWeavingHook();
			final ServiceRegistration<?> registration = bundleContext.registerService(new String[] { WeavingHook.class.getName(), WovenClassListener.class.getName() },
					otWeavingHook, null);
			
			// but wait until the extension registry is available for reading aspectBindings:
			try {
				ServiceReference<IExtensionRegistry> reference = bundleContext.getServiceReference(IExtensionRegistry.class);
				if (reference != null) {
					safeActivateHook(otWeavingHook, bundleContext, reference, registration);
				} else {
					bundleContext.addServiceListener(
						new ServiceListener() {
							public void serviceChanged(ServiceEvent event) {
								if(event.getType() == ServiceEvent.REGISTERED) {
									ServiceReference<IExtensionRegistry> extensionService = bundleContext.getServiceReference(IExtensionRegistry.class);
									safeActivateHook(otWeavingHook, bundleContext, extensionService, registration);
								}
							}
						},
						"(objectclass="+IExtensionRegistry.class.getName()+")"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			catch (InvalidSyntaxException ex) {
				log(ex, "Failed to register service listener");
			}
		}
		agentURL = bundleContext.getBundle().getEntry("/"+OTEQUINOX_AGENT_JAR_FILENAME);
		log(IStatus.INFO, "agentURL="+agentURL);
	}

	private void safeActivateHook(OTWeavingHook otWeavingHook, BundleContext bundleContext,
			@Nullable ServiceReference<IExtensionRegistry> extensionService, ServiceRegistration<?> registration) {
		try {
			otWeavingHook.activate(bundleContext, extensionService);
		} catch (OTAgentNotInstalled e) {
			registration.unregister();
			ILog log = acquireLog(bundleContext);
			log.log(new Status(IStatus.ERROR,
					bundleContext.getBundle().getSymbolicName(), 
					"Error activating OT/Equinox: "+e.getMessage()));
		}
	}

	@SuppressWarnings("restriction")
	private static ILog acquireLog(BundleContext bundleContext) {
		ServiceTracker<ExtendedLogService,ExtendedLogService> tracker
				= new ServiceTracker<ExtendedLogService,ExtendedLogService>(bundleContext, ExtendedLogService.class, null);
		tracker.open();
		ExtendedLogService logService = tracker.getService();
		Bundle bundle = bundleContext.getBundle();
		Logger logger = logService.getLogger(bundle, OTEQUINOX_LOGGER_NAME);
		org.eclipse.core.internal.runtime.Log result = new org.eclipse.core.internal.runtime.Log(bundle, logger);

		ServiceTracker<ExtendedLogReaderService, ExtendedLogReaderService> logReaderTracker 
				= new ServiceTracker<ExtendedLogReaderService,ExtendedLogReaderService>(bundleContext, ExtendedLogReaderService.class.getName(), null);
		logReaderTracker.open();
		ExtendedLogReaderService logReader = logReaderTracker.getService();

		final Logger equinoxLogger = logService.getLogger(bundle, org.eclipse.core.internal.runtime.PlatformLogWriter.EQUINOX_LOGGER_NAME);

		// listen to log events from our logger and asynchronously dispatch them to the equinox logger
		logReader.addLogListener(
			new LogListener() {
				@Override @NonNullByDefault(false)
				public void logged(LogEntry entry) {
					equinoxLogger.log(entry.getLevel(), entry.getMessage(), entry.getException());
				}
			},
			new LogFilter() {
				@Override @NonNullByDefault(false)
				public boolean isLoggable(Bundle bundle, String loggerName, int logLevel) {
					return OTEQUINOX_LOGGER_NAME.equals(loggerName);
				}
			}
		);
		return result;
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
	public void stop(@Nullable BundleContext bundleContext) throws Exception {
		plugin = null;
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

	public static void log (Throwable ex, String msg) {
		msg = "OT/Equinox: "+msg;
		Status status = new Status(IStatus.ERROR, TRANSFORMER_PLUGIN_ID, msg, ex);
		final InitializedPlugin plugin = TransformerPlugin.plugin;
		if (plugin != null) {
			plugin.log.log(status);
		} else {
			System.err.println(msg);
			ex.printStackTrace();
			synchronized (TransformerPlugin.class) {
				pendingLogEntries.add(status);
			}
		}
	}
	
	public static void log(int status, String msg) {
		if (status >= WARN_LEVEL)
			doLog(status, msg);
	}

	public static void doLog(int level, String msg) {
		try {
			Status status = new Status(level, TRANSFORMER_PLUGIN_ID, "OT/Equinox: "+msg);
			final InitializedPlugin plugin = TransformerPlugin.plugin;
			if (plugin != null) {
				plugin.log.log(status);
			} else {
				synchronized(TransformerPlugin.class) {
					pendingLogEntries.add(status);
				}
			}
		} catch (NoClassDefFoundError err) {
			if (level >= WARN_LEVEL)
				System.out.println(">> OT/Equinox: "+msg);
		}
	}
	
	public static void flushLog() {
		List<IStatus> copy;
		synchronized(TransformerPlugin.class) {
			copy = pendingLogEntries;
			pendingLogEntries = new ArrayList<>();
		}
		for (IStatus status : copy) {
			final InitializedPlugin plugin = TransformerPlugin.plugin;
			if (plugin != null) {
				plugin.log.log(status);
			} else {
				if (status.getCode() == IStatus.ERROR)
					System.err.println(status.getMessage());
				else
					System.out.println(status.getMessage());
			}
		}
	}
	
	public static void initialize(BundleContext bundleContext, AspectBindingRegistry aspectBindingRegistry, @Nullable AspectPermissionManager permissionManager) {
		plugin = new InitializedPlugin(aspectBindingRegistry, permissionManager, acquireLog(bundleContext));
	}

	/**
	 * Get the singleton instance of this class.
	 * <p>
	 * This method must not be called before the plugin is fully initialized, which depends
	 * on two triggers:
	 * </p>
	 * <ul>
	 * <li>This current plugin must be started by Equinox (should be guaranteed on access by Equinox).</li>
	 * <li>The extension registry has been started, which in turn triggers reading extensions against our extension points.</li>
	 * </ul>
	 */
	public static TransformerPlugin getDefault() {
		return plugin();
	}

	public static synchronized void registerTeamInstance(Team instance) {
		plugin().teamInstances.add(instance);
	}
	/**
	 * Copy all registered team instances into the given list,
     */
	public static synchronized void getTeamInstances(List<Team> list) {
		list.addAll(plugin().teamInstances);
	}

	/**
	 * public API:
	 * {@link IAspectRegistry#getAdaptingAspectPlugins(Bundle)} 
	 */
	public String[] getAdaptingAspectPlugins(Bundle basePlugin) {
		return getAdaptingAspectPlugins(basePlugin.getSymbolicName());
	}

	public String[] getAdaptingAspectPlugins(@Nullable String id) {
		throw notInitialized();
	}

	@Override
	public boolean isOTDT() {
		throw notInitialized();
	}

	@Override
	public boolean isAdaptedBasePlugin(@Nullable String baseBundleName) {
		throw notInitialized();
	}

	@Override
	public @Nullable String[] getAdaptedBasePlugins(Bundle aspectBundle) {
		throw notInitialized();
	}

	@Override
	public boolean hasInternalTeams(Bundle bundle) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDeniedAspectPlugin(String symbolicName) {
		throw notInitialized();
	}

	static IllegalStateException notInitialized() {
		return new IllegalStateException("TransformerPlugin has not been initialized");
	}

	public static @Nullable String getOtequinoxAgentPath() {
		if (agentURL != null) {
			try {
				return new File(FileLocator.toFileURL(agentURL).getFile())
							.getAbsolutePath();
			} catch (IOException e) {
				log(new IllegalStateException(e), "Failed to intialize location of "+OTEQUINOX_AGENT_JAR_FILENAME);
			}
		} else {
			log(IStatus.ERROR, "Failed to intialize location of "+OTEQUINOX_AGENT_JAR_FILENAME);
		}
		return null;
	}
}
