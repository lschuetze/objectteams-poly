/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010 Fraunhofer Gesellschaft, Munich, Germany,
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
 * $Id: MethodData.java 23416 2010-02-03 19:59:31Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 		Fraunhofer FIRST - Initial API and implementation
 * 		Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.util;

import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.objectteams.otdt.core.IMethodSpec;



/**
 * The implementation of methods getTypeParameterBounds & getTypeParameterNames
 * has been copied from class org.eclipse.jdt.internal.core.SourceMethodElementInfo 
 * of the Eclipse JDT.
 * 
 * @author kaiser
 * @version $Id$
 */
public class MethodData implements IMethodSpec 
{
    private static final String [] EMPTY_STRING_ARRAY = new String[0];
    
	private String   selector;
	// attention: this field seems to use the constant pool encoding of types:
    private String[] argumentsTypes;
    private String[] argumentsNames;
	// attention: this field seems to use the constant pool encoding of types:
    private String   returnType;

	static final ITypeParameter[] NO_TYPE_PARAMETERS = new ITypeParameter[0];
    public ITypeParameter[] typeParameters = NO_TYPE_PARAMETERS;

 
    private boolean isDeclaration = false;
    private boolean covariantReturn= false;
    
    // FIXME(SH): fill with values:
    private int sourceStart, sourceEnd;

    /** Create a long method spec. */
    public MethodData(String selector, String[] types, String[] names, String returnType, boolean isDeclaration)
	{
		this.selector  = selector;
		if (this.selector == null) // e.g. void foo -> bar; -- missing return-type or name
			this.selector = ""; // FIXME: should this better be dealt during error recovery? //$NON-NLS-1$

		this.argumentsTypes = types;
		this.argumentsNames = names;
		this.returnType = returnType;
		
		assert types != null: "Long method spec must have types"; //$NON-NLS-1$
		if (names == null)
			this.argumentsNames = EMPTY_STRING_ARRAY;
		
		this.isDeclaration = isDeclaration;
	}
    
    public MethodData(String selector, String[] types, String[] names, String returnType, boolean isDeclaration,
    				  boolean covariantReturn) 
    {
    	this(selector, types, names, returnType, isDeclaration);
    	this.covariantReturn= covariantReturn;
    }
    
    /** Create a short method spec (no signature). */
    public MethodData(String selector, boolean isDeclaration)
	{
		this.selector  = selector;
		this.returnType = null; // TODO(SH): is this OK??
		
	    this.argumentsTypes = EMPTY_STRING_ARRAY;
	    this.argumentsNames = EMPTY_STRING_ARRAY;
		
		this.isDeclaration = isDeclaration;
	}
    /**
     * Constructor for use by ClassFileInfo (for byte-code browsing).
     * @param selector
     * @param signature
     */
    public MethodData(String selector, String signature) {
    	signature = signature.replace('/', '.');
    	this.selector = selector;
    	this.argumentsTypes = Signature.getParameterTypes(signature);
    	this.returnType = Signature.getReturnType(signature);
    	this.argumentsNames = EMPTY_STRING_ARRAY;
    }

    public MethodData(String selector, String signature, boolean covariantReturn) {
		this(selector, signature);
		this.covariantReturn= covariantReturn;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#hasSignature()
	 */
	@Override
	public boolean hasSignature()
	{
		return this.argumentsTypes != EMPTY_STRING_ARRAY;
	}

    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#getArgumentTypes()
	 */
    @Override
	public String[] getArgumentTypes()
    {
        return this.argumentsTypes;
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#getArgumentNames()
	 */
    @Override
	public String[] getArgumentNames()
    {
    	return this.argumentsNames;
    }

    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#getSelector()
	 */
    @Override
	public String getSelector()
    {
        return this.selector;
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#getReturnType()
	 */
    @Override
	public String getReturnType()
    {
    	return this.returnType;
    }

    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#getSignature()
	 */
	@Override
	public String getSignature() {
		return Signature.createMethodSignature(this.argumentsTypes, this.returnType);
	}
	
    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#isDeclaration()
	 */
    @Override
	public boolean isDeclaration() {
    	return this.isDeclaration;
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#hasCovariantReturn()
	 */
    @Override
	public boolean hasCovariantReturn() {
    	return this.covariantReturn;
    }
    
    @Override
	public ITypeParameter[] getTypeParameters() {
    	return this.typeParameters;
    }
    /* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#getTypeParameterNames()
	 */
    public String[] getTypeParameterNames() {
    	// copied from org.eclipse.jdt.internal.core.SourceMethodElementInfo
    	int length = this.typeParameters.length;
    	if (length == 0) return CharOperation.NO_STRINGS;
    	String[] typeParameterNames = new String[length];
    	for (int i = 0; i < length; i++) {
    		typeParameterNames[i] = this.typeParameters[i].getElementName();
    	}
    	return typeParameterNames;
    }

    @Override
	public String toString()
    {
    	String signature = this.selector + "("; //$NON-NLS-1$
    	
    	if (this.argumentsTypes != null)
    	{
	    	for (int idx=0; idx < this.argumentsTypes.length; idx++)
	    	{
	    		signature += (idx == 0 ? "" : ", ")  //$NON-NLS-1$ //$NON-NLS-2$
	    			+ Signature.getSimpleName(Signature.toString(this.argumentsTypes[idx]));
	    	}
    	}
    	signature += ")"; //$NON-NLS-1$
    	
    	return signature;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#getSourceStart()
	 */
	@Override
	public int getSourceStart() {
		return this.sourceStart;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.objectteams.otdt.core.util.IMethodSpec#getSourceEnd()
	 */
	@Override
	public int getSourceEnd() {
		return this.sourceEnd;
	}
}
