/**********************************************************************
 * Copyright 2019 Stephan Herrmann
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.util;

public interface SMAPConstants {
    public final static int STEP_OVER_LINENUMBER = Short.MAX_VALUE *2;                       // = 65534
    public final static int STEP_INTO_LINENUMBER = STEP_OVER_LINENUMBER - 1;                 // = 65533
}
