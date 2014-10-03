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
package org.eclipse.objectteams.otdt.internal.refactoring.adaptor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.formatter.IndentManipulation;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.jdt.core.refactoring.descriptors.ExtractInterfaceDescriptor;
import org.eclipse.jdt.core.refactoring.descriptors.JavaRefactoringDescriptor;
import org.eclipse.jdt.internal.core.refactoring.descriptors.RefactoringSignatureDescriptorFactory;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility;
import org.eclipse.jdt.internal.corext.refactoring.JDTRefactoringDescriptorComment;
import org.eclipse.jdt.internal.corext.refactoring.JavaRefactoringDescriptorUtil;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.jdt.internal.corext.refactoring.changes.CreateCompilationUnitChange;
import org.eclipse.jdt.internal.corext.refactoring.changes.DynamicValidationRefactoringChange;
import org.eclipse.jdt.internal.corext.refactoring.util.ResourceUtil;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.JdtFlags;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.ltk.core.refactoring.CategorizedTextEditGroup;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextEditBasedChange;
import org.eclipse.ltk.core.refactoring.TextEditBasedChangeGroup;
import org.eclipse.ltk.core.refactoring.TextEditChangeGroup;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.internal.refactoring.RefactoringMessages;
import org.eclipse.objectteams.otdt.internal.refactoring.corext.rename.BaseCallFinder;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;

import base org.eclipse.jdt.internal.corext.refactoring.structure.ExtractInterfaceProcessor;
import base org.eclipse.jdt.internal.corext.refactoring.structure.constraints.SuperTypeRefactoringProcessor;
import base org.eclipse.jdt.internal.corext.util.CodeFormatterUtil;
import base org.eclipse.jdt.internal.ui.refactoring.ExtractInterfaceWizard.ExtractInterfaceInputPage;


/**
 * When extracting an interface from a role, offer to create the new interface
 * as a role of the enclosing team.
 * 
 * @author stephan
 */
