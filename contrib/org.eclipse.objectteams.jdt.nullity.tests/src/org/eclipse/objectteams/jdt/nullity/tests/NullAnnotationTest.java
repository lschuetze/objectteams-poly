/*******************************************************************************
 * Copyright (c) 2010, 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.objectteams.jdt.nullity.tests;


import java.net.URL;
import java.util.Map;

import junit.framework.Test;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.tests.compiler.regression.AbstractComparableTest;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.objectteams.internal.jdt.nullity.NullCompilerOptions;

@SuppressWarnings({ "unchecked", "rawtypes", "restriction" })
public class NullAnnotationTest extends AbstractComparableTest {

// class libraries including our default null annotation types:
String[] LIBS;

// names and content of custom annotations used in a few tests:
private static final String CUSTOM_NONNULL_NAME = "org/foo/NonNull.java";
private static final String CUSTOM_NONNULL_CONTENT = 
	"package org.foo;\n" +
	"import static java.lang.annotation.ElementType.*;\n" + 
	"import java.lang.annotation.*;\n" + 
	"@Retention(RetentionPolicy.CLASS)\n" + 
	"@Target({METHOD,PARAMETER,LOCAL_VARIABLE})\n" + 
	"public @interface NonNull {\n" + 
	"}\n";
private static final String CUSTOM_NULLABLE_NAME = "org/foo/Nullable.java";
private static final String CUSTOM_NULLABLE_CONTENT = "package org.foo;\n" +
	"import static java.lang.annotation.ElementType.*;\n" + 
	"import java.lang.annotation.*;\n" +
	"@Retention(RetentionPolicy.CLASS)\n" + 
	"@Target({METHOD,PARAMETER,LOCAL_VARIABLE})\n" + 
	"public @interface Nullable {\n" + 
	"}\n";

public NullAnnotationTest(String name) {
	super(name);
}

// Static initializer to specify tests subset using TESTS_* static variables
// All specified tests which do not belong to the class are skipped...
static {
//		TESTS_NAMES = new String[] { "test_constructor_with_nested_class" };
//		TESTS_NUMBERS = new int[] { 561 };
//		TESTS_RANGE = new int[] { 1, 2049 };
}

public static Test suite() {
	return buildComparableTestSuite(testClass());
}

public static Class testClass() {
	return NullAnnotationTest.class;
}

@Override
protected void setUp() throws Exception {
	super.setUp();
	if (LIBS == null) {
		String[] defaultLibs = getDefaultClassPaths();
		int len = defaultLibs.length;
		LIBS = new String[len+1];
		System.arraycopy(defaultLibs, 0, LIBS, 0, len);
		URL libEntry = Platform.getBundle("org.eclipse.objectteams.jdt.nullity.tests").getEntry("/lib/nullAnnotations.jar");
		LIBS[len] = FileLocator.toFileURL(libEntry).getPath();
	}
}
// Conditionally augment problem detection settings
static boolean setNullRelatedOptions = true;
protected Map getCompilerOptions() {
    Map defaultOptions = super.getCompilerOptions();
    if (setNullRelatedOptions) {
    	defaultOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	    defaultOptions.put(CompilerOptions.OPTION_ReportPotentialNullReference, CompilerOptions.ERROR);
	    defaultOptions.put(CompilerOptions.OPTION_ReportRedundantNullCheck, CompilerOptions.ERROR);
		defaultOptions.put(CompilerOptions.OPTION_ReportRawTypeReference, CompilerOptions.IGNORE);
		defaultOptions.put(CompilerOptions.OPTION_IncludeNullInfoFromAsserts, CompilerOptions.ENABLED);
		
		defaultOptions.put(CompilerOptions.OPTION_ReportMissingOverrideAnnotationForInterfaceMethodImplementation, CompilerOptions.DISABLED);

		// enable null annotations:
		defaultOptions.put(NullCompilerOptions.OPTION_AnnotationBasedNullAnalysis, CompilerOptions.ENABLED);
		// leave other new options at these defaults:
//		defaultOptions.put(CompilerOptions.OPTION_ReportNullContractViolation, CompilerOptions.ERROR);
//		defaultOptions.put(CompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
//		defaultOptions.put(CompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.WARNING);
		
//		defaultOptions.put(CompilerOptions.OPTION_NullableAnnotationName, "org.eclipse.jdt.annotation.Nullable");
//		defaultOptions.put(CompilerOptions.OPTION_NonNullAnnotationName, "org.eclipse.jdt.annotation.NonNull");
    }
    return defaultOptions;
}
void runNegativeTestWithLibs(String[] testFiles, String expectedErrorLog) {
	runNegativeTest(
			testFiles,
			expectedErrorLog,
			LIBS,
			false /*shouldFlush*/);
}
void runNegativeTestWithLibs(boolean shouldFlushOutputDirectory, String[] testFiles, Map customOptions, String expectedErrorLog) {
	runNegativeTest(
			shouldFlushOutputDirectory,
			testFiles,
			LIBS,
			customOptions,
			expectedErrorLog,
			"",/* expected output */
			"",/* expected error */
		    JavacTestOptions.Excuse.EclipseWarningConfiguredAsError);
}
void runNegativeTestWithLibs(String[] testFiles, Map customOptions, String expectedErrorLog) {
	runNegativeTestWithLibs(false /* flush output directory */,	testFiles, customOptions, expectedErrorLog);
}
void runConformTestWithLibs(String[] testFiles, Map customOptions, String expectedCompilerLog) {
	runConformTestWithLibs(false /* flush output directory */, testFiles, customOptions, expectedCompilerLog);
}
void runConformTestWithLibs(boolean shouldFlushOutputDirectory, String[] testFiles, Map customOptions, String expectedCompilerLog) {
	runConformTest(
			shouldFlushOutputDirectory,
			testFiles,
			LIBS,
			customOptions,
			expectedCompilerLog,
			"",/* expected output */
			"",/* expected error */
		    JavacTestOptions.Excuse.EclipseWarningConfiguredAsError);
}
void runConformTest(String[] testFiles, Map customOptions, String expectedOutputString) {
	runConformTest(
			testFiles, 
			expectedOutputString, 
			null /*classLibraries*/, 
			true /*shouldFlushOutputDirectory*/,
			null /*vmArguments*/, 
			customOptions, 
			null /*customRequestor*/);
	
}
// a nullable argument is dereferenced without a check
public void test_nullable_paramter_001() {
	runNegativeTest(
		new String[] {
			"X.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class X {\n" +
			  "    void foo(@Nullable Object o) {\n" +
			  "        System.out.print(o.toString());\n" +
			  "    }\n" +
			  "}\n"},
	    "----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	System.out.print(o.toString());\n" + 
		"	                 ^\n" + 
		"Potential null pointer access: The variable o may be null at this location\n" + 
		"----------\n",
		LIBS,
		true /* shouldFlush*/);
}

// a null value is passed to a nullable argument
public void test_nullable_paramter_002() {
	runConformTest(
		new String[] {
			"X.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class X {\n" +
			  "    void foo(@Nullable Object o) {\n" +
			  "        // nop\n" +
			  "    }\n" +
			  "    void bar() {\n" +
			  "        foo(null);\n" +
			  "    }\n" +
			  "}\n"},
	    "",
	    LIBS,
	    false/*shouldFlush*/,
	    null/*vmArgs*/);
}

