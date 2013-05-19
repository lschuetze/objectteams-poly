/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2013 (c) GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.objectteams.org for updates and contact.
 * 
 * Contributors:
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.compare;

import java.util.Iterator;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.CallinMappingDeclaration;
import org.eclipse.jdt.core.dom.CalloutMappingDeclaration;
import org.eclipse.jdt.core.dom.MethodSpec;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.objectteams.otdt.core.IOTJavaElement;
import org.eclipse.objectteams.otdt.ui.ImageManager;
import org.eclipse.swt.graphics.Image;

import base org.eclipse.jdt.internal.ui.compare.JavaCompareUtilities;
import base org.eclipse.jdt.internal.ui.compare.JavaNode;
import base org.eclipse.jdt.internal.ui.compare.JavaParseTreeBuilder;

/**
 * Make OT elements known to the java structure compare view.
 * We used slightly different strategies for types (role/team) and method bindings:
 * 
 * For types we let JavaNodes be created the regular way with the normal typeCode (CLASS/INTERFACE),
 * but before that we record in {@link #otTypeCode} if we've seen a team and/or role.
 * This information will be picked up after the constructor of JavaNode and stored in
 * field {@link StructureCompare.Node#otType}.
 * This strategy doesn't, however, work for method mappings, because a role method and
 * a method binding of the same name/signature would be merged into one structural node
 * (comparison just by type code and ID).
 * 
 * For method mappings we create a JavaNode with a OT-specific type code right away.
 * Now we only have to tell a few utility methods how to handles these type codes.
 * See role {@link StructureCompare.Util}.
 */
