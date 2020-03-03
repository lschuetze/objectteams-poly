/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2005, 2009 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
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
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.wizards.typecreation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.internal.corext.refactoring.TypeContextChecker;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.internal.ui.OTDTUIPluginConstants;


/**
 * A RoleCreator is responsible for creating a new role class.
 * 
 * @author kaschja
 * @version $Id: RoleCreator.java 23435 2010-02-04 00:14:38Z stephan $
 */
public class RoleCreator extends TypeCreator
{
    
	/**
	 * @return "java.lang.Object"
	 */
	protected String createDefaultSupertypeName()
	{
		return "java.lang.Object"; //$NON-NLS-1$
	}


	protected void writeInheritanceRelations(ImportsManager imports, StringBuffer buf) throws CoreException
	{
        super.writeInheritanceRelations(imports, buf);
        // where to write base imports to:
		ImportRewrite importsRewrite = getTypeInfo().isInlineType() ? imports.fImportsRewrite : imports.fTeamImportsRewrite;
		writeBaseClass(buf, importsRewrite);
	}

	private void writeBaseClass(StringBuffer buf, ImportRewrite imports) throws CoreException
	{
	    TypeInfo typeInfo = getTypeInfo();
		if ( !(typeInfo instanceof RoleTypeInfo) )
			return;
	    
	    RoleTypeInfo roleTypeInfo = (RoleTypeInfo) typeInfo;
	    String baseName = roleTypeInfo.getBaseTypeName();

	    if (baseName != null && baseName.trim().length() > 0 )
	    {
	    	buf.append(" playedBy "); //$NON-NLS-1$
			ITypeBinding binding= null;
			IType currentType = roleTypeInfo.getCurrentType();
			if (currentType != null) { // try to resolve it similar to a superclass:
				binding= TypeContextChecker.resolveSuperClass(baseName, currentType, getBaseTypeStubTypeContext());
				if (binding == null) {// but could also be an interface:
					binding= TypeContextChecker.resolveSuperInterfaces(new String[]{baseName}, currentType, getSuperInterfacesStubTypeContext())[0];
				}
			}
			if (binding != null) {
				if (hasCommonEnclosingTeam(currentType, binding)) {
					// no importing
					buf.append(binding.getName());				
				} else {
					buf.append(imports.addImport(binding));
					imports.setImportBase(binding);
				}
			} else {
				buf.append(imports.addImportBase(baseName));
			}

	    }
	}


	private boolean hasCommonEnclosingTeam(IType type, ITypeBinding binding) {
		IJavaElement currentElement = type;
		while ((currentElement = currentElement.getParent()) instanceof IType) {
			IType currentType = (IType) currentElement;
			if (OTModelManager.isTeam(currentType)) {
				ITypeBinding currentBinding = binding;
				while ((currentBinding = currentBinding.getDeclaringClass()) != null) {
					if (currentBinding.getQualifiedName().equals(currentType.getFullyQualifiedName('.')))
						return true;
				}
			}
		}
		return false;
	}


	protected void validateTypeCreation()
		throws CoreException
	{		
		if (getTypeInfo().getEnclosingTypeName().trim().length() == 0)
		{
			throw new CoreException(new Status(IStatus.ERROR,
			                                   OTDTUIPluginConstants.UIPLUGIN_ID, 
											   IStatus.OK, 
                                               "The role class " + getTypeInfo().getTypeName() + " must have an enclosing team.", //$NON-NLS-1$ //$NON-NLS-2$
											   null));
		}
	}
}
