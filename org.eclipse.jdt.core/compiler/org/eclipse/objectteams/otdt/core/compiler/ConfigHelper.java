/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2009 Stephan Herrmann
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.compiler;

import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.objectteams.otdt.core.exceptions.InternalCompilerError;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.Config;

/**
 * API class for protecting (indirect) calls into the compiler with some minimal setup.
 * @author stephan
 * @since 1.3.2
 */
public class ConfigHelper {

	/**
	 * A resources that doesn't throw on {@link #close()}
	 * @since 3.13 (OTDT 2.6)
	 */
	public interface IConfig extends AutoCloseable {
		@Override
		void close();
	}

	/**
	 * @deprecated use {@link #checkCreateStubConfig2(Object)}
	 */
	@Deprecated
	public static boolean checkCreateStubConfig(Object client) {
		return checkCreateStubConfig2(client) != null;
	}

	/**
	 * When no config has been prepared, create a stub config, which can
	 * handle all boolean queries but throws InternalCompilerError when
	 * attempting to access the lookupEnvironment or the parser.
	 * @since 3.13 (OTDT 2.6)
	 */
	public static IConfig checkCreateStubConfig2(Object client) {
		if (Config.hasConfig(client))
			return null; // no need to create a stub.
		Config newConfig = new Config(client, null, null) {
			@Override
			protected LookupEnvironment lookupEnvironment() {
				throw new InternalCompilerError("Lookup Environment not configured"); //$NON-NLS-1$
			}
			@Override
			protected Parser parser() {
				throw new InternalCompilerError("Parser not configured"); //$NON-NLS-1$
			}
		};
		Config.addConfig(newConfig);
		return newConfig;
	}

	/**
	 * Remove the config that has been registered for 'client'
	 * @param client
	 * @deprecated please use try-with-resources instead of manual removal of Configs
	 */
	@Deprecated
	public static void removeConfig(Object client) {
		Config.removeConfig(client);
	}
}
