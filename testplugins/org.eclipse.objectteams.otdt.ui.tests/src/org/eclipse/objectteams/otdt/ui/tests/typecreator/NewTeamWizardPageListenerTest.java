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
package org.eclipse.objectteams.otdt.ui.tests.typecreator;

import junit.framework.TestCase;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.objectteams.otdt.internal.ui.wizards.NewTeamWizardPage;
import org.eclipse.objectteams.otdt.internal.ui.wizards.listeners.NewTeamWizardPageListener;

/**
 * @author anklam
 *
 */
public class NewTeamWizardPageListenerTest extends TestCase
{
	private NewTeamWizardPage _teamWizardPage;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		_teamWizardPage = new NewTeamWizardPage();
		NewTeamWizardPageListener listener = new NewTeamWizardPageListener(_teamWizardPage);
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testValidateTypeName()
	{
		StringDialogField teamName = _teamWizardPage.getTypeNameDialogField();
		// validateTypeName() is called indirectly after setText()
		teamName.setText("TEST");
	}
}
