package set;

import list.List;
import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;

public abstract class ADTSortedSetJqwikTest extends ADTSetJqwikTest {

	@Override
	protected abstract <A extends Comparable<A>> SortedSet<A> empty();

	@Override
	protected abstract <A extends Comparable<A>> SortedSet<A> fromList(List<A> list);

	@Override
	protected abstract  <A extends Comparable<A>> SortedSet<A> set(A... list);

	// ∀s: SortedSet<A> :  any(x → x==findMin(s),s)   = true, falls s nicht leer
	@Property
	<A extends Comparable<A>> boolean findMin_any(@ForAll("sets") SortedSet<A> s){
		Assume.that(!s.isEmpty());
		return  s.any(x -> x.equals(s.findMin()));
	}

	// ∀s: SortedSet<A> :  all(x → x>=findMin(s),s)   = true, falls s nicht leer
	@Property
	<A extends Comparable<A>> boolean findMin_all(@ForAll("sets") SortedSet<A> s){
		Assume.that(!s.isEmpty());
		return  s.all(x -> x.compareTo(s.findMin()) >= 0);
	}

	// ∀s: SortedSet<A> :  any(x -> x = findMax(s), s) = true, falls s nicht leer
	@Property
	<A extends Comparable<A>> boolean findMax_any(@ForAll("sets") SortedSet<A> s){
		Assume.that(!s.isEmpty());
		return  s.any(x -> x.equals(s.findMax()));
	}

	// ∀s: SortedSet<A> :  all(x -> x <= findMax(s), s) = true, falls s nicht leer
	@Property
	<A extends Comparable<A>> boolean findMax_all(@ForAll("sets") SortedSet<A> s){
		Assume.that(!s.isEmpty());
		return  s.all(x -> x.compareTo(s.findMax()) <= 0);
	}

	// ∀ s: SortedSet, ∀ start,end: Integer :	findMin(fromList([start..end]))  = start, falls end >= start
	@Property
	boolean findMin_range(@ForAll @IntRange(min=1,max=100)  int start, @ForAll @IntRange(min=100,max=1000) int end) {
		Assume.that(end >= start);

		return TreeSet.fromList(List.range(start, end + 1)).findMin() == start;
	}

	// ∀ s: SortedSet, ∀ start,end: Integer :	findMax(fromList([start..end]))  = end, falls end >= start
	@Property
	boolean findMax_range(@ForAll @IntRange(min=1,max=100)  int start, @ForAll @IntRange(min=100,max=1000) int end) {
		Assume.that(end >= start);
		return TreeSet.fromList(List.range(start, end)).findMax().equals(end);
	}

	// ∀s: SortedSet<A> : any(x → exists(y->x==y,lookupMin(s)),s)   = true, falls s nicht leer
	@Property
	<A extends Comparable<A>> boolean lookupMin_any(@ForAll("sets") SortedSet<A> s){
		Assume.that(!s.isEmpty());
		return  s.any(x -> s.lookupMin().exists(y -> x.equals(y))); // Methode exists der Klasse Result hilft hier
	}

	// ∀s: SortedSet<A> : all(x → exists(y->x>=y,lookupMin(s)),s)   = true, falls s nicht leer
	@Property
	<A extends Comparable<A>> boolean lookupMin_all(@ForAll("sets") SortedSet<A> s){
		Assume.that(!s.isEmpty());
		return  s.all(x -> s.lookupMin().exists(min -> x.compareTo(min) >= 0)); // Methode exists der Klasse Result hilft hier
	}

	// ∀s: SortedSet<A> : any(x -> exists(y->x==y,lookupMax(s)),s) = true, falls s nicht leer
	@Property
	<A extends Comparable<A>> boolean lookupMax_any(@ForAll("sets") SortedSet<A> s){
		Assume.that(!s.isEmpty());
		return  s.any(x -> s.lookupMax().exists(y -> x.equals(y))); // Methode exists der Klasse Result hilft hier
	}

	// ∀s: SortedSet<A> : all(x -> exists(y->x>=y,lookupMax(s)),s) = true, falls s nicht leer
	@Property
	<A extends Comparable<A>> boolean lookupMax_all(@ForAll("sets") SortedSet<A> s){
		return  s.all(x -> s.lookupMax().exists(max -> x.compareTo(max) <= 0)); // Methode exists der Klasse Result hilft hier
	}

	//∀l:List : findMin(fromList(l)) = minimum(l), falls l nicht leer
	@Property
	public <A extends Comparable<A>> boolean findMin_minimum(@ForAll("lists") List<A> l) {
		Assume.that(!l.isEmpty());
		return TreeSet.fromList(l).findMin().equals(List.minimum(l));
	}

  // ∀l:List : findMax(fromList(l)) = maximum(l), falls l nicht leer
  @Property
	public <A extends Comparable<A>> boolean findMax_maximum(@ForAll("lists") List<A> l) {
		Assume.that(!l.isEmpty());
		return TreeSet.fromList(l).findMax().equals(List.maximum(l));
	}
}
