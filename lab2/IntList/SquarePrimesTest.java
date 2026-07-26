package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */

    @Test
    public void testSquarePrimes1() {
        IntList lst = IntList.of(5, 6, 7, 10, 11, 247, 213, 13);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("25 -> 6 -> 49 -> 10 -> 121 -> 247 -> 213 -> 169", lst.toString());
        assertTrue(changed);

    }

    @Test
    public void testSquarePrimes2() {
        IntList lst = IntList.of(23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("529 -> 841 -> 961 -> 1369 -> 1681 -> 1849 -> 2209 -> 2809 -> 3481 -> 3721 -> 4489 -> 5041 -> 5329 -> 6241 -> 6889 -> 7921 -> 9409", lst.toString());
        assertTrue(changed);

    }

    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }
}
