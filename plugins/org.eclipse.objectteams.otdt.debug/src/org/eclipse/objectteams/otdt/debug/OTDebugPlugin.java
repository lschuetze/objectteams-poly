/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
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
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug;

import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jdt.internal.debug.core.JDIDebugPlugin;
import org.eclipse.objectteams.otdt.debug.internal.Logger;
import org.eclipse.objectteams.otdt.debug.internal.OTDebugElementsContainerFactory;
import org.eclipse.objectteams.otdt.debug.internal.RoleBreakpointListener;
import org.eclipse.objectteams.otdt.debug.internal.StepFromLinenumberGenerator;
import org.eclipse.objectteams.otdt.debug.internal.TeamBreakpointListener;
import org.eclipse.objectteams.otdt.debug.internal.TempFileManager;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class.
 * @noinstantiate clients are not supposed to instantiate this class.
 * @noextend clients are not supposed to extend this class.
 */
public class OTDebugPlugin extends Plugin 
{
	/** ID of this plugin */
	public static final String PLUGIN_ID = "org.eclipse.objectteams.otdt.debug"; //$NON-NLS-1$
	/**
	 * Key of a boolean launch configuration attribute. The value denotes whether a launch is OT/J enabled.
	 */
	public static final String OT_LAUNCH = "org.eclipse.objectteams.launch"; //$NON-NLS-1$
    
    private OTDebugElementsContainerFactory _containerFactory;

    class OTDebugLaunchManager implements ILaunchesListener2
	{
        private Vector<ILaunch> _otLaunches;
        
		public OTDebugLaunchManager()
		{
			ILaunch[] launches = DebugPlugin.getDefault().getLaunchManager().getLaunches();
			
			if (launches != null)
			{
				_otLaunches = new Vector<ILaunch>();
				for (int i = 0; i < launches.length; i++) 
				{
                    if (isOTDebugLaunch(launches[i]))
                        _otLaunches.add(launches[i]);
				}
				checkRegisterOTDebugSupport(_otLaunches.size());
			}
		}
		
		public void terminateOTLaunches()
		{
		    // operate on a copy of _otLaunches to prevent concurrent modification
		    ILaunch[] launches = new ILaunch[_otLaunches.size()];
		    _otLaunches.copyInto(launches);
		    
		    for (int i = 0; i < launches.length; i++)
            {
                try {
	                launches[i].terminate();
                }
                catch (DebugException ex) { // only log
                    OTDebugPlugin.logException("Unable to terminate launch on bundle shutdown", ex); //$NON-NLS-1$
                }
            }
		}
		
		public int getOTLaunchesCount() 
		{
			return _otLaunches.size();
		}

		private boolean isOTDebugLaunch(ILaunch launch)
		{
			try 
			{
				if (ILaunchManager.DEBUG_MODE.equals(launch.getLaunchMode())) { 
					String isOTLaunch = launch.getAttribute(OT_LAUNCH);
					if (isOTLaunch != null && isOTLaunch.equals("true")) //$NON-NLS-1$
						return true;
					if (launch.getLaunchConfiguration() != null) 
						return launch.getLaunchConfiguration().getAttribute(OT_LAUNCH, false);
				}
			}
			catch (CoreException ex)
			{}
			return false;
		}

        public void launchesAdded(ILaunch[] launches)
        {
            for (int idx = 0; idx < launches.length; idx++)
            {
                ILaunch launch = launches[idx];
                if (isOTDebugLaunch(launch) && !_otLaunches.contains(launch))
                {
                    _otLaunches.add(launch);
                    checkRegisterOTDebugSupport(_otLaunches.size());
                }
            }
        }
        
        public void launchesTerminated(ILaunch[] launches)
        {
            forgetOTLaunches(launches);
        }
        
        private void forgetOTLaunches(ILaunch[] launches)
        {
            for (int idx = 0; idx < launches.length; idx++)
            {
                ILaunch launch = launches[idx];
                if (isOTDebugLaunch(launch) && _otLaunches.contains(launch))
                {
                    _otLaunches.remove(launch);
                    checkRegisterOTDebugSupport(_otLaunches.size());
                    otLaunchFinished(launch);
                }
            }
            assert(_otLaunches.size() >= 0);
        }

        //we're not interested in this
        public void launchesRemoved(ILaunch[] launches){}
        public void launchesChanged(ILaunch[] launches){}
	}
	
	private void otLaunchFinished(ILaunch launch)
    {
	    if (_tempFileManager != null) // we're really cautious today
	        _tempFileManager.deleteTempFile(launch);
    }
	
    //The shared instance.
	private static OTDebugPlugin plugin;
	
	private OTDebugLaunchManager _otLaunchManager;
	private TeamBreakpointListener _otTeamBreakpointListener;
	private RoleBreakpointListener _otRoleBreakpointListener;
    private IOTDebugEventListener[] _listeners = new IOTDebugEventListener[0];
    private TempFileManager _tempFileManager;
    private StepFromLinenumberGenerator _stepGenerator;
    private String _callinSteppingConfig = null;
    
