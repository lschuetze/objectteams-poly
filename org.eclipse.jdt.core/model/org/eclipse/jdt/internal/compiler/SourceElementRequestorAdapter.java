/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: SourceElementRequestorAdapter.java 19913 2009-04-18 23:20:09Z stephan $
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Fraunhofer FIRST - extended API and implementation
 *     Technical University Berlin - extended API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;

public class SourceElementRequestorAdapter implements ISourceElementRequestor {

	/**
	 * @see ISourceElementRequestor#acceptAnnotationTypeReference(char[][], int, int)
	 */
	public void acceptAnnotationTypeReference(
		char[][] typeName,
		int sourceStart,
		int sourceEnd) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptAnnotationTypeReference(char[], int)
	 */
	public void acceptAnnotationTypeReference(char[] typeName, int sourcePosition) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptConstructorReference(char[], int, int)
	 */
	public void acceptConstructorReference(
		char[] typeName,
		int argCount,
		int sourcePosition) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptFieldReference(char[], int)
	 */
	public void acceptFieldReference(char[] fieldName, int sourcePosition) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptImport(int, int, int, int, char[][], boolean, int)
	 */
	public void acceptImport(
		int declarationStart,
		int declarationEnd,
		int nameStart,
		int nameEnd,
		char[][] tokens,
		boolean onDemand,
		int modifiers) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptLineSeparatorPositions(int[])
	 */
	public void acceptLineSeparatorPositions(int[] positions) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptMethodReference(char[], int, int)
	 */
	public void acceptMethodReference(
		char[] methodName,
		int argCount,
		int sourcePosition) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptPackage(ImportReference)
	 */
	public void acceptPackage(ImportReference importReference) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptProblem(CategorizedProblem)
	 */
	public void acceptProblem(CategorizedProblem problem) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptTypeReference(char[][], int, int)
	 */
	public void acceptTypeReference(
		char[][] typeName,
		int sourceStart,
		int sourceEnd) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptTypeReference(char[], int)
	 */
	public void acceptTypeReference(char[] typeName, int sourcePosition) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptUnknownReference(char[][], int, int)
	 */
	public void acceptUnknownReference(
		char[][] name,
		int sourceStart,
		int sourceEnd) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#acceptUnknownReference(char[], int)
	 */
	public void acceptUnknownReference(char[] name, int sourcePosition) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#enterCompilationUnit()
	 */
	public void enterCompilationUnit() {
		// default implementation: do nothing
	}

	public void enterConstructor(MethodInfo methodInfo) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#enterField(ISourceElementRequestor.FieldInfo)
	 */
	public void enterField(FieldInfo fieldInfo) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#enterInitializer(int, int)
	 */
	public void enterInitializer(int declarationStart, int modifiers) {
		// default implementation: do nothing
	}

	public void enterMethod(MethodInfo methodInfo) {
		// default implementation: do nothing
	}

	public void enterType(TypeInfo typeInfo) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#exitCompilationUnit(int)
	 */
	public void exitCompilationUnit(int declarationEnd) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#exitConstructor(int)
	 */
	public void exitConstructor(int declarationEnd) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#exitField(int, int, int)
	 */
	public void exitField(int initializationStart, int declarationEnd, int declarationSourceEnd) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#exitInitializer(int)
	 */
	public void exitInitializer(int declarationEnd) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#exitMethod(int, Expression)
	 */
	public void exitMethod(int declarationEnd, Expression defaultValue) {
		// default implementation: do nothing
	}

	/**
	 * @see ISourceElementRequestor#exitType(int)
	 */
	public void exitType(int declarationEnd) {
		// default implementation: do nothing
	}
//{OTDTUI: added default implementation to corresponding extension in ISourceElementRequestor
    public void enterCalloutMapping(CalloutInfo calloutInfo)
    {
		// default implementation: do nothing
    }
    public void enterCalloutToFieldMapping(CalloutToFieldInfo calloutInfo)
    {
        // default implementation: do nothing
    }
    public void enterCallinMapping(CallinInfo callinInfo)
    {
		// default implementation: do nothing
    }
    public void exitCallinMapping(int sourceEnd, int declarationSourceEnd)
    {
		// default implementation: do nothing
    }
    public void exitCalloutMapping(int sourceEnd, int declarationSourceEnd)
    {
		// default implementation: do nothing
    }
    public void exitCalloutToFieldMapping(int sourceEnd, int declarationSourceEnd)
    {
    	// default implementation: do nothing
    }
//jwl}
//  {ObjectTeams: default implementation for baseReference
    public void acceptBaseReference(char[][] typeName, int sourceStart, int sourceEnd){}
//    haebor}

}

