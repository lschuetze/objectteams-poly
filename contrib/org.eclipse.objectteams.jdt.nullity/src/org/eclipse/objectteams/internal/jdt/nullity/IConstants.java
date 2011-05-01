/*******************************************************************************
 * Copyright (c) 2011 GK Software AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephan Herrmann - initial API and implementation 
 *******************************************************************************/
package org.eclipse.objectteams.internal.jdt.nullity;

import static org.eclipse.jdt.core.compiler.IProblem.ImportRelated;
import static org.eclipse.jdt.core.compiler.IProblem.Internal;
import static org.eclipse.jdt.core.compiler.IProblem.MethodRelated;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;

@SuppressWarnings("restriction")
public interface IConstants {

	/** Additional constants for {@link org.eclipse.jdt.internal.compiler.lookup.TagBits}. */
	static interface TagBits extends org.eclipse.jdt.internal.compiler.lookup.TagBits {
		long HasBoundArguments = ASTNode.Bit13; // for method bindings to avoid duplicate invocation of bindArguments()
		 // the following two should be added to TagBits.AllStandardAnnotationsMask:
		long AnnotationNullable = ASTNode.Bit54L;
		long AnnotationNonNull = ASTNode.Bit55L;
		long AnnotationNullableByDefault = ASTNode.Bit56L;
		long AnnotationNonNullByDefault = ASTNode.Bit57L;
	}
	
	/** Additional constants for {@link org.eclipse.jdt.internal.compiler.lookup.TypeIds}. */
	static interface TypeIds {
		final int T_ConfiguredAnnotationNullable = 60;
		final int T_ConfiguredAnnotationNonNull = 61;
		final int T_ConfiguredAnnotationNullableByDefault = 62;
		final int T_ConfiguredAnnotationNonNullByDefault = 63;
	}

	/** Additional constants for {@link org.eclipse.jdt.core.compiler.IProblem}. */
	static interface IProblem {
		/** @since 3.7 */
		int DefiniteNullFromNonNullMethod = MethodRelated + 880;
		/** @since 3.7 */
		int PotentialNullFromNonNullMethod = MethodRelated + 881;
		/** @since 3.7 */
		int NonNullReturnInsufficientInfo = MethodRelated + 882;
		/** @since 3.7 */
		int DefiniteNullToNonNullParameter = MethodRelated + 883;
		/** @since 3.7 */
		int PotentialNullToNonNullParameter = MethodRelated + 884;
		/** @since 3.7 */
		int NonNullParameterInsufficientInfo = MethodRelated + 885;
		/** @since 3.7 */
		int DefiniteNullToNonNullLocal = Internal + 886;
		/** @since 3.7 */
		int PotentialNullToNonNullLocal = Internal + 887;
		/** @since 3.7 */
		int NonNullLocalInsufficientInfo = Internal + 888;
		/** @since 3.7 */
		int ConflictingTypeEmulation = ImportRelated + 889;
		/** @since 3.7 */
		int MissingNullAnnotationType = ImportRelated + 890;
		/** @since 3.7 */
		int IllegalRedefinitionToNullableReturn = MethodRelated + 891;
		/** @since 3.7 */
		int IllegalRedefinitionToNonNullParameter = MethodRelated + 892;
		/** @since 3.7 */
		int IllegalDefinitionToNonNullParameter = MethodRelated + 893;
		/** @since 3.7 */
		int PotentialNullMessageSendReference = Internal + 894;
		/** @since 3.7 */
		int RedundantNullCheckOnNonNullMessageSend = 895;
	}
}
