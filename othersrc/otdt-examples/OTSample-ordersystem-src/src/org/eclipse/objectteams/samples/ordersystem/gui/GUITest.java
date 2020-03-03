/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010  Technical University Berlin, Germany, and others.
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
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.samples.ordersystem.gui;


public class GUITest {

    public static void main(String[] args) {
        GUITest tempGUITest = new GUITest();
        tempGUITest.init();
    }
    
    private void init() {
        // hook for security application
        // TODO: remove this when OT can adapt constructors
        new OrderSystemMainFrame();
    }
    
}
