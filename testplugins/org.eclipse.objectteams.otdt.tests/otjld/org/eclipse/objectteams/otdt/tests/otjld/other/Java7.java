/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 Stephan Herrmann
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.other;

import java.io.File;
import java.util.Map;

import junit.framework.Test;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.tests.util.Util;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

public class Java7 extends AbstractOTJLDTest {
	
	public Java7(String name) {
		super(name);
	}
	
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "testA01_tryWithResources01"};
//		TESTS_NUMBERS = new int[] { 1459 };
//		TESTS_RANGE = new int[] { 1097, -1 };
	}

	public static Test suite() {
		Test suite = buildMinimalComplianceTestSuite(Java7.class, F_1_7);
		TESTS_COUNTERS.put(Java7.class.getName(), new Integer(suite.countTestCases()));
		return suite;
	}

	public static Class testClass() {
		return Java7.class;
	}
	
	// Bug 355007 - Using role as resource in try-with-resources creates bogus byte code
	public void testA01_tryWithResources01() {
		String fileName = OUTPUT_DIR+"/p1/data.txt";
		new File(OUTPUT_DIR+"/p1").mkdirs();
		Util.writeToFile("Hello Reader", fileName);
		runConformTest(
			false, // should not flush
			new String[] {
		"p1/TeamA01twr01.java",
			"package p1;\n" +
			"import java.io.*;\n" +
			"public team class TeamA01twr01 {\n" + 
			"    protected class ReaderRole implements AutoCloseable playedBy TA01twr01 {\n" + 
			"        public char[] content;\n" + 
			"        protected void read12() throws IOException {\n" + 
			"            content = new char[12];\n" + 
			"            read(content);\n" + 
			"        }\n" + 
			"        void read(char[] chars) -> int read(char[] chars);\n" + 
			"        close -> close;\n" + 
			"    }\n" + 
			"    void test(TA01twr01 bfr) throws Exception {\n" + 
			"        try (ReaderRole r = new ReaderRole(bfr)) {\n" + 
			"            r.read12();\n" + 
			"            System.out.println(String.valueOf(r.content));\n" + 
			"        }\n" + 
			"    } \n" + 
			"    public static void main(String[] args) throws Exception {\n" + 
			"        new TeamA01twr01().test(new TA01twr01(\""+fileName+"\"));\n" + 
			"    }\n" + 
			"}",
	"p1/TA01twr01.java",
			"package p1;\n" +
			"import java.io.*;\n" +
			"public class TA01twr01 extends FileReader {\n" + 
			"    public TA01twr01(String fileName) throws IOException {\n" + 
			"        super(fileName);\n" + 
			"    }\n" + 
			"    @Override\n" + 
			"    public void close() {\n" + 
			"        try {\n" + 
			"            super.close();\n" + 
			"            System.out.println(\"closed\");\n" + 
			"        } catch (IOException ioe) {\n" + 
			"            ioe.printStackTrace();\n" + 
			"        }\n" + 
			"    }\n" + 
			"}",
			},
			"----------\n" +
			"1. WARNING in p1\\TeamA01twr01.java (at line 14)\n" +
			"	try (ReaderRole r = new ReaderRole(bfr)) {\n" +
			"	                    ^^^^^^^^^^^^^^^^^^^\n" +
			"Argument to lifting constructor ReaderRole(TA01twr01) is not a freshly created base object (of type p1.TA01twr01); may cause a DuplicateRoleException at runtime (OTJLD 2.4.1(c)).\n" +
			"----------\n",
			"Hello Reader\n" +
			"closed",
			"",
			null);
	}
	
	// Bug 388060 - [compiler] closeable role triggers resource warning from lift method
	@SuppressWarnings("unchecked")
	public void testA01_tryWithResources02() {
		String fileName = OUTPUT_DIR+"/p1/data.txt";
		new File(OUTPUT_DIR+"/p1").mkdirs();
		Util.writeToFile("Hello Reader", fileName);
		Map options = getCompilerOptions();
		options.put(JavaCore.COMPILER_PB_POTENTIALLY_UNCLOSED_CLOSEABLE, JavaCore.ERROR);
		runTest(
			new String[] {
		"p1/TeamA01twr01.java",
			"package p1;\n" +
			"import java.io.*;\n" +
			"public team class TeamA01twr01 {\n" + 
			"    protected class ReaderRole implements AutoCloseable playedBy TA01twr01 {\n" + 
			"        public char[] content;\n" + 
			"        protected void read12() throws IOException {\n" + 
			"            content = new char[12];\n" + 
			"            read(content);\n" + 
			"        }\n" + 
			"        void read(char[] chars) -> int read(char[] chars);\n" + 
			"        close -> close;\n" + 
			"    }\n" + 
			"    void test(String filename) throws Exception {\n" + 
			"        try (ReaderRole r = new ReaderRole(new TA01twr01(filename))) {\n" + 
			"            r.read12();\n" + 
			"            System.out.println(String.valueOf(r.content));\n" + 
			"        }\n" + 
			"    } \n" + 
			"    public static void main(String[] args) throws Exception {\n" + 
			"        new TeamA01twr01().test(\""+fileName+"\");\n" + 
			"    }\n" + 
			"}",
	"p1/TA01twr01.java",
			"package p1;\n" +
			"import java.io.*;\n" +
			"public class TA01twr01 extends FileReader {\n" + 
			"    public TA01twr01(String fileName) throws IOException {\n" + 
			"        super(fileName);\n" + 
			"    }\n" + 
			"    @Override\n" + 
			"    public void close() {\n" + 
			"        try {\n" + 
			"            super.close();\n" + 
			"            System.out.println(\"closed\");\n" + 
			"        } catch (IOException ioe) {\n" + 
			"            ioe.printStackTrace();\n" + 
			"        }\n" + 
			"    }\n" + 
			"}",
			},
			false, // no comp err
			"",
			"Hello Reader\n" +
			"closed",
			"",
			true, // force exec
			null, // libs
			false, // should not flush
			null, // vm args
			options,
			null, // requestor
			true);// skip javac
	}
}
