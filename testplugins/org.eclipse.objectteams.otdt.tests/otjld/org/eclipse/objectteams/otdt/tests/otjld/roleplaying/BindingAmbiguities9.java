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

public class BindingAmbiguities9 extends AbstractOTJLDTest {

     public BindingAmbiguities9(String name) {
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
         return BindingAmbiguities9.class;
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.9-otjld_g-ambiguous-binding-1
     public void _g_test739_ambiguousBinding1() {

        runConformTest(
             new String[] {
 		"T739ab1Main.java",
			    "\n" +
			    "public class T739ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team739ab1_2 t = new Team739ab1_2();\n" +
			    "        T739ab1_3    o = new T739ab1_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab1_4.java",
			    "\n" +
			    "public team class Team739ab1_4 extends Team739ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab1_3.java",
			    "\n" +
			    "public team class Team739ab1_3 extends Team739ab1_2 {\n" +
			    "    public class Role739ab1_4 extends Role739ab1_1 playedBy T739ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab1_3.this.toString() + \".Role739ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T739ab1_2.java",
			    "\n" +
			    "public class T739ab1_2 extends T739ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T739ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T739ab1_3.java",
			    "\n" +
			    "public class T739ab1_3 extends T739ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T739ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab1_1.java",
			    "\n" +
			    "public team class Team739ab1_1 {\n" +
			    "    public abstract class Role739ab1_1 playedBy T739ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab1_1.this.toString() + \".Role739ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role739ab1_2 extends Role739ab1_1 playedBy T739ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab1_1.this.toString() + \".Role739ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team739ab1_2.java",
			    "\n" +
			    "public team class Team739ab1_2 extends Team739ab1_1 {\n" +
			    "    public class Role739ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab1_2.this.toString() + \".Role739ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role739ab1_3 extends Role739ab1_2 playedBy T739ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab1_2.this.toString() + \".Role739ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T739ab1_3 as Role739ab1_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T739ab1_1.java",
			    "\n" +
			    "public abstract class T739ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T739ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team739ab1_2.Role739ab1_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.9-otjld_g-ambiguous-binding-2
     public void _g_test739_ambiguousBinding2() {

        runConformTest(
             new String[] {
 		"T739ab2Main.java",
			    "\n" +
			    "public class T739ab2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team739ab2_2 t = new Team739ab2_3();\n" +
			    "        T739ab2_3    o = new T739ab2_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab2_4.java",
			    "\n" +
			    "public team class Team739ab2_4 extends Team739ab2_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab2_2.java",
			    "\n" +
			    "public team class Team739ab2_2 extends Team739ab2_1 {\n" +
			    "    public class Role739ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab2_2.this.toString() + \".Role739ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role739ab2_3 extends Role739ab2_2 playedBy T739ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab2_2.this.toString() + \".Role739ab2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T739ab2_3 as Role739ab2_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T739ab2_1.java",
			    "\n" +
			    "public abstract class T739ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T739ab2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab2_3.java",
			    "\n" +
			    "public team class Team739ab2_3 extends Team739ab2_2 {\n" +
			    "    public class Role739ab2_4 extends Role739ab2_1 playedBy T739ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab2_3.this.toString() + \".Role739ab2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T739ab2_2.java",
			    "\n" +
			    "public class T739ab2_2 extends T739ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T739ab2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T739ab2_3.java",
			    "\n" +
			    "public class T739ab2_3 extends T739ab2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T739ab2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab2_1.java",
			    "\n" +
			    "public team class Team739ab2_1 {\n" +
			    "    public abstract class Role739ab2_1 playedBy T739ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab2_1.this.toString() + \".Role739ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role739ab2_2 extends Role739ab2_1 playedBy T739ab2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab2_1.this.toString() + \".Role739ab2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
             },
             "Team739ab2_3.Role739ab2_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.9-otjld_g-ambiguous-binding-3
     public void _g_test739_ambiguousBinding3() {

        runConformTest(
             new String[] {
 		"T739ab3Main.java",
			    "\n" +
			    "public class T739ab3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team739ab3_2 t = new Team739ab3_4();\n" +
			    "        T739ab3_3    o = new T739ab3_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab3_4.java",
			    "\n" +
			    "public team class Team739ab3_4 extends Team739ab3_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab3_1.java",
			    "\n" +
			    "public team class Team739ab3_1 {\n" +
			    "    public abstract class Role739ab3_1 playedBy T739ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab3_1.this.toString() + \".Role739ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role739ab3_2 extends Role739ab3_1 playedBy T739ab3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab3_1.this.toString() + \".Role739ab3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team739ab3_2.java",
			    "\n" +
			    "public team class Team739ab3_2 extends Team739ab3_1 {\n" +
			    "    public class Role739ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab3_2.this.toString() + \".Role739ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role739ab3_3 extends Role739ab3_2 playedBy T739ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab3_2.this.toString() + \".Role739ab3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T739ab3_3 as Role739ab3_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T739ab3_1.java",
			    "\n" +
			    "public abstract class T739ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T739ab3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team739ab3_3.java",
			    "\n" +
			    "public team class Team739ab3_3 extends Team739ab3_2 {\n" +
			    "    public class Role739ab3_4 extends Role739ab3_1 playedBy T739ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team739ab3_3.this.toString() + \".Role739ab3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team739ab3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T739ab3_2.java",
			    "\n" +
			    "public class T739ab3_2 extends T739ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T739ab3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T739ab3_3.java",
			    "\n" +
			    "public class T739ab3_3 extends T739ab3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T739ab3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team739ab3_4.Role739ab3_3");
     }

}

