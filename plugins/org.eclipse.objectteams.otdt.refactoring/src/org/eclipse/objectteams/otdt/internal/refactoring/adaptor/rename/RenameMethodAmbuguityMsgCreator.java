package org.eclipse.objectteams.otdt.internal.refactoring.adaptor.rename;

import org.eclipse.objectteams.otdt.internal.refactoring.RefactoringMessages;
import org.eclipse.objectteams.otdt.internal.refactoring.util.IAmbuguityMessageCreator;

public class RenameMethodAmbuguityMsgCreator implements IAmbuguityMessageCreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.objectteams.otdt.refactoring.util.IAmbuguityMessageCreator#createAmbiguousMethodSpecifierMsg()
	 */
	public String createAmbiguousMethodSpecifierMsg() {
		return RefactoringMessages.RenameMethodAmbuguityMsgCreator_ambiguousMethodSpec_error;
	}

}
