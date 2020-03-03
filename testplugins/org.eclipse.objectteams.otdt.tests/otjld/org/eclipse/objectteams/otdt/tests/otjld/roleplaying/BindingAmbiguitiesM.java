/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2010 Stephan Herrmann
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
 *        Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.roleplaying;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

// non-generated (manual) tests for binding ambiguities
public class BindingAmbiguitiesM extends AbstractOTJLDTest {

     public BindingAmbiguitiesM(String name) {
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
         return BindingAmbiguitiesM.class;
     }

     // lifting to existing role despite binding ambiguity
     // see  Bug 316200 -  [otjld] Method bindings in role with binding ambiguity?
     @SuppressWarnings("unchecked")
	 public void test73M_ambiguousBinding1() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
         runConformTest(
              new String[] {
  		"Team73Mab1.java",
  			    "import org.objectteams.LiftingFailedException;\n" +
  			    "@SuppressWarnings(\"ambiguousbinding\")\n" +
  			    "public team class Team73Mab1 {\n" +
  			    "    precedence R2_R, R1_R;\n" +
  			    "    \n" +
  			    "    protected abstract class R playedBy T73Mab1 {\n" +
  			    "        @SuppressWarnings(\"basecall\")\n" +
  			    "        callin void foo() { System.out.print(getClass().getName()); }\n" +
  			    "		 @SuppressWarnings(\"hidden-lifting-problem\")\n" +
  			    "        foo <- replace test; // lifting not recommended\n" +
  			    "    }\n" +
  			    "    protected class R1_R extends R\n" +
  			    "        base when (Team73Mab1.this.hasRole(base, R1_R.class))\n" +
  			    "    {\n" +
  			    "    }\n" +
  			    "    protected class R2_R extends R\n" +
  			    "        base when (Team73Mab1.this.hasRole(base, R2_R.class))\n" +
  			    "    {\n" +
  			    "    }\n" +
  			    "    \n" +
  			    "    Team73Mab1 (T73Mab1 as R1_R o) throws LiftingFailedException {}\n" +
  			    "    public static void main(String[] args) throws LiftingFailedException {\n" +
  			    "        T73Mab1 o = new T73Mab1();\n" +
  			    "        Team73Mab1 t = new Team73Mab1(o);\n" +
  			    "        t.activate();\n" +
  			    "        o.test();\n" +
  			    "    }\n" +
  			    "}\n" +
  			    "    \n",
  		"T73Mab1.java",
  			    "\n" +
  			    "public class T73Mab1 {\n" +
  			    "    void test() {}\n" +
  			    "}\n" +
  			    "    \n"
              },
              "Team73Mab1$__OT__R1_R",
              null/*classLibs*/,
              true/*shouldFlush*/,
              null/*vmArguments*/,
              options,
              null/*requester*/);     
     }

     // callin binding despite binding ambiguity regarding super base and intermediate roles,
     // specific base is unambiguously bound to a more specific role.
     // see  Bug 316200 -  [otjld] Method bindings in role with binding ambiguity?
     @SuppressWarnings("unchecked")
     public void test73M_ambiguousBinding2() {
         Map options = getCompilerOptions();
         options.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);

         runConformTest(
              new String[] {
  		"Team73Mab2.java",
  			    "\n" +
  			    "@SuppressWarnings({\"ambiguousbinding\", \"hidden-lifting-problem\"})\n" +
  			    "public team class Team73Mab2 {\n" +
  			    "    protected abstract class R playedBy T73Mab2_1 {\n" +
  			    "        @SuppressWarnings(\"basecall\")\n" +
  			    "        callin void foo() { System.out.print(getClass().getName()); }\n" +
  			    "        foo <- replace test; // lifting not recommended\n" +
  			    "    }\n" +
  			    "    protected class R1_R extends R { }\n" +
  			    "    protected class R2_R extends R { }\n" +
  			    "    protected class R3_R extends R2_R playedBy T73Mab2_2 { }\n" +
  			    "    \n" +
  			    "    public static void main(String[] args) {\n" +
  			    "        T73Mab2_2 o = new T73Mab2_2();\n" +
  			    "        Team73Mab2 t = new Team73Mab2();\n" +
  			    "        t.activate();\n" +
  			    "        o.test();\n" +
  			    "    }\n" +
  			    "}\n",
  		"T73Mab2_1.java",
  			    "\n" +
  			    "public class T73Mab2_1 {\n" +
  			    "    void test() {}\n" +
  			    "}\n",
  		"T73Mab2_2.java",
  			    "\n" +
  			    "public class T73Mab2_2 extends T73Mab2_1 {\n" +
  			    "}\n"
              },
              "Team73Mab2$__OT__R3_R",
              null/*classLibs*/,
              true/*shouldFlush*/,
              null/*vmArguments*/,
              options,
              null/*requester*/);     
     }

}

