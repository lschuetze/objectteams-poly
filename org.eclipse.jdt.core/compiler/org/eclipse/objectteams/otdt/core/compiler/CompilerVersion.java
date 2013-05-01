/**********************************************************************
 * This file is part of the "Object Teams Development Tooling"-Software.
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
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core.compiler;

import org.eclipse.objectteams.otdt.internal.core.compiler.mappings.CallinImplementorDyn;

/**
 * @since 3.9 (OTDT 2.2)
 */
public class CompilerVersion {
	/**
	 * Define whether the compiler should generate byte code suitable for dynamic weaving.
	 */
	public static void setDynamicWeaving(boolean useDynamicWeaving) {
		CallinImplementorDyn.DYNAMIC_WEAVING = useDynamicWeaving;
	}
}
