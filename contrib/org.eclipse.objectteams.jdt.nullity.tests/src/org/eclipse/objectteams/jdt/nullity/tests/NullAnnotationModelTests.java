/*******************************************************************************
 * Copyright (c) 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.objectteams.jdt.nullity.tests;

import java.io.IOException;
import java.net.URL;

import junit.framework.Test;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.tests.model.ReconcilerTests;
import org.eclipse.objectteams.internal.jdt.nullity.NullCompilerOptions;


public class NullAnnotationModelTests extends ReconcilerTests {
	
	String ANNOTATION_LIB;
	
	public static Test suite() {
		return buildModelTestSuite(NullAnnotationModelTests.class);
	}
	
	public NullAnnotationModelTests(String name) {
		super(name);
	}

	static {
//		TESTS_NAMES = new String[] { "testConvertedSourceType1" };
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		ANNOTATION_LIB = testJarPath("nullAnnotations.jar");
	}

	protected String testJarPath(String jarName) throws IOException {
		URL libEntry = Platform.getBundle("org.eclipse.objectteams.jdt.nullity.tests").getEntry("/lib/"+jarName);
		return FileLocator.toFileURL(libEntry).getPath();
	}

	public void testConvertedSourceType1() throws CoreException, InterruptedException {
    	try {
			// Resources creation
			IJavaProject p = createJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB", ANNOTATION_LIB}, "bin", "1.5");
			p.setOption(NullCompilerOptions.OPTION_AnnotationBasedNullAnalysis, JavaCore.ENABLED);
			p.setOption(NullCompilerOptions.OPTION_NonNullIsDefault, NullCompilerOptions.ENABLED);
	
			this.createFolder("/P/p1");
			String c1SourceString =	
				"package p1;\n" +
				"import org.eclipse.jdt.annotation.*;\n" +
				"public class C1 {\n" +
				"	 public String foo(@Nullable Object arg) {\n" + // this is consumed via SourceTypeConverter
				"		return arg == null ? \"\" : arg.toString();\n" +
				"	 }\n" +
				"}\n";
			this.createFile(
				"/P/p1/C1.java",
	    			c1SourceString);
			
			this.createFolder("/P/p2");
			String c2SourceString =
				"package p2;\n" +
				"public class C2 {\n" +
				"	 String bar(p1.C1 c, C2 c2) {;\n" +
				"        return c.foo(null);\n" + // don't complain despite default nonnull, foo has explicit @Nullable
				"    }\n" +
				"	 String foo(Object arg) {\n" +
				"		return arg == null ? null : arg.toString();\n" +
				"	 }\n" +
				"}\n";
			this.createFile(
				"/P/p2/C2.java",
	    			c2SourceString);

			char[] c2SourceChars = c2SourceString.toCharArray();
			this.problemRequestor.initialize(c2SourceChars);
			
			getCompilationUnit("/P/p2/C2.java").getWorkingCopy(this.wcOwner, null);
			
			assertProblems("Unexpected problems", "----------\n" + 
					"1. WARNING in /P/p2/C2.java (at line 7)\n" + 
					"	return arg == null ? null : arg.toString();\n" + 
					"	       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
					"Potential type mismatch: required \'@NonNull String\' but nullness of the provided value is unknown\n" + 
					"----------\n");
    	} finally {
    		deleteProject("P");
    	}
    }

	public void testBinaryType1() throws CoreException, InterruptedException, IOException {
    	try {
			// Resources creation
			IJavaProject p = createJavaProject("P", new String[] {""}, 
											   new String[] {"JCL15_LIB", ANNOTATION_LIB, testJarPath("example.jar")}, 
											   "bin", "1.5");
			p.setOption(NullCompilerOptions.OPTION_AnnotationBasedNullAnalysis, JavaCore.ENABLED);
			p.setOption(NullCompilerOptions.OPTION_NonNullIsDefault, NullCompilerOptions.ENABLED);
		
			// example.jar contains p1/C1.java just like testConvertedSourceType1()
			
			this.createFolder("/P/p2");
			String c2SourceString =
				"package p2;\n" +
				"public class C2 {\n" +
				"	 String bar(p1.C1 c) {;\n" +
				"        return c.foo(null);\n" + // don't complain despite default nonnull, foo has explicit @Nullable
				"    }\n" +
				"	 String foo(Object arg) {\n" +
				"		return arg == null ? null : arg.toString();\n" +
				"	 }\n" +
				"}\n";
			this.createFile(
				"/P/p2/C2.java",
	    			c2SourceString);

			char[] c2SourceChars = c2SourceString.toCharArray();
			this.problemRequestor.initialize(c2SourceChars);
			
			getCompilationUnit("/P/p2/C2.java").getWorkingCopy(this.wcOwner, null);
			
			assertProblems("Unexpected problems", "----------\n" + 
					"1. WARNING in /P/p2/C2.java (at line 7)\n" + 
					"	return arg == null ? null : arg.toString();\n" + 
					"	       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
					"Potential type mismatch: required \'@NonNull String\' but nullness of the provided value is unknown\n" + 
					"----------\n");
    	} finally {
    		deleteProject("P");
    	}
    }
}
