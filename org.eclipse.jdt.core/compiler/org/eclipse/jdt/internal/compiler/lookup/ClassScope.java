/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Fraunhofer FIRST - extended API and implementation
 *     Technical University Berlin - extended API and implementation
 *     Stephan Herrmann <stephan@cs.tu-berlin.de> - Contributions for
 *     						Bug 328281 - visibility leaks not detected when analyzing unused field in private class
 *     						Bug 300576 - NPE Computing type hierarchy when compliance doesn't match libraries
 *     						Bug 354536 - compiling package-info.java still depends on the order of compilation units
 *     						Bug 349326 - [1.7] new warning for missing try-with-resources
 *     						Bug 358903 - Filter practically unimportant resource leak warnings
 *							Bug 395977 - [compiler][resource] Resource leak warning behavior possibly incorrect for anonymous inner class
 *							Bug 395002 - Self bound generic class doesn't resolve bounds properly for wildcards for certain parametrisation.
 *							Bug 416176 - [1.8][compiler][null] null type annotations cause grief on type variables
 *							Bug 427199 - [1.8][resource] avoid resource leak warnings on Streams that have no resource
 *							Bug 429958 - [1.8][null] evaluate new DefaultLocation attribute of @NonNullByDefault
 *							Bug 434570 - Generic type mismatch for parametrized class annotation attribute with inner class
 *							Bug 444024 - [1.8][compiler][null] Type mismatch error in annotation generics assignment which happens "sometimes"
 *							Bug 459967 - [null] compiler should know about nullness of special methods like MyEnum.valueOf()
 *        Andy Clement (GoPivotal, Inc) aclement@gopivotal.com - Contributions for
 *                          Bug 415821 - [1.8][compiler] CLASS_EXTENDS target type annotation missing for anonymous classes
 *     het@google.com - Bug 456986 - Bogus error when annotation processor generates annotation type
 *     Lars Vogel <Lars.Vogel@vogella.com> - Contributions for
 *     						Bug 473178
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult.CheckPoint;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Expression.DecapsulationState;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.RecordComponent;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeParameter;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions.WeavingScheme;
import org.eclipse.jdt.internal.compiler.impl.IrritantSet;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.compiler.problem.IProblemRechecker;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObject;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.core.exceptions.InternalCompilerError;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.AbstractMethodMappingDeclaration;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.CallinMappingDeclaration;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.TypeValueParameter;
import org.eclipse.objectteams.otdt.internal.core.compiler.bytecode.WordValueAttribute;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.Dependencies;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.ITranslationStates;
import org.eclipse.objectteams.otdt.internal.core.compiler.control.StateHelper;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.CallinCalloutBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.CallinCalloutScope;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.ITeamAnchor;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.OTClassScope;
import org.eclipse.objectteams.otdt.internal.core.compiler.lookup.RoleTypeBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.RoleModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.model.TeamModel;
import org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.copyinheritance.CopyInheritance;
import org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.transformer.RoleMigrationImplementor;
import org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.transformer.RoleSplitter;
import org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.transformer.StandardElementGenerator;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.AstGenerator;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.IAlienScopeTypeReference;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.Protections;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.Sorting;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.TSuperHelper;
import org.eclipse.objectteams.otdt.internal.core.compiler.util.TypeAnalyzer;

/**
 * OTDT changes:
 *
 * GENERAL:
 * What: Additional setup:
 * 		 + Super-team linkage (connectSuperTeam() from connectAllSupers())
 * 		 + Base-class linkage (connectBaseclass() from buildFieldsAndMethods())
 *       + build method mappings (buildCallinCallouts() from buildFieldsAndMethods())
 * 	     + setup RoleFileCache for team (from buildType())
 *
 * What: Connect building of local type bindings with Dependencies.
 * Why:  Local types are found late, need to catch up in the translation process
 *
 * What: Do additional setup when linking referenceContext to its binding.
 * How:  Use TypeDeclaration.setBinding() instead of a direct assignment.
 *
 * What: Create OTClassScope for nested teams.
 * Why:  While nested teams are member types (not created by CompilationUnitScope)
 *       they still need a specialized scope.
 *
 * What: Add generated elements (addGenerated{Type,Field,Method}())
 *       These methods create (unresolved) bindings.
 *
 * What: Public entries for connecting generated elements
 * Which: + connectTypeHierarchyForGenerated()
 * 		  + connectSuperclassForGenerated()
 *        + buildFieldsAndMethodsForGenerated()
 *
 * ROLE FILES:
 * What: Add member types also to the enclosing team-package
 *
 * What: Manipulate order of processing for role files.
 * Why:  Establishing STATE_BINDINGS_COMPLETE may trigger introduction of role files.
 *       Compiler.accept will try to complete these which conflicts with the current process.
 * How:  + TypeModel._currentlyProcessedState and TeamModel._blockCatchup
 *         capture the state of processing
 *       + buildFieldsAndMethods() and connectTypeHierarchy() bail out during blockCatchup
 *       + traversing members in buildFieldsAndMethods() and connectMemberTypes()
 *         include newly introduces role files within their loops.
 *
 * VALIDITY:
 * What: Allow interfaces in nested teams.
 *
 * What: Flags/modifiers:
 * 		 + check for role / team modifiers
 *       + accept AccRole and AccSynthetic
 * 		 + accept AccProtected for roles in role files
 * 		   Why: top level types cannot be protected, but role files are not top-level ;-)
 *
 * What: Team.Confined must not have a superclass.
 *
 * What: Team must not extend non-team and vice versa.
 *
 * LOOKUP:
 * What: if findSupertype() failed, consider dropping __OT__
 * Why:  RoleSplitter might have been over-eager.
 * How:  need to roll back: remove problem binding, remove IProblem.
 */
@SuppressWarnings({"rawtypes"})
public class ClassScope extends Scope {

	public TypeDeclaration referenceContext;
	public TypeReference superTypeReference;
	java.util.ArrayList<Object> deferredBoundChecks; // contains TypeReference or Runnable. TODO consider making this a List<Runnable>

	public ClassScope(Scope parent, TypeDeclaration context) {
		super(Scope.CLASS_SCOPE, parent);
		this.referenceContext = context;
		this.deferredBoundChecks = null; // initialized if required
	}

	void buildAnonymousTypeBinding(SourceTypeBinding enclosingType, ReferenceBinding supertype) {
		LocalTypeBinding anonymousType = buildLocalType(enclosingType, enclosingType.fPackage);
		anonymousType.modifiers |= ExtraCompilerModifiers.AccLocallyUsed; // tag all anonymous types as used locally
		int inheritedBits = supertype.typeBits; // for anonymous class assume same properties as its super (as a closeable) ...
		// ... unless it overrides close():
		if ((inheritedBits & TypeIds.BitWrapperCloseable) != 0) {
			AbstractMethodDeclaration[] methods = this.referenceContext.methods;
			if (methods != null) {
				for (int i=0; i<methods.length; i++) {
					if (CharOperation.equals(TypeConstants.CLOSE, methods[i].selector) && methods[i].arguments == null) {
						inheritedBits &= TypeIds.InheritableBits;
						break;
					}
				}
			}
		}
		anonymousType.typeBits |= inheritedBits;
		anonymousType.setPermittedTypes(Binding.NO_PERMITTEDTYPES); // JLS 15 JEP 360 Preview - Sec 15.9.5
		if (supertype.isInterface()) {
			anonymousType.setSuperClass(getJavaLangObject());
			anonymousType.setSuperInterfaces(new ReferenceBinding[] { supertype });
			TypeReference typeReference = this.referenceContext.allocation.type;
			if (typeReference != null) {
				this.referenceContext.superInterfaces = new TypeReference[] { typeReference };
				if ((supertype.tagBits & TagBits.HasDirectWildcard) != 0) {
					problemReporter().superTypeCannotUseWildcard(anonymousType, typeReference, supertype);
					anonymousType.tagBits |= TagBits.HierarchyHasProblems;
					anonymousType.setSuperInterfaces(Binding.NO_SUPERINTERFACES);
				} if (supertype.isSealed()) {
					problemReporter().sealedAnonymousClassCannotExtendSealedType(typeReference, supertype);
					anonymousType.tagBits |= TagBits.HierarchyHasProblems;
					anonymousType.setSuperInterfaces(Binding.NO_SUPERINTERFACES);
				}
			}
		} else {
			anonymousType.setSuperClass(supertype);
			anonymousType.setSuperInterfaces(Binding.NO_SUPERINTERFACES);
			checkForEnumSealedPreview(supertype, anonymousType);
			TypeReference typeReference = this.referenceContext.allocation.type;
			if (typeReference != null) { // no check for enum constant body
				this.referenceContext.superclass = typeReference;
				if (supertype.erasure().id == TypeIds.T_JavaLangEnum) {
					problemReporter().cannotExtendEnum(anonymousType, typeReference, supertype);
					anonymousType.tagBits |= TagBits.HierarchyHasProblems;
					anonymousType.setSuperClass(getJavaLangObject());
				} else if (supertype.erasure().id == TypeIds.T_JavaLangRecord) {
					if (!(this.referenceContext.isRecord())) {
						problemReporter().recordCannotExtendRecord(anonymousType, typeReference, supertype);
						anonymousType.tagBits |= TagBits.HierarchyHasProblems;
						anonymousType.setSuperClass(getJavaLangObject());
					}
				} else if (supertype.isFinal()) {
					problemReporter().anonymousClassCannotExtendFinalClass(typeReference, supertype);
					anonymousType.tagBits |= TagBits.HierarchyHasProblems;
					anonymousType.setSuperClass(getJavaLangObject());
				} else if ((supertype.tagBits & TagBits.HasDirectWildcard) != 0) {
					problemReporter().superTypeCannotUseWildcard(anonymousType, typeReference, supertype);
					anonymousType.tagBits |= TagBits.HierarchyHasProblems;
					anonymousType.setSuperClass(getJavaLangObject());
				} else if (supertype.isSealed()) {
					problemReporter().sealedAnonymousClassCannotExtendSealedType(typeReference, supertype);
					anonymousType.tagBits |= TagBits.HierarchyHasProblems;
					anonymousType.setSuperClass(getJavaLangObject());
				}
			}
		}
		connectMemberTypes();
		buildFieldsAndMethods();
//{ObjectTeams: catchup also in OT-specific process (see class comment concerning local types in Dependencies):
	  if (this.referenceContext.isRole()) {
		RoleModel role = this.referenceContext.getRoleModel();
		Dependencies.ensureRoleState(role, ITranslationStates.STATE_FAULT_IN_TYPES-1);
/*orig*/anonymousType.faultInTypesForFieldsAndMethods();
		Dependencies.ensureRoleState(role, ITranslationStates.STATE_METHODS_VERIFIED-1);
	  } else {
// orig:
		anonymousType.faultInTypesForFieldsAndMethods();
// :giro
	  }
// SH}
		anonymousType.verifyMethods(environment().methodVerifier());
	}

	private void checkForEnumSealedPreview(ReferenceBinding supertype, LocalTypeBinding anonymousType) {
		if (compilerOptions().sourceLevel < ClassFileConstants.JDK15
				|| !compilerOptions().enablePreviewFeatures
				|| !supertype.isEnum()
				|| !(supertype instanceof SourceTypeBinding))
			return;

		SourceTypeBinding sourceSuperType = (SourceTypeBinding) supertype;
		ReferenceBinding[] permTypes = sourceSuperType.permittedTypes();
		int sz = permTypes == null ? 0 : permTypes.length;
		if (sz == 0) {
			permTypes = new ReferenceBinding[] {anonymousType};
		} else {
			System.arraycopy(permTypes, 0,
					permTypes = new ReferenceBinding[sz + 1], 0,
					sz);
			permTypes[sz] = anonymousType;
		}
		anonymousType.modifiers |= ClassFileConstants.AccFinal; // JLS 15 / sealed preview/Sec 8.9.1
		sourceSuperType.setPermittedTypes(permTypes);
	}

	void buildComponents() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
		if (!sourceType.isRecord()) return;
		if (sourceType.areComponentsInitialized()) return;
		if (this.referenceContext.recordComponents == null) {
			sourceType.setComponents(Binding.NO_COMPONENTS);
			return;
		}
		// count the number of fields vs. initializers
		RecordComponent[] recComps = this.referenceContext.recordComponents;
		int size = recComps.length;
		int count = size;

		// iterate the field declarations to create the bindings, lose all duplicates
		RecordComponentBinding[] componentBindings = new RecordComponentBinding[count];
		HashtableOfObject knownComponentNames = new HashtableOfObject(count);
		count = 0;
		for (int i = 0; i < size; i++) {
			RecordComponent recComp = recComps[i];
			RecordComponentBinding compBinding = new RecordComponentBinding(sourceType, recComp, null,
					recComp.modifiers | ExtraCompilerModifiers.AccUnresolved);
			compBinding.id = count;
			checkAndSetModifiersForComponents(compBinding, recComp);

			if (knownComponentNames.containsKey(recComp.name)) {
				RecordComponentBinding previousBinding = (RecordComponentBinding) knownComponentNames.get(recComp.name);
				if (previousBinding != null) {
					for (int f = 0; f < i; f++) {
						RecordComponent previousComponent = recComps[f];
						if (previousComponent.binding == previousBinding) {
							// flag the duplicate component name error here.
							problemReporter().recordDuplicateComponent(previousComponent);
							break;
						}
					}
				}
				knownComponentNames.put(recComp.name, null); // ensure that the duplicate field is found & removed
				problemReporter().recordDuplicateComponent(recComp);
				recComp.binding = null;
			} else {
				knownComponentNames.put(recComp.name, compBinding);
				// remember that we have seen a component with this name
				componentBindings[count++] = compBinding;
			}
		}
		// remove duplicate components
		if (count != componentBindings.length)
			System.arraycopy(componentBindings, 0, componentBindings = new RecordComponentBinding[count], 0, count);
		sourceType.setComponents(componentBindings);
		if (size > 0) {
			sourceType.isVarArgs = recComps[size-1].isVarArgs();
		}
	}
	private void checkAndSetModifiersForComponents(RecordComponentBinding compBinding, RecordComponent comp) {
		int modifiers = compBinding.modifiers;
		int realModifiers = modifiers & ExtraCompilerModifiers.AccJustFlag;
		if (realModifiers  != 0 && comp != null){
			problemReporter().recordComponentsCannotHaveModifiers(comp);
		}
	}

	void buildFields() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
		if (sourceType.areFieldsInitialized()) return;
//{ObjectTeams: prepare for merging value parameters into fields:
		int valueParamCount = valueParamCount(this.referenceContext.typeParameters);
		if (this.referenceContext.fields == null && valueParamCount > 0)
			this.referenceContext.fields = new FieldDeclaration[0];
// SH}
		if (this.referenceContext.fields == null) {
			sourceType.setFields(Binding.NO_FIELDS);
			return;
		}
		// count the number of fields vs. initializers
		FieldDeclaration[] fields = this.referenceContext.fields;
		int size = fields.length;
//{ObjectTeams: value parameters:
/* orig:
		int count = 0;
*/
		int count = valueParamCount;
// SH}
		for (int i = 0; i < size; i++) {
			switch (fields[i].getKind()) {
				case AbstractVariableDeclaration.FIELD:
				case AbstractVariableDeclaration.ENUM_CONSTANT:
					count++;
			}
		}

		// iterate the field declarations to create the bindings, lose all duplicates
		FieldBinding[] fieldBindings = new FieldBinding[count];
		HashtableOfObject knownFieldNames = new HashtableOfObject(count);
//{ObjectTeams: add fields from value parameters
	  if (this.referenceContext.typeParameters != null) {
		TypeValueParameter.resolveValueParameters(this.referenceContext.typeParameters, this, fieldBindings, knownFieldNames);
		count = valueParamCount;
	  } else
// SH}
		count = 0;
		for (int i = 0; i < size; i++) {
			FieldDeclaration field = fields[i];
			if (field.getKind() == AbstractVariableDeclaration.INITIALIZER) {
				// We used to report an error for initializers declared inside interfaces, but
				// now this error reporting is moved into the parser itself. See https://bugs.eclipse.org/bugs/show_bug.cgi?id=212713
			} else {
				FieldBinding fieldBinding = new FieldBinding(field, null, field.modifiers | ExtraCompilerModifiers.AccUnresolved, sourceType);
				fieldBinding.id = count;
				// field's type will be resolved when needed for top level types
				checkAndSetModifiersForField(fieldBinding, field);

				if (knownFieldNames.containsKey(field.name)) {
					FieldBinding previousBinding = (FieldBinding) knownFieldNames.get(field.name);
					if (previousBinding != null) {
						for (int f = 0; f < i; f++) {
							FieldDeclaration previousField = fields[f];
							if (previousField.binding == previousBinding) {
								problemReporter().duplicateFieldInType(sourceType, previousField);
								break;
							}
						}
					}
					knownFieldNames.put(field.name, null); // ensure that the duplicate field is found & removed
					problemReporter().duplicateFieldInType(sourceType, field);
					field.binding = null;
				} else {
					knownFieldNames.put(field.name, fieldBinding);
					// remember that we have seen a field with this name
					fieldBindings[count++] = fieldBinding;
				}
			}
		}
		// remove duplicate fields
		if (count != fieldBindings.length)
			System.arraycopy(fieldBindings, 0, fieldBindings = new FieldBinding[count], 0, count);
		sourceType.tagBits &= ~(TagBits.AreFieldsSorted|TagBits.AreFieldsComplete); // in case some static imports reached already into this type
		sourceType.setFields(fieldBindings);
	}

