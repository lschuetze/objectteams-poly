/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2003, 2015 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: OTREContainer.java 23427 2010-02-03 22:23:59Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.ext;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.objectteams.otdt.internal.core.ext.OTCoreExtMessages;
import org.eclipse.objectteams.otdt.internal.core.ext.OTVariableInitializer;
import org.eclipse.objectteams.otequinox.TransformerPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * This class serves the "OTRE" classpath container.
 * It also provides access to resolved paths for all OTRE-related jars.
 * @author gis
 */
public class OTREContainer implements IClasspathContainer
{
	/** Name of this classpath container, which hosts the full OTRE. */
	public static final String OTRE_CONTAINER_NAME     = "OTRE"; //$NON-NLS-1$
    
    // these are served from the current plugin:
    private static IPath  OTRE_MIN_JAR_PATH;
    private static IPath  OTRE_AGENT_JAR_PATH;
    private static IPath  OTDRE_AGENT_JAR_PATH;
    
    private static IPath[][]  BYTECODE_WEAVER_PATHS = new IPath[WeavingScheme.values().length][]; // will be initialized in {@link findBytecodeLib(BundleContext,boolean)}

    // details of this container: name and hosting plugin:
    private static final IPath  OTRE_CONTAINER_PATH = new Path(OTRE_CONTAINER_NAME);
    static String OT_RUNTIME_PLUGIN = "org.eclipse.objectteams.runtime"; //$NON-NLS-1$

    // file names for the above OTRE_X_JAR_PATH constants:
    private static final String OTRE_MIN_JAR_FILENAME   = "otre_min.jar"; //$NON-NLS-1$
    private static final String OTRE_AGENT_JAR_FILENAME = "otre_agent.jar"; //$NON-NLS-1$
    private static final String OTDRE_AGENT_JAR_FILENAME = "otredyn_agent.jar"; //$NON-NLS-1$
    
    private static final String OTRE_PLUGIN_NAME = "org.eclipse.objectteams.otre";    
    private static final String OTDRE_PLUGIN_NAME = "org.eclipse.objectteams.otredyn";

    // data for initializing the above BYTECODE_LIBRARY_PATH:
    private static final String BCEL_BUNDLE_NAME = "org.apache.bcel"; //$NON-NLS-1$
    private static final String BCEL_VERSION_RANGE = "[5.2.0,5.3.0)"; //$NON-NLS-1$

    // data for initializing the ASM_PATH:
	private static final String[] ASM_BUNDLE_NAMES = { "org.objectweb.asm", "org.objectweb.asm.tree", "org.objectweb.asm.commons" }; //$NON-NLS-1$
	private static final String ASM_VERSION_RANGE = "[5.0.1,6.0.0)"; //$NON-NLS-1$


    private IClasspathEntry[] _cpEntries;

    /**
     * Return a an array of classpath entry representing the OTRE.
     * @return a singleton array.
     */
    public IClasspathEntry[] getClasspathEntries()
    {
        if (_cpEntries == null)
        {
            IPath fullPath = new Path(OTVariableInitializer.getInstallatedPath(OTDTPlugin.getDefault(), OT_RUNTIME_PLUGIN, "bin")); //$NON-NLS-1$
            _cpEntries = new IClasspathEntry[] {JavaCore.newLibraryEntry(fullPath, fullPath, new Path("/"))}; //$NON-NLS-1$
        }
        
        return _cpEntries;
    }

    /** Answer the text that describes the OTRE container in the UI. */
    public String getDescription()
    {
        return OTCoreExtMessages.OTREContainer__Description;
    }

    /**
     * Answer the kind of this classpath container, value = {@link IClasspathContainer#K_APPLICATION}.
     */
    public int getKind()
    {
    	// don't mark as K_SYSTEM or K_SYSTEM_DEFAULT, which would prevent jdt.debug from adding this to the runtime classpath. 
    	return IClasspathContainer.K_APPLICATION;
    }

