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

public class BindingAmbiguities4 extends AbstractOTJLDTest {

     public BindingAmbiguities4(String name) {
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
         return BindingAmbiguities4.class;
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.4-otjld_g-ambiguous-binding-1
     public void _g_test734_ambiguousBinding1() {
        
        runConformTest(
             new String[] {
 		"T734ab1Main.java",
			    "\n" +
			    "public class T734ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team734ab1_1 t = new Team734ab1_1();\n" +
			    "        T734ab1_1    o = new T734ab1_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab1_4.java",
			    "\n" +
			    "public team class Team734ab1_4 extends Team734ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T734ab1_3.java",
			    "\n" +
			    "public class T734ab1_3 extends T734ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab1_1.java",
			    "\n" +
			    "public team class Team734ab1_1 {\n" +
			    "    public abstract class Role734ab1_1 playedBy T734ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab1_1.this.toString() + \".Role734ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role734ab1_2 extends Role734ab1_1 playedBy T734ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab1_1.this.toString() + \".Role734ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T734ab1_1 as Role734ab1_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab1_2.java",
			    "\n" +
			    "public team class Team734ab1_2 extends Team734ab1_1 {\n" +
			    "    public class Role734ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab1_2.this.toString() + \".Role734ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role734ab1_3 extends Role734ab1_2 playedBy T734ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab1_2.this.toString() + \".Role734ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab1_1.java",
			    "\n" +
			    "public abstract class T734ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab1_3.java",
			    "\n" +
			    "public team class Team734ab1_3 extends Team734ab1_2 {\n" +
			    "    public class Role734ab1_4 extends Role734ab1_1 playedBy T734ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab1_3.this.toString() + \".Role734ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab1_2.java",
			    "\n" +
			    "public class T734ab1_2 extends T734ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team734ab1_1.Role734ab1_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.4-otjld_g-ambiguous-binding-2
     public void _g_test734_ambiguousBinding2() {
        
        runConformTest(
             new String[] {
 		"T734ab2Main.java",
			    "\n" +
			    "public class T734ab2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team734ab2_1 t = new Team734ab2_2();\n" +
			    "        T734ab2_1    o = new T734ab2_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab2_4.java",
			    "\n" +
			    "public team class Team734ab2_4 extends Team734ab2_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab2_3.java",
			    "\n" +
			    "public team class Team734ab2_3 extends Team734ab2_2 {\n" +
			    "    public class Role734ab2_4 extends Role734ab2_1 playedBy T734ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab2_3.this.toString() + \".Role734ab2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab2_2.java",
			    "\n" +
			    "public class T734ab2_2 extends T734ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T734ab2_3.java",
			    "\n" +
			    "public class T734ab2_3 extends T734ab2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab2_1.java",
			    "\n" +
			    "public team class Team734ab2_1 {\n" +
			    "    public abstract class Role734ab2_1 playedBy T734ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab2_1.this.toString() + \".Role734ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role734ab2_2 extends Role734ab2_1 playedBy T734ab2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab2_1.this.toString() + \".Role734ab2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T734ab2_1 as Role734ab2_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab2_2.java",
			    "\n" +
			    "public team class Team734ab2_2 extends Team734ab2_1 {\n" +
			    "    public class Role734ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab2_2.this.toString() + \".Role734ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role734ab2_3 extends Role734ab2_2 playedBy T734ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab2_2.this.toString() + \".Role734ab2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab2_1.java",
			    "\n" +
			    "public abstract class T734ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team734ab2_2.Role734ab2_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.4-otjld_g-ambiguous-binding-3
     public void _g_test734_ambiguousBinding3() {
        
        runConformTest(
             new String[] {
 		"T734ab3Main.java",
			    "\n" +
			    "public class T734ab3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team734ab3_1 t = new Team734ab3_3();\n" +
			    "        T734ab3_1    o = new T734ab3_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab3_4.java",
			    "\n" +
			    "public team class Team734ab3_4 extends Team734ab3_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab3_2.java",
			    "\n" +
			    "public team class Team734ab3_2 extends Team734ab3_1 {\n" +
			    "    public class Role734ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab3_2.this.toString() + \".Role734ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role734ab3_3 extends Role734ab3_2 playedBy T734ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab3_2.this.toString() + \".Role734ab3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab3_1.java",
			    "\n" +
			    "public abstract class T734ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab3_3.java",
			    "\n" +
			    "public team class Team734ab3_3 extends Team734ab3_2 {\n" +
			    "    public class Role734ab3_4 extends Role734ab3_1 playedBy T734ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab3_3.this.toString() + \".Role734ab3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab3_2.java",
			    "\n" +
			    "public class T734ab3_2 extends T734ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T734ab3_3.java",
			    "\n" +
			    "public class T734ab3_3 extends T734ab3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab3_1.java",
			    "\n" +
			    "public team class Team734ab3_1 {\n" +
			    "    public abstract class Role734ab3_1 playedBy T734ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab3_1.this.toString() + \".Role734ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role734ab3_2 extends Role734ab3_1 playedBy T734ab3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab3_1.this.toString() + \".Role734ab3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T734ab3_1 as Role734ab3_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team734ab3_3.Role734ab3_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.4-otjld_g-ambiguous-binding-4
     public void _g_test734_ambiguousBinding4() {
        
        runConformTest(
             new String[] {
 		"T734ab4Main.java",
			    "\n" +
			    "public class T734ab4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team734ab4_1 t = new Team734ab4_4();\n" +
			    "        T734ab4_1    o = new T734ab4_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab4_4.java",
			    "\n" +
			    "public team class Team734ab4_4 extends Team734ab4_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab4_1.java",
			    "\n" +
			    "public team class Team734ab4_1 {\n" +
			    "    public abstract class Role734ab4_1 playedBy T734ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab4_1.this.toString() + \".Role734ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role734ab4_2 extends Role734ab4_1 playedBy T734ab4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab4_1.this.toString() + \".Role734ab4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T734ab4_1 as Role734ab4_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab4_2.java",
			    "\n" +
			    "public team class Team734ab4_2 extends Team734ab4_1 {\n" +
			    "    public class Role734ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab4_2.this.toString() + \".Role734ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role734ab4_3 extends Role734ab4_2 playedBy T734ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab4_2.this.toString() + \".Role734ab4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab4_1.java",
			    "\n" +
			    "public abstract class T734ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab4_3.java",
			    "\n" +
			    "public team class Team734ab4_3 extends Team734ab4_2 {\n" +
			    "    public class Role734ab4_4 extends Role734ab4_1 playedBy T734ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab4_3.this.toString() + \".Role734ab4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab4_2.java",
			    "\n" +
			    "public class T734ab4_2 extends T734ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T734ab4_3.java",
			    "\n" +
			    "public class T734ab4_3 extends T734ab4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team734ab4_4.Role734ab4_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.4-otjld_g-ambiguous-binding-5
     public void _g_test734_ambiguousBinding5() {
        
        runConformTest(
             new String[] {
 		"T734ab5Main.java",
			    "\n" +
			    "public class T734ab5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team734ab5_1 t = new Team734ab5_1();\n" +
			    "        T734ab5_1    o = new T734ab5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab5_4.java",
			    "\n" +
			    "public team class Team734ab5_4 extends Team734ab5_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T734ab5_3.java",
			    "\n" +
			    "public class T734ab5_3 extends T734ab5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab5_1.java",
			    "\n" +
			    "public team class Team734ab5_1 {\n" +
			    "    public abstract class Role734ab5_1 playedBy T734ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab5_1.this.toString() + \".Role734ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role734ab5_2 extends Role734ab5_1 playedBy T734ab5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab5_1.this.toString() + \".Role734ab5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T734ab5_1 as Role734ab5_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab5_2.java",
			    "\n" +
			    "public team class Team734ab5_2 extends Team734ab5_1 {\n" +
			    "    public class Role734ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab5_2.this.toString() + \".Role734ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role734ab5_3 extends Role734ab5_2 playedBy T734ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab5_2.this.toString() + \".Role734ab5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab5_1.java",
			    "\n" +
			    "public abstract class T734ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab5_3.java",
			    "\n" +
			    "public team class Team734ab5_3 extends Team734ab5_2 {\n" +
			    "    public class Role734ab5_4 extends Role734ab5_1 playedBy T734ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab5_3.this.toString() + \".Role734ab5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab5_2.java",
			    "\n" +
			    "public class T734ab5_2 extends T734ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team734ab5_1.Role734ab5_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.4-otjld_g-ambiguous-binding-6
     public void _g_test734_ambiguousBinding6() {
        
        runConformTest(
             new String[] {
 		"T734ab6Main.java",
			    "\n" +
			    "public class T734ab6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team734ab6_1 t = new Team734ab6_2();\n" +
			    "        T734ab6_1    o = new T734ab6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab6_4.java",
			    "\n" +
			    "public team class Team734ab6_4 extends Team734ab6_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab6_3.java",
			    "\n" +
			    "public team class Team734ab6_3 extends Team734ab6_2 {\n" +
			    "    public class Role734ab6_4 extends Role734ab6_1 playedBy T734ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab6_3.this.toString() + \".Role734ab6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab6_2.java",
			    "\n" +
			    "public class T734ab6_2 extends T734ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T734ab6_3.java",
			    "\n" +
			    "public class T734ab6_3 extends T734ab6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab6_1.java",
			    "\n" +
			    "public team class Team734ab6_1 {\n" +
			    "    public abstract class Role734ab6_1 playedBy T734ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab6_1.this.toString() + \".Role734ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role734ab6_2 extends Role734ab6_1 playedBy T734ab6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab6_1.this.toString() + \".Role734ab6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T734ab6_1 as Role734ab6_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab6_2.java",
			    "\n" +
			    "public team class Team734ab6_2 extends Team734ab6_1 {\n" +
			    "    public class Role734ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab6_2.this.toString() + \".Role734ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role734ab6_3 extends Role734ab6_2 playedBy T734ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab6_2.this.toString() + \".Role734ab6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab6_1.java",
			    "\n" +
			    "public abstract class T734ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team734ab6_2.Role734ab6_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.4-otjld_g-ambiguous-binding-7
     public void _g_test734_ambiguousBinding7() {
        
        runConformTest(
             new String[] {
 		"T734ab7Main.java",
			    "\n" +
			    "public class T734ab7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team734ab7_1 t = new Team734ab7_3();\n" +
			    "        T734ab7_1    o = new T734ab7_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab7_4.java",
			    "\n" +
			    "public team class Team734ab7_4 extends Team734ab7_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab7_2.java",
			    "\n" +
			    "public team class Team734ab7_2 extends Team734ab7_1 {\n" +
			    "    public class Role734ab7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab7_2.this.toString() + \".Role734ab7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role734ab7_3 extends Role734ab7_2 playedBy T734ab7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab7_2.this.toString() + \".Role734ab7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab7_1.java",
			    "\n" +
			    "public abstract class T734ab7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab7_3.java",
			    "\n" +
			    "public team class Team734ab7_3 extends Team734ab7_2 {\n" +
			    "    public class Role734ab7_4 extends Role734ab7_1 playedBy T734ab7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab7_3.this.toString() + \".Role734ab7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab7_2.java",
			    "\n" +
			    "public class T734ab7_2 extends T734ab7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T734ab7_3.java",
			    "\n" +
			    "public class T734ab7_3 extends T734ab7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab7_1.java",
			    "\n" +
			    "public team class Team734ab7_1 {\n" +
			    "    public abstract class Role734ab7_1 playedBy T734ab7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab7_1.this.toString() + \".Role734ab7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role734ab7_2 extends Role734ab7_1 playedBy T734ab7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab7_1.this.toString() + \".Role734ab7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T734ab7_1 as Role734ab7_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Failed to lift 'T734ab7_3' of class T734ab7_3 to type 'Role734ab7_2'\n" +
             "(See OT/J definition para. 7.3(c)).");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.4-otjld_g-ambiguous-binding-8
     public void _g_test734_ambiguousBinding8() {
        
        runConformTest(
             new String[] {
 		"T734ab8Main.java",
			    "\n" +
			    "public class T734ab8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team734ab8_1 t = new Team734ab8_4();\n" +
			    "        T734ab8_1    o = new T734ab8_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab8_4.java",
			    "\n" +
			    "public team class Team734ab8_4 extends Team734ab8_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab8_1.java",
			    "\n" +
			    "public team class Team734ab8_1 {\n" +
			    "    public abstract class Role734ab8_1 playedBy T734ab8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab8_1.this.toString() + \".Role734ab8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role734ab8_2 extends Role734ab8_1 playedBy T734ab8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab8_1.this.toString() + \".Role734ab8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T734ab8_1 as Role734ab8_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab8_2.java",
			    "\n" +
			    "public team class Team734ab8_2 extends Team734ab8_1 {\n" +
			    "    public class Role734ab8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab8_2.this.toString() + \".Role734ab8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role734ab8_3 extends Role734ab8_2 playedBy T734ab8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab8_2.this.toString() + \".Role734ab8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab8_1.java",
			    "\n" +
			    "public abstract class T734ab8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team734ab8_3.java",
			    "\n" +
			    "public team class Team734ab8_3 extends Team734ab8_2 {\n" +
			    "    public class Role734ab8_4 extends Role734ab8_1 playedBy T734ab8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team734ab8_3.this.toString() + \".Role734ab8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team734ab8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T734ab8_2.java",
			    "\n" +
			    "public class T734ab8_2 extends T734ab8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T734ab8_3.java",
			    "\n" +
			    "public class T734ab8_3 extends T734ab8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T734ab8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Failed to lift 'T734ab8_3' of class T734ab8_3 to type 'Role734ab8_2'\n" +
             "(See OT/J definition para. 7.3(c)).");
     }

}