// a non-null argument is checked for null
public void test_nonnull_parameter_001() {
	runNegativeTest(
		new String[] {
			"X.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class X {\n" +
			  "    void foo(@NonNull Object o) {\n" +
			  "        if (o != null)\n" +
			  "              System.out.print(o.toString());\n" +
			  "    }\n" +
			  "}\n"},
	    "----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	if (o != null)\n" + 
		"	    ^\n" + 
		"Redundant null check: The variable o cannot be null at this location\n" + 
		"----------\n",
		LIBS,
		true /* shouldFlush*/);
}
// a non-null argument is dereferenced without a check
public void test_nonnull_parameter_002() {
	runConformTest(
		new String[] {
			"X.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class X {\n" +
			  "    void foo(@NonNull Object o) {\n" +
			  "        System.out.print(o.toString());\n" +
			  "    }\n" +
			  "    public static void main(String... args) {\n" +
			  "        new X().foo(\"OK\");\n" +
			  "    }\n" +
			  "}\n"},
	    "OK",
	    LIBS,
	    false/*shouldFlush*/,
	    null/*vmArgs*/);
}
// passing null to nonnull parameter - many fields in enclosing class
public void test_nonnull_parameter_003() {
	runNegativeTest(
		new String[] {
			"X.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class X {\n" +
			  "    int i00, i01, i02, i03, i04, i05, i06, i07, i08, i09;" +
			  "    int i10, i11, i12, i13, i14, i15, i16, i17, i18, i19;" +
			  "    int i20, i21, i22, i23, i24, i25, i26, i27, i28, i29;" +
			  "    int i30, i31, i32, i33, i34, i35, i36, i37, i38, i39;" +
			  "    int i40, i41, i42, i43, i44, i45, i46, i47, i48, i49;" +
			  "    int i50, i51, i52, i53, i54, i55, i56, i57, i58, i59;" +
			  "    int i60, i61, i62, i63, i64, i65, i66, i67, i68, i69;" +
			  "    void foo(@NonNull Object o) {\n" +
			  "        System.out.print(o.toString());\n" +
			  "    }\n" +
			  "    void bar() {\n" +
			  "        foo(null);\n" +
			  "    }\n" +
			  "}\n"},
		"----------\n" + 
		"1. ERROR in X.java (at line 7)\n" + 
		"	foo(null);\n" + 
		"	    ^^^^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value is null\n" + 
		"----------\n",
		LIBS,
		true /* shouldFlush*/);
}
// passing potential null to nonnull parameter - target method is consumed from .class
public void test_nonnull_parameter_004() {
	runConformTestWithLibs(
			new String[] {
				"Lib.java",
					"import org.eclipse.jdt.annotation.*;\n" +
				"public class Lib {\n" +
				"    void setObject(@NonNull Object o) { }\n" +
				"}\n"
			},
			null /*customOptions*/,
			"");
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			  "public class X {\n" +
			  "    void bar(Lib l, boolean b) {\n" +
			  "        Object o = null;\n" +
			  "        if (b) o = new Object();\n" +
			  "        l.setObject(o);\n" +
			  "    }\n" +
			  "}\n"},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 5)\n" + 
		"	l.setObject(o);\n" + 
		"	            ^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value can be null\n" + 
		"----------\n");
}
// passing unknown value to nonnull parameter  - target method is consumed from .class
public void test_nonnull_parameter_005() {
	runConformTestWithLibs(
			new String[] {
				"Lib.java",
				"import org.eclipse.jdt.annotation.*;\n" +
				"public class Lib {\n" +
				"    void setObject(@NonNull Object o) { }\n" +
				"}\n"
			},
			null /*customOptions*/,
			"");
	runConformTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			  "public class X {\n" +
			  "    void bar(Lib l, Object o) {\n" +
			  "        l.setObject(o);\n" +
			  "    }\n" +
			  "}\n"},
		null /* options */,
		"----------\n" + 
		"1. WARNING in X.java (at line 3)\n" + 
		"	l.setObject(o);\n" + 
		"	            ^\n" + 
		"Potential type mismatch: required \'@NonNull Object\' but nullness of the provided value is unknown\n" + 
		"----------\n");
}
// a ternary non-null expression is passed to a nonnull parameter
public void test_nonnull_parameter_006() {
	Map customOptions = getCompilerOptions();
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runConformTestWithLibs(
		new String[] {
			"X.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class X {\n" +
			  "    	void m1(@NonNull String a) {}\n" + 
			  "		void m2(@Nullable String b) {\n" + 
			  "			m1(b == null ? \"\" : b);\n" + 
			  "		}\n" +
			  "}\n"},
		customOptions,
		""  /* compiler output */);
}
// nullable value passed to a non-null parameter in a super-call
public void test_nonnull_parameter_007() {
	Map customOptions = getCompilerOptions();
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
			"XSub.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class XSub extends XSuper {\n" +
			  "    	XSub(@Nullable String b) {\n" + 
			  "			super(b);\n" + 
			  "		}\n" +
			  "}\n",
			"XSuper.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class XSuper {\n" +
			  "    	XSuper(@NonNull String b) {\n" + 
			  "		}\n" +
			  "}\n"
		},		
		customOptions,
		"----------\n" + 
		"1. ERROR in XSub.java (at line 4)\n" + 
		"	super(b);\n" + 
		"	      ^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n");
}
// a nullable value is passed to a non-null parameter in an allocation expression
public void test_nonnull_parameter_008() {
	Map customOptions = getCompilerOptions();
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class X {\n" +
			  "    	X(@NonNull String a) {}\n" + 
			  "		static X create(@Nullable String b) {\n" + 
			  "			return new X(b);\n" + 
			  "		}\n" +
			  "}\n"},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 5)\n" + 
		"	return new X(b);\n" + 
		"	             ^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n"  /* compiler output */);
}
// a nullable value is passed to a non-null parameter in a qualified allocation expression
public void test_nonnull_parameter_009() {
	Map customOptions = getCompilerOptions();
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class X {\n" +
			  "    class Local {\n" +
			  "    	   Local(@NonNull String a) {}\n" +
			  "    }\n" + 
			  "	   Local create(@Nullable String b) {\n" + 
			  "	       return this.new Local(b);\n" + 
			  "    }\n" +
			  "}\n"},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 7)\n" + 
		"	return this.new Local(b);\n" + 
		"	                      ^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n"  /* compiler output */);
}
// assigning potential null to a nonnull local variable
public void test_nonnull_local_001() {
	runNegativeTest(
		new String[] {
			"X.java",
			  "import org.eclipse.jdt.annotation.*;\n" +
			  "public class X {\n" +
			  "    void foo(boolean b, Object p) {\n" +
			  "        @NonNull Object o1 = b ? null : new Object();\n" +
			  "        @NonNull String o2 = \"\";\n" +
			  "        o2 = null;\n" +
			  "        @NonNull Object o3 = p;\n" +
			  "    }\n" +
			  "}\n"},
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	@NonNull Object o1 = b ? null : new Object();\n" + 
		"	                     ^^^^^^^^^^^^^^^^^^^^^^^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value can be null\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 6)\n" + 
		"	o2 = null;\n" + 
		"	     ^^^^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value is null\n" + 
		"----------\n" + 
		"3. WARNING in X.java (at line 7)\n" + 
		"	@NonNull Object o3 = p;\n" + 
		"	                     ^\n" + 
		"Potential type mismatch: required \'@NonNull Object\' but nullness of the provided value is unknown\n" + 
		"----------\n",
		LIBS,
		true /* shouldFlush*/);
}

