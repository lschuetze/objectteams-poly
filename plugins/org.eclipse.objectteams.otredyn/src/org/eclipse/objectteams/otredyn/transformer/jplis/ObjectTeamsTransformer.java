/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2002, 2014 Berlin Institute of Technology, Germany, and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.transformer.jplis;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import org.eclipse.objectteams.otredyn.bytecode.AbstractBoundClass;
import org.eclipse.objectteams.otredyn.bytecode.ClassRepository;


/**
 * This class does all needed transformations at load time.
 * @author  Christine Hundt
 */

public class ObjectTeamsTransformer implements ClassFileTransformer {
	/* (non-Javadoc)
	 * @see java.lang.instrument.ClassFileTransformer#transform(java.lang.ClassLoader, java.lang.String, java.lang.Class, java.security.ProtectionDomain, byte[])
	 */

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
			
			
		return transform(loader, className, className, classBeingRedefined, classfileBuffer);
	}
	
	public byte[] transform(ClassLoader loader, String className, String classId, Class<?> classBeingRedefined,
            byte[] classfileBuffer) {
		if (!ObjectTeamsTransformer.isWeavable(className))
			return null;
		
		AbstractBoundClass clazz = ClassRepository.getInstance().getBoundClass(
				className.replace('/','.'), classId, loader);
		if (classBeingRedefined == null && !clazz.isFirstTransformation()) {
			return clazz.getBytecode(); // FIXME: re-loading existing class?? Investigate classloader, check classId strategy etc.pp.
		}
		try {
			if (clazz.isTransformationActive()) {
				return null;
			}
			clazz = ClassRepository.getInstance().getBoundClass(
					className, classId, classfileBuffer, loader);
			if (!clazz.isInterface())
				ClassRepository.getInstance().linkClassWithSuperclass(clazz);
			if (!clazz.isInterface() || clazz.isRole())
				clazz.transformAtLoadTime();
			
			classfileBuffer = clazz.getBytecode();
			clazz.dump(classfileBuffer, "initial");
		} catch(Throwable t) {
			t.printStackTrace();
		}
		
		return classfileBuffer;
	}

	public static boolean isWeavable(String className) {
		switch(className.charAt(0)) {
		case 'o':
			if (   className.startsWith("org/eclipse/objectteams/otre") // incl. otredyn
				|| className.startsWith("org/objectteams/") 			// Team etc.
				|| className.startsWith("org/objectweb/asm"))
				// skip OTRE and ASM classes
				return false;
			break;
		case 's':
			// skip, I saw a mysterious deadlock involving sun.misc.Cleaner
			if (className.startsWith("sun/misc"))
				return false;
			break;
		case 'j':
			// skip, I saw class loading circularity caused by accessing this class:
			if (   className.equals("java/util/LinkedHashMap$KeyIterator")
				|| className.startsWith("java/lang")
				|| className.startsWith("java/util")
				|| className.startsWith("java/io")
				) 
				return false;
			break;
		case '$':
			// funny case, motivated by the following stack trace:
			// 		java.lang.NoClassDefFoundError: org/eclipse/objectteams/otredyn/runtime/TeamManager
			//	    at $Proxy0.<clinit>(Unknown Source)
			//	    at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
			//	    at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57)
			//	    at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
			//	    at java.lang.reflect.Constructor.newInstance(Constructor.java:532)
			//	    at java.lang.reflect.Proxy.newProxyInstance(Proxy.java:605)
			return false;
		}
		return true;
	}
}
