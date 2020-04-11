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
package org.eclipse.objectteams.otdt.tests.otjld.rolesandteams;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

public class Covariance extends AbstractOTJLDTest {

	public Covariance(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test0c11_implicitlyInheritingStaticRoleMethod1"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return Covariance.class;
	}

    // a method uses a redefined role as a parameter
    // 6.3.1-otjld-redefined-role-as-paramerer-1
    public void test631_redefinedRoleAsParamerer1() {

       runConformTest(
            new String[] {
		"T631rrap1Main.java",
			    "\n" +
			    "public class T631rrap1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team631rrap1_2 t = new Team631rrap1_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap1_1.java",
			    "\n" +
			    "public team class Team631rrap1_1 {\n" +
			    "    public class Role631rrap1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap1_2.java",
			    "\n" +
			    "public team class Team631rrap1_2 extends Team631rrap1_1 {\n" +
			    "    public class Role631rrap1_1 {\n" +
			    "        public String toString() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role631rrap1_2 {\n" +
			    "        public Role631rrap1_1 getRole() {\n" +
			    "            return new Role631rrap1_1();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role631rrap1_2().getRole().toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method uses a redefined role as a parameter
    // 6.3.1-otjld-redefined-role-as-paramerer-2
    public void test631_redefinedRoleAsParamerer2() {

       runConformTest(
            new String[] {
		"T631rrap2Main.java",
			    "\n" +
			    "public class T631rrap2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team631rrap2_2 t = new Team631rrap2_2();\n" +
			    "        T631rrap2      o = new T631rrap2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T631rrap2.java",
			    "\n" +
			    "public class T631rrap2 {}\n" +
			    "    \n",
		"Team631rrap2_1.java",
			    "\n" +
			    "public team class Team631rrap2_1 {\n" +
			    "    public class Role631rrap2 playedBy T631rrap2 {\n" +
			    "        public String toString() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap2_2.java",
			    "\n" +
			    "public team class Team631rrap2_2 extends Team631rrap2_1 {\n" +
			    "    public class Role631rrap2 {\n" +
			    "        public String toString() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T631rrap2 as Role631rrap2 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method uses a redefined role as a parameter
    // 6.3.1-otjld-redefined-role-as-paramerer-3
    public void test631_redefinedRoleAsParamerer3() {

       runConformTest(
            new String[] {
		"T631rrap3Main.java",
			    "\n" +
			    "public class T631rrap3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team631rrap3_2 t = new Team631rrap3_2();\n" +
			    "        T631rrap3      o = new T631rrap3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T631rrap3.java",
			    "\n" +
			    "public class T631rrap3 {}\n" +
			    "    \n",
		"Team631rrap3_1.java",
			    "\n" +
			    "public team class Team631rrap3_1 {\n" +
			    "    public class Role631rrap3 playedBy T631rrap3 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T631rrap3 as Role631rrap3 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap3_2.java",
			    "\n" +
			    "public team class Team631rrap3_2 extends Team631rrap3_1 {\n" +
			    "    public class Role631rrap3 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method uses a redefined role as a parameter
    // 6.3.1-otjld-redefined-role-as-paramerer-4
    public void test631_redefinedRoleAsParamerer4() {

       runConformTest(
            new String[] {
		"T631rrap4Main.java",
			    "\n" +
			    "public class T631rrap4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team631rrap4_2 t = new Team631rrap4_2();\n" +
			    "        Role631rrap4_2<@t>   r = t.new Role631rrap4_2();\n" +
			    "\n" +
			    "        System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap4_1.java",
			    "\n" +
			    "public team class Team631rrap4_1 {\n" +
			    "    public class Role631rrap4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public team class Role631rrap4_2 {\n" +
			    "        protected class Inner631rrap4 {\n" +
			    "            public String getValue(Role631rrap4_1 r) {\n" +
			    "                return r.toString();\n" +
			    "            }\n" +
			    "        }\n" +
			    "\n" +
			    "        public String getValue() {\n" +
			    "            return new Inner631rrap4().getValue(new Role631rrap4_1());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap4_2.java",
			    "\n" +
			    "public team class Team631rrap4_2 extends Team631rrap4_1 {\n" +
			    "    public class Role631rrap4_1 {\n" +
			    "        public String toString() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method uses a redefined role as a parameter
    // 6.3.1-otjld-redefined-role-as-paramerer-5
    public void test631_redefinedRoleAsParamerer5() {

       runConformTest(
            new String[] {
		"T631rrap5Main.java",
			    "\n" +
			    "public class T631rrap5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team631rrap5_2 t = new Team631rrap5_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap5_1.java",
			    "\n" +
			    "public team class Team631rrap5_1 {\n" +
			    "    public class Role631rrap5 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        class Local631rrap5 {\n" +
			    "            String getValue(Role631rrap5 r) {\n" +
			    "                return r.getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "        return new Local631rrap5().getValue(new Role631rrap5());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap5_2.java",
			    "\n" +
			    "public team class Team631rrap5_2 extends Team631rrap5_1 {\n" +
			    "    public class Role631rrap5 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method uses a redefined role as a parameter
    // 6.3.1-otjld-redefined-role-as-paramerer-6
    public void test631_redefinedRoleAsParamerer6() {

       runConformTest(
            new String[] {
		"T631rrap6Main.java",
			    "\n" +
			    "public class T631rrap6Main {\n" +
			    "    private static String getValue(final Team631rrap6_1 t, Role631rrap6<@t> r) {\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team631rrap6_2 t = new Team631rrap6_2();\n" +
			    "        Role631rrap6<@t>     r = t.new Role631rrap6();\n" +
			    "\n" +
			    "        System.out.print(getValue(t, r));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap6_1.java",
			    "\n" +
			    "public team class Team631rrap6_1 {\n" +
			    "    public class Role631rrap6 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team631rrap6_2.java",
			    "\n" +
			    "public team class Team631rrap6_2 extends Team631rrap6_1 {\n" +
			    "    public class Role631rrap6 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method uses a redefined role as a return type
    // 6.3.2-otjld-redefined-role-as-return-type-1
    public void test632_redefinedRoleAsReturnType1() {

       runConformTest(
            new String[] {
		"Team632rrart1_2.java",
			    "\n" +
			    "public team class Team632rrart1_2 extends Team632rrart1_1 {\n" +
			    "    public class Role632rrart1 {\n" +
			    "        public void test() { \n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role632rrart1 getRole() {\n" +
			    "        return new Role632rrart1();\n" +
			    "    }\n" +
			    "    \n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team632rrart1_1 t = new Team632rrart1_2();\n" +
			    "        Role632rrart1<@t>     r = t.getRole();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team632rrart1_1.java",
			    "\n" +
			    "public team class Team632rrart1_1 {\n" +
			    "    public class Role632rrart1 {\n" +
			    "        public void test() { \n" +
			    "            System.out.print(\"NOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role632rrart1 getRole() {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method uses a sub role as a return type
    // 6.3.2-otjld-redefined-role-as-return-type-2
    public void test632_redefinedRoleAsReturnType2() {

       runConformTest(
            new String[] {
		"Team632rrart2_2.java",
			    "\n" +
			    "public team class Team632rrart2_2 extends Team632rrart2_1 {\n" +
			    "    public class Role632rrart2_2 extends Role632rrart2_1 {\n" +
			    "        public void test() { \n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role632rrart2_2 getRole() {\n" +
			    "        return new Role632rrart2_2();\n" +
			    "    }\n" +
			    "    \n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team632rrart2_1 t = new Team632rrart2_2();\n" +
			    "        Role632rrart2_1<@t>   r = t.getRole();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team632rrart2_1.java",
			    "\n" +
			    "public team class Team632rrart2_1 {\n" +
			    "    public class Role632rrart2_1 {\n" +
			    "        public void test() { \n" +
			    "            System.out.print(\"NOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role632rrart2_1 getRole() {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }
}
