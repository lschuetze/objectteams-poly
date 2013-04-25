package org.eclipse.objectteams.internal.osgi.weaving;

import static org.eclipse.objectteams.osgi.weaving.Activator.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.objectteams.internal.osgi.weaving.AspectBindingRegistry.WaitingTeamRecord;
import org.eclipse.objectteams.osgi.weaving.ActivationKind;
import org.eclipse.objectteams.otequinox.hook.ILogger;
import org.objectteams.ITeam;
import org.objectteams.Team;
import org.osgi.framework.Bundle;

/**
 * Each instance of this class represents the fact that a given base bundle has aspect bindings,
 * which require to load / instantiate / activate one or more teams at a suitable point in time.
 */
public class BaseBundleActivation {

	private AspectBindingRegistry aspectBindingRegistry;
	@SuppressWarnings("deprecation")
	private org.osgi.service.packageadmin.PackageAdmin admin;

	private String baseBundleName;
	
	private boolean teamsScanned = false;
	

	public BaseBundleActivation(String bundleSymbolicName, AspectBindingRegistry aspectBindingRegistry, 
			@SuppressWarnings("deprecation") org.osgi.service.packageadmin.PackageAdmin admin) 
	{
		this.baseBundleName = bundleSymbolicName;
		this.aspectBindingRegistry = aspectBindingRegistry;
		this.admin = admin;
	}
	
	/** Signal that the given class is being loaded and trigger any necessary loading/instantiation/activation. */
	public void fire(String className) {
		List<AspectBindingRegistry.WaitingTeamRecord> deferredTeamClasses = new ArrayList<>();
		List<AspectBinding> aspectBindings = aspectBindingRegistry.getAdaptingAspectBindings(baseBundleName);
		if (aspectBindings != null) {
			for (AspectBinding aspectBinding : aspectBindings) {
				if (aspectBinding.activated)
					continue;
				Bundle[] aspectBundles = admin.getBundles(aspectBinding.aspectPlugin, null);
				if (aspectBundles == null || aspectBundles.length == 0) {
					log(IStatus.ERROR, "Cannot find aspect bundle "+aspectBinding.aspectPlugin);
					continue;
				}
				Bundle aspectBundle = aspectBundles[0];
				if (shouldScan())
					scanTeamClasses(aspectBundle, aspectBinding);
				if (loadTeams(aspectBundle, aspectBinding, className, deferredTeamClasses))
//					aspectBinding.activated = true; // FIXME(SH): this still spoils team activation, the given class may not be the trigger
					;
			}
			if (!deferredTeamClasses.isEmpty())
				aspectBindingRegistry.addDeferredTeamClasses(deferredTeamClasses);
		}
	}

	private synchronized boolean shouldScan() {
		boolean shouldScan = !teamsScanned;
		teamsScanned = true;
		return shouldScan;
	}

	/** Read OT attributes of all teams in aspectBinding and collect affected base classes. */
	private void scanTeamClasses(Bundle bundle, AspectBinding aspectBinding) { 
		List<String> allTeams = aspectBinding.getAllTeams();
		ClassScanner scanner = new ClassScanner();
		for (String teamName : allTeams) {
			try {
				scanner.readOTAttributes(bundle, teamName);
				aspectBinding.addBaseClassNames(teamName, scanner.getCollectedBaseClassNames());
			} catch (Exception e) {
				log(e, "Failed to load team class "+teamName);
			}
		}
	}

	/** Team loading, 1st attempt (trying to do all three phases load/instantiate/activate). */
	private boolean loadTeams(Bundle aspectBundle, AspectBinding aspectBinding, String className, List<WaitingTeamRecord> deferredTeamClasses) {
		Collection<String> teamsForBase = aspectBinding.getTeamsForBase(className);
		if (teamsForBase == null) return true;
		TeamLoading delegate = new TeamLoading(deferredTeamClasses);
		for (String teamForBase : teamsForBase) {
			// Load:
			Class<? extends ITeam> teamClass;
			try {
				teamClass = (Class<? extends ITeam>) aspectBundle.loadClass(teamForBase);
			} catch (ClassNotFoundException e) {
				log(e, "Failed to load team "+teamForBase);
				continue;
			}
			// Instantiate?
			ActivationKind activationKind = aspectBinding.getActivation(teamForBase);
			if (activationKind == ActivationKind.NONE)
				continue;
			ITeam teamInstance = delegate.instantiateTeam(aspectBinding, teamClass, teamForBase);
			if (teamInstance == null)
				continue;
			// Activate?
			delegate.activateTeam(aspectBinding, teamForBase, teamInstance, activationKind);
		}
		return !delegate.needDeferring; // TODO, need to figure out whether we're done with aspectBinding.
	}

	/** Team loading, subsequent attempts. */
	public static void instantiateWaitingTeam(WaitingTeamRecord record, List<WaitingTeamRecord> deferredTeams)
			throws InstantiationException, IllegalAccessException 
	{
		ITeam teamInstance = record.teamInstance;
		String teamName = record.getTeamName();
		TeamLoading delegate = new TeamLoading(deferredTeams);
		if (teamInstance == null) {
			// Instantiate (we only get here if activationKind != NONE)
			teamInstance = delegate.instantiateTeam(record.aspectBinding, record.teamClass, teamName);
			if (teamInstance == null)
				return;
		}
		// Activate?
		ActivationKind activationKind = record.aspectBinding.getActivation(teamName);
		delegate.activateTeam(record.aspectBinding, teamName, teamInstance, activationKind);
	}

	/* Common parts for both first and subsequent loading attempts. */
	private static class TeamLoading {
		List<WaitingTeamRecord> deferredTeams;
		boolean needDeferring; // did we record the fact that a team needs deferring?
		
		public TeamLoading(List<WaitingTeamRecord> deferredTeams) {
			this.deferredTeams = deferredTeams;
		}

		@Nullable ITeam instantiateTeam(AspectBinding aspectBinding, Class<? extends ITeam> teamClass, String teamName) {
			try {
				ITeam instance = teamClass.newInstance();
				log(ILogger.INFO, "Instantiated team "+teamName);
				return instance;
			} catch (NoClassDefFoundError ncdfe) {
				needDeferring = true;
				deferredTeams.add(new WaitingTeamRecord(teamClass, aspectBinding, ncdfe.getMessage().replace('/','.')));
			} catch (Throwable e) {
				// application error during constructor execution?
				log(e, "Failed to instantiate team "+teamName);
			}
			return null;
		}
		void activateTeam(AspectBinding aspectBinding, String teamName, ITeam teamInstance, ActivationKind activationKind)
		{
			try {
				switch (activationKind) {
				case ALL_THREADS:
					teamInstance.activate(Team.ALL_THREADS);
					break;
				case THREAD:
					teamInstance.activate();
					break;
					//$CASES-OMITTED$
				}
			} catch (NoClassDefFoundError e) {
				deferredTeams.add(new WaitingTeamRecord(teamInstance, aspectBinding, e.getMessage().replace('/','.'))); // TODO(SH): synchronization
			} catch (Throwable t) {
				// application errors during activation
				log(t, "Failed to activate team "+teamName);
			}
		}
	}
}
