package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import org.objectteams.IBoundBase2;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.linker.TypeBasedGuardingDynamicLinker;

public final class CallinLinker implements TypeBasedGuardingDynamicLinker {

	@Override
	public boolean canLinkType(final Class<?> type) {
		return IBoundBase2.class.isAssignableFrom(type);
	}

	@Override
	public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices)
			throws Exception {
		final CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
		return CallinBootstrap.asTypeSafeReturn(getGuardedInvocation(linkRequest, desc), linkerServices, desc);
	}

	static GuardedInvocation getGuardedInvocation(final LinkRequest request, final CallSiteDescriptor desc) {

		throw new AssertionError("Not implemented.");
	}

}
