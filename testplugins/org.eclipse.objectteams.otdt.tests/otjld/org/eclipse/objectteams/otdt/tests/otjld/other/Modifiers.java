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
package org.eclipse.objectteams.otdt.tests.otjld.other;

import java.util.Map;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;

public class Modifiers extends AbstractOTJLDTest {
	
	public Modifiers(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test713_roleImplementsInheritedAbstractMethod2"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}
	
	public static Test suite() {
		return buildComparableTestSuite(testClass());
	}

	public static Class testClass() {
		return Modifiers.class;
	}

	@Override
	@SuppressWarnings("unchecked") // working with raw map
	protected Map getCompilerOptions() {
		Map options = super.getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.WARNING); // many tests actually suppress this warning
		return options;
	}

    // role is declared private
    // 7.1.1-otjld-role-modifier-1
    public void test711_roleModifier1() {
        runNegativeTestMatching(
            new String[] {
		"Team711rm1.java",
			    "\n" +
			    "public team class Team711rm1 {\n" +
			    "    private class R { }\n" +
			    "}\n" +
			    "    \n"
            },
            "1.2.1(a)");
    }

    // role is declared readonly (limitation)
    // 7.1.1-otjld-role-modifier-2
    public void test711_roleModifier2() {
        runNegativeTestMatching(
            new String[] {
		"Team711rm2.java",
			    "\n" +
			    "public team class Team711rm2 {\n" +
			    "    protected readonly class R { }\n" +
			    "}\n" +
			    "    \n"
            },
            "not yet supported");
    }

