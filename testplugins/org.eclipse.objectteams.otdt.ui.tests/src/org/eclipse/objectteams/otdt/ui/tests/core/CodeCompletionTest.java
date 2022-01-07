/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2016 Fraunhofer Gesellschaft, Munich, Germany,
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
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.core;

import static org.eclipse.jdt.internal.codeassist.RelevanceConstants.R_CASE;
import static org.eclipse.jdt.internal.codeassist.RelevanceConstants.R_DEFAULT;
import static org.eclipse.jdt.internal.codeassist.RelevanceConstants.R_EXPECTED_TYPE;
import static org.eclipse.jdt.internal.codeassist.RelevanceConstants.R_INTERESTING;
import static org.eclipse.jdt.internal.codeassist.RelevanceConstants.R_METHOD_OVERIDE;
import static org.eclipse.jdt.internal.codeassist.RelevanceConstants.R_NON_RESTRICTED;
import static org.eclipse.jdt.internal.codeassist.RelevanceConstants.R_RESOLVED;
import static org.junit.Assert.*;

import java.util.Hashtable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.core.manipulation.CodeTemplateContextType;
import org.eclipse.jdt.internal.core.manipulation.StubUtility;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.text.java.JavaCompletionProcessor;
import org.eclipse.jdt.internal.ui.text.java.JavaMethodCompletionProposal;
import org.eclipse.jdt.internal.ui.text.java.ParameterGuessingProposal;
import org.eclipse.jdt.internal.ui.text.template.contentassist.TemplateProposal;
import org.eclipse.jdt.testplugin.JavaProjectHelper;
import org.eclipse.jdt.testplugin.TestOptions;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.tests.core.CoreTests;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.java.CompletionProposalCollector;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.objectteams.otdt.internal.ui.assist.CompletionAdaptor;
import org.eclipse.objectteams.otdt.ui.tests.util.OTJavaProjectHelper;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.eclipse.objectteams.otdt.ui.tests.core.rule.ProjectTestSetup;


/**
 * Tests for code completion including the UI part
 * i.e., testing the actual rewriting and also selections.
 *
 * @author stephan
 * @since 1.1.8
 */
public class CodeCompletionTest {

	private static final int KEYWORD_RELEVANCE = R_DEFAULT+R_RESOLVED+R_INTERESTING+R_CASE+R_NON_RESTRICTED;
	private static final int CALLOUT_BASE_RELEVANCE = R_DEFAULT+R_RESOLVED+R_INTERESTING+R_CASE+R_METHOD_OVERIDE+R_NON_RESTRICTED;
	private static final int INTERESTING_CALLIN_CALLOUT_PROPOSAL = CompletionAdaptor.R_METHOD_MAPPING * CALLOUT_BASE_RELEVANCE;
	private static final int VERY_INTERESTING_CALLIN_CALLOUT_PROPOSAL = CompletionAdaptor.R_METHOD_MAPPING * (CALLOUT_BASE_RELEVANCE+R_EXPECTED_TYPE);

	@Rule
	public ProjectTestSetup pts = new ProjectTestSetup();

	@Rule public TestName name = new TestName();
	String getName() { return name.getMethodName(); }

	private IJavaProject fJProject1;

	private void codeComplete(ICompilationUnit cu, int offset, CompletionProposalCollector collector) throws JavaModelException {
		waitBeforeCoreCompletion(); // ??
		cu.codeComplete(offset, collector);
	}

	@Before
	public void setUp() throws Exception {
		fJProject1= OTJavaProjectHelper.createOTJavaProject("OTTestProject1", "bin");
		JavaProjectHelper.addRTJar17(fJProject1);
		JavaProjectHelper.addRequiredProject(fJProject1, ProjectTestSetup.getProject());

		Hashtable<String, String> options= TestOptions.getDefaultOptions();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		options.put(JavaCore.CODEASSIST_FIELD_PREFIXES, "f");
		JavaCore.setOptions(options);

		IPreferenceStore store= JavaPlugin.getDefault().getPreferenceStore();
		store.setValue(PreferenceConstants.CODEGEN_ADD_COMMENTS, true);
		store.setValue(PreferenceConstants.CODEASSIST_GUESS_METHOD_ARGUMENTS, false);
		store.setValue(PreferenceConstants.CODEASSIST_SHOW_VISIBLE_PROPOSALS, false);

		StubUtility.setCodeTemplate(CodeTemplateContextType.OVERRIDECOMMENT_ID, "/* (non-Javadoc)\n * ${see_to_overridden}\n */", null); // TODO(SH): this one is currently used for method bindings, see TODO in CompletionAdaptor.Stubs#createCallout
		StubUtility.setCodeTemplate(CodeTemplateContextType.DELEGATECOMMENT_ID, "/* (non-Javadoc)\n * ${see_to_target}\n */", null);
		StubUtility.setCodeTemplate(CodeTemplateContextType.METHODSTUB_ID, "//TODO\n${body_statement}", null);
		StubUtility.setCodeTemplate(CodeTemplateContextType.CONSTRUCTORCOMMENT_ID, "/**\n * Constructor.\n */", null);
		StubUtility.setCodeTemplate(CodeTemplateContextType.METHODCOMMENT_ID, "/**\n * Method.\n */", null);
		StubUtility.setCodeTemplate(CodeTemplateContextType.CONSTRUCTORSTUB_ID, "//TODO\n${body_statement}", null);
		StubUtility.setCodeTemplate(CodeTemplateContextType.GETTERCOMMENT_ID, "/**\n * @return the ${bare_field_name}\n */", fJProject1);
		StubUtility.setCodeTemplate(CodeTemplateContextType.SETTERCOMMENT_ID, "/**\n * @param ${param} the ${bare_field_name} to set\n */", fJProject1);

		fBeforeImports= "";
		fAfterImports= "";
		fMembers= "";
		fLocals= "";
		fTrigger= '\0';
		fWaitBeforeCompleting= false;
	}

	@After
	public void tearDown() throws Exception {
		IPreferenceStore store= JavaPlugin.getDefault().getPreferenceStore();
		store.setToDefault(PreferenceConstants.CODEGEN_ADD_COMMENTS);
		store.setToDefault(PreferenceConstants.CODEASSIST_GUESS_METHOD_ARGUMENTS);
		store.setToDefault(PreferenceConstants.CODEASSIST_SHOW_VISIBLE_PROPOSALS);
		closeAllEditors();
		JavaProjectHelper.delete(fJProject1);
	}

	public static void closeEditor(IEditorPart editor) {
		IWorkbenchPartSite site;
		IWorkbenchPage page;
		if (editor != null && (site= editor.getSite()) != null && (page= site.getPage()) != null)
			page.closeEditor(editor, false);
	}

	public static void closeAllEditors() {
		IWorkbenchWindow[] windows= PlatformUI.getWorkbench().getWorkbenchWindows();
		for (int i= 0; i < windows.length; i++) {
			IWorkbenchPage[] pages= windows[i].getPages();
			for (int j= 0; j < pages.length; j++) {
				IEditorReference[] editorReferences= pages[j].getEditorReferences();
				for (int k= 0; k < editorReferences.length; k++)
					closeEditor(editorReferences[k].getEditor(false));
			}
		}
	}

	/* Legacy style of testing: */
	@Ignore
	@Test
	public void testCreateCalloutCompletion() throws Exception {
		IPackageFragmentRoot sourceFolder= JavaProjectHelper.addSourceContainer(fJProject1, "src");

		// create base class:
		IPackageFragment pack1= sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("public class B {\n");
		buf.append("    public void foo() {\n");
		buf.append("    }\n");
		buf.append("}\n");
		String contents= buf.toString();

		pack1.createCompilationUnit("B.java", contents, false, null);

		// create team class:
		IPackageFragment pack2= sourceFolder.createPackageFragment("test2", false, null);
		buf= new StringBuffer();
		buf.append("package test2;\n");
		buf.append("import base test1.B;\n");
		buf.append("public team class T {\n");
		buf.append("    public class R playedBy B {\n");
		buf.append("        fo\n");
		buf.append("    }\n");
		buf.append("}\n");
		contents= buf.toString();

		ICompilationUnit cuT= pack2.createCompilationUnit("T.java", contents, false, null);

		fEditor= (JavaEditor)JavaUI.openInEditor(cuT);

		String str= "        fo";

		int offset= contents.indexOf(str) + str.length();

		CompletionProposalCollector collector= createCollector(cuT, offset);
		collector.setReplacementLength(0);
		codeComplete(cuT, offset, collector);
		IJavaCompletionProposal[] proposals= collector.getJavaCompletionProposals();

		assertTrue("proposals", proposals.length >= 1);
		IJavaCompletionProposal selected= null;
		for (IJavaCompletionProposal javaCompletionProposal : proposals) {
			if (javaCompletionProposal.getDisplayString().startsWith("foo()  void - Binding")) {
				selected= javaCompletionProposal;
				break;
			}
		}
		assertNotNull("expected proposal", selected);

		IDocument doc= fEditor.getDocumentProvider().getDocument(fEditor.getEditorInput());
		selected.apply(doc);

		buf= new StringBuffer();
		buf.append("package test2;\n" +
			"import base test1.B;\n" +
			"public team class T {\n" +
			"    public class R playedBy B {\n" +
			"\n" +
	        "        /* (non-Javadoc)\n" +
	        "         * @see test1.B#foo()\n" +
	        "         */\n" +
			"        void foo() -> void foo();\n" +
			"        \n" +
			"    }\n" +
			"}\n" +
			"");
		CoreTests.assertEqualString(doc.get(), buf.toString());
	}
	// helpers for above:
	private CompletionProposalCollector createCollector(ICompilationUnit cu, int offset) throws PartInitException, JavaModelException {
		CompletionProposalCollector collector= new CompletionProposalCollector(cu);
		collector.setInvocationContext(createContext(offset, cu));
		return collector;
	}