//{ObjectTeams: helper for the above
	private int valueParamCount(TypeParameter[] typeParameters) {
		int count = 0;
		if (typeParameters != null)
			for (int i = 0; i < typeParameters.length; i++)
				if (typeParameters[i] instanceof TypeValueParameter)
					count++;

		return count;
	}
// SH}

	void buildFieldsAndMethods() {
//{ObjectTeams: signal proccessing:
		if (!StateHelper.startProcessing(this.referenceContext,
									   ITranslationStates.STATE_LENV_DONE_FIELDS_AND_METHODS,
									   LookupEnvironment.BUILD_FIELDS_AND_METHODS))
			return; // catchup was blocked.
// SH}
		buildComponents();
		buildFields();
		buildMethods();
//{ObjectTeams: build callins and callouts
		buildCallinCallouts();
		// now, as we have field bindings, connect baseclass (which might depend on a field as anchor):
		connectBaseclass();
// Markus Witte}

		SourceTypeBinding sourceType = this.referenceContext.binding;
		if (!sourceType.isPrivate() && sourceType.superclass instanceof SourceTypeBinding && sourceType.superclass.isPrivate())
			((SourceTypeBinding) sourceType.superclass).tagIndirectlyAccessibleMembers();

		if (sourceType.isMemberType() && !sourceType.isLocalType())
			 ((MemberTypeBinding) sourceType).checkSyntheticArgsAndFields();

//{ObjectTeams: similar for value parameters:
		if (this.referenceContext.typeParameters != null) {
			TypeParameter[] typeParameters = this.referenceContext.typeParameters;
			for (int i = 0; i < typeParameters.length; i++) {
				if (typeParameters[i] instanceof TypeValueParameter) {
					TypeValueParameter param = (TypeValueParameter)typeParameters[i];
					sourceType.addSyntheticArgForValParam(param);
				}
			}
			// 2. go: type anchors may refer to value parameters (connected above)
			for (int i = 0; i < typeParameters.length; i++) {
				if (!(typeParameters[i] instanceof TypeValueParameter))
					typeParameters[i].connectTypeAnchors(this);
			}
		}
// SH}
//{ObjectTeams: don't cache member types/length, new role files might be added during the loop:
/* Orig:
		ReferenceBinding[] memberTypes = sourceType.memberTypes;
		for (int i = 0, length = memberTypes.length; i < length; i++)
  :giro */
		for (int i = 0; i < sourceType.memberTypes.length; i++) {
		  ReferenceBinding memberType = sourceType.memberTypes[i];

//addition: roles can be binary! or already compiled along some other path
		  if (   !memberType.isBinaryBinding()
			  && !StateHelper.hasState(memberType, ITranslationStates.STATE_FINAL))
/* orig:
			 ((SourceTypeBinding) memberTypes[i]).scope.buildFieldsAndMethods();
  :giro */
			((SourceTypeBinding) memberType).scope.buildFieldsAndMethods();
		}
// SH}
	}

	private LocalTypeBinding buildLocalType(SourceTypeBinding enclosingType, PackageBinding packageBinding) {

		this.referenceContext.scope = this;
		this.referenceContext.staticInitializerScope = new MethodScope(this, this.referenceContext, true);
		this.referenceContext.initializerScope = new MethodScope(this, this.referenceContext, false);

		// build the binding or the local type
		LocalTypeBinding localType = new LocalTypeBinding(this, enclosingType, innermostSwitchCase());
//{ObjectTeams: was assignment; use setter to allow additional setup
	/* @original
		this.referenceContext.binding = localType;
	 */
		this.referenceContext.setBinding(localType);
// SH}
		checkAndSetModifiers();
		buildTypeVariables();

		// Look at member types
		ReferenceBinding[] memberTypeBindings = Binding.NO_MEMBER_TYPES;
		if (this.referenceContext.memberTypes != null) {
			int size = this.referenceContext.memberTypes.length;
			memberTypeBindings = new ReferenceBinding[size];
			int count = 0;
			nextMember : for (int i = 0; i < size; i++) {
				TypeDeclaration memberContext = this.referenceContext.memberTypes[i];
				switch(TypeDeclaration.kind(memberContext.modifiers)) {
					case TypeDeclaration.INTERFACE_DECL :
						if (compilerOptions().sourceLevel >= ClassFileConstants.JDK16)
							break;
						//$FALL-THROUGH$
					case TypeDeclaration.ANNOTATION_TYPE_DECL :
						problemReporter().illegalLocalTypeDeclaration(memberContext);
						continue nextMember;
				}
				ReferenceBinding type = localType;
				// check that the member does not conflict with an enclosing type
				do {
					if (CharOperation.equals(type.sourceName, memberContext.name)) {
						problemReporter().typeCollidesWithEnclosingType(memberContext);
						continue nextMember;
					}
					type = type.enclosingType();
				} while (type != null);
				// check the member type does not conflict with another sibling member type
				for (int j = 0; j < i; j++) {
					if (CharOperation.equals(this.referenceContext.memberTypes[j].name, memberContext.name)) {
						problemReporter().duplicateNestedType(memberContext);
						continue nextMember;
					}
				}
				ClassScope memberScope = new ClassScope(this, this.referenceContext.memberTypes[i]);
				LocalTypeBinding memberBinding = memberScope.buildLocalType(localType, packageBinding);
				memberBinding.setAsMemberType();
				memberTypeBindings[count++] = memberBinding;
			}
			if (count != size)
				System.arraycopy(memberTypeBindings, 0, memberTypeBindings = new ReferenceBinding[count], 0, count);
		}
		localType.setMemberTypes(memberTypeBindings);
		return localType;
	}

	void buildLocalTypeBinding(SourceTypeBinding enclosingType) {

		LocalTypeBinding localType = buildLocalType(enclosingType, enclosingType.fPackage);
		connectTypeHierarchy();
		connectImplicitPermittedTypes();
		if (compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) {
			checkParameterizedTypeBounds();
			checkParameterizedSuperTypeCollisions();
		}
		buildFieldsAndMethods();
//{ObjectTeams: catchup also in OT-specific process (see class comment concerning local types in Dependencies).
	  if (this.referenceContext.isRole()) {
		RoleModel role = this.referenceContext.getRoleModel();
		Dependencies.ensureRoleState(role, ITranslationStates.STATE_FAULT_IN_TYPES-1);
/*orig*/localType.faultInTypesForFieldsAndMethods();
		Dependencies.ensureRoleState(role, ITranslationStates.STATE_METHODS_VERIFIED-1);
	  } else {
// orig:
		localType.faultInTypesForFieldsAndMethods();
// :giro
		}
// SH}

		this.referenceContext.binding.verifyMethods(environment().methodVerifier());
	}

	private void buildMemberTypes(AccessRestriction accessRestriction) {
	    SourceTypeBinding sourceType = this.referenceContext.binding;
		ReferenceBinding[] memberTypeBindings = Binding.NO_MEMBER_TYPES;
		if (this.referenceContext.memberTypes != null) {
			int length = this.referenceContext.memberTypes.length;
			memberTypeBindings = new ReferenceBinding[length];
			int count = 0;
			nextMember : for (int i = 0; i < length; i++) {
				TypeDeclaration memberContext = this.referenceContext.memberTypes[i];
				if (this.environment().root.isProcessingAnnotations && this.environment().isMissingType(memberContext.name)) {
					throw new SourceTypeCollisionException(); // resolved a type ref before APT generated the type
				}
				switch(TypeDeclaration.kind(memberContext.modifiers)) {
					case TypeDeclaration.INTERFACE_DECL :
					case TypeDeclaration.ANNOTATION_TYPE_DECL :
						if (compilerOptions().sourceLevel >= ClassFileConstants.JDK16)
							break;
						//$FALL-THROUGH$
						if (sourceType.isNestedType()
								&& sourceType.isClass() // no need to check for enum, since implicitly static
//{ObjectTeams: check for team (may indeed contain interface):
								&& !sourceType.isTeam()
// SH}
								&& !sourceType.isStatic()) {
							problemReporter().illegalLocalTypeDeclaration(memberContext);
							continue nextMember;
						}
					break;
				}
				ReferenceBinding type = sourceType;
				// check that the member does not conflict with an enclosing type
				do {
					if (CharOperation.equals(type.sourceName, memberContext.name)) {
						problemReporter().typeCollidesWithEnclosingType(memberContext);
//{ObjectTeams: tagging the type should suffice, keep going (otherwise breaks B.1.1-otjld-sh-15)
						break;
/* orig:
						continue nextMember;
  :giro */
// SH}
					}
					type = type.enclosingType();
				} while (type != null);
				// check that the member type does not conflict with another sibling member type
				for (int j = 0; j < i; j++) {
					if (CharOperation.equals(this.referenceContext.memberTypes[j].name, memberContext.name)) {
						problemReporter().duplicateNestedType(memberContext);
						continue nextMember;
					}
				}

//{ObjectTeams: what kind of scope is needed?
/* orig:
				ClassScope memberScope = new ClassScope(this, memberContext);
  :giro */
				ClassScope memberScope = memberContext.isTeam() ? 	// only for teams, no OTClassScope for inline roles.
					OTClassScope.createMemberOTClassScope(this, memberContext) :
					new ClassScope(this, memberContext);
// SH}
				memberTypeBindings[count++] = memberScope.buildType(sourceType, sourceType.fPackage, accessRestriction);
			}
			if (count != length)
				System.arraycopy(memberTypeBindings, 0, memberTypeBindings = new ReferenceBinding[count], 0, count);
		}
		sourceType.setMemberTypes(memberTypeBindings);
	}

	void buildMethods() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
		if (sourceType.areMethodsInitialized()) return;

		boolean isEnum = TypeDeclaration.kind(this.referenceContext.modifiers) == TypeDeclaration.ENUM_DECL;
		if (this.referenceContext.methods == null && !(isEnum || sourceType.isRecord())) {
			this.referenceContext.binding.setMethods(Binding.NO_METHODS);
			return;
		}

		// iterate the method declarations to create the bindings
		AbstractMethodDeclaration[] methods = this.referenceContext.methods;
		int size = methods == null ? 0 : methods.length;
		// look for <clinit> method
		int clinitIndex = -1;
		for (int i = 0; i < size; i++) {
			if (methods[i].isClinit()) {
				clinitIndex = i;
				break;
			}
		}

		int count = isEnum ? 2 : 0; // reserve 2 slots for special enum methods: #values() and #valueOf(String)
		MethodBinding[] methodBindings = new MethodBinding[(clinitIndex == -1 ? size : size - 1) + count];
		// create special methods for enums
		if (isEnum) {
			methodBindings[0] = sourceType.addSyntheticEnumMethod(TypeConstants.VALUES); // add <EnumType>[] values()
			methodBindings[1] = sourceType.addSyntheticEnumMethod(TypeConstants.VALUEOF); // add <EnumType> valueOf()
		}
		// create bindings for source methods
		boolean hasNativeMethods = false;
		if (sourceType.isAbstract()) {
			for (int i = 0; i < size; i++) {
				if (i != clinitIndex) {
//{ObjectTeams: Twisted scope for methods moved from a role to its team:
/* orig:
					MethodScope scope = new MethodScope(this, methods[i], false);
  :giro */
					MethodScope scope = createMethodScope(methods[i]);
// SH}
					MethodBinding methodBinding = scope.createMethod(methods[i]);
					if (methodBinding != null) { // is null if binding could not be created
						methodBindings[count++] = methodBinding;
						hasNativeMethods = hasNativeMethods || methodBinding.isNative();
					}
				}
			}
		} else {
			boolean hasAbstractMethods = false;
			for (int i = 0; i < size; i++) {
				if (i != clinitIndex) {
//{ObjectTeams: Twisted scope for methods moved from a role to its team:
/* orig:
					MethodScope scope = new MethodScope(this, methods[i], false);
  :giro */
					MethodScope scope = createMethodScope(methods[i]);
// SH}
					MethodBinding methodBinding = scope.createMethod(methods[i]);
					if (methodBinding != null) { // is null if binding could not be created
						methodBindings[count++] = methodBinding;
						hasAbstractMethods = hasAbstractMethods || methodBinding.isAbstract();
						hasNativeMethods = hasNativeMethods || methodBinding.isNative();
						if (methods[i].isCanonicalConstructor()) {
							methodBinding.tagBits |= TagBits.IsCanonicalConstructor;
						}
					}
				}
			}
			if (hasAbstractMethods)
//{ObjectTeams: support rechecking, because callout implementation might remove abstractness:
			{
			  if (sourceType.isDirectRole())
				problemReporter()
					.setRechecker(new IProblemRechecker() {		@Override
					public boolean shouldBeReported(IrritantSet[] foundIrritants) {
						for (MethodBinding methodBinding : ClassScope.this.referenceContext.binding.methods())
							if (methodBinding.isAbstract())
								return true;
						return false; // false alarm
					}})
					.abstractMethodInConcreteClass(sourceType);
			  else
// orig:
				problemReporter().abstractMethodInConcreteClass(sourceType);
// :giro
			}
// SH}
		}
		if (sourceType.isRecord()) {
			assert this.referenceContext.isRecord();
			methodBindings = sourceType.checkAndAddSyntheticRecordMethods(methodBindings, count);
			count = methodBindings.length;
		}
		if (count != methodBindings.length)
			System.arraycopy(methodBindings, 0, methodBindings = new MethodBinding[count], 0, count);
		sourceType.tagBits &= ~(TagBits.AreMethodsSorted|TagBits.AreMethodsComplete); // in case some static imports reached already into this type
		sourceType.setMethods(methodBindings);
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=243917, conservatively tag all methods and fields as
		// being in use if there is a native method in the class.
		if (hasNativeMethods) {
			for (int i = 0; i < methodBindings.length; i++) {
				methodBindings[i].modifiers |= ExtraCompilerModifiers.AccLocallyUsed;
			}
			FieldBinding[] fields = sourceType.unResolvedFields(); // https://bugs.eclipse.org/bugs/show_bug.cgi?id=301683
			for (int i = 0; i < fields.length; i++) {
				fields[i].modifiers |= ExtraCompilerModifiers.AccLocallyUsed;
			}
		}
		if (isEnum && compilerOptions().isAnnotationBasedNullAnalysisEnabled) {
			// mark return types of values & valueOf as nonnull (needed to wait till after setMethods() to avoid reentrance):
			LookupEnvironment environment = this.environment();
			((SyntheticMethodBinding)methodBindings[0]).markNonNull(environment);
			((SyntheticMethodBinding)methodBindings[1]).markNonNull(environment);
		}
	}

//{ObjectTeams: accessible to sub-class:
	protected
