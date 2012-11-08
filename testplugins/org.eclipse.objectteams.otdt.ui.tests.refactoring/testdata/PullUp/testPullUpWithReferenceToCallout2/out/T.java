public team class T {
	protected abstract class RSuper {

		protected abstract String getS2();

		protected abstract String getS1();

		protected void foo()
		{
			System.out.println(getS1()+getS2());
		}
		
	}
	protected class RSub extends RSuper playedBy B {
		@Override
		protected String getS1() -> get String s;
		@Override
		protected String getS2() -> String getS();
	}
	
	void main(RSuper r) {
		r.foo();
	}
}