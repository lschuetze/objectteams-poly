/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2016 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
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
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otmodel;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.tests.model.ReconcilerTests;
import org.eclipse.jdt.core.tests.util.AbstractCompilerTest;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.ISourceType;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions.WeavingScheme;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.parser.SourceTypeConverter;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.SearchableEnvironment;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.core.SourceTypeElementInfo;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
import org.eclipse.objectteams.otdt.core.ext.OTREContainer;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.Config;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.Dependencies;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.ITranslationStates;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;

import junit.framework.Test;

/**
 * Tests for errors shown only in the gutter (CompilationUnitProblemFinder),
 * but not reported by the ImageBuilder.
 *
 * @author stephan
 * @since 1.2.1
 */
public class OTReconcilerTests extends ReconcilerTests {

	public static Test suite() {
		return buildModelTestSuite(OTReconcilerTests.class);
	}

	public OTReconcilerTests(String name) {
		super(name);
	}

	@Override
	public void setUp() throws Exception {
		this.indexDisabledForTest = false;
		super.setUp();
	}

	static {
//		TESTS_NAMES = new String[] { "testDecodeTeamAnchor" };
	}
// ===== Copied all our modifications from AbstractJavaModelTests =====
	/*
	 * Returns the OS path to the external directory that contains external jar files.
	 * This path ends with a File.separatorChar.
	 */
	protected String getExternalPath() {
		if (EXTERNAL_JAR_DIR_PATH == null)
			try {
				String path = getWorkspaceRoot().getLocation().toFile().getParentFile().getCanonicalPath();
				if (path.charAt(path.length()-1) != File.separatorChar)
					path += File.separatorChar;
				EXTERNAL_JAR_DIR_PATH = path;
//{ObjectTeams: use separate dir for our JCL since we use a modified version which must not conflict with the std version:
				EXTERNAL_JAR_DIR_PATH += "OTJCLDir"+File.separatorChar;
// SH}
			} catch (IOException e) {
				e.printStackTrace();
			}
		return EXTERNAL_JAR_DIR_PATH;
	}
	/**
	 * Returns the OS path to the directory that contains this plugin.
	 */
	protected String getPluginDirectoryPath() {
		try {
//{ObjectTeams: adjust plugin id from org.eclipse.jdt.core.tests.model to org.eclipse.objectteams.otdt.tests
			URL platformURL = Platform.getBundle("org.eclipse.objectteams.otdt.tests").getEntry("/");
//carp}
			return new File(FileLocator.toFileURL(platformURL).getFile()).getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	protected IJavaProject setUpJavaProject(final String projectName) throws CoreException, IOException {
//{ObjectTeams: OT/J needs at least 1.5 (was 1.4):
		IJavaProject javaProject = setUpJavaProject(projectName, "1.5");
		return javaProject;
// SH}
	}
//{ObjectTeams: copy jcl at least once per test run:
	private static boolean forceCopyJCL= true;
// SH}
	/**
	 * Check locally for the required JCL files, <jclName>.jar and <jclName>src.zip.
	 * If not available, copy from the project resources.
	 */
	public void setupExternalJCL(String jclName) throws IOException {
		String externalPath = getExternalPath();
		String separator = java.io.File.separator;
		String resourceJCLDir = getPluginDirectoryPath() + separator + "JCL";
		java.io.File jclDir = new java.io.File(externalPath);
		java.io.File jclMin =
			new java.io.File(externalPath + jclName + ".jar");
		java.io.File jclMinsrc = new java.io.File(externalPath + jclName + "src.zip");
		if (!jclDir.exists()) {
			if (!jclDir.mkdir()) {
				//mkdir failed
				throw new IOException("Could not create the directory " + jclDir);
			}
			//copy the two files to the JCL directory
			java.io.File resourceJCLMin =
				new java.io.File(resourceJCLDir + separator + jclName + ".jar");
			copy(resourceJCLMin, jclMin);
			java.io.File resourceJCLMinsrc =
				new java.io.File(resourceJCLDir + separator + jclName + "src.zip");
			copy(resourceJCLMinsrc, jclMinsrc);
		} else {
			//check that the two files, jclMin.jar and jclMinsrc.zip are present
			//copy either file that is missing or less recent than the one in workspace
			java.io.File resourceJCLMin =
				new java.io.File(resourceJCLDir + separator + jclName + ".jar");
//{ObjectTeams: added option forceCopyJCL
			if (forceCopyJCL ||
				(jclMin.lastModified() < resourceJCLMin.lastModified())
                    || (jclMin.length() != resourceJCLMin.length())) {
				copy(resourceJCLMin, jclMin);
			}
			java.io.File resourceJCLMinsrc =
				new java.io.File(resourceJCLDir + separator + jclName + "src.zip");
			if (forceCopyJCL ||
				(jclMinsrc.lastModified() < resourceJCLMinsrc.lastModified())
                    || (jclMinsrc.length() != resourceJCLMinsrc.length())) {
				copy(resourceJCLMinsrc, jclMinsrc);
			}
			if (forceCopyJCL)
				System.out.println("Test "+this.getClass().getName()+" has copied jclMin.");
			forceCopyJCL= false; // done
// SH}
		}
	}

// ===== End COPY_AND_PASTE
	// --- support OT libraries: ---
	boolean addOTtoLibrary = false;
	protected void addOTLibrary(IJavaProject javaProject, String jarName, String sourceZipName, String[] pathAndContents, String compliance) throws CoreException, IOException {
		boolean prev = this.addOTtoLibrary;
		try {
			this.addOTtoLibrary = true;
			addLibrary(javaProject, jarName, sourceZipName, pathAndContents, compliance);
		} finally {
			this.addOTtoLibrary = prev;
		}
	}
	@Override
	protected String[] getJCL15PlusLibraryIfNeeded(String compliance) throws JavaModelException, IOException {
		if (compliance.charAt(compliance.length()-1) >= '8' && (AbstractCompilerTest.getPossibleComplianceLevels() & AbstractCompilerTest.F_1_8) != 0) {
			// ensure that the JCL 18 lib is setup (i.e. that the jclMin18.jar is copied)
			setUpJCLClasspathVariables("1.8");
			if (addOTtoLibrary) {
				IPath otreMinJarPath = OTREContainer.getOtreMinJarPath();
				return new String[] {getExternalJCLPathString("1.8"), otreMinJarPath.toString() };
			}
			return new String[] {getExternalJCLPathString("1.8")};
		}
		if (compliance.charAt(compliance.length()-1) >= '5' && (AbstractCompilerTest.getPossibleComplianceLevels() & AbstractCompilerTest.F_1_5) != 0) {
			// ensure that the JCL 15 lib is setup (i.e. that the jclMin15.jar is copied)
			setUpJCLClasspathVariables("1.5");
			if (addOTtoLibrary)
				return new String[] {getExternalJCLPathString("1.5"), "OTRE" };
			return new String[] {getExternalJCLPathString("1.5")};
		}
		if (addOTtoLibrary) {
			IPath otreMinJarPath = OTREContainer.getOtreMinJarPath();
			return new String[] { otreMinJarPath.toString() };
		}
		return null;
	}
	// ---

	protected IJavaProject createOTJavaProject(String projectName, String[] sourceFolders, String[] libraries, String output) throws CoreException {
		return createOTJavaProject(projectName, sourceFolders, libraries, "1.5", output);
	}

	protected IJavaProject createOTJavaProject(String projectName, String[] sourceFolders, String[] libraries, String compliance, String output) throws CoreException {
		return createOTJavaProject(projectName, sourceFolders, libraries, compliance, output, false, null);
	}
	protected IJavaProject createOTJavaProject(String projectName, String[] sourceFolders, String[] libraries, String compliance, String output, boolean useFullJcl, String weaving) throws CoreException {
		IJavaProject javaProject = createJavaProject(projectName, sourceFolders, libraries, output, compliance, useFullJcl);
		IProjectDescription description = javaProject.getProject().getDescription();
		description.setNatureIds(OTDTPlugin.createProjectNatures(description));
		javaProject.getProject().setDescription(description, null);
		javaProject.setOption(CompilerOptions.OPTION_ReportWeaveIntoSystemClass, CompilerOptions.IGNORE);
		if (weaving == null) {
			weaving = System.getProperty("ot.weaving", null);
			if (weaving != null)
				javaProject.setOption(CompilerOptions.OPTION_WeavingScheme, weaving.toUpperCase());
			else
				javaProject.setOption(CompilerOptions.OPTION_WeavingScheme, CompilerOptions.WeavingScheme.OTDRE.toString());
		}
		return javaProject;
	}

	// http://trac.objectteams.org/ot/ticket/142
	public void testTrac142() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			p.setOption(CompilerOptions.OPTION_WeavingScheme, WeavingScheme.OTDRE.name());

			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			//prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			this.createFile(
				"/P/SuperFoo.java",
				"public team class SuperFoo {\n" +
				"	public class Role {}\n" +
				"	public SuperFoo(Role r) {\n" +
				"	}\n" +
				"}\n"
			);
			String sourceFoo = "public team class Foo extends SuperFoo {\n" +
			"	public class Role playedBy String {}\n" +
			"	public Foo(String as Role r) {\n" +
			"       super(r);\n" +
			"	}\n" +
			"}\n";
			this.createFile(
					"/P/Foo.java",
					sourceFoo
			);

			char[] sourceChars = sourceFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);


