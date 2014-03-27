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
package org.eclipse.objectteams.otdt.tests.otjld.roleplaying;

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
public class AllBindingAmbiguitiesTests {

	public static Test suite() {
	
		List<Class<? extends TestCase>> testClasses = new ArrayList<Class<? extends TestCase>>();

		testClasses.add(BindingAmbiguitiesM.testClass());

		testClasses.add(BindingAmbiguities1.testClass());
		testClasses.add(BindingAmbiguities2.testClass());
		testClasses.add(BindingAmbiguities3.testClass());
		testClasses.add(BindingAmbiguities4.testClass());
		testClasses.add(BindingAmbiguities5.testClass());
		testClasses.add(BindingAmbiguities6.testClass());
		testClasses.add(BindingAmbiguities7.testClass());
		testClasses.add(BindingAmbiguities8.testClass());
		testClasses.add(BindingAmbiguities9.testClass());
		testClasses.add(BindingAmbiguities10.testClass());
		testClasses.add(BindingAmbiguities11.testClass());
		testClasses.add(BindingAmbiguities12.testClass());

		// Reset forgotten subsets tests
		TestCase.TESTS_PREFIX = null;
		TestCase.TESTS_NAMES = null;
		TestCase.TESTS_NUMBERS= null;
		TestCase.TESTS_RANGE = null;
		TestCase.RUN_ONLY_ID = null;
		
		return AbstractCompilerTest.buildAllCompliancesTestSuite(AllBindingAmbiguitiesTests.class, RegressionTestSetup.class, testClasses);
	}

	static Test buildSuite(Class testClass) {
		return AbstractOTJLDTest.buildMinimalComplianceTestSuite(testClass, F_1_6);
	}
}
