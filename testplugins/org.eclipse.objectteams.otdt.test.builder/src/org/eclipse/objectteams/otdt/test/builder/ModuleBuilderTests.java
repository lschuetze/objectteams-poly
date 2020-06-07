/*******************************************************************************
 * Copyright (c) 2020 Stephan Herrmann.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.objectteams.otdt.test.builder;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.tests.model.ModifyingResourceTests;
import org.eclipse.objectteams.otdt.core.ext.OTREContainer;
import org.eclipse.objectteams.otdt.tests.AbstractJavaModelTests;

import junit.framework.Test;

public class ModuleBuilderTests extends ModifyingResourceTests {
	public ModuleBuilderTests(String name) {
		super(name);
	}

	static {
//		 TESTS_NAMES = new String[] { "testReleaseOption8" };
	}
	private String sourceWorkspacePath = null;
	protected ProblemRequestor problemRequestor;
	public static Test suite() {
		if (!isJRE9) {
			// almost empty suite, since we need JRE9+
			Suite suite = new Suite(ModuleBuilderTests.class.getName());
			suite.addTest(new ModuleBuilderTests("thisSuiteRunsOnJRE9plus"));
			return suite;
		}
		return buildModelTestSuite(ModuleBuilderTests.class, BYTECODE_DECLARATION_ORDER);
	}
	public void thisSuiteRunsOnJRE9plus() {}

	@Override
	public String getSourceWorkspacePath() {
		return this.sourceWorkspacePath == null ? super.getSourceWorkspacePath() : this.sourceWorkspacePath;
	}
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.problemRequestor =  new ProblemRequestor();
		this.wcOwner = new WorkingCopyOwner() {
			public IProblemRequestor getProblemRequestor(ICompilationUnit unit) {
				return ModuleBuilderTests.this.problemRequestor;
			}
		};
	}
	@Override
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		// create general purpose project here?
	}

	@Override
	public void tearDownSuite() throws Exception {
		super.tearDownSuite();
		// delete general purpose project here?
	}

	// Test that OTRE being inaccessible is properly reported
	public void test001() throws CoreException {
		try {
			IJavaProject project = createJava9Project("Test01", new String[]{"src"});
			AbstractJavaModelTests.addOTJavaNature(project.getProject());
			AbstractJavaModelTests.addContainerEntry(project, new Path(OTREContainer.OTRE_CONTAINER_NAME), true);
			this.createFile("Test01/src/module-info.java", "module test01 {}\n");
			this.createFolder("Test01/src/com/greetings");
			this.createFile("Test01/src/com/greetings/MainTeam.java",
					"package com.greetings;\n" +
					"public team class MainTeam {}\n");
			waitForManualRefresh();
			waitForAutoBuild();
			project.getProject().build(IncrementalProjectBuilder.FULL_BUILD, null);
			assertProblemMarkers("Unexpected markers", "The type org.objectteams.Team is not accessible", project.getProject());
		} finally {
			deleteProject("Test01");
		}
	}

	// --- for future use: ---
	
	protected void assertNoErrors() throws CoreException {
		for (IProject p : getWorkspace().getRoot().getProjects()) {
			int maxSeverity = p.findMaxProblemSeverity(null, true, IResource.DEPTH_INFINITE);
			if (maxSeverity == IMarker.SEVERITY_ERROR) {
				for (IMarker marker : p.findMarkers(null, true, IResource.DEPTH_INFINITE))
					System.err.println("Marker "+ marker.toString());
			}
			assertFalse("Unexpected errors in project " + p.getName(), maxSeverity == IMarker.SEVERITY_ERROR);
		}
	}
	
	// sort by CHAR_START then MESSAGE
	@Override
	protected void sortMarkers(IMarker[] markers) {
		Arrays.sort(markers, Comparator.comparingInt((IMarker a) -> a.getAttribute(IMarker.CHAR_START, 0))
									   .thenComparing((IMarker a) -> a.getAttribute(IMarker.MESSAGE, "")));
	}
}
