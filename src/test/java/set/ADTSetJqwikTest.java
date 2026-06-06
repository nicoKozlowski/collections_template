package set;

import fpinjava.Result;
import list.JqwikUtils;
import list.List;
import net.jqwik.api.*;
import tuple.Tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static list.JqwikUtils.equalLists;
import static list.JqwikUtils.reverse;
import static tuple.Tuple.tuple;

public abstract class ADTSetJqwikTest {

	@Provide
	<A> Arbitrary<A> as(){
		return (Arbitrary<A>) ints();
	}

	protected  Arbitrary<Integer> ints() {
		return Arbitraries.integers().between(0,10);
	}

	private Arbitrary<String> strings() {
		return Arbitraries.strings().withCharRange('a','z').ofMinLength(2).ofMaxLength(5);
	}

	protected abstract <A extends Comparable<A>> Set<A> empty();

	protected abstract <A extends Comparable<A>> Set<A> fromList(List<A> list);

	private <A extends Comparable<A>> Set<A> fromCollection(Collection<A> coll){
		return fromList(list.JqwikUtils.fromCollection(coll));
	};

	protected abstract <A extends Comparable<A>> Set<A> set(A... xs);

	@Provide
	Arbitrary<List<Integer>> lists(){
		return JqwikUtils.lists(as(),5);
	}

	@Provide
	Arbitrary<Set<Integer>> sets(){
		return sets(ints(),5);
	}

	private <A extends Comparable<A>> Arbitrary<Set<A>> sets(Arbitrary<A> xa, int maxSize) {
		return JqwikUtils.lists(xa,maxSize).map(xs->fromList(xs));
	}

	@Provide
	Arbitrary<Tuple<Set<Integer>,Set<Integer>>> equalSets(){
		return equalSets(ints(),10);
	}

	private <A extends Serializable & Comparable<A>> Arbitrary<Tuple<Set<A>,Set<A>>> equalSets(Arbitrary<A> xa, int minLen, int maxLen){
		return equalLists(xa,minLen,maxLen).map(t->tuple(fromList(t.fst),fromList(reverse(t.snd))));
	}

	private <A extends Serializable & Comparable<A>> Arbitrary<Tuple<Set<A>,Set<A>>> equalSets(Arbitrary<A> xa, int maxLen){
		return equalSets(xa,0,maxLen);
	}

	@Property
	public <A> boolean equalSetsAreEqual(@ForAll("equalSets") Tuple<Set<A>,Set<A>> t){
		return t.fst.equals(t.snd);
	}

	@Property
	public boolean setsFromUnEqualArraysAreUnEqual(@ForAll Integer[] a1, @ForAll Integer[] a2){
		final var js1 = new java.util.TreeSet<>(Arrays.asList(a1));
		final var js2 = new java.util.TreeSet<>(Arrays.asList(a2));
    Assume.that(!js1.equals(js2));
		return !set(a1).isEqualTo(set(a2));
	}

	@Property
	public //<A extends Comparable<A>>
	boolean setsFromUnEqualJavaSetsAreUnEqual(@ForAll java.util.Set<Integer> js1, @ForAll java.util.Set<Integer>  js2){
		Assume.that(!js1.equals(js2));
		return !fromCollection(js1).isEqualTo(fromCollection(js2));
	}

	// isEmpty(empty)=true
	@Example
	public boolean isEmpty_empty() {
		return empty().isEmpty();
	}
	
	// size(empty)=0
	@Example
	public boolean size_empty() {
		return empty().size() == 0;
	}
	
	// ∀s:Set<A>, ∀x:A : size(insert(x,s)) = !member(x,s) ? size(s)+1 : size(s)
	@Property
	public <A> boolean size_insert(@ForAll("sets") Set<A> s, @ForAll("as") A x) {
		return s.insert(x).size() == (!s.member(x) ? s.size() + 1 : s.size());
	}
		
  // ∀s:Set<A>, ∀x:A, ∀y:A : member(y,delete(x,s))	= x=y ? false : member(y,s)
	@Property
	public <A> boolean member_delete(@ForAll("sets") Set<A> s, @ForAll("as") A x, @ForAll("as") A y) {
		return s.delete(x).member(y) == (!x.equals(y) && s.member(y));
	}
	
	// ∀x:A : member(x,empty) = false
	@Property
	public <A extends Comparable<A>> boolean member_empty(@ForAll("as") A x) {
		return !TreeSet.<A>empty().member(x);
	}
	
	// ∀s:Set<A>, ∀x:A, ∀y:A : member(y,insert(x,s)) = x=y ? true : member(y,s)
	@Property
	public <A> boolean member_insert(@ForAll("sets") Set<A> s, @ForAll("as") A x, @ForAll("as") A y) {
		return s.insert(x).member(y) == (x == y) || s.member(y);
	}
	
