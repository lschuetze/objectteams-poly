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
package org.eclipse.objectteams.otdt.tests.otjld.liftlower;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class SmartLifting23 extends AbstractOTJLDTest {

	public SmartLifting23(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test0c11_implicitlyInheritingStaticRoleMethod1"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test buildComparableTestSuite(Class evaluationTestClass) {
		Test suite = buildMinimalComplianceTestSuite(evaluationTestClass, F_1_6); // one compliance level is enough for smart lifting tests.
		TESTS_COUNTERS.put(evaluationTestClass.getName(), new Integer(suite.countTestCases()));
		return suite;
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return SmartLifting23.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.23-otjld-smart-lifting-1
    public void test6423_smartLifting1() {
        runNegativeTest(
            new String[] {
		"Team6423sl1_4.java",
			    "\n" +
			    "public team class Team6423sl1_4 extends Team6423sl1_3 {\n" +
			    "    public class Role6423sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl1_4.this.toString() + \".Role6423sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl1Main.java",
			    "\n" +
			    "public class T6423sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6423sl1_2 t = new Team6423sl1_2();\n" +
			    "        T6423sl1_5    o = new T6423sl1_5();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl1_3.java",
			    "\n" +
			    "public team class Team6423sl1_3 extends Team6423sl1_2 {\n" +
			    "    public class Role6423sl1_5 extends Role6423sl1_3 playedBy T6423sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl1_3.this.toString() + \".Role6423sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl1_6.java",
			    "\n" +
			    "public class T6423sl1_6 extends T6423sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl1_2.java",
			    "\n" +
			    "public abstract class T6423sl1_2 extends T6423sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl1_3.java",
			    "\n" +
			    "public class T6423sl1_3 extends T6423sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl1_1.java",
			    "\n" +
			    "public team class Team6423sl1_1 {\n" +
			    "    public class Role6423sl1_1 extends T6423sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl1_1.this.toString() + \".Role6423sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6423sl1_2 extends Role6423sl1_1 playedBy T6423sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl1_1.this.toString() + \".Role6423sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6423sl1_3 extends Role6423sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl1_1.this.toString() + \".Role6423sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl1_4.java",
			    "\n" +
			    "public class T6423sl1_4 extends T6423sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl1_2.java",
			    "\n" +
			    "public team class Team6423sl1_2 extends Team6423sl1_1 {\n" +
			    "    public class Role6423sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl1_2.this.toString() + \".Role6423sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6423sl1_4 extends Role6423sl1_3 playedBy T6423sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl1_2.this.toString() + \".Role6423sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6423sl1_5 as Role6423sl1_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl1_5.java",
			    "\n" +
			    "public class T6423sl1_5 extends T6423sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl1_1.java",
			    "\n" +
			    "public class T6423sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.23-otjld-smart-lifting-2
    public void test6423_smartLifting2() {
        runNegativeTest(
            new String[] {
		"Team6423sl2_4.java",
			    "\n" +
			    "public team class Team6423sl2_4 extends Team6423sl2_3 {\n" +
			    "    public class Role6423sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl2_4.this.toString() + \".Role6423sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl2Main.java",
			    "\n" +
			    "public class T6423sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6423sl2_2 t = new Team6423sl2_3();\n" +
			    "        T6423sl2_5    o = new T6423sl2_5();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl2_2.java",
			    "\n" +
			    "public team class Team6423sl2_2 extends Team6423sl2_1 {\n" +
			    "    public class Role6423sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl2_2.this.toString() + \".Role6423sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6423sl2_4 extends Role6423sl2_3 playedBy T6423sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl2_2.this.toString() + \".Role6423sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6423sl2_5 as Role6423sl2_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl2_5.java",
			    "\n" +
			    "public class T6423sl2_5 extends T6423sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl2_1.java",
			    "\n" +
			    "public class T6423sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl2_3.java",
			    "\n" +
			    "public team class Team6423sl2_3 extends Team6423sl2_2 {\n" +
			    "    public class Role6423sl2_5 extends Role6423sl2_3 playedBy T6423sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl2_3.this.toString() + \".Role6423sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl2_6.java",
			    "\n" +
			    "public class T6423sl2_6 extends T6423sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl2_2.java",
			    "\n" +
			    "public abstract class T6423sl2_2 extends T6423sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl2_3.java",
			    "\n" +
			    "public class T6423sl2_3 extends T6423sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl2_1.java",
			    "\n" +
			    "public team class Team6423sl2_1 {\n" +
			    "    public class Role6423sl2_1 extends T6423sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl2_1.this.toString() + \".Role6423sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6423sl2_2 extends Role6423sl2_1 playedBy T6423sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl2_1.this.toString() + \".Role6423sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6423sl2_3 extends Role6423sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl2_1.this.toString() + \".Role6423sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl2_4.java",
			    "\n" +
			    "public class T6423sl2_4 extends T6423sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.23-otjld-smart-lifting-3
    public void test6423_smartLifting3() {
        runNegativeTest(
            new String[] {
		"Team6423sl3_4.java",
			    "\n" +
			    "public team class Team6423sl3_4 extends Team6423sl3_3 {\n" +
			    "    public class Role6423sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl3_4.this.toString() + \".Role6423sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl3Main.java",
			    "\n" +
			    "public class T6423sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6423sl3_2 t = new Team6423sl3_4();\n" +
			    "        T6423sl3_5    o = new T6423sl3_5();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl3_1.java",
			    "\n" +
			    "public team class Team6423sl3_1 {\n" +
			    "    public class Role6423sl3_1 extends T6423sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl3_1.this.toString() + \".Role6423sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6423sl3_2 extends Role6423sl3_1 playedBy T6423sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl3_1.this.toString() + \".Role6423sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6423sl3_3 extends Role6423sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl3_1.this.toString() + \".Role6423sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl3_4.java",
			    "\n" +
			    "public class T6423sl3_4 extends T6423sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl3_2.java",
			    "\n" +
			    "public team class Team6423sl3_2 extends Team6423sl3_1 {\n" +
			    "    public class Role6423sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl3_2.this.toString() + \".Role6423sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6423sl3_4 extends Role6423sl3_3 playedBy T6423sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl3_2.this.toString() + \".Role6423sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6423sl3_5 as Role6423sl3_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl3_5.java",
			    "\n" +
			    "public class T6423sl3_5 extends T6423sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl3_1.java",
			    "\n" +
			    "public class T6423sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl3_3.java",
			    "\n" +
			    "public team class Team6423sl3_3 extends Team6423sl3_2 {\n" +
			    "    public class Role6423sl3_5 extends Role6423sl3_3 playedBy T6423sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl3_3.this.toString() + \".Role6423sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl3_6.java",
			    "\n" +
			    "public class T6423sl3_6 extends T6423sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl3_2.java",
			    "\n" +
			    "public abstract class T6423sl3_2 extends T6423sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl3_3.java",
			    "\n" +
			    "public class T6423sl3_3 extends T6423sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.23-otjld-smart-lifting-4
    public void test6423_smartLifting4() {
        runNegativeTest(
            new String[] {
		"Team6423sl4_4.java",
			    "\n" +
			    "public team class Team6423sl4_4 extends Team6423sl4_3 {\n" +
			    "    public class Role6423sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl4_4.this.toString() + \".Role6423sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl4Main.java",
			    "\n" +
			    "public class T6423sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6423sl4_2 t = new Team6423sl4_2();\n" +
			    "        T6423sl4_5    o = new T6423sl4_6();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl4_3.java",
			    "\n" +
			    "public class T6423sl4_3 extends T6423sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl4_1.java",
			    "\n" +
			    "public team class Team6423sl4_1 {\n" +
			    "    public class Role6423sl4_1 extends T6423sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl4_1.this.toString() + \".Role6423sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6423sl4_2 extends Role6423sl4_1 playedBy T6423sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl4_1.this.toString() + \".Role6423sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6423sl4_3 extends Role6423sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl4_1.this.toString() + \".Role6423sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl4_4.java",
			    "\n" +
			    "public class T6423sl4_4 extends T6423sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl4_2.java",
			    "\n" +
			    "public team class Team6423sl4_2 extends Team6423sl4_1 {\n" +
			    "    public class Role6423sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl4_2.this.toString() + \".Role6423sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6423sl4_4 extends Role6423sl4_3 playedBy T6423sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl4_2.this.toString() + \".Role6423sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6423sl4_5 as Role6423sl4_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl4_5.java",
			    "\n" +
			    "public class T6423sl4_5 extends T6423sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl4_1.java",
			    "\n" +
			    "public class T6423sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl4_3.java",
			    "\n" +
			    "public team class Team6423sl4_3 extends Team6423sl4_2 {\n" +
			    "    public class Role6423sl4_5 extends Role6423sl4_3 playedBy T6423sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl4_3.this.toString() + \".Role6423sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl4_6.java",
			    "\n" +
			    "public class T6423sl4_6 extends T6423sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl4_2.java",
			    "\n" +
			    "public abstract class T6423sl4_2 extends T6423sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.23-otjld-smart-lifting-5
    public void test6423_smartLifting5() {
        runNegativeTest(
            new String[] {
		"Team6423sl5_4.java",
			    "\n" +
			    "public team class Team6423sl5_4 extends Team6423sl5_3 {\n" +
			    "    public class Role6423sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl5_4.this.toString() + \".Role6423sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl5Main.java",
			    "\n" +
			    "public class T6423sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6423sl5_2 t = new Team6423sl5_3();\n" +
			    "        T6423sl5_5    o = new T6423sl5_6();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl5_3.java",
			    "\n" +
			    "public team class Team6423sl5_3 extends Team6423sl5_2 {\n" +
			    "    public class Role6423sl5_5 extends Role6423sl5_3 playedBy T6423sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl5_3.this.toString() + \".Role6423sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl5_6.java",
			    "\n" +
			    "public class T6423sl5_6 extends T6423sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl5_2.java",
			    "\n" +
			    "public abstract class T6423sl5_2 extends T6423sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl5_3.java",
			    "\n" +
			    "public class T6423sl5_3 extends T6423sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl5_1.java",
			    "\n" +
			    "public team class Team6423sl5_1 {\n" +
			    "    public class Role6423sl5_1 extends T6423sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl5_1.this.toString() + \".Role6423sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6423sl5_2 extends Role6423sl5_1 playedBy T6423sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl5_1.this.toString() + \".Role6423sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6423sl5_3 extends Role6423sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl5_1.this.toString() + \".Role6423sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl5_4.java",
			    "\n" +
			    "public class T6423sl5_4 extends T6423sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl5_2.java",
			    "\n" +
			    "public team class Team6423sl5_2 extends Team6423sl5_1 {\n" +
			    "    public class Role6423sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl5_2.this.toString() + \".Role6423sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6423sl5_4 extends Role6423sl5_3 playedBy T6423sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl5_2.this.toString() + \".Role6423sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6423sl5_5 as Role6423sl5_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl5_5.java",
			    "\n" +
			    "public class T6423sl5_5 extends T6423sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl5_1.java",
			    "\n" +
			    "public class T6423sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.23-otjld-smart-lifting-6
    public void test6423_smartLifting6() {
        runNegativeTest(
            new String[] {
		"Team6423sl6_4.java",
			    "\n" +
			    "public team class Team6423sl6_4 extends Team6423sl6_3 {\n" +
			    "    public class Role6423sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl6_4.this.toString() + \".Role6423sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl6Main.java",
			    "\n" +
			    "public class T6423sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6423sl6_2 t = new Team6423sl6_4();\n" +
			    "        T6423sl6_5    o = new T6423sl6_6();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl6_2.java",
			    "\n" +
			    "public team class Team6423sl6_2 extends Team6423sl6_1 {\n" +
			    "    public class Role6423sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl6_2.this.toString() + \".Role6423sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6423sl6_4 extends Role6423sl6_3 playedBy T6423sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl6_2.this.toString() + \".Role6423sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6423sl6_5 as Role6423sl6_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl6_5.java",
			    "\n" +
			    "public class T6423sl6_5 extends T6423sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl6_1.java",
			    "\n" +
			    "public class T6423sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl6_3.java",
			    "\n" +
			    "public team class Team6423sl6_3 extends Team6423sl6_2 {\n" +
			    "    public class Role6423sl6_5 extends Role6423sl6_3 playedBy T6423sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl6_3.this.toString() + \".Role6423sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl6_6.java",
			    "\n" +
			    "public class T6423sl6_6 extends T6423sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl6_2.java",
			    "\n" +
			    "public abstract class T6423sl6_2 extends T6423sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl6_3.java",
			    "\n" +
			    "public class T6423sl6_3 extends T6423sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl6_1.java",
			    "\n" +
			    "public team class Team6423sl6_1 {\n" +
			    "    public class Role6423sl6_1 extends T6423sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl6_1.this.toString() + \".Role6423sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6423sl6_2 extends Role6423sl6_1 playedBy T6423sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl6_1.this.toString() + \".Role6423sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6423sl6_3 extends Role6423sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl6_1.this.toString() + \".Role6423sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl6_4.java",
			    "\n" +
			    "public class T6423sl6_4 extends T6423sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.23-otjld-smart-lifting-7
    public void test6423_smartLifting7() {
        runNegativeTest(
            new String[] {
		"Team6423sl7_4.java",
			    "\n" +
			    "public team class Team6423sl7_4 extends Team6423sl7_3 {\n" +
			    "    public class Role6423sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl7_4.this.toString() + \".Role6423sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl7Main.java",
			    "\n" +
			    "public class T6423sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6423sl7_2       t  = new Team6423sl7_2();\n" +
			    "        final Team6423sl7_1 ft = new Team6423sl7_1();\n" +
			    "        T6423sl7_5          o  = ft.new Role6423sl7_1();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl7_1.java",
			    "\n" +
			    "public team class Team6423sl7_1 {\n" +
			    "    public class Role6423sl7_1 extends T6423sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl7_1.this.toString() + \".Role6423sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6423sl7_2 extends Role6423sl7_1 playedBy T6423sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl7_1.this.toString() + \".Role6423sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6423sl7_3 extends Role6423sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl7_1.this.toString() + \".Role6423sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl7_4.java",
			    "\n" +
			    "public class T6423sl7_4 extends T6423sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl7_2.java",
			    "\n" +
			    "public team class Team6423sl7_2 extends Team6423sl7_1 {\n" +
			    "    public class Role6423sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl7_2.this.toString() + \".Role6423sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6423sl7_4 extends Role6423sl7_3 playedBy T6423sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl7_2.this.toString() + \".Role6423sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6423sl7_5 as Role6423sl7_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl7_5.java",
			    "\n" +
			    "public class T6423sl7_5 extends T6423sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl7_1.java",
			    "\n" +
			    "public class T6423sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl7_3.java",
			    "\n" +
			    "public team class Team6423sl7_3 extends Team6423sl7_2 {\n" +
			    "    public class Role6423sl7_5 extends Role6423sl7_3 playedBy T6423sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl7_3.this.toString() + \".Role6423sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl7_6.java",
			    "\n" +
			    "public class T6423sl7_6 extends T6423sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl7_2.java",
			    "\n" +
			    "public abstract class T6423sl7_2 extends T6423sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl7_3.java",
			    "\n" +
			    "public class T6423sl7_3 extends T6423sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.23-otjld-smart-lifting-8
    public void test6423_smartLifting8() {
        runNegativeTest(
            new String[] {
		"Team6423sl8_4.java",
			    "\n" +
			    "public team class Team6423sl8_4 extends Team6423sl8_3 {\n" +
			    "    public class Role6423sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl8_4.this.toString() + \".Role6423sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl8Main.java",
			    "\n" +
			    "public class T6423sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6423sl8_2       t  = new Team6423sl8_3();\n" +
			    "        final Team6423sl8_1 ft = new Team6423sl8_1();\n" +
			    "        T6423sl8_5          o  = ft.new Role6423sl8_1();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl8_3.java",
			    "\n" +
			    "public class T6423sl8_3 extends T6423sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl8_1.java",
			    "\n" +
			    "public team class Team6423sl8_1 {\n" +
			    "    public class Role6423sl8_1 extends T6423sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl8_1.this.toString() + \".Role6423sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6423sl8_2 extends Role6423sl8_1 playedBy T6423sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl8_1.this.toString() + \".Role6423sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6423sl8_3 extends Role6423sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl8_1.this.toString() + \".Role6423sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl8_4.java",
			    "\n" +
			    "public class T6423sl8_4 extends T6423sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl8_2.java",
			    "\n" +
			    "public team class Team6423sl8_2 extends Team6423sl8_1 {\n" +
			    "    public class Role6423sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl8_2.this.toString() + \".Role6423sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6423sl8_4 extends Role6423sl8_3 playedBy T6423sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl8_2.this.toString() + \".Role6423sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6423sl8_5 as Role6423sl8_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl8_5.java",
			    "\n" +
			    "public class T6423sl8_5 extends T6423sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl8_1.java",
			    "\n" +
			    "public class T6423sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl8_3.java",
			    "\n" +
			    "public team class Team6423sl8_3 extends Team6423sl8_2 {\n" +
			    "    public class Role6423sl8_5 extends Role6423sl8_3 playedBy T6423sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl8_3.this.toString() + \".Role6423sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl8_6.java",
			    "\n" +
			    "public class T6423sl8_6 extends T6423sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl8_2.java",
			    "\n" +
			    "public abstract class T6423sl8_2 extends T6423sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.23-otjld-smart-lifting-9
    public void test6423_smartLifting9() {
        runNegativeTest(
            new String[] {
		"Team6423sl9_4.java",
			    "\n" +
			    "public team class Team6423sl9_4 extends Team6423sl9_3 {\n" +
			    "    public class Role6423sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl9_4.this.toString() + \".Role6423sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl9Main.java",
			    "\n" +
			    "public class T6423sl9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6423sl9_2       t  = new Team6423sl9_4();\n" +
			    "        final Team6423sl9_1 ft = new Team6423sl9_1();\n" +
			    "        T6423sl9_5          o  = ft.new Role6423sl9_1();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl9_3.java",
			    "\n" +
			    "public team class Team6423sl9_3 extends Team6423sl9_2 {\n" +
			    "    public class Role6423sl9_5 extends Role6423sl9_3 playedBy T6423sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl9_3.this.toString() + \".Role6423sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl9_6.java",
			    "\n" +
			    "public class T6423sl9_6 extends T6423sl9_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl9_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl9_2.java",
			    "\n" +
			    "public abstract class T6423sl9_2 extends T6423sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl9_3.java",
			    "\n" +
			    "public class T6423sl9_3 extends T6423sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl9_1.java",
			    "\n" +
			    "public team class Team6423sl9_1 {\n" +
			    "    public class Role6423sl9_1 extends T6423sl9_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl9_1.this.toString() + \".Role6423sl9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6423sl9_2 extends Role6423sl9_1 playedBy T6423sl9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl9_1.this.toString() + \".Role6423sl9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6423sl9_3 extends Role6423sl9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl9_1.this.toString() + \".Role6423sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6423sl9_4.java",
			    "\n" +
			    "public class T6423sl9_4 extends T6423sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6423sl9_2.java",
			    "\n" +
			    "public team class Team6423sl9_2 extends Team6423sl9_1 {\n" +
			    "    public class Role6423sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl9_2.this.toString() + \".Role6423sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6423sl9_4 extends Role6423sl9_3 playedBy T6423sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6423sl9_2.this.toString() + \".Role6423sl9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6423sl9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6423sl9_5 as Role6423sl9_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl9_5.java",
			    "\n" +
			    "public class T6423sl9_5 extends T6423sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl9_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6423sl9_1.java",
			    "\n" +
			    "public class T6423sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6423sl9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }
}
