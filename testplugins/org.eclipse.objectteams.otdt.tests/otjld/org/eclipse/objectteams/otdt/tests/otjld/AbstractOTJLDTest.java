/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2010, 2014 Stephan Herrmann
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
package org.eclipse.objectteams.otdt.tests.otjld;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.tests.compiler.regression.AbstractComparableTest;
import org.eclipse.jdt.core.tests.compiler.regression.InMemoryNameEnvironment;
import org.eclipse.jdt.core.tests.compiler.regression.RegressionTestSetup;
import org.eclipse.jdt.core.tests.compiler.regression.Requestor;
import org.eclipse.jdt.core.tests.util.CompilerTestSetup;
import org.eclipse.jdt.core.tests.util.TestVerifier;
import org.eclipse.jdt.core.tests.util.Util;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.core.search.processing.JobManager;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;
import org.eclipse.objectteams.otdt.tests.ClasspathUtil;

/**
 * Common super class for tests that were formerly implemented using jacks.
 * @author stephan
 */
public class AbstractOTJLDTest extends AbstractComparableTest {

	/** a test verifier that allows reusing a running vm even in the presence of (constant) vm arguments. */
	protected class OTTestVerifier extends TestVerifier {
		protected OTTestVerifier(boolean reuseVM) {
			super(reuseVM);
			this.fVMArguments = getOTVMArgs();
		}
		@Override
		public boolean verifyClassFiles(String sourceFilePath, String className, String expectedOutputString, String expectedErrorStringStart,
										String[] classpaths, String[] programArguments, String[] vmArguments) {
			vmArguments = mergeArgs(vmArguments);
			return super.verifyClassFiles(sourceFilePath, className, expectedOutputString, expectedErrorStringStart, classpaths, programArguments, vmArguments);
		}
		@Override
		public boolean vmArgsEqual(String[] newArgs) {
			return super.vmArgsEqual(mergeArgs(newArgs));
		}
		protected String[] mergeArgs(String[] newArgs) {
			String[] OTVMArgs = getOTVMArgs();
			if (newArgs == null)
				return OTVMArgs;
			else {
				int l1 = OTVMArgs.length;
				int l2 = newArgs.length;
				String[] result = new String[l1+l2];
				System.arraycopy(OTVMArgs, 0, result, 0, l1);
				System.arraycopy(newArgs, 0, result, l1, l2);
				return result;
			}
		}
	}

	private static String getOTDTJarPath(String jarName) {
		return ClasspathUtil.OTDT_PATH + File.separator + "lib" + File.separator + jarName + ".jar";
	}

	public String[] getOTVMArgs() {
		String OTRE_MIN_JAR_PATH, OTAGENT_JAR_PATH;
		OTRE_MIN_JAR_PATH 		= getOTDTJarPath("otre_min");
		switch (this.weavingScheme) {
		case OTDRE:
			OTAGENT_JAR_PATH  		= getOTDTJarPath("otredyn_agent");
			break;
		case OTRE:
			OTAGENT_JAR_PATH  		= getOTDTJarPath("otre_agent");
			break;
		default:
			throw new IllegalStateException("Unsupported weavingScheme "+this.weavingScheme);
		}
		if (isJRE9Plus)
			return new String[] {
				"-javaagent:"+OTAGENT_JAR_PATH,
				"-Xbootclasspath/a:"+OTRE_MIN_JAR_PATH,
				"-Dot.dump=1",
				"-Dobjectteams.otdre.verify=1",
				"--add-reads",
				"java.base=ALL-UNNAMED",
				"--add-reads",
				"jdk.net=ALL-UNNAMED",
				"--add-opens",
				"java.base/java.lang=ALL-UNNAMED"
			};
		return new String[] {
				"-javaagent:"+OTAGENT_JAR_PATH,
				"-Xbootclasspath/a:"+OTRE_MIN_JAR_PATH,
				"-Dot.dump=1",
				"-Dobjectteams.otdre.verify=1"
		};
	}

