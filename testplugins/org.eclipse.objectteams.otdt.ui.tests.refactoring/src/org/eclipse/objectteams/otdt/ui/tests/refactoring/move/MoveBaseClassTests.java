/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2012 Stephan Herrmann.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.refactoring.move;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.refactoring.reorg.JavaMoveProcessor;
import org.eclipse.jdt.ui.tests.refactoring.ParticipantTesting;
import org.eclipse.jdt.ui.tests.refactoring.RefactoringTestSetup;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.objectteams.otdt.core.ext.OTREContainer;
import org.eclipse.objectteams.otdt.ui.tests.util.JavaProjectHelper;

import base org.eclipse.jdt.ui.tests.refactoring.ccp.MoveTest;

// see Bug 367605 - [refactoring] missing update of base import during renaming or moving a base class
@SuppressWarnings("restriction")
public team class MoveBaseClassTests extends TestCase {

	@SuppressWarnings("decapsulation")
	protected class MoveBaseClass playedBy MoveTest {
		
		void setUp() -> void setUp();

		void tearDown() -> void tearDown();
		
		public MoveBaseClass() {
			base(MoveBaseClass.class.getName());
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
	private boolean projectInitialized = false;

	public MoveBaseClassTests() {
		this.mover = new MoveBaseClass();
	}
    public static Test suite() {
        return new RefactoringTestSetup(new TestSuite(MoveBaseClassTests.class));
    }

    public static Test setUpTest(Test someTest) {
        return new RefactoringTestSetup(someTest);
    }

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setupProject();
		this.mover.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.mover.tearDown();
	}
	
	void setupProject() throws Exception {
		if (!this.projectInitialized) {
			this.projectInitialized = true;
			IJavaProject javaProj = RefactoringTestSetup.getProject();
			JavaProjectHelper.addNatureToProject(javaProj.getProject(), JavaCore.OTJ_NATURE_ID, null);
	        OTREContainer.initializeOTJProject(javaProj.getProject());
		}
	}

	public void testDestination_yes_roleTypeToDifferentTeam() throws Exception {
		this.mover.testDestination_yes_baseTypeToDifferentPackage();
	}

}
