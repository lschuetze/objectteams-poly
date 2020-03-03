/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010  Technical University Berlin, Germany.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.example.observer.library;

import java.util.LinkedList;

/**
 * A title listed in the catalogue.
 */
public class Book {

	private String name;
	private String author;
	private String isbn;
	private LinkedList<BookCopy> copies = new LinkedList<BookCopy>();

	public Book (String name, String author, String isbn) {
		this.name = name;
		this.author = name;
		this.isbn = isbn;
	}

	public String getName () { return name ; }
	public String getAuthor () { return  author; }
	public String getISBN () { return  isbn; }

	/**
	 * When a new copy of this book is acquired, use this method to register.
	 * @param bc
	 * @return number of registered copies after the operation. 
	 */
	public int registerCopy (BookCopy bc) {
		copies.add(bc);
		return copies.size();
	}
}
