/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2010 IT Service Omikron GmbH and others.
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
 * 	  Thomas Dudziak - Initial API and implementation
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.callinbinding;

import java.io.File;
import java.util.Map;

import junit.framework.Test;

import org.eclipse.jdt.core.tests.util.Util;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

@SuppressWarnings("unchecked") // working with raw map
public class BaseCalls extends AbstractOTJLDTest {

	public BaseCalls(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test4516_inheritedBaseCall"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return BaseCalls.class;
	}

	@Override
	protected Map getCompilerOptions() {
		Map options = super.getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.WARNING); // many tests actually suppress this warning
		return options;
	}

    // a role method is callin-bound as 'replace' with a single base call
    // 4.5.1-otjld-single-base-call-1
    public void test451_singleBaseCall1() {

       runConformTest(
            new String[] {
		"T451sbc1Main.java",
			    "\n" +
			    "public class T451sbc1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team451sbc1 t = new Team451sbc1();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T451sbc1 o = new T451sbc1();\n" +
			    "\n" +
			    "        System.out.print(o.getValue(\"a\", \"b\"));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T451sbc1.java",
			    "\n" +
			    "public class T451sbc1 {\n" +
			    "    public String getValue(String arg1, String arg2) {\n" +
			    "        return arg1 + arg2;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team451sbc1.java",
			    "\n" +
			    "public team class Team451sbc1 {\n" +
			    "    public class Role451sbc1 playedBy T451sbc1 {\n" +
			    "        callin String getValue(String arg1, String arg2) {\n" +
			    "            return base.getValue(arg1+\"c\", arg2);\n" +
			    "        }\n" +
			    "        String getValue(String arg1, String arg2) <- replace String getValue(String arg1, String arg2) with {\n" +
			    "            arg1 <- arg2,\n" +
			    "            arg2 <- arg1,\n" +
			    "            result -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "abc");
    }

    // a role method is callin-bound as 'replace' with a single base call
    // 4.5.1-otjld-single-base-call-2
    public void test451_singleBaseCall2() {

       runConformTest(
            new String[] {
		"T451sbc2Main.java",
			    "\n" +
			    "public class T451sbc2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team451sbc2 t = new Team451sbc2();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T451sbc2 o = new T451sbc2();\n" +
			    "    \n" +
			    "            System.out.print(o.getValue(1.0, 2.0));\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T451sbc2.java",
			    "\n" +
			    "public class T451sbc2 {\n" +
			    "    public double getValue(double arg1, double arg2) {\n" +
			    "        return arg1 + arg2;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team451sbc2.java",
			    "\n" +
			    "public team class Team451sbc2 {\n" +
			    "    public class Role451sbc2 playedBy T451sbc2 {\n" +
			    "        callin double test(double arg) {\n" +
			    "            return base.test(2);\n" +
			    "        }\n" +
			    "        double test(double arg1) <- replace double getValue(double arg1, double arg2);\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "4.0");
    }

    // a role method is callin-bound as 'replace' with a single base call
    // 4.5.1-otjld_testbug-single-base-call-3
    public void _testbug_test451_singleBaseCall3() {
       // FIXME expect warning?
       runConformTest(
            new String[] {
		"T451sbc3Main.java",
			    "\n" +
			    "public class T451sbc3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team451sbc3_2 t2 = new Team451sbc3_2();\n" +
			    "\n" +
			    "        within (t2) {\n" +
			    "            Team451sbc3_1 t1 = new Team451sbc3_1();\n" +
			    "            T451sbc3      o  = new T451sbc3();\n" +
			    "\n" +
			    "            System.out.print(t1.test(o));\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T451sbc3.java",
			    "\n" +
			    "public class T451sbc3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team451sbc3_1.java",
			    "\n" +
			    "public team class Team451sbc3_1 {\n" +
			    "    public class Role451sbc3_1 playedBy T451sbc3 {\n" +
			    "        public String toString() {\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role451sbc3_1 getRole(T451sbc3 as Role451sbc3_1 obj) {\n" +
			    "        return obj;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test(Role451sbc3_1 arg) {\n" +
			    "        return arg.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team451sbc3_2.java",
			    "\n" +
			    "public team class Team451sbc3_2 {\n" +
			    "    public class Role451sbc3_2 playedBy Team451sbc3_1 {\n" +
			    "        callin String test(T451sbc3 obj) {\n" +
			    "            // argument is lowered\n" +
			    "            return obj.toString();\n" +
			    "        }\n" +
			    "        test <- replace test;\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "a");
    }

    // a role method is callin-bound as 'replace' with a single base call
    // 4.5.1-otjld-single-base-call-4
    public void test451_singleBaseCall4() {
        // FIXME expect warning?
       runConformTest(
            new String[] {
		"T451sbc4Main.java",
			    "\n" +
			    "public class T451sbc4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team451sbc4 t = new Team451sbc4();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T451sbc4_1 o1 = new T451sbc4_1();\n" +
			    "        T451sbc4_2 o2 = new T451sbc4_2();\n" +
			    "\n" +
			    "        System.out.print(o2.getValue(o1));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T451sbc4_1.java",
			    "\n" +
			    "public class T451sbc4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T451sbc4_2.java",
			    "\n" +
			    "public class T451sbc4_2 {\n" +
			    "    public String getValue(T451sbc4_1 arg) {\n" +
			    "        return arg.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team451sbc4.java",
			    "\n" +
			    "public team class Team451sbc4 {\n" +
			    "    public class Role451sbc4_1 playedBy T451sbc4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role451sbc4_2 playedBy T451sbc4_2 {\n" +
			    "        callin String test(Role451sbc4_1 obj) {\n" +
			    "            // argument is lifted\n" +
			    "            return obj.toString();\n" +
			    "        }\n" +
			    "        String test(Role451sbc4_1 obj) <- replace String getValue(T451sbc4_1 arg);\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "b");
    }

    // a role method is callin-bound as 'replace' with a single base call
    // 4.5.1-otjld-single-base-call-5
    public void test451_singleBaseCall5() {

       runConformTest(
            new String[] {
		"T451sbc5Main.java",
			    "\n" +
			    "public class T451sbc5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team451sbc5 t = new Team451sbc5();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T451sbc5 o = new T451sbc5();\n" +
			    "\n" +
			    "        System.out.print(o.getValue(\"OK\"));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T451sbc5.java",
			    "\n" +
			    "public class T451sbc5 {\n" +
			    "    public String test() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team451sbc5.java",
			    "\n" +
			    "public team class Team451sbc5 {\n" +
			    "    public class Role451sbc5 playedBy T451sbc5 {\n" +
			    "        callin String test() {\n" +
			    "            return base.test();\n" +
			    "        }\n" +
			    "        test <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // two callin methods have compatible but not identical signatures, each issueing a base call
    // 4.5.1-otjld-single-base-call-6
    public void test451_singleBaseCall6() {

       runConformTest(
            new String[] {
		"Team451sbc6.java",
			    "\n" +
			    "public team class Team451sbc6 {\n" +
			    "    protected class R playedBy T451sbc6 {\n" +
			    "        callin void rm(Object o) {\n" +
			    "            base.rm(o);\n" +
			    "            System.out.print(\"?\");\n" +
			    "        }\n" +
			    "        void rm(Object o) <- replace void test(Object o);\n" +
			    "        callin void rm(String s) {\n" +
			    "            base.rm(s.toUpperCase());\n" +
			    "        }\n" +
			    "        void rm(String s) <- replace void test(String s);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team451sbc6().activate();\n" +
			    "        T451sbc6 b = new T451sbc6();\n" +
			    "        b.test(\"o\");\n" +
			    "        Object a = \"K\";\n" +
			    "        b.test(a);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T451sbc6.java",
			    "\n" +
			    "public class T451sbc6 {\n" +
			    "    void test(Object o) {\n" +
			    "        System.out.print(\">\"+o);\n" +
			    "    }\n" +
			    "    void test(String s) {\n" +
			    "        System.out.print(\"!\"+s);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "!O>K?");
    }

    // a base call is unbound at the place of its declaration
    // 4.5.2-otjld-unbound-base-call-1
    public void test452_unboundBaseCall1() {

       runConformTest(
            new String[] {
		"Team452ubc1.java",
			    "\n" +
			    "public team class Team452ubc1 {\n" +
			    "    protected class R1 playedBy T452ubc1 {\n" +
			    "        callin String getValue() {\n" +
			    "            return base.getValue()+\"al\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected class R2 extends R1 {\n" +
			    "        getValue <- replace getVal;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team452ubc1().activate();\n" +
			    "        System.out.print(new T452ubc1().getVal());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T452ubc1.java",
			    "\n" +
			    "public class T452ubc1 {\n" +
			    "    String getVal() { return \"v\"; }\n" +
			    "}\n" +
			    "    \n"
            },
            "val");
    }

    // a base call is unbound at the place of its declaration - tsub overrides - tsubsub binds
    // 4.5.2-otjld-unbound-base-call-2
    public void test452_unboundBaseCall2() {

       runConformTest(
            new String[] {
		"Team452ubc2_3.java",
			    "\n" +
			    "public team class Team452ubc2_3 extends Team452ubc2_2 {\n" +
			    "    protected class R1 {\n" +
			    "        getValue <- replace getVal;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team452ubc2_3().activate();\n" +
			    "        System.out.print(new T452ubc2().getVal());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T452ubc2.java",
			    "\n" +
			    "public class T452ubc2 {\n" +
			    "    String getVal() { return \"v\"; }\n" +
			    "}\n" +
			    "    \n",
		"Team452ubc2_1.java",
			    "\n" +
			    "public team class Team452ubc2_1 {\n" +
			    "    protected class R1 {\n" +
			    "        callin String getValue() {\n" +
			    "            return base.getValue()+\"wrong\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team452ubc2_2.java",
			    "\n" +
			    "public team class Team452ubc2_2 extends Team452ubc2_1 {\n" +
			    "    protected class R1 playedBy T452ubc2 {\n" +
			    "        callin String getValue() {\n" +
			    "            return base.getValue()+\"al\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "val");
    }

    // a role method is callin-bound as 'replace' with a base call that uses the base method's real name, not the role method name
    // 4.5.3-otjld-illegal-base-call
    public void test453_illegalBaseCall() {
        runNegativeTestMatching(
            new String[] {
		"Team453ibc.java",
			    "\n" +
			    "public team class Team453ibc {\n" +
			    "    public class Role453ibc playedBy T453ibc {\n" +
			    "        callin String test() {\n" +
			    "            return base.getValue();\n" +
			    "        }\n" +
			    "        test <- replace getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T453ibc.java",
			    "\n" +
			    "public class T453ibc {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "4.3(a)");
    }

    // a role method is callin-bound as 'replace' with a base call that uses the base method without the base construct
    // 4.5.4-otjld-base-call-without-base
    public void test454_baseCallWithoutBase() {
        runNegativeTestMatching(
            new String[] {
		"Team454bcwb.java",
			    "\n" +
			    "public team class Team454bcwb {\n" +
			    "    public class Role454bcwb playedBy T454bcwb {\n" +
			    "        callin String test(String arg) {\n" +
			    "            return test(arg);\n" +
			    "        }\n" +
			    "        test <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T454bcwb.java",
			    "\n" +
			    "public class T454bcwb {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "4.2(d)");
    }

    // a role method is callin-bound, but not as 'replace', has a base call
    // 4.5.5-otjld-illegal-base-call-1
    public void test455_illegalBaseCall1() {
        runNegativeTestMatching(
            new String[] {
		"Team455ibc1.java",
			    "\n" +
			    "public team class Team455ibc1 {\n" +
			    "    public class Role455ibc1 playedBy T455ibc1 {\n" +
			    "        public String test(String arg) {\n" +
			    "            return base.test(arg);\n" +
			    "        }\n" +
			    "        test <- before test;\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T455ibc1.java",
			    "\n" +
			    "public class T455ibc1 {\n" +
			    "    public String test(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.6");
    }

    // a base call is attempted in a field initializer
    // 4.5.5-otjld-illegal-base-call-2
    public void test455_illegalBaseCall2() {
        runNegativeTestMatching(
            new String[] {
		"Team455ibc2.java",
			    "\n" +
			    "public team class Team455ibc2 {\n" +
			    "	protected class R playedBy T455ibc2 {\n" +
			    "		T455ibc2 b = base.other();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"T455ibc2.java",
			    "\n" +
			    "public class T455ibc2 {\n" +
			    "	public T455ibc2 other() { return new T455ibc2() ; }\n" +
			    "}	\n" +
			    "	\n"
            },
            "2.6");
    }

    // a base call in a callin method missing a return type
    // 4.5.5-otjld-illegal-base-call-3
    public void test455_illegalBaseCall3() {
        runNegativeTest(
            new String[] {
		"Team455ibc3.java",
			    "\n" +
			    "public team class Team455ibc3 {\n" +
			    "	protected class Role playedBy T455ibc3 {\n" +
			    "		callin foo() {\n" +
			    "			base.foo();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"T455ibc3.java",
			    "\n" +
			    "public class T455ibc3 {\n" +
			    "	public void foo() { }\n" +
			    "}	\n" +
			    "	\n"
            },
            "----------\n" +
    		"1. ERROR in Team455ibc3.java (at line 4)\n" +
    		"	callin foo() {\n" +
    		"	       ^^^^^\n" +
    		"Return type for the method is missing\n" +
    		"----------\n");
    }

    // a base call in a regular method missing a return type
    // 4.5.5-otjld-illegal-base-call-4
    public void test455_illegalBaseCall4() {
        runNegativeTestMatching(
            new String[] {
		"Team455ibc4.java",
			    "\n" +
			    "public team class Team455ibc4 {\n" +
			    "	protected class Role playedBy T455ibc4 {\n" +
			    "		foo() {\n" +
			    "			base.foo();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"T455ibc4.java",
			    "\n" +
			    "public class T455ibc4 {\n" +
			    "	public void foo() { }\n" +
			    "}	\n" +
			    "	\n"
            },
            "2.6");
    }

    // a base call using another role method name
    // 4.5.5-otjld-illegal-base-call-5
    public void test455_illegalBaseCall5() {
        runNegativeTestMatching(
            new String[] {
		"Team455ibc5.java",
			    "\n" +
			    "public team class Team455ibc5 {\n" +
			    "    protected class Role playedBy T455ibc5 {\n" +
			    "        void rm() {}\n" +
			    "        callin void rm2() {\n" +
			    "            base.rm();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"T455ibc5.java",
			    "\n" +
			    "public class T455ibc5 {\n" +
			    "    public void foo() { }\n" +
			    "}   \n" +
			    "    \n"
            },
            "4.3(a)");
    }

    // a base call has a wrong argument
    // 4.5.5-otjld-illegal-base-call-6
    public void test455_illegalBaseCall6() {
        runNegativeTest(
            new String[] {
		"Team455ibc6.java",
			    "\n" +
			    "public team class Team455ibc6 {\n" +
			    "    protected class Role playedBy T455ibc6 {\n" +
			    "        void rm() {}\n" +
			    "        callin void rm2() {\n" +
			    "            base.rm2(1);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"T455ibc6.java",
			    "\n" +
			    "public class T455ibc6 {\n" +
			    "    public void foo() { }\n" +
			    "}   \n" +
			    "    \n"
            },
            "----------\n" +
    		"1. ERROR in Team455ibc6.java (at line 6)\n" +
    		"	base.rm2(1);\n" +
    		"	^^^^^^^^^^^\n" +
    		"Base call \"base.rm2(1)\" does not match the signature of the enclosing callin method (OTJLD 4.3(a)).\n" +
    		"----------\n");
    }

    public void test455_illegalBaseCall7() {
        runNegativeTest(
            new String[] {
		"Team455ibc7.java",
			    "\n" +
			    "public team class Team455ibc7 {\n" +
			    "    protected class Role playedBy T455ibc7 {\n" +
			    "        void rm() {}\n" +
			    "        callin void rm2(Integer s, boolean b) {\n" +
			    "           if (b)\n" +
			    "				base.rm2(1, false);\n" +
			    "			else\n" +
			    "				base.rm2(\"wrong\", true);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"T455ibc7.java",
			    "\n" +
			    "public class T455ibc7 {\n" +
			    "    public void foo() { }\n" +
			    "}   \n" +
			    "    \n"
            },
            "----------\n" +
    		"1. ERROR in Team455ibc7.java (at line 9)\n" +
    		"	base.rm2(\"wrong\", true);\n" +
    		"	     ^^^\n" +
    		"Base call rm2(Integer, boolean) is not applicable for the arguments (String, boolean)\n" +
    		"----------\n");
    }

    // a role method is callin-bound as 'replace' with multiple base call  -- syntax error missing replace
    // 4.5.6-otjld-multiple-base-calls-1
    public void test456_multipleBaseCalls1() {
        runNegativeTestMatching(
            new String[] {
		"Team456mbc1.java",
			    "\n" +
			    "public team class Team456mbc1 {\n" +
			    "    public class Role456mbc1 playedBy T456mbc1 {\n" +
			    "        callin int getValue() {\n" +
			    "            return base.getValue() + base.getValue() + base.getValue();\n" +
			    "        }\n" +
			    "        getValue <- test;\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T456mbc1.java",
			    "\n" +
			    "public class T456mbc1 {\n" +
			    "    private int value = 0;\n" +
			    "    public int test(int arg) {\n" +
			    "        return value += arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "A.3.3");
    }

    // a role method is callin-bound as 'replace' with multiple base call
    // 4.5.6-otjld-multiple-base-calls-2
    public void test456_multipleBaseCalls2() {

       runConformTest(
            new String[] {
		"T456mbc2Main.java",
			    "\n" +
			    "public class T456mbc2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team456mbc2 t = new Team456mbc2();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T456mbc2 o = new T456mbc2();\n" +
			    "\n" +
			    "            System.out.print(o.test(2));\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T456mbc2.java",
			    "\n" +
			    "public class T456mbc2 {\n" +
			    "    private int value = 0;\n" +
			    "    public int test(int arg) {\n" +
			    "        return value += arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team456mbc2.java",
			    "\n" +
			    "public team class Team456mbc2 {\n" +
			    "    public class Role456mbc2 playedBy T456mbc2 {\n" +
			    "        callin int getValue() {\n" +
			    "            int i = base.getValue();\n" +
			    "            return base.getValue() + i + base.getValue();\n" +
			    "        }\n" +
			    "        getValue <- replace test;\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "12");
    }

    // a role method is callin-bound as 'replace' with multiple base call, binding in sub role
    // 4.5.6-otjld-multiple-base-calls-2a
    public void test456_multipleBaseCalls2a() {

       runConformTest(
            new String[] {
		"T456mbc2aMain.java",
			    "\n" +
			    "public class T456mbc2aMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team456mbc2a t = new Team456mbc2a();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T456mbc2a o = new T456mbc2a();\n" +
			    "\n" +
			    "            System.out.print(o.test(2));\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T456mbc2a.java",
			    "\n" +
			    "public class T456mbc2a {\n" +
			    "    private int value = 0;\n" +
			    "    public int test(int arg) {\n" +
			    "        return value += arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team456mbc2a.java",
			    "\n" +
			    "public team class Team456mbc2a {\n" +
			    "    public class Role456mbc2a_1 {\n" +
			    "        callin int getValue() {\n" +
			    "            int i = base.getValue();\n" +
			    "            System.out.print(\"VAL=\");\n" +
			    "            return base.getValue() + i + base.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role456mbc2a_2 extends Role456mbc2a_1 playedBy T456mbc2a {\n" +
			    "        getValue <- replace test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "VAL=12");
    }

    // a role method is callin-bound as 'replace' with multiple base call -- suppressed warning
    // 4.5.6-otjld-multiple-base-calls-3
    public void test456_multipleBaseCalls3() {
        runConformTest(
            new String[] {
		"T456mbc3.java",
			    "\n" +
			    "public class T456mbc3 {\n" +
			    "    private int value = 0;\n" +
			    "    public int test(int arg) {\n" +
			    "        return value += arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team456mbc3.java",
			    "\n" +
			    "public team class Team456mbc3 {\n" +
			    "    public class Role456mbc3 playedBy T456mbc3 {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin int getValue() {\n" +
			    "            return base.getValue() + base.getValue() + base.getValue();\n" +
			    "        }\n" +
			    "        getValue <- replace test;\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a callin method has no result, no base call is issued, therefor the base client receives no result  (base type)
    // 4.5.7-otjld-basecall-result-not-provided-1
    public void test457_basecallResultNotProvided1() {
        runNegativeTestMatching(
            new String[] {
		"Team457brnp1.java",
			    "\n" +
			    "public team class Team457brnp1 {\n" +
			    "	public class Role playedBy T457brnp1 {\n" +
			    "		callin void broken() {\n" +
			    "			// no basecall.\n" +
			    "		}\n" +
			    "		broken <- replace getValue;\n" +
			    "	}\n" +
			    "	public Team457brnp1 (T457brnp1 b) {\n" +
			    "		activate();\n" +
			    "		try {\n" +
			    "			System.out.print(b.getValue());\n" +
			    "		}  catch (org.objectteams.ResultNotProvidedException e) {\n" +
			    "			System.out.print(13);\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team457brnp1(new T457brnp1());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T457brnp1.java",
			    "\n" +
			    "public class T457brnp1 {\n" +
			    "	public int getValue() {\n" +
			    "		return 42;\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "4.3(e)");
    }

    // a callin method has no result, no base call is issued, therefor the base client receives no result  (reference type)
    // 4.5.7-otjld-basecall-result-not-provided-2
    public void test457_basecallResultNotProvided2() {
        runNegativeTestMatching(
            new String[] {
		"Team457brnp2.java",
			    "\n" +
			    "public team class Team457brnp2 {\n" +
			    "	public class Role playedBy T457brnp2 {\n" +
			    "		callin void broken() {\n" +
			    "			// no basecall.\n" +
			    "		}\n" +
			    "		broken <- replace getValue;\n" +
			    "	}\n" +
			    "	public Team457brnp2 (T457brnp2 b) {\n" +
			    "		activate();\n" +
			    "		try {\n" +
			    "			System.out.print(b.getValue());\n" +
			    "		}  catch (org.objectteams.ResultNotProvidedException e) {\n" +
			    "			System.out.print(13);\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T457brnp2.java",
			    "\n" +
			    "public class T457brnp2 {\n" +
			    "	public String getValue() {\n" +
			    "		return \"could be OK\";\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "4.3(e)");
    }

    // a callin method has no result, base call is potentially missing, therefor the base client receives no result  (base type)
    // 4.5.7-otjld-basecall-result-not-provided-3
    public void test457_basecallResultNotProvided3() {

       runConformTest(
            new String[] {
		"T457brnp3Main.java",
			    "\n" +
			    "public class T457brnp3Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team457brnp3(new T457brnp3());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T457brnp3.java",
			    "\n" +
			    "public class T457brnp3 {\n" +
			    "	public int getValue() {\n" +
			    "		return 42;\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team457brnp3.java",
			    "\n" +
			    "public team class Team457brnp3 {\n" +
			    "	public class Role playedBy T457brnp3 {\n" +
			    "		callin void broken() {\n" +
			    "			if (\"13\".equals(\"14\"))\n" +
			    "				base.broken();\n" +
			    "		}\n" +
			    "		broken <- replace getValue;\n" +
			    "	}\n" +
			    "	public Team457brnp3 (T457brnp3 b) {\n" +
			    "		activate();\n" +
			    "		try {\n" +
			    "			System.out.print(b.getValue());\n" +
			    "		}  catch (org.objectteams.ResultNotProvidedException e) {\n" +
			    "			System.out.print(\"OK\");\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a callin method has no result, base call is potentially missing, therefor the base client receives no result  (base type) - potential achieved by return
    // 4.5.7-otjld-basecall-result-not-provided-3b
    public void test457_basecallResultNotProvided3b() {

       runConformTest(
            new String[] {
		"T457brnp3bMain.java",
			    "\n" +
			    "public class T457brnp3bMain {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team457brnp3b(new T457brnp3b());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T457brnp3b.java",
			    "\n" +
			    "public class T457brnp3b {\n" +
			    "	public int getValue() {\n" +
			    "		return 42;\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team457brnp3b.java",
			    "\n" +
			    "public team class Team457brnp3b {\n" +
			    "	public class Role playedBy T457brnp3b {\n" +
			    "		callin void broken() {\n" +
			    "			if (!\"13\".equals(\"14\"))\n" +
			    "				return;\n" +
			    "			base.broken();\n" +
			    "		}\n" +
			    "		broken <- replace getValue;\n" +
			    "	}\n" +
			    "	public Team457brnp3b (T457brnp3b b) {\n" +
			    "		activate();\n" +
			    "		try {\n" +
			    "			System.out.print(b.getValue());\n" +
			    "		}  catch (org.objectteams.ResultNotProvidedException e) {\n" +
			    "			System.out.print(\"OK\");\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a callin method has no result, base call is potentially missing, therefor the base client receives no result  (base type) - potential achieved by return -- warning suppressed
    // 4.5.7-otjld-basecall-result-not-provided-3c
    public void test457_basecallResultNotProvided3c() {
        runConformTest(
            new String[] {
		"T457brnp3c.java",
			    "\n" +
			    "public class T457brnp3c {\n" +
			    "    public int getValue() {\n" +
			    "        return 42;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team457brnp3c.java",
			    "\n" +
			    "public team class Team457brnp3c {\n" +
			    "    public class Role playedBy T457brnp3c {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin void broken() {\n" +
			    "            if (!\"13\".equals(\"14\"))\n" +
			    "                return;\n" +
			    "            base.broken();\n" +
			    "        }\n" +
			    "        @SuppressWarnings(\"fragilecallin\")\n" +
			    "        broken <- replace getValue;\n" +
			    "    }\n" +
			    "    public Team457brnp3c (T457brnp3c b) {\n" +
			    "        activate();\n" +
			    "        try {\n" +
			    "            System.out.print(b.getValue());\n" +
			    "        }  catch (org.objectteams.ResultNotProvidedException e) {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a callin method has no result, base call is potentially missing, therefor the base client receives no result  (reference type)
    // 4.5.7-otjld-basecall-result-not-provided-4
    public void test457_basecallResultNotProvided4() {

       runConformTest(
            new String[] {
		"T457brnp4Main.java",
			    "\n" +
			    "public class T457brnp4Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team457brnp4(new T457brnp4());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T457brnp4.java",
			    "\n" +
			    "public class T457brnp4 {\n" +
			    "	public String getValue() {\n" +
			    "		return \"could be OK\";\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team457brnp4.java",
			    "\n" +
			    "public team class Team457brnp4 {\n" +
			    "	public class Role playedBy T457brnp4 {\n" +
			    "		callin void broken() {\n" +
			    "			if (\"13\".equals(\"14\"))\n" +
			    "				base.broken();\n" +
			    "		}\n" +
			    "		broken <- replace getValue;\n" +
			    "	}\n" +
			    "	public Team457brnp4 (T457brnp4 b) {\n" +
			    "		activate();\n" +
			    "		String result = b.getValue();\n" +
			    "		if (result == null)\n" +
			    "			System.out.print(\"OK\");\n" +
			    "		else\n" +
			    "			System.out.print(result);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a callin method has no result, base call is potentially missing, therefor the base client receives no result  (reference type)  - via byte code
    // 4.5.7-otjld-basecall-result-not-provided-5
    public void test457_basecallResultNotProvided5() {

       runConformTest(
            new String[] {
		"T457brnp5Main.java",
			    "\n" +
			    "public class T457brnp5Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team457brnp5_2(new T457brnp5());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T457brnp5.java",
			    "\n" +
			    "public class T457brnp5 {\n" +
			    "	public String getValue() {\n" +
			    "		return \"could be OK\";\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team457brnp5_1.java",
			    "\n" +
			    "public team class Team457brnp5_1 {\n" +
			    "	public class Role {\n" +
			    "		callin void broken() {\n" +
			    "			if (\"13\".equals(\"14\"))\n" +
			    "				base.broken();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team457brnp5_2.java",
			    "\n" +
			    "public team class Team457brnp5_2 extends Team457brnp5_1 {\n" +
			    "	public class Role playedBy T457brnp5 {\n" +
			    "		broken <- replace getValue;\n" +
			    "	}\n" +
			    "	public Team457brnp5_2 (T457brnp5 b) {\n" +
			    "		activate();\n" +
			    "		String result = b.getValue();\n" +
			    "		if (result == null)\n" +
			    "			System.out.print(\"OK\");\n" +
			    "		else\n" +
			    "			System.out.print(result);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a base call is potentially executed twice
    // 4.5.8-otjld-duplicate-base-call-1
    public void test458_duplicateBaseCall1() {

       runConformTest(
            new String[] {
		"T458dbc1Main.java",
			    "\n" +
			    "public class T458dbc1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team458dbc1(new T458dbc1());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T458dbc1.java",
			    "\n" +
			    "public class T458dbc1 {\n" +
			    "	public String getValue() {\n" +
			    "		return \"OK\";\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team458dbc1.java",
			    "\n" +
			    "public team class Team458dbc1 {\n" +
			    "	public class Role playedBy T458dbc1 {\n" +
			    "		callin String dubious() {\n" +
			    "			if (\"13\".equals(\"14\"))\n" +
			    "				System.out.println(base.dubious());\n" +
			    "			return base.dubious();\n" +
			    "		}\n" +
			    "		dubious <- replace getValue;\n" +
			    "	}\n" +
			    "	public Team458dbc1 (T458dbc1 o) {\n" +
			    "		activate();\n" +
			    "		System.out.print(o.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a base call is definitely executed twice
    // 4.5.8-otjld-duplicate-base-call-2
    public void test458_duplicateBaseCall2() {

       runConformTest(
            new String[] {
		"T458dbc2Main.java",
			    "\n" +
			    "public class T458dbc2Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team458dbc2(new T458dbc2());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T458dbc2.java",
			    "\n" +
			    "public class T458dbc2 {\n" +
			    "	public String getValue() {\n" +
			    "		return \"OK\";\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team458dbc2.java",
			    "\n" +
			    "public team class Team458dbc2 {\n" +
			    "	public class Role playedBy T458dbc2 {\n" +
			    "		callin String dubious() {\n" +
			    "			if (\"13\".equals(\"14\"))\n" +
			    "				System.out.println(base.dubious());\n" +
			    "			return base.dubious();\n" +
			    "		}\n" +
			    "		dubious <- replace getValue;\n" +
			    "	}\n" +
			    "	public Team458dbc2 (T458dbc2 o) {\n" +
			    "		activate();\n" +
			    "		System.out.print(o.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // two base calls are mentioned, but definitely exactly one is executed
    // 4.5.8-otjld-duplicate-base-call-3
    public void test458_duplicateBaseCall3() {
        runConformTest(
            new String[] {
		"T458dbc3.java",
			    "\n" +
			    "public class T458dbc3 {\n" +
			    "    public void make() {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team458dbc3.java",
			    "\n" +
			    "public team class Team458dbc3 {\n" +
			    "    protected class R playedBy T458dbc3 {\n" +
			    "        void it(int l) <- replace void make() with { l <- 4 }\n" +
			    "        callin void it(int l) {\n" +
			    "            if (l == 2) {\n" +
			    "                base.it(l);\n" +
			    "                return;\n" +
			    "            }\n" +
			    "            for (int i=0; i<l; i++) {\n" +
			    "                if (i == 3) {\n" +
			    "                    base.it(6);\n" +
			    "                    return;\n" +
			    "                }\n" +
			    "            }\n" +
			    "            base.it(l);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // two methods returning void are bound using replace, no basecall
    // 4.5.9-otjld-missing-basecall-in-voidmethod-1
    public void test459_missingBasecallInVoidmethod1() {

       runConformTest(
            new String[] {
		"T459mbiv1Main.java",
			    "\n" +
			    "public class T459mbiv1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team459mbiv1(new T459mbiv1());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T459mbiv1.java",
			    "\n" +
			    "public class T459mbiv1 {\n" +
			    "	void foo() { System.out.print(\"NOTOK\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team459mbiv1.java",
			    "\n" +
			    "public team class Team459mbiv1 {\n" +
			    "	protected class Role playedBy T459mbiv1 {\n" +
			    "		callin void bar () {\n" +
			    "			// no base call\n" +
			    "			System.out.print(\"OK\");\n" +
			    "		}\n" +
			    "		void bar() <- replace void foo();\n" +
			    "	}\n" +
			    "	public Team459mbiv1(T459mbiv1 obj) {\n" +
			    "		activate();\n" +
			    "		obj.foo();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a callin method calls its tsuper version which has a base call
    // 4.5.10-otjld-tsuper-instead-of-basecall-1
    public void test4510_tsuperInsteadOfBasecall1() {

       runConformTest(
            new String[] {
		"T4510tiob1Main.java",
			    "\n" +
			    "public class T4510tiob1Main {\n" +
			    "        @SuppressWarnings(\"unused\")\n" +
			    "	public static void main (String[] args) {\n" +
			    "		Team4510tiob1_2 t = new Team4510tiob1_2();\n" +
			    "		T4510tiob1 obj = new T4510tiob1();\n" +
			    "		obj.init();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"Team4510tiob1_1.java",
			    "\n" +
			    "public team class Team4510tiob1_1 {\n" +
			    "    public abstract class Role {\n" +
			    "	abstract char getChar();\n" +
			    "	callin void intercept() {\n" +
			    "		base.intercept();\n" +
			    "		System.out.print(getChar());\n" +
			    "	}\n" +
			    "    }\n" +
			    "}	\n" +
			    "	\n",
		"T4510tiob1.java",
			    "\n" +
			    "public class T4510tiob1 {\n" +
			    "	char c = 'X';\n" +
			    "	public char getChar() { \n" +
			    "		return c;\n" +
			    "	}\n" +
			    "	public void init() {\n" +
			    "		c = 'O';\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team4510tiob1_2.java",
			    "\n" +
			    "public team class Team4510tiob1_2 extends Team4510tiob1_1 {\n" +
			    "    public class Role playedBy T4510tiob1 {\n" +
			    "    	getChar -> getChar;\n" +
			    "	intercept <- replace init;\n" +
			    "	callin void intercept() {\n" +
			    "		tsuper.intercept();\n" +
			    "		System.out.print(\"K\");\n" +
			    "	}\n" +
			    "    }\n" +
			    "    public Team4510tiob1_2() {\n" +
			    "    	activate();\n" +
			    "    }\n" +
			    "}	\n" +
			    "	\n"
            },
            "OK");
    }

    // a callin method potentially calls its tsuper version which has a base call
    // 4.5.10-otjld-tsuper-instead-of-basecall-2
    public void test4510_tsuperInsteadOfBasecall2() {

       runConformTest(
            new String[] {
		"T4510tiob2Main.java",
			    "\n" +
			    "public class T4510tiob2Main {\n" +
			    "        @SuppressWarnings(\"unused\")\n" +
			    "	public static void main (String[] args) {\n" +
			    "		Team4510tiob2_2 t = new Team4510tiob2_2();\n" +
			    "		T4510tiob2 obj = new T4510tiob2();\n" +
			    "		obj.init();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"Team4510tiob2_1.java",
			    "\n" +
			    "public team class Team4510tiob2_1 {\n" +
			    "    public abstract class Role {\n" +
			    "	abstract char getChar();\n" +
			    "	callin void intercept() {\n" +
			    "		base.intercept();\n" +
			    "		System.out.print(getChar());\n" +
			    "	}\n" +
			    "    }\n" +
			    "}	\n" +
			    "	\n",
		"T4510tiob2.java",
			    "\n" +
			    "public class T4510tiob2 {\n" +
			    "	char c = 'X';\n" +
			    "	public char getChar() { \n" +
			    "		return c;\n" +
			    "	}\n" +
			    "	public void init() {\n" +
			    "		c = 'O';\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team4510tiob2_2.java",
			    "\n" +
			    "public team class Team4510tiob2_2 extends Team4510tiob2_1 {\n" +
			    "    public class Role playedBy T4510tiob2 {\n" +
			    "    	getChar -> getChar;\n" +
			    "	intercept <- replace init;\n" +
			    "	callin void intercept() {\n" +
			    "		if (\"13\".equals(\"13\"))\n" +
			    "			tsuper.intercept();\n" +
			    "		System.out.print(\"K\");\n" +
			    "	}\n" +
			    "    }\n" +
			    "    public Team4510tiob2_2() {\n" +
			    "    	activate();\n" +
			    "    }\n" +
			    "}	\n" +
			    "	\n"
            },
            "OK");
    }

    // a callin method calls its tsuper version which has no base call
    // 4.5.10-otjld-tsuper-instead-of-basecall-3
    public void test4510_tsuperInsteadOfBasecall3() {

       runConformTest(
            new String[] {
		"T4510tiob3Main.java",
			    "\n" +
			    "public class T4510tiob3Main {\n" +
			    "        @SuppressWarnings(\"unused\")\n" +
			    "	public static void main (String[] args) {\n" +
			    "		Team4510tiob3_2 t = new Team4510tiob3_2();\n" +
			    "		T4510tiob3 obj = new T4510tiob3();\n" +
			    "		obj.init();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"Team4510tiob3_1.java",
			    "\n" +
			    "public team class Team4510tiob3_1 {\n" +
			    "    public abstract class Role {\n" +
			    "	abstract char getChar();\n" +
			    "	callin void intercept() {\n" +
			    "		System.out.print(getChar());\n" +
			    "	}\n" +
			    "    }\n" +
			    "}	\n" +
			    "	\n",
		"T4510tiob3.java",
			    "\n" +
			    "public class T4510tiob3 {\n" +
			    "	char c = 'X';\n" +
			    "	public char getChar() { \n" +
			    "		return c;\n" +
			    "	}\n" +
			    "	public void init() {\n" +
			    "		c = 'O';\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team4510tiob3_2.java",
			    "\n" +
			    "public team class Team4510tiob3_2 extends Team4510tiob3_1 {\n" +
			    "    public class Role playedBy T4510tiob3 {\n" +
			    "    	getChar -> getChar;\n" +
			    "	intercept <- replace init;\n" +
			    "	callin void intercept() {\n" +
			    "		tsuper.intercept();\n" +
			    "		System.out.print(\"K\");\n" +
			    "	}\n" +
			    "    }\n" +
			    "    public Team4510tiob3_2() {\n" +
			    "    	activate();\n" +
			    "    }\n" +
			    "}	\n" +
			    "	\n"
            },
            "XK");
    }

    // a callin method creates an instance of a local class, that instance issues a base call
    // 4.5.11-otjld-basecall-in-anonymous-object-1
    public void test4511_basecallInAnonymousObject1() {

       runConformTest(
            new String[] {
		"Team4511biao1.java",
			    "\n" +
			    "public team class Team4511biao1 {\n" +
			    "    protected class R playedBy T4511biao1 {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin void intercept() {\n" +
			    "            I4511biao1 i = new I4511biao1() {\n" +
			    "                public void perform() {\n" +
			    "                    System.out.print(\"O\");\n" +
			    "                    base.intercept();\n" +
			    "                    System.out.print(\"!\");\n" +
			    "                }\n" +
			    "            };\n" +
			    "            i.perform();\n" +
			    "        }\n" +
			    "        intercept <- replace test;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4511biao1().activate();\n" +
			    "        new T4511biao1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4511biao1.java",
			    "\n" +
			    "public class T4511biao1 {\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I4511biao1.java",
			    "\n" +
			    "public interface I4511biao1 {\n" +
			    "    void perform();\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!");
    }

    // a base call from an anonymous object returns a value
    // 4.5.11-otjld-basecall-in-anonymous-object-2
    public void test4511_basecallInAnonymousObject2() {

       runConformTest(
            new String[] {
		"Team4511biao2.java",
			    "\n" +
			    "public team class Team4511biao2 {\n" +
			    "    protected class R playedBy T4511biao2 {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin String intercept() {\n" +
			    "            I4511biao2 i = new I4511biao2() {\n" +
			    "                public String perform() {\n" +
			    "                    System.out.print(\"O\");\n" +
			    "                    String result = base.intercept();\n" +
			    "                    return result + \"!\";\n" +
			    "                }\n" +
			    "            };\n" +
			    "            return i.perform();\n" +
			    "        }\n" +
			    "        intercept <- replace test;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4511biao2().activate();\n" +
			    "        System.out.print(new T4511biao2().test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4511biao2.java",
			    "\n" +
			    "public class T4511biao2 {\n" +
			    "    String test() {\n" +
			    "        return \"K\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I4511biao2.java",
			    "\n" +
			    "public interface I4511biao2 {\n" +
			    "    String perform();\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!");
    }

    // a base call from an anonymous object cannot tunnel a return-value
    // 4.5.11-otjld-basecall-in-anonymous-object-3
    public void test4511_basecallInAnonymousObject3() {
        runNegativeTestMatching(
            new String[] {
		"Team4511biao3.java",
			    "\n" +
			    "public team class Team4511biao3 {\n" +
			    "    protected class R playedBy T4511biao3 {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin void intercept() {\n" +
			    "            I4511biao3 i = new I4511biao3() {\n" +
			    "                public void perform() {\n" +
			    "                    System.out.print(\"O\");\n" +
			    "                    base.intercept();\n" +
			    "                    System.out.print(\"K\");\n" +
			    "                }\n" +
			    "            };\n" +
			    "            // no base call at this level\n" +
			    "            i.perform();\n" +
			    "        }\n" +
			    "        intercept <- replace test;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4511biao3().activate();\n" +
			    "        System.out.print(new T4511biao3().test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4511biao3.java",
			    "\n" +
			    "public class T4511biao3 {\n" +
			    "    String test() {\n" +
			    "        return \"!\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I4511biao3.java",
			    "\n" +
			    "public interface I4511biao3 {\n" +
			    "    void perform();\n" +
			    "}\n" +
			    "    \n"
            },
            "4.3(e)");
    }

    // a base call from an anonymous object returns a value - with copy inheritance
    // 4.5.11-otjld-basecall-in-anonymous-object-4
    public void test4511_basecallInAnonymousObject4() {

       runConformTest(
            new String[] {
		"Team4511biao4_2.java",
			    "\n" +
			    "public team class Team4511biao4_2 extends Team4511biao4_1 {\n" +
			    "    protected class R playedBy T4511biao4 {\n" +
			    "        intercept <- replace test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4511biao4_2().activate();\n" +
			    "        System.out.print(new T4511biao4().test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4511biao4.java",
			    "\n" +
			    "public class T4511biao4 {\n" +
			    "    String test() {\n" +
			    "        return \"K\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I4511biao4.java",
			    "\n" +
			    "public interface I4511biao4 {\n" +
			    "    String perform();\n" +
			    "}\n" +
			    "    \n",
		"Team4511biao4_1.java",
			    "\n" +
			    "public team class Team4511biao4_1 {\n" +
			    "    protected class R {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin String intercept() {\n" +
			    "            I4511biao4 i = new I4511biao4() {\n" +
			    "                public String perform() {\n" +
			    "                    System.out.print(\"O\");\n" +
			    "                    String result = base.intercept();\n" +
			    "                    return result + \"!\";\n" +
			    "                }\n" +
			    "            };\n" +
			    "            return i.perform();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!");
    }

    // a base call from an anonymous object returns a value - with copy inheritance , separate compilation
    // 4.5.11-otjld-basecall-in-anonymous-object-4a
    public void test4511_basecallInAnonymousObject4a() {

       runConformTest(
            new String[] {
		"Team4511biao4a_2.java",
			    "\n" +
			    "public team class Team4511biao4a_2 extends Team4511biao4a_1 {\n" +
			    "    protected class R playedBy T4511biao4a {\n" +
			    "        intercept <- replace test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4511biao4a_2().activate();\n" +
			    "        System.out.print(new T4511biao4a().test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4511biao4a.java",
			    "\n" +
			    "public class T4511biao4a {\n" +
			    "    String test() {\n" +
			    "        return \"K\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I4511biao4a.java",
			    "\n" +
			    "public interface I4511biao4a {\n" +
			    "    String perform();\n" +
			    "}\n" +
			    "    \n",
		"Team4511biao4a_1.java",
			    "\n" +
			    "public team class Team4511biao4a_1 {\n" +
			    "    protected class R {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin String intercept() {\n" +
			    "            I4511biao4a i = new I4511biao4a() {\n" +
			    "                public String perform() {\n" +
			    "                    System.out.print(\"O\");\n" +
			    "                    String result = base.intercept();\n" +
			    "                    return result + \"!\";\n" +
			    "                }\n" +
			    "            };\n" +
			    "            return i.perform();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!");
    }

    // a base call from an anonymous object returns a value - with two-level copy inheritance , compile in one go
    // 4.5.11-otjld-basecall-in-anonymous-object-5
    public void test4511_basecallInAnonymousObject5() {

       runConformTest(
            new String[] {
		"Team4511biao5_3.java",
			    "\n" +
			    "public team class Team4511biao5_3 extends Team4511biao5_2 {\n" +
			    "    protected class R playedBy T4511biao5 {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin String intercept() {\n" +
			    "            return tsuper.intercept()+\"$\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4511biao5_3().activate();\n" +
			    "        System.out.print(new T4511biao5().test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4511biao5.java",
			    "\n" +
			    "public class T4511biao5 {\n" +
			    "    String test() {\n" +
			    "        return \"K\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I4511biao5.java",
			    "\n" +
			    "public interface I4511biao5 {\n" +
			    "    String perform();\n" +
			    "}\n" +
			    "    \n",
		"Team4511biao5_1.java",
			    "\n" +
			    "public team class Team4511biao5_1 {\n" +
			    "    protected class R {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin String intercept() {\n" +
			    "            I4511biao5 i = new I4511biao5() {\n" +
			    "                public String perform() {\n" +
			    "                    System.out.print(\"O\");\n" +
			    "                    String result = base.intercept();\n" +
			    "                    return result + \"!\";\n" +
			    "                }\n" +
			    "            };\n" +
			    "            return i.perform();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4511biao5_2.java",
			    "\n" +
			    "public team class Team4511biao5_2 extends Team4511biao5_1 {\n" +
			    "    protected class R playedBy T4511biao5 {\n" +
			    "        intercept <- replace test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!$");
    }

    // a base call from an anonymous object returns a value - with two-level copy inheritance , separate compilation
    // 4.5.11-otjld-basecall-in-anonymous-object-5a
    public void test4511_basecallInAnonymousObject5a() {

       runConformTest(
            new String[] {
		"Team4511biao5a_3.java",
			    "\n" +
			    "public team class Team4511biao5a_3 extends Team4511biao5a_2 {\n" +
			    "    protected class R playedBy T4511biao5a {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin String intercept() {\n" +
			    "            return tsuper.intercept()+\"$\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4511biao5a_3().activate();\n" +
			    "        System.out.print(new T4511biao5a().test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4511biao5a.java",
			    "\n" +
			    "public class T4511biao5a {\n" +
			    "    String test() {\n" +
			    "        return \"K\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I4511biao5a.java",
			    "\n" +
			    "public interface I4511biao5a {\n" +
			    "    String perform();\n" +
			    "}\n" +
			    "    \n",
		"Team4511biao5a_1.java",
			    "\n" +
			    "public team class Team4511biao5a_1 {\n" +
			    "    protected class R {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin String intercept() {\n" +
			    "            I4511biao5a i = new I4511biao5a() {\n" +
			    "                public String perform() {\n" +
			    "                    System.out.print(\"O\");\n" +
			    "                    String result = base.intercept();\n" +
			    "                    return result + \"!\";\n" +
			    "                }\n" +
			    "            };\n" +
			    "            return i.perform();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4511biao5a_2.java",
			    "\n" +
			    "public team class Team4511biao5a_2 extends Team4511biao5a_1 {\n" +
			    "    protected class R playedBy T4511biao5a {\n" +
			    "        intercept <- replace test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!$");
    }

    // a base call in a static callin method lowers its argument
    // 4.5.12-otjld-basecall-requires-lowering-1
    public void test4512_basecallRequiresLowering1() {

       runConformTest(
            new String[] {
		"Team4512brl1.java",
			    "\n" +
			    "import base p1.T4512brl1;\n" +
			    "public team class Team4512brl1 {\n" +
			    "    protected class R playedBy T4512brl1 {\n" +
			    "        static callin void rm(R other) {\n" +
			    "            base.rm(other);\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        rm <- replace test;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4512brl1().activate();\n" +
			    "        p1.T4512brl1.test(new p1.T4512brl1());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T4512brl1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T4512brl1 {\n" +
			    "    public static void test(T4512brl1 other) {\n" +
			    "        other.print();\n" +
			    "    }\n" +
			    "    void print() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a base call invokes the super version
    // 4.5.13-otjld-basecall-super-access-1
    public void test4513_basecallSuperAccess1() {

       runConformTest(
            new String[] {
		"Team4513bsa1.java",
			    "\n" +
			    "public team class Team4513bsa1 {\n" +
			    "    protected class R playedBy T4513bsa1_2 {\n" +
			    "	@SuppressWarnings(\"decapsulation\")\n" +
			    "        callin String rm(int i) {\n" +
			    "            return \"R:\"+base.super.rm(i+40);\n" +
			    "        }\n" +
			    "        rm <- replace bm;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4513bsa1().activate();\n" +
			    "        System.out.print(new T4513bsa1_2().bm(2));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4513bsa1_1.java",
			    "\n" +
			    "public class T4513bsa1_1 {\n" +
			    "    String bm (int in) {\n" +
			    "        return \"Super:\"+in;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4513bsa1_2.java",
			    "\n" +
			    "public class T4513bsa1_2 extends T4513bsa1_1 {\n" +
			    "    String bm (int in) {\n" +
			    "        return \"Sub:\"+in;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R:Super:42");
    }

    // a base call invokes the super version
    // 4.5.13-otjld-basecall-super-access-2
    public void test4513_basecallSuperAccess2() {

       runConformTest(
            new String[] {
		"Team4513bsa2.java",
			    "\n" +
			    "public team class Team4513bsa2 {\n" +
			    "    protected class R playedBy T4513bsa2_2 {\n" +
			    "        @SuppressWarnings({\"basecall\",\"decapsulation\"})\n" +
			    "        callin String rm(int i) {\n" +
			    "            return \"R:\"+base.super.rm(i+40)+base.rm(i+20);\n" +
			    "        }\n" +
			    "        rm <- replace bm;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4513bsa2().activate();\n" +
			    "        System.out.print(new T4513bsa2_2().bm(2));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4513bsa2_1.java",
			    "\n" +
			    "public class T4513bsa2_1 {\n" +
			    "    String bm (int in) {\n" +
			    "        return \"Super:\"+in;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4513bsa2_2.java",
			    "\n" +
			    "public class T4513bsa2_2 extends T4513bsa2_1 {\n" +
			    "    String bm (int in) {\n" +
			    "        return \"Sub:\"+in;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R:Super:42Sub:22");
    }

    // code compiled by ecotj 1.2.3 is executed using OTRE >= 1.2.4
    // 4.5.13-otjld-basecall-super-access-3
    public void test4513_basecallSuperAccess3() {

    	String jarFilename = "T4513bsa3.jar";
    	String destPath = this.outputRootDirectoryPath+"/regression";
    	new File(destPath).mkdirs();
    	// upload the jar:
		Util.copy(getTestResourcePath(jarFilename), destPath);
    	// setup classpath:
    	String[] classPaths = getDefaultClassPaths();
    	int l = classPaths.length;
    	System.arraycopy(classPaths, 0, classPaths=new String[l+1], 0, l);
		classPaths[l] = this.outputRootDirectoryPath+"/regression/"+jarFilename;

        runNegativeTest(
            new String[] {
		"T4513bsa3Main.java",
			    "\n" +
			    "public class T4513bsa3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4513bsa3().activate();\n" +
			    "        System.out.print(new T4513bsa3().m(2));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            (this.weavingScheme == WeavingScheme.OTRE
            ?
            "----------\n" +
    		"1. ERROR in T4513bsa3Main.java (at line 1)\n" +
    		"	\n" +
    		"public class T4513bsa3Main {\n" +
    		"	^\n" +
    		"Class file Team4513bsa3$__OT__R.class has incompatible OT/J byte code version 1.2.3, please consider a full build of the declaring project.\n" +
    		"----------\n" +
    		"2. ERROR in T4513bsa3Main.java (at line 0)\n" +
    		"	\n" +
    		"public class T4513bsa3Main {\n" +
    		"	^\n" +
    		"Class file Team4513bsa3$__OT__Confined.class has incompatible OT/J byte code version 1.2.3, please consider a full build of the declaring project.\n" +
    		"----------\n"
    		:
            "----------\n" +
    		"1. ERROR in T4513bsa3Main.java (at line 1)\n" +
    		"	\n" +
    		"public class T4513bsa3Main {\n" +
    		"	^\n" +
    		"Class file Team4513bsa3$__OT__R.class has incompatible OT/J byte code version 1.2.3, please consider a full build of the declaring project.\n" +
    		"----------\n" +
    		"2. ERROR in T4513bsa3Main.java (at line 0)\n" +
    		"	\n" +
    		"public class T4513bsa3Main {\n" +
    		"	^\n" +
    		"Class file Team4513bsa3$__OT__R.class has been compiled for incompatible weaving target 'OTRE', please consider a full build of the declaring project.\n" +
    		"----------\n"
    		),
            classPaths,
            false/*shouldFlushOutputDirectory*/);
    }

    // a callin method has a base super call, same base method is bound by two roles
    // 4.5.13-otjld-basecall-super-access-4
    public void test4513_basecallSuperAccess4() {

       runConformTest(
            new String[] {
		"Team4513bsa4_2.java",
			    "\n" +
			    "public team class Team4513bsa4_2 {\n" +
			    "    protected class R2 playedBy T4513bsa4_2 {\n" +
			    "        callin String r2m(String in) {\n" +
			    "            return \"R2[\"+base.r2m(\"'\"+in+\"'\")+\"]\";\n" +
			    "        }\n" +
			    "        r2m <- replace bm;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4513bsa4_2().activate();\n" +
			    "        new Team4513bsa4_1().activate();\n" +
			    "        System.out.print(new T4513bsa4_2().bm(\"o\"));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4513bsa4_1.java",
			    "\n" +
			    "public class T4513bsa4_1 {\n" +
			    "    String bm(String in) {\n" +
			    "        return \"B0<\"+in+\">\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4513bsa4_2.java",
			    "\n" +
			    "public class T4513bsa4_2 extends T4513bsa4_1 {\n" +
			    "    String bm(String in) {\n" +
			    "        return \"B1{\"+in+\"}\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4513bsa4_1.java",
			    "\n" +
			    "public team class Team4513bsa4_1 {\n" +
			    "    protected class R1 playedBy T4513bsa4_2 {\n" +
			    "        callin String r1m(String in) {\n" +
			    "            return \"R1(\"+base.super.r1m(\"-\"+in+\"-\")+\")\";\n" +
			    "        }\n" +
			    "        r1m <- replace bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R1(B0<-o->)");
    }

    // a callin method has a base super call, super method is bound by another role
    // 4.5.13-otjld-basecall-super-access-5
    public void test4513_basecallSuperAccess5() {

       runConformTest(
            new String[] {
		"Team4513bsa5_2.java",
			    "\n" +
			    "public team class Team4513bsa5_2 {\n" +
			    "    protected class R2 playedBy T4513bsa5_1 {\n" +
			    "        callin String r2m(String in) {\n" +
			    "            return \"R2[\"+base.r2m(\"'\"+in+\"'\")+\"]\";\n" +
			    "        }\n" +
			    "        r2m <- replace bm;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4513bsa5_2().activate();\n" +
			    "        new Team4513bsa5_1().activate();\n" +
			    "        System.out.print(new T4513bsa5_2().bm(\"o\"));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4513bsa5_1.java",
			    "\n" +
			    "public class T4513bsa5_1 {\n" +
			    "    String bm(String in) {\n" +
			    "        return \"B0<\"+in+\">\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4513bsa5_2.java",
			    "\n" +
			    "public class T4513bsa5_2 extends T4513bsa5_1 {\n" +
			    "    String bm(String in) {\n" +
			    "        return \"B1{\"+in+\"}\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4513bsa5_1.java",
			    "\n" +
			    "public team class Team4513bsa5_1 {\n" +
			    "    protected class R1 playedBy T4513bsa5_2 {\n" +
			    "	@SuppressWarnings(\"decapsulation\")\n" +
			    "        callin String r1m(String in) {\n" +
			    "            return \"R1(\"+base.super.r1m(\"-\"+in+\"-\")+\")\";\n" +
			    "        }\n" +
			    "        r1m <- replace bm;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R1(B0<-o->)");
    }

    // a callin method tries to access an inexistent base super method
    // 4.5.13-otjld-basecall-super-access-6
    public void test4513_basecallSuperAccess6() {
        runNegativeTestMatching(
            new String[] {
		"Team4513bsa6.java",
			    "\n" +
			    "public team class Team4513bsa6 {\n" +
			    "  protected class R playedBy T4513bsa6 {\n" +
			    "    callin void rm() {\n" +
			    "      base.super.rm();\n" +
			    "    }\n" +
			    "    rm <- replace m;\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"T4513bsa6.java",
			    "\n" +
			    "public class T4513bsa6 {\n" +
			    "  void m() { }\n" +
			    "}\n" +
			    "  \n"
            },
            "4.3(f)");
    }

    // a role-of-role issues a base call
    // 4.5.14-otjld-basecall-to-lower-role-1
    public void test4514_basecallToLowerRole1() {

       runConformTest(
            new String[] {
		"Team4514btlr1_2.java",
			    "\n" +
			    "public team class Team4514btlr1_2<Team4514btlr1_1 baseTeam> {\n" +
			    "    protected class R2_1 playedBy R1_1<@baseTeam> {\n" +
			    "        callin String truncate(String val) {\n" +
			    "            System.out.print(\"R2_1:\"+val);\n" +
			    "            String v = base.truncate(val);\n" +
			    "            return v.substring(1);\n" +
			    "        }\n" +
			    "        truncate <- replace testing;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team4514btlr1_1 bt = new Team4514btlr1_1();\n" +
			    "        Team4514btlr1_2<@bt> ut = new Team4514btlr1_2<@bt>();\n" +
			    "        within(ut)\n" +
			    "            bt.run();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4514btlr1_1.java",
			    "\n" +
			    "public team class Team4514btlr1_1 {\n" +
			    "    public class R1_1 {\n" +
			    "        public String testing(String val) {\n" +
			    "            System.out.print(\"R1_1:\"+val);\n" +
			    "            return val.toUpperCase();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void run() {\n" +
			    "        System.out.print(new R1_1().testing(\"ok\"));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R2_1:okR1_1:okK");
    }

    // a role-of-role issues a base call - base team involves implicit inheritance
    // 4.5.14-otjld-basecall-to-lower-role-2
    public void test4514_basecallToLowerRole2() {

       runConformTest(
            new String[] {
		"Team4514btlr2_2.java",
			    "\n" +
			    "public team class Team4514btlr2_2<Team4514btlr2_0 baseTeam> {\n" +
			    "    protected class R2_1 playedBy R1_1<@baseTeam> {\n" +
			    "        callin String truncate(String val) {\n" +
			    "            System.out.print(\"R2_1:\"+val);\n" +
			    "            String v = base.truncate(val);\n" +
			    "            return v.substring(1);\n" +
			    "        }\n" +
			    "        truncate <- replace testing;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team4514btlr2_1 bt = new Team4514btlr2_1();\n" +
			    "        Team4514btlr2_2<@bt> ut = new Team4514btlr2_2<@bt>();\n" +
			    "        within(ut)\n" +
			    "            bt.run();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4514btlr2_0.java",
			    "\n" +
			    "public team class Team4514btlr2_0 {\n" +
			    "    public class R1_1 {\n" +
			    "        public String testing(String val) {\n" +
			    "            System.out.print(\"R1_1:\"+val);\n" +
			    "            return val.toUpperCase();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4514btlr2_1.java",
			    "\n" +
			    "public team class Team4514btlr2_1 extends Team4514btlr2_0 {\n" +
			    "    public void run() {\n" +
			    "        System.out.print(new R1_1().testing(\"ok\"));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R2_1:okR1_1:okK");
    }

    // a role-of-role issues a base call - base team involves both kinds of inheritance
    // 4.5.14-otjld-basecall-to-lower-role-3
    public void test4514_basecallToLowerRole3() {

       runConformTest(
            new String[] {
		"Team4514btlr3_2.java",
			    "\n" +
			    "public team class Team4514btlr3_2<Team4514btlr3_0 baseTeam> {\n" +
			    "    protected class R2_1 playedBy R1_1<@baseTeam> {\n" +
			    "        callin String truncate(String val) {\n" +
			    "            System.out.print(\"R2_1:\"+val);\n" +
			    "            String v = base.truncate(val);\n" +
			    "            return v.substring(1);\n" +
			    "        }\n" +
			    "        truncate <- replace testing;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team4514btlr3_1 bt = new Team4514btlr3_1();\n" +
			    "        Team4514btlr3_2<@bt> ut = new Team4514btlr3_2<@bt>();\n" +
			    "        within(ut)\n" +
			    "            bt.run();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4514btlr3_0.java",
			    "\n" +
			    "public team class Team4514btlr3_0 {\n" +
			    "    public class R1_1 {\n" +
			    "        public String testing(String val) {\n" +
			    "            System.out.print(\"R1_1:\"+val);\n" +
			    "            return val.toUpperCase();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4514btlr3_1.java",
			    "\n" +
			    "public team class Team4514btlr3_1 extends Team4514btlr3_0 {\n" +
			    "    public class R1_2 extends R1_1 {\n" +
			    "    }\n" +
			    "    public void run() {\n" +
			    "        System.out.print(new R1_2().testing(\"ok\"));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R2_1:okR1_1:okK");
    }

    // a callin with base call is involved in both explicit and implicit inheritance
    // witness for "Cannot generate method ci(), tsuper method has corrupt byte code"
    public void test4516_inheritedBaseCall1() {
    	runConformTest(
    		new String[] {
		"Team4516ibc1_2.java",
			"public team class Team4516ibc1_2 extends Team4516ibc1_1 {\n" +
			"	 @Override\n" +
			"    protected class R1 {}\n" +
			"	 public static void main(String... args) {\n" +
			"		 new Team4516ibc1_2().activate();\n" +
			"		 new B4516ibc1().bm();\n" +
			"	 }\n" +
			"}\n",
		"Team4516ibc1_1.java",
			"public team class Team4516ibc1_1 extends Team4516ibc1_0 {\n" +
			"    protected class R1 extends R0 {\n" +
			"		 @Override\n" +
			"		 callin void ci() {\n" +
			"			 System.out.print(\"T1.ci\");\n" +
			"			 base.ci();\n" +
			"		 }\n" +
			"	 }\n" +
			"}\n",
    	"Team4516ibc1_0.java",
    		"public team class Team4516ibc1_0 {\n" +
    		"    protected class R0 playedBy B4516ibc1 {\n" +
    		"		 callin void ci() {\n" +
            "			 System.out.print(\"T0.ci\");\n" +
            "			 base.ci();\n" +
    		"		 }\n" +
    		"		 ci <- replace bm;\n" +
    		"	 }\n" +
    		"}\n",
    	"B4516ibc1.java",
    		"public class B4516ibc1 {\n" +
    		"	 public void bm() {\n" +
    		"		 System.out.print(\"bm\");\n" +
    		"	 }\n" +
    		"}\n"
    		},
    		"T1.cibm");
    }

    // a callin with base call is involved in both explicit and implicit inheritance
    // witness for bogus complaint re missing base call
    public void test4516_inheritedBaseCall2() {
    	Map customOptions = getCompilerOptions();
    	customOptions.put(CompilerOptions.OPTION_ReportNotExactlyOneBasecall, CompilerOptions.ERROR);
    	runConformTest(
    		new String[] {
		"Team4516ibc2_2.java",
			"public team class Team4516ibc2_2 extends Team4516ibc2_1 {\n" +
			"	 @Override\n" +
			"    protected class R1 {}\n" +
			"	 public static void main(String... args) {\n" +
			"		 new Team4516ibc2_2().activate();\n" +
			"		 new B4516ibc2().bm();\n" +
			"	 }\n" +
			"}\n",
		"Team4516ibc2_1.java",
			"public team class Team4516ibc2_1 extends Team4516ibc2_0 {\n" +
			"    protected class R1 extends R0 {\n" +
			"		 @Override\n" +
			"		 callin void ci() {\n" +
			"			 System.out.print(\"T1.ci\");\n" +
			"			 super.ci();\n" +
			"		 }\n" +
			"	 }\n" +
			"}\n",
    	"Team4516ibc2_0.java",
    		"public team class Team4516ibc2_0 {\n" +
    		"    protected class R0 playedBy B4516ibc2 {\n" +
    		"		 callin void ci() {\n" +
            "			 System.out.print(\"T0.ci\");\n" +
            "			 base.ci();\n" +
    		"		 }\n" +
    		"		 ci <- replace bm;\n" +
    		"	 }\n" +
    		"}\n",
    	"B4516ibc2.java",
    		"public class B4516ibc2 {\n" +
    		"	 public void bm() {\n" +
    		"		 System.out.print(\"bm\");\n" +
    		"	 }\n" +
    		"}\n"
    		},
    		"T1.ciT0.cibm",
    		null/*classLibraries*/,
    		true/*shouldFlushOutputDirectory*/,
    		null/*vmArguments*/,
    		customOptions,
    		null/*requestor*/);
    }

    // a callin with base call is involved in both explicit and implicit inheritance
    // witness for bogus complaint re missing base call - super potentially calls base
    public void test4516_inheritedBaseCall3() {
    	Map customOptions = getCompilerOptions();
    	customOptions.put(CompilerOptions.OPTION_ReportNotExactlyOneBasecall, CompilerOptions.ERROR);

    	runTest(
    		new String[] {
		"Team4516ibc3_2.java",
			"public team class Team4516ibc3_2 extends Team4516ibc3_1 {\n" +
			"	 @Override\n" +
			"    protected class R1 {}\n" +
			"	 public static void main(String... args) {\n" +
			"		 new Team4516ibc3_2().activate();\n" +
			"		 new B4516ibc3().bm(true);\n" +
			"		 new B4516ibc3().bm(false);\n" +
			"	 }\n" +
			"}\n",
		"Team4516ibc3_1.java",
			"public team class Team4516ibc3_1 {\n" +
			"    protected class R1 extends R0 {\n" +
			"		 @Override\n" +
			"		 callin void ci(boolean f) {\n" +
			"			 System.out.print(\"R1.ci\");\n" +
			"			 super.ci(f);\n" + // this definite call potentially triggers a base-call
			"		 }\n" +
			"	 }\n" +
    		"    protected class R0 playedBy B4516ibc3 {\n" +
    		"		 callin void ci(boolean f) {\n" +
            "			 System.out.print(\"R0.ci\");\n" +
            "			 if (f)\n" +
            "			 	 base.ci(f);\n" +
    		"		 }\n" +
    		"		 ci <- replace bm;\n" +
    		"	 }\n" +
    		"}\n",
    	"B4516ibc3.java",
    		"public class B4516ibc3 {\n" +
    		"	 public void bm(boolean f) {\n" +
    		"		 System.out.print(\"bm\");\n" +
    		"	 }\n" +
    		"}\n"
    		},
    		true,
    		"----------\n" +
    		"1. ERROR in Team4516ibc3_1.java (at line 4)\n" +
    		"	callin void ci(boolean f) {\n" +
    		"	            ^^^^^^^^^^^^^\n" +
    		"Potentially missing base call in callin method (OTJLD 4.3(b)).\n" +
    		"----------\n" +
    		"2. ERROR in Team4516ibc3_1.java (at line 10)\n" +
    		"	callin void ci(boolean f) {\n" +
    		"	            ^^^^^^^^^^^^^\n" +
    		"Potentially missing base call in callin method (OTJLD 4.3(b)).\n" +
    		"----------\n",
    		"R1.ciR0.cibmR1.ciR0.ci",
    		null/*expectedErrorString*/,
    		true/*forceExecution*/,
    		null/*classLibraries*/,
    		true/*shouldFlushOutputDirectory*/,
    		null/*vmArguments*/,
    		customOptions/*customOptions*/,
    		null/*requestor*/,
    		true/*skipJava*/);
    }

    // a callin with base call is involved in both explicit and implicit inheritance
    // witness for bogus complaint re missing base call - super and this both potentially call base
    public void test4516_inheritedBaseCall4() {
    	Map customOptions = getCompilerOptions();
    	customOptions.put(CompilerOptions.OPTION_ReportNotExactlyOneBasecall, CompilerOptions.ERROR);

    	runTest(
    		new String[] {
		"Team4516ibc4_2.java",
			"public team class Team4516ibc4_2 extends Team4516ibc4_1 {\n" +
			"	 @Override\n" +
			"    protected class R1 {}\n" +
			"	 public static void main(String... args) {\n" +
			"		 new Team4516ibc4_2().activate();\n" +
			"		 new B4516ibc4().bm(0);\n" +
			"		 new B4516ibc4().bm(1);\n" +
			"		 new B4516ibc4().bm(2);\n" +
			"	 }\n" +
			"}\n",
		"Team4516ibc4_1.java",
			"public team class Team4516ibc4_1 {\n" +
			"    protected class R1 extends R0 {\n" +
			"		 @Override\n" +
			"		 callin void ci(int i) {\n" +
			"			 System.out.print(\"R1.ci\");\n" +
			"			 if (i > 0)\n" +
			"			 	 super.ci(i);\n" + // doubly potential base-call
			"		 }\n" +
			"	 }\n" +
    		"    protected class R0 playedBy B4516ibc4 {\n" +
    		"		 callin void ci(int i) {\n" +
            "			 System.out.print(\"R0.ci\");\n" +
            "			 if (i > 1)\n" +
            "			 	 base.ci(i);\n" +
    		"		 }\n" +
    		"		 ci <- replace bm;\n" +
    		"	 }\n" +
    		"}\n",
    	"B4516ibc4.java",
    		"public class B4516ibc4 {\n" +
    		"	 public void bm(int i) {\n" +
    		"		 System.out.print(\"bm\");\n" +
    		"	 }\n" +
    		"}\n"
    		},
    		true,
    		"----------\n" +
    		"1. ERROR in Team4516ibc4_1.java (at line 4)\n" +
    		"	callin void ci(int i) {\n" +
    		"	            ^^^^^^^^^\n" +
    		"Potentially missing base call in callin method (OTJLD 4.3(b)).\n" +
    		"----------\n" +
    		"2. ERROR in Team4516ibc4_1.java (at line 11)\n" +
    		"	callin void ci(int i) {\n" +
    		"	            ^^^^^^^^^\n" +
    		"Potentially missing base call in callin method (OTJLD 4.3(b)).\n" +
    		"----------\n",
    		"R1.ciR1.ciR0.ciR1.ciR0.cibm",
    		null/*expectedErrorString*/,
    		true/*forceExecution*/,
    		null/*classLibraries*/,
    		true/*shouldFlushOutputDirectory*/,
    		null/*vmArguments*/,
    		customOptions/*customOptions*/,
    		null/*requestor*/,
    		true/*skipJava*/);
    }

    // a callin with base call is involved in both explicit and implicit inheritance
    // witness for bogus complaint re missing base call - super potentially calls base, this definitely
    public void test4516_inheritedBaseCall5() {
    	Map customOptions = getCompilerOptions();
    	customOptions.put(CompilerOptions.OPTION_ReportNotExactlyOneBasecall, CompilerOptions.ERROR);

    	runTest(
    		new String[] {
		"Team4516ibc5_2.java",
			"public team class Team4516ibc5_2 extends Team4516ibc5_1 {\n" +
			"	 @Override\n" +
			"    protected class R1 {}\n" +
			"	 public static void main(String... args) {\n" +
			"		 new Team4516ibc5_2().activate();\n" +
			"		 new B4516ibc5().bm(0);\n" +
			"		 System.out.print(\"-\");\n" +
			"		 new B4516ibc5().bm(1);\n" +
			"		 System.out.print(\"-\");\n" +
			"		 new B4516ibc5().bm(2);\n" +
			"	 }\n" +
			"}\n",
		"Team4516ibc5_1.java",
			"public team class Team4516ibc5_1 {\n" +
			"    protected class R1 extends R0 {\n" +
			"		 @Override\n" +
			"		 callin void ci(int i) {\n" +
			"			 System.out.print(\"R1.ci\");\n" +
			"			 base.ci(i);\n" +		// definite
			"			 if (i > 0)\n" +
			"			 	 super.ci(i);\n" + // plus potential -> potentially duplicate
			"		 }\n" +
			"	 }\n" +
    		"    protected class R0 playedBy B4516ibc5 {\n" +
    		"		 callin void ci(int i) {\n" +
            "			 System.out.print(\"R0.ci\");\n" +
            "			 if (i > 1)\n" +
            "			 	 base.ci(i);\n" +
    		"		 }\n" +
    		"		 ci <- replace bm;\n" +
    		"	 }\n" +
    		"}\n",
    	"B4516ibc5.java",
    		"public class B4516ibc5 {\n" +
    		"	 public void bm(int i) {\n" +
    		"		 System.out.print(\"bm\");\n" +
    		"	 }\n" +
    		"}\n"
    		},
    		true,
    		"----------\n" +
    		"1. ERROR in Team4516ibc5_1.java (at line 8)\n" +
    		"	super.ci(i);\n" +
    		"	^^^^^^^^^^^\n" +
    		"Potentially duplicate base call (OTJLD 4.3(c)).\n" +
    		"----------\n" +
    		"2. ERROR in Team4516ibc5_1.java (at line 12)\n" +
    		"	callin void ci(int i) {\n" +
    		"	            ^^^^^^^^^\n" +
    		"Potentially missing base call in callin method (OTJLD 4.3(b)).\n" +
    		"----------\n",
    		"R1.cibm-R1.cibmR0.ci-R1.cibmR0.cibm",
    		null/*expectedErrorString*/,
    		true/*forceExecution*/,
    		null/*classLibraries*/,
    		true/*shouldFlushOutputDirectory*/,
    		null/*vmArguments*/,
    		customOptions/*customOptions*/,
    		null/*requestor*/,
    		true/*skipJava*/);
    }

    public void testBug495463() {
    	runNegativeTest(
    		new String[] {
    			"MyTeam.java",
    			"public team class MyTeam {\n" +
    			"	protected class R {\n" +
    			" 		callin void foo(int arga, int arg2) {\n" +
    			"			base.foo(missing, false);\n" +
    			"		}\n" +
    			"	}\n" +
    			"}\n"
    		},
    		"----------\n" +
			"1. ERROR in MyTeam.java (at line 4)\n" +
			"	base.foo(missing, false);\n" +
			"	         ^^^^^^^\n" +
			"missing cannot be resolved to a variable\n" +
			"----------\n");
    }

    public void testBug495463_b() {
    	runNegativeTest(
    		new String[] {
    			"MyTeam.java",
    			"public team class MyTeam {\n" +
    			"	protected class R {\n" +
    			" 		callin void foo(int arga, int arg2) {\n" +
    			"			base.foo(1, false);\n" +
    			"		}\n" +
    			"	}\n" +
    			"}\n"
    		},
    		"----------\n" +
			"1. ERROR in MyTeam.java (at line 4)\n" +
			"	base.foo(1, false);\n" +
			"	     ^^^\n" +
			"Base call foo(int, int) is not applicable for the arguments (int, boolean)\n" +
			"----------\n");
    }
}
