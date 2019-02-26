/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010, 2016 Stephan Herrmann.
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
package org.eclipse.objectteams.otdt.ui.tests.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.ui.text.correction.proposals.NewCUUsingWizardProposal;
import org.eclipse.jdt.testplugin.JavaProjectHelper;
import org.eclipse.jdt.testplugin.TestOptions;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.correction.CUCorrectionProposal;
import org.eclipse.jdt.ui.text.java.correction.ChangeCorrectionProposal;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
import org.osgi.framework.Bundle;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Testing whether standard Java quickfixes work in OT/J code, too.
 * 
 * @author stephan
 * @since 0.7.0
 */
public class JavaQuickFixTests extends OTQuickFixTest {
	private static final Class THIS= JavaQuickFixTests.class;

	public JavaQuickFixTests(String name) {
		super(name);
	}

	public static Test allTests() {
		return setUpTest(new TestSuite(THIS));
	}
	
	public static Test suite() {
		return allTests();
	}

	public static Test setUpTest(Test test) {
		return new ProjectTestSetup(test);
	}
	
	private String ANNOTATION_JAR_PATH;

	void setupForNullAnnotations(boolean isPlainJava) throws IOException, JavaModelException {
		if (this.ANNOTATION_JAR_PATH == null) {
			String version= "[1.1.0,2.0.0)"; // tests run at 1.5, need the "old" null annotations
			Bundle[] bundles= Platform.getBundles("org.eclipse.jdt.annotation", version);
			File bundleFile= FileLocator.getBundleFile(bundles[0]);
			if (bundleFile.isDirectory())
				this.ANNOTATION_JAR_PATH= bundleFile.getPath() + "/bin";
			else
				this.ANNOTATION_JAR_PATH= bundleFile.getPath();
		}
		JavaProjectHelper.addLibrary(fJProject1, new Path(ANNOTATION_JAR_PATH));
		
		Hashtable<String, String> options= TestOptions.getDefaultOptions();
		
		options.put(OTDTPlugin.OT_COMPILER_PURE_JAVA, isPlainJava ? JavaCore.ENABLED : JavaCore.DISABLED);

		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, String.valueOf(99));
		options.put(JavaCore.COMPILER_PB_STATIC_ACCESS_RECEIVER, JavaCore.ERROR);
		options.put(JavaCore.COMPILER_PB_UNCHECKED_TYPE_OPERATION, JavaCore.IGNORE);
		options.put(JavaCore.COMPILER_PB_MISSING_HASHCODE_METHOD, JavaCore.WARNING);
		options.put(JavaCore.COMPILER_ANNOTATION_NULL_ANALYSIS, JavaCore.ENABLED);
		options.put(JavaCore.COMPILER_PB_NULL_SPECIFICATION_VIOLATION, JavaCore.ERROR);
		options.put(JavaCore.COMPILER_PB_NULL_REFERENCE, JavaCore.ERROR);
		options.put(JavaCore.COMPILER_PB_POTENTIAL_NULL_REFERENCE, JavaCore.WARNING);
		options.put(JavaCore.COMPILER_PB_NULL_ANNOTATION_INFERENCE_CONFLICT, JavaCore.WARNING);
		options.put(JavaCore.COMPILER_PB_NULL_UNCHECKED_CONVERSION, JavaCore.WARNING);
		options.put(JavaCore.COMPILER_PB_REDUNDANT_NULL_CHECK, JavaCore.WARNING);
		options.put(JavaCore.COMPILER_PB_NULL_UNCHECKED_CONVERSION, JavaCore.WARNING);

