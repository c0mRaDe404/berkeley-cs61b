package deque;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;



public class MaxArrayDequeTest {

    @Test
    public void MaxArrayIteratorTest() {
        // not actually a Test
        MaxArrayDeque<Integer> a  = new MaxArrayDeque<>(new MaxArrayDeque.NumComparator());
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
        MaxArrayDeque<Integer> arr1 = (MaxArrayDeque<Integer>) MaxArrayDeque.of(new MaxArrayDeque.NumComparator(),1, 2, 3, 4, 5, 6, 7 );
        assertEquals(arr1.max(), (Integer) 7);
    }

    @Test
    public void MaxDequeMaxStringLengthTest() {
        MaxArrayDeque<String> arr1 = (MaxArrayDeque<String>) MaxArrayDeque.<String> of(new MaxArrayDeque.StringLengthComparator(), "hello", "i", "am", "spiderman");
        assertEquals(arr1.max(), "spiderman");
    }

    @Test
    public void MaxDequeMaxStringTest() {
        MaxArrayDeque<String> arr1 = (MaxArrayDeque<String>) MaxArrayDeque.<String> of(new MaxArrayDeque.StringComparator(), "hello", "i", "am", "spiderman");
        assertEquals(arr1.max(), "spiderman");
    }
    
    @Test
    public void MaxDequeNullTest() {
        MaxArrayDeque<Integer> arr1 = new MaxArrayDeque<>(new MaxArrayDeque.NumComparator());
        assertNull(arr1.max());
    }


    @Test
    public void MaxDequeMaxTest() {
        MaxArrayDeque<Integer> arr1 = new MaxArrayDeque<>(new MaxArrayDeque.NumComparator());
        for (int i = 0; i < 100; i++) {
            arr1.addLast(i);
        }
        assertEquals((Integer) 99, arr1.max());
    }
}
