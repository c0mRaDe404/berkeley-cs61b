package deque;


interface Deque<T> {

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

    public boolean isEmpty();

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


public class LinkedListDeque<T> implements Deque<T> {

    private Node<T> sentinel;
    private int size;


    private class Node<T> {
        T item;
        Node prev;
        Node next;
    }

    public LinkedListDeque() {
        sentinel = new Node<T>();
        sentinel.item = null;
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /**
     * add an item to the front.
     */

    @Override
    public void addFirst(T item) {
        Node<T> newNode = new Node<>();
        newNode.item = item;
        newNode.prev = sentinel;
        newNode.next = sentinel.next;
        sentinel.next = newNode;

        if (sentinel.prev == sentinel)
            sentinel.prev = newNode;
        size++;

    }

    /**
     * add an item to the end.
     */

    @Override
    public void addLast(T item) {
        if (sentinel.prev == sentinel) {
            addFirst(item);
        } else {
            Node<T> newNode = new Node<>();
            newNode.item = item;
            newNode.next = sentinel;
            newNode.prev = sentinel.prev;
            sentinel.prev.next = newNode;
            sentinel.prev = newNode;
            size++;
        }
    }

    /**
     * check if a deque is empty.
     */

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
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
        for (Node temp = sentinel.next; temp != sentinel; temp = temp.next) {
            System.out.print(temp.item + " ");
        }
    }

    /**
     * remove the first element.
     */

    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel)
            return null;
        Node temp = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return (T) temp.item;
    }

    /**
     * remove the last element.
     */

    @Override
    public T removeLast() {
        if (sentinel.next == sentinel)
            return null;

        Node temp = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return (T) temp.item;
    }

    /**
     * get the element at an index
     */

    @Override
    public T get(int index) {
        int counter = 0;
        for (Node temp = sentinel.next; temp != sentinel; temp = temp.next) {
            if (counter == index)
                return (T) temp.item;
            counter++;
        }
        return null;
    }

    /**
     * checks if both deque are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkedListDeque) {

            LinkedListDeque List = (LinkedListDeque) o;
            Node list1 = sentinel.next;
            Node list2 = List.sentinel.next;

            if (size != List.size)
                return false;

            while (list1 != sentinel && list2 != List.sentinel) {
                boolean isEqual = list1.item.equals(list2.item);
                if (!isEqual)
                    return false;
                list1 = list1.next;
                list2 = list2.next;
            }
        }
        return true;
    }

    /**
     * overloaded method to recursively walk along all the nodes
     * until index becomes 0.
     *
     * @param index
     * @param node
     * @return
     */
    private T getRecursive(int index, Node node) {
        if (node == sentinel)
            return null;
        if (index == 0)
            return (T) node.item;
        return (T) getRecursive(index - 1, node.next);
    }

    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }
}
