package org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.extractcallin;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.AbstractCallinRefactoringAction;
import org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.OTRefactoringMessages;
import org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.AbstractCallinRefactoringAction.SelectionDispatchAction;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Action classes for the extract callin refactoring:
 * <ul>
 * <li>The toplevel class provides the action to the main menu bar (registered via plugin.xml).</li>
 * <li>Inherited nested class {@link SelectionDispatchAction} provides the action to context menus of all views.</li>
 * <li>Nested class {@link ExtractCallinActionCommon} implements the shared stateful part of both actions.</li>
 * </ul>
 */
@SuppressWarnings("restriction")
public class ExtractCallinAction extends AbstractCallinRefactoringAction implements IWorkbenchWindowActionDelegate 
{
	/** 
	 * Fill inherited class {@link CallinRefactoringActionCommon} with details.
	 */
	class ExtractCallinActionCommon extends CallinRefactoringActionCommon
	{
		ExtractCallinActionCommon(JavaEditor editor) {
			super(editor);
		}
		
		ExtractCallinActionCommon(IWorkbenchWindow window) {
			super(window);
		}

		protected void filterElements(Object[] elements) {
			this.fJavaElement = null;
			if (elements == null || elements.length != 1)
				return;
			else if (elements[0] instanceof IMethod)
				this.fJavaElement = (IMethod) elements[0];
		}

		protected void doRun() {
			if (fJavaElement != null && fWindow != null) {
				try {
					new RefactoringWizardOpenOperation(
							new ExtractCallinWizard(
									new ExtractCallinRefactoring((IMethod)fJavaElement), 
									OTRefactoringMessages.ExtractCallin_extractCallin_name
							)
					).run(fWindow.getShell(), OTRefactoringMessages.ExtractCallin_extractCallin_name);
				} catch (InterruptedException exception) {
					// Do nothing
				}
			} else {
				MessageDialog.openInformation(null, OTRefactoringMessages.ExtractCallin_extractCallin_name, OTRefactoringMessages.ExtractCallin_notAvailableOnSelection_error);
			}		
		}
	}

	// =========== Start ExtractCallinAction ===========
	public ExtractCallinAction() {
		super(OTRefactoringMessages.ExtractCallin_extractCallin_commandName, OTRefactoringMessages.ExtractCallin_extractCallin_tooltip);
	}
	
	protected CallinRefactoringActionCommon createRefactoringActionCommon(IWorkbenchWindow window) {
		return new ExtractCallinActionCommon(window);
	}

	protected CallinRefactoringActionCommon createRefactoringActionCommon(JavaEditor editor) {
		return new ExtractCallinActionCommon(editor);
	}
}