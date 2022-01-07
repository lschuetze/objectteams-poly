/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2021 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.tests.compiler.errorreporting;

import junit.framework.TestSuite;

/**
 * This class collects all test cases in one test suite.
 *
 * @author brcan
 */
public class TestAll extends TestSuite
{
    public TestAll()
    {
        super();
    }

    public static TestSuite suite()
    {
    	TestSuite suite = new TestSuite("All Compiler Error Reporting Tests");

		// containment tests
		suite.addTestSuite(CompilationUnitContainmentTest.class);
		suite.addTestSuite(RoleContainmentTest.class);
		suite.addTestSuite(TeamContainmentTest.class);
		// inheritance tests
		suite.addTestSuite(RoleBaseInheritanceTest.class);
		suite.addTestSuite(RoleInheritanceTest.class);
		suite.addTestSuite(TeamInheritanceTest.class);
		// Copy Inheritance
		suite.addTestSuite(CopyInheritanceTest.class);
    	// binding tests
    	suite.addTestSuite(CallinBindingTest.class);
		suite.addTestSuite(CalloutBindingTest.class);
    	// lifting and lowering tests
    	suite.addTestSuite(LiftingTest.class);
    	suite.addTestSuite(LoweringTest.class);
    	// parameter mapping tests
    	suite.addTestSuite(ParameterMappingsTest.class);
		// team activation tests
		suite.addTestSuite(TeamActivationTest.class);
    	// parser tests
		suite.addTestSuite(ParserTest.class);
		//Code snippets and wrong code tests
		suite.addTestSuite(SourceSnippetRecoveryTest.class);
		//External Role tests
		suite.addTestSuite(ExternalRoleTest.class);


    	return suite;
    }
}
