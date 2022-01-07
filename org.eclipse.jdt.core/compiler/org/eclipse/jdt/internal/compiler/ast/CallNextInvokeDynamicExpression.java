package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.objectteams.otdt.internal.core.compiler.ast.BaseReference;

public class CallNextInvokeDynamicExpression extends Expression {

	public BaseReference base;
	public MethodBinding callinMethodBinding;
	public MethodBinding codegenBinding;

	public CallNextInvokeDynamicExpression(BaseReference base, MethodBinding binding, MethodBinding codegenBinding) {
		this.base = base;
		this.callinMethodBinding = binding;
		this.codegenBinding = codegenBinding;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.base == null) ? 0 : this.base.hashCode());
		result = prime * result + ((this.callinMethodBinding == null) ? 0 : this.callinMethodBinding.hashCode());
		result = prime * result + ((this.codegenBinding == null) ? 0 : this.codegenBinding.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CallNextInvokeDynamicExpression other = (CallNextInvokeDynamicExpression) obj;
		if (this.base == null) {
			if (other.base != null)
				return false;
		} else if (!this.base.equals(other.base))
			return false;
		if (this.callinMethodBinding == null) {
			if (other.callinMethodBinding != null)
				return false;
		} else if (!this.callinMethodBinding.equals(other.callinMethodBinding))
			return false;
		if (this.codegenBinding == null) {
			if (other.codegenBinding != null)
				return false;
		} else if (!this.codegenBinding.equals(other.codegenBinding))
			return false;
		return true;
	}

	@Override
	public StringBuffer printExpression(int indent, StringBuffer output) {
		// TODO Auto-generated method stub
		return null;
	}

}
