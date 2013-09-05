/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2012 Oliver Frank and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Oliver Frank - Initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.objectteams.otredyn.runtime.IBoundTeam;

/**
 * Represents a team class and stores the bindings this team has got.
 */
public abstract class AbstractTeam extends AbstractBoundClass implements IBoundTeam {
	/**
	 * All relevant bindings (callin and decapsulation) of this team.
	 * By internally using a TreeSet the set of bindings is naturally sorted 
	 * based on {@link Binding#compareTo(Binding)} (see there).
	 */
	private Set<Binding> bindings;
	private int highestAccessId;

	protected AbstractTeam(String name, String id, ClassLoader loader) {
		super(name, id, loader);
		bindings = new TreeSet<Binding>();
	}

	/**
	 * Adds a binding to the team.
	 * This method is intended to be called, 
	 * while parsing the bytecode
	 * @param binding
	 */
	public void addBinding(Binding binding) {
		bindings.add(binding);
	}

	public Collection<Binding> getBindings() {
		parseBytecode();
		return bindings;
	}
	
	/**
	 * Returns the superclass of this team as AbstractTeam
	 */
	@Override
	public AbstractTeam getSuperclass() {
		return (AbstractTeam) super.getSuperclass();
	}
	
	/**
	 * Record that this team uses the given accessId.
	 * @param accessId
	 */
	public void recordAccessId(int accessId) {
		this.highestAccessId = Math.max(this.highestAccessId, accessId);
	}

	public int getHighestAccessId() {
		return highestAccessId;
	}
}