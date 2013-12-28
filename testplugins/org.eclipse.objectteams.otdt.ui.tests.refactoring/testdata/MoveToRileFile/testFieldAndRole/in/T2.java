package p;

/**
 * This team is already documented.
 * @author stephan
 */
public team class T2 {

	public final T1 t1;

	void wurd() {
	};

	public T2(T1 t1) {
		this.t1 = t1;
	}

	protected class R2 playedBy R<@t1> {
		void foo() {
		}

		foo <- after rmeth;

		void rmeth() -> void rmeth();
	}
}

team class T1 {
	public class R playedBy MyBase {
		void rmeth() {
		}

		int hasManyArgs(String s, int i, Object o, MyBase b) -> int hasManyArgs(
				String s, int i, Object o, MyBase b);

	}
}
class MyBase {
	String name;

	public MyBase(String name) {
		this.name = name;
	}

	int hasManyArgs(String s, int i, Object o, MyBase b) {
		return 0;
	}
}