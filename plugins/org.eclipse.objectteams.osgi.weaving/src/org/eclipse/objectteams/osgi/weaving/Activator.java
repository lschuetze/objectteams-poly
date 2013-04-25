package org.eclipse.objectteams.osgi.weaving;

import static org.eclipse.objectteams.osgi.weaving.Constants.TRANSFORMER_PLUGIN_ID;

import java.util.Hashtable;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBindingRegistry;
import org.eclipse.objectteams.internal.osgi.weaving.OTWeavingHook;
import org.eclipse.objectteams.otequinox.hook.ILogger;
import org.eclipse.objectteams.otre.ClassLoaderAccess;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.weaving.WeavingHook;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private ServiceRegistration<WeavingHook> serviceRegistration;
	private AspectBindingRegistry aspectBindingRegistry;

	@SuppressWarnings("deprecation")
	private org.osgi.service.packageadmin.PackageAdmin packageAdmin;

	private static ILog log;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		frameworkInit(bundleContext);

		this.aspectBindingRegistry = new AspectBindingRegistry();
		this.aspectBindingRegistry.loadAspectBindings(this.packageAdmin);

		OTWeavingHook otWeavingHook = new OTWeavingHook(this.aspectBindingRegistry);
		this.serviceRegistration = context.registerService(WeavingHook.class, otWeavingHook, new Hashtable<String, Object>());
		
		OTREInit();
	}

	@SuppressWarnings({ "restriction", "deprecation" })
	private void frameworkInit(BundleContext bundleContext) {
		try {
			Activator.log = org.eclipse.core.internal.runtime.InternalPlatform.getDefault().getLog(bundleContext.getBundle());
		} catch (NullPointerException npe) {
			// WTF?
		}
		
		ServiceReference<?> ref= context.getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class.getName());
		if (ref!=null)
			this.packageAdmin = (org.osgi.service.packageadmin.PackageAdmin)context.getService(ref);
		else
			log(ILogger.ERROR, "Failed to load PackageAdmin service. Will not be able to handle fragments.");
	}

	private void OTREInit() {
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
		Activator.context = null;
		this.serviceRegistration.unregister();
	}

	// configure OT/Equinox debugging:
	public static int WARN_LEVEL = ILogger.ERROR;
	static {
		String level = System.getProperty("otequinox.debug");
		if (level != null) {
			level = level.toUpperCase();
			if (level.equals("OK"))
				WARN_LEVEL = ILogger.OK;
			else if (level.equals("INFO"))
				WARN_LEVEL = ILogger.INFO;
			else if (level.startsWith("WARN"))
				WARN_LEVEL = ILogger.WARNING;
			else if (level.startsWith("ERR"))
				WARN_LEVEL = ILogger.ERROR;
			else
				WARN_LEVEL = ILogger.OK;
		}
	}

	public static void log (Throwable ex, String msg) {
		msg = "OT/Equinox: "+msg;
		System.err.println(msg);
		ex.printStackTrace();
		log.log(new Status(IStatus.ERROR, TRANSFORMER_PLUGIN_ID, msg, ex));
	}
	
	public static void log(int status, String msg) {
		if (status >= WARN_LEVEL)
			doLog(status, msg);
	}

	private static void doLog(int status, String msg) {
		msg = "OT/Equinox: "+msg;
		if (log != null)
			log.log(new Status(status, TRANSFORMER_PLUGIN_ID, msg));
		else
			System.err.println(msg);
	}
}
