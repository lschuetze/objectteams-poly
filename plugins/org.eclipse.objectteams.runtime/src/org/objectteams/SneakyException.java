/**********************************************************************
 * This file is part of the "Object Teams Runtime Environment"
 *
 * Copyright 2012 GK Software AG
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.objectteams;

import java.io.PrintStream;

/**
 * Exception class used for tunneling checked exception through generic dispatch
 * code without matching throws declaration.
 * 
 * An original exception raised in a role method is wrapped during any of 
 * <code>_OT$callReplace</code>, <code>_OT$callBefore</code> and <code>_OT$callAfter</code>
 * and unwrapped in the initial wrapper of any base method.
 * 
 * Note that the opposite direction needs no wrapping: exceptions raised in
 * the base method are thrown as normal, because this happens inside _OT$callOrig
 * of the base class, which is created dynamically by OTREDyn and never checked
 * by any compiler.
 */
@SuppressWarnings("serial")
public class SneakyException extends RuntimeException {
	private Exception cause;

	/** Wrap a given exception in an unchecked SneakyException. */
	public SneakyException(Exception cause) {
		super(cause);
		this.cause = cause;
	}

	/** Re-throw the nested exception but hide it from the compiler. */
	public void rethrow() {
		SneakyException.<RuntimeException>sneakyThrow0(this.cause);
	}

	@SuppressWarnings("unchecked")
    private static <T extends Exception> void sneakyThrow0(Exception t) throws T {
	    throw (T)t;
    }
	
	@Override
	public String getMessage() {
		return this.cause.getMessage();
	}
	
	@Override
	public void printStackTrace(PrintStream s) {
		this.cause.printStackTrace(s);
	}
	
	@Override
	public StackTraceElement[] getStackTrace() {
		return this.cause.getStackTrace();
	}
}
