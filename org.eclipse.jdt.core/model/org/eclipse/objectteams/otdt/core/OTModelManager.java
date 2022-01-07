/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2004, 2016 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany, and others.
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
package org.eclipse.objectteams.otdt.core;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ExternalJavaProject;
import org.eclipse.objectteams.otdt.internal.core.BinaryRoleType;
import org.eclipse.objectteams.otdt.internal.core.CallinMapping;
import org.eclipse.objectteams.otdt.internal.core.CalloutMapping;
import org.eclipse.objectteams.otdt.internal.core.CalloutToFieldMapping;
import org.eclipse.objectteams.otdt.internal.core.MappingElementInfo;
import org.eclipse.objectteams.otdt.internal.core.OTModel;
import org.eclipse.objectteams.otdt.internal.core.OTType;
import org.eclipse.objectteams.otdt.internal.core.RoleFileType;
import org.eclipse.objectteams.otdt.internal.core.RoleType;
import org.eclipse.objectteams.otdt.internal.core.util.MethodData;


/**
 * Manager provides connection between JavaModel and OTM.
 *
 * @author jwloka
 * @version $Id: OTModelManager.java 23416 2010-02-03 19:59:31Z stephan $
 */
public class OTModelManager
{

	/**
	 * Expose this constant from {@link ExternalJavaProject} for use by other OT plugins.
	 * @since 3.7
	 */
	public static final String EXTERNAL_PROJECT_NAME = ExternalJavaProject.EXTERNAL_PROJECT_NAME;

	private final static OTModel MAPPING = OTModel.getSharedInstance();

	private static OTModelManager singleton;

    private OTModelReconcileListener reconcileListener;

	protected OTModelManager()
	{
		singleton = this;

		this.reconcileListener = new OTModelReconcileListener();
		JavaCore.addElementChangedListener(this.reconcileListener, ElementChangedEvent.POST_RECONCILE);
	}

	public static OTModelManager getSharedInstance()
	{
		if (singleton == null)
		{
			new OTModelManager();
		}

		return singleton;
	}

	public static void dispose()
	{
	    if (singleton != null)
	    {
	        JavaCore.removeElementChangedListener(singleton.reconcileListener);
	        OTModel.dispose();
	    }

	    singleton = null;
	}

	/** Register a type of which we don't yet have the flags. */
	public void addUnopenedType(IType type) {
		MAPPING.addUnopenedType(type);
	}

	/**
	 * Add the propriate Object Teams Model element for a given IType
	 */
	public IOTType addType(IType elem, int typeDeclFlags, String baseClassName, String baseClassAnchor, boolean isRoleFile)
	{
		IJavaElement parent = elem.getParent();
		IOTType result = null;

		while (parent != null) {
			switch (parent.getElementType())
			{
			    case IJavaElement.COMPILATION_UNIT:
			    case IJavaElement.CLASS_FILE:
			    	if (TypeHelper.isRole(typeDeclFlags)) {
			    		if (elem.isBinary()) {
			    			MAPPING.addOTElement( result = new BinaryRoleType(elem,
			    					parent,
			    					typeDeclFlags,
			    					baseClassName,
			    					baseClassAnchor));
			    		} else {
			    			if(isRoleFile) {
								//  could also be a teeam, which is handled inside the constructor
				    			MAPPING.addOTElement( result = new RoleFileType(elem,
				    					parent,
				    					typeDeclFlags,
				    					baseClassName,
				    					baseClassAnchor));
							} else {
								MAPPING.addOTElement( result = new RoleType(elem,
				    					parent,
				    					typeDeclFlags,
				    					baseClassName,
				    					baseClassAnchor));
							}
			    		}
			    	} else if (TypeHelper.isTeam(typeDeclFlags)) {
						MAPPING.addOTElement( result = new OTType(IOTJavaElement.TEAM, elem, null, typeDeclFlags) );
					}
					return result;
			    case IJavaElement.TYPE:
					IType   encType   = (IType)parent;
					IOTType otmParent = MAPPING.getOTElement(encType);

					return maybeAddRoleType(elem, otmParent, typeDeclFlags, baseClassName, baseClassAnchor);
		    	case IJavaElement.METHOD:
		    		break;
		    	case IJavaElement.INITIALIZER:
		    		break;
		    	case IJavaElement.FIELD:
					break;
			    default:
			    	new Throwable("Warning: unexpected parent for OT element: " + parent).printStackTrace(); //$NON-NLS-1$
			    	return result;
			}
			parent = parent.getParent();
		}
		return result;
	}

	private IOTType maybeAddRoleType(IType elem, IOTType otmParent,
	        int typeDeclFlags, String baseClassName, String baseClassAnchor)
    {
		IOTType result = null;
        if ((otmParent != null)
        	&& (TypeHelper.isTeam(otmParent.getFlags())
        		|| (TypeHelper.isRole(otmParent.getFlags())) ) )
        {
        	MAPPING.addOTElement( result = new RoleType(elem,
        	        							otmParent,
        	        							typeDeclFlags,
        	        							baseClassName,
        	        							baseClassAnchor));
        }
        return result;
    }


