/*******************************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2006, 2015 Technical University Berlin, Germany and others.
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
 *     Technical University Berlin - Initial API and implementation
 *     IBM Corporation - copies of individual methods from bound base classes.
 *******************************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.viewsupport;

import java.util.StringTokenizer;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.objectteams.otdt.core.IMethodMapping;
import org.eclipse.objectteams.otdt.core.IOTJavaElement;
import org.eclipse.objectteams.otdt.core.IOTType;
import org.eclipse.objectteams.otdt.core.IRoleType;
import org.eclipse.objectteams.otdt.core.OTModelManager;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.internal.core.MethodMapping;
import org.eclipse.objectteams.otdt.internal.core.OTJavaElement;
import org.eclipse.objectteams.otdt.internal.core.OTType;
import org.eclipse.objectteams.otdt.internal.ui.Messages;
import org.eclipse.objectteams.otdt.ui.ImageManager;
import org.eclipse.osgi.util.NLS;



import static org.eclipse.jdt.ui.JavaElementLabels.PREPEND_ROOT_PATH;
import static org.eclipse.jdt.ui.JavaElementLabels.ROOT_QUALIFIED;
import static org.eclipse.jdt.ui.JavaElementLabels.COLORIZE;
import static org.eclipse.jdt.ui.JavaElementLabels.CONCAT_STRING;
import static org.eclipse.jdt.ui.JavaElementLabels.M_POST_QUALIFIED;
import static org.eclipse.jdt.ui.JavaElementLabels.T_FULLY_QUALIFIED;
import static org.eclipse.jdt.ui.JavaElementLabels.M_FULLY_QUALIFIED;
import static org.eclipse.objectteams.otdt.internal.ui.viewsupport.DummyDecorator.*;
import static org.eclipse.objectteams.otdt.ui.ImageConstants.*;

import org.eclipse.jdt.internal.compiler.lookup.ExtraCompilerModifiers;
import org.eclipse.jdt.internal.core.manipulation.JavaElementLabelComposerCore.FlexibleBufferCore;
import org.eclipse.jdt.internal.corext.refactoring.util.JavaElementUtil;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementLabelComposer.FlexibleBuffer;
import org.eclipse.jdt.ui.text.IJavaColorConstants;

import base org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import base org.eclipse.jdt.internal.ui.viewsupport.JavaElementLabelComposer;
import base org.eclipse.jdt.internal.ui.InterfaceIndicatorLabelDecorator;
import base org.eclipse.jdt.ui.JavaElementImageDescriptor;
import base org.eclipse.jdt.ui.JavaElementLabels;
import base org.eclipse.jdt.ui.ProblemsLabelDecorator;

/**
 * Adapt icon presentation in the jdt ui to reflect also teams and roles.
 * Also callin methods and method mappings are decorated appropriately.
 * 
 * One method contains code copied from its base method.
 *
 * @author stephan
 */
