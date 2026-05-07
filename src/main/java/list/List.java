package list;

import java.util.function.Function;


public abstract class List<A> {

  public abstract A head();

  public abstract List<A> tail();

  public abstract boolean isEmpty();

  public abstract List<A> setHead(A h);

  public abstract boolean isEqualTo(List<A> xs);

  public abstract <B> B foldr(Function<A, Function<B,B>> f, B s);

  public abstract <B> B foldl(Function<B, Function<A, B>> f, B s);

  protected abstract void buildString(StringBuilder sb);

  public abstract int length();

  public abstract boolean elem(A x);

  public abstract boolean any(Function<A, Boolean> p);

  public abstract boolean all(Function<A, Boolean> p);

  public abstract <B> List<B> map(Function<A, B> f);

  public abstract List<A> filter(Function<A, Boolean> f);

  public abstract A finde(Function<A, Boolean> f);

  public abstract List<A> init();

  public abstract A last();

  public abstract List<A> take(int n);

  public abstract List<A> drop(int n);

  public abstract List<A> takeWhile(Function<A, Boolean> p);

  public abstract List<A> dropWhile(Function<A, Boolean> p);

  public abstract List<A> delete(A x);

  public abstract List<A> reverse();

  public List<A> cons(A a) {
    return new Cons<>(a, this);
  }

  @SuppressWarnings("rawtypes")
  public static final List NIL = new Nil();

  private List() {
  }

  private static class Nil<A> extends List<A> {

    private Nil() {
    }

    public A head() {
      throw new IllegalStateException("head called en empty list");
    }

    public List<A> tail() {
      throw new IllegalStateException("tail called en empty list");
    }

    public boolean isEmpty() {
      return true;
    }

    @Override
    public List<A> setHead(A h) {
      throw new IllegalStateException("setHead called on an empty list");
    }

    @Override
    public boolean isEqualTo(List<A> xs){
      return xs.isEmpty();
    }

    @Override
    public <B> B foldr(Function<A, Function<B,B>> f, B s) {
      return s;
    }

    @Override
    public <B> B foldl(Function<B, Function<A, B>> f, B s) {
      return s;
    }

    public String toString() {
      return "[]";
    }
    @Override
    protected void buildString(StringBuilder sb) {
    }

    @Override
    public int length() {
      return 0;
    }

    @Override
    public boolean elem(A x) {
      return false;
    }

    @Override
    public boolean any(Function<A, Boolean> p) {
      return false;
    }

    @Override
    public boolean all(Function<A, Boolean> p) {
      return true;
    }

    @Override
    public <B> List<B> map(Function<A, B> f) {
      return List.list();
    }

    @Override
    public List<A> filter(Function<A, Boolean> f) {
      return List.list();
    }

    @Override
    public A finde(Function<A, Boolean> f) {
      return null;
    }

    @Override
    public List<A> init() {
      return List.list();
    }

    @Override
    public A last() {
      throw new IllegalStateException("last called on empty list");
    }

    @Override
    public List<A> take(int n) {
      return List.list();
    }

    @Override
    public List<A> drop(int n) {
      return List.list();
    }

    @Override
    public List<A> takeWhile(Function<A, Boolean> p) {
      return List.list();
    }

    @Override
    public List<A> dropWhile(Function<A, Boolean> p) {
      return List.list();
    }

    @Override
    public List<A> delete(A x) {
      return List.list();
    }

    @Override
    public List<A> reverse() {
      return List.list();
    }
  }

  private static class Cons<A> extends List<A> {

    private final A head;
    private final List<A> tail;

    private Cons(A head, List<A> tail) {
      this.head = head;
      this.tail = tail;
    }

    public A head() {
      return head;
    }

    public List<A> tail() {
      return tail;
    }

    public boolean isEmpty() {
      return false;
    }

    @Override
    public List<A> setHead(A h) {
      return new Cons<>(h, tail);
    }

    @Override
    public boolean isEqualTo(List<A> xs) {
      if (xs.isEmpty()) {
        return false;
      } else {
        Cons<A> that = (Cons<A>) xs;
        return this.head.equals(that.head)
                && this.tail.isEqualTo(that.tail);
      }
    }

    @Override
    public <B> B foldr(Function<A, Function<B, B>> f, B s) {
      return f.apply(head).apply(tail.foldr(f, s));
    }

