/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008 Technical University Berlin, Germany.
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
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.compiler.adaptor;

import base org.eclipse.jdt.internal.compiler.ProcessTaskManager;

/**
 * This team observes the ProcessTaskManager in order to extend team activation
 * of sub-teams to the processingTask once it is created.
 * 
 * @author stephan
 * @since 1.2.0
 */
@SuppressWarnings("restriction")
public team class CompilationThreadWatcher 
{
	protected class ProcessTaskManager playedBy ProcessTaskManager 
	{
		
		@SuppressWarnings("decapsulation")
		Thread getProcessingThread() -> get Thread processingThread; 

		
		extendActivation <- after setConfig;
		void extendActivation() {
			CompilationThreadWatcher.this.activate(getProcessingThread());
		}
		

		void cleanup() <- after void run();
		void cleanup() { CompilationThreadWatcher.this.deactivate(); }
		
	}
}
