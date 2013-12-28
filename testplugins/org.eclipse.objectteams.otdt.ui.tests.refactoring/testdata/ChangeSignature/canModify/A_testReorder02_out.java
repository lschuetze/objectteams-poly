class MyBase {
	int hasManyArgs(String s, int i, Object o, MyBase b) { return 0; }
}

public team class MyTeam {
	protected class A playedBy MyBase {

		int m(String string, Object object, int i, A myRole) <- after int hasManyArgs(String s, int i, Object o, MyBase b) with {
			string <- s,
			object <- o,
			i <- i,
			myRole <- b
		}

		private int m(String string, Object object, int i, A myRole) {
			return 0;
		}
	}
}