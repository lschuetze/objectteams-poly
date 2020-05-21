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
 * $Id: PhantomType.java 23416 2010-02-03 19:59:31Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IOrdinaryClassFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.objectteams.otdt.core.IPhantomType;

/**
 * A PhantomType is a type in the OTTypeHierarchy. It is a place holder
 * for roles created by ObjectTeams copy inheritance. A PhantomType wraps
 * its 'nearest' declared (non-phantom) implicit super role,
 * but is located in a different team context.
 *
 * A PhantomType wraps it's 'nearest' tsuper in _realType,
 * but may store any number of tsuper types from different levels of team nesting.
 * However, only direct tsupers are remembered, not tsupers of tsupers.
 *
 * Most methods of this type are forwarded to the nearest real role type
 * or the enclosing team type.
 * A PhantomType (as its wrappee) may also be a nested team.
 * @author Michael Krueger (mkr)
 * @version $Id: PhantomType.java 23416 2010-02-03 19:59:31Z stephan $
 */
public class PhantomType implements IPhantomType
{

    private IType _realType;
    private IType _enclosingTeam;
    private IType[] _allRealTypes;

    public PhantomType(IType enclosingTeam, IType tsuperRole)
    {
        this._enclosingTeam = enclosingTeam;
        this.setRealType(tsuperRole);
    }

    public PhantomType(IType enclosingTeam, IType[] allRealTsuperRoles)
    {
        this._enclosingTeam = enclosingTeam;
        this._realType = allRealTsuperRoles[0];
        this._allRealTypes = allRealTsuperRoles;
    }

    // === cannot code complete inside a phantom role for which no source exists: ===

    /**
     * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, org.eclipse.jdt.core.ICompletionRequestor)
	 * @deprecated Use {@link #codeComplete(char[],int,int,char[][],char[][],int[],boolean,CompletionRequestor)} instead.
     */
    @Override
	public void codeComplete(char[] snippet, int insertion, int position,
            char[][] localVariableTypeNames, char[][] localVariableNames,
            int[] localVariableModifiers, boolean isStatic,
            org.eclipse.jdt.core.ICompletionRequestor requestor) throws JavaModelException
    {
        handleUnsupported();
    }

    /**
     * @see org.eclipse.jdt.core.IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, org.eclipse.jdt.core.ICompletionRequestor, org.eclipse.jdt.core.WorkingCopyOwner)
	 * @deprecated Use {@link #codeComplete(char[],int,int,char[][],char[][],int[],boolean,CompletionRequestor,WorkingCopyOwner)} instead.
     */
    @Override
	public void codeComplete(char[] snippet, int insertion, int position,
            char[][] localVariableTypeNames, char[][] localVariableNames,
            int[] localVariableModifiers, boolean isStatic,
            org.eclipse.jdt.core.ICompletionRequestor requestor, WorkingCopyOwner owner)
            throws JavaModelException
    {
        handleUnsupported();
    }

	@Override
	public void codeComplete(char[] snippet, int insertion, int position,
			char[][] localVariableTypeNames, char[][] localVariableNames,
			int[] localVariableModifiers, boolean isStatic,
			CompletionRequestor requestor, IProgressMonitor monitor)
			throws JavaModelException
	{
		handleUnsupported();
	}

	@Override
	public void codeComplete(char[] snippet, int insertion, int position,
			char[][] localVariableTypeNames, char[][] localVariableNames,
			int[] localVariableModifiers, boolean isStatic,
			CompletionRequestor requestor, WorkingCopyOwner owner,
			IProgressMonitor monitor) throws JavaModelException
	{
		handleUnsupported();
	}

