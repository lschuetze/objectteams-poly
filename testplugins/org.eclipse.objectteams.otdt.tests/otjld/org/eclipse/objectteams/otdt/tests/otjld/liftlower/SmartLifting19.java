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

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class SmartLifting19 extends AbstractOTJLDTest {

	public SmartLifting19(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test0c11_implicitlyInheritingStaticRoleMethod1"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Class testClass() {
		return SmartLifting19.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-1
    public void test6419_smartLifting1() {
        runNegativeTest(
            new String[] {
		"Team6419sl1_4.java",
			    "\n" +
			    "public team class Team6419sl1_4 extends Team6419sl1_3 {\n" +
			    "    public class Role6419sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl1_4.this.toString() + \".Role6419sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl1Main.java",
			    "\n" +
			    "public class T6419sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl1_2 t = new Team6419sl1_2();\n" +
			    "        T6419sl1_1    o = new T6419sl1_1();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl1_2.java",
			    "\n" +
			    "public team class Team6419sl1_2 extends Team6419sl1_1 {\n" +
			    "    public class Role6419sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl1_2.this.toString() + \".Role6419sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl1_4 extends Role6419sl1_3 playedBy T6419sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl1_2.this.toString() + \".Role6419sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl1_1 as Role6419sl1_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl1_5.java",
			    "\n" +
			    "public class T6419sl1_5 extends T6419sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl1_1.java",
			    "\n" +
			    "public class T6419sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl1_3.java",
			    "\n" +
			    "public team class Team6419sl1_3 extends Team6419sl1_2 {\n" +
			    "    public class Role6419sl1_5 extends Role6419sl1_3 playedBy T6419sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl1_3.this.toString() + \".Role6419sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl1_6.java",
			    "\n" +
			    "public class T6419sl1_6 extends T6419sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl1_2.java",
			    "\n" +
			    "public abstract class T6419sl1_2 extends T6419sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl1_3.java",
			    "\n" +
			    "public class T6419sl1_3 extends T6419sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl1_1.java",
			    "\n" +
			    "public team class Team6419sl1_1 {\n" +
			    "    public class Role6419sl1_1 extends T6419sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl1_1.this.toString() + \".Role6419sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl1_2 extends Role6419sl1_1 playedBy T6419sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl1_1.this.toString() + \".Role6419sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl1_3 extends Role6419sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl1_1.this.toString() + \".Role6419sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl1_4.java",
			    "\n" +
			    "public class T6419sl1_4 extends T6419sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-2
    public void test6419_smartLifting2() {
        runNegativeTest(
            new String[] {
		"Team6419sl2_4.java",
			    "\n" +
			    "public team class Team6419sl2_4 extends Team6419sl2_3 {\n" +
			    "    public class Role6419sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl2_4.this.toString() + \".Role6419sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl2Main.java",
			    "\n" +
			    "public class T6419sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl2_2 t = new Team6419sl2_3();\n" +
			    "        T6419sl2_1    o = new T6419sl2_1();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl2_1.java",
			    "\n" +
			    "public team class Team6419sl2_1 {\n" +
			    "    public class Role6419sl2_1 extends T6419sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl2_1.this.toString() + \".Role6419sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl2_2 extends Role6419sl2_1 playedBy T6419sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl2_1.this.toString() + \".Role6419sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl2_3 extends Role6419sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl2_1.this.toString() + \".Role6419sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl2_4.java",
			    "\n" +
			    "public class T6419sl2_4 extends T6419sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl2_2.java",
			    "\n" +
			    "public team class Team6419sl2_2 extends Team6419sl2_1 {\n" +
			    "    public class Role6419sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl2_2.this.toString() + \".Role6419sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl2_4 extends Role6419sl2_3 playedBy T6419sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl2_2.this.toString() + \".Role6419sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl2_1 as Role6419sl2_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl2_5.java",
			    "\n" +
			    "public class T6419sl2_5 extends T6419sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl2_1.java",
			    "\n" +
			    "public class T6419sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl2_3.java",
			    "\n" +
			    "public team class Team6419sl2_3 extends Team6419sl2_2 {\n" +
			    "    public class Role6419sl2_5 extends Role6419sl2_3 playedBy T6419sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl2_3.this.toString() + \".Role6419sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl2_6.java",
			    "\n" +
			    "public class T6419sl2_6 extends T6419sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl2_2.java",
			    "\n" +
			    "public abstract class T6419sl2_2 extends T6419sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl2_3.java",
			    "\n" +
			    "public class T6419sl2_3 extends T6419sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-3
    public void test6419_smartLifting3() {
        runNegativeTest(
            new String[] {
		"Team6419sl3_4.java",
			    "\n" +
			    "public team class Team6419sl3_4 extends Team6419sl3_3 {\n" +
			    "    public class Role6419sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl3_4.this.toString() + \".Role6419sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl3Main.java",
			    "\n" +
			    "public class T6419sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl3_2 t = new Team6419sl3_4();\n" +
			    "        T6419sl3_1    o = new T6419sl3_1();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl3_3.java",
			    "\n" +
			    "public class T6419sl3_3 extends T6419sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl3_1.java",
			    "\n" +
			    "public team class Team6419sl3_1 {\n" +
			    "    public class Role6419sl3_1 extends T6419sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl3_1.this.toString() + \".Role6419sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl3_2 extends Role6419sl3_1 playedBy T6419sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl3_1.this.toString() + \".Role6419sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl3_3 extends Role6419sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl3_1.this.toString() + \".Role6419sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl3_4.java",
			    "\n" +
			    "public class T6419sl3_4 extends T6419sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl3_2.java",
			    "\n" +
			    "public team class Team6419sl3_2 extends Team6419sl3_1 {\n" +
			    "    public class Role6419sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl3_2.this.toString() + \".Role6419sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl3_4 extends Role6419sl3_3 playedBy T6419sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl3_2.this.toString() + \".Role6419sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl3_1 as Role6419sl3_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl3_5.java",
			    "\n" +
			    "public class T6419sl3_5 extends T6419sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl3_1.java",
			    "\n" +
			    "public class T6419sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl3_3.java",
			    "\n" +
			    "public team class Team6419sl3_3 extends Team6419sl3_2 {\n" +
			    "    public class Role6419sl3_5 extends Role6419sl3_3 playedBy T6419sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl3_3.this.toString() + \".Role6419sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl3_6.java",
			    "\n" +
			    "public class T6419sl3_6 extends T6419sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl3_2.java",
			    "\n" +
			    "public abstract class T6419sl3_2 extends T6419sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-4
    public void test6419_smartLifting4() {
        runNegativeTest(
            new String[] {
		"Team6419sl4_4.java",
			    "\n" +
			    "public team class Team6419sl4_4 extends Team6419sl4_3 {\n" +
			    "    public class Role6419sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl4_4.this.toString() + \".Role6419sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl4Main.java",
			    "\n" +
			    "public class T6419sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl4_2 t = new Team6419sl4_2();\n" +
			    "        T6419sl4_1    o = new T6419sl4_3();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl4_3.java",
			    "\n" +
			    "public team class Team6419sl4_3 extends Team6419sl4_2 {\n" +
			    "    public class Role6419sl4_5 extends Role6419sl4_3 playedBy T6419sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl4_3.this.toString() + \".Role6419sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl4_6.java",
			    "\n" +
			    "public class T6419sl4_6 extends T6419sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl4_2.java",
			    "\n" +
			    "public abstract class T6419sl4_2 extends T6419sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl4_3.java",
			    "\n" +
			    "public class T6419sl4_3 extends T6419sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl4_1.java",
			    "\n" +
			    "public team class Team6419sl4_1 {\n" +
			    "    public class Role6419sl4_1 extends T6419sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl4_1.this.toString() + \".Role6419sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl4_2 extends Role6419sl4_1 playedBy T6419sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl4_1.this.toString() + \".Role6419sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl4_3 extends Role6419sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl4_1.this.toString() + \".Role6419sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl4_4.java",
			    "\n" +
			    "public class T6419sl4_4 extends T6419sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl4_2.java",
			    "\n" +
			    "public team class Team6419sl4_2 extends Team6419sl4_1 {\n" +
			    "    public class Role6419sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl4_2.this.toString() + \".Role6419sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl4_4 extends Role6419sl4_3 playedBy T6419sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl4_2.this.toString() + \".Role6419sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl4_1 as Role6419sl4_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl4_5.java",
			    "\n" +
			    "public class T6419sl4_5 extends T6419sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl4_1.java",
			    "\n" +
			    "public class T6419sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-5
    public void test6419_smartLifting5() {
        runNegativeTest(
            new String[] {
		"Team6419sl5_4.java",
			    "\n" +
			    "public team class Team6419sl5_4 extends Team6419sl5_3 {\n" +
			    "    public class Role6419sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl5_4.this.toString() + \".Role6419sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl5Main.java",
			    "\n" +
			    "public class T6419sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl5_2 t = new Team6419sl5_3();\n" +
			    "        T6419sl5_1    o = new T6419sl5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl5_2.java",
			    "\n" +
			    "public team class Team6419sl5_2 extends Team6419sl5_1 {\n" +
			    "    public class Role6419sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl5_2.this.toString() + \".Role6419sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl5_4 extends Role6419sl5_3 playedBy T6419sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl5_2.this.toString() + \".Role6419sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl5_1 as Role6419sl5_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl5_5.java",
			    "\n" +
			    "public class T6419sl5_5 extends T6419sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl5_1.java",
			    "\n" +
			    "public class T6419sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl5_3.java",
			    "\n" +
			    "public team class Team6419sl5_3 extends Team6419sl5_2 {\n" +
			    "    public class Role6419sl5_5 extends Role6419sl5_3 playedBy T6419sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl5_3.this.toString() + \".Role6419sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl5_6.java",
			    "\n" +
			    "public class T6419sl5_6 extends T6419sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl5_2.java",
			    "\n" +
			    "public abstract class T6419sl5_2 extends T6419sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl5_3.java",
			    "\n" +
			    "public class T6419sl5_3 extends T6419sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl5_1.java",
			    "\n" +
			    "public team class Team6419sl5_1 {\n" +
			    "    public class Role6419sl5_1 extends T6419sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl5_1.this.toString() + \".Role6419sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl5_2 extends Role6419sl5_1 playedBy T6419sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl5_1.this.toString() + \".Role6419sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl5_3 extends Role6419sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl5_1.this.toString() + \".Role6419sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl5_4.java",
			    "\n" +
			    "public class T6419sl5_4 extends T6419sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-6
    public void test6419_smartLifting6() {
        runNegativeTest(
            new String[] {
		"Team6419sl6_4.java",
			    "\n" +
			    "public team class Team6419sl6_4 extends Team6419sl6_3 {\n" +
			    "    public class Role6419sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl6_4.this.toString() + \".Role6419sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl6Main.java",
			    "\n" +
			    "public class T6419sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl6_2 t = new Team6419sl6_4();\n" +
			    "        T6419sl6_1    o = new T6419sl6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl6_1.java",
			    "\n" +
			    "public team class Team6419sl6_1 {\n" +
			    "    public class Role6419sl6_1 extends T6419sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl6_1.this.toString() + \".Role6419sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl6_2 extends Role6419sl6_1 playedBy T6419sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl6_1.this.toString() + \".Role6419sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl6_3 extends Role6419sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl6_1.this.toString() + \".Role6419sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl6_4.java",
			    "\n" +
			    "public class T6419sl6_4 extends T6419sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl6_2.java",
			    "\n" +
			    "public team class Team6419sl6_2 extends Team6419sl6_1 {\n" +
			    "    public class Role6419sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl6_2.this.toString() + \".Role6419sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl6_4 extends Role6419sl6_3 playedBy T6419sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl6_2.this.toString() + \".Role6419sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl6_1 as Role6419sl6_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl6_5.java",
			    "\n" +
			    "public class T6419sl6_5 extends T6419sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl6_1.java",
			    "\n" +
			    "public class T6419sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl6_3.java",
			    "\n" +
			    "public team class Team6419sl6_3 extends Team6419sl6_2 {\n" +
			    "    public class Role6419sl6_5 extends Role6419sl6_3 playedBy T6419sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl6_3.this.toString() + \".Role6419sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl6_6.java",
			    "\n" +
			    "public class T6419sl6_6 extends T6419sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl6_2.java",
			    "\n" +
			    "public abstract class T6419sl6_2 extends T6419sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl6_3.java",
			    "\n" +
			    "public class T6419sl6_3 extends T6419sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-7
    public void test6419_smartLifting7() {
        runNegativeTest(
            new String[] {
		"Team6419sl7_4.java",
			    "\n" +
			    "public team class Team6419sl7_4 extends Team6419sl7_3 {\n" +
			    "    public class Role6419sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl7_4.this.toString() + \".Role6419sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl7Main.java",
			    "\n" +
			    "public class T6419sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl7_2 t = new Team6419sl7_2();\n" +
			    "        T6419sl7_1    o = new T6419sl7_4();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl7_3.java",
			    "\n" +
			    "public class T6419sl7_3 extends T6419sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl7_1.java",
			    "\n" +
			    "public team class Team6419sl7_1 {\n" +
			    "    public class Role6419sl7_1 extends T6419sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl7_1.this.toString() + \".Role6419sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl7_2 extends Role6419sl7_1 playedBy T6419sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl7_1.this.toString() + \".Role6419sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl7_3 extends Role6419sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl7_1.this.toString() + \".Role6419sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl7_4.java",
			    "\n" +
			    "public class T6419sl7_4 extends T6419sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl7_2.java",
			    "\n" +
			    "public team class Team6419sl7_2 extends Team6419sl7_1 {\n" +
			    "    public class Role6419sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl7_2.this.toString() + \".Role6419sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl7_4 extends Role6419sl7_3 playedBy T6419sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl7_2.this.toString() + \".Role6419sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl7_1 as Role6419sl7_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl7_5.java",
			    "\n" +
			    "public class T6419sl7_5 extends T6419sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl7_1.java",
			    "\n" +
			    "public class T6419sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl7_3.java",
			    "\n" +
			    "public team class Team6419sl7_3 extends Team6419sl7_2 {\n" +
			    "    public class Role6419sl7_5 extends Role6419sl7_3 playedBy T6419sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl7_3.this.toString() + \".Role6419sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl7_6.java",
			    "\n" +
			    "public class T6419sl7_6 extends T6419sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl7_2.java",
			    "\n" +
			    "public abstract class T6419sl7_2 extends T6419sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-8
    public void test6419_smartLifting8() {
        runNegativeTest(
            new String[] {
		"Team6419sl8_4.java",
			    "\n" +
			    "public team class Team6419sl8_4 extends Team6419sl8_3 {\n" +
			    "    public class Role6419sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl8_4.this.toString() + \".Role6419sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl8Main.java",
			    "\n" +
			    "public class T6419sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl8_2 t = new Team6419sl8_3();\n" +
			    "        T6419sl8_1    o = new T6419sl8_4();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl8_3.java",
			    "\n" +
			    "public team class Team6419sl8_3 extends Team6419sl8_2 {\n" +
			    "    public class Role6419sl8_5 extends Role6419sl8_3 playedBy T6419sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl8_3.this.toString() + \".Role6419sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl8_6.java",
			    "\n" +
			    "public class T6419sl8_6 extends T6419sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl8_2.java",
			    "\n" +
			    "public abstract class T6419sl8_2 extends T6419sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl8_3.java",
			    "\n" +
			    "public class T6419sl8_3 extends T6419sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl8_1.java",
			    "\n" +
			    "public team class Team6419sl8_1 {\n" +
			    "    public class Role6419sl8_1 extends T6419sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl8_1.this.toString() + \".Role6419sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl8_2 extends Role6419sl8_1 playedBy T6419sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl8_1.this.toString() + \".Role6419sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl8_3 extends Role6419sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl8_1.this.toString() + \".Role6419sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl8_4.java",
			    "\n" +
			    "public class T6419sl8_4 extends T6419sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl8_2.java",
			    "\n" +
			    "public team class Team6419sl8_2 extends Team6419sl8_1 {\n" +
			    "    public class Role6419sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl8_2.this.toString() + \".Role6419sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl8_4 extends Role6419sl8_3 playedBy T6419sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl8_2.this.toString() + \".Role6419sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl8_1 as Role6419sl8_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl8_5.java",
			    "\n" +
			    "public class T6419sl8_5 extends T6419sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl8_1.java",
			    "\n" +
			    "public class T6419sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-9
    public void test6419_smartLifting9() {
        runNegativeTest(
            new String[] {
		"Team6419sl9_4.java",
			    "\n" +
			    "public team class Team6419sl9_4 extends Team6419sl9_3 {\n" +
			    "    public class Role6419sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl9_4.this.toString() + \".Role6419sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl9Main.java",
			    "\n" +
			    "public class T6419sl9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl9_2 t = new Team6419sl9_4();\n" +
			    "        T6419sl9_1    o = new T6419sl9_4();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl9_2.java",
			    "\n" +
			    "public team class Team6419sl9_2 extends Team6419sl9_1 {\n" +
			    "    public class Role6419sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl9_2.this.toString() + \".Role6419sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl9_4 extends Role6419sl9_3 playedBy T6419sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl9_2.this.toString() + \".Role6419sl9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl9_1 as Role6419sl9_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl9_5.java",
			    "\n" +
			    "public class T6419sl9_5 extends T6419sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl9_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl9_1.java",
			    "\n" +
			    "public class T6419sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl9_3.java",
			    "\n" +
			    "public team class Team6419sl9_3 extends Team6419sl9_2 {\n" +
			    "    public class Role6419sl9_5 extends Role6419sl9_3 playedBy T6419sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl9_3.this.toString() + \".Role6419sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl9_6.java",
			    "\n" +
			    "public class T6419sl9_6 extends T6419sl9_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl9_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl9_2.java",
			    "\n" +
			    "public abstract class T6419sl9_2 extends T6419sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl9_3.java",
			    "\n" +
			    "public class T6419sl9_3 extends T6419sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl9_1.java",
			    "\n" +
			    "public team class Team6419sl9_1 {\n" +
			    "    public class Role6419sl9_1 extends T6419sl9_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl9_1.this.toString() + \".Role6419sl9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl9_2 extends Role6419sl9_1 playedBy T6419sl9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl9_1.this.toString() + \".Role6419sl9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl9_3 extends Role6419sl9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl9_1.this.toString() + \".Role6419sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl9_4.java",
			    "\n" +
			    "public class T6419sl9_4 extends T6419sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-10
    public void test6419_smartLifting10() {
        runNegativeTest(
            new String[] {
		"Team6419sl10_4.java",
			    "\n" +
			    "public team class Team6419sl10_4 extends Team6419sl10_3 {\n" +
			    "    public class Role6419sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl10_4.this.toString() + \".Role6419sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl10Main.java",
			    "\n" +
			    "public class T6419sl10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl10_2 t = new Team6419sl10_2();\n" +
			    "        T6419sl10_1    o = new T6419sl10_5();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl10_2.java",
			    "\n" +
			    "public team class Team6419sl10_2 extends Team6419sl10_1 {\n" +
			    "    public class Role6419sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl10_2.this.toString() + \".Role6419sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl10_4 extends Role6419sl10_3 playedBy T6419sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl10_2.this.toString() + \".Role6419sl10_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl10_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl10_1 as Role6419sl10_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl10_5.java",
			    "\n" +
			    "public class T6419sl10_5 extends T6419sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl10_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl10_1.java",
			    "\n" +
			    "public class T6419sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl10_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl10_3.java",
			    "\n" +
			    "public team class Team6419sl10_3 extends Team6419sl10_2 {\n" +
			    "    public class Role6419sl10_5 extends Role6419sl10_3 playedBy T6419sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl10_3.this.toString() + \".Role6419sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl10_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl10_6.java",
			    "\n" +
			    "public class T6419sl10_6 extends T6419sl10_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl10_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl10_2.java",
			    "\n" +
			    "public abstract class T6419sl10_2 extends T6419sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl10_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl10_3.java",
			    "\n" +
			    "public class T6419sl10_3 extends T6419sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl10_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl10_1.java",
			    "\n" +
			    "public team class Team6419sl10_1 {\n" +
			    "    public class Role6419sl10_1 extends T6419sl10_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl10_1.this.toString() + \".Role6419sl10_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl10_2 extends Role6419sl10_1 playedBy T6419sl10_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl10_1.this.toString() + \".Role6419sl10_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl10_3 extends Role6419sl10_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl10_1.this.toString() + \".Role6419sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl10_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl10_4.java",
			    "\n" +
			    "public class T6419sl10_4 extends T6419sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-11
    public void test6419_smartLifting11() {
        runNegativeTest(
            new String[] {
		"Team6419sl11_4.java",
			    "\n" +
			    "public team class Team6419sl11_4 extends Team6419sl11_3 {\n" +
			    "    public class Role6419sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl11_4.this.toString() + \".Role6419sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl11Main.java",
			    "\n" +
			    "public class T6419sl11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl11_2 t = new Team6419sl11_3();\n" +
			    "        T6419sl11_1    o = new T6419sl11_5();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl11_1.java",
			    "\n" +
			    "public team class Team6419sl11_1 {\n" +
			    "    public class Role6419sl11_1 extends T6419sl11_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl11_1.this.toString() + \".Role6419sl11_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl11_2 extends Role6419sl11_1 playedBy T6419sl11_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl11_1.this.toString() + \".Role6419sl11_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl11_3 extends Role6419sl11_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl11_1.this.toString() + \".Role6419sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl11_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl11_4.java",
			    "\n" +
			    "public class T6419sl11_4 extends T6419sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl11_2.java",
			    "\n" +
			    "public team class Team6419sl11_2 extends Team6419sl11_1 {\n" +
			    "    public class Role6419sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl11_2.this.toString() + \".Role6419sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl11_4 extends Role6419sl11_3 playedBy T6419sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl11_2.this.toString() + \".Role6419sl11_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl11_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl11_1 as Role6419sl11_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl11_5.java",
			    "\n" +
			    "public class T6419sl11_5 extends T6419sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl11_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl11_1.java",
			    "\n" +
			    "public class T6419sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl11_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl11_3.java",
			    "\n" +
			    "public team class Team6419sl11_3 extends Team6419sl11_2 {\n" +
			    "    public class Role6419sl11_5 extends Role6419sl11_3 playedBy T6419sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl11_3.this.toString() + \".Role6419sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl11_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl11_6.java",
			    "\n" +
			    "public class T6419sl11_6 extends T6419sl11_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl11_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl11_2.java",
			    "\n" +
			    "public abstract class T6419sl11_2 extends T6419sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl11_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl11_3.java",
			    "\n" +
			    "public class T6419sl11_3 extends T6419sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl11_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-12
    public void test6419_smartLifting12() {
        runNegativeTest(
            new String[] {
		"Team6419sl12_4.java",
			    "\n" +
			    "public team class Team6419sl12_4 extends Team6419sl12_3 {\n" +
			    "    public class Role6419sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl12_4.this.toString() + \".Role6419sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl12Main.java",
			    "\n" +
			    "public class T6419sl12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl12_2 t = new Team6419sl12_4();\n" +
			    "        T6419sl12_1    o = new T6419sl12_5();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl12_3.java",
			    "\n" +
			    "public class T6419sl12_3 extends T6419sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl12_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl12_1.java",
			    "\n" +
			    "public team class Team6419sl12_1 {\n" +
			    "    public class Role6419sl12_1 extends T6419sl12_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl12_1.this.toString() + \".Role6419sl12_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl12_2 extends Role6419sl12_1 playedBy T6419sl12_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl12_1.this.toString() + \".Role6419sl12_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl12_3 extends Role6419sl12_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl12_1.this.toString() + \".Role6419sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl12_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl12_4.java",
			    "\n" +
			    "public class T6419sl12_4 extends T6419sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl12_2.java",
			    "\n" +
			    "public team class Team6419sl12_2 extends Team6419sl12_1 {\n" +
			    "    public class Role6419sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl12_2.this.toString() + \".Role6419sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl12_4 extends Role6419sl12_3 playedBy T6419sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl12_2.this.toString() + \".Role6419sl12_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl12_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl12_1 as Role6419sl12_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl12_5.java",
			    "\n" +
			    "public class T6419sl12_5 extends T6419sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl12_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl12_1.java",
			    "\n" +
			    "public class T6419sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl12_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl12_3.java",
			    "\n" +
			    "public team class Team6419sl12_3 extends Team6419sl12_2 {\n" +
			    "    public class Role6419sl12_5 extends Role6419sl12_3 playedBy T6419sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl12_3.this.toString() + \".Role6419sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl12_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl12_6.java",
			    "\n" +
			    "public class T6419sl12_6 extends T6419sl12_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl12_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl12_2.java",
			    "\n" +
			    "public abstract class T6419sl12_2 extends T6419sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl12_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-13
    public void test6419_smartLifting13() {
        runNegativeTest(
            new String[] {
		"Team6419sl13_4.java",
			    "\n" +
			    "public team class Team6419sl13_4 extends Team6419sl13_3 {\n" +
			    "    public class Role6419sl13_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl13_4.this.toString() + \".Role6419sl13_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl13_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl13Main.java",
			    "\n" +
			    "public class T6419sl13Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl13_2 t = new Team6419sl13_2();\n" +
			    "        T6419sl13_1    o = new T6419sl13_6();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl13_3.java",
			    "\n" +
			    "public team class Team6419sl13_3 extends Team6419sl13_2 {\n" +
			    "    public class Role6419sl13_5 extends Role6419sl13_3 playedBy T6419sl13_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl13_3.this.toString() + \".Role6419sl13_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl13_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl13_6.java",
			    "\n" +
			    "public class T6419sl13_6 extends T6419sl13_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl13_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl13_2.java",
			    "\n" +
			    "public abstract class T6419sl13_2 extends T6419sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl13_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl13_3.java",
			    "\n" +
			    "public class T6419sl13_3 extends T6419sl13_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl13_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl13_1.java",
			    "\n" +
			    "public team class Team6419sl13_1 {\n" +
			    "    public class Role6419sl13_1 extends T6419sl13_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl13_1.this.toString() + \".Role6419sl13_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl13_2 extends Role6419sl13_1 playedBy T6419sl13_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl13_1.this.toString() + \".Role6419sl13_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl13_3 extends Role6419sl13_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl13_1.this.toString() + \".Role6419sl13_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl13_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl13_4.java",
			    "\n" +
			    "public class T6419sl13_4 extends T6419sl13_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl13_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl13_2.java",
			    "\n" +
			    "public team class Team6419sl13_2 extends Team6419sl13_1 {\n" +
			    "    public class Role6419sl13_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl13_2.this.toString() + \".Role6419sl13_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl13_4 extends Role6419sl13_3 playedBy T6419sl13_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl13_2.this.toString() + \".Role6419sl13_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl13_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl13_1 as Role6419sl13_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl13_5.java",
			    "\n" +
			    "public class T6419sl13_5 extends T6419sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl13_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl13_1.java",
			    "\n" +
			    "public class T6419sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl13_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-14
    public void test6419_smartLifting14() {
        runNegativeTest(
            new String[] {
		"Team6419sl14_4.java",
			    "\n" +
			    "public team class Team6419sl14_4 extends Team6419sl14_3 {\n" +
			    "    public class Role6419sl14_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl14_4.this.toString() + \".Role6419sl14_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl14_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl14Main.java",
			    "\n" +
			    "public class T6419sl14Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl14_2 t = new Team6419sl14_3();\n" +
			    "        T6419sl14_1    o = new T6419sl14_6();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl14_2.java",
			    "\n" +
			    "public team class Team6419sl14_2 extends Team6419sl14_1 {\n" +
			    "    public class Role6419sl14_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl14_2.this.toString() + \".Role6419sl14_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl14_4 extends Role6419sl14_3 playedBy T6419sl14_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl14_2.this.toString() + \".Role6419sl14_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl14_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl14_1 as Role6419sl14_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl14_5.java",
			    "\n" +
			    "public class T6419sl14_5 extends T6419sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl14_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl14_1.java",
			    "\n" +
			    "public class T6419sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl14_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl14_3.java",
			    "\n" +
			    "public team class Team6419sl14_3 extends Team6419sl14_2 {\n" +
			    "    public class Role6419sl14_5 extends Role6419sl14_3 playedBy T6419sl14_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl14_3.this.toString() + \".Role6419sl14_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl14_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl14_6.java",
			    "\n" +
			    "public class T6419sl14_6 extends T6419sl14_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl14_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl14_2.java",
			    "\n" +
			    "public abstract class T6419sl14_2 extends T6419sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl14_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl14_3.java",
			    "\n" +
			    "public class T6419sl14_3 extends T6419sl14_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl14_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl14_1.java",
			    "\n" +
			    "public team class Team6419sl14_1 {\n" +
			    "    public class Role6419sl14_1 extends T6419sl14_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl14_1.this.toString() + \".Role6419sl14_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl14_2 extends Role6419sl14_1 playedBy T6419sl14_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl14_1.this.toString() + \".Role6419sl14_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl14_3 extends Role6419sl14_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl14_1.this.toString() + \".Role6419sl14_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl14_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl14_4.java",
			    "\n" +
			    "public class T6419sl14_4 extends T6419sl14_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl14_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-15
    public void test6419_smartLifting15() {
        runNegativeTest(
            new String[] {
		"Team6419sl15_4.java",
			    "\n" +
			    "public team class Team6419sl15_4 extends Team6419sl15_3 {\n" +
			    "    public class Role6419sl15_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl15_4.this.toString() + \".Role6419sl15_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl15_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl15Main.java",
			    "\n" +
			    "public class T6419sl15Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl15_2 t = new Team6419sl15_4();\n" +
			    "        T6419sl15_1    o = new T6419sl15_6();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl15_1.java",
			    "\n" +
			    "public team class Team6419sl15_1 {\n" +
			    "    public class Role6419sl15_1 extends T6419sl15_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl15_1.this.toString() + \".Role6419sl15_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl15_2 extends Role6419sl15_1 playedBy T6419sl15_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl15_1.this.toString() + \".Role6419sl15_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl15_3 extends Role6419sl15_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl15_1.this.toString() + \".Role6419sl15_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl15_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl15_4.java",
			    "\n" +
			    "public class T6419sl15_4 extends T6419sl15_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl15_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl15_2.java",
			    "\n" +
			    "public team class Team6419sl15_2 extends Team6419sl15_1 {\n" +
			    "    public class Role6419sl15_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl15_2.this.toString() + \".Role6419sl15_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl15_4 extends Role6419sl15_3 playedBy T6419sl15_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl15_2.this.toString() + \".Role6419sl15_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl15_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl15_1 as Role6419sl15_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl15_5.java",
			    "\n" +
			    "public class T6419sl15_5 extends T6419sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl15_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl15_1.java",
			    "\n" +
			    "public class T6419sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl15_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl15_3.java",
			    "\n" +
			    "public team class Team6419sl15_3 extends Team6419sl15_2 {\n" +
			    "    public class Role6419sl15_5 extends Role6419sl15_3 playedBy T6419sl15_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl15_3.this.toString() + \".Role6419sl15_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl15_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl15_6.java",
			    "\n" +
			    "public class T6419sl15_6 extends T6419sl15_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl15_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl15_2.java",
			    "\n" +
			    "public abstract class T6419sl15_2 extends T6419sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl15_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl15_3.java",
			    "\n" +
			    "public class T6419sl15_3 extends T6419sl15_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl15_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-16
    public void test6419_smartLifting16() {
        runNegativeTest(
            new String[] {
		"Team6419sl16_4.java",
			    "\n" +
			    "public team class Team6419sl16_4 extends Team6419sl16_3 {\n" +
			    "    public class Role6419sl16_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl16_4.this.toString() + \".Role6419sl16_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl16_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl16Main.java",
			    "\n" +
			    "public class T6419sl16Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl16_2       t  = new Team6419sl16_2();\n" +
			    "        final Team6419sl16_1 ft = new Team6419sl16_1();\n" +
			    "        T6419sl16_1          o  = ft.new Role6419sl16_1();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl16_3.java",
			    "\n" +
			    "public class T6419sl16_3 extends T6419sl16_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl16_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl16_1.java",
			    "\n" +
			    "public team class Team6419sl16_1 {\n" +
			    "    public class Role6419sl16_1 extends T6419sl16_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl16_1.this.toString() + \".Role6419sl16_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl16_2 extends Role6419sl16_1 playedBy T6419sl16_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl16_1.this.toString() + \".Role6419sl16_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl16_3 extends Role6419sl16_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl16_1.this.toString() + \".Role6419sl16_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl16_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl16_4.java",
			    "\n" +
			    "public class T6419sl16_4 extends T6419sl16_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl16_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl16_2.java",
			    "\n" +
			    "public team class Team6419sl16_2 extends Team6419sl16_1 {\n" +
			    "    public class Role6419sl16_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl16_2.this.toString() + \".Role6419sl16_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl16_4 extends Role6419sl16_3 playedBy T6419sl16_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl16_2.this.toString() + \".Role6419sl16_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl16_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl16_1 as Role6419sl16_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl16_5.java",
			    "\n" +
			    "public class T6419sl16_5 extends T6419sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl16_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl16_1.java",
			    "\n" +
			    "public class T6419sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl16_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl16_3.java",
			    "\n" +
			    "public team class Team6419sl16_3 extends Team6419sl16_2 {\n" +
			    "    public class Role6419sl16_5 extends Role6419sl16_3 playedBy T6419sl16_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl16_3.this.toString() + \".Role6419sl16_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl16_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl16_6.java",
			    "\n" +
			    "public class T6419sl16_6 extends T6419sl16_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl16_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl16_2.java",
			    "\n" +
			    "public abstract class T6419sl16_2 extends T6419sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl16_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-17
    public void test6419_smartLifting17() {
        runNegativeTest(
            new String[] {
		"Team6419sl17_4.java",
			    "\n" +
			    "public team class Team6419sl17_4 extends Team6419sl17_3 {\n" +
			    "    public class Role6419sl17_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl17_4.this.toString() + \".Role6419sl17_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl17_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl17Main.java",
			    "\n" +
			    "public class T6419sl17Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl17_2       t  = new Team6419sl17_3();\n" +
			    "        final Team6419sl17_1 ft = new Team6419sl17_1();\n" +
			    "        T6419sl17_1          o  = ft.new Role6419sl17_1();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl17_3.java",
			    "\n" +
			    "public team class Team6419sl17_3 extends Team6419sl17_2 {\n" +
			    "    public class Role6419sl17_5 extends Role6419sl17_3 playedBy T6419sl17_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl17_3.this.toString() + \".Role6419sl17_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl17_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl17_6.java",
			    "\n" +
			    "public class T6419sl17_6 extends T6419sl17_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl17_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl17_2.java",
			    "\n" +
			    "public abstract class T6419sl17_2 extends T6419sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl17_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl17_3.java",
			    "\n" +
			    "public class T6419sl17_3 extends T6419sl17_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl17_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl17_1.java",
			    "\n" +
			    "public team class Team6419sl17_1 {\n" +
			    "    public class Role6419sl17_1 extends T6419sl17_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl17_1.this.toString() + \".Role6419sl17_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl17_2 extends Role6419sl17_1 playedBy T6419sl17_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl17_1.this.toString() + \".Role6419sl17_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl17_3 extends Role6419sl17_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl17_1.this.toString() + \".Role6419sl17_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl17_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl17_4.java",
			    "\n" +
			    "public class T6419sl17_4 extends T6419sl17_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl17_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl17_2.java",
			    "\n" +
			    "public team class Team6419sl17_2 extends Team6419sl17_1 {\n" +
			    "    public class Role6419sl17_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl17_2.this.toString() + \".Role6419sl17_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl17_4 extends Role6419sl17_3 playedBy T6419sl17_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl17_2.this.toString() + \".Role6419sl17_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl17_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl17_1 as Role6419sl17_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl17_5.java",
			    "\n" +
			    "public class T6419sl17_5 extends T6419sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl17_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl17_1.java",
			    "\n" +
			    "public class T6419sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl17_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.19-otjld-smart-lifting-18
    public void test6419_smartLifting18() {
        runNegativeTest(
            new String[] {
		"Team6419sl18_4.java",
			    "\n" +
			    "public team class Team6419sl18_4 extends Team6419sl18_3 {\n" +
			    "    public class Role6419sl18_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl18_4.this.toString() + \".Role6419sl18_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl18_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl18Main.java",
			    "\n" +
			    "public class T6419sl18Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6419sl18_2       t  = new Team6419sl18_4();\n" +
			    "        final Team6419sl18_1 ft = new Team6419sl18_1();\n" +
			    "        T6419sl18_1          o  = ft.new Role6419sl18_1();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl18_2.java",
			    "\n" +
			    "public team class Team6419sl18_2 extends Team6419sl18_1 {\n" +
			    "    public class Role6419sl18_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl18_2.this.toString() + \".Role6419sl18_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6419sl18_4 extends Role6419sl18_3 playedBy T6419sl18_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl18_2.this.toString() + \".Role6419sl18_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl18_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6419sl18_1 as Role6419sl18_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl18_5.java",
			    "\n" +
			    "public class T6419sl18_5 extends T6419sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl18_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl18_1.java",
			    "\n" +
			    "public class T6419sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl18_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl18_3.java",
			    "\n" +
			    "public team class Team6419sl18_3 extends Team6419sl18_2 {\n" +
			    "    public class Role6419sl18_5 extends Role6419sl18_3 playedBy T6419sl18_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl18_3.this.toString() + \".Role6419sl18_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl18_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl18_6.java",
			    "\n" +
			    "public class T6419sl18_6 extends T6419sl18_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl18_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl18_2.java",
			    "\n" +
			    "public abstract class T6419sl18_2 extends T6419sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl18_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6419sl18_3.java",
			    "\n" +
			    "public class T6419sl18_3 extends T6419sl18_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl18_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6419sl18_1.java",
			    "\n" +
			    "public team class Team6419sl18_1 {\n" +
			    "    public class Role6419sl18_1 extends T6419sl18_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl18_1.this.toString() + \".Role6419sl18_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6419sl18_2 extends Role6419sl18_1 playedBy T6419sl18_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl18_1.this.toString() + \".Role6419sl18_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6419sl18_3 extends Role6419sl18_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6419sl18_1.this.toString() + \".Role6419sl18_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6419sl18_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6419sl18_4.java",
			    "\n" +
			    "public class T6419sl18_4 extends T6419sl18_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6419sl18_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }
}
