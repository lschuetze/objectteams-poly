/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2011, 2014 GK Software AG and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.runtime;

/**
 * Interface through which the {@link TeamManager} reaches into the OTREDyn.
 * Representation of a callin binding or a callout decapsulation binding.
 * 
 * @author stephan
 */
public interface IBinding {

	public static enum BindingType {
		CALLIN_BINDING,
		FIELD_ACCESS,
		METHOD_ACCESS
	}
	
	public final static int CALLIN_BASE = 1;
	public final static int STATIC_BASE = 2;

	BindingType getType();

	/** The base class as declared in the source level playedBy clause. */
	String getBoundClass();

	/** The base class actually declaring the referenced member. */
	String getDeclaringBaseClassName();

	/** Name of the bound base member. */
	String getMemberName();

	/** Signature (JVM encoding) of the bound base member. */
	String getMemberSignature();

	/** Answer flags describing the base method (static, callin). */
	int getBaseFlags();

	/** Answer the ID (callinId or perTeamAccessId) */
	int getPerTeamId();

	/** Does base method matching include overrides with covariant return type?. */
	boolean isHandleCovariantReturn();



}
