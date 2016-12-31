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

	static final String[] ROLE_LEAD = { "Project Lead" };
	
	public static final HashMap<String, List<Developer>> developersPerRepo = new HashMap<String, List<Developer>>();
	static {
		developersPerRepo.put("https://git.eclipse.org/c/equinox",
				Arrays.asList(new Developer("Ian Bull"), new Developer("Pascal Rapicault"), new Developer("Thomas Watson")));
		developersPerRepo.put("https://git.eclipse.org/c/platform",
				Arrays.asList(new Developer("Dani Megert")));
	}
	
	String name;
	String[] roles;

	public Developer(String name) {
		this.name = name;
		this.roles = ROLE_LEAD;
	}
	
	public void toPom(StringBuilder buf, String indent) {
		element("developer", indent, buf,
				subElement("name", this.name),
				getRolesElement());
	}
	
	private String getRolesElement() {
		StringBuilder rolesElement = new StringBuilder();
		element("roles", "", rolesElement, String.join("\n", getRoleElements()));
		return rolesElement.toString();
	}

	private String[] getRoleElements() {
		String[] roleElements = new String[this.roles.length];
		for (int i = 0; i < this.roles.length; i++) {
			StringBuilder subBuf = new StringBuilder();
			element("role", "", subBuf, this.roles[i]);
			roleElements[i] = subBuf.toString();
		}
		return roleElements;
	}

	public static String pomSubElements(List<Developer> devs) {
		StringBuilder buf = new StringBuilder();
		for (Developer developer : devs)
			developer.toPom(buf, "");
		return buf.toString();
	}
}
