/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
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
package org.eclipse.objectteams.otdt.internal.ui.viewsupport;

import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.objectteams.otdt.core.ext.IMarkableJavaElement;
import org.eclipse.objectteams.otdt.ui.OTDTUIPlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import base org.eclipse.ui.internal.views.markers.ExtendedMarkersView;

@SuppressWarnings("restriction")
public team class MarkerViewAdaptor {

	/**
	 * Name of the handle id attribute in a Java marker.
	 * Copy of protected constant from JavaCore.
	 */
	static final String ATT_HANDLE_ID =
		"org.eclipse.jdt.internal.core.JavaModelManager.handleId" ; //$NON-NLS-1$

	protected class MarkersView playedBy ExtendedMarkersView {

		void openMarkerInEditor(IMarker marker, IWorkbenchPage page) 
		<- after void openMarkerInEditor(IMarker marker, IWorkbenchPage page)
				base when (marker.getResource() instanceof WorkspaceRoot);

		private static void openMarkerInEditor(IMarker marker, IWorkbenchPage page) {
			String handleIdentifier = marker.getAttribute(ATT_HANDLE_ID, null);
			if (handleIdentifier != null) {
				IJavaElement target = JavaCore.create(handleIdentifier);
				try	{
					IEditorPart part = EditorUtility.openInEditor(target);
					// do we have a detail identifier to position the editor?
					String detailIdentifier = marker.getAttribute(IMarkableJavaElement.ATT_DETAIL_ID, null);
					if (detailIdentifier != null) {
						IJavaElement detail = JavaCore.create(detailIdentifier);
						if (detail.exists())
							EditorUtility.revealInEditor(part, detail);
					}
				} catch (PartInitException ex) {
					OTDTUIPlugin.logException("Problems initializing editor", ex); //$NON-NLS-1$
				}
			}			
		}
	}	
}
