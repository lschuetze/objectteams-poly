package org.eclipse.objectteams.otredyn.runtime.dynamic.linker;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.objectteams.otredyn.runtime.TeamManager;
import org.objectteams.ITeam;

public class CallSiteContext implements Iterable<ITeam> {

	public static Map<String, CallSiteContext> contexts = new ConcurrentHashMap<>();

	private ITeam[] teams;
	private int[] callinIds;
	private int index;
	public final int bmId;
	public final int joinpointId;
	public final String joinpointDescr;
	public final Class<?> baseClass;

	public CallSiteContext(String joinpointDescr, int bmId, Class<?> baseClass) {
		this.joinpointDescr = joinpointDescr;
		this.bmId = bmId;
		this.index = 0;
		this.baseClass = baseClass;
		this.joinpointId = TeamManager.getJoinpointId(joinpointDescr);
	}

	public void updateTeams() {
		this.teams = TeamManager.getTeams(joinpointId);
		this.callinIds = TeamManager.getCallinIds(joinpointId);
	}

	public void resetIndex() {
		index = 0;
	}
	
	public ITeam[] getTeams() {
		return teams;
	}
	
	public int getIndex() {
		return index;
	}

	@Override
	public Iterator<ITeam> iterator() {
		return new CallSiteContextIterator();
	}

	public int nextCallinId() {
		return callinIds[index - 1];
	}

	private class CallSiteContextIterator implements Iterator<ITeam> {

		@Override
		public boolean hasNext() {
			return index < teams.length - 1;
		}

		@Override
		public ITeam next() {
			return teams[index++];
		}

	}

}
