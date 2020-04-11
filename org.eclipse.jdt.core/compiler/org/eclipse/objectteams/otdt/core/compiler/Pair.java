/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2007, 2009 Technical University Berlin, Germany.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.compiler;

/**
 * Simple pair structure.
 *
 * @author stephan
 * @since 0.9.27
 *
 * @param <T1> type of the first element
 * @param <T2> type of the second element
 */
public class Pair<T1,T2> {
	public T1 first;
	public T2 second;
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	/** Test for shallow equality with the given other elements. */
	@SuppressWarnings("hiding")
	public boolean equals(T1 first, T2 second) {
		return this.first == first && this.second == second;
	}
}
