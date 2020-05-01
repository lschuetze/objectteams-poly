/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2011 Stephan Herrmann.
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
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.refactoring.move;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.corext.refactoring.reorg.JavaMoveProcessor;
import org.eclipse.jdt.ui.tests.refactoring.ParticipantTesting;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base org.eclipse.jdt.ui.tests.refactoring.ccp.MoveTest;

@SuppressWarnings("restriction")
public team class MoveRoleTests extends AbstractMoveTests {

	/**
	 * Need access to some inaccessible test methods from base, notably:
	 * verifyEnabled(), verifyValidDestination(), performRefactoring()
	 */
	@SuppressWarnings("decapsulation")
	protected class MoveRole extends Move playedBy MoveTest {
		
		public MoveRole() {
			base();
		}

		@SuppressWarnings("inferredcallout")
		public void testDestination_yes_roleTypeToDifferentTeam() throws Exception{
			ParticipantTesting.reset();
			IPackageFragment packB = getRoot().createPackageFragment("b", true, null);
			packB.createCompilationUnit("B.java", "package b;public class B {}", true, null);
			ICompilationUnit cu1= getPackageP().createCompilationUnit("T1.java", "package p;import base b.B;team class T1{void foo(){}protected class Role playedBy B{}}", false, new NullProgressMonitor());
			ICompilationUnit cu2= getPackageP().createCompilationUnit("T2.java", "package p;team class T2{void bar(){}}", false, new NullProgressMonitor());

			IType teamType = cu1.getTypes()[0];
			IType roleType = teamType.getType("Role");
			IJavaElement[] javaElements= { roleType };
			IResource[] resources= {};
			JavaMoveProcessor ref= verifyEnabled(resources, javaElements, createReorgQueries());

			Object destination= cu2.getTypes()[0];
			verifyValidDestination(ref, destination);

			RefactoringStatus status= performRefactoring(ref, true);
			assertEquals(null, status);

			// expect that base import has been added to cu2:
			String expectedSource2= "package p;\n\nimport base b.B;\n\nteam class T2{protected class Role playedBy B{}\n\nvoid bar(){}}";
			assertEqualLines("source compare failed", expectedSource2, cu2.getSource());

			// expect that base import has been removed from cu1:
			String expectedSource1= "package p;\n\nteam class T1{void foo(){}}";
			assertEqualLines("source compare failed", expectedSource1, cu1.getSource());
		}
	}

	private MoveRole mover;

	public MoveRoleTests() {
		this.mover = new MoveRole();
	}
	
	@Before
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.mover.genericbefore();
	}

	@After
	@Override
	protected void tearDown() throws Exception {
		this.mover.genericafter();
		super.tearDown();
	}

	@Test
	public void testDestination_yes_roleTypeToDifferentTeam() throws Exception {
		this.mover.testDestination_yes_roleTypeToDifferentTeam();
	}

}
