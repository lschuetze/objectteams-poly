/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2009, 2014 Germany and Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otequinox;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Interface for extenders wishing to participate in negotiation about aspect binding requests
 * including forced exports.
 * 
 * @author stephan
 * @since 1.2.6
 */
@NonNullByDefault
public interface IAspectRequestNegotiator {
	
	/** 
	 * Check whether a request for forced exports should be granted.
	 * @param aspectBundleSymbolicName the aspect issuing the request
	 * @param baseBundleSymbolicName   the affected base bundle
	 * @param basePackage              the affected base package
	 * @param previousNegotiation      the result of negotations up-to this point
	 * @return a structure holding the answer
	 */
	AspectBindingRequestAnswer checkForcedExport(String aspectBundleSymbolicName, String baseBundleSymbolicName, String basePackage, AspectPermission previousNegotiation);

	/** 
	 * Check whether a request for an aspect binding should be granted.
	 * @param aspectBundleSymbolicName the aspect issuing the request
	 * @param baseBundleSymbolicName   the affected base bundle
	 * @param teamClass 	           an affecting team class involved in this aspect binding
	 * @param previousNegotiation      the result of negotations up-to this point
	 * @return a structure holding the answer
	 */
	AspectBindingRequestAnswer checkAspectBinding(String aspectBundleSymbolicName, String baseBundleSymbolicName, String teamClass, AspectPermission previousNegotiation);
}
