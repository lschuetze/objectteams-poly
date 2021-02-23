package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import org.objectteams.ITeam;

import jdk.dynalink.linker.support.Lookup;

public class OTGuards {
	static final Lookup lookup = new Lookup(MethodHandles.lookup());

	public static final MethodHandle TEST_COMPOSITION_AND_INDEX = lookup.findOwnStatic("testTeamsComposition",
			boolean.class, Class[].class, int.class, ITeam[].class, int.class);
	
	@SuppressWarnings("unused")
	public static boolean testTeamsComposition(final Class<ITeam>[] stack, final int index, final ITeam[] testStack, final int testIndex) {
		if (index != testIndex || testStack == null || stack.length != testStack.length) {
			return false;
		}
		for (int i = 0; i < stack.length; i++) {
			if (!stack[i].isAssignableFrom(testStack[i].getClass())) {
				return false;
			}
		}
		
		return true;
	}
}
