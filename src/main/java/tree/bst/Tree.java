package tree.bst;

import fpinjava.Result;

public abstract class Tree <A extends Comparable<A>>{

    public abstract boolean isEmpty();

    public abstract Tree<A> insert(A x);

    public abstract boolean member(A x);

    public abstract int size();

    public abstract int height();

    public abstract Tree<A> delete(A x);

    public static <A extends Comparable<A>> Tree<A> tree() {
        return new Empty<>();
    }

    public abstract A findEq(A x);

    public abstract Result<A> lookupEq(A x);

    public abstract A findMin();

    public abstract A findMax();
}
