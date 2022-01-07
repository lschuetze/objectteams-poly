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
package org.eclipse.objectteams.otdt.ui.tests.refactoring.reorg;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportContainer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.TypedSource;
import org.eclipse.jdt.internal.ui.refactoring.reorg.CopyToClipboardAction;
import org.eclipse.jdt.internal.ui.refactoring.reorg.PasteAction;
import org.eclipse.jdt.internal.ui.refactoring.reorg.TypedSourceTransfer;
import org.eclipse.jdt.ui.tests.refactoring.GenericRefactoringTest;
import org.eclipse.jdt.ui.tests.refactoring.infra.MockClipboard;
import org.eclipse.jdt.ui.tests.refactoring.infra.MockWorkbenchSite;
import org.eclipse.jdt.ui.tests.refactoring.rules.RefactoringTestSetup;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.junit.Rule;
import org.junit.Test;


@SuppressWarnings("restriction")
public class PasteActionTest extends GenericRefactoringTest {

	private Clipboard _clipboard;
	private static final String REFACTORING_PATH= "Paste/";

	@Rule
	public RefactoringTestSetup fts= new RefactoringTestSetup();

	protected String getRefactoringPath() {
		return REFACTORING_PATH;
	}

	@Override
	public void genericbefore() throws Exception {
		super.genericbefore();
		_clipboard= new MockClipboard(Display.getDefault());
	}
	@Override
	public void genericafter() throws Exception {
		super.genericafter();
		_clipboard.dispose();
	}

	private static Object[] merge(Object[] array1, Object[] array2) {
		Set<Object> elements= new HashSet<>(array1.length + array2.length);
		elements.addAll(Arrays.asList(array1));
		elements.addAll(Arrays.asList(array2));
		return elements.toArray();
	}

	private PasteAction verifyEnabled(IResource[] copySelectedResources, IJavaElement[] copySelectedJavaElements, IResource[] pasteSelectedResources, IJavaElement[] pasteSelectedJavaElements) throws JavaModelException {
		PasteAction pasteAction= new PasteAction(new MockWorkbenchSite(merge(pasteSelectedResources, pasteSelectedJavaElements)), _clipboard);
		CopyToClipboardAction copyToClipboardAction= new CopyToClipboardAction(new MockWorkbenchSite(merge(copySelectedResources, copySelectedJavaElements)), _clipboard);
		copyToClipboardAction.setAutoRepeatOnFailure(true);
		copyToClipboardAction.update(copyToClipboardAction.getSelection());
		assertTrue("copy not enabled", copyToClipboardAction.isEnabled());
		copyToClipboardAction.run();

		pasteAction.update(pasteAction.getSelection());
		assertTrue("paste should be enabled", pasteAction.isEnabled());
		return pasteAction;
	}

	private PasteAction verifyEnabled(IResource[] copySelectedResources, IJavaElement[] copySelectedJavaElements, IWorkingSet pasteSelectedWorkingSet) throws JavaModelException {
		PasteAction pasteAction= new PasteAction(new MockWorkbenchSite(new Object[] {pasteSelectedWorkingSet}), _clipboard);
		CopyToClipboardAction copyToClipboardAction= new CopyToClipboardAction(new MockWorkbenchSite(merge(copySelectedResources, copySelectedJavaElements)), _clipboard);
		copyToClipboardAction.setAutoRepeatOnFailure(true);
		copyToClipboardAction.update(copyToClipboardAction.getSelection());
		assertTrue("copy not enabled", copyToClipboardAction.isEnabled());
		copyToClipboardAction.run();

		pasteAction.update(pasteAction.getSelection());
		assertTrue("paste should be enabled", pasteAction.isEnabled());
		return pasteAction;
	}

	@Test
	public void testEnabled_javaProject() throws Exception {
		IJavaElement[] javaElements= {fts.getProject()};
		IResource[] resources= {};
		verifyEnabled(resources, javaElements, new IResource[0], new IJavaElement[0]);
	}

	@Test
	public void testEnabled_project() throws Exception {
		IJavaElement[] javaElements= {};
		IResource[] resources= {fts.getProject().getProject()};
		verifyEnabled(resources, javaElements, new IResource[0], new IJavaElement[0]);
	}

