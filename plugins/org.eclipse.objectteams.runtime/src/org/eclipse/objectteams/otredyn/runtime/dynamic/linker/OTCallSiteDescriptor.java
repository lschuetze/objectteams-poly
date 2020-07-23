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
	public static final int CALL = 0b1;
	public static final int CALL_NEXT = 0b10;
	public static final int CALL_ALL_BINDINGS = 0b100;

	private static final int OPERATION_MASK = 0b111;

	private static final Operation[] OPERATIONS = new Operation[] { StandardOperation.CALL };

	private final String joinpointDesc;
	private final int boundMethodId;
	private final int flags;

	public OTCallSiteDescriptor(final Lookup lookup, final Operation operation, final MethodType methodType,
			final String joinpointDesc, final int boundMethodId, final int flags) {
		super(lookup, operation, methodType);
		this.joinpointDesc = joinpointDesc;
		this.boundMethodId = boundMethodId;
		this.flags = flags;
	}

	public static OTCallSiteDescriptor get(final Lookup lookup, final String name, final MethodType type,
			final int flags, final String joinpointDesc, final int boundMethodId) {
		final NamedOperation namedOp = StandardOperation.CALL.withNamespace(StandardNamespace.METHOD)
				.named(joinpointDesc);
		return get(lookup, namedOp, type, joinpointDesc, boundMethodId, flags);
	}

	private static OTCallSiteDescriptor get(final Lookup lookup, final Operation baseOp, final MethodType type,
			final String joinpointDesc, final int boundMethodId, final int flags) {
		return new OTCallSiteDescriptor(lookup, baseOp, type, joinpointDesc, boundMethodId, flags);
	}

	public int boundMethodId() {
		return boundMethodId;
	}

	public String getJoinpointDesc() {
		return joinpointDesc;
	}

	public boolean isCallNext() {
		return (flags & CALL_NEXT) == CALL_NEXT;
	}

	public boolean isCallAllBindings() {
		return (flags & CALL_ALL_BINDINGS) == CALL_ALL_BINDINGS;
	}

}
