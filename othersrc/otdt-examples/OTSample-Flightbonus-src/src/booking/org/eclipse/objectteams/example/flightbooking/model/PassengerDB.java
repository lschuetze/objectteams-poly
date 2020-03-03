/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.example.flightbooking.model;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.objectteams.example.flightbooking.model.Passenger;


/**
 * This class implements a list of <code>Passenger</code>s.
 */
public class PassengerDB {
    
	private Hashtable<String, Passenger> _data;
	private Enumeration<Passenger> _valueEnumeration;
	
	public PassengerDB() {
		_data = new Hashtable<String, Passenger>();
	}

	public void add(Passenger pass) {
		if (!_data.containsKey(pass.getName())) {
			_data.put(pass.getName(), pass);
		}
	}

	public void remove(Passenger pass) {
		_data.remove(pass.getName());
	}

	public Passenger get(String name) {
		return _data.get(name);
	}

	public boolean contains(String name) {
		return _data.containsKey(name);
	}
	
	
	public void reset() {
		_valueEnumeration = _data.elements();
	}
	
	public boolean hasNext() {
		return _valueEnumeration != null && _valueEnumeration.hasMoreElements();
	}
	
	public Passenger getNext() {
		return _valueEnumeration.nextElement();
	}
}