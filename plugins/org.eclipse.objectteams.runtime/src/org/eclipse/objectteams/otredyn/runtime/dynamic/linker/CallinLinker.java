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

public final class CallinLinker implements TypeBasedGuardingDynamicLinker {

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
		final String joinpointDesc = desc.getJoinpointDesc();
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
				beforeComposition = beforeComposition == null ? handleBefore(desc, team, binding, ctx)
						: MethodHandles.foldArguments(beforeComposition, handleBefore(desc, team, binding, ctx));
				break;
			case AFTER:
				// TODO Lars: Implement
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
		MethodHandle result = (replace == null) ? handleOrig(desc, ctx) : replace;
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

	// TODO Lars: variable argument lengths
	private static MethodHandle handleReplace(final OTCallSiteDescriptor desc, final ITeam team, final IBinding binding,
			final CallSiteContext ctx) {
		final Class<?> base = ObjectTeamsTypeUtilities.getBaseClass(binding.getBoundClass());
		// MethodHandle(__OT__Role,IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle rm = findRoleMethod(desc.getLookup(), desc.getJoinpointDesc(), team, binding);
		final boolean multipleArguments = rm.type().parameterCount() > 7;
		out("role function", rm.toString());
		// MethodHandle(Team,Base)__OT__Role
		final MethodHandle lift = lift(desc.getLookup(), desc.getJoinpointDesc(), team, binding, base);
		out("lift", lift.toString());
		// MethodHandle(Object[])__OT__Role
		final MethodHandle liftSpreader = lift.asSpreader(0, Object[].class, 2);
		out("liftSpreader", liftSpreader.toString());
		// MethodHandle(Object[],IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle liftToRole = MethodHandles.filterArguments(rm, 0, liftSpreader);
		out("liftToRole", liftToRole.toString());
		MethodType collectedTypes = MethodType.methodType(Object.class, team.getClass(), base);
		if (multipleArguments) {
			collectedTypes = collectedTypes.appendParameterTypes(rm.type().dropParameterTypes(0, 1).parameterList());
		}
		// MethodHandle(Team,Base,IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle liftToRoleCollect = liftToRole.asCollector(0, Object[].class, 2).asType(collectedTypes);
		out("liftToRoleCollect", liftToRoleCollect.toString());
		MethodType swapType = MethodType.methodType(Object.class, base, team.getClass());
		final MethodHandle swapped;
		if (multipleArguments) {
			swapType = swapType.appendParameterTypes(rm.type().dropParameterTypes(0, 1).parameterList());
			int[] reorder = new int[swapType.parameterCount()];
			reorder[0] = 1;
			reorder[1] = 0;
			for (int i = 2; i < reorder.length; i++) {
				reorder[i] = i;
			}
			// MethodHandle(Base,Team,IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
			swapped = MethodHandles.permuteArguments(liftToRoleCollect, swapType, reorder);
		} else {
			// // MethodHandle(Base,Team,IBoundBase2,ITeam[],int,int[],int,Object[])Object
			swapped = MethodHandles.permuteArguments(liftToRoleCollect, swapType, 1, 0);
		}
		out("swapped", swapped.toString());
		final MethodHandle unpackTeam = MethodHandles
				.insertArguments(MethodHandles.arrayElementGetter(ITeam[].class), 1, ctx.getIndex())
				.asType(MethodType.methodType(team.getClass(), ITeam[].class));
		out("unpackTeam", unpackTeam.toString());
		final MethodHandle unpacked = MethodHandles.filterArguments(swapped, 1, unpackTeam);
		out("unpacked", unpacked.toString());
		final MethodType unpackedType = MethodType.methodType(Object.class, base, ITeam[].class, base, ITeam[].class,
				int.class, int[].class, int.class, Object[].class, float.class);
		final MethodHandle unpackedTyped = unpacked.asType(unpackedType);
		out("unpackedTyped", unpackedTyped.toString());
		final MethodHandle expanded;
		if (multipleArguments) {
			expanded = unpackedTyped.asSpreader(8, Object[].class, rm.type().parameterCount() - 7);
		} else {
			expanded = unpackedTyped;
		}
		out("expanded", expanded.toString());
		final MethodType doubleBaseAndTeamType = MethodType.methodType(Object.class, base, ITeam[].class, int.class,
				int[].class, int.class, Object[].class);
		final MethodHandle doubleBaseAndTeam = MethodHandles.permuteArguments(expanded, doubleBaseAndTeamType, 0, 1, 0,
				1, 2, 3, 4, 5, 5);
		out("doubleBaseAndTeam", doubleBaseAndTeam.toString());
		return doubleBaseAndTeam;
	}

