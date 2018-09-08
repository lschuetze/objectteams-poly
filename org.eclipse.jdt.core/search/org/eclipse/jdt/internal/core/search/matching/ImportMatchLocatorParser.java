/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Fraunhofer FIRST - extended API and implementation
 *     Technical University Berlin - extended API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.objectteams.otdt.internal.core.search.matching.ReferenceToTeamLocator;

class ImportMatchLocatorParser extends MatchLocatorParser {

	boolean reportImportMatch;

protected ImportMatchLocatorParser(ProblemReporter problemReporter, MatchLocator locator) {
	super(problemReporter, locator);
	this.reportImportMatch = this.patternFineGrain == 0 || (this.patternFineGrain & IJavaSearchConstants.IMPORT_DECLARATION_TYPE_REFERENCE) != 0;
}
@Override
protected void consumeStaticImportOnDemandDeclarationName() {
	super.consumeStaticImportOnDemandDeclarationName();
	if (this.reportImportMatch) {
		this.patternLocator.match(this.astStack[this.astPtr], this.nodeSet);
	}
}
@Override
protected void consumeSingleStaticImportDeclarationName() {
	super.consumeSingleStaticImportDeclarationName();
	if (this.reportImportMatch) {
		this.patternLocator.match(this.astStack[this.astPtr], this.nodeSet);
	}
}
//{ObjectTeams:
@Override
protected void consumeSingleBaseImportDeclarationName() {
	super.consumeSingleBaseImportDeclarationName();
	if (this.reportImportMatch) {
		this.patternLocator.match(this.astStack[this.astPtr], this.nodeSet);
	}
}
// SH}
@Override
protected void consumeSingleTypeImportDeclarationName() {
	super.consumeSingleTypeImportDeclarationName();
	if (this.reportImportMatch) {
		this.patternLocator.match(this.astStack[this.astPtr], this.nodeSet);
	}
}
@Override
protected void consumeTypeImportOnDemandDeclarationName() {
	super.consumeTypeImportOnDemandDeclarationName();
	if (this.reportImportMatch) {
		this.patternLocator.match(this.astStack[this.astPtr], this.nodeSet);
	}
}
//{ObjectTeams: consider type references in team packages
@Override
protected void consumePackageDeclarationNameWithModifiers()
{
	super.consumePackageDeclarationNameWithModifiers();
	if (this.currentIsRole)
	{
	    if (   this.patternLocator instanceof TypeReferenceLocator
	    	|| this.patternLocator instanceof ReferenceToTeamLocator)
	    {
	        ImportReference importRef = this.compilationUnit.currentPackage;
	        int level = this.patternLocator.matchLevel(importRef);
			if (level != 0)
	            this.nodeSet.addMatch(importRef, level);
	    }
	}
}
// SH}
}
