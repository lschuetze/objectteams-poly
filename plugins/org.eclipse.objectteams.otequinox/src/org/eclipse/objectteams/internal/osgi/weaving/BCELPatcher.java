/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2013 GK Software AG
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
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.otequinox.TransformerPlugin.log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.CRC32;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.objectteams.otequinox.TransformerPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.hooks.weaving.WovenClass;

public class BCELPatcher {

	static final String BCEL_PLUGIN_ID = "org.apache.bcel";
	private static final String BCEL_PATH_DIR = "bcelpatch/";

	public static void fixBCEL(WovenClass clazz) {
		String name = clazz.getClassName();
		byte[] classbytes = clazz.getBytes();
		boolean shouldPatch = false;
		if ("org.apache.bcel.generic.InstructionHandle".equals(name)) {
			CRC32 crc32 = new CRC32();
			crc32.update(classbytes);
			long crc = crc32.getValue();
			String detail = "";
			if (classbytes.length != 0x1623)
				detail+="\n\tlength="+classbytes.length;	// identify original class
			if (crc != 0x42132087 && crc != 0xdb1b9859L)	// --""-- (I've seen two versions of this class file, semantically equivalent though)
				detail+="\n\tcrc="+crc;
			if (classbytes[0x0F00] != 0x18)
				detail+="\n\tmodifiers of getInstructionHandle="+classbytes[0xF00];	// modifiers of method getInstructionHandle at "static final"
			if (classbytes[0x105C] != 0x04)
				detail+="\n\tmodifiers of addHandle="+classbytes[0x105C];			// modifiers of method getHandle at "protected"
			if (detail.length() == 0) {
				shouldPatch = true;
			} else {
				log(IStatus.WARNING, "Class org.apache.bcel.generic.InstructionHandle needs a hot-patch but has unexpected byte code:"+detail);
			}
		} else if ("org.apache.bcel.generic.BranchHandle".equals(name)) {
			CRC32 crc32 = new CRC32();
			crc32.update(classbytes);
			long crc = crc32.getValue();
			String detail = "";
			if (classbytes.length != 0x09F1)
				detail+="\n\tlength="+classbytes.length;	// identify original class
			if (crc != 0xd3c37c19L && crc != 0x74bee71eL)	// --""-- (I've seen two versions of this class file, semantically equivalent though)
				detail+="\n\tcrc="+crc;
			if (classbytes[0x067E] != 0x18)
				detail+="\n\tmodifiers of getBranchHandle="+classbytes[0x067E];	// modifiers of method getBranchHandle at "static final"
			if (classbytes[0x06F8] != 0x04)
				detail+="\n\tmodifiers of addHandle="+classbytes[0x06F8];		// modifiers of method getHandle at "protected"
			if (detail.length() == 0) {
				shouldPatch = true;
			} else {
				log(IStatus.WARNING, "Class org.apache.bcel.generic.BranchHandle needs a hot-patch but has unexpected byte code:"+detail);
			}
		}
		if (shouldPatch) {
			Bundle otequinoxBundle = TransformerPlugin.getBundle();
			URL entry = otequinoxBundle.getEntry(BCEL_PATH_DIR+name+".class");
			try (InputStream stream = entry.openStream()) {
				int len = stream.available();
				byte[] newBytes = new byte[len];
				stream.read(newBytes);
				log(IStatus.INFO, "hot-patched a bug in class "+name+"\n"+
									"\tsee https://bugs.eclipse.org/bugs/show_bug.cgi?id=344350");
				clazz.setBytes(newBytes);
			} catch (IOException e) {
				log(e, "Failed to hot-patch bcel class "+name);
			}
		}
	}
}