	@Test
	public void testEnabled_workingSet() throws Exception {
		IWorkingSet ws= PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSet("Test", new IAdaptable[] {});
		try {
			verifyEnabled(new IResource[0], new IJavaElement[] {fts.getProject()}, ws);
		} finally {
			PlatformUI.getWorkbench().getWorkingSetManager().removeWorkingSet(ws);
		}
	}

	private void compareContents(String cuName) throws JavaModelException, IOException {
		assertEqualLines(cuName, getFileContents(getOutputTestFileName(cuName)), getPackageP().getCompilationUnit(cuName + ".java").getSource());
	}

	private void delete(ICompilationUnit cu) throws Exception {
		try {
			performDummySearch();
			cu.delete(true, new NullProgressMonitor());
		} catch (JavaModelException e) {
			e.printStackTrace();
			//ingore and keep going
		}
	}

	@Test
	public void test0() throws Exception{
		if (true) {
			printTestDisabledMessage("not implemented yet");
			return;
		}

		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");

		try {
			IType typeA= cuA.getType("A");
			IType typeB= cuB.getType("B");

			assertTrue("A does not exist", typeA.exists());
			assertTrue("B does not exist", typeB.exists());

			IJavaElement[] copyJavaElements= {typeA};
			IResource[] copyResources= {};
			IJavaElement[] pasteJavaElements= {typeB};
			IResource[] pasteResources= {};
			PasteAction paste= verifyEnabled(copyResources, copyJavaElements, pasteResources, pasteJavaElements);
			paste.run((IStructuredSelection)paste.getSelection());
			compareContents("A");
			compareContents("B");
		} finally{
			delete(cuA);
			delete(cuB);
		}
	}

	@Test
	public void test2() throws Exception{
		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");

		try {
			IField fieldY= cuA.getType("A").getField("y");
			IType typeB= cuB.getType("B");

			assertTrue("y does not exist", fieldY.exists());
			assertTrue("B does not exist", typeB.exists());

			IJavaElement[] copyJavaElements= {fieldY};
			IResource[] copyResources= {};
			IJavaElement[] pasteJavaElements= {typeB};
			IResource[] pasteResources= {};
			PasteAction paste= verifyEnabled(copyResources, copyJavaElements, pasteResources, pasteJavaElements);
			paste.run((IStructuredSelection)paste.getSelection());
			compareContents("A");
			compareContents("B");
		} finally{
			delete(cuA);
			delete(cuB);
		}
	}

	@Test
	public void test3() throws Exception{
//		printTestDisabledMessage("test for bug#19007");
		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
		ICompilationUnit cuB= createCUfromTestFile(getPackageP(), "B");

		try {
			IJavaElement elem0= cuA.getImport("java.lang.*");
			IImportContainer importContainer= cuB.getImportContainer();

			assertTrue("y does not exist", elem0.exists());
			assertTrue("B does not exist", importContainer.exists());

			IJavaElement[] copyJavaElements= {elem0};
			IResource[] copyResources= {};
			IJavaElement[] pasteJavaElements= {importContainer};
			IResource[] pasteResources= {};
			PasteAction paste= verifyEnabled(copyResources, copyJavaElements, pasteResources, pasteJavaElements);
			paste.run((IStructuredSelection)paste.getSelection());
			compareContents("A");
			compareContents("B");
		} finally{
			delete(cuA);
			delete(cuB);
		}
	}

	@Test
	public void test4() throws Exception{
//		printTestDisabledMessage("test for bug 20151");
		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
		try {
			IJavaElement elem0= cuA.getType("A").getMethod("f", new String[0]);
			IMethod method= cuA.getType("A").getMethod("f1", new String[0]);

			assertTrue("y does not exist", elem0.exists());
			assertTrue("B does not exist", method.exists());

			IJavaElement[] copyJavaElements= {elem0};
			IResource[] copyResources= {};
			IJavaElement[] pasteJavaElements= {method};
			IResource[] pasteResources= {};
			PasteAction paste= verifyEnabled(copyResources, copyJavaElements, pasteResources, pasteJavaElements);
			paste.run((IStructuredSelection)paste.getSelection());
			compareContents("A");
		} finally{
			delete(cuA);
		}
	}

