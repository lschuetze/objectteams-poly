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
package org.eclipse.objectteams.otdt.tests.otjld.teamactivation;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class ImplicitTeamActivation extends AbstractOTJLDTest {

	public ImplicitTeamActivation(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test5317_concurrentActivation1"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return ImplicitTeamActivation.class;
	}

	static final String IMPLICIT_ACTIVATION_ALWAYS    = "-Dot.implicit.team.activation=ALWAYS";
	static final String IMPLICIT_ACTIVATION_ANNOTATED = "-Dot.implicit.team.activation=ANNOTATED";
	static final String IMPLICIT_ACTIVATION_NEVER     = "-Dot.implicit.team.activation=NEVER";

	// a team is explicitly activated and a callin binding is called in the activation context
    // 5.3.1-otjld-callin-invocation-in-team-method
    public void test531_callinInvocationInTeamMethod() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T531ciitmMain.java",
			    "\n" +
			    "public class T531ciitmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team531ciitm t = new Team531ciitm();\n" +
			    "\n" +
			    "        t.test();\n" +
			    "        System.out.print(T531ciitm_1.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T531ciitm_1.java",
			    "\n" +
			    "public class T531ciitm_1 {\n" +
			    "    private static String value = \"\";\n" +
			    "    public static void addValue(String txt) {\n" +
			    "        value += txt;\n" +
			    "    }\n" +
			    "    public static String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T531ciitm_2.java",
			    "\n" +
			    "public class T531ciitm_2 {\n" +
			    "    public void test() {\n" +
			    "        T531ciitm_1.addValue(\"a\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team531ciitm.java",
			    "\n" +
			    "public team class Team531ciitm {\n" +
			    "    public class Role531ciitm playedBy T531ciitm_2 {\n" +
			    "        public void test() {\n" +
			    "            T531ciitm_1.addValue(\"b\");\n" +
			    "        }\n" +
			    "        test <- before test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        T531ciitm_2 o = new T531ciitm_2();\n" +
			    "\n" +
			    "        o.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "ba",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a team-level method is called while the team is not (expicitly) activated - after that call the team has to be deactivated again
    // 5.3.1a-otjld-callin-invocation-in-team-method
    public void test531a_callinInvocationInTeamMethod() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T531aciitmMain.java",
			    "\n" +
			    "public class T531aciitmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team531aciitm t = new Team531aciitm();\n" +
			    "		T531aciitm_2 o = new T531aciitm_2();\n" +
			    "        t.test(o);\n" +
			    "		o.test();\n" +
			    "        System.out.print(T531aciitm_1.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T531aciitm_1.java",
			    "\n" +
			    "public class T531aciitm_1 {\n" +
			    "    private static String value = \"\";\n" +
			    "    public static void addValue(String txt) {\n" +
			    "        value += txt;\n" +
			    "    }\n" +
			    "    public static String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T531aciitm_2.java",
			    "\n" +
			    "public class T531aciitm_2 {\n" +
			    "    public void test() {\n" +
			    "        T531aciitm_1.addValue(\"a\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team531aciitm.java",
			    "\n" +
			    "public team class Team531aciitm {\n" +
			    "    public class Role531aciitm playedBy T531aciitm_2 {\n" +
			    "        public void test() {\n" +
			    "            T531aciitm_1.addValue(\"b\");\n" +
			    "        }\n" +
			    "        test <- before test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T531aciitm_2 o) {\n" +
			    "        //T531aciitm_2 o = new T531aciitm_2();\n" +
			    "	if (o.equals(\"just another return branch\")) {\n" +
			    "        	return;\n" +
			    "        }\n" +
			    "	if (o!=null) {\n" +
			    "        	o.test();\n" +
			    "		return;\n" +
			    "	}\n" +
			    "	// never reached because o!=null is always true above.\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "baa",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a team-level method is called while the team is not (explicitly) activated - after that call the team has to be deactivated again (also if an exception has been thrown)
    // 5.3.1b-otjld-callin-invocation-in-team-method
    public void test531b_callinInvocationInTeamMethod() {
       if (isKnownFailure(this.getClass().getName()+".test531b_callinInvocationInTeamMethod"))
    	   return;
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T531bciitmMain.java",
			    "\n" +
			    "public class T531bciitmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team531bciitm t = new Team531bciitm();\n" +
			    "		T531bciitm_2 o = new T531bciitm_2();\n" +
			    "        try {\n" +
			    "			t.test(o);\n" +
			    "		} catch (ArrayIndexOutOfBoundsException aioobe) {}\n" +
			    "		o.test();\n" +
			    "        System.out.print(T531bciitm_1.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T531bciitm_1.java",
			    "\n" +
			    "public class T531bciitm_1 {\n" +
			    "    private static String value = \"\";\n" +
			    "    public static void addValue(String txt) {\n" +
			    "        value += txt;\n" +
			    "    }\n" +
			    "    public static String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T531bciitm_2.java",
			    "\n" +
			    "public class T531bciitm_2 {\n" +
			    "    public void test() {\n" +
			    "        T531bciitm_1.addValue(\"a\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team531bciitm.java",
			    "\n" +
			    "public team class Team531bciitm {\n" +
			    "    public class Role531bciitm playedBy T531bciitm_2 {\n" +
			    "        public void test() {\n" +
			    "            T531bciitm_1.addValue(\"b\");\n" +
			    "        }\n" +
			    "        test <- before test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T531bciitm_2 o) throws ArrayIndexOutOfBoundsException {\n" +
			    "        //T531bciitm_2 o = new T531bciitm_2();\n" +
			    "		 if (o.equals(\"just another return branch\")) {\n" +
			    "        	return;\n" +
			    "        }\n" +
			    "		 if (o!=null) {\n" +
			    "        	o.test();\n" +
			    "			int[] ia = new int[1];\n" +
			    "           int i = ia[100];\n" +
			    "           return;\n" +
			    "		 }\n" +
			    "		 // never reached because o!=null is always true above.\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "baa",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // implicit activation in a method with try-catch
    // 5.3.1c-otjld-callin-invocation-in-team-method
    public void test531c_callinInvocationInTeamMethod() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"Team531cciitm.java",
			    "\n" +
			    "public team class Team531cciitm {\n" +
			    "	protected class R playedBy T531cciitm {\n" +
			    "		void r() {\n" +
			    "			System.out.print(\"r\");\n" +
			    "		}\n" +
			    "		r <- after test;\n" +
			    "	}\n" +
			    "	public void testT(T531cciitm b) {\n" +
			    "		try {\n" +
			    "			if (!b.test())\n" +
			    "				throw new Exception(\"Failed\");\n" +
			    "		} catch (Exception ex) {\n" +
			    "			System.out.print(\"Ignored\");\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team531cciitm t = new Team531cciitm(); // don't activate here\n" +
			    "		T531cciitm b = new T531cciitm();\n" +
			    "		b.test();\n" +
			    "		t.testT(b);\n" +
			    "		b.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T531cciitm.java",
			    "\n" +
			    "public class T531cciitm {\n" +
			    "	public boolean test() {\n" +
			    "		System.out.print(\"b\");\n" +
			    "		return true;\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "bbrb",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // within is used in a team method
    // 5.3.2-otjld-within-in-team-method
    public void test532_withinInTeamMethod() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T532witmMain.java",
			    "\n" +
			    "public class T532witmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team532witm t = new Team532witm();\n" +
			    "\n" +
			    "        t.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T532witm.java",
			    "\n" +
			    "public class T532witm {\n" +
			    "    public String getValue() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team532witm.java",
			    "\n" +
			    "public team class Team532witm {\n" +
			    "    public class Role532witm playedBy T532witm {\n" +
			    "        callin String getValue() {\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        String getValue() <- replace String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        T532witm o = new T532witm();\n" +
			    "\n" +
			    "        within (this) {\n" +
			    "            System.out.print(o.getValue());\n" +
			    "        }\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "bb",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // activate is called in a team method
    // 5.3.3-otjld-activate-in-team-method
    public void test533_activateInTeamMethod() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T533aitmMain.java",
			    "\n" +
			    "public class T533aitmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team533aitm t = new Team533aitm();\n" +
			    "\n" +
			    "        t.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T533aitm.java",
			    "\n" +
			    "public class T533aitm {\n" +
			    "    public String getValue() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team533aitm.java",
			    "\n" +
			    "public team class Team533aitm {\n" +
			    "    public class Role533aitm playedBy T533aitm {\n" +
			    "        callin String test() {\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        test <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        T533aitm o = new T533aitm();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "        activate();\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "bb",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // activate is called in a team method - team has to be active afterwards!
    // 5.3.3a-otjld-activate-in-team-method
    public void test533a_activateInTeamMethod() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T533aaitmMain.java",
			    "\n" +
			    "public class T533aaitmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team533aaitm t = new Team533aaitm();\n" +
			    "        \n" +
			    "        T533aaitm o = new T533aaitm();\n" +
			    "        System.out.print(o.getValue());\n" +
			    "        t.doActivation();\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T533aaitm.java",
			    "\n" +
			    "public class T533aaitm {\n" +
			    "    public String getValue() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team533aaitm.java",
			    "\n" +
			    "public team class Team533aaitm {\n" +
			    "    public class Role533aaitm playedBy T533aaitm {\n" +
			    "        callin String test() {\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        test <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void doActivation() {\n" +
			    "        activate();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "ab",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // deactivate is called in a team method
    // 5.3.4-otjld-deactivate-in-team-method
    public void test534_deactivateInTeamMethod() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T534ditmMain.java",
			    "\n" +
			    "public class T534ditmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team534ditm t = new Team534ditm();\n" +
			    "\n" +
			    "        t.test();\n" +
			    "        System.out.print(T534ditm_1.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T534ditm_1.java",
			    "\n" +
			    "public class T534ditm_1 {\n" +
			    "    private static String value = \"\";\n" +
			    "    public static void addValue(String txt) {\n" +
			    "        value += txt;\n" +
			    "    }\n" +
			    "    public static String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T534ditm_2.java",
			    "\n" +
			    "public class T534ditm_2 {\n" +
			    "    public void test() {\n" +
			    "        T534ditm_1.addValue(\"a\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team534ditm.java",
			    "\n" +
			    "public team class Team534ditm {\n" +
			    "    public class Role534ditm playedBy T534ditm_2 {\n" +
			    "        public void doSomething() {\n" +
			    "            T534ditm_1.addValue(\"b\");\n" +
			    "        }\n" +
			    "        doSomething <- after test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        T534ditm_2 o = new T534ditm_2();\n" +
			    "\n" +
			    "        o.test();\n" +
			    "        T534ditm_1.addValue(\"-\");\n" +
			    "        deactivate();\n" +
			    "        o.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "ab-a",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // an activation context from another instance of the same team class is used within a team method
    // 5.3.5-otjld-different-context-in-team-method-1
    public void test535_differentContextInTeamMethod1() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T535dcitm1Main.java",
			    "\n" +
			    "public class T535dcitm1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team535dcitm1 t = new Team535dcitm1(\"b\");\n" +
			    "\n" +
			    "        t.test();\n" +
			    "        System.out.print(T535dcitm1_1.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T535dcitm1_1.java",
			    "\n" +
			    "public class T535dcitm1_1 {\n" +
			    "    private static String value = \"\";\n" +
			    "    public static void addValue(String txt) {\n" +
			    "        value += txt;\n" +
			    "    }\n" +
			    "    public static String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T535dcitm1_2.java",
			    "\n" +
			    "public class T535dcitm1_2 {\n" +
			    "    public void test() {\n" +
			    "        T535dcitm1_1.addValue(\"a\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team535dcitm1.java",
			    "\n" +
			    "public team class Team535dcitm1 {\n" +
			    "    private String msg;\n" +
			    "\n" +
			    "    public Team535dcitm1(String msg) {\n" +
			    "        this.msg = msg;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role535dcitm1 playedBy T535dcitm1_2 {\n" +
			    "        public void test() {\n" +
			    "            T535dcitm1_1.addValue(Team535dcitm1.this.msg);\n" +
			    "        }\n" +
			    "        test <- after test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        Team535dcitm1 otherT = new Team535dcitm1(\"c\");\n" +
			    "        T535dcitm1_2  o      = new T535dcitm1_2();\n" +
			    "\n" +
			    "        o.test();\n" +
			    "        T535dcitm1_1.addValue(\"-\");\n" +
			    "        // we have now two teams active\n" +
			    "        within (otherT) {\n" +
			    "            o.test();\n" +
			    "            T535dcitm1_1.addValue(\"-\");\n" +
			    "        }\n" +
			    "        o.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "ab-abc-ab",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // an activation context from a base team is used within a team method
    // 5.3.5-otjld-different-context-in-team-method-2
    public void test535_differentContextInTeamMethod2() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T535dcitm2Main.java",
			    "\n" +
			    "public class T535dcitm2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team535dcitm2_2 t = new Team535dcitm2_2();\n" +
			    "\n" +
			    "        t.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T535dcitm2.java",
			    "\n" +
			    "public class T535dcitm2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team535dcitm2_1.java",
			    "\n" +
			    "public team class Team535dcitm2_1 {\n" +
			    "    public class Role535dcitm2_1 playedBy T535dcitm2 {\n" +
			    "        callin String getValue() {\n" +
			    "            base.getValue();\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        gv1:\n" +
			    "        String getValue() <- replace String getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team535dcitm2_2.java",
			    "\n" +
			    "public team class Team535dcitm2_2 extends Team535dcitm2_1 {\n" +
			    "    public class Role535dcitm2_1 {\n" +
			    "        callin String getValue() {\n" +
			    "            base.getValue();\n" +
			    "            return \"d\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role535dcitm2_2 playedBy T535dcitm2 {\n" +
			    "        callin String getValue() {\n" +
			    "            base.getValue();\n" +
			    "            return \"c\";\n" +
			    "        }\n" +
			    "        gv2:\n" +
			    "        String getValue() <- replace String getValue();\n" +
			    "    }\n" +
			    "    precedence Role535dcitm2_2.gv2, Role535dcitm2_1.gv1;\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        Team535dcitm2_1 other = new Team535dcitm2_1();\n" +
			    "        T535dcitm2      o     = new T535dcitm2();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "        System.out.print(\"-\");\n" +
			    "        other.activate();\n" +
			    "        System.out.print(o.getValue());\n" +
			    "        System.out.print(\"-\");\n" +
			    "        other.deactivate();\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "c-b-c",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // an activation context from a sub team is used within a team method
    // 5.3.5-otjld-different-context-in-team-method-3
    public void test535_differentContextInTeamMethod3() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T535dcitm3Main.java",
			    "\n" +
			    "public class T535dcitm3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team535dcitm3_1 t = new Team535dcitm3_1();\n" +
			    "\n" +
			    "        t.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T535dcitm3.java",
			    "\n" +
			    "public class T535dcitm3 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team535dcitm3_1.java",
			    "\n" +
			    "public team class Team535dcitm3_1 {\n" +
			    "    public class Role535dcitm3_1 playedBy T535dcitm3 {\n" +
			    "        callin String test() {\n" +
			    "            base.test();\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        gv1:\n" +
			    "        test <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        Team535dcitm3_2 other = new Team535dcitm3_2();\n" +
			    "        T535dcitm3      o     = new T535dcitm3();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "        System.out.print(\"-\");\n" +
			    "        within (other) {\n" +
			    "            System.out.print(o.getValue());\n" +
			    "            System.out.print(\"-\");\n" +
			    "        }\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team535dcitm3_2.java",
			    "\n" +
			    "public team class Team535dcitm3_2 extends Team535dcitm3_1 {\n" +
			    "    public class Role535dcitm3_1 {\n" +
			    "        callin String getValue() {\n" +
			    "            \n" +
			    "            return \"c(\"+base.getValue()+\")\";\n" +
			    "        }\n" +
			    "        gv1: // overrides inherited binding\n" +
			    "        getValue <- replace getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "b-c(b)-b",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // an activation context from a different team is used within a team method
    // 5.3.5-otjld-different-context-in-team-method-4
    public void test535_differentContextInTeamMethod4() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T535dcitm4Main.java",
			    "\n" +
			    "public class T535dcitm4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team535dcitm4_2 t = new Team535dcitm4_2();\n" +
			    "\n" +
			    "        t.test();\n" +
			    "        System.out.print(T535dcitm4_1.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T535dcitm4_1.java",
			    "\n" +
			    "public class T535dcitm4_1 {\n" +
			    "    private static String value = \"\";\n" +
			    "    public static void addValue(String txt) {\n" +
			    "        value += txt;\n" +
			    "    }\n" +
			    "    public static String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T535dcitm4_2.java",
			    "\n" +
			    "public class T535dcitm4_2 {\n" +
			    "    public void test() {\n" +
			    "        T535dcitm4_1.addValue(\"a\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team535dcitm4_1.java",
			    "\n" +
			    "public team class Team535dcitm4_1 {\n" +
			    "    public class Role535dcitm4_1 playedBy T535dcitm4_2 {\n" +
			    "        public void test() {\n" +
			    "            T535dcitm4_1.addValue(\"b\");\n" +
			    "        }\n" +
			    "        test <- before test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team535dcitm4_2.java",
			    "\n" +
			    "public team class Team535dcitm4_2 {\n" +
			    "    public class Role535dcitm4_2 playedBy T535dcitm4_2 {\n" +
			    "        public void test() {\n" +
			    "            T535dcitm4_1.addValue(\"c\");\n" +
			    "        }\n" +
			    "        test <- after test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        Team535dcitm4_1 otherT = new Team535dcitm4_1();\n" +
			    "        T535dcitm4_2    o      = new T535dcitm4_2();\n" +
			    "\n" +
			    "        o.test();\n" +
			    "        T535dcitm4_1.addValue(\"-\");\n" +
			    "        within (otherT) {\n" +
			    "            o.test();\n" +
			    "            T535dcitm4_1.addValue(\"-\");\n" +
			    "        }\n" +
			    "        o.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "ac-bac-ac",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // within a team/role hierarchy multiple roles callin-bind to the same base method as 'before'/'after'; the order of activation of the teams defines the invocation order of these bindings
    // 5.3.6-otjld-activation-order
    public void test536_activationOrder() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T536aoMain.java",
			    "\n" +
			    "public class T536aoMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team536ao_3 t = new Team536ao_3();\n" +
			    "\n" +
			    "        t.test();\n" +
			    "        System.out.print(T536ao_1.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T536ao_1.java",
			    "\n" +
			    "public class T536ao_1 {\n" +
			    "    private static String value = \"\";\n" +
			    "    public static void addValue(String txt) {\n" +
			    "        value += txt;\n" +
			    "    }\n" +
			    "    public static String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T536ao_2.java",
			    "\n" +
			    "public class T536ao_2 {\n" +
			    "    public void test() {\n" +
			    "        T536ao_1.addValue(\"-\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team536ao_1.java",
			    "\n" +
			    "public team class Team536ao_1 {\n" +
			    "    public class Role536ao_1 playedBy T536ao_2 {\n" +
			    "        public void test1() {\n" +
			    "            T536ao_1.addValue(\"|1b|\");\n" +
			    "        }\n" +
			    "        public void test2() {\n" +
			    "            T536ao_1.addValue(\"|1a|\");\n" +
			    "        }\n" +
			    "        at:\n" +
			    "        test2 <- after  test;\n" +
			    "        bt:\n" +
			    "        test1 <- before test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team536ao_2.java",
			    "\n" +
			    "public team class Team536ao_2 extends Team536ao_1 {\n" +
			    "    public class Role536ao_2 extends Role536ao_1 {\n" +
			    "        public void test3() {\n" +
			    "            T536ao_1.addValue(\"|2b|\");\n" +
			    "        }\n" +
			    "        public void test4() {\n" +
			    "            T536ao_1.addValue(\"|2a|\");\n" +
			    "        }\n" +
			    "        bt:\n" +
			    "        test3 <- before test;\n" +
			    "        at:\n" +
			    "        test4 <- after  test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team536ao_3.java",
			    "\n" +
			    "public team class Team536ao_3  {\n" +
			    "    public class Role536ao_2 playedBy T536ao_2 {\n" +
			    "        public void test1() {\n" +
			    "            T536ao_1.addValue(\"|3b|\");\n" +
			    "        }\n" +
			    "        public void test2() {\n" +
			    "            T536ao_1.addValue(\"|3a|\");\n" +
			    "        }\n" +
			    "        bt:\n" +
			    "        test1 <- before test;\n" +
			    "        at:\n" +
			    "        test2 <- after  test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        Team536ao_1 t1 = new Team536ao_1();\n" +
			    "        Team536ao_2 t2 = new Team536ao_2();\n" +
			    "        T536ao_2    o  = new T536ao_2();\n" +
			    "\n" +
			    "        within (t2) {\n" +
			    "            t1.activate();\n" +
			    "            o.test();\n" +
			    "        }\n" +
			    "        o.test();\n" +
			    "        System.out.print(T536ao_1.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "|1b||2b||3b|-|3a||2a||1a||1b||3b|-|3a||1a||1b||2b||3b|-|3a||2a||1a||1b||3b|-|3a||1a|",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // within a team/role hierarchy multiple roles callin-bind to the same base method as 'before'/'after'; the order of activation of the teams defines the invocation order of these bindings
    // 5.3.7-otjld-activation-order
    public void test537_activationOrder() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T537aoMain.java",
			    "\n" +
			    "public class T537aoMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team537ao_3 t = new Team537ao_3();\n" +
			    "\n" +
			    "        t.test();\n" +
			    "        System.out.print(T537ao_1.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T537ao_1.java",
			    "\n" +
			    "public class T537ao_1 {\n" +
			    "    private static String value = \"\";\n" +
			    "    public static void addValue(String txt) {\n" +
			    "        value += txt;\n" +
			    "    }\n" +
			    "    public static String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T537ao_2.java",
			    "\n" +
			    "public class T537ao_2 {\n" +
			    "    public void test() {\n" +
			    "        T537ao_1.addValue(\"-\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team537ao_1.java",
			    "\n" +
			    "public team class Team537ao_1 {\n" +
			    "    public class Role537ao_1 playedBy T537ao_2 {\n" +
			    "        public void test1() {\n" +
			    "            T537ao_1.addValue(\"|1b|\");\n" +
			    "        }\n" +
			    "        public void test2() {\n" +
			    "            T537ao_1.addValue(\"|1a|\");\n" +
			    "        }\n" +
			    "        at:\n" +
			    "        test2 <- after  test;\n" +
			    "        bt:\n" +
			    "        test1 <- before test;\n" +
			    "    }\n" +
			    "    public class Role537ao_2 extends Role537ao_1 {\n" +
			    "        public void test3() {\n" +
			    "            T537ao_1.addValue(\"|2b|\");\n" +
			    "        }\n" +
			    "        public void test4() {\n" +
			    "            T537ao_1.addValue(\"|2a|\");\n" +
			    "        }\n" +
			    "        bt:\n" +
			    "        test3 <- before test;\n" +
			    "        at:\n" +
			    "        test4 <- after  test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team537ao_2.java",
			    "\n" +
			    "public team class Team537ao_2 extends Team537ao_1 {\n" +
			    "    public class Role537ao_2 {\n" +
			    "        public void test3() {\n" +
			    "            T537ao_1.addValue(\"|3b|\");\n" +
			    "        }\n" +
			    "        public void test4() {\n" +
			    "            T537ao_1.addValue(\"|3a|\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team537ao_3.java",
			    "\n" +
			    "public team class Team537ao_3  {\n" +
			    "    public class Role537ao_2 playedBy T537ao_2 {\n" +
			    "        public void test1() {\n" +
			    "            T537ao_1.addValue(\"|4b|\");\n" +
			    "        }\n" +
			    "        public void test2() {\n" +
			    "            T537ao_1.addValue(\"|4a|\");\n" +
			    "        }\n" +
			    "        bt:\n" +
			    "        test1 <- before test;\n" +
			    "        at:\n" +
			    "        test2 <- after  test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        Team537ao_1 t1 = new Team537ao_1();\n" +
			    "        Team537ao_2 t2 = new Team537ao_2();\n" +
			    "        T537ao_2    o  = new T537ao_2();\n" +
			    "\n" +
			    "        t1.activate();\n" +
			    "        within (t2) {\n" +
			    "            t1.deactivate();\n" +
			    "            o.test();\n" +
			    "        }\n" +
			    "        o.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "|3b||4b|-|4a||3a||4b|-|4a|",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // implicit activation of a team does not apply to another thread using the same team
    // 5.3.8-otjld-activation-context-and-thread
    public void test538_activationContextAndThread() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T538acatMain.java",
			    "\n" +
			    "public class T538acatMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team538acat t = new Team538acat();\n" +
			    "        T538acat_2  o = new T538acat_2();\n" +
			    "\n" +
			    "        System.out.print(o);\n" +
			    "        t.test(o);\n" +
			    "        T538acat_1.markMethodAsExited();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T538acat_1.java",
			    "\n" +
			    "public class T538acat_1 {\n" +
			    "    private static int contextStatus = 0;\n" +
			    "    public synchronized static void allowMethodExit() {\n" +
			    "        contextStatus = 1;\n" +
			    "    }\n" +
			    "    public synchronized static boolean isAllowedToExitMethod() {\n" +
			    "        return contextStatus == 1;\n" +
			    "    }\n" +
			    "    public synchronized static void markMethodAsExited() {\n" +
			    "        contextStatus = 2;\n" +
			    "    }\n" +
			    "    public synchronized static boolean hasMethodExited() {\n" +
			    "        return contextStatus == 2;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T538acat_2.java",
			    "\n" +
			    "public class T538acat_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Thread538acat.java",
			    "\n" +
			    "public class Thread538acat extends Thread {\n" +
			    "    private T538acat_2 obj;\n" +
			    "    public Thread538acat(T538acat_2 obj) {\n" +
			    "        super(\"\");\n" +
			    "        this.obj = obj;\n" +
			    "    }\n" +
			    "    public void run() {\n" +
			    "        System.out.print(obj);\n" +
			    "        T538acat_1.allowMethodExit();\n" +
			    "        while (!T538acat_1.hasMethodExited()) {\n" +
			    "            try\n" +
			    "            {\n" +
			    "                sleep(10);\n" +
			    "            }\n" +
			    "            catch (InterruptedException ex)\n" +
			    "            {}\n" +
			    "        }\n" +
			    "        System.out.print(obj);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team538acat.java",
			    "\n" +
			    "public team class Team538acat {\n" +
			    "    public class Role538acat playedBy T538acat_2 {\n" +
			    "        callin String getValue() {\n" +
			    "            base.getValue();\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        getValue <- replace toString;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T538acat_2 obj) {\n" +
			    "        Thread538acat r = new Thread538acat(obj);\n" +
			    "\n" +
			    "        System.out.print(obj);\n" +
			    "        r.start();\n" +
			    "        while (!T538acat_1.isAllowedToExitMethod()) {\n" +
			    "            try\n" +
			    "            {\n" +
			    "                Thread.sleep(10);\n" +
			    "            }\n" +
			    "            catch (InterruptedException ex)\n" +
			    "            {}\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "abaa",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a callin binding of an externalized role is called
    // 5.3.9-otjld-callin-binding-of-externalized-role
    public void test539_callinBindingOfExternalizedRole() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T539cboerMain.java",
			    "\n" +
			    "public class T539cboerMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team539cboer t = new Team539cboer();\n" +
			    "        Role539cboer_2<@t> r = t.new Role539cboer_2();\n" +
			    "        T539cboer_1        o = new T539cboer_1();\n" +
			    "\n" +
			    "        System.out.print(r.extractValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T539cboer_1.java",
			    "\n" +
			    "public class T539cboer_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T539cboer_2.java",
			    "\n" +
			    "public class T539cboer_2 {}\n" +
			    "    \n",
		"Team539cboer.java",
			    "\n" +
			    "public team class Team539cboer {\n" +
			    "    public class Role539cboer_1 playedBy T539cboer_1 {\n" +
			    "        callin String test() {\n" +
			    "            base.test();\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        test <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role539cboer_2 playedBy T539cboer_2 {\n" +
			    "    	public Role539cboer_2() {\n" +
			    "		base();\n" +
			    "	}\n" +
			    "        public String extractValue(T539cboer_1 obj) {\n" +
			    "            return obj.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "b",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // the final team instance of an externalized role is used in a within statement -- missing constructor
    // 5.3.10-otjld-callin-binding-with-final-team-1f
    public void test5310_callinBindingWithFinalTeam1f() {
        runNegativeTestMatching(
            new String[] {
		"T5310cbwft1fMain.java",
			    "\n" +
			    "public class T5310cbwft1fMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team5310cbwft1f t = new Team5310cbwft1f();\n" +
			    "        Role5310cbwft1f_2<@t> r = t.new Role5310cbwft1f_2();\n" +
			    "        T5310cbwft1f_1        o = new T5310cbwft1f_1();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "        within (t) {\n" +
			    "            System.out.print(o.getValue());\n" +
			    "        }\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5310cbwft1f_1.java",
			    "\n" +
			    "public class T5310cbwft1f_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5310cbwft1f_2.java",
			    "\n" +
			    "public class T5310cbwft1f_2 {}\n" +
			    "    \n",
		"Team5310cbwft1f.java",
			    "\n" +
			    "public team class Team5310cbwft1f {\n" +
			    "    public class Role5310cbwft1f_1 playedBy T5310cbwft1f_1 {\n" +
			    "        callin String getValue() {\n" +
			    "            base.getValue();\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        getValue <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role5310cbwft1f_2 playedBy T5310cbwft1f_1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "constructor");
    }

    // the final team instance of an externalized role is used in a within statement
    // 5.3.10-otjld-callin-binding-with-final-team-1
    public void test5310_callinBindingWithFinalTeam1() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T5310cbwft1Main.java",
			    "\n" +
			    "public class T5310cbwft1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team5310cbwft1 t = new Team5310cbwft1();\n" +
			    "        Role5310cbwft1_2<@t> r = t.new Role5310cbwft1_2();\n" +
			    "        T5310cbwft1_1        o = new T5310cbwft1_1();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "        within (t) {\n" +
			    "            System.out.print(o.getValue());\n" +
			    "        }\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5310cbwft1_1.java",
			    "\n" +
			    "public class T5310cbwft1_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5310cbwft1_2.java",
			    "\n" +
			    "public class T5310cbwft1_2 {}\n" +
			    "    \n",
		"Team5310cbwft1.java",
			    "\n" +
			    "public team class Team5310cbwft1 {\n" +
			    "    public class Role5310cbwft1_1 playedBy T5310cbwft1_1 {\n" +
			    "        callin String getValue() {\n" +
			    "            base.getValue();\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        getValue <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role5310cbwft1_2 playedBy T5310cbwft1_1 {\n" +
			    "	public Role5310cbwft1_2() {\n" +
			    "		base();\n" +
			    "	};    	\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "aba",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // the final team instance of an externalized role is used in a within statement
    // 5.3.10-otjld-callin-binding-with-final-team-2
    public void test5310_callinBindingWithFinalTeam2() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T5310cbwft2Main.java",
			    "\n" +
			    "public class T5310cbwft2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team5310cbwft2 t = new Team5310cbwft2();\n" +
			    "        Role5310cbwft2_2<@t> r = t.new Role5310cbwft2_2();\n" +
			    "        T5310cbwft2_1        o = new T5310cbwft2_1();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "        t.activate();\n" +
			    "        System.out.print(o.getValue());\n" +
			    "        t.deactivate();\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5310cbwft2_1.java",
			    "\n" +
			    "public class T5310cbwft2_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5310cbwft2_2.java",
			    "\n" +
			    "public class T5310cbwft2_2 {}\n" +
			    "    \n",
		"Team5310cbwft2.java",
			    "\n" +
			    "public team class Team5310cbwft2 {\n" +
			    "    public class Role5310cbwft2_1 playedBy T5310cbwft2_1 {\n" +
			    "        callin String getValue() {\n" +
			    "            base.getValue();\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        getValue <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role5310cbwft2_2 playedBy T5310cbwft2_1 {\n" +
			    "    	public Role5310cbwft2_2() {\n" +
			    "		base();\n" +
			    "	}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "aba",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // an activated team instance is used to create an externalized role which in turn invokes a callin binding
    // 5.3.13-otjld-externalized-role-with-active-team
    public void test5313_externalizedRoleWithActiveTeam() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T5313erwatMain.java",
			    "\n" +
			    "public class T5313erwatMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team5313erwat t = new Team5313erwat();\n" +
			    "        T5313erwat_1        o = new T5313erwat_1();\n" +
			    "	Role5313erwat_2<@t> r = null;\n" +
			    "	\n" +
			    "        within (t) {\n" +
			    "            System.out.print(o);\n" +
			    "\n" +
			    "            r = t.new Role5313erwat_2();\n" +
			    "\n" +
			    "            System.out.print(r.extractValue(o));\n" +
			    "            System.out.print(o);\n" +
			    "        }\n" +
			    "        System.out.print(o);\n" +
			    "        System.out.print(r.extractValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5313erwat_1.java",
			    "\n" +
			    "public class T5313erwat_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5313erwat_2.java",
			    "\n" +
			    "public class T5313erwat_2 {}\n" +
			    "    \n",
		"Team5313erwat.java",
			    "\n" +
			    "public team class Team5313erwat {\n" +
			    "    public class Role5313erwat_1 playedBy T5313erwat_1 {\n" +
			    "        callin String test() {\n" +
			    "            base.test();\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        test <- replace toString;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role5313erwat_2 playedBy T5313erwat_2 {\n" +
			    "        public String extractValue(T5313erwat_1 obj) {\n" +
			    "            return obj.toString();\n" +
			    "        }\n" +
			    "	public Role5313erwat_2() {\n" +
			    "		base();\n" +
			    "	}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "bbbab",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a team method, a nested team method and a role method have to activate the right teams
    // 5.3.14-otjld-implicit-activation-for-role-methods
    public void test5314_implicitActivationForRoleMethods() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T5314iafrmMain.java",
			    "\n" +
			    "public class T5314iafrmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final MyOuterTeam5314iafrm t = new MyOuterTeam5314iafrm();\n" +
			    "        t.tm();\n" +
			    "        final MyInnerTeam5314iafrm<@t> ti = t.new MyInnerTeam5314iafrm();\n" +
			    "        ti.trm();\n" +
			    "        MyRole5314iafrm<@ti> r = ti.new MyRole5314iafrm();\n" +
			    "        r.rm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"MyOuterTeam5314iafrm.java",
			    "\n" +
			    "public team class MyOuterTeam5314iafrm {\n" +
			    "    public void tm() {\n" +
			    "        System.out.println(\"MyOuterTeam.tm()\");\n" +
			    "        System.out.println(\"this: \"+isActive());\n" +
			    "    }\n" +
			    "    \n" +
			    "    public team class MyInnerTeam5314iafrm {\n" +
			    "        public void trm() {\n" +
			    "            System.out.println(\"MyOuterTeam.MyInnerTeam.trm()\");\n" +
			    "            System.out.println(\"this: \" + isActive());\n" +
			    "            System.out.println(\"outer: \" + MyOuterTeam5314iafrm.this.isActive());\n" +
			    "        }\n" +
			    "    \n" +
			    "        public class MyRole5314iafrm {\n" +
			    "            public MyRole5314iafrm() {}\n" +
			    "            \n" +
			    "            public void rm() {\n" +
			    "                System.out.println(\"MyTeam.MyRole.rm()\");\n" +
			    "                System.out.println(\"outer: \" + MyInnerTeam5314iafrm.this.isActive());\n" +
			    "                System.out.println(\"outer.outer: \" + MyOuterTeam5314iafrm.this.isActive());\n" +
			    "            }\n" +
			    "        }\n" +
			    "\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "MyOuterTeam.tm()\n" +
			"this: true\n" +
			"MyOuterTeam.MyInnerTeam.trm()\n" +
			"this: true\n" +
			"outer: true\n" +
			"MyTeam.MyRole.rm()\n" +
			"outer: true\n" +
			"outer.outer: false",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // implicit activation of a team does not apply to other threads
    // 5.3.15-otjld-implicit-activation-and-threads-1
    public void test5315_implicitActivationAndThreads1() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T5315iaat1Main.java",
			    "\n" +
			    "public class T5315iaat1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team5315iaat1().tm(new T5315iaat1());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5315iaat1.java",
			    "\n" +
			    "public class T5315iaat1 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team5315iaat1.java",
			    "\n" +
			    "public team class Team5315iaat1 {\n" +
			    "    public class MyRole playedBy T5315iaat1 {\n" +
			    "        callin void rm() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        rm <- replace bm;\n" +
			    "\n" +
			    "        abstract public void doCallout();\n" +
			    "        doCallout -> bm;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void tm(T5315iaat1 as MyRole r) {\n" +
			    "\n" +
			    "        Runnable bmc = new Runnable() {\n" +
			    "            public void run() {\n" +
			    "                new T5315iaat1().bm();\n" +
			    "            }\n" +
			    "        };\n" +
			    "        Thread baseMethodCallThread = new Thread(bmc);\n" +
			    "        baseMethodCallThread.start();\n" +
			    "        try {\n" +
			    "            baseMethodCallThread.join();\n" +
			    "        } catch (InterruptedException ie) {\n" +
			    "            ie.printStackTrace();\n" +
			    "        }\n" +
			    "        r.doCallout();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "OK",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // implicit activation ends as soon the current thread leaves the outermost teamlevel method
    // 5.3.15-otjld-implicit-activation-and-threads-2
    public void test5315_implicitActivationAndThreads2() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"T5315iaat2Main.java",
			    "\n" +
			    "public class T5315iaat2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team5315iaat2().tm(new T5315iaat2());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5315iaat2.java",
			    "\n" +
			    "public class T5315iaat2 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team5315iaat2.java",
			    "\n" +
			    "public team class Team5315iaat2 {\n" +
			    "    public class MyRole playedBy T5315iaat2 {\n" +
			    "        callin void rm() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        rm <- replace bm;\n" +
			    "\n" +
			    "        abstract public void doCallout();\n" +
			    "        doCallout -> bm;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void tm(T5315iaat2 as MyRole r) {\n" +
			    "        final MyRole r_final = r;\n" +
			    "        Runnable bmc = new Runnable() {\n" +
			    "            public void run() {\n" +
			    "                tm2(r_final);\n" +
			    "                // implicit activation for this thread should be finished!\n" +
			    "                new T5315iaat2().bm();\n" +
			    "            }\n" +
			    "        };\n" +
			    "        Thread baseMethodCallThread = new Thread(bmc);\n" +
			    "        baseMethodCallThread.start();\n" +
			    "        try {\n" +
			    "            baseMethodCallThread.join();\n" +
			    "        } catch (InterruptedException ie) {\n" +
			    "            ie.printStackTrace();\n" +
			    "        }\n" +
			    "        r.doCallout();\n" +
			    "    }\n" +
			    "\n" +
			    "    public void tm2(MyRole r) {\n" +
			    "        r.doCallout();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "KOK",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ALWAYS }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a method is annotated to use implicit activation
    // 5.3.16-otjld-implicit-activation-annotation-1
    public void test5316_implicitActivationAnnotation1() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"Team5316iaa1.java",
			    "\n" +
			    "import org.objectteams.ImplicitTeamActivation;\n" +
			    "public team class Team5316iaa1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(isActive()?\"+\":\"-\");\n" +
			    "        next();\n" +
			    "        System.out.print(isActive()?\"+\":\"-\");\n" +
			    "    }\n" +
			    "    @ImplicitTeamActivation\n" +
			    "    private void next() {\n" +
			    "        System.out.print(isActive()?\"+\":\"-\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team5316iaa1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "-+-",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ANNOTATED }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a role is annotated to use implicit activation - need to skip complex annotation first
    // 5.3.16-otjld-implicit-activation-annotation-2
    public void test5316_implicitActivationAnnotation2() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"Team5316iaa2.java",
			    "\n" +
			    "import org.objectteams.ImplicitTeamActivation;\n" +
			    "public team class Team5316iaa2 {\n" +
			    "    @A5316iaa2_2(value=\"huhu\",v3=@A5316iaa2_3({\"s1\", \"s2\"}))\n" +
			    "    @ImplicitTeamActivation\n" +
			    "    protected class R {\n" +
			    "        protected void next() {\n" +
			    "            System.out.print(isActive()?\"+\":\"-\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test() {\n" +
			    "        System.out.print(isActive()?\"+\":\"-\");\n" +
			    "        new R().next();\n" +
			    "        System.out.print(isActive()?\"+\":\"-\");\n" +
			    "\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team5316iaa2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"A5316iaa2_2.java",
			    "\n" +
			    "import java.lang.annotation.ElementType;\n" +
			    "import java.lang.annotation.Retention;\n" +
			    "import java.lang.annotation.RetentionPolicy;\n" +
			    "import java.lang.annotation.Target;\n" +
			    "@Retention(RetentionPolicy.RUNTIME)\n" +
			    "@Target({ElementType.METHOD, ElementType.TYPE})\n" +
			    "public @interface A5316iaa2_2 {\n" +
			    "        String value();\n" +
			    "        A5316iaa2_3 v3();\n" +
			    "}\n" +
			    "    \n",
		"A5316iaa2_3.java",
			    "\n" +
			    "import java.lang.annotation.ElementType;\n" +
			    "import java.lang.annotation.Retention;\n" +
			    "import java.lang.annotation.RetentionPolicy;\n" +
			    "import java.lang.annotation.Target;\n" +
			    "@Retention(RetentionPolicy.RUNTIME)\n" +
			    "@Target({ElementType.METHOD, ElementType.TYPE})\n" +
			    "public @interface A5316iaa2_3 {\n" +
			    "        String[] value();\n" +
			    "}\n" +
			    "    \n"
            },
            "-+-",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_ANNOTATED }/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // implicit activation is disabled
    // 5.3.16-otjld-implicit-activation-annotation-3
    public void test5316_implicitActivationAnnotation3() {
       Map customOptions = getCompilerOptions();
       runConformTest(
            new String[] {
		"Team5316iaa3.java",
			    "\n" +
			    "import org.objectteams.ImplicitTeamActivation;\n" +
			    "public team class Team5316iaa3 {\n" +
			    "    @ImplicitTeamActivation\n" +
			    "    protected class R {\n" +
			    "        protected void next() {\n" +
			    "            System.out.print(isActive()?\"+\":\"-\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test() {\n" +
			    "        System.out.print(isActive()?\"+\":\"-\");\n" +
			    "        new R().next();\n" +
			    "        System.out.print(isActive()?\"+\":\"-\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team5316iaa3().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "---",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            new String[] { IMPLICIT_ACTIVATION_NEVER}/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // concurrent implicit team activation causing AIOOBE
    // 5.3.17-otjld-concurrent-activation-1
    // see http://trac.objectteams.org/ot/ticket/331
    public void test5317_concurrentActivation1() {

       runConformTest(
            new String[] {
		"T5317ca1Main2.java",
			    "\n" +
			    "public class T5317ca1Main2 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new T5317ca1Main1().run();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I5317ca1.java",
			    "\n" +
			    "public interface I5317ca1 {\n" +
			    "    public String id();\n" +
			    "}\n" +
			    "    \n",
		"T5317ca1_1.java",
			    "\n" +
			    "public class T5317ca1_1 {\n" +
			    "}\n" +
			    "    \n",
		"T5317ca1_2.java",
			    "\n" +
			    "public class T5317ca1_2 extends T5317ca1_1 {\n" +
			    "    public String test2a(I5317ca1 other) {\n" +
			    "        return test2b(other);\n" +
			    "    }\n" +
			    "    public String test2b(I5317ca1 other) {\n" +
			    "        if (other.id().equals(\"never\"))\n" +
			    "            return \"wrong\";\n" +
			    "        return \"NotOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team5317ca1_1.java",
			    "\n" +
			    "@org.objectteams.ImplicitTeamActivation\n" +
			    "public team class Team5317ca1_1 {\n" +
			    "    protected class R1 implements I5317ca1 playedBy T5317ca1_1 {\n" +
			    "        protected String id() { return \"R1\"; }\n" +
			    "        protected String test1(R2 other) {\n" +
			    "            return other.test2(this);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected class R2 playedBy T5317ca1_2 {\n" +
			    "        String test2(I5317ca1 other) -> String test2a(I5317ca1 other);\n" +
			    "    }\n" +
//			    "	 public boolean isActive(Thread t) {\n" +
//			    "		try { Thread.sleep(100); } catch (InterruptedException ie) {}\n" +
//			    "		return super.isActive(t);\n" +
//			    "	 }\n" +
			    "    public String call(T5317ca1_1 as R1 o1, T5317ca1_2 as R2 o2) {\n" +
			    "        return o1.test1(o2);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team5317ca1_2.java",
			    "\n" +
			    "public team class Team5317ca1_2 {\n" +
			    "    protected class R2 playedBy T5317ca1_2 {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin String test3b(I5317ca1 other) {\n" +
			    "            return \"!\";\n" +
			    "        }\n" +
			    "        test3b <- replace test2b;\n" +
			    "        public String test3a() {\n" +
			    "            return \".\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public String test3(T5317ca1_2 as R2 o) {\n" +
			    "        return o.test3a();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T5317ca1Main1.java",
			    "\n" +
			    "public class T5317ca1Main1 {\n" +
			    "    T5317ca1_1 o1 = new T5317ca1_1();\n" +
			    "    T5317ca1_2 o2 = new T5317ca1_2();\n" +
			    "    class Run5317ca1_1 implements Runnable {\n" +
			    "        public void run() {\n" +
			    "            if( sleep( 1600 )) return;\n" +
			    "            final Team5317ca1_1 t1 = new Team5317ca1_1();\n" +
			    "            print(t1.call(o1, o2));\n" +
			    "        }\n" +
			    "    }\n" +
			    "    class Run5317ca1_2 implements Runnable {\n" +
			    "        public void run() {\n" +
			    "            if( sleep(200) ) return;\n" +
			    "            final Team5317ca1_2 t2 = new Team5317ca1_2();\n" +
			    "            t2.activate(org.objectteams.Team.ALL_THREADS);\n" +
			    "            for(int i = 5; i>0; i--) {\n" +
			    "                if( sleep(400) ) return;\n" +
			    "                print(t2.test3(o2));\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void run() {\n" +
			    "		 if ( Team5317ca1_2.class == null || Team5317ca1_1.class == null) return;\n" + // warm-up
			    "		 if ( sleep(1000) ) return;\n" +
			    "        Thread[] threads = new Thread[] {\n" +
			    "            new Thread(new Run5317ca1_1()),\n" +
			    "            new Thread(new Run5317ca1_1()),\n" +
			    "            new Thread(new Run5317ca1_2())\n" +
			    "        };\n" +
			    "        for(Thread i: threads) i.start();\n" +
			    "        for(Thread i: threads) join(i);\n" +
			    "    }\n" +
			    "    void join(Thread thread) {\n" +
			    "        try {\n" +
			    "            thread.join();\n" +
			    "        } catch (InterruptedException e) {\n" +
			    "            System.err.println(e);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    boolean sleep(long duration) {\n" +
			    "        try {\n" +
			    "            Thread.sleep(duration);\n" +
			    "            return false;\n" +
			    "        } catch (InterruptedException e) {\n" +
			    "            System.err.println(e);\n" +
			    "            return true;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void print(String s) {\n" +
			    "        System.out.print(s);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "...!!..");
    }
}
