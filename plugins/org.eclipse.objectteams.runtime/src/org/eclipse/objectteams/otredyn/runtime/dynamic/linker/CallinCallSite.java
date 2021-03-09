package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.support.ChainedCallSite;

public final class CallinCallSite extends ChainedCallSite {

	CallinCallSite(CallSiteDescriptor descriptor) {
		super(descriptor);
	}

	public OTCallSiteDescriptor getOTCallSiteDescriptor() {
		return (OTCallSiteDescriptor) getDescriptor();
	}

	public static CallinCallSite newCallinCallSite(final Lookup lookup, final String name, final MethodType type,
			final int flags, final String joinpointDesc, final int boundMethodId) {
		final OTCallSiteDescriptor desc = OTCallSiteDescriptor.get(lookup, name, type, flags, joinpointDesc,
				boundMethodId);
		return new CallinCallSite(desc);
	}

	@Override
	protected int getMaxChainLength() {
		return 12;
	}
}
