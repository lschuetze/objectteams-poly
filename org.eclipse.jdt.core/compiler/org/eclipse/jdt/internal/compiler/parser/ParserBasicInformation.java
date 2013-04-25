/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.parser;

/*An interface that contains static declarations for some basic information
 about the parser such as the number of rules in the grammar, the starting state, etc...*/
public interface ParserBasicInformation {
    public final static int

      ERROR_SYMBOL      = 129,
      MAX_NAME_LENGTH   = 41,
      NUM_STATES        = 1267,

      NT_OFFSET         = 129,
      SCOPE_UBOUND      = 196,
      SCOPE_SIZE        = 197,
      LA_STATE_OFFSET   = 15117,
      MAX_LA            = 1,
      NUM_RULES         = 887,
      NUM_TERMINALS     = 129,
      NUM_NON_TERMINALS = 394,
      NUM_SYMBOLS       = 523,
      START_STATE       = 1022,
      EOFT_SYMBOL       = 73,
      EOLT_SYMBOL       = 73,
      ACCEPT_ACTION     = 15116,
      ERROR_ACTION      = 15117;
}
