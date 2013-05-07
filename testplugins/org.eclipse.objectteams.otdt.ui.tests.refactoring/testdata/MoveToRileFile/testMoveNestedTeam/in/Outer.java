package p;

/**
 * The outer team
 * @author stephan
 */
public team class Outer {
	void test1() {
		
	} // end test1()
	
	/**
	 * The inner team
	 */
	protected team class Inner {
		protected class R playedBy OuterRole {
			callin void m() {
				base.m();
			}
			m <- replace bar;
		}
	}
	// internal test2:
	void test2() {
		
	}
	protected class OuterRole {
		String bar() { return ""; }
	}
}