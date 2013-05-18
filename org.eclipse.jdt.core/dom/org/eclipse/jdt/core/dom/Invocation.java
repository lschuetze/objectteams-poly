/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2013 GK Software AG, Germany,
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.jdt.core.dom;

import java.util.List;

/**
 * Simplify access to all implementing classes (OT-specific method invocations).
 * Cf. <code>org.eclipse.jdt.internal.corext.refactoring.code.Invocations</code>
 * and <code>org.eclipse.objectteams.otdt.internal.refactoring.adaptor.CorextAdaptor.Invocations</code>.
 */
public interface Invocation {
	List getArguments();
	ChildListPropertyDescriptor getArgumentsProperty();
	IMethodBinding resolveMethodBinding();
}
