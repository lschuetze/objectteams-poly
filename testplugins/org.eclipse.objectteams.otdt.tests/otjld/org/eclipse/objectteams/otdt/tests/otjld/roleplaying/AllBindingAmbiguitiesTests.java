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
package org.eclipse.objectteams.otdt.tests.otjld.roleplaying;

import static org.eclipse.objectteams.otdt.tests.otjld.AllTests.addComplianceSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author stephan
 */
public class AllBindingAmbiguitiesTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("All Binding Ambiguity Tests");

		addComplianceSuite(suite, BindingAmbiguitiesM.testClass());

		addComplianceSuite(suite, BindingAmbiguities1.testClass());
		addComplianceSuite(suite, BindingAmbiguities2.testClass());
		addComplianceSuite(suite, BindingAmbiguities3.testClass());
		addComplianceSuite(suite, BindingAmbiguities4.testClass());
		addComplianceSuite(suite, BindingAmbiguities5.testClass());
		addComplianceSuite(suite, BindingAmbiguities6.testClass());
		addComplianceSuite(suite, BindingAmbiguities7.testClass());
		addComplianceSuite(suite, BindingAmbiguities8.testClass());
		addComplianceSuite(suite, BindingAmbiguities9.testClass());
		addComplianceSuite(suite, BindingAmbiguities10.testClass());
		addComplianceSuite(suite, BindingAmbiguities11.testClass());
		addComplianceSuite(suite, BindingAmbiguities12.testClass());
		
		return suite;
	}
}
