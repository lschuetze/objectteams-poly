/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright (c) 2013 GK Software AG.
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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.core.manipulation.StubUtility;
import org.eclipse.jdt.internal.corext.refactoring.changes.CreateCompilationUnitChange;
import org.eclipse.jdt.internal.corext.refactoring.changes.CreatePackageChange;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.jdt.internal.corext.refactoring.util.RefactoringASTParser;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Strings;
import org.eclipse.jdt.core.manipulation.CodeGeneration;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.internal.refactoring.otrefactorings.OTRefactoringMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.text.edits.TextEditGroup;

/**
 * This refactoring extracts an inline role from its team and moves it to a new role file,
 * possibly creating the team package if a doesn't exist already.
 */
@SuppressWarnings("restriction")
public class MoveToRoleFileRefactoring extends Refactoring {
	

	private ICompilationUnit fTeamCUnit;

	private CompilationUnit fRootRole;

	private IType fTeamType;
	private IType fRoleType;

	public MoveToRoleFileRefactoring() {
	}

	public MoveToRoleFileRefactoring(IType roleType) {
		fRoleType = roleType;
	}

	@Override
	public String getName() {
		return OTRefactoringMessages.MoveToRoleFileRefactoring_moveToRoleFile_name;
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
		RefactoringStatus status= new RefactoringStatus();
		
		try {
			monitor.beginTask(OTRefactoringMessages.MoveToRoleFileRefactoring_preconditions_progress, 1);
			if (fRoleType == null){
				status.merge(RefactoringStatus.createFatalErrorStatus(OTRefactoringMessages.MoveToRoleFileRefactoring_noRole_error));
			} else if (!fRoleType.exists()){
				status.merge(RefactoringStatus.createFatalErrorStatus(NLS.bind(OTRefactoringMessages.MoveToRoleFileRefactoring_inexistentRole_error, 
						new Object[] { fRoleType.getElementName() })));
			} else if (fRoleType.isBinary()){
				status.merge(RefactoringStatus.createFatalErrorStatus(NLS.bind(OTRefactoringMessages.MoveToRoleFileRefactoring_binaryRole_error,
						new Object[] { fRoleType.getElementName() })));
			} else if (fRoleType.isReadOnly()){
				status.merge(RefactoringStatus.createFatalErrorStatus(NLS.bind(OTRefactoringMessages.MoveToRoleFileRefactoring_readOnlyRole_error,
						new Object[] { fRoleType.getElementName() })));
			} else if (!fRoleType.getCompilationUnit().isStructureKnown()){
					status.merge(RefactoringStatus.createFatalErrorStatus(NLS.bind(OTRefactoringMessages.MoveToRoleFileRefactoring_compileErrors_error,
						new Object[] { fRoleType.getCompilationUnit().getElementName() })));
			} else if (!OTModelManager.isRole(fRoleType)) {
				status.merge(RefactoringStatus.createFatalErrorStatus(NLS.bind(OTRefactoringMessages.MoveToRoleFileRefactoring_notInsideRole_error,
						new Object[] { fRoleType.getElementName() })));
			} else if (((IRoleType)OTModelManager.getOTElement(fRoleType)).isRoleFile()) {
				status.merge(RefactoringStatus.createFatalErrorStatus(NLS.bind(OTRefactoringMessages.MoveToRoleFileRefactoring_insideRoleFile_error,
						new Object[] { fRoleType.getElementName() })));
			} else { 
					status.merge(initialize(monitor));
			}
		} finally {
			monitor.done();
		}
		return status;
	}	
	
