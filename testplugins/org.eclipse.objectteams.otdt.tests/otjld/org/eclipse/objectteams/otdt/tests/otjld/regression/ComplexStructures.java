/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2013 GK Software AG.
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
package org.eclipse.objectteams.otdt.tests.otjld.regression;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class ComplexStructures extends AbstractOTJLDTest {
	
	public ComplexStructures(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which do not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "testBug391876"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return ComplexStructures.class;
	}
	
	public void testRoleCtor01() {
		runConformTest(
			new String[] {
				"SomeTeam.java",
				"public team class SomeTeam {\n" + 
				"\n" + 
				"	public class BGraph {\n" + 
				"		public BGraph() { }\n" + 
				"	}\n" + 
				"\n" + 
				"\n" + 
				"	public void test() {\n" + 
				"		final Features f = new Features();\n" + 
				"		BasicGraph<@f> g3 = f.newBasicGraphException();\n" + 
				"		g3.print();\n" + 
				"	}\n" + 
				"\n" + 
				"	public team class Features {\n" + 
				"		protected BasicGraph newBasicGraphException() {\n" + 
				"			return new BasicGraph();\n" + 
				"		}\n" + 
				"\n" + 
				"		public team class BasicGraph playedBy BGraph {\n" + 
				"			public BasicGraph() { base(); }\n" + 
				"\n" + 
				"			public void print() {\n" + 
				"				System.out.println(\"I am a basic graph ...\");\n" + 
				"			}\n" + 
				"		}\n" + 
				"	}\n" + 
				"\n" + 
				"	public static void main(String[] args) {\n" + 
				"		new SomeTeam().test();\n" + 
				"	}\n" + 
				"}\n"
			},
			"I am a basic graph ...");
	}
}