@SuppressWarnings({ "restriction", "decapsulation" })
public team class ExtractInterfaceAdaptor {
	
	/**
	 * Step 1: add a checkbox to the wizard: "[ ] Create as role of the enclosing team"
	 */
	protected class InputPage playedBy ExtractInterfaceInputPage {
		
		Processor getProcessor() -> get ExtractInterfaceProcessor fProcessor;
		Button createCheckbox(Composite parent, String title, boolean value)
		-> Button createCheckbox(Composite parent, String title, boolean value);
		
		Button fAsRole;
		
		void createAsRoleCheckBox(Composite parent) <- after Text createTextInputField(Composite parent)
			when (OTModelManager.isRole(getProcessor().getSubType()));
		void createAsRoleCheckBox(Composite parent) {
			String title= RefactoringMessages.ExtractInterfaceAdaptor_createAsRole_checkbox; 
			boolean defaultValue= true;
			fAsRole = createCheckbox(parent,  title, defaultValue);
			getProcessor().setAsRole(defaultValue);
			fAsRole.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					getProcessor().setAsRole(fAsRole.getSelection());
				}
			});
		}
	}
	/**
	 * Superclass of the main role {@link Processor} in order to adapt final methods.
	 * This role (and its sub) is only active when marked as "fAsRole", which is set from the checkbox.
	 */
	protected class SuperTypeProcessor playedBy SuperTypeRefactoringProcessor when (fAsRole) {
		boolean fAsRole = false;
		
		createTypeTemplate <- replace createTypeTemplate;
		callin String createTypeTemplate(ICompilationUnit unit, String imports, String fileComment, String comment, String content) 
			throws CoreException 
		{ 
			return base.createTypeTemplate(unit, imports, fileComment, comment, content); // no adaptation, overridden for real work
		}
		
		createTypeSource <- replace createTypeSource;
		callin String createTypeSource(ICompilationUnit extractedWorkingCopy, IType superType) 
			throws CoreException 
		{
			return base.createTypeSource(extractedWorkingCopy, superType); // no adaptation, overridden for real work
		}
	}
	/**
	 * Main adaptation: create different changes: don't create a new compilation unit,
	 * but insert the new interface into the enclosing team.
	 * This role is also a team to provide a temporary activation context for its
	 * nested role {@link Formatter}.
	 */
	protected team class Processor extends SuperTypeProcessor playedBy ExtractInterfaceProcessor {
		
		IMember[] getMembers() 			-> get IMember[] fMembers;
		protected IType getSubType() 	-> get IType fSubType;

		// indentation of the new interface depends on the nesting level of the current role (consumed by nested role Formatter).
		int indentationLevel;
		
		public void setAsRole(boolean value) {
			fAsRole = value;
		}
		
		createChange <- replace createChange;

		/** This method is essentially copied from its base and adjusted as marked. */
		@SuppressWarnings({ "inferredcallout", "basecall" })
		callin Change createChange(IProgressMonitor monitor) throws CoreException {
			Assert.isNotNull(monitor);
			IMember[] fMembers = getMembers();
			try {
				monitor.beginTask("", 1); //$NON-NLS-1$
				monitor.setTaskName(RefactoringCoreMessages.ExtractInterfaceProcessor_creating);
				final Map<String, String> arguments= new HashMap<String, String>();
				String project= null;
				final IJavaProject javaProject= fSubType.getJavaProject();
				if (javaProject != null)
					project= javaProject.getElementName();
				int flags= JavaRefactoringDescriptor.JAR_MIGRATION | JavaRefactoringDescriptor.JAR_REFACTORING | RefactoringDescriptor.STRUCTURAL_CHANGE | RefactoringDescriptor.MULTI_CHANGE;
				try {
					if (fSubType.isLocal() || fSubType.isAnonymous())
						flags|= JavaRefactoringDescriptor.JAR_SOURCE_ATTACHMENT;
				} catch (JavaModelException exception) {
					JavaPlugin.log(exception);
				}
//{ObjectTeams: changed part: get new type from the enclosing team instead of creating a new compilation unit:
				final IType type= fSubType.getDeclaringType().getType(fSuperName);
// SH}
				final String description= Messages.format(RefactoringCoreMessages.ExtractInterfaceProcessor_description_descriptor_short, BasicElementLabels.getJavaElementName(fSuperName));
				final String header= Messages.format(RefactoringCoreMessages.ExtractInterfaceProcessor_descriptor_description, new String[] { JavaElementLabels.getElementLabel(type, JavaElementLabels.ALL_FULLY_QUALIFIED), JavaElementLabels.getElementLabel(fSubType, JavaElementLabels.ALL_FULLY_QUALIFIED) });
				final JDTRefactoringDescriptorComment comment= new JDTRefactoringDescriptorComment(project, this, header);
				comment.addSetting(Messages.format(RefactoringCoreMessages.ExtractInterfaceProcessor_refactored_element_pattern, JavaElementLabels.getElementLabel(type, JavaElementLabels.ALL_FULLY_QUALIFIED)));
				final String[] settings= new String[fMembers.length];
				for (int index= 0; index < settings.length; index++)
					settings[index]= JavaElementLabels.getElementLabel(fMembers[index], JavaElementLabels.ALL_FULLY_QUALIFIED);
				comment.addSetting(JDTRefactoringDescriptorComment.createCompositeSetting(RefactoringCoreMessages.ExtractInterfaceProcessor_extracted_members_pattern, settings));
				addSuperTypeSettings(comment, true);
				final ExtractInterfaceDescriptor descriptor= RefactoringSignatureDescriptorFactory.createExtractInterfaceDescriptor(project, description, comment.asString(), arguments, flags);
				arguments.put(JavaRefactoringDescriptorUtil.ATTRIBUTE_INPUT, JavaRefactoringDescriptorUtil.elementToHandle(project, fSubType));
				arguments.put(JavaRefactoringDescriptorUtil.ATTRIBUTE_NAME, fSuperName);
				for (int index= 0; index < fMembers.length; index++)
					arguments.put(JavaRefactoringDescriptorUtil.ATTRIBUTE_ELEMENT + (index + 1), JavaRefactoringDescriptorUtil.elementToHandle(project, fMembers[index]));
				arguments.put(ATTRIBUTE_COMMENTS, Boolean.valueOf(fComments).toString());
				arguments.put(ATTRIBUTE_REPLACE, Boolean.valueOf(fReplace).toString());
				arguments.put(ATTRIBUTE_INSTANCEOF, Boolean.valueOf(fInstanceOf).toString());
//{ObjectTeams: different way to construct this change:
				TextEditBasedChange[] allChanges = fChangeManager.getAllChanges(); 
				if (fSuperSource != null && fSuperSource.length() > 0) {
					ICompilationUnit compilationUnit = fSubType.getCompilationUnit();
					// find insert position, start at name of enclosing team ...
					ISourceRange nameRange = fSubType.getDeclaringType().getNameRange();
					int offset = nameRange.getOffset() + nameRange.getLength();
					// ... then travel to the line past the next '{' 
					String source = compilationUnit.getSource();
					while (offset < source.length() && source.charAt(offset++) != '{');
					while (offset < source.length() && source.charAt(offset++) != '\n');
					// create the change:
					CompilationUnitChange createIfcChange = new CompilationUnitChange("", compilationUnit); //$NON-NLS-1$
					InsertEdit edit = new InsertEdit(offset, fSuperSource);
					createIfcChange.setEdit(edit);
					// insert it into the existing changes ...
					CompilationUnitChange combinedChange = (CompilationUnitChange) fChangeManager.get(compilationUnit);
					// ... using the same category:
					TextEditChangeGroup[] groups = combinedChange.getTextEditChangeGroups();
					TextEditGroup group = new CategorizedTextEditGroup(NLS.bind(RefactoringMessages.ExtractInterfaceAdaptor_createInterface_changeName,fSuperName), edit, groups[0].getGroupCategorySet());
					// finally assemble everything:
					combinedChange.addEdit(edit);
					combinedChange.addChangeGroup(new TextEditChangeGroup(createIfcChange, group));
					allChanges = new TextEditBasedChange[]{combinedChange};
				}
// SH}
				final DynamicValidationRefactoringChange change= new DynamicValidationRefactoringChange(descriptor, RefactoringCoreMessages.ExtractInterfaceRefactoring_name, allChanges);
/* orig:
				final IFile file= ResourceUtil.getFile(fSubType.getCompilationUnit());
				if (fSuperSource != null && fSuperSource.length() > 0)
					change.add(new CreateCompilationUnitChange(fSubType.getPackageFragment().getCompilationUnit(JavaModelUtil.getRenamedCUName(fSubType.getCompilationUnit(), fSuperName)), fSuperSource, file.getCharset(false)));
  :giro */
				monitor.worked(1);
				return change;
			} finally {
				monitor.done();
			}
		}
		/** avoid generating file comment, package declaration and import statements. */
		@Override
		@SuppressWarnings("basecall")
		callin String createTypeTemplate(ICompilationUnit unit, String imports, String fileComment, String comment, String content)
				throws CoreException 
		{
			Assert.isNotNull(unit);
			Assert.isNotNull(imports);
			Assert.isNotNull(content);
			final String delimiter= StubUtility.getLineDelimiterUsed(unit.getJavaProject());

			if (!content.startsWith(JdtFlags.VISIBILITY_STRING_PUBLIC))
				content = JdtFlags.VISIBILITY_STRING_PROTECTED+' '+content;
			String source = StubUtility.getCompilationUnitContent(unit, ""/*packageDeclaration*/, null/*fileComment*/, comment, content, delimiter); //$NON-NLS-1$
			return source+'\n';
		}

		/** This callin activates the enclosing Processor team in order to let the Formatter role feed our special indentation into formatting. */
		@Override
		callin String createTypeSource(ICompilationUnit extractedWorkingCopy, IType superType) 
			throws CoreException 
		{
			IType currentType = superType;
			this.indentationLevel = 0;
			while ((currentType = currentType.getDeclaringType()) != null)
				this.indentationLevel++;
			within(this)
				return base.createTypeSource(extractedWorkingCopy, superType);
		}
		
		/** Sole purpose of this role: manipulate the indentation level if the enclosing Processor team is active. */
		protected class Formatter playedBy CodeFormatterUtil 
		{
			TextEdit format2(int kind, String source, int indentationLevel, String lineSeparator, Map<String, String> options)
			<- replace TextEdit format2(int kind, String source, int indentationLevel, String lineSeparator, Map<String, String> options);
			
			static callin TextEdit format2(int kind, String source, int indentationLevel, String lineSeparator, Map<String, String> options) {
				return base.format2(kind, source, Processor.this.indentationLevel, lineSeparator, options);
			}
		}
	}
}
