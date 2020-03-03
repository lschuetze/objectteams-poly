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


import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

public class BindingAmbiguities11 extends AbstractOTJLDTest {

     public BindingAmbiguities11(String name) {
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
         return BindingAmbiguities11.class;
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.11-otjld_g-ambiguous-binding-1
     public void _g_test7311_ambiguousBinding1() {
        
        runConformTest(
             new String[] {
 		"T7311ab1Main.java",
			    "\n" +
			    "public class T7311ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team7311ab1_3 t = new Team7311ab1_3();\n" +
			    "        T7311ab1_2    o = new T7311ab1_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab1_4.java",
			    "\n" +
			    "public team class Team7311ab1_4 extends Team7311ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab1_2.java",
			    "\n" +
			    "public team class Team7311ab1_2 extends Team7311ab1_1 {\n" +
			    "    public class Role7311ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab1_2.this.toString() + \".Role7311ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role7311ab1_3 extends Role7311ab1_2 playedBy T7311ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab1_2.this.toString() + \".Role7311ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T7311ab1_2 as Role7311ab1_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7311ab1_1.java",
			    "\n" +
			    "public abstract class T7311ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab1_3.java",
			    "\n" +
			    "public team class Team7311ab1_3 extends Team7311ab1_2 {\n" +
			    "    public class Role7311ab1_4 extends Role7311ab1_1 playedBy T7311ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab1_3.this.toString() + \".Role7311ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T7311ab1_2.java",
			    "\n" +
			    "public class T7311ab1_2 extends T7311ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7311ab1_3.java",
			    "\n" +
			    "public class T7311ab1_3 extends T7311ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab1_1.java",
			    "\n" +
			    "public team class Team7311ab1_1 {\n" +
			    "    public abstract class Role7311ab1_1 playedBy T7311ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab1_1.this.toString() + \".Role7311ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role7311ab1_2 extends Role7311ab1_1 playedBy T7311ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab1_1.this.toString() + \".Role7311ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
             },
             "Team7311ab1_3.Role7311ab1_4");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.11-otjld_g-ambiguous-binding-2
     public void _g_test7311_ambiguousBinding2() {
        
        runConformTest(
             new String[] {
 		"T7311ab2Main.java",
			    "\n" +
			    "public class T7311ab2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team7311ab2_3 t = new Team7311ab2_4();\n" +
			    "        T7311ab2_2    o = new T7311ab2_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab2_4.java",
			    "\n" +
			    "public team class Team7311ab2_4 extends Team7311ab2_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab2_1.java",
			    "\n" +
			    "public team class Team7311ab2_1 {\n" +
			    "    public abstract class Role7311ab2_1 playedBy T7311ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab2_1.this.toString() + \".Role7311ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role7311ab2_2 extends Role7311ab2_1 playedBy T7311ab2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab2_1.this.toString() + \".Role7311ab2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab2_2.java",
			    "\n" +
			    "public team class Team7311ab2_2 extends Team7311ab2_1 {\n" +
			    "    public class Role7311ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab2_2.this.toString() + \".Role7311ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role7311ab2_3 extends Role7311ab2_2 playedBy T7311ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab2_2.this.toString() + \".Role7311ab2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T7311ab2_2 as Role7311ab2_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7311ab2_1.java",
			    "\n" +
			    "public abstract class T7311ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab2_3.java",
			    "\n" +
			    "public team class Team7311ab2_3 extends Team7311ab2_2 {\n" +
			    "    public class Role7311ab2_4 extends Role7311ab2_1 playedBy T7311ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab2_3.this.toString() + \".Role7311ab2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T7311ab2_2.java",
			    "\n" +
			    "public class T7311ab2_2 extends T7311ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7311ab2_3.java",
			    "\n" +
			    "public class T7311ab2_3 extends T7311ab2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team7311ab2_4.Role7311ab2_4");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.11-otjld_g-ambiguous-binding-3
     public void _g_test7311_ambiguousBinding3() {
        
        runConformTest(
             new String[] {
 		"T7311ab3Main.java",
			    "\n" +
			    "public class T7311ab3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team7311ab3_3 t = new Team7311ab3_3();\n" +
			    "        T7311ab3_2    o = new T7311ab3_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab3_4.java",
			    "\n" +
			    "public team class Team7311ab3_4 extends Team7311ab3_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7311ab3_3.java",
			    "\n" +
			    "public class T7311ab3_3 extends T7311ab3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab3_1.java",
			    "\n" +
			    "public team class Team7311ab3_1 {\n" +
			    "    public abstract class Role7311ab3_1 playedBy T7311ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab3_1.this.toString() + \".Role7311ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role7311ab3_2 extends Role7311ab3_1 playedBy T7311ab3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab3_1.this.toString() + \".Role7311ab3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab3_2.java",
			    "\n" +
			    "public team class Team7311ab3_2 extends Team7311ab3_1 {\n" +
			    "    public class Role7311ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab3_2.this.toString() + \".Role7311ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role7311ab3_3 extends Role7311ab3_2 playedBy T7311ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab3_2.this.toString() + \".Role7311ab3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T7311ab3_2 as Role7311ab3_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7311ab3_1.java",
			    "\n" +
			    "public abstract class T7311ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab3_3.java",
			    "\n" +
			    "public team class Team7311ab3_3 extends Team7311ab3_2 {\n" +
			    "    public class Role7311ab3_4 extends Role7311ab3_1 playedBy T7311ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab3_3.this.toString() + \".Role7311ab3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T7311ab3_2.java",
			    "\n" +
			    "public class T7311ab3_2 extends T7311ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team7311ab3_3.Role7311ab3_4");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.11-otjld_g-ambiguous-binding-4
     public void _g_test7311_ambiguousBinding4() {
        
        runConformTest(
             new String[] {
 		"T7311ab4Main.java",
			    "\n" +
			    "public class T7311ab4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team7311ab4_3 t = new Team7311ab4_4();\n" +
			    "        T7311ab4_2    o = new T7311ab4_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab4_4.java",
			    "\n" +
			    "public team class Team7311ab4_4 extends Team7311ab4_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab4_3.java",
			    "\n" +
			    "public team class Team7311ab4_3 extends Team7311ab4_2 {\n" +
			    "    public class Role7311ab4_4 extends Role7311ab4_1 playedBy T7311ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab4_3.this.toString() + \".Role7311ab4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T7311ab4_2.java",
			    "\n" +
			    "public class T7311ab4_2 extends T7311ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7311ab4_3.java",
			    "\n" +
			    "public class T7311ab4_3 extends T7311ab4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab4_1.java",
			    "\n" +
			    "public team class Team7311ab4_1 {\n" +
			    "    public abstract class Role7311ab4_1 playedBy T7311ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab4_1.this.toString() + \".Role7311ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role7311ab4_2 extends Role7311ab4_1 playedBy T7311ab4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab4_1.this.toString() + \".Role7311ab4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team7311ab4_2.java",
			    "\n" +
			    "public team class Team7311ab4_2 extends Team7311ab4_1 {\n" +
			    "    public class Role7311ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab4_2.this.toString() + \".Role7311ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role7311ab4_3 extends Role7311ab4_2 playedBy T7311ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team7311ab4_2.this.toString() + \".Role7311ab4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team7311ab4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T7311ab4_2 as Role7311ab4_4 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7311ab4_1.java",
			    "\n" +
			    "public abstract class T7311ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T7311ab4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team7311ab4_4.Role7311ab4_4");
     }

}

