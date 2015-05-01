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
 * $Id: OTJStratumGenerationTest002.java 23494 2010-02-05 23:06:44Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.compiler.smap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Test;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.objectteams.otdt.core.compiler.ISMAPConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.FileInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.LineInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.SmapStratum;

/**
 * @author ike
 *
 */
public class OTJStratumGenerationTest002 extends AbstractSourceMapGeneratorTest
{
	private org.eclipse.jdt.core.ICompilationUnit _subTeam; 
	private org.eclipse.jdt.core.ICompilationUnit _superTeam;
	private org.eclipse.jdt.core.ICompilationUnit _superSuperTeam;
	
    public OTJStratumGenerationTest002(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return buildModelTestSuite(OTJStratumGenerationTest002.class);
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        
        _subTeam = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "copyInheritance",
                "SubTeam.java");

        _superTeam = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "copyInheritance",
                "SuperTeam.java");

        _superSuperTeam = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "copyInheritance",
                "SuperSuperTeam.java");
    }
    public void testSimpleCopyInheritanceSmapRoleA() throws JavaModelException
    {
    	TYPENAME = "__OT__RoleA";
    	_enclosingTypename= "SubTeam";

    	expectedStrata.put(TYPENAME, createExpectedRoleAStratum(true /*fullSource*/));
        
        HashMap<String, int[]> expectedMethodLineNumbers = new HashMap<String, int[]>();
        expectedMethodLineNumbers.put("copyInheritance.SubTeam$__OT__RoleA.roleMethod0(LcopyInheritance/SubTeam$TSuper__OT__SuperTeam;)V", new int[]{13,14,15});
        expectedMethodLineNumbers.put("copyInheritance.SubTeam$__OT__RoleA.roleMethod0(LcopyInheritance/SuperTeam$TSuper__OT__SuperSuperTeam;)V", new int[]{16,17});
        expectedMethodLineNumbers.put("copyInheritance.SubTeam$__OT__RoleA.roleMethod1()V", new int[]{18,20,21});
        expectedMethodLineNumbers.put("copyInheritance.SubTeam$__OT__RoleA.roleMethod2()V", new int[]{22,23});
        
        

        try
        {
            String outputPath = getWorkspaceRoot().getLocationURI().getPath()+"/"+getTestProjectDir()+"/bin";
			parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_superSuperTeam, _superTeam, _subTeam}, 
            				expectedMethodLineNumbers,
            				null/*classPaths*/,
            				outputPath); // need this so that class files are actually written for next phase
			
	        expectedStrata.put(TYPENAME, createExpectedRoleAStratum(false /*not fullSource*/));

            // recompile SubTeam only to check usage of byte code information (CopyInheritanceSrc):
            String [] classPaths = getDefaultClassPaths();
            int oldLen = classPaths.length;
			System.arraycopy(classPaths, 0, classPaths=new String[oldLen+1], 1, oldLen);
            classPaths[0] = outputPath;
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_subTeam}, 
            			    expectedMethodLineNumbers,
            			    classPaths,			 // needed for retrieving class files from previous phase
            			    null/*outputPath*/);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
		}
    }

    // helper for testSimpleCopyInheritanceSmapRoleA.
    // 
	List<SmapStratum> createExpectedRoleAStratum(boolean fullSource) {
		// these two lines are swapped when reading super teams as binary:
		int twelve = fullSource ? 12 : 11;
		int eleven = fullSource ? 11 : 12;

		SmapStratum stratum_role = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
 
        // letters (a) ... indicate the order in which lines are assigned
        // class position is used by ctor, initFields and getTeam
        
        FileInfo fileInfo0 = stratum_role.getOrCreateFileInfo("SuperSuperTeam.java", "copyInheritance/SuperSuperTeam.java");
		LineInfo lineInfo1_role0 = new LineInfo(4,twelve); // (c) class position (4) mapped to synthetic line 12
        LineInfo lineInfo2_role0 = new LineInfo(8,16); // (e) method roleMethod0 (8..) mapped to synthetic lines 16-17  
        lineInfo2_role0.setRepeatCount(2);
        LineInfo lineInfo3_role0 = new LineInfo(12,18); // (f) method roleMethod1 (12,14,15) mapped to synthetic lines 18,20,21
        lineInfo3_role0.setRepeatCount(4); // repeat 4 although line numbers have a "hole" at comment line 19
        
        fileInfo0.addLineInfo(lineInfo1_role0);
        fileInfo0.addLineInfo(lineInfo2_role0);
        fileInfo0.addLineInfo(lineInfo3_role0);
 
        FileInfo fileInfo1 = stratum_role.getOrCreateFileInfo("SuperTeam.java", "copyInheritance/SuperTeam.java");
		LineInfo lineInfo1_role1 = new LineInfo(4,eleven); // (b) class position (4) mapped to synthetic line 11
        LineInfo lineInfo2_role1 = new LineInfo(11,13);    // (d) roleMethod0 (11..) mapped to synthetic lines 13-15
        lineInfo2_role1.setRepeatCount(3);
        LineInfo lineInfo3_role1 = new LineInfo(8,22); 	   // (g) method roleMethod2 (8..) mapped to synthetic lines 22-23 
        lineInfo3_role1.setRepeatCount(2);
        
        fileInfo1.addLineInfo(lineInfo1_role1);
        fileInfo1.addLineInfo(lineInfo2_role1);
        fileInfo1.addLineInfo(lineInfo3_role1);

        
        FileInfo fileInfo2 = stratum_role.getOrCreateFileInfo("SubTeam.java", "copyInheritance/SubTeam.java");
        
        LineInfo lineInfo1_role2 = new LineInfo(5,5); // (a) all original lines from SubTeam.RoleA
        lineInfo1_role2.setRepeatCount(6);
        LineInfo lineInfo2_role2 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo3_role2 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);
        
        fileInfo2.addLineInfo(lineInfo1_role2);
        fileInfo2.addLineInfo(lineInfo2_role2);
        fileInfo2.addLineInfo(lineInfo3_role2);
        
        stratum_role.optimize();
        List <SmapStratum>strata_role1 = new ArrayList<SmapStratum>();
        strata_role1.add(stratum_role);
		return strata_role1;
	}
    
    public void testSimpleCopyInheritanceSmapConfined()
    {
    	SmapStratum stratum_role = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
    	
    	FileInfo fileInfo_role0 = stratum_role.getOrCreateFileInfo("Team.java", "org/objectteams/Team.java");
    	
    	LineInfo lineInfo1_role0 = new LineInfo(OT_CONFINED_GET_TEAM_LINE, 5); // mapped line of _OT$getTeam() to synthetic line 5
    	fileInfo_role0.addLineInfo(lineInfo1_role0);
        
        LineInfo lineInfo1_role1 = new LineInfo(2,4); // default ctor at team position (2) mapped to synthetic line 4
        fileInfo_role0.addLineInfo(lineInfo1_role1);
        
        // no contribution from SuperTeam$__OT__Confined
        
        LineInfo lineInfo_role2 = new LineInfo(65533,65533);
        lineInfo_role2.setRepeatCount(2);
        
        fileInfo_role0.addLineInfo(lineInfo_role2);;
        
        TYPENAME = "__OT__Confined";
        _enclosingTypename= "SubTeam";
        List <SmapStratum>strata_role_confined = new ArrayList<SmapStratum>();
        strata_role_confined.add(stratum_role);
        
        expectedStrata.put(TYPENAME, strata_role_confined);
        
        HashMap<String, int[]> expectedMethodLineNumbers = new HashMap<String, int[]>();
        expectedMethodLineNumbers.put("copyInheritance.SubTeam$__OT__Confined._OT$getTeam()Lorg/objectteams/ITeam;", new int[]{5});

        try
        {
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{_superSuperTeam, _superTeam, _subTeam}, expectedMethodLineNumbers);
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }
    
    public void testCopyInheritanceOutOfOrderStatements() {
    	SmapStratum stratum_role = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
        
        FileInfo fileInfo0 = stratum_role.getOrCreateFileInfo("SuperTeam2.java", "copyInheritance/SuperTeam2.java");
        LineInfo lineInfo1_role0 = new LineInfo(6,4); // class position (6) mapped to synthetic line 4
        fileInfo0.addLineInfo(lineInfo1_role0);
        LineInfo lineInfo2_role0 = new LineInfo(8,5); // method (8) mapped to synthetic line 5..
        lineInfo2_role0.setRepeatCount(5);
        fileInfo0.addLineInfo(lineInfo2_role0);
        
        LineInfo lineInfo2_role1 = new LineInfo(65533,65533);
        lineInfo2_role1.setRepeatCount(2);
        
        fileInfo0.addLineInfo(lineInfo2_role1);

     
        TYPENAME = "__OT__R";
        _enclosingTypename = "SubTeam2";
        
        List <SmapStratum>strata_role = new ArrayList<SmapStratum>();
        strata_role.add(stratum_role);
        
        expectedStrata.put(TYPENAME, strata_role);
        
        HashMap<String, int[]> expectedMethodLineNumbers = new HashMap<String, int[]>();
        //expectedMethodLineNumbers.put("copyInheritance.SubTeam$__OT__Confined._OT$getTeam()Lorg/objectteams/Team;", new int[]{25});


        try
        {
        	ICompilationUnit superTeam = getCompilationUnit(
        			getTestProjectDir(),
        			"src",
        			"copyInheritance",
        	"SuperTeam2.java");
        	
        	ICompilationUnit subTeam = getCompilationUnit(
        			getTestProjectDir(),
        			"src",
        			"copyInheritance",
        	"SubTeam2.java");
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{superTeam, subTeam}, expectedMethodLineNumbers);
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
    }

}
