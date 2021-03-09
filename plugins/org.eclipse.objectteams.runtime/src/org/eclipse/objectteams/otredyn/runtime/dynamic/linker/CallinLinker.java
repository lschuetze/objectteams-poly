package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.eclipse.objectteams.otredyn.runtime.IBinding;
import org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util.ObjectTeamsTypeUtilities;
import org.objectteams.IBoundBase2;
import org.objectteams.ITeam;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.dynalink.linker.support.Guards;
import jdk.dynalink.linker.support.Lookup;

public final class CallinLinker implements TypeBasedGuardingDynamicLinker {

//	static Logger logger = LoggerFactory.getLogger(CallinLinker.class);

	private static final MethodHandle INCREMENT = Lookup.PUBLIC.findStatic(Math.class, "addExact",
			MethodType.methodType(int.class, int.class, int.class));
	private static final MethodHandle CALLORIG = Lookup.PUBLIC.findVirtual(IBoundBase2.class, "_OT$callOrig",
			MethodType.methodType(Object.class, int.class, Object[].class));
	private static final MethodHandle OT_CALL_ALL_BINDINGS = getOriginalDispatch(true);
	private static final MethodHandle OT_CALL_NEXT = getOriginalDispatch(false);

	private static MethodHandle getOriginalDispatch(boolean isCallAllBindings) {
		MethodType otMethodType;
		String otMethodName;
		if (isCallAllBindings) {
			otMethodName = "_OT$callAllBindings";
			otMethodType = MethodType.methodType(Object.class, IBoundBase2.class, ITeam[].class,
					int.class ,int[].class, int.class, Object[].class);
		} else {
			otMethodName = "_OT$callNext";
			otMethodType = MethodType.methodType(Object.class, IBoundBase2.class, ITeam[].class,
					int.class, int[].class, int.class, Object[].class, Object[].class, int.class);
		}
		// (ITeam,IBoundBase2,ITeam[],int,int[],int,Object[]...)Object
		final MethodHandle otMethod = Lookup.PUBLIC.findVirtual(ITeam.class, otMethodName, otMethodType);
		MethodType movedTeamAndIdxType = MethodType.methodType(Object.class, ITeam.class, ITeam[].class, int.class, IBoundBase2.class);
		movedTeamAndIdxType = movedTeamAndIdxType.appendParameterTypes(otMethodType.dropParameterTypes(0, 3).parameterList());
		int[] reorder = isCallAllBindings ? new int[] {0, 3, 1, 2, 4, 5, 6} : new int[] {0, 3, 1, 2, 4, 5, 6, 7, 8};
		// (ITeam,ITeam[],int,IBoundBase2,int[],int,Object[]...)Object
		final MethodHandle movedTeamAndIdx = MethodHandles.permuteArguments(otMethod, movedTeamAndIdxType, reorder);
		final MethodHandle unpackTeam = MethodHandles.arrayElementGetter(ITeam[].class);
		final MethodHandle boundTeamHandle = MethodHandles.foldArguments(movedTeamAndIdx, 0, unpackTeam);
		reorder = isCallAllBindings ? new int[] {1, 2, 0, 3, 4, 5} : new int[] {1, 2, 0, 3, 4, 5, 6, 7};
		// (IBoundBase2,ITeam[],int,int[],int,Object[]...)Object
		final MethodHandle movedTeamAndIdx2 = MethodHandles.permuteArguments(boundTeamHandle, otMethodType, reorder);
		// Call orig if team array is null
		MethodHandle guard = Guards.isNotNull().asType(MethodType.methodType(boolean.class, ITeam[].class));
		guard = MethodHandles.dropArguments(guard, 0, IBoundBase2.class);
		MethodHandle fallback = MethodHandles.dropArguments(CALLORIG, 1, ITeam[].class, int.class, int[].class);
		if (!isCallAllBindings) {
			fallback = MethodHandles.dropArguments(fallback, 6, Object[].class, int.class);
		}
		final MethodHandle result = MethodHandles.guardWithTest(guard, movedTeamAndIdx2, fallback);
		return result;
	}

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
//		logger.info("---- Relink ----");
		final CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
		if (!(desc instanceof OTCallSiteDescriptor)) {
			throw new LinkageError("CallSiteDescriptor is no OTCallSiteDescriptor");
		}
		final OTCallSiteDescriptor otdesc = (OTCallSiteDescriptor) desc;
		if (linkRequest.isCallSiteUnstable()) {
//			logger.info("-------- Callsite is unstable --------");
			if (otdesc.isCallAllBindings()) {
				return new GuardedInvocation(OT_CALL_ALL_BINDINGS);
			} else {
				return new GuardedInvocation(OT_CALL_NEXT);
			}
		}
		return CallinBootstrap.asTypeSafeReturn(getGuardedInvocation(linkRequest, otdesc), linkerServices, otdesc);
	}

	static GuardedInvocation getGuardedInvocation(final LinkRequest request, final OTCallSiteDescriptor desc) {
//		logger.debug("========== BEGIN getGuardedInvocation ==========");
		final String joinpointDesc = desc.getJoinpointDesc();
		boolean stopSearch = false;
		MethodHandle beforeComposition = null;
		MethodHandle replace = null;
		final Object[] stack = request.getArguments();
		final ITeam[] teams = (ITeam[]) stack[1];
		final int oldIndex = (int) stack[2];
		int index = oldIndex;
		final int[] callinIds = (int[]) stack[3];
		final Class<?> baseClass = request.getReceiver().getClass();
		MethodHandle guard;
		if (teams != null) {
			if (desc.isCallNext()) index++;
			while (!stopSearch && index < teams.length) {
				final ITeam team = teams[index];
//				logger.trace("Team \t\t{}", team.toString());
				final int callinId = callinIds[index];
//				logger.trace("callinId \t\t{}", Integer.valueOf(callinId).toString());
				final IBinding binding = ObjectTeamsTypeUtilities.getBindingFromId(joinpointDesc, team, callinId);
//				logger.trace("binding \t\t{}", binding.toString());
				int relativeIndex = index - oldIndex;
				final MethodHandle incrementor = MethodHandles.insertArguments(INCREMENT, 0, relativeIndex);
				switch (binding.getCallinModifier()) {
					case BEFORE:
						beforeComposition = beforeComposition == null ? handleBefore(desc, team, binding)
								: MethodHandles.foldArguments(beforeComposition, handleBefore(desc, team, binding));
						beforeComposition = MethodHandles.filterArguments(beforeComposition, 2, incrementor);
						index++;
						break;
					case AFTER:
						// TODO Lars: Implement
						break;
					case REPLACE:
						replace = handleReplace(desc, team, binding);
						replace = MethodHandles.filterArguments(replace, 2, incrementor);
						stopSearch = true;
						break;
				}
			}
			final int testStackLength = (desc.isCallNext()) ? teams.length - oldIndex : teams.length;
			Class<?>[] teamClasses = new Class<?>[testStackLength];
			for (int j = 0, i = oldIndex; i < teams.length; i++, j++) {
				teamClasses[j] = teams[i].getClass();
			}
			guard = OTGuards.TEST_COMPOSITION_AND_INDEX.bindTo(teamClasses);
		} else {
			guard = Guards.isNull();
		}
		guard = MethodHandles.dropArguments(guard, 0, IBoundBase2.class);
		MethodHandle result = (replace == null) ? handleOrig(desc, baseClass) : replace;
		if (beforeComposition != null) {
			result = MethodHandles.foldArguments(result, beforeComposition);
		}
//		logger.trace("result \t\t{}", result.toString());
//		logger.debug("========== END getGuardedInvocation ==========");
		return new GuardedInvocation(result, guard);
	}

	private static MethodHandle handleOrig(OTCallSiteDescriptor desc, Class<?> baseClass) {
//		logger.debug("========== BEGIN handleOrig ==========");
		final MethodHandle orig = ObjectTeamsTypeUtilities.findOrig(desc.getLookup(), baseClass);
//		logger.trace("orig \t\t{}", orig.toString());
		MethodHandle result = MethodHandles.dropArguments(orig, 1, ITeam[].class, int.class, int[].class);
//		logger.trace("result \t\t{}", result.toString());
		if (desc.isCallNext()) {
			// might make the super call from last int
			result = MethodHandles.dropArguments(result, 5, Object[].class);
			result = MethodHandles.dropArguments(result, 7, int.class);
//			logger.debug("result2 \t\t{}", result.toString());
		}
//		logger.debug("========== END handleOrig ==========");
		return result;
	}

	private static MethodHandle handleReplace(final OTCallSiteDescriptor desc, final ITeam team, final IBinding binding) {
//		logger.debug("========== BEGIN handleReplace ==========");
		final Class<?> base = ObjectTeamsTypeUtilities.getBaseClass(binding.getBoundClass());
		// MethodHandle(__OT__Role,IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle rm = findRoleMethod(desc.getLookup(), desc.getJoinpointDesc(), team, binding);
		final boolean multipleArguments = rm.type().parameterCount() > 7;
//		logger.trace("role function \t\t{}", rm.toString());
		// MethodHandle(Team,Base)__OT__Role
		final MethodHandle lift = lift(desc.getLookup(), desc.getJoinpointDesc(), team, binding, base);
//		logger.trace("lift \t\t{}", lift.toString());
		MethodType swapType = MethodType.methodType(lift.type().returnType(), base, team.getClass());
		// MethodHandle(Base,Team)__OT__Role
		final MethodHandle liftSwapped = MethodHandles.permuteArguments(lift, swapType, 1, 0);
//		logger.trace("liftSwapped \t\t{}", liftSwapped.toString());
		// MethodHandle(Object[])__OT__Role
		final MethodHandle liftSpreader = liftSwapped.asSpreader(0, Object[].class, 2);
//		logger.trace("liftSpreader \t\t{}", liftSpreader.toString());
		// MethodHandle(Object[],IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle liftToRole = MethodHandles.filterArguments(rm, 0, liftSpreader);
//		logger.trace("liftToRole \t\t{}", liftToRole.toString());
		MethodType collectedTypes = MethodType.methodType(Object.class, base, team.getClass());
		collectedTypes = collectedTypes.appendParameterTypes(rm.type().dropParameterTypes(0, 1).parameterList());
		// MethodHandle(Base,Team,IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle liftToRoleCollect = liftToRole.asCollector(0, Object[].class, 2).asType(collectedTypes);
//		logger.trace("liftToRoleCollect \t\t{}", liftToRoleCollect.toString());
		final MethodHandle expanded;
		if (multipleArguments) {
			expanded = liftToRoleCollect.asSpreader(8, Object[].class, rm.type().parameterCount() - 7);
		} else {
			expanded = MethodHandles.dropArguments(liftToRoleCollect, 8, Object[].class);
		}
		// MethodHandle(Base,Team,IBoundBase2,ITeam[],int,int[],int,Object[],Object[])Object
//		logger.trace("expanded \t\t{}", expanded.toString());
		final MethodType expandedType = MethodType.methodType(Object.class, base, team.getClass(), base, ITeam[].class,
				int.class, int[].class, int.class, Object[].class, Object[].class);
		// MethodHandle(Base,Team,Base,ITeam[],int,int[],int,Object[],Object[])Object
		final MethodHandle expandedTyped = expanded.asType(expandedType);
//		logger.trace("expandedTyped \t\t{}", expandedTyped.toString());
		MethodType doubleBaseAndTeamType = MethodType.methodType(Object.class, base, team.getClass(), ITeam[].class,
				int.class, int[].class, int.class, Object[].class);
		int[] reorder;
		if (desc.isCallAllBindings()) {
			reorder = new int[] { 0, 1, 0, 2, 3, 4, 5, 6, 6 };
		} else {
			reorder = new int[] { 0, 1, 0, 2, 3, 4, 5, 7, 7 };
			doubleBaseAndTeamType = doubleBaseAndTeamType.appendParameterTypes(Object[].class, int.class);
		}
		// MethodHandle(Base,Team,ITeam[],int,int[],int,Object[])Object
		final MethodHandle doubleBaseAndTeam = MethodHandles.permuteArguments(expandedTyped, doubleBaseAndTeamType, reorder);
//		logger.trace("doubleBaseAndTeam \t\t{}", doubleBaseAndTeam.toString());
		final MethodHandle unpackTeam = MethodHandles.arrayElementGetter(ITeam[].class)
				.asType(MethodType.methodType(team.getClass(), ITeam[].class, int.class));
//		logger.trace("unpackTeam \t\t{}", unpackTeam.toString());
		// MethodHandle(Base,ITeam[],int,int[],int,Object[])Object
		final MethodHandle unpacked = MethodHandles.foldArguments(doubleBaseAndTeam, 1, unpackTeam);
//		logger.trace("unpacked \t\t{}", unpacked.toString());
		return unpacked;
	}

	private static MethodHandle handleBefore(final OTCallSiteDescriptor desc, final ITeam team, final IBinding binding) {
//		logger.debug("========== BEGIN handleBefore ==========");
		final Class<?> base = ObjectTeamsTypeUtilities.getBaseClass(binding.getBoundClass());
		final MethodHandle rm = findRoleMethod(desc.getLookup(), desc.getJoinpointDesc(), team, binding);
		final boolean multipleArguments = rm.type().parameterCount() > 1;
//		logger.trace("role function \t\t{}", rm.toString());
		final MethodHandle lift = lift(desc.getLookup(), desc.getJoinpointDesc(), team, binding, base);
//		logger.trace("lift \t\t{}", lift.toString());
		MethodType swapType = MethodType.methodType(lift.type().returnType(), base, team.getClass());
		final MethodHandle liftSwapped = MethodHandles.permuteArguments(lift, swapType, 1, 0);
//		logger.trace("liftSwapped \t\t{}", liftSwapped.toString());
		final MethodHandle liftSpreader = liftSwapped.asSpreader(0, Object[].class, 2);
//		logger.trace("liftSpreader \t\t{}", liftSpreader.toString());
		final MethodHandle liftToRole = MethodHandles.filterArguments(rm, 0, liftSpreader);
//		logger.trace("liftToRole \t\t{}", liftToRole.toString());
		MethodType collectedTypes = MethodType.methodType(void.class, base, team.getClass());
		if (multipleArguments) {
			collectedTypes = collectedTypes.appendParameterTypes(rm.type().dropParameterTypes(0, 1).parameterList());
		}
		final MethodHandle liftToRoleCollect = liftToRole.asCollector(0, Object[].class, 2).asType(collectedTypes);
//		logger.trace("liftToRoleCollect \t\t{}", liftToRoleCollect.toString());
		final MethodHandle dropped;
		if (multipleArguments) {
			final MethodHandle unpackedArgs = liftToRoleCollect.asSpreader(Object[].class, rm.type().parameterCount() - 1);
			dropped = MethodHandles.dropArguments(unpackedArgs, 2, ITeam[].class, int.class, int[].class, int.class);
		} else {
			dropped = MethodHandles.dropArguments(liftToRoleCollect, 2, ITeam[].class, int.class, int[].class, int.class, Object[].class);
		}
//		logger.trace("dropped \t\t{}", dropped.toString());
		final MethodHandle unpackTeam = MethodHandles.arrayElementGetter(ITeam[].class)
				.asType(MethodType.methodType(team.getClass(), ITeam[].class, int.class));
//		logger.trace("unpackTeam \t\t{}", unpackTeam.toString());
		final MethodHandle unpacked = MethodHandles.foldArguments(dropped, 1, unpackTeam);
//		logger.trace("unpacked \t\t{}", unpacked.toString());
		if (desc.isCallAllBindings()) {
//			logger.debug("========== END handleBefore ==========");
			return unpacked;
		} else {
			final MethodHandle dropObjectParams = MethodHandles.dropArguments(unpacked, 6, Object[].class);
//			logger.trace("dropObjectParams \t\t{}", dropObjectParams.toString());
			final MethodHandle dropBaseId = MethodHandles.dropArguments(dropObjectParams, 7, int.class);
//			logger.trace("dropBaseId \t\t{}", dropBaseId.toString());
//			logger.debug("========== END handleBefore ==========");
			return dropBaseId;
		}
	}

}
