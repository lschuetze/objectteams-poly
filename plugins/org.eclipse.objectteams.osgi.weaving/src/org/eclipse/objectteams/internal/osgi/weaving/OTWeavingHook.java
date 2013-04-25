package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.osgi.weaving.Activator.log;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer;
import org.osgi.framework.Bundle;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;
import org.osgi.framework.wiring.BundleWiring;

public class OTWeavingHook implements WeavingHook {

	private AspectBindingRegistry aspectBindingRegistry;
	private ObjectTeamsTransformer objectTeamsTransformer;
	
	public OTWeavingHook(AspectBindingRegistry aspectBindingRegistry) {
		this.aspectBindingRegistry = aspectBindingRegistry;
		this.objectTeamsTransformer = new ObjectTeamsTransformer();
	}

	@Override
	public void weave(WovenClass wovenClass) {
		try {
			// TODO(SH): ideally this trigger would be inserted into the previous woven class
			// do whatever left-overs we find from previous invocations:
			aspectBindingRegistry.instantiateScheduledTeams();
			
			BundleWiring bundleWiring = wovenClass.getBundleWiring();
			String bundleName = bundleWiring.getBundle().getSymbolicName();
			String className = wovenClass.getClassName();
			
			// do whatever is needed *before* loading this class:
			aspectBindingRegistry.triggerLoadingHooks(bundleName, className);
			
			if (requiresWeaving(bundleWiring)) {
				Class<?> classBeingRedefined = null; // TODO
				ProtectionDomain protectionDomain = null; // TODO
				byte[] bytes = wovenClass.getBytes();
				try {
					log(IStatus.INFO, "About to transform class "+wovenClass);
					byte[] newBytes = objectTeamsTransformer.transform(bundleWiring.getClassLoader(),
										className, classBeingRedefined, protectionDomain, bytes);
					if (newBytes != bytes)
						wovenClass.setBytes(newBytes);
				} catch (IllegalClassFormatException e) {
					log(e, "Failed to transform class "+className);
				}
			}
			// unblock any waiting teams depending on this class:
			aspectBindingRegistry.scheduleTeamClassesFor(className);
		} catch (ClassCircularityError cce) {
			log(cce, "Weaver encountered a circular class dependency");
		}
	}

	private boolean requiresWeaving(BundleWiring bundleWiring) {
		@SuppressWarnings("null")@NonNull
		Bundle bundle = bundleWiring.getBundle();
		return aspectBindingRegistry.getAdaptedBasePlugins(bundle) != null
				|| aspectBindingRegistry.isAdaptedBasePlugin(bundle.getSymbolicName());
	}

}
