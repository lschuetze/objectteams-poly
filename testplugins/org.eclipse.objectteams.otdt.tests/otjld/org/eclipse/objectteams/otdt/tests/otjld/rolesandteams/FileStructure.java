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
package org.eclipse.objectteams.otdt.tests.otjld.rolesandteams;


import java.util.Map;

import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

/**
 * Former jacks tests from section file-structure (1.5.*)
 * @author stephan 
 */
@SuppressWarnings("unchecked")
public class FileStructure extends AbstractOTJLDTest {

	public FileStructure(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test1526_rofiTeamExtendsNonTeam1" };
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return FileStructure.class;
	}

	// this suite depends on finding source files on disk to fetch required types in to the compilation
	@Override
	protected void compileTestFiles(Compiler batchCompiler, String[] testFiles) {
		myWriteFiles(testFiles);
		super.compileTestFiles(batchCompiler, testFiles);
	}

	// an externally stored role class uses a feature of its team class
    // 1.5.1-otjld-access-from-external-role
    public void test151_accessFromExternalRole() {
       
       runConformTest(
            new String[] {
		"T151aferMain.java",
			    "\n" +
			    "public class T151aferMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team151afer t = new Team151afer();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team151afer.java",
			    "\n" +
			    "public team class Team151afer {\n" +
			    "\n" +
			    "    private String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role151afer().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team151afer/Role151afer.java",
			    "\n" +
			    "team package Team151afer;\n" +
			    "\n" +
			    "protected class Role151afer {\n" +
			    "    public String getValue() {\n" +
			    "        return Team151afer.this.getValueInternal();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an externally stored role class uses a 'friendly' feature of a class in the same package as its team class
    // 1.5.2-otjld-access-from-external-role-1
    public void test152_accessFromExternalRole1() {
       
       runConformTest(
            new String[] {
		"T152afer1Main.java",
			    "\n" +
			    "public class T152afer1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team152afer1 t = new Team152afer1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T152afer1.java",
			    "\n" +
			    "public class T152afer1 {\n" +
			    "    static String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team152afer1.java",
			    "\n" +
			    "public team class Team152afer1 {\n" +
			    "    public String getValue() {\n" +
			    "        return new Role152afer1().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team152afer1/Role152afer1.java",
			    "\n" +
			    "team package Team152afer1;\n" +
			    "\n" +
			    "protected class Role152afer1 {\n" +
			    "    public String getValue() {\n" +
			    "        return T152afer1.getValueInternal();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an externally stored role class uses a 'friendly' feature of a class in the same package as its team class - different compile order
    // 1.5.2-otjld-access-from-external-role-2
    public void test152_accessFromExternalRole2() {
       this.compileOrder = new String[][] {{"T152afer2.java"},{"Team152afer2.java"},{"Team152afer2/Role152afer2.java"},{"T152afer2Main.java"}};
       runConformTest(
            new String[] {
		"T152afer2Main.java",
			    "\n" +
			    "public class T152afer2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team152afer2 t = new Team152afer2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T152afer2.java",
			    "\n" +
			    "public class T152afer2 {\n" +
			    "    static String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team152afer2/Role152afer2.java",
			    "\n" +
			    "team package Team152afer2;\n" +
			    "\n" +
			    "protected class Role152afer2 {\n" +
			    "    public String getValue() {\n" +
			    "        return T152afer2.getValueInternal();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team152afer2.java",
			    "\n" +
			    "public team class Team152afer2 {\n" +
			    "    public String getValue() {\n" +
			    "        return new Role152afer2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the name of a team equals the name of a subpackage
    // 1.5.3-otjld_testbug-subpackage-name-equals-team-name
    public void _testbug_test153_subpackageNameEqualsTeamName() {
        runNegativeTest(
            new String[] {
		"Team153snetn/T153snetn.java",
			    "\n" +
			    "// not a role class !\n" +
			    "public class T153snetn {\n" +
			    "    public static String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team153snetn.java",
			    "\n" +
			    "public team class Team153snetn {\n" +
			    "    private String value = T153snetn.getValue();\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role file has a mismatching package declaration - compile the team
    // 1.5.4-otjld-invalid-file-structure-1
    public void test154_invalidFileStructure1() {
        runNegativeTest(
            new String[] {
		"Team154ifs1.java",
			    "\n" +
			    "public team class Team154ifs1 {\n" +
			    "	Role154ifs1 r;\n" +
			    "}\n" +
			    "    \n",
		"Team154ifs1/Role154ifs1.java",
			    "\n" +
			    "team package wrong;\n" +
			    "public class Role154ifs1 {}\n" +
			    "    \n"
            },
            null);
    }

    // a role file has a mismatching package declaration - compile the role
    // 1.5.4-otjld-invalid-file-structure-2
    public void test154_invalidFileStructure2() {
        runNegativeTestMatching(
            new String[] {
		"Team154ifs2/Role154ifs2.java",
			    "\n" +
			    "team package wrong;\n" +
			    "public class Role154ifs2 {}\n" +
			    "    \n",
		"Team154ifs2.java",
			    "\n" +
			    "public team class Team154ifs2 {\n" +
			    "	Role154ifs2 r;\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.5(c)");
    }

    // a role file has a package declaration without 'team' - compile the team which references the role
    // 1.5.4-otjld-invalid-file-structure-3
    public void test154_invalidFileStructure3() {
        runNegativeTest(
            new String[] {
		"Team154ifs3.java",
			    "\n" +
			    "public team class Team154ifs3 {\n" +
			    "	Role154ifs3 r;\n" +
			    "}\n" +
			    "    \n",
		"Team154ifs3/Role154ifs3.java",
			    "\n" +
			    "package Team154ifs3;\n" +
			    "public class Role154ifs3 {}\n" +
			    "    \n"
            },
            null);
    }

    // a role file has a package declaration without 'team' - compile the role, which references the team
    // 1.5.4-otjld-invalid-file-structure-4
    public void test154_invalidFileStructure4() {
        runNegativeTest(
            new String[] {
		"Team154ifs4.java",
			    "\n" +
			    "public team class Team154ifs4 {}\n" +
			    "    \n",
		"Team154ifs4/Role154ifs4.java",
			    "\n" +
			    "package Team154ifs4;\n" +
			    "public class Role154ifs4 {\n" +
			    "	org.objectteams.Team t = Team154ifs4.this;\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role file has a team package declaration but the team is missing
    // 1.5.4-otjld-invalid-file-structure-5
    public void test154_invalidFileStructure5() {
        runNegativeTestMatching(
            new String[] {
		"Team154ifs5/Role154ifs5.java",
			    "\n" +
			    "team package Team154ifs5;\n" +
			    "public class Role154ifs5 {\n" +
			    "        org.objectteams.Team t = Team154ifs5.this;\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.5(c)");
    }

    // a role file is compiled separately after its enclosing team - no problem ;-)
    // 1.5.5-otjld-invalid-compile-order-1
    public void test155_invalidCompileOrder1() {
    	this.compileOrder = new String[][] {{"Team155ico1.java"},{"Team155ico1/Role155ico1.java"}};
        runConformTest(
            new String[] {
		"Team155ico1.java",
			    "\n" +
			    "public team class Team155ico1 {}\n" +
			    "    \n",
		"Team155ico1/Role155ico1.java",
			    "\n" +
			    "team package Team155ico1;\n" +
			    "protected class Role155ico1 {}\n" +
			    "    \n"
            });
    }

    // a role file is compiled separately after its enclosing team - reading binary team
    // 1.5.5-otjld-invalid-compile-order-2
    public void test155_invalidCompileOrder2() {
    	this.compileOrder = new String[][] {{"Team155ico2.java"}, {"Team155ico2/Role155ico2.java", "T155icoMain2.java"}};
        runConformTest(
            new String[] {
		"T155icoMain2.java",
			    "\n" +
			    "public class T155icoMain2 {\n" +
			    "	final Team155ico2 t = null;\n" +
			    "   @SuppressWarnings(\"roletypesyntax\")\n" +
			    "	t.Role155ico2 r;\n" +
			    "}    \n" +
			    "    \n",
		"Team155ico2.java",
			    "\n" +
			    "public team class Team155ico2 {}\n" +
			    "    \n",
		"Team155ico2/Role155ico2.java",
			    "\n" +
			    "team package Team155ico2;\n" +
			    "public class Role155ico2 {}\n" +
			    "    \n"
            });
    }

    // a role file is compiled explicitely instead of its team
    // 1.5.6-otjld-explicit-compile-of-rolefile-1
    public void test156_explicitCompileOfRolefile1() {
       this.compileOrder = new String[][] {{"Team156ecor1/Role156ecor1.java"}, {"T156ecor1Main.java"}};
       // compiled implicitly via role:
       myWriteFiles(new String[] {"Team156ecor1.java",
			    "public team class Team156ecor1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(new Role156ecor1().getValue());\n" +
			    "    }\n" +
			    "}\n"});
       runConformTest(
            new String[] {
		"T156ecor1Main.java",
			    "public class T156ecor1Main {\n" +
			    "    public static void main(String[] args){ \n" +
			    "        Team156ecor1 t = new Team156ecor1();\n" +
			    "        t.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
	    "Team156ecor1/Role156ecor1.java",
			    "team package Team156ecor1;\n" +
			    "protected class Role156ecor1 {\n" +
			    "    protected String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK",
            null,/*class libraries*/
            false,/*flushOutputDirectory*/
            null/*vmarguments*/);
    }

    // a role file is empty
    // 1.5.7-otjld-empty-role-file-1
    public void test157_emptyRoleFile1() {
        runConformTest(
            new String[] {
		"Team157erf1.java",
			    "\n" +
			    "public team class Team157erf1 {\n" +
			    "}    \n" +
			    "    \n",
		"Team157erf1/Role.java",
			    ""
            });
    }

    // a role file contains an interface
    // 1.5.8-otjld-interface-role-file-1
    public void test158_interfaceRoleFile1() {
       
       runConformTest(
            new String[] {
		"Team158irf1.java",
			    "\n" +
			    "public team class Team158irf1 {\n" +
			    "	public IR158irf1 getR() {\n" +
			    "		return new R158irf1(\"OK\");\n" +
			    "	}\n" +
			    "	public static void main (String[] args) {\n" +
			    "		final Team158irf1 t = new Team158irf1();\n" +
			    "		IR158irf1<@t> r = t.getR();\n" +
			    "		r.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team158irf1/R158irf1.java",
			    "\n" +
			    "team package Team158irf1;\n" +
			    "public class R158irf1 implements IR158irf1 {\n" +
			    "	String val;\n" +
			    "	protected R158irf1 (String v) { \n" +
			    "		val = v; \n" +
			    "	}\n" +
			    "	void test() { \n" +
			    "		System.out.print(val); \n" +
			    "	}\n" +
			    "}    \n" +
			    "	\n",
		"Team158irf1/IR158irf1.java",
			    "\n" +
			    "team package Team158irf1;\n" +
			    "public interface IR158irf1 {\n" +
			    "	void test();\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a role file contains an interface - class requested before the interface it implements (witness for "searching base class to early")
    // 1.5.8-otjld-interface-role-file-1f
    public void test158_interfaceRoleFile1f() {
        runConformTest(
            new String[] {
		"Team158irf1f.java",
			    "\n" +
			    "public team class Team158irf1f {\n" +
			    "    public Object getR() {\n" +
			    "        return new R158irf1f(\"OK\"); \n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        final Team158irf1f t = new Team158irf1f();\n" +
			    "        Object r = t.getR();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team158irf1f/R158irf1f.java",
			    "\n" +
			    "team package Team158irf1f;\n" +
			    "public class R158irf1f implements IR158irf1f {\n" +
			    "    String val;\n" +
			    "    protected R158irf1f (String v) { \n" +
			    "        val = v; \n" +
			    "    }\n" +
			    "    void test() { \n" +
			    "        System.out.print(val); \n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team158irf1f/IR158irf1f.java",
			    "\n" +
			    "team package Team158irf1f;\n" +
			    "public interface IR158irf1f {\n" +
			    "    void test();\n" +
			    "}    \n" +
			    "    \n"
            });
    }

    // a role file contains an interface, class is inline
    // 1.5.8-otjld-interface-role-file-2
    public void test158_interfaceRoleFile2() {
       
       runConformTest(
            new String[] {
		"Team158irf2.java",
			    "\n" +
			    "public team class Team158irf2 {\n" +
			    "	public class R implements IR158irf2 {\n" +
			    "		String val;\n" +
			    "		protected R (String v) { \n" +
			    "			val = v; \n" +
			    "		}\n" +
			    "		void test() { \n" +
			    "			System.out.print(val); \n" +
			    "		}\n" +
			    "	}    \n" +
			    "	public IR158irf2 getR() {\n" +
			    "		return new R(\"OK\");\n" +
			    "	}\n" +
			    "	public static void main (String[] args) {\n" +
			    "		final Team158irf2 t = new Team158irf2();\n" +
			    "		IR158irf2<@t> r = t.getR();\n" +
			    "		r.test();\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n",
		"Team158irf2/IR158irf2.java",
			    "\n" +
			    "team package Team158irf2;\n" +
			    "public interface IR158irf2 {\n" +
			    "	void test();\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a package (not team modifier) collides with a type
    // 1.5.9-otjld-package-collides-with-type-1
    public void test159_packageCollidesWithType1() {
        runNegativeTestMatching(
            new String[] {
		"p159/T159pcwt1/T159pcwt1_2.java",
			    "\n" +
			    "package p159.T159pcwt1;\n" +
			    "public class T159pcwt1_2 {\n" +
			    "}	\n" +
			    "	\n",
		"p159/T159pcwt1.java",
			    "\n" +
			    "package p159;\n" +
			    "public class T159pcwt1 {\n" +
			    "}	\n" +
			    "	\n"
            },
            "collides");
    }

    // a package (not team modifier) collides with a type -- have syntax errors
    // 1.5.9-otjld-package-collides-with-type-2
    public void test159_packageCollidesWithType2() {
        runNegativeTestMatching(
            new String[] {
		"p159/T159pcwt2/T159pcwt2_2.java",
			    "\n" +
			    "package p159.T159pcwt2;\n" +
			    "public class T159pcwt2_2 {\n" +
			    "	void();\n" +
			    "}	\n" +
			    "	\n",
		"p159/T159pcwt2.java",
			    "\n" +
			    "package p159;\n" +
			    "public class T159pcwt2 {\n" +
			    "}	\n" +
			    "	\n"
            },
            "collides");
    }

    // a team package has to be recognized despite of syntax errors
    // 1.5.9-otjld-package-collides-with-type-3
    public void test159_packageCollidesWithType3() {
        runNegativeTestMatching(
            new String[] {
		"p159/T159pcwt3/T159pcwt3_1.java",
			    "\n" +
			    "team package p159.T159pcwt3;\n" +
			    "public class T159pcwt3_1 {\n" +
			    "	void();\n" +
			    "}	\n" +
			    "	\n",
		"p159/T159pcwt3.java",
			    "\n" +
			    "package p159;\n" +
			    "public team class T159pcwt3 {\n" +
			    "}	\n" +
			    "	\n"
            },
            "Syntax");
    }

    // a nested role file team extends its enclosing
    // 1.5.10-otjld-circular-containment-and-extends-1
    public void test1510_circularContainmentAndExtends1() {
        runNegativeTestMatching(
            new String[] {
		"p1510/Team1510ccae1_1/Team1510ccae1_2.java",
			    "\n" +
			    "team package p1510.Team1510ccae1_1;    \n" +
			    "public team class Team1510ccae1_2 extends Team1510ccae1_1 {\n" +
			    "    public class R {}\n" +
			    "}\n" +
			    "    \n",
		"p1510/Team1510ccae1_1.java",
			    "\n" +
			    "package p1510;    \n" +
			    "public team class Team1510ccae1_1 {\n" +
			    "}    \n" +
			    "    \n"
            },
            "1.5(c)");
    }

    // a role file's class name doesn't match the filename -- WITNESS for TPX-287
    // 1.5.11-otjld-wrong-classname-in-rolefile-1
    public void test1511_wrongClassnameInRolefile1() {
        runNegativeTestMatching(
            new String[] {
		"Team1511wcir1/R1511wcir1.java",
			    "\n" +
			    "team package Team1511wcir1;\n" +
			    "public class R {\n" +
			    "}	\n" +
			    "	\n",
		"Team1511wcir1.java",
			    "\n" +
			    "public team class Team1511wcir1 {\n" +
			    "	public R myRole;\n" +
			    "}	\n" +
			    "	\n"
            },
            "1.2.5(b)");
    }

    // a type is resolved via a role file's import - field declaration
    // 1.5.11-otjld-role-file-import-1
    public void test1511_roleFileImport1() {
    	myWriteFiles(
    		new String[] {
    	"Team1511rfi1.java",
    			"public team class Team1511rfi1 {}    \n"
    		});
        runConformTest(
            new String[] {
        "T1511rfi1Main.java",
				"public class T1511rfi1Main {\n" +
				"	 public static void main(String... args) {\n" +
				"		 System.out.println(\"OK\");\n" +
				"	 }\n" +
				"}\n",
		"Team1511rfi1/R1511rfi1.java",
			    "team package Team1511rfi1;\n" +
			    "import java.util.Collection;\n" +
			    "public class R1511rfi1 {\n" +
			    "    Collection<String> field;\n" +
			    "}\n"
            },
		    null, /*expectedOutput*/
            null,/*class libraries*/
            false,/*flushOutputDirectory*/
            null/*vmarguments*/
        );
    }

    // a type is resolved via a role file's import - callout binding
    // 1.5.11-otjld-role-file-import-2
    public void test1511_roleFileImport2() {
        Map customOptions = getCompilerOptions();
        customOptions.put(CompilerOptions.OPTION_ReportWeaveIntoSystemClass, CompilerOptions.WARNING);
        myWriteFiles(
        	new String[] {
        "Team1511rfi2.java",
			    "import java.util.Collection;\n" +
			    "public team class Team1511rfi2 {}\n"
			});
        runTestExpectingWarnings(
            new String[] {
        "T1511rfi2Main.java",
        		"public class T1511rfi2Main {\n" +
        		"	 public static void main(String... args) {\n" +
        		"		 System.out.println(\"OK\");\n" +
        		"	 }\n" +
        		"}\n",
		"Team1511rfi2/R1511rfi2.java",
			    "\n" +
			    "team package Team1511rfi2;\n" +
			    "import java.util.LinkedList;\n" +
			    "@SuppressWarnings({\"rawtypes\",\"unchecked\",\"bindingconventions\"})\n" +
			    "public class R1511rfi2 playedBy LinkedList {\n" +
			    "    boolean addAll(Collection c) -> boolean addAll(Collection c);\n" +
			    "}\n"
            },
            "----------\n" + 
    		"1. WARNING in Team1511rfi2\\R1511rfi2.java (at line 5)\n" + 
    		"	public class R1511rfi2 playedBy LinkedList {\n" + 
    		"	                                ^^^^^^^^^^\n" + 
    		"Base class java.util.LinkedList appears to be a system class, which means that load time weaving could possibly fail\n" + 
    		"if this class is loaded from rt.jar/the bootstrap classpath.\n" + 
    		"----------\n",
            false);
    }

    // a role file uses a static import - method
    // 1.5.11-otjld-role-file-import-3
    public void test1511_roleFileImport3() {
    	myWriteFiles(new String[] {
		"Team1511rfi3.java",
			    "public team class Team1511rfi3 {}\n"
    		});
        runConformTest(
            new String[] {
        "T1511rfi3Main.java",
        		"public class T1511rfi3Main {\n" +
        		"	 public static void main(String... args) {\n" +
        		"		 System.out.println(\"OK\");\n" +
        		"	 }\n" +
        		"}\n",
		"Team1511rfi3/Role1511rfi3.java",
			    "team package Team1511rfi3;\n" +
			    "import static java.lang.Math.abs;\n" +
			    "protected class Role1511rfi3 {\n" +
			    "    int i= abs(-1);\n" +
			    "}\n"
            },
		    null, /*expectedOutput*/
            null,/*class libraries*/
            false,/*flushOutputDirectory*/
            null/*vmarguments*/);
    }

    // a nested role file uses a static import - field
    // 1.5.11-otjld-role-file-import-4
    public void test1511_roleFileImport4() {
    	myWriteFiles(new String[] {"Team1511rfi4.java",
			    "\n" +
			    "public team class Team1511rfi4 {\n" +
			    "    protected team class Inner {}\n" +
			    "}\n"
			 });
        runConformTest(
            new String[] {
        "T1511rfi4Main.java",
        		"public class T1511rfi4Main {\n" +
        		"	 public static void main(String... args) {\n" +
        		"		 System.out.println(\"OK\");\n" +
        		"	 }\n" +
        		"}\n",
		"Team1511rfi4/Inner/Role1511rfi4.java",
			    "\n" +
			    "team package Team1511rfi4.Inner;\n" +
			    "import static java.lang.Math.PI;\n" +
			    "protected class Role1511rfi4 {\n" +
			    "    double i= PI;\n" +
			    "}\n"
            },
		    null, /*expectedOutput*/
            null,/*class libraries*/
            false,/*flushOutputDirectory*/
            null/*vmarguments*/);
    }

    // a base classes is base imported in the role file - wrong
    // 1.5.11-otjld-role-file-import-5
    public void test1511_roleFileImport5() {
        runNegativeTestMatching(
            new String[] {
		"Team1511rfi5/R1511rfi5.java",
			    "\n" +
			    "team package Team1511rfi5;\n" +
			    "import base pbase.T1511rfi5;\n" +
			    "protected class R1511rfi5 playedBy T1511rfi5 {}\n" +
			    "    \n",
		"pbase/T1511rfi5.java",
			    "\n" +
			    "package pbase;\n" +
			    "public class T1511rfi5 {}\n" +
			    "    \n",
		"Team1511rfi5.java",
			    "\n" +
			    "public team class Team1511rfi5 {\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team1511rfi5\\R1511rfi5.java (at line 3)\n" + 
    		"	import base pbase.T1511rfi5;\n" + 
    		"	            ^^^^^^^^^^^^^^^\n" + 
    		"\"base\" import is not allowed in a role file (OTJLD 2.1.2(d)).\n" + 
    		"----------\n" + 
    		"2. ERROR in Team1511rfi5\\R1511rfi5.java (at line 4)\n" + 
    		"	protected class R1511rfi5 playedBy T1511rfi5 {}\n" + 
    		"	                                   ^^^^^^^^^\n" + 
    		"T1511rfi5 cannot be resolved to a type\n" + 
    		"----------\n");
    }

    // a base classes is base imported in the team file - correct
    // 1.5.11-otjld-role-file-import-6
    public void test1511_roleFileImport6() {
    	myWriteFiles(
    		new String[] {
    	"Team1511rfi6.java",
			    "import base pbase.T1511rfi6; // base class of role file\n" +
			    "public team class Team1511rfi6 {\n" +
			    "}\n"
    		});
    	this.compileOrder = new String[][] {{"pbase/T1511rfi6.java"}, {"Team1511rfi6/R1511rfi6.java"}};
        runConformTest(
            new String[] {
		"pbase/T1511rfi6.java",
			    "\n" +
			    "package pbase;\n" +
			    "public class T1511rfi6 {}\n",
		"Team1511rfi6/R1511rfi6.java",
			    "team package Team1511rfi6;\n" +
			    "protected class R1511rfi6 playedBy T1511rfi6 {}\n"
            },
		    null, /*expectedOutput*/
            null,/*class libraries*/
            false,/*flushOutputDirectory*/
            null/*vmarguments*/);
    }

    // a team tries to exploit an import of its role file
    // 1.5.11-otjld-role-file-import-7
    public void test1511_roleFileImport7() {
        runNegativeTestMatching(
            new String[] {
		"Team1511rfi7.java",
			    "\n" +
			    "public team class Team1511rfi7 {\n" +
			    "    void foo(R1511rfi7 r) {\n" +
			    "        LinkedList<R1511rfi7> roles = new LinkedList<R1511rfi7>();\n" +
			    "        roles.append(r);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1511rfi7/R1511rfi7.java",
			    "\n" +
			    "team package Team1511rfi7;\n" +
			    "import java.util.LinkedList;\n" +
			    "protected class R1511rfi7 {}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team1511rfi7.java (at line 4)\n" + 
    		"	LinkedList<R1511rfi7> roles = new LinkedList<R1511rfi7>();\n" + 
    		"	^^^^^^^^^^\n" + 
    		"LinkedList cannot be resolved to a type\n" + 
    		"----------\n" + 
    		"2. ERROR in Team1511rfi7.java (at line 4)\n" + 
    		"	LinkedList<R1511rfi7> roles = new LinkedList<R1511rfi7>();\n" + 
    		"	                                  ^^^^^^^^^^\n" + 
    		"LinkedList cannot be resolved to a type\n" + 
    		"----------\n");
    }

    // role files are compiled indirectly
    // 1.5.12-otjld-role-file-compile-order-1
    public void test1512_roleFileCompileOrder1() {
       
       runConformTest(
            new String[] {
		"Team1512rfco1.java",
			    "\n" +
			    "public team class Team1512rfco1 {\n" +
			    "    Team1512rfco1() {\n" +
			    "        R1 r = new R1(new T1512rfco1());\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1512rfco1();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T1512rfco1.java",
			    "\n" +
			    "public class T1512rfco1 {\n" +
			    "}    \n" +
			    "    \n",
		"Team1512rfco1/R1.java",
			    "\n" +
			    "team package Team1512rfco1;\n" +
			    "public class R1 playedBy T1512rfco1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "        R2 r = new R2(new T1512rfco1());\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1512rfco1/R2.java",
			    "\n" +
			    "team package Team1512rfco1;\n" +
			    "public class R2 playedBy T1512rfco1 {\n" +
			    "    protected void test() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // role files are compiled indirectly - extending each other
    // 1.5.12-otjld-role-file-compile-order-2
    public void test1512_roleFileCompileOrder2() {
       
       runConformTest(
            new String[] {
		"Team1512rfco2.java",
			    "\n" +
			    "public team class Team1512rfco2 {\n" +
			    "    Team1512rfco2() {\n" +
			    "        R2 r = new R2(new T1512rfco2());\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1512rfco2();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T1512rfco2.java",
			    "\n" +
			    "public class T1512rfco2 {\n" +
			    "}    \n" +
			    "    \n",
		"Team1512rfco2/R1.java",
			    "\n" +
			    "team package Team1512rfco2;\n" +
			    "public class R1 playedBy T1512rfco2 {\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1512rfco2/R2.java",
			    "\n" +
			    "team package Team1512rfco2;\n" +
			    "public class R2 extends R1 {\n" +
			    "    protected void test() {\n" +
			    "	super.test();\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // role files (unbound) are compiled indirectly - extending each other
    // 1.5.12-otjld-role-file-compile-order-3
    public void test1512_roleFileCompileOrder3() {
       
       runConformTest(
            new String[] {
		"Team1512rfco3.java",
			    "\n" +
			    "public team class Team1512rfco3 {\n" +
			    "    Team1512rfco3() {\n" +
			    "        R2 r = new R2();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1512rfco3();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1512rfco3/R2.java",
			    "\n" +
			    "team package Team1512rfco3;\n" +
			    "public class R2 extends R1 {\n" +
			    "    public void test() {\n" +
			    "	super.test();\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1512rfco3/R1.java",
			    "\n" +
			    "team package Team1512rfco3;\n" +
			    "public class R1 {\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // an enum is defined in a role file
    // 1.5.13-otjld-wrong-type-in-rolefile-1
    public void test1513_wrongTypeInRolefile1() {
        runNegativeTestMatching(
            new String[] {
		"Team1513wtir1/R1513wtir1.java",
			    "\n" +
			    "team package Team1513wtir1;\n" +
			    "enum R1513wtri1 { WRONG, WORSE } \n" +
			    "    \n",
		"Team1513wtir1.java",
			    "\n" +
			    "public team class Team1513wtir1 {}\n" +
			    "    \n"
            },
            "1.2.5(e)");
    }

    // a role file imports its base class which is not visible to the team
    // 1.5.14-otjld-role-file-scope-1
    public void test1514_roleFileScope1() {
       
       runConformTest(
            new String[] {
		"p2/Team1514rfs1.java",
			    "\n" +
			    "package p2;\n" +
			    "public team class Team1514rfs1 {\n" +
			    "    Team1514rfs1() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1514rfs1();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"p1/T1514rfs1.java",
			    "\n" +
			    "package p1;    \n" +
			    "public class T1514rfs1 {\n" +
			    "}\n" +
			    "    \n",
		"p2/Team1514rfs1/R.java",
			    "\n" +
			    "team package p2.Team1514rfs1;\n" +
			    "import p1.T1514rfs1;\n" +
			    "@SuppressWarnings(\"bindingconventions\")\n" +
			    "protected class R playedBy T1514rfs1 {\n" +
			    "    protected R() { \n" +
			    "        this(new T1514rfs1());\n" +
			    "    }\n" +
			    "    R(T1514rfs1 b) {\n" +
			    "        // nop just hand writting lifting ctor.\n" +
			    "    }\n" +
			    "    protected void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "        \n" +
			    "    \n"
            },
            "OK");
    }

    // a role file defines a callin - one compilation
    // 1.5.15-otjld-callin-into-rolefile-1
    public void test1515_callinIntoRolefile1() {
       
       runConformTest(
            new String[] {
		"Team1515cir1.java",
			    "\n" +
			    "public team class Team1515cir1 {\n" +
			    "    R r; // yikes still needed to 'load' the role file?\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1515cir1().activate();\n" +
			    "        new T1515cir1().o();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1515cir1.java",
			    "\n" +
			    "public class T1515cir1 {\n" +
			    "    public void o() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1515cir1/R.java",
			    "\n" +
			    "team package Team1515cir1;\n" +
			    "public class R playedBy T1515cir1 {\n" +
			    "    void k() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "    k <- after o;\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a role file defines a callin - one compilation role driven
    // 1.5.15-otjld-callin-into-rolefile-2
    public void test1515_callinIntoRolefile2() {
       
       runConformTest(
            new String[] {
		"Team1515cir2.java",
			    "\n" +
			    "public team class Team1515cir2 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1515cir2().activate();\n" +
			    "        new T1515cir2().o();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1515cir2.java",
			    "\n" +
			    "public class T1515cir2 {\n" +
			    "    public void o() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1515cir2/R.java",
			    "\n" +
			    "team package Team1515cir2;\n" +
			    "public class R playedBy T1515cir2 {\n" +
			    "    void k() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "    k <- after o;\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a role file defines a callin - separate compilation
    // 1.5.15-otjld-callin-into-rolefile-3
    public void test1515_callinIntoRolefile3() {
       
       runConformTest(
            new String[] {
		"Team1515cir3.java",
			    "\n" +
			    "public team class Team1515cir3 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1515cir3().activate();\n" +
			    "        new T1515cir3().o();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1515cir3.java",
			    "\n" +
			    "public class T1515cir3 {\n" +
			    "    public void o() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1515cir3/R.java",
			    "\n" +
			    "team package Team1515cir3;\n" +
			    "public class R playedBy T1515cir3 {\n" +
			    "    void k() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "    k <- after o;\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a role file defines a callin and a base predicate - separate compilation - witness for TPX-428
    // 1.5.15-otjld-callin-into-rolefile-4
    public void test1515_callinIntoRolefile4() {
       
       runConformTest(
            new String[] {
		"Team1515cir4.java",
			    "\n" +
			    "public team class Team1515cir4 {\n" +
			    "    boolean frozen = false;\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team1515cir4 t = new Team1515cir4();\n" +
			    "        t.activate();\n" +
			    "        new T1515cir4().o();\n" +
			    "        t.frozen = true;\n" +
			    "        new T1515cir4().o();        \n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1515cir4.java",
			    "\n" +
			    "public class T1515cir4 {\n" +
			    "    public void o() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1515cir4/R.java",
			    "\n" +
			    "team package Team1515cir4;\n" +
			    "public class R playedBy T1515cir4 \n" +
			    "    base when (!frozen || hasRole(base, R.class))\n" +
			    "{\n" +
			    "    void k() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "    k <- after o;\n" +
			    "}    \n" +
			    "    \n"
            },
            "OKO");
    }

    // two role files define a callin and a base predicate - team driven compilation - witness for TPX-428 part 2
    // 1.5.15-otjld-callin-into-rolefile-5
    public void test1515_callinIntoRolefile5() {
       
       runConformTest(
            new String[] {
		"Team1515cir5.java",
			    "\n" +
			    "public team class Team1515cir5 {\n" +
			    "    precedence R1, R2;\n" +
			    "    Team1515cir5(T1515cir5 as R2 o) {\n" +
			    "        this.activate();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T1515cir5  b = new T1515cir5();\n" +
			    "        new Team1515cir5(b);\n" +
			    "        b.o();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1515cir5.java",
			    "\n" +
			    "public class T1515cir5 {\n" +
			    "    public void o() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1515cir5/R1.java",
			    "\n" +
			    "team package Team1515cir5;\n" +
			    "public class R1 playedBy T1515cir5 \n" +
			    "    base when (hasRole(base, R1.class))\n" +
			    "{\n" +
			    "    void n() {\n" +
			    "        System.out.print(\"N\");\n" +
			    "    }\n" +
			    "    n <- after o;\n" +
			    "}    \n" +
			    "    \n",
		"Team1515cir5/R2.java",
			    "\n" +
			    "team package Team1515cir5;\n" +
			    "public class R2 playedBy T1515cir5 \n" +
			    "    base when (hasRole(base, R2.class))\n" +
			    "{\n" +
			    "    void k() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "    k <- after o;\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a role file defines a callin - team driven compilation reusing binary role
    // 1.5.15-otjld_postponed_callin-into-rolefile-6
    public void _postponed_test1515_callinIntoRolefile6() {
       
       runConformTest(
            new String[] {
		"Team1515cir6.java",
			    "\n" +
			    "public team class Team1515cir6 {\n" +
			    "    Team1515cir6(T1515cir6 as R1 o) {\n" +
			    "        this.activate();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T1515cir6  b = new T1515cir6();\n" +
			    "        new Team1515cir6(b);\n" +
			    "        b.o();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1515cir6.java",
			    "\n" +
			    "public class T1515cir6 {\n" +
			    "    public void o() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1515cir6/R1.java",
			    "\n" +
			    "team package Team1515cir6;\n" +
			    "public class R1 playedBy T1515cir6\n" +
			    "    base when (hasRole(base, R1.class))\n" +
			    "{\n" +
			    "    void n() {\n" +
			    "        System.out.print(\"N\");\n" +
			    "    }\n" +
			    "    n <- after o;\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role file defines a static replace callin - team driven compilation reusing binary role
    // 1.5.15-otjld_postponed_callin-into-rolefile-7
    public void _postponed_test1515_callinIntoRolefile7() {
       
       runConformTest(
            new String[] {
		"Team1515cir7.java",
			    "\n" +
			    "public team class Team1515cir7 {\n" +
			    "    Team1515cir7(T1515cir7 as R1 o) {\n" +
			    "        this.activate();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T1515cir7  b = new T1515cir7();\n" +
			    "        new Team1515cir7(b);\n" +
			    "        b.o();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1515cir7.java",
			    "\n" +
			    "public class T1515cir7 {\n" +
			    "    public void o() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1515cir7/R1.java",
			    "\n" +
			    "team package Team1515cir7;\n" +
			    "public class R1 playedBy T1515cir7\n" +
			    "    base when (hasRole(base, R1.class))\n" +
			    "{\n" +
			    "    callin void n() {\n" +
			    "        base.n();\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "    n <- replace o;\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a nested team is implicitly inherited (phantom) and contains a role file
    // 1.5.16-otjld_postponed_role-file-of-phantom-nested-team-1
    public void _postponed_test1516_roleFileOfPhantomNestedTeam1() {
       
       runConformTest(
            new String[] {
		"Team1516rfopnt1_2.java",
			    "\n" +
			    "public team class Team1516rfopnt1_2 extends Team1516rfopnt1_1 {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1516rfopnt1_2();\n" +
			    "    } \n" +
			    "}    \n" +
			    "    \n",
		"Team1516rfopnt1_1.java",
			    "\n" +
			    "public team class Team1516rfopnt1_1 {\n" +
			    "    Team1516rfopnt1_1() {\n" +
			    "        new Inner();\n" +
			    "    }    \n" +
			    "}    \n" +
			    "    \n",
		"Team1516rfopnt1_2/Inner/R1516rfopnt1.java",
			    "\n" +
			    "team package Team1516rfopnt1_2.Inner;\n" +
			    "protected class R1516rfopnt1 {\n" +
			    "    void test() { System.out.print(\"OK\"); }\n" +
			    "}    \n" +
			    "    \n",
		"Team1516rfopnt1_1/Inner.java",
			    "\n" +
			    "team package Team1516rfopnt1_1;\n" +
			    "public team class Inner {\n" +
			    "    protected class R1516rfopnt1 {\n" +
			    "        void test() { System.out.print(\"NOK\"); }\n" +
			    "    }\n" +
			    "    public Inner() {\n" +
			    "        new R1516rfopnt1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a nested role is in a role file , so is its direct enclosing
    // 1.5.17-otjld-nested-role-file-1
    public void test1517_nestedRoleFile1() {
       myWriteFiles(
    		new String[] {
    	"Team1517nrf1/Inner/R.java",
			    "team package Team1517nrf1.Inner;\n" +
			    "protected class R {\n" +
			    "    protected void test() { System.out.print(\"OK\"); }\n" +
			    "}\n",
		"Team1517nrf1/Inner.java",
			    "\n" +
			    "team package Team1517nrf1;\n" +
			    "protected team class Inner {\n" +
			    "    protected Inner() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "}\n"
    		});
       runConformTest(
            new String[] {
		"Team1517nrf1.java",
			    "\n" +
			    "public team class Team1517nrf1  {\n" +
			    "    Team1517nrf1 () {\n" +
			    "        new Inner();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1517nrf1();\n" +
			    "    }\n" +
			    "}\n"
            },
            "OK",
            null,/*class libraries*/
            false,/*flushOutputDirectory*/
            null/*vmarguments*/);
    }

    // a nested role is in a role file, direct enclosing is not
    // 1.5.17-otjld-nested-role-file-2
    public void test1517_nestedRoleFile2() {
       
       runConformTest(
            new String[] {
		"Team1517nrf2.java",
			    "\n" +
			    "public team class Team1517nrf2  {\n" +
			    "    protected team class Inner {\n" +
			    "        protected Inner() {\n" +
			    "            new R().test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team1517nrf2 () {\n" +
			    "        new Inner();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1517nrf2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1517nrf2/Inner/R.java",
			    "\n" +
			    "team package Team1517nrf2.Inner;\n" +
			    "protected class R {\n" +
			    "    protected void test() { System.out.print(\"OK\"); }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // see CVS:swtdipl/ve/test2, bug reported by kmeier
    // 1.5.17-otjld-nested-role-file-3
    public void test1517_nestedRoleFile3() {
        myWriteFiles(
            new String[] {
		"p/Team1517nrf3/Inner1_2.java",
			    "team package p.Team1517nrf3;\n" +
			    "public team class Inner1_2 extends Inner1_1 {\n" +
			    "    @Override\n" +
			    "    public class R1_1 {\n" +
			    "    }\n" +
			    "}\n",
		"p/Team1517nrf3/Inner1_1.java",
			    "team package p.Team1517nrf3;\n" +
			    "public abstract team class Inner1_1 {\n" +
			    "    public abstract class R1_1 {\n" +
			    "    }\n" +
			    "}\n",
		"p/Team1517nrf3.java",
			    "package p;\n" +
			    "public team class Team1517nrf3 {\n" +
			    "    final Inner1_1 anchor;\n" +
			    "    public Team1517nrf3() {\n" +
			    "        anchor = new Inner1_2();\n" +
			    "    }\n" +
			    "}\n"
            });
        runConformTest(
            new String[] {
        "T1517nrf3Main.java",
        		"public class T1517nrf3Main {\n" +
        		"	 public static void main(String... args) {\n" +
        		"		 System.out.println(\"OK\");\n" +
        		"	 }\n" +
        		"}\n",
		"p/Team1517nrf3/Inner2.java",
			    "team package p.Team1517nrf3;\n" +
			    "public team class Inner2 {\n" +
			    "    protected class R2 playedBy R1_1<@anchor> {\n" +
			    "        R2(R1_1<@anchor> theBase/*, int i*/) {\n" +
			    "            //base(theBase);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n"
            },
            null,/*expectedOutput*/
            null,/*class libraries*/
            false,/*flushOutputDirectory*/
            null/*vmarguments*/);
    }

    // empty classes
    // 1.5.17-otjld-nested-role-file-4
    public void test1517_nestedRoleFile4() {
        runConformTest(
            new String[] {
		"p/Team1517nrf4.java",
			    "\n" +
			    "package p;\n" +
			    "public team class Team1517nrf4 {}\n" +
			    "    \n",
		"p/Team1517nrf4/Mid1517nrf4.java",
			    "\n" +
			    "team package p.Team1517nrf4;\n" +
			    "public team class Mid1517nrf4 {}\n" +
			    "    \n",
		"p/Team1517nrf4/Mid1517nrf4/R1517nrf4.java",
			    "\n" +
			    "team package p.Team1517nrf4.Mid1517nrf4;\n" +
			    "protected class R1517nrf4 {}\n" +
			    "    \n"
            });
    }

    // empty classes - 4 levels - missing 'team' at level 3
    // 1.5.17-otjld-nested-role-file-5
    public void test1517_nestedRoleFile5() {
    	String convertedOutputPath = new String(OUTPUT_DIR+'/').replace('/', '\\');
        runNegativeTest(
            new String[] {
		"p/Team1517nrf5/Mid1517nrf5/R1517nrf5/Deep1517nrf5.java",
			    "\n" +
			    "team package p.Team1517nrf5.Mid1517nrf5.R1517nrf5;\n" +
			    "protected class Deep1517nrf5 {}\n" +
			    "    \n",
		"p/Team1517nrf5.java",
			    "\n" +
			    "package p;\n" +
			    "public team class Team1517nrf5 {}\n" +
			    "    \n",
		"p/Team1517nrf5/Mid1517nrf5.java",
			    "\n" +
			    "team package p.Team1517nrf5;\n" +
			    "public team class Mid1517nrf5 {}\n" +
			    "    \n",
		"p/Team1517nrf5/Mid1517nrf5/R1517nrf5.java",
			    "\n" +
			    "team package p.Team1517nrf5.Mid1517nrf5;\n" +
			    "protected class R1517nrf5 {}\n" +
			    "    \n"
            },
            "----------\n" + 
            "1. ERROR in "+convertedOutputPath+"p\\Team1517nrf5\\Mid1517nrf5.java (at line 3)\n" + 
            "	public team class Mid1517nrf5 {}\n" + 
            "    \n" + 
            "	                            ^^^^^^^^^\n" + 
            "The return type is incompatible with Team1517nrf5.Mid1517nrf5.R1517nrf5()\n" + 
            "----------\n" + 
            "----------\n" + 
            "1. ERROR in "+convertedOutputPath+"p\\Team1517nrf5\\Mid1517nrf5\\R1517nrf5.java (at line 3)\n" + 
            "	protected class R1517nrf5 {}\n" + 
            "	                ^^^^^^^^^\n" + 
            "Missing anchor (team instance) for role type p.Team1517nrf5.Mid1517nrf5.R1517nrf5 outside its team context (OTJLD 1.2.2(b)).\n" + 
            "----------\n" + 
            // this is the main error message, the others are mostly caused by compilation order issues:
            "----------\n" + 
            "1. ERROR in p\\Team1517nrf5\\Mid1517nrf5\\R1517nrf5\\Deep1517nrf5.java (at line 3)\n" + 
            "	protected class Deep1517nrf5 {}\n" + 
            "	                ^^^^^^^^^^^^\n" + 
            "Member types not allowed in regular roles. Mark class p.Team1517nrf5.Mid1517nrf5.R1517nrf5 as a team if Deep1517nrf5 should be its role (OTJLD 1.5(a,b)). \n" + 
            "----------\n" + 
            "----------\n" + 
            "1. ERROR in p\\Team1517nrf5.java (at line 3)\n" + 
            "	public team class Team1517nrf5 {}\n" + 
            "	                  ^^^^^^^^^^^^\n" + 
            "The type Team1517nrf5 is already defined\n" + 
            "----------\n" + 
            "----------\n" + 
            "1. ERROR in p\\Team1517nrf5\\Mid1517nrf5.java (at line 3)\n" + 
            "	public team class Mid1517nrf5 {}\n" + 
            "	                  ^^^^^^^^^^^\n" + 
            "Duplicate nested type Mid1517nrf5\n" + 
            "----------\n");
    }

    // empty classes - 4 levels : OK
    // 1.5.17-otjld-nested-role-file-6
    public void test1517_nestedRoleFile6() {
    	this.compileOrder = new String[][] {
    			{"p/Team1517nrf6.java"}, 
    			{"p/Team1517nrf6/Mid1517nrf6.java"},
    			{"p/Team1517nrf6/Mid1517nrf6/R1517nrf6.java"},
    			{"p/Team1517nrf6/Mid1517nrf6/R1517nrf6/Deep1517nrf6.java"}};
        runConformTest(
            new String[] {
		"p/Team1517nrf6.java",
			    "\n" +
			    "package p;\n" +
			    "public team class Team1517nrf6 {}\n" +
			    "    \n",
		"p/Team1517nrf6/Mid1517nrf6.java",
			    "\n" +
			    "team package p.Team1517nrf6;\n" +
			    "public team class Mid1517nrf6 {}\n" +
			    "    \n",
		"p/Team1517nrf6/Mid1517nrf6/R1517nrf6.java",
			    "\n" +
			    "team package p.Team1517nrf6.Mid1517nrf6;\n" +
			    "protected team class R1517nrf6 {}\n" +
			    "    \n",
		"p/Team1517nrf6/Mid1517nrf6/R1517nrf6/Deep1517nrf6.java",
			    "\n" +
			    "team package p.Team1517nrf6.Mid1517nrf6.R1517nrf6;\n" +
			    "protected class Deep1517nrf6 {}\n" +
			    "    \n"
            });
    }

    // a phantom role is a tsub of a role file
    // 1.5.18-otjld-implicitly-inherit-role-file-1
    public void test1518_implicitlyInheritRoleFile1() {
       
       runConformTest(
            new String[] {
		"Team1518iirf1_3.java",
			    "\n" +
			    "public team class Team1518iirf1_3 extends Team1518iirf1_2 {\n" +
			    "    protected class R2 extends R {}\n" +
			    "    Team1518iirf1_3() {\n" +
			    "        new R2().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1518iirf1_3();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1518iirf1_1.java",
			    "\n" +
			    "public team class Team1518iirf1_1 {\n" +
			    "}\n" +
			    "    \n",
		"Team1518iirf1_2.java",
			    "\n" +
			    "public team class Team1518iirf1_2 extends Team1518iirf1_1 {\n" +
			    "}\n" +
			    "    \n",
		"Team1518iirf1_1/R.java",
			    "\n" +
			    "team package Team1518iirf1_1;\n" +
			    "protected class R {\n" +
			    "    protected void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a phantom role is a tsub of a role file - invisible method
    // 1.5.18-otjld-implicitly-inherit-role-file-1f
    public void test1518_implicitlyInheritRoleFile1f() {
        runNegativeTestMatching(
            new String[] {
		"Team1518iirf1f_3.java",
			    "\n" +
			    "public team class Team1518iirf1f_3 extends Team1518iirf1f_2 {\n" +
			    "    protected class R2 extends R {}\n" +
			    "    Team1518iirf1f_3() {\n" +
			    "        new R2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1518iirf1f_2.java",
			    "\n" +
			    "public team class Team1518iirf1f_2 extends Team1518iirf1f_1 {\n" +
			    "}\n" +
			    "    \n",
		"Team1518iirf1f_1/R.java",
			    "\n" +
			    "team package Team1518iirf1f_1;\n" +
			    "protected class R {\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1518iirf1f_1.java",
			    "\n" +
			    "public team class Team1518iirf1f_1 {\n" +
			    "}\n" +
			    "    \n"
            },
            "not visible");
    }

    // a phantom role is a tsub of a role file - bound role
    // 1.5.18-otjld-implicitly-inherit-role-file-2
    public void test1518_implicitlyInheritRoleFile2() {
       Map customOptions = getCompilerOptions();
       
       runConformTest(
            new String[] {
		"Team1518iirf2_3.java",
			    "\n" +
			    "public team class Team1518iirf2_3 extends Team1518iirf2_2 {\n" +
			    "    protected class R2 extends R {}\n" +
			    "    Team1518iirf2_3(T1518iirf2 as R2 r) {\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1518iirf2_3(new T1518iirf2());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1518iirf2.java",
			    "\n" +
			    "public class T1518iirf2 {\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1518iirf2_1.java",
			    "\n" +
			    "/**\n" +
			    " * @role R\n" +
			    " */\n" +
			    "public team class Team1518iirf2_1 {\n" +
			    "}\n" +
			    "    \n",
		"Team1518iirf2_2.java",
			    "\n" +
			    "public team class Team1518iirf2_2 extends Team1518iirf2_1 {\n" +
			    "}\n" +
			    "    \n",
		"Team1518iirf2_1/R.java",
			    "\n" +
			    "team package Team1518iirf2_1;\n" +
			    "protected class R playedBy T1518iirf2 {\n" +
			    "    abstract protected void test();\n" +
			    "    test -> test;\n" +
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

    // a role file contains a base import
    // 1.5.19-ojtld-base-import-for-role-file-1
    public void test1519_baseImportForRoleFile1() {
        runNegativeTestMatching(
            new String[] {
		"Team1519bifrf1/R1519bifrf1.java",
			    "\n" +
			    "team package Team1519bifrf1;\n" +
			    "import base p1.T1519bifrf1;\n" +
			    "protected class R playedBy T1519bifrf1 {}\n" +
			    "    \n",
		"p1/T1519bifrf1.java",
			    "\n" +
			    "public class T1519bifrf1 {}\n" +
			    "    \n",
		"Team1519bifrf1.java",
			    "\n" +
			    "public team class Team1519bifrf1 {}\n" +
			    "    \n"
            },
            "2.1.2(d)");
    }

    // a role file finds no team
    // 1.5.20-otjld-role-file-without-team-1
    public void test1520_roleFileWithoutTeam1() {
        runNegativeTestMatching(
            new String[] {
		"Team1520rfwt1/R1520rfwt1.java",
			    "\n" +
			    "team package Team1520rfwt1;\n" +
			    "protected class R1520rfwt1 {\n" +
			    "    void foo() {\n" +
			    "        System.out.print(\"bar\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team1520rfwt1\\R1520rfwt1.java (at line 2)\n" + 
    		"	team package Team1520rfwt1;\n" + 
    		"	             ^^^^^^^^^^^^^\n" + 
    		"Enclosing team Team1520rfwt1 not found for role file R1520rfwt1 (OTJLD 1.2.5(c)).\n" + 
    		"----------\n");
    }

    // a role file imports a deprecated type, warning suppressed
    // 1.5.21-otjld-import-warning-suppressed-1
    public void test1521_importWarningSuppressed1() {
    	myWriteFiles(
    		new String[] {
    	"Team1521iws1.java",
			    "public team class Team1521iws1 {}\n"
    		});
        runConformTest(
            new String[] {
		"p1/T1521iws1.java",
			    "package p1;\n" +
			    "@Deprecated public class T1521iws1 {}\n",
		"Team1521iws1/R.java",
			    "team package Team1521iws1;\n" +
			    "import p1.T1521iws1;\n" +
			    "@SuppressWarnings(\"deprecation\")\n" +
			    "protected class R {\n" +
			    "    T1521iws1 f;\n" +
			    "}\n"
            },
            null/*expectedOutput*/,
            null/*classLibraries*/,
            false/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            null/*customOptions*/,
            null/*no custom requestor*/);
    }

    // a team mentions its role files in a javadoc @role tag - OK
    // 1.5.22-otjld-role-tag-in-javadoc-1
    public void test1522_roleTagInJavadoc1() {
        runConformTest(
            new String[] {
		"Team1522rtij1.java",
			    "\n" +
			    "/**\n" +
			    "  * A team with two role files.\n" +
			    "  * @role R1522rtij1_1\n" +
			    "  * @role R1522rtij1_2\n" +
			    "  */\n" +
			    "public team class Team1522rtij1 {\n" +
			    "    /** @param bar a R1522rtij1_2 */\n" +
			    "    void foo(R1522rtij1_2 bar) {}\n" +
			    "}\n" +
			    "    \n",
		"Team1522rtij1/R1522rtij1_1.java",
			    "\n" +
			    "team package Team1522rtij1;\n" +
			    "protected class R1522rtij1_1 {}\n" +
			    "    \n",
		"Team1522rtij1/R1522rtij1_2.java",
			    "\n" +
			    "team package Team1522rtij1;\n" +
			    "protected class R1522rtij1_2 {}\n" +
			    "    \n"
            });
    }

    // a team mentions its role file in a javadoc @role tag - one role file missing
    // 1.5.22-otjld-role-tag-in-javadoc-2
    public void test1522_roleTagInJavadoc2() {
       Map customOptions = getCompilerOptions();
       // -enableJavadoc
       customOptions.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
       // -warn:+javadoc
       customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportMissingJavadocTags, CompilerOptions.WARNING);
// part of +javadoc but not needed:
//       customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadocTagsDeprecatedRef, CompilerOptions.ENABLED);
//       customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadocTagsNotVisibleRef, CompilerOptions.ENABLED);
//       customOptions.put(CompilerOptions.OPTION_ReportMissingJavadocTagsVisibility, CompilerOptions.PRIVATE);
        runTestExpectingWarnings(
            new String[] {
		"Team1522rtij2.java",
			    "\n" +
			    "/**\n" +
			    "  * A team with a role file.\n" +
			    "  * @role R1522rtij2_1\n" +
			    "  */\n" +
			    "public team class Team1522rtij2 {\n" +
			    "    /** @param bar a R1522rtij2_2 */\n" +
			    "    void foo(R1522rtij2_2 bar) {} // ensure it's loaded\n" +
			    "}\n" +
			    "    \n",
		"Team1522rtij2/R1522rtij2_1.java",
			    "\n" +
			    "team package Team1522rtij2;\n" +
			    "protected class R1522rtij2_1 {}\n" +
			    "    \n",
		"Team1522rtij2/R1522rtij2_2.java",
			    "\n" +
			    "team package Team1522rtij2;\n" +
			    "protected class R1522rtij2_2 {}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. WARNING in Team1522rtij2.java (at line 6)\n" + 
    		"	public team class Team1522rtij2 {\n" + 
    		"	                  ^^^^^^^^^^^^^\n" + 
    		"Javadoc: Missing tag for role file R1522rtij2_2 (OTJLD 1.2.5(d)).\n" + 
    		"----------\n",
            customOptions);
    }

    // a team mentions its role file in a javadoc @role tag - inline role mentioned
    // 1.5.22-otjld-role-tag-in-javadoc-3
    public void test1522_roleTagInJavadoc3() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
       customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportMissingJavadocTags, CompilerOptions.WARNING);
        runTestExpectingWarnings(
            new String[] {
		"Team1522rtij3.java",
			    "\n" +
			    "/**\n" +
			    "  * A team with a role file.\n" +
			    "  * @role R1522rtij3_1 this is really a role file\n" +
			    "  * @role R1522rtij3_2 this role is inline\n" +
			    "  */\n" +
			    "public team class Team1522rtij3 {\n" +
			    "    protected class R1522rtij3_2 {}\n" +
			    "    \n" +
			    "    /** @param bar a R1522rtij3_2 */\n" +
			    "    void foo(R1522rtij3_2 bar) {}\n" +
			    "}\n" +
			    "    \n",
		"Team1522rtij3/R1522rtij3_1.java",
			    "\n" +
			    "team package Team1522rtij3;\n" +
			    "protected class R1522rtij3_1 {}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. WARNING in Team1522rtij3.java (at line 5)\n" + 
    		"	* @role R1522rtij3_2 this role is inline\n" + 
    		"	        ^^^^^^^^^^^^\n" + 
    		"Javadoc: R1522rtij3_2 is an inline role, whereas the @role tag should only be used for role files (OTJLD 1.2.5(d)).\n" + 
    		"----------\n",
            customOptions);
    }

    // a team mentions its role file in a javadoc @role tag - non-role mentioned
    // 1.5.22-otjld-role-tag-in-javadoc-4
    public void test1522_roleTagInJavadoc4() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
       customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportMissingJavadocTags, CompilerOptions.WARNING);
        runTestExpectingWarnings(
            new String[] {
		"Team1522rtij4.java",
			    "\n" +
			    "/**\n" +
			    "  * A team with a role file.\n" +
			    "  * @role R1522rtij4_1\n" +
			    "  * @role String\n" +
			    "  */\n" +
			    "public team class Team1522rtij4 {\n" +
			    "    protected class R1522rtij4_2 {}\n" +
			    "\n" +
			    "    /** @param bar a R1522rtij4_2 */\n" +
			    "    void foo(R1522rtij4_2 bar) {}\n" +
			    "}\n" +
			    "    \n",
		"Team1522rtij4/R1522rtij4_1.java",
			    "\n" +
			    "team package Team1522rtij4;\n" +
			    "protected class R1522rtij4_1 {}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. WARNING in Team1522rtij4.java (at line 5)\n" + 
    		"	* @role String\n" + 
    		"	        ^^^^^^\n" + 
    		"Javadoc: String cannot be resolved to a role of this team (OTJLD 1.2.5(d)).\n" + 
    		"----------\n",
            customOptions);
    }

    // a team mentions its role file in a javadoc @role tag - type name missing
    // 1.5.22-otjld-role-tag-in-javadoc-5
    public void test1522_roleTagInJavadoc5() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
       customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportMissingJavadocTags, CompilerOptions.WARNING);
        runTestExpectingWarnings(
            new String[] {
		"Team1522rtij5.java",
			    "\n" +
			    "/**\n" +
			    "  * A team with a role file.\n" +
			    "  * @role\n" +
			    "  * @role R1522rtij5_1\n" +
			    "  */\n" +
			    "public team class Team1522rtij5 {\n" +
			    "    protected class R1522rtij5_2 {}\n" +
			    "\n" +
			    "    /** @param bar a R1522rtij5_2 */\n" +
			    "    void foo(R1522rtij5_2 bar) {}\n" +
			    "}\n" +
			    "    \n",
		"Team1522rtij5/R1522rtij5_1.java",
			    "\n" +
			    "team package Team1522rtij5;\n" +
			    "protected class R1522rtij5_1 {}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. WARNING in Team1522rtij5.java (at line 4)\n" + 
    		"	* @role\n" + 
    		"	   ^^^^\n" + 
    		"Javadoc: Missing identifier\n" + 
    		"----------\n",
            customOptions);
    }

    // a team mentions its role file in a javadoc @role tag - unresolvable type
    // 1.5.22-otjld-role-tag-in-javadoc-6
    public void test1522_roleTagInJavadoc6() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
       customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportMissingJavadocTags, CompilerOptions.WARNING);
        runTestExpectingWarnings(
            new String[] {
		"Team1522rtij6.java",
			    "\n" +
			    "/**\n" +
			    "  * A team with a role file.\n" +
			    "  * @role R1522rtij6_1\n" +
			    "  * @role Wrong\n" +
			    "  */\n" +
			    "public team class Team1522rtij6 {\n" +
			    "    protected class R1522rtij6_2 {}\n" +
			    "\n" +
			    "    /** @param bar a R1522rtij6_2 */\n" +
			    "    void foo(R1522rtij6_2 bar) {}\n" +
			    "}\n" +
			    "    \n",
		"Team1522rtij6/R1522rtij6_1.java",
			    "\n" +
			    "team package Team1522rtij6;\n" +
			    "protected class R1522rtij6_1 {}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. WARNING in Team1522rtij6.java (at line 5)\n" + 
    		"	* @role Wrong\n" + 
    		"	        ^^^^^\n" + 
    		"Javadoc: Wrong cannot be resolved to a role of this team (OTJLD 1.2.5(d)).\n" + 
    		"----------\n",
            customOptions);
    }

    // a regular class has a role file tag
    // 1.5.22-otjld-role-tag-in-javadoc-7
    public void test1522_roleTagInJavadoc7() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
       customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportMissingJavadocTags, CompilerOptions.WARNING);
        runTestExpectingWarnings(
            new String[] {
		"T1522rtij7.java",
			    "\n" +
			    "/**\n" +
			    "  * A class with a role file.\n" +
			    "  * @role R1522rtij7_1\n" +
			    "  */\n" +
			    "public class T1522rtij7 {\n" +
			    "    protected class R1522rtij7_1 {}\n" +
			    "\n" +
			    "    /** @param bar a R1522rtij7_1 */\n" +
			    "    void foo(R1522rtij7_1 bar) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. WARNING in T1522rtij7.java (at line 4)\n" + 
    		"	* @role R1522rtij7_1\n" + 
    		"	        ^^^^^^^^^^^^\n" + 
    		"Javadoc: Illegal @role tag, type T1522rtij7 is not a team (OTJLD 1.2.5(d)).\n" + 
    		"----------\n",
           customOptions);
    }

    // a role file has a name that does not match the contained class
    // 1.5.23-otjld-wrong-filename-1
    public void test1523_wrongFilename1() {
        runNegativeTestMatching(
            new String[] {
		"Team1523wf1/RFile.java",
			    "\n" +
			    "team package Team1523wf1;\n" +
			    "protected class RClass {}    \n" +
			    "    \n",
		"Team1523wf1.java",
			    "\n" +
			    "public team class Team1523wf1 {}\n" +
			    "    \n"
            },
            "1.2.5(b)");
    }
    
    // a compilation unit contains a secondary toplevel team class
    public void test1524_secondaryTeam1() {
    	runConformTest(
    		new String[] {
    	"Team1524st1.java",
    		"team class T0 {\n" +
    		"    protected void test() {\n" +
    		"		System.out.print(\"OK\");\n" +
    		"    }\n" +
    		"}\n" +
    		"public class Team1524st1 {\n" +
    		"	public static void main(String... args) {\n" +
    		"		new T0().test();\n" +
    		"   }\n" +
    		"}\n"
    		}, 
    		"OK");
    }

    // a role file has a NLS warning
    public void test1525_warningInRoleFile1() {
    	Map customOptions = getCompilerOptions();
    	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.WARNING);
        runTestExpectingWarnings(
            new String[] {
		"Team1525wirf1.java",
			    "\n" +
			    "public team class Team1525wirf1 {\n" +
			    "}\n",
		"Team1525wirf1/Role.java",
			    "team package Team1525wirf1;\n" +
			    "protected class Role {\n" +
			    "    String val = \"OK\";\n" +
			    "}\n"
            },
            "----------\n" +
            "1. WARNING in Team1525wirf1\\Role.java (at line 3)\n" +
            "	String val = \"OK\";\n" +
            "	             ^^^^\n" +
            "Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" +
            "----------\n",
            customOptions);
    }

    // a role file suppresses an NLS warning (comment style)
    public void test1525_warningInRoleFile2() {
    	Map customOptions = getCompilerOptions();
    	customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.ERROR);
        myWriteFiles(
            new String[] {
		"Team1525wirf1/Role.java",
			    "team package Team1525wirf1;\n" +
			    "protected class Role {\n" +
			    "    void foo() {\n" +
			    "        String val = \"OK\"; //$NON-NLS-1$\n" +
			    "    }\n" +
			    "}\n"
            });
        runConformTest(
        	new String[] {
		"Team1525wirf1.java",
			    "\n" +
			    "public team class Team1525wirf1 {\n" +
			    "	Role r;\n" +
			    "}\n",
        	},
            "",
            null/*classLibraries*/,
            false/*shouldFlushOutputFolder*/,
            null/*vmArguments*/,
            customOptions, 
            null/*customRequestor*/);
    }
    
    // a role file holds a nested team which extends a non-team role file
    public void test1526_rofiTeamExtendsNonTeam1() {
        Map customOptions = getCompilerOptions();
        customOptions.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
        customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.WARNING);
        customOptions.put(CompilerOptions.OPTION_ReportMissingJavadocTags, CompilerOptions.WARNING);

        compileOrder = new String[][]{new String[]{"Team1526rtent1.java", "Team1526rtent1/Role1.java"}, new String[] {"Team1526rtent1/Role2.java"}};
    	runConformTest(
    		new String[] {
    	"Team1526rtent1.java",
    			"public team class Team1526rtent1 {}\n",
    	"Team1526rtent1/Role1.java",
    			"team package Team1526rtent1;\n" +
    			"protected class Role1 {}\n",
    	"Team1526rtent1/Role2.java",
    			"team package Team1526rtent1;\n" +
    			"protected team class Role2 extends Role1 {}\n"
    		},
            "",
            null/*classLibraries*/,
            true/*shouldFlushOutputDirectory*/,
            null/*vmArguments*/,
            customOptions,
            null/*no custom requestor*/);
    }
}
