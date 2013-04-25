/**********************************************************************
 * This file is part of "Object Teams Runtime Environment"-Software
 * 
 * Copyright 2011 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 * 
 * This file is based on class org.apache.bcel.util.ClassLoaderRepository
 * originating from the Apache BCEL project which was provided under the 
 * Apache 2.0 license. Original Copyright from BCEL:
 * 
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 **********************************************************************/
package org.eclipse.objectteams.otre.bcel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.ClassLoaderRepository;
import org.eclipse.objectteams.otre.ClassLoaderAccess;

/**
 * Class loader repository that retains less memory than BCEL's original.
 */
@SuppressWarnings("serial")
public class DietClassLoaderRepository extends ClassLoaderRepository {

	// repeat a field that is private in the super class:
	Object dietClassLoaderRepository_loader;
	
	public DietClassLoaderRepository(Object loader) {
		super(loader instanceof ClassLoader ? (ClassLoader) loader : null);
		dietClassLoaderRepository_loader = loader;
	}

	@Override
	public JavaClass loadClass(String className) throws ClassNotFoundException {
        String classFile = className.replace('.', '/');
        JavaClass c = findClass(className);
        if (c != null) {
            return c;
        }
        try {
            InputStream is = ClassLoaderAccess.getResourceAsStream(dietClassLoaderRepository_loader, classFile + ".class");
            if (is == null) {
                throw new ClassNotFoundException(className + " not found.");
            }
            DietClassParser parser = new DietClassParser(is, className);
            c = parser.parse();
            storeClass(c);
            return c;
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        }
	}
	public JavaClass loadClassFully(String className) throws ClassNotFoundException {
        JavaClass c = findClass(className);
        if (c != null) { 
        	if (! (c instanceof DietJavaClass))
        		return c; // found fully parsed class, OK.
        	removeClass(c); // force new loading
        }
		return super.loadClass(className);
	}
}
