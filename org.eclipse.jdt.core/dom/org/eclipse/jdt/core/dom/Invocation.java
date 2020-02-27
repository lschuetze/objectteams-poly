/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2013 GK Software AG, Germany,
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
