/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010 Fraunhofer Gesellschaft, Munich, Germany,
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
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Fraunhofer FIRST - Initial API and implementation
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.ui.tests.hierarchy.contentprovider;

import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.objectteams.otdt.tests.FileBasedTest;



public class TreeNode
{
    private HashMap<Object, TreeNode> _children; //keys: elements, values: TreeNodes 
    private Object _element;
    
    public TreeNode(Object element)
    {
       _element = element;
       _children = new HashMap<Object, TreeNode>();
    }

    public TreeNode getChildNode(Object element)
    {
        return _children.get(element);
    }
    
    public TreeNode[] setChildrenByElements(Object[] childrenElements)
    {
        if(childrenElements == null)
        {
            return null;
        }
        for (int idx = 0; idx < childrenElements.length; idx++)
        {
            _children.put(childrenElements[idx], new TreeNode(childrenElements[idx]));
        }
        return getChildren();
    }
    
    public void addChildByElement(Object childElement) {
    	_children.put(childElement, new TreeNode(childElement));
    }
        
    public Object getElement()
    {
        return _element;
    }
    
    public TreeNode[] getChildren()
    {
        return _children.values().toArray(new TreeNode[_children.values().size()]);
    }
    
    public HashMap<Object, TreeNode> getChildrenMap()
    {
        return _children;
    }
    
    public Object[] getChildrenElements()
    {
        return _children.keySet().toArray();
    }
    
    public boolean equalsAsserted(TreeNode other, Comparator comp, int level)
    {
        if(!comp.same(_element, other.getElement()))
        {
            FileBasedTest.assertTrue("This tree element " + _element 
                    + " is different from " + other.getElement() 
                    + " at level: " + level, false);
            return false;
        }
        
        HashMap<Object, TreeNode> othersChildren = other.getChildrenMap();
        if(_children.size() != othersChildren.size())
        {
            FileBasedTest.assertEquals("this: " + _element + ", other: " + other.getElement() + " at level: " 
                    + level, _children.size(), othersChildren.size());
            return false;
        }
        if(_children.isEmpty())
        {
            return true;
        }
        Object[] childrenElements = _children.keySet().toArray();
        for (int idx = 0; idx < childrenElements.length; idx++)
        {
            Object curChild = childrenElements[idx];
            if(!othersChildren.containsKey(curChild))
            {
                FileBasedTest.assertTrue("Child " + curChild + " for parent " + other.getElement() + " at level " + level + " is missing", false);
                return false;
            }
        }
        level = level + 1;
        for (int idx = 0; idx < childrenElements.length; idx++)
        {
            Object curChild = childrenElements[idx];
            if(!_children.get(curChild).equalsAsserted(othersChildren.get(curChild), comp, level))
            {
                return false;
            }
        }
        return true;
    }

	public TreeNode findNode(IType value) {
		if (value == this._element)
			return this;
		TreeNode node = this._children.get(value);
		if (node != null)
			return node;
		for (TreeNode childNode : this._children.values()) {
			node = childNode.findNode(value);
			if (node != null)
				return node;
		}
		return null;
	}

	public void addChild(TreeNode node) {
		this._children.put(node.getElement(), node);
		
	}

	// facilitate debugging:
	public String toString() {
		StringBuffer buf = new StringBuffer();
		toString(buf, 0);
		return buf.toString();
	}

	private void toString(StringBuffer buf, int indent) {
		for (int i=0; i<indent; i++) buf.append(' ');
		IJavaElement element = (IJavaElement)this._element;
		buf.append(element.getParent().getElementName()).append('.').append(element.getElementName()).append('\n');
		for (Entry<Object,TreeNode> e : this._children.entrySet())
			e.getValue().toString(buf, 4);
	}
}