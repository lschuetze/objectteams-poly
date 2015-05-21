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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.objectteams.otredyn.bytecode.asm.ASMByteCodeAnalyzer.ClassInformation;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import static org.eclipse.objectteams.otredyn.transformer.names.ClassNames.OBJECT_SLASH;

/**
 * Variant of its superclass which strictly avoids the use of Class.forName(),
 * because that would by-pass our transformer, when invoked from within the transformer!
 */
public class LoaderAwareClassWriter extends ClassWriter {

	// Only use as a resource loader!
	private ClassLoader loader;
	private ASMByteCodeAnalyzer analyzer; // hopefully caching loaded classes per class-being-written is sufficient for performance

	public LoaderAwareClassWriter(ClassReader reader, int computeFrames, ClassLoader loader) {
		super(reader, computeFrames);
		this.loader = loader;
		this.analyzer = new ASMByteCodeAnalyzer(false);
	}
	
	@Override
	protected String getCommonSuperClass(String type1, String type2) {
		// simple cases first:
		if (type1.equals(type2))
			return type1;
		if (type1.equals(OBJECT_SLASH) || type2.equals(OBJECT_SLASH))
			return OBJECT_SLASH;
		
		// need to load class bytes:
		InputStream s1;
		InputStream s2;
        try {
    		s1 = this.loader.getResourceAsStream(type1+".class");
    		s2 = this.loader.getResourceAsStream(type2+".class");
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        ClassInformation ci1 = this.analyzer.getClassInformation(s1, type1);
        ClassInformation ci2 = this.analyzer.getClassInformation(s2, type2);
        if (ci1 == null || ci2 == null)
        	return OBJECT_SLASH;

        // do a breadth-first search: each iteration adds just one more level of super types:
        Set<String> allTypes1 = new HashSet<String>();
        Set<String> allTypes2 = new HashSet<String>();
        allTypes1.add(type1);
        allTypes2.add(type2);
        Set<String> newTypes1 = getDirectSupers(ci1);
        Set<String> newTypes2 = getDirectSupers(ci2);
        while (true) {
        	if (newTypes1.isEmpty() && newTypes2.isEmpty())
        		return OBJECT_SLASH;
        	for (String newType1 : newTypes1)
        		if (allTypes2.contains(newType1))
        			return newType1;
        	for (String newType2 : newTypes2)
        		if (allTypes1.contains(newType2))
        			return newType2;
        	allTypes1.addAll(newTypes1);
        	allTypes2.addAll(newTypes2);
        	newTypes1 = getDirectSupers(newTypes1);
        	newTypes2 = getDirectSupers(newTypes2);
        }
	}

	private Set<String> getDirectSupers(Set<String> types) {
		Set<String> result = new HashSet<String>();
		for (String type : types) {
			InputStream s;
			try {
				s = this.loader.getResourceAsStream(type+".class");
	        } catch (Exception e) {
	            throw new RuntimeException(e.toString());
			}
			ClassInformation ci = this.analyzer.getClassInformation(s, type);
			if (ci != null)
				result.addAll(getDirectSupers(ci));
		}
		return result;
	}

	private Set<String> getDirectSupers(ClassInformation ci) {
		Set<String> result = new HashSet<String>();
		String superClass = ci.getSuperClassName();
		if (superClass != null && !superClass.equals(OBJECT_SLASH)) // avoid prematurely answering j.l.Object
			result.add(superClass);
		for (String ifc : ci.getSuperInterfaceNames())
			result.add(ifc);
		return result;
	}

}
