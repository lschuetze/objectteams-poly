/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: OTSuperTypeHierarchyTest007_Stress.java 23494 2010-02-05 23:06:44Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.superhierarchy;

import junit.framework.Test;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.objectteams.otdt.core.hierarchy.OTTypeHierarchies;
import org.eclipse.objectteams.otdt.tests.hierarchy.FileBasedHierarchyTest;

public class OTSuperTypeHierarchyTest016_Regression extends FileBasedHierarchyTest
{
    private IType _T1;    
	private IType _T1_IR;

	public OTSuperTypeHierarchyTest016_Regression(String name)
	{
		super(name);
	}
	
	@SuppressWarnings("unused") // variants to statically choose from
	public static Test suite()
	{
		if (true)
		{
			return new Suite(OTSuperTypeHierarchyTest016_Regression.class);
		}
		junit.framework.TestSuite suite = 
			new Suite(OTSuperTypeHierarchyTest016_Regression.class.getName());
		return suite;
	}
	
	public void setUpSuite() throws Exception
	{
		setTestProjectDir("HierarchyRegression");
		super.setUpSuite();
		
		String srcFolder = "src";
		String pkg = "t";
		
        _T1 = getType(getTestProjectDir(), srcFolder, pkg, "T1");
        
        _T1_IR = getRole(_T1, "T1.IR");
	}
	
	// see http://bugs.eclipse.org/366976
	public void testInterfaceAtSyntaxError()
        throws JavaModelException
	{
        _focusType = _T1_IR;
        _testObj = createSuperTypeHierarchy(_focusType);

        // due to severe syntax errors, T1.IR is not connected:
        IType [] actual = OTTypeHierarchies.getInstance().getAllSuperInterfaces(_testObj, _focusType);
        assertEquals(0, actual.length);
        
        actual = OTTypeHierarchies.getInstance().getSuperclasses(_testObj, _focusType);
        assertEquals(0, actual.length);        
    }
}
