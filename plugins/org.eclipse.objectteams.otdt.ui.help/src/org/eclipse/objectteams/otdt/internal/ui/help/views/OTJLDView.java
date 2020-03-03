/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008 Technical University Berlin, Germany.
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
package org.eclipse.objectteams.otdt.internal.ui.help.views;

import org.eclipse.objectteams.otdt.internal.ui.help.OTJLDError;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.BrowserViewer;
import org.eclipse.ui.internal.browser.WebBrowserUtil;
import org.eclipse.ui.internal.browser.WebBrowserView;

/**
 * @author mosconi
 */
@SuppressWarnings("restriction")
public class OTJLDView extends WebBrowserView {

	@Override
	public void createPartControl(Composite parent) {
		viewer = new BrowserViewer(parent, IWorkbenchBrowserSupport.AS_VIEW);
		viewer.setContainer(this);
		setURL(OTJLDError.getHomepageURL().getURL());
	}
	
	public BrowserViewer getViewer() {
		return viewer;
	}
	
	public static boolean hasBrowser() {
		return WebBrowserUtil.canUseInternalWebBrowser();
	}

	@Override
	public void setURL(String url) {
		super.setURL(url);
	}
	
}