package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.objectteams.otredyn.runtime.IBinding;
import org.eclipse.objectteams.otredyn.runtime.IBinding.CallinModifier;
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

	private final static MethodHandle LOOP_BODY_BEFORE;
	// TODO Lars: after and replace
	private final static MethodHandle LOOP_START;
	private final static MethodHandle LOOP_END;

	static {
		FIND_ROLE_METHOD = Lookup.findOwnStatic(MethodHandles.lookup(), "findRoleMethod", MethodHandle.class,
				MethodHandles.Lookup.class, String.class, ITeam.class, IBinding.class);

		LIFT = Lookup.findOwnStatic(MethodHandles.lookup(), "lift", MethodHandle.class, MethodHandles.Lookup.class,
				String.class, ITeam.class, IBinding.class, Class.class);

		LOOP_BODY_BEFORE = Lookup.findOwnStatic(MethodHandles.lookup(), "loopBodyBefore", MethodHandle.class,
				MethodHandles.Lookup.class, String.class, MethodHandle.class, int.class, IBoundBase2.class,
				ITeam[].class, int.class, int[].class, int.class, Object[].class);

		LOOP_START = Lookup.findOwnStatic(MethodHandles.lookup(), "loopStart", int.class, IBoundBase2.class,
				ITeam[].class, int.class, int[].class, int.class, Object[].class);

		LOOP_END = Lookup.findOwnStatic(MethodHandles.lookup(), "loopEnd", int.class, String.class, IBoundBase2.class,
				ITeam[].class, int.class, int[].class, int.class, Object[].class);
	}

	private static MethodHandle LIFT(final MethodHandles.Lookup lookup, final String joinpointDesc) {
		return MethodHandles.insertArguments(LIFT, 0, lookup, joinpointDesc);
	}

	@SuppressWarnings("unused")
	private static MethodHandle lift(final MethodHandles.Lookup lookup, final String joinpointDesc, final ITeam team,
			final IBinding binding, final Class<?> baseClass) {
		// Retrieve the lifting method for the current binding
		final String liftingMethod = "_OT$liftTo$" + binding.getRoleClassName();
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
			final ITeam team, final IBinding binding) {
		final Class<?> roleClass = ObjectTeamsTypeUtilities.getRoleImplType(team.getClass(),
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

	@SuppressWarnings("unused")
	private static final int loopStart(final IBoundBase2 base, final ITeam[] teams, final int index, final int[] ids,
			final int boundMethodId, final Object[] args) {
		return index;
	}

	private static MethodHandle LOOP_END(final String joinpointDesc) {
		return MethodHandles.insertArguments(LOOP_END, 0, joinpointDesc);
	}

	@SuppressWarnings("unused")
	private static final int loopEnd(final String joinpointDesc, final IBoundBase2 base, final ITeam[] teams,
			final int index, final int[] ids, final int boundMethodId, final Object[] args) {
		boolean end = false;
		int i = index;
		while (!end) {
			final IBinding binding = ObjectTeamsTypeUtilities.getBindingFromId(joinpointDesc, teams[i], ids[i]);
			if (binding.getCallinModifier() == CallinModifier.REPLACE) {
				end = true;
			} else {
				i++;
			}
		}
		return i;
	}

	private static MethodHandle LOOP_BODY_BEFORE(final MethodHandles.Lookup lookup, final String joinpointDesc) {
		return MethodHandles.insertArguments(LOOP_BODY_BEFORE, 0, lookup, joinpointDesc);
	}

	// TODO Lars: a before/after callin is treated differently than a replace callin
	@SuppressWarnings("unused")
	private static final MethodHandle loopBodyBefore(final MethodHandles.Lookup lookup, final String joinpointDesc,
			final MethodHandle result, final int currentIndex, final IBoundBase2 base, final ITeam[] teams,
			final int index, final int[] ids, final int boundMethodId, final Object[] args) {
		final IBinding binding = ObjectTeamsTypeUtilities.getBindingFromId(joinpointDesc, teams[currentIndex],
				ids[currentIndex]);
		// Find the role method that is going to be called
		// (Role, Args)Ret
		final MethodHandle rm = findRoleMethod(lookup, joinpointDesc, teams[currentIndex], binding);
		out("role function", rm.toString());
		// Find the lifting function to lift BaseClass to RoleClass
		// (Team, Base)Role
		final MethodHandle lift = lift(lookup, joinpointDesc, teams[currentIndex], binding, base.getClass());
		out("lift", lift.toString());
		final MethodHandle liftSpreader = lift.asSpreader(Object[].class, 2);
		out("liftSpreader", liftSpreader.toString());
		final MethodHandle liftToRole = MethodHandles.filterArguments(rm, 0, liftSpreader);
		out("liftToRole", liftToRole.toString());
		final MethodType collectedTypes = MethodType.methodType(void.class, teams[currentIndex].getClass(),
				base.getClass());
		final MethodHandle liftToRoleCollect = liftToRole.asCollector(Object[].class, 2).asType(collectedTypes);
		out("liftToRoleCollect", liftToRoleCollect.toString());
		// TODO Lars: check for arguments (trailing Object[])
		final MethodType swapType = MethodType.methodType(void.class, base.getClass(), teams[currentIndex].getClass());
		final MethodHandle swapped = MethodHandles.permuteArguments(liftToRoleCollect, swapType, 1, 0);
		out("swapped", swapped.toString());
		final MethodHandle unpackTeam = MethodHandles
				.insertArguments(MethodHandles.arrayElementGetter(ITeam[].class), 1, currentIndex)
				.asType(MethodType.methodType(teams[currentIndex].getClass(), ITeam[].class));
		out("unpackTeam", unpackTeam.toString());
		final MethodHandle unpacked = MethodHandles.filterArguments(swapped, 1, unpackTeam);
		out("unpacked", unpacked.toString());
		final MethodHandle dropped = MethodHandles.dropArguments(unpacked, 2, int.class, int[].class, int.class,
				Object[].class);
		out("dropped", dropped.toString());

		return dropped;
	}

	@Override
	public boolean canLinkType(final Class<?> type) {
		return IBoundBase2.class.isAssignableFrom(type);
	}

	@Override
	public GuardedInvocation getGuardedInvocation(final LinkRequest linkRequest, final LinkerServices linkerServices)
			throws Exception {
		final CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
		if (!(desc instanceof OTCallSiteDescriptor)) {
			throw new LinkageError("CallSiteDescriptor is no OTCallSiteDescriptor");
		}
		final OTCallSiteDescriptor otdesc = (OTCallSiteDescriptor) desc;
//		return CallinBootstrap.asTypeSafeReturn(getGuardedInvocation(linkRequest, otdesc), linkerServices, otdesc);
		return getGuardedInvocation(linkRequest, otdesc);
	}

	static GuardedInvocation getGuardedInvocation(final LinkRequest request, final OTCallSiteDescriptor desc) {
		final OTCallSiteDescriptor csd = (OTCallSiteDescriptor) desc;
		final int joinpointId = TeamManager.getJoinpointId(csd.getJoinpointDesc());
		final String joinpointDesc = csd.getJoinpointDesc();
		out("joinpointDesc", joinpointDesc);

		out("MT", desc.getMethodType().toMethodDescriptorString());

//		final MethodHandle result = MethodHandles.countedLoop(LOOP_START, LOOP_END(desc.getJoinpointDesc()), null,
//				LOOP_BODY_BEFORE(desc.getLookup(), desc.getJoinpointDesc()));

//		return new GuardedInvocation(result);

		boolean stopSearch = false;

		MethodHandle beforeComposition = null;
		MethodHandle replace = null;

		CallSiteContext ctx = CallSiteContext.contexts.get(joinpointDesc);
		for (ITeam team : ctx) {

			final int callinId = ctx.nextCallinId();

			final IBinding binding = ObjectTeamsTypeUtilities.getBindingFromId(joinpointDesc, team, callinId);

			out("binding", binding.toString());

			switch (binding.getCallinModifier()) {
			case BEFORE:
				if (beforeComposition == null) {
					beforeComposition = handleBefore(desc, team, binding, ctx);
				} else {
					beforeComposition = MethodHandles.foldArguments(beforeComposition,
							handleBefore(desc, team, binding, ctx));
				}
				break;
			case AFTER:

				break;

			case REPLACE:
				replace = handleReplace(desc, team, binding, ctx);
				stopSearch = true;
				break;
			}

			if (stopSearch) {
				break;
			}

		}

		MethodHandle result = null;

		result = (replace == null) ? handleOrig(desc, ctx) : replace;
		if (beforeComposition != null) {
			result = MethodHandles.foldArguments(result, beforeComposition);
		}

		return new GuardedInvocation(result);
	}

	private static MethodHandle handleOrig(OTCallSiteDescriptor desc, CallSiteContext ctx) {
		final MethodHandle orig = ObjectTeamsTypeUtilities.findOrig(desc.getLookup(), ctx.baseClass);
		final MethodHandle result = MethodHandles.dropArguments(orig, 1, ITeam[].class, int.class, int[].class);
		return result;
	}

	private static MethodHandle handleReplace(OTCallSiteDescriptor desc, ITeam team1, IBinding binding,
			CallSiteContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	private static MethodHandle handleBefore(final OTCallSiteDescriptor desc, final ITeam team, final IBinding binding,
			final CallSiteContext ctx) {
		final Class<?> base = ObjectTeamsTypeUtilities.getBaseClass(binding.getBoundClass());
		// Find the role method that is going to be called
		// (Role, Args)Ret
		final MethodHandle rm = findRoleMethod(desc.getLookup(), desc.getJoinpointDesc(), team, binding);
		out("role function", rm.toString());
		// Find the lifting function to lift BaseClass to RoleClass
		// (Team, Base)Role
		final MethodHandle lift = lift(desc.getLookup(), desc.getJoinpointDesc(), team, binding, base);
		out("lift", lift.toString());
		final MethodHandle liftSpreader = lift.asSpreader(Object[].class, 2);
		out("liftSpreader", liftSpreader.toString());
		final MethodHandle liftToRole = MethodHandles.filterArguments(rm, 0, liftSpreader);
		out("liftToRole", liftToRole.toString());
		final MethodType collectedTypes = MethodType.methodType(void.class, team.getClass(), base);
		final MethodHandle liftToRoleCollect = liftToRole.asCollector(Object[].class, 2).asType(collectedTypes);
		out("liftToRoleCollect", liftToRoleCollect.toString());
		// TODO Lars: check for arguments (trailing Object[])
		final MethodType swapType = MethodType.methodType(void.class, base, team.getClass());
		final MethodHandle swapped = MethodHandles.permuteArguments(liftToRoleCollect, swapType, 1, 0);
		out("swapped", swapped.toString());
		final MethodHandle unpackTeam = MethodHandles
				.insertArguments(MethodHandles.arrayElementGetter(ITeam[].class), 1, ctx.getIndex())
				.asType(MethodType.methodType(team.getClass(), ITeam[].class));
		out("unpackTeam", unpackTeam.toString());
		final MethodHandle unpacked = MethodHandles.filterArguments(swapped, 1, unpackTeam);
		out("unpacked", unpacked.toString());
		final MethodHandle dropped = MethodHandles.dropArguments(unpacked, 2, int.class, int[].class, int.class,
				Object[].class);
		out("dropped", dropped.toString());
		return dropped;
	}

	private static void out(final String name, final String value) {
		final String format = "%-40s%s%n";
		System.out.printf(format, name, value);
	}

}
