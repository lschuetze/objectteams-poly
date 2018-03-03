/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2007 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: CallinMappingDeclaration.java 16313 2007-09-22 19:32:10Z stephan $
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.refactoring.adaptor;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractMethodMappingDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CallinMappingDeclaration;
import org.eclipse.jdt.core.dom.CalloutMappingDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.RoleTypeDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite.ImportRewriteContext;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.objectteams.otdt.core.ICallinMapping;
import org.eclipse.objectteams.otdt.core.ICalloutMapping;
import org.eclipse.objectteams.otdt.core.ICalloutToFieldMapping;
import org.eclipse.objectteams.otdt.core.IOTJavaElement;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.internal.refactoring.corext.OTRefactoringCoreMessages;
import org.eclipse.objectteams.otdt.internal.ui.assist.BaseImportRewriting;

// org.eclipse.jdt.core.manipulation:
import base org.eclipse.jdt.core.manipulation.ImportReferencesCollector;
// org.eclipse.jdt.ui
import base org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgPolicyFactory.SubCuElementReorgPolicy;
import base org.eclipse.jdt.internal.corext.refactoring.reorg.ReadOnlyResourceFinder;
import base org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgUtils;
import base org.eclipse.jdt.internal.corext.refactoring.structure.ASTNodeSearchUtil;
import base org.eclipse.jdt.internal.corext.refactoring.structure.ImportRewriteUtil;

/**
 * The team adapts classes from corext.refactoring.{reorg,structure}.
 * 
 * @role OTASTNodeSearchUtil belongs to this team
 * in order to facilitate access to state role methods.
 */
