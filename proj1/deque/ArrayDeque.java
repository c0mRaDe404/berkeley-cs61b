package deque;


public class ArrayDeque<T> implements Deque<T> {
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
        return head % queue.length;
    }

    private int getTail() {
        return tail % queue.length;
    }

    private void resize(int newSize) {

        int start = (head + 1) % queue.length;
        T[] temp = (T[]) new Object[newSize];

        for (int i = 0; i < size; i++)
            temp[i] = queue[(start + i) % queue.length];

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
        if (size >= queue.length)
            resize(queue.length * 2);

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
        if (size >= queue.length)
            resize(queue.length * 2);
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
        if (size == 0)
            return null;

        if (queue.length >= 16 && size < queue.length / 4)
            resize(queue.length / 4);

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

        if (size == 0)
            return null;

        if (queue.length >= 16 && size < queue.length / 4)
            resize(queue.length / 4);

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
        return queue[index];
    }

    /**
     * check if a deque is empty.
     */

    @Override
    public boolean isEmpty() {
        return size == 0;
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
        if (o instanceof ArrayDeque) {
            ArrayDeque temp = (ArrayDeque) o;

            if (size != temp.size())
                return false;
            for (int i = 0; i < size; i++) {
                if (!queue[i].equals(temp.get(i)))
                    return false;
            }
            return true;
        }
        return false;
    }
}
