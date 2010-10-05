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

public class SmartLifting14 extends AbstractOTJLDTest {
	
	public SmartLifting14(String name) {
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
		return SmartLifting14.class;
	}
    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.14-otjld-smart-lifting-1
    public void test6414_smartLifting1() {
       
       runConformTest(
            new String[] {
		"T6414sl1Main.java",
			    "\n" +
			    "public class T6414sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6414sl1_1 t = new Team6414sl1_1();\n" +
			    "        T6414sl1_2    o = new T6414sl1_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl1_4.java",
			    "\n" +
			    "public team class Team6414sl1_4 extends Team6414sl1_3 {\n" +
			    "    public class Role6414sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl1_4.this.toString() + \".Role6414sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl1_3.java",
			    "\n" +
			    "public team class Team6414sl1_3 extends Team6414sl1_2 {\n" +
			    "    public class Role6414sl1_5 extends Role6414sl1_3 playedBy T6414sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl1_3.this.toString() + \".Role6414sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl1_6.java",
			    "\n" +
			    "public class T6414sl1_6 extends T6414sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl1_2.java",
			    "\n" +
			    "public abstract class T6414sl1_2 extends T6414sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl1_3.java",
			    "\n" +
			    "public class T6414sl1_3 extends T6414sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl1_1.java",
			    "\n" +
			    "public team class Team6414sl1_1 {\n" +
			    "    public class Role6414sl1_1 extends T6414sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl1_1.this.toString() + \".Role6414sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6414sl1_2 extends Role6414sl1_1 playedBy T6414sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl1_1.this.toString() + \".Role6414sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6414sl1_3 extends Role6414sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl1_1.this.toString() + \".Role6414sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6414sl1_2 as Role6414sl1_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl1_4.java",
			    "\n" +
			    "public class T6414sl1_4 extends T6414sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl1_2.java",
			    "\n" +
			    "public team class Team6414sl1_2 extends Team6414sl1_1 {\n" +
			    "    public class Role6414sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl1_2.this.toString() + \".Role6414sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6414sl1_4 extends Role6414sl1_3 playedBy T6414sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl1_2.this.toString() + \".Role6414sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl1_5.java",
			    "\n" +
			    "public class T6414sl1_5 extends T6414sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl1_1.java",
			    "\n" +
			    "public class T6414sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6414sl1_1.Role6414sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.14-otjld-smart-lifting-2
    public void test6414_smartLifting2() {
       
       runConformTest(
            new String[] {
		"T6414sl2Main.java",
			    "\n" +
			    "public class T6414sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6414sl2_1 t = new Team6414sl2_2();\n" +
			    "        T6414sl2_2    o = new T6414sl2_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl2_4.java",
			    "\n" +
			    "public team class Team6414sl2_4 extends Team6414sl2_3 {\n" +
			    "    public class Role6414sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl2_4.this.toString() + \".Role6414sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl2_2.java",
			    "\n" +
			    "public team class Team6414sl2_2 extends Team6414sl2_1 {\n" +
			    "    public class Role6414sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl2_2.this.toString() + \".Role6414sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6414sl2_4 extends Role6414sl2_3 playedBy T6414sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl2_2.this.toString() + \".Role6414sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl2_5.java",
			    "\n" +
			    "public class T6414sl2_5 extends T6414sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl2_1.java",
			    "\n" +
			    "public class T6414sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl2_3.java",
			    "\n" +
			    "public team class Team6414sl2_3 extends Team6414sl2_2 {\n" +
			    "    public class Role6414sl2_5 extends Role6414sl2_3 playedBy T6414sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl2_3.this.toString() + \".Role6414sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl2_6.java",
			    "\n" +
			    "public class T6414sl2_6 extends T6414sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl2_2.java",
			    "\n" +
			    "public abstract class T6414sl2_2 extends T6414sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl2_3.java",
			    "\n" +
			    "public class T6414sl2_3 extends T6414sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl2_1.java",
			    "\n" +
			    "public team class Team6414sl2_1 {\n" +
			    "    public class Role6414sl2_1 extends T6414sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl2_1.this.toString() + \".Role6414sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6414sl2_2 extends Role6414sl2_1 playedBy T6414sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl2_1.this.toString() + \".Role6414sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6414sl2_3 extends Role6414sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl2_1.this.toString() + \".Role6414sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6414sl2_2 as Role6414sl2_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl2_4.java",
			    "\n" +
			    "public class T6414sl2_4 extends T6414sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6414sl2_2.Role6414sl2_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.14-otjld-smart-lifting-3
    public void test6414_smartLifting3() {
       
       runConformTest(
            new String[] {
		"T6414sl3Main.java",
			    "\n" +
			    "public class T6414sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6414sl3_1 t = new Team6414sl3_3();\n" +
			    "        T6414sl3_2    o = new T6414sl3_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl3_4.java",
			    "\n" +
			    "public team class Team6414sl3_4 extends Team6414sl3_3 {\n" +
			    "    public class Role6414sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl3_4.this.toString() + \".Role6414sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl3_1.java",
			    "\n" +
			    "public team class Team6414sl3_1 {\n" +
			    "    public class Role6414sl3_1 extends T6414sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl3_1.this.toString() + \".Role6414sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6414sl3_2 extends Role6414sl3_1 playedBy T6414sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl3_1.this.toString() + \".Role6414sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6414sl3_3 extends Role6414sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl3_1.this.toString() + \".Role6414sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6414sl3_2 as Role6414sl3_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl3_4.java",
			    "\n" +
			    "public class T6414sl3_4 extends T6414sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl3_2.java",
			    "\n" +
			    "public team class Team6414sl3_2 extends Team6414sl3_1 {\n" +
			    "    public class Role6414sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl3_2.this.toString() + \".Role6414sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6414sl3_4 extends Role6414sl3_3 playedBy T6414sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl3_2.this.toString() + \".Role6414sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl3_5.java",
			    "\n" +
			    "public class T6414sl3_5 extends T6414sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl3_1.java",
			    "\n" +
			    "public class T6414sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl3_3.java",
			    "\n" +
			    "public team class Team6414sl3_3 extends Team6414sl3_2 {\n" +
			    "    public class Role6414sl3_5 extends Role6414sl3_3 playedBy T6414sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl3_3.this.toString() + \".Role6414sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl3_6.java",
			    "\n" +
			    "public class T6414sl3_6 extends T6414sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl3_2.java",
			    "\n" +
			    "public abstract class T6414sl3_2 extends T6414sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl3_3.java",
			    "\n" +
			    "public class T6414sl3_3 extends T6414sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6414sl3_3.Role6414sl3_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.14-otjld-smart-lifting-4
    public void test6414_smartLifting4() {
       
       runConformTest(
            new String[] {
		"T6414sl4Main.java",
			    "\n" +
			    "public class T6414sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6414sl4_1 t = new Team6414sl4_4();\n" +
			    "        T6414sl4_2    o = new T6414sl4_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl4_4.java",
			    "\n" +
			    "public team class Team6414sl4_4 extends Team6414sl4_3 {\n" +
			    "    public class Role6414sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl4_4.this.toString() + \".Role6414sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl4_3.java",
			    "\n" +
			    "public class T6414sl4_3 extends T6414sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl4_1.java",
			    "\n" +
			    "public team class Team6414sl4_1 {\n" +
			    "    public class Role6414sl4_1 extends T6414sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl4_1.this.toString() + \".Role6414sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6414sl4_2 extends Role6414sl4_1 playedBy T6414sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl4_1.this.toString() + \".Role6414sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6414sl4_3 extends Role6414sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl4_1.this.toString() + \".Role6414sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6414sl4_2 as Role6414sl4_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl4_4.java",
			    "\n" +
			    "public class T6414sl4_4 extends T6414sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl4_2.java",
			    "\n" +
			    "public team class Team6414sl4_2 extends Team6414sl4_1 {\n" +
			    "    public class Role6414sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl4_2.this.toString() + \".Role6414sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6414sl4_4 extends Role6414sl4_3 playedBy T6414sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl4_2.this.toString() + \".Role6414sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl4_5.java",
			    "\n" +
			    "public class T6414sl4_5 extends T6414sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl4_1.java",
			    "\n" +
			    "public class T6414sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl4_3.java",
			    "\n" +
			    "public team class Team6414sl4_3 extends Team6414sl4_2 {\n" +
			    "    public class Role6414sl4_5 extends Role6414sl4_3 playedBy T6414sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl4_3.this.toString() + \".Role6414sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl4_6.java",
			    "\n" +
			    "public class T6414sl4_6 extends T6414sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl4_2.java",
			    "\n" +
			    "public abstract class T6414sl4_2 extends T6414sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6414sl4_4.Role6414sl4_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.14-otjld-smart-lifting-5
    public void test6414_smartLifting5() {
       
       runConformTest(
            new String[] {
		"T6414sl5Main.java",
			    "\n" +
			    "public class T6414sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6414sl5_1 t = new Team6414sl5_1();\n" +
			    "        T6414sl5_2    o = new T6414sl5_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl5_4.java",
			    "\n" +
			    "public team class Team6414sl5_4 extends Team6414sl5_3 {\n" +
			    "    public class Role6414sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl5_4.this.toString() + \".Role6414sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl5_3.java",
			    "\n" +
			    "public team class Team6414sl5_3 extends Team6414sl5_2 {\n" +
			    "    public class Role6414sl5_5 extends Role6414sl5_3 playedBy T6414sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl5_3.this.toString() + \".Role6414sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl5_6.java",
			    "\n" +
			    "public class T6414sl5_6 extends T6414sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl5_2.java",
			    "\n" +
			    "public abstract class T6414sl5_2 extends T6414sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl5_3.java",
			    "\n" +
			    "public class T6414sl5_3 extends T6414sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl5_1.java",
			    "\n" +
			    "public team class Team6414sl5_1 {\n" +
			    "    public class Role6414sl5_1 extends T6414sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl5_1.this.toString() + \".Role6414sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6414sl5_2 extends Role6414sl5_1 playedBy T6414sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl5_1.this.toString() + \".Role6414sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6414sl5_3 extends Role6414sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl5_1.this.toString() + \".Role6414sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6414sl5_2 as Role6414sl5_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl5_4.java",
			    "\n" +
			    "public class T6414sl5_4 extends T6414sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl5_2.java",
			    "\n" +
			    "public team class Team6414sl5_2 extends Team6414sl5_1 {\n" +
			    "    public class Role6414sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl5_2.this.toString() + \".Role6414sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6414sl5_4 extends Role6414sl5_3 playedBy T6414sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl5_2.this.toString() + \".Role6414sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl5_5.java",
			    "\n" +
			    "public class T6414sl5_5 extends T6414sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl5_1.java",
			    "\n" +
			    "public class T6414sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6414sl5_1.Role6414sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.14-otjld-smart-lifting-6
    public void test6414_smartLifting6() {
       
       runConformTest(
            new String[] {
		"T6414sl6Main.java",
			    "\n" +
			    "public class T6414sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6414sl6_1 t = new Team6414sl6_2();\n" +
			    "        T6414sl6_2    o = new T6414sl6_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl6_4.java",
			    "\n" +
			    "public team class Team6414sl6_4 extends Team6414sl6_3 {\n" +
			    "    public class Role6414sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl6_4.this.toString() + \".Role6414sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl6_2.java",
			    "\n" +
			    "public team class Team6414sl6_2 extends Team6414sl6_1 {\n" +
			    "    public class Role6414sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl6_2.this.toString() + \".Role6414sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6414sl6_4 extends Role6414sl6_3 playedBy T6414sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl6_2.this.toString() + \".Role6414sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl6_5.java",
			    "\n" +
			    "public class T6414sl6_5 extends T6414sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl6_1.java",
			    "\n" +
			    "public class T6414sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl6_3.java",
			    "\n" +
			    "public team class Team6414sl6_3 extends Team6414sl6_2 {\n" +
			    "    public class Role6414sl6_5 extends Role6414sl6_3 playedBy T6414sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl6_3.this.toString() + \".Role6414sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl6_6.java",
			    "\n" +
			    "public class T6414sl6_6 extends T6414sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl6_2.java",
			    "\n" +
			    "public abstract class T6414sl6_2 extends T6414sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl6_3.java",
			    "\n" +
			    "public class T6414sl6_3 extends T6414sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl6_1.java",
			    "\n" +
			    "public team class Team6414sl6_1 {\n" +
			    "    public class Role6414sl6_1 extends T6414sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl6_1.this.toString() + \".Role6414sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6414sl6_2 extends Role6414sl6_1 playedBy T6414sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl6_1.this.toString() + \".Role6414sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6414sl6_3 extends Role6414sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl6_1.this.toString() + \".Role6414sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6414sl6_2 as Role6414sl6_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl6_4.java",
			    "\n" +
			    "public class T6414sl6_4 extends T6414sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6414sl6_2.Role6414sl6_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.14-otjld-smart-lifting-7
    public void test6414_smartLifting7() {
       
       runConformTest(
            new String[] {
		"T6414sl7Main.java",
			    "\n" +
			    "public class T6414sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6414sl7_1 t = new Team6414sl7_3();\n" +
			    "        T6414sl7_2    o = new T6414sl7_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl7_4.java",
			    "\n" +
			    "public team class Team6414sl7_4 extends Team6414sl7_3 {\n" +
			    "    public class Role6414sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl7_4.this.toString() + \".Role6414sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl7_1.java",
			    "\n" +
			    "public team class Team6414sl7_1 {\n" +
			    "    public class Role6414sl7_1 extends T6414sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl7_1.this.toString() + \".Role6414sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6414sl7_2 extends Role6414sl7_1 playedBy T6414sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl7_1.this.toString() + \".Role6414sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6414sl7_3 extends Role6414sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl7_1.this.toString() + \".Role6414sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6414sl7_2 as Role6414sl7_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl7_4.java",
			    "\n" +
			    "public class T6414sl7_4 extends T6414sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl7_2.java",
			    "\n" +
			    "public team class Team6414sl7_2 extends Team6414sl7_1 {\n" +
			    "    public class Role6414sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl7_2.this.toString() + \".Role6414sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6414sl7_4 extends Role6414sl7_3 playedBy T6414sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl7_2.this.toString() + \".Role6414sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl7_5.java",
			    "\n" +
			    "public class T6414sl7_5 extends T6414sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl7_1.java",
			    "\n" +
			    "public class T6414sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl7_3.java",
			    "\n" +
			    "public team class Team6414sl7_3 extends Team6414sl7_2 {\n" +
			    "    public class Role6414sl7_5 extends Role6414sl7_3 playedBy T6414sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl7_3.this.toString() + \".Role6414sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl7_6.java",
			    "\n" +
			    "public class T6414sl7_6 extends T6414sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl7_2.java",
			    "\n" +
			    "public abstract class T6414sl7_2 extends T6414sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl7_3.java",
			    "\n" +
			    "public class T6414sl7_3 extends T6414sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6414sl7_3.Role6414sl7_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.14-otjld-smart-lifting-8
    public void test6414_smartLifting8() {
       
       runConformTest(
            new String[] {
		"T6414sl8Main.java",
			    "\n" +
			    "public class T6414sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6414sl8_1 t = new Team6414sl8_4();\n" +
			    "        T6414sl8_2    o = new T6414sl8_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl8_4.java",
			    "\n" +
			    "public team class Team6414sl8_4 extends Team6414sl8_3 {\n" +
			    "    public class Role6414sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl8_4.this.toString() + \".Role6414sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl8_3.java",
			    "\n" +
			    "public class T6414sl8_3 extends T6414sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl8_1.java",
			    "\n" +
			    "public team class Team6414sl8_1 {\n" +
			    "    public class Role6414sl8_1 extends T6414sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl8_1.this.toString() + \".Role6414sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6414sl8_2 extends Role6414sl8_1 playedBy T6414sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl8_1.this.toString() + \".Role6414sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6414sl8_3 extends Role6414sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl8_1.this.toString() + \".Role6414sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6414sl8_2 as Role6414sl8_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl8_4.java",
			    "\n" +
			    "public class T6414sl8_4 extends T6414sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl8_2.java",
			    "\n" +
			    "public team class Team6414sl8_2 extends Team6414sl8_1 {\n" +
			    "    public class Role6414sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl8_2.this.toString() + \".Role6414sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6414sl8_4 extends Role6414sl8_3 playedBy T6414sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl8_2.this.toString() + \".Role6414sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl8_5.java",
			    "\n" +
			    "public class T6414sl8_5 extends T6414sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl8_1.java",
			    "\n" +
			    "public class T6414sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6414sl8_3.java",
			    "\n" +
			    "public team class Team6414sl8_3 extends Team6414sl8_2 {\n" +
			    "    public class Role6414sl8_5 extends Role6414sl8_3 playedBy T6414sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6414sl8_3.this.toString() + \".Role6414sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6414sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6414sl8_6.java",
			    "\n" +
			    "public class T6414sl8_6 extends T6414sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6414sl8_2.java",
			    "\n" +
			    "public abstract class T6414sl8_2 extends T6414sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6414sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6414sl8_4.Role6414sl8_3");
    }
}
