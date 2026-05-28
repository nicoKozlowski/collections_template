package queue;

import list.List;
import tuple.Tuple;

public abstract class AbstractQueue<A> implements Queue<A> {

    // Laufzeit: O(n)
    @Override
    public boolean isEqualTo(Queue<A> q){
        return this.toList().equals(q.toList());
    }

    //Laufzeit: O(n)
    @Override
    public Queue<A> enQueueAll(List<A> xs) {
        Queue<A> q = this;
        return xs.foldl(acc -> x -> acc.enQueue(x), q);
    }

    public abstract A front();

    public abstract Queue<A> enQueue(A elem);

    public abstract Queue<A> deQueue();

    public abstract Tuple<A, Queue<A>> deQueueFront();

    public abstract boolean isEmpty();

    public abstract List<A> toList();

}
