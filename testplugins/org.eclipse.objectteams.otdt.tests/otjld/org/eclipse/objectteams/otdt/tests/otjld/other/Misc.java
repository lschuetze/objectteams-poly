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
 *        Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.other;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.jdt.core.tests.util.Util;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

@SuppressWarnings("unchecked")
public class Misc extends AbstractOTJLDTest {

     public Misc(String name) {
             super(name);
     }

     // Static initializer to specify tests subset using TESTS_* static variables
     // All specified tests which does not belong to the class are skipped...
     static {
//        TESTS_NAMES = new String[] { "test04m_javadocBaseImportReference1"};
//        TESTS_NUMBERS = new int { 1459 };
//        TESTS_RANGE = new int { 1097, -1 };
     }

     public static Test suite() {
         return buildComparableTestSuite(testClass());
     }

     public static Class testClass() {
         return Misc.class;
     }

     // a non-abstract team tries to instantiate an abstract role
     // 0.m.1-otjld-abstract-relevant-role-instantiated-1
     public void test0m1_abstractRelevantRoleInstantiated1() {
         runNegativeTestMatching(
             new String[] {
 		"Team0m1arri1.java",
			    "\n" +
			    "public team class Team0m1arri1 {\n" +
			    "	abstract protected class Role {\n" +
			    "	}\n" +
			    "	void foo() {\n" +
			    "		Role r = new Role();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
             },
             "2.5(b)");
     }

     // an abstract team instantiates an abstract role
     // 0.m.1-otjld-abstract-relevant-role-instantiated-2
     public void test0m1_abstractRelevantRoleInstantiated2() {
        
        runConformTest(
             new String[] {
 		"Team0m1arri2_2.java",
			    "\n" +
			    "public team class Team0m1arri2_2 extends Team0m1arri2_1 {\n" +
			    "	protected class Role {\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		System.out.print((new Team0m1arri2_2()).foo());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0m1arri2_1.java",
			    "\n" +
			    "public abstract team class Team0m1arri2_1 {\n" +
			    "	abstract protected class Role {\n" +
			    "		public String getValue() { return \"OK\"; }\n" +
			    "	}\n" +
			    "	String foo() {\n" +
			    "		Role r = new Role();\n" +
			    "		return r.getValue();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
             },
             "OK");
     }

     // an abstract team instantiates an abstract role
     // 0.m.1-otjld-abstract-relevant-role-instantiated-3
     public void test0m1_abstractRelevantRoleInstantiated3() {
        
        runConformTest(
             new String[] {
 		"Team0m1arri3_2.java",
			    "\n" +
			    "public team class Team0m1arri3_2 extends Team0m1arri3_1 {\n" +
			    "	public class Role {\n" +
			    "	}\n" +
			    "	public static void main(String[] args) {\n" +
			    "		final Team0m1arri3_1 t = new Team0m1arri3_2();\n" +
			    "		Role<@t> r = t.new Role();\n" +
			    "		System.out.print(r.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0m1arri3_1.java",
			    "\n" +
			    "public abstract team class Team0m1arri3_1 {\n" +
			    "	public abstract class Role {\n" +
			    "		public String getValue() { return \"OK\"; }\n" +
			    "	}\n" +
			    "	String foo() {\n" +
			    "		Role r = new Role();\n" +
			    "		return r.getValue();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
             },
             "OK");
     }

     // a non-abstract team instantiates an abstract role as externalized
     // 0.m.1-otjld-abstract-relevant-role-instantiated-4
     public void test0m1_abstractRelevantRoleInstantiated4() {
         runNegativeTestMatching(
             new String[] {
 		"T0m1arri4Main.java",
			    "\n" +
			    "public class T0m1arri4Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		final Team0m1arri4 t = new Team0m1arri4();\n" +
			    "		Role<@t> r = t.new Role();\n" +
			    "		System.out.print(r.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team0m1arri4.java",
			    "\n" +
			    "public team class Team0m1arri4 {\n" +
			    "	public abstract class Role {\n" +
			    "		public String getValue() { return \"OK\"; }\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
             },
             "OTJLD 2.5(b)");
     }