// SH}
	SourceTypeBinding buildType(SourceTypeBinding enclosingType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
		// provide the typeDeclaration with needed scopes
		this.referenceContext.scope = this;
		this.referenceContext.staticInitializerScope = new MethodScope(this, this.referenceContext, true);
		this.referenceContext.initializerScope = new MethodScope(this, this.referenceContext, false);

//{ObjectTeams: ROFI:
		boolean isRoleFile = false;
//SH}

		if (enclosingType == null) {
			char[][] className = CharOperation.arrayConcat(packageBinding.compoundName, this.referenceContext.name);
//{ObjectTeams: was assignment; use setter to allow additional setup:
	/* @original
			this.referenceContext.binding = new SourceTypeBinding(className, packageBinding, this);
	 */
			this.referenceContext.setBinding(new SourceTypeBinding(className, packageBinding, this));
// SH}
		} else {
//{ObjectTeams: ROFI:
			isRoleFile = enclosingType.teamPackage != null;
// SH}
			char[][] className = CharOperation.deepCopy(enclosingType.compoundName);
			className[className.length - 1] =
				CharOperation.concat(className[className.length - 1], this.referenceContext.name, '$');
			if (packageBinding.hasType0Any(className[className.length - 1])) {
				// report the error against the parent - its still safe to answer the member type
//{ObjectTeams: role files are handled below:
			  if (!isRoleFile)
//SH}
				this.parent.problemReporter().duplicateNestedType(this.referenceContext);
			}
//{ObjectTeams: was assignment; use setter to allow additional setup:
	/* @original
			this.referenceContext.binding = new MemberTypeBinding(className, this, enclosingType);
	 */
			this.referenceContext.setBinding(new MemberTypeBinding(className, this, enclosingType));
// SH}
		}

		SourceTypeBinding sourceType = this.referenceContext.binding;
		sourceType.module = module();
		environment().setAccessRestriction(sourceType, accessRestriction);
		ICompilationUnit compilationUnit = this.referenceContext.compilationResult.getCompilationUnit();
		if (compilationUnit != null && compilerOptions().isAnnotationBasedNullAnalysisEnabled) {
			String externalAnnotationPath = compilationUnit.getExternalAnnotationPath(CharOperation.toString(sourceType.compoundName));
			if (externalAnnotationPath != null) {
				ExternalAnnotationSuperimposer.apply(sourceType, externalAnnotationPath);
			}
		}

		TypeParameter[] typeParameters = this.referenceContext.typeParameters;
		sourceType.typeVariables = typeParameters == null || typeParameters.length == 0 ? Binding.NO_TYPE_VARIABLES : null;
		sourceType.fPackage.addType(sourceType);
//{ObjectTeams: ROFI
		if (isRoleFile) {
			// role types should be found also in the corresponding team package:
			enclosingType.teamPackage.addType(sourceType);
			// check duplicates:
			if (this.referenceContext.compilationUnit != null) // non-ROFI is analyzed below
			{
				TypeDeclaration[] memberTypes = this.parent.referenceType().memberTypes;
				for (int i = 0; i < memberTypes.length; i++) {
					if (   CharOperation.equals(memberTypes[i].name, this.referenceContext.name)
						&& TypeBinding.notEquals(memberTypes[i].binding, sourceType))
					{
						// mark the existing type (first in list of member types),
						// because ReferenceBinding.getMemberType() prefers elements
						// towards the end of the array, wouldn't want to continue
						// working with the type marked erroneous.

						// report only for interface part, has the nicer name ;-)
						if (this.referenceContext.isInterface())
							// while marking existing type still use this type's positions.
							problemReporter().duplicateNestedType(memberTypes[i], this.referenceContext.sourceStart, this.referenceContext.sourceEnd);
						else
							memberTypes[i].tagAsHavingErrors();
						break;
					}
				}
			}
		}
		if (TypeAnalyzer.isOrgObjectteamsTeam(sourceType)) {// handle the case of compiling org.objectteams.Team up front
			this.referenceContext.adjustOrgObjectteamsTeam();
			this.environment().getTeamMethodGenerator().registerOOTeamClass(sourceType);
		}
// SH}
		checkAndSetModifiers();
		buildTypeVariables();

		buildMemberTypes(accessRestriction);
//{ObjectTeams: setup cache for known role files:
		if (this.referenceContext.isTeam())
			this.referenceContext.getTeamModel().setupRoFiCache(this, environment());
// SH}
		return sourceType;
	}

	private void buildTypeVariables() {

	    SourceTypeBinding sourceType = this.referenceContext.binding;
		TypeParameter[] typeParameters = this.referenceContext.typeParameters;
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=324850, If they exist at all, process type parameters irrespective of source level.
		if (typeParameters == null || typeParameters.length == 0) {
		    sourceType.setTypeVariables(Binding.NO_TYPE_VARIABLES);
		    return;
		}
		sourceType.setTypeVariables(Binding.NO_TYPE_VARIABLES); // safety

		if (sourceType.id == TypeIds.T_JavaLangObject) { // handle the case of redefining java.lang.Object up front
			problemReporter().objectCannotBeGeneric(this.referenceContext);
			return;
		}
//{ObjectTeams: remove type value parameters
		typeParameters = filterTypeValueVariables(typeParameters);
		if (typeParameters.length == 0)
			return; // consumed all parameters
// SH}
		sourceType.setTypeVariables(createTypeVariables(typeParameters, sourceType));
		sourceType.modifiers |= ExtraCompilerModifiers.AccGenericSignature;
	}

//{ObjectTeams: build additional structural elements
	/**
	 * @param typeParameters all type parameters perhaps including type value parameters.
	 * @return remaining unconsumed type parameters.
	 */
	private TypeParameter[] filterTypeValueVariables(TypeParameter[] typeParameters)
	{
		int count = 0;
		TypeParameter[] filteredParams = new TypeParameter[typeParameters.length];
		for (int i = 0; i < typeParameters.length; i++) {
			if (!(typeParameters[i] instanceof TypeValueParameter))
				filteredParams[count++] = typeParameters[i];
		}
		if (count < typeParameters.length) {
			TypeParameter[] remainder = new TypeParameter[count];
			System.arraycopy(filteredParams, 0, remainder, 0, count);
			return remainder;
		}
		return typeParameters;
	}
	/**
	 * build Callin and Callout
	 */
	public void buildCallinCallouts() {
		if (this.referenceContext.callinCallouts == null) {
			this.referenceContext.binding.callinCallouts = Binding.NO_CALLIN_CALLOUT_BINDINGS;
			return;
		}

		if (!this.referenceContext.isSourceRole()) {
			problemReporter().methodMappingNotInBoundRole(this.referenceContext.callinCallouts[0], this.referenceContext);
			this.referenceContext.callinCallouts = null;
			this.referenceContext.binding.callinCallouts = Binding.NO_CALLIN_CALLOUT_BINDINGS;
			return;
		}

		if (this.referenceContext.binding.callinCallouts != null)
			return; // already done

		// iterate the binding declarations to create the bindings
		AbstractMethodMappingDeclaration[] callinCallouts = this.referenceContext.callinCallouts;
		int size = callinCallouts.length;
		CallinCalloutBinding[] bindings = new CallinCalloutBinding[size];

		int count = 0;
		HashSet<String> labels = new HashSet<String>();
		for (int i = 0; i < size; i++) {
			if (this.referenceContext.isInterface() && callinCallouts[i].mappings != null) {
				problemReporter().paramMapInInterface(callinCallouts[i], this.referenceContext.binding);
				continue;
			}

			CallinCalloutScope scope = new CallinCalloutScope(this, callinCallouts[i]);
			CallinCalloutBinding binding = scope.createBinding(callinCallouts[i]);
			if (binding != null) // is null if binding could not be created
				bindings[count++] = binding;
			if (callinCallouts[i].isCallin()) {
				CallinMappingDeclaration callinMapping = (CallinMappingDeclaration)callinCallouts[i];
				if (callinMapping.name[0] != '<') { // generated name?
					String label = String.valueOf(callinMapping.name);
					if (!labels.contains(label))
						labels.add(label);
					else
						scope.problemReporter().duplicateCallinName(callinMapping);
				}
			}
		}
		if (count != bindings.length)
			System.arraycopy(bindings, 0, bindings = new CallinCalloutBinding[count], 0, count);
		this.referenceContext.binding.callinCallouts = bindings;
		this.referenceContext.binding.modifiers |= ExtraCompilerModifiers.AccUnresolved; // until methods() is sent
	}

	public void addGeneratedType (TypeDeclaration memberTypeDeclaration)
	{
		SourceTypeBinding sourceType = this.referenceContext.binding;

		// FIXME(SH): if memberType is a team, don't we need an OTClassScope?
		ClassScope memberScope = new ClassScope(this, memberTypeDeclaration);
		if ((memberTypeDeclaration.bits & ASTNode.IsLocalType) != 0)
		{
			// not added to member types
			memberScope.buildLocalType(sourceType, getCurrentPackage());
			// ensure the copy has the same relative constant pool name (e.g., "1" as in T$__OT__R$1)
			char[] computedConstantPoolName = CharOperation.concatWith(
						new char[][]{sourceType.constantPoolName(), memberTypeDeclaration.name}, '$');
			LocalTypeBinding localTypeBinding = (LocalTypeBinding)memberTypeDeclaration.binding;
			localTypeBinding.setConstantPoolName(computedConstantPoolName);
			referenceCompilationUnit().record(localTypeBinding);
		} else {
			assert (this.referenceContext.memberTypes != null);

			// build the binding:
			SourceTypeBinding newBinding;
			if (memberTypeDeclaration.isRoleFile()) {
				memberScope.environment().internalBuildTypeBindings(memberTypeDeclaration.compilationUnit, null);
				newBinding= memberTypeDeclaration.binding;
			} else {
				newBinding= memberScope.buildType(sourceType, getCurrentPackage(), /*accessRestriction*/null);
			}

			// search for an equal but binary type binding:
			int size = sourceType.memberTypes.length;
			for (int i=0; i<size; i++) {
				ReferenceBinding member = sourceType.memberTypes[i];
				if (   member.isBinaryBinding()
					&& CharOperation.equals(member.compoundName, newBinding.compoundName))
				{
					sourceType.memberTypes[i] = newBinding; // found, simply replace
					return;
				}
			}

			if (!memberTypeDeclaration.isRoleFile()) { // otherwise OTClassScope.buildType() did this.
				// need to actually add:
				ReferenceBinding[] newMembers = new ReferenceBinding[size+1];
				System.arraycopy(sourceType.memberTypes, 0, newMembers, 0, size);
				newMembers[size]= newBinding;
				sourceType.setMemberTypes(newMembers);
			}
		}
	}

	public FieldBinding addGeneratedField(FieldDeclaration fieldDeclaration, boolean hasTypeProblem) {

		SourceTypeBinding sourceType = this.referenceContext.binding;
		if (sourceType.model.getState() >= ITranslationStates.STATE_BYTE_CODE_PREPARED)
		{
			problemReporter().compilationOrderProblem("Too late to add a field", this.referenceContext); //$NON-NLS-1$
			return null;
		}
		// perform initialization according to buildFields()
		FieldBinding fieldBinding = new FieldBinding(
				fieldDeclaration, null, fieldDeclaration.modifiers|ExtraCompilerModifiers.AccUnresolved, sourceType);

		sourceType.addField(fieldBinding);
		checkAndSetModifiersForField(fieldBinding, fieldDeclaration);

// FIXME(SH): does this improve robustness?
//		if ((sourceType.tagBits & TagBits.AreMethodsComplete) != 0)
			// trigger resolveTypeFor(FieldBinding)
		if (!hasTypeProblem)
			sourceType.getField(fieldDeclaration.name, true);
		return fieldBinding;
	}

	// TODO(SH): migrate clients to use SourceTypeBinding.addMethod() directly
    public SourceTypeBinding addGeneratedMethod(MethodBinding methodBinding)
    {
    	SourceTypeBinding sourceType = this.referenceContext.binding;
    	sourceType.addMethod(methodBinding);
    	this.referenceContext.binding.modifiers |= ExtraCompilerModifiers.AccUnresolved; // until methods() is sent
    	return sourceType;
    }

    MethodBinding createMethod(AbstractMethodDeclaration methodDecl) {
    	if (methodDecl.binding != null)
    		return methodDecl.binding; // nothing to do, observed on accessors for callout to private role field
        MethodScope scope = createMethodScope(methodDecl);
        return scope.createMethod(methodDecl);
    }

	private MethodScope createMethodScope(AbstractMethodDeclaration methodDecl) {
		MethodScope scope;
		if (methodDecl.model != null && methodDecl.model._sourceDeclaringType != null) {
        	TypeDeclaration foreignType = methodDecl.model._sourceDeclaringType;
        	scope = new MethodScope(foreignType.scope, methodDecl, false) {
        		@Override
        		public TypeDeclaration referenceType() {
        			return ClassScope.this.referenceContext;
        		}
        		@Override
        		public Object[] getEmulationPath(ReferenceBinding targetEnclosingType, boolean onlyExactMatch, boolean denyEnclosingArgInConstructorCall) {
        			Scope parentSave = this.parent;
        			try {
        				this.parent = ClassScope.this;
        				return super.getEmulationPath(targetEnclosingType, onlyExactMatch,
        					denyEnclosingArgInConstructorCall);
        			} finally {
        				this.parent = parentSave;
        			}
        		}
        	};
        } else {
        	scope = new MethodScope(this, methodDecl, false);
        }
		return scope;
	}
//Markus Witte}

	@Override
	void resolveTypeParameter(TypeParameter typeParameter) {
		typeParameter.resolve(this);
	}

	private void checkAndSetModifiers() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
		int modifiers = sourceType.modifiers;
		boolean isPreviewEnabled = compilerOptions().sourceLevel == ClassFileConstants.getLatestJDKLevel() &&
				compilerOptions().enablePreviewFeatures;
		boolean is16Plus = compilerOptions().sourceLevel >= ClassFileConstants.JDK16;
		boolean flagSealedNonModifiers = isPreviewEnabled &&
				(modifiers & (ExtraCompilerModifiers.AccSealed | ExtraCompilerModifiers.AccNonSealed)) != 0;
		if (sourceType.isRecord()) {
			/* JLS 14 Records Sec 8.10 - A record declaration is implicitly final. */
			modifiers |= ClassFileConstants.AccFinal;
		}
		if ((modifiers & ExtraCompilerModifiers.AccAlternateModifierProblem) != 0)
			problemReporter().duplicateModifierForType(sourceType);
		ReferenceBinding enclosingType = sourceType.enclosingType();
		boolean isMemberType = sourceType.isMemberType();
//{ObjectTeams: OT_CLASS_FLAGS attribute:
		WordValueAttribute.maybeCreateClassFlagsAttribute(this.referenceContext);
// SH}
		if (isMemberType) {
			if (sourceType.hasEnclosingInstanceContext())
				modifiers |= (enclosingType.modifiers & ExtraCompilerModifiers.AccGenericSignature);
			modifiers |= (enclosingType.modifiers & ClassFileConstants.AccStrictfp);
			// checks for member types before local types to catch local members
			if (enclosingType.isInterface())
				modifiers |= ClassFileConstants.AccPublic;
//{ObjectTeams: check for role / team modifiers
			modifiers = Protections.checkRoleModifiers(modifiers, this.referenceContext, this);
// SH}
			if (sourceType.isEnum()) {
				if (!is16Plus && !enclosingType.isStatic())
					problemReporter().nonStaticContextForEnumMemberType(sourceType);
				else
					modifiers |= ClassFileConstants.AccStatic;
			} else if (sourceType.isInterface()) {
				modifiers |= ClassFileConstants.AccStatic; // 8.5.1
			} else if (sourceType.isRecord()) {
				/* JLS 14 Records Sec 8.10 A nested record type is implicitly static */
				modifiers |= ClassFileConstants.AccStatic;
			}
		} else if (sourceType.isLocalType()) {
			if (sourceType.isEnum()) {
				if (!is16Plus) {
					problemReporter().illegalLocalTypeDeclaration(this.referenceContext);
					sourceType.modifiers = 0;
//{ObjectTeams: if inside a role mark as role instead of enum (possible by very broken code only):
					if (enclosingType.isRole())	{
						sourceType.roleModel = this.referenceContext.getRoleModel();
						sourceType.roleModel.setBinding(sourceType);
					}
// SH}
					return;
				}
				final int UNEXPECTED_MODIFIERS =~(ClassFileConstants.AccEnum | ClassFileConstants.AccStrictfp);
				if ((modifiers & ExtraCompilerModifiers.AccJustFlag & UNEXPECTED_MODIFIERS) != 0
						|| flagSealedNonModifiers) {
					problemReporter().illegalModifierForLocalEnumDeclaration(sourceType);
					return;
				}
				modifiers |= ClassFileConstants.AccStatic;
			} else if (sourceType.isRecord()) {
//				if (enclosingType != null && enclosingType.isLocalType()) {
//					problemReporter().illegalLocalTypeDeclaration(this.referenceContext);
//					return;
//				}
				if ((modifiers & ClassFileConstants.AccStatic) != 0) {
					if (!(this.parent instanceof ClassScope))
						problemReporter().recordIllegalStaticModifierForLocalClassOrInterface(sourceType);
					return;
				}
				modifiers |= ClassFileConstants.AccStatic;
			}
			if (sourceType.isAnonymousType()) {
				if (compilerOptions().complianceLevel < ClassFileConstants.JDK9)
					modifiers |= ClassFileConstants.AccFinal;
			    // set AccEnum flag for anonymous body of enum constants
			    if (this.referenceContext.allocation.type == null)
			    	modifiers |= ClassFileConstants.AccEnum;
			} else if (this.parent.referenceContext() instanceof TypeDeclaration) {
				TypeDeclaration typeDecl = (TypeDeclaration) this.parent.referenceContext();
				if (TypeDeclaration.kind(typeDecl.modifiers) == TypeDeclaration.INTERFACE_DECL) {
					// Sec 8.1.3 applies for local types as well
					modifiers |= ClassFileConstants.AccStatic;
				}
			}
			Scope scope = this;
			do {
				switch (scope.kind) {
					case METHOD_SCOPE :
						MethodScope methodScope = (MethodScope) scope;
						if (methodScope.isLambdaScope())
							methodScope = methodScope.namedMethodScope();
						if (methodScope.isInsideInitializer()) {
							SourceTypeBinding type = ((TypeDeclaration) methodScope.referenceContext).binding;

							// inside field declaration ? check field modifier to see if deprecated
							if (methodScope.initializedField != null) {
									// currently inside this field initialization
								if (methodScope.initializedField.isViewedAsDeprecated() && !sourceType.isDeprecated())
									modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
							} else {
								if (type.isStrictfp())
									modifiers |= ClassFileConstants.AccStrictfp;
								if (type.isViewedAsDeprecated() && !sourceType.isDeprecated())
									modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
							}
						} else {
							MethodBinding method = ((AbstractMethodDeclaration) methodScope.referenceContext).binding;
							if (method != null) {
								if (method.isStrictfp())
									modifiers |= ClassFileConstants.AccStrictfp;
								if (method.isViewedAsDeprecated() && !sourceType.isDeprecated())
									modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
							}
						}
						break;
					case CLASS_SCOPE :
						// local member
						if (enclosingType.isStrictfp())
							modifiers |= ClassFileConstants.AccStrictfp;
						if (enclosingType.isViewedAsDeprecated() && !sourceType.isDeprecated()) {
							modifiers |= ExtraCompilerModifiers.AccDeprecatedImplicitly;
							sourceType.tagBits |= enclosingType.tagBits & TagBits.AnnotationTerminallyDeprecated;
						}
						break;
				}
				scope = scope.parent;
			} while (scope != null);
		}

		// after this point, tests on the 16 bits reserved.
