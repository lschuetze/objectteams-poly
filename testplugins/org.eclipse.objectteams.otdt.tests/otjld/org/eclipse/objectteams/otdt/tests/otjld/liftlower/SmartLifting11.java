/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010 IT Service Omikron GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

public class SmartLifting11 extends AbstractOTJLDTest {
	
	public SmartLifting11(String name) {
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
		return SmartLifting11.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-1
    public void test6411_smartLifting1() {
       
       runConformTest(
            new String[] {
		"T6411sl1Main.java",
			    "\n" +
			    "public class T6411sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl1_1 t = new Team6411sl1_1();\n" +
			    "        T6411sl1_5    o = new T6411sl1_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl1_4.java",
			    "\n" +
			    "public team class Team6411sl1_4 extends Team6411sl1_3 {\n" +
			    "    public class Role6411sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl1_4.this.toString() + \".Role6411sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl1_2.java",
			    "\n" +
			    "public team class Team6411sl1_2 extends Team6411sl1_1 {\n" +
			    "    public class Role6411sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl1_2.this.toString() + \".Role6411sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl1_4 extends Role6411sl1_3 playedBy T6411sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl1_2.this.toString() + \".Role6411sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl1_5.java",
			    "\n" +
			    "public class T6411sl1_5 extends T6411sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl1_1.java",
			    "\n" +
			    "public class T6411sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl1_3.java",
			    "\n" +
			    "public team class Team6411sl1_3 extends Team6411sl1_2 {\n" +
			    "    public class Role6411sl1_5 extends Role6411sl1_3 playedBy T6411sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl1_3.this.toString() + \".Role6411sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl1_6.java",
			    "\n" +
			    "public class T6411sl1_6 extends T6411sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl1_2.java",
			    "\n" +
			    "public abstract class T6411sl1_2 extends T6411sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl1_3.java",
			    "\n" +
			    "public class T6411sl1_3 extends T6411sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl1_1.java",
			    "\n" +
			    "public team class Team6411sl1_1 {\n" +
			    "    public class Role6411sl1_1 extends T6411sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl1_1.this.toString() + \".Role6411sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl1_2 extends Role6411sl1_1 playedBy T6411sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl1_1.this.toString() + \".Role6411sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl1_3 extends Role6411sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl1_1.this.toString() + \".Role6411sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl1_5 as Role6411sl1_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl1_4.java",
			    "\n" +
			    "public class T6411sl1_4 extends T6411sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl1_1.Role6411sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-2
    public void test6411_smartLifting2() {
       
       runConformTest(
            new String[] {
		"T6411sl2Main.java",
			    "\n" +
			    "public class T6411sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl2_1 t = new Team6411sl2_2();\n" +
			    "        T6411sl2_5    o = new T6411sl2_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl2_4.java",
			    "\n" +
			    "public team class Team6411sl2_4 extends Team6411sl2_3 {\n" +
			    "    public class Role6411sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl2_4.this.toString() + \".Role6411sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl2_1.java",
			    "\n" +
			    "public team class Team6411sl2_1 {\n" +
			    "    public class Role6411sl2_1 extends T6411sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl2_1.this.toString() + \".Role6411sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl2_2 extends Role6411sl2_1 playedBy T6411sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl2_1.this.toString() + \".Role6411sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl2_3 extends Role6411sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl2_1.this.toString() + \".Role6411sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl2_5 as Role6411sl2_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl2_4.java",
			    "\n" +
			    "public class T6411sl2_4 extends T6411sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl2_2.java",
			    "\n" +
			    "public team class Team6411sl2_2 extends Team6411sl2_1 {\n" +
			    "    public class Role6411sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl2_2.this.toString() + \".Role6411sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl2_4 extends Role6411sl2_3 playedBy T6411sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl2_2.this.toString() + \".Role6411sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl2_5.java",
			    "\n" +
			    "public class T6411sl2_5 extends T6411sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl2_1.java",
			    "\n" +
			    "public class T6411sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl2_3.java",
			    "\n" +
			    "public team class Team6411sl2_3 extends Team6411sl2_2 {\n" +
			    "    public class Role6411sl2_5 extends Role6411sl2_3 playedBy T6411sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl2_3.this.toString() + \".Role6411sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl2_6.java",
			    "\n" +
			    "public class T6411sl2_6 extends T6411sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl2_2.java",
			    "\n" +
			    "public abstract class T6411sl2_2 extends T6411sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl2_3.java",
			    "\n" +
			    "public class T6411sl2_3 extends T6411sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl2_2.Role6411sl2_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-3
    public void test6411_smartLifting3() {
       
       runConformTest(
            new String[] {
		"T6411sl3Main.java",
			    "\n" +
			    "public class T6411sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl3_1 t = new Team6411sl3_3();\n" +
			    "        T6411sl3_5    o = new T6411sl3_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl3_4.java",
			    "\n" +
			    "public team class Team6411sl3_4 extends Team6411sl3_3 {\n" +
			    "    public class Role6411sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl3_4.this.toString() + \".Role6411sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl3_3.java",
			    "\n" +
			    "public class T6411sl3_3 extends T6411sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl3_1.java",
			    "\n" +
			    "public team class Team6411sl3_1 {\n" +
			    "    public class Role6411sl3_1 extends T6411sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl3_1.this.toString() + \".Role6411sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl3_2 extends Role6411sl3_1 playedBy T6411sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl3_1.this.toString() + \".Role6411sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl3_3 extends Role6411sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl3_1.this.toString() + \".Role6411sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl3_5 as Role6411sl3_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl3_4.java",
			    "\n" +
			    "public class T6411sl3_4 extends T6411sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl3_2.java",
			    "\n" +
			    "public team class Team6411sl3_2 extends Team6411sl3_1 {\n" +
			    "    public class Role6411sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl3_2.this.toString() + \".Role6411sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl3_4 extends Role6411sl3_3 playedBy T6411sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl3_2.this.toString() + \".Role6411sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl3_5.java",
			    "\n" +
			    "public class T6411sl3_5 extends T6411sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl3_1.java",
			    "\n" +
			    "public class T6411sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl3_3.java",
			    "\n" +
			    "public team class Team6411sl3_3 extends Team6411sl3_2 {\n" +
			    "    public class Role6411sl3_5 extends Role6411sl3_3 playedBy T6411sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl3_3.this.toString() + \".Role6411sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl3_6.java",
			    "\n" +
			    "public class T6411sl3_6 extends T6411sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl3_2.java",
			    "\n" +
			    "public abstract class T6411sl3_2 extends T6411sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl3_3.Role6411sl3_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-4
    public void test6411_smartLifting4() {
       
       runConformTest(
            new String[] {
		"T6411sl4Main.java",
			    "\n" +
			    "public class T6411sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl4_1 t = new Team6411sl4_4();\n" +
			    "        T6411sl4_5    o = new T6411sl4_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl4_4.java",
			    "\n" +
			    "public team class Team6411sl4_4 extends Team6411sl4_3 {\n" +
			    "    public class Role6411sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl4_4.this.toString() + \".Role6411sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl4_3.java",
			    "\n" +
			    "public team class Team6411sl4_3 extends Team6411sl4_2 {\n" +
			    "    public class Role6411sl4_5 extends Role6411sl4_3 playedBy T6411sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl4_3.this.toString() + \".Role6411sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl4_6.java",
			    "\n" +
			    "public class T6411sl4_6 extends T6411sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl4_2.java",
			    "\n" +
			    "public abstract class T6411sl4_2 extends T6411sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl4_3.java",
			    "\n" +
			    "public class T6411sl4_3 extends T6411sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl4_1.java",
			    "\n" +
			    "public team class Team6411sl4_1 {\n" +
			    "    public class Role6411sl4_1 extends T6411sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl4_1.this.toString() + \".Role6411sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl4_2 extends Role6411sl4_1 playedBy T6411sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl4_1.this.toString() + \".Role6411sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl4_3 extends Role6411sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl4_1.this.toString() + \".Role6411sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl4_5 as Role6411sl4_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl4_4.java",
			    "\n" +
			    "public class T6411sl4_4 extends T6411sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl4_2.java",
			    "\n" +
			    "public team class Team6411sl4_2 extends Team6411sl4_1 {\n" +
			    "    public class Role6411sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl4_2.this.toString() + \".Role6411sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl4_4 extends Role6411sl4_3 playedBy T6411sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl4_2.this.toString() + \".Role6411sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl4_5.java",
			    "\n" +
			    "public class T6411sl4_5 extends T6411sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl4_1.java",
			    "\n" +
			    "public class T6411sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl4_4.Role6411sl4_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-5
    public void test6411_smartLifting5() {
       
       runConformTest(
            new String[] {
		"T6411sl5Main.java",
			    "\n" +
			    "public class T6411sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl5_1 t = new Team6411sl5_1();\n" +
			    "        T6411sl5_5    o = new T6411sl5_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl5_4.java",
			    "\n" +
			    "public team class Team6411sl5_4 extends Team6411sl5_3 {\n" +
			    "    public class Role6411sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl5_4.this.toString() + \".Role6411sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl5_2.java",
			    "\n" +
			    "public team class Team6411sl5_2 extends Team6411sl5_1 {\n" +
			    "    public class Role6411sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl5_2.this.toString() + \".Role6411sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl5_4 extends Role6411sl5_3 playedBy T6411sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl5_2.this.toString() + \".Role6411sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl5_5.java",
			    "\n" +
			    "public class T6411sl5_5 extends T6411sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl5_1.java",
			    "\n" +
			    "public class T6411sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl5_3.java",
			    "\n" +
			    "public team class Team6411sl5_3 extends Team6411sl5_2 {\n" +
			    "    public class Role6411sl5_5 extends Role6411sl5_3 playedBy T6411sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl5_3.this.toString() + \".Role6411sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl5_6.java",
			    "\n" +
			    "public class T6411sl5_6 extends T6411sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl5_2.java",
			    "\n" +
			    "public abstract class T6411sl5_2 extends T6411sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl5_3.java",
			    "\n" +
			    "public class T6411sl5_3 extends T6411sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl5_1.java",
			    "\n" +
			    "public team class Team6411sl5_1 {\n" +
			    "    public class Role6411sl5_1 extends T6411sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl5_1.this.toString() + \".Role6411sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl5_2 extends Role6411sl5_1 playedBy T6411sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl5_1.this.toString() + \".Role6411sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl5_3 extends Role6411sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl5_1.this.toString() + \".Role6411sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl5_5 as Role6411sl5_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl5_4.java",
			    "\n" +
			    "public class T6411sl5_4 extends T6411sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl5_1.Role6411sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-6
    public void test6411_smartLifting6() {
       
       runConformTest(
            new String[] {
		"T6411sl6Main.java",
			    "\n" +
			    "public class T6411sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl6_1 t = new Team6411sl6_2();\n" +
			    "        T6411sl6_5    o = new T6411sl6_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl6_4.java",
			    "\n" +
			    "public team class Team6411sl6_4 extends Team6411sl6_3 {\n" +
			    "    public class Role6411sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl6_4.this.toString() + \".Role6411sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl6_1.java",
			    "\n" +
			    "public team class Team6411sl6_1 {\n" +
			    "    public class Role6411sl6_1 extends T6411sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl6_1.this.toString() + \".Role6411sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl6_2 extends Role6411sl6_1 playedBy T6411sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl6_1.this.toString() + \".Role6411sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl6_3 extends Role6411sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl6_1.this.toString() + \".Role6411sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl6_5 as Role6411sl6_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl6_4.java",
			    "\n" +
			    "public class T6411sl6_4 extends T6411sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl6_2.java",
			    "\n" +
			    "public team class Team6411sl6_2 extends Team6411sl6_1 {\n" +
			    "    public class Role6411sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl6_2.this.toString() + \".Role6411sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl6_4 extends Role6411sl6_3 playedBy T6411sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl6_2.this.toString() + \".Role6411sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl6_5.java",
			    "\n" +
			    "public class T6411sl6_5 extends T6411sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl6_1.java",
			    "\n" +
			    "public class T6411sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl6_3.java",
			    "\n" +
			    "public team class Team6411sl6_3 extends Team6411sl6_2 {\n" +
			    "    public class Role6411sl6_5 extends Role6411sl6_3 playedBy T6411sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl6_3.this.toString() + \".Role6411sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl6_6.java",
			    "\n" +
			    "public class T6411sl6_6 extends T6411sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl6_2.java",
			    "\n" +
			    "public abstract class T6411sl6_2 extends T6411sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl6_3.java",
			    "\n" +
			    "public class T6411sl6_3 extends T6411sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl6_2.Role6411sl6_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-7
    public void test6411_smartLifting7() {
       
       runConformTest(
            new String[] {
		"T6411sl7Main.java",
			    "\n" +
			    "public class T6411sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl7_1 t = new Team6411sl7_3();\n" +
			    "        T6411sl7_5    o = new T6411sl7_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl7_4.java",
			    "\n" +
			    "public team class Team6411sl7_4 extends Team6411sl7_3 {\n" +
			    "    public class Role6411sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl7_4.this.toString() + \".Role6411sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl7_3.java",
			    "\n" +
			    "public class T6411sl7_3 extends T6411sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl7_1.java",
			    "\n" +
			    "public team class Team6411sl7_1 {\n" +
			    "    public class Role6411sl7_1 extends T6411sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl7_1.this.toString() + \".Role6411sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl7_2 extends Role6411sl7_1 playedBy T6411sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl7_1.this.toString() + \".Role6411sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl7_3 extends Role6411sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl7_1.this.toString() + \".Role6411sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl7_5 as Role6411sl7_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl7_4.java",
			    "\n" +
			    "public class T6411sl7_4 extends T6411sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl7_2.java",
			    "\n" +
			    "public team class Team6411sl7_2 extends Team6411sl7_1 {\n" +
			    "    public class Role6411sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl7_2.this.toString() + \".Role6411sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl7_4 extends Role6411sl7_3 playedBy T6411sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl7_2.this.toString() + \".Role6411sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl7_5.java",
			    "\n" +
			    "public class T6411sl7_5 extends T6411sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl7_1.java",
			    "\n" +
			    "public class T6411sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl7_3.java",
			    "\n" +
			    "public team class Team6411sl7_3 extends Team6411sl7_2 {\n" +
			    "    public class Role6411sl7_5 extends Role6411sl7_3 playedBy T6411sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl7_3.this.toString() + \".Role6411sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl7_6.java",
			    "\n" +
			    "public class T6411sl7_6 extends T6411sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl7_2.java",
			    "\n" +
			    "public abstract class T6411sl7_2 extends T6411sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl7_3.Role6411sl7_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-8
    public void test6411_smartLifting8() {
       
       runConformTest(
            new String[] {
		"T6411sl8Main.java",
			    "\n" +
			    "public class T6411sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl8_1 t = new Team6411sl8_4();\n" +
			    "        T6411sl8_5    o = new T6411sl8_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl8_4.java",
			    "\n" +
			    "public team class Team6411sl8_4 extends Team6411sl8_3 {\n" +
			    "    public class Role6411sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl8_4.this.toString() + \".Role6411sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl8_3.java",
			    "\n" +
			    "public team class Team6411sl8_3 extends Team6411sl8_2 {\n" +
			    "    public class Role6411sl8_5 extends Role6411sl8_3 playedBy T6411sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl8_3.this.toString() + \".Role6411sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl8_6.java",
			    "\n" +
			    "public class T6411sl8_6 extends T6411sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl8_2.java",
			    "\n" +
			    "public abstract class T6411sl8_2 extends T6411sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl8_3.java",
			    "\n" +
			    "public class T6411sl8_3 extends T6411sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl8_1.java",
			    "\n" +
			    "public team class Team6411sl8_1 {\n" +
			    "    public class Role6411sl8_1 extends T6411sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl8_1.this.toString() + \".Role6411sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl8_2 extends Role6411sl8_1 playedBy T6411sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl8_1.this.toString() + \".Role6411sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl8_3 extends Role6411sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl8_1.this.toString() + \".Role6411sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl8_5 as Role6411sl8_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl8_4.java",
			    "\n" +
			    "public class T6411sl8_4 extends T6411sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl8_2.java",
			    "\n" +
			    "public team class Team6411sl8_2 extends Team6411sl8_1 {\n" +
			    "    public class Role6411sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl8_2.this.toString() + \".Role6411sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl8_4 extends Role6411sl8_3 playedBy T6411sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl8_2.this.toString() + \".Role6411sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl8_5.java",
			    "\n" +
			    "public class T6411sl8_5 extends T6411sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl8_1.java",
			    "\n" +
			    "public class T6411sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl8_4.Role6411sl8_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-9
    public void test6411_smartLifting9() {
       
       runConformTest(
            new String[] {
		"T6411sl9Main.java",
			    "\n" +
			    "public class T6411sl9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl9_1       t  = new Team6411sl9_1();\n" +
			    "        final Team6411sl9_1 ft = new Team6411sl9_1();\n" +
			    "        T6411sl9_5          o  = ft.new Role6411sl9_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl9_4.java",
			    "\n" +
			    "public team class Team6411sl9_4 extends Team6411sl9_3 {\n" +
			    "    public class Role6411sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl9_4.this.toString() + \".Role6411sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl9_2.java",
			    "\n" +
			    "public team class Team6411sl9_2 extends Team6411sl9_1 {\n" +
			    "    public class Role6411sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl9_2.this.toString() + \".Role6411sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl9_4 extends Role6411sl9_3 playedBy T6411sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl9_2.this.toString() + \".Role6411sl9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl9_5.java",
			    "\n" +
			    "public class T6411sl9_5 extends T6411sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl9_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl9_1.java",
			    "\n" +
			    "public class T6411sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl9_3.java",
			    "\n" +
			    "public team class Team6411sl9_3 extends Team6411sl9_2 {\n" +
			    "    public class Role6411sl9_5 extends Role6411sl9_3 playedBy T6411sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl9_3.this.toString() + \".Role6411sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl9_6.java",
			    "\n" +
			    "public class T6411sl9_6 extends T6411sl9_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl9_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl9_2.java",
			    "\n" +
			    "public abstract class T6411sl9_2 extends T6411sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl9_3.java",
			    "\n" +
			    "public class T6411sl9_3 extends T6411sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl9_1.java",
			    "\n" +
			    "public team class Team6411sl9_1 {\n" +
			    "    public class Role6411sl9_1 extends T6411sl9_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl9_1.this.toString() + \".Role6411sl9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl9_2 extends Role6411sl9_1 playedBy T6411sl9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl9_1.this.toString() + \".Role6411sl9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl9_3 extends Role6411sl9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl9_1.this.toString() + \".Role6411sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl9_5 as Role6411sl9_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl9_4.java",
			    "\n" +
			    "public class T6411sl9_4 extends T6411sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl9_1.Role6411sl9_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-10
    public void test6411_smartLifting10() {
       
       runConformTest(
            new String[] {
		"T6411sl10Main.java",
			    "\n" +
			    "public class T6411sl10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl10_1       t  = new Team6411sl10_2();\n" +
			    "        final Team6411sl10_1 ft = new Team6411sl10_1();\n" +
			    "        T6411sl10_5          o  = ft.new Role6411sl10_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl10_4.java",
			    "\n" +
			    "public team class Team6411sl10_4 extends Team6411sl10_3 {\n" +
			    "    public class Role6411sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl10_4.this.toString() + \".Role6411sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl10_2.java",
			    "\n" +
			    "public team class Team6411sl10_2 extends Team6411sl10_1 {\n" +
			    "    public class Role6411sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl10_2.this.toString() + \".Role6411sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl10_4 extends Role6411sl10_3 playedBy T6411sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl10_2.this.toString() + \".Role6411sl10_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl10_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl10_5.java",
			    "\n" +
			    "public class T6411sl10_5 extends T6411sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl10_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl10_1.java",
			    "\n" +
			    "public class T6411sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl10_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl10_3.java",
			    "\n" +
			    "public team class Team6411sl10_3 extends Team6411sl10_2 {\n" +
			    "    public class Role6411sl10_5 extends Role6411sl10_3 playedBy T6411sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl10_3.this.toString() + \".Role6411sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl10_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl10_6.java",
			    "\n" +
			    "public class T6411sl10_6 extends T6411sl10_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl10_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl10_2.java",
			    "\n" +
			    "public abstract class T6411sl10_2 extends T6411sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl10_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl10_3.java",
			    "\n" +
			    "public class T6411sl10_3 extends T6411sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl10_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl10_1.java",
			    "\n" +
			    "public team class Team6411sl10_1 {\n" +
			    "    public class Role6411sl10_1 extends T6411sl10_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl10_1.this.toString() + \".Role6411sl10_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl10_2 extends Role6411sl10_1 playedBy T6411sl10_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl10_1.this.toString() + \".Role6411sl10_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl10_3 extends Role6411sl10_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl10_1.this.toString() + \".Role6411sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl10_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl10_5 as Role6411sl10_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl10_4.java",
			    "\n" +
			    "public class T6411sl10_4 extends T6411sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl10_2.Role6411sl10_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-11
    public void test6411_smartLifting11() {
       
       runConformTest(
            new String[] {
		"T6411sl11Main.java",
			    "\n" +
			    "public class T6411sl11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl11_1       t  = new Team6411sl11_3();\n" +
			    "        final Team6411sl11_1 ft = new Team6411sl11_1();\n" +
			    "        T6411sl11_5          o  = ft.new Role6411sl11_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl11_4.java",
			    "\n" +
			    "public team class Team6411sl11_4 extends Team6411sl11_3 {\n" +
			    "    public class Role6411sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl11_4.this.toString() + \".Role6411sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl11_1.java",
			    "\n" +
			    "public team class Team6411sl11_1 {\n" +
			    "    public class Role6411sl11_1 extends T6411sl11_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl11_1.this.toString() + \".Role6411sl11_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl11_2 extends Role6411sl11_1 playedBy T6411sl11_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl11_1.this.toString() + \".Role6411sl11_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl11_3 extends Role6411sl11_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl11_1.this.toString() + \".Role6411sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl11_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl11_5 as Role6411sl11_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl11_4.java",
			    "\n" +
			    "public class T6411sl11_4 extends T6411sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl11_2.java",
			    "\n" +
			    "public team class Team6411sl11_2 extends Team6411sl11_1 {\n" +
			    "    public class Role6411sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl11_2.this.toString() + \".Role6411sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl11_4 extends Role6411sl11_3 playedBy T6411sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl11_2.this.toString() + \".Role6411sl11_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl11_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl11_5.java",
			    "\n" +
			    "public class T6411sl11_5 extends T6411sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl11_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl11_1.java",
			    "\n" +
			    "public class T6411sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl11_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl11_3.java",
			    "\n" +
			    "public team class Team6411sl11_3 extends Team6411sl11_2 {\n" +
			    "    public class Role6411sl11_5 extends Role6411sl11_3 playedBy T6411sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl11_3.this.toString() + \".Role6411sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl11_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl11_6.java",
			    "\n" +
			    "public class T6411sl11_6 extends T6411sl11_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl11_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl11_2.java",
			    "\n" +
			    "public abstract class T6411sl11_2 extends T6411sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl11_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl11_3.java",
			    "\n" +
			    "public class T6411sl11_3 extends T6411sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl11_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl11_3.Role6411sl11_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.11-otjld-smart-lifting-12
    public void test6411_smartLifting12() {
       
       runConformTest(
            new String[] {
		"T6411sl12Main.java",
			    "\n" +
			    "public class T6411sl12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6411sl12_1       t  = new Team6411sl12_4();\n" +
			    "        final Team6411sl12_1 ft = new Team6411sl12_1();\n" +
			    "        T6411sl12_5          o  = ft.new Role6411sl12_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl12_4.java",
			    "\n" +
			    "public team class Team6411sl12_4 extends Team6411sl12_3 {\n" +
			    "    public class Role6411sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl12_4.this.toString() + \".Role6411sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl12_3.java",
			    "\n" +
			    "public class T6411sl12_3 extends T6411sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl12_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl12_1.java",
			    "\n" +
			    "public team class Team6411sl12_1 {\n" +
			    "    public class Role6411sl12_1 extends T6411sl12_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl12_1.this.toString() + \".Role6411sl12_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6411sl12_2 extends Role6411sl12_1 playedBy T6411sl12_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl12_1.this.toString() + \".Role6411sl12_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6411sl12_3 extends Role6411sl12_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl12_1.this.toString() + \".Role6411sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl12_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6411sl12_5 as Role6411sl12_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl12_4.java",
			    "\n" +
			    "public class T6411sl12_4 extends T6411sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl12_2.java",
			    "\n" +
			    "public team class Team6411sl12_2 extends Team6411sl12_1 {\n" +
			    "    public class Role6411sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl12_2.this.toString() + \".Role6411sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6411sl12_4 extends Role6411sl12_3 playedBy T6411sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl12_2.this.toString() + \".Role6411sl12_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl12_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl12_5.java",
			    "\n" +
			    "public class T6411sl12_5 extends T6411sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl12_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl12_1.java",
			    "\n" +
			    "public class T6411sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl12_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6411sl12_3.java",
			    "\n" +
			    "public team class Team6411sl12_3 extends Team6411sl12_2 {\n" +
			    "    public class Role6411sl12_5 extends Role6411sl12_3 playedBy T6411sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6411sl12_3.this.toString() + \".Role6411sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6411sl12_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6411sl12_6.java",
			    "\n" +
			    "public class T6411sl12_6 extends T6411sl12_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl12_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6411sl12_2.java",
			    "\n" +
			    "public abstract class T6411sl12_2 extends T6411sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6411sl12_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6411sl12_4.Role6411sl12_5");
    }
}
