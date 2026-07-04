package graphMap;

import fpinjava.Result;
import list.List;
import map.SortedMap;
import set.Set;
import tuple.Tuple;

// gerichteter Graph
public interface Graph<A,B> {

    boolean isEmpty();

    Graph<A,B> insert(Tuple<A,A> e, B w);

    Set<A> adj(A v);

    List<A> adjList(A v);

    List<Tuple<A,B>> adjListW(A v);

    Set<A> nodes();

    List<A> nodesList();

    SortedMap<Tuple<A,A>,B> edges();

    boolean edgeIn(Tuple<A,A> e);

    boolean nodeIn(A v);

    Result<B> weight(Tuple<A,A> e);

    int size();

    boolean isSubgraphOf(Graph<A,B> g);

    boolean isEqualTo(Graph<A,B> g);

}
