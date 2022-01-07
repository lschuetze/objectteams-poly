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

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.objectteams.otdt.core.compiler.ISMAPConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.FileInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.LineInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.SmapStratum;

/**
 * @author ike
 *
 */
public class OTJStratumGenerationTest006 extends AbstractSourceMapGeneratorTest
{
	private org.eclipse.jdt.core.ICompilationUnit _superTeam;
	private org.eclipse.jdt.core.ICompilationUnit _subTeam;
	private org.eclipse.jdt.core.ICompilationUnit _baseClass;


    public OTJStratumGenerationTest006(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return buildModelTestSuite(OTJStratumGenerationTest006.class);
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        _superTeam = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "calloutOverride",
                "SuperTeam.java");

        _subTeam = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "calloutOverride",
                "SubTeam.java");

        _baseClass = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "calloutOverride",
                "BaseClass.java");
    }

    public void testSmapGeneration1() throws JavaModelException
    {
    	TYPENAME = "__OT__RoleA";
    	_enclosingTypename = "SuperTeam";

    	SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
        FileInfo fileInfo_role1 = stratum_role1.getOrCreateFileInfo("SuperTeam.java", "calloutOverride/SuperTeam.java");
        LineInfo lineInfo1_role1 = new LineInfo(4,4); // RoleA is l4-15
        lineInfo1_role1.setRepeatCount(12);
        LineInfo lineInfo1_role2 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo1_role3 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);

        fileInfo_role1.addLineInfo(lineInfo1_role1);
        fileInfo_role1.addLineInfo(lineInfo1_role2);
        fileInfo_role1.addLineInfo(lineInfo1_role3);
        List<SmapStratum> strata_role1 = new ArrayList<SmapStratum>();
        strata_role1.add(stratum_role1);

        stratum_role1.optimize();

        expectedStrata.put(TYPENAME, strata_role1);

        try
        {
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_baseClass, _superTeam, _subTeam});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }

    public void testSmapGeneration2()
    {
        SmapStratum stratum_role2 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);

    	FileInfo fileinfo = stratum_role2.getOrCreateFileInfo("Team.java", "org/objectteams/Team.java");
    	LineInfo lineInfo1 = new LineInfo(OT_CONFINED_GET_TEAM_LINE,3);
    	fileinfo.addLineInfo(lineInfo1);

        LineInfo lineInfo2 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo3 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);
        fileinfo.addLineInfo(lineInfo2);
        fileinfo.addLineInfo(lineInfo3);

        stratum_role2.optimize();

        TYPENAME = "__OT__Confined";
        _enclosingTypename = "SuperTeam";
        List<SmapStratum> strata_role2 = new ArrayList<SmapStratum>();
        strata_role2.add(stratum_role2);

        expectedStrata.put(TYPENAME, strata_role2);

        try
        {
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_baseClass, _superTeam, _subTeam});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }

    public void testSmapGeneration3() throws JavaModelException
    {
        TYPENAME = "__OT__RoleA";
        _enclosingTypename = "SubTeam";

    	SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
    	FileInfo fileInfo2 = stratum_role1.getOrCreateFileInfo("SubTeam.java", "calloutOverride/SubTeam.java");

    	FileInfo fileInfo1 = stratum_role1.getOrCreateFileInfo("SuperTeam.java", "calloutOverride/SuperTeam.java");
        LineInfo lineInfo1 = new LineInfo(4,8); // role ctor at synth line 8
        LineInfo lineInfo2 = new LineInfo(8,11); // anotherRoleMethod at synth line 11
        lineInfo2.setRepeatCount(2);
        LineInfo lineInfo3 = new LineInfo(13,9); // roleMethod at synth line 9
        lineInfo3.setRepeatCount(2);

        fileInfo1.addLineInfo(lineInfo1);
        fileInfo1.addLineInfo(lineInfo2);
        fileInfo1.addLineInfo(lineInfo3);

        LineInfo lineInfo4 = new LineInfo(4,4); // RoleA is l4-7
        lineInfo4.setRepeatCount(4);
        LineInfo lineInfo7 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo8 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);

        fileInfo2.addLineInfo(lineInfo4);
        fileInfo2.addLineInfo(lineInfo7);
        fileInfo2.addLineInfo(lineInfo8);

        stratum_role1.optimize();

        List<SmapStratum> strata_role1 = new ArrayList<SmapStratum>();
        strata_role1.add(stratum_role1);

        expectedStrata.put(TYPENAME, strata_role1);

        try
        {
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_baseClass, _superTeam, _subTeam});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }

    public void testSmapGeneration4()
    {
        SmapStratum stratum_role2 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);

		FileInfo fileInfo = stratum_role2.getOrCreateFileInfo("Team.java", "org/objectteams/Team.java");
		LineInfo lineInfo1 = new LineInfo(OT_CONFINED_GET_TEAM_LINE,4);
		fileInfo.addLineInfo(lineInfo1);

        LineInfo lineInfo2 = new LineInfo(2,3);
        fileInfo.addLineInfo(lineInfo2);

        LineInfo lineInfo3 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo4 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);
        fileInfo.addLineInfo(lineInfo3);
        fileInfo.addLineInfo(lineInfo4);

        stratum_role2.optimize();

        TYPENAME = "__OT__Confined";
        _enclosingTypename = "SubTeam";
        List<SmapStratum> strata_role2 = new ArrayList<SmapStratum>();
        strata_role2.add(stratum_role2);

        expectedStrata.put(TYPENAME, strata_role2);

        try
        {
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_baseClass, _superTeam, _subTeam});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }

}
