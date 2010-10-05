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

public class SmartLifting13 extends AbstractOTJLDTest {
	
	public SmartLifting13(String name) {
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
		return SmartLifting13.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-1
    public void test6413_smartLifting1() {
       
       runConformTest(
            new String[] {
		"T6413sl1Main.java",
			    "\n" +
			    "public class T6413sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl1_1 t = new Team6413sl1_1();\n" +
			    "        T6413sl1_1    o = new T6413sl1_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl1_4.java",
			    "\n" +
			    "public team class Team6413sl1_4 extends Team6413sl1_3 {\n" +
			    "    public class Role6413sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl1_4.this.toString() + \".Role6413sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl1_3.java",
			    "\n" +
			    "public class T6413sl1_3 extends T6413sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl1_1.java",
			    "\n" +
			    "public team class Team6413sl1_1 {\n" +
			    "    public class Role6413sl1_1 extends T6413sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl1_1.this.toString() + \".Role6413sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl1_2 extends Role6413sl1_1 playedBy T6413sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl1_1.this.toString() + \".Role6413sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl1_3 extends Role6413sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl1_1.this.toString() + \".Role6413sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl1_1 as Role6413sl1_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl1_4.java",
			    "\n" +
			    "public class T6413sl1_4 extends T6413sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl1_2.java",
			    "\n" +
			    "public team class Team6413sl1_2 extends Team6413sl1_1 {\n" +
			    "    public class Role6413sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl1_2.this.toString() + \".Role6413sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl1_4 extends Role6413sl1_3 playedBy T6413sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl1_2.this.toString() + \".Role6413sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl1_5.java",
			    "\n" +
			    "public class T6413sl1_5 extends T6413sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl1_1.java",
			    "\n" +
			    "public class T6413sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl1_3.java",
			    "\n" +
			    "public team class Team6413sl1_3 extends Team6413sl1_2 {\n" +
			    "    public class Role6413sl1_5 extends Role6413sl1_3 playedBy T6413sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl1_3.this.toString() + \".Role6413sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl1_6.java",
			    "\n" +
			    "public class T6413sl1_6 extends T6413sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl1_2.java",
			    "\n" +
			    "public abstract class T6413sl1_2 extends T6413sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl1_1.Role6413sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-2
    public void test6413_smartLifting2() {
       
       runConformTest(
            new String[] {
		"T6413sl2Main.java",
			    "\n" +
			    "public class T6413sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl2_1 t = new Team6413sl2_2();\n" +
			    "        T6413sl2_1    o = new T6413sl2_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl2_4.java",
			    "\n" +
			    "public team class Team6413sl2_4 extends Team6413sl2_3 {\n" +
			    "    public class Role6413sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl2_4.this.toString() + \".Role6413sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl2_3.java",
			    "\n" +
			    "public team class Team6413sl2_3 extends Team6413sl2_2 {\n" +
			    "    public class Role6413sl2_5 extends Role6413sl2_3 playedBy T6413sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl2_3.this.toString() + \".Role6413sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl2_6.java",
			    "\n" +
			    "public class T6413sl2_6 extends T6413sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl2_2.java",
			    "\n" +
			    "public abstract class T6413sl2_2 extends T6413sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl2_3.java",
			    "\n" +
			    "public class T6413sl2_3 extends T6413sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl2_1.java",
			    "\n" +
			    "public team class Team6413sl2_1 {\n" +
			    "    public class Role6413sl2_1 extends T6413sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl2_1.this.toString() + \".Role6413sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl2_2 extends Role6413sl2_1 playedBy T6413sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl2_1.this.toString() + \".Role6413sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl2_3 extends Role6413sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl2_1.this.toString() + \".Role6413sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl2_1 as Role6413sl2_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl2_4.java",
			    "\n" +
			    "public class T6413sl2_4 extends T6413sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl2_2.java",
			    "\n" +
			    "public team class Team6413sl2_2 extends Team6413sl2_1 {\n" +
			    "    public class Role6413sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl2_2.this.toString() + \".Role6413sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl2_4 extends Role6413sl2_3 playedBy T6413sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl2_2.this.toString() + \".Role6413sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl2_5.java",
			    "\n" +
			    "public class T6413sl2_5 extends T6413sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl2_1.java",
			    "\n" +
			    "public class T6413sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl2_2.Role6413sl2_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-3
    public void test6413_smartLifting3() {
       
       runConformTest(
            new String[] {
		"T6413sl3Main.java",
			    "\n" +
			    "public class T6413sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl3_1 t = new Team6413sl3_3();\n" +
			    "        T6413sl3_1    o = new T6413sl3_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl3_4.java",
			    "\n" +
			    "public team class Team6413sl3_4 extends Team6413sl3_3 {\n" +
			    "    public class Role6413sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl3_4.this.toString() + \".Role6413sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl3_2.java",
			    "\n" +
			    "public team class Team6413sl3_2 extends Team6413sl3_1 {\n" +
			    "    public class Role6413sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl3_2.this.toString() + \".Role6413sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl3_4 extends Role6413sl3_3 playedBy T6413sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl3_2.this.toString() + \".Role6413sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl3_5.java",
			    "\n" +
			    "public class T6413sl3_5 extends T6413sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl3_1.java",
			    "\n" +
			    "public class T6413sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl3_3.java",
			    "\n" +
			    "public team class Team6413sl3_3 extends Team6413sl3_2 {\n" +
			    "    public class Role6413sl3_5 extends Role6413sl3_3 playedBy T6413sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl3_3.this.toString() + \".Role6413sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl3_6.java",
			    "\n" +
			    "public class T6413sl3_6 extends T6413sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl3_2.java",
			    "\n" +
			    "public abstract class T6413sl3_2 extends T6413sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl3_3.java",
			    "\n" +
			    "public class T6413sl3_3 extends T6413sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl3_1.java",
			    "\n" +
			    "public team class Team6413sl3_1 {\n" +
			    "    public class Role6413sl3_1 extends T6413sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl3_1.this.toString() + \".Role6413sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl3_2 extends Role6413sl3_1 playedBy T6413sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl3_1.this.toString() + \".Role6413sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl3_3 extends Role6413sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl3_1.this.toString() + \".Role6413sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl3_1 as Role6413sl3_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl3_4.java",
			    "\n" +
			    "public class T6413sl3_4 extends T6413sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl3_3.Role6413sl3_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-4
    public void test6413_smartLifting4() {
       
       runConformTest(
            new String[] {
		"T6413sl4Main.java",
			    "\n" +
			    "public class T6413sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl4_1 t = new Team6413sl4_4();\n" +
			    "        T6413sl4_1    o = new T6413sl4_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl4_4.java",
			    "\n" +
			    "public team class Team6413sl4_4 extends Team6413sl4_3 {\n" +
			    "    public class Role6413sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl4_4.this.toString() + \".Role6413sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl4_1.java",
			    "\n" +
			    "public team class Team6413sl4_1 {\n" +
			    "    public class Role6413sl4_1 extends T6413sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl4_1.this.toString() + \".Role6413sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl4_2 extends Role6413sl4_1 playedBy T6413sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl4_1.this.toString() + \".Role6413sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl4_3 extends Role6413sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl4_1.this.toString() + \".Role6413sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl4_1 as Role6413sl4_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl4_4.java",
			    "\n" +
			    "public class T6413sl4_4 extends T6413sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl4_2.java",
			    "\n" +
			    "public team class Team6413sl4_2 extends Team6413sl4_1 {\n" +
			    "    public class Role6413sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl4_2.this.toString() + \".Role6413sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl4_4 extends Role6413sl4_3 playedBy T6413sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl4_2.this.toString() + \".Role6413sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl4_5.java",
			    "\n" +
			    "public class T6413sl4_5 extends T6413sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl4_1.java",
			    "\n" +
			    "public class T6413sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl4_3.java",
			    "\n" +
			    "public team class Team6413sl4_3 extends Team6413sl4_2 {\n" +
			    "    public class Role6413sl4_5 extends Role6413sl4_3 playedBy T6413sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl4_3.this.toString() + \".Role6413sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl4_6.java",
			    "\n" +
			    "public class T6413sl4_6 extends T6413sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl4_2.java",
			    "\n" +
			    "public abstract class T6413sl4_2 extends T6413sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl4_3.java",
			    "\n" +
			    "public class T6413sl4_3 extends T6413sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl4_4.Role6413sl4_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-5
    public void test6413_smartLifting5() {
       
       runConformTest(
            new String[] {
		"T6413sl5Main.java",
			    "\n" +
			    "public class T6413sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl5_1 t = new Team6413sl5_1();\n" +
			    "        T6413sl5_1    o = new T6413sl5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl5_4.java",
			    "\n" +
			    "public team class Team6413sl5_4 extends Team6413sl5_3 {\n" +
			    "    public class Role6413sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl5_4.this.toString() + \".Role6413sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl5_3.java",
			    "\n" +
			    "public class T6413sl5_3 extends T6413sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl5_1.java",
			    "\n" +
			    "public team class Team6413sl5_1 {\n" +
			    "    public class Role6413sl5_1 extends T6413sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl5_1.this.toString() + \".Role6413sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl5_2 extends Role6413sl5_1 playedBy T6413sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl5_1.this.toString() + \".Role6413sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl5_3 extends Role6413sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl5_1.this.toString() + \".Role6413sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl5_1 as Role6413sl5_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl5_4.java",
			    "\n" +
			    "public class T6413sl5_4 extends T6413sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl5_2.java",
			    "\n" +
			    "public team class Team6413sl5_2 extends Team6413sl5_1 {\n" +
			    "    public class Role6413sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl5_2.this.toString() + \".Role6413sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl5_4 extends Role6413sl5_3 playedBy T6413sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl5_2.this.toString() + \".Role6413sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl5_5.java",
			    "\n" +
			    "public class T6413sl5_5 extends T6413sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl5_1.java",
			    "\n" +
			    "public class T6413sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl5_3.java",
			    "\n" +
			    "public team class Team6413sl5_3 extends Team6413sl5_2 {\n" +
			    "    public class Role6413sl5_5 extends Role6413sl5_3 playedBy T6413sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl5_3.this.toString() + \".Role6413sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl5_6.java",
			    "\n" +
			    "public class T6413sl5_6 extends T6413sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl5_2.java",
			    "\n" +
			    "public abstract class T6413sl5_2 extends T6413sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl5_1.Role6413sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-6
    public void test6413_smartLifting6() {
       
       runConformTest(
            new String[] {
		"T6413sl6Main.java",
			    "\n" +
			    "public class T6413sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl6_1 t = new Team6413sl6_2();\n" +
			    "        T6413sl6_1    o = new T6413sl6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl6_4.java",
			    "\n" +
			    "public team class Team6413sl6_4 extends Team6413sl6_3 {\n" +
			    "    public class Role6413sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl6_4.this.toString() + \".Role6413sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl6_3.java",
			    "\n" +
			    "public team class Team6413sl6_3 extends Team6413sl6_2 {\n" +
			    "    public class Role6413sl6_5 extends Role6413sl6_3 playedBy T6413sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl6_3.this.toString() + \".Role6413sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl6_6.java",
			    "\n" +
			    "public class T6413sl6_6 extends T6413sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl6_2.java",
			    "\n" +
			    "public abstract class T6413sl6_2 extends T6413sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl6_3.java",
			    "\n" +
			    "public class T6413sl6_3 extends T6413sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl6_1.java",
			    "\n" +
			    "public team class Team6413sl6_1 {\n" +
			    "    public class Role6413sl6_1 extends T6413sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl6_1.this.toString() + \".Role6413sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl6_2 extends Role6413sl6_1 playedBy T6413sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl6_1.this.toString() + \".Role6413sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl6_3 extends Role6413sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl6_1.this.toString() + \".Role6413sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl6_1 as Role6413sl6_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl6_4.java",
			    "\n" +
			    "public class T6413sl6_4 extends T6413sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl6_2.java",
			    "\n" +
			    "public team class Team6413sl6_2 extends Team6413sl6_1 {\n" +
			    "    public class Role6413sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl6_2.this.toString() + \".Role6413sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl6_4 extends Role6413sl6_3 playedBy T6413sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl6_2.this.toString() + \".Role6413sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl6_5.java",
			    "\n" +
			    "public class T6413sl6_5 extends T6413sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl6_1.java",
			    "\n" +
			    "public class T6413sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl6_2.Role6413sl6_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-7
    public void test6413_smartLifting7() {
       
       runConformTest(
            new String[] {
		"T6413sl7Main.java",
			    "\n" +
			    "public class T6413sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl7_1 t = new Team6413sl7_3();\n" +
			    "        T6413sl7_1    o = new T6413sl7_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl7_4.java",
			    "\n" +
			    "public team class Team6413sl7_4 extends Team6413sl7_3 {\n" +
			    "    public class Role6413sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl7_4.this.toString() + \".Role6413sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl7_2.java",
			    "\n" +
			    "public team class Team6413sl7_2 extends Team6413sl7_1 {\n" +
			    "    public class Role6413sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl7_2.this.toString() + \".Role6413sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl7_4 extends Role6413sl7_3 playedBy T6413sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl7_2.this.toString() + \".Role6413sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl7_5.java",
			    "\n" +
			    "public class T6413sl7_5 extends T6413sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl7_1.java",
			    "\n" +
			    "public class T6413sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl7_3.java",
			    "\n" +
			    "public team class Team6413sl7_3 extends Team6413sl7_2 {\n" +
			    "    public class Role6413sl7_5 extends Role6413sl7_3 playedBy T6413sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl7_3.this.toString() + \".Role6413sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl7_6.java",
			    "\n" +
			    "public class T6413sl7_6 extends T6413sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl7_2.java",
			    "\n" +
			    "public abstract class T6413sl7_2 extends T6413sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl7_3.java",
			    "\n" +
			    "public class T6413sl7_3 extends T6413sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl7_1.java",
			    "\n" +
			    "public team class Team6413sl7_1 {\n" +
			    "    public class Role6413sl7_1 extends T6413sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl7_1.this.toString() + \".Role6413sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl7_2 extends Role6413sl7_1 playedBy T6413sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl7_1.this.toString() + \".Role6413sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl7_3 extends Role6413sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl7_1.this.toString() + \".Role6413sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl7_1 as Role6413sl7_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl7_4.java",
			    "\n" +
			    "public class T6413sl7_4 extends T6413sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl7_3.Role6413sl7_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-8
    public void test6413_smartLifting8() {
       
       runConformTest(
            new String[] {
		"T6413sl8Main.java",
			    "\n" +
			    "public class T6413sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl8_1 t = new Team6413sl8_4();\n" +
			    "        T6413sl8_1    o = new T6413sl8_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl8_4.java",
			    "\n" +
			    "public team class Team6413sl8_4 extends Team6413sl8_3 {\n" +
			    "    public class Role6413sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl8_4.this.toString() + \".Role6413sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl8_1.java",
			    "\n" +
			    "public team class Team6413sl8_1 {\n" +
			    "    public class Role6413sl8_1 extends T6413sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl8_1.this.toString() + \".Role6413sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl8_2 extends Role6413sl8_1 playedBy T6413sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl8_1.this.toString() + \".Role6413sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl8_3 extends Role6413sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl8_1.this.toString() + \".Role6413sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl8_1 as Role6413sl8_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl8_4.java",
			    "\n" +
			    "public class T6413sl8_4 extends T6413sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl8_2.java",
			    "\n" +
			    "public team class Team6413sl8_2 extends Team6413sl8_1 {\n" +
			    "    public class Role6413sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl8_2.this.toString() + \".Role6413sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl8_4 extends Role6413sl8_3 playedBy T6413sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl8_2.this.toString() + \".Role6413sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl8_5.java",
			    "\n" +
			    "public class T6413sl8_5 extends T6413sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl8_1.java",
			    "\n" +
			    "public class T6413sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl8_3.java",
			    "\n" +
			    "public team class Team6413sl8_3 extends Team6413sl8_2 {\n" +
			    "    public class Role6413sl8_5 extends Role6413sl8_3 playedBy T6413sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl8_3.this.toString() + \".Role6413sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl8_6.java",
			    "\n" +
			    "public class T6413sl8_6 extends T6413sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl8_2.java",
			    "\n" +
			    "public abstract class T6413sl8_2 extends T6413sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl8_3.java",
			    "\n" +
			    "public class T6413sl8_3 extends T6413sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl8_4.Role6413sl8_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-9
    public void test6413_smartLifting9() {
       
       runConformTest(
            new String[] {
		"T6413sl9Main.java",
			    "\n" +
			    "public class T6413sl9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl9_1 t = new Team6413sl9_1();\n" +
			    "        T6413sl9_1    o = new T6413sl9_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl9_4.java",
			    "\n" +
			    "public team class Team6413sl9_4 extends Team6413sl9_3 {\n" +
			    "    public class Role6413sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl9_4.this.toString() + \".Role6413sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl9_3.java",
			    "\n" +
			    "public class T6413sl9_3 extends T6413sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl9_1.java",
			    "\n" +
			    "public team class Team6413sl9_1 {\n" +
			    "    public class Role6413sl9_1 extends T6413sl9_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl9_1.this.toString() + \".Role6413sl9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl9_2 extends Role6413sl9_1 playedBy T6413sl9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl9_1.this.toString() + \".Role6413sl9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl9_3 extends Role6413sl9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl9_1.this.toString() + \".Role6413sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl9_1 as Role6413sl9_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl9_4.java",
			    "\n" +
			    "public class T6413sl9_4 extends T6413sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl9_2.java",
			    "\n" +
			    "public team class Team6413sl9_2 extends Team6413sl9_1 {\n" +
			    "    public class Role6413sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl9_2.this.toString() + \".Role6413sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl9_4 extends Role6413sl9_3 playedBy T6413sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl9_2.this.toString() + \".Role6413sl9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl9_5.java",
			    "\n" +
			    "public class T6413sl9_5 extends T6413sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl9_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl9_1.java",
			    "\n" +
			    "public class T6413sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl9_3.java",
			    "\n" +
			    "public team class Team6413sl9_3 extends Team6413sl9_2 {\n" +
			    "    public class Role6413sl9_5 extends Role6413sl9_3 playedBy T6413sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl9_3.this.toString() + \".Role6413sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl9_6.java",
			    "\n" +
			    "public class T6413sl9_6 extends T6413sl9_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl9_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl9_2.java",
			    "\n" +
			    "public abstract class T6413sl9_2 extends T6413sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl9_1.Role6413sl9_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-10
    public void test6413_smartLifting10() {
       
       runConformTest(
            new String[] {
		"T6413sl10Main.java",
			    "\n" +
			    "public class T6413sl10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl10_1 t = new Team6413sl10_2();\n" +
			    "        T6413sl10_1    o = new T6413sl10_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl10_4.java",
			    "\n" +
			    "public team class Team6413sl10_4 extends Team6413sl10_3 {\n" +
			    "    public class Role6413sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl10_4.this.toString() + \".Role6413sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl10_3.java",
			    "\n" +
			    "public class T6413sl10_3 extends T6413sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl10_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl10_1.java",
			    "\n" +
			    "public team class Team6413sl10_1 {\n" +
			    "    public class Role6413sl10_1 extends T6413sl10_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl10_1.this.toString() + \".Role6413sl10_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl10_2 extends Role6413sl10_1 playedBy T6413sl10_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl10_1.this.toString() + \".Role6413sl10_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl10_3 extends Role6413sl10_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl10_1.this.toString() + \".Role6413sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl10_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl10_1 as Role6413sl10_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl10_4.java",
			    "\n" +
			    "public class T6413sl10_4 extends T6413sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl10_2.java",
			    "\n" +
			    "public team class Team6413sl10_2 extends Team6413sl10_1 {\n" +
			    "    public class Role6413sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl10_2.this.toString() + \".Role6413sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl10_4 extends Role6413sl10_3 playedBy T6413sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl10_2.this.toString() + \".Role6413sl10_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl10_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl10_5.java",
			    "\n" +
			    "public class T6413sl10_5 extends T6413sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl10_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl10_1.java",
			    "\n" +
			    "public class T6413sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl10_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl10_3.java",
			    "\n" +
			    "public team class Team6413sl10_3 extends Team6413sl10_2 {\n" +
			    "    public class Role6413sl10_5 extends Role6413sl10_3 playedBy T6413sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl10_3.this.toString() + \".Role6413sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl10_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl10_6.java",
			    "\n" +
			    "public class T6413sl10_6 extends T6413sl10_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl10_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl10_2.java",
			    "\n" +
			    "public abstract class T6413sl10_2 extends T6413sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl10_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl10_2.Role6413sl10_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-11
    public void test6413_smartLifting11() {
       
       runConformTest(
            new String[] {
		"T6413sl11Main.java",
			    "\n" +
			    "public class T6413sl11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl11_1 t = new Team6413sl11_3();\n" +
			    "        T6413sl11_1    o = new T6413sl11_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl11_4.java",
			    "\n" +
			    "public team class Team6413sl11_4 extends Team6413sl11_3 {\n" +
			    "    public class Role6413sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl11_4.this.toString() + \".Role6413sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl11_3.java",
			    "\n" +
			    "public team class Team6413sl11_3 extends Team6413sl11_2 {\n" +
			    "    public class Role6413sl11_5 extends Role6413sl11_3 playedBy T6413sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl11_3.this.toString() + \".Role6413sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl11_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl11_6.java",
			    "\n" +
			    "public class T6413sl11_6 extends T6413sl11_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl11_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl11_2.java",
			    "\n" +
			    "public abstract class T6413sl11_2 extends T6413sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl11_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl11_3.java",
			    "\n" +
			    "public class T6413sl11_3 extends T6413sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl11_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl11_1.java",
			    "\n" +
			    "public team class Team6413sl11_1 {\n" +
			    "    public class Role6413sl11_1 extends T6413sl11_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl11_1.this.toString() + \".Role6413sl11_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl11_2 extends Role6413sl11_1 playedBy T6413sl11_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl11_1.this.toString() + \".Role6413sl11_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl11_3 extends Role6413sl11_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl11_1.this.toString() + \".Role6413sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl11_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl11_1 as Role6413sl11_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl11_4.java",
			    "\n" +
			    "public class T6413sl11_4 extends T6413sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl11_2.java",
			    "\n" +
			    "public team class Team6413sl11_2 extends Team6413sl11_1 {\n" +
			    "    public class Role6413sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl11_2.this.toString() + \".Role6413sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl11_4 extends Role6413sl11_3 playedBy T6413sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl11_2.this.toString() + \".Role6413sl11_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl11_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl11_5.java",
			    "\n" +
			    "public class T6413sl11_5 extends T6413sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl11_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl11_1.java",
			    "\n" +
			    "public class T6413sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl11_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl11_3.Role6413sl11_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-12
    public void test6413_smartLifting12() {
       
       runConformTest(
            new String[] {
		"T6413sl12Main.java",
			    "\n" +
			    "public class T6413sl12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl12_1 t = new Team6413sl12_4();\n" +
			    "        T6413sl12_1    o = new T6413sl12_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl12_4.java",
			    "\n" +
			    "public team class Team6413sl12_4 extends Team6413sl12_3 {\n" +
			    "    public class Role6413sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl12_4.this.toString() + \".Role6413sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl12_2.java",
			    "\n" +
			    "public team class Team6413sl12_2 extends Team6413sl12_1 {\n" +
			    "    public class Role6413sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl12_2.this.toString() + \".Role6413sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl12_4 extends Role6413sl12_3 playedBy T6413sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl12_2.this.toString() + \".Role6413sl12_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl12_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl12_5.java",
			    "\n" +
			    "public class T6413sl12_5 extends T6413sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl12_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl12_1.java",
			    "\n" +
			    "public class T6413sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl12_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl12_3.java",
			    "\n" +
			    "public team class Team6413sl12_3 extends Team6413sl12_2 {\n" +
			    "    public class Role6413sl12_5 extends Role6413sl12_3 playedBy T6413sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl12_3.this.toString() + \".Role6413sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl12_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl12_6.java",
			    "\n" +
			    "public class T6413sl12_6 extends T6413sl12_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl12_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl12_2.java",
			    "\n" +
			    "public abstract class T6413sl12_2 extends T6413sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl12_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl12_3.java",
			    "\n" +
			    "public class T6413sl12_3 extends T6413sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl12_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl12_1.java",
			    "\n" +
			    "public team class Team6413sl12_1 {\n" +
			    "    public class Role6413sl12_1 extends T6413sl12_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl12_1.this.toString() + \".Role6413sl12_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl12_2 extends Role6413sl12_1 playedBy T6413sl12_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl12_1.this.toString() + \".Role6413sl12_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl12_3 extends Role6413sl12_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl12_1.this.toString() + \".Role6413sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl12_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl12_1 as Role6413sl12_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl12_4.java",
			    "\n" +
			    "public class T6413sl12_4 extends T6413sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl12_4.Role6413sl12_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-13
    public void test6413_smartLifting13() {
       
       runConformTest(
            new String[] {
		"T6413sl13Main.java",
			    "\n" +
			    "public class T6413sl13Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl13_1 t = new Team6413sl13_1();\n" +
			    "        T6413sl13_1    o = new T6413sl13_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl13_4.java",
			    "\n" +
			    "public team class Team6413sl13_4 extends Team6413sl13_3 {\n" +
			    "    public class Role6413sl13_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl13_4.this.toString() + \".Role6413sl13_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl13_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl13_1.java",
			    "\n" +
			    "public team class Team6413sl13_1 {\n" +
			    "    public class Role6413sl13_1 extends T6413sl13_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl13_1.this.toString() + \".Role6413sl13_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl13_2 extends Role6413sl13_1 playedBy T6413sl13_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl13_1.this.toString() + \".Role6413sl13_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl13_3 extends Role6413sl13_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl13_1.this.toString() + \".Role6413sl13_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl13_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl13_1 as Role6413sl13_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl13_4.java",
			    "\n" +
			    "public class T6413sl13_4 extends T6413sl13_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl13_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl13_2.java",
			    "\n" +
			    "public team class Team6413sl13_2 extends Team6413sl13_1 {\n" +
			    "    public class Role6413sl13_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl13_2.this.toString() + \".Role6413sl13_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl13_4 extends Role6413sl13_3 playedBy T6413sl13_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl13_2.this.toString() + \".Role6413sl13_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl13_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl13_5.java",
			    "\n" +
			    "public class T6413sl13_5 extends T6413sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl13_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl13_1.java",
			    "\n" +
			    "public class T6413sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl13_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl13_3.java",
			    "\n" +
			    "public team class Team6413sl13_3 extends Team6413sl13_2 {\n" +
			    "    public class Role6413sl13_5 extends Role6413sl13_3 playedBy T6413sl13_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl13_3.this.toString() + \".Role6413sl13_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl13_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl13_6.java",
			    "\n" +
			    "public class T6413sl13_6 extends T6413sl13_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl13_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl13_2.java",
			    "\n" +
			    "public abstract class T6413sl13_2 extends T6413sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl13_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl13_3.java",
			    "\n" +
			    "public class T6413sl13_3 extends T6413sl13_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl13_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl13_1.Role6413sl13_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-14
    public void test6413_smartLifting14() {
       
       runConformTest(
            new String[] {
		"T6413sl14Main.java",
			    "\n" +
			    "public class T6413sl14Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl14_1 t = new Team6413sl14_2();\n" +
			    "        T6413sl14_1    o = new T6413sl14_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl14_4.java",
			    "\n" +
			    "public team class Team6413sl14_4 extends Team6413sl14_3 {\n" +
			    "    public class Role6413sl14_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl14_4.this.toString() + \".Role6413sl14_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl14_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl14_3.java",
			    "\n" +
			    "public class T6413sl14_3 extends T6413sl14_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl14_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl14_1.java",
			    "\n" +
			    "public team class Team6413sl14_1 {\n" +
			    "    public class Role6413sl14_1 extends T6413sl14_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl14_1.this.toString() + \".Role6413sl14_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl14_2 extends Role6413sl14_1 playedBy T6413sl14_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl14_1.this.toString() + \".Role6413sl14_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl14_3 extends Role6413sl14_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl14_1.this.toString() + \".Role6413sl14_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl14_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl14_1 as Role6413sl14_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl14_4.java",
			    "\n" +
			    "public class T6413sl14_4 extends T6413sl14_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl14_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl14_2.java",
			    "\n" +
			    "public team class Team6413sl14_2 extends Team6413sl14_1 {\n" +
			    "    public class Role6413sl14_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl14_2.this.toString() + \".Role6413sl14_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl14_4 extends Role6413sl14_3 playedBy T6413sl14_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl14_2.this.toString() + \".Role6413sl14_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl14_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl14_5.java",
			    "\n" +
			    "public class T6413sl14_5 extends T6413sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl14_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl14_1.java",
			    "\n" +
			    "public class T6413sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl14_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl14_3.java",
			    "\n" +
			    "public team class Team6413sl14_3 extends Team6413sl14_2 {\n" +
			    "    public class Role6413sl14_5 extends Role6413sl14_3 playedBy T6413sl14_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl14_3.this.toString() + \".Role6413sl14_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl14_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl14_6.java",
			    "\n" +
			    "public class T6413sl14_6 extends T6413sl14_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl14_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl14_2.java",
			    "\n" +
			    "public abstract class T6413sl14_2 extends T6413sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl14_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl14_2.Role6413sl14_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-15
    public void test6413_smartLifting15() {
       
       runConformTest(
            new String[] {
		"T6413sl15Main.java",
			    "\n" +
			    "public class T6413sl15Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl15_1 t = new Team6413sl15_3();\n" +
			    "        T6413sl15_1    o = new T6413sl15_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl15_4.java",
			    "\n" +
			    "public team class Team6413sl15_4 extends Team6413sl15_3 {\n" +
			    "    public class Role6413sl15_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl15_4.this.toString() + \".Role6413sl15_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl15_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl15_3.java",
			    "\n" +
			    "public team class Team6413sl15_3 extends Team6413sl15_2 {\n" +
			    "    public class Role6413sl15_5 extends Role6413sl15_3 playedBy T6413sl15_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl15_3.this.toString() + \".Role6413sl15_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl15_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl15_6.java",
			    "\n" +
			    "public class T6413sl15_6 extends T6413sl15_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl15_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl15_2.java",
			    "\n" +
			    "public abstract class T6413sl15_2 extends T6413sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl15_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl15_3.java",
			    "\n" +
			    "public class T6413sl15_3 extends T6413sl15_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl15_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl15_1.java",
			    "\n" +
			    "public team class Team6413sl15_1 {\n" +
			    "    public class Role6413sl15_1 extends T6413sl15_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl15_1.this.toString() + \".Role6413sl15_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl15_2 extends Role6413sl15_1 playedBy T6413sl15_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl15_1.this.toString() + \".Role6413sl15_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl15_3 extends Role6413sl15_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl15_1.this.toString() + \".Role6413sl15_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl15_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl15_1 as Role6413sl15_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl15_4.java",
			    "\n" +
			    "public class T6413sl15_4 extends T6413sl15_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl15_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl15_2.java",
			    "\n" +
			    "public team class Team6413sl15_2 extends Team6413sl15_1 {\n" +
			    "    public class Role6413sl15_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl15_2.this.toString() + \".Role6413sl15_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl15_4 extends Role6413sl15_3 playedBy T6413sl15_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl15_2.this.toString() + \".Role6413sl15_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl15_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl15_5.java",
			    "\n" +
			    "public class T6413sl15_5 extends T6413sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl15_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl15_1.java",
			    "\n" +
			    "public class T6413sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl15_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl15_3.Role6413sl15_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-16
    public void test6413_smartLifting16() {
       
       runConformTest(
            new String[] {
		"T6413sl16Main.java",
			    "\n" +
			    "public class T6413sl16Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl16_1 t = new Team6413sl16_4();\n" +
			    "        T6413sl16_1    o = new T6413sl16_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl16_4.java",
			    "\n" +
			    "public team class Team6413sl16_4 extends Team6413sl16_3 {\n" +
			    "    public class Role6413sl16_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl16_4.this.toString() + \".Role6413sl16_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl16_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl16_2.java",
			    "\n" +
			    "public team class Team6413sl16_2 extends Team6413sl16_1 {\n" +
			    "    public class Role6413sl16_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl16_2.this.toString() + \".Role6413sl16_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl16_4 extends Role6413sl16_3 playedBy T6413sl16_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl16_2.this.toString() + \".Role6413sl16_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl16_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl16_5.java",
			    "\n" +
			    "public class T6413sl16_5 extends T6413sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl16_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl16_1.java",
			    "\n" +
			    "public class T6413sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl16_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl16_3.java",
			    "\n" +
			    "public team class Team6413sl16_3 extends Team6413sl16_2 {\n" +
			    "    public class Role6413sl16_5 extends Role6413sl16_3 playedBy T6413sl16_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl16_3.this.toString() + \".Role6413sl16_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl16_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl16_6.java",
			    "\n" +
			    "public class T6413sl16_6 extends T6413sl16_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl16_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl16_2.java",
			    "\n" +
			    "public abstract class T6413sl16_2 extends T6413sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl16_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl16_3.java",
			    "\n" +
			    "public class T6413sl16_3 extends T6413sl16_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl16_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl16_1.java",
			    "\n" +
			    "public team class Team6413sl16_1 {\n" +
			    "    public class Role6413sl16_1 extends T6413sl16_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl16_1.this.toString() + \".Role6413sl16_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl16_2 extends Role6413sl16_1 playedBy T6413sl16_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl16_1.this.toString() + \".Role6413sl16_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl16_3 extends Role6413sl16_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl16_1.this.toString() + \".Role6413sl16_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl16_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl16_1 as Role6413sl16_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl16_4.java",
			    "\n" +
			    "public class T6413sl16_4 extends T6413sl16_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl16_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl16_4.Role6413sl16_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-17
    public void test6413_smartLifting17() {
       
       runConformTest(
            new String[] {
		"T6413sl17Main.java",
			    "\n" +
			    "public class T6413sl17Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl17_1 t = new Team6413sl17_1();\n" +
			    "        T6413sl17_1    o = new T6413sl17_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl17_4.java",
			    "\n" +
			    "public team class Team6413sl17_4 extends Team6413sl17_3 {\n" +
			    "    public class Role6413sl17_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl17_4.this.toString() + \".Role6413sl17_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl17_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl17_1.java",
			    "\n" +
			    "public team class Team6413sl17_1 {\n" +
			    "    public class Role6413sl17_1 extends T6413sl17_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl17_1.this.toString() + \".Role6413sl17_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl17_2 extends Role6413sl17_1 playedBy T6413sl17_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl17_1.this.toString() + \".Role6413sl17_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl17_3 extends Role6413sl17_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl17_1.this.toString() + \".Role6413sl17_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl17_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl17_1 as Role6413sl17_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl17_4.java",
			    "\n" +
			    "public class T6413sl17_4 extends T6413sl17_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl17_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl17_2.java",
			    "\n" +
			    "public team class Team6413sl17_2 extends Team6413sl17_1 {\n" +
			    "    public class Role6413sl17_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl17_2.this.toString() + \".Role6413sl17_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl17_4 extends Role6413sl17_3 playedBy T6413sl17_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl17_2.this.toString() + \".Role6413sl17_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl17_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl17_5.java",
			    "\n" +
			    "public class T6413sl17_5 extends T6413sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl17_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl17_1.java",
			    "\n" +
			    "public class T6413sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl17_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl17_3.java",
			    "\n" +
			    "public team class Team6413sl17_3 extends Team6413sl17_2 {\n" +
			    "    public class Role6413sl17_5 extends Role6413sl17_3 playedBy T6413sl17_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl17_3.this.toString() + \".Role6413sl17_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl17_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl17_6.java",
			    "\n" +
			    "public class T6413sl17_6 extends T6413sl17_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl17_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl17_2.java",
			    "\n" +
			    "public abstract class T6413sl17_2 extends T6413sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl17_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl17_3.java",
			    "\n" +
			    "public class T6413sl17_3 extends T6413sl17_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl17_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl17_1.Role6413sl17_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-18
    public void test6413_smartLifting18() {
       
       runConformTest(
            new String[] {
		"T6413sl18Main.java",
			    "\n" +
			    "public class T6413sl18Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl18_1 t = new Team6413sl18_2();\n" +
			    "        T6413sl18_1    o = new T6413sl18_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl18_4.java",
			    "\n" +
			    "public team class Team6413sl18_4 extends Team6413sl18_3 {\n" +
			    "    public class Role6413sl18_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl18_4.this.toString() + \".Role6413sl18_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl18_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl18_3.java",
			    "\n" +
			    "public class T6413sl18_3 extends T6413sl18_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl18_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl18_1.java",
			    "\n" +
			    "public team class Team6413sl18_1 {\n" +
			    "    public class Role6413sl18_1 extends T6413sl18_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl18_1.this.toString() + \".Role6413sl18_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl18_2 extends Role6413sl18_1 playedBy T6413sl18_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl18_1.this.toString() + \".Role6413sl18_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl18_3 extends Role6413sl18_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl18_1.this.toString() + \".Role6413sl18_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl18_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl18_1 as Role6413sl18_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl18_4.java",
			    "\n" +
			    "public class T6413sl18_4 extends T6413sl18_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl18_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl18_2.java",
			    "\n" +
			    "public team class Team6413sl18_2 extends Team6413sl18_1 {\n" +
			    "    public class Role6413sl18_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl18_2.this.toString() + \".Role6413sl18_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl18_4 extends Role6413sl18_3 playedBy T6413sl18_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl18_2.this.toString() + \".Role6413sl18_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl18_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl18_5.java",
			    "\n" +
			    "public class T6413sl18_5 extends T6413sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl18_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl18_1.java",
			    "\n" +
			    "public class T6413sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl18_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl18_3.java",
			    "\n" +
			    "public team class Team6413sl18_3 extends Team6413sl18_2 {\n" +
			    "    public class Role6413sl18_5 extends Role6413sl18_3 playedBy T6413sl18_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl18_3.this.toString() + \".Role6413sl18_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl18_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl18_6.java",
			    "\n" +
			    "public class T6413sl18_6 extends T6413sl18_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl18_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl18_2.java",
			    "\n" +
			    "public abstract class T6413sl18_2 extends T6413sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl18_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl18_2.Role6413sl18_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-19
    public void test6413_smartLifting19() {
       
       runConformTest(
            new String[] {
		"T6413sl19Main.java",
			    "\n" +
			    "public class T6413sl19Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl19_1 t = new Team6413sl19_3();\n" +
			    "        T6413sl19_1    o = new T6413sl19_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl19_4.java",
			    "\n" +
			    "public team class Team6413sl19_4 extends Team6413sl19_3 {\n" +
			    "    public class Role6413sl19_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl19_4.this.toString() + \".Role6413sl19_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl19_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl19_3.java",
			    "\n" +
			    "public team class Team6413sl19_3 extends Team6413sl19_2 {\n" +
			    "    public class Role6413sl19_5 extends Role6413sl19_3 playedBy T6413sl19_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl19_3.this.toString() + \".Role6413sl19_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl19_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl19_6.java",
			    "\n" +
			    "public class T6413sl19_6 extends T6413sl19_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl19_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl19_2.java",
			    "\n" +
			    "public abstract class T6413sl19_2 extends T6413sl19_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl19_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl19_3.java",
			    "\n" +
			    "public class T6413sl19_3 extends T6413sl19_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl19_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl19_1.java",
			    "\n" +
			    "public team class Team6413sl19_1 {\n" +
			    "    public class Role6413sl19_1 extends T6413sl19_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl19_1.this.toString() + \".Role6413sl19_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl19_2 extends Role6413sl19_1 playedBy T6413sl19_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl19_1.this.toString() + \".Role6413sl19_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl19_3 extends Role6413sl19_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl19_1.this.toString() + \".Role6413sl19_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl19_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl19_1 as Role6413sl19_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl19_4.java",
			    "\n" +
			    "public class T6413sl19_4 extends T6413sl19_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl19_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl19_2.java",
			    "\n" +
			    "public team class Team6413sl19_2 extends Team6413sl19_1 {\n" +
			    "    public class Role6413sl19_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl19_2.this.toString() + \".Role6413sl19_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl19_4 extends Role6413sl19_3 playedBy T6413sl19_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl19_2.this.toString() + \".Role6413sl19_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl19_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl19_5.java",
			    "\n" +
			    "public class T6413sl19_5 extends T6413sl19_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl19_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl19_1.java",
			    "\n" +
			    "public class T6413sl19_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl19_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl19_3.Role6413sl19_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-20
    public void test6413_smartLifting20() {
       
       runConformTest(
            new String[] {
		"T6413sl20Main.java",
			    "\n" +
			    "public class T6413sl20Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl20_1 t = new Team6413sl20_4();\n" +
			    "        T6413sl20_1    o = new T6413sl20_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl20_4.java",
			    "\n" +
			    "public team class Team6413sl20_4 extends Team6413sl20_3 {\n" +
			    "    public class Role6413sl20_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl20_4.this.toString() + \".Role6413sl20_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl20_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl20_3.java",
			    "\n" +
			    "public team class Team6413sl20_3 extends Team6413sl20_2 {\n" +
			    "    public class Role6413sl20_5 extends Role6413sl20_3 playedBy T6413sl20_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl20_3.this.toString() + \".Role6413sl20_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl20_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl20_6.java",
			    "\n" +
			    "public class T6413sl20_6 extends T6413sl20_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl20_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl20_2.java",
			    "\n" +
			    "public abstract class T6413sl20_2 extends T6413sl20_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl20_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl20_3.java",
			    "\n" +
			    "public class T6413sl20_3 extends T6413sl20_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl20_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl20_1.java",
			    "\n" +
			    "public team class Team6413sl20_1 {\n" +
			    "    public class Role6413sl20_1 extends T6413sl20_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl20_1.this.toString() + \".Role6413sl20_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl20_2 extends Role6413sl20_1 playedBy T6413sl20_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl20_1.this.toString() + \".Role6413sl20_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl20_3 extends Role6413sl20_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl20_1.this.toString() + \".Role6413sl20_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl20_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl20_1 as Role6413sl20_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl20_4.java",
			    "\n" +
			    "public class T6413sl20_4 extends T6413sl20_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl20_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl20_2.java",
			    "\n" +
			    "public team class Team6413sl20_2 extends Team6413sl20_1 {\n" +
			    "    public class Role6413sl20_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl20_2.this.toString() + \".Role6413sl20_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl20_4 extends Role6413sl20_3 playedBy T6413sl20_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl20_2.this.toString() + \".Role6413sl20_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl20_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl20_5.java",
			    "\n" +
			    "public class T6413sl20_5 extends T6413sl20_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl20_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl20_1.java",
			    "\n" +
			    "public class T6413sl20_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl20_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl20_4.Role6413sl20_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-21
    public void test6413_smartLifting21() {
       
       runConformTest(
            new String[] {
		"T6413sl21Main.java",
			    "\n" +
			    "public class T6413sl21Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl21_1       t  = new Team6413sl21_1();\n" +
			    "        final Team6413sl21_1 ft = new Team6413sl21_1();\n" +
			    "        T6413sl21_1          o  = ft.new Role6413sl21_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl21_4.java",
			    "\n" +
			    "public team class Team6413sl21_4 extends Team6413sl21_3 {\n" +
			    "    public class Role6413sl21_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl21_4.this.toString() + \".Role6413sl21_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl21_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl21_2.java",
			    "\n" +
			    "public team class Team6413sl21_2 extends Team6413sl21_1 {\n" +
			    "    public class Role6413sl21_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl21_2.this.toString() + \".Role6413sl21_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl21_4 extends Role6413sl21_3 playedBy T6413sl21_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl21_2.this.toString() + \".Role6413sl21_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl21_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl21_5.java",
			    "\n" +
			    "public class T6413sl21_5 extends T6413sl21_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl21_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl21_1.java",
			    "\n" +
			    "public class T6413sl21_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl21_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl21_3.java",
			    "\n" +
			    "public team class Team6413sl21_3 extends Team6413sl21_2 {\n" +
			    "    public class Role6413sl21_5 extends Role6413sl21_3 playedBy T6413sl21_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl21_3.this.toString() + \".Role6413sl21_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl21_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl21_6.java",
			    "\n" +
			    "public class T6413sl21_6 extends T6413sl21_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl21_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl21_2.java",
			    "\n" +
			    "public abstract class T6413sl21_2 extends T6413sl21_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl21_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl21_3.java",
			    "\n" +
			    "public class T6413sl21_3 extends T6413sl21_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl21_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl21_1.java",
			    "\n" +
			    "public team class Team6413sl21_1 {\n" +
			    "    public class Role6413sl21_1 extends T6413sl21_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl21_1.this.toString() + \".Role6413sl21_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl21_2 extends Role6413sl21_1 playedBy T6413sl21_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl21_1.this.toString() + \".Role6413sl21_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl21_3 extends Role6413sl21_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl21_1.this.toString() + \".Role6413sl21_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl21_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl21_1 as Role6413sl21_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl21_4.java",
			    "\n" +
			    "public class T6413sl21_4 extends T6413sl21_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl21_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl21_1.Role6413sl21_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-22
    public void test6413_smartLifting22() {
       
       runConformTest(
            new String[] {
		"T6413sl22Main.java",
			    "\n" +
			    "public class T6413sl22Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl22_1       t  = new Team6413sl22_2();\n" +
			    "        final Team6413sl22_1 ft = new Team6413sl22_1();\n" +
			    "        T6413sl22_1          o  = ft.new Role6413sl22_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl22_4.java",
			    "\n" +
			    "public team class Team6413sl22_4 extends Team6413sl22_3 {\n" +
			    "    public class Role6413sl22_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl22_4.this.toString() + \".Role6413sl22_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl22_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl22_1.java",
			    "\n" +
			    "public team class Team6413sl22_1 {\n" +
			    "    public class Role6413sl22_1 extends T6413sl22_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl22_1.this.toString() + \".Role6413sl22_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl22_2 extends Role6413sl22_1 playedBy T6413sl22_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl22_1.this.toString() + \".Role6413sl22_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl22_3 extends Role6413sl22_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl22_1.this.toString() + \".Role6413sl22_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl22_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl22_1 as Role6413sl22_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl22_4.java",
			    "\n" +
			    "public class T6413sl22_4 extends T6413sl22_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl22_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl22_2.java",
			    "\n" +
			    "public team class Team6413sl22_2 extends Team6413sl22_1 {\n" +
			    "    public class Role6413sl22_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl22_2.this.toString() + \".Role6413sl22_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl22_4 extends Role6413sl22_3 playedBy T6413sl22_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl22_2.this.toString() + \".Role6413sl22_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl22_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl22_5.java",
			    "\n" +
			    "public class T6413sl22_5 extends T6413sl22_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl22_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl22_1.java",
			    "\n" +
			    "public class T6413sl22_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl22_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl22_3.java",
			    "\n" +
			    "public team class Team6413sl22_3 extends Team6413sl22_2 {\n" +
			    "    public class Role6413sl22_5 extends Role6413sl22_3 playedBy T6413sl22_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl22_3.this.toString() + \".Role6413sl22_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl22_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl22_6.java",
			    "\n" +
			    "public class T6413sl22_6 extends T6413sl22_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl22_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl22_2.java",
			    "\n" +
			    "public abstract class T6413sl22_2 extends T6413sl22_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl22_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl22_3.java",
			    "\n" +
			    "public class T6413sl22_3 extends T6413sl22_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl22_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl22_2.Role6413sl22_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-23
    public void test6413_smartLifting23() {
       
       runConformTest(
            new String[] {
		"T6413sl23Main.java",
			    "\n" +
			    "public class T6413sl23Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl23_1       t  = new Team6413sl23_3();\n" +
			    "        final Team6413sl23_1 ft = new Team6413sl23_1();\n" +
			    "        T6413sl23_1          o  = ft.new Role6413sl23_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl23_4.java",
			    "\n" +
			    "public team class Team6413sl23_4 extends Team6413sl23_3 {\n" +
			    "    public class Role6413sl23_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl23_4.this.toString() + \".Role6413sl23_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl23_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl23_3.java",
			    "\n" +
			    "public class T6413sl23_3 extends T6413sl23_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl23_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl23_1.java",
			    "\n" +
			    "public team class Team6413sl23_1 {\n" +
			    "    public class Role6413sl23_1 extends T6413sl23_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl23_1.this.toString() + \".Role6413sl23_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl23_2 extends Role6413sl23_1 playedBy T6413sl23_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl23_1.this.toString() + \".Role6413sl23_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl23_3 extends Role6413sl23_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl23_1.this.toString() + \".Role6413sl23_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl23_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl23_1 as Role6413sl23_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl23_4.java",
			    "\n" +
			    "public class T6413sl23_4 extends T6413sl23_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl23_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl23_2.java",
			    "\n" +
			    "public team class Team6413sl23_2 extends Team6413sl23_1 {\n" +
			    "    public class Role6413sl23_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl23_2.this.toString() + \".Role6413sl23_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl23_4 extends Role6413sl23_3 playedBy T6413sl23_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl23_2.this.toString() + \".Role6413sl23_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl23_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl23_5.java",
			    "\n" +
			    "public class T6413sl23_5 extends T6413sl23_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl23_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl23_1.java",
			    "\n" +
			    "public class T6413sl23_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl23_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl23_3.java",
			    "\n" +
			    "public team class Team6413sl23_3 extends Team6413sl23_2 {\n" +
			    "    public class Role6413sl23_5 extends Role6413sl23_3 playedBy T6413sl23_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl23_3.this.toString() + \".Role6413sl23_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl23_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl23_6.java",
			    "\n" +
			    "public class T6413sl23_6 extends T6413sl23_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl23_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl23_2.java",
			    "\n" +
			    "public abstract class T6413sl23_2 extends T6413sl23_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl23_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl23_3.Role6413sl23_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.13-otjld-smart-lifting-24
    public void test6413_smartLifting24() {
       
       runConformTest(
            new String[] {
		"T6413sl24Main.java",
			    "\n" +
			    "public class T6413sl24Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team6413sl24_1       t  = new Team6413sl24_4();\n" +
			    "        final Team6413sl24_1 ft = new Team6413sl24_1();\n" +
			    "        T6413sl24_1          o  = ft.new Role6413sl24_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl24_4.java",
			    "\n" +
			    "public team class Team6413sl24_4 extends Team6413sl24_3 {\n" +
			    "    public class Role6413sl24_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl24_4.this.toString() + \".Role6413sl24_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl24_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl24_3.java",
			    "\n" +
			    "public team class Team6413sl24_3 extends Team6413sl24_2 {\n" +
			    "    public class Role6413sl24_5 extends Role6413sl24_3 playedBy T6413sl24_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl24_3.this.toString() + \".Role6413sl24_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl24_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl24_6.java",
			    "\n" +
			    "public class T6413sl24_6 extends T6413sl24_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl24_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl24_2.java",
			    "\n" +
			    "public abstract class T6413sl24_2 extends T6413sl24_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl24_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl24_3.java",
			    "\n" +
			    "public class T6413sl24_3 extends T6413sl24_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl24_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl24_1.java",
			    "\n" +
			    "public team class Team6413sl24_1 {\n" +
			    "    public class Role6413sl24_1 extends T6413sl24_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl24_1.this.toString() + \".Role6413sl24_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role6413sl24_2 extends Role6413sl24_1 playedBy T6413sl24_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl24_1.this.toString() + \".Role6413sl24_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role6413sl24_3 extends Role6413sl24_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl24_1.this.toString() + \".Role6413sl24_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl24_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T6413sl24_1 as Role6413sl24_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl24_4.java",
			    "\n" +
			    "public class T6413sl24_4 extends T6413sl24_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl24_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6413sl24_2.java",
			    "\n" +
			    "public team class Team6413sl24_2 extends Team6413sl24_1 {\n" +
			    "    public class Role6413sl24_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl24_2.this.toString() + \".Role6413sl24_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role6413sl24_4 extends Role6413sl24_3 playedBy T6413sl24_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team6413sl24_2.this.toString() + \".Role6413sl24_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team6413sl24_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T6413sl24_5.java",
			    "\n" +
			    "public class T6413sl24_5 extends T6413sl24_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl24_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6413sl24_1.java",
			    "\n" +
			    "public class T6413sl24_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T6413sl24_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team6413sl24_4.Role6413sl24_5");
    }
}
