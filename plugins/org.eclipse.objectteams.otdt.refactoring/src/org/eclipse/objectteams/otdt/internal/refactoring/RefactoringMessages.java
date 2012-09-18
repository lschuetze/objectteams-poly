package org.eclipse.objectteams.otdt.internal.refactoring;

import org.eclipse.osgi.util.NLS;

public class RefactoringMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.objectteams.otdt.internal.refactoring.RefactoringMessages"; //$NON-NLS-1$
	public static String ChangeSignatureAdaptor_callinBaseArgDeleteIncomplete_info;
	public static String ChangeSignatureAdaptor_callinRoleArgAddIncomplete_info;
	public static String ChangeSignatureAdaptor_calloutBaseArgAddIncomplete_info;
	public static String ChangeSignatureAdaptor_calloutRoleArgDeleteIncomplete_info;
	public static String ChangeSignatureAdaptor_cannotCreateParamMap_CTF_warning;
	public static String ChangeSignatureAdaptor_cannotCreateParamMap_MultiCallin_warning;
	public static String ChangeSignatureAdaptor_cannotUpdateParameterMapping_warning;
	public static String ChangeSignatureAdaptor_singaturelessBindingIncomplete_info;
	public static String ExtractInterfaceAdaptor_createAsRole_checkbox;
	public static String ExtractInterfaceAdaptor_createInterface_changeName;
	public static String MoveInstanceMethodAdaptor_ambiguousMethodSpec_error;
	public static String MoveInstanceMethodAdaptor_checkOverloading_progress;
	public static String MoveInstanceMethodAdaptor_moveInstanceMethod_name;
	public static String MoveInstanceMethodAdaptor_overloading_error;
	public static String PullUpAdaptor_ambiguousMethodSpec_error;
	public static String PullUpAdaptor_callinMethodToNonRole_error;
	public static String PullUpAdaptor_calloutToNonRole_error;
	public static String PullUpAdaptor_calloutToUnboundRole_error;
	public static String PullUpAdaptor_calloutBaseNotBoundInDest_error;
	public static String PullUpAdaptor_checkOverloading_progress;
	public static String PullUpAdaptor_checkOverriding_progress;
	public static String PullUpAdaptor_checkShadowing_progress;
	public static String PullUpAdaptor_fieldShadowing_error;
	public static String PullUpAdaptor_overloading_error;
	public static String PullUpAdaptor_overriding_error;
	public static String PullUpAdaptor_referencedByMethodBinding_error;
	public static String PullUpAdaptor_referencedCalloutUnresolvedBaseMember_error;
	public static String PushDownAdaptor_ambiguousMethodSpec_error;
	public static String PushDownAdaptor_boundAsCallout_error;
	public static String PushDownAdaptor_boundAsCTF_error;
	public static String PushDownAdaptor_boundInCallin_error;
	public static String PushDownAdaptor_checkShadowing_progress;
	public static String PushDownAdaptor_overloading_error;
	public static String PushDownAdaptor_overloading_progress;
	public static String PushDownAdaptor_overriding_progress;
	public static String PushDownAdaptor_overridingImplicitlyInherited_error;
	public static String PushDownAdaptor_phantomRoleConflict_error;
	public static String PushDownAdaptor_referencedByCallin_error;
	public static String PushDownAdaptor_referencedByCallout_error;
	public static String PushDownAdaptor_referencedByCalloutParamMap_error;
	public static String PushDownAdaptor_referencedByCTF_error;
	public static String PushDownAdaptor_referencedByCTFParamMap_error;
	public static String PushDownAdaptor_referencedByMethod_error;
	public static String PushDownAdaptor_referencedInCallinParamMap_error;
	public static String PushDownAdaptor_shadowing_error;
	public static String RenameMethodAmbuguityMsgCreator_ambiguousMethodSpec_error;
	public static String RenameMethodOverloadingMsgCreator_overloading_error;
	public static String RenameTypeAdaptor_addType_editName;
	public static String RenameTypeAdaptor_changeImplicitTypes_progress;
	public static String RenameTypeAdaptor_newTypeConflic_error;
	public static String RenameTypeAdaptor_overridden_error;
	public static String RenameTypeAdaptor_overriding_error;
	public static String RenameTypeAdaptor_overriding_progress;
	public static String RenameTypeAdaptor_predefinedRoleName_error;
	public static String RenameTypeAdaptor_roleShadowing_error;
	public static String RenameTypeAdaptor_shadowedByRole_error;
	public static String RenameTypeAdaptor_shadowExistingType_error;
	public static String RenameTypeAdaptor_teamCollidesWithPackage_error;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, RefactoringMessages.class);
	}

	private RefactoringMessages() {
	}
}
