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
package org.eclipse.objectteams.otdt.tests.otjld.rolesandteams;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

/**
 * Former jacks tests from section regular-role-inheritance (1.4.*)
 * @author stephan 
 */
public class RegularRoleInheritance extends AbstractOTJLDTest {
	
	public RegularRoleInheritance(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "testBug403396_2" };
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return RegularRoleInheritance.class;
	}
	
    // a role class extends a role from the same team and uses a feature of it
    // 1.4.1-otjld-same-team-super-role-access-1
    public void test141_sameTeamSuperRoleAccess1() {
       
       runConformTest(
            new String[] {
		"T141stsra1Main.java",
			    "\n" +
			    "public class T141stsra1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team141stsra1 t = new Team141stsra1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team141stsra1.java",
			    "\n" +
			    "public team class Team141stsra1 {\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role141stsra1_2().getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    protected class Role141stsra1_1 {\n" +
			    "        public String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    protected class Role141stsra1_2 extends Role141stsra1_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class extends a implicitly inherited role from the same team and uses a feature of it
    // 1.4.1-otjld-same-team-super-role-access-2
    public void test141_sameTeamSuperRoleAccess2() {
       
       runConformTest(
            new String[] {
		"T141stsra2Main.java",
			    "\n" +
			    "public class T141stsra2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team141stsra2_2 t = new Team141stsra2_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team141stsra2_1.java",
			    "\n" +
			    "public team class Team141stsra2_1 {\n" +
			    "    protected class Role141stsra2_1 {\n" +
			    "        public String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team141stsra2_2.java",
			    "\n" +
			    "public team class Team141stsra2_2 extends Team141stsra2_1 {\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role141stsra2_2().getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    protected class Role141stsra2_2 extends Role141stsra2_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class extends an inherited role from the same team and uses a feature of it
    // 1.4.1-otjld-same-team-super-role-access-3
    public void test141_sameTeamSuperRoleAccess3() {
       
       runConformTest(
            new String[] {
		"T141stsra3Main.java",
			    "\n" +
			    "public class T141stsra3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team141stsra3_4 t = new Team141stsra3_4();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team141stsra3_1.java",
			    "\n" +
			    "public team class Team141stsra3_1 {\n" +
			    "    protected class Role141stsra3_1 {\n" +
			    "        public String getValueInternal() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team141stsra3_2.java",
			    "\n" +
			    "public team class Team141stsra3_2 extends Team141stsra3_1 {\n" +
			    "\n" +
			    "    protected class Role141stsra3_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team141stsra3_3.java",
			    "\n" +
			    "public team class Team141stsra3_3 extends Team141stsra3_2 {}\n" +
			    "    \n",
		"Team141stsra3_4.java",
			    "\n" +
			    "public team class Team141stsra3_4 extends Team141stsra3_3 {\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role141stsra3_2().getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    protected class Role141stsra3_2 extends Role141stsra3_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class redefines an implicitly inherited role
    // 1.4.2-otjld-role-redefinition-1
    public void test142_roleRedefinition1() {
       
       runConformTest(
            new String[] {
		"T142rr1Main.java",
			    "\n" +
			    "public class T142rr1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team142rr1_2 t = new Team142rr1_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team142rr1_1.java",
			    "\n" +
			    "public team class Team142rr1_1 {\n" +
			    "    protected class Role142rr1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team142rr1_2.java",
			    "\n" +
			    "public team class Team142rr1_2 extends Team142rr1_1 {\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role142rr1().getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role142rr1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class redefines an implicitly inherited role
    // 1.4.2-otjld-role-redefinition-2
    public void test142_roleRedefinition2() {
       
       runConformTest(
            new String[] {
		"T142rr2Main.java",
			    "\n" +
			    "public class T142rr2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team142rr2_3 t = new Team142rr2_3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team142rr2_1.java",
			    "\n" +
			    "public team class Team142rr2_1 {\n" +
			    "    protected class Role142rr2 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team142rr2_2.java",
			    "\n" +
			    "public team class Team142rr2_2 extends Team142rr2_1 {}\n" +
			    "    \n",
		"Team142rr2_3.java",
			    "\n" +
			    "public team class Team142rr2_3 extends Team142rr2_2 {\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role142rr2().getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    protected class Role142rr2 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class redefines an implicitly inherited role
    // 1.4.2-otjld-role-redefinition-3
    public void test142_roleRedefinition3() {
       
       runConformTest(
            new String[] {
		"T142rr3Main.java",
			    "\n" +
			    "public class T142rr3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team142rr3_3 t = new Team142rr3_3();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team142rr3_1.java",
			    "\n" +
			    "public team class Team142rr3_1 {\n" +
			    "    protected class Role142rr3_1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team142rr3_2.java",
			    "\n" +
			    "public team class Team142rr3_2 extends Team142rr3_1 {\n" +
			    "    protected class Role142rr3_2 extends Role142rr3_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team142rr3_3.java",
			    "\n" +
			    "public team class Team142rr3_3 extends Team142rr3_2 {\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role142rr3_2().getValue();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role142rr3_2 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is implicitly cast to its direct base interface
    // 1.4.3-otjld-cast-to-baseinterface-1
    public void test143_castToBaseinterface1() {
       
       runConformTest(
            new String[] {
		"T143ctb1Main.java",
			    "\n" +
			    "public class T143ctb1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team143ctb1 t = new Team143ctb1();\n" +
			    "        T143ctb1          o = t.new Role143ctb1();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T143ctb1.java",
			    "\n" +
			    "public interface T143ctb1 {\n" +
			    "    String getValue();\n" +
			    "}\n" +
			    "    \n",
		"Team143ctb1.java",
			    "\n" +
			    "public team class Team143ctb1 {\n" +
			    "    public class Role143ctb1 implements T143ctb1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is explicitly cast to a base interface inherited from its implicit super role
    // 1.4.3-otjld-cast-to-baseinterface-2
    public void test143_castToBaseinterface2() {
       
       runConformTest(
            new String[] {
		"T143ctb2Main.java",
			    "\n" +
			    "public class T143ctb2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team143ctb2_2 t = new Team143ctb2_2();\n" +
			    "        T143ctb2      o = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T143ctb2.java",
			    "\n" +
			    "public interface T143ctb2 {\n" +
			    "    String getValue();\n" +
			    "}\n" +
			    "    \n",
		"Team143ctb2_1.java",
			    "\n" +
			    "public team class Team143ctb2_1 {\n" +
			    "    protected class Role143ctb2 implements T143ctb2 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team143ctb2_2.java",
			    "\n" +
			    "public team class Team143ctb2_2 extends Team143ctb2_1 {\n" +
			    "    public class Role143ctb2 {}\n" +
			    "\n" +
			    "    public T143ctb2 getRole() {\n" +
			    "        return new Role143ctb2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is implicitly cast to a base interface inherited from its explicit super role
    // 1.4.3-otjld-cast-to-baseinterface-3
    public void test143_castToBaseinterface3() {
       
       runConformTest(
            new String[] {
		"T143ctb3Main.java",
			    "\n" +
			    "public class T143ctb3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team143ctb3_2 t = new Team143ctb3_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T143ctb3.java",
			    "\n" +
			    "public interface T143ctb3 {\n" +
			    "    String getValue();\n" +
			    "}\n" +
			    "    \n",
		"Team143ctb3_1.java",
			    "\n" +
			    "public team class Team143ctb3_1 {\n" +
			    "    protected class Role143ctb3_1 implements T143ctb3 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team143ctb3_2.java",
			    "\n" +
			    "public team class Team143ctb3_2 extends Team143ctb3_1 {\n" +
			    "    public class Role143ctb3_2 extends Role143ctb3_1 {}\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return ((T143ctb3)(new Role143ctb3_2())).getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is implicitly cast to the normal super class of its implicit super role
    // 1.4.4-otjld-cast-to-superclass-1
    public void test144_castToSuperclass1() {
       
       runConformTest(
            new String[] {
		"T144cts1Main.java",
			    "\n" +
			    "public class T144cts1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team144cts1_2 t = new Team144cts1_2();\n" +
			    "        T144cts1            o = t.new Role144cts1();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T144cts1.java",
			    "\n" +
			    "class T144cts1 {\n" +
			    "    protected String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team144cts1_1.java",
			    "\n" +
			    "public team class Team144cts1_1 {\n" +
			    "    protected class Role144cts1 extends T144cts1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team144cts1_2.java",
			    "\n" +
			    "public team class Team144cts1_2 extends Team144cts1_1 {\n" +
			    "    public class Role144cts1 {\n" +
			    "		 public Role144cts1() {}\n" +
			    "	 }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is explicitly cast to the normal super class of its explicit super role
    // 1.4.4-otjld-cast-to-superclass-2
    public void test144_castToSuperclass2() {
       
       runConformTest(
            new String[] {
		"T144cts2Main.java",
			    "\n" +
			    "public class T144cts2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team144cts2 t = new Team144cts2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T144cts2.java",
			    "\n" +
			    "public class T144cts2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team144cts2.java",
			    "\n" +
			    "public team class Team144cts2 {\n" +
			    "    protected class Role144cts2_1 extends T144cts2 {}\n" +
			    "    public class Role144cts2_2 extends Role144cts2_1 {}\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        T144cts2 o = (T144cts2)(new Role144cts2_2());\n" +
			    "\n" +
			    "        return o.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class is implicitly cast to the normal super class of a remote super role
    // 1.4.4-otjld-cast-to-superclass-3
    public void test144_castToSuperclass3() {
       
       runConformTest(
            new String[] {
		"T144cts3Main.java",
			    "\n" +
			    "public class T144cts3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team144cts3_3 t = new Team144cts3_3();\n" +
			    "        T144cts3      o = t.getObject();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T144cts3.java",
			    "\n" +
			    "public class T144cts3 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team144cts3_1.java",
			    "\n" +
			    "public team class Team144cts3_1 {\n" +
			    "    protected class Role144cts3_1 extends T144cts3 {}\n" +
			    "}\n" +
			    "    \n",
		"Team144cts3_2.java",
			    "\n" +
			    "public team class Team144cts3_2 extends Team144cts3_1 {\n" +
			    "    protected class Role144cts3_2 extends Role144cts3_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team144cts3_3.java",
			    "\n" +
			    "public team class Team144cts3_3 extends Team144cts3_2 {\n" +
			    "    public T144cts3 getObject() {\n" +
			    "        return new Role144cts3_2(); // <--\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // trying to externalize protected role
    // 1.4.4-otjld-cast-to-superclass-4
    public void test144_castToSuperclass4() {
        runNegativeTest(
            new String[] {
		"T144cts4.java",
			    "\n" +
			    "class T144cts4 {\n" +
			    "    protected String getValue() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team144cts4_1.java",
			    "\n" +
			    "public team class Team144cts4_1 {\n" +
			    "    protected class Role144cts4 extends T144cts4 {}\n" +
			    "}\n" +
			    "    \n",
		"Team144cts4_2.java",
			    "\n" +
			    "public team class Team144cts4_2 extends Team144cts4_1 {\n" +
			    "    protected class Role144cts4 {}\n" +
			    "}\n" +
			    "    \n",
		"T144cts4Main.java",
			    "\n" +
			    "public class T144cts4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team144cts4_2 t = new Team144cts4_2();\n" +
			    "        T144cts4            o = t.new Role144cts4();\n" +
			    "\n" +
			    "        System.out.print(o.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role class has a different extends clause than its implicit super role
    // 1.4.5-otjld-change-of-extends-clause-1
    public void test145_changeOfExtendsClause1() {
        runNegativeTest(
            new String[] {
		"T145coec1_1.java",
			    "\n" +
			    "public class T145coec1_1 {}\n" +
			    "    \n",
		"T145coec1_2.java",
			    "\n" +
			    "public class T145coec1_2 {}\n" +
			    "    \n",
		"Team145coec1_1.java",
			    "\n" +
			    "public team class Team145coec1_1 {\n" +
			    "    public class Role144coec1 extends T145coec1_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team145coec1_2.java",
			    "\n" +
			    "public team class Team145coec1_2 extends Team145coec1_1 {\n" +
			    "    public class Role144coec1 extends T145coec1_2 {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role class has a different extends clause than its implicit super role
    // 1.4.5-otjld-change-of-extends-clause-2
    public void test145_changeOfExtendsClause2() {
        runConformTest(
            new String[] {
		"T145coec2_1.java",
			    "\n" +
			    "public class T145coec2_1 {}\n" +
			    "    \n",
		"T145coec2_2.java",
			    "\n" +
			    "public class T145coec2_2 extends T145coec2_1 {}\n" +
			    "    \n",
		"Team145coec2_1.java",
			    "\n" +
			    "public team class Team145coec2_1 {\n" +
			    "    public class Role144coec2 extends T145coec2_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team145coec2_2.java",
			    "\n" +
			    "public team class Team145coec2_2 extends Team145coec2_1 {\n" +
			    "    public class Role144coec2 extends T145coec2_2 {}\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a role class has an extends clause whereas its implicit super role has none
    // 1.4.5-otjld-change-of-extends-clause-3
    public void test145_changeOfExtendsClause3() {
        runConformTest(
            new String[] {
		"T145coec3.java",
			    "\n" +
			    "public class T145coec3 {}\n" +
			    "    \n",
		"Team145coec3_1.java",
			    "\n" +
			    "public team class Team145coec3_1 {\n" +
			    "    public class Role144coec3 {}\n" +
			    "}\n" +
			    "    \n",
		"Team145coec3_2.java",
			    "\n" +
			    "public team class Team145coec3_2 extends Team145coec3_1 {\n" +
			    "    public class Role144coec3 extends T145coec3 {}\n" +
			    "}\n" +
			    "    \n"
            });
    }

    // a role class has a normal super class, and invokes the super constructor
    // 1.4.6-otjld-super-constructor-access-1
    public void test146_superConstructorAccess1() {
       
       runConformTest(
            new String[] {
		"T146sca1Main.java",
			    "\n" +
			    "public class T146sca1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team146sca1 t = new Team146sca1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T146sca1.java",
			    "\n" +
			    "public class T146sca1 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    protected T146sca1() {\n" +
			    "        value = \"OK\";\n" +
			    "    }\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team146sca1.java",
			    "\n" +
			    "public team class Team146sca1 {\n" +
			    "    public class Role146sca1 extends T146sca1 {\n" +
			    "        public Role146sca1() {\n" +
			    "            super();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role146sca1().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class has a normal super class, and invokes a super constructor with a different signature
    // 1.4.6-otjld-super-constructor-access-2
    public void test146_superConstructorAccess2() {
       
       runConformTest(
            new String[] {
		"T146sca2Main.java",
			    "\n" +
			    "public class T146sca2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team146sca2 t = new Team146sca2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T146sca2.java",
			    "\n" +
			    "public class T146sca2 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    public T146sca2(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "    public String getValue() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team146sca2.java",
			    "\n" +
			    "public team class Team146sca2 {\n" +
			    "    public class Role146sca2 extends T146sca2 {\n" +
			    "        public Role146sca2() {\n" +
			    "            super(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role146sca2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a field of its normal super class without using super
    // 1.4.7-otjld-access-to-superclass-feature-1
    public void test147_accessToSuperclassFeature1() {
       
       runConformTest(
            new String[] {
		"T147atsf1Main.java",
			    "\n" +
			    "public class T147atsf1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team147atsf1 t = new Team147atsf1();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf1.java",
			    "\n" +
			    "public class T147atsf1 {\n" +
			    "    protected String value = \"OK\";\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf1.java",
			    "\n" +
			    "public team class Team147atsf1 {\n" +
			    "    public class Role147atsf1 extends T147atsf1 {\n" +
			    "        public String getValue() {\n" +
			    "            return value;\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role147atsf1().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a method of the normal super class of its implicit superrole without using super
    // 1.4.7-otjld-access-to-superclass-feature-2
    public void test147_accessToSuperclassFeature2() {
       
       runConformTest(
            new String[] {
		"T147atsf2Main.java",
			    "\n" +
			    "public class T147atsf2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team147atsf2_2 t = new Team147atsf2_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf2.java",
			    "\n" +
			    "public class T147atsf2 {\n" +
			    "    public String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf2_1.java",
			    "\n" +
			    "public team class Team147atsf2_1 {\n" +
			    "    public class Role147atsf2 extends T147atsf2 {}\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf2_2.java",
			    "\n" +
			    "public team class Team147atsf2_2 extends Team147atsf2_1 {\n" +
			    "    public class Role147atsf2 {\n" +
			    "        public String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return new Role147atsf2().getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a static method of the normal super class of its implicit superrole without using super
    // 1.4.7-otjld-access-to-superclass-feature-2s
    public void test147_accessToSuperclassFeature2s() {
       
       runConformTest(
            new String[] {
		"T147atsf2sMain.java",
			    "\n" +
			    "public class T147atsf2sMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team147atsf2s_2 t = new Team147atsf2s_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf2s.java",
			    "\n" +
			    "public class T147atsf2s {\n" +
			    "    public static String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf2s_1.java",
			    "\n" +
			    "public team class Team147atsf2s_1 {\n" +
			    "    public class Role147atsf2s extends T147atsf2s {}\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf2s_2.java",
			    "\n" +
			    "public team class Team147atsf2s_2 extends Team147atsf2s_1 {\n" +
			    "    public class Role147atsf2s {\n" +
			    "        public static String getValue() {\n" +
			    "            return getValueInternal();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return Role147atsf2s.getValue();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team class accesses a static method of a role's normal super class of its implicit superrole without using super
    // 1.4.7-otjld_unsupported_access-to-superclass-feature-2t
    public void _unsupported_test147_accessToSuperclassFeature2t() {
       
       runConformTest(
            new String[] {
		"T147atsf2tMain.java",
			    "\n" +
			    "public class T147atsf2tMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team147atsf2t_2 t = new Team147atsf2t_2();\n" +
			    "\n" +
			    "        System.out.print(t.getValue());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf2t.java",
			    "\n" +
			    "public class T147atsf2t {\n" +
			    "    public static String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf2t_1.java",
			    "\n" +
			    "public team class Team147atsf2t_1 {\n" +
			    "    public class Role147atsf2t extends T147atsf2t {}\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf2t_2.java",
			    "\n" +
			    "public team class Team147atsf2t_2 extends Team147atsf2t_1 {\n" +
			    "    public class Role147atsf2t {\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue() {\n" +
			    "        return Role147atsf2t.getValueInternal();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team class accesses a static method of an externalized role's normal super class of its implicit superrole without using super - SYNTAX NOT SUPPORTED
    // 1.4.7-otjld_unsupported_access-to-superclass-feature-2u
    public void _unsupported_test147accessToSuperclassFeature2u() {
       
       runConformTest(
            new String[] {
		"T147atsf2uMain.java",
			    "\n" +
			    "public class T147atsf2uMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team147atsf2u_2 t = new Team147atsf2u_2();\n" +
			    "\n" +
			    "        System.out.print(Role147atsf2u<@t>.getValueInternal());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf2u.java",
			    "\n" +
			    "public class T147atsf2u {\n" +
			    "    public static String getValueInternal() {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf2u_1.java",
			    "\n" +
			    "public team class Team147atsf2u_1 {\n" +
			    "    public class Role147atsf2u extends T147atsf2u {}\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf2u_2.java",
			    "\n" +
			    "public team class Team147atsf2u_2 extends Team147atsf2u_1 {\n" +
			    "    public class Role147atsf2u {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a final method of the normal super class
    // 1.4.7-otjld-access-to-superclass-feature-3
    public void test147_accessToSuperclassFeature3() {
       
       runConformTest(
            new String[] {
		"Team147atsf3.java",
			    "\n" +
			    "public team class Team147atsf3 {\n" +
			    "    protected class R extends T147atsf3_2 {\n" +
			    "    }\n" +
			    "    protected void test() {\n" +
			    "        R r = new R();\n" +
			    "        r.ok();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team147atsf3().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf3_1.java",
			    "\n" +
			    "public class T147atsf3_1 {\n" +
			    "    public final void ok () {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf3_2.java",
			    "\n" +
			    "public class T147atsf3_2 extends T147atsf3_1 {\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a final method of the normal super class, insufficient visibility (limitation), NOTE: when method was copied, IBM-JVM reported: "JVMVRFY007 final method overridden"
    // 1.4.7-otjld-access-to-superclass-feature-3f
    public void test147_accessToSuperclassFeature3f() {
        runNegativeTestMatching(
            new String[] {
		"T147atsf3f_1.java",
			    "\n" +
			    "public class T147atsf3f_1 {\n" +
			    "    protected final void ok () {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf3f_2.java",
			    "\n" +
			    "public class T147atsf3f_2 extends T147atsf3f_1 {\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf3f.java",
			    "\n" +
			    "public team class Team147atsf3f {\n" +
			    "    protected class R extends T147atsf3f_2 {\n" +
			    "    }\n" +
			    "    protected void test() {\n" +
			    "        R r = new R();\n" +
			    "        r.ok();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.3.2(e)");
    }

    // a role accesses a protected method inherited from its regular super class
    // 1.4.7-otjld-access-to-superclass-feature-4
    public void test147_accessToSuperclassFeature4() {
       
       runConformTest(
            new String[] {
		"Team147atsf4.java",
			    "\n" +
			    "public team class Team147atsf4 {\n" +
			    "    protected class R extends T147atsf4 {\n" +
			    "        protected void bridge() {\n" +
			    "            ok();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team147atsf4() {\n" +
			    "        new R().bridge();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team147atsf4();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf4.java",
			    "\n" +
			    "public class T147atsf4 {\n" +
			    "    protected void ok() {\n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role tries to access a private method from its regular superclass
    // 1.4.7-otjld-access-to-superclass-feature-5
    public void test147_accessToSuperclassFeature5() {
        runNegativeTestMatching(
            new String[] {
		"T147atsf5.java",
			    "\n" +
			    "public class T147atsf5 {\n" +
			    "    @SuppressWarnings(\"unused\") // accessed via decapsulation\n" +
			    "    private void notVisible() { }\n" +
			    "}\n" +
			    "    \n",
		"Team147atsf5.java",
			    "\n" +
			    "public team class Team147atsf5 {\n" +
			    "    protected class R extends T147atsf5 {\n" +
			    "        void test() {\n" +
			    "            notVisible();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team147atsf5.java (at line 5)\n" + 
    		"	notVisible();\n" + 
    		"	^^^^^^^^^^\n" + 
    		"The method notVisible() from the type T147atsf5 is not visible\n" + 
    		"----------\n");
    }

    // was falsly reporting ambiguity between a local method and a private method from its regular superclass
    // 1.4.7-otjld-access-to-superclass-feature-6
    public void test147_accessToSuperclassFeature6() {
       
       runConformTest(
            new String[] {
		"Team147atsf6.java",
			    "\n" +
			    "public team class Team147atsf6 {\n" +
			    "    protected class R extends T147atsf6_1 playedBy T147atsf6_2 {\n" +
			    "        void meth(int i) {\n" +
			    "            System.out.print(i);\n" +
			    "        }\n" +
			    "        meth <- after baseMeth;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team147atsf6().activate();\n" +
			    "        new T147atsf6_2().baseMeth(14);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf6_1.java",
			    "\n" +
			    "public class T147atsf6_1 {\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    private void meth() { }\n" +
			    "}\n" +
			    "    \n",
		"T147atsf6_2.java",
			    "\n" +
			    "public class T147atsf6_2 {\n" +
			    "    void baseMeth(int i) { }\n" +
			    "}\n" +
			    "    \n"
            },
            "14");
    }

    // a role class extends a role from another, unrelated team using an anchored type
    // 1.4.8-otjld-extending-role-from-unrelated-team
    public void test148_extendingRoleFromUnrelatedTeam1() {
        runNegativeTest(
            new String[] {
		"Team148erfut1_1.java",
			    "\n" +
			    "public team class Team148erfut1_1 {\n" +
			    "    public class Role148erfut1_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team148erfut1_2.java",
			    "\n" +
			    "public team class Team148erfut1_2 {\n" +
			    "    final Team148erfut1_1 t = new Team148erfut1_1();\n" +
			    "\n" +
			    "    public class Role148erfut1_2 extends t.Role148erfut1_1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team148erfut1_2.java (at line 5)\n" + 
    		"	public class Role148erfut1_2 extends t.Role148erfut1_1 {}\n" + 
    		"	                                     ^\n" + 
    		"t cannot be resolved to a type\n" + 
    		"----------\n");
    }
    
    // same as above but using new syntax depdendent type
    public void test148_extendingRoleFromUnrelatedTeam2() {
        runNegativeTest(
            new String[] {
		"Team148erfut2_1.java",
			    "\n" +
			    "public team class Team148erfut2_1 {\n" +
			    "    public class Role148erfut2_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team148erfut2_2.java",
			    "\n" +
			    "public team class Team148erfut2_2 {\n" +
			    "    final Team148erfut2_1 t = new Team148erfut2_1();\n" +
			    "\n" +
			    "    public class Role148erfut2_2 extends Role148erfut2_1<@t> {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team148erfut2_2.java (at line 5)\n" + 
    		"	public class Role148erfut2_2 extends Role148erfut2_1<@t> {}\n" + 
    		"	                                     ^^^^^^^^^^^^^^^\n" + 
    		"Cannot sub-class an externalized role type (OTJLD 1.2.2(g)).\n" + 
    		"----------\n");
    }

    // a role class extends a role from the superteam using an anchored type
    // 1.4.9-otjld-extending-role-from-superteam-1
    public void test149_extendingRoleFromSuperteam1() {
        runNegativeTest(
            new String[] {
		"Team149erfs1_1.java",
			    "\n" +
			    "public team class Team149erfs1_1 {\n" +
			    "    public class Role149erfs1_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team149erfs1_2.java",
			    "\n" +
			    "public team class Team149erfs1_2 extends Team149erfs1_1 {\n" +
			    "    final Team149erfs1_1 t = new Team149erfs1_1();\n" +
			    "\n" +
			    "    public class Role149erfs1_2 extends t.Role149erfs1_1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role class extends a role from the superteam using an anchored type
    // 1.4.9-otjld-extending-role-from-superteam-2
    public void test149_extendingRoleFromSuperteam2() {
        runNegativeTest(
            new String[] {
		"Team149erfs2_1.java",
			    "\n" +
			    "public team class Team149erfs2_1 {\n" +
			    "    public class Role149erfs2_1 {}\n" +
			    "}\n" +
			    "    \n",
		"Team149erfs2_2.java",
			    "\n" +
			    "public team class Team149erfs2_2 extends Team149erfs2_1 {\n" +
			    "    public class Role149erfs2_2 extends tsuper.Role149erfs2_1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a callout binding overrides a method from the explicit (non-role) superclass - witness for TPX-416
    // 1.4.10-otjld-callout-overrides-explicitly-inheritd-1
    public void test1410_calloutOverridesExplicitlyInheritd1() {
       
       runConformTest(
            new String[] {
		"Team1410cowi1.java",
			    "\n" +
			    "public team class Team1410cowi1 {\n" +
			    "    protected class R extends T1410cowi1_1 playedBy T1410cowi1_2 {\n" +
			    "        protected R() {\n" +
			    "            base();\n" +
			    "        }\n" +
			    "        int test() => int testOK();\n" +
			    "    }\n" +
			    "    public Team1410cowi1() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1410cowi1();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T1410cowi1_1.java",
			    "\n" +
			    "public class T1410cowi1_1 {\n" +
			    "    public int test() { System.out.print(\"NOK\"); return 0; }\n" +
			    "}\n" +
			    "    \n",
		"T1410cowi1_2.java",
			    "\n" +
			    "public class T1410cowi1_2 {\n" +
			    "    public int testOK() { System.out.print(\"OK\"); return 1; }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role class accesses a public field from its regular super class
    // 1.4.11-otjld-field-of-regular-super-1
    public void test1411_fieldOfRegularSuper1() {
       
       runConformTest(
            new String[] {
		"Team1411fors1.java",
			    "\n" +
			    "public team class Team1411fors1 {\n" +
			    "    protected class R extends T1411fors1 {\n" +
			    "        protected R(String v) {\n" +
			    "            super(v);\n" +
			    "        }\n" +
			    "        protected void test() {\n" +
			    "            System.out.print(this.val);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        new R(\"OK\").test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1411fors1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1411fors1.java",
			    "\n" +
			    "public class T1411fors1 {\n" +
			    "    public String val;\n" +
			    "    T1411fors1(String v) {\n" +
			    "        this.val = v;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role accesses a public field from its regular super class - different instances
    // 1.4.11-otjld-field-of-regular-super-2
    public void test1411_fieldOfRegularSuper2() {
       
       runConformTest(
            new String[] {
		"Team1411fors2.java",
			    "\n" +
			    "public team class Team1411fors2 {\n" +
			    "    protected class R extends T1411fors2 {\n" +
			    "        protected R(String v) {\n" +
			    "            super(v);\n" +
			    "        }\n" +
			    "        protected void test(R other) {\n" +
			    "            System.out.print(other.val);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        new R(\"this\").test(new R(\"OK\"));\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1411fors2().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1411fors2.java",
			    "\n" +
			    "public class T1411fors2 {\n" +
			    "    public String val;\n" +
			    "    T1411fors2(String v) {\n" +
			    "        this.val = v;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team accesses a public field from its role's regular super class
    // 1.4.11-otjld-field-of-regular-super-3
    public void test1411_fieldOfRegularSuper3() {
       
       runConformTest(
            new String[] {
		"Team1411fors3.java",
			    "\n" +
			    "public team class Team1411fors3 {\n" +
			    "    protected class R extends T1411fors3 {\n" +
			    "        protected R(String v) {\n" +
			    "            super(v);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        System.out.print(new R(\"OK\").val);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team1411fors3().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T1411fors3.java",
			    "\n" +
			    "public class T1411fors3 {\n" +
			    "    public String val;\n" +
			    "    T1411fors3(String v) {\n" +
			    "        this.val = v;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a team tries to access an inaccessible (protected) field from its role's regular super class
    // 1.4.11-otjld-field-of-regular-super-4
    public void test1411_fieldOfRegularSuper4() {
        runNegativeTestMatching(
            new String[] {
		"p1/T1411fors4.java",
			    "\n" +
			    "package p1;\n" +
			    "public class T1411fors4 {\n" +
			    "    protected String val;\n" +
			    "    protected T1411fors4(String v) {\n" +
			    "        this.val = v;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"p2/Team1411fors4.java",
			    "\n" +
			    "package p2;\n" +
			    "import p1.T1411fors4;\n" +
			    "public team class Team1411fors4 {\n" +
			    "    protected class R extends T1411fors4 {\n" +
			    "        protected R(String v) {\n" +
			    "            super(v);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    protected void test(R other) {\n" +
			    "         System.out.print(other.val);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "cannot be resolved");
    }

    public void testBug403396_1() {
    	runConformTest(
    		new String[] {
    			"base/BGraph.java",
    			"package base;\n" + 
    			"\n" + 
    			"public team class BGraph {\n" + 
    			"	public class BNode { }\n" + 
    			"	public class BEdge {}\n" + 
    			"}\n",
    			"features/Features.java",
    			"package features;\n" + 
    			"import base base.BGraph;\n" + 
    			"public team class Features {\n" + 
    			"	public team class BasicGraph playedBy BGraph {\n" + 
    			"		public BasicGraph() { base(); }\n" + 
    			"		public class Edge playedBy BEdge<@base> {\n" + 
    			"			public Edge() { base(); }\n" + 
    			"		}\n" + 
    			"	}\n" + 
    			"	public team class WeightedGraph extends BasicGraph {\n" + 
    			"	}\n" + 
    			"}\n"
    		});
    }

    public void testBug403396_2() {
    	runConformTest(
    		new String[] {
    			"base/BGraph.java",
    			"package base;\n" + 
    			"\n" + 
    			"public team class BGraph {\n" + 
    			"	public class BNode { }\n" + 
    			"	public class BEdge {}\n" + 
    			"}\n",
    			"features/Features.java",
    			"package features;\n" + 
    			"import base base.BGraph;\n" + 
    			"public team class Features {\n" + 
    			"	public team class BasicGraph playedBy BGraph {\n" + 
    			"		public BasicGraph() { base(); }\n" + 
    			"		public class Edge playedBy BEdge<@base> {\n" + 
    			"			public Edge() { base(); }\n" + 
    			"		}\n" + 
    			"	}\n" + 
    			"	public team class WeightedGraph extends BasicGraph {\n" +
    			"		@Override public class Edge {}\n" + 
    			"	}\n" + 
    			"}\n"
    		});
    }
}