	private static MethodHandle handleBefore(final OTCallSiteDescriptor desc, final ITeam team, final IBinding binding,
			final CallSiteContext ctx) {
		final Class<?> base = ObjectTeamsTypeUtilities.getBaseClass(binding.getBoundClass());
		final MethodHandle rm = findRoleMethod(desc.getLookup(), desc.getJoinpointDesc(), team, binding);
		final boolean multipleArguments = rm.type().parameterCount() > 1;
		out("role function", rm.toString());
		final MethodHandle lift = lift(desc.getLookup(), desc.getJoinpointDesc(), team, binding, base);
		out("lift", lift.toString());
		final MethodHandle liftSpreader = lift.asSpreader(0, Object[].class, 2);
		out("liftSpreader", liftSpreader.toString());
		final MethodHandle liftToRole = MethodHandles.filterArguments(rm, 0, liftSpreader);
		out("liftToRole", liftToRole.toString());
		MethodType collectedTypes = MethodType.methodType(void.class, team.getClass(), base);
		if (multipleArguments) {
			collectedTypes = collectedTypes.appendParameterTypes(rm.type().dropParameterTypes(0, 1).parameterList());
		}
		final MethodHandle liftToRoleCollect = liftToRole.asCollector(0, Object[].class, 2).asType(collectedTypes);
		out("liftToRoleCollect", liftToRoleCollect.toString());
		MethodType swapType = MethodType.methodType(void.class, base, team.getClass());
		final MethodHandle swapped;
		if (multipleArguments) {
			swapType = swapType.appendParameterTypes(rm.type().dropParameterTypes(0, 1).parameterList());
			final int[] reorder = new int[swapType.parameterCount()];
			reorder[0] = 1;
			reorder[1] = 0;
			for (int i = 2; i < reorder.length; i++) {
				reorder[i] = i;
			}
			swapped = MethodHandles.permuteArguments(liftToRoleCollect, swapType, reorder);
		} else {
			swapped = MethodHandles.permuteArguments(liftToRoleCollect, swapType, 1, 0);
		}
		out("swapped", swapped.toString());
		final MethodHandle unpackTeam = MethodHandles
				.insertArguments(MethodHandles.arrayElementGetter(ITeam[].class), 1, ctx.getIndex())
				.asType(MethodType.methodType(team.getClass(), ITeam[].class));
		out("unpackTeam", unpackTeam.toString());
		final MethodHandle unpacked = MethodHandles.filterArguments(swapped, 1, unpackTeam);
		out("unpacked", unpacked.toString());
		final MethodHandle dropped;
		if (multipleArguments) {
			final MethodHandle unpackedArgs = unpacked.asSpreader(Object[].class, rm.type().parameterCount() - 1);
			dropped = MethodHandles.dropArguments(unpackedArgs, 2, int.class, int[].class, int.class);
		} else {
			dropped = MethodHandles.dropArguments(unpacked, 2, int.class, int[].class, int.class, Object[].class);
		}
		out("dropped", dropped.toString());
		return dropped;
	}

	private static void out(final String name, final String value) {
		final String format = "%-40s%s%n";
		System.out.printf(format, name, value);
	}

}