//{ObjectTeams: also include _some_ extra modifiers like AccTeam / AccRole:
/* orig:
		int realModifiers = modifiers & ExtraCompilerModifiers.AccJustFlag;
  :giro */
		int realModifiers = modifiers & ExtraCompilerModifiers.AccOTTypeJustFlag;
// SH}

		if ((realModifiers & ClassFileConstants.AccInterface) != 0) { // interface and annotation type
			// detect abnormal cases for interfaces
			if (isMemberType) {
				final int UNEXPECTED_MODIFIERS =
					~(ClassFileConstants.AccPublic | ClassFileConstants.AccPrivate | ClassFileConstants.AccProtected | ClassFileConstants.AccStatic | ClassFileConstants.AccAbstract | ClassFileConstants.AccInterface | ClassFileConstants.AccStrictfp | ClassFileConstants.AccAnnotation
//{ObjectTeams more flags allowed for interfaces:
					  | ExtraCompilerModifiers.AccRole | ClassFileConstants.AccSynthetic
// SH}
					 );
				if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
					if ((realModifiers & ClassFileConstants.AccAnnotation) != 0)
						problemReporter().illegalModifierForAnnotationMemberType(sourceType);
					else
						problemReporter().illegalModifierForMemberInterface(sourceType);
//{ObjectTeams: prevent downstream problems with types illegally marked as team:
					modifiers &= ~ExtraCompilerModifiers.AccTeam;
					this.referenceContext.modifiers &= ~ExtraCompilerModifiers.AccTeam;
// SH}
				}
				/*
				} else if (sourceType.isLocalType()) { //interfaces cannot be defined inside a method
					int unexpectedModifiers = ~(AccAbstract | AccInterface | AccStrictfp);
					if ((realModifiers & unexpectedModifiers) != 0)
						problemReporter().illegalModifierForLocalInterface(sourceType);
				*/
			} else 	if (sourceType.isLocalType()) {
				final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccAbstract | ClassFileConstants.AccInterface
						| ClassFileConstants.AccStrictfp | ClassFileConstants.AccAnnotation
						| ((is16Plus && this.parent instanceof ClassScope) ? ClassFileConstants.AccStatic : 0));
				if ((realModifiers & UNEXPECTED_MODIFIERS) != 0
						|| (isPreviewEnabled && flagSealedNonModifiers))
					problemReporter().localStaticsIllegalVisibilityModifierForInterfaceLocalType(sourceType);
//				if ((modifiers & ClassFileConstants.AccStatic) != 0) {
//					problemReporter().recordIllegalStaticModifierForLocalClassOrInterface(sourceType);
//				}
				modifiers |= ClassFileConstants.AccStatic;
			} else {
				final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccAbstract | ClassFileConstants.AccInterface | ClassFileConstants.AccStrictfp | ClassFileConstants.AccAnnotation
//{ObjectTeams
										   | ClassFileConstants.AccSynthetic
										   );
//SH}
				if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
					if ((realModifiers & ClassFileConstants.AccAnnotation) != 0)
						problemReporter().illegalModifierForAnnotationType(sourceType);
					else
						problemReporter().illegalModifierForInterface(sourceType);
				}
			}
			/*
			 * AccSynthetic must be set if the target is greater than 1.5. 1.5 VM don't support AccSynthetics flag.
			 */
			if (sourceType.sourceName == TypeConstants.PACKAGE_INFO_NAME && compilerOptions().targetJDK > ClassFileConstants.JDK1_5) {
				modifiers |= ClassFileConstants.AccSynthetic;
			}
			modifiers |= ClassFileConstants.AccAbstract;
			} else if ((realModifiers & ClassFileConstants.AccEnum) != 0) {
			// detect abnormal cases for enums
			if (isMemberType) { // includes member types defined inside local types
				final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccPrivate | ClassFileConstants.AccProtected | ClassFileConstants.AccStatic | ClassFileConstants.AccStrictfp | ClassFileConstants.AccEnum
//{ObjectTeams more flags allowed for types:
					                      | ExtraCompilerModifiers.AccRole | ExtraCompilerModifiers.AccTeam | ClassFileConstants.AccSynthetic
										  );
// Markus Witte}
				if ((realModifiers & UNEXPECTED_MODIFIERS) != 0 || flagSealedNonModifiers) {
					problemReporter().illegalModifierForMemberEnum(sourceType);
					modifiers &= ~ClassFileConstants.AccAbstract; // avoid leaking abstract modifier
					realModifiers &= ~ClassFileConstants.AccAbstract;
//					modifiers &= ~(realModifiers & UNEXPECTED_MODIFIERS);
//					realModifiers = modifiers & ExtraCompilerModifiers.AccJustFlag;
				}
			} else if (sourceType.isLocalType()) {
//				if (flagSealedNonModifiers)
//					problemReporter().illegalModifierForLocalEnum(sourceType);
				// each enum constant is an anonymous local type and its modifiers were already checked as an enum constant field
			} else {
				int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccStrictfp | ClassFileConstants.AccEnum
//{ObjectTeams more flags allowed for types
								           | ExtraCompilerModifiers.AccTeam | ExtraCompilerModifiers.AccRole);
				if ((realModifiers & ExtraCompilerModifiers.AccRole) != 0)
					UNEXPECTED_MODIFIERS ^= ClassFileConstants.AccProtected; // even toplevel roles may be protected.
// Markus Witte+SH}
				if ((realModifiers & UNEXPECTED_MODIFIERS) != 0 || flagSealedNonModifiers)
					problemReporter().illegalModifierForEnum(sourceType);
			}
			if (!sourceType.isAnonymousType()) {
				checkAbstractEnum: {
					// does define abstract methods ?
					if ((this.referenceContext.bits & ASTNode.HasAbstractMethods) != 0) {
						modifiers |= ClassFileConstants.AccAbstract;
						break checkAbstractEnum;
					}
					// body of enum constant must implement any inherited abstract methods
					// enum type needs to implement abstract methods if one of its constants does not supply a body
					TypeDeclaration typeDeclaration = this.referenceContext;
					FieldDeclaration[] fields = typeDeclaration.fields;
					int fieldsLength = fields == null ? 0 : fields.length;
					if (fieldsLength == 0) break checkAbstractEnum; // has no constants so must implement the method itself
					AbstractMethodDeclaration[] methods = typeDeclaration.methods;
					int methodsLength = methods == null ? 0 : methods.length;
					// TODO (kent) cannot tell that the superinterfaces are empty or that their methods are implemented
					boolean definesAbstractMethod = typeDeclaration.superInterfaces != null;
					for (int i = 0; i < methodsLength && !definesAbstractMethod; i++)
						definesAbstractMethod = methods[i].isAbstract();
					if (!definesAbstractMethod) break checkAbstractEnum; // all methods have bodies
					boolean needAbstractBit = false;
					for (int i = 0; i < fieldsLength; i++) {
						FieldDeclaration fieldDecl = fields[i];
						if (fieldDecl.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT) {
							if (fieldDecl.initialization instanceof QualifiedAllocationExpression) {
								needAbstractBit = true;
							} else {
								break checkAbstractEnum;
							}
						}
					}
					// tag this enum as abstract since an abstract method must be implemented AND all enum constants define an anonymous body
					// as a result, each of its anonymous constants will see it as abstract and must implement each inherited abstract method
					if (needAbstractBit) {
						modifiers |= ClassFileConstants.AccAbstract;
					}
				}
				// final if no enum constant with anonymous body
				checkFinalEnum: {
					TypeDeclaration typeDeclaration = this.referenceContext;
					FieldDeclaration[] fields = typeDeclaration.fields;
					if (fields != null) {
						for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) {
							FieldDeclaration fieldDecl = fields[i];
							if (fieldDecl.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT) {
								if (fieldDecl.initialization instanceof QualifiedAllocationExpression) {
									break checkFinalEnum;
								}
							}
						}
					}
					modifiers |= ClassFileConstants.AccFinal;
				}
				if (isPreviewEnabled && (modifiers & ClassFileConstants.AccFinal) == 0)
					modifiers |= ExtraCompilerModifiers.AccSealed;
			}
		} else if (sourceType.isRecord()) {
			int UNEXPECTED_MODIFIERS = ExtraCompilerModifiers.AccNonSealed | ExtraCompilerModifiers.AccSealed;
			if (isMemberType) {
				final int EXPECTED_MODIFIERS = (ClassFileConstants.AccPublic | ClassFileConstants.AccPrivate | ClassFileConstants.AccProtected | ClassFileConstants.AccStatic | ClassFileConstants.AccFinal | ClassFileConstants.AccStrictfp);
				if ((realModifiers & ~EXPECTED_MODIFIERS) != 0 || (modifiers & UNEXPECTED_MODIFIERS) != 0)
					problemReporter().illegalModifierForInnerRecord(sourceType);
			} else if (sourceType.isLocalType()) {
				final int EXPECTED_MODIFIERS = (ClassFileConstants.AccFinal | ClassFileConstants.AccStrictfp | ClassFileConstants.AccStatic);
				if ((realModifiers & ~EXPECTED_MODIFIERS) != 0 || (modifiers & UNEXPECTED_MODIFIERS) != 0)
					problemReporter().illegalModifierForLocalRecord(sourceType);
			} else {
				final int EXPECTED_MODIFIERS = (ClassFileConstants.AccPublic |  ClassFileConstants.AccFinal | ClassFileConstants.AccStrictfp);
				if ((realModifiers & ~EXPECTED_MODIFIERS) != 0 || (modifiers & UNEXPECTED_MODIFIERS) != 0)
					problemReporter().illegalModifierForRecord(sourceType);
			}
			// JLS 14 8.10 : It is a compile-time error if a record declaration has the modifier abstract.

			/* Section 8.10 http://cr.openjdk.java.net/~gbierman/8222777/8222777-20190823/specs/records-jls.html#jls-8.10
			 * It is a compile-time error if a record declaration has the modifier abstract.
			 *
			 * A record declaration is implicitly final. It is permitted for the declaration of a record type
			 * to redundantly specify the final modifier.
			 *
			 * A nested record type is implicitly static. It is permitted for the declaration of a nested record
			 * type to redundantly specify the static modifier.
			 *
			 * This implies that it is impossible to declare a record type in the body of an inner class (8.1.3),
			 * because an inner class cannot have static members except for constant variables.
			 *
			 * It is a compile-time error if the same keyword appears more than once as a modifier for a record declaration,
			 * or if a record declaration has more than one of the access modifiers public, protected, and private (6.6).
			 */
		} else {
			// detect abnormal cases for classes
			if (isMemberType) { // includes member types defined inside local types
				final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccPrivate | ClassFileConstants.AccProtected | ClassFileConstants.AccStatic | ClassFileConstants.AccAbstract | ClassFileConstants.AccFinal | ClassFileConstants.AccStrictfp
//{ObjectTeams more flags allowed for types:
										    | ExtraCompilerModifiers.AccRole | ExtraCompilerModifiers.AccTeam | ClassFileConstants.AccSynthetic
					                        );
//SH}
				if ((realModifiers & UNEXPECTED_MODIFIERS) != 0)
					problemReporter().illegalModifierForMemberClass(sourceType);
			} else if (sourceType.isLocalType()) {
				final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccAbstract | ClassFileConstants.AccFinal | ClassFileConstants.AccStrictfp
						| ((is16Plus && this.parent instanceof ClassScope) ? ClassFileConstants.AccStatic : 0)
//{ObjectTeams more flags allowed for types:
											| ExtraCompilerModifiers.AccRole
											);
//SH}
				if ((realModifiers & UNEXPECTED_MODIFIERS) != 0 || flagSealedNonModifiers)
					problemReporter().illegalModifierForLocalClass(sourceType);
			} else {
				final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccAbstract | ClassFileConstants.AccFinal | ClassFileConstants.AccStrictfp
//{ObjectTeams more flags allowed for types:
											| ExtraCompilerModifiers.AccRole | ExtraCompilerModifiers.AccTeam | ClassFileConstants.AccSynthetic
											);
