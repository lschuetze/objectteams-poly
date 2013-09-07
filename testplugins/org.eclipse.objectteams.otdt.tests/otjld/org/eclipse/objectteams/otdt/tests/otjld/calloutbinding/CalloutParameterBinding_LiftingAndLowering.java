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
package org.eclipse.objectteams.otdt.tests.otjld.calloutbinding;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

@SuppressWarnings("unchecked")
public class CalloutParameterBinding_LiftingAndLowering extends AbstractOTJLDTest {
	
	public CalloutParameterBinding_LiftingAndLowering(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test321_calloutInvocationWithMappedParameter6"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return CalloutParameterBinding_LiftingAndLowering.class;
	}


    // an invocation of a callout-bound base method from the direct base class that has a parameter mapping
    // 3.2.1-otjld-callout-invocation-with-mapped-parameter-1
    public void test321_calloutInvocationWithMappedParameter1() {
       
       runConformTest(
            new String[] {
		"T321ciwmp1Main.java",
			    "\n" +
			    "public class T321ciwmp1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team321ciwmp1 t = new Team321ciwmp1();\n" +
			    "        T321ciwmp1    o = new T321ciwmp1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T321ciwmp1.java",
			    "\n" +
			    "public class T321ciwmp1 {\n" +
			    "    private String value = \"NOTOK\";\n" +
			    "\n" +
			    "    public void test(String arg) {\n" +
			    "        value = arg;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team321ciwmp1.java",
			    "\n" +
			    "public team class Team321ciwmp1 {\n" +
			    "\n" +
			    "    public class Role321ciwmp1 playedBy T321ciwmp1 {\n" +
			    "        public abstract void test(String arg) {}\n" +
			    "        void test(String arg) -> void test(String arg) with {\n" +
			    "            \"OK\" -> arg\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T321ciwmp1 as Role321ciwmp1 obj) {\n" +
			    "        obj.test(\"NOTOK\");\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an invocation of a callout-bound base method from the indirect base class that has a parameter mapping
    // 3.2.1-otjld-callout-invocation-with-mapped-parameter-2
    public void test321_calloutInvocationWithMappedParameter2() {
       
       runConformTest(
            new String[] {
		"T321ciwmp2Main.java",
			    "\n" +
			    "public class T321ciwmp2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team321ciwmp2 t = new Team321ciwmp2();\n" +
			    "        T321ciwmp2    o = new T321ciwmp2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T321ciwmp2.java",
			    "\n" +
			    "public class T321ciwmp2 {\n" +
			    "    private String value = \"\";\n" +
			    "\n" +
			    "    public void test(String arg1, long arg2, double arg3) {\n" +
			    "        value = arg1 + \"|\" + arg2 + \"|\" + arg3;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team321ciwmp2.java",
			    "\n" +
			    "public team class Team321ciwmp2 {\n" +
			    "\n" +
			    "    public class Role321ciwmp2_1 playedBy T321ciwmp2 {}\n" +
			    "\n" +
			    "    public class Role321ciwmp2_2 extends Role321ciwmp2_1 {\n" +
			    "        public abstract void test(String val1, int val2, long val3);\n" +
			    "        void test(String val1, int val2, long val3) -> void test(String arg1, long arg2, double arg3) with {\n" +
			    "            val1.substring(3) -> arg1,\n" +
			    "            -val2 -> arg2,\n" +
			    "            val3 -> arg3\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T321ciwmp2 as Role321ciwmp2_2 obj) {\n" +
			    "        obj.test(\"NOTOK\", -1, 2);\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK|1|2.0");
    }

    // an invocation of a callout-bound base method from the indirect base class that has a parameter mapping
    // 3.2.1-otjld-callout-invocation-with-mapped-parameter-3
    public void test321_calloutInvocationWithMappedParameter3() {
       
       runConformTest(
            new String[] {
		"T321ciwmp3Main.java",
			    "\n" +
			    "public class T321ciwmp3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team321ciwmp3_2 t = new Team321ciwmp3_2();\n" +
			    "        T321ciwmp3      o = new T321ciwmp3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T321ciwmp3.java",
			    "\n" +
			    "public class T321ciwmp3 {\n" +
			    "    private String value = \"\";\n" +
			    "\n" +
			    "    public void test(String arg1, long arg2) {\n" +
			    "        value = arg1 + \"|\" + arg2;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I321ciwmp3.java",
			    "\n" +
			    "public interface I321ciwmp3 {\n" +
			    "    public void test(double val1, Object val2);\n" +
			    "}\n" +
			    "    \n",
		"Team321ciwmp3_1.java",
			    "\n" +
			    "public team class Team321ciwmp3_1 {\n" +
			    "    public class Role321ciwmp3 playedBy T321ciwmp3 {}\n" +
			    "}\n" +
			    "    \n",
		"Team321ciwmp3_2.java",
			    "\n" +
			    "public team class Team321ciwmp3_2 extends Team321ciwmp3_1 {\n" +
			    "    public class Role321ciwmp3 implements I321ciwmp3 {\n" +
			    "        void test(double arg1, Object arg2) -> void test(String arg1, long arg2) with {\n" +
			    "            arg2.toString() -> arg1,\n" +
			    "            (long)arg1 -> arg2\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T321ciwmp3 as Role321ciwmp3 obj) {\n" +
			    "        obj.test(1.0, new StringBuffer(\"OK\"));\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK|1");
    }

    // corrected version of buggy reported example (callout version)
    // 3.2.1-otjld-callout-invocation-with-mapped-parameter-4
    public void test321_calloutInvocationWithMappedParameter4() {
       
       runConformTest(
            new String[] {
		"T321ciwmp4Main.java",
			    "\n" +
			    "public class T321ciwmp4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "                Team321ciwmp4 aTeam = new Team321ciwmp4();\n" +
			    "                aTeam.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T321ciwmp4.java",
			    "\n" +
			    "public class T321ciwmp4 {\n" +
			    "        public void getANumber(int number) {\n" +
			    "                System.out.print(\"Base: got number \" + number);\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n",
		"Team321ciwmp4.java",
			    "\n" +
			    "public team class Team321ciwmp4 {\n" +
			    "        protected class Role playedBy T321ciwmp4 {\n" +
			    "                public abstract void getData(String a);\n" +
			    "                void getData(String a) -> void getANumber(int b) with {\n" +
			    "                        Integer.valueOf(a) -> b\n" +
			    "                }\n" +
			    "        }\n" +
			    "        public void test() {\n" +
			    "            new Role(new T321ciwmp4()).getData(\"1951\");\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n"
            },
            "Base: got number 1951");
    }

    // corrected version of buggy reported example (callin version)
    // 3.2.1-otjld-callout-invocation-with-mapped-parameter-5
    public void test321_calloutInvocationWithMappedParameter5() {
       
       runConformTest(
            new String[] {
		"T321ciwmp5Main.java",
			    "\n" +
			    "public class T321ciwmp5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "                T321ciwmp5 baseObj = new T321ciwmp5();\n" +
			    "                Team321ciwmp5 aTeam = new Team321ciwmp5();\n" +
			    "                aTeam.activate();\n" +
			    "                baseObj.getANumber(1951);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T321ciwmp5.java",
			    "\n" +
			    "public class T321ciwmp5 {\n" +
			    "        public void getANumber(int number) {\n" +
			    "                System.out.print(\"Base: got number \" + number);\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n",
		"Team321ciwmp5.java",
			    "\n" +
			    "public team class Team321ciwmp5 {\n" +
			    "        protected class Role playedBy T321ciwmp5 {\n" +
			    "                public void getData(String a) {\n" +
			    "                    System.out.print(\"Role saw \"+a+\", \");\n" +
			    "                }\n" +
			    "                void getData(String a) <- before void getANumber(int b) with {\n" +
			    "                        a <- Integer.toString(b)\n" +
			    "                }\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n"
            },
            "Role saw 1951, Base: got number 1951");
    }

    // a callout binding has a syntax error (hang reported by Philippe Gerard)
    // 3.2.1-otjld-callout-invocation-with-mapped-parameter-6
    public void test321_calloutInvocationWithMappedParameter6() {
        runNegativeTest(
            new String[] {
		"Team321ciwmp6.java",
			    "\n" +
			    "public team class Team321ciwmp6 {\n" +
			    "        public class Role playedBy T321ciwmp6\n" +
			    "        {\n" +
			    "                int doSomethingOther(int r1) -> int doSomething(int b1) wrong\n" +
			    "                        with {\n" +
			    "                                r1 -> b1,\n" +
			    "                                result <- result\n" +
			    "                        };\n" +
			    "\n" +
			    "                int doCalloutGetSomeFieldDoubled() -> get int someField\n" +
			    "                        with {\n" +
			    "                                result <- 2 * base.someField\n" +
			    "                        };\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n",
		"T321ciwmp6.java",
			    "\n" +
			    "public class T321ciwmp6 {\n" +
			    "    int doSomething(int b1) { return 0; }\n" +
			    "    int someField;\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team321ciwmp6.java (at line 1)\n" + 
    		"	\n" + 
    		"public team class Team321ciwmp6 {\n" + 
    		"	^\n" + 
    		"Syntax error on token \";\", ++ expected before this token\n" + 
    		"----------\n" +
    		( this.complianceLevel < ClassFileConstants.JDK1_8 
    		?
	    		"2. ERROR in Team321ciwmp6.java (at line 5)\n" + 
	    		"	int doSomethingOther(int r1) -> int doSomething(int b1) wrong\n" + 
	    		"                        with {\n" + 
	    		"	                                                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
	    		"Syntax error on tokens, EmptyParameterMappings expected instead\n" + 
	    		"----------\n" + 
	    		"3. ERROR in Team321ciwmp6.java (at line 7)\n" + 
	    		"	r1 -> b1,\n" + 
	    		"	        ^\n" + 
	    		"Syntax error on token \",\", ; expected\n"+ 
	    		"----------\n" + 
	    		"4. ERROR in Team321ciwmp6.java (at line 9)\n" + 
	    		"	};\n" + 
	    		"	 ^\n" + 
	    		"Syntax error, insert \"}\" to complete ClassBody\n" + 
	    		"----------\n" + 
	    		"5. ERROR in Team321ciwmp6.java (at line 11)\n" + 
	    		"	int doCalloutGetSomeFieldDoubled() -> get int someField\n" + 
	    		"	^^^\n" + 
	    		"Syntax error on token \"int\", @ expected\n" + 
	    		"----------\n" + 
	    		"6. ERROR in Team321ciwmp6.java (at line 11)\n" + 
	    		"	int doCalloutGetSomeFieldDoubled() -> get int someField\n" + 
	    		"                        with {\n" + 
	    		"	^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
	    		"Syntax error on tokens, ClassHeader expected instead\n" + 
	    		"----------\n" + 
	    		"7. ERROR in Team321ciwmp6.java (at line 12)\n" + 
	    		"	with {\n" + 
	    		"                                result <- 2 * base.someField\n" + 
	    		"	      ^\n" + 
	    		"Syntax error on token \";\", & expected before this token\n" + 
	    		"----------\n" + 
	    		"8. ERROR in Team321ciwmp6.java (at line 13)\n" + 
	    		"	result <- 2 * base.someField\n" + 
	    		"	^^^^^^^^^^^^^\n" + 
	    		"Syntax error on tokens, delete these tokens\n" + 
	    		"----------\n" + 
	    		"9. ERROR in Team321ciwmp6.java (at line 13)\n" + 
	    		"	result <- 2 * base.someField\n" + 
	    		"	              ^^^^^\n" + 
	    		"Syntax error on tokens, delete these tokens\n" + 
	    		"----------\n" + 
	    		"10. ERROR in Team321ciwmp6.java (at line 13)\n" + 
	    		"	result <- 2 * base.someField\n" + 
	    		"	                  ^\n" + 
	    		"Syntax error, insert \"enum Identifier\" to complete EnumHeaderName\n" + 
	    		"----------\n" + 
	    		"11. ERROR in Team321ciwmp6.java (at line 13)\n" + 
	    		"	result <- 2 * base.someField\n" + 
	    		"	                  ^\n" + 
	    		"Syntax error, insert \"EnumBody\" to complete ClassBodyDeclarations\n" + 
	    		"----------\n" + 
	    		"12. ERROR in Team321ciwmp6.java (at line 14)\n" + 
	    		"	};\n" + 
	    		"	^\n" + 
	    		"Syntax error on token \"}\", { expected\n" + 
	    		"----------\n"
    		:
        		"2. ERROR in Team321ciwmp6.java (at line 5)\n" + 
        		"	int doSomethingOther(int r1) -> int doSomething(int b1) wrong\n" + 
        		"	                                                        ^^^^^\n" + 
        		"Syntax error on token \"wrong\", delete this token\n" + 
        		"----------\n" + 
        		"3. ERROR in Team321ciwmp6.java (at line 7)\n" + 
        		"	r1 -> b1,\n" + 
        		"	      ^^\n" + 
        		"Syntax error, insert \"AssignmentOperator Expression\" to complete Assignment\n" + 
        		"----------\n" + 
        		"4. ERROR in Team321ciwmp6.java (at line 7)\n" + 
        		"	r1 -> b1,\n" + 
        		"	      ^^\n" + 
        		"Syntax error, insert \"-> Identifier\" to complete ParameterMapping\n" + 
        		"----------\n" + 
        		"5. ERROR in Team321ciwmp6.java (at line 9)\n" + 
        		"	};\n" + 
        		"	 ^\n" + 
        		"Syntax error, insert \"}\" to complete ClassBody\n" + 
        		"----------\n" + 
        		"6. ERROR in Team321ciwmp6.java (at line 9)\n" + 
        		"	};\n" + 
        		"	 ^\n" + 
        		"Syntax error, insert \"}\" to complete ClassBody\n" + 
        		"----------\n" + 
        		"7. ERROR in Team321ciwmp6.java (at line 11)\n" + 
        		"	int doCalloutGetSomeFieldDoubled() -> get int someField\n" + 
        		"	^^^\n" + 
        		"Syntax error on token \"int\", @ expected\n" + 
        		"----------\n" + 
        		"8. ERROR in Team321ciwmp6.java (at line 11)\n" + 
        		"	int doCalloutGetSomeFieldDoubled() -> get int someField\n" + 
        		"                        with {\n" + 
        		"	^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
        		"Syntax error on tokens, ClassHeader expected instead\n" + 
        		"----------\n" + 
        		"9. ERROR in Team321ciwmp6.java (at line 12)\n" + 
        		"	with {\n" + 
        		"                                result <- 2 * base.someField\n" + 
        		"	      ^\n" + 
        		"Syntax error on token \";\", & expected before this token\n" + 
        		"----------\n" + 
        		"10. ERROR in Team321ciwmp6.java (at line 13)\n" + 
        		"	result <- 2 * base.someField\n" + 
        		"	^^^^^^^^^^^^^\n" + 
        		"Syntax error on tokens, delete these tokens\n" + 
        		"----------\n" + 
        		"11. ERROR in Team321ciwmp6.java (at line 13)\n" + 
        		"	result <- 2 * base.someField\n" + 
        		"	              ^^^^^\n" + 
        		"Syntax error on tokens, delete these tokens\n" + 
        		"----------\n" + 
        		"12. ERROR in Team321ciwmp6.java (at line 13)\n" + 
        		"	result <- 2 * base.someField\n" + 
        		"	                  ^\n" + 
        		"Syntax error, insert \"enum Identifier\" to complete EnumHeaderName\n" + 
        		"----------\n" + 
        		"13. ERROR in Team321ciwmp6.java (at line 13)\n" + 
        		"	result <- 2 * base.someField\n" + 
        		"	                  ^\n" + 
        		"Syntax error, insert \"EnumBody\" to complete ClassBodyDeclarations\n" + 
        		"----------\n" + 
        		"14. ERROR in Team321ciwmp6.java (at line 14)\n" + 
        		"	};\n" + 
        		"	^\n" + 
        		"Syntax error on token \"}\", { expected\n" + 
        		"----------\n"
    		));
    }

    // an invocation of a callout-bound base method from the direct base class that has a implicit result mapping
    // 3.2.2-otjld-callout-invocation-with-result-mapping-1
    public void test322_calloutInvocationWithResultMapping1() {
       
       runConformTest(
            new String[] {
		"T322ciwrm1Main.java",
			    "\n" +
			    "public class T322ciwrm1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team322ciwrm1 t = new Team322ciwrm1();\n" +
			    "        T322ciwrm1    o = new T322ciwrm1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T322ciwrm1.java",
			    "\n" +
			    "public class T322ciwrm1 {\n" +
			    "    public String test(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team322ciwrm1.java",
			    "\n" +
			    "public team class Team322ciwrm1 {\n" +
			    "    public class Role322ciwrm1_1 {\n" +
			    "        public String test(String arg) {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "	\n" +
			    "    public class Role322ciwrm1_2 extends Role322ciwrm1_1 playedBy T322ciwrm1 {\n" +
			    "        test => test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T322ciwrm1 as Role322ciwrm1_2 obj) {\n" +
			    "        return obj.test(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an invocation of a callout-bound base method from the indirect base class that has an explicit result mapping
    // 3.2.2-otjld-callout-invocation-with-result-mapping-2
    public void test322_calloutInvocationWithResultMapping2() {
       
       runConformTest(
            new String[] {
		"T322ciwrm2Main.java",
			    "\n" +
			    "public class T322ciwrm2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team322ciwrm2_2 t = new Team322ciwrm2_2();\n" +
			    "        T322ciwrm2    o = new T322ciwrm2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T322ciwrm2.java",
			    "\n" +
			    "public class T322ciwrm2 {\n" +
			    "    String test(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team322ciwrm2_1.java",
			    "\n" +
			    "public team class Team322ciwrm2_1 {\n" +
			    "    public class Role322ciwrm2 playedBy T322ciwrm2 {}\n" +
			    "}\n" +
			    "    \n",
		"Team322ciwrm2_2.java",
			    "\n" +
			    "public team class Team322ciwrm2_2 extends Team322ciwrm2_1 {\n" +
			    "\n" +
			    "    public class Role322ciwrm2 {\n" +
			    "        public abstract String test();\n" +
			    "        String test() -> String test(String arg) with {\n" +
			    "            \"NOTOK\" -> arg,\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T322ciwrm2 as Role322ciwrm2 obj) {\n" +
			    "        return obj.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a name-bound callout-binding has a parameter mapping
    // 3.2.3-otjld-parameter-mapping-when-name-bound
    public void test323_parameterMappingWhenNameBound() {
        runNegativeTest(
            new String[] {
		"T323pmwnb.java",
			    "\n" +
			    "public class T323pmwnb {\n" +
			    "    public void test(String arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team323pmwnb.java",
			    "\n" +
			    "public team class Team323pmwnb {\n" +
			    "\n" +
			    "    public class Role323pmwnb playedBy T323pmwnb {\n" +
			    "        public abstract void test(String arg);\n" +
			    "        test -> test with {\n" +
			    "            arg -> arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a name-bound callout-binding has a result mapping
    // 3.2.3-otjld-result-mapping-when-name-bound
    public void test323_resultMappingWhenNameBound() {
        runNegativeTest(
            new String[] {
		"T323rmwnb.java",
			    "\n" +
			    "public class T323rmwnb {\n" +
			    "    public String test() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team323rmwnb.java",
			    "\n" +
			    "public team class Team323rmwnb {\n" +
			    "\n" +
			    "    public class Role323rmwnb playedBy T323rmwnb {\n" +
			    "        public String test1() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        test1 -> test with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout-binding has a parameter mapping for an undefined parameter
    // 3.2.4-otjld-mapping-for-nonexisting-parameter-1
    public void test324_mappingForNonexistingParameter1() {
        runTestExpectingWarnings(
            new String[] {
		"T324mfnp1.java",
			    "\n" +
			    "public class T324mfnp1 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n",
		"Team324mfnp1.java",
			    "\n" +
			    "public team class Team324mfnp1 {\n" +
			    "\n" +
			    "    public class Role324mfnp1 playedBy T324mfnp1 {\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String arg) -> void test() with {\n" +
			    "            arg -> arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. WARNING in Team324mfnp1.java (at line 7)\n" + 
    		"	arg -> arg\n" + 
    		"	^^^^^^^^^^\n" + 
    		"Unused mapping for parameter arg is ignored (OTJLD 3.2).\n" + 
    		"----------\n");
    }

    // a callout-binding has a parameter mapping for an undefined parameter
    // 3.2.4-otjld-mapping-for-nonexisting-parameter-2
    public void test324_mappingForNonexistingParameter2() {
        runNegativeTest(
            new String[] {
		"T324mfnp2.java",
			    "\n" +
			    "public class T324mfnp2 {\n" +
			    "    public void test(String arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team324mfnp2.java",
			    "\n" +
			    "public team class Team324mfnp2 {\n" +
			    "\n" +
			    "    public class Role324mfnp2 playedBy T324mfnp2 {\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String arg) -> void test(String arg) with {\n" +
			    "            arg -> val\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout-binding has a parameter mapping for an undefined parameter, though the base method itself has a parameter of that name
    // 3.2.4-otjld-mapping-for-nonexisting-parameter-3
    public void test324_mappingForNonexistingParameter3() {
        runNegativeTest(
            new String[] {
		"T324mfnp3.java",
			    "\n" +
			    "public class T324mfnp3 {\n" +
			    "    public void test(String val) {}\n" +
			    "}\n" +
			    "    \n",
		"Team324mfnp3.java",
			    "\n" +
			    "public team class Team324mfnp3 {\n" +
			    "\n" +
			    "    public class Role324mfnp3 playedBy T324mfnp3 {\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String arg) -> void test(String arg) with {\n" +
			    "            arg -> val\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }


    // a callout-binding has two parameter mappings for the same parameter
    // 3.2.5-otjld-two-mappings-for-same-parameter
    public void test325_twoMappingsForSameParameter() {
        runNegativeTest(
            new String[] {
		"T325tmfsp.java",
			    "\n" +
			    "public class T325tmfsp {\n" +
			    "    public void test(String arg1, String arg2) {}\n" +
			    "}\n" +
			    "    \n",
		"Team325tmfsp.java",
			    "\n" +
			    "public team class Team325tmfsp {\n" +
			    "\n" +
			    "    public class Role325tmfsp playedBy T325tmfsp {\n" +
			    "        public abstract void test(String arg1, String arg2);\n" +
			    "        void test(String arg1, String arg2) -> void test(String arg1, String arg2) with {\n" +
			    "            arg2 -> arg2,\n" +
			    "            arg1 -> arg1,\n" +
			    "            \"OK\" -> arg2\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout-binding has a result mapping but the role method has no return type
    // 3.2.6-otjld-result-mapping-for-void
    public void test326_resultMappingForVoid() {
        runNegativeTest(
            new String[] {
		"T326rmfv.java",
			    "\n" +
			    "public class T326rmfv {\n" +
			    "    public String test(String arg) {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team326rmfv.java",
			    "\n" +
			    "public team class Team326rmfv {\n" +
			    "\n" +
			    "    public class Role326rmfv playedBy T326rmfv {\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String arg) -> String test(String arg) with {\n" +
			    "            result <- result,\n" +
			    "            arg -> arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout-binding has two result mappings
    // 3.2.7-otjld-two-result-mappings
    public void test327_twoResultMappings() {
        runNegativeTest(
            new String[] {
		"T327trm.java",
			    "\n" +
			    "public class T327trm {\n" +
			    "    public String test(String arg) {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team327trm.java",
			    "\n" +
			    "public team class Team327trm {\n" +
			    "\n" +
			    "    public class Role327trm playedBy T327trm {\n" +
			    "        public abstract String test(String arg);\n" +
			    "        String test(String arg) -> String test(String arg) with {\n" +
			    "            result <- result,\n" +
			    "            arg -> arg,\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a delimiter follows after the last parameter binding of a callout-binding
    // 3.2.8-otjld-delimiter-finishes-last-parameter-mapping-1
    public void test328_delimiterFinishesLastParameterMapping1() {
        runConformTest(
            new String[] {
		"T328dflpm1.java",
			    "\n" +
			    "public class T328dflpm1 {\n" +
			    "    public String test(String arg) {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team328dflpm1.java",
			    "\n" +
			    "public team class Team328dflpm1 {\n" +
			    "\n" +
			    "    public class Role328dflpm1 playedBy T328dflpm1 {\n" +
			    "        public abstract String test(String arg);\n" +
			    "        String test(String arg) -> String test(String arg) with {\n" +
			    "            arg -> arg,\n" +
			    "            result <- result,\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a delimiter follows after the last parameter binding of a callout-binding
    // 3.2.8-otjld-delimiter-finishes-last-parameter-mapping-2
    public void test328_delimiterFinishesLastParameterMapping2() {
        runNegativeTest(
            new String[] {
		"T328dflpm2.java",
			    "\n" +
			    "public class T328dflpm2 {\n" +
			    "    public String test(String arg) {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team328dflpm2.java",
			    "\n" +
			    "public team class Team328dflpm2 {\n" +
			    "\n" +
			    "    public class Role328dflpm2 playedBy T328dflpm2 {\n" +
			    "        public abstract String test(String arg);\n" +
			    "        String test(String arg) -> String test(String arg) with {\n" +
			    "            result <- result,\n" +
			    "            arg -> arg;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an illegal parameter mapping is used in a callout binding
    // 3.2.9-otjld-illegal-parameter-mapping
    public void test329_illegalParameterMapping() {
        runNegativeTest(
            new String[] {
		"T329ipm.java",
			    "\n" +
			    "public class T329ipm {\n" +
			    "    public void test(String arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team329ipm.java",
			    "\n" +
			    "public team class Team329ipm {\n" +
			    "\n" +
			    "    public class Role329ipm playedBy T329ipm {\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String arg) -> void test(String arg) with {\n" +
			    "            arg -> arg,\n" +
			    "            \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the direction of a parameter mapping is wrong
    // 3.2.10-otjld-wrong-parameter-mapping-direction-1
    public void test3210_wrongParameterMappingDirection1() {
        runNegativeTest(
            new String[] {
		"Team3210wpmd1.java",
			    "\n" +
			    "public team class Team3210wpmd1 {\n" +
			    "\n" +
			    "    public class Role3210wpmd1 playedBy T3210wpmd1 {\n" +
			    "        public abstract String test(String arg);\n" +
			    "        String test(String arg) -> void test(String arg1, String arg2) with {\n" +
			    "            arg -> arg1,\n" +
			    "            arg2 <- arg,\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3210wpmd1.java",
			    "\n" +
			    "public class T3210wpmd1 {\n" +
			    "    public void test(String arg1, String arg2) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team3210wpmd1.java (at line 6)\n" + 
    		"	String test(String arg) -> void test(String arg1, String arg2) with {\n" + 
    		"	                           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
    		"Incomplete parameter mapping: argument arg2 of base method void test(String arg1, String arg2) is not mapped (OTJLD 3.2(b)).\n" + 
    		"----------\n");
    }

    // the direction of a parameter mapping is wrong -
    // 3.2.10-otjld-wrong-parameter-mapping-direction-2
    public void test3210_wrongParameterMappingDirection2() {
        runNegativeTest(
            new String[] {
		"Team3210wpmd2.java",
			    "\n" +
			    "public team class Team3210wpmd2 {\n" +
			    "\n" +
			    "    public class Role3210wpmd2 playedBy T3210wpmd2 {\n" +
			    "        public abstract String test(String arg);\n" +
			    "        String test(String arg) -> void test(String arg1) with {\n" +
			    "            arg -> arg1,\n" +
			    "            resultWrong <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3210wpmd2.java",
			    "\n" +
			    "public class T3210wpmd2 {\n" +
			    "    public void test(String arg1) {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team3210wpmd2.java (at line 8)\n" + 
    		"	resultWrong <- \"OK\"\n" + 
    		"	^^^^^^^^^^^^^^^^^^^\n" + 
    		"Wrong mapping direction for parameter resultWrong, should use -> unless mapping \'result\' (OTJLD 3.2(b)). \n" + 
    		"----------\n");
    }

    // the direction of a result mapping is wrong
    // 3.2.11-otjld-wrong-result-mapping-direction
    public void test3211_wrongResultMappingDirection() {
        runNegativeTest(
            new String[] {
		"T3211wrmd.java",
			    "\n" +
			    "public class T3211wrmd {\n" +
			    "    public void test(String arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team3211wrmd.java",
			    "\n" +
			    "public team class Team3211wrmd {\n" +
			    "\n" +
			    "    public class Role3211wrmd playedBy T3211wrmd {\n" +
			    "        public abstract String test(String arg);\n" +
			    "        String test(String arg) -> void test(String arg) with {\n" +
			    "            arg -> arg,\n" +
			    "            \"OK\" -> result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the mapping expression is a call to a method without return value
    // 3.2.12-otjld-mapping-expression-has-no-value-1
    public void test3212_mappingExpressionHasNoValue1() {
        runNegativeTest(
            new String[] {
		"T3212mehnv1.java",
			    "\n" +
			    "public class T3212mehnv1 {\n" +
			    "    public void test(String arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team3212mehnv1.java",
			    "\n" +
			    "public team class Team3212mehnv1 {\n" +
			    "\n" +
			    "    public class Role3212mehnv1 playedBy T3212mehnv1 {\n" +
			    "        public void doSomething() {}\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String arg) -> void test(String arg) with {\n" +
			    "            doSomething() -> arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the mapping expression is a constructor call
    // 3.2.12-otjld-mapping-expression-has-no-value-2
    public void test3212_mappingExpressionHasNoValue2() {
        runNegativeTest(
            new String[] {
		"T3212mehnv2.java",
			    "\n" +
			    "public class T3212mehnv2 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n",
		"Team3212mehnv2.java",
			    "\n" +
			    "public team class Team3212mehnv2 {\n" +
			    "\n" +
			    "    public class Role3212mehnv2 playedBy T3212mehnv2 {\n" +
			    "        public abstract String test();\n" +
			    "        String test() -> void test() with {\n" +
			    "            result <- Role3212mehnv2()\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role method without return value is callout-bound using 'result' as a parameter name
    // 3.2.13-otjld-parameter-with-name-result-1
    public void test3213_parameterWithNameResult1() {
        runConformTest(
            new String[] {
		"T3213pwnr1.java",
			    "\n" +
			    "public class T3213pwnr1 {\n" +
			    "    public void test(String arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team3213pwnr1.java",
			    "\n" +
			    "public team class Team3213pwnr1 {\n" +
			    "\n" +
			    "    public class Role3213pwnr1 playedBy T3213pwnr1 {\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String result) -> void test(String arg);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a role method without return value is callout-bound using 'result' as a parameter name
    // 3.2.13-otjld-parameter-with-name-result-2
    public void test3213_parameterWithNameResult2() {
        runNegativeTestMatching(
            new String[] {
		"Team3213pwnr2.java",
			    "\n" +
			    "public team class Team3213pwnr2 {\n" +
			    "\n" +
			    "    public class Role3213pwnr2 playedBy T3213pwnr2 {\n" +
			    "        public abstract String test(String arg);\n" +
			    "        String test(String arg) -> String test(String result) with {\n" +
			    "            arg -> result,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3213pwnr2.java",
			    "\n" +
			    "public class T3213pwnr2 {\n" +
			    "    public String test(String arg) {\n" +
			    "        return \"\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3.2(c)");
    }

    // a field of the role class is used in a parameter mapping
    // 3.2.14-otjld-role-feature-access-1
    public void test3214_roleFeatureAccess1() {
       
       runConformTest(
            new String[] {
		"T3214rfa1Main.java",
			    "\n" +
			    "public class T3214rfa1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3214rfa1 t = new Team3214rfa1();\n" +
			    "        T3214rfa1    o = new T3214rfa1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3214rfa1.java",
			    "\n" +
			    "public class T3214rfa1 {\n" +
			    "    private String value = \"NOTOK\";\n" +
			    "\n" +
			    "    public void test(String arg) {\n" +
			    "        value = arg;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3214rfa1.java",
			    "\n" +
			    "public team class Team3214rfa1 {\n" +
			    "\n" +
			    "    public class Role3214rfa1 playedBy T3214rfa1 {\n" +
			    "        private String value = \"OK\";\n" +
			    "\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String arg) -> void test(String arg) with {\n" +
			    "            value -> arg\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3214rfa1 as Role3214rfa1 obj) {\n" +
			    "        obj.test(\"NOTOK\");\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method of the implicit superrole is used in a result mapping
    // 3.2.14-otjld-role-feature-access-2
    public void test3214_roleFeatureAccess2() {
       
       runConformTest(
            new String[] {
		"T3214rfa2Main.java",
			    "\n" +
			    "public class T3214rfa2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3214rfa2_2 t = new Team3214rfa2_2();\n" +
			    "        T3214rfa2      o = new T3214rfa2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3214rfa2.java",
			    "\n" +
			    "public class T3214rfa2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3214rfa2_1.java",
			    "\n" +
			    "public team class Team3214rfa2_1 {\n" +
			    "\n" +
			    "    public class Role3214rfa2 playedBy T3214rfa2 {\n" +
			    "        String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3214rfa2_2.java",
			    "\n" +
			    "public team class Team3214rfa2_2 extends Team3214rfa2_1 {\n" +
			    "\n" +
			    "    public class Role3214rfa2 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- getValueInternal()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3214rfa2 as Role3214rfa2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a field of the explicit superrole is used in a parameter mapping
    // 3.2.14-otjld-role-feature-access-3
    public void test3214_roleFeatureAccess3() {
       
       runConformTest(
            new String[] {
		"T3214rfa3Main.java",
			    "\n" +
			    "public class T3214rfa3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3214rfa3 t = new Team3214rfa3();\n" +
			    "        T3214rfa3    o = new T3214rfa3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3214rfa3.java",
			    "\n" +
			    "public class T3214rfa3 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3214rfa3.java",
			    "\n" +
			    "public team class Team3214rfa3 {\n" +
			    "\n" +
			    "    public class Role3214rfa3_1 playedBy T3214rfa3 {\n" +
			    "        protected String value = \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role3214rfa3_2 extends Role3214rfa3_1 {\n" +
			    "        public abstract String getValue(String arg);\n" +
			    "        String getValue(String arg) -> String getValue(String arg) with {\n" +
			    "            this.value -> arg,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3214rfa3 as Role3214rfa3_2 obj) {\n" +
			    "        return obj.getValue(\"NOTOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method of an indirect superrole is used in a result mapping
    // 3.2.14-otjld-role-feature-access-4
    public void test3214_roleFeatureAccess4() {
       
       runConformTest(
            new String[] {
		"T3214rfa4Main.java",
			    "\n" +
			    "public class T3214rfa4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3214rfa4_2 t = new Team3214rfa4_2();\n" +
			    "        T3214rfa4      o = new T3214rfa4();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3214rfa4.java",
			    "\n" +
			    "public class T3214rfa4 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3214rfa4_1.java",
			    "\n" +
			    "public team class Team3214rfa4_1 {\n" +
			    "\n" +
			    "    public class Role3214rfa4_1 playedBy T3214rfa4 {\n" +
			    "        public String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3214rfa4_2.java",
			    "\n" +
			    "public team class Team3214rfa4_2 extends Team3214rfa4_1 {\n" +
			    "\n" +
			    "    public class Role3214rfa4_2 extends Role3214rfa4_1 {\n" +
			    "        String getValue() => String getValue() with {\n" +
			    "            result <- getValueInternal()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3214rfa4 as Role3214rfa4_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method of the explicit superrole is used in a parameter mapping using super
    // 3.2.14-otjld-role-feature-access-5
    public void test3214_roleFeatureAccess5() {
       
       runConformTest(
            new String[] {
		"T3214rfa5Main.java",
			    "\n" +
			    "public class T3214rfa5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3214rfa5 t = new Team3214rfa5();\n" +
			    "        T3214rfa5    o = new T3214rfa5();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3214rfa5.java",
			    "\n" +
			    "public class T3214rfa5 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3214rfa5.java",
			    "\n" +
			    "public team class Team3214rfa5 {\n" +
			    "\n" +
			    "    public class Role3214rfa5_1 playedBy T3214rfa5 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role3214rfa5_2 extends Role3214rfa5_1 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- super.getValueInternal()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3214rfa5 as Role3214rfa5_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a field of the enclosing team is used in a parameter mapping
    // 3.2.15-otjld-team-feature-access-1
    public void test3215_teamFeatureAccess1() {
       
       runConformTest(
            new String[] {
		"T3215tfa1Main.java",
			    "\n" +
			    "public class T3215tfa1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3215tfa1 t = new Team3215tfa1();\n" +
			    "        T3215tfa1    o = new T3215tfa1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3215tfa1.java",
			    "\n" +
			    "public class T3215tfa1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3215tfa1.java",
			    "\n" +
			    "public team class Team3215tfa1 {\n" +
			    "    private String value = \"OK\";\n" +
			    "\n" +
			    "    public class Role3215tfa1 playedBy T3215tfa1 {\n" +
			    "        public abstract String getValue(String arg);\n" +
			    "        String getValue(String arg) -> String getValue(String arg) with {\n" +
			    "            Team3215tfa1.this.value -> arg,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3215tfa1 as Role3215tfa1 obj) {\n" +
			    "        return obj.getValue(\"NOTOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method of the direct superteam is used in a result mapping
    // 3.2.15-otjld-team-feature-access-2
    public void test3215_teamFeatureAccess2() {
       
       runConformTest(
            new String[] {
		"T3215tfa2Main.java",
			    "\n" +
			    "public class T3215tfa2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3215tfa2_2 t = new Team3215tfa2_2();\n" +
			    "        T3215tfa2      o = new T3215tfa2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3215tfa2.java",
			    "\n" +
			    "public class T3215tfa2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3215tfa2_1.java",
			    "\n" +
			    "public team class Team3215tfa2_1 {\n" +
			    "    public String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3215tfa2_2.java",
			    "\n" +
			    "public team class Team3215tfa2_2 extends Team3215tfa2_1 {\n" +
			    "    public class Role3215tfa2 playedBy T3215tfa2 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- getValueInternal()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3215tfa2 as Role3215tfa2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a field of an indirect superteam is used in a parameter mapping
    // 3.2.15-otjld-team-feature-access-3
    public void test3215_teamFeatureAccess3() {
       
       runConformTest(
            new String[] {
		"p1/T3215tfa3Main.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T3215tfa3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3215tfa3_3 t = new Team3215tfa3_3();\n" +
			    "        T3215tfa3      o = new T3215tfa3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T3215tfa3.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T3215tfa3 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/Team3215tfa3_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team3215tfa3_1 {\n" +
			    "    String value = \"OK\";\n" +
			    "}\n" +
			    "    \n",
		"p1/Team3215tfa3_2.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team3215tfa3_2 extends Team3215tfa3_1 {\n" +
			    "	public class Role3215tfa3 {\n" +
			    "            public String getValue(String arg) {\n" +
			    "            	return arg;\n" +
			    "	    }\n" +
			    "        }\n" +
			    "}\n" +
			    "    \n",
		"p1/Team3215tfa3_3.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team3215tfa3_3 extends Team3215tfa3_2 {\n" +
			    "    public class Role3215tfa3 playedBy T3215tfa3 {\n" +
			    "        String getValue(String arg) => String getValue(String arg) with {\n" +
			    "            arg -> arg,\n" +
			    "            result <- value\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3215tfa3 as Role3215tfa3 obj) {\n" +
			    "        return obj.getValue(\"NOTOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a field of the direct superteam is used in a result mapping via tsuper
    // 3.2.15-otjld-team-feature-access-4
    public void test3215_teamFeatureAccess4() {
       
       runConformTest(
            new String[] {
		"T3215tfa4Main.java",
			    "\n" +
			    "public class T3215tfa4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3215tfa4_2 t = new Team3215tfa4_2();\n" +
			    "        T3215tfa4      o = new T3215tfa4();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3215tfa4.java",
			    "\n" +
			    "public class T3215tfa4 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3215tfa4_1.java",
			    "\n" +
			    "public team class Team3215tfa4_1 {\n" +
			    "    protected String value = \"OK\";\n" +
			    "}\n" +
			    "    \n",
		"Team3215tfa4_2.java",
			    "\n" +
			    "public team class Team3215tfa4_2 extends Team3215tfa4_1 {\n" +
			    "    public class Role3215tfa4 playedBy T3215tfa4 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- Team3215tfa4_2.super.value\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3215tfa4 as Role3215tfa4 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a field of the enclosing normal class is used in a result mapping
    // 3.2.16-otjld-enclosing-class-feature-access-1
    public void test3216_enclosingClassFeatureAccess1() {
       
       runConformTest(
            new String[] {
		"T3216ecfa1Main.java",
			    "\n" +
			    "public class T3216ecfa1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T3216ecfa1_2.Team3216ecfa1 t = new T3216ecfa1_2().new Team3216ecfa1();\n" +
			    "        T3216ecfa1_1               o = new T3216ecfa1_1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa1_1.java",
			    "\n" +
			    "public class T3216ecfa1_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa1_2.java",
			    "\n" +
			    "public class T3216ecfa1_2 {\n" +
			    "    public team class Team3216ecfa1 {\n" +
			    "        public class Role3216ecfa1 playedBy T3216ecfa1_1 {\n" +
			    "            public abstract String getValue();\n" +
			    "            String getValue() -> String getValue() with {\n" +
			    "                result <- value\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(T3216ecfa1_1 as Role3216ecfa1 obj) {\n" +
			    "            return obj.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private String value = \"OK\";\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method of the superclass of the enclosing normal class is used in a parameter mapping
    // 3.2.16-otjld-enclosing-class-feature-access-2
    public void test3216_enclosingClassFeatureAccess2() {
       
       runConformTest(
            new String[] {
		"T3216ecfa2Main.java",
			    "\n" +
			    "public class T3216ecfa2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T3216ecfa2_3.Team3216ecfa2 t = new T3216ecfa2_3().new Team3216ecfa2();\n" +
			    "        T3216ecfa2_1               o = new T3216ecfa2_1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa2_1.java",
			    "\n" +
			    "public class T3216ecfa2_1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa2_2.java",
			    "\n" +
			    "public class T3216ecfa2_2 {\n" +
			    "    public String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa2_3.java",
			    "\n" +
			    "public class T3216ecfa2_3 extends T3216ecfa2_2 {\n" +
			    "    public team class Team3216ecfa2 {\n" +
			    "        public class Role3216ecfa2 playedBy T3216ecfa2_1 {\n" +
			    "            public abstract String getValue(String arg);\n" +
			    "            String getValue(String arg) -> String getValue(String arg) with {\n" +
			    "                result <- result,\n" +
			    "                getValueInternal() -> arg\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(T3216ecfa2_1 as Role3216ecfa2 obj) {\n" +
			    "            return obj.getValue(\"NOTOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a field of an indirect superclass of the enclosing normal class is used in a parameter mapping
    // 3.2.16-otjld-enclosing-class-feature-access-3
    public void test3216_enclosingClassFeatureAccess3() {
       
       runConformTest(
            new String[] {
		"T3216ecfa3Main.java",
			    "\n" +
			    "public class T3216ecfa3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T3216ecfa3_4.Team3216ecfa3 t = new T3216ecfa3_4().new Team3216ecfa3();\n" +
			    "        T3216ecfa3_1               o = new T3216ecfa3_1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa3_1.java",
			    "\n" +
			    "public class T3216ecfa3_1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa3_2.java",
			    "\n" +
			    "public class T3216ecfa3_2 {\n" +
			    "    protected String value = \"OK\";\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa3_3.java",
			    "\n" +
			    "public class T3216ecfa3_3 extends T3216ecfa3_2 {}\n" +
			    "    \n",
		"T3216ecfa3_4.java",
			    "\n" +
			    "public class T3216ecfa3_4 extends T3216ecfa3_3 {\n" +
			    "    public team class Team3216ecfa3 {\n" +
			    "    	public class Role3216ecfa3_1 playedBy T3216ecfa3_1 {\n" +
			    "            public String getValue(String arg) {\n" +
			    "                return \"NOTOK\";\n" +
			    "            }\n" +
			    "	}\n" +
			    "        public class Role3216ecfa3_2 extends Role3216ecfa3_1 playedBy T3216ecfa3_1 {\n" +
			    "            String getValue(String arg) => String getValue(String arg) with {\n" +
			    "                result <- result,\n" +
			    "                value -> arg\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(T3216ecfa3_1 as Role3216ecfa3_1 obj) {\n" +
			    "            return obj.getValue(\"NOTOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method of the superclass of the enclosing normal class is used in a result mapping
    // 3.2.16-otjld-enclosing-class-feature-access-4
    public void test3216_enclosingClassFeatureAccess4() {
       
       runConformTest(
            new String[] {
		"T3216ecfa4Main.java",
			    "\n" +
			    "public class T3216ecfa4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T3216ecfa4_3.Team3216ecfa4 t = new T3216ecfa4_3().new Team3216ecfa4();\n" +
			    "        T3216ecfa4_1               o = new T3216ecfa4_1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa4_1.java",
			    "\n" +
			    "public class T3216ecfa4_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa4_2.java",
			    "\n" +
			    "public class T3216ecfa4_2 {\n" +
			    "    String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3216ecfa4_3.java",
			    "\n" +
			    "public class T3216ecfa4_3 extends T3216ecfa4_2 {\n" +
			    "    public team class Team3216ecfa4 {\n" +
			    "        public class Role3216ecfa4 playedBy T3216ecfa4_1 {\n" +
			    "            public abstract String getValue();\n" +
			    "            String getValue() -> String getValue() with {\n" +
			    "                result <- T3216ecfa4_3.this.getValue()\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(T3216ecfa4_1 as Role3216ecfa4 obj) {\n" +
			    "            return obj.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a static method of the enclosing normal class is used in a parameter mapping
    // 3.2.17-otjld-static-feature-access-1
    public void test3217_staticFeatureAccess1() {
       
       runConformTest(
            new String[] {
		"T3217sfa1Main.java",
			    "\n" +
			    "public class T3217sfa1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T3217sfa1_2.Team3217sfa1 t = new T3217sfa1_2().new Team3217sfa1();\n" +
			    "        T3217sfa1_1               o = new T3217sfa1_1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3217sfa1_1.java",
			    "\n" +
			    "public class T3217sfa1_1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3217sfa1_2.java",
			    "\n" +
			    "public class T3217sfa1_2 {\n" +
			    "    public team class Team3217sfa1 {\n" +
			    "        public class Role3217sfa1 playedBy T3217sfa1_1 {\n" +
			    "            public abstract String getValue(String arg);\n" +
			    "            String getValue(String arg) -> String getValue(String arg) with {\n" +
			    "                result <- result,\n" +
			    "                getValueInternal() -> arg\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(T3217sfa1_1 as Role3217sfa1 obj) {\n" +
			    "            return obj.getValue(\"NOTOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private static String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a static method in another class in the same package is used in a result mapping
    // 3.2.17-otjld-static-feature-access-2
    public void test3217_staticFeatureAccess2() {
       
       runConformTest(
            new String[] {
		"T3217sfa2Main.java",
			    "\n" +
			    "public class T3217sfa2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3217sfa2 t = new Team3217sfa2();\n" +
			    "        T3217sfa2_1  o = new T3217sfa2_1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3217sfa2_1.java",
			    "\n" +
			    "public class T3217sfa2_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3217sfa2_2.java",
			    "\n" +
			    "class T3217sfa2_2 {\n" +
			    "    static String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3217sfa2.java",
			    "\n" +
			    "public team class Team3217sfa2 {\n" +
			    "    public class Role3217sfa2 playedBy T3217sfa2_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- T3217sfa2_2.getValue()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3217sfa2_1 as Role3217sfa2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a static field of the another nested class in the enclosing normal class is used in a parameter mapping
    // 3.2.17-otjld-static-feature-access-3
    public void test3217_staticFeatureAccess3() {
       
       runConformTest(
            new String[] {
		"T3217sfa3Main.java",
			    "\n" +
			    "public class T3217sfa3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T3217sfa3_2.Team3217sfa3 t = new T3217sfa3_2().new Team3217sfa3();\n" +
			    "        T3217sfa3_1               o = new T3217sfa3_1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3217sfa3_1.java",
			    "\n" +
			    "public class T3217sfa3_1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3217sfa3_2.java",
			    "\n" +
			    "public class T3217sfa3_2 {\n" +
			    "    public team class Team3217sfa3 {\n" +
			    "        public class Role3217sfa3 playedBy T3217sfa3_1 {\n" +
			    "            public abstract String getValue(String arg);\n" +
			    "            String getValue(String arg) -> String getValue(String arg) with {\n" +
			    "                result <- result,\n" +
			    "                Nested3217sfa3.value -> arg\n" +
			    "            }\n" +
			    "        }\n" +
			    "    \n" +
			    "        public String getValue(T3217sfa3_1 as Role3217sfa3 obj) {\n" +
			    "            return obj.getValue(\"NOTOK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private static class Nested3217sfa3 {\n" +
			    "        protected static String value = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a static field in another team class in the same package is used in a result mapping
    // 3.2.17-otjld-static-feature-access-4
    public void test3217_staticFeatureAccess4() {
       
       runConformTest(
            new String[] {
		"T3217sfa4Main.java",
			    "\n" +
			    "public class T3217sfa4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3217sfa4_2 t = new Team3217sfa4_2();\n" +
			    "        T3217sfa4      o = new T3217sfa4();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3217sfa4.java",
			    "\n" +
			    "public class T3217sfa4 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3217sfa4_1.java",
			    "\n" +
			    "public team class Team3217sfa4_1 {\n" +
			    "    public static String value = \"OK\";\n" +
			    "}\n" +
			    "    \n",
		"Team3217sfa4_2.java",
			    "\n" +
			    "public team class Team3217sfa4_2 {\n" +
			    "    public class Role3217sfa4 playedBy T3217sfa4 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- Team3217sfa4_1.value\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3217sfa4 as Role3217sfa4 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a static field in an inner class in another package is used in a parameter mapping
    // 3.2.17-otjld-static-feature-access-5
    public void test3217_staticFeatureAccess5() {
       
       runConformTest(
            new String[] {
		"p1/T3217sfa5Main.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T3217sfa5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3217sfa5 t = new Team3217sfa5();\n" +
			    "        T3217sfa5_1  o = new T3217sfa5_1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T3217sfa5_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T3217sfa5_1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p2/T3217sfa5_2.java",
			    "\n" +
			    "package p2;\n" +
			    "public class T3217sfa5_2 {\n" +
			    "    public static class Nested3217sfa5 {\n" +
			    "        public static String value = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/Team3217sfa5.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team3217sfa5 {\n" +
			    "    public class Role3217sfa5 playedBy T3217sfa5_1 {\n" +
			    "        public abstract String getValue(String arg);\n" +
			    "        String getValue(String arg) -> String getValue(String arg) with {\n" +
			    "            result <- result,\n" +
			    "            p2.T3217sfa5_2.Nested3217sfa5.value -> arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3217sfa5_1 as Role3217sfa5 obj) {\n" +
			    "        return obj.getValue(\"NOTOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the original parameter of the base method is used in a parameter mapping
    // 3.2.21-otjld-original-result-usage-1
    public void test3221_originalResultUsage1() {
        runNegativeTestMatching(
            new String[] {
		"Team3221oru1.java",
			    "\n" +
			    "public team class Team3221oru1 {\n" +
			    "    public class Role3221oru1 playedBy T3221oru1 {\n" +
			    "        public abstract void getValue(String arg);\n" +
			    "        void getValue(String arg) -> String getValue(String arg) with {\n" +
			    "            result + arg -> arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3221oru1.java",
			    "\n" +
			    "public class T3221oru1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3.2(c)");
    }

    // the original parameter of the base method is used in a parameter mapping
    // 3.2.21-otjld-original-result-usage-2
    public void test3221_originalResultUsage2() {
        runNegativeTestMatching(
            new String[] {
		"Team3221oru2.java",
			    "\n" +
			    "public team class Team3221oru2 {\n" +
			    "    public class Role3221oru2 playedBy T3221oru2 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue(String arg) with {\n" +
			    "            result <- \"OK\",\n" +
			    "            result -> arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3221oru2.java",
			    "\n" +
			    "public class T3221oru2 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3.2(c)");
    }

    // a callout binding has a return mapping though the base method has no return type
    // 3.2.22-otjld-no-return-type
    public void test3222_noReturnType() {
        runNegativeTest(
            new String[] {
		"T3222nrt.java",
			    "\n" +
			    "public class T3222nrt {\n" +
			    "    public void test(String arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team3222nrt.java",
			    "\n" +
			    "public team class Team3222nrt {\n" +
			    "    public class Role3222nrt playedBy T3222nrt {\n" +
			    "        public abstract String getValue(String arg);\n" +
			    "        String getValue(String arg) -> void test(String arg) with {\n" +
			    "            arg -> arg,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout binding has a result mapping that uses a field of the base class
    // 3.2.23-otjld-base-feature-access-1
    public void test3223_baseFeatureAccess1() {
        runNegativeTest(
            new String[] {
		"T3223bfa1.java",
			    "\n" +
			    "public class T3223bfa1 {\n" +
			    "    public String getValue1() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "    public String getValue2() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3223bfa1.java",
			    "\n" +
			    "public team class Team3223bfa1 {\n" +
			    "    public class Role3223bfa1 playedBy T3223bfa1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue2() with {\n" +
			    "            result <- getValue1()\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout binding has a result mapping that uses a method of the base class
    // 3.2.23-otjld-base-feature-access-2
    public void test3223_baseFeatureAccess2() {
        runNegativeTest(
            new String[] {
		"T3223bfa2.java",
			    "\n" +
			    "public class T3223bfa2 {\n" +
			    "    public String value = \"OK\";\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3223bfa2.java",
			    "\n" +
			    "public team class Team3223bfa2 {\n" +
			    "    public class Role3223bfa2 playedBy T3223bfa2 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        String getValue() => String getValue() with {\n" +
			    "            result <- value\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // in a result mapping, an instance of an anonymous class is created
    // 3.2.24-otjld-anonymous-class-instance
    public void test3224_anonymousClassInstance() {
       
       runConformTest(
            new String[] {
		"T3224aciMain.java",
			    "\n" +
			    "public class T3224aciMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3224aci t = new Team3224aci();\n" +
			    "        T3224aci    o = new T3224aci();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3224aci.java",
			    "\n" +
			    "public class T3224aci {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3224aci.java",
			    "\n" +
			    "public team class Team3224aci {\n" +
			    "    public class Role3224aci playedBy T3224aci {\n" +
			    "        public abstract Object getValue();\n" +
			    "        Object getValue() -> String getValue() with {\n" +
			    "            result <- new Object() {\n" +
			    "                public String toString() {\n" +
			    "                    return \"OK\";\n" +
			    "                }\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3224aci as Role3224aci obj) {\n" +
			    "        return obj.getValue().toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // in a callout binding, an object is lifted to a role class
    // 3.2.25-otjld_testbug-lifting-mapping-expression-1
    public void _testbug_test3225_liftingMappingExpression1() {
       
       runConformTest(
            new String[] {
		"T3225lme1Main.java",
			    "\n" +
			    "public class T3225lme1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3225lme1 t = new Team3225lme1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3225lme1.java",
			    "\n" +
			    "public class T3225lme1 { }\n" +
			    "    \n",
		"Team3225lme1.java",
			    "\n" +
			    "public team class Team3225lme1 {\n" +
			    "    public class Role3225lme1_1 playedBy Team3225lme1 {\n" +
			    "        public abstract String getValue(T3225lme1 obj);\n" +
			    "        String getValue(T3225lme1 obj) -> String getValue(Role3225lme1_2 obj);\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role3225lme1_2 playedBy T3225lme1 {\n" +
			    "        public String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private String getValue(Role3225lme1_2 obj) {\n" +
			    "        return obj.getValueInternal();\n" +
			    "    }\n" +
			    "\n" +
			    "    private String getValue(Team3225lme1 as Role3225lme1_1 obj) {\n" +
			    "        return obj.getValue(new T3225lme1());\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return getValue(this);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // in a callout binding with parameter mapping, an object is lifted to a role class
    // 3.2.25-otjld_testbug-lifting-mapping-expression-2
    public void _testbug_test3225_liftingMappingExpression2() {
       
       runConformTest(
            new String[] {
		"T3225lme2Main.java",
			    "\n" +
			    "public class T3225lme2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3225lme2 t = new Team3225lme2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3225lme2.java",
			    "\n" +
			    "public class T3225lme2 { }\n" +
			    "    \n",
		"Team3225lme2.java",
			    "\n" +
			    "public team class Team3225lme2 {\n" +
			    "    public class Role3225lme2_1 playedBy Team3225lme2 {\n" +
			    "        public abstract String getValue(T3225lme2 obj);\n" +
			    "        String getValue(T3225lme2 obj) -> String getValue(Role3225lme2_2 obj) with {\n" +
			    "            obj -> obj,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role3225lme2_2 playedBy T3225lme2 {\n" +
			    "        public String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    private String getValue(Role3225lme2_2 obj) {\n" +
			    "        return obj.getValueInternal();\n" +
			    "    }\n" +
			    "\n" +
			    "    private String getValue(Team3225lme2 as Role3225lme2_1 obj) {\n" +
			    "        return obj.getValue(new T3225lme2());\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return getValue(this);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // in a callout binding with parameter mapping, an object is lifted to a role class
    // 3.2.25-otjld-lifting-mapping-expression-3
    public void test3225_liftingMappingExpression3() {
        runNegativeTest(
            new String[] {
		"T3225lme3_1.java",
			    "\n" +
			    "public class T3225lme3_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3225lme3_1.java",
			    "\n" +
			    "public team class Team3225lme3_1 {\n" +
			    "    public class Role3225lme3_1 playedBy T3225lme3_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3225lme3_2.java",
			    "\n" +
			    "public class T3225lme3_2 {\n" +
			    "    public String getValue(final Team3225lme3_1 t, Role3225lme3_1<@t> r) {\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3225lme3_2.java",
			    "\n" +
			    "public team class Team3225lme3_2 {\n" +
			    "    public class Role3225lme3_2 playedBy T3225lme3_2 {\n" +
			    "        public abstract String getValue(final Team3225lme3_1 t, T3225lme3_1 obj);\n" +
			    "        String getValue(final Team3225lme3_1 t, T3225lme3_1 obj) -> String getValue(final Team3225lme3_1 t, Role3225lme3_1<@t> r) with {\n" +
			    "	    t   -> t,\n" +
			    "            obj -> r,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3225lme3_1 objA, T3225lme3_2 as Role3225lme3_2 objB) {\n" +
			    "        Team3225lme3_1 t = new Team3225lme3_1();\n" +
			    "\n" +
			    "        return objB.getValue(t, objA);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // syntax error in a callout binding: missing return type of base method
    // 3.2.25-otjld-lifting-mapping-expression-4
    public void test3225_liftingMappingExpression4() {
        runNegativeTest(
            new String[] {
		"T3225lme4_1.java",
			    "\n" +
			    "public class T3225lme4_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3225lme4_1.java",
			    "\n" +
			    "public team class Team3225lme4_1 {\n" +
			    "    public class Role3225lme4_1 playedBy T3225lme4_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // in a callout binding with parameter mapping, an object is lifted to a role class
    // 3.2.25-otjld-lifting-mapping-expression-5f
    public void test3225_liftingMappingExpression5f() {
        runNegativeTestMatching(
            new String[] {
		"Team3225lme5f_1.java",
			    "\n" +
			    "public team class Team3225lme5f_1 {\n" +
			    "    public class Role3225lme5f_1 playedBy T3225lme5f_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "	public Role3225lme5f_1 getRole(T3225lme5f_1 as Role3225lme5f_1 obj) {\n" +
			    "		return obj;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3225lme5f_1.java",
			    "\n" +
			    "public class T3225lme5f_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "6.2");
    }

    // in a callout binding with parameter mapping, an object is lifted to a role class
    // 3.2.25-otjld-lifting-mapping-expression-5
    public void test3225_liftingMappingExpression5() {
       
       runConformTest(
            new String[] {
		"T3225lme5Main.java",
			    "\n" +
			    "public class T3225lme5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3225lme5_2 t2 = new Team3225lme5_2();\n" +
			    "        System.out.print(t2.getValue(new T3225lme5_1(), new T3225lme5_2()));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3225lme5_1.java",
			    "\n" +
			    "public class T3225lme5_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3225lme5_1.java",
			    "\n" +
			    "public team class Team3225lme5_1 {\n" +
			    "    public class Role3225lme5_1 playedBy T3225lme5_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Role3225lme5_1 myGetRole(T3225lme5_1 as Role3225lme5_1 obj) {\n" +
			    "        return obj;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3225lme5_2.java",
			    "\n" +
			    "public class T3225lme5_2 {\n" +
			    "    public String getValue(final Team3225lme5_1 t, Role3225lme5_1<@t> r) {\n" +
			    "        return r.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3225lme5_2.java",
			    "\n" +
			    "public team class Team3225lme5_2 {\n" +
			    "    public class Role3225lme5_2 playedBy T3225lme5_2 {\n" +
			    "        public abstract String getValue(final Team3225lme5_1 t, T3225lme5_1 obj);\n" +
			    "        String getValue(final Team3225lme5_1 t, T3225lme5_1 obj) -> String getValue(final Team3225lme5_1 t, Role3225lme5_1<@t> r) with {\n" +
			    "            t  -> t,\n" +
			    "            t.myGetRole(obj) -> r,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3225lme5_1 objA, T3225lme5_2 as Role3225lme5_2 objB) {\n" +
			    "        Team3225lme5_1 t = new Team3225lme5_1();\n" +
			    "\n" +
			    "        return objB.getValue(t, objA);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // within a callout binding, an object is lowered to a role class
    // 3.2.26-otjld-lowering-mapping-expression-1
    public void test3226_loweringMappingExpression1() {
       
       runConformTest(
            new String[] {
		"T3226lme1Main.java",
			    "\n" +
			    "public class T3226lme1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3226lme1 t = new Team3226lme1();\n" +
			    "        T3226lme1_1  a = new T3226lme1_1();\n" +
			    "        T3226lme1_2  b = new T3226lme1_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(a, b));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3226lme1_1.java",
			    "\n" +
			    "public class T3226lme1_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3226lme1_2.java",
			    "\n" +
			    "public class T3226lme1_2 {\n" +
			    "    public String getValue(T3226lme1_1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3226lme1.java",
			    "\n" +
			    "public team class Team3226lme1 {\n" +
			    "    public class Role3226lme1_1 playedBy T3226lme1_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"NOTOK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "	public class Role3226lme1_0 {\n" +
			    "        public String getValue(Role3226lme1_1 obj) {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "	}\n" +
			    "    public class Role3226lme1_2\n" +
			    "			extends Role3226lme1_0\n" +
			    "			playedBy T3226lme1_2 {\n" +
			    "        String getValue(Role3226lme1_1 obj) => String getValue(T3226lme1_1 obj);\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3226lme1_1 as Role3226lme1_1 objA, T3226lme1_2 as Role3226lme1_2 objB) {\n" +
			    "        return objB.getValue(objA);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // within a callout binding, an object is lowered to a role class
    // 3.2.26-otjld-lowering-mapping-expression-2
    public void test3226_loweringMappingExpression2() {
       
       runConformTest(
            new String[] {
		"T3226lme2Main.java",
			    "\n" +
			    "public class T3226lme2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3226lme2 t = new Team3226lme2();\n" +
			    "        T3226lme2_1  a = new T3226lme2_1();\n" +
			    "        T3226lme2_2  b = new T3226lme2_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(a, b));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3226lme2_1.java",
			    "\n" +
			    "public class T3226lme2_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3226lme2_2.java",
			    "\n" +
			    "public class T3226lme2_2 {\n" +
			    "    public String getValue(T3226lme2_1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3226lme2.java",
			    "\n" +
			    "public team class Team3226lme2 {\n" +
			    "    public class Role3226lme2_1 playedBy T3226lme2_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"NOTOK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role3226lme2_2 playedBy T3226lme2_2 {\n" +
			    "        public abstract String getValue(Role3226lme2_1 obj);\n" +
			    "        String getValue(Role3226lme2_1 obj) -> String getValue(T3226lme2_1 obj) with {\n" +
			    "            result <- result,\n" +
			    "            obj -> obj\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3226lme2_1 as Role3226lme2_1 objA, T3226lme2_2 as Role3226lme2_2 objB) {\n" +
			    "        return objB.getValue(objA);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // within a callout binding, an object is lowered to a role class
    // 3.2.26-otjld-lowering-mapping-expression-2f
    public void test3226_loweringMappingExpression2f() {
        runNegativeTestMatching(
            new String[] {
		"Team3226lme2f.java",
			    "\n" +
			    "public team class Team3226lme2f {\n" +
			    "    public class Role3226lme2f_1 playedBy T3226lme2f_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"NOTOK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role3226lme2f_2 playedBy T3226lme2f_2 {\n" +
			    "        public abstract String getValue(Role3226lme2f_1 obj);\n" +
			    "        String getValue(Role3226lme2f_1 obj) => String getValue(T3226lme2f_1 obj) with {\n" +
			    "            result <- result,\n" +
			    "            obj -> obj\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3226lme2f_1 as Role3226lme2f_1 objA, T3226lme2f_2 as Role3226lme2f_2 objB) {\n" +
			    "        return objB.getValue(objA);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3226lme2f_1.java",
			    "\n" +
			    "public class T3226lme2f_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3226lme2f_2.java",
			    "\n" +
			    "public class T3226lme2f_2 {\n" +
			    "    public String getValue(T3226lme2f_1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3.1(e)");
    }

    // in a callout binding with parameter mapping, an object is lowered to a base class
    // 3.2.26-otjld-lowering-mapping-expression-3
    public void test3226_loweringMappingExpression3() {
       
       runConformTest(
            new String[] {
		"T3226lme3Main.java",
			    "\n" +
			    "public class T3226lme3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team3226lme3_1 t1 = new Team3226lme3_1();\n" +
			    "        Role3226lme3_1<@t1>  r  = t1.new Role3226lme3_1(\"NOTOK\");\n" +
			    "        Team3226lme3_2       t2 = new Team3226lme3_2();\n" +
			    "        T3226lme3_2          o  = new T3226lme3_2();\n" +
			    "\n" +
			    "        System.out.print(t2.getValue(o, t1, r));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3226lme3_1.java",
			    "\n" +
			    "public class T3226lme3_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3226lme3_2.java",
			    "\n" +
			    "public class T3226lme3_2 {\n" +
			    "    public String getValue(T3226lme3_1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3226lme3_1.java",
			    "\n" +
			    "public team class Team3226lme3_1 {\n" +
			    "    public class Role3226lme3_1 playedBy T3226lme3_1 {\n" +
			    "        private String value;\n" +
			    "\n" +
			    "        public Role3226lme3_1(String value) {\n" +
			    "            base();\n" +
			    "            this.value = value;\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- value\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3226lme3_2.java",
			    "\n" +
			    "public team class Team3226lme3_2 {\n" +
			    "    public class Role3226lme3_2 playedBy T3226lme3_2 {\n" +
			    "        public abstract String getValue(final Team3226lme3_1 t, Role3226lme3_1<@t> r);\n" +
			    "        String getValue(final Team3226lme3_1 t, Role3226lme3_1<@t> r) -> String getValue(T3226lme3_1 obj) with {\n" +
			    "            r -> obj,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3226lme3_2 as Role3226lme3_2 obj, final Team3226lme3_1 t, Role3226lme3_1<@t> r) {\n" +
			    "        return obj.getValue(t, r);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the expression in a parameter mapping cannot be converted
    // 3.2.27-otjld-expression-not-mappable-1
    public void test3227_expressionNotMappable1() {
        runNegativeTest(
            new String[] {
		"T3227enm1.java",
			    "\n" +
			    "public class T3227enm1 {\n" +
			    "    public void test(char arg) {}\n" +
			    "}\n" +
			    "    \n",
		"Team3227enm1.java",
			    "\n" +
			    "public team class Team3227enm1 {\n" +
			    "    public class Role3227enm1 playedBy T3227enm1 {\n" +
			    "        public abstract void test(int arg);\n" +
			    "        void test(int arg) -> void test(char arg) with {\n" +
			    "            arg -> arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the expression in a result mapping cannot be converted
    // 3.2.27-otjld-expression-not-mappable-2
    public void test3227_expressionNotMappable2() {
        runNegativeTest(
            new String[] {
		"T3227enm2.java",
			    "\n" +
			    "public class T3227enm2 {\n" +
			    "    public int test() {\n" +
			    "        return 0;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3227enm2.java",
			    "\n" +
			    "public team class Team3227enm2 {\n" +
			    "    public class Role3227enm2 playedBy T3227enm2 {\n" +
			    "        public long test() {\n" +
			    "            return 0;\n" +
			    "        }\n" +
			    "        long test() => int test() with {\n" +
			    "            result <- (double)result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the expression in a result mapping cannot be converted
    // 3.2.27-otjld-expression-not-mappable-3
    public void test3227_expressionNotMappable3() {
        runNegativeTest(
            new String[] {
		"T3227enm3.java",
			    "\n" +
			    "public class T3227enm3 {\n" +
			    "    public String test() {\n" +
			    "        return \"\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3227enm3.java",
			    "\n" +
			    "public team class Team3227enm3 {\n" +
			    "    public class Role3227enm3 playedBy T3227enm3 {\n" +
			    "        private Object getObject() {\n" +
			    "            return \"\";\n" +
			    "        }\n" +
			    "\n" +
			    "        public abstract String test();\n" +
			    "        String test() -> String test() with {\n" +
			    "            result <- getObject()\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the expression in a parameter mapping cannot be converted
    // 3.2.27-otjld-expression-not-mappable-4
    public void test3227_expressionNotMappable4() {
        runNegativeTest(
            new String[] {
		"T3227enm4.java",
			    "\n" +
			    "public class T3227enm4 {\n" +
			    "    public void test(double[][] values) {}\n" +
			    "}\n" +
			    "    \n",
		"Team3227enm4.java",
			    "\n" +
			    "public team class Team3227enm4 {\n" +
			    "    public class Role3227enm4 playedBy T3227enm4 {\n" +
			    "        public void test() {}\n" +
			    "        void test() => void test(double[][] arg) with {\n" +
			    "            new double[]{ 0.0 } -> arg\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the expression in a result mapping cannot be converted
    // 3.2.27-otjld-expression-not-mappable-5
    public void test3227_expressionNotMappable5() {
        runNegativeTest(
            new String[] {
		"T3227enm5_1.java",
			    "\n" +
			    "public class T3227enm5_1 {}\n" +
			    "    \n",
		"T3227enm5_2.java",
			    "\n" +
			    "public class T3227enm5_2 {}\n" +
			    "    \n",
		"T3227enm5_3.java",
			    "\n" +
			    "public class T3227enm5_3 {\n" +
			    "    public T3227enm5_1 test() {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3227enm5.java",
			    "\n" +
			    "public team class Team3227enm5 {\n" +
			    "    public class Role3227enm5_1 playedBy T3227enm5_2 {}\n" +
			    "\n" +
			    "    public class Role3227enm5_2 playedBy T3227enm5_3 {\n" +
			    "        public Role3227enm5_1 test() {\n" +
			    "            return null;\n" +
			    "        }\n" +
			    "        Role3227enm5_1 test() => T3227enm5_1 test() with {\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the expression in a parameter mapping cannot be converted
    // 3.2.27-otjld-expression-not-mappable-6
    public void test3227_expressionNotMappable6() {
        runNegativeTest(
            new String[] {
		"T3227enm6_1.java",
			    "\n" +
			    "public class T3227enm6_1 {}\n" +
			    "    \n",
		"T3227enm6_2.java",
			    "\n" +
			    "public class T3227enm6_2 {}\n" +
			    "    \n",
		"T3227enm6_3.java",
			    "\n" +
			    "public class T3227enm6_3 {\n" +
			    "    public void test(T3227enm6_1 obj) {}\n" +
			    "}\n" +
			    "    \n",
		"Team3227enm6.java",
			    "\n" +
			    "public team class Team3227enm6 {\n" +
			    "    public class Role3227enm6_1 playedBy T3227enm6_2 {}\n" +
			    "\n" +
			    "    public class Role3227enm6_2 playedBy T3227enm6_3 {\n" +
			    "        public abstract void test(Role3227enm6_1 obj);\n" +
			    "        void test(Role3227enm6_1 obj) => void test(T3227enm6_1 obj) with {\n" +
			    "            obj -> obj\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the callout-bound role method has a return type whereas the base method has none
    // 3.2.28-otjld-return-type-addition
    public void test3228_returnTypeAddition() {
       
       runConformTest(
            new String[] {
		"T3228rtaMain.java",
			    "\n" +
			    "public class T3228rtaMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3228rta t = new Team3228rta();\n" +
			    "        T3228rta    o = new T3228rta();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3228rta.java",
			    "\n" +
			    "public class T3228rta {\n" +
			    "    private String value = \"NOTOK\";\n" +
			    "    public void test(String arg) {\n" +
			    "        value = arg;\n" +
			    "    }\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3228rta.java",
			    "\n" +
			    "public team class Team3228rta {\n" +
			    "    public class Role3228rta playedBy T3228rta {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> void test(String arg) with {\n" +
			    "            result <- getValueInternal(),\n" +
			    "            \"OK\" -> arg\n" +
			    "        }\n" +
			    "\n" +
			    "        String getValueInternal() -> String getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3228rta as Role3228rta obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-bound role method has no return type whereas the base method has one
    // 3.2.29-otjld-return-type-removal-1
    public void test3229_returnTypeRemoval1() {
       
       runConformTest(
            new String[] {
		"T3229rtr1Main.java",
			    "\n" +
			    "public class T3229rtr1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3229rtr1 t = new Team3229rtr1();\n" +
			    "        T3229rtr1    o = new T3229rtr1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3229rtr1.java",
			    "\n" +
			    "public class T3229rtr1 {\n" +
			    "    private String value = \"NOTOK\";\n" +
			    "    public String test(String arg) {\n" +
			    "        value = arg;\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3229rtr1.java",
			    "\n" +
			    "public team class Team3229rtr1 {\n" +
			    "    public class Role3229rtr1 playedBy T3229rtr1 {\n" +
			    "        public abstract void test(String arg);\n" +
			    "        test -> test;\n" +
			    "\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3229rtr1 as Role3229rtr1 obj) {\n" +
			    "        obj.test(\"OK\");\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-bound role method has no return type whereas the base method has one, spurious result mapping
    // 3.2.29-otjld-return-type-removal-2f
    public void test3229_returnTypeRemoval2f() {
        runNegativeTestMatching(
            new String[] {
		"Team3229rtr2f.java",
			    "\n" +
			    "public team class Team3229rtr2f {\n" +
			    "    public class Role3229rtr2f playedBy T3229rtr2f {\n" +
			    "        private String value = \"NOTOK\";\n" +
			    "\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String arg) -> String test(String arg) with {\n" +
			    "            arg -> arg,\n" +
			    "            value <- result\n" +
			    "        }\n" +
			    "\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3229rtr2f as Role3229rtr2f obj) {\n" +
			    "        obj.test(\"OK\");\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3229rtr2f.java",
			    "\n" +
			    "public class T3229rtr2f {\n" +
			    "    public String test(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "3.2(c)");
    }

    // the callout-bound role method has no return type whereas the base method has one
    // 3.2.29-otjld-return-type-removal-2
    public void test3229_returnTypeRemoval2() {
       
       runConformTest(
            new String[] {
		"T3229rtr2Main.java",
			    "\n" +
			    "public class T3229rtr2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3229rtr2 t = new Team3229rtr2();\n" +
			    "        T3229rtr2    o = new T3229rtr2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3229rtr2.java",
			    "\n" +
			    "public class T3229rtr2 {\n" +
			    "    public String test(String arg) {\n" +
			    "        System.out.print(arg);\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3229rtr2.java",
			    "\n" +
			    "public team class Team3229rtr2 {\n" +
			    "    public class Role3229rtr2 playedBy T3229rtr2 {\n" +
			    "        private String value = \"K\";\n" +
			    "\n" +
			    "        public abstract void test(String arg);\n" +
			    "        void test(String arg) -> String test(String arg) with {\n" +
			    "            arg -> arg\n" +
			    "        }\n" +
			    "\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3229rtr2 as Role3229rtr2 obj) {\n" +
			    "        obj.test(\"O\");\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-binding uses this to access a role feature
    // 3.2.30-otjld-this-access-1
    public void test3230_thisAccess1() {
       
       runConformTest(
            new String[] {
		"T3230ta1Main.java",
			    "\n" +
			    "public class T3230ta1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3230ta1 t = new Team3230ta1();\n" +
			    "        T3230ta1    o = new T3230ta1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3230ta1.java",
			    "\n" +
			    "public class T3230ta1 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3230ta1.java",
			    "\n" +
			    "public team class Team3230ta1 {\n" +
			    "    public class Role3230ta1 playedBy T3230ta1 {\n" +
			    "        private String value = \"OK\";\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue(String arg) with {\n" +
			    "            this.value -> arg,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3230ta1 as Role3230ta1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-binding uses this to access a role feature - default visibility
    // 3.2.30-otjld-this-access-2a
    public void test3230_thisAccess2a() {
       
       runConformTest(
            new String[] {
		"T3230ta2aMain.java",
			    "\n" +
			    "public class T3230ta2aMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3230ta2a t = new Team3230ta2a();\n" +
			    "        T3230ta2a    o = new T3230ta2a();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3230ta2a.java",
			    "\n" +
			    "public class T3230ta2a {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3230ta2a.java",
			    "\n" +
			    "public team class Team3230ta2a {\n" +
			    "    public class Role3230ta2a playedBy T3230ta2a {\n" +
			    "        String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- this.getValueInternal()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3230ta2a as Role3230ta2a obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-binding uses this to access a private role feature
    // 3.2.30-otjld-this-access-2b
    public void test3230_thisAccess2b() {
       
       runConformTest(
            new String[] {
		"Team3230ta2b.java",
			    "\n" +
			    "public team class Team3230ta2b {\n" +
			    "    public class Role3230ta2b playedBy T3230ta2b {\n" +
			    "        private String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- this.getValueInternal()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3230ta2b as Role3230ta2b obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3230ta2b t = new Team3230ta2b();\n" +
			    "        T3230ta2b    o = new T3230ta2b();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3230ta2b.java",
			    "\n" +
			    "public class T3230ta2b {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-binding accesses a role feature unqualified
    // 3.2.30-otjld-this-access-2c
    public void test3230_thisAccess2c() {
       
       runConformTest(
            new String[] {
		"T3230ta2cMain.java",
			    "\n" +
			    "public class T3230ta2cMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3230ta2c t = new Team3230ta2c();\n" +
			    "        T3230ta2c    o = new T3230ta2c();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3230ta2c.java",
			    "\n" +
			    "public class T3230ta2c {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3230ta2c.java",
			    "\n" +
			    "public team class Team3230ta2c {\n" +
			    "    public class Role3230ta2c playedBy T3230ta2c {\n" +
			    "        private String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- getValueInternal()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3230ta2c as Role3230ta2c obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-bound role method uses this to access a role feature
    // 3.2.30-otjld-this-access-3
    public void test3230_thisAccess3() {
       
       runConformTest(
            new String[] {
		"T3230ta3Main.java",
			    "\n" +
			    "public class T3230ta3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3230ta3 t = new Team3230ta3();\n" +
			    "        T3230ta3    o = new T3230ta3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3230ta3.java",
			    "\n" +
			    "public class T3230ta3 {\n" +
			    "    public String value = \"NOTOK\";\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3230ta3.java",
			    "\n" +
			    "public team class Team3230ta3 {\n" +
			    "    public class Role3230ta3 playedBy T3230ta3 {\n" +
			    "        private String value = \"OK\";\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- this.value\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3230ta3 as Role3230ta3 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-binding uses super to access a feature of the superrole
    // 3.2.31-otjld-super-access-1
    public void test3231_superAccess1() {
       
       runConformTest(
            new String[] {
		"T3231sa1Main.java",
			    "\n" +
			    "public class T3231sa1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3231sa1 t = new Team3231sa1();\n" +
			    "        T3231sa1    o = new T3231sa1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3231sa1.java",
			    "\n" +
			    "public class T3231sa1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3231sa1.java",
			    "\n" +
			    "public team class Team3231sa1 {\n" +
			    "    public class Role3231sa1_1 playedBy T3231sa1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role3231sa1_2 extends Role3231sa1_1 {\n" +
			    "        String getValue() => String getValue() with {\n" +
			    "            result <- super.getValue()\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3231sa1 as Role3231sa1_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-binding uses super to access a feature of the superrole
    // 3.2.31-otjld-super-access-2
    public void test3231_superAccess2() {
       
       runConformTest(
            new String[] {
		"T3231sa2Main.java",
			    "\n" +
			    "public class T3231sa2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3231sa2_2 t = new Team3231sa2_2();\n" +
			    "        T3231sa2      o = new T3231sa2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3231sa2.java",
			    "\n" +
			    "public class T3231sa2 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3231sa2_1.java",
			    "\n" +
			    "public team class Team3231sa2_1 {\n" +
			    "    public class Role3231sa2_1 playedBy T3231sa2 {\n" +
			    "        protected String value = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3231sa2_2.java",
			    "\n" +
			    "public team class Team3231sa2_2 extends Team3231sa2_1 {\n" +
			    "    public class Role3231sa2_2 extends Role3231sa2_1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue(String arg) with {\n" +
			    "            super.value -> arg,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3231sa2 as Role3231sa2_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-bound role method uses super to access a feature of the superrole
    // 3.2.31-otjld-super-access-3
    public void test3231_superAccess3() {
       
       runConformTest(
            new String[] {
		"T3231sa3Main.java",
			    "\n" +
			    "public class T3231sa3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3231sa3_2 t = new Team3231sa3_2();\n" +
			    "        T3231sa3      o = new T3231sa3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3231sa3.java",
			    "\n" +
			    "public class T3231sa3 {\n" +
			    "    public String getValue(String arg) {\n" +
			    "        return arg;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3231sa3_1.java",
			    "\n" +
			    "public team class Team3231sa3_1 {\n" +
			    "    public class Role3231sa3_1 playedBy T3231sa3 {\n" +
			    "        private String value = \"OK\";\n" +
			    "\n" +
			    "        public String test(String arg) {\n" +
			    "            value = arg;\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3231sa3_2.java",
			    "\n" +
			    "public team class Team3231sa3_2 extends Team3231sa3_1 {\n" +
			    "    public class Role3231sa3_2 extends Role3231sa3_1 {\n" +
			    "        public abstract String test(String arg);\n" +
			    "        test -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3231sa3 as Role3231sa3_2 obj) {\n" +
			    "        return obj.test(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-binding uses tsuper to access a feature of the implicit superrole
    // 3.2.32-otjld-tsuper-access-1
    public void test3232_tsuperAccess1() {
       
       runConformTest(
            new String[] {
		"T3232ta1Main.java",
			    "\n" +
			    "public class T3232ta1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3232ta1_2 t = new Team3232ta1_2();\n" +
			    "        T3232ta1      o = new T3232ta1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3232ta1.java",
			    "\n" +
			    "public class T3232ta1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3232ta1_1.java",
			    "\n" +
			    "public team class Team3232ta1_1 {\n" +
			    "    public class Role3232ta1 playedBy T3232ta1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3232ta1 as Role3232ta1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3232ta1_2.java",
			    "\n" +
			    "public team class Team3232ta1_2 extends Team3232ta1_1 {\n" +
			    "    public class Role3232ta1 {\n" +
			    "        String getValue() => String getValue() with {\n" +
			    "            result <- tsuper.getValue()\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-bound role method tries to override a local method which uses tsuper
    // 3.2.32-otjld-tsuper-access-2
    public void test3232_tsuperAccess2() {
        runNegativeTest(
            new String[] {
		"T3232ta2.java",
			    "\n" +
			    "public class T3232ta2 {\n" +
			    "    public String test() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3232ta2_1.java",
			    "\n" +
			    "public team class Team3232ta2_1 {\n" +
			    "    public class Role3232ta2 playedBy T3232ta2 {\n" +
			    "        protected String test(String arg) {\n" +
			    "            return arg;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3232ta2_2.java",
			    "\n" +
			    "public team class Team3232ta2_2 extends Team3232ta2_1 {\n" +
			    "    public class Role3232ta2 {\n" +
			    "        protected String test(String arg) {\n" +
			    "            return tsuper.test(\"NOTOK\");\n" +
			    "        }\n" +
			    "        test => test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3232ta2 as Role3232ta2 obj) {\n" +
			    "        return obj.test(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the callout-binding uses base to access a feature of the base class
    // 3.2.33-otjld-base-use-1
    public void test3233_baseUse1() {
       
       runConformTest(
            new String[] {
		"Team3233bu1.java",
			    "\n" +
			    "public team class Team3233bu1 {\n" +
			    "    public class Role3233bu1 playedBy T3233bu1 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- base.value\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team3233bu1(T3233bu1 as Role3233bu1 r) {\n" +
			    "	System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "	new Team3233bu1(new T3233bu1());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3233bu1.java",
			    "\n" +
			    "public class T3233bu1 {\n" +
			    "    public String value = \"OK\";\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the callout-binding uses base to access a feature of the base class
    // 3.2.33-otjld-base-use-2
    public void test3233_baseUse2() {
       
       runConformTest(
            new String[] {
		"Team3233bu2.java",
			    "\n" +
			    "public team class Team3233bu2 {\n" +
			    "    public class Role3233bu2 playedBy T3233bu2 {\n" +
			    "        public abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- base.getValue()\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team3233bu2(T3233bu2 as Role3233bu2 r) {\n" +
			    "	System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "	new Team3233bu2(new T3233bu2());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3233bu2.java",
			    "\n" +
			    "public class T3233bu2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callout-binding without parameter mapping between parameter-conform role and base methods
    // 3.2.34-otjld-conform-parameter-mapping-1
    public void test3234_conformParameterMapping1() {
       
       runConformTest(
            new String[] {
		"T3234cpm1Main.java",
			    "\n" +
			    "public class T3234cpm1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3234cpm1_2 t  = new Team3234cpm1_2();\n" +
			    "        T3234cpm1_1    o1 = new T3234cpm1_1();\n" +
			    "        T3234cpm1_2    o2 = new T3234cpm1_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o1, o2));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3234cpm1_1.java",
			    "\n" +
			    "public class T3234cpm1_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3234cpm1_2.java",
			    "\n" +
			    "public class T3234cpm1_2 {\n" +
			    "    public String getValue(final Object arg1, T3234cpm1_1 arg2) {\n" +
			    "        return arg1.toString() + \"|\" + arg2.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3234cpm1_1.java",
			    "\n" +
			    "public team class Team3234cpm1_1 {\n" +
			    "    public class Role3234cpm1_1 playedBy T3234cpm1_1 {\n" +
			    "        String toString() => String toString() with {\n" +
			    "            result <- \"NOTOK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role3234cpm1_1 toRole(T3234cpm1_1 as Role3234cpm1_1 obj) {\n" +
			    "        return obj;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3234cpm1_2.java",
			    "\n" +
			    "public team class Team3234cpm1_2 {\n" +
			    "    public class Role3234cpm1_2 playedBy T3234cpm1_2 {\n" +
			    "        public abstract String getValue(final Team3234cpm1_1 arg1, Role3234cpm1_1<@arg1> arg2);\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3234cpm1_1 arg1, T3234cpm1_2 as Role3234cpm1_2 arg2) {\n" +
			    "        final Team3234cpm1_1 t = new Team3234cpm1_1();\n" +
			    "\n" +
			    "        return arg2.getValue(t, t.toRole(arg1));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK|OK");
    }

    // a callout-binding without parameter mapping between parameter-conform role and base methods
    // 3.2.34-otjld-conform-parameter-mapping-2
    public void test3234_conformParameterMapping2() {
       
       runConformTest(
            new String[] {
		"T3234cpm2Main.java",
			    "\n" +
			    "public class T3234cpm2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3234cpm2 t = new Team3234cpm2();\n" +
			    "        T3234cpm2    o = new T3234cpm2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o, new double[]{ 0.0 }));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3234cpm2.java",
			    "\n" +
			    "public class T3234cpm2 {\n" +
			    "    public String getValue(double[] objs) {\n" +
			    "        return String.valueOf(objs[0]);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3234cpm2.java",
			    "\n" +
			    "public team class Team3234cpm2 {\n" +
			    "    public class Role3234cpm2 playedBy T3234cpm2 {\n" +
			    "        public abstract String getValue(double[] objs) ;\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3234cpm2 as Role3234cpm2 obj, double[] values) {\n" +
			    "        return obj.getValue(values);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "0.0");
    }

    // a callout-binding without parameter mapping between parameter-conform role and base methods
    // 3.2.34-otjld-conform-parameter-mapping-3
    public void test3234_conformParameterMapping3() {
       
       runConformTest(
            new String[] {
		"T3234cpm3Main.java",
			    "\n" +
			    "public class T3234cpm3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3234cpm3 t = new Team3234cpm3();\n" +
			    "        T3234cpm3    o = new T3234cpm3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o, new String[]{ \"OK\"}));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3234cpm3.java",
			    "\n" +
			    "public class T3234cpm3 {\n" +
			    "    public String getValue(Object[] objs) {\n" +
			    "        return objs[0].toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3234cpm3.java",
			    "\n" +
			    "public team class Team3234cpm3 {\n" +
			    "    public class Role3234cpm3 playedBy T3234cpm3 {\n" +
			    "        public abstract Object getObject(String[] objs) ;\n" +
			    "        getObject -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3234cpm3 as Role3234cpm3 obj, String[] values) {\n" +
			    "        return obj.getObject(values).toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callout-binding without parameter mapping between parameter-conform role and base methods
    // 3.2.34-otjld-conform-parameter-mapping-4
    public void test3234_conformParameterMapping4() {
       
       runConformTest(
            new String[] {
		"T3234cpm4Main.java",
			    "\n" +
			    "public class T3234cpm4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3234cpm4_1 t1 = new Team3234cpm4_1();\n" +
			    "        Team3234cpm4_2 t2 = new Team3234cpm4_2();\n" +
			    "        T3234cpm4      o  = new T3234cpm4();\n" +
			    "\n" +
			    "        System.out.print(t2.getValue(t1, o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3234cpm4.java",
			    "\n" +
			    "public class T3234cpm4 {\n" +
			    "    public int getValue() {\n" +
			    "        return 0;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3234cpm4_1.java",
			    "\n" +
			    "public team class Team3234cpm4_1 {\n" +
			    "    public class Role3234cpm4_1 playedBy T3234cpm4 {\n" +
			    "        public abstract int getValue();\n" +
			    "        int getValue() -> int getValue() with {\n" +
			    "            result <- 1\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public int getValue(T3234cpm4 as Role3234cpm4_1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3234cpm4_2.java",
			    "\n" +
			    "public team class Team3234cpm4_2 {\n" +
			    "    public class Role3234cpm4_2 playedBy Team3234cpm4_1 {\n" +
			    "        public abstract double getValue(T3234cpm4 obj);\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public double getValue(Team3234cpm4_1 as Role3234cpm4_2 objA, T3234cpm4 objB) {\n" +
			    "        return objA.getValue(objB);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.0");
    }

    // a callout-binding without parameter mapping between parameter-conform role and base methods
    // 3.2.34-otjld-conform-parameter-mapping-5
    public void test3234_conformParameterMapping5() {
       
       runConformTest(
            new String[] {
		"T3234cpm5Main.java",
			    "\n" +
			    "public class T3234cpm5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3234cpm5 t = new Team3234cpm5();\n" +
			    "        T3234cpm5_2  o = new T3234cpm5_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3234cpm5_1.java",
			    "\n" +
			    "public class T3234cpm5_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3234cpm5_2.java",
			    "\n" +
			    "public class T3234cpm5_2 {\n" +
			    "    public T3234cpm5_1[] getObjects() {\n" +
			    "        return new T3234cpm5_1[] { new T3234cpm5_1() };\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3234cpm5.java",
			    "\n" +
			    "public team class Team3234cpm5 {\n" +
			    "    public class Role3234cpm5_1 playedBy T3234cpm5_1 {\n" +
			    "        String toString() => String toString() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role3234cpm5_2 playedBy T3234cpm5_2 {\n" +
			    "        public abstract Role3234cpm5_1[] getRoles();\n" +
			    "        getRoles -> getObjects;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3234cpm5_2 as Role3234cpm5_2 obj) {\n" +
			    "        return obj.getRoles()[0].toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callout-binding without parameter mapping between parameter-conform role and base methods
    // 3.2.34-otjld-conform-parameter-mapping-6
    public void test3234_conformParameterMapping6() {
       
       runConformTest(
            new String[] {
		"T3234cpm6Main.java",
			    "\n" +
			    "public class T3234cpm6Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3234cpm6  t    = new Team3234cpm6();\n" +
			    "        T3234cpm6_1[] objs = new T3234cpm6_1[]{ new T3234cpm6_1() };\n" +
			    "        T3234cpm6_2   o    = new T3234cpm6_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o, objs));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3234cpm6_1.java",
			    "\n" +
			    "public class T3234cpm6_1 {\n" +
			    "    public String toString() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3234cpm6_2.java",
			    "\n" +
			    "public class T3234cpm6_2 {\n" +
			    "    public String getValue(T3234cpm6_1[] values) {\n" +
			    "        return values[0].toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3234cpm6.java",
			    "\n" +
			    "public team class Team3234cpm6 {\n" +
			    "    protected class Role3234cpm6_1 playedBy T3234cpm6_1 {\n" +
			    "        String toString() => String toString() with {\n" +
			    "            result <- \"NOTOK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role3234cpm6_2 playedBy T3234cpm6_2 {\n" +
			    "        public abstract String getValue(Role3234cpm6_1[] objs);\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3234cpm6_2 as Role3234cpm6_2 obj, T3234cpm6_1 as Role3234cpm6_1 values[]) {\n" +
			    "        return obj.getValue(values);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a callout-binding without parameter mappings is defined between role and base methods with non-conforming return types
    // 3.2.36-otjld-nonconform-implicit-result-mapping-1
    public void test3236_nonconformImplicitResultMapping1() {
        runNegativeTest(
            new String[] {
		"T3236nirm1.java",
			    "\n" +
			    "public class T3236nirm1 {\n" +
			    "    public int getValue() {\n" +
			    "        return 0;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3236nirm1.java",
			    "\n" +
			    "public team class Team3236nirm1 {\n" +
			    "    public class Role3236nirm1 playedBy T3236nirm1 {\n" +
			    "        public abstract char getValue();\n" +
			    "        getValue -> getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout-binding without parameter mappings is defined between role and base methods with non-conforming return types
    // 3.2.36-otjld-nonconform-implicit-result-mapping-2
    public void test3236_nonconformImplicitResultMapping2() {
        runNegativeTest(
            new String[] {
		"T3236nirm2.java",
			    "\n" +
			    "public class T3236nirm2 {\n" +
			    "    public Object getObject() {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3236nirm2.java",
			    "\n" +
			    "public team class Team3236nirm2 {\n" +
			    "    public class Role3236nirm2 playedBy T3236nirm2 {\n" +
			    "        public abstract String getValue();\n" +
			    "        getValue -> getObject;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout-binding without parameter mappings is defined between role and base methods with non-conforming return types
    // 3.2.36-otjld-nonconform-implicit-result-mapping-3
    public void test3236_nonconformImplicitResultMapping3() {
        runNegativeTest(
            new String[] {
		"T3236nirm3.java",
			    "\n" +
			    "public class T3236nirm3 {\n" +
			    "    public double[][] getObjects() {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3236nirm3.java",
			    "\n" +
			    "public team class Team3236nirm3 {\n" +
			    "    public class Role3236nirm3 playedBy T3236nirm3 {\n" +
			    "        public double[] getValues() {\n" +
			    "            return null;\n" +
			    "        }\n" +
			    "        getValues => getObjects;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout-binding without parameter mappings is defined between role and base methods with non-conforming return types
    // 3.2.36-otjld-nonconform-implicit-result-mapping-4
    public void test3236_nonconformImplicitResultMapping4() {
        runNegativeTest(
            new String[] {
		"T3236nirm4_1.java",
			    "\n" +
			    "public class T3236nirm4_1 {}\n" +
			    "    \n",
		"T3236nirm4_2.java",
			    "\n" +
			    "public class T3236nirm4_2 {\n" +
			    "    public T3236nirm4_1[] getObjects() {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3236nirm4.java",
			    "\n" +
			    "public team class Team3236nirm4 {\n" +
			    "    public class Role3236nirm4_1 {}\n" +
			    "\n" +
			    "    public class Role3236nirm4_2 playedBy T3236nirm4_2 {\n" +
			    "        public Role3236nirm4_1[] getRoles() {\n" +
			    "            return null;\n" +
			    "        }\n" +
			    "        getRoles => getObjects;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout-binding without parameter mappings is defined between role and base methods with non-conforming return types
    // 3.2.36-otjld-nonconform-implicit-result-mapping-5
    public void test3236_nonconformImplicitResultMapping5() {
        runNegativeTest(
            new String[] {
		"T3236nirm5_1.java",
			    "\n" +
			    "public class T3236nirm5_1 {}\n" +
			    "    \n",
		"T3236nirm5_2.java",
			    "\n" +
			    "public class T3236nirm5_2 {}\n" +
			    "    \n",
		"Team3236nirm5_1.java",
			    "\n" +
			    "public team class Team3236nirm5_1 {\n" +
			    "    public class Role3236nirm5_1 playedBy T3236nirm5_1 {}\n" +
			    "\n" +
			    "    public Role3236nirm5_1 getNestedRole() {\n" +
			    "        return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3236nirm5_2.java",
			    "\n" +
			    "public team class Team3236nirm5_2 {\n" +
			    "    public class Role3236nirm5_2 playedBy Team3236nirm5_1 {\n" +
			    "        public abstract T3236nirm5_2 getObject();\n" +
			    "        getObject -> getNestedRole;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout-binding where the role method has more parameters than the base method
    // 3.2.37-otjld-role-has-more-parameters-1
    public void test3237_roleHasMoreParameters1() {
       
       runConformTest(
            new String[] {
		"T3237rhmp1Main.java",
			    "\n" +
			    "public class T3237rhmp1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3237rhmp1_2 t  = new Team3237rhmp1_2();\n" +
			    "        T3237rhmp1_1    o1 = new T3237rhmp1_1();\n" +
			    "        T3237rhmp1_2    o2 = new T3237rhmp1_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(0.0, o1, o2));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3237rhmp1_1.java",
			    "\n" +
			    "public class T3237rhmp1_1 {\n" +
			    "    public int getValue() {\n" +
			    "        return 1;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3237rhmp1_2.java",
			    "\n" +
			    "public class T3237rhmp1_2 {\n" +
			    "    public double getValue(double value, T3237rhmp1_1 obj) {\n" +
			    "        return value + obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3237rhmp1_1.java",
			    "\n" +
			    "public team class Team3237rhmp1_1 {\n" +
			    "    public class Role3237rhmp1_1 playedBy T3237rhmp1_1 {\n" +
			    "        public abstract int getValue();\n" +
			    "        int getValue() -> int getValue() with {\n" +
			    "            result <- 0\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role3237rhmp1_1 myGetRole(T3237rhmp1_1 as Role3237rhmp1_1 obj) {\n" +
			    "        return obj;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3237rhmp1_2.java",
			    "\n" +
			    "public team class Team3237rhmp1_2 {\n" +
			    "    public class Role3237rhmp1_2 playedBy T3237rhmp1_2 {\n" +
			    "        public abstract double getValue(double value, final Team3237rhmp1_1 t, Role3237rhmp1_1<@t> r);\n" +
			    "        double getValue(double value, final Team3237rhmp1_1 t, Role3237rhmp1_1<@t> r) -> double getValue(double value, T3237rhmp1_1 obj) with {\n" +
			    "            value -> value,\n" +
			    "            r -> obj,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public double getValue(double value, T3237rhmp1_1 objA, T3237rhmp1_2 as Role3237rhmp1_2 objB) {\n" +
			    "        final Team3237rhmp1_1 t = new Team3237rhmp1_1();\n" +
			    "\n" +
			    "        return objB.getValue(value, t, t.myGetRole(objA));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.0");
    }

    // a callout-binding where the role method has more parameters than the base method
    // 3.2.37-otjld-role-has-more-parameters-2
    public void test3237_roleHasMoreParameters2() {
       
       runConformTest(
            new String[] {
		"T3237rhmp2Main.java",
			    "\n" +
			    "public class T3237rhmp2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3237rhmp2 t = new Team3237rhmp2();\n" +
			    "        T3237rhmp2    o = new T3237rhmp2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3237rhmp2.java",
			    "\n" +
			    "public class T3237rhmp2 {\n" +
			    "    public String test(int value) {\n" +
			    "        return String.valueOf(value);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3237rhmp2.java",
			    "\n" +
			    "public team class Team3237rhmp2 {\n" +
			    "    public class Role3237rhmp2 playedBy T3237rhmp2 {\n" +
			    "        public abstract String test(int value1, String[] objs, int value2);\n" +
			    "        test -> test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3237rhmp2 as Role3237rhmp2 obj) {\n" +
			    "        return obj.test(1, null, 0);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1");
    }

    // a callout-binding where the role method has less parameters than the base method
    // 3.2.38-otjld-role-has-less-parameters
    public void test3238_roleHasLessParameters() {
       
       runConformTest(
            new String[] {
		"T3238rhlpMain.java",
			    "\n" +
			    "public class T3238rhlpMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3238rhlp t = new Team3238rhlp();\n" +
			    "        T3238rhlp    o = new T3238rhlp();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o, 1));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3238rhlp.java",
			    "\n" +
			    "public class T3238rhlp {\n" +
			    "    public String test(String txt, long value1, double value2) {\n" +
			    "        return txt + \"|\" + String.valueOf(value1) + \"|\" + String.valueOf(value2);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3238rhlp.java",
			    "\n" +
			    "public team class Team3238rhlp {\n" +
			    "    public class Role3238rhlp playedBy T3238rhlp {\n" +
			    "        public abstract String test(long value);\n" +
			    "        String test(long value) -> String test(String txt, long value1, double value2) with {\n" +
			    "            result <- result,\n" +
			    "            value -> value2,\n" +
			    "            0 -> value1,\n" +
			    "            \"OK\" -> txt\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T3238rhlp as Role3238rhlp obj, long value) {\n" +
			    "        return obj.test(value);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK|0|1.0");
    }

    // a callout-binding where the role method has a parameter order that differs from the base method
    // 3.2.39-otjld-different-parameter-order
    public void test3239_differentParameterOrder() {
       
       runConformTest(
            new String[] {
		"T3239dpoMain.java",
			    "\n" +
			    "public class T3239dpoMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team3239dpo t = new Team3239dpo();\n" +
			    "        T3239dpo    o = new T3239dpo();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o, 0, 2, new double[]{ 0.0, 1.0, 2.0 }));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T3239dpo.java",
			    "\n" +
			    "public class T3239dpo {\n" +
			    "    public double test(double[] values, int value1, int value2) {\n" +
			    "        return values[value1];\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team3239dpo.java",
			    "\n" +
			    "public team class Team3239dpo {\n" +
			    "    public class Role3239dpo playedBy T3239dpo {\n" +
			    "	protected abstract double test(int value1, int value2, double[] values);\n" +
			    "        double test(int value1, int value2, double[] values) -> double test(double[] values, int value1, int value2) with {\n" +
			    "            value1 -> value2,\n" +
			    "            value2 -> value1,\n" +
			    "            values -> values,\n" +
			    "            result <- result\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public double getValue(T3239dpo as Role3239dpo obj, int value1, int value2, double[] values) {\n" +
			    "        return obj.test(value1, value2, values);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.0");
    }

    // a base method appears in an after binding which returns an array type (WITNESS for TPX-278)
    // 3.2.40-otjld-after-binding-with-array-result-1
    public void test3240_afterBindingWithArrayResult1() {
       
       runConformTest(
            new String[] {
		"Team3240abwar1.java",
			    "\n" +
			    "public team class Team3240abwar1 {\n" +
			    "    public class R playedBy T3240abwar1 {\n" +
			    "        void rm() {\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        rm <- after test;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        (new Team3240abwar1()).activate();\n" +
			    "        T3240abwar1 b = new T3240abwar1();\n" +
			    "        b.test();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T3240abwar1.java",
			    "\n" +
			    "public class T3240abwar1 {\n" +
			    "    String[] test() { \n" +
			    "        System.out.print(\"O\");\n" +
			    "        return new String[0];\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a base method appears in an after binding with parameter mapping, base method returns an array type
    // 3.2.40-otjld-after-binding-with-array-result-2
    public void test3240_afterBindingWithArrayResult2() {
       
       runConformTest(
            new String[] {
		"Team3240abwar2.java",
			    "\n" +
			    "public team class Team3240abwar2 {\n" +
			    "    public class R playedBy T3240abwar2 {\n" +
			    "        void rm(String s1) {\n" +
			    "            System.out.print(s1+\"K\");\n" +
			    "        }\n" +
			    "        void rm(String s1) <- after String[] test() \n" +
			    "            with { s1 <- result[0] };\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        (new Team3240abwar2()).activate();\n" +
			    "        T3240abwar2 b = new T3240abwar2();\n" +
			    "        b.test();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T3240abwar2.java",
			    "\n" +
			    "public class T3240abwar2 {\n" +
			    "    String[] test() { \n" +
			    "        return new String[] {\"O\"};\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // an import is only used in a parameter mapping
    // 3.2.41-otjld-import-used-in-parammap-1
    public void test3241_importUsedInParammap1() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportUnusedParameter, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportUnusedPrivateMember, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportUnusedDeclaredThrownException, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportUnusedTypeArgumentsForMethodInvocation, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportUnusedLabel, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportUnusedImport, CompilerOptions.WARNING);
       customOptions.put(CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.WARNING);
       
       runConformTest(
            new String[] {
		"T3241iuip1Main.java",
			    "\n" +
			    "public class T3241iuip1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team3241iuip1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T3241iuip1_1.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T3241iuip1_1 {\n" +
			    "    public static String getO() { return \"O\"; }\n" +
			    "}\n" +
			    "    \n",
		"p1/T3241iuip1_2.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T3241iuip1_2 {\n" +
			    "    public String getK() { return \"K\"; }\n" +
			    "}\n" +
			    "    \n",
		"Team3241iuip1.java",
			    "\n" +
			    "import p1.T3241iuip1_1;\n" +
			    "import base p1.T3241iuip1_2;\n" +
			    "public team class Team3241iuip1 {\n" +
			    "    protected class R playedBy T3241iuip1_2 {\n" +
			    "        protected R() { base(); }\n" +
			    "        String getOK () -> String getK()\n" +
			    "            with { result <- T3241iuip1_1.getO()+result }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        System.out.print(new R().getOK());\n" +
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

    // a param map uses an non-externalized string
    // 3.2.41-otjld-import-used-in-parammap-2
    public void test3241_importUsedInParammap2() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.WARNING);
       
       runConformTest(
            new String[] {
		"T3241iuip2Main.java",
			    "\n" +
			    "public class T3241iuip2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team3241iuip2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/T3241iuip2_2.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T3241iuip2_2 {\n" +
			    "    public String getK() { return \"K\"; } //$NON-NLS-1$\n" +
			    "}\n" +
			    "    \n",
		"Team3241iuip2.java",
			    "\n" +
			    "import base p1.T3241iuip2_2;\n" +
			    "public team class Team3241iuip2 {\n" +
			    "    protected class R playedBy T3241iuip2_2 {\n" +
			    "        protected R() { base(); }\n" +
			    "        String getOK () -> String getK()\n" +
			    "            with { result <- \"O\"+result }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        System.out.print(new R().getOK());\n" +
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
}
