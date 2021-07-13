package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.DynamicLinker;
import jdk.dynalink.DynamicLinkerFactory;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.LinkerServices;

public final class CallinBootstrap {

	private static final int unstableRelinkThreshold;

	private static final GuardingDynamicLinker prioritizedLinkers;

	static {
		final String threshold = System.getProperty("otdyn.urt");
		unstableRelinkThreshold = (threshold == null) ? 8 : Integer.parseInt(threshold);
		prioritizedLinkers = new CallinLinker();
	}

	private CallinBootstrap() {
	}

	static GuardedInvocation asTypeSafeReturn(final GuardedInvocation inv, final LinkerServices linkerServices,
			final CallSiteDescriptor desc) {
		return inv == null ? null : inv.asTypeSafeReturn(linkerServices, desc.getMethodType());
	}

	// TODO Lars: remove boundMethodId
	public static CallSite bootstrap(final Lookup lookup, final String name, final MethodType type, final int flags,
			final String joinpointDesc, final int boundMethodId) {
//		System.out.println(joinpointDesc);
//		System.out.println("callAllBindings");
		final int newFlags = flags | OTCallSiteDescriptor.CALL_ALL_BINDINGS;
		return createDynamicLinker(lookup.lookupClass().getClassLoader(), unstableRelinkThreshold)
				.link(CallinCallSite.newCallinCallSite(lookup, name, type, newFlags, joinpointDesc, boundMethodId));
	}

	public static CallSite callNext(final Lookup lookup, final String name, final MethodType type, final int flags,
			final String joinpointDesc) {
//		System.out.println(joinpointDesc);
//		System.out.println("callNext");
		final int newFlags = flags | OTCallSiteDescriptor.CALL_NEXT;
		return createDynamicLinker(lookup.lookupClass().getClassLoader(), unstableRelinkThreshold)
				.link(CallinCallSite.newCallinCallSite(lookup, name, type, newFlags, joinpointDesc, 0));
	}

	public static DynamicLinker createDynamicLinker(final ClassLoader classLoader, final int unstableRelinkThreshold) {
		final DynamicLinkerFactory factory = new DynamicLinkerFactory();
		factory.setPrioritizedLinker(prioritizedLinkers);
		factory.setUnstableRelinkThreshold(unstableRelinkThreshold);
		factory.setClassLoader(classLoader);
		return factory.createLinker();
	}
}
