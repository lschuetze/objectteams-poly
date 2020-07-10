package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.eclipse.objectteams.otredyn.runtime.IBinding;
import org.eclipse.objectteams.otredyn.runtime.TeamManager;
import org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util.ObjectTeamsTypeUtilities;
import org.objectteams.IBoundBase2;
import org.objectteams.ITeam;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.dynalink.linker.support.Lookup;

public final class CallinLinker implements TypeBasedGuardingDynamicLinker {

	private final static MethodHandle FIND_ROLE_METHOD;
	private final static MethodHandle LIFT;

	private static MethodHandle LIFT(final MethodHandles.Lookup lookup, final String joinpointDesc) {
		return MethodHandles.insertArguments(LIFT, 0, lookup, joinpointDesc);
	}

	@SuppressWarnings("unused")
	private static MethodHandle lift(final MethodHandles.Lookup lookup, final String joinpointDesc, final ITeam[] teams,
			final int index) {
		// Retrieve the binding for the current callin
		final int bindingIndex = ObjectTeamsTypeUtilities.getTeamRepeatCount(teams, index);
		final ITeam team = teams[index];
		final IBinding binding = ObjectTeamsTypeUtilities.getBindingFromIndex(joinpointDesc, team, bindingIndex);

		// Retrieve the lifting method for the current binding
		final Class<?> baseClass = ObjectTeamsTypeUtilities.getBaseClass(binding.getBoundClass());
		final String liftingMethod = "$_OT$liftTo$" + binding.getRoleClassName();
		final MethodHandle lift = ObjectTeamsTypeUtilities.findVirtual(lookup, team.getClass(), liftingMethod,
				MethodType.methodType(
						ObjectTeamsTypeUtilities.getRoleItfType(team.getClass(), binding.getRoleClassName()),
						baseClass));

		// Lifting works on interfaces; cast the return type to the implementation type
		final MethodHandle adaptedLift = lift.asType(MethodType.methodType(
				ObjectTeamsTypeUtilities.getRoleImplType(team.getClass(), binding.getRoleClassName()), team.getClass(),
				baseClass));

		return adaptedLift;
	}

	private static MethodHandle FIND_ROLE_METHOD(final MethodHandles.Lookup lookup, final String joinpointDesc) {
		return MethodHandles.insertArguments(FIND_ROLE_METHOD, 0, lookup, joinpointDesc);
	}

	@SuppressWarnings("unused")
	private static MethodHandle findRoleMethod(final MethodHandles.Lookup lookup, final String joinpointDesc,
			final ITeam[] teams, final int index) {
		final int bindingIndex = ObjectTeamsTypeUtilities.getTeamRepeatCount(teams, index);
		final IBinding binding = ObjectTeamsTypeUtilities.getBindingFromIndex(joinpointDesc, teams[index],
				bindingIndex);
		final Class<?> roleClass = ObjectTeamsTypeUtilities.getRoleImplType(teams[index].getClass(),
				binding.getRoleClassName());
		final MethodType mt = MethodType.fromMethodDescriptorString(binding.getRoleMethodSignature(),
				roleClass.getClassLoader());

		try {
			return lookup.findVirtual(roleClass, binding.getRoleMethodName(), mt);
		} catch (NoSuchMethodException | IllegalAccessException e) {
			NoSuchMethodError ee = new NoSuchMethodError();
			ee.initCause(e);
			throw ee;
		}
	}

	static {
		FIND_ROLE_METHOD = Lookup.findOwnStatic(MethodHandles.lookup(), "findRoleMethod", MethodHandle.class,
				MethodHandles.Lookup.class, String.class, ITeam[].class, int.class);
		LIFT = Lookup.findOwnStatic(MethodHandles.lookup(), "lift", MethodHandle.class, MethodHandles.Lookup.class,
				String.class, ITeam[].class, int.class);
	}

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
		if (!(desc instanceof OTCallSiteDescriptor)) {
			throw new LinkageError("CallSiteDescriptor is no OTCallSiteDescriptor");
		}
		final OTCallSiteDescriptor csd = (OTCallSiteDescriptor) desc;
		final int joinpointId = TeamManager.getJoinpointId(csd.getJoinpointDesc());

		final MethodHandle roleMethod = FIND_ROLE_METHOD(desc.getLookup(), csd.getJoinpointDesc());
		final MethodHandle lift = LIFT(desc.getLookup(), csd.getJoinpointDesc());

		throw new UnsupportedOperationException("getGuardedInvocation");
	}

}
