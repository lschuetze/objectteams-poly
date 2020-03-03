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

import junit.framework.Test;

import org.eclipse.jdt.core.tests.junit.extension.TestCase;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class ExplicitRoleCreation extends AbstractOTJLDTest {
	
	public ExplicitRoleCreation(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test2335_creatingRoleOfRegularInner"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class<? extends TestCase> testClass() {
		return ExplicitRoleCreation.class;
	}

    // an unbound role without a superroles or -class is created using the default constructor
    // 2.3.1-otjld-creation-of-unbound-role-1
    public void test231_creationOfUnboundRole1() {
       
       runConformTest(
            new String[] {
		"T231cour1Main.java",
			    "\n" +
			    "public class T231cour1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team231cour1 t = new Team231cour1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team231cour1.java",
			    "\n" +
			    "public team class Team231cour1 {\n" +
			    "    public class Role231cour1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role231cour1().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an unbound role without a superroles or -class is created using a no-arg constructor
    // 2.3.1-otjld-creation-of-unbound-role-2
    public void test231_creationOfUnboundRole2() {
       
       runConformTest(
            new String[] {
		"T231cour2Main.java",
			    "\n" +
			    "public class T231cour2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team231cour2 t = new Team231cour2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team231cour2.java",
			    "\n" +
			    "public team class Team231cour2 {\n" +
			    "    public class Role231cour2 {\n" +
			    "        private String value;\n" +
			    "        // this is allowed according to 2.2h as this role is not bound\n" +
			    "        public Role231cour2() {\n" +
			    "            value = \"OK\";\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role231cour2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an unbound role with a superclass is created
    // 2.3.1-otjld-creation-of-unbound-role-3
    public void test231_creationOfUnboundRole3() {
       
       runConformTest(
            new String[] {
		"T231cour3Main.java",
			    "\n" +
			    "public class T231cour3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team231cour3 t = new Team231cour3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T231cour3.java",
			    "\n" +
			    "public class T231cour3 {\n" +
			    "    protected String value;\n" +
			    "    public T231cour3(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team231cour3.java",
			    "\n" +
			    "public team class Team231cour3 {\n" +
			    "    public class Role231cour3 extends T231cour3 {\n" +
			    "        public Role231cour3() {\n" +
			    "            super(\"OK\");\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role231cour3().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an unbound role with an implicit superrole is created with constructors using this and tsuper
    // 2.3.1-otjld-creation-of-unbound-role-4
    public void test231_creationOfUnboundRole4() {
       
       runConformTest(
            new String[] {
		"T231cour4Main.java",
			    "\n" +
			    "public class T231cour4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team231cour4_2 t = new Team231cour4_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team231cour4_1.java",
			    "\n" +
			    "public team class Team231cour4_1 {\n" +
			    "    public class Role231cour4 {\n" +
			    "        private String value;\n" +
			    "        public Role231cour4(String value) {\n" +
			    "            this.value = value;\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team231cour4_2.java",
			    "\n" +
			    "public team class Team231cour4_2 extends Team231cour4_1 {\n" +
			    "    public class Role231cour4 {\n" +
			    "        public Role231cour4() {\n" +
			    "            this(1);\n" +
			    "        }\n" +
			    "        private Role231cour4(int value) {\n" +
			    "            tsuper(String.valueOf(value));\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role231cour4().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1");
    }

    // an unbound role with an explicit superrole is created with constructors using this and ssuper
    // 2.3.1-otjld-creation-of-unbound-role-5
    public void test231_creationOfUnboundRole5() {
       
       runConformTest(
            new String[] {
		"T231cour5Main.java",
			    "\n" +
			    "public class T231cour5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team231cour5 t = new Team231cour5();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team231cour5.java",
			    "\n" +
			    "public team class Team231cour5 {\n" +
			    "    public class Role231cour5_1 {\n" +
			    "        private String value;\n" +
			    "        public Role231cour5_1(String value) {\n" +
			    "            this.value = value;\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role231cour5_2 extends Role231cour5_1 {\n" +
			    "        public Role231cour5_2() {\n" +
			    "            this(1);\n" +
			    "        }\n" +
			    "        private Role231cour5_2(int value) {\n" +
			    "            super(String.valueOf(value));\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role231cour5_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1");
    }

    // a role is created externalized with parameterized syntax
    // 2.3.1-otjld-creation-of-unbound-role-6
    public void test231_creationOfUnboundRole6() {
       
       runConformTest(
            new String[] {
		"T231cour6Main.java",
			    "\n" +
			    "public class T231cour6Main {\n" +
			    "  public static void main(String[] args) {\n" +
			    "    final Team231cour6 t = new Team231cour6();\n" +
			    "    R<@t> r = new R<@t>();\n" +
			    "    r.test();\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"Team231cour6.java",
			    "\n" +
			    "public team class Team231cour6 {\n" +
			    "  public class R {\n" +
			    "    public void test() { System.out.print(\"OK\"); }\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n"
            },
            "OK");
    }

    // a role directly bound to a baseclass and without a constructor is created
    // 2.3.2-otjld-bound-role-without-constructor-1
    public void test232_boundRoleWithoutConstructor1() {
       
       runConformTest(
            new String[] {
		"T232brwc1Main.java",
			    "\n" +
			    "public class T232brwc1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team232brwc1 t = new Team232brwc1();\n" +
			    "        T232brwc1    o = new T232brwc1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T232brwc1.java",
			    "\n" +
			    "public class T232brwc1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team232brwc1.java",
			    "\n" +
			    "public team class Team232brwc1 {\n" +
			    "    public class Role232brwc1 playedBy T232brwc1 {\n" +
			    "        private String value = \"OK\";\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- value\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T232brwc1 as Role232brwc1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role indirectly bound to a baseclass via its explicit superrole, and without a constructor is created
    // 2.3.2-otjld-bound-role-without-constructor-2
    public void test232_boundRoleWithoutConstructor2() {
       
       runConformTest(
            new String[] {
		"T232brwc2Main.java",
			    "\n" +
			    "public class T232brwc2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team232brwc2 t = new Team232brwc2();\n" +
			    "        T232brwc2    o = new T232brwc2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T232brwc2.java",
			    "\n" +
			    "public class T232brwc2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team232brwc2.java",
			    "\n" +
			    "public team class Team232brwc2 {\n" +
			    "    public class Role232brwc2_1 playedBy T232brwc2 {}\n" +
			    "\n" +
			    "    public class Role232brwc2_2 extends Role232brwc2_1 {\n" +
			    "        private String value = \"OK\";\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- value\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T232brwc2 as Role232brwc2_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role indirectly bound to a baseclass via its implicit superrole, and without a constructor is created
    // 2.3.2-otjld-bound-role-without-constructor-3
    public void test232_boundRoleWithoutConstructor3() {
       
       runConformTest(
            new String[] {
		"T232brwc3Main.java",
			    "\n" +
			    "public class T232brwc3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team232brwc3_2 t = new Team232brwc3_2();\n" +
			    "        T232brwc3      o = new T232brwc3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T232brwc3.java",
			    "\n" +
			    "public class T232brwc3 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team232brwc3_1.java",
			    "\n" +
			    "public team class Team232brwc3_1 {\n" +
			    "    public class Role232brwc3 playedBy T232brwc3 {}\n" +
			    "}\n" +
			    "    \n",
		"Team232brwc3_2.java",
			    "\n" +
			    "public team class Team232brwc3_2 extends Team232brwc3_1 {\n" +
			    "    public class Role232brwc3 {\n" +
			    "        private String value = \"OK\";\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- value\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public String getValue(T232brwc3 as Role232brwc3 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role indirectly bound to a baseclass via its indirect superrole, and without a constructor is created
    // 2.3.2-otjld-bound-role-without-constructor-4
    public void test232_boundRoleWithoutConstructor4() {
       
       runConformTest(
            new String[] {
		"T232brwc4Main.java",
			    "\n" +
			    "public class T232brwc4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team232brwc4_2 t = new Team232brwc4_2();\n" +
			    "        T232brwc4_2    o = new T232brwc4_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T232brwc4_1.java",
			    "\n" +
			    "public class T232brwc4_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T232brwc4_2.java",
			    "\n" +
			    "public class T232brwc4_2 extends T232brwc4_1 {}\n" +
			    "    \n",
		"Team232brwc4_1.java",
			    "\n" +
			    "public team class Team232brwc4_1 {\n" +
			    "    public class Role232brwc4_1 playedBy T232brwc4_1 {}\n" +
			    "\n" +
			    "    public class Role232brwc4_2 extends Role232brwc4_1 playedBy T232brwc4_2 {\n" +
			    "        private String value = \"OK\";\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- value\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team232brwc4_2.java",
			    "\n" +
			    "public team class Team232brwc4_2 extends Team232brwc4_1 {\n" +
			    "    public class Role232brwc4_3 extends Role232brwc4_2 {}\n" +
			    "\n" +
			    "    public String getValue(T232brwc4_2 as Role232brwc4_3 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role directly bound to a baseclass has a constructor without a base constructor call
    // 2.3.3-otjld-bound-role-constructor-without-basecalls-1
    public void test233_boundRoleConstructorWithoutBasecalls1() {
        runNegativeTestMatching(
            new String[] {
		"Team233brcwb1.java",
			    "\n" +
			    "public team class Team233brcwb1 {\n" +
			    "    public class Role233brcwb1 playedBy T233brcwb1 {\n" +
			    "        public Role233brcwb1(String value) {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T233brcwb1.java",
			    "\n" +
			    "public class T233brcwb1 {}\n" +
			    "    \n"
            },
            "2.4.2(b)");
    }

    // a role indirectly bound to a baseclass has a constructor without a base constructor call
    // 2.3.3-otjld-bound-role-constructor-without-basecalls-2
    public void test233_boundRoleConstructorWithoutBasecalls2() {
        runNegativeTestMatching(
            new String[] {
		"Team233brcwb2_2.java",
			    "\n" +
			    "public team class Team233brcwb2_2 extends Team233brcwb2_1 {\n" +
			    "    public class Role233brcwb2_3 extends Role233brcwb2_2 playedBy T233brcwb2_2 {\n" +
			    "        public Role233brcwb2_3() {\n" +
			    "            super(\"OK\");\n" +
			    "            // here the base call is missing as we're bound to a new baseclass\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T233brcwb2_1.java",
			    "\n" +
			    "public class T233brcwb2_1 {\n" +
			    "    public T233brcwb2_1(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"T233brcwb2_2.java",
			    "\n" +
			    "public class T233brcwb2_2 extends T233brcwb2_1 {\n" +
			    "    public T233brcwb2_2() {\n" +
			    "        super(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team233brcwb2_1.java",
			    "\n" +
			    "public team class Team233brcwb2_1 {\n" +
			    "    public class Role233brcwb2_1 playedBy T233brcwb2_1 {\n" +
			    "        public Role233brcwb2_1(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role233brcwb2_2 extends Role233brcwb2_1 {\n" +
			    "        public Role233brcwb2_2(String s) {\n" +
			    "            super(s);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.4.2(c)");
    }

    // a ctor of a role directly bound to a baseclass has no base-constructor-call
    // 2.3.4-otjld-bound-role-with-noarg-constructor-1
    public void test234_boundRoleWithNoargConstructor1() {
        runNegativeTestMatching(
            new String[] {
		"Team234brwnc1.java",
			    "\n" +
			    "public team class Team234brwnc1 {\n" +
			    "    public class Role234brwnc1 playedBy T234brwnc1 {\n" +
			    "        public Role234brwnc1() {\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T234brwnc1.java",
			    "\n" +
			    "public class T234brwnc1 {}\n" +
			    "    \n"
            },
            "2.4.2(b)");
    }

    // role constructors call each other but no base constructor
    // 2.3.4-otjld-bound-role-with-noarg-constructor-2
    public void test234_boundRoleWithNoargConstructor2() {
        runNegativeTestMatching(
            new String[] {
		"Team234brwnc2_2.java",
			    "\n" +
			    "public team class Team234brwnc2_2 extends Team234brwnc2_1 {\n" +
			    "    public class Role234brwnc2_2 extends Role234brwnc2_1 playedBy T234brwnc2 {\n" +
			    "        public Role234brwnc2_2() {\n" +
			    "            this(\"OK\");\n" +
			    "        }\n" +
			    "	public Role234brwnc2_2(String val) { \n" +
			    "	    super(val);\n" +
			    "	}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T234brwnc2.java",
			    "\n" +
			    "public class T234brwnc2 {}\n" +
			    "    \n",
		"Team234brwnc2_1.java",
			    "\n" +
			    "public team class Team234brwnc2_1 {\n" +
			    "    public class Role234brwnc2_1 {\n" +
			    "        public Role234brwnc2_1(String value) {}\n" +
			    "	public Role234brwnc2_1() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.4.2(b)");
    }

    // a role directly bound to a baseclass has a constructor with a base constructor call
    // 2.3.4-otjld-bound-role-with-noarg-constructor-3
    public void test234_boundRoleWithNoargConstructor3() {
       
       runConformTest(
            new String[] {
		"Team234brwnc3.java",
			    "\n" +
			    "public team class Team234brwnc3 {\n" +
			    "    public class Role234brwnc3 playedBy T234brwnc3 {\n" +
			    "        public Role234brwnc3() {\n" +
			    "            base(\"OK\");\n" +
			    "        }\n" +
			    "	protected abstract String getValue();\n" +
			    "	getValue -> getValue;\n" +
			    "    }\n" +
			    "    Team234brwnc3() {\n" +
			    "    	Role234brwnc3 r = new Role234brwnc3();\n" +
			    "	System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "    	new Team234brwnc3();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T234brwnc3.java",
			    "\n" +
			    "public class T234brwnc3 { \n" +
			    "	private String val;\n" +
			    "	T234brwnc3(String v) { val = v; }\n" +
			    "	String getValue() {\n" +
			    "		return val;\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role indirectly bound to a baseclass has a no-arg constructor
    // 2.3.4-otjld-bound-role-with-noarg-constructor-4
    public void test234_boundRoleWithNoargConstructor4() {
       
       runConformTest(
            new String[] {
		"Team234brwnc4_2.java",
			    "\n" +
			    "public team class Team234brwnc4_2 extends Team234brwnc4_1 {\n" +
			    "    public class Role234brwnc4_2 extends Role234brwnc4_1 {\n" +
			    "        public Role234brwnc4_2() {\n" +
			    "            super(\"OK\");\n" +
			    "        }\n" +
			    "	public Role234brwnc4_2(int i) {\n" +
			    "	    this();\n" +
			    "	}\n" +
			    "    }\n" +
			    "    Team234brwnc4_2 () {\n" +
			    "    	Role234brwnc4_2 r = new Role234brwnc4_2(1);\n" +
			    "	System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "    	new Team234brwnc4_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T234brwnc4.java",
			    "\n" +
			    "public class T234brwnc4 {\n" +
			    "	private String value;\n" +
			    "	T234brwnc4(String val) { \n" +
			    "		value = val;\n" +
			    "	}\n" +
			    "	String getValue() { return value; }\n" +
			    "}\n" +
			    "    \n",
		"Team234brwnc4_1.java",
			    "\n" +
			    "public team class Team234brwnc4_1 {\n" +
			    "    public class Role234brwnc4_1 playedBy T234brwnc4 {\n" +
			    "        public Role234brwnc4_1(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "	protected abstract String getValue();\n" +
			    "	getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role cannot be bound because super class has no argumentless constructor (needed for lifting)
    // 2.3.4-otjld-bound-role-with-noarg-constructor-5
    public void test234_boundRoleWithNoargConstructor5() {
        runNegativeTestMatching(
            new String[] {
		"Team234brwnc5_2.java",
			    "\n" +
			    "public team class Team234brwnc5_2 extends Team234brwnc5_1 {\n" +
			    "    public class Role234brwnc5_2 extends Role234brwnc5_1 playedBy T234brwnc5 {\n" +
			    "        public Role234brwnc5_2() {\n" +
			    "            this(\"OK\");\n" +
			    "        }\n" +
			    "        public Role234brwnc5_2(String val) { \n" +
			    "            super(val);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T234brwnc5.java",
			    "\n" +
			    "public class T234brwnc5 {}\n" +
			    "    \n",
		"Team234brwnc5_1.java",
			    "\n" +
			    "public team class Team234brwnc5_1 {\n" +
			    "    public class Role234brwnc5_1 {\n" +
			    "        public Role234brwnc5_1(String value) {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.3.1(c)");
    }

    // a role directly bound to a baseclass has a constructor with a base constructor call
    // 2.3.5-otjld-constructor-with-basecall-1
    public void test235_constructorWithBasecall1() {
       
       runConformTest(
            new String[] {
		"T235cwb1Main.java",
			    "\n" +
			    "public class T235cwb1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team235cwb1 t = new Team235cwb1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T235cwb1.java",
			    "\n" +
			    "public class T235cwb1 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public T235cwb1(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    protected String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team235cwb1.java",
			    "\n" +
			    "public team class Team235cwb1 {\n" +
			    "    public class Role235cwb1 playedBy T235cwb1 {\n" +
			    "        public Role235cwb1(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role235cwb1(\"OK\").getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role indirectly bound to a baseclass has a constructor with a base constructor call; base() implicitly includes calling the lift-ctor
    // 2.3.5-otjld-constructor-with-basecall-2
    public void test235_constructorWithBasecall2() {
       
       runConformTest(
            new String[] {
		"T235cwb2Main.java",
			    "\n" +
			    "public class T235cwb2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team235cwb2_2 t = new Team235cwb2_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T235cwb2_1.java",
			    "\n" +
			    "public class T235cwb2_1 {\n" +
			    "    protected String value = \"NOTOK\";\n" +
			    "\n" +
			    "    protected String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T235cwb2_2.java",
			    "\n" +
			    "public class T235cwb2_2 extends T235cwb2_1 {\n" +
			    "    public T235cwb2_2(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team235cwb2_1.java",
			    "\n" +
			    "public team class Team235cwb2_1 {\n" +
			    "    public class Role235cwb2_1 playedBy T235cwb2_1 {}\n" +
			    "    public class Role235cwb2_2 extends Role235cwb2_1 playedBy T235cwb2_2 {}\n" +
			    "}\n" +
			    "    \n",
		"Team235cwb2_2.java",
			    "\n" +
			    "public team class Team235cwb2_2 extends Team235cwb2_1 {\n" +
			    "    public class Role235cwb2_3 extends Role235cwb2_2 {\n" +
			    "        protected Role235cwb2_3(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role235cwb2_3(\"OK\").getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role indirectly bound to a baseclass invokes a base constructor using method invocation conversion
    // 2.3.5-otjld-constructor-with-basecall-3
    public void test235_constructorWithBasecall3() {
       
       runConformTest(
            new String[] {
		"T235cwb3Main.java",
			    "\n" +
			    "public class T235cwb3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team235cwb3 t = new Team235cwb3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T235cwb3.java",
			    "\n" +
			    "public class T235cwb3 {\n" +
			    "    protected long value = 0;\n" +
			    "    T235cwb3(long value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "    public long getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team235cwb3.java",
			    "\n" +
			    "public team class Team235cwb3 {\n" +
			    "    public class Role235cwb3 playedBy T235cwb3 {\n" +
			    "        public Role235cwb3(int value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public abstract long getValue();\n" +
			    "        long getValue() -> long getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public long getValue() {\n" +
			    "        return new Role235cwb3(1).getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1");
    }

    // a role directly bound to a baseclass has a constructor with a base constructor call
    // constructors declare an exception
    // Bug 322723 -  [compiler] a role constructor with declared exceptions causes bogus compile error
    public void test235_constructorWithBasecall4() {
       
       runConformTest(
            new String[] {
		"T235cwb4Main.java",
			    "\n" +
			    "public class T235cwb4Main {\n" +
			    "    public static void main(String[] args) throws Exception {\n" +
			    "        Team235cwb4 t = new Team235cwb4();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T235cwb4.java",
			    "\n" +
			    "public class T235cwb4 {\n" +
			    "    private String value;\n" +
			    "	 public T235cwb4() {}\n" +
			    "    public T235cwb4(String value) throws Exception {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    protected String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team235cwb4.java",
			    "\n" +
			    "public team class Team235cwb4 {\n" +
			    "    public class Role235cwb4 playedBy T235cwb4 {\n" +
			    "        public Role235cwb4(String value) throws Exception {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() throws Exception {\n" +
			    "        return new Role235cwb4(\"OK\").getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // Bug 323862 -  base constructor call can not be used as an expression
    public void test236_constructorWithBasecall5() {
    	runConformTest(
    		new String[]{
    	"Team236cwb5.java",
    			"public team class Team236cwb5 {\n" +
    			"    protected class R1 playedBy T236cwb5 {\n" +
    			"        protected String getVal() -> get String val;\n" +
    			"    }\n" +
    			"    protected class R2 extends R1 {\n" +
    			"        protected R2(String v) {\n" +
    			"            super(base(v));\n" +
    			"        }\n" +
    			"    }\n" +
    			"    void test() {\n" +
    			"        R1 r = new R2(\"OK\");\n" +
    			"        System.out.print(r.getVal());\n" +
    			"    }\n" +
    			"    public static void main(String... args) {\n" +
    			"        new Team236cwb5().test();\n" +
    			"    }\n" +
    			"}\n",
    	"T236cwb5.java",
    			"public class T236cwb5 {\n" +
    			"    String val;\n" +
    			"    T236cwb5(String v) { this.val = v; }\n" +
    			"}\n"
    		},
    		"OK");
    }

    // Bug 323862 -  base constructor call can not be used as an expression
    public void test236_constructorWithBasecall6() {
    	runNegativeTest(
    		new String[]{
    	"Team236cwb6.java",
    			"public team class Team236cwb6 {\n" +
    			"    protected class R1 {\n" +
    			"    }\n" +
    			"    protected class R2 extends R1 playedBy T236cwb6 {\n" +
    			"        protected String getVal() -> get String val;\n" +
    			"        protected R2(String v) {\n" +
    			"            super();\n" +
    			"            Object b = base(v);\n" +
    			"        }\n" +
    			"    }\n" +
    			"    void test() {\n" +
    			"        R2 r = new R2(\"OK\");\n" +
    			"        System.out.print(r.getVal());\n" +
    			"    }\n" +
    			"    public static void main(String... args) {\n" +
    			"        new Team236cwb6().test();\n" +
    			"    }\n" +
    			"}\n",
    	"T236cwb6.java",
    			"public class T236cwb6 {\n" +
    			"    String val;\n" +
    			"    T236cwb6(String v) { this.val = v; }\n" +
    			"}\n"
    		},
    		"----------\n" + 
    		"1. ERROR in Team236cwb6.java (at line 8)\n" + 
    		"	Object b = base(v);\n" + 
    		"	           ^^^^\n" + 
    		"Base constructor call not allowed in this position, must be first statement or argument to another constructor call (OTJLD 2.4.2(c)).\n" + 
    		"----------\n");
    }

    // 
    public void test236_constructorWithBasecall7() {
    	runConformTest(
    		new String[]{
    	"Team236cwb7.java",
    			"public team class Team236cwb7 {\n" +
    			"    protected class R1 playedBy T236cwb7 {\n" +
    			"        protected String getVal() -> get String val;\n" +
    			"        protected String getV1() { return \"O\"; }\n" +
    			"        protected String getV2() { return \"K\"; }\n" +
    			"    }\n" +
    			"    protected class R2 extends R1 {\n" +
    			"        protected R2(R1 r) throws Exception {\n" +
    			"            super(base(r));\n" +
    			"        }\n" +
    			"    }\n" +
    			"    void test() throws Exception {\n" +
    			"        R1 r = new R2(new R1(new T236cwb7(\"OK\")));\n" +
    			"        System.out.print(r.getVal());\n" +
    			"    }\n" +
    			"    public static void main(String... args) throws Exception {\n" +
    			"        new Team236cwb7().test();\n" +
    			"    }\n" +
    			"}\n",
    	"T236cwb7.java",
    			"public class T236cwb7 {\n" +
    			"    String val;\n" +
    			"    T236cwb7(String val) { this.val = val; }\n" +
    			"    T236cwb7(T236cwb7 other) { this.val = other.val; }\n" +
    			"}\n"
    		},
    		"OK");
    }

    // a role directly bound to a baseclass invokes a nonexisting base constructor
    // 2.3.6-otjld-no-such-base-constructor-1
    public void test236_noSuchBaseConstructor1() {
        runNegativeTestMatching(
            new String[] {
		"Team236nsbc1.java",
			    "\n" +
			    "public team class Team236nsbc1 {\n" +
			    "    public class Role236nsbc1 playedBy T236nsbc1 {\n" +
			    "        public Role236nsbc1(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T236nsbc1.java",
			    "\n" +
			    "public class T236nsbc1 {}\n" +
			    "    \n"
            },
            "undefined");
    }

    // a role indirectly bound to a baseclass invokes a not-visible base constructor
    // 2.3.6-otjld-no-such-base-constructor-2
    public void test236_noSuchBaseConstructor2() {
        runTestExpectingWarnings(
            new String[] {
		"T236nsbc2.java",
			    "\n" +
			    "public class T236nsbc2 {\n" +
			    "    public T236nsbc2() {}\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    private T236nsbc2(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team236nsbc2.java",
			    "\n" +
			    "public team class Team236nsbc2 {\n" +
			    "    public class Role236nsbc2_1 playedBy T236nsbc2 {}\n" +
			    "    public class Role236nsbc2_2 extends Role236nsbc2_1 {\n" +
			    "        public Role236nsbc2_2(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
			"1. WARNING in Team236nsbc2.java (at line 6)\n" +
			"	base(value);\n" +
			"	^^^^^^^^^^^^\n" +
			"Access restriction of constructor T236nsbc2(String) is overridden by this base constructor call (OTJLD 2.4.2(b)).\n" +
			"----------\n");
    }

    // a role indirectly bound to a baseclass invokes a base constructor but the arguments are not method-invocation convertible (JLS 5.3) -- error reporting not very precise, see comment in Scope.getConstructor()
    // 2.3.6-otjld-no-such-base-constructor-3
    public void test236_noSuchBaseConstructor3() {
        runNegativeTestMatching(
            new String[] {
		"Team236nsbc3_2.java",
			    "\n" +
			    "public team class Team236nsbc3_2 extends Team236nsbc3_1 {\n" +
			    "    public class Role236nsbc3_2 extends Role236nsbc3_1 {\n" +
			    "        public Role236nsbc3_2(String value) {\n" +
			    "            // both literals are of type int; see JLS 5.3 for details on this example\n" +
			    "            base(12, 2);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T236nsbc3.java",
			    "\n" +
			    "public class T236nsbc3 {\n" +
			    "    public T236nsbc3() {}\n" +
			    "    public T236nsbc3(byte a, int b) {}\n" +
			    "}\n" +
			    "    \n",
		"Team236nsbc3_1.java",
			    "\n" +
			    "public team class Team236nsbc3_1 {\n" +
			    "    public class Role236nsbc3_1 playedBy T236nsbc3 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "undefined");
    }

    // a role directly bound to a baseclass has a constructor with a this call refering to another constructor with a base constructor call
    // 2.3.7-otjld-self-and-base-calls-1
    public void test237_selfAndBaseCalls1() {
       
       runConformTest(
            new String[] {
		"T237sabc1Main.java",
			    "\n" +
			    "public class T237sabc1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team237sabc1 t = new Team237sabc1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T237sabc1.java",
			    "\n" +
			    "public class T237sabc1 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    protected T237sabc1(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team237sabc1.java",
			    "\n" +
			    "public team class Team237sabc1 {\n" +
			    "    public class Role237sabc1 playedBy T237sabc1 {\n" +
			    "        public Role237sabc1(char value) {\n" +
			    "            this(String.valueOf(value));\n" +
			    "        }\n" +
			    "        private Role237sabc1(String value) {\n" +
			    "            base(value + \"K\");\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role237sabc1('O').getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role with an explicit bound superrole class has a constructor with a super call refering to a super constructor with a base constructor call
    // 2.3.7-otjld-self-and-base-calls-2
    public void test237_selfAndBaseCalls2() {
       
       runConformTest(
            new String[] {
		"T237sabc2Main.java",
			    "\n" +
			    "public class T237sabc2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team237sabc2 t = new Team237sabc2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T237sabc2.java",
			    "\n" +
			    "public class T237sabc2 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    T237sabc2(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team237sabc2.java",
			    "\n" +
			    "public team class Team237sabc2 {\n" +
			    "    public class Role237sabc2_1 playedBy T237sabc2 {\n" +
			    "        protected Role237sabc2_1(String value) {\n" +
			    "            base(value + \"K\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role237sabc2_2 extends Role237sabc2_1 {\n" +
			    "        public Role237sabc2_2(char value) {\n" +
			    "            super(String.valueOf(value));\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role237sabc2_2('O').getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role with an explicit non-role super class has a constructor with a super call followed by a base call
    // 2.3.7-otjld-self-and-base-calls-3
    public void test237_selfAndBaseCalls3() {
       
       runConformTest(
            new String[] {
		"T237sabc3Main.java",
			    "\n" +
			    "public class T237sabc3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team237sabc3 t = new Team237sabc3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T237sabc3_1.java",
			    "\n" +
			    "public class T237sabc3_1 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public T237sabc3_1(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T237sabc3_2.java",
			    "\n" +
			    "public class T237sabc3_2 {\n" +
			    "    protected char charValue;\n" +
			    "\n" +
			    "    public T237sabc3_2(char value) {\n" +
			    "        charValue = value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team237sabc3.java",
			    "\n" +
			    "public team class Team237sabc3 {\n" +
			    "    public class Role237sabc3 extends T237sabc3_2 playedBy T237sabc3_1 {\n" +
			    "        Role237sabc3(T237sabc3_1 b) {\n" +
			    "            super('_');\n" +
			    "            throw new RuntimeException(\"Lifting of to this class not allowed\");\n" +
			    "        }\n" +
			    "        protected Role237sabc3(String value) {\n" +
			    "            super(value.charAt(0));\n" +
			    "            base(value.substring(1));\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- String.valueOf(charValue) + result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role237sabc3(\"OK\").getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role with an explicit superrole class bound to a different baseclass, has a constructor with a base call
    // 2.3.7-otjld-self-and-base-calls-4
    public void test237_selfAndBaseCalls4() {
       
       runConformTest(
            new String[] {
		"T237sabc4Main.java",
			    "\n" +
			    "public class T237sabc4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team237sabc4 t = new Team237sabc4();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T237sabc4_1.java",
			    "\n" +
			    "public class T237sabc4_1 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public T237sabc4_1(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T237sabc4_2.java",
			    "\n" +
			    "public class T237sabc4_2 extends T237sabc4_1 {\n" +
			    "    protected char charValue;\n" +
			    "\n" +
			    "    public T237sabc4_2(String value) {\n" +
			    "        super(value);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team237sabc4.java",
			    "\n" +
			    "public team class Team237sabc4 {\n" +
			    "    public class Role237sabc4_1 playedBy T237sabc4_1 {\n" +
			    "        public Role237sabc4_1(String value) {\n" +
			    "            base(\"NOTOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role237sabc4_2 extends Role237sabc4_1 playedBy T237sabc4_2 {\n" +
			    "        public Role237sabc4_2(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role237sabc4_2(\"OK\").getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role with an explicit superrole class bound to a different baseclass, has a constructor with a super call
    // 2.3.9-otjld-illegal-super-call
    public void test239_illegalSuperCall() {
        runNegativeTest(
            new String[] {
		"T239isc_1.java",
			    "\n" +
			    "public class T239isc_1 {\n" +
			    "    public T239isc_1(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"T239isc_2.java",
			    "\n" +
			    "public class T239isc_2 extends T239isc_1 {\n" +
			    "    public T239isc_2(String value) {\n" +
			    "        super(value);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team239isc.java",
			    "\n" +
			    "public team class Team239isc {\n" +
			    "    public class Role239isc_1 playedBy T239isc_1 {\n" +
			    "        public Role239isc_1(String value) {\n" +
			    "            base(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role239isc_2 extends Role239isc_1 playedBy T239isc_2 {\n" +
			    "        public Role239isc_2(String value) {\n" +
			    "            super(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with an explicit super role class, has a constructor with a super call to an undefined super constructor
    // 2.3.10-otjld-inaccessible-super-constructor-1
    public void test2310_inaccessibleSuperConstructor1() {
        runNegativeTestMatching(
            new String[] {
		"Team2310isc1.java",
			    "\n" +
			    "public team class Team2310isc1 {\n" +
			    "    public class Role2310isc1_1 playedBy T2310isc1 {\n" +
			    "        private Role2310isc1_1(String value) {\n" +
			    "            base(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role2310isc1_2 extends Role2310isc1_1 {\n" +
			    "        public Role2310isc1_2(String value) {\n" +
			    "            super(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2310isc1.java",
			    "\n" +
			    "public class T2310isc1 {\n" +
			    "    public T2310isc1(String value) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "not visible");
    }

    // a role with an implicit super role class, has a constructor with a super call to a private tsuper constructor
    // 2.3.10-otjld-inaccessible-super-constructor-1t
    public void test2310_inaccessibleSuperConstructor1t() {
       
       runConformTest(
            new String[] {
		"Team2310isc1t_2.java",
			    "\n" +
			    "public team class Team2310isc1t_2 extends Team2310isc1t_1 {\n" +
			    "    public class Role2310isc1t {\n" +
			    "        public Role2310isc1t(String value) {\n" +
			    "            tsuper(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team2310isc1t_2() {\n" +
			    "        new Role2310isc1t(\"NOK\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2310isc1t_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2310isc1t.java",
			    "\n" +
			    "public class T2310isc1t {\n" +
			    "    public T2310isc1t(String value) {\n" +
			    "        System.out.print(value);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2310isc1t_1.java",
			    "\n" +
			    "public team class Team2310isc1t_1 {\n" +
			    "    public class Role2310isc1t playedBy T2310isc1t {\n" +
			    "        private Role2310isc1t(String value) {\n" +
			    "            base(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role with an explicit super class, has a constructor with a super call to an inaccessible super constructor
    // 2.3.10-otjld-inaccessible-super-constructor-2
    public void test2310_inaccessibleSuperConstructor2() {
        runNegativeTest(
            new String[] {
		"p1/T2310isc2.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T2310isc2 {\n" +
			    "    T2310isc2(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"p2/Team2310isc2.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team2310isc2 {\n" +
			    "    public class Role2310isc2 extends p1.T2310isc2 {\n" +
			    "        public Role2310isc2(String value) {\n" +
			    "            super(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with no explicit super class, has a constructor with a super call
    // 2.3.10-otjld-inaccessible-super-constructor-3
    public void test2310_inaccessibleSuperConstructor3() {
        runNegativeTest(
            new String[] {
		"Team2310isc3.java",
			    "\n" +
			    "public team class Team2310isc3 {\n" +
			    "    public class Role2310isc3 {\n" +
			    "        public Role2310isc3(String value) {\n" +
			    "            super(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with no explicit super class, has a constructor with a super call
    // 2.3.10-otjld-inaccessible-super-constructor-4
    public void test2310_inaccessibleSuperConstructor4() {
        runNegativeTest(
            new String[] {
		"Team2310isc4_1.java",
			    "\n" +
			    "public team class Team2310isc4_1 {\n" +
			    "    public class Role2310isc4 {\n" +
			    "        public Role2310isc4(String value) {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2310isc4_2.java",
			    "\n" +
			    "public team class Team2310isc4_2 extends Team2310isc4_1 {\n" +
			    "    public class Role2310isc4 {\n" +
			    "        public Role2310isc4(String value) {\n" +
			    "            super(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with an explicit unbound super role class has a constructor with a super call but without a following base call
    // 2.3.11-otjld-missing-base-call-1
    public void test2311_missingBaseCall1() {
        runNegativeTestMatching(
            new String[] {
		"Team2311mbc1.java",
			    "\n" +
			    "public team class Team2311mbc1 {\n" +
			    "    public class Role2311mbc1_1 {\n" +
			    "        public Role2311mbc1_1(String value) {}\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role2311mbc1_2 extends Role2311mbc1_1 playedBy T2311mbc1 {\n" +
			    "        public Role2311mbc1_2(T2311mbc1 b) { // this is a lifting constructor\n" +
			    "            super(\"OK\"); \n" +
			    "        }\n" +
			    "        public Role2311mbc1_2() { // not a lifting ctor, needs a base(String) call.\n" +
			    "            super(\"NOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2311mbc1.java",
			    "\n" +
			    "public class T2311mbc1 {\n" +
			    "    public T2311mbc1(String value) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "2.4.2(b)");
    }

    // a role with an explicit unbound super role class has a constructor with a super call but without a following base call - also lift constructor cannot be generated
    // 2.3.11-otjld-missing-base-call-1f
    public void test2311_missingBaseCall1f() {
        runNegativeTestMatching(
            new String[] {
		"Team2311mbc1f.java",
			    "\n" +
			    "public team class Team2311mbc1f {\n" +
			    "    public class Role2311mbc1f_1 {\n" +
			    "        public Role2311mbc1f_1(String value) {}\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role2311mbc1f_2 extends Role2311mbc1f_1 playedBy T2311mbc1f {\n" +
			    "        public Role2311mbc1f_2(String value) {\n" +
			    "            super(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2311mbc1f.java",
			    "\n" +
			    "public class T2311mbc1f {\n" +
			    "    public T2311mbc1f(String value) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "2.3.1(c)");
    }

    // a role with an explicit non-role super class has a constructor with a super call but without a following base call
    // 2.3.11-otjld-missing-base-call-2
    public void test2311_missingBaseCall2() {
        runNegativeTest(
            new String[] {
		"T2311mbc2_1.java",
			    "\n" +
			    "public class T2311mbc2_1 {\n" +
			    "    public T2311mbc2_1(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"T2311mbc2_2.java",
			    "\n" +
			    "public class T2311mbc2_2 {\n" +
			    "    public T2311mbc2_2(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team2311mbc2.java",
			    "\n" +
			    "public team class Team2311mbc2 {\n" +
			    "    public class Role2311mbc2 extends T2311mbc2_2 playedBy T2311mbc2_1 {\n" +
			    "        public Role2311mbc2(String value) {\n" +
			    "            super(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with an explicit non-role super class has a constructor that refers via this to another constructor that has a super call
    // 2.3.12-otjld-indirect-super-call-1
    public void test2312_indirectSuperCall1() {
       
       runConformTest(
            new String[] {
		"T2312isc1Main.java",
			    "\n" +
			    "public class T2312isc1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team2312isc1 t = new Team2312isc1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2312isc1_1.java",
			    "\n" +
			    "public class T2312isc1_1 {\n" +
			    "    protected String value;\n" +
			    "\n" +
			    "    protected T2312isc1_1(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2312isc1_2.java",
			    "\n" +
			    "public class T2312isc1_2 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    protected T2312isc1_2(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2312isc1.java",
			    "\n" +
			    "public team class Team2312isc1 {\n" +
			    "    public class Role2312isc1 extends T2312isc1_1 playedBy T2312isc1_2 {\n" +
			    "        protected Role2312isc1(T2312isc1_2 b) {\n" +
			    "            super(\"illegal\");\n" +
			    "            throw new RuntimeException(\"Don't lift this role\");\n" +
			    "        }\n" +
			    "        public Role2312isc1(String value) {\n" +
			    "            this(value, \"K\");\n" +
			    "        }\n" +
			    "        private Role2312isc1(String value1, String value2) {\n" +
			    "            super(value1);\n" +
			    "            base(value2);\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- value + result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role2312isc1(\"O\").getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role with an explicit super role class has a constructor that refers via this to another constructor that has a super call
    // 2.3.12-otjld-indirect-super-call-2
    public void test2312_indirectSuperCall2() {
       
       runConformTest(
            new String[] {
		"T2312isc2Main.java",
			    "\n" +
			    "public class T2312isc2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team2312isc2_2 t = new Team2312isc2_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2312isc2.java",
			    "\n" +
			    "public class T2312isc2 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    protected T2312isc2(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2312isc2_1.java",
			    "\n" +
			    "public team class Team2312isc2_1 {\n" +
			    "    public class Role2312isc2_1 playedBy T2312isc2 {\n" +
			    "        protected Role2312isc2_1(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2312isc2_2.java",
			    "\n" +
			    "public team class Team2312isc2_2 extends Team2312isc2_1 {\n" +
			    "    public class Role2312isc2_2 extends Role2312isc2_1 {\n" +
			    "        public Role2312isc2_2(String value) {\n" +
			    "            this(value, \"K\");\n" +
			    "        }\n" +
			    "        private Role2312isc2_2(String value1, String value2) {\n" +
			    "            super(value1+value2);\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role2312isc2_2(\"O\").getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the test 2.3.12-2 originally had this syntax error
    // 2.3.12-otjld-syntax-error-missing-semi-3
    public void test2312_syntaxErrorMissingSemi3() {
        runNegativeTest(
            new String[] {
		"T2312sems3.java",
			    "\n" +
			    "public class T2312sems3 {\n" +
			    "    String getValue() { return null; }\n" +
			    "}\n" +
			    "    \n",
		"Team2312sems3.java",
			    "\n" +
			    "public team class Team2312sems3 {\n" +
			    "    public class Role2312sems3 playedBy T2312sems3 {\n" +
			    "        protected Role2312sems3(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue()\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with an implicit super role class has a constructor that has a tsuper followed by a base
    // 2.3.13-otjld-base-call-after-tsuper-call-1
    public void test2313_baseCallAfterTsuperCall1() {
        runNegativeTest(
            new String[] {
		"T2313bcatc1.java",
			    "\n" +
			    "public class T2313bcatc1 {\n" +
			    "    protected T2313bcatc1(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team2313bcatc1_1.java",
			    "\n" +
			    "public team class Team2313bcatc1_1 {\n" +
			    "    public class Role2313bcatc1 playedBy T2313bcatc1 {\n" +
			    "        protected Role2313bcatc1(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2313bcatc1_2.java",
			    "\n" +
			    "public team class Team2313bcatc1_2 extends Team2313bcatc1_1 {\n" +
			    "    public class Role2313bcatc1 {\n" +
			    "        public Role2313bcatc1(String value) {\n" +
			    "            tsuper(value);\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with an implicit super role class has a constructor that has a tsuper followed by a base
    // 2.3.13-otjld-base-call-after-tsuper-call-2
    public void test2313_baseCallAfterTsuperCall2() {
        runNegativeTest(
            new String[] {
		"T2313bcatc2.java",
			    "\n" +
			    "public class T2313bcatc2 {}\n" +
			    "    \n",
		"Team2313bcatc2_1.java",
			    "\n" +
			    "public team class Team2313bcatc2_1 {\n" +
			    "    public class Role2313bcatc2 playedBy T2313bcatc2 {\n" +
			    "        Role2313bcatc2(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2313bcatc2_2.java",
			    "\n" +
			    "public team class Team2313bcatc2_2 extends Team2313bcatc2_1 {}\n" +
			    "    \n",
		"Team2313bcatc2_3.java",
			    "\n" +
			    "public team class Team2313bcatc2_3 extends Team2313bcatc2_2 {\n" +
			    "    public class Role2313bcatc2 playedBy T2313bcatc2 {\n" +
			    "        public Role2313bcatc2(String value1, String value2) {\n" +
			    "            tsuper(value1+value2);\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with an implicit super role class has a constructor that references an undefined super constructor via tsuper
    // 2.3.14-otjld-inaccessible-tsuper-constructor-1
    public void test2314_inaccessibleTsuperConstructor1() {
        runNegativeTest(
            new String[] {
		"T2314itc1.java",
			    "\n" +
			    "public class T2314itc1 {\n" +
			    "    protected T2314itc1(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team2314itc1_1.java",
			    "\n" +
			    "public team class Team2314itc1_1 {\n" +
			    "    public class Role2314itc1 playedBy T2314itc1 {\n" +
			    "        public Role2314itc1(String value1, String value2) {\n" +
			    "            base(value1 + value2);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2314itc1_2.java",
			    "\n" +
			    "public team class Team2314itc1_2 extends Team2314itc1_1 {\n" +
			    "    public class Role2314itc1 {\n" +
			    "        public Role2314itc1(String value) {\n" +
			    "            tsuper(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with an implicit super role class has a constructor that references a private (accessible!) super constructor via tsuper
    // 2.3.14-otjld-inaccessible-tsuper-constructor-2
    public void test2314_inaccessibleTsuperConstructor2() {
       
       runConformTest(
            new String[] {
		"Team2314itc2_2.java",
			    "\n" +
			    "public team class Team2314itc2_2 extends Team2314itc2_1 {\n" +
			    "    public class Role2314itc2 {\n" +
			    "        public Role2314itc2(String value) {\n" +
			    "            tsuper(value);\n" +
			    "            System.out.print(\"!\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team2314itc2_2 t = new Team2314itc2_2();\n" +
			    "        t.new Role2314itc2(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2314itc2.java",
			    "\n" +
			    "public class T2314itc2 {\n" +
			    "    protected T2314itc2(String value) { System.out.print(value); }\n" +
			    "}\n" +
			    "    \n",
		"Team2314itc2_1.java",
			    "\n" +
			    "public team class Team2314itc2_1 {\n" +
			    "    public class Role2314itc2 playedBy T2314itc2 {\n" +
			    "        private Role2314itc2(String value) {\n" +
			    "            base(value);\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!");
    }

    // trying to assign role with non-final local as anchor
    // 2.3.14-otjld-inaccessible-tsuper-constructor-2l
    public void test2314_inaccessibleTsuperConstructor2l() {
        runNegativeTestMatching(
            new String[] {
		"Team2314itc2l_2.java",
			    "\n" +
			    "public team class Team2314itc2l_2 extends Team2314itc2l_1 {\n" +
			    "    public class Role2314itc2l {\n" +
			    "        public Role2314itc2l(String value) {\n" +
			    "            tsuper(value);\n" +
			    "	    System.out.print(\"!\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "    	Team2314itc2l_2 t = new Team2314itc2l_2();\n" +
			    "	Role2314itc2l<@t> r = t.new Role2314itc2l(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2314itc2l.java",
			    "\n" +
			    "public class T2314itc2l {\n" +
			    "    protected T2314itc2l(String value) { System.out.print(value); }\n" +
			    "}\n" +
			    "    \n",
		"Team2314itc2l_1.java",
			    "\n" +
			    "public team class Team2314itc2l_1 {\n" +
			    "    public class Role2314itc2l playedBy T2314itc2l {\n" +
			    "        private Role2314itc2l(String value) {\n" +
			    "            base(value);\n" +
			    "	    System.out.print(\"K\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.2(c)");
    }

    // trying to assign role with non-final field as anchor
    // 2.3.14-otjld-inaccessible-tsuper-constructor-2f
    public void test2314_inaccessibleTsuperConstructor2f() {
        runNegativeTestMatching(
            new String[] {
		"Team2314itc2f_2.java",
			    "\n" +
			    "public team class Team2314itc2f_2 extends Team2314itc2f_1 {\n" +
			    "    public class Role2314itc2f {\n" +
			    "        public Role2314itc2f(String value) {\n" +
			    "            tsuper(value);\n" +
			    "            System.out.print(\"!\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    private static Team2314itc2f_2 t = new Team2314itc2f_2();\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Role2314itc2f<@t> r = t.new Role2314itc2f(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2314itc2f.java",
			    "\n" +
			    "public class T2314itc2f {\n" +
			    "    protected T2314itc2f(String value) { System.out.print(value); }\n" +
			    "}\n" +
			    "    \n",
		"Team2314itc2f_1.java",
			    "\n" +
			    "public team class Team2314itc2f_1 {\n" +
			    "    public class Role2314itc2f playedBy T2314itc2f {\n" +
			    "        private Role2314itc2f(String value) {\n" +
			    "            base(value);\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.2(c)");
    }

    // a role with an implicit super role class has a constructor that references a super constructor via tsuper but the arguments are not method-invocation convertible
    // 2.3.14-otjld-inaccessible-tsuper-constructor-3
    public void test2314_inaccessibleTsuperConstructor3() {
        runNegativeTest(
            new String[] {
		"T2314itc3.java",
			    "\n" +
			    "public class T2314itc3 {\n" +
			    "    public T2314itc3() {}\n" +
			    "}\n" +
			    "    \n",
		"Team2314itc3_1.java",
			    "\n" +
			    "public team class Team2314itc3_1 {\n" +
			    "    public class Role2314itc3 playedBy T2314itc3 {\n" +
			    "        public Role2314itc3(long value1, short value2) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2314itc3_2.java",
			    "\n" +
			    "public team class Team2314itc3_2 extends Team2314itc3_1 {}\n" +
			    "    \n",
		"Team2314itc3_3.java",
			    "\n" +
			    "public team class Team2314itc3_3 extends Team2314itc3_2 {\n" +
			    "    public class Role2314itc3 playedBy T2314itc3 {\n" +
			    "        public Role2314itc3(int value) {\n" +
			    "            tsuper(value, value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role with an implicit super role class has a constructor that refers via tsuper to a super constructor that in turn uses a base call
    // 2.3.15-otjld-tsuper-call-1
    public void test2315_tsuperCall1() {
       
       runConformTest(
            new String[] {
		"T2315tc1Main.java",
			    "\n" +
			    "public class T2315tc1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team2315tc1_2 t = new Team2315tc1_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2315tc1.java",
			    "\n" +
			    "public class T2315tc1 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    T2315tc1(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2315tc1_1.java",
			    "\n" +
			    "public team class Team2315tc1_1 {\n" +
			    "    public class Role2315tc1 playedBy T2315tc1 {\n" +
			    "        protected Role2315tc1(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2315tc1_2.java",
			    "\n" +
			    "public team class Team2315tc1_2 extends Team2315tc1_1 {\n" +
			    "    public class Role2315tc1 {\n" +
			    "        public Role2315tc1(String value) {\n" +
			    "            tsuper(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role2315tc1(\"OK\").getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role with implicit and explicit super role classes using constructors with this, tsuper, super, and base
    // 2.3.15-otjld-tsuper-call-2
    public void test2315_tsuperCall2() {
       
       runConformTest(
            new String[] {
		"T2315tc2Main.java",
			    "\n" +
			    "public class T2315tc2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team2315tc2_2 t = new Team2315tc2_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2315tc2.java",
			    "\n" +
			    "public class T2315tc2 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public T2315tc2(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2315tc2_1.java",
			    "\n" +
			    "public team class Team2315tc2_1 {\n" +
			    "    public class Role2315tc2_1 playedBy T2315tc2 {\n" +
			    "        protected Role2315tc2_1(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role2315tc2_2 extends Role2315tc2_1 {\n" +
			    "        protected Role2315tc2_2(String value) {\n" +
			    "            super(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2315tc2_2.java",
			    "\n" +
			    "public team class Team2315tc2_2 extends Team2315tc2_1 {\n" +
			    "    public class Role2315tc2_2 playedBy T2315tc2 {\n" +
			    "        public Role2315tc2_2(String value) {\n" +
			    "            this(value, \"K\");\n" +
			    "        }\n" +
			    "        public Role2315tc2_2(String value1, String value2) {\n" +
			    "            tsuper(value1+value2);\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role2315tc2_2(\"O\").getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role with an implicit super role class has a constructor that refres via this to another constructor with a tsuper call, follwed by a base call
    // 2.3.16-otjld-illegal-base-call-after-this
    public void test2316_illegalBaseCallAfterThis() {
        runNegativeTest(
            new String[] {
		"T2316ibcat.java",
			    "\n" +
			    "public class T2316ibcat {\n" +
			    "    public T2316ibcat(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team2316ibcat_1.java",
			    "\n" +
			    "public team class Team2316ibcat_1 {\n" +
			    "    public class Role2316ibcat playedBy T2316ibcat {\n" +
			    "        public Role2316ibcat(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2316ibcat_2.java",
			    "\n" +
			    "public team class Team2316ibcat_2 extends Team2316ibcat_1 {\n" +
			    "    public class Role2316ibcat {\n" +
			    "        public Role2316ibcat(String value) {\n" +
			    "            this(value, \"K\");\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "        public Role2316ibcat(String value1, String value2) {\n" +
			    "            tsuper(value1+value2);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an unbound role has two constructors referencing each other
    // 2.3.17-otjld-circular-this-reference-1
    public void test2317_circularThisReference1() {
        runNegativeTest(
            new String[] {
		"Team2317ctr1.java",
			    "\n" +
			    "public team class Team2317ctr1 {\n" +
			    "    public class Role2317ctr1 {\n" +
			    "        public Role2317ctr1(String value) {\n" +
			    "            this();\n" +
			    "        }\n" +
			    "        private Role2317ctr1() {\n" +
			    "            this(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a bound role has three constructors that are referencing each other
    // 2.3.17-otjld-circular-this-reference-2
    public void test2317_circularThisReference2() {
        runNegativeTest(
            new String[] {
		"T2317ctr2.java",
			    "\n" +
			    "public class T2317ctr2 {\n" +
			    "    public T2317ctr2(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team2317ctr2.java",
			    "\n" +
			    "public team class Team2317ctr2 {\n" +
			    "    public class Role2317ctr2 playedBy T2317ctr2 {\n" +
			    "        public Role2317ctr2(int value) {\n" +
			    "            this((double)value);\n" +
			    "        }\n" +
			    "        public Role2317ctr2(double value) {\n" +
			    "            this(Double.toString(value));\n" +
			    "        }\n" +
			    "        public Role2317ctr2(String value) {\n" +
			    "            this(Integer.valueOf(value).intValue());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a call to a base constructor is not the first statement in a constructor of a bound role
    // 2.3.18-otjld-constructor-call-not-first-statement-1
    public void test2318_constructorCallNotFirstStatement1() {
        runNegativeTest(
            new String[] {
		"T2318ccnfs1.java",
			    "\n" +
			    "public class T2318ccnfs1 {\n" +
			    "    public T2318ccnfs1(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team2318ccnfs1.java",
			    "\n" +
			    "public team class Team2318ccnfs1 {\n" +
			    "    public class Role2318ccnfs1 playedBy T2318ccnfs1 {\n" +
			    "        public Role2318ccnfs1(double value) {\n" +
			    "            String strValue = Double.toString(value);\n" +
			    "            base(strValue);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a call to a super constructor is not the first statement in a constructor of an unbound role
    // 2.3.18-otjld-constructor-call-not-first-statement-2
    public void test2318_constructorCallNotFirstStatement2() {
        runNegativeTest(
            new String[] {
		"T2318ccnfs2.java",
			    "\n" +
			    "public class T2318ccnfs2 {\n" +
			    "    public T2318ccnfs2(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team2318ccnfs2.java",
			    "\n" +
			    "public team class Team2318ccnfs2 {\n" +
			    "    public class Role2318ccnfs2 extends T2318ccnfs2 {\n" +
			    "        public Role2318ccnfs2(double value) {\n" +
			    "            String strValue = Double.toString(value);\n" +
			    "            super(strValue);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a call to a super constructor is not the first statement in a constructor of an unbound role with an explicit super role class
    // 2.3.18-otjld-constructor-call-not-first-statement-3
    public void test2318_constructorCallNotFirstStatement3() {
        runNegativeTest(
            new String[] {
		"Team2318ccnfs3.java",
			    "\n" +
			    "public team class Team2318ccnfs3 {\n" +
			    "    public class Role2318ccnfs3_1 {\n" +
			    "        public Role2318ccnfs3_1(String value) {}\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role2318ccnfs3_2 extends Role2318ccnfs3_1 {\n" +
			    "        public Role2318ccnfs3_2(double value) {\n" +
			    "            String strValue = Double.toString(value);\n" +
			    "            super(strValue);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a call to a super constructor is not the first statement in a constructor of a bound role with an implicit super role class
    // 2.3.18-otjld-constructor-call-not-first-statement-4
    public void test2318_constructorCallNotFirstStatement4() {
        runNegativeTest(
            new String[] {
		"T2318ccnfs4.java",
			    "\n" +
			    "public class T2318ccnfs4 {\n" +
			    "    public T2318ccnfs4(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team2318ccnfs4_1.java",
			    "\n" +
			    "public team class Team2318ccnfs4_1 {\n" +
			    "    public class Role2318ccnfs4 playedBy T2318ccnfs4 {\n" +
			    "        public Role2318ccnfs4(String value) {\n" +
			    "            base(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2318ccnfs4_2.java",
			    "\n" +
			    "public team class Team2318ccnfs4_2 extends Team2318ccnfs4_1 {\n" +
			    "    public class Role2318ccnfs4 {\n" +
			    "        public Role2318ccnfs4(double value) {\n" +
			    "            String strValue = Double.toString(value);\n" +
			    "            tsuper(strValue);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an unbound role class has a constructor with a base call
    // 2.3.19-otjld-unbound-role-with-base-call
    public void test2319_unboundRoleWithBaseCall() {
        runNegativeTest(
            new String[] {
		"T2319urwbc.java",
			    "\n" +
			    "public class T2319urwbc {\n" +
			    "    public T2319urwbc(String value) {}\n" +
			    "}\n" +
			    "    \n",
		"Team2319urwbc.java",
			    "\n" +
			    "public team class Team2319urwbc {\n" +
			    "    public class Role2319urwbc extends T2319urwbc {\n" +
			    "        public Role2319urwbc(String value) {\n" +
			    "            base(strValue);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-1
    public void test2320_illegalConstructorCall1() {
        runNegativeTest(
            new String[] {
		"T2320icc1.java",
			    "\n" +
			    "public class T2320icc1 {}\n" +
			    "    \n",
		"Team2320icc1.java",
			    "\n" +
			    "public team class Team2320icc1 {\n" +
			    "    public class Role2320icc1 playedBy T2320icc1 {\n" +
			    "        public void test() {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-2
    public void test2320_illegalConstructorCall2() {
        runNegativeTest(
            new String[] {
		"T2320icc2.java",
			    "\n" +
			    "public class T2320icc2 {}\n" +
			    "    \n",
		"Team2320icc2.java",
			    "\n" +
			    "public team class Team2320icc2 {\n" +
			    "    public class Role2320icc2 playedBy T2320icc2 {\n" +
			    "        {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-3
    public void test2320_illegalConstructorCall3() {
        runNegativeTest(
            new String[] {
		"T2320icc3.java",
			    "\n" +
			    "public class T2320icc3 {}\n" +
			    "    \n",
		"Team2320icc3.java",
			    "\n" +
			    "public team class Team2320icc3 {\n" +
			    "    public Team2320icc3() {\n" +
			    "        base();\n" +
			    "    }\n" +
			    "    public class Role2320icc3 playedBy T2320icc3 {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-4
    public void test2320_illegalConstructorCall4() {
        runNegativeTest(
            new String[] {
		"T2320icc4_1.java",
			    "\n" +
			    "public class T2320icc4_1 {}\n" +
			    "    \n",
		"T2320icc4_2.java",
			    "\n" +
			    "public class T2320icc4_2 extends T2320icc4_1 {\n" +
			    "    public T2320icc4_2() {\n" +
			    "        base();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a super constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-5
    public void test2320_illegalConstructorCall5() {
        runNegativeTest(
            new String[] {
		"Team2320icc5.java",
			    "\n" +
			    "public team class Team2320icc5 {\n" +
			    "    public class Role2320icc5_1 {}\n" +
			    "\n" +
			    "    public class Role2320icc5_2 extends Role2320icc5_1 {\n" +
			    "        public void test() {\n" +
			    "            super();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a super constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-6
    public void test2320_illegalConstructorCall6() {
        runNegativeTest(
            new String[] {
		"Team2320icc6.java",
			    "\n" +
			    "public team class Team2320icc6 {\n" +
			    "    public class Role2320icc6_1 {}\n" +
			    "\n" +
			    "    public class Role2320icc6_2 extends Role2320icc6_1 {\n" +
			    "        {\n" +
			    "            super();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a super constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-7
    public void test2320_illegalConstructorCall7() {
        runNegativeTest(
            new String[] {
		"Team2320icc7_1.java",
			    "\n" +
			    "public team class Team2320icc7_1 {}\n" +
			    "    \n",
		"Team2320icc7_2.java",
			    "\n" +
			    "public team class Team2320icc7_2 extends Team2320icc7_1 {\n" +
			    "    public void test() {\n" +
			    "        super();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a super constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-8
    public void test2320_illegalConstructorCall8() {
        runNegativeTest(
            new String[] {
		"Team2320icc8_1.java",
			    "\n" +
			    "public team class Team2320icc8_1 {}\n" +
			    "    \n",
		"Team2320icc8_2.java",
			    "\n" +
			    "public team class Team2320icc8_2 extends Team2320icc8_1 {\n" +
			    "    {\n" +
			    "        super();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a tsuper constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-9
    public void test2320_illegalConstructorCall9() {
        runNegativeTest(
            new String[] {
		"Team2320icc9_1.java",
			    "\n" +
			    "public team class Team2320icc9_1 {\n" +
			    "    public class Role2320icc9 {}\n" +
			    "}\n" +
			    "    \n",
		"Team2320icc9_2.java",
			    "\n" +
			    "public team class Team2320icc9_2 extends Team2320icc9_1 {\n" +
			    "    public class Role2320icc9 {\n" +
			    "        public void test() {\n" +
			    "            tsuper();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a tsuper constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-10
    public void test2320_illegalConstructorCall10() {
        runNegativeTest(
            new String[] {
		"Team2320icc10_1.java",
			    "\n" +
			    "public team class Team2320icc10_1 {\n" +
			    "    public class Role2320icc10 {}\n" +
			    "}\n" +
			    "    \n",
		"Team2320icc10_2.java",
			    "\n" +
			    "public team class Team2320icc10_2 extends Team2320icc10_1 {\n" +
			    "    public class Role2320icc10 {\n" +
			    "        {\n" +
			    "            tsuper();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a tsuper constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-11
    public void test2320_illegalConstructorCall11() {
        runNegativeTest(
            new String[] {
		"Team2320icc11_1.java",
			    "\n" +
			    "public team class Team2320icc11_1 {\n" +
			    "}\n" +
			    "    \n",
		"Team2320icc11_2.java",
			    "\n" +
			    "public team class Team2320icc11_2 extends Team2320icc11_1 {\n" +
			    "    public Team2320icc11_2() {\n" +
			    "        tsuper();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a tsuper constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-12
    public void test2320_illegalConstructorCall12() {
        runNegativeTest(
            new String[] {
		"Team2320icc12_1.java",
			    "\n" +
			    "public team class Team2320icc12_1 {\n" +
			    "}\n" +
			    "    \n",
		"Team2320icc12_2.java",
			    "\n" +
			    "public team class Team2320icc12_2 extends Team2320icc12_1 {\n" +
			    "    public void test() {\n" +
			    "        tsuper();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a tsuper constructor call outside of a role constructor
    // 2.3.20-otjld-illegal-constructor-call-13
    public void test2320_illegalConstructorCall13() {
        runNegativeTest(
            new String[] {
		"Team2320icc13_1.java",
			    "\n" +
			    "public team class Team2320icc13_1 {\n" +
			    "}\n" +
			    "    \n",
		"Team2320icc13_2.java",
			    "\n" +
			    "public team class Team2320icc13_2 extends Team2320icc13_1 {\n" +
			    "    {\n" +
			    "        tsuper();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base constructor call in a local class in a role constructor
    // 2.3.20-otjld-illegal-constructor-call-14
    public void test2320_illegalConstructorCall14() {
        runNegativeTest(
            new String[] {
		"T2320icc14.java",
			    "\n" +
			    "public class T2320icc14 {}\n" +
			    "    \n",
		"Team2320icc14.java",
			    "\n" +
			    "public team class Team2320icc14 {\n" +
			    "    public class Role2320icc14 playedBy T2320icc14 {\n" +
			    "        Role2320icc14(String value1, String value2) {\n" +
			    "            this(value1 + value2);\n" +
			    "            class Local {\n" +
			    "                Local() {\n" +
			    "                    base();\n" +
			    "                }\n" +
			    "            }\n" +
			    "        }\n" +
			    "\n" +
			    "        private Role2320icc14(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a tsuper constructor call in a local class in a role constructor
    // 2.3.20-otjld-illegal-constructor-call-15
    public void test2320_illegalConstructorCall15() {
        runNegativeTest(
            new String[] {
		"Team2320icc15_1.java",
			    "\n" +
			    "public team class Team2320icc15_1 {\n" +
			    "    public class Role2320icc15 {}\n" +
			    "}\n" +
			    "    \n",
		"Team2320icc15_2.java",
			    "\n" +
			    "public team class Team2320icc15_2 extends Team2320icc15_1 {\n" +
			    "    public class Role2320icc15 {\n" +
			    "        public Role2320icc15(String value) {\n" +
			    "            class Local {\n" +
			    "                Local() {\n" +
			    "                    tsuper();\n" +
			    "                }\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base constructor call in a local class in a team
    // 2.3.20-otjld-illegal-constructor-call-16
    public void test2320_illegalConstructorCall16() {
        runNegativeTest(
            new String[] {
		"Team2320icc16.java",
			    "\n" +
			    "public team class Team2320icc16 {\n" +
			    "    public void test() {\n" +
			    "        class Local {\n" +
			    "            Local() {\n" +
			    "                base();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a base constructor call in a local class in a team
    // 2.3.20-otjld-illegal-constructor-call-17
    public void test2320_illegalConstructorCall17() {
        runNegativeTestMatching(
            new String[] {
		"Team2320icc17.java",
			    "\n" +
			    "public team class Team2320icc17 {\n" +
			    "    {\n" +
			    "        class Local {\n" +
			    "            Local() {\n" +
			    "                base();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.4.2");
    }

    // a tsuper constructor call in a local class in a team
    // 2.3.20-otjld-illegal-constructor-call-18
    public void test2320_illegalConstructorCall18() {
        runNegativeTest(
            new String[] {
		"Team2320icc18_1.java",
			    "\n" +
			    "public team class Team2320icc18_1 {}\n" +
			    "    \n",
		"Team2320icc18_2.java",
			    "\n" +
			    "public team class Team2320icc18_2 extends Team2320icc18_1 {\n" +
			    "    public void test() {\n" +
			    "        @SuppressWarnings(\"unused\") class Local {\n" +
			    "            Local() {\n" +
			    "                tsuper();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a tsuper constructor call in a local class in a team
    // 2.3.20-otjld-illegal-constructor-call-19
    public void test2320_illegalConstructorCall19() {
        runNegativeTest(
            new String[] {
		"Team2320icc19_1.java",
			    "\n" +
			    "public team class Team2320icc19_1 {}\n" +
			    "    \n",
		"Team2320icc19_2.java",
			    "\n" +
			    "public team class Team2320icc19_2 extends Team2320icc19_1 {\n" +
			    "    {\n" +
			    "        @SuppressWarnings(\"unused\") class Local {\n" +
			    "            Local() {\n" +
			    "                tsuper();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a unbound role with an explicit superrole has only one constructor which has no super call
    // 2.3.22-otjld_testbug-super-call-missing-1
    public void _testbug_test2322_superCallMissing1() {
        runNegativeTest(
            new String[] {
		"Team2322scm1.java",
			    "\n" +
			    "public team class Team2322scm1 {\n" +
			    "    public class Role2322scm1_1 {}\n" +
			    "\n" +
			    "    public class Role2322scm1_2 extends Role2322scm1_1 {\n" +
			    "        private String attr;\n" +
			    "\n" +
			    "        public Role2322scm1_2(String value) {\n" +
			    "            attr = value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a unbound role with an implicit superrole has only one constructor which has no tsuper call
    // 2.3.22-otjld_testbug-super-call-missing-2
    public void _testbug_test2322_superCallMissing2() {
        runNegativeTest(
            new String[] {
		"Team2322scm2_1.java",
			    "\n" +
			    "public team class Team2322scm2_1 {\n" +
			    "    public class Role2322scm2 {}\n" +
			    "}\n" +
			    "    \n",
		"Team2322scm2_2.java",
			    "\n" +
			    "public team class Team2322scm2_2 extends Team2322scm2_1 {\n" +
			    "    public class Role2322scm2 {\n" +
			    "        private String attr;\n" +
			    "\n" +
			    "        public Role2322scm2(String value) {\n" +
			    "            attr = value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role without an implicit superrole has a constructor that uses tsuper
    // 2.3.23-otjld-tsuper-without-implicit-superclass-1
    public void test2323_tsuperWithoutImplicitSuperclass1() {
        runNegativeTest(
            new String[] {
		"Team2323twis1.java",
			    "\n" +
			    "public team class Team2323twis1 {\n" +
			    "    public class Role2323twis1_1 {\n" +
			    "        public Role2323twis1_1(String value) {}\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role2323twis1_2 extends Role2323twis1_1 {\n" +
			    "        public Role2323twis1_2(String value) {\n" +
			    "            tsuper(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role without an implicit superrole has a constructor that uses tsuper
    // 2.3.23-otjld-tsuper-without-implicit-superclass-2
    public void test2323_tsuperWithoutImplicitSuperclass2() {
        runNegativeTest(
            new String[] {
		"Team2323twis2_1.java",
			    "\n" +
			    "public team class Team2323twis2_1 {\n" +
			    "    public class Role2323twis2_1 {\n" +
			    "        public Role2323twis2_1(String value) {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2323twis2_2.java",
			    "\n" +
			    "public team class Team2323twis2_2 extends Team2323twis2_1 {\n" +
			    "    public class Role2323twis2_2 extends Role2323twis2_1 {\n" +
			    "        public Role2323twis2_2(String value) {\n" +
			    "            tsuper(value);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a method in an unbound role has the name 'base'
    // 2.3.24-otjld-method-with-name-base-1
    public void test2324_methodWithNameBase1() {
        runNegativeTest(
            new String[] {
		"Team2324mwnb1.java",
			    "\n" +
			    "public team class Team2324mwnb1 {\n" +
			    "    public class Role2324mwnb1 {\n" +
			    "        public Role2324mwnb1(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "\n" +
			    "        public void base() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an inherited method in a bound role has the name 'base'
    // 2.3.24-otjld-method-with-name-base-2
    public void test2324_methodWithNameBase2() {
        runNegativeTestMatching(
            new String[] {
		"Team2324mwnb2.java",
			    "\n" +
			    "public team class Team2324mwnb2 {\n" +
			    "    public class Role2324mwnb2 extends T2324mwnb2_1 playedBy T2324mwnb2_2 {\n" +
			    "        public Role2324mwnb2(String value) {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2324mwnb2_1.java",
			    "\n" +
			    "public class T2324mwnb2_1 {\n" +
			    "    public void base() {}\n" +
			    "}\n" +
			    "    \n",
		"T2324mwnb2_2.java",
			    "\n" +
			    "public class T2324mwnb2_2 {}\n" +
			    "    \n"
            },
            "A.0.2");
    }

    // a role object is created in a field initializer of a sibling role
    // 2.3.25-otjld-field-initializer-creates-role-1
    public void test2325_fieldInitializerCreatesRole1() {
       
       runConformTest(
            new String[] {
		"Team2325ficr1.java",
			    "\n" +
			    "public team class Team2325ficr1 {\n" +
			    "	public class Role1 { \n" +
			    "		public String getValue() { \n" +
			    "			return \"OK\";\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public class Role2 {\n" +
			    "		Role1 other = new Role1();\n" +
			    "		public void test() {\n" +
			    "			System.out.print(other.getValue());\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public Team2325ficr1 () {\n" +
			    "		Role2 r = new Role2();\n" +
			    "		r.test();\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team2325ficr1();\n" +
			    "	}\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a qualified allocation uses the lifting ctor
    // 2.3.26-otjld-qualified-use-of-lifting-ctor-1
    public void test2326_qualifiedUseOfLiftingCtor1() {
        runNegativeTestMatching(
            new String[] {
		"Team2326quolc1.java",
			    "\n" +
			    "public team class Team2326quolc1 {\n" +
			    "	public class R playedBy T2326quolc1 {}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		final Team2326quolc1 t = new Team2326quolc1();\n" +
			    "		R<@t> r = t.new R(new T2326quolc1());\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"T2326quolc1.java",
			    "\n" +
			    "public class T2326quolc1 {}	\n" +
			    "	\n"
            },
            "2.4.1(a)");
    }

    // a lifting constructor is used with a new expression as argument
    // 2.3.27-otjld-explicit-use-of-lifting-constructor-1
    public void test2327_explicitUseOfLiftingConstructor1() {
       
       runConformTest(
            new String[] {
		"Team2327euolc1.java",
			    "\n" +
			    "public team class Team2327euolc1 {\n" +
			    "	protected class R playedBy T2327euolc1 {\n" +
			    "		protected abstract void t();\n" +
			    "		t -> test;\n" +
			    "	}\n" +
			    "	Team2327euolc1 () {\n" +
			    "		R r = new R(new T2327euolc1(\"OK\"));\n" +
			    "		r.t();\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team2327euolc1();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"T2327euolc1.java",
			    "\n" +
			    "public class T2327euolc1 {\n" +
			    "	private String val;\n" +
			    "	public T2327euolc1(String v) { val = v; }\n" +
			    "	public void test() { System.out.print(val); }\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a lifting constructor is used - giving a warning
    // 2.3.27-otjld-explicit-use-of-lifting-constructor-2
    public void test2327_explicitUseOfLiftingConstructor2() {
       
       runConformTest(
            new String[] {
		"T2327euolc2Main.java",
			    "\n" +
			    "public class T2327euolc2Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team2327euolc2();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"T2327euolc2.java",
			    "\n" +
			    "public class T2327euolc2 {\n" +
			    "	private String val;\n" +
			    "	public T2327euolc2(String v) { val = v; }\n" +
			    "	public void test() { System.out.print(val); }\n" +
			    "}\n" +
			    "	\n",
		"Team2327euolc2.java",
			    "\n" +
			    "public team class Team2327euolc2 {\n" +
			    "	protected class R playedBy T2327euolc2 {\n" +
			    "		void t() -> void test();\n" +
			    "	}\n" +
			    "	Team2327euolc2 () {\n" +
			    "		T2327euolc2 b = new T2327euolc2(\"OK\");\n" +
			    "		R r = new R(b);\n" +
			    "		r.t();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "OK");
    }

    // a lifting constructor is used - role already exists
    // 2.3.27-otjld-explicit-use-of-lifting-constructor-3
    public void test2327_explicitUseOfLiftingConstructor3() {
       
       runConformTest(
            new String[] {
		"T2327euolc3Main.java",
			    "\n" +
			    "public class T2327euolc3Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "	    try {\n" +
			    "		new Team2327euolc3();\n" +
			    "	    } catch (org.objectteams.DuplicateRoleException e) {\n" +
			    "		if (e.getMessage().indexOf(\"R\") != -1)\n" +
			    "		   System.out.print(\"CAUGHT\");\n" +
			    "		else\n" +
			    "		   throw e;\n" +
			    "	    }\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"T2327euolc3.java",
			    "\n" +
			    "public class T2327euolc3 {\n" +
			    "	private String val;\n" +
			    "	public T2327euolc3(String v) { val = v; }\n" +
			    "	public void test() { System.out.print(val); }\n" +
			    "}\n" +
			    "	\n",
		"Team2327euolc3.java",
			    "\n" +
			    "public team class Team2327euolc3 {\n" +
			    "	protected class R playedBy T2327euolc3 {\n" +
			    "		public abstract void t();\n" +
			    "		t -> test;\n" +
			    "	}\n" +
			    "	Team2327euolc3 () {\n" +
			    "		T2327euolc3 b = new T2327euolc3(\"OK\");\n" +
			    "                R r1 = new R(b);\n" +
			    "		R r = new R(b);\n" +
			    "		r.t();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "CAUGHT");
    }

    // 
    // 2.3.28-otjld-creating-array-of-externalized-role-1
    public void test2328_creatingArrayOfExternalizedRole1() {
       
       runConformTest(
            new String[] {
		"T2328caoer1Main.java",
			    "\n" +
			    "public class T2328caoer1Main {\n" +
			    "	public static void main (String[] args) {\n" +
			    "		Team2328caoer1.setup();\n" +
			    "		for (int i=0; i<Team2328caoer1.rs.length;i++) \n" +
			    "			Team2328caoer1.rs[i].test();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"Team2328caoer1.java",
			    "\n" +
			    "public team class Team2328caoer1 {\n" +
			    "	static final Team2328caoer1 t = new Team2328caoer1();\n" +
			    "	public static R<@t>[] rs;\n" +
			    "	public class R {\n" +
			    "		private String val;\n" +
			    "		public R(String v) { val = v;}\n" +
			    "		public void test() {\n" +
			    "			System.out.print(val);\n" +
			    "		}\n" +
			    "	}\n" +
			    "	static void setup() {\n" +
			    "		rs = new R<@t>[2];\n" +
			    "		rs[0] = t.new R(\"O\");\n" +
			    "		rs[1] = t.new R(\"K\");\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "OK");
    }

    // a role creation triggers lifting but the result is discarded (white-box witness for TPX-401)
    // 2.3.29-otjld-creation-as-statement-1
    public void test2329_creationAsStatement1() {
       
       runConformTest(
            new String[] {
		"Team2329cas1.java",
			    "\n" +
			    "public team class Team2329cas1 {\n" +
			    "    protected class R playedBy T2329cas1 {\n" +
			    "        void k() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        k <- after test \n" +
			    "            base when (Team2329cas1.this.hasRole(base));\n" +
			    "    }\n" +
			    "    @SuppressWarnings(\"roleinstantiation\")\n" +
			    "    void lift(T2329cas1 b) {\n" +
			    "        if (!\"a\".equals(\"b\"))\n" +
			    "            new R(b); // if this op doesn't pop, stack balance is broken\n" +
			    "        System.out.print(\"L\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team2329cas1 t = new Team2329cas1();\n" +
			    "        t.activate();\n" +
			    "        T2329cas1 b = new T2329cas1();\n" +
			    "        t.lift(b);\n" +
			    "        b.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2329cas1.java",
			    "\n" +
			    "public class T2329cas1 {\n" +
			    "    void test() { \n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "LOK");
    }

    // a role creation triggers lifting but the result is discarded (white-box witness for TPX-401) -- qualified creation
    // 2.3.29-otjld-creation-as-statement-2
    public void test2329_creationAsStatement2() {
       
       runConformTest(
            new String[] {
		"Team2329cas2.java",
			    "\n" +
			    "public team class Team2329cas2 {\n" +
			    "    R myR;\n" +
			    "    public class R playedBy T2329cas2 {\n" +
			    "        public R() {\n" +
			    "            base();\n" +
			    "            Team2329cas2.this.myR = this;\n" +
			    "        }\n" +
			    "        void k() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        k <- after test;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team2329cas2 t = new Team2329cas2();\n" +
			    "        t.activate();\n" +
			    "        if (\"a\".equals(\"a\"))\n" +
			    "            t.new R();\n" +
			    "        T2329cas2 b = t.myR;\n" +
			    "        b.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2329cas2.java",
			    "\n" +
			    "public class T2329cas2 {\n" +
			    "    void test() { \n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role with base-ctor-call is retrieved from the cache via getAllRoles()
    // 2.3.29-otjld-creation-as-statement-3
    public void test2329_creationAsStatement3() {
       
       runConformTest(
            new String[] {
		"Team2329cas3.java",
			    "\n" +
			    "public team class Team2329cas3 {\n" +
			    "    public class R playedBy T2329cas3 {\n" +
			    "        String val;\n" +
			    "        public R(String val) {\n" +
			    "            base();\n" +
			    "            this.val = val;\n" +
			    "        }\n" +
			    "        public void foo() {\n" +
			    "            System.out.print(val);\n" +
			    "            bar();\n" +
			    "        }\n" +
			    "        void bar() -> void test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team2329cas3 t = new Team2329cas3();\n" +
			    "        R<@t> r = t.new R(\"O\");\n" +
			    "        R<@t> r2 = t.new R(\"X\");\n" +
			    "        if (r == r2) return; // get rid of warning: r never read\n" +
			    "        t.unregisterRole(r2);\n" +
			    "        Object[] roles = t.getAllRoles();\n" +
			    "        if (roles.length != 1) System.out.print(roles.length);\n" +
			    "        T2329cas3 b = (R<@t>)roles[0];\n" +
			    "        if (t.hasRole(b))\n" +
			    "            ((R<@t>)roles[0]).foo();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2329cas3.java",
			    "\n" +
			    "public class T2329cas3 {\n" +
			    "    void test() { \n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team super-call tries to create a role before the team is initialized - reported by kmeier
    // 2.3.30-otjld-role-creation-in-supercall-1
    public void test2330_roleCreationInSupercall1() {
        runNegativeTestMatching(
            new String[] {
		"Team2320rcis1_2.java",
			    "\n" +
			    "public team class Team2320rcis1_2 extends Team2320rcis1_1 {\n" +
			    "    protected class R {}\n" +
			    "    public Team2320rcis1_2() {\n" +
			    "        super(new R());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2320rcis1_1.java",
			    " \n" +
			    "public abstract team class Team2320rcis1_1 {\n" +
			    "    protected abstract class R {}\n" +
			    "    public Team2320rcis1_1(R r) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "No enclosing instance");
    }

    // a team super-call tries to create a role before the team is initialized - invoked with null argument - reported by kmeier
    // 2.3.30-otjld-role-creation-in-supercall-1f
    public void test2330_roleCreationInSupercall1f() {
        runConformTest(
            new String[] {
		"Team2320rcis1f_2.java",
			    "\n" +
			    "public team class Team2320rcis1f_2 extends Team2320rcis1f_1 {\n" +
			    "    @Override\n" +
			    "    protected class R {}\n" +
			    "    public Team2320rcis1f_2() {\n" +
			    "        super(null);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2320rcis1f_1.java",
			    " \n" +
			    "public abstract team class Team2320rcis1f_1 {\n" +
			    "    protected abstract class R {}\n" +
			    "    public Team2320rcis1f_1(R r) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "");
    }

    // a team super-call tries to create a role before the team is initialized - invoked with casted null argument - reported by kmeier
    // 2.3.30-otjld-role-creation-in-supercall-1g
    public void test2330_roleCreationInSupercall1g() {
       
       runConformTest(
            new String[] {
		"Team2320rcis1g_2.java",
			    "\n" +
			    "public team class Team2320rcis1g_2 extends Team2320rcis1g_1 {\n" +
			    "    protected class R {}\n" +
			    "    public Team2320rcis1g_2() {\n" +
			    "        super((R)null);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2320rcis1g_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2320rcis1g_1.java",
			    " \n" +
			    "public abstract team class Team2320rcis1g_1 {\n" +
			    "    protected abstract class R {}\n" +
			    "    public Team2320rcis1g_1(R r) {\n" +
			    "        System.out.print(r);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "null");
    }

    // a team super-call tries to create a role before the team is initialized - create by declared lifting
    // 2.3.30-otjld-role-creation-in-supercall-2
    public void test2330_roleCreationInSupercall2() {
       
       runConformTest(
            new String[] {
		"Team2320rcis2_2.java",
			    "\n" +
			    "public team class Team2320rcis2_2 extends Team2320rcis2_1 {\n" +
			    "	 @Override\n" +
			    "    protected class R playedBy T2320rcis2 {\n" +
			    "        test -> test;\n" +
			    "    }\n" +
			    "    public Team2320rcis2_2(T2320rcis2 as R r) {\n" +
			    "        super(r);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2320rcis2_2(new T2320rcis2());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2320rcis2.java",
			    "\n" +
			    "public class T2320rcis2 {\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2320rcis2_1.java",
			    " \n" +
			    "public abstract team class Team2320rcis2_1 {\n" +
			    "    protected abstract class R {\n" +
			    "        protected abstract void test();\n" +
			    "    }\n" +
			    "    public Team2320rcis2_1(R r) {\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team super-call tries to create a role before the team is initialized - abstract role instantiated in super-team
    // 2.3.30-otjld-role-creation-in-supercall-3
    public void test2330_roleCreationInSupercall3() {
       
       runConformTest(
            new String[] {
		"Team2320rcis3_2.java",
			    "\n" +
			    "public team class Team2320rcis3_2 extends Team2320rcis3_1 {\n" +
			    "    protected class R {\n" +
			    "        protected void test() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team2320rcis3_2() {\n" +
			    "        super();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2320rcis3_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2320rcis3_1.java",
			    " \n" +
			    "public abstract team class Team2320rcis3_1 {\n" +
			    "    protected abstract class R {\n" +
			    "        protected abstract void test();\n" +
			    "    }\n" +
			    "    public Team2320rcis3_1() {\n" +
			    "        R r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team super-call tries to create a role before the team is initialized - abstract role instantiated in super-team via template method
    // 2.3.30-otjld-role-creation-in-supercall-3f
    public void test2330_roleCreationInSupercall3f() {
       
       runConformTest(
            new String[] {
		"Team2320rcis3f_2.java",
			    "\n" +
			    "public team class Team2320rcis3f_2 extends Team2320rcis3f_1 {\n" +
			    "    protected class R {\n" +
			    "        protected void test() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected R createR() {\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "    public Team2320rcis3f_2() {\n" +
			    "        super();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2320rcis3f_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team2320rcis3f_1.java",
			    " \n" +
			    "public abstract team class Team2320rcis3f_1 {\n" +
			    "    protected abstract class R {\n" +
			    "        protected abstract void test();\n" +
			    "    }\n" +
			    "    abstract protected R createR();\n" +
			    "    public Team2320rcis3f_1() {\n" +
			    "        R r = createR();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an unbound role is specialized to a bound role, default constructor becomes invalid
    // 2.3.31-otjld-role-creation-invalidated-by-specialization-1
    public void test2331_roleCreationInvalidatedBySpecialization1() {
       
       runConformTest(
            new String[] {
		"Team2331rcibs1_2.java",
			    "\n" +
			    "public team class Team2331rcibs1_2 extends Team2331rcibs1_1 {\n" +
			    "    protected class R playedBy T2331rcibs1 {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        try {\n" +
			    "            new Team2331rcibs1_2().test();\n" +
			    "        } catch (org.objectteams.IllegalRoleCreationException e) {\n" +
			    "            System.out.print(\"Caught\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2331rcibs1.java",
			    "\n" +
			    "public class T2331rcibs1 {}\n" +
			    "    \n",
		"Team2331rcibs1_1.java",
			    "\n" +
			    "public team class Team2331rcibs1_1 {\n" +
			    "    protected class R {}\n" +
			    "    void test() {\n" +
			    "        R r = new R();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Caught");
    }

    // A static role method creates a role instance
    // 2.3.32-otjld-role-creation-from-static-context-1
    public void test2332_roleCreationFromStaticContext1() {
       
       runConformTest(
            new String[] {
		"Team2332rcfsc1.java",
			    "\n" +
			    "public team class Team2332rcfsc1 {\n" +
			    "    protected class R {\n" +
			    "        String f;\n" +
			    "        protected R(String f) { this.f = f; }\n" +
			    "        protected static R newR(String first) {\n" +
			    "            return new R(first);\n" +
			    "        }\n" +
			    "        protected void test(String second) {\n" +
			    "            System.out.print(this.f);\n" +
			    "            System.out.print(second);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        R r = R.newR(\"O\");\n" +
			    "        r.test(\"K\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2332rcfsc1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class has two  lifting constructors
    // 2.3.33-otjld-duplicate-lifting-constructors-1
    public void test2333_duplicateLiftingConstructors1() {
        runNegativeTestMatching(
            new String[] {
		"Team2333dlc1.java",
			    "\n" +
			    "public team class Team2333dlc1 {\n" +
			    "    protected class R playedBy T2333dlc1 {\n" +
			    "        protected R(T2333dlc1 b1) {}\n" +
			    "        protected R(T2333dlc1 b2) {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T2333dlc1.java",
			    "\n" +
			    "public class T2333dlc1 {}\n" +
			    "    \n"
            },
            "Duplicate");
    }

    // the anchor in an explicit role creation has the type of a nested team
    // 2.3.34-otjld-creating-role-of-nested-team-1
    public void test2334_creatingRoleOfNestedTeam1() {
       
       runConformTest(
            new String[] {
		"Team2334cront1.java",
			    "\n" +
			    "public team class Team2334cront1 {\n" +
			    "    protected team class Mid {\n" +
			    "        public class R {\n" +
			    "            public void test() { System.out.print(\"OK\"); }\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        final Mid m = new Mid();\n" +
			    "        R<@m> r = new R<@m>();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2334cront1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // creating nested team and its role in one step
    // 2.3.34-otjld-creating-role-of-nested-team-2
    public void test2334_creatingRoleOfNestedTeam2() {
       
       runConformTest(
            new String[] {
		"Team2334cront2.java",
			    "\n" +
			    "public team class Team2334cront2 {\n" +
			    "    protected team class Mid {\n" +
			    "        public class R {\n" +
			    "            public void test() { System.out.print(\"OK\"); }\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        new Mid().new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2334cront2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role is bound to a regular inner with a non-visible constructor invoked via base()
    // 2.3.35-otjld-creating-role-of-regular-inner-1
    public void test2335_creatingRoleOfRegularInner1() {
       
       runConformTest(
            new String[] {
		"Team2335crori1.java",
			    "\n" +
			    "import b.T2335crori1;\n" +
			    "import base b.T2335crori1.Inner;\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team2335crori1 {\n" +
			    "    protected class R playedBy Inner {\n" +
			    "        protected R(T2335crori1 outer) {\n" +
			    "            outer.base(\"OK\");\n" +
			    "        }\n" +
			    "        String getVal() -> get String val;\n" +
			    "        protected void print() {\n" +
			    "            System.out.print(getVal());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        new R(new T2335crori1()).print();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2335crori1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"b/T2335crori1.java",
			    "\n" +
			    "package b;\n" +
			    "public class T2335crori1 {\n" +
			    "    class Inner {\n" +
			    "        String val;\n" +
			    "        private Inner(String v) {\n" +
			    "            this.val = v;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role is bound to a lower role with a non-visible constructor invoked via base()
    // 2.3.35-otjld-creating-role-of-regular-inner-2
    public void test2335_creatingRoleOfRegularInner2() {
       
       runConformTest(
            new String[] {
		"Team2335crori2_2.java",
			    "\n" +
			    "import b.Team2335crori2_1;\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team2335crori2_2 {\n" +
			    "    protected final Team2335crori2_1 lower = new Team2335crori2_1();\n" +
			    "    protected class R playedBy Inner<@lower> {\n" +
			    "        protected R(Team2335crori2_1 outer) {\n" +
			    "            outer.base(\"OK\");\n" + // TODO: is outer. acceptable, shouldn't it always be lower.base()??
			    "        }\n" +
			    "        String getVal() -> get String val;\n" +
			    "        protected void print() {\n" +
			    "            System.out.print(getVal());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        new R(lower).print();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2335crori2_2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"b/Team2335crori2_1.java",
			    "\n" +
			    "package b;\n" +
			    "public team class Team2335crori2_1 {\n" +
			    "    protected class Inner {\n" +
			    "        String val;\n" +
			    "        protected Inner(String v) {\n" +
			    "            this.val = v;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role is bound to a lower role with a non-visible constructor invoked via base()
    // addition: one more arg (2byte)
    public void test2335_creatingRoleOfRegularInner2b() {
       
       runConformTest(
            new String[] {
		"Team2335crori2b_2.java",
			    "\n" +
			    "import b.Team2335crori2b_1;\n" +
			    "@SuppressWarnings(\"decapsulation\")\n" +
			    "public team class Team2335crori2b_2 {\n" +
			    "    protected final Team2335crori2b_1 lower = new Team2335crori2b_1();\n" +
			    "    protected class R playedBy Inner<@lower> {\n" +
			    "        protected R(Team2335crori2b_1 outer) {\n" +
			    "            outer.base(\"OK\", 1.2345d);\n" + // TODO: is outer. acceptable, shouldn't it always be lower.base()??
			    "        }\n" +
			    "        String getVal() -> get String val;\n" +
			    "        protected void print() {\n" +
			    "            System.out.print(getVal());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        new R(lower).print();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team2335crori2b_2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"b/Team2335crori2b_1.java",
			    "\n" +
			    "package b;\n" +
			    "public team class Team2335crori2b_1 {\n" +
			    "    protected class Inner {\n" +
			    "        String val;\n" +
			    "        protected Inner(String v, double d) {\n" +
			    "            this.val = v+String.valueOf(d);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK1.2345");
    }
    // no test with private constructor: can never be called from  outside (has no creation method).
}
