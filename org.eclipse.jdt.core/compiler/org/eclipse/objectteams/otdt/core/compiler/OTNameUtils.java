/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2009 Stephan Herrmann
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id: OTNameUtils.java 23417 2010-02-03 20:13:55Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.compiler;

import static org.eclipse.objectteams.otdt.core.compiler.IOTConstants.BASE_PREDICATE_PREFIX;
import static org.eclipse.objectteams.otdt.core.compiler.IOTConstants.OT_DELIM_LEN;
import static org.eclipse.objectteams.otdt.core.compiler.IOTConstants.OT_DELIM_NAME;
import static org.eclipse.objectteams.otdt.core.compiler.IOTConstants.OT_DOLLAR_NAME;
import static org.eclipse.objectteams.otdt.core.compiler.IOTConstants.PREDICATE_METHOD_NAME;
import static org.eclipse.objectteams.otdt.core.compiler.IOTConstants.TSUPER_OT_NAME;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;

/**
 * API class providing various operations for special names in generated OT/J code.
 * @author stephan
 * @since 1.3.2
 */
public class OTNameUtils {

	// for inferred callout to field:
	private static final char[] SET = "set".toCharArray(); //$NON-NLS-1$
	private static final char[] GET = "get".toCharArray(); //$NON-NLS-1$

	/**
	 * Is selector the name of a predicate method?
	 */
	public static boolean isPredicate(char[] selector) {
		if (!CharOperation.prefixEquals(OT_DOLLAR_NAME, selector))
			return false;
		return (   CharOperation.prefixEquals(PREDICATE_METHOD_NAME, selector)
			    || CharOperation.prefixEquals(BASE_PREDICATE_PREFIX, selector));
	}

	/** Remove all occurrences of '__OT__' from a given type name. */
	public static char[] removeOTDelim(char[] typeName) {
		if (!CharOperation.contains(OT_DELIM_NAME, typeName))
			return typeName;
		char[] strippedName = new char[0];
		int start = 0;
		int pos = CharOperation.indexOf(OT_DELIM_NAME, typeName, true, start);
		while (pos > -1) {
			strippedName = CharOperation.concat(
								strippedName,
								CharOperation.subarray(typeName, start, pos));
			start = pos;
			pos = CharOperation.indexOf(OT_DELIM_NAME, typeName, true, start+1);
		}
		strippedName = CharOperation.concat(
								strippedName,
								CharOperation.subarray(typeName, start+OT_DELIM_LEN, -1));
		return strippedName;
	}

	/**
	 * Given a fieldName (e.g. val) construct an accessor method name (getVal, or setVal) for inferred callout.
	 * @param isSetter asking for a "set" accessor?
	 * @param fieldName
	 */
	public static char[] accessorName(boolean isSetter, char[] fieldName) {
		if (fieldName == null)
			return null;
		char[] capitalized= new char[fieldName.length];
		System.arraycopy(fieldName, 0, capitalized, 0, fieldName.length);
		capitalized[0] = Character.toUpperCase(fieldName[0]);
		char[] prefix = isSetter ? OTNameUtils.SET : OTNameUtils.GET;
		return CharOperation.concat(prefix, capitalized);
	}
	/**
	 * Given a fieldName (e.g. val) construct an accessor method name (getVal, or setVal) for inferred callout.
	 * @param isSetter asking for a "set" accessor?
	 * @param fieldName
	 * @since 3.10 OT 2.3
	 */
	public static String accessorName(boolean isSetter, String fieldName) {
		if (fieldName == null)
			return null;
		int len = fieldName.length();
		char[] accessor = new char[3+len];
		System.arraycopy(isSetter ? OTNameUtils.SET : OTNameUtils.GET, 0, accessor, 0, 3);
		accessor[3] = Character.toUpperCase(fieldName.charAt(0));
		fieldName.getChars(1, len, accessor, 4);
		return String.valueOf(accessor);
	}

	/** Does name denote a synthetic marker interface used for marking tsuper methods? */
	public static boolean isTSuperMarkerInterface(char[] name) {
		if (name == null) // some types like LocalTypeBinding and IntersectionCastTypeBinding don't have a name
			return false;
		int lastDollar = CharOperation.lastIndexOf('$', name);
		if (lastDollar > -1)
			name = CharOperation.subarray(name, lastDollar+1, -1);
	    return CharOperation.prefixEquals(TSUPER_OT_NAME, name);
	}

	/** Given that type is a role, determine confinedness by the simple name. */
	public static boolean isTopConfined(String elementName) {
		char[] name = elementName.toCharArray();
		return     CharOperation.equals(name, IOTConstants.ICONFINED)
				|| CharOperation.equals(name, IOTConstants.OTCONFINED)
				|| CharOperation.equals(name, IOTConstants.CONFINED);
	}

	/**
	 * does 'type' denote a predefined confined type?
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public static boolean isPredefinedConfined(ReferenceBinding type)
	{
		switch (type.id) {
			case IOTConstants.T_OrgObjectteamsIConfined:
			case IOTConstants.T_OrgObjectteamsITeamIConfined:
			case IOTConstants.T_OrgObjectteamsTeamOTConfined:
			case IOTConstants.T_OrgObjectteamsTeamConfined:
				return true;
		}
		return false;
	}


}
