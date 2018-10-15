/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2013 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.refactoring.adaptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BaseCallMessageSend;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.core.manipulation.StubUtility;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.jdt.internal.corext.refactoring.changes.TextChangeCompatibility;
import org.eclipse.jdt.internal.corext.refactoring.util.TextChangeManager;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.internal.refactoring.adaptor.rename.RenameMethodAmbuguityMsgCreator;
import org.eclipse.objectteams.otdt.internal.refactoring.adaptor.rename.RenameMethodOverloadingMsgCreator;
import org.eclipse.objectteams.otdt.internal.refactoring.corext.OTRefactoringCoreMessages;
import org.eclipse.objectteams.otdt.internal.refactoring.corext.rename.BaseCallFinder;
import org.eclipse.objectteams.otdt.internal.refactoring.util.RefactoringUtil;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

import base org.eclipse.jdt.internal.corext.refactoring.rename.MethodChecks;
import base org.eclipse.jdt.internal.corext.refactoring.rename.RenameAnalyzeUtil;
import base org.eclipse.jdt.internal.corext.refactoring.rename.RenamePackageProcessor;
import base org.eclipse.jdt.internal.corext.refactoring.rename.RenameVirtualMethodProcessor;
import base org.eclipse.jdt.internal.corext.refactoring.rename.TempOccurrenceAnalyzer;
import base org.eclipse.jdt.internal.corext.refactoring.rename.RenameAnalyzeUtil.ProblemNodeFinder;
import base org.eclipse.jdt.internal.corext.refactoring.rename.RenamePackageProcessor.ImportsManager;
import base org.eclipse.jdt.internal.corext.refactoring.rename.RenamePackageProcessor.PackageRenamer;
import base org.eclipse.jdt.internal.corext.refactoring.rename.RenamePackageProcessor.ImportsManager.ImportChange;
import base org.eclipse.jdt.internal.corext.util.JavaModelUtil;

/**
 * @author stephan
 *
 */
