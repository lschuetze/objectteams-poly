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
package org.eclipse.objectteams.otdt.ui.tests.refactoring;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author brcan
 *
 * Runs all OT-specific refactoring tests and corresponding eclipse refactoring tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	org.eclipse.objectteams.otdt.ui.tests.refactoring.util.AllTests.class,
	
	// OTDT refactoring tests
	// rename
	org.eclipse.objectteams.otdt.ui.tests.refactoring.rename.RenameMethodInInterfaceTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.rename.RenamePrivateMethodTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.rename.RenameStaticMethodTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.rename.RenameVirtualMethodInClassTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.rename.RenamePrivateFieldTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.rename.RenameTypeTests.class,
	
	// move
	org.eclipse.objectteams.otdt.ui.tests.refactoring.move.MoveInstanceMethodTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.move.MoveStaticMethodTests.class,
	
	//extract
	org.eclipse.objectteams.otdt.ui.tests.refactoring.extractmethod.ExtractMethodTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.extractmethod.ExtractMethodRefactoringUtilTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.extractmethod.ExtractTempTests.class,
	
	//copy & paste, delete (cut)
    org.eclipse.objectteams.otdt.ui.tests.refactoring.reorg.OTCopyToClipboardTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.reorg.OTDeleteTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.reorg.OTPasteActionTest.class,
	
	// pull & push
	org.eclipse.objectteams.otdt.ui.tests.refactoring.pullup.PullUpTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.pushdown.PushDownTests.class,
	
	// ot refactorings
	org.eclipse.objectteams.otdt.ui.tests.refactoring.inlinecallin.InlineCallinTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.extractcallin.ExtractCallinTests.class,
})
public class AllTests {
}
