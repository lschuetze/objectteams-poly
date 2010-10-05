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
package org.eclipse.objectteams.otdt.tests.otjld.rolesandteams;

import java.util.Map;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

@SuppressWarnings("unchecked")
public class ValueParameters extends AbstractOTJLDTest {
	
	public ValueParameters(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "testA120_enumInTeam1", "testA120_enumInTeam2"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return ValueParameters.class;
	}

    // a type is anchored to a non-existent variable
    // 1.9.1-otjld-invalid-type-anchor-1
    public void test191_invalidTypeAnchor1() {
        runNegativeTestMatching(
            new String[] {
		"T191ita1.java",
			    "\n" +
			    "public class T191ita1 {\n" +
			    "    R<@wrong> r;\n" +
			    "}    \n" +
			    "    \n",
		"Team191ita1.java",
			    "\n" +
			    "public team class Team191ita1 {\n" +
			    "    public class R {}\n" +
			    "}    \n" +
			    "    \n"
            },
            "9.2.1");
    }

    // a type is anchored to a non-existent variable - two wrong components
    // 1.9.1-otjld-invalid-type-anchor-2
    public void test191_invalidTypeAnchor2() {
        runNegativeTestMatching(
            new String[] {
		"T191ita2.java",
			    "\n" +
			    "public class T191ita2 {\n" +
			    "    R<@wrong.worse> r;\n" +
			    "}    \n" +
			    "    \n",
		"Team191ita2.java",
			    "\n" +
			    "public team class Team191ita2 {\n" +
			    "    public class R {}\n" +
			    "}    \n" +
			    "    \n"
            },
            "9.2.1");
    }

    // a type is anchored to a non-existent variable - second component is wrong
    // 1.9.1-otjld-invalid-type-anchor-3
    public void test191_invalidTypeAnchor3() {
        runNegativeTestMatching(
            new String[] {
		"T191ita3.java",
			    "\n" +
			    "public class T191ita3 {\n" +
			    "    Object good;\n" +
			    "    R<@good.worse> r;\n" +
			    "}    \n" +
			    "    \n",
		"Team191ita3.java",
			    "\n" +
			    "public team class Team191ita3 {\n" +
			    "    public class R {}\n" +
			    "}    \n" +
			    "    \n"
            },
            "9.2.1");
    }

