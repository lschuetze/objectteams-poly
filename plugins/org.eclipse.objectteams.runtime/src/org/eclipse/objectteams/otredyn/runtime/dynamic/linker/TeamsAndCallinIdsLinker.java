package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.objectteams.otredyn.runtime.TeamManager;

import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.linker.support.Lookup;

public class TeamsAndCallinIdsLinker implements GuardingDynamicLinker {

	private static final MethodHandle TEAMS_AND_CALLIN_IDS;
	private static final MethodHandle CACHED_VALUE;

	static {
		CACHED_VALUE = Lookup.findOwnStatic(MethodHandles.lookup(), "cachedValue", Object[].class, Object[].class);
		TEAMS_AND_CALLIN_IDS = Lookup.PUBLIC.findStatic(TeamManager.class, "getTeamsAndCallinIds",
				MethodType.methodType(Object[].class, int.class));
	}

	@SuppressWarnings("unused")
	private static Object[] cachedValue(final Object[] value) {
		return value;
	}

	@Override
	public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices)
			throws Exception {
		final TeamsAndCallinIdsCallSiteDescriptor csd;
		if (linkRequest.getCallSiteDescriptor() instanceof TeamsAndCallinIdsCallSiteDescriptor) {
			csd = (TeamsAndCallinIdsCallSiteDescriptor) linkRequest.getCallSiteDescriptor();
		} else {
			throw new IllegalArgumentException();
		}

		final GuardedInvocation result;
		final int joinpointId = csd.getJoinpointId();
		// Check if the callsite is unstable
		if (linkRequest.isCallSiteUnstable()) {
			// Behave as there is no invokedynamic
			//final MethodHandle target = TEAMS_AND_CALLIN_IDS.bindTo(joinpointId);
			final MethodHandle target = MethodHandles.insertArguments(TEAMS_AND_CALLIN_IDS, 0, joinpointId);
			result = new GuardedInvocation(target);
		} else {
			final Object[] data = TeamManager.getTeamsAndCallinIds(joinpointId);
			final MethodHandle target = CACHED_VALUE.bindTo(data);
			//final MethodHandle target = MethodHandles.insertArguments(CACHED_VALUE, 0, data);
			// TODO Lars: Do we still need a SwitchPoint when there are the guards over teams?
			final SwitchPoint sp = new SwitchPoint();
			TeamManager.registerSwitchPoint(joinpointId, sp);
			result = new GuardedInvocation(target, sp);
		}
		return result;
	}

}
