/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id: ResolvedCallinMapping.java 23416 2010-02-03 19:59:31Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.internal.core.util.MethodData;

public class ResolvedCallinMapping extends CallinMapping {
	private String uniqueKey;

	/*
	 * undocumented.
	 */
	public ResolvedCallinMapping(
			int          declarationStart,
			int          sourceStart,
			int			 sourceEnd,
	    	int          declarationEnd,
	        IRoleType    parent,
	    	IMethod 	 corrJavaMeth,
	        char[]       name,
	        int 	     callinKind,
	        MethodData   roleMethodHandle,
	        MethodData[] baseMethodHandles,
	        boolean 	 hasSignature,
	        String 		 uniqueKey)
	{
		super(declarationStart, sourceStart, sourceEnd, declarationEnd,
			  parent, corrJavaMeth,
			  name, callinKind, roleMethodHandle, baseMethodHandles,
			  hasSignature, /*addAsChild*/false);
		this.uniqueKey = uniqueKey;
	}

	@Override
	public String getKey() {
		return this.uniqueKey;
	}

	@Override
	public boolean isResolved() {
		return true;
	}
}
