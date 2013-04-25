package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.osgi.weaving.Activator.log;
import static org.eclipse.objectteams.osgi.weaving.Constants.ACTIVATION;
import static org.eclipse.objectteams.osgi.weaving.Constants.ASPECT_BINDING_EXTPOINT_ID;
import static org.eclipse.objectteams.osgi.weaving.Constants.BASE_PLUGIN;
import static org.eclipse.objectteams.osgi.weaving.Constants.CLASS;
import static org.eclipse.objectteams.osgi.weaving.Constants.ID;
import static org.eclipse.objectteams.osgi.weaving.Constants.SELF;
import static org.eclipse.objectteams.osgi.weaving.Constants.SUPERCLASS;
import static org.eclipse.objectteams.osgi.weaving.Constants.TEAM;
import static org.eclipse.objectteams.osgi.weaving.Constants.TRANSFORMER_PLUGIN_ID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.osgi.weaving.Constants;
import org.eclipse.objectteams.otequinox.hook.IAspectRegistry;
import org.eclipse.objectteams.otequinox.hook.ILogger;
import org.objectteams.ITeam;
import org.osgi.framework.Bundle;
import org.osgi.service.packageadmin.PackageAdmin;

@NonNullByDefault
public class AspectBindingRegistry {
	
	private static List<String> KNOWN_OTDT_ASPECTS = new ArrayList<String>();
	static {
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.jdt.ui");
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.compiler.adaptor");
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.refactoring");
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.pde.ui");
		KNOWN_OTDT_ASPECTS.add("org.eclipse.objectteams.otdt.samples");
	}
	/** main internal registry of aspect bindings. */
	private static HashMap<String, ArrayList<AspectBinding>> aspectBindingsByBasePlugin = 
			   new HashMap<String, ArrayList<AspectBinding>>();
	private static HashMap<String, ArrayList<AspectBinding>> aspectBindingsByAspectPlugin = 
		       new HashMap<String, ArrayList<AspectBinding>>();
	// map base bundle name to list of adapted base class names
	private static HashMap<String, ArrayList<String>> adaptedBaseClassNames =
		       new HashMap<String, ArrayList<String>>();
	// set of aspect plug-ins which have internal teams:
	private Set<String> selfAdaptingAspects= new HashSet<String>();
	
	private HashMap<String, BaseBundleActivation> baseTripWires = new HashMap<>();

	static class WaitingTeamRecord {
		@Nullable Class<? extends ITeam> teamClass;
		@Nullable ITeam teamInstance;
		AspectBinding aspectBinding;
		String notFoundClass;
		
		public WaitingTeamRecord(Class<? extends ITeam> teamClass, AspectBinding aspectBinding, String notFoundClass) {
			this.teamClass = teamClass;
			this.aspectBinding = aspectBinding;
			this.notFoundClass = notFoundClass;
		}
		public WaitingTeamRecord(ITeam teamInstance, AspectBinding aspectBinding, String notFoundClass) {
			this.teamInstance = teamInstance;
			this.aspectBinding = aspectBinding;
			this.notFoundClass = notFoundClass;
		}
		public WaitingTeamRecord(WaitingTeamRecord record, String notFoundClass) {
			this.teamClass = record.teamClass;
			this.teamInstance = record.teamInstance;
			this.aspectBinding = record.aspectBinding;
			this.notFoundClass = notFoundClass;
		}
		public @Nullable String getTeamName() {
			final Class<? extends ITeam> clazz = teamClass;
			if (clazz != null) {
				return clazz.getName();
			} else {
				final ITeam instance = teamInstance;
				if (instance != null)
					return instance.getClass().getName();
			}
			return "<unknown team>";
		}		
	}
	// records of teams that have been deferred due to unresolved class dependencies:
	private List<WaitingTeamRecord> deferredTeams = new ArrayList<>();
	// records of teams whose class dependencies should/could be unblocked by now:
	private List<WaitingTeamRecord> scheduledTeams = new ArrayList<>();

	public static boolean IS_OTDT = false;
	
	public boolean isOTDT() {
		return IS_OTDT;
	}

