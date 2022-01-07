/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008, 2019 Oliver Frank and others.
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
 * 		Oliver Frank - Initial API and implementation
 * 		Stephan Herrmann - adjusted for ASM 5.0
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.objectteams.internal.osgi.weaving.OTWeavingHook.WeavingScheme;
import org.eclipse.objectteams.otredyn.bytecode.Types;
import org.eclipse.objectteams.otredyn.bytecode.asm.Attributes;
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

	protected static class ClassAndAttributesReader extends ClassReader {
		private static final int MAX_ATTR_NAME_LEN = 100;  // should use the private(!!!) field ClassReader#maxStringLength
		
		WeavingScheme weavingScheme;
		int otClassFlags;
		boolean attributesRead;
		public ClassAndAttributesReader(byte[] classFile) {
			super(classFile);
		}
		public ClassAndAttributesReader(InputStream classStream) throws IOException {
			super(classStream);
		}
		void readOTAttributes() {
			if (attributesRead)
				return;
			attributesRead = true;
			char[] charBuffer = new char[MAX_ATTR_NAME_LEN];

		    int currentAttributeOffset = myGetFirstAttributeOffset();
		    int found = 0;
		    for (int i = readUnsignedShort(currentAttributeOffset - 2); i > 0; --i) {
		      // Read the attribute_info's attribute_name and attribute_length fields.
		      String attributeName = myReadUTF8(currentAttributeOffset, charBuffer);
		      int attributeLength = readInt(currentAttributeOffset + 2);
		      currentAttributeOffset += 6;
		      if (Attributes.ATTRIBUTE_OT_CLASS_FLAGS.equals(attributeName)) {
		    	  otClassFlags = readUnsignedShort(currentAttributeOffset);
		    	  found++;
		      } else if (Attributes.ATTRIBUTE_OT_COMPILER_VERSION.equals(attributeName)) {
		    	  int encodedVersion  = readUnsignedShort(currentAttributeOffset);
		    	  weavingScheme = ((encodedVersion & Attributes.OTDRE_FLAG) != 0) ? WeavingScheme.OTDRE : WeavingScheme.OTRE;
		    	  found++;
		      }
		      if (found == 2)
		    	  break;
		      currentAttributeOffset += attributeLength;
		    }
		}
		String myReadUTF8(int offset, char[] charBuffer) {
			try {
				return readUTF8(offset, charBuffer);
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				return "<name is too long>";
			}
		}
		/* copy of inaccessible method: */
		int myGetFirstAttributeOffset() {
			int currentOffset = header + 8 + readUnsignedShort(header + 6) * 2;
			int fieldsCount = readUnsignedShort(currentOffset);
			currentOffset += 2;
			while (fieldsCount-- > 0) {
				int attributesCount = readUnsignedShort(currentOffset + 6);
				currentOffset += 8;
				while (attributesCount-- > 0) {
					currentOffset += 6 + readInt(currentOffset + 2);
				}
			}
			int methodsCount = readUnsignedShort(currentOffset);
			currentOffset += 2;
			while (methodsCount-- > 0) {
				int attributesCount = readUnsignedShort(currentOffset + 6);
				currentOffset += 8;
				while (attributesCount-- > 0) {
					currentOffset += 6 + readInt(currentOffset + 2);
				}
			}
			return currentOffset + 2;
		}
	}

	public static class ClassInformation {
		private int modifiers;
		private String name;
		private String superClassName;
		private String[] superInterfaceNames;
		private ClassAndAttributesReader reader;
		private Boolean isOTClass;
		private @NonNull WeavingScheme weavingScheme = WeavingScheme.Unknown;

		ClassInformation(ClassAndAttributesReader classReader) {
			this.modifiers = classReader.getAccess();
			this.name = classReader.getClassName();
			this.superClassName = classReader.getSuperName();
			this.superInterfaceNames = classReader.getInterfaces();
			this.reader = classReader;
		}

		public boolean isOTClass() {
			if (isOTClass == null) {
				reader.readOTAttributes();
				isOTClass = (reader.otClassFlags & (Types.ROLE_FLAG | Types.TEAM_FLAG)) != 0;
			}
			return isOTClass;
		}

		public @NonNull WeavingScheme getWeavingScheme() {
			if (this.weavingScheme == WeavingScheme.Unknown) {
				evaluateAttributes();
			}
			return this.weavingScheme;
		}

		private void evaluateAttributes() {
			if (reader != null) {
				reader.readOTAttributes();
				WeavingScheme scheme = reader.weavingScheme;
				if (scheme != null)
					this.weavingScheme = scheme;
				this.isOTClass = (reader.otClassFlags & (Types.ROLE_FLAG | Types.TEAM_FLAG)) != 0;
				reader = null; // release the bytes etc.
			}
		}
		public boolean isInterface() {
			return (modifiers & Opcodes.ACC_INTERFACE) != 0;
		}

		public String getSuperClassName() {
			if (superClassName != null)
				return superClassName.replace('/', '.');
			return null;
		}
		
		public String[] getSuperInterfaceNames() {
			if (superInterfaceNames != null)
				for (int i = 0; i < superInterfaceNames.length; i++) {
					superInterfaceNames[i] = superInterfaceNames[i].replace('/', '.');
				}
			return superInterfaceNames;
		}

		public String getName() {
			if (this.name != null)
				return this.name.replace('/', '.');
			return "<no name>";
		}
	}

	private Map<String, ClassInformation> classInformationMap =
			new ConcurrentHashMap<String, ClassInformation>(512, 0.75f, 4);

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
		ClassAndAttributesReader classReader = classBytes != null ? new ClassAndAttributesReader(classBytes) : new ClassAndAttributesReader(classStream);
		classInformation = new ClassInformation(classReader);
		classInformationMap.put(className, classInformation);
		return classInformation;
	}

	public @NonNull WeavingScheme determineWeavingScheme(byte[] classBytes, String className) {
		try {
			ClassInformation classInformation = getClassInformation(classBytes, null, className);
			return classInformation.getWeavingScheme();
		} catch (IOException e) {
			// ignore
		}
		return WeavingScheme.Unknown;
	}
	public @NonNull WeavingScheme determineWeavingScheme(InputStream classStream, String className) {
		try {
			ClassInformation classInformation = getClassInformation(null, classStream, className);
			return classInformation.getWeavingScheme();
		} catch (IOException e) {
			// ignore
		}
		return WeavingScheme.Unknown;
	}
}
