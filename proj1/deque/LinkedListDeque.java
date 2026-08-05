package deque;


import java.util.Iterator;

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


public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private Node<T> sentinel;
    private int size;


    private static class Node<T> {
        T item;
        Node<T> prev;
        Node<T> next;

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
        sentinel.next.prev = newNode;
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
            Node<T> tailNode = sentinel.prev;
            newNode.item = item;
            tailNode.next = newNode;
            newNode.prev = tailNode;
            newNode.next = sentinel;
            sentinel.prev = newNode;
            size++;
        }
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
        for (Node temp = sentinel.next; temp != sentinel; temp = temp.next) {
            System.out.print(temp.item + " ");
        }
    }

    /**
     * remove the first element.
     */

    @Override
    public T removeFirst() {
        if (isEmpty())
            return null;
        Node<T> headNode = sentinel.next;
        sentinel.next = headNode.next;
        headNode.next.prev = sentinel;
        size--;
        return (T) headNode.item;
    }

    /**
     * remove the last element.
     */

    @Override
    public T removeLast() {
        if (isEmpty())
            return null;

        Node<T> tailNode = sentinel.prev;

        tailNode.prev.next = tailNode.next;
        sentinel.prev = tailNode.prev;
        size--;
        return (T) tailNode.item;
    }

    /**
     * get the element at an index
     */

    @Override
    public T get(int index) {

        if (index >= size || index < 0) {
            return null;
        }

        int counter = 0;
        for (Node<T> temp = sentinel.next; temp != sentinel; temp = temp.next) {
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
            Node<T> list1 = sentinel.next;
            Node<T> list2 = List.sentinel.next;

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
    private T getRecursive(int index, Node<T> node) {
        if (node == sentinel)
            return null;
        if (index == 0)
            return (T) node.item;
        return (T) getRecursive(index - 1, node.next);
    }

    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }


    @Override
    public Iterator<T> iterator() {
        return new LinkedListDeque.LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {

        int count = 0;

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public T next() {
            T value = get(count);
            count += 1;
            return value;
        }
    }
}
