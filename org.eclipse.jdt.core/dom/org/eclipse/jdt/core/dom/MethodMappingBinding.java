/**********************************************************************
 * This file is part of "Object Teams Development Tooling"-Software
 *
 * Copyright 2005, 2007 Fraunhofer Gesellschaft, Munich, Germany,
 * for its Fraunhofer Institute for Computer Architecture and Software
 * Technology (FIRST), Berlin, Germany and Technical University Berlin,
 * Germany.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * $Id: MethodMappingBinding.java 23417 2010-02-03 20:13:55Z stephan $
 *
 * Please visit http://www.eclipse.org/objectteams for updates and contact.
 *
 * Contributors:
 * Fraunhofer FIRST - Initial API and implementation
 * Technical University Berlin - Initial API and implementation
 **********************************************************************/
package org.eclipse.jdt.core.dom;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.parser.TerminalTokens;
import org.eclipse.objectteams.otdt.core.compiler.InferenceKind;


/**
 * Internal implementation of callin/callout mapping bindings.
 *
 * @author mkr
 */
class MethodMappingBinding implements IMethodMappingBinding
{

	private org.eclipse.objectteams.otdt.internal.core.compiler.lookup.CallinCalloutBinding binding;
	private BindingResolver resolver;
	private String name;
	private ITypeBinding declaringClass;
    private ITypeBinding baseClass;
    private IMethodBinding roleMethod;
    private IMethodBinding[] baseMethods;
    private IVariableBinding baseField;

	MethodMappingBinding(
            BindingResolver resolver,
            org.eclipse.objectteams.otdt.internal.core.compiler.lookup.CallinCalloutBinding binding)
    {
		this.resolver = resolver;
		this.binding = binding;
	}

	// svenk: added to hold annotations for method getAnnotations()
  private IAnnotationBinding[] annotations;

	/*
	 * @see IBinding#getName()
	 */
	@Override
	public String getName()
    {
		if (this.name == null)
		{
		    this.name = new String(this.binding.readableName());
		}

		return this.name;
	}

	/*
	 * @see IMethodMappingBinding#getDeclaringRoleClass()
	 */
	@Override
	public ITypeBinding getDeclaringRoleClass()
    {
		if (this.declaringClass == null)
        {
			this.declaringClass = this.resolver.getTypeBinding(this.binding._declaringRoleClass);
		}

		return this.declaringClass;
	}

    /*
     * @see IMethodMappingBinding#getReferencedBaseClass()
     */
    @Override
	public ITypeBinding getReferencedBaseClass()
    {
        if (this.baseClass == null)
        {
            this.baseClass = this.resolver.getTypeBinding(this.binding._declaringRoleClass.baseclass());
            //mkr: This is a workaround because _binding.referencedBaseClass is null
            // _baseClass = _resolver.getTypeBinding(_binding._referencedBaseClass);
        }

        return this.baseClass;
    }

    /*
     * @see IMethodMappingBinding#getRoleMethod()
     */
    @Override
	public IMethodBinding getRoleMethod()
    {
        if (this.roleMethod == null)
        {
            this.roleMethod = this.resolver.getMethodBinding(this.binding._roleMethodBinding);
        }

        return this.roleMethod;
    }

    /*
     * @see IMethodMappingBinding#getBaseMethods()
     */
    @Override
	public IMethodBinding[] getBaseMethods()
    {
        if (this.baseMethods == null)
        {
        	MethodBinding[] methodBindings = this.binding._baseMethods;
			if (methodBindings == null)
        		return new IMethodBinding[0];
        	this.baseMethods = new IMethodBinding[methodBindings.length];
        	for (int i = 0; i < methodBindings.length; i++)
				this.baseMethods[i] = this.resolver.getMethodBinding(methodBindings[i]);
        }

        return this.baseMethods;
    }
    
    @Override
	public IVariableBinding getBaseField()
    {
        if (this.baseField == null)
        {
        	FieldBinding fieldBinding = this.binding._baseField;
			if (fieldBinding == null)
        		return null;
			this.baseField = this.resolver.getVariableBinding(fieldBinding);
        }
        return this.baseField;
    }

