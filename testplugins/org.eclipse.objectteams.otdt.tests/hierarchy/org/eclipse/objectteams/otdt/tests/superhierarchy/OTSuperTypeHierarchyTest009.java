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
package org.eclipse.objectteams.otdt.tests.superhierarchy;

import junit.framework.Test;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.hierarchy.TypeHierarchy;
import org.eclipse.objectteams.otdt.tests.hierarchy.FileBasedHierarchyTest;
import org.eclipse.objectteams.otdt.core.TypeHelper;
import org.eclipse.objectteams.otdt.core.hierarchy.OTTypeHierarchies;

/**
 *
 * @author michael
 */
public class OTSuperTypeHierarchyTest009 extends FileBasedHierarchyTest {

	@SuppressWarnings("unused")
	private ITypeHierarchy _testObj;
	private IType _focusType;
    private IType _T10;
    private IType _T11;
	private IType _objectType;
    private IType _T10T00;
    private IType _T11T00;
    private IType _T10T00R0;
    private IType _T11T00R0;



	public OTSuperTypeHierarchyTest009(String name)
	{
		super(name);
	}

	public static Test suite()
	{
		if (true)
		{
			return new Suite(OTSuperTypeHierarchyTest009.class);
		}
		@SuppressWarnings("unused")
		junit.framework.TestSuite suite =
			new Suite(OTSuperTypeHierarchyTest009.class.getName());
		return suite;
	}

	public void setUpSuite() throws Exception
	{
		setTestProjectDir("Hierarchy");
		super.setUpSuite();

		String srcFolder = "src";
		String pkg = "test009";

		_T10 =
			getType(getTestProjectDir(),
					srcFolder,
					pkg,
					"T10");

        _T11 =
            getType(getTestProjectDir(),
                    srcFolder,
                    pkg,
                    "T11");


        _objectType =
            getType(getTestProjectDir(),
                    "rt.jar",
                    "java.lang",
                    "Object");

        _T10T00R0 = TypeHelper.findNestedRoleType(_T10, "T10.T00.R0");
        _T11T00R0 = TypeHelper.findNestedRoleType(_T11, "T11.T00.R0");
        _T10T00 = TypeHelper.findNestedRoleType(_T10, "T10.T00");
        _T11T00 = TypeHelper.findNestedRoleType(_T11, "T11.T00");

    }


	public void testCreation()
	{
		assertCreation(_T10);
        assertCreation(_T11);
        assertCreation(_T10T00);
        assertCreation(_T11T00);
        assertCreation(_T10T00R0);
        assertCreation(_T11T00R0);


    }


    public void testGetTSuperTypes_T11T00R0() throws JavaModelException
    {
        _focusType = _T11T00R0;

        TypeHierarchy hierarchy =
            new TypeHierarchy(_focusType, null, _focusType.getJavaProject(), false);
        hierarchy.refresh(new NullProgressMonitor());

        IType[] actual = OTTypeHierarchies.getInstance().getTSuperTypes(hierarchy, _focusType);
        IType[] expected = new IType[] { _T10T00R0 };

        assertEquals(expected.length, actual.length);
        assertTrue(compareTypes(expected, actual));
    }



    public void testGetSuperclass_T11T00R0() throws JavaModelException
    {
        _focusType = _T11T00R0;

        TypeHierarchy hierarchy =
            new TypeHierarchy(_focusType, null, _focusType.getJavaProject(), false);
        hierarchy.refresh(new NullProgressMonitor());

        IType actual = hierarchy.getSuperclass(_focusType);
        IType expected = _T10T00R0;

        assertTrue(compareTypes(expected, actual));
    }


	public void testGetAllSuperclasses_T11T00R0() throws JavaModelException
    {
        _focusType = _T11T00R0;

        TypeHierarchy hierarchy =
            new TypeHierarchy(_focusType, null, _focusType.getJavaProject(), false);
        hierarchy.refresh(new NullProgressMonitor());

        IType[] actual = hierarchy.getAllSuperclasses(_focusType);
        IType[] expected = new IType[] { _objectType,
                                         _T10T00R0,
                                       };

        assertEquals(expected.length, actual.length);
        assertTrue(compareTypes(expected, actual));
    }


    public void testGetSuperInterfaces_T11T00R0() throws JavaModelException
    {
        _focusType = _T11T00R0;

        TypeHierarchy hierarchy =
            new TypeHierarchy(_focusType, null, _focusType.getJavaProject(), false);
        hierarchy.refresh(new NullProgressMonitor());

        IType[] actual = OTTypeHierarchies.getInstance().getSuperInterfaces(hierarchy, _focusType);
        IType[] expected = new IType[0];

        assertEquals(expected.length, actual.length);
        assertTrue(compareTypes(expected, actual));
    }







}
