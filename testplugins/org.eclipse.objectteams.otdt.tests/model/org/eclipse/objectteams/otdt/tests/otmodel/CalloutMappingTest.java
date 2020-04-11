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
package org.eclipse.objectteams.otdt.tests.otmodel;

import junit.framework.Test;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.objectteams.otdt.core.ICalloutMapping;
import org.eclipse.objectteams.otdt.core.ICalloutToFieldMapping;
import org.eclipse.objectteams.otdt.core.IMethodMapping;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.core.OTModelManager;

/**
 *
 * testcase:
 * a team class with a bound role class and various callout binding declarations
 */
public class CalloutMappingTest extends FileBasedModelTest
{

    private IType _teamJavaElem;
    private IType _roleJavaElem;

    public CalloutMappingTest(String name)
    {
        super(name);
    }
    static boolean ALL_TESTS=true;
    public static Test suite()
    {
        if (ALL_TESTS) return new Suite(CalloutMappingTest.class);
        junit.framework.TestSuite suite = new Suite(CalloutMappingTest.class.getName());
        return suite;
    }

    public void setUpSuite() throws Exception
    {
        setTestProjectDir("CalloutMapping");
        super.setUpSuite();
    }

    public void setUp() throws Exception
    {
    		super.setUp();

        try
        {
            ICompilationUnit teamUnit = getCompilationUnit(
                    "CalloutMapping",
                    "src",
                    "teampkg",
                    "SampleTeam" + ".java");

            _teamJavaElem = teamUnit.getType("SampleTeam");

            _roleJavaElem = _teamJavaElem.getType("SampleRole");

        }
        catch (JavaModelException ex)
        {
            ex.printStackTrace();
        }
    }

    private IRoleType getRoleOTElem()
    {
        if ((_roleJavaElem != null) && (_roleJavaElem.exists()))
        {
            IOTType roleOTElem = OTModelManager.getOTElement(_roleJavaElem);

            if ((roleOTElem != null) && (roleOTElem instanceof IRoleType))
            {
                return ((IRoleType) roleOTElem);
            }
        }
        return null;
    }

    public void testExistenceOfMappings() throws JavaModelException
    {
        IRoleType roleOTElem = getRoleOTElem();
        assertNotNull(roleOTElem);

        IMethodMapping[] allMethodMappings = roleOTElem.getMethodMappings();
        assertEquals(4, allMethodMappings.length);

        IMethodMapping[] calloutMethodMappings = roleOTElem.getMethodMappings(IRoleType.CALLOUTS);
        assertEquals(4, calloutMethodMappings.length);

        IMethodMapping[] callinMethodMappings = roleOTElem.getMethodMappings(IRoleType.CALLINS);
        assertEquals(0, callinMethodMappings.length);
    }

    public void testMapping1FromMemento() throws JavaModelException
    {
        IRoleType roleOTElem = getRoleOTElem();
        assertNotNull(roleOTElem);
        IMethodMapping[] calloutMethodMappings = roleOTElem.getMethodMappings(IRoleType.CALLOUTS);

        IMethodMapping mapping = calloutMethodMappings[0];
        assertNotNull(mapping);

        String token = mapping.getHandleIdentifier();

        SourceType sourceType = (SourceType)roleOTElem.getCorrespondingJavaElement();
		IJavaElement fromMemento = JavaCore.create(token, sourceType.getCompilationUnit().getOwner());

        assertEquals("Elements should be equal", mapping, fromMemento);

        assertTrue("Element should exist", fromMemento.exists());

        assertTrue("Mapping should have signature", ((IMethodMapping)fromMemento).hasSignature());
    }

    public void testMapping2FromMemento() throws JavaModelException
    {
        IRoleType roleOTElem = getRoleOTElem();
        assertNotNull(roleOTElem);
        IMethodMapping[] calloutMethodMappings = roleOTElem.getMethodMappings(IRoleType.CALLOUTS);

        IMethodMapping mapping = calloutMethodMappings[1];
        assertNotNull(mapping);

        String token = mapping.getHandleIdentifier();

        SourceType sourceType = (SourceType)roleOTElem.getCorrespondingJavaElement();
		IJavaElement fromMemento = JavaCore.create(token, sourceType.getCompilationUnit().getOwner());

        assertEquals("Elements should be equal", mapping, fromMemento);

        assertTrue("Element should exist", fromMemento.exists());

        assertFalse("Mapping should not have signature", ((IMethodMapping)fromMemento).hasSignature());
    }

