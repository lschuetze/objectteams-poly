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

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class SmartLifting5 extends AbstractOTJLDTest {
	
	public SmartLifting5(String name) {
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
		return SmartLifting5.class;
	}

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-1
    public void test645_smartLifting1() {
       
       runConformTest(
            new String[] {
		"T645sl1Main.java",
			    "\n" +
			    "public class T645sl1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl1_1 t = new Team645sl1_1();\n" +
			    "        T645sl1_5    o = new T645sl1_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl1_4.java",
			    "\n" +
			    "public team class Team645sl1_4 extends Team645sl1_3 {\n" +
			    "    public class Role645sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl1_4.this.toString() + \".Role645sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl1_3.java",
			    "\n" +
			    "public team class Team645sl1_3 extends Team645sl1_2 {\n" +
			    "    public class Role645sl1_5 extends Role645sl1_3 playedBy T645sl1_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl1_3.this.toString() + \".Role645sl1_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl1_6.java",
			    "\n" +
			    "public class T645sl1_6 extends T645sl1_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl1_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl1_2.java",
			    "\n" +
			    "public abstract class T645sl1_2 extends T645sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl1_3.java",
			    "\n" +
			    "public class T645sl1_3 extends T645sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl1_1.java",
			    "\n" +
			    "public team class Team645sl1_1 {\n" +
			    "    public class Role645sl1_1 extends T645sl1_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl1_1.this.toString() + \".Role645sl1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl1_2 extends Role645sl1_1 playedBy T645sl1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl1_1.this.toString() + \".Role645sl1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl1_3 extends Role645sl1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl1_1.this.toString() + \".Role645sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl1_5 as Role645sl1_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl1_4.java",
			    "\n" +
			    "public class T645sl1_4 extends T645sl1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl1_2.java",
			    "\n" +
			    "public team class Team645sl1_2 extends Team645sl1_1 {\n" +
			    "    public class Role645sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl1_2.this.toString() + \".Role645sl1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl1_4 extends Role645sl1_3 playedBy T645sl1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl1_2.this.toString() + \".Role645sl1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl1_5.java",
			    "\n" +
			    "public class T645sl1_5 extends T645sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl1_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl1_1.java",
			    "\n" +
			    "public class T645sl1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl1_1.Role645sl1_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-2
    public void test645_smartLifting2() {
       
       runConformTest(
            new String[] {
		"T645sl2Main.java",
			    "\n" +
			    "public class T645sl2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl2_1 t = new Team645sl2_2();\n" +
			    "        T645sl2_5    o = new T645sl2_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl2_4.java",
			    "\n" +
			    "public team class Team645sl2_4 extends Team645sl2_3 {\n" +
			    "    public class Role645sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl2_4.this.toString() + \".Role645sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl2_2.java",
			    "\n" +
			    "public team class Team645sl2_2 extends Team645sl2_1 {\n" +
			    "    public class Role645sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl2_2.this.toString() + \".Role645sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl2_4 extends Role645sl2_3 playedBy T645sl2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl2_2.this.toString() + \".Role645sl2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl2_5.java",
			    "\n" +
			    "public class T645sl2_5 extends T645sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl2_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl2_1.java",
			    "\n" +
			    "public class T645sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl2_3.java",
			    "\n" +
			    "public team class Team645sl2_3 extends Team645sl2_2 {\n" +
			    "    public class Role645sl2_5 extends Role645sl2_3 playedBy T645sl2_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl2_3.this.toString() + \".Role645sl2_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl2_6.java",
			    "\n" +
			    "public class T645sl2_6 extends T645sl2_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl2_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl2_2.java",
			    "\n" +
			    "public abstract class T645sl2_2 extends T645sl2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl2_3.java",
			    "\n" +
			    "public class T645sl2_3 extends T645sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl2_1.java",
			    "\n" +
			    "public team class Team645sl2_1 {\n" +
			    "    public class Role645sl2_1 extends T645sl2_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl2_1.this.toString() + \".Role645sl2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl2_2 extends Role645sl2_1 playedBy T645sl2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl2_1.this.toString() + \".Role645sl2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl2_3 extends Role645sl2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl2_1.this.toString() + \".Role645sl2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl2_5 as Role645sl2_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl2_4.java",
			    "\n" +
			    "public class T645sl2_4 extends T645sl2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl2_2.Role645sl2_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-3
    public void test645_smartLifting3() {
       
       runConformTest(
            new String[] {
		"T645sl3Main.java",
			    "\n" +
			    "public class T645sl3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl3_1 t = new Team645sl3_3();\n" +
			    "        T645sl3_5    o = new T645sl3_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl3_4.java",
			    "\n" +
			    "public team class Team645sl3_4 extends Team645sl3_3 {\n" +
			    "    public class Role645sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl3_4.this.toString() + \".Role645sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl3_1.java",
			    "\n" +
			    "public team class Team645sl3_1 {\n" +
			    "    public class Role645sl3_1 extends T645sl3_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl3_1.this.toString() + \".Role645sl3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl3_2 extends Role645sl3_1 playedBy T645sl3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl3_1.this.toString() + \".Role645sl3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl3_3 extends Role645sl3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl3_1.this.toString() + \".Role645sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl3_5 as Role645sl3_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl3_4.java",
			    "\n" +
			    "public class T645sl3_4 extends T645sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl3_2.java",
			    "\n" +
			    "public team class Team645sl3_2 extends Team645sl3_1 {\n" +
			    "    public class Role645sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl3_2.this.toString() + \".Role645sl3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl3_4 extends Role645sl3_3 playedBy T645sl3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl3_2.this.toString() + \".Role645sl3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl3_5.java",
			    "\n" +
			    "public class T645sl3_5 extends T645sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl3_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl3_1.java",
			    "\n" +
			    "public class T645sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl3_3.java",
			    "\n" +
			    "public team class Team645sl3_3 extends Team645sl3_2 {\n" +
			    "    public class Role645sl3_5 extends Role645sl3_3 playedBy T645sl3_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl3_3.this.toString() + \".Role645sl3_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl3_6.java",
			    "\n" +
			    "public class T645sl3_6 extends T645sl3_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl3_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl3_2.java",
			    "\n" +
			    "public abstract class T645sl3_2 extends T645sl3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl3_3.java",
			    "\n" +
			    "public class T645sl3_3 extends T645sl3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl3_3.Role645sl3_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-4
    public void test645_smartLifting4() {
       
       runConformTest(
            new String[] {
		"T645sl4Main.java",
			    "\n" +
			    "public class T645sl4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl4_1 t = new Team645sl4_4();\n" +
			    "        T645sl4_5    o = new T645sl4_5();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl4_4.java",
			    "\n" +
			    "public team class Team645sl4_4 extends Team645sl4_3 {\n" +
			    "    public class Role645sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl4_4.this.toString() + \".Role645sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl4_3.java",
			    "\n" +
			    "public class T645sl4_3 extends T645sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl4_1.java",
			    "\n" +
			    "public team class Team645sl4_1 {\n" +
			    "    public class Role645sl4_1 extends T645sl4_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl4_1.this.toString() + \".Role645sl4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl4_2 extends Role645sl4_1 playedBy T645sl4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl4_1.this.toString() + \".Role645sl4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl4_3 extends Role645sl4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl4_1.this.toString() + \".Role645sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl4_5 as Role645sl4_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl4_4.java",
			    "\n" +
			    "public class T645sl4_4 extends T645sl4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl4_2.java",
			    "\n" +
			    "public team class Team645sl4_2 extends Team645sl4_1 {\n" +
			    "    public class Role645sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl4_2.this.toString() + \".Role645sl4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl4_4 extends Role645sl4_3 playedBy T645sl4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl4_2.this.toString() + \".Role645sl4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl4_5.java",
			    "\n" +
			    "public class T645sl4_5 extends T645sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl4_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl4_1.java",
			    "\n" +
			    "public class T645sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl4_3.java",
			    "\n" +
			    "public team class Team645sl4_3 extends Team645sl4_2 {\n" +
			    "    public class Role645sl4_5 extends Role645sl4_3 playedBy T645sl4_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl4_3.this.toString() + \".Role645sl4_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl4_6.java",
			    "\n" +
			    "public class T645sl4_6 extends T645sl4_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl4_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl4_2.java",
			    "\n" +
			    "public abstract class T645sl4_2 extends T645sl4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl4_4.Role645sl4_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-5
    public void test645_smartLifting5() {
       
       runConformTest(
            new String[] {
		"T645sl5Main.java",
			    "\n" +
			    "public class T645sl5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl5_1 t = new Team645sl5_1();\n" +
			    "        T645sl5_5    o = new T645sl5_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl5_4.java",
			    "\n" +
			    "public team class Team645sl5_4 extends Team645sl5_3 {\n" +
			    "    public class Role645sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl5_4.this.toString() + \".Role645sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl5_3.java",
			    "\n" +
			    "public team class Team645sl5_3 extends Team645sl5_2 {\n" +
			    "    public class Role645sl5_5 extends Role645sl5_3 playedBy T645sl5_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl5_3.this.toString() + \".Role645sl5_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl5_6.java",
			    "\n" +
			    "public class T645sl5_6 extends T645sl5_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl5_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl5_2.java",
			    "\n" +
			    "public abstract class T645sl5_2 extends T645sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl5_3.java",
			    "\n" +
			    "public class T645sl5_3 extends T645sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl5_1.java",
			    "\n" +
			    "public team class Team645sl5_1 {\n" +
			    "    public class Role645sl5_1 extends T645sl5_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl5_1.this.toString() + \".Role645sl5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl5_2 extends Role645sl5_1 playedBy T645sl5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl5_1.this.toString() + \".Role645sl5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl5_3 extends Role645sl5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl5_1.this.toString() + \".Role645sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl5_5 as Role645sl5_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl5_4.java",
			    "\n" +
			    "public class T645sl5_4 extends T645sl5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl5_2.java",
			    "\n" +
			    "public team class Team645sl5_2 extends Team645sl5_1 {\n" +
			    "    public class Role645sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl5_2.this.toString() + \".Role645sl5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl5_4 extends Role645sl5_3 playedBy T645sl5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl5_2.this.toString() + \".Role645sl5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl5_5.java",
			    "\n" +
			    "public class T645sl5_5 extends T645sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl5_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl5_1.java",
			    "\n" +
			    "public class T645sl5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl5_1.Role645sl5_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-6
    public void test645_smartLifting6() {
       
       runConformTest(
            new String[] {
		"T645sl6Main.java",
			    "\n" +
			    "public class T645sl6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl6_1 t = new Team645sl6_2();\n" +
			    "        T645sl6_5    o = new T645sl6_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl6_4.java",
			    "\n" +
			    "public team class Team645sl6_4 extends Team645sl6_3 {\n" +
			    "    public class Role645sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl6_4.this.toString() + \".Role645sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl6_2.java",
			    "\n" +
			    "public team class Team645sl6_2 extends Team645sl6_1 {\n" +
			    "    public class Role645sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl6_2.this.toString() + \".Role645sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl6_4 extends Role645sl6_3 playedBy T645sl6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl6_2.this.toString() + \".Role645sl6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl6_5.java",
			    "\n" +
			    "public class T645sl6_5 extends T645sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl6_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl6_1.java",
			    "\n" +
			    "public class T645sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl6_3.java",
			    "\n" +
			    "public team class Team645sl6_3 extends Team645sl6_2 {\n" +
			    "    public class Role645sl6_5 extends Role645sl6_3 playedBy T645sl6_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl6_3.this.toString() + \".Role645sl6_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl6_6.java",
			    "\n" +
			    "public class T645sl6_6 extends T645sl6_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl6_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl6_2.java",
			    "\n" +
			    "public abstract class T645sl6_2 extends T645sl6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl6_3.java",
			    "\n" +
			    "public class T645sl6_3 extends T645sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl6_1.java",
			    "\n" +
			    "public team class Team645sl6_1 {\n" +
			    "    public class Role645sl6_1 extends T645sl6_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl6_1.this.toString() + \".Role645sl6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl6_2 extends Role645sl6_1 playedBy T645sl6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl6_1.this.toString() + \".Role645sl6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl6_3 extends Role645sl6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl6_1.this.toString() + \".Role645sl6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl6_5 as Role645sl6_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl6_4.java",
			    "\n" +
			    "public class T645sl6_4 extends T645sl6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl6_2.Role645sl6_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-7
    public void test645_smartLifting7() {
       
       runConformTest(
            new String[] {
		"T645sl7Main.java",
			    "\n" +
			    "public class T645sl7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl7_1 t = new Team645sl7_3();\n" +
			    "        T645sl7_5    o = new T645sl7_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl7_4.java",
			    "\n" +
			    "public team class Team645sl7_4 extends Team645sl7_3 {\n" +
			    "    public class Role645sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl7_4.this.toString() + \".Role645sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl7_1.java",
			    "\n" +
			    "public team class Team645sl7_1 {\n" +
			    "    public class Role645sl7_1 extends T645sl7_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl7_1.this.toString() + \".Role645sl7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl7_2 extends Role645sl7_1 playedBy T645sl7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl7_1.this.toString() + \".Role645sl7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl7_3 extends Role645sl7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl7_1.this.toString() + \".Role645sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl7_5 as Role645sl7_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl7_4.java",
			    "\n" +
			    "public class T645sl7_4 extends T645sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl7_2.java",
			    "\n" +
			    "public team class Team645sl7_2 extends Team645sl7_1 {\n" +
			    "    public class Role645sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl7_2.this.toString() + \".Role645sl7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl7_4 extends Role645sl7_3 playedBy T645sl7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl7_2.this.toString() + \".Role645sl7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl7_5.java",
			    "\n" +
			    "public class T645sl7_5 extends T645sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl7_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl7_1.java",
			    "\n" +
			    "public class T645sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl7_3.java",
			    "\n" +
			    "public team class Team645sl7_3 extends Team645sl7_2 {\n" +
			    "    public class Role645sl7_5 extends Role645sl7_3 playedBy T645sl7_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl7_3.this.toString() + \".Role645sl7_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl7_6.java",
			    "\n" +
			    "public class T645sl7_6 extends T645sl7_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl7_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl7_2.java",
			    "\n" +
			    "public abstract class T645sl7_2 extends T645sl7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl7_3.java",
			    "\n" +
			    "public class T645sl7_3 extends T645sl7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl7_3.Role645sl7_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-8
    public void test645_smartLifting8() {
       
       runConformTest(
            new String[] {
		"T645sl8Main.java",
			    "\n" +
			    "public class T645sl8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl8_1 t = new Team645sl8_4();\n" +
			    "        T645sl8_5    o = new T645sl8_6();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl8_4.java",
			    "\n" +
			    "public team class Team645sl8_4 extends Team645sl8_3 {\n" +
			    "    public class Role645sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl8_4.this.toString() + \".Role645sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl8_3.java",
			    "\n" +
			    "public class T645sl8_3 extends T645sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl8_1.java",
			    "\n" +
			    "public team class Team645sl8_1 {\n" +
			    "    public class Role645sl8_1 extends T645sl8_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl8_1.this.toString() + \".Role645sl8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl8_2 extends Role645sl8_1 playedBy T645sl8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl8_1.this.toString() + \".Role645sl8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl8_3 extends Role645sl8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl8_1.this.toString() + \".Role645sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl8_5 as Role645sl8_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl8_4.java",
			    "\n" +
			    "public class T645sl8_4 extends T645sl8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl8_2.java",
			    "\n" +
			    "public team class Team645sl8_2 extends Team645sl8_1 {\n" +
			    "    public class Role645sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl8_2.this.toString() + \".Role645sl8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl8_4 extends Role645sl8_3 playedBy T645sl8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl8_2.this.toString() + \".Role645sl8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl8_5.java",
			    "\n" +
			    "public class T645sl8_5 extends T645sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl8_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl8_1.java",
			    "\n" +
			    "public class T645sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl8_3.java",
			    "\n" +
			    "public team class Team645sl8_3 extends Team645sl8_2 {\n" +
			    "    public class Role645sl8_5 extends Role645sl8_3 playedBy T645sl8_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl8_3.this.toString() + \".Role645sl8_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl8_6.java",
			    "\n" +
			    "public class T645sl8_6 extends T645sl8_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl8_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl8_2.java",
			    "\n" +
			    "public abstract class T645sl8_2 extends T645sl8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl8_4.Role645sl8_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-9
    public void test645_smartLifting9() {
       
       runConformTest(
            new String[] {
		"T645sl9Main.java",
			    "\n" +
			    "public class T645sl9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl9_1       t  = new Team645sl9_1();\n" +
			    "        final Team645sl9_1 ft = new Team645sl9_1();\n" +
			    "        T645sl9_5          o  = ft.new Role645sl9_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl9_4.java",
			    "\n" +
			    "public team class Team645sl9_4 extends Team645sl9_3 {\n" +
			    "    public class Role645sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl9_4.this.toString() + \".Role645sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl9_3.java",
			    "\n" +
			    "public team class Team645sl9_3 extends Team645sl9_2 {\n" +
			    "    public class Role645sl9_5 extends Role645sl9_3 playedBy T645sl9_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl9_3.this.toString() + \".Role645sl9_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl9_6.java",
			    "\n" +
			    "public class T645sl9_6 extends T645sl9_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl9_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl9_2.java",
			    "\n" +
			    "public abstract class T645sl9_2 extends T645sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl9_3.java",
			    "\n" +
			    "public class T645sl9_3 extends T645sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl9_1.java",
			    "\n" +
			    "public team class Team645sl9_1 {\n" +
			    "    public class Role645sl9_1 extends T645sl9_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl9_1.this.toString() + \".Role645sl9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl9_2 extends Role645sl9_1 playedBy T645sl9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl9_1.this.toString() + \".Role645sl9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl9_3 extends Role645sl9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl9_1.this.toString() + \".Role645sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl9_5 as Role645sl9_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl9_4.java",
			    "\n" +
			    "public class T645sl9_4 extends T645sl9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl9_2.java",
			    "\n" +
			    "public team class Team645sl9_2 extends Team645sl9_1 {\n" +
			    "    public class Role645sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl9_2.this.toString() + \".Role645sl9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl9_4 extends Role645sl9_3 playedBy T645sl9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl9_2.this.toString() + \".Role645sl9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl9_5.java",
			    "\n" +
			    "public class T645sl9_5 extends T645sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl9_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl9_1.java",
			    "\n" +
			    "public class T645sl9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl9_1.Role645sl9_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-10
    public void test645_smartLifting10() {
       
       runConformTest(
            new String[] {
		"T645sl10Main.java",
			    "\n" +
			    "public class T645sl10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl10_1       t  = new Team645sl10_2();\n" +
			    "        final Team645sl10_1 ft = new Team645sl10_1();\n" +
			    "        T645sl10_5          o  = ft.new Role645sl10_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl10_4.java",
			    "\n" +
			    "public team class Team645sl10_4 extends Team645sl10_3 {\n" +
			    "    public class Role645sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl10_4.this.toString() + \".Role645sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl10_3.java",
			    "\n" +
			    "public team class Team645sl10_3 extends Team645sl10_2 {\n" +
			    "    public class Role645sl10_5 extends Role645sl10_3 playedBy T645sl10_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl10_3.this.toString() + \".Role645sl10_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl10_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl10_6.java",
			    "\n" +
			    "public class T645sl10_6 extends T645sl10_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl10_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl10_2.java",
			    "\n" +
			    "public abstract class T645sl10_2 extends T645sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl10_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl10_3.java",
			    "\n" +
			    "public class T645sl10_3 extends T645sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl10_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl10_1.java",
			    "\n" +
			    "public team class Team645sl10_1 {\n" +
			    "    public class Role645sl10_1 extends T645sl10_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl10_1.this.toString() + \".Role645sl10_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl10_2 extends Role645sl10_1 playedBy T645sl10_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl10_1.this.toString() + \".Role645sl10_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl10_3 extends Role645sl10_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl10_1.this.toString() + \".Role645sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl10_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl10_5 as Role645sl10_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl10_4.java",
			    "\n" +
			    "public class T645sl10_4 extends T645sl10_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl10_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl10_2.java",
			    "\n" +
			    "public team class Team645sl10_2 extends Team645sl10_1 {\n" +
			    "    public class Role645sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl10_2.this.toString() + \".Role645sl10_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl10_4 extends Role645sl10_3 playedBy T645sl10_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl10_2.this.toString() + \".Role645sl10_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl10_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl10_5.java",
			    "\n" +
			    "public class T645sl10_5 extends T645sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl10_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl10_1.java",
			    "\n" +
			    "public class T645sl10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl10_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl10_2.Role645sl10_3");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-11
    public void test645_smartLifting11() {
       
       runConformTest(
            new String[] {
		"T645sl11Main.java",
			    "\n" +
			    "public class T645sl11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl11_1       t  = new Team645sl11_3();\n" +
			    "        final Team645sl11_1 ft = new Team645sl11_1();\n" +
			    "        T645sl11_5          o  = ft.new Role645sl11_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl11_4.java",
			    "\n" +
			    "public team class Team645sl11_4 extends Team645sl11_3 {\n" +
			    "    public class Role645sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl11_4.this.toString() + \".Role645sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl11_2.java",
			    "\n" +
			    "public team class Team645sl11_2 extends Team645sl11_1 {\n" +
			    "    public class Role645sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl11_2.this.toString() + \".Role645sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl11_4 extends Role645sl11_3 playedBy T645sl11_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl11_2.this.toString() + \".Role645sl11_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl11_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl11_5.java",
			    "\n" +
			    "public class T645sl11_5 extends T645sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl11_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl11_1.java",
			    "\n" +
			    "public class T645sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl11_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl11_3.java",
			    "\n" +
			    "public team class Team645sl11_3 extends Team645sl11_2 {\n" +
			    "    public class Role645sl11_5 extends Role645sl11_3 playedBy T645sl11_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl11_3.this.toString() + \".Role645sl11_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl11_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl11_6.java",
			    "\n" +
			    "public class T645sl11_6 extends T645sl11_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl11_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl11_2.java",
			    "\n" +
			    "public abstract class T645sl11_2 extends T645sl11_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl11_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl11_3.java",
			    "\n" +
			    "public class T645sl11_3 extends T645sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl11_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl11_1.java",
			    "\n" +
			    "public team class Team645sl11_1 {\n" +
			    "    public class Role645sl11_1 extends T645sl11_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl11_1.this.toString() + \".Role645sl11_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl11_2 extends Role645sl11_1 playedBy T645sl11_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl11_1.this.toString() + \".Role645sl11_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl11_3 extends Role645sl11_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl11_1.this.toString() + \".Role645sl11_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl11_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl11_5 as Role645sl11_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl11_4.java",
			    "\n" +
			    "public class T645sl11_4 extends T645sl11_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl11_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl11_3.Role645sl11_5");
    }

    // a base object from a class hierarchy is smart-lifted to a role class from a role class hierarchy
    // 6.4.5-otjld-smart-lifting-12
    public void test645_smartLifting12() {
       
       runConformTest(
            new String[] {
		"T645sl12Main.java",
			    "\n" +
			    "public class T645sl12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team645sl12_1       t  = new Team645sl12_4();\n" +
			    "        final Team645sl12_1 ft = new Team645sl12_1();\n" +
			    "        T645sl12_5          o  = ft.new Role645sl12_1();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl12_4.java",
			    "\n" +
			    "public team class Team645sl12_4 extends Team645sl12_3 {\n" +
			    "    public class Role645sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl12_4.this.toString() + \".Role645sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl12_1.java",
			    "\n" +
			    "public team class Team645sl12_1 {\n" +
			    "    public class Role645sl12_1 extends T645sl12_6 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl12_1.this.toString() + \".Role645sl12_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public abstract class Role645sl12_2 extends Role645sl12_1 playedBy T645sl12_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl12_1.this.toString() + \".Role645sl12_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role645sl12_3 extends Role645sl12_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl12_1.this.toString() + \".Role645sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl12_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T645sl12_5 as Role645sl12_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl12_4.java",
			    "\n" +
			    "public class T645sl12_4 extends T645sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl12_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl12_2.java",
			    "\n" +
			    "public team class Team645sl12_2 extends Team645sl12_1 {\n" +
			    "    public class Role645sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl12_2.this.toString() + \".Role645sl12_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role645sl12_4 extends Role645sl12_3 playedBy T645sl12_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl12_2.this.toString() + \".Role645sl12_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl12_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl12_5.java",
			    "\n" +
			    "public class T645sl12_5 extends T645sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl12_5\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl12_1.java",
			    "\n" +
			    "public class T645sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl12_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team645sl12_3.java",
			    "\n" +
			    "public team class Team645sl12_3 extends Team645sl12_2 {\n" +
			    "    public class Role645sl12_5 extends Role645sl12_3 playedBy T645sl12_5 {\n" +
			    "        public String toString() {\n" +
			    "            return Team645sl12_3.this.toString() + \".Role645sl12_5\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team645sl12_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T645sl12_6.java",
			    "\n" +
			    "public class T645sl12_6 extends T645sl12_5 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl12_6\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl12_2.java",
			    "\n" +
			    "public abstract class T645sl12_2 extends T645sl12_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl12_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T645sl12_3.java",
			    "\n" +
			    "public class T645sl12_3 extends T645sl12_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T645sl12_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team645sl12_4.Role645sl12_5");
    }
}
