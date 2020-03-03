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
package org.eclipse.objectteams.builder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.generic.ClassGen;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.objectteams.otre.ObjectTeamsTransformation;
import org.eclipse.objectteams.otre.RepositoryAccess;
import org.eclipse.objectteams.otre.jplis.JPLISEnhancer;
import org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer;

/**
 * Bridge between a build tool like Maven and the Object Teams weaver/transformer.
 * This class invokes the transformer in two phases:
 * <ul>
 * <li>First all specified team classes (and referenced teams/roles) are <em>scanned</em>
 *     in order to record all weaving instructions from byte code attributes</li>
 * <li>Only in a second phase we perform actual transformation, at which point we
 *     are sure, we don't miss any weaving instructions.</li>
 * </ul>
 */
public class BuildTimeWeaver {

	private ILogger logger;

	private Set<String> scannedClasses;	// to avoid double scanning
	private Set<String> wovenClasses;	// to avoid double weaving
	
	private int numMissing = 0;

	/**
	 * Create a build time weaver wired to the given logger.
	 * @param logger
	 */
	public BuildTimeWeaver(ILogger logger) {
		this.logger = logger;
	}

	/**
	 * Perform weaving for the given classes, reading classfiles using 'loader' and passing woven class bytes to the 'requestor'
	 * @param teamClasses known team classes, if not listed here, bindings are not guaranteed to be applied correctly.
	 * @param mainClass if a team config file is specified in property ot.teamconfig, then this class will get the activation code injected.
	 * @param loader use this loader for reading class files
	 * @param requestor invoke this for every class where transformation actually changed the class bytes.
	 * @throws IOException
	 * @throws IllegalClassFormatException
	 * @throws MojoFailureException if some class files could not be read.
	 */
	public void weave(String[] teamClasses, String mainClass, ClassLoader loader, Requestor requestor)
			throws IOException, IllegalClassFormatException, MojoFailureException 
	{
		// prepare and instantiate the transformer:
		System.setProperty("ot.equinox", "true"); // to prevent transitive class loading in JPLISEnhancer, which would NPE
		RepositoryAccess.setClassLoader(loader);
		ObjectTeamsTransformer transformer = new ObjectTeamsTransformer();

		// prepare sets of classes
		//  - translate team class names into file names:
        List<String> teamFileNames = new ArrayList<>();
        for (String teamClassName : teamClasses)
			teamFileNames.add(toClassFileName(teamClassName));
        //  - empty collections:
		wovenClasses = new HashSet<>();
		scannedClasses = new HashSet<>();
		Set<String> moreClassesToWeave = new HashSet<>();

		// 1st round: scan all team classes so we know what to weave (stored in CallinBindingManager)
		logger.info("==== Scanning OT classes ====");
		Collection<String> current = teamFileNames;
		while (!current.isEmpty()) {
			Set<String> otClasses = new HashSet<>();
			for (String classFileName : current) {
				scannedClasses.add(classFileName);
				logger.info("Scanning OT class: "+classFileName);
				try (InputStream stream = loader.getResourceAsStream(classFileName)) {
					if (stream == null) {
						logger.error("Failed to read class "+classFileName);
						numMissing++;
					} else {
						Collection<String> adaptedBases = readOTAttributes(transformer, stream, classFileName, loader, otClasses);
						moreClassesToWeave.addAll(adaptedBases);
					}
				}
				moreClassesToWeave.addAll(otClasses);
			}
			current = otClasses; // iterate over (transitively) referenced roles and teams
		}

		// 2.a: if a main class is specified weave it first to insert team activations:
		if (mainClass != null) {
			logger.info("==== Weaving main class ====");
			weaveClass(transformer, loader, toClassFileName(mainClass), requestor);
		}
		logger.info("==== Weaving teams ====");
		// 2.b: weave the teams:
		for (String teamClassFile : teamFileNames)
			weaveClass(transformer, loader, teamClassFile, requestor);
		// 2.c: weave all other classes:
		logger.info("==== Weaving other classes ====");
		for (String anyClass : moreClassesToWeave)
			weaveClass(transformer, loader, anyClass, requestor);
		logger.info("==== Number of woven classes: "+wovenClasses.size()+" ====");
		if (numMissing > 0)
			throw new MojoFailureException("Could not read "+numMissing+" class files");
	}