     // 
     // 0.m.2-otjld-string-constant-limit-1
     public void test0m2_stringConstantLimit1() {
        
        runConformTest(
             new String[] {
 		"Team0m2scl1_2.java",
			    "\n" +
			    "public team class Team0m2scl1_2 extends Team0m2scl1_1 {\n" +
			    "    Team0m2scl1_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        new Team0m2scl1_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team0m2scl1_1.java",
			    "\n" +
			    "public team class Team0m2scl1_1 {\n" +
			    "    protected class R {\n" +
			    "        String a=\"a\", b=\"b\", c=\"c\";\n" +
			    "        String d1=\"d1\", e1=\"e1\", f1=\"f1\", g1=\"g1\", h1=\"h1\", i1=\"i1\", j1=\"j1\", k1=\"k1\";\n" +
			    "        String d2=\"d2\", e2=\"e2\", f2=\"f2\", g2=\"g2\", h2=\"h2\", i2=\"i2\", j2=\"j2\", k2=\"k2\";\n" +
			    "        String d3=\"d3\", e3=\"e3\", f3=\"f3\", g3=\"g3\", h3=\"h3\", i3=\"i3\", j3=\"j3\", k3=\"k3\";\n" +
			    "        String d4=\"d4\", e4=\"e4\", f4=\"f4\", g4=\"g4\", h4=\"h4\", i4=\"i4\", j4=\"j4\", k4=\"k4\";\n" +
			    "        String d5=\"d5\", e5=\"e5\", f5=\"f5\", g5=\"g5\", h5=\"h5\", i5=\"i5\", j5=\"j5\", k5=\"k5\";\n" +
			    "        String d6=\"d6\", e6=\"e6\", f6=\"f6\", g6=\"g6\", h6=\"h6\", i6=\"i6\", j6=\"j6\", k6=\"k6\";\n" +
			    "        String d7=\"d7\", e7=\"e7\", f7=\"f7\", g7=\"g7\", h7=\"h7\", i7=\"i7\", j7=\"j7\", k7=\"k7\";\n" +
			    "        String ok=\"OK\";\n" +
			    "        protected void test() { System.out.print(ok); }\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
             },
             "OK");
     }

     // an integer constant must remain within the range of short constant pool indices - read from bytecode
     // 0.m.2-otjld-string-constant-limit-2
     public void test0m2_stringConstantLimit2() {
        
        runConformTest(
             new String[] {
 		"Team0m2scl2_2.java",
			    "\n" +
			    "public team class Team0m2scl2_2 extends Team0m2scl2_1 {\n" +
			    "    protected class R {\n" +
			    "        String d1=\"d1\", e1=\"e1\", f1=\"f1\", g1=\"g1\", h1=\"h1\", i1=\"i1\", j1=\"j1\", k1=\"k1\";\n" +
			    "        String d2=\"d2\", e2=\"e2\", f2=\"f2\", g2=\"g2\", h2=\"h2\", i2=\"i2\", j2=\"j2\", k2=\"k2\";\n" +
			    "        String d3=\"d3\", e3=\"e3\", f3=\"f3\", g3=\"g3\", h3=\"h3\", i3=\"i3\", j3=\"j3\", k3=\"k3\";\n" +
			    "        String d4=\"d4\", e4=\"e4\", f4=\"f4\", g4=\"g4\", h4=\"h4\", i4=\"i4\", j4=\"j4\", k4=\"k4\";\n" +
			    "        String d5=\"d5\", e5=\"e5\", f5=\"f5\", g5=\"g5\", h5=\"h5\", i5=\"i5\", j5=\"j5\", k5=\"k5\";\n" +
			    "        String d6=\"d6\", e6=\"e6\", f6=\"f6\", g6=\"g6\", h6=\"h6\", i6=\"i6\", j6=\"j6\", k6=\"k6\";\n" +
			    "        String d7=\"d7\", e7=\"e7\", f7=\"f7\", g7=\"g7\", h7=\"h7\", i7=\"i7\", j7=\"j7\", k7=\"k7\";\n" +
			    "    }\n" +
			    "    Team0m2scl2_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        new Team0m2scl2_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team0m2scl2_1.java",
			    "\n" +
			    "public team class Team0m2scl2_1 {\n" +
			    "    protected class R {\n" +
			    "        String a=\"a\", b=\"b\", c=\"c\";\n" +
			    "        String ok=\"OK\";\n" +
			    "	int much = 67000;\n" +
			    "        protected void test() { System.out.print(ok); System.out.print(much); }\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
             },
             "OK67000");
     }