	// ∀s:Set<A>, ∀x:A, ∀y:A : insert(y,insert(x,s))	= x=y ? insert(y,s) : insert(x,insert(y,s))
	@Property
	public <A> boolean insert_insert(@ForAll("sets") Set<A> s, @ForAll("as") A x, @ForAll("as") A y) {
		return s.insert(x).insert(y).equals(x.equals(y) ? s.insert(y) : s.insert(y).insert(x));
	}
		
	// ∀s:Set<A>, ∀x:A, ∀y:A : insert(y,insert(x,s)) = insert(x,insert(y,s))
	@Property
	public <A> boolean insertKommutativ(@ForAll("sets") Set<A> s, @ForAll("as") A x, @ForAll("as") A y) {
		return s.insert(x).insert(y).equals(s.insert(y).insert(x));
	}
	
	// ∀x:A : delete(x,empty) = empty
	@Property
	public <A extends Comparable<A>> boolean delete_empty(@ForAll("as") A x) {
		return TreeSet.<A>empty().delete(x).equals(empty());
	}

	// ∀s:Set<A>, ∀x:A, ∀y:A : delete(y,insert(x,s)) = x=y ? delete(y,s) : insert(x,delete(y,s))
	@Property
	public <A> boolean delete_insert(@ForAll("sets") Set<A> s, @ForAll("as") A x, @ForAll("as") A y) {
		return Objects.equals(s.insert(x).delete(y), x.equals(y) ? s.delete(y) : s.delete(y).insert(x));
	}

	// ∀s:Set<A>, ∀x:A, ∀y:A : findEq(y,insert(x,s)) = x=y ? x : findEq(y,s)
	@Property
	public <A> boolean findEq_insert(@ForAll("sets") Set<A> s, @ForAll("as") A x, @ForAll("as") A y) {
		return Objects.equals(s.insert(x).findEq(y), (x == y ? x : s.findEq(y)));
	}
	
	// ∀s:Set<A>, ∀x:A, ∀y:A : findEq(y,delete(x,s))	= x=y ? null : findEq(y,s)
	@Property
	public <A> boolean findEq_delete(@ForAll("sets") Set<A> s, @ForAll("as") A x, @ForAll("as") A y) {
		return Objects.equals(s.delete(x).findEq(y), (x == y ? null : s.findEq(y)));
	}


	// ∀s:Set : empty ⊆ s
	@Property
	public <A extends Comparable<A>> boolean emptySetIsSubsetOfAllSets(@ForAll("sets") Set<A> s) {
		return TreeSet.<A>empty().isSubsetOf(s);
	}

	// ∀s:Set : s ⊆ s
	@Property
	public <A> boolean everySetIsSubsetOfItself(@ForAll("sets") Set<A> s) {
		return s.isSubsetOf(s);
	}

	// ∀s:Set: disjoint(s, ∅) = true
	@Property
	public <A extends Comparable> boolean disjoint_empty(@ForAll("sets") Set<A> s) {
		return s.disjoint(empty());
	}

	//  ∀s:Set<A>, ∀x:A : disjoint({x}, s) = true , falls  x ∉ s
	@Property
	public <A extends Comparable> boolean disjoint_elem(@ForAll("sets") Set<A> s, @ForAll("as") A x) {
		Assume.that(!s.member(x));
		return TreeSet.set(x).disjoint(s);
	}

	// ∀s1:Set<A>, ∀s2:Set<A> : |s1 ∪ s2| = |s1| + |s2| , falls disjoint(s1, s2) = true
	@Property
	public <A> boolean disjoint_size(@ForAll("sets") Set<A> a, @ForAll("sets") Set<A> b){
		Assume.that(a.disjoint(b));
		return a.union(b).size() == a.size() + b.size();
	}

	// ∀a:Set, ∀b:Set, ∀x:A : falls x ∈ b und a ⊆ b dann a ∪ {x} ⊆ b
	@Property(maxDiscardRatio = 100)
	public <A extends Comparable<A>> boolean union_member_subset(@ForAll("sets") Set<A> a,
																		 @ForAll("sets") Set<A> b,
																		 @ForAll("as") A x){
		Assume.that(b.member(x));
		Assume.that(a.isSubsetOf(b));
		return TreeSet.set(x)
				.union(a)
				.isSubsetOf(b);
	}

	// ∀a:Set, ∀b:Set :  a ∪ b = b ∪ a
	@Property
	public <A> boolean kommutativGesetzUnion(@ForAll("sets") Set<A> a,
											 @ForAll("sets") Set<A> b) {
		return a.union(b).equals(b.union(a));
	}

