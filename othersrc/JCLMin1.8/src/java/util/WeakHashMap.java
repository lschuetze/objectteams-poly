package java.util;

public class WeakHashMap<K, V> implements Map<K, V> {
	public WeakHashMap() { }
	public WeakHashMap(int initialCapacity) { }
	public V put(K k, V v) { return v; }
	public void clear() {}
	public boolean isEmpty() { return true; }
	public boolean containsKey(Object k) { return true; }
	public V get(Object o) { return null; }
	public V remove(Object o) { return null; }
}
