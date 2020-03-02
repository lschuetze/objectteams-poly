/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009 Germany and Technical University Berlin, Germany.
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
package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.otequinox.AspectPermission.DENY;
import static org.eclipse.objectteams.otequinox.AspectPermission.GRANT;
import static org.eclipse.objectteams.otequinox.AspectPermission.UNDEFINED;
import static org.eclipse.objectteams.otequinox.TransformerPlugin.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBinding.BaseBundle;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBinding.TeamBinding;
import org.eclipse.objectteams.otequinox.ActivationKind;
import org.eclipse.objectteams.otequinox.AspectBindingRequestAnswer;
import org.eclipse.objectteams.otequinox.AspectPermission;
import org.eclipse.objectteams.otequinox.Constants;
import org.eclipse.objectteams.otequinox.IAspectRequestNegotiator;
import org.eclipse.objectteams.otequinox.TransformerPlugin;
import org.eclipse.osgi.internal.hookregistry.HookConfigurator;
import org.eclipse.osgi.service.datalocation.Location;
import org.objectteams.Team;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * Manage permissions of aspect bundles requesting to apply aspectBindings and forcedExports.
 * The following pieces of information are checked:
 * <ul>
 * <li>properties set in installation-wide config.ini or as command line args (handled by {@link HookConfigurator} (plus internal class OTStorageHook))</li>
 * <li>defaults set per workspace (file negotiationDefaults.txt)</li>
 * <li>individual GRANT/DENY per workspace (files grantedForcedExports.txt, deniedForcedExports.txt)</li>
 * <li>answers from registered negotiators (extension point org.eclipse.objectteams.otequinox.aspectBindingNegotiators, see {@link IAspectRequestNegotiator})</li>
 * </ul>
 *
 * <p>
 * The final answer for a given request is combined from all sources where the priority of any {@link #DENY} answer is highest, 
 * of {@link #UNDEFINED} is lowest.
 * </p>
 * <p>
 * If a negotiator has determined a decision and its answer has the <code>persistent</code> flag set,
 * this particular aspect permission is stored as per-workspace configuration.
 * </p>
 * @author stephan
 * @since 1.2.6
 */
@SuppressWarnings("restriction")
@NonNullByDefault
public class AspectPermissionManager {

	// property names for default configuration:
	private static final String FORCED_EXPORT_DEFAULT  = "forced.export.default";
	private static final String ASPECT_BINDING_DEFAULT = "aspect.binding.default";
	
	// workspace files where negotiation configuration is stored:
	private static final String NEGOTIATION_DEFAULTS_FILE   = "negotiationDefaults.txt";  
	private static final String GRANTED_FORCED_EXPORTS_FILE = "grantedForcedExports.txt";
	private static final String DENIED_FORCED_EXPORTS_FILE  = "deniedForcedExports.txt";

	private static final String GRANTED_TEAMS_FILE = "grantedTeams.txt";
	private static final String DENIED_TEAMS_FILE  = "deniedTeams.txt";

	// set of aspect plug-ins for which some permission has been denied:
	private Set<String> deniedAspects = new HashSet<String>();
	// default permission for aspect bindings:
	private AspectPermission defaultAspectBindingPermission = GRANT;
	// default permission for forced exports:
	private AspectPermission defaultForcedExportPermission = UNDEFINED; // not yet granted, but open for receiving a GRANT
	// for negotiation of aspect binding requests (incl. forced export):
	private List<IAspectRequestNegotiator> negotiators = new ArrayList<IAspectRequestNegotiator>();
	
	
	// collect all forced exports (denied/granted), granted should balance to an empty structure.
	// structure is: aspect-id -> (base bundle x base package)*
	private Map<String, List<@NonNull String[]>> deniedForcedExportsByAspect= new HashMap<>();
	private Map<String, List<@NonNull String[]>> grantedForcedExportsByAspect= new HashMap<>();
	
	// key is aspectId+"->"+baseId, value is array of team names
	private Map<String, Set<String>> deniedTeamsByAspectBinding = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> grantedTeamsByAspectBinding = new HashMap<String, Set<String>>();

	// the workspace directory for storing the state of this plugin
	@Nullable private IPath otequinoxState;
	// back link needed for accessing the state location:
	private Bundle transformerBundle;
	// helper instance needed to stop bundles by name
	@SuppressWarnings("deprecation")
	private org.osgi.service.packageadmin.@Nullable PackageAdmin packageAdmin;
	
	private ForcedExportsDelegate forcedExportsDelegate;
	
	public AspectPermissionManager(Bundle bundle, 
			@SuppressWarnings("deprecation") org.osgi.service.packageadmin.@Nullable PackageAdmin packageAdmin)
	{
		this.transformerBundle = bundle;
		this.packageAdmin = packageAdmin;
		this.forcedExportsDelegate = new ForcedExportsDelegate();
	}

	/* local cache for isReady(): */
	private boolean isWaitingForLocation = true;
	
	/** Before using this permission manager a client must check whether we're ready (instance location set). */
	public boolean isReady() {
		if (!isWaitingForLocation)
			return true;
		try {
			InternalPlatform platform = InternalPlatform.getDefault();
			Location instanceLocation = platform.getInstanceLocation();
			if (!instanceLocation.isSet())
				return false; // not yet capable
			this.isWaitingForLocation = false;
			fetchAspectBindingPermssionsFromWorkspace();
		} catch (NoClassDefFoundError ncdfe) {
			log(IStatus.WARNING, "Optional class InternalPlatform not found, cannot access workspace location");
			this.isWaitingForLocation = false;
			return true;
		}
		if (!this.obligations.isEmpty())
			for (Runnable job : this.obligations)
				job.run();
		return true;
	}

	/** 
	 * Fetch stored permissions from this plugin's workspace state.
	 * 
	 * @pre instance location should be set (see {@link #isReady()}),
	 *    otherwise will silently return without accessing workspace settings. 
	 */
	private void fetchAspectBindingPermssionsFromWorkspace() {
		try {
			IPath state = InternalPlatform.getDefault().getStateLocation(this.transformerBundle, true);
			this.otequinoxState = state;
			internalFetchAspectBindingPermssionsFromWorkspace(state);
		} catch (NoClassDefFoundError ncdfe) {
			log(IStatus.WARNING, "Optional class InternalPlatform not found, cannot access workspace location");
			return;
		}
	}

	/** Load extensions for EP org.eclipse.objectteams.otequinox.aspectBindingNegotiators. */
	public void loadAspectBindingNegotiators(IExtensionRegistry extensionRegistry) {
		IConfigurationElement[] aspectBindingNegotiatorsConfigs = extensionRegistry.getConfigurationElementsFor(
				Constants.TRANSFORMER_PLUGIN_ID, Constants.ASPECT_NEGOTIATOR_EXTPOINT_ID);		
		for (int i = 0; i < aspectBindingNegotiatorsConfigs.length; i++) {
			IConfigurationElement currentNegotiatorConfig = aspectBindingNegotiatorsConfigs[i];
			try {
				Object negotiator = currentNegotiatorConfig.createExecutableExtension("class");
				if (negotiator != null)
					this.negotiators.add(((IAspectRequestNegotiator)negotiator));
			} catch (CoreException e) {
				log(e, "Failed to instantiate extension "+currentNegotiatorConfig);
			}
		}
	}

	/** Delegatee of internal API {@link TransformerPlugin#isDeniedAspectPlugin(String)}. */
	public boolean isDeniedAspectPlugin(String symbolicName) {
		return this.deniedAspects.contains(symbolicName);
	}

	
	/**
	 * Check whether a given aspect requests forced exports from base, 
	 * and whether these requests are granted/denied by checking all available sources.
	 * 
	 * Clients should ask {@link #isReady()} (ie., instance location is set) before calling this method,
	 * otherwise workspace settings have to be silently ignored (any error should be signaled by client).
	 * 
	 * @param aspectId      symbolic name of the aspect bundle
	 * @param baseBundleId  symbolic name of the bound base bundle
     * @param forcedExports any forced exports requested in this aspect binding.
	 * @return whether all requests (if any) have been granted
	 */
	public boolean checkForcedExports(AspectBinding aspectBinding) {
		switch (aspectBinding.forcedExportsPermission) {
			case GRANT: return true;
			case DENY: return false;
			case UNDEFINED: 
				aspectBinding.forcedExportsPermission = internalCheckForcedExports(aspectBinding);
				return aspectBinding.forcedExportsPermission == GRANT;
		}
		return true;
	}
	private AspectPermission internalCheckForcedExports(AspectBinding aspectBinding) {
		IConfigurationElement[] forcedExports = aspectBinding.forcedExports;
		if (forcedExports.length == 0)
			return GRANT;
		
		String aspectId = aspectBinding.aspectPlugin;
		String baseBundleId = aspectBinding.basePluginName; 
		List<@NonNull String[]> deniedForcedExports = getConfiguredForcedExports(aspectId, DENY,  deniedForcedExportsByAspect);
		List<@NonNull String[]> grantedForcedExports= getConfiguredForcedExports(aspectId, GRANT, grantedForcedExportsByAspect);

		// iterate all requested forcedExports to search for a matching permission:
		for (IConfigurationElement forcedExport : forcedExports) { // [0..1] (as defined in the schema)
			String forcedExportsRequest = forcedExport.getValue();
			if (forcedExportsRequest == null)
				continue;
			for (@NonNull String singleForcedExportRequest : forcedExportsRequest.split(","))
			{
				singleForcedExportRequest = singleForcedExportRequest.trim();

				String[] listEntry;
				boolean grantReported = false;
				AspectPermission negotiatedPermission = this.defaultForcedExportPermission;
				
				// DENY by default?
				if (negotiatedPermission == DENY) {
					log(IStatus.ERROR, "Default denial of forced export regarding package "+singleForcedExportRequest+
									   " from bundle "+baseBundleId+" as requested by bundle "+aspectId+"; bundle not activated");
					this.deniedAspects.add(aspectId); // keep for answering the TransformerHook.
					return DENY; // NOPE!					
				}
				
				// DENY from configuration?
				listEntry = findRequestInList(baseBundleId, singleForcedExportRequest, deniedForcedExports);
				if (listEntry != null) {
					log(IStatus.ERROR, "Explicit denial of forced export regarding package "+singleForcedExportRequest+
									   " from bundle "+baseBundleId+" as requested by bundle "+aspectId+"; bundle not activated");
					this.deniedAspects.add(aspectId); // keep for answering the TransformerHook.
					return DENY; // NOPE!
				}

				// GRANT from configuration?
				listEntry = findRequestInList(baseBundleId, singleForcedExportRequest, grantedForcedExports);
				if (listEntry != null) {
					log(IStatus.INFO, "Forced export granted for "+aspectId+": "+singleForcedExportRequest+" (from bundle "+baseBundleId+")");
					grantReported = true;
					grantedForcedExports.remove(listEntry);
					negotiatedPermission = GRANT;
				}

				// default and persistent configuration did not DENY, proceed to the negotiators:
				boolean shouldPersist = false;
				for (IAspectRequestNegotiator negotiator : this.negotiators) {
					AspectBindingRequestAnswer answer = negotiator.checkForcedExport(aspectId, baseBundleId, singleForcedExportRequest, negotiatedPermission);
					if (answer.permission.compareTo(negotiatedPermission) > 0) // increasing priority of answer?
					{ 
						shouldPersist = answer.persistent;
						negotiatedPermission = answer.permission;
						// locally store as default for subsequent requests (not persistent, see below):
						if (answer.allRequests)
							this.defaultForcedExportPermission = negotiatedPermission;
						if (negotiatedPermission == DENY)
							break; // end of discussion.
					}
				}

				// make decision persistent?
				if (shouldPersist && negotiatedPermission != UNDEFINED)
					// FIXME(SH): handle "allRequests":
					persistForcedExportsAnswer(aspectId, baseBundleId, singleForcedExportRequest, negotiatedPermission);
				
				// report:
				if (negotiatedPermission == GRANT) {
					if (!grantReported)
						log(IStatus.INFO, "Negotiation granted forced export for "+aspectId+
										  ": "+singleForcedExportRequest+" (from bundle "+baseBundleId+')');
				} else {
					String verb = "did not grant";
					if (negotiatedPermission == DENY)
						verb = "denied";
					log(IStatus.ERROR, "Negotiation "+verb+" forced export for "+aspectId+
									   ": "+singleForcedExportRequest+" (from bundle "+baseBundleId+")"+
									   ". Aspect is not activated.");
					this.deniedAspects.add(aspectId); // keep for answering the TransformerHook.
					return DENY; // don't install illegal aspect
				}
			}
		}
		if (!grantedForcedExports.isEmpty())
			reportUnmatchForcedExports(aspectId, grantedForcedExports);
		return GRANT;
	}

	/**
	 * Get the forced exports configured for a given aspect bundle with permission <code>perm</code>.
	 * Consult {@link HookConfigurator} and store the result in <code>map</code>.
	 * 
	 * @param aspectId  symbolic name of the aspect in focus
	 * @param perm		are we asking about DENY or GRANT?
	 * @param map		in/out param for storing results from OTStorageHook
	 * @return		 	list of pairs (base bundle x base package)
	 */
	private List<@NonNull String[]> getConfiguredForcedExports( String                          aspectId, 
														AspectPermission 				perm, 
														Map<String, List<@NonNull String[]>> map)
    {
		List<@NonNull String[]> forcedExports= map.get(aspectId);
		if (forcedExports == null) {
			// fetch declarations from config.ini or other locations.
			forcedExports= forcedExportsDelegate.getForcedExportsByAspect(aspectId, perm);
			map.put(aspectId, forcedExports);
		}
		return forcedExports;
	}

	private String @Nullable[] findRequestInList(String baseBundleId, String basePackage, List<String[]> list) {
		for (String[] singleExport : list)
			if (   singleExport[0].equals(baseBundleId)
				&& singleExport[1].equals(basePackage))
			{
				return singleExport;
			}
		return null;
	}

	/**
	 * If the structure of grantedForcedExports is not empty we have mismatches between forced-export declarations.
	 * Report these mismatches as warnings.
	 */
	void reportUnmatchForcedExports(String aspectId, List<@NonNull String[]> unmatchedForcedExports)
	{
		for (String[] export: unmatchedForcedExports) {
			String baseId = export[0];
			String pack   = export[1];
			log(IStatus.WARNING, "Aspect "+aspectId+
							  " does not declare forced export of package "+
							  pack+" from bundle "+baseId+
							  " as declared in config.ini (or system property)");
		}
	}

	/* Simple strategy to append a forced export to a file (existing or to be created). */
	private void persistForcedExportsAnswer(String aspectId, String baseBundleId, String basePackage, AspectPermission negotiatedPermission) 
	{
		IPath state = this.otequinoxState;
		if (state == null) {
			log(IStatus.ERROR, "Can't persist forcedExports permission, no workspace location accessable.");
			return;
		}
		try {
			String fileName = (negotiatedPermission == DENY) ? DENIED_FORCED_EXPORTS_FILE : GRANTED_FORCED_EXPORTS_FILE;
			IPath forcedExportsPath = state.append(fileName);
			File forcedExportsFile = new File(forcedExportsPath.toOSString());
			if (!forcedExportsFile.exists())
				forcedExportsFile.createNewFile();
			try (FileWriter writer = new FileWriter(forcedExportsFile, true)) { // FIXME(SH): consider merge (after decision about file format)
				writer.append('\n');
				writer.append(baseBundleId);
				writer.append("\n[\n\t");
				writer.append(basePackage);
				writer.append(";x-friends:=\"");
				writer.append(aspectId);
				writer.append("\"\n]\n");
				writer.flush();
			}
		} catch (IOException ioe) {
			log(ioe, "Failed to persist negotiation result");
		}
	}
	
	/**
	 * Check the permissions for all given teams.
	 * @param teamsForBase the teams to check
	 * @return the set of denied teams
	 */
	Set<@NonNull TeamBinding> checkAspectPermissionDenial(Collection<TeamBinding> teamsForBase)
	{
		Set<TeamBinding> deniedTeams = new HashSet<TeamBinding>();
		for (TeamBinding teamForBase : teamsForBase) {
			AspectBinding aspectBinding = teamForBase.getAspectBinding();
			String aspectBundleName = aspectBinding.aspectPlugin;
			if (aspectBinding.hasBeenDenied) {
				deniedTeams.add(teamForBase);
			} else {
				if (!checkForcedExports(aspectBinding)) {
					deniedTeams.add(teamForBase);
					stopAspectBundle(aspectBinding, aspectBundleName, "requests unconfirmed forced export(s).");
				} else if (!checkTeamBinding(aspectBundleName, aspectBinding.basePluginName, teamForBase)) {
					deniedTeams.add(teamForBase);
					stopAspectBundle(aspectBinding, aspectBundleName, "requests unconfirmed aspect binding(s).");
				}
			}
		}
		return deniedTeams;
	}

	void stopAspectBundle(AspectBinding aspectBinding, String aspectBundleName, String reason) {
		try {
			aspectBinding.hasBeenDenied = true;
			Bundle aspectBundle = aspectBinding.aspectBundle;
			if (aspectBundle != null) {
				aspectBundle.stop();
				log(IStatus.ERROR, "Stopped bundle "+aspectBundleName+" which "+reason);
			} else {
				log(IStatus.ERROR, "Cannot stop aspect bundle "+aspectBundleName);
			}
		} catch (Throwable t) { // don't let the aspect bundle get by by throwing an unexpected exception!
			log(t, "Failed to stop bundle "+aspectBundleName+" which "+reason);
		}
	}

	/**
	 * Check permission for the aspect binding of one specific team.
	 * 
	 * Clients should ask {@link #isReady()} (ie., instance location is set) before calling this method,
	 * otherwise workspace settings have to be silently ignored (any error should be signaled by client).
 	 * 
	 * @param aspectBundleId
	 * @param baseBundleId
	 * @param teamBinding
	 * @return whether this team is permitted to adapt classes from the given base bundle.
	 */
	boolean checkTeamBinding(String aspectBundleId, String baseBundleId, TeamBinding teamBinding) {
		if (teamBinding.checkedPermission != null)
			return teamBinding.checkedPermission == AspectPermission.GRANT;

		boolean isGranted = internalCheckTeamBinding(aspectBundleId, baseBundleId, teamBinding.teamName);
		teamBinding.checkedPermission = isGranted ? AspectPermission.GRANT : AspectPermission.DENY;
		return isGranted;
	}

	boolean internalCheckTeamBinding(String aspectBundleId, String baseBundleId, String teamClass) 
	{
		boolean shouldReportGrant = false; // grant by default should not be reported
		AspectPermission negotiatedPermission = this.defaultAspectBindingPermission;

		// DENY by default?
		if (negotiatedPermission == DENY) {
			log(IStatus.ERROR, "Default denial of aspect binding regarding base bundle "+baseBundleId+
							   " as requested by bundle "+aspectBundleId+"; bundle not activated");
			this.deniedAspects.add(aspectBundleId); // keep for answering the TransformerHook.
			return false; // NOPE!					
		}

		
		String key = aspectBundleId+"->"+baseBundleId;
		
		// denied from configuration?
		Set<String> deniedTeams = deniedTeamsByAspectBinding.get(key);
		if (deniedTeams != null && !deniedTeams.isEmpty()) {
			if (deniedTeams.contains(teamClass)) {
				log(IStatus.ERROR, "Configured denial of aspect binding regarding base bundle "+baseBundleId+
						   " as requested by bundle "+aspectBundleId+"; bundle not activated");
				deniedAspects.add(aspectBundleId);
				return false;
			}
		}
		
		// granted from configuration?
		Set<String> grantedTeams = grantedTeamsByAspectBinding.get(key);
		if (grantedTeams != null && grantedTeams.contains(teamClass)) {
			negotiatedPermission = GRANT;
			shouldReportGrant = true;
		}
		
		// default and persistent configuration did not DENY, proceed to the negotiators:
		boolean shouldPersist = false;
		String denyingNegotiator = null;
		for (IAspectRequestNegotiator negotiator : this.negotiators) {
			AspectBindingRequestAnswer answer = negotiator.checkAspectBinding(aspectBundleId, baseBundleId, teamClass, negotiatedPermission);
			if (answer.permission.compareTo(negotiatedPermission) > 0) // increasing priority of answer?
			{ 
				shouldPersist = answer.persistent;
				negotiatedPermission = answer.permission;
				shouldReportGrant = negotiatedPermission == GRANT;
				// locally store as default for subsequent requests:
				if (answer.allRequests)
					this.defaultAspectBindingPermission = negotiatedPermission; // FIXME: differentiate: apply to all / all of same aspect bundle

				if (negotiatedPermission == DENY) {
					denyingNegotiator = negotiator.getClass().getName();
					break; // end of discussion.
				}
			}
		}

		// make decision persistent?
		if (shouldPersist && negotiatedPermission != UNDEFINED)
			persistTeamBindingAnswer(aspectBundleId, baseBundleId, teamClass, negotiatedPermission);
		
		// report:
		if (negotiatedPermission == GRANT) {
			if (shouldReportGrant)
				log(IStatus.INFO, "Negotiation granted aspect binding for "+aspectBundleId+
								  " to base bundle "+baseBundleId+" by means of team "+teamClass+'.');
		} else {
			String front = (negotiatedPermission == DENY)
					? "Negotiator "+denyingNegotiator + " denied" 
					: "Negotiation did not grant";
			log(IStatus.ERROR, front+" aspect binding for "+aspectBundleId+
							   " to base bundle "+baseBundleId+" by means of team "+teamClass+
							   ". Aspect is not activated.");
			this.deniedAspects.add(aspectBundleId); // keep for answering the TransformerPlugin.
			return false; // don't install illegal aspect
		}
		return true;
	}

	List<Runnable> obligations = new ArrayList<Runnable>();
	public void addBaseBundleObligations(final List<Team> teamInstances, final Collection<TeamBinding> teamClasses, final BaseBundle baseBundle) {
		schedule(new Runnable() {
			public void run() {
				List<TeamBinding> teamsToRevert = new ArrayList<TeamBinding>();
				// aspect bindings:
				for (TeamBinding teamClass : teamClasses)
					if (!checkTeamBinding(teamClass.getAspectBinding().aspectPlugin, baseBundle.bundleName, teamClass))
						teamsToRevert.add(teamClass);
				if (!teamsToRevert.isEmpty())
					revert(teamsToRevert);
			}
			void revert(List<TeamBinding> teamsToRevert) {
				try {
					Set<Bundle> bundlesToStop = new HashSet<Bundle>();
					for (TeamBinding teamClass : teamClasses) {
						if (teamClass.getActivation() != ActivationKind.NONE) {
							for (Team teamInstance : teamInstances)
								if (teamInstance.getClass() == teamClass.teamClass)
									teamInstance.deactivate(Team.ALL_THREADS);
							// could also check if roles are present already ...
						}
						Bundle aspectBundle = teamClass.getAspectBinding().aspectBundle;
						if (aspectBundle != null)
							bundlesToStop.add(aspectBundle);
					}
					for (Bundle bundle : bundlesToStop) {
						if ((bundle.getState() & (Bundle.STARTING|Bundle.ACTIVE)) != 0) {
							log(IStatus.ERROR, "Stopping aspect bundle "+bundle.getSymbolicName()+" with denied aspect binding(s)");
							bundle.stop();
						}
					}
				} catch (Exception e) {
					log(e, "Failed to revert aspect bundle with denied aspect bindings.");
				}
			}
		});
	}

	void schedule(Runnable job) {
		if (isReady()) // became ready since last query?
			job.run();
		else
			synchronized(obligations) {
				obligations.add(job);
			}
	}
		
	void stopIllegalBundle(String symbolicName) {
		String msgCore = "stop bundle "+symbolicName+" whose requests for forced exports have been denied";
		@SuppressWarnings("deprecation")
		org.osgi.service.packageadmin.PackageAdmin packAdmin = this.packageAdmin;
		if (packAdmin == null) {
			log(IStatus.ERROR, "Needing to "+msgCore+" but package admin is not available");
		} else {
			@SuppressWarnings("deprecation")
			Bundle[] bundles = packAdmin.getBundles(symbolicName, null);
			if (bundles == null)
				log(IStatus.ERROR, "Needing to "+msgCore+" but bundle cannot be retrieved");
			else
				try {
					bundles[0].stop();
				} catch (BundleException e) {
					log(e, "Failed to " + msgCore);
				}
		}
	}

	// ==== File I/O: ====

	private void internalFetchAspectBindingPermssionsFromWorkspace(IPath state) {
		// defaults:
		IPath configFilePath = state.append(NEGOTIATION_DEFAULTS_FILE);
		File configFile = new File(configFilePath.toOSString());		
		if (configFile.exists()) {
			Properties props = new Properties();
			try {
				try (FileInputStream inStream = new FileInputStream(configFile)) {
					props.load(inStream);
				}
				String value = (String) props.get(ASPECT_BINDING_DEFAULT);
				if (value != null)
					try {
						defaultAspectBindingPermission = AspectPermission.valueOf(value);
					} catch (IllegalArgumentException iae) {
						defaultAspectBindingPermission = AspectPermission.DENY;
						log(iae, "Cannot set default aspect permission from file "+NEGOTIATION_DEFAULTS_FILE+", assuming DENY.");
					}
				value = (String) props.get(FORCED_EXPORT_DEFAULT);
				if (value != null)
					try {
						defaultForcedExportPermission = AspectPermission.valueOf(value);
					} catch (IllegalArgumentException iae) {
						defaultForcedExportPermission = AspectPermission.DENY;
						log(iae, "Cannot set default forced exports permission from file "+NEGOTIATION_DEFAULTS_FILE+", assuming DENY.");
					}
			} catch (IOException ioex) {
				log(ioex, "Failed to read configuration file "+configFilePath.toOSString());
			}
		} else {
			try {
				File stateDir = new File(state.toOSString());
				if (!stateDir.exists())
					stateDir.mkdirs();
				configFile.createNewFile();
				writeNegotiationDefaults(configFile);
			} catch (IOException ioex) {
				log(ioex, "Failed to create configuration file "+configFilePath.toOSString());
			}
		}

		// configured grant / deny per team:

		configFilePath = state.append(GRANTED_TEAMS_FILE);
		configFile = new File(configFilePath.toOSString());
		if (configFile.exists())
			parseTeamPermissionFile(grantedTeamsByAspectBinding, configFile);
		
		configFilePath = state.append(DENIED_TEAMS_FILE);
		configFile = new File(configFilePath.toOSString());
		if (configFile.exists())
			parseTeamPermissionFile(deniedTeamsByAspectBinding, configFile);

		// configured grant / denied for forced exports:
		configFilePath = state.append(DENIED_FORCED_EXPORTS_FILE);
		configFile = new File(configFilePath.toOSString());
		if (configFile.exists())
			forcedExportsDelegate.parseForcedExportsFile(configFile, DENY);
		
		configFilePath = state.append(GRANTED_FORCED_EXPORTS_FILE);
		configFile = new File(configFilePath.toOSString());
		if (configFile.exists())
			forcedExportsDelegate.parseForcedExportsFile(configFile, GRANT);
	}

	private void writeNegotiationDefaults(File configFile)
			throws IOException 
	{
		try (FileWriter writer = new FileWriter(configFile)) {
			writer.append(ASPECT_BINDING_DEFAULT+'='+defaultAspectBindingPermission.toString()+'\n');
			writer.append(FORCED_EXPORT_DEFAULT+'='+defaultForcedExportPermission.toString()+'\n');
			writer.flush();
		}
		log(IStatus.INFO, "Created aspect binding defaults file "+configFile.getCanonicalPath());
	}

	private void parseTeamPermissionFile(Map<String, Set<String>> teamsByAspectBinding, File configFile) {
		try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) == '#') continue;
				@NonNull String[] parts = line.split("=");
				if (parts.length == 2) {
					Set<String> teams = new HashSet<String>();
					StringTokenizer teamToks = new StringTokenizer(parts[1], ",");
					while (teamToks.hasMoreElements())
						teams.add(teamToks.nextToken());
					teamsByAspectBinding.put(parts[0], teams);
				}
			}
		} catch (IOException e) {
			log(e, "Failed to read permission file "+configFile.getAbsolutePath());
		}
	}

	private void persistTeamBindingAnswer(String aspectBundleId, String baseBundleId, String teamClass, AspectPermission negotiatedPermission) 
	{
		IPath state = this.otequinoxState;
		if (state != null) {
			Map<String, Set<String>> teamsByAspect = null;
			IPath configFilePath = null;
			switch (negotiatedPermission) {
			case GRANT:
				teamsByAspect = this.grantedTeamsByAspectBinding;
				configFilePath = state.append(GRANTED_TEAMS_FILE);
				break;
			case DENY:
				teamsByAspect = this.deniedTeamsByAspectBinding;
				configFilePath = state.append(DENIED_TEAMS_FILE);
				break;
			default: return; // TODO: also persist UNDEFINED (just to avoid asking again?)
			}
			
			// in fact we store the entire state for the given category (grant / deny)
			// so first insert the new answer into the existing map:
			String key = aspectBundleId+"->"+baseBundleId;
			Set<String> teams = teamsByAspect.get(key);
			if (teams == null)
				teamsByAspect.put(key, teams = new HashSet<String>());
			teams.add(teamClass);

			// now dump the entire map:
			File configFile = new File(configFilePath.toOSString());
			try {
				if (!configFile.exists())
					configFile.createNewFile();
				try (FileWriter writer = new FileWriter(configFile, false)) {
					writer.write("# Aspect permission file generated from aspect negotiation results.\n");
					for (Map.Entry<String, Set<String>> entry : teamsByAspect.entrySet()) {
						writer.append(entry.getKey()).append('=');
						String sep = "";
						for (String t : entry.getValue()) {
							writer.append(sep).append(t);
							sep = ",";
						}
						writer.append('\n');
					}
					writer.flush();
				}
			} catch (IOException ioe) {
				log(ioe, "Failed to persist negotiation result");
			}
		}
	}
}
