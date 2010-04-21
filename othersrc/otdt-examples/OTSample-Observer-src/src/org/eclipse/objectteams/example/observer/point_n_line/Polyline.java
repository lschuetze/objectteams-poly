/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2004, 2010  Technical University Berlin, Germany.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id$
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.example.observer.point_n_line;

import java.util.*;

public class Polyline {
	private LinkedList<Point> points = new LinkedList<Point>();

	public void addPoint (Point p) {
		points.add(p);
	}
	
	public void addPoints (List<Point> pList) {
		for (Point p : pList) 
			addPoint(p);
	}
	
	public String toString () {
		StringBuffer buf = new StringBuffer("Line [");
		String sep = "";
		for (Point p : points) {
			buf.append(sep+p.toString());
			sep = ", ";
		}
		buf.append("]");
		return buf.toString();
	}
	
}
