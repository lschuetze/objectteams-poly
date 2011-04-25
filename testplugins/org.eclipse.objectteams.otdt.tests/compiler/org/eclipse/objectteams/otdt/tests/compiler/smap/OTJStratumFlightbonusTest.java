/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.tests.compiler.smap;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.objectteams.otdt.core.compiler.ISMAPConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.FileInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.LineInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.SmapStratum;

/**
 * Using the OTSample-FlightBonus for testing the SMAP generated in complex code
 * involving team nesting, role files and team inheritance.
 */
public class OTJStratumFlightbonusTest extends AbstractSourceMapGeneratorTest {

	public OTJStratumFlightbonusTest(String testName) {
		super(testName);
	}

    public static Test suite()
    {
        return new Suite(OTJStratumFlightbonusTest.class);
    }

	@Override
	protected IJavaProject setUpJavaProject(String projectName)
			throws CoreException, IOException 
	{
		IJavaProject javaProject = super.setUpJavaProject(projectName);
		addLibraryEntry(javaProject, getLibPath(), true);
		return javaProject;
	}

	protected String getLibPath() throws IOException {
		String targetWorkspacePath = getWorkspaceRoot().getLocation().toFile().getCanonicalPath();
		String absProjectPath = targetWorkspacePath + '/' + getTestProjectDir() ;
		return absProjectPath+"/lib/booking.jar";
	}
	
	@Override
	protected String[] getDefaultClassPaths() throws IOException {
		String[] defaultClassPaths = super.getDefaultClassPaths();
		int len = defaultClassPaths.length;
		System.arraycopy(defaultClassPaths, 0, 
						 defaultClassPaths = new String[len+1], 1, 
						 len);
		defaultClassPaths[0] = getLibPath();
		return defaultClassPaths;
	}

	// see also AbstractOTJLDTest
	protected String getTestResourcePath(String filename) {
        try
        {
            URL platformURL = Platform
				                .getBundle("org.eclipse.objectteams.otdt.tests")
				                .getEntry("/testresources/"+filename);
            return new File(FileLocator.toFileURL(platformURL).getFile())
                .getAbsolutePath();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return null;
	}

	public void testFlightBonusDialog_Specific() throws JavaModelException {
		ICompilationUnit roleFile = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "org.eclipse.objectteams.example.fbapplication.GUIConnector",
                "FlightBonusDialog.java");
        TYPENAME = "__OT__FlightBonusDialog";
        _enclosingTypename = "GUIConnector";

    	SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
    	FileInfo fileInfoTSuper = stratum_role1.getOrCreateFileInfo("FlightBonusDialog.java", "org/eclipse/objectteams/example/fbapplication/BonusGUI/FlightBonusDialog.java");
    	LineInfo lineInfo4 = new LineInfo(16, 41); // accessor for field "message"
    	LineInfo lineInfo5 = new LineInfo(11, 42); // class header
    	LineInfo lineInfo6 = new LineInfo(22, 43); // constructor
    	lineInfo6.setRepeatCount(5);
    	LineInfo lineInfo7 = new LineInfo(33, 48); // method "initializeMessage"
    	lineInfo7.setRepeatCount(2);
    	
    	fileInfoTSuper.addLineInfo(lineInfo4);
    	fileInfoTSuper.addLineInfo(lineInfo5);
    	fileInfoTSuper.addLineInfo(lineInfo6);
    	fileInfoTSuper.addLineInfo(lineInfo7);
    	
        FileInfo fileInfo = stratum_role1.getOrCreateFileInfo("FlightBonusDialog.java", "org/eclipse/objectteams/example/fbapplication/GUIConnector/FlightBonusDialog.java");
        LineInfo lineInfo1 = new LineInfo(15,15);
        lineInfo1.setRepeatCount(26);
        LineInfo lineInfo2 = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER,ISMAPConstants.STEP_INTO_LINENUMBER);
        LineInfo lineInfo3 = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER,ISMAPConstants.STEP_OVER_LINENUMBER);

        fileInfo.addLineInfo(lineInfo1);
        fileInfo.addLineInfo(lineInfo2);
        fileInfo.addLineInfo(lineInfo3);
        
        
        stratum_role1.optimize();
        
        List<SmapStratum> strata_role1 = new ArrayList<SmapStratum>();
        strata_role1.add(stratum_role1);
        
        expectedStrata.put(TYPENAME, strata_role1);

        try
        {
        	// use this if you want to inspect the generated classes:
        	// parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{roleFile}, null, null, "/tmp/bin");
        	parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{roleFile});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
	}

	public void testCollector_Specific() throws JavaModelException {
		ICompilationUnit roleFile = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "org.eclipse.objectteams.example.fbapplication.GUIConnector",
                "FlightBonusDialog.java");
        TYPENAME = "__OT__Collector";
        _enclosingTypename = "__OT__FlightBonusDialog";

    	SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
        FileInfo fileInfo1 = stratum_role1.getOrCreateFileInfo("FlightBonusDialog.java", "org/eclipse/objectteams/example/fbapplication/GUIConnector/FlightBonusDialog.java");
        LineInfo lineInfo1 = new LineInfo(1,1);
        lineInfo1.setRepeatCount(40);
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
            parseAndCompile(new org.eclipse.jdt.core.ICompilationUnit[]{roleFile});
        }
        catch (JavaModelException e)
        {
            fail(e.getMessage());
        }
	}
}
