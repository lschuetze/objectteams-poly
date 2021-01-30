package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.util.Iterator;

import org.eclipse.objectteams.otredyn.runtime.IBinding;
import org.eclipse.objectteams.otredyn.runtime.TeamManager;
import org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util.ObjectTeamsTypeUtilities;
import org.objectteams.IBoundBase2;
import org.objectteams.ITeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.dynalink.linker.support.Guards;

public final class CallinLinker implements TypeBasedGuardingDynamicLinker {

	static Logger logger = LoggerFactory.getLogger(CallinLinker.class);

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
		if (linkRequest.isCallSiteUnstable()) {
			logger.info("-------- Callsite is unstable --------");
			final String joinpointDesc = otdesc.getJoinpointDesc();
			CallSiteContext ctx = CallSiteContext.contexts.get(joinpointDesc);
			final boolean isCallAllBindings = otdesc.isCallAllBindings();
			if (isCallAllBindings) {
				ctx.updateTeams();
			}
			Iterator<ITeam> iter = ctx.iterator();
			// callAllBindings? -> index = 0
			// callNext? -> index = index - 1
			if (iter.hasNext()) {
				ITeam team = iter.next();
				MethodHandle otMethod;
				MethodType otMethodType;
				String otMethodName;
				if (isCallAllBindings) {
					otMethodType = MethodType.methodType(Object.class, IBoundBase2.class,
							ITeam[].class,int.class ,int[].class, int.class, Object[].class);
					otMethodName = "_OT$callAllBindings";
				} else {
					otMethodType = MethodType.methodType(Object.class, IBoundBase2.class, ITeam[].class,
							int.class ,int[].class, int.class, Object[].class, Object[].class, int.class);
					otMethodName = "_OT$callNext";
				}
				otMethod = otdesc.getLookup()
						.findVirtual(team.getClass(), otMethodName, otMethodType)
						.bindTo(team);
				return new GuardedInvocation(otMethod);
			} else {
				MethodHandle orig = handleOrig(otdesc, ctx);
				final int joinpointId = TeamManager.getJoinpointId(otdesc.getJoinpointDesc());
				SwitchPoint sp = new SwitchPoint();
				TeamManager.registerSwitchPoint(joinpointId, sp);
				return new GuardedInvocation(orig, sp);
			}
		}
		return CallinBootstrap.asTypeSafeReturn(getGuardedInvocation(linkRequest, otdesc), linkerServices, otdesc);
	}

	static GuardedInvocation getGuardedInvocation(final LinkRequest request, final OTCallSiteDescriptor desc) {
		logger.debug("========== BEGIN getGuardedInvocation ==========");
		final String joinpointDesc = desc.getJoinpointDesc();
		boolean stopSearch = false;
		MethodHandle beforeComposition = null;
		MethodHandle replace = null;
		CallSiteContext ctx = CallSiteContext.contexts.get(joinpointDesc);
		if (desc.isCallAllBindings()) {
			ctx.updateTeams();
		}
		for (ITeam team : ctx) {
			logger.trace("Team \t\t{}", team.toString());
			final int callinId = ctx.nextCallinId();
			logger.trace("callinId \t\t{}", Integer.valueOf(callinId).toString());
			final IBinding binding = ObjectTeamsTypeUtilities.getBindingFromId(joinpointDesc, team, callinId);
			logger.trace("binding \t\t{}", binding.toString());
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
		logger.trace("result \t\t{}", result.toString());
		MethodHandle guard;
		ITeam[] teams = ctx.getTeams();
		if (teams == null) {
			// MethodHandle(Object)boolean
			guard = Guards.isNull();
		} else {
			Class<?>[] stack = new Class<?>[teams.length];
			for (int i = 0; i < stack.length; i++) {
				stack[i] = teams[i].getClass();
			}
			// MethodHandle(ITeam[])boolean
			guard = OTGuards.TEST_COMPOSITION.bindTo(stack);
		}
		// MethodHandle(IBoundBase2,ITeam[])boolean
		guard = MethodHandles.dropArguments(guard, 0, IBoundBase2.class);
		final MethodType resultType = result.type().changeReturnType(Boolean.TYPE);
		// MethodHandle(IBoundBase2,ITeam[],...)boolean
		guard = MethodHandles.dropArguments(guard, 2, resultType.dropParameterTypes(0, 2).parameterList());
		logger.debug("========== END getGuardedInvocation ==========");
		return new GuardedInvocation(result, guard);
	}

	private static MethodHandle handleOrig(OTCallSiteDescriptor desc, CallSiteContext ctx) {
		logger.debug("========== BEGIN handleOrig ==========");
		final MethodHandle orig = ObjectTeamsTypeUtilities.findOrig(desc.getLookup(), ctx.baseClass);
		logger.trace("orig \t\t{}", orig.toString());
		MethodHandle result = MethodHandles.dropArguments(orig, 1, ITeam[].class, int.class, int[].class);
		logger.trace("result \t\t{}", result.toString());
		if (desc.isCallNext()) {
			// might make the super call from last int
			result = MethodHandles.dropArguments(result, 5, Object[].class);
			result = MethodHandles.dropArguments(result, 7, int.class);
			logger.debug("result2 \t\t{}", result.toString());
		}
		logger.debug("========== END handleOrig ==========");
		return result;
	}

	// TODO Lars: variable argument lengths
	private static MethodHandle handleReplace(final OTCallSiteDescriptor desc, final ITeam team, final IBinding binding,
			final CallSiteContext ctx) {
		logger.debug("========== BEGIN handleReplace ==========");
		final Class<?> base = ObjectTeamsTypeUtilities.getBaseClass(binding.getBoundClass());
		// MethodHandle(__OT__Role,IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle rm = findRoleMethod(desc.getLookup(), desc.getJoinpointDesc(), team, binding);
		final boolean multipleArguments = rm.type().parameterCount() > 7;
		logger.trace("role function \t\t{}", rm.toString());
		// MethodHandle(Team,Base)__OT__Role
		final MethodHandle lift = lift(desc.getLookup(), desc.getJoinpointDesc(), team, binding, base);
		logger.trace("lift \t\t{}", lift.toString());
		// MethodHandle(Object[])__OT__Role
		final MethodHandle liftSpreader = lift.asSpreader(0, Object[].class, 2);
		logger.trace("liftSpreader \t\t{}", liftSpreader.toString());
		// MethodHandle(Object[],IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle liftToRole = MethodHandles.filterArguments(rm, 0, liftSpreader);
		logger.trace("liftToRole \t\t{}", liftToRole.toString());
		MethodType collectedTypes = MethodType.methodType(Object.class, team.getClass(), base);
		if (multipleArguments) {
			collectedTypes = collectedTypes.appendParameterTypes(rm.type().dropParameterTypes(0, 1).parameterList());
		}
		// MethodHandle(Team,Base,IBoundBase2,ITeam[],int,int[],int,Object[],args*)Object
		final MethodHandle liftToRoleCollect = liftToRole.asCollector(0, Object[].class, 2).asType(collectedTypes);
		logger.trace("liftToRoleCollect \t\t{}", liftToRoleCollect.toString());
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
			// MethodHandle(Base,Team,IBoundBase2,ITeam[],int,int[],int,Object[])Object
			swapped = MethodHandles.permuteArguments(liftToRoleCollect, swapType, 1, 0);
		}
		logger.trace("swapped \t\t{}", swapped.toString());
		// TODO Lars: A way without insertArgument of index?
		final MethodHandle unpackTeam = MethodHandles
				.insertArguments(MethodHandles.arrayElementGetter(ITeam[].class), 1, ctx.getIndex() - 1)
				.asType(MethodType.methodType(team.getClass(), ITeam[].class));
		logger.trace("unpackTeam \t\t{}", unpackTeam.toString());
		final MethodHandle unpacked = MethodHandles.filterArguments(swapped, 1, unpackTeam);
		logger.trace("unpacked \t\t{}", unpacked.toString());
		final MethodType unpackedType = MethodType.methodType(Object.class, base, ITeam[].class, base, ITeam[].class,
				int.class, int[].class, int.class, Object[].class, float.class);
		final MethodHandle unpackedTyped = unpacked.asType(unpackedType);
		logger.trace("unpackedTyped \t\t{}", unpackedTyped.toString());
		final MethodHandle expanded;
		if (multipleArguments) {
			expanded = unpackedTyped.asSpreader(8, Object[].class, rm.type().parameterCount() - 7);
		} else {
			expanded = unpackedTyped;
		}
		logger.trace("expanded \t\t{}", expanded.toString());
		final MethodType doubleBaseAndTeamType = MethodType.methodType(Object.class, base, ITeam[].class, int.class,
				int[].class, int.class, Object[].class);
		final MethodHandle doubleBaseAndTeam = MethodHandles.permuteArguments(expanded, doubleBaseAndTeamType, 0, 1, 0,
				1, 2, 3, 4, 5, 5);
		logger.trace("doubleBaseAndTeam \t\t{}", doubleBaseAndTeam.toString());
		if (desc.isCallAllBindings()) {
			logger.debug("========== END handleReplace ==========");
			return doubleBaseAndTeam;
		} else {
			final MethodHandle dropObjectParams = MethodHandles.dropArguments(doubleBaseAndTeam, 6, Object[].class);
			logger.trace("dropObjectParams \t\t{}", dropObjectParams.toString());
			final MethodHandle dropBaseId = MethodHandles.dropArguments(dropObjectParams, 7, int.class);
			logger.trace("dropBaseId \t\t{}", dropBaseId.toString());
			logger.debug("========== END handleReplace ==========");
			return dropBaseId;
		}
	}

	private static MethodHandle handleBefore(final OTCallSiteDescriptor desc, final ITeam team, final IBinding binding,
			final CallSiteContext ctx) {
		logger.debug("========== BEGIN handleBefore ==========");
		final Class<?> base = ObjectTeamsTypeUtilities.getBaseClass(binding.getBoundClass());
		final MethodHandle rm = findRoleMethod(desc.getLookup(), desc.getJoinpointDesc(), team, binding);
		final boolean multipleArguments = rm.type().parameterCount() > 1;
		logger.trace("role function \t\t{}", rm.toString());
		final MethodHandle lift = lift(desc.getLookup(), desc.getJoinpointDesc(), team, binding, base);
		logger.trace("lift \t\t{}", lift.toString());
		final MethodHandle liftSpreader = lift.asSpreader(0, Object[].class, 2);
		logger.trace("liftSpreader \t\t{}", liftSpreader.toString());
		final MethodHandle liftToRole = MethodHandles.filterArguments(rm, 0, liftSpreader);
		logger.trace("liftToRole \t\t{}", liftToRole.toString());
		MethodType collectedTypes = MethodType.methodType(void.class, team.getClass(), base);
		if (multipleArguments) {
			collectedTypes = collectedTypes.appendParameterTypes(rm.type().dropParameterTypes(0, 1).parameterList());
		}
		final MethodHandle liftToRoleCollect = liftToRole.asCollector(0, Object[].class, 2).asType(collectedTypes);
		logger.trace("liftToRoleCollect \t\t{}", liftToRoleCollect.toString());
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
		logger.trace("swapped \t\t{}", swapped.toString());
		final MethodHandle unpackTeam = MethodHandles
				.insertArguments(MethodHandles.arrayElementGetter(ITeam[].class), 1, ctx.getIndex() - 1)
				.asType(MethodType.methodType(team.getClass(), ITeam[].class));
		logger.trace("unpackTeam \t\t{}", unpackTeam.toString());
		final MethodHandle unpacked = MethodHandles.filterArguments(swapped, 1, unpackTeam);
		logger.trace("unpacked \t\t{}", unpacked.toString());
		final MethodHandle dropped;
		if (multipleArguments) {
			final MethodHandle unpackedArgs = unpacked.asSpreader(Object[].class, rm.type().parameterCount() - 1);
			dropped = MethodHandles.dropArguments(unpackedArgs, 2, int.class, int[].class, int.class);
		} else {
			dropped = MethodHandles.dropArguments(unpacked, 2, int.class, int[].class, int.class, Object[].class);
		}
		logger.trace("dropped \t\t{}", dropped.toString());
		if (desc.isCallAllBindings()) {
			logger.debug("========== END handleBefore ==========");
			return dropped;
		} else {
			final MethodHandle dropObjectParams = MethodHandles.dropArguments(dropped, 6, Object[].class);
			logger.trace("dropObjectParams \t\t{}", dropObjectParams.toString());
			final MethodHandle dropBaseId = MethodHandles.dropArguments(dropObjectParams, 7, int.class);
			logger.trace("dropBaseId \t\t{}", dropBaseId.toString());
			logger.debug("========== END handleBefore ==========");
			return dropBaseId;
		}
	}

}
