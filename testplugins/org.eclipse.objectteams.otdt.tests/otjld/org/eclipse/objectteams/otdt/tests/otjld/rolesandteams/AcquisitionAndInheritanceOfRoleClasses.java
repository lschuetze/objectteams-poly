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

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

@SuppressWarnings("unchecked")
public class AcquisitionAndInheritanceOfRoleClasses extends AbstractOTJLDTest {

	public AcquisitionAndInheritanceOfRoleClasses(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test139_illegalTsuperAccess5f" };
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return AcquisitionAndInheritanceOfRoleClasses.class;
	}

    // a team accesses a public feature of an unbound role class defined in it
    // 1.3.1-otjld-role-feature-access-1
    public void test131_roleFeatureAccess1() {
       
       runConformTest(
            new String[] {
		"T131rfa1Main.java",
			    "\n" +
			    "public class T131rfa1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team131rfa1 t = new Team131rfa1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa1.java",
			    "\n" +
			    "public team class Team131rfa1 {\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role131rfa1().getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    protected class Role131rfa1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team accesses a friendly feature of a bound role class defined in it
    // 1.3.1-otjld-role-feature-access-2
    public void test131_roleFeatureAccess2() {
       
       runConformTest(
            new String[] {
		"T131rfa2Main.java",
			    "\n" +
			    "public class T131rfa2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team131rfa2 t = new Team131rfa2();\n" +
			    "        T131rfa2    o = new T131rfa2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue(o));\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T131rfa2.java",
			    "\n" +
			    "public class T131rfa2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa2.java",
			    "\n" +
			    "public team class Team131rfa2 {\n" +
			    "    public String getValue(T131rfa2 as Role131rfa2 obj) {\n" +
			    "        return obj.getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    protected class Role131rfa2 playedBy T131rfa2 {\n" +
			    "        protected abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team accesses a protected feature of an unbound role inherited from an indirect super team
    // 1.3.1-otjld-role-feature-access-3
    public void test131_roleFeatureAccess3() {
       
       runConformTest(
            new String[] {
		"T131rfa3Main.java",
			    "\n" +
			    "public class T131rfa3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team131rfa3_3 t = new Team131rfa3_3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa3_1.java",
			    "\n" +
			    "public team class Team131rfa3_1 {\n" +
			    "    protected class Role131rfa3 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa3_2.java",
			    "\n" +
			    "public team class Team131rfa3_2 extends Team131rfa3_1 {}\n" +
			    "    \n",
		"Team131rfa3_3.java",
			    "\n" +
			    "public team class Team131rfa3_3 extends Team131rfa3_2 {\n" +
			    "    public String getValue() {\n" +
			    "        return new Role131rfa3().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team accesses a static protected feature of a bound role inherited from a direct super team - callout
    // 1.3.1-otjld-role-feature-access-4
    public void test131_roleFeatureAccess4() {
       
       runConformTest(
            new String[] {
		"T131rfa4Main.java",
			    "\n" +
			    "public class T131rfa4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team131rfa4_2 t = new Team131rfa4_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T131rfa4.java",
			    "\n" +
			    "public class T131rfa4 {\n" +
			    "    public static String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa4_1.java",
			    "\n" +
			    "public team class Team131rfa4_1 {\n" +
			    "    protected class Role131rfa4 playedBy T131rfa4 {\n" +
			    "	    protected abstract static String getValue();\n" +
			    "	    String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa4_2.java",
			    "\n" +
			    "public team class Team131rfa4_2 extends Team131rfa4_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return Role131rfa4.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team accesses a static protected feature of a bound role inherited from a direct super team - shorthand callout
    // 1.3.1-otjld-role-feature-access-4s
    public void test131_roleFeatureAccess4s() {
       
       runConformTest(
            new String[] {
		"T131rfa4sMain.java",
			    "\n" +
			    "public class T131rfa4sMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team131rfa4s_2 t = new Team131rfa4s_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T131rfa4s.java",
			    "\n" +
			    "public class T131rfa4s {\n" +
			    "    public static String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa4s_1.java",
			    "\n" +
			    "public team class Team131rfa4s_1 {\n" +
			    "    protected class Role131rfa4s playedBy T131rfa4s {\n" +
			    "            protected String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa4s_2.java",
			    "\n" +
			    "public team class Team131rfa4s_2 extends Team131rfa4s_1 {\n" +
			    "    public String getValue() {\n" +
			    "        return Role131rfa4s.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team accesses a static protected feature of a unbound role inherited from a direct super team - regular method
    // 1.3.1-otjld-role-feature-access-5
    public void test131_roleFeatureAccess5() {
       
       runConformTest(
            new String[] {
		"T131rfa5Main.java",
			    "\n" +
			    "public class T131rfa5Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team131rfa5_2 t = new Team131rfa5_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa5_1.java",
			    "\n" +
			    "public abstract team class Team131rfa5_1 {\n" +
			    "    protected abstract class Role131rfa5_1  {\n" +
			    "        protected static String getValue(Role131rfa5_2 r) {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected abstract class Role131rfa5_2 extends Role131rfa5_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa5_2.java",
			    "\n" +
			    "public team class Team131rfa5_2 extends Team131rfa5_1 {\n" +
			    "    protected class Role131rfa5_2 {}\n" +
			    "    public String getValue() {\n" +
			    "        return Role131rfa5_1.getValue(new Role131rfa5_2());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role feature accessed via function in the team, overridden (challenging weakening
    // 1.3.1-otjld-role-feature-access-6
    public void test131_roleFeatureAccess6() {
       
       runConformTest(
            new String[] {
		"Team131rfa6_2.java",
			    "\n" +
			    "public team class Team131rfa6_2 extends Team131rfa6_1 {\n" +
			    "    public class R {\n" +
			    "        String val;\n" +
			    "        protected R(String val) { this.val = val; }\n" +
			    "        protected void test() {\n" +
			    "            System.out.print(this.val);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public R getR() { return new R(\"OK\"); }\n" +
			    "    void test() {\n" +
			    "        getR().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team131rfa6_2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team131rfa6_1.java",
			    "\n" +
			    "public team class Team131rfa6_1 {\n" +
			    "    public class R {}\n" +
			    "    public R getR() { return new R(); }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a feature inherited from its implicit super role
    // 1.3.2-otjld-role-inherited-feature-access-1
    public void test132_roleInheritedFeatureAccess1() {
       
       runConformTest(
            new String[] {
		"T132rfa1Main.java",
			    "\n" +
			    "public class T132rfa1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team132rfa1_2 t = new Team132rfa1_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa1_1.java",
			    "\n" +
			    "public team class Team132rfa1_1 {\n" +
			    "    public class Role132rfa1 {\n" +
			    "        private String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa1_2.java",
			    "\n" +
			    "public team class Team132rfa1_2 extends Team132rfa1_1 {\n" +
			    "    public class Role132rfa1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role132rfa1().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses an overwritten feature that it indirectly inherited from its implicit super role - default visibility
    // 1.3.2-otjld-role-inherited-feature-access-2
    public void test132_roleInheritedFeatureAccess2() {
       
       runConformTest(
            new String[] {
		"T132rfa2Main.java",
			    "\n" +
			    "public class T132rfa2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team132rfa2_3 t = new Team132rfa2_3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa2_1.java",
			    "\n" +
			    "public team class Team132rfa2_1 {\n" +
			    "    protected class Role132rfa2 {\n" +
			    "        String getValueInternal() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa2_2.java",
			    "\n" +
			    "public team class Team132rfa2_2 extends Team132rfa2_1 {}\n" +
			    "    \n",
		"Team132rfa2_3.java",
			    "\n" +
			    "public team class Team132rfa2_3 extends Team132rfa2_2 {\n" +
			    "    protected class Role132rfa2 {\n" +
			    "        String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "        protected String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role132rfa2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses an overwritten feature that it indirectly inherited from its implicit super role
    // 1.3.2-otjld-role-inherited-feature-access-2a
    public void test132_roleInheritedFeatureAccess2a() {
       
       runConformTest(
            new String[] {
		"T132rfa2aMain.java",
			    "\n" +
			    "public class T132rfa2aMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team132rfa2a_3 t = new Team132rfa2a_3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa2a_1.java",
			    "\n" +
			    "public team class Team132rfa2a_1 {\n" +
			    "    protected class Role132rfa2a {\n" +
			    "        private String getValueInternal() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa2a_2.java",
			    "\n" +
			    "public team class Team132rfa2a_2 extends Team132rfa2a_1 {}\n" +
			    "    \n",
		"Team132rfa2a_3.java",
			    "\n" +
			    "public team class Team132rfa2a_3 extends Team132rfa2a_2 {\n" +
			    "    protected class Role132rfa2a {\n" +
			    "        private String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "        protected String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role132rfa2a().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a feature inherited from its direct explicit super role
    // 1.3.2-otjld-role-inherited-feature-access-3
    public void test132_roleInheritedFeatureAccess3() {
       
       runConformTest(
            new String[] {
		"T132rfa3Main.java",
			    "\n" +
			    "public class T132rfa3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team132rfa3_2 t = new Team132rfa3_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa3_1.java",
			    "\n" +
			    "public team class Team132rfa3_1 {\n" +
			    "    protected class Role132rfa3_1 {\n" +
			    "        private String getRealValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return getRealValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa3_2.java",
			    "\n" +
			    "public team class Team132rfa3_2 extends Team132rfa3_1 {\n" +
			    "    protected class Role132rfa3_2 extends Role132rfa3_1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role132rfa3_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses an overwritten feature inherited from an indirectly explicit super role
    // 1.3.2-otjld-role-inherited-feature-access-4
    public void test132_roleInheritedFeatureAccess4() {
       
       runConformTest(
            new String[] {
		"T132rfa4Main.java",
			    "\n" +
			    "public class T132rfa4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team132rfa4_3 t = new Team132rfa4_3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa4_1.java",
			    "\n" +
			    "public team class Team132rfa4_1 {\n" +
			    "    public class Role132rfa4_1 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa4_2.java",
			    "\n" +
			    "public team class Team132rfa4_2 extends Team132rfa4_1 {\n" +
			    "    public class Role132rfa4_2 extends Role132rfa4_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team132rfa4_3.java",
			    "\n" +
			    "public team class Team132rfa4_3 extends Team132rfa4_2 {\n" +
			    "    public class Role132rfa4_2 {\n" +
			    "        protected String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role132rfa4_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an implicitly inherited role class is not the subtype (as of Java) of its super role
    // 1.3.3-otjld-role-not-subtype-of-superrole-1
    public void test133_roleNotSubtypeOfSuperrole1() {
       
       runConformTest(
            new String[] {
		"T133rnsos1Main.java",
			    "\n" +
			    "public class T133rnsos1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team133rnsos1_1 t1 = new Team133rnsos1_1();\n" +
			    "        final Team133rnsos1_2 t2 = new Team133rnsos1_2();\n" +
			    "\n" +
			    "        System.out.print(t2.getRole() instanceof Role133rnsos1<@t1> ? \"NOTOK\" : \"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos1_1.java",
			    "\n" +
			    "public team class Team133rnsos1_1 {\n" +
			    "    public class Role133rnsos1 {}\n" +
			    "\n" +
			    "    public Role133rnsos1 getRole() {\n" +
			    "        return new Role133rnsos1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos1_2.java",
			    "\n" +
			    "public team class Team133rnsos1_2 extends Team133rnsos1_1 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // an implicitly inherited role class is not the subtype (as of Java) of its super role, cast fails
    // 1.3.3-otjld-role-not-subtype-of-superrole-1a
    public void test133_roleNotSubtypeOfSuperrole1a() {
       
       runConformTest(
            new String[] {
		"T133rnsos1aMain.java",
			    "\n" +
			    "public class T133rnsos1aMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team133rnsos1a_1 t1 = new Team133rnsos1a_1();\n" +
			    "        final Team133rnsos1a_2 t2 = new Team133rnsos1a_2();\n" +
			    "\n" +
			    "        try {\n" +
			    "            Role133rnsos1a<@t1> r = (Role133rnsos1a<@t1>)t2.getRole();\n" +
			    "            System.out.print(\"NOTOK\"+r);\n" +
			    "        } catch (ClassCastException cce) {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos1a_1.java",
			    "\n" +
			    "public team class Team133rnsos1a_1 {\n" +
			    "    public class Role133rnsos1a {}\n" +
			    "\n" +
			    "    public Role133rnsos1a getRole() {\n" +
			    "        return new Role133rnsos1a();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos1a_2.java",
			    "\n" +
			    "public team class Team133rnsos1a_2 extends Team133rnsos1a_1 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // an implicitly inherited role class is not the subtype (as of Java) of its super role -- cast to unqualified role type
    // 1.3.3-otjld-role-not-subtype-of-superrole-1c
    public void test133_roleNotSubtypeOfSuperrole1c() {
       
       runConformTest(
            new String[] {
		"Team133rnsos1c_2.java",
			    "\n" +
			    "public team class Team133rnsos1c_2 extends Team133rnsos1c_1 {\n" +
			    "    public void test() {\n" +
			    "        final Team133rnsos1c_2 t2 = new Team133rnsos1c_2();\n" +
			    "        try {\n" +
			    "            System.out.print((Role133rnsos1c)t2.getRole());\n" +
			    "        } catch (ClassCastException cce) {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team133rnsos1c_2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos1c_1.java",
			    "\n" +
			    "public team class Team133rnsos1c_1 {\n" +
			    "    public class Role133rnsos1c {}\n" +
			    "\n" +
			    "    public Role133rnsos1c getRole() {\n" +
			    "        return new Role133rnsos1c();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an implicitly inherited role class is not the subtype (as of Java) of its super role -- unqualified role type expands to "tthis.R"
    // 1.3.3-otjld-role-not-subtype-of-superrole-1e
    public void test133_roleNotSubtypeOfSuperrole1e() {
       
       runConformTest(
            new String[] {
		"Team133rnsos1e_2.java",
			    "\n" +
			    "public team class Team133rnsos1e_2 extends Team133rnsos1e_1 {\n" +
			    "    public void test() {\n" +
			    "        final Team133rnsos1e_2 t2 = new Team133rnsos1e_2();\n" +
			    "\n" +
			    "        System.out.print(t2.getRole() instanceof Role133rnsos1e ? \"NOTOK\" : \"OK\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team133rnsos1e_2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos1e_1.java",
			    "\n" +
			    "public team class Team133rnsos1e_1 {\n" +
			    "    public class Role133rnsos1e {}\n" +
			    "\n" +
			    "    public Role133rnsos1e getRole() {\n" +
			    "        return new Role133rnsos1e();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an implicitly inherited role class is not the subtype (as of Java) of its super role - anchors are fields
    // 1.3.3-otjld-role-not-subtype-of-superrole-1f
    public void test133_roleNotSubtypeOfSuperrole1f() {
       
       runConformTest(
            new String[] {
		"T133rnsos1fMain.java",
			    "\n" +
			    "public class T133rnsos1fMain {\n" +
			    "    static final Team133rnsos1f_1 t1 = new Team133rnsos1f_1();\n" +
			    "    static final Team133rnsos1f_2 t2 = new Team133rnsos1f_2();\n" +
			    "    public static void main(String[] args) {\n" +
			    "        System.out.print(t2.getRole() instanceof Role133rnsos1f<@t1> ? \"NOTOK\" : \"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos1f_1.java",
			    "\n" +
			    "public team class Team133rnsos1f_1 {\n" +
			    "    public class Role133rnsos1f {}\n" +
			    "\n" +
			    "    public Role133rnsos1f getRole() {\n" +
			    "        return new Role133rnsos1f();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos1f_2.java",
			    "\n" +
			    "public team class Team133rnsos1f_2 extends Team133rnsos1f_1 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is not the subtype (as of Java) of its implicit super role
    // 1.3.3-otjld-role-not-subtype-of-superrole-2
    public void test133_roleNotSubtypeOfSuperrole2() {
       
       runConformTest(
            new String[] {
		"T133rnsos2Main.java",
			    "\n" +
			    "public class T133rnsos2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team133rnsos2_1 t1 = new Team133rnsos2_1();\n" +
			    "        final Team133rnsos2_2 t2 = new Team133rnsos2_2();\n" +
			    "\n" +
			    "        System.out.print(t2.getRole() instanceof Role133rnsos2<@t1> ? \"NOTOK\" : \"OK\");\n" +
			    "        final Team133rnsos2_1 t1a = t2;\n" +
			    "        System.out.print(t2.getRole() instanceof Role133rnsos2<@t1a> ? \"OK\" : \"NOTOK\");\n" +
			    "        System.out.print(t1a.getRole() instanceof Role133rnsos2<@t2> ? \"OK\" : \"NOTOK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos2_1.java",
			    "\n" +
			    "public team class Team133rnsos2_1 {\n" +
			    "    public class Role133rnsos2 {}\n" +
			    "\n" +
			    "    public Role133rnsos2 getRole() {\n" +
			    "        return new Role133rnsos2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos2_2.java",
			    "\n" +
			    "public team class Team133rnsos2_2 extends Team133rnsos2_1 {\n" +
			    "    public class Role133rnsos2 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "OKOKOK");
    }

    // a role class is a subtype (as of Java) of its explicit super role
    // 1.3.3-otjld-role-not-subtype-of-superrole-3
    public void test133_roleNotSubtypeOfSuperrole3() {
       
       runConformTest(
            new String[] {
		"T133rnsos3Main.java",
			    "\n" +
			    "public class T133rnsos3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team133rnsos3 t = new Team133rnsos3();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team133rnsos3.java",
			    "\n" +
			    "public team class Team133rnsos3 {\n" +
			    "    public class Role133rnsos3_1 {}\n" +
			    "    public class Role133rnsos3_2 extends Role133rnsos3_1 {}\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        Role133rnsos3_2 r = new Role133rnsos3_2();\n" +
			    "\n" +
			    "        return r instanceof Role133rnsos3_1 ? \"OK\" : \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // within a subteam, an instance of an implicitly inherited role is assigned to a variable of the superteam's role class
    // 1.3.4-otjld-subrole-not-assignment-compatible-1
    public void test134_subroleNotAssignmentCompatible1() {
        runNegativeTestMatching(
            new String[] {
		"Team134snac1_1.java",
			    "\n" +
			    "public team class Team134snac1_1 {\n" +
			    "    public class Role134snac1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team134snac1_2.java",
			    "\n" +
			    "public team class Team134snac1_2 extends Team134snac1_1 {\n" +
			    "    public void test() {\n" +
			    "        final Team134snac1_1 t = new Team134snac1_1();\n" +
			    "        Role134snac1<@t>     r = new Role134snac1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.1(d)");
    }

    // within a subteam, an instance of an implicitly inherited role is assigned to a variable of the superteam's role class - role not externalizable
    // 1.3.4-otjld-subrole-not-assignment-compatible-1f
    public void test134_subroleNotAssignmentCompatible1f() {
        runNegativeTestMatching(
            new String[] {
		"Team134snac1f_1.java",
			    "\n" +
			    "public team class Team134snac1f_1 {\n" +
			    "    protected class Role134snac1f {}\n" +
			    "}\n" +
			    "    \n",
		"Team134snac1f_2.java",
			    "\n" +
			    "public team class Team134snac1f_2 extends Team134snac1f_1 {\n" +
			    "    public void test() {\n" +
			    "        final Team134snac1f_1 t = new Team134snac1f_1();\n" +
			    "        Role134snac1f<@t>     r = new Role134snac1f();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.3(b)");
    }

    // within a subteam, an instance of an implicitly inherited role (that is redefined in the subteam) is assigned to a variable of the superteam's role class
    // 1.3.4-otjld-subrole-not-assignment-compatible-2
    public void test134_subroleNotAssignmentCompatible2() {
        runNegativeTestMatching(
            new String[] {
		"Team134snac2_1.java",
			    "\n" +
			    "public team class Team134snac2_1 {\n" +
			    "    public class Role134snac2 {}\n" +
			    "}\n" +
			    "    \n",
		"Team134snac2_2.java",
			    "\n" +
			    "public team class Team134snac2_2 extends Team134snac2_1 {\n" +
			    "    protected class Role134rsnac2 {}\n" +
			    "    public void test() {\n" +
			    "        final Team134snac2_1 t = new Team134snac2_1();\n" +
			    "        Role134snac2<@t>     r = new Role134snac2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.1(d)");
    }

    // a subteam redefines an implicitly inherited role class including one of the role class's features
    // 1.3.5-otjld-redefining-role-feature
    public void test135_redefiningRoleFeature() {
       
       runConformTest(
            new String[] {
		"T135rrfMain.java",
			    "\n" +
			    "public class T135rrfMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team135rrf_2 t = new Team135rrf_2();\n" +
			    "        Role135rrf<@t>     r = t.new Role135rrf();\n" +
			    "\n" +
			    "        System.out.print(r.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team135rrf_1.java",
			    "\n" +
			    "public team class Team135rrf_1 {\n" +
			    "    public class Role135rrf {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team135rrf_2.java",
			    "\n" +
			    "public team class Team135rrf_2 extends Team135rrf_1 {\n" +
			    "    public class Role135rrf {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a subteam redefines an implicitly inherited role class that is used via an anchored type in a feature of another role of the superteam
    // 1.3.6-otjld-redefined-anchored-type-1
    public void test136_redefinedAnchoredType1() {
       
       runConformTest(
            new String[] {
		"T136rat1Main.java",
			    "\n" +
			    "public class T136rat1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team136rat1_2 t = new Team136rat1_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team136rat1_1.java",
			    "\n" +
			    "public team class Team136rat1_1 {\n" +
			    "    public class Role136rat1_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public class Role136rat1_2 {\n" +
			    "        protected String getValue() {\n" +
			    "            final Team136rat1_1 t = new Team136rat1_1();\n" +
			    "            Role136rat1_1<@t>   r = t.new Role136rat1_1();\n" +
			    "\n" +
			    "            return r.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team136rat1_2.java",
			    "\n" +
			    "public team class Team136rat1_2 extends Team136rat1_1 {\n" +
			    "    public class Role136rat1_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role136rat1_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a subteam redefines an implicitly inherited role class that is used via an anchored type in an inner class of another role of the superteam
    // 1.3.6-otjld-redefined-anchored-type-2
    public void test136_redefinedAnchoredType2() {
       
       runConformTest(
            new String[] {
		"T136rat2Main.java",
			    "\n" +
			    "public class T136rat2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team136rat2_2 t = new Team136rat2_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team136rat2_1.java",
			    "\n" +
			    "public team class Team136rat2_1 {\n" +
			    "    public class Role136rat2_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected team class Role136rat2_2 {\n" +
			    "        protected class Inner136rat2 {\n" +
			    "            public String getValue() {\n" +
			    "                final Team136rat2_1 t = new Team136rat2_1();\n" +
			    "                Role136rat2_1<@t>   r = t.new Role136rat2_1();\n" +
			    "    \n" +
			    "                return r.getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return new Inner136rat2().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team136rat2_2.java",
			    "\n" +
			    "public team class Team136rat2_2 extends Team136rat2_1 {\n" +
			    "    public class Role136rat2_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role136rat2_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a subteam redefines an implicitly inherited role class that is used via an anchored type in a local class of another role of the superteam
    // 1.3.6-otjld-redefined-anchored-type-3
    public void test136_redefinedAnchoredType3() {
       
       runConformTest(
            new String[] {
		"T136rat3Main.java",
			    "\n" +
			    "public class T136rat3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team136rat3_2 t = new Team136rat3_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team136rat3_1.java",
			    "\n" +
			    "public team class Team136rat3_1 {\n" +
			    "    public class Role136rat3_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected class Role136rat3_2 {\n" +
			    "        public String getValue() {\n" +
			    "            class Local136rat3 {\n" +
			    "                String getValue() {\n" +
			    "                    final Team136rat3_1 t = new Team136rat3_1();\n" +
			    "                    Role136rat3_1<@t>   r = t.new Role136rat3_1();\n" +
			    "        \n" +
			    "                    return r.getValue();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            return new Local136rat3().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team136rat3_2.java",
			    "\n" +
			    "public team class Team136rat3_2 extends Team136rat3_1 {\n" +
			    "    public class Role136rat3_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role136rat3_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a feature of its explicit super role via super
    // 1.3.7-otjld-access-to-explicit-superrole-1
    public void test137_accessToExplicitSuperrole1() {
       
       runConformTest(
            new String[] {
		"T137ates1Main.java",
			    "\n" +
			    "public class T137ates1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team137ates1 t = new Team137ates1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team137ates1.java",
			    "\n" +
			    "public team class Team137ates1 {\n" +
			    "    protected class Role137ates1_1 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected class Role137ates1_2 extends Role137ates1_1 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return super.getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role137ates1_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a feature of its explicit super role via super in an inner class
    // 1.3.7-otjld-access-to-explicit-superrole-2
    public void test137_accessToExplicitSuperrole2() {
       
       runConformTest(
            new String[] {
		"T137ates2Main.java",
			    "\n" +
			    "public class T137ates2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team137ates2_2 t = new Team137ates2_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team137ates2_1.java",
			    "\n" +
			    "public team class Team137ates2_1 {\n" +
			    "    protected team class Role137ates2_1 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team137ates2_2.java",
			    "\n" +
			    "public team class Team137ates2_2 extends Team137ates2_1 {\n" +
			    "    protected team class Role137ates2_2 extends Role137ates2_1 {\n" +
			    "        public class Inner137ates2 {\n" +
			    "            public String getValue() {\n" +
			    "                return Role137ates2_2.super.getValueInternal();\n" +
			    "            }\n" +
			    "        }\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return new Inner137ates2().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    protected team class Role137ates2_1 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role137ates2_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a feature defined in a super role of its explicit super role via super in a local class
    // 1.3.7-otjld-access-to-explicit-superrole-3
    public void test137_accessToExplicitSuperrole3() {
       
       runConformTest(
            new String[] {
		"T137ates3Main.java",
			    "\n" +
			    "public class T137ates3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team137ates3_3 t = new Team137ates3_3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team137ates3_1.java",
			    "\n" +
			    "public team class Team137ates3_1 {\n" +
			    "    protected class Role137ates3_1 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team137ates3_2.java",
			    "\n" +
			    "public team class Team137ates3_2 extends Team137ates3_1 {\n" +
			    "    protected class Role137ates3_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team137ates3_3.java",
			    "\n" +
			    "public team class Team137ates3_3 extends Team137ates3_2 {\n" +
			    "    protected class Role137ates3_2 extends Role137ates3_1 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            class Local137ates3 {\n" +
			    "                String getValue() {\n" +
			    "                    return Role137ates3_2.super.getValueInternal();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            return new Local137ates3().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role137ates3_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a method of its implicit super role via tsuper
    // 1.3.8-otjld-access-to-superrole-1
    public void test138_accessToSuperrole1() {
       
       runConformTest(
            new String[] {
		"T138ats1Main.java",
			    "\n" +
			    "public class T138ats1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team138ats1_2 t = new Team138ats1_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats1_1.java",
			    "\n" +
			    "public team class Team138ats1_1 {\n" +
			    "    protected class Role138ats1_1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats1_2.java",
			    "\n" +
			    "public team class Team138ats1_2 extends Team138ats1_1 {\n" +
			    "    protected class Role138ats1_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return tsuper.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role138ats1_1().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a method defined in a super role of its implicit super role via tsuper from an anonymous class
    // 1.3.8-otjld-access-to-superrole-2
    public void test138_accessToSuperrole2() {
       
       runConformTest(
            new String[] {
		"T138ats2Main.java",
			    "\n" +
			    "public class T138ats2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team138ats2_3 t = new Team138ats2_3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats2_1.java",
			    "\n" +
			    "public team class Team138ats2_1 {\n" +
			    "    protected class Role138ats2_1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats2_2.java",
			    "\n" +
			    "public team class Team138ats2_2 extends Team138ats2_1 {\n" +
			    "    protected class Role138ats2_2 extends Role138ats2_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team138ats2_3.java",
			    "\n" +
			    "public team class Team138ats2_3 extends Team138ats2_2 {\n" +
			    "    protected class Role138ats2_2 {\n" +
			    "        public String getValue() {\n" +
			    "            final String tmp = tsuper.getValue();\n" +
			    "            Object obj = new Object() {\n" +
			    "                public String toString() {\n" +
			    "                    return tmp;\n" +
			    "                }\n" +
			    "            };\n" +
			    "\n" +
			    "            return obj.toString();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role138ats2_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a tsuper method outside overriding methods - overriding implicitly inherited attribute
    // 1.3.8-otjld-access-to-superrole-3f
    public void test138_accessToSuperrole3f() {
        runNegativeTestMatching(
            new String[] {
		"Team138ats3f_1.java",
			    "\n" +
			    "public team class Team138ats3f_1 {\n" +
			    "    protected class Role138ats3f_1 {\n" +
			    "        protected String attr = \"NOTOK\";\n" +
			    "        protected String getAttr() {\n" +
			    "            return attr;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats3f_2.java",
			    "\n" +
			    "public team class Team138ats3f_2 extends Team138ats3f_1 {\n" +
			    "    protected class Role138ats3f_1 {\n" +
			    "        protected String attr = tsuper.getAttr().substring(3);\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role138ats3f_1().attr;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.4(b)");
    }

    // a role class accesses a tsuper method outside overriding methods
    // 1.3.8-otjld-access-to-superrole-3
    public void test138_accessToSuperrole3() {
        runNegativeTestMatching(
            new String[] {
		"Team138ats3_1.java",
			    "\n" +
			    "public team class Team138ats3_1 {\n" +
			    "    protected class Role138ats3_1 {\n" +
			    "        protected String attr = \"NOTOK\";\n" +
			    "        protected String getAttr() {\n" +
			    "            return attr;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats3_2.java",
			    "\n" +
			    "public team class Team138ats3_2 extends Team138ats3_1 {\n" +
			    "    protected class Role138ats3_1 {\n" +
			    "        protected String attr2 = tsuper.getAttr().substring(3);\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role138ats3_1().attr;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.1(f)");
    }

    // a role class accesses a field of its implicit super role via tsuper
    // 1.3.8-otjld-access-to-superrole-4
    public void test138_accessToSuperrole4() {
       
       runConformTest(
            new String[] {
		"T138ats4Main.java",
			    "\n" +
			    "public class T138ats4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team138ats4_2 t = new Team138ats4_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats4_1.java",
			    "\n" +
			    "public team class Team138ats4_1 {\n" +
			    "    protected class Role138ats4_1 {\n" +
			    "        protected String attr = \"NOTOK\";\n" +
			    "        protected String getAttr() {\n" +
			    "            return attr;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats4_2.java",
			    "\n" +
			    "public team class Team138ats4_2 extends Team138ats4_1 {\n" +
			    "    protected class Role138ats4_1 {\n" +
			    "        protected String getAttr() {\n" +
			    "	        return tsuper.getAttr();\n" +
			    "	    }\n" +
			    "        public Role138ats4_1() {\n" +
			    "		    tsuper();\n" +
			    "		    attr = getAttr().substring(3);\n" +
			    "	    }\n" +
			    "        protected String getValue() { return attr; }\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role138ats4_1().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class overrides an implicitly inherited field
    // 1.3.8-otjld-access-to-superrole-5
    public void test138_accessToSuperrole5() {
        runNegativeTestMatching(
            new String[] {
		"Team138ats5_1.java",
			    "\n" +
			    "public team class Team138ats5_1 {\n" +
			    "    protected class Role138ats5_1 {\n" +
			    "        protected String attr = \"NOTOK\";\n" +
			    "        protected String getAttr() {\n" +
			    "            return attr;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats5_2.java",
			    "\n" +
			    "public team class Team138ats5_2 extends Team138ats5_1 {\n" +
			    "    protected class Role138ats5_1 {\n" +
			    "    	protected String getAttr() {\n" +
			    "			return tsuper.getAttr();\n" +
			    "		}\n" +
			    "        String attr = getAttr().substring(3);\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role138ats5_1().attr;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.4(b)");
    }

    // illegal tsuper call in an anonymous class
    // 1.3.8-otjld-access-to-superrole-6
    public void test138_accessToSuperrole6() {
        runNegativeTestMatching(
            new String[] {
		"Team138ats6_1.java",
			    "\n" +
			    "public team class Team138ats6_1 {\n" +
			    "    protected class Role138ats6_1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team138ats6_2.java",
			    "\n" +
			    "public team class Team138ats6_2 extends Team138ats6_1 {\n" +
			    "    protected class Role138ats6_2 extends Role138ats6_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team138ats6_3.java",
			    "\n" +
			    "public team class Team138ats6_3 extends Team138ats6_2 {\n" +
			    "    protected class Role138ats6_2 {\n" +
			    "        public String getValue() {\n" +
			    "            Object obj = new Object() {\n" +
			    "                public String toString() {\n" +
			    "                    return Role138ats6_2.tsuper.getValue();\n" +
			    "                }\n" +
			    "            };\n" +
			    "\n" +
			    "            return obj.toString();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected String getValue() {\n" +
			    "        return new Role138ats6_2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.1(f)");
    }

    // qualified tsuper refers to enclosing team without super
    // 1.3.8-otjld-access-to-superrole-7
    public void test138_accessToSuperrole7() {
        runNegativeTest(
            new String[] {
		"Team138ats7.java",
			    "\n" +
			    "public team class Team138ats7 {\n" +
			    "    protected team class Mid1 {\n" +
			    "        protected class R {\n" +
			    "            void foo() {}\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected team class Mid2 extends Mid1 {\n" +
			    "        @Override\n" +
			    "        protected class R {\n" +
			    "            @Override\n" +
			    "            void foo() {\n" +
			    "                Team138ats7.tsuper.foo();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team138ats7.java (at line 13)\n" + 
    		"	Team138ats7.tsuper.foo();\n" + 
    		"	^^^^^^^^^^^^^^^^^^\n" + 
    		"Invalid qualified \'tsuper\': type org.objectteams.Team contains no role corresponding to Team138ats7.Mid2.R (OTJLD 1.3.1(f)).\n" + 
    		"----------\n");
    }

    // a role class accesses a feature of its explicit super role via tsuper
    // 1.3.9-otjld-illegal-tsuper-access-1
    public void test139_illegalTsuperAccess1() {
        runNegativeTestMatching(
            new String[] {
		"Team139ita1_1.java",
			    "\n" +
			    "public team class Team139ita1_1 {\n" +
			    "    protected class Role139ita1_1 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team139ita1_2.java",
			    "\n" +
			    "public team class Team139ita1_2 extends Team139ita1_1 {\n" +
			    "    protected class Role139ita1_2 extends Role139ita1_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return tsuper.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.1(f)");
    }

    // a role class accesses a different method of its implicit super role via tsuper
    // 1.3.9-otjld-illegal-tsuper-access-2
    public void test139_illegalTsuperAccess2() {
        runNegativeTestMatching(
            new String[] {
		"Team139ita2_1.java",
			    "\n" +
			    "public team class Team139ita2_1 {\n" +
			    "    protected class Role139ita2 {\n" +
			    "        protected String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team139ita2_2.java",
			    "\n" +
			    "public team class Team139ita2_2 extends Team139ita2_1 {\n" +
			    "    protected class Role139ita2 {\n" +
			    "        public String getValue() {\n" +
			    "            return tsuper.getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.1(f)");
    }

    // a role class accesses a field of its implicit super role via tsuper
    // 1.3.9-otjld-illegal-tsuper-access-3
    public void test139_illegalTsuperAccess3() {
        runNegativeTestMatching(
            new String[] {
		"Team139ita3_1.java",
			    "\n" +
			    "public team class Team139ita3_1 {\n" +
			    "    protected class Role139ita3 {\n" +
			    "        protected String attr = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team139ita3_2.java",
			    "\n" +
			    "public team class Team139ita3_2 extends Team139ita3_1 {\n" +
			    "    protected class Role139ita3 {\n" +
			    "        public String getValue() {\n" +
			    "            return tsuper.attr;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "yntax");
    }

    // a role class accesses a method with the same name but different signature of its implicit super role via tsuper
    // 1.3.9-otjld-illegal-tsuper-access-4
    public void test139_illegalTsuperAccess4() {
        runNegativeTest(
            new String[] {
		"Team139ita4_1.java",
			    "\n" +
			    "public team class Team139ita4_1 {\n" +
			    "    protected class Role139ita4 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team139ita4_2.java",
			    "\n" +
			    "public team class Team139ita4_2 extends Team139ita4_1 {\n" +
			    "    @Override\n" +
			    "    protected class Role139ita4 {\n" +
			    "        public String getValue(String arg) {\n" +
			    "            return tsuper.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
            "1. ERROR in Team139ita4_2.java (at line 6)\n" +
            "	return tsuper.getValue();\n" +
			"	       ^^^^^^^^^^^^^^^^^\n" +
    		"Illegal tsuper call: can only invoke the method being overridden by the current method (OTJLD 1.3.1(f)).\n" +
            "----------\n");
    }

    // a role class accesses a method of its implicit super role via tsuper within an inner class - missing team keyword
    // 1.3.9-otjld-illegal-tsuper-access-5f
    public void test139_illegalTsuperAccess5f() {
        runNegativeTestMatching(
            new String[] {
		"Team139ita5f_1.java",
			    "\n" +
			    "public team class Team139ita5f_1 {\n" +
			    "    protected class Role139ita5f {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team139ita5f_2.java",
			    "\n" +
			    "public team class Team139ita5f_2 extends Team139ita5f_1 {\n" +
			    "    protected class Role139ita5f {\n" +
			    "        class Inner139ita5f {\n" +
			    "            private String getValue() {\n" +
			    "                return tsuper.getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.5");
    }

    // a role class accesses a method of its implicit super role via tsuper within an inner class
    // 1.3.9-otjld-illegal-tsuper-access-5
    public void test139_illegalTsuperAccess5() {
        runNegativeTestMatching(
            new String[] {
		"Team139ita5_1.java",
			    "\n" +
			    "public team class Team139ita5_1 {\n" +
			    "    protected class Role139ita5 {\n" +
			    "        protected String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team139ita5_2.java",
			    "\n" +
			    "public team class Team139ita5_2 extends Team139ita5_1 {\n" +
			    "    protected team class Role139ita5 {\n" +
			    "        protected class Inner139ita5 {\n" +
			    "            @SuppressWarnings(\"unused\")\n" +
			    "            private String getValue() {\n" +
			    "                return tsuper.getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.1(f)");
    }
    
    // a tsuper call is unresolved (requires specific error reporting, setting tsuperMethod to a ProblemMethodBinding)
    public void test139_illegalTsuperAccess6() {
        runNegativeTestMatching(
            new String[] {
		"Team139ita6_1.java",
			    "\n" +
			    "public team class Team139ita6_1 {\n" +
			    "    protected class Role139ita6 {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team139ita6_2.java",
			    "\n" +
			    "public team class Team139ita6_2 extends Team139ita6_1 {\n" +
			    "    @Override\n" +
			    "    protected class Role139ita6 {\n" +
			    "        callin String getValue(String arg) {\n" +
			    "            return tsuper.getValue(arg);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team139ita6_2.java (at line 6)\n" + 
    		"	return tsuper.getValue(arg);\n" + 
    		"	              ^^^^^^^^\n" + 
    		"The method getValue(String) is undefined for the type Team139ita6_1.Role139ita6\n" + 
    		"----------\n");
    }

    // a tsuper call in a callin method with generic return type (requries copyInheritanceSrc to be reflected when creating a ParameterizedMethodBinding)
    public void _test139_tsuperCallWithTypeParameter1() {
        runConformTest(
            new String[] {
		"T139tcwtp1Main.java",
    			"public class T139tcwtp1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team139tcwtp1_2().activate();\n" +
			    "        System.out.print(new T139tcwtp1().getValue(\"nv\"));\n" +
			    "    }\n" +
			    "}\n",
		"T139tcwtp1.java",
		    	"public class T139tcwtp1 {\n" +
			    "    String getValue(String arg) {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n",
		"Team139tcwtp1_1.java",
			    "\n" +
			    "public team class Team139tcwtp1_1 {\n" +
			    "    protected class Role139tcwtp1<E> {\n" +
			    "        abstract E val();\n" +
			    "        callin E getValue(String arg) {\n" +
			    "            return this.val();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n",
		"Team139tcwtp1_2.java",
			    "\n" +
			    "public team class Team139tcwtp1_2 extends Team139tcwtp1_1 {\n" +
			    "    protected class Role139tcwtp1<String> playedBy T139tcwtp1 {\n" +
			    "        String val() { return \"OK\"; }\n" +
			    "        getValue <- replace getValue;\n" +
			    "        callin String getValue(String arg) {\n" +
			    "            return tsuper.getValue(arg);\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n"
            },
            "OK");
    }

    // a role class has stronger access rights than its implicit superrole
    // 1.3.10-otjld-stronger-access-than-superrole-1
    public void test1310_strongerAccessThanSuperrole1() {
        runNegativeTestMatching(
            new String[] {
		"Team1310sats1_1.java",
			    "\n" +
			    "public team class Team1310sats1_1 {\n" +
			    "    public class Role1310sats1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team1310sats1_2.java",
			    "\n" +
			    "public team class Team1310sats1_2 extends Team1310sats1_1 {\n" +
			    "    protected class Role1310sats1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.1(h)");
    }

    // a role class has access rights that are incompatible to its implicit superrole - also illegal visibility for role
    // 1.3.10-otjld-stronger-access-than-superrole-2
    public void test1310_strongerAccessThanSuperrole2() {
        runNegativeTestMatching(
            new String[] {
		"Team1310sats2_1.java",
			    "\n" +
			    "public team class Team1310sats2_1 {\n" +
			    "    protected class Role1310sats2 {}\n" +
			    "}\n" +
			    "    \n",
		"Team1310sats2_2.java",
			    "\n" +
			    "public team class Team1310sats2_2 extends Team1310sats2_1 {\n" +
			    "    class Role1310sats2 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.1(a)");
    }

    // a role class has weaker access rights than its implicit superrole
    // 1.3.11-otjld-weaker-access-than-superrole-1
    public void test1311_weakerAccessThanSuperrole1() {
        runConformTest(
            new String[] {
		"Team1311wats1_1.java",
			    "\n" +
			    "public team class Team1311wats1_1 {\n" +
			    "    protected class Role1311wats1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team1311wats1_2.java",
			    "\n" +
			    "public team class Team1311wats1_2 extends Team1311wats1_1 {\n" +
			    "    public class Role1311wats1 {}\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a role class has weaker access rights than its implicit superrole
    // 1.3.11-otjld-weaker-access-than-superrole-2
    public void test1311_weakerAccessThanSuperrole2() {
        runConformTest(
            new String[] {
		"Team1311wats2_1.java",
			    "\n" +
			    "public team class Team1311wats2_1 {\n" +
			    "    protected class Role1311wats2 {}\n" +
			    "}\n" +
			    "    \n",
		"Team1311wats2_2.java",
			    "\n" +
			    "public team class Team1311wats2_2 extends Team1311wats2_1 {\n" +
			    "    public class Role1311wats2 {}\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a role class has other access rights than its explicit superrole
    // 1.3.12-otjld-other-access-than-explicit-superrole-1
    public void test1312_otherAccessThanExplicitSuperrole1() {
        runConformTest(
            new String[] {
		"Team1312oates1.java",
			    "\n" +
			    "public team class Team1312oates1 {\n" +
			    "    protected class Role1312oates1_1 {}\n" +
			    "    public class Role1312oates1_2 extends Role1312oates1_1 {}\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a role class has other access rights than its explicit superrole
    // 1.3.12-otjld-other-access-than-explicit-superrole-2
    public void test1312_otherAccessThanExplicitSuperrole2() {
        runConformTest(
            new String[] {
		"Team1312oates2.java",
			    "\n" +
			    "public team class Team1312oates2 {\n" +
			    "    public class Role1312oates2_1 {}\n" +
			    "    protected class Role1312oates2_2 extends Role1312oates2_1 {}\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a redefined method in a role class has weaker access rights than in its implicit superrole
    // 1.3.13-otjld-weaker-redefined-method-access-1
    public void test1313_weakerRedefinedMethodAccess1() {
        runConformTest(
            new String[] {
		"Team1313wrma1_1.java",
			    "\n" +
			    "public team class Team1313wrma1_1 {\n" +
			    "    public class Role1313wrma1 {\n" +
			    "        protected void test() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1313wrma1_2.java",
			    "\n" +
			    "public team class Team1313wrma1_2 extends Team1313wrma1_1 {\n" +
			    "    public class Role1313wrma1 {\n" +
			    "        public void test() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a redefined method in a role class has weaker access rights than in its explicit superrole
    // 1.3.13-otjld-weaker-redefined-method-access-2
    public void test1313_weakerRedefinedMethodAccess2() {
        runConformTest(
            new String[] {
		"Team1313wrma2.java",
			    "\n" +
			    "public team class Team1313wrma2 {\n" +
			    "    public class Role1313wrma2_1 {\n" +
			    "        void test() {}\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role1313wrma2_2 extends Role1313wrma2_1 {\n" +
			    "        public void test() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a redefined method in a role class has stronger access rights than in its implicit superrole
    // 1.3.14-otjld-stronger-redefined-method-access-1
    public void test1314_strongerRedefinedMethodAccess1() {
        runNegativeTestMatching(
            new String[] {
		"Team1314srma1_1.java",
			    "\n" +
			    "public team class Team1314srma1_1 {\n" +
			    "    public class Role1314srma1 {\n" +
			    "        public void test() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1314srma1_2.java",
			    "\n" +
			    "public team class Team1314srma1_2 extends Team1314srma1_1 {\n" +
			    "    public class Role1314srma1 {\n" +
			    "        protected void test() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.1(h)");
    }

    // a redefined method in a role class has stronger access rights than in its explicit superrole
    // 1.3.14-otjld-stronger-redefined-method-access-2
    public void test1314_strongerRedefinedMethodAccess2() {
        runNegativeTestMatching(
            new String[] {
		"Team1314srma2.java",
			    "\n" +
			    "public team class Team1314srma2 {\n" +
			    "    public class Role1314srma2_1 {\n" +
			    "        protected void test() {}\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role1314srma2_2 extends Role1314srma2_1 {\n" +
			    "        void test() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "visibility");
    }

    // a role class accesses a method of its implicit superclass using super
    // 1.3.15-otjld-feature-access-with-super-1
    public void test1315_featureAccessWithSuper1() {
        runNegativeTestMatching(
            new String[] {
		"Team1315faws1_1.java",
			    "\n" +
			    "public team class Team1315faws1_1 {\n" +
			    "    public class Role1315faws1 {\n" +
			    "        public void test() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1315faws1_2.java",
			    "\n" +
			    "public team class Team1315faws1_2 extends Team1315faws1_1 {\n" +
			    "    public class Role1315faws1 {\n" +
			    "        public void test() {\n" +
			    "            super.test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "undefined");
    }

    // a role class accesses a field of its implicit superclass using super
    // 1.3.15-otjld-feature-access-with-super-2
    public void test1315_featureAccessWithSuper2() {
        runNegativeTestMatching(
            new String[] {
		"Team1315faws2_1.java",
			    "\n" +
			    "public team class Team1315faws2_1 {\n" +
			    "    public class Role1315faws2 {\n" +
			    "        protected String attr;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1315faws2_2.java",
			    "\n" +
			    "public team class Team1315faws2_2 extends Team1315faws2_1 {\n" +
			    "    public class Role1315faws2 {\n" +
			    "        public String getValue() {\n" +
			    "            return super.attr;\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "resolved");
    }

    // a team accesses a bound feature of a role class defined in it without providing a base object
    // 1.3.16-otjld-role-feature-access-without-baseobject
    public void test1316_roleFeatureAccessWithoutBaseobject() {
        runNegativeTestMatching(
            new String[] {
		"T1316rfa.java",
			    "\n" +
			    "public class T1316rfa {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1316rfa.java",
			    "\n" +
			    "public team class Team1316rfa {\n" +
			    "    public String getValue() {\n" +
			    "        return new Role1316rfa().getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    protected class Role1316rfa playedBy T1316rfa {\n" +
			    "        protected abstract String getValue();\n" +
			    "        String getValue() -> String getValue() with {\n" +
			    "            result <- \"OK\"\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "2.3.1(b)");
    }

    // 
    // 1.3.17-otjld-multilevel-role-overriding-1
    public void test1317_multilevelRoleOverriding1() {
       
       runConformTest(
            new String[] {
		"Team1317mro1_3.java",
			    "\n" +
			    "public team class Team1317mro1_3 extends Team1317mro1_2 {\n" +
			    "    public class R {\n" +
			    "        public void test() { System.out.print(\"OK\"); }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1317mro1_3();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1317mro1_1.java",
			    "\n" +
			    "public team class Team1317mro1_1 {\n" +
			    "    public class R {\n" +
			    "        protected void test() { System.out.print(\"NOK\"); }\n" +
			    "    }\n" +
			    "    public Team1317mro1_1() {\n" +
			    "        R r = new R();\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"Team1317mro1_2.java",
			    "\n" +
			    "public team class Team1317mro1_2 extends Team1317mro1_1 {\n" +
			    "    public class R {\n" +
			    "        protected void test() { System.out.print(\"NOK2\"); }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a object of static non-role type is checked instanceof wrt a role type, witness for NPE reported by woll
    // 1.3.18-otjld-instanceof-non-role-to-role-1
    public void test1318_instanceofNonRoleToRole1() {
       
       runConformTest(
            new String[] {
		"Team1318inrtr1_2.java",
			    "\n" +
			    "public team class Team1318inrtr1_2 {\n" +
			    "    protected team class Inner playedBy Team1318inrtr1_1 {\n" +
			    "        void test(Object o) {\n" +
			    "            if (o instanceof R<@base>)\n" +
			    "                System.out.print(\"OK\");\n" +
			    "            else\n" +
			    "                System.out.print(\"NOK\");\n" +
			    "        }\n" +
			    "        protected Inner () {\n" +
			    "            base();\n" +
			    "            R<@base> r = getR();\n" +
			    "            test(r);\n" +
			    "        }\n" +
			    "        R<@base> getR() -> R<@base> getR();\n" +
			    "    }\n" +
			    "    Team1318inrtr1_2() {\n" +
			    "        new Inner();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1318inrtr1_2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1318inrtr1_1.java",
			    "\n" +
			    "public team class Team1318inrtr1_1 {\n" +
			    "    public class R0 {}\n" +
			    "    public class R extends R0 { }\n" +
			    "    public R getR() {\n" +
			    "        return new R();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // illegal attempting to import a role type
    // 1.3.19-otjld-role-import-1
    public void test1319_roleImport1() {
        runNegativeTestMatching(
            new String[] {
		"p1/Team1319ri1.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team1319ri1 {\n" +
			    "    public class R {}\n" +
			    "}\n" +
			    "    \n",
		"T1319ri1.java",
			    "\n" +
			    "import p1.Team1319ri1.R;\n" +
			    "public class T1319ri1 {}\n" +
			    "    \n"
            },
            "1.2.2(i)");
    }

    // a role declares a constant which is imported
    // 1.3.19-otjld-role-import-2
    public void test1319_roleImport2() {
       
       runConformTest(
            new String[] {
		"T1319ri2Main.java",
			    "\n" +
			    "import static p1.Team1319ri2.R.VAL;\n" +
			    "public class T1319ri2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        System.out.print(VAL);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p1/Team1319ri2.java",
			    "\n" +
			    "package p1;\n" +
			    "public team class Team1319ri2 {\n" +
			    "    public class R {\n" +
			    "        public static final String VAL = \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // EXPERIMENTAL, method arg
    // 1.3.20-otjld-tournament-polymorphism-1
    public void test1320_tournamentPolymorphism1() {
       
       runConformTest(
            new String[] {
		"Team1320tp1_3.java",
			    "\n" +
			    "public team class Team1320tp1_3 {\n" +
			    "    public final team class Nested extends Team1320tp1_1 {\n" +
			    "	public class R implements org.objectteams.ITeamMigratable {}\n" +
			    "    }\n" +
			    "    void test () {\n" +
			    "        final Nested n1 = new Nested();\n" +
			    "        final Nested n2 = new Nested();\n" +
			    "        R<@n1> r = new R<@n1>(\"OK\");\n" +
			    "        n2.setR(r.migrateToTeam(n2));\n" +
			    "        n2.test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1320tp1_3().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1320tp1_1.java",
			    "\n" +
			    "public team class Team1320tp1_1 {\n" +
			    "    public class R {\n" +
			    "        String val;\n" +
			    "        public R(String v) { this.val = v; }\n" +
			    "        protected String getVal() { return this.val; }\n" +
			    "    }\n" +
			    "    R r;\n" +
			    "    public void setR(R r) { this.r=r; }\n" +
			    "    public void test() {\n" +
			    "        System.out.print(r.getVal());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1320tp1_2.java",
			    "\n" +
			    "public team class Team1320tp1_2 extends Team1320tp1_1 { }\n" +
			    "    \n"
            },
            "OK");
    }

    // EXPERIMENTAL, assignment
    // 1.3.20-otjld-tournament-polymorphism-2
    public void test1320_tournamentPolymorphism2() {
       
       runConformTest(
            new String[] {
		"Team1320tp2_3.java",
			    "\n" +
			    "public team class Team1320tp2_3 {\n" +
			    "    public final team class Nested extends Team1320tp2_1 {\n" +
			    "	public class R implements org.objectteams.ITeamMigratable {}\n" +
			    "    }\n" +
			    "    void test () {\n" +
			    "        final Nested n1 = new Nested();\n" +
			    "        final Nested n2 = new Nested();\n" +
			    "        R<@n1> r = new R<@n1>(\"OK\");\n" +
			    "        R<@n2> r2 = r.migrateToTeam(n2);\n" +
			    "        n2.test(r2);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1320tp2_3().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1320tp2_1.java",
			    "\n" +
			    "public team class Team1320tp2_1 {\n" +
			    "    public class R {\n" +
			    "        String val;\n" +
			    "        public R(String v) { this.val = v; }\n" +
			    "        protected String getVal() { return this.val; }\n" +
			    "    }\n" +
			    "    public void test(R r) {\n" +
			    "        System.out.print(r.getVal());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1320tp2_2.java",
			    "\n" +
			    "public team class Team1320tp2_2 extends Team1320tp2_1 { }\n" +
			    "    \n"
            },
            "OK");
    }

    // EXPERIMENTAL, assignment, hierarchies on both sides, polymorphic use
    // 1.3.20-otjld-tournament-polymorphism-3
    public void test1320_tournamentPolymorphism3() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportOverrideFinalRole, CompilerOptions.IGNORE);
       
       runConformTest(
            new String[] {
		"Team1320tp3_4.java",
			    "\n" +
			    "public team class Team1320tp3_4 extends Team1320tp3_3 {\n" +
			    "    public final team class Nested extends Team1320tp3_2 {\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1320tp3_4().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1320tp3_1.java",
			    "\n" +
			    "public team class Team1320tp3_1 {\n" +
			    "    public class R {\n" +
			    "        String val;\n" +
			    "        public R(String v) { this.val = v; }\n" +
			    "        protected String getVal() { return this.val; }\n" +
			    "    }\n" +
			    "    public void test(R r) {\n" +
			    "        System.out.print(r.getVal());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1320tp3_2.java",
			    "\n" +
			    "public team class Team1320tp3_2 extends Team1320tp3_1 { }\n" +
			    "    \n",
		"Team1320tp3_3.java",
			    "\n" +
			    "public team class Team1320tp3_3 {\n" +
			    "    public final team class Nested extends Team1320tp3_1 {\n" +
			    "	     public class R implements org.objectteams.ITeamMigratable {}\n" +
			    "    }\n" +
			    "    void test () {\n" +
			    "        final Nested n1 = new Nested();\n" +
			    "        final Nested n2 = new Nested();\n" +
			    "        R<@n1> r = new R<@n1>(\"OK\");\n" +
			    "        R<@n2> r2 = r.migrateToTeam(n2);\n" +
			    "        n2.test(r2);\n" +
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

    // EXPERIMENTAL, assignment, hierarchies on both sides, polymorphic use, method is witness for tricky resolving of anchored types
    // 1.3.20-otjld-tournament-polymorphism-4
    public void test1320_tournamentPolymorphism4() {
       Map customOptions = getCompilerOptions();
       customOptions.put(CompilerOptions.OPTION_ReportOverrideFinalRole, CompilerOptions.IGNORE);
       
       runConformTest(
            new String[] {
		"Team1320tp4_4.java",
			    "\n" +
			    "public team class Team1320tp4_4 extends Team1320tp4_3 {\n" +
			    "    public final team class Nested extends Team1320tp4_2 {\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1320tp4_4().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1320tp4_1.java",
			    "\n" +
			    "public team class Team1320tp4_1 {\n" +
			    "    public class R {\n" +
			    "        String val;\n" +
			    "        public R(String v) { this.val = v; }\n" +
			    "        protected String getVal() { return this.val; }\n" +
			    "    }\n" +
			    "    public void test(R r) {\n" +
			    "        System.out.print(r.getVal());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1320tp4_2.java",
			    "\n" +
			    "public team class Team1320tp4_2 extends Team1320tp4_1 { }\n" +
			    "    \n",
		"Team1320tp4_3.java",
			    "\n" +
			    "public team class Team1320tp4_3 {\n" +
			    "    public final team class Nested extends Team1320tp4_1 {\n" +
			    "        public class R implements org.objectteams.ITeamMigratable {}\n" +
			    "    }\n" +
			    "    void test2(final Nested n1, final Nested n2) {\n" +
			    "        R<@n1> r1 = new R<@n1>(\"OK\");\n" +
			    "        n2.test(r1.migrateToTeam(n2));\n" +
			    "    }\n" +
			    "    void test () {\n" +
			    "        test2(new Nested(), new Nested());\n" +
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

    // a role migrates to a different team, wrong team
    // 1.3.21-otjld-team-migration-1
    public void test1321_teamMigration1() {
        runNegativeTestMatching(
            new String[] {
		"Team1321tm1.java",
			    "\n" +
			    "import org.objectteams.*;\n" +
			    "public final team class Team1321tm1 {\n" +
			    "    public class R implements ITeamMigratable {\n" +
			    "\n" +
			    "    }\n" +
			    "    void test(Team other) {\n" +
			    "        R r = new R();\n" +
			    "        r.migrateToTeam(other);\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        new Team1321tm1().test(null);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team1321tm1.java (at line 9)\n" + 
    		"	r.migrateToTeam(other);\n" + 
    		"	                ^^^^^\n" + 
    		"The special method R.migrateToTeam(Team1321tm1) is not applicable for the arguments (org.objectteams.Team).\n" + 
    		"----------\n");
    }

    // a role migrates to a different team, non-final team
    // 1.3.21-otjld-team-migration-2
    public void test1321_teamMigration2() {
        runNegativeTestMatching(
            new String[] {
		"Team1321tm2.java",
			    "\n" +
			    "import org.objectteams.*;\n" +
			    "public team class Team1321tm2 {\n" +
			    "    public class R implements ITeamMigratable {\n" +
			    "    }\n" +
			    "    void test(Team1321tm2 other) {\n" +
			    "        R r = new R();\n" +
			    "        r.migrateToTeam(other);\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        new Team1321tm2().test(null);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team1321tm2.java (at line 4)\n" + 
    		"	public class R implements ITeamMigratable {\n" + 
    		"	                          ^^^^^^^^^^^^^^^\n" + 
    		"Team migration not possible because enclosing team class Team1321tm2 is not final (OT/J experimental feature). \n" + 
    		"----------\n" + 
    		"2. ERROR in Team1321tm2.java (at line 8)\n" + 
    		"	r.migrateToTeam(other);\n" + 
    		"	  ^^^^^^^^^^^^^\n" + 
    		"The method migrateToTeam(Team1321tm2) is undefined for the type R<@tthis[Team1321tm2]>\n" + 
    		"----------\n");
    }

    // a role migrates to a different team, null team
    // 1.3.21-otjld-team-migration-3
    public void test1321_teamMigration3() {
       
       runConformTest(
            new String[] {
		"Team1321tm3.java",
			    "\n" +
			    "import org.objectteams.*;\n" +
			    "public final team class Team1321tm3 {\n" +
			    "    public class R implements ITeamMigratable { \n" +
			    "    }\n" +
			    "    void test(Team1321tm3 other) {\n" +
			    "        R r = new R();\n" +
			    "        try {\n" +
			    "            r.migrateToTeam(other);\n" +
			    "        } catch (NullPointerException npe) {\n" +
			    "            System.out.print(npe.getMessage());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        new Team1321tm3().test(null);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "Team argument must not be null");
    }

    // a role migrates to a different team, null team
    // 1.3.21-otjld-team-migration-4
    public void test1321_teamMigration4() {
        runNegativeTestMatching(
            new String[] {
		"Team1321tm4.java",
			    "\n" +
			    "import org.objectteams.*;\n" +
			    "public final team class Team1321tm4 {\n" +
			    "    public class R implements ITeamMigratable {\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        R r = new R();\n" +
			    "        r.migrateToTeam(null);\n" +
			    "    }\n" +
			    "    public static void main (String[] args) {\n" +
			    "        new Team1321tm4().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "not a valid new team anchor");
    }

    // a non-role tries to use team migration
    // 1.3.21-otjld-team-migration-5
    public void test1321_teamMigration5() {
        runNegativeTestMatching(
            new String[] {
		"T1321tm5.java",
			    "\n" +
			    "public class T1321tm5 implements org.objectteams.ITeamMigratable {\n" +
			    "    void foo(org.objectteams.Team aTeam) {\n" +
			    "        this.migrateToTeam(aTeam);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in T1321tm5.java (at line 2)\n" + 
    		"	public class T1321tm5 implements org.objectteams.ITeamMigratable {\n" + 
    		"	                                 ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
    		"Type T1321tm5 does not support team migration, only applicable for roles (OT/J experimental feature).\n" + 
    		"----------\n" + 
    		"2. ERROR in T1321tm5.java (at line 4)\n" + 
    		"	this.migrateToTeam(aTeam);\n" + 
    		"	     ^^^^^^^^^^^^^\n" + 
    		"The method migrateToTeam(Team) is undefined for the type T1321tm5\n" + 
    		"----------\n");
    }

    // an empty team inherits a role with a callin binding, regression: byte code copy did not find the callin wrapper's byte code
    // 1.3.22-otjld-inherited-callin-1
    public void test1322_inheritedCallin1() {
       runConformTest(
            new String[] {
		"Team1322ic1_2.java",
			    "\n" +
			    "public team class Team1322ic1_2 extends Team1322ic1_1 {\n" +
			    "    protected class R {}\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1322ic1_2().activate();\n" +
			    "        new T1322ic1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team1322ic1_1.java",
			    "public team class Team1322ic1_1 {\n" +
			    "    protected class R playedBy T1322ic1 {\n" +
			    "       private void o () { System.out.print(\"O\"); }\n" +
			    "       o <- before test;\n" +
			    "    }\n" +
			    "}",
		"T1322ic1.java",
			    "\n" +
			    "public class T1322ic1 {\n" +
			    "    public void test() {\n" +
			    "        System.out.print(\"K\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }
}
