/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010 IT Service Omikron GmbH and others.
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
package org.eclipse.objectteams.otdt.tests.otjld.callinbinding;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class CallinParameterMapping_LiftingAndLowering extends AbstractOTJLDTest {
	
	public CallinParameterMapping_LiftingAndLowering(String name) {
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
		return CallinParameterMapping_LiftingAndLowering.class;
	}

    // a role method is callin-bound as 'before' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-1
    public void test431_parameterMapping1() {
       
       runConformTest(
            new String[] {
		"T431pm1Main.java",
			    "\n" +
			    "public class T431pm1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm1 t = new Team431pm1();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T431pm1 o = new T431pm1();\n" +
			    "\n" +
			    "        System.out.print(o.getValue(\"OK\")+\"|\"+t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm1.java",
			    "\n" +
			    "public class T431pm1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm1.java",
			    "\n" +
			    "public team class Team431pm1 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public class Role431pm1 playedBy T431pm1 {\n" +
			    "        public String getValue(String arg) {\n" +
			    "            value = arg;\n" +
			    "            return arg;\n" +
			    "        }\n" +
			    "        String getValue(String arg) <- before String getValue(String arg) with {\n" +
			    "            arg <- arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK|OK");
    }

    // a role method is callin-bound as 'after' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-2
    public void test431_parameterMapping2() {
       
       runConformTest(
            new String[] {
		"T431pm2Main.java",
			    "\n" +
			    "public class T431pm2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm2 t = new Team431pm2();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T431pm2 o = new T431pm2();\n" +
			    "\n" +
			    "        System.out.print(o.getValue(\"OK\")+\"|\"+t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm2.java",
			    "\n" +
			    "public class T431pm2 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm2.java",
			    "\n" +
			    "public team class Team431pm2 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public class Role431pm2 playedBy T431pm2 {\n" +
			    "        public String getValue(Object arg) {\n" +
			    "            value = arg.toString();\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "        String getValue(Object arg) <- after String getValue(String arg) with {\n" +
			    "            arg <- arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK|OK");
    }

    // a role method is callin-bound as 'before' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-3
    public void test431_parameterMapping3() {
       
       runConformTest(
            new String[] {
		"T431pm3Main.java",
			    "\n" +
			    "public class T431pm3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm3 t = new Team431pm3();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T431pm3 o = new T431pm3();\n" +
			    "\n" +
			    "        System.out.print(o.getValue(1)+\"|\"+t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm3.java",
			    "\n" +
			    "public class T431pm3 {\n" +
			    "    public int getValue(int arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm3.java",
			    "\n" +
			    "public team class Team431pm3 {\n" +
			    "    private int value;\n" +
			    "\n" +
			    "    public class Role431pm3 playedBy T431pm3 {\n" +
			    "        public int test(int arg) {\n" +
			    "            value = arg;\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "        int test(int arg) <- before int getValue(int arg) with {\n" +
			    "            arg <- 0\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public int getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1|0");
    }

    // a role method is callin-bound as 'after' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-4
    public void test431_parameterMapping4() {
       
       runConformTest(
            new String[] {
		"T431pm4Main.java",
			    "\n" +
			    "public class T431pm4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm4 t = new Team431pm4();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T431pm4 o = new T431pm4();\n" +
			    "    \n" +
			    "            System.out.print(o.getValue(1)+\"|\"+t.getValue());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm4.java",
			    "\n" +
			    "public class T431pm4 {\n" +
			    "    public int getValue(int arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm4.java",
			    "\n" +
			    "public team class Team431pm4 {\n" +
			    "    private int value;\n" +
			    "\n" +
			    "    public class Role431pm4 playedBy T431pm4 {\n" +
			    "        public int test(int arg) {\n" +
			    "            value = arg;\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "        int test(int arg) <- after int getValue(int arg) with {\n" +
			    "            arg <- -arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public int getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1|-1");
    }

    // a role method is callin-bound as 'before' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-5
    public void test431_parameterMapping5() {
       
       runConformTest(
            new String[] {
		"T431pm5Main.java",
			    "\n" +
			    "public class T431pm5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm5 t = new Team431pm5();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T431pm5 o = new T431pm5();\n" +
			    "    \n" +
			    "            System.out.print(o.getValue(1)+\"|\"+t.getValue());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm5.java",
			    "\n" +
			    "public class T431pm5 {\n" +
			    "    public int getValue(int arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm5.java",
			    "\n" +
			    "public team class Team431pm5 {\n" +
			    "    private int value;\n" +
			    "\n" +
			    "    public class Role431pm5 playedBy T431pm5 {\n" +
			    "        public int test(int arg) {\n" +
			    "            value = arg;\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "        int test(int arg) <- before int getValue(int arg) with {\n" +
			    "            arg <- 2 * arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public int getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1|2");
    }

    // a role method is callin-bound as 'after' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-6
    public void test431_parameterMapping6() {
       
       runConformTest(
            new String[] {
		"T431pm6Main.java",
			    "\n" +
			    "public class T431pm6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm6 t = new Team431pm6();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T431pm6 o = new T431pm6();\n" +
			    "\n" +
			    "        System.out.print(o.getValue(1.0f, 2)+\"|\"+t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm6.java",
			    "\n" +
			    "public class T431pm6 {\n" +
			    "    public float getValue(float arg1, int arg2) {\n" +
			    "        return arg1 + arg2;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm6.java",
			    "\n" +
			    "public team class Team431pm6 {\n" +
			    "    private int value;\n" +
			    "\n" +
			    "    public class Role431pm6 playedBy T431pm6 {\n" +
			    "        public float test(float arg1, int arg2) {\n" +
			    "            value = arg2;\n" +
			    "            return arg1;\n" +
			    "        }\n" +
			    "        float test(float arg1, int arg2) <- after float getValue(float arg1, int arg2) with {\n" +
			    "            arg1 <- arg1,\n" +
			    "            arg2 <- (int)arg1\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public int getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3.0|1");
    }

    // a role method is callin-bound as 'before' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-7
    public void test431_parameterMapping7() {
       
       runConformTest(
            new String[] {
		"T431pm7Main.java",
			    "\n" +
			    "public class T431pm7Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm7 t = new Team431pm7();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T431pm7 o = new T431pm7();\n" +
			    "    \n" +
			    "            System.out.print(o.test(\"a\")+\"|\"+t.getValue());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm7.java",
			    "\n" +
			    "public class T431pm7 {\n" +
			    "    public String test(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm7.java",
			    "\n" +
			    "public team class Team431pm7 {\n" +
			    "    private String newValue = \"b\";\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public class Role431pm7 playedBy T431pm7 {\n" +
			    "        public String test(String arg) {\n" +
			    "            value = arg;\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "        String test(String arg) <- before String test(String arg) with {\n" +
			    "            arg <- Team431pm7.this.newValue\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "a|b");
    }

    // a role method is callin-bound as 'after' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-8
    public void test431_parameterMapping8() {
       
       runConformTest(
            new String[] {
		"T431pm8Main.java",
			    "\n" +
			    "public class T431pm8Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm8 t = new Team431pm8();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T431pm8_1 o = new T431pm8_1();\n" +
			    "    \n" +
			    "            System.out.print(o.getValue(\"a\")+\"|\"+t.getValue());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm8_1.java",
			    "\n" +
			    "public class T431pm8_1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm8_2.java",
			    "\n" +
			    "public class T431pm8_2 {\n" +
			    "    public static String getValue() {\n" +
			    "        return \"b\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm8.java",
			    "\n" +
			    "public team class Team431pm8 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public class Role431pm8 playedBy T431pm8_1 {\n" +
			    "        public String getValue(String arg) {\n" +
			    "            value = arg;\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "        String getValue(String arg) <- after String getValue(String arg) with {\n" +
			    "            arg <- T431pm8_2.getValue()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "a|b");
    }

    // a role method is callin-bound as 'before' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-9
    public void test431_parameterMapping9() {
       
       runConformTest(
            new String[] {
		"T431pm9Main.java",
			    "\n" +
			    "public class T431pm9Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm9 t = new Team431pm9();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T431pm9_1 o = new T431pm9_1();\n" +
			    "\n" +
			    "        System.out.print(o.test(\"a\")+\"|\"+t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm9_1.java",
			    "\n" +
			    "public class T431pm9_1 {\n" +
			    "    public String test(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm9_2.java",
			    "\n" +
			    "public class T431pm9_2 {\n" +
			    "    public String toString() {\n" +
			    "        return \"b\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm9.java",
			    "\n" +
			    "public team class Team431pm9 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public class Role431pm9 playedBy T431pm9_1 {\n" +
			    "        public String test(Object arg) {\n" +
			    "            value = arg.toString();\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "        String test(Object arg) <- before String test(String arg) with {\n" +
			    "            arg <- new T431pm9_2()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "a|b");
    }

    // a role method is callin-bound as 'replace' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-10
    public void test431_parameterMapping10() {
       
       runConformTest(
            new String[] {
		"T431pm10Main.java",
			    "\n" +
			    "public class T431pm10Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm10 t = new Team431pm10();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm10_1.java",
			    "\n" +
			    "public class T431pm10_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm10_2.java",
			    "\n" +
			    "public class T431pm10_2 {\n" +
			    "    public String test(T431pm10_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm10.java",
			    "\n" +
			    "import org.objectteams.ImplicitTeamActivation;\n" +
			    "public team class Team431pm10 {\n" +
			    "    public class Role431pm10_1 playedBy T431pm10_1 {\n" +
			    "        public String toString() {\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role431pm10_2 playedBy T431pm10_2 {\n" +
			    "        callin String test(Role431pm10_1 obj) {\n" +
			    "            // should lower the object and thus return \"a\";\n" +
			    "            String baseValue = base.test(obj);\n" +
			    "            return baseValue + \"|\" + obj.toString();\n" +
			    "        }\n" +
			    "        String test(Role431pm10_1 obj) <- replace String test(T431pm10_1 obj) with {\n" +
			    "            obj <- obj,\n" +
			    "            result -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    @ImplicitTeamActivation\n" +
			    "    public String getValue() {\n" +
			    "        T431pm10_2 obj = new T431pm10_2();\n" +
			    "        // we're in a team method so the team should be active -> callin binding of role Role431pm10_2 is enabled\n" +
			    "\n" +
			    "        return obj.test(new T431pm10_1());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "a|b");
    }

    // a role method is callin-bound as 'replace' with a parameter mapping - parameter is *lowered*
    // 4.3.1-otjld-parameter-mapping-11
    public void test431_parameterMapping11() {
        runNegativeTestMatching(
            new String[] {
		"Team431pm11_2.java",
			    "\n" +
			    "public team class Team431pm11_2 {\n" +
			    "    public class Role431pm11_2 playedBy Team431pm11_1 {\n" +
			    "        callin String getValue(T431pm11 obj) {\n" +
			    "            // should lift the object and thus return \"b\";\n" +
			    "            String baseValue = base.getValue(obj);\n" +
			    "            return baseValue + \"|\" + obj.toString();\n" +
			    "        }\n" +
			    "        String getValue(T431pm11 obj) <- replace String test(Role431pm11_1<@base> obj) with {\n" +
			    "            obj <- obj,\n" +
			    "            result -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm11.java",
			    "\n" +
			    "public class T431pm11 {\n" +
			    "    public String toString() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm11_1.java",
			    "\n" +
			    "public team class Team431pm11_1 {\n" +
			    "    public class Role431pm11_1 playedBy T431pm11 {\n" +
			    "    	public Role431pm11_1() {\n" +
			    "	       base();\n" +
			    "	    }\n" +
			    "        public String toString() {\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public String test(Role431pm11_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.2(b)");
    }

    // a role method is callin-bound as 'replace' with a parameter mapping
    // 4.3.1-otjld-parameter-mapping-11a
    public void test431_parameterMapping11a() {
       
       runConformTest(
            new String[] {
		"T431pm11aMain.java",
			    "\n" +
			    "public class T431pm11aMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm11a_2 t2 = new Team431pm11a_2();\n" +
			    "\n" +
			    "        within (t2) {\n" +
			    "            final Team431pm11a_1 t1 = new Team431pm11a_1();\n" +
			    "            Role431pm11a_1<@t1>  r  = t1.new Role431pm11a_1();\n" +
			    "\n" +
			    "            System.out.print(t1.test(r));\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm11a.java",
			    "\n" +
			    "public class T431pm11a {\n" +
			    "    public String toString() {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm11a_1.java",
			    "\n" +
			    "public team class Team431pm11a_1 {\n" +
			    "    public class Role431pm11a_1 playedBy T431pm11a {\n" +
			    "    	public Role431pm11a_1() {\n" +
			    "	    base();\n" +
			    "	}\n" +
			    "        public String toString() {\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "	public abstract String getString();\n" +
			    "	getString -> toString;\n" +
			    "    }\n" +
			    "    public String test(Role431pm11a_1 obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm11a_2.java",
			    "\n" +
			    "public team class Team431pm11a_2 {\n" +
			    "    public class Role431pm11a_2 playedBy Team431pm11a_1 {\n" +
			    "        callin String getValue(Role431pm11a_1<@base> obj) {\n" +
			    "            // should lift the object and thus return \"b\";\n" +
			    "            String baseValue = base.getValue(obj);\n" +
			    "            return baseValue + \"|\" + obj.getString();\n" +
			    "        }\n" +
			    "        String getValue(Role431pm11a_1<@base> obj) <- replace String test(Role431pm11a_1<@base> obj) with {\n" +
			    "            obj <- obj,\n" +
			    "            result -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "b|a");
    }

    // a role method is callin-bound as 'replace' with a reordered parameter mapping
    // 4.3.1-otjld-parameter-mapping-12
    public void test431_parameterMapping12() {
       
       runConformTest(
            new String[] {
		"T431pm12Main.java",
			    "\n" +
			    "public class T431pm12Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team431pm12 t = new Team431pm12();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T431pm12 o = new T431pm12();\n" +
			    "    \n" +
			    "            System.out.print(o.test(\"a\", \"b\"));\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm12.java",
			    "\n" +
			    "public class T431pm12 {\n" +
			    "    public String test(String arg1, String arg2) {\n" +
			    "        return arg1 + arg2;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm12.java",
			    "\n" +
			    "public team class Team431pm12 {\n" +
			    "    public class Role431pm12 playedBy T431pm12 {\n" +
			    "        callin String test(String arg1, String arg2) {\n" +
			    "            return base.test(arg1.toUpperCase(), arg2);\n" +
			    "        }\n" +
			    "        String test(String arg1, String arg2) <- replace String test(String arg1, String arg2) with {\n" +
			    "            arg1 <- arg2,\n" +
			    "            arg2 <- arg1,\n" +
			    "            result -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "aB");
    }

    // a parameter mapping is declared in a super team - has to be inherited to the subteam!
    // 4.3.1-otjld-parameter-mapping-13
    public void test431_parameterMapping13() {
       
       runConformTest(
            new String[] {
		"T431pm13Main.java",
			    "\n" +
			    "public class T431pm13Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        \n" +
			    "        Team431pm13_2 t = new Team431pm13_2();\n" +
			    "        t.activate();\n" +
			    "        T431pm13 b = new T431pm13();\n" +
			    "        b.bm(\"O\", null);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm13.java",
			    "\n" +
			    "     import java.awt.Component;\n" +
			    "public class T431pm13 {\n" +
			    "     public void bm(String s, Component c) {\n" +
			    "        System.out.print(s);\n" +
			    "    } \n" +
			    "}\n" +
			    "    \n",
		"Team431pm13.java",
			    "\n" +
			    "import java.awt.Component;\n" +
			    "public team class Team431pm13 {\n" +
			    "    public class Role431pm13 playedBy T431pm13 {\n" +
			    "        callin void rm(Component c) {\n" +
			    "            base.rm(c);\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "\n" +
			    "        void rm(Component cr) <- replace void bm(String s, Component cb) with {\n" +
			    "            cr <- cb\n" +
			    "        };\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team431pm13_2.java",
			    "\n" +
			    "public team class Team431pm13_2 extends Team431pm13 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // witness for Trac #285 - could not resolve role field in parameter mapping (RHS)
    // 4.3.1-otjld-parameter-mapping-14
    public void test431_parameterMapping14() {
       
       runConformTest(
            new String[] {
		"Team431pm14.java",
			    "\n" +
			    "public team class Team431pm14 {\n" +
			    "    protected class R playedBy T431pm14 {\n" +
			    "        public int f = 0;\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin void roleMethod(int x) {\n" +
			    "            System.out.print(x);\n" +
			    "        }\n" +
			    "\n" +
			    "        void roleMethod(int x) <- replace void baseMethod(int i) with {\n" +
			    "            x <- f\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team431pm14().activate();\n" +
			    "        new T431pm14().baseMethod(13);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm14.java",
			    "\n" +
			    "public class T431pm14 {\n" +
			    "    public void baseMethod(int i) {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "0");
    }

    // witness for Trac #285 - could not resolve role method in parameter mapping (RHS)
    // 4.3.1-otjld-parameter-mapping-15
    public void test431_parameterMapping15() {
       
       runConformTest(
            new String[] {
		"Team431pm15.java",
			    "\n" +
			    "public team class Team431pm15 {\n" +
			    "    protected class R playedBy T431pm15 {\n" +
			    "        public int f() { return  0; }\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin void roleMethod(int x) {\n" +
			    "            System.out.print(x);\n" +
			    "        }\n" +
			    "\n" +
			    "        void roleMethod(int x) <- replace void baseMethod(int i) with {\n" +
			    "            x <- f()\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team431pm15().activate();\n" +
			    "        new T431pm15().baseMethod(13);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm15.java",
			    "\n" +
			    "public class T431pm15 {\n" +
			    "    public void baseMethod(int i) {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "0");
    }

    // scoping of field vs. argument, see Trac #294
    // 4.3.1-otjld-parameter-mapping-16
    public void test431_parameterMapping16() {
       
       runConformTest(
            new String[] {
		"Team431pm16.java",
			    "\n" +
			    "public team class Team431pm16 {\n" +
			    "    protected class R playedBy T431pm16 {\n" +
			    "        void doInt(int i) {\n" +
			    "            System.out.print(i);\n" +
			    "        }\n" +
			    "        void doInt(int i) <- after void setName(String name)\n" +
			    "            base when (name.contains(\"xyz\"))\n" +
			    "            with {i <- name.length()};\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team431pm16().activate(ALL_THREADS);\n" +
			    "        new T431pm16().setName(\"OxyzK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm16.java",
			    "\n" +
			    "public class T431pm16 {\n" +
			    "    String name = \"initial\";\n" +
			    "    void setName(String name) { this.name = name; }\n" +
			    "}\n" +
			    "    \n"
            },
            "5");
    }

    // an after callin defines a role-to-base result mapping
    // 4.3.1-otjld-parameter-mapping-17
    public void test431_parameterMapping17() {
        runNegativeTestMatching(
            new String[] {
		"Team431pm17.java",
			    "\n" +
			    "public team class Team431pm17 {\n" +
			    "    protected class R playedBy T431pm17 {\n" +
			    "	int rm() { return 3; }\n" +
			    "	int rm() <- after int baseMethod(int i)\n" +
			    "	with { result * 2 -> result }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T431pm17.java",
			    "\n" +
			    "public class T431pm17 {\n" +
			    "    public int baseMethod(int i) {\n" +
			    "	return 5;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "4.4(c)");
    }

    // a role method callin-bound as 'replace' has a result mapping with an expression
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-1
    public void test432_expressionInReplaceParameterMapping1() {
       
       runConformTest(
            new String[] {
		"Team432eirpm1.java",
			    "\n" +
			    "public team class Team432eirpm1 {\n" +
			    "    public class Role432eirpm1 playedBy T432eirpm1 {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "        void test() <- replace String test() with {\n" +
			    "            \"OK\" -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "    	(new Team432eirpm1()).activate();\n" +
			    "	System.out.print((new T432eirpm1()).test());\n" +
			    "    }    \n" +
			    "}\n" +
			    "    \n",
		"T432eirpm1.java",
			    "\n" +
			    "public class T432eirpm1 {\n" +
			    "    public String test() { return \"NOK\";}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role method callin-bound as 'replace' tunnels the base result
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-1b
    public void test432_expressionInReplaceParameterMapping1b() {
       
       runConformTest(
            new String[] {
		"Team432eirpm1b.java",
			    "\n" +
			    "public team class Team432eirpm1b {\n" +
			    "    public class Role432eirpm1b playedBy T432eirpm1b {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "        void test() <- replace String test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "    	(new Team432eirpm1b()).activate();\n" +
			    "	System.out.print((new T432eirpm1b()).test());\n" +
			    "    }    \n" +
			    "}\n" +
			    "    \n",
		"T432eirpm1b.java",
			    "\n" +
			    "public class T432eirpm1b {\n" +
			    "    public String test() { return \"OK\";}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role method callin-bound as 'replace' has a legal result mapping with an expression (result to provided by role method)
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-2
    public void test432_expressionInReplaceParameterMapping2() {
       
       runConformTest(
            new String[] {
		"Team432eirpm2.java",
			    "\n" +
			    "public team class Team432eirpm2 {\n" +
			    "    private String value = \"OK\";\n" +
			    "\n" +
			    "    public class Role432eirpm2 playedBy T432eirpm2 {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "        void test() <- replace String test() with {\n" +
			    "            Team432eirpm2.this.value -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        (new Team432eirpm2()).activate();\n" +
			    "        T432eirpm2 b = new T432eirpm2();\n" +
			    "        System.out.print(b.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T432eirpm2.java",
			    "\n" +
			    "public class T432eirpm2 {\n" +
			    "    public String test() {\n" +
			    "        return \"NOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role method callin-bound as 'replace' has an illegal result mapping with an expression
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-2f
    public void test432_expressionInReplaceParameterMapping2f() {
        runNegativeTest(
            new String[] {
		"T432eirpm2f.java",
			    "\n" +
			    "public class T432eirpm2f {\n" +
			    "    public String test() {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team432eirpm2f.java",
			    "\n" +
			    "public team class Team432eirpm2f {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public class Role432eirpm2f playedBy T432eirpm2f {\n" +
			    "        callin String test() {\n" +
			    "            return base.test();\n" +
			    "        }\n" +
			    "        String test() <- replace String test() with {\n" +
			    "            Team432eirpm2f.this.value -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role method callin-bound as 'replace' has a parameter mapping with an expression
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-3
    public void test432_expressionInReplaceParameterMapping3() {
        runNegativeTest(
            new String[] {
		"T432eirpm3.java",
			    "\n" +
			    "public class T432eirpm3 {\n" +
			    "    public void test(int arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team432eirpm3.java",
			    "\n" +
			    "public team class Team432eirpm3 {\n" +
			    "    public class Role432eirpm3 playedBy T432eirpm3 {\n" +
			    "        callin void test(int arg) {\n" +
			    "            base.test(arg);\n" +
			    "        }\n" +
			    "        void test(int arg) <- replace void test(int arg) with {\n" +
			    "            arg <- -arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role method callin-bound as 'replace' has a result mapping with an expression
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-4
    public void test432_expressionInReplaceParameterMapping4() {
        runNegativeTest(
            new String[] {
		"T432eirpm4.java",
			    "\n" +
			    "public class T432eirpm4 {\n" +
			    "    public int test() {\n" +
			    "        return 0;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team432eirpm4.java",
			    "\n" +
			    "public team class Team432eirpm4 {\n" +
			    "    public class Role432eirpm4 playedBy T432eirpm4 {\n" +
			    "        callin int test() {\n" +
			    "            return base.test();\n" +
			    "        }\n" +
			    "        int test() <- replace int test() with {\n" +
			    "            result + 2 -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role method callin-bound as 'replace' has a parameter mapping with an expression
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-5
    public void test432_expressionInReplaceParameterMapping5() {
        runNegativeTest(
            new String[] {
		"T432eirpm5.java",
			    "\n" +
			    "public class T432eirpm5 {\n" +
			    "    public void test(int arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team432eirpm5.java",
			    "\n" +
			    "public team class Team432eirpm5 {\n" +
			    "    public int getValue(int arg) {\n" +
			    "        return 0;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role432eirpm5 playedBy T432eirpm5 {\n" +
			    "        callin void test(int arg) {\n" +
			    "            base.test(arg);\n" +
			    "        }\n" +
			    "        void test(int arg) <- replace void test(int arg) with {\n" +
			    "            arg <- getValue(arg)\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role method callin-bound as 'replace' has a parameter mapping with an expression
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-6
    public void test432_expressionInReplaceParameterMapping6() {
       
       runConformTest(
            new String[] {
		"Team432eirpm6.java",
			    "\n" +
			    "public team class Team432eirpm6 {\n" +
			    "    public class Role432eirpm6 playedBy T432eirpm6 {\n" +
			    "        callin void test(Object arg) {\n" +
			    "            base.test(arg);\n" +
			    "	    System.out.print(arg);\n" +
			    "        }\n" +
			    "        void test(Object arg) <- replace void test() with {\n" +
			    "            arg <- new String(\"OK\")\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "    	(new Team432eirpm6()).activate();\n" +
			    "	(new T432eirpm6()).test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T432eirpm6.java",
			    "\n" +
			    "public class T432eirpm6 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role method callin-bound as 'replace' has a result mapping with an expression
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-7
    public void test432_expressionInReplaceParameterMapping7() {
        runNegativeTest(
            new String[] {
		"T432eirpm7.java",
			    "\n" +
			    "public class T432eirpm7 {\n" +
			    "    public void test(Object arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team432eirpm7.java",
			    "\n" +
			    "public team class Team432eirpm7 {\n" +
			    "    public class Role432eirpm7 playedBy T432eirpm7 {\n" +
			    "        callin void test(String arg) {\n" +
			    "            base.test(arg);\n" +
			    "        }\n" +
			    "        void test(String arg) <- replace void test(Object arg) with {\n" +
			    "            arg <- arg.toString()\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role method callin-bound as 'replace' has a result mapping with an expression - legal case
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-8
    public void test432_expressionInReplaceParameterMapping8() {
       
       runConformTest(
            new String[] {
		"Team432eirpm8.java",
			    "\n" +
			    "public team class Team432eirpm8 {\n" +
			    "    public class Role432eirpm8 playedBy T432eirpm8 {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "        void test() <- replace String test() with {\n" +
			    "            \"OK\" -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        (new Team432eirpm8()).activate();\n" +
			    "        T432eirpm8 b = new T432eirpm8();\n" +
			    "        System.out.print(b.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T432eirpm8.java",
			    "\n" +
			    "public class T432eirpm8 {\n" +
			    "    public String test() { return \"NOK\"; }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role method callin-bound as 'replace' has no result mapping, value is tunneled from base call
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-9
    public void test432_expressionInReplaceParameterMapping9() {
       
       runConformTest(
            new String[] {
		"Team432eirpm9.java",
			    "\n" +
			    "public team class Team432eirpm9 {\n" +
			    "    public class Role432eirpm9 playedBy T432eirpm9 {\n" +
			    "        callin void test() {\n" +
			    "            System.out.print(\"O\");\n" +
			    "            base.test();\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        void test() <- replace String test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        (new Team432eirpm9()).activate();\n" +
			    "        T432eirpm9 b = new T432eirpm9();\n" +
			    "        System.out.print(b.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T432eirpm9.java",
			    "\n" +
			    "public class T432eirpm9 {\n" +
			    "    public String test() { return \"!\"; }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!");
    }

    // a role method callin-bound as 'replace' has a parameter mapping with a cast
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-10
    public void test432_expressionInReplaceParameterMapping10() {
       
       runConformTest(
            new String[] {
		"Team432eirpm10.java",
			    "\n" +
			    "public team class Team432eirpm10 {\n" +
			    "    protected class R playedBy T432eirpm10 {\n" +
			    "        callin void ci (String s) {\n" +
			    "            System.out.print(s);\n" +
			    "            base.ci(s);\n" +
			    "        }\n" +
			    "        void ci(String s) <- replace void test(Object o) \n" +
			    "            with { s <- (String)o }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team432eirpm10().activate();\n" +
			    "        new T432eirpm10().test(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T432eirpm10.java",
			    "\n" +
			    "public class T432eirpm10 {\n" +
			    "    void test(Object o) {\n" +
			    "        System.out.print(o);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OO");
    }

    // a role method callin-bound as 'replace' has a parameter mapping with a cast, cast is guarded
    // 4.3.2-otjld-expression-in-replace-parameter-mapping-11
    public void test432_expressionInReplaceParameterMapping11() {
       
       runConformTest(
            new String[] {
		"Team432eirpm11.java",
			    "\n" +
			    "public team class Team432eirpm11 {\n" +
			    "    protected class R playedBy T432eirpm11 {\n" +
			    "        callin void ci (R s) {\n" +
			    "            System.out.print(\"R\");\n" +
			    "            base.ci(s);\n" +
			    "            s.print();\n" +
			    "        }\n" +
			    "        void ci(R s) <- replace void test(Object o) \n" +
			    "            base when (o instanceof T432eirpm11)\n" +
			    "            with { s <- (T432eirpm11)o }\n" +
			    "        \n" +
			    "        void print() -> void print();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team432eirpm11().activate();\n" +
			    "        T432eirpm11 o1 = new T432eirpm11();\n" +
			    "        T432eirpm11 o2 = new T432eirpm11();\n" +
			    "        o1.test(\"O\");\n" +
			    "        o1.test(o2);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T432eirpm11.java",
			    "\n" +
			    "public class T432eirpm11 {\n" +
			    "    void test(Object o) {\n" +
			    "        System.out.print(o);\n" +
			    "    }\n" +
			    "    void print() {\n" +
			    "        System.out.print(\"!\");\n" +
			    "    }\n" +
			    "    public String toString() {\n" +
			    "        return \"K\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "ORK!");
    }

    // a role method with return type is callin-bound as 'before' and the return value is calculated
    // 4.3.3-otjld-result-of-binding-is-calculated-1
    public void test433_resultOfBindingIsCalculated1() {
       
       runConformTest(
            new String[] {
		"T433robic1Main.java",
			    "\n" +
			    "public class T433robic1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team433robic1 t = new Team433robic1();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T433robic1 o = new T433robic1();\n" +
			    "\n" +
			    "        System.out.print(o.getValue(\"b\")+\"|\"+t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T433robic1.java",
			    "\n" +
			    "public class T433robic1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team433robic1.java",
			    "\n" +
			    "public team class Team433robic1 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public class Role433robic1 playedBy T433robic1 {\n" +
			    "        public String getValue(String arg) {\n" +
			    "            return value = arg;\n" +
			    "        }\n" +
			    "        String getValue(String arg) <- before String getValue(String arg) with {\n" +
			    "            arg <- arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "a|b");
    }

    // a role method with return type is callin-bound as 'after' and the return value is calculated
    // 4.3.3-otjld-result-of-binding-is-calculated-2
    public void test433_resultOfBindingIsCalculated2() {
       
       runConformTest(
            new String[] {
		"T433robic2Main.java",
			    "\n" +
			    "public class T433robic2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team433robic2 t = new Team433robic2();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "\n" +
			    "        T433robic2 o = new T433robic2();\n" +
			    "\n" +
			    "        System.out.print(o.getValue(\"b\")+\"|\"+t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T433robic2.java",
			    "\n" +
			    "public class T433robic2 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team433robic2.java",
			    "\n" +
			    "public team class Team433robic2 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public class Role433robic2 playedBy T433robic2 {\n" +
			    "        public String test(String arg) {\n" +
			    "            return value = arg;\n" +
			    "        }\n" +
			    "        test <- after getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "a|b");
    }

    // a role method is callin-bound as 'before' and uses result in a parameter mapping
    // 4.3.4-otjld-result-in-parameter-mapping
    public void test434_resultInParameterMapping() {
        runNegativeTest(
            new String[] {
		"T434ripm.java",
			    "\n" +
			    "public class T434ripm {\n" +
			    "    public String test(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team434ripm.java",
			    "\n" +
			    "public team class Team434ripm {\n" +
			    "    public class Role434ripm playedBy T434ripm {\n" +
			    "        public String test(String arg) {\n" +
			    "            return arg;\n" +
			    "        }\n" +
			    "        String test(String arg) <- before String test(String arg) with {\n" +
			    "            arg <- result,\n" +
			    "            \"OK\" -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role method is callin-bound as 'after' and the result of the base method is used
    // 4.3.5-otjld-result-in-parameter-mapping
    public void test435_resultInParameterMapping() {
       
       runConformTest(
            new String[] {
		"T435ripmMain.java",
			    "\n" +
			    "public class T435ripmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team435ripm t = new Team435ripm();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T435ripm o = new T435ripm();\n" +
			    "    \n" +
			    "            System.out.print(o.getValue(\"b\"));\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T435ripm.java",
			    "\n" +
			    "public class T435ripm {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return \"a\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team435ripm.java",
			    "\n" +
			    "public team class Team435ripm {\n" +
			    "    public class Role435ripm playedBy T435ripm {\n" +
			    "        public String test(String arg) {\n" +
			    "            return arg;\n" +
			    "        }\n" +
			    "        String test(String arg) <- after String getValue(String arg) with {\n" +
			    "            arg <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "a");
    }

    // a role method is callin-bound as 'replace' and uses result in a parameter mapping
    // 4.3.6-otjld-result-in-parameter-mapping
    public void test436_resultInParameterMapping() {
        runNegativeTest(
            new String[] {
		"T436ripm.java",
			    "\n" +
			    "public class T436ripm {\n" +
			    "    public String test(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team436ripm.java",
			    "\n" +
			    "public team class Team436ripm {\n" +
			    "    public class Role436ripm playedBy T436ripm {\n" +
			    "        callin void test(String arg) {\n" +
			    "            base.test(arg);\n" +
			    "        }\n" +
			    "        void test(String arg) <- replace String test(String arg) with {\n" +
			    "            arg <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // TPX-281 : continue compiling after syntax error in param-mapping
    // 4.3.7-otjld-illegal-keyword-in-parameter-mapping-1
    public void test437_illegalKeywordInParameterMapping1() {
        runNegativeTest(
            new String[] {
		"T437ikipm1.java",
			    "\n" +
			    "public class T437ikipm1 {\n" +
			    "    void bar() {};\n" +
			    "}\n" +
			    "    \n",
		"Team437ikipm1_2.java",
			    "\n" +
			    "public team class Team437ikipm1_2 {\n" +
			    "	public class Role1 {\n" +
			    "		public Role1()  {\n" +
			    "			tsuper();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"Team437ikipm1_1.java",
			    "\n" +
			    "public team class Team437ikipm1_1 {\n" +
			    "	public class Role2 playedBy T437ikipm1 {\n" +
			    "	    void foo() {}\n" +
			    "		void foo() <- after void bar() \n" +
			    "			with { x <- class };\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            null);
    }

    // callin parameter mapping uses base to denote the base instance
    // 4.3.8-otjld-base-in-parameter-mapping-1
    public void test438_baseInParameterMapping1() {
       
       runConformTest(
            new String[] {
		"Team438bipm1.java",
			    "\n" +
			    "public team class Team438bipm1 {\n" +
			    "    public class Role2 playedBy T438bipm1 {\n" +
			    "        void foo(T438bipm1 b) {\n" +
			    "            b.ok();\n" +
			    "        }\n" +
			    "        void foo(T438bipm1 b) <- after void bar() \n" +
			    "            with { b <- base };\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team438bipm1().activate();\n" +
			    "        new T438bipm1().bar();\n" +
			    "    }\n" +
			    "}   \n" +
			    "    \n",
		"T438bipm1.java",
			    "\n" +
			    "public class T438bipm1 {\n" +
			    "    void bar() {}\n" +
			    "    void ok() { \n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a base expression is used to retrieve a sibling role
    // 4.3.8-otjld-base-in-parameter-mapping-2
    public void test438_baseInParameterMapping2() {
       
       runConformTest(
            new String[] {
		"Team438bipm2.java",
			    "\n" +
			    "public team class Team438bipm2 {\n" +
			    "    public class R1 playedBy T438bipm2 {\n" +
			    "        protected void test() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }    \n" +
			    "    }\n" +
			    "    protected class R2 playedBy T438bipm2 {\n" +
			    "        public R1 getSibling() -> int hashCode() \n" +
			    "            with { result <- base }\n" +
			    "    }\n" +
			    "    public Team438bipm2(T438bipm2 as R2 r2) {\n" +
			    "        R1 r1 = r2.getSibling();\n" +
			    "        r1.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team438bipm2(new T438bipm2());\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T438bipm2.java",
			    "\n" +
			    "public class T438bipm2 {\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callin binding with multiple base methods defines a parameter mapping
    // 4.3.9-otjld-param-map-multiple-basemethods-1
    public void test439_paramMapMultipleBasemethods1() {
       
       runConformTest(
            new String[] {
		"Team439pmmb1.java",
			    "\n" +
			    "public team class Team439pmmb1 {\n" +
			    "    protected class R playedBy T439pmmb1 {\n" +
			    "        void exclamation(char c) {\n" +
			    "            System.out.print(c);\n" +
			    "        }\n" +
			    "        void exclamation(char c) <- after void o(), void k()\n" +
			    "        with { c <- '!' }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team439pmmb1().activate();\n" +
			    "        T439pmmb1 b = new T439pmmb1();\n" +
			    "        b.o();\n" +
			    "        b.k();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T439pmmb1.java",
			    "\n" +
			    "public class T439pmmb1 {\n" +
			    "    void o() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "    void k() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "O!K!");
    }

    // a callin binding with multiple base methods defines a parameter mapping
    // 4.3.9-otjld-param-map-multiple-basemethods-2
    public void test439_paramMapMultipleBasemethods2() {
       
       runConformTest(
            new String[] {
		"Team439pmmb2.java",
			    "\n" +
			    "public team class Team439pmmb2 {\n" +
			    "    protected class R playedBy T439pmmb2 {\n" +
			    "        void exclamation(String s) {\n" +
			    "            System.out.print(s);\n" +
			    "        }\n" +
			    "        void exclamation(String s) <- after void o(char c), void k(char c)\n" +
			    "        with { s <- \"\"+c+\"!\" }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team439pmmb2().activate();\n" +
			    "        T439pmmb2 b = new T439pmmb2();\n" +
			    "        b.o('O');\n" +
			    "        b.k('K');\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T439pmmb2.java",
			    "\n" +
			    "public class T439pmmb2 {\n" +
			    "    void o(char c) {\n" +
			    "        System.out.print(c);\n" +
			    "    }\n" +
			    "    void k(char c) {\n" +
			    "        System.out.print(c);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OO!KK!");
    }

    // parameters require all kinds of tweeking
    // 4.3.10-otjld-maximum-parameter-tweeking-1
    public void test4310_maximumParameterTweeking1() {
       Map customOptions = getCompilerOptions();
       
       runConformTest(
            new String[] {
		"T4310mpt1Main.java",
			    "\n" +
			    "public class T4310mpt1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team4310mpt1_2 t = new Team4310mpt1_2();\n" +
			    "        t.activate();\n" +
			    "        System.out.print(T4310mpt1.test(\"p\", \"q\", (short)0));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4310mpt1.java",
			    "\n" +
			    "public class T4310mpt1 {\n" +
			    "    static int test(String a, String b, short f) {\n" +
			    "        System.out.print(a+b+f);\n" +
			    "        return f+8;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4310mpt1_1.java",
			    "\n" +
			    "public team class Team4310mpt1_1 {\n" +
			    "    String e = \"e\";\n" +
			    "    Team4310mpt1_1() {\n" +
			    "        this.activate();\n" +
			    "    }\n" +
			    "    public class R1 playedBy T4310mpt1 {\n" +
			    "        static callin int rm1(short g1, String c1, String d1) {\n" +
			    "            System.out.print(c1);\n" +
			    "            return base.rm1((short)(g1+1), d1, Team4310mpt1_1.this.e);\n" +
			    "        }\n" +
			    "        int rm1(short g1, String c1, String d1) <- replace int test(String a, String b, short f)\n" +
			    "            with {\n" +
			    "                    g1       <- f,\n" +
			    "                    c1       <- b,\n" +
			    "                    d1       <- \"h\",\n" +
			    "                    result   -> result\n" +
			    "            }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team4310mpt1_2.java",
			    "\n" +
			    "public team class Team4310mpt1_2 { \n" +
			    "    String m = \"m\";\n" +
			    "    String n = \"n\";\n" +
			    "    final Team4310mpt1_1 that = new Team4310mpt1_1();\n" +
			    "    protected class R2 playedBy R1<@that> {\n" +
			    "        static callin int rm2(String j2, short k2, String l2) {\n" +
			    "            System.out.print(j2);\n" +
			    "            return base.rm2(l2, (short)(k2+4), Team4310mpt1_2.this.m);\n" +
			    "        }\n" +
			    "        @SuppressWarnings(\"decapsulation\")\n" +
			    "        int rm2(String j2, short k2, String l2) <- replace int rm1(short g1, String c1, String d1)\n" +
			    "            with {\n" +
			    "                j2         <- Team4310mpt1_2.this.n,\n" +
			    "                k2         <- g1,\n" +
			    "                l2         <- \"o\",\n" +
			    "                result    -> result\n" +
			    "            }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "nqph513",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a callin parameter is mapped and requires lifting (and lowering in base-call)
    // 4.3.11-otjld-translating-mapped-parameter-1
    public void test4311_translatingMappedParameter1() {
       
       runConformTest(
            new String[] {
		"Team4311tmp1.java",
			    "\n" +
			    "public team class Team4311tmp1 {\n" +
			    "    protected class R playedBy T4311tmp1 {\n" +
			    "        callin void test(int j, R other) {\n" +
			    "            base.test(j+1, other);\n" +
			    "        }\n" +
			    "        void test(int j, R other) <- replace void test(String s1, T4311tmp1 other, int i)\n" +
			    "            with {\n" +
			    "                j <- i,\n" +
			    "                other <- other\n" +
			    "            }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4311tmp1().activate();\n" +
			    "        new T4311tmp1().test(\"O\", new T4311tmp1(), 3);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4311tmp1.java",
			    "\n" +
			    "public class T4311tmp1 {\n" +
			    "    void test(String s1, T4311tmp1 other, int i) {\n" +
			    "        System.out.print(s1+other.getS()+i);\n" +
			    "    }\n" +
			    "    private String getS() { return \"S\"; }\n" +
			    "}\n" +
			    "    \n"
            },
            "OS4");
    }

    // a callin parameter is mapped from a "new" expression and requires lifting (no lowering in base-call)
    // 4.3.11-otjld-translating-mapped-parameter-2
    public void test4311_translatingMappedParameter2() {
       
       runConformTest(
            new String[] {
		"Team4311tmp2.java",
			    "\n" +
			    "public team class Team4311tmp2 {\n" +
			    "    protected class R playedBy T4311tmp2 {\n" +
			    "        callin void test(int j, R other) {\n" +
			    "            System.out.print(other.getS());\n" +
			    "            base.test(j+1, other);\n" +
			    "        }\n" +
			    "        void test(int j, R other) <- replace void test(String s1, int i)\n" +
			    "            with {\n" +
			    "                j <- i,\n" +
			    "                other <- new T4311tmp2()\n" +
			    "            }\n" +
			    "        @SuppressWarnings(\"decapsulation\")\n" +
			    "        public String getS() -> String getS();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4311tmp2().activate();\n" +
			    "        new T4311tmp2().test(\"O\", 3);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4311tmp2.java",
			    "\n" +
			    "public class T4311tmp2 {\n" +
			    "    void test(String s1, int i) {\n" +
			    "        System.out.print(s1+i);\n" +
			    "    }\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    private String getS() { return \"S\"; }\n" +
			    "}\n" +
			    "    \n"
            },
            "SO4");
    }

    // a callin parameter is mapped and requires lifting (and lowering in base-call)
    // 4.3.11-otjld-translating-mapped-parameter-3
    public void test4311_translatingMappedParameter3() {
       
       runConformTest(
            new String[] {
		"Team4311tmp3.java",
			    "\n" +
			    "public team class Team4311tmp3 {\n" +
			    "    protected class R playedBy T4311tmp3 {\n" +
			    "        callin R test(int j) {\n" +
			    "            return base.test(j+1);\n" +
			    "        }\n" +
			    "        R test(int j) <- replace T4311tmp3 test(String s1, int i)\n" +
			    "            with {\n" +
			    "                j <- i,\n" +
			    "                result -> result\n" +
			    "            }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team4311tmp3().activate();\n" +
			    "        T4311tmp3 t = new T4311tmp3();\n" +
			    "        t = t.test(\"O\", 3);\n" +
			    "        System.out.print(t.getVal());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T4311tmp3.java",
			    "\n" +
			    "public class T4311tmp3 {\n" +
			    "    final String val;\n" +
			    "    T4311tmp3() {\n" +
			    "        val = \"NOK\";\n" +
			    "    }\n" +
			    "    T4311tmp3(String s) {\n" +
			    "        val = s;\n" +
			    "    }\n" +
			    "    T4311tmp3 test(String s1, int i) {\n" +
			    "        return new T4311tmp3(s1+i);\n" +
			    "    }\n" +
			    "    String getVal() { return val; }\n" +
			    "}\n" +
			    "    \n"
            },
            "O4");
    }

    // missing callin modifier (no signature)
    // 4.3.12-otjld-invalid-callin-binding-1
    public void test4312_invalidCallinBinding1() {
        runNegativeTestMatching(
            new String[] {
		"Team4312icb1.java",
			    "\n" +
			    "public team class Team4312icb1 {\n" +
			    "    public class R playedBy String {\n" +
			    "        overriding:\n" +
			    "        toString <- toString;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "A.3.3");
    }

    // missing callin modifier (with signature)
    // 4.3.12-otjld-invalid-callin-binding-2
    public void test4312_invalidCallinBinding2() {
        runNegativeTestMatching(
            new String[] {
		"Team4312icb2.java",
			    "\n" +
			    "public team class Team4312icb2 {\n" +
			    "    public class R playedBy String {\n" +
			    "        String toString() <- String toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "A.3.3");
    }

    // no signature, but parameter mapping
    // 4.3.12-otjld-invalid-callin-binding-3
    public void test4312_invalidCallinBinding3() {
        runNegativeTestMatching(
            new String[] {
		"Team4312icb3.java",
			    "\n" +
			    "public team class Team4312icb3 {\n" +
			    "    public class R playedBy String {\n" +
			    "        overriding:\n" +
			    "        toString <- after toString\n" +
			    "            with { result <- result.toUpperCase() }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "4.4(a)");
    }

    // no label, no signature, but parameter mapping
    // 4.3.12-otjld-invalid-callin-binding-4
    public void test4312_invalidCallinBinding4() {
        runNegativeTestMatching(
            new String[] {
		"Team4312icb4.java",
			    "\n" +
			    "public team class Team4312icb4 {\n" +
			    "    public class R playedBy String {\n" +
			    "        toString <- replace toString\n" +
			    "            with { result <- result.toUpperCase() }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "4.4(a)");
    }

    public void testBug469204() {
    	runConformTest(
    		new String[] {
    	"TBug469204.java",
    			"public class TBug469204 {\n" +
    			"	public static void main(String... args) {\n" +
    			"		new TeamBug469204().activate();\n" +
    			"		new Base469204_2().bm(new Base469204_1());\n" +
    			"	}\n" +
    			"}\n",
    	"TeamBug469204.java",
    			"public team class TeamBug469204 {\n" +
    			"	protected team class Mid playedBy Base469204_2 {\n" +
    			"		protected class Inner playedBy Base469204_1 {\n" +
    			"		}\n" +
    			"		callin void rm(Inner i) { System.out.print(i.getClass().getName()); }\n" +
    			"		void rm(Inner i) <- replace void bm(Base469204_1 b) with {\n" +
    			"			i <- b\n" +
    			"		}\n" +
    			"	}\n" +
    			"}\n",
    	"Base469204_1.java",
    			"public class Base469204_1 {}\n",
    	"Base469204_2.java",
    			"public class Base469204_2 {\n" +
    			"	void bm(Base469204_1 b) {}\n" +
    			"}\n"
    		},
    		"TeamBug469204$__OT__Mid$__OT__Inner");
    }
}