	private JavaContentAssistInvocationContext createContext(int offset, ICompilationUnit cu) throws PartInitException, JavaModelException {
		JavaEditor editor= (JavaEditor) JavaUI.openInEditor(cu);
		ISourceViewer viewer= editor.getViewer();
		return new JavaContentAssistInvocationContext(viewer, offset, editor);
	}

	// ==== START REAL TESTS: ====

	/** At the empty space within a role one could create a constructor. */
	@Test
	public void testCreateRoleConstructor() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        |",
				"R(",
				"        /**\n" +
				"         * Constructor.\n" +
				"         */\n" +
				"        public R(B b) {\n" +
				"            //TODO\n" +
				"\n" +
				"        }",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	@Test
	public void testCreateCallout1() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        fo|",
				"foo(",
				        "\n" + // TODO(SH): initial newline is not intended?
				"        /* (non-Javadoc)\n" +
				"         * @see test1.B#foo()\n" +
				"         */\n" +
				"        void |foo|() -> void foo();\n" +
				"        ",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}
	// abstract method exists:
	@Test
	public void testCreateCallout1a() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"	 	 abstract void foo();\n"+
				"        fo|",
				"foo(",
				"	 	 abstract void foo();\n"+
				        "\n" + // TODO(SH): initial newline is not intended?
				"        /* (non-Javadoc)\n" +
				"         * @see test1.B#foo()\n" +
				"         */\n" +
				"        void |foo|() -> void foo();\n" +
				"        ",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	// ROFI
	@Test
	public void testCreateCallout2() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertROFIBodyProposal(
				"    fo|",
				"foo(",
				        "\n" + // TODO(SH): initial newline is not intended?
				"    /* (non-Javadoc)\n" +
				"     * @see test1.B#foo()\n" +
				"     */\n" +
				"    void |foo|() -> void foo();\n" +
				"    ",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	@Test
	public void testCreateCalloutOverride1() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" +
				"	public class MyRole {\n" +
				"		String blub(int i) { return \"\"; }\n" +
				"	}\n" +
				"}\n",
				true, null);

		pkg.createCompilationUnit("ABase.java",
				"package test1.p1;\n" +
				"public class ABase {\n" +
				"	String blub(int k) { return null; }\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("\n");
		subTeamContent.append("import base test1.p1.ABase;\n");
		subTeamContent.append("public team class Completion_testCreateCalloutOverride1 extends SuperTeam {\n");
		subTeamContent.append("    protected class MyRole playedBy ABase {\n");
		subTeamContent.append("		\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("\n");
		expectedContent.append("import base test1.p1.ABase;\n");
		expectedContent.append("public team class Completion_testCreateCalloutOverride1 extends SuperTeam {\n");
		expectedContent.append("    protected class MyRole playedBy ABase {\n");
		expectedContent.append("\n");
		expectedContent.append("        /* (non-Javadoc)\n");
		expectedContent.append("         * @see test1.p1.ABase#blub(int)\n");
		expectedContent.append("         */\n");
		expectedContent.append("        String blub(int k) => String blub(int k);\n");
		expectedContent.append("		\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String completeAfter = "		";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("String");

		// discriminate from overriding "blub(int i)":
		assertProposal("blub(int k", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, "String".length()), 0);
	}


	// callout override to field
	@Test
	public void testCreateCalloutOverride2() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" +
				"	public class MyRole {\n" +
				"		void setBlub(int i) { }\n" +
				"	}\n" +
				"}\n",
				true, null);

		pkg.createCompilationUnit("ABase.java",
				"package test1.p1;\n" +
				"public class ABase {\n" +
				"	int blub;\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("import base test1.p1.ABase;\n");
		subTeamContent.append("public team class Completion_testCreateCalloutOverride1 extends SuperTeam {\n");
		subTeamContent.append("    protected class MyRole playedBy ABase {\n");
		subTeamContent.append("		set\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("import base test1.p1.ABase;\n");
		expectedContent.append("public team class Completion_testCreateCalloutOverride1 extends SuperTeam {\n");
		expectedContent.append("    protected class MyRole playedBy ABase {\n");
		expectedContent.append("\n");
		expectedContent.append("        /**\n");
		expectedContent.append("         * \n");
		expectedContent.append("         */\n");
		expectedContent.append("        void setBlub(int blub) => set int blub;\n");
		expectedContent.append("		\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String completeAfter = "		set";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("		");

		// discriminate from overriding "setBlub(int i)":
		assertProposal("setBlub(int)", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter+2, 0), 0);
	}


	// Bug 374840 - [assist] callout completion after parameter mapping garbles the code
	@Test
	public void testCreateCalloutOverride3() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");

		pkg.createCompilationUnit("ABase.java",
				"package test1.p1;\n" +
				"public class ABase {\n" +
				"	Object blub;\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import base test1.p1.ABase;\n");
		subTeamContent.append("public team class Completion_testCreateCalloutOverride3 {\n");
		subTeamContent.append("    protected class MyRole playedBy ABase {\n");
		subTeamContent.append("		public String getBlubString() -> get Object blub\n");
		subTeamContent.append("		    with { result <- blub.toString() }\n");
		subTeamContent.append("		toStr\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import base test1.p1.ABase;\n");
		expectedContent.append("public team class Completion_testCreateCalloutOverride3 {\n");
		expectedContent.append("    protected class MyRole playedBy ABase {\n");
		expectedContent.append("		public String getBlubString() -> get Object blub\n");
		expectedContent.append("		    with { result <- blub.toString() }\n");
		expectedContent.append("\n");
		expectedContent.append("        /* (non-Javadoc)\n");
		expectedContent.append("         * @see java.lang.Object#toString()\n");
		expectedContent.append("         */\n");
		expectedContent.append("        String toString() => String toString();\n");
		expectedContent.append("		\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String completeAfter = "		toStr";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("String toString");

		// discriminate from method override:
		assertProposal("toString()  String", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, "String".length()), 0);
	}

	@Test
	public void testCompleteCallout1() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        void foo() ->|",
				"foo(",
				"        void foo() ->| void foo();|" + // SH: why selected?
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	@Test
	public void testCompleteCallout2() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        foo -> |",
				"foo(",
				"        foo -> foo; |" + // SH: trailing space?
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* short, no callinModifier, callin-method, follows: short callin. */
	@Test
	public void testCompleteCallout3() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        callin void bar() {}\n"+
				"        void foo() ->|\n" +             // no leading space, inserted below (good)
				"        bar <- replace clone;\n",
				"foo(",
				"        callin void bar() {}\n" +
				"        void foo() ->| void foo();|\n" +
				"        bar <- replace clone;\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* short, prefix typed, follows: method */
	@Test
	public void testCompleteCallout4() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        foo -> f|",
				"foo(",
				"        foo -> |foo;|",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	@Test
	public void testCompleteCallout5() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"		|",
				"foo(",
				"\n"+
		        "        /* (non-Javadoc)\n" +
		        "         * @see test1.B#foo()\n" +
		        "         */\n" +
				"        void |foo|() -> void foo();\n" +
				"		",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	@Test
	public void testCompleteCallout6() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertROFIBodyProposal(
				"	|",
				"foo(",
				"\n"+
		        "    /* (non-Javadoc)\n" +
		        "     * @see test1.B#foo()\n" +
		        "     */\n" +
				"    void |foo|() -> void foo();\n" +
				"	",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	// callout to static from ROFI
	@Test
	public void testCompleteCallout7() throws Exception {
		createBaseClass("    public static void foo() {}\n");
		assertROFIBodyProposal(
				"	|",
				"foo(",
				"\n"+
		        "    /* (non-Javadoc)\n" +
		        "     * @see test1.B#foo()\n" +
		        "     */\n" +
				"    void |foo|() -> void foo();\n" +
				"	",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	// See Bug 395762: this case could trigger the reported hang
	@Test
	public void testCompleteParameterMapping1() throws Exception {
		createBaseClass("    java.util.List<String> names;\n");
		assertNosuchTypeBodyProposal(
				"        String setName(int i, String n) -> set java.util.List<String> names\n" +
				"            with { n -> base.| }",
				"names",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	// similar to above, positive case
	@Test
	public void testCompleteParameterMapping2() throws Exception {
		createBaseClass("    java.util.List<String> names;\n");
		assertTypeBodyProposal(
				"        String getName(int i) -> get java.util.List<String> names\n" +
				"            with { result <- base.| }",
				"names",
				"        String getName(int i) -> get java.util.List<String> names\n" +
				"            with { result <- base.names|| }",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	// Bug 353468 - [completion] completing a method binding inserts nested class by its binary name
	@Test
	public void testCreateMethodBinding1() throws Exception {
		// secondary types:
		createBaseClass("test1.b1", "B1", "    public class Inner{}\n");
		createBaseClass("test1.b2", "B2", "");
		// the base class bound to our role:
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("import test1.b1.B1;\n");
		buf.append("import test1.b2.B2;\n");
		buf.append("public class B {\n");
		buf.append("    public void foo(B1 b1, B1.Inner inner, B2 b2);\n");
		buf.append("}\n");
		String contents= buf.toString();
		IPackageFragment basePkg = CompletionTestSetup.getAbsoluteTestPackage(this.fJProject1, "test1");
		basePkg.createCompilationUnit("B.java", contents, true, null);

		fAfterImports = "\n" +
				"import test1.b1.B1;\n" +
				"import test1.b1.B1.Inner;\n" +
				"import test1.b2.B2;\n";

		assertTypeBodyProposal(
				"        |",
				"foo(",
				        "\n" +
				"        /* (non-Javadoc)\n" +
				"         * @see test1.B#foo(test1.b1.B1, test1.b1.B1.Inner, test1.b2.B2)\n" +
				"         */\n" +
				"        void |foo|(B1 b1, Inner inner, B2 b2) -> void foo(B1 b1, Inner inner,\n" +
				"                B2 b2);\n" +
				"        ",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/** A base method spec with return type and beginning of the selector is searched. */
	@Test
	public void testCompletionMethodSpecLong1() throws Exception {
		createBaseClass("");
		assertTypeBodyProposal(
	            "        String toString() => String toStr|",
	            "toString(",
	            "        String toString() => |String toString();|",
	            INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}


	/** A base method spec without return type and beginning of the selector is searched. */
	@Test
	public void testCompletionMethodSpecLong2() throws Exception {
		createBaseClass("");
		assertTypeBodyProposal(
	            "        String toString() => toStr|",
	            "toString(",
	            "        String toString() => |String toString();|",
	            INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/** A base method spec with return type and no selector is searched. */
	@Test
	public void testCompletionMethodSpecLong3() throws Exception {
		createBaseClass("");
		assertTypeBodyProposal(
	            "        String toString() => String |",
	            "toString(",
	            "        String toString() => String toString(); |", // trailing space?
	            INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* short, no callinModifier, follows: method */
	@Test
	public void testCompleteCallin1() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        void bar() {}\n"+
				"        bar <-|\n" +                    // no space, inserted below (good)
				"        void zork() {}\n",
				"foo(",
				"        void bar() {}\n" +
				"        bar <- |before| foo;\n" +
				"        void zork() {}\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}
	// concrete method exists, create full callin binding (cf. testCreateCallout1a()).
	@Test
	public void testCreateCallin1a() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"	 	 void foo() {}\n"+
				"        fo|",
				"foo(",
				"	 	 void foo() {}\n"+
				        "\n" + // TODO(SH): initial newline is not intended?
				"        /* (non-Javadoc)\n" +
				"         * @see test1.B#foo()\n" +
				"         */\n" +
				"        void |foo|() <- before void foo();\n" +
				"        ",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	// create callin with non-default selections in linked mode
	// DISABLED because I'm yet to find a way for triggering linked mode selection in a test
	@Ignore
	@Test
	public void testCreateCallin2() throws Exception {
		createBaseClass("    public String foo() {}\n");
		assertTypeBodyProposal(
				"        fo|",
				"foo(",
				        "\n" + // TODO(SH): initial newline is not intended?
				"        /* (non-Javadoc)\n" +
				"         * @see test1.B#foo()\n" +
				"         */\n" +
				"        |String| foo() <- before void foo();\n" +
				"        ",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* short, with callinModifier, follows: callout binding. */
	@Test
	public void testCompleteCallin2() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        void bar() {}\n"+
				"        bar <- after |\n" +
				"        void foo() -> void foo();\n",
				"foo(",
				"        void bar() {}\n" +
				"        bar <- after foo; |\n" +        // SH: trailing space?
				"        void foo() -> void foo();\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}


	/* short, with callinModifier, follows: callout binding, non-keyword return type. */
	@Test
	public void testCompleteCallin2a() throws Exception {
		createBaseClass("    public String foo() { return null; }\n");
		assertTypeBodyProposal(
				"        String bar() { return \"\"; }\n"+
				"        bar <- after |\n" +
				"        String foo() -> String foo();\n",
				"foo(",
				"        String bar() { return \"\"; }\n" +
				"        bar <- after foo; |\n" +        // SH: trailing space?
				"        String foo() -> String foo();\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* long, with callinModifier, follows: field. */
	@Test
	public void testCompleteCallin3() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        void bar() {}\n"+
				"        void bar() <-after |\n" + // cannot insert space before modifier (OK)
				"        int i;\n",
				"foo(",
				"        void bar() {}\n" +
				"        void bar() <-after void foo(); |\n" +
				"        int i;\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* long, no callinModifier, callin-method, follows: field. */
	@Test
	public void testCompleteCallin4() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        callin void bar() {}\n"+
				"        void bar() <- |\n" +
				"        int i;\n",
				"foo(",
				"        callin void bar() {}\n" +
				"        void bar() <- |replace| void foo(); \n" +
				"        int i;\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* long, no callinModifier, callin-method, follows: method. */
	@Test
	public void testCompleteCallin5() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        callin void bar() {}\n"+
				"        void bar() <-|\n" +               // no leading space, inserted below (good)
				"        int zork() {}\n",
				"foo(",
				"        callin void bar() {}\n" +
				"        void bar() <- |replace| void foo();\n" +
				"        int zork() {}\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* long, no callinModifier, callin-method, follows: short callin. */
	@Test
	public void testCompleteCallin6() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        callin void bar() {}\n"+
				"        void bar() <-|\n" +               // no leading space, inserted below (good)
				"        bar <- replace clone;\n",
				"foo(",
				"        callin void bar() {}\n" +
				"        void bar() <- |replace| void foo();\n" +
				"        bar <- replace clone;\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* long, no callinModifier, callin-method, follows: short callin. */
	@Test
	public void testCompleteCallin7() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        callin void bar() {}\n"+
				"        bar <- replace |\n" +
				"        bar <- replace clone;\n",
				"foo",
				"        callin void bar() {}\n" +
				"        bar <- replace foo; |\n" +    // SH: trailing space?
				"        bar <- replace clone;\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* short, prefix typed, with callinModifier, follows: callout binding. */
	@Test
	public void testCompleteCallin8() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        void bar() {}\n"+
				"        bar <- after f|\n" +
				"        void foo() -> void foo();\n",
				"foo(",
				"        void bar() {}\n" +
				"        bar <- after |foo;|\n" +
				"        void foo() -> void foo();\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* short, complete binding. */
	@Test
	public void testCompleteCallin9() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"        callin void bar() {}\n"+
				"        bar <- before| foo;\n" +
				"        int zork() {}\n",
				"foo",
				"        callin void bar() {}\n" +
				"        bar <- before| foo, foo;\n" +
				"        int zork() {}\n" +
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	@Test
	public void testCompleteBasecall1() throws Exception {
		createBaseClass("    public String getBaseText(Object object) {return null;}\n");
		assertTypeBodyProposal(
				"        callin String getText(Object o) {\n"+
				"        	 return base.|\n" +
				"        }\n" +
				"        getText <- replace getBaseText;\n",
				"", // should only have one proposal, so accept any to see if there are others
				"        callin String getText(Object o) {\n"+
				"        	 return base.getText(|o|)\n" +
				"        }\n" +
				"        getText <- replace getBaseText;\n" +
				"",
				0); // should only have one proposal, so accept any to see if there are others
	}

	// static case, result ignored - see https://bugs.eclipse.org/bugs/show_bug.cgi?id=331669
	@Test
	public void testCompleteBasecall2() throws Exception {
		createBaseClass("    public static String getBaseText(Object object) {return null;}\n");
		assertTypeBodyProposal(
				"        static callin String getText(Object o) {\n"+
				"        	 base.|\n" +
				"        	 return \"\";\n" +
				"        }\n" +
				"        getText <- replace getBaseText;\n",
				"", // should only have one proposal, so accept any to see if there are others
				"        static callin String getText(Object o) {\n"+
				"        	 base.getText(|o|)\n" +
				"        	 return \"\";\n" +
				"        }\n" +
				"        getText <- replace getBaseText;\n" +
				"",
				0); // should only have one proposal, so accept any to see if there are others
	}

	// http://bugs.eclipse.org/394061 - [assist] template proposals not working after a base call
	@Test
	public void testMethodInvocations1() throws Exception {
		createBaseClass("    public String getBaseText(Object object) {return null;}\n");
		assertTypeBodyProposal(
				"        callin String getText(Object o) {\n" +
				"			 if (o != null)\n"+
				"        	 	return base.getText(o);\n" +
				"			 sysout|\n" +
				"			 return \"\";\n" +
				"        }\n" +
				"        getText <- replace getBaseText;\n",
				"", // should only have one proposal, so accept any to see if there are others
				"        callin String getText(Object o) {\n"+
				"			 if (o != null)\n"+
				"        	 	return base.getText(o);\n" +
				"			 System.out.println(||);\n" +
				"			 return \"\";\n" +
				"        }\n" +
				"        getText <- replace getBaseText;\n" +
				"",
				0); // should only have one proposal, so accept any to see if there are others
	}
	// http://bugs.eclipse.org/394061 - [assist] template proposals not working after a base call
	// variant challenging completion after a tsuper call
	@Test
	public void testMethodInvocations2() throws Exception {
		IPackageFragmentRoot sourceFolder= JavaProjectHelper.addSourceContainer(fJProject1, "src");

		IPackageFragment pack1= sourceFolder.createPackageFragment("test1", false, null);
		String contents= "package test1;\n" +
				"\n" +
				"public team class CompletionTeam0 {\n" +
				"    protected class R {\n" +
				"        void foomethod(int i) {}\n" +
				"        void foomethod(String s) {}\n" +
				"        void barmethod(int i) {}\n" +
				"    }\n" +
				"}\n";
		pack1.createCompilationUnit("CompletionTeam0.java", contents, false, null);

		contents= "package test1;\n" +
				"\n" +
				"public team class CompletionTeam1 extends CompletionTeam0 {\n" +
				"    protected class R {\n" +
				"        void foomethod(int i) {\n" +
				"            tsuper.foomethod(i);\n" +
				"			 sysout//here\n" +
				"        }\n" +
				"    }\n" +
				"}\n";
		ICompilationUnit cu1= pack1.createCompilationUnit("CompletionTeam1.java", contents, false, null);

		String str= "//here";

		int offset= contents.indexOf(str);

		fEditor= (JavaEditor) EditorUtility.openInEditor(cu1);
		ICompletionProposal proposal= findNonNullProposal("", cu1, new Region(offset, 0), 0);
		String expectedContents= "package test1;\n" +
				"\n" +
				"public team class CompletionTeam1 extends CompletionTeam0 {\n" +
				"    protected class R {\n" +
				"        void foomethod(int i) {\n" +
				"            tsuper.foomethod(i);\n" +
				"			 System.out.println();//here\n" +
				"        }\n" +
				"    }\n" +
				"}\n";
		assertAppliedTemplateProposal(contents, (TemplateProposal)proposal, expectedContents);
	}

	@Test
	public void testCompleteTSuperCall1()  throws Exception {
		IPackageFragmentRoot sourceFolder= JavaProjectHelper.addSourceContainer(fJProject1, "src");

		IPackageFragment pack1= sourceFolder.createPackageFragment("test1", false, null);
		String contents= "package test1;\n" +
				"\n" +
				"public team class CompletionTeam0 {\n" +
				"    protected class R {\n" +
				"        void foomethod(int i) {}\n" +
				"        void foomethod(String s) {}\n" +
				"        void barmethod(int i) {}\n" +
				"    }\n" +
				"}\n";
		pack1.createCompilationUnit("CompletionTeam0.java", contents, false, null);

		contents= "package test1;\n" +
				"\n" +
				"public team class CompletionTeam1 extends CompletionTeam0 {\n" +
				"    protected class R {\n" +
				"        void foomethod(int i) {\n" +
				"            tsuper.//here\n" +
				"        }\n" +
				"    }\n" +
				"}\n";
		ICompilationUnit cu1= pack1.createCompilationUnit("CompletionTeam1.java", contents, false, null);

		String str= "//here";

		int offset= contents.indexOf(str);

		CompletionProposalCollector collector= createCollector(cu1, offset);
		collector.setReplacementLength(0);

		codeComplete(cu1, offset, collector);

		IJavaCompletionProposal[] proposals= collector.getJavaCompletionProposals();

		// strictly expect one method only.
		assertEquals(1, proposals.length);
		assertAppliedProposal(contents, proposals[0], "foomethod();"); // arguments are not inserted by this method :(
	}

	// see Trac #126
	@Test
	public void testCompleteInCallinMethod1() throws Exception {
		createBaseClass("    public String getText() { return null; }\n");
		ICompletionProposal proposal = assertTypeBodyProposal(
				"        void foo(int i) {} \n" +
				"        callin void getText() {\n" +
				"            int idx = 3;\n" +
				"        	 foo|\n" +
				"        }\n" +
				"        getText <- replace getText;\n",
				"foo", // this is just step 1.
				"        void foo(int i) {} \n" +
				"        callin void getText() {\n" +
				"            int idx = 3;\n" +
				"        	 foo(|i|);\n" +
				"        }\n" +
				"        getText <- replace getText;\n" +
				"",
				0); // should only have one proposal, so accept any to see if there are others
		// this is the interesting check: choices??
		assertChoices(proposal, new String[][]{new String[]{"i", "idx", "0"}});
	}

	/* Full c-t-f declaration */
	@Test
	public void testCalloutToField1() throws Exception {
		createBaseClass("public int myField;\n");
		assertTypeBodyProposal(
				"        void bar() {}\n"+
				"        get|\n"+
				"        void blub() {}\n",
				"getMyField",
				"        void bar() {}\n"+
				"        /**\n" +
				"         * \n" + // TODO(SH): define method binding comment templates
				"         */\n" +
				"        int getMyField() -> get int myField;\n"+
				"        |\n"+    // TODO(SH): newline not intended?
				"        void blub() {}\n"+
				"",
				INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* after modifier w/o trailing space */
	@Test
	public void testCalloutToField2() throws Exception {
		createBaseClass("public int myField;\n");
		assertTypeBodyProposal(
				"        void bar() {}\n"+
				"        int getMyField() -> get|\n"+
				"        void blub() {}\n",
				" int",
				"        void bar() {}\n"+
				"        int getMyField() -> get int myField;|\n"+
				"        void blub() {}\n"+
				"",
				VERY_INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* after modifier w/ trailing space */
	@Test
	public void testCalloutToField3() throws Exception {
		createBaseClass("public int myField;\n");
		assertTypeBodyProposal(
				"        void bar() {}\n"+
				"        void setMyField(int v) -> set |\n"+
				"        void blub() {}\n",
				"int",
				"        void bar() {}\n"+
				"        void setMyField(int v) -> set int myField;|\n"+
				"        void blub() {}\n"+
				"",
				VERY_INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* after modifier w/ trailing space - multiple base fields */
	@Test
	public void testCalloutToField4() throws Exception {
		createBaseClass("public int myField;\n"+
				        "public int yourField;\n"+
				        "public boolean nobodysField;");
		assertTypeBodyProposal(
				"        void bar() {}\n"+
				"        int getMyField() -> get |\n"+
				"        void blub() {}\n",
				"int y",
				"        void bar() {}\n"+
				"        int getMyField() -> get int yourField;|\n"+
				"        void blub() {}\n"+
				"",
				VERY_INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* after modifier w/ trailing space - multiple base fields, mismatching type */
	@Test
	public void testCalloutToField5() throws Exception {
		createBaseClass("public int myField;\n"+
				        "public int yourField;\n"+
				        "public boolean nobodysField;");
		assertNosuchTypeBodyProposal(
				"        void bar() {}\n"+
				"        int getMyField() -> get |\n"+
				"        void blub() {}\n",
				"boolean",
				VERY_INTERESTING_CALLIN_CALLOUT_PROPOSAL);
	}

	/* Don't accidentally propose a getter for _OT$base: */
	@Test
	public void testCalloutToField6() throws Exception {
		createBaseClass("public int myField;\n"+
				        "public int yourField;\n"+
				        "public boolean nobodysField;");
		assertNosuchTypeBodyProposal(
				"        void bar() {}\n"+
				"        get|\n"+
				"        void blub() {}\n",
				"get_",
				100);
	}

	@Test
	public void testPlayedBy1() throws Exception {
		createBaseClass("test2", "MyBase", "");
		fBeforeImports= "";
		fAfterImports= "\nimport base test2.MyBase;\n";
		assertTypeBodyProposal(
				"protected class R playedBy MyBa| {\n" +
				"}\n",
				"MyBase",
				"protected class R playedBy MyBase| {\n" +
				"}\n",
				0, false);
	}

	@Test
	public void testPlayedBy2() throws Exception {
		createBaseClass("test2", "AClass", "");
		fBeforeImports= "\n" +
						"import java.util.Map;\n";
		fAfterImports= "\n" +
						"import java.util.Map;\n" +
						"\n" +
						"import base test2.AClass;\n";
		assertTypeBodyProposal(
				"protected class AClass playedBy A| {\n" +
				"}\n",
				"ACl",
				"protected class AClass playedBy AClass| {\n" +
				"}\n",
				0, false);
	}

	// https://bugs.eclipse.org/460508 - Adopt and adjust new ImportRewriteAnalyzer from JDT/Core
	// base import for other role already exists, correctly sort in.
	@Test
	public void testPlayedBy3() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "test3");
		pkg.createCompilationUnit("OtherBase.java",
				"package test3;\n" +
				"public class OtherBase {}\n",
				true, null);

		createBaseClass("test2", "MyBase", "");
		fBeforeImports= "\n" +
						"import java.util.Map;\n" +
						"\n" +
						"import base test3.OtherBase;\n";
		fAfterImports= "\n" +
						"import java.util.Map;\n" +
						"\n" +
						"import base test2.MyBase;\n" +
						"import base test3.OtherBase;\n";
		assertTypeBodyProposal(
				"protected class R playedBy MyBa| {\n" +
				"}\n" +
				"protected class R2 playedBy OtherBase {\n" +
				"	Map<String,String> f;\n" +
				"}\n",
				"MyBase",
				"protected class R playedBy MyBase| {\n" +
				"}\n" +
				"protected class R2 playedBy OtherBase {\n" +
				"	Map<String,String> f;\n" +
				"}\n",
				0, false);
	}

	@Test
	public void testBaseGuard1() throws Exception {
		createBaseClass("test2", "AClass", "public boolean check() { return false; }");
		fBeforeImports = "import base test2.AClass;";
		fAfterImports = "import base test2.AClass;";
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass\n" +
				"        base when (base.|)\n" +
				"{\n" +
				"}\n",
				"che",
				"protected class ARole playedBy AClass\n" +
				"        base when (base.check()|)\n" +
				"{\n" +
				"}\n",
				0, false);
	}

	@Test
	public void testBaseGuard2() throws Exception {
		createBaseClass("test2", "AClass", "");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass\n" +
				"        ba|\n" +
				"{\n" +
				"}\n",
				"base",
				"protected class ARole playedBy AClass\n" +
				"        base when (|)\n" +
				"{\n" +
				"}\n",
				0, false);
	}
	@Test
	public void testGuard3() throws Exception {
		createBaseClass("test2", "AClass", "public boolean check() { return false; }");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass {\n" +
				"    toString <- after check w| \n" +
				"}\n",
				"wh",
				"protected class ARole playedBy AClass {\n" +
				"    toString <- after check when (|) \n" +
				"}\n",
				0, false);
	}
	@Test
	public void testGuard4() throws Exception {
		createBaseClass("test2", "AClass", "public String check() { return false; }");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass {\n" +
				"    String toString() <- after String check() w| \n" +
				"}\n",
				"wh",
				"protected class ARole playedBy AClass {\n" +
				"    String toString() <- after String check() when (|) \n" +
				"}\n",
				0, false);
	}

	// inside guard of short method binding
	// Bug 340083 - [assist] cannot complete inside a binding guard
	@Test
	public void testGuard5() throws Exception {
		fBeforeImports = "import base test2.AClass;";
		fAfterImports = "import base test2.AClass;";
		createBaseClass("test2", "AClass", "public String check() { return \"false\"; }");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass {\n" +
				"    boolean flag;\n" +
				"    toString <- after check when(f|); \n" +
				"}\n",
				"fla",
				"protected class ARole playedBy AClass {\n" +
				"    boolean flag;\n" +
				"    toString <- after check when(flag|); \n" +
				"}\n",
				0, false);
	}

	// inside guard of short method binding, field reference
	// Bug 340083 - [assist] cannot complete inside a binding guard
	@Test
	public void testGuard6() throws Exception {
		fBeforeImports = "import base test2.AClass;";
		fAfterImports = "import base test2.AClass;";
		createBaseClass("test2", "AClass", "public String check() { return \"false\"; }");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass {\n" +
				"    boolean flag;\n" +
				"    toString <- after check when(this.f|); \n" +
				"}\n",
				"fla",
				"protected class ARole playedBy AClass {\n" +
				"    boolean flag;\n" +
				"    toString <- after check when(this.flag|); \n" +
				"}\n",
				0, false);
	}

	// inside base guard of short method binding, field reference
	// Bug 340083 - [assist] cannot complete inside a binding guard
	@Test
	public void testGuard7() throws Exception {
		fBeforeImports = "import base test2.AClass;";
		fAfterImports = "import base test2.AClass;";
		createBaseClass("test2", "AClass", "public String check() { return \"false\"; }\n public boolean flig;\n");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass {\n" +
				"    boolean flag;\n" +
				"    toString <- after check base when(base.f|); \n" +
				"}\n",
				"fl",
				"protected class ARole playedBy AClass {\n" +
				"    boolean flag;\n" +
				"    toString <- after check base when(base.flig|); \n" +
				"}\n",
				0, false);
	}

	// see https://bugs.eclipse.org/340103 - [assist] FUP of bug 340083
	// inside base guard of method binding with signatures, field reference
	@Test
	public void testGuard8() throws Exception {
		fBeforeImports = "import base test2.AClass;";
		fAfterImports = "import base test2.AClass;";
		createBaseClass("test2", "AClass", "public String check() { return \"false\"; }\n public boolean flig;\n");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass {\n" +
				"    boolean flag;\n" +
				"    String toString() <- after String check() base when(base.f|); \n" +
				"}\n",
				"fl",
				"protected class ARole playedBy AClass {\n" +
				"    boolean flag;\n" +
				"    String toString() <- after String check() base when(base.flig|); \n" +
				"}\n",
				0, false);
	}

	@Test
	public void testCallinMethodModifier() throws Exception {
		createBaseClass("    public void foo() {}\n");
		assertTypeBodyProposal(
				"	 	 |",
				"callin",
				"	 	 callin",
				KEYWORD_RELEVANCE);
	}

	@Test
	public void testTeamKeywordToplevel() throws Exception {
		StringBuffer teamContent = new StringBuffer();
		teamContent.append("package test1;\n");
		teamContent.append("public  class MyTeam {\n");
		teamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("public team class MyTeam {\n");
		expectedContent.append("}");

		String completeAfter = "public ";
		int pos = teamContent.indexOf(completeAfter)+completeAfter.length();

		assertProposal("te", null, null, teamContent, new Region(pos, 0), expectedContent, new Region(pos+4, 0), 0);
	}

	@Test
	public void testTeamKeywordNested() throws Exception {
		StringBuffer teamContent = new StringBuffer();
		teamContent.append("package test1;\n");
		teamContent.append("public team class MyTeam {\n");
		teamContent.append("	protected \n");
		teamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("public team class MyTeam {\n");
		expectedContent.append("	protected team\n");
		expectedContent.append("}");

		String completeAfter = "protected ";
		int pos = teamContent.indexOf(completeAfter)+completeAfter.length();

		assertProposal("te", null, null, teamContent, new Region(pos, 0), expectedContent, new Region(pos+4, 0), 0);
	}

	@Test
	public void testRoleTag1() throws Exception {
		IPackageFragment teamPkg = CompletionTestSetup.getTestPackage(this.fJProject1, "MyTeam");
		teamPkg.createCompilationUnit("MyRole.java",
				"team package test1.MyTeam;\n" +
				"public class MyRole {}\n",
				true, null);
		StringBuffer teamContent = new StringBuffer();
		teamContent.append("package test1;\n");
		teamContent.append("/** @role My */\n");
		teamContent.append("public team class MyTeam {\n");
		teamContent.append("    MyRole roleField;\n"); // trigger loading
		teamContent.append("    protected class MyOtherRole {}\n");
		teamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("/** @role MyRole */\n");
		expectedContent.append("public team class MyTeam {\n");
		expectedContent.append("    MyRole roleField;\n");
		expectedContent.append("    protected class MyOtherRole {}\n");
		expectedContent.append("}");

		String completeAfter = "@role My";
		int pos = teamContent.indexOf(completeAfter)+completeAfter.length();

		assertProposal("My", null, null, teamContent, new Region(pos, 0), expectedContent, new Region(pos+4, 0), 0); // 4: len(MyRole)-len(My)
	}

	@Test
	public void testRoleTag2() throws Exception {
		IPackageFragment teamPkg = CompletionTestSetup.getTestPackage(this.fJProject1, "MyTeam");
		teamPkg.createCompilationUnit("MyRole.java",
				"team package test1.MyTeam;\n" +
				"public class MyRole {}\n",
				true, null);
		StringBuffer teamContent = new StringBuffer();
		teamContent.append("package test1;\n");
		teamContent.append("/** @role  */\n");
		teamContent.append("public team class MyTeam {\n");
		teamContent.append("    MyRole roleField;\n"); // trigger loading
		teamContent.append("    protected class MyOtherRole {}\n");
		teamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("/** @role MyRole */\n");
		expectedContent.append("public team class MyTeam {\n");
		expectedContent.append("    MyRole roleField;\n");
		expectedContent.append("    protected class MyOtherRole {}\n");
		expectedContent.append("}");

		String completeAfter = "@role ";
		int pos = teamContent.indexOf(completeAfter)+completeAfter.length();

		assertProposal("", null, null, teamContent, new Region(pos, 0), expectedContent, new Region(pos+6, 0), 0); // 6: len(MyRole)
	}


	@Test
	public void testCreateMethod1() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" +
				"	public class MyRole {\n" +
				"		String blub(int i) { return \"\"; }\n" +
				"	}\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("public team class Completion_testCreateMethod1 extends SuperTeam {\n");
		subTeamContent.append("    protected class MyRole {\n");
		subTeamContent.append("        blu\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("public team class Completion_testCreateMethod1 extends SuperTeam {\n");
		expectedContent.append("    protected class MyRole {\n");
		expectedContent.append("        /* (non-Javadoc)\n");
		expectedContent.append("         * @see test1.p1.SuperTeam.MyRole#blub(int)\n");
		expectedContent.append("         */\n");
		expectedContent.append("        @Override\n");
		expectedContent.append("        String blub(int i) {\n");
		expectedContent.append("            //TODO\n");
		expectedContent.append("            return tsuper.blub(i);\n");
		expectedContent.append("        }\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String completeAfter = "blu";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("}")+1;

		assertProposal("", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
	}

	// Bug 362003 - [assist] completion is broken after <B base R> after a base guard
	@Test
	public void testCreateMethod2() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("BaseClass.java",
				"package test1.p1;\n" +
				"public class BaseClass {\n" +
				"    public void blub() {}\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import base test1.p1.BaseClass;\n");
		subTeamContent.append("public team class Completion_testCreateMethod2 {\n");
		subTeamContent.append("    protected class Role0 {\n");
		subTeamContent.append("        void foo() {}\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("    protected class Role1 extends Role0 {\n");
		subTeamContent.append("        String val;\n");
		subTeamContent.append("        void bar1() {}\n");
		subTeamContent.append("          \n");
		subTeamContent.append("    }\n");
		subTeamContent.append("    protected class Role2 extends Role0 {\n");
		subTeamContent.append("        void bar() {}\n");
		subTeamContent.append("        bar <- after blub\n");
		subTeamContent.append("        		base when(4 == 5);\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("    <B base Role0> void takeIt(B as Role0 o) {\n");
		subTeamContent.append("        System.out.print(o);\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import base test1.p1.BaseClass;\n");
		expectedContent.append("public team class Completion_testCreateMethod2 {\n");
		expectedContent.append("    protected class Role0 {\n");
		expectedContent.append("        void foo() {}\n");
		expectedContent.append("    }\n");
		expectedContent.append("    protected class Role1 extends Role0 {\n");
		expectedContent.append("        String val;\n");
		expectedContent.append("        void bar1() {}\n");
		expectedContent.append("          /* (non-Javadoc)\n");
		expectedContent.append("         * @see test1.Completion_testCreateMethod2.Role0#foo()\n");
		expectedContent.append("         */\n");
		expectedContent.append("        @Override\n");
		expectedContent.append("        void foo() {\n");
		expectedContent.append("            //TODO\n");
		expectedContent.append("            super.foo();\n");
		expectedContent.append("        }\n");
		expectedContent.append("    }\n");
		expectedContent.append("    protected class Role2 extends Role0 {\n");
		expectedContent.append("        void bar() {}\n");
		expectedContent.append("        bar <- after blub\n");
		expectedContent.append("        		base when(4 == 5);\n");
		expectedContent.append("    }\n");
		expectedContent.append("    <B base Role0> void takeIt(B as Role0 o) {\n");
		expectedContent.append("        System.out.print(o);\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String completeAfter = "          ";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("super.foo()");
		posAfter = expectedContent.indexOf("}", posAfter)+1;

		assertProposal("foo", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
	}

	// override role, simple case
	@Test
	public void testOverrideRole1() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" +
				"	public class MyRole {\n" +
				"	}\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("public team class Completion_testOverrideRole1 extends SuperTeam {\n");
		subTeamContent.append("    \n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("public team class Completion_testOverrideRole1 extends SuperTeam {\n");
		expectedContent.append("    @Override\n");
		expectedContent.append("    public class MyRole {\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String completeAfter = "    ";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("}")+1;

		assertProposal("MyRole - Override", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
	}

	// override role, role file with mentioning in the team
	@Test
	public void testOverrideRole2() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" +
				"	RoleFile field\n;" + // help the compiler to find the role file
				"}\n",
				true, null);
		IPackageFragment rolePack = CompletionTestSetup.getTestPackage(this.fJProject1, "p1.SuperTeam");
		rolePack.createCompilationUnit("RoleFile.java",
				"team package test1.p1.SuperTeam;\n" +
				"protected class RoleFile { }\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("public team class Completion_testOverrideRole1 extends SuperTeam {\n");
		subTeamContent.append("    \n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("public team class Completion_testOverrideRole1 extends SuperTeam {\n");
		expectedContent.append("    @Override\n");
		expectedContent.append("    protected class RoleFile {\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String completeAfter = "    ";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("}")+1;

		assertProposal("RoleFile - Override", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
	}

	// override role, role file, without mentioning in the team (requires search engine help)
	@Test
	public void testOverrideRole3() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" + // no mentioning of RoleFile
				"}\n",
				true, null);
		IPackageFragment rolePack = CompletionTestSetup.getTestPackage(this.fJProject1, "p1.SuperTeam");
		rolePack.createCompilationUnit("RoleFile.java",
				"team package test1.p1.SuperTeam;\n" +
				"protected class RoleFile { }\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("public team class Completion_testOverrideRole1 extends SuperTeam {\n");
		subTeamContent.append("    \n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("public team class Completion_testOverrideRole1 extends SuperTeam {\n");
		expectedContent.append("    @Override\n");
		expectedContent.append("    protected class RoleFile {\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String completeAfter = "    ";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("}")+1;

		assertProposal("RoleFile - Override", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
	}

	// Bug 355255 - [assist] NPE during completion if team contains an enum
	@Test
	public void testOverrideRole4() throws Exception {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" +
				"	public class MyRole {\n" +
				"	}\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("public team class Completion_testOverrideRole1 extends SuperTeam {\n");
		subTeamContent.append("    enum Aufzaehlung { EINS, ZWEI }\n");
		subTeamContent.append("    \n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("public team class Completion_testOverrideRole1 extends SuperTeam {\n");
		expectedContent.append("    enum Aufzaehlung { EINS, ZWEI }\n");
		expectedContent.append("    @Override\n");
		expectedContent.append("    public class MyRole {\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String completeAfter = "}\n    ";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("    }")+5;

		assertProposal("MyRole - Override", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
	}

	// propose creating a team instance:
	@Test
	public void testNewExpression1() throws CoreException {
		createBaseClass("test2", "AClass", "public boolean check() { return false; }");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass {\n" +
				"}\n" +
				"static void foo() {\n" +
				"    new |\n" +
				"}\n",
				"Completion_",
				"protected class ARole playedBy AClass {\n" +
				"}\n" +
				"static void foo() {\n" +
				"    new Completion_testNewExpression1()|\n" +
				"}\n",
				0, false);

	}

	// propose creating a role instance (bound role):
	@Test
	public void testNewExpression2() throws CoreException {
		createBaseClass("test1", "AClass", "public boolean check() { return false; }");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass {\n" +
				"}\n" +
				"static void foo() {\n" +
				"    new |\n" +
				"}\n",
				"A",
				"protected class ARole playedBy AClass {\n" +
				"}\n" +
				"static void foo() {\n" +
				"    new ARole(|base|)\n" + // editing base argument
				"}\n",
				0, false);

	}

	// propose creating a role instance (unbound role):
	@Test
	public void testNewExpression3() throws CoreException {
		assertTypeBodyProposal(
				"protected class ARole {\n" +
				"}\n" +
				"static void foo() {\n" +
				"    new |\n" +
				"}\n",
				"A",
				"protected class ARole {\n" +
				"}\n" +
				"static void foo() {\n" +
				"    new ARole()|\n" + // no arguments to add
				"}\n",
				0, false);

	}

	// propose a regular diamond expression:
	@Test
	public void testNewExpression4() throws CoreException {
		fBeforeImports = "import java.util.Collection;\n" +
				"import base test2.AClass;\n";
		fAfterImports = "import java.util.Collection;\n" +
				"\n" +
				"import testutil.MyColl;\n" +
				"\n" +
				"import base test2.AClass;\n";
		createBaseClass("testutil", "MyColl", "<T> implements java.util.Collection<T>",
				"	public void addAll(Collection<? extends E> other) {}\n" +
				"	public E[] toArray() { return null; }\n" +
				"	public int size() { return 1; }\n");
		createBaseClass("test2", "AClass", "public boolean check() { return false; }");
		assertTypeBodyProposal(
				"protected class ARole playedBy AClass {\n" +
				"}\n" +
				"static void foo() {\n" +
				"    Collection<String> strings = new My|\n" +
				"}\n",
				"MyCol",
				"protected class ARole playedBy AClass {\n" +
				"}\n" +
				"static void foo() {\n" +
				"    Collection<String> strings = new MyColl<>()|\n" +
				"}\n",
				0, false);

	}

	// propose methods invoked via a phantom role, simple case
	@Test
	public void testMethodInvocation1() throws CoreException {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" +
				"	 protected class R {\n" +
				"        /** Doc of foo() */\n" +
				"        protected void foo() { }\n" +
				"    }\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("public team class Completion_testMethodInvocation1 extends SuperTeam {\n");
		subTeamContent.append("    void test(R r) {\n");
		subTeamContent.append("        r.\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("public team class Completion_testMethodInvocation1 extends SuperTeam {\n");
		expectedContent.append("    void test(R r) {\n");
		expectedContent.append("        r.foo();\n");
		expectedContent.append("    }\n");
		expectedContent.append("}");

		String expectedInfo = ">Doc of foo() </body></html>";

		String completeAfter = "r.";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("foo();")+6;

		ICompletionProposal proposal = assertProposal("foo", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
		assertTrue("Unexpected additional info", proposal.getAdditionalProposalInfo().endsWith(expectedInfo));
	}

	// propose methods invoked via a phantom role, two direct tsuper roles both have the method, pick the nearest version
	@Test
	public void testMethodInvocation2() throws CoreException {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" +
				"  protected team class Mid1 {\n" +
				"	 protected class R {\n" +
				"        /** Doc of original foo() */\n" +
				"        protected void foo() { }\n" +
				"    }\n" +
				"  }\n" +
				"  protected team class Mid2 extends Mid1 {\n" +
				"	 protected class R {\n" +
				"        /** foo in SuperMid2 */\n" +
				"        protected void foo() { }\n" +
				"    }\n" +
				"  }\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("public team class Completion_testMethodInvocation2 extends SuperTeam {\n");
		subTeamContent.append("  protected team class Mid1 {\n");
		subTeamContent.append("    protected class R {\n");
		subTeamContent.append("      /** foo in SubMid1 */\n");
		subTeamContent.append("      protected void foo() {}\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("  }\n");
		subTeamContent.append("  protected team class Mid2 {\n");
		subTeamContent.append("    void test(R r) {\n");
		subTeamContent.append("        r.\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("  }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("public team class Completion_testMethodInvocation2 extends SuperTeam {\n");
		expectedContent.append("  protected team class Mid1 {\n");
		expectedContent.append("    protected class R {\n");
		expectedContent.append("      /** foo in SubMid1 */\n");
		expectedContent.append("      protected void foo() {}\n");
		expectedContent.append("    }\n");
		expectedContent.append("  }\n");
		expectedContent.append("  protected team class Mid2 {\n");
		expectedContent.append("    void test(R r) {\n");
		expectedContent.append("        r.foo();\n");
		expectedContent.append("    }\n");
		expectedContent.append("  }\n");
		expectedContent.append("}");

		String superMid1R = "%E2%98%82=OTTestProject1/src%3Ctest1.p1%7BSuperTeam.java%E2%98%83SuperTeam%E2%98%83Mid1%E2%98%83R";
		String expectedInfo = ">foo in SuperMid2 " +
							  "<div><b>Overrides:</b> " +
							  "<a href='eclipse-javadoc:"+superMid1R+"~foo'>foo()</a> " +
							  "in <a href='eclipse-javadoc:"+superMid1R+"'>R</a></div></body></html>";

		String completeAfter = "r.";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("r.foo();")+8;

		ICompletionProposal proposal = assertProposal("foo", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
		assertTrue("Unexpected additional info", proposal.getAdditionalProposalInfo().endsWith(expectedInfo));
	}

	// propose methods invoked via a phantom role, two direct tsuper roles, only more distant one has the method
	@Test
	public void testMethodInvocation3() throws CoreException {
		IPackageFragment pkg = CompletionTestSetup.getTestPackage(this.fJProject1, "p1");
		pkg.createCompilationUnit("SuperTeam.java",
				"package test1.p1;\n" +
				"public team class SuperTeam {\n" +
				"  protected team class Mid1 {\n" +
				"	 protected class R {\n" +
				"        /** Doc of original foo() */\n" +
				"        protected void foo() { }\n" +
				"    }\n" +
				"  }\n" +
				"  protected team class Mid2 extends Mid1 {\n" +
				"	 protected class R {\n" +
				"    }\n" +
				"  }\n" +
				"}\n",
				true, null);

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("import test1.p1.SuperTeam;\n");
		subTeamContent.append("public team class Completion_testMethodInvocation3 extends SuperTeam {\n");
		subTeamContent.append("  protected team class Mid1 {\n");
		subTeamContent.append("    protected class R {\n");
		subTeamContent.append("      /** foo in SubMid1 */\n");
		subTeamContent.append("      protected void foo() {}\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("  }\n");
		subTeamContent.append("  protected team class Mid2 {\n");
		subTeamContent.append("    void test(R r) {\n");
		subTeamContent.append("        r.\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("  }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("import test1.p1.SuperTeam;\n");
		expectedContent.append("public team class Completion_testMethodInvocation3 extends SuperTeam {\n");
		expectedContent.append("  protected team class Mid1 {\n");
		expectedContent.append("    protected class R {\n");
		expectedContent.append("      /** foo in SubMid1 */\n");
		expectedContent.append("      protected void foo() {}\n");
		expectedContent.append("    }\n");
		expectedContent.append("  }\n");
		expectedContent.append("  protected team class Mid2 {\n");
		expectedContent.append("    void test(R r) {\n");
		expectedContent.append("        r.foo();\n");
		expectedContent.append("    }\n");
		expectedContent.append("  }\n");
		expectedContent.append("}");

		String superMid1R = "%E2%98%82=OTTestProject1/src%3Ctest1.p1%7BSuperTeam.java%E2%98%83SuperTeam%E2%98%83Mid1%E2%98%83R";
		String expectedInfo = ">foo in SubMid1 " +
							  "<div><b>Overrides:</b> " +
							  "<a href='eclipse-javadoc:"+superMid1R+"~foo'>foo()</a> " +
							  "in <a href='eclipse-javadoc:"+superMid1R+"'>R</a></div></body></html>";

		String completeAfter = "r.";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("r.foo();")+8;

		ICompletionProposal proposal = assertProposal("foo", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
		assertTrue("Unexpected additional info", proposal.getAdditionalProposalInfo().endsWith(expectedInfo));
	}


	// propose methods invoked via a phantom role, simple nested case
	@Test
	public void testMethodInvocation4() throws CoreException {


		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("public team class Completion_testMethodInvocation4 {\n");
		subTeamContent.append("  protected team class Mid1 {\n");
		subTeamContent.append("    protected class R {\n");
		subTeamContent.append("      /** foo in Mid1 */\n");
		subTeamContent.append("      protected void foo() {}\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("  }\n");
		subTeamContent.append("  protected team class Mid2 extends Mid1 {\n");
		subTeamContent.append("    protected class R {}\n");
		subTeamContent.append("    void test(R r) {\n");
		subTeamContent.append("        r.\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("  }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("public team class Completion_testMethodInvocation4 {\n");
		expectedContent.append("  protected team class Mid1 {\n");
		expectedContent.append("    protected class R {\n");
		expectedContent.append("      /** foo in Mid1 */\n");
		expectedContent.append("      protected void foo() {}\n");
		expectedContent.append("    }\n");
		expectedContent.append("  }\n");
		expectedContent.append("  protected team class Mid2 extends Mid1 {\n");
		expectedContent.append("    protected class R {}\n");
		expectedContent.append("    void test(R r) {\n");
		expectedContent.append("        r.foo();\n");
		expectedContent.append("    }\n");
		expectedContent.append("  }\n");
		expectedContent.append("}");

		String expectedInfo = ">foo in Mid1 </body></html>";

		String completeAfter = "r.";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("r.foo();")+8;

		ICompletionProposal proposal = assertProposal("foo", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
		assertTrue("Unexpected additional info", proposal.getAdditionalProposalInfo().endsWith(expectedInfo));
	}

	// propose methods invoked via a phantom role, simple nested case
	@Test
	public void testMethodInvocation5() throws CoreException {

		createBaseClass("test1", "B", "public boolean check() { return false; }");

		StringBuffer subTeamContent = new StringBuffer();
		subTeamContent.append("package test1;\n");
		subTeamContent.append("public team class Completion_testMethodInvocation5 {\n");
		subTeamContent.append("    protected class R playedBy B {\n");
		subTeamContent.append("      protected void foo() {}\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("    java.util.List<R> roles;\n");
		subTeamContent.append("    void test(B as R rarg) {\n");
		subTeamContent.append("        roles.\n");
		subTeamContent.append("    }\n");
		subTeamContent.append("  }\n");
		subTeamContent.append("}");

		StringBuffer expectedContent = new StringBuffer();
		expectedContent.append("package test1;\n");
		expectedContent.append("public team class Completion_testMethodInvocation5 {\n");
		expectedContent.append("    protected class R playedBy B {\n");
		expectedContent.append("      protected void foo() {}\n");
		expectedContent.append("    }\n");
		expectedContent.append("    java.util.List<R> roles;\n");
		expectedContent.append("    void test(B as R rarg) {\n");
		expectedContent.append("        roles.add(arg0)\n");
		expectedContent.append("    }\n");
		expectedContent.append("  }\n");
		expectedContent.append("}");


		String completeAfter = "roles.";
		int pos = subTeamContent.indexOf(completeAfter)+completeAfter.length();
		int posAfter = expectedContent.indexOf("roles.add(arg0)")+10; // at start of argument

		ICompletionProposal proposal = assertProposal("add", null, null, subTeamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 4), 0);
		assertTrue("Should be a parameter guessing proposal", proposal instanceof ParameterGuessingProposal);
		assertChoices(proposal, new String[][]{new String[]{"arg0", "rarg", "null"}}); // <- this is key: expect "rarg" next to "arg0"!
	}

	@Test
	public void testBug416779a() throws CoreException {
		StringBuffer teamContent = new StringBuffer(
				"public team class Completion_testBug416779 {\n" +
				"	protecte abstract class R {\n" +
				"	}\n" +
				"	R test() {\n" +
				"		return new R\n" +
				"	}\n" +
				"}\n");
		String completeBehind = "new R";
		int pos = teamContent.indexOf(completeBehind)+completeBehind.length();

		assertNosuchProposal("R()", teamContent, new Region(pos, 0),  0);
		assertNosuchProposal("Completion_testBug416779.R()", teamContent, new Region(pos, 0),  0);
	}

	@Test
	public void testBug416779b() throws CoreException {
		StringBuffer teamContent = new StringBuffer(
				"public team class Completion_testBug416779 {\n" +
				"	protecte class R {\n" +
				"	}\n" +
				"	R test() {\n" +
				"		return new R\n" +
				"	}\n" +
				"}\n");
		String completeBehind = "new R";
		int pos = teamContent.indexOf(completeBehind)+completeBehind.length();

		StringBuffer expectedContent = new StringBuffer(
				"public team class Completion_testBug416779 {\n" +
				"	protecte class R {\n" +
				"	}\n" +
				"	R test() {\n" +
				"		return new R()\n" +
				"	}\n" +
				"}\n");

		int posAfter = expectedContent.indexOf("new R()") + "new R()".length();

		ICompletionProposal proposal = assertProposal("R()", null, null, teamContent, new Region(pos, 0), expectedContent, new Region(posAfter, 0), 0);
		assertTrue("Should be a java method proposal", proposal instanceof JavaMethodCompletionProposal);
	}

	// == Below: Helper methods/fields. ==

	private void createBaseClass(String classBody)
			throws JavaModelException, CoreException
	{
		createBaseClass("test1", "B", classBody);
	}
	private void createBaseClass(String basePackage, String className, String classBody)
			throws JavaModelException, CoreException
	{
		createBaseClass(basePackage, className, "", classBody);
	}
	private void createBaseClass(String basePackage, String className, String classHeaderDetails, String classBody)
			throws JavaModelException, CoreException
	{
		// create a base class:
		StringBuffer buf= new StringBuffer();
		buf.append("package "+basePackage+";\n");
		buf.append("public class "+className+classHeaderDetails+" {\n");
		buf.append(classBody);
		buf.append("}\n");
		String contents= buf.toString();

		IPackageFragment basePkg = CompletionTestSetup.getAbsoluteTestPackage(this.fJProject1, basePackage);
		basePkg.createCompilationUnit(className+".java", contents, true, null);
	}

	// from 3.4 AbstractCompletionTest:
	protected static final String CARET= "|";

	private ICompilationUnit fCU;
	private JavaEditor fEditor;

	private String fBeforeImports;
	private String fAfterImports;
	private String fMembers;
	private String fLocals;
	private char fTrigger;
	private boolean fWaitBeforeCompleting;

	protected void assertChoices(ICompletionProposal proposal, String[][] expected) {
		assertTrue("Not a ParameterGuessingProposal", proposal instanceof ParameterGuessingProposal);
		ParameterGuessingProposal pgProposal = (ParameterGuessingProposal)proposal;
		ICompletionProposal[][] choices = new ProposalAdaptor().getChoices(pgProposal);
		assertEquals("Not same number of choices", expected.length, choices.length);
		for (int i=0; i<choices.length; i++) {
			assertEquals("Not same number of nested choices", expected[i].length, choices[i].length);
			for (int j=0; j<choices.length; j++) {
				assertEquals("Unexpected choice", expected[i][j], choices[i][j].getDisplayString());
			}
		}
	}

	protected ICompletionProposal assertTypeBodyProposal(String before, String selector, String expected, int requiredRelevance)
			throws CoreException
	{
		return assertTypeBodyProposal(before, selector, expected, requiredRelevance, true);
	}
	protected ICompletionProposal assertTypeBodyProposal(String before, String selector, String expected, int requiredRelevance, boolean addRole)
			throws CoreException
	{
		StringBuffer contents= new StringBuffer();
		IRegion preSelection= assembleClassBodyTestCUExtractSelection(contents, before, fBeforeImports, addRole);
		StringBuffer result= new StringBuffer();
		IRegion expectedSelection= assembleClassBodyTestCUExtractSelection(result, expected, fAfterImports, addRole);

		return assertProposal(selector, null, null, contents, preSelection, result, expectedSelection, requiredRelevance);
	}

	protected ICompletionProposal assertROFIBodyProposal(String before, String selector, String expected, int requiredRelevance)
			throws CoreException
	{
		IPackageFragment testPkg = CompletionTestSetup.getTestPackage(this.fJProject1, null);
		String teamName = "Completion_" + getName();
		testPkg.createCompilationUnit(teamName + ".java",
				"package test1;\n" +
				"/** @role RoFiRole */\n" +
				"public team class Completion_"+getName()+" {}\n",
				true, null);

		StringBuffer contents= new StringBuffer();
		IRegion preSelection= assembleROFIBodyTestCUExtractSelection(contents, before, fBeforeImports);
		StringBuffer result= new StringBuffer();
		IRegion expectedSelection= assembleROFIBodyTestCUExtractSelection(result, expected, fAfterImports);

		return assertProposal(selector, teamName, "RoFiRole", contents, preSelection, result, expectedSelection, requiredRelevance);
	}

	protected void assertNosuchTypeBodyProposal(String before, String selector, int requiredRelevance)
			throws CoreException
	{
		StringBuffer contents= new StringBuffer();
		IRegion preSelection= assembleClassBodyTestCUExtractSelection(contents, before, fBeforeImports, true);

		assertNosuchProposal(selector, contents, preSelection, requiredRelevance);
	}

	private IRegion assembleClassBodyTestCUExtractSelection(StringBuffer buffer, String javadocLine, String imports, boolean addRole) {
		String prefix= "package test1;\n" +
		imports +
		"\n" +
		"public team class Completion_" + getName() + " {\n" + // SH: added: team, removed <T> (crashed!!) FIXME!!
//{ObjectTeams:
		(addRole ?
		"    protected class R playedBy B {\n" : "") +
// SH}
		fLocals +
		""; // SH: removed blanks
		String postfix= "\n" +
		"\n" +
		fMembers +
//{ObjectTeams:
		(addRole ?
		"    }\n":"")+
// SH}
		"}\n";
		StringBuffer lineBuffer= new StringBuffer(javadocLine);
		int firstPipe= lineBuffer.indexOf(CARET);
		int secondPipe;
		if (firstPipe == -1) {
			firstPipe= lineBuffer.length();
			secondPipe= firstPipe;
		} else {
			lineBuffer.replace(firstPipe, firstPipe + CARET.length(), "");
			secondPipe= lineBuffer.indexOf(CARET, firstPipe);
			if (secondPipe ==-1)
				secondPipe= firstPipe;
			else
				lineBuffer.replace(secondPipe, secondPipe + CARET.length(), "");
		}
		buffer.append(prefix + lineBuffer + postfix);
		return new Region(firstPipe + prefix.length(), secondPipe - firstPipe);
	}

	private IRegion assembleROFIBodyTestCUExtractSelection(StringBuffer buffer, String javadocLine, String imports) {
		String teamName = "Completion_" + getName();
		String prefix= "team package test1."+teamName+";\n" +
		imports +
		"\n" +
		"protected class RoFiRole playedBy B {\n" +
		fLocals +
		"";
		String postfix= "\n" +
		"\n" +
		fMembers +
		"}\n";
		StringBuffer lineBuffer= new StringBuffer(javadocLine);
		int firstPipe= lineBuffer.indexOf(CARET);
		int secondPipe;
		if (firstPipe == -1) {
			firstPipe= lineBuffer.length();
			secondPipe= firstPipe;
		} else {
			lineBuffer.replace(firstPipe, firstPipe + CARET.length(), "");
			secondPipe= lineBuffer.indexOf(CARET, firstPipe);
			if (secondPipe ==-1)
				secondPipe= firstPipe;
			else
				lineBuffer.replace(secondPipe, secondPipe + CARET.length(), "");
		}
		buffer.append(prefix + lineBuffer + postfix);
		return new Region(firstPipe + prefix.length(), secondPipe - firstPipe);
	}

	private ICompletionProposal assertProposal(String selector, String relativePackage, String typeName, StringBuffer contents, IRegion preSelection, StringBuffer result, IRegion expectedSelection, int requiredRelevance) throws CoreException {
//{ObjectTeams: made package and file name configurable via new arguments `relativePackage'/`typeName':
		IPackageFragment pkg = (relativePackage == null) ? CompletionTestSetup.getAnonymousTestPackage(this.fJProject1) : CompletionTestSetup.getTestPackage(this.fJProject1, relativePackage);
		fCU= (typeName == null) ? createCU(pkg, contents.toString()) : pkg.createCompilationUnit(typeName + ".java", contents.toString(), false, null);
// SH}
		fEditor= (JavaEditor) EditorUtility.openInEditor(fCU);
		IDocument doc;
		ITextSelection postSelection;
		ICompletionProposal proposal = null;
		try {
			proposal= findNonNullProposal(selector, fCU, preSelection, requiredRelevance);
			doc= fEditor.getDocumentProvider().getDocument(fEditor.getEditorInput());
//{ObjectTeams: selection needs to be set in the editor:
			fEditor.getSelectionProvider().setSelection(new TextSelection(preSelection.getOffset(), preSelection.getLength()));
// SH}
			apply(fEditor, doc, proposal, preSelection);
			postSelection= (ITextSelection) fEditor.getSelectionProvider().getSelection();
		} finally {
			JavaProjectHelper.emptyDisplayLoop();
			closeEditor(fEditor);
		}

		assertEquals(result.toString(), doc.get());
		assertEquals("selection offset", expectedSelection.getOffset(), postSelection.getOffset());
		assertEquals("selection length", expectedSelection.getLength(), postSelection.getLength());
		return proposal;
	}

	private void assertNosuchProposal(String selector, StringBuffer contents, IRegion preSelection, int requiredRelevance)
			throws CoreException
	{
		fCU= createCU(CompletionTestSetup.getAnonymousTestPackage(this.fJProject1), contents.toString());
		fEditor= (JavaEditor) EditorUtility.openInEditor(fCU);
		try {
			ICompletionProposal proposal= findNamedProposal(selector, fCU, preSelection, requiredRelevance);
			assertNull("found unexpected proposal: "+selector, proposal);
		} finally {
			closeEditor(fEditor);
		}
	}

	// from 3.4 CodeCompletionTest
	private void assertAppliedProposal(String contents, IJavaCompletionProposal proposal, String completion) {
		IDocument doc= new Document(contents);
		proposal.apply(doc);
		int offset2= contents.indexOf("//here");
		String result= contents.substring(0, offset2) + completion + contents.substring(offset2);
		assertEquals(result, doc.get());
	}

	private void assertAppliedTemplateProposal(String contents, TemplateProposal proposal, String expectedContents) {
		int offset= contents.indexOf("//here");
		proposal.apply(fEditor.getViewer(), ';', 0, offset);
		assertEquals(expectedContents, fEditor.getViewer().getDocument().get());
	}

	private ICompilationUnit createCU(IPackageFragment pack1, String contents) throws JavaModelException {
		ICompilationUnit cu= pack1.createCompilationUnit("Completion_" + getName() + ".java", contents, false, null);
		return cu;
	}

	private ICompletionProposal findNonNullProposal(String prefix, ICompilationUnit cu, IRegion selection, int requiredRelevance) throws JavaModelException, PartInitException {
		ICompletionProposal proposal= findNamedProposal(prefix, cu, selection, requiredRelevance);
		assertNotNull("no proposal starting with \"" + prefix + "\"", proposal);
		return proposal;
	}

	private ICompletionProposal findNamedProposal(String prefix, ICompilationUnit cu, IRegion selection, int requiredRelevance) throws JavaModelException, PartInitException {
		ICompletionProposal[] proposals= collectProposals(cu, selection);

		ICompletionProposal found= null;
		for (int i= 0; i < proposals.length; i++) {
			if (proposals[i] instanceof IJavaCompletionProposal)
				if (((IJavaCompletionProposal)proposals[i]).getRelevance() < requiredRelevance)
					continue;
			String displayString= proposals[i].getDisplayString();
			if (displayString.startsWith(prefix)) {
				if (found == null || displayString.equals(prefix))
					found= proposals[i];
			}
		}
		return found;
	}

	private ICompletionProposal[] collectProposals(ICompilationUnit cu, IRegion selection) throws JavaModelException, PartInitException {
		waitBeforeCoreCompletion();
		ContentAssistant assistant= new ContentAssistant();
		assistant.setDocumentPartitioning(IJavaPartitions.JAVA_PARTITIONING);
		IContentAssistProcessor javaProcessor= new JavaCompletionProcessor(fEditor, assistant, getContentType());

		ICompletionProposal[] proposals= javaProcessor.computeCompletionProposals(fEditor.getViewer(), selection.getOffset());
		return proposals;
	}

	/**
	 * Invokes {@link Thread#sleep(long)} if {@link #waitBeforeCompleting(boolean)} was set to
	 * <code>true</code> or camel case completions are enabled. For some reasons, inner types and
	 * camel case matches don't show up otherwise.
	 *
	 * @since 3.2
	 */
	private void waitBeforeCoreCompletion() {
	    if (fWaitBeforeCompleting || JavaCore.ENABLED.equals(JavaCore.getOption(JavaCore.CODEASSIST_CAMEL_CASE_MATCH))) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException x) {
			}
		}
    }

	protected String getContentType() {
		return IDocument.DEFAULT_CONTENT_TYPE;
	}

	private void apply(ITextEditor editor, IDocument doc, ICompletionProposal proposal, IRegion selection) {
		if (proposal instanceof ICompletionProposalExtension2) {
			ICompletionProposalExtension2 ext= (ICompletionProposalExtension2) proposal;
			ITextViewer viewer= (ITextViewer) editor.getAdapter(ITextOperationTarget.class);
			ext.selected(viewer, false);
			viewer.setSelectedRange(selection.getOffset(), selection.getLength());
			ext.apply(viewer, fTrigger, 0, selection.getOffset());
			Point range= proposal.getSelection(doc);
			if (range != null)
				viewer.setSelectedRange(range.x, range.y);
		} else if (proposal instanceof ICompletionProposalExtension) {
			ICompletionProposalExtension ext= (ICompletionProposalExtension) proposal;
			ext.apply(doc, fTrigger, selection.getOffset() + selection.getLength());
		} else {
			proposal.apply(doc);
		}
	}
}
