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

public class BindingAmbiguities6 extends AbstractOTJLDTest {

     public BindingAmbiguities6(String name) {
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
         return BindingAmbiguities6.class;
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.6-otjld_g-ambiguous-binding-1
     public void _g_test736_ambiguousBinding1() {
        
        runConformTest(
             new String[] {
 		"T736ab1Main.java",
			    "\n" +
			    "public class T736ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team736ab1_1 t = new Team736ab1_1();\n" +
			    "        T736ab1_3    o = new T736ab1_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab1_4.java",
			    "\n" +
			    "public team class Team736ab1_4 extends Team736ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab1_2.java",
			    "\n" +
			    "public team class Team736ab1_2 extends Team736ab1_1 {\n" +
			    "    public class Role736ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab1_2.this.toString() + \".Role736ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role736ab1_3 extends Role736ab1_2 playedBy T736ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab1_2.this.toString() + \".Role736ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T736ab1_1.java",
			    "\n" +
			    "public abstract class T736ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab1_3.java",
			    "\n" +
			    "public team class Team736ab1_3 extends Team736ab1_2 {\n" +
			    "    public class Role736ab1_4 extends Role736ab1_1 playedBy T736ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab1_3.this.toString() + \".Role736ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T736ab1_2.java",
			    "\n" +
			    "public class T736ab1_2 extends T736ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T736ab1_3.java",
			    "\n" +
			    "public class T736ab1_3 extends T736ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab1_1.java",
			    "\n" +
			    "public team class Team736ab1_1 {\n" +
			    "    public abstract class Role736ab1_1 playedBy T736ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab1_1.this.toString() + \".Role736ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role736ab1_2 extends Role736ab1_1 playedBy T736ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab1_1.this.toString() + \".Role736ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T736ab1_3 as Role736ab1_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team736ab1_1.Role736ab1_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.6-otjld_g-ambiguous-binding-2
     public void _g_test736_ambiguousBinding2() {
        
        runConformTest(
             new String[] {
 		"T736ab2Main.java",
			    "\n" +
			    "public class T736ab2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team736ab2_1 t = new Team736ab2_2();\n" +
			    "        T736ab2_3    o = new T736ab2_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab2_4.java",
			    "\n" +
			    "public team class Team736ab2_4 extends Team736ab2_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab2_1.java",
			    "\n" +
			    "public team class Team736ab2_1 {\n" +
			    "    public abstract class Role736ab2_1 playedBy T736ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab2_1.this.toString() + \".Role736ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role736ab2_2 extends Role736ab2_1 playedBy T736ab2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab2_1.this.toString() + \".Role736ab2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T736ab2_3 as Role736ab2_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab2_2.java",
			    "\n" +
			    "public team class Team736ab2_2 extends Team736ab2_1 {\n" +
			    "    public class Role736ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab2_2.this.toString() + \".Role736ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role736ab2_3 extends Role736ab2_2 playedBy T736ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab2_2.this.toString() + \".Role736ab2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T736ab2_1.java",
			    "\n" +
			    "public abstract class T736ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab2_3.java",
			    "\n" +
			    "public team class Team736ab2_3 extends Team736ab2_2 {\n" +
			    "    public class Role736ab2_4 extends Role736ab2_1 playedBy T736ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab2_3.this.toString() + \".Role736ab2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T736ab2_2.java",
			    "\n" +
			    "public class T736ab2_2 extends T736ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T736ab2_3.java",
			    "\n" +
			    "public class T736ab2_3 extends T736ab2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team736ab2_2.Role736ab2_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.6-otjld_g-ambiguous-binding-3
     public void _g_test736_ambiguousBinding3() {
        
        runConformTest(
             new String[] {
 		"T736ab3Main.java",
			    "\n" +
			    "public class T736ab3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team736ab3_1 t = new Team736ab3_3();\n" +
			    "        T736ab3_3    o = new T736ab3_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab3_4.java",
			    "\n" +
			    "public team class Team736ab3_4 extends Team736ab3_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T736ab3_3.java",
			    "\n" +
			    "public class T736ab3_3 extends T736ab3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab3_1.java",
			    "\n" +
			    "public team class Team736ab3_1 {\n" +
			    "    public abstract class Role736ab3_1 playedBy T736ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab3_1.this.toString() + \".Role736ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role736ab3_2 extends Role736ab3_1 playedBy T736ab3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab3_1.this.toString() + \".Role736ab3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T736ab3_3 as Role736ab3_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab3_2.java",
			    "\n" +
			    "public team class Team736ab3_2 extends Team736ab3_1 {\n" +
			    "    public class Role736ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab3_2.this.toString() + \".Role736ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role736ab3_3 extends Role736ab3_2 playedBy T736ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab3_2.this.toString() + \".Role736ab3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T736ab3_1.java",
			    "\n" +
			    "public abstract class T736ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab3_3.java",
			    "\n" +
			    "public team class Team736ab3_3 extends Team736ab3_2 {\n" +
			    "    public class Role736ab3_4 extends Role736ab3_1 playedBy T736ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab3_3.this.toString() + \".Role736ab3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T736ab3_2.java",
			    "\n" +
			    "public class T736ab3_2 extends T736ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Failed to lift 'T736ab3_3' of class T736ab3_3 to type 'Role736ab3_2'\n" +
             "(See OT/J definition para. 7.3(c)).");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.6-otjld_g-ambiguous-binding-4
     public void _g_test736_ambiguousBinding4() {
        
        runConformTest(
             new String[] {
 		"T736ab4Main.java",
			    "\n" +
			    "public class T736ab4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team736ab4_1 t = new Team736ab4_4();\n" +
			    "        T736ab4_3    o = new T736ab4_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab4_4.java",
			    "\n" +
			    "public team class Team736ab4_4 extends Team736ab4_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab4_3.java",
			    "\n" +
			    "public team class Team736ab4_3 extends Team736ab4_2 {\n" +
			    "    public class Role736ab4_4 extends Role736ab4_1 playedBy T736ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab4_3.this.toString() + \".Role736ab4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T736ab4_2.java",
			    "\n" +
			    "public class T736ab4_2 extends T736ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T736ab4_3.java",
			    "\n" +
			    "public class T736ab4_3 extends T736ab4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab4_1.java",
			    "\n" +
			    "public team class Team736ab4_1 {\n" +
			    "    public abstract class Role736ab4_1 playedBy T736ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab4_1.this.toString() + \".Role736ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role736ab4_2 extends Role736ab4_1 playedBy T736ab4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab4_1.this.toString() + \".Role736ab4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T736ab4_3 as Role736ab4_2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team736ab4_2.java",
			    "\n" +
			    "public team class Team736ab4_2 extends Team736ab4_1 {\n" +
			    "    public class Role736ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab4_2.this.toString() + \".Role736ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role736ab4_3 extends Role736ab4_2 playedBy T736ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team736ab4_2.this.toString() + \".Role736ab4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team736ab4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T736ab4_1.java",
			    "\n" +
			    "public abstract class T736ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T736ab4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Failed to lift 'T736ab4_3' of class T736ab4_3 to type 'Role736ab4_2'\n" +
             "(See OT/J definition para. 7.3(c)).");
     }

}

