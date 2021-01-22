package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.objectteams.ITeam;

import jdk.dynalink.linker.support.Lookup;

public class OTGuards {
	public static final MethodHandle TEST_COMPOSITION;
	
	static {
        final Lookup lookup = new Lookup(MethodHandles.lookup());

        TEST_COMPOSITION  = lookup.findOwnStatic("testTeamsComposition", boolean.class, Class[].class, ITeam[].class);
    }
	
	@SuppressWarnings("unused")
	public static boolean testTeamsComposition(final Class<ITeam>[] stack, final ITeam[] test) {		
		if (test == null || stack.length != test.length) {
			return false;
		}
		
		for (int i = 0; i < stack.length; i++) {
			if (!stack[i].isAssignableFrom(test[i].getClass())) {
				return false;
			}
		}
		
		return true;
	}
}
