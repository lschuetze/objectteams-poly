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
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.OTModelManager;

/**
 *
 * testcase:
 * a role class (defined insight the file of its team class) with an attribut
 * attribut assignment with instantiation of an anonymous class
 * the anonymous class is a team class with role class
 */
public class Test5 extends AttributeAssignmentTest
{

    private final String ANONYMOUS_INNERROLE_NAME = "RoleClass";


    public static Test suite()
    {
        if (true)
        {
            return new Suite(Test5.class);
        }
        junit.framework.TestSuite suite = new Suite(Test5.class
            .getName());
        return suite;
    }

    public Test5(String name)
    {
        super(name);
    }

    protected String getTeamName()
    {
        return "Test5_TeamB";
    }

    protected String getRoleName()
    {
        return "SampleRole";
    }

    public void testExistenceOfAnonymousTypeInOTModel() throws JavaModelException
    {
        IType anonymousTypeJavaElem = getAnonymousType();
        assertNotNull(anonymousTypeJavaElem);

        IOTType anonymousTypeOTElem = OTModelManager.getOTElement(anonymousTypeJavaElem);
        assertNotNull(anonymousTypeOTElem);
    }

    public void testTeamPropertyOfAnonymousType() throws JavaModelException
    {
        IType anonymousTypeJavaElem = getAnonymousType();
        assertNotNull(anonymousTypeJavaElem);

        IOTType anonymousTypeOTElem = OTModelManager.getOTElement(anonymousTypeJavaElem);
        assertNotNull(anonymousTypeOTElem);

        assertTrue(anonymousTypeOTElem.isTeam());
    }

    public void testContainmentOfRoleInAnonymousType() throws JavaModelException
    {
        IType anonymousTypeJavaElem = getAnonymousType();
        assertNotNull(anonymousTypeJavaElem);

        IOTType anonymousTypeOTElem = OTModelManager.getOTElement(anonymousTypeJavaElem);
        assertNotNull(anonymousTypeOTElem);
        assertTrue(anonymousTypeOTElem.isTeam());

//{OTModelUpdate
        IOTType[] innerTypes = (IOTType[]) anonymousTypeOTElem.getInnerTypes();
//haebor}
        assertNotNull(innerTypes);
        assertTrue(innerTypes.length == 1);
        assertTrue(innerTypes[0].isRole());
        assertEquals(innerTypes[0].getElementName(), ANONYMOUS_INNERROLE_NAME);
    }
}
