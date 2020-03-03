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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.runtime.AdapterManager;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;
import org.eclipse.jdt.internal.debug.core.model.JDIObjectValue;
import org.eclipse.jdt.internal.debug.core.model.JDIThisVariable;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;

import com.sun.jdi.ThreadReference;

/**
 * JDI representation of a known team instance.
 * @author ike
 */
//TODO(ike): what about launches and thread termination->notify and update _activethreads
public class TeamInstance extends JDIThisVariable implements IAdaptable
{
	/**
	 * Constant for inactive team state.
	 */
	public static final int IS_INACTIVE = 0;
	/**
	 * Constant for a team instance that has been implicitly activated
	 */
	public static final int IS_IMPLICITACTIVE = 1;
	/**
	 * Constant for a team instance that has been explicitly activated
	 */
	public static final int IS_ACTIVE = 2;

	private class TeamThread {
		public long threadID = 0;
		public long time = 0;

		public TeamThread(long threadID, long time) {
			this.threadID = threadID;
			this.time = time;
		}		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TeamThread)
				return (threadID == ((TeamThread)obj).threadID);
			
			return super.equals(obj);
		}	
		@Override
		public String toString() {
			return "threadID="+threadID+",time="+time;  //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
    private List<TeamThread> _activethreads;
    private List<TeamThread> _deactivethreads;
    private List<TeamThread> _implicitActivethreads;
    private int _activationState; // TODO(SH): use for caching
    private TeamThread _globalThread;;
    private long _creationTime;
    private long _activationTime; // captured when asked, fetch value from TeamThread 
    
    
    /**
     * Create a new team instance representation from a JDT variable.
     * @param var variable from which the object reference shall be extracted
     * @throws DebugException
     */
    public TeamInstance(JDIThisVariable var) throws DebugException
    {
    	super((JDIDebugTarget)var.getDebugTarget(),
    		 ((JDIObjectValue)var.getValue()).getUnderlyingObject());
    	_activethreads = new ArrayList<TeamThread>();
    	_deactivethreads = new ArrayList<TeamThread>();
    	_implicitActivethreads = new ArrayList<TeamThread>();
    	_globalThread = new TeamThread(-1, 0);
    	_creationTime = System.currentTimeMillis();
    	_activationTime = 0;
    }
    
    /**
     * Wall-clock time of when the team instance was created.
     * @return time in milliseconds
     */
    public long getCreationTime()
    {
    	return _creationTime;
    }
    
    /**
     * Wall-clock time of when the team instance was (last) activated.
     * @return time in milliseconds
     */
    public long getActivationTime()
    {
    	return _activationTime;
    }

    /**
     * Mark that the team instance has been implicitly activated for a given thread.
     * @param threadID JDI-ID of the thread object
     */
    public void setImplicitActiveForThreadID(long threadID)
    {
    	TeamThread teamThread = new TeamThread(threadID, System.currentTimeMillis());
    	_implicitActivethreads.add(teamThread);
    }
    
    /**
     * Mark that the team instance has been implicitly deactivated for a given thread.
     * @param threadID JDI-ID of the thread object
     */
    public void setImplicitInactiveForThreadID(long threadID)
    {
    	TeamThread teamThread = new TeamThread(threadID, 0);
    	_implicitActivethreads.remove(teamThread);
    }

    /**
     * Mark that the team instance has been explicitly activated for a given thread.
     * @param threadID JDI-ID of the thread object
     */
    public void setActiveForThreadID(long threadID)
    {
    	TeamThread teamThread = new TeamThread(threadID, System.currentTimeMillis());
    	_activethreads.add(teamThread);
    	_deactivethreads.remove(teamThread);
    }
    
    /**
     * Mark that the team instance has been explicitly deactivated for a given thread.
     * @param threadID JDI-ID of the thread object
     */
    public void setInactiveForThreadID(long threadID)
    {
    	TeamThread teamThread = new TeamThread(threadID, System.currentTimeMillis());
    	_activethreads.remove(teamThread);
    	_deactivethreads.add(teamThread);
    }

	public Object getAdapter(Class adapter)
	{
		return AdapterManager.getDefault().getAdapter(this, adapter);
	}
	
	/**
	 * Answer the activation state of the team instance with respect to the given debug element.
	 * When debugElement is either a stackframe or a thread the corresponding thread is used.
	 * Otherwise the global activation state is answered. 
	 * @param debugElement may be used to denote a thread for which activation is queried.
	 * @return activation state encoded as one of {@link #IS_ACTIVE}, {@link #IS_IMPLICITACTIVE} or {@link #IS_INACTIVE}.
	 */
	public int getActivationState(IDebugElement debugElement)
	{
		IThread thread= null;
		if (debugElement instanceof IStackFrame) {
			IStackFrame stackFrame= (IStackFrame)debugElement;
			thread= stackFrame.getThread();
		} else if (debugElement instanceof IThread) {
			thread = (IThread)debugElement;
		} 
		if (thread != null)
			return getActivationState(thread);
		return globalActivationState();
	}

	/**
	 * Answer whether the team instance is active for the given thread
	 * @param thread debug representation of the thread
	 * @return true if the team instance is active for the given thread.
	 */
	public boolean isActiveFor(IThread thread)
	{
		boolean isActive= false;
		ThreadReference threadRef= null;
		if (thread != null) {
			threadRef= ((JDIThread)thread).getUnderlyingThread();
			TeamThread teamThread = new TeamThread(threadRef.uniqueID(), 0);
			isActive = _activethreads.contains(teamThread);
			if (isActive) {
				setActivationTime(_activethreads.get(_activethreads.indexOf(teamThread)).time);
				return true;
			}
			if (_deactivethreads.contains(teamThread))
				return false;
		}

		//team is perhaps global active 
		if(!isActive)
			isActive = (_activethreads.contains(_globalThread));

		return isActive;
	}

	/**
	 * Answer whether the team instance is implicitly active for the given thread
	 * @param thread debug representation of the thread
	 * @return true if the team instance is implicitly active for the given thread.
	 */
	public boolean isImplicitActiveFor(IThread thread)
	{
		ThreadReference threadRef = ((JDIThread)thread).getUnderlyingThread();
		long threadID = threadRef.uniqueID();
		TeamThread teamThread= new TeamThread(threadID, 0);
		boolean isImplicitActive = _implicitActivethreads.contains(teamThread);
		
		if(isImplicitActive)
		{
			setActivationTime(_implicitActivethreads.get(_implicitActivethreads.indexOf(teamThread)).time);
		}
		
		return isImplicitActive;
	}
	
	/**
	 * Store the value of the pseudo thread org.objectteams.Team.ALL_THREADS.
	 * @param globalThreadID JDI object ID of the constant value
	 */
	public void setGlobalThreadID(long globalThreadID)
	{
		_globalThread.threadID = globalThreadID;
	}

	private int getActivationState(IThread thread)
	{
		if(isActiveFor(thread))
			return TeamInstance.IS_ACTIVE;
		else
			if(isImplicitActiveFor(thread))
				return TeamInstance.IS_IMPLICITACTIVE;
			else
				return TeamInstance.IS_INACTIVE;
	}

	private  int globalActivationState() {
		if (_activethreads.contains(_globalThread))
			return IS_ACTIVE;
		return IS_INACTIVE;
	}

	private void setActivationTime(long time)
	{
		_activationTime = time;
	}
}