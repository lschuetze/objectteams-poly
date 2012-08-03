public team class T {
	protected abstract class RSuper {

		void foo()
		{
			System.out.println(getS1()+getS2());
		}
		
		abstract String getS1();
		
		abstract String getS2();
	}
	protected class RSub extends RSuper playedBy B {
		String getS1() -> get String s;
		String getS2() -> String getS();
	}
}