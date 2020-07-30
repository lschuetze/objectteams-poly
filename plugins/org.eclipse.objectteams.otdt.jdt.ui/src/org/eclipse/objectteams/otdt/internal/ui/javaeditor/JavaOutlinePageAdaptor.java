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

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.core.ext.OTDTPlugin;
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
		ISelection getSelection() -> ISelection getSelection();
		Object getInput() -> Object getInput();

		void unwrapOTType(Object element) <- replace void associate(Object element, Item item)
			base when (element instanceof IOTType); 

		callin void unwrapOTType(Object element) {
			base.unwrapOTType(((IOTType)element).getCorrespondingJavaElement());
		}

		void handleOpen(SelectionEvent event) <- replace void handleOpen(SelectionEvent event);

		@SuppressWarnings("basecall")
		callin void handleOpen(SelectionEvent event) {
			ISelection selection = getSelection();
			if (selection instanceof IStructuredSelection) {
				Object firstElement = ((IStructuredSelection) selection).getFirstElement();
				if (firstElement instanceof IJavaElement) {
					IJavaElement selectedElement = (IJavaElement) firstElement;
					IType selectedType = (IType) selectedElement.getAncestor(IJavaElement.TYPE);
					try {
						if (selectedType != null && Flags.isRole(selectedType.getFlags())) {
							IClassFile selectedRoot = (IClassFile) selectedElement.getAncestor(IJavaElement.CLASS_FILE);
							if (selectedRoot != null && OTModelManager.isRole((IType)selectedElement.getAncestor(IJavaElement.TYPE))) {
								Object input = getInput();
								if (input instanceof ITypeRoot) {
									if (!selectedRoot.equals(input)) {
										IEditorPart editor = EditorUtility.openInEditor(firstElement, true);
										EditorUtility.revealInEditor(editor, selectedElement);
										return;
									}
								}
							}
						}
					} catch (PartInitException e) {
						OTDTPlugin.logException("Failed to open element in editor", e); //$NON-NLS-1$
					} catch (JavaModelException e) {
						OTDTPlugin.logException("Failed access flags of type "+selectedElement.getElementName(), e); //$NON-NLS-1$
					}
				}
			}
			base.handleOpen(event);
		}
	}
}
