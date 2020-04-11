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
package org.eclipse.objectteams.otdt.tests.otjld.roleplaying;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class BindingAmbiguities5 extends AbstractOTJLDTest {

     public BindingAmbiguities5(String name) {
             super(name);
     }

     // Static initializer to specify tests subset using TESTS_* static variables
     // All specified tests which does not belong to the class are skipped...
     static {
//        TESTS_NAMES = new String { ""};
//        TESTS_NUMBERS = new int { 1459 };
//        TESTS_RANGE = new int { 1097, -1 };
     }

     public static Test suite() {
         return buildComparableTestSuite(testClass());
     }

     public static Class testClass() {
         return BindingAmbiguities5.class;
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.5-otjld_g-ambiguous-binding-1
     public void _g_test735_ambiguousBinding1() {

        runConformTest(
             new String[] {
 		"T735ab1Main.java",
			    "\n" +
			    "public class T735ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team735ab1_1 t = new Team735ab1_1();\n" +
			    "        T735ab1_2    o = new T735ab1_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab1_4.java",
			    "\n" +
			    "public team class Team735ab1_4 extends Team735ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab1_3.java",
			    "\n" +
			    "public team class Team735ab1_3 extends Team735ab1_2 {\n" +
			    "    public class Role735ab1_4 extends Role735ab1_1 playedBy T735ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab1_3.this.toString() + \".Role735ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab1_2.java",
			    "\n" +
			    "public class T735ab1_2 extends T735ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T735ab1_3.java",
			    "\n" +
			    "public class T735ab1_3 extends T735ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab1_1.java",
			    "\n" +
			    "public team class Team735ab1_1 {\n" +
			    "    public abstract class Role735ab1_1 playedBy T735ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab1_1.this.toString() + \".Role735ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role735ab1_2 extends Role735ab1_1 playedBy T735ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab1_1.this.toString() + \".Role735ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T735ab1_2 as Role735ab1_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab1_2.java",
			    "\n" +
			    "public team class Team735ab1_2 extends Team735ab1_1 {\n" +
			    "    public class Role735ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab1_2.this.toString() + \".Role735ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role735ab1_3 extends Role735ab1_2 playedBy T735ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab1_2.this.toString() + \".Role735ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab1_1.java",
			    "\n" +
			    "public abstract class T735ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team735ab1_1.Role735ab1_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.5-otjld_g-ambiguous-binding-2
     public void _g_test735_ambiguousBinding2() {

        runConformTest(
             new String[] {
 		"T735ab2Main.java",
			    "\n" +
			    "public class T735ab2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team735ab2_1 t = new Team735ab2_2();\n" +
			    "        T735ab2_2    o = new T735ab2_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab2_4.java",
			    "\n" +
			    "public team class Team735ab2_4 extends Team735ab2_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab2_2.java",
			    "\n" +
			    "public team class Team735ab2_2 extends Team735ab2_1 {\n" +
			    "    public class Role735ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab2_2.this.toString() + \".Role735ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role735ab2_3 extends Role735ab2_2 playedBy T735ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab2_2.this.toString() + \".Role735ab2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab2_1.java",
			    "\n" +
			    "public abstract class T735ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab2_3.java",
			    "\n" +
			    "public team class Team735ab2_3 extends Team735ab2_2 {\n" +
			    "    public class Role735ab2_4 extends Role735ab2_1 playedBy T735ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab2_3.this.toString() + \".Role735ab2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab2_2.java",
			    "\n" +
			    "public class T735ab2_2 extends T735ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T735ab2_3.java",
			    "\n" +
			    "public class T735ab2_3 extends T735ab2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab2_1.java",
			    "\n" +
			    "public team class Team735ab2_1 {\n" +
			    "    public abstract class Role735ab2_1 playedBy T735ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab2_1.this.toString() + \".Role735ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role735ab2_2 extends Role735ab2_1 playedBy T735ab2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab2_1.this.toString() + \".Role735ab2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T735ab2_2 as Role735ab2_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team735ab2_2.Role735ab2_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.5-otjld_g-ambiguous-binding-3
     public void _g_test735_ambiguousBinding3() {

        runConformTest(
             new String[] {
 		"T735ab3Main.java",
			    "\n" +
			    "public class T735ab3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team735ab3_1 t = new Team735ab3_3();\n" +
			    "        T735ab3_2    o = new T735ab3_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab3_4.java",
			    "\n" +
			    "public team class Team735ab3_4 extends Team735ab3_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab3_1.java",
			    "\n" +
			    "public team class Team735ab3_1 {\n" +
			    "    public abstract class Role735ab3_1 playedBy T735ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab3_1.this.toString() + \".Role735ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role735ab3_2 extends Role735ab3_1 playedBy T735ab3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab3_1.this.toString() + \".Role735ab3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T735ab3_2 as Role735ab3_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab3_2.java",
			    "\n" +
			    "public team class Team735ab3_2 extends Team735ab3_1 {\n" +
			    "    public class Role735ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab3_2.this.toString() + \".Role735ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role735ab3_3 extends Role735ab3_2 playedBy T735ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab3_2.this.toString() + \".Role735ab3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab3_1.java",
			    "\n" +
			    "public abstract class T735ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab3_3.java",
			    "\n" +
			    "public team class Team735ab3_3 extends Team735ab3_2 {\n" +
			    "    public class Role735ab3_4 extends Role735ab3_1 playedBy T735ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab3_3.this.toString() + \".Role735ab3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab3_2.java",
			    "\n" +
			    "public class T735ab3_2 extends T735ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T735ab3_3.java",
			    "\n" +
			    "public class T735ab3_3 extends T735ab3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team735ab3_3.Role735ab3_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.5-otjld_g-ambiguous-binding-4
     public void _g_test735_ambiguousBinding4() {

        runConformTest(
             new String[] {
 		"T735ab4Main.java",
			    "\n" +
			    "public class T735ab4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team735ab4_1 t = new Team735ab4_4();\n" +
			    "        T735ab4_2    o = new T735ab4_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab4_4.java",
			    "\n" +
			    "public team class Team735ab4_4 extends Team735ab4_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T735ab4_3.java",
			    "\n" +
			    "public class T735ab4_3 extends T735ab4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab4_1.java",
			    "\n" +
			    "public team class Team735ab4_1 {\n" +
			    "    public abstract class Role735ab4_1 playedBy T735ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab4_1.this.toString() + \".Role735ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role735ab4_2 extends Role735ab4_1 playedBy T735ab4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab4_1.this.toString() + \".Role735ab4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T735ab4_2 as Role735ab4_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab4_2.java",
			    "\n" +
			    "public team class Team735ab4_2 extends Team735ab4_1 {\n" +
			    "    public class Role735ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab4_2.this.toString() + \".Role735ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role735ab4_3 extends Role735ab4_2 playedBy T735ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab4_2.this.toString() + \".Role735ab4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab4_1.java",
			    "\n" +
			    "public abstract class T735ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab4_3.java",
			    "\n" +
			    "public team class Team735ab4_3 extends Team735ab4_2 {\n" +
			    "    public class Role735ab4_4 extends Role735ab4_1 playedBy T735ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab4_3.this.toString() + \".Role735ab4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab4_2.java",
			    "\n" +
			    "public class T735ab4_2 extends T735ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team735ab4_4.Role735ab4_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.5-otjld_g-ambiguous-binding-5
     public void _g_test735_ambiguousBinding5() {

        runConformTest(
             new String[] {
 		"T735ab5Main.java",
			    "\n" +
			    "public class T735ab5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team735ab5_1 t = new Team735ab5_1();\n" +
			    "        T735ab5_2    o = new T735ab5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab5_4.java",
			    "\n" +
			    "public team class Team735ab5_4 extends Team735ab5_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab5_3.java",
			    "\n" +
			    "public team class Team735ab5_3 extends Team735ab5_2 {\n" +
			    "    public class Role735ab5_4 extends Role735ab5_1 playedBy T735ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab5_3.this.toString() + \".Role735ab5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab5_2.java",
			    "\n" +
			    "public class T735ab5_2 extends T735ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T735ab5_3.java",
			    "\n" +
			    "public class T735ab5_3 extends T735ab5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab5_1.java",
			    "\n" +
			    "public team class Team735ab5_1 {\n" +
			    "    public abstract class Role735ab5_1 playedBy T735ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab5_1.this.toString() + \".Role735ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role735ab5_2 extends Role735ab5_1 playedBy T735ab5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab5_1.this.toString() + \".Role735ab5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T735ab5_2 as Role735ab5_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab5_2.java",
			    "\n" +
			    "public team class Team735ab5_2 extends Team735ab5_1 {\n" +
			    "    public class Role735ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab5_2.this.toString() + \".Role735ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role735ab5_3 extends Role735ab5_2 playedBy T735ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab5_2.this.toString() + \".Role735ab5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab5_1.java",
			    "\n" +
			    "public abstract class T735ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team735ab5_1.Role735ab5_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.5-otjld_g-ambiguous-binding-6
     public void _g_test735_ambiguousBinding6() {

        runConformTest(
             new String[] {
 		"T735ab6Main.java",
			    "\n" +
			    "public class T735ab6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team735ab6_1 t = new Team735ab6_2();\n" +
			    "        T735ab6_2    o = new T735ab6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab6_4.java",
			    "\n" +
			    "public team class Team735ab6_4 extends Team735ab6_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab6_2.java",
			    "\n" +
			    "public team class Team735ab6_2 extends Team735ab6_1 {\n" +
			    "    public class Role735ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab6_2.this.toString() + \".Role735ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role735ab6_3 extends Role735ab6_2 playedBy T735ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab6_2.this.toString() + \".Role735ab6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab6_1.java",
			    "\n" +
			    "public abstract class T735ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab6_3.java",
			    "\n" +
			    "public team class Team735ab6_3 extends Team735ab6_2 {\n" +
			    "    public class Role735ab6_4 extends Role735ab6_1 playedBy T735ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab6_3.this.toString() + \".Role735ab6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab6_2.java",
			    "\n" +
			    "public class T735ab6_2 extends T735ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T735ab6_3.java",
			    "\n" +
			    "public class T735ab6_3 extends T735ab6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab6_1.java",
			    "\n" +
			    "public team class Team735ab6_1 {\n" +
			    "    public abstract class Role735ab6_1 playedBy T735ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab6_1.this.toString() + \".Role735ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role735ab6_2 extends Role735ab6_1 playedBy T735ab6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab6_1.this.toString() + \".Role735ab6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T735ab6_2 as Role735ab6_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team735ab6_2.Role735ab6_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.5-otjld_g-ambiguous-binding-7
     public void _g_test735_ambiguousBinding7() {

        runConformTest(
             new String[] {
 		"T735ab7Main.java",
			    "\n" +
			    "public class T735ab7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team735ab7_1 t = new Team735ab7_3();\n" +
			    "        T735ab7_2    o = new T735ab7_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab7_4.java",
			    "\n" +
			    "public team class Team735ab7_4 extends Team735ab7_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab7_1.java",
			    "\n" +
			    "public team class Team735ab7_1 {\n" +
			    "    public abstract class Role735ab7_1 playedBy T735ab7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab7_1.this.toString() + \".Role735ab7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role735ab7_2 extends Role735ab7_1 playedBy T735ab7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab7_1.this.toString() + \".Role735ab7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T735ab7_2 as Role735ab7_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab7_2.java",
			    "\n" +
			    "public team class Team735ab7_2 extends Team735ab7_1 {\n" +
			    "    public class Role735ab7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab7_2.this.toString() + \".Role735ab7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role735ab7_3 extends Role735ab7_2 playedBy T735ab7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab7_2.this.toString() + \".Role735ab7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab7_1.java",
			    "\n" +
			    "public abstract class T735ab7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab7_3.java",
			    "\n" +
			    "public team class Team735ab7_3 extends Team735ab7_2 {\n" +
			    "    public class Role735ab7_4 extends Role735ab7_1 playedBy T735ab7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab7_3.this.toString() + \".Role735ab7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab7_2.java",
			    "\n" +
			    "public class T735ab7_2 extends T735ab7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T735ab7_3.java",
			    "\n" +
			    "public class T735ab7_3 extends T735ab7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Failed to lift 'T735ab7_3' of class T735ab7_3 to type 'Role735ab7_2'\n" +
             "(See OT/J definition para. 7.3(c)).");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.5-otjld_g-ambiguous-binding-8
     public void _g_test735_ambiguousBinding8() {

        runConformTest(
             new String[] {
 		"T735ab8Main.java",
			    "\n" +
			    "public class T735ab8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team735ab8_1 t = new Team735ab8_4();\n" +
			    "        T735ab8_2    o = new T735ab8_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab8_4.java",
			    "\n" +
			    "public team class Team735ab8_4 extends Team735ab8_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T735ab8_3.java",
			    "\n" +
			    "public class T735ab8_3 extends T735ab8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab8_1.java",
			    "\n" +
			    "public team class Team735ab8_1 {\n" +
			    "    public abstract class Role735ab8_1 playedBy T735ab8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab8_1.this.toString() + \".Role735ab8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role735ab8_2 extends Role735ab8_1 playedBy T735ab8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab8_1.this.toString() + \".Role735ab8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T735ab8_2 as Role735ab8_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab8_2.java",
			    "\n" +
			    "public team class Team735ab8_2 extends Team735ab8_1 {\n" +
			    "    public class Role735ab8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab8_2.this.toString() + \".Role735ab8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role735ab8_3 extends Role735ab8_2 playedBy T735ab8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab8_2.this.toString() + \".Role735ab8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab8_1.java",
			    "\n" +
			    "public abstract class T735ab8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team735ab8_3.java",
			    "\n" +
			    "public team class Team735ab8_3 extends Team735ab8_2 {\n" +
			    "    public class Role735ab8_4 extends Role735ab8_1 playedBy T735ab8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team735ab8_3.this.toString() + \".Role735ab8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team735ab8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T735ab8_2.java",
			    "\n" +
			    "public class T735ab8_2 extends T735ab8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T735ab8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Failed to lift 'T735ab8_3' of class T735ab8_3 to type 'Role735ab8_2'\n" +
             "(See OT/J definition para. 7.3(c)).");
     }

}

