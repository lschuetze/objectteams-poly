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
import static org.eclipse.jdt.core.compiler.IProblem.TypeRelated;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

@SuppressWarnings("restriction")
public class Constants {

	/** Additional constants for {@link org.eclipse.jdt.internal.compiler.lookup.TagBits}. */
	static interface TagBits extends org.eclipse.jdt.internal.compiler.lookup.TagBits {
		 // the following two should be added to TagBits.AllStandardAnnotationsMask:
		
		// MethodBinding or LocalVariableBinding (argument):
		long AnnotationNullable = ASTNode.Bit59L;
		long AnnotationNonNull = ASTNode.Bit60L;
		// PackageBinding or TypeBinding or MethodBinding:
		long AnnotationNullUnspecifiedByDefault = ASTNode.Bit61L;
		long AnnotationNonNullByDefault = ASTNode.Bit62L;
	}
	
	// ASTNode:
	// for annotation reference:
	public static final int IsSynthetic = ASTNode.Bit7;
	// for method declaration to avoid duplicate invocation of bindArguments()
	public static final int HasBoundArguments = ASTNode.Bit10; 
	
	/** Additional constants for {@link org.eclipse.jdt.internal.compiler.lookup.TypeIds}. */
	static interface TypeIds {
		final int T_ConfiguredAnnotationNullable = 80;
		final int T_ConfiguredAnnotationNonNull = 81;
		final int T_ConfiguredAnnotationNonNullByDefault = 82;
	}

	/** Additional constants for {@link org.eclipse.jdt.core.compiler.IProblem}. */
	public static interface IProblem {
		/** @since 3.7 */
		int RequiredNonNullButProvidedNull = TypeRelated + 910;
		/** @since 3.7 */
		int RequiredNonNullButProvidedPotentialNull = TypeRelated + 911;
		/** @since 3.7 */
		int RequiredNonNullButProvidedUnknown = TypeRelated + 912;
		/** @since 3.7 */
		int MissingNullAnnotationType = ImportRelated + 913;
		/** @since 3.7 */
		int IllegalReturnNullityRedefinition = MethodRelated + 914;
		/** @since 3.7 */
		int IllegalRedefinitionToNonNullParameter = MethodRelated + 915;
		/** @since 3.7 */
		int IllegalDefinitionToNonNullParameter = MethodRelated + 916;
		/** @since 3.7 */
		int ParameterLackingNonNullAnnotation = MethodRelated + 917;
		/** @since 3.7 */
		int ParameterLackingNullableAnnotation = MethodRelated + 918;
		/** @since 3.7 */
		int PotentialNullMessageSendReference = Internal + 919;
		/** @since 3.7 */
		int RedundantNullCheckOnNonNullMessageSend = Internal + 920;
		/** @since 3.7 */
		int CannotImplementIncompatibleNullness = Internal + 921;
		/** @since 3.7 */
		int NullAnnotationNameMustBeQualified = Internal + 922;
		/** @since 3.7 */
		int NullAnnotationIsRedundant = MethodRelated + 923;
	}
	
	/** Translate from a nullness annotation to the corresponding tag bit or 0L. */
	public static long getNullnessTagbit(TypeBinding nullnessAnnotation) {
		switch (nullnessAnnotation.id) {
		case TypeIds.T_ConfiguredAnnotationNonNull : 
			return TagBits.AnnotationNonNull;
		case TypeIds.T_ConfiguredAnnotationNullable : 
			return TagBits.AnnotationNullable;
		default: 
			return 0L;
		}
	}

	/**
	 * This faked annotation type binding marks types with unspecified nullness.
	 * For use in {@link PackageBinding#nullnessDefaultAnnotation} and {@link SourceTypeBinding#nullnessDefaultAnnotation} 
	 */
	final static ReferenceBinding NULL_UNSPECIFIED = new ReferenceBinding() {
		public boolean hasTypeBit(int bit) {
			return false;
		} 
		/* faked type binding */ 
	};
}
