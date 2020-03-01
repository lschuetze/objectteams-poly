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
package org.eclipse.objectteams.otdt.internal.ui.javaeditor;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.widgets.Item;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.ui.Util;

import base org.eclipse.jdt.internal.ui.javaeditor.JavaOutlinePage.JavaOutlineViewer;
import base org.eclipse.jdt.internal.ui.javaeditor.JavaOutlinePage.ChildrenProvider;

/**
 * Purpose:
 * <ul>
 * <li>filter generated elements from the outline page.
 * <li>avoid object schizophrenia in StructuredViewer.elementMap
 * </ul>
 *  
 * @author stephan
 */
@SuppressWarnings({ "restriction", "decapsulation" })
public team class JavaOutlinePageAdaptor 
{
	protected class ContentProviderAdaptor playedBy ChildrenProvider
	{
		matches <- replace matches;
		callin boolean matches(IJavaElement element) {
			if (base.matches(element))
				return true;
			return Util.isGenerated(element);
		}
	}
	protected class Viewer playedBy JavaOutlineViewer 
	{
		void unwrapOTType(Object element) <- replace void associate(Object element, Item item)
			base when (element instanceof IOTType); 

		callin void unwrapOTType(Object element) {
			base.unwrapOTType(((IOTType)element).getCorrespondingJavaElement());
		}		
	}
}
