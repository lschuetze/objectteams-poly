/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.parser;

/*An interface that contains static declarations for some basic information
 about the parser such as the number of rules in the grammar, the starting state, etc...*/
public interface ParserBasicInformation {
    public final static int

      ERROR_SYMBOL      = 146,
      MAX_NAME_LENGTH   = 41,
      NUM_STATES        = 1424,

      NT_OFFSET         = 146,
      SCOPE_UBOUND      = 379,
      SCOPE_SIZE        = 380,
      LA_STATE_OFFSET   = 19607,
      MAX_LA            = 1,
      NUM_RULES         = 1033,
      NUM_TERMINALS     = 146,
      NUM_NON_TERMINALS = 466,
      NUM_SYMBOLS       = 612,
      START_STATE       = 1277,
      EOFT_SYMBOL       = 64,
      EOLT_SYMBOL       = 64,
      ACCEPT_ACTION     = 19606,
      ERROR_ACTION      = 19607;
}