	private void weaveClass(ObjectTeamsTransformer transformer, ClassLoader loader, String classFile, Requestor requestor)
			throws IOException, IllegalClassFormatException 
	{
		if (!wovenClasses.add(classFile))
			return;
		try (InputStream stream = loader.getResourceAsStream(classFile)) {
			if (stream == null) {
				logger.error("Failed to read class "+classFile);
				numMissing++;
			} else {
				int avail = stream.available();
				byte[] classBytes = new byte[avail];
				int real = 0;
				while (real < avail)
					real += stream.read(classBytes, real, avail-real);
				byte[] newBytes = transformer.transform(loader, classFile, null, null, classBytes);
				if (newBytes != classBytes) {
					requestor.accept(classFile, newBytes);
					logger.debug("Has woven class: "+classFile);
				}
			}
		}
	}
	
	/**
	 * Modified version of original OTRE API.
	 * @param transformer
	 * @param file content of the class file to scan
	 * @param fileName of the class file to scan
	 * @param loader class loader to use for loading required classes
	 * @param otClassFileNames here we collect the file names of roles and referenced teams of a currently scanned OT class
	 * @return adapted base classes
	 */
	private Collection<String> readOTAttributes(ObjectTeamsTransformer transformer, InputStream file, String fileName,
									ClassLoader loader, final Set<String> otClassFileNames) 
			throws IOException 
	{
		ClassParser   cp  = new ClassParser(file, fileName);
		ClassGen      cg  = new ClassGen(cp.parse());
		JPLISEnhancer jpe = new JPLISEnhancer(cg, loader) {
			@Override
			public void loadClass(String className, ObjectTeamsTransformation client) {
				// intercept referenced classes (roles) to add them to the queue
				String classFileName = toClassFileName(className);
				if (!scannedClasses.contains(classFileName)) {
					logger.debug("\tQueueing OT class: "+classFileName);
					otClassFileNames.add(classFileName);
					// also add role interfaces (by deleting the __OT__ prefix back to front):
					int lastDollar = classFileName.lastIndexOf('$');
					while (lastDollar != -1 && classFileName.substring(lastDollar+1).startsWith("__OT__")) {
						classFileName = classFileName.substring(0, lastDollar+1)+classFileName.substring(lastDollar+7);
						if (!scannedClasses.contains(classFileName)) {
							logger.debug("\tQueueing OT class: "+classFileName);
							otClassFileNames.add(classFileName);
						}
						lastDollar = classFileName.lastIndexOf('$');
					}
				}
				super.loadClass(className, client);
			}
		};
		
		ObjectTeamsTransformation firstTransformation = new ObjectTeamsTransformation(loader) {};
		firstTransformation.checkReadClassAttributes(jpe, cg, cg.getClassName(), cg.getConstantPool());
		
		// transform to class file names:
		Collection<String> adaptedBases = firstTransformation.fetchAdaptedBases();
		Set<String> adaptedBaseFiles = new HashSet<>();
		for (String base : adaptedBases) {
			String baseFile = toClassFileName(base);
			if (!this.scannedClasses.contains(baseFile)) // ... unless already scanned
				adaptedBaseFiles.add(baseFile);
		}
		
		// no double recording of referenced classes as OT class and base:
		adaptedBaseFiles.removeAll(otClassFileNames);
		
		// log:
		for (String baseClassFile : adaptedBaseFiles)
			logger.debug("\tScheduling base: "+baseClassFile);

		return adaptedBaseFiles;
	}

	static String toClassFileName(String className) {
		return className.replace('.', '/') + ".class";
	}
}
