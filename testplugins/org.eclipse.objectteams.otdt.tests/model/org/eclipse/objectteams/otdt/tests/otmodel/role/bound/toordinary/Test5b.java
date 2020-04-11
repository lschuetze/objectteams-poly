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
package org.eclipse.objectteams.otdt.tests.otmodel.role.bound.toordinary;

import junit.framework.Test;

/**
 *
 * This class contains testing methods for a test setting with a role class with a method
 * and a method mapping
 * whereas the method (is concrete and) has no parameters,
 * the method mapping is a callout mapping (=>)
 * and the role class is bound to a baseclass
 */

//TODO (kaschja) test special properties of "=>"-MethodMappings by the time there is a distinction between "->" and "=>" in the OT-model
public class Test5b extends Test5a
{
    public Test5b(String name)
    {
        super(name);
//TODO (kaschja) replace "->" by "=>" by the time there is a distinction between them in the OT-model
        setMappingName("roleMethod -> baseMethod");
    }

    public static Test suite()
    {
        if (true)
        {
            return new Suite(Test5b.class);
        }
        junit.framework.TestSuite suite = new Suite(Test5b.class
            .getName());
        return suite;
    }

    public void setUpSuite() throws Exception
    {
        super.setUpSuite();
        getTestSetting().setTeamClass("Team_5b");
    }



}
