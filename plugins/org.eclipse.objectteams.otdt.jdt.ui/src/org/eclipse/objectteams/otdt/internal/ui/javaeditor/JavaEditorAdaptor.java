/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2009 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
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
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.javaeditor;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.manipulation.SharedASTProviderCore;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.objectteams.otdt.core.OTModelManager;

import base org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import base org.eclipse.jdt.internal.ui.text.CombinedWordRule.WordMatcher;
import base org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;


/**
 * This team adapts the JDT Java Editor for OT/J.
 * It replaces the former OTJavaEditor, OTJavaTextTools and OTJavaSourceViewerConfiguration classes.
 * 
 * @author mosconi
 * @since 1.3.0
 */
@SuppressWarnings({"restriction", "inferredcallout", "decapsulation"})
public team class JavaEditorAdaptor {

	protected class CompilationUnitEditor playedBy CompilationUnitEditor {
		// this instance manages the override indicator for role classes:
		RoleOverrideIndicatorManager fRoleOverrideIndicatorManager;
		
		uninstallOverrideIndicator <- after uninstallOverrideIndicator;
		protected void uninstallOverrideIndicator() {
			if (fRoleOverrideIndicatorManager != null) {
				fRoleOverrideIndicatorManager.removeAnnotations();
				fRoleOverrideIndicatorManager = null;
			}
		}
		
		installOverrideIndicator <- after installOverrideIndicator;
		protected void installOverrideIndicator(boolean provideAST) {
			// verbatim repeat:
			IAnnotationModel model = getDocumentProvider().getAnnotationModel(getEditorInput());
			final ITypeRoot inputElement = getInputJavaElement();
			if (model == null || inputElement == null)
				return;
			//own:
			IType primaryType = inputElement.findPrimaryType();
			if (primaryType != null && !OTModelManager.hasOTElementFor(primaryType))
				return; // plain Java

			fRoleOverrideIndicatorManager = new RoleOverrideIndicatorManager(model, inputElement, null);

			if (provideAST) {
				CompilationUnit ast = SharedASTProviderCore.getAST(inputElement, SharedASTProviderCore.WAIT_ACTIVE_ONLY, getProgressMonitor());
				fRoleOverrideIndicatorManager.reconciled(ast, true, getProgressMonitor());
			}
			addReconcileListener(fRoleOverrideIndicatorManager);
		}
	}

	final static String LAST_JAVA_KEYWORD = "while"; //$NON-NLS-1$
	final static String WITHIN_KEYWORD    = "within"; //$NON-NLS-1$

	protected team class JavaSourceViewerConfiguration playedBy JavaSourceViewerConfiguration 
	{
		callin void initializeScanners() {
			within (this)
				base.initializeScanners();
		}
		initializeScanners <- replace initializeScanners;
		
		
		/** Add the OT/J keyword 'within' to the matcher. This role is only active during initializeScanners(). */
		protected class WordMatcher playedBy WordMatcher {
			
			void addWord(String word, IToken token) <- replace void addWord(String word, IToken token);
			
			@SuppressWarnings("basecall") // multiple
			callin void addWord(String word, IToken token) {
				base.addWord(word, token);
				if (LAST_JAVA_KEYWORD.equals(word)) {
					base.addWord(WITHIN_KEYWORD, token); // in plain java mode "within" is our only keyword
				}
			}
		}
	}
}
