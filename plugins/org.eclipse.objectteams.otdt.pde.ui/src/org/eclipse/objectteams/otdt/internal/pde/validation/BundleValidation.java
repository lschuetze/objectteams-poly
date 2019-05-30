/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009, 2019 Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.pde.validation;

import static org.eclipse.objectteams.otequinox.Constants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IMethodMappingBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.corext.dom.Bindings;
import org.eclipse.objectteams.otdt.core.IMethodMapping;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.internal.pde.ui.OTPDEUIMessages;
import org.eclipse.objectteams.otequinox.ActivationKind;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.builders.CompilerFlags;
import org.eclipse.pde.internal.core.builders.IHeader;
import org.eclipse.pde.internal.core.builders.IncrementalErrorReporter.VirtualMarker;
import org.eclipse.pde.internal.core.builders.PDEMarkerFactory;
import org.eclipse.pde.internal.core.ibundle.IManifestHeader;
import org.eclipse.pde.internal.core.text.bundle.BundleActivationPolicyHeader;
import org.eclipse.pde.internal.core.text.bundle.BundleModel;
import org.eclipse.pde.internal.core.text.plugin.PluginAttribute;
import org.eclipse.pde.internal.ui.correction.AbstractManifestMarkerResolution;
import org.eclipse.pde.internal.ui.correction.AbstractPDEMarkerResolution;
import org.eclipse.pde.internal.ui.correction.AbstractXMLMarkerResolution;
import org.eclipse.pde.internal.ui.correction.AddExportPackageMarkerResolution;
import org.eclipse.ui.IMarkerResolution;
import org.osgi.framework.Constants;
import org.osgi.framework.namespace.PackageNamespace;
import org.osgi.resource.Capability;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import base org.eclipse.pde.internal.core.builders.BundleErrorReporter;
import base org.eclipse.pde.internal.core.builders.ExtensionsErrorReporter;
import base org.eclipse.pde.internal.core.builders.ManifestConsistencyChecker;
import base org.eclipse.pde.internal.core.builders.XMLErrorReporter;
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
	static final int CHANGE_DOT_TO_DOLLAR = 0x1803; // must not overlap with any constant in org.eclipse.pde.internal.core.builders.PDEMarkerFactory.

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
		protected Set<String> aspectPackages = new HashSet<String>();

		/** packages containing bound base classes, which require the team to be bound to the corresponding base bundle. */
		public Map<String,List<String>> requiredBasePackagesPerTeam = new HashMap<String, List<String>>();
		
		IProject getProject() -> IProject getProject();  

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
		protected void addRequiredBasePackage(String teamName, String baseName) {
			List<String> bases = requiredBasePackagesPerTeam.get(teamName);
			if (bases == null)
				requiredBasePackagesPerTeam.put(teamName, bases = new ArrayList<>());
			bases.add(baseName);
		}
	}
	
	/** Super-role for access to internal members. */
	protected class XMLAnalyzer playedBy XMLErrorReporter {
		@SuppressWarnings("decapsulation")
		protected String generateLocationPath(Node node, String attrName) -> String generateLocationPath(Node node, String attrName);

		@SuppressWarnings("decapsulation")
		protected IProject getFProject() -> get IProject fProject;
	}

	/**
	 * Detects aspectBindings declared in plugin.xml and records information in the current {@link BundleCheckingContext}.
	 * Directly reports erroneous use of '.' for nested team names.
	 */
	protected class ExtensionAnalyzer extends XMLAnalyzer playedBy ExtensionsErrorReporter 
			base when (BundleValidation.this.bundleContext.get() != null)
	{	
		
		@SuppressWarnings("decapsulation")
		State getState() -> get IPluginModelBase fModel
			with { result <- fModel.getBundleDescription().getContainingState() }
		
		VirtualMarker report(String message, int line, int severity, int fixId, String category)
		-> VirtualMarker report(String message, int line, int severity, int fixId, String category);

		@SuppressWarnings("decapsulation")
		int getLine(Element element) -> int getLine(Element element);
		
		@SuppressWarnings("decapsulation")
		int getLine(Element element, String attrName) -> int getLine(Element element, String attrName);

		VirtualMarker report(String message, int line, int severity, int fixId, Element element, String attrName, String category)
		<- replace VirtualMarker report(String message, int line, int severity, int fixId, Element element, String attrName, String category);

		void checkAspectBinding(Element element) <- after void validateExtension(Element element);

		protected void checkAspectBinding(Element element) 
		{
			Object pointID = element.getAttribute("point"); //$NON-NLS-1$
			if (ASPECT_BINDING_FQEXTPOINT_ID.equals(pointID)) 
			{
				BundleCheckingContext context = BundleValidation.this.bundleContext.get();
				// it's an aspect bundle
				context.isAspectBundle = true;
				
				IJavaProject jProject = JavaCore.create(context.getProject());

				boolean hasSelfAdaptation = false;
				NodeList baseNodes = element.getElementsByTagName(BASE_PLUGIN);
				for (int b=0; b<baseNodes.getLength(); b++) {
					if (SELF.equalsIgnoreCase(((Element)baseNodes.item(b)).getAttribute(ID))) {
						hasSelfAdaptation = true;
						break;
					}
				}

				Map<String,Set<String>> superBasePackagesByTeam;
				{
					List<IMethodMapping> mappings = new ArrayList<>();
	
					// collect binding requirements by nested teams of all bound teams:
					NodeList teamNodes = element.getElementsByTagName(TEAM);
					for (int t=0; t<teamNodes.getLength(); t++) {
						// record aspect packages:
						Object teamClass = ((Element)teamNodes.item(t)).getAttribute(CLASS);
						if (teamClass instanceof String)
							checkNestedTeams((String) teamClass, context, hasSelfAdaptation, mappings);
					}
					// collect packages with overridden base methods:
					superBasePackagesByTeam = collectOverridden(mappings);
				}
				NodeList aspectBindings = element.getChildNodes();
				int aspectCount = aspectBindings.getLength();
				for (int i = 0; i < aspectCount; i++) {
					Node aspectBinding = aspectBindings.item(i);
					// does it have elements with relevant activation?
					boolean isSelfAdaptation = false;
					boolean hasActivation = true;
					BundleDescription baseBundle = null;
					List<String> teamNames = new ArrayList<String>();

					NodeList children = aspectBinding.getChildNodes();
					int childrenCount = children.getLength();
					for (int j = 0; j < childrenCount; j++) {
						Node child = children.item(j);
						if (child instanceof Element) {
							Element childElement = (Element)child;
							String tagName = childElement.getTagName();
							if (BASE_PLUGIN.equals(tagName)) {
								String baseId = childElement.getAttribute(ID);
								if (baseId != null) {
									if (baseId.toUpperCase().equals(SELF))
										isSelfAdaptation = true; // missing bundle activation is not fatal in this case
									else
										baseBundle = checkBasePlugIn(baseId, getLine(childElement));
								}

							} else if (TEAM.equals(tagName)) {
								// analyze aspect packages:
								Element teamNode = childElement;
								Object teamClass = teamNode.getAttribute(CLASS);
								if (!(teamClass instanceof String))
									continue;
							
								String teamName = (String) teamClass;
								String actualPackage = checkActualPackage(context, teamNode, teamName);
								if (actualPackage == null)
									report(OTPDEUIMessages.Validation_MissingPackage_error, getLine(teamNode),
											CompilerFlags.ERROR, PDEMarkerFactory.NO_RESOLUTION, PDEMarkerFactory.CAT_FATAL);
								else
									context.aspectPackages.add(actualPackage);
								teamNames.add(teamName);

								// team activation?
								Object activation = teamNode.getAttribute(ACTIVATION);
								if (ActivationKind.ALL_THREADS.toString().equals(activation)) {
									hasActivation = true;
								} else if (ActivationKind.THREAD.toString().equals(activation)) {
									hasActivation = true;
								}
								NodeList superBases = teamNode.getElementsByTagName(SUPER_BASE);
								for (int k=0; k<superBases.getLength(); k++) {
									// report bad declarations & remove superBase requirements matching this declaration 
									Node grandChild = superBases.item(k);
									if (grandChild instanceof Element) {
										checkSuperBaseClass((Element) grandChild, superBasePackagesByTeam.get(teamName), jProject, baseBundle);
									}
								}
							}
						}
					}
					if (hasActivation && !isSelfAdaptation)
						context.hasTeamActivation = true;
					if (baseBundle != null) {
						// remove packages provided by this baseBundle from the list of required packages
						Set<String> providedPackages = new HashSet<String>();
						// for SELF-adaptation we don't need a package export, that's why we include those 
						// requirements from this check (see checkNestedTeams(..hasSelfAdaptation)).
						for (Capability cap : baseBundle.getCapabilities(PackageNamespace.PACKAGE_NAMESPACE))
							providedPackages.add((String) cap.getAttributes().get(PackageNamespace.PACKAGE_NAMESPACE));
						for (String teamName: teamNames) {
							List<String> basePackagesList = context.requiredBasePackagesPerTeam.get(teamName);
							if (basePackagesList != null) {
								Iterator<String> basePackages = basePackagesList.iterator();
								while (basePackages.hasNext()) {
									String basePackage = basePackages.next();
									if (providedPackages.contains(basePackage))
										basePackages.remove();
								}
							}
						}
					}
				}
				// complain about remaining requiredBasePackages (i.e., those for which no binding was provided)
				reportUnmatchedRequirements(element, context.requiredBasePackagesPerTeam,
											OTPDEUIMessages.Validation_MissingBindingForBasePackage_error);
				// complain about remaining undeclared super bases:
				reportUnmatchedRequirements(element, superBasePackagesByTeam,
											OTPDEUIMessages.Validation_MissingSuperBasePackageDecl_error);
			}
		}

		private void reportUnmatchedRequirements(Element element,
				Map<String, ? extends Collection<String>> packagesPerTeam,
				String errorMessageTemplate)
		{
			for (Entry<String, ? extends Collection<String>> entry : packagesPerTeam.entrySet()) {
				Collection<String> requiredBasePackages = entry.getValue();
				if (requiredBasePackages != null && !requiredBasePackages.isEmpty()) {
					for (String requiredBasePackage : requiredBasePackages) {
						report(NLS.bind(errorMessageTemplate, entry.getKey(), requiredBasePackage),
								getLine(element),
								CompilerFlags.ERROR,
								PDEMarkerFactory.NO_RESOLUTION,
								PDEMarkerFactory.CAT_FATAL);
					}
				}
			}
		}

		private Map<String, Set<String>> collectOverridden(List<IMethodMapping> mappings) {
			Map<String,Set<String>> superBasePackagesByTeam = new HashMap<>();
			try {
				// collect role types from mappings (IType, then ITypeBinding):
				Set<IType> roleTypes = new HashSet<IType>(); 
				for (IMethodMapping mapping : mappings) {
					IType type = mapping.getDeclaringType();
					if (!"java.lang.Object".equals(type.getSuperclassName())) //$NON-NLS-1$
						roleTypes.add(type);
				}
				if (roleTypes.isEmpty())
					return Collections.emptyMap();
				ASTParser parser = ASTParser.newParser(AST.JLS12);
				parser.setProject(JavaCore.create(getFProject()));
				IBinding[] bindings = parser.createBindings(roleTypes.toArray(new IType[roleTypes.size()]), null);

				// from ITypeBinding descend into IMethodMappingBinding, then IMethodBinding (base):
				for (IBinding binding : bindings) {
					if (binding instanceof ITypeBinding) {
						String teamName = ((ITypeBinding) binding).getDeclaringClass().getQualifiedName();
						Set<String> perTeamResult = superBasePackagesByTeam.get(teamName);
						for (IMethodMappingBinding mappingBinding : ((ITypeBinding) binding).getResolvedMethodMappings()) {
							for (IMethodBinding basemethod : mappingBinding.getBaseMethods()) {
								// find overridden
								for (IMethodBinding overriddenMethod : Bindings.findOverriddenMethods(basemethod, true, false)) {									
									// remember package of declaring class
									String packageName = overriddenMethod.getDeclaringClass().getPackage().getName();
									if (perTeamResult == null) {
										superBasePackagesByTeam.put(teamName, perTeamResult = new HashSet<>());
									}
									perTeamResult.add(packageName);
								}
							}
						}
					}
				}
			} catch (JavaModelException e) {
				// cannot analyse
			}
			return superBasePackagesByTeam;
		}

		private void checkSuperBaseClass(Element elem, Set<String> collectedPackages, IJavaProject jProject, BundleDescription baseBundle) {
			try {
				String packageName = null;
				String superBaseClass = elem.getAttribute(SUPER_BASE_CLASS);
				if (superBaseClass != null) {
					IType clazz = jProject.findType(superBaseClass);
					if (clazz != null) { // otherwise assume standard validation already complained
						packageName = clazz.getPackageFragment().getElementName();
						if (collectedPackages == null || !collectedPackages.remove(packageName)) {
							report(NLS.bind(OTPDEUIMessages.Validation_UnnecessarySuperBase_warning, superBaseClass),
									getLine(elem), 
									CompilerFlags.WARNING,
									PDEMarkerFactory.NO_RESOLUTION,
									PDEMarkerFactory.CAT_OTHER);
						}
					}
				}
				if (packageName != null) {
					String bundleName = elem.getAttribute(SUPER_BASE_PLUGIN);
					BundleDescription basePlugIn = (bundleName != null) ? checkBasePlugIn(bundleName, getLine(elem))
													: baseBundle; // fall back if no explicit plugin
					if (basePlugIn != null) {
						for (Capability cap : basePlugIn.getCapabilities(PackageNamespace.PACKAGE_NAMESPACE)) {
							if (packageName.equals(cap.getAttributes().get(PackageNamespace.PACKAGE_NAMESPACE)))
								return;
						}
						report(NLS.bind(OTPDEUIMessages.Validation_PackageNotInSuperBase_error, bundleName, packageName),
								getLine(elem), 
								CompilerFlags.ERROR,
								PDEMarkerFactory.NO_RESOLUTION,
								PDEMarkerFactory.CAT_FATAL);						
					}
				}
			} catch (JavaModelException e) {
				// cannot analyse
			}
		}

		String checkActualPackage(BundleCheckingContext context, Element teamNode, String teamName) {
			int lastDot = teamName.lastIndexOf('.');
			if (lastDot == -1)
				return null;
			String packageName = teamName.substring(0, lastDot);
			String actualPackage = getContainingPackage(context, packageName);
			if (packageName != actualPackage) {
				VirtualMarker marker = report(NLS.bind(OTPDEUIMessages.Validation_NotAPackage_error, packageName),
						   getLine(teamNode, CLASS), 
						   CompilerFlags.ERROR,
						   CHANGE_DOT_TO_DOLLAR,
						   PDEMarkerFactory.CAT_FATAL);
				if (marker != null) {
					marker.setAttribute("package", actualPackage); //$NON-NLS-1$
					marker.setAttribute("team", teamName); //$NON-NLS-1$
					marker.setAttribute(PDEMarkerFactory.MPK_LOCATION_PATH, generateLocationPath(teamNode, CLASS));
				}
			}
			return actualPackage;
		}
		String getContainingPackage(BundleCheckingContext context, String packageNameCandidate) {
			IProject project = context.getProject();
			if (project != null) {
				IJavaProject jProject = JavaCore.create(project);
				if (jProject != null) {
					try {
						IJavaElement jElement = jProject.findElement(new Path(packageNameCandidate));
						if (jElement != null && jElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT)
							return packageNameCandidate;
						jElement = jProject.findType(packageNameCandidate);
						if (jElement != null) {
							IJavaElement ancestor = jElement.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
							if (ancestor != null)
								return ancestor.getElementName();
						}
					} catch (JavaModelException e) {
						// cannot analyse
					}
				}
			}
			return packageNameCandidate; // be shy about reporting errors in error contexts
		}

		BundleDescription checkBasePlugIn(String symbolicName, int lineNo) {
			BundleDescription[] bundles = getState().getBundles(symbolicName);
			if (bundles.length == 0) {
				report(NLS.bind(OTPDEUIMessages.Validation_UnresolveBasePlugin_error, symbolicName),
						   lineNo, 
						   CompilerFlags.ERROR,
						   PDEMarkerFactory.NO_RESOLUTION,
						   PDEMarkerFactory.CAT_OTHER);
				return null;
			}
			return bundles[0];
		}
		
		void checkNestedTeams(String teamName, BundleCheckingContext context, boolean hasSelfAdaptation, List<IMethodMapping> mappings) {
			teamName = teamName.replace('$', '.');
			IJavaProject jPrj = JavaCore.create(getFProject());
			if (jPrj.exists()) {
				try {
					IType teamType = jPrj.findType(teamName);
					if (teamType != null) {
						for (IType member : teamType.getTypes()) {
							if (OTModelManager.isTeam(member)) {
								String nestedTeamName = member.getFullyQualifiedName('$'); // name as used in aspectBinding.basePlugin
								for (IType role : OTModelManager.getOTElement(member).getRoleTypes()) {
									IType aBase = ((IRoleType) OTModelManager.getOTElement(role)).getBaseClass();
									if (aBase != null
											&& !(hasSelfAdaptation && aBase.getJavaProject().equals(jPrj)))
										context.addRequiredBasePackage(nestedTeamName, aBase.getPackageFragment().getElementName());
								}
								checkNestedTeams(nestedTeamName, context, hasSelfAdaptation, mappings);
							} else {
								IRoleType role = (IRoleType) OTModelManager.getOTElement(member);
								for (IMethodMapping mapping : role.getMethodMappings())
									mappings.add(mapping);
							}
						}
					}
				} catch (JavaModelException e) {
					// cannot analyse
				}
			}
		}

		@SuppressWarnings("basecall")
		callin VirtualMarker report(String message, int line, int severity, int fixId, Element element, String attrName, String category) {
			if (fixId == PDEMarkerFactory.M_DISCOURAGED_CLASS) {
				if (matchElementPath(element, new String[] {ASPECT_BINDING, TEAM, SUPER_BASE}, 2))
					return null; // don't report restriction inside aspectBinding/superBase
			}
			return base.report(message, line, severity, fixId, element, attrName, category);
		}

		private boolean matchElementPath(Element cur, String[] containerTags, int idx) {
			if (idx < 0)
				return true;
			if (!containerTags[idx].equals(cur.getTagName()))
				return false;
			Node parentNode = cur.getParentNode();
			if (parentNode instanceof Element)
				return matchElementPath((Element) parentNode, containerTags, idx-1);
			return false;
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
		void addMarkerAttribute(VirtualMarker marker, String attr, String val)
			-> void addMarkerAttribute(VirtualMarker marker, String attr, String val);
		@SuppressWarnings("decapsulation")
		IHeader getHeader(String key) -> IHeader getHeader(String key);
		VirtualMarker report(String message, int line, int severity, int resolution, String category) 
			-> VirtualMarker report(String message, int line, int severity, int resolution, String category);
		
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
			Set<String> needingExport = bundleContext.get().aspectPackages;
			if (needingExport.isEmpty()) return;
			IHeader header = getHeader(Constants.EXPORT_PACKAGE);
			if (header != null) {
				ManifestElement[] elements = header.getElements();
				for (int i = 0; i < elements.length; i++)
					needingExport.remove(elements[i].getValue());
			}
			for (String unmatched : needingExport) {
				VirtualMarker marker = report(NLS.bind(OTPDEUIMessages.Validation_MissingAspectPackageExport_error, unmatched), 
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
			super(marker, AbstractPDEMarkerResolution.CREATE_TYPE, marker.getAttribute("export", null)); //$NON-NLS-1$
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

	/** Unbound role: rewrite the team@class attribute for proper usage of '$' as inner class separator. */
	protected class ChangeDotToDollarResolution extends AbstractXMLMarkerResolution {
		String packageName;
		String teamName;
		String newName;

		public ChangeDotToDollarResolution(IMarker marker) {
			super(CHANGE_DOT_TO_DOLLAR, marker);
			this.packageName = marker.getAttribute("package", null); //$NON-NLS-1$
			this.teamName = marker.getAttribute("team", null); //$NON-NLS-1$
			this.newName = packageName + '.' +teamName.substring(this.packageName.length()+1).replace('.', '$');
		}
		@Override
		public String getLabel() {
			return NLS.bind(OTPDEUIMessages.Resolution_ChangeDotToDollar_label, this.teamName);
		}
		@Override
		public String getDescription() {
			return NLS.bind(OTPDEUIMessages.Resolution_ChangeDotToDollar_description, this.teamName, this.newName);
		}

		@Override
		protected void createChange(IPluginModelBase model) {
			Object node = findNode(model);
			if (!(node instanceof PluginAttribute))
				return;
			
			PluginAttribute attr = (PluginAttribute) node;
			attr.getEnclosingElement().setXMLAttribute(attr.getName(), this.newName);
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
					case BundleValidation.CHANGE_DOT_TO_DOLLAR :
						return new IMarkerResolution[] {new ChangeDotToDollarResolution(marker) };
				}
			}
			return result;
		}
	}
}
