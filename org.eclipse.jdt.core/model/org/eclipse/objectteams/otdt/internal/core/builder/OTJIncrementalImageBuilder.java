/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2008 Technical University Berlin, Germany.
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
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.core.builder;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.builder.IncrementalImageBuilder;
import org.eclipse.jdt.internal.core.builder.JavaBuilder;
import org.eclipse.jdt.internal.core.builder.SourceFile;

/**
 * Specialize the IncrementalImageBuilder to include teams of role files in 
 * the incremental compilation process.
 * 
 * @author stephan
 * @version $Id: OTJIncrementalImageBuilder.java 23401 2010-02-02 23:56:05Z stephan $
 */
public class OTJIncrementalImageBuilder extends IncrementalImageBuilder {


	public OTJIncrementalImageBuilder(JavaBuilder javaBuilder) {
		super(javaBuilder);
	}
	
	/**
	 * For each affected source file, guess whether it could be a role
	 * file and include its team in the compilation process.
	 */
	@Override
	protected void addAffectedSourceFiles() {
		super.addAffectedSourceFiles();
		if (this.sourceFiles != null) {
			SourceFile[] oldSources = new SourceFile[this.sourceFiles.size()];
			this.sourceFiles.toArray(oldSources); // copy, because list might be extended.
			for (int i=0; i<oldSources.length; i++) {
				checkAddTeamFile(oldSources[i]);
			}
		}
	}
		
	private void checkAddTeamFile(SourceFile file) {
		IPath path = file.getPath();
		// TODO (SH): should we try more than one level?
		IPath teamPath = path.removeLastSegments(1).addFileExtension("java"); //$NON-NLS-1$
		if (!teamPath.isEmpty()) {
			SourceFile teamFile = this.javaBuilder.findSourceByPath(teamPath);
			if (teamFile != null) {
				if (JavaBuilder.DEBUG)
					System.out.println("Adding team file: "+teamPath); //$NON-NLS-1$
				this.sourceFiles.add(teamFile);
			}
		}
	}
}
