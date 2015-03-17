/*******************************************************************************
 * Copyright (c) 2015 Google Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     John Glassmyer <jogl@google.com> - import group sorting is broken - https://bugs.eclipse.org/430303
 *******************************************************************************/
package org.eclipse.jdt.internal.core.dom.rewrite.imports;

import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.ImportDeclaration;

/**
 * Encapsulates an import's fully qualified name, whether it is on-demand, and whether it is static.
 * <p>
 * The fully qualified name is divided into two parts:
 * <ul>
 * <li>a container name, which is everything preceding the last dot ('.').
 * <li>a simple name, which is the part following the last dot ("*" for an on-demand import).
 * </ul>
 */
public final class ImportName {
	static ImportName createFor(ImportDeclaration importDeclaration) {
		String declName = importDeclaration.getName().getFullyQualifiedName();
		if (importDeclaration.isOnDemand()) {
			return createOnDemand(importDeclaration.isStatic(), declName);
		}
//{ObjectTeams: base
/* orig:
		return createFor(importDeclaration.isStatic(), declName);
  :giro */
		return createFor(importDeclaration.isStatic(), importDeclaration.isBase(), declName);
// SH}
	}

	static ImportName createOnDemand(boolean isStatic, String containerName) {
//{ObjectTeams: no on-demand base imports
/* orig:
		return new ImportName(isStatic, containerName, "*"); //$NON-NLS-1$
  :giro */
		return new ImportName(isStatic, false, containerName, "*"); //$NON-NLS-1$
// SH}
	}

//{ObjectTeams: base
/* orig:
	public static ImportName createFor(boolean isStatic, String qualifiedName) {
		String containerName = Signature.getQualifier(qualifiedName);
		String simpleName = Signature.getSimpleName(qualifiedName);
		return new ImportName(isStatic, containerName, simpleName);
  :giro */
	public static ImportName createFor(boolean isStatic, /*OT:*/boolean isBase, String qualifiedName) {
		String containerName = Signature.getQualifier(qualifiedName);
		String simpleName = Signature.getSimpleName(qualifiedName);
		return new ImportName(isStatic, /*OT:*/isBase, containerName, simpleName);
// SH}
	}

	public final boolean isStatic;
	public final String containerName;
	public final String simpleName;
	public final String qualifiedName;

//{ObjectTeams: base
	public final boolean isBase;
/* orig:
	private ImportName(boolean isStatic, String containerName, String simpleName) {
  :giro */
	private ImportName(boolean isStatic, boolean isBase, String containerName, String simpleName) {
		this.isBase = isBase;
// SH}
		this.isStatic = isStatic;
		this.containerName = containerName;
		this.simpleName = simpleName;

		this.qualifiedName = containerName.isEmpty() ? simpleName : containerName + "." + simpleName; //$NON-NLS-1$;
	}

	@Override
	public String toString() {
		String template = this.isStatic ? "staticImport(%s)" : "typeImport(%s)"; //$NON-NLS-1$ //$NON-NLS-2$
//{ObjectTeams: base
		if (this.isBase) template = "baseImport(%s)"; //$NON-NLS-1$
// SH}
		return String.format(template, this.qualifiedName);
	}

	@Override
	public int hashCode() {
		int result = this.qualifiedName.hashCode();
//{ObjectTeams: base
/* orig:
		result = 31 * result + (this.isStatic ? 1 : 0);
  :giro */
		result = 31 * result + (this.isStatic ? 1 : this.isBase ? 3 : 0);
// SH}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ImportName)) {
			return false;
		}

		ImportName other = (ImportName) obj;
//{ObjectTeams: base
		if (this.isBase != other.isBase) return false;
// SH}

		return this.qualifiedName.equals(other.qualifiedName) && this.isStatic == other.isStatic;
	}

	public boolean isOnDemand() {
		return this.simpleName.equals("*"); //$NON-NLS-1$
	}

	/**
	 * Returns an on-demand ImportName with the same isStatic and containerName as this ImportName.
	 */
	ImportName getContainerOnDemand() {
		if (this.isOnDemand()) {
			return this;
		}

		return ImportName.createOnDemand(this.isStatic, this.containerName);
	}
}