package deque;

import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;

import static org.junit.Assert.*;


public class MaxArrayDequeTest {
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

    @Test
    public void MaxArrayIteratorTest() {
        // not actually a Test
        MaxArrayDeque<Integer> a = new MaxArrayDeque<>(new NumComparator());
        Iterator<Integer> iter = a.iterator();
        for (int i = 0; i < 10; i++) {
            a.addLast(i);
        }

        for (Integer item : a) {
            System.out.println(item);
        }
    }

    @Test
    public void MaxDequeMaxIntegerTest() {
        MaxArrayDeque<Integer> arr1 = (MaxArrayDeque<Integer>) MaxArrayDequeTest.of(new NumComparator(), 1, 2, 3, 4, 5, 6, 7);
        assertEquals(arr1.max(), (Integer) 7);
    }

    @Test
    public void MaxDequeMaxStringLengthTest() {
        MaxArrayDeque<String> arr1 = (MaxArrayDeque<String>) MaxArrayDequeTest.<String>of(new StringLengthComparator(), "hello", "i", "am", "spiderman");
        assertEquals(arr1.max(), "spiderman");
    }

    @Test
    public void MaxDequeMaxStringTest() {
        MaxArrayDeque<String> arr1 = (MaxArrayDeque<String>) MaxArrayDequeTest.<String>of(new StringComparator(), "hello", "i", "am", "spiderman");
        assertEquals(arr1.max(), "spiderman");
    }

    @Test
    public void MaxDequeNullTest() {
        MaxArrayDeque<Integer> arr1 = new MaxArrayDeque<>(new NumComparator());
        assertNull(arr1.max());
    }


    @Test
    public void MaxDequeMaxTest() {
        MaxArrayDeque<Integer> arr1 = new MaxArrayDeque<>(new NumComparator());
        for (int i = 0; i < 100; i++) {
            arr1.addLast(i);
        }
        assertEquals((Integer) 99, arr1.max());
    }
}
