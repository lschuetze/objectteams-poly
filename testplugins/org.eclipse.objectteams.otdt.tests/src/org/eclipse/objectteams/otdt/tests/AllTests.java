/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2018 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.tests;

import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests
{

	public static Test suite()
    {
		TestSuite suite = new TestSuite("All OTDT Tests without UI");
		//$JUnit-BEGIN$

		//Parser
		suite.addTest(org.eclipse.objectteams.otdt.tests.parser.AllTests.suite());

		//compiler
		suite.addTest(org.eclipse.objectteams.otdt.tests.compiler.errorreporting.TestAll.suite());
		suite.addTest(org.eclipse.objectteams.otdt.tests.compiler.ast.TypeDeclarationTest.suite());
		suite.addTest(org.eclipse.objectteams.otdt.tests.compiler.smap.AllTests.suite());
		suite.addTest(org.eclipse.objectteams.otdt.tests.compiler.AttributesTest.suite());

		//hierarchy
		suite.addTest(org.eclipse.objectteams.otdt.tests.hierarchy.AllTests.suite());

		//model
		suite.addTest(org.eclipse.objectteams.otdt.tests.otmodel.AllTests.suite());

		//search
		suite.addTest(org.eclipse.objectteams.otdt.tests.search.AllTests.suite());

		//completion
		suite.addTest(org.eclipse.objectteams.otdt.tests.model.AllTests.suite());

		//formatter
		suite.addTest(org.eclipse.objectteams.otdt.core.tests.formatter.FormatterTests.suite());

		//selection
		suite.addTest(org.eclipse.objectteams.otdt.tests.selection.AllTests.suite());

		//builder
		// tests moved to separate plug-in, needs OT/Equinox to enable compiler.adaptor.

		// the OTJLD suite:
		suite.addTest(org.eclipse.objectteams.otdt.tests.otjld.AllTests.suite());

		//$JUnit-END$
		return suite;
    }
}
