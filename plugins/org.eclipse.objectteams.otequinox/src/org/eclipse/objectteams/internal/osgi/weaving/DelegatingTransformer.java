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
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.objectteams.otredyn.bytecode.IRedefineStrategy;
import org.eclipse.objectteams.otredyn.bytecode.RedefineStrategyFactory;
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
		public OTDRETransformer() {
			RedefineStrategyFactory.setRedefineStrategy(new OTEquinoxRedefineStrategy());
		}
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

	/** Enable OTDRE to use the OTEquinoxDebugAgent, if present, for class redefinition. */
	private static class OTEquinoxRedefineStrategy implements IRedefineStrategy {
		private static final String OT_EQUINOX_DEBUG_AGENT = "org.eclipse.objectteams.otdt.internal.debug.adaptor.launching.OTEquinoxDebugAgent";

		static Instrumentation instrumentation;

		public void redefine(Class<?> clazz, byte[] bytecode) throws ClassNotFoundException, UnmodifiableClassException {
			ClassDefinition arr_cd[] = { new ClassDefinition(clazz, bytecode) };
			try {
				Instrumentation instrum = getInstrumentation();
				if (instrum == null)
					throw new UnmodifiableClassException("Cannot redefined class "+clazz.getName()+", no instrumentation available");
				instrum.redefineClasses(arr_cd);
			} catch (ClassFormatError cfe) {
				// error output during redefinition tends to swallow the stack, print it now:
				System.err.println("Error redefining "+clazz.getName());
				cfe.printStackTrace();
				throw cfe;
			}
		}
		
		static Instrumentation getInstrumentation() {
			if (instrumentation == null) {
				synchronized (OTEquinoxRedefineStrategy.class) {
					if (instrumentation == null)
						try {
							Class<?> agentClass = ClassLoader.getSystemClassLoader().loadClass(OT_EQUINOX_DEBUG_AGENT);
							java.lang.reflect.Method getInstr = agentClass.getMethod("getInstrumentation", new Class<?>[0]);
							instrumentation = (Instrumentation)getInstr.invoke(null, new Object[0]);
						} catch (Throwable t) {}
				}
			}
			return instrumentation;
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
