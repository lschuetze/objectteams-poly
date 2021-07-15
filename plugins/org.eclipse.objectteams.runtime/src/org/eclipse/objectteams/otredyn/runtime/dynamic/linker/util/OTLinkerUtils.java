package org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import jdk.dynalink.linker.support.Lookup;

public final class OTLinkerUtils {
	
	private static final MethodHandle INCREMENT = Lookup.PUBLIC.findStatic(Math.class, "addExact",
			MethodType.methodType(int.class, int.class, int.class));
	
	public static MethodHandle incrementInt(final int first) {
		return MethodHandles.insertArguments(INCREMENT, 0, first);
	}
}
