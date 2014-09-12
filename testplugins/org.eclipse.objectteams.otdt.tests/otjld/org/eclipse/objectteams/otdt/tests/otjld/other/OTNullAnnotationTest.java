/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2014 Stephan Herrmann
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
package org.eclipse.objectteams.otdt.tests.otjld.other;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDNullAnnotationTest;

public class OTNullAnnotationTest extends AbstractOTJLDNullAnnotationTest {
	public OTNullAnnotationTest(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which do not belong to the class are skipped...
	static {
//			TESTS_NAMES = new String[] { "testExplicitTeamAnchor1" };
//			TESTS_NUMBERS = new int[] { 561 };
//			TESTS_RANGE = new int[] { 1, 2049 };
	}

	public static Test suite() {
		return buildMinimalComplianceTestSuite(testClass(), F_1_5);
	}

	public static Class testClass() {
		return OTNullAnnotationTest.class;
	}
	
	public void testNullableBase() {
		runNegativeTestWithLibs(
			new String[] {
				"bug443299a/MyTeam.java",
				"package bug443299a;\n" +
				"public team class MyTeam {\n" +
				"	protected class MyRole playedBy MyBase {}\n" +
				"	void test() {\n" +
				"		new MyRole(null);\n" +
				"	}\n" +
				"}\n",
				"bug443299a/MyBase.java",
				"package bug443299a;\n" +
				"public class MyBase {}\n"
			}, 
			getCompilerOptions(),
			"----------\n" + 
			"1. WARNING in bug443299a\\MyTeam.java (at line 5)\n" + 
			"	new MyRole(null);\n" + 
			"	^^^^^^^^^^^^^^^^\n" + 
			"Argument to lifting constructor MyRole(MyBase) is not a freshly created base object (of type bug443299a.MyBase); may cause a DuplicateRoleException at runtime (OTJLD 2.4.1(c)).\n" + 
			"----------\n" + 
			"2. ERROR in bug443299a\\MyTeam.java (at line 5)\n" + 
			"	new MyRole(null);\n" + 
			"	           ^^^^\n" + 
			"Null type mismatch: required \'@NonNull MyBase\' but the provided value is null\n" + 
			"----------\n");
	}
}
