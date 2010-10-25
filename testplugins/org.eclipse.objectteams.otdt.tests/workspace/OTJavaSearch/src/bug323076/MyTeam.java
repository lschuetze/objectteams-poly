package bug323076;

public team class MyTeam {
	protected class R playedBy MyBase {}
	
	public void foo(MyBase as R r[]) {}
	
	void test() {
		foo(new MyBase[]{ new MyBase() });
	}
}