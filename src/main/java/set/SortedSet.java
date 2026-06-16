package set;

import fpinjava.Result;
import list.List;

public interface SortedSet<A extends Comparable<A>> extends Set<A>{

    List<A> toAscList();

    A findMax();

    A findMin();

    Result<A> lookupMin();

    Result<A> lookupMax();

    @Override
    SortedSet<A> insert(A e);

    @Override
    SortedSet<A> delete(A e);

}
