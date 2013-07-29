/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009 Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: BundleValidation.java 23470 2010-02-05 19:13:24Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.pde.validation;

import static org.eclipse.objectteams.otequinox.Constants.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.objectteams.otdt.internal.pde.ui.OTPDEUIMessages;
import org.eclipse.objectteams.otequinox.ActivationKind;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.builders.CompilerFlags;
import org.eclipse.pde.internal.core.builders.IHeader;
import org.eclipse.pde.internal.core.builders.PDEMarkerFactory;
import org.eclipse.pde.internal.core.ibundle.IManifestHeader;
import org.eclipse.pde.internal.core.text.bundle.BundleActivationPolicyHeader;
import org.eclipse.pde.internal.core.text.bundle.BundleModel;
import org.eclipse.pde.internal.ui.correction.AbstractManifestMarkerResolution;
import org.eclipse.pde.internal.ui.correction.AbstractPDEMarkerResolution;
import org.eclipse.pde.internal.ui.correction.AddExportPackageMarkerResolution;
import org.eclipse.ui.IMarkerResolution;
import org.osgi.framework.Constants;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.osgi.util.NLS;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import base org.eclipse.pde.internal.core.builders.BundleErrorReporter;
import base org.eclipse.pde.internal.core.builders.ExtensionsErrorReporter;
import base org.eclipse.pde.internal.core.builders.ManifestConsistencyChecker;
import base org.eclipse.pde.internal.ui.correction.ResolutionGenerator;

/**
 * Enhanced validation of bundle manifests.
 * <ul>
 * <li>Check whether all bundles with aspectBindings have a proper activation policy set,<br>
 *    Provide suitable quick assist if activation policy is wrong/missing.</li>
 * </ul>
 * 
 * @author stephan
 * @since 1.2.7
 */
