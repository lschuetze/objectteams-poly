/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2012 GK Software AG.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * `	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.debug.adaptor.launching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.List;

/** 
 * When debugging an OT/Equinox application, the regular framework hooks will
 * not automatically be triggered during redefineClasses().
 * This agent takes care of invoking installed class loader hooks.
 */
public class OTEquinoxDebugAgent {

	static boolean DUMP = System.getProperty("otequinox.dump.redefine") != null;

	static class OTEquinoxTransformerDelegate implements ClassFileTransformer {

		public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
				ProtectionDomain protectionDomain, byte[] classfileBuffer)
				throws IllegalClassFormatException 
		{
			if (classBeingRedefined == null)
				return null; // first loading already goes through framework hooks.
			try {
				if (isDefaultClassLoader(loader)) {
					String classNameDot = className.replace('/', '.'); // transformer expects dot-based names!
					boolean modified = false;
					for (Object hook : getClassLoadingHooks(loader)) {
						// TODO(SH): fetch remaining arguments?
						byte[] newBytes = processClass(hook, classNameDot, classfileBuffer, null/*classpathEntry*/, null/*entry*/, getClasspathManager(loader));
						if (newBytes != null) {
							if (DUMP) dumpBytes(className, newBytes);
							classfileBuffer = newBytes;
							modified = true;
						}
					}
					if (modified)
						return classfileBuffer;
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return null;
		}

		private void dumpBytes(String className, byte[] newBytes)
				throws FileNotFoundException, IOException 
		{
			String fileName = className+".class";
			int lastSlash = fileName.lastIndexOf('/');
			if (lastSlash != -1)
				new File(fileName.substring(0, lastSlash)).mkdirs();
			File dumpFile = new File(fileName);
			FileOutputStream dumpStream = new FileOutputStream(dumpFile);
			dumpStream.write(newBytes);
			dumpStream.close();
		}
		
		// ===== The following members provide reflection based access to otherwise inaccessible classes from org.eclipse.osgi =====
		
		// cached reflection members:
		private Class<?> dclClass;
		private Method getConfiguration;
		private Method getHookRegistry;
		private Method getClassLoaderHooks;
		private Method getClasspathManager;
		private Method processClass;
		
		private boolean isDefaultClassLoader(ClassLoader loader) {
			/* Emulates:
			 *   return loader instanceof EquinoxClassLoader;
			 */
			Class<?> clazz = loader.getClass();
			if (this.dclClass != null) {
				return this.dclClass == clazz;
			} else if (clazz.getName().equals("org.eclipse.osgi.internal.loader.EquinoxClassLoader")) {
				this.dclClass = clazz;
				return true;
			}
			return false;
		}
		
		private List<?> getClassLoadingHooks(ClassLoader loader) 
				throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException 
		{
			/* Emulates:
			 *  EquinoxConfiguration conf = ((EquinoxClassLoader)loader).getConfiguration();
			 *  return conf.getHookRegistry().getClassLoaderHooks();
			 */
			if (this.getConfiguration == null) {
				this.getConfiguration = this.dclClass.getDeclaredMethod("getConfiguration", (Class[])null);
				this.getConfiguration.setAccessible(true);
			}
			Object conf = this.getConfiguration.invoke(loader, (Object[])null);
			if (this.getHookRegistry == null)
				this.getHookRegistry = conf.getClass().getMethod("getHookRegistry", (Class[]) null);
			Object hookRegistry = this.getHookRegistry.invoke(conf, (Object[]) null);
			if (this.getClassLoaderHooks == null)
				this.getClassLoaderHooks = hookRegistry.getClass().getMethod("getClassLoaderHooks", (Class[])null);
			return (List<?>) this.getClassLoaderHooks.invoke(hookRegistry, (Object[])null);
		}
		
		private Object getClasspathManager(ClassLoader loader) 
				throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException 
		{
			/* Emulates:
			 *   return ((DefaultClassLoader)loader).getClasspathManager(); 
			 */
			if (this.getClasspathManager == null)
				this.getClasspathManager = this.dclClass.getMethod("getClasspathManager", (Class[]) null);
			return this.getClasspathManager.invoke(loader, (Object[]) null);
		}

		private byte[] processClass(Object hook, String className, byte[] classfileBuffer, Object classpathEntry, Object entry, Object classpathManager) 
				throws IllegalArgumentException, IllegalAccessException, InvocationTargetException 
		{
			// TODO: support other hooks, too? (need all arguments!)
			if (!hook.getClass().getName().equals("org.eclipse.osgi.internal.weaving.WeavingHookConfigurator"))
				return null;
			/* Emulates:
			 *   return ((WeavingHookConfigurator)hook).processClass(className, classfileBuffer, classpathEntry, entry, classpathManager);
			 */
			findMethod:
			if (this.processClass == null) {
				for (Method m : hook.getClass().getMethods()) {
					if (m.getName().equals("processClass")) {
						this.processClass = m;
						break findMethod;
					}
				}
				throw new NoSuchMethodError("processClass");
			}
			return (byte[])this.processClass.invoke(hook, className, classfileBuffer, classpathEntry, entry, classpathManager);
		}
	}

	/** Install this transformer into the instrumentation. */
	public static void premain(String options, Instrumentation inst) {
		inst.addTransformer(new OTEquinoxTransformerDelegate());
	}
}
