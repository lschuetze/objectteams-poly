/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2008, 2010 Technical University Berlin, Germany.
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
package org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.copyinheritance;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.ast.Expression.DecapsulationState;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.core.exceptions.InternalCompilerError;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.ITranslationStates;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.StateHelper;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.ITeamAnchor;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.RoleTypeBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.TeamModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.copyinheritance.CopyInheritance.SupertypeObligation;
import org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.transformer.StandardElementGenerator;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.AstGenerator;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.Protections;

/**
 * This class is a module for several functions related to copying and
 * connecting the type structure of roles from a super team.
 * @since 1.2.1 (before that it was nested in class CopyInheritance).
 */
public class TypeLevel {

	/**
	 * MAIN ENTRY into methods provided by this nested class.
	 *
	 * If roleDecl is a role interface with a matching tsuper interface,
	 * create the link in superInterfaces.
	 * Copy all supers (baseclass, superclass/interfaces).
	 * Also adjust super class/interfaces if they have been resolved as
	 * members of the super team.
	 * Be careful with links across teams: adjust only relatively.
	 *
	 * @param superTeam
	 * @param roleDecl
	 */
	public static SupertypeObligation[] connectRoleClasses(
	        ReferenceBinding  superTeam,
	        TypeDeclaration   roleDecl)
	{
		ArrayList<SupertypeObligation> obligations = new ArrayList<SupertypeObligation>();
	    ReferenceBinding[] tsuperRoles  = roleDecl.getRoleModel().getTSuperRoleBindings();
	    SourceTypeBinding destRole     = roleDecl.binding;
	    if (tsuperRoles.length > 0) {
	    	// connect to the last added tsuper role. Earlier tsupers have already been processed
	    	// in earlier invocations as ClassScope.connectTeamMemberTypes(..) descends depth first
	        ReferenceBinding tsuperRole = tsuperRoles[tsuperRoles.length-1];
			assert(tsuperRole.isDirectRole());

			// don't bother with AST, we are called after hierarchies have been connected
			// at the binding level.
			mergeSuperinterfaces(superTeam, tsuperRole, destRole);
		    if (!roleDecl.isInterface())
		    	copyAdjustSuperclass(tsuperRole, roleDecl, obligations);
		    copyBaseclass(tsuperRole, roleDecl);

	    } else {
	    	// no tsupers to merge with, but still need to adjust the super class:
	    	copyAdjustSuperclass(null, roleDecl, obligations);
	    }

		adjustSuperinterfaces(superTeam, destRole, obligations);
		// compatibility may have changed, clear negative cache entries:
		if (destRole !=  null)
			destRole.resetIncompatibleTypes();

	    SupertypeObligation[] result = new SupertypeObligation[obligations.size()];
	    obligations.toArray(result);
	    return result;
	}

	/**
	 * When bindings for superinterfaces are created (ClassScope.connectSuperinterfaces()),
	 * they might falsely be resolved to some role of a super team.
	 * Now is the time to adjust this reference to the copied role in this team.
	 * @param superTeam
	 * @param destRole
	 * @param obligations
	 */
	private static void adjustSuperinterfaces(
				ReferenceBinding superTeam,
				SourceTypeBinding destRole,
				ArrayList<SupertypeObligation> obligations)
	{
		if (destRole == null)
			return; // no hope;
	    ReferenceBinding[] superinterfaces = destRole.superInterfaces();
	    if (superinterfaces != null) {
		    for (int i=0; i<superinterfaces.length; i++) {
		    	// TODO(SH): enclosingType could be a tsuper of superTeam.
		        if (TypeBinding.equalsEquals(superinterfaces[i].enclosingType(), superTeam)) {
		    		ReferenceBinding teamType  = destRole.enclosingType();
		            ReferenceBinding superinterface =
		                    teamType.getMemberType(superinterfaces[i].internalName());
		            if (TypeBinding.notEquals(superinterface, destRole)) // not for the tsuper link.
		            {
		                obligations.add(new SupertypeObligation(superinterface, superinterfaces[i], null, null));
		                superinterfaces[i] = superinterfaces[i].transferTypeArguments(superinterface);
		                destRole.scope.compilationUnitScope().recordSuperTypeReference(superinterface);
		            }
		        }
		    }
	    }
	}

