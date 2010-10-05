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
package org.eclipse.objectteams.otdt.tests.otjld.liftlower;

import static org.eclipse.jdt.core.tests.util.AbstractCompilerTest.F_1_6;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author stephan
 */
public class AllSmartLiftingTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("All Smart Lifting Tests");
		suite.addTest(buildSuite(SmartLifting1.testClass()));
		suite.addTest(buildSuite(SmartLifting2.testClass()));
		suite.addTest(buildSuite(SmartLifting3.testClass()));
		suite.addTest(buildSuite(SmartLifting4.testClass()));
		suite.addTest(buildSuite(SmartLifting5.testClass()));
		suite.addTest(buildSuite(SmartLifting6.testClass()));
		suite.addTest(buildSuite(SmartLifting7.testClass()));
		suite.addTest(buildSuite(SmartLifting8.testClass()));
		suite.addTest(buildSuite(SmartLifting9.testClass()));
		suite.addTest(buildSuite(SmartLifting10.testClass()));
		suite.addTest(buildSuite(SmartLifting11.testClass()));
		suite.addTest(buildSuite(SmartLifting12.testClass()));
		suite.addTest(buildSuite(SmartLifting13.testClass()));
		suite.addTest(buildSuite(SmartLifting14.testClass()));
		suite.addTest(buildSuite(SmartLifting15.testClass()));
		suite.addTest(buildSuite(SmartLifting16.testClass()));
		suite.addTest(buildSuite(SmartLifting17.testClass()));
		suite.addTest(buildSuite(SmartLifting18.testClass()));
		suite.addTest(buildSuite(SmartLifting19.testClass()));
		suite.addTest(buildSuite(SmartLifting20.testClass()));
		suite.addTest(buildSuite(SmartLifting21.testClass()));
		suite.addTest(buildSuite(SmartLifting22.testClass()));
		suite.addTest(buildSuite(SmartLifting23.testClass()));
		suite.addTest(buildSuite(SmartLifting24.testClass()));
		suite.addTest(buildSuite(SmartLifting25.testClass()));
		suite.addTest(buildSuite(SmartLifting26.testClass()));
		suite.addTest(buildSuite(SmartLifting27.testClass()));
		suite.addTest(buildSuite(SmartLifting28.testClass()));
		suite.addTest(buildSuite(SmartLifting29.testClass()));
		suite.addTest(buildSuite(SmartLifting30.testClass()));

		return suite;
	}

	static Test buildSuite(Class testClass) {
		return AbstractOTJLDTest.buildMinimalComplianceTestSuite(testClass, F_1_6);
	}
}
