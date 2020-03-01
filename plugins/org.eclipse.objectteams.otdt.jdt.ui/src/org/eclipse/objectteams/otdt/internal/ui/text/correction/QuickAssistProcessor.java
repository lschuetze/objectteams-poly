/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 Stephan Herrmann.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.text.correction;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractMethodMappingDeclaration;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;
import org.eclipse.jdt.ui.text.java.correction.ICommandAccess;

/**
 * OT/J specific quick assists.
 * @since 0.7.0
 */
public class QuickAssistProcessor implements IQuickAssistProcessor {

	enum Errors { NONE, EXPECTED, UNEXPECTED }
	
	public boolean hasAssists(IInvocationContext context) throws CoreException {
		ASTNode coveringNode= context.getCoveringNode();
		if (coveringNode != null)
			return hasToggleSignaturesOfMethodMappingProposal(coveringNode);
		return false;
	}

	private boolean hasToggleSignaturesOfMethodMappingProposal(ASTNode coveringNode) {
		while (coveringNode != null) {
			switch (coveringNode.getNodeType()) {
			case ASTNode.CALLIN_MAPPING_DECLARATION:
			case ASTNode.CALLOUT_MAPPING_DECLARATION:
				AbstractMethodMappingDeclaration methodMapping = (AbstractMethodMappingDeclaration)coveringNode;
				if (methodMapping.hasParameterMapping())
					return false;
				return true;
			case ASTNode.TYPE_DECLARATION:
			case ASTNode.ROLE_TYPE_DECLARATION:
			case ASTNode.COMPILATION_UNIT:
				return false;
			}
			coveringNode = coveringNode.getParent();
		}
		return false;
	}

	public IJavaCompletionProposal[] getAssists(IInvocationContext context, IProblemLocation[] locations)
			throws CoreException
	{
		ASTNode coveringNode= context.getCoveringNode();
		if (coveringNode != null) {
			ArrayList<ICommandAccess> resultingCollections= new ArrayList<ICommandAccess>();

			Errors matchedErrorsAtLocation= matchErrorsAtLocation(locations, 
					new int[]{IProblem.UnresolvedCallinMethodSpec, IProblem.UnresolvedCalloutMethodSpec}); // handled by quickfix

			// no duplicate proposals for problems handled by quickfix, but ignore unexpected errors:
			if (matchedErrorsAtLocation != Errors.EXPECTED)
				MappingProposalSubProcessor.getRemoveMethodMappingSignaturesProposal(context.getCompilationUnit(), coveringNode, 1, resultingCollections);

			matchedErrorsAtLocation= matchErrorsAtLocation(locations, 
					new int[]{IProblem.AmbiguousCallinMethodSpec, IProblem.AmbiguousCalloutMethodSpec}); // handled by quickfix

			if (matchedErrorsAtLocation == Errors.NONE)
				MappingProposalSubProcessor.getAddMethodMappingSignaturesProposal(context.getCompilationUnit(), coveringNode, 1, resultingCollections);
			return resultingCollections.toArray(new IJavaCompletionProposal[resultingCollections.size()]);
		}
		return null;
	}
	
	private Errors matchErrorsAtLocation(IProblemLocation[] locations, int[] expectedProblemIds) {
		boolean hasMatch = false;
		if (locations != null) {
			locations: for (int i= 0; i < locations.length; i++) {
				IProblemLocation location= locations[i];
				if (location.isError()) {
					int problemId = location.getProblemId();
					if (expectedProblemIds != null)
						for (int j = 0; j < expectedProblemIds.length; j++)
							if (expectedProblemIds[j] == problemId) {
								hasMatch = true;
								continue locations;
							}
					if (IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER.equals(location.getMarkerType())
							&& JavaCore.getOptionForConfigurableSeverity(problemId) != null) {
						// continue (only drop out for severe (non-optional) errors)
					} else {
						return Errors.UNEXPECTED;
					}
				}
			}
		}
		return hasMatch ? Errors.EXPECTED : Errors.NONE;
	}
}
