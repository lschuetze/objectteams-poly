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
 * $Id: OTJStratumGenerationTest004.java 23494 2010-02-05 23:06:44Z stephan $
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
public class OTJStratumGenerationTest004 extends AbstractSourceMapGeneratorTest
{
	private org.eclipse.jdt.core.ICompilationUnit _team1; 
	@SuppressWarnings("unused")
	private org.eclipse.jdt.core.ICompilationUnit _team2;
	private org.eclipse.jdt.core.ICompilationUnit _role;
	
    public OTJStratumGenerationTest004(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new Suite(OTJStratumGenerationTest004.class);
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        
        _team1 = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "roleAndTeam",
                "TeamA.java");

        _role = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "roleAndTeam.TeamA",
                "RoleA.java");
    }
    
    public void testSmapGeneration1() throws JavaModelException
    {
        TYPENAME = "__OT__RoleA";
        _enclosingTypename = "TeamA";

    	SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
        FileInfo fileInfo1 = stratum_role1.getOrCreateFileInfo("RoleA.java", "roleAndTeam/TeamA/RoleA.java");
        LineInfo lineInfo1 = new LineInfo(1,1);
        lineInfo1.setRepeatCount(11);
        LineInfo lineInfo2 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo3 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);

        fileInfo1.addLineInfo(lineInfo1);
        fileInfo1.addLineInfo(lineInfo2);
        fileInfo1.addLineInfo(lineInfo3);
        
        stratum_role1.optimize();
        
        List<SmapStratum> strata_role1 = new ArrayList<SmapStratum>();
        strata_role1.add(stratum_role1);
        
        expectedStrata.put(TYPENAME, strata_role1);

        try
        {
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_team1, _role});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testSmapGeneration2()
    {
        TYPENAME = "__OT__Confined";
        _enclosingTypename = "TeamA";

    	SmapStratum stratum_role2 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
        FileInfo fileInfo1 = stratum_role2.getOrCreateFileInfo("Team.java", "org/objectteams/Team.java");
        LineInfo lineInfo1 = new LineInfo(OT_CONFINED_GET_TEAM_LINE,4);
        fileInfo1.addLineInfo(lineInfo1);
        
        LineInfo lineInfo2 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo3 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);
        fileInfo1.addLineInfo(lineInfo2);
        fileInfo1.addLineInfo(lineInfo3);
        
        stratum_role2.optimize();
        
        List<SmapStratum> strata_role2 = new ArrayList<SmapStratum>();
        strata_role2.add(stratum_role2);
        
        expectedStrata.put(TYPENAME, strata_role2);
        
        try
        {
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_team1, _role});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }

}
