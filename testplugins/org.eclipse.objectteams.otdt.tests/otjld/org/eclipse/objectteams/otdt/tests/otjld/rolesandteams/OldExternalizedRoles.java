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

public class OldExternalizedRoles extends AbstractOTJLDTest {
	
	public OldExternalizedRoles(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test621_accessToExternalizedRole3e", "test621_accessToExternalizedRole3g"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return OldExternalizedRoles.class;
	}

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-1
    public void test621_accessToExternalizedRole1() {
       
       runConformTest(
            new String[] {
		"T621ater1Main.java",
			    "\n" +
			    "public class T621ater1Main {\n" +
			    "    private static final Team621ater1 t = new Team621ater1();\n" +
			    "\n" +
			    "    @SuppressWarnings(\"roletypesyntax\")\n" +
			    "    private static String test(t.Role621ater1 r) {\n" +
			    "        return r.value;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        System.out.print(test(t.new Role621ater1()));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater1.java",
			    "\n" +
			    "public team class Team621ater1 {\n" +
			    "    public class Role621ater1 {\n" +
			    "        public String value = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-2
    public void test621_accessToExternalizedRole2() {
       
       runConformTest(
            new String[] {
		"T621ater2Main.java",
			    "\n" +
			    "public class T621ater2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater2 t = new Team621ater2();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater2.java",
			    "\n" +
			    "public team class Team621ater2 {\n" +
			    "    private Role621ater2 r = this.new Role621ater2();\n" +
			    "\n" +
			    "    public class Role621ater2 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-3
    public void test621_accessToExternalizedRole3() {
       
       runConformTest(
            new String[] {
		"T621ater3Main.java",
			    "\n" +
			    "public class T621ater3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater3_2 t = new Team621ater3_2();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T621ater3.java",
			    "\n" +
			    "public team class T621ater3 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater3_1.java",
			    "\n" +
			    "public team class Team621ater3_1 {\n" +
			    "    public class Role621ater3 playedBy T621ater3 {\n" +
			    "        public Role621ater3(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater3_2.java",
			    "\n" +
			    "public team class Team621ater3_2 {\n" +
			    "    public String test() {\n" +
			    "        final Team621ater3_1 t = new Team621ater3_1();\n" +
			    "\n" +
			    "        return testInternal(new Object() {\n" +
			    "            public String toString() {\n" +
			    "                return t.new Role621ater3(\"NOTOK\").getValue();\n" +
			    "            }\n" +
			    "        });\n" +
			    "    }\n" +
			    "\n" +
			    "    private String testInternal(Object obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object, externalized access to non-public constructor attempted
    // 6.2.1-otjld-access-to-externalized-role-3e
    public void test621_accessToExternalizedRole3e() {
        runNegativeTest(
            new String[] {
		"Team621ater3e_2.java",
			    "\n" +
			    "public team class Team621ater3e_2 {\n" +
			    "    public String test() {\n" +
			    "        final Team621ater3e_1 t = new Team621ater3e_1();\n" +
			    "\n" +
			    "        return testInternal(new Object() {\n" +
			    "			 @Override\n" +
			    "            public String toString() {\n" +
			    "                return t.new Role621ater3e(\"NOTOK\").getValue();\n" +
			    "            }\n" +
			    "        });\n" +
			    "    }\n" +
			    "\n" +
			    "    private String testInternal(Object obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T621ater3e.java",
			    "\n" +
			    "public team class T621ater3e {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater3e_1.java",
			    "\n" +
			    "public team class Team621ater3e_1 {\n" +
			    "    public class Role621ater3e playedBy T621ater3e {\n" +
			    "        protected Role621ater3e(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
			"1. ERROR in Team621ater3e_2.java (at line 9)\n" +
			"	return t.new Role621ater3e(\"NOTOK\").getValue();\n" +
			"	       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
			"Cannot access non-public constructor Team621ater3e_1.Role621ater3e(String) to instantiate externalized role (OTJLD 1.2.1(e)).\n" +
			"----------\n");
    }

    // access to a feature of an externalized role object, externalized access to non-public constructor attempted, compiled in parts
    public void test621_accessToExternalizedRole3g() {
		runConformTest(
			new String[] {
		"Team621ater3g_1.java",
			    "\n" +
			    "public team class Team621ater3g_1 {\n" +
			    "    public class Role621ater3g playedBy T621ater3g {\n" +
			    "        protected Role621ater3g(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T621ater3g.java",
			    "\n" +
			    "public team class T621ater3g {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            });
        runNegativeTest(
            new String[] {
		"Team621ater3g_2.java",
			    "\n" +
			    "public team class Team621ater3g_2 {\n" +
			    "    public String test() {\n" +
			    "        final Team621ater3g_1 t = new Team621ater3g_1();\n" +
			    "\n" +
			    "        return testInternal(new Object() {\n" +
			    "			 @Override\n" +
			    "            public String toString() {\n" +
			    "                return t.new Role621ater3g(\"NOTOK\").getValue();\n" +
			    "            }\n" +
			    "        });\n" +
			    "    }\n" +
			    "\n" +
			    "    private String testInternal(Object obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
			"1. ERROR in Team621ater3g_2.java (at line 9)\n" +
			"	return t.new Role621ater3g(\"NOTOK\").getValue();\n" +
			"	       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
			"Cannot access non-public constructor Team621ater3g_1.Role621ater3g(String) to instantiate externalized role (OTJLD 1.2.1(e)).\n" +
			"----------\n",
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            null/*customOptions*/);
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-4f
    public void test621_accessToExternalizedRole4f() {
        runNegativeTestMatching(
            new String[] {
		"Team621ater4f_2.java",
			    "\n" +
			    "public team class Team621ater4f_2 {\n" +
			    "    public String test(final Team621ater4f_1 t) {\n" +
			    "        class Local621ater4f {\n" +
			    "            String getValue(Role621ater4f<@t> r) {\n" +
			    "                return r.value;\n" +
			    "            }\n" +
			    "        }\n" +
			    "\n" +
			    "        return new Local621ater4f().getValue(t.new Role621ater4f());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater4f_1.java",
			    "\n" +
			    "public team class Team621ater4f_1 {\n" +
			    "    public class Role621ater4f {\n" +
			    "        String value = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.1(e)");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-4
    public void test621_accessToExternalizedRole4() {
       
       runConformTest(
            new String[] {
		"T621ater4Main.java",
			    "\n" +
			    "public class T621ater4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater4_1 t1 = new Team621ater4_1();\n" +
			    "        Team621ater4_2 t2 = new Team621ater4_2();\n" +
			    "\n" +
			    "        System.out.print(t2.test(t1));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater4_1.java",
			    "\n" +
			    "public team class Team621ater4_1 {\n" +
			    "    public class Role621ater4 {\n" +
			    "        public String value = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater4_2.java",
			    "\n" +
			    "public team class Team621ater4_2 {\n" +
			    "    public String test(final Team621ater4_1 t) {\n" +
			    "        class Local621ater4 {\n" +
			    "            String getValue(Role621ater4<@t> r) {\n" +
			    "                return r.value;\n" +
			    "            }\n" +
			    "        }\n" +
			    "\n" +
			    "        return new Local621ater4().getValue(t.new Role621ater4());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a protected feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-5
    public void test621_accessToExternalizedRole5() {
        runNegativeTestMatching(
            new String[] {
		"Team621ater5_2.java",
			    "\n" +
			    "public team class Team621ater5_2 extends Team621ater5_1 {\n" +
			    "    public String test() {\n" +
			    "        Role621ater5<@singleton> r = singleton.new Role621ater5();\n" +
			    "\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "    public Team621ater5_2(Team621ater5_1 t) {\n" +
			    "    	super(t);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater5_1.java",
			    "\n" +
			    "public team class Team621ater5_1 {\n" +
			    "    protected final Team621ater5_1 singleton;\n" +
			    "    public Team621ater5_1(Team621ater5_1 t) {\n" +
			    "    	singleton = t;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role621ater5 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.1(e)");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-6
    public void test621_accessToExternalizedRole6() {
       
       runConformTest(
            new String[] {
		"T621ater6Main.java",
			    "\n" +
			    "public class T621ater6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater6_1 t1 = new Team621ater6_1();\n" +
			    "        Team621ater6_2 t2 = new Team621ater6_2();\n" +
			    "\n" +
			    "        System.out.print(t1.test(t2));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T621ater6.java",
			    "\n" +
			    "public team class T621ater6 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater6_1.java",
			    "\n" +
			    "public team class Team621ater6_1 {\n" +
			    "    public String test(final Team621ater6_2 t) {\n" +
			    "        Role621ater6<@t> r = t.new Role621ater6(\"NOTOK\");\n" +
			    "\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater6_2.java",
			    "\n" +
			    "public team class Team621ater6_2 extends Team621ater6_1 {\n" +
			    "    public class Role621ater6 playedBy T621ater6 {\n" +
			    "        public Role621ater6(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-7
    public void test621_accessToExternalizedRole7() {
       
       runConformTest(
            new String[] {
		"T621ater7Main.java",
			    "\n" +
			    "public class T621ater7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater7_3 t = new Team621ater7_3();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater7_1.java",
			    "\n" +
			    "public team class Team621ater7_1 {\n" +
			    "    public class Role621ater7 {\n" +
			    "        String value = \"OK\";\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater7_2.java",
			    "\n" +
			    "public abstract team class Team621ater7_2 {\n" +
			    "    public String test() {\n" +
			    "        final Team621ater7_1 t = new Team621ater7_1();\n" +
			    "        Role621ater7<@t>     r = t.new Role621ater7();\n" +
			    "\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater7_3.java",
			    "\n" +
			    "public team class Team621ater7_3 extends Team621ater7_2 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a (default visible) feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-7e
    public void test621_accessToExternalizedRole7e() {
        runNegativeTestMatching(
            new String[] {
		"Team621ater7e_2.java",
			    "\n" +
			    "public abstract team class Team621ater7e_2 {\n" +
			    "    public String test() {\n" +
			    "        final Team621ater7e_1 t = new Team621ater7e_1();\n" +
			    "        Role621ater7e<@t>     r = t.new Role621ater7e();\n" +
			    "\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater7e_1.java",
			    "\n" +
			    "public team class Team621ater7e_1 {\n" +
			    "    public class Role621ater7e {\n" +
			    "        String value = \"OK\";\n" +
			    "        String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.1(e)");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld_testbug-access-to-externalized-role-8
    public void _testbug_test621_accessToExternalizedRole8() {
       
       runConformTest(
            new String[] {
		"T621ater8Main.java",
			    "\n" +
			    "public class T621ater8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater8 t = new Team621ater8();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater8.java",
			    "\n" +
			    "public team class Team621ater8 {\n" +
			    "    public class Role621ater8 {\n" +
			    "        public String getValue(Team621ater8.this.Role621ater8 r) {\n" +
			    "            return r.getValue();\n" +
			    "        }\n" +
			    "        private String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        Role621ater8 r1 = new Role621ater8();\n" +
			    "        Role621ater8 r2 = new Role621ater8();\n" +
			    "\n" +
			    "        return r1.getValue(r2);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld_testbug-access-to-externalized-role-9
    public void _testbug_test621_accessToExternalizedRole9() {
       
       runConformTest(
            new String[] {
		"T621ater9Main.java",
			    "\n" +
			    "public class T621ater9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater9 t = new Team621ater9();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T621ater9.java",
			    "\n" +
			    "public team class T621ater9 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater9_1.java",
			    "\n" +
			    "public team class Team621ater9_1 {\n" +
			    "    public class Role621ater9_1 playedBy T621ater9 {\n" +
			    "        public Role621ater9_1(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        protected abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater9_2.java",
			    "\n" +
			    "public team class Team621ater9_2 extends Team621ater9_1 {\n" +
			    "    public class Role621ater9_2 extends Role621ater9_1 {\n" +
			    "        public Role621ater9_2(String value) {\n" +
			    "            super(value);\n" +
			    "        }\n" +
			    "\n" +
			    "        public String getValue() {\n" +
			    "            Team621ater9_2.this.Role621ater9_1 r = Team621ater9_2.this.new Role621ater9_1(\"NOTOK\");\n" +
			    "\n" +
			    "            return r.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        Role621ater9_2 r = new Role621ater9_2();\n" +
			    "\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld_testbug-access-to-externalized-role-10
    public void _testbug_test621_accessToExternalizedRole10() {
       
       runConformTest(
            new String[] {
		"T621ater10Main.java",
			    "\n" +
			    "public class T621ater10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater10 t = new Team621ater10();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater10.java",
			    "\n" +
			    "public team class Team621ater10 {\n" +
			    "    public class Role621ater10_1 {\n" +
			    "        Team621ater10.this.Role621ater10_2 r = new Team621ater10.this.Role621ater10_2();\n" +
			    "\n" +
			    "        public String getValue() {\n" +
			    "            return r.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role621ater10_2 extends Role621ater10_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        Role621ater10_1 r = new Role621ater10_1();\n" +
			    "\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-11
    public void test621_accessToExternalizedRole11() {
       
       runConformTest(
            new String[] {
		"T621ater11Main.java",
			    "\n" +
			    "public class T621ater11Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater11_2 t = new Team621ater11_2(new Team621ater11_3());\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater11_1.java",
			    "\n" +
			    "public team class Team621ater11_1 {\n" +
			    "    protected final Team621ater11_3 singleton;\n" +
			    "    public Team621ater11_1(Team621ater11_3 t) {\n" +
			    "    	singleton = t;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater11_2.java",
			    "\n" +
			    "public team class Team621ater11_2 extends Team621ater11_1 {\n" +
			    "    Role621ater11_2<@singleton> r;\n" +
			    "    public Team621ater11_2(Team621ater11_3 t) {\n" +
			    "    	super(t);\n" +
			    "	if (t != null)\n" +
			    "     	    r = singleton.new Role621ater11_2();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role621ater11_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return r.value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        Role621ater11_1 r = new Role621ater11_1();\n" +
			    "\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater11_3.java",
			    "\n" +
			    "public team class Team621ater11_3 extends Team621ater11_2 {\n" +
			    "    public Team621ater11_3() {\n" +
			    "    	super(null);\n" +
			    "    }\n" +
			    "    public class Role621ater11_2 extends Role621ater11_1 {\n" +
			    "        public String value = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-12
    public void test621_accessToExternalizedRole12() {
       
       runConformTest(
            new String[] {
		"T621ater12Main.java",
			    "\n" +
			    "public class T621ater12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater12_2 t = new Team621ater12_2();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T621ater12.java",
			    "\n" +
			    "public team class T621ater12 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater12_1.java",
			    "\n" +
			    "public team class Team621ater12_1 {\n" +
			    "    public class Role621ater12_1 playedBy T621ater12 {\n" +
			    "        public Role621ater12_1(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater12_2.java",
			    "\n" +
			    "public team class Team621ater12_2 extends Team621ater12_1 {\n" +
			    "    public class Role621ater12_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "	public Role621ater12_1() {\n" +
			    "		base();\n" +
			    "	}\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role621ater12_2 extends Role621ater12_1 {\n" +
			    "        private final Team621ater12_1 t = new Team621ater12_1();\n" +
			    "\n" +
			    "        public String getValue() {\n" +
			    "            // this should create an instance of the original role, not the redefined one in this team\n" +
			    "            Role621ater12_1<@t> r = t.new Role621ater12_1(\"NOTOK\");\n" +
			    "\n" +
			    "            return r.getValue();\n" +
			    "        }\n" +
			    "	public Role621ater12_2() {\n" +
			    "		super();\n" +
			    "	}\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        Role621ater12_2 r = new Role621ater12_2();\n" +
			    "\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-13
    public void test621_accessToExternalizedRole13() {
       
       runConformTest(
            new String[] {
		"T621ater13Main.java",
			    "\n" +
			    "public class T621ater13Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team621ater13_1 t1 = new Team621ater13_1();\n" +
			    "        Role621ater13_1<@t1>  r1 = t1.new Role621ater13_1();\n" +
			    "        final Team621ater13_2 t2 = new Team621ater13_2();\n" +
			    "        Role621ater13_2<@t2>  r2 = t2.new Role621ater13_2();\n" +
			    "\n" +
			    "        System.out.print(r2.getValue(t1, r1));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater13_1.java",
			    "\n" +
			    "public team class Team621ater13_1 {\n" +
			    "    public class Role621ater13_1 {\n" +
			    "        String value = \"OK\";\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater13_2.java",
			    "\n" +
			    "public team class Team621ater13_2 {\n" +
			    "    public class Role621ater13_2 {\n" +
			    "        public String getValue(final Team621ater13_1 t, Role621ater13_1<@t> r) {\n" +
			    "            return r.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-14
    public void test621_accessToExternalizedRole14() {
       
       runConformTest(
            new String[] {
		"T621ater14Main.java",
			    "\n" +
			    "public class T621ater14Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team621ater14_1       t1 = new Team621ater14_1();\n" +
			    "        final Team621ater14_2 t2 = new Team621ater14_2();\n" +
			    "        Role621ater14_2<@t2>  r2 = t2.new Role621ater14_2();\n" +
			    "\n" +
			    "        System.out.print(r2.getValue(t1));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater14_1.java",
			    "\n" +
			    "public team class Team621ater14_1 {\n" +
			    "    public class Role621ater14_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater14_2.java",
			    "\n" +
			    "public team class Team621ater14_2 {\n" +
			    "    public class Role621ater14_2 {\n" +
			    "        public String getValue(final Team621ater14_1 t) {\n" +
			    "            return getValueInternal(new Object() {\n" +
			    "                public String toString() {\n" +
			    "                    return t.new Role621ater14_1().getValue();\n" +
			    "                }\n" +
			    "            });\n" +
			    "        }\n" +
			    "\n" +
			    "        private String getValueInternal(Object obj) {\n" +
			    "            return obj.toString();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object -- unresolved type anchor in method of local class
    // 6.2.1-otjld-access-to-externalized-role-15f
    public void test621_accessToExternalizedRole15f() {
        runNegativeTest(
            new String[] {
		"T621ater15f.java",
			    "\n" +
			    "public team class T621ater15f {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater15f_1.java",
			    "\n" +
			    "public team class Team621ater15f_1 {\n" +
			    "    public class Role621ater15f_1 playedBy T621ater15f {\n" +
			    "        public Role621ater15f_1(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        protected abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater15f_2.java",
			    "\n" +
			    "public team class Team621ater15f_2 {\n" +
			    "    public class Role621ater15f_2 {\n" +
			    "        public String getValue() {\n" +
			    "            final Team621ater15f_1 t = new Team621ater15f_1();\n" +
			    "\n" +
			    "            class Local621ater15f {\n" +
			    "                String getValue(Role621ater15f_1<@wrong> r) {\n" +
			    "                    return r.getValue();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            return new Local621ater15f().getValue(t.new Role621ater15f_1(\"NOTOK\"));\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // access to a feature of an externalized role object
    // 6.2.1-otjld-access-to-externalized-role-15
    public void test621_accessToExternalizedRole15() {
       
       runConformTest(
            new String[] {
		"T621ater15Main.java",
			    "\n" +
			    "public class T621ater15Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team621ater15_2 t = new Team621ater15_2();\n" +
			    "        Role621ater15_2<@t>   r = t.new Role621ater15_2();\n" +
			    "\n" +
			    "        System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T621ater15.java",
			    "\n" +
			    "public team class T621ater15 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater15_1.java",
			    "\n" +
			    "public team class Team621ater15_1 {\n" +
			    "    public class Role621ater15_1 playedBy T621ater15 {\n" +
			    "        public Role621ater15_1(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team621ater15_2.java",
			    "\n" +
			    "public team class Team621ater15_2 {\n" +
			    "    public class Role621ater15_2 {\n" +
			    "        public String getValue() {\n" +
			    "            final Team621ater15_1 t = new Team621ater15_1();\n" +
			    "\n" +
			    "            class Local621ater15 {\n" +
			    "                String getValue(Role621ater15_1<@t> r) {\n" +
			    "                    return r.getValue();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            return new Local621ater15().getValue(t.new Role621ater15_1(\"NOTOK\"));\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an externalized role is declared with a non-final team variable, but never involved in any assignment
    // 6.2.2-otjld-nonfinal-externalized-role-anchor-1
    public void test622_nonfinalExternalizedRoleAnchor1() {
       
       runConformTest(
            new String[] {
		"Team622nera1_2.java",
			    "\n" +
			    "public team class Team622nera1_2 {\n" +
			    "    public class Role622nera1_2 {\n" +
			    "        protected Team622nera1_1 t = new Team622nera1_1();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role622nera1_3 extends Role622nera1_2 {\n" +
			    "        public String getValue() {\n" +
			    "            return t.new Role622nera1_1().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team622nera1_2() {\n" +
			    "    	Role622nera1_3 r = new Role622nera1_3();\n" +
			    "	System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "    	new Team622nera1_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team622nera1_1.java",
			    "\n" +
			    "public team class Team622nera1_1 {\n" +
			    "    public class Role622nera1_1 {\n" +
			    "        String value = \"OK\";\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an externalized role is declared with a non-final team variable, but never involved in any assignment
    // 6.2.2-otjld-nonfinal-externalized-role-anchor-1a
    public void test622_nonfinalExternalizedRoleAnchor1a() {
       
       runConformTest(
            new String[] {
		"Team622nera1a_2.java",
			    "\n" +
			    "public team class Team622nera1a_2 {\n" +
			    "    public class Role622nera1a_2 {\n" +
			    "        protected Team622nera1a_1 t = new Team622nera1a_1();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role622nera1a_3 extends Role622nera1a_2 {\n" +
			    "        public String getValue() {\n" +
			    "            return t.getRole1().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team622nera1a_2() {\n" +
			    "        Role622nera1a_3 r = new Role622nera1a_3();\n" +
			    "        System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team622nera1a_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team622nera1a_1.java",
			    "\n" +
			    "public team class Team622nera1a_1 {\n" +
			    "    public class Role622nera1a_1 {\n" +
			    "        String value = \"OK\";\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Role622nera1a_1 getRole1() {\n" +
			    "        return new Role622nera1a_1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an externalized role is declared with a non-final team obtained from a method call, but never involved in any assignment
    // 6.2.2-otjld-nonfinal-externalized-role-anchor-1m
    public void test622_nonfinalExternalizedRoleAnchor1m() {
       
       runConformTest(
            new String[] {
		"Team622nera1m_2.java",
			    "\n" +
			    "public team class Team622nera1m_2 {\n" +
			    "    public class Role622nera1m_2 {\n" +
			    "        protected Team622nera1m_1 getT() {\n" +
			    "            return new Team622nera1m_1();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role622nera1m_3 extends Role622nera1m_2 {\n" +
			    "        public String getValue() {\n" +
			    "            return getT().getRole1().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team622nera1m_2() {\n" +
			    "        Role622nera1m_3 r = new Role622nera1m_3();\n" +
			    "        System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team622nera1m_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team622nera1m_1.java",
			    "\n" +
			    "public team class Team622nera1m_1 {\n" +
			    "    public class Role622nera1m_1 {\n" +
			    "        String value = \"OK\";\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Role622nera1m_1 getRole1() {\n" +
			    "        return new Role622nera1m_1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an externalized role is declared with a non-final team obtained from a method call, but never involved in any assignment
    // 6.2.2-otjld-nonfinal-externalized-role-anchor-1n
    public void test622_nonfinalExternalizedRoleAnchor1n() {
       
       runConformTest(
            new String[] {
		"Team622nera1n_2.java",
			    "\n" +
			    "public team class Team622nera1n_2 {\n" +
			    "    public class Role622nera1n_2 {\n" +
			    "        protected Team622nera1n_1 getT() {\n" +
			    "            return new Team622nera1n_1();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role622nera1n_3 extends Role622nera1n_2 {\n" +
			    "        public String getValue() {\n" +
			    "            return getT().new Role622nera1n_1().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team622nera1n_2() {\n" +
			    "        Role622nera1n_3 r = new Role622nera1n_3();\n" +
			    "        System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team622nera1n_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team622nera1n_1.java",
			    "\n" +
			    "public team class Team622nera1n_1 {\n" +
			    "    public class Role622nera1n_1 {\n" +
			    "        String value = \"OK\";\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Role622nera1n_1 getRole1() {\n" +
			    "        return new Role622nera1n_1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an externalized role is declared with a non-final team variable, assignment  fails
    // 6.2.2-otjld-nonfinal-externalized-role-anchor-1f
    public void test622_nonfinalExternalizedRoleAnchor1f() {
        runNegativeTestMatching(
            new String[] {
		"Team622nera1f_2.java",
			    "\n" +
			    "public team class Team622nera1f_2 {\n" +
			    "    public class Role622nera1f_2 {\n" +
			    "        protected Team622nera1f_1 t = new Team622nera1f_1();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role622nera1f_3 extends Role622nera1f_2 {\n" +
			    "        public String getValue() {\n" +
			    "            Role622nera1f_1<@t> r = t.getRole1();\n" +
			    "            return r.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team622nera1f_2() {\n" +
			    "        Role622nera1f_3 r = new Role622nera1f_3();\n" +
			    "        System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team622nera1f_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team622nera1f_1.java",
			    "\n" +
			    "public team class Team622nera1f_1 {\n" +
			    "    public class Role622nera1f_1 {\n" +
			    "        String value = \"OK\";\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Role622nera1f_1 getRole1() {\n" +
			    "        return new Role622nera1f_1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.2(c)");
    }

    // an externalized role is declared with a non-final team variable
    // 6.2.2-otjld-nonfinal-externalized-role-anchor-2
    public void test622_nonfinalExternalizedRoleAnchor2() {
        runNegativeTest(
            new String[] {
		"Team622nera2_1.java",
			    "\n" +
			    "public team class Team622nera2_1 {\n" +
			    "    public class Role622nera2_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team622nera2_2.java",
			    "\n" +
			    "public team class Team622nera2_2 {\n" +
			    "    private Team622nera2_1     t = new Team622nera2_1();\n" +
			    "    private Role622nera2_1<@t> r;\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an externalized role is declared with a non-final team variable
    // 6.2.2-otjld-nonfinal-externalized-role-anchor-3
    public void test622_nonfinalExternalizedRoleAnchor3() {
        runNegativeTest(
            new String[] {
		"Team622nera3.java",
			    "\n" +
			    "public team class Team622nera3 {\n" +
			    "    public class Role622nera3 {\n" +
			    "        public String value = \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(Team622nera3 t, Role622nera3<@t> r) {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an externalized role is declared with a non-final team variable
    // 6.2.2-otjld-nonfinal-externalized-role-anchor-4
    public void test622_nonfinalExternalizedRoleAnchor4() {
        runNegativeTest(
            new String[] {
		"Team622nera4.java",
			    "\n" +
			    "public team class Team622nera4 {\n" +
			    "    public class Role622nera4 {\n" +
			    "        public String value = \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        Team622nera4     t = this;\n" +
			    "        Role622nera4<@t> r = t.new Role622nera4();\n" +
			    "\n" +
			    "        return r.value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an externalized role is declared with the team type identifier instead of a variable
    // 6.2.3-otjld-externalized-role-with-team-type
    public void test623_externalizedRoleWithTeamType() {
        runNegativeTest(
            new String[] {
		"Team623erwtt.java",
			    "\n" +
			    "public team class Team623erwtt {\n" +
			    "    public class Role623erwtt {}\n" +
			    "}\n" +
			    "    \n",
		"T623erwtt.java",
			    "\n" +
			    "public class T623erwtt {\n" +
			    "    public void test() {\n" +
			    "        Team623erwtt.Role623erwtt r = new Team623erwtt().new Role623erwtt();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an externalized role is declared with the a variable of an unrelated team
    // 6.2.4-otjld-externalized-role-with-unrelated-team-1
    public void test624_externalizedRoleWithUnrelatedTeam1() {
        runNegativeTest(
            new String[] {
		"Team624erwut1_1.java",
			    "\n" +
			    "public team class Team624erwut1_1 {}\n" +
			    "    \n",
		"Team624erwut1_2.java",
			    "\n" +
			    "public team class Team624erwut1_2 {\n" +
			    "    public class Role624erwut1 {}\n" +
			    "    public void test() {\n" +
			    "        final Team624erwut1_1 t = new Team624erwut1_1();\n" +
			    "        Role624erwut1<@t>     r = t.new Role624erwut1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an externalized role is declared with the a variable of an unrelated team
    // 6.2.4-otjld-externalized-role-with-unrelated-team-2
    public void test624_externalizedRoleWithUnrelatedTeam2() {
        runNegativeTest(
            new String[] {
		"Team624erwut2_1.java",
			    "\n" +
			    "public team class Team624erwut2_1 {}\n" +
			    "    \n",
		"Team624erwut2_2.java",
			    "\n" +
			    "public team class Team624erwut2_2 extends Team624erwut2_1 {\n" +
			    "    public class Role624erwut2 {}\n" +
			    "}\n" +
			    "    \n",
		"T624erwut2.java",
			    "\n" +
			    "public class T624erwut2 {\n" +
			    "    public void test(final Team624erwut2_1 t, Role624erwut2<@t> r) {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // access to a feature of an externalized role object that has converted
    // 6.2.5-otjld-different-dynamic-type-externalized-role-1
    public void test625_differentDynamicTypeExternalizedRole1() {
       
       runConformTest(
            new String[] {
		"T625ddter1Main.java",
			    "\n" +
			    "public class T625ddter1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team625ddter1 t = new Team625ddter1();\n" +
			    "        Role625ddter1_1<@t> r = t.new Role625ddter1_2();\n" +
			    "\n" +
			    "        System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team625ddter1.java",
			    "\n" +
			    "public team class Team625ddter1 {\n" +
			    "    public class Role625ddter1_1 {\n" +
			    "	public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role625ddter1_2 extends Role625ddter1_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of an externalized role object that has been lifted -- illegal syntax
    // 6.2.5-otjld-different-dynamic-type-externalized-role-2
    public void test625_differentDynamicTypeExternalizedRole2() {
        runNegativeTestMatching(
            new String[] {
		"Team625ddter2_2.java",
			    "\n" +
			    "public team class Team625ddter2_2 {\n" +
			    "    public String getValue(final Team625ddter2_1 t, T625ddter2 as t.Role625ddter2_1 r) {\n" +
			    "        return r.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T625ddter2.java",
			    "\n" +
			    "public team class T625ddter2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team625ddter2_1.java",
			    "\n" +
			    "public team class Team625ddter2_1 {\n" +
			    "    public class Role625ddter2_1 playedBy T625ddter2 {\n" +
			    "        public Role625ddter2_1(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        protected abstract String test();\n" +
			    "        test -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.3.2(a)");
    }

    // a local extends an externalized role
    // 6.2.6-otjld-local-class-extends-externalized-role-1
    public void test626_localClassExtendsExternalizedRole1() {
        runNegativeTest(
            new String[] {
		"Team626lceer1.java",
			    "\n" +
			    "public team class Team626lceer1 {\n" +
			    "    public class Role626lceer1 {}\n" +
			    "}\n" +
			    "    \n",
		"T626lceer1.java",
			    "\n" +
			    "public class T626lceer1 {\n" +
			    "    public void test(final Team626lceer1 t) {\n" +
			    "        class Local626lceer1 extends Role626lceer1<@t> {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a local extends an externalized role
    // 6.2.6-otjld-local-class-extends-externalized-role-2
    public void test626_localClassExtendsExternalizedRole2() {
        runNegativeTestMatching(
            new String[] {
		"Team626lceer2.java",
			    "\n" +
			    "public team class Team626lceer2 {\n" +
			    "    public class Role626lceer2 {}\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        final Team626lceer2 t = this;\n" +
			    "        Object              o = new Role626lceer2<@t>() {\n" +
			    "            public String toString() {\n" +
			    "                return \"OK\";\n" +
			    "            }\n" +
			    "        };\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.2(g)");
    }
}
