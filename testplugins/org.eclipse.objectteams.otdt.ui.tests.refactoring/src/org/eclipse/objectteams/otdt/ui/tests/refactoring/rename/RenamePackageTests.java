/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *		IBM Corporation - initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 *******************************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.refactoring.rename;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.core.refactoring.descriptors.RefactoringSignatureDescriptorFactory;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenamePackageProcessor;
import org.eclipse.jdt.ui.tests.refactoring.ParticipantTesting;
import org.eclipse.jdt.ui.tests.refactoring.infra.DebugUtils;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveArguments;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameArguments;
import org.eclipse.objectteams.otdt.ui.tests.refactoring.MySetup;
import org.eclipse.objectteams.otdt.ui.tests.refactoring.RefactoringTest;

/**
 * Based on {@link org.eclipse.jdt.ui.tests.refactoring.RenamePackageTests} and adapted.
 */
@SuppressWarnings({"restriction", "rawtypes", "unchecked"})
public class RenamePackageTests extends RefactoringTest {

	private static final Class clazz= RenamePackageTests.class;
	private static final String REFACTORING_PATH= "RenamePackage/";

	private boolean fUpdateReferences;
	private boolean fUpdateTextualMatches;
	private String fQualifiedNamesFilePatterns;
	private boolean fRenameSubpackages;

	public RenamePackageTests(String name) {
		super(name);
	}

	public static Test suite() {
		return new MySetup(new TestSuite(clazz));
	}

	public static Test setUpTest(Test someTest) {
		return new MySetup(someTest);
	}

	protected void setUp() throws Exception {
		super.setUp();
		fUpdateReferences= true;
		fUpdateTextualMatches= false;
		fQualifiedNamesFilePatterns= null;
		fRenameSubpackages= false;
		// fIsPreDeltaTest= true;
	}

	protected String getRefactoringPath() {
		return REFACTORING_PATH;
	}

	// -------------
	private RenameJavaElementDescriptor createRefactoringDescriptor(IPackageFragment pack, String newName) {
		RenameJavaElementDescriptor descriptor= RefactoringSignatureDescriptorFactory.createRenameJavaElementDescriptor(IJavaRefactorings.RENAME_PACKAGE);
		descriptor.setJavaElement(pack);
		descriptor.setNewName(newName);
		descriptor.setUpdateReferences(true);
		return descriptor;
	}

	/* non java-doc
	 * the 0th one is the one to rename
	 */
	@SuppressWarnings("unused")
	private void helper1(String packageNames[], String[][] packageFiles, String newPackageName) throws Exception{
		IPackageFragment[] packages= new IPackageFragment[packageNames.length];
		for (int i= 0; i < packageFiles.length; i++){
			packages[i]= getRoot().createPackageFragment(packageNames[i], true, null);
			for (int j= 0; j < packageFiles[i].length; j++){
				createCUfromTestFile(packages[i], packageFiles[i][j], packageNames[i].replace('.', '/') + "/");
				//DebugUtils.dump(cu.getElementName() + "\n" + cu.getSource());
			}
		}
		IPackageFragment thisPackage= packages[0];
		RefactoringStatus result= performRefactoring(createRefactoringDescriptor(thisPackage, newPackageName));
		assertNotNull("precondition was supposed to fail", result);
		if (_isVerbose)
			DebugUtils.dump("" + result);
	}

