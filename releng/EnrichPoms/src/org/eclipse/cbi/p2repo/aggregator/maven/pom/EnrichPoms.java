/********************************************************************************
 * Copyright (c) 2016 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial implementation
 ********************************************************************************/
package org.eclipse.cbi.p2repo.aggregator.maven.pom;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class EnrichPoms {

	private static final String DOT_POM = ".pom";
	private static final String DOT_JAR = ".jar";
	private static final String BAK_SUFFIX = "-bak";
	
	private static final String SWT_ID = "org.eclipse.swt";
	private static final String[] LINES_SWT_TO_SKIP = {
		"<dependency>",
		"<groupId>org.eclipse.platform</groupId>",
		"<artifactId>org.eclipse.swt.gtk.linux.aarch64</artifactId>",
		"<version>[3.105.3,3.105.3]</version>",
		"</dependency>",
		"<dependency>",
		"<groupId>org.eclipse.platform</groupId>",
		"<artifactId>org.eclipse.swt.gtk.linux.arm</artifactId>",
		"<version>[3.105.3,3.105.3]</version>",
		"</dependency>"
	};


	public static void main(String[] args) throws IOException {
		Path path = FileSystems.getDefault().getPath(args[0]);
		if (!Files.exists(path) || !Files.isDirectory(path))
			throw new IllegalArgumentException(path.toString()+ " is not a directory");
		
		Files.walk(path)
			.filter(EnrichPoms::isArtifact)
			.forEach(EnrichPoms::enrich);
	}
	
	public static boolean isArtifact(Path path) {
		if (Files.isDirectory(path))
			return false;
		if (!path.getFileName().toString().endsWith(DOT_POM))
			return false;
		Path jarPath = getCorrespondingJarPath(path);
		return Files.exists(jarPath);
	}

	public static Path getCorrespondingJarPath(Path pomPath) {
		String fileName = pomPath.getFileName().toString();
		String jarName = fileName.substring(0, fileName.length()-DOT_POM.length())+DOT_JAR; 
		return pomPath.resolveSibling(jarName);
	}

	public static void enrich(Path pomPath) {
		try {
			Path backPath = pomPath.resolveSibling(pomPath.getFileName().toString()+BAK_SUFFIX);
			if (Files.exists(backPath)) {
				System.out.println("Skipping (-bak exists): "+pomPath);
				return;
			}
			Path jarPath = getCorrespondingJarPath(pomPath);
			ArtifactInfo info = ManifestReader.read(jarPath);
			boolean isSWT = SWT_ID.equals(info.bsn);
			Path newPom = Files.createTempFile(pomPath.getParent(), "", DOT_POM);
			try (OutputStreamWriter out = new OutputStreamWriter(Files.newOutputStream(newPom))) {
				boolean copyrightInserted = false;
				boolean detailsInserted = false;
				List<String> allLines = Files.readAllLines(pomPath);
				for (int i = 0; i < allLines.size(); i++) {
					if (isSWT && matches(LINES_SWT_TO_SKIP, allLines, i)) {
						i += LINES_SWT_TO_SKIP.length;
					}
					String line = allLines.get(i);
					out.write(line);
					out.append('\n');
					if (!copyrightInserted) {
						if (line.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
							out.append(ArtifactInfo.COPYRIGHT);
							copyrightInserted = true;
						}
					}
					if (!detailsInserted) {
						if (line.contains("</description>")) {
							out.append(info.toPomFragment());
							detailsInserted = true;
						}
					}
				}
			}
			if (!Files.exists(backPath))
				Files.move(pomPath, backPath);
			Files.move(newPom, pomPath);
		} catch (IOException e) {
			System.err.println("Failed to rewrite pom "+pomPath+": "+e.getClass()+": "+e.getMessage());
		}
	}

	/** Hack to work around https://bugs.eclipse.org/510996 */
	private static boolean matches(String[] linesToSkip, List<String> allLines, int idx) {
		if (idx+linesToSkip.length >= allLines.size())
			return false;
		for (int i = 0; i < linesToSkip.length; i++) {
			if (!allLines.get(idx+i).trim().equals(linesToSkip[i]))
				return false;
		}
		return true;
	}
}