    // a role not marked as abstract has an abstract method
    // 7.1.2-otjld-nonabstract-role-has-abstract-method-1
    public void test712_nonabstractRoleHasAbstractMethod1() {
        runNegativeTest(
            new String[] {
		"Team712nrham1.java",
			    "\n" +
			    "public team class Team712nrham1 {\n" +
			    "    public class Role712nrham1 {\n" +
			    "        protected abstract void test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role not marked as abstract has an abstract method from an interface
    // 7.1.2-otjld-nonabstract-role-has-abstract-method-2
    public void test712_nonabstractRoleHasAbstractMethod2() {
        runNegativeTest(
            new String[] {
		"T712nrham2.java",
			    "\n" +
			    "public interface T712nrham2 {\n" +
			    "    void test();\n" +
			    "}\n" +
			    "    \n",
		"Team712nrham2.java",
			    "\n" +
			    "public team class Team712nrham2 {\n" +
			    "    public class Role712nrham2 implements T712nrham2 {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role not marked as abstract has an abstract method from its normal superclass
    // 7.1.2-otjld-nonabstract-role-has-abstract-method-3
    public void test712_nonabstractRoleHasAbstractMethod3() {
        runNegativeTest(
            new String[] {
		"T712nrham3.java",
			    "\n" +
			    "public abstract class T712nrham3 {\n" +
			    "    protected abstract void test();\n" +
			    "}\n" +
			    "    \n",
		"Team712nrham3.java",
			    "\n" +
			    "public team class Team712nrham3 {\n" +
			    "    public class Role712nrham3 extends T712nrham3 {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role not marked as abstract has an unbound callin method 'inherited' from its implicit superrole
    // 7.1.2-otjld-nonabstract-role-has-abstract-method-4
    public void test712_nonabstractRoleHasAbstractMethod4() {
        runNegativeTest(
            new String[] {
		"Team712nrham4_1.java",
			    "\n" +
			    "public abstract team class Team712nrham4_1 {\n" +
			    "    public abstract class Role712nrham4 {\n" +
			    "        abstract void test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team712nrham4_2.java",
			    "\n" +
			    "public team class Team712nrham4_2 extends Team712nrham4_1 {\n" +
			    "    public class Role712nrham4 {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role not marked as abstract has an inherited unbound callin method
    // 7.1.2-otjld-nonabstract-role-has-abstract-method-5
    public void test712_nonabstractRoleHasAbstractMethod5() {
        runNegativeTest(
            new String[] {
		"Team712nrham5.java",
			    "\n" +
			    "public abstract team class Team712nrham5 {\n" +
			    "    public abstract class Role712nrham5_1 {\n" +
			    "        protected abstract void test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role712nrham5_2 extends Role712nrham5_1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a role implements an inherited abstract method
    // 7.1.3-otjld-role-implements-inherited-abstract-method-1
    public void test713_roleImplementsInheritedAbstractMethod1() {
       
       runConformTest(
            new String[] {
		"T713riiam1Main.java",
			    "\n" +
			    "public class T713riiam1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team713riiam1 t = new Team713riiam1();\n" +
			    "        Role713riiam1<@t>   r = t.new Role713riiam1();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T713riiam1.java",
			    "\n" +
			    "public interface T713riiam1 {\n" +
			    "    String test();\n" +
			    "}\n" +
			    "    \n",
		"Team713riiam1.java",
			    "\n" +
			    "public team class Team713riiam1 {\n" +
			    "    public class Role713riiam1 implements T713riiam1 {\n" +
			    "        public String test() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role implements an inherited abstract method
    // 7.1.3-otjld-role-implements-inherited-abstract-method-2
    public void test713_roleImplementsInheritedAbstractMethod2() {
       
       runConformTest(
            new String[] {
		"T713riiam2Main.java",
			    "\n" +
			    "public class T713riiam2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team713riiam2 t = new Team713riiam2();\n" +
			    "        Role713riiam2<@t>   r = t.new Role713riiam2();\n" +
			    "\n" +
			    "        System.out.print(r.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T713riiam2.java",
			    "\n" +
			    "public abstract class T713riiam2 {\n" +
			    "    abstract String test();\n" +
			    "}\n" +
			    "    \n",
		"Team713riiam2.java",
			    "\n" +
			    "public team class Team713riiam2 {\n" +
			    "    public class Role713riiam2 extends T713riiam2 {\n" +
			    "        @Override\n" +
			    "        public String test() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role implements an inherited abstract method
    // 7.1.3-otjld-role-implements-inherited-abstract-method-3
    public void test713_roleImplementsInheritedAbstractMethod3() {
       
       runConformTest(
            new String[] {
		"T713riiam3Main.java",
			    "\n" +
			    "public class T713riiam3Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team713riiam3 t = new Team713riiam3();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team713riiam3.java",
			    "\n" +
			    "public team class Team713riiam3 {\n" +
			    "    public abstract class Role713riiam3_1 {\n" +
			    "        protected abstract String test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role713riiam3_2 extends Role713riiam3_1 {\n" +
			    "        public String test() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        Role713riiam3_1 r = new Role713riiam3_2();\n" +
			    "\n" +
			    "        return r.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a role implements an inherited abstract method
    // 7.1.3-otjld-role-implements-inherited-abstract-method-4
    public void test713_roleImplementsInheritedAbstractMethod4() {
       
       runConformTest(
            new String[] {
		"T713riiam4Main.java",
			    "\n" +
			    "public class T713riiam4Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team713riiam4_2 t = new Team713riiam4_2();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team713riiam4_1.java",
			    "\n" +
			    "public abstract team class Team713riiam4_1 {\n" +
			    "    public abstract class Role713riiam4 {\n" +
			    "        protected abstract String test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        return new Role713riiam4().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team713riiam4_2.java",
			    "\n" +
			    "public team class Team713riiam4_2 extends Team713riiam4_1 {\n" +
			    "    public class Role713riiam4 {\n" +
			    "        public String test() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a non-abstract team has an abstract role and concrete subrole, and does not instantiate the abstract role
    // 7.1.6-otjld-nonabstract-team-with-abstract-role-1
    public void test716_nonabstractTeamWithAbstractRole1() {
       
       runConformTest(
            new String[] {
		"T716ntwar1Main.java",
			    "\n" +
			    "public class T716ntwar1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team716ntwar1 t = new Team716ntwar1();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team716ntwar1.java",
			    "\n" +
			    "public team class Team716ntwar1 {\n" +
			    "    public abstract class Role716ntwar1_1 {\n" +
			    "        protected abstract String test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role716ntwar1_2 extends Role716ntwar1_1 {\n" +
			    "        public String test() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        Role716ntwar1_1 r = new Role716ntwar1_2();\n" +
			    "\n" +
			    "        return r.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a non-abstract team has an abstract role and concrete subrole, and does not instantiate the abstract role
    // 7.1.6-otjld-nonabstract-team-with-abstract-role-2
    public void test716_nonabstractTeamWithAbstractRole2() {
       
       runConformTest(
            new String[] {
		"T716ntwar2Main.java",
			    "\n" +
			    "public class T716ntwar2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team716ntwar2_1 t = new Team716ntwar2_2();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T716ntwar2.java",
			    "\n" +
			    "public class T716ntwar2 {\n" +
			    "    private String value;\n" +
			    "\n" +
			    "    T716ntwar2(String value) {\n" +
			    "        this.value = value;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String toString() {\n" +
			    "        return value;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team716ntwar2_1.java",
			    "\n" +
			    "public abstract team class Team716ntwar2_1 {\n" +
			    "    public abstract class Role716ntwar2_1 {\n" +
			    "        public abstract String test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        return new Role716ntwar2_1().test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team716ntwar2_2.java",
			    "\n" +
			    "public team class Team716ntwar2_2 extends Team716ntwar2_1 {\n" +
			    "    public class Role716ntwar2_1 playedBy T716ntwar2 {\n" +
			    "        public Role716ntwar2_1() {\n" +
			    "            base(\"OK\");\n" +
			    "        }\n" +
			    "        test -> toString;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a non-abstract team has an abstract relevant role
    // 7.1.7-otjld-team-with-abstract-relevant-role-1
    public void test717_teamWithAbstractRelevantRole1() {
        runNegativeTestMatching(
            new String[] {
		"Team717twarr1.java",
			    "\n" +
			    "public team class Team717twarr1 {\n" +
			    "    public abstract class Role717twarr1_1 {\n" +
			    "        abstract protected void test();\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role717twarr1_2 extends Role717twarr1_1 {\n" +
			    "        @Override\n" +
			    "        public void test() {}\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test() {\n" +
			    "        Role717twarr1_1 r = new Role717twarr1_1();\n" +
			    "\n" +
			    "        r.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team717twarr1.java (at line 13)\n" + 
    		"	Role717twarr1_1 r = new Role717twarr1_1();\n" + 
    		"	                    ^^^^^^^^^^^^^^^^^^^^^\n" + 
    		"Team Team717twarr1 must be declared abstract, because abstract role Role717twarr1_1 is relevant (OTJLD 2.5(b)).\n" + 
    		"----------\n");
    }

    // a non-abstract team has an abstract relevant role
    // 7.1.7-otjld-team-with-abstract-relevant-role-2
    public void test717_teamWithAbstractRelevantRole2() {
        runNegativeTest(
            new String[] {
		"T717twarr2.java",
			    "\n" +
			    "public class T717twarr2 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n",
		"Team717twarr2_1.java",
			    "\n" +
			    "public abstract team class Team717twarr2_1 {\n" +
			    "    public abstract class Role717twarr2_1 playedBy T717twarr2 {\n" +
			    "        abstract void test();\n" +
			    "\n" +
			    "        callin void doSomething() {\n" +
			    "            base.doSomething();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team717twarr2_2.java",
			    "\n" +
			    "public team class Team717twarr2_2 extends Team717twarr2_1 {\n" +
			    "    public void test(T717twarr2 as Role717twarr2_1 obj) {}\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // an abstract role class is instantiated and the instantiation is performed in a concrete sub-team of the team definining the role
    // 7.1.9-otjld-abstract-role-class-instantiation-1
    public void test719_abstractRoleClassInstantiation1() {
       
       runConformTest(
            new String[] {
		"T719arci1Main.java",
			    "\n" +
			    "public class T719arci1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team719arci1_1 t = new Team719arci1_2();\n" +
			    "\n" +
			    "        System.out.print(t.test());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team719arci1_1.java",
			    "\n" +
			    "public abstract team class Team719arci1_1 {\n" +
			    "    public abstract class Role719arci1 {\n" +
			    "        public String toString() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public String test() {\n" +
			    "        Role719arci1 r = new Role719arci1();\n" +
			    "\n" +
			    "        return r.toString();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team719arci1_2.java",
			    "\n" +
			    "public team class Team719arci1_2 extends Team719arci1_1 {\n" +
			    "    public class Role719arci1 {}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // an abstract role class is instantiated and the instantiation is performed in a concrete sub-team of the team definining the role
    // 7.1.9-otjld-abstract-role-class-instantiation-2
    public void test719_abstractRoleClassInstantiation2() {
       
       runConformTest(
            new String[] {
		"T719arci2Main.java",
			    "\n" +
			    "public class T719arci2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team719arci2_1 t = new Team719arci2_3();\n" +
			    "        Role719arci2<@t>     r = t.new Role719arci2();\n" +
			    "\n" +
			    "        System.out.print(r.toString());\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team719arci2_1.java",
			    "\n" +
			    "public abstract team class Team719arci2_1 {\n" +
			    "    public abstract class Role719arci2 {\n" +
			    "        public String toString() {\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team719arci2_2.java",
			    "\n" +
			    "public abstract team class Team719arci2_2 extends Team719arci2_1 {\n" +
			    "    public class Role719arci2 {}\n" +
			    "}\n" +
			    "    \n",
		"Team719arci2_3.java",
			    "\n" +
			    "public team class Team719arci2_3 extends Team719arci2_2 {}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method with a callin modifier is invoked directly
    // 7.1.10-otjld-invocation-of-callin-method
    public void test7110_invocationOfCallinMethod() {
        runNegativeTestMatching(
            new String[] {
		"Team7110iocm.java",
			    "\n" +
			    "public team class Team7110iocm {\n" +
			    "    public class Role7110iocm playedBy T7110iocm {\n" +
			    "        callin void doSomething() {\n" +
			    "            base.doSomething();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public void test(T7110iocm as Role7110iocm obj) {\n" +
			    "        obj.doSomething();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7110iocm.java",
			    "\n" +
			    "public class T7110iocm {}\n" +
			    "    \n"
            },
            "4.2(d)");
    }

    // a method with a callin modifier is bound, but not as replace
    // 7.1.11-otjld-nonreplace-binding-of-callin-method-1
    public void test7111_nonreplaceBindingOfCallinMethod1() {
        runNegativeTest(
            new String[] {
		"T7111nbocm1.java",
			    "\n" +
			    "public class T7111nbocm1 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n",
		"Team7111nbocm1.java",
			    "\n" +
			    "public team class Team7111nbocm1 {\n" +
			    "    public class Role7111nbocm1 playedBy T7111nbocm1 {\n" +
			    "        callin void doSomething() {\n" +
			    "            base.doSomething();\n" +
			    "        }\n" +
			    "        doSomething <- after test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a method with a callin modifier is bound, but not as replace
    // 7.1.11-otjld-nonreplace-binding-of-callin-method-2
    public void test7111_nonreplaceBindingOfCallinMethod2() {
        runNegativeTest(
            new String[] {
		"T7111nbocm2.java",
			    "\n" +
			    "public class T7111nbocm2 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n",
		"Team7111nbocm2.java",
			    "\n" +
			    "public team class Team7111nbocm2 {\n" +
			    "    public class Role7111nbocm2 playedBy T7111nbocm2 {\n" +
			    "        callin void doSomething() {\n" +
			    "            base.doSomething();\n" +
			    "        }\n" +
			    "        void doSomething() <- before void test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a method with a callin modifier is callout bound
    // 7.1.11-otjld-nonreplace-binding-of-callin-method-3
    public void test7111_nonreplaceBindingOfCallinMethod3() {
        runNegativeTest(
            new String[] {
		"T7111nbocm3.java",
			    "\n" +
			    "public class T7111nbocm3 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n",
		"Team7111nbocm3.java",
			    "\n" +
			    "public team class Team7111nbocm3 {\n" +
			    "    public class Role7111nbocm3_1 playedBy T7111nbocm3 {\n" +
			    "        callin void doSomething() {\n" +
			    "            base.doSomething();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role7111nbocm3_2 extends Role7111nbocm3_1 {\n" +
			    "        doSomething => test;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            null);
    }

    // a method with a callin modifier overwrites an inherited non-callin method
    // 7.1.12-otjld-callin-method-overwrites-noncallin-method-1
    public void test7112_callinMethodOverwritesNoncallinMethod1() {
        runNegativeTestMatching(
            new String[] {
		"Team7112cmonm1.java",
			    "\n" +
			    "public team class Team7112cmonm1 {\n" +
			    "    public class Role7112cmonm1 extends T7112cmonm1 {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7112cmonm1.java",
			    "\n" +
			    "public class T7112cmonm1 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "4.2(d)");
    }

    // a method with a callin modifier overwrites an inherited non-callin method
    // 7.1.12-otjld-callin-method-overwrites-noncallin-method-2
    public void test7112_callinMethodOverwritesNoncallinMethod2() {
        runNegativeTest(
            new String[] {
		"Team7112cmonm2.java",
			    "\n" +
			    "public team class Team7112cmonm2 {\n" +
			    "    public class Role7112cmonm2 implements T7112cmonm2_1 playedBy T7112cmonm2_2 {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "        test <- replace doSomething;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7112cmonm2_1.java",
			    "\n" +
			    "public interface T7112cmonm2_1 {\n" +
			    "    void test();\n" +
			    "}\n" +
			    "    \n",
		"T7112cmonm2_2.java",
			    "\n" +
			    "public class T7112cmonm2_2 {\n" +
			    "    public void doSomething() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team7112cmonm2.java (at line 4)\n" + 
    		"	callin void test() {\n" + 
    		"	^^^^^^^^^^^^^^^^^^\n" + 
    		"Modifier \"callin\" not allowed here; trying to override a method from T7112cmonm2_1 which is not a callin method (OTJLD 4.2(d)). \n" + 
    		"----------\n");
    }

    // a method with a callin modifier overwrites an inherited callout binding
    // 7.1.12-otjld-callin-method-overwrites-noncallin-method-3
    public void test7112_callinMethodOverwritesNoncallinMethod3() {
        runNegativeTestMatching(
            new String[] {
		"Team7112cmonm3.java",
			    "\n" +
			    "public team class Team7112cmonm3 {\n" +
			    "    public class Role7112cmonm3_1 playedBy T7112cmonm3 {\n" +
			    "        public abstract void test();\n" +
			    "        test -> test;\n" +
			    "    }\n" +
			    "    public class Role7112cmonm3_2 extends Role7112cmonm3_1 {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7112cmonm3.java",
			    "\n" +
			    "public class T7112cmonm3 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "4.2(d)");
    }

    // a role inherits conflicting callin and non-callin methods
    // 7.1.12-otjld-callin-method-overwrites-noncallin-method-4
    public void test7112_callinMethodOverwritesNoncallinMethod4() {
        runNegativeTestMatching(
            new String[] {
		"Team7112cmonm4_2.java",
			    "\n" +
			    "public team class Team7112cmonm4_2 extends Team7112cmonm4_1 {\n" +
			    "    public class Role7112cmonm4 extends T7112cmonm4 { }\n" +
			    "}\n" +
			    "    \n",
		"T7112cmonm4.java",
			    "\n" +
			    "public class T7112cmonm4 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n",
		"Team7112cmonm4_1.java",
			    "\n" +
			    "public team class Team7112cmonm4_1 {\n" +
			    "    public class Role7112cmonm4  {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "4.2(d)");
    }

    // a method without a callin modifier overwrites an inherited callin method
    // 7.1.13-otjld-noncallin-method-overwrites-callin-method-1
    public void test7113_noncallinMethodOverwritesCallinMethod1() {
        runNegativeTestMatching(
            new String[] {
		"Team7113nmocm1.java",
			    "\n" +
			    "public team class Team7113nmocm1 {\n" +
			    "    public class Role7113nmocm1_1 playedBy T7113nmocm1 {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "        test <- replace test;\n" +
			    "    }\n" +
			    "    public class Role7113nmocm1_2 extends Role7113nmocm1_1 {\n" +
			    "        public void test() {}\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7113nmocm1.java",
			    "\n" +
			    "public class T7113nmocm1 {\n" +
			    "    public void test() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "4.2(d)");
    }

    // a method without a callin modifier overwrites an inherited callin method
    // 7.1.13-otjld-noncallin-method-overwrites-callin-method-2
    public void test7113_noncallinMethodOverwritesCallinMethod2() {
        runNegativeTestMatching(
            new String[] {
		"Team7113nmocm2_2.java",
			    "\n" +
			    "public abstract team class Team7113nmocm2_2 extends Team7113nmocm2_1 {\n" +
			    "    public abstract class Role7113nmocm2 {\n" +
			    "        public abstract void test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7113nmocm2.java",
			    "\n" +
			    "public class T7113nmocm2 {\n" +
			    "    public void doSomething() {}\n" +
			    "}\n" +
			    "    \n",
		"Team7113nmocm2_1.java",
			    "\n" +
			    "public team class Team7113nmocm2_1 {\n" +
			    "    public class Role7113nmocm2 playedBy T7113nmocm2 {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "        test <- replace doSomething;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "4.2(d)");
    }

    // a method without a callin modifier overwrites an inherited callin method
    // 7.1.13-otjld-noncallin-method-overwrites-callin-method-3
    public void test7113_noncallinMethodOverwritesCallinMethod3() {
        runNegativeTestMatching(
            new String[] {
		"Team7113nmocm3_2.java",
			    "\n" +
			    "public team class Team7113nmocm3_2 extends Team7113nmocm3_1 {\n" +
			    "    public class Role7113nmocm3_2 extends Role7113nmocm3_1 {\n" +
			    "        test => doSomething;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7113nmocm3.java",
			    "\n" +
			    "public class T7113nmocm3 {\n" +
			    "    public void doSomething() {}\n" +
			    "}\n" +
			    "    \n",
		"Team7113nmocm3_1.java",
			    "\n" +
			    "public team class Team7113nmocm3_1 {\n" +
			    "    public class Role7113nmocm3_1 playedBy T7113nmocm3 {\n" +
			    "        callin void test() {\n" +
			    "            base.test();\n" +
			    "        }\n" +
			    "        test <- replace doSomething;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "----------\n" + 
    		"1. ERROR in Team7113nmocm3_2.java (at line 4)\n" + 
    		"	test => doSomething;\n" + 
    		"	^^^^\n" + 
    		"Cannot bind method test() as callout; is a callin method (OTJLD 4.2(d)).\n" + 
    		"----------\n");
    }

    // a method with a callin modifier overwrites an inherited callin method
    // 7.1.14-otjld-callin-method-overwrites-callin-method-1
    public void test7114_callinMethodOverwritesCallinMethod1() {
       
       runConformTest(
            new String[] {
		"T7114cmocm1Main.java",
			    "\n" +
			    "public class T7114cmocm1Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team7114cmocm1 t = new Team7114cmocm1();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T7114cmocm1 o = new T7114cmocm1();\n" +
			    "\n" +
			    "            System.out.print(o.getValue());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7114cmocm1.java",
			    "\n" +
			    "public class T7114cmocm1 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7114cmocm1.java",
			    "\n" +
			    "public team class Team7114cmocm1 {\n" +
			    "    public class Role7114cmocm1_1 playedBy T7114cmocm1 {\n" +
			    "        callin String test() {\n" +
			    "            return base.test();\n" +
			    "        }\n" +
			    "        test <- replace getValue;\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role7114cmocm1_2 extends Role7114cmocm1_1 {\n" +
			    "        callin String test() {\n" +
			    "            base.test();\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method with a callin modifier overwrites an inherited callin method
    // 7.1.14-otjld-callin-method-overwrites-callin-method-2
    public void test7114_callinMethodOverwritesCallinMethod2() {
       
       runConformTest(
            new String[] {
		"T7114cmocm2Main.java",
			    "\n" +
			    "public class T7114cmocm2Main {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team7114cmocm2_1 t = new Team7114cmocm2_2();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T7114cmocm2 o = new T7114cmocm2();\n" +
			    "\n" +
			    "            System.out.print(o.getValue());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7114cmocm2.java",
			    "\n" +
			    "public class T7114cmocm2 {\n" +
			    "    public String getValue() {\n" +
			    "        return \"NOTOK\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7114cmocm2_1.java",
			    "\n" +
			    "public team class Team7114cmocm2_1 {\n" +
			    "    public class Role7114cmocm2 playedBy T7114cmocm2 {\n" +
			    "        callin String test() {\n" +
			    "            return base.test();\n" +
			    "        }\n" +
			    "        test <- replace getValue;\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7114cmocm2_2.java",
			    "\n" +
			    "public team class Team7114cmocm2_2 extends Team7114cmocm2_1 {\n" +
			    "    public class Role7114cmocm2 {\n" +
			    "        callin String test() {\n" +
			    "            base.test();\n" +
			    "            return \"OK\";\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a method with a callin modifier redefines an inaccessible non-callin method
    // 7.1.15-otjld-callin-method-redefines-noncallin-method
    public void test7115_callinMethodRedefinesNoncallinMethod() {
       
       runConformTest(
            new String[] {
		"T7115cmrnmMain.java",
			    "\n" +
			    "public class T7115cmrnmMain {\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team7115cmrnm t = new Team7115cmrnm();\n" +
			    "\n" +
			    "        within (t) {\n" +
			    "            T7115cmrnm o = new T7115cmrnm();\n" +
			    "\n" +
			    "            System.out.print(t.getValue(o));\n" +
			    "            System.out.print(o.test());\n" +
			    "        }\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7115cmrnm.java",
			    "\n" +
			    "public class T7115cmrnm {\n" +
			    "    public String test() {\n" +
			    "        return \"\";\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"Team7115cmrnm.java",
			    "\n" +
			    "public team class Team7115cmrnm {\n" +
			    "    public class Role7115cmrnm_1 playedBy T7115cmrnm {\n" +
			    "        private String getValue() {\n" +
			    "            return \"a\";\n" +
			    "        }\n" +
			    "\n" +
			    "        public String test() {\n" +
			    "            return getValue();\n" +
			    "        }\n" +
			    "    }\n" +
			    "\n" +
			    "    public class Role7115cmrnm_2 extends Role7115cmrnm_1 {\n" +
			    "        // this should work as the original method is not visible\n" +
			    "        callin String getValue() {\n" +
			    "            base.getValue();\n" +
			    "            return \"b\";\n" +
			    "        }\n" +
			    "        getValue <- replace test;\n" +
			    "    }\n" +
			    "\n" +
			    "    public String getValue(T7115cmrnm as Role7115cmrnm_2 obj) {\n" +
			    "        // this in turn calls the original test method, not the callin\n" +
			    "        return obj.test();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "ab");
    }

    // a static role method calls a non-static team method , qualified this reference passed by parameter mapping
    // 7.1.16-otjld-static-role-method-1
    public void test7116_staticRoleMethod1() {
       
       runConformTest(
            new String[] {
		"Team7116srm1.java",
			    "\n" +
			    "public team class Team7116srm1 {\n" +
			    "    String val = \"OK\";\n" +
			    "    void print() {\n" +
			    "        System.out.print(val);\n" +
			    "    }\n" +
			    "    protected class R playedBy T7116srm1 {\n" +
			    "        static void test(Team7116srm1 tthis) {\n" +
			    "            tthis.print();\n" +
			    "        }\n" +
			    "        void test(Team7116srm1 tthis) <- after void nothing() \n" +
			    "            with {tthis <- Team7116srm1.this}\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team7116srm1 t = new Team7116srm1();\n" +
			    "        t.activate();\n" +
			    "        new T7116srm1().nothing();\n" +
			    "    }\n" +
			    "}    \n" +
			    "    \n",
		"T7116srm1.java",
			    "\n" +
			    "public class T7116srm1 {\n" +
			    "    void nothing() {}\n" +
			    "}    \n" +
			    "    \n"
            },
            "OK");
    }

    // a static role method accesses the enclosing team instance
    // 7.1.16-otjld-static-role-method-2
    public void test7116_staticRoleMethod2() {
       
       runConformTest(
            new String[] {
		"Team7116srm2.java",
			    "\n" +
			    "public team class Team7116srm2 {\n" +
			    "    protected class R {\n" +
			    "        static protected void test(int i) {\n" +
			    "            int j = i+1;\n" +
			    "            Team7116srm2.this.test();\n" +
			    "            System.out.print(j);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        System.out.print(val);\n" +
			    "    }\n" +
			    "    String val = null;\n" +
			    "    Team7116srm2 () {\n" +
			    "        val = \"OK\";\n" +
			    "        R.test(4);\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team7116srm2();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK5");
    }

    // a static role method accesses the enclosing team instance, invoked via callin
    // 7.1.16-otjld-static-role-method-3
    public void test7116_staticRoleMethod3() {
       
       runConformTest(
            new String[] {
		"Team7116srm3.java",
			    "\n" +
			    "public team class Team7116srm3 {\n" +
			    "    protected class R playedBy T7116srm3 {\n" +
			    "        static void test() {\n" +
			    "            Team7116srm3.this.test();\n" +
			    "        }\n" +
			    "        test <- after dummy;\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        System.out.print(val);\n" +
			    "    }\n" +
			    "    String val = null;\n" +
			    "    Team7116srm3 () {\n" +
			    "        val = \"OK\";\n" +
			    "        activate();\n" +
			    "    }\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team7116srm3 t = new Team7116srm3();\n" +
			    "        new T7116srm3().dummy();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7116srm3.java",
			    "\n" +
			    "public class T7116srm3 {\n" +
			    "    void dummy() {}\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a static callin method accesses the enclosing team instance
    // 7.1.16-otjld-static-role-method-4
    public void test7116_staticRoleMethod4() {
       
       runConformTest(
            new String[] {
		"Team7116srm4.java",
			    "\n" +
			    "public team class Team7116srm4 {\n" +
			    "    protected class R playedBy T7116srm4 {\n" +
			    "        static callin void test(int i) {\n" +
			    "            base.test(i);\n" +
			    "            int j = i+1;\n" +
			    "            Team7116srm4.this.test();\n" +
			    "            System.out.print(j);\n" +
			    "        }\n" +
			    "        void test(int i) <- replace void dummy()\n" +
			    "            with { i <- 4 };\n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        System.out.print(val);\n" +
			    "    }\n" +
			    "    String val = null;\n" +
			    "    Team7116srm4 () {\n" +
			    "        val = \"K\";\n" +
			    "        activate();\n" +
			    "    }\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team7116srm4 t = new Team7116srm4();\n" +
			    "        T7116srm4.dummy();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7116srm4.java",
			    "\n" +
			    "public class T7116srm4 {\n" +
			    "    static void dummy() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK5");
    }

    // a static callin method calls another static role method (regular)
    // 7.1.16-otjld-static-role-method-5
    public void test7116_staticRoleMethod5() {
       
       runConformTest(
            new String[] {
		"Team7116srm5.java",
			    "\n" +
			    "public team class Team7116srm5 {\n" +
			    "    protected class R playedBy T7116srm5 {\n" +
			    "        static void doit() {\n" +
			    "            System.out.print(\"O\");\n" +
			    "        }\n" +
			    "        @SuppressWarnings(\"basecall\")\n" +
			    "        static callin void intercept() {\n" +
			    "            doit();\n" +
			    "            System.out.print(\"K\");\n" +
			    "        }\n" +
			    "        intercept <- replace baseTest;\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        Team7116srm5 t = new Team7116srm5();\n" +
			    "        t.activate();\n" +
			    "        T7116srm5.baseTest();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7116srm5.java",
			    "\n" +
			    "public class T7116srm5 {\n" +
			    "    static void baseTest() {\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK");
    }

    // a static callin method accesses an indirect enclosing team instance
    // 7.1.16-otjld-static-role-method-6
    public void test7116_staticRoleMethod6() {
       
       runConformTest(
            new String[] {
		"Team7116srm6.java",
			    "\n" +
			    "public team class Team7116srm6 {\n" +
			    "    protected team class Mid { \n" +
			    "        protected class R playedBy T7116srm6 {\n" +
			    "            static callin void test(int i) {\n" +
			    "                base.test(i);\n" +
			    "                int j = i+1;\n" +
			    "                Team7116srm6.this.test();\n" +
			    "                System.out.print(j);\n" +
			    "            }\n" +
			    "            test <- replace test;\n" +
			    "        }\n" +
			    "    }\n" +
			    "    Team7116srm6() { \n" +
			    "        new Mid().activate(); \n" +
			    "    }\n" +
			    "    void test() {\n" +
			    "        System.out.print(\"T\");\n" +
			    "    }\n" +
			    "    public static void main(String[] args) {\n" +
			    "        new Team7116srm6().activate();\n" +
			    "        T7116srm6.test(3);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7116srm6.java",
			    "\n" +
			    "public class T7116srm6 {\n" +
			    "    public static void test(int i) {\n" +
			    "        System.out.print(\"B\"+i);\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "B3T4");
    }

    // a static callin method of a nested role accesses the enclosing team instance
    // 7.1.16-otjld-static-role-method-7
    public void test7116_staticRoleMethod7() {
       
       runConformTest(
            new String[] {
		"Team7116srm7.java",
			    "\n" +
			    "public team class Team7116srm7 {\n" +
			    "    protected team class Mid {\n" +
			    "        protected class R playedBy T7116srm7 {\n" +
			    "            static callin void test(int i) {\n" +
			    "                base.test(i);\n" +
			    "                int j = i+1;\n" +
			    "                Mid.this.test();\n" +
			    "                System.out.print(j);\n" +
			    "            }\n" +
			    "            void test(int i) <- replace void dummy()\n" +
			    "                with { i <- 4 };\n" +
			    "        }\n" +
			    "        void test() {\n" +
			    "            System.out.print(Team7116srm7.this.val);\n" +
			    "        }\n" +
			    "    }\n" +
			    "    String val = null;\n" +
			    "    Team7116srm7 () {\n" +
			    "        val = \"K\";\n" +
			    "        Mid m = new Mid();\n" +
			    "        m.activate();\n" +
			    "    }\n" +
			    "    @SuppressWarnings(\"unused\")\n" +
			    "    public static void main(String[] args) {\n" +
			    "        final Team7116srm7 t = new Team7116srm7();\n" +
			    "        T7116srm7.dummy();\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n",
		"T7116srm7.java",
			    "\n" +
			    "public class T7116srm7 {\n" +
			    "    static void dummy() {\n" +
			    "        System.out.print(\"O\");\n" +
			    "    }\n" +
			    "}\n" +
			    "    \n"
            },
            "OK5");
    }
}