@SuppressWarnings("restriction")
public team class BundleValidation 
{
	/** Constant for a problem that can be resolved by adding an activation policy to the manifest. */
	static final int ADD_ACTIVATION_POLICY = 0x1801; // must not overlap with any constant in org.eclipse.pde.internal.core.builders.PDEMarkerFactory. 
	/** Constant for a problem that can be resolved by adding an activation policy to the manifest. */
	static final int ADD_PACKAGE_EXPORT = 0x1802; // must not overlap with any constant in org.eclipse.pde.internal.core.builders.PDEMarkerFactory. 
	
	ThreadLocal<BundleCheckingContext> bundleContext = new ThreadLocal<BundleCheckingContext>();
	
	/** 
	 * Defines the context of validating one bundle. 
	 * One instance of this role exists per control flow (= per thread) 
	 * for retrieval in downstream callin bindings. 
	 */
	protected class BundleCheckingContext playedBy ManifestConsistencyChecker 
	{
		// flags set during validation of one bundle:
		protected boolean isAspectBundle = false;
		protected boolean hasTeamActivation = false;
		protected List<String> aspectPackages = new ArrayList<String>();

		@SuppressWarnings("decapsulation")
		spanContext <- replace validateFiles;
		
		callin void spanContext() {
			BundleValidation.this.bundleContext.set(this);
			try {
				base.spanContext();
			} finally {
				// withdraw role, is for one-time use only:
				BundleValidation.this.bundleContext.set(null);
				BundleValidation.this.unregisterRole(this, BundleCheckingContext.class);
			}
		}
	}
	
	/**
	 * Detects aspectBindings declared in plugin.xml and records information in the current {@link BundleCheckingContext}.
	 */
	protected class ExtensionAnalyzer playedBy ExtensionsErrorReporter 
			base when (BundleValidation.this.bundleContext.get() != null)
	{	
		
		@SuppressWarnings("decapsulation")
		State getState() -> get IPluginModelBase fModel
			with { result <- fModel.getBundleDescription().getContainingState() }
		
		IMarker report(String message, int line, int severity, String category) 
		-> IMarker report(String message, int line, int severity, String category);

		@SuppressWarnings("decapsulation")
		int getLine(Element element) -> int getLine(Element element);


		void checkAspectBinding(Element element) <- after void validateExtension(Element element);

		protected void checkAspectBinding(Element element) 
		{
			Object pointID = element.getAttribute("point"); //$NON-NLS-1$
			if (ASPECT_BINDING_FQEXTPOINT_ID.equals(pointID)) 
			{
				BundleCheckingContext context = BundleValidation.this.bundleContext.get();
				// it's an aspect bundle
				context.isAspectBundle = true;
				
				// does it have elements with relevant activation?
				NodeList baseNodes = element.getElementsByTagName(BASE_PLUGIN);
				if (baseNodes.getLength() > 0) { 
					String baseId = ((Element)baseNodes.item(0)).getAttribute(ID);
					if (baseId != null) {
						checkBasePlugIn(baseId, getLine((Element)baseNodes.item(0)));
						if (baseId.toUpperCase().equals(SELF))
							return; // missing bundle activation is not fatal in this case
					}
				}
				
				// check the teams for activation ALL_THREADS or THREAD:
				NodeList teamNodes = element.getElementsByTagName(TEAM);
				for (int t=0; t<teamNodes.getLength(); t++) {
					// record aspect packages:
					Element teamNode = (Element)teamNodes.item(t);
					Object teamClass = teamNode.getAttribute(CLASS);
					if (teamClass instanceof String) {
						String teamName = (String) teamClass;
						int lastDot = teamName.lastIndexOf('.');
						context.aspectPackages.add(teamName.substring(0, lastDot));
					}
					// team activation?
					Object activation = teamNode.getAttribute(ACTIVATION);
					if (ActivationKind.ALL_THREADS.toString().equals(activation)) {
						context.hasTeamActivation = true;
						break;
					} else if (ActivationKind.THREAD.toString().equals(activation)) {
						context.hasTeamActivation = true;
						break;
					}
				}
			}
		}
		void checkBasePlugIn(String symbolicName, int lineNo) {
			BundleDescription[] bundles = getState().getBundles(symbolicName);
			if (bundles.length == 0)
				report(NLS.bind(OTPDEUIMessages.Validation_UnresolveBasePlugin_error, symbolicName),
						   lineNo, 
						   CompilerFlags.ERROR, 
						   PDEMarkerFactory.CAT_OTHER);
		}
	}
	
	/**
	 * Validates whether activation policy is set if needed.
     * This role is only active for bundles with one or more aspect bindings.
	 */
	protected class BundleErrorReporter playedBy BundleErrorReporter 
			base when (BundleValidation.this.bundleContext.get().isAspectBundle)
	{			
		@SuppressWarnings("decapsulation")
		void addMarkerAttribute(IMarker marker, String attr, String val)
			-> void addMarkerAttribute(IMarker marker, String attr, String val);
		@SuppressWarnings("decapsulation")
		IHeader getHeader(String key) -> IHeader getHeader(String key);
		IMarker report(String message, int line, int severity, int resolution, String category) 
			-> IMarker report(String message, int line, int severity, int resolution, String category);
		
		void validateBundleActivatorPolicy() <- after void validateBundleActivatorPolicy();
		
		void validateBundleActivatorPolicy() 
		{
			IHeader header = getHeader(Constants.BUNDLE_ACTIVATIONPOLICY);
			int lineNo = 1;
			if (header != null) {
				if (Constants.ACTIVATION_LAZY.equals(header.getValue()))
					return; // OK!
				lineNo = header.getLineNumber()+1;
			}
			boolean hasTeamActivation = BundleValidation.this.bundleContext.get().hasTeamActivation;
			report(OTPDEUIMessages.Validation_MissingActivationPolicy_error, 
				   lineNo, 
				   hasTeamActivation ? CompilerFlags.ERROR : CompilerFlags.WARNING, 	// only severe if relevant team activation is requested. 
				   ADD_ACTIVATION_POLICY, 
				   PDEMarkerFactory.CAT_FATAL);
		}

		void validateExportPackages() <- after void validateExportPackages();

		void validateExportPackages() {
			List<String> needingExport = bundleContext.get().aspectPackages;
			if (needingExport.isEmpty()) return;
			IHeader header = getHeader(Constants.EXPORT_PACKAGE);
			if (header != null) {
				ManifestElement[] elements = header.getElements();
				for (int i = 0; i < elements.length; i++)
					needingExport.remove(elements[i].getValue());
			}
			for (String unmatched : needingExport) {
				IMarker marker = report(NLS.bind(OTPDEUIMessages.Validation_MissingAspectPackageExport_error, unmatched), 
						   1, 
						   CompilerFlags.ERROR, 	// can reduce severity when we have the option to add the export at runtime 
						   ADD_PACKAGE_EXPORT, 
						   PDEMarkerFactory.CAT_FATAL);
				addMarkerAttribute(marker, "package", unmatched); //$NON-NLS-1$

				IHeader aspectBundleName = getHeader(Constants.BUNDLE_SYMBOLICNAME);
				if (aspectBundleName != null && aspectBundleName.getValue() != null) {
					String bundleSymbolicName = aspectBundleName.getValue();
					int semi = bundleSymbolicName.indexOf(';');
					if (semi != -1)
						bundleSymbolicName = bundleSymbolicName.substring(0, semi); // strip of attributes/directives like ;singleton:=true
					addMarkerAttribute(marker, "export", unmatched+";ot-aspect-host=\""+bundleSymbolicName+"\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				else {
					addMarkerAttribute(marker, "export", unmatched); //$NON-NLS-1$
				}
			}
		}
	}

	/** Unbound role: simple rewriting of the manifest to add or correct an activation policy header. */
	protected class SetActivationPolicyResolution extends AbstractManifestMarkerResolution 
	{	
		public SetActivationPolicyResolution(int type) {
			super(type);
		}
	
		protected void createChange(BundleModel model) {
	
			IManifestHeader header = model.getBundle().getManifestHeader(Constants.BUNDLE_ACTIVATIONPOLICY);
	
			if (header != null && header instanceof BundleActivationPolicyHeader)
				((BundleActivationPolicyHeader) header).setLazyStart(true);
			else
				model.getBundle().setHeader(Constants.BUNDLE_ACTIVATIONPOLICY, Constants.ACTIVATION_LAZY);
		}
	
		public String getLabel() {
			return OTPDEUIMessages.Resolution_AddBundleActivationPolicy_label;
		}
	}
	
	/** Unbound role: simple rewriting of the manifest to add an Export-Package header. */
	protected class ExportAspectPackageResolution extends AddExportPackageMarkerResolution {
		String packageName;
		String export; // extended version with ot-aspect-host attribute
		public ExportAspectPackageResolution(IMarker marker) {
			super(AbstractPDEMarkerResolution.CREATE_TYPE, marker.getAttribute("export", null)); //$NON-NLS-1$
			this.packageName = marker.getAttribute("package", null); //$NON-NLS-1$
			this.export = marker.getAttribute("export", null); //$NON-NLS-1$
		}
		@Override
		public String getLabel() {
			return NLS.bind(OTPDEUIMessages.Resolution_AddAspectPackageExport_label, packageName);
		}
		@Override
		public String getDescription() {
			return NLS.bind(OTPDEUIMessages.Resolution_AddAspectPackageExport_description, packageName, export);
		}
	}
	
	/**
	 * Advise the base class for handling missing/incorrect activation policy 
	 * (code {@link BundleValidation#ADD_ACTIVATION_POLICY}).
	 */
	protected class ResolutionGenerator playedBy ResolutionGenerator {

		IMarkerResolution[] getResolutions(IMarker marker) <- replace IMarkerResolution[] getResolutions(IMarker marker);
		
		callin IMarkerResolution[] getResolutions(IMarker marker) {
			IMarkerResolution[] result = base.getResolutions(marker);
			if (result.length == 0) {
				int problemID = marker.getAttribute(PDEMarkerFactory.PROBLEM_ID, PDEMarkerFactory.NO_RESOLUTION);
				switch (problemID) {
					case BundleValidation.ADD_ACTIVATION_POLICY :
						return new IMarkerResolution[] {new SetActivationPolicyResolution(AbstractPDEMarkerResolution.CREATE_TYPE)};
					case BundleValidation.ADD_PACKAGE_EXPORT :
						return new IMarkerResolution[] {new ExportAspectPackageResolution(marker) };
				}
			}
			return result;
		}
	}
}