	/* Load extensions for org.eclipse.objectteams.otequinox.aspectBindings and check aspect permissions. */
	public void loadAspectBindings(@Nullable PackageAdmin packageAdmin) {
		IConfigurationElement[] aspectBindingConfigs = RegistryFactory.getRegistry().getConfigurationElementsFor(
				TRANSFORMER_PLUGIN_ID, ASPECT_BINDING_EXTPOINT_ID);
		
		for (int i = 0; i < aspectBindingConfigs.length; i++) {
			IConfigurationElement currentBindingConfig = aspectBindingConfigs[i];

			//aspect:
			@SuppressWarnings("null")@NonNull String aspectBundleId= currentBindingConfig.getContributor().getName();
			IS_OTDT |= KNOWN_OTDT_ASPECTS.contains(aspectBundleId);
			if (packageAdmin != null) {
				Bundle[] aspectBundles = packageAdmin.getBundles(aspectBundleId, null);
				if (aspectBundles == null || aspectBundles.length == 0 || (aspectBundles[0].getState() < Bundle.RESOLVED)) {
					log(ILogger.ERROR, "aspect bundle "+aspectBundleId+" is not resolved - not loading aspectBindings.");
					continue;
				}
			}
			
			//base:
			IConfigurationElement[] basePlugins = currentBindingConfig.getChildren(BASE_PLUGIN);
			if (basePlugins.length != 1) {
				log(ILogger.ERROR, "aspectBinding of "+aspectBundleId+" must declare exactly one basePlugin");
				continue;
			}
			String baseBundleId = basePlugins[0].getAttribute(ID);
			
// FIXME(SH):
//			//base fragments?
//			IConfigurationElement[] fragments = basePlugins[0].getChildren(REQUIRED_FRAGMENT);
//			if (fragments != null && !checkRequiredFragments(aspectBundleId, baseBundleId, fragments)) // reported inside
//				continue;
			
			AspectBinding binding = new AspectBinding(aspectBundleId, baseBundleId, basePlugins[0].getChildren(Constants.FORCED_EXPORTS_ELEMENT));
			// TODO(SH): maybe enforce that every bundle id is given only once?

			//teams:
			IConfigurationElement[] teams = currentBindingConfig.getChildren(TEAM);
			binding.initTeams(teams.length);
			try {
				for (int j = 0; j < teams.length; j++) {
					String teamClass = teams[j].getAttribute(CLASS);
					binding.teamClasses[j] = teamClass;
					String activation = teams[j].getAttribute(ACTIVATION);
					binding.setActivation(j, activation);
				}
				
				@NonNull String realBaseBundleId = baseBundleId.toUpperCase().equals(SELF) ? aspectBundleId : baseBundleId;
				addBindingForBaseBundle(realBaseBundleId, binding);
				addBindingForAspectBundle(aspectBundleId, binding);
				if (!baseTripWires.containsKey(realBaseBundleId))
					baseTripWires.put(realBaseBundleId, new BaseBundleActivation(realBaseBundleId, this, packageAdmin));

				
				// now that binding.teamClasses is filled connect to super team, if requested:
				for (int j = 0; j < teams.length; j++) {
					String superTeamName = teams[j].getAttribute(SUPERCLASS);
					if (superTeamName != null)
						addSubTeam(aspectBundleId, binding.teamClasses[j], superTeamName);
				}
				log(ILogger.INFO, "registered:\n"+binding);
			} catch (Throwable t) {
				log(t, "Invalid aspectBinding extension");
			}
		}
	}
	

	private static void addBindingForBaseBundle(String baseBundleId, AspectBinding binding) {
		ArrayList<AspectBinding> bindingList = aspectBindingsByBasePlugin.get(baseBundleId);
		if (bindingList == null) {
			bindingList = new ArrayList<AspectBinding>();
			aspectBindingsByBasePlugin.put(baseBundleId, bindingList);
		}
		bindingList.add(binding);
	}

	private void addBindingForAspectBundle(String aspectBundleId, AspectBinding binding) {
		ArrayList<AspectBinding> bindingList = aspectBindingsByAspectPlugin.get(aspectBundleId);
		if (bindingList == null) {
			bindingList = new ArrayList<AspectBinding>();
			aspectBindingsByAspectPlugin.put(aspectBundleId, bindingList);
		}
		bindingList.add(binding);
		if (binding.basePlugin.toUpperCase().equals(SELF))
			selfAdaptingAspects.add(aspectBundleId);
	}
	
	/**
	 * Record a sub-class relationship of two teams within the same aspect bundle.
	 * 
	 * @param aspectBundleId
	 * @param subTeamName (nullable only until we have JSR 308)
	 * @param teamName
	 */
	private void addSubTeam(String aspectBundleId, @Nullable String subTeamName, String teamName) {
		ArrayList<AspectBinding> bindingList = aspectBindingsByAspectPlugin.get(aspectBundleId);
		if (bindingList == null) {
			Exception e = new Exception("No such aspect binding");
			log(e, "Class "+teamName+" not registered (declared to be superclass of team "+subTeamName);
		} else {
			for (AspectBinding binding : bindingList)
				if (binding.teamClasses != null)
					for (int i=0; i < binding.teamClasses.length; i++) 
						if (binding.teamClasses[i].equals(teamName)) {
							if (binding.subTeamClasses[i] == null)
								binding.subTeamClasses[i] = new ArrayList<String>();
							binding.subTeamClasses[i].add(subTeamName);
							return;
						}
			Exception e = new Exception("No such aspect binding");
			log(e, "Class "+teamName+" not registered(2) (declared to be superclass of team "+subTeamName);
		}		
	}


