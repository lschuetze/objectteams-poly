/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2006, 2007 Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: BaseImportRewriting.java 23438 2010-02-04 20:05:24Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.assist;


import java.util.HashSet;
import java.util.List;

import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.LiftingType;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.RoleTypeDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite.ImportRewriteContext;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.objectteams.otdt.internal.corext.RoleFileAdaptor;

import base org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import base org.eclipse.jdt.internal.codeassist.CompletionEngine;
import base org.eclipse.jdt.internal.codeassist.InternalCompletionProposal;
import base org.eclipse.jdt.internal.codeassist.complete.CompletionParser;
import base org.eclipse.jdt.internal.compiler.ast.TypeReference;

/**
 * This team advises the completion engine et al to produce base-imports if appropriate.
 * It implements a DataRelayChain with the following stages:
 * 
 * (1) CompletionParser
 * (2) CompletionOnBaseTypeReference
 * (3) CompletionEngine
 * (4) LazyJavaTypeCompletionProposal (in CompletionAdaptor, since type is in a different plug-in).
 * (5) ImportRewrite
 * 	 Field: needsBaseImport
 * 	 Set: passive: Field is externally written
 * 	 Eval: addEntry <- replace addEntry
 * 	 	-> regular API ("goal")
 * 
 * Additionally the OrganizeImportsAction is advised, too. Here the initial entry is {@link ImportRewrite#create(CompilationUnit,boolean)}
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class BaseImportRewriting 
{
	private static BaseImportRewriting instance;
	public static BaseImportRewriting instance() {
		return instance;
	}
	public BaseImportRewriting() {
		instance = this;
	}

	/**
	 * While parsing we mark a assist type reference right after <code>playedBy</code>.
	 * Marking is done by decorating with a {@link CompletionOnBaseTypeRef} role.
	 */
	protected class CompletionParser playedBy CompletionParser 
	{
		/** Intermediate data storage #1: */
		boolean nextIsBaseclass = false;
		
		/** Initial entry (input) of this DataRelayChain. */
		callin void consumeClassHeaderPlayedBy() {
			boolean flagSave = nextIsBaseclass;
			nextIsBaseclass = true;
			try {
				base.consumeClassHeaderPlayedBy();
			} finally {
				nextIsBaseclass = flagSave;
			}
		}
		consumeClassHeaderPlayedBy <- replace consumeClassHeaderPlayedBy;
		
		/** Entry (output) conditionally passing data to next role. */
		void checkRecordBaseclassReference(CompletionOnBaseTypeRef ref)
			<- after TypeReference createSingleAssistTypeReference(char[] assistName, long position) 
			with { ref <- result }
		void checkRecordBaseclassReference(CompletionOnBaseTypeRef ref) {
			if (!this.nextIsBaseclass)
				// not within required context, cancel the role:
				BaseImportRewriting.this.unregisterRole(ref, CompletionOnBaseTypeRef.class);
		}
	}

	/** 
	 * This role is registered by lifting in {@link CompletionParser#checkRecordBaseclassReference(CompletionOnBaseTypeRef)}.
	 * Its presence marks a type reference as completion on base class.
	 */
	protected class CompletionOnBaseTypeRef	playedBy TypeReference
		base when (BaseImportRewriting.this.hasRole(base, CompletionOnBaseTypeRef.class))
	{
		/** adjust pretty printing for testing and debugging. */
		@SuppressWarnings("basecall")
		callin StringBuffer printExpression(int indent, StringBuffer output){
			output.append("<CompleteOnBaseclass:");//$NON-NLS-1$
			return output.append(getToken()).append('>');
		}
		printExpression <- replace printExpression;

		char[] getToken() -> char[] getLastToken(); 
	}
	
	@SuppressWarnings("decapsulation"/*final baseclass*/)
	protected class CompletionEngine playedBy CompletionEngine
	{
		/** Intermediate data storage #2: */
		public boolean assistNodeIsBaseclass = false;
				
		/** Entry (input): transfer information from CompletionOnBaseTypeReference role to this role. */
		void markAsCompletingBaseclass(ASTNode astNode) {
			this.assistNodeIsBaseclass = true;
		}
		void markAsCompletingBaseclass(ASTNode astNode)
			<- before boolean complete(ASTNode astNode, ASTNode astNodeParent, ASTNode enclosingNode, CompilationUnitDeclaration compilationUnitDeclaration, Binding qualifiedBinding, Scope scope, boolean insideTypeAnnotation)
			when (BaseImportRewriting.this.hasRole(astNode, CompletionOnBaseTypeRef.class));
	}
	
	@SuppressWarnings("decapsulation")
	protected class ImportRewriteAdaptor playedBy ImportRewrite
	{
		/** triggered from OrganizeImportsOperation.run() we create instances of this role: */ 
		void markImportRewrite(ImportRewriteAdaptor rewrite, CompilationUnit astRoot)
			<- after ImportRewrite create(CompilationUnit astRoot, boolean restoreExistingImports)
			when (result != null)
			with { rewrite <- result, astRoot <- astRoot }
		
		/** Remember all known base classes within the given CU as needing a base import. */
		@SuppressWarnings("unchecked")
		static void markImportRewrite(ImportRewriteAdaptor rewrite, CompilationUnit astRoot) {
			try {
				List<AbstractTypeDeclaration> types = astRoot.types();
				if (types != null)
					for (AbstractTypeDeclaration type : types) {
						if (type.isRole())
							rewrite.cuIsRoFi = true;
						if (type.isTeam() || type.isRole())
							rememberBasesForBaseImport(rewrite, (TypeDeclaration)type);
					}
			} catch (Exception e) {
				JavaCore.getJavaCore().getLog().log(new Status(Status.ERROR, JavaCore.PLUGIN_ID, Status.OK, 
						"Error idendifying base classes for import.", e)); //$NON-NLS-1$
			}
		}
		
		/** Remember all base classes referenced by 'theTeam' as needing base import. */
		static void rememberBasesForBaseImport(ImportRewriteAdaptor rewrite, TypeDeclaration type) {
			try {
				// descend into AST searching for in-line role types:
				if (type instanceof RoleTypeDeclaration) {
					Type baseClassType = ((RoleTypeDeclaration)type).getBaseClassType();
					if (baseClassType != null) {
						SimpleName baseName = getSimpleTypeName(baseClassType);
						if (baseName != null)
							rewrite.baseNames.add(baseName.getIdentifier());
					}
				}
				for (Object decl : type.bodyDeclarations())
					if (decl instanceof RoleTypeDeclaration)
						rememberBasesForBaseImport(rewrite, (TypeDeclaration)decl); // recurse
					else if (type.isTeam() && decl instanceof MethodDeclaration)
						rememberBasesForBaseImport(rewrite, (MethodDeclaration)decl); // search for declared lifting
						
				// also fetch role files:
				org.eclipse.jdt.core.dom.ASTNode enclosing = type.getParent();
				if (enclosing instanceof CompilationUnit)
					rewrite.baseNames.addAll(RoleFileAdaptor.getRoFiBaseClassNames((CompilationUnit)enclosing));
			} catch (Exception javaModelException){
				// nop
			}
		}
		
		static void rememberBasesForBaseImport(ImportRewriteAdaptor rewrite, MethodDeclaration methodDecl) {
			for (Object param : methodDecl.parameters()) {
				if (param instanceof SingleVariableDeclaration) {
					Type type = ((SingleVariableDeclaration) param).getType();
					if (type instanceof LiftingType) {
						SimpleName baseName = getSimpleTypeName(((LiftingType) type).getBaseType());
						if (baseName != null)
							rewrite.baseNames.add(baseName.getIdentifier());
					}
				}					
			}			
		}
		
		static SimpleName getSimpleTypeName(Type type) {
			if (type instanceof ParameterizedType)
				type = ((ParameterizedType) type).getType();
			if (type instanceof ArrayType)
				type = ((ArrayType) type).getComponentType();
			if (type.isSimpleType()) {
				Name name = ((SimpleType) type).getName();
				if (name.isSimpleName())
					return (SimpleName)name;
				else
					return ((QualifiedName)name).getName();
			} else if (type.isQualifiedType()) {
				return ((QualifiedType) type).getName();
			}
			return null;
		}
		
		// === Instance level methods and method bindings:
		
		/** Intermediate data storage #3 (passive, externally written from #checkForBaseImport()). */
		public boolean needsBaseImport = false;
		/** Alternative intermediate storage: the stored names should be treated as base classes. */
		public HashSet<String> baseNames = new HashSet<String>();
		/** Is the current CU a role file? */
		boolean cuIsRoFi = false;
		
		/** Final entry (output): conditionally change import entry from NORMAL to BASE. */
		@SuppressWarnings("basecall")
		callin void adaptEntry1(String entry) {
			String adapted = doAdaptEntry(entry);
			if (adapted != null)
				base.adaptEntry1(adapted);
		}
		/** Final entry (output): conditionally change import entry from NORMAL to BASE. */
		@SuppressWarnings("basecall")
		callin boolean adaptEntry2(String entry) {
			String adapted = doAdaptEntry(entry);
			if (adapted != null)
				return base.adaptEntry2(adapted);
			return false;
		}
		adaptEntry1 <- replace addEntry
			base when (hasRole(base, ImportRewriteAdaptor.class));
		adaptEntry2 <- replace removeEntry
			base when (hasRole(base, ImportRewriteAdaptor.class));
		
		String doAdaptEntry (String entry) {
			if (entry.charAt(0) == getNormalChar()) {
				String[] names = entry.split("\\."); //$NON-NLS-1$
				if (needsBaseImport || baseNames.contains(names[names.length-1])) {
					if (cuIsRoFi)
						return null; // don't propose to add a base import to a role file
					else
						entry = getBaseChar()+entry.substring(1);
				}
			}
			return entry;
		}
		
		// this callin tells the ImportRewrite not to consider local declarations as a conflict,
		// if we already know that we need a base import
		// => fixes completing a base class with the same name as its role.
		callin String internalAddImport(String fullTypeName, ImportRewriteContext context)
		{
			if (needsBaseImport)
				context= new ImportRewriteContext() {
					@Override
					public int findInContext(String qualifier, String name,	int kind) {
						return getDefaultImportRewriteContext().findInContext(qualifier, name, kind);
						// explicitly don't look in declarations!
					}
				};
			return base.internalAddImport(fullTypeName, context);
		}
		String internalAddImport(String fullTypeName, ImportRewriteContext context) 
			<- replace String internalAddImport(String fullTypeName, ImportRewriteContext context);
		
		callin void markForBaseImport() {
			try {
				this.needsBaseImport = true;
				base.markForBaseImport();
			} finally {
				BaseImportRewriting.this.unregisterRole(this, ImportRewriteAdaptor.class);
			}
		}
		markForBaseImport <- replace addImportBase;
		
		ImportRewriteContext getDefaultImportRewriteContext() -> ImportRewriteContext getDefaultImportRewriteContext();
		
		// callout to constants:
		char getNormalChar() -> get char NORMAL_PREFIX;
		char getBaseChar()   -> get char BASE_PREFIX;
	}
	
	/** Passive role: provide access to one field. */
	protected class CompletionProposal playedBy InternalCompletionProposal {
		@SuppressWarnings("decapsulation")
		protected
		CompletionEngine getEngine() -> get CompletionEngine completionEngine;
	}

	/** API for use by LazyJavaTypeCompletionProposal: propagate data from a {@link CompletionEngine} to an {@link ImportRewriteAdaptor}. 
	 * @param cp      completion proposal being processed
	 * @param rewrite import rewrite that may have to add the "base" modifier.
	 */
	public void checkForBaseImport(InternalCompletionProposal as CompletionProposal   cp,
								   ImportRewrite              as ImportRewriteAdaptor rewrite) 
	{
		if (cp == null)
			return;
		CompletionEngine engine = cp.getEngine();
		if (engine != null && rewrite != null)
			rewrite.needsBaseImport = engine.assistNodeIsBaseclass;
	}
	
	/**
	 * API for org.eclipse.objectteams.otdt.internal.refactoring.adaptor.BaseImporting:
	 * Mark the given simple name as requiring a base import.
	 * @param imports the rewrite in use
	 * @param baseName the simple name of a base class.
	 */
	public void markForBaseImport(ImportRewrite as ImportRewriteAdaptor imports, String baseName) {
		if (imports.baseNames == null)
			imports.baseNames = new HashSet<String>();
		imports.baseNames.add(baseName);
	}

}
