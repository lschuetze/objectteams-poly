/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * 		IBM Corporation - Initial API and implementation
 * 		Fraunhofer FIRST - Initial API and implementation
 * 		Technical University Berlin - Initial API and implementation
 *******************************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.callinmarkers;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.IClassFileEditorInput;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author gis
 */
public abstract class JavaEditorActivationListener implements IPartListener2, IWindowListener
{
//{OT_COPY_PASTE from CompilationUnitEditor and ClassFileEditor
	protected abstract void activeJavaEditorChanged(IWorkbenchPart editor);

	/*
	 * @see org.eclipse.jdt.internal.ui.javaeditor.JavaEditor#getInputJavaElement()
	 */
	protected IJavaElement getInputJavaElement(IEditorPart editor) {
	    final IEditorInput editorInput = editor.getEditorInput();
	    
	    if (editorInput instanceof IClassFileEditorInput)
	    	return ((IClassFileEditorInput) editorInput).getClassFile();
		else if (editor instanceof JavaEditor)
			return JavaPlugin.getDefault().getWorkingCopyManager().getWorkingCopy(editorInput);
	    
	    return null;
	}
//carp}
	

//{OT_COPY_PASTE from org.eclipse.jdt.internal.ui.javaeditor.ASTProvider.ActivationListener
	protected IWorkbenchPart fActiveEditor;

	public void installListener() {
		try {
			PlatformUI.getWorkbench().addWindowListener(this);
		} catch (IllegalStateException ise) {
			return; // wrongly trying to install in a headless environment?
		}
		
		// Ensure existing windows get connected
		for (IWorkbenchWindow windows : PlatformUI.getWorkbench().getWorkbenchWindows())
			windows.getPartService().addPartListener(this);
	}
	
    public void uninstallListener() {
    	try {
    		PlatformUI.getWorkbench().removeWindowListener(this);
    	} catch (IllegalStateException ise) {
    		return; // wrongly trying to uninstall in a headless environment?
    	}
		
		// Ensure existing windows get disconnected
		for (IWorkbenchWindow windows : PlatformUI.getWorkbench().getWorkbenchWindows())
			windows.getPartService().removePartListener(this);
	}
	
	public void partActivated(IWorkbenchPartReference ref) {
		if (isJavaEditor(ref) && !isActiveEditor(ref))
			activeJavaEditorChanged(ref.getPart(true));
	}
	
	public void partBroughtToTop(IWorkbenchPartReference ref) {
		if (isJavaEditor(ref) && !isActiveEditor(ref))
			activeJavaEditorChanged(ref.getPart(true));
	}
	
	public void partClosed(IWorkbenchPartReference ref) {
		if (isActiveEditor(ref))
			activeJavaEditorChanged(null);
	}
	
	public void partDeactivated(IWorkbenchPartReference ref) {
	}
	
	public void partOpened(IWorkbenchPartReference ref) {
		if (isJavaEditor(ref) && !isActiveEditor(ref))
			activeJavaEditorChanged(ref.getPart(true));
	}
	
	public void partHidden(IWorkbenchPartReference ref) {
	}
	
	public void partVisible(IWorkbenchPartReference ref) {
		if (isJavaEditor(ref) && !isActiveEditor(ref))
			activeJavaEditorChanged(ref.getPart(true));
	}
	
	public void partInputChanged(IWorkbenchPartReference ref) {
	}

	public void windowActivated(IWorkbenchWindow window) {
		IWorkbenchPartReference ref= window.getPartService().getActivePartReference();
		if (isJavaEditor(ref) && !isActiveEditor(ref))
			activeJavaEditorChanged(ref.getPart(true));
	}

	public void windowDeactivated(IWorkbenchWindow window) {
	}

	public void windowClosed(IWorkbenchWindow window) {
		if (fActiveEditor != null && fActiveEditor.getSite() != null && window == fActiveEditor.getSite().getWorkbenchWindow()) 
			activeJavaEditorChanged(null);
		window.getPartService().removePartListener(this);
	}

	public void windowOpened(IWorkbenchWindow window) {
		window.getPartService().addPartListener(this);
	}
	
	protected boolean isActiveEditor(IWorkbenchPartReference ref) {
		return ref != null && isActiveEditor(ref.getPart(false));
	}
	
	protected boolean isActiveEditor(IWorkbenchPart part) {
		return part != null && (part == fActiveEditor);
	}
	
	protected boolean isJavaEditor(IWorkbenchPartReference ref) {
		if (ref == null)
			return false;
		
		String id= ref.getId();
		return JavaUI.ID_CF_EDITOR.equals(id) || JavaUI.ID_CU_EDITOR.equals(id); 
	}
}
//carp} -- end OT_COPY_PASTE
