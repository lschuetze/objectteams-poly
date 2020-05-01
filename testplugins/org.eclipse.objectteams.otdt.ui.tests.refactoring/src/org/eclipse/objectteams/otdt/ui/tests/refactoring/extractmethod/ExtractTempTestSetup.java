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
package org.eclipse.objectteams.otdt.ui.tests.refactoring.extractmethod;

import java.util.Hashtable;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.objectteams.otdt.ui.tests.util.JavaProjectHelper;
import org.eclipse.objectteams.otdt.ui.tests.util.TestOptions;
import org.junit.rules.ExternalResource;

/**
 * @author stephan
 */
public class ExtractTempTestSetup extends ExternalResource
{
	private IJavaProject _javaProject;
	private IPackageFragmentRoot _root;
	private static final String CONTAINER = "src";

	private IPackageFragment _statementsPackage;

	public IPackageFragmentRoot getRoot() {
		return _root;
	}

    @SuppressWarnings("restriction")
	protected void before() throws Exception
    {
		Hashtable<String, String> options = TestOptions.getFormatterOptions();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "0");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		JavaCore.setOptions(options);
		TestOptions.initializeCodeGenerationOptions();
		JavaPlugin.getDefault().getCodeTemplateStore().load();

		_javaProject = JavaProjectHelper.createOTJavaProject("TestProject", "bin");
		JavaProjectHelper.addRTJar(_javaProject);
		_root = JavaProjectHelper.addSourceContainer(_javaProject, CONTAINER);

		RefactoringCore.getUndoManager().flush();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceDescription description = workspace.getDescription();
		description.setAutoBuilding(false);
		workspace.setDescription(description);

		//new packages for OT/J tests
		_statementsPackage = getRoot().createPackageFragment("statements_in", true, null);
    }

	@SuppressWarnings("restriction")
	protected void after() {
		try {
			JavaProjectHelper.performDummySearch(_javaProject);
			JavaProjectHelper.delete(_javaProject);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public IPackageFragment getStatementsPackage()
	{
	    return _statementsPackage;
	}
}
