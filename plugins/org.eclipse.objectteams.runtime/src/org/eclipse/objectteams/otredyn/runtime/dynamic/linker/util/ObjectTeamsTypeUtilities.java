package org.eclipse.objectteams.otredyn.runtime.dynamic.linker.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

import org.eclipse.objectteams.otredyn.runtime.IBinding;
import org.eclipse.objectteams.otredyn.runtime.TeamManager;
import org.objectteams.ITeam;

public final class ObjectTeamsTypeUtilities {

	private static final String ROLE_TYPE_SEP = "$__OT__";
	private static final String ROLE_ITF_SEP = "$";

	public static Class<?> getBaseClass(final String baseClassName, final ClassLoader baseClassLoader) {
		final Class<?> clazz;
		try {
			clazz = Class.forName(baseClassName.replace('/', '.'), true, baseClassLoader);
		} catch (ClassNotFoundException e) {
			final NoSuchMethodError ee = new NoSuchMethodError();
			ee.initCause(e);
			throw ee;
		}
		return clazz;
	}

	public static Class<?> getRoleImplType(final Class<?> team, final String role) {
		final String roleClassName = team.getName() + ROLE_TYPE_SEP + role;
		final Class<?> clazz;
		try {
			clazz = Class.forName(roleClassName, true, team.getClassLoader());
		} catch (ClassNotFoundException e) {
			final NoSuchMethodError ee = new NoSuchMethodError();
			ee.initCause(e);
			throw ee;
		}
		return clazz;
	}

	public static Class<?> getRoleItfType(final Class<?> team, final String role) {
		final String roleClassName = team.getName() + ROLE_ITF_SEP + role;
		final Class<?> clazz;
		try {
			clazz = Class.forName(roleClassName, true, team.getClassLoader());
		} catch (ClassNotFoundException e) {
			final NoSuchMethodError ee = new NoSuchMethodError();
			ee.initCause(e);
			throw ee;
		}
		return clazz;
	}

	public static IBinding getBindingFromId(final String joinpointDesc, final ITeam team, final int callinId) {
		final List<IBinding> bindings = TeamManager.getPrecedenceSortedCallinBindings(team, joinpointDesc);
		return bindings.stream().filter(b -> b.getPerTeamId() == callinId).findFirst().get();
	}

	public static MethodHandle findVirtual(MethodHandles.Lookup lookup, Class<?> declaringClass, String name,
			MethodType type) {
		try {
			return lookup.findVirtual(declaringClass, name, type);
		} catch (NoSuchMethodException e) {
			NoSuchMethodError ee = new NoSuchMethodError("Class " + declaringClass + " and "
					+ declaringClass.getSuperclass() + ", name " + name + " , type " + type.toMethodDescriptorString());
			ee.initCause(e);
			throw ee;
		} catch (IllegalAccessException e) {
			IllegalAccessError ee = new IllegalAccessError(e.getMessage());
			ee.initCause(e);
			throw ee;
		}
	}
}
