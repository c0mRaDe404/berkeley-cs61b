package deque;


import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] queue;
    private int head;
    private int tail;
    private int size;

    public ArrayDeque() {
        queue = (T[]) new Object[8];
        head = tail = -1;
        size = 0;
    }

    /**
     * add an item to the front.
     */

    private int getHead() {
        return ((head % queue.length) + queue.length) % queue.length;
    }

    private int getTail() {
        return ((tail % queue.length) + queue.length) % queue.length;
    }

    private int getHeadPos(int offset) {
        return (getHead() + 1 + offset) % queue.length;
    }

    private void resize(int newSize) {

        int start = getHeadPos(0);
        T[] temp = (T[]) new Object[newSize];

        for (int i = 0; i < size; i++) {
            temp[i] = queue[(start + i) % queue.length];
        }

        queue = temp;
        head = queue.length - 1;
        tail = size;
    }

    /**
     * adds an item to the first
     *
     * @param item
     */

    @Override
    public void addFirst(T item) {
        if (size >= queue.length) {
            resize(queue.length * 2);
        }

        if (isEmpty()) {
            head = queue.length;
            tail = queue.length + 1;
        }

        queue[getHead()] = item;
        head--;
        size++;
    }

    /**
     * add an item to the end.
     *
     * @param item
     */

    @Override
    public void addLast(T item) {
        if (size >= queue.length) {
            resize(queue.length * 2);
        }

        if (isEmpty()) {
            head = queue.length - 1;
            tail = queue.length;
        }
        queue[getTail()] = item;
        tail++;
        size++;

    }


    /**
     * remove the first element.
     */

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        if (queue.length >= 16 && size < queue.length / 4) {
            resize(queue.length / 2);
        }

        head++;
        size--;
        T temp = queue[getHead()];
        queue[getHead()] = null;
        return temp;
    }

    /**
     * remove the last element.
     */

    @Override
    public T removeLast() {

        if (isEmpty()) {
            return null;
        }

        if (queue.length >= 16 && size < queue.length / 4) {
            resize(queue.length / 2);
        }

        tail--;
        size--;
        T temp = queue[getTail()];
        queue[getTail()] = null;
        return temp;
    }

    /**
     * get the element at an index
     *
     * @param index
     */

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return queue[getHeadPos(index)];
    }


    /**
     * get the size of a deque.
     */

    @Override
    public int size() {
        return size;
    }

    /**
     * print a deque.
     */

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(queue[i] + " ");
        }
    }


    /**
     * checks if both deque are the same
     */


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (o instanceof Deque) {

            Deque temp = (Deque) o;

            if (size != temp.size()) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                if (!queue[i].equals(temp.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {

        int count = 0;

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public T next() {
            T value = get(getHeadPos(count));
            count += 1;
            return value;
        }
    }
}
