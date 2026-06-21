//package map;
//
//import list.List;
//import net.jqwik.api.*;
//import tuple.Tuple;
//import static list.List.list;
//import static map.JqwikUtils.*;
//import static tuple.Tuple.tuple;
//
//
//public abstract class ADTMapJqwikTest {
//
//	protected abstract <K,V> Map<K,V> empty();
//
//  protected abstract <K,V> Map<K,V> fromList(List<Tuple<K,V>> l);
//
//  protected abstract Map<String,Integer> wordMap(String s);
///*
//	private Map<Integer,String> map(List<Integer> keys){ // just 4 Fun
//		Function<Integer, String> f = i -> i + "";
//		return keys.foldl(m -> i -> m.insert(i, f.apply(i)), empty());
//	}
//*/
//	@Provide
//	Arbitrary<String> keys(){
//		return Arbitraries.strings().withCharRange('a', 'c')
//						.ofMinLength(3).ofMaxLength(5);
//	}
//
//	@Provide
//	Arbitrary<Integer> values() {
//		return Arbitraries.integers().between(20, 100);
//	}
//
//	@Provide
//	Arbitrary<List<Tuple<String,Integer>>> keyValueLists(){
//		return tupleLists(keys(),values(),10);
//	}
//
//	@Provide
//	Arbitrary<Map<String,Integer>> maps(){
//		return maps(keyValueLists());
//	}
//
//	private <A extends Comparable<A>,B> Arbitrary<Map<A,B>> maps(Arbitrary<List<Tuple<A,B>>> ts){
//		return ts.map(this::fromList);
//	}
//
//	@Provide
//	Arbitrary<Tuple<Map<String,Integer>,Map<String,Integer>>> equalMaps(){
//		return equalTupleLists(keys(),values(),6).map(t->tuple(fromList(t.fst),fromList(t.snd)));
//	}
//
//	@Provide
//	Arbitrary<Tuple<Map<String,Integer>,Map<String,Integer>>> mapsWithSameDomainsDiffCoDomains(){
//		final int maxLen = 10;
//    final var ka = keys();
//    final var va1 = Arbitraries.integers().between(0, 50);
//		final var va2 = Arbitraries.integers().between(51, 100);
//		final var ma1 = maps(tupleLists(ka,va1,maxLen));
//		final var ma2 = maps(tupleLists(ka,va2,maxLen));
//		return tuples(ma1,ma2);
//	}
//
//	@Provide
//	Arbitrary<Tuple<Map<String,Integer>,Map<String,Integer>>> mapsWithSameKeysDiffValues(){
//		final var va1 = Arbitraries.integers().between(0, 50);
//		final var va2 = Arbitraries.integers().between(51, 100);
//		return tupleListsWithSameAsDiffBs(keys(), va1, va2, 10)
//						.map(t->tuple(fromList(t.fst),fromList(t.snd)));
//	}
//
//	@Property
//	<K,V>
//	boolean equalMapsAreEqual(@ForAll("equalMaps") Tuple<Map<K,V>,Map<K,V>> t){
//		return t.fst.isEqualTo(t.snd);
//	}
//
//	@Property
//	<K,V>
//	boolean mapsWithSameDomainsDiffCoDomainsAreUnEqual(
//					@ForAll("mapsWithSameDomainsDiffCoDomains") Tuple<Map<K,V>,Map<K,V>> t){
//	  final var m1 = t.fst;
//	  final var m2 = t.snd;
//		Assume.that(!m1.isEmpty()||!m2.isEmpty());
//		return !m1.isEqualTo(m2);
//	}
//
//	@Property
//	<K,V>
//	boolean mapsWithSameKeysDiffValuesAreUnEqual(
//					@ForAll("mapsWithSameKeysDiffValues") Tuple<Map<K,V>,Map<K,V>> t){
//		final var m1 = t.fst;
//		final var m2 = t.snd;
//		Assume.that(!m1.isEmpty()||!m2.isEmpty());
//		return !m1.isEqualTo(m2);
//	}
//
//	// isEmpty(empty)	= true
//	@Example
//	boolean isEmpty_empty(){
//		return TreeMap.empty().isEmpty();
//	}
//
//	// ∀m:Map<K,V>, ∀k:K, ∀v:V
//	// isEmpty(insert(k,v,m))	= false
//	@Property
//	<K,V>
//	boolean isEmpty_insert(@ForAll("maps")  Map<K,V> m,
//												 @ForAll("keys")   K k,
//												 @ForAll("values") V v){
//		return !m.insert(k, v).isEmpty();
//	}
//
//	// ∀k:K : member(k,empty) = false
//	@Property
//	<K extends Comparable<K>>
//	boolean member_empty(@ForAll("keys") K k){
//		return !((SortedMap<K, ?>) empty()).member(k);
//	}
//
//	// ∀m:Map<K,V>, ∀k1,k2:K, ∀v:V
//	// member(k2,insert(k1,v,m)) = k1=k2 ? true : member(k2,m)
//	@Property
//	<K,V>
//	boolean member_insert(@ForAll("maps")  Map<K,V> m,
//												@ForAll("keys") K k1,
//												@ForAll("keys") K k2,
//												@ForAll("values") V v){
//
//		Comparable compK1 = (Comparable) k1;
//		Comparable compK2 = (Comparable) k2;
//
//		return ((SortedMap) m.insert(k1, v)).member(compK2) ==
//				(k1.equals(k2) ? true : ((SortedMap) m).member(compK2));
//
//	}
//
//	// ∀m:Map<K,V>, ∀k1,k2:K
//	// member(k2,delete(k1,m))	=  k1=k2 ? false : member(k2,m)
//	@Property
//	<K,V>
//	boolean member_delete(@ForAll("maps")  Map<K,V> m,
//												@ForAll("keys") K k1,
//												@ForAll("keys") K k2){
//		Comparable compK1 = (Comparable) k1;
//		Comparable compK2 = (Comparable) k2;
//
//		return ((SortedMap) m).delete(compK1).member(compK2) ==
//				(k1.equals(k2) ? false : ((SortedMap) m).member(compK2));
//	}
//
//	// size(empty) =  0
//	@Example
//	boolean size_empty(){
//		return ((SortedMap<?, ?>) empty()).toList().length() == 0;
//	}
//
//	// ∀m:Map<K,V>, ∀k:K, ∀v:V
//	// size(insert(k,v,m)) = !member(k,m) ? size(m)+1 : size(m)
//	@Property
//	<K,V>
//	boolean size_insert(@ForAll("maps")  Map<K,V> m,
//											@ForAll("keys") K k,
//											@ForAll("values") V v){
//		return false;
//	}
//
//	// ∀m:Map<K,V>, ∀k1,k2:K, ∀v1,∀v2:V
//	// insert(k2,v2,insert(k1,v1,m)) = k1=k2 ? insert(k2,v2,m)
//	//  																		 : insert(k1,v1,insert(k2,v2,m))
//	@Property
//	<K,V>
//	boolean insert_insert(@ForAll("maps")  Map<K,V> m,
//												@ForAll("keys") K k1,
//												@ForAll("keys") K k2,
//												@ForAll("values") V v1,
//												@ForAll("values") V v2){
//		return false;
//	}
//
//	// ∀m:Map<K,V>, ∀k1,k2:K, ∀v:V
//	// delete(k2,insert(k1,v,m)) = k1=k2 ? delete(k2,m) : insert(k1,v,delete(k2,m))
//	@Property
//	<K,V>
//	boolean delete_insert(@ForAll("maps")  Map<K,V> m,
//												@ForAll("keys") K k1,
//												@ForAll("keys") K k2,
//												@ForAll("values") V v){
//		return false;
//	}
//
//	// ∀m:Map<K,V>, ∀k1,k2:K, ∀v:V
//	// get(k2,insert(k1,v,m)) =  k1=k2 ? v : get(k2,m)
//	@Property
//	<K,V>
//	boolean get_insert(@ForAll("maps")  Map<K,V> m,
//										 @ForAll("keys") K k1,
//										 @ForAll("keys") K k2,
//										 @ForAll("values") V v){
//		return false; //null-sicher vergleichen!
//	}
//
//
//	// ∀m:Map<K,V>, ∀k1,k2:K, ∀v:V
//	// lookup(k2,insert(k1,v,m)) =  k1=k2 ? v : lookup(k2,m)
//	@Property
//	<K,V>
//	boolean lookup_insert(@ForAll("maps")  Map<K,V> m,
//												@ForAll("keys") K k1,
//												@ForAll("keys") K k2,
//												@ForAll("values") V v){
//		return false;
//	}
//
//	// ∀m:Map<K,V>, ∀k1,k2:K
//	// get(k2,delete(k1,m))	= k1=k2 ? null : get(k2,m)
//	@Property
//	<K,V>
//	boolean get_delete(@ForAll("maps")  Map<K,V> m,
//										 @ForAll("keys") K k1,
//										 @ForAll("keys") K k2){
//		return false; //null-sicher vergleichen!
//	}
//
//	// ∀m:Map<K,V>, ∀k1,k2:K
//	// lookup(k2,delete(k1,m))	= k1=k2 ? Result.empty : lookup(k2,m)
//	@Property
//	<K,V>
//	boolean lookup_delete(@ForAll("maps")  Map<K,V> m,
//												@ForAll("keys") K k1,
//												@ForAll("keys") K k2){
//		return false;
//	}
//
//	// fromList([]) = empty
//	@Example
//	boolean testFromList(){
//		return false;
//	}
//
//	// ∀k : K, ∀v : V, ∀xs
//	// List<Tuple<K,V>> fromList(xs++[(k,v)]) = insert(k,v,fromList(xs))
//	@Property
//	<K,V>
//	boolean testFromList(@ForAll("keys")   K k,
//											 @ForAll("values") V v,
//											 @ForAll("keyValueLists") List<Tuple<K,V>> xs) {
//		return false;
//	}
//
//
//	// ∀m:Map<K,V> : empty ⊆ m
//	@Property
//	<K,V>
//	boolean emptyMapIsSubmapOfAllMaps(@ForAll("maps") Map<K,V> m) {
//		return false;
//	}
//
//	// ∀m:Map<K,V> : m ⊆ m
//	@Property
//	<K,V>
//	boolean everyMapIsSubmapOfItself(@ForAll("maps") Map<K,V> m) {
//		return false;
//	}
//
//	// ∀a:Map<K,V>, ∀b:Map<K,V>, ∀c:Map<K,V>
//	// a ∪ (b ∪ c) = (a ∪ b) ∪ c
//	@Property
//	<K,V>
//	boolean assoziativGesetzUnion(@ForAll("maps") Map<K,V> a,
//																@ForAll("maps") Map<K,V> b,
//																@ForAll("maps") Map<K,V> c) {
//		return false;
//	}
//
//	// ∀a:Map<K,V>, ∀b:Map<K,V>, ∀c:Map<K,V>
//	// a ∩ (b ∩ c) = (a ∩ b) ∩ c
//	@Property
//	<K,V>
//	boolean assoziativGesetzIntersect(@ForAll("maps") Map<K,V> a,
//																		@ForAll("maps") Map<K,V> b,
//																		@ForAll("maps") Map<K,V> c) {
//		return false;
//	}
//
//	// ∀a:Map<K,V>, ∀b:Map<K,V>, ∀c:Map<K,V>
//	// a ∩ (b ∪ c) = (a ∩ b) ∪ (a ∩ c)
//	@Property
//	<K,V>
//	boolean distributivGesetz(@ForAll("maps") Map<K,V> a,
//														@ForAll("maps") Map<K,V> b,
//														@ForAll("maps") Map<K,V> c) {
//		return false;
//	}
//
//	// ∀a:Map<K,V>, ∀b:Map<K,V>
//	// a ∪ (a ∩ b) = a
//	@Property
//	<K,V>
//	boolean absorptionsGesetz(@ForAll("maps") Map<K,V> a,
//														@ForAll("maps") Map<K,V> b) {
//		return false;
//	}
//
//	// ∀a:Map<K,V>, ∀b:Map<K,V>, ∀k:K :
//	// get(k,(a ∪ b)) = member(k,a)&&member(k,b)||member(k,a)?get(k,a)
//	//                                                       :member(k,b)?get(k,b)
//	//                                                                    :null
//	@Property
//	<K,V>
//	boolean defOfUnionGet(@ForAll("maps") Map<K,V> a,
//												@ForAll("maps") Map<K,V> b,
//												@ForAll("keys")   K k) {
//		return false; //null-sicher vergleichen!
//	}
//
//	// ∀a:Map<K,V>, ∀b:Map<K,V>, ∀k:K :
//	// lookup(k,(a ∪ b)) = (member(k,a)&&member(k,b))||member(k,a)?lookup(k,a)
//	//                                                             :member(k,b)?lookup(k,b)
//	//                                                                         :Result.Empty
//	@Property
//	<K,V>
//	boolean defOfUnionLookup(@ForAll("maps") Map<K,V> a,
//										 @ForAll("maps") Map<K,V> b,
//										 @ForAll("keys")   K k) {
//		return false;
//	}
//
//	// ∀a:Map<K,V>, ∀b:Map<K,V>, ∀k:K
//	// get(k,(a ∩ b)) = member(k,a)&&member(k,b)?get(k,a):null
//	@Property
//	<K,V>
//	boolean defOfIntersectionGet(@ForAll("maps") Map<K,V> a,
//															 @ForAll("maps") Map<K,V> b,
//															 @ForAll("keys")   K k) {
//		return false;
//	}
//
//	// ∀a:Map<K,V>, ∀b:Map<K,V>, ∀k:K
//	// lookup(k,(a ∩ b)) = member(k,a)&&member(k,b)?lookup(k,a):Result.Empty
//	@Property
//	<K,V>
//	boolean defOfIntersectionLookup(@ForAll("maps") Map<K,V> a,
//																	@ForAll("maps") Map<K,V> b,
//																	@ForAll("keys")   K k) {
//		return false;
//	}
//
//	@Example
//	boolean testInsertWithUpdate(){
//		Map<Character,Integer> m = fromList(list(tuple('a',1),tuple('b',2)));
//		return(m.insertWith(x->y->x+y, 'a', 3).isEqualTo(m.insert('a', 4)));
//	}
//
//	@Example
//	boolean testInsertWithNewKey(){
//		Map<Character,Integer> m = fromList(list(tuple('a',1),tuple('b',2)));
//		return(m.insertWith(x->y->x+y, 'e', 17).isEqualTo(m.insert('e', 17)));
//	}
//
//	@Example
//	boolean testWordMap() {
//		String s = "aber und aber und und oder";
//		Map<String,Integer> m = fromList(list(tuple("aber",2),tuple("und",3),tuple("oder",1)));
//		return(wordMap(s).isEqualTo(m));
//	}
//}
