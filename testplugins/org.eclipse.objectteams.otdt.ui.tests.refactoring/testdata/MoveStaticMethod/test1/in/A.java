package p;
import java.util.Map;
import java.util.WeakHashMap;
public class A {
	public static int size;
	public static Map m() {
		return new WeakHashMap(size);
	}
}
