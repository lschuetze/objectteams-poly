/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 * 
 * Copyright 2005-2008 Berlin Institute of Technology, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: JPLISEnhancer.java 23408 2010-02-03 18:07:35Z stephan $
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * Berlin Institute of Technology - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otre.jplis;


import java.io.IOException;
import java.io.InputStream;

import org.eclipse.objectteams.otre.ClassEnhancer;
import org.eclipse.objectteams.otre.ClassLoaderAccess;
import org.eclipse.objectteams.otre.ObjectTeamsTransformation;
import org.objectteams.OTREInternalError;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;


/**
* This class implements the ClassEnhancer interface with the JPLIS (Java5) specific behavior.
*  
* @author Christine Hundt
* @author Juergen Widiker
* @author Stephan Herrmann
*/

public class JPLISEnhancer implements ClassEnhancer {

	private Object loader;
	
	public JPLISEnhancer(ClassGen cg, Object loader) {
		this.loader = loader;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.objectteams.otre.ClassEnhancer#addImplements(java.lang.String, org.apache.bcel.generic.ClassGen)
	 */
	public void addImplements(String interfaceName, ClassGen cg) {
		cg.addInterface(interfaceName);		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.objectteams.otre.common.ClassEnhancer#addMethod(org.apache.bcel.classfile.Method, de.fub.bytecode.generic.ClassGen)
	 */
	public void addMethod(Method m, ClassGen cg) {
		if (cg.containsMethod(m.getName(), m.getSignature()) != null)
			new OTREInternalError("Warning: repetive adding of method "
					           + m.getName() + m.getSignature() 
							   + " to class " + cg.getClassName())
						.printStackTrace();
		cg.addMethod(m);
		requireClassFileVersionLessThan51(cg);
	}

	public void addOrReplaceMethod(Method method, ClassGen cg) {
		Method existingMethod = cg.containsMethod(method.getName(), method.getSignature());
		if (existingMethod == null)
			addMethod(method, cg);
		else
			cg.replaceMethod(existingMethod, method);
		requireClassFileVersionLessThan51(cg);
	}

	public static void requireClassFileVersionLessThan51(ClassGen cg) {
		// added methods would be invalid without a stackmap attribute,
		// work around this by setting the class file to 50.0 (Java6),
		// where the old verifier is still supported as a fallback.
		if (cg.getMajor() >= 51)
			cg.setMajor(50);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.objectteams.otre.common.ClassEnhancer#addField(org.apache.bcel.classfile.Field, de.fub.bytecode.generic.ClassGen)
	 */
	public void addField(Field f, ClassGen cg) {
		if (cg.containsField(f.getName()) != null)
			new OTREInternalError("Warning: repetitive adding of field "
					           + f.getName() + f.getSignature() 
							   + " to class " + cg.getClassName())
						.printStackTrace();
		cg.addField(f);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.objectteams.otre.common.ClassEnhancer#loadClass(java.lang.String)
	 */
	public void loadClass(String className, ObjectTeamsTransformation client) {
		// SH: no forced class loading within OT/Equinox
		if(System.getProperty("ot.equinox") != null)
			return;
		InputStream is = null;
		try {
			String binaryName = className.replace('.', '/');
			is = ClassLoaderAccess.getResourceAsStream(this.loader, binaryName+".class");
			if (is != null) {
				ClassGen cg = new ClassGen(new ClassParser(is, className).parse());
				client.checkReadClassAttributes(this, cg, className, cg.getConstantPool());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					// nothing we can do
				}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.objectteams.otre.common.ClassEnhancer#decapsulateMethod(org.apache.bcel.classfile.Method, java.lang.String, de.fub.bytecode.generic.ConstantPoolGen)
	 */
	public void decapsulateMethod(Method m, ClassGen cg, String packageName, ConstantPoolGen cpg) {
		String className = cg.getClassName();
		int flags = m.getAccessFlags();
		MethodGen mg = new MethodGen(m, className, cpg); // no need to remove attributes, code remains unchanged

		if ((flags & Constants.ACC_PUBLIC) == 0) {
			int newFlags = flags;
			newFlags &= ~(Constants.ACC_PROTECTED|Constants.ACC_PRIVATE); // clear old visibility
			newFlags |= Constants.ACC_PUBLIC;                             // set new visibility
			mg.setAccessFlags(newFlags);
            if(System.getProperty("ot.log") != null)
                ObjectTeamsTransformation.printLogMessage("Adjusting from "
										+ Utility.accessToString(flags)
										+ " to public:\n\t"
										+ className
                        				+ "." + m);
			if (!packageName.equals("NO_PACKAGE")) 
				checkSeal(packageName, className);
		}
		cg.replaceMethod(m, mg.getMethod());
	}
	
	private static void checkSeal(String package_name, String class_name) {
		Package pckg = Package.getPackage(package_name);
		if ( (pckg != null) && pckg.isSealed()) 
			throw new IllegalAccessError(
					"OT/J callout binding:\n"
					+"Trying to break sealed "+pckg);
	}
}
