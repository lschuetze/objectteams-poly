package org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.extractcallin;

import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.OTRefactoringMessages;

public class ExtractCallinWizard extends RefactoringWizard {

	public ExtractCallinWizard(ExtractCallinRefactoring refactoring, String pageTitle) {
		super(refactoring, DIALOG_BASED_USER_INTERFACE | PREVIEW_EXPAND_FIRST_NODE);
		setDefaultPageTitle(pageTitle);
	}

	@Override
	protected void addUserInputPages() {
		addPage(new ExtractCallinInputPage(OTRefactoringMessages.ExtractCallinWizard_extractCallinInput_pageName));
	}
}