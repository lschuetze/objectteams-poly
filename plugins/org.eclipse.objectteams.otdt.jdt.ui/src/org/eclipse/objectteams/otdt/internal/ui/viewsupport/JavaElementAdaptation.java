/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2006, 2007 Technical University Berlin, Germany.
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
package org.eclipse.objectteams.otdt.internal.ui.viewsupport;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.objectteams.otdt.core.IOTJavaElement;

import base org.eclipse.jdt.internal.ui.JavaElementAdapterFactory;

/**
 * This team takes care of associating IOTJavaElements to their resources. 
 * This is relevant, e.g., for label decorations (text) for OT elements.
 * 
 * @author stephan
 */
@SuppressWarnings("restriction")
public team class JavaElementAdaptation 
{
	/** 
	 * This role makes its baseclass capable of handling IOTJavaElements
	 * via their corresponding IJavaElement.
	 * Note, that JavaElementAdapterFactory cannot handle IOTJavaElements,
	 * but for resource-lookup the corresponding IJavaElement should be fine.
	 */
	protected class FactoryAdaptor playedBy JavaElementAdapterFactory 
	{
		/** Map any IOTJavaElement to its corresponding IJavaElement. */
		callin IJavaElement getJavaElement(Object element) {
			if (element instanceof IOTJavaElement)
				element = ((IOTJavaElement)element).getCorrespondingJavaElement();
			return base.getJavaElement(element);
		}
		@SuppressWarnings("decapsulation")
		getJavaElement <- replace getJavaElement; 
	}
}
