/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2020 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.ui.tests.refactoring.reorg;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author brcan
 *
 * Runs all OT-specific refactoring tests and corresponding eclipse refactoring tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	//copy & paste, delete (cut)
	org.eclipse.objectteams.otdt.ui.tests.refactoring.reorg.OTCopyToClipboardTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.reorg.OTDeleteTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.reorg.PasteActionTest.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.reorg.OTPasteActionTest.class
})
public class AllTests {
}
