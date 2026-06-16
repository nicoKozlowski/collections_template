package map;

import fpinjava.Function;
import list.List;
import set.Set;
import set.SortedSet;
import set.TreeSet;
import tuple.Tuple;



public class TreeMap<K extends Comparable<K>,V> implements SortedMap<K,V> {


	private final set.SortedSet<Entry<K,V>> set;


	private TreeMap() {
		this.set = TreeSet.empty();
	}

	@SuppressWarnings("unchecked")
	private TreeMap(SortedSet<Entry<K, V>> set){
        this.set = set;
    }


	public static <K extends Comparable<K>,V> SortedMap<K,V> empty() {
		return new TreeMap<>();
	}

	public static <K extends Comparable<K>,V>
	SortedMap<K,V> fromList(List<Tuple<K,V>> list) {

		SortedMap<K,V> map = TreeMap.empty();

		return list.foldl(
				acc -> t -> acc.insert(t.fst, t.snd),
				map
		);
	}

	@Override
	public boolean isEqualTo(SortedMap<K, V> o) {
		return equals(o);
	}

	@Override
	public SortedMap<K,V> insert(K key, V value) {
		Entry<K,V> e = new Entry<>(key, value);

		return new TreeMap<>(
				set.delete(e)
						.insert(e)
		);
	}

	public static SortedMap<String,Integer> wordMap(String s) {
		SortedMap<String,Integer> map = TreeMap.empty();

		for(String word : s.split("\\s+")) {
			Integer count = map.get(word);
			map = map.insert(word,
					count == null ? 1 : count + 1);
		}

		return map;
	}

	@Override
	public boolean member(K key) {
		return set.member(new Entry<>(key, null));
	}

	@Override
	public SortedMap<K,V> delete(K key) {
		return new TreeMap<>(
				set.delete(new Entry<>(key, null))
		);
	}

	@Override
	public V get(K key) {
		Entry<K,V> entry =
				set.findEq(new Entry<>(key, null));

		return entry == null
				? null
				: entry.value;
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public String toString() {
		if (set.isEmpty()) {
			return "{}";
		} else {

			String listString =
					set.toList()
							.map(Entry::toString)
							.toString();

			return "{" +
					listString.substring(1, listString.length() - 1)
					+ "}";
		}
	}

	@Override
	public SortedMap<K,V> insertWith(
			Function<V, Function<V, V>> f,
			K key,
			V value) {

		V oldValue = get(key);

		if (oldValue == null) {
			return insert(key, value);
		} else {

			return insert(
					key,
					f.apply(value).apply(oldValue)
			);
		}
	}

	//Laufzeit: O(1)
	@Override
	public Set<Entry<K,V>> entrySet() {
		return set;
	}

	//Laufzeit: O(n)
	@Override
	public List<Tuple<K,V>> toList() {
		return set.toList()
				.map(e -> Tuple.tuple(e.key, e.value));
	}

	//Laufzeit: O(n)
	@Override
	public List<Tuple<K,V>> toAscList() {
		return set.toAscList()
				.map(e -> Tuple.tuple(e.key, e.value));
	}

	//Laufzeit: O(n)
	@Override
	public List<K> keys() {
		return set.toList()
				.map(e -> e.key);
	}

	//Laufzeit: O(n)
	@Override
	public List<K> keysAsc() {
		return set.toAscList()
				.map(e -> e.key);
	}

	//Laufzeit: O(n log n)
	@Override
	public Set<K> keysSet() {
		return TreeSet.fromList(keys());
	}

	//Laufzeit: O(n)
	@Override
	public List<V> elems() {
		return set.toList()
				.map(e -> e.value);
	}

	//Laufzeit: O(n)
	@Override
	public List<V> elemsAsc() {
		return set.toAscList()
				.map(e -> e.value);
	}

	//Laufzeit: O(n)
	@Override
	public boolean all(Function<K, Function<V, Boolean>> p) {
		return set.toList()
				.all(e -> p.apply(e.key)
						.apply(e.value));
	}

	//Laufzeit: O(n)
	@Override
	public boolean allKeys(Function<K, Boolean> p) {
		return keys().all(p::apply);
	}

	//Laufzeit: O(n log m)
	@Override
	public boolean isSubmapOf(SortedMap<K,V> m) {
		return entrySet().all(
				e -> {
					V mv = m.get(e.key);
					return mv != null && mv.equals(e.value);
				}
		);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof SortedMap<?, ?>)) {
			return false;
		} else {
			return isEqualTo((SortedMap<K, V>) o);
		}
	}
}
