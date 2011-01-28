/*******************************************************************************
 * Copyright (c) 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.objectteams.internal.jdt.nullity.quickfix;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.ui.fix.AbstractMultiFix;
import org.eclipse.jdt.ui.cleanup.CleanUpRequirements;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.objectteams.internal.jdt.nullity.NullCompilerOptions;

/**
 * Cleanup for adding required null annotations.
 * 
 * Crafted after the lead of Java50CleanUp
 * 
 * @author stephan
 */
@SuppressWarnings({ "rawtypes", "restriction", "unchecked" })
public class NullAnnotationsCleanUp extends AbstractMultiFix {

	private QuickFixes master;

	public NullAnnotationsCleanUp(Map<String, String> options, QuickFixes quickFixes) {
		super(options);
		this.master = quickFixes;
	}

	/**
	 * {@inheritDoc}
	 */
	public CleanUpRequirements getRequirements() {
		boolean requireAST= requireAST();
		Map requiredOptions= requireAST ? getRequiredOptions() : null;
		return new CleanUpRequirements(requireAST, false, false, requiredOptions);
	}

	private boolean requireAST() {
		boolean addAnotations= isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS);

		return addAnotations && isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS_NULLABLE);
	}

	/**
	 * {@inheritDoc}
	 */
	protected ICleanUpFix createFix(CompilationUnit compilationUnit) throws CoreException {
		if (compilationUnit == null)
			return null;

		boolean addAnotations= isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS);
		boolean addNullable= isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS_NULLABLE);
		return this.master.createCleanUp(compilationUnit, addAnotations && addNullable, null);
	}

	/**
	 * {@inheritDoc}
	 */
	protected ICleanUpFix createFix(CompilationUnit compilationUnit, IProblemLocation[] problems) throws CoreException {
		if (compilationUnit == null)
			return null;

		boolean addAnnotations= isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS);
		boolean addNullable = isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS_NULLABLE);
		return this.master.createCleanUp(compilationUnit, addAnnotations && addNullable,  problems);
	}

	private Map getRequiredOptions() {
		Map result= new Hashtable();
		if (isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS) && isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS_NULLABLE)) {
			result.put(NullCompilerOptions.OPTION_ReportNullContractViolation, JavaCore.WARNING);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getStepDescriptions() {
		List result= new ArrayList();
		if (isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS) && isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS_NULLABLE)) {
			result.add(FixMessages.NullAnnotationsCleanUp_add_nullable_annotation);
		}
		return (String[])result.toArray(new String[result.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPreview() {
		StringBuffer buf= new StringBuffer();

		buf.append("class E {\n"); //$NON-NLS-1$
		if (isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS) && isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS_OVERRIDE)) {
			buf.append("    @Nullable\n"); //$NON-NLS-1$
		}
		buf.append("    public Object foo() { return null; }\n"); //$NON-NLS-1$
		buf.append("}\n"); //$NON-NLS-1$

		return buf.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canFix(ICompilationUnit compilationUnit, IProblemLocation problem) {
		int id= problem.getProblemId();
		
		if (this.master.isMissingNullableAnnotationProblem(id)) {
			if (isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS) && isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS_NULLABLE)) {
				if (this.master.hasExplicitNullnessAnnotation(compilationUnit, problem.getOffset()))
					return false;
				return true;
			}			
		}

		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	public int computeNumberOfFixes(CompilationUnit compilationUnit) {
		// note that this method also counts problems that cannot be fixed by this fix
		// because a method already has an explicit null annotation. We cannot check
		// here (which would be a bit expensive anyway) because we have no proper
		// problemlocation.
		int result= 0;
		
		boolean addAnnotations= isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS);
		boolean addMissingNullable= addAnnotations && isEnabled(CleanUpConstants.ADD_MISSING_ANNOTATIONS_NULLABLE);
		
		IProblem[] problems= compilationUnit.getProblems();
		for (int i= 0; i < problems.length; i++) {
			int id= problems[i].getID();
			if (addMissingNullable && this.master.isMissingNullableAnnotationProblem(id))
				if (!this.master.hasExplicitNullnessAnnotation((ICompilationUnit) compilationUnit.getJavaElement(), problems[i].getSourceStart()))
					result++;
		}
		return result;
	}
}
