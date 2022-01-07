/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008, 2015 Oliver Frank and others.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 		Oliver Frank - Initial API and implementation
 * 		Stephan Herrmann - adjusted for ASM 5.0
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode.asm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.objectteams.otredyn.transformer.names.ClassNames;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

/**
 * This class performs some fast readClass analyses
 * to determine further processing.
 * 
 * @author Oliver Frank
 * @since 1.2.3
 */
public class ASMByteCodeAnalyzer {

	boolean useExternalForm = false; // true: use '.', false: use '/'
	

	public class ClassInformation {
		private int modifiers;
		private String superClassName;
		private String[] superInterfaceNames;

		ClassInformation(ClassReader classReader) {
			this.modifiers = classReader.getAccess();
			this.superClassName = classReader.getSuperName();
			this.superInterfaceNames = classReader.getInterfaces();
		}

		public ClassInformation(Class<?> clazz) {
			this.modifiers = clazz.getModifiers();
			Class<?> superclass = clazz.getSuperclass();
			this.superClassName = superclass != null
					? superclass.getName().replace('.', '/')
					: ClassNames.OBJECT_SLASH;
			Class<?>[] interfaces = clazz.getInterfaces();
			this.superInterfaceNames = new String[interfaces.length];
			for (int i = 0; i < interfaces.length; i++) {
				this.superInterfaceNames[i] = interfaces[i].getName().replace('.', '/');
			}
		}

		public boolean isInterface() {
			return (modifiers & Opcodes.ACC_INTERFACE) != 0;
		}

		public String getSuperClassName() {
			if (superClassName != null)
				return useExternalForm ? superClassName.replace('/', '.') : superClassName;
			return null;
		}
		
		public String[] getSuperInterfaceNames() {
			if (superInterfaceNames != null && useExternalForm)
				for (int i = 0; i < superInterfaceNames.length; i++) {
					superInterfaceNames[i] = superInterfaceNames[i].replace('/', '.');
				}
			return superInterfaceNames;
		}
	}

	private Map<String, ClassInformation> classInformationMap =
			new ConcurrentHashMap<String, ClassInformation>(512, 0.75f, 4);

	/** Should the external (dot-separated) form be used? */
	public ASMByteCodeAnalyzer(boolean useExternalForm) {
		this.useExternalForm = useExternalForm;
	}
	
	public ClassInformation getClassInformation(InputStream classStream, String className) {
		try {
			return getClassInformation(null, classStream, className);
		} catch (IOException e) {
			return null;
		}
	}
	
	public ClassInformation getClassInformation(byte[] classBytes, String className) {
		try {
			return getClassInformation(classBytes, null, className);
		} catch (IOException e) {
			return null;
		}
	}

	private ClassInformation getClassInformation(byte[] classBytes,
			InputStream classStream, String className) throws IOException 
	{
		ClassInformation classInformation = classInformationMap.get(className);
		if (classInformation != null) {
			return classInformation;
		}
		ClassReader classReader = classBytes != null ? new ClassReader(classBytes) : new ClassReader(classStream);
		classInformation = new ClassInformation(classReader);
		classInformationMap.put(className, classInformation);
		return classInformation;
	}

	public ClassInformation getClassInformation(String className, ClassLoader loader) {
		ClassInformation classInformation = classInformationMap.get(className);
		if (classInformation != null) {
			return classInformation;
		}
		try {
			Class<?> clazz = loader.loadClass(className.replace('/', '.'));
			if (clazz != null)
				return new ClassInformation(clazz);
		} catch (ClassNotFoundException e) {
			return null;
		}
		return null;
	}
}
