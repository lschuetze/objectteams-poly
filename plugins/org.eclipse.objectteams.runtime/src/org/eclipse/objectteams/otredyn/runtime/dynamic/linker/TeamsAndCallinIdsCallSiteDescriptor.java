package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.Operation;

public class TeamsAndCallinIdsCallSiteDescriptor extends CallSiteDescriptor {
	
	final int joinpointId;

	public TeamsAndCallinIdsCallSiteDescriptor(Lookup lookup, Operation operation, MethodType methodType, final int joinpointId) {
		super(lookup, operation, methodType);
		this.joinpointId = joinpointId;
	}
	
	int getJoinpointId() {
		return this.joinpointId;
	}
}
