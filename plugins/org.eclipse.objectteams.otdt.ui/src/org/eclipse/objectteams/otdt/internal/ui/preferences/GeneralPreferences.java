/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2003, 2008 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.internal.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @author gis
 */
public class GeneralPreferences
{
    public static String CALLIN_MARKER_ENABLED_BOOL = "prefs.ot.callinmarker.enabled"; //$NON-NLS-1$
    
//TODO (haebor): use mechanisms that are defined by DefaultPreferences 
    public static void initDefaults(IPreferenceStore prefs)
    {
        prefs.setDefault(CALLIN_MARKER_ENABLED_BOOL, true);
    }
}
