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
 * $Id: MappingElementInfo.java 23416 2010-02-03 19:59:31Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core;

import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.objectteams.otdt.core.IFieldAccessSpec;
import org.eclipse.objectteams.otdt.internal.core.util.MethodData;


/**
 * Data structure holding mapping related informations provided by the
 * SourceElementParser and CompilationUnitStructureRequestor.
 * 
 * @author kaiser
 * @version $Id: MappingElementInfo.java 23416 2010-02-03 19:59:31Z stephan $
 */
public class MappingElementInfo
{
	private MethodData   roleMethod;
	private MethodData[] baseMethods;
    private IFieldAccessSpec    baseField;
    private int          sourceStart;
    private int          sourceEnd;
	private int          declarationSourceStart;
	private int          declarationSourceEnd;
	private char[]       callinName;
	private int          callinKind;
	private boolean      hasSignature;
	private boolean      isOverride; // callout override.
	private int			 declaredModifiers;
	char[][] 			 categories;
	public Annotation[] annotations;
	
    
    public MethodData getRoleMethod()
    {
        return this.roleMethod;
    }
    
    public MethodData[] getBaseMethods()
    {
        return this.baseMethods;
    }
    
    public IFieldAccessSpec getBaseField()
    {
    	return this.baseField;
    }

    public int getDeclarationSourceStart()
    {
        return this.declarationSourceStart;
    }

    public int getSourceStart() 
    {
		return this.sourceStart;
	}

    public int getSourceEnd() 
    {
		return this.sourceEnd;
	}
    
    public int getDeclarationSourceEnd()
    {
    	return this.declarationSourceEnd;
    }
    
    public char[] getCallinName() 
    {
    	return this.callinName;
    }

    public int getCallinKind()
    {
        return this.callinKind;
    }

    public char[][] getCategories()
    {
    	return this.categories;
    }

    public void setRoleMethod(MethodData data)
    {
        this.roleMethod = data;
    }
    
    public void setBaseMethods(MethodData[] data)
    {
        this.baseMethods = data;
    }

    public void setBaseField(IFieldAccessSpec data)
    {
    	this.baseField = data;
    }
    
    public void setDeclarationStart(int start)
    {
        this.declarationSourceStart = start;
    }
    
	public void setSourceStart(int start) 
	{
		this.sourceStart = start;
	}
    
	public void setSourceEnd(int end) 
	{
		this.sourceEnd = end;
	}
    
    public void setDeclarationSourceEnd(int end)
    {
        this.declarationSourceEnd = end;
    }

    public void setCallinName(char[] callinName) 
    {
    	this.callinName = callinName;
    }
    
    public void setCallinKind(int terminalTokenCallinKind)
    {
    	this.callinKind = CallinMapping.convertTerminalTokenToModel(terminalTokenCallinKind);
    }

    public boolean hasSignature()
    {
        return this.hasSignature;
    }
    
    public void setHasSignature(boolean signature)
    {
        this.hasSignature = signature;
    }
    
    public void setCategories(char[][] categories)
    {
    	this.categories = categories;
    }

	public boolean isOverride() {
		return this.isOverride;
	}
	
	public void setOverride(boolean flag) {
		this.isOverride = flag;
	}

	public int getDeclaredModifiers() {
		return this.declaredModifiers;
	}

	public void setDeclaredModifiers(int declaredModifiers) {
		this.declaredModifiers = declaredModifiers;
	}
}
