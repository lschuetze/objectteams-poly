/**********************************************************************
 * This file is part of "Object Teams Runtime Environment"-Software
 * 
 * Copyright 2011 GK Software AG
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
 * 	  Stephan Herrmann - Initial API and implementation
 * 
 * This file is based on class org.apache.bcel.classfile.ClassParser
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
 */
package org.eclipse.objectteams.otre.bcel;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.JavaClass;

/**
 * Read only structural information from a .class file:
 * <ul>
 * <li>class name
 * <li>access flags
 * <li>names of super class and super interfaces
 * </ul>
 * Also discard the constant pool when done reading. 
 */
public final class DietClassParser {

    private DataInputStream file;
    private String file_name;
    private int class_name_index, superclass_name_index;
    private int access_flags; // Access rights of parsed class
    private boolean is_zip; // Loaded from zip file?
    private static final int BUFSIZE = 8192;


    /**
     * Parse class from the given stream.
     *
     * @param file Input stream
     * @param file_name File name
     */
    public DietClassParser(InputStream file, String file_name) {
        this.file_name = file_name;
        String clazz = file.getClass().getName(); // Not a very clean solution ...
        is_zip = clazz.startsWith("java.util.zip.") || clazz.startsWith("java.util.jar.");
        if (file instanceof DataInputStream) {
            this.file = (DataInputStream) file;
        } else {
            this.file = new DataInputStream(new BufferedInputStream(file, BUFSIZE));
        }
    }

    /**
     * Parse the given Java class file and return an object that represents
     * the contained data, i.e., constants, methods, fields and commands.
     * A <em>ClassFormatException</em> is raised, if the file is not a valid
     * .class file. (This does not include verification of the byte code as it
     * is performed by the java interpreter).
     *
     * @return Class object representing the parsed class file
     * @throws  IOException
     * @throws  ClassFormatException
     */
    public JavaClass parse() throws IOException, ClassFormatException {
        /****************** Read headers ********************************/
        // Check magic tag of class file
        if (file.readInt() != 0xCAFEBABE)
		    throw new ClassFormatException(file_name + " is not a Java .class file");
        // Get compiler version
        int minor = file.readUnsignedShort();
		int major = file.readUnsignedShort();
        /****************** Read constant pool and related **************/
        // Read constant pool entries
        ConstantPool constant_pool = new ConstantPool(file);
        // Get class information
        readClassInfo();
        // Get interface information, i.e., implemented interfaces
        int[] interfaces = readInterfaces();
        /****************** Don't read class fields and methods ***************/
        // Return the information we have gathered in a new object
        return new DietJavaClass(class_name_index, superclass_name_index, file_name, major, minor,
                access_flags, constant_pool, interfaces, 
                is_zip ? JavaClass.ZIP : JavaClass.FILE);
    }

    /**
     * Read information about the class and its super class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void readClassInfo() throws IOException {
        access_flags = file.readUnsignedShort();
        /* Interfaces are implicitely abstract, the flag should be set
         * according to the JVM specification.
         */
        if ((access_flags & Constants.ACC_INTERFACE) != 0) {
            access_flags |= Constants.ACC_ABSTRACT;
        }
        class_name_index = file.readUnsignedShort();
        superclass_name_index = file.readUnsignedShort();
    }

    /**
     * Read information about the interfaces implemented by this class.
     * @throws  IOException
     */
    private int[] readInterfaces() throws IOException {
        int interfaces_count = file.readUnsignedShort();
        int[] interfaces = new int[interfaces_count];
        for (int i = 0; i < interfaces_count; i++) {
            interfaces[i] = file.readUnsignedShort();
        }
        return interfaces;
    }
}
