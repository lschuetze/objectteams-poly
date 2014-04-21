/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014 GK Software AG
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.internal.osgi.weaving;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.Bundle;

/**
 * Generalization over the transformers of OTRE and OTDRE.
 */
public abstract class DelegatingTransformer {

	/** Factory method for a fresh transformer. */
	static @NonNull DelegatingTransformer newTransformer(boolean useDynamicWeaver) {
		if (useDynamicWeaver)
			return new OTDRETransformer();
		else
			return new OTRETransformer();
	}
	
	private static class OTRETransformer extends DelegatingTransformer {
		org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer transformer = new org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer();
		@Override
		public void readOTAttributes(String className, InputStream inputStream, String fileName, Bundle bundle) throws ClassFormatError, IOException {
			this.transformer.readOTAttributes(inputStream, fileName, bundle);
		}
		public Collection<String> fetchAdaptedBases() {
			return this.transformer.fetchAdaptedBases();
		}
		public byte[] transform(Bundle bundle, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes)
				throws IllegalClassFormatException
		{
			return this.transformer.transform(bundle, className, classBeingRedefined, protectionDomain, bytes);
		}
	}
	
	private static class OTDRETransformer extends DelegatingTransformer {
		org.eclipse.objectteams.otredyn.transformer.jplis.ObjectTeamsTransformer transformer =
				new org.eclipse.objectteams.otredyn.transformer.jplis.ObjectTeamsTransformer();
		@Override
		public void readOTAttributes(String className, InputStream inputStream, String fileName, Bundle bundle) throws ClassFormatError, IOException {
			// TODO provide classID
			this.transformer.readOTAttributes(className, className.replace('.', '/'), inputStream, getBundleLoader(bundle));
		}
		public Collection<String> fetchAdaptedBases() {
			return transformer.fetchAdaptedBases();
		}
		public byte[] transform(final Bundle bundle, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) {
			// TODO provide classID
			return transformer.transform(getBundleLoader(bundle), className, className.replace('.', '/'), classBeingRedefined, bytes);
		}
	}

	static ClassLoader getBundleLoader(final Bundle bundle) {
		return new ClassLoader() {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				return bundle.loadClass(name);
			}
		};
	}
	
	public abstract void readOTAttributes(String className, InputStream inputStream, String fileName, Bundle bundle) throws ClassFormatError, IOException;
	
	public abstract byte[] transform(Bundle bundle, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes)
			throws IllegalClassFormatException;

	public abstract Collection<String> fetchAdaptedBases();
}
