/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 GK Software AG
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
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.model;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.AbstractJavaModelTests;

public class MiscRegressionTests extends AbstractJavaModelTests {
	protected static IJavaProject REGRESSION_PROJECT;

	public MiscRegressionTests(String name) {
		super(name);
	}
	public void setUpSuite() throws Exception {
		if (REGRESSION_PROJECT == null)  {
			REGRESSION_PROJECT = setUpJavaProject("Regression");
		} else {
			setUpProjectCompliance(REGRESSION_PROJECT, "1.5");
		}
		super.setUpSuite();
	}
	public void tearDownSuite() throws Exception {
		super.tearDownSuite();
	}
	static {
//		TESTS_NAMES = new String[] { "testInconsistentHierarchy1"};
	}
	public static Test suite() {
		if (true) {
			return buildModelTestSuite(MiscRegressionTests.class);
		}
		TestSuite suite = new Suite(MiscRegressionTests.class.getName());		

		suite.addTest(new MiscRegressionTests("testFoo"));			
		return suite;
	}

	// Bug 330304 -  [compiler] AIOOBE in Scanner.internalScanIdentifierOrKeyword()
	public void testBug330304() throws JavaModelException {
		assertTrue(JavaConventions.validateIdentifier("thsupe",CompilerOptions.VERSION_1_5,CompilerOptions.VERSION_1_5).isOK());
		assertTrue(JavaConventions.validateIdentifier("thrsup",CompilerOptions.VERSION_1_5,CompilerOptions.VERSION_1_5).isOK());
		assertTrue(JavaConventions.validateIdentifier("throsu",CompilerOptions.VERSION_1_5,CompilerOptions.VERSION_1_5).isOK());
	}
}
