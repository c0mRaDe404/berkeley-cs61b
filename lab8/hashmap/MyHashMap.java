package hashmap;

import javax.naming.OperationNotSupportedException;
import java.security.InvalidKeyException;
import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author Bhuvanesh
 */
public class MyHashMap<K, V> implements Map61B<K, V> {


    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */

    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Collection<Node>[] buckets; // an array of buckets
    private int size; // total # of elements at any point
    private double loadFactor; // size / length <= loadFactor

    public MyHashMap() {
        this(16);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */

    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        size = 0;
        loadFactor = maxLoad;
    }

    /** check if the key is null
     * @param key
     * @return boolean
     */
    private boolean isNullKey(K key) {
        return key == null;
    }

    /** Throws an exception if the key is null
     *
     * @param key
     * @throws NullPointerException
     */
    private void checkNullKey(K key) {
        if (isNullKey(key)) {
            throw new NullPointerException();
        }
    }

    /**
     * converts an object's hashcode to a valid index
     *
     * @param key
     * @param tableSize
     * @return integer
     */
    private int getHash(K key, int tableSize) {
        checkNullKey(key);
        return (key.hashCode() & 0x7fffffff) % tableSize;
    }

    /** resizes the hash table
     * @param newSize
     */
    private void resizeTable(int newSize) {
        Collection<Node>[] oldBucket = buckets;
        buckets = createTable(newSize);
        size = 0;
        for (int i = 0; i < oldBucket.length; i++) {
            for (Node node : oldBucket[i]) {
                put(node.key, node.value);
            }
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     *
     * @param key
     * @param value
     * @return Node(Key, Value)
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects

     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i].clear();
        }
        size = 0;
    }

    /** get a bucket and check if it's already been filled
     *
     * @param bucket
     * @return boolean
     */
    private boolean containsKey(Node bucket) {
       if (bucket == null) {
           return false;
       }
       return true;
    }

    @Override
    public boolean containsKey(K key) {
        checkNullKey(key);
        Node bucket = getBucket(key);
        return containsKey(bucket);
    }

    @Override
    public V get(K key) {
        checkNullKey(key);
        Node bucket = getBucket(key);
        if (bucket == null) {
            return null;
        } else {
            return bucket.value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    /** find a bucket in the table by its key value
     * @param key
     * @return node
     */
    private Node getBucket(K key) {
        checkNullKey(key);
        int index = getHash(key, buckets.length);
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    /** creates a new bucket
     * @param key
     * @param value
     */
    private void addBucket(K key, V value) {
        int index = getHash(key, buckets.length);
        Node newNode = createNode(key, value);
        buckets[index].add(newNode);
        size++;
    }


    /** updates a bucket's value
     *
     * @param bucket
     * @param value
     */
    private void updateBucket(Node bucket, V value) {
       bucket.value = value;
    }

    @Override
    public void put(K key, V value) {
        checkNullKey(key);
        double load = (double) size / buckets.length;
        if (load >= loadFactor) {
            resizeTable((int) (buckets.length * 2));
        }

        Node bucket = getBucket(key);

        if (!containsKey(bucket)) {
            addBucket(key, value);
        } else {
            updateBucket(bucket, value);
        }

    }


    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<K>();
        for (K key : this) {
            keys.add(key);
        }
        return keys;
    }

    @Override
    public V remove(K key) {
        return remove(key, null);
    }

    /** removes a bucket
     *
      * @param key
     * @param value
     * @return value
     */
    private V removeBucket(K key, V value) {

        int index = getHash(key, buckets.length);
        Node bucket = getBucket(key);

        if (bucket.value.equals(value) || value == null) {
            buckets[index].remove(bucket);
        }

        size--;
        return bucket.value;

    }

    @Override
    public V remove(K key, V value) {
        checkNullKey(key);
        if (!containsKey(key)) {
            return null;
        }
        return removeBucket(key, value);

    }

    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        private int currentIndex;
        private Iterator<Node> iter;
        private int count;

        MyHashMapIterator() {
             currentIndex = 0;
             iter = getIter();
             count = 0;
        }

        @Override
        public boolean hasNext() {
           return count < size;
        }

        @Override
        public K next() {
            assert hasNext();
            if (!iter.hasNext()) {
                iter = getIter();
            }
            count++;
          return iter.next().key;
        }

        private Iterator<Node> getIter() {

            if (buckets[currentIndex].isEmpty()) {
                while (hasNext() && buckets[currentIndex].isEmpty()) {
                    currentIndex++;
                }
            }

            return buckets[currentIndex++].iterator();
        }
    }

}
