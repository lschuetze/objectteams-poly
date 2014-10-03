/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014 GK Software AG
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode.asm;

import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * Variant of its superclass which strictly avoids the use of Class.forName(),
 * because that would by-pass our transformer, when invoked from within the transformer!
 */
public class LoaderAwareClassWriter extends ClassWriter {

	// Only use as a resource loader!
	private ClassLoader loader;

	public LoaderAwareClassWriter(ClassReader reader, int computeFrames, ClassLoader loader) {
		super(reader, computeFrames);
		this.loader = loader;
	}
	
	@Override
	protected String getCommonSuperClass(String type1, String type2) {
        try {
    		InputStream s1 = this.loader.getResourceAsStream(type1+".class");
    		InputStream s2 = this.loader.getResourceAsStream(type2+".class");
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        // FIXME: analyse super types from the above streams!
        // consider using (a copy of?) org.eclipse.objectteams.internal.osgi.weaving.ASMByteCodeAnalyzer
        return type2;
//        if (c.isAssignableFrom(d)) {
//        	System.err.println("answer "+type1);
//            return type1;
//        }
//        if (d.isAssignableFrom(c)) {
//        	System.err.println("answer "+type2);
//            return type2;
//        }
//        System.err.println("not assignable");
//        if (c.isInterface() || d.isInterface()) {
//            return "java/lang/Object";
//        } else {
//            do {
//                c = c.getSuperclass();
//            } while (!c.isAssignableFrom(d));
//            return c.getName().replace('.', '/');
//        }
	}

}
