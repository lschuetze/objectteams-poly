/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
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
package org.eclipse.objectteams.otdt.internal.ui.viewsupport;

import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.objectteams.otdt.core.ext.IMarkableJavaElement;
import org.eclipse.objectteams.otdt.ui.OTDTUIPlugin;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import base org.eclipse.ui.internal.views.markers.ExtendedMarkersView;

/**
 * Help the "Problems" view to locate markers placed on class files.
 * @since 2.1.0
 */
@SuppressWarnings("restriction")
public team class MarkerViewAdaptor {

	/**
	 * Name of the handle id attribute in a Java marker.
	 * Copy of protected constant from JavaCore.
	 */
	static final String ATT_HANDLE_ID =
		"org.eclipse.jdt.internal.core.JavaModelManager.handleId" ; //$NON-NLS-1$

	/** 
	 * Inner team allows deferred activation in order to avoid early loading of interface IMarker,
	 * which would trigger activating the resources plugin, which would initialize the instance location
	 * to its default rather than waiting for the Choose Workspace dialog.
	 */
	protected team class MarkersViewLifeCycle playedBy ExtendedMarkersView {

		void activate(Thread t) <- before void init(IViewSite site, IMemento memento)
				with { t <-  ALL_THREADS }
		void deactivate(Thread t) <- before void dispose()
				with { t <-  ALL_THREADS }

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
}
