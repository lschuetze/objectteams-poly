/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010, 2014 Stephan Herrmann
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

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

public class ImplicitInheritance extends AbstractOTJLDTest {
	
	public ImplicitInheritance(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test0c15_overrideBoundSuperRole"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return ImplicitInheritance.class;
	}

    // implicit sub-role overrides method with role signature
    // 0.c.1-otjld-implicit-overrides-1
    public void test0c1_implicitOverrides1() {
       
       runConformTest(
            new String[] {
		"T0c112.java",
			    "\n" +
			    "public team class T0c112 extends T0c111 {\n" +
			    "	public class R {\n" +
			    "		protected String value(R other) {\n" +
			    "			return \"OK\";\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public T0c112 () {\n" +
			    "		R r = new R();\n" +
			    "		System.out.print(r.value(new R()));\n" +
			    "	}\n" +
			    "	public static void main (String[] args) {\n" +
			    "		new T0c112();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T0c111.java",
			    "\n" +
			    "public team class T0c111 {\n" +
			    "	public class R {\n" +
			    "		protected String value(R other) {\n" +
			    "			return \"NOTOK\";\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a constructor with self call is copied as tsuper, adjustment required
    // 0.c.2-otjld-tsuper-in-constructor-1
    public void test0c2_tsuperInConstructor1() {
       
       runConformTest(
            new String[] {
		"Team0c2tic1_2.java",
			    "\n" +
			    "public team class Team0c2tic1_2 extends Team0c2tic1_1 {\n" +
			    "	protected class R {\n" +
			    "		protected R () {\n" +
			    "			tsuper();\n" +
			    "			val = val.toUpperCase();\n" +
			    "		}\n" +
			    "		R(String v) {\n" +
			    "			val = \"NOK\";\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public Team0c2tic1_2 () {\n" +
			    "		R r = new R();\n" +
			    "		System.out.print(r.getVal());\n" +
			    "	}\n" +
			    "	public static void main (String[] args) {\n" +
			    "		new Team0c2tic1_2();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c2tic1_1.java",
			    "\n" +
			    "public team class Team0c2tic1_1 {\n" +
			    "	protected class R {\n" +
			    "		String val;\n" +
			    "		R () {\n" +
			    "			this(\"o\");\n" +
			    "		}\n" +
			    "		R (String v) {\n" +
			    "			val = v+\"k\";\n" +
			    "		}\n" +
			    "                protected String getVal() { return val; }\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a constructor with self call is copied as tsuper, ctor called in self call not overridden
    // 0.c.2-otjld-tsuper-in-constructor-2
    public void test0c2_tsuperInConstructor2() {
       
       runConformTest(
            new String[] {
		"Team0c2tic2_2.java",
			    "\n" +
			    "public team class Team0c2tic2_2 extends Team0c2tic2_1 {\n" +
			    "	protected class R {\n" +
			    "		protected R () {\n" +
			    "			tsuper();\n" +
			    "			val = val.toUpperCase();\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public Team0c2tic2_2 () {\n" +
			    "		R r = new R();\n" +
			    "		System.out.print(r.getVal());\n" +
			    "	}\n" +
			    "	public static void main (String[] args) {\n" +
			    "		new Team0c2tic2_2();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c2tic2_1.java",
			    "\n" +
			    "public team class Team0c2tic2_1 {\n" +
			    "	protected class R {\n" +
			    "		String val;\n" +
			    "		protected R () {\n" +
			    "			this(\"o\");\n" +
			    "		}\n" +
			    "		R (String v) {\n" +
			    "			val = v+\"k\";\n" +
			    "		}\n" +
			    "                public String getVal() { return val; }\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a team overrides a method returning role
    // 0.c.3-otjld-override-method-with-roletype-1
    public void test0c3_overrideMethodWithRoletype1() {
       
       runConformTest(
            new String[] {
		"T0c3omwr1Main.java",
			    "\n" +
			    "public class T0c3omwr1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		final Team0c3omwr1_1 t = new Team0c3omwr1_2();\n" +
			    "		Role<@t> r = t.getR();\n" +
			    "		System.out.print(r.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c3omwr1_1.java",
			    "\n" +
			    "public team class Team0c3omwr1_1 {\n" +
			    "	public class Role {\n" +
			    "		String val;\n" +
			    "		public Role(String v) {\n" +
			    "			val = v;\n" +
			    "		}\n" +
			    "		public String getValue() { return val+\"O\"; }\n" +
			    "	}\n" +
			    "	public Role getR () {\n" +
			    "		return new Role(\"N\");\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c3omwr1_2.java",
			    "\n" +
			    "public team class Team0c3omwr1_2 extends Team0c3omwr1_1 {\n" +
			    "	public class Role {\n" +
			    "		public String getValue() { return val+\"K\"; }\n" +
			    "	}\n" +
			    "	public Role getR() {\n" +
			    "		return new Role(\"O\");\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a team overrides a method returning role using super
    // 0.c.3-otjld-override-method-with-roletype-2
    public void test0c3_overrideMethodWithRoletype2() {
       
       runConformTest(
            new String[] {
		"T0c3omwr2Main.java",
			    "\n" +
			    "public class T0c3omwr2Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		final Team0c3omwr2_1 t = new Team0c3omwr2_2();\n" +
			    "		Role<@t> r = t.getR(\"X\");\n" +
			    "		System.out.print(r.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c3omwr2_1.java",
			    "\n" +
			    "public team class Team0c3omwr2_1 {\n" +
			    "	public class Role {\n" +
			    "		String val;\n" +
			    "		public Role(String v) {\n" +
			    "			val = v;\n" +
			    "		}\n" +
			    "		public String getValue() { return val+\"O\"; }\n" +
			    "	}\n" +
			    "	public Role getR (String v) {\n" +
			    "		return new Role(v);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c3omwr2_2.java",
			    "\n" +
			    "public team class Team0c3omwr2_2 extends Team0c3omwr2_1 {\n" +
			    "	public class Role {\n" +
			    "		public String getValue() { return val+\"K\"; }\n" +
			    "	}\n" +
			    "	public Role getR(String v) {\n" +
			    "		return super.getR(\"O\");\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a role overrides an implicitly inherited method return a role type
    // 0.c.3-otjld-override-method-with-roletype-3
    public void test0c3_overrideMethodWithRoletype3() {
       
       runConformTest(
            new String[] {
		"T0c3omwr3Main.java",
			    "\n" +
			    "public class T0c3omwr3Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team0c3omwr3_2();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c3omwr3_1.java",
			    "\n" +
			    "public team class Team0c3omwr3_1 {\n" +
			    "	public class Role {\n" +
			    "		public Role getR() {\n" +
			    "			return this;\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c3omwr3_2.java",
			    "\n" +
			    "public team class Team0c3omwr3_2 extends Team0c3omwr3_1 {\n" +
			    "	private Role r = new Role(\"OK\");\n" +
			    "	public class Role {\n" +
			    "		String val;\n" +
			    "		public Role(String v) { val = v; }\n" +
			    "		public Role getR() {\n" +
			    "			return Team0c3omwr3_2.this.r;\n" +
			    "		}\n" +
			    "		public String getValue() { return val; }\n" +
			    "	}\n" +
			    "	public Team0c3omwr3_2() {\n" +
			    "		Role r1 = new Role(\"NOK\");\n" +
			    "		Role r2 = r1.getR();\n" +
			    "		System.out.print(r2.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a role implicitly inherits an abstract method
    // 0.c.4-otjld-copy-abstract-method-1
    public void test0c4_copyAbstractMethod1() {
       
       runConformTest(
            new String[] {
		"Team0c4cam1_3.java",
			    "\n" +
			    "public team class Team0c4cam1_3 extends Team0c4cam1_2 {\n" +
			    "	public class Role {\n" +
			    "		public String getValue() { return \"OK\"; }\n" +
			    "	}\n" +
			    "	public Team0c4cam1_3() {\n" +
			    "		System.out.print((new Role()).getValue());\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team0c4cam1_3();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c4cam1_1.java",
			    "\n" +
			    "public team class Team0c4cam1_1 {\n" +
			    "	public abstract class Role {\n" +
			    "		public abstract String getValue();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c4cam1_2.java",
			    "\n" +
			    "public team class Team0c4cam1_2 extends Team0c4cam1_1 {\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a role implicitly inherits an abstract method that is called from another method
    // 0.c.4-otjld-copy-abstract-method-2
    public void test0c4_copyAbstractMethod2() {
       
       runConformTest(
            new String[] {
		"Team0c4cam2_3.java",
			    "\n" +
			    "public team class Team0c4cam2_3 extends Team0c4cam2_2 {\n" +
			    "	public class Role {\n" +
			    "		String getVal() { return \"OK\"; }\n" +
			    "	}\n" +
			    "	public Team0c4cam2_3() {\n" +
			    "		System.out.print((new Role()).getValue());\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team0c4cam2_3();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c4cam2_1.java",
			    "\n" +
			    "public team class Team0c4cam2_1 {\n" +
			    "	public abstract class Role {\n" +
			    "		abstract String getVal();\n" +
			    "		public String getValue() { return getVal(); }\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c4cam2_2.java",
			    "\n" +
			    "public team class Team0c4cam2_2 extends Team0c4cam2_1 {\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // Bug 355311 - error regarding abstract method in non-abstract role may be displayed at position 0
    public void test0c4_copyAbstractMethod3() {
       
       runNegativeTest(
            new String[] {
		"Team0c4cam3_1.java",
			    "\n" +
			    "public team class Team0c4cam3_1 {\n" +
			    "	public abstract class Role {\n" +
			    "		abstract String getVal();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c4cam3_2.java",
			    "\n" +
			    "public team class Team0c4cam3_2 extends Team0c4cam3_1 {\n" +
			    "   @Override\n" +
			    "	public class Role {\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "----------\n" + 
    		"1. ERROR in Team0c4cam3_2.java (at line 4)\n" + 
    		"	public class Role {\n" + 
    		"	             ^^^^\n" + 
    		"The abstract method getVal in type Role can only be defined by an abstract class\n" + 
    		"----------\n" +
    		"2. ERROR in Team0c4cam3_2.java (at line 4)\n" + 
    		"	public class Role {\n" + 
    		"	             ^^^^\n" + 
    		"The type Team0c4cam3_2.Role must implement the inherited abstract method Team0c4cam3_2.Role.getVal()\n" + 
    		"----------\n");
    }

    // Bug 359894 - [compiler] support @Override for static role methods
    public void test0c4_copyAbstractMethod4() {
       
       runConformTest(
            new String[] {
		"Team0c4cam4_1.java",
			    "\n" +
			    "public team class Team0c4cam4_1 {\n" +
			    "	public abstract class Role {\n" +
			    "		abstract static String getVal();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c4cam4_2.java",
			    "\n" +
			    "public team class Team0c4cam4_2 extends Team0c4cam4_1 {\n" +
			    "   @Override\n" +
			    "	public class Role {\n" +
			    "       @Override\n" +
			    "       static String getVal() { return null; }\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "");
    }

    // a role implicitely inherits a constructor  - changed between compiles - buggy tsuper-tsuper
    // 0.c.5-otjld-changes-between-compiles-1
    public void test0c5_changesBetweenCompiles1() {
        runNegativeTest(
            new String[] {
		"T0c5cbc1.java",
			    "\n" +
			    "public class  T0c5cbc1 {}	\n" +
			    "	\n",
		"Team0c5cbc1_3.java",
			    "\n" +
			    "public team class Team0c5cbc1_3 extends Team0c5cbc1_2 {\n" +
			    "	public class R {\n" +
			    "		R(String s) {\n" +
			    "			tsuper(s);\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public Team0c5cbc1_3() {\n" +
			    "		(new R(\"OK\")).test();\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team0c5cbc1_3();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"Team0c5cbc1_1.java",
			    "\n" +
			    "public team class Team0c5cbc1_1 {\n" +
			    "	public class R playedBy T0c5cbc1 {\n" +
			    "		String t;\n" +
			    "		R(String s) {\n" +
			    "			// base(); missing\n" +
			    "			this.t = s;\n" +
			    "		}\n" +
			    "		public void test() {\n" +
			    "			System.out.print(t);\n" +
			    "		}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"Team0c5cbc1_2.java",
			    "\n" +
			    "public team class Team0c5cbc1_2 extends Team0c5cbc1_1 {\n" +
			    "	public class R {\n" +
			    "		R(String s) {\n" +
			    "			tsuper(s);\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            null);
    }

    // a role implicitely inherits a constructor  - changed between compiles - corrected program - change not relevant here
    // 0.c.5-otjld-changes-between-compiles-2
    public void test0c5_changesBetweenCompiles2() {
       
       runConformTest(
            new String[] {
		"Team0c5cbc2_3.java",
			    "\n" +
			    "public team class Team0c5cbc2_3 extends Team0c5cbc2_2 {\n" +
			    "	public class R {\n" +
			    "		protected R(String s3) {\n" +
			    "			tsuper(s3);\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public Team0c5cbc2_3() {\n" +
			    "		(new R(\"OK\")).test();\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team0c5cbc2_3();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"T0c5cbc2.java",
			    "\n" +
			    "public class  T0c5cbc2 {}	\n" +
			    "	\n",
		"Team0c5cbc2_1.java",
			    "\n" +
			    "public team class Team0c5cbc2_1 {\n" +
			    "	public class R playedBy T0c5cbc2 {\n" +
			    "		String t;\n" +
			    "		R(String s1) {\n" +
			    "			base();\n" +
			    "			this.t = s1;\n" +
			    "		}\n" +
			    "		public void test() {\n" +
			    "			System.out.print(t);\n" +
			    "		}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n",
		"Team0c5cbc2_2.java",
			    "\n" +
			    "public team class Team0c5cbc2_2 extends Team0c5cbc2_1 {\n" +
			    "	public class R {\n" +
			    "		R(String s2) {\n" +
			    "			tsuper(s2);\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // a role refines the extends clause, invoking tsuper of its bound tsuper role
    // 0.c.6-otjld-ctor-with-changed-extends-1
    public void test0c6_ctorWithChangedExtends1() {
        runNegativeTestMatching(
            new String[] {
		"Team0c6cwce1_2.java",
			    "\n" +
			    "public team class Team0c6cwce1_2 extends Team0c6cwce1_1 {\n" +
			    "	public class R extends Super0c6cwce1 {\n" +
			    "		protected R(String s) {\n" +
			    "			tsuper(s);\n" +
			    "		}\n" +
			    "		protected void test() {\n" +
			    "			System.out.print(s);\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public Team0c6cwce1_2() {\n" +
			    "		(new R(\"OK\")).test();\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team0c6cwce1_2();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T0c6cwce1.java",
			    "\n" +
			    "public class  T0c6cwce1 {}	\n" +
			    "	\n",
		"Super0c6cwce1.java",
			    "\n" +
			    "public class  Super0c6cwce1 {}	\n" +
			    "	\n",
		"Team0c6cwce1_1.java",
			    "\n" +
			    "public team class Team0c6cwce1_1 {\n" +
			    "	public class R playedBy T0c6cwce1 {\n" +
			    "		String s;\n" +
			    "		R(String s) {\n" +
			    "			base();\n" +
			    "			this.s = s;\n" +
			    "		}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "1.3.2(c)");
    }

    // a role refines the extends clause, invoking super and base and a role method
    // 0.c.6-otjld-ctor-with-changed-extends-2
    public void test0c6_ctorWithChangedExtends2() {
       
       runConformTest(
            new String[] {
		"Team0c6cwce2_2.java",
			    "\n" +
			    "public team class Team0c6cwce2_2 extends Team0c6cwce2_1 {\n" +
			    "	public class R extends Super0c6cwce2 {\n" +
			    "		protected R(String s) {\n" +
			    "			super();\n" +
			    "			base();\n" +
			    "			setS(s);\n" +
			    "		}\n" +
			    "		protected void test() {\n" +
			    "			System.out.print(s);\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public Team0c6cwce2_2() {\n" +
			    "		(new R(\"OK\")).test();\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team0c6cwce2_2();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T0c6cwce2.java",
			    "\n" +
			    "public class  T0c6cwce2 {}	\n" +
			    "	\n",
		"Super0c6cwce2.java",
			    "\n" +
			    "public class  Super0c6cwce2 {}	\n" +
			    "	\n",
		"Team0c6cwce2_1.java",
			    "\n" +
			    "public team class Team0c6cwce2_1 {\n" +
			    "	public class R playedBy T0c6cwce2 {\n" +
			    "		String s;\n" +
			    "		protected R(String s) {\n" +
			    "			base();\n" +
			    "			setS(s);\n" +
			    "		}\n" +
			    "		void setS(String s) {\n" +
			    "			this.s = s;\n" +
			    "		}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "OK");
    }

    // a role refines the extends clause, invoking super and base and a final role method
    // 0.c.6-otjld-ctor-with-changed-extends-3
    public void test0c6_ctorWithChangedExtends3() {
       
       runConformTest(
            new String[] {
		"Team0c6cwce3_2.java",
			    "\n" +
			    "public team class Team0c6cwce3_2 extends Team0c6cwce3_1 {\n" +
			    "	public class R extends Super0c6cwce3 {\n" +
			    "		protected R(String s) {\n" +
			    "			super();\n" +
			    "			base();\n" +
			    "			setS(s);\n" +
			    "		}\n" +
			    "		public void test() {\n" +
			    "			System.out.print(s);\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public Team0c6cwce3_2() {\n" +
			    "		(new R(\"OK\")).test();\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team0c6cwce3_2();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T0c6cwce3.java",
			    "\n" +
			    "public class  T0c6cwce3 {}	\n" +
			    "	\n",
		"Super0c6cwce3.java",
			    "\n" +
			    "public class  Super0c6cwce3 {}	\n" +
			    "	\n",
		"Team0c6cwce3_1.java",
			    "\n" +
			    "public team class Team0c6cwce3_1 {\n" +
			    "	public class R playedBy T0c6cwce3 {\n" +
			    "		String s;\n" +
			    "		protected R(String s) {\n" +
			    "			base();\n" +
			    "			setS(s);\n" +
			    "		}\n" +
			    "		final void setS(String s) {\n" +
			    "			this.s = s;\n" +
			    "		}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "OK");
    }
    public void test0c6_ctorWithChangedExtends4() {
        runConformTest(
        	new String[] {
         "Team0c6cwce4_2.java",
        		"public team class Team0c6cwce4_2 extends Team0c6cwce4_1 {\n" + // should inherit class R1 extends R0
         		"	@Override\n" +
    			"	protected class R2 extends R1 {\n" + 
    			"		public R2() {\n" + 
    			"			super();\n" + 
    			"		}\n" + 
    			"	}\n" + 
    			"	void test() {\n" + 
    			"		R2 r = new R2();\n" + 
    			"		System.out.print(r.val);\n" + 
    			"	}\n" + 
    			"	public static void main(String[] args) {\n" + 
    			"		new Team0c6cwce4_2().test();\n" + 
    			"	}\n" + 
    			"}\n",
    	 "Team0c6cwce4_1.java",
		    	"public team class Team0c6cwce4_1 {\n" + 
		 		"	protected class R0 {\n" +
		 		"		public String val;\n" + 
		 		"\n" + 
		 		"		public R0(String val) {\n" + 
		 		"			this.val = val;\n" + 
		 		"		}		\n" + 
		 		"	}\n" + 
		 		"	protected class R1 extends R0 {\n" + 
		 		"		public R1() {\n" + 
		 		"			super(\"T1.R1\");\n" + 
		 		"		}\n" + 
		 		"	}\n" + 
		 		"	protected class R2 extends R0 {\n" + 
		 		"		public R2(String val) {\n" + 
		 		"			super(val);\n" + 
		 		"		}		\n" + 
		 		"	}\n" + 
		 		"}\n"
		 		},
		 		"T1.R1");
    }
    public void test0c6_ctorWithChangedExtends5() {
        runConformTest(
             new String[] {
         "Team0c6cwce5_2.java",
         		"public team class Team0c6cwce5_2 extends Team0c6cwce5_1 {\n" +
         		"	@Override\n" +
         		"   protected class R1 extends R0 {} \n" + // should inherited ctor public R1() 
         		"	@Override\n" +
     			"	protected class R2 extends R1 {\n" + 
     			"		public R2() {\n" + 
     			"			super();\n" + 
     			"		}\n" + 
     			"	}\n" + 
     			"	void test() {\n" + 
     			"		R2 r = new R2();\n" + 
     			"		System.out.print(r.val);\n" + 
     			"	}\n" + 
     			"	public static void main(String[] args) {\n" + 
     			"		new Team0c6cwce5_2().test();\n" + 
     			"	}\n" + 
     			"}\n",
     	 "Team0c6cwce5_1.java",
 		    	"public team class Team0c6cwce5_1 {\n" + 
 		 		"	protected class R0 {\n" +
 		 		"		public String val;\n" + 
 		 		"\n" + 
 		 		"		public R0(String val) {\n" + 
 		 		"			this.val = val;\n" + 
 		 		"		}		\n" + 
 		 		"	}\n" + 
 		 		"	protected class R1 extends R0 {\n" + 
 		 		"		public R1() {\n" + 
 		 		"			super(\"T1.R1\");\n" + 
 		 		"		}\n" + 
 		 		"	}\n" + 
 		 		"	protected class R2 extends R0 {\n" + 
 		 		"		public R2(String val) {\n" + 
 		 		"			super(val);\n" + 
 		 		"		}		\n" + 
 		 		"	}\n" + 
 		 		"}\n"
 		 		});
    }
    public void test0c6_ctorWithChangedExtends6() {
        runNegativeTest(
             new String[] {
         "Team0c6cwce6_2.java",
          		"public team class Team0c6cwce6_2 extends Team0c6cwce6_1 {\n" +
          		"	@Override\n" +
          		"   protected class R1 extends R0 {\n" +
          		"   } \n" +  
          		"	@Override\n" +
      			"	protected class R2 extends R1 {\n" + 
      			"		public R2() {\n" + 
      			"			super();\n" +
      			"			tsuper(\"T2.R2\");\n" + 
      			"		}\n" + 
      			"	}\n" + 
      			"	void test() {\n" + 
      			"		R2 r = new R2();\n" + 
      			"		System.out.print(r.val);\n" + 
      			"	}\n" + 
      			"	public static void main(String[] args) {\n" + 
      			"		new Team0c6cwce6_2().test();\n" + 
      			"	}\n" + 
      			"}\n",
      	 "Team0c6cwce6_1.java",
  		    	"public team class Team0c6cwce6_1 {\n" + 
  		 		"	protected class R0 {\n" +
  		 		"		public String val;\n" + 
  		 		"\n" + 
  		 		"		public R0(String val) {\n" + 
  		 		"			this.val = val;\n" + 
  		 		"		}		\n" + 
  		 		"	}\n" + 
  		 		"	protected class R1 extends R0 {\n" + 
  		 		"		public R1() {\n" + 
  		 		"			super(\"T1.R1\");\n" + 
  		 		"		}\n" + 
  		 		"	}\n" + 
  		 		"	protected class R2 extends R0 {\n" + 
  		 		"		public R2(String val) {\n" + 
  		 		"			super(val);\n" + 
  		 		"		}		\n" + 
  		 		"	}\n" + 
  		 		"}\n"
  		 		},
  		 		"----------\n" +
  		 		"1. ERROR in Team0c6cwce6_2.java (at line 9)\n" +
  		 		"	tsuper(\"T2.R2\");\n" +
  		 		"	^^^^^^^^^^^^^^^^\n" +
  		 		"Constructor call (tsuper) must be the first statement in a role constructor (OTJLD 2.4.2).\n" +
  		 		"----------\n");
    }

    // tsuper ctor call in non-role (is actually a method call now ;-)
    // 0.c.7-otjld-illegal-tsuper-ctor-call-1
    public void test0c7_illegalTsuperCtorCall1() {
        runConformTest(
            new String[] {
		"T0c7itcc1.java",
			    "\n" +
			    "public class T0c7itcc1 {\n" +
			    "	T0c7itcc1 () {\n" +
			    "		tsuper();\n" +
			    "	}\n" +
			    "	void tsuper() {}\n" +
			    "}\n" +
			    "	\n"
            });
    }

    // tsuper ctor call in role without tsuper role
    // 0.c.7-otjld-illegal-tsuper-ctor-call-2
    public void test0c7_illegalTsuperCtorCall2() {
        runNegativeTestMatching(
            new String[] {
		"Team0c7itcc2.java",
			    "\n" +
			    "public team class Team0c7itcc2 {\n" +
			    "	protected class R {\n" +
			    "		R() {\n" +
			    "			tsuper();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "cc");
    }

    // tsuper ctor call in role with wrong signature
    // 0.c.7-otjld-illegal-tsuper-ctor-call-3
    public void test0c7_illegalTsuperCtorCall3() {
        runNegativeTest(
            new String[] {
		"Team0c7itcc3_2.java",
			    "\n" +
			    "public team class Team0c7itcc3_2 extends Team0c7itcc3_1 {\n" +
			    "	@Override\n" +
			    "	protected class R {\n" +
			    "		public R() {\n" +
			    "			tsuper(7);\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c7itcc3_1.java",
			    "\n" +
			    "public team class Team0c7itcc3_1 {\n" +
			    "	protected class R { /* only default ctor */ }\n" +
			    "}	\n" +
			    "	\n"
            },
            "----------\n" + 
    		"1. ERROR in Team0c7itcc3_2.java (at line 6)\n" + 
    		"	tsuper(7);\n" + 
    		"	^^^^^^^^^^\n" + 
    		"The constructor Team0c7itcc3_1.R(int) is undefined\n" + 
    		"----------\n");
    }

    // a role tries to override a final method from its tsuper role
    // 0.c.8-otjld-overriding-final-method-1
    public void test0c8_overridingFinalMethod1() {
        runNegativeTestMatching(
            new String[] {
		"Team0c8ofm1_2.java",
			    "\n" +
			    "public team class Team0c8ofm1_2 extends Team0c8ofm1_1 {\n" +
			    "	protected class R {\n" +
			    "		void foo() { System.out.print(\"NOK\"); }\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c8ofm1_1.java",
			    "\n" +
			    "public team class Team0c8ofm1_1 {\n" +
			    "	protected class R {\n" +
			    "		final void foo() { System.out.print(\"NOP\"); }\n" +
			    "	}	\n" +
			    "}\n" +
			    "	\n"
            },
            "final");
    }

    // a role tries to override a final method from its super role
    // 0.c.8-otjld-overriding-final-method-2
    public void test0c8_overridingFinalMethod2() {
        runNegativeTestMatching(
            new String[] {
		"Team0c8ofm2_2.java",
			    "\n" +
			    "public team class Team0c8ofm2_2 extends Team0c8ofm2_1 {\n" +
			    "	protected class R2 extends R1 {\n" +
			    "		void foo() { System.out.print(\"NOK\"); }\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0c8ofm2_1.java",
			    "\n" +
			    "public team class Team0c8ofm2_1 {\n" +
			    "	protected class R1 {\n" +
			    "		final void foo() { System.out.print(\"NOP\"); }\n" +
			    "	}	\n" +
			    "}\n" +
			    "	\n"
            },
            "final");
    }

    // a private method is callin bound, subteam inherits everything. Bug reported by OTS-students working on OTPong
    // 0.c.9-otjld-implicitly-inheriting-callin-to-private-1
    public void test0c9_implicitlyInheritingCallinToPrivate1() {
       
       runConformTest(
            new String[] {
		"Team0c9iictp1_2.java",
			    "\n" +
			    "public team class Team0c9iictp1_2 extends Team0c9iictp1_1 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0c9iictp1_2().activate();\n" +
			    "        new T0c9iictp1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T0c9iictp1.java",
			    "\n" +
			    "public class T0c9iictp1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0c9iictp1_1.java",
			    "\n" +
			    "public team class Team0c9iictp1_1 {\n" +
			    "    protected class R playedBy T0c9iictp1 {\n" +
			    "        private void test() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        t: test <- after test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role tries to override a final tsuper role
    // 0.c.10-otjld-override-final-role-1
    public void test0c10_overrideFinalRole1() {
        runNegativeTestMatching(
            new String[] {
		"Team0c10ofr1_2.java",
			    "\n" +
			    "public team class Team0c10ofr1_2 extends Team0c10ofr1_1 {\n" +
			    "    protected class R {}\n" +
			    "}\n" +
			    "    \n",
		"Team0c10ofr1_1.java",
			    "\n" +
			    "public team class Team0c10ofr1_1 {\n" +
			    "    final protected class R {}\n" +
			    "}\n" +
			    "    \n"
            },
            "final");
    }

    // 
    // 0.c.11-otjld-implicitly-inheriting-static-role-method-1
    public void test0c11_implicitlyInheritingStaticRoleMethod1() {
       
       runConformTest(
            new String[] {
		"Team0c11iisrm1_2.java",
			    "\n" +
			    "public team class Team0c11iisrm1_2 extends Team0c11iisrm1_1 {\n" +
			    "    protected class R {\n" +
			    "        String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team0c11iisrm1_2() {\n" +
			    "        System.out.print(R.getVal(new R(),\"!\"));\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0c11iisrm1_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0c11iisrm1_1.java",
			    "\n" +
			    "public team class Team0c11iisrm1_1 {\n" +
			    "    abstract protected class R {\n" +
			    "        protected static String getVal(R r, String postfix) {\n" +
			    "            R r2 = r;\n" +
			    "            String result = r2.getValue()+postfix;\n" +
			    "            return result;\n" +
			    "        }\n" +
			    "        abstract String getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK!");
    }

    // a tsuper call initially does not require a marker arg, but byte code copy has to add one
    // 0.c.12-otjld-tsuper-call-rewriting-1
    public void test0c12_tsuperCallRewriting1() {
       
       runConformTest(
            new String[] {
		"Team0c12tcr1_3.java",
			    "\n" +
			    "public team class Team0c12tcr1_3 extends Team0c12tcr1_2 {\n" +
			    "    protected class R {\n" +
			    "        protected R() { // overrides inherited no-arg ctor\n" +
			    "            tsuper(23);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        new R();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0c12tcr1_3().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0c12tcr1_1.java",
			    "\n" +
			    "public team class Team0c12tcr1_1 {\n" +
			    "    protected class R {\n" +
			    "        R() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0c12tcr1_2.java",
			    "\n" +
			    "public team class Team0c12tcr1_2 extends Team0c12tcr1_1 {\n" +
			    "    protected class R {\n" +
			    "        R(int i) {\n" +
			    "            tsuper(); // no marker arg needed\n" +
			    "            System.out.print(i);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK23");
    }

    // a class implicitly inherits a super interface declaration - src-level role ifc
    // 0.c.13-otjld-implicitly-inherited-superinterface-1
    public void test0c13_implicitlyInheritedSuperinterface1() {
       
       runConformTest(
            new String[] {
		"Team0c13iis1_2.java",
			    "\n" +
			    "public team class Team0c13iis1_2 extends Team0c13iis1_1 {\n" +
			    "    protected class R1 extends R0 playedBy T0c13iis1 {\n" +
			    "        public void print() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test(T0c13iis1 as R1 o) {\n" +
			    "        test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0c13iis1_2().test(new T0c13iis1());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0c13iis1_1.java",
			    "\n" +
			    "public abstract team class Team0c13iis1_1 {\n" +
			    "    protected interface I { void print(); }\n" +
			    "    protected abstract class R0 implements I {}\n" +
			    "    void test() {\n" +
			    "        for (Object role : getAllRoles()) {\n" +
			    "            if (role instanceof I)\n" +
			    "                ((I)role).print();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T0c13iis1.java",
			    "\n" +
			    "public class T0c13iis1 {}\n" +
			    "    \n"
            },
            "OK");
    }
    
    // a role implicitly inherits a final field with an initializer.
    // see Bug 315322 -  [compiler] final field with initializer breaks implicit inheritance
    public void test0c14_implicitlyInheritedInitializedField1() {
    	runConformTest(
    		new String[] {
    	"Team0c14iiif1_2.java",
    			"public team class Team0c14iiif1_2 extends Team0c14iiif1_1 {\n" +
    			"	protected class R {\n" +
    			"		protected R() { tsuper(); }\n" + // triggers blank final analysis
    			"		protected void test() {\n" +
    			"			System.out.print(val);\n" +
    			"		}\n" +
    			"	}\n" +
    			"	public void test() {\n" +
    			"		new R().test();\n" +
    			"	}\n" +
    			"	public static void main(String... args) {\n" +
    			"		new Team0c14iiif1_2().test();\n" +
    			"	}\n" +
    			"}\n",
    	"Team0c14iiif1_1.java",
    			"public team class Team0c14iiif1_1 {\n" +
    			"	protected class R {\n" +
    			"		final String val= new String(\"OK\");\n" +
    			"	}\n" +
    			"}\n"
    		},
    		"OK");
    }

    // for the following tests confer also test2331_roleCreationInvalidatedBySpecialization1().

    // see Bug 318415 -  Copy inheritance problem
    // no harm since illegal ctor is not invoked
    public void _test0c15_overrideBoundSuperRole1() {
    	runConformTest(
    		new String[] {
    	"T0c15obsr1Main.java",
	    		"import org.objectteams.Team;\n" +
	    		"public class T0c15obsr1Main {\n" +
	    		"	public static void main(String[] args) {\n" +
	    		"		Team0c15obsr1_2 t = new Team0c15obsr1_2();\n" +
	    		"		t.activate(Team.ALL_THREADS);\n" +
	    		"		T0c15obsr1_1 b = new T0c15obsr1_2();\n" +
	    		"		b.m();\n" +
	    		"	}\n" +
	    		"}\n",
    	"T0c15obsr1_1.java",
	    		"public class T0c15obsr1_1 {\n" +
	    		"	public void m() { System.out.print(\"m\"); }\n" +
	    		"}\n",
    	"T0c15obsr1_2.java",
	    		"public class T0c15obsr1_2 extends T0c15obsr1_1 {\n" +
	    		"}\n",
    	"Team0c15obsr1_1.java",
	    		"public team class Team0c15obsr1_1 {\n" +
	    		"	protected class R {\n" +
	    		"		void foo() { System.out.print(\"foo\"); }\n" +
	    		"	}\n" +
	    		"	protected class RSub extends R playedBy T0c15obsr1_2 {\n" +
	    		"		foo <- after m;\n" +
	    		"	}\n" +
	    		"}\n",
    	"Team0c15obsr1_2.java",
	    		"public team class Team0c15obsr1_2 extends Team0c15obsr1_1 {\n" +
	    		"	protected class R playedBy T0c15obsr1_1 {}\n" +
	    		"}\n"    		
    		},
    		"mfoo");
    }

    // see Bug 318415 -  Copy inheritance problem
    public void _test0c15_overrideBoundSuperRole4() {
    	runConformTest(
    		new String[] {
    	"T0c15obsr4Main.java",
	    		"import org.objectteams.Team;\n" +
	    		"public class T0c15obsr4Main {\n" +
	    		"	public static void main(String[] args) {\n" +
	    		"		Team0c15obsr4_2 t = new Team0c15obsr4_2();\n" +
	    		"		t.activate(Team.ALL_THREADS);\n" +
	    		"		T0c15obsr4_1 b = new T0c15obsr4_2();\n" +
	    		"		b.m();\n" +
	    		"	}\n" +
	    		"}\n",
    	"T0c15obsr4_1.java",
	    		"public class T0c15obsr4_1 {\n" +
	    		"	public void m() { System.out.print(\"m\"); }\n" +
	    		"}\n",
    	"T0c15obsr4_2.java",
	    		"public class T0c15obsr4_2 extends T0c15obsr4_1 {\n" +
	    		"}\n",
    	"Team0c15obsr4_1.java",
	    		"public team class Team0c15obsr4_1 {\n" +
	    		"	protected class R {\n" +
	    		"		void foo() { System.out.print(\"foo\"); }\n" +
	    		"	}\n" +
	    		"	protected class RSub extends R playedBy T0c15obsr4_2 {\n" +
	    		"		foo <- after m;\n" +
	    		"	}\n" +
	    		"}\n",
    	"Team0c15obsr4_2.java",
	    		"public team class Team0c15obsr4_2 extends Team0c15obsr4_1 {\n" +
	    		"	protected class R playedBy T0c15obsr4_1 {}\n" +
	    		"	protected class RSub {\n" +
	    		"		protected RSub(T0c15obsr4_2 o) {\n" +
	    		"			tsuper(o);\n" +
	    		"		}\n" +
	    		"	}\n" +
	    		"}\n"    		
    		},
    		"mfoo");
    }

    // see Bug 318415 -  Copy inheritance problem
    // cannot detect dynamically bound use of illegal role ctor => runtime exception
    public void test0c15_overrideBoundSuperRole2() {
    	runConformTest(
    		new String[] {
    	"T0c15obsr2Main.java",
	    		"import org.objectteams.Team;\n" +
	    		"public class T0c15obsr2Main {\n" +
	    		"	public static void main(String[] args) {\n" +
	    		"		Team0c15obsr2_2 t = new Team0c15obsr2_2();\n" +
	    		"		try {\n" +
	    		"			t.test();\n" +
	    		"		} catch (org.objectteams.IllegalRoleCreationException irce) {\n" +
	    		"			System.out.print(\"caught\");\n" +
	    		"		}\n" +
	    		"	}\n" +
	    		"}\n",
    	"T0c15obsr2_1.java",
	    		"public class T0c15obsr2_1 {}\n",
    	"Team0c15obsr2_1.java",
	    		"public team class Team0c15obsr2_1 {\n" +
	    		"	protected class R {}\n" +
	    		"	protected void test() {\n" +
	    		"		new R();\n" +
	    		"	}\n" +
	    		"}\n",
    	"Team0c15obsr2_2.java",
	    		"public team class Team0c15obsr2_2 extends Team0c15obsr2_1 {\n" +
	    		"	protected class R playedBy T0c15obsr2_1 {}\n" +
	    		"}\n"    		
    		},
    		"caught");
    }

    // detect use of inherited illegal role ctor 
    // see Bug 318415 -  Copy inheritance problem
    public void test0c15_overrideBoundSuperRole3() {
    	runConformTest(
    		new String[] {
    	"T0c15obsr3Main.java",
	    		"import org.objectteams.Team;\n" +
	    		"public class T0c15obsr3Main {\n" +
	    		"	public static void main(String[] args) {\n" +
	    		"		Team0c15obsr3_2 t = new Team0c15obsr3_2();\n" +
	    		"		try {\n" +
	    		"			t.test();\n" +
	    		"		} catch (org.objectteams.IllegalRoleCreationException irce) {\n" +
	    		"			System.out.print(\"caught\");\n" +
	    		"		}\n" +
	    		"	}\n" +
	    		"}\n",
    	"T0c15obsr3_1.java",
	    		"public class T0c15obsr3_1 {}\n",
    	"Team0c15obsr3_1.java",
	    		"public team class Team0c15obsr3_1 {\n" +
	    		"	protected class R0 {\n" +
	    		"		protected R0() {\n" +
	    		"			new R();" +
	    		"		}\n" +
	    		"	}\n" +
	    		"	public class R {}\n" +
	    		"	protected void test() {\n" +
	    		"		new R0();\n" +
	    		"	}\n" +
	    		"}\n",
    	"Team0c15obsr3_2.java",
	    		"public team class Team0c15obsr3_2 extends Team0c15obsr3_1 {\n" +
	    		"	public class R playedBy T0c15obsr3_1 {}\n" +
	    		"}\n"    		
    		},
    		"caught");
    }
    // witness for a regression : Method has no byte code: private java.lang.String test(java.lang.String)
    // 						      thrown from RoleModel.getByteCodeOffset(MethodBinding)
    public void test0c16_implicitInheritanceRegression1() {
    	runNegativeTest(
    		new String[] {
    	"Team0c16iir1_2.java",
				"public team class Team0c16iir1_2 extends Team0c16iir1_1<String> {\n" +
				"   @Override\n" +
				"	public class R playedBy T0c16iir1 {\n" +
				"		 test <- before test;\n" +
				"   }\n" +
				"}\n",
    	"Team0c16iir1_1.java",
    			"public team class Team0c16iir1_1<U> {\n" +
    			"	public class R {\n" +
				"        private String test(String u) {\n" +
				"            return \"O\"+base.test(u);\n" +
				"        }\n" +
    			"   }\n" +
    			"}\n",
    	"T0c16iir1.java",
    			"public class T0c16iir1 {\n" +
    			"   protected String test(String u){ return u;}" +
    			"}\n"
    		},
    		"----------\n" + 
    		"1. ERROR in Team0c16iir1_1.java (at line 4)\n" + 
			"	return \"O\"+base.test(u);\n" + 
			"	           ^^^^^^^^^^^^\n" + 
			"Cannot use \'base\' in the regular method \'test(String)\' (OTJLD 2.6(c)).\n" + 
			"----------\n");
    }

    // Bug 400362 - [compiler] role field with anchored role type breaks implicit inheritance
    public void test0c16_implicitInheritanceRegression2() {
    	runConformTest(
    		new String[]{
    	"p2b/SubTeam.java",
    		"package p2b;\n" +
    		"import p2a.SuperTeam;\n" +
    		"public team class SubTeam extends SuperTeam {\n" +
    		"	public abstract class R {}\n" +
    		"}\n",
    	"p2a/SuperTeam.java",
    		"package p2a;\n" +
    		"public team class SuperTeam {\n" +
    		"	final OtherTeam other=new OtherTeam();\n" +
    		"	public abstract class R {\n" +
    		"		OR<@other> otherRole;\n" +
    		"	}\n" +
    		"}\n",
    	"p2a/OtherTeam.java",
    		"package p2a;\n" +
    		"public team class OtherTeam {\n" +
    		"	public abstract class OR {}\n" +
    		"}\n"
    		},
    		"");
    }
    // Bug 433109 - [compiler] Cannot generate method $SWITCH_TABLE$ ...
    public void test0c16_implicitInheritanceRegression3() {
    	runConformTest(
    		new String[]{
    	"p3b/SubTeam.java",
    		"package p3b;\n" +
    		"import java.lang.annotation.*;\n" +
    		"import p3a.SuperTeam;\n" +
    		"public team class SubTeam extends SuperTeam {\n" +
    		"	public void test() {\n" +
    		"		new R().test(ElementType.METHOD);\n" +
    		"	}\n" +
    		"	public static void main(String... args) {\n" +
    		"		new SubTeam().test();\n" +
    		"	}\n" +
    		"}\n",
    	"p3a/SuperTeam.java",
    		"package p3a;\n" +
    		"import java.lang.annotation.*;\n" +
    		"public team class SuperTeam {\n" +
    		"	public class R {\n" +
    		"		protected void test(ElementType e) {\n" +
    		"			switch (e) {\n" +
    		"				case FIELD: System.out.println(\"F\"); break;\n" +
    		"				case METHOD: System.out.println(\"M\"); break;\n" +
    		"				default: System.out.println(\"?\"); break;\n" +
    		"			}\n" +
    		"		}\n" +
    		"	}\n" +
    		"}\n"
    		},
    		"M");
    }
}