	@Test
	public void testPastingJavaElementIntoWorkingSet() throws Exception {
		IWorkingSet ws= PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSet("Test", new IAdaptable[] {});
		try {
			IResource[] resources= {};
			IJavaElement[] jElements= {fts.getProject()};
			PasteAction paste= verifyEnabled(resources , jElements, ws);
			paste.run((IStructuredSelection)paste.getSelection());
			assertEquals("Only one element", 1, ws.getElements().length);
			assertEquals(fts.getProject(), ws.getElements()[0]);
		} finally {
			PlatformUI.getWorkbench().getWorkingSetManager().removeWorkingSet(ws);
		}
	}

	@Test
	public void testPastingResourceIntoWorkingSet() throws Exception {
		IWorkingSet ws= PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSet("Test", new IAdaptable[] {});
		IFolder folder= fts.getProject().getProject().getFolder("folder");
		folder.create(true, true, null);
		try {
			IResource[] resources= {folder};
			IJavaElement[] jElements= {};
			PasteAction paste= verifyEnabled(resources , jElements, ws);
			paste.run((IStructuredSelection)paste.getSelection());
			assertEquals("Only one element", 1, ws.getElements().length);
			assertEquals(folder, ws.getElements()[0]);
		} finally {
			performDummySearch();
			PlatformUI.getWorkbench().getWorkingSetManager().removeWorkingSet(ws);
			folder.delete(true, false, null);
		}
	}

	@Test
	public void testPastingJavaElementAsResourceIntoWorkingSet() throws Exception {
		IWorkingSet ws= PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSet("Test", new IAdaptable[] {});
		try {
			IResource[] resources= {fts.getProject().getProject()};
			IJavaElement[] jElements= {};
			PasteAction paste= verifyEnabled(resources , jElements, ws);
			paste.run((IStructuredSelection)paste.getSelection());
			assertEquals("Only one element", 1, ws.getElements().length);
			assertEquals(fts.getProject(), ws.getElements()[0]);
		} finally {
			PlatformUI.getWorkbench().getWorkingSetManager().removeWorkingSet(ws);
		}
	}

	@Test
	public void testPastingExistingElementIntoWorkingSet() throws Exception {
		IWorkingSet ws= PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSet("Test",
			new IAdaptable[] {fts.getProject()});
		try {
			IResource[] resources= {};
			IJavaElement[] jElements= {fts.getProject()};
			PasteAction paste= verifyEnabled(resources , jElements, ws);
			paste.run((IStructuredSelection)paste.getSelection());
			assertEquals("Only one element", 1, ws.getElements().length);
			assertEquals(fts.getProject(), ws.getElements()[0]);
		} finally {
			PlatformUI.getWorkbench().getWorkingSetManager().removeWorkingSet(ws);
		}
	}

	@Test
	public void testPastingChildJavaElementIntoWorkingSet() throws Exception {
		IWorkingSet ws= PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSet("Test",
			new IAdaptable[] {fts.getProject()});
		try {
			IResource[] resources= {};
			IJavaElement[] jElements= {getPackageP()};
			PasteAction paste= verifyEnabled(resources , jElements, ws);
			paste.run((IStructuredSelection)paste.getSelection());
			assertEquals("Only one element", 1, ws.getElements().length);
			assertEquals(fts.getProject(), ws.getElements()[0]);
		} finally {
			PlatformUI.getWorkbench().getWorkingSetManager().removeWorkingSet(ws);
		}
	}

	@Test
	public void testPastingChildResourceIntoWorkingSet() throws Exception {
		IFolder folder= fts.getProject().getProject().getFolder("folder");
		folder.create(true, true, null);
		IWorkingSet ws= PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSet("Test",
			new IAdaptable[] {folder});
		IFolder sub= folder.getFolder("sub");
		sub.create(true, true, null);
		try {
			IResource[] resources= {sub};
			IJavaElement[] jElements= {};
			PasteAction paste= verifyEnabled(resources , jElements, ws);
			paste.run((IStructuredSelection)paste.getSelection());
			assertEquals("Only one element", 1, ws.getElements().length);
			assertEquals(folder, ws.getElements()[0]);
		} finally {
			performDummySearch();
			folder.delete(true, false, null);
			sub.delete(true, false, null);
			PlatformUI.getWorkbench().getWorkingSetManager().removeWorkingSet(ws);
		}
	}