// a method tries to tighten the type specification, super declares parameter o as @Nullable
// other parameters: s is redefined from not constrained to @Nullable which is OK
//                   third is redefined from not constrained to @NonNull which is bad, too
public void test_parameter_specification_inheritance_001() {
	runConformTestWithLibs(
		new String[] {
			"Lib.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Lib {\n" +
			"    void foo(String s, @Nullable Object o, Object third) { }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"");
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X extends Lib {\n" +
			"    @Override\n" +
			"    void foo(@Nullable String s, @NonNull Object o, @NonNull Object third) { System.out.print(o.toString()); }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	void foo(@Nullable String s, @NonNull Object o, @NonNull Object third) { System.out.print(o.toString()); }\n" + 
		"	                             ^^^^^^^^^^^^^^^\n" + 
		"Illegal redefinition of parameter o, inherited method from Lib declares this parameter as @Nullable\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 4)\n" + 
		"	void foo(@Nullable String s, @NonNull Object o, @NonNull Object third) { System.out.print(o.toString()); }\n" + 
		"	                                                ^^^^^^^^^^^^^^^\n" + 
		"Illegal redefinition of parameter third, inherited method from Lib does not constrain this parameter\n" + 
		"----------\n");
}
// a method body fails to redeclare the inherited null annotation, super declares parameter as @Nullable
public void test_parameter_specification_inheritance_002() {
	runConformTest(
		new String[] {
			"Lib.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Lib {\n" +
			"    void foo(@Nullable Object o) { }\n" +
			"}\n"
		},
		"",
	    LIBS,
	    false/*shouldFlush*/,
	    null/*vmArgs*/);
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			"public class X extends Lib {\n" +
			"    @Override\n" +
			"    void foo(Object o) {\n" +
			"        System.out.print(o.toString());\n" +
			"    }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 3)\n" + 
		"	void foo(Object o) {\n" + 
		"	         ^^^^^^\n" + 
		"Missing null annotation: inherited method from Lib declares this parameter as @Nullable\n" + 
		"----------\n");
}
// a method relaxes the parameter null specification, super interface declares parameter o as @NonNull
// other (first) parameter just repeats the inherited @NonNull
public void test_parameter_specification_inheritance_003() {
	runConformTest(
		new String[] {
			"IX.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public interface IX {\n" +
			"    void foo(@NonNull String s, @NonNull Object o);\n" +
			"}\n",
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X implements IX {\n" +
			"    public void foo(@NonNull String s, @Nullable Object o) { ; }\n" +
			"    void bar() { foo(\"OK\", null); }\n" +
			"}\n"
		},
		"",
	    LIBS,
	    false/*shouldFlush*/,
	    null/*vmArgs*/);
}
// a method adds a @NonNull annotation, super interface has no null annotation
// changing other from unconstrained to @Nullable is OK
public void test_parameter_specification_inheritance_004() {
	runConformTest(
		new String[] {
			"IX.java",
			"public interface IX {\n" +
			"    void foo(Object o, Object other);\n" +
			"}\n"
		});
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X implements IX {\n" +
			"    public void foo(@NonNull Object o, @Nullable Object other) { System.out.print(o.toString()); }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 3)\n" + 
		"	public void foo(@NonNull Object o, @Nullable Object other) { System.out.print(o.toString()); }\n" + 
		"	                ^^^^^^^^^^^^^^^\n" + 
		"Illegal redefinition of parameter o, inherited method from IX does not constrain this parameter\n" + 
		"----------\n");
}
// a method tries to relax the null contract, super declares @NonNull return
public void test_parameter_specification_inheritance_005() {
	runConformTestWithLibs(
		new String[] {
			"Lib.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Lib {\n" +
			"    @NonNull Object getObject() { return new Object(); }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"");
	runNegativeTestWithLibs(
		false, //dont' flush
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X extends Lib {\n" +
			"    @Override\n" +
			"    @Nullable Object getObject() { return null; }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	@Nullable Object getObject() { return null; }\n" + 
		"	^^^^^^^^^^^^^^^^\n" + 
		"The return type is incompatible with the @NonNull return from Lib.getObject()\n" + 
		"----------\n");
}

// super has no constraint for return, sub method confirms the null contract as @Nullable 
public void test_parameter_specification_inheritance_006() {
	runConformTest(
		new String[] {
			"Lib.java",
			"public class Lib {\n" +
			"    Object getObject() { return null; }\n" +
			"}\n"
		});
	runConformTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X extends Lib {\n" +
			"    @Override\n" +
			"    @Nullable Object getObject() { return null; }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"");
}
// a method body violates the inherited null specification, super declares @NonNull return, missing redeclaration
public void test_parameter_specification_inheritance_007() {
	runConformTestWithLibs(
		new String[] {
			"Lib.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Lib {\n" +
			"    @NonNull Object getObject() { return new Object(); }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"");
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			"public class X extends Lib {\n" +
			"    @Override\n" +
			"    Object getObject() { return null; }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 3)\n" + 
		"	Object getObject() { return null; }\n" + 
		"	^^^^^^\n" + 
		"The return type is incompatible with the @NonNull return from Lib.getObject()\n" + 
		"----------\n");
}
//a method body violates the @NonNull return specification (repeated from super)
public void test_parameter_specification_inheritance_007a() {
	runConformTestWithLibs(
		new String[] {
			"Lib.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Lib {\n" +
			"    @NonNull Object getObject() { return new Object(); }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"");
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X extends Lib {\n" +
			"    @Override\n" +
			"    @NonNull Object getObject() { return null; }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	@NonNull Object getObject() { return null; }\n" + 
		"	                                     ^^^^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value is null\n" + 
		"----------\n");
}
// a client potentially violates the inherited null specification, super interface declares @NonNull parameter
public void test_parameter_specification_inheritance_008() {
	Map options = getCompilerOptions();
	options.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runConformTestWithLibs(
		new String[] {
			"IX.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public interface IX {\n" +
			"    void printObject(@NonNull Object o);\n" +
			"}\n"
		},
		null /*customOptions*/,
		"");
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			"public class X implements IX {\n" +
			"    public void printObject(Object o) { System.out.print(o.toString()); }\n" +
			"}\n",
			"M.java",
			"public class M{\n" +
			"    void foo(IX x, Object o) {\n" +
			"        x.printObject(o);\n" +
			"    }\n" +
			"}\n"
		},
		options,
		"----------\n" + 
		// additional error:
		"1. ERROR in X.java (at line 2)\n" + 
		"	public void printObject(Object o) { System.out.print(o.toString()); }\n" + 
		"	                        ^^^^^^\n" + 
		"Missing null annotation: inherited method from IX declares this parameter as @NonNull\n" + 
		"----------\n" +
		// main error:
		"----------\n" + 
		"1. ERROR in M.java (at line 3)\n" + 
		"	x.printObject(o);\n" + 
		"	              ^\n" + 
		"Potential type mismatch: required \'@NonNull Object\' but nullness of the provided value is unknown\n" + 
		"----------\n");
}
// a static method has a more relaxed null contract than a like method in the super class, but no overriding.
public void test_parameter_specification_inheritance_009() {
	runConformTestWithLibs(
		new String[] {
			"Lib.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Lib {\n" +
			"    @NonNull static Object getObject() { return new Object(); }\n" +
			"}\n",
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X extends Lib {\n" +
			"    @Nullable static Object getObject() { return null; }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"");
}
// class default is nonnull, method and its super both use the default
public void test_parameter_specification_inheritance_010() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runConformTestWithLibs(
		new String[] {
	"p1/X.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class X {\n" +
			"    protected String getString(String s) {\n" +
			"        if (Character.isLowerCase(s.charAt(0)))\n" +
			"	        return getString(s);\n" +
			"	     return s;\n" +
			"    }\n" +
			"}\n",
	"p1/Y.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class Y extends X {\n" +
			"    @Override\n" +
			"    protected String getString(String s) {\n" +
			"	     return super.getString(s);\n" +
			"    }\n" +
			"}\n",
		},
		customOptions,
		"");
}
// class default is nonnull, method and its super both use the default, super-call passes null
public void test_parameter_specification_inheritance_011() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
	"p1/X.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class X {\n" +
			"    protected String getString(String s) {\n" +
			"        if (Character.isLowerCase(s.charAt(0)))\n" +
			"	        return getString(s);\n" +
			"	     return s;\n" +
			"    }\n" +
			"}\n",
	"p1/Y.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class Y extends X {\n" +
			"    @Override\n" +
			"    protected String getString(String s) {\n" +
			"	     return super.getString(null);\n" +
			"    }\n" +
			"}\n",
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in p1\\Y.java (at line 7)\n" + 
		"	return super.getString(null);\n" + 
		"	                       ^^^^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value is null\n" + 
		"----------\n");
}
// methods from two super types have different null contracts.
// sub-class merges both using the weakest common contract 
public void test_parameter_specification_inheritance_012() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runConformTestWithLibs(
		new String[] {
	"p1/X.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    public @Nullable String getString(String s1, @Nullable String s2, @NonNull String s3) {\n" +
			"	     return s1;\n" +
			"    }\n" +
			"}\n",
	"p1/IY.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public interface IY {\n" +
			"    @NonNull String getString(@NonNull String s1, @NonNull String s2, @Nullable String s3);\n" +
			"}\n",
	"p1/Y.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Y extends X implements IY {\n" +
			"    @Override\n" +
			"    public @NonNull String getString(@Nullable String s1, @Nullable String s2, @Nullable String s3) {\n" +
			"	     return \"\";\n" +
			"    }\n" +
			"}\n",
		},
		customOptions,
		"");
}
// methods from two super types have different null contracts.
// sub-class overrides this method in non-conforming ways 
public void test_parameter_specification_inheritance_013() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
	"p1/X.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    public @Nullable String getString(String s1, @Nullable String s2, @NonNull String s3) {\n" +
			"	     return s1;\n" +
			"    }\n" +
			"}\n",
	"p1/IY.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public interface IY {\n" +
			"    @NonNull String getString(@NonNull String s1, @NonNull String s2, @Nullable String s3);\n" +
			"}\n",
	"p1/Y.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Y extends X implements IY {\n" +
			"    @Override\n" +
			"    public @Nullable String getString(String s1, @NonNull String s2, @NonNull String s3) {\n" +
			"	     return \"\";\n" +
			"    }\n" +
			"}\n",
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in p1\\Y.java (at line 5)\n" + 
		"	public @Nullable String getString(String s1, @NonNull String s2, @NonNull String s3) {\n" + 
		"	       ^^^^^^^^^^^^^^^^\n" + 
		"The return type is incompatible with the @NonNull return from IY.getString(String, String, String)\n" + 
		"----------\n" + 
		"2. ERROR in p1\\Y.java (at line 5)\n" + 
		"	public @Nullable String getString(String s1, @NonNull String s2, @NonNull String s3) {\n" + 
		"	                                  ^^^^^^\n" + 
		"Missing null annotation: inherited method from IY declares this parameter as @NonNull\n" + 
		"----------\n" + 
		"3. ERROR in p1\\Y.java (at line 5)\n" + 
		"	public @Nullable String getString(String s1, @NonNull String s2, @NonNull String s3) {\n" + 
		"	                                             ^^^^^^^^^^^^^^^\n" + 
		"Illegal redefinition of parameter s2, inherited method from X declares this parameter as @Nullable\n" + 
		"----------\n" + 
		"4. ERROR in p1\\Y.java (at line 5)\n" + 
		"	public @Nullable String getString(String s1, @NonNull String s2, @NonNull String s3) {\n" + 
		"	                                                                 ^^^^^^^^^^^^^^^\n" + 
		"Illegal redefinition of parameter s3, inherited method from IY declares this parameter as @Nullable\n" + 
		"----------\n");
}
// methods from two super types have different null contracts.
// sub-class does not override, but should to bridge the incompatibility
public void test_parameter_specification_inheritance_014() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
	"p1/IY.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public interface IY {\n" +
			"    public @NonNull String getString1(String s);\n" +
			"    public @NonNull String getString2(String s);\n" +
			"    public String getString3(@Nullable String s);\n" +
			"    public @NonNull String getString4(@Nullable String s);\n" +
			"    public @NonNull String getString5(@Nullable String s);\n" +
			"    public @Nullable String getString6(@NonNull String s);\n" +
			"}\n",
	"p1/X.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    public @Nullable String getString1(String s) {\n" + // incomp. return
			"	     return s;\n" +
			"    }\n" +
			"    public String getString2(String s) {\n" +			 // incomp. return
			"	     return s;\n" +
			"    }\n" +
			"    public String getString3(String s) {\n" +			 // incomp. arg
			"	     return \"\";\n" +
			"    }\n" +
			"    public @NonNull String getString4(@Nullable String s) {\n" +
			"	     return \"\";\n" +
			"    }\n" +
			"    public @NonNull String getString5(@NonNull String s) {\n" + // incomp. arg
			"	     return s;\n" +
			"    }\n" +
			"    public @NonNull String getString6(@Nullable String s) {\n" +
			"	     return \"\";\n" +
			"    }\n" +
			"}\n",
	"p1/Y.java",
			"package p1;\n" +
			"public class Y extends X implements IY {\n" +
			"}\n",
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in p1\\Y.java (at line 2)\n" + 
		"	public class Y extends X implements IY {\n" + 
		"	             ^\n" + 
		"The method getString1(String) from class X cannot implement the corresponding method from type IY due to incompatible nullness constraints. \n" + 
		"----------\n" + 
		"2. ERROR in p1\\Y.java (at line 2)\n" + 
		"	public class Y extends X implements IY {\n" + 
		"	             ^\n" + 
		"The method getString2(String) from class X cannot implement the corresponding method from type IY due to incompatible nullness constraints. \n" + 
		"----------\n" + 
		"3. ERROR in p1\\Y.java (at line 2)\n" + 
		"	public class Y extends X implements IY {\n" + 
		"	             ^\n" + 
		"The method getString5(String) from class X cannot implement the corresponding method from type IY due to incompatible nullness constraints. \n" + 
		"----------\n" + 
		"4. ERROR in p1\\Y.java (at line 2)\n" + 
		"	public class Y extends X implements IY {\n" + 
		"	             ^\n" + 
		"The method getString3(String) from class X cannot implement the corresponding method from type IY due to incompatible nullness constraints. \n" + 
		"----------\n");
}
// a nullable return value is dereferenced without a check
public void test_nullable_return_001() {
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @Nullable Object getObject() { return null; }\n" +
			"    void foo() {\n" +
			"        Object o = getObject();\n" +
			"        System.out.print(o.toString());\n" +
			"    }\n" +
			"}\n"
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 6)\n" + 
		"	System.out.print(o.toString());\n" + 
		"	                 ^\n" + 
		"Potential null pointer access: The variable o may be null at this location\n" + 
		"----------\n");
}
// a nullable return value is dereferenced without a check, method is read from .class file
public void test_nullable_return_002() {
	runConformTestWithLibs(
		new String[] {
			"Lib.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Lib {\n" +
			"    @Nullable Object getObject() { return null; }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"");
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			"public class X {\n" +
			"    void foo(Lib l) {\n" +
			"        Object o = l.getObject();\n" +
			"        System.out.print(o.toString());\n" +
			"    }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	System.out.print(o.toString());\n" + 
		"	                 ^\n" + 
		"Potential null pointer access: The variable o may be null at this location\n" + 
		"----------\n");
}
// a non-null return value is checked for null, method is read from .class file
public void test_nonnull_return_001() {
	runConformTestWithLibs(
		new String[] {
			"Lib.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Lib {\n" +
			"    @NonNull Object getObject() { return new Object(); }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"");
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
			"X.java",
			"public class X {\n" +
			"    void foo(Lib l) {\n" +
			"        Object o = l.getObject();\n" +
			"        if (o != null)\n" +
			"            System.out.print(o.toString());\n" +
			"    }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	if (o != null)\n" + 
		"	    ^\n" + 
		"Redundant null check: The variable o cannot be null at this location\n" + 
		"----------\n");
}
// a non-null method returns null
public void test_nonnull_return_003() {
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @NonNull Object getObject(boolean b) {\n" +
			"        if (b)\n" +
			"            return null;\n" + // definite specification violation despite enclosing "if"
			"        return new Object();\n" +
			"    }\n" +
			"}\n"
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 5)\n" + 
		"	return null;\n" + 
		"	       ^^^^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value is null\n" + 
		"----------\n");
}
// a non-null method potentially returns null
public void test_nonnull_return_004() {
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @NonNull Object getObject(@Nullable Object o) {\n" +
			"        return o;\n" + // 'o' is only potentially null
			"    }\n" +
			"}\n"
		},
		null /*customOptions*/,
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	return o;\n" + 
		"	       ^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value can be null\n" + 
		"----------\n");
}
// a non-null method returns its non-null argument
public void test_nonnull_return_005() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	runConformTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @NonNull Object getObject(@NonNull Object o) {\n" +
			"        return o;\n" +
			"    }\n" +
			"}\n"
		},
		customOptions,
		"");
}
//a non-null method has insufficient nullness info for its return value
public void test_nonnull_return_006() {
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @NonNull Object getObject(Object o) {\n" +
			"        return o;\n" +
			"    }\n" +
			"}\n"
		},
		"----------\n" + 
		"1. WARNING in X.java (at line 4)\n" + 
		"	return o;\n" + 
		"	       ^\n" + 
		"Potential type mismatch: required \'@NonNull Object\' but nullness of the provided value is unknown\n" + 
		"----------\n");
}
// a result from a nullable method is directly dereferenced
public void test_nonnull_return_007() {
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @Nullable Object getObject() {\n" +
			"        return null;\n" +
			"    }\n" +
			"    void test() {\n" +
			"        getObject().toString();\n" +
			"    }\n" +
			"}\n"
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 7)\n" + 
		"	getObject().toString();\n" + 
		"	^^^^^^^^^^^\n" + 
		"Potential null pointer access: The method getObject() may return null\n" + 
		"----------\n");
}
// a result from a nonnull method is directly checked for null: redundant
public void test_nonnull_return_008() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportRedundantNullCheck, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @NonNull Object getObject() {\n" +
			"        return new Object();\n" +
			"    }\n" +
			"    void test() {\n" +
			"        if (getObject() == null)\n" +
			"		     throw new RuntimeException();\n" +
			"    }\n" +
			"}\n"
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 7)\n" + 
		"	if (getObject() == null)\n" + 
		"	    ^^^^^^^^^^^\n" + 
		"Redundant null check: The method getObject() cannot return null\n" + 
		"----------\n");
}
// a result from a nonnull method is directly checked for null (from local): redundant
public void test_nonnull_return_009() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportRedundantNullCheck, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @NonNull Object getObject() {\n" +
			"        return new Object();\n" +
			"    }\n" +
			"    void test() {\n" +
			"        Object left = null;\n" +
			"        if (left != getObject())\n" +
			"		     throw new RuntimeException();\n" +
			"    }\n" +
			"}\n"
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 8)\n" + 
		"	if (left != getObject())\n" + 
		"	    ^^^^\n" + 
		"Redundant null check: The variable left can only be null at this location\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 8)\n" + 
		"	if (left != getObject())\n" + 
		"	            ^^^^^^^^^^^\n" + 
		"Redundant null check: The method getObject() cannot return null\n" + 
		"----------\n");
}
// a result from a nullable method is assigned and checked for null (from local): not redundant
// see also Bug 336428 - [compiler][null] bogus warning "redundant null check" in condition of do {} while() loop
public void test_nonnull_return_010() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportRedundantNullCheck, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @Nullable X getX() {\n" +
			"        return new X();\n" +
			"    }\n" +
			"    void test() {\n" +
			"        X left = this;\n" +
			"        do {\n" +
			"            if (left == null) \n" +
			"	   	         throw new RuntimeException();\n" +
			"        } while ((left = left.getX()) != null);\n" + // no warning/error here!
			"    }\n" +
			"}\n"
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 9)\n" + 
		"	if (left == null) \n" + 
		"	    ^^^^\n" + 
		"Null comparison always yields false: The variable left cannot be null at this location\n" + 
		"----------\n");
}
// a non-null method returns a checked-for null value, but that branch is dead code
public void test_nonnull_return_011() {
	Map customOptions = getCompilerOptions();
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class X {\n" +
			"    Object getObject(Object dubious) {\n" +
			"        if (dubious == null)\n" + // redundant
			"            return dubious;\n" + // definitely null, but not reported inside dead code
			"        return new Object();\n" +
			"    }\n" +
			"}\n"
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 5)\n" + 
		"	if (dubious == null)\n" + 
		"	    ^^^^^^^\n" + 
		"Null comparison always yields false: The variable dubious cannot be null at this location\n" + 
		"----------\n" + 
		"2. WARNING in X.java (at line 6)\n" + 
		"	return dubious;\n" + 
		"	^^^^^^^^^^^^^^^\n" + 
		"Dead code\n" + 
		"----------\n");
}
// a non-null method returns a definite null from a conditional expression
// requires the fix for Bug 354554 - [null] conditional with redundant condition yields weak error message
// TODO(SH): ENABLE!
public void _test_nonnull_return_012() {
	Map customOptions = getCompilerOptions();
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class X {\n" +
			"    Object getObject(Object dubious) {\n" +
			"        return dubious == null ? dubious : null;\n" +
			"    }\n" +
			"}\n"
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 5)\n" + 
		"	return dubious == null ? dubious : null;\n" + 
		"	       ^^^^^^^\n" + 
		"Null comparison always yields false: The variable dubious cannot be null at this location\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 5)\n" + 
		"	return dubious == null ? dubious : null;\n" + 
		"	       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value is null\n" + 
		"----------\n");
}
// don't apply any default annotations to return void
public void test_nonnull_return_013() {
	Map customOptions = getCompilerOptions();
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	runConformTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class X {\n" +
			"    void getObject() {}\n" +
			"}\n",
			"Y.java",
			"public class Y extends X {\n" +
			"    @Override\n" +
			"    void getObject() {}\n" + // don't complain, void takes no (default) annotation
			"}\n"
		},
		customOptions,
		"");
}
// mixed use of fully qualified name / explicit import
public void test_annotation_import_001() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullableAnnotationName, "org.foo.Nullable");
	customOptions.put(NullCompilerOptions.OPTION_NonNullAnnotationName, "org.foo.NonNull");
	runConformTestWithLibs(
		new String[] {
			CUSTOM_NULLABLE_NAME,
			CUSTOM_NULLABLE_CONTENT,
			CUSTOM_NONNULL_NAME,
			CUSTOM_NONNULL_CONTENT,
			"Lib.java",
			"public class Lib {\n" +
			"    @org.foo.NonNull Object getObject() { return new Object(); }\n" + 	// FQN
			"}\n",
			"X.java",
			"import org.foo.NonNull;\n" +											// explicit import
			"public class X {\n" +
			"    @NonNull Object getObject(@NonNull Lib l) {\n" +
			"        return l.getObject();\n" +
			"    }\n" +
			"}\n"
		},
		customOptions,
		"");
}