// SH}

				if ((realModifiers & UNEXPECTED_MODIFIERS) != 0)
					problemReporter().illegalModifierForClass(sourceType);
			}

			// check that Final and Abstract are not set together
			if ((realModifiers & (ClassFileConstants.AccFinal | ClassFileConstants.AccAbstract)) == (ClassFileConstants.AccFinal | ClassFileConstants.AccAbstract))
				problemReporter().illegalModifierCombinationFinalAbstractForClass(sourceType);
		}

		if (isMemberType) {
			// test visibility modifiers inconsistency, isolate the accessors bits
			if (enclosingType.isInterface()) {
				if ((realModifiers & (ClassFileConstants.AccProtected | ClassFileConstants.AccPrivate)) != 0) {
					problemReporter().illegalVisibilityModifierForInterfaceMemberType(sourceType);

					// need to keep the less restrictive
					if ((realModifiers & ClassFileConstants.AccProtected) != 0)
						modifiers &= ~ClassFileConstants.AccProtected;
					if ((realModifiers & ClassFileConstants.AccPrivate) != 0)
						modifiers &= ~ClassFileConstants.AccPrivate;
				}
			} else {
				int accessorBits = realModifiers & (ClassFileConstants.AccPublic | ClassFileConstants.AccProtected | ClassFileConstants.AccPrivate);
				if ((accessorBits & (accessorBits - 1)) > 1) {
					problemReporter().illegalVisibilityModifierCombinationForMemberType(sourceType);

					// need to keep the less restrictive so disable Protected/Private as necessary
					if ((accessorBits & ClassFileConstants.AccPublic) != 0) {
						if ((accessorBits & ClassFileConstants.AccProtected) != 0)
							modifiers &= ~ClassFileConstants.AccProtected;
						if ((accessorBits & ClassFileConstants.AccPrivate) != 0)
							modifiers &= ~ClassFileConstants.AccPrivate;
					} else if ((accessorBits & ClassFileConstants.AccProtected) != 0 && (accessorBits & ClassFileConstants.AccPrivate) != 0) {
						modifiers &= ~ClassFileConstants.AccPrivate;
					}
				}
			}

			// static modifier test
			if ((realModifiers & ClassFileConstants.AccStatic) == 0) {
				if (enclosingType.isInterface())
					modifiers |= ClassFileConstants.AccStatic;
			} else if (!enclosingType.isStatic()) {
//{ObjectTeams: member-interfaces are always (implicitly) static,
			  // role interfaces need to be members (at any level of nesting)!
			  if (!sourceType.isRole()) {
// SH}
//				if (sourceType.isRecord())
//					problemReporter().recordNestedRecordInherentlyStatic(sourceType);
//				else
					if (!is16Plus)
					// error the enclosing type of a static field must be static or a top-level type
					problemReporter().illegalStaticModifierForMemberType(sourceType);
/* OT: */	  }
			}
		}

		sourceType.modifiers = modifiers;
	}

	/* This method checks the modifiers of a field.
	*
	* 9.3 & 8.3
	* Need to integrate the check for the final modifiers for nested types
	*
	* Note : A scope is accessible by : fieldBinding.declaringClass.scope
	*/
	private void checkAndSetModifiersForField(FieldBinding fieldBinding, FieldDeclaration fieldDecl) {
//{ObjectTeams: share model if set:
		if (fieldDecl.model  != null)
			fieldDecl.model.setBinding(fieldBinding);
// SH}
		int modifiers = fieldBinding.modifiers;
		final ReferenceBinding declaringClass = fieldBinding.declaringClass;
		if ((modifiers & ExtraCompilerModifiers.AccAlternateModifierProblem) != 0)
			problemReporter().duplicateModifierForField(declaringClass, fieldDecl);

//{ObjectTeams: synthetic role interfaces may have any access modifier.
	/*@original
		if (declaringClass.isInterface()) {
	 */
		if (declaringClass.isRegularInterface()) {
// SH}
			final int IMPLICIT_MODIFIERS = ClassFileConstants.AccPublic | ClassFileConstants.AccStatic | ClassFileConstants.AccFinal;
			// set the modifiers
			modifiers |= IMPLICIT_MODIFIERS;

			// and then check that they are the only ones
			if ((modifiers & ExtraCompilerModifiers.AccJustFlag) != IMPLICIT_MODIFIERS) {
				if ((declaringClass.modifiers  & ClassFileConstants.AccAnnotation) != 0)
					problemReporter().illegalModifierForAnnotationField(fieldDecl);
				else
					problemReporter().illegalModifierForInterfaceField(fieldDecl);
			}
			fieldBinding.modifiers = modifiers;
			return;
		} else if (fieldDecl.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT) {
			// check that they are not modifiers in source
			if ((modifiers & ExtraCompilerModifiers.AccJustFlag) != 0)
				problemReporter().illegalModifierForEnumConstant(declaringClass, fieldDecl);

			// set the modifiers
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=267670. Force all enumerators to be marked
			// as used locally. We are unable to track the usage of these reliably as they could be used
			// in non obvious ways via the synthesized methods values() and valueOf(String) or by using
			// Enum.valueOf(Class<T>, String).
			final int IMPLICIT_MODIFIERS = ClassFileConstants.AccPublic | ClassFileConstants.AccStatic | ClassFileConstants.AccFinal | ClassFileConstants.AccEnum | ExtraCompilerModifiers.AccLocallyUsed;
			fieldBinding.modifiers|= IMPLICIT_MODIFIERS;
			return;
		}

		// after this point, tests on the 16 bits reserved.
		int realModifiers = modifiers & ExtraCompilerModifiers.AccJustFlag;
		final int UNEXPECTED_MODIFIERS = ~(ClassFileConstants.AccPublic | ClassFileConstants.AccPrivate | ClassFileConstants.AccProtected | ClassFileConstants.AccFinal | ClassFileConstants.AccStatic | ClassFileConstants.AccTransient | ClassFileConstants.AccVolatile
//{ObjectTeams: synthetic fields
									| ClassFileConstants.AccSynthetic);
//MacWitte}
		if ((realModifiers & UNEXPECTED_MODIFIERS) != 0) {
			problemReporter().illegalModifierForField(declaringClass, fieldDecl);
			modifiers &= ~ExtraCompilerModifiers.AccJustFlag | ~UNEXPECTED_MODIFIERS;
		}

		int accessorBits = realModifiers & (ClassFileConstants.AccPublic | ClassFileConstants.AccProtected | ClassFileConstants.AccPrivate);
		if ((accessorBits & (accessorBits - 1)) > 1) {
			problemReporter().illegalVisibilityModifierCombinationForField(declaringClass, fieldDecl);

			// need to keep the less restrictive so disable Protected/Private as necessary
			if ((accessorBits & ClassFileConstants.AccPublic) != 0) {
				if ((accessorBits & ClassFileConstants.AccProtected) != 0)
					modifiers &= ~ClassFileConstants.AccProtected;
				if ((accessorBits & ClassFileConstants.AccPrivate) != 0)
					modifiers &= ~ClassFileConstants.AccPrivate;
			} else if ((accessorBits & ClassFileConstants.AccProtected) != 0 && (accessorBits & ClassFileConstants.AccPrivate) != 0) {
				modifiers &= ~ClassFileConstants.AccPrivate;
			}
		}
//{ObjectTeams: static role fields must be final (limitation):
		if (declaringClass.isRole() && ((modifiers & ClassFileConstants.AccStatic) != 0)) {
			if ((modifiers & ClassFileConstants.AccFinal) == 0) {
				problemReporter().staticRoleFieldMustBeFinal(fieldDecl);
			}
		}
// SH}

		if ((realModifiers & (ClassFileConstants.AccFinal | ClassFileConstants.AccVolatile)) == (ClassFileConstants.AccFinal | ClassFileConstants.AccVolatile))
			problemReporter().illegalModifierCombinationFinalVolatileForField(declaringClass, fieldDecl);

		if (fieldDecl.initialization == null && (modifiers & ClassFileConstants.AccFinal) != 0)
			modifiers |= ExtraCompilerModifiers.AccBlankFinal;
		fieldBinding.modifiers = modifiers;
	}

	public void checkParameterizedSuperTypeCollisions() {
		// check for parameterized interface collisions (when different parameterizations occur)
		SourceTypeBinding sourceType = this.referenceContext.binding;
		ReferenceBinding[] interfaces = sourceType.superInterfaces;
		Map invocations = new HashMap(2);
		ReferenceBinding itsSuperclass = sourceType.isInterface() ? null : sourceType.superclass;
		nextInterface: for (int i = 0, length = interfaces.length; i < length; i++) {
			ReferenceBinding one =  interfaces[i];
			if (one == null) continue nextInterface;
//{ObjectTeams: confined have no superclass/superinterface
			if (TypeAnalyzer.isTopConfined(one))
				continue nextInterface;
//SH}
			if (itsSuperclass != null && hasErasedCandidatesCollisions(itsSuperclass, one, invocations, sourceType, this.referenceContext))
				continue nextInterface;
			nextOtherInterface: for (int j = 0; j < i; j++) {
				ReferenceBinding two = interfaces[j];
				if (two == null) continue nextOtherInterface;
				if (hasErasedCandidatesCollisions(one, two, invocations, sourceType, this.referenceContext))
					continue nextInterface;
			}
		}

		TypeParameter[] typeParameters = this.referenceContext.typeParameters;
		nextVariable : for (int i = 0, paramLength = typeParameters == null ? 0 : typeParameters.length; i < paramLength; i++) {
			TypeParameter typeParameter = typeParameters[i];
			TypeVariableBinding typeVariable = typeParameter.binding;
			if (typeVariable == null || !typeVariable.isValidBinding()) continue nextVariable;

			TypeReference[] boundRefs = typeParameter.bounds;
			if (boundRefs != null) {
				boolean checkSuperclass = TypeBinding.equalsEquals(typeVariable.firstBound, typeVariable.superclass);
				for (int j = 0, boundLength = boundRefs.length; j < boundLength; j++) {
					TypeReference typeRef = boundRefs[j];
					TypeBinding superType = typeRef.resolvedType;
					if (superType == null || !superType.isValidBinding()) continue;

					// check against superclass
					if (checkSuperclass)
						if (hasErasedCandidatesCollisions(superType, typeVariable.superclass, invocations, typeVariable, typeRef))
							continue nextVariable;
					// check against superinterfaces
					for (int index = typeVariable.superInterfaces.length; --index >= 0;)
						if (hasErasedCandidatesCollisions(superType, typeVariable.superInterfaces[index], invocations, typeVariable, typeRef))
							continue nextVariable;
				}
			}
		}

		ReferenceBinding[] memberTypes = this.referenceContext.binding.memberTypes;
		if (memberTypes != null && memberTypes != Binding.NO_MEMBER_TYPES)
			for (int i = 0, size = memberTypes.length; i < size; i++)
//{ObjectTeams: consider binary role files, and fully translated ones:
			   if (   !memberTypes[i].isBinaryBinding()
				   && !StateHelper.hasState(memberTypes[i], ITranslationStates.STATE_FINAL))
// SH}
				 ((SourceTypeBinding) memberTypes[i]).scope.checkParameterizedSuperTypeCollisions();
	}

	private void checkForInheritedMemberTypes(SourceTypeBinding sourceType) {
		// search up the hierarchy of the sourceType to see if any superType defines a member type
		// when no member types are defined, tag the sourceType & each superType with the HasNoMemberTypes bit
		// assumes super types have already been checked & tagged
		ReferenceBinding currentType = sourceType;
		ReferenceBinding[] interfacesToVisit = null;
		int nextPosition = 0;
		do {
			if (currentType.hasMemberTypes()) // avoid resolving member types eagerly
				return;

			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			// in code assist cases when source types are added late, may not be finished connecting hierarchy
			if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
				if (interfacesToVisit == null) {
					interfacesToVisit = itsInterfaces;
					nextPosition = interfacesToVisit.length;
				} else {
					int itsLength = itsInterfaces.length;
					if (nextPosition + itsLength >= interfacesToVisit.length)
						System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
					nextInterface : for (int a = 0; a < itsLength; a++) {
						ReferenceBinding next = itsInterfaces[a];
						for (int b = 0; b < nextPosition; b++)
							if (TypeBinding.equalsEquals(next, interfacesToVisit[b])) continue nextInterface;
						interfacesToVisit[nextPosition++] = next;
					}
				}
			}
		} while ((currentType = currentType.superclass()) != null && (currentType.tagBits & TagBits.HasNoMemberTypes) == 0);

		if (interfacesToVisit != null) {
			// contains the interfaces between the sourceType and any superclass, which was tagged as having no member types
			boolean needToTag = false;
			for (int i = 0; i < nextPosition; i++) {
				ReferenceBinding anInterface = interfacesToVisit[i];
				if ((anInterface.tagBits & TagBits.HasNoMemberTypes) == 0) { // skip interface if it already knows it has no member types
					if (anInterface.hasMemberTypes()) // avoid resolving member types eagerly
						return;

					needToTag = true;
					ReferenceBinding[] itsInterfaces = anInterface.superInterfaces();
					if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
						int itsLength = itsInterfaces.length;
						if (nextPosition + itsLength >= interfacesToVisit.length)
							System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
						nextInterface : for (int a = 0; a < itsLength; a++) {
							ReferenceBinding next = itsInterfaces[a];
							for (int b = 0; b < nextPosition; b++)
								if (TypeBinding.equalsEquals(next, interfacesToVisit[b])) continue nextInterface;
							interfacesToVisit[nextPosition++] = next;
						}
					}
				}
			}

			if (needToTag) {
				for (int i = 0; i < nextPosition; i++)
					interfacesToVisit[i].tagBits |= TagBits.HasNoMemberTypes;
			}
		}

		// tag the sourceType and all of its superclasses, unless they have already been tagged
		currentType = sourceType;
		do {
			currentType.tagBits |= TagBits.HasNoMemberTypes;
		} while ((currentType = currentType.superclass()) != null && (currentType.tagBits & TagBits.HasNoMemberTypes) == 0);
	}

	// Perform deferred bound checks for parameterized type references (only done after hierarchy is connected)
	public void  checkParameterizedTypeBounds() {
//{ObjectTeams: expect re-entry:
/* orig:
		for (int i = 0, l = this.deferredBoundChecks == null ? 0 : this.deferredBoundChecks.size(); i < l; i++) {
			Object toCheck = this.deferredBoundChecks.get(i);
			if (toCheck instanceof TypeReference)
				((TypeReference) toCheck).checkBounds(this);
			else if (toCheck instanceof Runnable)
				((Runnable) toCheck).run();
		}
		this.deferredBoundChecks = null;
  :giro */
		ArrayList toCheckList = this.deferredBoundChecks;
		this.deferredBoundChecks = null;
		for (int i = 0, l = toCheckList == null ? 0 : toCheckList.size(); i < l; i++) {
			Object toCheck = toCheckList.get(i);
			if (toCheck instanceof TypeReference)
				((TypeReference) toCheck).checkBounds(this);
			else if (toCheck instanceof Runnable)
				((Runnable) toCheck).run();
		}
// SH}

		ReferenceBinding[] memberTypes = this.referenceContext.binding.memberTypes;
		if (memberTypes != null && memberTypes != Binding.NO_MEMBER_TYPES)
			for (int i = 0, size = memberTypes.length; i < size; i++)
//{ObjectTeams: binary roles need not be checked again (hopefully) (nor do completely translated ones)
			   if (   !memberTypes[i].isBinaryBinding()
				   && !StateHelper.hasState(memberTypes[i], ITranslationStates.STATE_FINAL))
// SH}
				 ((SourceTypeBinding) memberTypes[i]).scope.checkParameterizedTypeBounds();
	}

	private void connectMemberTypes() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
//{ObjectTeams: extracted treatment of teams to new method:
		if (this.referenceContext.isTeam()) {
			connectTeamMemberTypes();
			return;
		}
// SH}
		ReferenceBinding[] memberTypes = sourceType.memberTypes;
		if (memberTypes != null && memberTypes != Binding.NO_MEMBER_TYPES) {
			for (int i = 0, size = memberTypes.length; i < size; i++)
//{ObjectTeams: respect reused binary members:
			  if (!memberTypes[i].isBinaryBinding())
// SH}
				 ((SourceTypeBinding) memberTypes[i]).scope.connectTypeHierarchy();
		}
	}
//{ObjectTeams: different order, more to do, for members of a team:
	// @PRE: current is a team.
	private void connectTeamMemberTypes() {

		// 1. shallow connecting of roles:
		SourceTypeBinding sourceType = this.referenceContext.binding;
		// don't cache memberTypes/size, loop might introduce new role files
		if (sourceType.memberTypes != null && sourceType.memberTypes != Binding.NO_MEMBER_TYPES) {
			for (int i = 0; i < sourceType.memberTypes.length; i++) {
				ReferenceBinding memberType = sourceType.memberTypes[i];
				// cope with binary role in source type team and pre-translated roles
				if (   !memberType.isBinaryBinding()
					&& !StateHelper.hasState(memberType, ITranslationStates.STATE_FINAL))
				{
					((SourceTypeBinding) memberType).scope.connectTypeHierarchyWithoutMembers();
				}
			}
		}

		// 2. copy roles and create OT-specific connections:
		if (   !isOrgObjectteamsTeam(sourceType)
			&& (sourceType.tagBits & TagBits.HierarchyHasProblems) == 0)
		{
			CopyInheritance.copyRoles(sourceType);
		}

		// 3. descend into roles:
		if (sourceType.memberTypes != null && sourceType.memberTypes != Binding.NO_MEMBER_TYPES) {
			Sorting.sortMemberTypes(sourceType);
			for (int i = 0; i < sourceType.memberTypes.length; i++) {
				ReferenceBinding memberType = sourceType.memberTypes[i];
				// cope with binary role in source type team and pre-translated roles
				if (   !memberType.isBinaryBinding()
					&& !StateHelper.hasState(memberType, ITranslationStates.STATE_FINAL))
				{
					((SourceTypeBinding) memberType).scope.connectMemberTypes();
				}
			}
		}
	}
// SH}

	/*
		Our current belief based on available JCK tests is:
			inherited member types are visible as a potential superclass.
			inherited interfaces are not visible when defining a superinterface.

		Error recovery story:
			ensure the superclass is set to java.lang.Object if a problem is detected
			resolving the superclass.

		Answer false if an error was reported against the sourceType.
	*/
	private boolean connectSuperclass() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
//{ObjectTeams: propagate hierarchy problems:
		if (sourceType.isRole() && ((sourceType.enclosingType().tagBits & TagBits.HierarchyHasProblems) != 0))
			sourceType.tagBits |= TagBits.HierarchyHasProblems;
		// separate treatment for team classes:
		if (sourceType.isTeam())
			return connectSuperteam();
// SH}
		if (sourceType.id == TypeIds.T_JavaLangObject) { // handle the case of redefining java.lang.Object up front
			sourceType.setSuperClass(null);
			sourceType.setSuperInterfaces(Binding.NO_SUPERINTERFACES);
			sourceType.setPermittedTypes(Binding.NO_PERMITTEDTYPES);
			if (!sourceType.isClass())
				problemReporter().objectMustBeClass(sourceType);
			if (this.referenceContext.superclass != null || (this.referenceContext.superInterfaces != null && this.referenceContext.superInterfaces.length > 0))
				problemReporter().objectCannotHaveSuperTypes(sourceType);
			return true; // do not propagate Object's hierarchy problems down to every subtype
		}
		if (this.referenceContext.superclass == null) {
//{ObjectTeams: Confined roles may have no superclass
			if (TypeAnalyzer.isTopConfined(sourceType))
				return true;
// SH}
			if (sourceType.isEnum() && compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) // do not connect if source < 1.5 as enum already got flagged as syntax error
				return connectEnumSuperclass();
			sourceType.setSuperClass(getJavaLangObject());
			return !detectHierarchyCycle(sourceType, sourceType.superclass, null);
		}
		TypeReference superclassRef = this.referenceContext.superclass;
//{ObjectTeams: option to roll back problems:
		CheckPoint cp = this.referenceContext.compilationResult.getCheckPoint(this.referenceContext);
// SH}
		ReferenceBinding superclass = findSupertype(superclassRef);
//{ObjectTeams: for roles give a second chance
	    if (superclass == null || superclass.erasure() instanceof MissingTypeBinding) { // problem binding is in superclassRef.resolvedType
            ReferenceBinding adjustedSuperclass = checkAdjustSuperclass(this.referenceContext.superclass, cp);
            if (adjustedSuperclass == null || adjustedSuperclass.isValidBinding())
            	superclass = adjustedSuperclass;
	    }
