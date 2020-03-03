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
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug.internal;


@SuppressWarnings("nls")
public class Logger
{
 
    public static boolean ON = true;
    
	public static void log(int level, String caller, String message)
    {
        switch (level) {
        case 0:
            
            printLog("# ",caller, message);
            break;

        case 1:
            
            printLog("## ",caller, message);
            break;
        case 2:
            
            printLog("## ",caller, message);
            break;
            
        default:
            break;
        }
    }
    
    private static void printLog(String prefix, String caller, String message)
    {
        if (ON)
            System.out.println(prefix + caller + " -> " +message);    
    }
}
