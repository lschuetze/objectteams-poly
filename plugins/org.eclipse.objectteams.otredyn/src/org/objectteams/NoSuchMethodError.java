/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 *
 * Copyright 2011 GK Software AG.
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
package org.objectteams;

/**
 * This error is thrown when some generated code falls through a switch
 * without finding a requested target method.
 *
 * @author stephan
 */
public class NoSuchMethodError extends java.lang.NoSuchMethodError {
	private static final long serialVersionUID = 8166812526610195056L;

	public NoSuchMethodError(int accessId, String className, String accessReason) {
		super("Method with internal id "+accessId+" cannot be found in "+className+" for "+accessReason);
	}
}
