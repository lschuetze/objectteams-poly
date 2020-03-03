/*******************************************************************************
 * Copyright (c) 2016 GK Software AG, and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation
 *******************************************************************************/
package org.eclipse.objectteams.otdt.internal.samples;

import java.util.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.*;

public class ShowSampleAction extends Action implements IIntroAction {
	
	private String sampleId;

	@Override
	public void run(IIntroSite site, Properties params) {
		sampleId = params.getProperty("id"); //$NON-NLS-1$
		if (sampleId == null)
			return;

		Runnable r = new Runnable() {
			@Override
			public void run() {
				SampleWizard wizard = new SampleWizard();
				try {
					wizard.setInitializationData(null, "class", sampleId); //$NON-NLS-1$
					wizard.setSwitchPerspective(false);
					wizard.setSelectRevealEnabled(false);
					wizard.setActivitiesEnabled(false);
					WizardDialog dialog = new WizardDialog(getActiveWorkbenchShell(), wizard);
					dialog.create();
					if (dialog.open() == Window.OK) {
						switchToSampleStandby(wizard);
					}
				} catch (CoreException e) {
					OTSamplesPlugin.logException(e, null, null);
				}
			}
		};

		Shell currentShell = getActiveWorkbenchWindow().getShell();
		currentShell.getDisplay().asyncExec(r);
	}
	
	Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		return null;
	}

	IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	private void switchToSampleStandby(SampleWizard wizard) {
		StringBuffer url = new StringBuffer();
		url.append("http://org.eclipse.ui.intro/showStandby?"); //$NON-NLS-1$
		url.append("pluginId=org.eclipse.pde.ui"); //$NON-NLS-1$
		url.append("&"); //$NON-NLS-1$
		url.append("partId=org.eclipse.pde.ui.sampleStandbyPart"); //$NON-NLS-1$
		url.append("&"); //$NON-NLS-1$
		url.append("input="); //$NON-NLS-1$
		url.append(sampleId);
		IIntroURL introURL = IntroURLFactory.createIntroURL(url.toString());
		if (introURL != null) {
			introURL.execute();
			ensureProperContext(wizard);
		}
	}

	private void ensureProperContext(SampleWizard wizard) {
		IConfigurationElement sample = wizard.getSelection();
		String perspId = sample.getAttribute("perspectiveId"); //$NON-NLS-1$
		if (perspId != null) {
			try {
				wizard.enableActivities();
				PlatformUI.getWorkbench().showPerspective(perspId, getActiveWorkbenchWindow());
				wizard.selectReveal(getActiveWorkbenchShell());
			} catch (WorkbenchException e) {
				OTSamplesPlugin.logException(e, null, null);
			}
		}
		enableActivities(sample);
	}

	private void enableActivities(IConfigurationElement sample) {
		// TODO: what was planned here (never was implemented in pde.ui)
	}
}