	/**
	 * Copy the 'playedBy' declaration and check for illegal overriding.
	 * @param srcRole
	 * @param destRoleDecl
	 */
	private static void copyBaseclass(ReferenceBinding srcRole, TypeDeclaration destRoleDecl)
	{
		// Note(SH): playedBy overriding is now checked from ClassScope.connectBaseclass()

		SourceTypeBinding destRole = destRoleDecl.binding;
		// baseclass can safely be accessed if its
		// - a binary type
		// - has passed completeFieldsAndMethods
		//   (including state final, where the ast-link is removed).
		ReferenceBinding srcErasure = (ReferenceBinding) srcRole.erasure();
		if (   srcErasure.isBinaryBinding()
			|| StateHelper.hasState(srcRole, ITranslationStates.STATE_LENV_DONE_FIELDS_AND_METHODS))
		{
			// directly add to binding only:
			if (srcRole.rawBaseclass() != null) {  // don't call baseclass() here!
		        if (destRoleDecl.baseclass == null) {
		        	destRole.baseclass = checkAdjustImplicitlyRefinedBase(srcRole.baseclass, destRoleDecl, destRole);
		            destRoleDecl.scope.compilationUnitScope().recordSuperTypeReference(destRole.baseclass);
		            if (   destRole.baseclass.isValidBinding()
		            	&& TypeBinding.notEquals(srcRole.baseclass, destRole.baseclass)) // only if actually redefining baseclass
		            	StandardElementGenerator.checkCreateBaseField(destRoleDecl, destRole.baseclass, false);
		        }
		    }
		} else {
			// source roles should avoid calling baseclass().
			// rather create the reference at AST level:
			final TypeDeclaration srcDecl= srcErasure.roleModel.getAst();
			if (srcDecl.baseclass != null && destRoleDecl.baseclass == null) {
				long pos= (((long)destRoleDecl.sourceStart)<<32)+destRoleDecl.sourceEnd;
				// create a special type reference that uses the original scope for resolving:
				destRoleDecl.baseclass= new AstGenerator(pos).alienScopeTypeReference(srcDecl.baseclass, srcDecl.scope.parent);
				destRoleDecl.baseclass.setBaseclassDecapsulation(DecapsulationState.REPORTED);
				destRoleDecl.baseclass.isGenerated = true;
			}
		}
	}

	/**
	 * Starting from the tsuper role's base class check whether implicit playedBy refinement
	 * is involved. If so, create a weakened type to reflect the dual type of the base field.
	 */
	private static ReferenceBinding checkAdjustImplicitlyRefinedBase(ReferenceBinding  baseclass,
																	 TypeDeclaration   destRoleDecl,
																	 SourceTypeBinding destRole)
	{
		// only if base class is a base-anchored role:
		if (   RoleTypeBinding.isRoleWithExplicitAnchor(baseclass)
			&& ((RoleTypeBinding)baseclass)._teamAnchor.isBaseAnchor())
		{
			// only if the current enclosing has an explicit playedBy (TODO(SH): multi-level!)
			if (   destRoleDecl.enclosingType != null
				&& destRoleDecl.enclosingType.baseclass != null)
			{
				// navigate manually to the strong base type:
				ReferenceBinding destEnclosing = destRole.enclosingType();
				ReferenceBinding outerBase  = destEnclosing.baseclass();
				if (!outerBase.isCompatibleWith(baseclass.enclosingType()))
					return baseclass;
				ReferenceBinding innerBase  = outerBase.getMemberType(baseclass.sourceName());
				ITeamAnchor outerBaseField = destEnclosing.getField(IOTConstants._OT_BASE, true);
				assert outerBaseField != null;
				// combine both types:
				TypeBinding[] typeArguments = baseclass.isParameterizedType() ? ((ParameterizedTypeBinding)baseclass).arguments : null;
				TypeBinding innerBaseRoleType = outerBaseField.getDependentTypeBinding(innerBase, -1, typeArguments, 0);
				return ((RoleTypeBinding)innerBaseRoleType).weakenFrom(baseclass);
			}
		}
		return baseclass;
	}