	@Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor) throws JavaModelException {
        handleUnsupported();
	}

	@Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor, WorkingCopyOwner owner) throws JavaModelException {
        handleUnsupported();
	}


    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#createField(java.lang.String, org.eclipse.jdt.core.IJavaElement, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public IField createField(String contents, IJavaElement sibling,
            boolean force, IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.createField(contents, sibling, force, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#createInitializer(java.lang.String, org.eclipse.jdt.core.IJavaElement, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public IInitializer createInitializer(String contents,
            IJavaElement sibling, IProgressMonitor monitor)
            throws JavaModelException
    {
        return this._realType.createInitializer(contents, sibling, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#createMethod(java.lang.String, org.eclipse.jdt.core.IJavaElement, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public IMethod createMethod(String contents, IJavaElement sibling,
            boolean force, IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.createMethod(contents, sibling, force, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#createType(java.lang.String, org.eclipse.jdt.core.IJavaElement, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public IType createType(String contents, IJavaElement sibling,
            boolean force, IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.createType(contents, sibling, force, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#findMethods(org.eclipse.jdt.core.IMethod)
     */
    @Override
	public IMethod[] findMethods(IMethod method)
    {
    	if (this._allRealTypes == null)
    		return this._realType.findMethods(method);
    	for (IType type : this._allRealTypes) {
			IMethod[] ms = type.findMethods(method);
			if (ms != null)
				return ms;
		}
    	return null;
    }

    @Override
	public String getElementName()
    {
        return this._realType.getElementName();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getField(java.lang.String)
     */
    @Override
	public IField getField(String name)
    {
    	if (this._allRealTypes == null)
    		return this._realType.getField(name);
    	for (IType type : this._allRealTypes) {
			IField f = type.getField(name);
			if (f != null)
				return f;
		}
    	return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getFields()
     */
    @Override
	public IField[] getFields() throws JavaModelException
    {
        if (this._allRealTypes == null)
        	return this._realType.getFields();
    	List<IField> uniqueFields = new ArrayList<IField>();
    	Set<String> names = new HashSet<String>();
    	for (int i = 0; i < this._allRealTypes.length; i++)
			for (IField field : this._allRealTypes[i].getFields())
				if (names.add(field.getElementName()))
					uniqueFields.add(field);
    	return uniqueFields.toArray(new IField[uniqueFields.size()]);
    }

    @Override
    public IField getRecordComponent(String name) {
    	return null;
    }

    @Override
	public String getFullyQualifiedName()
    {
        return getFullyQualifiedName('$');
    }

    @Override
	public String getFullyQualifiedName(char enclosingTypeSeparator)
    {
        return this._enclosingTeam.getFullyQualifiedName(enclosingTypeSeparator)
               + enclosingTypeSeparator
               + getElementName();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getInitializer(int)
     */
    @Override
	public IInitializer getInitializer(int occurrenceCount)
    {
        return this._realType.getInitializer(occurrenceCount);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getInitializers()
     */
    @Override
	public IInitializer[] getInitializers() throws JavaModelException
    {
        return this._realType.getInitializers();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getMethod(java.lang.String, java.lang.String[])
     */
    @Override
	public IMethod getMethod(String name, String[] parameterTypeSignatures)
    {
    	if (this._allRealTypes == null)
    		return this._realType.getMethod(name, parameterTypeSignatures);
    	for (int i = 0; i < this._allRealTypes.length; i++) {
			IMethod method = this._allRealTypes[i].getMethod(name, parameterTypeSignatures);
			if (method != null)
				return method;
    	}
    	return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getMethods()
     */
    @Override
	public IMethod[] getMethods() throws JavaModelException
    {
        if (this._allRealTypes == null)
        	return this._realType.getMethods();
    	List<IMethod> uniqueMethods = new ArrayList<IMethod>();
    	Set<String> signatures = new HashSet<String>();
    	for (int i = 0; i < this._allRealTypes.length; i++)
			for (IMethod method : this._allRealTypes[i].getMethods())
				if (signatures.add(getSimpleMethodSignature(method)))
					uniqueMethods.add(method);
    	return uniqueMethods.toArray(new IMethod[uniqueMethods.size()]);
    }

	private String getSimpleMethodSignature(IMethod method) {
		String[] params= method.getParameterTypes(); // TODO(SH) types may be type variables (see MethodProposalInfo.isSameMethodSignature)
		StringBuffer sign = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			String simpleName= Signature.getSimpleName(params[i]);
			sign.append(simpleName);
			if (i>0)
				sign.append(',');
		}
		return sign.toString();
	}

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getPackageFragment()
     */
    @Override
	public IPackageFragment getPackageFragment()
    {
        return this._enclosingTeam.getPackageFragment();
    }

    /**
     * If the type is a role file decide for the role files CU.
     * @see IMember#getTypeRoot()
     */
    @Override
	public ITypeRoot getTypeRoot() {
    	return this._realType.getTypeRoot();
    }


    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getSuperclassName()
     */
    @Override
	public String getSuperclassName() throws JavaModelException
    {
        return this._realType.getSuperclassName();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getSuperclassTypeSignature()
     */
    @Override
	public String getSuperclassTypeSignature() throws JavaModelException
    {
        return this._realType.getSuperclassTypeSignature();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getSuperInterfaceTypeSignatures()
     */
    @Override
	public String[] getSuperInterfaceTypeSignatures() throws JavaModelException
    {
        return this._realType.getSuperInterfaceTypeSignatures();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getSuperInterfaceNames()
     */
    @Override
	public String[] getSuperInterfaceNames() throws JavaModelException
    {
        return this._realType.getSuperInterfaceNames();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypeParameterSignatures()
     */
    @Override
	public String[] getTypeParameterSignatures() throws JavaModelException
    {
        return this._realType.getTypeParameterSignatures();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getType(java.lang.String)
     */
    @Override
	public IType getType(String name)
    {
   		IType inner = this._realType.getType(name);
   		return new PhantomType(this, inner);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypeQualifiedName()
     */
    @Override
	public String getTypeQualifiedName()
    {
        return getTypeQualifiedName('$');
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypeQualifiedName(char)
     */
    @Override
	public String getTypeQualifiedName(char enclosingTypeSeparator)
    {
        if (isBinary())
        {
            return this._enclosingTeam.getTypeQualifiedName(enclosingTypeSeparator);
        }
        else
        {
            return this._enclosingTeam.getTypeQualifiedName(enclosingTypeSeparator).concat(
                        enclosingTypeSeparator + this._realType.getElementName());
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#getTypes()
     */
    @Override
	public IType[] getTypes() throws JavaModelException
    {
        if (this._allRealTypes == null)
        	return this._realType.getTypes();
    	List<IType> uniqueTypes = new ArrayList<IType>();
    	Set<String> names = new HashSet<String>();
    	for (int i = 0; i < this._allRealTypes.length; i++)
			for (IType type: this._allRealTypes[i].getTypes())
				if (names.add(type.getElementName()))
					uniqueTypes.add(type);
    	return uniqueTypes.toArray(new IType[uniqueTypes.size()]);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isAnonymous()
     */
    @Override
	public boolean isAnonymous() throws JavaModelException
    {
        return false; // implicit inheritance is by name, thus phantom roles cannot be anoymous
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isClass()
     */
    @Override
	public boolean isClass() throws JavaModelException
    {
        return this._realType.isClass();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isEnum()
     */
    @Override
	public boolean isEnum() throws JavaModelException
    {
        return this._realType.isEnum();
    }

    @Override
	public boolean isRecord() throws JavaModelException
	{
    	return this._realType.isRecord();
	}

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isInterface()
     */
    @Override
	public boolean isInterface() throws JavaModelException
    {
        return this._realType.isInterface();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isAnnotation()
     */
    @Override
	public boolean isAnnotation() throws JavaModelException
    {
        return this._realType.isAnnotation();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isLocal()
     */
    @Override
	public boolean isLocal() throws JavaModelException
    {
        return this._realType.isLocal();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#isMember()
     */
    @Override
	public boolean isMember() throws JavaModelException
    {
        return true;
    }

    @Override
    public boolean isLambda() {
    	return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#loadTypeHierachy(java.io.InputStream, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public ITypeHierarchy loadTypeHierachy(InputStream input,
            IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.loadTypeHierachy(input, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public ITypeHierarchy newSupertypeHierarchy(IProgressMonitor monitor)
            throws JavaModelException
    {
        return this._realType.newSupertypeHierarchy(monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.jdt.core.ICompilationUnit[], org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public ITypeHierarchy newSupertypeHierarchy(
            ICompilationUnit[] workingCopies, IProgressMonitor monitor)
            throws JavaModelException
    {
        return this._realType.newSupertypeHierarchy(workingCopies, monitor);
    }

    /**
     * @see org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.jdt.core.IWorkingCopy[], org.eclipse.core.runtime.IProgressMonitor)
     * @deprecated Use {@link #newSupertypeHierarchy(ICompilationUnit[], IProgressMonitor)} instead
     */
    @Override
	public ITypeHierarchy newSupertypeHierarchy(org.eclipse.jdt.core.IWorkingCopy[] workingCopies,
            IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.newSupertypeHierarchy(workingCopies, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newSupertypeHierarchy(org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public ITypeHierarchy newSupertypeHierarchy(WorkingCopyOwner owner,
            IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.newSupertypeHierarchy(owner, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.IJavaProject, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public ITypeHierarchy newTypeHierarchy(IJavaProject project,
            IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.newTypeHierarchy(project, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.IJavaProject, org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public ITypeHierarchy newTypeHierarchy(IJavaProject project,
            WorkingCopyOwner owner, IProgressMonitor monitor)
            throws JavaModelException
    {
        return this._realType.newTypeHierarchy(project, owner, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public ITypeHierarchy newTypeHierarchy(IProgressMonitor monitor)
            throws JavaModelException
    {
        return this._realType.newTypeHierarchy(monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.ICompilationUnit[], org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public ITypeHierarchy newTypeHierarchy(ICompilationUnit[] workingCopies,
            IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.newTypeHierarchy(workingCopies, monitor);
    }

    /**
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.IWorkingCopy[], org.eclipse.core.runtime.IProgressMonitor)
	 * @deprecated Use {@link #newTypeHierarchy(ICompilationUnit[], IProgressMonitor)} instead
     */
    @Override
	public ITypeHierarchy newTypeHierarchy(org.eclipse.jdt.core.IWorkingCopy[] workingCopies,
            IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.newTypeHierarchy(workingCopies, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#newTypeHierarchy(org.eclipse.jdt.core.WorkingCopyOwner, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public ITypeHierarchy newTypeHierarchy(WorkingCopyOwner owner,
            IProgressMonitor monitor) throws JavaModelException
    {
        return this._realType.newTypeHierarchy(owner, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#resolveType(java.lang.String)
     */
    @Override
	public String[][] resolveType(String typeName) throws JavaModelException
    {
        return this._realType.resolveType(typeName);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IType#resolveType(java.lang.String, org.eclipse.jdt.core.WorkingCopyOwner)
     */
    @Override
	public String[][] resolveType(String typeName, WorkingCopyOwner owner)
            throws JavaModelException
    {
        return this._realType.resolveType(typeName, owner);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getClassFile()
     */
    @Override
	public IOrdinaryClassFile getClassFile()
    {
        return this._enclosingTeam.getClassFile();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getCompilationUnit()
     */
    @Override
	public ICompilationUnit getCompilationUnit()
    {
        return this._enclosingTeam.getCompilationUnit();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getDeclaringType()
     */
    @Override
	public IType getDeclaringType()
    {
    	return this._enclosingTeam;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getFlags()
     */
    @Override
	public int getFlags() throws JavaModelException
    {
        return this._realType.getFlags();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getNameRange()
     */
    @Override
	public ISourceRange getNameRange() throws JavaModelException
    {
        return this._realType.getNameRange();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#getType(java.lang.String, int)
     */
    @Override
	public IType getType(String name, int occurrenceCount)
    {
        return this._realType.getType(name, occurrenceCount);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IMember#isBinary()
     */
    @Override
	public boolean isBinary()
    {
        return this._enclosingTeam.isBinary();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceReference#exists()
     */
    @Override
	public boolean exists()
    {
        return this._enclosingTeam.exists() && this._realType.exists();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getAncestor(int)
     */
    @Override
	public IJavaElement getAncestor(int ancestorType)
    {
        return this._enclosingTeam.getAncestor(ancestorType);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getCorrespondingResource()
     */
    @Override
	public IResource getCorrespondingResource() throws JavaModelException
    {
        return this._enclosingTeam.getCorrespondingResource();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getElementType()
     */
    @Override
	public int getElementType()
    {
        return TYPE;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getHandleIdentifier()
     */
    @Override
	public String getHandleIdentifier()
    {
        return this._realType.getHandleIdentifier();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getJavaModel()
     */
    @Override
	public IJavaModel getJavaModel()
    {
        return this._realType.getJavaModel();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getJavaProject()
     */
    @Override
	public IJavaProject getJavaProject()
    {
        return this._enclosingTeam.getJavaProject();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getOpenable()
     */
    @Override
	public IOpenable getOpenable()
    {
        return this._enclosingTeam.getOpenable();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getParent()
     */
    @Override
	public IJavaElement getParent()
    {
        return this._enclosingTeam;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getPath()
     */
    @Override
	public IPath getPath()
    {
        String lastSegment = this._realType.getPath().lastSegment();
        if (lastSegment != null)
        {
            return this._enclosingTeam.getPath().append(lastSegment);
        }
        else
        {
            return this._enclosingTeam.getPath();
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getPrimaryElement()
     */
    @Override
	public IJavaElement getPrimaryElement()
    {
        return this._enclosingTeam.getPrimaryElement();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getResource()
     */
    @Override
	public IResource getResource()
    {
        return this._enclosingTeam.getResource();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getSchedulingRule()
     */
    @Override
	public ISchedulingRule getSchedulingRule()
    {
        return this._enclosingTeam.getSchedulingRule();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#getUnderlyingResource()
     */
    @Override
	public IResource getUnderlyingResource() throws JavaModelException
    {
        return this._enclosingTeam.getUnderlyingResource();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#isReadOnly()
     */
    @Override
	public boolean isReadOnly()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IJavaElement#isStructureKnown()
     */
    @Override
	public boolean isStructureKnown() throws JavaModelException
    {
        return this._enclosingTeam.isStructureKnown() && this._realType.isStructureKnown();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceReference#getSource()
     */
    @Override
	public String getSource() throws JavaModelException
    {
        return this._realType.getSource();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceReference#getSourceRange()
     */
    @Override
	public ISourceRange getSourceRange() throws JavaModelException
    {
        return this._realType.getSourceRange();
    }

	@Override
	public IAnnotation getAnnotation(String name) {
		return this._realType.getAnnotation(name);
	}

	@Override
	public IAnnotation[] getAnnotations() throws JavaModelException {
        if (this._allRealTypes == null)
        	return this._realType.getAnnotations();
    	List<IAnnotation> uniqueAnnotations = new ArrayList<IAnnotation>();
    	Set<String> names = new HashSet<String>();
    	for (int i = 0; i < this._allRealTypes.length; i++)
			for (IAnnotation annotation : this._allRealTypes[i].getAnnotations())
				if (names.add(annotation.getElementName()))
					uniqueAnnotations.add(annotation);
    	return uniqueAnnotations.toArray(new IAnnotation[uniqueAnnotations.size()]);
	}

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#copy(org.eclipse.jdt.core.IJavaElement, org.eclipse.jdt.core.IJavaElement, java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public void copy(IJavaElement container, IJavaElement sibling,
            String rename, boolean replace, IProgressMonitor monitor)
            throws JavaModelException
    {
        this._realType.copy(container, sibling, rename, replace, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#delete(boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public void delete(boolean force, IProgressMonitor monitor)
            throws JavaModelException
    {
        this._realType.delete(force, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#move(org.eclipse.jdt.core.IJavaElement, org.eclipse.jdt.core.IJavaElement, java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public void move(IJavaElement container, IJavaElement sibling,
            String rename, boolean replace, IProgressMonitor monitor)
            throws JavaModelException
    {
        this._realType.move(container, sibling, rename, replace, monitor);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.ISourceManipulation#rename(java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
	public void rename(String name, boolean replace, IProgressMonitor monitor)
            throws JavaModelException
    {
        this._realType.rename(name, replace, monitor);

    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IParent#getChildren()
     */
    @Override
	public IJavaElement[] getChildren() throws JavaModelException
    {
        return this._realType.getChildren();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jdt.core.IParent#hasChildren()
     */
    @Override
	public boolean hasChildren() throws JavaModelException
    {
        return this._realType.hasChildren();
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
	@Override
	public <T> T getAdapter(Class<T> adapter)
    {
        return this._realType.getAdapter(adapter);
    }

    @Override
	@SuppressWarnings("nls")
	public String toString()
    {
        return "(" + getFullyQualifiedName() + "|" + this._realType.getFullyQualifiedName('.') + ")";
    }

    public IType getRealType()
    {
        return this._realType;
    }

    private void setRealType(IType type)
    {
        if (type instanceof PhantomType)
			this._realType = ((PhantomType)type).getRealType();
		else
			this._realType = type;
    }

	@Override
	public int hashCode()
    {
		return getFullyQualifiedName().hashCode();
	}

	@Override
	public boolean equals(Object arg0)
    {
        if (arg0 == null || !(arg0 instanceof PhantomType) )
        {
        	return false;
        }

        PhantomType other = (PhantomType)arg0;
		return this._enclosingTeam.equals(other._enclosingTeam)
                    && (this._realType.equals(other._realType));
	}

	private void handleUnsupported()
    {
        throw new UnsupportedOperationException("Unsupported operation on PhantomType " //$NON-NLS-1$
                                                + getFullyQualifiedName());
    }

	@Override
	public ITypeParameter[] getTypeParameters() throws JavaModelException {
		return this._realType.getTypeParameters();
	}

	@Override
	public boolean isResolved() {
		return this._realType.isResolved() && this._enclosingTeam.isResolved();
	}

	// TODO: check whether we need to support any of those methods

	@Override
	public IJavaElement[] getChildrenForCategory(String category) throws JavaModelException {
        handleUnsupported();
		return null;
	}

	@Override
	public String getFullyQualifiedParameterizedName() throws JavaModelException {
        handleUnsupported();
		return null;
	}

	@Override
	public String getKey() {
        handleUnsupported();
		return null;
	}

	@Override
	public ITypeParameter getTypeParameter(String name) {
        handleUnsupported();
		return null;
	}

	@Override
	public String[] getCategories() throws JavaModelException {
        handleUnsupported();
		return null;
	}

	@Override
	public ISourceRange getJavadocRange() throws JavaModelException {
        return this._realType.getJavadocRange();
	}

	@Override
	public int getOccurrenceCount() {
        handleUnsupported();
		return 0;
	}

	@Override
	public String getAttachedJavadoc(IProgressMonitor monitor) throws JavaModelException {
        return this._realType.getAttachedJavadoc(monitor);
	}
}