	private RenamePackageProcessor helper2(String[] packageNames, String[][] packageFileNames, String newPackageName) throws Exception{
			ParticipantTesting.reset();
			IPackageFragment[] packages= new IPackageFragment[packageNames.length];
			ICompilationUnit[][] cus= new ICompilationUnit[packageFileNames.length][packageFileNames[0].length];
			for (int i= 0; i < packageNames.length; i++){
				packages[i]= getRoot().createPackageFragment(packageNames[i], true, null);
				for (int j= 0; j < packageFileNames[i].length; j++){
					cus[i][j]= createCUfromTestFile(packages[i], packageFileNames[i][j], packageNames[i].replace('.', '/') + "/");
				}
			}
			IPackageFragment thisPackage= packages[0];
			boolean hasSubpackages= thisPackage.hasSubpackages();

			IPath path= thisPackage.getParent().getPath();
			path= path.append(newPackageName.replace('.', '/'));
			IFolder target= ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
			boolean targetExists= target.exists();
			boolean isRename= !targetExists && !thisPackage.hasSubpackages() && thisPackage.getResource().getParent().equals(target.getParent());

			String[] createHandles= null;
			String[] moveHandles= null;
			String[] deleteHandles= null;
			boolean doDelete= true;

			String[] renameHandles= null;
			if (isRename) {
				renameHandles= ParticipantTesting.createHandles(thisPackage, thisPackage.getResource());
			} else {
				renameHandles= ParticipantTesting.createHandles(thisPackage);
				IContainer loop= target;
				List handles= new ArrayList();
				while (loop != null && !loop.exists()) {
					handles.add(ParticipantTesting.createHandles(loop)[0]);
					loop= loop.getParent();
				}
				createHandles= (String[]) handles.toArray(new String[handles.size()]);
				IFolder source= (IFolder)thisPackage.getResource();
				deleteHandles= ParticipantTesting.createHandles(source);
				IResource members[]= source.members();
				List movedObjects= new ArrayList();
				for (int i= 0; i < members.length; i++) {
					if (members[i] instanceof IFolder) {
						doDelete= false;
					} else {
						movedObjects.add(members[i]);
					}
				}
				moveHandles= ParticipantTesting.createHandles(movedObjects.toArray());
			}
			RenameJavaElementDescriptor descriptor= createRefactoringDescriptor(thisPackage, newPackageName);
			descriptor.setUpdateReferences(fUpdateReferences);
			descriptor.setUpdateTextualOccurrences(fUpdateTextualMatches);
			setFilePatterns(descriptor);
			Refactoring refactoring= createRefactoring(descriptor);
			RefactoringStatus result= performRefactoring(refactoring);
			assertEquals("preconditions were supposed to pass", null, result);

			if (isRename) {
				ParticipantTesting.testRename(renameHandles,
					new RenameArguments[] {
						new RenameArguments(newPackageName, fUpdateReferences),
						new RenameArguments(target.getName(), fUpdateReferences)
					}
				);
			} else {
				ParticipantTesting.testRename(renameHandles,
					new RenameArguments[] {
					new RenameArguments(newPackageName, fUpdateReferences)});

				ParticipantTesting.testCreate(createHandles);

				List args= new ArrayList();
				for (int i= 0; i < packageFileNames[0].length; i++) {
					args.add(new MoveArguments(target, fUpdateReferences));
				}
				ParticipantTesting.testMove(moveHandles, (MoveArguments[]) args.toArray(new MoveArguments[args.size()]));

				if (doDelete) {
					ParticipantTesting.testDelete(deleteHandles);
				} else {
					ParticipantTesting.testDelete(new String[0]);
				}
			}

			//---

			if (hasSubpackages) {
				assertTrue("old package does not exist anymore", getRoot().getPackageFragment(packageNames[0]).exists());
			} else {
				assertTrue("package not renamed", ! getRoot().getPackageFragment(packageNames[0]).exists());
			}
			IPackageFragment newPackage= getRoot().getPackageFragment(newPackageName);
			assertTrue("new package does not exist", newPackage.exists());

			for (int i= 0; i < packageFileNames.length; i++){
				String packageName= (i == 0)
								? newPackageName.replace('.', '/') + "/"
								: packageNames[i].replace('.', '/') + "/";
				for (int j= 0; j < packageFileNames[i].length; j++){
					String s1= getFileContents(getOutputTestFileName(packageFileNames[i][j], packageName));
					ICompilationUnit cu=
						(i == 0)
							? newPackage.getCompilationUnit(packageFileNames[i][j] + ".java")
							: cus[i][j];
					//DebugUtils.dump("cu:" + cu.getElementName());
					String s2= cu.getSource();

					//DebugUtils.dump("expected:" + s1);
					//DebugUtils.dump("was:" + s2);
					assertEqualLines("invalid update in file " + cu.getElementName(), s1,	s2);
				}
			}
			RefactoringProcessor processor= ((ProcessorBasedRefactoring) refactoring).getProcessor();
			return (RenamePackageProcessor) processor;
	}

	class PackageRename {
		String[] fPackageNames;
		final String[][] fPackageFileNames;
		final String fNewPackageName;
		final boolean fTestWithDummyFiles;

		final IPackageFragment[] fPackages;
		final ICompilationUnit[][] fCus;

		public PackageRename (String[] packageNames, String[][] packageFileNames, String newPackageName) throws Exception {
			this(packageNames, packageFileNames, newPackageName, false);
		}

		public PackageRename(String[] packageNames, String[][] packageFileNames, String newPackageName, boolean testWithDummyFiles) throws Exception {
			fPackageNames= packageNames;
			fPackageFileNames= packageFileNames;
			fNewPackageName= newPackageName;
			fTestWithDummyFiles= testWithDummyFiles;

			fPackages= new IPackageFragment[packageNames.length];
			fCus= new ICompilationUnit[packageFileNames.length][];
			for (int i= 0; i < packageFileNames.length; i++){
				fPackages[i]= getRoot().createPackageFragment(packageNames[i], true, null);
				fCus[i]= new ICompilationUnit[packageFileNames[i].length];
				for (int j= 0; j < packageFileNames[i].length; j++){
					if (testWithDummyFiles) {
						fCus[i][j]= createDummyCU(fPackages[i], packageFileNames[i][j]);
					} else {
						fCus[i][j]= createCUfromTestFile(fPackages[i], packageFileNames[i][j], packageNames[i].replace('.', '/') + "/");
					}
				}
			}
		}

		private ICompilationUnit createDummyCU(IPackageFragment packageFragment, String typeName) throws JavaModelException {
			String contents= getDummyContents(packageFragment.getElementName(), typeName);
			return packageFragment.createCompilationUnit(typeName + ".java", contents, true, null);
		}

