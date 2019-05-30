package p1;

import p0.C0;

public class C1 extends C0 {
	@Override
	protected String getName(int i) {
		return super.getName(i*2);
	}
	protected String method2(int i) {
		return "nothing";
	}
}
