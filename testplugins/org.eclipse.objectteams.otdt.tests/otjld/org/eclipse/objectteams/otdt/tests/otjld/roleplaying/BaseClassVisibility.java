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

public class BaseClassVisibility extends AbstractOTJLDTest {

	public BaseClassVisibility(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test247_baseImportScope"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return BaseClassVisibility.class;
	}

    // a role class is played by a public top-level class in a different package
    // 2.4.1-otjld-visible-base-class-1
    public void test241_visibleBaseClass1() {

       runConformTest(
            new String[] {
		"T241vbc1Main.java",
			    "\n" +
			    "public class T241vbc1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        p2.Team241vbc1 t = new p2.Team241vbc1();\n" +
			    "        p1.T241vbc1    o = new p1.T241vbc1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T241vbc1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T241vbc1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p2/Team241vbc1.java",
			    "\n" +
			    "package p2;\n" +
			    "import base p1.T241vbc1;\n" +
			    "public team class Team241vbc1 {\n" +
			    "    public class Role241vbc1 playedBy T241vbc1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(p1.T241vbc1 as Role241vbc1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by a friendly top-level class in the same package
    // 2.4.1-otjld-visible-base-class-2
    public void test241_visibleBaseClass2() {

       runConformTest(
            new String[] {
		"T241vbc2Main.java",
			    "\n" +
			    "public class T241vbc2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team241vbc2 t = new Team241vbc2();\n" +
			    "        T241vbc2    o = new T241vbc2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T241vbc2.java",
			    "\n" +
			    "class T241vbc2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team241vbc2.java",
			    "\n" +
			    "public team class Team241vbc2 {\n" +
			    "    public class Role241vbc2 playedBy T241vbc2 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T241vbc2 as Role241vbc2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by its public top-level enclosing class
    // 2.4.1-otjld-visible-base-class-3
    public void test241_visibleBaseClass3() {
        runNegativeTestMatching(
            new String[] {
		"T241vbc3.java",
			    "\n" +
			    "public class T241vbc3 {\n" +
			    "    @SuppressWarnings(\"unused\") private String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    private team class Team241vbc3 {\n" +
			    "        public class Role241vbc3 playedBy T241vbc3 {\n" +
			    "            public abstract String getValue();\n" +
			    "            String getValue() -> String getValue() with {\n" +
			    "                result <- \"OK\"\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(T241vbc3 as Role241vbc3 obj) {\n" +
			    "            return obj.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T241vbc3    o = new T241vbc3();\n" +
			    "        Team241vbc3 t = o.new Team241vbc3();\n" +
			    " \n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(b)");
    }

    // a role class is played by its friendly top-level enclosing class
    // 2.4.1-otjld-visible-base-class-4
    public void test241_visibleBaseClass4() {
        runNegativeTestMatching(
            new String[] {
		"T241vbc4.java",
			    "\n" +
			    "class T241vbc4 {\n" +
			    "    @SuppressWarnings(\"unused\") private String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public team class Team241vbc4 {\n" +
			    "        protected class Role241vbc4 playedBy T241vbc4 {\n" +
			    "            public abstract String getValue();\n" +
			    "            String getValue() -> String getValue() with {\n" +
			    "                result <- \"OK\"\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(T241vbc4 as Role241vbc4 obj) {\n" +
			    "            return obj.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T241vbc4    o = new T241vbc4();\n" +
			    "        Team241vbc4 t = o.new Team241vbc4();\n" +
			    " \n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(b)");
    }

    // a role class is played by its protected enclosing class
    // 2.4.1-otjld-visible-base-class-5
    public void test241_visibleBaseClass5() {
        runNegativeTestMatching(
            new String[] {
		"T241vbc5.java",
			    "\n" +
			    "public class T241vbc5 {\n" +
			    "    protected static class Nested241vbc5 {\n" +
			    "        @SuppressWarnings(\"unused\") private String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    \n" +
			    "        public team class Team241vbc5 {\n" +
			    "            public class Role241vbc5 playedBy Nested241vbc5 {\n" +
			    "                public abstract String getValue();\n" +
			    "                String getValue() -> String getValue() with {\n" +
			    "                    result <- \"OK\"\n" +
			    "                }\n" +
			    "            }\n" +
			    "        \n" +
			    "            public String getValue(Nested241vbc5 as Role241vbc5 obj) {\n" +
			    "                return obj.getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Nested241vbc5 o = new Nested241vbc5();\n" +
			    "        Nested241vbc5.Team241vbc5   t = o.new Team241vbc5();\n" +
			    " \n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(b)");
    }

    // a role class is played by its private enclosing class
    // 2.4.1-otjld-visible-base-class-6
    public void test241_visibleBaseClass6() {
        runNegativeTestMatching(
            new String[] {
		"T241vbc6.java",
			    "\n" +
			    "public class T241vbc6 {\n" +
			    "    private class Inner241vbc6 {\n" +
			    "        @SuppressWarnings(\"unused\") private String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    \n" +
			    "        protected team class Team241vbc6 {\n" +
			    "            public class Role241vbc6 playedBy Inner241vbc6 {\n" +
			    "                public abstract String getValue();\n" +
			    "                String getValue() -> String getValue() with {\n" +
			    "                    result <- \"OK\"\n" +
			    "                }\n" +
			    "            }\n" +
			    "\n" +
			    "            public String getValue(Inner241vbc6 as Role241vbc6 obj) {\n" +
			    "                return obj.getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Inner241vbc6 o = new T241vbc6().new Inner241vbc6();\n" +
			    "        Inner241vbc6.Team241vbc6  t = o.new Team241vbc6();\n" +
			    " \n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(b)");
    }

    // a role class is played by another private inner class in the same enclosing class
    // 2.4.1-otjld-visible-base-class-7
    public void test241_visibleBaseClass7() {

       runConformTest(
            new String[] {
		"T241vbc7.java",
			    "\n" +
			    "public class T241vbc7 {\n" +
			    "    private class Inner241vbc7 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private team class Team241vbc7 {\n" +
			    "        public class Role241vbc7 playedBy Inner241vbc7 {\n" +
			    "            public abstract String getValue();\n" +
			    "            String getValue() -> String getValue() with {\n" +
			    "                result <- \"OK\"\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(Inner241vbc7 as Role241vbc7 obj) {\n" +
			    "            return obj.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Inner241vbc7 o = new T241vbc7().new Inner241vbc7();\n" +
			    "        Team241vbc7  t = new T241vbc7().new Team241vbc7();\n" +
			    " \n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by another public nested class in the same enclosing class
    // 2.4.1-otjld-visible-base-class-8
    public void test241_visibleBaseClass8() {

       runConformTest(
            new String[] {
		"T241vbc8.java",
			    "\n" +
			    "public class T241vbc8 {\n" +
			    "    public static class Nested241vbc8 {\n" +
			    "        public static class DeepNested241vbc8 {\n" +
			    "            public String getValue() {\n" +
			    "                return \"NOTOK\";\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private team class Team241vbc8 {\n" +
			    "        public class Role241vbc8 playedBy Nested241vbc8.DeepNested241vbc8 {\n" +
			    "            public abstract String getValue();\n" +
			    "            String getValue() -> String getValue() with {\n" +
			    "                result <- \"OK\"\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(Nested241vbc8.DeepNested241vbc8 as Role241vbc8 obj) {\n" +
			    "            return obj.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Nested241vbc8.DeepNested241vbc8 o = new Nested241vbc8.DeepNested241vbc8();\n" +
			    "        Team241vbc8                     t = new T241vbc8().new Team241vbc8();\n" +
			    " \n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by another protected inner class inherited in the same enclosing class
    // 2.4.1-otjld-visible-base-class-9
    public void test241_visibleBaseClass9() {

       runConformTest(
            new String[] {
		"T241vbc9_2.java",
			    "\n" +
			    "public class T241vbc9_2 extends T241vbc9_1 {\n" +
			    "    private team class Team241vbc9 {\n" +
			    "        public class Role241vbc9 playedBy Inner241vbc9 {\n" +
			    "            public abstract String getValue();\n" +
			    "            String getValue() -> String getValue() with {\n" +
			    "                result <- \"OK\"\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(Inner241vbc9 as Role241vbc9 obj) {\n" +
			    "            return obj.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Inner241vbc9 o = new T241vbc9_2().new Inner241vbc9();\n" +
			    "        Team241vbc9  t = new T241vbc9_2().new Team241vbc9();\n" +
			    " \n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T241vbc9_1.java",
			    "\n" +
			    "public class T241vbc9_1 {\n" +
			    "    protected class Inner241vbc9 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by another friendly nested class inherited in the same enclosing class
    // 2.4.1-otjld-visible-base-class-10
    public void test241_visibleBaseClass10() {

       runConformTest(
            new String[] {
		"T241vbc10_2.java",
			    "\n" +
			    "public class T241vbc10_2 extends T241vbc10_1 {\n" +
			    "    private team class Team241vbc10 {\n" +
			    "        public class Role241vbc10 playedBy Nested241vbc10 {\n" +
			    "            public abstract String getValue();\n" +
			    "            String getValue() -> String getValue() with {\n" +
			    "                result <- \"OK\"\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(Nested241vbc10 as Role241vbc10 obj) {\n" +
			    "            return obj.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Nested241vbc10 o = new Nested241vbc10();\n" +
			    "        Team241vbc10   t = new T241vbc10_2().new Team241vbc10();\n" +
			    " \n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T241vbc10_1.java",
			    "\n" +
			    "public class T241vbc10_1 {\n" +
			    "    static class Nested241vbc10 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by a protected inner class inherited from the explicit super class
    // 2.4.1-otjld-visible-base-class-11
    public void test241_visibleBaseClass11() {

       runConformTest(
            new String[] {
		"T241vbc11Main.java",
			    "\n" +
			    "public class T241vbc11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team241vbc11            t = new Team241vbc11();\n" +
			    "        T241vbc11.Inner241vbc11 o = new T241vbc11().new Inner241vbc11();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T241vbc11.java",
			    "\n" +
			    "public class T241vbc11 {\n" +
			    "    protected class Inner241vbc11 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team241vbc11.java",
			    "\n" +
			    "public team class Team241vbc11 {\n" +
			    "    public class Role241vbc11 extends T241vbc11 playedBy T241vbc11.Inner241vbc11 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T241vbc11.Inner241vbc11 as Role241vbc11 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by a protected inner class inherited from the explicit super class
    // 2.4.1-otjld-visible-base-class-12
    public void test241_visibleBaseClass12() {

       runConformTest(
            new String[] {
		"T241vbc12Main.java",
			    "\n" +
			    "public class T241vbc12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team241vbc12 t = new Team241vbc12();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T241vbc12.java",
			    "\n" +
			    "public class T241vbc12 {\n" +
			    "    protected class Inner241vbc12 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team241vbc12.java",
			    "\n" +
			    "public team class Team241vbc12 {\n" +
			    "    public class Role241vbc12_1 extends T241vbc12 {}\n" +
			    "\n" +
			    "    public class Role241vbc12_2 playedBy Role241vbc12_1.Inner241vbc12 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private String getValue(Role241vbc12_1.Inner241vbc12 as Role241vbc12_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return getValue(new Role241vbc12_1().new Inner241vbc12());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by a unresolved type - using declared lifting
    // 2.4.1-otjld-visible-base-class-12f
    public void test241_visibleBaseClass12f() {
        runNegativeTest(
            new String[] {
		"Team241vbc12f.java",
			    "\n" +
			    "public team class Team241vbc12f {\n" +
			    "    public class Role241vbc12f playedBy InvalidType {\n" +
			    "        protected String getValue() { return \"OK\"; }\n" +
			    "    }\n" +
			    "\n" +
			    "    private String getValue(InvalidType as Role241vbc12f obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
    		"1. ERROR in Team241vbc12f.java (at line 3)\n" +
    		"	public class Role241vbc12f playedBy InvalidType {\n" +
    		"	                                    ^^^^^^^^^^^\n" +
    		"InvalidType cannot be resolved to a type\n" +
    		"----------\n" +
    		"2. ERROR in Team241vbc12f.java (at line 7)\n" +
    		"	private String getValue(InvalidType as Role241vbc12f obj) {\n" +
    		"	                        ^^^^^^^^^^^\n" +
    		"InvalidType cannot be resolved to a type\n" +
    		"----------\n");
    }

    // a role class is played by another role class in the same team
    // 2.4.1-otjld-visible-base-class-13
    public void test241_visibleBaseClass13() {
        runNegativeTestMatching(
            new String[] {
		"Team241vbc13.java",
			    "\n" +
			    "public team class Team241vbc13 {\n" +
			    "    public class Role241vbc13_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role241vbc13_2 playedBy Role241vbc13_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(Role241vbc13_1 as Role241vbc13_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team241vbc13 t = new Team241vbc13();\n" +
			    "        Role241vbc13_1<@t> r = t.new Role241vbc13_1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(r));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(a)");
    }

    // a role class is played by an implicitly inherited role class in the same team
    // 2.4.1-otjld-visible-base-class-14
    public void test241_visibleBaseClass14() {
        runNegativeTestMatching(
            new String[] {
		"Team241vbc14_2.java",
			    "\n" +
			    "public team class Team241vbc14_2 extends Team241vbc14_1 {\n" +
			    "    public class Role241vbc14_2 playedBy Role241vbc14_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(Role241vbc14_1 as Role241vbc14_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team241vbc14_1.java",
			    "\n" +
			    "public team class Team241vbc14_1 {\n" +
			    "    protected class Role241vbc14_1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(a)");
    }

    // a role class is played by another team
    // 2.4.1-otjld-visible-base-class-15
    public void test241_visibleBaseClass15() {

       runConformTest(
            new String[] {
		"T241vbc15Main.java",
			    "\n" +
			    "public class T241vbc15Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team241vbc15_1 t1 = new Team241vbc15_1();\n" +
			    "        Team241vbc15_2 t2 = new Team241vbc15_2();\n" +
			    "\n" +
			    "        System.out.print(t2.getValue(t1));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team241vbc15_1.java",
			    "\n" +
			    "team class Team241vbc15_1 {\n" +
			    "    String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team241vbc15_2.java",
			    "\n" +
			    "public team class Team241vbc15_2 {\n" +
			    "    public class Role241vbc15_2 playedBy Team241vbc15_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(Team241vbc15_1 as Role241vbc15_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by a public externalized role - syntax error in role creation
    // 2.4.1-otjld-visible-base-class-16f
    public void test241_visibleBaseClass16f() {
        runNegativeTestMatching(
            new String[] {
		"Team241vbc16f_2.java",
			    "\n" +
			    "public team class Team241vbc16f_2 {\n" +
			    "    private final Team241vbc16f_1 t = new Team241vbc16f_1();\n" +
			    "\n" +
			    "    public class Role241vbc16f playedBy Role241vbc16f<@t> {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "    \n" +
			    "    private String getValue(Role241vbc16f<@t> as Role241vbc16f obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return getValue(new t.Role241vbc16f());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team241vbc16f_1.java",
			    "\n" +
			    "public team class Team241vbc16f_1 {\n" +
			    "    public class Role241vbc16f {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.2(h)");
    }

    // a role class is played by a public externalized role
    // 2.4.1-otjld-visible-base-class-16
    public void test241_visibleBaseClass16() {

       runConformTest(
            new String[] {
		"T241vbc16Main.java",
			    "\n" +
			    "public class T241vbc16Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team241vbc16_2 t = new Team241vbc16_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team241vbc16_1.java",
			    "\n" +
			    "public team class Team241vbc16_1 {\n" +
			    "    public class Role241vbc16 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team241vbc16_2.java",
			    "\n" +
			    "public team class Team241vbc16_2 {\n" +
			    "    private final Team241vbc16_1 t = new Team241vbc16_1();\n" +
			    "\n" +
			    "    public class Role241vbc16 playedBy Role241vbc16<@t> {\n" +
			    "        public abstract String getValue();\n" +
			    "        @SuppressWarnings(\"decapsulation\")\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private String getValue(Role241vbc16<@t> as Role241vbc16 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return getValue(t.new Role241vbc16());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // base class is non-static inner class - inner role created by lifting
    // 2.4.1-otjld-visible-base-class-17
    public void test241_visibleBaseClass17() {

       runConformTest(
            new String[] {
		"Team241vbc17.java",
			    "\n" +
			    "import base p.T241vbc17;\n" +
			    "import base p.T241vbc17.InnerBase;\n" +
			    "\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team241vbc17 {\n" +
			    "    protected team class RM playedBy T241vbc17 {\n" +
			    "        RM(T241vbc17 b) {\n" +
			    "            this.activate();\n" +
			    "        }\n" +
			    "        protected class RI playedBy InnerBase {\n" +
			    "            void print() { System.out.print(\"OK\"); }\n" +
			    "            print <- after test;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team241vbc17(T241vbc17 as RM r) { // lift and activate RM\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        p.T241vbc17 b = new p.T241vbc17();\n" +
			    "        new Team241vbc17(b);\n" +
			    "        b.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p/T241vbc17.java",
			    "\n" +
			    "package p;\n" +
			    "public class T241vbc17 {\n" +
			    "    class InnerBase {\n" +
			    "        void test() {\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test() {\n" +
			    "        new InnerBase().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // base class is non-static inner class - RM calls base constructor - inner role created by lifting
    // 2.4.1-otjld-visible-base-class-18
    public void test241_visibleBaseClass18() {

       runConformTest(
            new String[] {
		"Team241vbc18.java",
			    "\n" +
			    "import base p.T241vbc18;\n" +
			    "import base p.T241vbc18.InnerBase;\n" +
			    "\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team241vbc18 {\n" +
			    "    protected team class RM playedBy T241vbc18 {\n" +
			    "        protected RM() {\n" +
			    "            base();\n" +
			    "            this.activate();\n" +
			    "        }\n" +
			    "        protected class RI playedBy InnerBase {\n" +
			    "            void print() { System.out.print(\"OK\"); }\n" +
			    "            print <- after test;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    p.T241vbc18 getNewBase() {\n" +
			    "        return new RM(); // lowered\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team241vbc18 t = new Team241vbc18();\n" +
			    "        p.T241vbc18 b = t.getNewBase();\n" +
			    "        b.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p/T241vbc18.java",
			    "\n" +
			    "package p;\n" +
			    "public class T241vbc18 {\n" +
			    "    class InnerBase {\n" +
			    "        void test() {\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test() {\n" +
			    "        new InnerBase().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // base class is non-static inner class - RM calls base constructor - inner role created by lifting - ctor not visible
    // 2.4.1-otjld-visible-base-class-18e
    public void test241_visibleBaseClass18e() {
        runNegativeTestMatching(
            new String[] {
		"Team241vbc18e.java",
			    "\n" +
			    "import base p.T241vbc18e;\n" +
			    "import base p.T241vbc18e.InnerBase;\n" +
			    "\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team241vbc18e {\n" +
			    "    protected team class RM playedBy T241vbc18e {\n" +
			    "        RM() {\n" +
			    "            base();\n" +
			    "            this.activate();\n" +
			    "        }\n" +
			    "        protected class RI playedBy InnerBase {\n" +
			    "            void print() { System.out.print(\"OK\"); }\n" +
			    "            print <- after test;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    p.T241vbc18e getNewBase() {\n" +
			    "        return new RM(); // <-- report visibility problem here\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p/T241vbc18e.java",
			    "\n" +
			    "package p;\n" +
			    "public class T241vbc18e {\n" +
			    "    class InnerBase {\n" +
			    "        void test() {\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test() {\n" +
			    "        new InnerBase().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "not visible");
    }

    // base class is non-static inner class - inner base constructor called - inner role instantiated
    // 2.4.1-otjld-visible-base-class-19
    public void test241_visibleBaseClass19() {

       runConformTest(
            new String[] {
		"Team241vbc19.java",
			    "\n" +
			    "import base p.T241vbc19;\n" +
			    "import base p.T241vbc19.InnerBase;\n" +
			    "\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team241vbc19 {\n" +
			    "    protected team class RM playedBy T241vbc19 {\n" +
			    "        protected RM() {\n" +
			    "            base();\n" +
			    "            this.activate();\n" +
			    "        }\n" +
			    "        protected void test() {\n" +
			    "            RI ri = new RI();\n" +
			    "            ri.callTest();\n" +
			    "        }\n" +
			    "        protected class RI playedBy InnerBase {\n" +
			    "            protected RI() {\n" +
			    "                base();\n" +
			    "            }\n" +
			    "            void print() { System.out.print(\"OK\"); }\n" +
			    "            print <- after test;\n" +
			    "            protected abstract void callTest();\n" +
			    "	    void callTest() -> void test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team241vbc19 () {\n" +
			    "        RM rm = new RM();\n" +
			    "        rm.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team241vbc19();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p/T241vbc19.java",
			    "\n" +
			    "package p;\n" +
			    "public class T241vbc19 {\n" +
			    "    class InnerBase {\n" +
			    "        void test() {\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test() {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a non-visible base ctor is called using decapsulation
    // 2.4.1-otjld-visible-base-class-20
    public void test241_visibleBaseClass20() {

       runConformTest(
            new String[] {
		"Team241vbc20.java",
			    "\n" +
			    "import base p.T241vbc20;\n" +
			    "public team class Team241vbc20 {\n" +
			    "  protected class R playedBy T241vbc20 {\n" +
			    "    @SuppressWarnings(\"decapsulation\")\n" +
			    "    protected R(String v) {\n" +
			    "      base(v);\n" +
			    "    }\n" +
			    "    protected void print() {\n" +
			    "      System.out.print(getVal());\n" +
			    "    }\n" +
			    "    @SuppressWarnings(\"decapsulation\")\n" +
			    "    String getVal() -> get String val;\n" +
			    "  }\n" +
			    "  Team241vbc20 () {\n" +
			    "    R r= new R(\"OK\");\n" +
			    "    r.print();\n" +
			    "  }\n" +
			    "  public static void main(String[] args) {\n" +
			    "    new Team241vbc20();\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"p/T241vbc20.java",
			    "\n" +
			    "package p;\n" +
			    "public class T241vbc20 {\n" +
			    "  String val;\n" +
			    "  T241vbc20(String v) {\n" +
			    "    this.val= v;\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n"
            },
            "OK");
    }

    // a role class is played by a friendly top-level class in a different package
    // 2.4.2-otjld-inaccessible-base-class-1
    public void test242_inaccessibleBaseClass1() {
        runTestExpectingWarnings(
            new String[] {
		"p1/T242ibc1.java",
			    "\n" +
			    "package p1;\n" +
			    "class T242ibc1 {}\n" +
			    "    \n",
		"p2/Team242ibc1.java",
			    "\n" +
			    "package p2;\n" +
			    "import base p1.T242ibc1;\n" +
			    "public team class Team242ibc1 {\n" +
			    "    public class Role242ibc1 playedBy T242ibc1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
    		"1. WARNING in p2\\Team242ibc1.java (at line 3)\n" +
    		"	import base p1.T242ibc1;\n" +
    		"	            ^^^^^^^^^^^\n" +
    		"Overriding access restriction of base class p1.T242ibc1 (OTJLD 2.1.2(c)).\n" +
    		"----------\n");
    }

    // a role class is played by a private inner class in another class in the same package
    // 2.4.2-otjld-inaccessible-base-class-2
    public void test242_inaccessibleBaseClass2() {
        runTestExpectingWarnings(
            new String[] {
		"T242ibc2.java",
			    "\n" +
			    "public class T242ibc2 {\n" +
			    "    private class Inner242ibc2 {}\n" +
			    "}\n" +
			    "    \n",
		"Team242ibc2.java",
			    "\n" +
			    "public team class Team242ibc2 {\n" +
			    "    public class Role242ibc2 playedBy T242ibc2.Inner242ibc2 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
			"1. WARNING in Team242ibc2.java (at line 3)\n" +
			"	public class Role242ibc2 playedBy T242ibc2.Inner242ibc2 {}\n" +
			"	                                  ^^^^^^^^^^^^^^^^^^^^^\n" +
			"Overriding access restriction of base class T242ibc2.Inner242ibc2 (OTJLD 2.1.2(c)).\n" +
			"----------\n");
    }

    // a role class is played by a protected nested class in a different package
    // 2.4.2-otjld-inaccessible-base-class-3
    public void test242_inaccessibleBaseClass3() {
        runTestExpectingWarnings(
            new String[] {
		"p1/T242ibc3.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T242ibc3 {\n" +
			    "    protected static class Nested242ibc3 {}\n" +
			    "}\n" +
			    "    \n",
		"p2/Team242ibc3.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.T242ibc3;\n" +
			    "\n" +
			    "public team class Team242ibc3 {\n" +
			    "    public class Role242ibc3 playedBy T242ibc3.Nested242ibc3 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
			"1. WARNING in p2\\Team242ibc3.java (at line 6)\n" +
			"	public class Role242ibc3 playedBy T242ibc3.Nested242ibc3 {}\n" +
			"	                                  ^^^^^^^^^^^^^^^^^^^^^^\n" +
			"Overriding access restriction of base class p1.T242ibc3.Nested242ibc3 (OTJLD 2.1.2(c)).\n" +
			"----------\n");
    }

    // a role class is played by a friendly inner class in a different package
    // 2.4.2-otjld-inaccessible-base-class-4
    public void test242_inaccessibleBaseClass4() {
        runTestExpectingWarnings(
            new String[] {
		"p1/T242ibc4.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T242ibc4 {\n" +
			    "    class Inner242ibc4 {}\n" +
			    "}\n" +
			    "    \n",
		"p2/Team242ibc4.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.T242ibc4;\n" +
			    "\n" +
			    "public team class Team242ibc4 {\n" +
			    "    public class Role242ibc4 playedBy T242ibc4.Inner242ibc4 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
			"1. WARNING in p2\\Team242ibc4.java (at line 6)\n" +
			"	public class Role242ibc4 playedBy T242ibc4.Inner242ibc4 {}\n" +
			"	                                  ^^^^^^^^^^^^^^^^^^^^^\n" +
			"Overriding access restriction of base class p1.T242ibc4.Inner242ibc4 (OTJLD 2.1.2(c)).\n" +
			"----------\n");
    }

    // a role class is played by a private nested class in a different package
    // 2.4.2-otjld-inaccessible-base-class-5
    public void test242_inaccessibleBaseClass5() {
        runTestExpectingWarnings(
            new String[] {
		"p1/T242ibc5.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T242ibc5 {\n" +
			    "    private static class Nested242ibc5 {}\n" +
			    "}\n" +
			    "    \n",
		"p2/Team242ibc5.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.T242ibc5;\n" +
			    "public team class Team242ibc5 {\n" +
			    "    public class Role242ibc5 playedBy T242ibc5.Nested242ibc5 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
			"1. WARNING in p2\\Team242ibc5.java (at line 5)\n" +
			"	public class Role242ibc5 playedBy T242ibc5.Nested242ibc5 {}\n" +
			"	                                  ^^^^^^^^^^^^^^^^^^^^^^\n" +
			"Overriding access restriction of base class p1.T242ibc5.Nested242ibc5 (OTJLD 2.1.2(c)).\n" +
			"----------\n");
    }

    // a role class is played by a private inner class 'inherited' by the enclosing class
    // 2.4.2-otjld-inaccessible-base-class-6
    public void test242_inaccessibleBaseClass6() {
        runNegativeTestMatching(
            new String[] {
		"T242ibc6_2.java",
			    "\n" +
			    "public class T242ibc6_2 extends T242ibc6_1 {\n" +
			    "    public team class Team242ibc6 {\n" +
			    "        public class Role242ibc6 playedBy Inner242ibc6 {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T242ibc6_1.java",
			    "\n" +
			    "public class T242ibc6_1 {\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    private class Inner242ibc6 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "visib");
    }

    // a role class is played by a friendly nested class 'inherited' by the enclosing class from another package
    // 2.4.2-otjld-inaccessible-base-class-7
    public void test242_inaccessibleBaseClass7() {
        runNegativeTestMatching(
            new String[] {
		"p2/T242ibc7_2.java",
			    "\n" +
			    "package p2;\n" +
			    "public class T242ibc7_2 extends p1.T242ibc7_1 {\n" +
			    "    public team class Team242ibc7 {\n" +
			    "        public class Role242ibc7 playedBy Nested242ibc7 {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T242ibc7_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T242ibc7_1 {\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    private class Nested242ibc7 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "visib");
    }

    // a role class is played by a public inner class of the role
    // 2.4.2-otjld-inaccessible-base-class-8
    public void test242_inaccessibleBaseClass8() {
        runNegativeTestMatching(
            new String[] {
		"Team242ibc8.java",
			    "\n" +
			    "public team class Team242ibc8 {\n" +
			    "    public team class Role242ibc8 playedBy Inner242ibc8 {\n" +
			    "        public class Inner242ibc8 {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(b)");
    }

    // a role class is played by a friendly inner class 'inherited' by the role from its normal superclass in another package
    // 2.4.2-otjld-inaccessible-base-class-9
    public void test242_inaccessibleBaseClass9() {
        runNegativeTestMatching(
            new String[] {
		"p2/Team242ibc9.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team242ibc9 {\n" +
			    "    public class Role242ibc9 extends p1.T242ibc9 playedBy Inner242ibc9 {}\n" +
			    "}\n" +
			    "    \n",
		"p1/T242ibc9.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T242ibc9 {\n" +
			    "    class Inner242ibc9 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "visible");
    }

    // a role class is played by a private nested class 'inherited' by the role from its normal superclass
    // 2.4.2-otjld-inaccessible-base-class-10
    public void test242_inaccessibleBaseClass10() {
        runNegativeTestMatching(
            new String[] {
		"Team242ibc10.java",
			    "\n" +
			    "public team class Team242ibc10 {\n" +
			    "    public class Role242ibc10 extends T242ibc10 playedBy Nested242ibc10 {}\n" +
			    "}\n" +
			    "    \n",
		"T242ibc10.java",
			    "\n" +
			    "public class T242ibc10 {\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    private static class Nested242ibc10 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "not visible");
    }

    // a role class is played by a (illegally) friendly role 'inherited' by the enclosing team from a team in another package
    // 2.4.2-otjld-inaccessible-base-class-11
    public void test242_inaccessibleBaseClass11() {
        runNegativeTestMatching(
            new String[] {
		"p2/Team242ibc11_2.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team242ibc11_2 extends p1.Team242ibc11_1 {\n" +
			    "    public class Role242ibc11_2 playedBy Role242ibc11_1 {}\n" +
			    "}\n" +
			    "    \n",
		"p1/Team242ibc11_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team242ibc11_1 {\n" +
			    "    class Role242ibc11_1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(a)");
    }

    // a role class is played by a protected role 'inherited' by the enclosing team
    // 2.4.2-otjld-inaccessible-base-class-12
    public void test242_inaccessibleBaseClass12() {
        runNegativeTestMatching(
            new String[] {
		"Team242ibc12_2.java",
			    "\n" +
			    "public team class Team242ibc12_2 extends Team242ibc12_1 {\n" +
			    "    public class Role242ibc12_2 playedBy Role242ibc12_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team242ibc12_1.java",
			    "\n" +
			    "public team class Team242ibc12_1 {\n" +
			    "    protected class Role242ibc12_1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(a)");
    }

    // a role class is played by a protected externalized role in another package
    // 2.4.2-otjld-inaccessible-base-class-13
    public void test242_inaccessibleBaseClass13() {
        runTestExpectingWarnings(
            new String[] {
		"p1/Team242ibc13_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team242ibc13_1 {\n" +
			    "    protected class Role242ibc13_1 {}\n" +
			    "}\n" +
			    "    \n",
		"p2/Team242ibc13_2.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team242ibc13_2 extends p1.Team242ibc13_1 {\n" +
			    "    private final p1.Team242ibc13_1 t = new p1.Team242ibc13_1();\n" +
			    "    public class Role242ibc13_2 playedBy Role242ibc13_1<@t> {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
			"1. WARNING in p2\\Team242ibc13_2.java (at line 5)\n" +
			"	public class Role242ibc13_2 playedBy Role242ibc13_1<@t> {}\n" +
			"	                                     ^^^^^^^^^^^^^^\n" +
			"Overriding access restriction of base class Role242ibc13_1<@t> (OTJLD 2.1.2(c)).\n" +
			"----------\n");
    }

    // a role class is played by a confined externalized role in another package
    // 2.4.2-otjld-inaccessible-base-class-13c
    public void test242_inaccessibleBaseClass13c() {
        runNegativeTest(
            new String[] {
		"p2/Team242ibc13c_2.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team242ibc13c_2 extends p1.Team242ibc13c_1 {\n" +
			    "    private final p1.Team242ibc13c_1 t = new p1.Team242ibc13c_1();\n" +
			    "    public class Role242ibc13c_2 playedBy Role242ibc13c_1<@t> {}\n" +
			    "}\n" +
			    "    \n",
		"p1/Team242ibc13c_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team242ibc13c_1 {\n" +
			    "    protected class Role242ibc13c_1 extends Confined {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
    		"1. ERROR in p2\\Team242ibc13c_2.java (at line 5)\n" +
    		"	public class Role242ibc13c_2 playedBy Role242ibc13c_1<@t> {}\n" +
    		"	                                      ^^^^^^^^^^^^^^^\n" +
    		"Trying to override access restriction of confined base class Role242ibc13c_1<@t> (OTJLD 2.1.2(c)).\n" +
    		"----------\n");
    }

    // a role class is played by a protected externalized role in another package
    // 2.4.2-otjld-inaccessible-base-class-14
    public void test242_inaccessibleBaseClass14() {

       runConformTest(
            new String[] {
		"p2/Team242ibc14_2.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team242ibc14_2 extends p1.Team242ibc14_1 {\n" +
			    "    private final p1.Team242ibc14_1 t = new p1.Team242ibc14_1();\n" +
			    "    @SuppressWarnings(\"decapsulation\")\n" +
			    "    public class Role242ibc14_2 playedBy Role242ibc14_1<@t> {\n" +
			    "    	void m2(){ System.out.print(\"K\"); }\n" +
			    "    	m2 <- after m1;\n" +
			    "    }    \n" +
			    "    public static void main(String[] args) {\n" +
			    "    	Team242ibc14_2 o = new Team242ibc14_2();\n" +
			    "    	o.activate();\n" +
			    "    	o.t.create();\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n",
		"p1/Team242ibc14_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team242ibc14_1 {\n" +
			    "    protected class Role242ibc14_1 {\n" +
			    "    	protected void m1(){ System.out.print(\"O\"); }\n" +
			    "    }    \n" +
			    "    public void create(){\n" +
			    "    	Role242ibc14_1 r = new Role242ibc14_1();\n" +
			    "    	r.m1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is played by a private inner class, reported by kmeier
    // 2.4.2-otjld-inaccessible-base-class-15
    public void test242_inaccessibleBaseClass15() {

       runConformTest(
            new String[] {
		"Team242ibc15.java",
			    "\n" +
			    "public team class Team242ibc15 {\n" +
			    "        @SuppressWarnings(\"decapsulation\")\n" +
			    "        public class TestRole playedBy T242ibc15.InnerTarget {\n" +
			    "                private abstract int getX();\n" +
			    "                getX -> get x;\n" +
			    "                void test() {\n" +
			    "                    System.out.print(getX());\n" +
			    "                }\n" +
			    "                test <- after doSomething;\n" +
			    "        }\n" +
			    "        public static void main(String[] args) {\n" +
			    "            new Team242ibc15().activate();\n" +
			    "            new T242ibc15().doSomething();\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n",
		"T242ibc15.java",
			    "\n" +
			    "public class T242ibc15 {\n" +
			    "        private class InnerTarget {\n" +
			    "                @SuppressWarnings(\"unused\")\n" +
			    "                private int x;\n" +
			    "                public void doSomething() {\n" +
			    "                        x = 42;\n" +
			    "                }\n" +
			    "        }\n" +
			    "        public void doSomething() {\n" +
			    "                new InnerTarget().doSomething();\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n"
            },
            "42");
    }

    // role playedBy public inner, accessing list of role - reported by A.Werner
    // 2.4.2-otjld-inaccessible-base-class-16
    public void test242_inaccessibleBaseClass16() {

       runConformTest(
            new String[] {
		"Team242ibc16.java",
			    "\n" +
			    "import java.util.List;\n" +
			    "@SuppressWarnings({\"unchecked\",\"rawtypes\"})\n" +
			    "public team class Team242ibc16 {\n" +
			    "    @SuppressWarnings(\"decapsulation\")\n" +
			    "    protected team class RoleClass playedBy T242ibc16 {\n" +
			    "        protected RoleClass() {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        protected abstract void test();\n" +
			    "        test -> test;\n" +
			    "\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin void addInner() {\n" +
			    "                getList().add(new InnerRole(\"OK\"));\n" +
			    "        }\n" +
			    "        addInner <- replace addInner;\n" +
			    "\n" +
			    "        List<T242ibc16.InnerBase> getList() -> get List list;\n" +
			    "\n" +
			    "        protected class InnerRole playedBy T242ibc16.InnerBase {\n" +
			    "            protected InnerRole(String s) {\n" +
			    "                base(s);\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    Team242ibc16 () {\n" +
			    "        this.activate();\n" +
			    "        RoleClass rm = new RoleClass();\n" +
			    "        rm.test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team242ibc16();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T242ibc16.java",
			    "\n" +
			    "import java.util.List;\n" +
			    "import java.util.ArrayList;\n" +
			    "import java.util.Iterator;\n" +
			    "\n" +
			    "@SuppressWarnings({\"unchecked\",\"rawtypes\"})\n" +
			    "public class T242ibc16 {\n" +
			    "    private List list = new ArrayList();\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "                addInner();\n" +
			    "                printInner();\n" +
			    "    }\n" +
			    "    private void addInner() {\n" +
			    "            list.add(new InnerBase(\"ERROR\"));\n" +
			    "    }\n" +
			    "    private void printInner(){\n" +
			    "        for (Iterator i = list.iterator(); i.hasNext();) {\n" +
			    "                InnerBase element = (InnerBase) i.next();\n" +
			    "                element.print();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class InnerBase {\n" +
			    "        String name;\n" +
			    "\n" +
			    "        protected InnerBase(String name) {\n" +
			    "                this.name = name; \n" +
			    "        }\n" +
			    "\n" +
			    "        public void print() {\n" +
			    "                System.out.print(name);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role playedBy private inner, accessing list of role - reported by A.Werner
    // 2.4.2-otjld-inaccessible-base-class-17
    public void test242_inaccessibleBaseClass17() {

       runConformTest(
            new String[] {
		"Team242ibc17.java",
			    "\n" +
			    "import java.util.List;\n" +
			    "@SuppressWarnings(\"rawtypes\")\n" +
			    "public team class Team242ibc17 {\n" +
			    "    @SuppressWarnings(\"decapsulation\")\n" +
			    "    protected team class RoleClass playedBy T242ibc17 {\n" +
			    "        protected RoleClass() {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        protected abstract void test();\n" +
			    "        test -> test;\n" +
			    "\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin void addInner() {\n" +
			    "                addToList(new InnerRole(\"OK\"));\n" +
			    "        }\n" +
			    "        addInner <- replace addInner;\n" +
			    "\n" +
			    "        boolean addToList(InnerRole r) -> get List list \n" +
			    "            with { result <- added((List<T242ibc17.InnerBase>)result, r) }\n" +
			    "\n" +
			    "        protected class InnerRole playedBy T242ibc17.InnerBase {\n" +
			    "            protected InnerRole(String s) {\n" +
			    "                base(s);\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public <T> boolean added(List<T> l, T e) {\n" +
			    "            l.add(e);\n" +
			    "            return true;\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    Team242ibc17 () {\n" +
			    "        this.activate();\n" +
			    "        RoleClass rm = new RoleClass();\n" +
			    "        rm.test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team242ibc17();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T242ibc17.java",
			    "\n" +
			    "import java.util.List;\n" +
			    "import java.util.ArrayList;\n" +
			    "import java.util.Iterator;\n" +
			    "\n" +
			    "@SuppressWarnings({\"unchecked\",\"rawtypes\"})\n" +
			    "public class T242ibc17 {\n" +
			    "    private List list = new ArrayList();\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "                addInner();\n" +
			    "                printInner();\n" +
			    "    }\n" +
			    "    private void addInner() {\n" +
			    "            list.add(new InnerBase(\"ERROR\"));\n" +
			    "    }\n" +
			    "    private void printInner(){\n" +
			    "        for (Iterator i = list.iterator(); i.hasNext();) {\n" +
			    "                InnerBase element = (InnerBase) i.next();\n" +
			    "                element.print();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private class InnerBase {\n" +
			    "        String name;\n" +
			    "\n" +
			    "        protected InnerBase(String name) {\n" +
			    "                this.name = name; \n" +
			    "        }\n" +
			    "\n" +
			    "        public void print() {\n" +
			    "                System.out.print(name);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role playedBy private inner, accessing list of role - workaround using ILowerable - reported by A.Werner
    // 2.4.2-otjld-inaccessible-base-class-18
    public void test242_inaccessibleBaseClass18() {

       runConformTest(
            new String[] {
		"Team242ibc18.java",
			    "\n" +
			    "import java.util.List;\n" +
			    "public team class Team242ibc18 {\n" +
			    "    @SuppressWarnings(\"decapsulation\")\n" +
			    "    protected team class RoleClass playedBy T242ibc18 {\n" +
			    "        protected RoleClass() {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        protected abstract void test();\n" +
			    "        test -> test;\n" +
			    "\n" +
			    "        @SuppressWarnings({ \"basecall\", \"unchecked\" })\n" +
			    "        callin void addInner() {\n" +
			    "                getList().add((new InnerRole(\"OK\")).lower());\n" +
			    "        }\n" +
			    "        addInner <- replace addInner;\n" +
			    "\n" +
			    "        @SuppressWarnings(\"rawtypes\")\n" +
			    "        List getList() -> get List list;\n" +
			    "\n" +
			    "        protected class InnerRole implements ILowerable playedBy T242ibc18.InnerBase {\n" +
			    "            protected InnerRole(String s) {\n" +
			    "                base(s);\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    Team242ibc18 () {\n" +
			    "        this.activate();\n" +
			    "        RoleClass rm = new RoleClass();\n" +
			    "        rm.test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team242ibc18();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T242ibc18.java",
			    "\n" +
			    "import java.util.List;\n" +
			    "import java.util.ArrayList;\n" +
			    "import java.util.Iterator;\n" +
			    "\n" +
			    "@SuppressWarnings({\"unchecked\",\"rawtypes\"})\n" +
			    "public class T242ibc18 {\n" +
			    "    private List list = new ArrayList();\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "                addInner();\n" +
			    "                printInner();\n" +
			    "    }\n" +
			    "    private void addInner() {\n" +
			    "            list.add(new InnerBase(\"ERROR\"));\n" +
			    "    }\n" +
			    "    private void printInner(){\n" +
			    "        for (Iterator i = list.iterator(); i.hasNext();) {\n" +
			    "                InnerBase element = (InnerBase) i.next();\n" +
			    "                element.print();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private class InnerBase {\n" +
			    "        String name;\n" +
			    "\n" +
			    "        protected InnerBase(String name) {\n" +
			    "                this.name = name; \n" +
			    "        }\n" +
			    "\n" +
			    "        public void print() {\n" +
			    "                System.out.print(name);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role nested in a role file is bound to a private static inner class
    // sub-role reported bogus visibility problem re phantom role
    public void test242_inaccessibleBaseClass19() {
    	runConformTest(
    		new String[] {
    	"Team242iabc19.java",
				"import base p242iabc19.T242iabc19.Inner;\n" +
				"\n" +
				"@SuppressWarnings(\"decapsulation\")\n" +
				"public team class Team242iabc19 {\n" +
				"	void test() {\n" +
				"		new Mid2().test();\n" +
				"	}\n" +
				"	public static void main(String[] args) {\n" +
				"		new Team242iabc19().test();\n" +
				"	}\n" +
				"}\n",
    	"Team242iabc19/Mid1.java",
				"team package Team242iabc19;\n" +
				"\n" +
				"protected team class Mid1 {\n" +
				"	protected class Role playedBy Inner {\n" +
				"		@SuppressWarnings(\"decapsulation\")\n" +
				"		protected Role() { base(); }\n" +
				"		@SuppressWarnings(\"decapsulation\")\n" +
				"		protected void test() -> void test();\n" +
				"	}\n" +
				"}\n",
    	"Team242iabc19/Mid2.java",
		    	"team package Team242iabc19;\n" +
				"\n" +
				"protected team class Mid2 extends Mid1 {\n" +
				"	protected void test() {\n" +
				"		new Role().test();\n" +
				"	}\n" +
				"}\n",
    	"p242ibc19/T242iabc19.java",
		    	"package p242iabc19;\n" +
				"\n" +
				"public class T242iabc19 {\n" +
				"	@SuppressWarnings(\"unused\")\n" +
				"	private static class Inner {\n" +
				"		void test() {\n" +
				"			System.out.print(\"OK\");\n" +
				"		}\n" +
				"	}\n" +
				"}\n"
    		},
    		"OK");
    }

    // a role nested in a role file is bound to a private static inner class
    // sub-role reported bogus visibility problem - inner role is materialized
    public void test242_inaccessibleBaseClass20() {
    	runConformTest(
    		new String[] {
    	"Team242iabc20.java",
				"import base p242iabc20.T242iabc20.Inner;\n" +
				"\n" +
				"@SuppressWarnings(\"decapsulation\")\n" +
				"public team class Team242iabc20 {\n" +
				"	void test() {\n" +
				"		new Mid2().test();\n" +
				"	}\n" +
				"	public static void main(String[] args) {\n" +
				"		new Team242iabc20().test();\n" +
				"	}\n" +
				"}\n",
    	"Team242iabc20/Mid1.java",
				"team package Team242iabc20;\n" +
				"\n" +
				"protected team class Mid1 {\n" +
				"	protected class Role playedBy Inner {\n" +
				"		@SuppressWarnings(\"decapsulation\")\n" +
				"		protected Role() { base(); }\n" +
				"		@SuppressWarnings(\"decapsulation\")\n" +
				"		protected void test() -> void test();\n" +
				"	}\n" +
				"}\n",
    	"Team242iabc20/Mid2.java",
		    	"team package Team242iabc20;\n" +
				"\n" +
				"protected team class Mid2 extends Mid1 {\n" +
				"   @Override\n" +
				"	protected class Role playedBy Inner {}\n" +
				"	protected void test() {\n" +
				"		new Role().test();\n" +
				"	}\n" +
				"}\n",
    	"p242ibc19/T242iabc20.java",
		    	"package p242iabc20;\n" +
				"\n" +
				"public class T242iabc20 {\n" +
				"	@SuppressWarnings(\"unused\")\n" +
				"	private static class Inner {\n" +
				"		void test() {\n" +
				"			System.out.print(\"OK\");\n" +
				"		}\n" +
				"	}\n" +
				"}\n"
    		},
    		"OK");
    }

    // a role class is played by a base class that has the same short name
    // 2.4.3-otjld-baseclass-has-same-shortname-1
    public void test243_baseclassHasSameShortname1() {
        runNegativeTestMatching(
            new String[] {
		"p2/Team243bhss1.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.*;\n" +
			    "public team class Team243bhss1 {\n" +
			    "    public class T243bhss1 playedBy T243bhss1 {}\n" +
			    "}\n" +
			    "    \n",
		"p1/T243bhss1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T243bhss1 {}\n" +
			    "    \n"
            },
            "2.1.2(a)");
    }

    // a role class is played by a base class that has the same short name as another role in the same team
    // 2.4.3-otjld-baseclass-has-same-shortname-2
    public void test243_baseclassHasSameShortname2() {
        runNegativeTestMatching(
            new String[] {
		"p2/Team243bhss2_2.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.*;\n" +
			    "public team class Team243bhss2_2 extends Team243bhss2_1 {\n" +
			    "    // the T243bhss2 specifies the inherited role, not the imported class\n" +
			    "    public class Role243bhss2 playedBy T243bhss2 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T243bhss2.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T243bhss2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p2/Team243bhss2_1.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team243bhss2_1 {\n" +
			    "    public class T243bhss2 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(a)");
    }

    // a role class is played by a base class that has the same short name as another role in the same team
    // 2.4.3-otjld-baseclass-has-same-shortname-3
    public void test243_baseclassHasSameShortname3() {
        runNegativeTestMatching(
            new String[] {
		"p2/Team243bhss3_2.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.T243bhss3;\n" +
			    "public team class Team243bhss3_2 extends Team243bhss3_1 {\n" +
			    "    // this should not compile as the short name is ambiguous because of the single-type import (see JLS 8.5)\n" +
			    "    public class Role243bhss3 playedBy T243bhss3 {}\n" +
			    "}\n" +
			    "    \n",
		"p1/T243bhss3.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T243bhss3 {}\n" +
			    "    \n",
		"p2/Team243bhss3_1.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team243bhss3_1 {\n" +
			    "    public class T243bhss3 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(a)");
    }

    // a role class is played by a public top-level class of the same short name in a different package, and the fully qualified name is used in the playedBy
    // 2.4.6-otjld-baseclass-has-same-shortname
    public void test246_baseclassHasSameShortname() {

       runConformTest(
            new String[] {
		"T246bhssMain.java",
			    "\n" +
			    "public class T246bhssMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        p2.Team246bhss t = new p2.Team246bhss();\n" +
			    "        p1.T246bhss    o = new p1.T246bhss();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T246bhss.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T246bhss {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p2/Team246bhss.java",
			    "\n" +
			    "package p2;\n" +
			    "import base p1.T246bhss;\n" +
			    "public team class Team246bhss {\n" +
			    "    public class T246bhss playedBy T246bhss {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(p1.T246bhss as T246bhss obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role and its base have the same name, base import used, callin binding declared
    // 2.4.6-otjld-baseclass-has-same-shortname-2
    public void test246_baseclassHasSameShortname2() {

       runConformTest(
            new String[] {
		"Team246bhss2.java",
			    "\n" +
			    "import base p1.T246bhss2;\n" +
			    "public team class Team246bhss2 {\n" +
			    "    protected class T246bhss2 playedBy T246bhss2 {\n" +
			    "        void print(T246bhss2 other) {\n" +
			    "            other.dosomething();\n" +
			    "        }\n" +
			    "        @SuppressWarnings(\"decapsulation\")\n" +
			    "        void dosomething() -> void dosomething();\n" +
			    "        void print(T246bhss2 other) <- after T246bhss2 getOther()\n" +
			    "            with { other <- result }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team246bhss2().activate();\n" +
			    "        new p1.T246bhss2().getOther();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T246bhss2.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T246bhss2 {\n" +
			    "    void dosomething() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "    public T246bhss2 getOther () {\n" +
			    "        System.out.print(\"O\");\n" +
			    "        return this;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a base guard uses a base imported class
    // 2.4.7-otjld-base-import-scope-1
    public void test247_baseImportScope1() {

       runConformTest(
            new String[] {
		"pBase/T247bisMain1.java",
			    "\n" +
			    "package pBase;\n" +
			    "public class T247bisMain1 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new pTeam.Team247bis1().activate();\n" +
			    "        new T247bis1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"pBase/T247bis1.java",
			    "\n" +
			    "package pBase;\n" +
			    "class T247bis1 {\n" +
			    "    void test() {};\n" +
			    "}\n" +
			    "    \n",
		"pTeam/Team247bis1.java",
			    "\n" +
			    "package pTeam;\n" +
			    "import base pBase.T247bis1;\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team247bis1 {\n" +
			    "    protected class T247bis1 playedBy T247bis1 \n" +
			    "        base when (base.getClass() == T247bis1.class)\n" +
			    "    {\n" +
			    "        void print() { \n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "        print <- after test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a parameter mapping (callout) uses the base import scope
    // 2.4.7-otjld-base-import-scope-2
    public void test247_baseImportScope2() {

       runConformTest(
            new String[] {
		"pTeam/Team247bis2.java",
			    "\n" +
			    "package pTeam;\n" +
			    "import base pBase.T247bis2;\n" +
			    "\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team247bis2 {\n" +
			    "    protected class R playedBy T247bis2 {\n" +
			    "        protected R() { base(\"X\"); }\n" +
			    "        protected abstract String getTest();\n" +
			    "	String getTest() -> T247bis2 test() \n" +
			    "        with {\n" +
			    "            result <- ((result instanceof T247bis2) \n" +
			    "                        ? result.val\n" +
			    "                        : \"NO\")\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team247bis2() {\n" +
			    "        R r = new R();\n" +
			    "        System.out.print(r.getTest()+\"K\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team247bis2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"pBase/T247bis1.java",
			    "\n" +
			    "package pBase;\n" +
			    "class T247bis2 {\n" +
			    "    public String val;\n" +
			    "    T247bis2(String v) { this.val = v; }\n" +
			    "    T247bis2 test() {\n" +
			    "        return new T247bis2(\"O\");\n" +
			    "    };\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team attempts to base-import a whole package
    // 2.4.7-otjld-base-import-scope-3
    public void test247_baseImportScope3() {
        runNegativeTestMatching(
            new String[] {
		"p2/Team247bis3.java",
			    "\n" +
			    "package p2;\n" +
			    "import base p1.*;\n" +
			    "public team class Team247bis3 {}\n" +
			    "    \n",
		"p1/T247bis3_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T247bis3_1 {}\n" +
			    "    \n"
            },
            "Syntax error");
    }

    // a team uses a base-imported class as a return type for one of its methods
    public void test247_baseImportScope4() {
        runConformTest(
            new String[] {
        "T247bis4Main.java",
        		"public class T247bis4Main {\n" +
        		"    public static void main(String... args) {\n" +
        		"         System.out.print(new p2.Team247bis4().getR());\n" +
        		"    }\n" +
        		"}\n",
		"p2/Team247bis4.java",
			    "package p2;\n" +
			    "import base p1.T247bis4_1;\n" +
			    "public team class Team247bis4 {\n" +
			    "    protected class R playedBy T247bis4_1 {\n" +
			    "        protected R() {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public T247bis4_1 getR() {\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "}\n",
		"p1/T247bis4_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T247bis4_1 {\n" +
			    "    @Override public String toString() { return \"Base\"; }\n" +
			    "}\n"
            },
            "Base");
    }
    // resolving team method return type prefers role over base class
    public void test247_baseImportScope5() {
        runConformTest(
            new String[] {
        "T247bis5Main.java",
        		"public class T247bis5Main {\n" +
        		"    public static void main(String... args) {\n" +
        		"         new p2.Team247bis5().test();\n" +
        		"    }\n" +
        		"}\n",
		"p2/Team247bis5.java",
			    "package p2;\n" +
			    "import base p1.T247bis5_1;\n" +
			    "public team class Team247bis5 {\n" +
			    "    protected class T247bis5_1 playedBy T247bis5_1 {\n" +
			    "         protected T247bis5_1() { base(); }\n" +
			    "         @Override public String toString() { return \"Role\"; }\n" +
			    "    }\n" +
			    "    T247bis5_1 getR() {\n" +
			    "        return new T247bis5_1();\n" +
			    "    }\n" +
			    "    public void test() {\n" +
			    "         System.out.print(getR());\n" +
			    "    }\n" +
			    "}\n",
		"p1/T247bis5_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T247bis5_1 {\n" +
			    "    @Override public String toString() { return \"Base\"; }\n" +
			    "}\n"
            },
            "Role");
    }

    // a team uses a base-imported class as a return type for one of its methods
    // also: Bug 372391 - [compiler] creating bound role in field declaration throws NPE on role cache
    public void test247_baseImportScope6() {
        runConformTest(
            new String[] {
        "T247bis6Main.java",
        		"public class T247bis6Main {\n" +
        		"    public static void main(String... args) {\n" +
        		"         System.out.print(new p2.Team247bis6().getR());\n" +
        		"    }\n" +
        		"}\n",
		"p2/Team247bis6.java",
			    "package p2;\n" +
			    "import base p1.T247bis6_1;\n" +
			    "public team class Team247bis6 {\n" +
			    "    protected class R playedBy T247bis6_1 {\n" +
			    "        protected R() {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    R r = new R();\n" +
			    "    public T247bis6_1 getR() {\n" +
			    "        return this.r;\n" +
			    "    }\n" +
			    "}\n",
		"p1/T247bis6_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T247bis6_1 {\n" +
			    "    @Override public String toString() { return \"Base\"; }\n" +
			    "}\n"
            },
            "Base");
    }
}
