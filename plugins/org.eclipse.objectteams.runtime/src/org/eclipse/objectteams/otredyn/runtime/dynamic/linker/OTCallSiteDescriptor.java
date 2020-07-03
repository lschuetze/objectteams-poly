package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.Operation;
import jdk.dynalink.StandardOperation;

public final class OTCallSiteDescriptor extends CallSiteDescriptor {
	/** Call operation {@code base.fn(args...)} */
	public static final int CALL = 1;

	private static final int OPERATION_MASK = 2;

	// TODO: Add operations and OP caches
	private static final Operation[] OPERATIONS = new Operation[] { StandardOperation.CALL };

	public OTCallSiteDescriptor(Lookup lookup, Operation operation, MethodType methodType) {
		super(lookup, operation, methodType);
		// TODO Auto-generated constructor stub
	}

	public static OTCallSiteDescriptor get(final Lookup lookup, final String name, final MethodType type,
			final int flags, final String joinpointDesc, final int boundMethodId) {
		final int opIndex = flags & OPERATION_MASK;
		return null;
	}

}
