/**
 * This file is part of "Object Teams Development Tooling"-Software.
 *
 * Copyright 2013 GK Software AG.
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
 * 	Stephan Herrmann - Initial API and implementation.
 */
package org.eclipse.objectteams.weaver.plugin;

import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.repository.RepositorySystem;
import org.eclipse.objectteams.builder.BuildTimeWeaver;
import org.eclipse.objectteams.builder.ILogger;
import org.eclipse.objectteams.builder.Requestor;

/**
 * Mojo / Maven plug-in for performing OT/J byte code weaving during build time,
 * rather than at load-time as the OTRE normally does.
 * This serves as a fallback for running OT/J programs on platforms where
 * class loading cannot be intercepted for load-time weaving.
 * <p>
 * <b>Configuration:</b> Clients using this plug-in configure the weaver
 * using the following properties:
 * <ul>
 * <li>{@link #teamClasses}
 * <li>{@link #activeTeamClasses} 
 * <li>{@link #mainClass}
 * </ul>
 */
@Mojo(name="weave",defaultPhase=LifecyclePhase.PROCESS_CLASSES)
public class WeaverMojo extends AbstractMojo
{
	
	private static final String OT_TEAMCONFIG_PROPERTY = "ot.teamconfig"; //$NON-NLS-1$
	private static final String OT_TEAMCONFIG_FILENAME = "otteamconfig.txt"; //$NON-NLS-1$

	private static final String MISSING_MAIN_CLASS_NAME_ERROR =
			"Missing main class name. A main class is mandatory when activeTeamClasses are given";


	// ======== Parameters for configuration via pom.xml: =========
	
	/** List of all team classes to weave. */
	@Parameter(required=true)
	protected String[] teamClasses;

	/**
	 * List of team classes ot instantiate and activate on program start,
	 * requires {@link #mainClass} to be set, too.
	 */
	@Parameter
	protected String[] activeTeamClasses;

	/**
	 * Main class, required if activation of teams should be woven,
	 * see {@link #activeTeamClasses}.
	 */
	@Parameter
	protected String mainClass;

    /**
     * Where to place woven class files.
     */
	@Parameter(defaultValue="target/woven-classes")
    protected File wovenClassDirectory;

	// ====== Parameters not meant for client configuration, simply access project context: =====
	
	@Parameter(defaultValue="${project}")
	private org.apache.maven.project.MavenProject mavenProject;

	@Parameter(defaultValue="${localRepository}")
	private org.apache.maven.artifact.repository.ArtifactRepository localRepository;

	// ====== Dependency injection ======

	@Component
	private RepositorySystem repoSystem;

	/**
	 * Main entry into this Mojo.
	 */
	public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if (!wovenClassDirectory.exists())
			wovenClassDirectory.mkdirs();

		try {
			ILogger logger = new ILogger() {
				@Override public void info(String msg)  { getLog().info(msg); }
				@Override public void debug(String msg) { getLog().debug(msg); }
				@Override public void error(String msg) { getLog().error(msg); }
			};
			BuildTimeWeaver weaver = new BuildTimeWeaver(logger);

			if (activeTeamClasses != null && activeTeamClasses.length != 0) {
				if (mainClass == null) {
					getLog().error(MISSING_MAIN_CLASS_NAME_ERROR);
					throw new MojoFailureException(MISSING_MAIN_CLASS_NAME_ERROR);
				}
				File configFile = new File(wovenClassDirectory, OT_TEAMCONFIG_FILENAME);
				try (FileWriter fileWriter = new FileWriter(configFile)) {
					for (String activeTeam : activeTeamClasses)
						fileWriter.append(activeTeam+'\n');
				}
				System.setProperty(OT_TEAMCONFIG_PROPERTY, configFile.getAbsolutePath());
			} else {
				String configProperty = System.getProperty(OT_TEAMCONFIG_PROPERTY);
				if (configProperty != null) {
					throw new MojoFailureException("Property "+OT_TEAMCONFIG_PROPERTY+" is set to "+configProperty+" but cannot be used via Maven");
				}
			}

			Requestor requestor = new Requestor(wovenClassDirectory.getAbsolutePath());

			ClassLoader loader= getClassLoader();

			weaver.weave(teamClasses, mainClass, loader, requestor);

		} catch (MojoFailureException e) {
			throw e;
		} catch (Throwable e) {
        	throw new MojoExecutionException("Failed to weave classes", e);
		}
    }

    private ClassLoader getClassLoader() throws MalformedURLException, DependencyResolutionRequiredException {
	    List<URL> urls = new ArrayList<URL>();
	    // collect URLs to load from:
	    // -- seems to correspond to project output folders:
	    for (Object object : mavenProject.getCompileClasspathElements()) {
			urls.add(new File((String) object).toURI().toURL());
    		getLog().debug("WeaverMojo using classpath element "+object);
		}
	    // -- reference all maven dependencies via the local repository:
	    for (Dependency dependency : mavenProject.getDependencies()) {
	    	Artifact artifact = repoSystem.createDependencyArtifact(dependency);
	    	String path = localRepository.getUrl()+localRepository.pathOf(artifact);
    		urls.add(new URL(path));
    		getLog().debug("WeaverMojo using dependency from "+path);
	    }
	    return new URLClassLoader(urls.toArray(new URL[] {}));
    }

    // ------------- currently unused: ---------------------
    
    /**
     * Recursively collect all .class files into 'result'
     * @param root directory where searching starts
     * @param currentPrefix either "" (root) or "some/prefix/", so that file names can be directly appended
     * @param result accumulated list of all filenames found
     */
    void listRecursive(File root, String currentPrefix, Set<String> result) {
        File[] list = root.listFiles();
        if (list == null) return;
        for (File f : list ) {
            if (f.isDirectory() )
				listRecursive(f, currentPrefix+f.getName()+File.separator, result);
			else if (f.getName().endsWith(".class"))
				result.add((currentPrefix+f.getName()).replace('\\', '/'));
        }
    }
}
