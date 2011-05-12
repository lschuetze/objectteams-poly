/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 Stephan Herrmann
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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.api;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

@SuppressWarnings("unchecked")
public class Reflection extends AbstractOTJLDTest {
	
	public Reflection(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test926_roleClassLiteralAccess"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return Reflection.class;
	}
	
    // hasRole is called, role exists
    // 9.2.1-otjld-has-role-method-1
    public void test921_hasRoleMethod1() {
       
       runConformTest(
            new String[] {
		"Team921hrm1.java",
			    "\n" +
			    "public team class Team921hrm1 {\n" +
			    "    protected class R playedBy T921hrm1 {}\n" +
			    "    Team921hrm1 (T921hrm1 as R o) {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T921hrm1 o = new T921hrm1();\n" +
			    "        Team921hrm1 t = new Team921hrm1(o);\n" +
			    "        if (t.hasRole(o))\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T921hrm1.java",
			    "\n" +
			    "public class T921hrm1 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole is called, role does not exist
    // 9.2.1-otjld-has-role-method-2
    public void test921_hasRoleMethod2() {
       
       runConformTest(
            new String[] {
		"Team921hrm2.java",
			    "\n" +
			    "public team class Team921hrm2 {\n" +
			    "    protected class R playedBy T921hrm2 {}\n" +
			    "    Team921hrm2 () {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T921hrm2 o = new T921hrm2();\n" +
			    "        Team921hrm2 t = new Team921hrm2();\n" +
			    "        if (t.hasRole(o))\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T921hrm2.java",
			    "\n" +
			    "public class T921hrm2 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole is called, no bound role
    // 9.2.1-otjld-has-role-method-3
    public void test921_hasRoleMethod3() {
       
       runConformTest(
            new String[] {
		"Team921hrm3.java",
			    "\n" +
			    "public team class Team921hrm3 {\n" +
			    "    protected class R {}\n" +
			    "    Team921hrm3 () {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Object o = new Object();\n" +
			    "        Team921hrm3 t = new Team921hrm3();\n" +
			    "        if (t.hasRole(o))\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole is called, multiple bound roles
    // 9.2.1-otjld-has-role-method-4
    public void test921_hasRoleMethod4() {
       
       runConformTest(
            new String[] {
		"Team921hrm4.java",
			    "\n" +
			    "public team class Team921hrm4 {\n" +
			    "    protected class R1 playedBy T921hrm4_1 {}\n" +
			    "    protected class R2 playedBy T921hrm4_2 {}\n" +
			    "    Team921hrm4 (T921hrm4_1 as R1 o) {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T921hrm4_1 o = new T921hrm4_1();\n" +
			    "        Team921hrm4 t = new Team921hrm4(o);\n" +
			    "        if (t.hasRole(o))\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T921hrm4_1.java",
			    "\n" +
			    "public class T921hrm4_1 {}\n" +
			    "    \n",
		"T921hrm4_2.java",
			    "\n" +
			    "public class T921hrm4_2 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole is called, multiple bound roles with common supertype
    // 9.2.1-otjld-has-role-method-5
    public void test921_hasRoleMethod5() {
       
       runConformTest(
            new String[] {
		"Team921hrm5.java",
			    "\n" +
			    "public team class Team921hrm5 {\n" +
			    "    protected class R {}\n" +
			    "    protected class R1 extends R playedBy T921hrm5_1 {}\n" +
			    "    protected class R2 extends R playedBy T921hrm5_2 {}\n" +
			    "    Team921hrm5 (T921hrm5_1 as R1 o) {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T921hrm5_1 o = new T921hrm5_1();\n" +
			    "        Team921hrm5 t = new Team921hrm5(o);\n" +
			    "        if (t.hasRole(o))\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T921hrm5_1.java",
			    "\n" +
			    "public class T921hrm5_1 {}\n" +
			    "    \n",
		"T921hrm5_2.java",
			    "\n" +
			    "public class T921hrm5_2 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole is called, multiple bound roles with common supertype, class selected
    // 9.2.1-otjld-has-role-method-5c
    public void test921_hasRoleMethod5c() {
       
       runConformTest(
            new String[] {
		"Team921hrm5c.java",
			    "\n" +
			    "public team class Team921hrm5c {\n" +
			    "    protected class R {}\n" +
			    "    protected class R1 extends R playedBy T921hrm5c_1 {}\n" +
			    "    protected class R2 extends R playedBy T921hrm5c_2 {}\n" +
			    "    Team921hrm5c (T921hrm5c_1 as R1 o) {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T921hrm5c_1 o = new T921hrm5c_1();\n" +
			    "        Team921hrm5c t = new Team921hrm5c(o);\n" +
			    "        t.test(o);\n" +
			    "    }\n" +
			    "    void test(Object o) {\n" +
			    "        if (hasRole(o, R1.class))\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "        if (hasRole(o, R2.class))\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T921hrm5c_1.java",
			    "\n" +
			    "public class T921hrm5c_1 {}\n" +
			    "    \n",
		"T921hrm5c_2.java",
			    "\n" +
			    "public class T921hrm5c_2 {}\n" +
			    "    \n"
            },
            "OKOK");
    }

    // hasRole is called from guard predicate, multiple bound roles with common supertype, class selected, see Trac #109
    // 9.2.1-otjld-has-role-method-5c2
    public void test921_hasRoleMethod5c2() {
        Map customOptions = getCompilerOptions();
        customOptions.put(CompilerOptions.OPTION_SuppressOptionalErrors, CompilerOptions.ENABLED);
       runConformTest(
            new String[] {
		"Team921hrm5c2.java",
			    "import org.objectteams.LiftingFailedException;\n" +
			    "@SuppressWarnings(\"ambiguousbinding\")\n" +
			    "public team class Team921hrm5c2 {\n" +
			    "    precedence R2_R, R1_R;\n" +
			    "    \n" +
			    "    protected abstract class R playedBy T921hrm5c2 {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin void foo() { System.out.print(getClass().getName()); }\n" +
			    "//        foo <- replace test; // lifting impossible\n" +
			    "    }\n" +
			    "    protected class R1_R extends R\n" +
			    "        base when (Team921hrm5c2.this.hasRole(base, R1_R.class))\n" +
			    "    {\n" +
			    "		 @SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "        foo <- replace test;\n" +
			    "    }\n" +
			    "    protected class R2_R extends R\n" +
			    "        base when (Team921hrm5c2.this.hasRole(base, R2_R.class))\n" +
			    "    {\n" +
			    "		 @SuppressWarnings(\"hidden-lifting-problem\")\n" +
			    "        foo <- replace test;\n" +
			    "    }\n" +
			    "    \n" +
			    "    Team921hrm5c2 (T921hrm5c2 as R1_R o) throws LiftingFailedException {}\n" +
			    "    public static void main(String[] args) throws LiftingFailedException {\n" +
			    "        T921hrm5c2 o = new T921hrm5c2();\n" +
			    "        Team921hrm5c2 t = new Team921hrm5c2(o);\n" +
			    "        t.activate();\n" +
			    "        o.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T921hrm5c2.java",
			    "\n" +
			    "public class T921hrm5c2 {\n" +
			    "    void test() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "Team921hrm5c2$__OT__R1_R",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // hasRole is called, multiple bound roles in one hierarchy
    // 9.2.1-otjld-has-role-method-6
    public void test921_hasRoleMethod6() {
       
       runConformTest(
            new String[] {
		"Team921hrm6.java",
			    "\n" +
			    "public team class Team921hrm6 {\n" +
			    "    protected class R playedBy T921hrm6_1 {}\n" +
			    "    protected class R1 extends R {}\n" +
			    "    protected class R2 extends R playedBy T921hrm6_2 {}\n" +
			    "    Team921hrm6 (T921hrm6_1 as R o1, T921hrm6_2 as R o2) {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T921hrm6_1 o1 = new T921hrm6_1();\n" +
			    "        T921hrm6_2 o2 = new T921hrm6_2();\n" +
			    "        Team921hrm6 t = new Team921hrm6(o1, o2);\n" +
			    "        if (t.hasRole(o1))\n" +
			    "            System.out.print(\"O\");\n" +
			    "        else\n" +
			    "            System.out.print(\"-\");\n" +
			    "        if (t.hasRole(o2))\n" +
			    "            System.out.print(\"K\");\n" +
			    "        else\n" +
			    "            System.out.print(\"-\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T921hrm6_1.java",
			    "\n" +
			    "public class T921hrm6_1 {}\n" +
			    "    \n",
		"T921hrm6_2.java",
			    "\n" +
			    "public class T921hrm6_2 extends T921hrm6_1 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole(Object,Class) called, role exists
    // 9.2.2-otjld-has-role-method-1
    public void test922_hasRoleMethod1() {
       
       runConformTest(
            new String[] {
		"Team922hrm1.java",
			    "\n" +
			    "public team class Team922hrm1 {\n" +
			    "    protected class R playedBy T922hrm1 {}\n" +
			    "    Team922hrm1 (T922hrm1 as R o) {}\n" +
			    "    void test(T922hrm1 o) {\n" +
			    "        if (hasRole(o, R.class))\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T922hrm1 o = new T922hrm1();\n" +
			    "        Team922hrm1 t = new Team922hrm1(o);\n" +
			    "        t.test(o);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T922hrm1.java",
			    "\n" +
			    "public class T922hrm1 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole(Object,Class) called, role exists, but illegal role type
    // 9.2.2-otjld-has-role-method-1f
    public void test922_hasRoleMethod1f() {
       
       runConformTest(
            new String[] {
		"Team922hrm1f.java",
			    "\n" +
			    "public team class Team922hrm1f {\n" +
			    "    protected class R playedBy T922hrm1f {}\n" +
			    "    Team922hrm1f (T922hrm1f as R o) {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T922hrm1f o = new T922hrm1f();\n" +
			    "        Team922hrm1f t = new Team922hrm1f(o);\n" +
			    "        try {\n" +
			    "            if (t.hasRole(o, T922hrm1f.class))\n" +
			    "                System.out.print(\"OK\");\n" +
			    "            else\n" +
			    "                System.out.print(\"NOK\");\n" +
			    "        }\n" +
			    "        catch (IllegalArgumentException ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T922hrm1f.java",
			    "\n" +
			    "public class T922hrm1f {}\n" +
			    "    \n"
            },
            "No such bound role type in this team: T922hrm1f");
    }

    // hasRole(Object,Class) called, role does not exist
    // 9.2.2-otjld-has-role-method-2
    public void test922_hasRoleMethod2() {
       
       runConformTest(
            new String[] {
		"Team922hrm2.java",
			    "\n" +
			    "public team class Team922hrm2 {\n" +
			    "    protected class R playedBy T922hrm2 {}\n" +
			    "    Team922hrm2 (Object o) {\n" +
			    "        if (hasRole(o, R.class))\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T922hrm2 o = new T922hrm2();\n" +
			    "        new Team922hrm2(o);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T922hrm2.java",
			    "\n" +
			    "public class T922hrm2 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole(Object,Class) called, role does not exist
    // 9.2.2-otjld-has-role-method-2f
    public void test922_hasRoleMethod2f() {
        runNegativeTestMatching(
            new String[] {
		"Team922hrm2f.java",
			    "\n" +
			    "public team class Team922hrm2f {\n" +
			    "    protected class R playedBy T922hrm2f {}\n" +
			    "    Team922hrm2f () {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T922hrm2f o = new T922hrm2f();\n" +
			    "        Team922hrm2f t = new Team922hrm2f();\n" +
			    "        if (t.hasRole(o, R.class))\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T922hrm2f.java",
			    "\n" +
			    "public class T922hrm2f {}\n" +
			    "    \n"
            },
            "6.1(c)");
    }

    // hasRole(Object,Class) called, no bound role
    // 9.2.2-otjld-has-role-method-3
    public void test922_hasRoleMethod3() {
       
       runConformTest(
            new String[] {
		"Team922hrm3.java",
			    "\n" +
			    "public team class Team922hrm3 {\n" +
			    "    protected class R {}\n" +
			    "    Team922hrm3 (Object o) {\n" +
			    "        try {\n" +
			    "            if (hasRole(o, R.class))\n" +
			    "                System.out.print(\"NOK\");\n" +
			    "            else\n" +
			    "                System.out.print(\"OK\");\n" +
			    "        } catch (IllegalArgumentException ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Object o = new Object();\n" +
			    "        Team922hrm3 t = new Team922hrm3(o);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "No such bound role type in this team: Team922hrm3$R");
    }

    // hasRole(Object,Class) called, multiple bound roles
    // 9.2.2-otjld-has-role-method-4
    public void test922_hasRoleMethod4() {
       
       runConformTest(
            new String[] {
		"Team922hrm4.java",
			    "\n" +
			    "public team class Team922hrm4 {\n" +
			    "    protected class R1 playedBy T922hrm4_1 {}\n" +
			    "    protected class R2 playedBy T922hrm4_2 {}\n" +
			    "    Team922hrm4 (T922hrm4_1 as R1 o) {}\n" +
			    "    void test(T922hrm4_1 o) {\n" +
			    "        if (hasRole(o, R1.class))\n" +
			    "            System.out.print(\"O\");\n" +
			    "        else\n" +
			    "            System.out.print(\"-\");\n" +
			    "        if (hasRole(o, R2.class))\n" +
			    "            System.out.print(\"-\");\n" +
			    "        else\n" +
			    "            System.out.print(\"K\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T922hrm4_1 o = new T922hrm4_1();\n" +
			    "        Team922hrm4 t = new Team922hrm4(o);\n" +
			    "        t.test(o);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T922hrm4_1.java",
			    "\n" +
			    "public class T922hrm4_1 {}\n" +
			    "    \n",
		"T922hrm4_2.java",
			    "\n" +
			    "public class T922hrm4_2 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole(Object,Class) called, multiple bound roles with common supertype
    // 9.2.2-otjld-has-role-method-5
    public void test922_hasRoleMethod5() {
       
       runConformTest(
            new String[] {
		"Team922hrm5.java",
			    "\n" +
			    "public team class Team922hrm5 {\n" +
			    "    protected class R {}\n" +
			    "    protected class R1 extends R playedBy T922hrm5_1 {}\n" +
			    "    protected class R2 extends R playedBy T922hrm5_2 {}\n" +
			    "    Team922hrm5 (T922hrm5_1 as R1 o) {}\n" +
			    "    void test(T922hrm5_1 o) {\n" +
			    "        if (hasRole(o, R1.class))\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T922hrm5_1 o = new T922hrm5_1();\n" +
			    "        Team922hrm5 t = new Team922hrm5(o);\n" +
			    "        t.test(o);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T922hrm5_1.java",
			    "\n" +
			    "public class T922hrm5_1 {}\n" +
			    "    \n",
		"T922hrm5_2.java",
			    "\n" +
			    "public class T922hrm5_2 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // hasRole(Object,Class) called, multiple bound roles in one hierarchy
    // 9.2.2-otjld-has-role-method-6
    public void test922_hasRoleMethod6() {
       
       runConformTest(
            new String[] {
		"Team922hrm6.java",
			    "\n" +
			    "public team class Team922hrm6 {\n" +
			    "    protected class R playedBy T922hrm6_1 {}\n" +
			    "    protected class R1 extends R {}\n" +
			    "    protected class R2 extends R playedBy T922hrm6_2 {}\n" +
			    "    Team922hrm6 (T922hrm6_1 as R o1, T922hrm6_2 as R o2) {}\n" +
			    "    void test(T922hrm6_1 o1, T922hrm6_2 o2) {\n" +
			    "        if (hasRole(o1, R.class))\n" +
			    "            System.out.print(\"O\");\n" +
			    "        else\n" +
			    "            System.out.print(\"-\");\n" +
			    "        if (hasRole(o2, R.class))\n" +
			    "            System.out.print(\"K\");\n" +
			    "        else\n" +
			    "            System.out.print(\"-\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T922hrm6_1 o1 = new T922hrm6_1();\n" +
			    "        T922hrm6_2 o2 = new T922hrm6_2();\n" +
			    "        Team922hrm6 t = new Team922hrm6(o1, o2);\n" +
			    "        t.test(o1, o2);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T922hrm6_1.java",
			    "\n" +
			    "public class T922hrm6_1 {}\n" +
			    "    \n",
		"T922hrm6_2.java",
			    "\n" +
			    "public class T922hrm6_2 extends T922hrm6_1 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // getRole(Object) called
    // 9.2.3-otjld-get-role-method-1
    public void test923_getRoleMethod1() {
       
       runConformTest(
            new String[] {
		"Team923grm1.java",
			    "\n" +
			    "public team class Team923grm1 {\n" +
			    "    protected class R playedBy T923grm1 {\n" +
			    "        public String toString() { return \"OK\"; }\n" +
			    "    }\n" +
			    "    Team923grm1(T923grm1 as R o) {}\n" +
			    "    public static void main (String[] args0) {\n" +
			    "        T923grm1 o = new T923grm1();\n" +
			    "        Team923grm1 t = new Team923grm1(o);\n" +
			    "        System.out.print(t.getRole(o));\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T923grm1.java",
			    "\n" +
			    "public class T923grm1 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // getRole(Object,Class) called
    // 9.2.3-otjld-get-role-method-2
    public void test923_getRoleMethod2() {
       
       runConformTest(
            new String[] {
		"Team923grm2_2.java",
			    "\n" +
			    "public team class Team923grm2_2 extends Team923grm2_1 {\n" +
			    "    Team923grm2_2(T923grm2 as R o) { super(o); }\n" +
			    "    void test(T923grm2 o) {\n" +
			    "        System.out.print(getRole(o, R.class).toString());\n" +
			    "    }\n" +
			    "    public static void main (String[] args0) {\n" +
			    "        T923grm2 o = new T923grm2();\n" +
			    "        Team923grm2_1 t = new Team923grm2_2(o);\n" +
			    "        t.test(o);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T923grm2.java",
			    "\n" +
			    "public class T923grm2 {}\n" +
			    "    \n",
		"Team923grm2_1.java",
			    "\n" +
			    "public team class Team923grm2_1 {\n" +
			    "    protected class R playedBy T923grm2 {\n" +
			    "        public String toString() { return \"OK\"; }\n" +
			    "    }\n" +
			    "    Team923grm2_1(R o) {}\n" +
			    "    void test(T923grm2 o) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // getRole(Object) called - compile in one go
    // 9.2.3-otjld-get-role-method-2a
    public void test923_getRoleMethod2a() {
       
       runConformTest(
            new String[] {
		"Team923grm2a_2.java",
			    "\n" +
			    "public team class Team923grm2a_2 extends Team923grm2a_1 {\n" +
			    "    Team923grm2a_2(T923grm2a as R o) { super(o);}\n" +
			    "    public static void main (String[] args0) {\n" +
			    "        T923grm2a o = new T923grm2a();\n" +
			    "        Team923grm2a_1 t = new Team923grm2a_2(o);\n" +
			    "        System.out.print(t.getRole(o));\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team923grm2a_1.java",
			    "\n" +
			    "public team class Team923grm2a_1 {\n" +
			    "    protected class R playedBy T923grm2a {\n" +
			    "        public String toString() { return \"OK\"; }\n" +
			    "    }\n" +
			    "    Team923grm2a_1(R o) {}\n" +
			    "}\n" +
			    "    \n",
		"T923grm2a.java",
			    "\n" +
			    "public class T923grm2a {}\n" +
			    "    \n"
            },
            "OK");
    }

    // getRole(Object,Class) called - old role class literal syntax
    // 9.2.3-otjld-get-role-method-2f
    public void test923_getRoleMethod2f() {
       
       runConformTest(
            new String[] {
		"T923grm2fMain.java",
			    "\n" +
			    "public class T923grm2fMain {\n" +
			    "    @SuppressWarnings(\"roletypesyntax\")\n" +
			    "    public static void main (String[] args0) {\n" +
			    "        T923grm2f o = new T923grm2f();\n" +
			    "        final Team923grm2f_1 t = new Team923grm2f_2(o);\n" +
			    "        R<@t> r = t.getRole(o, t.R.class);\n" +
			    "        r.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T923grm2f.java",
			    "\n" +
			    "public class T923grm2f {}\n" +
			    "    \n",
		"Team923grm2f_1.java",
			    "\n" +
			    "public team class Team923grm2f_1 {\n" +
			    "    public class R playedBy T923grm2f {\n" +
			    "        public void print() { System.out.print(\"OK\"); }\n" +
			    "    }\n" +
			    "    Team923grm2f_1(R o) {}\n" +
			    "}\n" +
			    "    \n",
		"Team923grm2f_2.java",
			    "\n" +
			    "public team class Team923grm2f_2 extends Team923grm2f_1 {\n" +
			    "    Team923grm2f_2(T923grm2f as R o) { super(o);}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // getRole(Object,Class) called - generic singature leveraged
    // 9.2.3-otjld-get-role-method-2g
    public void test923_getRoleMethod2g() {
       
       runConformTest(
            new String[] {
		"T923grm2gMain.java",
			    "\n" +
			    "public class T923grm2gMain {\n" +
			    "    public static void main (String[] args0) {\n" +
			    "        T923grm2g o = new T923grm2g();\n" +
			    "        final Team923grm2g_1 t = new Team923grm2g_2(o); \n" +
			    "        R<@t> r = t.getRole(o, R<@t>.class);\n" +
			    "        r.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T923grm2g.java",
			    "\n" +
			    "public class T923grm2g {}\n" +
			    "    \n",
		"Team923grm2g_1.java",
			    "\n" +
			    "public team class Team923grm2g_1 {\n" +
			    "    public class R playedBy T923grm2g {\n" +
			    "        public void print() { System.out.print(\"OK\"); }\n" +
			    "    }\n" +
			    "    Team923grm2g_1(R o) {}\n" +
			    "}\n" +
			    "    \n",
		"Team923grm2g_2.java",
			    "\n" +
			    "public team class Team923grm2g_2 extends Team923grm2g_1 {\n" +
			    "    Team923grm2g_2(T923grm2g as R o) { super(o);}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // getRole(Object,Class) called - generic singature used, but illegal use of protected role
    // 9.2.3-otjld-get-role-method-2gp
    public void test923_getRoleMethod2gp() {
        runNegativeTest(
            new String[] {
		"Team923grm2gp_2.java",
			    "\n" +
			    "public team class Team923grm2gp_2 extends Team923grm2gp_1 {\n" +
			    "    Team923grm2gp_2(T923grm2gp as R o) { super(o);}\n" +
			    "    public static void main (String[] args0) {\n" +
			    "        T923grm2gp o = new T923grm2gp();\n" +
			    "        final Team923grm2gp_1 t = new Team923grm2gp_2(o);\n" +
			    "        R<@t> r = t.getRole(o, R<@t>.class);\n" +
			    "        r.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T923grm2gp.java",
			    "\n" +
			    "public class T923grm2gp {}\n" +
			    "    \n",
		"Team923grm2gp_1.java",
			    "\n" +
			    "public team class Team923grm2gp_1 {\n" +
			    "    protected class R playedBy T923grm2gp {\n" +
			    "        public void print() { System.out.print(\"OK\"); }\n" +
			    "    }\n" +
			    "    Team923grm2gp_1(R o) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team923grm2gp_2.java (at line 7)\n" + 
    		"	R<@t> r = t.getRole(o, R<@t>.class);\n" + 
    		"	^\n" + 
    		"Illegal parameterized use of non-public role R (OTJLD 1.2.3(b)).\n" + 
    		"----------\n" + 
    		"2. ERROR in Team923grm2gp_2.java (at line 7)\n" + 
    		"	R<@t> r = t.getRole(o, R<@t>.class);\n" + 
    		"	                       ^\n" + 
    		"Illegal parameterized use of non-public role R (OTJLD 1.2.3(b)).\n" + 
    		"----------\n");
    }

    // getRole(Object,Class) called - duplicate roles exist
    // 9.2.3-otjld-get-role-method-3
    public void test923_getRoleMethod3() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportPotentialNullReference, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportRedundantNullCheck, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.WARNING);
       
       runConformTest(
            new String[] {
		"Team923grm3_2.java",
			    "\n" +
			    "public team class Team923grm3_2 extends Team923grm3_1 {\n" +
			    "    protected class R2 playedBy T923grm3 {\n" +
			    "        public String toString() { return \"NOK2\"; }\n" +
			    "    }\n" +
			    "    Team923grm3_2(T923grm3 as R o1, T923grm3 as R2 o2) { \n" +
			    "        super(o1);\n" +
			    "    }\n" +
			    "    public static void main (String[] args0) {\n" +
			    "        T923grm3 o = new T923grm3();\n" +
			    "        Team923grm3_1 t = new Team923grm3_2(o, o);\n" +
			    "        try {\n" +
			    "            System.out.print(t.getRole(o));\n" +
			    "        } catch (org.objectteams.DuplicateRoleException ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T923grm3.java",
			    "\n" +
			    "public class T923grm3 {}\n" +
			    "    \n",
		"Team923grm3_1.java",
			    "\n" +
			    "public team class Team923grm3_1 {\n" +
			    "    protected class R playedBy T923grm3 {\n" +
			    "        public String toString() { return \"NOK\"; }\n" +
			    "    }\n" +
			    "    Team923grm3_1(R o) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "Ambiguous role instances: found a role in hierarchies R2 and R",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // getRole(Object,Class) called - duplicate roles exist, suppress null warnings (while getRole has a suppressed null warning itself)
    // 9.2.3-otjld-get-role-method-3n
    public void test923_getRoleMethod3n() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportPotentialNullReference, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportRedundantNullCheck, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.WARNING);
       
       runConformTest(
            new String[] {
		"Team923grm3n_2.java",
			    "\n" +
			    "@SuppressWarnings(\"null\")\n" +
			    "public team class Team923grm3n_2 extends Team923grm3n_1 {\n" +
			    "    void nullCheck(int i, int j) {\n" +
			    "        Object o = null;\n" +
			    "        if (i == 3) o = new Object();\n" +
			    "        if (j == 3) o.toString();\n" +
			    "    }\n" +
			    "    protected class R2 playedBy T923grm3n {\n" +
			    "        public String toString() { return \"NOK2\"; }\n" +
			    "    }\n" +
			    "    Team923grm3n_2(T923grm3n as R o1, T923grm3n as R2 o2) {\n" +
			    "        super(o1);\n" +
			    "    }\n" +
			    "    public static void main (String[] args0) {\n" +
			    "        T923grm3n o = new T923grm3n();\n" +
			    "        Team923grm3n_1 t = new Team923grm3n_2(o, o);\n" +
			    "        try {\n" +
			    "            System.out.print(t.getRole(o));\n" +
			    "        } catch (org.objectteams.DuplicateRoleException ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T923grm3n.java",
			    "\n" +
			    "public class T923grm3n {}\n" +
			    "    \n",
		"Team923grm3n_1.java",
			    "\n" +
			    "public team class Team923grm3n_1 {\n" +
			    "    protected class R playedBy T923grm3n {\n" +
			    "        public String toString() { return \"NOK\"; }\n" +
			    "    }\n" +
			    "    Team923grm3n_1(R o) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "Ambiguous role instances: found a role in hierarchies R2 and R",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a replace callin tests the reflective function isExecutingCallin
    // 9.2.4-otjld-is-executing-callin-called-1
    public void test924_isExecutingCallinCalled1() {
       
       runConformTest(
            new String[] {
		"Team924iscc1.java",
			    "\n" +
			    "public team class Team924iscc1 {\n" +
			    "    protected class R playedBy T924iscc1 {\n" +
			    "        callin void hook() {\n" +
			    "            base.hook();\n" +
			    "            System.out.print(\n" +
			    "                Team924iscc1.this.isExecutingCallin()?\n" +
			    "                    \"OK\" : \"NOK\");\n" +
			    "        }\n" +
			    "        hook <- replace nop;\n" +
			    "    }\n" +
			    "    Team924iscc1() { activate(); }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team924iscc1();\n" +
			    "        T924iscc1 b = new T924iscc1();\n" +
			    "        b.nop();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T924iscc1.java",
			    "\n" +
			    "public class T924iscc1 {\n" +
			    "    public void nop() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }


    // a replace callin with result tests the reflective function isExecutingCallin
    // 9.2.4-otjld-is-executing-callin-called-2
    public void test924_isExecutingCallinCalled2() {
       
       runConformTest(
            new String[] {
		"Team924iscc2.java",
			    "\n" +
			    "public team class Team924iscc2 {\n" +
			    "    protected class R playedBy T924iscc2 {\n" +
			    "        callin String hook() {\n" +
			    "            return base.hook()+\"K\";\n" +
			    "        }\n" +
			    "        hook <- replace nip;\n" +
			    "    }\n" +
			    "    Team924iscc2() { activate(); }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T924iscc2 b = new T924iscc2();\n" +
			    "        System.out.print(b.nip());\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T924iscc2.java",
			    "\n" +
			    "public class T924iscc2 {\n" +
			    "    private Team924iscc2 t = new Team924iscc2();\n" +
			    "    public String nip() { return \n" +
			    "        t.isExecutingCallin()?\n" +
			    "           \"O\" : \"-\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an after callin tests the reflective function isExecutingCallin
    // 9.2.4-otjld-is-executing-callin-called-3
    public void test924_isExecutingCallinCalled3() {
       
       runConformTest(
            new String[] {
		"Team924iscc3.java",
			    "\n" +
			    "public team class Team924iscc3 {\n" +
			    "    protected class R playedBy T924iscc3 {\n" +
			    "        void hook() {\n" +
			    "            System.out.print(\n" +
			    "                Team924iscc3.this.isExecutingCallin()?\n" +
			    "                    \"OK\" : \"NOK\");\n" +
			    "        }\n" +
			    "        hook <- after nop;\n" +
			    "    }\n" +
			    "    Team924iscc3() { activate(); }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team924iscc3();\n" +
			    "        T924iscc3 b = new T924iscc3();\n" +
			    "        b.nop();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T924iscc3.java",
			    "\n" +
			    "public class T924iscc3 {\n" +
			    "    public void nop() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callin method throws an exception, yet the isExecuting flag is reset
    // 9.2.4-otjld-is-executing-callin-called-4
    public void test924_isExecutingCallinCalled4() {
       
       runConformTest(
            new String[] {
		"Team924iecc4.java",
			    "\n" +
			    "public team class Team924iecc4 {\n" +
			    "	protected class R playedBy T924iecc4 {\n" +
			    "		callin void nothing () { \n" +
			    "			if (Boolean.TRUE != new Object())\n" +
			    "				throw new RuntimeException(\"Don't\");\n" +
			    "			else\n" +
			    "				base.nothing();\n" +
			    "		}\n" +
			    "		nothing <- replace nop;\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team924iecc4 t = new Team924iecc4();\n" +
			    "		t.activate();\n" +
			    "		T924iecc4 b = new T924iecc4();\n" +
			    "		try {\n" +
			    "			b.nop();\n" +
			    "		} catch (RuntimeException e) {\n" +
			    "			System.out.print(\"Ex:\");\n" +
			    "		}\n" +
			    "		System.out.print(t.isExecutingCallin()?\"NOK\":\"OK\");\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T924iecc4.java",
			    "\n" +
			    "public class T924iecc4 { \n" +
			    "	public void nop() {}\n" +
			    "}	\n" +
			    "	\n"
            },
            "Ex:OK");
    }

    // a callin to equals() prevents recursion by a guard using isExecutingCallin
    // 9.2.4-otjld-is-executing-callin-called-5
    public void test924_isExecutingCallinCalled5() {
       
       runConformTest(
            new String[] {
		"Team924iecc5.java",
			    "\n" +
			    "public team class Team924iecc5 {\n" +
			    "    protected class R playedBy T924iecc5 {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin boolean always(R other) {\n" +
			    "            //System.out.print(base.always(other));\n" +
			    "            return true;\n" +
			    "        }\n" +
			    "        boolean always(R other) <- replace boolean equals(Object other)\n" +
			    "            base when (!isExecutingCallin() && (other instanceof T924iecc5))\n" +
			    "            with { other <- (T924iecc5)other }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        org.objectteams.Team t = new Team924iecc5();\n" +
			    "        t.activate();\n" +
			    "        Object o1 = new T924iecc5();\n" +
			    "        Object o2 = new T924iecc5();\n" +
			    "        System.out.print(o1.equals(o2));\n" +
			    "        System.out.print(t.getAllRoles().length);\n" +
			    "        // need to fill hash map to provoke conflict -> needs to call equals()\n" +
			    "        Object[] o = new Object[20000];\n" +
			    "        for (int i=0; i<10000; i++) {\n" +
			    "            o[i*2] = new T924iecc5();\n" +
			    "            o[i*2+1] = new T924iecc5();\n" +
			    "            if (!o[i*2].equals(o[i*2+1]))\n" +
			    "                throw new RuntimeException(\"not equal: \"+i);\n" +
			    "        }\n" +
			    "        System.gc();\n" +
			    "        System.out.print(t.getAllRoles().length);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T924iecc5.java",
			    "\n" +
			    "public class T924iecc5 {}\n" +
			    "    \n"
            },
            "true220002");
    }

    // a role is unregistered between two callins
    // 9.2.5-otjld-role-is-unregistered-1
    public void test925_roleIsUnregistered1() {
       
       runConformTest(
            new String[] {
		"Team925riu1.java",
			    "\n" +
			    "public team class Team925riu1 {\n" +
			    "    String current = \"O\";\n" +
			    "    protected class R1 playedBy T925riu1 {}\n" +
			    "    protected class R playedBy T925riu1 {\n" +
			    "        String val;\n" +
			    "        public R(T925riu1 b) {\n" +
			    "            val = current;\n" +
			    "            current = \"K\";\n" +
			    "        }\n" +
			    "        void print() {\n" +
			    "            System.out.print(val);\n" +
			    "            unregisterRole(this, R.class);\n" +
			    "        }\n" +
			    "        print <- after foo;\n" +
			    "    }\n" +
			    "    Team925riu1() { activate() ;}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        org.objectteams.Team t = new Team925riu1();\n" +
			    "        T925riu1 b = new T925riu1();\n" +
			    "        b.foo();\n" +
			    "        b.foo();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T925riu1.java",
			    "\n" +
			    "public class T925riu1 {\n" +
			    "    void foo() {}\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a role is unregistered between two callins
    // 9.2.5-otjld-role-is-unregistered-2
    public void test925_roleIsUnregistered2() {
       
       runConformTest(
            new String[] {
		"Team925riu2.java",
			    "\n" +
			    "public team class Team925riu2 {\n" +
			    "    String current = \"O\";\n" +
			    "    protected class R2 playedBy T925riu2 {}\n" +
			    "    protected class R playedBy T925riu2 {\n" +
			    "        String val;\n" +
			    "        public R(T925riu2 b) {\n" +
			    "            val = current;\n" +
			    "            current = \"K\";\n" +
			    "        }\n" +
			    "        void print() {\n" +
			    "            System.out.print(val);\n" +
			    "            Team925riu2.this.unregisterRole(this);\n" +
			    "        }\n" +
			    "        print <- after foo;\n" +
			    "    }\n" +
			    "    Team925riu2() { activate() ;}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        org.objectteams.Team t = new Team925riu2();\n" +
			    "        T925riu2 b = new T925riu2();\n" +
			    "        b.foo();\n" +
			    "        b.foo();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T925riu2.java",
			    "\n" +
			    "public class T925riu2 {\n" +
			    "    void foo() {}\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a class literal for a role class is used in a team method across team inheritance
    // 9.2.6-otjld-role-class-literal-access-1
    public void test926_roleClassLiteralAccess1() {
       
       runConformTest(
            new String[] {
		"Team926rcla1_2.java",
			    "\n" +
			    "public team class Team926rcla1_2 extends Team926rcla1_1 {\n" +
			    "    void testR(T926rcla1 as R r) {\n" +
			    "        test(r); // lowered again ..\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team926rcla1_2 t = new Team926rcla1_2();\n" +
			    "        t.testR(new T926rcla1());\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T926rcla1.java",
			    "\n" +
			    "public class T926rcla1 {}    \n" +
			    "    \n",
		"Team926rcla1_1.java",
			    "\n" +
			    "public team class Team926rcla1_1 {\n" +
			    "    protected class R playedBy T926rcla1 {}\n" +
			    "    void test(T926rcla1 b) {\n" +
			    "        if (hasRole(b, R.class))\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        else \n" +
			    "            System.out.print(\"NOK\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a class literal for a role class is used in a role method across team inheritance
    // 9.2.6-otjld-role-class-literal-access-2
    public void test926_roleClassLiteralAccess2() {
       
       runConformTest(
            new String[] {
		"Team926rcla2_2.java",
			    "\n" +
			    "public team class Team926rcla2_2 extends Team926rcla2_1 {\n" +
			    "    void testR(T926rcla2 as R r) {\n" +
			    "        r.test(r); // lowered again ..\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team926rcla2_2 t = new Team926rcla2_2();\n" +
			    "        t.testR(new T926rcla2());\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T926rcla2.java",
			    "\n" +
			    "public class T926rcla2 {}    \n" +
			    "    \n",
		"Team926rcla2_1.java",
			    "\n" +
			    "public team class Team926rcla2_1 {\n" +
			    "    protected class R playedBy T926rcla2 {\n" +
			    "        protected void test(T926rcla2 b) {\n" +
			    "            if (hasRole(b, R.class))\n" +
			    "                System.out.print(\"OK\");\n" +
			    "            else \n" +
			    "                System.out.print(\"NOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }
    
    // role class literal in nested team - inconsistent team/role combination
    // see Bug 311201 -  hasRole(Object, Class) call in inner teams ends up in NoSuchMethodError
    public void test926_roleClassLiteralAccess3() {
    	runNegativeTest(
    		new String[] {
    	"T926rcla3Main.java",
    			"public class T926rcla3Main {\n" +
    			"	public static void main(String ... args) {\n" +
    			"		T926rcla3_1 ob = new T926rcla3_1();\n" +
    			"		T926rcla3_2 ib = new T926rcla3_2();\n" +
    			"		final Team926rcla3 ot = new Team926rcla3();\n" +
    			"		Inner926rcla3<@ot> it = ot.registerTeam(ob);\n" +
    			"		it.test(ib);\n" +
    			"	}\n" +
    			"}\n",
    	"Team926rcla3.java",
				"public team class Team926rcla3 {\n" +
				"	public team class Inner926rcla3 playedBy T926rcla3_1 {\n" +
				"		protected class Role926rcla3 playedBy T926rcla3_2 {}\n" +
				"		@SuppressWarnings(\"ambiguouslowering\")\n" +
				"		public void test(T926rcla3_2 as Role926rcla3 o) {\n" +
				"			System.out.print(Team926rcla3.this.hasRole(o, Role926rcla3.class));\n" +
				"		}\n" +
				"	}\n" +
				"	public Inner926rcla3 registerTeam(T926rcla3_1 as Inner926rcla3 t) { return t; }\n" +
				"}\n",
    	"T926rcla3_1.java",
    			"public class T926rcla3_1 {}\n",
    	"T926rcla3_2.java",
    			"public class T926rcla3_2 {}\n"
    		},
    		"",
    		null /*classLibraries*/,
    		true /*shouldFlush*/,
    		null /*customOptions*/,
    		"java.lang.IllegalArgumentException: No such bound role type in this team: Team926rcla3$__OT__Inner926rcla3$Role926rcla3\n" + 
    		"	at Team926rcla3.hasRole(Team926rcla3.java:1)\n" + 
    		"	at Team926rcla3$__OT__Inner926rcla3.test(Team926rcla3.java:6)\n" + 
    		"	at T926rcla3Main.main(T926rcla3Main.java:7)");
    }
    
    // role class literal in nested team
    // see Bug 311201 -  hasRole(Object, Class) call in inner teams ends up in NoSuchMethodError
    public void test926_roleClassLiteralAccess4() {
    	runConformTest(
    		new String[] {
    	"T926rcla4Main.java",
    			"public class T926rcla4Main {\n" +
    			"	public static void main(String ... args) {\n" +
    			"		T926rcla4_1 ob = new T926rcla4_1();\n" +
    			"		T926rcla4_2 ib = new T926rcla4_2();\n" +
    			"		final Team926rcla4 ot = new Team926rcla4();\n" +
    			"		Inner926rcla4<@ot> it = ot.registerTeam(ob);\n" +
    			"		it.test(ib);\n" +
    			"	}\n" +
    			"}\n",
    	"Team926rcla4.java",
				"public team class Team926rcla4 {\n" +
				"	public team class Inner926rcla4 playedBy T926rcla4_1 {\n" +
				"		protected class Role926rcla4 extends Confined playedBy T926rcla4_2 {}\n" +
				"		public void test(T926rcla4_2 as Role926rcla4 o) {\n" +
				"			System.out.print(Inner926rcla4.this.hasRole(o, Role926rcla4.class));\n" +
				"		}\n" +
				"	}\n" +
				"	public Inner926rcla4 registerTeam(T926rcla4_1 as Inner926rcla4 t) { return t; }\n" +
				"}\n",
    	"T926rcla4_1.java",
    			"public class T926rcla4_1 {}\n",
    	"T926rcla4_2.java",
    			"public class T926rcla4_2 {}\n"
    		},
    		"true");
    }

    // get roles from one role cache
    // 9.2.7-otjld-get-all-roles-1
    public void test927_getAllRoles1() {
       
       runConformTest(
            new String[] {
		"Team927gar1.java",
			    "\n" +
			    "import java.util.Arrays;    \n" +
			    "public team class Team927gar1 {\n" +
			    "    public class R1 implements Comparable<Object> playedBy T927gar1 { \n" +
			    "        toString => toString;\n" +
			    "        public int compareTo(Object other) {\n" +
			    "            return toString().compareTo(other.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected class R2 playedBy T927gar1 { \n" +
			    "        toString => toString;\n" +
			    "    }\n" +
			    "    R1 r1a, r1b;\n" +
			    "    R2 r2;\n" +
			    "    Team927gar1 () {\n" +
			    "        r1a= new R1(new T927gar1(\"R1a\"));\n" +
			    "        r1b= new R1(new T927gar1(\"R1b\"));\n" +
			    "        r2= new R2(new T927gar1(\"R2\"));\n" +
			    "    }\n" +
			    "    Object[] getAllR1() {\n" +
			    "        return getAllRoles(R1.class);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team927gar1 t = new Team927gar1();\n" +
			    "        Object[] roles = t.getAllR1();\n" +
			    "        Arrays.sort(roles);\n" +
			    "        for(Object o : roles)\n" +
			    "            System.out.print(o);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T927gar1.java",
			    "\n" +
			    "public class T927gar1 {\n" +
			    "    String v;\n" +
			    "    public T927gar1(String v) { this.v = v; }\n" +
			    "    public String toString() { return v; }\n" +
			    "}    \n" +
			    "    \n"
            },
            "R1aR1b");
    }

    // get roles from all (two) role caches
    // 9.2.7-otjld-get-all-roles-2
    public void test927_getAllRoles2() {
       
       runConformTest(
            new String[] {
		"Team927gar2.java",
			    "\n" +
			    "import java.util.Arrays;    \n" +
			    "public team class Team927gar2 {\n" +
			    "    public class R1 implements Comparable<Object> playedBy T927gar2 { \n" +
			    "        toString => toString;\n" +
			    "        public int compareTo(Object other) {\n" +
			    "            return toString().compareTo(other.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected class R2 implements Comparable<Object> playedBy T927gar2 { \n" +
			    "        toString => toString;\n" +
			    "        public int compareTo(Object other) {\n" +
			    "            return toString().compareTo(other.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    R1 r1a, r1b;\n" +
			    "    R2 r2;\n" +
			    "    Team927gar2 () {\n" +
			    "        r1a= new R1(new T927gar2(\"R1a\"));\n" +
			    "        r1b= new R1(new T927gar2(\"R1b\"));\n" +
			    "        r2= new R2(new T927gar2(\"R2\"));\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team927gar2 t = new Team927gar2();\n" +
			    "        Object[] roles = t.getAllRoles();\n" +
			    "        Arrays.sort(roles);\n" +
			    "        for(Object o : roles)\n" +
			    "            System.out.print(o);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T927gar2.java",
			    "\n" +
			    "public class T927gar2 {\n" +
			    "    String v;\n" +
			    "    public T927gar2(String v) { this.v = v; }\n" +
			    "    public String toString() { return v; }\n" +
			    "}    \n" +
			    "    \n"
            },
            "R1aR1bR2");
    }

    // selectively get roles of specified type (using super class to actually select all)
    // 9.2.7-otjld-get-all-roles-3
    public void test927_getAllRoles3() {
       
       runConformTest(
            new String[] {
		"Team927gar3.java",
			    "\n" +
			    "import java.util.Arrays;\n" +
			    "public team class Team927gar3 {\n" +
			    "    public class R1 implements Comparable<Object> playedBy T927gar3 {\n" +
			    "        toString => toString;\n" +
			    "        public int compareTo(Object other) {\n" +
			    "            return toString().compareTo(other.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected class R2 extends R1 {\n" +
			    "        toString => toString;\n" +
			    "    }\n" +
			    "    R1 r1;\n" +
			    "    R2 r2;\n" +
			    "    @SuppressWarnings(\"roleinstantiation\")\n" +
			    "    Team927gar3 () {\n" +
			    "        r1= new R1(new T927gar3(\"R1a\"));\n" +
			    "        new R1(new T927gar3(\"R1b\")); // this role will be garbage collected\n" +
			    "        r2= new R2(new T927gar3(\"R2\"));\n" +
			    "    }\n" +
			    "    Object[] getAllR1() {\n" +
			    "	System.gc();\n" +
			    "        return getAllRoles(R1.class);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team927gar3 t = new Team927gar3();\n" +
			    "        Object[] roles = t.getAllR1();\n" +
			    "        Arrays.sort(roles);\n" +
			    "        for(Object o : roles)\n" +
			    "            System.out.print(o);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T927gar3.java",
			    "\n" +
			    "public class T927gar3 {\n" +
			    "    String v;\n" +
			    "    public T927gar3(String v) { this.v = v; }\n" +
			    "    public String toString() { return v; }\n" +
			    "}\n" +
			    "    \n"
            },
            "R1aR2");
    }

    // selectively get roles of specified type (only subclass)
    // 9.2.7-otjld-get-all-roles-4
    public void test927_getAllRoles4() {
       
       runConformTest(
            new String[] {
		"Team927gar4.java",
			    "\n" +
			    "import java.util.Arrays;\n" +
			    "public team class Team927gar4 {\n" +
			    "    public class R1 implements Comparable<Object> playedBy T927gar4 {\n" +
			    "        toString => toString;\n" +
			    "        public int compareTo(Object other) {\n" +
			    "            return toString().compareTo(other.toString());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class R2 extends R1 {\n" +
			    "        public abstract String printRole();\n" +
			    "        String printRole() -> String toString();\n" +
			    "    }\n" +
			    "    R1 r1a, r1b;\n" +
			    "    R2 r2;\n" +
			    "    @SuppressWarnings(\"roleinstantiation\")\n" +
			    "    Team927gar4 () {\n" +
			    "        r1a= new R1(new T927gar4(\"R1a\"));\n" +
			    "        r1b= new R1(new T927gar4(\"R1b\"));\n" +
			    "        r2= new R2(new T927gar4(\"R2\"));\n" +
			    "    }\n" +
			    "    R2[] getAllR2() {\n" +
			    "        return getAllRoles(R2.class);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team927gar4 t = new Team927gar4();\n" +
			    "        R2<@t>[] roles = t.getAllR2();\n" +
			    "        Arrays.sort(roles);\n" +
			    "        for(R2<@t> o : roles)\n" +
			    "            System.out.print(o.printRole());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T927gar4.java",
			    "\n" +
			    "public class T927gar4 {\n" +
			    "    String v;\n" +
			    "    public T927gar4(String v) { this.v = v; }\n" +
			    "    public String toString() { return v; }\n" +
			    "}\n" +
			    "    \n"
            },
            "R2");
    }
    
    // challenge concurrent modification exception (see https://bugs.eclipse.org/308181)
    public void test927_getAllRoles5() {
        runConformTest(
             new String[] {
        "T927gar5.java",
        		"public class T927gar5 {}\n",
        "Team927gar5.java",
        		"public team class Team927gar5 {\n" +
        		"	public class R playedBy T927gar5 {} \n" +
        		"	public R accept(T927gar5 as R r) { return r;}\n" +
             	"}\n",
        "T927gar5Main.java",
        		"public class T927gar5Main {\n" +
        		"	public static void main(String[] args) {\n" +
				"		final Team927gar5 t = new Team927gar5();\n" +
				"		final int[] nProbes = new int[1];\n" +
				"		new Thread(new Runnable() {\n" +
				"			public void run() {\n" +
				"				while (true) {\n" +
				"					int n = t.getAllRoles().length;\n" +
				"					nProbes[0]++;\n" +
				"				}\n" +
             	"			}\n" +
             	"		}).start();\n" +
				"		T927gar5[] bases = new T927gar5[100000];\n" +
				"		R<@t>[] roles = new R<@t>[100000];\n" +
				"		for (int i=0; i<bases.length; i+=3)\n" +
				"			bases[i] = new T927gar5();\n" +
				"		for (int k=0; k<10000; k++) {\n" +
				"			int i = (int)(bases.length * Math.random());\n" +
				"			if (bases[i] == null) {\n" +
				"				roles[i] = t.accept(bases[i] = new T927gar5());\n" +
             	"			} else {\n" +
				"				t.unregisterRole(roles[i]);\n" +
				"				bases[i] = null;\n" +
				"				roles[i] = null;\n" +
             	"			}\n" +
             	"		}\n" +
        		"		//System.out.println(\"N=\"+nProbes[0]);\n" +
        		"		System.exit(0);\n" +
                "	}\n" +
        		"}"
             },
             "");
    }
    // Bug 336152 - [compiler] incompatible cast error if "Object" is hidden by an import 
    public void test927_getAllRoles6() {
    	runConformTest(
    		new String[] {
    	"Team927gar6.java",
    			"import t927gar6.Object;\n" + 
    			"\n" + 
    			"public team class Team927gar6 {\n" + 
    			"	protected class R playedBy Object {\n" + 
    			"		protected R() { base(); }\n" + 
    			"		toString => toString;\n" + 
    			"		\n" + 
    			"	}\n" + 
    			"	public static void main(String[] args) {\n" + 
    			"		Team927gar6 t = new Team927gar6();\n" + 
    			"		t.test();\n" + 
    			"	}\n" + 
    			"	private void test() {\n" + 
    			"		System.out.println(new R().toString());\n" + 
    			"	}\n" + 
    			"}\n",
    	"t927gar6/Object.java",
    			"package t927gar6;\n" + 
    			"\n" + 
    			"public class Object {\n" + 
    			"	@Override\n" + 
    			"	public String toString() {\n" + 
    			"		return \"OK\";\n" + 
    			"	}\n" + 
    			"}\n"
    		},
    		"OK");
    }
}
