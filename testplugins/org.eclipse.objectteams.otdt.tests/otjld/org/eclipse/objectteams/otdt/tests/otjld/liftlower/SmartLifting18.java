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

public class SmartLifting18 extends AbstractOTJLDTest {
	
	public SmartLifting18(String name) {
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
		return SmartLifting18.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.18-otjld-smart-lifting-1
    public void test6418_smartLifting1() {
       
       runConformTest(
            new String[] {
		"T6418sl1Main.java",
			    "\n" +
			    "public class T6418sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6418sl1_1 t = new Team6418sl1_1();\n" +
			    "        T6418sl1_6    o = new T6418sl1_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl1_4.java",
			    "\n" +
			    "public team class Team6418sl1_4 extends Team6418sl1_3 {\n" +
			    "    public class Role6418sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl1_4.this.toString() + \".Role6418sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl1_3.java",
			    "\n" +
			    "public team class Team6418sl1_3 extends Team6418sl1_2 {\n" +
			    "    public class Role6418sl1_5 extends Role6418sl1_3 playedBy T6418sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl1_3.this.toString() + \".Role6418sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl1_6.java",
			    "\n" +
			    "public class T6418sl1_6 extends T6418sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl1_2.java",
			    "\n" +
			    "public abstract class T6418sl1_2 extends T6418sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl1_3.java",
			    "\n" +
			    "public class T6418sl1_3 extends T6418sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl1_1.java",
			    "\n" +
			    "public team class Team6418sl1_1 {\n" +
			    "    public class Role6418sl1_1 extends T6418sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl1_1.this.toString() + \".Role6418sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6418sl1_2 extends Role6418sl1_1 playedBy T6418sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl1_1.this.toString() + \".Role6418sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6418sl1_3 extends Role6418sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl1_1.this.toString() + \".Role6418sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6418sl1_6 as Role6418sl1_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl1_4.java",
			    "\n" +
			    "public class T6418sl1_4 extends T6418sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl1_2.java",
			    "\n" +
			    "public team class Team6418sl1_2 extends Team6418sl1_1 {\n" +
			    "    public class Role6418sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl1_2.this.toString() + \".Role6418sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6418sl1_4 extends Role6418sl1_3 playedBy T6418sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl1_2.this.toString() + \".Role6418sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl1_5.java",
			    "\n" +
			    "public class T6418sl1_5 extends T6418sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl1_1.java",
			    "\n" +
			    "public class T6418sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6418sl1_1.Role6418sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.18-otjld-smart-lifting-2
    public void test6418_smartLifting2() {
       
       runConformTest(
            new String[] {
		"T6418sl2Main.java",
			    "\n" +
			    "public class T6418sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6418sl2_1 t = new Team6418sl2_2();\n" +
			    "        T6418sl2_6    o = new T6418sl2_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl2_4.java",
			    "\n" +
			    "public team class Team6418sl2_4 extends Team6418sl2_3 {\n" +
			    "    public class Role6418sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl2_4.this.toString() + \".Role6418sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl2_2.java",
			    "\n" +
			    "public team class Team6418sl2_2 extends Team6418sl2_1 {\n" +
			    "    public class Role6418sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl2_2.this.toString() + \".Role6418sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6418sl2_4 extends Role6418sl2_3 playedBy T6418sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl2_2.this.toString() + \".Role6418sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl2_5.java",
			    "\n" +
			    "public class T6418sl2_5 extends T6418sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl2_1.java",
			    "\n" +
			    "public class T6418sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl2_3.java",
			    "\n" +
			    "public team class Team6418sl2_3 extends Team6418sl2_2 {\n" +
			    "    public class Role6418sl2_5 extends Role6418sl2_3 playedBy T6418sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl2_3.this.toString() + \".Role6418sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl2_6.java",
			    "\n" +
			    "public class T6418sl2_6 extends T6418sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl2_2.java",
			    "\n" +
			    "public abstract class T6418sl2_2 extends T6418sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl2_3.java",
			    "\n" +
			    "public class T6418sl2_3 extends T6418sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl2_1.java",
			    "\n" +
			    "public team class Team6418sl2_1 {\n" +
			    "    public class Role6418sl2_1 extends T6418sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl2_1.this.toString() + \".Role6418sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6418sl2_2 extends Role6418sl2_1 playedBy T6418sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl2_1.this.toString() + \".Role6418sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6418sl2_3 extends Role6418sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl2_1.this.toString() + \".Role6418sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6418sl2_6 as Role6418sl2_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl2_4.java",
			    "\n" +
			    "public class T6418sl2_4 extends T6418sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6418sl2_2.Role6418sl2_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.18-otjld-smart-lifting-3
    public void test6418_smartLifting3() {
       
       runConformTest(
            new String[] {
		"T6418sl3Main.java",
			    "\n" +
			    "public class T6418sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6418sl3_1 t = new Team6418sl3_3();\n" +
			    "        T6418sl3_6    o = new T6418sl3_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl3_4.java",
			    "\n" +
			    "public team class Team6418sl3_4 extends Team6418sl3_3 {\n" +
			    "    public class Role6418sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl3_4.this.toString() + \".Role6418sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl3_1.java",
			    "\n" +
			    "public team class Team6418sl3_1 {\n" +
			    "    public class Role6418sl3_1 extends T6418sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl3_1.this.toString() + \".Role6418sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6418sl3_2 extends Role6418sl3_1 playedBy T6418sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl3_1.this.toString() + \".Role6418sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6418sl3_3 extends Role6418sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl3_1.this.toString() + \".Role6418sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6418sl3_6 as Role6418sl3_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl3_4.java",
			    "\n" +
			    "public class T6418sl3_4 extends T6418sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl3_2.java",
			    "\n" +
			    "public team class Team6418sl3_2 extends Team6418sl3_1 {\n" +
			    "    public class Role6418sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl3_2.this.toString() + \".Role6418sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6418sl3_4 extends Role6418sl3_3 playedBy T6418sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl3_2.this.toString() + \".Role6418sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl3_5.java",
			    "\n" +
			    "public class T6418sl3_5 extends T6418sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl3_1.java",
			    "\n" +
			    "public class T6418sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl3_3.java",
			    "\n" +
			    "public team class Team6418sl3_3 extends Team6418sl3_2 {\n" +
			    "    public class Role6418sl3_5 extends Role6418sl3_3 playedBy T6418sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl3_3.this.toString() + \".Role6418sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl3_6.java",
			    "\n" +
			    "public class T6418sl3_6 extends T6418sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl3_2.java",
			    "\n" +
			    "public abstract class T6418sl3_2 extends T6418sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl3_3.java",
			    "\n" +
			    "public class T6418sl3_3 extends T6418sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6418sl3_3.Role6418sl3_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.18-otjld-smart-lifting-4
    public void test6418_smartLifting4() {
       
       runConformTest(
            new String[] {
		"T6418sl4Main.java",
			    "\n" +
			    "public class T6418sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6418sl4_1 t = new Team6418sl4_4();\n" +
			    "        T6418sl4_6    o = new T6418sl4_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl4_4.java",
			    "\n" +
			    "public team class Team6418sl4_4 extends Team6418sl4_3 {\n" +
			    "    public class Role6418sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl4_4.this.toString() + \".Role6418sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl4_3.java",
			    "\n" +
			    "public class T6418sl4_3 extends T6418sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl4_1.java",
			    "\n" +
			    "public team class Team6418sl4_1 {\n" +
			    "    public class Role6418sl4_1 extends T6418sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl4_1.this.toString() + \".Role6418sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6418sl4_2 extends Role6418sl4_1 playedBy T6418sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl4_1.this.toString() + \".Role6418sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6418sl4_3 extends Role6418sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl4_1.this.toString() + \".Role6418sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6418sl4_6 as Role6418sl4_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl4_4.java",
			    "\n" +
			    "public class T6418sl4_4 extends T6418sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl4_2.java",
			    "\n" +
			    "public team class Team6418sl4_2 extends Team6418sl4_1 {\n" +
			    "    public class Role6418sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl4_2.this.toString() + \".Role6418sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6418sl4_4 extends Role6418sl4_3 playedBy T6418sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl4_2.this.toString() + \".Role6418sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl4_5.java",
			    "\n" +
			    "public class T6418sl4_5 extends T6418sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl4_1.java",
			    "\n" +
			    "public class T6418sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl4_3.java",
			    "\n" +
			    "public team class Team6418sl4_3 extends Team6418sl4_2 {\n" +
			    "    public class Role6418sl4_5 extends Role6418sl4_3 playedBy T6418sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl4_3.this.toString() + \".Role6418sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl4_6.java",
			    "\n" +
			    "public class T6418sl4_6 extends T6418sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl4_2.java",
			    "\n" +
			    "public abstract class T6418sl4_2 extends T6418sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6418sl4_4.Role6418sl4_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.18-otjld-smart-lifting-5
    public void test6418_smartLifting5() {
       
       runConformTest(
            new String[] {
		"T6418sl5Main.java",
			    "\n" +
			    "public class T6418sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6418sl5_1       t  = new Team6418sl5_1();\n" +
			    "        final Team6418sl5_1 ft = new Team6418sl5_1();\n" +
			    "        T6418sl5_6          o  = ft.new Role6418sl5_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl5_4.java",
			    "\n" +
			    "public team class Team6418sl5_4 extends Team6418sl5_3 {\n" +
			    "    public class Role6418sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl5_4.this.toString() + \".Role6418sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl5_3.java",
			    "\n" +
			    "public team class Team6418sl5_3 extends Team6418sl5_2 {\n" +
			    "    public class Role6418sl5_5 extends Role6418sl5_3 playedBy T6418sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl5_3.this.toString() + \".Role6418sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl5_6.java",
			    "\n" +
			    "public class T6418sl5_6 extends T6418sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl5_2.java",
			    "\n" +
			    "public abstract class T6418sl5_2 extends T6418sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl5_3.java",
			    "\n" +
			    "public class T6418sl5_3 extends T6418sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl5_1.java",
			    "\n" +
			    "public team class Team6418sl5_1 {\n" +
			    "    public class Role6418sl5_1 extends T6418sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl5_1.this.toString() + \".Role6418sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6418sl5_2 extends Role6418sl5_1 playedBy T6418sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl5_1.this.toString() + \".Role6418sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6418sl5_3 extends Role6418sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl5_1.this.toString() + \".Role6418sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6418sl5_6 as Role6418sl5_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl5_4.java",
			    "\n" +
			    "public class T6418sl5_4 extends T6418sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl5_2.java",
			    "\n" +
			    "public team class Team6418sl5_2 extends Team6418sl5_1 {\n" +
			    "    public class Role6418sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl5_2.this.toString() + \".Role6418sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6418sl5_4 extends Role6418sl5_3 playedBy T6418sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl5_2.this.toString() + \".Role6418sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl5_5.java",
			    "\n" +
			    "public class T6418sl5_5 extends T6418sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl5_1.java",
			    "\n" +
			    "public class T6418sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6418sl5_1.Role6418sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.18-otjld-smart-lifting-6
    public void test6418_smartLifting6() {
       
       runConformTest(
            new String[] {
		"T6418sl6Main.java",
			    "\n" +
			    "public class T6418sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6418sl6_1       t  = new Team6418sl6_2();\n" +
			    "        final Team6418sl6_1 ft = new Team6418sl6_1();\n" +
			    "        T6418sl6_6          o  = ft.new Role6418sl6_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl6_4.java",
			    "\n" +
			    "public team class Team6418sl6_4 extends Team6418sl6_3 {\n" +
			    "    public class Role6418sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl6_4.this.toString() + \".Role6418sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl6_2.java",
			    "\n" +
			    "public team class Team6418sl6_2 extends Team6418sl6_1 {\n" +
			    "    public class Role6418sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl6_2.this.toString() + \".Role6418sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6418sl6_4 extends Role6418sl6_3 playedBy T6418sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl6_2.this.toString() + \".Role6418sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl6_5.java",
			    "\n" +
			    "public class T6418sl6_5 extends T6418sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl6_1.java",
			    "\n" +
			    "public class T6418sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl6_3.java",
			    "\n" +
			    "public team class Team6418sl6_3 extends Team6418sl6_2 {\n" +
			    "    public class Role6418sl6_5 extends Role6418sl6_3 playedBy T6418sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl6_3.this.toString() + \".Role6418sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl6_6.java",
			    "\n" +
			    "public class T6418sl6_6 extends T6418sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl6_2.java",
			    "\n" +
			    "public abstract class T6418sl6_2 extends T6418sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl6_3.java",
			    "\n" +
			    "public class T6418sl6_3 extends T6418sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl6_1.java",
			    "\n" +
			    "public team class Team6418sl6_1 {\n" +
			    "    public class Role6418sl6_1 extends T6418sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl6_1.this.toString() + \".Role6418sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6418sl6_2 extends Role6418sl6_1 playedBy T6418sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl6_1.this.toString() + \".Role6418sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6418sl6_3 extends Role6418sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl6_1.this.toString() + \".Role6418sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6418sl6_6 as Role6418sl6_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl6_4.java",
			    "\n" +
			    "public class T6418sl6_4 extends T6418sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6418sl6_2.Role6418sl6_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.18-otjld-smart-lifting-7
    public void test6418_smartLifting7() {
       
       runConformTest(
            new String[] {
		"T6418sl7Main.java",
			    "\n" +
			    "public class T6418sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6418sl7_1       t  = new Team6418sl7_3();\n" +
			    "        final Team6418sl7_1 ft = new Team6418sl7_1();\n" +
			    "        T6418sl7_6          o  = ft.new Role6418sl7_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl7_4.java",
			    "\n" +
			    "public team class Team6418sl7_4 extends Team6418sl7_3 {\n" +
			    "    public class Role6418sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl7_4.this.toString() + \".Role6418sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl7_1.java",
			    "\n" +
			    "public team class Team6418sl7_1 {\n" +
			    "    public class Role6418sl7_1 extends T6418sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl7_1.this.toString() + \".Role6418sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6418sl7_2 extends Role6418sl7_1 playedBy T6418sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl7_1.this.toString() + \".Role6418sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6418sl7_3 extends Role6418sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl7_1.this.toString() + \".Role6418sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6418sl7_6 as Role6418sl7_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl7_4.java",
			    "\n" +
			    "public class T6418sl7_4 extends T6418sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl7_2.java",
			    "\n" +
			    "public team class Team6418sl7_2 extends Team6418sl7_1 {\n" +
			    "    public class Role6418sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl7_2.this.toString() + \".Role6418sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6418sl7_4 extends Role6418sl7_3 playedBy T6418sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl7_2.this.toString() + \".Role6418sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl7_5.java",
			    "\n" +
			    "public class T6418sl7_5 extends T6418sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl7_1.java",
			    "\n" +
			    "public class T6418sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl7_3.java",
			    "\n" +
			    "public team class Team6418sl7_3 extends Team6418sl7_2 {\n" +
			    "    public class Role6418sl7_5 extends Role6418sl7_3 playedBy T6418sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl7_3.this.toString() + \".Role6418sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl7_6.java",
			    "\n" +
			    "public class T6418sl7_6 extends T6418sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl7_2.java",
			    "\n" +
			    "public abstract class T6418sl7_2 extends T6418sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl7_3.java",
			    "\n" +
			    "public class T6418sl7_3 extends T6418sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6418sl7_3.Role6418sl7_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.18-otjld-smart-lifting-8
    public void test6418_smartLifting8() {
       
       runConformTest(
            new String[] {
		"T6418sl8Main.java",
			    "\n" +
			    "public class T6418sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6418sl8_1       t  = new Team6418sl8_4();\n" +
			    "        final Team6418sl8_1 ft = new Team6418sl8_1();\n" +
			    "        T6418sl8_6          o  = ft.new Role6418sl8_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl8_4.java",
			    "\n" +
			    "public team class Team6418sl8_4 extends Team6418sl8_3 {\n" +
			    "    public class Role6418sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl8_4.this.toString() + \".Role6418sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl8_3.java",
			    "\n" +
			    "public class T6418sl8_3 extends T6418sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl8_1.java",
			    "\n" +
			    "public team class Team6418sl8_1 {\n" +
			    "    public class Role6418sl8_1 extends T6418sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl8_1.this.toString() + \".Role6418sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6418sl8_2 extends Role6418sl8_1 playedBy T6418sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl8_1.this.toString() + \".Role6418sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6418sl8_3 extends Role6418sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl8_1.this.toString() + \".Role6418sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6418sl8_6 as Role6418sl8_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl8_4.java",
			    "\n" +
			    "public class T6418sl8_4 extends T6418sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl8_2.java",
			    "\n" +
			    "public team class Team6418sl8_2 extends Team6418sl8_1 {\n" +
			    "    public class Role6418sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl8_2.this.toString() + \".Role6418sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6418sl8_4 extends Role6418sl8_3 playedBy T6418sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl8_2.this.toString() + \".Role6418sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl8_5.java",
			    "\n" +
			    "public class T6418sl8_5 extends T6418sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl8_1.java",
			    "\n" +
			    "public class T6418sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6418sl8_3.java",
			    "\n" +
			    "public team class Team6418sl8_3 extends Team6418sl8_2 {\n" +
			    "    public class Role6418sl8_5 extends Role6418sl8_3 playedBy T6418sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6418sl8_3.this.toString() + \".Role6418sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6418sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6418sl8_6.java",
			    "\n" +
			    "public class T6418sl8_6 extends T6418sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6418sl8_2.java",
			    "\n" +
			    "public abstract class T6418sl8_2 extends T6418sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6418sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6418sl8_4.Role6418sl8_5");
    }
}