	// ∀a:Set, ∀b:Set : a ∩ b = b ∩ a
	@Property
	public <A> boolean kommutativGesetzIntersect(@ForAll("sets") Set<A> a,
												 @ForAll("sets") Set<A> b) {
		return a.intersection(b).equals(b.intersection(a));
	}

	// ∀a:Set, ∀b:Set, ∀c:Set : a ∪ (b ∪ c) = (a ∪ b) ∪ c
	@Property
	public <A> boolean assoziativGesetzUnion(@ForAll("sets") Set<A> a,
											 @ForAll("sets") Set<A> b,
											 @ForAll("sets") Set<A> c) {
		return a.union(b.union(c)).equals((a.union(b)).union(c));
	}


	// ∀a:Set, ∀b:Set, ∀c:Set : a ∩ (b ∩ c) = (a ∩ b) ∩ c
	@Property
	public <A> boolean assoziativGesetzIntersect(@ForAll("sets") Set<A> a,
												@ForAll("sets") Set<A> b,
												 @ForAll("sets") Set<A> c) {
		return a.intersection(b.intersection(c)).equals((a.intersection(b)).intersection(c));
	}

	// ∀a:Set, ∀b:Set, ∀c:Set : a ∪ (b ∩ c) = (a ∪ b) ∩ (a ∪ c)
	@Property
	public <A> boolean distributivGesetz(@ForAll("sets") Set<A> a,
										 @ForAll("sets") Set<A> b,
										 @ForAll("sets") Set<A> c) {
		return a.union(b.intersection(c)).equals((a.union(b)).intersection(a.union(c)));
	}
	
	// ∀a:Set, ∀b:Set : a ∪ (a ∩ b) = a
	@Property
	public <A> boolean absorptionsGesetz(@ForAll("sets") Set<A> a, @ForAll("sets") Set<A> b) {
		return a.union(a.intersection(b)).equals(a);
	}

	// ∀a:Set, ∀b:Set, ∀x ∈ (a ∪ b) : x ∈ A || x ∈ B
	@Property
	public <A> boolean defOfUnion(@ForAll("sets") Set<A> a, @ForAll("sets") Set<A> b) {
		return a.union(b).toList().all(x ->
				a.member(x) || b.member(x));
	}

	// ∀a:Set, ∀b:Set, ∀x ∈ (a ∩ b) : x ∈ A && x ∈ B
	@Property
	public <A> boolean defOfIntersection(@ForAll("sets") Set<A> a, @ForAll("sets") Set<A> b) {
		return a.intersection(b).toList().all(x ->
				a.member(x) && b.member(x));
	}
	
	// ∀a:Set, ∀b:Set, ∀c:Set : U [a,b,c] = (a ∪ b) ∪ c
	@Property
	public <A> boolean testUnions(@ForAll("sets") Set<A> a,
												        @ForAll("sets") Set<A> b,
												        @ForAll("sets") Set<A> c) {
		return a.union(b).union(c).equals(a.union(b.union(c)));
	}

	@Property
	public <A extends Comparable<A>> boolean lookupEq_insert(
			@ForAll("sets") Set<A> s,
			@ForAll("as") A x,
			@ForAll("as") A y) {

		return s.insert(x).lookupEq(y).equals(x.equals(y)
				? Result.success(x)
				: s.lookupEq(y));
	}

	@Property
	public <A extends Comparable<A>> boolean lookupEq_delete(
			@ForAll("sets") Set<A> s,
			@ForAll("as") A x,
			@ForAll("as") A y) {

		return s.delete(x).lookupEq(y).equals(x.equals(y)
				? Result.empty()
				: s.lookupEq(y));
	}

	@Property
	<A extends Comparable<A>> boolean lookupMin_any(
			@ForAll("sets") SortedSet<A> s) {
		Assume.that(!s.isEmpty());
		return s.lookupMin().exists(x -> x.equals(s.findMin()));
	}

	@Property
	<A extends Comparable<A>> boolean lookupMin_all(
			@ForAll("sets") SortedSet<A> s) {

		Assume.that(!s.isEmpty());

		return s.all(x -> s.lookupMin().exists(min ->
						x.compareTo(min) >= 0));
	}

	@Property
	<A extends Comparable<A>> boolean lookupMax_any(
			@ForAll("sets") SortedSet<A> s) {
		Assume.that(!s.isEmpty());
		return s.lookupMax().exists(x -> x.equals(s.findMax()));
	}

	@Property
	<A extends Comparable<A>> boolean lookupMax_all(
			@ForAll("sets") SortedSet<A> s) {
		Assume.that(!s.isEmpty());
		return s.all(x -> s.lookupMax().exists(max ->
						x.compareTo(max) <= 0));
	}
}
