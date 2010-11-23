/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public IJavaElement getElement() {
		return maybeTransformElement(this.delegate.getElement());
	}
	
	public IJavaElementDelta[] getAddedChildren() {
		return wrapChildren(this.delegate.getAddedChildren());
	}
	
	public IJavaElementDelta[] getAffectedChildren() {
		return wrapChildren(this.delegate.getAffectedChildren());
	}
	
	public IJavaElementDelta[] getChangedChildren() {
		return wrapChildren(this.delegate.getChangedChildren());
	}
	
	
	public IJavaElement getMovedFromElement() {
		return maybeTransformElement(this.delegate.getMovedFromElement());
	}
	
	public IJavaElement getMovedToElement() {
		return maybeTransformElement(this.delegate.getMovedToElement());
	}
	
	public IJavaElementDelta[] getRemovedChildren() {
		return wrapChildren(this.delegate.getRemovedChildren());
	}
	
	public IJavaElementDelta[] getAnnotationDeltas() {
		return wrapChildren(this.delegate.getAnnotationDeltas());
	}

//pure delegates	
	public IResourceDelta[] getResourceDeltas() {
		return this.delegate.getResourceDeltas();
	}
	
	public String toString() {
		return this.delegate.toString();
	}
	
	public int getFlags() {
		return this.delegate.getFlags();
	}
	
	public int getKind() {
		return this.delegate.getKind();
	}

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
