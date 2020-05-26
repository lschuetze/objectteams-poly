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
 * $Id$
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.dom.rewrite.describing;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.CallinMappingDeclaration;
import org.eclipse.jdt.core.dom.RoleTypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.objectteams.otdt.ui.tests.util.OTJavaProjectHelper;



public class AstRewritingDescribingTest
	extends org.eclipse.jdt.core.tests.rewrite.describing.ASTRewritingTest
{

	public AstRewritingDescribingTest(String name) {
		super(name);
	}

	public AstRewritingDescribingTest(String name, int apilevel) {
		super(name, apilevel);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
        OTJavaProjectHelper.addNatureToProject(project1.getProject(), JavaCore.OTJ_NATURE_ID, null);
	}

	public static RoleTypeDeclaration findRoleTypeDeclaration (TypeDeclaration teamType, String roleName)
	{
		for (Object member : teamType.bodyDeclarations()) {
			if (member instanceof RoleTypeDeclaration) {
				RoleTypeDeclaration roleType = (RoleTypeDeclaration)member;
				if (roleType.getName().getIdentifier().equals(roleName))
					return (RoleTypeDeclaration)member;
			}
		}
		return null;
	}

	public static CallinMappingDeclaration findCallinMappingDeclaration(RoleTypeDeclaration typeDecl, String roleMethodName)
	{
		CallinMappingDeclaration[] callinCallouts = typeDecl.getCallIns();
		for (int i= 0; i < callinCallouts.length; i++) {
			if (roleMethodName.equals(callinCallouts[i].getRoleMappingElement().getName().getIdentifier())) {
				return callinCallouts[i];
			}
		}
		return null;
	}

}
