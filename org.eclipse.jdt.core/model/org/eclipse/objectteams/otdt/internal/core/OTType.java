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
 * $Id: OTType.java 23417 2010-02-03 20:13:55Z stephan $
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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOrdinaryClassFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.core.SourceTypeElementInfo;
import org.eclipse.jdt.internal.core.util.MementoTokenizer;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.core.TypeHelper;
import org.eclipse.objectteams.otdt.core.search.OTSearchEngine;
import org.eclipse.objectteams.otdt.internal.core.search.matching.ReferenceToTeamPackagePattern;


/**
 * Generic OTType implementation. Actually this instance is only used to
 * represent teams.
 *
 * @author jwloka
 * @version $Id: OTType.java 23417 2010-02-03 20:13:55Z stephan $
 */
public class OTType extends OTJavaElement implements IOTType
{
	int flags;

	public OTType(int type, IType correspondingJavaType, IJavaElement parent, int flags)
	{
		super(type, correspondingJavaType, parent);
		this.flags                  = flags;
	}

	public OTType(int type, IType correspondingJavaType, IJavaElement parent, int flags, boolean addToParent)
	{
		super(type, correspondingJavaType, parent, addToParent);
		this.flags                  = flags;
	}

	@Override
	public boolean isRole()
	{
		return TypeHelper.isRole(this.flags);
	}

	@Override
	public boolean isTeam()
	{
		return TypeHelper.isTeam(this.flags);
	}

	/**
	 * Returns the corresponding resource, if this is a toplevel type, otherwise null.
	 */
	@Override
	public IResource getCorrespondingResource() throws JavaModelException
	{
		IJavaElement javaParent = getCorrespondingJavaElement().getParent();
		if (javaParent instanceof ICompilationUnit)
		{
			return ((ICompilationUnit)javaParent).getCorrespondingResource();
		}

		return null;
	}

