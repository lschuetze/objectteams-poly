/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014 GK Software AG
 *  
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
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otequinox.turbo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Dictionary;

import org.eclipse.osgi.framework.log.FrameworkLog;
import org.eclipse.osgi.framework.log.FrameworkLogEntry;
import org.eclipse.osgi.framework.util.Headers;
import org.eclipse.osgi.internal.hookregistry.HookConfigurator;
import org.eclipse.osgi.internal.hookregistry.HookRegistry;
import org.eclipse.osgi.internal.hookregistry.StorageHookFactory;
import org.eclipse.osgi.storage.BundleInfo.Generation;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

/** This class intercepts each bundle manifest and checks if forced exports must be injected. */
public class OTStorageHook extends StorageHookFactory<Object, Object, OTStorageHook.StorageHookImpl> implements HookConfigurator {
	// HINTS FOR DEBUGGING:
	// - This fragment must be colocated with org.eclipse.osgi - perhaps a symbolic link helps to establish this.
	// - This class must be accessible by its qualified name with no leading "src", again a symbolic link my help:
	//   $ ln -s bin/org org

	private FrameworkLog fwLog;

	class StorageHookImpl extends StorageHookFactory.StorageHook<Object,Object> {

		public StorageHookImpl(Generation generation) {
			super(generation, OTStorageHook.class);
		}

		@Override
		public void initialize(Dictionary<String, String> manifest) throws BundleException {
			// when initializing, intercept the manifest and conditionally inject forced exports
			String[] id = manifest.get(Constants.BUNDLE_SYMBOLICNAME).split(";");
			if (id.length > 0) {
				String packages = ForcedExportsRegistry.getGrantedForcedExportsByBase(id[0]);
				if (packages != null && !packages.isEmpty()) {
					String exportedPackages = manifest.get(Constants.EXPORT_PACKAGE);
					if (exportedPackages != null && !exportedPackages.isEmpty())
						exportedPackages = exportedPackages+','+packages;
					else
						exportedPackages = packages;
					putHeader(id[0], Constants.EXPORT_PACKAGE, exportedPackages, packages);
				}
			}
		}
		/** Reflexively perform our (unusual) work. */
		void putHeader(String id, String header, String value, String added) {
			try {
				Generation gen = getGeneration();
				Method getRawHeaders = gen.getClass().getDeclaredMethod("getRawHeaders", new Class<?>[0]);
				getRawHeaders.setAccessible(true);
				@SuppressWarnings("unchecked")
				Headers<String,String> headers = (Headers<String, String>) getRawHeaders.invoke(gen, new Object[0]);
				Field readOnly = headers.getClass().getDeclaredField("readOnly");
				readOnly.setAccessible(true);
				readOnly.set(headers, false);
				// pay-load:
				headers.put(header, value);
				// restore:
				readOnly.setAccessible(false);
				getRawHeaders.setAccessible(false);
				fwLog.log(new FrameworkLogEntry(OTStorageHook.class.getName(), "OT/Equinox Turbo: added forced export into base bundle "+id+":\n\t"+added, FrameworkLogEntry.INFO, null, null));
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
				fwLog.log(new FrameworkLogEntry(OTStorageHook.class.getName(), "Unable to inject forced exports", FrameworkLogEntry.ERROR, e, null));
			}
		}

		@Override
		public void load(Object loadContext, DataInputStream is) throws IOException {
			// nop
		}

		@Override
		public void save(Object saveContext, DataOutputStream os) throws IOException {
			// nop
		}
	}

	@Override
	public void addHooks(HookRegistry hookRegistry) {
		hookRegistry.addStorageHookFactory(this);
		fwLog = hookRegistry.getContainer().getLogServices().getFrameworkLog();
		ForcedExportsRegistry.install(fwLog);
	}
	
	@Override
	protected StorageHookImpl createStorageHook(Generation generation) {
		return new StorageHookImpl(generation);
	}

	@Override
	public int getStorageVersion() {
		return 0;
	}

	@Override
	public boolean isCompatibleWith(int version) {
		return false; // FIXME: forcing to re-init every time, is this OK?
	}

}