	public static boolean IS_JRE_8;
	static {
		String javaVersion = System.getProperty("java.specification.version");
		IS_JRE_8 = "1.8".equals(javaVersion);
	}
	protected String foreach(String elemType) {
		return (IS_JRE_8 && this.complianceLevel < ClassFileConstants.JDK1_8)
				? "public void forEach(java.util.function.Consumer<? super "+elemType+"> element) {}\n"
				: "";
	}
	protected String spliterator(String elemType) {
		return (IS_JRE_8 && this.complianceLevel < ClassFileConstants.JDK1_8)
				? "public java.util.Spliterator<"+elemType+"> spliterator() { return null; }\n"
				: "";
	}
	protected String spliteratorCallout() {
		return (IS_JRE_8 && this.complianceLevel < ClassFileConstants.JDK1_8) ? "spliterator -> spliterator;\n" : "";
	}

// copy from JDT-orig for visibility's sake
	protected class Runner {
		public boolean shouldFlushOutputDirectory = true;
		// input:
		public String[] testFiles;
		public String[] dependantFiles;
		public String[] classLibraries;
		public boolean  libsOnModulePath;
		// control compilation:
		public Map<String,String> customOptions;
		public boolean performStatementsRecovery;
		public boolean generateOutput;
		public ICompilerRequestor customRequestor;
		// compiler result:
		public String expectedCompilerLog;
		public String[] alternateCompilerLogs;
		public boolean showCategory;
		public boolean showWarningToken;
		// javac:
		public boolean skipJavac;
		public String expectedJavacOutputString;
		public JavacTestOptions javacTestOptions;
		// execution:
		public boolean forceExecution;
		public String[] vmArguments;
		public String expectedOutputString;
		public String expectedErrorString;

		public ASTVisitor visitor;

		public Runner() {}

		public void runConformTest() {
			runTest(this.shouldFlushOutputDirectory,
					this.testFiles,
					this.dependantFiles != null ? this.dependantFiles : new String[] {},
					this.classLibraries,
					this.libsOnModulePath,
					this.customOptions,
					this.performStatementsRecovery,
					new Requestor(
							this.generateOutput,
							this.customRequestor,
							this.showCategory,
							this.showWarningToken),
					false,
					this.expectedCompilerLog,
					this.alternateCompilerLogs,
					this.forceExecution,
					this.vmArguments,
					this.expectedOutputString,
					this.expectedErrorString,
					this.visitor,
					this.expectedJavacOutputString != null ? this.expectedJavacOutputString : this.expectedOutputString,
					this.skipJavac ? JavacTestOptions.SKIP : this.javacTestOptions);
		}

		public void runNegativeTest() {
			runTest(this.shouldFlushOutputDirectory,
					this.testFiles,
					this.dependantFiles != null ? this.dependantFiles : new String[] {},
					this.classLibraries,
					this.libsOnModulePath,
					this.customOptions,
					this.performStatementsRecovery,
					new Requestor(
							this.generateOutput,
							this.customRequestor,
							this.showCategory,
							this.showWarningToken),
					true,
					this.expectedCompilerLog,
					this.alternateCompilerLogs,
					this.forceExecution,
					this.vmArguments,
					this.expectedOutputString,
					this.expectedErrorString,
					this.visitor,
					this.expectedJavacOutputString != null ? this.expectedJavacOutputString : this.expectedOutputString,
					this.skipJavac ? JavacTestOptions.SKIP : this.javacTestOptions);
		}

		public void runWarningTest() {
			runTest(this.shouldFlushOutputDirectory,
					this.testFiles,
					this.dependantFiles != null ? this.dependantFiles : new String[] {},
					this.classLibraries,
					this.libsOnModulePath,
					this.customOptions,
					this.performStatementsRecovery,
					new Requestor(
							this.generateOutput,
							this.customRequestor,
							this.showCategory,
							this.showWarningToken),
					false,
					this.expectedCompilerLog,
					this.alternateCompilerLogs,
					this.forceExecution,
					this.vmArguments,
					this.expectedOutputString,
					this.expectedErrorString,
					this.visitor,
					this.expectedJavacOutputString != null ? this.expectedJavacOutputString : this.expectedOutputString,
					this.skipJavac ? JavacTestOptions.SKIP : this.javacTestOptions);
		}
	}

	// ===

	protected static final JavacTestOptions DEFAULT_TEST_OPTIONS = new JavacTestOptions();

	// shall compiler output be matched exactly or using some matching?
	boolean errorMatching = false;

	// each subarray defines a set of classes to be compiled together:
	protected String[][] compileOrder;

	protected WeavingScheme weavingScheme = WeavingScheme.OTDRE;

	public AbstractOTJLDTest(String name) {
		super(name);
	}

	/** Add otre/otdre and (bcel or asm) to the class path. */
	@Override
	protected String[] getDefaultClassPaths() {
		String[] defaults = super.getDefaultClassPaths();
		int len = defaults.length;
		IPath[] bytecodeLibJarPath = ClasspathUtil.getWeaverPaths(this.weavingScheme);
		int len2 = bytecodeLibJarPath.length;
		System.arraycopy(defaults, 0, defaults=new String[len+1+len2], 0, len);
		defaults[len] = new Path(ClasspathUtil.getOTREPath(this.weavingScheme)).toString();
		for (int i=0; i<len2; i++)
			defaults[len+1+i] = bytecodeLibJarPath[i].toString();
		return defaults;
	}

	boolean jobManVerbose;

