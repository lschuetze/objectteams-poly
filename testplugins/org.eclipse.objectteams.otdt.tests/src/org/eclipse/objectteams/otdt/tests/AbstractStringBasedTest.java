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
package org.eclipse.objectteams.otdt.tests;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.jdt.core.tests.model.ModifyingResourceTests;

/**
 * @author jwloka
 *
 */
public class AbstractStringBasedTest extends ModifyingResourceTests
{
    public static final String TEST_PROJECT = "OTModelTest";

    private IJavaProject _testPrj;

    public AbstractStringBasedTest(String name)
    {
        super(name);
    }

    protected void createProjectFile(String path, String content)
        throws CoreException
    {
        super.createFile("/" + TEST_PROJECT + path, content);
    }

    protected void createProjectFolder(String folderName) throws CoreException
    {
        super.createFolder("/" + TEST_PROJECT + "/" + folderName);
    }

    protected IJavaProject getTestProject()
    {
        return _testPrj;
    }

    protected void setUp() throws Exception
    {
		super.setUp();
        _testPrj = createJavaProject(TEST_PROJECT, new String[]
        {
            ""
        }, "");
    }

    public void tearDown() throws Exception
    {
        deleteProject(TEST_PROJECT);
    }

    public void setTestProject(IJavaProject prj)
    {
        _testPrj = prj;
    }

    protected IOTType getOTElem(String name) throws JavaModelException
    {
       IType javaElem = getTestProject().findType(name);
       return OTModelManager.getOTElement(javaElem);
    }
}