package p;

import java.util.Map;
import java.util.WeakHashMap;

class B{

	public static Map m() {
		return new WeakHashMap(A.size);
	}
}