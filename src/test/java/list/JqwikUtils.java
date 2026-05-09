package list;

import fpinjava.Function; // durch Ihr Function-Interface ersetzen!
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import org.apache.commons.lang3.SerializationUtils;
import tuple.Tuple;
import static tuple.Tuple.tuple;
import java.io.Serializable;
import java.util.Collection;

import static list.List.list;

public class JqwikUtils {

  private JqwikUtils(){}

  // Author: Pierre-Yves Saumont
  public static <T> List<T> fromCollection(Collection<T> ct) {
    List<T> lt = list();
    for (T t : ct) {
      lt = lt.cons(t);
    }
    return reverse(lt);
  } // das war die letzte Schleife ...

  // Author: Pierre-Yves Saumont
  public static <A,B> List<Tuple<A,B>> zip(List<A> listA, List<B> listB) {
    List<Tuple<A,B>> list = list();
    List<A> workListT = listA;
    List<B> workListU = listB;
    while (!(workListT.isEmpty()||workListU.isEmpty())) {
      list = list.cons(tuple(workListT.head(), workListU.head()));
      workListT = workListT.tail();
      workListU = workListU.tail();
    }
    return reverse(list);
  } // die wirklich letzte Schleife ...

  public static <A1,A2> Tuple<List<A1>, List<A2>> unzip(List<Tuple<A1,A2>> list) {
    return foldr( t -> tl -> tuple(tl.fst.cons(t.fst), tl.snd.cons(t.snd)),tuple(list(), list()),list);
  }

  public static <A> Arbitrary<List<A>> lists(Arbitrary<A> xa, int minLen, int maxLen){
    return  xa.list().ofMinSize(minLen).ofMaxSize(maxLen).map(JqwikUtils::fromCollection);
  }

  public static <A> Arbitrary<List<A>> lists(Arbitrary<A> xa, int maxLen){
    return  lists(xa,0,maxLen);
  }

  public static <A extends Serializable> Arbitrary<Tuple<A,A>> duplicate(Arbitrary<A> xa){
    return xa.map(x->tuple(x, SerializationUtils.clone(x)));
  }

  public static <A extends Serializable> Arbitrary<Tuple<List<A>,List<A>>> equalLists(Arbitrary<A> xa, int minLen, int maxLen){
    return lists(duplicate(xa),minLen,maxLen).map(xs->unzip(xs));
  }

  public static <A extends Serializable> Arbitrary<Tuple<List<A>,List<A>>> equalLists(Arbitrary<A> xa, int maxLen){
    return equalLists(xa,0,maxLen);
  }

  // Just 4 Fun
  private static <A> Arbitrary<Tuple<List<A>,List<A>>> equalLists2(Arbitrary<A> xa, int min, int max){
    return  xa.list().ofMinSize(min).ofMaxSize(max)
            .map(xs->Tuple.<List<A>,List<A>>tuple(fromCollection(xs),
                    fromCollection(new java.util.ArrayList<>(xs))));
  }
  // Just 4 Fun
  public static Arbitrary<Tuple<Integer,Integer>> diffInts(int min, int max) {
    Arbitrary<Integer> middle = Arbitraries.integers().between(min,max);
    return middle.flatMap(
            mid->Arbitraries.integers().between(min,mid).flatMap(
                    fst->Arbitraries.integers().between(mid,max).map(
                            snd->tuple(fst,snd)
                    )
            )
    );
  }

  public static Arbitrary<Tuple<List<Integer>,List<Integer>>> unEqualIntLists(int min, int max, int minLen, int maxLen){
    Arbitrary<Integer> middle = Arbitraries.integers().between(min,max-1);
    return middle.flatMap(mid-> {
        Arbitrary<List<Integer>> fstList = lists(Arbitraries.integers().between(min,mid),minLen,maxLen);
        Arbitrary<List<Integer>> sndList = lists(Arbitraries.integers().between(mid+1,max),minLen,maxLen);
        return Combinators.combine(fstList,sndList).as((fst,snd)->tuple(fst,snd));
      }
    );
  }

  // Just 4 Fun
  public static Arbitrary<Tuple<List<Integer>,List<Integer>>> unEqualIntLists2(int min, int max, int minLen, int maxLen){
    Arbitrary<Integer> middle = Arbitraries.integers().between(min,max-1);
    Arbitrary<Integer> fst = middle.flatMap(mid-> Arbitraries.integers().between(min,mid));
    Arbitrary<Integer> snd = middle.flatMap(mid->Arbitraries.integers().between(mid+1,max));
    Arbitrary<List<Integer>> fstList = lists(fst,minLen,maxLen);
    Arbitrary<List<Integer>> sndList = lists(snd,minLen,maxLen);
    return Combinators.combine(fstList,sndList).as((f,s)->tuple(f,s));
  }

  public static <A> List<A> reverse(List<A> list){
    return foldl(x -> y -> x.cons(y), list(), list);
  }

  private static <A,B> B foldr(Function<A, Function<B,B>> f, B s, List<A> xs) {
    return xs.isEmpty() ? s
            : f.apply(xs.head()).apply(foldr(f, s, xs.tail()));
  }

  private static <A,B> B foldl(Function<B, Function<A, B>> f, B s, List<A> xs) {
    return xs.isEmpty() ? s
            : foldl(f, f.apply(s).apply(xs.head()), xs.tail());
  }
}