	/**
	 * Internal API for TransformerHook:
	 * see {@link IAspectRegistry#getAdaptedBasePlugins(Bundle)}
	 */
	public @Nullable String[] getAdaptedBasePlugins(Bundle aspectBundle) {
		ArrayList<AspectBinding> bindings = aspectBindingsByAspectPlugin.get(aspectBundle.getSymbolicName());
		if (bindings == null) return null;
		String[] basePlugins = new String[bindings.size()];
		for (int i=0; i<basePlugins.length; i++) {
			basePlugins[i] = bindings.get(i).basePlugin;
		}
		return basePlugins;
	}

	/** Is `symbolicName' the name of a base plugin for which an adapting team is registered? */
	public boolean isAdaptedBasePlugin(@Nullable String symbolicName) {
		ArrayList<AspectBinding> list = aspectBindingsByBasePlugin.get(symbolicName);
		return list != null && !list.isEmpty();
	}
	
	/**
	 * public API:
	 * {@link IAspectRegistry#getAdaptingAspectPlugins(Bundle)} 
	 */
	public String[] getAdaptingAspectPlugins(Bundle basePlugin) {
		return getAdaptingAspectPlugins(basePlugin.getSymbolicName());
	}
	/**
	 * public API:
  	 * Get the names of aspect plugins adapting a given base plugin.
	 * @param basePluginName symbolic name of a base plugin.
	 * @return non-null array of symbolic names of aspect plugins.
	 */
	public String[] getAdaptingAspectPlugins(@Nullable String basePluginName) {
		ArrayList<AspectBinding> list = aspectBindingsByBasePlugin.get(basePluginName);
		
		if (list == null)
			return new String[0];
		
		String[] aspects = new String[list.size()];
		for (int i=0; i< list.size(); i++) 
			aspects[i] = list.get(i).aspectPlugin;
		
		return aspects;
	}
	
	/**
	 * Get the list of aspect bindings affecting the given base plugin.
	 */
	public @Nullable List<AspectBinding> getAdaptingAspectBindings(@Nullable String basePluginName) {
		return aspectBindingsByBasePlugin.get(basePluginName);
	}
	
	/**
     * Recored the names of base classes adapted by a given team from a given aspect bundle.
	 */
	public void storeAdaptedBaseClassNames(String aspectBundleName, String teamName, Collection<String> baseClassNames) 
	{
		// search the base plugin being adapted by the given team:
		String basePlugin = null;
		bindings:
		for (AspectBinding aspectBinding : aspectBindingsByAspectPlugin.get(aspectBundleName)) {
			for (String aspectTeamClass : aspectBinding.teamClasses)
				if (aspectTeamClass.equals(teamName)) {
					basePlugin = aspectBinding.basePlugin;
					break bindings;
				}
		}
		if (basePlugin == null && selfAdaptingAspects.contains(aspectBundleName))
			basePlugin = aspectBundleName;
		if (basePlugin == null) {
			log(ILogger.ERROR, "Base plugin for team "+teamName+" from "+aspectBundleName+" not found!");
			return;
		}
		// merge base class names into existing:
		synchronized (adaptedBaseClassNames) {
			ArrayList<String> baseBundleClassNames = adaptedBaseClassNames.get(basePlugin);
			if (baseBundleClassNames == null) {
				baseBundleClassNames = new ArrayList<String>();
				adaptedBaseClassNames.put(basePlugin, baseBundleClassNames);
			}
			baseBundleClassNames.addAll(baseClassNames);
		}
	}

	/** Check if the given base bundle / base class mandate any loading/instantiation/activation of teams. */
	public void triggerLoadingHooks(@Nullable String bundleName, @Nullable String className) {
		BaseBundleActivation activation = baseTripWires.get(bundleName);
		if (activation != null)
			activation.fire(className);
	}

	/** Record the given team classes as waiting for instantiation/activation. */
	public synchronized void addDeferredTeamClasses(List<WaitingTeamRecord> teamClasses) {
		deferredTeams.addAll(teamClasses);		
	}

	/**
	 * Check if the given class has been recorded as not-found before,
	 * If so, unblock the team class(es) that depend on this class
	 */
	public synchronized void scheduleTeamClassesFor(@Nullable String className) {
		List<WaitingTeamRecord> currentList = deferredTeams;
		deferredTeams = new ArrayList<>();
		for (WaitingTeamRecord record : currentList) {
			if (record.notFoundClass.equals(className))
				scheduledTeams.add(record);
			else
				deferredTeams.add(record);
		}
	}

	/**
	 * Try to instantiate/activate any deferred teams that have been unblocked by now.
	 */
	public void instantiateScheduledTeams() {
		List<WaitingTeamRecord> currentList;
		synchronized (this) {			
			currentList = scheduledTeams;
			scheduledTeams = new ArrayList<>();
		}
		for(WaitingTeamRecord record : currentList) {
			try {
				BaseBundleActivation.instantiateWaitingTeam(record, deferredTeams);
			} catch (Exception e) {
				log(e, "Failed to instantiate team "+record.getTeamName());
				continue;
			}
		}
	}
}