     // an integer constant must remain within the range of short constant pool indices - compile in one go (RoleModel lost its AST, still enable copying)
     // 0.m.2-otjld-string-constant-limit-3
     public void test0m2_stringConstantLimit3() {
        
        runConformTest(
             new String[] {
 		"Team0m2scl3_2.java",
			    "\n" +
			    "public team class Team0m2scl3_2 extends Team0m2scl3_1 {\n" +
			    "    protected class R {\n" +
			    "        String d1=\"d1\", e1=\"e1\", f1=\"f1\", g1=\"g1\", h1=\"h1\", i1=\"i1\", j1=\"j1\", k1=\"k1\";\n" +
			    "        String d2=\"d2\", e2=\"e2\", f2=\"f2\", g2=\"g2\", h2=\"h2\", i2=\"i2\", j2=\"j2\", k2=\"k2\";\n" +
			    "        String d3=\"d3\", e3=\"e3\", f3=\"f3\", g3=\"g3\", h3=\"h3\", i3=\"i3\", j3=\"j3\", k3=\"k3\";\n" +
			    "        String d4=\"d4\", e4=\"e4\", f4=\"f4\", g4=\"g4\", h4=\"h4\", i4=\"i4\", j4=\"j4\", k4=\"k4\";\n" +
			    "        String d5=\"d5\", e5=\"e5\", f5=\"f5\", g5=\"g5\", h5=\"h5\", i5=\"i5\", j5=\"j5\", k5=\"k5\";\n" +
			    "        String d6=\"d6\", e6=\"e6\", f6=\"f6\", g6=\"g6\", h6=\"h6\", i6=\"i6\", j6=\"j6\", k6=\"k6\";\n" +
			    "        String d7=\"d7\", e7=\"e7\", f7=\"f7\", g7=\"g7\", h7=\"h7\", i7=\"i7\", j7=\"j7\", k7=\"k7\";\n" +
			    "    }\n" +
			    "    Team0m2scl3_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        new Team0m2scl3_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team0m2scl3_1.java",
			    "\n" +
			    "public team class Team0m2scl3_1 {\n" +
			    "    protected class R {\n" +
			    "        String a=\"a\", b=\"b\", c=\"c\";\n" +
			    "        String ok=\"OK\";\n" +
			    "	int much = 67000;\n" +
			    "        protected void test() { System.out.print(ok); System.out.print(much); }\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
             },
             "OK67000");
     }

     // a long constant must remain within the range of short constant pool indices - read from bytecode
     // 0.m.2-otjld-string-constant-limit-4
     public void test0m2_stringConstantLimit4() {
        
        runConformTest(
             new String[] {
 		"Team0m2scl4_2.java",
			    "\n" +
			    "public team class Team0m2scl4_2 extends Team0m2scl4_1 {\n" +
			    "    protected class R {\n" +
			    "        String d1=\"d1\", e1=\"e1\", f1=\"f1\", g1=\"g1\", h1=\"h1\", i1=\"i1\", j1=\"j1\", k1=\"k1\";\n" +
			    "        String d2=\"d2\", e2=\"e2\", f2=\"f2\", g2=\"g2\", h2=\"h2\", i2=\"i2\", j2=\"j2\", k2=\"k2\";\n" +
			    "        String d3=\"d3\", e3=\"e3\", f3=\"f3\", g3=\"g3\", h3=\"h3\", i3=\"i3\", j3=\"j3\", k3=\"k3\";\n" +
			    "        String d4=\"d4\", e4=\"e4\", f4=\"f4\", g4=\"g4\", h4=\"h4\", i4=\"i4\", j4=\"j4\", k4=\"k4\";\n" +
			    "        String d5=\"d5\", e5=\"e5\", f5=\"f5\", g5=\"g5\", h5=\"h5\", i5=\"i5\", j5=\"j5\", k5=\"k5\";\n" +
			    "        String d6=\"d6\", e6=\"e6\", f6=\"f6\", g6=\"g6\", h6=\"h6\", i6=\"i6\", j6=\"j6\", k6=\"k6\";\n" +
			    "        String d7=\"d7\", e7=\"e7\", f7=\"f7\", g7=\"g7\", h7=\"h7\", i7=\"i7\", j7=\"j7\", k7=\"k7\";\n" +
			    "    }\n" +
			    "    Team0m2scl4_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        new Team0m2scl4_2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team0m2scl4_1.java",
			    "\n" +
			    "public team class Team0m2scl4_1 {\n" +
			    "    protected class R {\n" +
			    "        String a=\"a\", b=\"b\", c=\"c\";\n" +
			    "        String ok=\"OK\";\n" +
			    "	long much = 67000000;\n" +
			    "        protected void test() { System.out.print(ok); System.out.print(much); }\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
             },
             "OK67000000");
     }

