package aspect;

import base base537350_b.FindReferencesAction;
import base base537350_b.FindDeclarationsAction;
import base base537350_b.FindImplementersAction;
import base base537350_b.FindAction;

public team class FindActionAdaptor {

	protected class FindAction playedBy FindAction {
		protected String getString() { return "RFindAction"; }
	}
	protected class RFindReferencesAction extends FindAction playedBy FindReferencesAction {
		@SuppressWarnings("decapsulation")
		get <- replace get;
		callin void get(StringBuilder result) {
			result.append(getString());
			result.append('-');
			base.get(result);
			result.append('-');
			result.append("RFindReferencesAction");
		}
	}
	protected class RFindDeclarationsAction extends FindAction playedBy FindDeclarationsAction {
		@SuppressWarnings("decapsulation")
		get <- replace get;
		callin void get(StringBuilder result) {
			result.append(getString());
			result.append('-');
			base.get(result);
			result.append('-');
			result.append("RFindDeclarationsAction");
		}
	}
	protected class RFindImplementersAction extends FindAction playedBy FindImplementersAction {
		@SuppressWarnings("decapsulation")
		get <- replace get;
		callin void get(StringBuilder result) {
			result.append(getString());
			result.append('-');
			base.get(result);
			result.append('-');
			result.append("RFindImplementersAction");
		}
	}
}
