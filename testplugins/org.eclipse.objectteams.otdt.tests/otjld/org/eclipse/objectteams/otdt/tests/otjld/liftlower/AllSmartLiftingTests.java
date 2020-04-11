/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2010, 2014 Stephan Herrmann
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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otjld.liftlower;

import static org.eclipse.objectteams.otdt.tests.otjld.AllTests.addComplianceSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author stephan
 */
public class AllSmartLiftingTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("All Smart Lifting Tests");

		addComplianceSuite(suite, SmartLifting1.testClass());
		addComplianceSuite(suite, SmartLifting2.testClass());
		addComplianceSuite(suite, SmartLifting3.testClass());
		addComplianceSuite(suite, SmartLifting4.testClass());
		addComplianceSuite(suite, SmartLifting5.testClass());
		addComplianceSuite(suite, SmartLifting6.testClass());
		addComplianceSuite(suite, SmartLifting7.testClass());
		addComplianceSuite(suite, SmartLifting8.testClass());
		addComplianceSuite(suite, SmartLifting9.testClass());
		addComplianceSuite(suite, SmartLifting10.testClass());
		addComplianceSuite(suite, SmartLifting11.testClass());
		addComplianceSuite(suite, SmartLifting12.testClass());
		addComplianceSuite(suite, SmartLifting13.testClass());
		addComplianceSuite(suite, SmartLifting14.testClass());
		addComplianceSuite(suite, SmartLifting15.testClass());
		addComplianceSuite(suite, SmartLifting16.testClass());
		addComplianceSuite(suite, SmartLifting17.testClass());
		addComplianceSuite(suite, SmartLifting18.testClass());
		addComplianceSuite(suite, SmartLifting19.testClass());
		addComplianceSuite(suite, SmartLifting20.testClass());
		addComplianceSuite(suite, SmartLifting21.testClass());
		addComplianceSuite(suite, SmartLifting22.testClass());
		addComplianceSuite(suite, SmartLifting23.testClass());
		addComplianceSuite(suite, SmartLifting24.testClass());
		addComplianceSuite(suite, SmartLifting25.testClass());
		addComplianceSuite(suite, SmartLifting26.testClass());
		addComplianceSuite(suite, SmartLifting27.testClass());
		addComplianceSuite(suite, SmartLifting28.testClass());
		addComplianceSuite(suite, SmartLifting29.testClass());
		addComplianceSuite(suite, SmartLifting30.testClass());

		return suite;
	}
}
