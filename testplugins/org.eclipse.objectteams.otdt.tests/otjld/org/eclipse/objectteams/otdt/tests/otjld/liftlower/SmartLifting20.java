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

public class SmartLifting20 extends AbstractOTJLDTest {
	
	public SmartLifting20(String name) {
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
		return SmartLifting20.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.20-otjld-smart-lifting-1
    public void test6420_smartLifting1() {
        runNegativeTest(
            new String[] {
		"Team6420sl1_4.java",
			    "\n" +
			    "public team class Team6420sl1_4 extends Team6420sl1_3 {\n" +
			    "    public class Role6420sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl1_4.this.toString() + \".Role6420sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl1Main.java",
			    "\n" +
			    "public class T6420sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6420sl1_2 t = new Team6420sl1_2();\n" +
			    "        T6420sl1_2    o = new T6420sl1_3();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl1_2.java",
			    "\n" +
			    "public team class Team6420sl1_2 extends Team6420sl1_1 {\n" +
			    "    public class Role6420sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl1_2.this.toString() + \".Role6420sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6420sl1_4 extends Role6420sl1_3 playedBy T6420sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl1_2.this.toString() + \".Role6420sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6420sl1_2 as Role6420sl1_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl1_5.java",
			    "\n" +
			    "public class T6420sl1_5 extends T6420sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl1_1.java",
			    "\n" +
			    "public class T6420sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl1_3.java",
			    "\n" +
			    "public team class Team6420sl1_3 extends Team6420sl1_2 {\n" +
			    "    public class Role6420sl1_5 extends Role6420sl1_3 playedBy T6420sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl1_3.this.toString() + \".Role6420sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl1_6.java",
			    "\n" +
			    "public class T6420sl1_6 extends T6420sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl1_2.java",
			    "\n" +
			    "public abstract class T6420sl1_2 extends T6420sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl1_3.java",
			    "\n" +
			    "public class T6420sl1_3 extends T6420sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl1_1.java",
			    "\n" +
			    "public team class Team6420sl1_1 {\n" +
			    "    public class Role6420sl1_1 extends T6420sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl1_1.this.toString() + \".Role6420sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6420sl1_2 extends Role6420sl1_1 playedBy T6420sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl1_1.this.toString() + \".Role6420sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6420sl1_3 extends Role6420sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl1_1.this.toString() + \".Role6420sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl1_4.java",
			    "\n" +
			    "public class T6420sl1_4 extends T6420sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.20-otjld-smart-lifting-2
    public void test6420_smartLifting2() {
        runNegativeTest(
            new String[] {
		"Team6420sl2_4.java",
			    "\n" +
			    "public team class Team6420sl2_4 extends Team6420sl2_3 {\n" +
			    "    public class Role6420sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl2_4.this.toString() + \".Role6420sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl2Main.java",
			    "\n" +
			    "public class T6420sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6420sl2_2 t = new Team6420sl2_3();\n" +
			    "        T6420sl2_2    o = new T6420sl2_3();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl2_1.java",
			    "\n" +
			    "public team class Team6420sl2_1 {\n" +
			    "    public class Role6420sl2_1 extends T6420sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl2_1.this.toString() + \".Role6420sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6420sl2_2 extends Role6420sl2_1 playedBy T6420sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl2_1.this.toString() + \".Role6420sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6420sl2_3 extends Role6420sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl2_1.this.toString() + \".Role6420sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl2_4.java",
			    "\n" +
			    "public class T6420sl2_4 extends T6420sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl2_2.java",
			    "\n" +
			    "public team class Team6420sl2_2 extends Team6420sl2_1 {\n" +
			    "    public class Role6420sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl2_2.this.toString() + \".Role6420sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6420sl2_4 extends Role6420sl2_3 playedBy T6420sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl2_2.this.toString() + \".Role6420sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6420sl2_2 as Role6420sl2_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl2_5.java",
			    "\n" +
			    "public class T6420sl2_5 extends T6420sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl2_1.java",
			    "\n" +
			    "public class T6420sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl2_3.java",
			    "\n" +
			    "public team class Team6420sl2_3 extends Team6420sl2_2 {\n" +
			    "    public class Role6420sl2_5 extends Role6420sl2_3 playedBy T6420sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl2_3.this.toString() + \".Role6420sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl2_6.java",
			    "\n" +
			    "public class T6420sl2_6 extends T6420sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl2_2.java",
			    "\n" +
			    "public abstract class T6420sl2_2 extends T6420sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl2_3.java",
			    "\n" +
			    "public class T6420sl2_3 extends T6420sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.20-otjld-smart-lifting-3
    public void test6420_smartLifting3() {
        runNegativeTest(
            new String[] {
		"Team6420sl3_4.java",
			    "\n" +
			    "public team class Team6420sl3_4 extends Team6420sl3_3 {\n" +
			    "    public class Role6420sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl3_4.this.toString() + \".Role6420sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl3Main.java",
			    "\n" +
			    "public class T6420sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6420sl3_2 t = new Team6420sl3_4();\n" +
			    "        T6420sl3_2    o = new T6420sl3_3();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl3_3.java",
			    "\n" +
			    "public class T6420sl3_3 extends T6420sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl3_1.java",
			    "\n" +
			    "public team class Team6420sl3_1 {\n" +
			    "    public class Role6420sl3_1 extends T6420sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl3_1.this.toString() + \".Role6420sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6420sl3_2 extends Role6420sl3_1 playedBy T6420sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl3_1.this.toString() + \".Role6420sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6420sl3_3 extends Role6420sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl3_1.this.toString() + \".Role6420sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl3_4.java",
			    "\n" +
			    "public class T6420sl3_4 extends T6420sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl3_2.java",
			    "\n" +
			    "public team class Team6420sl3_2 extends Team6420sl3_1 {\n" +
			    "    public class Role6420sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl3_2.this.toString() + \".Role6420sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6420sl3_4 extends Role6420sl3_3 playedBy T6420sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl3_2.this.toString() + \".Role6420sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6420sl3_2 as Role6420sl3_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl3_5.java",
			    "\n" +
			    "public class T6420sl3_5 extends T6420sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl3_1.java",
			    "\n" +
			    "public class T6420sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl3_3.java",
			    "\n" +
			    "public team class Team6420sl3_3 extends Team6420sl3_2 {\n" +
			    "    public class Role6420sl3_5 extends Role6420sl3_3 playedBy T6420sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl3_3.this.toString() + \".Role6420sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl3_6.java",
			    "\n" +
			    "public class T6420sl3_6 extends T6420sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl3_2.java",
			    "\n" +
			    "public abstract class T6420sl3_2 extends T6420sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.20-otjld-smart-lifting-4
    public void test6420_smartLifting4() {
        runNegativeTest(
            new String[] {
		"Team6420sl4_4.java",
			    "\n" +
			    "public team class Team6420sl4_4 extends Team6420sl4_3 {\n" +
			    "    public class Role6420sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl4_4.this.toString() + \".Role6420sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl4Main.java",
			    "\n" +
			    "public class T6420sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6420sl4_2 t = new Team6420sl4_2();\n" +
			    "        T6420sl4_2    o = new T6420sl4_4();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl4_3.java",
			    "\n" +
			    "public team class Team6420sl4_3 extends Team6420sl4_2 {\n" +
			    "    public class Role6420sl4_5 extends Role6420sl4_3 playedBy T6420sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl4_3.this.toString() + \".Role6420sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl4_6.java",
			    "\n" +
			    "public class T6420sl4_6 extends T6420sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl4_2.java",
			    "\n" +
			    "public abstract class T6420sl4_2 extends T6420sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl4_3.java",
			    "\n" +
			    "public class T6420sl4_3 extends T6420sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl4_1.java",
			    "\n" +
			    "public team class Team6420sl4_1 {\n" +
			    "    public class Role6420sl4_1 extends T6420sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl4_1.this.toString() + \".Role6420sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6420sl4_2 extends Role6420sl4_1 playedBy T6420sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl4_1.this.toString() + \".Role6420sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6420sl4_3 extends Role6420sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl4_1.this.toString() + \".Role6420sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl4_4.java",
			    "\n" +
			    "public class T6420sl4_4 extends T6420sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl4_2.java",
			    "\n" +
			    "public team class Team6420sl4_2 extends Team6420sl4_1 {\n" +
			    "    public class Role6420sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl4_2.this.toString() + \".Role6420sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6420sl4_4 extends Role6420sl4_3 playedBy T6420sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl4_2.this.toString() + \".Role6420sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6420sl4_2 as Role6420sl4_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl4_5.java",
			    "\n" +
			    "public class T6420sl4_5 extends T6420sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl4_1.java",
			    "\n" +
			    "public class T6420sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.20-otjld-smart-lifting-5
    public void test6420_smartLifting5() {
        runNegativeTest(
            new String[] {
		"Team6420sl5_4.java",
			    "\n" +
			    "public team class Team6420sl5_4 extends Team6420sl5_3 {\n" +
			    "    public class Role6420sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl5_4.this.toString() + \".Role6420sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl5Main.java",
			    "\n" +
			    "public class T6420sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6420sl5_2 t = new Team6420sl5_3();\n" +
			    "        T6420sl5_2    o = new T6420sl5_4();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl5_2.java",
			    "\n" +
			    "public team class Team6420sl5_2 extends Team6420sl5_1 {\n" +
			    "    public class Role6420sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl5_2.this.toString() + \".Role6420sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6420sl5_4 extends Role6420sl5_3 playedBy T6420sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl5_2.this.toString() + \".Role6420sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6420sl5_2 as Role6420sl5_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl5_5.java",
			    "\n" +
			    "public class T6420sl5_5 extends T6420sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl5_1.java",
			    "\n" +
			    "public class T6420sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl5_3.java",
			    "\n" +
			    "public team class Team6420sl5_3 extends Team6420sl5_2 {\n" +
			    "    public class Role6420sl5_5 extends Role6420sl5_3 playedBy T6420sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl5_3.this.toString() + \".Role6420sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl5_6.java",
			    "\n" +
			    "public class T6420sl5_6 extends T6420sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl5_2.java",
			    "\n" +
			    "public abstract class T6420sl5_2 extends T6420sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl5_3.java",
			    "\n" +
			    "public class T6420sl5_3 extends T6420sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl5_1.java",
			    "\n" +
			    "public team class Team6420sl5_1 {\n" +
			    "    public class Role6420sl5_1 extends T6420sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl5_1.this.toString() + \".Role6420sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6420sl5_2 extends Role6420sl5_1 playedBy T6420sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl5_1.this.toString() + \".Role6420sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6420sl5_3 extends Role6420sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl5_1.this.toString() + \".Role6420sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl5_4.java",
			    "\n" +
			    "public class T6420sl5_4 extends T6420sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.20-otjld-smart-lifting-6
    public void test6420_smartLifting6() {
        runNegativeTest(
            new String[] {
		"Team6420sl6_4.java",
			    "\n" +
			    "public team class Team6420sl6_4 extends Team6420sl6_3 {\n" +
			    "    public class Role6420sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl6_4.this.toString() + \".Role6420sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl6Main.java",
			    "\n" +
			    "public class T6420sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6420sl6_2 t = new Team6420sl6_4();\n" +
			    "        T6420sl6_2    o = new T6420sl6_4();\n" +
			    "\n" +
			    "        System.out.print(t.t2(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl6_1.java",
			    "\n" +
			    "public team class Team6420sl6_1 {\n" +
			    "    public class Role6420sl6_1 extends T6420sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl6_1.this.toString() + \".Role6420sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6420sl6_2 extends Role6420sl6_1 playedBy T6420sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl6_1.this.toString() + \".Role6420sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6420sl6_3 extends Role6420sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl6_1.this.toString() + \".Role6420sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl6_4.java",
			    "\n" +
			    "public class T6420sl6_4 extends T6420sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl6_2.java",
			    "\n" +
			    "public team class Team6420sl6_2 extends Team6420sl6_1 {\n" +
			    "    public class Role6420sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl6_2.this.toString() + \".Role6420sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6420sl6_4 extends Role6420sl6_3 playedBy T6420sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl6_2.this.toString() + \".Role6420sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T6420sl6_2 as Role6420sl6_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl6_5.java",
			    "\n" +
			    "public class T6420sl6_5 extends T6420sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl6_1.java",
			    "\n" +
			    "public class T6420sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6420sl6_3.java",
			    "\n" +
			    "public team class Team6420sl6_3 extends Team6420sl6_2 {\n" +
			    "    public class Role6420sl6_5 extends Role6420sl6_3 playedBy T6420sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6420sl6_3.this.toString() + \".Role6420sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6420sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6420sl6_6.java",
			    "\n" +
			    "public class T6420sl6_6 extends T6420sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl6_2.java",
			    "\n" +
			    "public abstract class T6420sl6_2 extends T6420sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6420sl6_3.java",
			    "\n" +
			    "public class T6420sl6_3 extends T6420sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6420sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }
}
