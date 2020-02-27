/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 *
 * Copyright 2004-2009 Berlin Institute of Technology, Germany.
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
 * Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author resix
 */
public class ListValueHashMap<ValueType> {
	private HashMap<String, LinkedList<ValueType>> hashMap = new HashMap<String, LinkedList<ValueType>>();
	//redundant structure for faster access:
	private LinkedList<ValueType> flattenValues = new  LinkedList<ValueType>();

	/**
	 * @param key
	 * @param value
	 */
	public void put(String key, ValueType value) {
		LinkedList<ValueType> list;
		if (!hashMap.containsKey(key)) {
			list = new LinkedList<ValueType>();
		} else {
			list = hashMap.get(key);
		}
		list.add(value);
		hashMap.put(key, list);
		flattenValues.add(value);
	}

	/**
	 * @return
	 */
	public List<ValueType> getFlattenValues() {
		return flattenValues;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public LinkedList<ValueType> get(String key) {
		if (!hashMap.containsKey(key))  {
			return null;
		}
		return hashMap.get(key);
	}
	
	public boolean containsKey(Object o) {
		return hashMap.containsKey(o);	
	}

	public Set<String> keySet() {
		return hashMap.keySet();
	}

	public Set<Entry<String, LinkedList<ValueType>>>  entrySet() {
		return hashMap.entrySet();
	}
	
	public int size() {
		return hashMap.size();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder(32);
		Iterator<Entry<String, LinkedList<ValueType>>> it = hashMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, LinkedList<ValueType>> entry = (Entry<String, LinkedList<ValueType>>) it.next();
			result.append(entry.getKey());
            result.append(": ");
            result.append(entry.getValue().toString());
            result.append("\n");
		}
		if (result.length() == 0)
			return super.toString();
		return result.toString();
	}
}