		private String getDummyContents(String packName, String typeName) {
			StringBuffer contents= new StringBuffer();
			if (packName.length() != 0)
				contents.append("package ").append(packName).append(";\n");
			contents.append("public class ").append(typeName).append(" { }\n");
			return contents.toString();
		}

		public void createAndPerform(int expectedSeverity) throws CoreException, Exception {
			IPackageFragment thisPackage= fPackages[0];
			RenameJavaElementDescriptor descriptor= createRefactoringDescriptor(thisPackage, fNewPackageName);
			descriptor.setUpdateReferences(fUpdateReferences);
			descriptor.setUpdateTextualOccurrences(fUpdateTextualMatches);
			setFilePatterns(descriptor);
			descriptor.setUpdateHierarchy(fRenameSubpackages);
			RefactoringStatus result= performRefactoring(descriptor);
			if (expectedSeverity == RefactoringStatus.OK)
				assertEquals("preconditions were supposed to pass", null, result);
			else
				assertEquals(expectedSeverity, result.getSeverity());
		}

		public void execute() throws Exception {
			createAndPerform(RefactoringStatus.OK);

			IPackageFragment oldPackage= getRoot().getPackageFragment(fPackageNames[0]);
			assertTrue("package not renamed: " + fPackageNames[0], ! oldPackage.exists());
			IPackageFragment newPackage= getRoot().getPackageFragment(fNewPackageName);
			assertTrue("new package does not exist", newPackage.exists());

			checkExpectedState();
		}

		public void checkExpectedState() throws IOException, JavaModelException {
			for (int i= 0; i < fPackageFileNames.length; i++){
				String packageName= getNewPackageName(fPackageNames[i]);
				String packagePath= packageName.replace('.', '/') + "/";

				for (int j= 0; j < fPackageFileNames[i].length; j++){
					String expected;
					if (fTestWithDummyFiles) {
						expected= getDummyContents(packageName, fPackageFileNames[i][j]);
					} else {
						expected= getFileContents(getOutputTestFileName(fPackageFileNames[i][j], packagePath));
					}
					ICompilationUnit cu= getRoot().getPackageFragment(packageName).getCompilationUnit(fPackageFileNames[i][j] + ".java");
					String actual= cu.getSource();
					assertEqualLines("invalid update in file " + cu.getElementName(), expected,	actual);
				}
			}
		}

		public String getNewPackageName(String oldPackageName) {
			if (oldPackageName.equals(fPackageNames[0]))
				return fNewPackageName;

			if (fRenameSubpackages && oldPackageName.startsWith(fPackageNames[0] + "."))
				return fNewPackageName + oldPackageName.substring(fPackageNames[0].length());

			return oldPackageName;
		}

		public void checkOriginalState() throws Exception {
			IJavaElement[] rootChildren= getRoot().getChildren();
			ArrayList existingPacks= new ArrayList();
			for (int i= 0; i < rootChildren.length; i++) {
				existingPacks.add(rootChildren[i].getElementName());
			}
			assertEqualSets(Arrays.asList(fPackageNames), existingPacks);

			for (int i= 0; i < fPackageFileNames.length; i++){
				String packageName= fPackageNames[i];
				String packagePath= packageName.replace('.', '/') + "/";
				IPackageFragment pack= getRoot().getPackageFragment(packageName);

				IJavaElement[] packChildren= pack.getChildren();
				ArrayList existingCUs= new ArrayList();
				for (int j= 0; j < packChildren.length; j++) {
					String cuName= packChildren[j].getElementName();
					existingCUs.add(cuName.substring(0, cuName.length() - 5));
				}
				assertEqualSets(Arrays.asList(fPackageFileNames[i]), existingCUs);

				for (int j= 0; j < fPackageFileNames[i].length; j++){
					String expected;
					if (fTestWithDummyFiles) {
						expected= getDummyContents(packageName, fPackageFileNames[i][j]);
					} else {
						expected= getFileContents(getInputTestFileName(fPackageFileNames[i][j], packagePath));
					}
					ICompilationUnit cu= pack.getCompilationUnit(fPackageFileNames[i][j] + ".java");
					String actual= cu.getSource();
					assertEqualLines("invalid undo in file " + cu.getElementName(), expected,	actual);
				}
			}

		}

		private void assertEqualSets(Collection expected, Collection actual) {
			HashSet expectedSet= new HashSet(expected);
			expectedSet.removeAll(actual);
			assertEquals("not all expected in actual", "[]", expectedSet.toString());

			HashSet actualSet= new HashSet(actual);
			actualSet.removeAll(expected);
			assertEquals("not all actual in expected", "[]", actualSet.toString());
		}
	}

	private void setFilePatterns(RenameJavaElementDescriptor descriptor) {
		descriptor.setUpdateQualifiedNames(fQualifiedNamesFilePatterns != null);
		if (fQualifiedNamesFilePatterns != null)
			descriptor.setFileNamePatterns(fQualifiedNamesFilePatterns);
	}

	// ---------- tests -------------

	public void testRenameBaseImportedPackage1() throws Exception {
		helper2(new String[]{"bases", "teams"}, new String[][]{{"MyClass"}, {"MyTeam"}}, "baseclasses");
	}
}
