/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2013 (c) GK Software AG
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.rolefile;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.IUndoManager;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.internal.ui.refactoring.RefactoringStatusDialog;
import org.eclipse.objectteams.otdt.internal.refactoring.OTRefactoringPlugin;
import org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.OTRefactoringMessages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Action classes for the move to role file refactoring:
 * <ul>
 * <li>The toplevel class provides the action to the main menu bar (registered via plugin.xml).</li>
 * <li>Inherited nested class {@link SelectionDispatchAction} provides the action to context menus of all views.</li>
 * <li>Nested class {@link MoveToRoleFileActionCommon} implements the shared stateful part of both actions.</li>
 * </ul>
 */
@SuppressWarnings("restriction")
public class MoveToRoleFileAction extends AbstractRoleFileRefactoringAction implements IWorkbenchWindowActionDelegate 
{	
	/** 
	 * Fill inherited class {@link CallinRefactoringActionCommon} with details.
	 */
	class MoveToRoleFileActionCommon extends RoleFileRefactoringActionCommon
	{
		MoveToRoleFileActionCommon(JavaEditor editor) {
			super(editor);
		}
		
		MoveToRoleFileActionCommon(IWorkbenchWindow window) {
			super(window);
		}

		protected void filterElements(Object[] elements) {
			this.fJavaElement = null;
			if (elements == null || elements.length != 1)
				return;
			else if (elements[0] instanceof IType)
				this.fJavaElement = (IType) elements[0];
		}

		protected void doRun() {
			if (fJavaElement != null && fWindow != null) {
				MoveToRoleFileRefactoring refactoring = new MoveToRoleFileRefactoring((IType)fJavaElement);
				RefactoringStatus status = null;
				try {
					trying: {
						status = refactoring.checkInitialConditions(new NullProgressMonitor());
						if (!status.isOK()) break trying;
						status = refactoring.checkFinalConditions(new NullProgressMonitor());
						if (!status.isOK()) break trying;
						performChange(refactoring);
						return;
					}							
				} catch (OperationCanceledException e) {
					// do nothing
					return;
				} catch (CoreException e) {
					OTRefactoringPlugin.getInstance().getLog().log(new Status(IStatus.ERROR, OTRefactoringPlugin.PLUGIN_ID,
											OTRefactoringMessages.MoveToRoleFile_exception, e));
					status.addError(OTRefactoringMessages.MoveToRoleFile_exception_seeLog);
				}
				RefactoringStatusDialog dialog= new RefactoringStatusDialog(status,
						fWindow.getShell(),
						OTRefactoringMessages.MoveToRoleFile_errorDialog_title,
						false);
				dialog.open();
			} else {
				MessageDialog.openInformation(null, 
											  OTRefactoringMessages.MoveToRoleFileRefactoring_moveToRoleFile_name, 
											  OTRefactoringMessages.MoveToRoleFile_notAvailableOnSelection_error);
			}
		}
	}
	
	// from test

    protected final void performChange(final Refactoring refactoring) throws CoreException {
        CreateChangeOperation create = new CreateChangeOperation(refactoring);
        PerformChangeOperation perform = new PerformChangeOperation(create);
        perform.setUndoManager(getUndoManager(), refactoring.getName());
        ResourcesPlugin.getWorkspace().run(perform, new NullProgressMonitor());
    }

    protected IUndoManager getUndoManager() {
        IUndoManager undoManager = RefactoringCore.getUndoManager();
        undoManager.flush();
        return undoManager;
    }

	
	// =========== Start MoveToRoleFileAction ===========
	
	public MoveToRoleFileAction () {
		super(OTRefactoringMessages.MoveToRoleFile_movetoRolefile_commandName, OTRefactoringMessages.MoveToRoleFile_movetoRolefile_tooltip);
	}

	protected RoleFileRefactoringActionCommon createRefactoringActionCommon(IWorkbenchWindow window) {
		return new MoveToRoleFileActionCommon(window);
	}

	protected RoleFileRefactoringActionCommon createRefactoringActionCommon(JavaEditor editor) {
		return new MoveToRoleFileActionCommon(editor);
	}
}