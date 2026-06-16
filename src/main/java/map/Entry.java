package map;

public class Entry<K extends Comparable<K>, V>
		implements Comparable<Entry<K,V>> {
// ist das obige korrekt??? Nein, da die Sortierung von Map ausschließlich über Schlüssel K erfolgen soll

	public final K key;
	public final V value;

	@Override
	public int compareTo(Entry<K,V> other) {
		return 0;
	}

	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
	}

}