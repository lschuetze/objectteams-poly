/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2019 Fraunhofer Gesellschaft, Munich, Germany,
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
 * $Id$
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.test.builder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.Test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.tests.builder.Problem;
import org.eclipse.jdt.internal.core.JavaCorePreferenceInitializer;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;
import org.eclipse.objectteams.otdt.tests.FileBasedTest;

@SuppressWarnings({ "nls", "restriction" })
public class OTEquinoxBuilderTests extends OTBuilderTests {

	static {
//		TESTS_NAMES = new String[] { "testBug494254"};
	}

	MyFileBasedTest fileManager= new MyFileBasedTest("delegate");
	class MyFileBasedTest extends FileBasedTest {
		public MyFileBasedTest(String name) {
			super(name);
		}
		@Override
		protected String getPluginID() {
			return "org.eclipse.objectteams.otdt.test.builder";
		}
		@Override // make available locally:
		protected IJavaProject setUpJavaProject(String projectName) throws CoreException, IOException
		{
			IJavaProject jProject = setUpOTJProject(projectName);

			// and adjust a few options:

			// use OTDRE since our test projects now use 1.8:
			jProject.setOption(OTDTPlugin.OT_COMPILER_WEAVING_SCHEME, WeavingScheme.OTDRE.name());

			// suppress PDE warnings regarding missing package exports:
			IFolder settings = jProject.getProject().getFolder(".settings");
			if (!settings.exists())
				settings.create(true, true, null);
			String pdePrefs = "compilers.p.missing-packages=2\n";
			settings.getFile("org.eclipse.pde.prefs").create(new ByteArrayInputStream(pdePrefs.getBytes()), true, null);

			return jProject;
		}
		protected IJavaProject setUpOTJProject(final String projectName) throws CoreException, IOException {
			// Eclipse / Equinox generally requires 11:
			IJavaProject javaProject = super.setUpJavaProject(projectName, "11");

	        // add OTJavaNature
			IProjectDescription description = javaProject.getProject().getDescription();
			description.setNatureIds(OTDTPlugin.createProjectNatures(description));
			javaProject.getProject().setDescription(description, null);
			return javaProject;
		}

		protected void replaceWorkspaceFile(String src, IJavaProject project, String dest)
			throws IOException, CoreException
		{
			File srcFile = new File(getPluginDirectoryPath()+File.separator+"workspace"+File.separator+src);
			IFile destFile= project.getProject().getFile(dest);
			destFile.setContents(new FileInputStream(srcFile), true, false, null);
		}
	};

	public OTEquinoxBuilderTests(String name) {
		super(name);
	}

	public static Test suite() {
//*
			return buildTestSuite(OTEquinoxBuilderTests.class);
/*/
		return new OTEquinoxBuilderTests("testBaseImportTwoAspectBindings");
//*/
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		env.setAutoBuilding(false);

		DEBUG = true;

	}

