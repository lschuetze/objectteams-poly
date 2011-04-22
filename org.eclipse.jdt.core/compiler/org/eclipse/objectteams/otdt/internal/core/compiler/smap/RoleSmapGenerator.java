/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2006 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: RoleSmapGenerator.java 23417 2010-02-03 20:13:55Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/

package org.eclipse.objectteams.otdt.internal.core.compiler.smap;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.eclipse.objectteams.otdt.core.compiler.ISMAPConstants;

/** Generates smap for a RoleType. *
 * @author ike */
public class RoleSmapGenerator extends AbstractSmapGenerator
{

    public RoleSmapGenerator(TypeDeclaration type)
    {
        super(type);
    }

    public char[] generate()
    {
        for (Iterator<SmapStratum> iter = this._strata.iterator(); iter.hasNext();)
        {
            SmapStratum stratum = iter.next();

            if(stratum.getStratumName().equals(ISMAPConstants.OTJ_STRATUM_NAME))
            {
                return generateOTJSmap(stratum);
            }
        }

        return null;
    }

    private char[] generateOTJSmap(SmapStratum stratum)
    {
        LineInfoCollector lineInfoCollector = new LineInfoCollector();

        //faster generation, if role is just roleFile
        if (generatePartialOTJSmap(stratum, lineInfoCollector))
        {
            return getSMAP().toCharArray();
        }

        fillSmap(stratum, lineInfoCollector);

        return getSMAP().toCharArray();
    }

