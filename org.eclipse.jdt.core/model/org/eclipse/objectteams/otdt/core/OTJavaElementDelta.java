/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
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
 * $Id: OTJavaElementDelta.java 23416 2010-02-03 19:59:31Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * @author Jochen
 *
 */
public class OTJavaElementDelta implements IJavaElementDelta 
{
	private IJavaElementDelta delegate;
	private Map<IJavaElement,IOTJavaElement>	otElemMapping;

	public OTJavaElementDelta(IJavaElementDelta delta) {
		this(new HashMap<IJavaElement,IOTJavaElement>(), delta);//intentionally empty HashMap!
	}

	/**
	 * @param otElemMapping
	 * @param delta
	 */
	public OTJavaElementDelta(Map<IJavaElement,IOTJavaElement> otElemMapping, IJavaElementDelta delta) {
		this.delegate = delta;
		this.otElemMapping = otElemMapping;
	}

	@Override
	public IJavaElement getElement() {
		return maybeTransformElement(this.delegate.getElement());
	}
	
	@Override
	public IJavaElementDelta[] getAddedChildren() {
		return wrapChildren(this.delegate.getAddedChildren());
	}
	
	@Override
	public IJavaElementDelta[] getAffectedChildren() {
		return wrapChildren(this.delegate.getAffectedChildren());
	}
	
	@Override
	public IJavaElementDelta[] getChangedChildren() {
		return wrapChildren(this.delegate.getChangedChildren());
	}
	
	
	@Override
	public IJavaElement getMovedFromElement() {
		return maybeTransformElement(this.delegate.getMovedFromElement());
	}
	
	@Override
	public IJavaElement getMovedToElement() {
		return maybeTransformElement(this.delegate.getMovedToElement());
	}
	
	@Override
	public IJavaElementDelta[] getRemovedChildren() {
		return wrapChildren(this.delegate.getRemovedChildren());
	}
	
	@Override
	public IJavaElementDelta[] getAnnotationDeltas() {
		return wrapChildren(this.delegate.getAnnotationDeltas());
	}

//pure delegates	
	@Override
	public IResourceDelta[] getResourceDeltas() {
		return this.delegate.getResourceDeltas();
	}
	
	@Override
	public String toString() {
		return this.delegate.toString();
	}
	
	@Override
	public int getFlags() {
		return this.delegate.getFlags();
	}
	
	@Override
	public int getKind() {
		return this.delegate.getKind();
	}

	@Override
	public CompilationUnit getCompilationUnitAST() {
		return this.delegate.getCompilationUnitAST();
	}
	
	
//end of delegates
//helpers
	private OTJavaElementDelta[] wrapChildren(IJavaElementDelta[] deltas) {
		return wrapDeltas(deltas);
	}
	
	private OTJavaElementDelta[] wrapDeltas(IJavaElementDelta[] deltas) {
		OTJavaElementDelta[] result = new OTJavaElementDelta[deltas.length];
		for(int idx = 0; idx < deltas.length; idx++)
			result[idx] = new OTJavaElementDelta(this.otElemMapping, deltas[idx]);

		return result;
	}
	
	private IJavaElement maybeTransformElement(IJavaElement element) {
		if(element instanceof IType) {
			IOTJavaElement otElement = 
				OTModelManager.getOTElement((IType)element);
			if(otElement != null) {
				return otElement;
			} else {
				IOTJavaElement otElem = this.otElemMapping.get(element);
				if (otElem != null)
					return otElem;
			}
		}
		return element;
	}
}
