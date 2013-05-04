/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 (c) GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.refactoring.rolefile;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.rolefile.MoveToRoleFileRefactoring;
import org.eclipse.objectteams.otdt.ui.tests.refactoring.MySetup;
import org.eclipse.objectteams.otdt.ui.tests.refactoring.RefactoringTest;

public class MoveToRileFileTests extends RefactoringTest {
	private static final String REFACTORING_PATH = "MoveToRileFile/";

	public MoveToRileFileTests(String name) {
		super(name);
	}

	public static Test suite() {
		return new MySetup(new TestSuite(MoveToRileFileTests.class));
	}

	public static Test setUpTest(Test test) {
		return new MySetup(test);
	}

	protected String getRefactoringPath() {
		return REFACTORING_PATH;
	}

	private ICompilationUnit[] createOutputCUs(String[] cuNames) throws Exception {
		ICompilationUnit[] cus = new ICompilationUnit[cuNames.length];

		for (int idx = 0; idx < cuNames.length; idx++) {
			Assert.isNotNull(cuNames[idx]);
			IPackageFragment pack = getPackageP();
			String cuName = cuNames[idx];
			int slash = cuName.lastIndexOf('/');
			if (slash != -1) {
				String subDirName = cuName.substring(0, slash);
				String cuSimpleName = cuName.substring(slash+1);
				pack = ((IPackageFragmentRoot)pack.getParent()).getPackageFragment(pack.getElementName()+'.'+subDirName);
				cus[idx] = createCUfromTestFile(pack, cuSimpleName, subDirName+'/', false);
			} else {
				cus[idx] = createCUfromTestFile(pack, cuName, false);
			}
		}
		return cus;
	}


	private static MoveToRoleFileRefactoring createMoveToRoleFileRefactoring(IType role)
			throws JavaModelException {
		MoveToRoleFileRefactoring refactoring = new MoveToRoleFileRefactoring(role);
		return refactoring;
	}

	/**
	 * The <code>ICompilationUnit</code> containing the role that defines the
	 * <code>ICallinMapping</code> at position 0 and its base at position 1.
	 * 
	 */
	private void performMoveToRoleFile_pass(String inputCU, String roleName, String[] outputCUNames) throws Exception {
		ICompilationUnit cu = createCUfromTestFile(getPackageP(), inputCU);
		ICompilationUnit[] outputCUs = null;
		try {

			IType roleType = getType(cu, roleName);
			assertTrue("role  " + roleName + " does not exist", roleType.exists());


			MoveToRoleFileRefactoring ref = createMoveToRoleFileRefactoring(roleType);

			assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
	
			RefactoringStatus checkInputResult = ref.checkFinalConditions(new NullProgressMonitor());
			assertTrue("precondition was supposed to pass", !checkInputResult.hasError());
			performChange(ref, false);

			outputCUs = createOutputCUs(outputCUNames);
			for (int i = 0; i < outputCUs.length; i++) {
				String expected = getFileContents(getOutputTestFileName(outputCUNames[i]));
				String actual = outputCUs[i].getSource();
				assertEqualLines(expected, actual);
			}
		} finally {
			performDummySearch();
			if (outputCUs != null)
				for (int i = 0; i < outputCUs.length; i++) {
					outputCUs[i].delete(false, null);
				}
		}
	}
	
	/**
	 * The <code>ICompilationUnit</code> containing the role that defines the
	 * <code>ICallinMapping</code> at position 0 and its base at position 1.
	 * 
	 */
	private void performMoveToRoleFile_failing(String[] cuNames, String roleName) throws Exception {
//		ICompilationUnit[] cus = createCUs(cuNames);
//		try {
//
//			IType roleType = getType(cus[0], roleName);
//			assertTrue("role  " + roleName + " does not exist", roleType.exists());
//
//			MoveToRoleFileRefactoring ref = createMoveToRoleFileRefactoring(roleType);
//
//			assertTrue("activation", ref.checkInitialConditions(new NullProgressMonitor()).isOK());
//
//			RefactoringStatus result = performRefactoring(ref);
//			assertNotNull("precondition was supposed to fail.", result);
//			assertTrue("precondition was supposed to fail.", !result.isOK());
//			assertNotNull("precondition result is expected to contain an error.", result.getEntryMatchingSeverity(RefactoringStatus.ERROR));
//			
//		} finally {
//			performDummySearch();
//			for (int i = 0; i < cus.length; i++) {
//				cus[i].delete(false, null);
//			}
//
//		}
	}


	/******* tests ******************/

	/* Move To Role File Tests */
	
	public void testSimpleRole1() throws Exception {
		performMoveToRoleFile_pass("T", "R", new String[] { "T", "T/R" });
	}
	
	public void testWithMoreContents() throws Exception {
		performMoveToRoleFile_pass("T", "R2", new String[] { "T", "T/R2" });
	}
}
