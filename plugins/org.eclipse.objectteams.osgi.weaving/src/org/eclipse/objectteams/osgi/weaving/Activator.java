package org.eclipse.objectteams.osgi.weaving;

import static org.eclipse.objectteams.osgi.weaving.Constants.TRANSFORMER_PLUGIN_ID;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBindingRegistry;
import org.eclipse.objectteams.otequinox.hook.ILogger;
import org.eclipse.objectteams.otre.ClassLoaderAccess;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private static ILog log;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		acquireLog(bundleContext);
	
		OTREInit();
	}

	@SuppressWarnings("deprecation")
	public static AspectBindingRegistry loadAspectBindingRegistry() {
		org.osgi.service.packageadmin.PackageAdmin packageAdmin = null;;
		
		ServiceReference<?> ref= context.getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class.getName());
		if (ref!=null)
			packageAdmin = (org.osgi.service.packageadmin.PackageAdmin)context.getService(ref);
		else
			log(ILogger.ERROR, "Failed to load PackageAdmin service. Will not be able to handle fragments.");

		AspectBindingRegistry aspectBindingRegistry = new AspectBindingRegistry();
		aspectBindingRegistry.loadAspectBindings(packageAdmin);
		return aspectBindingRegistry;
	}

	@SuppressWarnings("restriction")
	private void acquireLog(BundleContext bundleContext) {
		try {
			Activator.log = org.eclipse.core.internal.runtime.InternalPlatform.getDefault().getLog(bundleContext.getBundle());
		} catch (NullPointerException npe) {
			// WTF?
		}
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
