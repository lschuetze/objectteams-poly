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
 * This file is based on class org.apache.bcel.classfile.ConstantPool
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

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantUtf8;

/**
 * Simple gateway to class {@link org.apache.bcel.classfile.ConstantPool}
 * whose constructor {@link org.apache.bcel.classfile.ConstantPool#ConstantPool(DataInputStream)}
 * is not visible, unfortunately.
 */
@SuppressWarnings("serial")
public class ConstantPool extends org.apache.bcel.classfile.ConstantPool {
    
	static Constant DONT_CARE_CONSTANT = new ConstantInteger(13);
	static Constant DONT_CARE_CONSTANT_WIDE = new ConstantLong(42);

	ConstantPool(DataInputStream file) throws IOException {
		super(readConstants(file));
    }
	static Constant[] readConstants(DataInputStream file) throws IOException {		
		byte tag;
		int constant_pool_count = file.readUnsignedShort();
		Constant[] constant_pool = new Constant[constant_pool_count];
		/* constant_pool[0] is unused by the compiler and may be used freely
		 * by the implementation.
		 */
		for (int i = 1; i < constant_pool_count; i++) {
			constant_pool[i] = readConstant(file);
			/* Quote from the JVM specification:
			 * "All eight byte constants take up two spots in the constant pool.
			 * If this is the n'th byte in the constant pool, then the next item
			 * will be numbered n+2"
			 * 
			 * Thus we have to increment the index counter.
			 */
			tag = constant_pool[i].getTag();
			if ((tag == Constants.CONSTANT_Double) || (tag == Constants.CONSTANT_Long)) {
				i++;
			}
		}
		return constant_pool;
	}

	static final Constant readConstant(DataInputStream file)
			throws IOException, ClassFormatException {
		byte b = file.readByte(); // Read tag byte
		switch (b) {
		case Constants.CONSTANT_Class:
			return new ConstantClass(file.readUnsignedShort());
		case Constants.CONSTANT_Fieldref:
			return new ConstantFieldref(file.readUnsignedShort(), file.readUnsignedShort());
		case Constants.CONSTANT_Methodref:
			return new ConstantMethodref(file.readUnsignedShort(), file.readUnsignedShort());
		case Constants.CONSTANT_InterfaceMethodref:
			return new ConstantInterfaceMethodref(file.readUnsignedShort(), file.readUnsignedShort());
		case Constants.CONSTANT_String:
			file.readUnsignedShort(); break;
		case Constants.CONSTANT_Integer:
			file.readInt(); break;
		case Constants.CONSTANT_Float:
			file.readFloat(); break;
		case Constants.CONSTANT_Long:
			file.readLong(); return DONT_CARE_CONSTANT_WIDE;
		case Constants.CONSTANT_Double:
			file.readDouble(); return DONT_CARE_CONSTANT_WIDE;
		case Constants.CONSTANT_NameAndType:
			return new ConstantNameAndType(file.readUnsignedShort(), file.readUnsignedShort());
		case Constants.CONSTANT_Utf8:
			return new ConstantUtf8(file.readUTF());
		// new in 1.7:
		case 15: // CONSTANT_MethodHandle
			file.readByte(); 			// reference_kind
			file.readUnsignedShort();	// reference_index
			break;
		case 16: // CONSTANT_MethodType
			file.readUnsignedShort();	// descriptor_index
			break;
		case 18: // CONSTANT_InvokeDynamic
			file.readUnsignedShort();	// bootstrap_method_attr_index
			file.readUnsignedShort();	// name_and_type_index;
			break;
		default:
			throw new ClassFormatException(
					"Invalid byte tag in constant pool: " + b);
		}
		return DONT_CARE_CONSTANT;
	}
}