@SuppressWarnings("restriction")
public team class RenameAdaptor 
{
	@SuppressWarnings("basecall")
	protected class MethodChecks playedBy MethodChecks 
	{
		// dispatching some methods. Might consider collecting the dispatch and the implementation..		
		
		static callin IMethod getTopmostMethod(IMethod method, ITypeHierarchy typeHierarchy, IProgressMonitor monitor) 
				throws JavaModelException 
		{
			return RefactoringUtil.getTopmostMethod(method, typeHierarchy, monitor);
		}
		getTopmostMethod <- replace getTopmostMethod;
		
		static callin boolean isVirtual(IMethod method) 
				throws JavaModelException 
		{
			return RefactoringUtil.isVirtual(method);
		}
		boolean isVirtual(IMethod method) <- replace boolean isVirtual(IMethod method);
	}

	/**
	 * This class implements the adaptation of the rename virtual method
	 * refactoring for OT/J 
	 * (both from otdt..RenameVirtualMethodProcessor and OTRenameVirtualMethodProcessor).
	 * 
	 * <p>The adaptation is implemented as follows:</p>
	 * <p>
	 * Preconditions:
	 * <ol>
	 *   <li><code>checkFinalConditions(IProgressMonitor, CheckConditionsContext)
	 *       </code>:</li>
	 *   <ul>
	 *     <li>An additional check has been added for cases, where a role class
	 *         implements an interface. See the comment of this method for a more
	 *         detailed description of the added check.</li>
	 *     <li>For collecting all references to the target method, the <code>
	 *         RefactoringSearchEngine</code> is used <code>(RenameMethodProcessor.
	 *         getOccurrences(IProgressMonitor, RefactoringStatus))</code>. In
	 *         particular, the classes <code>MatchLocatorParser</code>, <code>
	 *         MatchLocator</code>, <code>PatternLocator</code>, <code>OrLocator
	 *         </code>, and <code>MethodLocator</code> have been modified, in order
	 *         to find references in callin and callout method bindings.</li>
	 *   </ul>
	 * </ol>
	 * </p>
	 * 
	 * @author brcan
	 */
	@SuppressWarnings("decapsulation") // multiple
	protected class RenameVirtualMethodProcessor
//			extends JavaRenameProcessor
			playedBy RenameVirtualMethodProcessor
	{	

		ITypeHierarchy getFCachedHierarchy() -> get ITypeHierarchy fCachedHierarchy;
		void setFCachedHierarchy(ITypeHierarchy fCachedHierarchy) -> set ITypeHierarchy fCachedHierarchy;

		// communication vars for enclosing doCheckFinalConditions():
		IProgressMonitor tmpMonitor = null;
		String tmpErrorMsg = null;
		/*
		 * TODO(SH): isSpecialCase() is only invoked for interface methods. Is this the correct hook?
		 */
		void isOTSpecialCase () throws CoreException 
		{
			if (RefactoringUtil.isOTSpecialCase(getMethod(),
					getNewElementName(),
					true,
					tmpMonitor))
			{
				tmpErrorMsg = OTRefactoringCoreMessages.getString("RenameVirtualMethodRefactoring.special_team_method"); //$NON-NLS-1$
			}
		}
		 isOTSpecialCase <- after isSpecialCase;
		/**
	     * Provides OT-specific final condition checking and performs the
	     * final condition checking provided by the standard Eclipse
	     * implementation.
	     * <p>
	     * What:
	     * <ol>
	     *   <li>OT-specific final condition checking:</li>
	     *   <ul>
		 *     <li>The following additional checks/changes have been added:
		 *         (1) check for overloading and ambiguous method specs.
		 *         (2) Update base calls when renaming callin methods.
		 *     </li>
	     *   </ul>
	     * </ol>
	     * </p>
	     */
		callin RefactoringStatus doCheckFinalConditions(IProgressMonitor pm, CheckConditionsContext checkContext) 
				throws CoreException 
		{
			tmpMonitor = pm;
			tmpErrorMsg = null;
			RefactoringStatus result = null;
			try {
				result = base.doCheckFinalConditions(pm, checkContext);
				result.merge(checkOverloadingAndAmbiguity(pm));
			} finally {
				if (tmpErrorMsg != null && result != null)
					result.addError(tmpErrorMsg);
				tmpErrorMsg = null;
			}
			return result;
		}
		doCheckFinalConditions <- replace doCheckFinalConditions;
	
		//	also check for overloading and ambiguity in OT-subclassed elements
		private RefactoringStatus checkOverloadingAndAmbiguity(IProgressMonitor pm)
				throws JavaModelException 
		{
	    	IMethod newMethod = getOriginalMethod();
	    	IType focusType = getOriginalMethod().getDeclaringType();
	    	if (focusType.isAnnotation() || focusType.isEnum())
	    		return new RefactoringStatus();
	    	ICompilationUnit cu = focusType.getCompilationUnit();
	    	String newMethodName = getNewElementName();
			
			try
			{
				return RefactoringUtil.checkOverloadingAndAmbiguity(cu,
						focusType,
						newMethodName,
						getOriginalMethod().getElementName(),
						newMethod.getParameterTypes(),
						new RenameMethodAmbuguityMsgCreator(),
						new RenameMethodOverloadingMsgCreator(), pm);
			} 
	    	catch (JavaModelException e)
			{
	    		//TODO(jsv): Use meaningful message and store it in the message file
	    		RefactoringStatus result = new RefactoringStatus();
	    		result.addError(OTRefactoringCoreMessages.getString("RenameAdaptor.error_check_overload_ambiguity")); //$NON-NLS-1$
	    		return result;
			}
		}
		
		void addOccurrences(TextChangeManager manager, IProgressMonitor pm, RefactoringStatus status) 
	    		throws CoreException 
	    {
	    	createChangeForPotentialBaseCalls(manager);
	    }
	    addOccurrences <- after addOccurrences;

	    /**
	     * Creates changes for base call in a callin method.
	     * Appearance of warning or error is not possible, this method does not
	     * return a refactoring status.
	     */
	    private void createChangeForPotentialBaseCalls(TextChangeManager changeManager)
	    {
	        Set<IMethod> methodsToRename = getMethodsToRename();
	        
	        for (Iterator<IMethod> methods = methodsToRename.iterator(); methods.hasNext();)
	        {
	            IMethod currentMethod =  methods.next();
	            MethodDeclaration methodDeclaration = null;
	    		try
	    		{
	    		    if (!Flags.isCallin(currentMethod.getFlags()))
	    		    {
	    		        continue;
	    		    }
	    			methodDeclaration = RefactoringUtil.getMethodDeclaration(currentMethod);
	    		} 
	        	catch (JavaModelException ex)
	        	{
	        	    JavaPlugin.logErrorMessage("Problems while searching DOM AST representation of a method"); //$NON-NLS-1$
	        	    continue;
	    		}
	        	
	        	BaseCallFinder baseCallFinder = new BaseCallFinder();
	        	methodDeclaration.accept(baseCallFinder);
	        	BaseCallMessageSend[] baseCalls = baseCallFinder.getResult();
	        	
	        	for (int idx = 0;idx < baseCalls.length; idx++)
	        	{
	        	    ICompilationUnit cu = currentMethod.getCompilationUnit();
	        	    TextChange textChange = changeManager.get(cu);
	        	    
	        	    String editName = OTRefactoringCoreMessages.getString(
	        	            "OTRenameVirtualMethodProcessor.update_base_call_occurrence"); //$NON-NLS-1$
	        	    TextChangeCompatibility.addTextEdit(textChange, editName,
	        	            new ReplaceEdit(baseCalls[idx].getName().getStartPosition(),
	        	                    baseCalls[idx].getName().getLength(),
	        	                    getNewElementName()));
	        	}
	        }
	    }
	  
		IMethod getOriginalMethod()  	  -> IMethod getOriginalMethod();
		String  getNewElementName()       -> String  getNewElementName();
		Set<IMethod> getMethodsToRename() -> Set<IMethod> getMethodsToRename();
		IMethod getMethod() 		      -> IMethod getMethod();
	}
	
	@SuppressWarnings("decapsulation") // base class is final
	protected class JMUtil playedBy JavaModelUtil {

		isVisibleInHierarchy <- replace isVisibleInHierarchy;

		@SuppressWarnings("basecall")
		static callin boolean isVisibleInHierarchy(IMember member, IPackageFragment pack) throws JavaModelException {
			if (Flags.isPrivate(member.getFlags())) {
				IType declaringType = member.getDeclaringType();
				if (OTModelManager.isRole(declaringType) && pack.equals(declaringType.getPackageFragment()))
					return true;
			}
			return base.isVisibleInHierarchy(member, pack);
		}
	}
	
	/** Detect when trying to rename a team package. */
	protected class RenamePackage playedBy RenamePackageProcessor {

		@SuppressWarnings("decapsulation")
		IPackageFragment getFPackage() -> get IPackageFragment fPackage;

		checkInitialConditions <- replace checkInitialConditions;

		callin RefactoringStatus checkInitialConditions() throws CoreException {
			RefactoringStatus status = base.checkInitialConditions();
			String qualifiedName = getFPackage().getElementName();
			int pos = qualifiedName.lastIndexOf('.');
			if (pos == -1) 
				return status;
			// find "parent" package:
			String parentName = qualifiedName.substring(0, pos);
			IPackageFragment parentPackage = ((IPackageFragmentRoot) getFPackage().getParent()).getPackageFragment(parentName);
			if (!parentPackage.exists()) {
				status.addWarning(OTRefactoringCoreMessages.getString("RenameAdaptor.error_parent_package_not_exist")); //$NON-NLS-1$
				return status;
			}
			// search "parent" package for a class corresponding to this package
			String simpleName = qualifiedName.substring(pos+1);
			for (IJavaElement sibling : parentPackage.getChildren())
				if (sibling.getElementName().equals(simpleName+".java")) //$NON-NLS-1$
					switch (sibling.getElementType()) {
					case IJavaElement.COMPILATION_UNIT:
						 for (IType type : ((ICompilationUnit)sibling).getTypes())
							 if (   type.getElementName().equals(simpleName)
								 && OTModelManager.isTeam(type)) 
							 {
								 // yep, have a team class of the same FQN, so it must be a team package.
								 status.addFatalError(OTRefactoringCoreMessages.getString("RenameAdaptor.error_cannot_rename_team_package")); //$NON-NLS-1$
								 return status;
							 }
						 break;
					case IJavaElement.CLASS_FILE:
						status.addError(OTRefactoringCoreMessages.getString("RenameAdaptor.error_binary_class_potential_team_package")); //$NON-NLS-1$
						return status;
					}
			return status;
		}
	}

	/** Add support for base imports: check if an import being updated is a base import and record those separately. */
	protected class PackageRenamer playedBy PackageRenamer {

		@SuppressWarnings("decapsulation")
		ImportsManager getFImportsManager() -> get ImportsManager fImportsManager;

		@SuppressWarnings("decapsulation")
		updateImport <- replace updateImport;

		@SuppressWarnings("basecall")
		callin void updateImport(ICompilationUnit cu, IImportDeclaration importDeclaration, String updatedImport) throws JavaModelException {
			if ((importDeclaration.getFlags() & ExtraCompilerModifiers.AccBase) != 0) {
				final ImportsManager importsManager = getFImportsManager();
				ImportChange<@importsManager> importChange= importsManager.getImportChange(cu);
				importChange.removeBaseImport(importDeclaration.getElementName());
				importChange.addBaseImport(updatedImport);
				
			} else {
				base.updateImport(cu, importDeclaration, updatedImport);
			}
		}
	}

	/** Add support for base imports: when performing the changes invoke choose the appropriate add- method in ImportRewrite. */
	protected team class ImportsManager playedBy ImportsManager {

		/** Extend existing record class by two more lists. */
		@SuppressWarnings("decapsulation")
		public class ImportChange playedBy ImportChange {
			protected List<String> 	getStaticToRemove() -> get ArrayList<String> 	fStaticToRemove;
			protected List<String> 	getToRemove() 		-> get ArrayList<String> 	fToRemove;
			protected List<String[]> 	getStaticToAdd() 	-> get ArrayList<String[]> 	fStaticToAdd;
			protected List<String> 	getToAdd() 			-> get ArrayList<String> 	fToAdd;

			protected ArrayList<String> fBaseToRemove= new ArrayList<String>();
			protected ArrayList<String> fBaseToAdd= new ArrayList<String>();

			public void removeBaseImport(String elementName) {
				fBaseToRemove.add(elementName);
			}
			public void addBaseImport(String updatedImport) {
				fBaseToAdd.add(updatedImport);
			}
		}

		@SuppressWarnings("decapsulation")
		Iterator<ICompilationUnit> getCompilationUnits() -> get HashMap<ICompilationUnit, ImportChange> fImportChanges
				with {	result <- fImportChanges.keySet().iterator() }

		protected ImportChange getImportChange(ICompilationUnit cu) ->  ImportChange getImportChange(ICompilationUnit cu);

		rewriteImports <- replace rewriteImports;

		/* Copy from its base version. */
		@SuppressWarnings("basecall")
		callin void rewriteImports(TextChangeManager changeManager, IProgressMonitor pm) throws CoreException {
//{ObjectTeams: separate iteration (over ICU) from fetching ImportChanges (needs lifting):
			/* orig:
				for (Iterator<Entry<ICompilationUnit, ImportChange>> iter= fImportChanges.entrySet().iterator(); iter.hasNext();) {
					Entry<ICompilationUnit, ImportChange> entry= iter.next();
					ICompilationUnit cu= entry.getKey();
					ImportChange importChange= entry.getValue();
  :giro */
			for (Iterator<ICompilationUnit> iter= getCompilationUnits(); iter.hasNext();) {
				ICompilationUnit cu= iter.next();
				ImportChange importChange= getImportChange(cu);
// SH}
				ImportRewrite importRewrite= StubUtility.createImportRewrite(cu, true);
				importRewrite.setFilterImplicitImports(false);
				for (Iterator<String> iterator= importChange.getStaticToRemove().iterator(); iterator.hasNext();) {
					importRewrite.removeStaticImport(iterator.next());
				}
				for (Iterator<String> iterator= importChange.getToRemove().iterator(); iterator.hasNext();) {
					importRewrite.removeImport(iterator.next());
				}
//{ObectTeams: one more kind of imports to remove
				for (Iterator<String> iterator= importChange.fBaseToRemove.iterator(); iterator.hasNext();) {
					importRewrite.removeImportBase(iterator.next());
				}
// SH}
				for (Iterator<String[]> iterator= importChange.getStaticToAdd().iterator(); iterator.hasNext();) {
					String[] toAdd= iterator.next();
					importRewrite.addStaticImport(toAdd[0], toAdd[1], true);
				}
				for (Iterator<String> iterator= importChange.getToAdd().iterator(); iterator.hasNext();) {
					importRewrite.addImport(iterator.next());
				}
//{ObectTeams: one more kind of imports to add
				for (Iterator<String> iterator= importChange.fBaseToAdd.iterator(); iterator.hasNext();) {
					importRewrite.addImportBase(iterator.next());
				}
// SH}
				if (importRewrite.hasRecordedChanges()) {
					TextEdit importEdit= importRewrite.rewriteImports(pm);
					String name= RefactoringCoreMessages.RenamePackageRefactoring_update_imports;
					try {
						TextChangeCompatibility.addTextEdit(changeManager.get(cu), name, importEdit);
					} catch (MalformedTreeException e) {
						JavaPlugin.logErrorMessage("MalformedTreeException while processing cu " + cu); //$NON-NLS-1$
						throw e;
					}
				}
			}
		}
	}


	/**
	 * Find more occurrences, currently:
	 * - references to an argument with declared lifting via the fakedRoleVariable.
	 */
	@SuppressWarnings("decapsulation")
	protected class TempOccurrenceAnalyzer playedBy TempOccurrenceAnalyzer {

		boolean addReferenceNode(SimpleName name)                      -> get Set<SimpleName> fReferenceNodes
        		with {  result 										<- 		base.fReferenceNodes.add(name) }
		VariableDeclaration getFTempDeclaration() 						-> get VariableDeclaration fTempDeclaration;
		void setFTempDeclaration(VariableDeclaration fTempDeclaration)	-> set VariableDeclaration fTempDeclaration;
		void setFTempBinding(IBinding fTempBinding) 					-> set IBinding fTempBinding;

		void perform() -> void perform();
		
		SimpleName originalName;

		void performAgain() <- after void perform();
	
		private void performAgain() {
			VariableDeclaration fTempDeclaration = getFTempDeclaration();
			if (fTempDeclaration instanceof SingleVariableDeclaration) {
				VariableDeclaration roleVar = ((SingleVariableDeclaration) fTempDeclaration).getFakedRoleVariable();
				if (roleVar != null) {
					// remember original declaration to be added during getReferenceAndDeclarationNodes():
					this.originalName = fTempDeclaration.getName();
					// re-initialize and search again using the faked roleVar:
					setFTempDeclaration(roleVar);
					setFTempBinding(roleVar.resolveBinding());
					perform();
				}
			}			
		}

		void getReferenceAndDeclarationNodes() <- before SimpleName[] getReferenceAndDeclarationNodes()
			when(this.originalName != null);

		private void getReferenceAndDeclarationNodes() {
			addReferenceNode(originalName);
		}		
	}
	
	/** Resolve more issues with declared lifting. */
	@SuppressWarnings("decapsulation")
	protected team class OTNodeAdjustments playedBy ProblemNodeFinder {

		@SuppressWarnings("decapsulation")
		public OTNodeAdjustments() {
			base();
		}

		getProblemNodes <- replace getProblemNodes;

		/** 
		 * When analyzing name clashes we compare variable bindings against a given binding key.
		 * Ensure we never use the internal version (_OT$arg) of a lifting argument.
		 */
		static callin SimpleName[] getProblemNodes(ASTNode methodNode, VariableDeclaration variableNode) {
			VariableDeclaration fakedRoleVariable = getFakeRoleVar(variableNode);
			if (fakedRoleVariable != null) 			// this one has the correct name, so use this
				within (new OTNodeAdjustments())	// and during the base call also do the second level adjustment via role Utility
					return base.getProblemNodes(methodNode, fakedRoleVariable);
			return base.getProblemNodes(methodNode, variableNode);
		}

		static VariableDeclaration getFakeRoleVar(VariableDeclaration variable) {
			if (variable instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration fakedRoleVariable = ((SingleVariableDeclaration)variable).getFakedRoleVariable();
				if (fakedRoleVariable != null)
					return fakedRoleVariable;
			}
			return null;			
		}

		protected class Utility playedBy RenameAnalyzeUtil {
			
			// assume this is triggered only from NameNodeVisitor.visit(SimpleName) (on behalf of getProblemNodes(), see above)
			getVariableDeclaration <- replace getVariableDeclaration;

			static callin VariableDeclaration getVariableDeclaration() {
				VariableDeclaration variable = base.getVariableDeclaration();
				VariableDeclaration roleVar = getFakeRoleVar(variable);
				if (roleVar != null)
					return roleVar;
				return variable;
			}		
		}
	}
}
