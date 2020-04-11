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

public class SmartLifting7 extends AbstractOTJLDTest {

	public SmartLifting7(String name) {
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
		return SmartLifting7.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-1
    public void test647_smartLifting1() {

       runConformTest(
            new String[] {
		"T647sl1Main.java",
			    "\n" +
			    "public class T647sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl1_1 t = new Team647sl1_1();\n" +
			    "        T647sl1_1    o = new T647sl1_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl1_4.java",
			    "\n" +
			    "public team class Team647sl1_4 extends Team647sl1_3 {\n" +
			    "    public class Role647sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl1_4.this.toString() + \".Role647sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl1_1.java",
			    "\n" +
			    "public team class Team647sl1_1 {\n" +
			    "    public class Role647sl1_1 extends T647sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl1_1.this.toString() + \".Role647sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl1_2 extends Role647sl1_1 playedBy T647sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl1_1.this.toString() + \".Role647sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl1_3 extends Role647sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl1_1.this.toString() + \".Role647sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl1_1 as Role647sl1_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl1_4.java",
			    "\n" +
			    "public class T647sl1_4 extends T647sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl1_2.java",
			    "\n" +
			    "public team class Team647sl1_2 extends Team647sl1_1 {\n" +
			    "    public class Role647sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl1_2.this.toString() + \".Role647sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl1_4 extends Role647sl1_3 playedBy T647sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl1_2.this.toString() + \".Role647sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl1_5.java",
			    "\n" +
			    "public class T647sl1_5 extends T647sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl1_1.java",
			    "\n" +
			    "public class T647sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl1_3.java",
			    "\n" +
			    "public team class Team647sl1_3 extends Team647sl1_2 {\n" +
			    "    public class Role647sl1_5 extends Role647sl1_3 playedBy T647sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl1_3.this.toString() + \".Role647sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl1_6.java",
			    "\n" +
			    "public class T647sl1_6 extends T647sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl1_2.java",
			    "\n" +
			    "public abstract class T647sl1_2 extends T647sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl1_3.java",
			    "\n" +
			    "public class T647sl1_3 extends T647sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl1_1.Role647sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-2
    public void test647_smartLifting2() {

       runConformTest(
            new String[] {
		"T647sl2Main.java",
			    "\n" +
			    "public class T647sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl2_1 t = new Team647sl2_2();\n" +
			    "        T647sl2_1    o = new T647sl2_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl2_4.java",
			    "\n" +
			    "public team class Team647sl2_4 extends Team647sl2_3 {\n" +
			    "    public class Role647sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl2_4.this.toString() + \".Role647sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl2_3.java",
			    "\n" +
			    "public class T647sl2_3 extends T647sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl2_1.java",
			    "\n" +
			    "public team class Team647sl2_1 {\n" +
			    "    public class Role647sl2_1 extends T647sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl2_1.this.toString() + \".Role647sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl2_2 extends Role647sl2_1 playedBy T647sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl2_1.this.toString() + \".Role647sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl2_3 extends Role647sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl2_1.this.toString() + \".Role647sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl2_1 as Role647sl2_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl2_4.java",
			    "\n" +
			    "public class T647sl2_4 extends T647sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl2_2.java",
			    "\n" +
			    "public team class Team647sl2_2 extends Team647sl2_1 {\n" +
			    "    public class Role647sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl2_2.this.toString() + \".Role647sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl2_4 extends Role647sl2_3 playedBy T647sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl2_2.this.toString() + \".Role647sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl2_5.java",
			    "\n" +
			    "public class T647sl2_5 extends T647sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl2_1.java",
			    "\n" +
			    "public class T647sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl2_3.java",
			    "\n" +
			    "public team class Team647sl2_3 extends Team647sl2_2 {\n" +
			    "    public class Role647sl2_5 extends Role647sl2_3 playedBy T647sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl2_3.this.toString() + \".Role647sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl2_6.java",
			    "\n" +
			    "public class T647sl2_6 extends T647sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl2_2.java",
			    "\n" +
			    "public abstract class T647sl2_2 extends T647sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl2_2.Role647sl2_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-3
    public void test647_smartLifting3() {

       runConformTest(
            new String[] {
		"T647sl3Main.java",
			    "\n" +
			    "public class T647sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl3_1 t = new Team647sl3_3();\n" +
			    "        T647sl3_1    o = new T647sl3_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl3_4.java",
			    "\n" +
			    "public team class Team647sl3_4 extends Team647sl3_3 {\n" +
			    "    public class Role647sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl3_4.this.toString() + \".Role647sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl3_3.java",
			    "\n" +
			    "public team class Team647sl3_3 extends Team647sl3_2 {\n" +
			    "    public class Role647sl3_5 extends Role647sl3_3 playedBy T647sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl3_3.this.toString() + \".Role647sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl3_6.java",
			    "\n" +
			    "public class T647sl3_6 extends T647sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl3_2.java",
			    "\n" +
			    "public abstract class T647sl3_2 extends T647sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl3_3.java",
			    "\n" +
			    "public class T647sl3_3 extends T647sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl3_1.java",
			    "\n" +
			    "public team class Team647sl3_1 {\n" +
			    "    public class Role647sl3_1 extends T647sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl3_1.this.toString() + \".Role647sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl3_2 extends Role647sl3_1 playedBy T647sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl3_1.this.toString() + \".Role647sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl3_3 extends Role647sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl3_1.this.toString() + \".Role647sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl3_1 as Role647sl3_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl3_4.java",
			    "\n" +
			    "public class T647sl3_4 extends T647sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl3_2.java",
			    "\n" +
			    "public team class Team647sl3_2 extends Team647sl3_1 {\n" +
			    "    public class Role647sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl3_2.this.toString() + \".Role647sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl3_4 extends Role647sl3_3 playedBy T647sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl3_2.this.toString() + \".Role647sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl3_5.java",
			    "\n" +
			    "public class T647sl3_5 extends T647sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl3_1.java",
			    "\n" +
			    "public class T647sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl3_3.Role647sl3_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-4
    public void test647_smartLifting4() {

       runConformTest(
            new String[] {
		"T647sl4Main.java",
			    "\n" +
			    "public class T647sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl4_1 t = new Team647sl4_4();\n" +
			    "        T647sl4_1    o = new T647sl4_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl4_4.java",
			    "\n" +
			    "public team class Team647sl4_4 extends Team647sl4_3 {\n" +
			    "    public class Role647sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl4_4.this.toString() + \".Role647sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl4_2.java",
			    "\n" +
			    "public team class Team647sl4_2 extends Team647sl4_1 {\n" +
			    "    public class Role647sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl4_2.this.toString() + \".Role647sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl4_4 extends Role647sl4_3 playedBy T647sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl4_2.this.toString() + \".Role647sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl4_5.java",
			    "\n" +
			    "public class T647sl4_5 extends T647sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl4_1.java",
			    "\n" +
			    "public class T647sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl4_3.java",
			    "\n" +
			    "public team class Team647sl4_3 extends Team647sl4_2 {\n" +
			    "    public class Role647sl4_5 extends Role647sl4_3 playedBy T647sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl4_3.this.toString() + \".Role647sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl4_6.java",
			    "\n" +
			    "public class T647sl4_6 extends T647sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl4_2.java",
			    "\n" +
			    "public abstract class T647sl4_2 extends T647sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl4_3.java",
			    "\n" +
			    "public class T647sl4_3 extends T647sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl4_1.java",
			    "\n" +
			    "public team class Team647sl4_1 {\n" +
			    "    public class Role647sl4_1 extends T647sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl4_1.this.toString() + \".Role647sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl4_2 extends Role647sl4_1 playedBy T647sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl4_1.this.toString() + \".Role647sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl4_3 extends Role647sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl4_1.this.toString() + \".Role647sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl4_1 as Role647sl4_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl4_4.java",
			    "\n" +
			    "public class T647sl4_4 extends T647sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl4_4.Role647sl4_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-5
    public void test647_smartLifting5() {

       runConformTest(
            new String[] {
		"T647sl5Main.java",
			    "\n" +
			    "public class T647sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl5_1 t = new Team647sl5_1();\n" +
			    "        T647sl5_1    o = new T647sl5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl5_4.java",
			    "\n" +
			    "public team class Team647sl5_4 extends Team647sl5_3 {\n" +
			    "    public class Role647sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl5_4.this.toString() + \".Role647sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl5_1.java",
			    "\n" +
			    "public team class Team647sl5_1 {\n" +
			    "    public class Role647sl5_1 extends T647sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl5_1.this.toString() + \".Role647sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl5_2 extends Role647sl5_1 playedBy T647sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl5_1.this.toString() + \".Role647sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl5_3 extends Role647sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl5_1.this.toString() + \".Role647sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl5_1 as Role647sl5_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl5_4.java",
			    "\n" +
			    "public class T647sl5_4 extends T647sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl5_2.java",
			    "\n" +
			    "public team class Team647sl5_2 extends Team647sl5_1 {\n" +
			    "    public class Role647sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl5_2.this.toString() + \".Role647sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl5_4 extends Role647sl5_3 playedBy T647sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl5_2.this.toString() + \".Role647sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl5_5.java",
			    "\n" +
			    "public class T647sl5_5 extends T647sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl5_1.java",
			    "\n" +
			    "public class T647sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl5_3.java",
			    "\n" +
			    "public team class Team647sl5_3 extends Team647sl5_2 {\n" +
			    "    public class Role647sl5_5 extends Role647sl5_3 playedBy T647sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl5_3.this.toString() + \".Role647sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl5_6.java",
			    "\n" +
			    "public class T647sl5_6 extends T647sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl5_2.java",
			    "\n" +
			    "public abstract class T647sl5_2 extends T647sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl5_3.java",
			    "\n" +
			    "public class T647sl5_3 extends T647sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl5_1.Role647sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-6
    public void test647_smartLifting6() {

       runConformTest(
            new String[] {
		"T647sl6Main.java",
			    "\n" +
			    "public class T647sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl6_1 t = new Team647sl6_2();\n" +
			    "        T647sl6_1    o = new T647sl6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl6_4.java",
			    "\n" +
			    "public team class Team647sl6_4 extends Team647sl6_3 {\n" +
			    "    public class Role647sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl6_4.this.toString() + \".Role647sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl6_3.java",
			    "\n" +
			    "public class T647sl6_3 extends T647sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl6_1.java",
			    "\n" +
			    "public team class Team647sl6_1 {\n" +
			    "    public class Role647sl6_1 extends T647sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl6_1.this.toString() + \".Role647sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl6_2 extends Role647sl6_1 playedBy T647sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl6_1.this.toString() + \".Role647sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl6_3 extends Role647sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl6_1.this.toString() + \".Role647sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl6_1 as Role647sl6_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl6_4.java",
			    "\n" +
			    "public class T647sl6_4 extends T647sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl6_2.java",
			    "\n" +
			    "public team class Team647sl6_2 extends Team647sl6_1 {\n" +
			    "    public class Role647sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl6_2.this.toString() + \".Role647sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl6_4 extends Role647sl6_3 playedBy T647sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl6_2.this.toString() + \".Role647sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl6_5.java",
			    "\n" +
			    "public class T647sl6_5 extends T647sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl6_1.java",
			    "\n" +
			    "public class T647sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl6_3.java",
			    "\n" +
			    "public team class Team647sl6_3 extends Team647sl6_2 {\n" +
			    "    public class Role647sl6_5 extends Role647sl6_3 playedBy T647sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl6_3.this.toString() + \".Role647sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl6_6.java",
			    "\n" +
			    "public class T647sl6_6 extends T647sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl6_2.java",
			    "\n" +
			    "public abstract class T647sl6_2 extends T647sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl6_2.Role647sl6_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-7
    public void test647_smartLifting7() {

       runConformTest(
            new String[] {
		"T647sl7Main.java",
			    "\n" +
			    "public class T647sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl7_1 t = new Team647sl7_3();\n" +
			    "        T647sl7_1    o = new T647sl7_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl7_4.java",
			    "\n" +
			    "public team class Team647sl7_4 extends Team647sl7_3 {\n" +
			    "    public class Role647sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl7_4.this.toString() + \".Role647sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl7_3.java",
			    "\n" +
			    "public team class Team647sl7_3 extends Team647sl7_2 {\n" +
			    "    public class Role647sl7_5 extends Role647sl7_3 playedBy T647sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl7_3.this.toString() + \".Role647sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl7_6.java",
			    "\n" +
			    "public class T647sl7_6 extends T647sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl7_2.java",
			    "\n" +
			    "public abstract class T647sl7_2 extends T647sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl7_3.java",
			    "\n" +
			    "public class T647sl7_3 extends T647sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl7_1.java",
			    "\n" +
			    "public team class Team647sl7_1 {\n" +
			    "    public class Role647sl7_1 extends T647sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl7_1.this.toString() + \".Role647sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl7_2 extends Role647sl7_1 playedBy T647sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl7_1.this.toString() + \".Role647sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl7_3 extends Role647sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl7_1.this.toString() + \".Role647sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl7_1 as Role647sl7_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl7_4.java",
			    "\n" +
			    "public class T647sl7_4 extends T647sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl7_2.java",
			    "\n" +
			    "public team class Team647sl7_2 extends Team647sl7_1 {\n" +
			    "    public class Role647sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl7_2.this.toString() + \".Role647sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl7_4 extends Role647sl7_3 playedBy T647sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl7_2.this.toString() + \".Role647sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl7_5.java",
			    "\n" +
			    "public class T647sl7_5 extends T647sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl7_1.java",
			    "\n" +
			    "public class T647sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl7_3.Role647sl7_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-8
    public void test647_smartLifting8() {

       runConformTest(
            new String[] {
		"T647sl8Main.java",
			    "\n" +
			    "public class T647sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl8_1 t = new Team647sl8_4();\n" +
			    "        T647sl8_1    o = new T647sl8_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl8_4.java",
			    "\n" +
			    "public team class Team647sl8_4 extends Team647sl8_3 {\n" +
			    "    public class Role647sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl8_4.this.toString() + \".Role647sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl8_2.java",
			    "\n" +
			    "public team class Team647sl8_2 extends Team647sl8_1 {\n" +
			    "    public class Role647sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl8_2.this.toString() + \".Role647sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl8_4 extends Role647sl8_3 playedBy T647sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl8_2.this.toString() + \".Role647sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl8_5.java",
			    "\n" +
			    "public class T647sl8_5 extends T647sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl8_1.java",
			    "\n" +
			    "public class T647sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl8_3.java",
			    "\n" +
			    "public team class Team647sl8_3 extends Team647sl8_2 {\n" +
			    "    public class Role647sl8_5 extends Role647sl8_3 playedBy T647sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl8_3.this.toString() + \".Role647sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl8_6.java",
			    "\n" +
			    "public class T647sl8_6 extends T647sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl8_2.java",
			    "\n" +
			    "public abstract class T647sl8_2 extends T647sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl8_3.java",
			    "\n" +
			    "public class T647sl8_3 extends T647sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl8_1.java",
			    "\n" +
			    "public team class Team647sl8_1 {\n" +
			    "    public class Role647sl8_1 extends T647sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl8_1.this.toString() + \".Role647sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl8_2 extends Role647sl8_1 playedBy T647sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl8_1.this.toString() + \".Role647sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl8_3 extends Role647sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl8_1.this.toString() + \".Role647sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl8_1 as Role647sl8_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl8_4.java",
			    "\n" +
			    "public class T647sl8_4 extends T647sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl8_4.Role647sl8_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-9
    public void test647_smartLifting9() {

       runConformTest(
            new String[] {
		"T647sl9Main.java",
			    "\n" +
			    "public class T647sl9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl9_1 t = new Team647sl9_1();\n" +
			    "        T647sl9_1    o = new T647sl9_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl9_4.java",
			    "\n" +
			    "public team class Team647sl9_4 extends Team647sl9_3 {\n" +
			    "    public class Role647sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl9_4.this.toString() + \".Role647sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl9_1.java",
			    "\n" +
			    "public team class Team647sl9_1 {\n" +
			    "    public class Role647sl9_1 extends T647sl9_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl9_1.this.toString() + \".Role647sl9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl9_2 extends Role647sl9_1 playedBy T647sl9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl9_1.this.toString() + \".Role647sl9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl9_3 extends Role647sl9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl9_1.this.toString() + \".Role647sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl9_1 as Role647sl9_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl9_4.java",
			    "\n" +
			    "public class T647sl9_4 extends T647sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl9_2.java",
			    "\n" +
			    "public team class Team647sl9_2 extends Team647sl9_1 {\n" +
			    "    public class Role647sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl9_2.this.toString() + \".Role647sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl9_4 extends Role647sl9_3 playedBy T647sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl9_2.this.toString() + \".Role647sl9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl9_5.java",
			    "\n" +
			    "public class T647sl9_5 extends T647sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl9_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl9_1.java",
			    "\n" +
			    "public class T647sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl9_3.java",
			    "\n" +
			    "public team class Team647sl9_3 extends Team647sl9_2 {\n" +
			    "    public class Role647sl9_5 extends Role647sl9_3 playedBy T647sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl9_3.this.toString() + \".Role647sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl9_6.java",
			    "\n" +
			    "public class T647sl9_6 extends T647sl9_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl9_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl9_2.java",
			    "\n" +
			    "public abstract class T647sl9_2 extends T647sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl9_3.java",
			    "\n" +
			    "public class T647sl9_3 extends T647sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl9_1.Role647sl9_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-10
    public void test647_smartLifting10() {

       runConformTest(
            new String[] {
		"T647sl10Main.java",
			    "\n" +
			    "public class T647sl10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl10_1 t = new Team647sl10_2();\n" +
			    "        T647sl10_1    o = new T647sl10_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl10_4.java",
			    "\n" +
			    "public team class Team647sl10_4 extends Team647sl10_3 {\n" +
			    "    public class Role647sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl10_4.this.toString() + \".Role647sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl10_1.java",
			    "\n" +
			    "public team class Team647sl10_1 {\n" +
			    "    public class Role647sl10_1 extends T647sl10_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl10_1.this.toString() + \".Role647sl10_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl10_2 extends Role647sl10_1 playedBy T647sl10_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl10_1.this.toString() + \".Role647sl10_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl10_3 extends Role647sl10_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl10_1.this.toString() + \".Role647sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl10_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl10_1 as Role647sl10_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl10_4.java",
			    "\n" +
			    "public class T647sl10_4 extends T647sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl10_2.java",
			    "\n" +
			    "public team class Team647sl10_2 extends Team647sl10_1 {\n" +
			    "    public class Role647sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl10_2.this.toString() + \".Role647sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl10_4 extends Role647sl10_3 playedBy T647sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl10_2.this.toString() + \".Role647sl10_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl10_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl10_5.java",
			    "\n" +
			    "public class T647sl10_5 extends T647sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl10_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl10_1.java",
			    "\n" +
			    "public class T647sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl10_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl10_3.java",
			    "\n" +
			    "public team class Team647sl10_3 extends Team647sl10_2 {\n" +
			    "    public class Role647sl10_5 extends Role647sl10_3 playedBy T647sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl10_3.this.toString() + \".Role647sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl10_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl10_6.java",
			    "\n" +
			    "public class T647sl10_6 extends T647sl10_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl10_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl10_2.java",
			    "\n" +
			    "public abstract class T647sl10_2 extends T647sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl10_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl10_3.java",
			    "\n" +
			    "public class T647sl10_3 extends T647sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl10_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl10_2.Role647sl10_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-11
    public void test647_smartLifting11() {

       runConformTest(
            new String[] {
		"T647sl11Main.java",
			    "\n" +
			    "public class T647sl11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl11_1 t = new Team647sl11_3();\n" +
			    "        T647sl11_1    o = new T647sl11_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl11_4.java",
			    "\n" +
			    "public team class Team647sl11_4 extends Team647sl11_3 {\n" +
			    "    public class Role647sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl11_4.this.toString() + \".Role647sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl11_3.java",
			    "\n" +
			    "public class T647sl11_3 extends T647sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl11_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl11_1.java",
			    "\n" +
			    "public team class Team647sl11_1 {\n" +
			    "    public class Role647sl11_1 extends T647sl11_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl11_1.this.toString() + \".Role647sl11_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl11_2 extends Role647sl11_1 playedBy T647sl11_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl11_1.this.toString() + \".Role647sl11_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl11_3 extends Role647sl11_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl11_1.this.toString() + \".Role647sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl11_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl11_1 as Role647sl11_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl11_4.java",
			    "\n" +
			    "public class T647sl11_4 extends T647sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl11_2.java",
			    "\n" +
			    "public team class Team647sl11_2 extends Team647sl11_1 {\n" +
			    "    public class Role647sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl11_2.this.toString() + \".Role647sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl11_4 extends Role647sl11_3 playedBy T647sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl11_2.this.toString() + \".Role647sl11_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl11_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl11_5.java",
			    "\n" +
			    "public class T647sl11_5 extends T647sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl11_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl11_1.java",
			    "\n" +
			    "public class T647sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl11_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl11_3.java",
			    "\n" +
			    "public team class Team647sl11_3 extends Team647sl11_2 {\n" +
			    "    public class Role647sl11_5 extends Role647sl11_3 playedBy T647sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl11_3.this.toString() + \".Role647sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl11_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl11_6.java",
			    "\n" +
			    "public class T647sl11_6 extends T647sl11_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl11_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl11_2.java",
			    "\n" +
			    "public abstract class T647sl11_2 extends T647sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl11_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl11_3.Role647sl11_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-12
    public void test647_smartLifting12() {

       runConformTest(
            new String[] {
		"T647sl12Main.java",
			    "\n" +
			    "public class T647sl12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl12_1 t = new Team647sl12_4();\n" +
			    "        T647sl12_1    o = new T647sl12_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl12_4.java",
			    "\n" +
			    "public team class Team647sl12_4 extends Team647sl12_3 {\n" +
			    "    public class Role647sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl12_4.this.toString() + \".Role647sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl12_3.java",
			    "\n" +
			    "public team class Team647sl12_3 extends Team647sl12_2 {\n" +
			    "    public class Role647sl12_5 extends Role647sl12_3 playedBy T647sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl12_3.this.toString() + \".Role647sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl12_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl12_6.java",
			    "\n" +
			    "public class T647sl12_6 extends T647sl12_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl12_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl12_2.java",
			    "\n" +
			    "public abstract class T647sl12_2 extends T647sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl12_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl12_3.java",
			    "\n" +
			    "public class T647sl12_3 extends T647sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl12_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl12_1.java",
			    "\n" +
			    "public team class Team647sl12_1 {\n" +
			    "    public class Role647sl12_1 extends T647sl12_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl12_1.this.toString() + \".Role647sl12_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl12_2 extends Role647sl12_1 playedBy T647sl12_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl12_1.this.toString() + \".Role647sl12_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl12_3 extends Role647sl12_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl12_1.this.toString() + \".Role647sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl12_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl12_1 as Role647sl12_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl12_4.java",
			    "\n" +
			    "public class T647sl12_4 extends T647sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl12_2.java",
			    "\n" +
			    "public team class Team647sl12_2 extends Team647sl12_1 {\n" +
			    "    public class Role647sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl12_2.this.toString() + \".Role647sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl12_4 extends Role647sl12_3 playedBy T647sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl12_2.this.toString() + \".Role647sl12_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl12_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl12_5.java",
			    "\n" +
			    "public class T647sl12_5 extends T647sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl12_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl12_1.java",
			    "\n" +
			    "public class T647sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl12_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl12_4.Role647sl12_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-13
    public void test647_smartLifting13() {

       runConformTest(
            new String[] {
		"T647sl13Main.java",
			    "\n" +
			    "public class T647sl13Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl13_1 t = new Team647sl13_1();\n" +
			    "        T647sl13_1    o = new T647sl13_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl13_4.java",
			    "\n" +
			    "public team class Team647sl13_4 extends Team647sl13_3 {\n" +
			    "    public class Role647sl13_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl13_4.this.toString() + \".Role647sl13_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl13_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl13_2.java",
			    "\n" +
			    "public team class Team647sl13_2 extends Team647sl13_1 {\n" +
			    "    public class Role647sl13_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl13_2.this.toString() + \".Role647sl13_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl13_4 extends Role647sl13_3 playedBy T647sl13_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl13_2.this.toString() + \".Role647sl13_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl13_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl13_5.java",
			    "\n" +
			    "public class T647sl13_5 extends T647sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl13_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl13_1.java",
			    "\n" +
			    "public class T647sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl13_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl13_3.java",
			    "\n" +
			    "public team class Team647sl13_3 extends Team647sl13_2 {\n" +
			    "    public class Role647sl13_5 extends Role647sl13_3 playedBy T647sl13_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl13_3.this.toString() + \".Role647sl13_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl13_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl13_6.java",
			    "\n" +
			    "public class T647sl13_6 extends T647sl13_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl13_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl13_2.java",
			    "\n" +
			    "public abstract class T647sl13_2 extends T647sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl13_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl13_3.java",
			    "\n" +
			    "public class T647sl13_3 extends T647sl13_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl13_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl13_1.java",
			    "\n" +
			    "public team class Team647sl13_1 {\n" +
			    "    public class Role647sl13_1 extends T647sl13_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl13_1.this.toString() + \".Role647sl13_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl13_2 extends Role647sl13_1 playedBy T647sl13_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl13_1.this.toString() + \".Role647sl13_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl13_3 extends Role647sl13_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl13_1.this.toString() + \".Role647sl13_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl13_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl13_1 as Role647sl13_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl13_4.java",
			    "\n" +
			    "public class T647sl13_4 extends T647sl13_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl13_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl13_1.Role647sl13_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-14
    public void test647_smartLifting14() {

       runConformTest(
            new String[] {
		"T647sl14Main.java",
			    "\n" +
			    "public class T647sl14Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl14_1 t = new Team647sl14_2();\n" +
			    "        T647sl14_1    o = new T647sl14_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl14_4.java",
			    "\n" +
			    "public team class Team647sl14_4 extends Team647sl14_3 {\n" +
			    "    public class Role647sl14_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl14_4.this.toString() + \".Role647sl14_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl14_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl14_1.java",
			    "\n" +
			    "public team class Team647sl14_1 {\n" +
			    "    public class Role647sl14_1 extends T647sl14_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl14_1.this.toString() + \".Role647sl14_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl14_2 extends Role647sl14_1 playedBy T647sl14_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl14_1.this.toString() + \".Role647sl14_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl14_3 extends Role647sl14_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl14_1.this.toString() + \".Role647sl14_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl14_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl14_1 as Role647sl14_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl14_4.java",
			    "\n" +
			    "public class T647sl14_4 extends T647sl14_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl14_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl14_2.java",
			    "\n" +
			    "public team class Team647sl14_2 extends Team647sl14_1 {\n" +
			    "    public class Role647sl14_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl14_2.this.toString() + \".Role647sl14_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl14_4 extends Role647sl14_3 playedBy T647sl14_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl14_2.this.toString() + \".Role647sl14_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl14_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl14_5.java",
			    "\n" +
			    "public class T647sl14_5 extends T647sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl14_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl14_1.java",
			    "\n" +
			    "public class T647sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl14_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl14_3.java",
			    "\n" +
			    "public team class Team647sl14_3 extends Team647sl14_2 {\n" +
			    "    public class Role647sl14_5 extends Role647sl14_3 playedBy T647sl14_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl14_3.this.toString() + \".Role647sl14_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl14_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl14_6.java",
			    "\n" +
			    "public class T647sl14_6 extends T647sl14_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl14_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl14_2.java",
			    "\n" +
			    "public abstract class T647sl14_2 extends T647sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl14_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl14_3.java",
			    "\n" +
			    "public class T647sl14_3 extends T647sl14_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl14_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl14_2.Role647sl14_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-15
    public void test647_smartLifting15() {

       runConformTest(
            new String[] {
		"T647sl15Main.java",
			    "\n" +
			    "public class T647sl15Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl15_1 t = new Team647sl15_3();\n" +
			    "        T647sl15_1    o = new T647sl15_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl15_4.java",
			    "\n" +
			    "public team class Team647sl15_4 extends Team647sl15_3 {\n" +
			    "    public class Role647sl15_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl15_4.this.toString() + \".Role647sl15_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl15_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl15_3.java",
			    "\n" +
			    "public class T647sl15_3 extends T647sl15_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl15_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl15_1.java",
			    "\n" +
			    "public team class Team647sl15_1 {\n" +
			    "    public class Role647sl15_1 extends T647sl15_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl15_1.this.toString() + \".Role647sl15_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl15_2 extends Role647sl15_1 playedBy T647sl15_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl15_1.this.toString() + \".Role647sl15_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl15_3 extends Role647sl15_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl15_1.this.toString() + \".Role647sl15_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl15_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl15_1 as Role647sl15_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl15_4.java",
			    "\n" +
			    "public class T647sl15_4 extends T647sl15_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl15_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl15_2.java",
			    "\n" +
			    "public team class Team647sl15_2 extends Team647sl15_1 {\n" +
			    "    public class Role647sl15_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl15_2.this.toString() + \".Role647sl15_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl15_4 extends Role647sl15_3 playedBy T647sl15_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl15_2.this.toString() + \".Role647sl15_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl15_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl15_5.java",
			    "\n" +
			    "public class T647sl15_5 extends T647sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl15_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl15_1.java",
			    "\n" +
			    "public class T647sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl15_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl15_3.java",
			    "\n" +
			    "public team class Team647sl15_3 extends Team647sl15_2 {\n" +
			    "    public class Role647sl15_5 extends Role647sl15_3 playedBy T647sl15_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl15_3.this.toString() + \".Role647sl15_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl15_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl15_6.java",
			    "\n" +
			    "public class T647sl15_6 extends T647sl15_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl15_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl15_2.java",
			    "\n" +
			    "public abstract class T647sl15_2 extends T647sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl15_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl15_3.Role647sl15_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-16
    public void test647_smartLifting16() {

       runConformTest(
            new String[] {
		"T647sl16Main.java",
			    "\n" +
			    "public class T647sl16Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl16_1 t = new Team647sl16_4();\n" +
			    "        T647sl16_1    o = new T647sl16_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl16_4.java",
			    "\n" +
			    "public team class Team647sl16_4 extends Team647sl16_3 {\n" +
			    "    public class Role647sl16_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl16_4.this.toString() + \".Role647sl16_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl16_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl16_3.java",
			    "\n" +
			    "public team class Team647sl16_3 extends Team647sl16_2 {\n" +
			    "    public class Role647sl16_5 extends Role647sl16_3 playedBy T647sl16_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl16_3.this.toString() + \".Role647sl16_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl16_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl16_6.java",
			    "\n" +
			    "public class T647sl16_6 extends T647sl16_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl16_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl16_2.java",
			    "\n" +
			    "public abstract class T647sl16_2 extends T647sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl16_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl16_3.java",
			    "\n" +
			    "public class T647sl16_3 extends T647sl16_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl16_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl16_1.java",
			    "\n" +
			    "public team class Team647sl16_1 {\n" +
			    "    public class Role647sl16_1 extends T647sl16_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl16_1.this.toString() + \".Role647sl16_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl16_2 extends Role647sl16_1 playedBy T647sl16_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl16_1.this.toString() + \".Role647sl16_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl16_3 extends Role647sl16_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl16_1.this.toString() + \".Role647sl16_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl16_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl16_1 as Role647sl16_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl16_4.java",
			    "\n" +
			    "public class T647sl16_4 extends T647sl16_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl16_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl16_2.java",
			    "\n" +
			    "public team class Team647sl16_2 extends Team647sl16_1 {\n" +
			    "    public class Role647sl16_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl16_2.this.toString() + \".Role647sl16_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl16_4 extends Role647sl16_3 playedBy T647sl16_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl16_2.this.toString() + \".Role647sl16_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl16_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl16_5.java",
			    "\n" +
			    "public class T647sl16_5 extends T647sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl16_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl16_1.java",
			    "\n" +
			    "public class T647sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl16_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl16_4.Role647sl16_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-17
    public void test647_smartLifting17() {

       runConformTest(
            new String[] {
		"T647sl17Main.java",
			    "\n" +
			    "public class T647sl17Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl17_1 t = new Team647sl17_1();\n" +
			    "        T647sl17_1    o = new T647sl17_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl17_4.java",
			    "\n" +
			    "public team class Team647sl17_4 extends Team647sl17_3 {\n" +
			    "    public class Role647sl17_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl17_4.this.toString() + \".Role647sl17_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl17_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl17_2.java",
			    "\n" +
			    "public team class Team647sl17_2 extends Team647sl17_1 {\n" +
			    "    public class Role647sl17_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl17_2.this.toString() + \".Role647sl17_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl17_4 extends Role647sl17_3 playedBy T647sl17_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl17_2.this.toString() + \".Role647sl17_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl17_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl17_5.java",
			    "\n" +
			    "public class T647sl17_5 extends T647sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl17_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl17_1.java",
			    "\n" +
			    "public class T647sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl17_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl17_3.java",
			    "\n" +
			    "public team class Team647sl17_3 extends Team647sl17_2 {\n" +
			    "    public class Role647sl17_5 extends Role647sl17_3 playedBy T647sl17_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl17_3.this.toString() + \".Role647sl17_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl17_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl17_6.java",
			    "\n" +
			    "public class T647sl17_6 extends T647sl17_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl17_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl17_2.java",
			    "\n" +
			    "public abstract class T647sl17_2 extends T647sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl17_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl17_3.java",
			    "\n" +
			    "public class T647sl17_3 extends T647sl17_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl17_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl17_1.java",
			    "\n" +
			    "public team class Team647sl17_1 {\n" +
			    "    public class Role647sl17_1 extends T647sl17_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl17_1.this.toString() + \".Role647sl17_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl17_2 extends Role647sl17_1 playedBy T647sl17_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl17_1.this.toString() + \".Role647sl17_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl17_3 extends Role647sl17_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl17_1.this.toString() + \".Role647sl17_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl17_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl17_1 as Role647sl17_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl17_4.java",
			    "\n" +
			    "public class T647sl17_4 extends T647sl17_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl17_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl17_1.Role647sl17_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-18
    public void test647_smartLifting18() {

       runConformTest(
            new String[] {
		"T647sl18Main.java",
			    "\n" +
			    "public class T647sl18Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl18_1 t = new Team647sl18_2();\n" +
			    "        T647sl18_1    o = new T647sl18_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl18_4.java",
			    "\n" +
			    "public team class Team647sl18_4 extends Team647sl18_3 {\n" +
			    "    public class Role647sl18_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl18_4.this.toString() + \".Role647sl18_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl18_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl18_1.java",
			    "\n" +
			    "public team class Team647sl18_1 {\n" +
			    "    public class Role647sl18_1 extends T647sl18_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl18_1.this.toString() + \".Role647sl18_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl18_2 extends Role647sl18_1 playedBy T647sl18_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl18_1.this.toString() + \".Role647sl18_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl18_3 extends Role647sl18_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl18_1.this.toString() + \".Role647sl18_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl18_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl18_1 as Role647sl18_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl18_4.java",
			    "\n" +
			    "public class T647sl18_4 extends T647sl18_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl18_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl18_2.java",
			    "\n" +
			    "public team class Team647sl18_2 extends Team647sl18_1 {\n" +
			    "    public class Role647sl18_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl18_2.this.toString() + \".Role647sl18_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl18_4 extends Role647sl18_3 playedBy T647sl18_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl18_2.this.toString() + \".Role647sl18_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl18_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl18_5.java",
			    "\n" +
			    "public class T647sl18_5 extends T647sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl18_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl18_1.java",
			    "\n" +
			    "public class T647sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl18_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl18_3.java",
			    "\n" +
			    "public team class Team647sl18_3 extends Team647sl18_2 {\n" +
			    "    public class Role647sl18_5 extends Role647sl18_3 playedBy T647sl18_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl18_3.this.toString() + \".Role647sl18_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl18_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl18_6.java",
			    "\n" +
			    "public class T647sl18_6 extends T647sl18_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl18_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl18_2.java",
			    "\n" +
			    "public abstract class T647sl18_2 extends T647sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl18_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl18_3.java",
			    "\n" +
			    "public class T647sl18_3 extends T647sl18_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl18_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl18_2.Role647sl18_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-19
    public void test647_smartLifting19() {

       runConformTest(
            new String[] {
		"T647sl19Main.java",
			    "\n" +
			    "public class T647sl19Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl19_1 t = new Team647sl19_3();\n" +
			    "        T647sl19_1    o = new T647sl19_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl19_4.java",
			    "\n" +
			    "public team class Team647sl19_4 extends Team647sl19_3 {\n" +
			    "    public class Role647sl19_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl19_4.this.toString() + \".Role647sl19_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl19_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl19_3.java",
			    "\n" +
			    "public class T647sl19_3 extends T647sl19_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl19_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl19_1.java",
			    "\n" +
			    "public team class Team647sl19_1 {\n" +
			    "    public class Role647sl19_1 extends T647sl19_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl19_1.this.toString() + \".Role647sl19_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl19_2 extends Role647sl19_1 playedBy T647sl19_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl19_1.this.toString() + \".Role647sl19_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl19_3 extends Role647sl19_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl19_1.this.toString() + \".Role647sl19_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl19_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl19_1 as Role647sl19_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl19_4.java",
			    "\n" +
			    "public class T647sl19_4 extends T647sl19_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl19_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl19_2.java",
			    "\n" +
			    "public team class Team647sl19_2 extends Team647sl19_1 {\n" +
			    "    public class Role647sl19_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl19_2.this.toString() + \".Role647sl19_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl19_4 extends Role647sl19_3 playedBy T647sl19_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl19_2.this.toString() + \".Role647sl19_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl19_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl19_5.java",
			    "\n" +
			    "public class T647sl19_5 extends T647sl19_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl19_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl19_1.java",
			    "\n" +
			    "public class T647sl19_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl19_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl19_3.java",
			    "\n" +
			    "public team class Team647sl19_3 extends Team647sl19_2 {\n" +
			    "    public class Role647sl19_5 extends Role647sl19_3 playedBy T647sl19_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl19_3.this.toString() + \".Role647sl19_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl19_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl19_6.java",
			    "\n" +
			    "public class T647sl19_6 extends T647sl19_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl19_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl19_2.java",
			    "\n" +
			    "public abstract class T647sl19_2 extends T647sl19_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl19_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl19_3.Role647sl19_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-20
    public void test647_smartLifting20() {

       runConformTest(
            new String[] {
		"T647sl20Main.java",
			    "\n" +
			    "public class T647sl20Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl20_1 t = new Team647sl20_4();\n" +
			    "        T647sl20_1    o = new T647sl20_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl20_4.java",
			    "\n" +
			    "public team class Team647sl20_4 extends Team647sl20_3 {\n" +
			    "    public class Role647sl20_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl20_4.this.toString() + \".Role647sl20_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl20_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl20_3.java",
			    "\n" +
			    "public class T647sl20_3 extends T647sl20_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl20_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl20_1.java",
			    "\n" +
			    "public team class Team647sl20_1 {\n" +
			    "    public class Role647sl20_1 extends T647sl20_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl20_1.this.toString() + \".Role647sl20_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl20_2 extends Role647sl20_1 playedBy T647sl20_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl20_1.this.toString() + \".Role647sl20_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl20_3 extends Role647sl20_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl20_1.this.toString() + \".Role647sl20_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl20_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl20_1 as Role647sl20_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl20_4.java",
			    "\n" +
			    "public class T647sl20_4 extends T647sl20_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl20_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl20_2.java",
			    "\n" +
			    "public team class Team647sl20_2 extends Team647sl20_1 {\n" +
			    "    public class Role647sl20_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl20_2.this.toString() + \".Role647sl20_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl20_4 extends Role647sl20_3 playedBy T647sl20_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl20_2.this.toString() + \".Role647sl20_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl20_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl20_5.java",
			    "\n" +
			    "public class T647sl20_5 extends T647sl20_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl20_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl20_1.java",
			    "\n" +
			    "public class T647sl20_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl20_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl20_3.java",
			    "\n" +
			    "public team class Team647sl20_3 extends Team647sl20_2 {\n" +
			    "    public class Role647sl20_5 extends Role647sl20_3 playedBy T647sl20_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl20_3.this.toString() + \".Role647sl20_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl20_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl20_6.java",
			    "\n" +
			    "public class T647sl20_6 extends T647sl20_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl20_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl20_2.java",
			    "\n" +
			    "public abstract class T647sl20_2 extends T647sl20_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl20_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl20_4.Role647sl20_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-21
    public void test647_smartLifting21() {

       runConformTest(
            new String[] {
		"T647sl21Main.java",
			    "\n" +
			    "public class T647sl21Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl21_1       t  = new Team647sl21_1();\n" +
			    "        final Team647sl21_1 ft = new Team647sl21_1();\n" +
			    "        T647sl21_1          o  = ft.new Role647sl21_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl21_4.java",
			    "\n" +
			    "public team class Team647sl21_4 extends Team647sl21_3 {\n" +
			    "    public class Role647sl21_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl21_4.this.toString() + \".Role647sl21_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl21_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl21_3.java",
			    "\n" +
			    "public team class Team647sl21_3 extends Team647sl21_2 {\n" +
			    "    public class Role647sl21_5 extends Role647sl21_3 playedBy T647sl21_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl21_3.this.toString() + \".Role647sl21_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl21_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl21_6.java",
			    "\n" +
			    "public class T647sl21_6 extends T647sl21_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl21_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl21_2.java",
			    "\n" +
			    "public abstract class T647sl21_2 extends T647sl21_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl21_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl21_3.java",
			    "\n" +
			    "public class T647sl21_3 extends T647sl21_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl21_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl21_1.java",
			    "\n" +
			    "public team class Team647sl21_1 {\n" +
			    "    public class Role647sl21_1 extends T647sl21_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl21_1.this.toString() + \".Role647sl21_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl21_2 extends Role647sl21_1 playedBy T647sl21_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl21_1.this.toString() + \".Role647sl21_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl21_3 extends Role647sl21_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl21_1.this.toString() + \".Role647sl21_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl21_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl21_1 as Role647sl21_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl21_4.java",
			    "\n" +
			    "public class T647sl21_4 extends T647sl21_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl21_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl21_2.java",
			    "\n" +
			    "public team class Team647sl21_2 extends Team647sl21_1 {\n" +
			    "    public class Role647sl21_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl21_2.this.toString() + \".Role647sl21_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl21_4 extends Role647sl21_3 playedBy T647sl21_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl21_2.this.toString() + \".Role647sl21_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl21_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl21_5.java",
			    "\n" +
			    "public class T647sl21_5 extends T647sl21_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl21_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl21_1.java",
			    "\n" +
			    "public class T647sl21_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl21_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl21_1.Role647sl21_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-22
    public void test647_smartLifting22() {

       runConformTest(
            new String[] {
		"T647sl22Main.java",
			    "\n" +
			    "public class T647sl22Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl22_1       t  = new Team647sl22_2();\n" +
			    "        final Team647sl22_1 ft = new Team647sl22_1();\n" +
			    "        T647sl22_1          o  = ft.new Role647sl22_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl22_4.java",
			    "\n" +
			    "public team class Team647sl22_4 extends Team647sl22_3 {\n" +
			    "    public class Role647sl22_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl22_4.this.toString() + \".Role647sl22_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl22_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl22_2.java",
			    "\n" +
			    "public team class Team647sl22_2 extends Team647sl22_1 {\n" +
			    "    public class Role647sl22_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl22_2.this.toString() + \".Role647sl22_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl22_4 extends Role647sl22_3 playedBy T647sl22_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl22_2.this.toString() + \".Role647sl22_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl22_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl22_5.java",
			    "\n" +
			    "public class T647sl22_5 extends T647sl22_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl22_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl22_1.java",
			    "\n" +
			    "public class T647sl22_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl22_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl22_3.java",
			    "\n" +
			    "public team class Team647sl22_3 extends Team647sl22_2 {\n" +
			    "    public class Role647sl22_5 extends Role647sl22_3 playedBy T647sl22_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl22_3.this.toString() + \".Role647sl22_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl22_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl22_6.java",
			    "\n" +
			    "public class T647sl22_6 extends T647sl22_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl22_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl22_2.java",
			    "\n" +
			    "public abstract class T647sl22_2 extends T647sl22_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl22_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl22_3.java",
			    "\n" +
			    "public class T647sl22_3 extends T647sl22_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl22_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl22_1.java",
			    "\n" +
			    "public team class Team647sl22_1 {\n" +
			    "    public class Role647sl22_1 extends T647sl22_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl22_1.this.toString() + \".Role647sl22_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl22_2 extends Role647sl22_1 playedBy T647sl22_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl22_1.this.toString() + \".Role647sl22_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl22_3 extends Role647sl22_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl22_1.this.toString() + \".Role647sl22_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl22_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl22_1 as Role647sl22_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl22_4.java",
			    "\n" +
			    "public class T647sl22_4 extends T647sl22_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl22_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl22_2.Role647sl22_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-23
    public void test647_smartLifting23() {

       runConformTest(
            new String[] {
		"T647sl23Main.java",
			    "\n" +
			    "public class T647sl23Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl23_1       t  = new Team647sl23_3();\n" +
			    "        final Team647sl23_1 ft = new Team647sl23_1();\n" +
			    "        T647sl23_1          o  = ft.new Role647sl23_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl23_4.java",
			    "\n" +
			    "public team class Team647sl23_4 extends Team647sl23_3 {\n" +
			    "    public class Role647sl23_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl23_4.this.toString() + \".Role647sl23_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl23_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl23_1.java",
			    "\n" +
			    "public team class Team647sl23_1 {\n" +
			    "    public class Role647sl23_1 extends T647sl23_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl23_1.this.toString() + \".Role647sl23_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl23_2 extends Role647sl23_1 playedBy T647sl23_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl23_1.this.toString() + \".Role647sl23_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl23_3 extends Role647sl23_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl23_1.this.toString() + \".Role647sl23_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl23_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl23_1 as Role647sl23_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl23_4.java",
			    "\n" +
			    "public class T647sl23_4 extends T647sl23_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl23_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl23_2.java",
			    "\n" +
			    "public team class Team647sl23_2 extends Team647sl23_1 {\n" +
			    "    public class Role647sl23_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl23_2.this.toString() + \".Role647sl23_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl23_4 extends Role647sl23_3 playedBy T647sl23_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl23_2.this.toString() + \".Role647sl23_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl23_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl23_5.java",
			    "\n" +
			    "public class T647sl23_5 extends T647sl23_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl23_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl23_1.java",
			    "\n" +
			    "public class T647sl23_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl23_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl23_3.java",
			    "\n" +
			    "public team class Team647sl23_3 extends Team647sl23_2 {\n" +
			    "    public class Role647sl23_5 extends Role647sl23_3 playedBy T647sl23_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl23_3.this.toString() + \".Role647sl23_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl23_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl23_6.java",
			    "\n" +
			    "public class T647sl23_6 extends T647sl23_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl23_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl23_2.java",
			    "\n" +
			    "public abstract class T647sl23_2 extends T647sl23_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl23_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl23_3.java",
			    "\n" +
			    "public class T647sl23_3 extends T647sl23_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl23_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl23_3.Role647sl23_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.7-otjld-smart-lifting-24
    public void test647_smartLifting24() {

       runConformTest(
            new String[] {
		"T647sl24Main.java",
			    "\n" +
			    "public class T647sl24Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team647sl24_1       t  = new Team647sl24_4();\n" +
			    "        final Team647sl24_1 ft = new Team647sl24_1();\n" +
			    "        T647sl24_1          o  = ft.new Role647sl24_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl24_4.java",
			    "\n" +
			    "public team class Team647sl24_4 extends Team647sl24_3 {\n" +
			    "    public class Role647sl24_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl24_4.this.toString() + \".Role647sl24_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl24_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl24_3.java",
			    "\n" +
			    "public class T647sl24_3 extends T647sl24_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl24_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl24_1.java",
			    "\n" +
			    "public team class Team647sl24_1 {\n" +
			    "    public class Role647sl24_1 extends T647sl24_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl24_1.this.toString() + \".Role647sl24_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role647sl24_2 extends Role647sl24_1 playedBy T647sl24_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl24_1.this.toString() + \".Role647sl24_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role647sl24_3 extends Role647sl24_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl24_1.this.toString() + \".Role647sl24_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl24_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T647sl24_1 as Role647sl24_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl24_4.java",
			    "\n" +
			    "public class T647sl24_4 extends T647sl24_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl24_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl24_2.java",
			    "\n" +
			    "public team class Team647sl24_2 extends Team647sl24_1 {\n" +
			    "    public class Role647sl24_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl24_2.this.toString() + \".Role647sl24_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role647sl24_4 extends Role647sl24_3 playedBy T647sl24_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl24_2.this.toString() + \".Role647sl24_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl24_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl24_5.java",
			    "\n" +
			    "public class T647sl24_5 extends T647sl24_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl24_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl24_1.java",
			    "\n" +
			    "public class T647sl24_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl24_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team647sl24_3.java",
			    "\n" +
			    "public team class Team647sl24_3 extends Team647sl24_2 {\n" +
			    "    public class Role647sl24_5 extends Role647sl24_3 playedBy T647sl24_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team647sl24_3.this.toString() + \".Role647sl24_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team647sl24_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T647sl24_6.java",
			    "\n" +
			    "public class T647sl24_6 extends T647sl24_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl24_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T647sl24_2.java",
			    "\n" +
			    "public abstract class T647sl24_2 extends T647sl24_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T647sl24_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team647sl24_4.Role647sl24_5");
    }
}
