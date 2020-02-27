/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2012 GK Software AG
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
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.jdt.core.dom;

import java.util.List;

/**
 * Generalization over MethodDeclaration and MethodSpec
 * @since 3.9
 */
public interface IMethodNode {
	List parameters();
	List typeParameters();
	Type getReturnType2();
	void setReturnType2(Type type);
}
