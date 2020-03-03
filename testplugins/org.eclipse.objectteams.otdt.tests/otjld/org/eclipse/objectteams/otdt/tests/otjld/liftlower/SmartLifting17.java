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

public class SmartLifting17 extends AbstractOTJLDTest {
	
	public SmartLifting17(String name) {
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
		return SmartLifting17.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-1
    public void test6417_smartLifting1() {
       
       runConformTest(
            new String[] {
		"T6417sl1Main.java",
			    "\n" +
			    "public class T6417sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl1_1 t = new Team6417sl1_1();\n" +
			    "        T6417sl1_5    o = new T6417sl1_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl1_4.java",
			    "\n" +
			    "public team class Team6417sl1_4 extends Team6417sl1_3 {\n" +
			    "    public class Role6417sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl1_4.this.toString() + \".Role6417sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl1_3.java",
			    "\n" +
			    "public class T6417sl1_3 extends T6417sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl1_1.java",
			    "\n" +
			    "public team class Team6417sl1_1 {\n" +
			    "    public class Role6417sl1_1 extends T6417sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl1_1.this.toString() + \".Role6417sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl1_2 extends Role6417sl1_1 playedBy T6417sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl1_1.this.toString() + \".Role6417sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl1_3 extends Role6417sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl1_1.this.toString() + \".Role6417sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl1_5 as Role6417sl1_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl1_4.java",
			    "\n" +
			    "public class T6417sl1_4 extends T6417sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl1_2.java",
			    "\n" +
			    "public team class Team6417sl1_2 extends Team6417sl1_1 {\n" +
			    "    public class Role6417sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl1_2.this.toString() + \".Role6417sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl1_4 extends Role6417sl1_3 playedBy T6417sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl1_2.this.toString() + \".Role6417sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl1_5.java",
			    "\n" +
			    "public class T6417sl1_5 extends T6417sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl1_1.java",
			    "\n" +
			    "public class T6417sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl1_3.java",
			    "\n" +
			    "public team class Team6417sl1_3 extends Team6417sl1_2 {\n" +
			    "    public class Role6417sl1_5 extends Role6417sl1_3 playedBy T6417sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl1_3.this.toString() + \".Role6417sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl1_6.java",
			    "\n" +
			    "public class T6417sl1_6 extends T6417sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl1_2.java",
			    "\n" +
			    "public abstract class T6417sl1_2 extends T6417sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl1_1.Role6417sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-2
    public void test6417_smartLifting2() {
       
       runConformTest(
            new String[] {
		"T6417sl2Main.java",
			    "\n" +
			    "public class T6417sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl2_1 t = new Team6417sl2_2();\n" +
			    "        T6417sl2_5    o = new T6417sl2_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl2_4.java",
			    "\n" +
			    "public team class Team6417sl2_4 extends Team6417sl2_3 {\n" +
			    "    public class Role6417sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl2_4.this.toString() + \".Role6417sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl2_3.java",
			    "\n" +
			    "public team class Team6417sl2_3 extends Team6417sl2_2 {\n" +
			    "    public class Role6417sl2_5 extends Role6417sl2_3 playedBy T6417sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl2_3.this.toString() + \".Role6417sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl2_6.java",
			    "\n" +
			    "public class T6417sl2_6 extends T6417sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl2_2.java",
			    "\n" +
			    "public abstract class T6417sl2_2 extends T6417sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl2_3.java",
			    "\n" +
			    "public class T6417sl2_3 extends T6417sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl2_1.java",
			    "\n" +
			    "public team class Team6417sl2_1 {\n" +
			    "    public class Role6417sl2_1 extends T6417sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl2_1.this.toString() + \".Role6417sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl2_2 extends Role6417sl2_1 playedBy T6417sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl2_1.this.toString() + \".Role6417sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl2_3 extends Role6417sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl2_1.this.toString() + \".Role6417sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl2_5 as Role6417sl2_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl2_4.java",
			    "\n" +
			    "public class T6417sl2_4 extends T6417sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl2_2.java",
			    "\n" +
			    "public team class Team6417sl2_2 extends Team6417sl2_1 {\n" +
			    "    public class Role6417sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl2_2.this.toString() + \".Role6417sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl2_4 extends Role6417sl2_3 playedBy T6417sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl2_2.this.toString() + \".Role6417sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl2_5.java",
			    "\n" +
			    "public class T6417sl2_5 extends T6417sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl2_1.java",
			    "\n" +
			    "public class T6417sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl2_2.Role6417sl2_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-3
    public void test6417_smartLifting3() {
       
       runConformTest(
            new String[] {
		"T6417sl3Main.java",
			    "\n" +
			    "public class T6417sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl3_1 t = new Team6417sl3_3();\n" +
			    "        T6417sl3_5    o = new T6417sl3_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl3_4.java",
			    "\n" +
			    "public team class Team6417sl3_4 extends Team6417sl3_3 {\n" +
			    "    public class Role6417sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl3_4.this.toString() + \".Role6417sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl3_2.java",
			    "\n" +
			    "public team class Team6417sl3_2 extends Team6417sl3_1 {\n" +
			    "    public class Role6417sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl3_2.this.toString() + \".Role6417sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl3_4 extends Role6417sl3_3 playedBy T6417sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl3_2.this.toString() + \".Role6417sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl3_5.java",
			    "\n" +
			    "public class T6417sl3_5 extends T6417sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl3_1.java",
			    "\n" +
			    "public class T6417sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl3_3.java",
			    "\n" +
			    "public team class Team6417sl3_3 extends Team6417sl3_2 {\n" +
			    "    public class Role6417sl3_5 extends Role6417sl3_3 playedBy T6417sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl3_3.this.toString() + \".Role6417sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl3_6.java",
			    "\n" +
			    "public class T6417sl3_6 extends T6417sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl3_2.java",
			    "\n" +
			    "public abstract class T6417sl3_2 extends T6417sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl3_3.java",
			    "\n" +
			    "public class T6417sl3_3 extends T6417sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl3_1.java",
			    "\n" +
			    "public team class Team6417sl3_1 {\n" +
			    "    public class Role6417sl3_1 extends T6417sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl3_1.this.toString() + \".Role6417sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl3_2 extends Role6417sl3_1 playedBy T6417sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl3_1.this.toString() + \".Role6417sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl3_3 extends Role6417sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl3_1.this.toString() + \".Role6417sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl3_5 as Role6417sl3_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl3_4.java",
			    "\n" +
			    "public class T6417sl3_4 extends T6417sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl3_3.Role6417sl3_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-4
    public void test6417_smartLifting4() {
       
       runConformTest(
            new String[] {
		"T6417sl4Main.java",
			    "\n" +
			    "public class T6417sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl4_1 t = new Team6417sl4_4();\n" +
			    "        T6417sl4_5    o = new T6417sl4_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl4_4.java",
			    "\n" +
			    "public team class Team6417sl4_4 extends Team6417sl4_3 {\n" +
			    "    public class Role6417sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl4_4.this.toString() + \".Role6417sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl4_1.java",
			    "\n" +
			    "public team class Team6417sl4_1 {\n" +
			    "    public class Role6417sl4_1 extends T6417sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl4_1.this.toString() + \".Role6417sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl4_2 extends Role6417sl4_1 playedBy T6417sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl4_1.this.toString() + \".Role6417sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl4_3 extends Role6417sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl4_1.this.toString() + \".Role6417sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl4_5 as Role6417sl4_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl4_4.java",
			    "\n" +
			    "public class T6417sl4_4 extends T6417sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl4_2.java",
			    "\n" +
			    "public team class Team6417sl4_2 extends Team6417sl4_1 {\n" +
			    "    public class Role6417sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl4_2.this.toString() + \".Role6417sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl4_4 extends Role6417sl4_3 playedBy T6417sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl4_2.this.toString() + \".Role6417sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl4_5.java",
			    "\n" +
			    "public class T6417sl4_5 extends T6417sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl4_1.java",
			    "\n" +
			    "public class T6417sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl4_3.java",
			    "\n" +
			    "public team class Team6417sl4_3 extends Team6417sl4_2 {\n" +
			    "    public class Role6417sl4_5 extends Role6417sl4_3 playedBy T6417sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl4_3.this.toString() + \".Role6417sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl4_6.java",
			    "\n" +
			    "public class T6417sl4_6 extends T6417sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl4_2.java",
			    "\n" +
			    "public abstract class T6417sl4_2 extends T6417sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl4_3.java",
			    "\n" +
			    "public class T6417sl4_3 extends T6417sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl4_4.Role6417sl4_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-5
    public void test6417_smartLifting5() {
       
       runConformTest(
            new String[] {
		"T6417sl5Main.java",
			    "\n" +
			    "public class T6417sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl5_1 t = new Team6417sl5_1();\n" +
			    "        T6417sl5_5    o = new T6417sl5_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl5_4.java",
			    "\n" +
			    "public team class Team6417sl5_4 extends Team6417sl5_3 {\n" +
			    "    public class Role6417sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl5_4.this.toString() + \".Role6417sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl5_3.java",
			    "\n" +
			    "public class T6417sl5_3 extends T6417sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl5_1.java",
			    "\n" +
			    "public team class Team6417sl5_1 {\n" +
			    "    public class Role6417sl5_1 extends T6417sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl5_1.this.toString() + \".Role6417sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl5_2 extends Role6417sl5_1 playedBy T6417sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl5_1.this.toString() + \".Role6417sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl5_3 extends Role6417sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl5_1.this.toString() + \".Role6417sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl5_5 as Role6417sl5_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl5_4.java",
			    "\n" +
			    "public class T6417sl5_4 extends T6417sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl5_2.java",
			    "\n" +
			    "public team class Team6417sl5_2 extends Team6417sl5_1 {\n" +
			    "    public class Role6417sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl5_2.this.toString() + \".Role6417sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl5_4 extends Role6417sl5_3 playedBy T6417sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl5_2.this.toString() + \".Role6417sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl5_5.java",
			    "\n" +
			    "public class T6417sl5_5 extends T6417sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl5_1.java",
			    "\n" +
			    "public class T6417sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl5_3.java",
			    "\n" +
			    "public team class Team6417sl5_3 extends Team6417sl5_2 {\n" +
			    "    public class Role6417sl5_5 extends Role6417sl5_3 playedBy T6417sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl5_3.this.toString() + \".Role6417sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl5_6.java",
			    "\n" +
			    "public class T6417sl5_6 extends T6417sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl5_2.java",
			    "\n" +
			    "public abstract class T6417sl5_2 extends T6417sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl5_1.Role6417sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-6
    public void test6417_smartLifting6() {
       
       runConformTest(
            new String[] {
		"T6417sl6Main.java",
			    "\n" +
			    "public class T6417sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl6_1 t = new Team6417sl6_2();\n" +
			    "        T6417sl6_5    o = new T6417sl6_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl6_4.java",
			    "\n" +
			    "public team class Team6417sl6_4 extends Team6417sl6_3 {\n" +
			    "    public class Role6417sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl6_4.this.toString() + \".Role6417sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl6_3.java",
			    "\n" +
			    "public team class Team6417sl6_3 extends Team6417sl6_2 {\n" +
			    "    public class Role6417sl6_5 extends Role6417sl6_3 playedBy T6417sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl6_3.this.toString() + \".Role6417sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl6_6.java",
			    "\n" +
			    "public class T6417sl6_6 extends T6417sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl6_2.java",
			    "\n" +
			    "public abstract class T6417sl6_2 extends T6417sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl6_3.java",
			    "\n" +
			    "public class T6417sl6_3 extends T6417sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl6_1.java",
			    "\n" +
			    "public team class Team6417sl6_1 {\n" +
			    "    public class Role6417sl6_1 extends T6417sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl6_1.this.toString() + \".Role6417sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl6_2 extends Role6417sl6_1 playedBy T6417sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl6_1.this.toString() + \".Role6417sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl6_3 extends Role6417sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl6_1.this.toString() + \".Role6417sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl6_5 as Role6417sl6_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl6_4.java",
			    "\n" +
			    "public class T6417sl6_4 extends T6417sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl6_2.java",
			    "\n" +
			    "public team class Team6417sl6_2 extends Team6417sl6_1 {\n" +
			    "    public class Role6417sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl6_2.this.toString() + \".Role6417sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl6_4 extends Role6417sl6_3 playedBy T6417sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl6_2.this.toString() + \".Role6417sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl6_5.java",
			    "\n" +
			    "public class T6417sl6_5 extends T6417sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl6_1.java",
			    "\n" +
			    "public class T6417sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl6_2.Role6417sl6_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-7
    public void test6417_smartLifting7() {
       
       runConformTest(
            new String[] {
		"T6417sl7Main.java",
			    "\n" +
			    "public class T6417sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl7_1 t = new Team6417sl7_3();\n" +
			    "        T6417sl7_5    o = new T6417sl7_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl7_4.java",
			    "\n" +
			    "public team class Team6417sl7_4 extends Team6417sl7_3 {\n" +
			    "    public class Role6417sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl7_4.this.toString() + \".Role6417sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl7_2.java",
			    "\n" +
			    "public team class Team6417sl7_2 extends Team6417sl7_1 {\n" +
			    "    public class Role6417sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl7_2.this.toString() + \".Role6417sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl7_4 extends Role6417sl7_3 playedBy T6417sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl7_2.this.toString() + \".Role6417sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl7_5.java",
			    "\n" +
			    "public class T6417sl7_5 extends T6417sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl7_1.java",
			    "\n" +
			    "public class T6417sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl7_3.java",
			    "\n" +
			    "public team class Team6417sl7_3 extends Team6417sl7_2 {\n" +
			    "    public class Role6417sl7_5 extends Role6417sl7_3 playedBy T6417sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl7_3.this.toString() + \".Role6417sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl7_6.java",
			    "\n" +
			    "public class T6417sl7_6 extends T6417sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl7_2.java",
			    "\n" +
			    "public abstract class T6417sl7_2 extends T6417sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl7_3.java",
			    "\n" +
			    "public class T6417sl7_3 extends T6417sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl7_1.java",
			    "\n" +
			    "public team class Team6417sl7_1 {\n" +
			    "    public class Role6417sl7_1 extends T6417sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl7_1.this.toString() + \".Role6417sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl7_2 extends Role6417sl7_1 playedBy T6417sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl7_1.this.toString() + \".Role6417sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl7_3 extends Role6417sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl7_1.this.toString() + \".Role6417sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl7_5 as Role6417sl7_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl7_4.java",
			    "\n" +
			    "public class T6417sl7_4 extends T6417sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl7_3.Role6417sl7_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-8
    public void test6417_smartLifting8() {
       
       runConformTest(
            new String[] {
		"T6417sl8Main.java",
			    "\n" +
			    "public class T6417sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl8_1 t = new Team6417sl8_4();\n" +
			    "        T6417sl8_5    o = new T6417sl8_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl8_4.java",
			    "\n" +
			    "public team class Team6417sl8_4 extends Team6417sl8_3 {\n" +
			    "    public class Role6417sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl8_4.this.toString() + \".Role6417sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl8_1.java",
			    "\n" +
			    "public team class Team6417sl8_1 {\n" +
			    "    public class Role6417sl8_1 extends T6417sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl8_1.this.toString() + \".Role6417sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl8_2 extends Role6417sl8_1 playedBy T6417sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl8_1.this.toString() + \".Role6417sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl8_3 extends Role6417sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl8_1.this.toString() + \".Role6417sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl8_5 as Role6417sl8_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl8_4.java",
			    "\n" +
			    "public class T6417sl8_4 extends T6417sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl8_2.java",
			    "\n" +
			    "public team class Team6417sl8_2 extends Team6417sl8_1 {\n" +
			    "    public class Role6417sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl8_2.this.toString() + \".Role6417sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl8_4 extends Role6417sl8_3 playedBy T6417sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl8_2.this.toString() + \".Role6417sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl8_5.java",
			    "\n" +
			    "public class T6417sl8_5 extends T6417sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl8_1.java",
			    "\n" +
			    "public class T6417sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl8_3.java",
			    "\n" +
			    "public team class Team6417sl8_3 extends Team6417sl8_2 {\n" +
			    "    public class Role6417sl8_5 extends Role6417sl8_3 playedBy T6417sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl8_3.this.toString() + \".Role6417sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl8_6.java",
			    "\n" +
			    "public class T6417sl8_6 extends T6417sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl8_2.java",
			    "\n" +
			    "public abstract class T6417sl8_2 extends T6417sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl8_3.java",
			    "\n" +
			    "public class T6417sl8_3 extends T6417sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl8_4.Role6417sl8_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-9
    public void test6417_smartLifting9() {
       
       runConformTest(
            new String[] {
		"T6417sl9Main.java",
			    "\n" +
			    "public class T6417sl9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl9_1       t  = new Team6417sl9_1();\n" +
			    "        final Team6417sl9_1 ft = new Team6417sl9_1();\n" +
			    "        T6417sl9_5          o  = ft.new Role6417sl9_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl9_4.java",
			    "\n" +
			    "public team class Team6417sl9_4 extends Team6417sl9_3 {\n" +
			    "    public class Role6417sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl9_4.this.toString() + \".Role6417sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl9_3.java",
			    "\n" +
			    "public class T6417sl9_3 extends T6417sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl9_1.java",
			    "\n" +
			    "public team class Team6417sl9_1 {\n" +
			    "    public class Role6417sl9_1 extends T6417sl9_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl9_1.this.toString() + \".Role6417sl9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl9_2 extends Role6417sl9_1 playedBy T6417sl9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl9_1.this.toString() + \".Role6417sl9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl9_3 extends Role6417sl9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl9_1.this.toString() + \".Role6417sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl9_5 as Role6417sl9_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl9_4.java",
			    "\n" +
			    "public class T6417sl9_4 extends T6417sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl9_2.java",
			    "\n" +
			    "public team class Team6417sl9_2 extends Team6417sl9_1 {\n" +
			    "    public class Role6417sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl9_2.this.toString() + \".Role6417sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl9_4 extends Role6417sl9_3 playedBy T6417sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl9_2.this.toString() + \".Role6417sl9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl9_5.java",
			    "\n" +
			    "public class T6417sl9_5 extends T6417sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl9_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl9_1.java",
			    "\n" +
			    "public class T6417sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl9_3.java",
			    "\n" +
			    "public team class Team6417sl9_3 extends Team6417sl9_2 {\n" +
			    "    public class Role6417sl9_5 extends Role6417sl9_3 playedBy T6417sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl9_3.this.toString() + \".Role6417sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl9_6.java",
			    "\n" +
			    "public class T6417sl9_6 extends T6417sl9_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl9_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl9_2.java",
			    "\n" +
			    "public abstract class T6417sl9_2 extends T6417sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl9_1.Role6417sl9_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-10
    public void test6417_smartLifting10() {
       
       runConformTest(
            new String[] {
		"T6417sl10Main.java",
			    "\n" +
			    "public class T6417sl10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl10_1       t  = new Team6417sl10_2();\n" +
			    "        final Team6417sl10_1 ft = new Team6417sl10_1();\n" +
			    "        T6417sl10_5          o  = ft.new Role6417sl10_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl10_4.java",
			    "\n" +
			    "public team class Team6417sl10_4 extends Team6417sl10_3 {\n" +
			    "    public class Role6417sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl10_4.this.toString() + \".Role6417sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl10_3.java",
			    "\n" +
			    "public class T6417sl10_3 extends T6417sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl10_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl10_1.java",
			    "\n" +
			    "public team class Team6417sl10_1 {\n" +
			    "    public class Role6417sl10_1 extends T6417sl10_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl10_1.this.toString() + \".Role6417sl10_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl10_2 extends Role6417sl10_1 playedBy T6417sl10_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl10_1.this.toString() + \".Role6417sl10_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl10_3 extends Role6417sl10_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl10_1.this.toString() + \".Role6417sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl10_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl10_5 as Role6417sl10_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl10_4.java",
			    "\n" +
			    "public class T6417sl10_4 extends T6417sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl10_2.java",
			    "\n" +
			    "public team class Team6417sl10_2 extends Team6417sl10_1 {\n" +
			    "    public class Role6417sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl10_2.this.toString() + \".Role6417sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl10_4 extends Role6417sl10_3 playedBy T6417sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl10_2.this.toString() + \".Role6417sl10_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl10_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl10_5.java",
			    "\n" +
			    "public class T6417sl10_5 extends T6417sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl10_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl10_1.java",
			    "\n" +
			    "public class T6417sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl10_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl10_3.java",
			    "\n" +
			    "public team class Team6417sl10_3 extends Team6417sl10_2 {\n" +
			    "    public class Role6417sl10_5 extends Role6417sl10_3 playedBy T6417sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl10_3.this.toString() + \".Role6417sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl10_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl10_6.java",
			    "\n" +
			    "public class T6417sl10_6 extends T6417sl10_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl10_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl10_2.java",
			    "\n" +
			    "public abstract class T6417sl10_2 extends T6417sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl10_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl10_2.Role6417sl10_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-11
    public void test6417_smartLifting11() {
       
       runConformTest(
            new String[] {
		"T6417sl11Main.java",
			    "\n" +
			    "public class T6417sl11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl11_1       t  = new Team6417sl11_3();\n" +
			    "        final Team6417sl11_1 ft = new Team6417sl11_1();\n" +
			    "        T6417sl11_5          o  = ft.new Role6417sl11_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl11_4.java",
			    "\n" +
			    "public team class Team6417sl11_4 extends Team6417sl11_3 {\n" +
			    "    public class Role6417sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl11_4.this.toString() + \".Role6417sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl11_3.java",
			    "\n" +
			    "public team class Team6417sl11_3 extends Team6417sl11_2 {\n" +
			    "    public class Role6417sl11_5 extends Role6417sl11_3 playedBy T6417sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl11_3.this.toString() + \".Role6417sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl11_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl11_6.java",
			    "\n" +
			    "public class T6417sl11_6 extends T6417sl11_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl11_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl11_2.java",
			    "\n" +
			    "public abstract class T6417sl11_2 extends T6417sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl11_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl11_3.java",
			    "\n" +
			    "public class T6417sl11_3 extends T6417sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl11_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl11_1.java",
			    "\n" +
			    "public team class Team6417sl11_1 {\n" +
			    "    public class Role6417sl11_1 extends T6417sl11_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl11_1.this.toString() + \".Role6417sl11_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl11_2 extends Role6417sl11_1 playedBy T6417sl11_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl11_1.this.toString() + \".Role6417sl11_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl11_3 extends Role6417sl11_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl11_1.this.toString() + \".Role6417sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl11_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl11_5 as Role6417sl11_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl11_4.java",
			    "\n" +
			    "public class T6417sl11_4 extends T6417sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl11_2.java",
			    "\n" +
			    "public team class Team6417sl11_2 extends Team6417sl11_1 {\n" +
			    "    public class Role6417sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl11_2.this.toString() + \".Role6417sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl11_4 extends Role6417sl11_3 playedBy T6417sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl11_2.this.toString() + \".Role6417sl11_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl11_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl11_5.java",
			    "\n" +
			    "public class T6417sl11_5 extends T6417sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl11_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl11_1.java",
			    "\n" +
			    "public class T6417sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl11_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl11_3.Role6417sl11_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.17-otjld-smart-lifting-12
    public void test6417_smartLifting12() {
       
       runConformTest(
            new String[] {
		"T6417sl12Main.java",
			    "\n" +
			    "public class T6417sl12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6417sl12_1       t  = new Team6417sl12_4();\n" +
			    "        final Team6417sl12_1 ft = new Team6417sl12_1();\n" +
			    "        T6417sl12_5          o  = ft.new Role6417sl12_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl12_4.java",
			    "\n" +
			    "public team class Team6417sl12_4 extends Team6417sl12_3 {\n" +
			    "    public class Role6417sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl12_4.this.toString() + \".Role6417sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl12_2.java",
			    "\n" +
			    "public team class Team6417sl12_2 extends Team6417sl12_1 {\n" +
			    "    public class Role6417sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl12_2.this.toString() + \".Role6417sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6417sl12_4 extends Role6417sl12_3 playedBy T6417sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl12_2.this.toString() + \".Role6417sl12_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl12_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl12_5.java",
			    "\n" +
			    "public class T6417sl12_5 extends T6417sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl12_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl12_1.java",
			    "\n" +
			    "public class T6417sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl12_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl12_3.java",
			    "\n" +
			    "public team class Team6417sl12_3 extends Team6417sl12_2 {\n" +
			    "    public class Role6417sl12_5 extends Role6417sl12_3 playedBy T6417sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl12_3.this.toString() + \".Role6417sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl12_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6417sl12_6.java",
			    "\n" +
			    "public class T6417sl12_6 extends T6417sl12_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl12_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl12_2.java",
			    "\n" +
			    "public abstract class T6417sl12_2 extends T6417sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl12_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl12_3.java",
			    "\n" +
			    "public class T6417sl12_3 extends T6417sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl12_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6417sl12_1.java",
			    "\n" +
			    "public team class Team6417sl12_1 {\n" +
			    "    public class Role6417sl12_1 extends T6417sl12_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl12_1.this.toString() + \".Role6417sl12_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6417sl12_2 extends Role6417sl12_1 playedBy T6417sl12_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl12_1.this.toString() + \".Role6417sl12_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6417sl12_3 extends Role6417sl12_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6417sl12_1.this.toString() + \".Role6417sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6417sl12_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6417sl12_5 as Role6417sl12_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6417sl12_4.java",
			    "\n" +
			    "public class T6417sl12_4 extends T6417sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6417sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6417sl12_4.Role6417sl12_5");
    }
}
