/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008, 2012 Technical University Berlin, Germany, and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: MoveAdaptor.java 23473 2010-02-05 19:46:08Z stephan $
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.refactoring.adaptor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.RefactoringStatusEntry;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.internal.ui.assist.BaseImportRewriting;

import base org.eclipse.jdt.internal.core.ImportDeclaration;
import base org.eclipse.jdt.internal.corext.refactoring.reorg.MoveCuUpdateCreator;
import base org.eclipse.jdt.internal.corext.refactoring.structure.MoveStaticMembersProcessor;

/**
 * Adapt move refactorings:
 * <ul>
 * <li>Adjust rules for move static member with respect to OT/J</li>
 * <li>Handle base imports when moving a type to a different package.</li>
 * </ul>
 * 
 * @author stephan
 * @since 1.1.7
 */
@SuppressWarnings("restriction")
public team class MoveAdaptor 
{
	@SuppressWarnings("decapsulation")
	protected class MoveStaticMembersProcessor playedBy MoveStaticMembersProcessor {

		IType getFDestinationType() -> get IType fDestinationType;

		RefactoringStatus checkDestinationType() <- replace RefactoringStatus checkDestinationType();
		callin RefactoringStatus checkDestinationType() 
				throws JavaModelException 
		{
			RefactoringStatus result= base.checkDestinationType();
			if (result.isOK())
				return result;
			// check for error that is actually tolerable for roles:
			IOTType otType= OTModelManager.getOTElement(getFDestinationType());
			if (otType != null) {
				if (otType.isRole()) 
				{
					boolean filtered= false;
					RefactoringStatus newStatus= new RefactoringStatus();

					for (RefactoringStatusEntry entry : result.getEntries())
						if (isTolerableError(entry))							
							filtered= true;// filter out: roles may indeed hold static members (in contrast to nested types in Java)
						else
							newStatus.addEntry(entry);

					if (filtered)
						return newStatus;
				}
			}
			return result;
		}
		boolean isTolerableError(RefactoringStatusEntry entry) {
			if (entry.isError()) 
				return entry.getMessage().equals(RefactoringCoreMessages.MoveMembersRefactoring_static_declaration);
			return false;
		}
	}
	
	/** Handle base imports when moving a type to a different package. */
	protected team class MoveCU playedBy MoveCuUpdateCreator {

		/** Span the context when this nested team should be active. */
		@SuppressWarnings("decapsulation")
		addReferenceUpdates <- replace addReferenceUpdates;

		callin void addReferenceUpdates() throws JavaModelException, CoreException {
			within(this)
				base.addReferenceUpdates();			
		}

		/* captured reference. */
		ImportRewrite rewrite;

		void getImportRewrite(ImportRewrite rewrite) <- after ImportRewrite getImportRewrite(ICompilationUnit cu)
			when (this.isActive()) // explicit check since not located in a nested role
			with { rewrite <- result}

		private void getImportRewrite(ImportRewrite rewrite) {
			this.rewrite = rewrite;
		}
		
		/** Nested role for detecting base imports. */
		protected class Import playedBy ImportDeclaration {			
			
			@SuppressWarnings("decapsulation")
			String getName() -> get String name;

			void checkBaseFlag(int flags) <- after int getFlags()
				with { flags <- result }

			private void checkBaseFlag(int flags) {
				if (rewrite != null && (flags & ExtraCompilerModifiers.AccBase) != 0) {
					// we need the simple name:
					String name = getName();
					int lastDot = name.lastIndexOf('.');
					if (lastDot != -1)
						name = name.substring(lastDot+1);
					// register this name for base imports:
					BaseImportRewriting.instance().markForBaseImport(rewrite, name);
				}				
			}
		}
	}
}