// use of explicit imports throughout
public void test_annotation_import_002() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullableAnnotationName, "org.foo.Nullable");
	customOptions.put(NullCompilerOptions.OPTION_NonNullAnnotationName, "org.foo.NonNull");
	runConformTest(
		new String[] {
			CUSTOM_NULLABLE_NAME,
			CUSTOM_NULLABLE_CONTENT,
			CUSTOM_NONNULL_NAME,
			CUSTOM_NONNULL_CONTENT,
			"Lib.java",
			"import org.foo.NonNull;\n" +
			"public class Lib {\n" +
			"    @NonNull Object getObject() { return new Object(); }\n" +
			"}\n",
			"X.java",
			"import org.foo.NonNull;\n" +
			"public class X {\n" +
			"    @NonNull Object getObject(@org.foo.Nullable String dummy, @NonNull Lib l) {\n" +
			"        Object o = l.getObject();" +
			"        return o;\n" +
			"    }\n" +
			"}\n"
		},
		customOptions,
		"");
}
// explicit import of existing annotation types
// using a Lib without null specifications
public void test_annotation_import_005() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullableAnnotationName, "org.foo.MayBeNull");
	customOptions.put(NullCompilerOptions.OPTION_NonNullAnnotationName, "org.foo.MustNotBeNull");
	runNegativeTest(
		true/*shouldFlushOutputDirectory*/,
		new String[] {
			"org/foo/MayBeNull.java",
			"package org.foo;\n" +
			"import java.lang.annotation.*;\n" +
			"@Retention(RetentionPolicy.CLASS)\n" +
			"public @interface MayBeNull {}\n",
			
			"org/foo/MustNotBeNull.java",
			"package org.foo;\n" +
			"import java.lang.annotation.*;\n" +
			"@Retention(RetentionPolicy.CLASS)\n" +
			"public @interface MustNotBeNull {}\n",

			"Lib.java",
			"public class Lib {\n" +
			"    Object getObject() { return new Object(); }\n" +
			"}\n",
			"X.java",
			"import org.foo.*;\n" +
			"public class X {\n" +
			"    @MustNotBeNull Object getObject(@MustNotBeNull Lib l) {\n" +
			"        return l.getObject();\n" +
			"    }\n" +
			"}\n",
			
		},
		null /*no libs*/,
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	return l.getObject();\n" + 
		"	       ^^^^^^^^^^^^^\n" + 
		"Potential type mismatch: required \'@MustNotBeNull Object\' but nullness of the provided value is unknown\n" + 
		"----------\n",
		JavacTestOptions.Excuse.EclipseWarningConfiguredAsError);
}
// a non-null method returns a value obtained from an unannotated method, missing annotation types
public void test_annotation_import_006() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullableAnnotationName, "org.foo.MayBeNull");
	customOptions.put(NullCompilerOptions.OPTION_NonNullAnnotationName, "org.foo.MustNotBeNull");
	runNegativeTest(
		true/*shouldFlushOutputDirectory*/,
		new String[] {
			"Lib.java",
			"public class Lib {\n" +
			"    Object getObject() { return new Object(); }\n" +
			"}\n",
			"X.java",
			"public class X {\n" +
			"    @MustNotBeNull Object getObject(@MustNotBeNull Lib l) {\n" +
			"        return l.getObject();\n" +
			"    }\n" +
			"}\n"
		},
		null /* no libs */,
		customOptions,
		"----------\n" +
		"1. ERROR in X.java (at line 2)\n" + 
		"	@MustNotBeNull Object getObject(@MustNotBeNull Lib l) {\n" + 
		"	 ^^^^^^^^^^^^^\n" + 
		"MustNotBeNull cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 2)\n" + 
		"	@MustNotBeNull Object getObject(@MustNotBeNull Lib l) {\n" + 
		"	                                 ^^^^^^^^^^^^^\n" + 
		"MustNotBeNull cannot be resolved to a type\n" + 
		"----------\n",
		JavacTestOptions.Excuse.EclipseWarningConfiguredAsError);
}
// using nullness defaulting to nonnull, missing annotation types
public void test_annotation_import_007() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportNullContractInsufficientInfo, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullableAnnotationName, "org.foo.MayBeNull");
	customOptions.put(NullCompilerOptions.OPTION_NonNullAnnotationName, "org.foo.MustNotBeNull");
	customOptions.put(NullCompilerOptions.OPTION_NullnessDefault, NullCompilerOptions.NONNULL);
	runNegativeTest(
		true/*shouldFlushOutputDirectory*/,
		new String[] {
			"Lib.java",
			"public class Lib {\n" +
			"    Object getObject() { return new Object(); }\n" +
			"}\n",
			"X.java",
			"public class X {\n" +
			"    Object getObject(Lib l) {\n" +
			"        return l.getObject();\n" +
			"    }\n" +
			"}\n"
		},
		LIBS,
		customOptions,
		"----------\n" + 
		"1. ERROR in Lib.java (at line 1)\n" + 
		"	public class Lib {\n" + 
		"	^\n" + 
		"Buildpath problem: the type org.foo.MustNotBeNull which is configured as a null annotation type cannot be resolved\n" + 
		"----------\n",
		JavacTestOptions.Excuse.EclipseWarningConfiguredAsError);
}

