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

public class BindingAmbiguities12 extends AbstractOTJLDTest {

     public BindingAmbiguities12(String name) {
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
         return BindingAmbiguities12.class;
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.12-otjld_g-ambiguous-binding-1
     public void _g_test7312_ambiguousBinding1() {

        runConformTest(
             new String[] {
 		"T7312ab1Main.java",
			    "\n" +
			    "public class T7312ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team7312ab1_3 t = new Team7312ab1_3();\n" +
			    "        T7312ab1_3    o = new T7312ab1_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7312ab1_4.java",
			    "\n" +
			    "public team class Team7312ab1_4 extends Team7312ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7312ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7312ab1_1.java",
			    "\n" +
			    "public team class Team7312ab1_1 {\n" +
			    "    public abstract class Role7312ab1_1 playedBy T7312ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7312ab1_1.this.toString() + \".Role7312ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role7312ab1_2 extends Role7312ab1_1 playedBy T7312ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7312ab1_1.this.toString() + \".Role7312ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7312ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team7312ab1_2.java",
			    "\n" +
			    "public team class Team7312ab1_2 extends Team7312ab1_1 {\n" +
			    "    public class Role7312ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7312ab1_2.this.toString() + \".Role7312ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role7312ab1_3 extends Role7312ab1_2 playedBy T7312ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7312ab1_2.this.toString() + \".Role7312ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7312ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T7312ab1_3 as Role7312ab1_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7312ab1_1.java",
			    "\n" +
			    "public abstract class T7312ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7312ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7312ab1_3.java",
			    "\n" +
			    "public team class Team7312ab1_3 extends Team7312ab1_2 {\n" +
			    "    public class Role7312ab1_4 extends Role7312ab1_1 playedBy T7312ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7312ab1_3.this.toString() + \".Role7312ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7312ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T7312ab1_2.java",
			    "\n" +
			    "public class T7312ab1_2 extends T7312ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7312ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7312ab1_3.java",
			    "\n" +
			    "public class T7312ab1_3 extends T7312ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7312ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team7312ab1_3.Role7312ab1_4");
     }

}