	/**
	 * Merge super interfaces from implicitly inherited role and current.
	 * Adjust all role types to the current team, except the link between
	 * a role and its tsuper version (adjust that to the super-team).
	 *
	 * @param srcRole
	 * @param destRole
	 */
	static void mergeSuperinterfaces(
			ReferenceBinding  superTeam,
			ReferenceBinding  srcRole,
			SourceTypeBinding destRole)
	{
	    ReferenceBinding[] srcSuperIfcs = srcRole.superInterfaces();
	    if (   srcSuperIfcs == null
	    	|| srcSuperIfcs.length ==0)
	    {
	    	return;
	    }

		// for avoiding duplicates:
		HashSet<ReferenceBinding> newInterfaces = new HashSet<ReferenceBinding>();
		if (destRole.superInterfaces != null) {
			for (int i = 0; i < destRole.superInterfaces.length; i++) {
				newInterfaces.add(destRole.superInterfaces[i]);
			}
		}

		// merge
		ReferenceBinding destTeam = destRole.enclosingType();
		for (int i = 0; i < srcSuperIfcs.length; i++) {
			ReferenceBinding newSuperIfc = srcSuperIfcs[i];
			if (CharOperation.equals(srcSuperIfcs[i].internalName(), destRole.internalName()))
			{
				continue; // the link between class part and interface part.
			} else {
				newSuperIfc = destTeam.getMemberType(newSuperIfc.internalName());
				if (newSuperIfc == null)
					newSuperIfc = srcSuperIfcs[i]; // not a role of destTeam: restore
			}
			if (newSuperIfc == null)
				throw new InternalCompilerError("superinterface not found for " //$NON-NLS-1$
							+new String(destRole.internalName())+": " //$NON-NLS-1$
							+new String(srcSuperIfcs[i].readableName()));
			if (superTeam.isParameterizedType())
				newSuperIfc = (ReferenceBinding) Scope.substitute((ParameterizedTypeBinding)superTeam, newSuperIfc);
			newInterfaces.add(newSuperIfc);
			destRole.scope.compilationUnitScope().recordSuperTypeReference(newSuperIfc);
		}
		// write back into array:
		System.arraycopy(
				newInterfaces.toArray(), 0,
				destRole.superInterfaces =  new ReferenceBinding[newInterfaces.size()], 0,
				newInterfaces.size());
	}

	/**
	 * Add the "implements" link that connects a sub-role-ifc to
	 * its implicit super-role-ifc.
	 * Also checks compatibility of visibilities.
	 * @param roleInterfaceDeclaration
	 * @param superrole
	 */
	public static void addImplicitInheritance(
	        TypeDeclaration  roleInterfaceDeclaration,
	        ReferenceBinding superrole)
	{
		assert (roleInterfaceDeclaration.binding.tagBits & TagBits.BeginHierarchyCheck) != 0
				: "binding should be connected"; //$NON-NLS-1$
	    int modifiers          = roleInterfaceDeclaration.modifiers;
	    int inheritedModifiers = superrole.modifiers;
	    if (!Protections.isAsVisible(modifiers, inheritedModifiers))
	    {
	        roleInterfaceDeclaration.scope.problemReporter().
	                reducingRoleVisibility(
	                    roleInterfaceDeclaration,
	                    modifiers,
	                    inheritedModifiers);
	    }
    	// create bindings:
	    ReferenceBinding[] superInterfaces = roleInterfaceDeclaration.binding.superInterfaces;
	    ReferenceBinding[] newSuperInterfaces=null;
	    int len = 0;
	    if (superInterfaces != null) {
		    for (int i=0; i<superInterfaces.length; i++)
		    	if (TypeBinding.equalsEquals(superInterfaces[i], superrole))
		    		return; // superinterface already present.
		    len = superInterfaces.length;
		    newSuperInterfaces = new ReferenceBinding[len+1];
		    System.arraycopy(
		    		superInterfaces, 0,
		    		newSuperInterfaces, 0,
					len);
	    } else {
	    	newSuperInterfaces = new ReferenceBinding[1];
	    }

	    // this line cannot assign null, would have produced NPE above:
	    newSuperInterfaces[len] = superrole;
	    roleInterfaceDeclaration.scope.compilationUnitScope().recordSuperTypeReference(superrole);
	    roleInterfaceDeclaration.binding.superInterfaces = newSuperInterfaces;
	}

