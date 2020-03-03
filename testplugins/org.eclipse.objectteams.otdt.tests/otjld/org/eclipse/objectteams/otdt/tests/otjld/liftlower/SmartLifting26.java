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

public class SmartLifting26 extends AbstractOTJLDTest {
	
	public SmartLifting26(String name) {
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
		return SmartLifting26.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.26-otjld-smart-lifting-1
    public void test6426_smartLifting1() {
        runNegativeTest(
            new String[] {
		"Team6426sl1_4.java",
			    "\n" +
			    "public team class Team6426sl1_4 extends Team6426sl1_3 {\n" +
			    "    public class Role6426sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl1_4.this.toString() + \".Role6426sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl1Main.java",
			    "\n" +
			    "public class T6426sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6426sl1_3 t = new Team6426sl1_3();\n" +
			    "        T6426sl1_2    o = new T6426sl1_3();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl1_3.java",
			    "\n" +
			    "public class T6426sl1_3 extends T6426sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl1_1.java",
			    "\n" +
			    "public team class Team6426sl1_1 {\n" +
			    "    public class Role6426sl1_1 extends T6426sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl1_1.this.toString() + \".Role6426sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6426sl1_2 extends Role6426sl1_1 playedBy T6426sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl1_1.this.toString() + \".Role6426sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6426sl1_3 extends Role6426sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl1_1.this.toString() + \".Role6426sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6426sl1_4.java",
			    "\n" +
			    "public class T6426sl1_4 extends T6426sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl1_2.java",
			    "\n" +
			    "public team class Team6426sl1_2 extends Team6426sl1_1 {\n" +
			    "    public class Role6426sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl1_2.this.toString() + \".Role6426sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6426sl1_4 extends Role6426sl1_3 playedBy T6426sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl1_2.this.toString() + \".Role6426sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6426sl1_5.java",
			    "\n" +
			    "public class T6426sl1_5 extends T6426sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl1_1.java",
			    "\n" +
			    "public class T6426sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl1_3.java",
			    "\n" +
			    "public team class Team6426sl1_3 extends Team6426sl1_2 {\n" +
			    "    public class Role6426sl1_5 extends Role6426sl1_3 playedBy T6426sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl1_3.this.toString() + \".Role6426sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6426sl1_2 as Role6426sl1_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl1_6.java",
			    "\n" +
			    "public class T6426sl1_6 extends T6426sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl1_2.java",
			    "\n" +
			    "public abstract class T6426sl1_2 extends T6426sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.26-otjld-smart-lifting-2
    public void test6426_smartLifting2() {
        runNegativeTest(
            new String[] {
		"Team6426sl2_4.java",
			    "\n" +
			    "public team class Team6426sl2_4 extends Team6426sl2_3 {\n" +
			    "    public class Role6426sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl2_4.this.toString() + \".Role6426sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl2Main.java",
			    "\n" +
			    "public class T6426sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6426sl2_3 t = new Team6426sl2_4();\n" +
			    "        T6426sl2_2    o = new T6426sl2_3();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl2_3.java",
			    "\n" +
			    "public team class Team6426sl2_3 extends Team6426sl2_2 {\n" +
			    "    public class Role6426sl2_5 extends Role6426sl2_3 playedBy T6426sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl2_3.this.toString() + \".Role6426sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6426sl2_2 as Role6426sl2_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl2_6.java",
			    "\n" +
			    "public class T6426sl2_6 extends T6426sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl2_2.java",
			    "\n" +
			    "public abstract class T6426sl2_2 extends T6426sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl2_3.java",
			    "\n" +
			    "public class T6426sl2_3 extends T6426sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl2_1.java",
			    "\n" +
			    "public team class Team6426sl2_1 {\n" +
			    "    public class Role6426sl2_1 extends T6426sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl2_1.this.toString() + \".Role6426sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6426sl2_2 extends Role6426sl2_1 playedBy T6426sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl2_1.this.toString() + \".Role6426sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6426sl2_3 extends Role6426sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl2_1.this.toString() + \".Role6426sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6426sl2_4.java",
			    "\n" +
			    "public class T6426sl2_4 extends T6426sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl2_2.java",
			    "\n" +
			    "public team class Team6426sl2_2 extends Team6426sl2_1 {\n" +
			    "    public class Role6426sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl2_2.this.toString() + \".Role6426sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6426sl2_4 extends Role6426sl2_3 playedBy T6426sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl2_2.this.toString() + \".Role6426sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6426sl2_5.java",
			    "\n" +
			    "public class T6426sl2_5 extends T6426sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl2_1.java",
			    "\n" +
			    "public class T6426sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.26-otjld-smart-lifting-3
    public void test6426_smartLifting3() {
        runNegativeTest(
            new String[] {
		"Team6426sl3_4.java",
			    "\n" +
			    "public team class Team6426sl3_4 extends Team6426sl3_3 {\n" +
			    "    public class Role6426sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl3_4.this.toString() + \".Role6426sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl3Main.java",
			    "\n" +
			    "public class T6426sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6426sl3_3 t = new Team6426sl3_3();\n" +
			    "        T6426sl3_2    o = new T6426sl3_4();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl3_2.java",
			    "\n" +
			    "public team class Team6426sl3_2 extends Team6426sl3_1 {\n" +
			    "    public class Role6426sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl3_2.this.toString() + \".Role6426sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6426sl3_4 extends Role6426sl3_3 playedBy T6426sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl3_2.this.toString() + \".Role6426sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6426sl3_5.java",
			    "\n" +
			    "public class T6426sl3_5 extends T6426sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl3_1.java",
			    "\n" +
			    "public class T6426sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl3_3.java",
			    "\n" +
			    "public team class Team6426sl3_3 extends Team6426sl3_2 {\n" +
			    "    public class Role6426sl3_5 extends Role6426sl3_3 playedBy T6426sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl3_3.this.toString() + \".Role6426sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6426sl3_2 as Role6426sl3_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl3_6.java",
			    "\n" +
			    "public class T6426sl3_6 extends T6426sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl3_2.java",
			    "\n" +
			    "public abstract class T6426sl3_2 extends T6426sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl3_3.java",
			    "\n" +
			    "public class T6426sl3_3 extends T6426sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl3_1.java",
			    "\n" +
			    "public team class Team6426sl3_1 {\n" +
			    "    public class Role6426sl3_1 extends T6426sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl3_1.this.toString() + \".Role6426sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6426sl3_2 extends Role6426sl3_1 playedBy T6426sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl3_1.this.toString() + \".Role6426sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6426sl3_3 extends Role6426sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl3_1.this.toString() + \".Role6426sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6426sl3_4.java",
			    "\n" +
			    "public class T6426sl3_4 extends T6426sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.26-otjld-smart-lifting-4
    public void test6426_smartLifting4() {
        runNegativeTest(
            new String[] {
		"Team6426sl4_4.java",
			    "\n" +
			    "public team class Team6426sl4_4 extends Team6426sl4_3 {\n" +
			    "    public class Role6426sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl4_4.this.toString() + \".Role6426sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl4Main.java",
			    "\n" +
			    "public class T6426sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6426sl4_3 t = new Team6426sl4_4();\n" +
			    "        T6426sl4_2    o = new T6426sl4_4();\n" +
			    "\n" +
			    "        System.out.print(t.t3(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl4_1.java",
			    "\n" +
			    "public team class Team6426sl4_1 {\n" +
			    "    public class Role6426sl4_1 extends T6426sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl4_1.this.toString() + \".Role6426sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6426sl4_2 extends Role6426sl4_1 playedBy T6426sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl4_1.this.toString() + \".Role6426sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6426sl4_3 extends Role6426sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl4_1.this.toString() + \".Role6426sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6426sl4_4.java",
			    "\n" +
			    "public class T6426sl4_4 extends T6426sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl4_2.java",
			    "\n" +
			    "public team class Team6426sl4_2 extends Team6426sl4_1 {\n" +
			    "    public class Role6426sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl4_2.this.toString() + \".Role6426sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6426sl4_4 extends Role6426sl4_3 playedBy T6426sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl4_2.this.toString() + \".Role6426sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6426sl4_5.java",
			    "\n" +
			    "public class T6426sl4_5 extends T6426sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl4_1.java",
			    "\n" +
			    "public class T6426sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6426sl4_3.java",
			    "\n" +
			    "public team class Team6426sl4_3 extends Team6426sl4_2 {\n" +
			    "    public class Role6426sl4_5 extends Role6426sl4_3 playedBy T6426sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6426sl4_3.this.toString() + \".Role6426sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6426sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t3(T6426sl4_2 as Role6426sl4_5 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl4_6.java",
			    "\n" +
			    "public class T6426sl4_6 extends T6426sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl4_2.java",
			    "\n" +
			    "public abstract class T6426sl4_2 extends T6426sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6426sl4_3.java",
			    "\n" +
			    "public class T6426sl4_3 extends T6426sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6426sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }
}