		JavaCore.setOptions(options);
	}

	// the following three test try to reproduce Bug 311890 -  [assist] support "create class" quickfix for OT/J references
	
	// create a class for an unresolved playedBy declaration
	public void testCreateClass1() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("    protected class R playedBy B {\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList<IJavaCompletionProposal> proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 6);
		// create class / interface / enum B
		// add type parameter B to R / T1
		// fix project setup
		assertCorrectLabels(proposals);

		for (Object prop : proposals) {
			if (prop instanceof NewCUUsingWizardProposal) {
				NewCUUsingWizardProposal proposal= (NewCUUsingWizardProposal)prop;
				if ("Create class 'B'".equals(proposal.getDisplayString())) {
					return; // done
				}
			}
		}
		fail("Expected proposal not found");
	}
	
	// create a class for an unresolved playedBy declaration in a nested role
	public void testCreateClass2() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("    protected team class Mid {\n");
		buf.append("    	protected class R playedBy B {\n");
		buf.append("    	}\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList<IJavaCompletionProposal> proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 7);
		// create class / interface / enum B
		// add type parameter B to R / Mid / T1
		// fix project setup
		assertCorrectLabels(proposals);

		for (Object prop : proposals) {
			if (prop instanceof NewCUUsingWizardProposal) {
				NewCUUsingWizardProposal proposal= (NewCUUsingWizardProposal)prop;
				if ("Create class 'B'".equals(proposal.getDisplayString())) {
					return; // done
				}
			}
		}
		fail("Expected proposal not found");
	}


	// create a class for an unresolved playedBy declaration
	public void testCreateClass3() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("    protected class R playedBy B {\n");
		buf.append("    }\n");
		buf.append("    public void foo(B as R o) {}\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList<IJavaCompletionProposal> proposals= collectCorrections(cu, astRoot, 2, 1, null);
		assertNumberOfProposals(proposals, 5);
		// create class / interface / enum B
		// add type parameter B to T1
		// fix project setup
		assertCorrectLabels(proposals);

		for (Object prop : proposals) {
			if (prop instanceof NewCUUsingWizardProposal) {
				NewCUUsingWizardProposal proposal= (NewCUUsingWizardProposal)prop;
				if ("Create class 'B'".equals(proposal.getDisplayString())) {
					return; // done
				}
			}
		}
		fail("Expected proposal not found");
	}
	
	// Bug 348574 - [quickfix] implement abstract methods from tsuper
	public void testAddAbstractMethods1() throws CoreException {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T2 extends T1 {\n");
		buf.append("  protected class R {\n");
		buf.append("  }\n");
		buf.append("}\n");
		ICompilationUnit cu1=pack1.createCompilationUnit("T2.java", buf.toString(), false, null);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("  abstract protected class R {\n");
		buf.append("      abstract void foo() {}\n");
		buf.append("  }\n");
		buf.append("}\n");
		pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu1);
		ArrayList<IJavaCompletionProposal> proposals1= collectCorrections(cu1, astRoot, 2, 0);
		assertNumberOfProposals(proposals1, 0);
		ArrayList<IJavaCompletionProposal> proposals2= collectCorrections(cu1, astRoot, 2, 1);
		assertNumberOfProposals(proposals2, 2);
		assertCorrectLabels(proposals1);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals2.get(0);
		String preview1= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T2 extends T1 {\n");
		buf.append("  protected class R {\n");
		buf.append("\n");
		buf.append("    @Override\n");
		buf.append("    void foo() {\n");
		buf.append("        // TODO Auto-generated method stub\n"); 
		buf.append("        \n");
		buf.append("    }\n");
		buf.append("  }\n");
		buf.append("}\n");
		String expected1= buf.toString();	


		proposal= (CUCorrectionProposal) proposals2.get(1);
		String preview2= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T2 extends T1 {\n");
		buf.append("  protected abstract class R {\n");
		buf.append("  }\n");
		buf.append("}\n");
		String expected2= buf.toString();		
		
		assertEqualStringsIgnoreOrder(new String[] { preview1, preview2 }, new String[] { expected1, expected2 });		
	}

	// Bug 348574 - [quickfix] implement abstract methods from tsuper
	// abstract static method
	public void testAddAbstractMethods2() throws CoreException {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T2 extends T1 {\n");
		buf.append("  protected class R {\n");
		buf.append("  }\n");
		buf.append("}\n");
		ICompilationUnit cu1=pack1.createCompilationUnit("T2.java", buf.toString(), false, null);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("  abstract protected class R {\n");
		buf.append("      abstract static void foo() {}\n");
		buf.append("  }\n");
		buf.append("}\n");
		pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu1);
		ArrayList<IJavaCompletionProposal> proposals1= collectCorrections(cu1, astRoot, 2, 0);
		assertNumberOfProposals(proposals1, 0);
		ArrayList<IJavaCompletionProposal> proposals2= collectCorrections(cu1, astRoot, 2, 1);
		assertNumberOfProposals(proposals2, 2);
		assertCorrectLabels(proposals1);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals2.get(0);
		String preview1= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T2 extends T1 {\n");
		buf.append("  protected class R {\n");
		buf.append("\n");
		buf.append("    @Override\n");
		buf.append("    static void foo() {\n");
		buf.append("        // TODO Auto-generated method stub\n"); 
		buf.append("        \n");
		buf.append("    }\n");
		buf.append("  }\n");
		buf.append("}\n");
		String expected1= buf.toString();	


		proposal= (CUCorrectionProposal) proposals2.get(1);
		String preview2= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T2 extends T1 {\n");
		buf.append("  protected abstract class R {\n");
		buf.append("  }\n");
		buf.append("}\n");
		String expected2= buf.toString();		
		
		assertEqualStringsIgnoreOrder(new String[] { preview1, preview2 }, new String[] { expected1, expected2 });		
	}

	public void testExtractPotentiallyNullField1() throws Exception {
		setupForNullAnnotations(true/*plainJava*/);
		
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("import org.eclipse.jdt.annotation.*;\n");
		buf.append("class ResolvedTeam {\n");
		buf.append("   @Nullable ResolvedTeam superTeam;\n");
		buf.append("   String getDescriptor() { return null; }\n");
		buf.append("}\n");
		buf.append("public class E {\n");
		buf.append("    public String foo(@NonNull ResolvedTeam team) {\n");
		buf.append("		if (team.superTeam != null)\n");
		buf.append("        	return team.superTeam.getDescriptor();\n");
		buf.append("		return null;\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("E.java", buf.toString(), false, null);

		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList<IJavaCompletionProposal> proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 3);
		assertCorrectLabels(proposals);

		// primary proposal: Extract to checked local variable
		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		assertEquals("Display String", "Extract to checked local variable", proposal.getDisplayString());
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("import org.eclipse.jdt.annotation.*;\n");
		buf.append("class ResolvedTeam {\n");
		buf.append("   @Nullable ResolvedTeam superTeam;\n");
		buf.append("   String getDescriptor() { return null; }\n");
		buf.append("}\n");
		buf.append("public class E {\n");
		buf.append("    public String foo(@NonNull ResolvedTeam team) {\n");
		buf.append("		if (team.superTeam != null) {\n");
		buf.append("            final ResolvedTeam superTeam2 = team.superTeam;\n");
		buf.append("            if (superTeam2 != null) {\n");
		buf.append("                return superTeam2.getDescriptor();\n");
		buf.append("            } else {\n");
		buf.append("                // TODO handle null value\n");
		buf.append("                return null;\n");
		buf.append("            }\n");
		buf.append("        }\n");
		buf.append("		return null;\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());

		// secondary
		ChangeCorrectionProposal otherProposal = (ChangeCorrectionProposal) proposals.get(1);
		assertEquals("Display String 2", "Add @SuppressWarnings 'null' to 'foo()'", otherProposal.getDisplayString());
		
		otherProposal = (ChangeCorrectionProposal) proposals.get(2);
		assertEquals("Display String 3", "Configure problem severity", otherProposal.getDisplayString());
	}
}