	/**
	 * Inner types are represented (like in the JavaModel) as children. This
	 * methods filters IType children and returns them.
	 */
	@Override
	public IType[] getInnerTypes()
	{
		List<IType> result = new LinkedList<IType>();
		IJavaElement[] children = getChildren();

		for (int idx = 0; idx < children.length; idx++)
		{
			if (children[idx] instanceof IOTType)
			{
				result.add((IType)children[idx]);
			}
		}

		return result.toArray(new IType[result.size()]);
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public IType[] getRoleTypes(int which) throws JavaModelException
	{
	    return getRoleTypes(which, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IType[] getRoleTypes(int which, String roleName) throws JavaModelException
	{
	    ArrayList<IType> result = new ArrayList<IType>();
	    IType[] typesToConsider = null;

	    final int BOTH_HIERARCHIES_MASK = IMPLICTLY_INHERITED | EXPLICITLY_INHERITED;

	    if ((which & BOTH_HIERARCHIES_MASK) != 0)
	    {
	        ITypeHierarchy hierarchy = ((IType)getCorrespondingJavaElement()).newSupertypeHierarchy(new NullProgressMonitor());
	        typesToConsider = getTypesToSearchForRoles(hierarchy, which & BOTH_HIERARCHIES_MASK);

	    }

	    if ((which & EXCLUDE_SELF) == 0)
	    {
	        int newLen = (typesToConsider != null) ? typesToConsider.length + 1 : 1;
	        IType[] ttc = new IType[newLen];
	        ttc[0] = this;
	        if (typesToConsider != null)
	            System.arraycopy(typesToConsider, 0, ttc, 1, typesToConsider.length);
	        typesToConsider = ttc;
	    }
	    else

	    if (typesToConsider == null)
	        throw new IllegalArgumentException("EXCLUDE_SELF without a hierarchy requested"); //$NON-NLS-1$

	    typesToConsider = fixTypesToConsider(typesToConsider);

        //TODO(haebor) consider that roles from binaries can't be differentiated since external roles are inlined
		if ((which & ROLEFILE) != 0)
		{
//			packageSearchGetRoleFiles(result);
		    result.addAll(searchEngineGetRoleFiles(typesToConsider, roleName));
		}

        if ((which & INLINED) != 0)
        {
            for (int i = 0; i < typesToConsider.length; i++)
            {
                IType[] roleTypes = typesToConsider[i].getTypes();
                for (int j = 0; j < roleTypes.length; j++)
                {
                    IType currentType = roleTypes[j];
                    if (roleName == null || roleName.equals(currentType.getElementName()))
	                    result.add(currentType);
                }
            }
        }

        return result.toArray(new IType[result.size()]);
	}

	// hook for OTTypeHierarchies to select behavior depending of argument 'which':
	private IType[] getTypesToSearchForRoles(ITypeHierarchy hierarchy, int which) throws JavaModelException {
		// default case:
		return hierarchy.getAllSuperclasses(this);
	}

	private static IType[] fixTypesToConsider(IType[] typesToConsider)
    {
	    ArrayList<IType> result = new ArrayList<IType>(typesToConsider.length);

	    for (int i = 0; i < typesToConsider.length; i++)
        {
            IType type = typesToConsider[i];
            if (!TypeHelper.isOrgObjectTeamsTeam(type))
            {
                IOTType role = OTModelManager.getOTElement(type);
                if (role != null)
                    result.add(role);
            }
        }

	    return result.toArray(new IType[result.size()]);
    }

    private List<IType> searchEngineGetRoleFiles(IType[] teamsToConsider, String roleName) throws JavaModelException
    {
        final List<IType> searchResult = new ArrayList<IType>();
        char[] role = (roleName == null) ? null : roleName.toCharArray();

        try
        {

	        OTSearchEngine searchEngine = new OTSearchEngine();
            IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[]{this.getAncestor(IJavaElement.JAVA_PROJECT)});
            SearchRequestor requestor = new SearchRequestor() {
                @Override
				public void acceptSearchMatch(SearchMatch match) throws CoreException
                {
                    searchResult.add((IType)match.getElement());
                }
            };

            for (int i = 0; i < teamsToConsider.length; i++)
            {
                IType currentType = teamsToConsider[i];
                IOTType ottype = OTModelManager.getOTElement(currentType);
                if (ottype == null || !ottype.isTeam())
                	continue;

	            SearchPattern pattern =
	                new ReferenceToTeamPackagePattern(currentType.getFullyQualifiedName().toCharArray(), role,
	                        SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
	            searchEngine.search(pattern, scope, requestor, new NullProgressMonitor());
            }
        }
 		catch (CoreException exc)
        {
            throw new JavaModelException(exc);
        }
        return searchResult;
    }

    /**
	 * Returns all role types (inlined and role files) contained in this team.
	 * @throws JavaModelException
	 */
	@Override
	public IType[] getRoleTypes() throws JavaModelException
	{
	    return getRoleTypes(IOTType.INLINED | IOTType.ROLEFILE);
	}

	@Override
	public int getFlags()
	{
		return this.flags;
	}

	@Override
	public IType getRoleType(String simpleName)
	{
	    if (isTeam() && exists())
	    {
		    IType roleType = getType(simpleName);
		    try {
		    	// smarter way of asking whether the roleType exists:
			    Object info = ((JavaElement)roleType).getElementInfo();
			    // only source types need specific handling of role files.
			    // for these, getElementInfo() -> getAsRoFi() may have searched the real RoFi element, extract now:
			    if (info instanceof SourceTypeElementInfo)
			    	return ((SourceTypeElementInfo) info).getHandle();
			    return roleType;
		    } catch (JavaModelException jme) {
		    	return null;
		    }
	    }

	    return null;
	}

	@Override
	public IType searchRoleType(String simpleName) {
		try
		{
			List<IType> roleFiles = searchEngineGetRoleFiles(new IType[] { this }, simpleName);
			if (roleFiles.size() > 0)
				return roleFiles.get(0); // actually there may be more, due to multiple src-folders...

// previous implementation without search engine
//			    String encTeamName = this.getFullyQualifiedName();
//			    String qualName	  = encTeamName + "." + simpleName;
//	            roleType = getJavaProject().findType(qualName);
//	            if (roleType != null)
//	            {
//	                return roleType;
//	            }
		}
		catch (JavaModelException ignored) { /* not found */ }
		return null;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this.getCorrespondingJavaElement())
			return true;

		if(!(obj instanceof OTType))
		{
		    return false;
		}

		OTType other = (OTType)obj;

		return super.equals(other)
				&& getFlags() == other.getFlags();
	}

	@Override
	@SuppressWarnings("nls")
	public String toString()
	{
		return "OTType " + getElementName() + " for type: " + getCorrespondingJavaElement().toString();
	}

	private IType getIType()
	{
	    return (IType)super.getCorrespondingJavaElement();
	}

	/**
	 * @deprecated Use {@link #codeComplete(char[],int,int,char[][],char[][],int[],boolean,CompletionRequestor)} instead.
	 */
    @Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, org.eclipse.jdt.core.ICompletionRequestor requestor) throws JavaModelException
    {
        getIType().codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor);
    }
    /**
	 * @deprecated Use {@link #codeComplete(char[],int,int,char[][],char[][],int[],boolean,CompletionRequestor,WorkingCopyOwner)} instead.
     */
    @Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, org.eclipse.jdt.core.ICompletionRequestor requestor, WorkingCopyOwner owner) throws JavaModelException
    {
        getIType().codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor, owner);
    }
    @Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor, IProgressMonitor monitor)
    		throws JavaModelException
    {
    	getIType().codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor, monitor);
    }
    @Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor, WorkingCopyOwner owner, IProgressMonitor monitor)
    		throws JavaModelException
    {
    	getIType().codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor, owner, monitor);
    }
    @Override
	public IField createField(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().createField(contents, sibling, force, monitor);
    }

    @Override
	public IInitializer createInitializer(String contents, IJavaElement sibling, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().createInitializer(contents, sibling, monitor);
    }

    @Override
	public IMethod createMethod(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().createMethod(contents, sibling, force, monitor);
    }

    @Override
	public IType createType(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().createType(contents, sibling, force, monitor);
    }

    @Override
	public IMethod[] findMethods(IMethod method)
    {
        return getIType().findMethods(method);
    }

    @Override
	public IField getField(String name)
    {
        return getIType().getField(name);
    }

    @Override
	public IField[] getFields() throws JavaModelException
    {
        return getIType().getFields();
    }

    @Override
    public IField getRecordComponent(String name) {
    	return null;
    }

    @Override
	public String getFullyQualifiedName()
    {
        return getIType().getFullyQualifiedName();
    }

    @Override
	public String getFullyQualifiedName(char enclosingTypeSeparator)
    {
        return getIType().getFullyQualifiedName(enclosingTypeSeparator);
    }

    @Override
	public IInitializer getInitializer(int occurrenceCount)
    {
        return getIType().getInitializer(occurrenceCount);
    }

    @Override
	public IInitializer[] getInitializers() throws JavaModelException
    {
        return getIType().getInitializers();
    }

    @Override
	public IMethod getMethod(String name, String[] parameterTypeSignatures)
    {
        return getIType().getMethod(name, parameterTypeSignatures);
    }

    @Override
	public IMethod[] getMethods() throws JavaModelException
    {
        return getIType().getMethods();
    }

    @Override
	public IAnnotation getAnnotation(String name) {
    	return getIType().getAnnotation(name);
    }

    @Override
	public IAnnotation[] getAnnotations() throws JavaModelException {
    	return getIType().getAnnotations();
    }

    @Override
	public IPackageFragment getPackageFragment()
    {
        return getIType().getPackageFragment();
    }

    @Override
	public String getSuperclassName() throws JavaModelException
    {
        return getIType().getSuperclassName();
    }

    @Override
	public String getSuperclassTypeSignature() throws JavaModelException
    {
        return getIType().getSuperclassTypeSignature();
    }

    @Override
	public String[] getSuperInterfaceTypeSignatures() throws JavaModelException
    {
        return getIType().getSuperInterfaceTypeSignatures();
    }

    @Override
	public String[] getSuperInterfaceNames() throws JavaModelException
    {
        return getIType().getSuperInterfaceNames();
    }

    @Override
	public String[] getTypeParameterSignatures() throws JavaModelException
    {
        return getIType().getTypeParameterSignatures();
    }

    @Override
	public IType getType(String name)
    {
        return getIType().getType(name);
    }

    @Override
	public String getTypeQualifiedName()
    {
        return getIType().getTypeQualifiedName();
    }

    @Override
	public String getTypeQualifiedName(char enclosingTypeSeparator)
    {
        return getIType().getTypeQualifiedName(enclosingTypeSeparator);
    }

    @Override
	public IType[] getTypes() throws JavaModelException
    {
		return getIType().getTypes();
    }

    @Override
	public boolean isAnonymous() throws JavaModelException
    {
        return getIType().isAnonymous();
    }

    @Override
	public boolean isClass() throws JavaModelException
    {
        return getIType().isClass();
    }

    @Override
	public boolean isEnum() throws JavaModelException
    {
        return getIType().isEnum();
    }

	@Override
	public boolean isRecord() throws JavaModelException {
		return getIType().isRecord();
	}

    @Override
	public boolean isInterface() throws JavaModelException
    {
        return getIType().isInterface();
    }

    @Override
	public boolean isAnnotation() throws JavaModelException
    {
        return getIType().isAnnotation();
    }

    @Override
	public boolean isLocal() throws JavaModelException
    {
        return getIType().isLocal();
    }

    @Override
	public boolean isMember() throws JavaModelException
    {
        return getIType().isMember();
    }

	@Override
	public boolean isLambda() {
		return false;
	}

	@Override
	public ITypeHierarchy loadTypeHierachy(InputStream input, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().loadTypeHierachy(input, monitor);
    }

    @Override
	public ITypeHierarchy newSupertypeHierarchy(IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newSupertypeHierarchy(monitor);
    }

    @Override
	public ITypeHierarchy newSupertypeHierarchy(ICompilationUnit[] workingCopies, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newSupertypeHierarchy(workingCopies, monitor);
    }
    /**
	 * @deprecated Use {@link #newSupertypeHierarchy(ICompilationUnit[], IProgressMonitor)} instead
     */
    @Override
	public ITypeHierarchy newSupertypeHierarchy(org.eclipse.jdt.core.IWorkingCopy[] workingCopies, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newSupertypeHierarchy(workingCopies, monitor);
    }

    @Override
	public ITypeHierarchy newSupertypeHierarchy(WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newSupertypeHierarchy(owner, monitor);
    }

    @Override
	public ITypeHierarchy newTypeHierarchy(IJavaProject project, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newTypeHierarchy(project, monitor);
    }

    @Override
	public ITypeHierarchy newTypeHierarchy(IJavaProject project, WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newTypeHierarchy(project, owner, monitor);
    }

    @Override
	public ITypeHierarchy newTypeHierarchy(IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newTypeHierarchy(monitor);
    }

    @Override
	public ITypeHierarchy newTypeHierarchy(ICompilationUnit[] workingCopies, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newTypeHierarchy(workingCopies, monitor);
    }
    /**
	 * @deprecated Use {@link #newTypeHierarchy(ICompilationUnit[], IProgressMonitor)} instead
     */
    @Override
	public ITypeHierarchy newTypeHierarchy(org.eclipse.jdt.core.IWorkingCopy[] workingCopies, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newTypeHierarchy(workingCopies, monitor);
    }

    @Override
	public ITypeHierarchy newTypeHierarchy(WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaModelException
    {
        return getIType().newTypeHierarchy(owner, monitor);
    }

    @Override
	public String[][] resolveType(String typeName) throws JavaModelException
    {
        return getIType().resolveType(typeName);
    }

    @Override
	public String[][] resolveType(String typeName, WorkingCopyOwner owner) throws JavaModelException
    {
        return getIType().resolveType(typeName, owner);
    }

    @Override
	public IOrdinaryClassFile getClassFile()
    {
        return getIType().getClassFile();
    }

    @Override
	public ICompilationUnit getCompilationUnit()
    {
        return getIType().getCompilationUnit();
    }

    @Override
	public ITypeRoot getTypeRoot() {
    	return getIType().getTypeRoot();
    }

    @Override
	public IType getDeclaringType()
    {
        return getIType().getDeclaringType();
    }

    @Override
	public ISourceRange getNameRange() throws JavaModelException
    {
        return getIType().getNameRange();
    }

    @Override
	public IType getType(String name, int occurrenceCount)
    {
        return getIType().getType(name, occurrenceCount);
    }

    @Override
	public boolean isBinary()
    {
        return getIType().isBinary();
    }

    @Override
	public String getSource() throws JavaModelException
    {
        return getIType().getSource();
    }

    @Override
	public ISourceRange getSourceRange() throws JavaModelException
    {
        return getIType().getSourceRange();
    }

    @Override
	public void copy(IJavaElement container, IJavaElement sibling, String rename, boolean replace, IProgressMonitor monitor) throws JavaModelException
    {
        getIType().copy(container, sibling, rename, replace, monitor);
    }

    @Override
	public void delete(boolean force, IProgressMonitor monitor) throws JavaModelException
    {
        getIType().delete(force, monitor);
    }

    @Override
	public void move(IJavaElement container, IJavaElement sibling, String rename, boolean replace, IProgressMonitor monitor) throws JavaModelException
    {
        getIType().move(container, sibling, rename, replace, monitor);
    }

    @Override
	public void rename(String name, boolean replace, IProgressMonitor monitor) throws JavaModelException
    {
        getIType().rename(name, replace, monitor);
    }

	@Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor) throws JavaModelException {
		getIType().codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor);
	}

	@Override
	public void codeComplete(char[] snippet, int insertion, int position, char[][] localVariableTypeNames, char[][] localVariableNames, int[] localVariableModifiers, boolean isStatic, CompletionRequestor requestor, WorkingCopyOwner owner) throws JavaModelException {
		getIType().codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor, owner);
	}

	@Override
	public IJavaElement[] getChildrenForCategory(String category) throws JavaModelException {
		return getIType().getChildrenForCategory(category);
	}

	@Override
	public String getFullyQualifiedParameterizedName() throws JavaModelException {
		return getIType().getFullyQualifiedParameterizedName();
	}

	@Override
	public String getKey() {
		return getIType().getKey();
	}

	@Override
	public ITypeParameter[] getTypeParameters() throws JavaModelException {
		return getIType().getTypeParameters();
	}

	@Override
	public ITypeParameter getTypeParameter(String name) {
		return getIType().getTypeParameter(name);
	}

	@Override
	public boolean isResolved() {
		return getIType().isResolved();
	}

	@Override
	public String[] getCategories() throws JavaModelException {
		return getIType().getCategories();
	}

	@Override
	public ISourceRange getJavadocRange() throws JavaModelException {
		return getIType().getJavadocRange();
	}

	@Override
	public int getOccurrenceCount() {
		return getIType().getOccurrenceCount();
	}

	@Override
	public String getAttachedJavadoc(IProgressMonitor monitor) throws JavaModelException {
		return getIType().getAttachedJavadoc(monitor);
	}

	@Override
	public void close() throws JavaModelException {
		super.close();
		OTModelManager.removeOTElement(this);
	}

	@Override
	protected Object createElementInfo() {
		throw new UnsupportedOperationException("Not yet implemented for OTType");
	}

	@Override
	public IJavaElement getHandleFromMemento(String token, MementoTokenizer memento, WorkingCopyOwner owner) {
		return ((JavaElement) getCorrespondingJavaElement()).getHandleFromMemento(token, memento, owner);
	}

	@Override
	public String[] getPermittedSubtypeNames() throws JavaModelException {
		return getIType().getPermittedSubtypeNames();
	}

	@Override
	public boolean isSealed() throws JavaModelException {
		return getIType().isSealed();
	}
}