	@Override
	public void initialize(CompilerTestSetup setUp) {
		super.initialize(setUp);
		if ("otre".equals(System.getProperty("ot.weaving"))
				||"otre".equals(System.getProperty("test.ot.weaving")))
			weavingScheme = WeavingScheme.OTRE;
		if (setUp instanceof RegressionTestSetup) {
			RegressionTestSetup regressionSetTup = (RegressionTestSetup) setUp;
			if (!(regressionSetTup.verifier instanceof OTTestVerifier)) {
				// overwrite plain TestVerifier:
				regressionSetTup.verifier = this.verifier = new OTTestVerifier(true/*reuseVM*/);
			}
		}
		jobManVerbose = JobManager.VERBOSE;
		JobManager.VERBOSE = false;
	}

	@Override
	protected TestVerifier getTestVerifier(boolean reuseVM) {
		return new OTTestVerifier(reuseVM);
	}

	@Override
	protected void tearDown() throws Exception {
		this.compileOrder = null;
		super.tearDown();
		JobManager.VERBOSE = jobManVerbose;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map getCompilerOptions() {
		Map options = super.getCompilerOptions();
		options.put(CompilerOptions.OPTION_ReportUnnecessaryElse, CompilerOptions.IGNORE);
		options.put(CompilerOptions.OPTION_ReportSyntheticAccessEmulation, CompilerOptions.IGNORE);
		options.put(CompilerOptions.OPTION_ReportUnusedWarningToken, CompilerOptions.ERROR);
		options.put(CompilerOptions.OPTION_WeavingScheme, this.weavingScheme.name());
		// FIXME: consider setting to warning:
		options.put(CompilerOptions.OPTION_ReportOtreWeavingIntoJava8, CompilerOptions.IGNORE);
		return options;
	}

	protected void runNegativeTestMatching(String[] testFiles, String expectedCompilerLog) {
		this.errorMatching = true;
		runTest(
		 		// test directory preparation
				true /* flush output directory */,
				testFiles /* test files */,
				// compiler options
				null /* no class libraries */,
				null /* no custom options */,
				false /* do not perform statements recovery */,
				null /* no custom requestor */,
				// compiler results
				true /* expecting compiler errors */,
				expectedCompilerLog /* expected compiler log */,
				// runtime options
				false /* do not force execution */,
				null /* no vm arguments */,
				// runtime results
				null /* do not check output string */,
				null /* do not check error string */,
				// javac options
				DEFAULT_TEST_OPTIONS /* javac test options */);
		this.errorMatching = false;
	}

	/** Relaxed comparison using contains rather than equals. TODO(SH): pattern matching. */
	protected void checkCompilerLog(String[] testFiles, Requestor requestor,
			String[] alternatePlatformIndependantExpectedLogs, Throwable exception) {
		String computedProblemLog = Util.convertToIndependantLineDelimiter(requestor.problemLog.toString());
		for (String platformIndependantExpectedLog : alternatePlatformIndependantExpectedLogs) {
			if (computedProblemLog.contains(platformIndependantExpectedLog))
				return; // OK
		}
		logTestTitle();
		System.out.println(Util.displayString(computedProblemLog, INDENT, SHIFT));
		logTestFiles(false, testFiles);
		if (errorMatching && exception == null)
			fail("Invalid problem log\n"+computedProblemLog);
		if (!errorMatching && exception == null) {
			assertEquals("Invalid problem log ", alternatePlatformIndependantExpectedLogs[0], computedProblemLog);
		}
    }
	// inaccessible helper from super
	void logTestFiles(boolean logTitle, String[] testFiles) {
		if (logTitle) {
			logTestTitle();
		}
		for (int i = 0; i < testFiles.length; i += 2) {
			System.out.print(testFiles[i]);
			System.out.println(" ["); //$NON-NLS-1$
			System.out.println(testFiles[i + 1]);
			System.out.println("]"); //$NON-NLS-1$
		}
	}
	// inaccessible helper from super
	void logTestTitle() {
		System.out.println(getClass().getName() + '#' + getName());
	}

	// support explicit compile order in several steps:
	@Override
	protected void compileTestFiles(Compiler batchCompiler, String[] testFiles) {
		if (this.compileOrder == null) {
			batchCompiler.sortCompilationUnits = true;
			super.compileTestFiles(batchCompiler, testFiles);
			batchCompiler.lookupEnvironment.nameEnvironment.cleanup(); // don't use cached info from previous runs
		} else {
			for (String[] bunch : this.compileOrder) {
				String[] bunchFiles = new String[bunch.length * 2];
				int b = 0;
				for (int i = 0; i < bunch.length; i++) {
					int b0 = b;
					for (int j = 0; j < testFiles.length; j+=2) {
						if (bunch[i].equals(testFiles[j])) {
							bunchFiles[b++] = testFiles[j];
							bunchFiles[b++] = testFiles[j+1];
						}
					}
					assertTrue("Unmatched filename: "+bunch[i], b == b0+2);
				}
				super.compileTestFiles(batchCompiler, bunchFiles);
				batchCompiler.lookupEnvironment.nameEnvironment.cleanup(); // don't use cached info from previous runs
			}
		}
	}

	@Override
	protected INameEnvironment getNameEnvironment(final String[] testFiles, String[] classPaths, Map<String, String> options) {
		this.classpaths = classPaths == null ? getDefaultClassPaths() : classPaths;
		// make cleanup weaker:
		return new InMemoryNameEnvironment(testFiles, getClassLibs(false, options)) {
			@Override
			public void cleanup() {
				for (int i = 0, max = this.classLibs.length; i < max; i++)
					if (this.classLibs[i] instanceof FileSystem)
						((FileSystem) this.classLibs[i]).softReset();
			}
		};
	}

	/** Additional entry for tests expecting a compiler warning and don't run. */
	protected void runTestExpectingWarnings(String[] files, String expectedWarnings) {
    	Map options = getCompilerOptions();
		runConformTest(
	 		// test directory preparation
			true /* flush output directory */,
			files,
			// compiler options
			null /* no class libraries */,
			options /* custom options - happen to be the default not changed by the test suite */,
			// compiler results
			expectedWarnings,
			// runtime results
			null /* do not check output string */,
			null /* do not check error string */,
			// javac options
			JavacTestOptions.Excuse.EclipseHasSomeMoreWarnings /* javac test options */);
	}

	/** Additional entry for tests expecting a compiler warning and don't run. */
	protected void runTestExpectingWarnings(String[] files, String expectedWarnings, Map options) {
		runConformTest(
	 		// test directory preparation
			true /* flush output directory */,
			files,
			// compiler options
			null /* no class libraries */,
			options /* custom options - happen to be the default not changed by the test suite */,
			// compiler results
			expectedWarnings,
			// runtime results
			null /* do not check output string */,
			null /* do not check error string */,
			// javac options
			JavacTestOptions.Excuse.EclipseHasSomeMoreWarnings /* javac test options */);
	}

	/** Additional entry for tests expecting a compiler warning and don't run. */
	protected void runTestExpectingWarnings(String[] files, String expectedWarnings, boolean flushOutputDirectory) {
    	Map options = getCompilerOptions();
		runConformTest(
	 		// test directory preparation
			flushOutputDirectory,
			files,
			// compiler options
			null /* no class libraries */,
			options /* custom options - happen to be the default not changed by the test suite */,
			// compiler results
			expectedWarnings,
			// runtime results
			null /* do not check output string */,
			null /* do not check error string */,
			// javac options
			JavacTestOptions.Excuse.EclipseHasSomeMoreWarnings /* javac test options */);
	}
	/** Additional entry for tests expecting a compiler warning and run. */
	protected void runTestExpectingWarnings(String[] files, String expectedWarnings, String expectedOutput) {
    	Map options = getCompilerOptions();
		runConformTest(
	 		// test directory preparation
			true/*flushOutputDirectory*/,
			files,
			// compiler options
			null /* no class libraries */,
			options /* custom options - happen to be the default not changed by the test suite */,
			// compiler results
			expectedWarnings,
			// runtime results
			expectedOutput,
			null /* do not check error string */,
			// javac options
			JavacTestOptions.Excuse.EclipseHasSomeMoreWarnings /* javac test options */);
	}

	protected void myWriteFiles(String[] testFiles) {
		// force the directory to comply with the infrastructure from AbstractRegressionTest:
		String testName = null;
		try {
			testName = getName();
			setName("regression");
			// Write files in dir
			writeFiles(testFiles);
		} finally {
			setName(testName);
		}
	}

	protected String getTestResourcePath(String filename) {
        try
        {
            URL platformURL = Platform
				                .getBundle("org.eclipse.objectteams.otdt.tests")
				                .getEntry("/testresources/"+filename);
            return new File(FileLocator.toFileURL(platformURL).getFile())
                .getAbsolutePath();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return null;
	}
	/** Create a vm arg for team activation and reset the verifier (avoid reuse of a running VM). */
	protected String[] getTeamActivationVMArgs(String relativeFilePath) {
		if (this.verifier != null)
			this.verifier.shutDown();
        this.verifier = getTestVerifier(false);
        this.createdVerifier = true;
		return new String[] {
				"-Dot.teamconfig="+OUTPUT_DIR+'/'+relativeFilePath
		};
	}
	
	static protected int ignoreCount = 0;
	protected boolean isKnownFailure(String test) {
		System.err.println("Test "+test+" is known to fail (#"+(++ignoreCount)+"), see https://bugs.eclipse.org/484164");
		return true; // boolean result to avoid dead code warning at call site
	}
}