// SH}
		if (superclass != null) { // is null if a cycle was detected cycle or a problem
//{ObjectTeams: additional check:
   			if (!Protections.checkCompatibleEnclosingForRoles(
   	                this,
   	                this.referenceContext,
   	                this.referenceContext.binding,
   	                superclass)) {
   				// nop, already reported inside
   			} else
// SH}
			if (!superclass.isClass() && (superclass.tagBits & TagBits.HasMissingType) == 0) {
				problemReporter().superclassMustBeAClass(sourceType, superclassRef, superclass);
			} else if (superclass.isFinal()) {
				problemReporter().classExtendFinalClass(sourceType, superclassRef, superclass);
			} else if ((superclass.tagBits & TagBits.HasDirectWildcard) != 0) {
				problemReporter().superTypeCannotUseWildcard(sourceType, superclassRef, superclass);
			} else if (superclass.erasure().id == TypeIds.T_JavaLangEnum) {
				problemReporter().cannotExtendEnum(sourceType, superclassRef, superclass);
			} else if (superclass.erasure().id == TypeIds.T_JavaLangRecord) {
				if (!(this.referenceContext.isRecord())) {
					problemReporter().recordCannotExtendRecord(sourceType, superclassRef, superclass);
				} else {
					return connectRecordSuperclass();
				}
			} else if ((superclass.tagBits & TagBits.HierarchyHasProblems) != 0
					|| !superclassRef.resolvedType.isValidBinding()) {
				sourceType.setSuperClass(superclass);
				sourceType.tagBits |= TagBits.HierarchyHasProblems; // propagate if missing supertype
				return superclassRef.resolvedType.isValidBinding(); // reported some error against the source type ?
//{ObjectTeams: team super class only allowed for teams
            } else if ((superclass.id == IOTConstants.T_OrgObjectTeamsTeam || superclass.isTeam())
                        && !sourceType.isTeam())
            {
            	problemReporter().regularExtendsTeam(sourceType, this.referenceContext.superclass, superclass);
// SH}
			} else {
				// only want to reach here when no errors are reported
				sourceType.setSuperClass(superclass);
				sourceType.typeBits |= (superclass.typeBits & TypeIds.InheritableBits);
				// further analysis against white lists for the unlikely case we are compiling java.io.*:
				if ((sourceType.typeBits & (TypeIds.BitAutoCloseable|TypeIds.BitCloseable)) != 0)
					sourceType.typeBits |= sourceType.applyCloseableClassWhitelists(this.compilerOptions());
				return true;
			}
		}
		sourceType.tagBits |= TagBits.HierarchyHasProblems;
		sourceType.setSuperClass(sourceType.isRecord() ? getJavaLangRecord() : getJavaLangObject());
		if ((sourceType.superclass.tagBits & TagBits.BeginHierarchyCheck) == 0)
			detectHierarchyCycle(sourceType, sourceType.superclass, null);
		return false; // reported some error against the source type
	}

	/**
	 *  enum X (implicitly) extends Enum<X>
	 */
	private boolean connectEnumSuperclass() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
		ReferenceBinding rootEnumType = getJavaLangEnum();
		if ((rootEnumType.tagBits & TagBits.HasMissingType) != 0) {
			sourceType.tagBits |= TagBits.HierarchyHasProblems; // mark missing supertpye
			sourceType.setSuperClass(rootEnumType);
			return false;
		}
		boolean foundCycle = detectHierarchyCycle(sourceType, rootEnumType, null);
		// arity check for well-known Enum<E>
		TypeVariableBinding[] refTypeVariables = rootEnumType.typeVariables();
		if (refTypeVariables == Binding.NO_TYPE_VARIABLES) { // check generic
			problemReporter().nonGenericTypeCannotBeParameterized(0, null, rootEnumType, new TypeBinding[]{ sourceType });
			return false; // cannot reach here as AbortCompilation is thrown
		} else if (1 != refTypeVariables.length) { // check arity
			problemReporter().incorrectArityForParameterizedType(null, rootEnumType, new TypeBinding[]{ sourceType });
			return false; // cannot reach here as AbortCompilation is thrown
		}
		// check argument type compatibility
		ParameterizedTypeBinding  superType = environment().createParameterizedType(
			rootEnumType,
			new TypeBinding[]{
				environment().convertToRawType(sourceType, false /*do not force conversion of enclosing types*/),
			} ,
			null);
		sourceType.tagBits |= (superType.tagBits & TagBits.HierarchyHasProblems); // propagate if missing supertpye
		sourceType.setSuperClass(superType);
		// bound check (in case of bogus definition of Enum type)
		if (!refTypeVariables[0].boundCheck(superType, sourceType, this, null).isOKbyJLS()) {
			problemReporter().typeMismatchError(rootEnumType, refTypeVariables[0], sourceType, null);
		}
		return !foundCycle;
	}
	// Call only when we know there's no explicit permits clause and this is a sealed type
	private void connectImplicitPermittedTypes(SourceTypeBinding sourceType) {
		List<SourceTypeBinding> types = new ArrayList<>();
		for (TypeDeclaration typeDecl : this.referenceCompilationUnit().types) {
			types.addAll(sourceType.collectAllTypeBindings(typeDecl, this.compilationUnitScope()));
		}
		Set<ReferenceBinding> permSubTypes = new LinkedHashSet<>();
		for (ReferenceBinding type : types) {
			if (!TypeBinding.equalsEquals(type, sourceType) && type.findSuperTypeOriginatingFrom(sourceType) != null) {
				permSubTypes.add(type);
			}
		}
		if (sourceType.isSealed() && sourceType.isLocalType()) {
			// bug xxxx flag Error and return;
		}
		if (permSubTypes.size() == 0) {
			if (!sourceType.isLocalType()) // error flagged already
				problemReporter().sealedSealedTypeMissingPermits(sourceType, this.referenceContext);
			return;
		}
		sourceType.setPermittedTypes(permSubTypes.toArray(new ReferenceBinding[0]));
	}
/**
	 * @see #connectPermittedTypes()
	 */
	void connectImplicitPermittedTypes() {
		TypeDeclaration typeDecl = this.referenceContext;
		SourceTypeBinding sourceType = typeDecl.binding;
		if (sourceType.id == TypeIds.T_JavaLangObject || sourceType.isEnum() || sourceType.isRecord()) // already handled
			return;
		if (sourceType.isSealed() && (typeDecl.permittedTypes == null ||
				typeDecl.permittedTypes.length == 0)) {
			connectImplicitPermittedTypes(sourceType);
		}
		ReferenceBinding[] memberTypes = sourceType.memberTypes;
		if (memberTypes != null && memberTypes != Binding.NO_MEMBER_TYPES) {
			for (int i = 0, size = memberTypes.length; i < size; i++)
//{ObjectTeams: protect against binary member:
			  if (memberTypes[i] instanceof SourceTypeBinding)
// SH}
				 ((SourceTypeBinding) memberTypes[i]).scope.connectImplicitPermittedTypes();
		}
	}
	/**
	 * This method only deals with the permitted types that are explicitly declared
	 * in a type's permits clause. The implicitly permitted types are all filled in
	 * in {@link #connectImplicitPermittedTypes()}. The reason being, the implicitly
	 * permitted types require the complete type hierarchy to be ready. Therefore, this
	 * method is called inside {@link #connectTypeHierarchy()} and connectImplicitPermittedTypes()
	 * is called after the connectTypeHierarchy(). Why can't we do both after connectTypeHierarchy()?
	 * That is because, in a very specific case of one of an explicitly permitted type also being
	 * a member type and is referenced in the permits clause without type qualifier, we would allow
	 * the following incorrect code:
	 * <pre>
	 * 	public sealed class X permits Y {
	 *		final class Y extends X {}
	 *	}
	 *	</pre>
	 *  If we were to resolve <code>Y</code> in <code>permits Y</code> after resolving
	 *  the hierarchy, Y is resolved in current scope. However, Y should only be
	 *  allowed with the qualifier, in this case, X.Y.
	 */
	void connectPermittedTypes() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
		sourceType.setPermittedTypes(Binding.NO_PERMITTEDTYPES);
		if (this.referenceContext.permittedTypes == null) {
			return;
		}
		if (sourceType.id == TypeIds.T_JavaLangObject || sourceType.isEnum()) // already handled
			return;

		int length = this.referenceContext.permittedTypes.length;
		ReferenceBinding[] permittedTypeBindings = new ReferenceBinding[length];
		int count = 0;
		nextPermittedType : for (int i = 0; i < length; i++) {
			TypeReference permittedTypeRef = this.referenceContext.permittedTypes[i];
			ReferenceBinding permittedType = findPermittedtype(permittedTypeRef);
			if (permittedType == null) { // detected cycle
				continue nextPermittedType;
			}
			// check for simple interface collisions
			// Check for a duplicate interface once the name is resolved, otherwise we may be confused (i.e. a.b.I and c.d.I)
			for (int j = 0; j < i; j++) {
				if (TypeBinding.equalsEquals(permittedTypeBindings[j], permittedType)) {
					problemReporter().sealedDuplicateTypeInPermits(sourceType, permittedTypeRef, permittedType);
					continue nextPermittedType;
				}
			}
			// only want to reach here when no errors are reported
			permittedTypeBindings[count++] = permittedType;
		}
		// hold onto all correctly resolved superinterfaces
		if (count > 0) {
			if (count != length)
				System.arraycopy(permittedTypeBindings, 0, permittedTypeBindings = new ReferenceBinding[count], 0, count);
			sourceType.setPermittedTypes(permittedTypeBindings);
		} else {
			sourceType.setPermittedTypes(Binding.NO_PERMITTEDTYPES);
		}
	}

//{ObjectTeams connect and adjust more class relationships:

    // RoleSplitter might have been over-eager: would we be better of, if we strip
    // the OT_DELIM prefix of our superclass?
    // We only get called, when the long name did not find a class, i.e.,
    // it can't be a role.
    private ReferenceBinding checkAdjustSuperclass(TypeReference superRef, CheckPoint cp)
    {
    	ReferenceBinding found = (ReferenceBinding)superRef.resolvedType;
    	if (found != null && found.isValidBinding())
    		found = null;    // returning found shall signal errors
    		// Note, that indeed superRef.resolveType() may have returned null,
    	    // yet superRef.resolvedType can be a valid type. This happens, e.g.,
    	 	// in org.eclipse.jdt.core.tests.compiler.regression.JavadocTest_1_4
    		// testcasses testBug83127*: a parameterized type reference refers
    		// to a valid class but its parameters have errors.
        if (!this.referenceContext.isSourceRole()) {
        	if ((this.referenceContext.binding.tagBits & TagBits.HierarchyHasProblems) != 0)
        		return null; // already detected a cycle, don't use 'found'!
            return found;
        }
        if (superRef instanceof SingleTypeReference) {
            SingleTypeReference singRef = (SingleTypeReference)superRef;
            if (!RoleSplitter.isClassPartName(singRef.token))
                return found;
            singRef.token = RoleSplitter.getInterfacePartName(singRef.token);
        } else if (superRef instanceof QualifiedTypeReference) {
            QualifiedTypeReference qualRef = (QualifiedTypeReference)superRef;
            int pos = qualRef.tokens.length-1;
            char[] lastToken = qualRef.tokens[pos];
            if (!RoleSplitter.isClassPartName(lastToken))
                return found;
            lastToken = RoleSplitter.getInterfacePartName(lastToken);
            qualRef.tokens[pos] = lastToken;
        }
        // before the second attempt delete first error:
        superRef.resolvedType = null;                    // remove problem binding.
        this.referenceContext.compilationResult.rollBack(cp); // remove IProblem
        return findSupertype(superRef);
    }

	public boolean connectBaseclass() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
		if (!this.referenceContext.isDirectRole()) {
			if (this.referenceContext.baseclass != null)
				problemReporter().playedByInRegularClass(sourceType, this.referenceContext.baseclass);
			return true;
		}
		if (sourceType.isSynthInterface())
			return true; // will transfer baseclass to the ifc part in Dependencies.linkBaseclass
        if (this.referenceContext.baseclass == null) {
        	// leave sourceType.baseclass untouched, might be set by CopyInheritance.copyBaseclass()
            return true;
		}
        if (sourceType.roleModel == null) {
        	// if we already have errors silently quit, otherwise there's a bug here:
        	if (!this.referenceContext.ignoreFurtherInvestigation)
        		throw new InternalCompilerError("Missing role model"); //$NON-NLS-1$
        	return false;
        }
        try {
        	TypeReference baseclassRef = this.referenceContext.baseclass;

	        ReferenceBinding baseclass= findBaseclass(baseclassRef);

	        // detect cycle wrt. containment:
	        ReferenceBinding currentType = sourceType;
	        while (currentType != null && baseclass != null) {
	        	if (TypeBinding.equalsEquals(currentType, baseclass)) {
	        		problemReporter().playedByEnclosing(sourceType, this.referenceContext.baseclass, baseclass);
	        		sourceType.roleModel._playedByEnclosing = true;
	        		break;
	        	}
	        	currentType = currentType.enclosingType();
	        }

	        // check illegal containment cycle:
	        if (baseclass != null) {
	        	ReferenceBinding currentClass = baseclass;
	        	while (currentClass != null) {
	        		if (TypeBinding.equalsEquals(currentClass, sourceType)) {
	        			problemReporter().baseclassIsMember(sourceType, baseclassRef, baseclass);
	        			baseclass = null;
	        			break;
	        		}
	        		currentClass = currentClass.enclosingType();
	        	}
	        }

			if (baseclass != null) { // is null if a cycle was detected

				if (   baseclass.isRole()
					&& !baseclass.isRoleType())
				{
					if (RoleModel.isRoleFromOuterEnclosing(sourceType, baseclass)) {
						TeamModel outerTeam = baseclass.enclosingType().getTeamModel();
						baseclass = (ReferenceBinding)outerTeam.getTThis().getRoleTypeBinding(baseclass, 0);
					} else {
						// unused: TypeReference roleClassRef = TypeAnalyzer.getRoleClassReference(baseclassRef);
						// TODO (SH): replace with general lookup of common enclosing team?
						//            (cf. RoleTypeBinding.findCommonEnclosingTeam())
						if (  TypeBinding.equalsEquals(baseclass.enclosingType(), sourceType.enclosingType())
						   || TypeBinding.equalsEquals(baseclass.enclosingType(), sourceType.enclosingType().superclass()))
						{
							problemReporter().baseclassIsRoleOfTheSameTeam(sourceType, this.referenceContext.baseclass, baseclass);
							sourceType.baseclass = null;
							baseclassRef.resolvedType = null;
							RoleModel.setTagBit(sourceType, RoleModel.BaseclassHasProblems);
							this.referenceContext.pushDownBindingProblem();
							return false;
						}
						problemReporter().missingTypeAnchor(this.referenceContext.baseclass, baseclass);
					}
				}
				TypeVariableBinding[] roleVariables = sourceType.typeVariables;
				if (baseclass.isParameterizedType()) {
					if (!areEqualTypeParameters(roleVariables, ((ParameterizedTypeBinding) baseclass).arguments)) {
						problemReporter().parameterizedBaseclass(baseclassRef, baseclass);
						baseclass = (ReferenceBinding)baseclass.erasure();
					}
				} else if (sourceType.typeVariables() != Binding.NO_TYPE_VARIABLES) {
					problemReporter().nonParameterizedBaseclass(baseclassRef, sourceType);
				}

				if (baseclass.isFinal())
					problemReporter().decapsulationOfFinal(baseclassRef, baseclass);
				if (baseclass instanceof BinaryTypeBinding
					&& ((BinaryTypeBinding)baseclass).version >= ClassFileConstants.JDK1_8
					&& compilerOptions().weavingScheme == WeavingScheme.OTRE) {
					problemReporter().otreCannotWeaveIntoJava8(baseclassRef, baseclass, (int) (((BinaryTypeBinding)baseclass).version >> 16));
				}
				if (/*   !sourceType.isInterface() // FIXME(SH): ifc playedBy ifc is currently incompatible with add/removeRole infrastructure.
                    && */baseclass.isInterface() && (baseclass.tagBits & TagBits.HasMissingType) == 0)
				{
					// Continue after reporting.
					if (!baseclass.isSynthInterface()) // un-anchored reference to role type? (already reported above).
						problemReporter().baseclassMustBeAClass(sourceType, this.referenceContext.baseclass, baseclass);
				}
				if ((baseclass.tagBits & TagBits.HierarchyHasProblems) != 0)
					RoleModel.setTagBit(sourceType, RoleModel.BaseclassHasProblems);
				if (!baseclass.isValidBinding() && baseclass instanceof ProblemReferenceBinding) {
					TypeBinding match= ((ProblemReferenceBinding)baseclass).closestMatch();
					if (match instanceof ReferenceBinding) {
						if (baseclassRef.getBaseclassDecapsulation() != DecapsulationState.CONFINED) //=> decaps confined already reported
							problemReporter().invalidType(this.referenceContext.baseclass, baseclass);
					}
				} else if (sourceType.id == TypeIds.T_JavaLangObject) {
					// can only happen if Object extends another type... will never happen unless we're testing for it.
					sourceType.tagBits |= TagBits.HierarchyHasProblems;
					sourceType.baseclass = null;
					return true;
				} else {
					// only want to reach here when no errors are reported
					sourceType.baseclass = baseclass;

					RoleModel roleModel = sourceType.roleModel;
					roleModel._state.addJob(ITranslationStates.STATE_ROLES_LINKED, new Runnable() {
						@Override
						public void run() {
							checkBaseClassRedefinition(ClassScope.this.referenceContext);
						}
					});

					char[] packageName = baseclass.compoundName[0];
					if (   CharOperation.equals(packageName, "java".toCharArray())   //$NON-NLS-1$
						|| CharOperation.equals(packageName, "javax".toCharArray())) //$NON-NLS-1$
						problemReporter().tryingToWeaveIntoSystemClass(this.referenceContext.baseclass, baseclass);

					// pretend "implements o.o.IBoundBase", will be generated by the OTRE:
					baseclass.setIsBoundBase(sourceType);

					boolean createBinding = sourceType.unResolvedFields() != Binding.UNINITIALIZED_FIELDS; // don't create field binding if none have been built yet
					StandardElementGenerator.checkCreateBaseField(this.referenceContext, baseclass, createBinding);
					return true;
				}
		    }
        } finally {
			// trap for selection/completion only after checking base.R things:
			this.referenceContext.baseclass.aboutToResolve(this);
        }
		RoleModel.setTagBit(sourceType, RoleModel.BaseclassHasProblems);
        // give at least some legal base class:
		sourceType.baseclass = getJavaLangObject();
		if ((sourceType.baseclass.tagBits & TagBits.BeginHierarchyCheck) == 0)
			detectHierarchyCycle(sourceType, sourceType.baseclass, null);

        this.referenceContext.pushDownBindingProblem();

		return false; // reported some error against the source type
	}

	public void connectBaseclassRecurse() {
		connectBaseclass();
		TypeDeclaration[] members = this.referenceContext.memberTypes;
		if (members != null)
			for (TypeDeclaration member : members)
				member.scope.connectBaseclassRecurse();
	}

	private boolean areEqualTypeParameters(TypeVariableBinding[] roleTypeVariables, TypeBinding[] baseTypeArguments) {
		if (roleTypeVariables.length != baseTypeArguments.length)
			return false;
		for (int i = 0; i < roleTypeVariables.length; i++) {
			if (TypeBinding.notEquals(roleTypeVariables[i], baseTypeArguments[i]))
				return false;
		}
		return true;
	}

	// check OTJLD 2.1(c) and OTJLD 2.1(d)
	void checkBaseClassRedefinition(TypeDeclaration roleDecl)
	{
		ReferenceBinding baseclass = roleDecl.binding.baseclass;
		Dependencies.ensureBindingState(baseclass, ITranslationStates.STATE_ROLES_LINKED);

		ReferenceBinding superclass = roleDecl.binding.superclass;
		if (superclass != null) {
			ReferenceBinding superBase = superclass.rawBaseclass();
			if (   superBase != null
				&& !baseclass.isCompatibleWith(superBase))
			{
				problemReporter().illegalPlayedByRedefinition(roleDecl.baseclass, baseclass,
															  superclass,		  superBase);
			}
		}
		// for comparison with tsuper's baseclass separately check type and type-anchor:
		ITeamAnchor baseAnchor = null;
		if (RoleTypeBinding.isRoleWithExplicitAnchor(baseclass)) {
			baseAnchor = ((RoleTypeBinding)baseclass)._teamAnchor;
			baseclass = baseclass.getRealType();
		}
		for (ReferenceBinding tsuperRole: roleDecl.getRoleModel().getTSuperRoleBindings()) {
			ReferenceBinding tsuperBase = tsuperRole.baseclass();
			if (   tsuperBase != null && TypeBinding.notEquals(tsuperBase, baseclass)) {
				ITeamAnchor tsuperAnchor = null;
				if (RoleTypeBinding.isRoleWithExplicitAnchor(tsuperBase)) {
					tsuperAnchor = ((RoleTypeBinding)tsuperBase)._teamAnchor;
					tsuperBase = tsuperBase.getRealType();
				}
				if (   !(TypeBinding.equalsEquals(baseclass, tsuperBase) || TSuperHelper.isTSubOf(baseclass, tsuperBase))
					|| !TSuperHelper.isEquivalentField(baseAnchor, tsuperAnchor))
				{
					problemReporter().overridesPlayedBy(ClassScope.this.referenceContext, tsuperBase);
				}
			}
		}
	}

	// mostly like connectSuperclass
	private boolean connectSuperteam() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
