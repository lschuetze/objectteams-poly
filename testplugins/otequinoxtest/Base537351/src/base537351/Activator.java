package base537351;

import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	@Override
	public void start(@NonNull BundleContext context) throws @NonNull Exception {
		System.out.println("START");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("SHUTDOWN");
	}

}
