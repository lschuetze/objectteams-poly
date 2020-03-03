/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011, 2014 GK Software AG
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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.test.builder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import junit.framework.Test;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.tests.builder.Problem;
import org.eclipse.jdt.core.tests.util.Util;
import org.eclipse.objectteams.otdt.tests.ClasspathUtil;

/**
 * Tests for  Bug 314610 - Produce warning when two roles capture the same method at the same time
 * and related issues.
 */
public class MultipleCallinsWarningTest extends OTBuilderTests {

	private char NL = '\n';
	private IPath root;
	private IPath projectPath;
	
	public MultipleCallinsWarningTest(String testName) {
		super(testName);
	}
	

	public static Test suite() {
		return buildTestSuite(MultipleCallinsWarningTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.projectPath = env.addProject("MultipleCallinsProject", "1.5"); 
		env.addExternalJars(projectPath, Util.getJavaClassLibs());
		env.addExternalJar(projectPath, ClasspathUtil.getOTREPath(this.weavingScheme));
		
		// remove old package fragment root so that names don't collide
		env.removePackageFragmentRoot(projectPath, "");
		
		this.root = env.addPackageFragmentRoot(projectPath, "src");
		env.setOutputFolder(projectPath, "bin");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.root = null;
		this.projectPath = null;
	}
	
	public Problem[] getProblemsFor(IJavaElement element, String additionalMarkerType){
		IResource resource= env.getWorkspace().getRoot();
		try {
			ArrayList<Problem> problems = new ArrayList<Problem>();
			IMarker[] markers = resource.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
			for (int i = 0; i < markers.length; i++)
				if (JavaCore.isReferencedBy(element, markers[i]))
					problems.add(new Problem(markers[i]));

			markers = resource.findMarkers(IJavaModelMarker.BUILDPATH_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
			for (int i = 0; i < markers.length; i++)
				if (JavaCore.isReferencedBy(element, markers[i]))
					problems.add(new Problem(markers[i]));

			markers = resource.findMarkers(IJavaModelMarker.TASK_MARKER, true, IResource.DEPTH_INFINITE);
			for (int i = 0; i < markers.length; i++)
				if (JavaCore.isReferencedBy(element, markers[i]))
					problems.add(new Problem(markers[i]));

			if (additionalMarkerType != null) {
				markers = resource.findMarkers(additionalMarkerType, true, IResource.DEPTH_INFINITE);
				for (int i = 0; i < markers.length; i++)
					if (JavaCore.isReferencedBy(element, markers[i]))
						problems.add(new Problem(markers[i]));
			}

			Problem[] result = new Problem[problems.size()];
			problems.toArray(result);
			Arrays.sort(result, new Comparator<Problem>() {
				public int compare(Problem o1, Problem o2) {
					return o1.toString().compareTo(o2.toString());
				}
			});
			return result;
		} catch(CoreException e){
			// ignore
		}
		return new Problem[0];
	}

	public void testMultipleCallinsDifferentTeams1() throws JavaModelException 
	{
		System.out.println("***** testMultipleCallinsDifferentTeams1() *****");

		IPath pathToBase = env.addClass(this.root, "p", "MyBase",
	    		   "package p;" +
	    	  NL + "public class MyBase " +
			  NL + "{ " +
			  NL + "    public void baseMethod(){}" +
			  NL + "} ");
	      
	    env.addClass(this.root, "p1", "MyTeam1",
	    		   "package p1;" +
	          NL + "import base p.MyBase;\n" +
	    	  NL + "public team class MyTeam1 " +
			  NL + "{ " +	
			  NL + "	protected class MyRole playedBy MyBase " +
			  NL + "    { " +	
			  NL + "	    public void role1Method() {}" +
			  NL + "        role1Method <- after baseMethod; " +
			  NL + "    } "+
			  NL + "}");
		
	    env.addClass(this.root, "p2", "MyTeam2",
	    		   "package p2;" +
	          NL + "import base p.MyBase;\n" +
	    	  NL + "public team class MyTeam2 " +
			  NL + "{ " +	
			  NL + "	protected class MyRole playedBy MyBase " +
			  NL + "    { " +	
			  NL + "	    public void role2Method() {}" +
			  NL + "        role2Method <- after baseMethod; " +
			  NL + "    } "+
			  NL + "}");
		
		fullBuild(projectPath);
	    
		expectingProblemsFor(pathToBase);
		Problem expectedProblem = new Problem("", 
											  "Multiple callin bindings are affecting method baseMethod() (perform full build to recompute).", 
											  pathToBase, 51, 61, -1/*category*/, IMarker.SEVERITY_WARNING);
		expectingSpecificProblemFor(pathToBase, 
									expectedProblem);
	}
	
	// Bug 355321 - Avoid warning for multiple callins from the same team
	public void testMultipleCallinsDifferentTeams2() throws JavaModelException 
	{
		System.out.println("***** testMultipleCallinsDifferentTeams2() *****");

	    env.addClass(this.root, "p1", "MyTeam1",
	    		   "package p1;" +
	          NL + "import base java.util.ArrayList;\n" +
	    	  NL + "public team class MyTeam1 " +
			  NL + "{ " +	
			  NL + "	protected class MyRole playedBy ArrayList " +
			  NL + "    { " +	
			  NL + "	    public void role1Method() {}" +
			  NL + "        role1Method <- after size; " +
			  NL + "    } "+
			  NL + "}");
		
	    env.addClass(this.root, "p2", "MyTeam2",
	    		   "package p2;" +
	          NL + "import base java.util.ArrayList;\n" +
	    	  NL + "public team class MyTeam2 " +
			  NL + "{ " +	
			  NL + "	protected class MyRole playedBy ArrayList " +
			  NL + "    { " +	
			  NL + "	    public void role2Method() {}" +
			  NL + "        role2Method <- after size; " +
			  NL + "    } "+
			  NL + "}");
		
		fullBuild(projectPath);

		String expectedWarningMessage = "Multiple callin bindings are affecting method size() (perform full build to recompute).";

		IJavaElement baseElement = env.getJavaProject("MultipleCallinsProject").findElement(new Path("java/util/ArrayList.class")); 
		Problem[] problems = getProblemsFor(baseElement, null); // IMarkableJavaElement.GLOBAL_PROBLEM_ID is subtype of JAVA_MODEL_PROBLEM_MARKER
		
		assertEquals("unexpected number of problems", 1, problems.length);

		assertEquals("wrong problem message", expectedWarningMessage, problems[0].getMessage());
		assertEquals("wrong problem severity", IMarker.SEVERITY_WARNING, problems[0].getSeverity());
	}

	public void testMultipleCallinsSameTeam() throws JavaModelException 
	{
		System.out.println("***** testMultipleCallinsSameTeam() *****");

		IPath pathToBase = env.addClass(root, "p", "MyBase",
	    		   "package p;" +
	    	  NL + "public class MyBase " +
			  NL + "{ " +
			  NL + "    public void baseMethod(){}" +
			  NL + "} ");
	      
	    env.addClass(root, "p1", "MyTeam1",
	    		   "package p1;" +
	          NL + "import base p.MyBase;\n" +
	    	  NL + "public team class MyTeam1 " +
			  NL + "{ " +
			  NL + "    precedence MyRole1, MyRole2;" +
			  NL + "	protected class MyRole1 playedBy MyBase " +
			  NL + "    { " +	
			  NL + "	    public void role1Method() {}" +
			  NL + "        role1Method <- after baseMethod; " +
			  NL + "    } "+
			  NL + "	protected class MyRole2 playedBy MyBase " +
			  NL + "    { " +	
			  NL + "	    public void role2Method() {}" +
			  NL + "        role2Method <- after baseMethod; " +
			  NL + "    } "+
			  NL + "}");
		
		fullBuild(projectPath);
	    
		expectingNoProblemsFor(pathToBase);
	}
}
