package queue;

import list.List;
import tuple.Tuple;


import static list.List.list;
import static tuple.Tuple.tuple;

public class OneListQ<A> extends AbstractQueue<A> {

	private final List<A> elements;

	private OneListQ(List<A> elements){
		this.elements = elements;
	}

	// Laufzeit: O(n)
	@Override
	public Queue<A> enQueue(A elem) {
		return new OneListQ<>(List.append(elements, List.list(elem)));
	}

	// Laufzeit: O(1)
	@Override
	public A front() {

		if (isEmpty()) throw new IllegalStateException("empty queue");
		else return elements.head();
	}

	// Laufzeit: O(1)
	@Override
	public Queue<A> deQueue() {

		if (isEmpty()) throw new IllegalStateException("empty queue");
		else return new OneListQ<>(elements.tail());
	}

	// Laufzeit: O(1)
	@Override
	public Tuple<A, Queue<A>> deQueueFront() {

		if (isEmpty()) throw new IllegalStateException("empty queue");
		else return tuple(front(), deQueue());
	}

	// Laufzeit: O(1)
	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	// Laufzeit O(1)

	public static <A> Queue<A> empty() {
		return new OneListQ<>(list());
	}

	// Laufzeit: O(n)
	@Override
	public Queue<A> enQueueAll(List<A> xs) {
		return new OneListQ<>(List.append(elements, xs));
	}

	// Laufzeit: O(1)
	@Override
	public List<A> toList() {
		return elements;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Queue<?> q)) return false;
		return this.toList().equals(q.toList());
	}
}