	public boolean generatePartialOTJSmap(SmapStratum stratum, LineInfoCollector lineInfoCollector)
    {
        LineNumberProvider provider = this._type.getRoleModel().getLineNumberProvider();

        String sourceName = new String();
        String absoluteSourceName = new String();
        boolean isCompleted = false;

        if (!provider.containsLineInfos())
        {
            if(this._type.isRoleFile())
            {
                sourceName = getSourceNameFromRoleTypeDecl(this._type) + ISMAPConstants.OTJ_JAVA_ENDING;
                absoluteSourceName = getAbsoluteSourcePath(getPackagePathFromRoleTypeDecl(this._type), sourceName);

                int maxLineNumber = this._type.getRoleModel()._maxLineNumber;

                FileInfo fileInfo = stratum.getOrCreateFileInfo(sourceName, absoluteSourceName);
                LineInfo lineInfo = new LineInfo(ISMAPConstants.OTJ_START_LINENUMBER, ISMAPConstants.OTJ_START_LINENUMBER);
                lineInfo.setRepeatCount(maxLineNumber);
                fileInfo.addLineInfo(lineInfo);

                LineInfo stepOverLineInfo = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER, ISMAPConstants.STEP_OVER_LINENUMBER);
                fileInfo.addLineInfo(stepOverLineInfo);
                lineInfoCollector.storeLineInfo(stepOverLineInfo);

                LineInfo stepIntoLineInfo = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER, ISMAPConstants.STEP_INTO_LINENUMBER);
                fileInfo.addLineInfo(stepIntoLineInfo);
                lineInfoCollector.storeLineInfo(stepIntoLineInfo);

                isCompleted = true;

                return isCompleted;
            }
        }

        for (Iterator<ReferenceBinding> iter = provider.getLineInfos().keySet().iterator(); iter.hasNext();)
        {
            ReferenceBinding copySrc = iter.next();
            List <LineInfo> lineInfos = provider.getLineInfosForType(copySrc);

            //superrole is a rolefile
            if (isCopySrcRoleFile(copySrc))
            {
                TypeDeclaration superrole = copySrc.roleModel.getClassPartAst();
                sourceName = getSourceNameFromRoleTypeDecl(superrole) + ISMAPConstants.OTJ_JAVA_ENDING;
                absoluteSourceName = getAbsoluteSourcePath(getPackagePathFromRoleTypeDecl(superrole), sourceName);
            }
            else
            {
            	ReferenceBinding toplevelType = copySrc, currentBinding = copySrc;
            	while ((currentBinding = currentBinding.enclosingType()) != null) {
					toplevelType = currentBinding;
					if (isCopySrcRoleFile(toplevelType))
						break;
				}
                sourceName = getSourceNameFromRefBinding(toplevelType) + ISMAPConstants.OTJ_JAVA_ENDING;
                absoluteSourceName = getAbsoluteSourcePath(getPackagePathFromRefBinding(toplevelType), sourceName);
            }

            FileInfo fileInfo = stratum.getOrCreateFileInfo(sourceName, absoluteSourceName);
            fileInfo.addLineInfo(lineInfos);
            lineInfoCollector.storeLineInfos(lineInfos);
        }

        return isCompleted;
    }

	/** Create mappings on itself. All unused linenumbers(from 1 to maxLinenumber) are mapped to enclosing type which,
	 * contains the role. Special linenumber (ISMAPConstants.STEP_OVER_LINENUMBER, ISMAPConstants.STEP_INTO_LINENUMBER)
	 * are added, too.
	 *
	 */
    private void fillSmap(SmapStratum stratum, LineInfoCollector lineInfoCollector)
    {
        LineNumberProvider provider = this._type.getRoleModel().getLineNumberProvider();
        FileInfo fileInfo;
        
        List<FileInfo> knownFileInfos = stratum.getFileInfos();
        if (this._type.isPurelyCopied && !knownFileInfos.isEmpty()) {
			fileInfo = knownFileInfos.get(0); // current type has no source, add special lines to existing fileInfo
		} else {
	    	String sourceName;
	    	String absoluteSourceName;
	        if (this._type.isRoleFile())
	        {
	            sourceName = getSourceNameFromRoleTypeDecl(this._type) + ISMAPConstants.OTJ_JAVA_ENDING;
	            absoluteSourceName = getAbsoluteSourcePath(getPackagePathFromRoleTypeDecl(this._type), sourceName);
	        }
	        else
	        {
	        	sourceName = getRootEnclosingTypeNameFromTypeDecl(this._type) + ISMAPConstants.OTJ_JAVA_ENDING;
	        	absoluteSourceName = getAbsoluteSourcePath(getPackagePathFromTypeDecl(this._type), sourceName);
	        }
	
	        fileInfo = stratum.getOrCreateFileInfo(sourceName, absoluteSourceName);
	        int[] lineSeparatorPositions = this._type.compilationResult.lineSeparatorPositions;
			int startLine = lineSeparatorPositions == null 
						? 1
						: Util.getLineNumber(this._type.sourceStart, lineSeparatorPositions, 0, lineSeparatorPositions.length-1);
	        for (int idx = startLine; idx <= provider.getSourceEndLineNumber(); idx++)
	        {
	            if (!lineInfoCollector.existsLineInfoFor(idx))
	            {
	            	LineInfo newLineInfo = new LineInfo(idx,idx);
	            	fileInfo.addLineInfo(newLineInfo);
	                lineInfoCollector.storeLineInfo(newLineInfo);
	            }
	        }
        }

        if (!lineInfoCollector.existsLineInfoFor(ISMAPConstants.STEP_OVER_LINENUMBER))
        {
            LineInfo stepOverLineInfo = new LineInfo(ISMAPConstants.STEP_OVER_LINENUMBER, ISMAPConstants.STEP_OVER_LINENUMBER);
            fileInfo.addLineInfo(stepOverLineInfo);
            lineInfoCollector.storeLineInfo(stepOverLineInfo);
        }

        if (!lineInfoCollector.existsLineInfoFor(ISMAPConstants.STEP_INTO_LINENUMBER))
        {
        	LineInfo stepIntoLineInfo = new LineInfo(ISMAPConstants.STEP_INTO_LINENUMBER, ISMAPConstants.STEP_INTO_LINENUMBER);
        	fileInfo.addLineInfo(stepIntoLineInfo);
        	lineInfoCollector.storeLineInfo(stepIntoLineInfo);
        }
    }

    boolean isCopySrcRoleFile(ReferenceBinding copySrc)
    {
    	if (copySrc.roleModel == null)
    		return false; // assumably not a role
        TypeDeclaration typeDecl = copySrc.roleModel.getClassPartAst();
        if (typeDecl != null)
        {
            return typeDecl.isRoleFile();
        }

        return false;
    }

    protected String getSourceNameFromRefBinding(ReferenceBinding binding)
    {
        return String.valueOf(binding.sourceName());
    }

    protected String getRootEnclosingTypeNameFromTypeDecl(TypeDeclaration type)
    {
        TypeDeclaration enclosingTypeDecl = type.enclosingType;
        if(enclosingTypeDecl != null)
        {
            return getRootEnclosingTypeNameFromTypeDecl(enclosingTypeDecl);
        }
        else
        {
            return String.valueOf(type.name);
        }
    }

    protected String getPackagePathFromTypeDecl(TypeDeclaration type)
    {
        TypeDeclaration enclosingTypeDecl = type.enclosingType;
        if(enclosingTypeDecl != null)
        {
            return getPackagePathFromTypeDecl(enclosingTypeDecl);
        }
        else
        {
            return getPackagePathFromRefBinding(type.binding);
        }
    }

    protected String getSourceNameFromRoleTypeDecl(TypeDeclaration type)
    {
        return String.valueOf(type.getRoleModel().getInterfacePartBinding().sourceName);
    }

    protected String getPackagePathFromRoleTypeDecl(TypeDeclaration type)
    {
        CompilationUnitDeclaration cuDecl = type.compilationUnit;
        if (cuDecl == null)
        {
            cuDecl = type.getModel().getAst().compilationUnit;
        }

        ImportReference ref = cuDecl.currentPackage;
        char [][] packageParts = ref.getImportName();
        StringBuffer packageName = new StringBuffer();
        for (int idx = 0; idx < packageParts.length; idx++)
        {
            packageName.append(String.valueOf(packageParts[idx]) + ISMAPConstants.OTJ_PATH_DELIMITER);
        }

        if (packageName.length() > 0)
            return packageName.toString();

        return null;
    }
}
