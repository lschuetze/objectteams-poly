/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: OTSearchRequestor.java 23416 2010-02-03 19:59:31Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.search;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.OTModelManager;

/**
 * @author brcan
 * $Id: OTSearchRequestor.java 23416 2010-02-03 19:59:31Z stephan $
 */
public class OTSearchRequestor extends SearchRequestor
{
    private ArrayList<IOTType> otTypes = null;
    
    public OTSearchRequestor()
    {
        this.otTypes = new ArrayList<IOTType>();
    }

    @Override
	public void acceptSearchMatch(SearchMatch match) throws CoreException
    {
        IType javaType = null;
        Object element = match.getElement();
        
        if (element instanceof IType)
            javaType = (IType) element;
        
        else if (match.getResource() != null)
        {
            IJavaElement jel = JavaCore.create(match.getResource());
            if (jel.getElementType() == IJavaElement.TYPE)
                javaType = (IType) jel;
        }

        if (javaType != null)
        {
            IOTType otType = OTModelManager.getOTElement(javaType);
            if (otType == null)
            {
                try 
				{
					int modifiers = javaType.getFlags();
					if (Flags.isTeam(modifiers) || Flags.isRole(modifiers)) {
						javaType.getOpenable().open(null);
						otType = OTModelManager.getOTElement(javaType);
					}
				}
                catch (JavaModelException ex) {
                	// ignore -- element probably not present (e.g. because of __OT__RoleClass looking for its source)
				}
            }
            
            if (otType != null)
                this.otTypes.add(otType);
        }
    }
    
    public IOTType[] getOTTypes()
    {
        return this.otTypes.toArray(new IOTType[(this.otTypes.size())]);
    }
}
