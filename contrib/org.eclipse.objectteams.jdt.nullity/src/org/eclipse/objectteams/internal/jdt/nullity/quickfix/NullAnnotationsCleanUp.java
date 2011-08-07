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

import static org.eclipse.objectteams.internal.jdt.nullity.Constants.IProblem.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
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
	private int handledProblemID;

	public NullAnnotationsCleanUp(Map<String, String> options, QuickFixes quickFixes, int handledProblemID) {
		super(options);
		this.master = quickFixes;
		this.handledProblemID = handledProblemID;
	}

	/**
	 * {@inheritDoc}
	 */
	public CleanUpRequirements getRequirements() {
		Map requiredOptions= getRequiredOptions();
		return new CleanUpRequirements(true, false, false, requiredOptions);
	}


	/**
	 * {@inheritDoc}
	 */
	protected ICleanUpFix createFix(CompilationUnit compilationUnit) throws CoreException {
		return this.createFix(compilationUnit, null);
	}

	/**
	 * {@inheritDoc}
	 */
	protected ICleanUpFix createFix(CompilationUnit compilationUnit, IProblemLocation[] problems) throws CoreException {
		if (compilationUnit == null)
			return null;
		IProblemLocation[] locations = null;
		ArrayList<IProblemLocation> filteredLocations = new ArrayList<IProblemLocation>();
		if (problems != null) {
			for (int i = 0; i < problems.length; i++) {
				if (problems[i].getProblemId() == this.handledProblemID)
					filteredLocations.add(problems[i]);
			}
			locations = filteredLocations.toArray(new IProblemLocation[filteredLocations.size()]);
		}
		return this.master.createCleanUp(compilationUnit, locations, this.handledProblemID);
	}

	private Map getRequiredOptions() {
		Map result= new Hashtable();
		// TODO(SH): might set depending on this.handledProblemID, not sure about the benefit
		result.put(NullCompilerOptions.OPTION_ReportNullContractViolation, JavaCore.WARNING);
		result.put(CompilerOptions.OPTION_ReportRedundantNullCheck, JavaCore.WARNING);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getStepDescriptions() {
		List result= new ArrayList();
		switch (this.handledProblemID) {
		case IProblem.NonNullLocalVariableComparisonYieldsFalse:
		case IProblem.RedundantNullCheckOnNonNullLocalVariable:
		case RequiredNonNullButProvidedNull:
		case RequiredNonNullButProvidedPotentialNull:
		case RequiredNonNullButProvidedUnknown:
			result.add(FixMessages.NullAnnotationsCleanUp_add_nullable_annotation);
			break;
		case IllegalDefinitionToNonNullParameter:
		case IllegalRedefinitionToNonNullParameter:
		case ParameterLackingNonNullAnnotation:
			result.add(FixMessages.NullAnnotationsCleanUp_add_nonnull_annotation);
			break;	
		}
		return (String[])result.toArray(new String[result.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPreview() {
		// not used when not provided as a true cleanup(?)
		return "No preview available"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canFix(ICompilationUnit compilationUnit, IProblemLocation problem) {
		int id= problem.getProblemId();
		
		if (id == this.handledProblemID) {
			// FIXME search specifically: return param (which??)
//			if (QuickFixes.hasExplicitNullnessAnnotation(compilationUnit, problem.getOffset()))
//				return false;
			return true;			
		}

		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	public int computeNumberOfFixes(CompilationUnit compilationUnit) {
		int result= 0;
		
		IProblem[] problems= compilationUnit.getProblems();
		for (int i= 0; i < problems.length; i++) {
			int id= problems[i].getID();
			if (id == this.handledProblemID) {
				// FIXME search specifically: return param (which??)
//				if (!QuickFixes.hasExplicitNullnessAnnotation(compilationUnit, problems[i].getSourceStart()))
					result++;
			}
		}
		return result;
	}
}