// a null annotation is illegally used on a class:
public void test_illegal_annotation_001() {
	runNegativeTest(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNull public class X {\n" +
			"}\n"
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	@NonNull public class X {\n" + 
		"	^^^^^^^^\n" + 
		"The annotation @NonNull is disallowed for this location\n" + 
		"----------\n",
		LIBS,
		false/*shouldFlush*/);	
}
// setting default to nullable, default applies to a parameter
public void test_default_nullness_001() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullnessDefault, NullCompilerOptions.NULLABLE);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @NonNull Object getObject(Object o) {\n" +
			"        return o;\n" + // illegal due to default @Nullable of parameter
			"    }\n" +
			"}\n",

		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	return o;\n" + 
		"	       ^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value can be null\n" + 
		"----------\n");
}
public void test_default_nullness_002() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullnessDefault, NullCompilerOptions.NONNULL);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    Object getObject(@Nullable Object o) {\n" +
			"        return new Object();\n" +
			"    }\n" +
			"}\n",
			"Y.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Y extends X {\n" +
			"    @Override\n" +
			"    @Nullable Object getObject(Object o) {\n" + // complain illegal return redef and inherited annot is not repeated
			"        return o;\n" +
			"    }\n" +
			"}\n",
		},
		customOptions,
		// main error:
		"----------\n" + 
		"1. ERROR in Y.java (at line 4)\n" + 
		"	@Nullable Object getObject(Object o) {\n" + 
		"	^^^^^^^^^^^^^^^^\n" + 
		"The return type is incompatible with the @NonNull return from X.getObject(Object)\n" + 
		"----------\n" +
		// additional error:
		"2. ERROR in Y.java (at line 4)\n" + 
		"	@Nullable Object getObject(Object o) {\n" + 
		"	                           ^^^^^^\n" + 
		"Illegal redefinition of parameter o, inherited method from X declares this parameter as @Nullable\n" + 
		"----------\n");
}
// package default is non-null
public void test_default_nullness_003() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	runNegativeTestWithLibs(
		new String[] {
	"p1/X.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class X {\n" +
			"    protected Object getObject(@Nullable Object o) {\n" +
			"        return new Object();\n" +
			"    }\n" +
			"}\n",
	"p2/package-info.java",
			"@org.eclipse.jdt.annotation.NonNullByDefault\n" +
			"package p2;\n",
	"p2/Y.java",
			"package p2;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Y extends p1.X {\n" +
			"    @Override\n" +
			"    protected @Nullable Object getObject(@Nullable Object o) {\n" +
			"        bar(o);\n" +
			"        return o;\n" +
			"    }\n" +
			"	 void bar(Object o2) { }\n" + // parameter is nonnull per package default
			"}\n"
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in p2\\Y.java (at line 5)\n" + 
		"	protected @Nullable Object getObject(@Nullable Object o) {\n" + 
		"	          ^^^^^^^^^^^^^^^^\n" + 
		"The return type is incompatible with the @NonNull return from X.getObject(Object)\n" + 
		"----------\n" + 
		"2. ERROR in p2\\Y.java (at line 6)\n" + 
		"	bar(o);\n" + 
		"	    ^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value can be null\n" + 
		"----------\n");
}
// package level default is consumed from package-info.class
public void test_default_nullness_003a() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	runConformTestWithLibs(
		new String[] {
	"p1/X.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class X {\n" +
			"    protected Object getObject(@Nullable Object o) {\n" +
			"        return new Object();\n" +
			"    }\n" +
			"	 protected void bar(Object o2) { }\n" + // parameter is nonnull per type default
			"}\n",
	"p2/package-info.java",
			"@org.eclipse.jdt.annotation.NonNullByDefault\n" +
			"package p2;\n",
			},
			customOptions,
			"");
	// check if default is visible from package-info.class.
	runNegativeTestWithLibs(
		false, // don't flush
		new String[] {
	"p2/Y.java",
			"package p2;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Y extends p1.X {\n" +
			"    @Override\n" +
			"    protected @Nullable Object getObject(@Nullable Object o) {\n" + // can't override inherited default nonnull 
			"        bar(o);\n" + // parameter is nonnull in super class's .class file
			"        accept(o);\n" +
			"        return o;\n" +
			"    }\n" +
			"    void accept(Object a) {}\n" + // governed by package level default
			"}\n"
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in p2\\Y.java (at line 5)\n" + 
		"	protected @Nullable Object getObject(@Nullable Object o) {\n" + 
		"	          ^^^^^^^^^^^^^^^^\n" + 
		"The return type is incompatible with the @NonNull return from X.getObject(Object)\n" + 
		"----------\n" + 
		"2. ERROR in p2\\Y.java (at line 6)\n" + 
		"	bar(o);\n" + 
		"	    ^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value can be null\n" + 
		"----------\n" + 
		"3. ERROR in p2\\Y.java (at line 7)\n" + 
		"	accept(o);\n" + 
		"	       ^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value can be null\n" + 
		"----------\n");
}
// don't apply type-level default to non-reference type
public void test_default_nullness_004() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	runConformTestWithLibs(
		new String[] {
	"p1/X.java",
			"package p1;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class X {\n" +
			"    protected Object getObject(boolean o) {\n" +
			"        return new Object();\n" +
			"    }\n" +
			"}\n",
	"p2/Y.java",
			"package p2;\n" +
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class Y extends p1.X {\n" +
			"    @Override\n" +
			"    protected @NonNull Object getObject(boolean o) {\n" +
			"        return o ? this : new Object();\n" +
			"    }\n" +
			"}\n"
		},
		customOptions,
		"");
}
// package default is non-null
// see also Bug 354536 - compiling package-info.java still depends on the order of compilation units
public void test_default_nullness_005() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NonNullAnnotationName, "org.foo.NonNull");
	runNegativeTestWithLibs(
		new String[] {
	"p1/X.java",
			"package p1;\n" +
			"public class X {\n" +
			"    class Inner {" +
			"        protected Object getObject(String s) {\n" +
			"            return null;\n" +
			"        }\n" +
			"    }\n" +
			"}\n",
	"p1/package-info.java",
			"@org.eclipse.jdt.annotation.NonNullByDefault\n" +
			"package p1;\n",
	CUSTOM_NONNULL_NAME,
			CUSTOM_NONNULL_CONTENT
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in p1\\X.java (at line 4)\n" + 
		"	return null;\n" + 
		"	       ^^^^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value is null\n" + 
		"----------\n");
}
// package default is non-null, package-info.java read before the annotation type
// compile order: beginToCompile(X.Inner) triggers reading of package-info.java before the annotation type was read
public void test_default_nullness_006() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NonNullAnnotationName, "org.foo.NonNull");
	runNegativeTestWithLibs(
		new String[] {
	"p1/package-info.java",
			"@org.eclipse.jdt.annotation.NonNullByDefault\n" +
			"package p1;\n",
	"p1/X.java",
			"package p1;\n" +
			"public class X {\n" +
			"    class Inner {" +
			"        protected Object getObject(String s) {\n" +
			"            return null;\n" +
			"        }\n" +
			"    }\n" +
			"}\n",
	CUSTOM_NONNULL_NAME,
			CUSTOM_NONNULL_CONTENT
		},
		customOptions,
		"----------\n" + 
		"1. ERROR in p1\\X.java (at line 4)\n" + 
		"	return null;\n" + 
		"	       ^^^^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value is null\n" + 
		"----------\n");
}
// global default nonnull, but return may be null 
public void test_default_nullness_007() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullnessDefault, NullCompilerOptions.NONNULL);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    @Nullable Object dangerous() {\n" +
			"        return null;\n" + 
			"    }\n" +
			"    Object broken() {\n" +
			"        return dangerous();\n" +
			"    }\n" +
			"}\n",

		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 7)\n" + 
		"	return dangerous();\n" + 
		"	       ^^^^^^^^^^^\n" + 
		"Type mismatch: required \'@NonNull Object\' but the provided value can be null\n" + 
		"----------\n");
}
// a nonnull variable is dereferenced in a loop
public void test_nonnull_var_in_constrol_structure_1() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullnessDefault, NullCompilerOptions.NONNULL);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    void print4(@NonNull String s) {\n" +
			"        for (int i=0; i<4; i++)\n" +
			"             print(s);\n" + 
			"    }\n" +
			"    void print5(@Nullable String s) {\n" +
			"        for (int i=0; i<5; i++)\n" +
			"             print(s);\n" + 
			"    }\n" +
			"    void print6(boolean b) {\n" +
			"        String s = b ? null : \"\";\n" +
			"        for (int i=0; i<5; i++)\n" +
			"             print(s);\n" + 
			"    }\n" +
			"    void print(@NonNull String s) {\n" +
			"        System.out.print(s);\n" +
			"    }\n" +
			"}\n",

		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 9)\n" + 
		"	print(s);\n" + 
		"	      ^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 14)\n" + 
		"	print(s);\n" + 
		"	      ^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n");
}
// a nonnull variable is dereferenced in a finally block
public void test_nonnull_var_in_constrol_structure_2() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullnessDefault, NullCompilerOptions.NONNULL);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    void print4(@NonNull String s) {\n" +
			"        try { /*empty*/ } finally {\n" +
			"             print(s);\n" +
			"        }\n" + 
			"    }\n" +
			"    void print5(@Nullable String s) {\n" +
			"        try { /*empty*/ } finally {\n" +
			"             print(s);\n" +
			"        }\n" + 
			"    }\n" +
			"    void print6(boolean b) {\n" +
			"        String s = b ? null : \"\";\n" +
			"        try { /*empty*/ } finally {\n" +
			"             print(s);\n" +
			"        }\n" + 
			"    }\n" +
			"    void print(@NonNull String s) {\n" +
			"        System.out.print(s);\n" +
			"    }\n" +
			"}\n",

		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 10)\n" + 
		"	print(s);\n" + 
		"	      ^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 16)\n" + 
		"	print(s);\n" + 
		"	      ^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n");
}
// a nonnull variable is dereferenced in a finally block inside a loop
public void test_nonnull_var_in_constrol_structure_3() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullnessDefault, NullCompilerOptions.NONNULL);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"public class X {\n" +
			"    void print4(@NonNull String s) {\n" +
			"        for (int i=0; i<4; i++)\n" +
			"            try { /*empty*/ } finally {\n" +
			"                 print(s);\n" +
			"            }\n" + 
			"    }\n" +
			"    void print5(@Nullable String s) {\n" +
			"        for (int i=0; i<5; i++)\n" +
			"            try { /*empty*/ } finally {\n" +
			"                 print(s);\n" +
			"            }\n" + 
			"    }\n" +
			"    void print6(boolean b) {\n" +
			"        String s = b ? null : \"\";\n" +
			"        for (int i=0; i<4; i++)\n" +
			"            try { /*empty*/ } finally {\n" +
			"                 print(s);\n" +
			"            }\n" + 
			"    }\n" +
			"    void print(@NonNull String s) {\n" +
			"        System.out.print(s);\n" +
			"    }\n" +
			"}\n",

		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 12)\n" + 
		"	print(s);\n" + 
		"	      ^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 19)\n" + 
		"	print(s);\n" + 
		"	      ^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n");
}
//a nonnull variable is dereferenced method of a nested type
public void test_nesting_1() {
	Map customOptions = getCompilerOptions();
	customOptions.put(CompilerOptions.OPTION_ReportNullReference, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_ReportPotentialNullContractViolation, CompilerOptions.ERROR);
	customOptions.put(NullCompilerOptions.OPTION_NullnessDefault, NullCompilerOptions.NONNULL);
	runNegativeTestWithLibs(
		new String[] {
			"X.java",
			"import org.eclipse.jdt.annotation.*;\n" +
			"@NonNullByDefault\n" +
			"public class X {\n" +
			"    void print4(final String s1) {\n" +
			"        for (int i=0; i<3; i++)\n" +
			"            new Runnable() {\n" +
			"                public void run() {\n" +
			"                     print(s1);\n" +
			"                }\n" +
			"            }.run();\n" + 
			"    }\n" +
			"    void print8(final @Nullable String s2) {\n" +
			"        for (int i=0; i<3; i++)\n" +
			"            new Runnable() {\n" +
			"                public void run() {\n" +
			"                     print(s2);\n" +
			"                }\n" +
			"            }.run();\n" + 
			"    }\n" +
			"    void print16(boolean b) {\n" +
			"        final String s3 = b ? null : \"\";\n" +
			"        for (int i=0; i<3; i++)\n" +
			"            new Runnable() {\n" +
			"                public void run() {\n" +
			"                     @NonNull String s3R = s3;\n" +
			"                }\n" +
			"            }.run();\n" + 
			"    }\n" +
			"    void print(String s) {\n" +
			"        System.out.print(s);\n" +
			"    }\n" +
			"}\n",

		},
		customOptions,
		"----------\n" + 
		"1. ERROR in X.java (at line 16)\n" + 
		"	print(s2);\n" + 
		"	      ^^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 25)\n" + 
		"	@NonNull String s3R = s3;\n" + 
		"	                      ^^\n" + 
		"Type mismatch: required \'@NonNull String\' but the provided value can be null\n" + 
		"----------\n");
}
// Test a regression incurred to the OT/J based implementation
// by the fix in Bug 360328 - [compiler][null] detect null problems in nested code (local class inside a loop)
public void test_constructor_with_nested_class() {
	runConformTest(
		new String[] {
			"X.java",
			"public class X {\n" +
			"    final Object o1;\n" +
			"    final Object o2;\n" +
			"    public X() {\n" +
			"         this.o1 = new Object() {\n" +
			"             public String toString() { return \"O1\"; }\n" +
			"         };\n" +
			"         this.o2 = new Object();" +
			"    }\n" +
			"}\n"
		},
		"");
}
}