     // a float constant must remain within the range of short constant pool indices - compile in one go
     // 0.m.2-otjld-string-constant-limit-5
     public void test0m2_stringConstantLimit5() {
        
        runConformTest(
             new String[] {
 		"Team0m2scl5_2.java",
			    "\n" +
			    "public team class Team0m2scl5_2 extends Team0m2scl5_1 {\n" +
			    "    protected class R {\n" +
			    "        String d1=\"d1\", e1=\"e1\", f1=\"f1\", g1=\"g1\", h1=\"h1\", i1=\"i1\", j1=\"j1\", k1=\"k1\";\n" +
			    "        String d2=\"d2\", e2=\"e2\", f2=\"f2\", g2=\"g2\", h2=\"h2\", i2=\"i2\", j2=\"j2\", k2=\"k2\";\n" +
			    "        String d3=\"d3\", e3=\"e3\", f3=\"f3\", g3=\"g3\", h3=\"h3\", i3=\"i3\", j3=\"j3\", k3=\"k3\";\n" +
			    "        String d4=\"d4\", e4=\"e4\", f4=\"f4\", g4=\"g4\", h4=\"h4\", i4=\"i4\", j4=\"j4\", k4=\"k4\";\n" +
			    "        String d5=\"d5\", e5=\"e5\", f5=\"f5\", g5=\"g5\", h5=\"h5\", i5=\"i5\", j5=\"j5\", k5=\"k5\";\n" +
			    "        String d6=\"d6\", e6=\"e6\", f6=\"f6\", g6=\"g6\", h6=\"h6\", i6=\"i6\", j6=\"j6\", k6=\"k6\";\n" +
			    "        String d7=\"d7\", e7=\"e7\", f7=\"f7\", g7=\"g7\", h7=\"h7\", i7=\"i7\", j7=\"j7\", k7=\"k7\";\n" +
			    "    }\n" +
			    "    Team0m2scl5_2() {\n" +
			    "        R r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        new Team0m2scl5_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team0m2scl5_1.java",
			    "\n" +
			    "public team class Team0m2scl5_1 {\n" +
			    "    protected class R {\n" +
			    "        String a=\"a\", b=\"b\", c=\"c\";\n" +
			    "        String ok=\"OK\";\n" +
			    "        float much = 6.7f;\n" +
			    "        protected void test() { System.out.print(ok); System.out.print(much); }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
             },
             "OK6.7");
     }

     // 
     // 0.m.3-otjld-mismatching-filename-1
     public void test0m3_mismatchingFilename1() {
         runNegativeTestMatching(
             new String[] {
 		"Team0m3mf1.java",
			    "\n" +
			    "public team class WrongName {\n" +
			    "    protected class R {}\n" +
			    "}    \n" +
			    "    \n"
             },
             "in its own");
     }

     // WITNESS for TPX-280
     // 0.m.4-otjld-private-toplevel-class-1
     public void test0m4_privateToplevelClass1() {
         runNegativeTestMatching(
             new String[] {
 		"T0m4ptc1_1.java",
			    "\n" +
			    "public class T0m4ptc1_1 {}\n" +
			    "private class T0m4ptc1_2 {}    \n" +
			    "    \n"
             },
             "Illegal modifier");
     }

