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

public class SmartLifting25 extends AbstractOTJLDTest {
	
	public SmartLifting25(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test0c11_implicitlyInheritingStaticRoleMethod1"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return SmartLifting25.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-1
    public void test6425_smartLifting1() {
        runNegativeTest(
            new String[] {
		"Team6425sl1_4.java",
			    "\n" +
			    "public team class Team6425sl1_4 extends Team6425sl1_3 {\n" +
			    "    public class Role6425sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl1_4.this.toString() + \".Role6425sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl1Main.java",
			    "\n" +
			    "public class T6425sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl1_3 t = new Team6425sl1_3();\n" +
			    "        T6425sl1_1    o = new T6425sl1_1();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl1_1.java",
			    "\n" +
			    "public team class Team6425sl1_1 {\n" +
			    "    public class Role6425sl1_1 extends T6425sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl1_1.this.toString() + \".Role6425sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl1_2 extends Role6425sl1_1 playedBy T6425sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl1_1.this.toString() + \".Role6425sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl1_3 extends Role6425sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl1_1.this.toString() + \".Role6425sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl1_4.java",
			    "\n" +
			    "public class T6425sl1_4 extends T6425sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl1_2.java",
			    "\n" +
			    "public team class Team6425sl1_2 extends Team6425sl1_1 {\n" +
			    "    public class Role6425sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl1_2.this.toString() + \".Role6425sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl1_4 extends Role6425sl1_3 playedBy T6425sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl1_2.this.toString() + \".Role6425sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl1_5.java",
			    "\n" +
			    "public class T6425sl1_5 extends T6425sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl1_1.java",
			    "\n" +
			    "public class T6425sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl1_3.java",
			    "\n" +
			    "public team class Team6425sl1_3 extends Team6425sl1_2 {\n" +
			    "    public class Role6425sl1_5 extends Role6425sl1_3 playedBy T6425sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl1_3.this.toString() + \".Role6425sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl1_1 as Role6425sl1_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl1_6.java",
			    "\n" +
			    "public class T6425sl1_6 extends T6425sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl1_2.java",
			    "\n" +
			    "public abstract class T6425sl1_2 extends T6425sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl1_3.java",
			    "\n" +
			    "public class T6425sl1_3 extends T6425sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-2
    public void test6425_smartLifting2() {
        runNegativeTest(
            new String[] {
		"Team6425sl2_4.java",
			    "\n" +
			    "public team class Team6425sl2_4 extends Team6425sl2_3 {\n" +
			    "    public class Role6425sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl2_4.this.toString() + \".Role6425sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl2Main.java",
			    "\n" +
			    "public class T6425sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl2_3 t = new Team6425sl2_4();\n" +
			    "        T6425sl2_1    o = new T6425sl2_1();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl2_3.java",
			    "\n" +
			    "public class T6425sl2_3 extends T6425sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl2_1.java",
			    "\n" +
			    "public team class Team6425sl2_1 {\n" +
			    "    public class Role6425sl2_1 extends T6425sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl2_1.this.toString() + \".Role6425sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl2_2 extends Role6425sl2_1 playedBy T6425sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl2_1.this.toString() + \".Role6425sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl2_3 extends Role6425sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl2_1.this.toString() + \".Role6425sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl2_4.java",
			    "\n" +
			    "public class T6425sl2_4 extends T6425sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl2_2.java",
			    "\n" +
			    "public team class Team6425sl2_2 extends Team6425sl2_1 {\n" +
			    "    public class Role6425sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl2_2.this.toString() + \".Role6425sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl2_4 extends Role6425sl2_3 playedBy T6425sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl2_2.this.toString() + \".Role6425sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl2_5.java",
			    "\n" +
			    "public class T6425sl2_5 extends T6425sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl2_1.java",
			    "\n" +
			    "public class T6425sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl2_3.java",
			    "\n" +
			    "public team class Team6425sl2_3 extends Team6425sl2_2 {\n" +
			    "    public class Role6425sl2_5 extends Role6425sl2_3 playedBy T6425sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl2_3.this.toString() + \".Role6425sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl2_1 as Role6425sl2_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl2_6.java",
			    "\n" +
			    "public class T6425sl2_6 extends T6425sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl2_2.java",
			    "\n" +
			    "public abstract class T6425sl2_2 extends T6425sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-3
    public void test6425_smartLifting3() {
        runNegativeTest(
            new String[] {
		"Team6425sl3_4.java",
			    "\n" +
			    "public team class Team6425sl3_4 extends Team6425sl3_3 {\n" +
			    "    public class Role6425sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl3_4.this.toString() + \".Role6425sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl3Main.java",
			    "\n" +
			    "public class T6425sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl3_3 t = new Team6425sl3_3();\n" +
			    "        T6425sl3_1    o = new T6425sl3_3();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl3_3.java",
			    "\n" +
			    "public team class Team6425sl3_3 extends Team6425sl3_2 {\n" +
			    "    public class Role6425sl3_5 extends Role6425sl3_3 playedBy T6425sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl3_3.this.toString() + \".Role6425sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl3_1 as Role6425sl3_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl3_6.java",
			    "\n" +
			    "public class T6425sl3_6 extends T6425sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl3_2.java",
			    "\n" +
			    "public abstract class T6425sl3_2 extends T6425sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl3_3.java",
			    "\n" +
			    "public class T6425sl3_3 extends T6425sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl3_1.java",
			    "\n" +
			    "public team class Team6425sl3_1 {\n" +
			    "    public class Role6425sl3_1 extends T6425sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl3_1.this.toString() + \".Role6425sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl3_2 extends Role6425sl3_1 playedBy T6425sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl3_1.this.toString() + \".Role6425sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl3_3 extends Role6425sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl3_1.this.toString() + \".Role6425sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl3_4.java",
			    "\n" +
			    "public class T6425sl3_4 extends T6425sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl3_2.java",
			    "\n" +
			    "public team class Team6425sl3_2 extends Team6425sl3_1 {\n" +
			    "    public class Role6425sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl3_2.this.toString() + \".Role6425sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl3_4 extends Role6425sl3_3 playedBy T6425sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl3_2.this.toString() + \".Role6425sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl3_5.java",
			    "\n" +
			    "public class T6425sl3_5 extends T6425sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl3_1.java",
			    "\n" +
			    "public class T6425sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-4
    public void test6425_smartLifting4() {
        runNegativeTest(
            new String[] {
		"Team6425sl4_4.java",
			    "\n" +
			    "public team class Team6425sl4_4 extends Team6425sl4_3 {\n" +
			    "    public class Role6425sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl4_4.this.toString() + \".Role6425sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl4Main.java",
			    "\n" +
			    "public class T6425sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl4_3 t = new Team6425sl4_4();\n" +
			    "        T6425sl4_1    o = new T6425sl4_3();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl4_2.java",
			    "\n" +
			    "public team class Team6425sl4_2 extends Team6425sl4_1 {\n" +
			    "    public class Role6425sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl4_2.this.toString() + \".Role6425sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl4_4 extends Role6425sl4_3 playedBy T6425sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl4_2.this.toString() + \".Role6425sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl4_5.java",
			    "\n" +
			    "public class T6425sl4_5 extends T6425sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl4_1.java",
			    "\n" +
			    "public class T6425sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl4_3.java",
			    "\n" +
			    "public team class Team6425sl4_3 extends Team6425sl4_2 {\n" +
			    "    public class Role6425sl4_5 extends Role6425sl4_3 playedBy T6425sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl4_3.this.toString() + \".Role6425sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl4_1 as Role6425sl4_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl4_6.java",
			    "\n" +
			    "public class T6425sl4_6 extends T6425sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl4_2.java",
			    "\n" +
			    "public abstract class T6425sl4_2 extends T6425sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl4_3.java",
			    "\n" +
			    "public class T6425sl4_3 extends T6425sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl4_1.java",
			    "\n" +
			    "public team class Team6425sl4_1 {\n" +
			    "    public class Role6425sl4_1 extends T6425sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl4_1.this.toString() + \".Role6425sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl4_2 extends Role6425sl4_1 playedBy T6425sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl4_1.this.toString() + \".Role6425sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl4_3 extends Role6425sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl4_1.this.toString() + \".Role6425sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl4_4.java",
			    "\n" +
			    "public class T6425sl4_4 extends T6425sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-5
    public void test6425_smartLifting5() {
        runNegativeTest(
            new String[] {
		"Team6425sl5_4.java",
			    "\n" +
			    "public team class Team6425sl5_4 extends Team6425sl5_3 {\n" +
			    "    public class Role6425sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl5_4.this.toString() + \".Role6425sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl5Main.java",
			    "\n" +
			    "public class T6425sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl5_3 t = new Team6425sl5_3();\n" +
			    "        T6425sl5_1    o = new T6425sl5_4();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl5_1.java",
			    "\n" +
			    "public team class Team6425sl5_1 {\n" +
			    "    public class Role6425sl5_1 extends T6425sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl5_1.this.toString() + \".Role6425sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl5_2 extends Role6425sl5_1 playedBy T6425sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl5_1.this.toString() + \".Role6425sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl5_3 extends Role6425sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl5_1.this.toString() + \".Role6425sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl5_4.java",
			    "\n" +
			    "public class T6425sl5_4 extends T6425sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl5_2.java",
			    "\n" +
			    "public team class Team6425sl5_2 extends Team6425sl5_1 {\n" +
			    "    public class Role6425sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl5_2.this.toString() + \".Role6425sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl5_4 extends Role6425sl5_3 playedBy T6425sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl5_2.this.toString() + \".Role6425sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl5_5.java",
			    "\n" +
			    "public class T6425sl5_5 extends T6425sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl5_1.java",
			    "\n" +
			    "public class T6425sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl5_3.java",
			    "\n" +
			    "public team class Team6425sl5_3 extends Team6425sl5_2 {\n" +
			    "    public class Role6425sl5_5 extends Role6425sl5_3 playedBy T6425sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl5_3.this.toString() + \".Role6425sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl5_1 as Role6425sl5_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl5_6.java",
			    "\n" +
			    "public class T6425sl5_6 extends T6425sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl5_2.java",
			    "\n" +
			    "public abstract class T6425sl5_2 extends T6425sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl5_3.java",
			    "\n" +
			    "public class T6425sl5_3 extends T6425sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-6
    public void test6425_smartLifting6() {
        runNegativeTest(
            new String[] {
		"Team6425sl6_4.java",
			    "\n" +
			    "public team class Team6425sl6_4 extends Team6425sl6_3 {\n" +
			    "    public class Role6425sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl6_4.this.toString() + \".Role6425sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl6Main.java",
			    "\n" +
			    "public class T6425sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl6_3 t = new Team6425sl6_4();\n" +
			    "        T6425sl6_1    o = new T6425sl6_4();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl6_3.java",
			    "\n" +
			    "public class T6425sl6_3 extends T6425sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl6_1.java",
			    "\n" +
			    "public team class Team6425sl6_1 {\n" +
			    "    public class Role6425sl6_1 extends T6425sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl6_1.this.toString() + \".Role6425sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl6_2 extends Role6425sl6_1 playedBy T6425sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl6_1.this.toString() + \".Role6425sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl6_3 extends Role6425sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl6_1.this.toString() + \".Role6425sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl6_4.java",
			    "\n" +
			    "public class T6425sl6_4 extends T6425sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl6_2.java",
			    "\n" +
			    "public team class Team6425sl6_2 extends Team6425sl6_1 {\n" +
			    "    public class Role6425sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl6_2.this.toString() + \".Role6425sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl6_4 extends Role6425sl6_3 playedBy T6425sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl6_2.this.toString() + \".Role6425sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl6_5.java",
			    "\n" +
			    "public class T6425sl6_5 extends T6425sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl6_1.java",
			    "\n" +
			    "public class T6425sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl6_3.java",
			    "\n" +
			    "public team class Team6425sl6_3 extends Team6425sl6_2 {\n" +
			    "    public class Role6425sl6_5 extends Role6425sl6_3 playedBy T6425sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl6_3.this.toString() + \".Role6425sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl6_1 as Role6425sl6_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl6_6.java",
			    "\n" +
			    "public class T6425sl6_6 extends T6425sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl6_2.java",
			    "\n" +
			    "public abstract class T6425sl6_2 extends T6425sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-7
    public void test6425_smartLifting7() {
        runNegativeTest(
            new String[] {
		"Team6425sl7_4.java",
			    "\n" +
			    "public team class Team6425sl7_4 extends Team6425sl7_3 {\n" +
			    "    public class Role6425sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl7_4.this.toString() + \".Role6425sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl7Main.java",
			    "\n" +
			    "public class T6425sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl7_3 t = new Team6425sl7_3();\n" +
			    "        T6425sl7_1    o = new T6425sl7_5();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl7_3.java",
			    "\n" +
			    "public team class Team6425sl7_3 extends Team6425sl7_2 {\n" +
			    "    public class Role6425sl7_5 extends Role6425sl7_3 playedBy T6425sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl7_3.this.toString() + \".Role6425sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl7_1 as Role6425sl7_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl7_6.java",
			    "\n" +
			    "public class T6425sl7_6 extends T6425sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl7_2.java",
			    "\n" +
			    "public abstract class T6425sl7_2 extends T6425sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl7_3.java",
			    "\n" +
			    "public class T6425sl7_3 extends T6425sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl7_1.java",
			    "\n" +
			    "public team class Team6425sl7_1 {\n" +
			    "    public class Role6425sl7_1 extends T6425sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl7_1.this.toString() + \".Role6425sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl7_2 extends Role6425sl7_1 playedBy T6425sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl7_1.this.toString() + \".Role6425sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl7_3 extends Role6425sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl7_1.this.toString() + \".Role6425sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl7_4.java",
			    "\n" +
			    "public class T6425sl7_4 extends T6425sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl7_2.java",
			    "\n" +
			    "public team class Team6425sl7_2 extends Team6425sl7_1 {\n" +
			    "    public class Role6425sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl7_2.this.toString() + \".Role6425sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl7_4 extends Role6425sl7_3 playedBy T6425sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl7_2.this.toString() + \".Role6425sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl7_5.java",
			    "\n" +
			    "public class T6425sl7_5 extends T6425sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl7_1.java",
			    "\n" +
			    "public class T6425sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-8
    public void test6425_smartLifting8() {
        runNegativeTest(
            new String[] {
		"Team6425sl8_4.java",
			    "\n" +
			    "public team class Team6425sl8_4 extends Team6425sl8_3 {\n" +
			    "    public class Role6425sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl8_4.this.toString() + \".Role6425sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl8Main.java",
			    "\n" +
			    "public class T6425sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl8_3 t = new Team6425sl8_4();\n" +
			    "        T6425sl8_1    o = new T6425sl8_5();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl8_2.java",
			    "\n" +
			    "public team class Team6425sl8_2 extends Team6425sl8_1 {\n" +
			    "    public class Role6425sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl8_2.this.toString() + \".Role6425sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl8_4 extends Role6425sl8_3 playedBy T6425sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl8_2.this.toString() + \".Role6425sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl8_5.java",
			    "\n" +
			    "public class T6425sl8_5 extends T6425sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl8_1.java",
			    "\n" +
			    "public class T6425sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl8_3.java",
			    "\n" +
			    "public team class Team6425sl8_3 extends Team6425sl8_2 {\n" +
			    "    public class Role6425sl8_5 extends Role6425sl8_3 playedBy T6425sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl8_3.this.toString() + \".Role6425sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl8_1 as Role6425sl8_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl8_6.java",
			    "\n" +
			    "public class T6425sl8_6 extends T6425sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl8_2.java",
			    "\n" +
			    "public abstract class T6425sl8_2 extends T6425sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl8_3.java",
			    "\n" +
			    "public class T6425sl8_3 extends T6425sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl8_1.java",
			    "\n" +
			    "public team class Team6425sl8_1 {\n" +
			    "    public class Role6425sl8_1 extends T6425sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl8_1.this.toString() + \".Role6425sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl8_2 extends Role6425sl8_1 playedBy T6425sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl8_1.this.toString() + \".Role6425sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl8_3 extends Role6425sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl8_1.this.toString() + \".Role6425sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl8_4.java",
			    "\n" +
			    "public class T6425sl8_4 extends T6425sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-9
    public void test6425_smartLifting9() {
        runNegativeTest(
            new String[] {
		"Team6425sl9_4.java",
			    "\n" +
			    "public team class Team6425sl9_4 extends Team6425sl9_3 {\n" +
			    "    public class Role6425sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl9_4.this.toString() + \".Role6425sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl9Main.java",
			    "\n" +
			    "public class T6425sl9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl9_3 t = new Team6425sl9_3();\n" +
			    "        T6425sl9_1    o = new T6425sl9_6();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl9_1.java",
			    "\n" +
			    "public team class Team6425sl9_1 {\n" +
			    "    public class Role6425sl9_1 extends T6425sl9_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl9_1.this.toString() + \".Role6425sl9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl9_2 extends Role6425sl9_1 playedBy T6425sl9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl9_1.this.toString() + \".Role6425sl9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl9_3 extends Role6425sl9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl9_1.this.toString() + \".Role6425sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl9_4.java",
			    "\n" +
			    "public class T6425sl9_4 extends T6425sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl9_2.java",
			    "\n" +
			    "public team class Team6425sl9_2 extends Team6425sl9_1 {\n" +
			    "    public class Role6425sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl9_2.this.toString() + \".Role6425sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl9_4 extends Role6425sl9_3 playedBy T6425sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl9_2.this.toString() + \".Role6425sl9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl9_5.java",
			    "\n" +
			    "public class T6425sl9_5 extends T6425sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl9_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl9_1.java",
			    "\n" +
			    "public class T6425sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl9_3.java",
			    "\n" +
			    "public team class Team6425sl9_3 extends Team6425sl9_2 {\n" +
			    "    public class Role6425sl9_5 extends Role6425sl9_3 playedBy T6425sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl9_3.this.toString() + \".Role6425sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl9_1 as Role6425sl9_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl9_6.java",
			    "\n" +
			    "public class T6425sl9_6 extends T6425sl9_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl9_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl9_2.java",
			    "\n" +
			    "public abstract class T6425sl9_2 extends T6425sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl9_3.java",
			    "\n" +
			    "public class T6425sl9_3 extends T6425sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-10
    public void test6425_smartLifting10() {
        runNegativeTest(
            new String[] {
		"Team6425sl10_4.java",
			    "\n" +
			    "public team class Team6425sl10_4 extends Team6425sl10_3 {\n" +
			    "    public class Role6425sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl10_4.this.toString() + \".Role6425sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl10Main.java",
			    "\n" +
			    "public class T6425sl10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl10_3 t = new Team6425sl10_4();\n" +
			    "        T6425sl10_1    o = new T6425sl10_6();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl10_1.java",
			    "\n" +
			    "public team class Team6425sl10_1 {\n" +
			    "    public class Role6425sl10_1 extends T6425sl10_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl10_1.this.toString() + \".Role6425sl10_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl10_2 extends Role6425sl10_1 playedBy T6425sl10_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl10_1.this.toString() + \".Role6425sl10_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl10_3 extends Role6425sl10_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl10_1.this.toString() + \".Role6425sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl10_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl10_4.java",
			    "\n" +
			    "public class T6425sl10_4 extends T6425sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl10_2.java",
			    "\n" +
			    "public team class Team6425sl10_2 extends Team6425sl10_1 {\n" +
			    "    public class Role6425sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl10_2.this.toString() + \".Role6425sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl10_4 extends Role6425sl10_3 playedBy T6425sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl10_2.this.toString() + \".Role6425sl10_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl10_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl10_5.java",
			    "\n" +
			    "public class T6425sl10_5 extends T6425sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl10_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl10_1.java",
			    "\n" +
			    "public class T6425sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl10_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl10_3.java",
			    "\n" +
			    "public team class Team6425sl10_3 extends Team6425sl10_2 {\n" +
			    "    public class Role6425sl10_5 extends Role6425sl10_3 playedBy T6425sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl10_3.this.toString() + \".Role6425sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl10_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl10_1 as Role6425sl10_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl10_6.java",
			    "\n" +
			    "public class T6425sl10_6 extends T6425sl10_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl10_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl10_2.java",
			    "\n" +
			    "public abstract class T6425sl10_2 extends T6425sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl10_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl10_3.java",
			    "\n" +
			    "public class T6425sl10_3 extends T6425sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl10_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-11
    public void test6425_smartLifting11() {
        runNegativeTest(
            new String[] {
		"Team6425sl11_4.java",
			    "\n" +
			    "public team class Team6425sl11_4 extends Team6425sl11_3 {\n" +
			    "    public class Role6425sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl11_4.this.toString() + \".Role6425sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl11Main.java",
			    "\n" +
			    "public class T6425sl11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl11_3       t  = new Team6425sl11_3();\n" +
			    "        final Team6425sl11_1 ft = new Team6425sl11_1();\n" +
			    "        T6425sl11_1          o  = ft.new Role6425sl11_1();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl11_3.java",
			    "\n" +
			    "public class T6425sl11_3 extends T6425sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl11_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl11_1.java",
			    "\n" +
			    "public team class Team6425sl11_1 {\n" +
			    "    public class Role6425sl11_1 extends T6425sl11_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl11_1.this.toString() + \".Role6425sl11_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl11_2 extends Role6425sl11_1 playedBy T6425sl11_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl11_1.this.toString() + \".Role6425sl11_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl11_3 extends Role6425sl11_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl11_1.this.toString() + \".Role6425sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl11_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl11_4.java",
			    "\n" +
			    "public class T6425sl11_4 extends T6425sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl11_2.java",
			    "\n" +
			    "public team class Team6425sl11_2 extends Team6425sl11_1 {\n" +
			    "    public class Role6425sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl11_2.this.toString() + \".Role6425sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl11_4 extends Role6425sl11_3 playedBy T6425sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl11_2.this.toString() + \".Role6425sl11_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl11_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl11_5.java",
			    "\n" +
			    "public class T6425sl11_5 extends T6425sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl11_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl11_1.java",
			    "\n" +
			    "public class T6425sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl11_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl11_3.java",
			    "\n" +
			    "public team class Team6425sl11_3 extends Team6425sl11_2 {\n" +
			    "    public class Role6425sl11_5 extends Role6425sl11_3 playedBy T6425sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl11_3.this.toString() + \".Role6425sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl11_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl11_1 as Role6425sl11_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl11_6.java",
			    "\n" +
			    "public class T6425sl11_6 extends T6425sl11_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl11_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl11_2.java",
			    "\n" +
			    "public abstract class T6425sl11_2 extends T6425sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl11_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.25-otjld-smart-lifting-12
    public void test6425_smartLifting12() {
        runNegativeTest(
            new String[] {
		"Team6425sl12_4.java",
			    "\n" +
			    "public team class Team6425sl12_4 extends Team6425sl12_3 {\n" +
			    "    public class Role6425sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl12_4.this.toString() + \".Role6425sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl12Main.java",
			    "\n" +
			    "public class T6425sl12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6425sl12_3       t  = new Team6425sl12_4();\n" +
			    "        final Team6425sl12_1 ft = new Team6425sl12_1();\n" +
			    "        T6425sl12_1          o  = ft.new Role6425sl12_1();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl12_3.java",
			    "\n" +
			    "public team class Team6425sl12_3 extends Team6425sl12_2 {\n" +
			    "    public class Role6425sl12_5 extends Role6425sl12_3 playedBy T6425sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl12_3.this.toString() + \".Role6425sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl12_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6425sl12_1 as Role6425sl12_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl12_6.java",
			    "\n" +
			    "public class T6425sl12_6 extends T6425sl12_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl12_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl12_2.java",
			    "\n" +
			    "public abstract class T6425sl12_2 extends T6425sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl12_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl12_3.java",
			    "\n" +
			    "public class T6425sl12_3 extends T6425sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl12_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl12_1.java",
			    "\n" +
			    "public team class Team6425sl12_1 {\n" +
			    "    public class Role6425sl12_1 extends T6425sl12_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl12_1.this.toString() + \".Role6425sl12_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6425sl12_2 extends Role6425sl12_1 playedBy T6425sl12_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl12_1.this.toString() + \".Role6425sl12_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6425sl12_3 extends Role6425sl12_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl12_1.this.toString() + \".Role6425sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl12_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl12_4.java",
			    "\n" +
			    "public class T6425sl12_4 extends T6425sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6425sl12_2.java",
			    "\n" +
			    "public team class Team6425sl12_2 extends Team6425sl12_1 {\n" +
			    "    public class Role6425sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl12_2.this.toString() + \".Role6425sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6425sl12_4 extends Role6425sl12_3 playedBy T6425sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6425sl12_2.this.toString() + \".Role6425sl12_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6425sl12_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6425sl12_5.java",
			    "\n" +
			    "public class T6425sl12_5 extends T6425sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl12_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6425sl12_1.java",
			    "\n" +
			    "public class T6425sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6425sl12_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }
}
