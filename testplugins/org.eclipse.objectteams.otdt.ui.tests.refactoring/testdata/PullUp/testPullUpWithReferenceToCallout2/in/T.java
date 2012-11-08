public team class T {
	protected class RSuper {
		
	}
	protected class RSub extends RSuper playedBy B {
		String getS1() -> get String s;
		String getS2() -> String getS();
		protected void foo()
		{
			System.out.println(getS1()+getS2());
		}
	}
	
	void main(RSub r) {
		r.foo();
	}
}