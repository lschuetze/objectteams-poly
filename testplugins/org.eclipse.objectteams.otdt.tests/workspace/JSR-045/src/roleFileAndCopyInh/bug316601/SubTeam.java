package roleFileAndCopyInh.bug316601;

import roleFileAndCopyInh.bug316601.base.Base2;

/**
 * @role Role2
 */
public team class SubTeam extends SuperTeam {
	SubTeam(Base2 as Role2 r) {
		System.out.println(r);
	}
	public static void main(String[] args) {
		Base2 b = new Base2();
		new SubTeam(b);
	}
}