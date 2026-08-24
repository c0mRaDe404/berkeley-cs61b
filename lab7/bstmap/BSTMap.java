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



    private void printInOrder(BSTNode node) {
        if (node == null) {
            return;
        }

        printInOrder(node.left);
        System.out.print(node.key + "(" + node.value + ")"+ " ");
        printInOrder(node.right);
    }

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

   private boolean isLeaf(BSTNode node) {
        return node.left == null && node.right == null;
   }

    private BSTNode min(BSTNode node) {
       BSTNode temp = node;
       while (temp != null) {
           temp = temp.left;
       }
       return temp;
    }

    private BSTNode max(BSTNode node) {
        BSTNode temp = node;
        while (temp != null) {
            temp = temp.right;
        }
        return temp;
    }


    private V remove(BSTNode node, K key, V value) {
        if (node == null) {
            size -= 1;
            return null;
        }
        if (key.compareTo(node.key) == 0) {
           if (isLeaf(node)) {
               return null;
           } else {
             BSTNode predecessor = max(node.left);
             if(predecessor != null) {

             } else {
                 BSTNode successor = min(node.right);

             }
           }
        } else if (key.compareTo(node.key) >= 1) {
            node.right.value = remove(node.right, key, value);
        } else {
            node.left.value = remove(node.left, key, value);
        }

        return node;

    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
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
