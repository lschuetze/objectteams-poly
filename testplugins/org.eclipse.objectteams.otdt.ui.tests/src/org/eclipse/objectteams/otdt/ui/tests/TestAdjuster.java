/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014 GK Software AG
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
package org.eclipse.objectteams.otdt.ui.tests;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;

import base org.eclipse.jdt.ui.tests.quickfix.LocalCorrectionsQuickFixTest;
import base org.eclipse.jdt.ui.tests.quickfix.QuickFixTest;

/**
 * Adjust individual tests in org.eclipse.jdt.ui.tests
 */
public team class TestAdjuster {

	protected team class LocalCorrections playedBy LocalCorrectionsQuickFixTest {

		/** In this test OT/J produces different number of syntax errors. */
		void changeNumberOfExpectedProblems() <- replace void testNoFixFor_ParsingErrorInsertToComplete();

		callin void changeNumberOfExpectedProblems() throws Exception {
			within(this)
				base.changeNumberOfExpectedProblems();
		}
		
		protected class Collect playedBy QuickFixTest {

			collectAllCorrections <- replace collectAllCorrections;

			static callin void collectAllCorrections(ICompilationUnit iCompilationUnit, CompilationUnit compilationUnit, int nProblems)
					throws CoreException
			{
				base.collectAllCorrections(iCompilationUnit, compilationUnit, nProblems == 9 ? 10 : nProblems);
			}
		}		
	}
}
