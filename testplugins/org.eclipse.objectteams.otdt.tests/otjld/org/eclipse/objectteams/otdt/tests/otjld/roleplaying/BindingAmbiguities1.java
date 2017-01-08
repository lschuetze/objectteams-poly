/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2014 IT Service Omikron GmbH and others.
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

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

@SuppressWarnings("unchecked")
public class BindingAmbiguities1 extends AbstractOTJLDTest {

     public BindingAmbiguities1(String name) {
             super(name);
     }

     // Static initializer to specify tests subset using TESTS_* static variables
     // All specified tests which does not belong to the class are skipped...
     static {
//        TESTS_NAMES = new String[] { "test731_ambiguousBinding1"};
//        TESTS_NUMBERS = new int { 1459 };
//        TESTS_RANGE = new int { 1097, -1 };
     }

     public static Test suite() {
         return buildComparableTestSuite(testClass());
     }

     public static Class testClass() {
         return BindingAmbiguities1.class;
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.1-otjld_g-ambiguous-binding-1
	public void test731_ambiguousBinding1() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
         options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);
        runConformTest(
             new String[] {
 		"T731ab1Main.java",
			    "\n" +
			    "public class T731ab1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team731ab1_1 t = new Team731ab1_1();\n" +
			    "        T731ab1_1    o = new T731ab1_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab1_4.java",
			    "\n" +
			    "public team class Team731ab1_4 extends Team731ab1_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab1_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab1_3.java",
			    "@SuppressWarnings(\"hidden-lifting-problem\")\n" + // this team introduces the binding ambiguity (incompatible override of liftTo methods)
			    "public team class Team731ab1_3 extends Team731ab1_2 {\n" +
			    "    public class Role731ab1_4 extends Role731ab1_1 playedBy T731ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab1_3.this.toString() + \".Role731ab1_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab1_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab1_2.java",
			    "\n" +
			    "public class T731ab1_2 extends T731ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab1_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T731ab1_3.java",
			    "\n" +
			    "public class T731ab1_3 extends T731ab1_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab1_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab1_1.java",
//			    "@SuppressWarnings(\"abstractrelevantrole\")\n" +
			    "public team class Team731ab1_1 {\n" +
			    "    public class Role731ab1_1 playedBy T731ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab1_1.this.toString() + \".Role731ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab1_2 extends Role731ab1_1 playedBy T731ab1_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab1_1.this.toString() + \".Role731ab1_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab1_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab1_1 as Role731ab1_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab1_2.java",
			    "\n" +
			    "public team class Team731ab1_2 extends Team731ab1_1 {\n" +
			    "    public class Role731ab1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab1_2.this.toString() + \".Role731ab1_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab1_3 extends Role731ab1_2 playedBy T731ab1_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab1_2.this.toString() + \".Role731ab1_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab1_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab1_1.java",
			    "\n" +
			    "public abstract class T731ab1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab1_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team731ab1_1.Role731ab1_2",
             null/*classLibs*/,
             true/*shouldFlush*/,
             null/*vmArguments*/,
             options,
             null/*requester*/);
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.1-otjld_g-ambiguous-binding-2
     public void test731_ambiguousBinding2() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
         options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);

