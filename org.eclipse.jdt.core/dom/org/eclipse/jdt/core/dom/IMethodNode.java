/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2012 GK Software AG
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