    /**
     * The name of this classpath container as a path, value = "OTRE". 
     */
    public IPath getPath()
    {
        return OTRE_CONTAINER_PATH;
    }

    /**
     * Answer the path of the "otre_min.jar" archive, which is placed on the bootclasspath when running OT/J programs.
     * @return resolved path
     */
    public static IPath getOtreMinJarPath () {
    	if (OTRE_MIN_JAR_PATH == null)
            OTRE_MIN_JAR_PATH   = OTDTPlugin.getResolvedVariablePath(OTDTPlugin.OTDT_INSTALLDIR, "lib/"+OTRE_MIN_JAR_FILENAME); //$NON-NLS-1$
    	return OTRE_MIN_JAR_PATH;
    }
    
    /**
     * Answer the path of the "otre_agent.jar" archive, which is passed as a -javaagent to the JVM.
     * @return resolved path
     */
    public static IPath getOtreAgentJarPath(WeavingScheme scheme) {
    	switch (scheme) {
    	case OTRE:
    		if (OTRE_AGENT_JAR_PATH == null)
    			OTRE_AGENT_JAR_PATH = OTDTPlugin.getResolvedVariablePath(OTDTPlugin.OTDT_INSTALLDIR, "lib/"+OTRE_AGENT_JAR_FILENAME); //$NON-NLS-1$
    		return OTRE_AGENT_JAR_PATH;
    	case OTDRE:
    		if (OTDRE_AGENT_JAR_PATH == null)
    			OTDRE_AGENT_JAR_PATH = OTDTPlugin.getResolvedVariablePath(OTDTPlugin.OTDT_INSTALLDIR, "lib/"+OTDRE_AGENT_JAR_FILENAME); //$NON-NLS-1$
    		return OTDRE_AGENT_JAR_PATH;
    	default:
    		throw new IncompatibleClassChangeError("Unexpected enum constant "+scheme);
    	}
    }
    
    /**
     * Answer the path of the "otequinoxAgent.jar" archive, which is passed as a -javaagent to the JVM for OT/Equinox debug launches.
     * @return resolved path
     */
    public static IPath getOtequinoxAgentJarPath() {
    	return new Path(TransformerPlugin.getOtequinoxAgentPath());
    }
    
