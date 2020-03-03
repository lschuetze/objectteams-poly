/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2005, 2009 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
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
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.callinmarkers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;

public class MySubProgressMonitor extends SubProgressMonitor
{
    public MySubProgressMonitor(IProgressMonitor monitor, int ticks)
    {
        super(monitor, ticks, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK);
    }
    
    public void doneNothing() {
        beginTask("", 1); //$NON-NLS-1$
        done();
    }
    
    @Override
    public void worked(int work) {
    	if (isCanceled())
    		throw new OperationCanceledException();
    	super.worked(work);
    }
}