package queue;

import list.List;
import tuple.Tuple;

import static list.List.append;
import static list.List.list;
import static tuple.Tuple.tuple;

public class TwoListQ<A> implements Queue<A> {

	private final List<A> front;
	private final List<A> rear;

	private TwoListQ(List<A> front, List<A> rear){
		this.front = front;
		this.rear = rear;
	}

	private TwoListQ<A> normalize() {
		if (front.isEmpty()) {
			return new TwoListQ<>(rear.reverse(), list());
		}
		return this;
	}

	//Laufzeit: O(1)
	@Override
	public Queue<A> enQueue(A elem) {
		return new TwoListQ<>(front, append(rear, list(elem))).normalize();
	}

	//Laufzeit: O(1)
	@Override
	public A front() {
		final TwoListQ<A> q = normalize();
		if (q.front.isEmpty()) {
			throw new IllegalStateException("empty queue");
		}
		return q.front.head();
	}

	//Laufzeit: O(1)
	@Override
	public Queue<A> deQueue() {
		final TwoListQ<A> q = normalize();
		if (q.front.isEmpty()) {
			throw new IllegalStateException("empty queue");
		}
		return new TwoListQ<>(q.front.tail(), q.rear);
	}

	//Laufzeit: O(1)
	@Override
	public Tuple<A, Queue<A>> deQueueFront() {
		final TwoListQ<A> q = normalize();
		if (q.front.isEmpty()) {
			throw new IllegalStateException("empty queue");
		}
		return tuple(q.front.head(), q.deQueue());
	}

	//Laufzeit: O(1)
	@Override
	public boolean isEmpty() {
		return front.isEmpty() && rear.isEmpty();
	}

	//Laufzeit: O(1)
	public static <A> Queue<A> empty() {
		return new TwoListQ<>(list(), list());
	}

	//Laufzeit: O(n)
	@Override
	public Queue<A> enQueueAll(List<A> xs) {
		return new TwoListQ<>(front, append(rear, xs)).normalize();
	}

	//Laufzeit: O(n)
	@Override
	public List<A> toList() {
		return append(front, rear.reverse());
	}

	//Laufzeit: O(n)
	@Override
	public boolean isEqualTo(Queue<A> q){
		return toList().equals(q.toList());
	}


}