	public OTDebugPlugin() 
	{
		super();
		plugin = this;
	}
	
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);

		_otLaunchManager = new OTDebugLaunchManager();
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(_otLaunchManager);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception 
	{
		// when this plugin is stopped, terminate any running OT launches
	    unregisterOTDebugSupport();
	    _otLaunchManager.terminateOTLaunches();
		
		DebugPlugin.getDefault().getLaunchManager().removeLaunchListener(_otLaunchManager);
		_otLaunchManager = null;
		if (_tempFileManager != null)
		{
			_tempFileManager.deleteAll();
			_tempFileManager = null;
		}
		
		super.stop(context);
	}

    /**
	 * Returns the shared instance.
	 */
	public static OTDebugPlugin getDefault() {
		return plugin;
	}

	/**
	 * Configure the callin stepping mode. The value will be passed to the VM using
	 * {@link OTVMRunnerAdaptor#OT_DEBUG_CALLIN_STEPPING_VMARG} and will be interpreted by the OTRE.
	 * @param config a comma separated list (subset) of these tokens: "role", "recurse", "orig"
	 */
	public void setCallinSteppingConfig(String config) {
		this._callinSteppingConfig = config;
	}
	/**
	 * Answer the configured callin stepping mode. The value is suitable for being passed 
	 * to the VM using {@link OTVMRunnerAdaptor#OT_DEBUG_CALLIN_STEPPING_VMARG}.
	 * @param config a comma separated list (subset) of these tokens: "role", "recurse", "orig"
	 */
	public String getCallinSteppingConfig() {
		return this._callinSteppingConfig;
	}

	/**
	 * Create an error status marked as originating from this plugin.
	 * @param message the error message
	 * @return a status instance
	 */
	public static IStatus createErrorStatus(String message)
	{
	    return new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, null);
	}

	/**
	 * Create an error status marked as originating from this plugin.
	 * @param message the error message
	 * @param exception an exception to associate with the status.
	 * @return a status instance
	 */
	public static IStatus createErrorStatus(String message, Throwable exception)
	{
	    return new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, exception);
	}

	/**
	 * Log an exception marked as originating from this plugin.
	 * @param message
	 * @param exception
	 */
	public static void logException(String message, Throwable exception) {
		plugin.getLog().log(createErrorStatus(message, exception));
	}

	TempFileManager getTempFileManager()
    {
	    if (_tempFileManager == null)
	        _tempFileManager = new TempFileManager();
	    
        return _tempFileManager;
    }

	/**
	 * Get the registered debug event listeners.
	 * @return the stored array of listeners (unprotected)
	 */
	public IOTDebugEventListener[] getOTDebugEventListeners()
    {
        return _listeners;
    }

	/**
	 * Add a listener that will be called when a team-relevant breakpoint was hit.
	 * @param listener
	 */
	public void addOTDebugEventListener(IOTDebugEventListener listener)
	{
	    int newLength = _listeners.length + 1;
	    IOTDebugEventListener[] newListeners = new IOTDebugEventListener[newLength];
	    System.arraycopy(_listeners, 0, newListeners, 0, _listeners.length);
	    newListeners[_listeners.length] = listener;
	    _listeners = newListeners;
	}

	/**
	 * remove the given debug event listener.
	 * @param listener
	 */
	public void removeOTDebugEventListener(IOTDebugEventListener listener)
	{
	    int occurrences = 0;
	    for (int i = 0; i < _listeners.length; i++)
        {
            if (listener.equals(_listeners[i]))
                occurrences++;
        }

	    if (occurrences > 0)
	    {
		    int newLength = _listeners.length - occurrences;
		    IOTDebugEventListener[] newListeners = new IOTDebugEventListener[newLength];

		    int insertionIndex = 0;
		    for (int i = 0; i < _listeners.length; i++)
	        {
	            if (!listener.equals(_listeners[i]))
	                newListeners[insertionIndex++] = _listeners[i];
	        }

	        _listeners = newListeners;
	    }
	}
	
	private void checkRegisterOTDebugSupport(int otLaunchCount)
	{
		// Note: the order seems to be undefined! After finishing a launch, we do not 
		// immediately get the launchRemoved event. We may first get another launchAdded 
		// and then the previous launchRemoved. So we can't rely on the counter being 
		// 0 or 1 here.
		if (otLaunchCount <= 0)
		{
			unregisterOTDebugSupport();
		}
		else if (_otTeamBreakpointListener == null)
			registerOTDebugSupport();
	}

	private void registerOTDebugSupport() 
	{
		assert(_otTeamBreakpointListener == null);
		
		 _containerFactory= new OTDebugElementsContainerFactory();
		Platform.getAdapterManager().registerAdapters(_containerFactory, ILaunch.class);
		
		_otTeamBreakpointListener = TeamBreakpointListener.getInstance();
		_otRoleBreakpointListener = RoleBreakpointListener.getInstance();
		_stepGenerator = StepFromLinenumberGenerator.getInstance();

		DebugPlugin.getDefault().addDebugEventFilter(_stepGenerator);
		JDIDebugPlugin.getDefault().addJavaBreakpointListener(_otTeamBreakpointListener);
		JDIDebugPlugin.getDefault().addJavaBreakpointListener(_otRoleBreakpointListener);
	}

	private void unregisterOTDebugSupport() 
	{
		if (_otTeamBreakpointListener != null)
		{
			DebugPlugin.getDefault().removeDebugEventFilter(_stepGenerator);
			JDIDebugPlugin.getDefault().removeJavaBreakpointListener(_otTeamBreakpointListener);
			JDIDebugPlugin.getDefault().removeJavaBreakpointListener(_otRoleBreakpointListener);
			try
            {
                OTBreakpointInstaller.uninstallTeamBreakpoints();
            }
            catch (CoreException e)
            {
                Logger.log(0,"OTDebugPlugin.unregisterOTDebugSupport()","ERROR unable to remove Breakpoints"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            _otTeamBreakpointListener.dispose();
            _otTeamBreakpointListener = null;

            _otRoleBreakpointListener.dispose();
            _otRoleBreakpointListener = null;
		}
        if (_containerFactory != null)
           	_containerFactory.dispose();
        _containerFactory= null;
	}
}
