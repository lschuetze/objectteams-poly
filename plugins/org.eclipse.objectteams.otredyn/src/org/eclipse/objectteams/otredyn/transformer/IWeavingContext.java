/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2015 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 */
package org.eclipse.objectteams.otredyn.transformer;

import org.eclipse.objectteams.runtime.IReweavingTask;

/**
 * Callback protocol to let the transformer query its context.
 * @since 1.1.0
 */
public interface IWeavingContext {
	/**
	 * Is the given class included in load-time weaving, i.e., will
	 * the weaving context pass the class to the transformer during
	 * initial loading?
	 * @param className full qualified classname, dot-separated for packages 
	 * 		and dollar-separated for nested classes. 
	 * @return true if the given class is included in load-time weaving.
	 */
	boolean isWeavable(String className);
	
	/**
	 * When reweaving for className is required, check if this needs to
	 * be scheduled for later.
	 * <br/>
	 * When answering <code>true</code> the weaving context will remember
	 * the given task and invoke it when ready to do so.
	 * <br/>
	 * When answering <code>false</code> the class can be immediately rewoven,
	 * and the weaving context does not directly participate in this.
	 * @param className
	 * @param task
	 * @return <code>true</code> indicates the task has been scheduled for later.
	 */
	boolean scheduleReweaving(String className, IReweavingTask task);
}
