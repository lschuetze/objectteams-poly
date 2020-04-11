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

import org.eclipse.objectteams.otdt.core.ICallinMapping;

/**
 *
 * This class contains testing methods for a test setting with a role class with a method
 * and a method mapping
 * whereas the method is concrete and has no parameters,
 * the method mapping is an after-callin mapping
 * and the role class is bound to a baseclass
 */
public class Test5e extends Test5c
{
    public Test5e(String name)
    {
        super(name);
        setExpectedCallinKind(ICallinMapping.KIND_AFTER);
    }

    public static Test suite()
    {
        if (true)
        {
            return new Suite(Test5e.class);
        }
        junit.framework.TestSuite suite = new Suite(Test5e.class
            .getName());
        return suite;
    }

    public void setUpSuite() throws Exception
    {
        super.setUpSuite();
        getTestSetting().setTeamClass("Team_5e");
    }
}
