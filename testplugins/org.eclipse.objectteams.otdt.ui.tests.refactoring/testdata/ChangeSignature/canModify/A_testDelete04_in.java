package p;
class A{
	private void m(int i, int x){
	}
}
team class MyTeam {
	protected class R playedBy A {
		void m(int i, int x) <- replace void m(int i, int x);
		callin void m(int i, int x) {
			base.m(i, x);
		}
	}
}