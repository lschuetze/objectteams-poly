/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2010 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.ui.tests.refactoring.rename;

import java.util.Hashtable;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameMethodProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameVirtualMethodProcessor;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
import org.eclipse.objectteams.otdt.ui.tests.refactoring.MySetup;
import org.eclipse.objectteams.otdt.ui.tests.refactoring.RefactoringTest;

/**
 * @author brcan
 *
 */
@SuppressWarnings("restriction")
public class RenameVirtualMethodInClassTests extends RefactoringTest {
	private static final String REFACTORING_PATH = "RenameVirtualMethodInClass/";

	public RenameVirtualMethodInClassTests(String name) {
		super(name);
	}

	public static Test suite() {
		return new MySetup(new TestSuite(RenameVirtualMethodInClassTests.class));
	}

	public static Test setUpTest(Test test) {
		return new MySetup(test);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void setUp() throws Exception
	{
		super.setUp();
		Hashtable options = JavaCore.getOptions();
		options.put(OTDTPlugin.OT_COMPILER_INFERRED_CALLOUT, JavaCore.WARNING);
		JavaCore.setOptions(options);
	}

	protected String getRefactoringPath() {
		return REFACTORING_PATH;
	}

	private RenameRefactoring createRefactoring(RenameMethodProcessor processor) {
		return new RenameRefactoring(processor);
	}

	private RenameMethodProcessor createProcessor(IMethod method) {
		return new RenameVirtualMethodProcessor(method);
	}

	private void performRenaming_failing(String[] cuNames, String declaringTypeName, String[] signature, boolean updateReferences) throws Exception {
		performRenameRefactoring_failing(cuNames, declaringTypeName, "m", "k", signature, updateReferences);
	}

	private void performRenameRefactoring_failing(String[] cuNames, String declaringTypeName, String methodName, String newMethodName, String[] signatures,
			boolean updateReferences) throws Exception {
		ICompilationUnit[] cus = createCUs(cuNames);
		IType declaringType = getType(cus[0], declaringTypeName);
		IMethod method = declaringType.getMethod(methodName, signatures);
		RenameMethodProcessor processor = createProcessor(method);
		RenameRefactoring ref = createRefactoring(processor);
		processor.setUpdateReferences(updateReferences);
		processor.setNewElementName(newMethodName);
		RefactoringStatus result = performRefactoring(ref);

		assertNotNull("precondition was supposed to fail!", result);
	}

	private void performRenaming_passing(String[] cuNames, String declaringTypeName) throws Exception {
		performRenamingMtoK_passing(cuNames, declaringTypeName, true);
	}

	private void performRenamingMtoK_passing(String[] cuNames, String declaringTypeName, boolean updateReferences) throws Exception {
		performRenameRefactoring_passing(cuNames, declaringTypeName, "m", "k", new String[0], true, updateReferences);
	}

	private void performRenameRefactoring_passing(String[] cuNames, String declaringTypeName, String methodName, String newMethodName, String[] signatures,
			boolean shouldPass, boolean updateReferences) throws Exception {
		ICompilationUnit[] cus = createCUs(cuNames);
		IType declaringType = getType(cus[0], declaringTypeName);
		IMethod method = declaringType.getMethod(methodName, signatures);
		RenameMethodProcessor processor = createProcessor(method);
		RenameRefactoring ref = createRefactoring(processor);
		processor.setUpdateReferences(updateReferences);
		processor.setNewElementName(newMethodName);
		RefactoringStatus status = performRefactoring(ref);

		assertEquals("was supposed to pass!", null, status);
		if (!shouldPass) {
			for (int idx = 0; idx < cus.length; idx++) {
				assertTrue("incorrect renaming because of java model!", !getFileContents(createOutputTestFileName(cus, idx)).equals(cus[idx].getSource()));
			}
			return;
		}

		for (int idx = 0; idx < cus.length; idx++) {
			String expectedRenaming = getFileContents(createOutputTestFileName(cus, idx));
			String actualRenaming = cus[idx].getSource();
			assertEqualLines("incorrect renaming!", expectedRenaming, actualRenaming);
		}
		assertTrue("anythingToUndo", RefactoringCore.getUndoManager().anythingToUndo());
		assertTrue("! anythingToRedo", !RefactoringCore.getUndoManager().anythingToRedo());

		RefactoringCore.getUndoManager().performUndo(null, new NullProgressMonitor());

		for (int idx = 0; idx < cus.length; idx++) {
			assertEqualLines("invalid undo!", getFileContents(createInputTestFileName(cus, idx)), cus[idx].getSource());
		}
		assertTrue("! anythingToUndo", !RefactoringCore.getUndoManager().anythingToUndo());
		assertTrue("anythingToRedo", RefactoringCore.getUndoManager().anythingToRedo());

		RefactoringCore.getUndoManager().performRedo(null, new NullProgressMonitor());
		for (int idx = 0; idx < cus.length; idx++) {
			assertEqualLines("invalid redo", getFileContents(createOutputTestFileName(cus, idx)), cus[idx].getSource());
		}
	}

	/********** tests **********/

	public void testUpdateDeclarationOfOverridingRoleMethod() throws Exception {
		performRenaming_passing(new String[] { "B", "T" }, "B");
	}

	public void testUpdateReferenceToBaseMethodInCallinBinding() throws Exception {
		performRenaming_passing(new String[] { "B", "T" }, "B");
	}

	public void testUpdateReferenceToBaseMethodInCallinBindings1() throws Exception {
		performRenaming_passing(new String[] { "B", "T" }, "B");
	}

	public void testUpdateReferenceToBaseMethodInCallinBindings2() throws Exception {
		performRenaming_passing(new String[] { "B", "T1", "T2" }, "B");
	}

	public void testUpdateReferenceToBaseMethodInCalloutBinding1() throws Exception {
		performRenaming_passing(new String[] { "B", "T" }, "B");
	}

	public void testUpdateReferenceToBaseMethodInCalloutBinding2() throws Exception {
		performRenameRefactoring_passing(new String[] { "B", "T" }, "B", "getAmount", "getQuantity", new String[0], true, true);
	}

	public void testUpdateReferenceToBaseMethodInCalloutBinding3() throws Exception {
		performRenameRefactoring_passing(new String[] { "B", "T" }, "B", "getAmount", "getQuantity", new String[0], true, true);
	}

	public void testUpdateReferenceToBaseMethodInExplicitlyOverriddenCalloutBinding() throws Exception {
		performRenaming_passing(new String[] { "B", "T" }, "B");
	}

	public void testUpdateReferenceInFieldInitializationInRole() throws Exception {
		performRenaming_passing(new String[] { "B", "T" }, "B");
	}

	public void testUpdateReferenceInFieldInitializationInTeam() throws Exception {
		performRenaming_passing(new String[] { "B", "T" }, "B");
	}

	public void testUpdateReferenceToBaseMethodInImplicitlyOverriddenCalloutBinding() throws Exception {
		performRenaming_passing(new String[] { "B", "T1", "T2" }, "B");
	}

	public void testUpdateReferenceInRoleMethod() throws Exception {
		performRenaming_passing(new String[] { "B", "T" }, "B");
	}

	public void testUpdateReferenceInTeamMethod() throws Exception {
		performRenaming_passing(new String[] { "B", "T" }, "B");
	}

	public void testUpdateReferenceToOverridingRoleMethod() throws Exception {
		performRenaming_passing(new String[] { "S", "T" }, "S");
	}

	public void testUpdateReferenceToOverridingRoleMethodInCallinBinding() throws Exception {
		performRenaming_passing(new String[] { "S", "B", "T" }, "S");
	}

	public void testUpdateReferenceToOverridingRoleMethodInCallinBindings1() throws Exception {
		performRenaming_passing(new String[] { "S", "B", "T" }, "S");
	}

	public void testUpdateReferenceToOverridingRoleMethodInCallinBindings2() throws Exception {
		performRenaming_passing(new String[] { "S", "B", "T1", "T2" }, "S");
	}

	public void testUpdateReferenceToOverridingRoleMethodInExplicitlyOverriddenCalloutBinding() throws Exception {
		performRenaming_passing(new String[] { "S", "B", "T" }, "S");
	}

	public void testUpdateReferenceToOverridingRoleMethodInImplicitlyOverriddenCalloutBinding() throws Exception {
		performRenaming_passing(new String[] { "S", "B", "T1", "T2" }, "S");
	}

	public void testUpdateReferenceToOverridingRoleMethodInCallinBindingOfImplicitSubrole() throws Exception {
		performRenaming_passing(new String[] { "S", "B", "T1", "T2" }, "S");
	}

	public void testUpdateReferenceToOverridingRoleMethodInCalloutBindingOfImplicitSubrole() throws Exception {
		performRenaming_passing(new String[] { "S", "B", "T1", "T2" }, "S");
	}

	public void testUpdateReferenceToOverridingRoleMethodInTsuperCall() throws Exception {
		performRenaming_passing(new String[] { "S", "T1", "T2" }, "S");
	}

	public void testRenamePrivateMethodInRoleclass() throws Exception {
		performRenaming_passing(new String[] { "T" }, "R");
	}

	public void testRenamePrivateAbstractMethodInRoleclass() throws Exception {
		performRenameRefactoring_passing(new String[] { "T", "B" }, "R", "m1", "m3", new String[0], true, true);
	}

	public void testRenamePrivateMethodInNestedTeam() throws Exception {
		performRenaming_passing(new String[] { "T" }, "TR");
	}

	public void testUpdateImplicitlyInheritedAndOverridingPrivateMethod1() throws Exception {
		performRenaming_passing(new String[] { "T1", "T2" }, "R1");
	}

	public void testUpdateImplicitlyInheritedAndOverridingPrivateMethod2() throws Exception {
		performRenaming_passing(new String[] { "T1", "T2" }, "TR1");
	}

	public void testUpdateReferenceToPrivateRoleMethodInCalloutBinding() throws Exception {
		performRenameRefactoring_passing(new String[] { "T1", "T2", "B" }, "R", "m1", "m3", new String[0], true, true);
	}

	public void testUpdateReferenceToPrivateRoleMethodInCallinBinding() throws Exception {
		performRenameRefactoring_passing(new String[] { "T", "B" }, "R", "m1", "m3", new String[0], true, true);
	}

	public void testUpdateMethodToCauseAmbiguousCallinBinding1() throws Exception {
		// performRenaming_passing(new String[] { "B", "T" }, "B");
		performRenaming_failing(new String[] { "B", "T" }, "B", new String[0], true);
	}

	public void testUpdateMethodToCauseAmbiguousCallinBinding2() throws Exception {
		performRenaming_failing(new String[] { "B", "T" }, "B", new String[0], true);
	}

}