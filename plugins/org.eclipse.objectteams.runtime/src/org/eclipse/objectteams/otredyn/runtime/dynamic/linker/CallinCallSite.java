package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.support.ChainedCallSite;

public final class CallinCallSite extends ChainedCallSite {

	private static final int maxChainLength;

	static {
		final String chainLength = System.getProperty("otdyn.mcl");
		maxChainLength = (chainLength == null) ? 8 : Integer.parseInt(chainLength);
	}

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
	public int getMaxChainLength() {
		return maxChainLength;
	}
}
