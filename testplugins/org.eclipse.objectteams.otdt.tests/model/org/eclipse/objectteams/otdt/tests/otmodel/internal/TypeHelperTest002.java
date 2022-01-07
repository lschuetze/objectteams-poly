/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2021 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.tests.otmodel.internal;

import junit.framework.Test;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.core.TypeHelper;
import org.eclipse.objectteams.otdt.tests.otmodel.FileBasedModelTest;

/**
 * @author mkr
 */
public class TypeHelperTest002 extends FileBasedModelTest
{

    private IOTType _testObject;
    private IRoleType _t4r1;
    private IRoleType _t4r2;
    private IRoleType _t3r3;
    private IRoleType _t5r1;

    public TypeHelperTest002(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new Suite(TypeHelperTest002.class);
    }

    /**
     * Creates a team hierarchy with implicit role type inheritance.
     */
    public void setUpSuite() throws Exception
    {
        setTestProjectDir("Hierarchy");
        super.setUpSuite();

        IType teamIType =
            getType(getTestProjectDir(),
                "analysis",
                "teamhierarchy",
                "Team5");
        _testObject = OTModelManager.getOTElement(teamIType);

        _t4r1 =
            getRole(getTestProjectDir(),
                "analysis",
                "teamhierarchy",
                "Team4",
                "R1");

        _t4r2 =
            getRole(getTestProjectDir(),
                "analysis",
                "teamhierarchy",
                "Team4",
                "R2");

        _t3r3 =
            getRole(getTestProjectDir(),
                "analysis",
                "teamhierarchy",
                "Team3",
                "R3");

        _t5r1 =
            getRole(getTestProjectDir(),
                "analysis",
                "teamhierarchy",
                "Team5",
                "R1");
    }

    protected void setUp() throws Exception {
    	this.indexDisabledForTest = false;
        super.setUp();
    }

    public void testExists_testObject()
    	throws JavaModelException
	{
	    assertNotNull(_testObject);
	    assertTrue(_testObject.exists());
        assertTrue(_testObject.isTeam());
	}

    public void testGetInheritedRoleTypes() throws JavaModelException
    {
        IType[] expected = new IType[] { _t4r1, _t4r2, _t3r3 };
        IType[] actual = TypeHelper.getInheritedRoleTypes(_testObject);

        assertTrue( compareTypes(expected, actual) );
    }

    public void testGetAllRoleTypes() throws JavaModelException
    {
        IType[] expected = new IType[] { _t5r1, _t4r2, _t3r3 };
        IType[] actual = TypeHelper.getAllRoleTypes(_testObject);

        assertTrue( compareTypes(expected, actual) );
    }

    // test hierarchy of team extending non-team
    public void testGetAllRoleTypes2() throws JavaModelException
    {
        IType teamIType =
            getType(getTestProjectDir(),
                "analysis",
                "teamhierarchy",
                "Team6");
        IOTType team6 = OTModelManager.getOTElement(teamIType);

        IType[] expected = new IType[] {
			        		getRole(getTestProjectDir(),
				                "analysis",
				                "teamhierarchy",
				                "Team6",
				                "R")
				            };
        IType[] actual = TypeHelper.getAllRoleTypes(team6);

        assertTrue( compareTypes(expected, actual) );
    }
}