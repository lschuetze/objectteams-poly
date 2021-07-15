package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import org.objectteams.ITeam;

import jdk.dynalink.linker.support.Lookup;

public class OTGuards {
	static final Lookup lookup = new Lookup(MethodHandles.lookup());

	public static final MethodHandle TEST_TEAM_COMPOSITION = lookup.findOwnStatic("testTeamComposition",
			boolean.class, Class[].class, ITeam[].class, int.class);

	
	/**
	 * Compare the teams on the call stack with the guard.
	 * 
	 * @param guardedStack The classes of teams that comprise the guard
	 * @param runtimeStack The teams that are on the call stack at execution time
	 * @param index The current index that is put on the call stack at execution time
	 * @return Whether the guard is structural equivalent to the current teams on the call stack
	 */
	@SuppressWarnings("unused")
	private static boolean testTeamComposition(final Class<ITeam>[] guardedStack, final ITeam[] runtimeStack, final int index) {
		// Abort if there is no active team on the stack
		// or the guarded stack is longer than the runtime stack beginning at the current index
		if (runtimeStack == null || index + guardedStack.length > runtimeStack.length) {
			return false;
		}
		for (int i = 0; i < guardedStack.length; i++) {
			int j = i + index;
			//TODO: isAssignableFrom could introduce problems when looking at team inheritance
			if (!guardedStack[i].isAssignableFrom(runtimeStack[j].getClass())) {
				return false;
			}
		}
		// We have captured a stack of [] which means we will call the base method.
		// This is just okay if the runtime stack is also completely run through.
		if(guardedStack.length == 0) {
			return runtimeStack.length == index + 1;
		}
		return true;
	}
	
	public static MethodHandle buildGuard(Class<?>[] guardedStack) {
		// insert guardedStack into testTeamComposition argument position 0
		return TEST_TEAM_COMPOSITION.bindTo(guardedStack);
	}
}
