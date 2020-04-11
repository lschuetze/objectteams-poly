/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2006 Fraunhofer Gesellschaft, Munich, Germany,
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
 * $Id: RoleSmapGenerator.java 23417 2010-02-03 20:13:55Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/

package org.eclipse.objectteams.otdt.internal.core.compiler.smap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jdt.core.compiler.CharOperation;
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

    @Override
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
        boolean isCompleted = false;

        if (!provider.containsLineInfos())
        {
            if(this._type.isRoleFile())
            {
                FileInfo fileInfo = getOrCreateFileInfoForType(stratum, this._type.binding);

                int maxLineNumber = this._type.getRoleModel()._maxLineNumber;
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

        Hashtable<ReferenceBinding, Vector<LineInfo>> allLineInfos = provider.getLineInfos();
		if (!allLineInfos.isEmpty()) {
			// ensure main type (if it exists in source) is first:
			if (!this._type.isPurelyCopied)
				getOrCreateFileInfoForType(stratum, getCUType(this._type.binding));

			Set<ReferenceBinding> typesSet = allLineInfos.keySet();
	        // for testability ensure stable order:
	        ReferenceBinding[] types = typesSet.toArray(new ReferenceBinding[typesSet.size()]);
	        Arrays.sort(types, new Comparator<ReferenceBinding>() {
	        	@Override
				public int compare(ReferenceBinding o1, ReferenceBinding o2) { return CharOperation.compareTo(o1.constantPoolName(), o2.constantPoolName()); }
			});
			for (ReferenceBinding copySrc : types)
	        {
	            List <LineInfo> lineInfos = provider.getLineInfosForType(copySrc);

	            FileInfo fileInfo = getOrCreateFileInfoForType(stratum, getCUType(copySrc));
	            fileInfo.addLineInfo(lineInfos);
	            lineInfoCollector.storeLineInfos(lineInfos);
	        }
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
			fileInfo = getOrCreateFileInfoForType(stratum, getCUType(this._type.binding));

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
    	return copySrc.roleModel.isRoleFile();
    }
}
