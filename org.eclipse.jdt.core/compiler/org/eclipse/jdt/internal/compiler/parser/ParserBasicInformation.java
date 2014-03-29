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

      ERROR_SYMBOL      = 138,
      MAX_NAME_LENGTH   = 41,
      NUM_STATES        = 1361,

      NT_OFFSET         = 138,
      SCOPE_UBOUND      = 371,
      SCOPE_SIZE        = 372,
      LA_STATE_OFFSET   = 19170,
      MAX_LA            = 1,
      NUM_RULES         = 977,
      NUM_TERMINALS     = 138,
      NUM_NON_TERMINALS = 437,
      NUM_SYMBOLS       = 575,
      START_STATE       = 1164,
      EOFT_SYMBOL       = 65,
      EOLT_SYMBOL       = 65,
      ACCEPT_ACTION     = 19169,
      ERROR_ACTION      = 19170;
}
