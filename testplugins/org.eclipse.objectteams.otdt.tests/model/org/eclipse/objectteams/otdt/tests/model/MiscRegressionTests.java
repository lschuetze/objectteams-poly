/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 GK Software AG
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
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.model;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
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

	// https://bugs.eclipse.org/407223 - [compiler] role file found in a supposed default package confuses the compiler
	public void testRoleFileOutsideBuildPath()  throws JavaModelException {
		IJavaElement cu = JavaCore.create(getProject("Regression").getFolder("nosrc").getFolder("T").getFile("R.java"));
		ICompilationUnit wc = ((ICompilationUnit)cu);
		wc.becomeWorkingCopy(null);
		ITypeHierarchy hierarchy = wc.getType("R").newSupertypeHierarchy(null);
		IType[] allClasses = hierarchy.getAllClasses();
		assertNotNull(allClasses);
		assertEquals(2, allClasses.length); // R and Object
	}
}
