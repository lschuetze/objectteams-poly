/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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
      NUM_STATES        = 1371,

      NT_OFFSET         = 138,
      SCOPE_UBOUND      = 379,
      SCOPE_SIZE        = 380,
      LA_STATE_OFFSET   = 19154,
      MAX_LA            = 1,
      NUM_RULES         = 981,
      NUM_TERMINALS     = 138,
      NUM_NON_TERMINALS = 438,
      NUM_SYMBOLS       = 576,
      START_STATE       = 1852,
      EOFT_SYMBOL       = 65,
      EOLT_SYMBOL       = 65,
      ACCEPT_ACTION     = 19153,
      ERROR_ACTION      = 19154;
}