    @Override
    public <B> B foldl(Function<B, Function<A, B>> f, B s) {
      return tail.foldl(f, f.apply(s).apply(head));
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("[");
      buildString(sb);
      sb.append("]");
      return sb.toString();
    }

    @Override
    protected void buildString(StringBuilder sb) {
      sb.append(head);
      if (!tail.isEmpty()) {
        sb.append(", ");
        tail.buildString(sb);
      }
    }

    @Override
    public int length() {
      return foldl(acc -> x -> acc + 1, 0);
    }

    @Override
    public boolean elem(A x) {
      return head.equals(x) || tail.elem(x);
    }
//    @Override
//    public boolean elem(A x) {
//      return any(y -> y.equals(x));
//    }

    @Override
    public boolean any(Function<A, Boolean> p) {
      return p.apply(head) || tail.any(p);
    }

    @Override
    public boolean all(Function<A, Boolean> p) {
      return p.apply(head) && tail.all(p);
    }
//    @Override
//    public boolean all(Function<A, Boolean> p) {
//      return !any(a -> !p.apply(a));
//    }

    @Override
    public <B> List<B> map(Function<A, B> f) {
      return foldr(x -> acc -> new Cons<>(f.apply(x), acc), List.list());
    }

    @Override
    public List<A> filter(Function<A, Boolean> f) {
      return foldr(x -> acc -> f.apply(x) ? acc.cons(x) : acc, List.list());
    }

    @Override
    public A finde(Function<A, Boolean> f) {
      return f.apply(head) ? head : tail.finde(f);
    }

    @Override
    public List<A> init() {
      if (tail.isEmpty()) return List.list();
      else return new Cons<>(head, tail.init());
    }

    @Override
    public A last() {
      if (tail.isEmpty()) return head;
      else return tail.last();
    }

    @Override
    public List<A> take(int n) {
      if (n <= 0 || isEmpty()) return List.list();
      else return tail.take(n - 1).cons(head);
    }

    @Override
    public List<A> drop(int n) {
      if (n <= 0 || isEmpty()) return this;
      else return tail.drop(n - 1);
    }

    @Override
    public List<A> takeWhile(Function<A, Boolean> p) {
      if (isEmpty() || !p.apply(head)) return List.list();
      else return tail.takeWhile(p).cons(head);
    }

    @Override
    public List<A> dropWhile(Function<A, Boolean> p) {
      if (isEmpty() || !p.apply(head)) return this;
      else return tail.dropWhile(p);
    }

    @Override
    public List<A> delete(A x) {
      return foldr(y -> acc -> y.equals(x) ? acc : acc.cons(y), List.list());
    }

    @Override
    public List<A> reverse() {
      return foldl(acc -> x -> acc.cons(x), List.list());
    }
  }

  @SuppressWarnings("unchecked")
  public static <A> List<A> list() {
    return NIL;
  }

  @SafeVarargs
  public static <A> List<A> list(A... a) {
    List<A> n = list();
    for (int i = a.length - 1; i >= 0; i--) {
      n = new Cons<>(a[i], n);
    }
    return n;
  }

  public static <A,B> B foldr(Function<A, Function<B,B>> f, B s, List<A> xs) {
    return xs.foldr(f, s);
  }

  public static <A, B> B foldl(Function<B, Function<A, B>> f, B s, List<A> xs) {
    return xs.foldl(f, s);
  }

  public static Integer sum(List<Integer> list) {
    return list.foldl(acc -> x -> acc + x, 0);
  }

  public static Double prod(List<Double> list) {
    return list.foldl(acc -> x -> acc * x, 1.0);
  }

  public static <A> List<A> append(List<A> list1, List<A> list2) {
    return list1.foldr(x -> acc -> acc.cons(x), list2);
  }

  public static <A> List<A> concat(List<List<A>> list) {
    return list.foldr(x -> acc -> append(x, acc), List.list());
  }

  public static boolean and(List<Boolean> list) {
    return list.foldl(acc -> x -> acc && x, true);
  }

  public static boolean or(List<Boolean> list) {
    return list.foldl(acc -> x -> acc || x, false);
  }

  public static Integer minimum(List<Integer> list) {
    if (list.isEmpty()) throw new IllegalStateException();
    return list.tail().foldl(acc -> x -> Math.min(acc, x), list.head());
  }

  public static Integer maximum(List<Integer> list) {
    if (list.isEmpty()) throw new IllegalStateException();
    return list.tail().foldl(acc -> x -> Math.max(acc, x), list.head());
  }
}