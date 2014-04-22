/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014, GK Software AG, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *			Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.ext;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.objectteams.otdt.internal.core.ext.OTCoreExtMessages;
import org.eclipse.objectteams.otdt.internal.core.ext.OTVariableInitializer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * This class serves the "OTRE" classpath container.
 * It also provides access to resolved paths for all OTRE-related jars.
 * @since 2.3
 */
public class OTDREContainer implements IClasspathContainer
{
	/** Name of this classpath container, which hosts the full OTRE. */
	public static final String CONTAINER_NAME     = "OTDRE"; //$NON-NLS-1$
    
    // these are served from the current plugin:
    private static IPath  OTRE_MIN_JAR_PATH;
    private static IPath  OTRE_AGENT_JAR_PATH;
    private static IPath  OTEQUINOX_AGENT_JAR_PATH;
    
    public static IPath[]  BYTECODE_LIBRARY_PATH; // will be initialized in {@link findBytecodeLib(BundleContext,boolean)}

    // details of this container: name and hosting plugin:
    static final IPath  CONTAINER_PATH = new Path(CONTAINER_NAME);
    static String OT_RUNTIME_PLUGIN = "org.eclipse.objectteams.runtime"; //$NON-NLS-1$

    // file names for the above OTRE_X_JAR_PATH constants:
    private static final String OTRE_MIN_JAR_FILENAME   = "otredyn_min.jar"; //$NON-NLS-1$
    private static final String OTRE_AGENT_JAR_FILENAME = "otredyn_agent.jar"; //$NON-NLS-1$
    private static final String OTEQUINOX_AGENT_JAR_FILENAME = "otequinoxAgent.jar"; //$NON-NLS-1$

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

    /** Answer the text that describes the OTDRE container in the UI. */
    public String getDescription()
    {
        return OTCoreExtMessages.OTDREContainer__Description;
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
     * The name of this classpath container as a path, value = "OTDRE". 
     */
    public IPath getPath()
    {
        return CONTAINER_PATH;
    }

    /**
     * Answer the path of the "otredyn_min.jar" archive, which is placed on the bootclasspath when running OT/J programs.
     * @return resolved path
     */
    public static IPath getOtreMinJarPath () {
    	if (OTRE_MIN_JAR_PATH == null)
            OTRE_MIN_JAR_PATH   = OTDTPlugin.getResolvedVariablePath(OTDTPlugin.OTDT_INSTALLDIR, "lib/"+OTRE_MIN_JAR_FILENAME); //$NON-NLS-1$
    	return OTRE_MIN_JAR_PATH;
    }
    
    /**
     * Answer the path of the "otredyn_agent.jar" archive, which is passed as a -javaagent to the JVM.
     * @return resolved path
     */
    public static IPath getOtreAgentJarPath() {
    	if (OTRE_AGENT_JAR_PATH == null)
            OTRE_AGENT_JAR_PATH = OTDTPlugin.getResolvedVariablePath(OTDTPlugin.OTDT_INSTALLDIR, "lib/"+OTRE_AGENT_JAR_FILENAME); //$NON-NLS-1$
    	return OTRE_AGENT_JAR_PATH;
    }
    
    /**
     * Answer the path of the "otequinoxAgent.jar" archive, which is passed as a -javaagent to the JVM for OT/Equinox debug launches.
     * @return resolved path
     */
    public static IPath getOtequinoxAgentJarPath() {
    	if (OTEQUINOX_AGENT_JAR_PATH == null)
    		OTEQUINOX_AGENT_JAR_PATH = OTDTPlugin.getResolvedVariablePath(OTDTPlugin.OTDT_INSTALLDIR, "lib/"+OTEQUINOX_AGENT_JAR_FILENAME); //$NON-NLS-1$
    	return OTEQUINOX_AGENT_JAR_PATH;
    }
	
	/** Fetch the location of the asm bundle into BYTECODE_LIBRARY_PATH. */
    @SuppressWarnings("deprecation") // class PackageAdmin is "deprectated"
	static void findBytecodeLib(BundleContext context) throws IOException {
		ServiceReference<org.osgi.service.packageadmin.PackageAdmin> ref =
				(ServiceReference<org.osgi.service.packageadmin.PackageAdmin>) context.getServiceReference(org.osgi.service.packageadmin.PackageAdmin.class);
		if (ref == null)
			throw new IllegalStateException("Cannot connect to PackageAdmin"); //$NON-NLS-1$
		org.osgi.service.packageadmin.PackageAdmin packageAdmin = context.getService(ref);
		BYTECODE_LIBRARY_PATH = new IPath[ASM_BUNDLE_NAMES.length];
		int i = 0;
		for (String bundleName : ASM_BUNDLE_NAMES) {
			for (Bundle bundle : packageAdmin.getBundles(bundleName, ASM_VERSION_RANGE))	
				BYTECODE_LIBRARY_PATH[i++] = new Path(FileLocator.toFileURL(bundle.getEntry("/")).getFile()); //$NON-NLS-1$
		}
		if (i != 3)
			throw new RuntimeException("bytecode libarary for OTDRE not found"); //$NON-NLS-1$
	}
}
