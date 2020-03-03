/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
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
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.otmodel.anonymousinnerclass.rolelevel.internal;

import junit.framework.Test;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * 
 * testcase:
 * a role class (defined insight the file of its team class) with a method
 * instantiation of an anonymous class inside the method
 * the anonymous class is an ordinary class
 */
public class Test2 extends LocalClassTest
{

    private final String ANONYMOUS_METHOD_NAME = "additionalMethod";    
    
    
    public static Test suite()
    {
        if (true)
        {
            return new Suite(Test2.class);
        }
        junit.framework.TestSuite suite = new Suite(Test2.class
            .getName());
        return suite;
    }
        
    public Test2(String name)
    {
        super(name);
    }

    protected String getTeamName()
    {
        return "Test2_SampleTeam";
    }
    
    protected String getRoleName()
    {
        return "SampleRole";  
    }


    
    public void testContainmentOfMethodInAnonymousType() throws JavaModelException
    {
        IType anonymousType = getAnonymousType();
        assertNotNull(anonymousType);
        
        IMethod method = anonymousType.getMethod(ANONYMOUS_METHOD_NAME, new String[0]);
        assertNotNull(method);
        assertTrue(method.exists());
    }
}
