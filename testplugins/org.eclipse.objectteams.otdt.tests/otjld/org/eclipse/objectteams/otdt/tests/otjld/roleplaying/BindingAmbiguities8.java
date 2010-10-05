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
package org.eclipse.objectteams.otdt.tests.otjld.roleplaying;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class BindingAmbiguities8 extends AbstractOTJLDTest {

     public BindingAmbiguities8(String name) {
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
         return BindingAmbiguities8.class;
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.8-otjld_g-ambiguous-binding-1
     public void _g_test738_ambiguousBinding1() {
        
        runConformTest(
             new String[] {
 		"T738ab1Main.java",
			    "\n" +
			    "public class T738ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team738ab1_2 t = new Team738ab1_2();\n" +
			    "        T738ab1_2    o = new T738ab1_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab1_4.java",
			    "\n" +
			    "public team class Team738ab1_4 extends Team738ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab1_3.java",
			    "\n" +
			    "public class T738ab1_3 extends T738ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab1_1.java",
			    "\n" +
			    "public team class Team738ab1_1 {\n" +
			    "    public abstract class Role738ab1_1 playedBy T738ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab1_1.this.toString() + \".Role738ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role738ab1_2 extends Role738ab1_1 playedBy T738ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab1_1.this.toString() + \".Role738ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team738ab1_2.java",
			    "\n" +
			    "public team class Team738ab1_2 extends Team738ab1_1 {\n" +
			    "    public class Role738ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab1_2.this.toString() + \".Role738ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role738ab1_3 extends Role738ab1_2 playedBy T738ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab1_2.this.toString() + \".Role738ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T738ab1_2 as Role738ab1_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab1_1.java",
			    "\n" +
			    "public abstract class T738ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab1_3.java",
			    "\n" +
			    "public team class Team738ab1_3 extends Team738ab1_2 {\n" +
			    "    public class Role738ab1_4 extends Role738ab1_1 playedBy T738ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab1_3.this.toString() + \".Role738ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T738ab1_2.java",
			    "\n" +
			    "public class T738ab1_2 extends T738ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team738ab1_2.Role738ab1_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.8-otjld_g-ambiguous-binding-2
     public void _g_test738_ambiguousBinding2() {
        
        runConformTest(
             new String[] {
 		"T738ab2Main.java",
			    "\n" +
			    "public class T738ab2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team738ab2_2 t = new Team738ab2_3();\n" +
			    "        T738ab2_2    o = new T738ab2_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab2_4.java",
			    "\n" +
			    "public team class Team738ab2_4 extends Team738ab2_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab2_3.java",
			    "\n" +
			    "public team class Team738ab2_3 extends Team738ab2_2 {\n" +
			    "    public class Role738ab2_4 extends Role738ab2_1 playedBy T738ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab2_3.this.toString() + \".Role738ab2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T738ab2_2.java",
			    "\n" +
			    "public class T738ab2_2 extends T738ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab2_3.java",
			    "\n" +
			    "public class T738ab2_3 extends T738ab2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab2_1.java",
			    "\n" +
			    "public team class Team738ab2_1 {\n" +
			    "    public abstract class Role738ab2_1 playedBy T738ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab2_1.this.toString() + \".Role738ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role738ab2_2 extends Role738ab2_1 playedBy T738ab2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab2_1.this.toString() + \".Role738ab2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team738ab2_2.java",
			    "\n" +
			    "public team class Team738ab2_2 extends Team738ab2_1 {\n" +
			    "    public class Role738ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab2_2.this.toString() + \".Role738ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role738ab2_3 extends Role738ab2_2 playedBy T738ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab2_2.this.toString() + \".Role738ab2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T738ab2_2 as Role738ab2_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab2_1.java",
			    "\n" +
			    "public abstract class T738ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team738ab2_3.Role738ab2_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.8-otjld_g-ambiguous-binding-3
     public void _g_test738_ambiguousBinding3() {
        
        runConformTest(
             new String[] {
 		"T738ab3Main.java",
			    "\n" +
			    "public class T738ab3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team738ab3_2 t = new Team738ab3_4();\n" +
			    "        T738ab3_2    o = new T738ab3_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab3_4.java",
			    "\n" +
			    "public team class Team738ab3_4 extends Team738ab3_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab3_2.java",
			    "\n" +
			    "public team class Team738ab3_2 extends Team738ab3_1 {\n" +
			    "    public class Role738ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab3_2.this.toString() + \".Role738ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role738ab3_3 extends Role738ab3_2 playedBy T738ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab3_2.this.toString() + \".Role738ab3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T738ab3_2 as Role738ab3_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab3_1.java",
			    "\n" +
			    "public abstract class T738ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab3_3.java",
			    "\n" +
			    "public team class Team738ab3_3 extends Team738ab3_2 {\n" +
			    "    public class Role738ab3_4 extends Role738ab3_1 playedBy T738ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab3_3.this.toString() + \".Role738ab3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T738ab3_2.java",
			    "\n" +
			    "public class T738ab3_2 extends T738ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab3_3.java",
			    "\n" +
			    "public class T738ab3_3 extends T738ab3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab3_1.java",
			    "\n" +
			    "public team class Team738ab3_1 {\n" +
			    "    public abstract class Role738ab3_1 playedBy T738ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab3_1.this.toString() + \".Role738ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role738ab3_2 extends Role738ab3_1 playedBy T738ab3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab3_1.this.toString() + \".Role738ab3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
             },
             "Team738ab3_4.Role738ab3_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.8-otjld_g-ambiguous-binding-4
     public void _g_test738_ambiguousBinding4() {
        
        runConformTest(
             new String[] {
 		"T738ab4Main.java",
			    "\n" +
			    "public class T738ab4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team738ab4_2 t = new Team738ab4_2();\n" +
			    "        T738ab4_2    o = new T738ab4_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab4_4.java",
			    "\n" +
			    "public team class Team738ab4_4 extends Team738ab4_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab4_1.java",
			    "\n" +
			    "public team class Team738ab4_1 {\n" +
			    "    public abstract class Role738ab4_1 playedBy T738ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab4_1.this.toString() + \".Role738ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role738ab4_2 extends Role738ab4_1 playedBy T738ab4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab4_1.this.toString() + \".Role738ab4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team738ab4_2.java",
			    "\n" +
			    "public team class Team738ab4_2 extends Team738ab4_1 {\n" +
			    "    public class Role738ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab4_2.this.toString() + \".Role738ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role738ab4_3 extends Role738ab4_2 playedBy T738ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab4_2.this.toString() + \".Role738ab4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T738ab4_2 as Role738ab4_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab4_1.java",
			    "\n" +
			    "public abstract class T738ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab4_3.java",
			    "\n" +
			    "public team class Team738ab4_3 extends Team738ab4_2 {\n" +
			    "    public class Role738ab4_4 extends Role738ab4_1 playedBy T738ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab4_3.this.toString() + \".Role738ab4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T738ab4_2.java",
			    "\n" +
			    "public class T738ab4_2 extends T738ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab4_3.java",
			    "\n" +
			    "public class T738ab4_3 extends T738ab4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team738ab4_2.Role738ab4_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.8-otjld_g-ambiguous-binding-5
     public void _g_test738_ambiguousBinding5() {
        
        runConformTest(
             new String[] {
 		"T738ab5Main.java",
			    "\n" +
			    "public class T738ab5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team738ab5_2 t = new Team738ab5_3();\n" +
			    "        T738ab5_2    o = new T738ab5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab5_4.java",
			    "\n" +
			    "public team class Team738ab5_4 extends Team738ab5_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab5_3.java",
			    "\n" +
			    "public class T738ab5_3 extends T738ab5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab5_1.java",
			    "\n" +
			    "public team class Team738ab5_1 {\n" +
			    "    public abstract class Role738ab5_1 playedBy T738ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab5_1.this.toString() + \".Role738ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role738ab5_2 extends Role738ab5_1 playedBy T738ab5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab5_1.this.toString() + \".Role738ab5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team738ab5_2.java",
			    "\n" +
			    "public team class Team738ab5_2 extends Team738ab5_1 {\n" +
			    "    public class Role738ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab5_2.this.toString() + \".Role738ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role738ab5_3 extends Role738ab5_2 playedBy T738ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab5_2.this.toString() + \".Role738ab5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T738ab5_2 as Role738ab5_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab5_1.java",
			    "\n" +
			    "public abstract class T738ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab5_3.java",
			    "\n" +
			    "public team class Team738ab5_3 extends Team738ab5_2 {\n" +
			    "    public class Role738ab5_4 extends Role738ab5_1 playedBy T738ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab5_3.this.toString() + \".Role738ab5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T738ab5_2.java",
			    "\n" +
			    "public class T738ab5_2 extends T738ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team738ab5_3.Role738ab5_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.8-otjld_g-ambiguous-binding-6
     public void _g_test738_ambiguousBinding6() {
        
        runConformTest(
             new String[] {
 		"T738ab6Main.java",
			    "\n" +
			    "public class T738ab6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team738ab6_2 t = new Team738ab6_4();\n" +
			    "        T738ab6_2    o = new T738ab6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab6_4.java",
			    "\n" +
			    "public team class Team738ab6_4 extends Team738ab6_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab6_3.java",
			    "\n" +
			    "public team class Team738ab6_3 extends Team738ab6_2 {\n" +
			    "    public class Role738ab6_4 extends Role738ab6_1 playedBy T738ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab6_3.this.toString() + \".Role738ab6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T738ab6_2.java",
			    "\n" +
			    "public class T738ab6_2 extends T738ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab6_3.java",
			    "\n" +
			    "public class T738ab6_3 extends T738ab6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team738ab6_1.java",
			    "\n" +
			    "public team class Team738ab6_1 {\n" +
			    "    public abstract class Role738ab6_1 playedBy T738ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab6_1.this.toString() + \".Role738ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role738ab6_2 extends Role738ab6_1 playedBy T738ab6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab6_1.this.toString() + \".Role738ab6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"Team738ab6_2.java",
			    "\n" +
			    "public team class Team738ab6_2 extends Team738ab6_1 {\n" +
			    "    public class Role738ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab6_2.this.toString() + \".Role738ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role738ab6_3 extends Role738ab6_2 playedBy T738ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team738ab6_2.this.toString() + \".Role738ab6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team738ab6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t2(T738ab6_2 as Role738ab6_3 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T738ab6_1.java",
			    "\n" +
			    "public abstract class T738ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T738ab6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team738ab6_4.Role738ab6_3");
     }

}

