/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2014 Fraunhofer Gesellschaft, Munich, Germany,
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

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.tests.builder.BuilderTests;
import org.eclipse.jdt.core.tests.builder.Problem;
import org.eclipse.jdt.core.tests.builder.TestingEnvironment;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;
import org.eclipse.objectteams.otdt.internal.compiler.adaptor.BuildManager;
import org.eclipse.pde.internal.core.PDECoreMessages;

public class OTBuilderTests extends BuilderTests {

	OTTestingEnvironment otenv;

	WeavingScheme weavingScheme = WeavingScheme.OTDRE; // FIXME: test OTRE, too!

	public OTBuilderTests(String name) {
		super(name);
	}

	/** Sets up this test.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		this.otenv = new OTTestingEnvironment();
		this.otenv.activate();
		env = new TestingEnvironment() {
			@Override
			public Problem[] getProblemsFor(IPath path) {
				// capture some more problems than the super method, but not all:
				Problem[] allProblems = getProblemsFor(path, "org.eclipse.pde.core.problem");
				Problem[] filteredProblems = new Problem[allProblems.length];
				int j=0;
				for (int i = 0; i < allProblems.length; i++) {
					// don't know why this warning is getting reported against perfectly valid files, ignore it for now:
					if (allProblems[i].getMessage().equals(PDECoreMessages.Builders_Manifest_useless_file))
						continue;
					if (allProblems[i].getMessage().startsWith("The JRE container on the classpath is not a perfect match to the"))
						continue; // start of PDECoreMessages.BundleErrorReporter_reqExecEnv_conflict - reported when not running on JavaSE-11
					// some plugin.xml have a deprecated 'action' extension, just ignore those:
					if (allProblems[i].getMessage().contains("deprecated"))
						continue;
					if (allProblems[i].getMessage().startsWith("The compiler compliance specified is")) // new warning from bug 533880
						continue;
					filteredProblems[j++] = allProblems[i];
				}
				if (j < allProblems.length)
					System.arraycopy(filteredProblems, 0, filteredProblems = new Problem[j], 0, j);
				return filteredProblems;
			}
		};
		env.openEmptyWorkspace();
		env.resetWorkspace();

		BuildManager.DEBUG = 2;
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.otenv.deactivate();
		this.otenv = null;
	}
	/** Verifies that the given element has problems.
	 * Old implementation from BuilderTests
	 */
	protected void expectingProblemsFor(IPath expected) {
		if (DEBUG)
			printProblemsFor(expected);

		/* get the leaf problems for this type */
		Problem[] problems = env.getProblemsFor(expected);
		assertTrue("missing expected problem with " + expected.toString(), problems.length > 0); //$NON-NLS-1$
	}

}