    public void testMapping3FromMemento() throws JavaModelException
    {
        IRoleType roleOTElem = getRoleOTElem();
        assertNotNull(roleOTElem);
        IMethodMapping[] calloutMethodMappings = roleOTElem.getMethodMappings(IRoleType.CALLOUTS);

        IMethodMapping mapping = calloutMethodMappings[2];
        assertNotNull(mapping);

        String token = mapping.getHandleIdentifier();

        SourceType sourceType = (SourceType)roleOTElem.getCorrespondingJavaElement();
		IJavaElement fromMemento = JavaCore.create(token, sourceType.getCompilationUnit().getOwner());

        assertEquals("Elements should be equal", mapping, fromMemento);

        assertTrue("Element should exist", fromMemento.exists());

        ICalloutMapping mappingFromMemento = (ICalloutMapping)fromMemento;
		assertTrue("Mapping should have signature", mappingFromMemento.hasSignature());

        assertTrue("Expecting 1 role parameter", mappingFromMemento.getRoleMethodHandle().getArgumentTypes().length == 1);
        assertTrue("Expecting 2 base parameters", mappingFromMemento.getBaseMethodHandle().getArgumentTypes().length == 2);
    }

    public void testMapping4FromMemento() throws JavaModelException
    {
        IRoleType roleOTElem = getRoleOTElem();
        assertNotNull(roleOTElem);
        IMethodMapping[] calloutMethodMappings = roleOTElem.getMethodMappings(IRoleType.CALLOUTS);

        IMethodMapping mapping = calloutMethodMappings[3];
        assertNotNull(mapping);

        String token = mapping.getHandleIdentifier();

        SourceType sourceType = (SourceType)roleOTElem.getCorrespondingJavaElement();
		IJavaElement fromMemento = JavaCore.create(token, sourceType.getCompilationUnit().getOwner());

        assertEquals("Elements should be equal", mapping, fromMemento);

        assertTrue("Element should exist", fromMemento.exists());

        assertTrue("Expecting callout to field", fromMemento instanceof ICalloutToFieldMapping);
        ICalloutToFieldMapping mappingFromMemento = (ICalloutToFieldMapping)fromMemento;
		assertTrue("Mapping should have signature", mappingFromMemento.hasSignature());

        assertFalse("Expecting no setter", mappingFromMemento.getBaseFieldHandle().isSetter());
    }

    public void testCtfWithAnnotation() throws JavaModelException
    {
        IRoleType roleOTElem = getRoleOTElem();
        assertNotNull(roleOTElem);
        IMethodMapping[] calloutMethodMappings = roleOTElem.getMethodMappings(IRoleType.CALLOUTS);

        IMethodMapping mapping = calloutMethodMappings[3];
        assertNotNull(mapping);

        IAnnotation[] annotations = mapping.getAnnotations();
        assertTrue("Annotations should not be null", annotations != null);
        assertEquals("Wrong number of annotations", 1, annotations.length);
        assertEquals("Wrong annotation type", "SuppressWarnings", annotations[0].getElementName());
        IMemberValuePair[] memberValuePairs = annotations[0].getMemberValuePairs();
        assertTrue("Pairs should not be null", memberValuePairs != null);
        assertEquals("Wrong number of pairs", 1, memberValuePairs.length);
        assertEquals("Wrong value", "decapsulation", memberValuePairs[0].getValue());
    }


    public void testCtfExceptions() throws JavaModelException
    {
        IRoleType roleOTElem = getRoleOTElem();
        assertNotNull(roleOTElem);
        IMethodMapping[] calloutMethodMappings = roleOTElem.getMethodMappings(IRoleType.CALLOUTS);

        IMethodMapping mapping = calloutMethodMappings[3];
        assertNotNull(mapping);

        assertEquals("Wrong number of declared exceptions", 0, ((IMethod)mapping).getExceptionTypes().length); // and don't NPE
    }

}
