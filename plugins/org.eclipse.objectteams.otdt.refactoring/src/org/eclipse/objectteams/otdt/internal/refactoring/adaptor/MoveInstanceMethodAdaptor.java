package org.eclipse.objectteams.otdt.internal.refactoring.adaptor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.objectteams.otdt.internal.refactoring.RefactoringMessages;
import org.eclipse.objectteams.otdt.internal.refactoring.util.IAmbuguityMessageCreator;
import org.eclipse.objectteams.otdt.internal.refactoring.util.IOverloadingMessageCreator;
import org.eclipse.objectteams.otdt.internal.refactoring.util.RefactoringUtil;

import base org.eclipse.jdt.internal.corext.refactoring.structure.MoveInstanceMethodProcessor;

/**
 * @author Johannes Gebauer
 *
 */
@SuppressWarnings("restriction")
public team class MoveInstanceMethodAdaptor {

	
	public MoveInstanceMethodAdaptor() {
		System.out.println("MoveInstanceMethodAdaptor.<init>");
	}

	@SuppressWarnings("decapsulation")
	protected class MoveInstanceMethodProcessor playedBy MoveInstanceMethodProcessor {
		
		public MoveInstanceMethodProcessor(MoveInstanceMethodProcessor moveInstanceMethodProcessor) {
			System.out.println("MoveInstanceMethodProcessor.<init>");
		}
		
		// callouts
		IMethod getFMethod() -> get IMethod fMethod;
		IMethod getMethod() -> IMethod getMethod();
		String getMethodName() -> String getMethodName();
		IType getTargetType() -> IType getTargetType();

		void checkFinalConditions(IProgressMonitor pm, RefactoringStatus result) throws CoreException {
			System.out.println(">>> MoveInstanceMethodProcessor.checkFinalConditions()");
			if(!result.hasFatalError())
				result.merge(RefactoringUtil.checkForExistingRoles(RefactoringMessages.MoveInstanceMethodAdaptor_moveInstanceMethod_name, getFMethod().getJavaProject(), pm));
			pm.beginTask(RefactoringMessages.MoveInstanceMethodAdaptor_checkOverloading_progress, 1);
			pm.subTask(""); //$NON-NLS-1$
			result.merge(checkOverloadingAndAmbiguity(pm));
			pm.worked(1);
			pm.done();
			System.out.println("<<< MoveInstanceMethodProcessor.checkFinalConditions()");
		}

		void checkFinalConditions(IProgressMonitor pm, RefactoringStatus status) <- after RefactoringStatus checkFinalConditions(IProgressMonitor pm,
				CheckConditionsContext context) with {
			pm <- pm,
			status <- result
		}

		private RefactoringStatus checkOverloadingAndAmbiguity(IProgressMonitor pm) throws JavaModelException {
			
			String[] paramTypes = getMethod().getParameterTypes();

			return RefactoringUtil.checkOverloadingAndAmbiguity(getTargetType(), null /* targetTypeHierarchy */, getMethodName(), paramTypes,
					new IAmbuguityMessageCreator() {

						public String createAmbiguousMethodSpecifierMsg() {
							return RefactoringMessages.MoveInstanceMethodAdaptor_ambiguousMethodSpec_error;
						}

					}, new IOverloadingMessageCreator() {

						public String createOverloadingMessage() {
							return RefactoringMessages.MoveInstanceMethodAdaptor_overloading_error;
						}

					}, pm);
		}
	}
}
