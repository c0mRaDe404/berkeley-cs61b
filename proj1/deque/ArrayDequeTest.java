package deque;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Performs some basic linked list tests.
 */
public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        ArrayDeque<String> lld1 = new ArrayDeque<String>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeFirst();
        // should be empty
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create ArrayDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String> lld1 = new ArrayDeque<String>();
        ArrayDeque<Double> lld2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty ArrayDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigArrayDequeTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999; i > 500; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

    }


    @Test
    public void getTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000; i++) {
            lld1.addLast(i);
        }

        int value = lld1.get(999);

        assertEquals(999, value);
    }

    @Test
    public void equalsTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        ArrayDeque<Integer> lld2 = new ArrayDeque<Integer>();

        for (int i = 0; i < 1000; i++) {
            lld1.addLast(i);
            lld2.addLast(i);
        }
        assertTrue(lld1.equals(lld2));

        lld1 = new ArrayDeque<Integer>();
        lld2 = new ArrayDeque<Integer>();

        for (int i = 0; i < 1000; i++) {
            lld1.addLast(i);
            if (i > 500) continue;
            lld2.addLast(i);
        }
        assertEquals(false, lld1.equals(lld2));


        lld1 = new ArrayDeque<Integer>();
        lld2 = new ArrayDeque<Integer>();

        for (int i = 0; i < 1000; i++) {
            lld1.addLast(i);
            if (i > 1) continue;
            lld2.addLast(i);
        }
        assertEquals(false, lld1.equals(lld2));

        lld1 = new ArrayDeque<Integer>();
        lld2 = new ArrayDeque<Integer>();

        for (int i = 0; i < 1000; i++) {
            if (i > 200)
                lld1.addLast(i);
            if (i > 300)
                lld2.addLast(i);

        }
        assertFalse(lld1.equals(lld2));
    }

    @Test
    public void ArrayDequeAndLinkedListDequeTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();
        for (int i = 0; i < 10; i++) {
            lld1.addLast(i);
            lld2.addLast(i);
        }

        for (double i = 0; i < 5; i++) {
            assertEquals("Should have the same value", (int) lld2.removeFirst(), (int) lld1.removeFirst(), 0.0);
        }

        for (double i = 9; i > 5; i--) {
            assertEquals("Should have the same value", (int) lld2.removeLast(), (int) lld1.removeLast(), 0.0);
        }

    }

    @Test
    public void testRemoveFirst() {
        ArrayDeque<String> dq = new ArrayDeque<>();
        dq.addLast("A");
        dq.addLast("B");
        dq.addLast("C");

        assertEquals("A", dq.removeFirst());
        assertEquals("B", dq.removeFirst());
        assertEquals("C", dq.removeFirst());
        assertTrue(dq.isEmpty());
    }

    @Test
    public void testRemoveLast() {
        ArrayDeque<String> dq = new ArrayDeque<>();
        dq.addLast("A");
        dq.addLast("B");
        dq.addLast("C");

        assertEquals("C", dq.removeLast());
        assertEquals("B", dq.removeLast());
        assertEquals("A", dq.removeLast());
        assertTrue(dq.isEmpty());
    }

    @Test
    public void testRemoveFromEmptyReturnsNull() {
        ArrayDeque<String> dq = new ArrayDeque<>();
        assertNull(dq.removeFirst());
        assertNull(dq.removeLast());
    }

    @Test
    public void testRemoveUntilEmpty() {
        ArrayDeque<String> dq = new ArrayDeque<>();
        dq.addFirst("A");
        dq.addFirst("B");

        assertEquals("B", dq.removeFirst());
        assertEquals("A", dq.removeFirst());
        assertNull(dq.removeFirst());  // Empty now
    }


    @Test
    public void testResizeOnAddFirst() {
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            dq.addFirst(i);
        }

        assertEquals(20, dq.size());
        assertEquals(19, (int) dq.get(0));  // Last added is first
        assertEquals(0, (int) dq.get(19));  // First added is last
    }

    @Test
    public void testResizeOnAddLast() {
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            dq.addLast(i);
        }

        assertEquals(20, dq.size());
        assertEquals(0, (int) dq.get(0));
        assertEquals(19, (int) dq.get(19));
    }

    @Test
    public void testResizeShrinkOnRemove() {
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            dq.addLast(i);
        }

        for (int i = 0; i < 15; i++) {
            dq.removeFirst();
        }

        assertEquals(5, dq.size());
        // Should have shrunk when size < capacity/4
    }

    @Test
    public void testResizeAlternatingAddRemove() {
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                dq.addFirst(i);
            } else {
                dq.addLast(i);
            }
        }

        // Just verify no exceptions and size is correct
        assertEquals(100, dq.size());
    }


    @Test
    public void removeComparisonTest() {
        LinkedListDeque<Integer> list1 = new LinkedListDeque<>();
        ArrayDeque<Integer> list2 = new ArrayDeque<>();

        for (int i = 0; i < 100; i++) {
            list1.addFirst(i);
            list2.addFirst(i);
        }

    }


    @Test
    public void doRandomizedTest() {
        int iterations = 200;
        for (int i = 0; i < iterations; i++) {
            randomizedTest();
        }
    }

    @Test
    public void randomizedTest() {
        LinkedListDeque<Integer> list1 = new LinkedListDeque<>();
        ArrayDeque<Integer> list2 = new ArrayDeque<>();

        int N = 200;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                list1.addLast(randVal);
                list2.addLast(randVal);
                //System.out.println("addLast("+randVal+")");
                Assert.assertEquals("Operation 0 failed!", list1.get(list1.size() - 1), list2.get(list2.size() - 1));

            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                list1.addFirst(randVal);
                list2.addFirst(randVal);
                //System.out.println("addFirst("+randVal+")");
                Assert.assertEquals("Operation 1 failed!", list1.get(0), list2.get(0));

            } else if (operationNumber == 2) {
                if (list1.size() > 0 && list2.size() > 0) {
                    //System.out.println("removeFirst()");
                    Assert.assertEquals("Operation 2 failed!", list1.removeFirst(), list2.removeFirst());
                }
            } else if (operationNumber == 3) {
                if (list1.size() > 0 && list2.size() > 0) {
                    //System.out.println("removeLast()");
                    Assert.assertEquals("Operation 3 failed!", list1.removeLast(), list2.removeLast());
                }
            }
        }

    }

    @Test
    public void arrayDequeIteratorTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        ArrayDeque<Integer> lld2 = new ArrayDeque<Integer>();

        for (int i = 0; i < 10; i++) {
            lld1.addLast(i);
            lld2.addLast(i);
        }

       for (Integer value : lld1) {
           assertEquals(value, lld2.removeFirst());
       }

    }
}

