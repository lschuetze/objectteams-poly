/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2003-2009 Berlin Institute of Technology, Germany.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 *
 * Please visit http://www.objectteams.org for updates and contact.
 *
 * Contributors:
 * Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.objectteams;

/**
 * @author stephan 
 */
public class OTREInternalError extends Error {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String _bugmsg = 
        "An error occurred in the Object Teams runtime environment.\n"+
        "We would appreciate if you submit a bug report at https://bugs.eclipse.org/bugs/enter_bug.cgi?product=Objectteams.\n"+
        "Please include your program (if possible) and the following diagnostic\n"+
        "in your report. -- Thank you. The OT/J developers\n";
    /**
     * 
     */
    public OTREInternalError() {
        super(_bugmsg);
    }

    /**
     * @param message
     */
    public OTREInternalError(String message) {
        super(_bugmsg+message);
    }

    /**
     * @param cause
     */
    public OTREInternalError(Throwable cause) {
        super(_bugmsg+cause.toString());
    }

    /**
     * @param message
     * @param cause
     */
    public OTREInternalError(String message, Throwable cause) {
        super(_bugmsg+message+cause.toString());
    }

}
