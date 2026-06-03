package tree.bst;

import fpinjava.Result;

public final class Node<A extends Comparable<A>> extends Tree<A> {

    private final A value;
    private final Tree<A> left;
    private final Tree<A> right;

    public Node(A value, Tree<A> left, Tree<A> right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Tree<A> insert(A x) {
        int cmp = x.compareTo(value);

        return cmp < 0
                ? new Node<>(value, left.insert(x), right)
                : new Node<>(value, left, right.insert(x));
    }

    @Override
    public boolean member(A x) {
        int cmp = x.compareTo(value);

        return (cmp == 0)
                ? true
                : (cmp < 0)
                  ? left.member(x)
                  : right.member(x);
    }

    @Override
    public int size() {
        return 1 + left.size() + right.size();
    }

    @Override
    public int height() {
        return 1 + Math.max(left.height(), right.height());
    }

    @Override
    public Tree<A> delete(A x) {
        int cmp = x.compareTo(value);

        if (cmp < 0) {
            return new Node<>(value, left.delete(x), right);

        } else if (cmp > 0) {
            return new Node<>(value, left, right.delete(x));

        } else {

            if (left.isEmpty()) {
                return right;
            }

            if (right.isEmpty()) {
                return left;
            }

            A min = right.findMin();
            return new Node<>(min, left, right.delete(min));
        }
    }

    @Override
    public A findEq(A x) {
        int cmp = x.compareTo(value);

        if (cmp == 0) {
            return value;
        } else if (cmp < 0) {
            return left.findEq(x);
        } else {
            return right.findEq(x);
        }
    }

    @Override
    public Result<A> lookupEq(A x) {
        int cmp = x.compareTo(value);

        if (cmp == 0) {
            return Result.success(value);
        } else if (cmp < 0) {
            return left.lookupEq(x);
        } else {
            return right.lookupEq(x);
        }
    }

    @Override
    public A findMin() {
        if (left.isEmpty()) {
            return value;
        }
        return left.findMin();
    }

    @Override
    public A findMax() {
        if (right.isEmpty()) {
            return value;
        }
        return right.findMax();
    }
}
