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

import org.eclipse.jdt.core.tests.util.CompilerTestSetup;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDNullAnnotationTest;

/** Test combination of OT-types with null type annotations. */
public class OTNullTypeAnnotationTest extends AbstractOTJLDNullAnnotationTest {
	
	public OTNullTypeAnnotationTest(String name) {
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
		return buildMinimalComplianceTestSuite(testClass(), F_1_8);
	}

	public static Class testClass() {
		return OTNullTypeAnnotationTest.class;
	}
	
	@Override
	public void initialize(CompilerTestSetup setUp) {
		super.initialize(setUp);
		if (this.weavingScheme == WeavingScheme.OTRE)
			System.err.println("Running Java8 tests for OTRE weaver will skip most tests");
	}

	// Bug 437767 - [java8][null] semantically integrate null type annotations with anchored role types 
	// see comment 1
	public void testImplicitTeamAnchor1() {
		if (this.weavingScheme == WeavingScheme.OTRE) return;
		runConformTestWithLibs(
			new String[] {
				"MyTeam.java",
				"import org.eclipse.jdt.annotation.*;\n" +
				"import java.util.*;\n" +
				"public team class MyTeam {\n" +
				"	protected class MyRole {\n" +
				"		protected void print() {}\n" +
				"	}\n" +
				"	void test() {\n" + 
				"		List<@NonNull MyRole> someRoles = new ArrayList<>();\n" + 
				"		@NonNull MyRole role = someRoles.get(0);\n" + 
				"		role.print();\n" + 
				"	}\n" +
				"}\n"
			},
			getCompilerOptions(),
			"");
	}

	public void testImplicitTeamAnchor2() {
		if (this.weavingScheme == WeavingScheme.OTRE) return;
		runNegativeTestWithLibs(
			new String[] {
				"MyTeam.java",
				"import org.eclipse.jdt.annotation.*;\n" +
				"import java.util.*;\n" +
				"public team class MyTeam {\n" +
				"	protected class MyRole {\n" +
				"		protected void print() {}\n" +
				"	}\n" +
				"	void test() {\n" + 
				"		List<@Nullable MyRole> someRoles = new ArrayList<>();\n" + 
				"		@NonNull MyRole role = someRoles.get(0);\n" + 
				"		role.print();\n" + 
				"	}\n" +
				"}\n"
			},
			getCompilerOptions(),
			"----------\n" + 
			"1. ERROR in MyTeam.java (at line 9)\n" + 
			"	@NonNull MyRole role = someRoles.get(0);\n" + 
			"	                       ^^^^^^^^^^^^^^^^\n" + 
			"Null type mismatch (type annotations): required \'MyTeam.@NonNull MyRole\' but this expression has type \'MyTeam.@Nullable MyRole\'\n" + 
			"----------\n");
	}

	public void testExplicitTeamAnchor1() {
		if (this.weavingScheme == WeavingScheme.OTRE) return;
		runNegativeTestWithLibs(
			new String[] {
				"Main.java",
				"import org.eclipse.jdt.annotation.*;\n" +
				"import java.util.*;\n" +
				"public class Main {\n" +
				"	void test(final MyTeam t, List<@Nullable MyRole<@t>> someRoles) {\n" +
				"		@NonNull MyRole<@t> role = someRoles.get(0);\n" + 
				"		role.print();\n" + 
				"	}\n" +
				"}\n",
				"MyTeam.java",
				"public team class MyTeam {\n" +
				"	public class MyRole {\n" +
				"		public void print() {}\n" +
				"	}\n" +
				"}\n"
			},
			getCompilerOptions(),
			"----------\n" + 
			"1. ERROR in Main.java (at line 5)\n" + 
			"	@NonNull MyRole<@t> role = someRoles.get(0);\n" + 
			"	                           ^^^^^^^^^^^^^^^^\n" + 
			"Null type mismatch (type annotations): required \'MyTeam.@NonNull MyRole\' but this expression has type \'MyTeam.@Nullable MyRole\'\n" + 
			"----------\n");
	}

	public void testLiftingType1() {
		if (this.weavingScheme == WeavingScheme.OTRE) return;
		runNegativeTestWithLibs(
			new String[] {
				"MyTeam.java",
				"import org.eclipse.jdt.annotation.*;\n" +
				"public team class MyTeam {\n" +
				"	protected class MyRole playedBy MyBase {\n" +
				"		protected void print() {}\n" +
				"	}\n" +
				"	void test(@Nullable MyBase as @NonNull MyRole r) {}\n" +
				"}\n",
				"MyBase.java",
				"public class MyBase {}\n"
			},
			getCompilerOptions(),
			"----------\n" + 
			"1. ERROR in MyTeam.java (at line 6)\n" + 
			"	void test(@Nullable MyBase as @NonNull MyRole r) {}\n" + 
			"	                    ^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Null type mismatch (type annotations): required \'MyTeam.@NonNull MyRole\' but this expression has type \'@Nullable MyBase\'\n" + 
			"----------\n");
	}

	public void testLiftingType2() {
		if (this.weavingScheme == WeavingScheme.OTRE) return;
		runNegativeTestWithLibs(
			new String[] {
				"MyTeam.java",
				"import org.eclipse.jdt.annotation.*;\n" +
				"public team class MyTeam {\n" +
				"	protected class MyRole playedBy MyBase {\n" +
				"		protected void print() {}\n" +
				"	}\n" +
				"	void test1(MyBase @Nullable[] as MyRole @NonNull[] r) {}\n" +
				"	void test2(@Nullable MyBase @NonNull[] as @NonNull MyRole @NonNull[] r) {}\n" +
				"}\n",
				"MyBase.java",
				"public class MyBase {}\n"
			},
			getCompilerOptions(),
			"----------\n" + 
			"1. ERROR in MyTeam.java (at line 6)\n" + 
			"	void test1(MyBase @Nullable[] as MyRole @NonNull[] r) {}\n" + 
			"	           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Null type mismatch (type annotations): required \'MyTeam.MyRole @NonNull[]\' but this expression has type \'MyBase @Nullable[]\'\n" + 
			"----------\n" + 
			"2. ERROR in MyTeam.java (at line 7)\n" + 
			"	void test2(@Nullable MyBase @NonNull[] as @NonNull MyRole @NonNull[] r) {}\n" + 
			"	                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Null type mismatch (type annotations): required \'MyTeam.@NonNull MyRole @NonNull[]\' but this expression has type \'@Nullable MyBase @NonNull[]\'\n" + 
			"----------\n");
	}

}
