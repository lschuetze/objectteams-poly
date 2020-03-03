/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 GK Software AG
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.refactoring.adapt;

import java.io.IOException;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.ParameterInfo;

import base org.eclipse.jdt.ui.tests.refactoring.ChangeSignatureTests;
import base org.eclipse.jdt.ui.tests.refactoring.RefactoringTest;

@SuppressWarnings("restriction")
public team class JdtTests {
	
	protected team class ChangeSignature playedBy ChangeSignatureTests {
		
		final static String OT_NAME = "a_of_string";
		
		testGenerics04 <- replace testGenerics04;
		
		@SuppressWarnings({ "inferredcallout", "basecall", "decapsulation" })
		callin void testGenerics04() throws Exception {
			String[] signature= {"QList<QInteger;>;", "QA<QString;>;"};
			String[] newNames= {"li"};
			String[] newTypes= {"List<Integer>"};
			String[] newDefaultValues= {"null"};
			ParameterInfo[] newParamInfo= createNewParamInfos(newTypes, newNames, newDefaultValues);
			int[] newIndices= {2};

//{ObjectTeams: avoid keyword "as" - renamed to "a_of_string"	
			String[] oldParamNames= {"li", OT_NAME};
			String[] newParamNames= {"li", OT_NAME};
// SH}
			String[] newParameterTypeNames= null;
			int[] permutation= {1, 2, 0};
			int[] deletedIndices= {0};
			int newVisibility= Modifier.PUBLIC;
			String newReturnTypeName= null;
			within (this) // OT: enable nested role ContentPatching
				helperDoAll("A", "m", signature, newParamInfo, newIndices, oldParamNames, newParamNames, newParameterTypeNames, permutation, newVisibility, deletedIndices, newReturnTypeName);
		}
		
		protected class ContentPatching playedBy RefactoringTest {

			String getFileContents(String fileName) <- replace String getFileContents(String fileName);

			callin String getFileContents(String fileName) throws IOException {
				String orig = base.getFileContents(fileName);
				// word "as" appears after " ", "\t" and "(", but don't replace in "class":
				return orig.replaceAll("([ \t(])as", "$1"+OT_NAME);
			}			
		}
	}
}