    @Override
	public String[] getBaseArgumentNames() {
    	MethodBinding[] methodBindings = this.binding._baseMethods;
		if (methodBindings != null && methodBindings.length > 0)
		{
			String[] result = new String[methodBindings[0].parameters.length];
			AbstractMethodDeclaration methodDecl = methodBindings[0].sourceMethod();
			if (methodDecl != null) {
				Argument[] args = methodDecl.arguments;
				if (args != null) {
					for (int i = 0; i < args.length; i++)
						result[i] = String.valueOf(args[i].name);

					return result;
				}
			}
			for (int i = 0; i < result.length; i++)
				result[i] = "arg"+i;  //$NON-NLS-1$
			return result;
		}
    	return new String[0];
    }

	/*
	 * @see IBinding#getKind()
	 */
	@Override
	public int getKind()
    {
		return IBinding.METHOD_MAPPING;
	}

	/*
	 * @see IBinding#getModifiers()
	 */
	@Override
	public int getModifiers()
    {
        if (this.binding.type == org.eclipse.objectteams.otdt.internal.core.compiler.lookup.CallinCalloutBinding.CALLIN)
        {
            switch (this.binding.callinModifier)
            {
                case TerminalTokens.TokenNamebefore:
                    return Modifier.OT_BEFORE_CALLIN;
                case TerminalTokens.TokenNameafter:
                    return Modifier.OT_AFTER_CALLIN;
                case TerminalTokens.TokenNamereplace:
                    return  Modifier.OT_REPLACE_CALLIN;
                default:
                    return 0;
            }
        }
//        else if (_binding.type == org.eclipse.jdt.internal.compiler.lookup.CallinCalloutBinding.CALLOUT)
//        {
//            // As for now, CalloutMappings have no modifier,
//            // since get and set are properties of FieldAccessSpec.
//        }

	    return 0;
	}

	/*
	 * @see IBinding#isDeprecated()
	 */
	@Override
	public boolean isDeprecated()
    {
		return false;
	}

	/**
	 * @see IBinding#isSynthetic()
	 */
	@Override
	public boolean isSynthetic()
    {
		return false;
	}

	@Override
	public boolean isCallin() {
		return this.binding.isCallin();
	}

	/*
	 * @see IBinding#getKey()
	 */
    @Override
	public String getKey()
    {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getDeclaringRoleClass().getKey());
		buffer.append('/');
        buffer.append(this.getName());
		buffer.append(')');

        return buffer.toString();
	}

	/*
	 * For debugging purpose only.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
    {
		return this.binding.toString();
	}

	@Override
	public IJavaElement getJavaElement() {
		// SH: could not find a path that could possibly call this method [26.2.07]
		return null;
	}

	@Override
	public boolean isEqualTo(IBinding other) {
		if (this == other)
			return true;
		if (other == null)
			return false;

		if (!(other instanceof MethodMappingBinding)) {
			// consider a callout as equal to the role method it defines.
			if (!this.isCallin() && this.getRoleMethod().isEqualTo(other))
				return true;
			return false;
		}
		// untested below [06.02.09]
		org.eclipse.objectteams.otdt.internal.core.compiler.lookup.CallinCalloutBinding otherBinding = ((MethodMappingBinding) other).binding;
		if (BindingComparator.isEqual(this.binding, otherBinding))
			return true;
		return false;
	}

// (svenk: implement method from IBinding
  @Override
public IAnnotationBinding[] getAnnotations() {
    if (this.annotations != null) {
      return this.annotations;
    }
    org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding[] annots = this.binding.getAnnotations();
    int length = annots == null ? 0 : annots.length;
    if (annots == null) {
      return this.annotations = AnnotationBinding.NoAnnotations;
    }
    IAnnotationBinding[] domInstances = new AnnotationBinding[length];
    for (int i = 0; i < length; i++) {
      final IAnnotationBinding annotationInstance = this.resolver.getAnnotationInstance(annots[i]);
      if (annotationInstance == null) {
        return this.annotations = AnnotationBinding.NoAnnotations;
      }
      domInstances[i] = annotationInstance;
    }
    return this.annotations = domInstances;
  }
// svenk)

	@Override
	public boolean isRecovered() {
		// method mappings are not (yet) recovered (cf. e.g., DefaultBindingResolver.getVariableBinding())
		return false;
	}



	@Override
	public InferenceKind getInferenceKind() {
		if (this.binding != null)
			return this.binding.inferred;
		return InferenceKind.NONE;
	}

}