@SuppressWarnings({ "restriction", "decapsulation" })
public team class ReorgAdaptor 
{
	protected class SubCuElementReorgPolicy playedBy SubCuElementReorgPolicy 
	{
		
		// ========= Callouts: =========
		abstract void copyMemberToDestination(IMember member, CompilationUnitRewrite targetRewriter, CompilationUnit sourceCuNode, CompilationUnit targetCuNode, BodyDeclaration newMember) 
				throws JavaModelException;
		copyMemberToDestination -> copyMemberToDestination;
		
		abstract static String getUnindentedSource(ISourceReference sourceReference) 
				throws JavaModelException;
		getUnindentedSource -> getUnindentedSource;

		IJavaElement getJavaElementDestination() -> IJavaElement getJavaElementDestination();

		// ======== Overrides =========
		
		// this callin dispatches on the element being moved:
		copyToDestination <- replace copyToDestination;
		@SuppressWarnings("basecall")
		callin void copyToDestination(IJavaElement element, CompilationUnitRewrite targetRewriter, CompilationUnit sourceCuNode, CompilationUnit targetCuNode) 
			throws CoreException 
		{
			AbstractMethodMappingDeclaration newMapping= null;
			IMember mapping= null;
			switch(element.getElementType()){
			// treat OT java model elements					
			case IOTJavaElement.CALLIN_MAPPING:
				mapping= (IMember)element;
				newMapping= (CallinMappingDeclaration)targetRewriter.getASTRewrite().createStringPlaceholder(getUnindentedSource(mapping), ASTNode.CALLIN_MAPPING_DECLARATION);					
				break;
			case IOTJavaElement.CALLOUT_MAPPING:
				mapping= (IMember)element;
				newMapping= (CalloutMappingDeclaration)targetRewriter.getASTRewrite().createStringPlaceholder(getUnindentedSource(mapping), ASTNode.CALLOUT_MAPPING_DECLARATION);
				break;
			case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
				mapping= (IMember)element;
				newMapping= (CalloutMappingDeclaration)targetRewriter.getASTRewrite().createStringPlaceholder(getUnindentedSource(mapping), ASTNode.CALLOUT_MAPPING_DECLARATION);
				break;
			case IOTJavaElement.TEAM:
			case IOTJavaElement.ROLE:
				element= ((IOTType)element).getCorrespondingJavaElement();
				// fall through with real java element
			default:
				base.copyToDestination(element, targetRewriter, sourceCuNode, targetCuNode);
				return;
			}
			// callout which is again callin-intercepted:
			copyMemberToDestination(mapping, targetRewriter, sourceCuNode, targetCuNode, newMapping);
		}
		
		// this callin dispatches on the DnD-target:
		getDestinationNode <- replace getDestinationNode;
		@SuppressWarnings("basecall")
		callin ASTNode getDestinationNode(IJavaElement destination, CompilationUnit target) 
				throws JavaModelException 
		{
			switch (destination.getElementType()) {
				case IOTJavaElement.CALLIN_MAPPING:
					return OTASTNodeSearchUtil.getCallinMappingDeclarationNode((ICallinMapping)destination, target);
				case IOTJavaElement.CALLOUT_MAPPING:
					return OTASTNodeSearchUtil.getCalloutMappingDeclarationNode((ICalloutMapping)destination, target);
				case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
					return OTASTNodeSearchUtil.getCalloutToFieldMappingDeclarationNode((ICalloutToFieldMapping)destination, target);
				case IOTJavaElement.TEAM:
				case IOTJavaElement.ROLE:
					destination = (((IOTType)destination).getCorrespondingJavaElement());
					// fall through with real java element
				default:
					return base.getDestinationNode(destination, target);
			}
		}
	}
	
	protected class ReadOnlyResourceFinder playedBy ReadOnlyResourceFinder 
	{
		@SuppressWarnings("basecall")
		static callin boolean hasReadOnlyResourcesAndSubResources(IJavaElement javaElement)
			throws CoreException 
		{
			switch(javaElement.getElementType()){
			// consider OT-specific elements
			case IOTJavaElement.TEAM:
			case IOTJavaElement.ROLE:
			case IOTJavaElement.CALLIN_MAPPING:
			case IOTJavaElement.CALLOUT_MAPPING:
			case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
				return false;
			default:
				return base.hasReadOnlyResourcesAndSubResources(javaElement);
			}
		}
		boolean hasReadOnlyResourcesAndSubResources(IJavaElement javaElement)
			<- replace boolean hasReadOnlyResourcesAndSubResources(IJavaElement javaElement);
	}
	
	final static int[] OT_MEMBERS = new int[]{IOTJavaElement.CALLIN_MAPPING, IOTJavaElement.CALLOUT_MAPPING, IOTJavaElement.CALLOUT_TO_FIELD_MAPPING};
	
	/** Patch a few hard coded switch statements in ReorgUtils. */
	protected class ReorgUtils playedBy ReorgUtils {

		boolean hasOnlyExpectedOrOTTypes(int[] types) <- replace boolean hasOnlyElementsOfType(IJavaElement[] javaElements, int[] types)
			with { types <- types }
		static callin boolean hasOnlyExpectedOrOTTypes(int[] types) {
			// pseudo switch: base uses set comparison.
			if (types[0] == IJavaElement.FIELD) {
				// when expecting fields also accept ot members:
				int l= types.length;
				int[] newTypes = new int[l+OT_MEMBERS.length];
				System.arraycopy(types, 0, newTypes, 0, l);
				System.arraycopy(OT_MEMBERS, 0, newTypes, l, OT_MEMBERS.length);
				return base.hasOnlyExpectedOrOTTypes(newTypes);
			}
			return base.hasOnlyExpectedOrOTTypes(types);
		}

		
		String createNamePattern(IJavaElement element) 
			<- replace String createNamePattern(IJavaElement element);
		@SuppressWarnings("basecall")
		static callin String createNamePattern(IJavaElement element)
			throws JavaModelException
		{
			switch (element.getElementType()) {
			case IOTJavaElement.TEAM:
				return OTRefactoringCoreMessages.getString("ReorgUtils.21"); //$NON-NLS-1$
			case IOTJavaElement.ROLE:
				return OTRefactoringCoreMessages.getString("ReorgUtils.23"); //$NON-NLS-1$
			case IOTJavaElement.CALLOUT_MAPPING:
				return OTRefactoringCoreMessages.getString("ReorgUtils.24"); //$NON-NLS-1$
			case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
				return OTRefactoringCoreMessages.getString("ReorgUtils.25"); //$NON-NLS-1$
			case IOTJavaElement.CALLIN_MAPPING:
				return OTRefactoringCoreMessages.getString("ReorgUtils.26"); //$NON-NLS-1$
			default:
				return base.createNamePattern(element); 
			}
		}		

		// createNameArguments now uses JavaElementLabels.getElementLabel()
		// nothing to adapt in ReorgUtils.
	}

	/** 
	 * When reorg updates a CU's imports, activate additional analysis
	 * for creating base imports if needed. 
	 */
	protected class ImportRewriteUtil playedBy ImportRewriteUtil {

		void addImports(CompilationUnitRewrite rewrite, ImportRewriteContext context)
		<- replace
		void addImports(CompilationUnitRewrite rewrite,
				ImportRewriteContext context, ASTNode node,
				Map<Name, String> typeImports, Map<Name, String> staticImports,
				Collection<IBinding> excludeBindings, boolean declarations);

		static callin void addImports(CompilationUnitRewrite rewrite, ImportRewriteContext context) {
			within (new BaseImporting(rewrite))
				base.addImports(rewrite, context);
		}
	}
	/**
	 * Mediate between {@link org.eclipse.jdt.internal.corext.codemanipulation.ImportReferencesCollector}
	 * and our {@link BaseImportRewriting} (from <code>org.eclipse.objectteams.otdt.jdt.ui</code>).
	 * This team is activated only temporarily when reorg is adding imports.
	 *
	 * @since 2.1
	 */
	protected team class BaseImporting {

		ImportRewrite rewrite;

		public BaseImporting(CompilationUnitRewrite rewrite) {
			this.rewrite = rewrite.getImportRewrite();
		}

		protected class BaseImportReferencesCollector playedBy ImportReferencesCollector {

			void typeRefFound(Name node) <- after void typeRefFound(Name node)
				base when (node != null);

			private void typeRefFound(Name node) {
				SimpleName name = (node instanceof QualifiedName)
						? ((QualifiedName)node).getName()
						: (SimpleName)node;
				String baseName = name.getIdentifier();
				// find this type's location in parent:
				ASTNode parent = node.getParent();
				StructuralPropertyDescriptor location = node.getLocationInParent();
				while (parent instanceof Type) {
					location = parent.getLocationInParent();
					parent = parent.getParent();
				}
				// if its the reference in a playedBy clause ...
				@SuppressWarnings("deprecation")
				StructuralPropertyDescriptor baseClassProperty = (node.getAST().apiLevel() == AST.JLS2) 
						? RoleTypeDeclaration.BASECLASS_PROPERTY
						: RoleTypeDeclaration.BASECLASS_TYPE_PROPERTY;
				if (location == baseClassProperty)
					// ... let BaseImportRewriting know that it may need a base import:
					BaseImportRewriting.instance().markForBaseImport(rewrite, baseName);
			}		
		}
	}
}