	/**
	 * Adds the ObjectTeams classes to the given JavaProject's classpath,
	 * and ensures the Java compliance is >= 1.5
	 * Handles both variants, OTRE and OTDRE.
	 */
	public static void initializeOTJProject(IProject project) throws CoreException
	{		
		if (project == null)
		{
			return; 
		}
	
		if (project.hasNature(JavaCore.NATURE_ID))
		{
			IJavaProject 	  javaPrj   = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
			IClasspathEntry[] classpath = javaPrj.getRawClasspath();
			
			if (!isOTREAlreadyInClasspath(classpath))
			{
	            addOTREToClasspath(javaPrj, classpath);
			}
			String javaVersion = javaPrj.getOption(JavaCore.COMPILER_COMPLIANCE, true);
			if (javaVersion.compareTo(JavaCore.VERSION_1_5)<0) {
				javaPrj.setOption(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
				javaPrj.setOption(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
				javaPrj.setOption(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
				javaPrj.setOption(JavaCore.COMPILER_PB_ASSERT_IDENTIFIER, JavaCore.ERROR);
				javaPrj.setOption(JavaCore.COMPILER_PB_ENUM_IDENTIFIER, JavaCore.ERROR);
				javaPrj.setOption(JavaCore.COMPILER_CODEGEN_INLINE_JSR_BYTECODE, JavaCore.ENABLED);
			}
		}
	}

	private static boolean isOTREAlreadyInClasspath(IClasspathEntry[] classpath) {
	    for (int idx = 0; classpath != null && idx < classpath.length; idx++) {
	        IClasspathEntry entry = classpath[idx];
	        if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER &&
	        		(entry.getPath().equals(OTRE_CONTAINER_PATH)))
				return true;
	    }
	    
	    return false;
	}

	public static IPath getContainerPath() {
		return OTRE_CONTAINER_PATH;
	}

	public static IPath[] getWeaverPaths(WeavingScheme scheme) {
		return BYTECODE_WEAVER_PATHS[scheme.ordinal()];
	}

	/**
	 * Add the object teams foundation classes to the classpath.
	 */
	private static void addOTREToClasspath(IJavaProject javaPrj, IClasspathEntry[] classpath)
			throws JavaModelException, CoreException
	{
		IClasspathEntry[] newClasspath = new IClasspathEntry[classpath.length + 1];
	
		System.arraycopy( classpath, 0, newClasspath, 0, classpath.length );
	
		newClasspath[classpath.length] = JavaCore.newContainerEntry(OTRE_CONTAINER_PATH, false);
	    
		if (newClasspath[classpath.length] != null)
			javaPrj.setRawClasspath( newClasspath, null );
		else
			throw new CoreException( new Status(
										IStatus.ERROR, 
										OTDTPlugin.PLUGIN_ID, 
										IStatus.OK,
										OTCoreExtMessages.OTREContainer_otre_not_found, 
										null) );
	}
	
	/** Fetch the location of the otre, otdre, bcel and asm bundles into {@link #BYTECODE_WEAVER_PATHS}. */
	@SuppressWarnings("deprecation") // class PackageAdmin is "deprecated"
	static void findBytecodeLibs(BundleContext context) throws IOException {
		ServiceReference<org.osgi.service.packageadmin.PackageAdmin> ref =
				(ServiceReference<org.osgi.service.packageadmin.PackageAdmin>) context.getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class);
		if (ref == null)
			throw new IllegalStateException("Cannot connect to PackageAdmin"); //$NON-NLS-1$
		org.osgi.service.packageadmin.PackageAdmin packageAdmin = context.getService(ref);
		BCEL: {
			String bundleName = BCEL_BUNDLE_NAME;
			String bundleVersionRange = BCEL_VERSION_RANGE;
			for (Bundle bundle : packageAdmin.getBundles(bundleName, bundleVersionRange)) {			
				BYTECODE_WEAVER_PATHS[WeavingScheme.OTRE.ordinal()] =
						new IPath[] {
							new Path(FileLocator.toFileURL(bundle.getEntry("/")).getFile()), //$NON-NLS-1$
							new Path(OTVariableInitializer.getInstallatedPath(OTDTPlugin.getDefault(), OTRE_PLUGIN_NAME, "bin")) //$NON-NLS-1$
						};
				break BCEL;
			}
			throw new RuntimeException("bytecode libarary for OTRE not found"); //$NON-NLS-1$
		}
		ASM : {
			int asm = WeavingScheme.OTDRE.ordinal();
			BYTECODE_WEAVER_PATHS[asm] = new IPath[ASM_BUNDLE_NAMES.length+1];
			int i = 0;
			BYTECODE_WEAVER_PATHS[asm][i++] = new Path(OTVariableInitializer.getInstallatedPath(OTDTPlugin.getDefault(), OTDRE_PLUGIN_NAME, "bin")); //$NON-NLS-1$
			for (String bundleName : ASM_BUNDLE_NAMES) {
				for (Bundle bundle : packageAdmin.getBundles(bundleName, ASM_VERSION_RANGE)) {
					URL bundleEntry = bundle.getEntry("bin"); // source project?
					if (bundleEntry == null)
						bundleEntry = bundle.getEntry("/"); // binary bundle
					BYTECODE_WEAVER_PATHS[asm][i++] = new Path(FileLocator.toFileURL(bundleEntry).getFile()); //$NON-NLS-1$
					break;
				}
			}
			if (i == 4)
				break ASM;
			throw new RuntimeException("bytecode libarary for OTDRE not found"); //$NON-NLS-1$
		}
	}
	
}
