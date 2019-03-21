/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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

      ERROR_SYMBOL      = 147,
      MAX_NAME_LENGTH   = 41,
      NUM_STATES        = 1438,

      NT_OFFSET         = 147,
      SCOPE_UBOUND      = 386,
      SCOPE_SIZE        = 387,
      LA_STATE_OFFSET   = 19669,
      MAX_LA            = 1,
      NUM_RULES         = 1049,
      NUM_TERMINALS     = 147,
      NUM_NON_TERMINALS = 475,
      NUM_SYMBOLS       = 622,
      START_STATE       = 1305,
      EOFT_SYMBOL       = 66,
      EOLT_SYMBOL       = 66,
      ACCEPT_ACTION     = 19668,
      ERROR_ACTION      = 19669;
}
