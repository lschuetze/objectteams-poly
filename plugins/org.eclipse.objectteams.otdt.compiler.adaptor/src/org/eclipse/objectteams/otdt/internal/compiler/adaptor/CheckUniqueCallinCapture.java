/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2011 GK Software AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 	  Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.compiler.adaptor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.builder.SourceFile;
import org.eclipse.jdt.internal.core.util.Messages;
import org.eclipse.jdt.internal.core.util.Util;
import org.eclipse.objectteams.otdt.compiler.adaptor.CompilerAdaptorPlugin;
import org.eclipse.objectteams.otdt.core.ext.IMarkableJavaElement;
import org.eclipse.objectteams.otdt.core.ext.MarkableFactory;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.FieldAccessSpec;

import base org.eclipse.jdt.internal.core.builder.BatchImageBuilder;
import base org.eclipse.jdt.internal.core.builder.JavaBuilder;
import base org.eclipse.objectteams.otdt.internal.core.compiler.ast.MethodSpec;

/**
 * This class observes the compiler during each full build to find any base method
 * that is callin-bound by more than one callin binding.
 * 
 * @author stephan
 * @since 2.1.0
 */
@SuppressWarnings("restriction")
public team class CheckUniqueCallinCapture {

	private static final Object MARKER_SRC_OTDT = "OTDT";

	/**
	 * Observe the BatchImageBuilder to detect full builds, during which only we are active.
	 * Note, that the activity has to be transfered from the initial thread to the thread
	 * "Compiler Processing Task", which is handled by the super-class CompilationThreadWatcher.
	 */
	protected team class Batch extends CompilationThreadWatcher playedBy BatchImageBuilder {


		@SuppressWarnings("decapsulation")
		JavaBuilder getJavaBuilder() -> get JavaBuilder javaBuilder;

		Map<MethodBinding,ReferenceBinding> baseMethods; // maps base methods to a team declaring a callin
		Set<MethodBinding> duplicates;

		void compile(SourceFile[] units) <- replace void compile(SourceFile[] units);

		callin void compile(SourceFile[] units) {
			try {
				// clean init:
				this.baseMethods = new HashMap<MethodBinding,ReferenceBinding>();
				this.duplicates = new HashSet<MethodBinding>();
				// activate for this compilation batch:
				within(this)
					base.compile(units);
				// report results:
				for(MethodBinding dup : this.duplicates)
					reportDuplicateCallinCapture(dup);
			} finally {
				this.baseMethods = null;
				this.duplicates = null;
			}
		}

		/** 
		 * This nested role does the detail work:
		 * Collect all callin-bound base methods, detecting duplicates.
		 */
		protected class BaseMethodResolver playedBy MethodSpec {

			void resolvedBaseMethod(MethodBinding resolvedMethod, Scope scope)
			<- after
			MethodBinding resolveFeature(ReferenceBinding receiverType,
										 BlockScope scope, boolean callinExpected,
										 boolean isBaseSide, boolean allowEnclosing)
				base when (isBaseSide && base.isDeclaration && !(base instanceof FieldAccessSpec))
				with { resolvedMethod <- result, scope <- scope}

			void resolvedBaseMethod(MethodBinding resolvedMethod, Scope scope) {
				ReferenceBinding previousTeam = baseMethods.get(resolvedMethod);
				if (previousTeam != null) {
					if (previousTeam != getTeam(scope))
						duplicates.add(resolvedMethod);
				} else {
					baseMethods.put(resolvedMethod, getTeam(scope));
				}
			}
			ReferenceBinding getTeam(Scope scope) {
				ReferenceBinding role = scope.enclosingSourceType();
				return (role != null) ? role.enclosingType() : null;
			}
		}
		/** Report one affected base method. */
		void reportDuplicateCallinCapture(MethodBinding methodBinding) {
			try {
				if (methodBinding.declaringClass == null)
					return;
				String qualifiedClassName = String.valueOf(methodBinding.declaringClass.readableName());
				IType type = this.getJavaBuilder().getJavaProject().findType(qualifiedClassName);
				if (type == null)
					return;
				IMarkableJavaElement markable = MarkableFactory.createMarkable(type);
				IMarker marker = markable.createMarker(IMarkableJavaElement.GLOBAL_PROBLEM_ID);
				String message = Messages.bind(OTMessages.CheckUniqueCallinCapture_warning_multipleCallinsToBaseMethod, 
											   String.valueOf(methodBinding.shortReadableName()));
				int severity = IMarker.SEVERITY_WARNING;
				int start = 0, end = 0;
				IMethod method = findMethod(type, methodBinding);
				if (method != null) {
					marker.setAttribute(IMarkableJavaElement.ATT_DETAIL_ID, method.getHandleIdentifier());
					ISourceRange nameRange = method.getNameRange();
			    	if (nameRange.getOffset() >= 0 && nameRange.getLength() >= 0) {
						start = nameRange.getOffset();
						end = start+nameRange.getLength();
			    	}
				}
				marker.setAttributes(
						new String[] {IMarker.MESSAGE, IMarker.SEVERITY, 	  IMarker.CHAR_START, IMarker.CHAR_END, IMarker.SOURCE_ID},
						new Object[] {message, 		   new Integer(severity), new Integer(start), new Integer(end), MARKER_SRC_OTDT});
			} catch (CoreException e) {
				CompilerAdaptorPlugin.logException(OTMessages.CheckUniqueCallinCapture_error_cannotCreateMarker+methodBinding, e);
			}
		}

		IMethod findMethod(IType type, MethodBinding method) throws JavaModelException {
			TypeBinding[] params = method.parameters;
			String names[] = new String[params.length];
			for (int i = 0; i < params.length; i++)
				names[i] = Signature.createTypeSignature(params[i].readableName(), true);
			return Util.findMethod(type, method.selector, names, false);
		}
	}
	/** Gateway only */
	protected class JavaBuilder playedBy JavaBuilder {
		@SuppressWarnings("decapsulation")
		protected JavaProject getJavaProject() -> get JavaProject javaProject;
	}
}
