/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 Stephan Herrmann
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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.rolesandteams;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

public class Confinement extends AbstractOTJLDTest {
	
	public Confinement(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test1710_confinedBoundRole3"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return Confinement.class;
	}
    // attempting to invoke a method from Object on an IConfined object
    // 1.7.1-otjld-invalid-method-on-iconfined-1
    public void test171_invalidMethodOnIconfined1() {
        runNegativeTestMatching(
            new String[] {
		"T171imoi1Main.java",
			    "\n" +
			    "public class T171imoi1Main {\n" +
			    "    public static void main (String[] args) {\n" +
			    "        org.objectteams.IConfined i = new T171imoi1();\n" +
			    "        Object o = i.clone();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T171imoi1.java",
			    "\n" +
			    "public class T171imoi1 implements org.objectteams.IConfined {\n" +
			    "    void foo() {\n" +
			    "        System.out.print(\"foo\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "undefined");
    }

    // invoking a method from user class on an IConfined object
    // 1.7.1-otjld-invalid-method-on-iconfined-2
    public void test171_invalidMethodOnIconfined2() {
       
       runConformTest(
            new String[] {
		"T171imoi2Main.java",
			    "\n" +
			    "public class T171imoi2Main {\n" +
			    "    public static void main (String[] args) {\n" +
			    "        I171imoi2 i = new T171imoi2();\n" +
			    "        i.foo();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"I171imoi2.java",
			    "\n" +
			    "public interface I171imoi2 extends org.objectteams.IConfined {\n" +
			    "    void foo();\n" +
			    "}\n" +
			    "    \n",
		"T171imoi2.java",
			    "\n" +
			    "public class T171imoi2 implements I171imoi2 {\n" +
			    "    public void foo() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // attempting to invoke a method from Object on an IConfined role
    // 1.7.1-otjld-invalid-method-on-iconfined-3
    public void test171_invalidMethodOnIconfined3() {
        runNegativeTest(
            new String[] {
		"Team171imoi3.java",
			    "\n" +
			    "public team class Team171imoi3 {\n" +
			    "    protected class R implements IConfined {\n" +
			    "        void foo() {\n" +
			    "            System.out.print(\"foo\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team171imoi3() {\n" +
			    "        IConfined i = new R();\n" +
			    "        Object o = i.clone();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team171imoi3.java (at line 10)\n" + 
    		"	Object o = i.clone();\n" + 
    		"	             ^^^^^\n" + 
    		"The method clone() is undefined for the type IConfined<@tthis[Team171imoi3]>\n" + 
    		"----------\n");
    }

    // attempting to invoke a method from Object on an IConfined role -- illegal qualified role type
    // 1.7.1-otjld-invalid-method-on-iconfined-3b
    public void test171_invalidMethodOnIconfined3b() {
        runNegativeTestMatching(
            new String[] {
		"Team171imoi3b.java",
			    "\n" +
			    "public team class Team171imoi3b {\n" +
			    "    protected class R implements org.objectteams.Team.IConfined {\n" +
			    "        void foo() {\n" +
			    "            System.out.print(\"foo\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team171imoi3b() {\n" +
			    "        org.objectteams.Team.IConfined i = new R();\n" +
			    "        Object o = i.clone();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.2(b)");
    }

    // attempting to invoke a method from Object on an externalized IConfined role
    // 1.7.1-otjld-invalid-method-on-iconfined-3e
    public void test171_invalidMethodOnIconfined3e() {
        runNegativeTestMatching(
            new String[] {
		"Team171imoi3e.java",
			    "\n" +
			    "public team class Team171imoi3e {\n" +
			    "    protected class R implements IConfined {\n" +
			    "        void foo() {\n" +
			    "            System.out.print(\"foo\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public IConfined getR() {\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "    public boolean foo() {\n" +
			    "        final Team171imoi3e t= new Team171imoi3e();\n" +
			    "        IConfined<@t> i = t.getR();\n" +
			    "        return i.equals(null);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "undefined");
    }

    // attempting to invoke a method from Object on an externalized IConfined role
    // 1.7.1-otjld-invalid-method-on-iconfined-3E
    public void test171_invalidMethodOnIconfined3E() {
        runNegativeTestMatching(
            new String[] {
		"Team171imoi3E.java",
			    "\n" +
			    "public team class Team171imoi3E {\n" +
			    "    protected class R implements IConfined {\n" +
			    "        void foo() {\n" +
			    "            System.out.print(\"foo\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public IConfined getR() {\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "    public String foo() {\n" +
			    "        final Team171imoi3E t= new Team171imoi3E();\n" +
			    "        IConfined<@t> i = t.getR();\n" +
			    "        return i.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "undefined");
    }

    // attempting to invoke a method from Object on an externalized IConfined role
    // 1.7.1-otjld-invalid-method-on-iconfined-3f
    public void test171_invalidMethodOnIconfined3f() {
        runNegativeTestMatching(
            new String[] {
		"Team171imoi3f.java",
			    "\n" +
			    "public team class Team171imoi3f {\n" +
			    "    protected class R implements IConfined {\n" +
			    "        void foo() {\n" +
			    "            System.out.print(\"foo\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public IConfined getR() {\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "    public String foo() {\n" +
			    "        final Team171imoi3f t= new Team171imoi3f();\n" +
			    "        IConfined<@t> i = t.getR();\n" +
			    "        return \"Result:\"+i;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "undefined");
    }

    // attempting to invoke a method from Object on an externalized IConfined role
    // 1.7.1-otjld-invalid-method-on-iconfined-3F
    public void test171_invalidMethodOnIconfined3F() {
        runNegativeTestMatching(
            new String[] {
		"Team171imoi3F.java",
			    "\n" +
			    "public team class Team171imoi3F {\n" +
			    "    protected class R implements IConfined {\n" +
			    "        void foo() {\n" +
			    "            System.out.print(\"foo\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public IConfined getR() {\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "    public String foo() {\n" +
			    "        final Team171imoi3F t= new Team171imoi3F();\n" +
			    "        IConfined<@t> i = t.getR();\n" +
			    "        return i+\"Result:\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "undefined");
    }

    // invoking a method from user class on an IConfined role
    // 1.7.1-otjld-invalid-method-on-iconfined-4
    public void test171_invalidMethodOnIconfined4() {
       
       runConformTest(
            new String[] {
		"T171imoi4Main.java",
			    "\n" +
			    "public class T171imoi4Main {\n" +
			    "    public static void main (String[] args) {\n" +
			    "        Team171imoi4 t = new Team171imoi4();\n" +
			    "	I171imoi4 r = t.getR();\n" +
			    "        r.foo();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"I171imoi4.java",
			    "\n" +
			    "public interface I171imoi4 extends org.objectteams.IConfined {\n" +
			    "	void foo();\n" +
			    "}\n" +
			    "	\n",
		"Team171imoi4.java",
			    "\n" +
			    "public team class Team171imoi4 {\n" +
			    "	protected class R171imoi4 implements I171imoi4 {\n" +
			    "    		public void foo() {\n" +
			    "        		System.out.print(\"OK\");\n" +
			    "    		}\n" +
			    "	}\n" +
			    "	public I171imoi4 getR() {\n" +
			    "		return new R171imoi4();\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // invoking a method from user class on an IConfined role
    // 1.7.1-otjld-invalid-method-on-iconfined-5
    public void test171_invalidMethodOnIconfined5() {
       
       runConformTest(
            new String[] {
		"T171imoi5Main.java",
			    "\n" +
			    "public class T171imoi5Main {\n" +
			    "    public static void main (String[] args) {\n" +
			    "        final Team171imoi5 t = new Team171imoi5();\n" +
			    "	I171imoi5<@t> r = t.getR();\n" +
			    "        r.foo();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team171imoi5.java",
			    "\n" +
			    "public team class Team171imoi5 {\n" +
			    "	public interface I171imoi5 extends IConfined {\n" +
			    "    		void foo();\n" +
			    "	}\n" +
			    "	protected class R171imoi5 implements I171imoi5 {\n" +
			    "    		public void foo() {\n" +
			    "        		System.out.print(\"OK\");\n" +
			    "    		}\n" +
			    "	}\n" +
			    "	public I171imoi5 getR() {\n" +
			    "		return new R171imoi5();\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // invoking a method that is listed in Confined - according to 6.2(a) nothing is listed there
    // 1.7.1-otjld_invalid_invalid-method-on-iconfined-6
    public void _invalid_test171_invalidMethodOnIconfined6() {
       
       runConformTest(
            new String[] {
		"T171imoi6Main.java",
			    "\n" +
			    "public class T171imoi6Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team171imoi6 t = new Team171imoi6();\n" +
			    "		t.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team171imoi6.java",
			    "\n" +
			    "public team class Team171imoi6 {\n" +
			    "	protected class R extends Confined {\n" +
			    "	}\n" +
			    "	void test() { \n" +
			    "		R r = new R();\n" +
			    "		int h = r.hashCode();\n" +
			    "		System.out.print(\"OK\");\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n" +
			    "	\n"
            },
            "OK");
    }

    // attempting to widen a confined role to type Object
    // 1.7.2-otjld-invalid-widening-of-confined-role-1
    public void test172_invalidWideningOfConfinedRole1() {
        runNegativeTestMatching(
            new String[] {
		"Team172iwocr1.java",
			    "\n" +
			    "public team class Team172iwocr1 {\n" +
			    "    protected class R extends Confined {\n" +
			    "    }\n" +
			    "    Object getR() {\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "cannot convert");
    }

    // attempting to cast a confined role to type Object
    // 1.7.2-otjld-invalid-widening-of-confined-role-1c
    public void test172_invalidWideningOfConfinedRole1c() {
        runNegativeTestMatching(
            new String[] {
		"Team172iwocr1c.java",
			    "\n" +
			    "public team class Team172iwocr1c {\n" +
			    "    protected class R extends Confined {\n" +
			    "    }\n" +
			    "    Object getR() {\n" +
			    "        return (Object)new R();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Cannot cast");
    }

    // attempting to cast a confined role to type Object -- read class file
    // 1.7.2-otjld-invalid-widening-of-confined-role-1C
    public void test172_invalidWideningOfConfinedRole1C() {
        runNegativeTestMatching(
            new String[] {
		"Team172iwocr1C_2.java",
			    "\n" +
			    "public team class Team172iwocr1C_2 extends Team172iwocr1C_1 {\n" +
			    "    Object getR() {\n" +
			    "        return (Object)new R();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team172iwocr1C_1.java",
			    "\n" +
			    "public team class Team172iwocr1C_1 {\n" +
			    "    protected class R extends Confined {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Cannot cast");
    }

    // attempting to assign a confined role to different team instance - missing final
    // 1.7.2-otjld-invalid-widening-of-confined-role-2
    public void test172_invalidWideningOfConfinedRole2() {
        runNegativeTestMatching(
            new String[] {
		"Team172iwocr2.java",
			    "\n" +
			    "public team class Team172iwocr2 {\n" +
			    "    protected class R extends Confined {\n" +
			    "    }\n" +
			    "    void test (R r) {\n" +
			    "	Team172iwocr2 t = new Team172iwocr2();\n" +
			    "	t.test(r);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "is not applicable");
    }

    // attempting to assign a confined role to different team instance
    // 1.7.2-otjld-invalid-widening-of-confined-role-3
    public void test172_invalidWideningOfConfinedRole3() {
        runNegativeTestMatching(
            new String[] {
		"Team172iwocr3.java",
			    "\n" +
			    "public team class Team172iwocr3 {\n" +
			    "    protected class R extends Confined {\n" +
			    "    }\n" +
			    "    void test (R r) {\n" +
			    "	final Team172iwocr3 t = new Team172iwocr3();\n" +
			    "	t.test(r);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "is not applicable");
    }

    // attempting to widen a confined role from custom IConfined to type Object
    public void test172_invalidWideningOfConfinedRole4() {
        runNegativeTestMatching(
            new String[] {
		"Team172iwocr4.java",
			    "\n" +
			    "public team class Team172iwocr4 {\n" +
			    "	 protected interface IMyConfined extends IConfined {}\n" +
			    "    protected class R extends Confined implements IMyConfined {\n" +
			    "    }\n" +
			    "    Object getR() {\n" +
			    "        IMyConfined ic = new R();\n" +
			    "		 return ic;" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "cannot convert");
    }

    // attempting to cast a confined role from custom IConfined to type Object
    public void test172_invalidWideningOfConfinedRole5() {
        runNegativeTest(
            new String[] {
		"Team172iwocr5.java",
			    "\n" +
			    "public team class Team172iwocr5 {\n" +
			    "	 protected interface IMyConfined extends IConfined {}\n" +
			    "    protected class R extends Confined implements IMyConfined {\n" +
			    "    }\n" +
			    "    Object getR() {\n" +
			    "        IMyConfined ic = new R();\n" +
			    "		 return (Object)ic;" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team172iwocr5.java (at line 8)\n" + 
    		"	return (Object)ic;    }\n" + 
    		"	       ^^^^^^^^^^\n" + 
    		"Cannot cast from IMyConfined<@tthis[Team172iwocr5]> to Object\n" + 
    		"----------\n");
    }

    // attempting to widen a confined role from custom IConfined to type Object applies lowering
    public void test172_invalidWideningOfConfinedRole6() {
        runConformTest(
            new String[] {
		"Team172iwocr6.java",
			    "\n" +
			    "public team class Team172iwocr6 {\n" +
			    "	 protected interface IMyConfined extends IConfined {}\n" +
			    "    protected class R extends Confined implements IMyConfined playedBy T172iwocr6 {\n" +
			    "    }\n" +
			    "    void testR() {\n" +
			    "        Object o = new R(new T172iwocr6());\n" +
			    "		 System.out.print(o instanceof T172iwocr6);\n" +
			    "    }\n" +
			    "	 public static void main(String... args) {\n" +
			    "		 new Team172iwocr6().testR();" +
			    "	 }\n" +
			    "}\n",
		"T172iwocr6.java",
			    "public class T172iwocr6 {}\n"
            },
            "true");
    }

    // a protected role type is mentioned outside the team
    // 1.7.3-otjld-mentioning-protected-role-outside-team-1
    public void test173_mentioningProtectedRoleOutsideTeam1() {
        runNegativeTestMatching(
            new String[] {
		"T173mprot1Main.java",
			    "\n" +
			    "public class T173mprot1Main{\n" +
			    "        @SuppressWarnings(\"unused\")\n" +
			    "	public static void main(String[] args) {\n" +
			    "		final Team173mprot1 t = new Team173mprot1();\n" +
			    "		R<@t> r = null;\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team173mprot1.java",
			    "\n" +
			    "public team class Team173mprot1 {\n" +
			    "	protected class R {}\n" +
			    "}\n" +
			    "	\n"
            },
            "1.2.3(b)");
    }

    // a protected role type is mentioned outside the team instance
    // 1.7.3-otjld-mentioning-protected-role-outside-team-2
    public void test173_mentioningProtectedRoleOutsideTeam2() {
        runNegativeTestMatching(
            new String[] {
		"Team173mprot2.java",
			    "\n" +
			    "public team class Team173mprot2 {\n" +
			    "	protected class R {}\n" +
			    "        @SuppressWarnings(\"unused\")\n" +
			    "	public void foo() {\n" +
			    "		final Team173mprot2 t = new Team173mprot2();\n" +
			    "		R<@t> r = null;\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "1.2.3(b)");
    }

    // a protected role type is mentioned outside the team instance
    // 1.7.3-otjld-mentioning-protected-role-outside-team-3
    public void test173_mentioningProtectedRoleOutsideTeam3() {
        runNegativeTestMatching(
            new String[] {
		"Team173mprot3.java",
			    "\n" +
			    "public team class Team173mprot3 {\n" +
			    "	protected class R {}\n" +
			    "	public R getR() { return new R(); }\n" +
			    "	public void foo() {\n" +
			    "		final Team173mprot3 t = new Team173mprot3();\n" +
			    "		R<@t> r = t.getR();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "1.2.2(a)");
    }

    // an object of a confined type is seen by the type IConfined
    // 1.7.4-otjld-viewing-confined-role-by-interface-1
    public void test174_viewingConfinedRoleByInterface1() {
       
       runConformTest(
            new String[] {
		"T174vcrbi1Main.java",
			    "\n" +
			    "public class T174vcrbi1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		final Team174vcrbi1 t = new Team174vcrbi1();\n" +
			    "		IConfined<@t> i = t.getR();\n" +
			    "		t.receiveR(i);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team174vcrbi1.java",
			    "\n" +
			    "public team class Team174vcrbi1 {\n" +
			    "	protected class R extends Confined implements IConfined {\n" +
			    "		protected String getValue() { return \"OK\"; }\n" +
			    "	}\n" +
			    "	public IConfined getR() { \n" +
			    "		return new R(); \n" +
			    "	}\n" +
			    "	public void receiveR(IConfined i) {\n" +
			    "		R r = (R)i;\n" +
			    "		System.out.print(r.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // an object of a confined type is seen by the type IConfined -- passed to wrong team
    // 1.7.4-otjld-viewing-confined-role-by-interface-2
    public void test174_viewingConfinedRoleByInterface2() {
        runNegativeTestMatching(
            new String[] {
		"T174vcrbi2Main.java",
			    "\n" +
			    "public class T174vcrbi2Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		final Team174vcrbi2 t1 = new Team174vcrbi2();\n" +
			    "		IConfined<@t1> i = t1.getR();\n" +
			    "		final Team174vcrbi2 t2 = new Team174vcrbi2();\n" +
			    "		t2.receiveR(i);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team174vcrbi2.java",
			    "\n" +
			    "public team class Team174vcrbi2 {\n" +
			    "	protected class R extends Confined implements IConfined {\n" +
			    "		protected String getValue() { return \"OK\"; }\n" +
			    "	}\n" +
			    "	public IConfined getR() { \n" +
			    "		return new R(); \n" +
			    "	}\n" +
			    "	public void receiveR(IConfined i) {\n" +
			    "		R r = (R)i;\n" +
			    "		System.out.print(r.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "not applicable");
    }

    // an object of a confined type is seen by the type org.obectteams.IConfined -- passed to wrong team
    // 1.7.4-otjld-viewing-confined-role-by-interface-3
    public void test174_viewingConfinedRoleByInterface3() {
       
       runConformTest(
            new String[] {
		"T174vcrbi3Main.java",
			    "\n" +
			    "import org.objectteams.IConfined;\n" +
			    "public class T174vcrbi3Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team174vcrbi3 t1 = new Team174vcrbi3();\n" +
			    "		IConfined i = t1.getR();\n" +
			    "		Team174vcrbi3 t2 = new Team174vcrbi3();\n" +
			    "		t2.receiveR(i);\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team174vcrbi3.java",
			    "\n" +
			    "public team class Team174vcrbi3 {\n" +
			    "	protected class R extends Confined implements org.objectteams.IConfined {\n" +
			    "		protected String getValue() { return \"NOK\"; }\n" +
			    "	}\n" +
			    "	public org.objectteams.IConfined getR() { \n" +
			    "		return new R(); \n" +
			    "	}\n" +
			    "	public void receiveR(org.objectteams.IConfined i) {\n" +
			    "		try {\n" +
			    "			R r = (R)i;\n" +
			    "			System.out.print(r.getValue());\n" +
			    "		} catch (ClassCastException cce) {\n" +
			    "			if (cce instanceof org.objectteams.RoleCastException)\n" +
			    "				System.out.print(\"CAUGHT\");\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "CAUGHT");
    }

    // a team tries to override Team.Confined
    // 1.7.5-otjld-overriding-confined-1
    public void test175_overridingConfined1() {
        runNegativeTestMatching(
            new String[] {
		"Team175oc1.java",
			    "\n" +
			    "public team class Team175oc1 {\n" +
			    "	public class Confined {\n" +
			    "		void foo() {}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "7.2(a)");
    }

    // a team tries to override Team.IConfined
    // 1.7.5-otjld-overriding-confined-2
    public void test175_overridingConfined2() {
        runNegativeTestMatching(
            new String[] {
		"Team175oc2.java",
			    "\n" +
			    "public team class Team175oc2 {\n" +
			    "	public class IConfined {\n" +
			    "		void foo() {}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "1.3.1(c)");
    }

    // a team tries to override Team.Confined - conflict class-ifc
    // 1.7.5-otjld-overriding-confined-3
    public void test175_overridingConfined3() {
        runNegativeTestMatching(
            new String[] {
		"Team175oc3.java",
			    "\n" +
			    "public team class Team175oc3 {\n" +
			    "	public interface Confined {\n" +
			    "		void foo();\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "1.3.1(c)");
    }

    // a team tries to override Team.IConfined - conflict class-ifc
    // 1.7.5-otjld-overriding-confined-4
    public void test175_overridingConfined4() {
        runNegativeTestMatching(
            new String[] {
		"Team175oc4.java",
			    "\n" +
			    "public team class Team175oc4 {\n" +
			    "	public class IConfined {\n" +
			    "		void foo() {}\n" +
			    "	}\n" +
			    "}	\n" +
			    "	\n"
            },
            "1.3.1(c)");
    }

    // a role class tries to override a role interface
    // 1.7.6-otjld-class-overrides-ifc
    public void test176_classOverridesIfc() {
        runNegativeTestMatching(
            new String[] {
		"Team176coi_2.java",
			    "\n" +
			    "public team class Team176coi_2 extends Team176coi_1 {\n" +
			    "	public class I {}\n" +
			    "}\n" +
			    "	\n",
		"Team176coi_1.java",
			    "\n" +
			    "public team class Team176coi_1 {\n" +
			    "	public interface I {\n" +
			    "		void foo();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OTJLD 1.3.1(c)");
    }

    // see also TPX-465
    // 1.7.7-otjld-otjld-example-1
    public void test177_example1() {
       
       runConformTest(
            new String[] {
		"T177oe1Main.java",
			    "\n" +
			    "public class T177oe1Main {\n" +
			    "   public static void main(String[] args)\n" +
			    "   {\n" +
			    "     final Team177oe1 comp = new Team177oe1();\n" +
			    "     IConfined<@comp> emp = comp.getEmployee(\"emp1\");\n" +
			    "     //System.out.println(emp); <-- forbidden!\n" +
			    "     comp.payBonus(emp, 100);\n" +
			    "   }\n" +
			    "}\n" +
			    "    \n",
		"Team177oe1.java",
			    "\n" +
			    "import java.util.HashMap;\n" +
			    "\n" +
			    "@SuppressWarnings({\"rawtypes\",\"unchecked\"})\n" +
			    "public team class Team177oe1 {\n" +
			    "        private HashMap employees;\n" +
			    "\n" +
			    "        protected class Employee implements IConfined\n" +
			    "        {\n" +
			    "                protected void pay(int amount)\n" +
			    "                {\n" +
			    "                        System.out.print(\"Pay: \"+ amount);\n" +
			    "                }\n" +
			    "        }\n" +
			    "\n" +
			    "        public IConfined getEmployee(String ID)\n" +
			    "        {\n" +
			    "                employees = new HashMap();\n" +
			    "                employees.put(ID, new Employee());\n" +
			    "                return (Employee) employees.get(ID); // cast needed because get returns Object\n" +
			    "        }\n" +
			    "\n" +
			    "        \n" +
			    "        public void payBonus(IConfined emp, int amount)\n" +
			    "        {\n" +
			    "                ((Employee) emp).pay(amount);\n" +
			    "        }\n" +
			    "}\n" +
			    "\n" +
			    "    \n"
            },
            "Pay: 100");
    }

    // see also TPX-465 - but using generics
    // 1.7.7-otjld-otjld-example-2
    public void test177_example2() {
       
       runConformTest(
            new String[] {
		"T177oe2Main.java",
			    "\n" +
			    "public class T177oe2Main {\n" +
			    "   public static void main(String[] args)\n" +
			    "   {\n" +
			    "     final Team177oe2 comp = new Team177oe2();\n" +
			    "     IConfined<@comp> emp = comp.getEmployee(\"emp1\");\n" +
			    "     //System.out.println(emp); <-- forbidden!\n" +
			    "     comp.payBonus(emp, 100);\n" +
			    "   }\n" +
			    "}\n" +
			    "    \n",
		"Team177oe2.java",
			    "\n" +
			    "import java.util.HashMap;\n" +
			    "\n" +
			    "public team class Team177oe2 {\n" +
			    "        private HashMap<String, Employee> employees;\n" +
			    "\n" +
			    "        protected class Employee implements IConfined\n" +
			    "        {\n" +
			    "                protected void pay(int amount)\n" +
			    "                {\n" +
			    "                        System.out.print(\"Pay: \"+ amount);\n" +
			    "                }\n" +
			    "        }\n" +
			    "\n" +
			    "        public IConfined getEmployee(String ID)\n" +
			    "        {\n" +
			    "                employees = new HashMap<String, Employee>();\n" +
			    "                employees.put(ID, new Employee());\n" +
			    "                return employees.get(ID); \n" +
			    "        }\n" +
			    "\n" +
			    "        \n" +
			    "        public void payBonus(IConfined emp, int amount)\n" +
			    "        {\n" +
			    "                ((Employee) emp).pay(amount);\n" +
			    "        }\n" +
			    "}\n" +
			    "\n" +
			    "    \n"
            },
            "Pay: 100");
    }

    // see also TPX-465 -- casting null
    // 1.7.7-otjld-otjld-example-3
    public void test177_example3() {
       
       runConformTest(
            new String[] {
		"T177oe3Main.java",
			    "\n" +
			    "public class T177oe3Main {\n" +
			    "   public static void main(String[] args)\n" +
			    "   {\n" +
			    "     final Team177oe3 comp = new Team177oe3();\n" +
			    "     IConfined<@comp> emp = comp.getEmployee(\"emp1\");\n" +
			    "     System.out.print(emp == null ? \"OK\" : \"NOK\");\n" +
			    "   }\n" +
			    "}\n" +
			    "    \n",
		"Team177oe3.java",
			    "\n" +
			    "import java.util.HashMap;\n" +
			    "\n" +
			    "@SuppressWarnings(\"rawtypes\")\n" +
			    "public team class Team177oe3 {\n" +
			    "        private HashMap employees;\n" +
			    "\n" +
			    "        protected class Employee implements IConfined\n" +
			    "        {\n" +
			    "                protected void pay(int amount)\n" +
			    "                {\n" +
			    "                        System.out.print(\"Pay: \"+ amount);\n" +
			    "                }\n" +
			    "        }\n" +
			    "\n" +
			    "        public IConfined getEmployee(String ID)\n" +
			    "        {\n" +
			    "                employees = new HashMap();\n" +
			    "                return (Employee) employees.get(ID); // actually casting null\n" +
			    "        }\n" +
			    "\n" +
			    "        \n" +
			    "        public void payBonus(IConfined emp, int amount)\n" +
			    "        {\n" +
			    "                ((Employee) emp).pay(amount);\n" +
			    "        }\n" +
			    "}\n" +
			    "\n" +
			    "    \n"
            },
            "OK");
    }

    // relevant cast to IConfined
    // 1.7.7-otjld-otjld-example-4
    public void test177_example4() {
       
       runConformTest(
            new String[] {
		"T177oe4Main.java",
			    "\n" +
			    "public class T177oe4Main {\n" +
			    "   public static void main(String[] args)\n" +
			    "   {\n" +
			    "     final Team177oe4 comp = new Team177oe4();\n" +
			    "     Object emp = comp.getEmployee(\"emp1\");\n" +
			    "     IConfined<@comp> confEmp = (IConfined<@comp>)emp; // relevant cast\n" +
			    "     comp.payBonus(confEmp, 100);\n" +
			    "   }\n" +
			    "}\n" +
			    "    \n",
		"Team177oe4.java",
			    "\n" +
			    "import java.util.HashMap;\n" +
			    "\n" +
			    "public team class Team177oe4 {\n" +
			    "        private HashMap<String,BO> employees;\n" +
			    "\n" +
			    "        public class BO {}\n" +
			    "        \n" +
			    "        protected class Employee extends BO implements IConfined // useless since BO is externalizable\n" +
			    "        {\n" +
			    "                protected void pay(int amount)\n" +
			    "                {\n" +
			    "                        System.out.print(\"Pay: \"+ amount);\n" +
			    "                }\n" +
			    "        }\n" +
			    "\n" +
			    "        public Object getEmployee(String ID)\n" +
			    "        {\n" +
			    "                employees = new HashMap<String,BO>();\n" +
			    "                employees.put(ID, new Employee());\n" +
			    "                return employees.get(ID); // BO is conform to Object\n" +
			    "        }\n" +
			    "\n" +
			    "        \n" +
			    "        public void payBonus(IConfined emp, int amount)\n" +
			    "        {\n" +
			    "                ((Employee) emp).pay(amount);\n" +
			    "        }\n" +
			    "}\n" +
			    "\n" +
			    "    \n"
            },
            "Pay: 100");
    }

    // showing the compiler error
    // 1.7.7-otjld-otjld-example-5
    public void test177_example5() {
        runNegativeTest(
            new String[] {
		"T177oe5Main.java",
			    "\n" +
			    "public class T177oe5Main {\n" +
			    "   public static void main(String[] args)\n" +
			    "   {\n" +
			    "     final Team177oe5 comp = new Team177oe5();\n" +
			    "     IConfined<@comp> emp = comp.getEmployee(\"emp1\");\n" +
			    "     System.out.println(emp); //<-- forbidden!\n" +
			    "     comp.payBonus(emp, 100);\n" +
			    "   }\n" +
			    "}\n" +
			    "    \n",
		"Team177oe5.java",
			    "\n" +
			    "import java.util.HashMap;\n" +
			    "\n" +
			    "public team class Team177oe5 {\n" +
			    "        private HashMap<String, Employee> employees;\n" +
			    "\n" +
			    "        protected class Employee implements IConfined\n" +
			    "        {\n" +
			    "                protected void pay(int amount)\n" +
			    "                {\n" +
			    "                        System.out.print(\"Pay: \"+ amount);\n" +
			    "                }\n" +
			    "        }\n" +
			    "\n" +
			    "        public IConfined getEmployee(String ID)\n" +
			    "        {\n" +
			    "                employees = new HashMap<String, Employee>();\n" +
			    "                employees.put(ID, new Employee());\n" +
			    "                return employees.get(ID); \n" +
			    "        }\n" +
			    "\n" +
			    "\n" +
			    "        public void payBonus(IConfined emp, int amount)\n" +
			    "        {\n" +
			    "                ((Employee) emp).pay(amount);\n" +
			    "        }\n" +
			    "}\n" +
			    "\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in T177oe5Main.java (at line 7)\n" + 
    		"	System.out.println(emp); //<-- forbidden!\n" + 
    		"	           ^^^^^^^\n" + 
    		"The method println(boolean) in the type PrintStream is not applicable for the arguments (IConfined<@comp>)\n" + 
    		"----------\n");
    }

    // see TPX-466
    // 1.7.8-otjld-confined-implicit-inheritance-1
    public void test178_confinedImplicitInheritance1() {
       
       runConformTest(
            new String[] {
		"Team178cii1_2.java",
			    "\n" +
			    "public team class Team178cii1_2 extends Team178cii1_1 {\n" +
			    "    Team178cii1_2() {\n" +
			    "        new R();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team178cii1_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team178cii1_1.java",
			    "\n" +
			    "public team class Team178cii1_1 {\n" +
			    "    protected class R extends Confined {\n" +
			    "        protected R() {\n" +
			    "            super();\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // see TPX-467
    // 1.7.8-otjld-confined-implicit-inheritance-2
    public void test178_confinedImplicitInheritance2() {
       
       runConformTest(
            new String[] {
		"Team178cii2_2.java",
			    "\n" +
			    "public team class Team178cii2_2 extends Team178cii2_1 {\n" +
			    "    protected class R extends Confined {\n" +
			    "		  protected R() {}\n" +
			    "    }\n" +
			    "    Team178cii2_2() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team178cii2_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team178cii2_1.java",
			    "\n" +
			    "public team class Team178cii2_1 {\n" +
			    "    protected class R extends Confined {\n" +
			    "        R() {\n" +
			    "            super();\n" +
			    "            System.out.print(\"NOK\"); // bypassed by ctor calling super().\n" +
			    "        }\n" +
			    "        protected void test() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // variation: explicit tsuper
    // 1.7.8-otjld-confined-implicit-inheritance-3
    public void test178_confinedImplicitInheritance3() {
       
       runConformTest(
            new String[] {
		"Team178cii3_2.java",
			    "\n" +
			    "public team class Team178cii3_2 extends Team178cii3_1 {\n" +
			    "    @Override\n" +
			    "    protected class R extends Confined {\n" +
			    "        R(int i) { tsuper(); }\n" +
			    "    }\n" +
			    "    Team178cii3_2() {\n" +
			    "        new R();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team178cii3_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team178cii3_1.java",
			    "\n" +
			    "public team class Team178cii3_1 {\n" +
			    "    protected class R extends Confined {\n" +
			    "        protected R() {\n" +
			    "            super();\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // trying to use baseclass decapsulation for a confined role
    // 1.7.9-otjld-decapsulating-confined-1
    public void test179_decapsulatingConfined1() {
        runNegativeTestMatching(
            new String[] {
		"Team179dc1_2.java",
			    "\n" +
			    "public team class Team179dc1_2 {\n" +
			    "    final Team179dc1_1 other = new Team179dc1_1();\n" +
			    "    public class R playedBy Secret<@other> {}\n" +
			    "}\n" +
			    "    \n",
		"Team179dc1_1.java",
			    "\n" +
			    "public team class Team179dc1_1 {\n" +
			    "    protected class Secret extends Confined {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(c)");
    }

    // trying to use baseclass decapsulation for a confined role , anchored to base
    // 1.7.9-otjld-decapsulating-confined-2
    public void test179_decapsulatingConfined2() {
        runNegativeTestMatching(
            new String[] {
		"Team179dc2_2.java",
			    "\n" +
			    "public team class Team179dc2_2 {\n" +
			    "    public team class Inner playedBy Team179dc2_1 {\n" +
			    "        public class R playedBy Secret<@base> {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team179dc2_1.java",
			    "\n" +
			    "public team class Team179dc2_1 {\n" +
			    "    protected class Secret extends Confined {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.1.2(c)");
    }

    // a bound role is confined
    // 1.7.10-otjld-confined-bound-role-1
    public void test1710_confinedBoundRole1() {
       
       runConformTest(
            new String[] {
		"T1710cbr1Main.java",
			    "\n" +
			    "public class T1710cbr1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T1710cbr1 object = new T1710cbr1(\"Agent\");\n" +
			    "        Team1710cbr1 safe = new Team1710cbr1();\n" +
			    "        safe.createCredentials(object);\n" +
			    "        if (safe.checkCredentials(object))\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1710cbr1.java",
			    "\n" +
			    "public class T1710cbr1 {\n" +
			    "    String name;\n" +
			    "    T1710cbr1(String name) {\n" +
			    "        this.name = name;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1710cbr1.java",
			    "\n" +
			    "public team class Team1710cbr1 {\n" +
			    "    protected class Credentials extends Confined playedBy T1710cbr1 {\n" +
			    "        String name() -> get String name;\n" +
			    "        String pw;\n" +
			    "        protected void setCredentials() {\n" +
			    "            this.pw = \"secret\"; // should use secure keyboard input\n" +
			    "        }\n" +
			    "        protected boolean checkCredentials() {\n" +
			    "            String given = \"secret\"; // should use secure keyboard input\n" +
			    "            System.out.print(\"Checking \"+this.name()+\": \");\n" +
			    "            return pw.equals(given);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void createCredentials(T1710cbr1 as Credentials object) {\n" +
			    "        object.setCredentials();\n" +
			    "    }\n" +
			    "    public boolean checkCredentials(T1710cbr1 as Credentials object) {\n" +
			    "        return object.checkCredentials();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Checking Agent: OK");
    }

    // a bound role is confined, base constructor used
    public void test1710_confinedBoundRole2() {
       
       runConformTest(
            new String[] {
		"T1710cbr2Main.java",
			    "\n" +
			    "public class T1710cbr2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team1710cbr2 safe = new Team1710cbr2();\n" +
			    "        T1710cbr2 object = safe.getT1710cbr2(\"Agent\");\n" +
			    "        if (safe.checkCredentials(object))\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        else\n" +
			    "            System.out.print(\"NOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1710cbr2.java",
			    "\n" +
			    "public class T1710cbr2 {\n" +
			    "    String name;\n" +
			    "    T1710cbr2(String name) {\n" +
			    "        this.name = name;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1710cbr2.java",
			    "\n" +
			    "public team class Team1710cbr2 {\n" +
			    "    protected class Credentials extends Confined playedBy T1710cbr2 {\n" +
			    "		 protected Credentials(String name) {\n" +
			    "			 base(name);\n" +
			    "		 }\n" +
			    "        String name() -> get String name;\n" +
			    "        String pw;\n" +
			    "        protected void setCredentials() {\n" +
			    "            this.pw = \"secret\"; // should use secure keyboard input\n" +
			    "        }\n" +
			    "        protected boolean checkCredentials() {\n" +
			    "            String given = \"secret\"; // should use secure keyboard input\n" +
			    "            System.out.print(\"Checking \"+this.name()+\": \");\n" +
			    "            return pw.equals(given);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void createCredentials(T1710cbr2 as Credentials object) {\n" +
			    "        object.setCredentials();\n" +
			    "    }\n" +
			    "    public boolean checkCredentials(T1710cbr2 as Credentials object) {\n" +
			    "        return object.checkCredentials();\n" +
			    "    }\n" +
			    "    public T1710cbr2 getT1710cbr2(String name) {\n" +
			    "		 Credentials object = new Credentials(name);" +
			    "		 object.setCredentials();\n" +
			    "		 return object;" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Checking Agent: OK");
    }

    // a bound role is confined, callin defined
    // see https://bugs.eclipse.org/370271 [compiler] NPE in PredicateGenerator with no predicate present
    public void test1710_confinedBoundRole3() {
       
       runConformTest(
            new String[] {
		"T1710cbr3Main.java",
			    "\n" +
			    "public class T1710cbr3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team1710cbr3 safe = new Team1710cbr3();\n" +
			    "        safe.activate();\n" +
			    "        T1710cbr3 object = new T1710cbr3(\"secret\");\n" +
			    "        object.print();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1710cbr3.java",
			    "\n" +
			    "public class T1710cbr3 {\n" +
			    "    String name;\n" +
			    "    T1710cbr3(String name) {\n" +
			    "        this.name = name;\n" +
			    "    }\n" +
			    "    void print() {\n" +
			    "        System.out.print(this.name);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1710cbr3.java",
			    "\n" +
			    "public team class Team1710cbr3 {\n" +
			    "    protected class Guard extends Confined playedBy T1710cbr3 {\n" +
			    "        callin void forbid() {\n" +
			    "            System.out.print(\"forbidden\");\n" +
			    "        }\n" +
			    "        forbid <- replace print;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "forbidden");
    }

    // an array of confined is not(?) compatible
    // 1.7.11-otjld-array-of-confined-1
    public void test1711_arrayOfConfined1() {
        runNegativeTestMatching(
            new String[] {
		"Team1711aoc1.java",
			    "\n" +
			    "public team class Team1711aoc1 {\n" +
			    "    protected class Secret extends Confined { }\n" +
			    "    void test() {\n" +
			    "        Secret[] treasure= new Secret[] { new Secret() };\n" +
			    "        Object giveaway= treasure;\n" +
			    "        Object[] openarray= (Object[])giveaway;\n" +
			    "        System.out.print(openarray[0]);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "7.2(b)");
    }

    // an array of confined is not compatible
    // 1.7.11-otjld-array-of-confined-2
    public void test1711_arrayOfConfined2() {
        runNegativeTestMatching(
            new String[] {
		"Team1711aoc2.java",
			    "\n" +
			    "public team class Team1711aoc2 {\n" +
			    "    protected class Secret extends Confined { }\n" +
			    "    void test() {\n" +
			    "        Secret[] treasure= new Secret[] { new Secret() };\n" +
			    "        Object[] giveaways= treasure;\n" +
			    "        System.out.println(giveaways[0]);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Type mismatch");
    }

    // a role explicitly extends confined, using the super type
    // 1.7.12-otjld-explicit-inheritance-of-confined-1
    public void test1712_explicitInheritanceOfConfined1() {
       
       runConformTest(
            new String[] {
		"Team1712eioc1.java",
			    "\n" +
			    "public team class Team1712eioc1 {\n" +
			    "    protected class R extends Confined {\n" +
			    "	protected void sayOK () {\n" +
			    "	    System.out.print(\"OK\");\n" +
			    "	}\n" +
			    "    }\n" +
			    "    Confined getConfined() {\n" +
			    "	return new R();\n" +
			    "    }\n" +
			    "    void testConfined(Confined c) {\n" +
			    "	R r = (R)c;\n" +
			    "	r.sayOK();\n" +
			    "    }\n" +
			    "    Team1712eioc1() {\n" +
			    "	Confined c = getConfined();\n" +
			    "	testConfined(c);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "	new Team1712eioc1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }
}
