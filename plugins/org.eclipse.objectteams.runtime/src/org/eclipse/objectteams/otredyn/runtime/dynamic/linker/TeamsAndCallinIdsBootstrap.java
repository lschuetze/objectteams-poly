package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.DynamicLinker;
import jdk.dynalink.DynamicLinkerFactory;
import jdk.dynalink.StandardOperation;
import jdk.dynalink.linker.GuardingDynamicLinker;

public final class TeamsAndCallinIdsBootstrap {

	private final static int unstableRelinkThreshold = 8;

	private final static GuardingDynamicLinker prioritizedLinkers;

	static {
		prioritizedLinkers = new TeamsAndCallinIdsLinker();
	}

	private TeamsAndCallinIdsBootstrap() {

	}

	public static CallSite bootstrap(final Lookup lookup, final String name, final MethodType type,
			final int joinpointId) {
		return createDynamicLinker(lookup.lookupClass().getClassLoader(), unstableRelinkThreshold)
				.link(new TeamsAndCallinIdsCallSite(
						new TeamsAndCallinIdsCallSiteDescriptor(lookup, StandardOperation.GET, type, joinpointId)));
	}

	public static DynamicLinker createDynamicLinker(final ClassLoader classLoader, final int unstableRelinkThreshold) {
		final DynamicLinkerFactory factory = new DynamicLinkerFactory();
		factory.setPrioritizedLinker(prioritizedLinkers);
		factory.setUnstableRelinkThreshold(unstableRelinkThreshold);
		factory.setClassLoader(classLoader);
		return factory.createLinker();
	}

}
