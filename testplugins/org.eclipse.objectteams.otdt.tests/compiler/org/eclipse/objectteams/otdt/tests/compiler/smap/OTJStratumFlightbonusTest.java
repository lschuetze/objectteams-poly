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
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.objectteams.otdt.core.compiler.ISMAPConstants;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.FileInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.LineInfo;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.SmapStratum;
import org.eclipse.objectteams.otdt.internal.core.compiler.smap.TeamSmapGenerator;

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

	// GUIConnector.FlightBonusDialog is a nested team as a role file
	public void testFlightBonusDialog_Specific() throws JavaModelException {
		ICompilationUnit roleFile = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "org.eclipse.objectteams.example.fbapplication.GUIConnector",
                "FlightBonusDialog.java");
        TYPENAME = "__OT__FlightBonusDialog";
        _enclosingTypename = "GUIConnector";

    	SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
    	FileInfo fileInfo = stratum_role1.getOrCreateFileInfo("FlightBonusDialog.java", "org/eclipse/objectteams/example/fbapplication/GUIConnector/FlightBonusDialog.java");

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

	// test a nested role within GUIConnector.FlightBonusDialog (see above)
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

	// FlightBonus is a toplevel team containing a role in a role file (Subscriber)
	public void testFlightBonus() throws JavaModelException {
		ICompilationUnit roleFile = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "org.eclipse.objectteams.example.fbapplication",
                "FlightBonus.java");
        TYPENAME = "FlightBonus";
        _enclosingTypename = null; // this signals we are not testing a role

    	SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);

    	FileInfo fileInfo2 = stratum_role1.getOrCreateFileInfo("Subscriber.java", "org/eclipse/objectteams/example/fbapplication/FlightBonus/Subscriber.java");
    	LineInfo lineInfo4 = new LineInfo(7,75);  // class header: position for liftTo method
    	LineInfo lineInfo5 = new LineInfo(12,76); // callin binding
    	
    	fileInfo2.addLineInfo(lineInfo4);
    	fileInfo2.addLineInfo(lineInfo5);
    	
        FileInfo fileInfo1 = stratum_role1.getOrCreateFileInfo("FlightBonus.java", "org/eclipse/objectteams/example/fbapplication/FlightBonus.java");
        LineInfo lineInfo1 = new LineInfo(1,1);	// all lines of class FlightBonus
        lineInfo1.setRepeatCount(74);
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

	// The role file of the above team
	public void testSubscriber_Specific() throws JavaModelException {
		ICompilationUnit roleFile = getCompilationUnit(
                getTestProjectDir(),
                "src",
                "org.eclipse.objectteams.example.fbapplication.FlightBonus",
                "Subscriber.java");
        TYPENAME = "__OT__Subscriber";
        _enclosingTypename = "FlightBonus";

    	SmapStratum stratum_role1 = new SmapStratum(ISMAPConstants.OTJ_STRATUM_NAME);
        
    	FileInfo fileInfo1 = stratum_role1.getOrCreateFileInfo("Subscriber.java", "org/eclipse/objectteams/example/fbapplication/FlightBonus/Subscriber.java");

    	// copy-inherited methods
        FileInfo fileInfo2 = stratum_role1.getOrCreateFileInfo("Bonus.java", "org/eclipse/objectteams/example/bonussystem/Bonus.java");
        
        LineInfo lineInfo4 = new LineInfo(39,21); // class header (e.g., _OT$getTeam())
        LineInfo lineInfo6 = new LineInfo(44,30); // method getCollectedCredits
        LineInfo lineInfo7 = new LineInfo(49,28); // method collectCredits
        lineInfo7.setRepeatCount(2);
        LineInfo lineInfo8 = new LineInfo(58,22); // method buy
        lineInfo8.setRepeatCount(6);

        fileInfo2.addLineInfo(lineInfo4);
        fileInfo2.addLineInfo(lineInfo6);
        fileInfo2.addLineInfo(lineInfo7);
        fileInfo2.addLineInfo(lineInfo8);

        LineInfo lineInfo1 = new LineInfo(7,7); 	// all lines of class Subscriber
        lineInfo1.setRepeatCount(14);

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

	@Override
	public void callback(CompilationUnitDeclaration cuDecl) {
		if (this._enclosingTypename != null) {
			super.callback(cuDecl);
		} else {
		    TypeDeclaration typeDecl = cuDecl.types[0];
		    
		    assertNotNull("TypeDeclaration should not be null.", typeDecl);

		    String typeName = new String(typeDecl.name);
		    List<SmapStratum> expectedStrataForType = expectedStrata.get(typeName);
		    if (expectedStrataForType == null)
		    	return;
            TeamSmapGenerator teamfileSmapGenerator = new TeamSmapGenerator(typeDecl);
            teamfileSmapGenerator.addStratum("OTJ");
            teamfileSmapGenerator.generate();
            List actualStrata = teamfileSmapGenerator.getStrata();
			assertEquals("Strata of type \"" + typeName + "\" should be equal.\n", expectedStrataForType.toString(), actualStrata.toString());
		}
	}
}
