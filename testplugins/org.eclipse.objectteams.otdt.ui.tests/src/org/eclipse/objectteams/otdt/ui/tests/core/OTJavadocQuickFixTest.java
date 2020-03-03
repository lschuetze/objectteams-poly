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

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.core.manipulation.CodeTemplateContextType;
import org.eclipse.jdt.internal.core.manipulation.StubUtility;
import org.eclipse.jdt.testplugin.TestOptions;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.correction.CUCorrectionProposal;
import org.eclipse.objectteams.otdt.ui.tests.core.rule.ProjectTestSetup;
import org.eclipse.objectteams.otdt.ui.tests.util.JavaProjectHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * largely inspired by org.eclipse.jdt.ui.tests.quickfix.JavadocQuickFixTest.
 * @author stephan
 * @since 1.2.5
 */
@RunWith(JUnit4.class)
public class OTJavadocQuickFixTest extends OTQuickFixTest {

	@Rule
    public ProjectTestSetup projectsetup = new ProjectTestSetup();

	@Before
	@Override
	public void setUp() throws Exception {
		Hashtable<String,String> options= TestOptions.getDefaultOptions();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		options.put(JavaCore.COMPILER_PB_INVALID_JAVADOC, JavaCore.ERROR);
		options.put(JavaCore.COMPILER_PB_INVALID_JAVADOC_TAGS, JavaCore.ENABLED);
		options.put(JavaCore.COMPILER_PB_MISSING_JAVADOC_TAGS, JavaCore.ERROR);
		options.put(JavaCore.COMPILER_PB_MISSING_JAVADOC_COMMENTS, JavaCore.ERROR);
		options.put(JavaCore.COMPILER_PB_MISSING_JAVADOC_COMMENTS_OVERRIDING, JavaCore.ENABLED);
		JavaCore.setOptions(options);			

		StringBuffer comment= new StringBuffer();
		comment.append("/**\n");
		comment.append(" * A comment.\n");
		comment.append(" * ${tags}\n");
		comment.append(" */");
		String res= comment.toString();
		StubUtility.setCodeTemplate(CodeTemplateContextType.CONSTRUCTORCOMMENT_ID, res, null);
		StubUtility.setCodeTemplate(CodeTemplateContextType.METHODCOMMENT_ID, res, null);
		StubUtility.setCodeTemplate(CodeTemplateContextType.TYPECOMMENT_ID, res, null);
		
		comment= new StringBuffer();
		comment.append("/**\n");
		comment.append(" * A field comment for ${field}.\n");
		comment.append(" */");
		StubUtility.setCodeTemplate(CodeTemplateContextType.FIELDCOMMENT_ID, comment.toString(), null);

		comment= new StringBuffer();
		comment.append("/**\n");
		comment.append(" * A override comment.\n");
		comment.append(" * ${see_to_overridden}\n");
		comment.append(" */");
		StubUtility.setCodeTemplate(CodeTemplateContextType.OVERRIDECOMMENT_ID, comment.toString(), null);
		
		comment= new StringBuffer();
		comment.append("/**\n");
		comment.append(" * A delegate comment.\n");
		comment.append(" * ${see_to_target}\n");
		comment.append(" */");
		StubUtility.setCodeTemplate(CodeTemplateContextType.DELEGATECOMMENT_ID, comment.toString(), null);
		
		fJProject1= ProjectTestSetup.getProject();

		fSourceFolder= JavaProjectHelper.addSourceContainer(fJProject1, "src");
		
//{ObjectTeams: add the OTRE:
		JavaProjectHelper.addLibrary(fJProject1, new Path(OTRE_JAR_PATH));
// SH}
	}
	
	@Test
	public void testMissingRoleTag1() throws Exception {
		IPackageFragment teamPkg = fSourceFolder.createPackageFragment("test1.MyTeam", false, null);
		teamPkg.createCompilationUnit("MyRole.java", 
				"team package test1.MyTeam;\n" +
				"public class MyRole {}\n", 
				true, null);

		IPackageFragment pack1= fSourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("/**\n");
		buf.append(" */\n");
		buf.append("public team class MyTeam {\n");
		buf.append("    private MyRole r = null;\n"); // use field to trigger loading of role file
		buf.append("}\n");
		ICompilationUnit cu= pack1.createCompilationUnit("MyTeam.java", buf.toString(), false, null);
		
		CompilationUnit astRoot= getASTRoot(cu);
		ArrayList<IJavaCompletionProposal> proposals= collectCorrections(cu, astRoot, 1);
		assertNumberOfProposals(proposals, 2);
		assertCorrectLabels(proposals);
		
		CUCorrectionProposal proposal= (CUCorrectionProposal) proposals.get(1);
		String preview1= getPreviewContent(proposal);
				
		buf= new StringBuffer();
		buf.append("package test1;\n");
		buf.append("/**\n");
		buf.append(" * @role MyRole\n");
		buf.append(" */\n");
		buf.append("public team class MyTeam {\n");
		buf.append("    private MyRole r = null;\n");
		buf.append("}\n");
		String expected= buf.toString();
		assertEqualString(preview1, expected);
		
		assertEqualString("Configure problem severity", proposals.get(0).getDisplayString());
	}

}
