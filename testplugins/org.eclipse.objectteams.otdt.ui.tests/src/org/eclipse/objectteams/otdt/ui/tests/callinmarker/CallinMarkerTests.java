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
package org.eclipse.objectteams.otdt.ui.tests.callinmarker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.internal.runtime.RuntimeLog;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.objectteams.otdt.core.ext.MarkableFactory;
import org.eclipse.objectteams.otdt.internal.ui.callinmarkers.AnnotationHelper;
import org.eclipse.objectteams.otdt.internal.ui.callinmarkers.CallinMarker;
import org.eclipse.objectteams.otdt.internal.ui.callinmarkers.CallinMarkerCreator2;
import org.eclipse.objectteams.otdt.internal.ui.callinmarkers.CallinMarkerJob;
import org.eclipse.objectteams.otdt.ui.tests.FileBasedUITest;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import junit.framework.Test;

public class CallinMarkerTests extends FileBasedUITest
{
    private static String SRC_FOLDER = "src";
    private IType _baseType;
    private IResource _baseResource;
    private MyCallinMarkerCreator _creator;
    
    private class MyCallinMarkerCreator extends CallinMarkerCreator2
    {
        private boolean _finished = false;

        public boolean isFinished()
        {
            return _finished;
        }
        
		public void reset() {
			this._finished = false;
		}
        public void initialize(IEditorPart targetEditor)
        {
        	// simulate initialization as performed by CallinMarkerCreator.activeJavaEditorChanged()
	        if (targetEditor != null) {
	            this.fActiveEditor = targetEditor;
	    		this.annotationHelper = new AnnotationHelper(targetEditor, targetEditor.getEditorInput());
	        }
        }
        
        protected void schedule(final CallinMarkerJob job,
                IStatusLineManager statusLine)
        {
            job.addJobChangeListener(new JobChangeAdapter() {
                public void done(IJobChangeEvent event)
                {
                    _finished = true;
                }
            });

            super.schedule(job, statusLine);
        }

		public boolean hasJob() {
			return this._currentJob != null;
		}
    }
    
    public CallinMarkerTests(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        if (true)
        {
            return new Suite(CallinMarkerTests.class);
        }
        @SuppressWarnings("unused") // dead due to above 'if (true)'
		junit.framework.TestSuite suite = new Suite(CallinMarkerTests.class.getName());
        return suite;
    }
    
    public void setUpSuite() throws Exception
    {
    	enableUiMonitoring(false);
        setTestProjectDir("CallinMarker");
        
        super.setUpSuite();
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        
        _creator = new MyCallinMarkerCreator();
        _creator.setEnabled(true);
        System.out.println("Running test: "+this.getName());
    }
    @Override
    protected void tearDown() throws Exception {
    	super.tearDown();
    	if (this._creator != null) {
    		this._creator.setEnabled(false);
    		this._creator = null;
    	}
    	_baseType = null;
    	_baseResource = null;
    }

    @Override
    public void tearDownSuite() throws Exception {
    	enableUiMonitoring(true);
    	super.tearDownSuite();
    }

	private void enableUiMonitoring(boolean enable) {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode("org.eclipse.ui.monitoring");
    	if (preferences != null) {
    		preferences.putBoolean("monitoring_enabled", enable);
    		Display display = org.eclipse.swt.widgets.Display.getDefault();
    		long start = System.currentTimeMillis();
			while (display.readAndDispatch()) {
				if (System.currentTimeMillis() > start + 5000) {
					System.err.println("display did not get idle in 5 sec.");
					break;
				}
			}
    	}
	}

    class MyLogListener implements ILogListener {
		List<IStatus> status = new ArrayList<IStatus>();
		public void logging(IStatus status, String plugin) {
			if (status.getSeverity() == IStatus.ERROR) {
				if (status.isMultiStatus())
					this.status.add(new Status(status.getSeverity(), plugin, status.getCode(), status.getMessage(), status.getException()));
				else
					this.status.add(status);
			}
			for (IStatus sub : status.getChildren())
				logging(sub, plugin);
		}
	}

