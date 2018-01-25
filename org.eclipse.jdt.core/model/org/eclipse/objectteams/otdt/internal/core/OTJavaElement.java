/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2012 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany, and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.Member;
import org.eclipse.jdt.internal.core.util.Util;
import org.eclipse.objectteams.otdt.core.IOTJavaElement;


/**
 * OTJavaElement implementation.
 * 
 * @author jwloka
 * @version $Id: OTJavaElement.java 23416 2010-02-03 19:59:31Z stephan $
 */
public abstract class OTJavaElement extends Member implements IOTJavaElement
{
    // previously, a mapping and its referenced method had the same handle identifier.
    // However, cf.
    // -  JavaElement.JEM_METHOD et al.
    // -  SourceType.getHandleFromMemento()
    // -  org.eclipse.objectteams.otdt.metrics.core.internal.OTCore (unused!)

	// Used for handle identifier
    public static final char OTEM_METHODMAPPING = '§';
    public static final String METHODMAPPING = Character.toString(OTEM_METHODMAPPING);

	private int          type;
	private List<IJavaElement>         children;
	private IJavaElement correspondingJavaElem;
	
	public OTJavaElement(int type, IJavaElement correspondingJavaElem, IJavaElement parent)
	{
		this(type, correspondingJavaElem, parent, true);
	}

	/**
	 * @param addAsChild Set this to false to avoid adding this element as a child of its parent, e.g. for temporary objects.
	 */
	public OTJavaElement(int type, IJavaElement correspondingJavaElem, IJavaElement parent, boolean addAsChild)
	{
		super((JavaElement) parent);
		this.type     = type;
		this.correspondingJavaElem = correspondingJavaElem;
		this.children = new ArrayList<IJavaElement>();

		if (addAsChild && parent instanceof OTJavaElement)
			((OTJavaElement)parent).addChild(this);
	}

	@Override
	public boolean hasChildren()
	{
	    return this.children.size() > 0;
	}
	
	@Override
	public IJavaElement[] getChildren()
	{
		synchronized(this.children) {
			if (this.children.isEmpty() && this.correspondingJavaElem instanceof IType)
				// fetch children from the java element (on first access):
				try {
					for (IJavaElement child : ((IType)this.correspondingJavaElem).getChildren())
						this.children.add(child);
				} catch (JavaModelException e) { /* noop */ }
			return this.children.toArray( new IJavaElement[this.children.size()] );
		}
	}
	
	public void addChild(IOTJavaElement child)
	{
		if (child != null)
			this.children.add(child);
	}
	
	@Override
	public IJavaElement getParent()
	{
//{OTModelUpdate : if null return the parent of the wrapped java element
//orig:		return _parent;
	    IJavaElement result = this.parent;
	    
	    if (result == null)
	        result = getCorrespondingJavaElement().getParent();
	    
	    return result;
//jwl}	    
	}	

	@Override
	public String getElementName()
	{
		return getCorrespondingJavaElement().getElementName();
	}

	@Override
	public int getElementType()
	{
		return this.type;
	}
	
	/**
	 * Assumes instanceof check is done in subclasses!
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) 
		{ 
		    return true;
		}
		if(obj == null)
		{
		    return false;
		}

		OTJavaElement other = (OTJavaElement)obj;
		
		return getParent().equals(other.getParent())
                && (this.type == other.type)
                && getCorrespondingJavaElement().equals(other.getCorrespondingJavaElement());
	}
	

	/**
	 * COPIED basically from JavaElement
	 * Returns the hash code for this Java element. By default,
	 * the hash code for an element is a combination of its name
	 * and parent's hash code. Elements with other requirements must
	 * override this method.
	 */
	@Override
	public int hashCode() {
		IJavaElement myParent = this.getParent();
		if (myParent == null) return super.hashCode();
		return Util.combineHashCodes(getElementName().hashCode(), myParent.hashCode());
	}

    @Override
	@SuppressWarnings("nls")
	public String toString()
	{
		return "OTJavaElement " + getElementName();	
	}
	
    @Override
	public IJavaElement getCorrespondingJavaElement()
    {
        return this.correspondingJavaElem;
    }
    
	@Override
	public OTJavaElement resolved(Binding binding) {
		return this;
	}

    public void setCorrespondingJavaElement(IJavaElement javaElem)
    {
        this.correspondingJavaElem = javaElem;
    }

// delegated IJavaElement methods
    @Override
	public boolean exists()
    {
        return this.correspondingJavaElem.exists();
    }

    @Override
    public void close() throws JavaModelException {
    	super.close();
    	((JavaElement) this.correspondingJavaElem).close();
    }
    
    @Override
	public IJavaElement getAncestor(int ancestorType)
    {
    	// first see if an OT element was requested (COPY&PASTE from JavaElement):
		IJavaElement element = this;
		while (element != null) {
			if (element.getElementType() == ancestorType)  return element;
			element= element.getParent();
		}
		// delegate to the java part:
        return this.correspondingJavaElem.getAncestor(ancestorType);
    }

    @Override
	public IResource getCorrespondingResource() throws JavaModelException
    {
    	// see SourceRefElement
    	if (!exists()) throw newNotPresentException();
    	return null;
    }

    @Override
	public String getHandleIdentifier()
    {
        return this.correspondingJavaElem.getHandleIdentifier();
    }

    @Override
	public IJavaModel getJavaModel()
    {
        return this.correspondingJavaElem.getJavaModel();
    }

    @Override
	public IJavaProject getJavaProject()
    {
        return this.correspondingJavaElem.getJavaProject();
    }

    @Override
	public IOpenable getOpenable()
    {
        return this.correspondingJavaElem.getOpenable();
    }

    @Override
	public IPath getPath()
    {
        return this.correspondingJavaElem.getPath();
    }

    @Override
	public IJavaElement getPrimaryElement()
    {
        return this.correspondingJavaElem.getPrimaryElement();
    }

    @Override
	public IResource getResource()
    {
        return this.correspondingJavaElem.getResource();
    }

    @Override
	public ISchedulingRule getSchedulingRule()
    {
        return this.correspondingJavaElem.getSchedulingRule();
    }

    @Override
	public IResource getUnderlyingResource() throws JavaModelException
    {
        return this.correspondingJavaElem.getUnderlyingResource();
    }

    @Override
	public boolean isReadOnly()
    {
        return this.correspondingJavaElem.isReadOnly();
    }

    @Override
	public boolean isStructureKnown() throws JavaModelException
    {
        return this.correspondingJavaElem.isStructureKnown();
    }
    
    @Override
    public <T> T getAdapter(Class<T> adapter)
    {
    	T result = super.getAdapter(adapter);
    	if (result == null)
    		result = this.correspondingJavaElem.getAdapter(adapter);
    	
    	return result;
    }
	
	@Override
	public void toString(int tab, StringBuffer buffer) {
		for (int i = tab; i > 0; i--)
			buffer.append("  "); //$NON-NLS-1$
		buffer.append(this.toString());
	}
}
