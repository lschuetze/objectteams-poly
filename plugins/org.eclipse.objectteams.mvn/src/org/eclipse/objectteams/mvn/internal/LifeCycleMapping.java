/*******************************************************************************
 * Copyright (c) 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.objectteams.mvn.internal;

import org.eclipse.m2e.core.internal.lifecyclemapping.model.LifecycleMappingMetadataSource;
import org.osgi.framework.Bundle;

import base org.eclipse.m2e.core.internal.lifecyclemapping.LifecycleMappingFactory;
import base org.eclipse.m2e.core.internal.lifecyclemapping.model.PluginExecutionFilter;

/**
 * This class intercepts reading of org.eclipse.m2e.jdt/lifecycle-mapping-metadata.xml
 * so that we replace <code><compilerId>javac</compilerId></code>
 * with <code><compilerId>jdt</compilerId></code>.
 * This seems to be the only way how we can use the jdt compiler via m2e.
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class LifeCycleMapping {
	
	final static String M2E_JDT_PLUGIN 	= "org.eclipse.m2e.jdt";
	final static String COMPILER_ID 	= "compilerId";
	final static String JDT 			= "jdt";

	/** Cflow-like bracket: work only while reading metadata from org.eclipse.m2e.jdt. */
	protected team class Factory playedBy LifecycleMappingFactory {

		@SuppressWarnings("decapsulation")
		void getMetadataSource(Bundle bundle) <- replace LifecycleMappingMetadataSource getMetadataSource(Bundle bundle)
			base when (M2E_JDT_PLUGIN.equals(bundle.getSymbolicName()));

		static callin void getMetadataSource(Bundle bundle) {
			within (new Context())
				base.getMetadataSource(bundle);
		}
	}
	/** While reading metadata from org.eclipse.m2e.jdt replace any compilerId with "jdt". */
	protected team class Context {
		protected class Filter playedBy PluginExecutionFilter {

			void addParameter(Object key, String value) <- replace void addParameter(Object key, String value)
				base when (COMPILER_ID.equals(key));

			callin void addParameter(Object key, String value) {
				base.addParameter(key, JDT);
			}			
		}
	}
}
