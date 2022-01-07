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
package org.eclipse.objectteams.otdt.tests.subhierarchy;

import junit.framework.Test;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.hierarchy.TypeHierarchy;
import org.eclipse.objectteams.otdt.tests.hierarchy.FileBasedHierarchyTest;
import org.eclipse.objectteams.otdt.core.TypeHelper;

/**
 *
 * @author Michael Krueger (mkr)
 */
public class OTSubTypeHierarchyTest011 extends FileBasedHierarchyTest {

	@SuppressWarnings("unused")
	private ITypeHierarchy _testObj;
	private IType _focusType;
    private IType _T10;
    private IType _T21;
	@SuppressWarnings("unused")
	private IType _objectType;
    private IType _T21T11T0R1;
    private IType _T10T0R1;
    private IType _T10T0R2;


	public OTSubTypeHierarchyTest011(String name)
	{
		super(name);
	}

	public static Test suite()
	{
		if (true)
		{
			return new Suite(OTSubTypeHierarchyTest011.class);
		}
		@SuppressWarnings("unused")
		junit.framework.TestSuite suite =
			new Suite(OTSubTypeHierarchyTest011.class.getName());
		return suite;
	}

	public void setUpSuite() throws Exception
	{
		setTestProjectDir("Hierarchy");
		super.setUpSuite();

		String srcFolder = "src";
		String pkg = "test011";

		_T10 =
			getType(getTestProjectDir(),
					srcFolder,
					pkg,
					"T10");

        _T21 =
            getType(getTestProjectDir(),
                    srcFolder,
                    pkg,
                    "T21");


        _objectType =
            getType(getTestProjectDir(),
                    "rt.jar",
                    "java.lang",
                    "Object");

        _T21T11T0R1 = TypeHelper.findNestedRoleType(_T21, "T21.T11.T0.R1");
        _T10T0R1 = TypeHelper.findNestedRoleType(_T10, "T10.T0.R1");
        _T10T0R2 = TypeHelper.findNestedRoleType(_T10, "T10.T0.R2");

    }


	public void testCreation()
	{
		assertCreation(_T10);
        assertCreation(_T21);
        assertCreation(_T21T11T0R1);
        assertCreation(_T10T0R1);
        assertCreation(_T10T0R2);
    }


    public void testGetSubtypes_T10T0R1() throws JavaModelException
    {
        _focusType = _T10T0R1;

        TypeHierarchy hierarchy =
            new TypeHierarchy(_focusType, null, _focusType.getJavaProject(), true);
        hierarchy.refresh(new NullProgressMonitor());

        IType[] actual = hierarchy.getSubtypes(_focusType);
        IType[] expected = new IType[] { _T21T11T0R1 };

        assertEquals(expected.length, actual.length);
        assertTrue(compareTypes(expected, actual));
    }

    public void testGetSubtypes_T10T0R2() throws JavaModelException
    {
        _focusType = _T10T0R2;

        TypeHierarchy hierarchy =
            new TypeHierarchy(_focusType, null, _focusType.getJavaProject(), true);
        hierarchy.refresh(new NullProgressMonitor());

        IType[] actual = hierarchy.getSubtypes(_focusType);
        IType[] expected = new IType[] {
//        								_T21T11T0R1, // indirect sub
        								_T10T0R1
										};

        assertEquals(expected.length, actual.length);
        assertTrue(compareTypes(expected, actual));
    }

    public void testGetAllSubtypes_T10T0R1() throws JavaModelException
    {
        _focusType = _T10T0R1;

        TypeHierarchy hierarchy =
            new TypeHierarchy(_focusType, null, _focusType.getJavaProject(), true);
        hierarchy.refresh(new NullProgressMonitor());

        IType[] actual = hierarchy.getAllSubtypes(_focusType);
        IType[] expected = new IType[] { _T21T11T0R1 };

        assertEquals(expected.length, actual.length);
        assertTrue(compareTypes(expected, actual));
    }


    public void testGetAllSubtypes_T10T0R2() throws JavaModelException
    {
        _focusType = _T10T0R2;

        TypeHierarchy hierarchy =
            new TypeHierarchy(_focusType, null, _focusType.getJavaProject(), true);
        hierarchy.refresh(new NullProgressMonitor());

        IType[] actual = hierarchy.getAllSubtypes(_focusType);
        IType[] expected = new IType[] { _T10T0R1, _T21T11T0R1 };

        assertEquals(expected.length, actual.length);
        assertTrue(compareTypes(expected, actual));
    }

}
