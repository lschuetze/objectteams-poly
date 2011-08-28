/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.core;

import java.util.ArrayList;
import java.util.Hashtable;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalModel;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalPositionGroup;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalPositionGroup.Proposal;
import org.eclipse.jdt.internal.corext.template.java.CodeTemplateContextType;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.text.correction.proposals.CUCorrectionProposal;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Testing corrections for unresolved methods in OT specific contexts.
 * @author stephan
 * @since 1.2.1
 */
public class UnresolvedMethodsQuickFixTest extends OTQuickFixTest {
	private static final Class<UnresolvedMethodsQuickFixTest> THIS= UnresolvedMethodsQuickFixTest.class;

	public UnresolvedMethodsQuickFixTest(String name) {
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

	@Override
	protected void addOptions(Hashtable options) {
		super.addOptions(options);
		IPreferenceStore store= JavaPlugin.getDefault().getPreferenceStore();
		store.setValue(PreferenceConstants.CODEGEN_ADD_COMMENTS, false);

		StubUtility.setCodeTemplate(CodeTemplateContextType.METHODSTUB_ID, "", null);
		StubUtility.setCodeTemplate(CodeTemplateContextType.CONSTRUCTORSTUB_ID, "", null);
	}

	public void testMethodInSameType() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("import java.util.Vector;\n");
		buf.append("public team class T {\n");
		buf.append("    protected class R {\n");
		buf.append("        void foo(Vector vec) {\n");
		buf.append("            int i= goo(vec, true);\n");
		buf.append("        }\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 2);
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("import java.util.Vector;\n");
		buf.append("public team class T {\n");
		buf.append("    protected class R {\n");
		buf.append("        void foo(Vector vec) {\n");
		buf.append("            int i= goo(vec, true);\n");
		buf.append("        }\n");
		buf.append("\n");
		buf.append("        private int goo(Vector vec, boolean b) {\n");
		buf.append("            return 0;\n");
		buf.append("        }\n");		
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
		
		proposal= (CUCorrectionProposal) proposals.get(1);
		preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("import java.util.Vector;\n");
		buf.append("public team class T {\n");
		buf.append("    protected class R {\n");
		buf.append("        void foo(Vector vec) {\n");
		buf.append("            int i= goo(vec, true);\n");
		buf.append("        }\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    public int goo(Vector vec, boolean b) {\n");
		buf.append("        return 0;\n");
		buf.append("    }\n");		
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}

	public void testMethodInDifferentClass1() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("import java.util.Vector;\n");
		buf.append("public team class T {\n");
		buf.append("    protected class R {\n");
		buf.append("        void foo(Vector vec, R2 r) {\n");
		buf.append("            int i= r.goo(vec, true);\n");
		buf.append("        }\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("team package test1.T;\n");
		buf.append("public class R2 {\n");
		buf.append("}\n");
		IPackageFragment pack1T= fSourceFolder.createPackageFragment("test1.T", false, null);
		pack1T.createCompilationUnit("R2.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 2); // 2nd proposal not really useful (cast to Object??)
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("team package test1.T;\n");
		buf.append("\n");
		buf.append("import java.util.Vector;\n");
		buf.append("\n");
		buf.append("public class R2 {\n");
		buf.append("\n");
		buf.append("    public int goo(Vector vec, boolean b) {\n");
		buf.append("        return 0;\n");
		buf.append("    }\n");		
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	/* See Trac #12 */
	public void testMethodInDifferentClass2() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("    public class R {\n");
		buf.append("    }\n");
		buf.append("}\n");
		pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("import java.util.Vector;\n");
		buf.append("public team class T2 {\n");
		buf.append("	public class R2 {\n");
		buf.append("        void foo(final T1 t1, R<@t1> r, Vector vec) {\n");
		buf.append("            int i= r.goo(vec, true);\n");
		buf.append("        }\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T2.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 2); // 2nd proposal not really useful (cast to Object??)
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("\n");
		buf.append("import java.util.Vector;\n");
		buf.append("\n");
		buf.append("public team class T1 {\n");
		buf.append("    public class R {\n");
		buf.append("\n");
		buf.append("        public int goo(Vector vec, boolean b) {\n");
		buf.append("            return 0;\n");
		buf.append("        }\n");		
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	
	public void testTSuperConstructor() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T2 extends T1 {\n");
		buf.append("  protected class R {\n");
		buf.append("    public R(int i) {\n");
		buf.append("        tsuper(i);\n");
		buf.append("    }\n");
		buf.append("  }\n");
		buf.append("}\n");
		ICompilationUnit cu1=pack1.createCompilationUnit("T2.java", buf.toString(), false, null);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("  protected class R {\n");
		buf.append("  }\n");
		buf.append("}\n");
		pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu1);
		ArrayList proposals= collectCorrections(cu1, astRoot);
		assertNumberOfProposals(proposals, 2);
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview1= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("  protected class R {\n");
		buf.append("\n");
		buf.append("    public R(int i) {\n");
		buf.append("    }\n");		
		buf.append("  }\n");
		buf.append("}\n");
		String expected1= buf.toString();	

		proposal= (CUCorrectionProposal) proposals.get(1);
		String preview2= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T2 extends T1 {\n");
		buf.append("  protected class R {\n");
		buf.append("    public R(int i) {\n");
		buf.append("        tsuper();\n");
		buf.append("    }\n");
		buf.append("  }\n");
		buf.append("}\n");
		String expected2= buf.toString();		
		
		assertEqualStringsIgnoreOrder(new String[] { preview1, preview2 }, new String[] { expected1, expected2 });		

	}
	
	// static callin method created
	public void testCallinMethod1() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("    static void foo(int i) {\n");
		buf.append("    }\n");
		buf.append("}\n");
		pack1.createCompilationUnit("B.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        void voo(int j) <- replace void foo(int i);\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 2); // don't propose to generate into team!!
		assertCorrectLabels(proposals);

		// create callin method voo:
		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview1= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        void voo(int j) <- replace void foo(int i);\n");
		buf.append("\n");
		buf.append("        static callin void voo(int j) {\n");
		buf.append("        }\n");		
		buf.append("    }\n");
		buf.append("}\n");
		String expected1= buf.toString();
		
		// remove signatures in callin binding voo<-foo
		proposal= (CUCorrectionProposal) proposals.get(1);
		String preview2= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        voo <- replace foo;\n");
		buf.append("    }\n");
		buf.append("}\n");
		String expected2= buf.toString();
		
		assertEqualStringsIgnoreOrder(new String[] { preview1, preview2 }, new String[] { expected1, expected2 });		
	}
	// non-static callin method created
	public void testCallinMethod2() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("    String foo(int i, Boolean b) {\n");
		buf.append("    }\n");
		buf.append("}\n");
		pack1.createCompilationUnit("B.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        String voo(int j, Boolean b) <- replace String foo(int i, Boolean b);\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 2); // don't propose to generate into team!!
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview1= getPreviewContent(proposal);

		// create callin method voo:
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        String voo(int j, Boolean b) <- replace String foo(int i, Boolean b);\n");
		buf.append("\n");
		buf.append("        callin String voo(int j, Boolean b) {\n");
		buf.append("            return null;\n");
		buf.append("        }\n");		
		buf.append("    }\n");
		buf.append("}\n");
		String expected1= buf.toString();

		// remove signatures in callin binding voo<-foo
		proposal= (CUCorrectionProposal) proposals.get(1);
		String preview2= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        voo <- replace foo;\n");
		buf.append("    }\n");
		buf.append("}\n");
		String expected2= buf.toString();
		
		assertEqualStringsIgnoreOrder(new String[] { preview1, preview2 }, new String[] { expected1, expected2 });		
	}

	// non-static non-callin method created (base side)
	// Bug 316665 -  [quickfix] create method from unresolved replace callin RHS adds "callin" modifier
	public void testCallinMethod3() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("}\n");
		pack1.createCompilationUnit("B.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        String voo(int j, Boolean b) <- replace String foo(int i, Boolean b);\n");
		buf.append("        callin String voo(int j, Boolean b) {\n");
		buf.append("            return base.voo(j, b);\n");
		buf.append("        }\n");		
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 2);
		assertCorrectLabels(proposals);

		// create base method foo:
		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview1= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("\n");
		buf.append("    public String foo(int i, Boolean b) {\n"); // expect no "callin" modifier
		buf.append("        return null;\n");
		buf.append("    }\n");
		buf.append("}\n");
		String expected1 = buf.toString();
		
		// remove signatures from callin voo<-foo
		proposal= (CUCorrectionProposal) proposals.get(1);
		String preview2= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        voo <- replace foo;\n");
		buf.append("        callin String voo(int j, Boolean b) {\n");
		buf.append("            return base.voo(j, b);\n");
		buf.append("        }\n");		
		buf.append("    }\n");
		buf.append("}\n");
		String expected2= buf.toString();
		
		assertEqualStringsIgnoreOrder(new String[] { preview1, preview2 }, new String[] { expected1, expected2 });		
	}
	// Bug 329988 - Quickfix method generation on missing replace callin method generates wrong method
	// callin method created from short callin binding -> need to infer method signature
	public void testUnresolvedCallinMapping1() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("    String foo(int idx, Boolean b) {\n");
		buf.append("    }\n");
		buf.append("}\n");
		pack1.createCompilationUnit("B.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        voo <- replace foo;\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 1); // don't propose to generate into team!!
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        voo <- replace foo;\n");
		buf.append("\n");
		buf.append("        callin String voo(int i, Boolean boolean1) {\n");
		buf.append("            return null;\n");
		buf.append("        }\n");		
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}

	// Bug 329988 - Quickfix method generation on missing replace callin method generates wrong method
	// missing base method created (short callin binding)
	public void testUnresolvedCallinMapping2() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("}\n");
		pack1.createCompilationUnit("B.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        voo <- replace foo;\n");
		buf.append("        callin String voo(int j, Boolean flag) {\n");
		buf.append("            return base.voo(j, flag);\n");
		buf.append("        }\n");		
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 1);
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("\n");
		buf.append("    public String foo(int i, Boolean boolean1) {\n");
		buf.append("        return null;\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}

	// Bug 329988 - Quickfix method generation on missing replace callin method generates wrong method
	// non-static callin method created (short callin binding) - lifting/lowering involved
	public void testUnresolvedCallinMapping3() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("    B foo(int idx, B b) {\n");
		buf.append("    }\n");
		buf.append("}\n");
		pack1.createCompilationUnit("B.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R1 playedBy B {\n");
		buf.append("        voo <- replace foo;\n");
		buf.append("    }\n");
		buf.append("	public class R2 playedBy B {}\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 1); // don't propose to generate into team!!
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R1 playedBy B {\n");
		buf.append("        voo <- replace foo;\n");
		buf.append("\n");
		buf.append("        callin R1 voo(int i, R1 r1) {\n");
		buf.append("            return null;\n");
		buf.append("        }\n");		
		buf.append("    }\n");
		buf.append("	public class R2 playedBy B {}\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
		
		LinkedProposalModel linkedProposalModel = new ProposalAdaptor().getLinkedProposalModel(proposal);
		assertLinkedTypeProposals(linkedProposalModel, "arg_type_1", new String[]{"R1", "R2", "B"});
		assertLinkedTypeProposals(linkedProposalModel, "type", new String[]{"R1", "R2", "B"});
	}

	// helper for above:
	private LinkedProposalPositionGroup assertLinkedTypeProposals(LinkedProposalModel linkedProposalModel, String key, String[] expectedStrings) {
		LinkedProposalPositionGroup typePositionGroup = linkedProposalModel.getPositionGroup(key, false);
		assertNotNull(typePositionGroup);
		Proposal[] linkedTypeProposals = typePositionGroup.getProposals();
		assertEquals("Wrong number of linked type proposals", expectedStrings.length, linkedTypeProposals.length);
		for (int i = 0; i < expectedStrings.length; i++)
			assertEquals("Unexpected "+(i+1)+". type", expectedStrings[i], linkedTypeProposals[i].getDisplayString());
		return typePositionGroup;
	}

	// Bug 329988 - Quickfix method generation on missing replace callin method generates wrong method
	// missing base method created (short callout binding)
	public void testUnresolvedCallout1() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("}\n");
		pack1.createCompilationUnit("B.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R playedBy B {\n");
		buf.append("        abstract String voo(int j, Boolean flag);\n");
		buf.append("        voo -> foo;\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 1);
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("\n");
		buf.append("    public String foo(int i, Boolean boolean1) {\n");
		buf.append("        return null;\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}
	

	// Bug 329988 - Quickfix method generation on missing replace callin method generates wrong method
	// missing base method created (short callout binding) - translation involved
	public void testUnresolvedCallout2() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("}\n");
		pack1.createCompilationUnit("B.java", buf.toString(), false, null);
		
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class T1 {\n");
		buf.append("	public class R1 playedBy B {\n");
		buf.append("        abstract R1 voo(int j, R1 r1);\n");
		buf.append("        voo -> foo;\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("T1.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList proposals= collectCorrections(cu, astRoot);
		assertNumberOfProposals(proposals, 1);
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("\n");
		buf.append("    public B foo(int i, B b) {\n");
		buf.append("        return null;\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());
	}

	// Bug 339520 - [quickfix] creating a role constructor via quickfix creates ';' instead of body
	public void testRoleInstanceCreation1() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class E {\n");
		buf.append("    public class A {\n");
		buf.append("    }\n");
		buf.append("    public void foo(int i) {\n");
		buf.append("        A a= new A(i);\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu1=pack1.createCompilationUnit("E.java", buf.toString(), false, null);

		CompilationUnit astRoot= getASTRoot(cu1);
		ArrayList proposals= collectCorrections(cu1, astRoot);
		assertNumberOfProposals(proposals, 2);
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview1= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class E {\n");
		buf.append("    public class A {\n");
		buf.append("\n");
		buf.append("        public A(int i) {\n");
		buf.append("        }\n");
		buf.append("    }\n");
		buf.append("    public void foo(int i) {\n");
		buf.append("        A a= new A(i);\n");
		buf.append("    }\n");
		buf.append("}\n");
		String expected1= buf.toString();	

		proposal= (CUCorrectionProposal) proposals.get(1);
		String preview2= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public team class E {\n");
		buf.append("    public class A {\n");
		buf.append("    }\n");
		buf.append("    public void foo(int i) {\n");
		buf.append("        A a= new A();\n");
		buf.append("    }\n");
		buf.append("}\n");
		String expected2= buf.toString();		
		
		assertEqualStringsIgnoreOrder(new String[] { preview1, preview2 }, new String[] { expected1, expected2 });		
	}

/* some orig tests for use in OT-variants:
 * 	
	
	public void testConstructorInvocation() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class E {\n");
		buf.append("    public E(int i) {\n");
		buf.append("        this(i, true);\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu1=pack1.createCompilationUnit("E.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu1);
		ArrayList proposals= collectCorrections(cu1, astRoot);
		assertNumberOfProposals(proposals, 1);
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview1= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class E {\n");
		buf.append("    public E(int i) {\n");
		buf.append("        this(i, true);\n");
		buf.append("    }\n");
		buf.append("\n");
		buf.append("    public E(int i, boolean b) {\n");
		buf.append("    }\n");		
		buf.append("}\n");
		String expected1= buf.toString();
				
		assertEqualString(preview1, expected1);
	}
	
	
	public void testSuperMethodInvocation() throws Exception {
		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class E extends A {\n");
		buf.append("    public void foo(int i) {\n");
		buf.append("        super.foo(i);\n");
		buf.append("    }\n");
		buf.append("}\n");
		ICompilationUnit cu1=pack1.createCompilationUnit("E.java", buf.toString(), false, null);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class A {\n");
		buf.append("}\n");
		pack1.createCompilationUnit("A.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu1);
		ArrayList proposals= collectCorrections(cu1, astRoot);
		assertNumberOfProposals(proposals, 1);
		assertCorrectLabels(proposals);

		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(0);
		String preview= getPreviewContent(proposal);

		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class A {\n");
		buf.append("\n");
		buf.append("    public void foo(int i) {\n");
		buf.append("    }\n");
		buf.append("}\n");
		assertEqualString(preview, buf.toString());

	}
*/

}