// team: handle o.o.Team vaguely similar to j.l.Object:
		if (isOrgObjectteamsTeam(sourceType)) { // handle the case of redefining org.objectteams.Team up front
			this.referenceContext.modifiers |= ExtraCompilerModifiers.AccTeam;
			sourceType.superclass = getJavaLangObject();
			if (   this.referenceContext.superclass != null)
				problemReporter().teamCannotHaveSuperTypes(sourceType);
			return true; // do not propagate Team's hierarchy problems down to every subtype
		}
// :maet

		if (this.referenceContext.superclass == null) {
			if (sourceType.isEnum() && compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) // do not connect if source < 1.5 as enum already got flagged as syntax error
// team: enum not allowed for team, different default superclass
			{
				problemReporter().illegalModifierForTeam(this.referenceContext);
				return false;
			}
			LookupEnvironment environment = environment();
			try {
				environment.missingClassFileLocation = this.referenceContext;
				sourceType.superclass = getOrgObjectteamsTeam();
				if (sourceType.superclass == null || (sourceType.superclass.tagBits & TagBits.HasMissingType) != 0) {
					sourceType.tagBits |= TagBits.HierarchyHasProblems;
					return false; // o.o.Team not found
				}
			} finally {
				environment.missingClassFileLocation = null;
			}
// :maet
			return !detectHierarchyCycle(sourceType, sourceType.superclass, null);
		}
		TypeReference superclassRef = this.referenceContext.superclass;
//{ObjectTeams: option to roll back problems:
		CheckPoint cp = this.referenceContext.compilationResult.getCheckPoint(this.referenceContext);
// SH}
		ReferenceBinding superclass = findSupertype(superclassRef);
//{ObjectTeams: second chance.
	    if (superclass == null || superclass instanceof MissingTypeBinding) { // problem binding is in superclassRef.resolvedType
            ReferenceBinding adjustedSuperclass = checkAdjustSuperclass(superclassRef, cp);
            if (adjustedSuperclass != null && adjustedSuperclass.isValidBinding())
            	superclass = adjustedSuperclass;
	    }
// SH}

		if (superclass != null) { // is null if a cycle was detected cycle or a problem
//{ObjectTeams: additional checks:
	        if (!Protections.checkCompatibleEnclosingForRoles(
	                this,
	                this.referenceContext,
	                this.referenceContext.binding,
	                superclass)) {
	        	// nop, already reported inside
			} else
// SH}
			if (!superclass.isClass() && (superclass.tagBits & TagBits.HasMissingType) == 0) {
				problemReporter().superclassMustBeAClass(sourceType, superclassRef, superclass);
			} else if (superclass.isFinal()) {
				problemReporter().classExtendFinalClass(sourceType, superclassRef, superclass);
			} else if ((superclass.tagBits & TagBits.HasDirectWildcard) != 0) {
				problemReporter().superTypeCannotUseWildcard(sourceType, superclassRef, superclass);
			} else if (superclass.erasure().id == TypeIds.T_JavaLangEnum) {
				problemReporter().cannotExtendEnum(sourceType, superclassRef, superclass);
			} else if ((superclass.tagBits & TagBits.HierarchyHasProblems) != 0
					|| !superclassRef.resolvedType.isValidBinding()) {
				sourceType.superclass = superclass;
				sourceType.tagBits |= TagBits.HierarchyHasProblems; // propagate if missing supertype
				return superclassRef.resolvedType.isValidBinding(); // reported some error against the source type ?
// :maet
			} else {
				// only want to reach here when no errors are reported
				sourceType.superclass = superclass;

// team:
				// if superclass is not o.o.Team add "implements org.objectteams.ITeam":
				if (!superclass.isTeam()) {
					AstGenerator gen = new AstGenerator(this.referenceContext);
					QualifiedTypeReference additionalSuperIfc = gen.qualifiedTypeReference(IOTConstants.ORG_OBJECTTEAMS_ITEAM);
					if (this.referenceContext.superInterfaces != null) {
						int len = this.referenceContext.superInterfaces.length;
						System.arraycopy(this.referenceContext.superInterfaces, 0, this.referenceContext.superInterfaces = new TypeReference[len+1], 0, len);
						this.referenceContext.superInterfaces[len] = additionalSuperIfc;
					} else {
						this.referenceContext.superInterfaces = new TypeReference[] { additionalSuperIfc };
					}
				}

				// might need line numbers of super team, although parser thought we would not..
                if (   superclass instanceof SourceTypeBinding
                     && superclass.model.getState() != ITranslationStates.STATE_FINAL)
                {
                	CompilationUnitDeclaration result = ((SourceTypeBinding)superclass).scope.referenceCompilationUnit();
                	if (result.compilationResult.lineSeparatorPositions == null)
                		result.compilationResult.lineSeparatorPositions = new int[0];
                }
// :maet
				return true;
			}
		}
		sourceType.tagBits |= TagBits.HierarchyHasProblems;
		if (!isOrgObjectteamsTeam(sourceType)) {
			sourceType.superclass = getOrgObjectteamsTeam();
			if ((sourceType.superclass.tagBits & TagBits.BeginHierarchyCheck) == 0)
				detectHierarchyCycle(sourceType, sourceType.superclass, null);
		}
		return false; // reported some error against the source type
	}
// Markus Witte+SH}


	private boolean connectRecordSuperclass() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
		ReferenceBinding rootRecordType = getJavaLangRecord();
		sourceType.setSuperClass(rootRecordType);
		if ((rootRecordType.tagBits & TagBits.HasMissingType) != 0) {
			sourceType.tagBits |= TagBits.HierarchyHasProblems; // mark missing supertpye
			return false;
		}
		return !detectHierarchyCycle(sourceType, rootRecordType, null);
	}
	/*
		Our current belief based on available JCK 1.3 tests is:
			inherited member types are visible as a potential superclass.
			inherited interfaces are visible when defining a superinterface.

		Error recovery story:
			ensure the superinterfaces contain only valid visible interfaces.

		Answer false if an error was reported against the sourceType.
	*/
	private boolean connectSuperInterfaces() {
		SourceTypeBinding sourceType = this.referenceContext.binding;
		sourceType.setSuperInterfaces(Binding.NO_SUPERINTERFACES);
		if (this.referenceContext.superInterfaces == null) {
			if (sourceType.isAnnotationType() && compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5) { // do not connect if source < 1.5 as annotation already got flagged as syntax error) {
				ReferenceBinding annotationType = getJavaLangAnnotationAnnotation();
				boolean foundCycle = detectHierarchyCycle(sourceType, annotationType, null);
				sourceType.setSuperInterfaces(new ReferenceBinding[] { annotationType });
				return !foundCycle;
			}
			return true;
		}
		if (sourceType.id == TypeIds.T_JavaLangObject) // already handled the case of redefining java.lang.Object
			return true;

		boolean noProblems = true;
		int length = this.referenceContext.superInterfaces.length;
		ReferenceBinding[] interfaceBindings = new ReferenceBinding[length];
		int count = 0;
		nextInterface : for (int i = 0; i < length; i++) {
		    TypeReference superInterfaceRef = this.referenceContext.superInterfaces[i];
			ReferenceBinding superInterface = findSupertype(superInterfaceRef);
			if (superInterface == null) { // detected cycle
				sourceType.tagBits |= TagBits.HierarchyHasProblems;
				noProblems = false;
				continue nextInterface;
			}
//{ObjectTeams: check special rules for "implements IXXXMigratable"
			noProblems &= RoleMigrationImplementor.checkMigratableInterfaces(sourceType, superInterfaceRef, superInterface, this);
			if (!noProblems) {
				sourceType.tagBits |= TagBits.HierarchyHasProblems;
				continue nextInterface;
			}
			if (   sourceType.isInterface()
				&& TypeAnalyzer.isConfined(superInterface)
				&& this.referenceContext.superclass == null)
			{
				sourceType.superclass = null; // cancel premature superclass j.l.Object
			}
// SH}

			// check for simple interface collisions
			// Check for a duplicate interface once the name is resolved, otherwise we may be confused (i.e. a.b.I and c.d.I)
			for (int j = 0; j < i; j++) {
				if (TypeBinding.equalsEquals(interfaceBindings[j], superInterface)) {
					problemReporter().duplicateSuperinterface(sourceType, superInterfaceRef, superInterface);
					sourceType.tagBits |= TagBits.HierarchyHasProblems;
					noProblems = false;
					continue nextInterface;
				}
			}
			if (!superInterface.isInterface() && (superInterface.tagBits & TagBits.HasMissingType) == 0) {
				problemReporter().superinterfaceMustBeAnInterface(sourceType, superInterfaceRef, superInterface);
				sourceType.tagBits |= TagBits.HierarchyHasProblems;
				noProblems = false;
				continue nextInterface;
			} else if (superInterface.isAnnotationType()){
				problemReporter().annotationTypeUsedAsSuperinterface(sourceType, superInterfaceRef, superInterface);
			}
			if ((superInterface.tagBits & TagBits.HasDirectWildcard) != 0) {
				problemReporter().superTypeCannotUseWildcard(sourceType, superInterfaceRef, superInterface);
				sourceType.tagBits |= TagBits.HierarchyHasProblems;
				noProblems = false;
				continue nextInterface;
			}
			if ((superInterface.tagBits & TagBits.HierarchyHasProblems) != 0
					|| !superInterfaceRef.resolvedType.isValidBinding()) {
				sourceType.tagBits |= TagBits.HierarchyHasProblems; // propagate if missing supertype
				noProblems &= superInterfaceRef.resolvedType.isValidBinding();
			}
			// only want to reach here when no errors are reported
			sourceType.typeBits |= (superInterface.typeBits & TypeIds.InheritableBits);
			// further analysis against white lists for the unlikely case we are compiling java.util.stream.Stream:
			if ((sourceType.typeBits & (TypeIds.BitAutoCloseable|TypeIds.BitCloseable)) != 0)
				sourceType.typeBits |= sourceType.applyCloseableInterfaceWhitelists();
			interfaceBindings[count++] = superInterface;
		}
		// hold onto all correctly resolved superinterfaces
		if (count > 0) {
			if (count != length)
				System.arraycopy(interfaceBindings, 0, interfaceBindings = new ReferenceBinding[count], 0, count);
			sourceType.setSuperInterfaces(interfaceBindings);
		}
		return noProblems;
	}

//{ObjectTeams: special entries for generated types.
	/**
	 * connect hierarchy for a generate type.
	 * @param full should be do all steps of connectTypeHierarchy() or save descending for later?
	 */
	public void connectTypeHierarchyForGenerated(boolean full) {
		if (full)
			connectTypeHierarchy();
		else
			connectTypeHierarchyWithoutMembers();
	}

	public void buildFieldsAndMethodsForLateRole(){
		assert (   this.referenceContext.isGenerated
				|| this.referenceContext.isPurelyCopied
				|| this.referenceContext.binding.isLocalType()
				|| this.referenceContext.isRoleFile()) : "Only applicable for late roles"; //$NON-NLS-1$
		buildFieldsAndMethods();
	}
//Markus Witte}

	void connectTypeHierarchy() {
//{ObjectTeams:
		// already done?
		if (StateHelper.hasState(this.referenceContext.binding,
				                 ITranslationStates.STATE_LENV_CONNECT_TYPE_HIERARCHY))
			return;
		// signal proccessing:
		if (!StateHelper.startProcessing(this.referenceContext,
				   ITranslationStates.STATE_LENV_CONNECT_TYPE_HIERARCHY,
				   LookupEnvironment.CONNECT_TYPE_HIERARCHY))
			return; // catchup was blocked.
// SH}
		SourceTypeBinding sourceType = this.referenceContext.binding;
		CompilationUnitScope compilationUnitScope = compilationUnitScope();
		boolean wasAlreadyConnecting = compilationUnitScope.connectingHierarchy;
		compilationUnitScope.connectingHierarchy = true;
		try {
			if ((sourceType.tagBits & TagBits.BeginHierarchyCheck) == 0) {
				sourceType.tagBits |= TagBits.BeginHierarchyCheck;
				environment().typesBeingConnected.add(sourceType);
				boolean noProblems = connectSuperclass();
				noProblems &= connectSuperInterfaces();
				environment().typesBeingConnected.remove(sourceType);
				sourceType.tagBits |= TagBits.EndHierarchyCheck;
				connectPermittedTypes();
				noProblems &= connectTypeVariables(this.referenceContext.typeParameters, false);
				sourceType.tagBits |= TagBits.TypeVariablesAreConnected;
				if (noProblems && sourceType.isHierarchyInconsistent())
					problemReporter().hierarchyHasProblems(sourceType);
			}
//{ObjectTeams: top level source super-team must be fully loaded/connected:
			ReferenceBinding superType= sourceType.superclass;
			if (   superType != null
					&& superType.isTeam())
			{
				ReferenceBinding superOriginal = (ReferenceBinding) superType.original();
				if (!superOriginal.isBinaryBinding()) {
					ClassScope superScope = ((SourceTypeBinding) superOriginal).scope;
					if (superScope != null)
						superScope.connectTypeHierarchy();
				}
			}
// SH}
			connectMemberTypes();
		} finally {
			compilationUnitScope.connectingHierarchy = wasAlreadyConnecting;
		}
		LookupEnvironment env = environment();
		try {
			env.missingClassFileLocation = this.referenceContext;
			checkForInheritedMemberTypes(sourceType);
		} catch (AbortCompilation e) {
			e.updateContext(this.referenceContext, referenceCompilationUnit().compilationResult);
			throw e;
		} finally {
			env.missingClassFileLocation = null;
		}
	}

	@Override
	public boolean deferCheck(Runnable check) {
		if (compilationUnitScope().connectingHierarchy) {
			if (this.deferredBoundChecks == null)
				this.deferredBoundChecks = new ArrayList<>();
			this.deferredBoundChecks.add(check);
			return true;
		} else {
			return false;
		}
	}

