/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2012 Stephan Herrmann
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.other;

import junit.framework.Test;

import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class Java8 extends AbstractOTJLDTest {

	public Java8(String name) {
		super(name);
	}

// Static initializer to specify tests subset using TESTS_* static variables
// All tests which do not belong to the subset are skipped...
	static {
//		TESTS_NAMES = new String[] { "testA11_lambdaExpression02"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	@SuppressWarnings("unchecked")
	public static Test suite() {
		Test suite = buildMinimalComplianceTestSuite(Java8.class, F_1_8);
		TESTS_COUNTERS.put(Java8.class.getName(), new Integer(suite.countTestCases()));
		return suite;
	}

	public static Class testClass() {
		return Java8.class;
	}

	// A lambda expression appears in a parameter mapping
	// - empty param list
	public void testA11_lambdaExpression01() {
		runConformTest(
			new String[] {
		"p1/TeamA11le01.java",
			"package p1;\n" +
			"public team class TeamA11le01 {\n" +
			"    protected class Role playedBy TA11le01 {\n" +
			"        protected String v = \"OK\";\n" +
			"        void outside() -> void run(Runnable r) with {\n" +
			"            () -> System.out.println(v) -> r\n" +
			"        }\n" +
			"    }\n" +
			"    void test(TA11le01 as Role r) {\n" +
			"        r.outside();\n" +
			"    } \n" +
			"    public static void main(String[] args) throws Exception {\n" +
			"        new TeamA11le01().test(new TA11le01());\n" +
			"    }\n" +
			"}",
	"p1/TA11le01.java",
			"package p1;\n" +
			"public class TA11le01 {\n" +
			"    public void run(Runnable r) {\n" +
			"      try {\n" +
			"        r.run();\n" +
			"      } catch (NullPointerException npe) {System.out.print(\"caught\"); }\n" +
			"    }\n" +
			"}",
			},
			"OK"); // FIXME real execution of lambda
	}

	// A lambda expression appears in a parameter mapping
	// - single ident param
	// - lambda is 2nd mapping
	public void testA11_lambdaExpression02() {
		runConformTest(
			new String[] {
		"p1/TeamA11le02.java",
			"package p1;\n" +
			"public team class TeamA11le02 {\n" +
			"    protected class Role playedBy TA11le02 {\n" +
			"        protected String v;\n" +
			"        void outside() -> void run(String s, RunWithArg<String> r) with {\n" +
			"            \"prefix.\" -> s,\n" +
			"            x -> System.out.println(Role.this.v) -> r\n" +
			"        }\n" +
			"    }\n" +
			"    void test(TA11le02 as Role r) {\n" +
			"        r.v = \"OK\";" +
			"        r.outside();\n" +
			"    } \n" +
			"    public static void main(String[] args) throws Exception {\n" +
			"        new TeamA11le02().test(new TA11le02());\n" +
			"    }\n" +
			"}",
	"p1/RunWithArg.java",
			"package p1;\n" +
			"public interface RunWithArg<T> {\n" +
			"	void run(T a);\n" +
			"}\n",
	"p1/TA11le02.java",
			"package p1;\n" +
			"public class TA11le02 {\n" +
			"    public void run(String s, RunWithArg<String> r) {\n" +
			"        System.out.print(s);\n" +
			"      try {\n" +
			"        r.run(s);\n" +
			"      } catch (NullPointerException npe) {System.out.print(\"caught\"); }\n" +
			"    }\n" +
			"}",
			},
			"prefix.OK");
	}

	// A lambda expression appears in a parameter mapping
	// - two type elided params
	public void testA11_lambdaExpression03() {
		runConformTest(
			new String[] {
		"p1/TeamA11le03.java",
			"package p1;\n" +
			"public team class TeamA11le03 {\n" +
			"    protected class Role playedBy TA11le03 {\n" +
			"        void outside() -> void run(IA11le03 r) with {\n" +
			"            (x,y) -> System.out.println(\"\"+x+y) -> r\n" +
			"        }\n" +
			"    }\n" +
			"    void test(TA11le03 as Role r) {\n" +
			"        r.outside();\n" +
			"    } \n" +
			"    public static void main(String[] args) throws Exception {\n" +
			"        new TeamA11le03().test(new TA11le03());\n" +
			"    }\n" +
			"}",
		"p1/IA11le03.java",
			"package p1;\n" +
			"public interface IA11le03 {\n" +
			"    void invoke(String s1, String s2);\n" +
			"}",
		"p1/TA11le03.java",
			"package p1;\n" +
			"public class TA11le03 {\n" +
			"    public void run(IA11le03 r) {\n" +
			"      try {\n" +
			"        r.invoke(\"O\", \"K\");\n" +
			"      } catch (NullPointerException npe) {System.out.print(\"caught\"); }\n" +
			"    }\n" +
			"}",
			},
			"OK");
	}

	// A lambda expression appears in a parameter mapping
	// - two typed params
	public void testA11_lambdaExpression04() {
		runConformTest(
			new String[] {
		"p1/TeamA11le04.java",
			"package p1;\n" +
			"public team class TeamA11le04 {\n" +
			"    protected class Role playedBy TA11le04 {\n" +
			"        void outside() -> void run(IA11le04 r) with {\n" +
			"            (String x, String y) -> System.out.println(\"\"+x+y) -> r\n" +
			"        }\n" +
			"    }\n" +
			"    void test(TA11le04 as Role r) {\n" +
			"        r.outside();\n" +
			"    } \n" +
			"    public static void main(String[] args) throws Exception {\n" +
			"        new TeamA11le04().test(new TA11le04());\n" +
			"    }\n" +
			"}",
		"p1/IA11le04.java",
			"package p1;\n" +
			"public interface IA11le04 {\n" +
			"    void invoke(String s1, String s2);\n" +
			"}",
		"p1/TA11le04.java",
			"package p1;\n" +
			"public class TA11le04 {\n" +
			"    public void run(IA11le04 r) {\n" +
			"      try {\n" +
			"        r.invoke(\"O\", \"K\");\n" +
			"      } catch (NullPointerException npe) {System.out.print(\"caught\"); }\n" +
			"    }\n" +
			"}",
			},
			"OK");
	}
	
	public void testTypeAnnotationAndTypeAnchor_1() {
		runConformTest(
			new String[] {
		"Marker.java",
			"import java.lang.annotation.*;\n" +
			"@Retention(RetentionPolicy.CLASS)\n" + 
			"@Target({ ElementType.TYPE_USE })\n" + 
			"public @interface Marker {}\n",
		"T1.java,",
			"public team class T1 {\n" +
			"	public class R {}\n" +
			"}\n",
		"C1.java",
			"public abstract class C1 {\n" +
			"	abstract void test1(final T1 o, R<@o> c);\n" +
			"	abstract void test2(final Object o, C2<@Marker Object> c);\n" +
			"}\n" +
			"class C2<T> {}\n"
			});
	}
	
	public void testTypeAnnotationAndTypeAnchor_2() {
		runConformTest(
			new String[] {
		"p1/Marker.java",
				"package p1;\n" +
				"import java.lang.annotation.*;\n" +
				"@Retention(RetentionPolicy.CLASS)\n" + 
				"@Target({ ElementType.TYPE_USE })\n" + 
				"public @interface Marker {}\n",
		"p1/Marker2.java",
				"package p1;\n" +
				"import java.lang.annotation.*;\n" +
				"@Retention(RetentionPolicy.CLASS)\n" + 
				"@Target({ ElementType.TYPE_USE })\n" + 
				"public @interface Marker2 {}\n",
		"T1.java,",
			"public team class T1 {\n" +
			"	public class R {}\n" +
			"}\n",
		"C1.java",
			"public abstract class C1 {\n" +
			"	final T1 t = new T1();\n" +
			"	abstract void test1(final C1 a, R<@a.t> c);\n" +
			"	abstract void test2(final Object o, C2<java.lang.@p1.Marker Object> c);\n" + // bug, should be java.lang. @p1.Marker Object
			"	abstract void test3(final Object o, C2<@p1.Marker @p1.Marker2 Object> c);\n" + // bug, should be java.lang. @p1.Marker Object
			"}\n" +
			"class C2<T> {}\n"
			});
	}
}