	@Test
	public void testPastingChildResourceIntoWorkingSetContainingParent() throws Exception {
		IWorkingSet ws= PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSet("Test",
			new IAdaptable[] {fts.getProject()});
		IFolder folder= fts.getProject().getProject().getFolder("folder");
		folder.create(true, true, null);
		try {
			IResource[] resources= {folder};
			IJavaElement[] jElements= {};
			PasteAction paste= verifyEnabled(resources , jElements, ws);
			paste.run((IStructuredSelection)paste.getSelection());
			assertEquals("Only one element", 1, ws.getElements().length);
			assertEquals(fts.getProject(), ws.getElements()[0]);
		} finally {
			performDummySearch();
			folder.delete(true, false, null);
			PlatformUI.getWorkbench().getWorkingSetManager().removeWorkingSet(ws);
		}
	}

	private void setClipboardContents(TypedSource[] typedSources, int repeat) {
		final int maxRepeat= 10;
		try {
			_clipboard.setContents(new Object[] {typedSources}, new Transfer[] {TypedSourceTransfer.getInstance()});
		} catch (SWTError e) {
			if (e.code != DND.ERROR_CANNOT_SET_CLIPBOARD || repeat >= maxRepeat)
				throw e;
			setClipboardContents(typedSources, repeat+1);
		}
	}

	private void copyAndPasteTypedSources(IJavaElement[] elemsForClipboard, IJavaElement[] pasteSelectedJavaElements, boolean pasteEnabled) throws CoreException {
		setClipboardContents(TypedSource.createTypedSources(elemsForClipboard), 0);
		PasteAction pasteAction= new PasteAction(new MockWorkbenchSite(pasteSelectedJavaElements), _clipboard);
		pasteAction.update(pasteAction.getSelection());
		assertEquals("action enablement", pasteEnabled, pasteAction.isEnabled());
		if (pasteEnabled)
			pasteAction.run((IStructuredSelection)pasteAction.getSelection());
	}

	@Test
	public void testPastingTypedResources0() throws Exception {
		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
		try {
			IJavaElement methodM= cuA.getType("A").getMethod("m", new String[0]);
			IJavaElement[] elemsForClipboard= {methodM};
			IJavaElement[] pasteSelectedJavaElements= {methodM};
			boolean enabled= true;
			copyAndPasteTypedSources(elemsForClipboard, pasteSelectedJavaElements, enabled);
			compareContents("A");
		} finally{
			delete(cuA);
		}
	}

	@Test
	public void testPastingTypedResources1() throws Exception {
		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
		try {
			IType typeA= cuA.getType("A");
			IJavaElement fieldF= typeA.getField("f");
			IJavaElement[] elemsForClipboard= {fieldF};
			IJavaElement[] pasteSelectedJavaElements= {typeA};
			boolean enabled= true;
			copyAndPasteTypedSources(elemsForClipboard, pasteSelectedJavaElements, enabled);
			compareContents("A");
		} finally{
			delete(cuA);
		}
	}

	@Test
	public void testPastingTypedResources2() throws Exception {
		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
		try {
			IType typeA= cuA.getType("A");
			IJavaElement fieldF= typeA.getField("f");
			IJavaElement[] elemsForClipboard= {fieldF};
			IJavaElement[] pasteSelectedJavaElements= {typeA};
			boolean enabled= true;
			copyAndPasteTypedSources(elemsForClipboard, pasteSelectedJavaElements, enabled);
			compareContents("A");
		} finally{
			delete(cuA);
		}
	}

	@Test
	public void testPastingTypedResources3() throws Exception {
		ICompilationUnit cuA= createCUfromTestFile(getPackageP(), "A");
		try {
			IType typeA= cuA.getType("A");
			IJavaElement fieldF= typeA.getField("f");
			IJavaElement fieldG= typeA.getField("g");
			IJavaElement[] elemsForClipboard= {fieldF, fieldG};
			IJavaElement[] pasteSelectedJavaElements= {typeA};
			boolean enabled= true;
			copyAndPasteTypedSources(elemsForClipboard, pasteSelectedJavaElements, enabled);
			compareContents("A");
		} finally{
			delete(cuA);
		}
	}
}
