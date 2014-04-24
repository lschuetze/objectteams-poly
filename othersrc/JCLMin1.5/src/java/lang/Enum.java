package java.lang;

public abstract class Enum<T extends Enum<T>> implements Comparable<T>, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	protected Enum(String name, int ordinal) {
	}
	public final String name() {
		return null;
	}
	public final int ordinal() {
		return 0;
	}
}