	/**
	 * Merge the superclass information of a role and its super role, checking compatibility
	 * and updating references to roles of the super team.
	 *
	 * @param tsuperRole   may be null
	 * @param destRoleDecl @pre: !isInterface()
	 * @param obligations
	 */
	static void copyAdjustSuperclass(ReferenceBinding tsuperRole, TypeDeclaration destRoleDecl, ArrayList<SupertypeObligation> obligations) {
		if(tsuperRole != null) {
		    if (   destRoleDecl.superclass == null
		    	&& CharOperation.equals(tsuperRole.internalName(), IOTConstants.OTCONFINED))
		    {
		    	// types __OT__Confined can be used as super, since they are essentially empty.
		    	// wouldn't want to connect to java.lang.Object!
		    	destRoleDecl.superclass = new AstGenerator(destRoleDecl).typeReference(tsuperRole);
		    	destRoleDecl.superclass.resolvedType = tsuperRole;
		    	destRoleDecl.binding.superclass = tsuperRole;
		    	return; // nothing more to check
		    }
		}
		if (destRoleDecl.binding == null)
			return; // no hope without a binding
		ReferenceBinding destTeam  = destRoleDecl.binding.enclosingType();
	    assert(destTeam != null);
	    checkAdjustSuperclass(destRoleDecl, destTeam, tsuperRole, obligations);
	}