    // a type is anchored to a  variable with unresolved type
    // 1.9.1-otjld-invalid-type-anchor-4
    public void test191_invalidTypeAnchor4() {
        runNegativeTestMatching(
            new String[] {
		"T191ita4Main.java",
			    "\n" +
			    "// missing import: import org.objectteams.Team;    \n" +
			    "public class T191ita4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team t = new Team();\n" +
			    "        T191ita4<@t> r = t.new T191ita4();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T191ita4.java",
			    "\n" +
			    "import org.objectteams.Team;    \n" +
			    "public class T191ita4<Team t> {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "9.2.1");
    }

    // a type is anchored to a type reference
    // 1.9.1-otjld-invalid-type-anchor-5
    public void test191_invalidTypeAnchor5() {
        runNegativeTestMatching(
            new String[] {
		"T191ita5Main.java",
			    "\n" +
			    "public class T191ita5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T191ita5<@java.lang.Object> r;\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T191ita5.java",
			    "\n" +
			    "import org.objectteams.Team;    \n" +
			    "public class T191ita5<Team t> {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "9.2.1");
    }

    // a regular class has a value parameter - compile in one pass
    // 1.9.2-otjld-regular-class-with-value-parameter-1
    public void test192_regularClassWithValueParameter1() {
       
       runConformTest(
            new String[] {
		"T192rcwvp1Main.java",
			    "\n" +
			    "import org.objectteams.Team;    \n" +
			    "public class T192rcwvp1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team t = new Team();\n" +
			    "        T192rcwvp1<@t> r = new T192rcwvp1<@t>();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T192rcwvp1.java",
			    "\n" +
			    "import org.objectteams.Team;    \n" +
			    "public class T192rcwvp1<Team t> {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a regular class has a value parameter - compile in separately
    // 1.9.2-otjld-regular-class-with-value-parameter-2
    public void test192_regularClassWithValueParameter2() {
       
       runConformTest(
            new String[] {
		"T192rcwvp2Main.java",
			    "\n" +
			    "import org.objectteams.Team;    \n" +
			    "public class T192rcwvp2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team t = new Team();\n" +
			    "        T192rcwvp2<@t> r = new T192rcwvp2<@t>();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T192rcwvp2.java",
			    "\n" +
			    "import org.objectteams.Team;    \n" +
			    "public class T192rcwvp2<Team t> {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a type has a valid anchor but the type cannot be resolved - role type
    // 1.9.4-otjld-invalid-anchored-type-1
    public void test194_invalidAnchoredType1() {
        runNegativeTestMatching(
            new String[] {
		"T194iat1.java",
			    "\n" +
			    "public class T194iat1 {\n" +
			    "    Object o = new Object();\n" +
			    "    R<@o> r;\n" +
			    "}    \n" +
			    "    \n",
		"Team194iat1.java",
			    "\n" +
			    "public team class Team194iat1 {\n" +
			    "    public class R {}\n" +
			    "}    \n" +
			    "    \n"
            },
            "resolve");
    }

    // a type has a valid anchor but the type cannot be resolved - plain dependent type
    // 1.9.4-otjld-invalid-anchored-type-2
    public void test194_invalidAnchoredType2() {
        runNegativeTestMatching(
            new String[] {
		"T194iat2_2.java",
			    "\n" +
			    "public class T194iat2_2 {\n" +
			    "    Object o = new Object();\n" +
			    "    Wrong<@o> r;\n" +
			    "}    \n" +
			    "    \n",
		"T194iat2_1.java",
			    "\n" +
			    "public class T194iat2_1<String anchor> {\n" +
			    "}    \n" +
			    "    \n"
            },
            "resolve");
    }

    // a type has a valid anchor but the anchor does not match - plain dependent type
    // 1.9.4-otjld-invalid-anchored-type-3
    public void test194_invalidAnchoredType3() {
        runNegativeTestMatching(
            new String[] {
		"T194iat3_2.java",
			    "\n" +
			    "public class T194iat3_2 {\n" +
			    "    Object o = new Object();\n" +
			    "    T194iat3_1<@o> r;\n" +
			    "}    \n" +
			    "    \n",
		"T194iat3_1.java",
			    "\n" +
			    "public class T194iat3_1<String anchor> {\n" +
			    "}    \n" +
			    "    \n"
            },
            "9.2.1");
    }

    // a type has a valid anchor but the defines no value parameter - plain dependent type
    // 1.9.4-otjld-invalid-anchored-type-4
    public void test194_invalidAnchoredType4() {
        runNegativeTestMatching(
            new String[] {
		"T194iat4_2.java",
			    "\n" +
			    "public class T194iat4_2 {\n" +
			    "    Object o = new Object();\n" +
			    "    T194iat4_1<@o> r;\n" +
			    "}    \n" +
			    "    \n",
		"T194iat4_1.java",
			    "\n" +
			    "public class T194iat4_1 {\n" +
			    "}    \n" +
			    "    \n"
            },
            "9.2.1");
    }

    // a qualified type reference has a value parameter
    // 1.9.5-otjld-qualified-dependent-type-reference-1
    public void test195_qualifiedDependentTypeReference1() {
        runNegativeTestMatching(
            new String[] {
		"T195qdtr1.java",
			    "\n" +
			    "public class T195qdtr1 {\n" +
			    "    final String s;\n" +
			    "    java.util.List<@s> l;\n" +
			    "}\n" +
			    "    \n"
            },
            "A.9(a)");
    }

    // using "base" in a type anchor
    // 1.9.6-otjld-base-anchored-type-1
    public void test196_baseAnchoredType1() {
       
       runConformTest(
            new String[] {
		"Team196bat1_2.java",
			    "\n" +
			    "public team class Team196bat1_2 {\n" +
			    "    protected team class Mid playedBy Team196bat1_1 {\n" +
			    "        protected class R playedBy R<@base> {\n" +
			    "            void k() { System.out.print(\"K\"); }\n" +
			    "            k <- after test;\n" +
			    "        }\n" +
			    "        protected Mid() { base(); activate(); run(); }\n" +
			    "        void run() -> void run();\n" +
			    "    }\n" +
			    "    Team196bat1_2() {\n" +
			    "        new Mid();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team196bat1_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team196bat1_1.java",
			    "\n" +
			    "public team class Team196bat1_1  {\n" +
			    "    public class R {\n" +
			    "        protected void test() {\n" +
			    "            System.out.print(\"O\");\n" +
			    "        }\n" +
			    "    }    \n" +
			    "    public void run() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // using "base" in a type anchor-prefix
    // 1.9.6-otjld-base-anchored-type-2
    public void test196_baseAnchoredType2() {
        runNegativeTestMatching(
            new String[] {
		"Team196bat2_2.java",
			    "\n" +
			    "public team class Team196bat2_2 {\n" +
			    "    protected team class Role playedBy T196bat2 {\n" +
			    "        R<@base.theTeam> r;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team196bat2_1.java",
			    "\n" +
			    "public team class Team196bat2_1  {\n" +
			    "    public class R {\n" +
			    "        void test() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public R getR() {\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T196bat2.java",
			    "\n" +
			    "public class T196bat2 {\n" +
			    "    public final Team196bat2_1 theTeam = new Team196bat2_1();\n" +
			    "    R<@theTeam> getR() {\n" +
			    "        return theTeam.getR();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Syntax");
    }

    // using "base" in a type anchor
    // 1.9.6-otjld-base-anchored-type-3
    public void test196_baseAnchoredType3() {
       
       runConformTest(
            new String[] {
		"Team196bat3_2.java",
			    "\n" +
			    "public team class Team196bat3_2 {\n" +
			    "    protected team class Mid playedBy Team196bat3_1 {\n" +
			    "        int i=0;\n" +
			    "        protected class RU playedBy R<@base> {\n" +
			    "            void k() { \n" +
			    "                Mid.this.i++;\n" +
			    "                getR().test();\n" +
			    "            }\n" +
			    "            k <- after test when (i<2);\n" +
			    "            R<@Mid.base> getR() -> R<@Mid.base> getOther();\n" +
			    "        }\n" +
			    "        protected Mid() { base(); activate(); run(); }\n" +
			    "        void run() -> void run();\n" +
			    "    }\n" +
			    "    Team196bat3_2() {\n" +
			    "        new Mid();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team196bat3_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team196bat3_1.java",
			    "\n" +
			    "public team class Team196bat3_1  {\n" +
			    "    public class R {\n" +
			    "        String val;\n" +
			    "        protected R(String v) { this.val = v; }\n" +
			    "        public void test() {\n" +
			    "            System.out.print(val);\n" +
			    "        }\n" +
			    "        public R getOther() {\n" +
			    "            return new R(\"O\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void run() {\n" +
			    "        new R(\"K\").test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "KOO");
    }

    // using "base" in a type anchor (callout-return), nested generics
    // 1.9.6-otjld-base-anchored-type-4
    public void test196_baseAnchoredType4() {
       
       runConformTest(
            new String[] {
		"Team196bat4_2.java",
			    "\n" +
			    "import java.util.List;\n" +
			    "public team class Team196bat4_2 {\n" +
			    "    protected team class Mid playedBy Team196bat4_1 {\n" +
			    "        int i=0;\n" +
			    "        protected class RU playedBy R<@base> {\n" +
			    "            void k() { \n" +
			    "                Mid.this.i++;\n" +
			    "                getR().test();\n" +
			    "            }\n" +
			    "            k <- after test when (i<2);\n" +
			    "            R<@Mid.base> getR() -> List<R<@Mid.base>> getOther()\n" +
			    "                with { result <- result.get(0) }\n" +
			    "        }\n" +
			    "        protected Mid() { base(); activate(); run(); }\n" +
			    "        void run() -> void run();\n" +
			    "    }\n" +
			    "    Team196bat4_2() {\n" +
			    "        new Mid();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team196bat4_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team196bat4_1.java",
			    "\n" +
			    "import java.util.*;\n" +
			    "public team class Team196bat4_1  {\n" +
			    "    public class R {\n" +
			    "        String val;\n" +
			    "        protected R(String v) { this.val = v; }\n" +
			    "        public void test() {\n" +
			    "            System.out.print(val);\n" +
			    "        }\n" +
			    "        public List<R> getOther() {\n" +
			    "            List<R> l = new ArrayList<R>();\n" +
			    "            l.add(new R(\"O\"));\n" +
			    "            return l;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void run() {\n" +
			    "        new R(\"K\").test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "KOO");
    }

    // using "base" in a type anchor (callout-to-field-return), nested generics
    // 1.9.6-otjld-base-anchored-type-4f
    public void test196_baseAnchoredType4f() {
       
       runConformTest(
            new String[] {
		"Team196bat4f_2.java",
			    "\n" +
			    "import java.util.List;\n" +
			    "public team class Team196bat4f_2 {\n" +
			    "    protected team class Mid playedBy Team196bat4f_1 {\n" +
			    "        protected class RU playedBy R<@base> {\n" +
			    "            void k() { \n" +
			    "                getR().test();\n" +
			    "            }\n" +
			    "            k <- before test when (hasR());\n" +
			    "            R<@Mid.base> getR() -> get List<R1<@Mid.base>> other\n" +
			    "                with { result <- other.get(0) }\n" +
			    "			boolean hasR()      -> get List<R1<@Mid.base>> other\n" +
			    "				with { result <- !other.isEmpty() }\n" +
			    "        }\n" +
			    "        protected Mid() { base(); activate(); run(); }\n" +
			    "        void run() -> void run();\n" +
			    "    }\n" +
			    "    Team196bat4f_2() {\n" +
			    "        new Mid();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team196bat4f_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team196bat4f_1.java",
			    "\n" +
			    "import java.util.*;\n" +
			    "public team class Team196bat4f_1  {\n" +
			    "    public class R {\n" +
			    "        String val;\n" +
			    "        protected R(String v) { \n" +
			    "		this.val = v;\n" +
			    "		this.other = new ArrayList<R1>();\n" +
			    "		if (!val.equals(\"O\")) // avoid recursion!\n" +
			    "                    this.other.add(new R1(\"O\"));\n" +
			    "	}\n" +
			    "        public void test() {\n" +
			    "            System.out.print(val);\n" +
			    "        }\n" +
			    "        public List<R1> other;\n" +
			    "    }\n" +
			    "    public class R1 extends R {\n" +
			    "	protected R1(String v) { super(v); }\n" +
			    "    }\n" +
			    "    public void run() {\n" +
			    "        new R(\"K\").test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // using "base" in a type anchor (callin-return), array
    // 1.9.6-otjld-base-anchored-type-5
    public void test196_baseAnchoredType5() {
       
       runConformTest(
            new String[] {
		"Team196bat5_2.java",
			    "\n" +
			    "public team class Team196bat5_2 {\n" +
			    "    protected team class Mid playedBy Team196bat5_1 {\n" +
			    "        protected class RU playedBy R<@base> {\n" +
			    "            callin R<@Mid.base>[] getR() {\n" +
			    "				R<@Mid.base>[] rs = base.getR();\n" +
			    "				rs[0].setVal(\"O\");\n" +
			    "				return rs;\n" +
			    "			}\n" +
			    "            R<@Mid.base>[] getR() <- replace R<@Mid.base>[] getOther();\n" +
			    "        }\n" +
			    "        protected Mid() { base(); activate(); run(); }\n" +
			    "        void run() -> void run();\n" +
			    "    }\n" +
			    "    Team196bat5_2() {\n" +
			    "        new Mid();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team196bat5_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team196bat5_1.java",
			    "\n" +
			    "public team class Team196bat5_1  {\n" +
			    "    public class R {\n" +
			    "        String val;\n" +
			    "        protected R(String v) { this.val = v; }\n" +
			    "	public void setVal(String v) {\n" +
			    "		this.val = v;\n" +
			    "	}\n" +
			    "	public String toString() { return this.val; }\n" +
			    "        protected void test() {\n" +
			    "                R[] rs = getOther();\n" +
			    "                System.out.print(rs[0]);\n" +
			    "                System.out.print(this);\n" +
			    "        }\n" +
			    "        public R[] getOther() {\n" +
			    "            return new R[] {new R(\"F\")};\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void run() {\n" +
			    "        new R(\"K\").test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role type is explicitly anchored to 'this' (OTJLD(1.2.2(d))
    // 1.9.7-otjld-special-anchors-1
    public void test197_specialAnchors1() {
       
       runConformTest(
            new String[] {
		"Team197sa1.java",
			    "\n" +
			    "public team class Team197sa1 {\n" +
			    "    public class RoleX {\n" +
			    "        public void print() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class RoleY {}\n" +
			    "    public RoleX<@this> getRoleX (RoleY<@this> r) {\n" +
			    "        return new RoleX();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team197sa1 t = new Team197sa1();\n" +
			    "        RoleX<@t> r = t.getRoleX(t.new RoleY());\n" +
			    "        r.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a warning is given for old style path syntax
    // 1.9.8-otjld-old-syntax-1
    public void test198_oldSyntax1() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportDeprecatedPathSyntax, CompilerOptions.WARNING);
        runTestExpectingWarnings(
            new String[] {
		"Team198os1.java",
			    "\n" +
			    "public team class Team198os1 {\n" +
			    "    public class R {}\n" +
			    "    public void foo(final Team198os1 other, other.R r) {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
            "1. WARNING in Team198os1.java (at line 4)\n" +
            "	public void foo(final Team198os1 other, other.R r) {\n"+
            "	                                        ^^^^^^^\n" +
            "Path syntax for externalized role type is deprecated (OTJLD 1.2.2(b)).\n" +
            "----------\n");
    }

    // a warning is given for old style path syntax
    // 1.9.8-otjld-old-syntax-2
    public void test198_oldSyntax2() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportDeprecatedPathSyntax, CompilerOptions.WARNING);
        runTestExpectingWarnings(
            new String[] {
		"Team198os2.java",
			    "\n" +
			    "public team class Team198os2 {\n" +
			    "    public class R {}\n" +
			    "}\n" +
			    "    \n",
		"T198os2Main.java",
			    "\n" +
			    "public class T198os2Main {\n" +
			    "    final Team198os2 other = new Team198os2();\n" +
			    "    public void foo(other.R r) {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
            "1. WARNING in T198os2Main.java (at line 4)\n" +
            "	public void foo(other.R r) {\n" +
            "	                ^^^^^^^\n" +
            "Path syntax for externalized role type is deprecated (OTJLD 1.2.2(b)).\n" +
        	"----------\n");
    }

    // a team has a value parameter - parameter used as value
    // 1.9.9-otjld-parameterized-team-1
    public void test199_parameterizedTeam1() {
       
       runConformTest(
            new String[] {
		"T199pt1Main.java",
			    "\n" +
			    "public class T199pt1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final T199pt1_1 t1= new T199pt1_1();\n" +
			    "        Team199pt1_2<@t1> t2= new Team199pt1_2<@t1>();\n" +
			    "        t2.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T199pt1_1.java",
			    "\n" +
			    "public class T199pt1_1 {\n" +
			    "    void print() {\n" +
			    "        System.out.print(\"T1\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt1_2.java",
			    "\n" +
			    "public team class Team199pt1_2<T199pt1_1 other> {\n" +
			    "    public void test() {\n" +
			    "        other.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "T1");
    }

    // a team has a value parameter - parameter used as anchor for field type
    // 1.9.9-otjld-parameterized-team-2
    public void test199_parameterizedTeam2() {
       
       runConformTest(
            new String[] {
		"T199pt2Main.java",
			    "\n" +
			    "public class T199pt2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team199pt2_1 t1= new Team199pt2_1();\n" +
			    "        Team199pt2_2<@t1> t2= new Team199pt2_2<@t1>();\n" +
			    "        t2.setR(t1.getR());\n" +
			    "        t2.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt2_1.java",
			    "\n" +
			    "public team class Team199pt2_1 {\n" +
			    "    public class R1 {\n" +
			    "        public void print() {\n" +
			    "            System.out.print(\"R1\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public R1 getR() {\n" +
			    "        return new R1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt2_2.java",
			    "\n" +
			    "public team class Team199pt2_2<Team199pt2_1 other> {\n" +
			    "    R1<@other> r1;\n" +
			    "    public void setR(R1<@other> r) {\n" +
			    "        this.r1= r;\n" +
			    "    }\n" +
			    "    public void test() {\n" +
			    "        this.r1.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R1");
    }

    // a team has a value parameter - parameter used as anchor for base-role
    // 1.9.9-otjld-parameterized-team-3
    public void test199_parameterizedTeam3() {
       
       runConformTest(
            new String[] {
		"T199pt3Main.java",
			    "\n" +
			    "public class T199pt3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team199pt3_1 t1= new Team199pt3_1();\n" +
			    "        Team199pt3_2<@t1> t2= new Team199pt3_2<@t1>();\n" +
			    "        t2.test(t1.getR());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt3_1.java",
			    "\n" +
			    "public team class Team199pt3_1 {\n" +
			    "    public class R1 {\n" +
			    "        public void print() {\n" +
			    "            System.out.print(\"R1\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public R1 getR() {\n" +
			    "        return new R1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt3_2.java",
			    "\n" +
			    "public team class Team199pt3_2<Team199pt3_1 other> {\n" +
			    "    protected class R2 playedBy R1<@other> {\n" +
			    "        protected void print() {\n" +
			    "            System.out.print(\"R2->\");\n" +
			    "            basePrint();\n" +
			    "        }\n" +
			    "        void basePrint() -> void print();\n" +
			    "    }\n" +
			    "    public void test(R1<@other> as R2 r) {\n" +
			    "        r.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R2->R1");
    }

    // a team has a value parameter - parameter used as anchor for base-role - uses decapsulation
    // 1.9.9-otjld-parameterized-team-3d
    public void test199_parameterizedTeam3d() {
       
       runConformTest(
            new String[] {
		"T199pt3dMain.java",
			    "\n" +
			    "public class T199pt3dMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team199pt3d_1 t1= new Team199pt3d_1();\n" +
			    "        Team199pt3d_2<@t1> t2= new Team199pt3d_2<@t1>();\n" +
			    "        t2.test(t1.getR());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt3d_1.java",
			    "\n" +
			    "public team class Team199pt3d_1 {\n" +
			    "    public class R1 {\n" +
			    "        void print() {\n" +
			    "            System.out.print(\"R1\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public R1 getR() {\n" +
			    "        return new R1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt3d_2.java",
			    "\n" +
			    "public team class Team199pt3d_2<Team199pt3d_1 other> {\n" +
			    "    protected class R2 playedBy R1<@other> {\n" +
			    "        protected void print() {\n" +
			    "            System.out.print(\"R2->\");\n" +
			    "            basePrint();\n" +
			    "        }\n" +
			    "        @SuppressWarnings(\"decapsulation\")\n" +
			    "        void basePrint() -> void print();\n" +
			    "    }\n" +
			    "    public void test(R1<@other> as R2 r) {\n" +
			    "        r.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "R2->R1");
    }

    // a team has a value parameter - nesting involved
    // 1.9.9-otjld-parameterized-team-4
    public void test199_parameterizedTeam4() {
       
       runConformTest(
            new String[] {
		"T199pt4Main.java",
			    "\n" +
			    "public class T199pt4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team199pt4_1 t1= new Team199pt4_1();\n" +
			    "        Team199pt4_2<@t1> t2= new Team199pt4_2<@t1>();\n" +
			    "        t2.test(t1.getR());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt4_1.java",
			    "\n" +
			    "public team class Team199pt4_1 {\n" +
			    "    public class R1 {\n" +
			    "        public void print() {\n" +
			    "            System.out.print(\"R1\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public R1 getR() {\n" +
			    "        return new R1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt4_2.java",
			    "\n" +
			    "public team class Team199pt4_2<Team199pt4_1 other> {\n" +
			    "    protected class RMid playedBy R1<@other> {\n" +
			    "        void basePrint() -> void print();\n" +
			    "    }\n" +
			    "    protected team class Mid {\n" +
			    "        protected class RInner playedBy RMid {\n" +
			    "            protected void print() {\n" +
			    "                System.out.print(\"RInner->\");\n" +
			    "                midPrint();\n" +
			    "            }\n" +
			    "            void midPrint() -> void basePrint();\n" +
			    "        }\n" +
			    "        protected void midTest(RMid as RInner r) {\n" +
			    "            r.print();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test(R1<@other> as RMid r) {\n" +
			    "        Mid m= new Mid();\n" +
			    "        m.midTest(r);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "RInner->R1");
    }

    // a team has a value parameter - nesting involved - unresolved method
    // 1.9.9-otjld-parameterized-team-4f
    public void test199_parameterizedTeam4f() {
        runNegativeTest(
            new String[] {
		"Team199pt4f_2.java",
			    "\n" +
			    "public team class Team199pt4f_2<Team199pt4f_1 other> {\n" +
			    "    protected class RMid playedBy R1<@other> {\n" +
			    "        void basePrint() -> void print();\n" +
			    "    }\n" +
			    "    protected team class Mid {\n" +
			    "	protected class RInner playedBy RMid {\n" +
			    "	    protected void print() {\n" +
			    "		System.out.print(\"RInner->\");\n" +
			    "		wrongPrint();\n" +
			    "	    }\n" +
			    "            void midPrint() -> void basePrint();\n" +
			    "	}\n" +
			    "        protected void midTest(RMid as RInner r) {\n" +
			    "            r.print();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test(R1<@other> as RMid r) {\n" +
			    "        Mid m= new Mid();\n" +
			    "        m.midTest(r);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt4f_1.java",
			    "\n" +
			    "public team class Team199pt4f_1 {\n" +
			    "    public class R1 {\n" +
			    "        public void print() {\n" +
			    "            System.out.print(\"R1\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public R1 getR() {\n" +
			    "        return new R1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team199pt4f_2.java (at line 10)\n" + 
    		"	wrongPrint();\n" + 
    		"	^^^^^^^^^^\n" + 
    		"The method wrongPrint() is undefined for the type Team199pt4f_2.Mid.RInner\n" + 
    		"----------\n");
    }

    // a team has a value parameter - nesting involved - very broken: missing team modifier
    // 1.9.9-otjld-parameterized-team-4ff
    public void test199_parameterizedTeam4ff() {
        runNegativeTestMatching(
            new String[] {
		"Team199pt4ff_2.java",
			    "\n" +
			    "public team class Team199pt4ff_2<Team199pt4ff_1 other> {\n" +
			    "    protected class RMid playedBy R1<@other> {\n" +
			    "        void basePrint() -> void print();\n" +
			    "    }\n" +
			    "    protected class Mid {\n" +
			    "        protected class RInner playedBy RMid {\n" +
			    "            protected void print() {\n" +
			    "                System.out.print(\"RInner->\");\n" +
			    "                wrongPrint();\n" +
			    "            }\n" +
			    "            void midPrint() -> void basePrint();\n" +
			    "        }\n" +
			    "        protected void midTest(RMid as RInner r) {\n" +
			    "            r.print();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test(R1<@other> as RMid r) {\n" +
			    "        Mid m= new Mid();\n" +
			    "        m.midTest(r);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team199pt4ff_1.java",
			    "\n" +
			    "public team class Team199pt4ff_1 {\n" +
			    "    public class R1 {\n" +
			    "        public void print() {\n" +
			    "            System.out.print(\"R1\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public R1 getR() {\n" +
			    "        return new R1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team199pt4ff_2.java (at line 7)\n" + 
    		"	protected class RInner playedBy RMid {\n" + 
    		"	                ^^^^^^\n" + 
    		"Member types not allowed in regular roles. Mark class Team199pt4ff_2.Mid as a team if RInner should be its role (OTJLD 1.5(a,b)). \n" + 
    		"----------\n" + 
    		"2. ERROR in Team199pt4ff_2.java (at line 10)\n" + 
    		"	wrongPrint();\n" + 
    		"	^^^^^^^^^^\n" + 
    		"The method wrongPrint() is undefined for the type Team199pt4ff_2.Mid.RInner\n" + 
    		"----------\n");
    }

    // a type depends on an enum instance, ctor has explicit arg, too
    // 1.9.10-otjld-dependent-type-1
    public void test1910_dependentType1() {
       
       runConformTest(
            new String[] {
		"T1910dt1Main.java",
			    "\n" +
			    "public class T1910dt1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final E1910dt1 tok = E1910dt1.Tok;\n" +
			    "        T1910dt1<@tok> t = new T1910dt1<@tok>(\"OK\");\n" +
			    "        System.out.print(t);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"E1910dt1.java",
			    "\n" +
			    "public enum E1910dt1 {\n" +
			    "    Tik, Tak, Tok;\n" +
			    "}\n" +
			    "    \n",
		"T1910dt1.java",
			    "\n" +
			    "public class T1910dt1<E1910dt1 ticker> {\n" +
			    "    String val;\n" +
			    "    public T1910dt1(String v) {\n" +
			    "        this.val = v;\n" +
			    "    }\n" +
			    "    public String toString() {\n" +
			    "        return this.val+this.ticker;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OKTok");
    }
}
