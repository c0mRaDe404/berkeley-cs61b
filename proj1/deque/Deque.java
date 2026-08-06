package deque;

public interface Deque<T> {
    /**
     * add an item to the front.
     */

    void addFirst(T item);

    /**
     * add an item to the end.
     */

    void addLast(T item);

    /**
     * check if a deque is empty.
     */

    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * get the size of a deque.
     */

    int size();

    /**
     * print a deque.
     */

    void printDeque();

    /**
     * remove the first element.
     */

    T removeFirst();

    /**
     * remove the last element.
     */

    T removeLast();

    /**
     * get the element at an index
     */

    T get(int index);

    /**
     * checks if both deque are the same
     */
    boolean equals(Object o);
}
