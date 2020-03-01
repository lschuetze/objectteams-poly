/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2014 Stephan Herrmann.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 * 		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.ui.preferences.OptionsConfigurationBlock.Key;
import org.eclipse.objectteams.otdt.core.ext.WeavingScheme;
import org.eclipse.objectteams.otdt.internal.ui.Messages;
import org.eclipse.osgi.util.NLS;

import base org.eclipse.jdt.internal.ui.preferences.ComplianceConfigurationBlock;

/**
 * Additions to JDT/UI's validations for project settings
 * @since 2.3
 */
@SuppressWarnings("restriction")
public team class Validations {

	protected class Compliance playedBy ComplianceConfigurationBlock {

		@SuppressWarnings("decapsulation")
		String getValue(Key key) -> String getValue(Key key);

		@SuppressWarnings("decapsulation")
		Key getPREF_CODEGEN_TARGET_PLATFORM() -> get Key PREF_CODEGEN_TARGET_PLATFORM;
		
		@SuppressWarnings("decapsulation")
		IProject getFProject() -> get IProject fProject;
		
		@SuppressWarnings("decapsulation")
		IStatus validateCompliance() <- replace IStatus validateCompliance();

		/**
		 * Bug 433423 - [compiler] warn when compiling for OTRE and binding to 1.8 classes
		 */
		callin IStatus validateCompliance() {
			IStatus jdtStatus = base.validateCompliance();
			IProject prj = getFProject();
			if (prj != null) {
				try {
					if (prj.hasNature(JavaCore.OTJ_NATURE_ID)) {
						IJavaProject jProject = JavaCore.create(prj);
						if (jProject.exists()) {
							String target = getValue(getPREF_CODEGEN_TARGET_PLATFORM());
							long jdkTarget = CompilerOptions.versionToJdkLevel(target);
							if (jdkTarget >= ClassFileConstants.JDK1_8) {
								String scheme = jProject.getOption(JavaCore.COMPILER_OPT_WEAVING_SCHEME, true);
								if (WeavingScheme.OTRE.name().equals(scheme)) {
									IStatus newStatus = new Status(IStatus.WARNING, JavaCore.PLUGIN_ID, 
											NLS.bind(Messages.Validation_Target18IncompatibleWithOTRE_warning, scheme, target));
									if (jdtStatus.isOK())
										return newStatus;
									StringBuilder sb = new StringBuilder(Messages.Validation_MultipleComplianceProblems_error);
									sb.append(' ').append(jdtStatus.getMessage());
									sb.append("; ").append(newStatus.getMessage()); //$NON-NLS-1$
									return new MultiStatus( JavaCore.PLUGIN_ID, 0,
													new IStatus[] { jdtStatus, newStatus },
													sb.toString(),
													null);
								}
							}
						}
					}
				} catch (CoreException e) {
					// cannot analyse further
				}
			}
			return jdtStatus;
		}		
	}	
}
