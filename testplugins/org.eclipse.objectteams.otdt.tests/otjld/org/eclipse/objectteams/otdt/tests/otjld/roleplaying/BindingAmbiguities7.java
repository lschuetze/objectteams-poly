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

public class BindingAmbiguities7 extends AbstractOTJLDTest {

     public BindingAmbiguities7(String name) {
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
         return BindingAmbiguities7.class;
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.7-otjld_g-ambiguous-binding-1
     public void _g_test737_ambiguousBinding1() {

        runConformTest(
             new String[] {
 		"T737ab1Main.java",
			    "\n" +
			    "public class T737ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team737ab1_2 t = new Team737ab1_2();\n" +
			    "        T737ab1_1    o = new T737ab1_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab1_4.java",
			    "\n" +
			    "public team class Team737ab1_4 extends Team737ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab1_1.java",
			    "\n" +
			    "public team class Team737ab1_1 {\n" +
			    "    public abstract class Role737ab1_1 playedBy T737ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab1_1.this.toString() + \".Role737ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role737ab1_2 extends Role737ab1_1 playedBy T737ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab1_1.this.toString() + \".Role737ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team737ab1_2.java",
			    "\n" +
			    "public team class Team737ab1_2 extends Team737ab1_1 {\n" +
			    "    public class Role737ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab1_2.this.toString() + \".Role737ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role737ab1_3 extends Role737ab1_2 playedBy T737ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab1_2.this.toString() + \".Role737ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T737ab1_1 as Role737ab1_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab1_1.java",
			    "\n" +
			    "public abstract class T737ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab1_3.java",
			    "\n" +
			    "public team class Team737ab1_3 extends Team737ab1_2 {\n" +
			    "    public class Role737ab1_4 extends Role737ab1_1 playedBy T737ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab1_3.this.toString() + \".Role737ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T737ab1_2.java",
			    "\n" +
			    "public class T737ab1_2 extends T737ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab1_3.java",
			    "\n" +
			    "public class T737ab1_3 extends T737ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team737ab1_2.Role737ab1_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.7-otjld_g-ambiguous-binding-2
     public void _g_test737_ambiguousBinding2() {

        runConformTest(
             new String[] {
 		"T737ab2Main.java",
			    "\n" +
			    "public class T737ab2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team737ab2_2 t = new Team737ab2_3();\n" +
			    "        T737ab2_1    o = new T737ab2_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab2_4.java",
			    "\n" +
			    "public team class Team737ab2_4 extends Team737ab2_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab2_3.java",
			    "\n" +
			    "public class T737ab2_3 extends T737ab2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab2_1.java",
			    "\n" +
			    "public team class Team737ab2_1 {\n" +
			    "    public abstract class Role737ab2_1 playedBy T737ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab2_1.this.toString() + \".Role737ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role737ab2_2 extends Role737ab2_1 playedBy T737ab2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab2_1.this.toString() + \".Role737ab2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team737ab2_2.java",
			    "\n" +
			    "public team class Team737ab2_2 extends Team737ab2_1 {\n" +
			    "    public class Role737ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab2_2.this.toString() + \".Role737ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role737ab2_3 extends Role737ab2_2 playedBy T737ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab2_2.this.toString() + \".Role737ab2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T737ab2_1 as Role737ab2_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab2_1.java",
			    "\n" +
			    "public abstract class T737ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab2_3.java",
			    "\n" +
			    "public team class Team737ab2_3 extends Team737ab2_2 {\n" +
			    "    public class Role737ab2_4 extends Role737ab2_1 playedBy T737ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab2_3.this.toString() + \".Role737ab2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T737ab2_2.java",
			    "\n" +
			    "public class T737ab2_2 extends T737ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team737ab2_3.Role737ab2_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.7-otjld_g-ambiguous-binding-3
     public void _g_test737_ambiguousBinding3() {

        runConformTest(
             new String[] {
 		"T737ab3Main.java",
			    "\n" +
			    "public class T737ab3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team737ab3_2 t = new Team737ab3_4();\n" +
			    "        T737ab3_1    o = new T737ab3_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab3_4.java",
			    "\n" +
			    "public team class Team737ab3_4 extends Team737ab3_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab3_3.java",
			    "\n" +
			    "public team class Team737ab3_3 extends Team737ab3_2 {\n" +
			    "    public class Role737ab3_4 extends Role737ab3_1 playedBy T737ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab3_3.this.toString() + \".Role737ab3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T737ab3_2.java",
			    "\n" +
			    "public class T737ab3_2 extends T737ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab3_3.java",
			    "\n" +
			    "public class T737ab3_3 extends T737ab3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab3_1.java",
			    "\n" +
			    "public team class Team737ab3_1 {\n" +
			    "    public abstract class Role737ab3_1 playedBy T737ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab3_1.this.toString() + \".Role737ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role737ab3_2 extends Role737ab3_1 playedBy T737ab3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab3_1.this.toString() + \".Role737ab3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team737ab3_2.java",
			    "\n" +
			    "public team class Team737ab3_2 extends Team737ab3_1 {\n" +
			    "    public class Role737ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab3_2.this.toString() + \".Role737ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role737ab3_3 extends Role737ab3_2 playedBy T737ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab3_2.this.toString() + \".Role737ab3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T737ab3_1 as Role737ab3_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab3_1.java",
			    "\n" +
			    "public abstract class T737ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team737ab3_4.Role737ab3_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.7-otjld_g-ambiguous-binding-4
     public void _g_test737_ambiguousBinding4() {

        runConformTest(
             new String[] {
 		"T737ab4Main.java",
			    "\n" +
			    "public class T737ab4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team737ab4_2 t = new Team737ab4_2();\n" +
			    "        T737ab4_1    o = new T737ab4_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab4_4.java",
			    "\n" +
			    "public team class Team737ab4_4 extends Team737ab4_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab4_2.java",
			    "\n" +
			    "public team class Team737ab4_2 extends Team737ab4_1 {\n" +
			    "    public class Role737ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab4_2.this.toString() + \".Role737ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role737ab4_3 extends Role737ab4_2 playedBy T737ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab4_2.this.toString() + \".Role737ab4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T737ab4_1 as Role737ab4_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab4_1.java",
			    "\n" +
			    "public abstract class T737ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab4_3.java",
			    "\n" +
			    "public team class Team737ab4_3 extends Team737ab4_2 {\n" +
			    "    public class Role737ab4_4 extends Role737ab4_1 playedBy T737ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab4_3.this.toString() + \".Role737ab4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T737ab4_2.java",
			    "\n" +
			    "public class T737ab4_2 extends T737ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab4_3.java",
			    "\n" +
			    "public class T737ab4_3 extends T737ab4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab4_1.java",
			    "\n" +
			    "public team class Team737ab4_1 {\n" +
			    "    public abstract class Role737ab4_1 playedBy T737ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab4_1.this.toString() + \".Role737ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role737ab4_2 extends Role737ab4_1 playedBy T737ab4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab4_1.this.toString() + \".Role737ab4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
             },
             "Team737ab4_2.Role737ab4_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.7-otjld_g-ambiguous-binding-5
     public void _g_test737_ambiguousBinding5() {

        runConformTest(
             new String[] {
 		"T737ab5Main.java",
			    "\n" +
			    "public class T737ab5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team737ab5_2 t = new Team737ab5_3();\n" +
			    "        T737ab5_1    o = new T737ab5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab5_4.java",
			    "\n" +
			    "public team class Team737ab5_4 extends Team737ab5_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab5_1.java",
			    "\n" +
			    "public team class Team737ab5_1 {\n" +
			    "    public abstract class Role737ab5_1 playedBy T737ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab5_1.this.toString() + \".Role737ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role737ab5_2 extends Role737ab5_1 playedBy T737ab5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab5_1.this.toString() + \".Role737ab5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team737ab5_2.java",
			    "\n" +
			    "public team class Team737ab5_2 extends Team737ab5_1 {\n" +
			    "    public class Role737ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab5_2.this.toString() + \".Role737ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role737ab5_3 extends Role737ab5_2 playedBy T737ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab5_2.this.toString() + \".Role737ab5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T737ab5_1 as Role737ab5_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab5_1.java",
			    "\n" +
			    "public abstract class T737ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab5_3.java",
			    "\n" +
			    "public team class Team737ab5_3 extends Team737ab5_2 {\n" +
			    "    public class Role737ab5_4 extends Role737ab5_1 playedBy T737ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab5_3.this.toString() + \".Role737ab5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T737ab5_2.java",
			    "\n" +
			    "public class T737ab5_2 extends T737ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab5_3.java",
			    "\n" +
			    "public class T737ab5_3 extends T737ab5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team737ab5_3.Role737ab5_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.7-otjld_g-ambiguous-binding-6
     public void _g_test737_ambiguousBinding6() {

        runConformTest(
             new String[] {
 		"T737ab6Main.java",
			    "\n" +
			    "public class T737ab6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team737ab6_2 t = new Team737ab6_4();\n" +
			    "        T737ab6_1    o = new T737ab6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab6_4.java",
			    "\n" +
			    "public team class Team737ab6_4 extends Team737ab6_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab6_3.java",
			    "\n" +
			    "public class T737ab6_3 extends T737ab6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab6_1.java",
			    "\n" +
			    "public team class Team737ab6_1 {\n" +
			    "    public abstract class Role737ab6_1 playedBy T737ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab6_1.this.toString() + \".Role737ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role737ab6_2 extends Role737ab6_1 playedBy T737ab6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab6_1.this.toString() + \".Role737ab6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team737ab6_2.java",
			    "\n" +
			    "public team class Team737ab6_2 extends Team737ab6_1 {\n" +
			    "    public class Role737ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab6_2.this.toString() + \".Role737ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role737ab6_3 extends Role737ab6_2 playedBy T737ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab6_2.this.toString() + \".Role737ab6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T737ab6_1 as Role737ab6_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T737ab6_1.java",
			    "\n" +
			    "public abstract class T737ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team737ab6_3.java",
			    "\n" +
			    "public team class Team737ab6_3 extends Team737ab6_2 {\n" +
			    "    public class Role737ab6_4 extends Role737ab6_1 playedBy T737ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team737ab6_3.this.toString() + \".Role737ab6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team737ab6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T737ab6_2.java",
			    "\n" +
			    "public class T737ab6_2 extends T737ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T737ab6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team737ab6_4.Role737ab6_3");
     }

}

