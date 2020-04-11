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
package org.eclipse.objectteams.otdt.tests.compiler.smap;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.objectteams.otdt.core.compiler.ISMAPConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.FileInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.LineInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.SmapStratum;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.TeamSmapGenerator;

/**
 * @author ike
 *
 */
public class OTJStratumGenerationTest003 extends AbstractSourceMapGeneratorTest
{
	private org.eclipse.jdt.core.ICompilationUnit _team1;
	private org.eclipse.jdt.core.ICompilationUnit _team2;
	private org.eclipse.jdt.core.ICompilationUnit _role;
	private String _packagePath;

	static {
//		TESTS_NAMES = new String[] { "testSmapGeneration3" };
	}

    public OTJStratumGenerationTest003(String name)
    {
        super(name);
    }

    public static Test suite()
    {
    	return buildModelTestSuite(OTJStratumGenerationTest003.class);
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        _packagePath = "roleFileAndCopyInh";

        _team1 = getCompilationUnit(
                getTestProjectDir(),
                "src",
                _packagePath,
                "SubTeam.java");

        _team2 = getCompilationUnit(
                getTestProjectDir(),
                "src",
                _packagePath,
                "SuperTeam.java");

        _role = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "roleFileAndCopyInh.SuperTeam",
                "RoleA.java");
    }

    public void testSmapGeneration1() throws JavaModelException
    {

        TYPENAME = "__OT__RoleA";
        _enclosingTypename = "SubTeam";

        String roleFileSourceName = "RoleA.java";

        SmapStratum stratum_role = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
        FileInfo fileInfo1 = stratum_role.getOrCreateFileInfo(
        		roleFileSourceName, _packagePath + "/" + "SuperTeam" + "/" + roleFileSourceName);

        LineInfo lineInfo2 = new LineInfo(9,1); // method roleMethod (9..) mapped to synthetic line 1.. (no own lines in SubTeam.RoleA).
        lineInfo2.setRepeatCount(2);
        LineInfo lineInfo1 = new LineInfo(5,3); // role (5..) mapped to synthetic line 3
        LineInfo lineInfo3 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo4 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);

        fileInfo1.addLineInfo(lineInfo1);
        fileInfo1.addLineInfo(lineInfo2);
        fileInfo1.addLineInfo(lineInfo3);
        fileInfo1.addLineInfo(lineInfo4);

        stratum_role.optimize();

        List<SmapStratum> strata_role1 = new ArrayList<SmapStratum>();
        strata_role1.add(stratum_role);

        expectedStrata.put(TYPENAME, strata_role1);

        try
        {
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_team2, _team1, _role});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }

    public void testSmapGeneration2()
    {
        SmapStratum stratum_role = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);

        FileInfo fileInfo = stratum_role.getOrCreateFileInfo("Team.java", "org/objectteams/Team.java");
        LineInfo lineInfo1 = new LineInfo(OT_CONFINED_GET_TEAM_LINE,7); // getTeam mapped to synthetic line 7
        fileInfo.addLineInfo(lineInfo1);

        LineInfo lineInfo2 = new LineInfo(3,6); // class position to first synthetic line 6
        fileInfo.addLineInfo(lineInfo2);

        // SubTeam$RoleA has no code, special line-nos are added to the previous fileInfo:
        LineInfo lineInfo3 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo4 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);
        fileInfo.addLineInfo(lineInfo3);
        fileInfo.addLineInfo(lineInfo4);

        stratum_role.optimize();

        TYPENAME = "__OT__Confined";
        _enclosingTypename = "SubTeam";
        List<SmapStratum> strata_role2 = new ArrayList<SmapStratum>();
        strata_role2.add(stratum_role);

        expectedStrata.put(TYPENAME, strata_role2);

        try
        {
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_team2, _team1, _role});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }

    public void testSmapGeneration3()
    {
        TYPENAME = "SubTeam";
        String subPackage = "bug316601";
        _enclosingTypename = null;

        String teamSourceName = "SubTeam.java";
        String role2SourceName = "Role2.java";

        SmapStratum stratum_subteam = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);

		FileInfo fileInfo2 = stratum_subteam.getOrCreateFileInfo(
        		role2SourceName, _packagePath + '/' + subPackage + '/' + TYPENAME + '/' + role2SourceName);

        LineInfo lineInfo3 = new LineInfo(3,17);  // Role2 header (3) mapped to synthetic line 17 (for lifting)

        fileInfo2.addLineInfo(lineInfo3);


        FileInfo fileInfo1 = stratum_subteam.getOrCreateFileInfo(
        		teamSourceName, _packagePath + '/' + subPackage + '/' + teamSourceName);

        LineInfo lineInfo1 = new LineInfo(1,1);  // 16 lines of subteam verbatim
        lineInfo1.setRepeatCount(16);
        LineInfo lineInfo2 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        lineInfo2.setRepeatCount(2);

        fileInfo1.addLineInfo(lineInfo1);
        fileInfo1.addLineInfo(lineInfo2);
        stratum_subteam.optimize();

        List<SmapStratum> strata_role1 = new ArrayList<SmapStratum>();
        strata_role1.add(stratum_subteam);

        expectedStrata.put(TYPENAME, strata_role1);

        try
        {
            ICompilationUnit superTeam = getCompilationUnit(
                    getTestProjectDir(),
                    "src",
                    _packagePath+'.'+subPackage,
                    "SuperTeam.java");

            ICompilationUnit superRole1 = getCompilationUnit(
                    getTestProjectDir(),
                    "src",
                    _packagePath+'.'+subPackage+'.'+"SuperTeam",
                    "Role1.java");

            ICompilationUnit subTeam = getCompilationUnit(
                    getTestProjectDir(),
                    "src",
                    _packagePath+'.'+subPackage,
                    "SubTeam.java");

            ICompilationUnit subRole2 = getCompilationUnit(
                    getTestProjectDir(),
                    "src",
                    _packagePath+'.'+subPackage+'.'+"SubTeam",
                    role2SourceName);

            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{superTeam, superRole1, subTeam, subRole2});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }

	public void callback(CompilationUnitDeclaration cuDecl) {
		if (_enclosingTypename != null) {
			super.callback(cuDecl);
			return;
		}
		// testing the team itself
	    String cuDeclName = String.valueOf(cuDecl.getMainTypeName());
	    if (!TYPENAME.equals(cuDeclName))
	        return;


	    TypeDeclaration typeDecl = cuDecl.types[0];

	    assertNotNull("TypeDeclaration should not be null.", typeDecl);

	    assertTrue("TypeDeclaration should be a team.", typeDecl.isTeam());

	    TeamSmapGenerator teamSmapGenerator = new TeamSmapGenerator(typeDecl);
        teamSmapGenerator.addStratum("OTJ");
        teamSmapGenerator.generate();
        List actualStrata = teamSmapGenerator.getStrata();

        assertEquals("Strata of type \"" + TYPENAME + "\" should be equal.\n", expectedStrata.get(TYPENAME).toString(), actualStrata.toString());
	}
}
