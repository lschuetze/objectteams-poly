package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

import org.eclipse.objectteams.otredyn.runtime.IBinding;
import org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util.OTGuardUtils;
import org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util.OTLinkerUtils;
import org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util.OTTypeUtils;
import org.objectteams.IBoundBase2;
import org.objectteams.ITeam;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.objectteams.Team;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.dynalink.linker.support.Guards;
import jdk.dynalink.linker.support.Lookup;

public final class CallinLinker implements TypeBasedGuardingDynamicLinker {

//	private static Logger logger = LoggerFactory.getLogger(CallinLinker.class);
	private static final boolean NO_DEG = System.getProperty("otdyn.nodeg") != null;

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
		// (ITeam,IBoundBase2,ITeam[],int,int[],int,Object[] [,Object[],int])Object
		final MethodHandle otMethod = Lookup.PUBLIC.findVirtual(ITeam.class, otMethodName, otMethodType);
		MethodType movedTeamAndIdxType = MethodType.methodType(Object.class, ITeam.class, ITeam[].class, int.class, IBoundBase2.class);
		movedTeamAndIdxType = movedTeamAndIdxType.appendParameterTypes(otMethodType.dropParameterTypes(0, 3).parameterList());
		int[] reorder = isCallAllBindings ? new int[] {0, 3, 1, 2, 4, 5, 6} : new int[] {0, 3, 1, 2, 4, 5, 6, 7, 8};
		// (ITeam,ITeam[],int,IBoundBase2,int[],int,Object[] [,Object[],int])Object
		final MethodHandle movedTeamAndIdx = MethodHandles.permuteArguments(otMethod, movedTeamAndIdxType, reorder);
		final MethodHandle teamGetter = MethodHandles.arrayElementGetter(ITeam[].class);
		// (ITeam[],int,IBoundBase2,int[],int,Object[] [,Object[],int])Object
		final MethodHandle boundTeamHandle = MethodHandles.foldArguments(movedTeamAndIdx, 0, teamGetter);
		reorder = isCallAllBindings ? new int[] {1, 2, 0, 3, 4, 5} : new int[] {1, 2, 0, 3, 4, 5, 6, 7};
		// (IBoundBase2,ITeam[],int,int[],int,Object[] [,Object[],int])Object
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
		final MethodHandle lift = OTTypeUtils.findVirtual(lookup, team.getClass(), liftingMethod,
				MethodType.methodType(
						OTTypeUtils.getRoleItfType(team.getClass(), binding.getRoleClassName()),
						baseClass));

		// Lifting works on interfaces; cast the return type to the implementation type
		final MethodHandle adaptedLift = lift.asType(MethodType.methodType(
				OTTypeUtils.getRoleImplType(team.getClass(), binding.getRoleClassName()), team.getClass(),
				baseClass));