     // javadoc references uses base import
     public void test04m_javadocBaseImportReference1() {
    	 Map customOptions = getCompilerOptions();
         customOptions.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
    	 customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.ERROR);
    	 customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadocTags, CompilerOptions.ENABLED);
    	 customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadocTagsVisibility, CompilerOptions.PROTECTED);
    	 runConformTest(new String[] {
    	 "pteam/Team0m4jbir1.java",
    	 		"package pteam;\n" +
    	 		"import base pbase.T0m4jbir1;\n" +
    	 		"public team class Team0m4jbir1 {\n" +
    	 		"   /** Role for {@link T0m4jbir1} as its base.\n" +
    	 		"     * @see T0m4jbir1\n" +
    	 		"     */\n" +
    	 		"	protected class R playedBy T0m4jbir1 {} \n" +
    	 		"}\n",
    	 "pbase/T0m4jbir1.java",
    	 		"package pbase;\n" +
    	 		"public class T0m4jbir1 {}\n"
    	 	},
    	 	"",
    	 	null/*classLibraries*/,
    	 	false/*shouldFlushOutputDirectory*/,
    	 	null/*vmArguments*/,
    	 	customOptions,
    	 	null/*requestor*/);
     }
     
     String[] getClassLibraries() {
 		if (this.verifier != null)
 			this.verifier.shutDown();
         this.verifier = getTestVerifier(false);
         this.createdVerifier = true;

     	String[] jarFilenames = {"bug370040_prj14.jar", "bug370040_prj15.jar"};
     	String destPath = this.outputRootDirectoryPath+"/regression";
     	createOutputTestDirectory("/regression");
     	// upload the jars:
 		Util.copy(getTestResourcePath(jarFilenames[0]), destPath);
 		Util.copy(getTestResourcePath(jarFilenames[1]), destPath);
     	// setup classpath:
     	String[] classPaths = getDefaultClassPaths();
     	int l = classPaths.length;
     	System.arraycopy(classPaths, 0, classPaths=new String[l+2], 0, l);
 		classPaths[l] = this.outputRootDirectoryPath+"/regression/"+jarFilenames[0];
 		classPaths[l+1] = this.outputRootDirectoryPath+"/regression/"+jarFilenames[1];
 		return classPaths;
     }

     // Bug 370040 - [otre] NoSuchFieldError when mixing class file versions within one type hierarchy
     public void testMixedClassFileFormats1() {
    	 runConformTest(
    		new String[] {
    			"potj/Main.java",
    			"package potj;\n" + 
    			"import p4.SubSubBase;\n" + 
    			"public class Main {\n" + 
    			"	public static void main(String[] args) {\n" + 
    			"		new SubTeam().activate();\n" + 
    			"		new Team3().activate();\n" + 
    			"		new SubSubBase().foo();\n" + 
    			"	}\n" + 
    			"}\n",
    			"potj/SuperTeam.java",
    			"package potj;\n" + 
    			"\n" + 
    			"import base p1.AbstractSuperBase;\n" + 
    			"public team class SuperTeam {\n" + 
    			"	protected class R0 playedBy AbstractSuperBase {\n" + // weaving into a 1.5 class file (using ldc for class literal)
    			"		ci <- replace foo;\n" + 
    			"\n" + 
    			"		callin void ci() {\n" + 
    			"			System.out.println(\"SuperTeam$R.ci()\");\n" + 
    			"			base.ci();\n" + 
    			"		}\n" + 
    			"	}\n" + 
    			"}",
    			"potj/SubTeam.java",
    			"package potj;\n" + 
    			"\n" + 
    			"import base p4.SubBase;\n" + 
    			"public team class SubTeam extends SuperTeam {\n" + 
    			"	protected class R1 extends R0 playedBy SubBase {\n" + // weaving into a 1.4 class file (needs manual management using Class.forName())
    			"		\n" + 
    			"	}\n" + 
    			"}\n",
    			"potj/Team3.java",
    			"package potj;\n" + 
    			"\n" + 
    			"import base p4.SubSubBase;\n" + 
    			"public team class Team3 {\n" + 
    			"	protected class R3 playedBy SubSubBase {\n" + 
    			"		rm <-after foo;\n" + 
    			"\n" + 
    			"		private void rm() {\n" + 
    			"			System.out.println(\"R3.rm\");\n" + 
    			"		}\n" + 
    			"	}\n" + 
    			"}\n",
    		},
    		"SuperTeam$R.ci()\n" + 
			"SuperBase.foo()\n" + 
			"R3.rm",
            getClassLibraries(),
            false/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/);
     }
     
     // Bug 304728 - [otre] [compiler] Support basic serialization of teams and roles
     // Bug 304729 - [otre] Selectively consider activation state during team serialization
     public void _testTeamSerialization1() {
    	 runConformTest(
    		new String[] {
    			"TeamSerializationMain.java",
    				"import java.io.ByteArrayInputStream;\n" + 
    				"import java.io.ByteArrayOutputStream;\n" + 
    				"import java.io.IOException;\n" + 
    				"import java.io.ObjectInputStream;\n" + 
    				"import java.io.ObjectOutputStream;\n" + 
    				"\n" + 
    				"import org.objectteams.Team;\n" + 
    				"\n" + 
    				"import teampack.PersistentTeam;\n" + 
    				"import basepack.PersistentBase;\n" + 
    				"\n" + 
    				"\n" + 
    				"public class TeamSerializationMain {\n" + 
    				"	public static void main(String[] args) throws IOException, ClassNotFoundException {\n" + 
    				"		PersistentTeam t = new PersistentTeam();\n" + 
    				"		t.activate(Team.ALL_THREADS);\n" + 
    				"		PersistentBase basel = new PersistentBase(\"b1\");\n" + 
    				"		t.name(basel, \"R1\");\n" + 
    				"		basel.hello();\n" + 
    				"\n" + 
    				"		// store, deactivate and forget it:\n" + 
    				"		ByteArrayOutputStream baos = new ByteArrayOutputStream();\n" + 
    				"		ObjectOutputStream oos = new ObjectOutputStream(baos);\n" + 
    				"		oos.writeObject(t);\n" + 
    				"		t.deactivate(Team.ALL_THREADS);\n" + 
    				"		t = null;\n" + 
    				"		basel = null;\n" + 
    				"		\n" + 
    				"		// no team active now:\n" + 
    				"		new PersistentBase(\"interm\").hello();\n" + 
    				"\n" + 
    				"		// restore:\n" + 
    				"		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));\n" + 
    				"		Object o = ois.readObject();\n" + 
    				"		basel = (PersistentBase) ((PersistentTeam)o).getBase();\n" + 
    				"		\n" + 
    				"		// trigger from old and new objects:\n" + 
    				"		basel.hello();\n" + 
    				"		new PersistentBase(\"finale\").hello();\n" + 
    				"	}\n" + 
    				"}\n",
    			"teampack/PersistentTeam.java",
    				"package teampack;\n" + 
    				"\n" + 
    				"import java.io.IOException;\n" + 
    				"import java.io.Serializable;\n" + 
    				"\n" + 
    				"import base basepack.PersistentBase;\n" + 
    				"\n" + 
    				"public team class PersistentTeam implements Serializable {\n" + 
    				"\n" + 
    				"	private void writeObject(java.io.ObjectOutputStream out)\n" + 
    				"            throws IOException\n" + 
    				"    {\n" + 
    				"        out.defaultWriteObject();\n" + 
    				"        writeGlobalActivationState(out);\n" + 
    				"        Object[] allRoles = getAllRoles();\n" + 
    				"        out.writeInt(allRoles.length);\n" + 
    				"		 for (Object o : allRoles)\n" + 
    				"        	out.writeObject(o);\n" + 
    				"    }\n" + 
    				"    private void readObject(java.io.ObjectInputStream in)\n" + 
    				"            throws IOException, ClassNotFoundException\n" + 
    				"    {\n" + 
    				"        in.defaultReadObject();\n" + 
    				"        readGlobalActivationState(in);\n" + 
    				"        restore();\n" + 
    				"        int numRoles = in.readInt();\n" + 
    				"        for (int i = 0; i < numRoles; i++)\n" + 
    				"			restoreRole(R.class, in.readObject());\n" + 
    				"    }\n" + 
    				"    \n" + 
    				"    protected class R implements Serializable, ILowerable playedBy PersistentBase {\n" + 
    				"    	protected String roleName;\n" + 
    				"\n" + 
    				"		void hello() <- after void hello();\n" + 
    				"    	\n" + 
    				"		private void hello() {\n" + 
    				"			System.out.println(\"Says \"+this.roleName);\n" + 
    				"		}    	\n" + 
    				"    }\n" + 
    				"\n" + 
    				"	public void name(PersistentBase as R r, String name) {\n" + 
    				"		r.roleName = name;\n" + 
    				"	}\n" + 
    				"	\n" + 
    				"	public Object getBase() {\n" + 
    				"		R[] rs = getAllRoles(R.class);\n" + 
    				"		return rs[0].lower();\n" + 
    				"	}\n" + 
    				"}\n",
    			"packbase/PersistentBase.java",
	    			"package basepack;\n" + 
	    			"\n" + 
	    			"import java.io.Serializable;\n" + 
	    			"\n" + 
	    			"public class PersistentBase implements Serializable {\n" + 
	    			"	String name;\n" + 
	    			"\n" + 
	    			"	public PersistentBase(String name) {\n" + 
	    			"		this.name = name;\n" + 
	    			"	}\n" + 
	    			"\n" + 
	    			"	public void hello() {\n" + 
	    			"		System.out.println(\"Hello \"+this.name);\n" + 
	    			"	}\n" + 
	    			"}\n"
    		}, 
    		"Hello b1\n" + 
			"Says R1\n" + 
			"Hello interm\n" + 
			"Hello b1\n" + 
			"Says R1\n" + 
			"Hello finale\n" + 
			"Says null");
     }
}

