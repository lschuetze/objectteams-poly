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

import java.util.Map;

import junit.framework.Test;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class BindingAmbiguities2 extends AbstractOTJLDTest {

     public BindingAmbiguities2(String name) {
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
         return BindingAmbiguities2.class;
     }
     
     @SuppressWarnings("unchecked")
	 @Override
     protected Map getCompilerOptions() {
    	 Map options = super.getCompilerOptions();
    	 options.put(CompilerOptions.OPTION_ReportAbstractPotentialRelevantRole, CompilerOptions.IGNORE);
    	 return options;
    }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.2-otjld_g-ambiguous-binding-1
     public void test732_ambiguousBinding1() {
        
        runConformTest(
             new String[] {
 		"T732ab1Main.java",
			    "\n" +
			    "public class T732ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team732ab1_1 t = new Team732ab1_1();\n" +
			    "        T732ab1_2    o = new T732ab1_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab1_4.java",
			    "\n" +
			    "public team class Team732ab1_4 extends Team732ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab1_2.java",
			    "\n" +
			    "public team class Team732ab1_2 extends Team732ab1_1 {\n" +
			    "    public class Role732ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab1_2.this.toString() + \".Role732ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role732ab1_3 extends Role732ab1_2 playedBy T732ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab1_2.this.toString() + \".Role732ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab1_1.java",
			    "\n" +
			    "public abstract class T732ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab1_3.java",
			    "\n" +
			    "public team class Team732ab1_3 extends Team732ab1_2 {\n" +
			    "    public class Role732ab1_4 extends Role732ab1_1 playedBy T732ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab1_3.this.toString() + \".Role732ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab1_2.java",
			    "\n" +
			    "public class T732ab1_2 extends T732ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T732ab1_3.java",
			    "\n" +
			    "public class T732ab1_3 extends T732ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab1_1.java",
			    "\n" +
			    "public team class Team732ab1_1 {\n" +
			    "    protected abstract class Role732ab1_1 playedBy T732ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab1_1.this.toString() + \".Role732ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role732ab1_2 extends Role732ab1_1 playedBy T732ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab1_1.this.toString() + \".Role732ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T732ab1_2 as Role732ab1_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team732ab1_1.Role732ab1_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.2-otjld_g-ambiguous-binding-2
     public void test732_ambiguousBinding2() {
        
        runConformTest(
             new String[] {
 		"T732ab2Main.java",
			    "\n" +
			    "public class T732ab2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team732ab2_1 t = new Team732ab2_2();\n" +
			    "        T732ab2_2    o = new T732ab2_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab2_4.java",
			    "\n" +
			    "public team class Team732ab2_4 extends Team732ab2_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab2_1.java",
			    "\n" +
			    "public team class Team732ab2_1 {\n" +
			    "    protected abstract class Role732ab2_1 playedBy T732ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab2_1.this.toString() + \".Role732ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role732ab2_2 extends Role732ab2_1 playedBy T732ab2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab2_1.this.toString() + \".Role732ab2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T732ab2_2 as Role732ab2_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab2_2.java",
			    "\n" +
			    "public team class Team732ab2_2 extends Team732ab2_1 {\n" +
			    "    public class Role732ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab2_2.this.toString() + \".Role732ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role732ab2_3 extends Role732ab2_2 playedBy T732ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab2_2.this.toString() + \".Role732ab2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab2_1.java",
			    "\n" +
			    "public abstract class T732ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab2_3.java",
			    "\n" +
			    "public team class Team732ab2_3 extends Team732ab2_2 {\n" +
			    "    public class Role732ab2_4 extends Role732ab2_1 playedBy T732ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab2_3.this.toString() + \".Role732ab2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab2_2.java",
			    "\n" +
			    "public class T732ab2_2 extends T732ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T732ab2_3.java",
			    "\n" +
			    "public class T732ab2_3 extends T732ab2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team732ab2_2.Role732ab2_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.2-otjld_g-ambiguous-binding-3
     public void test732_ambiguousBinding3() {
        
        runConformTest(
             new String[] {
 		"T732ab3Main.java",
			    "\n" +
			    "public class T732ab3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team732ab3_1 t = new Team732ab3_3();\n" +
			    "        T732ab3_2    o = new T732ab3_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab3_4.java",
			    "\n" +
			    "public team class Team732ab3_4 extends Team732ab3_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T732ab3_3.java",
			    "\n" +
			    "public class T732ab3_3 extends T732ab3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab3_1.java",
			    "\n" +
			    "public team class Team732ab3_1 {\n" +
			    "    protected abstract class Role732ab3_1 playedBy T732ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab3_1.this.toString() + \".Role732ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role732ab3_2 extends Role732ab3_1 playedBy T732ab3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab3_1.this.toString() + \".Role732ab3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T732ab3_2 as Role732ab3_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab3_2.java",
			    "\n" +
			    "public team class Team732ab3_2 extends Team732ab3_1 {\n" +
			    "    public class Role732ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab3_2.this.toString() + \".Role732ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role732ab3_3 extends Role732ab3_2 playedBy T732ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab3_2.this.toString() + \".Role732ab3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab3_1.java",
			    "\n" +
			    "public abstract class T732ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab3_3.java",
			    "\n" +
			    "public team class Team732ab3_3 extends Team732ab3_2 {\n" +
			    "    public class Role732ab3_4 extends Role732ab3_1 playedBy T732ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab3_3.this.toString() + \".Role732ab3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab3_2.java",
			    "\n" +
			    "public class T732ab3_2 extends T732ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team732ab3_3.Role732ab3_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.2-otjld_g-ambiguous-binding-4
     public void test732_ambiguousBinding4() {
        
        runConformTest(
             new String[] {
 		"T732ab4Main.java",
			    "\n" +
			    "public class T732ab4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team732ab4_1 t = new Team732ab4_4();\n" +
			    "        T732ab4_2    o = new T732ab4_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab4_4.java",
			    "\n" +
			    "public team class Team732ab4_4 extends Team732ab4_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab4_3.java",
			    "\n" +
			    "public team class Team732ab4_3 extends Team732ab4_2 {\n" +
			    "    public class Role732ab4_4 extends Role732ab4_1 playedBy T732ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab4_3.this.toString() + \".Role732ab4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab4_2.java",
			    "\n" +
			    "public class T732ab4_2 extends T732ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T732ab4_3.java",
			    "\n" +
			    "public class T732ab4_3 extends T732ab4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab4_1.java",
			    "\n" +
			    "public team class Team732ab4_1 {\n" +
			    "    protected abstract class Role732ab4_1 playedBy T732ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab4_1.this.toString() + \".Role732ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role732ab4_2 extends Role732ab4_1 playedBy T732ab4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab4_1.this.toString() + \".Role732ab4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T732ab4_2 as Role732ab4_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab4_2.java",
			    "\n" +
			    "public team class Team732ab4_2 extends Team732ab4_1 {\n" +
			    "    public class Role732ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab4_2.this.toString() + \".Role732ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role732ab4_3 extends Role732ab4_2 playedBy T732ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab4_2.this.toString() + \".Role732ab4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab4_1.java",
			    "\n" +
			    "public abstract class T732ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team732ab4_4.Role732ab4_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.2-otjld_g-ambiguous-binding-5
     public void test732_ambiguousBinding5() {
        
        runConformTest(
             new String[] {
 		"T732ab5Main.java",
			    "\n" +
			    "public class T732ab5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team732ab5_1 t = new Team732ab5_1();\n" +
			    "        T732ab5_2    o = new T732ab5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab5_4.java",
			    "\n" +
			    "public team class Team732ab5_4 extends Team732ab5_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab5_2.java",
			    "\n" +
			    "public team class Team732ab5_2 extends Team732ab5_1 {\n" +
			    "    public class Role732ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab5_2.this.toString() + \".Role732ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role732ab5_3 extends Role732ab5_2 playedBy T732ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab5_2.this.toString() + \".Role732ab5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab5_1.java",
			    "\n" +
			    "public abstract class T732ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab5_3.java",
			    "\n" +
			    "public team class Team732ab5_3 extends Team732ab5_2 {\n" +
			    "    public class Role732ab5_4 extends Role732ab5_1 playedBy T732ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab5_3.this.toString() + \".Role732ab5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab5_2.java",
			    "\n" +
			    "public class T732ab5_2 extends T732ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T732ab5_3.java",
			    "\n" +
			    "public class T732ab5_3 extends T732ab5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab5_1.java",
			    "\n" +
			    "public team class Team732ab5_1 {\n" +
			    "    protected abstract class Role732ab5_1 playedBy T732ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab5_1.this.toString() + \".Role732ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role732ab5_2 extends Role732ab5_1 playedBy T732ab5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab5_1.this.toString() + \".Role732ab5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T732ab5_2 as Role732ab5_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team732ab5_1.Role732ab5_2");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.2-otjld_g-ambiguous-binding-6
     public void test732_ambiguousBinding6() {
        
        runConformTest(
             new String[] {
 		"T732ab6Main.java",
			    "\n" +
			    "public class T732ab6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team732ab6_1 t = new Team732ab6_2();\n" +
			    "        T732ab6_2    o = new T732ab6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab6_4.java",
			    "\n" +
			    "public team class Team732ab6_4 extends Team732ab6_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab6_1.java",
			    "\n" +
			    "public team class Team732ab6_1 {\n" +
			    "    protected abstract class Role732ab6_1 playedBy T732ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab6_1.this.toString() + \".Role732ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role732ab6_2 extends Role732ab6_1 playedBy T732ab6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab6_1.this.toString() + \".Role732ab6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T732ab6_2 as Role732ab6_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab6_2.java",
			    "\n" +
			    "public team class Team732ab6_2 extends Team732ab6_1 {\n" +
			    "    public class Role732ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab6_2.this.toString() + \".Role732ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role732ab6_3 extends Role732ab6_2 playedBy T732ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab6_2.this.toString() + \".Role732ab6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab6_1.java",
			    "\n" +
			    "public abstract class T732ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab6_3.java",
			    "\n" +
			    "public team class Team732ab6_3 extends Team732ab6_2 {\n" +
			    "    public class Role732ab6_4 extends Role732ab6_1 playedBy T732ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab6_3.this.toString() + \".Role732ab6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab6_2.java",
			    "\n" +
			    "public class T732ab6_2 extends T732ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T732ab6_3.java",
			    "\n" +
			    "public class T732ab6_3 extends T732ab6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team732ab6_2.Role732ab6_3");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.2-otjld_g-ambiguous-binding-7
     public void test732_ambiguousBinding7() {
        
        runConformTest(
             new String[] {
 		"T732ab7Main.java",
			    "\n" +
			    "public class T732ab7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team732ab7_1 t = new Team732ab7_3();\n" +
			    "        T732ab7_2    o = new T732ab7_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab7_4.java",
			    "\n" +
			    "public team class Team732ab7_4 extends Team732ab7_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T732ab7_3.java",
			    "\n" +
			    "public class T732ab7_3 extends T732ab7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab7_1.java",
			    "\n" +
			    "public team class Team732ab7_1 {\n" +
			    "    protected abstract class Role732ab7_1 playedBy T732ab7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab7_1.this.toString() + \".Role732ab7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role732ab7_2 extends Role732ab7_1 playedBy T732ab7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab7_1.this.toString() + \".Role732ab7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T732ab7_2 as Role732ab7_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab7_2.java",
			    "\n" +
			    "public team class Team732ab7_2 extends Team732ab7_1 {\n" +
			    "    public class Role732ab7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab7_2.this.toString() + \".Role732ab7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role732ab7_3 extends Role732ab7_2 playedBy T732ab7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab7_2.this.toString() + \".Role732ab7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab7_1.java",
			    "\n" +
			    "public abstract class T732ab7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab7_3.java",
			    "\n" +
			    "public team class Team732ab7_3 extends Team732ab7_2 {\n" +
			    "    public class Role732ab7_4 extends Role732ab7_1 playedBy T732ab7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab7_3.this.toString() + \".Role732ab7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab7_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab7_2.java",
			    "\n" +
			    "public class T732ab7_2 extends T732ab7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
     		 "org.objectteams.LiftingFailedException: \n" + 
             "Failed to lift 'T732ab7_3' of class T732ab7_3 to type 'Role732ab7_1'\n" +
        	 "(See OT/J definition para. 2.3.4(c)).");
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.2-otjld_g-ambiguous-binding-8
     public void test732_ambiguousBinding8() {
        
        runConformTest(
             new String[] {
 		"T732ab8Main.java",
			    "\n" +
			    "public class T732ab8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team732ab8_1 t = new Team732ab8_4();\n" +
			    "        T732ab8_2    o = new T732ab8_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab8_4.java",
			    "\n" +
			    "public team class Team732ab8_4 extends Team732ab8_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab8_3.java",
			    "\n" +
			    "public team class Team732ab8_3 extends Team732ab8_2 {\n" +
			    "    public class Role732ab8_4 extends Role732ab8_1 playedBy T732ab8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab8_3.this.toString() + \".Role732ab8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab8_2.java",
			    "\n" +
			    "public class T732ab8_2 extends T732ab8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T732ab8_3.java",
			    "\n" +
			    "public class T732ab8_3 extends T732ab8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab8_1.java",
			    "\n" +
			    "public team class Team732ab8_1 {\n" +
			    "    protected abstract class Role732ab8_1 playedBy T732ab8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab8_1.this.toString() + \".Role732ab8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role732ab8_2 extends Role732ab8_1 playedBy T732ab8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab8_1.this.toString() + \".Role732ab8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T732ab8_2 as Role732ab8_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team732ab8_2.java",
			    "\n" +
			    "public team class Team732ab8_2 extends Team732ab8_1 {\n" +
			    "    public class Role732ab8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab8_2.this.toString() + \".Role732ab8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role732ab8_3 extends Role732ab8_2 playedBy T732ab8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team732ab8_2.this.toString() + \".Role732ab8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team732ab8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T732ab8_1.java",
			    "\n" +
			    "public abstract class T732ab8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T732ab8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
     		 "org.objectteams.LiftingFailedException: \n" + 
             "Failed to lift 'T732ab8_3' of class T732ab8_3 to type 'Role732ab8_1'\n" +
             "(See OT/J definition para. 2.3.4(c)).");
     }

}

