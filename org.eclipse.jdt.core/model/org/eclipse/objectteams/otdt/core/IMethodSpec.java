/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2010 GK Software AG
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ITypeParameter;

/**
 * Represents a method spec in a method mapping (callout/callin) in the extended Java model.
 * 
 * @author stephan
 * @since 3.7
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IMethodSpec {

	/** Answer whether this method spec specifies signatures (otherwise it only consists of the selector. */
	public boolean hasSignature();

	/** Answer the argument types in the method spec's signature, if specified (see {@link #hasSignature()}). */
	public String[] getArgumentTypes();

	/** Answer the argument names in the method spec's signature, if specified (see {@link #hasSignature()}). */
	public String[] getArgumentNames();

	/** Answer the selector by which this method spec refers to a (base or role) method. */
	public String getSelector();

	/** Answer the return type in the method spec's signature, if specified (see {@link #hasSignature()}). */
	public String getReturnType();

	/** Similar to {@link IMethod#getSignature()}, but works as a handle only method  (see also {@link #hasSignature()}). */
	public String getSignature();

	/** Answer whether this method spec actually declares a method (i.e., it's a role method spec in a callout method binding).*/
	public boolean isDeclaration();

	/** Answer whether the method spec matches base methods with more specific return types than the type specified here (callin only). */
	public boolean hasCovariantReturn();

	/** Answer the type parameters. */
	public ITypeParameter[] getTypeParameters();

	/** Start position of this element. */
	public int getSourceStart();

	/** End position of this element. */
	public int getSourceEnd();
}