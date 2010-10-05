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
package org.eclipse.objectteams.otdt.tests.otjld.other;

import java.util.Map;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

@SuppressWarnings("unchecked")
public class AccessModifiers extends AbstractOTJLDTest {
	
	public AccessModifiers(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test0a9_staticFinalRoleField4"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return AccessModifiers.class;
	}
    // private role method is accessed outside role
    // 0.a.1.a-otjld-private-role-method-1
    public void test0a1a_privateRoleMethod1() {
        runNegativeTestMatching(
            new String[] {
		"Team0a1a1.java",
			    "\n" +
			    "public team class Team0a1a1 {\n" +
			    "	protected class R1 {\n" +
			    "		private void secret () {}\n" +
			    "	}\n" +
			    "	protected class R2 {\n" +
			    "		void foo (R1 r) {\n" +
			    "			r.secret();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "1.2.1(e)");
    }

    // private role method has an unresolved parameter
    // 0.a.1.a-otjld-private-role-method-2
    public void test0a1a_privateRoleMethod2() {
        runNegativeTest(
            new String[] {
		"Team0a1a2.java",
			    "\n" +
			    "public team class Team0a1a2 {\n" +
			    "	protected class R1 {\n" +
			    "		private void secret (Missing a) {}\n" +
			    "		void foo() {\n" +
			    "		  this.secret(null);\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "----------\n" + 
    		"1. ERROR in Team0a1a2.java (at line 4)\n" + 
    		"	private void secret (Missing a) {}\n" + 
    		"	                     ^^^^^^^\n" + 
    		"Missing cannot be resolved to a type\n" + 
    		"----------\n" + 
    		"2. ERROR in Team0a1a2.java (at line 6)\n" + 
    		"	this.secret(null);\n" + 
    		"	     ^^^^^^\n" + 
    		"The method secret(Missing) from the type Team0a1a2.R1 refers to the missing type Missing\n" + 
    		"----------\n");
    }

    // private role method is unused - yet don't report this
    // 0.a.1.a-otjld-private-role-method-3
    public void test0a1a_privateRoleMethod3() {
        runConformTest(
            new String[] {
		"Team0a1a3.java",
			    "\n" +
			    "public team class Team0a1a3 {\n" +
			    "	protected class R1 {\n" +
			    "		private void secret (int a) {}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            });
    }

    // private role method is unused in super team, used (call) in subteam
    // 0.a.1.a-otjld-private-role-method-4
    public void test0a1a_privateRoleMethod4() {
        runConformTest(
            new String[] {
		"Team0a1a4_1.java",
			    "\n" +
			    "public team class Team0a1a4_1 {\n" +
			    "	protected class R1 {\n" +
			    "		private void secret (int a) {}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0a1a4_2.java",
			    "\n" +
			    "public team class Team0a1a4_2 extends Team0a1a4_1 {\n" +
			    "	protected class R1 {\n" +
			    "		void doit() {\n" +
			    "			secret(3);\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            });
    }

    // private role method is unused in super team and also in subteam - don't report against copy-inherited version
    // 0.a.1.a-otjld-private-role-method-5
    public void test0a1a_privateRoleMethod5() {
        runConformTest(
            new String[] {
		"Team0a1a5_1.java",
			    "\n" +
			    "public team class Team0a1a5_1 {\n" +
			    "	protected class R1 {\n" +
			    "		private void secret (int a) {}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0a1a5_2.java",
			    "\n" +
			    "public team class Team0a1a5_2 extends Team0a1a5_1 {\n" +
			    "	protected class R1 {\n" +
			    "		void doit() {}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            });
    }

    // private role method is unused in super team, used (callin) in subteam
    // 0.a.1.a-otjld-private-role-method-6
    public void test0a1a_privateRoleMethod6() {
        runConformTest(
            new String[] {
		"Team0a1a6_1.java",
			    "\n" +
			    "public team class Team0a1a6_1 {\n" +
			    "	protected class R1 {\n" +
			    "		private void secret (int a) {}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"T0a1a6.java",
			    "\n" +
			    "public class T0a1a6 {\n" +
			    "	public void api(int j) {};\n" +
			    "}\n" +
			    "	\n",
		"Team0a1a6_2.java",
			    "\n" +
			    "public team class Team0a1a6_2 extends Team0a1a6_1 {\n" +
			    "	protected class R1 playedBy T0a1a6 {\n" +
			    "		secret <- after api;\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            });
    }

    // private role method is callin bound, binding inheritance
    // 0.a.1.a-otjld-private-role-method-7
    public void test0a1a_privateRoleMethod7() {
       
       runConformTest(
            new String[] {
		"Team0a1aprm7_2.java",
			    "\n" +
			    "public team class Team0a1aprm7_2 extends Team0a1aprm7_1 {\n" +
			    "    protected class R {\n" +
			    "        void print() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team0a1aprm7_2 t = new Team0a1aprm7_2();\n" +
			    "        t.activate();\n" +
			    "        new T0a1aprm7().bm();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T0a1aprm7.java",
			    "\n" +
			    "public class T0a1aprm7 {\n" +
			    "    void bm() { System.out.print(\"O\"); }\n" +
			    "}\n" +
			    "    \n",
		"Team0a1aprm7_1.java",
			    "\n" +
			    "public team class Team0a1aprm7_1 {\n" +
			    "    protected class R playedBy T0a1aprm7 {\n" +
			    "        private void rm(R other) {\n" +
			    "            print();\n" +
			    "        }\n" +
			    "        void rm(R other) <- after void bm() with { other <- null };\n" +
			    "        void print() { System.out.print(\"X\"); }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a private static role method is defined in a role file
    // 0.a.1.a-otjld-private-role-method-8
    public void test0a1a_privateRoleMethod8() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportInferredCallout, CompilerOptions.WARNING);
       
       runConformTest(
            new String[] {
		"Team0a1aprm8_2.java",
			    "\n" +
			    "public team class Team0a1aprm8_2 extends Team0a1aprm8_1 {\n" +
			    "    void test() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a1aprm8_2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0a1aprm8_1.java",
			    "\n" +
			    "public team class Team0a1aprm8_1 {\n" +
			    "    R r;\n" +
			    "}\n" +
			    "    \n",
		"Team0a1aprm8_1/R.java",
			    "\n" +
			    "team package Team0a1aprm8_1;\n" +
			    "protected class R {\n" +
			    "    private static int getI() {\n" +
			    "        return 3;\n" +
			    "    }\n" +
			    "    protected void test() {\n" +
			    "        System.out.print(getI());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }

    // a private static role method is defined in a role file
    // 0.a.1.a-otjld-private-role-method-9
    public void test0a1a_privateRoleMethod9() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportInferredCallout, CompilerOptions.WARNING);
       
       runConformTest(
            new String[] {
		"Team0a1aprm9_2.java",
			    "\n" +
			    "public team class Team0a1aprm9_2 extends Team0a1aprm9_1 {\n" +
			    "    void test() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a1aprm9_2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0a1aprm9_1.java",
			    "\n" +
			    "/** fillcomment..\n" +
			    " * @role R */\n" +
			    "public team class Team0a1aprm9_1 {\n" +
			    "    R r;\n" +
			    "}\n" +
			    "    \n",
		"Team0a1aprm9_1/R.java",
			    "\n" +
			    "team package Team0a1aprm9_1;\n" +
			    "protected class R {\n" +
			    "    private static int getI() {\n" +
			    "        return 3;\n" +
			    "    }\n" +
			    "    protected void test() {\n" +
			    "        System.out.print(getI());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }
    
    // private method of nested team accesses protected inner role
    public void test0a1a_privateRoleMethod10() {
        runConformTest(
            new String[] {
        "Team0a1aprm10.java",
        		"public team class Team0a1aprm10 {\n" + 
        		"	protected team class Mid {\n" + 
        		"		protected class R {\n" +
        		"		    protected void m() {\n" + 
        		"			    System.out.println(\"OK\");\n" + 
        		"		    }\n" + 
        		"       }\n" + 
        		"		private void m(R r) {\n" + 
        		"			r.m();\n" + 
        		"		}\n" + 
        		"		protected void test() {\n" + 
        		"			m(new R());\n" + 
        		"		}\n" + 
        		"	}\n" + 
        		"	void test() {\n" + 
        		"		new Mid().test();\n" + 
        		"	}\n" + 
        		"	public static void main(String[] args) {\n" + 
        		"		new Team0a1aprm10().test();\n" + 
        		"	}\n" + 
        		"}\n"
            },
            "OK");
    }
    
    // a private role field has a type of a non-externalizable role (synth accessor despite role being protected - witness for a regression in miniCRM )
    // 0.a.1.A-otjld-private-role-field
    public void test0a1A_privateRoleField() {
       
       runConformTest(
            new String[] {
		"Team0a1A.java",
			    "\n" +
			    "public team class Team0a1A {\n" +
			    "    protected class R1 {\n" +
			    "        protected void test() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected class R2 {\n" +
			    "        private R1 r1 = null;\n" +
			    "        protected R2(R1 r) {\n" +
			    "            this.r1 = r;\n" +
			    "        }\n" +
			    "        protected void test() {\n" +
			    "            this.r1.test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team0a1A() {\n" +
			    "        R2 r2 = new R2(new R1());\n" +
			    "        r2.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a1A();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a private role field is accessed - multilevel team inheritance - witness for CCE in RoleTypeBinding
    // 0.a.1.A-otjld-private-role-field-2
    public void test0a1A_privateRoleField2() {
       
       runConformTest(
            new String[] {
		"Team0a1A2_3.java",
			    "\n" +
			    "public team class Team0a1A2_3 extends Team0a1A2_2 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a1A2_3().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0a1A2_1.java",
			    "\n" +
			    "public team class Team0a1A2_1 {\n" +
			    "    protected class Other {\n" +
			    "        protected void print() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected class R {\n" +
			    "        private Other o;\n" +
			    "        protected void setOther(Other o) {\n" +
			    "            this.o = o;\n" +
			    "        }\n" +
			    "        protected void print() {\n" +
			    "            this.o.print();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0a1A2_2.java",
			    "\n" +
			    "public team class Team0a1A2_2 extends Team0a1A2_1 {\n" +
			    "    void test() {\n" +
			    "        R r = new R();\n" +
			    "        r.setOther(new Other());\n" +
			    "        r.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a private role method needs a synth accessor, arg has role type of same name as its base (was incorrectly resolved to the base class
    // 0.a.1.A-otjld-private-role-method-1
    public void test0a1A_privateRoleMethod1() {
       
       runConformTest(
            new String[] {
		"T0a1Aprm1Main.java",
			    "\n" +
			    "import pbase.T0a1Aprm1;\n" +
			    "public class T0a1Aprm1Main {\n" +
			    "  public static void main(String[] args) {\n" +
			    "    Team0a1Aprm1 t = new Team0a1Aprm1();\n" +
			    "    t.test(new T0a1Aprm1());\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n",
		"pbase/T0a1Aprm1.java",
			    "\n" +
			    "package pbase;\n" +
			    "public class T0a1Aprm1 {\n" +
			    "}\n" +
			    "  \n",
		"Team0a1Aprm1.java",
			    "\n" +
			    "import base pbase.T0a1Aprm1;\n" +
			    "public team class Team0a1Aprm1 {\n" +
			    "  protected class T0a1Aprm1 playedBy T0a1Aprm1 {\n" +
			    "    protected void print() { System.out.print(\"OK\"); }\n" +
			    "  }\n" +
			    "  protected class R2 {\n" +
			    "    protected void callTest(T0a1Aprm1 other) {\n" +
			    "      test(other).print();\n" +
			    "    }\n" +
			    "    private T0a1Aprm1 test(T0a1Aprm1 other) {\n" +
			    "      return other;\n" +
			    "    }\n" +
			    "  }\n" +
			    "  void test(T0a1Aprm1 as T0a1Aprm1 t) {\n" +
			    "    R2 r2 = new R2();\n" +
			    "    r2.callTest(t);\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n"
            },
            "OK");
    }

    // default-acc role method is accessed outside role
    // 0.a.1.b-otjld-default-role-method
    public void test0a1b_defaultRoleMethod() {
        runNegativeTest(
            new String[] {
		"Team0a1b.java",
			    "\n" +
			    "public team class Team0a1b {\n" +
			    "	protected class R1 {\n" +
			    "		void secret () {}\n" +
			    "	}\n" +
			    "	protected class R2 {\n" +
			    "		void foo (R1 r) {\n" +
			    "			r.secret();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "----------\n" +
			"1. ERROR in Team0a1b.java (at line 8)\n" +
			"	r.secret();\n" +
			"	  ^^^^^^\n" +
			"The method secret() from the role type Team0a1b.R1 is not visible (OTJLD 1.2.1(e)).\n" +
			"----------\n");
    }

    // default-acc role ctor is accessed outside role
    // new in junit
    public void test0a1b_defaultRoleMethod2() {
        runNegativeTest(
            new String[] {
		"Team0a1bdrm2.java",
			    "\n" +
			    "public team class Team0a1bdrm2 {\n" +
			    "	protected class R1 {\n" +
			    "		R1 () {}\n" +
			    "	}\n" +
			    "	protected class R2 {\n" +
			    "		R1 foo () {\n" +
			    "			return new R1();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "----------\n" +
			"1. ERROR in Team0a1bdrm2.java (at line 8)\n" +
			"	return new R1();\n" +
			"	       ^^^^^^^^\n" +
			"The role constructor Team0a1bdrm2.R1() is not visible (OTJLD 1.2.1(e)).\n" +
			"----------\n");
    }

    // protected role method is accessed outside role
    // 0.a.1.c-otjld-protected-role-method
    public void test0a1c_protectedRoleMethod() {
        runConformTest(
            new String[] {
		"Team0a1c.java",
			    "\n" +
			    "public team class Team0a1c {\n" +
			    "	protected class R1 {\n" +
			    "		protected void secret () {}\n" +
			    "	}\n" +
			    "	protected class R2 {\n" +
			    "		void foo (R1 r) {\n" +
			    "			r.secret();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            });
    }

    // protected role method is accessed outside the package
    // 0.a.3-otjld-protected-role-method-1
    public void test0a3_protectedRoleMethod1() {
        runNegativeTestMatching(
            new String[] {
		"Main0a3prm1.java",
			    "\n" +
			    "import p1.T0a3prm1;\n" +
			    "public class Main0a3prm1 {\n" +
			    "	final T0a3prm1 t = new T0a3prm1 ();\n" +
			    "	void bar () {\n" +
			    "		R<@t> r = null;\n" +
			    "		r.secret();\n" +
			    "	}\n" +
			    "}	\n",
		"p1/T0a3prm1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class T0a3prm1 {\n" +
			    "	public class R {\n" +
			    "		protected void secret () {}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "1.2.1(e)");
    }

    // protected role method is accessed outside the package, read from bytecode
    // 0.a.3-otjld-protected-role-method-2
    public void test0a3_protectedRoleMethod2() {
        runNegativeTestMatching(
            new String[] {
		"Main0a3prm2.java",
			    "\n" +
			    "import p1.T0a3prm2;\n" +
			    "public class Main0a3prm2 {\n" +
			    "	final T0a3prm2 t = new T0a3prm2 ();\n" +
			    "	void bar () {\n" +
			    "		R<@t> r = null;\n" +
			    "		r.secret();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"p1/T0a3prm2.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class T0a3prm2 {\n" +
			    "	public class R {\n" +
			    "		protected void secret () {}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "1.2.1(e)");
    }

    // public role method is accessed outside the package
    // 0.a.4-otjld-public-role-method-1
    public void test0a4_publicRoleMethod1() {
        runConformTest(
            new String[] {
		"Main0a4prm1.java",
			    "\n" +
			    "import p1.Team0a4prm1 ;\n" +
			    "public class Main0a4prm1 {\n" +
			    "	final Team0a4prm1 t = new Team0a4prm1 ();\n" +
			    "        @SuppressWarnings(\"null\")\n" +
			    "	void bar () {\n" +
			    "		R<@t> r = null;\n" +
			    "		r.secret();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"p1/Team0a4prm1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a4prm1 {\n" +
			    "	public class R {\n" +
			    "		public void secret () {}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            });
    }

    // public role method is accessed outside the package, modifiers read from bytecode
    // 0.a.4-otjld-public-role-method-2
    public void test0a4_publicRoleMethod2() {
        runConformTest(
            new String[] {
		"p1/Team0a4prm2.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a4prm2 {\n" +
			    "	public class R {\n" +
			    "		public void secret () {}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Main0a4prm2.java",
			    "\n" +
			    "import p1.Team0a4prm2 ;\n" +
			    "public class Main0a4prm2 {\n" +
			    "	final Team0a4prm2 t = new Team0a4prm2 ();\n" +
			    "        @SuppressWarnings(\"null\")\n" +
			    "	void bar () {\n" +
			    "		R<@t> r = null;\n" +
			    "		r.secret();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            });
    }

    // public field accessed within team
    // 0.a.5-otjld-public-role-field
    public void test0a5_publicRoleField() {
       
       runConformTest(
            new String[] {
		"T0a5prf.java",
			    "\n" +
			    "public team class T0a5prf {\n" +
			    "	protected class R {\n" +
			    "		public String val = \"OK\";\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new T0a5prf();\n" +
			    "	}\n" +
			    "	T0a5prf() {\n" +
			    "		R r = new R();\n" +
			    "		System.out.print(r.val);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // role field accessed outside role  default visib
    // 0.a.6-otjld-role-field-access-1
    public void test0a6_roleFieldAccess1() {
        runNegativeTestMatching(
            new String[] {
		"Team0a6rfa1.java",
			    "\n" +
			    "public team class Team0a6rfa1 {\n" +
			    "	protected class R {\n" +
			    "		String val = \"OK\";\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		new Team0a6rfa1();\n" +
			    "	}\n" +
			    "	Team0a6rfa1() {\n" +
			    "		R r = new R();\n" +
			    "		System.out.print(r.val);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "visible");
    }

    // protected role field accessed outside role
    // 0.a.6-otjld-role-field-access-2
    public void test0a6_roleFieldAccess2() {
       
       runConformTest(
            new String[] {
		"Team0a6rfa2.java",
			    "\n" +
			    "public team class Team0a6rfa2 {\n" +
			    "    protected class R {\n" +
			    "        protected String val = \"OK\";\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a6rfa2();\n" +
			    "    }\n" +
			    "    Team0a6rfa2() {\n" +
			    "        R r = new R();\n" +
			    "        System.out.print(r.val);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role field accessed from other role
    // 0.a.7-otjld-role-field-access-other-role-1
    public void test0a7_roleFieldAccessOtherRole1() {
        runNegativeTestMatching(
            new String[] {
		"Team0a7rfaor1.java",
			    "\n" +
			    "		public team class Team0a7rfaor1 {\n" +
			    "			protected class R {\n" +
			    "				String val = \"OK\";\n" +
			    "			}\n" +
			    "			protected class R1 {\n" +
			    "				protected void foo() {\n" +
			    "					R r = new R();\n" +
			    "					System.out.print(r.val);\n" +
			    "				}\n" +
			    "			}\n" +
			    "			public static void main(String[] args) {\n" +
			    "				new Team0a7rfaor1();\n" +
			    "			}\n" +
			    "			Team0a7rfaor1() { \n" +
			    "				(new R1()).foo();\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "visible");
    }

    // protected role field accessed from other role
    // 0.a.7-otjld-role-field-access-other-role-2
    public void test0a7_roleFieldAccessOtherRole2() {
       
       runConformTest(
            new String[] {
		"Team0a7rfaor2.java",
			    "\n" +
			    "        public team class Team0a7rfaor2 {\n" +
			    "            protected class R {\n" +
			    "                protected String val = \"OK\";\n" +
			    "            }\n" +
			    "            protected class R1 {\n" +
			    "                protected void foo() {\n" +
			    "                    R r = new R();\n" +
			    "                    System.out.print(r.val);\n" +
			    "                }\n" +
			    "            }\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a7rfaor2();\n" +
			    "            }\n" +
			    "            Team0a7rfaor2() { \n" +
			    "                (new R1()).foo();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // protected role field accessed from other role - receiver is a message send
    // 0.a.7-otjld-role-field-access-other-role-3
    public void test0a7_roleFieldAccessOtherRole3() {
       
       runConformTest(
            new String[] {
		"Team0a7rfaor3.java",
			    "\n" +
			    "        public team class Team0a7rfaor3 {\n" +
			    "            protected class R {\n" +
			    "                public String val = \"OK\";\n" +
			    "            }\n" +
			    "            protected class R1 {\n" +
			    "                protected void foo() {\n" +
			    "                    System.out.print(getR().val);\n" +
			    "                }\n" +
			    "                R getR() {\n" +
			    "                    return new R();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a7rfaor3();\n" +
			    "            }\n" +
			    "            Team0a7rfaor3() { \n" +
			    "                (new R1()).foo();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // role field accessed from other role
    // 0.a.8-otjld-role-field-access-other-role-1
    public void test0a8_roleFieldAccessOtherRole1() {
        runNegativeTestMatching(
            new String[] {
		"Team0a8rfaor1.java",
			    "\n" +
			    "        public team class Team0a8rfaor1 {\n" +
			    "            protected class R {\n" +
			    "                String val = \"OK\";\n" +
			    "            }\n" +
			    "            protected class R1 {\n" +
			    "                protected void foo() { \n" +
			    "                    System.out.print(new R().val);\n" +
			    "                }\n" +
			    "            }\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a8rfaor1();\n" +
			    "            }\n" +
			    "            Team0a8rfaor1() { \n" +
			    "                (new R1()).foo();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "visible");
    }

    // role field accessed from other role
    // 0.a.8-otjld-role-field-access-other-role-2
    public void test0a8_roleFieldAccessOtherRole2() {
       
       runConformTest(
            new String[] {
		"Team0a8rfaor2.java",
			    "\n" +
			    "		public team class Team0a8rfaor2 {\n" +
			    "			protected class R {\n" +
			    "				protected String val = \"OK\";\n" +
			    "			}\n" +
			    "			protected class R1 {\n" +
			    "				protected void foo() { \n" +
			    "					System.out.print(new R().val);\n" +
			    "				}\n" +
			    "			}\n" +
			    "			public static void main(String[] args) {\n" +
			    "				new Team0a8rfaor2();\n" +
			    "			}\n" +
			    "			Team0a8rfaor2() { \n" +
			    "				(new R1()).foo();\n" +
			    "			}\n" +
			    "		}\n" +
			    "	\n"
            },
            "OK");
    }

    // role field accessed across externalized roles
    // 0.a.8-otjld-role-field-access-other-role-2e
    public void test0a8_roleFieldAccessOtherRole2e() {
       
       runConformTest(
            new String[] {
		"T0a8rfaor2eMain.java",
			    "\n" +
			    "public class T0a8rfaor2eMain {\n" +
			    "        public static void main(String[] args) {\n" +
			    "                final Team0a8rfaor2e t = new Team0a8rfaor2e();\n" +
			    "                R1<@t> r = t.new R1();\n" +
			    "                r.foo();\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n",
		"Team0a8rfaor2e.java",
			    "\n" +
			    "		public team class Team0a8rfaor2e {\n" +
			    "		}\n" +
			    "	\n",
		"Team0a8rfaor2e/R.java",
			    "\n" +
			    "team package Team0a8rfaor2e;\n" +
			    "protected class R {\n" +
			    "        protected String val = \"OK\";\n" +
			    "}\n" +
			    "	\n",
		"Team0a8rfaor2e/R1.java",
			    "\n" +
			    "team package Team0a8rfaor2e;\n" +
			    "public class R1 {\n" +
			    "        public void foo() { \n" +
			    "                System.out.print(new R().val);\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // role field accessed across externalized roles (MOVE to incremental tests and solve using BuildManager)
    // 0.a.8-otjld_role-field-access-other-role-2f
    public void _role_test0a8_fieldAccessOtherRole2f() {
       
       runConformTest(
            new String[] {
		"T0a8rfaor2fMain.java",
			    "\n" +
			    "public class T0a8rfaor2fMain {\n" +
			    "        public static void main(String[] args) {\n" +
			    "                final Team0a8rfaor2f t = new Team0a8rfaor2f();\n" +
			    "                R1<@t> r = t.new R1();\n" +
			    "                r.foo();\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n",
		"Team0a8rfaor2f.java",
			    "\n" +
			    "		public team class Team0a8rfaor2f {\n" +
			    "		}\n" +
			    "	\n",
		"Team0a8rfaor2f/R.java",
			    "\n" +
			    "team package Team0a8rfaor2f;\n" +
			    "protected class R {\n" +
			    "        protected String val = \"OK\";\n" +
			    "}\n" +
			    "	\n",
		"Team0a8rfaor2f/R1.java",
			    "\n" +
			    "team package Team0a8rfaor2f;\n" +
			    "public class R1 {\n" +
			    "        void foo() { \n" +
			    "                System.out.print(new R().val);\n" +
			    "        }\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // role field accessed from other role - implicitely inherited to other package
    // 0.a.8-otjld-role-field-access-other-role-3
    public void test0a8_roleFieldAccessOtherRole3() {
       
       runConformTest(
            new String[] {
		"p2/Team0a8rfaor3_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "        public team class Team0a8rfaor3_2 extends p1.Team0a8rfaor3_1 {\n" +
			    "            protected class R1 {\n" +
			    "                protected void foo() {\n" +
			    "                    System.out.print(new R().val);\n" +
			    "                }\n" +
			    "            }\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a8rfaor3_2();\n" +
			    "            }\n" +
			    "            Team0a8rfaor3_2() { \n" +
			    "                (new R1()).foo();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"p1/Team0a8rfaor3_1.java",
			    "\n" +
			    "package p1;    \n" +
			    "        public team class Team0a8rfaor3_1 {\n" +
			    "            protected class R {\n" +
			    "                protected String val = \"OK\";\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // role field accessed from the team - implicitely inherited to other package
    // 0.a.8-otjld-role-field-accessed-by-team-4
    public void test0a8_roleFieldAccessedByTeam4() {
       
       runConformTest(
            new String[] {
		"p2/Team0a8rfabt4_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "        public team class Team0a8rfabt4_2 extends p1.Team0a8rfabt4_1 {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a8rfabt4_2();\n" +
			    "            }\n" +
			    "            Team0a8rfabt4_2() { \n" +
			    "                System.out.print(r.val);\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"p1/Team0a8rfabt4_1.java",
			    "\n" +
			    "package p1;    \n" +
			    "        public team class Team0a8rfabt4_1 {\n" +
			    "            protected R r = new R();\n" +
			    "            protected class R {\n" +
			    "                protected String val = \"OK\";\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // role synth field (this$0) used from nested role - implicitely inherited to other package
    // 0.a.8-otjld-role-field-accessed-by-team-4t
    public void test0a8_roleFieldAccessedByTeam4t() {
       
       runConformTest(
            new String[] {
		"p2/Team0a8rfabt4t_2.java",
			    " // important: compile in one go!\n" +
			    "package p2;\n" +
			    "        public team class Team0a8rfabt4t_2 extends p1.Team0a8rfabt4t_1 {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a8rfabt4t_2();\n" +
			    "            }\n" +
			    "            Team0a8rfabt4t_2() { \n" +
			    "                System.out.print(r.getVal());\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"p1/Team0a8rfabt4t_1.java",
			    "\n" +
			    "package p1;\n" +
			    "        public team class Team0a8rfabt4t_1 {\n" +
			    "            final Mid m = new Mid();\n" +
			    "            protected R<@m> r = m.new R();\n" +
			    "            protected String val = \"OK\";\n" +
			    "            protected team class Mid {\n" +
			    "                public class R {\n" +
			    "                    public String getVal() {\n" +
			    "                        return Team0a8rfabt4t_1.this.val;\n" +
			    "                    }\n" +
			    "                }\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // extract from 4t, but erroneously accessing protected nested role
    // 0.a.8-otjld-role-field-accessed-by-team-4f
    public void test0a8_roleFieldAccessedByTeam4f() {
        runNegativeTestMatching(
            new String[] {
		"p1/Team0a8rfabt4f_1.java",
			    "\n" +
			    "package p1;    \n" +
			    "        public team class Team0a8rfabt4f_1 {\n" +
			    "            final Mid m = new Mid();\n" +
			    "            protected R<@Mid> r = m.new R(); // Mid is an additional error\n" +
			    "            protected String val = \"OK\";\n" +
			    "            protected team class Mid {\n" +
			    "                protected class R {\n" +
			    "                    protected String getVal() {\n" +
			    "                        return Team0a8rfabt4f_1.this.val;\n" +
			    "                    }\n" +
			    "                }\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "1.2.2(a)");
    }

    // role field accessed (write) from the team - implicitely inherited to other package
    // 0.a.8-otjld-role-field-accessed-by-team-5
    public void test0a8_roleFieldAccessedByTeam5() {
       
       runConformTest(
            new String[] {
		"p2/Team0a8rfabt5_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "        public team class Team0a8rfabt5_2 extends p1.Team0a8rfabt5_1 {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a8rfabt5_2();\n" +
			    "            }\n" +
			    "            Team0a8rfabt5_2() { \n" +
			    "                System.out.print(r.val);\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"p1/Team0a8rfabt5_1.java",
			    "\n" +
			    "package p1;    \n" +
			    "        public team class Team0a8rfabt5_1 {\n" +
			    "            protected R r = new R();\n" +
			    "            protected class R {\n" +
			    "                protected String val;\n" +
			    "            }\n" +
			    "            public Team0a8rfabt5_1() {\n" +
			    "                r.val = \"OK\";\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // role field accessed (write) from the team - implicitely inherited to other package, using source type (1 compile)
    // 0.a.8-otjld-role-field-accessed-by-team-5s
    public void test0a8_roleFieldAccessedByTeam5s() {
       
       runConformTest(
            new String[] {
		"p2/Team0a8rfabt5s_2.java",
			    "\n" +
			    "package p2;\n" +
			    "        public team class Team0a8rfabt5s_2 extends p1.Team0a8rfabt5s_1 {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a8rfabt5s_2();\n" +
			    "            }\n" +
			    "            Team0a8rfabt5s_2() { \n" +
			    "                System.out.print(r.val);\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"p1/Team0a8rfabt5s_1.java",
			    "\n" +
			    "package p1;\n" +
			    "        public team class Team0a8rfabt5s_1 {\n" +
			    "            protected R r = new R();\n" +
			    "            protected class R {\n" +
			    "                protected String val;\n" +
			    "            }\n" +
			    "            public Team0a8rfabt5s_1() {\n" +
			    "                r.val = \"OK\";\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // role field accessed from the team - implicitely inherited to other package - write object
    // 0.a.8-otjld-role-field-accessed-by-team-6
    public void test0a8_roleFieldAccessedByTeam6() {
       
       runConformTest(
            new String[] {
		"p2/Team0a8rfabt6_2.java",
			    "\n" +
			    "package p2;\n" +
			    "        public team class Team0a8rfabt6_2 extends p1.Team0a8rfabt6_1 {\n" +
			    "            protected class R {\n" +
			    "                protected void test() {\n" +
			    "                    print();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a8rfabt6_2();\n" +
			    "            }\n" +
			    "            Team0a8rfabt6_2() {\n" +
			    "                r.val = \"OK\";\n" +
			    "                r.test();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"p1/Team0a8rfabt6_1.java",
			    "\n" +
			    "package p1;\n" +
			    "        public team class Team0a8rfabt6_1 {\n" +
			    "            protected R r = new R();\n" +
			    "            protected class R {\n" +
			    "                protected String val = \"NOK\";\n" +
			    "                void print() {\n" +
			    "                    System.out.print(val);\n" +
			    "                }\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // role field accessed from the team - implicitely inherited to other package - super access to default-vis role method
    // 0.a.8-otjld-role-field-accessed-by-team-6s
    public void test0a8_roleFieldAccessedByTeam6s() {
       
       runConformTest(
            new String[] {
		"p2/Team0a8rfabt6s_2.java",
			    "\n" +
			    "package p2;\n" +
			    "        public team class Team0a8rfabt6s_2 extends p1.Team0a8rfabt6s_1 {\n" +
			    "            protected class R2 {\n" +
			    "                protected void test() {\n" +
			    "                    super.print();\n" +
			    "                }\n" +
			    "                void print() { System.out.print(\"NOK\"); }\n" +
			    "            }\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a8rfabt6s_2();\n" +
			    "            }\n" +
			    "            Team0a8rfabt6s_2() {\n" +
			    "                r.val = \"OK\";\n" +
			    "                r.test();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"p1/Team0a8rfabt6s_1.java",
			    "\n" +
			    "package p1;\n" +
			    "        public team class Team0a8rfabt6s_1 {\n" +
			    "            protected R2 r = new R2();\n" +
			    "            protected class R {\n" +
			    "                protected String val = \"NOK\";\n" +
			    "                void print() {\n" +
			    "                    System.out.print(val);\n" +
			    "                }\n" +
			    "            }\n" +
			    "            protected class R2 extends R { }\n" +
			    "        }\n" +
			    "    \n"
            },
            "OK");
    }

    // role field accessed from the team - implicitely inherited to other package  - failed access to default-vis role method
    // 0.a.8-otjld-role-field-accessed-by-team-6f
    public void test0a8_roleFieldAccessedByTeam6f() {
        runNegativeTestMatching(
            new String[] {
		"p2/Team0a8rfabt6f_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "        public team class Team0a8rfabt6f_2 extends p1.Team0a8rfabt6f_1 {\n" +
			    "            Team0a8rfabt6f_2() {\n" +
			    "                r.val = \"OK\";\n" +
			    "                r.print();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"p1/Team0a8rfabt6f_1.java",
			    "\n" +
			    "package p1;    \n" +
			    "        public team class Team0a8rfabt6f_1 {\n" +
			    "            protected R r = new R();\n" +
			    "            protected class R {\n" +
			    "                protected String val = \"NOK\";\n" +
			    "                void print() {\n" +
			    "                    System.out.print(val);\n" +
			    "                }\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "not visible");
    }

    // role field accessed from the team - implicitely inherited to other package - write long
    // 0.a.8-otjld-role-field-accessed-by-team-7
    public void test0a8_roleFieldAccessedByTeam7() {
       
       runConformTest(
            new String[] {
		"p2/Team0a8rfabt7_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "        public team class Team0a8rfabt7_2 extends p1.Team0a8rfabt7_1 {\n" +
			    "            public static void main(String[] args) {\n" +
			    "                new Team0a8rfabt7_2();\n" +
			    "            }\n" +
			    "            Team0a8rfabt7_2() { \n" +
			    "                r.val = 42L;\n" +
			    "                r.print();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n",
		"p1/Team0a8rfabt7_1.java",
			    "\n" +
			    "package p1;    \n" +
			    "        public team class Team0a8rfabt7_1 {\n" +
			    "            protected R r = new R();\n" +
			    "            protected class R {\n" +
			    "                protected long val = 0;\n" +
			    "                protected void print() {\n" +
			    "                    System.out.print(val);\n" +
			    "                }\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n"
            },
            "42");
    }

    // a field of an externalized role is written and read, team is compatible but not identical
    // 0.a.8-otjld-role-field-access-other-role-8
    public void test0a8_roleFieldAccessOtherRole8() {
       
       runConformTest(
            new String[] {
		"T0a8rfaor8Main.java",
			    "\n" +
			    "public class T0a8rfaor8Main {\n" +
			    "    public static void main(String[] args) { \n" +
			    "        final Team0a8rfaor8_2 t2 = new Team0a8rfaor8_2();\n" +
			    "        final Team0a8rfaor8_1 t  = new Team0a8rfaor8_1(t2);\n" +
			    "\n" +
			    "        R<@t> r1 = t.new R();\n" +
			    "        R<@t.theT> r2 = t.theT.new R();\n" +
			    "        r2.val = 3;\n" +
			    "        r1.test(r2);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0a8rfaor8_1.java",
			    "\n" +
			    "public team class Team0a8rfaor8_1 {\n" +
			    "    public final Team0a8rfaor8_1 theT;\n" +
			    "    public Team0a8rfaor8_1(Team0a8rfaor8_1 other) {\n" +
			    "        theT = other;\n" +
			    "    }\n" +
			    "    Team0a8rfaor8_1() { \n" +
			    "        theT = null;\n" +
			    "    }\n" +
			    "    public class R {\n" +
			    "        public org.objectteams.Team theTeam() { return Team0a8rfaor8_1.this; }\n" +
			    "        public int val;\n" +
			    "        public void test(R<@theT> other) {\n" +
			    "            System.out.print(other.val); // must use accessor of team 2\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0a8rfaor8_2.java",
			    "\n" +
			    "public team class Team0a8rfaor8_2 extends Team0a8rfaor8_1 {\n" +
			    "    Team0a8rfaor8_2() { super(); }\n" +
			    "    Team0a8rfaor8_2(Team0a8rfaor8_1 other) { super(other); }\n" +
			    "}\n" +
			    "    \n"
            },
            "3");
    }

    // a non-public field of an externalized role is accessed: error (simple read)
    // 0.a.8-otjld-role-field-access-other-role-9
    public void test0a8_roleFieldAccessOtherRole9() {
        runNegativeTestMatching(
            new String[] {
		"Team0a8rfaor9.java",
			    "\n" +
			    "public team class Team0a8rfaor9 {\n" +
			    "    public class R {\n" +
			    "        protected String val;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team0a8rfaor9 t = new Team0a8rfaor9();\n" +
			    "        R<@t> r = t.new R();\n" +
			    "        System.out.print(r.val);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.1(e)");
    }

    // a non-public field of an externalized role is accessed: error (FieldReference read)
    // 0.a.8-otjld-role-field-access-other-role-10
    public void test0a8_roleFieldAccessOtherRole10() {
        runNegativeTestMatching(
            new String[] {
		"Team0a8rfaor10.java",
			    "\n" +
			    "public team class Team0a8rfaor10 {\n" +
			    "    public class R {\n" +
			    "        protected String val;\n" +
			    "    }\n" +
			    "    R getR() { return new R(); }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team0a8rfaor10 t = new Team0a8rfaor10();\n" +
			    "        System.out.print(t.getR().val);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.1(e)");
    }

    // a non-public field of an externalized role is accessed: error (simple write)
    // 0.a.8-otjld-role-field-access-other-role-11
    public void test0a8_roleFieldAccessOtherRole11() {
        runNegativeTestMatching(
            new String[] {
		"Team0a8rfaor11.java",
			    "\n" +
			    "public team class Team0a8rfaor11 {\n" +
			    "    public class R {\n" +
			    "        protected String val;\n" +
			    "    }\n" +
			    "    R getR() { return new R(); }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team0a8rfaor11 t = new Team0a8rfaor11();\n" +
			    "        R<@t> r = t.new R();\n" +
			    "        r.val = \"NOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.1(e)");
    }

    // a non-public field of an externalized role is accessed: error (FieldReference write)
    // 0.a.8-otjld-role-field-access-other-role-12
    public void test0a8_roleFieldAccessOtherRole12() {
        runNegativeTestMatching(
            new String[] {
		"Team0a8rfaor12.java",
			    "\n" +
			    "public team class Team0a8rfaor12 {\n" +
			    "    public class R {\n" +
			    "        protected String val;\n" +
			    "    }\n" +
			    "    R getR() { return new R(); }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team0a8rfaor12 t = new Team0a8rfaor12();\n" +
			    "        t.getR().val = \"NOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.1(e)");
    }

    // a wide field is accessed via another role instance of the same team
    // 0.a.8-otjld-role-field-access-other-role-13
    public void test0a8_roleFieldAccessOtherRole13() {
       
       runConformTest(
            new String[] {
		"Team0a8rfaor13.java",
			    "\n" +
			    "public team class Team0a8rfaor13 {\n" +
			    "    protected class R {\n" +
			    "        protected long val;\n" +
			    "        protected R(long v) {\n" +
			    "            this.val = v;\n" +
			    "        }\n" +
			    "        protected void test(R other) {\n" +
			    "            other.val += 3;\n" +
			    "            System.out.print(other.val);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team0a8rfaor13() {\n" +
			    "        R r1 = new R(7L);\n" +
			    "        R r2 = new R(10L);\n" +
			    "        r1.test(r2);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a8rfaor13();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "13");
    }

    // a role has a public static final field
    // 0.a.9-otjld-static-final-role-field-1
    public void test0a9_staticFinalRoleField1() {
       
       runConformTest(
            new String[] {
		"T0a9sfrf1Main.java",
			    "\n" +
			    "public class T0a9sfrf1Main {\n" +
			    "        public static void main (String[] args) {\n" +
			    "                T0a9sfrf1.test();\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team0a9sfrf1.java",
			    "\n" +
			    "public team class Team0a9sfrf1 {\n" +
			    "        public class R {\n" +
			    "                public static final String val = \"OK\";\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"T0a9sfrf1.java",
			    "\n" +
			    "public class T0a9sfrf1 {\n" +
			    "        static void test() {\n" +
			    "                final Team0a9sfrf1 t = new Team0a9sfrf1();\n" +
			    "                R<@t> r = t.new R();\n" +
			    "                System.out.print(r.val);\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n"
            },
            "OK");
    }

    // a role has a public static non-final field - access via role instance
    // 0.a.9-otjld-static-final-role-field-1a
    public void test0a9_staticFinalRoleField1a() {
        runNegativeTestMatching(
            new String[] {
		"Team0a9sfrf1a.java",
			    "\n" +
			    "public team class Team0a9sfrf1a {\n" +
			    "        public class R {\n" +
			    "                public static String val;\n" +
			    "                R() {\n" +
			    "                    val = \"OK\";\n" +
			    "                }\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n"
            },
            "Compiler limitation");
    }

    // a role has a public static non-final field - access via anchored role type: unsupported syntax
    // 0.a.9-otjld_static-final-role-field-1b
    public void _static_test0a9_finalRoleField1b() {
       
       runConformTest(
            new String[] {
		"T0a9sfrf1bMain.java",
			    "\n" +
			    "public class T0a9sfrf1bMain {\n" +
			    "        public static void main (String[] args) {\n" +
			    "                T0a9sfrf1b.test();\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team0a9sfrf1b.java",
			    "\n" +
			    "public team class Team0a9sfrf1b {\n" +
			    "        public class R {\n" +
			    "                public static String val;\n" +
			    "                R() {\n" +
			    "                    val = \"OK\";\n" +
			    "                }\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"T0a9sfrf1b.java",
			    "\n" +
			    "public class T0a9sfrf1b {\n" +
			    "        static void test() {\n" +
			    "                final Team0a9sfrf1b t = new Team0a9sfrf1b();\n" +
			    "                System.out.print(R<@t>.val);\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n"
            },
            "OK");
    }

    // a role has a public static non-final field - access via anchored role type
    // 0.a.9-otjld_static-final-role-field-1c
    public void _static_test0a9_finalRoleField1c() {
       
       runConformTest(
            new String[] {
		"T0a9sfrf1cMain.java",
			    "\n" +
			    "public class T0a9sfrf1cMain {\n" +
			    "        public static void main (String[] args) {\n" +
			    "                T0a9sfrf1c.test();\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team0a9sfrf1c.java",
			    "\n" +
			    "public team class Team0a9sfrf1c {\n" +
			    "        public class R {\n" +
			    "                public static String val;\n" +
			    "                R() {\n" +
			    "                    val = \"OK\";\n" +
			    "                }\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"T0a9sfrf1c.java",
			    "\n" +
			    "public class T0a9sfrf1c {\n" +
			    "        static void test() {\n" +
			    "                final Team0a9sfrf1c t = new Team0a9sfrf1c();\n" +
			    "                System.out.print(t.R.val);\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n"
            },
            "OK");
    }

    // a role has a public static non-final field - access via fully static role type
    // 0.a.9-otjld_static-final-role-field-1d
    public void _static_test0a9_finalRoleField1d() {
        runNegativeTestMatching(
            new String[] {
		"T0a9sfrf1d.java",
			    "\n" +
			    "public class T0a9sfrf1d {\n" +
			    "        static void test() {\n" +
			    "                System.out.print(Team0a9sfrf1d.R.val);\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team0a9sfrf1d.java",
			    "\n" +
			    "public team class Team0a9sfrf1d {\n" +
			    "        public class R {\n" +
			    "                public static String val;\n" +
			    "                R() {\n" +
			    "                    val = \"OK\";\n" +
			    "                }\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n"
            },
            "??");
    }

    // a role has a protected static final field
    // 0.a.9-otjld-static-final-role-field-2
    public void test0a9_staticFinalRoleField2() {
        runNegativeTestMatching(
            new String[] {
		"T0a9sfrf2Main.java",
			    "\n" +
			    "public class T0a9sfrf2Main {\n" +
			    "        public static void main (String[] args) {\n" +
			    "                final Team0a9sfrf2 t = new Team0a9sfrf2();\n" +
			    "                R<@t> r = t.new R();\n" +
			    "                System.out.print(r.val);\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team0a9sfrf2.java",
			    "\n" +
			    "public team class Team0a9sfrf2 {\n" +
			    "        public class R {\n" +
			    "                protected static final String val = \"OK\";\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n"
            },
            "1.2.1(e)");
    }

    // a role has a protected static final field
    // 0.a.9-otjld-static-final-role-field-3
    public void test0a9_staticFinalRoleField3() {
       
       runConformTest(
            new String[] {
		"T0a9sfrf3Main.java",
			    "\n" +
			    "public class T0a9sfrf3Main {\n" +
			    "        public static void main (String[] args) {\n" +
			    "                new Team0a9sfrf3_2();\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team0a9sfrf3_1.java",
			    "\n" +
			    "public team class Team0a9sfrf3_1 {\n" +
			    "        public class R {\n" +
			    "                protected static final String val = \"OK\";\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team0a9sfrf3_2.java",
			    "\n" +
			    "public team class Team0a9sfrf3_2 extends Team0a9sfrf3_1 {\n" +
			    "        Team0a9sfrf3_2() {\n" +
			    "                System.out.print(R.val);\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n"
            },
            "OK");
    }

    // a role has a private static final field, witness for AIOOBE in SyntheticRoleBridgeMethodBinding
    public void test0a9_staticFinalRoleField4() {
        runConformTest(
            new String[] {
		"T0a9sfrf4Main.java",
			    "\n" +
			    "public class T0a9sfrf4Main {\n" +
			    "        public static void main (String[] args) {\n" +
			    "                final Team0a9sfrf4 t = new Team0a9sfrf4();\n" +
			    "                R<@t> r = t.new R();\n" +
			    "                System.out.print(r.getVal());\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n",
		"Team0a9sfrf4.java",
			    "\n" +
			    "public team class Team0a9sfrf4 {\n" +
			    "        public class R {\n" +
			    "                private static final int val = 13;\n" +
			    "                public int getVal() { return val; }\n" +
			    "        }\n" +
			    "}\n" +
			    "        \n"
            },
            "13");
    }

    // a team field accessed from its role across packages needs a synthetic accessor - role type
    // 0.a.10-otjld-team-field-needs-synthetic-1
    public void test0a10_teamFieldNeedsSynthetic1() {
       
       runConformTest(
            new String[] {
		"p2/Team0a10tfns1_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "public team class Team0a10tfns1_2 extends p1.Team0a10tfns1_1 {\n" +
			    "    Team0a10tfns1_2() {\n" +
			    "        r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a10tfns1_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/Team0a10tfns1_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a10tfns1_1 {\n" +
			    "    protected R r;\n" +
			    "    protected class R {\n" +
			    "        public void test() {\n" +
			    "            if (r == this) \n" +
			    "                System.out.print(\"OK\");\n" +
			    "            else\n" +
			    "                System.out.print(\"NOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team field accessed from its role across packages needs a synthetic accessor - basic type
    // 0.a.10-otjld-team-field-needs-synthetic-2
    public void test0a10_teamFieldNeedsSynthetic2() {
       
       runConformTest(
            new String[] {
		"p2/Team0a10tfns2_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "public team class Team0a10tfns2_2 extends p1.Team0a10tfns2_1 {\n" +
			    "    Team0a10tfns2_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.set(\"OK\");\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a10tfns2_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/Team0a10tfns2_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a10tfns2_1 {\n" +
			    "    protected String s;\n" +
			    "    protected class R {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(s);\n" +
			    "        }\n" +
			    "        public void set(String aS) {\n" +
			    "            s = aS;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team field accessed from its role across packages needs a synthetic accessor - qualified outer access
    // 0.a.10-otjld-team-field-needs-synthetic-3
    public void test0a10_teamFieldNeedsSynthetic3() {
       
       runConformTest(
            new String[] {
		"p2/Team0a10tfns3_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "public team class Team0a10tfns3_2 extends p1.Team0a10tfns3_1 {\n" +
			    "    Team0a10tfns3_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.set(\"OK\");\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a10tfns3_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/Team0a10tfns3_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a10tfns3_1 {\n" +
			    "    protected String s;\n" +
			    "    protected class R {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(Team0a10tfns3_1.this.s);\n" +
			    "        }\n" +
			    "        public void set(String s) {\n" +
			    "            Team0a10tfns3_1.this.s = s;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team field accessed from its role across packages needs a synthetic accessor - qualified field access
    // 0.a.10-otjld-team-field-needs-synthetic-4
    public void test0a10_teamFieldNeedsSynthetic4() {
       
       runConformTest(
            new String[] {
		"p2/Team0a10tfns4_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "public team class Team0a10tfns4_2 extends p1.Team0a10tfns4_1 {\n" +
			    "    Team0a10tfns4_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.set(\"OK\");\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a10tfns4_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/Team0a10tfns4_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a10tfns4_1 {\n" +
			    "    protected String s;\n" +
			    "    protected class R {\n" +
			    "        Team0a10tfns4_1 t;\n" +
			    "        protected R () {\n" +
			    "            t = Team0a10tfns4_1.this;\n" +
			    "        }\n" +
			    "        public void test() {\n" +
			    "            System.out.print(t.s);\n" +
			    "        }\n" +
			    "        public void set(String s) {\n" +
			    "            t.s = s;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team field accessed from its role across packages needs a synthetic accessor - broken Outer.this in field initializer
    // 0.a.10-otjld-team-field-needs-synthetic-5
    public void test0a10_teamFieldNeedsSynthetic5() {
       
       runConformTest(
            new String[] {
		"p2/Team0a10tfns5_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "public team class Team0a10tfns5_2 extends p1.Team0a10tfns5_1 {\n" +
			    "    Team0a10tfns5_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.set(\"OK\");\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a10tfns5_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/Team0a10tfns5_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a10tfns5_1 {\n" +
			    "    protected String s;\n" +
			    "    protected class R {\n" +
			    "        Team0a10tfns5_1 t = Team0a10tfns5_1.this;\n" +
			    "        public void test() {\n" +
			    "            System.out.print(t.s);\n" +
			    "        }\n" +
			    "        public void set(String s) {\n" +
			    "            t.s = s;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team method accessed from its role across packages needs a synthetic accessor - role type
    // 0.a.11-otjld-team-method-needs-synthetic-1
    public void test0a11_teamMethodNeedsSynthetic1() {
       
       runConformTest(
            new String[] {
		"p2/Team0a11tmns1_2.java",
			    "\n" +
			    "package p2;    \n" +
			    "public team class Team0a11tmns1_2 extends p1.Team0a11tmns1_1 {\n" +
			    "    Team0a11tmns1_2() {\n" +
			    "        r = new R();\n" +
			    "        getR().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a11tmns1_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/Team0a11tmns1_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a11tmns1_1 {\n" +
			    "    protected R r;\n" +
			    "    protected R getR() { return r; }\n" +
			    "    protected class R {\n" +
			    "        public void test() {\n" +
			    "            if (getR() == this) \n" +
			    "                System.out.print(\"OK\");\n" +
			    "            else\n" +
			    "                System.out.print(\"NOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role accesses a friendly field of its team, team inheritance crosses packages
    // 0.a.12-otjld-role-accessing-team-field-1
    public void test0a12_roleAccessingTeamField1() {
       
       runConformTest(
            new String[] {
		"p2/Team0a12ratf1_2.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team0a12ratf1_2 extends p1.Team0a12ratf1_1 {\n" +
			    "    public Team0a12ratf1_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a12ratf1_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/Team0a12ratf1_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a12ratf1_1 {\n" +
			    "    String val = \"OK\";\n" +
			    "    protected class R {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(Team0a12ratf1_1.this.val);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role accesses a protected field of its team, team inheritance crosses packages
    // 0.a.12-otjld-role-accessing-team-field-2
    public void test0a12_roleAccessingTeamField2() {
       
       runConformTest(
            new String[] {
		"p2/Team0a12ratf2_2.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team0a12ratf2_2 extends p1.Team0a12ratf2_1 {\n" +
			    "    public Team0a12ratf2_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a12ratf2_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/Team0a12ratf2_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team0a12ratf2_1 {\n" +
			    "    protected String val = \"OK\";\n" +
			    "    protected class R {\n" +
			    "        public void test() {\n" +
			    "            System.out.print(Team0a12ratf2_1.this.val);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a local class accesses a private feature of its enclosing role
    // 0.a.13-otjld-private-role-method-accessed-1
    public void test0a13_privateRoleMethodAccessed1() {
       
       runConformTest(
            new String[] {
		"Team0a13prma1.java",
			    "\n" +
			    "public team class Team0a13prma1 {\n" +
			    "    protected class R {\n" +
			    "        private void print() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "        protected void test() {\n" +
			    "            class L {\n" +
			    "                void doit() {\n" +
			    "                    print();\n" +
			    "                }\n" +
			    "            };\n" +
			    "            new L().doit();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team0a13prma1 () {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a13prma1();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a private role method is callin bound
    // 0.a.13-otjld-private-role-method-accessed-2
    public void test0a13_privateRoleMethodAccessed2() {
       
       runConformTest(
            new String[] {
		"Team0a13prma2.java",
			    "\n" +
			    "public team class Team0a13prma2 {\n" +
			    "    protected class R playedBy T0a13prma2 {\n" +
			    "        private void print() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "        print <- after hook;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        org.objectteams.Team t = new Team0a13prma2();\n" +
			    "        t.activate();\n" +
			    "        new T0a13prma2().hook();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T0a13prma2.java",
			    "\n" +
			    "public class T0a13prma2 {\n" +
			    "    void hook() { }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a protected role type is used qualified
    // 0.a.14-otjld-qualified-protected-role-1
    public void test0a14_qualifiedProtectedRole1() {
        runNegativeTestMatching(
            new String[] {
		"Team0a14qpr1.java",
			    " // else: 1.2.2(a)\n" +
			    "public team class Team0a14qpr1 {\n" +
			    "    protected class R {}\n" +
			    "    \n" +
			    "    void foo() {\n" +
			    "        Team0a14qpr1.this.R r = new R();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Syntax");
    }

    // a protected role type is used qualified
    // 0.a.14-otjld-qualified-protected-role-2
    public void test0a14_qualifiedProtectedRole2() {
        runNegativeTestMatching(
            new String[] {
		"Team0a14qpr2.java",
			    " \n" +
			    "public team class Team0a14qpr2 {\n" +
			    "    protected class R {}\n" +
			    "    \n" +
			    "    void foo() {\n" +
			    "        Team0a14qpr2.R r = new R();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.3(b)");
    }

    // a public role advertizes a protected role in a public method
    // 0.a.15-otjld-advertize-protected-role-1
    public void test0a15_advertizeProtectedRole1() {
        runNegativeTestMatching(
            new String[] {
		"T0a15apr1Main.java",
			    "\n" +
			    "public class T0a15apr1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team0a15apr1 t = new Team0a15apr1();\n" +
			    "        PublicRole<@t> r1 = new PublicRole<@t>();\n" +
			    "        SecretRole<@t> r2 = r1.getSecret();\n" +
			    "    }\n" +
			    "}\n" +
			    "  \n",
		"Team0a15apr1.java",
			    "\n" +
			    "public team class Team0a15apr1 {\n" +
			    "  public class PublicRole {\n" +
			    "    public SecretRole getSecret() {\n" +
			    "      return new SecretRole();\n" +
			    "    }\n" +
			    "  }\n" +
			    "  protected class SecretRole {}\n" +
			    "}\n" +
			    "  \n"
            },
            "1.2.2(a)");
    }

    // a public role advertizes a protected role in a public method
    // 0.a.15-otjld-advertize-protected-role-1a
    public void test0a15_advertizeProtectedRole1a() {
       
       runConformTest(
            new String[] {
		"T0a15apr1aMain.java",
			    "\n" +
			    "public class T0a15apr1aMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team0a15apr1a t = new Team0a15apr1a();\n" +
			    "        PublicRole<@t> r1 = new PublicRole<@t>();\n" +
			    "        Object r2 = r1.getSecret();\n" +
			    "        System.out.print(r2.getClass().getName());\n" +
			    "    }\n" +
			    "}\n" +
			    "  \n",
		"Team0a15apr1a.java",
			    "\n" +
			    "public team class Team0a15apr1a {\n" +
			    "  public class PublicRole {\n" +
			    "    public SecretRole getSecret() {\n" +
			    "      return new SecretRole();\n" +
			    "    }\n" +
			    "  }\n" +
			    "  protected class SecretRole {\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n"
            },
            "Team0a15apr1a$__OT__SecretRole");
    }

    // a public role advertizes a protected role in a protected method - witness also for bug re non-final anchor
    // 0.a.15-otjld-advertize-protected-role-2
    public void test0a15_advertizeProtectedRole2() {
       
       runConformTest(
            new String[] {
		"Team0a15apr2.java",
			    "\n" +
			    "public team class Team0a15apr2 {\n" +
			    "  public class PublicRole {\n" +
			    "    protected SecretRole getSecret() {\n" +
			    "      return new SecretRole();\n" +
			    "    }  \n" +
			    "  }\n" +
			    "  protected class SecretRole {\n" +
			    "    protected void print() { System.out.print(\"Secret\"); }\n" +
			    "  }\n" +
			    "  Team0a15apr2() {\n" +
			    "    SecretRole sr = new PublicRole().getSecret();\n" +
			    "    sr.print();\n" +
			    "  }\n" +
			    "  public static void main(String[] args) {\n" +
			    "    new Team0a15apr2();\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n"
            },
            "Secret");
    }

    // a protected role advertizes a protected role in a public method
    // 0.a.15-otjld-advertize-protected-role-3
    public void test0a15_advertizeProtectedRole3() {
       
       runConformTest(
            new String[] {
		"Team0a15apr3.java",
			    "\n" +
			    "public team class Team0a15apr3 {\n" +
			    "  protected class ProtectedRole {\n" +
			    "    public SecretRole getSecret() {\n" +
			    "      return new SecretRole();\n" +
			    "    }  \n" +
			    "  }\n" +
			    "  protected class SecretRole {\n" +
			    "    protected void print() { System.out.print(\"Secret\"); }\n" +
			    "  }\n" +
			    "  Team0a15apr3() {\n" +
			    "    SecretRole sr = new ProtectedRole().getSecret();\n" +
			    "    sr.print();\n" +
			    "  }\n" +
			    "  public static void main(String[] args) {\n" +
			    "    new Team0a15apr3();\n" +
			    "  }\n" +
			    "}\n" +
			    "  \n"
            },
            "Secret");
    }

    // a role overrides a static method from its tsuper
    // 0.a.16-otjld_unsupported_static-role-method-1
    public void _unsupported_test0a16_staticRoleMethod1() {
       
       runConformTest(
            new String[] {
		"Team0a16srm1_2.java",
			    "\n" +
			    "public team class Team0a16srm1_2 extends Team0a16srm1_1 {\n" +
			    "    public class R {\n" +
			    "        public static void foo() {\n" +
			    "            System.out.println(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team0a16srm1_2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0a16srm1_1.java",
			    "\n" +
			    "public team class Team0a16srm1_1 {\n" +
			    "    public class R {\n" +
			    "        public static void foo() {\n" +
			    "            System.out.println(\"wrong\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        R.foo();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }
}
