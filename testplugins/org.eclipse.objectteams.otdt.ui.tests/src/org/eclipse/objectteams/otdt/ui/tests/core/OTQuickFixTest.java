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
 * $Id: OTQuickFixTest.java 23495 2010-02-05 23:15:16Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.core;

import java.io.File;
import java.util.Hashtable;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalModel;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalPositionGroup;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalPositionGroup.Proposal;
import org.eclipse.jdt.internal.ui.text.correction.proposals.LinkedCorrectionProposal;
import org.eclipse.jdt.testplugin.JavaProjectHelper;
import org.eclipse.jdt.testplugin.TestOptions;
import org.eclipse.jdt.ui.tests.quickfix.QuickFixTest;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
import org.eclipse.objectteams.otdt.core.ext.OTJavaNature;
import org.eclipse.objectteams.otdt.core.ext.OTREContainer;

public class OTQuickFixTest extends QuickFixTest {

	protected IJavaProject fJProject1;
	
	protected IPackageFragmentRoot fSourceFolder;
	
	public OTQuickFixTest(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite suite= new TestSuite();
		suite.addTest(ModifierCorrectionsQuickFixTest.suite());
		suite.addTest(CalloutQuickFixTest.suite());
		suite.addTest(CallinQuickFixTest.suite());
		suite.addTest(UnresolvedMethodsQuickFixTest.suite());
		suite.addTest(OTJavadocQuickFixTest.suite());
		suite.addTest(AddImportQuickFixTest.suite());
		suite.addTest(JavaQuickFixTests.suite());
		suite.addTest(PrecedenceQuickFixTest.suite());
		suite.addTest(StatementQuickFixTest.suite());
		return suite;
	}

	public static final String OT_RUNTIME_PATH;
	public static final String OTRE_JAR_PATH;
	static {
		OT_RUNTIME_PATH = JavaCore.getClasspathVariable(OTDTPlugin.OTDT_INSTALLDIR).toOSString();
		OTRE_JAR_PATH = OT_RUNTIME_PATH 
						+ File.separator
						+ "lib" //$NON-NLS-1$
						+ File.separator
						+ "otre.jar"; //$NON-NLS-1$
	}
	
	@SuppressWarnings("unchecked")
	protected void addOptions(Hashtable options) {
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		options.put(JavaCore.COMPILER_PB_STATIC_ACCESS_RECEIVER, JavaCore.ERROR);		
	}

	protected void setUp() throws Exception {
		Hashtable options= TestOptions.getDefaultOptions();
		addOptions(options);
		
		JavaCore.setOptions(options);			
		
		fJProject1= ProjectTestSetup.getProject();
		if (!OTJavaNature.hasOTJavaNature(fJProject1.getProject())) {
			IProjectDescription description = fJProject1.getProject().getDescription();
			description.setNatureIds(OTDTPlugin.createProjectNatures(description));
			fJProject1.getProject().setDescription(description, null);
			OTREContainer.initializeOTJProject(fJProject1.getProject());
		}
		
		fSourceFolder= JavaProjectHelper.addSourceContainer(fJProject1, "src");
	}
	
	protected void tearDown() throws Exception {
		JavaProjectHelper.clear(fJProject1, ProjectTestSetup.getDefaultClasspath());
	}
	
	protected void assertChoices(ICompletionProposal proposal, String linkedGroup, String[] expected) {
		assertTrue("Not a LinkedCorrectionProposal", proposal instanceof LinkedCorrectionProposal);
		LinkedCorrectionProposal linkedProposal = (LinkedCorrectionProposal)proposal;
		
		LinkedProposalModel linkedProposalModel = new ProposalAdaptor().getLinkedProposalModel(linkedProposal);
		LinkedProposalPositionGroup positionGroup = linkedProposalModel.getPositionGroup(linkedGroup, false);
		Proposal[] choices = positionGroup.getProposals();
		assertEquals("Not same number of choices", expected.length, choices.length);
		for (int i=0; i<choices.length; i++) {
			assertEquals("Unexpected choice", expected[i], choices[i].getDisplayString());
		}
	}

}
