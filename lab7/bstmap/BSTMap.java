package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private  BSTNode root = null;
    private int size = 0;
    private V deleted = null;

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

    /** get the node matches the key and returns it */
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

    /** find the minimum of rtree, replace it with curr and delete the minimum */
    private BSTNode removeMinAndReplace(BSTNode curr, BSTNode rtree) {

        if (rtree.left == null) {
            curr.key = rtree.key;
            curr.value = rtree.value;
            return rtree.right;
        }

       rtree.left  = removeMinAndReplace(curr, rtree.left);
       return rtree;
    }

    /** removes a bst node */
    private BSTNode bstRemoveNode(BSTNode curr) {
        deleted = curr.value;
        size--;
       if(isLeaf(curr)) {
          return null;
       } else if (curr.left == null) {
           return curr.right;
       } else if (curr.right == null) {
          return curr.left;
       } else {
          curr.right = removeMinAndReplace(curr, curr.right);
          return curr;
       }
    }

    private BSTNode removeNode(BSTNode curr, K key, V value) {
        if (curr == null) {
            return null;
        }

        if (key.compareTo(curr.key) == 0) {
             // need to trigger remove over here;
             if (value != null && !value.equals(curr.value)) {
                return curr;
             }
             return bstRemoveNode(curr);
        } else if (key.compareTo(curr.key) >= 1) {
            curr.right = removeNode(curr.right, key, value);
        } else {
            curr.left = removeNode(curr.left, key, value);
        }

        return curr;
    }


    @Override
    public V remove(K key) {
        root = removeNode(root, key, null);
        return deleted;
    }

    @Override
    public V remove(K key, V value) {
        root  = removeNode(root, key, value);
        return deleted;
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
