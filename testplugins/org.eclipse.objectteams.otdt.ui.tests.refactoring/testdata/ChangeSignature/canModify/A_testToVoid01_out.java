class MyBase {
	int hasManyArgs(String s, int i, Object o, MyBase b) { return 0; }
}

public team class MyTeam {
	protected class A playedBy MyBase {

		void m(String string, int i, Object object, A myRole) <- after int hasManyArgs(String s, int i, Object o, MyBase b);

		private void m(String string, int i, Object object, A myRole) {
			return 0;
		}
	}
}