	IProject reopenProject(String projectName) throws CoreException {
		final IProject project = fileManager.getWorkspaceRoot().getProject(projectName);
		IWorkspaceRunnable populate = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				//project.create(null);
				project.open(null);
			}
		};
		fileManager.getWorkspace().run(populate, null);
		env.addProject(project);
		return project;
	}

	public void testForcedExports() throws CoreException, IOException {
		IJavaProject trac18b= fileManager.setUpJavaProject("Trac18b");
		env.addProject(trac18b.getProject());
		IJavaProject trac18a= fileManager.setUpJavaProject("Trac18a");
		env.addProject(trac18a.getProject());
		fullBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectingOnlySpecificProblemsFor(trac18a.getPath(), new Problem[] {
			getDecapsulationProblem(trac18a, "trac18b.actions.SampleAction", "trac18a/Team18.java", 42, 70),   // import
			getDecapsulationProblem(trac18a, "trac18b.actions.SampleAction", "trac18a/Team18.java", 199, 211), // playedBy
		});
		// after success now break it:
		fileManager.replaceWorkspaceFile("Trac18a2/plugin.xml", trac18a, "plugin.xml");
		incrementalBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectAccessRestriction(trac18a, "src/trac18a/Team18.java", 42, 70);
	}

	public void testForcedExportsMissing() throws CoreException, IOException {
		IJavaProject trac18b= fileManager.setUpJavaProject("Trac18b");
		env.addProject(trac18b.getProject());
		IJavaProject trac18a2= fileManager.setUpJavaProject("Trac18a2");
		env.addProject(trac18a2.getProject());
		fullBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectAccessRestriction(trac18a2, "src/trac18a/Team18.java", 42, 70);
		// fix the error
		fileManager.replaceWorkspaceFile("Trac18a/plugin.xml", trac18a2, "plugin.xml");
		incrementalBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectingOnlySpecificProblemsFor(trac18a2.getPath(), new Problem[] {
			getDecapsulationProblem(trac18a2, "trac18b.actions.SampleAction", "trac18a/Team18.java", 42, 70),
			getDecapsulationProblem(trac18a2, "trac18b.actions.SampleAction", "trac18a/Team18.java", 163, 175),
		});
	}

	/* project Trac18a3 contains a base-ctor call and a callout binding,
	 * both referring to the force-exported base class.*/
	public void testForcedExportsGeneratedMethodRefs() throws CoreException, IOException {
		IJavaProject trac18b= fileManager.setUpJavaProject("Trac18b");
		env.addProject(trac18b.getProject());
		IJavaProject trac18a3= fileManager.setUpJavaProject("Trac18a3");
		env.addProject(trac18a3.getProject());
		fullBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectingOnlySpecificProblemsFor(trac18a3.getPath(), new Problem[] {
			getDecapsulationProblem(trac18a3, "trac18b.actions.SampleAction", "trac18a/Team18.java", 42, 70),
			getDecapsulationProblem(trac18a3, "trac18b.actions.SampleAction", "trac18a/Team18.java", 163, 175)
			// base-ctor call no longer flagged
		});
	}
	/* trying to produce a broken and bogus error message a la Trac #154 (no success yet). */
	public void testForcedExportTrac154() throws CoreException, IOException {
		IJavaProject trac154b1= fileManager.setUpJavaProject("Trac154b1");
		env.addProject(trac154b1.getProject());
		IJavaProject trac154b2= fileManager.setUpJavaProject("Trac154b2");
		env.addProject(trac154b2.getProject());
		IJavaProject trac154a= fileManager.setUpJavaProject("Trac154a");
		env.addProject(trac154a.getProject());
		fullBuild();
		expectingNoProblemsFor(trac154b1.getPath());
		expectingNoProblemsFor(trac154b2.getPath());
		expectingOnlySpecificProblemsFor(trac154a.getPath(), new Problem[] {
			getDecapsulationProblem(trac154a, "trac154b1.actions.SampleAction", "aspect/MyTeam.java", 29, 59),
			getDecapsulationProblem(trac154a, "trac154b1.actions.SampleAction", "aspect/MyTeam.java", 118, 130)
		});
	}

	public void testBaseImportNoAspectBinding () throws CoreException, IOException {
		IJavaProject trac18b= fileManager.setUpJavaProject("Trac18b");
		env.addProject(trac18b.getProject());
		IJavaProject aspectPlugin= fileManager.setUpJavaProject("MissingAspectBinding");
		aspectPlugin.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aspectPlugin.getProject());
		fullBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectingOnlySpecificProblemsFor(aspectPlugin.getPath(), new Problem[] {
			new Problem("", "Illegal base import: no aspect binding declared for team MissingAspectBindingTeam or any nested team (OT/Equinox).",
						aspectPlugin.getPath().append(new Path("src/MissingAspectBindingTeam.java")),
						12, 34,
						CategorizedProblem.CAT_CODE_STYLE, IMarker.SEVERITY_ERROR)
		});
		// now fix it:
		fileManager.replaceWorkspaceFile("MissingAspectBinding/plugin-corrected.xml", aspectPlugin, "plugin.xml");
		incrementalBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectingNoProblemsFor(aspectPlugin.getPath());
	}

	/* Two aspect bindings refer to the same team, which has base imports referring to two different base bundles. */
	public void testBaseImportTwoAspectBindings () throws CoreException, IOException {
		IJavaProject trac213b1= fileManager.setUpJavaProject("Trac213b1");
		env.addProject(trac213b1.getProject());
		IJavaProject trac213b2= fileManager.setUpJavaProject("Trac213b2");
		env.addProject(trac213b2.getProject());
		fullBuild();

		// initially only an empty team
		IJavaProject aspectPlugin= fileManager.setUpJavaProject("Trac213a");
		aspectPlugin.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aspectPlugin.getProject());
		fullBuild();

		// now add content to the team, binding to Trac213b1:
		fileManager.replaceWorkspaceFile("Trac213a/auxil/TheTeam_step1.java", aspectPlugin, "src/trac213a/TheTeam.java");

		incrementalBuild();
		expectingNoProblemsFor(trac213b1.getPath());
		expectingNoProblemsFor(trac213b2.getPath());
		expectingNoProblemsFor(aspectPlugin.getPath());

		// now add content to the team, binding to Trac213b2:
		fileManager.replaceWorkspaceFile("Trac213a/auxil/plugin_step2.xml", aspectPlugin, "plugin.xml");
		fileManager.replaceWorkspaceFile("Trac213a/auxil/TheTeam_step2.java", aspectPlugin, "src/trac213a/TheTeam.java");

		incrementalBuild();
		expectingNoProblemsFor(trac213b1.getPath());
		expectingNoProblemsFor(trac213b2.getPath());
		expectingNoProblemsFor(aspectPlugin.getPath());
	}

	public void testWrongBaseImport1() throws CoreException, IOException {
		IJavaProject trac18b= fileManager.setUpJavaProject("Trac18b");
		env.addProject(trac18b.getProject());
		IJavaProject aspectPlugin= fileManager.setUpJavaProject("WrongBaseImport1");
		aspectPlugin.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aspectPlugin.getProject());
		fullBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectingOnlySpecificProblemsFor(aspectPlugin.getPath(), new Problem[] {
			new Problem("", "Illegal base import: this package is not provided by the declared base plug-in(s) Trac18b (OT/Equinox).",
						aspectPlugin.getPath().append(new Path("src/WrongBaseImportTeam1.java")),
						12, 63,
						CategorizedProblem.CAT_CODE_STYLE, IMarker.SEVERITY_ERROR)
		});
	}

	public void testWrongBaseImport2() throws CoreException, IOException {
		IJavaProject trac18b= fileManager.setUpJavaProject("Trac18b");
		env.addProject(trac18b.getProject());
		IJavaProject aspectPlugin= fileManager.setUpJavaProject("WrongBaseImport2");
		aspectPlugin.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aspectPlugin.getProject());
		fullBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectingOnlySpecificProblemsFor(aspectPlugin.getPath(), new Problem[] {
			new Problem("", "Illegal base import: this package is not provided by the declared base plug-in org.eclipse.objectteams.otequinox but by plug-in Trac18b (OT/Equinox).",
						aspectPlugin.getPath().append(new Path("src/WrongBaseImportTeam2.java")),
						12, 34,
						CategorizedProblem.CAT_TYPE, IMarker.SEVERITY_ERROR)
		});
	}

	public void testBaseImportTrac132 () throws CoreException, IOException {
		IJavaProject trac132b= fileManager.setUpJavaProject("Trac132b");
		env.addProject(trac132b.getProject());
		IJavaProject aspectPlugin1= fileManager.setUpJavaProject("Trac132a1");
		aspectPlugin1.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aspectPlugin1.getProject());
		IJavaProject aspectPlugin2= fileManager.setUpJavaProject("Trac132a2");
		aspectPlugin2.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aspectPlugin2.getProject());
		fullBuild();
		expectingNoProblemsFor(trac132b.getPath());
		expectingNoProblemsFor(aspectPlugin1.getPath());
		expectingNoProblemsFor(aspectPlugin2.getPath());
	}

	public void testBaseImportTrac304 () throws CoreException, IOException {
		IJavaProject trac304b= fileManager.setUpJavaProject("Trac304b");
		env.addProject(trac304b.getProject());
		// the fragment:
		IJavaProject trac304f= fileManager.setUpJavaProject("Trac304f");
		env.addProject(trac304f.getProject());
		IJavaProject trac304ot= fileManager.setUpJavaProject("Trac304ot");
		trac304ot.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(trac304ot.getProject());
		fullBuild();
		expectingNoProblemsFor(trac304b.getPath());
		expectingNoProblemsFor(trac304ot.getPath());
	}

	public void testIllegalUseOfForcedExport() throws CoreException, IOException {
		IJavaProject trac18b= fileManager.setUpJavaProject("Trac18b");
		env.addProject(trac18b.getProject());
		IJavaProject aspectPlugin= fileManager.setUpJavaProject("IllegalUseOfForcedExport");
		aspectPlugin.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aspectPlugin.getProject());
		fullBuild();
		expectingNoProblemsFor(trac18b.getPath());
		expectingOnlySpecificProblemsFor(aspectPlugin.getPath(),
				new Problem[] {
					getIllegalUseOfForcedExportProblem(aspectPlugin, "trac18b.actions.SampleAction", 88, 100),
					getIllegalUseOfForcedExportProblem(aspectPlugin, "trac18b.actions.SampleAction", 7, 35)});
	}

	public void testAccumulatedBases1() throws CoreException, IOException {
		IJavaProject project= fileManager.setUpJavaProject("AccumulatedBases");
		project.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(project.getProject());
		fullBuild();
		expectingNoProblemsFor(project.getPath());
		fileManager.replaceWorkspaceFile("AccumulatedBases/versions/Team1.java.1", project, "src/accumulatedbases/Team1.java");
		incrementalBuild();
		expectingNoProblemsFor(project.getPath());
	}

	public void testBaseImportTrac132_2 () throws CoreException, IOException {
		IJavaProject trac132b= fileManager.setUpJavaProject("Trac132b");
		env.addProject(trac132b.getProject());
		IJavaProject aspectPlugin1= fileManager.setUpJavaProject("Trac132a1");
		aspectPlugin1.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aspectPlugin1.getProject());
		IJavaProject aspectPlugin2= fileManager.setUpJavaProject("Trac132a2");
		aspectPlugin2.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aspectPlugin2.getProject());
		fullBuild();
		expectingNoProblemsFor(trac132b.getPath());
		expectingNoProblemsFor(aspectPlugin1.getPath());
		expectingNoProblemsFor(aspectPlugin2.getPath());

		// not needed.
