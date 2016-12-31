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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.eclipse.cbi.p2repo.aggregator.maven.pom.ArtifactInfo.element;
import static org.eclipse.cbi.p2repo.aggregator.maven.pom.ArtifactInfo.subElement;

public class Developer {

	static final String PLATFORM_GIT_REPO = "https://git.eclipse.org/c/platform";

	static final String[] ROLE_LEAD = { "Project Lead" };
	
	public static final HashMap<String, List<Developer>> developersPerRepo = new HashMap<String, List<Developer>>();
	static {
		Developer dani = new Developer("Dani Megert");
		developersPerRepo.put(PLATFORM_GIT_REPO,
				Arrays.asList(dani));
		developersPerRepo.put("https://git.eclipse.org/c/equinox",
				Arrays.asList(new Developer("Ian Bull"), new Developer("Pascal Rapicault"), new Developer("Thomas Watson")));
		developersPerRepo.put("https://git.eclipse.org/c/jdt",
				Arrays.asList(dani));
		developersPerRepo.put("https://git.eclipse.org/c/pde",
				Arrays.asList(new Developer("Curtis Windatt"), new Developer("Vikas Chandra")));
	}

	public static void addDevelopers(String projRepo, String bsn, String indent, StringBuilder buf) {
		List<Developer> devs = getDevelopers(projRepo, bsn);
		if (devs == null)
			System.err.println("No developers for project repo "+projRepo+" ("+bsn+")");
		else
			element("developers", indent, buf, Developer.pomSubElements(devs));
		
	}

	private static List<Developer> getDevelopers(String projRepo, String bsn) {
		// "platform" artifacts in pde repos:
		if ("org.eclipse.ui.views.log".equals(bsn) || "org.eclipse.ui.trace".equals(bsn))
			return developersPerRepo.get(PLATFORM_GIT_REPO);
		// "platform" artifacts in jdt repos:
		if ("org.eclipse.ltk.core.refactoring".equals(bsn) || "org.eclipse.ltk.ui.refactoring".equals(bsn))
			return developersPerRepo.get(PLATFORM_GIT_REPO);
		return developersPerRepo.get(projRepo);
	}

	private static String pomSubElements(List<Developer> devs) {
		StringBuilder buf = new StringBuilder();
		for (Developer developer : devs)
			developer.toPom(buf, "");
		return buf.toString();
	}

	String name;
	String[] roles;

	Developer(String name) {
		this.name = name;
		this.roles = ROLE_LEAD;
	}
	
	void toPom(StringBuilder buf, String indent) {
		element("developer", indent, buf,
				subElement("name", this.name),
				getRolesElement());
	}
	
	String getRolesElement() {
		StringBuilder rolesElement = new StringBuilder();
		element("roles", "", rolesElement, String.join("\n", getRoleElements()));
		return rolesElement.toString();
	}

	String[] getRoleElements() {
		String[] roleElements = new String[this.roles.length];
		for (int i = 0; i < this.roles.length; i++) {
			StringBuilder subBuf = new StringBuilder();
			element("role", "", subBuf, this.roles[i]);
			roleElements[i] = subBuf.toString();
		}
		return roleElements;
	}
}
