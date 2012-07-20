/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 *
 *
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Technical University Berlin - extended API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.parser;

/**
 * IMPORTANT NOTE: These constants are dedicated to the internal Scanner implementation.
 * It is mirrored in org.eclipse.jdt.core.compiler public package where it is API.
 * The mirror implementation is using the backward compatible ITerminalSymbols constant
 * definitions (stable with 2.0), whereas the internal implementation uses TerminalTokens
 * which constant values reflect the latest parser generation state.
 */
/**
 * Maps each terminal symbol in the java-grammar into a unique integer.
 * This integer is used to represent the terminal when computing a parsing action.
 *
 * Disclaimer : These constant values are generated automatically using a Java
 * grammar, therefore their actual values are subject to change if new keywords
 * were added to the language (for instance, 'assert' is a keyword in 1.4).
 */
public interface TerminalTokens {

	// special tokens not part of grammar - not autogenerated
	int 
		TokenNameNotAToken = 0,
	    TokenNameWHITESPACE = 1000,
		TokenNameCOMMENT_LINE = 1001,
		TokenNameCOMMENT_BLOCK = 1002,
		TokenNameCOMMENT_JAVADOC = 1003;

	int
      TokenNameIdentifier = 13,
      TokenNameabstract = 35,
      TokenNameassert = 87,
      TokenNameboolean = 48,
      TokenNamebreak = 88,
      TokenNamebyte = 49,
      TokenNamecase = 119,
      TokenNamecatch = 116,
      TokenNamechar = 50,
      TokenNameclass = 81,
      TokenNamecontinue = 89,
      TokenNameconst = 132,
      TokenNamedefault = 100,
      TokenNamedo = 90,
      TokenNamedouble = 51,
      TokenNameelse = 122,
      TokenNameenum = 101,
      TokenNameextends = 102,
      TokenNamefalse = 62,
      TokenNamefinal = 36,
      TokenNamefinally = 120,
      TokenNamefloat = 52,
      TokenNamefor = 91,
      TokenNamegoto = 133,
      TokenNameif = 92,
      TokenNameimplements = 130,
      TokenNameimport = 117,
      TokenNameinstanceof = 19,
      TokenNameint = 53,
      TokenNameinterface = 93,
      TokenNamelong = 54,
      TokenNamenative = 37,
      TokenNamenew = 60,
      TokenNamenull = 63,
      TokenNamepackage = 118,
      TokenNameprivate = 38,
      TokenNameprotected = 39,
      TokenNamepublic = 40,
      TokenNamereturn = 94,
      TokenNameshort = 55,
      TokenNamestatic = 32,
      TokenNamestrictfp = 41,
      TokenNamesuper = 59,
      TokenNameswitch = 95,
      TokenNamesynchronized = 42,
      TokenNamethis = 58,
      TokenNamethrow = 96,
      TokenNamethrows = 123,
      TokenNametransient = 43,
      TokenNametrue = 64,
      TokenNametry = 97,
      TokenNamevoid = 56,
      TokenNamevolatile = 44,
      TokenNamewhile = 86,
      TokenNameas = 125,
      TokenNamebase = 33,
      TokenNamecallin = 45,
      TokenNameplayedBy = 131,
      TokenNameprecedence = 121,
      TokenNamereadonly = 46,
      TokenNameteam = 34,
      TokenNametsuper = 61,
      TokenNamewhen = 103,
      TokenNamewith = 115,
      TokenNamewithin = 98,
      TokenNamereplace = 126,
      TokenNameafter = 124,
      TokenNamebefore = 127,
      TokenNameget = 128,
      TokenNameset = 129,
      TokenNameIntegerLiteral = 65,
      TokenNameLongLiteral = 66,
      TokenNameFloatingPointLiteral = 67,
      TokenNameDoubleLiteral = 68,
      TokenNameCharacterLiteral = 69,
      TokenNameStringLiteral = 70,
      TokenNamePLUS_PLUS = 2,
      TokenNameMINUS_MINUS = 3,
      TokenNameEQUAL_EQUAL = 21,
      TokenNameLESS_EQUAL = 15,
      TokenNameGREATER_EQUAL = 16,
      TokenNameNOT_EQUAL = 22,
      TokenNameLEFT_SHIFT = 20,
      TokenNameRIGHT_SHIFT = 14,
      TokenNameUNSIGNED_RIGHT_SHIFT = 18,
      TokenNamePLUS_EQUAL = 104,
      TokenNameMINUS_EQUAL = 105,
      TokenNameMULTIPLY_EQUAL = 106,
      TokenNameDIVIDE_EQUAL = 107,
      TokenNameAND_EQUAL = 108,
      TokenNameOR_EQUAL = 109,
      TokenNameXOR_EQUAL = 110,
      TokenNameREMAINDER_EQUAL = 111,
      TokenNameLEFT_SHIFT_EQUAL = 112,
      TokenNameRIGHT_SHIFT_EQUAL = 113,
      TokenNameUNSIGNED_RIGHT_SHIFT_EQUAL = 114,
      TokenNameOR_OR = 31,
      TokenNameAND_AND = 30,
      TokenNamePLUS = 4,
      TokenNameMINUS = 5,
      TokenNameNOT = 73,
      TokenNameREMAINDER = 10,
      TokenNameXOR = 26,
      TokenNameAND = 23,
      TokenNameMULTIPLY = 9,
      TokenNameOR = 28,
      TokenNameTWIDDLE = 74,
      TokenNameDIVIDE = 11,
      TokenNameGREATER = 12,
      TokenNameLESS = 8,
      TokenNameLPAREN = 24,
      TokenNameRPAREN = 25,
      TokenNameLBRACE = 76,
      TokenNameRBRACE = 57,
      TokenNameLBRACKET = 6,
      TokenNameRBRACKET = 77,
      TokenNameSEMICOLON = 27,
      TokenNameQUESTION = 29,
      TokenNameCOLON = 75,
      TokenNameCOMMA = 47,
      TokenNameDOT = 1,
      TokenNameEQUAL = 82,
      TokenNameAT = 17,
      TokenNameELLIPSIS = 85,
      TokenNameARROW = 83,
      TokenNameCOLON_COLON = 7,
      TokenNameBeginLambda = 71,
      TokenNameBeginTypeArguments = 80,
      TokenNameElidedSemicolonAndRightBrace = 78,
      TokenNameBINDIN = 84,
      TokenNameCALLOUT_OVERRIDE = 99,
      TokenNameSYNTHBINDOUT = 79,
      TokenNameEOF = 72,
      TokenNameERROR = 134;


	// This alias is statically inserted by generateOTParser.sh:
	int TokenNameBINDOUT = TokenNameARROW;
}