			getCompilationUnit("/P/Foo.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n"
			);
		} finally {
			deleteProject("P");
		}
	}

	// http://trac.objectteams.org/ot/ticket/143 (comment1)
	public void testTrac143b() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			p.setOption(CompilerOptions.OPTION_WeavingScheme, WeavingScheme.OTDRE.name());

			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			this.createFile(
				"/P/SuperFoo.java",
				"public team class SuperFoo {\n" +
				"	public abstract class Role {\n" +
				"		abstract void foo();\n" +
				"	}\n" +
				"}\n"
			);
			String sourceFoo = "public team class Foo extends SuperFoo {\n" +
			"	public class Role playedBy String {\n" +
			"		void foo() -> int length();\n" +
			"	}\n" +
			"}\n";
			this.createFile(
					"/P/Foo.java",
					sourceFoo
			);
			String sourceSubFoo = "public team class SubFoo extends Foo {\n" +
			"	public class Role {}\n" +
			"}\n";
			this.createFile(
					"/P/SubFoo.java",
					sourceSubFoo
			);

			char[] sourceChars = sourceSubFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);


			getCompilationUnit("/P/SubFoo.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n"
			);
		} finally {
			deleteProject("P");
		}
	}


	// http://trac.objectteams.org/ot/ticket/248
	public void testTrac248() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			// ROFI will be pulled in via CompilationUnitProblemFinder.accept -> SourceTypeConverter.buildCompilationUnit
			this.createFolder("/P/src/FooTeam");
			this.createFile(
					"/P/src/FooTeam/R.java",
					"team package FooTeam;\n" +
					"protected class R {\n" +
					"    private static int getI() {\n" +
					"        return 3;\n" +
					"    }\n" +
					"    protected int test() {\n" +
					"        return getI();\n" +
					"    }\n" +
					"}\n");

			String sourceTeam =
				"public team class FooTeam {\n" +
				"   R r;\n" +
				"}\n";
			this.createFile(
					"/P/src/FooTeam.java",
					sourceTeam
			);

			char[] sourceChars = sourceTeam.toCharArray();
			this.problemRequestor.initialize(sourceChars);


			getCompilationUnit("/P/src/FooTeam.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n"
			);
		} finally {
			deleteProject("P");
		}
	}

	// http://trac.objectteams.org/ot/ticket/259
	public void testTrac259() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			this.createFile(
				"/P/MyBase.java",
				"public class MyBase {\n" +
				"	private void secretDo() {\n" +
				"	}\n" +
				"}\n"
			);
			this.createFolder("/P/Foo");
			this.createFile("/P/Foo/Role.java",
					"team package Foo;\n" +
					"protected class Role playedBy MyBase {\n" +
					"	protected void doit() -> void secretDo();\n" +
					"}\n");
			String sourceFoo = "public team class Foo {\n" +
			"	protected void test(Role r) {\n" +
			" 		r.doit();\n" +
			"	}\n" +
			"}\n";
			this.createFile(
					"/P/Foo.java",
					sourceFoo
			);

			char[] sourceChars = sourceFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);


			getCompilationUnit("/P/Foo.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n"
			);
		} finally {
			deleteProject("P");
		}
	}

	// previously a syntax error could cause an IllegalArgumentException, see r19238 ff.
	public void testSyntaxError() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			this.createFile(
				"/P/Bar.java",
				"public class Bar {\n" +
				"   String baz() { return null;  }\n" +
				"}\n"
			);
			String sourceFoo = "public team class Foo  {\n" +
			"	public class Role playedBy String \n" +
			"       base when (base.){\n" +
			"       String getFoo() -> get String foo\n" +
			"         with { result <- (String)foo }\n" +
			"   }\n" +
			"   void doodle() {}" +
			"}\n";
			this.createFile(
					"/P/Foo.java",
					sourceFoo
			);
			String sourceMain = "public class Main {\n" +
					"void zork(Foo f) {\n" +
					"    f.doodle();\n" +
					"}\n" +
					"}";
			this.createFile(
					"/P/Main.java",
					sourceMain
			);

			char[] sourceChars = sourceMain.toCharArray();
			this.problemRequestor.initialize(sourceChars);


			getCompilationUnit("/P/Main.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n"
			);
		} finally {
			deleteProject("P");
		}
	}

	@SuppressWarnings("unchecked") // options in a raw map
	public void testLocalInRoFi() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			// set compliance to 1.6 to force generated of a stack map attribute.
			Map options = p.getOptions(true);
			options.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_6);
			options.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_6);
			options.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_6);
			p.setOptions(options);
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			this.createFile(
				"/P/Plain.java",
				"public class Plain {\n" +
				"    public Object getVal() {\n" +
				"        return \"Plain\";\n" +
				"    }\n" +
				"}\n"
			);
			String sourceTeam = "public team class Foo {\n" +
				"    String foo, blub, bar, dings, wurgs, zork;\n" +
				"    public static void main(String[] args) {\n" +
				"        new Foo().new Role().test();\n" +
				"    }\n" +
				"}\n";
			this.createFile(
					"/P/Foo.java",
					sourceTeam
			);
			this.createFolder("/P/Foo");

			String sourceRole1= "team package Foo;\n" +
				"public class Role1 {\n" +
				"	String s1, s2, s3, s4, s5, s6;\n" +
				"}\n";
			this.createFile(
					"/P/Foo/Role1.java",
					sourceRole1);

			String sourceRole2 = "team package Foo;\n" +
				"public class Role2 extends Role1 {\n" +
				"        protected void test() {\n" +
				"            Plain isub = new Plain() {\n" +
				"                public Object getVal() {\n" +
				"                    Object edits = super.getVal();\n" +
				"                    if (edits instanceof String) {\n" +
				"                        String string = (String)edits;\n" +
				"                        @SuppressWarnings(\"unused\")\n" +
				"                        int l = string.length();\n" +
				"                    }\n" +
				"                    return edits;\n" +
				"                }\n" +
				"            };\n" +
				"            @SuppressWarnings(\"unused\")\n" +
				"            Object v = isub.getVal();\n" +
				"        }\n" +
				"}\n";
			this.createFile(
					"/P/Foo/Role2.java",
					sourceRole2
			);

			char[] sourceChars = sourceRole2.toCharArray();
			this.problemRequestor.initialize(sourceChars);


			ICompilationUnit wc = getCompilationUnit("/P/Foo/Role2.java").getWorkingCopy(this.wcOwner, null);
			wc.reconcile(AST.getJLSLatest(),
						 ICompilationUnit.FORCE_PROBLEM_DETECTION|ICompilationUnit.ENABLE_STATEMENTS_RECOVERY|ICompilationUnit.ENABLE_BINDINGS_RECOVERY,
						 wc.getOwner(), null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n" +
				"----------\n" +
				"----------\n"
			);
		} finally {
			deleteProject("P");
		}
	}


	@SuppressWarnings("unchecked") // options in a raw map
	public void testRemoveRole() throws CoreException, InterruptedException, UnsupportedEncodingException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			// set compliance to 1.6 to force generated of a stack map attribute.
			Map options = p.getOptions(true);
			options.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_6);
			options.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_6);
			options.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_6);
			p.setOptions(options);
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);


			String sourceTeam = "public team class Foo {\n" +
				"	public class Role1 {\n" +
				"		String s1, s2, s3, s4, s5, s6;\n" +
				"	}\n" +
				"   String foo, blub, bar, dings, wurgs, zork;\n" +
				"   public static void main(String[] args) {\n" +
				"       new Foo().new Role1().test();\n" +
				"   }\n" +
				"}\n";
			this.createFile(
					"/P/Foo.java",
					sourceTeam
			);

			this.problemRequestor.initialize(sourceTeam.toCharArray());

			ICompilationUnit wc = getCompilationUnit("/P/Foo.java").getWorkingCopy(this.wcOwner, null);
			IType itype = wc.getType("Foo");
			IOTType ottype = OTModelManager.getOTElement(itype);
			IType rt = ottype.getRoleType("Role1");
			IOTType roleType = OTModelManager.getOTElement(rt); // this caches the role type
			assertTrue ("Role should initially exist", rt.exists());
			assertTrue ("RoleType should initially exist", roleType.exists());

			// delete Role1
			wc.applyTextEdit(new DeleteEdit(
					("public team class Foo {\n").length(),
					("	public class Role1 {\n" +
							"		String s1, s2, s3, s4, s5, s6;\n" +
					"	}\n").length()),
					null);
			wc.reconcile(AST.getJLSLatest(),
					ICompilationUnit.FORCE_PROBLEM_DETECTION|ICompilationUnit.ENABLE_STATEMENTS_RECOVERY|ICompilationUnit.ENABLE_BINDINGS_RECOVERY,
					wc.getOwner(), null);

			// watch that
			// ... the JavaElement no longer exists:
			rt = itype.getType("Role1");
			assertFalse ("Role should no longer exist", rt.exists());
			// ... and OTModel no longer has a RoleType:
			ottype = OTModelManager.getOTElement(itype);
			rt = ottype.getRoleType("Role1");
			assertNull("Role should be null", rt);
		} finally {
			deleteProject("P");
		}
	}

	public void testAnchoredType01() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			//prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			this.createFile(
				"/P/BaseTeam.java",
				"public team class BaseTeam {\n" +
				"	public class Role {}\n" +
				"}\n"
			);

			String sourceFoo = "public team class Foo {\n" +
					"   protected team class Mid playedBy BaseTeam {\n" +
					"		public class Inner1 playedBy Role<@Mid.base> {}\n" +
					"		protected class Inner2 playedBy Role<@base> {}\n" +
					"		@SuppressWarnings(\"roletypesyntax\")\n" +
					"		protected class Inner3 playedBy base.Role {}\n" +
					"	}\n" +
					"}\n";
			this.createFile(
					"/P/Foo.java",
					sourceFoo
			);

			char[] sourceChars = sourceFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);

			ICompilationUnit fooWC = getCompilationUnit("/P/Foo.java").getWorkingCopy(this.wcOwner, null);
			IType foo =  fooWC.getType("Foo");

			CompilerOptions compilerOptions = new CompilerOptions(p.getOptions(true));
			ProblemReporter problemReporter = new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(),
					compilerOptions,
					new DefaultProblemFactory());

			// force usage of type converter:
			CompilationUnitDeclaration parsedUnit =
				SourceTypeConverter.buildCompilationUnit(
						new ISourceType[] {(ISourceType) ((SourceType)foo).getElementInfo()},
						SourceTypeConverter.FIELD_AND_METHOD | SourceTypeConverter.MEMBER_TYPE,
						problemReporter,
						new CompilationResult("Foo.java".toCharArray(), 1, 1, 90));

			// force resolving:
			process(parsedUnit, p, compilerOptions, problemReporter, ITranslationStates.STATE_RESOLVED);

			// evaluate result:
			String result = "";
			CategorizedProblem[] problems = parsedUnit.compilationResult().problems;
			assertNotNull(problems);
			for (IProblem problem : problems)
				if (problem != null && problem.isError())
					result += problem;
			assertEquals("", result);
		} finally {
			deleteProject("P");
		}
	}

	public void testAnchoredType02() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			//prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			this.createFile(
				"/P/BaseTeam.java",
				"public team class BaseTeam {\n" +
				"	public class Role {}\n" +
				"   Role baseField;" +
				"}\n"
			);

			String sourceFoo = "public team class Foo {\n" +
					"   protected team class Mid playedBy BaseTeam {\n" +
					"		public class Inner playedBy Role<@Mid.base> {}\n" +
					"       Inner get1() -> get Role<@Mid.base> baseField;\n" +
					"       Inner get2() -> get Role<@base> baseField;\n" +
					"		@SuppressWarnings(\"roletypesyntax\")\n" +
					"       Inner get3() -> get base.Role baseField;\n" +
					"	}\n" +
					"}\n";
			this.createFile(
					"/P/Foo.java",
					sourceFoo
			);

			char[] sourceChars = sourceFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);

			ICompilationUnit fooWC = getCompilationUnit("/P/Foo.java").getWorkingCopy(this.wcOwner, null);
			IType foo =  fooWC.getType("Foo");

			CompilerOptions compilerOptions = new CompilerOptions(p.getOptions(true));
			ProblemReporter problemReporter = new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(),
					compilerOptions,
					new DefaultProblemFactory());

			// force usage of type converter:
			CompilationUnitDeclaration parsedUnit =
				SourceTypeConverter.buildCompilationUnit(
						new ISourceType[] {(ISourceType) ((SourceType)foo).getElementInfo()},
						SourceTypeConverter.FIELD_AND_METHOD | SourceTypeConverter.MEMBER_TYPE,
						problemReporter,
						new CompilationResult("Foo.java".toCharArray(), 1, 1, 90));

			// force resolving:
			process(parsedUnit, p, compilerOptions, problemReporter, ITranslationStates.STATE_RESOLVED);

			// evaluate result:
			String result = "";
			CategorizedProblem[] problems = parsedUnit.compilationResult().problems;
			assertNotNull(problems);
			for (IProblem problem : problems)
				if (problem != null && problem.isError())
					result += problem;
			assertEquals("", result);
		} finally {
			deleteProject("P");
		}
	}

	class SourceTypeCompiler extends Compiler {
		public SourceTypeCompiler(INameEnvironment nameEnvironment,
				IErrorHandlingPolicy policy,
				CompilerOptions compilerOptions,
				IProblemFactory problemFactory)
		{
			super(nameEnvironment, policy, compilerOptions,
				  new ICompilerRequestor() { public void acceptResult(CompilationResult result) { } },
				  problemFactory);
		}

		// from CompilationUnitProblemFinder:
		public void accept(ISourceType[] sourceTypes, PackageBinding packageBinding, AccessRestriction accessRestriction) {
			// ensure to jump back to toplevel type for first one (could be a member)
			while (sourceTypes[0].getEnclosingType() != null) {
				sourceTypes[0] = sourceTypes[0].getEnclosingType();
			}

			CompilationResult result =
				new CompilationResult(sourceTypes[0].getFileName(), 1, 1, this.options.maxProblemsPerUnit);

			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=305259, build the compilation unit in its own sand box.
			final long savedComplianceLevel = this.options.complianceLevel;
			final long savedSourceLevel = this.options.sourceLevel;

			LookupEnvironment environment = packageBinding.environment;
			if (environment == null)
				environment = this.lookupEnvironment;

			try {
				IJavaProject project = ((SourceTypeElementInfo) sourceTypes[0]).getHandle().getJavaProject();
				this.options.complianceLevel = CompilerOptions.versionToJdkLevel(project.getOption(JavaCore.COMPILER_COMPLIANCE, true));
				this.options.sourceLevel = CompilerOptions.versionToJdkLevel(project.getOption(JavaCore.COMPILER_SOURCE, true));

				// need to hold onto this
				CompilationUnitDeclaration unit =
					SourceTypeConverter.buildCompilationUnit(
							sourceTypes,//sourceTypes[0] is always toplevel here
							SourceTypeConverter.FIELD_AND_METHOD // need field and methods
							| SourceTypeConverter.MEMBER_TYPE // need member types
							| SourceTypeConverter.FIELD_INITIALIZATION, // need field initialization
							environment.problemReporter,
							result);

				if (unit != null) {
	//{ObjectTeams: controlled by Dependencies:
				  try (Config config = Dependencies.setup(this, this.parser, environment, true, false))
				  {
	// orig:  Note(SH): this will redirect:
					environment.buildTypeBindings(unit, accessRestriction);
					environment.completeTypeBindings(unit);
	// :giro
				  }
	// SH}
				}
			} finally {
				this.options.complianceLevel = savedComplianceLevel;
				this.options.sourceLevel = savedSourceLevel;
			}
		}
	}
	private void process(CompilationUnitDeclaration parsedUnit, IJavaProject p,
			CompilerOptions compilerOptions, ProblemReporter problemReporter, int state)
			throws JavaModelException {
		Parser parser = new Parser(problemReporter, false);
		INameEnvironment nameEnvironment = new SearchableEnvironment((JavaProject)p, this.wcOwner, false);
		Compiler compiler = new SourceTypeCompiler(
					nameEnvironment,
					DefaultErrorHandlingPolicies.proceedWithAllProblems(),
					compilerOptions,
					problemReporter.problemFactory);

		try (Config config = Dependencies.setup(this, parser, compiler.lookupEnvironment, true, false))
		{
			Dependencies.ensureState(parsedUnit, state);
		}
	}

	public void testAnchoredType03() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			//prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			this.createFile(
				"/P/BaseTeam.java",
				"public team class BaseTeam {\n" +
				"	public class Role {}\n" +
				"}\n"
			);
			String sourceFoo = "public team class Foo {\n" +
					"   protected team class Mid playedBy BaseTeam {\n" +
					"		public class Inner1 playedBy Role<@Mid.base> {}\n" +
					"		protected class Inner2 playedBy Role<@base> {}\n" +
					"		@SuppressWarnings(\"roletypesyntax\")\n" +
					"		protected class Inner3 playedBy base.Role {}\n" +
					"	}\n" +
					"}\n";
			this.createFile(
					"/P/Foo.java",
					sourceFoo
			);

			char[] sourceChars = sourceFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);

			this.createFile(
					"/P/Main.java",
					"public team class Main extends Foo {\n" +
					"	public team class Mid {\n" +
					"		public class Inner1 {}\n" +
					"		public class Inner2 {}\n" +
					"		public class Inner3 {}\n" +
					"	}\n" +
					"}\n"
			);

			getCompilationUnit("/P/Main.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n"
			);
		} finally {
			deleteProject("P");
		}
	}

	//  Bug 316658 -  [reconciler] implicitly inherited field reports "illegal modifier"
    public void testImplicitlyInheritedInitializedField1() throws CoreException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			//prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);
			this.createFile(
				"/P/MyTeam.java",
	    			"public team class MyTeam {\n" +
	    			"	protected class R {\n" +
	    			"		final String val= new String(\"OK\");\n" +
	    			"	}\n" +
	    			"}\n");

			String sourceString = "public team class MySubTeam extends MyTeam {\n" +
			"	protected class R {\n" +
			"		protected R() { super(); }\n" +
			"		protected void test() {\n" +
			"			//nop\n" +
			"		}\n" +
			"	}\n" +
			"	public void test() {\n" +
			"		new R().test();\n" +
			"	}\n" +
			"	public static void main(String... args) {\n" +
			"		new MySubTeam().test();\n" +
			"	}\n" +
			"}\n";
			this.createFile(
				"/P/MySubTeam.java",
	    			sourceString);

			char[] sourceChars = sourceString.toCharArray();
			this.problemRequestor.initialize(sourceChars);

			getCompilationUnit("/P/MySubTeam.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n");

    	} finally {
    		deleteProject("P");
    	}
    }

    // Bug 321352 -  [compiler][reconciler] reporting of non-externalized string constants in role files
    //   should report as error
    public void testNLSinRoFi1() throws CoreException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaCore.ERROR);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);
			this.createFolder("/P/MyTeam");
			String roleSourceString =
				"team package MyTeam;\n" +
				"public class Role {\n" +
				"	void foo() {\n" +
				"		String val= \"OK\";\n" +
				"	}\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam/Role.java",
	    			roleSourceString);

			String teamSourceString =
				"public team class  MyTeam {\n" +
				"	Role r;\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam.java",
	    			teamSourceString);

			char[] roleSourceChars = roleSourceString.toCharArray();
			this.problemRequestor.initialize(roleSourceChars);

			getCompilationUnit("/P/MyTeam/Role.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"1. ERROR in /P/MyTeam/Role.java (at line 4)\n" +
				"	String val= \"OK\";\n" +
				"	            ^^^^\n" +
				"Non-externalized string literal; it should be followed by //$NON-NLS-<n>$\n" +
				"----------\n");

    	} finally {
    		deleteProject("P");
    	}
    }

    // Bug 321352 -  [compiler][reconciler] reporting of non-externalized string constants in role files
    //   using $NON-NLS-<n>$
    public void testNLSinRoFi2() throws CoreException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaCore.ERROR);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);
			this.createFolder("/P/MyTeam");
			String roleSourceString =
				"team package MyTeam;\n" +
				"public class Role {\n" +
				"	void foo() {\n" +
				"		String val= \"OK\"; //$NON-NLS-1$\n" +
				"	}\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam/Role.java",
	    			roleSourceString);

			String teamSourceString =
				"public team class  MyTeam {\n" +
				"	Role r;\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam.java",
	    			teamSourceString);

			char[] roleSourceChars = roleSourceString.toCharArray();
			this.problemRequestor.initialize(roleSourceChars);

			getCompilationUnit("/P/MyTeam/Role.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n");

    	} finally {
    		deleteProject("P");
    	}
    }

    // a role file holds a nested team which extends a non-team role file
    // Bug 324526 -  [reconciler] NPE during AST creation, team in role file subclassing non-team role
    public void testRoFiNestedTeam() throws CoreException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaCore.ERROR);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);
			this.createFolder("/P/MyTeam");
			String role1SourceString =
				"team package MyTeam;\n" +
				"public class Role1 {\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam/Role1.java",
	    			role1SourceString);
			String role2SourceString =
				"team package MyTeam;\n" +
				"public team class Role2 extends Role1 {\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam/Role2.java",
	    			role2SourceString);

			String teamSourceString =
				"public team class  MyTeam {\n" +
				"	Role1 r;\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam.java",
	    			teamSourceString);

			char[] role2SourceChars = role2SourceString.toCharArray();
			this.problemRequestor.initialize(role2SourceChars);

			getCompilationUnit("/P/MyTeam/Role2.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n");
    	} finally {
    		deleteProject("P");
    	}
    }

    // a role file holds a nested team which extends a non-team role file
    // real life witness for  Bug 325681 -  [compiler] syntax error in role file may case NPE in RoleModel.getTeamModel()
    public void testRoFiNestedTeam_SyntaxError() throws CoreException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaCore.ERROR);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);
			this.createFile(
					"/P/JavaLinkedModeProposal.java",
		    		"public class JavaLinkedModeProposal {\n" +
		    		"    String baseMethod() { return null; }\n" +
		    		"}\n");
			this.createFolder("/P/MyTeam");
			String role1SourceString =
				"team package MyTeam;\n" +
				"public class Role1 {\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam/Role1.java",
	    			role1SourceString);
			String role2SourceString =

				"team package MyTeam;\n" +
				"\n" +
				"import static org.eclipse.objectteams.otdt.ui.ImageConstants.CALLINBINDING_AFTER_IMG;\n" +
				"import static org.eclipse.objectteams.otdt.ui.ImageConstants.CALLINBINDING_BEFORE_IMG;\n" +
				"import static org.eclipse.objectteams.otdt.ui.ImageConstants.CALLINBINDING_REPLACE_IMG;\n" +
				"import static org.eclipse.objectteams.otdt.ui.ImageConstants.CALLOUTBINDING_IMG;\n" +
				"\n" +
				"import java.util.List;\n" +
				"\n" +
				"import org.eclipse.core.runtime.CoreException;\n" +
				"import org.eclipse.jdt.core.CompletionProposal;\n" +
				"import org.eclipse.jdt.core.ICompilationUnit;\n" +
				"import org.eclipse.jdt.core.IJavaProject;\n" +
				"import org.eclipse.jdt.core.JavaModelException;\n" +
				"import org.eclipse.jdt.core.dom.AST;\n" +
				"import org.eclipse.jdt.core.dom.ASTNode;\n" +
				"import org.eclipse.jdt.core.dom.AbstractMethodMappingDeclaration;\n" +
				"import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;\n" +
				"import org.eclipse.jdt.core.dom.IMethodBinding;\n" +
				"import org.eclipse.jdt.core.dom.ITypeBinding;\n" +
				"import org.eclipse.jdt.core.dom.MethodBindingOperator;\n" +
				"import org.eclipse.jdt.core.dom.MethodSpec;\n" +
				"import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;\n" +
				"import org.eclipse.jdt.core.dom.SingleVariableDeclaration;\n" +
				"import org.eclipse.jdt.core.dom.Type;\n" +
				"import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;\n" +
				"import org.eclipse.jdt.core.dom.rewrite.ITrackedNodePosition;\n" +
				"import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;\n" +
				"import org.eclipse.jdt.internal.corext.codemanipulation.CodeGenerationSettings;\n" +
				"import org.eclipse.jdt.internal.corext.fix.LinkedProposalPositionGroup;\n" +
				"import org.eclipse.jdt.internal.corext.fix.LinkedProposalPositionGroup.Proposal;\n" +
				"import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;\n" +
				"import org.eclipse.jface.text.link.LinkedModeModel;\n" +
				"import org.eclipse.jface.text.link.LinkedPosition;\n" +
				"import org.eclipse.objectteams.otdt.internal.ui.util.Images;\n" +
				"import org.eclipse.objectteams.otdt.internal.ui.util.OTStubUtility;\n" +
				"import org.eclipse.swt.graphics.Image;\n" +
				"import org.eclipse.text.edits.DeleteEdit;\n" +
				"import org.eclipse.text.edits.MultiTextEdit;\n" +
				"import org.eclipse.text.edits.ReplaceEdit;\n" +
				"import org.eclipse.text.edits.TextEdit;\n" +
				"\n" +
				"/** \n" +
				" */ \n" +
				"@SuppressWarnings(\"restriction\")\n" +
				"protected team class CreateMethodMappingCompletionProposal extends MethodMappingCompletionProposal \n" +
				"{\n" +
				"\n" +
				"	/* gateway to private final base class. */\n" +
				"	@SuppressWarnings(\"decapsulation\")\n" +
				"	protected class MyJavaLinkedModeProposal playedBy JavaLinkedModeProposal  {\n" +
				"\n" +
				"		public MyJavaLinkedModeProposal(ICompilationUnit unit, ITypeBinding typeProposal, int relevance) {\n" +
				"			base(unit, typeProposal, relevance);\n" +
				"		}\n" +
				"		TextEdit computeEdits(int offset, LinkedPosition position, char trigger, int stateMask, LinkedModeModel model) \n" +
				"		-> TextEdit computeEdits(int offset, LinkedPosition position, char trigger, int stateMask, LinkedModeModel model);\n" +
				"	}\n" +
				"\n" +
				"\n" +
				"\n" +
				"	boolean fIsOverride = false;\n" +
				"	boolean fIsOnlyCallin = false; \n" +
				"	\n" +
				"	protected CreateMethodMappingCompletionProposal(IJavaProject 	   jProject, \n" +
				"												    ICompilationUnit   cu,\n" +
				"												    CompletionProposal proposal,\n" +
				"												    String[]           paramTypes,\n" +
				"												    boolean 		   isOverride,\n" +
				"												    boolean 		   isOnlyCallin,\n" +
				"												    int                length,\n" +
				"												    String             displayName, \n" +
				"												    Image              image)\n" +
				"	{\n" +
				"		super(jProject, cu, proposal, paramTypes, length, displayName, image);\n" +
				"		this.fIsOverride= isOverride;\n" +
				"		this.fIsOnlyCallin = isOnlyCallin;\n" +
				"	}\n" +
				"	protected CreateMethodMappingCompletionProposal(IJavaProject 	   jProject, \n" +
				"									    ICompilationUnit   cu,  \n" +
				"									    CompletionProposal proposal,\n" +
				"									    int                length,\n" +
				"									    String             displayName,\n" +
				"									    Image              image) \n" +
				"	{\n" +
				"		super(jProject, cu, proposal, length, displayName, image);\n" +
				"	}\n" +
				"	\n" +
				"	/** Create a rewrite that additionally removes typed fragment if needed. \n" +
				"     *  That fragment will not be represented by an AST-node, that could be removed.\n" +
				"     */\n" +
				"	ASTRewrite createRewrite(AST ast) \n" +
				"	{\n" +
				"		if (fLength == 0)\n" +
				"			return ASTRewrite.create(ast);\n" +
				"		\n" +
				"		// the typed prefix will have to be deleted:\n" +
				"		final TextEdit delete= new DeleteEdit(fReplaceStart, fLength);\n" +
				"		\n" +
				"		// return a custom rewrite that additionally deletes typed fragment\n" +
				"		return new ASTRewrite(ast) {\n" +
				"			@Override\n" +
				"			public TextEdit rewriteAST() \n" +
				"					throws JavaModelException, IllegalArgumentException \n" +
				"			{\n" +
				"				TextEdit edits = super.rewriteAST();\n" +
				"				if (edits instanceof MultiTextEdit) {\n" +
				"					MultiTextEdit multi = (MultiTextEdit) edits;\n" +
				"					multi.addChild(delete);\n" +
				"				}\n" +
				"				return edits;\n" +
				"			}\n" +
				"		};\n" +
				"	}\n" +
				"	\n" +
				"	/** Overridable, see CalloutToFieldCompletionProposal.\n" +
				"	 *  At least baseBinding must be set, roleBinding is optional.\n" +
				"	 */\n" +
				"	boolean setupRewrite(ICompilationUnit                 iCU, \n" +
				"			          ASTRewrite       				   rewrite, \n" +
				"			          ImportRewrite   			       importRewrite,\n" +
				"			          ITypeBinding					   roleBinding,\n" +
				"			          ITypeBinding					   baseBinding,\n" +
				"			          ASTNode                          type,\n" +
				"			          AbstractMethodMappingDeclaration partialMapping,\n" +
				"			          ChildListPropertyDescriptor      bodyProperty) \n" +
				"			throws CoreException\n" +
				"	{\n" +
				"		// find base method:\n" +
				"		IMethodBinding method= findMethod(baseBinding, fMethodName, fParamTypes);\n" +
				"		if (method == null)\n" +
				"			return false;\n" +
				"		\n" +
				"		CodeGenerationSettings settings= JavaPreferencesSettings.getCodeGenerationSettings(fJavaProject);\n" +
				"		// create callout:\n" +
				"		AbstractMethodMappingDeclaration stub= this.fIsOnlyCallin \n" +
				"				? OTStubUtility.createCallin(iCU, rewrite, importRewrite,\n" +
				"						 				    method, baseBinding.getName(), ModifierKeyword.BEFORE_KEYWORD, settings)\n" +
				"				: OTStubUtility.createCallout(iCU, rewrite, importRewrite,\n" +
				"											 method, baseBinding.getName(), settings);\n" +
				"		if (stub != null) {\n" +
				"			insertStub(rewrite, type, bodyProperty, fReplaceStart, stub);\n" +
				"			\n" +
				"			MethodSpec roleMethodSpec = (MethodSpec)stub.getRoleMappingElement();\n" +
				"			\n" +
				"			// return type:\n" +
				"			ITrackedNodePosition returnTypePosition = null;\n" +
				"			ITypeBinding returnType = method.getReturnType();\n" +
				"			if (!(returnType.isPrimitive() && \"void\".equals(returnType.getName()))) {\n" +
				"				returnTypePosition = rewrite.track(roleMethodSpec.getReturnType2());\n" +
				"				addLinkedPosition(returnTypePosition, true, ROLEMETHODRETURN_KEY);\n" +
				"				LinkedProposalPositionGroup group1 = getLinkedProposalModel().getPositionGroup(ROLEMETHODRETURN_KEY, true);\n" +
				"				group1.addProposal(new MyJavaLinkedModeProposal(iCU, method.getReturnType(), 13)); //$NON-NLS-1$\n" +
				"				group1.addProposal(\"void\", null, 13); //$NON-NLS-1$\n" +
				"			}\n" +
				"			\n" +
				"			// role method name:\n" +
				"			addLinkedPosition(rewrite.track(roleMethodSpec.getName()), false, ROLEMETHODNAME_KEY);\n" +
				"			\n" +
				"			// argument lifting?\n" +
				"			if (roleBinding != null)\n" +
				"				addLiftingProposals(roleBinding, method, stub, rewrite);\n" +
				"			\n" +
				"			// binding operator:\n" +
				"			addLinkedPosition(rewrite.track(stub.bindingOperator()), false, BINDINGKIND_KEY);\n" +
				"			LinkedProposalPositionGroup group2= getLinkedProposalModel().getPositionGroup(BINDINGKIND_KEY, true);\n" +
				"			if (!this.fIsOnlyCallin) {\n" +
				"				String calloutToken = \"->\";\n" +
				"				if (this.fIsOverride) {\n" +
				"					calloutToken = \"=>\";\n" +
				"					stub.bindingOperator().setBindingKind(MethodBindingOperator.KIND_CALLOUT_OVERRIDE);\n" +
				"				}\n" +
				"				group2.addProposal(calloutToken, Images.getImage(CALLOUTBINDING_IMG), 13);         //$NON-NLS-1$\n" +
				"			}\n" +
				"			group2.addProposal(makeBeforeAfterBindingProposal(\"<- before\", Images.getImage(CALLINBINDING_BEFORE_IMG), returnTypePosition));  //$NON-NLS-1$\n" +
				"			group2.addProposal(\"<- replace\", Images.getImage(CALLINBINDING_REPLACE_IMG), 13); //$NON-NLS-1$\n" +
				"			group2.addProposal(makeBeforeAfterBindingProposal(\"<- after\",  Images.getImage(CALLINBINDING_AFTER_IMG), returnTypePosition));   //$NON-NLS-1$\n" +
				"		}\n" +
				"		return true;	\n" +
				"	}\n" +
				"	/** Create a method-binding proposal that, when applied, will change the role-returntype to \"void\": */\n" +
				"	Proposal makeBeforeAfterBindingProposal(String displayString, Image image, final ITrackedNodePosition returnTypePosition) {\n" +
				"		return new Proposal(displayString, image, 13) {\n" +
				"			@Override\n" +
				"			public TextEdit computeEdits(int offset, LinkedPosition position, char trigger, int stateMask, LinkedModeModel model)\n" +
				"					throws CoreException \n" +
				"			{\n" +
				"				MultiTextEdit edits = new MultiTextEdit();\n" +
				"				if (returnTypePosition != null)\n" +
				"					edits.addChild(new ReplaceEdit(returnTypePosition.getStartPosition(), returnTypePosition.getLength(), \"void\"));\n" +
				"				edits.addChild(super.computeEdits(offset, position, trigger, stateMask, model));\n" +
				"				return edits;\n" +
				"			}\n" +
				"		};\n" +
				"	}\n" +
				"	\n" +
				"	/** Check if any parameters or the return type are candidates for lifting/lowering. */\n" +
				"	@SuppressWarnings(\"rawtypes\")\n" +
				"	private void addLiftingProposals(ITypeBinding roleTypeBinding, IMethodBinding methodBinding,\n" +
				"			AbstractMethodMappingDeclaration stub, ASTRewrite rewrite) \n" +
				"	{\n" +
				"		ITypeBinding[] roles= roleTypeBinding.getDeclaringClass().getDeclaredTypes();\n" +
				"		MethodSpec roleSpec= (MethodSpec)stub.getRoleMappingElement();\n" +
				"		List params= roleSpec.parameters();\n" +
				"		ITypeBinding[] paramTypes = methodBinding.getParameterTypes();\n" +
				"		for (int i= 0; i<params.size(); i++)\n" +
				"			addLiftingProposalGroup(rewrite, ROLEPARAM_KEY+i, roles, \n" +
				"							        ((SingleVariableDeclaration)params.get(i)).getType(), paramTypes[i]);\n" +
				"		addLiftingProposalGroup(rewrite, ROLEPARAM_KEY+\"return\", roles,  //$NON-NLS-1$\n" +
				"									roleSpec.getReturnType2(), methodBinding.getReturnType());\n" +
				"	}\n" +
				"	/**\n" +
				"	 * check whether a given type is played by a role from a given array and create a proposal group containing base and role type. \n" +
				"	 * @param rewrite\n" +
				"	 * @param positionGroupID \n" +
				"	 * @param roles       available roles in the enclosing team\n" +
				"	 * @param type        AST node to investigate\n" +
				"	 * @param typeBinding type binding of AST node to investigate\n" +
				"	 */\n" +
				"	void addLiftingProposalGroup(ASTRewrite rewrite, String positionGroupID, ITypeBinding[] roles, Type type, ITypeBinding typeBinding)\n" +
				"	{\n" +
				"		for (ITypeBinding roleBinding : roles) {\n" +
				"			if (roleBinding.isSynthRoleIfc()) continue; // synth ifcs would otherwise cause dupes\n" +
				"			if (typeBinding.equals(roleBinding.getBaseClass())) {\n" +
				"				ITrackedNodePosition argTypePos= rewrite.track(type);\n" +
				"				addLinkedPosition(argTypePos, true, positionGroupID);\n" +
				"				LinkedProposalPositionGroup group=\n" +
				"					getLinkedProposalModel().getPositionGroup(positionGroupID, true);\n" +
				"				group.addProposal(type.toString(), null, 13);\n" +
				"				group.addProposal(roleBinding.getName(), null, 13);\n" +
				"				break;\n" +
				"			}\n" +
				"		}		\n" +
				"	}\n" +
				"}";
			this.createFile(
				"/P/MyTeam/CreateMethodMappingCompletionProposal.java",
	    			role2SourceString);

			String teamSourceString =
				"public team class  MyTeam {\n" +
				"	CreateMethodMappingCompletionProposal r;\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam.java",
	    			teamSourceString);

			char[] role2SourceChars = role2SourceString.toCharArray();
			this.problemRequestor.initialize(role2SourceChars);

			ICompilationUnit cu = getCompilationUnit("/P/MyTeam/CreateMethodMappingCompletionProposal.java").getWorkingCopy(this.wcOwner, null);
			// inject an error at 'random' location
			cu.applyTextEdit(new InsertEdit(1000, "\t\t@"), null);
			IType r2 = cu.getType("CreateMethodMappingCompletionProposal");
			IType inner = r2.getType("MyJavaLinkedModeProposal");
			IRoleType innerRole = (IRoleType) OTModelManager.getOTElement(inner);
			IType base = innerRole.getBaseClass();
			assertTrue("base should not null", base != null);
			assertEquals("wrong name of baseclass", "JavaLinkedModeProposal", base.getElementName());
    	} finally {
    		deleteProject("P");
    	}
    }


    // Bug 351520 - Undefined getClass() for role in seperate role file
    public void testClassLiteralForRoFi() throws CoreException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaCore.ERROR);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);
			this.createFolder("/P/MyTeam");
			String roleSourceString =
				"team package MyTeam;\n" +
				"public class Role {\n" +
				"	void foo() {\n" +
				"		String val= \"OK\";\n" +
				"	}\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam/Role.java",
	    			roleSourceString);

			String teamSourceString =
				"public team class  MyTeam {\n" +
				"	final MyTeam other = new MyTeam();\n" +
				"   Class c = Role<@other>.class;\n" +
				"}\n";
			this.createFile(
				"/P/MyTeam.java",
	    			teamSourceString);

			char[] teamSourceChars = teamSourceString.toCharArray();
			this.problemRequestor.initialize(teamSourceChars);

			getCompilationUnit("/P/MyTeam.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"1. WARNING in /P/MyTeam.java (at line 3)\n" +
				"	Class c = Role<@other>.class;\n" +
				"	^^^^^\n" +
				"Class is a raw type. References to generic type Class<T> should be parameterized\n" +
				"----------\n");

    	} finally {
    		deleteProject("P");
    	}
    }

    // cf. Bug 337413 - [otjld][compiler] consider changing LiftingFailedException to a checked exception
    // error AmbiguousLiftingMayBreakClients was not shown in the editor.
    public void testSubTeamIntroducesBindingAmbiguity() throws CoreException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaCore.ERROR);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);
			String baseSourceString =
				"public class Base {\n" +
				"}\n";
			this.createFile(
				"/P/Base.java",
	    		baseSourceString);
			String superTeamSourceString =
				"public team class SuperTeam {\n" +
				"     protected class R0 playedBy Base {}\n" +
				"     protected class R1 extends R0 {}\n" +
				"     public void foo(Base as R0 bar) {}\n" +
				"}\n";
			this.createFile(
				"/P/SuperTeam.java",
    			superTeamSourceString);
			String subTeamSourceString =
				"public team class SubTeam extends SuperTeam {\n" +
				"     protected class R2 extends R0 {}\n" +
				"}\n";
			this.createFile(
				"/P/SubTeam.java",
    			subTeamSourceString);


			char[] subTeamSourceChars = subTeamSourceString.toCharArray();
			this.problemRequestor.initialize(subTeamSourceChars);

			getCompilationUnit("/P/SubTeam.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"1. WARNING in /P/SubTeam.java (at line 1)\n" +
				"	public team class SubTeam extends SuperTeam {\n" +
				"	                  ^^^^^^^\n" +
				"Potential ambiguity in role binding. The base \'Base\' is bound to the following roles: SubTeam.R1,SubTeam.R2 (OTJLD 2.3.4(a)).\n" +
				"----------\n" +
				"2. ERROR in /P/SubTeam.java (at line 1)\n" +
				"	public team class SubTeam extends SuperTeam {\n" +
				"	                  ^^^^^^^\n" +
				"Team introduces binding ambiguity for role R1<@tthis[SubTeam]>, which may break clients of the super team (OTJLD 2.3.5(d)).\n" +
				"----------\n" +
				"3. ERROR in /P/SubTeam.java (at line 1)\n" +
				"	public team class SubTeam extends SuperTeam {\n" +
				"	                  ^^^^^^^\n" +
				"Team introduces binding ambiguity for role R0<@tthis[SubTeam]>, which may break clients of the super team (OTJLD 2.3.5(d)).\n" +
				"----------\n");
    	} finally {
    		deleteProject("P");
    	}
    }

    public void testBug348574a() throws CoreException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);

			String superTeamSourceString =
				"public team class SuperTeam {\n" +
				"     protected abstract class R0 {\n" +
				"         abstract void foo();" +
				"         abstract static void fooStatic();" +
				"     }\n" +
				"}\n";
			this.createFile(
				"/P/SuperTeam.java",
    			superTeamSourceString);

			String subTeamSourceString =
				"public team class SubTeam extends SuperTeam {\n" +
				"     protected class R0 {}\n" +
				"}\n";
			this.createFile(
				"/P/SubTeam.java",
    			subTeamSourceString);


			char[] subTeamSourceChars = subTeamSourceString.toCharArray();
			this.problemRequestor.initialize(subTeamSourceChars);

			getCompilationUnit("/P/SubTeam.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"1. ERROR in /P/SubTeam.java (at line 2)\n" +
				"	protected class R0 {}\n" +
				"	                ^^\n" +
				"The abstract method foo in type R0 can only be defined by an abstract class\n" +
				"----------\n" +
				"2. ERROR in /P/SubTeam.java (at line 2)\n" +
				"	protected class R0 {}\n" +
				"	                ^^\n" +
				"The type SubTeam.R0 must implement the inherited abstract method SubTeam.R0.foo()\n" +
				"----------\n" +
				"3. ERROR in /P/SubTeam.java (at line 2)\n" +
				"	protected class R0 {}\n" +
				"	                ^^\n" +
				"The abstract method fooStatic in type R0 can only be defined by an abstract class\n" +
				"----------\n" +
				"4. ERROR in /P/SubTeam.java (at line 2)\n" +
				"	protected class R0 {}\n" +
				"	                ^^\n" +
				"The type SubTeam.R0 must implement the inherited abstract method SubTeam.R0.fooStatic()\n" +
				"----------\n");
    	} finally {
    		deleteProject("P");
    	}
    }

    // Bug 382188 - NPE in copyRole() when commenting out roles in a nested
    public void testEmptyNestedExternalTeam() throws CoreException, InterruptedException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);

			this.createFolder("/P/p");

			String superTeamSourceString =
				"package p;\n" +
				"public team class SuperTeam {\n" +
				"}\n";
			this.createFile(
				"/P/p/SuperTeam.java",
    			superTeamSourceString);

			String superMidString =
				"team package p.SuperTeam;\n" +
				"protected team class Mid {\n" +
				"    protected class Inner {}\n" +
				"}\n";
			this.createFolder(
				"/P/p/SuperTeam");
			this.createFile(
				"/P/p/SuperTeam/Mid.java",
				superMidString);

			String subTeamSourceString =
				"package p;\n" +
				"public team class SubTeam extends SuperTeam {\n" +
				"    protected class Mid2 {}\n" +
				"}\n";
			this.createFile(
				"/P/p/SubTeam.java",
    			subTeamSourceString);

			this.createFolder(
					"/P/p/SubTeam");
			String subMidCompleteSourceString =
				"team package p/SubTeam;\n" +
				"protected team class Mid {\n" +
				"    protected class Inner {}\n" +
				"}\n";
			this.createFile("/P/p/SubTeam/Mid.java", subMidCompleteSourceString);

			project.build(IncrementalProjectBuilder.FULL_BUILD, null);

			String subMidSourceString =
				"team package p.SubTeam;\n" +
				"protected team class Mid {\n" +
				"}\n";

			char[] subMidSourceChars = subMidSourceString.toCharArray();
			this.problemRequestor.initialize(subMidSourceChars);

			ICompilationUnit icu = getCompilationUnit("/P/p/SubTeam/Mid.java").getWorkingCopy(this.wcOwner, null);
			assertNoProblem(subMidSourceChars, icu);
    	} finally {
    		deleteProject("P");
    	}
    }

    // Bug 400360 - [reconciler] fails to resolve callout-to-field with path-anchored type
    public void testBug400360() throws CoreException, InterruptedException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);

			String allShapesSourceString =
					"public team class AllShapes {\n" +
		    		"\n" +
		    		"	public abstract class Connector { }\n" +
		    		"	public abstract class RectangularConnector extends Connector { }\n" +
		    		"}\n";
			this.createFile(
				"/P/AllShapes.java",
				allShapesSourceString);

			String chdSourceString =
					"public team class CompanyHierarchyDisplay {\n" +
		    		"    \n" +
		    		"	public final AllShapes _shapes = new AllShapes();\n" +
		    		"	\n" +
		    		"    public class Connection {\n" +
		    		"    	Connector<@_shapes> connShape;\n" +
		    		"    }\n" +
		    		"}\n";
			this.createFile(
				"/P/CompanyHierarchyDisplay.java",
    			chdSourceString);

			String versionASourceString =
					"public team class VersionA {\n" +
		    		"    private final CompanyHierarchyDisplay _chd;\n" +
		    		"    \n" +
		    		"    public VersionA(CompanyHierarchyDisplay chd) {\n" +
		    		"    	_chd = chd;\n" +
		    		"    }\n" +
		    		"    \n" +
		    		"    public class RectangularConnections playedBy Connection<@_chd> {\n" +
		    		"       final AllShapes _shapesX = _chd._shapes;\n" +
		    		"       @SuppressWarnings(\"decapsulation\")\n" +
		    		"		void setShape(RectangularConnector<@_shapesX> shape) -> set Connector<@_chd._shapes> connShape;\n" +
		    		"    }\n" +
		    		"}\n";
			this.createFile(
				"/P/VersionA.java",
				versionASourceString);

			char[] versionASourceChars = versionASourceString.toCharArray();
			this.problemRequestor.initialize(versionASourceChars);

			ICompilationUnit unit = getCompilationUnit("/P/VersionA.java").getWorkingCopy(this.wcOwner, null);

			assertNoProblem(versionASourceChars, unit);
    	} finally {
    		deleteProject("P");
    	}
    }

    // Bug 406603 - [reconciler] fails to decode team anchor reference if name contains a 'D'
    public void testDecodeTeamAnchor() throws CoreException, InterruptedException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaCore.ERROR);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);

			String teamSourceString =
				"public team class  DomainObject {\n" +
				"	public class Item{}\n" +
				"   public void processItem(Item it) {}\n" +
				"}\n";
			this.createFile(
				"/P/DomainObject.java",
	    			teamSourceString);

			this.createFile(
				"/P/ItemService.java",
				"public class ItemService<DomainObject theDO> {\n" +
				"     public void processItem(Item<@theDO> item) { }\n" +
				"}\n");

			project.build(IncrementalProjectBuilder.FULL_BUILD, null);

			String clientSourceString =
				"public class Client {\n" +
				"	void foo(final DomainObject theDO) {\n" +
				"		ItemService<@theDO> service = new ItemService<@theDO>();" +
				"		Item<@theDO> myItem = new Item<@theDO>();\n" +
				"		service.processItem(myItem);\n" +
				"	}\n" +
				"}\n";
			this.createFile(
				"/P/Client.java",
				clientSourceString);

			char[] clientSourceChars = clientSourceString.toCharArray();
			this.problemRequestor.initialize(clientSourceChars);

			ICompilationUnit unit = getCompilationUnit("/P/Client.java").getWorkingCopy(this.wcOwner, null);

			assertNoProblem(clientSourceChars, unit);

    	} finally {
    		deleteProject("P");
    	}
    }


    // static role method accesses enclosing team instance
    public void testStaticRoleMethod() throws CoreException, InterruptedException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"}, new String[] {"JCL17_LIB"}, "1.7", "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);

			this.createFolder("/P/src/p");
			String teamSourceString =
				"package p;\n" +
				"public team class MyTeam {\n" +
				"	String val;" +
				"}\n";
			this.createFile("P/src/p/MyTeam.java", teamSourceString);
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);

			this.createFolder("/P/src/p/MyTeam");
			String roleSourceString =
					"team package p.MyTeam;\n" +
					"protected class R {\n" +
					"    static String test() { /* missing return */ }\n" +
					"}\n";
			this.createFile("/P/src/p/MyTeam/R.java", roleSourceString);
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);

			this.workingCopy = getCompilationUnit("/P/src/p/MyTeam/R.java").getWorkingCopy(this.wcOwner, null);

			roleSourceString =
				"team package p.MyTeam;\n" +
				"protected class R {\n" +
				"   static String test(boolean b) {\n" +
				"		if (b)\n" +
				"			return MyTeam.this.val;\n" +
				"		return null;\n" +
				"	}\n" +
				"}\n";

			char[] roleSourceChars = roleSourceString.toCharArray();

			ICompilationUnit icu = getCompilationUnit("/P/src/p/MyTeam/R.java").getWorkingCopy(this.wcOwner, null);
			assertNoProblem(roleSourceChars, icu);
    	} finally {
    		deleteProject("P");
    	}
    }

    // Bug 466097: Converted ast of org.objectteams.Team has no _OT$- methods
    public void testSetExecutingCallin() throws CoreException, InterruptedException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"}, new String[] {"JCL17_LIB"}, "1.7", "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);

			this.createFolder("/P/src/org/objectteams");
			this.createFile("P/src/org/objectteams/Team.java",
					"package org.objectteams;\n" +
					"public class Team implements ITeam {\n" +
					"	public boolean _OT$setExecutingCallin(boolean newFlag) {\n" +
					"		boolean oldVal = _OT$isExecutingCallin;\n" +
					"		_OT$isExecutingCallin = newFlag;\n" +
					"		return oldVal;\n" +
					"	}\n" +
					"}\n");
			this.createFolder("/P/src/b");
			String b1SourceString =
				"package b;\n" +
				"public class B1 {\n" +
				"	void bm1() {};\n" +
				"}\n";
			this.createFile("P/src/b/B1.java", b1SourceString);

			String b2SourceString =
					"package b;\n" +
					"public class B2 {\n" +
					"	public void bm2() {};\n" +
					"}\n";
			this.createFile("P/src/b/B2.java", b2SourceString);

			this.createFolder("/P/src/p");
			String team1SourceString =
				"package p;\n" +
				"import base b.B1;\n" +
				"public team class MyTeam1 {\n" +
				"	protected class R1 playedBy B1 {\n" +
				"		void rm1() {}\n" +
				"		rm1 <- after bm1;\n" +
				"	}\n" +
				"}\n";
			this.createFile("P/src/p/MyTeam1.java", team1SourceString);
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);
			p.close();
			p.open(null);

			String team2SourceString =
					"package p;\n" +
					"import base b.B2;\n" +
					"public team class MyTeam2 extends MyTeam1 {\n" +
					"	protected class R2 playedBy B2 {\n" +
					"		callin void rm2() { base.rm2(); }\n" +
					"		rm2 <- replace bm2;\n" +
					"	}\n" +
					"}\n";
			this.createFile("/P/src/p/MyTeam2.java", team2SourceString);

			this.problemRequestor.initialize(team2SourceString.toCharArray());
			ICompilationUnit unit = getCompilationUnit("/P/src/p/MyTeam2.java").getWorkingCopy(this.wcOwner, null);
			assertNoProblem((team2SourceString).toCharArray(), unit);

    	} finally {
    		deleteProject("P");
    	}
    }

    public void testUnresolvedSuperTeam() throws CoreException, InterruptedException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"}, new String[] {"JCL17_LIB"}, "1.7", "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);

			project.build(IncrementalProjectBuilder.FULL_BUILD, null);

			this.createFolder("/P/src/p2");
			String team2SourceString =
					"package p2;\n" +
					"public team class MyTeam2 extends MyTeam1 {\n" +
					"	protected class R2 extends R1 {\n" +
					"		boolean test() {\n" +
					"			return getFlag();\n" +
					"		}\n" +
					"	}\n" +
					"}\n";
			this.createFile("/P/src/p2/MyTeam2.java", team2SourceString);

			this.problemRequestor.initialize(team2SourceString.toCharArray());
			getCompilationUnit("/P/src/p2/MyTeam2.java").getWorkingCopy(this.wcOwner, null);
			assertProblems("Expecting problems",
					"----------\n" +
					"1. ERROR in /P/src/p2/MyTeam2.java (at line 2)\n" +
					"	public team class MyTeam2 extends MyTeam1 {\n" +
					"	                                  ^^^^^^^\n" +
					"MyTeam1 cannot be resolved to a type\n" +
					"----------\n" +
					"2. ERROR in /P/src/p2/MyTeam2.java (at line 3)\n" +
					"	protected class R2 extends R1 {\n" +
					"	                ^^\n" +
					"The hierarchy of the type R2 is inconsistent\n" +
					"----------\n" +
					"3. ERROR in /P/src/p2/MyTeam2.java (at line 3)\n" +
					"	protected class R2 extends R1 {\n" +
					"	                           ^^\n" +
					"R1 cannot be resolved to a type\n" +
					"----------\n" +
					"4. ERROR in /P/src/p2/MyTeam2.java (at line 5)\n" +
					"	return getFlag();\n" +
					"	       ^^^^^^^\n" +
					"The method getFlag() is undefined for the type MyTeam2.R2\n" +
					"----------\n",
					this.problemRequestor);

    	} finally {
    		deleteProject("P");
    	}
    }

    public void testCalloutVisibility() throws CoreException, InterruptedException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"}, new String[] {"JCL17_LIB"}, "1.7", "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			OTREContainer.initializeOTJProject(project);

			this.createFolder("/P/src/b");
			String b1SourceString =
				"package b;\n" +
				"public class B1 {\n" +
    			"	int f0;\n" +
    			"	private int f1;\n" +
    			"	private int f2;\n" +
    			"	private int f3;\n" +
				"}\n";
			this.createFile("P/src/b/B1.java", b1SourceString);

			this.createFolder("/P/src/p");
			String team1SourceString =
				"package p;\n" +
				"import base b.B1;\n" +
    			"@SuppressWarnings(\"decapsulation\")\n" +
				"public team class MyTeam1 {\n" +
				"	protected class R playedBy B1 {\n" +
    			"		int getF0() -> get int f0;\n" + // package vis
    			"		int getF1() -> get int f1;\n" + // ERR: private from private base field
    			"		private int getF2() -> get int f2;\n" + // ERR: private
    			"		protected int getF3() -> get int f3;\n" + // protected
    			"\n" +
    			"		int m1() { return 1; }\n" +
    			"		private int m2() { return 2; }\n" +
    			"		protected int m3() { return 3; }\n" +
				"	}\n" +
				"}\n";
			this.createFile("P/src/p/MyTeam1.java", team1SourceString);
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);
			p.close();
			p.open(null);

			this.createFolder("/P/src/p2");
			String team2SourceString =
					"package p;\n" +
					"import p.MyTeam1;\n" +
					"public team class MyTeam2 extends MyTeam1 {\n" +
	    			"	@Override\n" +
	    			"	protected class R {\n" + // no probs in implicit inheritance
	    			"		int test1() {\n" +
	    			"			return getF0() +\n" +
	    			"					getF1() +\n" +
	    			"					getF2() +\n" +
	    			"					getF3();\n" +
	    			"		}\n" +
	    			"		int test2() {\n" +
	    			"			return m1() +\n" +
	    			"					m2() +\n" +
	    			"					m3();\n" +
	    			"		}\n" +
	    			"	}\n" +
	    			"	protected class R2 extends R {\n" +
	    			"		int test3() {\n" +
	    			"			return getF1() +\n" +
	    			"					getF2() +\n" +
	    			"					getF3();\n" +
	    			"		}\n" +
	    			"		int test4() {\n" +
	    			"			return m1() +\n" +
	    			"					m2() +\n" +
	    			"					m3();\n" +
	    			"		}\n" +
	    			"	}\n" +
					"}\n";
			String teamFilename = "/P/src/p/MyTeam2.java";
			this.createFile(teamFilename, team2SourceString);

			this.problemRequestor.initialize(team2SourceString.toCharArray());
			getCompilationUnit(teamFilename).getWorkingCopy(this.wcOwner, null);

			assertProblems("Expecting problems",
		    		"----------\n" +
    				"1. ERROR in /P/src/p/MyTeam2.java (at line 20)\n" +
    				"	return getF1() +\n" +
    				"	       ^^^^^\n" +
    				"The method getF1() from the role type MyTeam2.R is not visible (OTJLD 1.2.1(e)).\n" +
    				"----------\n" +
    				"2. ERROR in /P/src/p/MyTeam2.java (at line 21)\n" +
    				"	getF2() +\n" +
    				"	^^^^^\n" +
    				"The method getF2() from the role type MyTeam2.R is not visible (OTJLD 1.2.1(e)).\n" +
    				"----------\n" +
    				"3. ERROR in /P/src/p/MyTeam2.java (at line 26)\n" +
    				"	m2() +\n" +
    				"	^^\n" +
    				"The method m2() from the role type MyTeam2.R is not visible (OTJLD 1.2.1(e)).\n" +
    				"----------\n",
					this.problemRequestor);
    	} finally {
    		deleteProject("P");
    	}
    }

    public void testTeamInJar1() throws CoreException, InterruptedException, IOException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"},
					new String[] {"JCL18_FULL"}, "1.8", "bin", true/*fullJCL*/,
					CompilerOptions.WeavingScheme.OTRE.toString());
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			addOTLibrary(p, "teams.jar", "teamsSrc.zip", new String[] {
				"p/MyTeam.java",
				"package p;\n" +
				"public team class MyTeam {\n" +
				"	protected class MyR {\n" +
				"		void test() {}\n" +
				"	}\n" +
				"}\n"
			}, "1.8");

			this.createFolder("/P/src/p2");
			String subTeamSourceString =
				"package p2;\n" +
				"public team class SubTeam extends p.MyTeam {\n" +
    			"	@Override\n" +
    			"	protected class MyR {\n" +
    			"		void test2() {\n" +
    			"			test();\n" +
    			"		}\n" +
    			"	}\n" +
				"}\n";
			String teamFileName = "P/src/p2/SubTeam.java";
			this.createFile(teamFileName, subTeamSourceString);

			waitUntilIndexesReady();
			this.workingCopies = new ICompilationUnit[1];

			// Get first working copy and verify that there's no error
			this.problemRequestor.initialize(subTeamSourceString.toCharArray());
			this.workingCopies[0] = getCompilationUnit(teamFileName).getWorkingCopy(this.wcOwner, null);
			assertNoProblem(subTeamSourceString.toCharArray(), this.workingCopies[0]);

    	} finally {
    		deleteProject("P");
    	}
    }
    // Bug 501655 - [reconcile] base call in role file is marked as illegal
    public void testBaseCallinRoFi1() throws CoreException {
    	try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {""}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaCore.ERROR);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_LOCAL, JavaCore.IGNORE);

			createRuntimeStubs();
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);

			createFolder("/P/b");
			createFile("/P/b/MyBase.java",
				"public class MyBase {\n" +
				"	void m(Object o, String[] vals) throws Exception {}\n" +
				"}\n");
			createFolder("/P/MyTeam");
			String roleSourceString =
				"team package MyTeam;\n" +
				"public class Role playedBy MyBase {\n" +
				"	@SuppressWarnings(\"decapsulation\")\n" +
				"	foo <- replace m;\n" +
				"	callin void foo(Object o, String[] vals) throws Exception {\n" +
				"		base.foo(o, vals);\n" +
				"	}\n" +
				"}\n";

			createFile(
				"/P/MyTeam/Role.java",
	    			roleSourceString);

			String teamSourceString =
				"import base b.MyBase;\n" +
				"public team class  MyTeam {\n" +
				"	Role r;\n" +
				"}\n";
			createFile(
				"/P/MyTeam.java",
	    			teamSourceString);

			char[] roleSourceChars = roleSourceString.toCharArray();
			this.problemRequestor.initialize(roleSourceChars);

			getCompilationUnit("/P/MyTeam/Role.java").getWorkingCopy(this.wcOwner, null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n");

    	} finally {
    		deleteProject("P");
    	}
    }

	public void testBug545316a() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			//prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			createFolder("/P/src/pbase");
			createFile(
				"/P/src/pbase/Base.java",
				"package pbase;\n" +
				"public class Base {\n" +
				"	public void baseMethod(int flag, String name) {}\n" +
				"}\n"
			);

			String sourceFoo =
					"import base pbase.Base;\n" +
					"public team class Foo {\n" +
					"	@SuppressWarnings(\"unused\") private String v1;\n" +
					"	@SuppressWarnings(\"unused\") private String v2;\n" +
					"	@SuppressWarnings(\"unused\") private String v3;\n" +
					"	@SuppressWarnings(\"unused\") private String v4;\n" +
					"	@SuppressWarnings(\"unused\") private String v5;\n" +
					"	@SuppressWarnings(\"unused\") private String v6;\n" +
					"	@SuppressWarnings(\"unused\") private String v7;\n" +
					"	@SuppressWarnings(\"unused\") private String v8;\n" +
					"	@SuppressWarnings(\"unused\") private String v9;\n" +
					"	@SuppressWarnings(\"unused\") private String v10;\n" +
					"	@SuppressWarnings(\"unused\") private String v11;\n" +
					"   protected class R playedBy Base {\n" +
					"		void roleMethod(String onlyThis) {}\n" +
					"		void roleMethod(String onlyThis) <- after void baseMethod(int flag, String name)\n" +
					"			with { onlyThis <- name }\n" +
					"	}\n" +
					"}\n";
			this.createFile(
					"/P/src/Foo.java",
					sourceFoo
			);

			char[] sourceChars = sourceFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);

			ICompilationUnit fooWC = getCompilationUnit("/P/src/Foo.java").getWorkingCopy(this.wcOwner, null);
			IType foo =  fooWC.getType("Foo");

			CompilerOptions compilerOptions = new CompilerOptions(p.getOptions(true));
			ProblemReporter problemReporter = new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(),
					compilerOptions,
					new DefaultProblemFactory());

			// force usage of type converter:
			CompilationUnitDeclaration parsedUnit =
				SourceTypeConverter.buildCompilationUnit(
						new ISourceType[] {(ISourceType) ((SourceType)foo).getElementInfo()},
						SourceTypeConverter.FIELD_AND_METHOD | SourceTypeConverter.MEMBER_TYPE | SourceTypeConverter.LOCAL_TYPE,
						problemReporter,
						new CompilationResult("Foo.java".toCharArray(), 1, 1, 90));

			// force resolving:
			process(parsedUnit, p, compilerOptions, problemReporter, ITranslationStates.STATE_RESOLVED);

			// evaluate result:
			CategorizedProblem[] problems = parsedUnit.compilationResult().problems;
			assertNull(problems);
		} finally {
			deleteProject("P");
		}
	}

	public void testBug545316b() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			//prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			createFolder("/P/src/pbase");
			createFile(
				"/P/src/pbase/Base.java",
				"package pbase;\n" +
				"public class Base {\n" +
				"	public void baseMethod(int flag, String name) {}\n" +
				"}\n"
			);

			String sourceFoo =
					"import base pbase.Base;\n" +
					"public team class Foo {\n" +
					"	@SuppressWarnings(\"unused\") private String v1;\n" +
					"	@SuppressWarnings(\"unused\") private String v2;\n" +
					"	@SuppressWarnings(\"unused\") private String v3;\n" +
					"	@SuppressWarnings(\"unused\") private String v4;\n" +
					"	@SuppressWarnings(\"unused\") private String v5;\n" +
					"	@SuppressWarnings(\"unused\") private String v6;\n" +
					"	@SuppressWarnings(\"unused\") private String v7;\n" +
					"	@SuppressWarnings(\"unused\") private String v8;\n" +
					"	@SuppressWarnings(\"unused\") private String v9;\n" +
					"	@SuppressWarnings(\"unused\") private String v10;\n" +
					"	@SuppressWarnings(\"unused\") private String v11;\n" +
					"   protected class R playedBy Base {\n" +
					"		void roleMethod(int f, String both) {}\n" +
					"		void m2(String onlyThis) {}\n" +
					"		void m2(String onlyThis) <- before void baseMethod(int flag, String name)\n" +
					"			with { onlyThis <- name }\n" +
					"		void roleMethod(int f, String both) <- after void baseMethod(int flag, String name)\n" +
					"			with { f <- flag, both <- name }\n" +
					"	}\n" +
					"}\n";
			this.createFile(
					"/P/src/Foo.java",
					sourceFoo
			);

			char[] sourceChars = sourceFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);

			ICompilationUnit fooWC = getCompilationUnit("/P/src/Foo.java").getWorkingCopy(this.wcOwner, null);
			IType foo =  fooWC.getType("Foo");

			CompilerOptions compilerOptions = new CompilerOptions(p.getOptions(true));
			ProblemReporter problemReporter = new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(),
					compilerOptions,
					new DefaultProblemFactory());

			// force usage of type converter:
			CompilationUnitDeclaration parsedUnit =
				SourceTypeConverter.buildCompilationUnit(
						new ISourceType[] {(ISourceType) ((SourceType)foo).getElementInfo()},
						SourceTypeConverter.FIELD_AND_METHOD | SourceTypeConverter.MEMBER_TYPE | SourceTypeConverter.LOCAL_TYPE,
						problemReporter,
						new CompilationResult("Foo.java".toCharArray(), 1, 1, 90));

			// force resolving:
			process(parsedUnit, p, compilerOptions, problemReporter, ITranslationStates.STATE_RESOLVED);

			// evaluate result:
			CategorizedProblem[] problems = parsedUnit.compilationResult().problems;
			assertNull(problems);
		} finally {
			deleteProject("P");
		}
	}

	public void testBug545316c() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"}, new String[] {"JCL15_LIB"}, "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			//prjDesc.setNatureIds(OTDTPlugin.createProjectNatures(prjDesc));
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);

			OTREContainer.initializeOTJProject(project);

			createFolder("/P/src/pbase");
			createFile(
				"/P/src/pbase/Base.java",
				"package pbase;\n" +
				"public class Base {\n" +
				"	public static void baseMethod1(int flag, String name) {}\n" +
				"	public static void baseMethod(int flag, String name) {}\n" +
				"	public String f;\n" +
				"}\n"
			);

			String sourceFoo =
					"import base pbase.Base;\n" +
					"public team class Foo {\n" +
					"	@SuppressWarnings(\"unused\") private String v1;\n" +
					"	@SuppressWarnings(\"unused\") private String v2;\n" +
					"	@SuppressWarnings(\"unused\") private String v3;\n" +
					"	@SuppressWarnings(\"unused\") private String v4;\n" +
					"	@SuppressWarnings(\"unused\") private String v5;\n" +
					"	@SuppressWarnings(\"unused\") private String v6;\n" +
					"	@SuppressWarnings(\"unused\") private String v7;\n" +
					"	@SuppressWarnings(\"unused\") private String v8;\n" +
					"	@SuppressWarnings(\"unused\") private String v9;\n" +
					"	@SuppressWarnings(\"unused\") private String v10;\n" +
					"	@SuppressWarnings(\"unused\") private String v11;\n" +
					"   protected class R playedBy Base {\n" +
					"		static callin void roleMethod1(int f, String both) {\n" +
					"			base.roleMethod1(f, both);\n" +
					"		}\n" +
					"		roleMethod1 <- replace baseMethod1;\n" +
					"		void m2(int f, String both) <- replace void baseMethod(int flag, String name);\n" +
					"		static callin void m2(int f, String both) {\n" +
					"			base.m2(f, both);\n" +
					"		}\n" +
					"		int getFLength() -> get String f with {\n" +
					"			result <- f.length()\n" +
					"		}\n" +
					"	}\n" +
					"}\n";
			this.createFile(
					"/P/src/Foo.java",
					sourceFoo
			);

			// FIXME: removed Indexer.getInstance().waitForIndex(null);
			char[] sourceChars = sourceFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);

			ICompilationUnit fooWC = getCompilationUnit("/P/src/Foo.java").getWorkingCopy(this.wcOwner, null);
			IType foo =  fooWC.getType("Foo");

			CompilerOptions compilerOptions = new CompilerOptions(p.getOptions(true));
			ProblemReporter problemReporter = new ProblemReporter(
					DefaultErrorHandlingPolicies.proceedWithAllProblems(),
					compilerOptions,
					new DefaultProblemFactory());

			// force usage of type converter:
			CompilationUnitDeclaration parsedUnit =
				SourceTypeConverter.buildCompilationUnit(
						new ISourceType[] {(ISourceType) ((SourceType)foo).getElementInfo()},
						SourceTypeConverter.FIELD_AND_METHOD | SourceTypeConverter.MEMBER_TYPE | SourceTypeConverter.LOCAL_TYPE,
						problemReporter,
						new CompilationResult("Foo.java".toCharArray(), 1, 1, 90));

			// force resolving:
			process(parsedUnit, p, compilerOptions, problemReporter, ITranslationStates.STATE_RESOLVED);

			// evaluate result:
			CategorizedProblem[] problems = parsedUnit.compilationResult().problems;
			assertNull(problems);
		} finally {
			deleteProject("P");
		}
	}

	public void testBug547646() throws CoreException, InterruptedException {
		try {
			// Resources creation
			IJavaProject p = createOTJavaProject("P", new String[] {"src"}, new String[] {"JCL18_LIB"}, "1.8", "bin");
			IProject project = p.getProject();
			IProjectDescription prjDesc = project.getDescription();
			prjDesc.setBuildSpec(OTDTPlugin.createProjectBuildCommands(prjDesc));
			project.setDescription(prjDesc, null);
			p.setOption(JavaCore.COMPILER_PB_UNUSED_WARNING_TOKEN, JavaCore.ERROR);

			OTREContainer.initializeOTJProject(project);

			createFile(
				"/P/src/Foo0.java",
				"public team class Foo0 {\n" +
				"	protected class R {\n" +
				"		void m() {}\n" + // byte code of this method will not be available during reconcile of the sub-team
				"	}\n" +
				"}\n"
			);

			// trigger 110 suppressed warnings:
			StringBuilder attributes = new StringBuilder();
			for (int i=0; i<110; i++)
				attributes.append("@SuppressWarnings(\"unused\") private int i"+i+";\n");

			String sourceFoo =
					"public team class Foo extends Foo0 {\n" +
					attributes.toString() +
					"   protected class R {\n" +
					"	    @SuppressWarnings(\"basecall\")\n" +
					"		callin void roleMethod(int f, String both) {\n" +
					"			switch (f) {\n" +
					"				case 1:\n" +
					"					m();\n" +
					"					break;\n" +
					"				default:\n" +
					"					base.roleMethod(f, both);\n" +
					"			}\n" +
					"		}\n" +
					"	}\n" +
					"}\n";
			this.createFile(
					"/P/src/Foo.java",
					sourceFoo
			);

			char[] sourceChars = sourceFoo.toCharArray();
			this.problemRequestor.initialize(sourceChars);

			ICompilationUnit fooWC = getCompilationUnit("/P/src/Foo.java").getWorkingCopy(this.wcOwner, null);
			assertProblems("Unexpected problems",
					"----------\n" +
					"----------\n");

			this.problemRequestor.initialize(sourceChars);
			fooWC.reconcile(AST.getJLSLatest(),
					 ICompilationUnit.FORCE_PROBLEM_DETECTION|ICompilationUnit.ENABLE_STATEMENTS_RECOVERY|ICompilationUnit.ENABLE_BINDINGS_RECOVERY,
					 fooWC.getOwner(), null);

			assertProblems(
				"Unexpected problems",
				"----------\n" +
				"----------\n"
			);
		} finally {
			deleteProject("P");
		}
	}

	private void createRuntimeStubs() throws CoreException {
		createFolder("/P/org/objectteams");
		createFile("/P/org/objectteams/ITeam.java",
				"package org.objectteams;\n" +
				"public interface ITeam {\n" +
				"}\n");
		createFile("/P/org/objectteams/Team.java",
				"package org.objectteams;\n" +
				"public class Team implements ITeam {\n" +
				"	public Object _OT$callNext(IBoundBase2 baze, ITeam[] teams, int idx, int[] callinIds, int boundMethodId, Object[] args, Object[] baseCallArgs, int baseCallFlags)\n" +
				"		return null;\n" +
				"	}\n" +
				"}\n");
		createFile("/P/org/objectteams/ITeamMigratable.java",
				"package org.objectteams;\n" +
				"\n" +
				"public interface ITeamMigratable {\n" +
				"	<R> R migrateToTeam(final ITeam otherTeam);\n" +
				"}"
				);
		createFile("/P/org/objectteams/IBaseMigratable.java",
				"package org.objectteams;\n" +
				"public interface IBaseMigratable {\n" +
				"	<B> void migrateToBase(B otherBase);\n" +
				"}\n" +
				"");
		createFile("/P/org/objectteams/IBoundBase2.java",
				"package org.objectteams;\n" +
				"public interface IBoundBase2 {}\n");
	}

}
