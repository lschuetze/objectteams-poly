/**
 * 
 */
package org.eclipse.objectteams.otdt.internal.refactoring.adaptor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.refactoring.descriptors.ExtractClassDescriptor;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.objectteams.otdt.internal.refactoring.util.RefactoringUtil;

import base org.eclipse.jdt.internal.corext.refactoring.code.InlineConstantRefactoring;
import base org.eclipse.jdt.internal.corext.refactoring.code.InlineMethodRefactoring;
import base org.eclipse.jdt.internal.corext.refactoring.code.IntroduceIndirectionRefactoring;
import base org.eclipse.jdt.internal.corext.refactoring.structure.ChangeSignatureProcessor;
import base org.eclipse.jdt.internal.corext.refactoring.structure.ChangeTypeRefactoring;
import base org.eclipse.jdt.internal.corext.refactoring.structure.ExtractClassRefactoring;
import base org.eclipse.jdt.internal.corext.refactoring.structure.IntroduceParameterObjectProcessor;

/**
 * This team holds roles for refactorings that are not yet fully ot-aware. It adds an refactoring info status if any roles exist within the target project.   
 * 
 * @author Johannes Gebauer
 * 
 */
@SuppressWarnings("restriction")
public team class NotSupportedRefactoringAdaptor {
	
	protected class IntroduceIndirectionRefactoring playedBy IntroduceIndirectionRefactoring {
		
		IJavaProject getProject() -> IJavaProject getProject();
		
		private void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) when(!status.hasFatalError()) {
			status.merge(RefactoringUtil.checkForExistingRoles(RefactoringCoreMessages.IntroduceIndirectionRefactoring_introduce_indirection_name, getProject(),pm));
		}

		void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) <- after RefactoringStatus checkFinalConditions(
				IProgressMonitor pm) with {
			pm <- pm,
			status <- result
		}
	}
	
	protected class InlineMethodRefactoring playedBy InlineMethodRefactoring {
		@SuppressWarnings("decapsulation")
		ITypeRoot getFInitialTypeRoot() -> get ITypeRoot fInitialTypeRoot;

		private void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) when(!status.hasFatalError()) {
			status.merge(RefactoringUtil.checkForExistingRoles(RefactoringCoreMessages.InlineMethodRefactoring_name, getFInitialTypeRoot().getJavaProject(), pm));
		}

		void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) <- after RefactoringStatus checkFinalConditions(IProgressMonitor pm) with {
			pm <- pm,
			status <- result
		}
	}
	
	protected class InlineConstantRefactoring playedBy InlineConstantRefactoring {
		@SuppressWarnings("decapsulation")
		IField getFField() -> get IField fField;

		private void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) when(!status.hasFatalError()) {
			status.merge(RefactoringUtil.checkForExistingRoles(RefactoringCoreMessages.InlineConstantRefactoring_name, getFField().getJavaProject(), pm));
		}

		void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) <- after RefactoringStatus checkFinalConditions(IProgressMonitor pm) with {
			pm <- pm,
			status <- result
		}
	}

	protected class ExtractClassRefactoring playedBy ExtractClassRefactoring {
		@SuppressWarnings("decapsulation")
		ExtractClassDescriptor getFDescriptor() -> get ExtractClassDescriptor fDescriptor;

		private void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) when(!status.hasFatalError()) {
			status.merge(RefactoringUtil.checkForExistingRoles(RefactoringCoreMessages.ExtractClassRefactoring_refactoring_name, getFDescriptor().getType().getJavaProject(), pm));
		}

		void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) <- after RefactoringStatus checkFinalConditions(IProgressMonitor pm) with {
			pm <- pm,
			status <- result
		}
	}

	/* gateway to a private field */
	protected class ChangeSignatureProcessor playedBy ChangeSignatureProcessor {
		@SuppressWarnings("decapsulation")
		protected IMethod getFMethod() -> get IMethod fMethod;
	}
	
	protected class IntroduceParameterObjectProcessor extends ChangeSignatureProcessor playedBy IntroduceParameterObjectProcessor {
		protected void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) when(!status.hasFatalError()) {
			status.merge(RefactoringUtil.checkForExistingRoles(RefactoringCoreMessages.IntroduceParameterObjectRefactoring_refactoring_name, getFMethod().getJavaProject(), pm));
		}
		void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) <- after RefactoringStatus checkFinalConditions(IProgressMonitor pm,
				CheckConditionsContext context) with {
			pm <- pm,
			status <- result
		}
	}

	protected class ChangeTypeRefactoring playedBy ChangeTypeRefactoring {
		@SuppressWarnings("decapsulation")
		ICompilationUnit getFCu() -> get ICompilationUnit fCu;

		private void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) when(!status.hasFatalError()) {
			status.merge(RefactoringUtil.checkForExistingRoles(RefactoringCoreMessages.ChangeTypeRefactoring_name, getFCu().getJavaProject(), pm));
		}

		void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) <- after RefactoringStatus checkFinalConditions(IProgressMonitor pm) with {
			pm <- pm,
			status <- result
		}
	}
}
