/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute and Computer Architecture and Software
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
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.debug.ui;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.Bundle;


/**
 * @author ike
 *
 */

@SuppressWarnings("nls")
public class OTDebugImages
{
    public static final String TEAM_ACTIVATED           = "icons/team_act.gif";
    public static final String TEAM_IMPLICIT_ACTIVATED  = "icons/team_act_implicit.gif";
    public static final String TEAM_INACTIVATED         = "icons/team_inact.gif";
    public static final String SORT_TEAMS_BY_ACTIVATION_TIME = "icons/sort_by_activation.gif";
    public static final String SORT_TEAMS_BY_NAME 		= "icons/sort_by_name.gif";
    public static final String SORT_TEAMS_BY_INSTANTIATION = "icons/sort_by_instantiation.gif";
    public static final String UPDATE_TEAM_VIEW_ACTION = "icons/refresh.gif";

    
    public static void register()
    {
        checkBundleState();

        OTDebugUIPlugin plugin = OTDebugUIPlugin.getDefault();
        Bundle bundle = plugin.getBundle();
        ImageRegistry registry = plugin.getImageRegistry();

        register(TEAM_ACTIVATED, bundle, registry);
        register(TEAM_IMPLICIT_ACTIVATED, bundle, registry);
        register(TEAM_INACTIVATED, bundle, registry);
        register(SORT_TEAMS_BY_ACTIVATION_TIME, bundle, registry);
        register(SORT_TEAMS_BY_NAME, bundle, registry);
        register(SORT_TEAMS_BY_INSTANTIATION, bundle, registry);
        register(UPDATE_TEAM_VIEW_ACTION, bundle, registry);
    }
    
    public static ImageDescriptor get(String image)
    {
        checkBundleState();
        return OTDebugUIPlugin.getDefault().getImageRegistry().getDescriptor(image);
    }
    
    static void register(String icon, Bundle bundle, ImageRegistry registry)
    {
        URL imageURL = bundle.getEntry(icon);
        ImageDescriptor desc = ImageDescriptor.createFromURL(imageURL);
        registry.put(icon, desc);
    }    

    private static void checkBundleState()
    {
        if (OTDebugUIPlugin.getDefault().getBundle().getState() != Bundle.ACTIVE)
            throw new IllegalStateException("Bundle not active: " + OTDebugUIPlugin.getDefault().getBundle().getBundleId());
    }
    
}
