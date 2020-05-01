/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2012 Stephan Herrmann.
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

import base org.eclipse.jdt.ui.tests.refactoring.ccp.MoveTest;

// see Bug 367605 - [refactoring] missing update of base import during renaming or moving a base class
@SuppressWarnings("restriction")
public team class MoveBaseClassTests extends AbstractMoveTests {

	@SuppressWarnings("decapsulation")
	protected class MoveBaseClass extends Move playedBy MoveTest {

		public MoveBaseClass() {
			base();
		}

		@SuppressWarnings("inferredcallout")
		public void testDestination_yes_baseTypeToDifferentPackage() throws Exception{
			ParticipantTesting.reset();
			IPackageFragment packB = getRoot().createPackageFragment("b", true, null);
			IPackageFragment packBTemp = getRoot().createPackageFragment("b.temp", true, null);
			ICompilationUnit cuB= packB.createCompilationUnit("B.java", "package b;public class B {}", true, null);
			ICompilationUnit cuT= getPackageP().createCompilationUnit("T1.java",
					"package p;\n" +
					"import base b.B;\n" +
					"team class T1{void foo(){}protected class Role playedBy B{}}",
					false, new NullProgressMonitor());
			IType baseType = cuB.getTypes()[0];
			IJavaElement[] javaElements= {baseType};
			IResource[] resources= {};
			JavaMoveProcessor ref= verifyEnabled(resources, javaElements, createReorgQueries());

			Object destination= packBTemp;
			verifyValidDestination(ref, destination);

			RefactoringStatus status= performRefactoring(ref, true);
			assertEquals(null, status);

			// expect that base import has been updated in cuT:
			String expectedSource2=
					"package p;\n" +
					"import base b.temp.B;\n" +
					"team class T1{void foo(){}protected class Role playedBy B{}}";
			assertEqualLines("source compare failed", expectedSource2, cuT.getSource());
		}
	}

	private MoveBaseClass mover;

	public MoveBaseClassTests() {
		this.mover = new MoveBaseClass();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.mover.genericbefore();
	}

	@Override
	protected void tearDown() throws Exception {
		this.mover.genericafter();
		super.tearDown();
	}

	public void testDestination_yes_roleTypeToDifferentTeam() throws Exception {
		this.mover.testDestination_yes_baseTypeToDifferentPackage();
	}

}
