package map;


import fpinjava.Function;
import list.List;
import set.Set;
import tuple.Tuple;

public interface SortedMap<K extends Comparable<K>,V>  {

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
}