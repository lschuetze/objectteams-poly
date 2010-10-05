/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 Stephan Herrmann
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: AllTests.java 23529 2010-02-18 23:06:04Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.roleplaying;

import static org.eclipse.jdt.core.tests.util.AbstractCompilerTest.F_1_6;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author stephan
 */
public class AllBindingAmbiguitiesTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("All Binding Ambiguities Tests");
		suite.addTest(buildSuite(BindingAmbiguitiesM.testClass()));

		suite.addTest(buildSuite(BindingAmbiguities1.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities2.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities3.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities4.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities5.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities6.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities7.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities8.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities9.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities10.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities11.testClass()));
		suite.addTest(buildSuite(BindingAmbiguities12.testClass()));

		return suite;
	}

	static Test buildSuite(Class testClass) {
		return AbstractOTJLDTest.buildMinimalComplianceTestSuite(testClass, F_1_6);
	}
}
