/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010, 2014 Stephan Herrmann
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.liftlower;

import static org.eclipse.jdt.core.tests.util.AbstractCompilerTest.F_1_6;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.eclipse.jdt.core.tests.compiler.regression.RegressionTestSetup;
import org.eclipse.jdt.core.tests.junit.extension.TestCase;
import org.eclipse.jdt.core.tests.util.AbstractCompilerTest;
import org.eclipse.objectteams.otdt.tests.otjld.AbstractOTJLDTest;

/**
 * @author stephan
 */
public class AllSmartLiftingTests {

	public static Test suite() {
		List<Class<? extends TestCase>> testClasses = new ArrayList<Class<? extends TestCase>>();

		testClasses.add(SmartLifting1.testClass());
		testClasses.add(SmartLifting2.testClass());
		testClasses.add(SmartLifting3.testClass());
		testClasses.add(SmartLifting4.testClass());
		testClasses.add(SmartLifting5.testClass());
		testClasses.add(SmartLifting6.testClass());
		testClasses.add(SmartLifting7.testClass());
		testClasses.add(SmartLifting8.testClass());
		testClasses.add(SmartLifting9.testClass());
		testClasses.add(SmartLifting10.testClass());
		testClasses.add(SmartLifting11.testClass());
		testClasses.add(SmartLifting12.testClass());
		testClasses.add(SmartLifting13.testClass());
		testClasses.add(SmartLifting14.testClass());
		testClasses.add(SmartLifting15.testClass());
		testClasses.add(SmartLifting16.testClass());
		testClasses.add(SmartLifting17.testClass());
		testClasses.add(SmartLifting18.testClass());
		testClasses.add(SmartLifting19.testClass());
		testClasses.add(SmartLifting20.testClass());
		testClasses.add(SmartLifting21.testClass());
		testClasses.add(SmartLifting22.testClass());
		testClasses.add(SmartLifting23.testClass());
		testClasses.add(SmartLifting24.testClass());
		testClasses.add(SmartLifting25.testClass());
		testClasses.add(SmartLifting26.testClass());
		testClasses.add(SmartLifting27.testClass());
		testClasses.add(SmartLifting28.testClass());
		testClasses.add(SmartLifting29.testClass());
		testClasses.add(SmartLifting30.testClass());


		// Reset forgotten subsets tests
		TestCase.TESTS_PREFIX = null;
		TestCase.TESTS_NAMES = null;
		TestCase.TESTS_NUMBERS= null;
		TestCase.TESTS_RANGE = null;
		TestCase.RUN_ONLY_ID = null;
		
		return AbstractCompilerTest.buildAllCompliancesTestSuite(AllSmartLiftingTests.class, RegressionTestSetup.class, testClasses);
	}

	static Test buildSuite(Class testClass) {
		return AbstractOTJLDTest.buildMinimalComplianceTestSuite(testClass, F_1_6);
	}
}
