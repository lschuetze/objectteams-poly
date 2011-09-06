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

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.batch.Main;

import base org.eclipse.jdt.core.dom.ASTConverter;

/**
 * Adaptations to the DOM AST
 * 
 * @author stephan
 */
@SuppressWarnings({ "decapsulation", "restriction" })
public team class DOMAdaptation {

	// copies from OT-version of org.eclipse.jdt.core.dom.Modifier:
	public static final int OT_CALLIN = 0x80000000;
	public static final int OT_TEAM = 0x8000;

	int [] translation = null;

	@SuppressWarnings("nls")
	public void initVersion() {
		if (this.translation == null) {
			this.translation = new int[1024];
			ResourceBundle bundle = Main.ResourceBundleFactory.getBundle(Locale.getDefault());
			try {
				String otVersion = bundle.getString("otdtc.version");
				if (otVersion.contains("2.0.0"))
					initTranslation(TerminalTokens_OT200.class);
				else if (otVersion.contains("2.0.1"))
					initTranslation(TerminalTokens_OT201.class);
				else if (otVersion.contains("2.1.0 M1"))
					initTranslation(TerminalTokens_OT21M1.class);
				else if (otVersion.contains("2.1.0 M2"))
					initTranslation(TerminalTokens_OT21M1.class);
			} catch (MissingResourceException mre) {
				String version = bundle.getString("compiler.version");
				if (version.contains("3.7.0"))
					initTranslation(TerminalTokens_R370.class);
				else if (version.contains("3.7.1"))
					initTranslation(TerminalTokens_B74R37x.class);
				else if (version.contains("3.8.0 M1"))
					initTranslation(TerminalTokens_R38M1.class);
				else if (version.contains("3.8.0 M2"))
					initTranslation(TerminalTokens_R38M1.class);
			}
		}
	}
	void initTranslation(Class<?> terminalTokensClass) {
		for (Field field :terminalTokensClass.getDeclaredFields()) {
			try {
				int thisValue = field.getInt(null);
				int ot21m1Value = TerminalTokens_OT21M1.class.getField(field.getName()).getInt(null);
				this.translation[thisValue] = ot21m1Value;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected class Converter playedBy ASTConverter {
		
		
		void setModifiers(BodyDeclaration bodyDeclaration, org.eclipse.jdt.internal.compiler.ast.Annotation[] annotations, int modifiersEnd)
		<- replace void setModifiers(BodyDeclaration bodyDeclaration, org.eclipse.jdt.internal.compiler.ast.Annotation[] annotations, int modifiersEnd);
		
		@SuppressWarnings({ "basecall", "inferredcallout" })
		callin void setModifiers(BodyDeclaration bodyDeclaration, org.eclipse.jdt.internal.compiler.ast.Annotation[] annotations, int modifiersEnd) {
			this.scanner.tokenizeWhiteSpace = false;
			try {
				int token;
				int indexInAnnotations = 0;
//{Modification:
				initVersion();
				while ((token = translation[this.scanner.getNextToken()]) != TerminalTokens_OT21M1.TokenNameEOF) {
/* orig:
				while ((token = this.scanner.getNextToken()) != TerminalTokens.TokenNameEOF) {
  */
					IExtendedModifier modifier = null;
//  :giro 
		switchToken:
// SH}
					switch(token) {
						case TerminalTokens_OT21M1.TokenNameabstract:
							modifier = createModifier(Modifier.ModifierKeyword.ABSTRACT_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNamepublic:
							modifier = createModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNamestatic:
							modifier = createModifier(Modifier.ModifierKeyword.STATIC_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNameprotected:
							modifier = createModifier(Modifier.ModifierKeyword.PROTECTED_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNameprivate:
							modifier = createModifier(Modifier.ModifierKeyword.PRIVATE_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNamefinal:
							modifier = createModifier(Modifier.ModifierKeyword.FINAL_KEYWORD);
							break;
	//{ObjectTeams: callin team
						case TerminalTokens_OT21M1.TokenNamecallin:
							modifier = createModifier(Modifier.ModifierKeyword.fromFlagValue(OT_CALLIN));
							break;
						case TerminalTokens_OT21M1.TokenNameteam:
							modifier = createModifier(Modifier.ModifierKeyword.fromFlagValue(OT_TEAM));
							break;
	// SH}
						case TerminalTokens_OT21M1.TokenNamenative:
							modifier = createModifier(Modifier.ModifierKeyword.NATIVE_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNamesynchronized:
							modifier = createModifier(Modifier.ModifierKeyword.SYNCHRONIZED_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNametransient:
							modifier = createModifier(Modifier.ModifierKeyword.TRANSIENT_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNamevolatile:
							modifier = createModifier(Modifier.ModifierKeyword.VOLATILE_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNamestrictfp:
							modifier = createModifier(Modifier.ModifierKeyword.STRICTFP_KEYWORD);
							break;
						case TerminalTokens_OT21M1.TokenNameAT :
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
						case TerminalTokens_OT21M1.TokenNameCOMMENT_BLOCK :
						case TerminalTokens_OT21M1.TokenNameCOMMENT_LINE :
						case TerminalTokens_OT21M1.TokenNameCOMMENT_JAVADOC :
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
