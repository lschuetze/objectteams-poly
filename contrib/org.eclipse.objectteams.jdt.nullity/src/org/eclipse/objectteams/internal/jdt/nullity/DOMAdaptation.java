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

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.compiler.parser.TerminalTokens;

import base org.eclipse.jdt.core.dom.ASTConverter;

/**
 * Adaptations to the DOM AST
 * 
 * @author stephan
 */
@SuppressWarnings({ "decapsulation", "restriction" })
public team class DOMAdaptation {
	
	protected class Converter playedBy ASTConverter {
		
		void setModifiers(BodyDeclaration bodyDeclaration, org.eclipse.jdt.internal.compiler.ast.Annotation[] annotations, int modifiersEnd)
		<- replace void setModifiers(BodyDeclaration bodyDeclaration, org.eclipse.jdt.internal.compiler.ast.Annotation[] annotations, int modifiersEnd);
		
		@SuppressWarnings({ "inferredcallout", "basecall" })
		callin void setModifiers(BodyDeclaration bodyDeclaration, org.eclipse.jdt.internal.compiler.ast.Annotation[] annotations, int modifiersEnd) {
			this.scanner.tokenizeWhiteSpace = false;
			try {
				int token;
				int indexInAnnotations = 0;
				while ((token = this.scanner.getNextToken()) != TerminalTokens.TokenNameEOF) {
					IExtendedModifier modifier = null;
		switchToken:
					switch(token) {
						case TerminalTokens.TokenNameabstract:
							modifier = createModifier(Modifier.ModifierKeyword.ABSTRACT_KEYWORD);
							break;
						case TerminalTokens.TokenNamepublic:
							modifier = createModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD);
							break;
						case TerminalTokens.TokenNamestatic:
							modifier = createModifier(Modifier.ModifierKeyword.STATIC_KEYWORD);
							break;
						case TerminalTokens.TokenNameprotected:
							modifier = createModifier(Modifier.ModifierKeyword.PROTECTED_KEYWORD);
							break;
						case TerminalTokens.TokenNameprivate:
							modifier = createModifier(Modifier.ModifierKeyword.PRIVATE_KEYWORD);
							break;
						case TerminalTokens.TokenNamefinal:
							modifier = createModifier(Modifier.ModifierKeyword.FINAL_KEYWORD);
							break;
						case TerminalTokens.TokenNamenative:
							modifier = createModifier(Modifier.ModifierKeyword.NATIVE_KEYWORD);
							break;
						case TerminalTokens.TokenNamesynchronized:
							modifier = createModifier(Modifier.ModifierKeyword.SYNCHRONIZED_KEYWORD);
							break;
						case TerminalTokens.TokenNametransient:
							modifier = createModifier(Modifier.ModifierKeyword.TRANSIENT_KEYWORD);
							break;
						case TerminalTokens.TokenNamevolatile:
							modifier = createModifier(Modifier.ModifierKeyword.VOLATILE_KEYWORD);
							break;
						case TerminalTokens.TokenNamestrictfp:
							modifier = createModifier(Modifier.ModifierKeyword.STRICTFP_KEYWORD);
							break;
						case TerminalTokens.TokenNameAT :
							// we have an annotation
							if (annotations != null && indexInAnnotations < annotations.length) {
//{Modification: skip synthetic annotations
/* orig:
							org.eclipse.jdt.internal.compiler.ast.Annotation annotation = annotations[indexInAnnotations++]; 
  :giro */
								org.eclipse.jdt.internal.compiler.ast.Annotation annotation;
								do {
									if (indexInAnnotations == annotations.length)
										break switchToken;
									annotation = annotations[indexInAnnotations++];
								} while ((annotation.bits & Constants.IsSynthetic) != 0);
// SH}
								modifier = convert(annotation);
								this.scanner.resetTo(annotation.declarationSourceEnd + 1, modifiersEnd);
							}
							break;
						case TerminalTokens.TokenNameCOMMENT_BLOCK :
						case TerminalTokens.TokenNameCOMMENT_LINE :
						case TerminalTokens.TokenNameCOMMENT_JAVADOC :
							break;
						default :
							// there is some syntax errors in source code
							break;
					}
					if (modifier != null) {
						bodyDeclaration.modifiers().add(modifier);
					}
				}
			} catch(InvalidInputException e) {
				// ignore
			}
		}
	}
}
