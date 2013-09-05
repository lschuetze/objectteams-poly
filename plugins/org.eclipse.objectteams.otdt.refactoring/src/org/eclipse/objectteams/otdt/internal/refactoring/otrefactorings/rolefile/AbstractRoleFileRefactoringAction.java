/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright (c) 2013 GK Software AG.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.rolefile;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.SelectionActionCommon;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;

@SuppressWarnings("restriction")
public abstract class AbstractRoleFileRefactoringAction extends Action {
	/** 
	 * This class defines the common stateful part of {@link MoveToRoleFileRefactoring} (and 
	 * in the future: {@link InlineRoleFileRefactoring.SelectionDispatchAction}). 
	 */
	protected abstract class RoleFileRefactoringActionCommon extends SelectionActionCommon
	{
		public RoleFileRefactoringActionCommon(JavaEditor editor) {
			super(editor);
		}
		
		public RoleFileRefactoringActionCommon(IWorkbenchWindow window) {
			super(window);
		}

		public boolean isRefactoringAvailable() throws JavaModelException {
			if (   fJavaElement != null 
			  	&& fJavaElement.exists() 
			  	&& fJavaElement.isStructureKnown()
			  	&& fJavaElement instanceof IType) 
			{
				IType type = (IType)fJavaElement;
				return OTModelManager.isRole(type);				
			}
			return false;
		}
	}
	
	/** Provide the respective refactoring action to view context menus (editor, package explorer, outline ...) */
	public class SelectionDispatchAction extends org.eclipse.jdt.ui.actions.SelectionDispatchAction 
	{	
		RoleFileRefactoringActionCommon common;
		
		public SelectionDispatchAction(IWorkbenchSite site) {
			super(site);
			common = createRefactoringActionCommon(site.getWorkbenchWindow());
			init();
		}
		
		public SelectionDispatchAction(JavaEditor editor) {
			super(editor.getEditorSite());
			common = createRefactoringActionCommon(editor);
			init();
		}

		void init() {
			setText(commandName);
			setToolTipText(commandTooltip);
			setEnabled(true); // further checking in run().
		}

		@Override
		public void selectionChanged(JavaTextSelection javaTextSelection) {
			try {
				common.selectionChanged(javaTextSelection);
				setEnabled(common.isRefactoringAvailable());
			} catch (CoreException ce) {
				setEnabled(false);
			}
		}
		
		@Override
		public void selectionChanged(IStructuredSelection selection) {
			try {
				common.selectionChanged(selection);
				setEnabled(common.isRefactoringAvailable());
			} catch (JavaModelException e) {
				setEnabled(false);
			}
		}
		
		@Override
		public void selectionChanged(ITextSelection selection) {
			// ignore useless selection, let above methods prevail; further checks in run().
		}
	
		@Override
		public void run() {
			common.run();
		}
	}
	
	// =========== Start AbstractCallinRefactoringAction ===========

	RoleFileRefactoringActionCommon common;
	
	final protected String commandTooltip;
	final protected String commandName;
	
	protected AbstractRoleFileRefactoringAction(String commandName, String commandTooltip) {
		this.commandName = commandName;
		this.commandTooltip = commandTooltip;
	}

	public void init(IWorkbenchWindow window) {
		common = createRefactoringActionCommon(window);
	}

	abstract protected RoleFileRefactoringActionCommon createRefactoringActionCommon(IWorkbenchWindow window);
	abstract protected RoleFileRefactoringActionCommon createRefactoringActionCommon(JavaEditor editor);

	public void dispose() {
		// Do nothing
	}

	public void selectionChanged(IAction action, ISelection selection) {
		try {
			if (selection instanceof IStructuredSelection) {
				common.selectionChanged((IStructuredSelection)selection);
				action.setEnabled(common.isRefactoringAvailable());
			} else if (selection instanceof JavaTextSelection) {
				common.selectionChanged((JavaTextSelection) selection);
				action.setEnabled(common.isRefactoringAvailable());
			} else {
				common.clearJavaElement();
				action.setEnabled(true); // further checks in run().
			}
		} catch (JavaModelException exception) {
			action.setEnabled(false);
		}
	}

	public void run(IAction action) {
		common.run();
	}
}
