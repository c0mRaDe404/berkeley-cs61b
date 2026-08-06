package deque;

public interface Deque<T> {
    /**
     * add an item to the front.
     */

    public void addFirst(T item);

    /**
     * add an item to the end.
     */

    public void addLast(T item);

    /**
     * check if a deque is empty.
     */

    default public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * get the size of a deque.
     */

    public int size();

    /**
     * print a deque.
     */

    public void printDeque();

    /**
     * remove the first element.
     */

    public T removeFirst();

    /**
     * remove the last element.
     */

    public T removeLast();

    /**
     * get the element at an index
     */

    public T get(int index);

    /**
     * checks if both deque are the same
     */
    public boolean equals(Object o);
}
