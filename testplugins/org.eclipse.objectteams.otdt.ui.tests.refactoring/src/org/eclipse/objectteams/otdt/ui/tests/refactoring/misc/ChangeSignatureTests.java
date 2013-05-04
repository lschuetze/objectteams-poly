/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 GK Software AG
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
package org.eclipse.objectteams.otdt.ui.tests.refactoring.misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.core.refactoring.descriptors.RefactoringSignatureDescriptorFactory;
import org.eclipse.jdt.internal.corext.refactoring.ParameterInfo;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringAvailabilityTester;
import org.eclipse.jdt.internal.corext.refactoring.structure.ChangeSignatureProcessor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.objectteams.otdt.ui.tests.refactoring.MySetup;
import org.eclipse.objectteams.otdt.ui.tests.refactoring.RefactoringTest;

/**
 * Some helper methods in this class have been inspired by the test class
 * <code>ChangeSignatureTests</code> in the test suite
 * <code>org.eclipse.jdt.ui.tests.refactoring</code> provided by Eclipse JDT.
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public class ChangeSignatureTests extends RefactoringTest {
	
    private static final String REFACTORING_PATH = "ChangeSignature/";

	public ChangeSignatureTests(String name) {
		super(name);
	}

    public static Test suite()
    {
        return new MySetup(new TestSuite(ChangeSignatureTests.class));
    }

    public static Test setUpTest(Test test)
    {
        return new MySetup(test);
    }

    protected String getRefactoringPath()
    {
        return REFACTORING_PATH;
    }

    private Refactoring createRefactoring(ChangeSignatureProcessor processor)
    {
        return new ProcessorBasedRefactoring(processor);
    }

	private ChangeSignatureProcessor createProcessor(IMethod method) throws JavaModelException
    {
        return new ChangeSignatureProcessor(method);
    }


	private String getSimpleTestFileName(boolean canReorder, boolean input){
		String fileName = "A_" + getName();
		if (canReorder)
			fileName += input ? "_in": "_out";
		return fileName + ".java";
	}

	private String getTestFileName(boolean canReorder, boolean input){
		String fileName= getTestFolderPath(canReorder);
		return fileName + getSimpleTestFileName(canReorder, input);
	}

	private String getTestFolderPath(boolean canModify) {
		String fileName= TEST_PATH_PREFIX + getRefactoringPath();
		fileName += (canModify ? "canModify/": "cannotModify/");
		return fileName;
	}

	//---helpers

	protected ICompilationUnit createCUfromTestFile(IPackageFragment pack, boolean canRename, boolean input) throws Exception {
		return createCU(pack, getSimpleTestFileName(canRename, input), getFileContents(getTestFileName(canRename, input)));
	}

	static ParameterInfo[] createNewParamInfos(String[] newTypes, String[] newNames, String[] newDefaultValues) {
		if (newTypes == null)
			return new ParameterInfo[0];
		ParameterInfo[] result= new ParameterInfo[newTypes.length];
		for (int i= 0; i < newDefaultValues.length; i++) {
			result[i]= ParameterInfo.createInfoForAddedParameter(newTypes[i], newNames[i], newDefaultValues[i]);
		}
		return result;
	}

	static void addInfos(List list, ParameterInfo[] newParamInfos, int[] newIndices) {
		if (newParamInfos == null || newIndices == null)
			return;
		for (int i= newIndices.length - 1; i >= 0; i--) {
			list.add(newIndices[i], newParamInfos[i]);
		}
	}

	private void helperAdd(String[] signature, ParameterInfo[] newParamInfos, int[] newIndices, boolean createDelegate, String expectedInfo) throws Exception {
		ICompilationUnit cu= createCUfromTestFile(getPackageP(), true, true);
		IType classA= getType(cu, "A");
		IMethod method = classA.getMethod("m", signature);
		assertTrue("method does not exist", method.exists());
		assertTrue("refactoring not available", RefactoringAvailabilityTester.isChangeSignatureAvailable(method));

		ChangeSignatureProcessor processor= new ChangeSignatureProcessor(method);
		Refactoring ref= new ProcessorBasedRefactoring(processor);

		processor.setDelegateUpdating(createDelegate);
		addInfos(processor.getParameterInfos(), newParamInfos, newIndices);
		RefactoringStatus initialConditions= ref.checkInitialConditions(new NullProgressMonitor());
		assertTrue("precondition was supposed to pass:"+initialConditions.getEntryWithHighestSeverity(), initialConditions.isOK());
		RefactoringStatus result= performRefactoring(ref, true);
		if (expectedInfo != null) {
			assertTrue("precondition was supposed to create an info:"+result.getEntryWithHighestSeverity(), result.hasInfo());
			assertEquals("wrong info", expectedInfo, result.getEntryMatchingSeverity(RefactoringStatus.INFO).getMessage());
		} else {
			assertEquals("precondition was supposed to pass", null, result);
		}

		IPackageFragment pack= (IPackageFragment)cu.getParent();
		String newCuName= getSimpleTestFileName(true, true);
		ICompilationUnit newcu= pack.getCompilationUnit(newCuName);
		assertTrue(newCuName + " does not exist", newcu.exists());
		String expectedFileContents= getFileContents(getTestFileName(true, false));
		assertEqualLines("invalid renaming", expectedFileContents, newcu.getSource());

//		assertParticipant(classA);
	}

	private void helperDelete(String[] signature, int[] deleteIndices, boolean createDelegate, String expectedInfo) throws Exception {
		ICompilationUnit cu= createCUfromTestFile(getPackageP(), true, true);
		IType classA= getType(cu, "A");
		helperDelete(cu, classA, signature, deleteIndices,
				createDelegate, expectedInfo);
	}

	void helperDelete(ICompilationUnit cu, IType declaringClass, String[] signature, int[] deleteIndices, boolean createDelegate, String expectedInfo)
			  throws JavaModelException, CoreException, Exception, IOException 
	{
		IMethod method = declaringClass.getMethod("m", signature);
		assertTrue("method does not exist", method.exists());
		assertTrue("refactoring not available", RefactoringAvailabilityTester.isChangeSignatureAvailable(method));

		ChangeSignatureProcessor processor= new ChangeSignatureProcessor(method);
		Refactoring ref= new ProcessorBasedRefactoring(processor);

		processor.setDelegateUpdating(createDelegate);
		markAsDeleted(processor.getParameterInfos(), deleteIndices);
		RefactoringStatus initialConditions= ref.checkInitialConditions(new NullProgressMonitor());
		assertTrue("precondition was supposed to pass:"+initialConditions.getEntryWithHighestSeverity(), initialConditions.isOK());
		RefactoringStatus result= performRefactoring(ref, true);
		if (expectedInfo != null) {
			assertTrue("precondition was supposed to create an info:"+result.getEntryWithHighestSeverity(), result.hasInfo());
			assertEquals("wrong info", expectedInfo, result.getEntryMatchingSeverity(RefactoringStatus.INFO).getMessage());
		} else {
			assertEquals("precondition was supposed to pass", null, result);
		}

		IPackageFragment pack= (IPackageFragment)cu.getParent();
		String newCuName= getSimpleTestFileName(true, true);
		ICompilationUnit newcu= pack.getCompilationUnit(newCuName);
		assertTrue(newCuName + " does not exist", newcu.exists());
		String expectedFileContents= getFileContents(getTestFileName(true, false));
		assertEqualLines("invalid renaming", expectedFileContents, newcu.getSource());

//		assertParticipant(classA);
	}

	void helperRename(ICompilationUnit cu, IType declaringClass, String[] signature, int idx, String oldName, String newName, String expectedInfo)
			  throws JavaModelException, CoreException, Exception, IOException 
	{
		IMethod method = declaringClass.getMethod("m", signature);
		assertTrue("method does not exist", method.exists());
		assertTrue("refactoring not available", RefactoringAvailabilityTester.isChangeSignatureAvailable(method));

		ChangeSignatureProcessor processor= new ChangeSignatureProcessor(method);
		Refactoring ref= new ProcessorBasedRefactoring(processor);

		processor.setDelegateUpdating(false);
		markAsRenamed(processor.getParameterInfos().get(idx), oldName, newName);
		RefactoringStatus initialConditions= ref.checkInitialConditions(new NullProgressMonitor());
		assertTrue("precondition was supposed to pass:"+initialConditions.getEntryWithHighestSeverity(), initialConditions.isOK());
		RefactoringStatus result= performRefactoring(ref, true);
		if (expectedInfo != null) {
			assertTrue("precondition was supposed to create an info:"+result.getEntryWithHighestSeverity(), result.hasInfo());
			assertEquals("wrong info", expectedInfo, result.getEntryMatchingSeverity(RefactoringStatus.INFO).getMessage());
		} else {
			assertEquals("precondition was supposed to pass", null, result);
		}

		IPackageFragment pack= (IPackageFragment)cu.getParent();
		String newCuName= getSimpleTestFileName(true, true);
		ICompilationUnit newcu= pack.getCompilationUnit(newCuName);
		assertTrue(newCuName + " does not exist", newcu.exists());
		String expectedFileContents= getFileContents(getTestFileName(true, false));
		assertEqualLines("invalid content", expectedFileContents, newcu.getSource());
	}

	// from RenameTempTests with adaptation:
	private void helperRegularRename(String newName, boolean updateReferences, IJavaElement element, ICompilationUnit cu,
			// new for OT: don't use automatic outfile name
			String outFileName) 
					throws Exception
	{
		assertTrue(element.getClass().toString(), element instanceof ILocalVariable);

		final RenameJavaElementDescriptor descriptor= RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_LOCAL_VARIABLE);
		descriptor.setJavaElement(element);
		descriptor.setNewName(newName);
		descriptor.setUpdateReferences(updateReferences);

		final RefactoringStatus status= new RefactoringStatus();
		final Refactoring refactoring= descriptor.createRefactoring(status);
		assertTrue("status should be ok", status.isOK());
		assertNotNull("refactoring should not be null", refactoring);

		RefactoringStatus result= performRefactoring(refactoring);
		assertEquals("precondition was supposed to pass", null, result);

		IPackageFragment pack= (IPackageFragment) cu.getParent();
		String newCuName= getSimpleTestFileName(true, true);
		ICompilationUnit newcu= pack.getCompilationUnit(newCuName);
		assertTrue(newCuName + " does not exist", newcu.exists());
		assertEqualLines("incorrect renaming", getFileContents(outFileName), newcu.getSource());
	}

	private void markAsRenamed(ParameterInfo parameterInfo, String oldName, String newName) {
		assertEquals(parameterInfo.getOldName(), oldName);
		parameterInfo.setNewName(newName);
	}

	private void helperPermute(String[] newOrder, String[] signature, boolean createDelegate) throws Exception{
		ICompilationUnit cu= createCUfromTestFile(getPackageP(), true, true);
		IType classA= getType(cu, "A");
		IMethod method = classA.getMethod("m", signature);
		assertTrue("method does not exist", method.exists());
		assertTrue("refactoring not available", RefactoringAvailabilityTester.isChangeSignatureAvailable(method));

		ChangeSignatureProcessor processor= new ChangeSignatureProcessor(method);
		Refactoring ref= new ProcessorBasedRefactoring(processor);

		processor.setDelegateUpdating(createDelegate);
		permute(processor.getParameterInfos(), newOrder);
		ref.checkInitialConditions(new NullProgressMonitor());
		RefactoringStatus result= performRefactoring(ref, true);
		assertEquals("precondition was supposed to pass", null, result);

		IPackageFragment pack= (IPackageFragment)cu.getParent();
		String newCuName= getSimpleTestFileName(true, true);
		ICompilationUnit newcu= pack.getCompilationUnit(newCuName);
		assertTrue(newCuName + " does not exist", newcu.exists());
		String expectedFileContents= getFileContents(getTestFileName(true, false));
//		assertEquals("invalid renaming", expectedFileContents, newcu.getSource());
		assertEqualLines(expectedFileContents, newcu.getSource());

//		assertParticipant(classA);
	}

	private void permute(List infos, String[] newOrder) {
		int[] permutation= createPermutation(infos, newOrder);
		List swapped= new ArrayList(infos.size());
		ParameterInfo[] newInfos= new  ParameterInfo[infos.size()];
		for (int i= 0; i < permutation.length; i++) {
			newInfos[i]= (ParameterInfo)infos.get(permutation[i]);
		}
		infos.clear();
		for (int i= 0; i < newInfos.length; i++) {
			infos.add(newInfos[i]);
		}
	}

	private static int[] createPermutation(List infos, String[] newOrder) {
		int[] result= new int[infos.size()];
		for (int i= 0; i < result.length; i++) {
			result[i]= indexOfOldName(infos, newOrder[i]);
		}
		return result;
	}

	private static int indexOfOldName(List infos, String string) {
		for (Iterator iter= infos.iterator(); iter.hasNext();) {
			ParameterInfo info= (ParameterInfo) iter.next();
			if (info.getOldName().equals(string))
				return infos.indexOf(info);
		}
		assertTrue(false);
		return -1;
	}

	private void markAsDeleted(List list, int[] deleted) {
		if (deleted == null)
			return;
		for (int i= 0; i < deleted.length; i++) {
			((ParameterInfo)list.get(deleted[i])).markAsDeleted();
		}
	}

	// add argument at base side of callin
	public void testAdd01()throws Exception{
		String[] signature= {"I"};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		ParameterInfo[] newParamInfo= createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {1};
		helperAdd(signature, newParamInfo, newIndices, true/*delegate*/, null/*expectInfo*/);
	}

	// add argument at role side (inherited from non-role) of callout
	public void testAdd02()throws Exception{
		String[] signature= {"I"};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		ParameterInfo[] newParamInfo= createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {1};
		helperAdd(signature, newParamInfo, newIndices, false/*delegate*/, null/*expectInfo*/);
	}

	// add argument at role side of callin
	public void testAdd03()throws Exception{
		String[] signature= {"I"};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		ParameterInfo[] newParamInfo= createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {1};
		helperAdd(signature, newParamInfo, newIndices, false/*delegate*/, "Adding arguments at the role side of a callin binding is not fully supported. Please manually update transitive references."/*expectedInfo*/);
	}

	// add argument at base side of callout
	public void testAdd04()throws Exception{
		String[] signature= {"I"};
		String[] newNames= {"x"};
		String[] newTypes= {"int"};
		String[] newDefaultValues= {"0"};
		ParameterInfo[] newParamInfo= createNewParamInfos(newTypes, newNames, newDefaultValues);
		int[] newIndices= {1};
		helperAdd(signature, newParamInfo, newIndices, false/*delegate*/, "Adding arguments at the base side of a callout binding is not fully supported. Please manually update transitive references."/*expectedInfo*/);
	}

	// delete argument at base side of callin
	public void testDelete01()throws Exception{
		int[] deleteIndices= {1};
		helperDelete(new String[]{"I", "I"}, deleteIndices, true/*delegate*/, "Deleting arguments at the base side of a callin binding is not fully supported. Please manually update transitive references."/*expectInfo*/);
	}

	// delete argument at role side (inherited from non-role) of callout
	public void testDelete02()throws Exception{
		int[] deleteIndices= {1};
		helperDelete(new String[]{"I", "I"}, deleteIndices, false/*delegate*/, "Deleting arguments at the role side of a callout binding is not fully supported. Please manually update transitive references."/*expectInfo*/);
	}

	// delete argument in role method bound in signature-less callin
	public void testDelete03()throws Exception{
		ICompilationUnit cu= createCUfromTestFile(getPackageP(), true, true);
		IType roleR= getType(cu, "MyTeam").getType("R");
		int[] deleteIndices= {1};
		helperDelete(cu, roleR, new String[]{"I", "I"}, deleteIndices, false/*delegate*/, "Affected method binding has no signatures; consider adding signatures first so that the refactoring can absorb incompatible changes using parameter mappings"/*expectInfo*/);
	}

	public void testReorder01() throws Exception {
		helperPermute(new String[]{"ignore", "b", "a"}, new String[]{"I", "Z", "QString;"}, false);
	}

	public void testRename01() throws Exception {
		ICompilationUnit cu= createCUfromTestFile(getPackageP(), true, true);
		IType teamType = cu.getType("MyTeam");
		helperRename(cu, teamType, new String[]{"QA;"}, 0, "arg", "renamed", null);
	}

	// simulate performing the same operation as testRename01() triggered by Shift-Alt-R: 
	public void testRename01a() throws Exception {
		boolean canRename = true;
		String simpleTestFileName = "A_testRename01_in.java";
		String simpleOutFileName = "A_testRename01_out.java";
		String testFolder = getTestFolderPath(canRename);
		ICompilationUnit cu= createCU(getPackageP(), getSimpleTestFileName(canRename, true), getFileContents(testFolder+simpleTestFileName));
		IType teamType = cu.getType("MyTeam");
		IMethod method = teamType.getMethod("m", new String[]{"QA;"});
		ILocalVariable argument = method.getParameters()[0];
		helperRegularRename("renamed", true, argument, cu, testFolder+simpleOutFileName);
	}
}
