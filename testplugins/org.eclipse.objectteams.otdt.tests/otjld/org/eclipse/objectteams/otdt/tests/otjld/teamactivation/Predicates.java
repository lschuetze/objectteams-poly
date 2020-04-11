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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.teamactivation;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

@SuppressWarnings("unchecked")
public class Predicates extends AbstractOTJLDTest {

	public Predicates(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test914_bindingPredicate21"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return Predicates.class;
	}

    // a role class has a predicate
    // 9.1.1-otjld-class-predicate-1
    public void test911_classPredicate1() {

       runConformTest(
            new String[] {
		"T911cp1Main.java",
			    "\n" +
			    "public class T911cp1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team911cp1 t = new Team911cp1();\n" +
			    "		T911cp1 o = new T911cp1();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T911cp1.java",
			    "\n" +
			    "public class T911cp1 {\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team911cp1.java",
			    "\n" +
			    "public team class Team911cp1 {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T911cp1 \n" +
			    "		when (thisTeamIsActive)\n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a role class has a base predicate
    // 9.1.1-otjld-class-predicate-2
    public void test911_classPredicate2() {

       runConformTest(
            new String[] {
		"T911cp2Main.java",
			    "\n" +
			    "public class T911cp2Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team911cp2 t = new Team911cp2();\n" +
			    "		T911cp2 o = new T911cp2();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T911cp2.java",
			    "\n" +
			    "public class T911cp2 {\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team911cp2.java",
			    "\n" +
			    "public team class Team911cp2 {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T911cp2 \n" +
			    "		base when (thisTeamIsActive)\n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a team class has a base predicate
    // 9.1.1-otjld-class-predicate-3
    public void test911_classPredicate3() {

       runConformTest(
            new String[] {
		"T911cp3Main.java",
			    "\n" +
			    "public class T911cp3Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team911cp3 t = new Team911cp3();\n" +
			    "		T911cp3 o = new T911cp3();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T911cp3.java",
			    "\n" +
			    "public class T911cp3 {\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team911cp3.java",
			    "\n" +
			    "public team class Team911cp3 \n" +
			    "	base when (thisTeamIsActive)\n" +
			    "{\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T911cp3 \n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a nested team has a base predicate - which affects its own callins and those of its roles
    // 9.1.1-otjld-class-predicate-4
    public void test911_classPredicate4() {

       runConformTest(
            new String[] {
		"T911cp4Main.java",
			    "\n" +
			    "public class T911cp4Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team911cp4 t = new Team911cp4();\n" +
			    "		t.activate();\n" +
			    "		T911cp4_1 b1 = new T911cp4_1();\n" +
			    "		T911cp4_2 b2 = new T911cp4_2();\n" +
			    "		b1.a();\n" +
			    "		b2.b();\n" +
			    "		t.isActivated = true;\n" +
			    "		b1.a();\n" +
			    "		b2.b();\n" +
			    "		t.isActivated = false;\n" +
			    "		b1.a();\n" +
			    "		b2.b();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T911cp4_1.java",
			    "\n" +
			    "public class T911cp4_1 {\n" +
			    "	public void a() {\n" +
			    "		System.out.print(\"a\");\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T911cp4_2.java",
			    "\n" +
			    "public class T911cp4_2 {\n" +
			    "	public void b() {\n" +
			    "		System.out.print(\"b\");\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team911cp4.java",
			    "\n" +
			    "public team class Team911cp4 {\n" +
			    "	public boolean isActivated = false;\n" +
			    "	public team class Nested playedBy T911cp4_1\n" +
			    "		base when (Team911cp4.this.isActivated) \n" +
			    "	{\n" +
			    "		Nested(T911cp4_1 b) {\n" +
			    "			this.activate();\n" +
			    "		}\n" +
			    "		void p() {\n" +
			    "			System.out.print(\"p\");\n" +
			    "		}\n" +
			    "		p <- after a;\n" +
			    "		protected class Inner playedBy T911cp4_2 {\n" +
			    "			void q() {\n" +
			    "				System.out.print(\"q\");\n" +
			    "			}\n" +
			    "			q <- after b;\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "abapbqab");
    }

    // two roles have base predicates - witness of TPX-428 part 2
    // 9.1.1-otjld-class-predicate-5
    public void test911_classPredicate5() {

       runConformTest(
            new String[] {
		"Team911cp5.java",
			    "\n" +
			    "public team class Team911cp5 {\n" +
			    "    protected class R1 playedBy T911cp5\n" +
			    "        base when (hasRole(base, R1.class)) \n" +
			    "    {\n" +
			    "        void n () { \n" +
			    "            System.out.print(\"N\");\n" +
			    "        }\n" +
			    "        n <- before test;\n" +
			    "    }\n" +
			    "    protected class R2 playedBy T911cp5\n" +
			    "        base when (hasRole(base, R2.class)) \n" +
			    "    {\n" +
			    "        void o () { \n" +
			    "            System.out.print(\"O\");\n" +
			    "        }\n" +
			    "        o <- before test;\n" +
			    "    }\n" +
			    "    precedence R1, R2;\n" +
			    "    Team911cp5(T911cp5 as R2 r) {\n" +
			    "        this.activate();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T911cp5 o = new T911cp5();\n" +
			    "        new Team911cp5(o);\n" +
			    "        o.test();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T911cp5.java",
			    "\n" +
			    "public class T911cp5 {\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a role within a nested team has a base predicate (reported by kmeier)
    // 9.1.1-otjld-class-predicate-6
    public void test911_classPredicate6() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.WARNING);
       runConformTest(
            new String[] {
		"Team911cp6.java",
			    "\n" +
			    "@SuppressWarnings(\"unused\")\n" +
			    "public team class Team911cp6 {\n" +
			    "    protected team class InnerTeam {\n" +
			    "        protected class R playedBy T911cp6 \n" +
			    "            base when (hasRole(base, R.class))\n" +
			    "        {\n" +
			    "            void k() {\n" +
			    "                System.out.print(\"K\");\n" +
			    "            }\n" +
			    "            k <- after test;\n" +
			    "        }\n" +
			    "        protected InnerTeam(T911cp6 as R o) {\n" +
			    "            activate();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team911cp6(T911cp6 b) {\n" +
			    "        InnerTeam it = new InnerTeam(b);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T911cp6 b = new T911cp6();\n" +
			    "        Team911cp6 t = new Team911cp6(b);\n" +
			    "        b.test();\n" +
			    "        new T911cp6().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T911cp6.java",
			    "\n" +
			    "public class T911cp6 {\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OKO",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a super role has a binding base guard, a sub role has a base guard at class level
    // 9.1.1-otjld-class-predicate-7
    public void test911_classPredicate7() {
        Map customOptions = getCompilerOptions();
        customOptions.put(CompilerOptions.OPTION_ReportHiddenLiftingProblem, CompilerOptions.IGNORE);

       runConformTest(
            new String[] {
		"Team911cp7.java",
			    "import org.objectteams.LiftingFailedException;\n" +
			    "@SuppressWarnings(\"abstractrelevantrole\")\n" +
			    "public team class Team911cp7 {\n" +
			    "    protected abstract class R1 playedBy T911cp7_1 {\n" +
			    "        abstract void rm1();\n" +
			    "        rm1 <- after test\n" +
			    "        base when (Team911cp7.this.hasRole(base, R1.class));\n" +
			    "    }\n" +
			    "    protected class R2 extends R1 playedBy T911cp7_2 \n" +
			    "        base when (Team911cp7.this.hasRole(base, R1.class))\n" +
			    "    {\n" +
			    "        void rm1() {\n" +
			    "            System.out.print(\"rm1\");\n" +
			    "        }\n" +
			    "        void rm2() {\n" +
			    "            System.out.print(\"rm2\");\n" +
			    "        }\n" +
			    "        rm2 <- before test;\n" +
			    "    }\n" +
			    "    void register(T911cp7_1 as R1 o) throws LiftingFailedException {}\n" +
			    "    public static void main(String[] args) throws LiftingFailedException {\n" +
			    "        Team911cp7 t=new Team911cp7();\n" +
			    "        t.activate();\n" +
			    "        T911cp7_2 b= new T911cp7_2();\n" +
			    "        b.test();\n" +
			    "        t.register(b);\n" +
			    "        b.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T911cp7_1.java",
			    "\n" +
			    "public abstract class T911cp7_1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"Base1\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T911cp7_2.java",
			    "\n" +
			    "public class T911cp7_2 extends T911cp7_1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"Base2\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Base2rm2Base2rm1",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a super role has a binding base guard, a sub role has a base guard at class level - non-adapted sub-role
    // 9.1.1-otjld-class-predicate-8
    public void test911_classPredicate8() {
        Map customOptions = getCompilerOptions();
        customOptions.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);

       runConformTest(
            new String[] {
		"Team911cp8.java",
			    "import org.objectteams.LiftingFailedException;\n" +
			    "@SuppressWarnings(\"abstractrelevantrole\")\n" +
			    "public team class Team911cp8 {\n" +
			    "    protected abstract class R1 playedBy T911cp8_1 {\n" +
			    "        abstract void rm1();\n" +
			    "        @SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "        rm1 <- after test\n" +
			    "        base when (Team911cp8.this.hasRole(base, R1.class));\n" +
			    "    }\n" +
			    "    protected class R2 extends R1 playedBy T911cp8_2 \n" +
			    "        base when (Team911cp8.this.hasRole(base, R1.class))\n" +
			    "    {\n" +
			    "        void rm1() {\n" +
			    "            System.out.print(\"rm1\");\n" +
			    "        }\n" +
			    "        void rm2() {\n" +
			    "            System.out.print(\"rm2\");\n" +
			    "        }\n" +
			    "        rm2 <- before test;\n" +
			    "    }\n" +
			    "    void register(T911cp8_1 as R1 o) throws LiftingFailedException {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team911cp8 t=new Team911cp8();\n" +
			    "        t.activate();\n" +
			    "        T911cp8_1 b= new T911cp8_3(); // doesn't have a suitable role\n" +
			    "        b.test();\n" +
			    "        try {\n" +
			    "            t.register(b);\n" +
			    "        } catch (org.objectteams.LiftingFailedException lfe) {\n" +
			    "            System.out.print(\".notLifted.\");\n" +
			    "        }\n" +
			    "        b.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T911cp8_1.java",
			    "\n" +
			    "public abstract class T911cp8_1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"Base1\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T911cp8_2.java",
			    "\n" +
			    "public class T911cp8_2 extends T911cp8_1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"Base2\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T911cp8_3.java",
			    "\n" +
			    "public class T911cp8_3 extends T911cp8_1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"Base3\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Base3.notLifted.Base3",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a role class has a base predicate using base in the expression
    // 9.1.2-otjld-base-expression-1
    public void test912_baseExpression1() {

       runConformTest(
            new String[] {
		"T912ba1Main.java",
			    "\n" +
			    "public class T912ba1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team912ba1 t = new Team912ba1();\n" +
			    "		T912ba1 o = new T912ba1();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		o.mode = 1;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T912ba1.java",
			    "\n" +
			    "public class T912ba1 {\n" +
			    "        public int mode = 0;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team912ba1.java",
			    "\n" +
			    "public team class Team912ba1 {\n" +
			    "	protected class Role1 playedBy T912ba1 \n" +
			    "		base when (base.mode == 1)\n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a team class and a role have predicates
    // 9.1.3-otjld-multiple-predicates-1
    public void test913_multiplePredicates1() {

       runConformTest(
            new String[] {
		"T913mp1Main.java",
			    "\n" +
			    "public class T913mp1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team913mp1 t = new Team913mp1();\n" +
			    "		T913mp1 o = new T913mp1();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created(1)\");\n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = false;\n" +
			    "		o.test();\n" +
			    "                o.activatable = false;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "		o.activatable = true;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T913mp1.java",
			    "\n" +
			    "public class T913mp1 {\n" +
			    "        public boolean activatable = false;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team913mp1.java",
			    "\n" +
			    "public team class Team913mp1 \n" +
			    "	when (thisTeamIsActive)\n" +
			    "{\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T913mp1\n" +
			    "            when (isActive())\n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test;\n" +
			    "                abstract boolean isActive();\n" +
			    "                isActive -> get activatable;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KKKOK");
    }

    // a team class and a role have base predicates
    // 9.1.3-otjld-multiple-predicates-2
    public void test913_multiplePredicates2() {

       runConformTest(
            new String[] {
		"T913mp2Main.java",
			    "\n" +
			    "public class T913mp2Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team913mp2 t = new Team913mp2();\n" +
			    "		T913mp2 o = new T913mp2();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(1)\");\n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = false;\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(2)\");\n" +
			    "                o.activatable = false;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(3)\");\n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T913mp2.java",
			    "\n" +
			    "public class T913mp2 {\n" +
			    "        public boolean activatable = false;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team913mp2.java",
			    "\n" +
			    "public team class Team913mp2 \n" +
			    "	base when (thisTeamIsActive)\n" +
			    "{\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T913mp2\n" +
			    "            base when (base.activatable)\n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KKKOK");
    }

    // a role class has a predicate, so does its super
    // 9.1.3-otjld-multiple-predicates-3
    public void test913_multiplePredicates3() {

       runConformTest(
            new String[] {
		"T913mp3Main.java",
			    "\n" +
			    "public class T913mp3Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team913mp3 t = new Team913mp3();\n" +
			    "		T913mp3 o = new T913mp3();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created(1)\");\n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = false;\n" +
			    "		o.test();\n" +
			    "                o.activatable = false;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "		o.activatable = true;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T913mp3.java",
			    "\n" +
			    "public class T913mp3 {\n" +
			    "        public boolean activatable = false;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team913mp3.java",
			    "\n" +
			    "public team class Team913mp3 {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "        abstract protected class Role0 \n" +
			    "		when (isActive())\n" +
			    "        {\n" +
			    "        	void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "                abstract boolean isActive();\n" +
			    "        }\n" +
			    "	protected class Role1 extends Role0 playedBy T913mp3 \n" +
			    "                when (thisTeamIsActive)\n" +
			    "	{\n" +
			    "		print <- before test;\n" +
			    "                isActive -> get activatable;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KKKOK");
    }

    // a role class has a base predicate, so does its super
    // 9.1.3-otjld-multiple-predicates-4
    public void test913_multiplePredicates4() {

       runConformTest(
            new String[] {
		"T913mp4Main.java",
			    "\n" +
			    "public class T913mp4Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team913mp4 t = new Team913mp4();\n" +
			    "		T913mp4 o = new T913mp4();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(1)\");\n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = false;\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(2)\");\n" +
			    "                o.activatable = false;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(3)\");\n" +
			    "		o.activatable = true;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T913mp4.java",
			    "\n" +
			    "public class T913mp4 {\n" +
			    "        public boolean activatable = false;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team913mp4.java",
			    "\n" +
			    "public team class Team913mp4 {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "        abstract protected class Role0 playedBy T913mp4 \n" +
			    "                base when (Team913mp4.this.thisTeamIsActive)\n" +
			    "        {\n" +
			    "        	void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "        }\n" +
			    "	protected class Role1 extends Role0 \n" +
			    "		base when (base.activatable)\n" +
			    "	{\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KKKOK");
    }

    // a role class has a predicate, its super has a base predicate
    // 9.1.3-otjld-multiple-predicates-5
    public void test913_multiplePredicates5() {

       runConformTest(
            new String[] {
		"T913mp5Main.java",
			    "\n" +
			    "public class T913mp5Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team913mp5 t = new Team913mp5();\n" +
			    "		T913mp5 o = new T913mp5();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(1)\");\n" +
			    "                o.activatable = false;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();                \n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(2)\");\n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = false;\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created\");\n" +
			    "		o.activatable = true;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T913mp5.java",
			    "\n" +
			    "public class T913mp5 {\n" +
			    "        public boolean activatable = false;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team913mp5.java",
			    "\n" +
			    "public team class Team913mp5 {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "        abstract protected class Role0 playedBy T913mp5 \n" +
			    "		base when (isActivatable(base))\n" +
			    "        {\n" +
			    "        	void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "        }\n" +
			    "	protected class Role1 extends Role0 \n" +
			    "                when (Team913mp5.this.thisTeamIsActive)\n" +
			    "	{\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "        boolean isActivatable(Object b) {\n" +
			    "            if (b instanceof T913mp5)\n" +
			    "                return ((T913mp5)b).activatable;\n" +
			    "            return false;\n" +
			    "        }\n" +
			    "}	\n" +
			    "	\n"
            },
            "KKKOK");
    }

    // a role class has a predicate, so does its tsuper
    // 9.1.3-otjld-multiple-predicates-6
    public void test913_multiplePredicates6() {

       runConformTest(
            new String[] {
		"T913mp6Main.java",
			    "\n" +
			    "public class T913mp6Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team913mp6_2 t = new Team913mp6_2();\n" +
			    "		T913mp6 o = new T913mp6();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created(1)\");\n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = false;\n" +
			    "		o.test();\n" +
			    "                o.activatable = false;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "		o.activatable = true;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T913mp6.java",
			    "\n" +
			    "public class T913mp6 {\n" +
			    "        public boolean activatable = false;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team913mp6_1.java",
			    "\n" +
			    "public team class Team913mp6_1 {\n" +
			    "        abstract protected class Role0 \n" +
			    "		when (isActive())\n" +
			    "        {\n" +
			    "        	void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "                abstract boolean isActive();\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team913mp6_2.java",
			    "\n" +
			    "public team class Team913mp6_2 extends Team913mp6_1 {        \n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role0 playedBy T913mp6 \n" +
			    "                when (thisTeamIsActive)\n" +
			    "	{\n" +
			    "		print <- before test;\n" +
			    "                isActive -> get activatable;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KKKOK");
    }

    // a role class has a predicate, its tsuper has a base predicate
    // 9.1.3-otjld-multiple-predicates-7
    public void test913_multiplePredicates7() {

       runConformTest(
            new String[] {
		"T913mp7Main.java",
			    "\n" +
			    "public class T913mp7Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team913mp7_2 t = new Team913mp7_2();\n" +
			    "		T913mp7 o = new T913mp7();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");                \n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = false;\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created(1)\");\n" +
			    "                o.activatable = false;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "		o.activatable = true;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T913mp7.java",
			    "\n" +
			    "public class T913mp7 {\n" +
			    "        public boolean activatable = false;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team913mp7_1.java",
			    "\n" +
			    "public team class Team913mp7_1 {\n" +
			    "        protected class Role0 playedBy T913mp7 \n" +
			    "		base when (base.activatable)\n" +
			    "        {\n" +
			    "        	void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team913mp7_2.java",
			    "\n" +
			    "public team class Team913mp7_2 extends Team913mp7_1 {        \n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role0 \n" +
			    "                when (thisTeamIsActive)\n" +
			    "	{\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KKKOK");
    }

    // a role class has a base predicate so has its tsuper
    // 9.1.3-otjld-multiple-predicates-8
    public void test913_multiplePredicates8() {

       runConformTest(
            new String[] {
		"T913mp8Main.java",
			    "\n" +
			    "public class T913mp8Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team913mp8_2 t = new Team913mp8_2();\n" +
			    "		T913mp8 o = new T913mp8();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created (1)\");                \n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = false;\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created (2)\");                \n" +
			    "                o.activatable = false;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created (3)\");                \n" +
			    "		o.activatable = true;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T913mp8.java",
			    "\n" +
			    "public class T913mp8 {\n" +
			    "        public boolean activatable = false;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team913mp8_1.java",
			    "\n" +
			    "public team class Team913mp8_1 {\n" +
			    "        protected class Role0 playedBy T913mp8 \n" +
			    "		base when (base.activatable)\n" +
			    "        {\n" +
			    "        	void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team913mp8_2.java",
			    "\n" +
			    "public team class Team913mp8_2 extends Team913mp8_1 {        \n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role0 \n" +
			    "                base when (Team913mp8_2.this.thisTeamIsActive)\n" +
			    "	{\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KKKOK");
    }

    // a role class has a base predicate, its tsuper has a regulare predicate
    // 9.1.3-otjld-multiple-predicates-9
    public void test913_multiplePredicates9() {

       runConformTest(
            new String[] {
		"T913mp9Main.java",
			    "\n" +
			    "public class T913mp9Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team913mp9_2 t = new Team913mp9_2();\n" +
			    "		T913mp9 o = new T913mp9();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(1)\");\n" +
			    "                o.activatable = true;\n" +
			    "		t.thisTeamIsActive = false;\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created(2)\");\n" +
			    "                o.activatable = false;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created(1)\");\n" +
			    "		o.activatable = true;\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T913mp9.java",
			    "\n" +
			    "public class T913mp9 {\n" +
			    "        public boolean activatable = false;\n" +
			    "	void test() { System.out.print(\"K\"); }\n" +
			    "}\n" +
			    "	\n",
		"Team913mp9_1.java",
			    "\n" +
			    "public team class Team913mp9_1 {\n" +
			    "        abstract protected class Role0 \n" +
			    "		when (isActive())\n" +
			    "        {\n" +
			    "        	void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "                abstract boolean isActive();\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team913mp9_2.java",
			    "\n" +
			    "public team class Team913mp9_2 extends Team913mp9_1 {        \n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role0 playedBy T913mp9 \n" +
			    "                base when (thisTeamIsActive)\n" +
			    "	{\n" +
			    "		print <- before test;\n" +
			    "                isActive -> get activatable;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KKKOK");
    }

    // a replace callin binding has a base predicate, the surrounding role class has an additional base predicate
    // 9.1.3-otjld-multiple-predicates-10
    public void test913_multiplePredicates10() {

       runConformTest(
            new String[] {
		"T913mp10Main.java",
			    "\n" +
			    "public class T913mp10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Account913mp10 acc1 = new Account913mp10();\n" +
			    "        Account913mp10 acc2 = new Account913mp10();\n" +
			    "        \n" +
			    "        Team913mp10 t = new Team913mp10();\n" +
			    "        t.activate();\n" +
			    "        \n" +
			    "        t.participate(acc1);\n" +
			    "        \n" +
			    "        acc1.credit(999);\n" +
			    "        acc2.credit(999);\n" +
			    "        System.out.println(acc1.getBalance());\n" +
			    "        System.out.println(acc2.getBalance());\n" +
			    "        \n" +
			    "        acc1.credit(2000);\n" +
			    "        acc2.credit(2000);\n" +
			    "        System.out.println(acc1.getBalance());\n" +
			    "        System.out.println(acc2.getBalance());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Account913mp10.java",
			    "\n" +
			    "public class Account913mp10 {\n" +
			    "    private int balance;\n" +
			    "    public int getBalance() {\n" +
			    "        return balance;\n" +
			    "    }\n" +
			    "    public void credit(int amount) {\n" +
			    "        balance += amount;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team913mp10.java",
			    "\n" +
			    "public team class Team913mp10 {\n" +
			    "    public void participate(Account913mp10 as BonusAccount ba) {}\n" +
			    "         \n" +
			    "    public class BonusAccount playedBy Account913mp10\n" +
			    "        base when(Team913mp10.this.hasRole(base, BonusAccount.class))\n" +
			    "    {     \n" +
			    "        callin void creditBonus(int amount) \n" +
			    "        {\n" +
			    "            base.creditBonus(amount+(amount/100));\n" +
			    "        }\n" +
			    "        void creditBonus(int amount) <- replace void credit(int i)\n" +
			    "            base when (i > 1000)\n" +
			    "        ; \n" +
			    "    }    \n" +
			    "}\n" +
			    "        \n"
            },
            "999\n" +
			"999\n" +
			"3019\n" +
			"2999");
    }

    // a role method and its binding both have predicates
    // 9.1.3-otjld-multiple-predicates-11
    public void test913_multiplePredicates11() {

       runConformTest(
            new String[] {
		"Team913mp11.java",
			    "\n" +
			    "public team class Team913mp11 {\n" +
			    "    protected class R playedBy T913mp11 {\n" +
			    "	boolean active1 = false;\n" +
			    "	boolean active2 = true;\n" +
			    "	void rm()\n" +
			    "	    when(this.active1)\n" +
			    "	{\n" +
			    "	    throw new RuntimeException(\"Shouldn't happen\");\n" +
			    "	}\n" +
			    "	void rm() <- after int doBase()\n" +
			    "	    when(this.active2);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "	new Team913mp11().activate();\n" +
			    "	System.out.print(new T913mp11().doBase());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T913mp11.java",
			    "\n" +
			    "public class T913mp11 {\n" +
			    "    int doBase() { return 1; }\n" +
			    "}\n" +
			    "    \n"
            },
            "1");
    }

    // a callin binding has a guard predicate
    // 9.1.4-otjld-binding-predicate-1
    public void test914_bindingPredicate1() {

       runConformTest(
            new String[] {
		"T914bp1Main.java",
			    "\n" +
			    "public class T914bp1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp1 t = new Team914bp1();\n" +
			    "		T914bp1 o = new T914bp1();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T914bp1.java",
			    "\n" +
			    "public class T914bp1 {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp1.java",
			    "\n" +
			    "public team class Team914bp1 {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T914bp1 \n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test\n" +
			    "		    when (thisTeamIsActive);\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a callin binding has a guard predicate and parameter mappings -- role and base have the same name
    // 9.1.4-otjld-binding-predicate-1a
    public void test914_bindingPredicate1a() {

       runConformTest(
            new String[] {
		"T914bp1aMain.java",
			    "\n" +
			    "import p1.T914bp1a;\n" +
			    "public class T914bp1aMain {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp1a t = new Team914bp1a();\n" +
			    "		T914bp1a o = new T914bp1a();\n" +
			    "		t.activate();\n" +
			    "		o.test(\"O\");\n" +
			    "        if (!t.hasRole(o))\n" +
			    "             throw new Error(\"role was not created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test(\"X\");\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"p1/T914bp1a.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T914bp1a {\n" +
			    "        public T914bp1a test(String s) {\n" +
			    "            System.out.print(s);\n" +
			    "            return this;\n" +
			    "        }\n" +
			    "        public void print() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp1a.java",
			    "\n" +
			    "import base p1.T914bp1a;\n" +
			    "public team class Team914bp1a {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class T914bp1a playedBy T914bp1a \n" +
			    "	{\n" +
			    "		void print(int l, char first, T914bp1a other) {\n" +
			    "			System.out.print(first);\n" +
			    "			System.out.print(l);\n" +
			    "			other.basePrint();\n" +
			    "		}\n" +
			    "		void print(int l, char first, T914bp1a other) <- after T914bp1a test(String s)\n" +
			    "		    when (thisTeamIsActive)\n" +
			    "		    with { l <- s.length(), first <- s.charAt(0), other <- result }\n" +
			    "		void basePrint() -> void print();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "OXX1K");
    }

    // a callin binding has a base guard predicate
    // 9.1.4-otjld-binding-predicate-2
    public void test914_bindingPredicate2() {

       runConformTest(
            new String[] {
		"T914bp2Main.java",
			    "\n" +
			    "public class T914bp2Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp2 t = new Team914bp2();\n" +
			    "		T914bp2 o = new T914bp2();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T914bp2.java",
			    "\n" +
			    "public class T914bp2 {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp2.java",
			    "\n" +
			    "public team class Team914bp2 {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T914bp2 \n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test\n" +
			    "		    base when (Team914bp2.this.thisTeamIsActive);\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a callin binding with multiple base methods has a base guard predicate - WITNESS for TPX-286
    // 9.1.4-otjld-binding-predicate-2a
    public void test914_bindingPredicate2a() {

       runConformTest(
            new String[] {
		"T914bp2aMain.java",
			    "\n" +
			    "public class T914bp2aMain {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp2a t = new Team914bp2a();\n" +
			    "		T914bp2a o = new T914bp2a();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T914bp2a.java",
			    "\n" +
			    "public class T914bp2a {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "	public void unUsed() {}\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp2a.java",
			    "\n" +
			    "public team class Team914bp2a {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T914bp2a \n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test, unUsed\n" +
			    "		    base when (Team914bp2a.this.thisTeamIsActive);\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a callin binding with multiple base methods has a base guard predicate - have signatures
    // 9.1.4-otjld-binding-predicate-2b
    public void test914_bindingPredicate2b() {

       runConformTest(
            new String[] {
		"T914bp2bMain.java",
			    "\n" +
			    "public class T914bp2bMain {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp2b t = new Team914bp2b();\n" +
			    "		T914bp2b o = new T914bp2b();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T914bp2b.java",
			    "\n" +
			    "public class T914bp2b {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "	public int unUsed() { return -1;}\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp2b.java",
			    "\n" +
			    "public team class Team914bp2b {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T914bp2b \n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		void print() <- before void test(), int unUsed()\n" +
			    "		    base when (Team914bp2b.this.thisTeamIsActive);\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a callin binding with multiple base methods has a regular guard predicate
    // 9.1.4-otjld-binding-predicate-2c
    public void test914_bindingPredicate2c() {

       runConformTest(
            new String[] {
		"T914bp2cMain.java",
			    "\n" +
			    "public class T914bp2cMain {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp2c t = new Team914bp2c();\n" +
			    "		T914bp2c o = new T914bp2c();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T914bp2c.java",
			    "\n" +
			    "public class T914bp2c {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "	public void unUsed() {}\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp2c.java",
			    "\n" +
			    "public team class Team914bp2c {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T914bp2c \n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test, unUsed\n" +
			    "		    when (Team914bp2c.this.thisTeamIsActive);\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a callin binding has a base guard predicate - static methods
    // 9.1.4-otjld-binding-predicate-2s
    public void test914_bindingPredicate2s() {

       runConformTest(
            new String[] {
		"T914bp2sMain.java",
			    "\n" +
			    "public class T914bp2sMain {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp2s t = new Team914bp2s();\n" +
			    "		t.activate();\n" +
			    "		T914bp2s.test();\n" +
			    "                if (t.getAllRoles().length > 0)\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		T914bp2s.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T914bp2s.java",
			    "\n" +
			    "public class T914bp2s {\n" +
			    "        public static void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp2s.java",
			    "\n" +
			    "public team class Team914bp2s {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T914bp2s \n" +
			    "	{\n" +
			    "		static void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test\n" +
			    "		    base when (Team914bp2s.this.thisTeamIsActive);\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a callin binding has a role guard predicate - static methods
    // 9.1.4-otjld-binding-predicate-2r
    public void test914_bindingPredicate2r() {

       runConformTest(
            new String[] {
		"T914bp2rMain.java",
			    "\n" +
			    "public class T914bp2rMain {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp2r t = new Team914bp2r();\n" +
			    "		t.activate();\n" +
			    "		T914bp2r.test();\n" +
			    "                if (t.getAllRoles().length > 0)\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		T914bp2r.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T914bp2r.java",
			    "\n" +
			    "public class T914bp2r {\n" +
			    "        public static void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp2r.java",
			    "\n" +
			    "public team class Team914bp2r {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T914bp2r \n" +
			    "	{\n" +
			    "		static void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test\n" +
			    "		    when (Team914bp2r.this.thisTeamIsActive);\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a callin binding has a role guard predicate - static methods with different signatures
    // 9.1.4-otjld-binding-predicate-2R
    public void test914_bindingPredicate2R() {

       runConformTest(
            new String[] {
		"T914bp2RMain.java",
			    "\n" +
			    "public class T914bp2RMain {\n" +
			    "        public static void main(String[] args) {\n" +
			    "                Team914bp2R t = new Team914bp2R();\n" +
			    "                t.activate();\n" +
			    "                T914bp2R.test(\"OK\", 1);\n" +
			    "                if (t.getAllRoles().length > 0)\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "                t.thisTeamIsActive = true;\n" +
			    "                T914bp2R.test(\"OK\", 1);\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"T914bp2R.java",
			    "\n" +
			    "public class T914bp2R {\n" +
			    "        public static void test(String s, int i) {\n" +
			    "            System.out.print(s.substring(i));\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n",
		"Team914bp2R.java",
			    "\n" +
			    "public team class Team914bp2R {\n" +
			    "        public boolean thisTeamIsActive = false;\n" +
			    "        protected class Role1 playedBy T914bp2R\n" +
			    "        {\n" +
			    "                @SuppressWarnings(\"basecall\")\n" +
			    "                static callin void print(String s) {\n" +
			    "                        System.out.print(s);\n" +
			    "                }\n" +
			    "                void print(String s) <- replace void test(String s, int i)\n" +
			    "                    when (Team914bp2R.this.thisTeamIsActive);\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n"
            },
            "KOK");
    }

    // a callin binding has a base guard predicate with parameter
    // 9.1.4-otjld-binding-predicate-3
    public void test914_bindingPredicate3() {

       runConformTest(
            new String[] {
		"T914bp3Main.java",
			    "\n" +
			    "public class T914bp3Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp3 t = new Team914bp3();\n" +
			    "		T914bp3 o = new T914bp3();\n" +
			    "		t.activate();\n" +
			    "		o.test(1);\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		o.test(0);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T914bp3.java",
			    "\n" +
			    "public class T914bp3 {\n" +
			    "        public void test(int value) {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp3.java",
			    "\n" +
			    "public team class Team914bp3 {\n" +
			    "	public int code = 0;\n" +
			    "	protected class Role1 playedBy T914bp3 \n" +
			    "	{\n" +
			    "		void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		void print() <- before void test(int value)\n" +
			    "		    base when (Team914bp3.this.code == value);\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a replace callin binding has a base guard predicate with parameter
    // 9.1.4-otjld-binding-predicate-4
    public void test914_bindingPredicate4() {

       runConformTest(
            new String[] {
		"T914bp4Main.java",
			    "\n" +
			    "public class T914bp4Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team914bp4 t = new Team914bp4();\n" +
			    "		T914bp4 o = new T914bp4();\n" +
			    "		t.activate();\n" +
			    "		o.test(1);\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		o.test(0);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T914bp4.java",
			    "\n" +
			    "public class T914bp4 {\n" +
			    "        public void test(int value) {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp4.java",
			    "\n" +
			    "public team class Team914bp4 {\n" +
			    "	public int code = 0;\n" +
			    "	protected class Role1 playedBy T914bp4 \n" +
			    "	{\n" +
			    "		callin void print() {\n" +
			    "			System.out.print(\"O\");\n" +
			    "			base.print();\n" +
			    "		}\n" +
			    "		void print() <- replace void test(int value)\n" +
			    "		    base when (Team914bp4.this.code == value);\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a before callin binding has a base guard predicate with parameter - no team-level attribute declared
    // 9.1.4-otjld-binding-predicate-5
    public void test914_bindingPredicate5() {

       runConformTest(
            new String[] {
		"T914bp5Main.java",
			    "\n" +
			    "public class T914bp5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp5 t = new Team914bp5();\n" +
			    "        T914bp5 o = new T914bp5();\n" +
			    "        t.activate();\n" +
			    "        System.out.println(o.sqrt(7));\n" +
			    "        System.out.println(o.sqrt(-7));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp5.java",
			    "\n" +
			    "public class T914bp5 {\n" +
			    "    public double sqrt(double x) {\n" +
			    "        return Math.sqrt(x);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp5.java",
			    "\n" +
			    "public team class Team914bp5 {\n" +
			    "    //public int code = 0; //<- with this it works!\n" +
			    "    protected class Role1 playedBy T914bp5 \n" +
			    "    {\n" +
			    "    void precondition(double value) {\n" +
			    "        System.out.println(\"Alert!\");\n" +
			    "    }\n" +
			    "    void precondition(double d) <- before double sqrt(double x)\n" +
			    "      base when (x < 0);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "2.6457513110645907\n" +
			"Alert!\n" +
			"NaN");
    }

    // a before callin binding has a regular guard predicate with parameter
    // 9.1.4-otjld-binding-predicate-6
    public void test914_bindingPredicate6() {

       runConformTest(
            new String[] {
		"T914bp6Main.java",
			    "\n" +
			    "public class T914bp6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp6 t = new Team914bp6();\n" +
			    "        T914bp6 o = new T914bp6();\n" +
			    "        t.activate();\n" +
			    "        System.out.println(o.sqrt(7));\n" +
			    "        System.out.println(o.sqrt(-7));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp6.java",
			    "\n" +
			    "public class T914bp6 {\n" +
			    "    public double sqrt(double x) {\n" +
			    "        return Math.sqrt(x);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp6.java",
			    "\n" +
			    "public team class Team914bp6 {\n" +
			    "    protected class Role1 playedBy T914bp6 \n" +
			    "    {\n" +
			    "    void precondition(double value) {\n" +
			    "        System.out.println(\"Alert!\");\n" +
			    "    }\n" +
			    "    void precondition(double d) <- before double sqrt(double x)\n" +
			    "      when (d < 0);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "2.6457513110645907\n" +
			"Alert!\n" +
			"NaN");
    }

    // a callin binding with base guard predicate is added in a sub team
    // 9.1.4-otjld-binding-predicate-7
    public void test914_bindingPredicate7() {

       runConformTest(
            new String[] {
		"T914bp7Main.java",
			    "\n" +
			    "public class T914bp7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp7_2 t = new Team914bp7_2();\n" +
			    "        T914bp7 o = new T914bp7();\n" +
			    "        t.activate();\n" +
			    "        System.out.println(o.sqrt(7));\n" +
			    "        System.out.println(o.sqrt(-7));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp7.java",
			    "\n" +
			    "public class T914bp7 {\n" +
			    "    public double sqrt(double x) {\n" +
			    "        return Math.sqrt(x);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp7_1.java",
			    "\n" +
			    "public team class Team914bp7_1 {\n" +
			    "    protected class Role1\n" +
			    "    {\n" +
			    "        void precondition() {\n" +
			    "            System.out.println(\"Alert!\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"Team914bp7_2.java",
			    "\n" +
			    "public team class Team914bp7_2 extends Team914bp7_1 {\n" +
			    "    protected class Role1 playedBy T914bp7 \n" +
			    "    {\n" +
			    "        void precondition() <- before double sqrt(double x)\n" +
			    "            base when (x < 0);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "2.6457513110645907\n" +
			"Alert!\n" +
			"NaN");
    }

    // a callin binding with base guard predicate is repeated in a sub team - named callin
    // 9.1.4-otjld-binding-predicate-7a
    public void test914_bindingPredicate7a() {

       runConformTest(
            new String[] {
		"T914bp7aMain.java",
			    "\n" +
			    "public class T914bp7aMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp7a_2 t = new Team914bp7a_2();\n" +
			    "        T914bp7a o = new T914bp7a();\n" +
			    "        t.activate();\n" +
			    "        System.out.println(o.sqrt(7));\n" +
			    "        System.out.println(o.sqrt(-7));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp7a.java",
			    "\n" +
			    "public class T914bp7a {\n" +
			    "    public double sqrt(double x) {\n" +
			    "        return Math.sqrt(x);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp7a_1.java",
			    "\n" +
			    "public team class Team914bp7a_1 {\n" +
			    "    protected class Role1 playedBy T914bp7a \n" +
			    "    {\n" +
			    "        void precondition() {\n" +
			    "            System.out.println(\"Alert!\");\n" +
			    "        }\n" +
			    "        sqrtPre:\n" +
			    "        void precondition() <- before double sqrt(double x)\n" +
			    "            base when (x < 0);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"Team914bp7a_2.java",
			    "\n" +
			    "public team class Team914bp7a_2 extends Team914bp7a_1 {\n" +
			    "    protected class Role1 \n" +
			    "    {\n" +
			    "        sqrtPre:\n" +
			    "        void precondition() <- before double sqrt(double x)\n" +
			    "            base when (x < 0);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "2.6457513110645907\n" +
			"Alert!\n" +
			"NaN");
    }

    // a callin binding with base guard predicate is repeated in a sub team - unnamed callin
    // 9.1.4-otjld-binding-predicate-7f
    public void test914_bindingPredicate7f() {
        runNegativeTestMatching(
            new String[] {
		"Team914bp7f_2.java",
			    "\n" +
			    "public team class Team914bp7f_2 extends Team914bp7f_1 {\n" +
			    "    protected class Role1 \n" +
			    "    {\n" +
			    "        void precondition() <- before double sqrt(double x)\n" +
			    "            base when (x < 0);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"T914bp7f.java",
			    "\n" +
			    "public class T914bp7f {\n" +
			    "    public double sqrt(double x) {\n" +
			    "        return Math.sqrt(x);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp7f_1.java",
			    "\n" +
			    "public team class Team914bp7f_1 {\n" +
			    "    protected class Role1 playedBy T914bp7f \n" +
			    "    {\n" +
			    "        void precondition() {\n" +
			    "            System.out.println(\"Alert!\");\n" +
			    "        }\n" +
			    "        void precondition() <- before double sqrt(double x)\n" +
			    "            base when (x < 0);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "OTJLD 4.8");
    }

    // a role method is bound after AND before the same base method, both bindings have base guard predicates
    // 9.1.4-otjld-binding-predicate-8
    public void test914_bindingPredicate8() {

       runConformTest(
            new String[] {
		"T914bp8Main.java",
			    "\n" +
			    "public class T914bp8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp8 t = new Team914bp8();\n" +
			    "        T914bp8 o = new T914bp8();\n" +
			    "        t.activate();\n" +
			    "        System.out.println(o.sqrt(7));\n" +
			    "        System.out.println(o.sqrt(-7));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp8.java",
			    "\n" +
			    "public class T914bp8 {\n" +
			    "    public double sqrt(double x) {\n" +
			    "        return Math.sqrt(x);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp8.java",
			    "\n" +
			    "public team class Team914bp8 {\n" +
			    "    public int dummy = 0; //<- workaround!\n" +
			    "    protected class Role1 playedBy T914bp8 \n" +
			    "    {\n" +
			    "    void precondition() {\n" +
			    "        System.out.println(\"Alert!\");\n" +
			    "    }\n" +
			    "    void precondition() <- before double sqrt(double x)\n" +
			    "      base when (x < 0);\n" +
			    "    \n" +
			    "    void precondition() <- after double sqrt(double x)\n" +
			    "      base when (x < 0);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "2.6457513110645907\n" +
			"Alert!\n" +
			"Alert!\n" +
			"NaN");
    }

    // a role method returning void is bound after a base method returning void using a base guard predicate
    // 9.1.4-otjld-binding-predicate-9
    public void test914_bindingPredicate9() {

       runConformTest(
            new String[] {
		"T914bp9Main.java",
			    "\n" +
			    "public class T914bp9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp9 t = new Team914bp9();\n" +
			    "        T914bp9 o = new T914bp9();\n" +
			    "        t.activate();\n" +
			    "        o.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp9.java",
			    "\n" +
			    "public class T914bp9 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp9.java",
			    "\n" +
			    "public team class Team914bp9 {\n" +
			    "    protected class Role1 playedBy T914bp9 \n" +
			    "    {\n" +
			    "        void post() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        void post() <- after void bm()\n" +
			    "          base when (true);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "OK");
    }

    // a role method is bound before a base method  with no arguments using a base guard predicate - the full signatures are used in the binding (vs.  "9.1.4-otjld-binding-predicate-2")
    // 9.1.4-otjld-binding-predicate-10
    public void test914_bindingPredicate10() {

       runConformTest(
            new String[] {
		"T914bp10Main.java",
			    "\n" +
			    "public class T914bp10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp10 t = new Team914bp10();\n" +
			    "        T914bp10 o = new T914bp10();\n" +
			    "        t.activate();\n" +
			    "        o.bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp10.java",
			    "\n" +
			    "public class T914bp10 {\n" +
			    "    public void bm() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp10.java",
			    "\n" +
			    "public team class Team914bp10 {\n" +
			    "    protected class Role1 playedBy T914bp10 \n" +
			    "    {\n" +
			    "     void pre() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "     }\n" +
			    "     void pre() <- before void bm()\n" +
			    "       base when (true);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "KO");
    }

    // a role method is bound before a base method with an argument omitting the signatures - in the base guard predicate the identifier 'base' is used to access a private field
    // 9.1.4-otjld-binding-predicate-11
    public void test914_bindingPredicate11() {
       Map customOptions = getCompilerOptions();

       runConformTest(
            new String[] {
		"T914bp11Main.java",
			    "\n" +
			    "public class T914bp11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp11 t = new Team914bp11();\n" +
			    "        T914bp11 o = new T914bp11();\n" +
			    "        t.activate();\n" +
			    "        o.bm(7);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp11.java",
			    "\n" +
			    "public class T914bp11 {\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    private boolean check = true;\n" +
			    "    \n" +
			    "    public void bm(int i) {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp11.java",
			    "\n" +
			    "public team class Team914bp11 {\n" +
			    "    protected class Role1 playedBy T914bp11 \n" +
			    "    {\n" +
			    "     void pre() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "     }\n" +
			    "     pre <- before bm\n" +
			    "       base when (base.check);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "KO",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // like "9.1.4-otjld-binding-predicate-11" with signatures in the binding
    // 9.1.4-otjld-binding-predicate-11a
    public void test914_bindingPredicate11a() {

       runConformTest(
            new String[] {
		"T914bp11aMain.java",
			    "\n" +
			    "public class T914bp11aMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp11a t = new Team914bp11a();\n" +
			    "        T914bp11a o = new T914bp11a();\n" +
			    "        t.activate();\n" +
			    "        o.bm(7);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp11a.java",
			    "\n" +
			    "public class T914bp11a {\n" +
			    "    public boolean check = true;\n" +
			    "    \n" +
			    "    public void bm(int i) {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp11a.java",
			    "\n" +
			    "public team class Team914bp11a {\n" +
			    "    protected class Role1 playedBy T914bp11a \n" +
			    "    {\n" +
			    "     void pre() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "     }\n" +
			    "     void pre() <- before void bm(int i)\n" +
			    "       base when (base.check);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "KO");
    }

    // like "9.1.4-otjld-binding-predicate-11" with signatures in the binding + plus a team predicate
    // 9.1.4-otjld-binding-predicate-12
    public void test914_bindingPredicate12() {

       runConformTest(
            new String[] {
		"T914bp12Main.java",
			    "\n" +
			    "public class T914bp12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp12 t = new Team914bp12();\n" +
			    "        T914bp12 o = new T914bp12();\n" +
			    "        t.activate();\n" +
			    "        o.bm(7);\n" +
			    "	o.check=true;\n" +
			    "        o.bm(7);\n" +
			    "	t.docheck=true;\n" +
			    "        o.bm(7);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp12.java",
			    "\n" +
			    "public class T914bp12 {\n" +
			    "    public boolean check = false;\n" +
			    "    \n" +
			    "    public void bm(int i) {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp12.java",
			    "\n" +
			    "public team class Team914bp12 \n" +
			    "    base when (docheck)\n" +
			    "{\n" +
			    "    public boolean docheck = false;\n" +
			    "    protected class Role1 playedBy T914bp12 \n" +
			    "    {\n" +
			    "     void pre() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "     }\n" +
			    "     void pre() <- before void bm(int i)\n" +
			    "       base when (base.check);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "OOKO");
    }

    // like "9.1.4-otjld-binding-predicate-12" but regular guards only
    // 9.1.4-otjld-binding-predicate-13
    public void test914_bindingPredicate13() {

       runConformTest(
            new String[] {
		"T914bp13Main.java",
			    "\n" +
			    "public class T914bp13Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team914bp13 t = new Team914bp13();\n" +
			    "        T914bp13 o = new T914bp13();\n" +
			    "        t.activate();\n" +
			    "        o.bm(7);\n" +
			    "	t.setCheck(o);\n" +
			    "        o.bm(7);\n" +
			    "	t.docheck=true;\n" +
			    "        o.bm(7);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T914bp13.java",
			    "\n" +
			    "public class T914bp13 {\n" +
			    "    public void bm(int i) {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team914bp13.java",
			    "\n" +
			    "public team class Team914bp13 \n" +
			    "    when (docheck)\n" +
			    "{\n" +
			    "    public boolean docheck = false;\n" +
			    "    protected class Role1 playedBy T914bp13 \n" +
			    "    {\n" +
			    "     boolean check = false;\n" +
			    "     void pre() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "     }\n" +
			    "     void pre() <- before void bm(int i)\n" +
			    "       when (check);\n" +
			    "     protected void setCheck(boolean val) {\n" +
			    "        check = val;\n" +
			    "     }\n" +
			    "    }\n" +
			    "    public void setCheck(T914bp13 as Role1 r) {\n" +
			    "    	r.setCheck(true);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "OOKO");
    }

    // binding a base method two times to the same role method (predecdence declaration) one is inhibited by a guard predicate (Reported by Henry Sudhof on 5.1.2006)
    // 9.1.4-otjld-binding-predicate-14
    public void test914_bindingPredicate14() {

       runConformTest(
            new String[] {
		"T914bp14Main.java",
			    "\n" +
			    "public class T914bp14Main {\n" +
			    "    public void foo(){\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "\n" +
			    "        T914bp14Main b = new T914bp14Main();\n" +
			    "        Team914bp14 t = new Team914bp14();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "        b.foo();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team914bp14.java",
			    "\n" +
			    "public team class Team914bp14 \n" +
			    "{\n" +
			    "    precedence RoleClass.binding1, RoleClass.binding2;\n" +
			    "    protected class RoleClass playedBy T914bp14Main\n" +
			    "    {\n" +
			    "        public void advice1(){\n" +
			    "            System.out.print(\"O\");\n" +
			    "        }\n" +
			    "\n" +
			    "        binding1: advice1 <- before foo\n" +
			    "            when (false);\n" +
			    "\n" +
			    "        binding2: advice1 <- before foo;\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "OK");
    }

    // binding a base method two times to the same role method (predecdence declaration) with replace
    // 9.1.4-otjld-binding-predicate-15
    public void test914_bindingPredicate15() {

       runConformTest(
            new String[] {
		"T914bp15Main.java",
			    "\n" +
			    "public class T914bp15Main {\n" +
			    "    public void foo(){\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "\n" +
			    "        T914bp15Main b = new T914bp15Main();\n" +
			    "        Team914bp15 t = new Team914bp15();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "        b.foo();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team914bp15.java",
			    "\n" +
			    "public team class Team914bp15 \n" +
			    "{\n" +
			    "    precedence RoleClass.binding1, RoleClass.binding2;\n" +
			    "    protected class RoleClass playedBy T914bp15Main\n" +
			    "    {\n" +
			    "        callin void advice1() {\n" +
			    "            System.out.print(\"O\");\n" +
			    "            base.advice1();\n" +
			    "        }\n" +
			    "\n" +
			    "        binding1: advice1 <- replace foo\n" +
			    "            when (false);\n" +
			    "\n" +
			    "        binding2: advice1 <- replace foo;\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "OK");
    }

    // a base guard applies base-class decapsulation for its parameters and in an expression
    // 9.1.4-otjld-binding-predicate-16
    public void test914_bindingPredicate16() {

       runConformTest(
            new String[] {
		"p1/T914bp16Main.java",
			    "\n" +
			    "package p1;\n" +
			    "import p2.Team914bp16;\n" +
			    "public class T914bp16Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team914bp16().activate();\n" +
			    "        T914bp16 b= new T914bp16();\n" +
			    "        b.test(new T914bp16());\n" +
			    "        b.test(new T914bp16());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T914bp16.java",
			    "\n" +
			    "package p1;\n" +
			    "class T914bp16 {\n" +
			    "    boolean yes=true;\n" +
			    "    T914bp16 test(T914bp16 other) {\n" +
			    "        yes= !yes;\n" +
			    "        return yes? this: other;\n" +
			    "    }\n" +
			    "    boolean getYes() {\n" +
			    "        return yes;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p2/Team914bp16.java",
			    "\n" +
			    "package p2;\n" +
			    "import base p1.T914bp16;\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team914bp16 {\n" +
			    "    protected class R playedBy T914bp16 {\n" +
			    "        void check() {\n" +
			    "            System.out.print(\"X\");\n" +
			    "        }\n" +
			    "        void check() <- after T914bp16 test(T914bp16 other) \n" +
			    "            base when (result.getYes() == other.getYes() && base.getYes());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "X");
    }

    // a base guard accesses method parameters but the callin binding fails to mention signatures
    // 9.1.4-otjld-binding-predicate-17
    public void test914_bindingPredicate17() {
        runNegativeTestMatching(
            new String[] {
		"p2/Team914bp17.java",
			    "\n" +
			    "package p2;\n" +
			    "import base p1.T914bp17;\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team914bp17 {\n" +
			    "    protected class R playedBy T914bp17 {\n" +
			    "        void check(Object other) {\n" +
			    "            System.out.print(\"X\");\n" +
			    "        }\n" +
			    "        check <- after test\n" +
			    "            when (other.getYes() && base.getYes());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T914bp17.java",
			    "\n" +
			    "package p1;\n" +
			    "class T914bp17 {\n" +
			    "    boolean yes=true;\n" +
			    "    T914bp17 test(T914bp17 other) {\n" +
			    "        yes= !yes;\n" +
			    "        return yes? this: other;\n" +
			    "    }\n" +
			    "    boolean getYes() {\n" +
			    "        return yes;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "5.4.1(a)");
    }

    // a base guard accesses the method result but the callin binding fails to mention signatures
    // 9.1.4-otjld-binding-predicate-18
    public void test914_bindingPredicate18() {
        runNegativeTestMatching(
            new String[] {
		"p2/Team914bp18.java",
			    "\n" +
			    "package p2;\n" +
			    "import base p1.T914bp18;\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team914bp18 {\n" +
			    "    protected class R playedBy T914bp18 {\n" +
			    "        void check() {\n" +
			    "            System.out.print(\"X\");\n" +
			    "        }\n" +
			    "        check <- after test\n" +
			    "            base when (result.getYes() == other.getYes() && base.getYes());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T914bp18.java",
			    "\n" +
			    "package p1;\n" +
			    "class T914bp18 {\n" +
			    "    boolean yes=true;\n" +
			    "    T914bp18 test(T914bp18 other) {\n" +
			    "        yes= !yes;\n" +
			    "        return yes? this: other;\n" +
			    "    }\n" +
			    "    boolean getYes() {\n" +
			    "        return yes;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "5.4.1(a)");
    }

    // a method guard accesses parameters but the callin binding fails to mention signatures (not a problem)
    // 9.1.4-otjld-binding-predicate-19
    public void test914_bindingPredicate19() {

       runConformTest(
            new String[] {
		"p2/Team914bp19.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.T914bp19;\n" +
			    "@SuppressWarnings(\"bindingconventions\")\n" +
			    "public team class Team914bp19 {\n" +
			    "    protected class R playedBy T914bp19 {\n" +
			    "        void check(T914bp19 other)\n" +
			    "            when (other.getYes())\n" +
			    "        {\n" +
			    "            System.out.print(\"X\");\n" +
			    "        }\n" +
			    "        check <- after test;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team914bp19().activate();\n" +
			    "        T914bp19 b= new T914bp19();\n" +
			    "        b.test(b);\n" +
			    "        b.test(b);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T914bp19.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T914bp19 {\n" +
			    "    boolean yes=true;\n" +
			    "    public T914bp19 test(T914bp19 other) {\n" +
			    "        yes= !yes;\n" +
			    "        return yes? this: other;\n" +
			    "    }\n" +
			    "    public boolean getYes() {\n" +
			    "        return yes;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "X");
    }

    // a method guard accesses a parameter which is mapped in a parameter mapping
    // 9.1.4-otjld-binding-predicate-20
    public void test914_bindingPredicate20() {

       runConformTest(
            new String[] {
		"p2/Team914bp20.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.T914bp20;\n" +
			    "@SuppressWarnings(\"bindingconventions\")\n" +
			    "public team class Team914bp20 {\n" +
			    "    protected class R playedBy T914bp20 {\n" +
			    "        void check(T914bp20 other)\n" +
			    "            when (other.getYes())\n" +
			    "        {\n" +
			    "            System.out.print(\"X\");\n" +
			    "        }\n" +
			    "        void check(T914bp20 other) <- after T914bp20 test(int foo, T914bp20 other)\n" +
			    "                with { other <- new T914bp20(other) }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team914bp20().activate();\n" +
			    "        T914bp20 b= new T914bp20();\n" +
			    "        b.test(1, b);\n" +
			    "        b.test(2, b);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T914bp20.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T914bp20 {\n" +
			    "    boolean yes=true;\n" +
			    "    public T914bp20() {}\n" +
			    "    public T914bp20(T914bp20 other) {\n" +
			    "        this.yes = other.yes;\n" +
			    "    }\n" +
			    "    public T914bp20 test(int foo, T914bp20 other) {\n" +
			    "        yes= !yes;\n" +
			    "        return yes? this: other;\n" +
			    "    }\n" +
			    "    public boolean getYes() {\n" +
			    "        return yes;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "X");
    }

    // unresolved method reference in a base guard, role already has a re-checkable problem
    public void test914_bindingPredicate21() {
    	runNegativeTest(
    		new String[]{
    	"Team914bp21.java",
    			"public team class Team914bp21 {\n" +
    			"	protected class R playedBy T914bp21 {\n" +
    			"		abstract void blub();\n" + // this produces a problem with a rechecker
    			"		blub -> blub;\n" +
    			"		void foo() {} \n" +
    			"		foo <- after bar \n" +
    			"			base when (test(base));\n" +
    			"	}\n" +
    			"}\n",
    	"T914bp21.java",
    			"public class T914bp21 {\n" +
    			"	void bar() {}\n" +
    			"	void blub() {}\n" +
    			"}\n"
    		},
    		"----------\n" +
    		"1. ERROR in Team914bp21.java (at line 7)\n" +
    		"	base when (test(base));\n" +
    		"	           ^^^^\n" +
    		"The method test(T914bp21) is undefined for the type Team914bp21.R\n" +
    		"----------\n");
    }

    // Bug 354244 - Role-side callin guard predicate interferes with Team.isExecutingCallin()
    public void test914_bindingPredicate22() {

        runConformTest(
             new String[] {
 		"T914bp22Main.java",
 			    "\n" +
 			    "public class T914bp22Main {\n" +
 			    "	public static void main(String[] args) {\n" +
 			    "		Team914bp22 t = new Team914bp22();\n" +
 			    "		T914bp22 o = new T914bp22();\n" +
 			    "		t.activate();\n" +
 			    "		o.test(1);\n" +
 			    "       if (t.isExecutingCallin())\n" +
 			    "           throw new Error(\"Flag not reset\");\n" +
 			    "	}\n" +
 			    "}\n" +
 			    "	\n",
 		"T914bp22.java",
 			    "\n" +
 			    "public class T914bp22 {\n" +
 			    "        public void test(int value) {\n" +
 			    "            System.out.print(\"OK\");\n" +
 			    "        }\n" +
 			    "}    \n" +
 			    "    \n",
 		"Team914bp22.java",
 			    "\n" +
 			    "public team class Team914bp22 {\n" +
 			    "	public int code = 0;\n" +
 			    "	protected class Role1 playedBy T914bp22 \n" +
 			    "	{\n" +
 			    "		void print(int value) {\n" +
 			    "			System.out.print(\"NOT\");\n" +
 			    "		}\n" +
 			    "		void print(int value) <- before void test(int value)\n" +
 			    "		    when (Team914bp22.this.code == value);\n" +
 			    "	}\n" +
 			    "}	\n" +
 			    "	\n"
             },
             "OK");
    }


    // a role method has a guard predicate
    // 9.1.5-otjld-method-predicate-1
    public void test915_methodPredicate1() {

       runConformTest(
            new String[] {
		"T915mp1Main.java",
			    "\n" +
			    "public class T915mp1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team915mp1 t = new Team915mp1();\n" +
			    "		T915mp1 o = new T915mp1();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (!t.hasRole(o))\n" +
			    "                    throw new Error(\"role was not created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T915mp1.java",
			    "\n" +
			    "public class T915mp1 {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "}    \n" +
			    "    \n",
		"Team915mp1.java",
			    "\n" +
			    "public team class Team915mp1 {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T915mp1 \n" +
			    "	{\n" +
			    "		void print() \n" +
			    "		    when (thisTeamIsActive)\n" +
			    "                {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a role method has a base guard predicate
    // 9.1.5-otjld-method-predicate-2
    public void test915_methodPredicate2() {

       runConformTest(
            new String[] {
		"T915mp2Main.java",
			    "\n" +
			    "public class T915mp2Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team915mp2 t = new Team915mp2();\n" +
			    "		T915mp2 o = new T915mp2();\n" +
			    "		t.activate();\n" +
			    "		o.test();\n" +
			    "                if (t.hasRole(o))\n" +
			    "                    throw new Error(\"role was created\");\n" +
			    "		t.thisTeamIsActive = true;\n" +
			    "		o.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T915mp2.java",
			    "\n" +
			    "public class T915mp2 {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "}    \n" +
			    "    \n",
		"Team915mp2.java",
			    "\n" +
			    "public team class Team915mp2 {\n" +
			    "	public boolean thisTeamIsActive = false;\n" +
			    "	protected class Role1 playedBy T915mp2 \n" +
			    "	{\n" +
			    "		void print() \n" +
			    "		    base when (thisTeamIsActive)\n" +
			    "                {\n" +
			    "			System.out.print(\"O\");\n" +
			    "		}\n" +
			    "		print <- before test;\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "KOK");
    }

    // a role method has a regular guard predicate with parameter
    // 9.1.5-otjld-method-predicate-3
    public void test915_methodPredicate3() {

       runConformTest(
            new String[] {
		"T915mp3Main.java",
			    "\n" +
			    "public class T915mp3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team915mp3 t = new Team915mp3();\n" +
			    "        T915mp3 o = new T915mp3();\n" +
			    "        t.activate();\n" +
			    "        System.out.println(o.sqrt(7));\n" +
			    "        System.out.println(o.sqrt(-7));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T915mp3.java",
			    "\n" +
			    "public class T915mp3 {\n" +
			    "    public double sqrt(double x) {\n" +
			    "        return Math.sqrt(x);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team915mp3.java",
			    "\n" +
			    "public team class Team915mp3 {\n" +
			    "    protected class Role1 playedBy T915mp3 \n" +
			    "    {\n" +
			    "    void precondition(double value)\n" +
			    "        when (value < 0)     \n" +
			    "    {\n" +
			    "        System.out.println(\"Alert!\");\n" +
			    "    }\n" +
			    "    void precondition(double d) <- before double sqrt(double x);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "2.6457513110645907\n" +
			"Alert!\n" +
			"NaN");
    }

    // a callin role method has a regular guard predicate with parameter
    // 9.1.5-otjld-method-predicate-4
    public void test915_methodPredicate4() {

       runConformTest(
            new String[] {
		"T915mp4Main.java",
			    "\n" +
			    "public class T915mp4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team915mp4 t = new Team915mp4();\n" +
			    "        T915mp4 o = new T915mp4();\n" +
			    "        t.activate();\n" +
			    "        System.out.println(o.sqrt(7));\n" +
			    "        System.out.println(o.sqrt(-7));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T915mp4.java",
			    "\n" +
			    "public class T915mp4 {\n" +
			    "    public double sqrt(double x) {\n" +
			    "        return Math.sqrt(x);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team915mp4.java",
			    "\n" +
			    "public team class Team915mp4 {\n" +
			    "    protected class Role1 playedBy T915mp4 \n" +
			    "    {\n" +
			    "    callin void precondition(double value)\n" +
			    "        when (value < 0)     \n" +
			    "    {\n" +
			    "        System.out.println(\"Alert!\");\n" +
			    "        base.precondition(Math.abs(value));\n" +
			    "    }\n" +
			    "    void precondition(double d) <- replace double sqrt(double x);\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n"
            },
            "2.6457513110645907\n" +
			"Alert!\n" +
			"2.6457513110645907");
    }

    // a callin role method has a regular guard predicate with parameter - also have a role predicate
    // 9.1.5-otjld-method-predicate-5
    public void test915_methodPredicate5() {

       runConformTest(
            new String[] {
		"T915mp5Main.java",
			    "\n" +
			    "public class T915mp5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team915mp5 t = new Team915mp5();\n" +
			    "        T915mp5 o = new T915mp5();\n" +
			    "        t.activate();\n" +
			    "        System.out.println(o.sqrt(7));\n" +
			    "        System.out.println(o.sqrt(-7));\n" +
			    "        t.docheck = false;\n" +
			    "        System.out.println(o.sqrt(-7));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T915mp5.java",
			    "\n" +
			    "public class T915mp5 {\n" +
			    "    public double sqrt(double x) {\n" +
			    "        return Math.sqrt(x);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team915mp5.java",
			    "\n" +
			    "public team class Team915mp5 {\n" +
			    "    protected class Role1 playedBy T915mp5 \n" +
			    "    	when (Team915mp5.this.docheck)\n" +
			    "    {\n" +
			    "	callin void precondition(double value)\n" +
			    "		when (value < 0)     \n" +
			    "	{\n" +
			    "		System.out.println(\"Alert!\");\n" +
			    "		base.precondition(Math.abs(value));\n" +
			    "	}\n" +
			    "	void precondition(double d) <- replace double sqrt(double x);\n" +
			    "    }\n" +
			    "    public boolean docheck = true;\n" +
			    "}   \n" +
			    "    \n"
            },
            "2.6457513110645907\n" +
			"Alert!\n" +
			"2.6457513110645907\n" +
			"NaN");
    }

    // a base predicate of an after binding accesses the result of the base method
    // 9.1.6-otjld-base-predicate-accessing-result-1
    public void test916_basePredicateAccessingResult1() {

       runConformTest(
            new String[] {
		"Team916bpar1.java",
			    "\n" +
			    "public team class Team916bpar1 {\n" +
			    "    public class R playedBy T916bpar1 {\n" +
			    "        void ok () { \n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "        void ok() <- after int check(int v) \n" +
			    "            base when (result == 14);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        (new Team916bpar1()).activate();\n" +
			    "        (new T916bpar1()).check(7);\n" +
			    "        (new T916bpar1()).check(8);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T916bpar1.java",
			    "\n" +
			    "public class T916bpar1 {\n" +
			    "        int check(int v) {\n" +
			    "            return v*2;\n" +
			    "        } \n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a base predicate of an after binding (short form) accesses the result of the base method - can't resolve "result"
    // 9.1.6-otjld-base-predicate-accessing-result-1s
    public void test916_basePredicateAccessingResult1s() {
        runNegativeTestMatching(
            new String[] {
		"Team916bpar1s.java",
			    "\n" +
			    "public team class Team916bpar1s {\n" +
			    "    public class R playedBy T916bpar1s {\n" +
			    "        void ok () { \n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "        ok <- after check\n" +
			    "            base when (result == 14);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        (new Team916bpar1s()).activate();\n" +
			    "        (new T916bpar1s()).check(7);\n" +
			    "        (new T916bpar1s()).check(8);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T916bpar1s.java",
			    "\n" +
			    "public class T916bpar1s {\n" +
			    "        int check(int v) {\n" +
			    "            return v*2;\n" +
			    "        } \n" +
			    "}    \n" +
			    "    \n"
            },
            "5.4.1(a)");
    }

    // a predicate is not the first element in a class
    // WITNESS for TPX-252 - fixed between 0.8.0 and 0.8.1
    // 9.1.6-otjld-predicate-confusing-the-parser-1
    public void test916_predicateConfusingTheParser1() {

       runConformTest(
            new String[] {
		"Team916pctp1.java",
			    "\n" +
			    "public team class Team916pctp1 {\n" +
			    "    public class R playedBy T916pctp1 {\n" +
			    "        void print() { System.out.print(\"OK\"); }\n" +
			    "        void test() when(true) { print(); }\n" +
			    "        test <- after foo;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        (new Team916pctp1()).activate();\n" +
			    "        (new T916pctp1()).foo();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T916pctp1.java",
			    "\n" +
			    "public class T916pctp1 {\n" +
			    "    void foo() {}\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a role of role has a base predicate (was bugreport ImplicitMayhem by hsudhof)
    // 9.1.7-otjld-predicate-in-role-of-role-1
    public void test917_predicateInRoleOfRole1() {

       runConformTest(
            new String[] {
		"p2/Team917piror1_2.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.Team917piror1_1;\n" +
			    "public team class Team917piror1_2 {\n" +
			    "    final Team917piror1_1 otherTeam = new Team917piror1_1();\n" +
			    "    protected class R2 playedBy R<@otherTeam>\n" +
			    "        base when (true)\n" +
			    "    {\n" +
			    "        void print() { \n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "        print <- after test;\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        otherTeam.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team917piror1_2 t2 = new Team917piror1_2();\n" +
			    "        t2.activate();\n" +
			    "        new Team917piror1_1().test(); // effectless, wrong team\n" +
			    "        t2.test();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/Team917piror1_1.java",
			    "\n" +
			    "package p1;    \n" +
			    "public team class Team917piror1_1 {\n" +
			    "    public class R {\n" +
			    "        protected void test() {\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role level base guard blocks lifting of a callin argument
    // 9.1.8-otjld_rejected_feature-base-guard-affecting-argument-lifting-1
    public void _rejected_test918_featureBaseGuardAffectingArgumentLifting1() {

       runConformTest(
            new String[] {
		"Team918bgaal1.java",
			    "\n" +
			    "public team class Team918bgaal1 {\n" +
			    "  boolean thisTeamIsActive = false;\n" +
			    "  protected class R1 playedBy T918bgaal1_1 \n" +
			    "    base when (thisTeamIsActive) \n" +
			    "  {}\n" +
			    "  protected class R2 playedBy T918bgaal1_2 {\n" +
			    "    @SuppressWarnings(\"basecall\")\n" +
			    "    callin void rm(R1 r) { System.out.print(\"rm\"); }\n" +
			    "    rm <- replace bm;\n" +
			    "  }\n" +
			    "  public static void main(String[] args) {\n" +
			    "    Team918bgaal1 t = new Team918bgaal1();\n" +
			    "    t.activate();\n" +
			    "    new T918bgaal1_2().bm(new T918bgaal1_1());\n" +
			    "    t.thisTeamIsActive = true;\n" +
			    "    System.out.print(\"-\");\n" +
			    "    new T918bgaal1_2().bm(new T918bgaal1_1());\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"T918bgaal1_1.java",
			    "\n" +
			    "public class T918bgaal1_1 {}\n" +
			    "  \n",
		"T918bgaal1_2.java",
			    "\n" +
			    "public class T918bgaal1_2 {\n" +
			    "  void bm(T918bgaal1_1 other) {\n" +
			    "    System.out.print(\"bm\");\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n"
            },
            "bm-rm");
    }

    // a binding guard throws an undeclared exception
    // 9.1.9-otjld-exception-in-predicate-1
    public void test919_exceptionInPredicate1() {

       runConformTest(
            new String[] {
		"Team919eip1.java",
			    "\n" +
			    "public team class Team919eip1 {\n" +
			    "    protected class R playedBy T919eip1 {\n" +
			    "        Object noObj = null;\n" +
			    "        void wrong() { System.out.print(\"wrong\"); }\n" +
			    "        wrong <- after test\n" +
			    "            when (noObj.equals(null));\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team919eip1().activate();\n" +
			    "        new T919eip1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T919eip1.java",
			    "\n" +
			    "public class T919eip1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a binding guard throws a declared exception
    // 9.1.9-otjld-exception-in-predicate-2
    public void test919_exceptionInPredicate2() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportExceptionInGuard, CompilerOptions.WARNING);

       runConformTest(
            new String[] {
		"Team919eip2.java",
			    "\n" +
			    "public team class Team919eip2 {\n" +
			    "    protected class R playedBy T919eip2 {\n" +
			    "        boolean test() throws Exception { throw new Exception(\"false\"); }\n" +
			    "        void wrong() { System.out.print(\"wrong\"); }\n" +
			    "        @SuppressWarnings(\"exceptioninguard\")\n" +
			    "        wrong <- after test\n" +
			    "            when (test());\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team919eip2().activate();\n" +
			    "        new T919eip2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T919eip2.java",
			    "\n" +
			    "public class T919eip2 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    public void testBugXYZ() {
    	runConformTest(
    		new String[] {
    			"bugxyz/Main.java",
    			"package bugxyz;\n" +
    			"\n" +
    			"public class Main {\n" +
    			"	public static void main(String[] args) {\n" +
    			"		new T1().activate();\n" +
    			"		new B1().foo();\n" +
    			"	}\n" +
    			"}\n",
    			"bugxzy/B1.java",
    			"package bugxyz;\n" +
    			"\n" +
    			"public class B1 {\n" +
    			"	boolean test() { return true; }\n" +
    			"	void foo() { }\n" +
    			"}\n",
    			"bugxzy/B2.java",
    			"package bugxyz;\n" +
    			"\n" +
    			"public class B2 extends B1 {}\n",
    			"bugxyz/T1.java",
    			"package bugxyz;\n" +
    			"\n" +
    			"public team class T1 {\n" +
    			"	protected class R1 playedBy B1 {\n" +
    			"		bar <- before foo\n" +
    			"			base when (base.test());\n" +
    			"\n" +
    			"		private void bar() {\n" +
    			"			System.out.println(\"bar\");\n" +
    			"		}\n" +
    			"	}\n" +
    			"	protected class R2 extends R1 playedBy B2 { }\n" +
    			"}\n"
    		},
    		"bar");
    }
}
