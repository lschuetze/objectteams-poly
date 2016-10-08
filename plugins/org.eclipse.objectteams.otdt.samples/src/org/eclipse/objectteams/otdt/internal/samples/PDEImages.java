/*******************************************************************************
 * Copyright (c) 2016 GK Software AG, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation
 *******************************************************************************/
package org.eclipse.objectteams.otdt.internal.samples;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.pde.internal.ui.PDELabelProvider;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.swt.graphics.Image;

/**
 * Encapsulate discouraged access to images from PDE/UI via their label provider.
 */
@SuppressWarnings("restriction")
public class PDEImages {

	public static final ImageDescriptor DESC_NEWEXP_WIZ= PDEPluginImages.DESC_NEWEXP_WIZ;
	public static final ImageDescriptor DESC_RUN_EXC = PDEPluginImages.DESC_RUN_EXC;
	public static final ImageDescriptor DESC_DEBUG_EXC = PDEPluginImages.DESC_DEBUG_EXC;
	public static final ImageDescriptor DESC_NEWEXP_TOOL = PDEPluginImages.DESC_NEWEXP_TOOL;

	private PDELabelProvider provider;

	public PDEImages(PDELabelProvider provider) {
		this.provider = provider;
	}

	public static PDEImages connect(Object consumer) {
		PDELabelProvider provider = PDEPlugin.getDefault().getLabelProvider();
		provider.connect(consumer);
		return new PDEImages(provider);
	}

	public void disconnect(Object consumer) {
		provider.disconnect(consumer);
		provider = null;
	}

	public Image get(ImageDescriptor desc) {
		return provider.get(desc);
	}
}
