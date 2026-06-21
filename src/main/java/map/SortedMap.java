package map;


import fpinjava.Function;
import fpinjava.Result;
import list.List;
import set.Set;
import tuple.Tuple;

public interface SortedMap<K extends Comparable<K>,V> extends Map<K, V>  {

  default boolean isEmpty() {
    return false;
  }

  default boolean isEqualTo(SortedMap<K, V> o) {
    return false;
  }

  default SortedMap<K,V> insertWith(Function<V,Function<V, V>> f, K key, V value){
    return null;
  }

  SortedMap<K,V> insert(K key, V value) ;

  boolean member(K key);

  SortedMap<K,V> delete(K key);

  V get(K key);

  int size();

  Set<Entry<K,V>> entrySet();

  List<Tuple<K,V>> toList();

  List<Tuple<K,V>> toAscList();

  List<K> keys();

  List<K> keysAsc();

  Set<K> keysSet();

  List<V> elems();

  List<V> elemsAsc();

  boolean all(Function<K, Function<V, Boolean>> p);

  boolean allKeys(Function<K, Boolean> p);

  boolean isSubmapOf(SortedMap<K,V> m);

  <V2> V2 foldr(Function<V, Function<V2, V2>> f, V2 acc);

  <V2> V2 foldl(Function<V2, Function<V, V2>> f, V2 acc);

  SortedMap<K,V> filter(Function<V, Boolean> p);

  <V2> SortedMap<K,V2> map(Function<V, V2> f);

  SortedMap<K, V> union(SortedMap<K, V> m);

  SortedMap<K, V> intersection(SortedMap<K, V> m);

  Result<V> lookup(K k);

}