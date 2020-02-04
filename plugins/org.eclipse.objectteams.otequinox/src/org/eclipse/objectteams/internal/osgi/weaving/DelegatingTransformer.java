/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2015, 2018 GK Software SE
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
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.EnumSet;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.internal.osgi.weaving.ASMByteCodeAnalyzer.ClassInformation;
import org.eclipse.objectteams.internal.osgi.weaving.OTWeavingHook.WeavingReason;
import org.eclipse.objectteams.internal.osgi.weaving.OTWeavingHook.WeavingScheme;
import org.eclipse.objectteams.internal.osgi.weaving.Util.ProfileKind;
import org.eclipse.objectteams.otredyn.bytecode.IRedefineStrategy;
import org.eclipse.objectteams.otredyn.bytecode.RedefineStrategyFactory;
import org.eclipse.objectteams.otredyn.transformer.IWeavingContext;
import org.eclipse.objectteams.runtime.DebugHooks;
import org.eclipse.objectteams.runtime.IReweavingTask;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * Generalization over the transformers of OTRE and OTDRE.
 */
public abstract class DelegatingTransformer {

	static final String OT_EQUINOX_DEBUG_AGENT = "org.eclipse.objectteams.otequinox.OTEquinoxAgent";

	@SuppressWarnings("serial")
	public static class OTAgentNotInstalled extends Exception {
		OTAgentNotInstalled() {
			super("Agent class "+DelegatingTransformer.OT_EQUINOX_DEBUG_AGENT+" was not installed. OT/Equinox will be desabled.\n" +
					"If this happens during the restart after installing OT/Equinox, please exit Eclipse and perform a fresh start.");
		}
	}

	static void checkAgentAvailability(WeavingScheme weavingScheme) throws OTAgentNotInstalled {
		if (weavingScheme == WeavingScheme.OTDRE) {
			try {
				ClassLoader.getSystemClassLoader().loadClass(DelegatingTransformer.OT_EQUINOX_DEBUG_AGENT);
			} catch (ClassNotFoundException cnfe) {
				throw new OTAgentNotInstalled();
			}
		}
	}

	/** Factory method for a fresh transformer. */
	static @NonNull DelegatingTransformer newTransformer(WeavingScheme weavingScheme, @NonNull OTWeavingHook hook, @NonNull BundleWiring wiring) {
		switch (weavingScheme) {
			case OTDRE:
				return new OTDRETransformer(getWeavingContext(hook, wiring));
			case OTRE:
				return new OTRETransformer();
			default:
				throw new NullPointerException("WeavingScheme must be defined");
		}
	}
	
	private static class OTRETransformer extends DelegatingTransformer {
		org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer transformer = new org.eclipse.objectteams.otre.jplis.ObjectTeamsTransformer();
		@Override
		public void readOTAttributes(@NonNull String className, @NonNull InputStream inputStream, @NonNull String fileName, Bundle bundle) throws ClassFormatError, IOException {
			this.transformer.readOTAttributes(inputStream, fileName, bundle);
		}
		public Collection<@NonNull String> fetchAdaptedBases() {
			return this.transformer.fetchAdaptedBases();
		}
		public byte[] transform(Bundle bundle, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes)
				throws IllegalClassFormatException
		{
			return this.transformer.transform(bundle, className, classBeingRedefined, protectionDomain, bytes);
		}
	}
	
	private static class OTDRETransformer extends DelegatingTransformer {
		org.eclipse.objectteams.otredyn.transformer.jplis.ObjectTeamsTransformer transformer;
		public OTDRETransformer(IWeavingContext weavingContext) {
			RedefineStrategyFactory.setRedefineStrategy(new OTEquinoxRedefineStrategy());
			transformer = new org.eclipse.objectteams.otredyn.transformer.jplis.ObjectTeamsTransformer(weavingContext);
		}
		@Override
		public void readOTAttributes(@NonNull String className, @NonNull InputStream inputStream, @NonNull String fileName, Bundle bundle) throws ClassFormatError, IOException {
			// TODO provide classID
			this.transformer.readOTAttributes(className, className.replace('.', '/'), inputStream, getBundleLoader(bundle));
		}
		public Collection<@NonNull String> fetchAdaptedBases() {
			return transformer.fetchAdaptedBases();
		}
		public byte[] transform(final Bundle bundle, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
			// TODO provide classID
			return transformer.transform(getBundleLoader(bundle), className, className.replace('.', '/'), classBeingRedefined, bytes);
		}
	}

