package list;

import java.util.function.Function;


public abstract class List<A> {

  public abstract A head();

  public abstract List<A> tail();

  public abstract boolean isEmpty();

  public abstract List<A> setHead(A h);

  public abstract boolean isEqualTo(List<A> xs);

  public abstract <B> B foldr(Function<A, Function<B,B>> f, B s);

  public static <A,B> B foldr(Function<A, Function<B,B>> f, B s, List<A> xs) {
    return xs.foldr(f, s);
  }

  public abstract <B> B foldl(Function<B, Function<A, B>> f, B s);

  public static <A, B> B foldl(Function<B, Function<A, B>> f, B s, List<A> xs) {
    return xs.foldl(f, s);
  }

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
    public boolean isEqualTo(List<A> xs){
      if (xs.isEmpty()) {
        return false;
      }
      else {
        Cons<A> that = (Cons<A>) xs;
        return this.head.equals(that.head)
                && this.tail.isEqualTo(that.tail);
      }
    }

    @Override
    public <B> B foldr(Function<A, Function<B,B>> f, B s) {
      return f.apply(head).apply(tail.foldr(f, s));
    }

    @Override
    public <B> B foldl(Function<B, Function<A,B>> f, B s) {
      return tail.foldl(f,f.apply(s).apply(head));
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
}