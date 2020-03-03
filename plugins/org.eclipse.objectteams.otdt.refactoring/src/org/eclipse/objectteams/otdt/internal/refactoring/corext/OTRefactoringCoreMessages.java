/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2005, 2006 Fraunhofer Gesellschaft, Munich, Germany,
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
package org.eclipse.objectteams.otdt.internal.refactoring.corext;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * This utility class is a part of the OT/J refactoring adaptation.
 * It contains some helper methods in order to retrieve message
 * strings from a resource bundle.
 * 
 * @author brcan
 */
public class OTRefactoringCoreMessages
{
	private static final String RESOURCE_BUNDLE = OTRefactoringCoreMessages.class.getName();

	private static ResourceBundle _resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

	private OTRefactoringCoreMessages()
	{
	    //no instance
	}

	public static String getString(String key)
	{
		try
		{
			return _resourceBundle.getString(key);
		}
		catch (MissingResourceException e)
		{
			return key;
		}
	}
	
	public static String getFormattedString(String key, Object arg)
	{
		try
		{
			return NLS.bind(_resourceBundle.getString(key), new Object[] { arg });
		}
		catch (MissingResourceException e)
		{
			return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
		}	
	}
		
	public static String getFormattedString(String key, Object[] args)
	{
		try
		{
			return NLS.bind(_resourceBundle.getString(key), args);
		}
		catch (MissingResourceException e)
		{
			return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
		}	
	}	
}
