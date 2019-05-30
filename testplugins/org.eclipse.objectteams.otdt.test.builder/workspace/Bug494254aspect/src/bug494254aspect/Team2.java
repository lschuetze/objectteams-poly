package bug494254aspect;

import base p1.C1;

public team class Team2 {
	protected class R playedBy C1 {
		void getName(int i) <- before String method2(int i);

		private void getName(int i) {
			System.out.println("intercepted");
		}
	}
}
