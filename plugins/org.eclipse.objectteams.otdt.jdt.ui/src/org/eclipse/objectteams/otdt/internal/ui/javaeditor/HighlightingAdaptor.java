/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 * 
 * Copyright 2006, 2021  Technical University Berlin, Germany, and others.
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
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.objectteams.otdt.internal.ui.javaeditor;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractMethodMappingDeclaration;
import org.eclipse.jdt.core.dom.BaseCallMessageSend;
import org.eclipse.jdt.core.dom.BaseConstructorInvocation;
import org.eclipse.jdt.core.dom.CallinMappingDeclaration;
import org.eclipse.jdt.core.dom.CalloutMappingDeclaration;
import org.eclipse.jdt.core.dom.GuardPredicateDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.LiftingType;
import org.eclipse.jdt.core.dom.MethodBindingOperator;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodSpec;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterMapping;
import org.eclipse.jdt.core.dom.PrecedenceDeclaration;
import org.eclipse.jdt.core.dom.RoleTypeDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TSuperConstructorInvocation;
import org.eclipse.jdt.core.dom.TSuperMessageSend;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeAnchor;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.javaeditor.SemanticHighlighting;
import org.eclipse.jdt.internal.ui.javaeditor.SemanticHighlightingsCore;
import org.eclipse.jdt.internal.ui.javaeditor.SemanticToken;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jdt.ui.text.IJavaColorConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.objectteams.otdt.core.compiler.IOTConstants;
import org.eclipse.objectteams.otdt.internal.ui.text.OutlineAdaptor;
import org.eclipse.swt.graphics.RGB;

import base org.eclipse.jdt.internal.ui.javaeditor.SemanticHighlightingManager;
import base org.eclipse.jdt.internal.ui.javaeditor.SemanticHighlightingManager.Highlighting;
import base org.eclipse.jdt.internal.ui.javaeditor.SemanticHighlightingReconciler;
import base org.eclipse.jdt.internal.ui.javaeditor.SemanticHighlightingReconciler.PositionCollector;
import base org.eclipse.jdt.internal.ui.javaeditor.SemanticHighlightings.ParameterVariableHighlighting;
import base org.eclipse.jdt.internal.ui.javaeditor.SemanticHighlightings.RestrictedIdentifiersHighlighting;

/**
 * This aspect fixes highlighting (syntax & semantic) for OT/J code
 *
 * @author stephan
 * @since 0.9.18
 */
