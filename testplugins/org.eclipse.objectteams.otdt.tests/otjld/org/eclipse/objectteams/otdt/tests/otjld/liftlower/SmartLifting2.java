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

public class SmartLifting2 extends AbstractOTJLDTest {

	public SmartLifting2(String name) {
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
		return SmartLifting2.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.2-otjld-smart-lifting-1
    public void test642_smartLifting1() {

       runConformTest(
            new String[] {
		"T642sl1Main.java",
			    "\n" +
			    "public class T642sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team642sl1_1 t = new Team642sl1_1();\n" +
			    "        T642sl1_2    o = new T642sl1_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl1_4.java",
			    "\n" +
			    "public team class Team642sl1_4 extends Team642sl1_3 {\n" +
			    "    public class Role642sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl1_4.this.toString() + \".Role642sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl1_2.java",
			    "\n" +
			    "public team class Team642sl1_2 extends Team642sl1_1 {\n" +
			    "    public class Role642sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl1_2.this.toString() + \".Role642sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role642sl1_4 extends Role642sl1_3 playedBy T642sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl1_2.this.toString() + \".Role642sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl1_5.java",
			    "\n" +
			    "public class T642sl1_5 extends T642sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl1_1.java",
			    "\n" +
			    "public class T642sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl1_3.java",
			    "\n" +
			    "public team class Team642sl1_3 extends Team642sl1_2 {\n" +
			    "    public class Role642sl1_5 extends Role642sl1_3 playedBy T642sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl1_3.this.toString() + \".Role642sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl1_6.java",
			    "\n" +
			    "public class T642sl1_6 extends T642sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl1_2.java",
			    "\n" +
			    "public abstract class T642sl1_2 extends T642sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl1_3.java",
			    "\n" +
			    "public class T642sl1_3 extends T642sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl1_1.java",
			    "\n" +
			    "public team class Team642sl1_1 {\n" +
			    "    public class Role642sl1_1 extends T642sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl1_1.this.toString() + \".Role642sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role642sl1_2 extends Role642sl1_1 playedBy T642sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl1_1.this.toString() + \".Role642sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role642sl1_3 extends Role642sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl1_1.this.toString() + \".Role642sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T642sl1_2 as Role642sl1_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl1_4.java",
			    "\n" +
			    "public class T642sl1_4 extends T642sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team642sl1_1.Role642sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.2-otjld-smart-lifting-2
    public void test642_smartLifting2() {

       runConformTest(
            new String[] {
		"T642sl2Main.java",
			    "\n" +
			    "public class T642sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team642sl2_1 t = new Team642sl2_2();\n" +
			    "        T642sl2_2    o = new T642sl2_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl2_4.java",
			    "\n" +
			    "public team class Team642sl2_4 extends Team642sl2_3 {\n" +
			    "    public class Role642sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl2_4.this.toString() + \".Role642sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl2_1.java",
			    "\n" +
			    "public team class Team642sl2_1 {\n" +
			    "    public class Role642sl2_1 extends T642sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl2_1.this.toString() + \".Role642sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role642sl2_2 extends Role642sl2_1 playedBy T642sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl2_1.this.toString() + \".Role642sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role642sl2_3 extends Role642sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl2_1.this.toString() + \".Role642sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T642sl2_2 as Role642sl2_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl2_4.java",
			    "\n" +
			    "public class T642sl2_4 extends T642sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl2_2.java",
			    "\n" +
			    "public team class Team642sl2_2 extends Team642sl2_1 {\n" +
			    "    public class Role642sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl2_2.this.toString() + \".Role642sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role642sl2_4 extends Role642sl2_3 playedBy T642sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl2_2.this.toString() + \".Role642sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl2_5.java",
			    "\n" +
			    "public class T642sl2_5 extends T642sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl2_1.java",
			    "\n" +
			    "public class T642sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl2_3.java",
			    "\n" +
			    "public team class Team642sl2_3 extends Team642sl2_2 {\n" +
			    "    public class Role642sl2_5 extends Role642sl2_3 playedBy T642sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl2_3.this.toString() + \".Role642sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl2_6.java",
			    "\n" +
			    "public class T642sl2_6 extends T642sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl2_2.java",
			    "\n" +
			    "public abstract class T642sl2_2 extends T642sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl2_3.java",
			    "\n" +
			    "public class T642sl2_3 extends T642sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team642sl2_2.Role642sl2_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.2-otjld-smart-lifting-3
    public void test642_smartLifting3() {

       runConformTest(
            new String[] {
		"T642sl3Main.java",
			    "\n" +
			    "public class T642sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team642sl3_1 t = new Team642sl3_3();\n" +
			    "        T642sl3_2    o = new T642sl3_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl3_4.java",
			    "\n" +
			    "public team class Team642sl3_4 extends Team642sl3_3 {\n" +
			    "    public class Role642sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl3_4.this.toString() + \".Role642sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl3_3.java",
			    "\n" +
			    "public class T642sl3_3 extends T642sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl3_1.java",
			    "\n" +
			    "public team class Team642sl3_1 {\n" +
			    "    public class Role642sl3_1 extends T642sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl3_1.this.toString() + \".Role642sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role642sl3_2 extends Role642sl3_1 playedBy T642sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl3_1.this.toString() + \".Role642sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role642sl3_3 extends Role642sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl3_1.this.toString() + \".Role642sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T642sl3_2 as Role642sl3_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl3_4.java",
			    "\n" +
			    "public class T642sl3_4 extends T642sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl3_2.java",
			    "\n" +
			    "public team class Team642sl3_2 extends Team642sl3_1 {\n" +
			    "    public class Role642sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl3_2.this.toString() + \".Role642sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role642sl3_4 extends Role642sl3_3 playedBy T642sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl3_2.this.toString() + \".Role642sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl3_5.java",
			    "\n" +
			    "public class T642sl3_5 extends T642sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl3_1.java",
			    "\n" +
			    "public class T642sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl3_3.java",
			    "\n" +
			    "public team class Team642sl3_3 extends Team642sl3_2 {\n" +
			    "    public class Role642sl3_5 extends Role642sl3_3 playedBy T642sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl3_3.this.toString() + \".Role642sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl3_6.java",
			    "\n" +
			    "public class T642sl3_6 extends T642sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl3_2.java",
			    "\n" +
			    "public abstract class T642sl3_2 extends T642sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team642sl3_3.Role642sl3_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.2-otjld-smart-lifting-4
    public void test642_smartLifting4() {

       runConformTest(
            new String[] {
		"T642sl4Main.java",
			    "\n" +
			    "public class T642sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team642sl4_1 t = new Team642sl4_4();\n" +
			    "        T642sl4_2    o = new T642sl4_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl4_4.java",
			    "\n" +
			    "public team class Team642sl4_4 extends Team642sl4_3 {\n" +
			    "    public class Role642sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl4_4.this.toString() + \".Role642sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl4_3.java",
			    "\n" +
			    "public team class Team642sl4_3 extends Team642sl4_2 {\n" +
			    "    public class Role642sl4_5 extends Role642sl4_3 playedBy T642sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl4_3.this.toString() + \".Role642sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl4_6.java",
			    "\n" +
			    "public class T642sl4_6 extends T642sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl4_2.java",
			    "\n" +
			    "public abstract class T642sl4_2 extends T642sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl4_3.java",
			    "\n" +
			    "public class T642sl4_3 extends T642sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl4_1.java",
			    "\n" +
			    "public team class Team642sl4_1 {\n" +
			    "    public class Role642sl4_1 extends T642sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl4_1.this.toString() + \".Role642sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role642sl4_2 extends Role642sl4_1 playedBy T642sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl4_1.this.toString() + \".Role642sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role642sl4_3 extends Role642sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl4_1.this.toString() + \".Role642sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T642sl4_2 as Role642sl4_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl4_4.java",
			    "\n" +
			    "public class T642sl4_4 extends T642sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl4_2.java",
			    "\n" +
			    "public team class Team642sl4_2 extends Team642sl4_1 {\n" +
			    "    public class Role642sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl4_2.this.toString() + \".Role642sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role642sl4_4 extends Role642sl4_3 playedBy T642sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl4_2.this.toString() + \".Role642sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl4_5.java",
			    "\n" +
			    "public class T642sl4_5 extends T642sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl4_1.java",
			    "\n" +
			    "public class T642sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team642sl4_4.Role642sl4_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.2-otjld-smart-lifting-5
    public void test642_smartLifting5() {

       runConformTest(
            new String[] {
		"T642sl5Main.java",
			    "\n" +
			    "public class T642sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team642sl5_1 t = new Team642sl5_1();\n" +
			    "        T642sl5_2    o = new T642sl5_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl5_4.java",
			    "\n" +
			    "public team class Team642sl5_4 extends Team642sl5_3 {\n" +
			    "    public class Role642sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl5_4.this.toString() + \".Role642sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl5_2.java",
			    "\n" +
			    "public team class Team642sl5_2 extends Team642sl5_1 {\n" +
			    "    public class Role642sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl5_2.this.toString() + \".Role642sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role642sl5_4 extends Role642sl5_3 playedBy T642sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl5_2.this.toString() + \".Role642sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl5_5.java",
			    "\n" +
			    "public class T642sl5_5 extends T642sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl5_1.java",
			    "\n" +
			    "public class T642sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl5_3.java",
			    "\n" +
			    "public team class Team642sl5_3 extends Team642sl5_2 {\n" +
			    "    public class Role642sl5_5 extends Role642sl5_3 playedBy T642sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl5_3.this.toString() + \".Role642sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl5_6.java",
			    "\n" +
			    "public class T642sl5_6 extends T642sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl5_2.java",
			    "\n" +
			    "public abstract class T642sl5_2 extends T642sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl5_3.java",
			    "\n" +
			    "public class T642sl5_3 extends T642sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl5_1.java",
			    "\n" +
			    "public team class Team642sl5_1 {\n" +
			    "    public class Role642sl5_1 extends T642sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl5_1.this.toString() + \".Role642sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role642sl5_2 extends Role642sl5_1 playedBy T642sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl5_1.this.toString() + \".Role642sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role642sl5_3 extends Role642sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl5_1.this.toString() + \".Role642sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T642sl5_2 as Role642sl5_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl5_4.java",
			    "\n" +
			    "public class T642sl5_4 extends T642sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team642sl5_1.Role642sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.2-otjld-smart-lifting-6
    public void test642_smartLifting6() {

       runConformTest(
            new String[] {
		"T642sl6Main.java",
			    "\n" +
			    "public class T642sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team642sl6_1 t = new Team642sl6_2();\n" +
			    "        T642sl6_2    o = new T642sl6_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl6_4.java",
			    "\n" +
			    "public team class Team642sl6_4 extends Team642sl6_3 {\n" +
			    "    public class Role642sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl6_4.this.toString() + \".Role642sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl6_1.java",
			    "\n" +
			    "public team class Team642sl6_1 {\n" +
			    "    public class Role642sl6_1 extends T642sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl6_1.this.toString() + \".Role642sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role642sl6_2 extends Role642sl6_1 playedBy T642sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl6_1.this.toString() + \".Role642sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role642sl6_3 extends Role642sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl6_1.this.toString() + \".Role642sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T642sl6_2 as Role642sl6_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl6_4.java",
			    "\n" +
			    "public class T642sl6_4 extends T642sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl6_2.java",
			    "\n" +
			    "public team class Team642sl6_2 extends Team642sl6_1 {\n" +
			    "    public class Role642sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl6_2.this.toString() + \".Role642sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role642sl6_4 extends Role642sl6_3 playedBy T642sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl6_2.this.toString() + \".Role642sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl6_5.java",
			    "\n" +
			    "public class T642sl6_5 extends T642sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl6_1.java",
			    "\n" +
			    "public class T642sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl6_3.java",
			    "\n" +
			    "public team class Team642sl6_3 extends Team642sl6_2 {\n" +
			    "    public class Role642sl6_5 extends Role642sl6_3 playedBy T642sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl6_3.this.toString() + \".Role642sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl6_6.java",
			    "\n" +
			    "public class T642sl6_6 extends T642sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl6_2.java",
			    "\n" +
			    "public abstract class T642sl6_2 extends T642sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl6_3.java",
			    "\n" +
			    "public class T642sl6_3 extends T642sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team642sl6_2.Role642sl6_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.2-otjld-smart-lifting-7
    public void test642_smartLifting7() {

       runConformTest(
            new String[] {
		"T642sl7Main.java",
			    "\n" +
			    "public class T642sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team642sl7_1 t = new Team642sl7_3();\n" +
			    "        T642sl7_2    o = new T642sl7_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl7_4.java",
			    "\n" +
			    "public team class Team642sl7_4 extends Team642sl7_3 {\n" +
			    "    public class Role642sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl7_4.this.toString() + \".Role642sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl7_3.java",
			    "\n" +
			    "public class T642sl7_3 extends T642sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl7_1.java",
			    "\n" +
			    "public team class Team642sl7_1 {\n" +
			    "    public class Role642sl7_1 extends T642sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl7_1.this.toString() + \".Role642sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role642sl7_2 extends Role642sl7_1 playedBy T642sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl7_1.this.toString() + \".Role642sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role642sl7_3 extends Role642sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl7_1.this.toString() + \".Role642sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T642sl7_2 as Role642sl7_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl7_4.java",
			    "\n" +
			    "public class T642sl7_4 extends T642sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl7_2.java",
			    "\n" +
			    "public team class Team642sl7_2 extends Team642sl7_1 {\n" +
			    "    public class Role642sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl7_2.this.toString() + \".Role642sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role642sl7_4 extends Role642sl7_3 playedBy T642sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl7_2.this.toString() + \".Role642sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl7_5.java",
			    "\n" +
			    "public class T642sl7_5 extends T642sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl7_1.java",
			    "\n" +
			    "public class T642sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl7_3.java",
			    "\n" +
			    "public team class Team642sl7_3 extends Team642sl7_2 {\n" +
			    "    public class Role642sl7_5 extends Role642sl7_3 playedBy T642sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl7_3.this.toString() + \".Role642sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl7_6.java",
			    "\n" +
			    "public class T642sl7_6 extends T642sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl7_2.java",
			    "\n" +
			    "public abstract class T642sl7_2 extends T642sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team642sl7_3.Role642sl7_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.2-otjld-smart-lifting-8
    public void test642_smartLifting8() {

       runConformTest(
            new String[] {
		"T642sl8Main.java",
			    "\n" +
			    "public class T642sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team642sl8_1 t = new Team642sl8_4();\n" +
			    "        T642sl8_2    o = new T642sl8_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl8_4.java",
			    "\n" +
			    "public team class Team642sl8_4 extends Team642sl8_3 {\n" +
			    "    public class Role642sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl8_4.this.toString() + \".Role642sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl8_3.java",
			    "\n" +
			    "public team class Team642sl8_3 extends Team642sl8_2 {\n" +
			    "    public class Role642sl8_5 extends Role642sl8_3 playedBy T642sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl8_3.this.toString() + \".Role642sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl8_6.java",
			    "\n" +
			    "public class T642sl8_6 extends T642sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl8_2.java",
			    "\n" +
			    "public abstract class T642sl8_2 extends T642sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl8_3.java",
			    "\n" +
			    "public class T642sl8_3 extends T642sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl8_1.java",
			    "\n" +
			    "public team class Team642sl8_1 {\n" +
			    "    public class Role642sl8_1 extends T642sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl8_1.this.toString() + \".Role642sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role642sl8_2 extends Role642sl8_1 playedBy T642sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl8_1.this.toString() + \".Role642sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role642sl8_3 extends Role642sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl8_1.this.toString() + \".Role642sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T642sl8_2 as Role642sl8_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl8_4.java",
			    "\n" +
			    "public class T642sl8_4 extends T642sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team642sl8_2.java",
			    "\n" +
			    "public team class Team642sl8_2 extends Team642sl8_1 {\n" +
			    "    public class Role642sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl8_2.this.toString() + \".Role642sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role642sl8_4 extends Role642sl8_3 playedBy T642sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team642sl8_2.this.toString() + \".Role642sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team642sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T642sl8_5.java",
			    "\n" +
			    "public class T642sl8_5 extends T642sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T642sl8_1.java",
			    "\n" +
			    "public class T642sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T642sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team642sl8_4.Role642sl8_3");
    }
}
