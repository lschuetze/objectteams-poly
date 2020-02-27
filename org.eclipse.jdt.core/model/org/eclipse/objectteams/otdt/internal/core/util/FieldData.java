/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
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
 * $Id: FieldData.java 23416 2010-02-03 19:59:31Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.util;

import org.eclipse.jdt.core.Signature;
import org.eclipse.objectteams.otdt.core.IFieldAccessSpec;

/**
 * @author brcan
 *
 */
public class FieldData implements IFieldAccessSpec
{
    private String selector;
    //attention: this field seems to use the constant pool encoding of types
    private String fieldType;
    
    private boolean isSetter;

    public FieldData(String selector, String fieldType, boolean isSetter)
    {
        this.selector   = selector;
        this.fieldType = fieldType;
        this.isSetter   = isSetter;
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IFieldAccessSpec#getSelector()
	 */
    @Override
	public String getSelector()
    {
        return this.selector;
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IFieldAccessSpec#getFieldType()
	 */
    @Override
	public String getFieldType()
    {
        return this.fieldType;
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IFieldAccessSpec#isSetter()
	 */
    @Override
	public boolean isSetter() 
    {
    	return this.isSetter;
    }
    
    @Override
	public String toString()
    {
    	if ("".equals(this.fieldType)) //$NON-NLS-1$
    		return "<missing type> "+this.selector; //$NON-NLS-1$
        return Signature.getSimpleName(Signature.toString(this.fieldType)) + " " + this.selector; //$NON-NLS-1$
    }
}