@SuppressWarnings("restriction")
public team class HighlightingAdaptor {

	protected class SemanticHighlightingManager playedBy SemanticHighlightingManager
	{
		void myInstall() 
		{
			updateKWColor();

			//{Hack as workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=142299
			OutlineAdaptor.doActivate();
			// SH}
		}
		void myInstall() 
			<- after void install(JavaEditor je, JavaSourceViewer sv, IColorManager cm, IPreferenceStore ps);

		private void updateKWColor() {
			IPreferenceStore ps = PreferenceConstants.getPreferenceStore();
			RGB kwRgb= PreferenceConverter.getColor(ps, IJavaColorConstants.JAVA_KEYWORD);
			String key = PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_PREFIX + SemanticHighlightingsCore.RESTRICTED_KEYWORDS + PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_COLOR_SUFFIX;
			RGB restrictedKwRgb = PreferenceConverter.getColor(ps, key);
			if (kwRgb.equals(restrictedKwRgb) && kwRgb.equals(new RGB(127, 0, 85))) {
				// restricted keywords share the color of regular keywords, and both are at the default value.
				// -> modify the color for restricted keywords to orange:
				PreferenceConverter.setValue(ps, key, new RGB(245, 121, 0));
			}
		}
	}

	/**
 	 * This role brings syntax highlighting to arguments of method specs.
	 */
	@SuppressWarnings("decapsulation")
	protected class ParameterHighLighting playedBy ParameterVariableHighlighting {

		boolean consumes(SemanticToken token) <- replace boolean consumes(SemanticToken token);

		callin boolean consumes(SemanticToken token) {
			if (base.consumes(token))
				return true;
			IBinding binding= token.getBinding();
			if (binding != null && binding.getKind() == IBinding.VARIABLE && !((IVariableBinding) binding).isField()) {
				ASTNode decl= token.getRoot().findDeclaringNode(binding);
				return decl != null && decl.getLocationInParent() == MethodSpec.PARAMETERS_PROPERTY;
			}
			return false;
		}		
	}
	

	protected team class OTSemanticReconciler playedBy SemanticHighlightingReconciler {

		@SuppressWarnings("basecall")
		protected class Collector implements ILowerable playedBy PositionCollector {

			@SuppressWarnings("decapsulation")
			void addPosition(int offset, int length, HighlightingRole highlighting) 
				-> void addPosition(int offset, int length, Highlighting highlighting);

			boolean visit(PackageDeclaration node) <- replace boolean visit(PackageDeclaration node);
			callin boolean visit(PackageDeclaration node) {
				highlightModifiers(node.modifiers(), ModifierKeyword.TEAM_KEYWORD);
				return true;
			}

			boolean visitType(TypeDeclaration node) <- replace boolean visit(TypeDeclaration node)
				base when (node.isTeam());
			callin boolean visitType(TypeDeclaration node) {
				highlightModifiers(node.modifiers(), ModifierKeyword.TEAM_KEYWORD);
				return true;
			}

			boolean visit(RoleTypeDeclaration node) <- replace boolean visit(RoleTypeDeclaration node);
			callin boolean visit(RoleTypeDeclaration node) {
				if (node.isTeam()) {
					highlightModifiers(node.modifiers(), ModifierKeyword.TEAM_KEYWORD);
				}
				if (node.getPlayedByPosition() != 0) {
					highlightScopedKeyword(node.getPlayedByPosition(), "playedBy".length()); //$NON-NLS-1$
				}
				return true;
			}

			boolean visitMethod(MethodDeclaration node) <- replace boolean visit(MethodDeclaration node)
				base when (isCallin(node));
			callin boolean visitMethod(MethodDeclaration node) {
				highlightModifiers(node.modifiers(), ModifierKeyword.CALLIN_KEYWORD);
				return true;
			}

			@SuppressWarnings("unchecked") // modifiers() has raw type List
			private static boolean isCallin(MethodDeclaration node) {
				return node.modifiers().stream()
						.filter(Modifier.class::isInstance)
						.map(m -> ((Modifier) m).getKeyword())
						.anyMatch(kw -> kw == ModifierKeyword.CALLIN_KEYWORD);
			}

			void highlightModifiers(List<?> modifiers, ModifierKeyword... keywords) {
				for (Object object : modifiers) {
					if (object instanceof Modifier) {
						Modifier modifier = (Modifier) object;
						for (ModifierKeyword kw : keywords) {
							if (modifier.getKeyword() == kw)
								highlightScopedKeyword((Modifier) object);
						}
					}
				}
			}

			boolean visit(MethodBindingOperator node) <- replace boolean visit(MethodBindingOperator node);
			callin boolean visit(MethodBindingOperator operator) {
				Modifier modifier = operator.bindingModifier();
				if (modifier != null) {
					return highlightScopedKeyword(modifier);
				}
				return true;
			}

			<T extends AbstractMethodMappingDeclaration> boolean visit(T node) <- replace 
				boolean visit(CallinMappingDeclaration node),
				boolean visit(CalloutMappingDeclaration node);
			callin <T extends AbstractMethodMappingDeclaration> boolean visit(T node) {
				if (node.getWithKeywordStart() > 0)
					highlightScopedKeyword(node.getWithKeywordStart(), "with".length()); //$NON-NLS-1$
				return true;
			}

			boolean visit(ParameterMapping node) <- replace boolean visit(ParameterMapping node);
			callin boolean visit(ParameterMapping node) {
				ASTNode methodMapping = node.getParent();
				// this paragraph "copied" from ReplaceResultReferenceVisitor
				boolean isResultDir = false;
				if (methodMapping instanceof CallinMappingDeclaration) {
					isResultDir = !node.isBindIN();
					if (!isResultDir 
							&& ((CallinMappingDeclaration) methodMapping).bindingOperator().getBindingModifier() == Modifier.OT_AFTER_CALLIN) {
						isResultDir = true;
					}
				} else {
					isResultDir = node.isBindIN();
				}
				//
				String resultString = String.valueOf(IOTConstants.RESULT);
				if (isResultDir) {
					node.getExpression().accept(
						new ASTVisitor() {
							public boolean visit(SimpleName name) {
								if (resultString.equals(name.getIdentifier())) {
									highlightScopedKeyword(name);
								}
								return false;
							}
						});
				}
				if (resultString.equals(node.getIdentifier().getIdentifier()))
					highlightScopedKeyword(node.getIdentifier());
				return false;
			}

			boolean visit(LiftingType node) <- replace boolean visit(LiftingType node);
			callin boolean visit(LiftingType node) {
				// highlight everything between base type and role type
				Type baseType = node.getBaseType();
				int start = baseType.getStartPosition()+baseType.getLength()+1;
				highlightScopedKeyword(start, node.getRoleType().getStartPosition()-start);
				return false;
			}

			
			boolean visit(BaseCallMessageSend node) <- replace boolean visit(BaseCallMessageSend node);
			callin boolean visit(BaseCallMessageSend node) {
				return highlightScopedKeyword(node.getStartPosition(), 4);
			}

			boolean visit(BaseConstructorInvocation node) <- replace boolean visit(BaseConstructorInvocation node);
			callin boolean visit(BaseConstructorInvocation node) {
				return highlightScopedKeyword(node);
			}

			void visitTSuper(ASTNode node) <- after
					boolean visit(TSuperMessageSend node),
					boolean visit(TSuperConstructorInvocation node);
			void visitTSuper(ASTNode node) {
				highlightScopedKeyword(node.getStartPosition(), "tsuper".length()); //$NON-NLS-1$
			}

			boolean visit(GuardPredicateDeclaration node) <- replace boolean visit(GuardPredicateDeclaration node);
			callin boolean visit(GuardPredicateDeclaration node) {
				if (node.isBase()) {
					highlightScopedKeyword(node.getStartPosition(), 4);
				}
				return highlightScopedKeyword(node.getWhenPosition(), 4);
			}

			boolean visitImport(ImportDeclaration node) <- replace boolean visit(ImportDeclaration node)
				base when (node.isBase());
			callin boolean visitImport(ImportDeclaration node) {
				return highlightScopedKeyword(node.getBaseModifierPosition(), 4);
			}

			boolean visit(TypeAnchor node) <- replace boolean visit(TypeAnchor node);
			callin boolean visit(TypeAnchor node) {
				if (node.getPath() == null) {
					// assumed to be a 'base' anchor
					return highlightScopedKeyword(node);
				}
				return false;
			}

			boolean visit(PrecedenceDeclaration node) <- replace boolean visit(PrecedenceDeclaration node);
			callin boolean visit(PrecedenceDeclaration node) {
				int length = "precedence".length(); //$NON-NLS-1$
				if (node.isAfter())
					length += " after".length(); //$NON-NLS-1$ // Note: this is not exact
				return highlightScopedKeyword(node.getStartPosition(), length);
			}

			/** Apply the 'restricted identifiers' styling to the given node. */
			private boolean highlightScopedKeyword(ASTNode node) {
				return highlightScopedKeyword(node.getStartPosition(), node.getLength());
			}
			private boolean highlightScopedKeyword(int offset, int length) {
				if (offset > -1 && length > 0) {
					SemanticHighlighting[] jobSemanticHighlightings = getFJobSemanticHighlightings();
					if (jobSemanticHighlightings != null) {
						for (int i= 0; i < jobSemanticHighlightings.length; i++) {
							if (typeTester.isRestrictedIdentifierHighlighting(jobSemanticHighlightings[i])) {
								addPosition(offset, length, getFJobHighlighting(i));
								return false;
							}
						}
					}
				}
				return true;
			}

		}

		@SuppressWarnings("decapsulation")
		SemanticHighlighting[] getFJobSemanticHighlightings() -> get SemanticHighlighting[] fJobSemanticHighlightings;

		@SuppressWarnings("decapsulation")
		HighlightingRole getFJobHighlighting(int i) -> get Highlighting[] fJobHighlightings with {
			result <- fJobHighlightings[i]
		}
		
		install <- after install;
		private void install() {
			this.activate(ALL_THREADS);
		}
		uninstall <- after uninstall;
		private void uninstall() {
			this.deactivate(ALL_THREADS);
			HighlightingAdaptor.this.unregisterRole(this);
		}

		/*
		RestrictedHighlighting[] getFJobHighlightings() -> get Highlighting[] fJobHighlightings;
		crash from m.invoke(target, offset, length, getFJobHighlightings()[i]);
		 java.lang.ArrayIndexOutOfBoundsException: Index 2 out of bounds for length 2
	at org.eclipse.objectteams.otdt.internal.core.compiler.statemachine.transformer.InsertTypeAdjustmentsVisitor.endVisit(InsertTypeAdjustmentsVisitor.java:144)
	at org.eclipse.jdt.internal.compiler.ast.MessageSend.traverse(MessageSend.java:1790)
	at org.eclipse.jdt.internal.compiler.ast.Block.resolveUsing(Block.java:184)
	at org.eclipse.jdt.internal.compiler.ast.TryStatement.resolve(TryStatement.java:1235)
	at org.eclipse.jdt.internal.compiler.ast.Block.resolve(Block.java:149)
	at org.eclipse.jdt.internal.compiler.ast.IfStatement.resolveIfStatement(IfStatement.java:294)
	at org.eclipse.jdt.internal.compiler.ast.IfStatement.resolve(IfStatement.java:320)
	at org.eclipse.jdt.internal.compiler.ast.Block.resolve(Block.java:149)
	at org.eclipse.jdt.internal.compiler.ast.ForeachStatement.resolve(ForeachStatement.java:723)
	at org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration.resolveStatements(AbstractMethodDeclaration.java:977)
		 */
	}

	RestrictedIdentifiersHighlightingRole typeTester = new RestrictedIdentifiersHighlightingRole();

	@SuppressWarnings("decapsulation") // overrides finalness
	protected class RestrictedIdentifiersHighlightingRole implements ILowerable playedBy RestrictedIdentifiersHighlighting {
		Class<?> restrictedIdentifiersHighlightingClass;
		protected RestrictedIdentifiersHighlightingRole() {
			base();
			ILowerable role = this;
			restrictedIdentifiersHighlightingClass = role.lower().getClass(); // this class cannot be used in source-level instanceof checks!
		}
		public boolean isRestrictedIdentifierHighlighting(SemanticHighlighting highlighting) {
			return restrictedIdentifiersHighlightingClass.isInstance(highlighting);
		}
	}

	/** Empty role as handle for unmentionable base class. */
	protected class HighlightingRole playedBy Highlighting { /* empty */ }
}
