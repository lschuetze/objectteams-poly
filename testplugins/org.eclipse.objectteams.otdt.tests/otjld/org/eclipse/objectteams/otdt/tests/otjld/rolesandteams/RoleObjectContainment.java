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
 * Former jacks tests from section role-object-containment (1.1.*)
 * @author stephan
 */
public class RoleObjectContainment extends AbstractOTJLDTest {

	public RoleObjectContainment(String name) {
		super(name);
	}

	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test117_nestedClassClashesWithInherited1" };
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return RoleObjectContainment.class;
	}

	// 1.1.1-otjld-team-method-invocation-1
	// role invokes method of enclosing team using the unqualified name
	public void testTeamMethodInvocation1() {
		runConformTest(
			new String[] {
			"T111tmi1Main.java",
				"public class T111tmi1Main {\n" +
				"    public static void main(String[] args) {\n" +
				"        final Team111tmi1 t = new Team111tmi1();\n" +
				"        Role111tmi1<@t>   r = t.getRole();\n" +
				"\n" +
				"        System.out.print(r.test());\n" +
				"    }\n" +
				"}",
			"Team111tmi1.java",
				"public team class Team111tmi1 {\n" +
				"    private String getValue()\n" +
				"    {\n" +
				"        return \"OK\";\n" +
				"    }\n" +
				"\n" +
				"    public Role111tmi1 getRole() {\n" +
				"        return new Role111tmi1();\n" +
				"    }\n" +
				"\n" +
				"    public class Role111tmi1 {\n" +
				"        public String test() {\n" +
				"            return getValue();\n" +
				"        }\n" +
				"    }\n" +
				"}",
			},
			"OK");
	}
    // role invokes method of enclosing team using the unqualified name
    // 1.1.1-otjld-team-method-invocation-1
    public void test111_teamMethodInvocation1() {
        runConformTest(
            new String[] {
		"T111tmi1Main.java",
			    "\n" +
			    "public class T111tmi1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team111tmi1 t = new Team111tmi1();\n" +
			    "        Role111tmi1<@t>   r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team111tmi1.java",
			    "\n" +
			    "public team class Team111tmi1 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role111tmi1 getRole() {\n" +
			    "        return new Role111tmi1();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role111tmi1 {\n" +
			    "        public String test() {\n" +
			    "            return getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of enclosing team using unqualified name from an inner class of the role class
    // 1.1.1-otjld-team-method-invocation-2
    public void test111_teamMethodInvocation2() {
        runConformTest(
            new String[] {
		"T111tmi2Main.java",
			    "\n" +
			    "public class T111tmi2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team111tmi2 t = new Team111tmi2();\n" +
			    "        Role111tmi2<@t>   r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team111tmi2.java",
			    "\n" +
			    "public team class Team111tmi2 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role111tmi2 getRole() {\n" +
			    "        return new Role111tmi2();\n" +
			    "    }\n" +
			    "\n" +
			    "    public team class Role111tmi2 {\n" +
			    "        protected class Inner111tmi2 {\n" +
			    "            public String test() {\n" +
			    "                return getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public String test() {\n" +
			    "            return new Inner111tmi2().test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of enclosing team using unqualified name from an local class in the role class
    // 1.1.1-otjld-team-method-invocation-3
    public void test111_teamMethodInvocation3() {
        runConformTest(
            new String[] {
		"T111tmi3Main.java",
			    "\n" +
			    "public class T111tmi3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team111tmi3 t = new Team111tmi3();\n" +
			    "        Role111tmi3<@t>   r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team111tmi3.java",
			    "\n" +
			    "public team class Team111tmi3 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role111tmi3 getRole() {\n" +
			    "        return new Role111tmi3();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role111tmi3 {\n" +
			    "        public String test() {\n" +
			    "            class Local111tmi3 {\n" +
			    "                String test() {\n" +
			    "                    return getValue();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            return new Local111tmi3().test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of enclosing team using qualified this
    // 1.1.2-otjld-team-method-invocation-1
    public void test112_teamMethodInvocation1() {
        runConformTest(
            new String[] {
		"T112tmi1Main.java",
			    "\n" +
			    "public class T112tmi1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team112tmi1 t = new Team112tmi1();\n" +
			    "        Role112tmi1<@t>   r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team112tmi1.java",
			    "\n" +
			    "public team class Team112tmi1 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role112tmi1 getRole() {\n" +
			    "        return new Role112tmi1();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role112tmi1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String test() {\n" +
			    "            return Team112tmi1.this.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of enclosing team using qualified this from an inner class of the role class
    // 1.1.2-otjld-team-method-invocation-2
    public void test112_teamMethodInvocation2() {
        runConformTest(
            new String[] {
		"T112tmi2Main.java",
			    "\n" +
			    "public class T112tmi2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team112tmi2 t = new Team112tmi2();\n" +
			    "        Role112tmi2<@t>   r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team112tmi2.java",
			    "\n" +
			    "public team class Team112tmi2 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role112tmi2 getRole() {\n" +
			    "        return new Role112tmi2();\n" +
			    "    }\n" +
			    "\n" +
			    "    public team class Role112tmi2 {\n" +
			    "        protected class Inner112tmi2 {\n" +
			    "            public String getValue() {\n" +
			    "                return Team112tmi2.this.getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String test() {\n" +
			    "            return new Inner112tmi2().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of enclosing team using qualified this from an local class in the role class
    // 1.1.2-otjld-team-method-invocation-3
    public void test112_teamMethodInvocation3() {
        runConformTest(
            new String[] {
		"T112tmi3Main.java",
			    "\n" +
			    "public class T112tmi3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team112tmi3 t = new Team112tmi3();\n" +
			    "        Role112tmi3<@t>   r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team112tmi3.java",
			    "\n" +
			    "public team class Team112tmi3 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role112tmi3 getRole() {\n" +
			    "        return new Role112tmi3();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role112tmi3 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String test() {\n" +
			    "            class Local112tmi3 {\n" +
			    "                String getValue() {\n" +
			    "                    return Team112tmi3.this.getValue();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            return new Local112tmi3().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role local type invokes a private method of the enclosing team
    // 1.1.2-otjld-team-method-invocation-4
    public void test112_teamMethodInvocation4() {
        runConformTest(
            new String[] {
		"Team112tmi4.java",
			    "\n" +
			    "public team class Team112tmi4 {\n" +
			    "    private void test() { \n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "    protected class R {\n" +
			    "        protected void test() {\n" +
			    "            new Object() {\n" +
			    "                void test() {\n" +
			    "                    Team112tmi4.this.test();\n" +
			    "                }\n" +
			    "            }.test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team112tmi4() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team112tmi4();\n" +
			    "    } \n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role local type invokes a private method of the enclosing team (which again is a role of an outer)
    // 1.1.2-otjld-team-method-invocation-4a
    public void test112_teamMethodInvocation4a() {
        runConformTest(
            new String[] {
		"Team112tmi4a.java",
			    "\n" +
			    "public team class Team112tmi4a {\n" +
			    "    protected team class Mid {\n" +
			    "        private void test() { \n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "        protected class R {\n" +
			    "            protected void test() {\n" +
			    "                new Object() {\n" +
			    "                    void test() {\n" +
			    "                        Mid.this.test();\n" +
			    "                    }\n" +
			    "                }.test();\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public Mid() {\n" +
			    "            new R().test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team112tmi4a() {\n" +
			    "        new Mid();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team112tmi4a();\n" +
			    "    } \n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a named role local type invokes a private method of the enclosing team
    // 1.1.2-otjld-team-method-invocation-4l
    public void test112_teamMethodInvocation4l() {
        runConformTest(
            new String[] {
		"Team112tmi4l.java",
			    "\n" +
			    "public team class Team112tmi4l {\n" +
			    "    private void test() { \n" +
			    "        System.out.print(\"OK\");\n" +
			    "    }\n" +
			    "    protected class R {\n" +
			    "        protected void test() {\n" +
			    "            class Local {\n" +
			    "                void test() {\n" +
			    "                    Team112tmi4l.this.test();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            new Local().test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team112tmi4l() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team112tmi4l();\n" +
			    "    } \n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role local type invokes a private method of the enclosing team
    // 1.1.2-otjld-team-method-invocation-5
    public void test112_teamMethodInvocation5() {
        runConformTest(
            new String[] {
		"Team112tmi5.java",
			    "\n" +
			    "public team class Team112tmi5 {\n" +
			    "    private String val = \"OK\";\n" +
			    "    protected class R {\n" +
			    "        protected void test() {\n" +
			    "            new Object() {\n" +
			    "                void test() {\n" +
			    "                    System.out.print(Team112tmi5.this.val);\n" +
			    "                }\n" +
			    "            }.test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team112tmi5() {\n" +
			    "        new R().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team112tmi5();\n" +
			    "    } \n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role local type invokes a private method of the enclosing team
    // 1.1.2-otjld-team-method-invocation-5a
    public void test112_teamMethodInvocation5a() {
        runConformTest(
            new String[] {
		"Team112tmi5a.java",
			    "\n" +
			    "public team class Team112tmi5a {\n" +
			    "    protected team class Mid {\n" +
			    "        private String val = \"OK\";\n" +
			    "        protected class R {\n" +
			    "            protected void test() {\n" +
			    "                new Object() {\n" +
			    "                    void test() {\n" +
			    "                        System.out.print(Mid.this.val);\n" +
			    "                    }\n" +
			    "                }.test();\n" +
			    "            }\n" +
			    "        }\n" +
			    "        protected Mid() {\n" +
			    "            new R().test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public Team112tmi5a() {\n" +
			    "        new Mid();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team112tmi5a();\n" +
			    "    } \n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of outermost normal class using the unqualified name
    // 1.1.3-otjld-method-invocation-1
    public void test113_methodInvocation1() {
        runConformTest(
            new String[] {
		"T113mi1Main.java",
			    "\n" +
			    "public class T113mi1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final T113mi1.Team113mi1 t = new T113mi1().new Team113mi1();\n" +
			    "        Role113mi1<@t>           r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T113mi1.java",
			    "\n" +
			    "public class T113mi1 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "    public team class Team113mi1 {\n" +
			    "        public Role113mi1 getRole() {\n" +
			    "            return new Role113mi1();\n" +
			    "        }\n" +
			    "    \n" +
			    "        public class Role113mi1 {\n" +
			    "            public String test() {\n" +
			    "                return getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of outermost normal class using unqualified name from an inner class of the role class
    // 1.1.3-otjld-method-invocation-2
    public void test113_methodInvocation2() {
        runConformTest(
            new String[] {
		"T113mi2Main.java",
			    "\n" +
			    "public class T113mi2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final T113mi2.Team113mi2 t = new T113mi2().new Team113mi2();\n" +
			    "        Role113mi2<@t>           r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T113mi2.java",
			    "\n" +
			    "public class T113mi2 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "    public team class Team113mi2 {\n" +
			    "    \n" +
			    "        public Role113mi2 getRole() {\n" +
			    "            return new Role113mi2();\n" +
			    "        }\n" +
			    "    \n" +
			    "        public team class Role113mi2 {\n" +
			    "            protected class Inner113mi2 {\n" +
			    "                public String test() {\n" +
			    "                    return getValue();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            public String test() {\n" +
			    "                return new Inner113mi2().test();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of outermost normal class using unqualified name from an local class in the role class
    // 1.1.3-otjld-method-invocation-3
    public void test113_methodInvocation3() {
        runConformTest(
            new String[] {
		"T113mi3Main.java",
			    "\n" +
			    "public class T113mi3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final T113mi3.Team113mi3 t = new T113mi3().new Team113mi3();\n" +
			    "        Role113mi3<@t>           r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T113mi3.java",
			    "\n" +
			    "public class T113mi3 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "    public team class Team113mi3 {\n" +
			    "    \n" +
			    "        public Role113mi3 getRole() {\n" +
			    "            return new Role113mi3();\n" +
			    "        }\n" +
			    "    \n" +
			    "        public class Role113mi3 {\n" +
			    "            public String test() {\n" +
			    "                class Local113mi3 {\n" +
			    "                    String test() {\n" +
			    "                        return getValue();\n" +
			    "                    }\n" +
			    "                }\n" +
			    "                return new Local113mi3().test();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role local type tries to invoke a role method using unqualified this
    // 1.1.3-otjld-method-invocation-4
    public void test113_methodInvocation4() {
        runNegativeTestMatching(
            new String[] {
		"Team113mi4.java",
			    "\n" +
			    "public team class Team113mi4 {\n" +
			    "    protected class R {\n" +
			    "        void print() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "        protected void test() {\n" +
			    "            new Runnable() {\n" +
			    "                public void run() {\n" +
			    "                    this.print();\n" +
			    "                }\n" +
			    "            }.run();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team113mi4().new R().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "undefined");
    }

    // a role local type invokes a role method using qualified this
    // 1.1.3-otjld-method-invocation-5
    public void test113_methodInvocation5() {
        runConformTest(
            new String[] {
		"Team113mi5.java",
			    "\n" +
			    "public team class Team113mi5 {\n" +
			    "    protected class R {\n" +
			    "        void print() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "        protected void test() {\n" +
			    "            new Runnable() {\n" +
			    "                public void run() {\n" +
			    "                    R.this.print();\n" +
			    "                }\n" +
			    "            }.run();\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team113mi5().new R().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of outermost normal class using qualified this
    // 1.1.4-otjld-method-invocation-1
    public void test114_methodInvocation1() {
        runConformTest(
            new String[] {
		"T114mi1Main.java",
			    "\n" +
			    "public class T114mi1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final T114mi1.Team114mi1 t = new T114mi1().new Team114mi1();\n" +
			    "        Role114mi1<@t>           r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T114mi1.java",
			    "\n" +
			    "public class T114mi1 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "    public team class Team114mi1 {\n" +
			    "        public Role114mi1 getRole() {\n" +
			    "            return new Role114mi1();\n" +
			    "        }\n" +
			    "    \n" +
			    "        public class Role114mi1 {\n" +
			    "            public String getValue() {\n" +
			    "                return \"NOTOK\";\n" +
			    "            }\n" +
			    "            public String test() {\n" +
			    "                return T114mi1.this.getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of outermost normal class using qualified this from an inner class of the role class
    // 1.1.4-otjld-method-invocation-2
    public void test114_methodInvocation2() {
        runConformTest(
            new String[] {
		"T114mi2Main.java",
			    "\n" +
			    "public class T114mi2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final T114mi2.Team114mi2 t = new T114mi2().new Team114mi2();\n" +
			    "        Role114mi2<@t>           r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T114mi2.java",
			    "\n" +
			    "public class T114mi2 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "    public team class Team114mi2 {\n" +
			    "    \n" +
			    "        public Role114mi2 getRole() {\n" +
			    "            return new Role114mi2();\n" +
			    "        }\n" +
			    "    \n" +
			    "        public team class Role114mi2 {\n" +
			    "            protected class Inner114mi2 {\n" +
			    "                public String test() {\n" +
			    "                    return T114mi2.this.getValue();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            public String getValue() {\n" +
			    "                return \"NOTOK\";\n" +
			    "            }\n" +
			    "            public String test() {\n" +
			    "                return new Inner114mi2().test();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // role invokes method of outermost normal class using qualified this from an local class in the role class
    // 1.1.4-otjld-method-invocation-3
    public void test114_methodInvocation3() {
        runConformTest(
            new String[] {
		"T114mi3Main.java",
			    "\n" +
			    "public class T114mi3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final T114mi3.Team114mi3 t = new T114mi3().new Team114mi3();\n" +
			    "        Role114mi3<@t>           r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T114mi3.java",
			    "\n" +
			    "public class T114mi3 {\n" +
			    "    private String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "    public team class Team114mi3 {\n" +
			    "    \n" +
			    "        public Role114mi3 getRole() {\n" +
			    "            return new Role114mi3();\n" +
			    "        }\n" +
			    "    \n" +
			    "        public class Role114mi3 {\n" +
			    "            public String getValue() {\n" +
			    "                return \"NOTOK\";\n" +
			    "            }\n" +
			    "            public String test() {\n" +
			    "                class Local114mi3 {\n" +
			    "                    String test() {\n" +
			    "                        return T114mi3.this.getValue();\n" +
			    "                    }\n" +
			    "                }\n" +
			    "                return new Local114mi3().test();\n" +
			    "            }\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role invokes a method on a (inherited) inner of a sibling role
    // 1.1.4-otjld-method-invocation-5
    public void test114_methodInvocation5() {
        runConformTest(
            new String[] {
		"Team114mi5.java",
			    "\n" +
			    "public team class Team114mi5 {\n" +
			    "    protected class R1 extends T114mi5 {\n" +
			    "        Inner getInner() { return new Inner(); }\n" +
			    "    }\n" +
			    "    protected class R2 {\n" +
			    "        protected void test() {\n" +
			    "            R1 r1 = new R1();\n" +
			    "            R1.Inner inner = r1.new Inner();\n" +
			    "            System.out.print(inner.getIt());\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        new R2().test();\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team114mi5().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T114mi5.java",
			    "\n" +
			    "public class T114mi5 {\n" +
			    "    public class Inner {\n" +
			    "        String getIt() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // team inherits a role that calls a redefined method of the enclosing team using qualified this
    // 1.1.5-otjld-team-method-invocation-1
    public void test115_teamMethodInvocation1() {
        runConformTest(
            new String[] {
		"T115tmi1Main.java",
			    "\n" +
			    "public class T115tmi1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team115tmi1_2 t = new Team115tmi1_2();\n" +
			    "        Role115tmi1<@t>     r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team115tmi1_1.java",
			    "\n" +
			    "public team class Team115tmi1_1 {\n" +
			    "    protected String getValue()\n" +
			    "    {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role115tmi1 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String test() {\n" +
			    "            return Team115tmi1_1.this.getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team115tmi1_2.java",
			    "\n" +
			    "public team class Team115tmi1_2 extends Team115tmi1_1 {\n" +
			    "    protected String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role115tmi1 getRole() {\n" +
			    "        return new Role115tmi1();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // team indirectly inherits a role that has a local class that calls a redefined method of the enclosing team using qualified this
    // 1.1.5-otjld-team-method-invocation-2
    public void test115_teamMethodInvocation2() {
        runConformTest(
            new String[] {
		"T115tmi2Main.java",
			    "\n" +
			    "public class T115tmi2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team115tmi2_3 t = new Team115tmi2_3();\n" +
			    "        Role115tmi2<@t>     r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team115tmi2_1.java",
			    "\n" +
			    "public team class Team115tmi2_1 {\n" +
			    "    protected String getValue()\n" +
			    "    {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role115tmi2 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String test() {\n" +
			    "            class Local115tmi2 {\n" +
			    "                String getValue() {\n" +
			    "                    return Team115tmi2_1.this.getValue();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            return new Local115tmi2().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team115tmi2_2.java",
			    "\n" +
			    "public team class Team115tmi2_2 extends Team115tmi2_1 {}\n" +
			    "    \n",
		"Team115tmi2_3.java",
			    "\n" +
			    "public team class Team115tmi2_3 extends Team115tmi2_2 {\n" +
			    "    protected String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role115tmi2 getRole() {\n" +
			    "        return new Role115tmi2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // team indirectly inherits a role that has a nested class that calls a redefined method of the enclosing team using qualified this
    // 1.1.5-otjld-team-method-invocation-3
    public void test115_teamMethodInvocation3() {
        runConformTest(
            new String[] {
		"T115tmi3Main.java",
			    "\n" +
			    "public class T115tmi3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team115tmi3_3 t = new Team115tmi3_3();\n" +
			    "        Role115tmi3<@t>     r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team115tmi3_1.java",
			    "\n" +
			    "public team class Team115tmi3_1 {\n" +
			    "    protected String getValue()\n" +
			    "    {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public team class Role115tmi3 {\n" +
			    "        protected class Nested115tmi3 {\n" +
			    "            public String getValue() {\n" +
			    "                return Team115tmi3_1.this.getValue();\n" +
			    "            }\n" +
			    "        }\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String test() {\n" +
			    "            return new Nested115tmi3().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team115tmi3_2.java",
			    "\n" +
			    "public team class Team115tmi3_2 extends Team115tmi3_1 {}\n" +
			    "    \n",
		"Team115tmi3_3.java",
			    "\n" +
			    "public team class Team115tmi3_3 extends Team115tmi3_2 {\n" +
			    "    protected String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role115tmi3 getRole() {\n" +
			    "        return new Role115tmi3();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // team directly inherits a role that has a local class that calls a redefined method of the enclosing team using qualified this
    // 1.1.5-otjld-team-method-invocation-4
    public void test115_teamMethodInvocation4() {
        runConformTest(
            new String[] {
		"T115tmi4Main.java",
			    "\n" +
			    "public class T115tmi4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team115tmi4_2 t = new Team115tmi4_2();\n" +
			    "        Role115tmi4<@t>     r = t.getRole();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team115tmi4_1.java",
			    "\n" +
			    "public team class Team115tmi4_1 {\n" +
			    "    protected String getValue()\n" +
			    "    {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role115tmi4 {\n" +
			    "        public String getValue() {\n" +
			    "            return \"NOTOK\";\n" +
			    "        }\n" +
			    "        public String test() {\n" +
			    "            class Local115tmi4 {\n" +
			    "                String getValue() {\n" +
			    "                    return Team115tmi4_1.this.getValue();\n" +
			    "                }\n" +
			    "            }\n" +
			    "            return new Local115tmi4().getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team115tmi4_2.java",
			    "\n" +
			    "public team class Team115tmi4_2 extends Team115tmi4_1 {\n" +
			    "    protected String getValue()\n" +
			    "    {\n" +
			    "        return \"OK\";\n" +
			    "    }\n" +
			    "\n" +
			    "    public Role115tmi4 getRole() {\n" +
			    "        return new Role115tmi4();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an assignment to a qualified this is not allowed
    // 1.1.6-otjld-qualified-this-assignment
    public void test116_qualifiedThisAssignment() {
        runNegativeTest(
            new String[] {
		"Team116qta.java",
			    "\n" +
			    "public team class Team116qta {\n" +
			    "    public class Role116qta {\n" +
			    "        public void test() {\n" +
			    "            Team116qta.this = new Team116qta();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" +
    		"1. ERROR in Team116qta.java (at line 5)\n" +
    		"	Team116qta.this = new Team116qta();\n" +
    		"	^^^^^^^^^^^^^^^\n" +
    		"The left-hand side of an assignment must be a variable\n" +
    		"----------\n");
    }

    // a nested class within a role has a name clash with a nested class inherited from the tsuper role
    // 1.1.7-otjld-nested-class-clashes-with-inherited-1
    public void test117_nestedClassClashesWithInherited1() {
        runNegativeTest(
            new String[] {
		"Team117nccwi1_1.java",
			    "\n" +
			    "public team class Team117nccwi1_1 {\n" +
			    "	public class Role117nccwi1 {\n" +
			    "		class Nested {\n" +
			    "			void foo() { System.out.println(\"foo\"); }\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n",
		"Team117nccwi1_2.java",
			    "\n" +
			    "public team class Team117nccwi1_2 extends Team117nccwi1_1 {\n" +
			    "	public class Role117nccwi1 {\n" +
			    "		class Nested {\n" +
			    "			void bar() { System.out.println(\"bar\"); }\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role uses a nested class implicitly inherited from its tsuper role
    // 1.1.8-otjld-nested-class-used-in-tsub-1
    public void test118_nestedClassUsedInTsub1() {
        runConformTest(
            new String[] {
		"T118ncuit1Main.java",
			    "\n" +
			    "public team class T118ncuit1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team118ncuit1_2 t = new Team118ncuit1_2();\n" +
			    "		System.out.print(t.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n",
		"Team118ncuit1_1.java",
			    "\n" +
			    "public team class Team118ncuit1_1 {\n" +
			    "	public team class Role118ncuit {\n" +
			    "		public class Nested {\n" +
			    "			public String getValue() {\n" +
			    "				return \"OK\";\n" +
			    "			}\n" +
			    "		}\n" +
			    "		protected Nested getNested() {\n" +
			    "			return new Nested();\n" +
			    "		}\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n",
		"Team118ncuit1_2.java",
			    "\n" +
			    "public team class Team118ncuit1_2 extends Team118ncuit1_1 {\n" +
			    "	String getValue() {\n" +
			    "		final Role118ncuit r = new Role118ncuit();\n" +
			    "		Nested<@r> n = r.getNested();\n" +
			    "		return n.getValue();\n" +
			    "	}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // A team has a role which is again a team
    // 1.1.9-otjld-nested-team-1
    public void test119_nestedTeam1() {
        runConformTest(
            new String[] {
		"T119nt1Main.java",
			    "\n" +
			    "public class T119nt1Main {\n" +
			    "	public static void main(String[] args) {\n" +
			    "		Team119nt1 t = new Team119nt1();\n" +
			    "		System.out.print(t.getValue());\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n",
		"Team119nt1.java",
			    "\n" +
			    "public team class Team119nt1 {\n" +
			    "	public team class RT119nt1 {\n" +
			    "		protected class Role {\n" +
			    "			protected String getValue() {\n" +
			    "				return \"OK\";\n" +
			    "			}\n" +
			    "		}\n" +
			    "		protected String getValue() {\n" +
			    "			Role r = new Role();\n" +
			    "			return r.getValue();\n" +
			    "		}\n" +
			    "	}\n" +
			    "	public String getValue() {\n" +
			    "		RT119nt1 rt = new RT119nt1();\n" +
			    "		return rt.getValue();\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            "OK");
    }

    // A team has a role with member types but not marked as team
    // 1.1.9-otjld-nested-team-2
    public void test119_nestedTeam2() {
        runNegativeTest(
            new String[] {
		"Team119nt2.java",
			    "\n" +
			    "public team class Team119nt2 {\n" +
			    "	public class Role119nt2 {\n" +
			    "		class Nested {}\n" +
			    "	}\n" +
			    "}\n" +
			    "	\n"
            },
            null);
    }

    // A team class defines a static class
    // 1.1.9-otjld-static-team-member-1
    public void test119_staticTeamMember1() {
        runNegativeTestMatching(
            new String[] {
		"Team119stm1.java",
			    "\n" +
			    "public team class Team119stm1 {\n" +
			    "    public static class NotARole {\n" +
			    "        protected void test() {\n" +
			    "            System.out.print(\"OK\");\n" +
			    "        }\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        NotARole nar = new NotARole();\n" +
			    "        nar.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OTJLD 1.2.1");
    }
}
