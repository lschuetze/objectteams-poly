/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010  Technical University Berlin, Germany.
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
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.example.observer.library;

/**
 * While Persons are actually quite complex, this class models only
 * what is needed to identify a Person when borrowing a book copy.
 */
public class Person {

	private String name;

	public Person (String n) {
		name = n;
	}

	public String getName () { return name; }

    public String toString() { return name; }
}
