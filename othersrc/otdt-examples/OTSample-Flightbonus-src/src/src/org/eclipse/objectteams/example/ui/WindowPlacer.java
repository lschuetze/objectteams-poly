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
package org.eclipse.objectteams.example.ui;

import java.awt.Component;
import java.awt.Container;

import base java.awt.Window;


/**
 * This aspect ensures that each window will appear in the middle
 * of the screen.
 */
public team class WindowPlacer  {

    @SuppressWarnings("bindingtosystemclass")
	protected class WindowAdaptor playedBy Window {
    	// callouts:
    	Container getParent()   		   -> Container getParent();
    	void setCentered(Container parent) -> void setLocationRelativeTo(Component parent);

    	// callin:
    	void centerRelativeToParent() <- after void pack(); 
    	
    	// the action:
    	protected void centerRelativeToParent() {
    		setCentered(getParent());
    	}    	
    }
}
