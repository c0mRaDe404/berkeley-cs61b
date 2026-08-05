package deque;

import org.apache.commons.collections.comparators.NullComparator;

import java.util.Comparator;

class MaxArrayDeque<T> extends ArrayDeque<T> {

    Comparator<T> c;

    public MaxArrayDeque (Comparator<T> c) {
        this.c = c;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        } else {
            T max = get(0);
            for (int i = 1; i < size(); i++) {
                int result = c.compare(max, get(i));
                if (result < 0) {
                    max = get(i);
                }
            }
            return max;
        }
    }

    public T max() {
        return max(c);
    }


   public static class NumComparator implements Comparator<Integer> {
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public static class StringComparator implements Comparator<String> {
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    public static class StringLengthComparator implements Comparator<String> {
        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }
    }

    public static <T> Deque<T> of(Comparator<T> c, T... values) {
        MaxArrayDeque<T> deque = new MaxArrayDeque<>(c);
        for (T value : values) {
            deque.addLast(value);
        }
        return deque;
    }
}
