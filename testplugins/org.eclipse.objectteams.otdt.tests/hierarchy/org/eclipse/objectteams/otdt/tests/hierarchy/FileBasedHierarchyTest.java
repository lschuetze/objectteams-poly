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
package org.eclipse.objectteams.otdt.tests.hierarchy;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.hierarchy.TypeHierarchy;
import org.eclipse.objectteams.otdt.tests.otmodel.FileBasedModelTest;

public class FileBasedHierarchyTest extends FileBasedModelTest
{
	protected ITypeHierarchy _testObj;
	protected IType _focusType;

    public FileBasedHierarchyTest(String name)
    {
        super(name);
    }

	@Override
	protected void setUp() throws Exception {
		this.indexDisabledForTest = false;
		super.setUp();
	}

	private ITypeHierarchy createTypeHierarchy(IType focusType, boolean computeSubtypes) throws JavaModelException
    {
    	ITypeHierarchy hierarchy = new TypeHierarchy(focusType, null, focusType.getJavaProject(), computeSubtypes);
		hierarchy.refresh(new NullProgressMonitor());
        return hierarchy;
    }

    public ITypeHierarchy createTypeHierarchy(IType focusType) throws JavaModelException
    {
    	return createTypeHierarchy(focusType, true);
    }

    public ITypeHierarchy createSuperTypeHierarchy(IType focusType) throws JavaModelException
    {
        return createTypeHierarchy(focusType, false);
    }



}