	private RefactoringStatus initialize(IProgressMonitor monitor) {
		RefactoringStatus status = new RefactoringStatus();
		fTeamType = (IType) fRoleType.getParent();
		fTeamCUnit = fTeamType.getCompilationUnit();
		
		if (fTeamType != null && !(fTeamType.getParent() instanceof ICompilationUnit)) {
			status.merge(RefactoringStatus.createFatalErrorStatus(NLS.bind(OTRefactoringMessages.MoveToRoleFileRefactoring_teamNotToplevel_error,
					new Object[] { fTeamType.getElementName() })));
		} 
		
		if (fRootRole == null) {
			fRootRole = RefactoringASTParser.parseWithASTProvider(fTeamCUnit, true, new SubProgressMonitor(monitor, 99));
		}		
		return status;
	}
	
	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		// I can't think of anything that could fail at this stage
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		pm.beginTask(OTRefactoringMessages.MoveToRoleFileRefactoring_creatingChange_progress, 5);
		CompositeChange change = new CompositeChange(OTRefactoringMessages.MoveToRoleFileRefactoring_change_name);
		TextEditGroup editGroup = new TextEditGroup(OTRefactoringMessages.MoveToRoleFileRefactoring_change_name);
		ICompilationUnit newCuWC= null;
		try {
			// packages
			IPackageFragment enclosingPackage = fRoleType.getPackageFragment();
			IPackageFragmentRoot root = (IPackageFragmentRoot) enclosingPackage.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
			String enclPackName = enclosingPackage.getElementName();
			IPackageFragment teamPackage = root.getPackageFragment(
					enclPackName.length()==0 ? fTeamType.getElementName() : enclPackName+'.'+fTeamType.getElementName());

			// fetch AST for team and role:
			CompilationUnitRewrite cuRewrite = new CompilationUnitRewrite(fTeamCUnit);
			CompilationUnit teamCU = cuRewrite.getRoot();
			TypeDeclaration teamNode = (TypeDeclaration) findDeclaration(teamCU, fTeamType);
			ASTNode roleNode = findDeclaration(teamCU, fRoleType);

			// new CU:
			if (!teamPackage.getResource().exists())
				change.add(new CreatePackageChange(teamPackage));
			newCuWC= teamPackage.getCompilationUnit(fRoleType.getElementName()+JavaModelUtil.DEFAULT_CU_SUFFIX).getWorkingCopy(new SubProgressMonitor(pm, 2));

			// (1) create role:
			// extract role source (as text):
			String oldSource = fTeamCUnit.getSource();
			ISourceRange sourceRange= fRoleType.getSourceRange();
			StringBuilder newRoleSource= new StringBuilder();
			IJavaProject javaProject = fRoleType.getJavaProject();
			if (StubUtility.doAddComments(javaProject))
				newRoleSource.append(CodeGeneration.getFileComment(newCuWC, StubUtility.getLineDelimiterUsed(javaProject)));
			newRoleSource.append("\nteam package "+teamPackage.getElementName()+";\n\n"); //$NON-NLS-1$ //$NON-NLS-2$
			newRoleSource.append(getAlignedSourceBlock(javaProject, 
					oldSource.substring(sourceRange.getOffset(), sourceRange.getOffset() + sourceRange.getLength())));

			// done change #1:
			change.add(new CreateCompilationUnitChange(newCuWC, newRoleSource.toString(), null));
			
			// (2) modify team:
			// remove role from team:
			ASTRewrite rewrite = cuRewrite.getASTRewrite();
			ListRewrite teamMembersRewrite = rewrite.getListRewrite(teamNode, teamNode.getBodyDeclarationsProperty());
			teamMembersRewrite.remove(roleNode, editGroup);
			
			// add javadoc tag '@role roleName'
			Javadoc teamDoc = teamNode.getJavadoc();
			AST ast = teamCU.getAST();
			TextElement roleName = ast.newTextElement();
			roleName.setText(fRoleType.getElementName());
			TagElement roleTag = ast.newTagElement();
			roleTag.setTagName("@role"); //$NON-NLS-1$
			roleTag.fragments().add(roleName);
			if (teamDoc == null) { // need to add a fresh Javadoc
				teamDoc = ast.newJavadoc();
				teamDoc.tags().add(roleTag);
				rewrite.set(teamNode, teamNode.getJavadocProperty(), teamDoc, editGroup);
			} else { // need to insert tag into existing Javadoc
				ListRewrite tags = rewrite.getListRewrite(teamDoc, Javadoc.TAGS_PROPERTY);
				tags.insertLast(roleTag, editGroup);
			}

			// done change #2:
			change.add(cuRewrite.createChange(true, new SubProgressMonitor(pm, 2)));
		} finally {
			if (newCuWC != null)
				newCuWC.discardWorkingCopy();
		}
		
		pm.done();
		return change;
	}
	
	private ASTNode findDeclaration(CompilationUnit unit, ISourceReference source) throws JavaModelException {
		ISourceRange range = source.getSourceRange();
		NodeFinder finder = new NodeFinder(unit, range.getOffset(), range.getLength());
		return finder.getCoveredNode();
	}

	// helper inspired by MoveInnerToTopRefactoring: 
	private String getAlignedSourceBlock(IJavaProject javaProject, String block) {
		Assert.isNotNull(block);
		final String[] lines= Strings.convertIntoLines(block);
		Strings.trimIndentation(lines, javaProject, false);
		return Strings.concatenate(lines, StubUtility.getLineDelimiterUsed(javaProject));
	}
}