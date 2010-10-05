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
package org.eclipse.objectteams.otdt.tests.otjld.other;

import java.util.Map;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

@SuppressWarnings("unchecked")
public class Exceptions extends AbstractOTJLDTest {
	
	public Exceptions(String name) {
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
		return Exceptions.class;
	}

    // a callout binding does not redeclare all exceptions that its base method declares
    // 7.5.1-otjld-callout-doesnt-redeclare-base-exceptions-1
    public void test751_calloutDoesntRedeclareBaseExceptions1() {
        runNegativeTestMatching(
            new String[] {
		"Team751cdrbe1.java",
			    "\n" +
			    "public team class Team751cdrbe1 {\n" +
			    "    public class Role751cdrbe1 playedBy T751cdrbe1 {\n" +
			    "        public abstract void test();\n" +
			    "        test -> test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T751cdrbe1.java",
			    "\n" +
			    "public class T751cdrbe1 {\n" +
			    "    public void test() throws java.io.IOException {\n" +
			    "        throw new java.io.IOException();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3.1(h)");
    }

    // a callout binding does not redeclare all exceptions that its base method declares
    // 7.5.1-otjld-callout-doesnt-redeclare-base-exceptions-2
    public void test751_calloutDoesntRedeclareBaseExceptions2() {
        runNegativeTestMatching(
            new String[] {
		"Team751cdrbe2.java",
			    "\n" +
			    "public team class Team751cdrbe2 {\n" +
			    "    public class Role751cdrbe2_1 playedBy T751cdrbe2 {\n" +
			    "        public void doSomething() throws Exception751cdrbe2, IllegalAccessException {}\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role751cdrbe2_2 extends Role751cdrbe2_1 {\n" +
			    "        doSomething => test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Exception751cdrbe2.java",
			    "\n" +
			    "public class Exception751cdrbe2 extends Exception {}\n" +
			    "    \n",
		"T751cdrbe2.java",
			    "\n" +
			    "public class T751cdrbe2 {\n" +
			    "    public void test() throws IllegalAccessException, Exception751cdrbe2, InstantiationException {\n" +
			    "        throw new Exception751cdrbe2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3.1(h)");
    }

    // a callout binding redeclares all exceptions that its base method declares
    // 7.5.2-otjld-callout-redeclares-all-base-exceptions-1
    public void test752_calloutRedeclaresAllBaseExceptions1() {
       
       runConformTest(
            new String[] {
		"T752crabe1Main.java",
			    "\n" +
			    "public class T752crabe1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team752crabe1 t = new Team752crabe1();\n" +
			    "        T752crabe1    o = new T752crabe1();\n" +
			    "\n" +
			    "        try {\n" +
			    "            t.test(o);\n" +
			    "        } catch (Exception ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T752crabe1.java",
			    "\n" +
			    "public class T752crabe1 {\n" +
			    "    public void test() throws java.io.IOException, IllegalAccessException {\n" +
			    "        throw new java.io.IOException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team752crabe1.java",
			    "\n" +
			    "public team class Team752crabe1 {\n" +
			    "    public class Role752crabe1 playedBy T752crabe1 {\n" +
			    "        public abstract void test() throws java.io.IOException, IllegalAccessException;\n" +
			    "        test -> test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T752crabe1 as Role752crabe1 obj) throws java.io.IOException, IllegalAccessException {\n" +
			    "        obj.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callout binding redeclares all exceptions needing declaration of what its base method declares
    // 7.5.2-otjld-callout-redeclares-all-base-exceptions-2
    public void test752_calloutRedeclaresAllBaseExceptions2() {
       
       runConformTest(
            new String[] {
		"T752crabe2Main.java",
			    "\n" +
			    "public class T752crabe2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team752crabe2 t = new Team752crabe2();\n" +
			    "        T752crabe2    o = new T752crabe2();\n" +
			    "\n" +
			    "        try {\n" +
			    "            t.test(o);\n" +
			    "        } catch (Exception ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T752crabe2.java",
			    "\n" +
			    "public class T752crabe2 {\n" +
			    "    public void test() throws java.io.IOException, NullPointerException, IllegalAccessException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team752crabe2.java",
			    "\n" +
			    "public team class Team752crabe2 {\n" +
			    "    public class Role752crabe2_1 playedBy T752crabe2 {\n" +
			    "        public void test() throws IllegalAccessException, java.io.IOException {\n" +
			    "            throw new java.io.IOException(\"NOTOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role752crabe2_2 extends Role752crabe2_1 {\n" +
			    "        test => test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T752crabe2 as Role752crabe2_2 obj) throws Exception {\n" +
			    "        obj.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a shorthand callout binding implicitly declares all exceptions needing declaration of what its base method declares
    // 7.5.2-otjld-callout-redeclares-all-base-exceptions-3
    public void test752_calloutRedeclaresAllBaseExceptions3() {
       
       runConformTest(
            new String[] {
		"T752crabe3Main.java",
			    "\n" +
			    "public class T752crabe3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team752crabe3 t = new Team752crabe3();\n" +
			    "        T752crabe3    o = new T752crabe3();\n" +
			    "\n" +
			    "        try {\n" +
			    "            t.test(o);\n" +
			    "        } catch (Exception ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T752crabe3.java",
			    "\n" +
			    "public class T752crabe3 {\n" +
			    "    public void test() throws java.io.IOException, NullPointerException, IllegalAccessException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team752crabe3.java",
			    "\n" +
			    "public team class Team752crabe3 {\n" +
			    "\n" +
			    "    public class Role752crabe3 playedBy T752crabe3  {\n" +
			    "        protected void test() -> void test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T752crabe3 as Role752crabe3 obj) throws Exception {\n" +
			    "        obj.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callout binding redeclares all exceptions needing declaration of what its base method declares
    // 7.5.2-otjld-callout-redeclares-all-base-exceptions-4
    public void test752_calloutRedeclaresAllBaseExceptions4() {
        runNegativeTestMatching(
            new String[] {
		"Team752crabe4.java",
			    "\n" +
			    "public team class Team752crabe4 {\n" +
			    "\n" +
			    "    public class Role752crabe4 playedBy T752crabe4  {\n" +
			    "        protected void test() -> void test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T752crabe4 as Role752crabe4 obj) {\n" +
			    "        obj.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T752crabe4.java",
			    "\n" +
			    "public class T752crabe4 {\n" +
			    "    public void test() throws java.io.IOException, NullPointerException, IllegalAccessException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Unhandled exception");
    }

    // an inferred callout passes through all exceptions of its base method
    // 7.5.2-otjld-callout-redeclares-all-base-exceptions-5
    public void test752_calloutRedeclaresAllBaseExceptions5() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportInferredCallout, CompilerOptions.WARNING);
       
       runConformTest(
            new String[] {
		"T752crabe5Main.java",
			    "\n" +
			    "public class T752crabe5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team752crabe5 t = new Team752crabe5();\n" +
			    "        T752crabe5    o = new T752crabe5();\n" +
			    "\n" +
			    "        try {\n" +
			    "            t.test(o);\n" +
			    "        } catch (Exception ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T752crabe5.java",
			    "\n" +
			    "public class T752crabe5 {\n" +
			    "    public void test() throws java.io.IOException, NullPointerException, IllegalAccessException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team752crabe5.java",
			    "\n" +
			    "public team class Team752crabe5 {\n" +
			    "\n" +
			    "    public class Role752crabe5 playedBy T752crabe5  {\n" +
			    "        @SuppressWarnings(\"inferredcallout\")\n" +
			    "        protected void roleMethod() throws Exception {\n" +
			    "            test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T752crabe5 as Role752crabe5 obj) throws Exception {\n" +
			    "        obj.roleMethod();\n" +
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

    // an inferred callout passes through all exceptions of its base method - call site does not handle exception
    // 7.5.2-otjld-callout-redeclares-all-base-exceptions-6
    public void test752_calloutRedeclaresAllBaseExceptions6() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportInferredCallout, CompilerOptions.WARNING);
        runNegativeTest(
            new String[] {
		"Team752crabe6.java",
			    "\n" +
			    "public team class Team752crabe6 {\n" +
			    "\n" +
			    "    public class Role752crabe6 playedBy T752crabe6  {\n" +
			    "        @SuppressWarnings(\"inferredcallout\")\n" +
			    "        protected void roleMethod() {\n" +
			    "            test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T752crabe6 as Role752crabe6 obj) throws Exception {\n" +
			    "        obj.roleMethod();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T752crabe6.java",
			    "\n" +
			    "public class T752crabe6 {\n" +
			    "    public void test() throws java.io.IOException, NullPointerException, IllegalAccessException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
			"1. ERROR in Team752crabe6.java (at line 7)\n" +
			"	test();\n" +
			"	^^^^^^\n" +
			"Unhandled exception type IOException\n" +
			"----------\n",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            customOptions);
    }

    // a callout binding redeclares all exceptions that its base method declares, plus additional ones
    // 7.5.3-otjld-callout-declares-additional-exceptions-1
    public void test753_calloutDeclaresAdditionalExceptions1() {
       
       runConformTest(
            new String[] {
		"T753cdae1Main.java",
			    "\n" +
			    "public class T753cdae1Main {\n" +
			    "    public static void main(String[] args) throws Exception {\n" +
			    "        Team753cdae1 t = new Team753cdae1();\n" +
			    "        T753cdae1    o = new T753cdae1();\n" +
			    "\n" +
			    "        try {\n" +
			    "            t.test(o);\n" +
			    "        } catch (NullPointerException ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T753cdae1.java",
			    "\n" +
			    "public class T753cdae1 {\n" +
			    "    public void test() throws java.io.IOException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team753cdae1.java",
			    "\n" +
			    "public team class Team753cdae1 {\n" +
			    "    public class Role753cdae1_1 playedBy T753cdae1 {\n" +
			    "        public void test() throws IllegalAccessException, java.io.IOException {\n" +
			    "            throw new IllegalAccessException(\"NOTOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role753cdae1_2 extends Role753cdae1_1 {\n" +
			    "        test => test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T753cdae1 as Role753cdae1_2 obj) throws Exception {\n" +
			    "        obj.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callout binding redeclares all exceptions that its base method declares, plus additional ones
    // 7.5.3-otjld-callout-declares-additional-exceptions-2
    public void test753_calloutDeclaresAdditionalExceptions2() {
       
       runConformTest(
            new String[] {
		"T753cdae2Main.java",
			    "\n" +
			    "public class T753cdae2Main {\n" +
			    "    public static void main(String[] args) throws Exception {\n" +
			    "        Team753cdae2 t = new Team753cdae2();\n" +
			    "        T753cdae2    o = new T753cdae2();\n" +
			    "\n" +
			    "        try {\n" +
			    "            t.test(o);\n" +
			    "        } catch (java.io.IOException ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T753cdae2.java",
			    "\n" +
			    "public class T753cdae2 {\n" +
			    "    public void test() throws java.io.IOException, IllegalAccessException {\n" +
			    "        throw new java.io.IOException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team753cdae2.java",
			    "\n" +
			    "public team class Team753cdae2 {\n" +
			    "    public class Role753cdae2 playedBy T753cdae2 {\n" +
			    "        public abstract void test() throws NullPointerException, IllegalAccessException, java.io.IOException;\n" +
			    "        test -> test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T753cdae2 as Role753cdae2 obj) throws Exception {\n" +
			    "        obj.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callout binding does not redeclare all exceptions that its base method declares, but additional ones
    // 7.5.4-otjld-callout-doesnt-redeclare-base-exceptions
    public void test754_calloutDoesntRedeclareBaseExceptions() {
        runNegativeTest(
            new String[] {
		"Exception754cdrbe.java",
			    "\n" +
			    "public class Exception754cdrbe extends Exception {}\n" +
			    "    \n",
		"T754cdrbe.java",
			    "\n" +
			    "public class T754cdrbe {\n" +
			    "    public void test() throws java.io.IOException, IllegalAccessException {\n" +
			    "        throw new java.io.IOException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team754cdrbe.java",
			    "\n" +
			    "public team class Team754cdrbe {\n" +
			    "    public class Role754cdrbe playedBy T754cdrbe {\n" +
			    "        public abstract void test() throws Exception754cdrbe, java.io.IOException;\n" +
			    "        test -> test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callin binding redeclares some but not all exceptions that its base method declares
    // 7.5.5-otjld-callin-redeclares-some-base-exceptions-1
    public void test755_callinRedeclaresSomeBaseExceptions1() {
       
       runConformTest(
            new String[] {
		"T755crsbe1Main.java",
			    "\n" +
			    "public class T755crsbe1Main {\n" +
			    "    public static void main(String[] args) throws Exception {\n" +
			    "        Team755crsbe1 t = new Team755crsbe1();\n" +
			    "        T755crsbe1    o = new T755crsbe1();\n" +
			    "\n" +
			    "        try {\n" +
			    "            within (t) {\n" +
			    "                o.test();\n" +
			    "            }\n" +
			    "        } catch (java.io.IOException ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T755crsbe1.java",
			    "\n" +
			    "public class T755crsbe1 {\n" +
			    "    public void test() throws java.io.IOException, IllegalAccessException {\n" +
			    "        throw new NullPointerException(\"NOTOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team755crsbe1.java",
			    "\n" +
			    "public team class Team755crsbe1 {\n" +
			    "    public class Role755crsbe1_1 playedBy T755crsbe1 {\n" +
			    "        public void test() throws java.io.IOException {\n" +
			    "            throw new java.io.IOException(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role755crsbe1_2 extends Role755crsbe1_1 {\n" +
			    "        test <- before test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callin binding redeclares some but not all exceptions that its base method declares
    // 7.5.5-otjld-callin-redeclares-some-base-exceptions-2
    public void test755_callinRedeclaresSomeBaseExceptions2() {
       
       runConformTest(
            new String[] {
		"T755crsbe2Main.java",
			    "\n" +
			    "public class T755crsbe2Main {\n" +
			    "    public static void main(String[] args) throws Exception {\n" +
			    "        Team755crsbe2 t = new Team755crsbe2();\n" +
			    "        T755crsbe2    o = new T755crsbe2();\n" +
			    "\n" +
			    "        t.activate();\n" +
			    "        try {\n" +
			    "            o.test();\n" +
			    "        } catch (IllegalAccessException ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        }\n" +
			    "        t.deactivate();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Exception755crsbe2.java",
			    "\n" +
			    "public class Exception755crsbe2 extends Exception {}\n" +
			    "    \n",
		"T755crsbe2.java",
			    "\n" +
			    "public class T755crsbe2 {\n" +
			    "    public void test() throws java.io.IOException, Exception755crsbe2, NullPointerException, IllegalAccessException {\n" +
			    "        throw new java.io.IOException(\"NOTOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team755crsbe2.java",
			    "\n" +
			    "public team class Team755crsbe2 {\n" +
			    "    public class Role755crsbe2 playedBy T755crsbe2 {\n" +
			    "        callin void doSomething() throws IllegalAccessException, Exception755crsbe2 {\n" +
			    "            // we don't want to call the base method, otherwise we'd have to redeclare every base exception\n" +
			    "            throw new IllegalAccessException(\"OK\");\n" +
			    "        }\n" +
			    "        void doSomething() <- replace void test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callin binding redeclares all exceptions that its base method declares
    // 7.5.6-otjld-callin-redeclares-all-base-exceptions-1
    public void test756_callinRedeclaresAllBaseExceptions1() {
       
       runConformTest(
            new String[] {
		"T756crabe1Main.java",
			    "\n" +
			    "public class T756crabe1Main {\n" +
			    "    public static void main(String[] args) throws Exception {\n" +
			    "        Team756crabe1_2 t = new Team756crabe1_2();\n" +
			    "        T756crabe1      o = new T756crabe1();\n" +
			    "\n" +
			    "        try {\n" +
			    "            t.activate();\n" +
			    "            o.test();\n" +
			    "        } catch (NullPointerException ex) {\n" +
			    "            System.out.print(ex.getMessage());\n" +
			    "        } finally {\n" +
			    "            t.deactivate();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T756crabe1.java",
			    "\n" +
			    "public class T756crabe1 {\n" +
			    "    public void test() throws java.io.IOException, IllegalAccessException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team756crabe1_1.java",
			    "\n" +
			    "public team class Team756crabe1_1 {\n" +
			    "    public class Role756crabe1_1 playedBy T756crabe1 {\n" +
			    "        public void testInternal() throws IllegalAccessException, java.io.IOException {\n" +
			    "            throw new IllegalAccessException(\"NOTOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team756crabe1_2.java",
			    "\n" +
			    "public team class Team756crabe1_2 extends Team756crabe1_1 {\n" +
			    "    public class Role756crabe1_2 extends Role756crabe1_1 {\n" +
			    "        testInternal <- after test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callin binding redeclares all exceptions that its base method declares
    // 7.5.6-otjld-callin-redeclares-all-base-exceptions-2
    public void test756_callinRedeclaresAllBaseExceptions2() {
       
       runConformTest(
            new String[] {
		"T756crabe2Main.java",
			    "\n" +
			    "public class T756crabe2Main {\n" +
			    "    public static void main(String[] args) throws Exception {\n" +
			    "        Team756crabe2 t = new Team756crabe2();\n" +
			    "        T756crabe2    o = new T756crabe2();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            try {\n" +
			    "                o.test();\n" +
			    "            } catch (NullPointerException ex) {\n" +
			    "                System.out.print(ex.getMessage());\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T756crabe2.java",
			    "\n" +
			    "public class T756crabe2 {\n" +
			    "    public void test() throws java.io.IOException, IllegalAccessException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team756crabe2.java",
			    "\n" +
			    "public team class Team756crabe2 {\n" +
			    "    public class Role756crabe2 playedBy T756crabe2 {\n" +
			    "        callin void test() throws java.io.IOException, NullPointerException, IllegalAccessException {\n" +
			    "            base.test();\n" +
			    "            throw new IllegalAccessException(\"NOTOK\");\n" +
			    "        }\n" +
			    "\n" +
			    "        test <- replace test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callin binding redeclares all exceptions that its base method declares plus additional ones
    // 7.5.7-otjld-callin-declares-additional-exceptions-1
    public void test757_callinDeclaresAdditionalExceptions1() {
        runNegativeTest(
            new String[] {
		"Exception757cdae1.java",
			    "\n" +
			    "public class Exception757cdae1 extends Exception {}\n" +
			    "    \n",
		"T757cdae1.java",
			    "\n" +
			    "public class T757cdae1 {\n" +
			    "    public String test() throws java.io.IOException, IllegalAccessException {\n" +
			    "        throw new IllegalAccessException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team757cdae1.java",
			    "\n" +
			    "public team class Team757cdae1 {\n" +
			    "    public class Role757cdae1 playedBy T757cdae1 {\n" +
			    "        public void beforeTest() throws java.io.IOException, IllegalAccessException, Exception757cdae1 {\n" +
			    "            throw new Exception757cdae1(\"NOTOK\");\n" +
			    "        }\n" +
			    "\n" +
			    "        beforeTest <- before test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callin binding redeclares all exceptions that its base method declares plus additional ones
    // 7.5.7-otjld-callin-declares-additional-exceptions-2
    public void test757_callinDeclaresAdditionalExceptions2() {
        runNegativeTest(
            new String[] {
		"Exception757cdae2.java",
			    "\n" +
			    "public class Exception757cdae2 extends Exception {}\n" +
			    "    \n",
		"T757cdae2.java",
			    "\n" +
			    "public class T757cdae2 {\n" +
			    "    public void test() throws java.io.IOException, IllegalAccessException {\n" +
			    "        throw new IllegalAccessException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team757cdae2.java",
			    "\n" +
			    "public team class Team757cdae2 {\n" +
			    "    public class Role757cdae2 playedBy T757cdae2 {\n" +
			    "        callin void test() throws Exception757cdae2, java.io.IOException, IllegalAccessException {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "\n" +
			    "        test <- replace test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callin binding redeclares no exceptions that its base method declares but other ones
    // 7.5.8-otjld-callin-declares-other-exceptions-1
    public void test758_callinDeclaresOtherExceptions1() {
        runNegativeTestMatching(
            new String[] {
		"Team758cdoe1.java",
			    "\n" +
			    "public team class Team758cdoe1 {\n" +
			    "    public class Role758cdoe1 playedBy T758cdoe1 {\n" +
			    "        callin String test() throws IllegalAccessException {\n" +
			    "            base.test();\n" +
			    "            throw new IllegalAccessException(\"NOTOK\");\n" +
			    "        }\n" +
			    "\n" +
			    "        test <- replace test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T758cdoe1.java",
			    "\n" +
			    "public class T758cdoe1 {\n" +
			    "    public String test() throws java.io.IOException, NullPointerException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "4.1(g)");
    }

    // a callin binding fails to declare exception that its base method declares and issues a base call
    // 7.5.8-otjld-callin-declares-other-exceptions-2
    public void test758_callinDeclaresOtherExceptions2() {
        runNegativeTestMatching(
            new String[] {
		"Team758cdoe2.java",
			    "\n" +
			    "public team class Team758cdoe2 {\n" +
			    "    public class Role758cdoe2 playedBy T758cdoe2 {\n" +
			    "        callin String test() {\n" +
			    "            return base.test();\n" +
			    "        }\n" +
			    "\n" +
			    "        test <- replace test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T758cdoe2.java",
			    "\n" +
			    "public class T758cdoe2 {\n" +
			    "    public String test() throws java.io.IOException, NullPointerException {\n" +
			    "        throw new NullPointerException(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Unhandled");
    }

    // a callin binding catches an exception thrown by its base-call
    // 7.5.8-otjld-callin-declares-other-exceptions-3
    public void test758_callinDeclaresOtherExceptions3() {
       
       runConformTest(
            new String[] {
		"Team758cdoe3.java",
			    "\n" +
			    "public team class Team758cdoe3 {\n" +
			    "    public class Role758cdoe3 playedBy T758cdoe3 {\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        callin String test()  {\n" +
			    "            try {\n" +
			    "                return base.test();\n" +
			    "            } catch (java.io.IOException ex) {\n" +
			    "                return \"OK\";\n" +
			    "            }\n" +
			    "        }\n" +
			    "\n" +
			    "        test <- replace test;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team758cdoe3().activate(); \n" +
			    "        try {\n" +
			    "            System.out.print(new T758cdoe3().test());\n" +
			    "        } catch (java.io.IOException ex) {\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T758cdoe3.java",
			    "\n" +
			    "public class T758cdoe3 {\n" +
			    "    public String test() throws java.io.IOException, NullPointerException {\n" +
			    "        throw new java.io.IOException(\"ups\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }
}