	/** Enable OTDRE to use the OTEquinoxAgent, if present, for class redefinition. */
	private static class OTEquinoxRedefineStrategy implements IRedefineStrategy {

		public void redefine(Class<?> clazz, byte[] bytecode) throws ClassNotFoundException, UnmodifiableClassException {
			ClassDefinition arr_cd[] = { new ClassDefinition(clazz, bytecode) };
			try {
				long start = System.nanoTime();
				reflectivelyInvoke(arr_cd);
				if (Util.PROFILE) Util.profile(start, ProfileKind.RedefineClasses, clazz.getName());
				DebugHooks.afterRedefineClasses(clazz.getName());
			} catch (ClassFormatError|UnmodifiableClassException e) {
				// error output during redefinition tends to swallow the stack, print it now:
				System.err.println("Error redefining "+clazz.getName());
				e.printStackTrace();
				ClassInformation classInformation = new ASMByteCodeAnalyzer().getClassInformation(bytecode, clazz.getName());
				if (!classInformation.getName().equals(clazz.getName())) {
					System.err.println("Name mismatch "+clazz.getName()+" vs "+classInformation.getName());
				} else if (!classInformation.getSuperClassName().equals(clazz.getSuperclass().getName())) {
					System.err.println("Superclass mismatch "+clazz.getSuperclass().getName()+" vs "+classInformation.getSuperClassName());
				}
				throw e;
			}
		}
		
		static void reflectivelyInvoke(ClassDefinition[] definitions) throws ClassNotFoundException, ClassFormatError, UnmodifiableClassException {
			try {
				Class<?> agentClass = ClassLoader.getSystemClassLoader().loadClass(OT_EQUINOX_DEBUG_AGENT);
				java.lang.reflect.Method redefine = agentClass.getMethod("redefine", new Class<?>[]{ClassDefinition[].class});
				redefine.invoke(null, new Object[]{definitions});
			} catch (InvocationTargetException ite) {
				Throwable cause = ite.getCause();
				if (cause instanceof ClassFormatError)
					throw (ClassFormatError)cause;
				if (cause instanceof UnmodifiableClassException)
					throw (UnmodifiableClassException)cause;
				throw new UnmodifiableClassException(cause.getClass().getName()+": "+cause.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw cnfe;
			} catch (Throwable t) {
				throw new UnmodifiableClassException(t.getClass().getName()+": "+t.getMessage());
			}
		}
	}

	static @Nullable ClassLoader getBundleLoader(final @Nullable Bundle bundle) {
		if (bundle == null) return null;
		return new ClassLoader() {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				return bundle.loadClass(name);
			}
			@Override
			public URL getResource(String name) {
				return bundle.getResource(name);
			}
		};
	}

	static IWeavingContext getWeavingContext(final @NonNull OTWeavingHook hook, final @NonNull BundleWiring bundleWiring) {
		return new IWeavingContext() {
			@Override
			public boolean isWeavable(String className, boolean considerSupers, boolean allWeavingReasons) {
				if (className == null)
					return false;
				// boolean allWeavingReasons is used in the signature, because IWeavingContext cannot see WeavingReaons:
				EnumSet<@NonNull WeavingReason> reasons = allWeavingReasons ? EnumSet.allOf(WeavingReason.class) : EnumSet.of(WeavingReason.Base);
				WeavingReason reason = hook.requiresWeaving(bundleWiring, className, null, considerSupers, reasons);
				return reason != WeavingReason.None;
			}
			
			@Override
			public boolean scheduleReweaving(String className, /*@NonNull*/ IReweavingTask task) {
				return hook.scheduleReweaving(className, task);
			}
		};
	}
	
	// FIXME: it's unclear if we can tollerate @Nullable Bundle
	public abstract void readOTAttributes(@NonNull String className, @NonNull InputStream inputStream, @NonNull String fileName, Bundle bundle) throws ClassFormatError, IOException;
	
	public abstract byte[] transform(Bundle bundle, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes)
			throws IllegalClassFormatException;

	public abstract Collection<@NonNull String> fetchAdaptedBases();
}
