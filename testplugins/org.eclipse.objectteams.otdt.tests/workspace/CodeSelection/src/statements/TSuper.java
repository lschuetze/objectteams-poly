package statements;

public team class TSuper {
	protected team class Mid1 {
		protected team class Inner1 {
			protected class R1 {
				void m(int a) { }
			}
		}
		protected team class Inner2 {
			protected class R1 {
				void m(int a) { }
			}
		}
	}
	protected team class Mid2 extends Mid1 {
		protected team class Inner1 {
			protected class R1 {
				void m(int a) { }
			}
		}
		protected team class Inner2 extends Inner1 {
			protected class R1 {
				void m(int a) {
					tsuper.m(1);
					Inner2.tsuper.m(2);
				}
			}
		}
	}
}