    void createNonJavaPrj(String projectName) throws IOException, CoreException {
		// copy files in project from source workspace to target workspace
		String sourceWorkspacePath = getSourceWorkspacePath();
		String targetWorkspacePath = getWorkspaceRoot().getLocation().toFile().getCanonicalPath();
		copyDirectory(new File(sourceWorkspacePath, projectName), new File(targetWorkspacePath, projectName));

		// create project
		final IProject project = getWorkspaceRoot().getProject(projectName);
		IWorkspaceRunnable populate = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				project.create(null);
				project.open(null);
			}
		};
		getWorkspace().run(populate, null);    	
    }
    
    private IType getJavaType(String projectName, String srcFolderName, String pkgName, String typeName) throws JavaModelException
    {
        ICompilationUnit typeUnit = getCompilationUnit(
                projectName,
                srcFolderName,
                pkgName,
                typeName +".java");
        IType typeJavaElem = typeUnit.getType(typeName);
       
        if ((typeJavaElem != null) && (typeJavaElem.exists()))
        {
            return typeJavaElem;
        }
        return null;
    }    

    private void synchronousCreateMarkers(IResource resource) throws PartInitException, JavaModelException
    {
    	_creator.reset();
    	// this already triggers activeJavaEditorChanged, if project is a java project:
    	IJavaElement element = (_baseType != null)
    		? _baseType.getCompilationUnit()
    		: JavaCore.create(resource);

		IEditorPart activeEditor = JavaUI.openInEditor(element);
    	if (!this._creator.isFinished() && !this._creator.hasJob()) {
    		// explicitly trigger in non-java projects
    		 _creator.initialize(activeEditor);
    		_creator.updateCallinMarkers(MarkableFactory.createMarkable(resource), null);
    	}
        try {
	        while (!_creator.isFinished())
	        {
	            if (PlatformUI.getWorkbench().getDisplay().readAndDispatch())
	                Thread.sleep(20);
	        }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
            fail("Interrupted while waiting for CallinMarkerCreator");
        }
    }
    
    // The markers appear in undefined order, so we need to look them up in a Set
    private void assertMarkers(Set<String> expectedMarkers, IMarker[] markers)
    {
        assertNotNull(markers);
        assertNotNull(expectedMarkers);
        
        assertEquals("Wrong number of markers", expectedMarkers.size(), markers.length);
        
        try {
	        for (int i = 0; i < markers.length; i++)
	        {
	            assertNotNull(markers[i]);
	            String methodId = (String) markers[i].getAttribute(CallinMarker.ATTR_BASE_ELEMENT);
	            assertNotNull("CallinMarker without methodId attribute", methodId);
	            
	            boolean isExpected = expectedMarkers.contains(methodId);
	            assertTrue("Unexpected marker found for method id: " + methodId, isExpected);
	        }
        }
        catch (CoreException ex) {
            ex.printStackTrace();
            fail("CoreException while trying to get marker attribute");
        }
    }

    private IMarker[] getCallinMarkers(IResource resource)
    {
        try
        {
            return resource.findMarkers(CallinMarker.CALLIN_ID, true, IResource.DEPTH_INFINITE);
        }
        catch (CoreException ex)
        {
            ex.printStackTrace();
            fail(ex.getMessage());
            return null; // not reached anyway
        }
    }

    public void testMarkers_1() throws JavaModelException, PartInitException
    {
        _baseType = getJavaType(getTestProjectDir(), SRC_FOLDER, "foo", "MyBase");
        _baseResource = _baseType.getResource();

        assertNotNull(_baseResource);
        
        synchronousCreateMarkers(_baseResource);

        Set<String> expectedMarkers = new HashSet<String>();
        expectedMarkers.add("=CallinMarker/src<foo{MyBase.java[MyBase~baseMethod");
        expectedMarkers.add("=CallinMarker/src<foo{MyBase.java[MyBase~bm2");
        expectedMarkers.add("=CallinMarker/src<foo{MyBase.java[MyBase~bm3");
        expectedMarkers.add("=CallinMarker/src<foo{MyBase.java[MyBase~bm4");
        
        IMarker[] markers = getCallinMarkers(_baseResource);
        assertMarkers(expectedMarkers, markers);
    }
    
    /** compute marker although one baseclass is missing. */
    public void testMarkers_2() throws JavaModelException, PartInitException
    {
        _baseType = getJavaType(getTestProjectDir(), SRC_FOLDER, "bar", "BaseTeam");
        _baseResource = _baseType.getResource();

        assertNotNull(_baseResource);
        
        synchronousCreateMarkers(_baseResource);

        Set<String> expectedMarkers = new HashSet<String>();
        expectedMarkers.add("=CallinMarker/src<bar{BaseTeam.java[BaseTeam~murx");
        
        IMarker[] markers = getCallinMarkers(_baseResource);
        assertMarkers(expectedMarkers, markers);
    }
    /** Base classes have a member-super cycle (OK since they're static members). */
    public void testMarkers_3() throws JavaModelException, PartInitException
    {
        _baseType = getJavaType(getTestProjectDir(), SRC_FOLDER, "cycle", "Base1");
        _baseResource = _baseType.getResource();

        assertNotNull(_baseResource);
        
        synchronousCreateMarkers(_baseResource);

        Set<String> expectedMarkers = new HashSet<String>();
        expectedMarkers.add("=CallinMarker/src<cycle{Base1.java[Base1[Inner~foo");
        
        IMarker[] markers = getCallinMarkers(_baseResource);
        assertMarkers(expectedMarkers, markers);
    }
    
    /** Cycle a la https://bugs.eclipse.org/303474 and callin-to-callin */
    public void testMarkers_4() throws JavaModelException, PartInitException
    {
        _baseType = getJavaType(getTestProjectDir(), SRC_FOLDER, "cycle2", "B");
        _baseResource = _baseType.getResource();

        assertNotNull(_baseResource);
        
        synchronousCreateMarkers(_baseResource);

        Set<String> expectedMarkers = new HashSet<String>();
        expectedMarkers.add("=CallinMarker/src<cycle2{B.java[B[R~run");
        
        IMarker[] markers = getCallinMarkers(_baseResource);
        assertMarkers(expectedMarkers, markers);
    }
    
    // see http://trac.objectteams.org/ot/ticket/188
    public void testMarkers_NonJavaPrj1() throws CoreException, IOException, InterruptedException
    {    	
    	createNonJavaPrj("NonJavaPrj");
    	MyLogListener myLogListener = new MyLogListener();
    	// can't use startLogListening() because we need to listen to RuntimeLog.
		RuntimeLog.addLogListener(myLogListener);
		
    	try {
	        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	        IResource resource = workspaceRoot.findMember(new Path("/NonJavaPrj/folder/File.java"));
	        
	        synchronousCreateMarkers(resource);
	        
	        // wait for logging to occur after job is done:
	        while(this._creator.isCreatingMarkersFor(resource))
	        	Thread.sleep(100);
	        
	        if (!myLogListener.status.isEmpty())
	        	fail("Unexpected Log: "+myLogListener.status.get(0));
	
	        IMarker[] markers = getCallinMarkers(resource);
	        assertEquals("Should have no markers", markers.length, 0);
    	} finally {
    		RuntimeLog.removeLogListener(myLogListener);
    		deleteProject("NonJavaPrj");
    	}
    }
    
    // see http://trac.objectteams.org/ot/ticket/188
    public void testMarkers_NonJavaPrj2() throws CoreException, IOException, InterruptedException
    {
    	createNonJavaPrj("NonJavaPrj");
    	MyLogListener myLogListener = new MyLogListener();
    	// can't use startLogListening() because we need to listen to RuntimeLog.
		RuntimeLog.addLogListener(myLogListener);
		
    	try {
	        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	        IResource resource = workspaceRoot.findMember(new Path("/NonJavaPrj/folder/File.java"));
	        
	        // cu pretends to exist when previously opened in a java editor
	        JavaUI.openInEditor(JavaCore.create(resource));
	        
	        synchronousCreateMarkers(resource);
	        
	        // wait for logging to occur after job is done:
	        while(this._creator.isCreatingMarkersFor(resource))
	        	Thread.sleep(100);
	        
	        if (!myLogListener.status.isEmpty())
	        	fail("Unexpected Log: "+myLogListener.status.get(0));
	
	        IMarker[] markers = getCallinMarkers(resource);
	        assertEquals("Should have no markers", markers.length, 0);
    	} finally {
    		RuntimeLog.removeLogListener(myLogListener);
    		deleteProject("NonJavaPrj");
    	}
    }
    
    // see http://trac.objectteams.org/ot/ticket/188
    public void testMarkers_NonJavaPrj3() throws CoreException, IOException, InterruptedException
    {
    	createNonJavaPrj("NonJavaPrj");
    	MyLogListener myLogListener = new MyLogListener();
    	// can't use startLogListening() because we need to listen to RuntimeLog.
		RuntimeLog.addLogListener(myLogListener);
		
    	try {
	        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	        // use classfile:
	        IResource resource = workspaceRoot.findMember(new Path("/NonJavaPrj/bin/folder/File.class"));
	        
	        // cu pretends to exist when previously opened in a java editor
	        JavaUI.openInEditor(JavaCore.create(resource));
	        
	        synchronousCreateMarkers(resource);
	        
	        // wait for logging to occur after job is done:
	        while(this._creator.isCreatingMarkersFor(resource))
	        	Thread.sleep(100);
	        
	        assertEquals("Unexpeted number of log entries", 1, myLogListener.status.size());
	        assertEquals("Unexpected Log[0]", 
	        			 "Status ERROR: org.eclipse.ui code=4 Unable to create part " +
	        			 "org.eclipse.ui.PartInitException: The class file is not on the classpath",
	        			 myLogListener.status.get(0).toString());
	
	        IMarker[] markers = getCallinMarkers(resource);
	        assertEquals("Should have no markers", markers.length, 0);
    	} finally {
    		RuntimeLog.removeLogListener(myLogListener);
    		deleteProject("NonJavaPrj");
    	}
    }
}
