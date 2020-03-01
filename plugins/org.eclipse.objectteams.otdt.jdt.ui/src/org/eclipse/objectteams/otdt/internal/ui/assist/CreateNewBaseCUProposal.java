/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2012 GK Software AG, Germany.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.assist;

import org.eclipse.jdt.core.dom.LiftingType;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.RoleTypeDeclaration;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jface.text.IDocument;

import base org.eclipse.jdt.internal.ui.text.correction.proposals.NewCUUsingWizardProposal;
import base org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * This team advises the quick fix for creating a new type to create the correct kind of import:
 * when creating a base class (after "playedBy" or before "as") create a base import.
 * @since 2.1
 */
@SuppressWarnings("restriction")
public team class CreateNewBaseCUProposal {

	/** Outer role to span a context while the proposal is being applied on a base class reference. */
	protected team class WizardProposal playedBy NewCUUsingWizardProposal {
		
		@SuppressWarnings("decapsulation")
		Name getNode() -> get Name fNode;

		void spanContext() <- replace void apply(IDocument document)
			when (isBaseclassReference());

		callin void spanContext() {
			within (this)
				base.spanContext();
		}
		boolean isBaseclassReference() {
			StructuralPropertyDescriptor locationInParent = getNode().getParent().getLocationInParent();
			return (locationInParent == RoleTypeDeclaration.BASECLASS_TYPE_PROPERTY
						|| locationInParent == LiftingType.BASE_TYPE_PROPERTY);
		}

		/** Inner role to trigger the effect of generating a base import. */
		@SuppressWarnings("decapsulation")
		protected class Import playedBy ImportRewrite {
			
			void addImport(String qualifiedTypeName) <- before String addImport(String qualifiedTypeName);
			
			void addImport(String qualifiedTypeName) {
				int lastDot = qualifiedTypeName.lastIndexOf('.');
				BaseImportRewriting.instance().markForBaseImport(this, qualifiedTypeName.substring(lastDot+1));
			}
		}
	}
}
