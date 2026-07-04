package graphMap.map;

import fpinjava.Result;
import graphMap.Graph;
import list.List;
import map.SortedMap;
import map.TreeMap;
import queue.Queue;
import queue.TwoListQ;
import set.Set;
import set.TreeSet;
import tuple.Tuple;


// Gerichteter Graph
public class MapGraph<A extends Comparable<A>,B> implements Graph<A,B> {

	private final SortedMap<A, SortedMap<A,B>> graph;

	private MapGraph() {
		graph = TreeMap.empty();
	}

	private MapGraph(SortedMap<A, SortedMap<A,B>> graph) {
		this.graph = graph;
	}

	@Override
	public boolean isEqualTo(Graph<A,B> g) {
		return this.isSubgraphOf(g) && g.isSubgraphOf(this);

	}

	@Override
	public Graph<A, B> insert(Tuple<A, A> e, B w) {

		SortedMap<A, SortedMap<A, B>> withTarget = graph.member(e.snd)
				? graph
				: graph.insert(e.snd, TreeMap.empty());


		SortedMap<A, B> innerMap = withTarget.lookup(e.fst).getOrElse(TreeMap.empty());


		SortedMap<A, B> newInnerMap = innerMap.insert(e.snd, w);

		return new MapGraph<>(withTarget.insert(e.fst, newInnerMap));
	}

	public static <A extends Comparable<A>, B> Graph<A,B> empty() {
		return new MapGraph<>();
	}

	public static <A extends Comparable<A>, B> Graph<A, B> fromList(List<Tuple<Tuple<A, A>, B>> ews) {

		return ews.foldl(g -> edgeWithWeight -> {
			Tuple<A, A> edge = edgeWithWeight.fst;
			B weight = edgeWithWeight.snd;
			return g.insert(edge, weight);
		}, MapGraph.<A, B>empty());
	}

	@Override
	public boolean isEmpty() {
		return graph.isEmpty();
	}

	@Override
	public boolean nodeIn(A v) {
		return graph.member(v);
	}

	@Override
	public Set<A> adj(A v) {
		return graph.lookup(v)
				.map(SortedMap::keysSet)
				.getOrElse(TreeSet.empty());
	}

	@Override
	public Result<B> weight(Tuple<A,A> e) {
		return graph.lookup(e.fst)
				.flatMap(m -> m.lookup(e.snd));
	}

	@Override
	public List<A> adjList(A v) {
		return graph.lookup(v)
				.map(SortedMap::keysAsc)
				.getOrElse(List.list());
	}

	@Override
	public List<Tuple<A,B>> adjListW(A v) {
		return graph.lookup(v)
				.map(SortedMap::toAscList)
				.getOrElse(List.list());
	}

	@Override
	public Set<A> nodes() {
		return graph.keysSet();
	}

	@Override
	public List<A> nodesList() {
		return graph.keysAsc();
	}

	@Override
	public SortedMap<Tuple<A, A>, B> edges() {

		return graph.toAscList().foldl(acc -> entry -> {
			A source = entry.fst;
			SortedMap<A, B> targets = entry.snd;

			return targets.toAscList().foldl(innerAcc -> innerEntry -> {
				A target = innerEntry.fst;
				B weight = innerEntry.snd;


				Tuple<A, A> edge = Tuple.tuple(source, target);
				return innerAcc.insert(edge, weight);
			}, acc);
		}, TreeMap.empty());
	}

	@Override
	public boolean edgeIn(Tuple<A, A> e) {

		return weight(e).exists(x -> true);
	}

	@Override
	public int size() {
		return edges().size();
	}

	@Override
	public boolean isSubgraphOf(Graph<A, B> g) {

		boolean allNodesIn = nodesList().foldl(acc -> n -> acc && g.nodeIn(n), true);
		if (!allNodesIn) return false;

		return graph.toAscList().foldl(acc -> entry -> {
			A source = entry.fst;
			SortedMap<A, B> targets = entry.snd;

			return targets.toAscList().foldl(innerAcc -> innerEntry -> {
				A target = innerEntry.fst;
				B weight = innerEntry.snd;
				Tuple<A, A> edge = Tuple.tuple(source, target);

				boolean edgeMatches = g.weight(edge)
						.map(w -> w.equals(weight))
						.getOrElse(false);

				return innerAcc && edgeMatches;
			}, acc);
		}, true);
	}

	@Override
	public String toString() {
		if (this.isEmpty()) {
			return "Graph: {}";
		} else {

			return graph.toAscList().foldl(sb -> entry -> {
				A source = entry.fst;
				SortedMap<A, B> targets = entry.snd;

				sb.append("  ").append(source).append(" -> ");

				if (targets.isEmpty()) {
					sb.append("{}\n");
				} else {
					sb.append("{");

					StringBuilder targetsSb = targets.toAscList().foldl(innerSb -> innerEntry -> {
						A target = innerEntry.fst;
						B weight = innerEntry.snd;

						if (innerSb.length() > 1) {
							innerSb.append(", ");
						}
						innerSb.append(target).append(" (w: ").append(weight).append(")");
						return innerSb;
					}, new StringBuilder());

					sb.append(targetsSb).append("}\n");
				}
				return sb;
			}, new StringBuilder("Graph:\n")).toString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Graph)) {
			return false;
		} else {
			Graph<A, B> that = (Graph<A, B>) o;

			return this.isEqualTo(that);
		}
	}

	public static <A extends Comparable<A>, B> List<A> breadthFirstSearch(Graph<A, B> graph, A start) {
		if (!graph.nodeIn(start)) {
			return List.list();
		} else {


			Queue<A> initialQueue = TwoListQ.<A>empty().enQueue(start);
			Set<A> initialVisited = TreeSet.empty();
			List<A> initialResult = List.list();

			return bfsRecursive(graph, initialQueue, initialVisited, initialResult);
		}
	}

	private static <A extends Comparable<A>, B> List<A> bfsRecursive(
			Graph<A, B> graph,
			Queue<A> queue,
			Set<A> visited,
			List<A> result) {


		if (queue.isEmpty()) {
			return result.reverse();
		} else {


			A current = queue.front();
			Queue<A> tailQueue = queue.deQueue();


			if (visited.member(current)) {
				return bfsRecursive(graph, tailQueue, visited, result);
			} else {

				Set<A> nextVisited = visited.insert(current);
				List<A> nextResult = result.cons(current);

				List<A> neighbors = graph.adjList(current);

				Queue<A> nextQueue = neighbors.foldl(q -> neighbor -> {
					if (!nextVisited.member(neighbor)) {
						return q.enQueue(neighbor);
					} else {
						return q;
					}
				}, tailQueue);

				return bfsRecursive(graph, nextQueue, nextVisited, nextResult);
			}
		}
	}
}

