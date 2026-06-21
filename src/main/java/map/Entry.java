package map;

public class Entry<K extends Comparable<K>, V>
		implements Comparable<Entry<K,V>> {
// ist das obige korrekt??? Nein, da die Sortierung von Map ausschließlich über Schlüssel K erfolgen soll

	public final K key;
	public final V value;

	@Override
	public int compareTo(Entry<K,V> other) {
		return this.key.compareTo(other.key);
	}

	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Entry<?, ?> other)) return false;
		return key.equals(other.key);
	}

}