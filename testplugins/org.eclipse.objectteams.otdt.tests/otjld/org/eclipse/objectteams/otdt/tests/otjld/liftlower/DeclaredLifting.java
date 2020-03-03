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
package org.eclipse.objectteams.otdt.tests.otjld.liftlower;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

@SuppressWarnings("unchecked")
public class DeclaredLifting extends AbstractOTJLDTest {
	
	public DeclaredLifting(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test612_liftedExternalizedRoleFeatureAccess1"};
//		TESTS_NAMES = new String[] { "test6113_maximalSyntax1" };
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return DeclaredLifting.class;
	}

    // access to a feature of a role object that has been created using declared lifting
    // 6.1.1-otjld-lifted-role-feature-access-1
    public void test611_liftedRoleFeatureAccess1() {
       
       runConformTest(
            new String[] {
		"T611lrfa1Main.java",
			    "\n" +
			    "public class T611lrfa1Main {\n" +
			    "    @SuppressWarnings(\"roletypesyntax\")\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team611lrfa1 t = new Team611lrfa1();\n" +
			    "        t.Role611lrfa1_2   r = t.new Role611lrfa1_2(\"NOTOK\");\n" +
			    "\n" +
			    "        // should lower the role object as the external signature of the method is\n" +
			    "        //   Team611lrfa1.getValue(T611lrfa1 obj)\n" +
			    "        System.out.print(t.getValue(r));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T611lrfa1.java",
			    "\n" +
			    "public class T611lrfa1 {}\n" +
			    "    \n",
		"Team611lrfa1.java",
			    "\n" +
			    "public team class Team611lrfa1 {\n" +
			    "    public class Role611lrfa1_1 playedBy T611lrfa1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role611lrfa1_2 playedBy T611lrfa1 {\n" +
			    "        @SuppressWarnings(\"unused\")\n" +
			    "        private String value;\n" +
			    "\n" +
			    "        public Role611lrfa1_2(String value) {\n" +
			    "            base();\n" +
			    "            this.value = value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T611lrfa1 as Role611lrfa1_1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of a role object that has been created using declared lifting
    // 6.1.1-otjld-lifted-role-feature-access-2
    public void test611_liftedRoleFeatureAccess2() {
        runConformTest(
            new String[] {
		"Team611lrfa2_1.java",
			    "\n" +
			    "public team class Team611lrfa2_1 {\n" +
			    "    public class Role611lrfa2 playedBy I611lrfa2 {}\n" +
			    "}\n" +
			    "    \n",
		"I611lrfa2.java",
			    "\n" +
			    "public interface I611lrfa2 {}\n" +
			    "    \n",
		"T611lrfa2.java",
			    "\n" +
			    "public class T611lrfa2 implements I611lrfa2 {}\n" +
			    "    \n"
            },
            ""); // warning re binding-to-interface cannot be suppressed but does not harm
    }

    // access to a feature of a role object that has been created using declared lifting
    // 6.1.1-otjld-lifted-role-feature-access-3
    public void test611_liftedRoleFeatureAccess3() {
       
       runConformTest(
            new String[] {
		"T611lrfa3Main.java",
			    "\n" +
			    "public class T611lrfa3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team611lrfa3 t = new Team611lrfa3();\n" +
			    "        T611lrfa3_2  o = new T611lrfa3_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T611lrfa3_1.java",
			    "\n" +
			    "public abstract class T611lrfa3_1 {}\n" +
			    "    \n",
		"T611lrfa3_2.java",
			    "\n" +
			    "public class T611lrfa3_2 extends T611lrfa3_1 {}\n" +
			    "    \n",
		"Team611lrfa3.java",
			    "\n" +
			    "public team class Team611lrfa3 {\n" +
			    "    protected class Role611lrfa3_1 playedBy T611lrfa3_1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    protected class Role611lrfa3_2 extends Role611lrfa3_1 {}\n" +
			    "\n" +
			    "    public String getValue(T611lrfa3_1 as Role611lrfa3_2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // access to a feature of a role object that has been created using declared lifting
    // 6.1.1-otjld-lifted-role-feature-access-4
    public void test611_liftedRoleFeatureAccess4() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportInvalidJavadoc, CompilerOptions.WARNING);
       
       runConformTest(
            new String[] {
		"T611lrfa4Main.java",
			    "\n" +
			    "public class T611lrfa4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team611lrfa4_2 t = new Team611lrfa4_2();\n" +
			    "        T611lrfa4      o = new T611lrfa4();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T611lrfa4.java",
			    "\n" +
			    "public class T611lrfa4 {}\n" +
			    "    \n",
		"Team611lrfa4_1.java",
			    "\n" +
			    "public abstract team class Team611lrfa4_1 {\n" +
			    "    public abstract class Role611lrfa4_1 playedBy T611lrfa4 {\n" +
			    "        public String value = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team611lrfa4_2.java",
			    "\n" +
			    "public team class Team611lrfa4_2 extends Team611lrfa4_1 {\n" +
			    "    public class Role611lrfa4_2 extends Role611lrfa4_1 {}\n" +
			    "\n" +
			    "    /**\n" +
			    "     * Intentionally omitted return tag, to trigger one warning so we can detect\n" +
			    "     * that _no_ warning is reported against obj (see Trac #205).\n" +
			    "     * @param obj\n" +
			    "     */\n" +
			    "    public String getValue(T611lrfa4 as Role611lrfa4_2 obj) {\n" +
			    "        return obj.value;\n" +
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

    // access to a feature of an externalized role object that has been created using declared lifting
    // 6.1.2-otjld-lifted-externalized-role-feature-access-1
    public void test612_liftedExternalizedRoleFeatureAccess1() {
        runNegativeTestMatching(
            new String[] {
		"Team612lerfa1.java",
			    "\n" +
			    "public team class Team612lerfa1 {\n" +
			    "    public class Role612lerfa1 playedBy T612lerfa1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public static String getValue(final Team612lerfa1 t, T612lerfa1 as t.Role612lerfa1_1 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T612lerfa1.java",
			    "\n" +
			    "public class T612lerfa1 {}\n" +
			    "    \n"
            },
            "2.3.2(a)");
    }

    // access to a feature of an externalized role object that has been created using declared lifting
    // 6.1.2-otjld-lifted-externalized-role-feature-access-2
    public void test612_liftedExternalizedRoleFeatureAccess2() {
        runNegativeTestMatching(
            new String[] {
		"Team612lerfa2_2.java",
			    "\n" +
			    "public team class Team612lerfa2_2 {\n" +
			    "    public String getValue(final Team612lerfa2_1 t, T612lerfa2 as t.Role612lerfa2 obj) {\n" +
			    "        return obj.value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T612lerfa2.java",
			    "\n" +
			    "public class T612lerfa2 {}\n" +
			    "    \n",
		"Team612lerfa2_1.java",
			    "\n" +
			    "public team class Team612lerfa2_1 {\n" +
			    "    protected class Role612lerfa2 playedBy T612lerfa2 {\n" +
			    "        String value = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.3.2(a)");
    }

    // a team implements a method of an interface using declared lifting for the parameter
    // 6.1.3-otjld-interface-implementation-with-declared-lifting
    public void test613_interfaceImplementationWithDeclaredLifting() {
       
       runConformTest(
            new String[] {
		"T613iiwdlMain.java",
			    "\n" +
			    "public class T613iiwdlMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        I613iiwdl i = new Team613iiwdl();\n" +
			    "        T613iiwdl o = new T613iiwdl();\n" +
			    "\n" +
			    "        System.out.print(i.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T613iiwdl.java",
			    "\n" +
			    "public class T613iiwdl {\n" +
			    "    public String toString() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"I613iiwdl.java",
			    "\n" +
			    "public interface I613iiwdl {\n" +
			    "    String getValue(T613iiwdl obj);\n" +
			    "}\n" +
			    "    \n",
		"Team613iiwdl.java",
			    "\n" +
			    "public team class Team613iiwdl implements I613iiwdl {\n" +
			    "    public class Role613iiwdl playedBy T613iiwdl {\n" +
			    "        public String toString() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T613iiwdl as Role613iiwdl obj) {\n" +
			    "        return obj.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // the as construct is used outside of the parameter list of a team method
    // 6.1.4-otjld-illegal-use-of-as-construct-1
    public void test614_illegalUseOfAsConstruct1() {
        runNegativeTest(
            new String[] {
		"T614iuoac1.java",
			    "\n" +
			    "public class T614iuoac1 {}\n" +
			    "    \n",
		"Team614iuoac1.java",
			    "\n" +
			    "public team class Team614iuoac1 {\n" +
			    "    public class Role614iuoac1 playedBy T614iuoac1 {}\n" +
			    "\n" +
			    "    public T614iuoac1 as Role614iuoac1 obj;\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the as construct is used outside of the parameter list of a team method
    // 6.1.4-otjld-illegal-use-of-as-construct-2
    public void test614_illegalUseOfAsConstruct2() {
        runNegativeTest(
            new String[] {
		"T614iuoac2.java",
			    "\n" +
			    "public class T614iuoac2 {}\n" +
			    "    \n",
		"Team614iuoac2.java",
			    "\n" +
			    "public team class Team614iuoac2 {\n" +
			    "    public class Role614iuoac2 playedBy T614iuoac2 {}\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        T614iuoac2 as Role614iuoac2 obj;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the as construct is used outside of the parameter list of a team method
    // 6.1.4-otjld-illegal-use-of-as-construct-3
    public void test614_illegalUseOfAsConstruct3() {
        runNegativeTest(
            new String[] {
		"T614iuoac3.java",
			    "\n" +
			    "public class T614iuoac3 {}\n" +
			    "    \n",
		"Team614iuoac3.java",
			    "\n" +
			    "public team class Team614iuoac3 {\n" +
			    "    public class Role614iuoac3 playedBy T614iuoac3 {}\n" +
			    "\n" +
			    "    public T614iuoac3 as Role614iuoac3 test() {\n" +
			    "         return null;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the as construct is used outside of the parameter list of a team method
    // 6.1.4-otjld-illegal-use-of-as-construct-4
    public void test614_illegalUseOfAsConstruct4() {
        runNegativeTest(
            new String[] {
		"T614iuoac4.java",
			    "\n" +
			    "public class T614iuoac4 {}\n" +
			    "    \n",
		"Team614iuoac4.java",
			    "\n" +
			    "public team class Team614iuoac4 {\n" +
			    "    public class Role614iuoac4 playedBy T614iuoac4 {}\n" +
			    "\n" +
			    "    public void test(T614iuoac4 obj) {\n" +
			    "        ((T614iuoac4 as Role614iuoac4)obj).toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the as construct is used outside of the parameter list of a team method
    // 6.1.4-otjld-illegal-use-of-as-construct-5
    public void test614_illegalUseOfAsConstruct5() {
        runNegativeTest(
            new String[] {
		"T614iuoac5.java",
			    "\n" +
			    "public class T614iuoac5 {}\n" +
			    "    \n",
		"Team614iuoac5.java",
			    "\n" +
			    "public team class Team614iuoac5 {\n" +
			    "    public class Role614iuoac5 playedBy T614iuoac5 {}\n" +
			    "\n" +
			    "    public void test(T614iuoac5 obj) {\n" +
			    "        if (obj instanceof T614iuoac5 as Role614iuoac5) {\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the as construct is used outside of the parameter list of a team method
    // 6.1.4-otjld-illegal-use-of-as-construct-6
    public void test614_illegalUseOfAsConstruct6() {
        runNegativeTest(
            new String[] {
		"T614iuoac6.java",
			    "\n" +
			    "public class T614iuoac6 {}\n" +
			    "    \n",
		"Team614iuoac6.java",
			    "\n" +
			    "public team class Team614iuoac6 {\n" +
			    "    public class Role614iuoac6 playedBy T614iuoac6 {}\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        T614iuoac6 obj = new T614iuoac6 as Role614iuoac6();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the as construct is used outside of the parameter list of a team method
    // 6.1.4-otjld-illegal-use-of-as-construct-7
    public void test614_illegalUseOfAsConstruct7() {
        runNegativeTest(
            new String[] {
		"T614iuoac7.java",
			    "\n" +
			    "public class T614iuoac7 {}\n" +
			    "    \n",
		"Team614iuoac7.java",
			    "\n" +
			    "public team class Team614iuoac7 {\n" +
			    "    public class Role614iuoac7_1 playedBy T614iuoac7 {}\n" +
			    "\n" +
			    "    public class Role614iuoac7_2 {\n" +
			    "        public void test(T614iuoac7 as Role614iuoac7_1 obj) {\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the as construct is used outside of the parameter list of a team method
    // 6.1.4-otjld-illegal-use-of-as-construct-8
    public void test614_illegalUseOfAsConstruct8() {
        runNegativeTest(
            new String[] {
		"T614iuoac8.java",
			    "\n" +
			    "public class T614iuoac8 {}\n" +
			    "    \n",
		"Team614iuoac8.java",
			    "\n" +
			    "public class Team614iuoac8 {\n" +
			    "    public class Role614iuoac8_1 {}\n" +
			    "\n" +
			    "    public void test(T614iuoac8 as Role614iuoac8_1 obj) {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the right side of an as construct does not specify a valid role class
    // 6.1.5-otjld-invalid-role-class-for-lifting-1
    public void test615_invalidRoleClassForLifting1() {
        runNegativeTest(
            new String[] {
		"T615ircfl1.java",
			    "\n" +
			    "public class T615ircfl1 {}\n" +
			    "    \n",
		"Team615ircfl1.java",
			    "\n" +
			    "public team class Team615ircfl1 {\n" +
			    "    public class Role615ircfl1_1 playedBy T615ircfl1 {}\n" +
			    "\n" +
			    "    public class Role615ircfl1_2 {}\n" +
			    "\n" +
			    "    public void test(T615ircfl1 as Role615ircfl1_2 obj) {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the right side of an as construct does not specify a valid role class
    // 6.1.5-otjld-invalid-role-class-for-lifting-2
    public void test615_invalidRoleClassForLifting2() {
        runNegativeTest(
            new String[] {
		"T615ircfl2_1.java",
			    "\n" +
			    "public class T615ircfl2_1 {}\n" +
			    "    \n",
		"T615ircfl2_2.java",
			    "\n" +
			    "public class T615ircfl2_2 extends T615ircfl2_1 {}\n" +
			    "    \n",
		"Team615ircfl2.java",
			    "\n" +
			    "public team class Team615ircfl2 {\n" +
			    "    public class Role615ircfl2 playedBy T615ircfl2_1 {}\n" +
			    "\n" +
			    "    public void test(T615ircfl2_1 as T615ircfl2_2 obj) {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the right side of an as construct does not specify a valid role class
    // 6.1.5-otjld-invalid-role-class-for-lifting-3
    public void test615_invalidRoleClassForLifting3() {
        runNegativeTest(
            new String[] {
		"T615ircfl3.java",
			    "\n" +
			    "public class T615ircfl3 {}\n" +
			    "    \n",
		"Team615ircfl3_1.java",
			    "\n" +
			    "public team class Team615ircfl3_1 {\n" +
			    "    public class Role615ircfl3 playedBy T615ircfl3 {}\n" +
			    "}\n" +
			    "    \n",
		"Team615ircfl3_2.java",
			    "\n" +
			    "public team class Team615ircfl3_2 extends Team615ircfl3_1 {\n" +
			    "    public void test(T615ircfl3 as Team615ircfl3_1 obj) {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the left side of an as construct does not specify a base class valid for this role class
    // 6.1.6-otjld-invalid-base-class-for-lifting-1
    public void test616_invalidBaseClassForLifting1() {
        runNegativeTest(
            new String[] {
		"T616ibcfl1_1.java",
			    "\n" +
			    "public class T616ibcfl1_1 {}\n" +
			    "    \n",
		"T616ibcfl1_2.java",
			    "\n" +
			    "public class T616ibcfl1_2 extends T616ibcfl1_1 {}\n" +
			    "    \n",
		"Team616ibcfl1.java",
			    "\n" +
			    "public team class Team616ibcfl1 {\n" +
			    "    public class Role616ibcfl1 playedBy T616ibcfl1_2 {}\n" +
			    "\n" +
			    "    public void test(T616ibcfl1_1 as Role616ibcfl1 obj) {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the left side of an as construct does not specify a base class valid for this role class
    // 6.1.6-otjld-invalid-base-class-for-lifting-2
    public void test616_invalidBaseClassForLifting2() {
        runNegativeTest(
            new String[] {
		"T616ibcfl2.java",
			    "\n" +
			    "public class T616ibcfl2 {}\n" +
			    "    \n",
		"Team616ibcfl2.java",
			    "\n" +
			    "public team class Team616ibcfl2 {\n" +
			    "    public class Role616ibcfl2_1 playedBy T616ibcfl2 {}\n" +
			    "    public class Role616ibcfl2_2 playedBy T616ibcfl2 {}\n" +
			    "\n" +
			    "    public void test(Role616ibcfl2_1 as Role616ibcfl2_2 obj) {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the left side of an as construct does not specify a base class valid for this role class
    // 6.1.6-otjld-invalid-base-class-for-lifting-3
    public void test616_invalidBaseClassForLifting3() {
        runNegativeTest(
            new String[] {
		"T616ibcfl3.java",
			    "\n" +
			    "public class T616ibcfl3 {}\n" +
			    "    \n",
		"Team616ibcfl3.java",
			    "\n" +
			    "public team class Team616ibcfl3 {\n" +
			    "    public class Role616ibcfl3_1 playedBy T616ibcfl3 {}\n" +
			    "    public class Role616ibcfl3_2 extends Role616ibcfl3_1 {}\n" +
			    "\n" +
			    "    public void test(Role616ibcfl3_1 as Role616ibcfl3_2 obj) {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // the left side of an as construct does not specify a base class valid for this role class
    // 6.1.6-otjld-invalid-base-class-for-lifting-4
    public void test616_invalidBaseClassForLifting4() {
        runNegativeTest(
            new String[] {
		"T616ibcfl4.java",
			    "\n" +
			    "public class T616ibcfl4 {}\n" +
			    "    \n",
		"Team616ibcfl4_1.java",
			    "\n" +
			    "public team class Team616ibcfl4_1 {\n" +
			    "    public class Role616ibcfl4 playedBy T616ibcfl4 {}\n" +
			    "}\n" +
			    "    \n",
		"Team616ibcfl4_2.java",
			    "\n" +
			    "public team class Team616ibcfl4_2 extends Team616ibcfl4_1 {\n" +
			    "    public void test(Team616ibcfl4_1 as Role616ibcfl4 obj) {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a nested team has a method with declared lifting
    // 6.1.7-otjld-declared-lifting-in-nested-team-1
    public void test617_declaredLiftingInNestedTeam1() {
       
       runConformTest(
            new String[] {
		"Team617dlint1.java",
			    "\n" +
			    "public team class Team617dlint1 {\n" +
			    "    public team class InnerTeam {\n" +
			    "        protected class InnerRole playedBy T617dlint1 {\n" +
			    "            protected void foo() {\n" +
			    "                System.out.print(\"K\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public void test(T617dlint1 as InnerRole o) {\n" +
			    "            o.foo();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public InnerTeam getTeam() {\n" +
			    "        return new InnerTeam();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T617dlint1 b = new T617dlint1();\n" +
			    "        b.test();\n" +
			    "        final Team617dlint1 t = new Team617dlint1();\n" +
			    "        InnerTeam<@t> it = t.getTeam();\n" +
			    "        it.test(b);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T617dlint1.java",
			    "\n" +
			    "public class T617dlint1 {\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a nested team has a method with declared lifting -- base type anchored to "base"
    // 6.1.7-otjld-declared-lifting-in-nested-team-2
    public void test617_declaredLiftingInNestedTeam2() {
       
       runConformTest(
            new String[] {
		"Team617dlint2_2.java",
			    "\n" +
			    "public team class Team617dlint2_2 {\n" +
			    "    public team class InnerTeam playedBy Team617dlint2_1 {\n" +
			    "        protected class InnerRole playedBy R<@base> {\n" +
			    "            protected void foo() {\n" +
			    "                System.out.print(\"K\");\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public abstract R<@base> getBaseR();\n" +
			    "        getBaseR -> getR;\n" +
			    "        public void test(R<@base> r, R<@base> as InnerRole o) {\n" +
			    "            r.bar();\n" +
			    "            o.foo();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test(Team617dlint2_1 as InnerTeam it) {\n" +
			    "        it.test(it.getBaseR(), it.getBaseR());\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team617dlint2_2 t = new Team617dlint2_2();\n" +
			    "        Team617dlint2_1 bt = new Team617dlint2_1();\n" +
			    "        t.test(bt);\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team617dlint2_1.java",
			    "\n" +
			    "public team class Team617dlint2_1 {\n" +
			    "    public class R {\n" +
			    "        public void bar() {\n" +
			    "            System.out.print(\"I\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    R getR() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OOIK");
    }

    // A catch clause uses declared lifting
    // 6.1.8-otjld-exception-lifting-1
    public void test618_exceptionLifting1() {
       
       runConformTest(
            new String[] {
		"Team618el1.java",
			    "\n" +
			    "public team class Team618el1 {\n" +
			    "    protected class R playedBy T618el1 {\n" +
			    "        protected void test() -> void test();\n" +
			    "    }\n" +
			    "    protected class E playedBy E618el1 {\n" +
			    "        public String toString() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team618el1() {\n" +
			    "        R r = new R(new T618el1());\n" +
			    "        try {\n" +
			    "            r.test();\n" +
			    "        } catch (E618el1 as E ex) {\n" +
			    "            System.out.print(\"CAUGHT:\"+ex);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team618el1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"E618el1.java",
			    "\n" +
			    "@SuppressWarnings(\"serial\")\n" +
			    "public class E618el1 extends Exception {\n" +
			    "    public E618el1(String msg) {\n" +
			    "        super(msg);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T618el1.java",
			    "\n" +
			    "public class T618el1 {\n" +
			    "    void test () throws E618el1 {\n" +
			    "        throw new E618el1(\"NOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "CAUGHT:OK");
    }

    // A catch clause uses declared lifting, rethrowing
    // 6.1.8-otjld-exception-lifting-2
    public void test618_exceptionLifting2() {
       
       runConformTest(
            new String[] {
		"Team618el2.java",
			    "\n" +
			    "public team class Team618el2 {\n" +
			    "    protected class R playedBy T618el2 {\n" +
			    "        protected abstract void test() throws E618el2;\n" +
			    "        test -> test;\n" +
			    "    }\n" +
			    "    protected class E playedBy E618el2 {\n" +
			    "        public String toString() {\n" +
			    "            return \"NOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public void test() throws E618el2\n" +
			    "    {\n" +
			    "        R r = new R(new T618el2());\n" +
			    "        try {\n" +
			    "            r.test();\n" +
			    "        } catch (E618el2 as E ex) {\n" +
			    "            System.out.print(\"CAUGHT(1)\");\n" +
			    "            throw ex;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        try {\n" +
			    "            new Team618el2().test();\n" +
			    "        } catch (E618el2 ex) {\n" +
			    "            System.out.print(\"CAUGHT(2)\"+ex);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"E618el2.java",
			    "\n" +
			    "@SuppressWarnings(\"serial\")\n" +
			    "public class E618el2 extends Exception {\n" +
			    "    public E618el2(String msg) {\n" +
			    "        super(msg);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T618el2.java",
			    "\n" +
			    "public class T618el2 {\n" +
			    "    void test () throws E618el2 {\n" +
			    "        throw new E618el2(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "CAUGHT(1)CAUGHT(2)E618el2: OK");
    }

    // A catch clause within a role uses declared lifting
    // 6.1.8-otjld-exception-lifting-3
    public void test618_exceptionLifting3() {
       
       runConformTest(
            new String[] {
		"Team618el3.java",
			    "\n" +
			    "public team class Team618el3 {\n" +
			    "    protected class R playedBy T618el3 {\n" +
			    "        protected void doit() {\n" +
			    "            try {\n" +
			    "                test();\n" +
			    "            } catch (E618el3 as E ex) {\n" +
			    "                System.out.print(\"CAUGHT:\"+ex);\n" +
			    "            }\n" +
			    "        }\n" +
			    "        protected abstract void test() throws E618el3;\n" +
			    "        void test() -> void test();\n" +
			    "    }\n" +
			    "    protected class E playedBy E618el3 {\n" +
			    "        public String toString() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team618el3() {\n" +
			    "        R r = new R(new T618el3());\n" +
			    "        r.doit();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team618el3();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"E618el3.java",
			    "\n" +
			    "@SuppressWarnings(\"serial\")\n" +
			    "public class E618el3 extends Exception {\n" +
			    "    public E618el3(String msg) {\n" +
			    "        super(msg);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T618el3.java",
			    "\n" +
			    "public class T618el3 {\n" +
			    "    void test () throws E618el3 {\n" +
			    "        throw new E618el3(\"NOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "CAUGHT:OK");
    }

    // a catch clause outside a team tries to lift its argument
    // 6.1.8-otjld-exception-lifting-4
    public void test618_exceptionLifting4() {
        runNegativeTestMatching(
            new String[] {
		"T618el4.java",
			    "\n" +
			    "public class T618el4 {\n" +
			    "    void foo() {\n" +
			    "        try {\n" +
			    "            System.out.println(\"nothing\");\n" +
			    "        } catch (Object as String o) {\n" +
			    "            // nop\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "A.0.1");
    }

    // a team ctor used both declared lifting and a role anchored to another argument
    // 6.1.9-otjld-declared-lifting-and-externalized-role-1
    public void test619_declaredLiftingAndExternalizedRole1() {
       
       runConformTest(
            new String[] {
		"Team619dlaer1_2.java",
			    "\n" +
			    "public team class Team619dlaer1_2 extends Team619dlaer1_1 {\n" +
			    "    public class R playedBy T619dlaer1 {\n" +
			    "        test -> test;\n" +
			    "        // FIXME(SH): absence of this ctor is only complained at runtime!\n" +
			    "        public R() {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team619dlaer1_2(T619dlaer1 as R r1, final Team619dlaer1_1 tother, R<@tother> rother) {\n" +
			    "        super(tother, rother, r1);\n" +
			    "    }\n" +
			    "    public Team619dlaer1_2() {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team619dlaer1_2 tother = new Team619dlaer1_2();\n" +
			    "        new Team619dlaer1_2(new T619dlaer1(), tother, tother.new R());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T619dlaer1.java",
			    "\n" +
			    "public class T619dlaer1 {\n" +
			    "    double test() { return 42.0; }\n" +
			    "}\n" +
			    "    \n",
		"Team619dlaer1_1.java",
			    "\n" +
			    "public team class Team619dlaer1_1 {\n" +
			    "    public abstract class R {\n" +
			    "        public abstract double test();\n" +
			    "    }\n" +
			    "    public Team619dlaer1_1(final Team619dlaer1_1 tother, R<@tother> rother, R r1) {\n" +
			    "        System.out.print(r1.test());\n" +
			    "        System.out.print(r1.test() == rother.test());\n" +
			    "    }\n" +
			    "    public Team619dlaer1_1() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "42.0true");
    }

    // bogus source positions if these coincide: role file, declared lifting, method requires bridge for visibility
    // 6.1.10-otjld-declared-lifting-in-nested-1
    public void test6110_declaredLiftingInNested1() {
       
       runConformTest(
            new String[] {
		"Team6110dlin1.java",
			    "\n" +
			    "public team class Team6110dlin1 {\n" +
			    "    void test() {\n" +
			    "        new Mid().runTest();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team6110dlin1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6110dlin1.java",
			    "\n" +
			    "public class T6110dlin1 {\n" +
			    "}\n" +
			    "    \n",
		"Team6110dlin1/Mid.java",
			    "\n" +
			    "team package Team6110dlin1;    \n" +
			    "protected team class Mid {\n" +
			    "    protected class R playedBy T6110dlin1 {\n" +
			    "        String val;\n" +
			    "        protected R(String v) {\n" +
			    "            base();\n" +
			    "            this.val = v;\n" +
			    "            Mid.this.test(this); // lower before declared lifting\n" +
			    "        }\n" +
			    "        protected void beep() {\n" +
			    "            System.out.print(this.val);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    private void test(T6110dlin1 as R r) {\n" +
			    "        r.beep();\n" +
			    "    }\n" +
			    "    public void runTest() {\n" +
			    "        new R(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }
    
    // Lifting to an unbound super role
    // Bug 339801 - [compiler] declared lifting cannot resolve base imported base type
    public void test6111_declaredLiftingToSuper1() {
    	runConformTest(
    		new String[] {
    	"p1/T6111dlts1Main.java",
    			"package p1;" +
    			"import p2.Team6111dlts1;\n" +
    			"public class T6111dlts1Main {\n" +
    			"    public static void main(String... args) {\n" +
    			"        new Team6111dlts1().test(new T6111dlts1_1());\n" +
    			"    }\n" +
    			"}\n",
    	"p2/Team6111dlts1.java",
    			"package p2;\n" +
    			"import base p1.T6111dlts1_1;\n" +
    			"@SuppressWarnings(\"decapsulation\")\n" +
    			"public team class Team6111dlts1 {\n" +
    			"    protected abstract class R0 {\n" +
    			"        protected abstract void test();\n" +
    			"    }\n" +
    			"    protected class R1 extends R0 playedBy T6111dlts1_1 {\n" +
    			"        test -> test;" +
    			"    }\n" +
    			"    public void test(T6111dlts1_1 as R0 r) {\n" +
    			"        r.test();\n" +
    			"    }\n" +
    			"}\n",
    	"p1/T6111dlts1_1.java",
    			"package p1;\n" +
    			"class T6111dlts1_1 {\n" +
    			"    void test() { System.out.print(\"OK\"); }\n" +
    			"}\n",
    		},
    		"OK");
    }

    // Bug 355441 - illegal bytecode for team constructor with declared lifting and field access
    public void _test6112_declaredLiftingInConstructor1() {
       runConformTest(
            new String[] {
		"T6112dlic1Main.java",
			    "\n" +
			    "public class T6112dlic1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T6112dlic1      o = new T6112dlic1();\n" +
			    "        Team6112dlic1_2 t = new Team6112dlic1_2(o);\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6112dlic1.java",
			    "\n" +
			    "public class T6112dlic1 {}\n" +
			    "    \n",
		"Team6112dlic1_1.java",
			    "\n" +
			    "public team class Team6112dlic1_1 {\n" +
			    "    Role6112dlic1_1 r;\n" +
			    "    public Team6112dlic1_1(Role6112dlic1_1 obj) {\n" +
			    "        this.r = obj;\n" +
			    "    }\n" +
			    "    public class Role6112dlic1_1 playedBy T6112dlic1 {\n" +
			    "        public String value = \"OK\";\n" +
			    "    }\n" +
			    "    public String getValue() {\n" +
			    "        return this.r.value;" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6112dlic1_2.java",
			    "\n" +
			    "public team class Team6112dlic1_2 extends Team6112dlic1_1 {\n" +
			    "    public Team6112dlic1_2(T6112dlic1 as Role6112dlic1_1 obj) {\n" +
			    "        super(obj);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // Bug 355441 - illegal bytecode for team constructor with declared lifting and field access
    public void _test6112_declaredLiftingInConstructor2() {
       runConformTest(
            new String[] {
		"T6112dlic2Main.java",
			    "\n" +
			    "public class T6112dlic2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        T6112dlic2      o = new T6112dlic2();\n" +
			    "        Team6112dlic2_2 t = new Team6112dlic2_2(o);\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T6112dlic2.java",
			    "\n" +
			    "public class T6112dlic2 {}\n" +
			    "    \n",
		"Team6112dlic2_1.java",
			    "\n" +
			    "public team class Team6112dlic2_1 {\n" +
			    "    private Role6112dlic2_1 r;\n" +
			    "    public Team6112dlic2_1(Role6112dlic2_1 obj) {\n" +
			    "        this.r = obj;\n" +
			    "    }\n" +
			    "    public class Role6112dlic2_1 playedBy T6112dlic2 {\n" +
			    "        public String value = \"OK\";\n" +
			    "    }\n" +
			    "    public String getValue() {\n" +
			    "        return this.r.value;" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team6112dlic2_2.java",
			    "\n" +
			    "public team class Team6112dlic2_2 extends Team6112dlic2_1 {\n" +
			    "    public Team6112dlic2_2(T6112dlic2 as Role6112dlic2_1 obj) {\n" +
			    "        super(obj);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // Bug 393072 - VerifyError caused by super call within team constructor using declared lifting
    // array lifting and super-call
    public void test6112_declaredLiftingInConstructor3() {
    	runConformTest(
    		new String[] {
    	"T6112dlic3.java",
    			"public class T6112dlic3 {}\n",
    	"Team6112dlic3_0.java",
    			"public team class Team6112dlic3_0 {\n" + 
    			"    R[] f;\n" + 
    			"    Team6112dlic3_0(R[] roles){ this.f = roles; }\n" + 
    			"    protected class R {}\n" + 
    			"}\n",
    	"Team6112dlic3_1.java",
    			"public team class Team6112dlic3_1 extends Team6112dlic3_0 {\n" + 
    			"    Team6112dlic3_1(T6112dlic3[] as R[] roles) { super(roles); }\n" + 
    			"    @Override\n" +
    			"    protected class R playedBy T6112dlic3 {}\n" + 
    			"}\n"
    		});
    }

    // Bug 393072 - VerifyError caused by super call within team constructor using declared lifting
    // array lifting and super-call - additional contructor provided
    public void test6112_declaredLiftingInConstructor4() {
    	runConformTest(
    		new String[] {
    	"Team6112dlic4_1.java",
    			"public team class Team6112dlic4_1 extends Team6112dlic4_0 {\n" + 
    			"    Team6112dlic4_1(T6112dlic4[] as R[] roles) { super(roles); }\n" + 
    			"    @Override\n" +
    			"    protected class R playedBy T6112dlic4 {\n" +
    			"        test -> OK;\n" +
    			"    }\n" +
    			"    public static void main(String[] args) {\n" +
    			"        T6112dlic4 b = new T6112dlic4();\n" +
    			"        new Team6112dlic4_1(new T6112dlic4[]{b});\n" +
    			"    }\n" + 
    			"}\n",
    	"T6112dlic4.java",
    			"public class T6112dlic4 {\n" +
    			"    void OK() { System.out.print(\"OK\"); }\n" +
    			"}\n",
    	"Team6112dlic4_0.java",
    			"public abstract team class Team6112dlic4_0 {\n" + 
    			"    R[] f;\n" + 
    			"    Team6112dlic4_0(){ System.out.print(\"wrong\"); }\n" + 
    			"    Team6112dlic4_0(R[] roles){\n" +
    			"        this.f = roles;\n" +
    			"        this.f[0].test();" +
    			"    }\n" + 
    			"    protected abstract class R { protected abstract void test(); }\n" + 
    			"}\n",
    		},
    		"OK");
    }

    // Bug 393072 - VerifyError caused by super call within team constructor using declared lifting
    // expected lifting does not happen
    public void test6112_declaredLiftingInConstructor5() {
    	runConformTest(
    		new String[] {
		"Team6112dlic5_1.java",
				"public team class Team6112dlic5_1 extends Team6112dlic5_0 {\n" + 
				"    Team6112dlic5_1() { super(new R[0]); }\n" +
				"    public void test() {\n" +
				"        new R().test(this.f.length);\n" +
				"    }\n" + 
				"    @Override\n" +
				"    protected class R playedBy T6112dlic5 {\n" +
				"        protected R() { base(); }\n" +
    			"        test -> OK;\n" +
				"    }\n" + 
    			"    public static void main(String[] args) {\n" +
    			"        new Team6112dlic5_1().test();\n" +
    			"    }\n" + 
				"}\n",
    	"T6112dlic5.java",
    			"public class T6112dlic5 {\n" +
    			"    void OK(int x) { System.out.print(\"OK\"+x); }\n" +
    			"}\n",
    	"Team6112dlic5_0.java",
    			"public abstract team class Team6112dlic5_0 {\n" + 
    			"    R[] f;\n" + 
    			"    Team6112dlic5_0(R[] roles){\n" +
    			"        this.f = roles;\n" +
    			"    }\n" +
    			"    protected abstract class R { protected abstract void test(int l); }\n" + 
    			"}\n",
    		},
    		"OK0");
    }

    // Bug 397712 - [compiler] illegal bytecode from chains of team constructors
    public void test6112_declaredLiftingInConstructor6() {
    	runConformTest(
    		new String[] {
		"Team6112dlic6_2.java",
				"public team class Team6112dlic6_2 extends Team6112dlic6_1 {\n" + 
				"    Team6112dlic6_2(T6112dlic6 as R r) { super(r); }\n" +
				"    @Override\n" +
				"    protected class R playedBy T6112dlic6 {\n" +
    			"        test -> OK;\n" +
				"    }\n" + 
    			"    public static void main(String[] args) {\n" +
    			"        new Team6112dlic6_2(new T6112dlic6()).test();\n" +
    			"    }\n" + 
				"}\n",
		"Team6112dlic6_1.java",
				"public team class Team6112dlic6_1 extends Team6112dlic6_0 {\n" +
				"    R r;" + 
				"    protected Team6112dlic6_1(R r) { this.r = r; }\n" +
				"    public void test() {\n" +
				"        this.r.test(2);\n" +
				"    }\n" + 
				"}\n",
    	"T6112dlic6.java",
    			"public class T6112dlic6 {\n" +
    			"    void OK(int x) { System.out.print(\"OK\"+x); }\n" +
    			"}\n",
    	"Team6112dlic6_0.java",
    			"public abstract team class Team6112dlic6_0 {\n" + 
    			"    Team6112dlic6_0(){}\n" +
    			"    protected abstract class R { protected abstract void test(int l); }\n" + 
    			"}\n",
    		},
    		"OK2");
    }
    
    public void test6113_declaredLiftingOfExternalizedRole1() {
    	runConformTest(new String[] {
    		"Team6113dloer1_2.java",
    			"public team class Team6113dloer1_2 {\n" +
    			"final static Team6113dloer1_1 other = new Team6113dloer1_1();\n" +
    			"    protected class R playedBy R<@other> {\n" +
    			"        void test() -> void test();\n" +
    			"    }\n" +
    			"    void test(R<@other> as R r) {\n" +
    			"        r.test();\n" +
    			"    }\n" +
    			"    public static void main(String[] args) {\n" +
    			"        other.val = \"OK\";\n" +
    			"        new Team6113dloer1_2().test(other.getR());\n" +
    			"    }\n" +
    			"}\n",
    		"Team6113dloer1_1.java",
    			"public team class Team6113dloer1_1 {\n" +
    			"public String val;\n" +
    			"public class R {\n" +
    			"    public void test() {\n" +
    			"        System.out.print(val);\n" +
    			"    }\n" +
    			"}\n" +
    			"public R getR() { return new R(); }\n" +
    			"}\n"
    	},
    	"OK");
    }

    public void test6113_maximalSyntax1() {
        if (this.complianceLevel < ClassFileConstants.JDK1_8) return;
        runConformTest(
             new String[] {
 		"p2/Team6113ms1.java",
 			    "package p2;\n" +
	    		"import base p1.T6113ms1;\n" +
 			    "public team class Team6113ms1 {\n" +
 			    "    public class Role6113ms1_1 playedBy T6113ms1 {\n" +
 			    "        protected String getValue() {\n" +
 			    "            return \"OK\";\n" +
 			    "        }\n" +
 			    "    }\n" +
 			    "\n" +
 			    "    public class Role6113ms1_2 playedBy T6113ms1 {\n" +
 			    "        @SuppressWarnings(\"unused\")\n" +
 			    "        private String value;\n" +
 			    "\n" +
 			    "        public Role6113ms1_2(String value) {\n" +
 			    "            base();\n" +
 			    "            this.value = value;\n" +
 			    "        }\n" +
 			    "    }\n" +
 			    "\n" +
//         			    "    public String getValue(int i, final @NonNullB T6113ms1<@NullableB String>@d1[]@d12 @d123[]@d2 @d22 @d234[] as @NonNullR Role6113ms1_1<@NullableR String>@D2[]@D2 @D22[] obj) {\n" +
 				"    public String getValue(java.lang.Object i, final p1.@NonNullB T6113ms1[] as @NonNullR Role6113ms1_1[] obj) {\n" +
 			    "        return obj[0].getValue();\n" +
 			    "    }\n" +
 			    "    public static void main(String[] args) {\n" +
 			    "		new Team6113ms1().test();\n" +
 			    "	}\n" +
 			    "	void test() {\n" +
 			    "        Role6113ms1_2   r = new Role6113ms1_2(\"NOTOK\");\n" +
 			    "		 Role6113ms1_2[] rs = new Role6113ms1_2[]{r};\n" +
 			    "\n" +
 			    "        // should lower the role object as the external signature of the method is\n" +
 			    "        //   Team6113ms1.getValue(Object,T6113ms1[] obj)\n" +
 			    "        System.out.print(getValue(null, rs));\n" +
 			    "    }\n" +
 			    "}\n" +
 			    "    \n",
 		"p1/T6113ms1.java",
 			    "package p1;\n" +
 			    "public class T6113ms1 {}\n" +
 			    "    \n",
 		"p2/NonNullB.java\n",
 				"package p2;\n" +
 				"import java.lang.annotation.*;\n" +
				"@Target(ElementType.TYPE_USE) public @interface NonNullB {}\n",
 		"p2/NonNullR.java\n",
 				"package p2;\n" +
 				"import java.lang.annotation.*;\n" +
				"@Target(ElementType.TYPE_USE) public @interface NonNullR {}\n"
             },
             "OK");
    	
    }

}