    /**
	 * @noreference This method is not intended to be referenced by clients.
	 * @nooverride This method is not intended to be re-implemented or extended by clients.
	 */
    public ICallinMapping addCallinBinding(IType role, MappingElementInfo info)
	{
		IOTType otmRole = MAPPING.getOTElement(role);

		if ((otmRole != null) && (otmRole instanceof IRoleType))
		{
//{OTModelUpdate
		    IMethodSpec corrRoleMethData = info.getRoleMethod();
		    IMethod correspondingRoleMethod =
		        role.getMethod(corrRoleMethData.getSelector(),
		                       corrRoleMethData.getArgumentTypes());
//haebor}
			return new CallinMapping(info.getDeclarationSourceStart(),
						      info.getSourceStart(),
						      info.getSourceEnd(),
							  info.getDeclarationSourceEnd(),
							  (IRoleType)otmRole,
//{OTModelUpdate
//orig:	(IMethod) otmRole.getParent(),
						      correspondingRoleMethod,
//haebor}
							  info.getCallinName(),
							  info.getCallinKind(),
							  info.getRoleMethod(),
							  info.getBaseMethods(), info.hasSignature());
		}

		return null;
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 * @nooverride This method is not intended to be re-implemented or extended by clients.
	 */
	public ICalloutMapping addCalloutBinding(IType role, MappingElementInfo info)
	{
		IOTType otmRole = MAPPING.getOTElement(role);

		if ((otmRole != null) && (otmRole instanceof IRoleType))
		{
//{OTModelUpdate
		    IMethodSpec corrRoleMethData = info.getRoleMethod();
		    IMethod correspondingRoleMethod =
		        role.getMethod(corrRoleMethData.getSelector(),
		                       corrRoleMethData.getArgumentTypes());
//haebor}
			MethodData[] baseMethods = info.getBaseMethods();
			return new CalloutMapping(info.getDeclarationSourceStart(),
						       info.getSourceStart(),
						       info.getSourceEnd(),
							   info.getDeclarationSourceEnd(),
							   role,
							   correspondingRoleMethod,
							   info.getRoleMethod(),
							   baseMethods == null ? null : baseMethods[0],
							   info.hasSignature(),
							   info.isOverride(),
							   info.getDeclaredModifiers(),
							   true/*addAsChild*/);
		}

		return null;
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 * @nooverride This method is not intended to be re-implemented or extended by clients.
	 */
    public ICalloutToFieldMapping addCalloutToFieldBinding(IType role, MappingElementInfo info)
    {
        IOTType otmRole = MAPPING.getOTElement(role);

        if ((otmRole != null) && (otmRole instanceof IRoleType))
        {
            IMethodSpec corrRoleMethData = info.getRoleMethod();
            IMethod correspondingRoleMethod =
                role.getMethod(corrRoleMethData.getSelector(),
                               corrRoleMethData.getArgumentTypes());
            return new CalloutToFieldMapping(info.getDeclarationSourceStart(),
            		   info.getSourceStart(),
            		   info.getSourceEnd(),
                       info.getDeclarationSourceEnd(),
                       role,
                       correspondingRoleMethod,
					   info.getRoleMethod(),
					   info.getBaseField(),
					   info.hasSignature(),
					   info.isOverride(),
					   info.getDeclaredModifiers(),
					   true/*addAsChild*/);
        }

        return null;
    }

    public void addOTElement(IOTType otType)
    {
        MAPPING.addOTElement(otType);
    }

	/**
	 * Returns associated OTM element for a given type if there is one.
	 *
	 * @return corresponding OTM element or null if no such element exists
	 */
    public static IOTType getOTElement(@Nullable IType type)
    {
    	if (type != null) {
    		type.exists(); // ensure opened
    		return MAPPING.getOTElement(type);
    	}
    	return null;
    }

    public static boolean hasOTElementFor(@Nullable IType type)
    {
    	if (type != null) {
			type.exists(); // ensure opened
			return MAPPING.hasOTElementFor(type);
		}
    	return false;
    }

    public static void removeOTElement(IType type)
    {
    	removeOTElement(type, false);
    }

	/**
	 * @see OTModel#removeOTElement(IType, boolean)
	 */
	public static void removeOTElement(IType type, boolean hasChanged)
	{
		MAPPING.removeOTElement(type, hasChanged);
	}

	/**
	 * Utility function.
	 * @param type
	 * @return true if an OT-Type is registered for type that is a role
	 */
	public static boolean isRole(IType type) {
		IOTType ottype = getOTElement(type);
		return ottype != null && ottype.isRole();
	}

	/**
	 * Utility function.
	 * @param type
	 * @return true if an OT-Type is registered for type that is a team, or has flag TEAM set
	 */
	public static boolean isTeam(IType type) {
		if (hasOTElementFor(type))
		{
			IOTType ottype = getOTElement(type);
			return ottype.isTeam();
		}
		try {
			return type.exists() && Flags.isTeam(type.getFlags());
		} catch (JavaModelException e) {
			return false;
		}
	}

	/**
	 * returns true if this member belongs to a role. Takes binary role-interfaceparts into account.
	 * FIXME (carp): this is mostly a workaround for binary role-interfaceparts. When this problem is
	 * fixed conceptually, check and "port" all clients of this method.
	 */
	public static boolean belongsToRole(IMember member)
	{
		IType enclosing = member.getDeclaringType();
		if (enclosing != null)
		{
			IOTType otType = getOTElement(enclosing);
			return otType != null && otType.isRole();
		}

		return false;
	}
}
