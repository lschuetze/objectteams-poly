package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.NamedOperation;
import jdk.dynalink.Operation;
import jdk.dynalink.StandardNamespace;
import jdk.dynalink.StandardOperation;

public final class OTCallSiteDescriptor extends CallSiteDescriptor {
	/** Call operation {@code base.fn(args...)} */
	public static final int CALL = 0b0;

	private static final int OPERATION_MASK = 0b1;

	private static final Operation[] OPERATIONS = new Operation[] { StandardOperation.CALL };

	private final int boundMethodId;

	public OTCallSiteDescriptor(Lookup lookup, Operation operation, MethodType methodType, int boundMethodId) {
		super(lookup, operation, methodType);
		this.boundMethodId = boundMethodId;
	}

	public static OTCallSiteDescriptor get(final Lookup lookup, final String name, final MethodType type,
			final int flags, final String joinpointDesc, final int boundMethodId) {
		final int opIndex = flags & OPERATION_MASK; // always 0 for CALL
		final Operation baseOp = OPERATIONS[opIndex];
		final NamedOperation namedOp = baseOp.withNamespace(StandardNamespace.METHOD).named(joinpointDesc);
		return get(lookup, namedOp, type, boundMethodId);
	}

	private static OTCallSiteDescriptor get(Lookup lookup, Operation baseOp, MethodType type, int flags) {
		return new OTCallSiteDescriptor(lookup, baseOp, type, flags);
	}

	public int boundMethodId() {
		return boundMethodId;
	}

	public String getJoinpointDesc() {
		if (getOperation() instanceof NamedOperation) {
			final NamedOperation namedOp = (NamedOperation) getOperation();
			return namedOp.getName().toString();
		}
		return "";
	}

}