//		JavaModelManager.getJavaModelManager().removePerProjectInfo((JavaProject) trac132b);
//		JavaModelManager.getJavaModelManager().removePerProjectInfo((JavaProject) aspectPlugin1);
//		JavaModelManager.getJavaModelManager().removePerProjectInfo((JavaProject) aspectPlugin2);

		// doesn't work see :
		//ResourcesPlugin.getPlugin().getBundle().stop();
		//ResourcesPlugin.getPlugin().getBundle().start();

		// simulate shutdown (from AbstractJavaModelTests.simulateExitRestart:
		env.getWorkspace().save(true, null);
		JavaModelManager.getJavaModelManager().shutdown();
		JavaModelManager.doNotUse(); // reset the MANAGER singleton
		// this one is extra: also restart PDE's PluginModelManager:
		OTTestingEnvironment.restart();
		JavaModelManager.getJavaModelManager().startup();
		new JavaCorePreferenceInitializer().initializeDefaultPreferences();


		env.openEmptyWorkspace();
		env.setAutoBuilding(true);

		trac132b= JavaCore.create(reopenProject("Trac132b"));
		aspectPlugin1= JavaCore.create(reopenProject("Trac132a1"));
		aspectPlugin2= JavaCore.create(reopenProject("Trac132a2"));
		env.addClass(new Path("/Trac132a2/src"), "Dummy", "public class Dummy {}"); // trigger minimal build
		incrementalBuild();
		expectingNoProblemsFor(trac132b.getPath());
		expectingNoProblemsFor(aspectPlugin1.getPath());
		expectingNoProblemsFor(aspectPlugin2.getPath());
	}

	public void testBug374833() throws CoreException, IOException {
		IJavaProject pluginPrj= fileManager.setUpJavaProject("UnresolvedBasePlugin");
		env.addProject(pluginPrj.getProject());
		fullBuild();
		expectingOnlySpecificProblemsFor(pluginPrj.getPath(),
				new Problem[] {
					new Problem("", "Base plug-in Missing.Plugin cannot be resolved",
							pluginPrj.getPath().append("plugin.xml"),
							-1, -1, -1, IMarker.SEVERITY_ERROR)
				});
	}

	public void testAspectExport () throws CoreException, IOException {
		IJavaProject aeb= fileManager.setUpJavaProject("AspectExportBase");
		env.addProject(aeb.getProject());
		IJavaProject aea= fileManager.setUpJavaProject("AspectExportAspect");
		aea.setOption("org.eclipse.objectteams.otdt.compiler.problem.binding_conventions", "error");
		env.addProject(aea.getProject());
		fullBuild();
		expectingNoProblemsFor(aeb.getPath());
		expectingOnlySpecificProblemsFor(aea.getPath(),
				new Problem[] {
					getMissingAspectExportProblem(aea, "aea")});
	}

	public void testBug419987() throws CoreException, IOException {
		IJavaProject aeb= fileManager.setUpJavaProject("Base419987");
		env.addProject(aeb.getProject());
		IJavaProject aea= fileManager.setUpJavaProject("Bug419987");
		env.addProject(aea.getProject());
		fullBuild();
		expectingNoProblemsFor(aeb.getPath());
		expectingNoProblemsFor(aea.getPath());
	}

	public void testBug484275() throws CoreException, IOException {
		IJavaProject basePrj= fileManager.setUpJavaProject("Base484275");
		env.addProject(basePrj.getProject());
		IJavaProject aspPrj= fileManager.setUpJavaProject("Bug484275");
		env.addProject(aspPrj.getProject());
		fullBuild();
		expectingNoProblemsFor(basePrj.getPath());
		expectingOnlySpecificProblemsFor(aspPrj.getPath(), new Problem[] {
			new Problem("", "Team 'teams.MyTeam$Middle' lacks a binding for base package 'internal'",
					aspPrj.getPath().append(new Path("plugin.xml")),
						-1, -1, -1, IMarker.SEVERITY_ERROR)
		});
	}

	public void testBug495489() throws CoreException, IOException {
		IJavaProject aspPrj= fileManager.setUpJavaProject("Bug495489");
		env.addProject(aspPrj.getProject());
		fullBuild();
		expectingNoProblemsFor(aspPrj.getPath());
	}

	public void testBug494254() throws CoreException, IOException {
		IJavaProject basePrj0= fileManager.setUpJavaProject("Bug494254base0");
		env.addProject(basePrj0.getProject());
		IJavaProject basePrj1= fileManager.setUpJavaProject("Bug494254base1");
		env.addProject(basePrj1.getProject());
		IJavaProject aspPrj= fileManager.setUpJavaProject("Bug494254aspect");
		env.addProject(aspPrj.getProject());
		fullBuild();
		expectingNoProblemsFor(basePrj0.getPath());
		expectingNoProblemsFor(basePrj1.getPath());
		expectingOnlySpecificProblemsFor(aspPrj.getPath(), new Problem[] {
			new Problem("", "Team 'bug494254aspect.Team1' lacks a superBase declaration for base class 'p0.C0'",
					aspPrj.getPath().append(new Path("plugin.xml")),
						-1, -1, -1, IMarker.SEVERITY_ERROR),
			new Problem("", "Plug-in 'Bug494254base0' does not provide package 'p1'",
					aspPrj.getPath().append("plugin.xml"),
						-1, -1, -1, IMarker.SEVERITY_ERROR),
			new Problem("", "Unnecessary superBase declaration 'p0.C0'",
					aspPrj.getPath().append("plugin.xml"),
						-1, -1, -1, IMarker.SEVERITY_WARNING),
			new Problem("", "Unnecessary superBase declaration 'p1.C1'",
					aspPrj.getPath().append("plugin.xml"),
						-1, -1, -1, IMarker.SEVERITY_WARNING)
		});
		// repair and check absence of problems:
		IFile normal = (IFile) aspPrj.getProject().findMember("plugin.xml");
		IFile ok = (IFile) aspPrj.getProject().findMember("plugin-ok.xml");
		normal.setContents(ok.getContents(), 0, null);
		fullBuild();
		expectingNoProblemsFor(aspPrj.getPath());
	}

	// ---------------- HELPERS: ---------------------------
	private Problem getDecapsulationProblem(IJavaProject project, String baseclassName, String teamPath, int start, int end) {
		return new Problem("", "Decapsulating base class "+baseclassName+" by means of a forced export. Note, that additionally a corresponing declaration is needed in config.ini (OTJLD 2.1.2(c) + OT/Equinox).",
				project.getPath().append(new Path("src/"+teamPath)),
				start, end,
				CategorizedProblem.CAT_RESTRICTION, IMarker.SEVERITY_WARNING);
	}
	private Problem getIllegalUseOfForcedExportProblem(IJavaProject project, String className, int start, int end) {
		return new Problem("", "The forced-exported type "+className+" cannot be accessed other than as a role's base class (OT/Equinox).",
				project.getPath().append(new Path("src/IllegalUseOfForcedExportTeam.java")),
				start, end,
				CategorizedProblem.CAT_TYPE, IMarker.SEVERITY_ERROR);
	}
	private void expectAccessRestriction(IJavaProject project, String fileName, int start, int end) {
		expectingSpecificProblemFor(project.getPath(),
			new Problem("", "Access restriction: The type 'SampleAction' is not API (restriction on required project 'Trac18b')",
						project.getPath().append(new Path(fileName)),
						start, end,
						CategorizedProblem.CAT_RESTRICTION, IMarker.SEVERITY_ERROR)
		);
	}
	private Problem getMissingAspectExportProblem(IJavaProject project, String packageName) {
		return new Problem("", "Package "+packageName+" containing one or more bound teams must be exported.",
				project.getPath().append(new Path("META-INF/MANIFEST.MF")),
				-1, -1,
				-1, IMarker.SEVERITY_ERROR);
	}
}
