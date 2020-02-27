/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2003, 2010 Berlin Institute of Technology, Germany.
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
 *		Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.transformer;

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
        "We would appreciate if you send a bug report to support@ObjectTeams.org.\n"+
        "Please include your program (if possible) and the following diagnostic\n"+
        "in your report. -- Thank you. The ObjectTeams/Java developers\n";
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
