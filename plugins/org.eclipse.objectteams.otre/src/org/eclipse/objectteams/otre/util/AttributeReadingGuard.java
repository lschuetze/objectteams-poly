/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2002-2009 Berlin Institute of Technology, Germany.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Please visit http://www.objectteams.org for updates and contact.
 *
 * Contributors:
 * Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otre.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Christine Hundt
 * @author Stephan Herrmann
 */
public class AttributeReadingGuard {
	
	private static Map<Object, AttributeReadingGuard> instances = new HashMap<Object, AttributeReadingGuard>();
    private static AttributeReadingGuard defaultInstance = new AttributeReadingGuard();
    
    private ArrayList<String> servedClasses = new ArrayList<String>();
    
    // this one flag is really global (concerns the one main class of the application):
    private static boolean firstLoaded = true;

    /**
     * @param className
     * @return
     */
    public boolean iAmTheFirst(String className) {
        return !this.servedClasses.contains(className);    
    }
    
    /**
     * Processing the given class is done.
     * @param className
     */
    public void workDone(String className) {
    	this.servedClasses.add(className);    
    }
    
    /**
     * @return whether this class is the first being loaded => possibly the main class.
     */
    public static synchronized boolean isFirstLoadedClass() {
    	if (!firstLoaded)
    		return false;
    	firstLoaded = false;
    	return true;
    }

    /** First loaded class has no main => it was a false alarm. */
	public static void reset() {
		firstLoaded = true;
	}

	/**
	 * Since actual data are stored in an instance, static methods need to retrieve the appropriate
     * instance regarding the given token (s.t. like a class loader).
	 */
	public static AttributeReadingGuard getInstanceForLoader(Object token) {
		if (token == null)
			return defaultInstance;
		
		AttributeReadingGuard instance = instances.get(token);
		if (instance == null)
			instances.put(token, instance = new AttributeReadingGuard());
		return instance;
	}
}
