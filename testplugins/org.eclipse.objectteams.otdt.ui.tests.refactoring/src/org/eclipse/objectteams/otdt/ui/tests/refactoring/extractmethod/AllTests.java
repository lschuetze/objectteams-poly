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


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author brcan
 *
 * Runs all OT-specific refactoring tests and corresponding eclipse refactoring tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	org.eclipse.objectteams.otdt.ui.tests.refactoring.extractmethod.ExtractMethodTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.extractmethod.ExtractMethodRefactoringUtilTests.class,
	org.eclipse.objectteams.otdt.ui.tests.refactoring.extractmethod.ExtractTempTests.class,
})
public class AllTests {

}
