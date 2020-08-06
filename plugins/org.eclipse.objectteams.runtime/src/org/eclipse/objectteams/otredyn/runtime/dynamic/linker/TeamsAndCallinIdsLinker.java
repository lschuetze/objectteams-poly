package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;

import org.eclipse.objectteams.otredyn.runtime.TeamManager;

import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.linker.support.Lookup;

public class TeamsAndCallinIdsLinker implements GuardingDynamicLinker {

	private static MethodHandle getTeamsAndCallinIds = null;

	private static MethodHandle getTeamsAndCallinIds(final MethodHandles.Lookup lookup) {
		if (getTeamsAndCallinIds == null) {
			try {
				getTeamsAndCallinIds = lookup.findStatic(TeamManager.class, "getTeamsAndCallinIds",
						MethodType.methodType(Object[].class, int.class));
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return getTeamsAndCallinIds;
		// return MethodHandles.insertArguments(getTeamsAndCallinIds, 0, joinpointId);
	}

	@Override
	public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices)
			throws Exception {
		MethodHandle result = null;

		// Check if the callsite is unstable
		if (linkRequest.isCallSiteUnstable()) {

		} else {
			if (linkRequest.getCallSiteDescriptor() instanceof TeamsAndCallinIdsCallSiteDescriptor) {
				final TeamsAndCallinIdsCallSiteDescriptor csd = (TeamsAndCallinIdsCallSiteDescriptor) linkRequest
						.getCallSiteDescriptor();
				result = getTeamsAndCallinIds(csd.getLookup());
			}
		}

		final SwitchPoint sp = new SwitchPoint();
		return new GuardedInvocation(result, sp);
	}
}
