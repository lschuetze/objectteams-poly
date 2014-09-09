/**********************************************************************
 * This file is part of "Object Teams Dynamic Runtime Environment"
 * 
 * Copyright 2009, 2014 Oliver Frank and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 * 
 * Contributors:
 *		Oliver Frank - Initial API and implementation
 *		Stephan Herrmann - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otredyn.bytecode.asm;

import java.util.HashSet;

import org.eclipse.objectteams.otredyn.bytecode.Binding;
import org.eclipse.objectteams.otredyn.bytecode.asm.Attributes.CallinBindingsAttribute;
import org.eclipse.objectteams.otredyn.bytecode.asm.Attributes.OTClassFlagsAttribute;
import org.eclipse.objectteams.otredyn.bytecode.asm.Attributes.RoleBaseBindingsAttribute;
import org.eclipse.objectteams.otredyn.bytecode.asm.Attributes.CallinBindingsAttribute.MultiBinding;
import org.eclipse.objectteams.otredyn.bytecode.asm.Attributes.CallinPrecedenceAttribute;
import org.eclipse.objectteams.otredyn.bytecode.asm.Attributes.OTSpecialAccessAttribute;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.eclipse.objectteams.otredyn.bytecode.asm.AsmBoundClass.ASM_API;

/**
 * This class is used to parse the bytecode of a class.
 * It sets the informations, that are parsed, in the {@link AsmBoundClass}
 * @author Oliver Frank
 */
class AsmClassVisitor extends ClassVisitor {

	private AsmBoundClass clazz;
	
	public AsmClassVisitor(AsmBoundClass clazz) {
		super(ASM_API);
		this.clazz = clazz;
	}
	
	/**
	 * Parses common information about the class.
	 */
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		clazz.setSuperClassName(superName);
		clazz.setSuperInterfaces(interfaces);
		clazz.setModifiers(access);
	}
	
	/**
	 * Parses the methods of the class
	 */
	@Override
	public MethodVisitor visitMethod(int access, final String name, final String desc, String signature, String[] exceptions) {
		clazz.addMethod(name, desc, (access & Opcodes.ACC_STATIC) != 0, (access & (Opcodes.ACC_PUBLIC|Opcodes.ACC_PROTECTED|Opcodes.ACC_PRIVATE)));
		if (clazz.isTeam() || clazz.isRole())
			// check for method annotation ImplicitTeamActivation:
			return new MethodVisitor(this.api) {
				@Override
				public AnnotationVisitor visitAnnotation(String annDesc, boolean visible) {
					if (annDesc.equals(AddImplicitActivationAdapter.ANNOTATION_IMPLICIT_ACTIVATION))
						clazz.registerMethodForImplicitActivation(name+desc);
					return super.visitAnnotation(annDesc, visible);
				}
			};
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	/**
	 * Parses the fields of the class
	 */
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		clazz.addField(name, desc, (access & Opcodes.ACC_STATIC) != 0, (access & (Opcodes.ACC_PUBLIC|Opcodes.ACC_PROTECTED|Opcodes.ACC_PRIVATE)));
		return super.visitField(access, name, desc, signature, value);
	}
	
	/**
	 * Parses the class file attributes of a class.
	 * This is only needed, if the class is a team.
	 */
	@Override
	public void visitAttribute(Attribute attribute) {
		if (clazz.boundBaseClasses == null)
			clazz.boundBaseClasses = new HashSet<String>();
		if (attribute.type.equals(Attributes.ATTRIBUTE_OT_DYN_CALLIN_BINDINGS)) {
			CallinBindingsAttribute attr = (CallinBindingsAttribute) attribute;
			MultiBinding[] multiBindings = attr.getBindings();
			for (int i=multiBindings.length-1; i>=0; i--) { // reverse loop to ensure proper overwriting:
				String roleClassName = multiBindings[i].getRoleClassName();
				String callinLabel = multiBindings[i].getCallinLabel();
				String baseClassName = multiBindings[i].getBaseClassName();
				clazz.boundBaseClasses.add(baseClassName.replace('/', '.'));
				String[] baseMethodNames = multiBindings[i].getBaseMethodNames();
				String[] baseMethodSignatures = multiBindings[i].getBaseMethodSignatures();
				String[] declaringBaseClassNames = multiBindings[i].getDeclaringBaseClassName();
				int callinModifier = multiBindings[i].getCallinModifier();
				int[] callinIds = multiBindings[i].getCallinIds();
				int[] baseFlags = multiBindings[i].getBaseFlags();
				boolean handleCovariantReturn = multiBindings[i].isHandleCovariantReturn();
				for (int j = 0; j < baseMethodNames.length; j++) {
					Binding binding = new Binding(clazz, roleClassName, callinLabel, baseClassName, 
												  baseMethodNames[j], baseMethodSignatures[j], declaringBaseClassNames[j],
												  callinModifier, callinIds[j], baseFlags[j], handleCovariantReturn);
					clazz.addBinding(binding);
				}
			}
		} else if (attribute.type.equals(Attributes.ATTRIBUTE_CALLIN_PRECEDENCE)) {
			CallinPrecedenceAttribute attr = (CallinPrecedenceAttribute)attribute;
			clazz.precedenceses.add(attr.labels);
		} else if (attribute.type.equals(Attributes.ATTRIBUTE_OT_CLASS_FLAGS)) {
			clazz.setOTClassFlags(((OTClassFlagsAttribute)attribute).flags);
		} else if (attribute.type.equals(Attributes.ATTRIBUTE_OT_SPECIAL_ACCESS)) {
			((OTSpecialAccessAttribute)attribute).registerAt(clazz);
		} else if (attribute.type.equals(Attributes.ATTRIBUTE_ROLE_BASE_BINDINGS)) {
			for (String base : ((RoleBaseBindingsAttribute) attribute).bases)
				clazz.boundBaseClasses.add(base.replace('/', '.'));
		}
	}
	
	/**
	 * check for class annotation ImplicitTeamActivation:
	 */
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (desc.equals(AddImplicitActivationAdapter.ANNOTATION_IMPLICIT_ACTIVATION))
			clazz.enableImplicitActivation();
		return super.visitAnnotation(desc, visible);
	}
}
