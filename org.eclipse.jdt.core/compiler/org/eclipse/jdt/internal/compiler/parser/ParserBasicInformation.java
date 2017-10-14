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

      ERROR_SYMBOL      = 147,
      MAX_NAME_LENGTH   = 41,
      NUM_STATES        = 1422,

      NT_OFFSET         = 147,
      SCOPE_UBOUND      = 379,
      SCOPE_SIZE        = 380,
      LA_STATE_OFFSET   = 19539,
      MAX_LA            = 1,
      NUM_RULES         = 1032,
      NUM_TERMINALS     = 147,
      NUM_NON_TERMINALS = 466,
      NUM_SYMBOLS       = 613,
      START_STATE       = 1088,
      EOFT_SYMBOL       = 65,
      EOLT_SYMBOL       = 65,
      ACCEPT_ACTION     = 19538,
      ERROR_ACTION      = 19539;
}
