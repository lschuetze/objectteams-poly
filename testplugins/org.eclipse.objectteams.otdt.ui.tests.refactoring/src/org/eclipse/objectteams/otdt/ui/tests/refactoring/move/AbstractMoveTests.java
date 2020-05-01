/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2020 Stephan Herrmann.
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
package org.eclipse.objectteams.otdt.ui.tests.refactoring.move;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.tests.refactoring.rules.RefactoringTestSetup;
import org.eclipse.objectteams.otdt.core.ext.OTREContainer;
import org.eclipse.objectteams.otdt.ui.tests.util.JavaProjectHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import junit.framework.TestCase;

import base org.eclipse.jdt.ui.tests.refactoring.GenericRefactoringTest;

/** Super team for tests that need access to private test methods from JDT/UI. */
@SuppressWarnings("restriction")
public abstract team class AbstractMoveTests extends TestCase {

	protected class Move playedBy GenericRefactoringTest {
		public void genericbefore() -> void genericbefore();
		public void genericafter() -> void genericafter();
	}

	private boolean projectInitialized = false;

	@Rule
	public RefactoringTestSetup fts= new RefactoringTestSetup();

	@Before
	@Override
	protected void setUp() throws Exception {
		this.fts.before();
		setupProject();
	}

	@After
	@Override
	protected void tearDown() throws Exception {
		this.fts.after();
	}
	
	void setupProject() throws Exception {
		if (!this.projectInitialized) {
			this.projectInitialized = true;
			IJavaProject javaProj = RefactoringTestSetup.getProject();
			JavaProjectHelper.addNatureToProject(javaProj.getProject(), JavaCore.OTJ_NATURE_ID, null);
	        OTREContainer.initializeOTJProject(javaProj.getProject());
		}
	}

}
