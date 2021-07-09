package org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util;

import org.objectteams.ITeam;

public final class ObjectTeamsGuardUtility {
	
	public static Class<?>[] guardTestStack(final ITeam[] teams, final int index, final int startIndex) {
		final int testStackLength = index - startIndex;
		Class<?>[] testStack = new Class<?>[testStackLength];
		//for (int j = 0, i = startIndex; i < index; i++, j++) {
		for(int i = 0; i < testStackLength; i++) {
			int j = i + startIndex;
			testStack[i] = teams[j].getClass();
		}
		return testStack;
	}
}
