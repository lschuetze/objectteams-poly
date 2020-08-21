package java.lang;

public class ThreadLocal<T> {
	protected synchronized T initialValue() { return null; }
	public void set(T t) {}
	public T get() { return null; }
}