//{ObejctTeams: accessible from CopyInheritance:
/* orig:
	private void connectTypeHierarchyWithoutMembers() {
  :giro */
	public void connectTypeHierarchyWithoutMembers() {
// SH}
		// must ensure the imports are resolved
//{ObjectTeams: RoFi?
		if (this.referenceContext.isRoleFile())
			checkRoleFileImports();
// SH}
		if (this.parent instanceof CompilationUnitScope) {
			if (((CompilationUnitScope) this.parent).imports == null)
				 ((CompilationUnitScope) this.parent).checkAndSetImports();
		} else if (this.parent instanceof ClassScope) {
			// ensure that the enclosing type has already been checked
			 ((ClassScope) this.parent).connectTypeHierarchyWithoutMembers();
		}

		// double check that the hierarchy search has not already begun...
		SourceTypeBinding sourceType = this.referenceContext.binding;
		if ((sourceType.tagBits & TagBits.BeginHierarchyCheck) != 0)
			return;

		CompilationUnitScope compilationUnitScope = compilationUnitScope();
		boolean wasAlreadyConnecting = compilationUnitScope.connectingHierarchy;
		compilationUnitScope.connectingHierarchy = true;
		try {
			sourceType.tagBits |= TagBits.BeginHierarchyCheck;
			environment().typesBeingConnected.add(sourceType);
			boolean noProblems = connectSuperclass();
			noProblems &= connectSuperInterfaces();
			environment().typesBeingConnected.remove(sourceType);
			sourceType.tagBits |= TagBits.EndHierarchyCheck;
			connectPermittedTypes();
			noProblems &= connectTypeVariables(this.referenceContext.typeParameters, false);
			sourceType.tagBits |= TagBits.TypeVariablesAreConnected;
			if (noProblems && sourceType.isHierarchyInconsistent())
				problemReporter().hierarchyHasProblems(sourceType);
		} finally {
			compilationUnitScope.connectingHierarchy = wasAlreadyConnecting;
		}
	}

//{ObjectTeams: ROFI (to be overridden in OTClassScope)
	protected void checkRoleFileImports() {
		CompilationUnitScope cuScope= compilationUnitScope();
		if (cuScope.imports == null)
			cuScope.checkAndSetImports();
	}
// SH}

	public boolean detectHierarchyCycle(TypeBinding superType, TypeReference reference) {
		if (!(superType instanceof ReferenceBinding)) return false;

		if (reference == this.superTypeReference) { // see findSuperType()
			if (superType.isTypeVariable())
				return false; // error case caught in resolveSuperType()
			// abstract class X<K,V> implements java.util.Map<K,V>
			//    static abstract class M<K,V> implements Entry<K,V>
			if (superType.isParameterizedType())
				superType = ((ParameterizedTypeBinding) superType).genericType();
			compilationUnitScope().recordSuperTypeReference(superType); // to record supertypes
			return detectHierarchyCycle(this.referenceContext.binding, (ReferenceBinding) superType, reference);
		}
		// Reinstate the code deleted by the fix for https://bugs.eclipse.org/bugs/show_bug.cgi?id=205235
		// For details, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=294057.
		if ((superType.tagBits & TagBits.BeginHierarchyCheck) == 0 && superType instanceof SourceTypeBinding)
			// ensure if this is a source superclass that it has already been checked
			((SourceTypeBinding) superType).scope.connectTypeHierarchyWithoutMembers();

		return false;
	}

	// Answer whether a cycle was found between the sourceType & the superType
	private boolean detectHierarchyCycle(SourceTypeBinding sourceType, ReferenceBinding superType, TypeReference reference) {
		if (superType.isRawType())
//{ObjectTeams: relax the cast to include half-raw dependent types:
/* orig:
			superType = ((RawTypeBinding) superType).genericType();
  :giro */
			superType = ((ParameterizedTypeBinding) superType).genericType();
// SH}
		// by this point the superType must be a binary or source type

		if (TypeBinding.equalsEquals(sourceType, superType)) {
			problemReporter().hierarchyCircularity(sourceType, superType, reference);
			sourceType.tagBits |= TagBits.HierarchyHasProblems;
			return true;
		}

		if (superType.isMemberType()) {
			ReferenceBinding current = superType.enclosingType();
			do {
				if (current.isHierarchyBeingActivelyConnected()) {
					problemReporter().hierarchyCircularity(sourceType, current, reference);
					sourceType.tagBits |= TagBits.HierarchyHasProblems;
					current.tagBits |= TagBits.HierarchyHasProblems;
					return true;
				}
			} while ((current = current.enclosingType()) != null);
		}

		if (superType.isBinaryBinding()) {
			// force its superclass & superinterfaces to be found... 2 possibilities exist - the source type is included in the hierarchy of:
			//		- a binary type... this case MUST be caught & reported here
			//		- another source type... this case is reported against the other source type
			if (superType.problemId() != ProblemReasons.NotFound && (superType.tagBits & TagBits.HierarchyHasProblems) != 0) {
				sourceType.tagBits |= TagBits.HierarchyHasProblems;
				problemReporter().hierarchyHasProblems(sourceType);
				return true;
			}
			boolean hasCycle = false;
			ReferenceBinding parentType = superType.superclass();
			if (parentType != null) {
				if (TypeBinding.equalsEquals(sourceType, parentType)) {
					problemReporter().hierarchyCircularity(sourceType, superType, reference);
					sourceType.tagBits |= TagBits.HierarchyHasProblems;
					superType.tagBits |= TagBits.HierarchyHasProblems;
					return true;
				}
				if (parentType.isParameterizedType())
					parentType = ((ParameterizedTypeBinding) parentType).genericType();
				hasCycle |= detectHierarchyCycle(sourceType, parentType, reference);
				if ((parentType.tagBits & TagBits.HierarchyHasProblems) != 0) {
					sourceType.tagBits |= TagBits.HierarchyHasProblems;
					parentType.tagBits |= TagBits.HierarchyHasProblems; // propagate down the hierarchy
				}
			}

			ReferenceBinding[] itsInterfaces = superType.superInterfaces();
			if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
				for (int i = 0, length = itsInterfaces.length; i < length; i++) {
					ReferenceBinding anInterface = itsInterfaces[i];
					if (TypeBinding.equalsEquals(sourceType, anInterface)) {
						problemReporter().hierarchyCircularity(sourceType, superType, reference);
						sourceType.tagBits |= TagBits.HierarchyHasProblems;
						superType.tagBits |= TagBits.HierarchyHasProblems;
						return true;
					}
					if (anInterface.isParameterizedType())
						anInterface = ((ParameterizedTypeBinding) anInterface).genericType();
					hasCycle |= detectHierarchyCycle(sourceType, anInterface, reference);
					if ((anInterface.tagBits & TagBits.HierarchyHasProblems) != 0) {
						sourceType.tagBits |= TagBits.HierarchyHasProblems;
						superType.tagBits |= TagBits.HierarchyHasProblems;
					}
				}
			}
			return hasCycle;
		}

		if (superType.isHierarchyBeingActivelyConnected()) {
			org.eclipse.jdt.internal.compiler.ast.TypeReference ref = ((SourceTypeBinding) superType).scope.superTypeReference;
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=133071
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=121734
			if (ref != null && ref.resolvedType != null) {
				ReferenceBinding s = (ReferenceBinding) ref.resolvedType;
				do {
					if (s.isHierarchyBeingActivelyConnected()) {
						problemReporter().hierarchyCircularity(sourceType, superType, reference);
						sourceType.tagBits |= TagBits.HierarchyHasProblems;
						superType.tagBits |= TagBits.HierarchyHasProblems;
						return true;
					}
				} while ((s = s.enclosingType()) != null);
			}
			if (ref != null && ref.resolvedType == null) {
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=319885 Don't cry foul prematurely.
				// Check the edges traversed to see if there really is a cycle.
				char [] referredName = ref.getLastToken();
				for (Iterator iter = environment().typesBeingConnected.iterator(); iter.hasNext();) {
					SourceTypeBinding type = (SourceTypeBinding) iter.next();
					if (CharOperation.equals(referredName, type.sourceName())) {
						problemReporter().hierarchyCircularity(sourceType, superType, reference);
						sourceType.tagBits |= TagBits.HierarchyHasProblems;
						superType.tagBits |= TagBits.HierarchyHasProblems;
						return true;
					}
				}
			}
		}
		if ((superType.tagBits & TagBits.BeginHierarchyCheck) == 0) {
			// ensure if this is a source superclass that it has already been checked
			if (superType.isValidBinding() && !superType.isUnresolvedType())
				((SourceTypeBinding) superType).scope.connectTypeHierarchyWithoutMembers();
		}
		if ((superType.tagBits & TagBits.HierarchyHasProblems) != 0)
			sourceType.tagBits |= TagBits.HierarchyHasProblems;
		return false;
	}

	private ReferenceBinding findSupertype(TypeReference typeReference) {
		CompilationUnitScope unitScope = compilationUnitScope();
		LookupEnvironment env = unitScope.environment;
		try {
			env.missingClassFileLocation = typeReference;
			typeReference.aboutToResolve(this); // allows us to trap completion & selection nodes
/* FIXME(SH): want to override parent.getTypeOrPackage(), call chain is:
 *            typeReference.resolveSuperType(Scope)
 *  		  TypeReference.getTypeBinding(Scope)
 *  		  Scope.getTypeOrPackage() -> loops over parents.
 *           Does OTClassScope need to override getTypeOrPackage/getPackage ??
 */
			unitScope.recordQualifiedReference(typeReference.getTypeName());
			this.superTypeReference = typeReference;
			ReferenceBinding superType = (ReferenceBinding) typeReference.resolveSuperType(this);
//{ObjectTeams:	anchor.R is not allowed in this position
			if (superType != null && superType.isRoleType())
			{
				RoleTypeBinding superRole = (RoleTypeBinding)superType;
				if (superRole.hasExplicitAnchor()) {
					typeReference.resolvedType = new ProblemReferenceBinding(typeReference.getTypeName(), superType, ProblemReasons.NotVisible);
					problemReporter().externalizedRoleNotAllowedHere(typeReference, superRole);
					return null;
				}
			}
// SH}
			return superType;
		} catch (AbortCompilation e) {
			SourceTypeBinding sourceType = this.referenceContext.binding;
			if (sourceType.superInterfaces == null)  sourceType.setSuperInterfaces(Binding.NO_SUPERINTERFACES); // be more resilient for hierarchies (144976)
			if (sourceType.permittedTypes == null)  sourceType.setPermittedTypes(Binding.NO_PERMITTEDTYPES);
			e.updateContext(typeReference, referenceCompilationUnit().compilationResult);
			throw e;
		} finally {
			env.missingClassFileLocation = null;
			this.superTypeReference = null;
		}
	}

//{ObjectTeams: similar to findSupertype but for "playedBy"
	private ReferenceBinding findBaseclass(TypeReference typeReference) {
		CompilationUnitScope unitScope = compilationUnitScope();
		LookupEnvironment env = unitScope.environment;
		try {
			env.missingClassFileLocation = typeReference;

// a problem be _returned_ in any case (selection may otherwise _throw_ a problem!)
/* orig:
			typeReference.aboutToResolve(this); // allows us to trap completion & selection nodes
  :giro */
// SH}
/* FIXME(SH): want to override parent.getTypeOrPackage(), call chain is:
 *            typeReference.resolveSuperType(Scope)
 *  		  TypeReference.getTypeBinding(Scope)
 *  		  Scope.getTypeOrPackage() -> loops over parents.
 *           Does OTClassScope need to override getTypeOrPackage/getPackage ??
 */
			unitScope.recordQualifiedReference(typeReference.getTypeName());
//{ObjectTeams: preferably resolve using base import scope:
/* orig:
			ReferenceBinding superType = (ReferenceBinding) typeReference.resolveSuperType(this);
 */
			typeReference.deprecationProblemId = IProblem.DeprecatedBaseclass;
			ReferenceBinding superType;
			if (typeReference.checkResolveUsingBaseImportScope(this, 0, false) != null) {
				superType = (ReferenceBinding)typeReference.resolvedType;
			} else {
				superType = (ReferenceBinding) typeReference.resolveSuperType(this);
				if (isImportedType(typeReference)) {
					problemReporter().regularlyImportedBaseclass(typeReference);
				} else if (   typeReference instanceof QualifiedTypeReference 							// could mean several things:
						   && superType != null && superType.isValidBinding()) 							// (only investigate if valid)
				{
	        		if (   !RoleTypeBinding.isRoleWithExplicitAnchor(superType) 					    // old style externalized still tolerated
	        			&& ((QualifiedTypeReference)typeReference).tokens.length > superType.depth()+1)	// if lenght == depth+1: qualifiation by enclosing types only
	        		{
	        			problemReporter().qualifiedReferenceToBaseclass(typeReference);					// qualified unimported base class is deprecated
	        		}
	        	}
			}
			return superType;
		} catch (AbortCompilation e) {
			e.updateContext(typeReference, referenceCompilationUnit().compilationResult);
			throw e;
		} finally {
			env.missingClassFileLocation = null;
		}
	}

	private boolean isImportedType(TypeReference typeReference)
	{
		if (!(typeReference instanceof SingleTypeReference) || (typeReference instanceof IAlienScopeTypeReference))
			return false;
		if (typeReference instanceof ParameterizedSingleTypeReference) {
			ParameterizedSingleTypeReference paramType = (ParameterizedSingleTypeReference) typeReference;
			if (paramType.typeAnchors != null && paramType.typeAnchors.length>0)
				return false; // resolved via anchor not via import
		}
		TypeBinding type= typeReference.resolvedType;
		if (type == null || !type.isValidBinding() || !(type instanceof ReferenceBinding))
			return false;
		PackageBinding aPackage= ((ReferenceBinding)type).fPackage;
		return aPackage != getCurrentPackage()
			&& !CharOperation.equals(aPackage.compoundName, TypeConstants.JAVA_LANG);
	}
// SH}

	private ReferenceBinding findPermittedtype(TypeReference typeReference) {
		CompilationUnitScope unitScope = compilationUnitScope();
		LookupEnvironment env = unitScope.environment;
		try {
			env.missingClassFileLocation = typeReference;
			typeReference.aboutToResolve(this); // allows us to trap completion & selection nodes
			unitScope.recordQualifiedReference(typeReference.getTypeName());
			ReferenceBinding permittedType = (ReferenceBinding) typeReference.resolveType(this);
			return permittedType;
		} catch (AbortCompilation e) {
			SourceTypeBinding sourceType = this.referenceContext.binding;
			if (sourceType.permittedTypes == null)  sourceType.setPermittedTypes(Binding.NO_PERMITTEDTYPES);
			e.updateContext(typeReference, referenceCompilationUnit().compilationResult);
			throw e;
		} finally {
			env.missingClassFileLocation = null;
		}
	}
	/* Answer the problem reporter to use for raising new problems.
	*
	* Note that as a side-effect, this updates the current reference context
	* (unit, type or method) in case the problem handler decides it is necessary
	* to abort.
	*/
	@Override
	public ProblemReporter problemReporter() {
		MethodScope outerMethodScope;
		if ((outerMethodScope = outerMostMethodScope()) == null) {
			ProblemReporter problemReporter = referenceCompilationUnit().problemReporter;
			problemReporter.referenceContext = this.referenceContext;
			return problemReporter;
		}
		return outerMethodScope.problemReporter();
	}

	/* Answer the reference type of this scope.
	* It is the nearest enclosing type of this scope.
	*/
	@Override
	public TypeDeclaration referenceType() {
		return this.referenceContext;
	}

	@Override
	public boolean hasDefaultNullnessFor(int location, int sourceStart) {
		int nonNullByDefaultValue = localNonNullByDefaultValue(sourceStart);
		if (nonNullByDefaultValue != 0) {
			return (nonNullByDefaultValue & location) != 0;
		}
		SourceTypeBinding binding = this.referenceContext.binding;
		if (binding != null) {
			int nullDefault = binding.getNullDefault();
			if (nullDefault != 0) {
				return (nullDefault & location) != 0;
			}
		}
		return this.parent.hasDefaultNullnessFor(location, sourceStart);
	}

	@Override
	public /* @Nullable */ Binding checkRedundantDefaultNullness(int nullBits, int sourceStart) {
		Binding target = localCheckRedundantDefaultNullness(nullBits, sourceStart);
		if (target != null) {
			return target;
		}
		SourceTypeBinding binding = this.referenceContext.binding;
		if (binding != null) {
			int nullDefault = binding.getNullDefault();
			if (nullDefault != 0) {
				return (nullDefault == nullBits) ? binding : null;
			}
		}
		return this.parent.checkRedundantDefaultNullness(nullBits, sourceStart);
	}

	@Override
	public String toString() {
		if (this.referenceContext != null)
			return "--- Class Scope ---\n\n"  //$NON-NLS-1$
							+ this.referenceContext.binding.toString();
		return "--- Class Scope ---\n\n Binding not initialized" ; //$NON-NLS-1$
	}
//{ObjectTeams: does this class's compulation unit have ignoreFurtherInvestigation set?
	public boolean cuIgnoreFurtherInvestigation() {
		return referenceCompilationUnit() == null || referenceCompilationUnit().ignoreFurtherInvestigation;
	}
// SH}
}
