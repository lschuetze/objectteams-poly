/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2010 Stephan Herrmann
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.rolesandteams;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class RelevantRole extends AbstractOTJLDTest {

	public RelevantRole(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "testA120_enumInTeam1", "testA120_enumInTeam2"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return RelevantRole.class;
	}

    // a role implicitly inherits from an abstract relevant role but has a different constructor
    // 1.8.1-otjld-refining-abstract-role-1
    public void test181_refiningAbstractRole1() {

       runConformTest(
            new String[] {
		"Team181rar1_2.java",
			    "\n" +
			    "public team class Team181rar1_2 extends Team181rar1_1 {\n" +
			    "	protected class Role {\n" +
			    "		public Role(int i) {};\n" +
			    "		protected void print() {\n" +
			    "			System.out.print(\"OK\");\n" +
			    "		}\n" +
			    "	}\n" +
			    "	Team181rar1_2 () {\n" +
			    "		test();\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team181rar1_2();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team181rar1_1.java",
			    "\n" +
			    "public abstract team class Team181rar1_1 {\n" +
			    "	protected abstract class Role {// only default constructor\n" +
			    "		protected abstract void print();\n" +
			    "	}\n" +
			    "	void test() {\n" +
			    "		Role r = new Role();\n" +
			    "		r.print();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "OK");
    }

    // a role implicitly inherits from an abstract irrelevant role but has a different constructor
    // 1.8.1-otjld-refining-abstract-role-2
    public void test181_refiningAbstractRole2() {
        runConformTest(
            new String[] {
		"Team181rar2_1.java",
			    "\n" +
			    "public abstract team class Team181rar2_1 {\n" +
			    "	protected abstract class Role {// only default constructor\n" +
			    "	}\n" +
			    "	void test() {\n" +
			    "		// don't instantiate Role!\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"Team181rar2_2.java",
			    "\n" +
			    "public team class Team181rar2_2 extends Team181rar2_1 {\n" +
			    "	protected class Role {\n" +
			    "		public Role(int i) {};\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            });
    }

    // a role implicitly inherits from an abstract irrelevant role but has a different constructor due to a playedBy clause
    // 1.8.1-otjld-refining-abstract-role-3
    public void test181_refiningAbstractRole3() {
        runConformTest(
            new String[] {
		"Team181rar3_1.java",
			    "\n" +
			    "public abstract team class Team181rar3_1 {\n" +
			    "	protected abstract class Role {// only default constructor\n" +
			    "	}\n" +
			    "	void test() {\n" +
			    "		// don't instantiate Role!\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"T181rar3.java",
			    "\n" +
			    "public class T181rar3 {}	\n" +
			    "	\n",
		"Team181rar3_2.java",
			    "\n" +
			    "public team class Team181rar3_2 extends Team181rar3_1 {\n" +
			    "	protected class Role playedBy T181rar3 {}\n" +
			    "}\n" +
			    "	\n"
            });
    }

    // an abstract relevant role is inherited as a phantom
    // Bug 336420 - [compiler] NPE during reporting "abstract relevant role"
    public void test182_phantomAbstractRole1() {
    	runNegativeTest(
    		new String[] {
    	"Team182par1_2.java",
    			"public team class Team182par1_2 extends Team182par1_1 {\n" +
    			"}\n",
    	"Team182par1_1.java",
    			"public team class Team182par1_1 {\n" +
    			"    protected abstract class R playedBy T182par1 {}\n" +
    			"}\n",
    	"T182par1.java",
    			"public class T182par1 {}"
    		},
    		"----------\n" +
			"1. ERROR in Team182par1_2.java (at line 1)\n" +
			"	public team class Team182par1_2 extends Team182par1_1 {\n" +
			"	                                        ^^^^^^^^^^^^^\n" +
			"Team Team182par1_2 must be declared abstract, because abstract role R is relevant (OTJLD 2.5(b)).\n" +
    		"----------\n" +
    		"----------\n" +
    		"1. ERROR in Team182par1_1.java (at line 2)\n" +
    		"	protected abstract class R playedBy T182par1 {}\n" +
    		"	                         ^\n" +
    		"Team Team182par1_1 must be declared abstract, because abstract role R is relevant (OTJLD 2.5(b)).\n" +
    		"----------\n",
    		null, // classLibraries
    		true, // shouldFlushOutputDirectory
    		null, // customOptions
    		true, // generateOutput
    		false, // showCategory
    		true, // showWarningToken
    		true, // skipJavac
    		true // performStatementsRecovery
    	);
    	// run again, reusing binary inherited role:
    	runNegativeTest(
    		new String[] {
    	"Team182par1_2.java",
    			"public team class Team182par1_2 extends Team182par1_1 {\n" +
    			"}\n",
    		},
    		"----------\n" +
    		"1. ERROR in Team182par1_2.java (at line 1)\n" +
    		"	public team class Team182par1_2 extends Team182par1_1 {\n" +
    		"	                                        ^^^^^^^^^^^^^\n" +
    		"Team Team182par1_2 must be declared abstract, because abstract role R is relevant (OTJLD 2.5(b)).\n" +
    		"----------\n",
    		null, // classLibraries
    		false, // shouldFlushOutputDirectory
    		null, // customOptions
    		true, // generateOutput
    		false, // showCategory
    		true, // showWarningToken
    		true, // skipJavac
    		true // performStatementsRecovery
    	);
    }

    // an abstract potentially relevant role is inherited as a phantom
    // Bug 336420 - [compiler] NPE during reporting "abstract relevant role"
    public void test182_phantomAbstractRole2() {
    	runNegativeTest(
    		new String[] {
    	"Team182par2_2.java",
    			"public team class Team182par2_2 extends Team182par2_1 {\n" +
    			"}\n",
    	"Team182par2_1.java",
    			"public team class Team182par2_1 {\n" +
    			"    protected abstract class R playedBy T182par2 {}\n" +
    			"}\n",
    	"T182par2.java",
    			"public abstract class T182par2 {}"
    		},
    		"----------\n" +
			"1. WARNING in Team182par2_2.java (at line 1)\n" +
			"	public team class Team182par2_2 extends Team182par2_1 {\n" +
			"	                                        ^^^^^^^^^^^^^\n" +
			"[@sup:abstractrelevantrole] Team Team182par2_2 may need to be declared abstract, because irrelevance for abstract role R could not be shown (OTJLD 2.5(b)).\n" +
    		"----------\n" +
    		"----------\n" +
    		"1. WARNING in Team182par2_1.java (at line 2)\n" +
    		"	protected abstract class R playedBy T182par2 {}\n" +
    		"	                         ^\n" +
    		"[@sup:abstractrelevantrole] Team Team182par2_1 may need to be declared abstract, because irrelevance for abstract role R could not be shown (OTJLD 2.5(b)).\n" +
    		"----------\n",
    		null, // classLibraries
    		true, // shouldFlushOutputDirectory
    		null, // customOptions
    		true, // generateOutput
    		false, // showCategory
    		true, // showWarningToken
    		true, // skipJavac
    		true // performStatementsRecovery
    	);
    	// run again, reusing binary inherited role:
    	runNegativeTest(
    		new String[] {
    	"Team182par2_2.java",
    			"public team class Team182par2_2 extends Team182par2_1 {\n" +
    			"}\n",
    		},
    		"----------\n" +
    		"1. WARNING in Team182par2_2.java (at line 1)\n" +
    		"	public team class Team182par2_2 extends Team182par2_1 {\n" +
    		"	                                        ^^^^^^^^^^^^^\n" +
    		"[@sup:abstractrelevantrole] Team Team182par2_2 may need to be declared abstract, because irrelevance for abstract role R could not be shown (OTJLD 2.5(b)).\n" +
    		"----------\n",
    		null, // classLibraries
    		false, // shouldFlushOutputDirectory
    		null, // customOptions
    		true, // generateOutput
    		false, // showCategory
    		true, // showWarningToken
    		true, // skipJavac
    		true // performStatementsRecovery
    	);
    }
}