@SuppressWarnings("restriction")
public team class StructureCompare {
	
	// additions to constants in org.eclipse.jdt.internal.ui.compare.JavaNode
	public static final int TEAM = 20;
	public static final int ROLE = 21;
	public static final int TEAM_ROLE = 22;
	public static final int CALLOUT = 23;
	public static final int CALLIN = 24;

	// additions to constants in org.eclipse.jdt.internal.ui.compare.JavaCompareUtilities.METHOD
	public static final char C_CALLOUT = '>';
	public static final char C_CALLIN = '!';

	// drop-box: when creating a JavaNode, while this field holds a non-null value,
	// -> decorate the JavaNode with a Node that stores the otType.
	ThreadLocal<Integer> otTypeCode = new ThreadLocal<Integer>();
	
	/**
	 * A structure node that maintains to type codes:
	 * <ul><li>one from original JDT/UI (in the base object)</li>
	 * <li>plus an OT-specific type code (for roles, teams)</li></ul>
	 */
	@SuppressWarnings("decapsulation") // invisible base class
	protected class Node playedBy JavaNode {
		
		protected int getCLASS() -> get int CLASS;

		int getTypeCode() -> int getTypeCode();

		Image getImage() <- replace Image getImage();
		
		int otType;

		void init() <- after JavaNode(JavaNode parent, int type, String name, int start, int length)
			base when (otTypeCode.get() != null);
		
		void init() {
			this.otType = otTypeCode.get();
			otTypeCode.set(null);
		}

		@SuppressWarnings("basecall")
		callin Image getImage() {
			String imgName;
			switch (this.otType) {
			case TEAM:
				imgName = ImageManager.TEAM_IMG; break;
			case TEAM_ROLE:
				imgName = ImageManager.TEAM_ROLE_IMG; break;
			case ROLE:
				imgName = ImageManager.ROLECLASS_IMG; break;
			default:
				switch (getTypeCode()) {
				case CALLOUT:
					imgName = ImageManager.CALLOUTBINDING_IMG; break;
				case CALLIN:
					imgName = ImageManager.CALLINBINDING_REPLACE_IMG; break;
				default:
					return base.getImage();
				}
			}
			return org.eclipse.objectteams.otdt.ui.ImageManager.getSharedInstance().get(imgName);
		}		
	}

	/**
	 * Detect OT-elements during tree traversal and
	 * - create {@link JavaNode} instances with tweaked type code (method mappings), or,
	 * - trigger creationg of {@link Node} role instances with additional type code for team/role.
	 */
	@SuppressWarnings("decapsulation") // invisible base class
	protected class TreeBuilder playedBy JavaParseTreeBuilder {

		void push(int type, String name, int declarationStart, int length) 
		-> void push(int type, String name, int declarationStart, int length);

		void pop() -> void pop();

		String getType(Type type) -> String getType(Type type);

		void markOTType(TypeDeclaration node)					<- before boolean visit(TypeDeclaration node);
		
		boolean visitCallout(CalloutMappingDeclaration node) 	<- replace boolean visit(CalloutMappingDeclaration node);
		boolean visitCallin(CallinMappingDeclaration node) 		<- replace boolean visit(CallinMappingDeclaration node);

		void pop() <- after void endVisit(CalloutMappingDeclaration node);
		void pop() <- after void endVisit(CallinMappingDeclaration node);
		
		void markOTType(TypeDeclaration node) {
			if (node.isTeam()) {
				if (node.isRole()) otTypeCode.set(TEAM_ROLE);
				else               otTypeCode.set(TEAM);
			} else if (node.isRole()) {
				otTypeCode.set(ROLE);
			}
		}

		@SuppressWarnings("basecall")
		callin boolean visitCallout(CalloutMappingDeclaration node) {
	        String signature= getSignature((MethodSpec) node.getRoleMappingElement());
	        push(CALLOUT, signature, node.getStartPosition(), node.getLength());
	        return false;
	    }

		@SuppressWarnings("basecall")
		callin boolean visitCallin(CallinMappingDeclaration node) {
	        String signature= getSignature((MethodSpec) node.getRoleMappingElement());
	        push(CALLIN, signature, node.getStartPosition(), node.getLength());
	        return false;
	    }

	    private String getSignature(MethodSpec node) {
	        StringBuffer buffer= new StringBuffer();
	        buffer.append(node.getName().toString());
	        buffer.append('(');
	        boolean first= true;
	        @SuppressWarnings("unchecked")
			Iterator<SingleVariableDeclaration> iterator= node.parameters().iterator();
	        while (iterator.hasNext()) {
	        	SingleVariableDeclaration svd= iterator.next();
	            if (!first)
	                buffer.append(", "); //$NON-NLS-1$
	            buffer.append(getType(svd.getType()));
	            if (svd.isVarargs())
	                buffer.append("..."); //$NON-NLS-1$
	            first= false;
	        }
	        buffer.append(')');
	        return buffer.toString();
	    }
	}

	/**
	 * Tell this utility how to handle callin/callout codes/ids.
	 */
	protected class Util playedBy JavaCompareUtilities {

		@SuppressWarnings("decapsulation")
		buildID <- replace buildID;

		@SuppressWarnings("basecall")
		static callin String buildID(int type, String name) {
			switch(type) {
			case CALLOUT:
				StringBuilder sb = new StringBuilder();
				sb.append(C_CALLOUT);
				sb.append(name);
				return sb.toString();
			case CALLIN:
				sb = new StringBuilder();
				sb.append(C_CALLIN);
				sb.append(name);
				return sb.toString();
			default:
				return base.buildID(type, name);
			}
		}

		@SuppressWarnings("decapsulation")
		getJavaElementID <- replace getJavaElementID;

		@SuppressWarnings("basecall")
		static callin String getJavaElementID(IJavaElement je) {
			switch (je.getElementType()) {
			case IOTJavaElement.CALLOUT_MAPPING:
				StringBuilder sb = new StringBuilder();
				sb.append(C_CALLOUT);
				sb.append(JavaElementLabels.getElementLabel(je, JavaElementLabels.M_PARAMETER_TYPES));
				return sb.toString();
			case IOTJavaElement.CALLIN_MAPPING:
				sb = new StringBuilder();
				sb.append(C_CALLIN);
				sb.append(JavaElementLabels.getElementLabel(je, JavaElementLabels.M_PARAMETER_TYPES));
				return sb.toString();
			default:
				return base.getJavaElementID(je);
			}
		}
	}
}