@SuppressWarnings({"restriction","decapsulation","basecall"})
public team class ViewAdaptor extends JFaceDecapsulator
{
	/** new, OT-specific adornment flag for methods. */
	final static int CALLIN = 0x40000000; // can't use 0x80000000 due to Assert.isTrue(fFlags >= 0) in JavaElementImageDescriptor()

	/** new, OT-specific adornment flag for types. */
	final static int BOUND_ROLE = 0x20000000; // must not clash with other bits in JavaElementImageDescriptor

	// use this instance for lookup which decorations are enabled:
	IDecoratorManager decoratorMgr = null;
	
	/**
	 * This role extends the existing label provider with capabilities to show
	 * specific icons for OT elements.
	 * 
	 * Technical note: in general the WorkbenchAdapter should take care of providing
	 * labels and images. This role handles only exceptional cases:
	 * - invocations bypassing getBaseImageDescriptor().
	 * - adornment computation does not use an adapter
	 */
	protected class ImageProviderAdaptor playedBy JavaElementImageProvider
	{
		public ImageProviderAdaptor(JavaElementImageProvider javaElementImageProvider) {
			// lazy init of team field:
			if (decoratorMgr == null)
				decoratorMgr= PlatformUI.getWorkbench().getDecoratorManager();
		}
		
		/**
		 * Role and Team images for a few more views (based on flags not element).
		 * 
		 * Technical note: getTypeImageDescriptor et al. are called by
		 * numerous clients which do not use the IWorkbenchAdapter for rendering,
		 * so we indeed have to adapt this method despite the use if IWorkbenchAdapter in
		 * getBaseImageDescriptor.
		 */
		static callin ImageDescriptor getClassImageDescriptor(int flags, boolean useLightIcons) {
			String img = null;
			if (Flags.isRole(flags)) {
				if (useLightIcons) {
					img = ROLE_ALT_IMG;
				} else {
					if (Flags.isTeam(flags)) {
						if (Flags.isProtected(flags)) 
							img = TEAM_ROLE_PROTECTED_IMG;
						else
							img = TEAM_ROLE_IMG;
					} else {
						if (Flags.isProtected(flags))
							img = ROLECLASS_PROTECTED_IMG;
						else
							img = ROLECLASS_IMG;
					}
				}
			} else if (Flags.isTeam(flags)) {
				if (useLightIcons)
					img = TEAM_ALT_IMG;
				else
					img = TEAM_IMG;
			}
			if (img != null)
				return ImageManager.getSharedInstance().getDescriptor(img);
			return base.getClassImageDescriptor(flags, useLightIcons);
		}
		// Overriding different base methods using appropriate parameter mappings:
		ImageDescriptor getClassImageDescriptor(int flags, boolean useLightIcons) // FIXME(SH): combining this callin with the one below gives an exception inside the OTRE
			<- replace ImageDescriptor getClassImageDescriptor(int flags)
			with { flags  <- flags, useLightIcons <- false }
		ImageDescriptor getClassImageDescriptor(int flags, boolean useLightIcons)
		<- replace ImageDescriptor getInnerClassImageDescriptor(boolean a1, int flags)
			with { flags  <- flags, useLightIcons <- false }
		ImageDescriptor getClassImageDescriptor(int flags, boolean useLightIcons) 
			<- replace ImageDescriptor getTypeImageDescriptor(boolean isInner, boolean isInInterfaceOrAnnotation, int flags, boolean useLightIcons) 
			with { flags  <- flags, useLightIcons <- useLightIcons }

		callin ImageDescriptor getBaseImageDescriptor(IJavaElement element) {
			String name = element.getElementName();
			if (name.startsWith(String.valueOf(IOTConstants.BASE_PREDICATE_PREFIX))) {
				return ImageManager.getSharedInstance().getDescriptor(BASEGUARD_IMG);
			} else if (name.startsWith(String.valueOf(IOTConstants.PREDICATE_METHOD_NAME))) {
				return ImageManager.getSharedInstance().getDescriptor(GUARD_IMG);
			}
			return base.getBaseImageDescriptor(element);
		}

		ImageDescriptor getBaseImageDescriptor(IJavaElement element) 
			<- replace ImageDescriptor getJavaImageDescriptor(IJavaElement element, int renderFlags)
			base when (element.getElementType() == IJavaElement.METHOD);
		
		/**
		 * Add OT-specific adornment flags: CALLIN and BOUND_ROLE 
		 */
		callin int computeJavaAdornmentFlags(IJavaElement element, int renderFlags) 
		{
			int flags = base.computeJavaAdornmentFlags(element, renderFlags);
			if (showOverlayIcons(renderFlags) && element instanceof IMember) {
				try {
					if (element instanceof IMethod) {
						if ((((IMethod)element).getFlags() & Flags.AccCallin) != 0) 
							flags |= CALLIN; 
					} else if (element instanceof IType) {
						IOTType otType = OTModelManager.getOTElement((IType)element);
						if (otType != null && otType.isRole()) {
							IRoleType roleType = (IRoleType)otType;
							if (isBoundRole(roleType))
								flags |= BOUND_ROLE;
							if (isOverriding(roleType))
								flags |= org.eclipse.jdt.ui.JavaElementImageDescriptor.OVERRIDES;
						}
					}
				} catch (JavaModelException ex) {
					// do nothing. Can't compute runnable adornment or get flags
				}
			}
			return flags;
		}
		/** Overriding. */
		computeJavaAdornmentFlags <- replace computeJavaAdornmentFlags;
		
		boolean isBoundRole(IRoleType roleType) throws JavaModelException {
			if (!decoratorMgr.getEnabled(BOUND_ROLE_DECORATOR_ID))
				return false;
			return roleType.getBaseClass() != null;  // getBaseClass() also finds superBaseclass
		}
		
		boolean isOverriding(IRoleType roleType) throws JavaModelException {
			if (!decoratorMgr.getEnabled(OVERRIDING_ROLE_DECORATOR_ID))
				return false;							 // disabled
			if ((roleType.getFlags() & ExtraCompilerModifiers.AccOverridingRole) != 0)
				return true;							 // stored flag
			return roleType.getTSuperRoles().length > 0; // need to compute
		}
		
		/**
		 * Refine strategy to determine if an abstract marker should be added.
		 * OT_COPY_PASTE from this role's base class.
		 */
		static callin boolean confirmAbstract(IMember element) throws JavaModelException
		{
			// never show the abstract symbol on interfaces
			int elementType = element.getElementType();
			if (elementType == IJavaElement.TYPE
// {OTDTUI :
				|| elementType == OTJavaElement.TEAM
				|| elementType == OTJavaElement.ROLE) {
// haebor}
				return ! JavaModelUtil.isInterfaceOrAnnotation((IType) element);
			}
			return true;
		}
		/** Overriding. */
		confirmAbstract <- replace confirmAbstract;
		
		// ===== callout: =====
		boolean showOverlayIcons(int flags) -> boolean showOverlayIcons(int flags);
	}
	
	protected class JavaElementLabels playedBy JavaElementLabels 
	{
		// copies from JavaElementLabelComposer:
		//   - constant:
		final static long QUALIFIER_FLAGS= org.eclipse.jdt.ui.JavaElementLabels.P_COMPRESSED | org.eclipse.jdt.ui.JavaElementLabels.USE_RESOLVED;
		//   - helper function:
		static final boolean getFlag(long flags, long flag) {
			return (flags & flag) != 0;
		}
		
		// callouts:
		void getTypeLabel(IType type, long flags, StringBuffer result) 
			-> void getTypeLabel(IType type, long flags, StringBuffer result);
		void getTypeLabel(IType type, long flags, StyledString result) 
			-> void getTypeLabel(IType type, long flags, StyledString result);
		void getPackageFragmentRootLabel(IPackageFragmentRoot pack, long flags, StyledString result) 
			-> void getPackageFragmentRootLabel(IPackageFragmentRoot pack, long flags, StyledString result);
			
			
		/** If buf holds a name containing __OT__ remove that part. */
		static void beautifyOTLabel(StringBuffer buf) {
			String value = buf.toString();
			int start = value.lastIndexOf('.');
			if (start < 0)
				start = 0;
			else
				start = start + 1;
			if (value.startsWith(IOTConstants.OT_DELIM, start))
				buf.delete(start, start+IOTConstants.OT_DELIM_LEN);
		}
		void beautifyOTLabel(StringBuffer buf) <- after void getTypeLabel(IType type, long flags, StringBuffer buf) 
			with { buf <- buf }
		// for constructors:
		void beautifyOTLabel(StringBuffer buf) <- after void getMethodLabel(IMethod method, long flags, StringBuffer buf) 
			with { buf <- buf }

		/** Qualification for method mappings. */
		void getElementLabel(IJavaElement element, long flags, StringBuffer buf) 
		<- replace void getElementLabel(IJavaElement element, long flags, StringBuffer buf);

		static callin void getElementLabel(IJavaElement element, long flags, StringBuffer buf) 
		{
			if (element instanceof IMethodMapping) {
				// base method only uses elementName(), may need to prepend qualification:
				if (getFlag(flags, M_FULLY_QUALIFIED)) {
					new org.eclipse.jdt.internal.ui.viewsupport.JavaElementLabelComposer(buf)
						.appendTypeLabel(((IMethodMapping)element).getDeclaringType(), T_FULLY_QUALIFIED | (flags & QUALIFIER_FLAGS));
					buf.append('.');
				}				
			}
			base.getElementLabel(element, flags, buf);
			
			if (element instanceof IMethodMapping) {
				// to this point only elementName() is used, may need to append qualification:
				if (getFlag(flags, M_POST_QUALIFIED)) {
					buf.append(CONCAT_STRING);
					getTypeLabel(((IMethodMapping)element).getDeclaringType(),  T_FULLY_QUALIFIED | (flags & QUALIFIER_FLAGS), buf);
				}
			}
		}

		/** Styled labels for method mappings. */
		void getElementLabel(IJavaElement element, long flags, StyledString res)
		<- replace void getElementLabel(IJavaElement element, long flags, StyledString res);
		
		static callin void getElementLabel(IJavaElement element, long flags, StyledString result) {
			int type= element.getElementType();
			switch (type) {
			case IOTJavaElement.CALLIN_MAPPING:
			case IOTJavaElement.CALLOUT_MAPPING:
			case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
				break; // implementation is below
			default:
				base.getElementLabel(element, flags, result);
				return;
			}
			
			IPackageFragmentRoot root= null;
			
			if (type != IJavaElement.JAVA_MODEL && type != IJavaElement.JAVA_PROJECT && type != IJavaElement.PACKAGE_FRAGMENT_ROOT)
				root= JavaModelUtil.getPackageFragmentRoot(element);
			if (root != null && getFlag(flags, PREPEND_ROOT_PATH)) {
				getPackageFragmentRootLabel(root, ROOT_QUALIFIED, result);
				result.append(CONCAT_STRING);
			}
			getMethodMappingLabel((IMethodMapping)element, flags, result);
		}

		private static void getMethodMappingLabel(IMethodMapping element, long flags, StyledString result) {

			result.append(element.getElementName());

			// post qualification
			if (getFlag(flags, M_POST_QUALIFIED)) {
				int offset= result.length();
				result.append(CONCAT_STRING);
				getTypeLabel(element.getDeclaringType(), T_FULLY_QUALIFIED | (flags & QUALIFIER_FLAGS), result);
				if (getFlag(flags, COLORIZE)) {
					result.setStyle(offset, result.length() - offset, StyledString.QUALIFIER_STYLER);
				}
			}
		}
	}
	
	/** Helper for {@link JavaElementLabelComposer#beautifyGuardLabel(IMethod,long)} retrieve and cache keyword color. */
	Color keywordColor = null;
	Color getKeywordColor() {
		if (keywordColor == null) {
			RGB rgb= PreferenceConverter.getColor(JavaPlugin.getDefault().getPreferenceStore(), IJavaColorConstants.JAVA_KEYWORD);
			keywordColor = new Color(Display.getCurrent(), rgb);
		}
		return keywordColor;
	}
	
	protected class JavaElementLabelComposer playedBy JavaElementLabelComposer {

		FlexibleBufferCore getFBuffer() -> get FlexibleBufferCore fBuffer;
		
		void beautifyGuardLabel(IMethod method, long flags) <- replace void appendMethodLabel(IMethod method, long flags);

		callin void beautifyGuardLabel(IMethod method, long flags) {
			String name = method.getElementName();
			String displayName = null;
			String guardedElement = null;
			if (name.startsWith(String.valueOf(IOTConstants.BASE_PREDICATE_PREFIX))) {
				displayName = "base when"; //$NON-NLS-1$
				guardedElement = getGuardString(name.substring(IOTConstants.BASE_PREDICATE_PREFIX.length), method); 
			} else if (name.startsWith(String.valueOf(IOTConstants.PREDICATE_METHOD_NAME))) {
				displayName = "when"; //$NON-NLS-1$
				guardedElement = getGuardString(name.substring(IOTConstants.PREDICATE_METHOD_NAME.length), method); 
			}
			if (displayName != null) {
				// displayName as keyword:
				FlexibleBufferCore buffer = this.getFBuffer();
				final Color keywordColor = ViewAdaptor.this.getKeywordColor();
				int offset = buffer.length();
				buffer.append(displayName);
				if (buffer instanceof FlexibleBuffer) {
					((FlexibleBuffer) buffer).setStyle(offset, displayName.length(), new StyledString.Styler() {
						@Override public void applyStyles(TextStyle textStyle) {
							textStyle.foreground = keywordColor;
						}
					});
				}
				// append explanation:
				offset = buffer.length();
				String qualifier = NLS.bind(Messages.ViewAdaptor_guard_predicate_postfix,
					    			new Object[]{guardedElement});
				buffer.append(qualifier);
				if (buffer instanceof FlexibleBuffer) {
					((FlexibleBuffer) buffer).setStyle(offset, qualifier.length(), StyledString.QUALIFIER_STYLER);
				}
			} else {
				base.beautifyGuardLabel(method, flags);
			}
		}
		private static String getGuardString(String suffix, IMethod method) {
			if (suffix.length() == 0)
				return method.getDeclaringType().getElementName();
			StringTokenizer tokens = new StringTokenizer(suffix, "$"); //$NON-NLS-1$
			String roleSelector = tokens.nextToken();
			if (!tokens.hasMoreTokens())
				return roleSelector; // method guard
			tokens.nextToken(); // modifier, unused
			String baseSelector = tokens.nextToken();
			return roleSelector+"<-"+baseSelector; //$NON-NLS-1$
		}
	}
	
	/**
	 * This role extends "Java Type Indicators" (icons overlayed on CUD-icons)
	 * to also support teams and roles.
	 */
	protected class OTClassLabelDecorator playedBy InterfaceIndicatorLabelDecorator
	{
		PretendAllRoleFilesArePublic pretendAllRoleFilesArePublic = new PretendAllRoleFilesArePublic();
		
		/** Overriding. */
		addOverlays <- replace addOverlays;
		callin void addOverlays(Object element, IDecoration decoration) throws JavaModelException
		{		
			IType type = getMainType(element);
			if (type == null)
				// base uses index search, needs a little help from us ;-)
				within (pretendAllRoleFilesArePublic) {
					base.addOverlays(element, decoration);
					return;
				}
			IOTType otType = OTModelManager.getOTElement(type);
			if (otType != null) {
				String img = null;
				if (otType.isRole()) {
					if (otType.isTeam())
						img = TEAM_ROLE_OVR;
					else
						img = ROLE_OVR;
				} else if (otType.isTeam()) {
					img = TEAM_OVR;
				}
				if (img != null) {
					decoration.addOverlay(ImageManager.getSharedInstance().getDescriptor(img), IDecoration.TOP_RIGHT);
					return;
				}
			}
			base.addOverlays(type, decoration);
		}

		/* Overriding */
		addOverlaysFromFlags <- replace addOverlaysFromFlags;
		callin void addOverlaysFromFlags(int flags, IDecoration decoration) {
			// FIXME(SH): this might be incomplete: do flags always contain role/team??
			String img = null;
			if (Flags.isRole(flags)) {
				if (Flags.isTeam(flags))
					img = TEAM_ROLE_OVR;
				else
					img = ROLE_OVR;
			} else if (Flags.isTeam(flags)) {
				img = TEAM_OVR;
			}
			if (img != null) {
				decoration.addOverlay(ImageManager.getSharedInstance().getDescriptor(img), IDecoration.TOP_RIGHT);
				return;
			}
			base.addOverlaysFromFlags(flags, decoration);
		}
		
		/** By use of the DecoratorManagerAdaptor we may indeed receive package fragments,
		 *  which the base plugin doesn't expect here (argument `element'). 
		 *  So we have to adapt the query for the element's main type: 
		 */
		IType getMainType(Object element) throws JavaModelException 
		{
			// if element is a team package it's main type is the team type.
			if (element instanceof IPackageFragment) {
				ICompilationUnit teamUnit = TeamPackageUtil.getTeamUnit((IPackageFragment)element);
				IType teamType = JavaElementUtil.getMainType(teamUnit);
				if (teamType != null)
					return teamType;
			}
			return null;
		}
	} 
	
	/**
	 * This role draws the CALLIN adornment if requested.
	 */
	protected class ElementImageDescriptor extends CompositeImageDescriptor playedBy JavaElementImageDescriptor
	{
		private int deltaX = 0;   // adjustment to pass into the JDT/UI
		
		/** Add two more adornments while drawing: callin (for methods) and bound (for roles). */
		drawTopRight <- replace drawTopRight
			base when ((base.getAdronments() & (BOUND_ROLE|CALLIN)) != 0);
		callin void drawTopRight() {
			int flags = getAdornments();
			// when drawing right-to-left start with our adornments:
			ImageDescriptor adornment = null;
			
			if ((flags & BOUND_ROLE) != 0)
				adornment = ImageManager.getSharedInstance().getDescriptor(BOUNDROLE_IMG);
			else if ((flags & CALLIN) != 0)
				adornment = ImageManager.getSharedInstance().getDescriptor(CALLINMETHOD_IMG);

			if (adornment != null)
				addTopRightImage(adornment, new Point(getSize().x, 0));
			
			base.drawTopRight(); // during this call adjustX<-addTopRightImage is still active.
		}

		void adjustX(ImageDescriptor desc, Point pos) <- before void addTopRightImage(ImageDescriptor desc, Point pos)
			base when (ViewAdaptor.this.isExecutingCallin()); // only during drawTopRight callin
		private void adjustX(ImageDescriptor desc, Point pos) {
			if (deltaX != 0) {
				pos.x -= deltaX; // don't overwrite the image we have drawn in drawTopRight()
				deltaX = 0;
			}
		}
		
		// ===== callout interface: =====
		int getAdornments()                                    -> int getAdronments(); // note: typo in jdt.ui
		Point getSize()           						       -> Point getSize();
		void addTopRightImage(ImageDescriptor desc, Point pos) -> void addTopRightImage(ImageDescriptor desc, Point pos);
	}
	
	/** Add error/warning decorations to OT elements: */
	protected class ProblemsLabelDecorator playedBy ProblemsLabelDecorator 
	{
		callin int computeAdornmentFlags(Object obj) 
		{
			class FakedMethodMapping extends MethodMapping {
				FakedMethodMapping (MethodMapping orig) {
					super(orig.getDeclarationSourceStart(), orig.getSourceStart(), 
						  orig.getSourceEnd(),  			orig.getDeclarationSourceEnd(), 
						  IJavaElement.METHOD, // <-- pretend to be a method 
						  (IMethod)orig.getCorrespondingJavaElement(), 
						  (IType)orig.getParent(), 
						  orig.getRoleMethodHandle(), 
						  orig.hasSignature(), /*addAsChild*/false);
				}
				public int getMappingKind()                     { return 0; /* don't care.*/ }
				public OTJavaElement resolved(char[] uniqueKey) { return null; /* don't care*/ }
				protected void getBaseMethodsForHandle(StringBuffer buff) { /* don't care */}
				protected char getMappingKindChar() { return 'o'; }
				protected IRoleType getDeclaringRole() { return null; }
			}
			
			class FakedType extends OTType {
				FakedType(OTType orig) {
					super(IJavaElement.TYPE, // <-- pretend to be a regular type 
						  (IType)orig.getCorrespondingJavaElement(), 
						  orig.getParent(), orig.getFlags(),
						  false); // don't add to the parent, element should not be persistent!
				}
			}
	
			if (obj instanceof IJavaElement) {
				IJavaElement element= (IJavaElement) obj;
				int type= element.getElementType();
				switch (type) {
				case IOTJavaElement.CALLOUT_MAPPING:
				case IOTJavaElement.CALLOUT_TO_FIELD_MAPPING:
				case IOTJavaElement.CALLIN_MAPPING:
					obj = new FakedMethodMapping((MethodMapping)obj);
					break;
				case IOTJavaElement.TEAM:
				case IOTJavaElement.ROLE:
					// we indeed see these types:
					// - when rendering breadcrumb parents for method mappings
					// - when rendering the type of a role file in the package explorer
					obj = new FakedType((OTType)obj);
					break;
				}
			}
			return base.computeAdornmentFlags(obj);
		}
		computeAdornmentFlags <- replace computeAdornmentFlags;
	}
}
