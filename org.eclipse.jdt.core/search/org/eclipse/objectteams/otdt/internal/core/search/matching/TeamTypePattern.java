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
 * $Id: TeamTypePattern.java 23417 2010-02-03 20:13:55Z stephan $
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.search.matching;

import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.internal.core.search.indexing.IIndexConstants;
import org.eclipse.jdt.internal.core.search.matching.TypeDeclarationPattern;

/**
 * NEW for OTDT
 * 
 * A SearchPattern for locating Team types.
 * @author gis
 */
public class TeamTypePattern extends TypeDeclarationPattern
{
    protected static char[][] TEAM_CATEGORIES = new char[][] { IIndexConstants.TEAM_DECL };
    
    /**
     * @param classOrInterface TYPE_SUFFIX, CLASS_SUFFIX or INTERFACE_SUFFIX
     * @param matchRule SearchPattern.R_EXACT_MATCH et al.
     */
    public TeamTypePattern(
            char[] pkg, 
            char[][] enclosingTypeNames, 
            char[] simpleName, 
            char classOrInterface, 
            int matchRule)
    {
        super(pkg, enclosingTypeNames, simpleName, classOrInterface, matchRule);
        this.kind = TEAM_DECL_PATTERN;
    }
    
    @Override
	public SearchPattern getBlankPattern() 
    {
    	return new TeamTypePattern(null, null, null, TYPE_SUFFIX, R_EXACT_MATCH | R_CASE_SENSITIVE);
    }
    
// Reimplement those when we need special index handling apart from the category
//    public void decodeIndexKey(char[] key)
//    {
//        super.decodeIndexKey(key);
//    }
//    public char[] getIndexKey()
//    {
//        return super.getIndexKey();
//    }
//    public boolean matchesDecodedKey(SearchPattern decodedPattern)
//    {
//        return super.matchesDecodedKey(decodedPattern);
//    }
    
    @Override
	public char[][] getIndexCategories()
    {
        return TEAM_CATEGORIES;
        //return CATEGORIES; 
    }
}
