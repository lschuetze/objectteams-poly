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

public class SmartLifting8 extends AbstractOTJLDTest {
	
	public SmartLifting8(String name) {
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
		return SmartLifting8.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.8-otjld-smart-lifting-1
    public void test648_smartLifting1() {
       
       runConformTest(
            new String[] {
		"T648sl1Main.java",
			    "\n" +
			    "public class T648sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team648sl1_1 t = new Team648sl1_1();\n" +
			    "        T648sl1_2    o = new T648sl1_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl1_4.java",
			    "\n" +
			    "public team class Team648sl1_4 extends Team648sl1_3 {\n" +
			    "    public class Role648sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl1_4.this.toString() + \".Role648sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl1_3.java",
			    "\n" +
			    "public class T648sl1_3 extends T648sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl1_1.java",
			    "\n" +
			    "public team class Team648sl1_1 {\n" +
			    "    public class Role648sl1_1 extends T648sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl1_1.this.toString() + \".Role648sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role648sl1_2 extends Role648sl1_1 playedBy T648sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl1_1.this.toString() + \".Role648sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role648sl1_3 extends Role648sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl1_1.this.toString() + \".Role648sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T648sl1_2 as Role648sl1_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl1_4.java",
			    "\n" +
			    "public class T648sl1_4 extends T648sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl1_2.java",
			    "\n" +
			    "public team class Team648sl1_2 extends Team648sl1_1 {\n" +
			    "    public class Role648sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl1_2.this.toString() + \".Role648sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role648sl1_4 extends Role648sl1_3 playedBy T648sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl1_2.this.toString() + \".Role648sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl1_5.java",
			    "\n" +
			    "public class T648sl1_5 extends T648sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl1_1.java",
			    "\n" +
			    "public class T648sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl1_3.java",
			    "\n" +
			    "public team class Team648sl1_3 extends Team648sl1_2 {\n" +
			    "    public class Role648sl1_5 extends Role648sl1_3 playedBy T648sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl1_3.this.toString() + \".Role648sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl1_6.java",
			    "\n" +
			    "public class T648sl1_6 extends T648sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl1_2.java",
			    "\n" +
			    "public abstract class T648sl1_2 extends T648sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team648sl1_1.Role648sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.8-otjld-smart-lifting-2
    public void test648_smartLifting2() {
       
       runConformTest(
            new String[] {
		"T648sl2Main.java",
			    "\n" +
			    "public class T648sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team648sl2_1 t = new Team648sl2_2();\n" +
			    "        T648sl2_2    o = new T648sl2_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl2_4.java",
			    "\n" +
			    "public team class Team648sl2_4 extends Team648sl2_3 {\n" +
			    "    public class Role648sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl2_4.this.toString() + \".Role648sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl2_3.java",
			    "\n" +
			    "public team class Team648sl2_3 extends Team648sl2_2 {\n" +
			    "    public class Role648sl2_5 extends Role648sl2_3 playedBy T648sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl2_3.this.toString() + \".Role648sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl2_6.java",
			    "\n" +
			    "public class T648sl2_6 extends T648sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl2_2.java",
			    "\n" +
			    "public abstract class T648sl2_2 extends T648sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl2_3.java",
			    "\n" +
			    "public class T648sl2_3 extends T648sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl2_1.java",
			    "\n" +
			    "public team class Team648sl2_1 {\n" +
			    "    public class Role648sl2_1 extends T648sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl2_1.this.toString() + \".Role648sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role648sl2_2 extends Role648sl2_1 playedBy T648sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl2_1.this.toString() + \".Role648sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role648sl2_3 extends Role648sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl2_1.this.toString() + \".Role648sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T648sl2_2 as Role648sl2_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl2_4.java",
			    "\n" +
			    "public class T648sl2_4 extends T648sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl2_2.java",
			    "\n" +
			    "public team class Team648sl2_2 extends Team648sl2_1 {\n" +
			    "    public class Role648sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl2_2.this.toString() + \".Role648sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role648sl2_4 extends Role648sl2_3 playedBy T648sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl2_2.this.toString() + \".Role648sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl2_5.java",
			    "\n" +
			    "public class T648sl2_5 extends T648sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl2_1.java",
			    "\n" +
			    "public class T648sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team648sl2_2.Role648sl2_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.8-otjld-smart-lifting-3
    public void test648_smartLifting3() {
       
       runConformTest(
            new String[] {
		"T648sl3Main.java",
			    "\n" +
			    "public class T648sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team648sl3_1 t = new Team648sl3_3();\n" +
			    "        T648sl3_2    o = new T648sl3_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl3_4.java",
			    "\n" +
			    "public team class Team648sl3_4 extends Team648sl3_3 {\n" +
			    "    public class Role648sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl3_4.this.toString() + \".Role648sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl3_2.java",
			    "\n" +
			    "public team class Team648sl3_2 extends Team648sl3_1 {\n" +
			    "    public class Role648sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl3_2.this.toString() + \".Role648sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role648sl3_4 extends Role648sl3_3 playedBy T648sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl3_2.this.toString() + \".Role648sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl3_5.java",
			    "\n" +
			    "public class T648sl3_5 extends T648sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl3_1.java",
			    "\n" +
			    "public class T648sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl3_3.java",
			    "\n" +
			    "public team class Team648sl3_3 extends Team648sl3_2 {\n" +
			    "    public class Role648sl3_5 extends Role648sl3_3 playedBy T648sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl3_3.this.toString() + \".Role648sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl3_6.java",
			    "\n" +
			    "public class T648sl3_6 extends T648sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl3_2.java",
			    "\n" +
			    "public abstract class T648sl3_2 extends T648sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl3_3.java",
			    "\n" +
			    "public class T648sl3_3 extends T648sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl3_1.java",
			    "\n" +
			    "public team class Team648sl3_1 {\n" +
			    "    public class Role648sl3_1 extends T648sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl3_1.this.toString() + \".Role648sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role648sl3_2 extends Role648sl3_1 playedBy T648sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl3_1.this.toString() + \".Role648sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role648sl3_3 extends Role648sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl3_1.this.toString() + \".Role648sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T648sl3_2 as Role648sl3_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl3_4.java",
			    "\n" +
			    "public class T648sl3_4 extends T648sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team648sl3_3.Role648sl3_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.8-otjld-smart-lifting-4
    public void test648_smartLifting4() {
       
       runConformTest(
            new String[] {
		"T648sl4Main.java",
			    "\n" +
			    "public class T648sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team648sl4_1 t = new Team648sl4_4();\n" +
			    "        T648sl4_2    o = new T648sl4_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl4_4.java",
			    "\n" +
			    "public team class Team648sl4_4 extends Team648sl4_3 {\n" +
			    "    public class Role648sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl4_4.this.toString() + \".Role648sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl4_1.java",
			    "\n" +
			    "public team class Team648sl4_1 {\n" +
			    "    public class Role648sl4_1 extends T648sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl4_1.this.toString() + \".Role648sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role648sl4_2 extends Role648sl4_1 playedBy T648sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl4_1.this.toString() + \".Role648sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role648sl4_3 extends Role648sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl4_1.this.toString() + \".Role648sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T648sl4_2 as Role648sl4_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl4_4.java",
			    "\n" +
			    "public class T648sl4_4 extends T648sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl4_2.java",
			    "\n" +
			    "public team class Team648sl4_2 extends Team648sl4_1 {\n" +
			    "    public class Role648sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl4_2.this.toString() + \".Role648sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role648sl4_4 extends Role648sl4_3 playedBy T648sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl4_2.this.toString() + \".Role648sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl4_5.java",
			    "\n" +
			    "public class T648sl4_5 extends T648sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl4_1.java",
			    "\n" +
			    "public class T648sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl4_3.java",
			    "\n" +
			    "public team class Team648sl4_3 extends Team648sl4_2 {\n" +
			    "    public class Role648sl4_5 extends Role648sl4_3 playedBy T648sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl4_3.this.toString() + \".Role648sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl4_6.java",
			    "\n" +
			    "public class T648sl4_6 extends T648sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl4_2.java",
			    "\n" +
			    "public abstract class T648sl4_2 extends T648sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl4_3.java",
			    "\n" +
			    "public class T648sl4_3 extends T648sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team648sl4_4.Role648sl4_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.8-otjld-smart-lifting-5
    public void test648_smartLifting5() {
       
       runConformTest(
            new String[] {
		"T648sl5Main.java",
			    "\n" +
			    "public class T648sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team648sl5_1 t = new Team648sl5_1();\n" +
			    "        T648sl5_2    o = new T648sl5_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl5_4.java",
			    "\n" +
			    "public team class Team648sl5_4 extends Team648sl5_3 {\n" +
			    "    public class Role648sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl5_4.this.toString() + \".Role648sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl5_3.java",
			    "\n" +
			    "public class T648sl5_3 extends T648sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl5_1.java",
			    "\n" +
			    "public team class Team648sl5_1 {\n" +
			    "    public class Role648sl5_1 extends T648sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl5_1.this.toString() + \".Role648sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role648sl5_2 extends Role648sl5_1 playedBy T648sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl5_1.this.toString() + \".Role648sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role648sl5_3 extends Role648sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl5_1.this.toString() + \".Role648sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T648sl5_2 as Role648sl5_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl5_4.java",
			    "\n" +
			    "public class T648sl5_4 extends T648sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl5_2.java",
			    "\n" +
			    "public team class Team648sl5_2 extends Team648sl5_1 {\n" +
			    "    public class Role648sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl5_2.this.toString() + \".Role648sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role648sl5_4 extends Role648sl5_3 playedBy T648sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl5_2.this.toString() + \".Role648sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl5_5.java",
			    "\n" +
			    "public class T648sl5_5 extends T648sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl5_1.java",
			    "\n" +
			    "public class T648sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl5_3.java",
			    "\n" +
			    "public team class Team648sl5_3 extends Team648sl5_2 {\n" +
			    "    public class Role648sl5_5 extends Role648sl5_3 playedBy T648sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl5_3.this.toString() + \".Role648sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl5_6.java",
			    "\n" +
			    "public class T648sl5_6 extends T648sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl5_2.java",
			    "\n" +
			    "public abstract class T648sl5_2 extends T648sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team648sl5_1.Role648sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.8-otjld-smart-lifting-6
    public void test648_smartLifting6() {
       
       runConformTest(
            new String[] {
		"T648sl6Main.java",
			    "\n" +
			    "public class T648sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team648sl6_1 t = new Team648sl6_2();\n" +
			    "        T648sl6_2    o = new T648sl6_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl6_4.java",
			    "\n" +
			    "public team class Team648sl6_4 extends Team648sl6_3 {\n" +
			    "    public class Role648sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl6_4.this.toString() + \".Role648sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl6_3.java",
			    "\n" +
			    "public team class Team648sl6_3 extends Team648sl6_2 {\n" +
			    "    public class Role648sl6_5 extends Role648sl6_3 playedBy T648sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl6_3.this.toString() + \".Role648sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl6_6.java",
			    "\n" +
			    "public class T648sl6_6 extends T648sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl6_2.java",
			    "\n" +
			    "public abstract class T648sl6_2 extends T648sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl6_3.java",
			    "\n" +
			    "public class T648sl6_3 extends T648sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl6_1.java",
			    "\n" +
			    "public team class Team648sl6_1 {\n" +
			    "    public class Role648sl6_1 extends T648sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl6_1.this.toString() + \".Role648sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role648sl6_2 extends Role648sl6_1 playedBy T648sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl6_1.this.toString() + \".Role648sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role648sl6_3 extends Role648sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl6_1.this.toString() + \".Role648sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T648sl6_2 as Role648sl6_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl6_4.java",
			    "\n" +
			    "public class T648sl6_4 extends T648sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl6_2.java",
			    "\n" +
			    "public team class Team648sl6_2 extends Team648sl6_1 {\n" +
			    "    public class Role648sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl6_2.this.toString() + \".Role648sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role648sl6_4 extends Role648sl6_3 playedBy T648sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl6_2.this.toString() + \".Role648sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl6_5.java",
			    "\n" +
			    "public class T648sl6_5 extends T648sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl6_1.java",
			    "\n" +
			    "public class T648sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team648sl6_2.Role648sl6_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.8-otjld-smart-lifting-7
    public void test648_smartLifting7() {
       
       runConformTest(
            new String[] {
		"T648sl7Main.java",
			    "\n" +
			    "public class T648sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team648sl7_1 t = new Team648sl7_3();\n" +
			    "        T648sl7_2    o = new T648sl7_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl7_4.java",
			    "\n" +
			    "public team class Team648sl7_4 extends Team648sl7_3 {\n" +
			    "    public class Role648sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl7_4.this.toString() + \".Role648sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl7_2.java",
			    "\n" +
			    "public team class Team648sl7_2 extends Team648sl7_1 {\n" +
			    "    public class Role648sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl7_2.this.toString() + \".Role648sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role648sl7_4 extends Role648sl7_3 playedBy T648sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl7_2.this.toString() + \".Role648sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl7_5.java",
			    "\n" +
			    "public class T648sl7_5 extends T648sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl7_1.java",
			    "\n" +
			    "public class T648sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl7_3.java",
			    "\n" +
			    "public team class Team648sl7_3 extends Team648sl7_2 {\n" +
			    "    public class Role648sl7_5 extends Role648sl7_3 playedBy T648sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl7_3.this.toString() + \".Role648sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl7_6.java",
			    "\n" +
			    "public class T648sl7_6 extends T648sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl7_2.java",
			    "\n" +
			    "public abstract class T648sl7_2 extends T648sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl7_3.java",
			    "\n" +
			    "public class T648sl7_3 extends T648sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl7_1.java",
			    "\n" +
			    "public team class Team648sl7_1 {\n" +
			    "    public class Role648sl7_1 extends T648sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl7_1.this.toString() + \".Role648sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role648sl7_2 extends Role648sl7_1 playedBy T648sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl7_1.this.toString() + \".Role648sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role648sl7_3 extends Role648sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl7_1.this.toString() + \".Role648sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T648sl7_2 as Role648sl7_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl7_4.java",
			    "\n" +
			    "public class T648sl7_4 extends T648sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team648sl7_3.Role648sl7_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.8-otjld-smart-lifting-8
    public void test648_smartLifting8() {
       
       runConformTest(
            new String[] {
		"T648sl8Main.java",
			    "\n" +
			    "public class T648sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team648sl8_1 t = new Team648sl8_4();\n" +
			    "        T648sl8_2    o = new T648sl8_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl8_4.java",
			    "\n" +
			    "public team class Team648sl8_4 extends Team648sl8_3 {\n" +
			    "    public class Role648sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl8_4.this.toString() + \".Role648sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl8_1.java",
			    "\n" +
			    "public team class Team648sl8_1 {\n" +
			    "    public class Role648sl8_1 extends T648sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl8_1.this.toString() + \".Role648sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role648sl8_2 extends Role648sl8_1 playedBy T648sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl8_1.this.toString() + \".Role648sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role648sl8_3 extends Role648sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl8_1.this.toString() + \".Role648sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T648sl8_2 as Role648sl8_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl8_4.java",
			    "\n" +
			    "public class T648sl8_4 extends T648sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl8_2.java",
			    "\n" +
			    "public team class Team648sl8_2 extends Team648sl8_1 {\n" +
			    "    public class Role648sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl8_2.this.toString() + \".Role648sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role648sl8_4 extends Role648sl8_3 playedBy T648sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl8_2.this.toString() + \".Role648sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl8_5.java",
			    "\n" +
			    "public class T648sl8_5 extends T648sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl8_1.java",
			    "\n" +
			    "public class T648sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team648sl8_3.java",
			    "\n" +
			    "public team class Team648sl8_3 extends Team648sl8_2 {\n" +
			    "    public class Role648sl8_5 extends Role648sl8_3 playedBy T648sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team648sl8_3.this.toString() + \".Role648sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team648sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T648sl8_6.java",
			    "\n" +
			    "public class T648sl8_6 extends T648sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl8_2.java",
			    "\n" +
			    "public abstract class T648sl8_2 extends T648sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T648sl8_3.java",
			    "\n" +
			    "public class T648sl8_3 extends T648sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T648sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team648sl8_4.Role648sl8_3");
    }
}
