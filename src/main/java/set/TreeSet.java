package set;

import java.util.function.Function;
import fpinjava.Result;
import list.List;
import tree.bst.Tree;


public class TreeSet<A extends Comparable<A>> implements SortedSet<A> {

	private final Tree<A> tree;

	private TreeSet(Tree<A> tree) {
		this.tree = tree;
	}

	public static <A extends Comparable<A>> SortedSet<A> empty() {
		return new TreeSet<A>(Tree.tree());
	}

	public static <A extends Comparable<A>> SortedSet<A> fromList(List<A> xs) {
		return fromList(xs, empty());
	}

	private static <A extends Comparable<A>> SortedSet<A> fromList(List<A> xs, SortedSet<A> acc) {
		if (xs.isEmpty()) {
			return acc;
		} else {
			return fromList(xs.tail(), (SortedSet<A>) acc.insert(xs.head()));
		}
	}

	public static <A extends Comparable<A>> SortedSet<A> fromSet(Set<A> s) {
		return fromList(s.toList());
	}

	@SafeVarargs
	public static <A extends Comparable<A>> SortedSet<A> set(A... xs) {
		return set(xs, 0, empty());
	}

	private static <A extends Comparable<A>> SortedSet<A> set(
			A[] xs,
			int i,
			SortedSet<A> acc
	) {
		if (i >= xs.length) {
			return acc;
		} else {
			return set(xs, i + 1, (SortedSet<A>) acc.insert(xs[i]));
		}
	}

	@Override
	public boolean isEqualTo(Set<A> other) {
		return tree.size() == other.size()
				&& contains(tree.inOrder(), other);
	}

	private boolean contains(List<A> xs, Set<A> s) {
		if (xs.isEmpty()) {
			return true;
		} else {
			return s.member(xs.head())
					&& contains(xs.tail(), s);
		}
	}

	@Override
	public SortedSet<A> insert(A e) {
		return new TreeSet<>(tree.delete(e).insert(e));
	}

	@Override
	public SortedSet<A> delete(A e) {
		return new TreeSet<>(tree.delete(e));
	}

	@Override
	public boolean member(A e) {
		return tree.member(e);
	}

	@Override
	public int size() {
		return tree.size();
	}

	@Override
	public boolean isEmpty() {
		return tree.isEmpty();
	}

	@Override
	public A findEq(A e) {
		return tree.findEq(e);
	}

	@Override
	public List<A> toList() {
		return tree.inOrder();
	}

	@Override
	public List<A> toAscList() {
		return tree.inOrder();
	}

	@Override
	public A findMin() {
		return tree.findMin();
	}

	@Override
	public A findMax() {
		return tree.findMax();
	}

	@Override
	public Result<A> lookupMin() {
		if (tree.isEmpty()) {
			return Result.empty();
		} else {
			return Result.success(tree.findMin());
		}
	}

	@Override
	public Result<A> lookupMax() {
		if (tree.isEmpty()) {
			return Result.empty();
		} else {
			return Result.success(tree.findMax());
		}
	}

	@Override
	public String toString() {
		return "{" + toString(tree.inOrder()) + "}";
	}

	private String toString(List<A> xs) {
		if (xs.isEmpty()) {
			return "";
		} else if (xs.tail().isEmpty()) {
			return xs.head().toString();
		} else {
			return xs.head() + ", " + toString(xs.tail());
		}
	}

	//Laufzeit:O(n)
	public boolean any(Function<A, Boolean> p) {
		return tree.inOrder().any(p);
	}

	//Laufzeit:O(n)
	public boolean all(Function<A, Boolean> p) {
		return tree.inOrder().all(p);
	}

	//Laufzeit:O(n * h)
	@Override
	public boolean isSubsetOf(Set<A> s) {
		return tree.inOrder()
				.all(x -> s.member(x));
	}

	//Laufzeit:O(n * h)
	@Override
	public boolean disjoint(Set<A> s) {
		return tree.inOrder()
				.all(x -> !s.member(x));
	}

	public static <A extends Comparable<A>, B>
		B foldr(Function<A, Function<B, B>> f,
	        B s,
	        Set<A> xs) {

		return List.foldr(f, s, xs.toList());
	}

	public static <A extends Comparable<A>>
		Set<A> filter(Function<A, Boolean> p,
	              Set<A> xs) {

		return TreeSet.fromList(
				xs.toList().filter(p)
		);
	}

	public static <A extends Comparable<A>,
			B extends Comparable<B>>
				Set<B> map(
						Function<A, B> f,
						Set<A> xs) {

		return TreeSet.fromList(
				xs.toList().map(f)
		);
	}

	public static <A extends Comparable<A>, B>
	B foldl(Function<B, Function<A, B>> f,
	        B s,
	        Set<A> xs) {

		return List.foldl(f, s, xs.toList());
	}

	public Set<A> union(Set<A> s) {
		return union(this.toAscList(), s);
	}

	private Set<A> union(List<A> xs, Set<A> acc) {
		if (xs.isEmpty()) {
			return acc;
		} else { return union(
				xs.tail(),
				acc.insert(xs.head())
		);
		}
	}

	public Set<A> intersection(Set<A> s) {
		return intersection(this.toAscList(), s, TreeSet.empty());
	}

	private Set<A> intersection(
			List<A> xs,
			Set<A> s,
			Set<A> acc
	) {
		if (xs.isEmpty()) {
			return acc;
		} else {
			return s.member(xs.head())
					? intersection(
					xs.tail(),
					s,
					acc.insert(xs.head())
			)
					: intersection(
					xs.tail(),
					s,
					acc
			);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Set<?> other)) {
			return false;
		} else {
			try {
				@SuppressWarnings("unchecked")
				Set<A> s = (Set<A>) other;

				return this.isSubsetOf(s) && s.isSubsetOf(this);
			} catch (ClassCastException e) {
				return false;
			}
		}
	}

	//Laufzeit: O(n * h)
	public static SortedSet<String> wordSet(String s) {
		return fromList(List.list(s.split(" ")));
	}


}

