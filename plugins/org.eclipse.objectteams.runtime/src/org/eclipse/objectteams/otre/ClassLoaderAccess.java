/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 * 
 * Copyright 2013 GK Software AG
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 	Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otre;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * Generalization over different strategies for obtaining a resource / class on the classpath.
 * <p>
 * If a {@link ClassLoader} is provided use its method {@link ClassLoader#getResource(String)} 
 * and {@link ClassLoader#loadClass(String)}.
 * </p><p>
 * Otherwise we need a {@link Method} instance for each kind of access which needs to match
 * the type of the object passed as "loader". The latter method is used in Equinox settings,
 * where the "loader" will be an <code>org.osgi.framework.Bundle</code>.
 * </p><p>
 * Selecting one of the above strategies happens globally when the system start up,
 * which implies that {@link #setGetResource(Method)} and {@link #setLoadClass(Method)}
 * need to be called before any weaving is triggered.
 * </p>
 */
public class ClassLoaderAccess {

	private static Method getResourceMethod;
	private static Method loadClassMethod;

	public static void setGetResource(Method method) {
		getResourceMethod = method;
	}
	public static void setLoadClass(Method method) {
		loadClassMethod = method;
	}

	public static InputStream getResourceAsStream(Object loader, String name) {
		if (loader instanceof ClassLoader)
			return ((ClassLoader) loader).getResourceAsStream(name);
		try {
			URL url = (URL) getResourceMethod.invoke(loader, name);
			return url.openStream();
		} catch (Exception e) {
			return null;
		}
	}

	public static Class<?> loadClass(Object loader, String name) 
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException 
	{
		if (loader instanceof ClassLoader)
			return ((ClassLoader) loader).loadClass(name);
		else
			return (Class<?>)loadClassMethod.invoke(loader, name);
	}
}