		return adaptedLift;
	}

	private static MethodHandle findRoleMethod(final MethodHandles.Lookup lookup, final String joinpointDesc,
			final ITeam team, final IBinding binding) {
		final Class<?> roleClass = OTTypeUtils.getRoleImplType(team.getClass(),
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
		if (!NO_DEG && linkRequest.isCallSiteUnstable()) {
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
		MethodHandle beforeComposition = null, replace = null, afterComposition = null;
		final Object[] stack = request.getArguments();
		final Class<?> baseClass = stack[0].getClass();
		final ITeam[] teams = (ITeam[]) stack[1];
		final int startingIndex;
		final int[] callinIds = (int[]) stack[3];
		
		if(teams == null) {
			MethodHandle guard = Guards.isNull();
			guard = MethodHandles.dropArguments(guard, 0, IBoundBase2.class);
			return new GuardedInvocation(handleOrig(desc, baseClass), guard);
		}
		
		int indexIncrement;
		boolean stopSearch = false;
		// If we are in a callNext the index must be increased by 1
		// as it still points to the last team.
		// The startingIndex is used to create the guard and must also
		// be incremented by 1.
		if(desc.isCallNext()) {
			indexIncrement = 1;
			startingIndex = (int) stack[2] + 1;
		} else {
			indexIncrement = 0;
			startingIndex = (int) stack[2];
		}
		int index = startingIndex;
		
		while (!stopSearch && index < teams.length) {
			final ITeam team = teams[index];
			final int callinId = callinIds[index];
			final IBinding binding = OTTypeUtils.getBindingFromId(joinpointDesc, team, callinId);
			switch (binding.getCallinModifier()) {
				case BEFORE:
					final MethodHandle handleBefore = MethodHandles.filterArguments(
							handleBeforeAndAfter(desc, team, binding, indexIncrement), 2, OTLinkerUtils.incrementInt(indexIncrement));
					beforeComposition = beforeComposition == null ? handleBefore
							: MethodHandles.foldArguments(handleBefore, beforeComposition);
					break;
				case AFTER:
					final MethodHandle handleAfter = MethodHandles.filterArguments(
							handleBeforeAndAfter(desc, team, binding, indexIncrement), 2, OTLinkerUtils.incrementInt(indexIncrement));
					afterComposition = afterComposition == null ? handleAfter
							: MethodHandles.foldArguments(afterComposition, handleAfter);
					break;
				case REPLACE:
					replace = MethodHandles.filterArguments(
							handleReplace(desc, team, binding, indexIncrement), 2, OTLinkerUtils.incrementInt(indexIncrement));
					stopSearch = true;
					break;
			}
			indexIncrement++;
			index++;
		}
		
		Class<?>[] guardedStack = OTGuardUtils.constructTestStack(teams, startingIndex, index);
		MethodHandle guard = OTGuards.buildGuard(guardedStack);
		guard = MethodHandles.dropArguments(guard, 0, IBoundBase2.class);
		
		// Check if we are in a callNext then the index must also be increased
		// When checking the guard. Otherwise we will check the old index.
		if(desc.isCallNext()) {
			guard = MethodHandles.filterArguments(guard, 2, OTLinkerUtils.incrementInt(1));
		}
		 
		MethodHandle result = (replace == null) ? handleOrig(desc, baseClass) : replace;
		if (afterComposition != null) {
			final MethodHandle returnWrapper = MethodHandles.identity(Object.class);
			final MethodHandle returnWrapperDropped = MethodHandles.dropArguments(returnWrapper, 1, afterComposition.type().parameterList());
			afterComposition = MethodHandles.foldArguments(returnWrapperDropped, 1, afterComposition);
			result = MethodHandles.foldArguments(afterComposition, result);
		}
		if (beforeComposition != null) {
			result = MethodHandles.foldArguments(result, beforeComposition);
		}
//		logger.debug("========== END getGuardedInvocation ==========");
		return new GuardedInvocation(result, guard);
	}

	private static MethodHandle handleOrig(OTCallSiteDescriptor desc, Class<?> baseClass) {
//		logger.debug("========== BEGIN handleOrig ==========");
		final MethodHandle orig = OTTypeUtils.findOrig(desc.getLookup(), baseClass);
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
	
	private static final int[] REORDER_CALLALLBINDINGS = new int[] { 0, 1, 0, 2, 3, 4, 5, 6, 6 };
	private static final int[] REORDER_CALLNEXT = new int[] { 0, 1, 0, 2, 3, 4, 5, 7, 7 };
	
	private static MethodHandle handleReplace(final OTCallSiteDescriptor desc, final ITeam team, final IBinding binding, final int indexIncrement) {
//		logger.debug("========== BEGIN handleReplace ==========");
		final Class<?> base = OTTypeUtils.getBaseClass(binding.getBoundClass());
		// MethodHandle(__OT__Role,IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle rm = findRoleMethod(desc.getLookup(), desc.getJoinpointDesc(), team, binding);
//		logger.trace("role function \t\t{}", rm.toString());
		final boolean multipleArguments = rm.type().parameterCount() > 7;
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
		MethodType collectedTypes = MethodType.methodType(Object.class, base, team.getClass(), base);
		collectedTypes = collectedTypes.appendParameterTypes(rm.type().dropParameterTypes(0, 2).parameterList());
		// MethodHandle(Base,Team,IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle liftToRoleCollect = liftToRole.asCollector(0, Object[].class, 2).asType(collectedTypes);
//		logger.trace("liftToRoleCollect \t\t{}", liftToRoleCollect.toString());
		// MethodHandle(Base,Team,IBoundBase2,ITeam[],int,int[],int,Object[],Object[])Object
		final MethodHandle unpackedArgs;
		if (multipleArguments) {
			unpackedArgs = liftToRoleCollect.asSpreader(8, Object[].class, rm.type().parameterCount() - 7);
		} else {
			unpackedArgs = MethodHandles.dropArguments(liftToRoleCollect, 8, Object[].class);
		}
//		logger.trace("expanded \t\t{}", expanded.toString());
		MethodType doubleBaseAndArgsType = MethodType.methodType(Object.class, base, team.getClass(), ITeam[].class,
				int.class, int[].class, int.class, Object[].class);
		final int[] reorder;
		if (desc.isCallAllBindings()) {
			reorder = REORDER_CALLALLBINDINGS;
		} else {
			reorder = REORDER_CALLNEXT;
			doubleBaseAndArgsType = doubleBaseAndArgsType.appendParameterTypes(Object[].class, int.class);
		}
		// MethodHandle(Base,Team,ITeam[],int,int[],int,Object[])Object
		final MethodHandle doubleBaseAndArgs = MethodHandles.permuteArguments(unpackedArgs, doubleBaseAndArgsType, reorder);
//		logger.trace("doubleBaseAndTeam \t\t{}", doubleBaseAndTeam.toString());
		final MethodHandle teamGetter = MethodHandles.arrayElementGetter(ITeam[].class)
				.asType(MethodType.methodType(team.getClass(), ITeam[].class, int.class));
//		logger.trace("unpackTeam \t\t{}", unpackTeam.toString());
		// MethodHandle(Base,ITeam[],int,int[],int,Object[])Object
		final MethodHandle unpackedTeams = MethodHandles.foldArguments(doubleBaseAndArgs, 1, teamGetter);
//		logger.trace("unpacked \t\t{}", unpacked.toString());
		return unpackedTeams;
	}

	private static MethodHandle handleBeforeAndAfter(final OTCallSiteDescriptor desc, final ITeam team, final IBinding binding, final int relativeIndex) {
//		logger.debug("========== BEGIN handleBefore ==========");
		final Class<?> base = OTTypeUtils.getBaseClass(binding.getBoundClass());
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
		final MethodHandle teamGetter = MethodHandles.arrayElementGetter(ITeam[].class)
				.asType(MethodType.methodType(team.getClass(), ITeam[].class, int.class));
//		logger.trace("unpackTeam \t\t{}", unpackTeam.toString());
		final MethodHandle unpackedTeams = MethodHandles.foldArguments(dropped, 1, teamGetter);
//		logger.trace("unpacked \t\t{}", unpacked.toString());
		if (desc.isCallAllBindings()) {
//			logger.debug("========== END handleBefore ==========");
			return unpackedTeams;
		} else {
			final MethodHandle dropObjectParams = MethodHandles.dropArguments(unpackedTeams, 5, Object[].class);
//			logger.trace("dropObjectParams \t\t{}", dropObjectParams.toString());
			final MethodHandle dropBaseId = MethodHandles.dropArguments(dropObjectParams, 7, int.class);
//			logger.trace("dropBaseId \t\t{}", dropBaseId.toString());
//			logger.debug("========== END handleBefore ==========");
			return dropBaseId;
		}
	}

}
