/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2020, Stephan Herrmann
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
package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.TestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	test537351.TestBug537351.class,
	test537533.TestBug537533.class,
	test537350_a.TestBug537350_A.class,
	base537350_b.TestBug537350_B.class,
	aspect571542.b.Test571542.class
})
public class AllOTEquinoxTests extends TestSuite { }
