package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import org.objectteams.ITeam;

import jdk.dynalink.linker.support.Lookup;

public class OTGuards {
	static final Lookup lookup = new Lookup(MethodHandles.lookup());

	public static final MethodHandle TEST_TEAM_COMPOSITION = lookup.findOwnStatic("testTeamComposition",
			boolean.class, Class[].class, ITeam[].class, int.class);

	@SuppressWarnings("unused")
	private static boolean testTeamComposition(final Class<ITeam>[] testStack, final ITeam[] stack, final int index) {
		if (stack == null || testStack.length != stack.length - index) {
			return false;
		}
		for (int i = 0, j = index; i < testStack.length; i++, j++) {
			if (!testStack[i].isAssignableFrom(stack[j].getClass())) {
				return false;
			}
		}

		return true;
	}
}
