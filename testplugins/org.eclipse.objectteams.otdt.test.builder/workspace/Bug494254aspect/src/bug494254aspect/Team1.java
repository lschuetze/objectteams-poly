package bug494254aspect;

import base p1.C1;

public team class Team1 {
	protected class R playedBy C1 {
		void getName(int i) <- before String getName(int i);

		private void getName(int i) {
			System.out.println("intercepted");
		}
	}
}
