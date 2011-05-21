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
 * $Id: OTJStratumGenerationTest005.java 23494 2010-02-05 23:06:44Z stephan $
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
public class OTJStratumGenerationTest005 extends AbstractSourceMapGeneratorTest
{
	private org.eclipse.jdt.core.ICompilationUnit _superTeam; 
	private org.eclipse.jdt.core.ICompilationUnit _subTeam;
	private org.eclipse.jdt.core.ICompilationUnit _baseClass;
    
	
    public OTJStratumGenerationTest005(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return buildModelTestSuite(OTJStratumGenerationTest005.class);
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        
        _superTeam = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "callout",
                "SuperTeam.java");

        _subTeam = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "callout",
                "SubTeam.java");
        
        _baseClass = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "callout",
                "BaseClass.java");
    }
    
    public void testSmapGeneration1() throws JavaModelException
    {
        SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
        FileInfo fileInfo_role1 = stratum_role1.getOrCreateFileInfo("SuperTeam.java", "callout/SuperTeam.java");
        LineInfo lineInfo1_role1 = new LineInfo(4,4); // RoleA is l4-9
        lineInfo1_role1.setRepeatCount(6);
        LineInfo lineInfo2 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo3 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);

        fileInfo_role1.addLineInfo(lineInfo1_role1);
        fileInfo_role1.addLineInfo(lineInfo2);
        fileInfo_role1.addLineInfo(lineInfo3);
        
        stratum_role1.optimize();
        
        TYPENAME = "__OT__RoleA";
        _enclosingTypename = "SuperTeam";
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
    
    public void testSmapGeneration2()
    {
        SmapStratum stratum_role2 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);;
        
        FileInfo fileInfo = stratum_role2.getOrCreateFileInfo("Team.java", "org/objectteams/Team.java");
        LineInfo lineInfo1 = new LineInfo(OT_CONFINED_GET_TEAM_LINE,3);
        fileInfo.addLineInfo(lineInfo1);
        
        // SubTeam$__OT__Confined has no code, special line-nos are added to the previous fileInfo:
        LineInfo lineInfo2 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo3 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);
        fileInfo.addLineInfo(lineInfo2);
        fileInfo.addLineInfo(lineInfo3);
        
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
        SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
        
        FileInfo fileInfo1_role1 = stratum_role1.getOrCreateFileInfo("SuperTeam.java", "callout/SuperTeam.java");
        LineInfo fileInfo1_lineInfo1 = new LineInfo(4,4);
        LineInfo fileInfo1_lineInfo2 = new LineInfo(8,5);
        fileInfo1_role1.addLineInfo(fileInfo1_lineInfo1);
        fileInfo1_role1.addLineInfo(fileInfo1_lineInfo2);
        LineInfo fileInfo2_lineInfo3 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo fileInfo2_lineInfo4 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);

        fileInfo1_role1.addLineInfo(fileInfo2_lineInfo3);
        fileInfo1_role1.addLineInfo(fileInfo2_lineInfo4);

        stratum_role1.optimize();
        
        TYPENAME = "__OT__RoleA";
        _enclosingTypename = "SubTeam";
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
    	LineInfo lineInfo1 = new LineInfo(OT_CONFINED_GET_TEAM_LINE,5);
    	fileInfo.addLineInfo(lineInfo1);
    
        LineInfo lineInfo2 = new LineInfo(2,4);
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
