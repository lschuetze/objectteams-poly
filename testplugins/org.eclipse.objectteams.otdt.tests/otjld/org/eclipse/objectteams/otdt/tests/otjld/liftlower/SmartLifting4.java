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

public class SmartLifting4 extends AbstractOTJLDTest {
	
	public SmartLifting4(String name) {
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
		return SmartLifting4.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.4-otjld-smart-lifting-1
    public void test644_smartLifting1() {
       
       runConformTest(
            new String[] {
		"T644sl1Main.java",
			    "\n" +
			    "public class T644sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team644sl1_1 t = new Team644sl1_1();\n" +
			    "        T644sl1_4    o = new T644sl1_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl1_4.java",
			    "\n" +
			    "public team class Team644sl1_4 extends Team644sl1_3 {\n" +
			    "    public class Role644sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl1_4.this.toString() + \".Role644sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl1_3.java",
			    "\n" +
			    "public class T644sl1_3 extends T644sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl1_1.java",
			    "\n" +
			    "public team class Team644sl1_1 {\n" +
			    "    public class Role644sl1_1 extends T644sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl1_1.this.toString() + \".Role644sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role644sl1_2 extends Role644sl1_1 playedBy T644sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl1_1.this.toString() + \".Role644sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role644sl1_3 extends Role644sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl1_1.this.toString() + \".Role644sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T644sl1_4 as Role644sl1_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl1_4.java",
			    "\n" +
			    "public class T644sl1_4 extends T644sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl1_2.java",
			    "\n" +
			    "public team class Team644sl1_2 extends Team644sl1_1 {\n" +
			    "    public class Role644sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl1_2.this.toString() + \".Role644sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role644sl1_4 extends Role644sl1_3 playedBy T644sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl1_2.this.toString() + \".Role644sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T644sl1_5.java",
			    "\n" +
			    "public class T644sl1_5 extends T644sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl1_1.java",
			    "\n" +
			    "public class T644sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl1_3.java",
			    "\n" +
			    "public team class Team644sl1_3 extends Team644sl1_2 {\n" +
			    "    public class Role644sl1_5 extends Role644sl1_3 playedBy T644sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl1_3.this.toString() + \".Role644sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T644sl1_6.java",
			    "\n" +
			    "public class T644sl1_6 extends T644sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl1_2.java",
			    "\n" +
			    "public abstract class T644sl1_2 extends T644sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team644sl1_1.Role644sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.4-otjld-smart-lifting-2
    public void test644_smartLifting2() {
       
       runConformTest(
            new String[] {
		"T644sl2Main.java",
			    "\n" +
			    "public class T644sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team644sl2_1 t = new Team644sl2_2();\n" +
			    "        T644sl2_4    o = new T644sl2_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl2_4.java",
			    "\n" +
			    "public team class Team644sl2_4 extends Team644sl2_3 {\n" +
			    "    public class Role644sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl2_4.this.toString() + \".Role644sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl2_3.java",
			    "\n" +
			    "public team class Team644sl2_3 extends Team644sl2_2 {\n" +
			    "    public class Role644sl2_5 extends Role644sl2_3 playedBy T644sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl2_3.this.toString() + \".Role644sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T644sl2_6.java",
			    "\n" +
			    "public class T644sl2_6 extends T644sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl2_2.java",
			    "\n" +
			    "public abstract class T644sl2_2 extends T644sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl2_3.java",
			    "\n" +
			    "public class T644sl2_3 extends T644sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl2_1.java",
			    "\n" +
			    "public team class Team644sl2_1 {\n" +
			    "    public class Role644sl2_1 extends T644sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl2_1.this.toString() + \".Role644sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role644sl2_2 extends Role644sl2_1 playedBy T644sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl2_1.this.toString() + \".Role644sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role644sl2_3 extends Role644sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl2_1.this.toString() + \".Role644sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T644sl2_4 as Role644sl2_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl2_4.java",
			    "\n" +
			    "public class T644sl2_4 extends T644sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl2_2.java",
			    "\n" +
			    "public team class Team644sl2_2 extends Team644sl2_1 {\n" +
			    "    public class Role644sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl2_2.this.toString() + \".Role644sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role644sl2_4 extends Role644sl2_3 playedBy T644sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl2_2.this.toString() + \".Role644sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T644sl2_5.java",
			    "\n" +
			    "public class T644sl2_5 extends T644sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl2_1.java",
			    "\n" +
			    "public class T644sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team644sl2_2.Role644sl2_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.4-otjld-smart-lifting-3
    public void test644_smartLifting3() {
       
       runConformTest(
            new String[] {
		"T644sl3Main.java",
			    "\n" +
			    "public class T644sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team644sl3_1 t = new Team644sl3_3();\n" +
			    "        T644sl3_4    o = new T644sl3_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl3_4.java",
			    "\n" +
			    "public team class Team644sl3_4 extends Team644sl3_3 {\n" +
			    "    public class Role644sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl3_4.this.toString() + \".Role644sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl3_2.java",
			    "\n" +
			    "public team class Team644sl3_2 extends Team644sl3_1 {\n" +
			    "    public class Role644sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl3_2.this.toString() + \".Role644sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role644sl3_4 extends Role644sl3_3 playedBy T644sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl3_2.this.toString() + \".Role644sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T644sl3_5.java",
			    "\n" +
			    "public class T644sl3_5 extends T644sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl3_1.java",
			    "\n" +
			    "public class T644sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl3_3.java",
			    "\n" +
			    "public team class Team644sl3_3 extends Team644sl3_2 {\n" +
			    "    public class Role644sl3_5 extends Role644sl3_3 playedBy T644sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl3_3.this.toString() + \".Role644sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T644sl3_6.java",
			    "\n" +
			    "public class T644sl3_6 extends T644sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl3_2.java",
			    "\n" +
			    "public abstract class T644sl3_2 extends T644sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl3_3.java",
			    "\n" +
			    "public class T644sl3_3 extends T644sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl3_1.java",
			    "\n" +
			    "public team class Team644sl3_1 {\n" +
			    "    public class Role644sl3_1 extends T644sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl3_1.this.toString() + \".Role644sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role644sl3_2 extends Role644sl3_1 playedBy T644sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl3_1.this.toString() + \".Role644sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role644sl3_3 extends Role644sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl3_1.this.toString() + \".Role644sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T644sl3_4 as Role644sl3_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl3_4.java",
			    "\n" +
			    "public class T644sl3_4 extends T644sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team644sl3_3.Role644sl3_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.4-otjld-smart-lifting-4
    public void test644_smartLifting4() {
       
       runConformTest(
            new String[] {
		"T644sl4Main.java",
			    "\n" +
			    "public class T644sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team644sl4_1 t = new Team644sl4_4();\n" +
			    "        T644sl4_4    o = new T644sl4_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl4_4.java",
			    "\n" +
			    "public team class Team644sl4_4 extends Team644sl4_3 {\n" +
			    "    public class Role644sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl4_4.this.toString() + \".Role644sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl4_1.java",
			    "\n" +
			    "public team class Team644sl4_1 {\n" +
			    "    public class Role644sl4_1 extends T644sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl4_1.this.toString() + \".Role644sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role644sl4_2 extends Role644sl4_1 playedBy T644sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl4_1.this.toString() + \".Role644sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role644sl4_3 extends Role644sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl4_1.this.toString() + \".Role644sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T644sl4_4 as Role644sl4_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl4_4.java",
			    "\n" +
			    "public class T644sl4_4 extends T644sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl4_2.java",
			    "\n" +
			    "public team class Team644sl4_2 extends Team644sl4_1 {\n" +
			    "    public class Role644sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl4_2.this.toString() + \".Role644sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role644sl4_4 extends Role644sl4_3 playedBy T644sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl4_2.this.toString() + \".Role644sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T644sl4_5.java",
			    "\n" +
			    "public class T644sl4_5 extends T644sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl4_1.java",
			    "\n" +
			    "public class T644sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team644sl4_3.java",
			    "\n" +
			    "public team class Team644sl4_3 extends Team644sl4_2 {\n" +
			    "    public class Role644sl4_5 extends Role644sl4_3 playedBy T644sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team644sl4_3.this.toString() + \".Role644sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team644sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T644sl4_6.java",
			    "\n" +
			    "public class T644sl4_6 extends T644sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl4_2.java",
			    "\n" +
			    "public abstract class T644sl4_2 extends T644sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T644sl4_3.java",
			    "\n" +
			    "public class T644sl4_3 extends T644sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T644sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team644sl4_4.Role644sl4_3");
    }
}
