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
package org.eclipse.objectteams.otdt.tests.otjld.regression;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class CompilationOrder extends AbstractOTJLDTest {

	public CompilationOrder(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "testB22_twoToplevelTeams1"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return CompilationOrder.class;
	}
    // @bug https://bugs.eclipse.org/303474 -  [compiler] Fragile compilation for circularly referenced classes
	// plus https://bugs.eclipse.org/310398 -  [compiler] incremental compilation fails for mutually dependent teams
    public void testB21_circularDependency1() {
    	this.compileOrder = new String[][]{{"A.java", "B.java"}, {"TheTest.java"}};
        runConformTest(
            new String[] {
		"TheTest.java",
				"public class TheTest {\n" +
				"        final B b = new B();\n" +
				"        final A a = new A<@b>();\n" +
				"\n" +
				"        public void run() {\n" +
				"                within(b) {\n" +
				"                        a.run();\n" +
				"                }\n" +
				"        }\n" +
				"        public static void main(String[] args) {\n" +
				"			new TheTest().run();\n" +
				"		}\n" +
				" }",
		"A.java",
			    "public team class A<B b> {\n" +
				"        public void run() {\n" +
				"                System.out.println(\"A.run()\");\n" +
				"        }\n" +
				"        public class Q playedBy R<@b> {\n" +
				"                @SuppressWarnings(\"decapsulation\")\n" +
				"                void run() <- replace void run();\n" +
				"                // 1. originally the compiler forced to declare \'Object run()\'\n" +
				"                // 2. compile process is fragile with circular references, like in this exercise\n" +
				"                callin void run() {\n" +
				"                        base.run();\n" +
				"                        System.out.println(\"callin: Q.run()\");\n" +
				"                }\n" +
				"        }       \n" +
				"}",
		"B.java",
				"public team class B {\n" +
				"        public class R playedBy A {\n" +
				"                void run() <- replace void run();\n" +
				"                callin void run() {\n" +
				"                        base.run();\n" +
				"                        System.out.println(\"R.run()\");\n" +
				"                }\n" +
				"        }       \n" +
				"}"
            },
            "A.run()\nR.run()");
    }

    // basic case, no error observed
    public void testB22_twoToplevelTeams1() {
    	runConformTest(
    		new String[] {
    	"TeamB22ttt1.java",
    			"public team class TeamB22ttt1 {\n" +
    			"	protected team class R {\n" +
    			"		protected class InnerRole {}\n" +
    			"		protected void test() {\n" +
    			"			System.out.print(\"OK\");\n" +
    			"		}\n" +
    			"	}\n" +
    			"	public void test() {\n" +
    			"		new R().test();\n" +
    			"	}\n" +
    			"	public static void main(String... args) {\n" +
    			"		new TeamB22ttt1_1().test();\n" +
    			"	}\n" +
    			"}\n" +
    			"team class TeamB22ttt1_1 extends TeamB22ttt1 {\n" +
    			"	protected team class R {}\n" +
    			"}\n"
    		},
    		"OK");
    }

    // with two nested role-teams, witness for InternalCompilerError("Class file was not yet written to disk")
    public void testB22_twoToplevelTeams2() {
    	runConformTest(
    		new String[] {
    	"TeamB22ttt2.java",
    			"public team class TeamB22ttt2 {\n" +
    			"	protected team class R1 {\n" +
    			"		protected void test() {\n" +
    			"			System.out.print(\"OK\");\n" +
    			"		}\n" +
    			"	}\n" +
    			"	protected team class R2 extends R1 {\n" +
    			"		" +
    			"	}\n" +
    			"	public void test() {\n" +
    			"		new R2().test();\n" +
    			"	}\n" +
    			"	public static void main(String... args) {\n" +
    			"		new TeamB22ttt2_1().test();\n" +
    			"	}\n" +
    			"}\n" +
    			"team class TeamB22ttt2_1 extends TeamB22ttt2 {\n" +
    			"	protected team class R1 {}\n" +
    			"	protected team class R2 extends R1 {\n" +
    			"		" +
    			"	}\n" +
    			"}\n"
    		},
    		"OK");
    }
    // Bug 343594 - [compiler] VerifierError when synthetic accessor is copied during incremental build
    public void testB22_synthAccessorCopiedFromBinary1() {
    	compileOrder = new String[][] {{"TeamB22sacfb1_1.java"}, {"TeamB22sacfb1_2.java"}};
    	runConformTest(
    		new String[] {
	    	"TeamB22sacfb1_2.java",
	    		"public team class TeamB22sacfb1_2 extends TeamB22sacfb1_1 {\n" +
	    		"    protected team class Mid {\n" +
	    		"        protected void test() {\n" +
	    		"            new R().foo();\n" +
	    		"        }\n" +
	    		"    }\n" +
	    		"    void run() {\n" +
	    		"        new Mid().test();\n" +
	    		"    }\n" +
	    		"    public static void main(String... args) {\n" +
	    		"        new TeamB22sacfb1_2().run();\n" +
	    		"    }\n" +
	    		"}\n",
	    	"TeamB22sacfb1_1.java",
    			"public team class TeamB22sacfb1_1 {\n" +
    			"    protected team class Mid {\n" +
    			"        protected class R {\n" +
    			"            protected void foo() {\n" +
    			"                TeamB22sacfb1_1.this.huray();\n" +
    			"            }\n" +
    			"        }\n" +
    			"    }\n" +
    			"    void huray() {\n" +
    			"        System.out.print(\"OK\");" +
    			"    }\n" +
    			"}\n"
    		},
    		"OK");
    }
    public void testBug550408() {
    	runConformTest(
    		new String[] {
		"Base550408.java",
				"public class Base550408 {\n" +
				"	public void bm() {}\n" +
				"}\n",
    	"Team550408_1.java",
    			"public team class Team550408_1 {\n" +
    			"	protected class R playedBy Base550408 {\n" +
    			"		protected void test() {\n" +
    			"			System.out.print(\"OK\");\n" +
    			"		}\n" +
    			"		test <- after bm;\n" +
    			"	}\n" +
    			"}\n",
    		},
    		"");
    	Runner runner = new Runner();
    	runner.shouldFlushOutputDirectory = false;
    	runner.testFiles = new String[] {
    		"Base550408_2.java",
    			"public class Base550408_2 {\n" +
    			"	public void bm2() {}\n" +
    			"}\n",
			"Team550408_2.java",
    			"team class Team550408_2 {\n" +
				"	protected team class Mid extends Team550408_1 playedBy Base550408_2 {\n" +
    			"		protected void test2() {\n" +
    			"			System.out.print(\"OK\");\n" +
    			"		}\n" +
    			"		test2 <- after bm2;\n" +
				"	}\n" +
				"}\n"
    		};
    	runner.runConformTest();
    }
}