	/**
	 * After the superclasses of tsuper role and current role have been determined,
	 * find out whether the new 'extends' clause was legal, and adjust superclass
	 * if needed (ie., update references to roles of the super team).
	 * @param destRoleDecl @pre: !isInterface()
	 * @param destTeam
	 * @param tsuperRole (may be null)
	 * @param obligations record here any sub-type relations that need checking after all roles have been adjusted
	 */
	private static void checkAdjustSuperclass(
			TypeDeclaration  destRoleDecl,
			ReferenceBinding destTeam,
			ReferenceBinding tsuperRole,
			ArrayList<SupertypeObligation> obligations)
	{
		ClassScope destScope = destRoleDecl.scope;

		ReferenceBinding inheritedSuperclass = null;
		ReferenceBinding newSuperclass = destRoleDecl.binding.superclass;
		boolean refineToTeam = false;
		if (tsuperRole != null) {
			inheritedSuperclass = tsuperRole.superclass();
			refineToTeam = !tsuperRole.isTeam() && destRoleDecl.isTeam();
			if (tsuperRole.isTeam() && !destRoleDecl.isTeam()) {
				destScope.problemReporter().regularOverridesTeam(destRoleDecl, tsuperRole);
				return;
			}
		}
		TypeReference newExtends = destRoleDecl.superclass;
		if (   newExtends == null	 // no source-level "extends" -> investige implicit (binding-level) extends:
			&& newSuperclass != null // either implicit super (j.l.Object or o.o.Team) or inherited from other tsuper
			&& !refineToTeam	     // an implicit extends to org.objectteams.Team is NOT dropped if the tsuper was not a team
			&& ((newSuperclass.id == TypeIds.T_JavaLangObject) || newSuperclass.id == IOTConstants.T_OrgObjectTeamsTeam)) // otherwise it was copied from a tsuper role
		{
			newSuperclass = null; // drop default 'extends java.lang.Object' or o.o.Team
		}

		// extends __OT_Confined overriding nothing or __OT__Confined is OK:
		if (   (   newSuperclass != null
				&& CharOperation.equals(newSuperclass.internalName(), IOTConstants.OTCONFINED))
			&& (   inheritedSuperclass == null
				|| CharOperation.equals(inheritedSuperclass.internalName(), IOTConstants.OTCONFINED)))
			return;

		if (   newSuperclass != null
			&& newSuperclass.isDirectRole())
		{
		    // check team compatibility
		    if (!TeamModel.areCompatibleEnclosings(destTeam, newSuperclass.original().enclosingType()))
		    {
		        destScope.problemReporter().extendIncompatibleEnclosingTypes(
		        		destRoleDecl, newSuperclass, newSuperclass.enclosingType());
		        // set a meaningfull superclass instead:
		        destRoleDecl.binding.superclass = (inheritedSuperclass != null) ?
		        			inheritedSuperclass : destScope.getJavaLangObject();
		        return;
		    }
		}
	    if (newSuperclass != null)
    		newSuperclass = strengthenSuper(destTeam, newSuperclass);
		if (inheritedSuperclass != null)
		{
			inheritedSuperclass = strengthenSuper(destTeam, inheritedSuperclass);
		    if (newSuperclass == null) {
		    	newSuperclass = inheritedSuperclass;
		    } else if (TypeBinding.notEquals(newSuperclass, inheritedSuperclass)) {
	    		// is the old superclass actually a tsuper version of the new superclass?
	    		if (   newSuperclass.roleModel == null
	    			|| !newSuperclass.roleModel.hasTSuperRole(inheritedSuperclass))
	    		{
	    			SupertypeObligation oblig = new SupertypeObligation(newSuperclass, inheritedSuperclass, newExtends, tsuperRole);
	    			// check now or later?
	    			if (obligations != null)
	    				obligations.add(oblig);
	    			else
	    				oblig.check(destRoleDecl);
	    		}
		    	destRoleDecl.getRoleModel()._refinesExtends = true;
		    }
		}
	    if (newSuperclass != null) {
	    	if (TypeBinding.equalsEquals(newSuperclass, destRoleDecl.binding)) {
	    		// a role extends its implicit super role: circularity!
	    		// error is already reported on behalf of the interface part (real circularity)
	    	} else {
	    		if (newSuperclass.isCompatibleWith(destRoleDecl.binding)) {
	    			// new super class is also a subclass of current
					destRoleDecl.scope.problemReporter().hierarchyCircularity(destRoleDecl.binding, newSuperclass, destRoleDecl);
					destRoleDecl.binding.tagBits |= TagBits.HierarchyHasProblems;
					newSuperclass.tagBits |= TagBits.HierarchyHasProblems;
	    		} else {
			        destRoleDecl.binding.superclass = destRoleDecl.binding.superclass.transferTypeArguments(newSuperclass);
			        destRoleDecl.scope.compilationUnitScope().recordSuperTypeReference(newSuperclass);
	    		}
	    	}
	        // don't update AST: not needed beside error reporting
			// and then only the old node has source positions..
	    }
	}

	// don't use TeamModel.strengthenRole because we don't want a RoleTypeBinding.
	static ReferenceBinding strengthenSuper(ReferenceBinding destTeam, ReferenceBinding superRole) {
		if (!superRole.isRole())
			return superRole;
		if (TeamModel.isTeamContainingRole(destTeam.superclass(), superRole))
			return destTeam.getMemberType(superRole.internalName());
		if (destTeam.isRole()) {
			ReferenceBinding[] tsuperTeams = destTeam.roleModel.getTSuperRoleBindings();
			for (int i = tsuperTeams.length-1; i >= 0; i--) { // check highest prio first (which comes last in the array)
				if (TypeBinding.equalsEquals(tsuperTeams[i], superRole.enclosingType()))
					return destTeam.getMemberType(superRole.internalName());
				if (TeamModel.isTeamContainingRole(tsuperTeams[i], superRole)) {
					ReferenceBinding strongEnclosing = destTeam.getMemberType(superRole.enclosingType().internalName());
					if (strongEnclosing != null)
						return strengthenSuper(strongEnclosing, superRole);
					return superRole;
				}
			}
		}
		return superRole;
	}

}