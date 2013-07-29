/**
 * This file is part of "Object Teams Development Tooling"-Software.
 *
 * Copyright 2013 GK Software AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation.
 */
package org.eclipse.objectteams.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Strategy for handling woven class bytes.
 * Default implementation stores class files in a given output directory.
 */
public class Requestor {
	String outDir;
	
	/**
	 * Creates a requestor to store class files in the given directory.
	 * @param outDir the directory where class files shall be stored.
	 */
	public Requestor(String outDir) {
		this.outDir = outDir;
	}
	
	/**
	 * Callback for the {@link BuildTimeWeaver}: a class has been effectively transformed.
	 * @param className class name in binary form ('/'-separated).
	 * @param classBytes the transformed class bytes
	 * @throws IOException if something went wrong writing the file...
	 */
	public void accept(String className, byte[] classBytes) throws IOException {
		File outFile = new File(outDir+File.separator+className);
		File parent = outFile.getParentFile();
		if (!parent.exists())
			parent.mkdirs();
		outFile.createNewFile();
		try (FileOutputStream outStream = new FileOutputStream(outFile)) {
			outStream.write(classBytes);
		}		
	}
}
