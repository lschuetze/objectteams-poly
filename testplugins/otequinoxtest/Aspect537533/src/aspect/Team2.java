package aspect;

import base537533.JavaTypeCompletionProposal;

import base base537533.MethodDeclarationCompletionProposal;

public team class Team2 {
	protected class RMethodDeclarationCompletionProposal playedBy MethodDeclarationCompletionProposal {
		void setReplacementString(StringBuilder result) <- replace void setReplacementString(StringBuilder result);

		@SuppressWarnings("basecall")
		callin void setReplacementString(StringBuilder result) {
			result.append("RMethodDeclarationCompletionProposal");
		}
	}
	protected class OverrideRoleCompletionProposal extends JavaTypeCompletionProposal { // unbound
		protected void run(StringBuilder result) {
			setReplacementString(result);
		}
	}
	public void run(StringBuilder result) {
		new OverrideRoleCompletionProposal().run(result);
	}
}
