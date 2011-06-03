/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2003, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: RoleHierarchieAnalyzer.java 23416 2010-02-03 19:59:31Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.compiler.lifting;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.objectteams.otdt.core.compiler.Pair;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.RoleModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.TeamModel;

/**
 * step 3 (hierarchie analysis) of smart lifting algorithm
 * step 3 = step 4 + step 5
 * @author brcan ,kaschja
 * @version $Id: RoleHierarchieAnalyzer.java 23416 2010-02-03 19:59:31Z stephan $
 */
public class RoleHierarchieAnalyzer
{
	private ProblemReporter _problemReporter;
	private TypeDeclaration _teamTypeDeclaration;

	public RoleHierarchieAnalyzer(TypeDeclaration teamTypeDeclaration, ProblemReporter problemReporter)
	{
		this._teamTypeDeclaration = teamTypeDeclaration;
		this._problemReporter = problemReporter;
	}

	/**
	 * step 3 of smart lifting algorithm
	 * @param role the role whose sub-hierarchie is analyzed
	 * @return the roles which are filtered by the analysis
	 */
	public RoleModel[] analyze(TreeNode role)
	{
		Set<RoleModel> relevantRoles = getRelevantRoles(role);
        checkInstantiability(relevantRoles);
    	detectLiftingAmbiguity(relevantRoles);
		RoleModel[] resultArray = relevantRoles.toArray(new RoleModel[relevantRoles.size()]);
		role.getTreeObject().setSubRoles(resultArray);
		return resultArray;
	}

	/**
	 * step 4 of smart lifting algorithm (elimination of irrelevant roles)
	 * @param role the role whose sub-hierarchie is analyzed
	 * @return vector of RoleTreeObjects
	 */
	private Set<RoleModel> getRelevantRoles(TreeNode role)
	{
		HashSet<RoleModel> result   = new HashSet<RoleModel>();
		TreeNode[] 	  children = role.getChildren();
		RoleModel     parent   = role.getTreeObject();

		// stop recursion if the role has no children
		if (children == null)
		{
            if (!parent.hasBaseclassProblem())
			    result.add(parent);
			return result;
		}

        if (isRelevant(parent, children))
        	result.add(parent);

		// invoke this very method recursively for every child
	    for (int idx = 0; idx < children.length; idx++)
        {
	    	Set<RoleModel> relevantChildren = getRelevantRoles(children[idx]);
            if (!relevantChildren.isEmpty())
			    result.addAll(relevantChildren);
	    }

		return result;
	}

	public void checkInstantiability(Set<RoleModel> relevantRoles) {
        TeamModel teamModel = null;
        List<RoleModel> irrelevant = new ArrayList<RoleModel>();
        for (RoleModel role : relevantRoles) {
            if (teamModel == null)
                teamModel = role.getTeamModel();
            if (role.getBinding().isAbstract()) {
                if (!teamModel.getBinding().isAbstract()) {
                	if (   role.getBinding().isPublic()
                        && !role.getBinding().isInterface())
                		// public role classes could be instantiated from any part of the program,
                		// Force the team to be abstract, too:
                        this._problemReporter.abstractRelevantRole(role, teamModel);
                    else if (role.getBaseTypeBinding().isAbstract())
                    	// if base is abstract, too, we could be lucky, just warn:
                        this._problemReporter.abstractPotentiallyRelevantRole(role, teamModel);
                    else
                        this._problemReporter.abstractRelevantRole(role, teamModel);
                }
                irrelevant.add(role);
            }
        }
        relevantRoles.removeAll(irrelevant);
    }

	/**
	 * analyze whether a role has children with the same "playedBy" relation
	 * @param parent the current role under consideration
	 * @param children known children of parent
	 * @return true if the role is relevant for lifting, i.e., has no children which
	 * 			inherit the playedBy binding unmodified.
	 */
    private boolean isRelevant(RoleModel parent, TreeNode[] children)
    {
    	ReferenceBinding parentBaseType = parent.getBaseTypeBinding();

    	// check the "playedBy" relation for each sub-role class
    	// whether it is identical to the parent class
    	for (int idx = 0; idx < children.length; idx++)
        {
            RoleModel child = children[idx].getTreeObject();
            if (child.hasBaseclassProblem())
                continue;
            if(child.getBaseTypeBinding() == parentBaseType)
            	return false; // not relevant, child can be used for lifting
        }
    	// if the role has no children with the same "playedBy"
    	// relations, it is relevant
       	return true;
    }

	/**
	 * step 5 of smart lifting algorithm (foldBaseClasses)
	 * @param roles vector of RoleModels (result of getRelevantRoles())
	 */
	private void detectLiftingAmbiguity(Set<RoleModel> roles)
	{
		if ((roles == null) || roles.isEmpty())
			return;

		Set<RoleModel> rolesToAnalyze = new HashSet<RoleModel>();

		Set<RoleModel> ambiguitySet   = new HashSet<RoleModel>();

        Iterator<RoleModel> iterator = roles.iterator();
		RoleModel role 	= iterator.next();
        ReferenceBinding baseBinding = role.getBaseTypeBinding();

        ambiguitySet.add(role);

        // filter roles with ambiguous base to role bindings
        while (iterator.hasNext())
        {
            RoleModel  otherRole = iterator.next();
        	ReferenceBinding otherBase = otherRole.getBaseTypeBinding();

        	// check whether both roles are bound to the same base
        	if (baseBinding == otherBase)
        	{
        		ambiguitySet.add(otherRole);
        	}
        	else
        	{
        		rolesToAnalyze.add(otherRole);
        	}
        }

        // there were > 1 references on base
        if (ambiguitySet.size() > 1) {
        	ReferenceBinding[] commonSupers = getCommonBoundSuperRoles(ambiguitySet.iterator());
        	if (commonSupers.length > 0) {
        		this._problemReporter.potentiallyAmbiguousRoleBinding(this._teamTypeDeclaration, ambiguitySet);
        		for (ReferenceBinding commonSuper : commonSupers)
        			this._teamTypeDeclaration.getTeamModel().ambigousLifting.add(new Pair<ReferenceBinding, ReferenceBinding>(baseBinding, commonSuper));

        		for (int i = 0; i < commonSupers.length; i++) {
					commonSupers[i].roleModel._hasBindingAmbiguity = true;
				}
        	}
        }

		// call this very method recursively if there are still roles to analyze
		if (!rolesToAnalyze.isEmpty())
		{
			detectLiftingAmbiguity(rolesToAnalyze);
		}
	}

	/**
	 * Given that for roles binding ambiguity exists, find the set of common
	 * bound super roles.
	 *
	 * @param roles
	 * @return array of super roles with baseclass binding and more than one
	 *           sub-role in 'roles'
	 */
	private ReferenceBinding[] getCommonBoundSuperRoles(Iterator<RoleModel> roles) {
		HashSet<ReferenceBinding> allBoundSupers = new HashSet<ReferenceBinding>();
		LinkedList<ReferenceBinding> commonBoundSupers = new LinkedList<ReferenceBinding>();
		while (roles.hasNext()) {
			RoleModel role = roles.next();
			ReferenceBinding currentRole = role.getBinding();
			while (true) {
				currentRole = currentRole.superclass();
				if (currentRole == null || ! currentRole.isRole())
					break;
				if (currentRole.baseclass() == null)
					continue;
				if (allBoundSupers.contains(currentRole))
					commonBoundSupers.add(currentRole);
				else
					allBoundSupers.add(currentRole);
			}
		}
		ReferenceBinding[] result = new ReferenceBinding[commonBoundSupers.size()];
		commonBoundSupers.toArray(result);
		return result;
	}
}
