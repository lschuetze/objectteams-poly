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
 * $Id: TsuperReference.java 19873 2009-04-13 16:51:05Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.impl.Constant;

/**
 * NEW for OTDT.
 *
 * What: the receiver for tsuper.myMethod() calls.
 *
 * What: Resolve the tsuper role corresponding to this tsuper reference.
 * How:  Have to respect qualification (-> selectTSuper()).
 *
 * @author macwitte
 * @version $Id: TsuperReference.java 19873 2009-04-13 16:51:05Z stephan $
 */
public class TsuperReference extends ThisReference {

/** for qualified tsuper a la: WayOuterTeam.tsuper.m(). */
public TypeReference qualification;

public TsuperReference(int pos, int sourceEnd) {
	super(pos,sourceEnd);
	this.sourceStart = pos;
	this.sourceEnd = sourceEnd;
}

@Override
public boolean isSuper() {
	return false;
}
@Override
public boolean isThis() {
	return true;
}


public boolean checkAccess(MethodScope methodScope) {
	// see checkAccess of ThisReference

	SourceTypeBinding roleType = methodScope.enclosingSourceType();
	return roleType.isDirectRole(); // error will be reported in addMarkerArg (invoked from TSuperMessageSend)
}

@Override
public TypeBinding resolveType(BlockScope scope) {
	if (this.resolvedType != null)
		return this.resolvedType;

	this.constant = Constant.NotAConstant;
	if (!isImplicitThis() && !checkAccess(scope.methodScope()))
		return null;
	ReferenceBinding enclosingRole = scope.enclosingSourceType(); // checked to be a role

	// simple case first:
	if (this.qualification == null)
		return this.resolvedType = enclosingRole.roleModel.getTSuperRoleBinding();


	TypeBinding  qualifyingType = this.qualification.resolveType(scope);
	if (qualifyingType != null && (qualifyingType instanceof ReferenceBinding))
	{
		ReferenceBinding   qualRef     = (ReferenceBinding)qualifyingType;
		ReferenceBinding[] tsuperRoles = enclosingRole.roleModel.getTSuperRoleBindings();
		ReferenceBinding   superRole   = selectTSuper(qualRef, tsuperRoles);
		if (superRole == null) {
			scope.problemReporter().invalidQualifiedTSuper(
										this, qualRef.superclass(), enclosingRole);
			return null;
		}
		return this.resolvedType = superRole;
	}
	return null;
}
/** find the suitable tsuper role, which is determined as being the first tsuper role
 *  which lies inside the superclass of the qualifying type.
 *  TA1
 *    TB1
 * 		R
 *    TB2
 *      R
 *  TA2
 *    TB1
 *      R
 *    TB2
 *      R
 *        TB2.tsuper
 *  superTeam = TA2.TB1
 *  tsupers = TA1.TB2.R (outside TA2.TB1), TA2.TB1.R (inside TA2.TB1) => result TA2.TB1.R
 */
private ReferenceBinding selectTSuper(ReferenceBinding qualifyingType, ReferenceBinding[] tsupers)
{
	ReferenceBinding superTeam = qualifyingType.getRealClass().superclass();
	for (int i = tsupers.length-1; i >= 0; i--) {
		if (contains(superTeam, tsupers[i]))
			return tsupers[i];
	}
	return null;
}
/** is inner contained (via reverse of enclosingType) in outer? */
private boolean contains(ReferenceBinding outer, ReferenceBinding inner) {
	ReferenceBinding current = inner.enclosingType();
	while (current != null) {
		if (TypeBinding.equalsEquals(current, outer))
			return true;
		current = current.enclosingType();
	}
	return false;
}


@Override
public StringBuffer printExpression(int indent, StringBuffer output){
	if (this.qualification != null) {
		this.qualification.printExpression(indent, output);
		output.append("."); //$NON-NLS-1$
	}
    output.append("tsuper"); //$NON-NLS-1$
    return output;
}

@Override
public void traverse(ASTVisitor visitor, BlockScope blockScope) {
	visitor.visit(this, blockScope);
	visitor.endVisit(this, blockScope);
}
}
