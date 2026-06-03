package tree.bst;

import fpinjava.Result;

public final class Empty<A extends Comparable<A>> extends Tree<A> {

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Tree<A> insert(A x) {
        return new Node<>(x, this, this);
    }

    @Override
    public boolean member(A x) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public Tree<A> delete(A x) {
        return this;
    }



    @Override
    public A findEq(A x) {
        return null;
    }

    @Override
    public Result<A> lookupEq(A x) {
        return Result.empty();
    }

    @Override
    public A findMin() {
        throw new IllegalStateException("empty tree");
    }

    @Override
    public A findMax() {
        throw new IllegalStateException("empty tree");
    }
}
