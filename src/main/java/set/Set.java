package set;

import java.util.function.Function;

import fpinjava.Result;
import list.List;


public interface Set<A>  {

  Set<A> insert(A e);

  Set<A> delete(A e);

  boolean member(A e);

  int size();

  boolean isEmpty();

  A findEq(A e);

  List<A> toList();

  boolean isEqualTo(Set<A> other);

   A findMin();

   A findMax();

   Result<A> lookupMin();

   Result<A> lookupMax();

  boolean any(Function<A, Boolean> p);

  boolean all(Function<A, Boolean> p);

  boolean isSubsetOf(Set<A> s);

  boolean disjoint(Set<A> s);

  Set<A> union(Set<A> s);

  Set<A> intersection(Set<A> s);

  Result<A> lookupEq(A e);

}
