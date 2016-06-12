public class TheBase {
	public void bar() {
		print("bar");
	}
	public static String result = "";
	public static void print(String s) {
		result += s;
		System.out.print(s);
	}
}