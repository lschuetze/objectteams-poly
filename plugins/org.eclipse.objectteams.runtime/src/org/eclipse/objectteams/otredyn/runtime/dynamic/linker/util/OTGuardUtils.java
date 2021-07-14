package org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util;

import org.objectteams.ITeam;

public final class OTGuardUtils {
	
	/**
	 * Creates an array of class entries that comprise a stack that is used for guarding.
	 * 
	 * @param teams The current teams on the call stack
	 * @param finalIndex The final index the dispatch plan arrived at
	 * @param startIndex The index that is currently on the call stack
	 * @return An array of class entries from teams[startIndex] until teams[finalIndex]
	 */
	public static Class<?>[] constructTestStack(final ITeam[] teams, final int startIndex, final int finalIndex) {
		final int testStackLength = finalIndex - startIndex;
		Class<?>[] testStack = new Class<?>[testStackLength];
		for(int i = 0; i < testStackLength; i++) {
			int j = i + startIndex;
			testStack[i] = teams[j].getClass();
		}
		return testStack;
	}
}
