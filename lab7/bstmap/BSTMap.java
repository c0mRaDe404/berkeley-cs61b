package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private BSTNode root = null;
    private int size = 0;


    private class BSTNode {

        K key;
        V value;
        BSTNode left;
        BSTNode right;

        private BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
        private BSTNode(K key, V value, BSTNode left, BSTNode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(root, key) != null;
    }

    private BSTNode get(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) == 0) {
            return node;
        } else if (key.compareTo(node.key) >= 1) {
            return get(node.right, key);
        } else {
            return get(node.left, key);
        }
    }

    @Override
    public V get(K key) {
        BSTNode node = get(root, key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    /** helper method for put */
    private BSTNode put(BSTNode node, K key, V value) {

        if (node == null) {
            size += 1;
            return new BSTNode(key, value);
        }
        if (key.compareTo(node.key) == 0) {
            node.value = value;
        } else if (key.compareTo(node.key) >= 1) {
            node.right = put(node.right, key, value);
        } else {
            node.left = put(node.left, key, value);
        }

        return node;
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }


    /** helper for printInOrder */
    private void printInOrder(BSTNode node) {
        if (node == null) {
            return;
        }

        printInOrder(node.left);
        System.out.print(node.key + "(" + node.value + ")"+ " ");
        printInOrder(node.right);
    }

    /** print BST inorder */
    public void printInOrder() {
        printInOrder(root);
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (K key : this) {
            set.add(key);
        }
        return set;
    }

    /** checks if a node is a leaf */
    private boolean isLeaf(BSTNode node) {
        return node.left == null && node.right == null;
    }

    private BSTNode deleteMin(BSTNode root, BSTNode curr) {
       if (curr.left == null) {
          root.value = curr.value;
          root.key = curr.key;
          return remove(curr.right, curr.key, null);
       } else {
           curr.left = deleteMin(root, curr.left);
       }
       return curr;
    }

    /** helper for helper of the remove */
    private BSTNode removeNode(BSTNode node, K key, V value) {
        if (isLeaf(node)) { // if it's a leaf, return null;
            return null;
        } else if (node.left == null) { // if left is empty, return right;
            return node.right;
        } else if (node.right == null) { // if right is empty, return left;
            return node.left;
        } else { // otherwise replace
            node.right =  deleteMin(node, node.right);
            return node;
        }
    }
    /** helper method for remove */
    private BSTNode remove(BSTNode node, K key, V value) {
        if (node == null) {
            size -= 1;
            return null;
        }
        if (key.compareTo(node.key) == 0) {
            return removeNode(node, key, value); // call delete once the key is found
        } else if (key.compareTo(node.key) >= 1) {
            BSTNode temp;
            temp = remove(node.right, key, value);
            node.right = temp;
        } else {
            BSTNode temp;
            temp = remove(node.left, key, value);
            node.left = temp;
        }

        return node;

    }

    @Override
    public V remove(K key) {
       return remove(root, key, null).value;
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    private class BSTMapIterator implements Iterator<K> {
        Deque<BSTNode> stack;

        BSTMapIterator() {
            stack =  new LinkedList<>();
            if (root != null) {
                stack.push(root);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            assert hasNext();
            BSTNode cur = stack.pop();
            if (cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.left != null) {
                stack.push(cur.left);
            }
            return cur.key;
        }
    }


}
