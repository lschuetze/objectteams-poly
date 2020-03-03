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

public class SmartLifting1 extends AbstractOTJLDTest {
	
	public SmartLifting1(String name) {
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
		return SmartLifting1.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-1
    public void test641_smartLifting1() {
       
       runConformTest(
            new String[] {
		"T641sl1Main.java",
			    "\n" +
			    "public class T641sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl1_1 t = new Team641sl1_1();\n" +
			    "        T641sl1_1    o = new T641sl1_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl1_4.java",
			    "\n" +
			    "public team class Team641sl1_4 extends Team641sl1_3 {\n" +
			    "    public class Role641sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl1_4.this.toString() + \".Role641sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl1_3.java",
			    "\n" +
			    "public team class Team641sl1_3 extends Team641sl1_2 {\n" +
			    "    public class Role641sl1_5 extends Role641sl1_3 playedBy T641sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl1_3.this.toString() + \".Role641sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl1_6.java",
			    "\n" +
			    "public class T641sl1_6 extends T641sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl1_2.java",
			    "\n" +
			    "public abstract class T641sl1_2 extends T641sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl1_3.java",
			    "\n" +
			    "public class T641sl1_3 extends T641sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl1_1.java",
			    "\n" +
			    "public team class Team641sl1_1 {\n" +
			    "    public class Role641sl1_1 extends T641sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl1_1.this.toString() + \".Role641sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl1_2 extends Role641sl1_1 playedBy T641sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl1_1.this.toString() + \".Role641sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl1_3 extends Role641sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl1_1.this.toString() + \".Role641sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl1_1 as Role641sl1_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl1_4.java",
			    "\n" +
			    "public class T641sl1_4 extends T641sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl1_2.java",
			    "\n" +
			    "public team class Team641sl1_2 extends Team641sl1_1 {\n" +
			    "    public class Role641sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl1_2.this.toString() + \".Role641sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl1_4 extends Role641sl1_3 playedBy T641sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl1_2.this.toString() + \".Role641sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl1_5.java",
			    "\n" +
			    "public class T641sl1_5 extends T641sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl1_1.java",
			    "\n" +
			    "public class T641sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl1_1.Role641sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-2
    public void test641_smartLifting2() {
       
       runConformTest(
            new String[] {
		"T641sl2Main.java",
			    "\n" +
			    "public class T641sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl2_1 t = new Team641sl2_2();\n" +
			    "        T641sl2_1    o = new T641sl2_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl2_4.java",
			    "\n" +
			    "public team class Team641sl2_4 extends Team641sl2_3 {\n" +
			    "    public class Role641sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl2_4.this.toString() + \".Role641sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl2_2.java",
			    "\n" +
			    "public team class Team641sl2_2 extends Team641sl2_1 {\n" +
			    "    public class Role641sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl2_2.this.toString() + \".Role641sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl2_4 extends Role641sl2_3 playedBy T641sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl2_2.this.toString() + \".Role641sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl2_5.java",
			    "\n" +
			    "public class T641sl2_5 extends T641sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl2_1.java",
			    "\n" +
			    "public class T641sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl2_3.java",
			    "\n" +
			    "public team class Team641sl2_3 extends Team641sl2_2 {\n" +
			    "    public class Role641sl2_5 extends Role641sl2_3 playedBy T641sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl2_3.this.toString() + \".Role641sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl2_6.java",
			    "\n" +
			    "public class T641sl2_6 extends T641sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl2_2.java",
			    "\n" +
			    "public abstract class T641sl2_2 extends T641sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl2_3.java",
			    "\n" +
			    "public class T641sl2_3 extends T641sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl2_1.java",
			    "\n" +
			    "public team class Team641sl2_1 {\n" +
			    "    public class Role641sl2_1 extends T641sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl2_1.this.toString() + \".Role641sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl2_2 extends Role641sl2_1 playedBy T641sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl2_1.this.toString() + \".Role641sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl2_3 extends Role641sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl2_1.this.toString() + \".Role641sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl2_1 as Role641sl2_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl2_4.java",
			    "\n" +
			    "public class T641sl2_4 extends T641sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl2_2.Role641sl2_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-3
    public void test641_smartLifting3() {
       
       runConformTest(
            new String[] {
		"T641sl3Main.java",
			    "\n" +
			    "public class T641sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl3_1 t = new Team641sl3_3();\n" +
			    "        T641sl3_1    o = new T641sl3_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl3_4.java",
			    "\n" +
			    "public team class Team641sl3_4 extends Team641sl3_3 {\n" +
			    "    public class Role641sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl3_4.this.toString() + \".Role641sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl3_1.java",
			    "\n" +
			    "public team class Team641sl3_1 {\n" +
			    "    public class Role641sl3_1 extends T641sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl3_1.this.toString() + \".Role641sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl3_2 extends Role641sl3_1 playedBy T641sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl3_1.this.toString() + \".Role641sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl3_3 extends Role641sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl3_1.this.toString() + \".Role641sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl3_1 as Role641sl3_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl3_4.java",
			    "\n" +
			    "public class T641sl3_4 extends T641sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl3_2.java",
			    "\n" +
			    "public team class Team641sl3_2 extends Team641sl3_1 {\n" +
			    "    public class Role641sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl3_2.this.toString() + \".Role641sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl3_4 extends Role641sl3_3 playedBy T641sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl3_2.this.toString() + \".Role641sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl3_5.java",
			    "\n" +
			    "public class T641sl3_5 extends T641sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl3_1.java",
			    "\n" +
			    "public class T641sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl3_3.java",
			    "\n" +
			    "public team class Team641sl3_3 extends Team641sl3_2 {\n" +
			    "    public class Role641sl3_5 extends Role641sl3_3 playedBy T641sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl3_3.this.toString() + \".Role641sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl3_6.java",
			    "\n" +
			    "public class T641sl3_6 extends T641sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl3_2.java",
			    "\n" +
			    "public abstract class T641sl3_2 extends T641sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl3_3.java",
			    "\n" +
			    "public class T641sl3_3 extends T641sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl3_3.Role641sl3_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-4
    public void test641_smartLifting4() {
       
       runConformTest(
            new String[] {
		"T641sl4Main.java",
			    "\n" +
			    "public class T641sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl4_1 t = new Team641sl4_4();\n" +
			    "        T641sl4_1    o = new T641sl4_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl4_4.java",
			    "\n" +
			    "public team class Team641sl4_4 extends Team641sl4_3 {\n" +
			    "    public class Role641sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl4_4.this.toString() + \".Role641sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl4_3.java",
			    "\n" +
			    "public class T641sl4_3 extends T641sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl4_1.java",
			    "\n" +
			    "public team class Team641sl4_1 {\n" +
			    "    public class Role641sl4_1 extends T641sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl4_1.this.toString() + \".Role641sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl4_2 extends Role641sl4_1 playedBy T641sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl4_1.this.toString() + \".Role641sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl4_3 extends Role641sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl4_1.this.toString() + \".Role641sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl4_1 as Role641sl4_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl4_4.java",
			    "\n" +
			    "public class T641sl4_4 extends T641sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl4_2.java",
			    "\n" +
			    "public team class Team641sl4_2 extends Team641sl4_1 {\n" +
			    "    public class Role641sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl4_2.this.toString() + \".Role641sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl4_4 extends Role641sl4_3 playedBy T641sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl4_2.this.toString() + \".Role641sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl4_5.java",
			    "\n" +
			    "public class T641sl4_5 extends T641sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl4_1.java",
			    "\n" +
			    "public class T641sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl4_3.java",
			    "\n" +
			    "public team class Team641sl4_3 extends Team641sl4_2 {\n" +
			    "    public class Role641sl4_5 extends Role641sl4_3 playedBy T641sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl4_3.this.toString() + \".Role641sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl4_6.java",
			    "\n" +
			    "public class T641sl4_6 extends T641sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl4_2.java",
			    "\n" +
			    "public abstract class T641sl4_2 extends T641sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl4_4.Role641sl4_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-5
    public void test641_smartLifting5() {
       
       runConformTest(
            new String[] {
		"T641sl5Main.java",
			    "\n" +
			    "public class T641sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl5_1 t = new Team641sl5_1();\n" +
			    "        T641sl5_1    o = new T641sl5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl5_4.java",
			    "\n" +
			    "public team class Team641sl5_4 extends Team641sl5_3 {\n" +
			    "    public class Role641sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl5_4.this.toString() + \".Role641sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl5_3.java",
			    "\n" +
			    "public team class Team641sl5_3 extends Team641sl5_2 {\n" +
			    "    public class Role641sl5_5 extends Role641sl5_3 playedBy T641sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl5_3.this.toString() + \".Role641sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl5_6.java",
			    "\n" +
			    "public class T641sl5_6 extends T641sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl5_2.java",
			    "\n" +
			    "public abstract class T641sl5_2 extends T641sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl5_3.java",
			    "\n" +
			    "public class T641sl5_3 extends T641sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl5_1.java",
			    "\n" +
			    "public team class Team641sl5_1 {\n" +
			    "    public class Role641sl5_1 extends T641sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl5_1.this.toString() + \".Role641sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl5_2 extends Role641sl5_1 playedBy T641sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl5_1.this.toString() + \".Role641sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl5_3 extends Role641sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl5_1.this.toString() + \".Role641sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl5_1 as Role641sl5_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl5_4.java",
			    "\n" +
			    "public class T641sl5_4 extends T641sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl5_2.java",
			    "\n" +
			    "public team class Team641sl5_2 extends Team641sl5_1 {\n" +
			    "    public class Role641sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl5_2.this.toString() + \".Role641sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl5_4 extends Role641sl5_3 playedBy T641sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl5_2.this.toString() + \".Role641sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl5_5.java",
			    "\n" +
			    "public class T641sl5_5 extends T641sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl5_1.java",
			    "\n" +
			    "public class T641sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl5_1.Role641sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-6
    public void test641_smartLifting6() {
       
       runConformTest(
            new String[] {
		"T641sl6Main.java",
			    "\n" +
			    "public class T641sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl6_1 t = new Team641sl6_2();\n" +
			    "        T641sl6_1    o = new T641sl6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl6_4.java",
			    "\n" +
			    "public team class Team641sl6_4 extends Team641sl6_3 {\n" +
			    "    public class Role641sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl6_4.this.toString() + \".Role641sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl6_2.java",
			    "\n" +
			    "public team class Team641sl6_2 extends Team641sl6_1 {\n" +
			    "    public class Role641sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl6_2.this.toString() + \".Role641sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl6_4 extends Role641sl6_3 playedBy T641sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl6_2.this.toString() + \".Role641sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl6_5.java",
			    "\n" +
			    "public class T641sl6_5 extends T641sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl6_1.java",
			    "\n" +
			    "public class T641sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl6_3.java",
			    "\n" +
			    "public team class Team641sl6_3 extends Team641sl6_2 {\n" +
			    "    public class Role641sl6_5 extends Role641sl6_3 playedBy T641sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl6_3.this.toString() + \".Role641sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl6_6.java",
			    "\n" +
			    "public class T641sl6_6 extends T641sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl6_2.java",
			    "\n" +
			    "public abstract class T641sl6_2 extends T641sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl6_3.java",
			    "\n" +
			    "public class T641sl6_3 extends T641sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl6_1.java",
			    "\n" +
			    "public team class Team641sl6_1 {\n" +
			    "    public class Role641sl6_1 extends T641sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl6_1.this.toString() + \".Role641sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl6_2 extends Role641sl6_1 playedBy T641sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl6_1.this.toString() + \".Role641sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl6_3 extends Role641sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl6_1.this.toString() + \".Role641sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl6_1 as Role641sl6_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl6_4.java",
			    "\n" +
			    "public class T641sl6_4 extends T641sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl6_2.Role641sl6_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-7
    public void test641_smartLifting7() {
       
       runConformTest(
            new String[] {
		"T641sl7Main.java",
			    "\n" +
			    "public class T641sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl7_1 t = new Team641sl7_3();\n" +
			    "        T641sl7_1    o = new T641sl7_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl7_4.java",
			    "\n" +
			    "public team class Team641sl7_4 extends Team641sl7_3 {\n" +
			    "    public class Role641sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl7_4.this.toString() + \".Role641sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl7_1.java",
			    "\n" +
			    "public team class Team641sl7_1 {\n" +
			    "    public class Role641sl7_1 extends T641sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl7_1.this.toString() + \".Role641sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl7_2 extends Role641sl7_1 playedBy T641sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl7_1.this.toString() + \".Role641sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl7_3 extends Role641sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl7_1.this.toString() + \".Role641sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl7_1 as Role641sl7_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl7_4.java",
			    "\n" +
			    "public class T641sl7_4 extends T641sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl7_2.java",
			    "\n" +
			    "public team class Team641sl7_2 extends Team641sl7_1 {\n" +
			    "    public class Role641sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl7_2.this.toString() + \".Role641sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl7_4 extends Role641sl7_3 playedBy T641sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl7_2.this.toString() + \".Role641sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl7_5.java",
			    "\n" +
			    "public class T641sl7_5 extends T641sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl7_1.java",
			    "\n" +
			    "public class T641sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl7_3.java",
			    "\n" +
			    "public team class Team641sl7_3 extends Team641sl7_2 {\n" +
			    "    public class Role641sl7_5 extends Role641sl7_3 playedBy T641sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl7_3.this.toString() + \".Role641sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl7_6.java",
			    "\n" +
			    "public class T641sl7_6 extends T641sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl7_2.java",
			    "\n" +
			    "public abstract class T641sl7_2 extends T641sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl7_3.java",
			    "\n" +
			    "public class T641sl7_3 extends T641sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl7_3.Role641sl7_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-8
    public void test641_smartLifting8() {
       
       runConformTest(
            new String[] {
		"T641sl8Main.java",
			    "\n" +
			    "public class T641sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl8_1 t = new Team641sl8_4();\n" +
			    "        T641sl8_1    o = new T641sl8_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl8_4.java",
			    "\n" +
			    "public team class Team641sl8_4 extends Team641sl8_3 {\n" +
			    "    public class Role641sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl8_4.this.toString() + \".Role641sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl8_3.java",
			    "\n" +
			    "public class T641sl8_3 extends T641sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl8_1.java",
			    "\n" +
			    "public team class Team641sl8_1 {\n" +
			    "    public class Role641sl8_1 extends T641sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl8_1.this.toString() + \".Role641sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl8_2 extends Role641sl8_1 playedBy T641sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl8_1.this.toString() + \".Role641sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl8_3 extends Role641sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl8_1.this.toString() + \".Role641sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl8_1 as Role641sl8_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl8_4.java",
			    "\n" +
			    "public class T641sl8_4 extends T641sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl8_2.java",
			    "\n" +
			    "public team class Team641sl8_2 extends Team641sl8_1 {\n" +
			    "    public class Role641sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl8_2.this.toString() + \".Role641sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl8_4 extends Role641sl8_3 playedBy T641sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl8_2.this.toString() + \".Role641sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl8_5.java",
			    "\n" +
			    "public class T641sl8_5 extends T641sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl8_1.java",
			    "\n" +
			    "public class T641sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl8_3.java",
			    "\n" +
			    "public team class Team641sl8_3 extends Team641sl8_2 {\n" +
			    "    public class Role641sl8_5 extends Role641sl8_3 playedBy T641sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl8_3.this.toString() + \".Role641sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl8_6.java",
			    "\n" +
			    "public class T641sl8_6 extends T641sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl8_2.java",
			    "\n" +
			    "public abstract class T641sl8_2 extends T641sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl8_4.Role641sl8_4");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-9
    public void test641_smartLifting9() {
       
       runConformTest(
            new String[] {
		"T641sl9Main.java",
			    "\n" +
			    "public class T641sl9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl9_1 t = new Team641sl9_1();\n" +
			    "        T641sl9_1    o = new T641sl9_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl9_4.java",
			    "\n" +
			    "public team class Team641sl9_4 extends Team641sl9_3 {\n" +
			    "    public class Role641sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl9_4.this.toString() + \".Role641sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl9_3.java",
			    "\n" +
			    "public team class Team641sl9_3 extends Team641sl9_2 {\n" +
			    "    public class Role641sl9_5 extends Role641sl9_3 playedBy T641sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl9_3.this.toString() + \".Role641sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl9_6.java",
			    "\n" +
			    "public class T641sl9_6 extends T641sl9_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl9_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl9_2.java",
			    "\n" +
			    "public abstract class T641sl9_2 extends T641sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl9_3.java",
			    "\n" +
			    "public class T641sl9_3 extends T641sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl9_1.java",
			    "\n" +
			    "public team class Team641sl9_1 {\n" +
			    "    public class Role641sl9_1 extends T641sl9_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl9_1.this.toString() + \".Role641sl9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl9_2 extends Role641sl9_1 playedBy T641sl9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl9_1.this.toString() + \".Role641sl9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl9_3 extends Role641sl9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl9_1.this.toString() + \".Role641sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl9_1 as Role641sl9_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl9_4.java",
			    "\n" +
			    "public class T641sl9_4 extends T641sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl9_2.java",
			    "\n" +
			    "public team class Team641sl9_2 extends Team641sl9_1 {\n" +
			    "    public class Role641sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl9_2.this.toString() + \".Role641sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl9_4 extends Role641sl9_3 playedBy T641sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl9_2.this.toString() + \".Role641sl9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl9_5.java",
			    "\n" +
			    "public class T641sl9_5 extends T641sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl9_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl9_1.java",
			    "\n" +
			    "public class T641sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl9_1.Role641sl9_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-10
    public void test641_smartLifting10() {
       
       runConformTest(
            new String[] {
		"T641sl10Main.java",
			    "\n" +
			    "public class T641sl10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl10_1 t = new Team641sl10_2();\n" +
			    "        T641sl10_1    o = new T641sl10_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl10_4.java",
			    "\n" +
			    "public team class Team641sl10_4 extends Team641sl10_3 {\n" +
			    "    public class Role641sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl10_4.this.toString() + \".Role641sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl10_3.java",
			    "\n" +
			    "public team class Team641sl10_3 extends Team641sl10_2 {\n" +
			    "    public class Role641sl10_5 extends Role641sl10_3 playedBy T641sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl10_3.this.toString() + \".Role641sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl10_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl10_6.java",
			    "\n" +
			    "public class T641sl10_6 extends T641sl10_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl10_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl10_2.java",
			    "\n" +
			    "public abstract class T641sl10_2 extends T641sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl10_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl10_3.java",
			    "\n" +
			    "public class T641sl10_3 extends T641sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl10_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl10_1.java",
			    "\n" +
			    "public team class Team641sl10_1 {\n" +
			    "    public class Role641sl10_1 extends T641sl10_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl10_1.this.toString() + \".Role641sl10_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl10_2 extends Role641sl10_1 playedBy T641sl10_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl10_1.this.toString() + \".Role641sl10_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl10_3 extends Role641sl10_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl10_1.this.toString() + \".Role641sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl10_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl10_1 as Role641sl10_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl10_4.java",
			    "\n" +
			    "public class T641sl10_4 extends T641sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl10_2.java",
			    "\n" +
			    "public team class Team641sl10_2 extends Team641sl10_1 {\n" +
			    "    public class Role641sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl10_2.this.toString() + \".Role641sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl10_4 extends Role641sl10_3 playedBy T641sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl10_2.this.toString() + \".Role641sl10_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl10_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl10_5.java",
			    "\n" +
			    "public class T641sl10_5 extends T641sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl10_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl10_1.java",
			    "\n" +
			    "public class T641sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl10_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl10_2.Role641sl10_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-11
    public void test641_smartLifting11() {
       
       runConformTest(
            new String[] {
		"T641sl11Main.java",
			    "\n" +
			    "public class T641sl11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl11_1 t = new Team641sl11_3();\n" +
			    "        T641sl11_1    o = new T641sl11_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl11_4.java",
			    "\n" +
			    "public team class Team641sl11_4 extends Team641sl11_3 {\n" +
			    "    public class Role641sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl11_4.this.toString() + \".Role641sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl11_2.java",
			    "\n" +
			    "public team class Team641sl11_2 extends Team641sl11_1 {\n" +
			    "    public class Role641sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl11_2.this.toString() + \".Role641sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl11_4 extends Role641sl11_3 playedBy T641sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl11_2.this.toString() + \".Role641sl11_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl11_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl11_5.java",
			    "\n" +
			    "public class T641sl11_5 extends T641sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl11_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl11_1.java",
			    "\n" +
			    "public class T641sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl11_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl11_3.java",
			    "\n" +
			    "public team class Team641sl11_3 extends Team641sl11_2 {\n" +
			    "    public class Role641sl11_5 extends Role641sl11_3 playedBy T641sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl11_3.this.toString() + \".Role641sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl11_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl11_6.java",
			    "\n" +
			    "public class T641sl11_6 extends T641sl11_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl11_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl11_2.java",
			    "\n" +
			    "public abstract class T641sl11_2 extends T641sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl11_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl11_3.java",
			    "\n" +
			    "public class T641sl11_3 extends T641sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl11_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl11_1.java",
			    "\n" +
			    "public team class Team641sl11_1 {\n" +
			    "    public class Role641sl11_1 extends T641sl11_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl11_1.this.toString() + \".Role641sl11_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl11_2 extends Role641sl11_1 playedBy T641sl11_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl11_1.this.toString() + \".Role641sl11_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl11_3 extends Role641sl11_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl11_1.this.toString() + \".Role641sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl11_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl11_1 as Role641sl11_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl11_4.java",
			    "\n" +
			    "public class T641sl11_4 extends T641sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl11_3.Role641sl11_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-12
    public void test641_smartLifting12() {
       
       runConformTest(
            new String[] {
		"T641sl12Main.java",
			    "\n" +
			    "public class T641sl12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl12_1 t = new Team641sl12_4();\n" +
			    "        T641sl12_1    o = new T641sl12_4();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl12_4.java",
			    "\n" +
			    "public team class Team641sl12_4 extends Team641sl12_3 {\n" +
			    "    public class Role641sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl12_4.this.toString() + \".Role641sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl12_1.java",
			    "\n" +
			    "public team class Team641sl12_1 {\n" +
			    "    public class Role641sl12_1 extends T641sl12_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl12_1.this.toString() + \".Role641sl12_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl12_2 extends Role641sl12_1 playedBy T641sl12_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl12_1.this.toString() + \".Role641sl12_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl12_3 extends Role641sl12_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl12_1.this.toString() + \".Role641sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl12_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl12_1 as Role641sl12_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl12_4.java",
			    "\n" +
			    "public class T641sl12_4 extends T641sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl12_2.java",
			    "\n" +
			    "public team class Team641sl12_2 extends Team641sl12_1 {\n" +
			    "    public class Role641sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl12_2.this.toString() + \".Role641sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl12_4 extends Role641sl12_3 playedBy T641sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl12_2.this.toString() + \".Role641sl12_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl12_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl12_5.java",
			    "\n" +
			    "public class T641sl12_5 extends T641sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl12_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl12_1.java",
			    "\n" +
			    "public class T641sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl12_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl12_3.java",
			    "\n" +
			    "public team class Team641sl12_3 extends Team641sl12_2 {\n" +
			    "    public class Role641sl12_5 extends Role641sl12_3 playedBy T641sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl12_3.this.toString() + \".Role641sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl12_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl12_6.java",
			    "\n" +
			    "public class T641sl12_6 extends T641sl12_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl12_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl12_2.java",
			    "\n" +
			    "public abstract class T641sl12_2 extends T641sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl12_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl12_3.java",
			    "\n" +
			    "public class T641sl12_3 extends T641sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl12_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl12_4.Role641sl12_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-13
    public void test641_smartLifting13() {
       
       runConformTest(
            new String[] {
		"T641sl13Main.java",
			    "\n" +
			    "public class T641sl13Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl13_1 t = new Team641sl13_1();\n" +
			    "        T641sl13_1    o = new T641sl13_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl13_4.java",
			    "\n" +
			    "public team class Team641sl13_4 extends Team641sl13_3 {\n" +
			    "    public class Role641sl13_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl13_4.this.toString() + \".Role641sl13_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl13_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl13_3.java",
			    "\n" +
			    "public class T641sl13_3 extends T641sl13_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl13_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl13_1.java",
			    "\n" +
			    "public team class Team641sl13_1 {\n" +
			    "    public class Role641sl13_1 extends T641sl13_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl13_1.this.toString() + \".Role641sl13_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl13_2 extends Role641sl13_1 playedBy T641sl13_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl13_1.this.toString() + \".Role641sl13_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl13_3 extends Role641sl13_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl13_1.this.toString() + \".Role641sl13_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl13_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl13_1 as Role641sl13_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl13_4.java",
			    "\n" +
			    "public class T641sl13_4 extends T641sl13_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl13_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl13_2.java",
			    "\n" +
			    "public team class Team641sl13_2 extends Team641sl13_1 {\n" +
			    "    public class Role641sl13_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl13_2.this.toString() + \".Role641sl13_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl13_4 extends Role641sl13_3 playedBy T641sl13_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl13_2.this.toString() + \".Role641sl13_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl13_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl13_5.java",
			    "\n" +
			    "public class T641sl13_5 extends T641sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl13_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl13_1.java",
			    "\n" +
			    "public class T641sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl13_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl13_3.java",
			    "\n" +
			    "public team class Team641sl13_3 extends Team641sl13_2 {\n" +
			    "    public class Role641sl13_5 extends Role641sl13_3 playedBy T641sl13_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl13_3.this.toString() + \".Role641sl13_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl13_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl13_6.java",
			    "\n" +
			    "public class T641sl13_6 extends T641sl13_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl13_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl13_2.java",
			    "\n" +
			    "public abstract class T641sl13_2 extends T641sl13_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl13_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl13_1.Role641sl13_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-14
    public void test641_smartLifting14() {
       
       runConformTest(
            new String[] {
		"T641sl14Main.java",
			    "\n" +
			    "public class T641sl14Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl14_1 t = new Team641sl14_2();\n" +
			    "        T641sl14_1    o = new T641sl14_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl14_4.java",
			    "\n" +
			    "public team class Team641sl14_4 extends Team641sl14_3 {\n" +
			    "    public class Role641sl14_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl14_4.this.toString() + \".Role641sl14_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl14_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl14_3.java",
			    "\n" +
			    "public team class Team641sl14_3 extends Team641sl14_2 {\n" +
			    "    public class Role641sl14_5 extends Role641sl14_3 playedBy T641sl14_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl14_3.this.toString() + \".Role641sl14_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl14_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl14_6.java",
			    "\n" +
			    "public class T641sl14_6 extends T641sl14_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl14_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl14_2.java",
			    "\n" +
			    "public abstract class T641sl14_2 extends T641sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl14_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl14_3.java",
			    "\n" +
			    "public class T641sl14_3 extends T641sl14_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl14_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl14_1.java",
			    "\n" +
			    "public team class Team641sl14_1 {\n" +
			    "    public class Role641sl14_1 extends T641sl14_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl14_1.this.toString() + \".Role641sl14_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl14_2 extends Role641sl14_1 playedBy T641sl14_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl14_1.this.toString() + \".Role641sl14_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl14_3 extends Role641sl14_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl14_1.this.toString() + \".Role641sl14_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl14_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl14_1 as Role641sl14_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl14_4.java",
			    "\n" +
			    "public class T641sl14_4 extends T641sl14_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl14_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl14_2.java",
			    "\n" +
			    "public team class Team641sl14_2 extends Team641sl14_1 {\n" +
			    "    public class Role641sl14_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl14_2.this.toString() + \".Role641sl14_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl14_4 extends Role641sl14_3 playedBy T641sl14_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl14_2.this.toString() + \".Role641sl14_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl14_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl14_5.java",
			    "\n" +
			    "public class T641sl14_5 extends T641sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl14_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl14_1.java",
			    "\n" +
			    "public class T641sl14_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl14_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl14_2.Role641sl14_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-15
    public void test641_smartLifting15() {
       
       runConformTest(
            new String[] {
		"T641sl15Main.java",
			    "\n" +
			    "public class T641sl15Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl15_1 t = new Team641sl15_3();\n" +
			    "        T641sl15_1    o = new T641sl15_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl15_4.java",
			    "\n" +
			    "public team class Team641sl15_4 extends Team641sl15_3 {\n" +
			    "    public class Role641sl15_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl15_4.this.toString() + \".Role641sl15_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl15_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl15_2.java",
			    "\n" +
			    "public team class Team641sl15_2 extends Team641sl15_1 {\n" +
			    "    public class Role641sl15_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl15_2.this.toString() + \".Role641sl15_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl15_4 extends Role641sl15_3 playedBy T641sl15_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl15_2.this.toString() + \".Role641sl15_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl15_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl15_5.java",
			    "\n" +
			    "public class T641sl15_5 extends T641sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl15_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl15_1.java",
			    "\n" +
			    "public class T641sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl15_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl15_3.java",
			    "\n" +
			    "public team class Team641sl15_3 extends Team641sl15_2 {\n" +
			    "    public class Role641sl15_5 extends Role641sl15_3 playedBy T641sl15_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl15_3.this.toString() + \".Role641sl15_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl15_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl15_6.java",
			    "\n" +
			    "public class T641sl15_6 extends T641sl15_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl15_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl15_2.java",
			    "\n" +
			    "public abstract class T641sl15_2 extends T641sl15_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl15_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl15_3.java",
			    "\n" +
			    "public class T641sl15_3 extends T641sl15_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl15_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl15_1.java",
			    "\n" +
			    "public team class Team641sl15_1 {\n" +
			    "    public class Role641sl15_1 extends T641sl15_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl15_1.this.toString() + \".Role641sl15_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl15_2 extends Role641sl15_1 playedBy T641sl15_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl15_1.this.toString() + \".Role641sl15_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl15_3 extends Role641sl15_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl15_1.this.toString() + \".Role641sl15_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl15_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl15_1 as Role641sl15_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl15_4.java",
			    "\n" +
			    "public class T641sl15_4 extends T641sl15_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl15_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl15_3.Role641sl15_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-16
    public void test641_smartLifting16() {
       
       runConformTest(
            new String[] {
		"T641sl16Main.java",
			    "\n" +
			    "public class T641sl16Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl16_1 t = new Team641sl16_4();\n" +
			    "        T641sl16_1    o = new T641sl16_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl16_4.java",
			    "\n" +
			    "public team class Team641sl16_4 extends Team641sl16_3 {\n" +
			    "    public class Role641sl16_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl16_4.this.toString() + \".Role641sl16_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl16_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl16_1.java",
			    "\n" +
			    "public team class Team641sl16_1 {\n" +
			    "    public class Role641sl16_1 extends T641sl16_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl16_1.this.toString() + \".Role641sl16_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl16_2 extends Role641sl16_1 playedBy T641sl16_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl16_1.this.toString() + \".Role641sl16_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl16_3 extends Role641sl16_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl16_1.this.toString() + \".Role641sl16_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl16_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl16_1 as Role641sl16_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl16_4.java",
			    "\n" +
			    "public class T641sl16_4 extends T641sl16_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl16_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl16_2.java",
			    "\n" +
			    "public team class Team641sl16_2 extends Team641sl16_1 {\n" +
			    "    public class Role641sl16_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl16_2.this.toString() + \".Role641sl16_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl16_4 extends Role641sl16_3 playedBy T641sl16_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl16_2.this.toString() + \".Role641sl16_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl16_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl16_5.java",
			    "\n" +
			    "public class T641sl16_5 extends T641sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl16_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl16_1.java",
			    "\n" +
			    "public class T641sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl16_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl16_3.java",
			    "\n" +
			    "public team class Team641sl16_3 extends Team641sl16_2 {\n" +
			    "    public class Role641sl16_5 extends Role641sl16_3 playedBy T641sl16_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl16_3.this.toString() + \".Role641sl16_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl16_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl16_6.java",
			    "\n" +
			    "public class T641sl16_6 extends T641sl16_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl16_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl16_2.java",
			    "\n" +
			    "public abstract class T641sl16_2 extends T641sl16_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl16_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl16_3.java",
			    "\n" +
			    "public class T641sl16_3 extends T641sl16_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl16_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl16_4.Role641sl16_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-17
    public void test641_smartLifting17() {
       
       runConformTest(
            new String[] {
		"T641sl17Main.java",
			    "\n" +
			    "public class T641sl17Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl17_1 t = new Team641sl17_1();\n" +
			    "        T641sl17_1    o = new T641sl17_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl17_4.java",
			    "\n" +
			    "public team class Team641sl17_4 extends Team641sl17_3 {\n" +
			    "    public class Role641sl17_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl17_4.this.toString() + \".Role641sl17_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl17_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl17_3.java",
			    "\n" +
			    "public class T641sl17_3 extends T641sl17_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl17_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl17_1.java",
			    "\n" +
			    "public team class Team641sl17_1 {\n" +
			    "    public class Role641sl17_1 extends T641sl17_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl17_1.this.toString() + \".Role641sl17_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl17_2 extends Role641sl17_1 playedBy T641sl17_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl17_1.this.toString() + \".Role641sl17_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl17_3 extends Role641sl17_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl17_1.this.toString() + \".Role641sl17_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl17_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl17_1 as Role641sl17_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl17_4.java",
			    "\n" +
			    "public class T641sl17_4 extends T641sl17_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl17_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl17_2.java",
			    "\n" +
			    "public team class Team641sl17_2 extends Team641sl17_1 {\n" +
			    "    public class Role641sl17_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl17_2.this.toString() + \".Role641sl17_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl17_4 extends Role641sl17_3 playedBy T641sl17_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl17_2.this.toString() + \".Role641sl17_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl17_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl17_5.java",
			    "\n" +
			    "public class T641sl17_5 extends T641sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl17_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl17_1.java",
			    "\n" +
			    "public class T641sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl17_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl17_3.java",
			    "\n" +
			    "public team class Team641sl17_3 extends Team641sl17_2 {\n" +
			    "    public class Role641sl17_5 extends Role641sl17_3 playedBy T641sl17_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl17_3.this.toString() + \".Role641sl17_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl17_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl17_6.java",
			    "\n" +
			    "public class T641sl17_6 extends T641sl17_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl17_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl17_2.java",
			    "\n" +
			    "public abstract class T641sl17_2 extends T641sl17_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl17_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl17_1.Role641sl17_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-18
    public void test641_smartLifting18() {
       
       runConformTest(
            new String[] {
		"T641sl18Main.java",
			    "\n" +
			    "public class T641sl18Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl18_1 t = new Team641sl18_2();\n" +
			    "        T641sl18_1    o = new T641sl18_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl18_4.java",
			    "\n" +
			    "public team class Team641sl18_4 extends Team641sl18_3 {\n" +
			    "    public class Role641sl18_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl18_4.this.toString() + \".Role641sl18_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl18_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl18_3.java",
			    "\n" +
			    "public team class Team641sl18_3 extends Team641sl18_2 {\n" +
			    "    public class Role641sl18_5 extends Role641sl18_3 playedBy T641sl18_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl18_3.this.toString() + \".Role641sl18_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl18_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl18_6.java",
			    "\n" +
			    "public class T641sl18_6 extends T641sl18_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl18_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl18_2.java",
			    "\n" +
			    "public abstract class T641sl18_2 extends T641sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl18_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl18_3.java",
			    "\n" +
			    "public class T641sl18_3 extends T641sl18_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl18_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl18_1.java",
			    "\n" +
			    "public team class Team641sl18_1 {\n" +
			    "    public class Role641sl18_1 extends T641sl18_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl18_1.this.toString() + \".Role641sl18_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl18_2 extends Role641sl18_1 playedBy T641sl18_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl18_1.this.toString() + \".Role641sl18_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl18_3 extends Role641sl18_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl18_1.this.toString() + \".Role641sl18_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl18_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl18_1 as Role641sl18_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl18_4.java",
			    "\n" +
			    "public class T641sl18_4 extends T641sl18_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl18_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl18_2.java",
			    "\n" +
			    "public team class Team641sl18_2 extends Team641sl18_1 {\n" +
			    "    public class Role641sl18_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl18_2.this.toString() + \".Role641sl18_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl18_4 extends Role641sl18_3 playedBy T641sl18_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl18_2.this.toString() + \".Role641sl18_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl18_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl18_5.java",
			    "\n" +
			    "public class T641sl18_5 extends T641sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl18_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl18_1.java",
			    "\n" +
			    "public class T641sl18_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl18_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl18_2.Role641sl18_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-19
    public void test641_smartLifting19() {
       
       runConformTest(
            new String[] {
		"T641sl19Main.java",
			    "\n" +
			    "public class T641sl19Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl19_1 t = new Team641sl19_3();\n" +
			    "        T641sl19_1    o = new T641sl19_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl19_4.java",
			    "\n" +
			    "public team class Team641sl19_4 extends Team641sl19_3 {\n" +
			    "    public class Role641sl19_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl19_4.this.toString() + \".Role641sl19_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl19_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl19_2.java",
			    "\n" +
			    "public team class Team641sl19_2 extends Team641sl19_1 {\n" +
			    "    public class Role641sl19_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl19_2.this.toString() + \".Role641sl19_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl19_4 extends Role641sl19_3 playedBy T641sl19_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl19_2.this.toString() + \".Role641sl19_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl19_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl19_5.java",
			    "\n" +
			    "public class T641sl19_5 extends T641sl19_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl19_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl19_1.java",
			    "\n" +
			    "public class T641sl19_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl19_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl19_3.java",
			    "\n" +
			    "public team class Team641sl19_3 extends Team641sl19_2 {\n" +
			    "    public class Role641sl19_5 extends Role641sl19_3 playedBy T641sl19_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl19_3.this.toString() + \".Role641sl19_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl19_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl19_6.java",
			    "\n" +
			    "public class T641sl19_6 extends T641sl19_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl19_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl19_2.java",
			    "\n" +
			    "public abstract class T641sl19_2 extends T641sl19_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl19_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl19_3.java",
			    "\n" +
			    "public class T641sl19_3 extends T641sl19_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl19_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl19_1.java",
			    "\n" +
			    "public team class Team641sl19_1 {\n" +
			    "    public class Role641sl19_1 extends T641sl19_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl19_1.this.toString() + \".Role641sl19_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl19_2 extends Role641sl19_1 playedBy T641sl19_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl19_1.this.toString() + \".Role641sl19_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl19_3 extends Role641sl19_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl19_1.this.toString() + \".Role641sl19_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl19_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl19_1 as Role641sl19_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl19_4.java",
			    "\n" +
			    "public class T641sl19_4 extends T641sl19_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl19_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl19_3.Role641sl19_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-20
    public void test641_smartLifting20() {
       
       runConformTest(
            new String[] {
		"T641sl20Main.java",
			    "\n" +
			    "public class T641sl20Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl20_1 t = new Team641sl20_4();\n" +
			    "        T641sl20_1    o = new T641sl20_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl20_4.java",
			    "\n" +
			    "public team class Team641sl20_4 extends Team641sl20_3 {\n" +
			    "    public class Role641sl20_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl20_4.this.toString() + \".Role641sl20_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl20_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl20_2.java",
			    "\n" +
			    "public team class Team641sl20_2 extends Team641sl20_1 {\n" +
			    "    public class Role641sl20_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl20_2.this.toString() + \".Role641sl20_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl20_4 extends Role641sl20_3 playedBy T641sl20_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl20_2.this.toString() + \".Role641sl20_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl20_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl20_5.java",
			    "\n" +
			    "public class T641sl20_5 extends T641sl20_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl20_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl20_1.java",
			    "\n" +
			    "public class T641sl20_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl20_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl20_3.java",
			    "\n" +
			    "public team class Team641sl20_3 extends Team641sl20_2 {\n" +
			    "    public class Role641sl20_5 extends Role641sl20_3 playedBy T641sl20_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl20_3.this.toString() + \".Role641sl20_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl20_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl20_6.java",
			    "\n" +
			    "public class T641sl20_6 extends T641sl20_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl20_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl20_2.java",
			    "\n" +
			    "public abstract class T641sl20_2 extends T641sl20_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl20_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl20_3.java",
			    "\n" +
			    "public class T641sl20_3 extends T641sl20_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl20_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl20_1.java",
			    "\n" +
			    "public team class Team641sl20_1 {\n" +
			    "    public class Role641sl20_1 extends T641sl20_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl20_1.this.toString() + \".Role641sl20_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl20_2 extends Role641sl20_1 playedBy T641sl20_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl20_1.this.toString() + \".Role641sl20_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl20_3 extends Role641sl20_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl20_1.this.toString() + \".Role641sl20_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl20_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl20_1 as Role641sl20_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl20_4.java",
			    "\n" +
			    "public class T641sl20_4 extends T641sl20_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl20_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl20_4.Role641sl20_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-21
    public void test641_smartLifting21() {
       
       runConformTest(
            new String[] {
		"T641sl21Main.java",
			    "\n" +
			    "public class T641sl21Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl21_1       t  = new Team641sl21_1();\n" +
			    "        final Team641sl21_1 ft = new Team641sl21_1();\n" +
			    "        T641sl21_1          o  = ft.new Role641sl21_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl21_4.java",
			    "\n" +
			    "public team class Team641sl21_4 extends Team641sl21_3 {\n" +
			    "    public class Role641sl21_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl21_4.this.toString() + \".Role641sl21_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl21_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl21_1.java",
			    "\n" +
			    "public team class Team641sl21_1 {\n" +
			    "    public class Role641sl21_1 extends T641sl21_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl21_1.this.toString() + \".Role641sl21_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl21_2 extends Role641sl21_1 playedBy T641sl21_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl21_1.this.toString() + \".Role641sl21_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl21_3 extends Role641sl21_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl21_1.this.toString() + \".Role641sl21_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl21_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl21_1 as Role641sl21_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl21_4.java",
			    "\n" +
			    "public class T641sl21_4 extends T641sl21_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl21_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl21_2.java",
			    "\n" +
			    "public team class Team641sl21_2 extends Team641sl21_1 {\n" +
			    "    public class Role641sl21_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl21_2.this.toString() + \".Role641sl21_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl21_4 extends Role641sl21_3 playedBy T641sl21_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl21_2.this.toString() + \".Role641sl21_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl21_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl21_5.java",
			    "\n" +
			    "public class T641sl21_5 extends T641sl21_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl21_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl21_1.java",
			    "\n" +
			    "public class T641sl21_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl21_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl21_3.java",
			    "\n" +
			    "public team class Team641sl21_3 extends Team641sl21_2 {\n" +
			    "    public class Role641sl21_5 extends Role641sl21_3 playedBy T641sl21_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl21_3.this.toString() + \".Role641sl21_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl21_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl21_6.java",
			    "\n" +
			    "public class T641sl21_6 extends T641sl21_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl21_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl21_2.java",
			    "\n" +
			    "public abstract class T641sl21_2 extends T641sl21_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl21_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl21_3.java",
			    "\n" +
			    "public class T641sl21_3 extends T641sl21_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl21_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl21_1.Role641sl21_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-22
    public void test641_smartLifting22() {
       
       runConformTest(
            new String[] {
		"T641sl22Main.java",
			    "\n" +
			    "public class T641sl22Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl22_1       t  = new Team641sl22_2();\n" +
			    "        final Team641sl22_1 ft = new Team641sl22_1();\n" +
			    "        T641sl22_1          o  = ft.new Role641sl22_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl22_4.java",
			    "\n" +
			    "public team class Team641sl22_4 extends Team641sl22_3 {\n" +
			    "    public class Role641sl22_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl22_4.this.toString() + \".Role641sl22_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl22_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl22_3.java",
			    "\n" +
			    "public class T641sl22_3 extends T641sl22_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl22_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl22_1.java",
			    "\n" +
			    "public team class Team641sl22_1 {\n" +
			    "    public class Role641sl22_1 extends T641sl22_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl22_1.this.toString() + \".Role641sl22_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl22_2 extends Role641sl22_1 playedBy T641sl22_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl22_1.this.toString() + \".Role641sl22_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl22_3 extends Role641sl22_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl22_1.this.toString() + \".Role641sl22_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl22_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl22_1 as Role641sl22_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl22_4.java",
			    "\n" +
			    "public class T641sl22_4 extends T641sl22_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl22_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl22_2.java",
			    "\n" +
			    "public team class Team641sl22_2 extends Team641sl22_1 {\n" +
			    "    public class Role641sl22_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl22_2.this.toString() + \".Role641sl22_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl22_4 extends Role641sl22_3 playedBy T641sl22_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl22_2.this.toString() + \".Role641sl22_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl22_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl22_5.java",
			    "\n" +
			    "public class T641sl22_5 extends T641sl22_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl22_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl22_1.java",
			    "\n" +
			    "public class T641sl22_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl22_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl22_3.java",
			    "\n" +
			    "public team class Team641sl22_3 extends Team641sl22_2 {\n" +
			    "    public class Role641sl22_5 extends Role641sl22_3 playedBy T641sl22_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl22_3.this.toString() + \".Role641sl22_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl22_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl22_6.java",
			    "\n" +
			    "public class T641sl22_6 extends T641sl22_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl22_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl22_2.java",
			    "\n" +
			    "public abstract class T641sl22_2 extends T641sl22_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl22_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl22_2.Role641sl22_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-23
    public void test641_smartLifting23() {
       
       runConformTest(
            new String[] {
		"T641sl23Main.java",
			    "\n" +
			    "public class T641sl23Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl23_1       t  = new Team641sl23_3();\n" +
			    "        final Team641sl23_1 ft = new Team641sl23_1();\n" +
			    "        T641sl23_1          o  = ft.new Role641sl23_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl23_4.java",
			    "\n" +
			    "public team class Team641sl23_4 extends Team641sl23_3 {\n" +
			    "    public class Role641sl23_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl23_4.this.toString() + \".Role641sl23_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl23_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl23_3.java",
			    "\n" +
			    "public team class Team641sl23_3 extends Team641sl23_2 {\n" +
			    "    public class Role641sl23_5 extends Role641sl23_3 playedBy T641sl23_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl23_3.this.toString() + \".Role641sl23_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl23_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl23_6.java",
			    "\n" +
			    "public class T641sl23_6 extends T641sl23_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl23_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl23_2.java",
			    "\n" +
			    "public abstract class T641sl23_2 extends T641sl23_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl23_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl23_3.java",
			    "\n" +
			    "public class T641sl23_3 extends T641sl23_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl23_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl23_1.java",
			    "\n" +
			    "public team class Team641sl23_1 {\n" +
			    "    public class Role641sl23_1 extends T641sl23_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl23_1.this.toString() + \".Role641sl23_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl23_2 extends Role641sl23_1 playedBy T641sl23_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl23_1.this.toString() + \".Role641sl23_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl23_3 extends Role641sl23_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl23_1.this.toString() + \".Role641sl23_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl23_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl23_1 as Role641sl23_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl23_4.java",
			    "\n" +
			    "public class T641sl23_4 extends T641sl23_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl23_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl23_2.java",
			    "\n" +
			    "public team class Team641sl23_2 extends Team641sl23_1 {\n" +
			    "    public class Role641sl23_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl23_2.this.toString() + \".Role641sl23_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl23_4 extends Role641sl23_3 playedBy T641sl23_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl23_2.this.toString() + \".Role641sl23_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl23_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl23_5.java",
			    "\n" +
			    "public class T641sl23_5 extends T641sl23_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl23_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl23_1.java",
			    "\n" +
			    "public class T641sl23_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl23_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl23_3.Role641sl23_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.1-otjld-smart-lifting-24
    public void test641_smartLifting24() {
       
       runConformTest(
            new String[] {
		"T641sl24Main.java",
			    "\n" +
			    "public class T641sl24Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team641sl24_1       t  = new Team641sl24_4();\n" +
			    "        final Team641sl24_1 ft = new Team641sl24_1();\n" +
			    "        T641sl24_1          o  = ft.new Role641sl24_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl24_4.java",
			    "\n" +
			    "public team class Team641sl24_4 extends Team641sl24_3 {\n" +
			    "    public class Role641sl24_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl24_4.this.toString() + \".Role641sl24_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl24_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl24_2.java",
			    "\n" +
			    "public team class Team641sl24_2 extends Team641sl24_1 {\n" +
			    "    public class Role641sl24_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl24_2.this.toString() + \".Role641sl24_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role641sl24_4 extends Role641sl24_3 playedBy T641sl24_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl24_2.this.toString() + \".Role641sl24_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl24_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl24_5.java",
			    "\n" +
			    "public class T641sl24_5 extends T641sl24_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl24_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl24_1.java",
			    "\n" +
			    "public class T641sl24_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl24_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl24_3.java",
			    "\n" +
			    "public team class Team641sl24_3 extends Team641sl24_2 {\n" +
			    "    public class Role641sl24_5 extends Role641sl24_3 playedBy T641sl24_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl24_3.this.toString() + \".Role641sl24_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl24_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T641sl24_6.java",
			    "\n" +
			    "public class T641sl24_6 extends T641sl24_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl24_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl24_2.java",
			    "\n" +
			    "public abstract class T641sl24_2 extends T641sl24_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl24_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl24_3.java",
			    "\n" +
			    "public class T641sl24_3 extends T641sl24_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl24_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team641sl24_1.java",
			    "\n" +
			    "public team class Team641sl24_1 {\n" +
			    "    public class Role641sl24_1 extends T641sl24_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl24_1.this.toString() + \".Role641sl24_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role641sl24_2 extends Role641sl24_1 playedBy T641sl24_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl24_1.this.toString() + \".Role641sl24_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role641sl24_3 extends Role641sl24_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team641sl24_1.this.toString() + \".Role641sl24_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team641sl24_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T641sl24_1 as Role641sl24_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T641sl24_4.java",
			    "\n" +
			    "public class T641sl24_4 extends T641sl24_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T641sl24_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team641sl24_4.Role641sl24_5");
    }
}
