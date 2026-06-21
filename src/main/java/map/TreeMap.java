package map;

import fpinjava.Function;
import fpinjava.Result;
import list.List;
import set.Set;
import set.SortedSet;
import set.TreeSet;
import tuple.Tuple;



public class TreeMap<K extends Comparable<K>,V> implements SortedMap<K,V> {


	private final set.SortedSet<Entry<K, V>> set;


	private TreeMap() {
		this.set = TreeSet.empty();
	}

	private TreeMap(SortedSet<Entry<K, V>> set) {
		this.set = set;
	}


	public static <K extends Comparable<K>, V> SortedMap<K, V> empty() {
		return new TreeMap<>();
	}

	public static <K extends Comparable<K>, V>
	SortedMap<K, V> fromList(List<Tuple<K, V>> list) {

		SortedMap<K, V> map = TreeMap.empty();

		return list.foldl(
				acc -> t -> acc.insert(t.fst, t.snd),
				map
		);
	}

	@Override
	public boolean isEqualTo(SortedMap<K,V> o) {
		if (o == null) return false;
		return this.isSubmapOf(o) && o.isSubmapOf(this);
	}

	@Override
	public SortedMap<K, V> insert(K key, V value) {
		Entry<K, V> e = new Entry<>(key, value);

		return new TreeMap<>(
				set.delete(e)
						.insert(e)
		);
	}

	@Override
	public boolean member(K key) {
		return set.member(new Entry<>(key, null));
	}

	@Override
	public SortedMap<K, V> delete(K key) {
		return new TreeMap<>(
				set.delete(new Entry<>(key, null))
		);
	}

	@Override
	public V get(K key) {
		Entry<K, V> entry =
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
	public SortedMap<K, V> insertWith(
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
	public Set<Entry<K, V>> entrySet() {
		return set;
	}

	//Laufzeit: O(n)
	@Override
	public List<Tuple<K, V>> toList() {
		return set.toList()
				.map(e -> Tuple.tuple(e.key, e.value));
	}

	//Laufzeit: O(n)
	@Override
	public List<Tuple<K, V>> toAscList() {
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
	public boolean isSubmapOf(SortedMap<K, V> m) {
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
		}
		if (!(o instanceof SortedMap<?, ?>)) {
			return false;
		}
		return this.entrySet().equals(((SortedMap<?, ?>) o).entrySet());
	}

	@Override
	public <V2> V2 foldr(Function<V, Function<V2, V2>> f, V2 acc) {
		return set.toAscList()
				.reverse()
				.foldl(v2 -> e -> f.apply(e.value).apply(v2), acc);
	}

	@Override
	public <V2> V2 foldl(Function<V2, Function<V, V2>> f, V2 acc) {

		return set.toAscList().foldl(
				(V2 a) -> (Entry<K, V> e) -> f.apply(a).apply(e.value),
				acc
		);
	}

	@Override
	public SortedMap<K, V> filter(Function<V, Boolean> p) {

		SortedSet<Entry<K, V>> result =
				set.toAscList().foldl(
						acc -> e -> p.apply(e.value)
								? acc.insert(e)
								: acc,
						TreeSet.empty()
				);

		return new TreeMap<>(result);
	}

	@Override
	public <V2> SortedMap<K, V2> map(Function<V, V2> f) {
		SortedSet<Entry<K, V2>> mapped =
				set.toAscList()
						.foldl(acc -> e ->
										acc.insert(new Entry<>(e.key, f.apply(e.value))),
								TreeSet.empty());

		return new TreeMap<>(mapped);
	}

	@Override
	public SortedMap<K, V> union(SortedMap<K, V> m) {

		SortedSet<Entry<K, V>> result =
				m.toList().foldl(
						acc -> t -> {
							Entry<K, V> e = new Entry<>(t.fst, t.snd);
							return acc.member(new Entry<>(t.fst, null))
									? acc
									: acc.insert(e);
						},
						set
				);

		return new TreeMap<>(result);
	}

	@Override
	public SortedMap<K, V> intersection(SortedMap<K, V> m) {

		SortedSet<Entry<K, V>> result =
				set.toAscList().foldl(
						acc -> e ->
								m.member(e.key)
										? acc.insert(e)
										: acc,
						TreeSet.empty()
				);

		return new TreeMap<>(result);
	}

	//Laufzeit: O(n log m)
	public static SortedMap<String, Integer> wordMap(String s) {

		List<String> words = List.list(s.split("\\s+"));

		return words.foldl(
				map -> word -> {
					Integer count = map.get(word);

					return map.insert(
							word,
							count == null ? 1 : count + 1
					);
				},
				TreeMap.empty()
		);
	}

	@Override
	public Result<V> lookup(K key) {
		return set.lookupEq(new Entry<>(key, null))
				.map(e -> e.value);
	}

	@Override
	public int size() {
		return set.size();
	}
}