        runConformTest(
             new String[] {
 		"T731ab2Main.java",
			    "\n" +
			    "public class T731ab2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team731ab2_1 t = new Team731ab2_2();\n" +
			    "        T731ab2_1    o = new T731ab2_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab2_4.java",
			    "\n" +
			    "public team class Team731ab2_4 extends Team731ab2_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab2_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab2_2.java",
			    "\n" +
			    "public team class Team731ab2_2 extends Team731ab2_1 {\n" +
			    "    public class Role731ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab2_2.this.toString() + \".Role731ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab2_3 extends Role731ab2_2 playedBy T731ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab2_2.this.toString() + \".Role731ab2_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab2_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab2_1.java",
			    "\n" +
			    "public abstract class T731ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab2_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab2_3.java",
			    "@SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "public team class Team731ab2_3 extends Team731ab2_2 {\n" +
			    "    public class Role731ab2_4 extends Role731ab2_1 playedBy T731ab2_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab2_3.this.toString() + \".Role731ab2_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab2_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab2_2.java",
			    "\n" +
			    "public class T731ab2_2 extends T731ab2_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab2_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T731ab2_3.java",
			    "\n" +
			    "public class T731ab2_3 extends T731ab2_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab2_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab2_1.java",
			    "\n" +
			    "public team class Team731ab2_1 {\n" +
			    "    public class Role731ab2_1 playedBy T731ab2_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab2_1.this.toString() + \".Role731ab2_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab2_2 extends Role731ab2_1 playedBy T731ab2_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab2_1.this.toString() + \".Role731ab2_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab2_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab2_1 as Role731ab2_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team731ab2_2.Role731ab2_2",
             null/*classLibs*/,
             true/*shouldFlush*/,
             null/*vmArguments*/,
             options,
             null/*requester*/);
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.1-otjld_g-ambiguous-binding-3
     public void test731_ambiguousBinding3() {
        Map options = getCompilerOptions();
        options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
        options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);
        runConformTest(
             new String[] {
 		"T731ab3Main.java",
			    "\n" +
			    "public class T731ab3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team731ab3_1 t = new Team731ab3_3();\n" +
			    "        T731ab3_1    o = new T731ab3_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab3_4.java",
			    "\n" +
			    "public team class Team731ab3_4 extends Team731ab3_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab3_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab3_1.java",
			    "\n" +
			    "public team class Team731ab3_1 {\n" +
			    "    public class Role731ab3_1 playedBy T731ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab3_1.this.toString() + \".Role731ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab3_2 extends Role731ab3_1 playedBy T731ab3_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab3_1.this.toString() + \".Role731ab3_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab3_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab3_1 as Role731ab3_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab3_2.java",
			    "\n" +
			    "public team class Team731ab3_2 extends Team731ab3_1 {\n" +
			    "    public class Role731ab3_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab3_2.this.toString() + \".Role731ab3_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab3_3 extends Role731ab3_2 playedBy T731ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab3_2.this.toString() + \".Role731ab3_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab3_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab3_1.java",
			    "\n" +
			    "public abstract class T731ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab3_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab3_3.java",
			    "@SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "public team class Team731ab3_3 extends Team731ab3_2 {\n" +
			    "    public class Role731ab3_4 extends Role731ab3_1 playedBy T731ab3_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab3_3.this.toString() + \".Role731ab3_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab3_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab3_2.java",
			    "\n" +
			    "public class T731ab3_2 extends T731ab3_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab3_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T731ab3_3.java",
			    "\n" +
			    "public class T731ab3_3 extends T731ab3_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab3_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team731ab3_3.Role731ab3_2",
             null/*classLibs*/,
             true/*shouldFlush*/,
             null/*vmArguments*/,
             options,
             null/*requester*/);
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.1-otjld_g-ambiguous-binding-4
     public void test731_ambiguousBinding4() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
         options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);
        runConformTest(
             new String[] {
 		"T731ab4Main.java",
			    "\n" +
			    "public class T731ab4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team731ab4_1 t = new Team731ab4_4();\n" +
			    "        T731ab4_1    o = new T731ab4_2();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab4_4.java",
			    "\n" +
			    "public team class Team731ab4_4 extends Team731ab4_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab4_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T731ab4_3.java",
			    "\n" +
			    "public class T731ab4_3 extends T731ab4_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab4_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab4_1.java",
			    "\n" +
			    "public team class Team731ab4_1 {\n" +
			    "    public class Role731ab4_1 playedBy T731ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab4_1.this.toString() + \".Role731ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab4_2 extends Role731ab4_1 playedBy T731ab4_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab4_1.this.toString() + \".Role731ab4_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab4_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab4_1 as Role731ab4_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab4_2.java",
			    "\n" +
			    "public team class Team731ab4_2 extends Team731ab4_1 {\n" +
			    "    public class Role731ab4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab4_2.this.toString() + \".Role731ab4_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab4_3 extends Role731ab4_2 playedBy T731ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab4_2.this.toString() + \".Role731ab4_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab4_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab4_1.java",
			    "\n" +
			    "public abstract class T731ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab4_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab4_3.java",
			    "@SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "public team class Team731ab4_3 extends Team731ab4_2 {\n" +
			    "    public class Role731ab4_4 extends Role731ab4_1 playedBy T731ab4_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab4_3.this.toString() + \".Role731ab4_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab4_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab4_2.java",
			    "\n" +
			    "public class T731ab4_2 extends T731ab4_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab4_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team731ab4_4.Role731ab4_2",
             null/*classLibs*/,
             true/*shouldFlush*/,
             null/*vmArguments*/,
             options,
             null/*requester*/);
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.1-otjld_g-ambiguous-binding-5
     public void test731_ambiguousBinding5() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
         options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);
        runConformTest(
             new String[] {
 		"T731ab5Main.java",
			    "\n" +
			    "public class T731ab5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team731ab5_1 t = new Team731ab5_1();\n" +
			    "        T731ab5_1    o = new T731ab5_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab5_4.java",
			    "\n" +
			    "public team class Team731ab5_4 extends Team731ab5_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab5_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab5_3.java",
			    "@SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "public team class Team731ab5_3 extends Team731ab5_2 {\n" +
			    "    public class Role731ab5_4 extends Role731ab5_1 playedBy T731ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab5_3.this.toString() + \".Role731ab5_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab5_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab5_2.java",
			    "\n" +
			    "public class T731ab5_2 extends T731ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab5_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T731ab5_3.java",
			    "\n" +
			    "public class T731ab5_3 extends T731ab5_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab5_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab5_1.java",
			    "\n" +
			    "public team class Team731ab5_1 {\n" +
			    "    public class Role731ab5_1 playedBy T731ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab5_1.this.toString() + \".Role731ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab5_2 extends Role731ab5_1 playedBy T731ab5_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab5_1.this.toString() + \".Role731ab5_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab5_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab5_1 as Role731ab5_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab5_2.java",
			    "\n" +
			    "public team class Team731ab5_2 extends Team731ab5_1 {\n" +
			    "    public class Role731ab5_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab5_2.this.toString() + \".Role731ab5_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab5_3 extends Role731ab5_2 playedBy T731ab5_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab5_2.this.toString() + \".Role731ab5_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab5_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab5_1.java",
			    "\n" +
			    "public abstract class T731ab5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab5_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team731ab5_1.Role731ab5_2",
             null/*classLibs*/,
             true/*shouldFlush*/,
             null/*vmArguments*/,
             options,
             null/*requester*/);
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.1-otjld_g-ambiguous-binding-6
     public void test731_ambiguousBinding6() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
         options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);
        runConformTest(
             new String[] {
 		"T731ab6Main.java",
			    "\n" +
			    "public class T731ab6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team731ab6_1 t = new Team731ab6_2();\n" +
			    "        T731ab6_1    o = new T731ab6_3();\n" +
			    "\n" +
			    "        System.out.print(t.t1(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab6_4.java",
			    "\n" +
			    "public team class Team731ab6_4 extends Team731ab6_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab6_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab6_2.java",
			    "\n" +
			    "public team class Team731ab6_2 extends Team731ab6_1 {\n" +
			    "    public class Role731ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab6_2.this.toString() + \".Role731ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab6_3 extends Role731ab6_2 playedBy T731ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab6_2.this.toString() + \".Role731ab6_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab6_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab6_1.java",
			    "\n" +
			    "public abstract class T731ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab6_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab6_3.java",
			    "@SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "public team class Team731ab6_3 extends Team731ab6_2 {\n" +
			    "    public class Role731ab6_4 extends Role731ab6_1 playedBy T731ab6_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab6_3.this.toString() + \".Role731ab6_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab6_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab6_2.java",
			    "\n" +
			    "public class T731ab6_2 extends T731ab6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab6_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T731ab6_3.java",
			    "\n" +
			    "public class T731ab6_3 extends T731ab6_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab6_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab6_1.java",
			    "\n" +
			    "public team class Team731ab6_1 {\n" +
			    "    public class Role731ab6_1 playedBy T731ab6_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab6_1.this.toString() + \".Role731ab6_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab6_2 extends Role731ab6_1 playedBy T731ab6_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab6_1.this.toString() + \".Role731ab6_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab6_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab6_1 as Role731ab6_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "Team731ab6_2.Role731ab6_3",
             null/*classLibs*/,
             true/*shouldFlush*/,
             null/*vmArguments*/,
             options,
             null/*requester*/);
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.1-otjld_g-ambiguous-binding-7
     // see Bug 327334 -  [compiler] generated lift methods fail to detect some lifting ambiguities
     public void test731_ambiguousBinding7() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
         options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);
        runConformTest(
             new String[] {
 		"T731ab7Main.java",
			    "\n" +
			    "public class T731ab7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team731ab7_1 t = new Team731ab7_3();\n" +
			    "        T731ab7_1    o = new T731ab7_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab7_4.java",
			    "\n" +
			    "public team class Team731ab7_4 extends Team731ab7_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab7_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab7_1.java",
			    "\n" +
			    "public team class Team731ab7_1 {\n" +
			    "    public class Role731ab7_1 playedBy T731ab7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab7_1.this.toString() + \".Role731ab7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab7_2 extends Role731ab7_1 playedBy T731ab7_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab7_1.this.toString() + \".Role731ab7_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab7_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab7_1 as Role731ab7_1 obj) throws org.objectteams.LiftingFailedException {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab7_2.java",
			    "\n" +
			    "public team class Team731ab7_2 extends Team731ab7_1 {\n" +
			    "    public class Role731ab7_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab7_2.this.toString() + \".Role731ab7_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab7_3 extends Role731ab7_2 playedBy T731ab7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab7_2.this.toString() + \".Role731ab7_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab7_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab7_1.java",
			    "\n" +
			    "public abstract class T731ab7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab7_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab7_3.java",
			    "@SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "public team class Team731ab7_3 extends Team731ab7_2 {\n" +
			    "    public class Role731ab7_4 extends Role731ab7_1 playedBy T731ab7_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab7_3.this.toString() + \".Role731ab7_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab7_3\";\n" +
			    "    }\n" +
			    "    public String t1(T731ab7_1 as Role731ab7_1 obj) throws org.objectteams.LiftingFailedException {\n" +
			    "        return super.t1(obj);\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab7_2.java",
			    "\n" +
			    "public class T731ab7_2 extends T731ab7_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab7_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T731ab7_3.java",
			    "\n" +
			    "public class T731ab7_3 extends T731ab7_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab7_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "org.objectteams.LiftingFailedException: \n" + 
     		 "Failed to lift \'T731ab7_3\' of class T731ab7_3 to type \'Role731ab7_1\'\n" + 
     		 "(See OT/J definition para. 2.3.4(c)).",
     		null/*classLibs*/,
            true/*shouldFlush*/,
            null/*vmArguments*/,
            options,
            null/*requester*/);
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // 7.3.1-otjld_g-ambiguous-binding-8
     // see Bug 327334 -  [compiler] generated lift methods fail to detect some lifting ambiguities
     public void test731_ambiguousBinding8() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
         options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);
        runConformTest(
             new String[] {
 		"T731ab8Main.java",
			    "\n" +
			    "public class T731ab8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team731ab8_1 t = new Team731ab8_4();\n" +
			    "        T731ab8_1    o = new T731ab8_3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (org.objectteams.LiftingFailedException ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab8_4.java",
			    "\n" +
			    "public team class Team731ab8_4 extends Team731ab8_3 {\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab8_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T731ab8_3.java",
			    "\n" +
			    "public class T731ab8_3 extends T731ab8_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab8_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab8_1.java",
			    "\n" +
			    "public team class Team731ab8_1 {\n" +
			    "    public class Role731ab8_1 playedBy T731ab8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8_1.this.toString() + \".Role731ab8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab8_2 extends Role731ab8_1 playedBy T731ab8_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8_1.this.toString() + \".Role731ab8_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab8_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab8_1 as Role731ab8_1 obj) throws org.objectteams.LiftingFailedException  {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab8_2.java",
			    "\n" +
			    "public team class Team731ab8_2 extends Team731ab8_1 {\n" +
			    "    public class Role731ab8_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8_2.this.toString() + \".Role731ab8_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab8_3 extends Role731ab8_2 playedBy T731ab8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8_2.this.toString() + \".Role731ab8_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab8_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab8_1.java",
			    "\n" +
			    "public abstract class T731ab8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab8_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab8_3.java",
			    "@SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "public team class Team731ab8_3 extends Team731ab8_2 {\n" +
			    "    public class Role731ab8_4 extends Role731ab8_1 playedBy T731ab8_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8_3.this.toString() + \".Role731ab8_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab8_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab8_2.java",
			    "\n" +
			    "public class T731ab8_2 extends T731ab8_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab8_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "org.objectteams.LiftingFailedException: \n" + 
     		 "Failed to lift \'T731ab8_3\' of class T731ab8_3 to type \'Role731ab8_1\'\n" + 
     		 "(See OT/J definition para. 2.3.4(c)).",
     		null/*classLibs*/,
            true/*shouldFlush*/,
            null/*vmArguments*/,
            options,
            null/*requester*/);
     }

     // smart-lifting of a base object to a role object results in an ambiguous binding
     // Bug 330002 - Wrong linearization of tsuper calls in diamond inheritance
     // as previous but check that error is correctly reported.
     public void test731_ambiguousBinding8e() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);
        runNegativeTestMultiResult(
             new String[] {
		"T731ab8e_3.java",
			    "\n" +
			    "public class T731ab8e_3 extends T731ab8e_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab8e_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab8e_1.java",
			    "\n" +
			    "public team class Team731ab8e_1 {\n" +
			    "    public class Role731ab8e_1 playedBy T731ab8e_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8e_1.this.toString() + \".Role731ab8e_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab8e_2 extends Role731ab8e_1 playedBy T731ab8e_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8e_1.this.toString() + \".Role731ab8e_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab8e_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab8e_1 as Role731ab8e_1 obj) throws org.objectteams.LiftingFailedException  {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab8e_2.java",
			    "\n" +
			    "public team class Team731ab8e_2 extends Team731ab8e_1 {\n" +
			    "    public class Role731ab8e_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8e_2.this.toString() + \".Role731ab8e_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab8e_3 extends Role731ab8e_2 playedBy T731ab8e_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8e_2.this.toString() + \".Role731ab8e_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab8e_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab8e_1.java",
			    "\n" +
			    "public abstract class T731ab8e_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab8e_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab8e_3.java",
			    "public team class Team731ab8e_3 extends Team731ab8e_2 {\n" +
			    "    public class Role731ab8e_4 extends Role731ab8e_1 playedBy T731ab8e_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab8e_3.this.toString() + \".Role731ab8e_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab8e_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab8e_2.java",
			    "\n" +
			    "public class T731ab8e_2 extends T731ab8e_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab8e_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             options,
             new String[] { // expect errors on same location in either order
	     		"----------\n" + 
				"1. WARNING in Team731ab8e_3.java (at line 1)\n" + 
				"	public team class Team731ab8e_3 extends Team731ab8e_2 {\n" + 
				"	                  ^^^^^^^^^^^^^\n" + 
				"Potential ambiguity in role binding. The base \'T731ab8e_3\' is bound to the following roles: Team731ab8e_3.Role731ab8e_3,Team731ab8e_3.Role731ab8e_4 (OTJLD 2.3.4(a)).\n" + 
				"----------\n" + 
				"2. ERROR in Team731ab8e_3.java (at line 1)\n" + 
				"	public team class Team731ab8e_3 extends Team731ab8e_2 {\n" + 
				"	                  ^^^^^^^^^^^^^\n" + 
				"Team introduces binding ambiguity for role Role731ab8e_3<@tthis[Team731ab8e_3]>, which may break clients of the super team (OTJLD 2.3.5(d)).\n" + 
				"----------\n" + 
				"3. ERROR in Team731ab8e_3.java (at line 1)\n" + 
				"	public team class Team731ab8e_3 extends Team731ab8e_2 {\n" + 
				"	                  ^^^^^^^^^^^^^\n" + 
				"Team introduces binding ambiguity for role Role731ab8e_2<@tthis[Team731ab8e_3]>, which may break clients of the super team (OTJLD 2.3.5(d)).\n" + 
				"----------\n" + 
				"4. ERROR in Team731ab8e_3.java (at line 1)\n" + 
				"	public team class Team731ab8e_3 extends Team731ab8e_2 {\n" + 
				"	                  ^^^^^^^^^^^^^\n" + 
				"Team introduces binding ambiguity for role Role731ab8e_1<@tthis[Team731ab8e_3]>, which may break clients of the super team (OTJLD 2.3.5(d)).\n"  +
				"----------\n",
	     		"----------\n" + 
				"1. WARNING in Team731ab8e_3.java (at line 1)\n" + 
				"	public team class Team731ab8e_3 extends Team731ab8e_2 {\n" + 
				"	                  ^^^^^^^^^^^^^\n" + 
				"Potential ambiguity in role binding. The base \'T731ab8e_3\' is bound to the following roles: Team731ab8e_3.Role731ab8e_3,Team731ab8e_3.Role731ab8e_4 (OTJLD 2.3.4(a)).\n" + 
				"----------\n" + 
				"2. ERROR in Team731ab8e_3.java (at line 1)\n" + 
				"	public team class Team731ab8e_3 extends Team731ab8e_2 {\n" + 
				"	                  ^^^^^^^^^^^^^\n" + 
				"Team introduces binding ambiguity for role Role731ab8e_1<@tthis[Team731ab8e_3]>, which may break clients of the super team (OTJLD 2.3.5(d)).\n" + 
				"----------\n" + 
				"3. ERROR in Team731ab8e_3.java (at line 1)\n" + 
				"	public team class Team731ab8e_3 extends Team731ab8e_2 {\n" + 
				"	                  ^^^^^^^^^^^^^\n" + 
				"Team introduces binding ambiguity for role Role731ab8e_3<@tthis[Team731ab8e_3]>, which may break clients of the super team (OTJLD 2.3.5(d)).\n" +
				"----------\n" + 
				"4. ERROR in Team731ab8e_3.java (at line 1)\n" + 
				"	public team class Team731ab8e_3 extends Team731ab8e_2 {\n" + 
				"	                  ^^^^^^^^^^^^^\n" + 
				"Team introduces binding ambiguity for role Role731ab8e_2<@tthis[Team731ab8e_3]>, which may break clients of the super team (OTJLD 2.3.5(d)).\n" +
				"----------\n"
             } );
     }
     // smart-lifting of a base object to a role object results in an ambiguous binding
     // manual addition: subtype T4 (OK) of base T2 which also has an ambiguous sub type T3
     // see Bug 327334 -  [compiler] generated lift methods fail to detect some lifting ambiguities
     public void test731_ambiguousBinding9() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
         options.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotation, CompilerOptions.DISABLED);
        runConformTest(
             new String[] {
 		"T731ab9Main.java",
			    "\n" +
			    "public class T731ab9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team731ab9_1 t = new Team731ab9_3();\n" +
			    "        T731ab9_1    o = new T731ab9_4();\n" +
			    "\n" +
			    "        try {\n" +
			    "            System.out.print(t.t1(o));\n" +
			    "        } catch (Exception ex) {\n" +
			    "            System.out.print(ex.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n",
		"T731ab9_3.java",
			    "\n" +
			    "public class T731ab9_3 extends T731ab9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab9_3\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab9_1.java",
			    "\n" +
			    "public team class Team731ab9_1 {\n" +
			    "    public class Role731ab9_1 playedBy T731ab9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab9_1.this.toString() + \".Role731ab9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role731ab9_2 extends Role731ab9_1 playedBy T731ab9_2 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab9_1.this.toString() + \".Role731ab9_2\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab9_1\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String t1(T731ab9_1 as Role731ab9_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab9_2.java",
			    "\n" +
			    "public team class Team731ab9_2 extends Team731ab9_1 {\n" +
			    "    public class Role731ab9_1 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab9_2.this.toString() + \".Role731ab9_1\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role731ab9_3 extends Role731ab9_2 playedBy T731ab9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab9_2.this.toString() + \".Role731ab9_3\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab9_2\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab9_1.java",
			    "\n" +
			    "public abstract class T731ab9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab9_1\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team731ab9_3.java",
			    "@SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "public team class Team731ab9_3 extends Team731ab9_2 {\n" +
			    "    public class Role731ab9_4 extends Role731ab9_1 playedBy T731ab9_3 {\n" +
			    "        public String toString() {\n" +
			    "            return Team731ab9_3.this.toString() + \".Role731ab9_4\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"Team731ab9_3\";\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n",
		"T731ab9_2.java",
			    "\n" +
			    "public class T731ab9_2 extends T731ab9_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab9_2\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T731ab9_4.java",
			    "\n" +
			    "public class T731ab9_4 extends T731ab9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"T731ab9_4\";\n" +
			    "    }\n" +
			    "}\n" +
			    "\n"
             },
             "Team731ab9_3.Role731ab9_2",
             null/*classLibs*/,
             true/*shouldFlush*/,
             null/*vmArguments*/,
             options,
             null/*requester*/);
     }

}

