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
 * $Id$
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.refactoring;

import org.eclipse.objectteams.otdt.tests.FileBasedTest;

/**
 * @author svacina
 */
public class FileBasedRefactoringTest extends FileBasedTest {

    public FileBasedRefactoringTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
    	this.indexDisabledForTest = false;
    	super.setUp();
    }

    protected String getPluginID() {
        return "org.eclipse.objectteams.otdt.ui.tests.refactoring";
    }
}
