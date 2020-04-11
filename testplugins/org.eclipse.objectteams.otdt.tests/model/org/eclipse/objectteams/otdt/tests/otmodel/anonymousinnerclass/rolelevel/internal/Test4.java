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

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.objectteams.otdt.core.IMethodMapping;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.core.OTModelManager;

/**
 *
 * testcase:
 *  a role class (defined insight the file of its team class) with a method
 * instantiation of an anonymous class inside the method
 * the anonymous class is a role class with a method mapping
 */
public class Test4 extends LocalClassTest
{

    private final String ANONYMOUS_MAPPING_NAME = "roleMethod() <- baseMethod()";


    public static Test suite()
    {
        if (true)
        {
            return new Suite(Test4.class);
        }
        junit.framework.TestSuite suite = new Suite(Test4.class
            .getName());
        return suite;
    }

    public Test4(String name)
    {
        super(name);
    }

    protected String getTeamName()
    {
        return "Test4_SampleTeam";
    }

    protected String getRoleName()
    {
        return "Role2";
    }

    public void testExistenceOfAnonymousTypeInOTModel() throws JavaModelException
    {
        IType anonymousTypeJavaElem = getAnonymousType();
        assertNotNull(anonymousTypeJavaElem);

        IOTType anonymousTypeOTElem = OTModelManager.getOTElement(anonymousTypeJavaElem);
        assertNotNull(anonymousTypeOTElem);
    }

    public void testRolePropertyOfAnonymousType() throws JavaModelException
    {
        IType anonymousTypeJavaElem = getAnonymousType();
        assertNotNull(anonymousTypeJavaElem);

        IOTType anonymousTypeOTElem = OTModelManager.getOTElement(anonymousTypeJavaElem);
        assertNotNull(anonymousTypeOTElem);

        assertTrue(anonymousTypeOTElem.isRole());
        assertTrue(anonymousTypeOTElem instanceof IRoleType);
    }

    public void testContainmentOfMethodMappingInAnonymousType() throws JavaModelException
    {
        IType anonymousTypeJavaElem = getAnonymousType();
        assertNotNull(anonymousTypeJavaElem);

        IOTType anonymousTypeOTElem = OTModelManager.getOTElement(anonymousTypeJavaElem);
        assertNotNull(anonymousTypeOTElem);

        assertTrue(anonymousTypeOTElem instanceof IRoleType);
        IRoleType role = (IRoleType) anonymousTypeOTElem;
        assertNotNull(role);

        IMethodMapping[] mappings = role.getMethodMappings();
        assertNotNull(mappings);
        assertTrue(mappings.length == 1);
        assertEquals(mappings[0].getElementName(), ANONYMOUS_MAPPING_NAME);
    }
}
